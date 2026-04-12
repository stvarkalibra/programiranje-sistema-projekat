package org.example.finansije.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.example.finansije.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Bad request from reservation service"
            );
            case 401 -> new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized request to reservation service"
            );
            case 403 -> new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Forbidden request to reservation service"
            );
            case 404 -> new NotFoundException("Booking not found");
            case 409 -> new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Conflict in reservation service"
            );
            case 500 -> new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Internal error in reservation service"
            );
            case 503 -> new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Reservation service unavailable"
            );
            default -> new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error from reservation service: " + response.status()
            );
        };
    }
}