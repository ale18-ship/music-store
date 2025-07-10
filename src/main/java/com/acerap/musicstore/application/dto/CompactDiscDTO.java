package com.acerap.musicstore.application.dto;

import com.acerap.musicstore.domain.model.CompactDisc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CompactDiscDTO(
        UUID globalId,
        String storeCode,
        String title,
        BigDecimal price,
        String currency,
        String artist,
        int productionYear,
        String recordCompany,
        List<TrackDTO> tracks,
        int stock
) {
    public static CompactDiscDTO fromDomain(CompactDisc disc) {
        return new CompactDiscDTO(
                disc.getGlobalId(),
                disc.getStoreCode(),
                disc.getTitle(),
                disc.getPrice().amount(),
                disc.getPrice().currency().getCurrencyCode(),
                disc.getArtist().getName(),
                disc.getProductionYear(),
                disc.getRecordCompany().getName(),
                disc.getTracks().stream().map(TrackDTO::fromDomain).toList(),
                disc.getStock()
        );
    }
}
