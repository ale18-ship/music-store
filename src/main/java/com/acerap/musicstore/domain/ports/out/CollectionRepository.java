package com.acerap.musicstore.domain.ports.out;

import com.acerap.musicstore.domain.model.Collection;

import java.util.Optional;
import java.util.UUID;

public interface CollectionRepository {
    Collection save(Collection collection);
    Optional<Collection> findById(UUID id);
}
