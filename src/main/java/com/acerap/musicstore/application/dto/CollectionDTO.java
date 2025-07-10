package com.acerap.musicstore.application.dto;

import com.acerap.musicstore.domain.model.CompactDisc;
import com.acerap.musicstore.domain.model.Collection;
import com.acerap.musicstore.domain.valueobjects.Money;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * DTO para transferencia de datos de Colecciones
 */
public record CollectionDTO(
        UUID globalId,
        String name,
        BigDecimal price,
        String currency,
        String promoter,
        List<CompactDiscDTO> discs,
        BigDecimal totalIndividualPrice,
        BigDecimal discountAmount
) {
    /**
     * Método estático para convertir de dominio a DTO
     */
    public static CollectionDTO fromDomain(Collection collection) {
        Money totalIndividual = collection.calculateTotalIndividualPrice();
        Money collectionPrice = collection.getPrice();

        return new CollectionDTO(
                collection.getGlobalId(),
                collection.getName(),
                collectionPrice.amount(),
                collectionPrice.currency().getCurrencyCode(),
                collection.getPromoter(),
                collection.getDiscs().stream()
                        .map(CompactDiscDTO::fromDomain)
                        .collect(Collectors.toList()),
                totalIndividual.amount(),
                totalIndividual.amount().subtract(collectionPrice.amount())
        );
    }

    /**
     * Método estático para convertir de DTO a dominio
     * (Requiere servicios para cargar entidades completas)
     */
    public static Collection toDomain(CollectionDTO dto,
                                      List<CompactDisc> discs) {
        Collection collection = new Collection();
        collection.setGlobalId(dto.globalId());
        collection.setName(dto.name());
        collection.setPrice(new Money(dto.price(), java.util.Currency.getInstance(dto.currency())));
        collection.setPromoter(dto.promoter());
        collection.setDiscs(discs);
        return collection;
    }

    /**
     * DTO anidado para respuesta simplificada
     */
    public record Summary(
            UUID id,
            String name,
            BigDecimal price,
            String currency,
            int discCount
    ) {
        public static Summary fromDomain(Collection collection) {
            return new Summary(
                    collection.getGlobalId(),
                    collection.getName(),
                    collection.getPrice().amount(),
                    collection.getPrice().currency().getCurrencyCode(),
                    collection.getDiscs().size()
            );
        }
    }
}
