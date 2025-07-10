package com.acerap.musicstore.infrastructure.persistence;

import com.acerap.musicstore.domain.model.RecordCompany;
import com.acerap.musicstore.domain.valueobjects.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JdbcRecordCompanyRepositoryTest {

    @Autowired
    private JdbcRecordCompanyRepository recordCompanyRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void clearDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE record_companies CASCADE");
    }

    @Test
    void save_shouldInsertNewRecordCompany() {
        RecordCompany company = new RecordCompany(
                "Sony Music",
                new Address("Main St", "100", "10001", "USA"),
                "Japan"
        );

        RecordCompany saved = recordCompanyRepository.save(company);

        assertNotNull(saved);
        assertEquals("Sony Music", saved.getName());
    }

    @Test
    void findByName_shouldReturnCompany() {
        RecordCompany company = new RecordCompany(
                "Warner Music",
                new Address("Broadway", "200", "10002", "USA"),
                "USA"
        );
        recordCompanyRepository.save(company);

        Optional<RecordCompany> found = recordCompanyRepository.findByName("Warner Music");

        assertTrue(found.isPresent());
        assertEquals("Broadway", found.get().getAddress().street());
    }

    @Test
    void findByName_shouldReturnEmptyForNonExistent() {
        Optional<RecordCompany> found = recordCompanyRepository.findByName("No Existe");
        assertTrue(found.isEmpty());
    }
}