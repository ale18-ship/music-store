package com.acerap.musicstore.domain.ports.in;

import com.acerap.musicstore.domain.model.Collection;
import com.acerap.musicstore.domain.valueobjects.Money;

import java.util.UUID;

public interface CollectionService {
    Collection createCollection(Collection collection);
    Collection findCollectionById(UUID id);
    Money calculateDiscount(UUID collectionId);
}
