package org.example.gateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.gateway.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class ErrorResponseWriter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Mono<Void> write(ServerHttpResponse response, HttpStatus status, String message, String traceId) {
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.getHeaders().set(HeaderNames.TRACE_ID, traceId);

        ErrorResponse errorResponse = new ErrorResponse(
                message,
                status.value(),
                traceId,
                LocalDateTime.now()
        );

        byte[] body;
        try {
            body = objectMapper.writeValueAsString(errorResponse).getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            body = ("{\"message\":\"" + message + "\",\"status\":" + status.value() +
                    ",\"traceId\":\"" + traceId + "\"}").getBytes(StandardCharsets.UTF_8);
        }

        return response.writeWith(Mono.just(response.bufferFactory().wrap(body)));
    }
}