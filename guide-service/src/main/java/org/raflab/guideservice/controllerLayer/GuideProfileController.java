package org.raflab.guideservice.controllerLayer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.raflab.guideservice.clientLayer.RemoteAdventureDTO;
import org.raflab.guideservice.dtoLayer.request.GuideProfileRequest;
import org.raflab.guideservice.dtoLayer.response.GuideProfileResponse;
import org.raflab.guideservice.serviceLayer.GuideProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/guides")
@RequiredArgsConstructor
@Tag(name = "Guide Profile Controller", description = "Endpointi za upravljanje profilima vodiča")
public class GuideProfileController {

    private final GuideProfileService guideService;

    @Operation(summary = "Prikaz svih vodiča")
    @GetMapping
    public ResponseEntity<List<GuideProfileResponse>> getAll() {
        return ResponseEntity.ok(guideService.getAllGuides());
    }

    @Operation(summary = "Prikaz vodiča po ID-u")
    @GetMapping("/{id}")
    public ResponseEntity<GuideProfileResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(guideService.getGuideById(id));
    }

    @Operation(summary = "Kreiranje novog vodiča")
    @PostMapping
    public ResponseEntity<GuideProfileResponse> create(@Valid @RequestBody GuideProfileRequest request) {
        return new ResponseEntity<>(guideService.createGuide(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Izmena profila vodiča")
    @PutMapping("/{id}")
    public ResponseEntity<GuideProfileResponse> update(@PathVariable Long id, @Valid @RequestBody GuideProfileRequest request) {
        return ResponseEntity.ok(guideService.updateGuide(id, request));
    }

    @Operation(summary = "Brisanje profila vodiča")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        guideService.deleteGuide(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Pronađi elite vodiče (rejting iznad proseka sistema)")
    @GetMapping("/elite")
    public ResponseEntity<List<GuideProfileResponse>> getElite() {
        return ResponseEntity.ok(guideService.getEliteGuides());
    }

    @Operation(summary = "Pronađi zamenu za vodiča (isti jezik, visok rejting)")
    @GetMapping("/replacements")
    public ResponseEntity<List<GuideProfileResponse>> getReplacements(@RequestParam Long languageId) {
        return ResponseEntity.ok(guideService.findReplacements(languageId));
    }

    @Operation(summary = "Provera da li vodič sme na teren (rejting + važeća licenca)")
    @GetMapping("/{id}/ready-check")
    public ResponseEntity<Boolean> checkReady(@PathVariable Long id) {
        return ResponseEntity.ok(guideService.isGuideReadyForField(id));
    }

    @Operation(summary = "Dodeli bonus na rejting za poliglote (3+ jezika)")
    @PostMapping("/{id}/apply-bonus")
    public ResponseEntity<GuideProfileResponse> applyBonus(@PathVariable Long id) {
        return ResponseEntity.ok(guideService.applyLanguageBonus(id));
    }

    @Operation(summary = "Statistika raspodele vodiča po kvalitetu (Gold/Silver/Bronze)")
    @GetMapping("/stats/rating-distribution")
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(guideService.getRatingDistribution());
    }

    @Operation(summary = "Prikaz premium avantura vodiča iznad određene cene (OPENFEIGN)")
    @GetMapping("/{id}/premium-adventures")
    public ResponseEntity<List<RemoteAdventureDTO>> getPremium(
            @PathVariable Long id,
            @RequestParam Double minPrice) {
        return ResponseEntity.ok(guideService.getPremiumReport(id, minPrice));
    }

    @Operation(summary = "Izračunavanje ukupne bazične vrednosti svih tura koje vodič drži (OPENFEIGN)")
    @GetMapping("/{id}/total-revenue")
    public ResponseEntity<Double> getRevenue(@PathVariable Long id) {
        return ResponseEntity.ok(guideService.getRevenue(id));
    }

    @Operation(summary = "Provera da li je vodič preopterećen teškim i ekstremnim turama (OPENFEIGN)")
    @GetMapping("/{id}/is-overloaded")
    public ResponseEntity<Boolean> getOverload(@PathVariable Long id) {
        return ResponseEntity.ok(guideService.checkOverload(id));
    }

    @Operation(summary = "Globalna pretraga avantura po kategoriji (OPENFEIGN + JOIN)")
    @GetMapping("/search/category/{categoryId}")
    public ResponseEntity<List<RemoteAdventureDTO>> searchByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(guideService.getAllInCategory(categoryId));
    }
}