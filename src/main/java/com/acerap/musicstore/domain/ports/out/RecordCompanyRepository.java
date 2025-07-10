package com.acerap.musicstore.domain.ports.out;

import com.acerap.musicstore.domain.model.RecordCompany;

import java.util.Optional;

public interface RecordCompanyRepository {
    RecordCompany save(RecordCompany company);
    Optional<RecordCompany> findByName(String name);
}
