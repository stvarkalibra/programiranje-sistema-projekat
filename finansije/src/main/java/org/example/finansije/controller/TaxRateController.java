package org.example.finansije.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finansije.request.TaxRateRequest;
import org.example.finansije.response.TaxRateResponse;
import org.example.finansije.service.TaxRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taxrates")
@RequiredArgsConstructor
@Tag(name = "TaxRate Controller", description = "CRUD operacije za TaxRate")
public class TaxRateController {

    private final TaxRateService taxRateService;

    @PostMapping
    @Operation(summary = "Kreiranje novog tax rate-a")
    public ResponseEntity<TaxRateResponse> save(
            @Valid @RequestBody TaxRateRequest request) {

        return ResponseEntity.ok(taxRateService.save(request));
    }

    @GetMapping
    @Operation(summary = "Prikaz svih tax rate-ova")
    public ResponseEntity<List<TaxRateResponse>> findAll() {

        return ResponseEntity.ok(taxRateService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pronalazak tax rate-a po id-u")
    public ResponseEntity<TaxRateResponse> findById(@PathVariable Long id) {

        return ResponseEntity.ok(taxRateService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Izmena tax rate-a")
    public ResponseEntity<TaxRateResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TaxRateRequest request) {

        return ResponseEntity.ok(taxRateService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Brisanje tax rate-a")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        taxRateService.deleteById(id);
        return ResponseEntity.ok("TaxRate deleted successfully.");
    }
}