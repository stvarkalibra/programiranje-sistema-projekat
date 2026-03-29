package org.example.rezervacije.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.rezervacije.enums.BookingStatus;
import org.example.rezervacije.request.BookingRequest;
import org.example.rezervacije.response.BookingResponse;
import org.example.rezervacije.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking Controller", description = "CRUD operacije za booking")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Kreiranje novog booking-a")
    public ResponseEntity<BookingResponse> save(
            @Valid @RequestBody BookingRequest request) {

        return ResponseEntity.ok(bookingService.save(request));
    }

    @GetMapping
    @Operation(summary = "Prikaz svih booking-a")
    public ResponseEntity<List<BookingResponse>> findAll() {

        return ResponseEntity.ok(bookingService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pronalazak booking-a po id-u")
    public ResponseEntity<BookingResponse> findById(@PathVariable Long id) {

        return ResponseEntity.ok(bookingService.findById(id));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Pronalazak booking-a po statusu")
    public ResponseEntity<List<BookingResponse>> findByStatus(@PathVariable BookingStatus status) {

        return ResponseEntity.ok(bookingService.findByStatus(status));
    }

    @GetMapping("/guest/{guestId}")
    @Operation(summary = "Pronalazak booking-a po guest id-u")
    public ResponseEntity<List<BookingResponse>> findByGuestId(@PathVariable Long guestId) {

        return ResponseEntity.ok(bookingService.findByGuestId(guestId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Izmena booking-a")
    public ResponseEntity<BookingResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody BookingRequest request) {

        return ResponseEntity.ok(bookingService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Brisanje booking-a")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        bookingService.deleteById(id);
        return ResponseEntity.ok("Booking deleted successfully.");
    }
}