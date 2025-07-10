package com.acerap.musicstore.infrastructure.persistence;

import com.acerap.musicstore.domain.enums.MusicGenre;
import com.acerap.musicstore.domain.model.CompactDisc;
import com.acerap.musicstore.domain.model.Track;
import com.acerap.musicstore.domain.ports.out.ArtistRepository;
import com.acerap.musicstore.domain.ports.out.CompactDiscRepository;
import com.acerap.musicstore.domain.ports.out.RecordCompanyRepository;
import com.acerap.musicstore.domain.valueobjects.Duration;
import com.acerap.musicstore.domain.valueobjects.Money;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcCompactDiscRepository implements CompactDiscRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ArtistRepository artistRepository;
    private final RecordCompanyRepository recordCompanyRepository;

    public JdbcCompactDiscRepository(JdbcTemplate jdbcTemplate,
                                     ArtistRepository artistRepository,
                                     RecordCompanyRepository recordCompanyRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.artistRepository = artistRepository;
        this.recordCompanyRepository = recordCompanyRepository;
    }

    @Override
    public CompactDisc save(CompactDisc disc) {
        if (disc.getGlobalId() == null) {
            // Insert
            String insertSql = """
                INSERT INTO compact_discs 
                (global_id, store_code, title, price_amount, price_currency, 
                 artist_id, production_year, record_company_name, stock) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

            UUID newId = UUID.randomUUID();
            jdbcTemplate.update(insertSql,
                    newId,
                    disc.getStoreCode(),
                    disc.getTitle(),
                    disc.getPrice().amount(),
                    disc.getPrice().currency().getCurrencyCode(),
                    disc.getArtist().getId(),
                    disc.getProductionYear(),
                    disc.getRecordCompany().getName(),
                    disc.getStock());

            disc.setGlobalId(newId);
        } else {
            // Update
            String updateSql = """
                UPDATE compact_discs SET 
                store_code = ?, title = ?, price_amount = ?, price_currency = ?,
                artist_id = ?, production_year = ?, record_company_name = ?, stock = ?
                WHERE global_id = ?
                """;

            jdbcTemplate.update(updateSql,
                    disc.getStoreCode(),
                    disc.getTitle(),
                    disc.getPrice().amount(),
                    disc.getPrice().currency().getCurrencyCode(),
                    disc.getArtist().getId(),
                    disc.getProductionYear(),
                    disc.getRecordCompany().getName(),
                    disc.getStock(),
                    disc.getGlobalId());
        }

        saveTracks(disc);
        return disc;
    }

    @Override
    public Optional<CompactDisc> findById(UUID id) {
        String sql = "SELECT * FROM compact_discs WHERE global_id = ?";
        List<CompactDisc> results = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            try {
                return mapRowToCompactDisc(rs);
            } catch (SQLException e) {
                throw new DataAccessException("Error mapping row to CompactDisc", e) {};
            }
        });

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<CompactDisc> findByGenre(MusicGenre genre) {
        String sql = """
            SELECT cd.* FROM compact_discs cd
            JOIN artists a ON cd.artist_id = a.id
            WHERE a.genre = ?
            """;
        return jdbcTemplate.query(sql, new Object[]{genre.name()}, (rs, rowNum) -> {
            try {
                return mapRowToCompactDisc(rs);
            } catch (SQLException e) {
                throw new DataAccessException("Error mapping row to CompactDisc", e) {};
            }
        });
    }

    @Override
    public List<CompactDisc> findByArtistName(String artistName) {
        String sql = """
            SELECT cd.* FROM compact_discs cd
            JOIN artists a ON cd.artist_id = a.id
            WHERE a.name = ?
            """;
        return jdbcTemplate.query(sql, new Object[]{artistName}, (rs, rowNum) -> {
            try {
                return mapRowToCompactDisc(rs);
            } catch (SQLException e) {
                throw new DataAccessException("Error mapping row to CompactDisc", e) {};
            }
        });
    }

    @Override
    public void updateStock(UUID discId, int quantity) {
        String sql = "UPDATE compact_discs SET stock = ? WHERE global_id = ?";
        jdbcTemplate.update(sql, quantity, discId);
    }

    @Override
    public boolean existsByStoreCode(String storeCode) {
        String sql = "SELECT COUNT(*) FROM compact_discs WHERE store_code = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, storeCode);
        return count != null && count > 0;
    }

    @Override
    public List<CompactDisc> findByTitleContainingIgnoreCase(String title) {
        String sql = "SELECT * FROM compact_discs WHERE LOWER(title) LIKE LOWER(?)";
        return jdbcTemplate.query(sql, new Object[]{"%" + title + "%"}, (rs, rowNum) -> {
            try {
                return mapRowToCompactDisc(rs);
            } catch (SQLException e) {
                throw new DataAccessException("Error mapping row to CompactDisc", e) {};
            }
        });
    }

    @Override
    public List<CompactDisc> findByStockLessThan(int threshold) {
        String sql = "SELECT * FROM compact_discs WHERE stock < ?";
        return jdbcTemplate.query(sql, new Object[]{threshold}, (rs, rowNum) -> {
            try {
                return mapRowToCompactDisc(rs);
            } catch (SQLException e) {
                throw new DataAccessException("Error mapping row to CompactDisc", e) {};
            }
        });
    }

    private CompactDisc mapRowToCompactDisc(ResultSet rs) throws SQLException {
        CompactDisc disc = new CompactDisc();
        disc.setGlobalId(UUID.fromString(rs.getString("global_id")));
        disc.setStoreCode(rs.getString("store_code"));
        disc.setTitle(rs.getString("title"));

        // Manejo seguro del dinero
        BigDecimal amount = rs.getBigDecimal("price_amount");
        String currencyCode = rs.getString("price_currency");
        if (amount != null && currencyCode != null) {
            disc.setPrice(new Money(amount, Currency.getInstance(currencyCode)));
        }

        // Cargar artista si existe
        String artistId = rs.getString("artist_id");
        if (artistId != null) {
            artistRepository.findById(UUID.fromString(artistId))
                    .ifPresent(disc::setArtist);
        }

        // Cargar compañía discográfica si existe
        String companyName = rs.getString("record_company_name");
        if (companyName != null) {
            recordCompanyRepository.findByName(companyName)
                    .ifPresent(disc::setRecordCompany);
        }

        disc.setProductionYear(rs.getInt("production_year"));
        disc.setStock(rs.getInt("stock"));

        // Cargar pistas bajo demanda
        loadTracks(disc);

        return disc;
    }

    private void saveTracks(CompactDisc disc) {
        // Eliminar todas las pistas existentes para este disco
        String deleteSql = "DELETE FROM tracks WHERE disc_id = ?";
        jdbcTemplate.update(deleteSql, disc.getGlobalId());

        // Insertar las pistas actuales
        String insertSql = "INSERT INTO tracks (disc_id, position, title, duration_minutes, duration_seconds) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(insertSql, disc.getTracks(), disc.getTracks().size(),
                (ps, track) -> {
                    ps.setObject(1, disc.getGlobalId());
                    ps.setInt(2, track.getPosition());
                    ps.setString(3, track.getTitle());
                    ps.setInt(4, track.getDuration().minutes());
                    ps.setInt(5, track.getDuration().seconds());
                });
    }

    private void loadTracks(CompactDisc disc) {
        String sql = """
            SELECT position, title, duration_minutes, duration_seconds
            FROM tracks WHERE disc_id = ? ORDER BY position
            """;

        List<Track> tracks = jdbcTemplate.query(sql, (rs, rowNum) -> {
            try {
                return new Track(
                        rs.getInt("position"),
                        rs.getString("title"),
                        new Duration(
                                rs.getInt("duration_minutes"),
                                rs.getInt("duration_seconds")
                        )
                );
            } catch (SQLException e) {
                throw new DataAccessException("Error mapping track", e) {};
            }
        }, disc.getGlobalId());

        disc.setTracks(tracks);
    }
}