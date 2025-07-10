package com.acerap.musicstore.infrastructure.persistence;

import com.acerap.musicstore.domain.enums.MusicGenre;
import com.acerap.musicstore.domain.model.Artist;
import com.acerap.musicstore.domain.ports.out.ArtistRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcArtistRepository implements ArtistRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcArtistRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Artist save(Artist artist) {
        if (artist.getId() == null) {
            // Insert
            String sql = """
                INSERT INTO artists (id, name, country, genre, members)
                VALUES (?, ?, ?, ?, ?)
                """;
            UUID newId = UUID.randomUUID();
            jdbcTemplate.update(sql, ps -> {
                ps.setObject(1, newId);
                ps.setString(2, artist.getName());
                ps.setString(3, artist.getCountry());
                ps.setString(4, artist.getGenre().name());
                ps.setArray(5, ps.getConnection().createArrayOf("text",
                        artist.getMembers().toArray()));
            });
            artist.setId(newId);
        } else {
            // Update
            String sql = """
                UPDATE artists SET 
                name = ?, country = ?, genre = ?, members = ?
                WHERE id = ?
                """;
            jdbcTemplate.update(sql, ps -> {
                ps.setString(1, artist.getName());
                ps.setString(2, artist.getCountry());
                ps.setString(3, artist.getGenre().name());
                ps.setArray(4, ps.getConnection().createArrayOf("text",
                        artist.getMembers().toArray()));
                ps.setObject(5, artist.getId());
            });
        }
        return artist;
    }

    @Override
    public Optional<Artist> findById(UUID id) {
        String sql = "SELECT * FROM artists WHERE id = ?";
        try {
            Artist artist = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{id},
                    (rs, rowNum) -> mapRowToArtist(rs)
            );
            return Optional.ofNullable(artist);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Artist> findByName(String name) {
        String sql = "SELECT * FROM artists WHERE name = ?";
        try {
            Artist artist = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{name},
                    (rs, rowNum) -> mapRowToArtist(rs)
            );
            return Optional.ofNullable(artist);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Artist mapRowToArtist(ResultSet rs) throws SQLException {
        Artist artist = new Artist();
        artist.setId(UUID.fromString(rs.getString("id")));
        artist.setName(rs.getString("name"));
        artist.setCountry(rs.getString("country"));
        artist.setGenre(MusicGenre.valueOf(rs.getString("genre")));

        // Manejo de miembros (array PostgreSQL)
        java.sql.Array membersArray = rs.getArray("members");
        if (membersArray != null) {
            String[] members = (String[]) membersArray.getArray();
            artist.setMembers(List.of(members));
        }

        return artist;
    }
}
