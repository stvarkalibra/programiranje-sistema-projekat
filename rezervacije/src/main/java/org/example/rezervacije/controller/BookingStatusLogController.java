package org.example.rezervacije.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.rezervacije.request.BookingStatusLogRequest;
import org.example.rezervacije.response.BookingStatusLogResponse;
import org.example.rezervacije.service.BookingStatusLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking-status-logs")
@RequiredArgsConstructor
@Tag(name = "BookingStatusLog Controller", description = "CRUD operacije za BookingStatusLog")
public class BookingStatusLogController {

    private final BookingStatusLogService bookingStatusLogService;

    @PostMapping
    @Operation(summary = "Kreiranje novog booking status log-a")
    public ResponseEntity<BookingStatusLogResponse> save(
            @Valid @RequestBody BookingStatusLogRequest request) {
        return ResponseEntity.ok(bookingStatusLogService.save(request));
    }

    @GetMapping
    @Operation(summary = "Prikaz svih booking status logova")
    public ResponseEntity<List<BookingStatusLogResponse>> findAll() {
        return ResponseEntity.ok(bookingStatusLogService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pronalazak booking status log-a po id-u")
    public ResponseEntity<BookingStatusLogResponse> findById(
            @Parameter(description = "Id booking status log-a")
            @PathVariable Long id) {
        return ResponseEntity.ok(bookingStatusLogService.findById(id));
    }

    @GetMapping("/booking/{bookingId}")
    @Operation(summary = "Pronalazak svih logova po booking id-u")
    public ResponseEntity<List<BookingStatusLogResponse>> findByBookingId(
            @Parameter(description = "Id booking-a")
            @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingStatusLogService.findByBookingId(bookingId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Izmena booking status log-a")
    public ResponseEntity<BookingStatusLogResponse> update(
            @Parameter(description = "Id booking status log-a")
            @PathVariable Long id,
            @Valid @RequestBody BookingStatusLogRequest request) {
        return ResponseEntity.ok(bookingStatusLogService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Brisanje booking status log-a po id-u")
    public ResponseEntity<String> deleteById(
            @Parameter(description = "Id booking status log-a")
            @PathVariable Long id) {
        bookingStatusLogService.deleteById(id);
        return ResponseEntity.ok("BookingStatusLog deleted successfully.");
    }
}