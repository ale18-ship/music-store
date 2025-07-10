package com.acerap.musicstore.domain.model;

import com.acerap.musicstore.domain.valueobjects.Money;

import java.util.*;

public class CompactDisc {
    private UUID globalId;
    private String storeCode;
    private String title;
    private Money price;
    private Artist artist;
    private int productionYear;
    private RecordCompany recordCompany;
    private List<Track> tracks;
    private int stock;

    public CompactDisc() {
    }

    public CompactDisc(UUID globalId, String storeCode, String title, Money price, Artist artist, int productionYear, RecordCompany recordCompany, List<Track> tracks, int stock) {
        this.globalId = globalId;
        this.storeCode = storeCode;
        this.title = title;
        this.price = price;
        this.artist = artist;
        this.productionYear = productionYear;
        this.recordCompany = recordCompany;
        this.tracks = tracks;
        this.stock = stock;
    }

    public CompactDisc(String storeCode, String title, Money price, Artist artist, int productionYear, RecordCompany recordCompany, List<Track> tracks, int stock) {
        this.storeCode = storeCode;
        this.title = title;
        this.price = price;
        this.artist = artist;
        this.productionYear = productionYear;
        this.recordCompany = recordCompany;
        this.tracks = tracks;
        this.stock = stock;
    }

    public void addTrack(Track track) {
        if (tracks.stream().anyMatch(t -> t.getPosition() == track.getPosition())) {
            throw new IllegalArgumentException("Ya existe una pista en la posición " + track.getPosition());
        }
        tracks.add(track);
        tracks.sort(Comparator.comparingInt(Track::getPosition));
    }

    public void removeTrack(int position) {
        boolean removed = tracks.removeIf(track -> track.getPosition() == position);
        if (!removed) {
            throw new IllegalArgumentException("No existe una pista en la posición " + position);
        }
        // Reorganizar posiciones si es necesario
        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i).getPosition() != i + 1) {
                tracks.get(i).setPosition(i + 1);
            }
        }
    }

    public void reduceStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalStateException("Not enough stock");
        }
        this.stock -= quantity;
    }

    public UUID getGlobalId() {
        return globalId;
    }

    public void setGlobalId(UUID globalId) {
        this.globalId = globalId;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public RecordCompany getRecordCompany() {
        return recordCompany;
    }

    public void setRecordCompany(RecordCompany recordCompany) {
        this.recordCompany = recordCompany;
    }

    public List<Track> getTracks() {
        return Collections.unmodifiableList(tracks);
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = new ArrayList<>(tracks);
        this.tracks.sort(Comparator.comparingInt(Track::getPosition));
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
