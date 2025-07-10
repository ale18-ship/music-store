package com.acerap.musicstore.domain.valueobjects;

import java.util.Objects;

public record Address(String street, String number, String postalCode, String country) {
    public Address {
        Objects.requireNonNull(street);
        Objects.requireNonNull(country);
        if (street.isBlank() || country.isBlank()) {
            throw new IllegalArgumentException("Street and country cannot be blank");
        }
    }
}
