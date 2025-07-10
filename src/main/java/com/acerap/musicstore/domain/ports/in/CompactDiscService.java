package com.acerap.musicstore.domain.ports.in;

import com.acerap.musicstore.domain.enums.MusicGenre;
import com.acerap.musicstore.domain.model.CompactDisc;
import com.acerap.musicstore.domain.model.Track;
import com.acerap.musicstore.domain.valueobjects.Money;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompactDiscService {
    CompactDisc createDisc(CompactDisc disc);
    Optional<CompactDisc> findDiscById(UUID id);
    List<CompactDisc> findDiscsByGenre(MusicGenre genre);
    List<CompactDisc> findDiscsByArtist(String artistName);
    List<CompactDisc> findDiscsByTitle(String title);
    void updateStock(UUID discId, int quantity);
    void reduceStock(UUID discId, int quantity);
    void increaseStock(UUID discId, int quantity);
    List<CompactDisc> findLowStockDiscs(int threshold);
    CompactDisc updateDiscPrice(UUID discId, Money newPrice);
    void addTrackToDisc(UUID discId, Track track);
    void removeTrackFromDisc(UUID discId, int trackPosition);
}
