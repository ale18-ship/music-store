package com.acerap.musicstore.domain.model;

import com.acerap.musicstore.domain.valueobjects.Money;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Collection {
    private UUID globalId;
    private String name;
    private Money price;
    private String promoter;
    private List<CompactDisc> discs;

    public Collection() {
    }

    public Collection(UUID globalId, String name, Money price, String promoter, List<CompactDisc> discs) {
        this.globalId = globalId;
        this.name = name;
        this.price = price;
        this.promoter = promoter;
        this.discs = discs;
    }

    public Money calculateTotalIndividualPrice() {
        return discs.stream()
                .map(CompactDisc::getPrice)
                .reduce(new Money(BigDecimal.ZERO, price.currency()), Money::add);
    }

    public UUID getGlobalId() {
        return globalId;
    }

    public void setGlobalId(UUID globalId) {
        this.globalId = globalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public String getPromoter() {
        return promoter;
    }

    public void setPromoter(String promoter) {
        this.promoter = promoter;
    }

    public List<CompactDisc> getDiscs() {
        return discs;
    }

    public void setDiscs(List<CompactDisc> discs) {
        this.discs = discs;
    }
}
