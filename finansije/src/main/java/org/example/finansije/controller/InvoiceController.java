package org.example.finansije.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finansije.enums.InvoiceStatus;
import org.example.finansije.request.InvoiceRequest;
import org.example.finansije.response.BookingResponse;
import org.example.finansije.response.InvoiceResponse;
import org.example.finansije.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
@Tag(name = "Invoice Controller", description = "CRUD operacije za invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    @Operation(summary = "Kreiranje novog invoice-a")
    public ResponseEntity<InvoiceResponse> save(
            @Valid @RequestBody InvoiceRequest request) {

        return ResponseEntity.ok(invoiceService.save(request));
    }

    @GetMapping
    @Operation(summary = "Prikaz svih invoice-a")
    public ResponseEntity<List<InvoiceResponse>> findAll() {

        return ResponseEntity.ok(invoiceService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pronalazak invoice-a po id-u")
    public ResponseEntity<InvoiceResponse> findById(@PathVariable Long id) {

        return ResponseEntity.ok(invoiceService.findById(id));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Pronalazak invoice-a po statusu")
    public ResponseEntity<List<InvoiceResponse>> findByStatus(@PathVariable InvoiceStatus status) {

        return ResponseEntity.ok(invoiceService.findByStatus(status));
    }

    @GetMapping("/booking/{bookingId}")
    @Operation(summary = "Pronalazak invoice-a po booking id-u")
    public ResponseEntity<InvoiceResponse> findByBookingId(@PathVariable Long bookingId) {

        return ResponseEntity.ok(invoiceService.findByBookingId(bookingId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Izmena invoice-a")
    public ResponseEntity<InvoiceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody InvoiceRequest request) {

        return ResponseEntity.ok(invoiceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Brisanje invoice-a")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        invoiceService.deleteById(id);
        return ResponseEntity.ok("Invoice deleted successfully.");
    }

    @PutMapping("/update-booking-status/{id}")
    @Operation(summary = "Izmena statusa bookinga koji nije placen")
    public void updateBookingStatus(@PathVariable Long id) {

        invoiceService.updateBookingStatus(id);
    }
}