package com.acerap.musicstore.infrastructure.persistence;

import com.acerap.musicstore.domain.model.RecordCompany;
import com.acerap.musicstore.domain.ports.out.RecordCompanyRepository;
import com.acerap.musicstore.domain.valueobjects.Address;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcRecordCompanyRepository implements RecordCompanyRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcRecordCompanyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public RecordCompany save(RecordCompany company) {
        if (findByName(company.getName()).isEmpty()) {
            String sql = "INSERT INTO record_companies (name, street, number, postal_code, country, headquarters_country) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    company.getName(),
                    company.getAddress().street(),
                    company.getAddress().number(),
                    company.getAddress().postalCode(),
                    company.getAddress().country(),
                    company.getHeadquartersCountry());
        }
        return company;
    }

    @Override
    public Optional<RecordCompany> findByName(String name) {
        String sql = "SELECT * FROM record_companies WHERE name = ?";
        try {
            RecordCompany company = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{name}, // Usar array de parámetros
                    (rs, rowNum) -> mapRowToRecordCompany(rs) // Implementar RowMapper como lambda
            );
            return Optional.ofNullable(company);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RecordCompany mapRowToRecordCompany(ResultSet rs) throws SQLException {
        return new RecordCompany(
                rs.getString("name"),
                new Address(
                        rs.getString("street"),
                        rs.getString("number"),
                        rs.getString("postal_code"),
                        rs.getString("country")
                ),
                rs.getString("headquarters_country")
        );
    }
}
