package com.acerap.musicstore.domain.ports.out;

import com.acerap.musicstore.domain.model.Artist;

import java.util.Optional;
import java.util.UUID;

public interface ArtistRepository {
    Artist save(Artist artist);
    Optional<Artist> findById(UUID id);
    Optional<Artist> findByName(String name);
}