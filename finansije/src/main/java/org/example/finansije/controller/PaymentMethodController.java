package org.example.finansije.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finansije.request.PaymentMethodRequest;
import org.example.finansije.response.PaymentMethodResponse;
import org.example.finansije.service.PaymentMethodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
@RequiredArgsConstructor
@Tag(name = "PaymentMethod Controller", description = "CRUD operacije za payment method")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @PostMapping
    @Operation(summary = "Kreiranje nove payment method")
    public ResponseEntity<PaymentMethodResponse> save(
            @Valid @RequestBody PaymentMethodRequest request) {

        return ResponseEntity.ok(paymentMethodService.save(request));
    }

    @GetMapping
    @Operation(summary = "Prikaz svih payment method")
    public ResponseEntity<List<PaymentMethodResponse>> findAll() {

        return ResponseEntity.ok(paymentMethodService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pronalazak payment method po id-u")
    public ResponseEntity<PaymentMethodResponse> findById(@PathVariable Long id) {

        return ResponseEntity.ok(paymentMethodService.findById(id));
    }

    @GetMapping("/active/{active}")
    @Operation(summary = "Pronalazak payment method po statusu aktivnosti")
    public ResponseEntity<List<PaymentMethodResponse>> findByActive(@PathVariable boolean active) {

        return ResponseEntity.ok(paymentMethodService.findByActive(active));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Izmena payment method")
    public ResponseEntity<PaymentMethodResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PaymentMethodRequest request) {

        return ResponseEntity.ok(paymentMethodService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Brisanje payment method")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        paymentMethodService.deleteById(id);
        return ResponseEntity.ok("Payment method deleted successfully.");
    }
}