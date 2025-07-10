package com.acerap.musicstore.infrastructure.persistence;

import com.acerap.musicstore.domain.enums.MusicGenre;
import com.acerap.musicstore.domain.model.Artist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JdbcArtistRepositoryTest {

    @Autowired
    private JdbcArtistRepository artistRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void clearDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE artists CASCADE");
    }

    @Test
    void findByName_shouldReturnArtist() {
        // Crear artista con nombre único
        String uniqueName = "Test Artist " + UUID.randomUUID();
        Artist artist = new Artist();
        artist.setName(uniqueName);
        artist.setCountry("US");
        artist.setGenre(MusicGenre.ROCK);
        artist.setMembers(List.of("Member1", "Member2"));

        artistRepository.save(artist);

        // Buscar por el nombre único
        Optional<Artist> foundArtist = artistRepository.findByName(uniqueName);

        assertTrue(foundArtist.isPresent());
        assertEquals(artist.getId(), foundArtist.get().getId());
    }

    @Test
    void findByName_shouldReturnEmptyWhenNotFound() {
        Optional<Artist> foundArtist = artistRepository.findByName("Non Existent Artist");
        assertTrue(foundArtist.isEmpty());
    }

    @Test
    void save_shouldInsertNewArtist() {
        Artist artist = new Artist();
        artist.setName("Unique Artist " + UUID.randomUUID());
        artist.setCountry("UK");
        artist.setGenre(MusicGenre.POP);
        artist.setMembers(List.of("Member1")); // Lista inicializada con al menos un miembro

        Artist savedArtist = artistRepository.save(artist);

        assertNotNull(savedArtist.getId());
        assertEquals("UK", savedArtist.getCountry());
        assertFalse(savedArtist.getMembers().isEmpty()); // Verificación adicional
    }
}