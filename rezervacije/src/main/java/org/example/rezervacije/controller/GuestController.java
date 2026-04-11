package org.example.rezervacije.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.rezervacije.request.GuestRequest;
import org.example.rezervacije.response.GuestResponse;
import org.example.rezervacije.service.GuestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
@Tag(name = "Guest Controller", description = "CRUD operacije za goste")
public class GuestController {

    private final GuestService guestService;

    @PostMapping
    @Operation(summary = "Kreiranje novog gosta")
    public ResponseEntity<GuestResponse> save(@Valid @RequestBody GuestRequest guestRequest) {
        return ResponseEntity.ok(guestService.save(guestRequest));
    }

    @GetMapping
    @Operation(summary = "Prikaz svih gostiju")
    public ResponseEntity<List<GuestResponse>> findAll() {
        return ResponseEntity.ok(guestService.findAll());
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Pronalazak gosta po email-u")
    public ResponseEntity<GuestResponse> findByEmail(
            @Parameter(description = "Email gosta")
            @PathVariable String email) {
        return ResponseEntity.ok(guestService.findByEmail(email));
    }

    @GetMapping("/phone/{phoneNumber}")
    @Operation(summary = "Pronalazak gosta po broju telefona")
    public ResponseEntity<GuestResponse> findByPhoneNumber(
            @Parameter(description = "Broj telefona gosta")
            @PathVariable String phoneNumber) {
        return ResponseEntity.ok(guestService.findByPhoneNumber(phoneNumber));
    }

    @PutMapping("/email/{email}")
    @Operation(summary = "Izmena gosta po email-u")
    public ResponseEntity<GuestResponse> updateByEmail(
            @Parameter(description = "Postojeći email gosta")
            @PathVariable String email,
            @Valid @RequestBody GuestRequest guestRequest) {
        return ResponseEntity.ok(guestService.updateByEmail(email, guestRequest));
    }

    @DeleteMapping("/email/{email}")
    @Operation(summary = "Brisanje gosta po email-u")
    public ResponseEntity<String> deleteByEmail(
            @Parameter(description = "Email gosta")
            @PathVariable String email) {
        guestService.deleteByEmail(email);
        return ResponseEntity.ok("Guest deleted successfully.");
    }

    @DeleteMapping("/phone/{phoneNumber}")
    @Operation(summary = "Brisanje gosta po broju telefona")
    public ResponseEntity<String> deleteByPhoneNumber(
            @Parameter(description = "Broj telefona gosta")
            @PathVariable String phoneNumber) {
        guestService.deleteByPhoneNumber(phoneNumber);
        return ResponseEntity.ok("Guest deleted successfully.");
    }
}