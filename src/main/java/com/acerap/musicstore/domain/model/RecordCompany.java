package com.acerap.musicstore.domain.model;

import com.acerap.musicstore.domain.valueobjects.Address;

public class RecordCompany {
    private String name;
    private Address address;
    private String headquartersCountry;

    public RecordCompany() {
    }

    public RecordCompany(String name, Address address, String headquartersCountry) {
        this.name = name;
        this.address = address;
        this.headquartersCountry = headquartersCountry;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public String getHeadquartersCountry() {
        return headquartersCountry;
    }
}
