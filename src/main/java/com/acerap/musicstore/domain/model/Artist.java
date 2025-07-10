package com.acerap.musicstore.domain.model;

import com.acerap.musicstore.domain.enums.MusicGenre;

import java.util.List;
import java.util.UUID;

public class Artist {
    private UUID id;
    private String name;
    private String country;
    private MusicGenre genre;
    private List<String> members; // Para grupos musicales

    public Artist(){}

    public Artist(String name, String country, MusicGenre genre, List<String> members) {
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.members = members;
    }

    public Artist(UUID id, String name, String country, MusicGenre genre, List<String> members) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.members = members;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public MusicGenre getGenre() {
        return genre;
    }

    public void setGenre(MusicGenre genre) {
        this.genre = genre;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
