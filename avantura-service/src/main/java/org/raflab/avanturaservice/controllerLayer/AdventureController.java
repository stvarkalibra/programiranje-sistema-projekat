package org.raflab.avanturaservice.controllerLayer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.raflab.avanturaservice.dtoLayer.request.AdventureRequest;
import org.raflab.avanturaservice.dtoLayer.response.AdventureResponse;
import org.raflab.avanturaservice.serviceLayer.AdventureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/adventures")
@RequiredArgsConstructor
@Tag(name = "Adventure Controller", description = "Endpointi za upravljanje avanturama")
public class AdventureController {

    private final AdventureService adventureService;

    // --- OSNOVNI CRUD (5 endpointa) ---

    @Operation(summary = "Prikaz svih avantura")
    @GetMapping
    public ResponseEntity<List<AdventureResponse>> getAll() {
        return ResponseEntity.ok(adventureService.getAllAdventures());
    }

    @Operation(summary = "Prikaz jedne avanture po ID-u")
    @GetMapping("/{id}")
    public ResponseEntity<AdventureResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(adventureService.getAdventureById(id));
    }

    @Operation(summary = "Kreiranje nove avanture")
    @PostMapping
    public ResponseEntity<AdventureResponse> create(@Valid @RequestBody AdventureRequest request) {
        return new ResponseEntity<>(adventureService.createAdventure(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Izmena postojeće avanture")
    @PutMapping("/{id}")
    public ResponseEntity<AdventureResponse> update(@PathVariable Long id, @Valid @RequestBody AdventureRequest request) {
        return ResponseEntity.ok(adventureService.updateAdventure(id, request));
    }

    @Operation(summary = "Brisanje avanture")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adventureService.deleteAdventure(id);
        return ResponseEntity.noContent().build();
    }

    // Dodaj u AdventureController
    @Operation(summary = "Izračunaj finalnu cenu sa popustom i PDV-om [MATEMATIKA]")
    @GetMapping("/{id}/calculate-price")
    public ResponseEntity<Double> calculatePrice(@PathVariable Long id, @RequestParam Double discount) {
        return ResponseEntity.ok(adventureService.calculateAdventurePrice(id, discount));
    }

    @Operation(summary = "Filtriranje avantura po više parametara [FILTRIRANJE]")
    @GetMapping("/filter")
    public ResponseEntity<List<AdventureResponse>> filter(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String difficulty) {
        return ResponseEntity.ok(adventureService.filterAdventures(categoryId, maxPrice, difficulty));
    }

    @Operation(summary = "Filtriranje avantura određenog vodiča koje su skuplje od zadate cene (OPENFEIGN)")
    @GetMapping("/guide/{guideId}/premium")
    public ResponseEntity<List<AdventureResponse>> getPremium(
            @PathVariable Long guideId,
            @RequestParam Double minPrice) {
        return ResponseEntity.ok(adventureService.getPremiumAdventures(guideId, minPrice));
    }

    @Operation(summary = "Izračunavanje ukupne sume svih osnovnih cena (basePrice) za sve ture jednog vodiča (OPENFEIGN)")
    @GetMapping("/guide/{guideId}/revenue")
    public ResponseEntity<Double> getRevenue(@PathVariable Long guideId) {
        return ResponseEntity.ok(adventureService.getTotalGuideRevenue(guideId));
    }

    @Operation(summary = "Logička provera opterećenja vodiča na osnovu broja aktivnih tura u bazi (OPENFEIGN)")
    @GetMapping("/guide/{guideId}/check-overload")
    public ResponseEntity<Boolean> checkOverload(@PathVariable Long guideId) {
        return ResponseEntity.ok(adventureService.isGuideOverloaded(guideId));
    }

    @Operation(summary = "Dobavljanje svih avantura za određenu kategoriju putem JOIN upita (OPENFEIGN + JOIN)")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<AdventureResponse>> getByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(adventureService.getAdventuresByCategory(categoryId));
    }
}