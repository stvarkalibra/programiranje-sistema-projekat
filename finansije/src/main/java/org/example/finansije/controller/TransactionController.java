package org.example.finansije.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finansije.request.TransactionRequest;
import org.example.finansije.response.PaymentMethodStatsResponse;
import org.example.finansije.response.TransactionResponse;
import org.example.finansije.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction Controller", description = "CRUD operacije za transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Kreiranje nove transakcije")
    public ResponseEntity<TransactionResponse> save(
            @Valid @RequestBody TransactionRequest request) {

        return ResponseEntity.ok(transactionService.save(request));
    }

    @GetMapping
    @Operation(summary = "Prikaz svih transakcija")
    public ResponseEntity<List<TransactionResponse>> findAll() {

        return ResponseEntity.ok(transactionService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pronalazak transakcije po id-u")
    public ResponseEntity<TransactionResponse> findById(@PathVariable Long id) {

        return ResponseEntity.ok(transactionService.findById(id));
    }

    @GetMapping("/invoice/{invoiceId}")
    @Operation(summary = "Pronalazak transakcija po invoice id-u")
    public ResponseEntity<List<TransactionResponse>> findByInvoiceId(@PathVariable Long invoiceId) {

        return ResponseEntity.ok(transactionService.findByInvoiceId(invoiceId));
    }

    @GetMapping("/payment-method/{paymentMethodId}")
    @Operation(summary = "Pronalazak transakcija po payment method id-u")
    public ResponseEntity<List<TransactionResponse>> findByPaymentMethodId(@PathVariable Long paymentMethodId) {

        return ResponseEntity.ok(transactionService.findByPaymentMethodId(paymentMethodId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Izmena transakcije")
    public ResponseEntity<TransactionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request) {

        return ResponseEntity.ok(transactionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Brisanje transakcije")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        transactionService.deleteById(id);
        return ResponseEntity.ok("Transaction deleted successfully.");
    }

    @GetMapping("/payment-methods-statistics")
    @Operation(summary = "Slozeniji upit nad bazom")
    public ResponseEntity<List<PaymentMethodStatsResponse>> getPaymentMethodStatistics() {
        return ResponseEntity.ok(transactionService.getPaymentMethodStatistics());
    }
}