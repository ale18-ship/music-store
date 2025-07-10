package com.acerap.musicstore.application.service;

import com.acerap.musicstore.domain.enums.MusicGenre;
import com.acerap.musicstore.domain.exception.DomainException;
import com.acerap.musicstore.domain.model.Artist;
import com.acerap.musicstore.domain.model.CompactDisc;
import com.acerap.musicstore.domain.model.RecordCompany;
import com.acerap.musicstore.domain.model.Track;
import com.acerap.musicstore.domain.ports.in.CompactDiscService;
import com.acerap.musicstore.domain.ports.out.ArtistRepository;
import com.acerap.musicstore.domain.ports.out.CompactDiscRepository;
import com.acerap.musicstore.domain.ports.out.RecordCompanyRepository;
import com.acerap.musicstore.domain.valueobjects.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CompactDiscServiceImpl implements CompactDiscService {

    private final CompactDiscRepository discRepository;
    private final ArtistRepository artistRepository;
    private final RecordCompanyRepository recordCompanyRepository;

    public CompactDiscServiceImpl(CompactDiscRepository discRepository,
                                  ArtistRepository artistRepository,
                                  RecordCompanyRepository recordCompanyRepository) {
        this.discRepository = discRepository;
        this.artistRepository = artistRepository;
        this.recordCompanyRepository = recordCompanyRepository;
    }

    @Override
    public CompactDisc createDisc(CompactDisc disc) {
        validateDisc(disc);

        // Verificar si ya existe un CD con el mismo código de tienda
        if (discRepository.existsByStoreCode(disc.getStoreCode())) {
            throw new DomainException("Ya existe un CD con el código de tienda: " + disc.getStoreCode());
        }

        return discRepository.save(disc);
    }

    @Override
    public Optional<CompactDisc> findDiscById(UUID id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        return discRepository.findById(id);
    }

    @Override
    public List<CompactDisc> findDiscsByGenre(MusicGenre genre) {
        return discRepository.findByGenre(genre);
    }

    @Override
    public List<CompactDisc> findDiscsByArtist(String artistName) {
        return discRepository.findByArtistName(artistName);
    }

    @Override
    public List<CompactDisc> findDiscsByTitle(String title) {
        return discRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public void updateStock(UUID discId, int quantity) {
        if (quantity < 0) {
            throw new DomainException("La cantidad no puede ser negativa");
        }

        CompactDisc disc = findDiscById(discId)
                .orElseThrow(() -> new DomainException("CD no encontrado con ID: " + discId));
        disc.setStock(quantity);
        discRepository.save(disc);
    }

    @Override
    public void reduceStock(UUID discId, int quantity) {
        if (quantity <= 0) {
            throw new DomainException("La cantidad debe ser positiva");
        }

        CompactDisc disc = findDiscById(discId)
                .orElseThrow(() -> new DomainException("CD no encontrado con ID: " + discId));

        if (disc.getStock() < quantity) {
            throw new DomainException("Stock insuficiente para el CD: " + disc.getTitle() +
                    ". Stock actual: " + disc.getStock());
        }

        disc.setStock(disc.getStock() - quantity);
        discRepository.save(disc);
    }

    @Override
    public void increaseStock(UUID discId, int quantity) {
        if (quantity <= 0) {
            throw new DomainException("La cantidad debe ser positiva");
        }

        CompactDisc disc = findDiscById(discId)
                .orElseThrow(() -> new DomainException("CD no encontrado con ID: " + discId));

        disc.setStock(disc.getStock() + quantity);
        discRepository.save(disc);
    }

    @Override
    public List<CompactDisc> findLowStockDiscs(int threshold) {
        return discRepository.findByStockLessThan(threshold);
    }

    @Override
    public CompactDisc updateDiscPrice(UUID discId, Money newPrice) {
        CompactDisc disc = findDiscById(discId)
                .orElseThrow(() -> new DomainException("CD no encontrado con ID: " + discId));
        disc.setPrice(newPrice);
        return discRepository.save(disc);
    }

    @Override
    public void addTrackToDisc(UUID discId, Track track) {
        CompactDisc disc = findDiscById(discId)
                .orElseThrow(() -> new DomainException("CD no encontrado con ID: " + discId));
        disc.addTrack(track);
        discRepository.save(disc);
    }

    @Override
    public void removeTrackFromDisc(UUID discId, int trackPosition) {
        CompactDisc disc = findDiscById(discId)
                .orElseThrow(() -> new DomainException("CD no encontrado con ID: " + discId));
        disc.removeTrack(trackPosition);
        discRepository.save(disc);
    }

    private void validateDisc(CompactDisc disc) {
        // Validar artista
        Artist artist = artistRepository.findByName(disc.getArtist().getName())
                .orElseThrow(() -> new DomainException("Artista no encontrado: " + disc.getArtist().getName()));

        // Validar compañía discográfica
        RecordCompany company = recordCompanyRepository.findByName(disc.getRecordCompany().getName())
                .orElseThrow(() -> new DomainException("Compañía discográfica no encontrada: " + disc.getRecordCompany().getName()));

        // Validar año de producción
        if (disc.getProductionYear() < 1900 || disc.getProductionYear() > java.time.Year.now().getValue()) {
            throw new DomainException("Año de producción no válido");
        }

        // Validar pistas
        if (disc.getTracks() == null || disc.getTracks().isEmpty()) {
            throw new DomainException("El CD debe contener al menos una pista");
        }

        // Validar precio
        if (disc.getPrice().amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("El precio debe ser mayor que cero");
        }

        // Actualizar referencias a las entidades completas
        disc.setArtist(artist);
        disc.setRecordCompany(company);
    }
}
