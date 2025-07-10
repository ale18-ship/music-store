package com.acerap.musicstore.domain.ports.out;

import com.acerap.musicstore.domain.enums.MusicGenre;
import com.acerap.musicstore.domain.model.CompactDisc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompactDiscRepository {
    CompactDisc save(CompactDisc disc);
    Optional<CompactDisc> findById(UUID id);
    List<CompactDisc> findByGenre(MusicGenre genre);
    List<CompactDisc> findByArtistName(String artistName);
    void updateStock(UUID discId, int quantity);
    boolean existsByStoreCode(String storeCode);
    List<CompactDisc> findByTitleContainingIgnoreCase(String title);
    List<CompactDisc> findByStockLessThan(int threshold);
}
