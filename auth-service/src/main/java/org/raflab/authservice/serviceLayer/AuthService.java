package org.raflab.authservice.serviceLayer;

import lombok.RequiredArgsConstructor;
import org.raflab.authservice.dtoLayer.AuthValidationResponse;
import org.raflab.authservice.entityLayer.User;
import org.raflab.authservice.repositoryLayer.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public AuthValidationResponse validateApiKey(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing API key");
        }

        User user = userRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API key"));

        return new AuthValidationResponse(
                true,
                user.getUsername(),
                user.getRoles()
                        .stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toList())
        );
    }
}