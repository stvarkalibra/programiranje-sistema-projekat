package org.raflab.authservice.controllerLayer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.raflab.authservice.dtoLayer.AuthValidationResponse;
import org.raflab.authservice.serviceLayer.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "Validacija korisnika preko X-API-KEY zaglavlja")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Validacija API ključa i vraćanje korisnika i njegovih uloga")
    @GetMapping("/validate")
    public ResponseEntity<AuthValidationResponse> validate(
            @Parameter(description = "API key korisnika", required = true)
            @RequestHeader(name = "X-API-KEY", required = false) String apiKey
    ) {
        return ResponseEntity.ok(authService.validateApiKey(apiKey));
    }
}