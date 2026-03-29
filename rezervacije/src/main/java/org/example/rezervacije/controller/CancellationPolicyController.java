package org.example.rezervacije.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.rezervacije.request.CancellationPolicyRequest;
import org.example.rezervacije.response.CancellationPolicyResponse;
import org.example.rezervacije.service.CancellationPolicyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cancellation-policies")
@RequiredArgsConstructor
@Tag(name = "Cancellation Policy Controller", description = "CRUD operacije za cancellation policy")
public class CancellationPolicyController {

    private final CancellationPolicyService cancellationPolicyService;

    @PostMapping
    @Operation(summary = "Kreiranje nove cancellation policy")
    public ResponseEntity<CancellationPolicyResponse> save(
            @Valid @RequestBody CancellationPolicyRequest request) {
        return ResponseEntity.ok(cancellationPolicyService.save(request));
    }

    @GetMapping
    @Operation(summary = "Prikaz svih cancellation policy")
    public ResponseEntity<List<CancellationPolicyResponse>> findAll() {
        return ResponseEntity.ok(cancellationPolicyService.findAll());
    }

    @GetMapping("/{policyName}")
    @Operation(summary = "Pronalazak cancellation policy po nazivu")
    public ResponseEntity<CancellationPolicyResponse> findByPolicyName(
            @Parameter(description = "Naziv cancellation policy")
            @PathVariable String policyName) {
        return ResponseEntity.ok(cancellationPolicyService.findByPolicyName(policyName));
    }

    @PutMapping("/{policyName}")
    @Operation(summary = "Izmena cancellation policy po nazivu")
    public ResponseEntity<CancellationPolicyResponse> updateByPolicyName(
            @Parameter(description = "Postojeći naziv cancellation policy")
            @PathVariable String policyName,
            @Valid @RequestBody CancellationPolicyRequest request) {
        return ResponseEntity.ok(cancellationPolicyService.updateByPolicyName(policyName, request));
    }

    @DeleteMapping("/{policyName}")
    @Operation(summary = "Brisanje cancellation policy po nazivu")
    public ResponseEntity<String> deleteByPolicyName(
            @Parameter(description = "Naziv cancellation policy")
            @PathVariable String policyName) {
        cancellationPolicyService.deleteByPolicyName(policyName);
        return ResponseEntity.ok("Cancellation policy deleted successfully.");
    }
}