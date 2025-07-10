package com.acerap.musicstore.application.web;

import com.acerap.musicstore.application.dto.CompactDiscDTO;
import com.acerap.musicstore.domain.model.CompactDisc;
import com.acerap.musicstore.domain.ports.in.CompactDiscService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/discs")
public class CompactDiscController {

    private final CompactDiscService discService;

    public CompactDiscController(CompactDiscService discService) {
        this.discService = discService;
    }

    @PostMapping
    public ResponseEntity<CompactDisc> createDisc(@RequestBody CompactDisc disc) {
        return ResponseEntity.ok(discService.createDisc(disc));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompactDiscDTO> getDisc(@PathVariable UUID id) {
        return discService.findDiscById(id)
                .map(CompactDiscDTO::fromDomain)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
