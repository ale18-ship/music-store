package com.acerap.musicstore.infrastructure.persistence;

import com.acerap.musicstore.domain.enums.MusicGenre;
import com.acerap.musicstore.domain.model.Artist;
import com.acerap.musicstore.domain.model.CompactDisc;
import com.acerap.musicstore.domain.model.RecordCompany;
import com.acerap.musicstore.domain.model.Track;
import com.acerap.musicstore.domain.ports.out.ArtistRepository;
import com.acerap.musicstore.domain.ports.out.RecordCompanyRepository;
import com.acerap.musicstore.domain.valueobjects.Address;
import com.acerap.musicstore.domain.valueobjects.Duration;
import com.acerap.musicstore.domain.valueobjects.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JdbcCompactDiscRepositoryTest extends AbstractPostgreSQLTest {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private RecordCompanyRepository recordCompanyRepository;

    private JdbcCompactDiscRepository discRepository;

    private Artist testArtist;
    private RecordCompany testCompany;

    @BeforeEach
    void setUpRepositories() {
        // Inicializar repositorios
        artistRepository = new JdbcArtistRepository(jdbcTemplate);
        recordCompanyRepository = new JdbcRecordCompanyRepository(jdbcTemplate);
        discRepository = new JdbcCompactDiscRepository(jdbcTemplate, artistRepository, recordCompanyRepository);

        // Crear y persistir artista de prueba
        testArtist = new Artist();
        testArtist.setName("Test Artist");
        testArtist.setCountry("US");
        testArtist.setGenre(MusicGenre.ROCK);
        testArtist.setMembers(List.of("Member1", "Member2"));
        testArtist = artistRepository.save(testArtist); // Guardar y obtener con ID

        // Crear y persistir compañía de prueba
        testCompany = new RecordCompany(
                "Test Records",
                new Address("Test St", "123", "28001", "Spain"),
                "US"
        );
        recordCompanyRepository.save(testCompany);

        // Verificar que los datos existen
        assertNotNull(testArtist.getId());
        assertTrue(artistRepository.findById(testArtist.getId()).isPresent());
        assertTrue(recordCompanyRepository.findByName(testCompany.getName()).isPresent());
    }

    @Test
    void save_shouldInsertNewDisc() {
        CompactDisc disc = createTestDisc();

        CompactDisc savedDisc = discRepository.save(disc);

        assertNotNull(savedDisc.getGlobalId());
        assertEquals(disc.getTitle(), savedDisc.getTitle());
        assertEquals(2, savedDisc.getTracks().size());
    }

    @Test
    void findById_shouldReturnDiscWithTracks() {
        CompactDisc disc = createTestDisc();
        discRepository.save(disc);

        Optional<CompactDisc> foundDisc = discRepository.findById(disc.getGlobalId());

        assertTrue(foundDisc.isPresent());
        assertEquals(disc.getTitle(), foundDisc.get().getTitle());
        assertEquals(2, foundDisc.get().getTracks().size());
    }

    @Test
    void findById_shouldReturnEmptyForNonExistentId() {
        Optional<CompactDisc> foundDisc = discRepository.findById(UUID.randomUUID());

        assertTrue(foundDisc.isEmpty());
    }

    @Test
    void findByGenre_shouldReturnDiscsOfGivenGenre() {
        CompactDisc disc = createTestDisc();
        discRepository.save(disc);

        List<CompactDisc> discs = discRepository.findByGenre(MusicGenre.ROCK);

        assertFalse(discs.isEmpty());
        assertEquals(disc.getTitle(), discs.get(0).getTitle());
    }

    @Test
    void findByArtistName_shouldReturnDiscsByArtist() {
        CompactDisc disc = createTestDisc();
        discRepository.save(disc);

        List<CompactDisc> discs = discRepository.findByArtistName("Test Artist");

        assertFalse(discs.isEmpty());
        assertEquals(disc.getTitle(), discs.get(0).getTitle());
    }

    @Test
    void updateStock_shouldModifyStockQuantity() {
        CompactDisc disc = createTestDisc();
        discRepository.save(disc);

        discRepository.updateStock(disc.getGlobalId(), 50);

        Optional<CompactDisc> updatedDisc = discRepository.findById(disc.getGlobalId());
        assertTrue(updatedDisc.isPresent());
        assertEquals(50, updatedDisc.get().getStock());
    }

    @Test
    void existsByStoreCode_shouldReturnTrueForExistingCode() {
        CompactDisc disc = createTestDisc();
        discRepository.save(disc);

        boolean exists = discRepository.existsByStoreCode("TEST-001");

        assertTrue(exists);
    }

    @Test
    void findByTitleContainingIgnoreCase_shouldFindMatchingDiscs() {
        CompactDisc disc = createTestDisc();
        discRepository.save(disc);

        List<CompactDisc> discs = discRepository.findByTitleContainingIgnoreCase("test");

        assertFalse(discs.isEmpty());
        assertEquals(disc.getTitle(), discs.get(0).getTitle());
    }

    @Test
    void findByStockLessThan_shouldReturnDiscsWithLowStock() {
        CompactDisc disc1 = createTestDisc();
        disc1.setStock(5);
        discRepository.save(disc1);

        CompactDisc disc2 = createTestDisc();
        disc2.setStoreCode("TEST-002");
        disc2.setStock(20);
        discRepository.save(disc2);

        List<CompactDisc> lowStockDiscs = discRepository.findByStockLessThan(10);

        assertEquals(1, lowStockDiscs.size());
        assertEquals(disc1.getStoreCode(), lowStockDiscs.get(0).getStoreCode());
    }

    private CompactDisc createTestDisc() {
        CompactDisc disc = new CompactDisc();
        disc.setStoreCode("TEST-001"); // + UUID.randomUUID().toString().substring(0, 4));
        disc.setTitle("Test Disc");
        disc.setPrice(new Money(BigDecimal.valueOf(19.99), Currency.getInstance("EUR")));
        disc.setArtist(testArtist); // Usar artista persistido
        disc.setProductionYear(2023);
        disc.setRecordCompany(testCompany); // Usar compañía persistida
        disc.setStock(10);

        Track track1 = new Track(1, "Test Track 1", new Duration(3, 45));
        Track track2 = new Track(2, "Test Track 2", new Duration(4, 20));

        disc.setTracks(List.of(track1, track2));
        return disc;
    }
}
