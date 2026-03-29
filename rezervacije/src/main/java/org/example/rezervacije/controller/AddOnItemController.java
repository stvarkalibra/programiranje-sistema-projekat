package org.example.rezervacije.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.rezervacije.request.AddOnItemRequest;
import org.example.rezervacije.response.AddOnItemResponse;
import org.example.rezervacije.service.AddOnItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addon-items")
@RequiredArgsConstructor
@Tag(name = "AddOnItem Controller", description = "CRUD operacije za AddOnItem")
public class AddOnItemController {

    private final AddOnItemService addOnItemService;

    @PostMapping
    @Operation(summary = "Kreiranje novog dodatnog servisa")
    public ResponseEntity<AddOnItemResponse> save(
            @Valid @RequestBody AddOnItemRequest request) {

        return ResponseEntity.ok(addOnItemService.save(request));
    }

    @GetMapping
    @Operation(summary = "Prikaz svih dodatnih servisa")
    public ResponseEntity<List<AddOnItemResponse>> findAll() {

        return ResponseEntity.ok(addOnItemService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pronalazak dodatnog servisa po id-u")
    public ResponseEntity<AddOnItemResponse> findById(@PathVariable Long id) {

        return ResponseEntity.ok(addOnItemService.findById(id));
    }

    @GetMapping("/booking/{bookingId}")
    @Operation(summary = "Pronalazak dodatnih servisa po booking-u")
    public ResponseEntity<List<AddOnItemResponse>> findByBookingId(@PathVariable Long bookingId) {

        return ResponseEntity.ok(addOnItemService.findByBookingId(bookingId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Izmena dodatnog servisa")
    public ResponseEntity<AddOnItemResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AddOnItemRequest request) {

        return ResponseEntity.ok(addOnItemService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Brisanje dodatnog servisa")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        addOnItemService.deleteById(id);

        return ResponseEntity.ok("AddOnItem deleted successfully.");
    }
}