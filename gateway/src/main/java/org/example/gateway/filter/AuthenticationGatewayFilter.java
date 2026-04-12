package org.example.gateway.filter;

import lombok.RequiredArgsConstructor;
import org.example.gateway.dto.AuthValidationResponse;
import org.example.gateway.service.RouteRoleService;
import org.example.gateway.util.ErrorResponseWriter;
import org.example.gateway.util.HeaderNames;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthenticationGatewayFilter implements GlobalFilter, Ordered {

    private final WebClient authWebClient;
    private final RouteRoleService routeRoleService;
    private final ErrorResponseWriter errorResponseWriter;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/actuator") || path.startsWith("/api/auth")) {
            return chain.filter(exchange);
        }

        String apiKey = exchange.getRequest().getHeaders().getFirst(HeaderNames.API_KEY);
        String traceId = exchange.getRequest().getHeaders().getFirst(HeaderNames.TRACE_ID);

        // Ako nema API key -> 401
        if (apiKey == null || apiKey.isBlank()) {
            return errorResponseWriter.write(
                    exchange.getResponse(),
                    HttpStatus.UNAUTHORIZED,
                    "Missing API key",
                    traceId
            );
        }

        String finalTraceId = traceId;

        return authWebClient.get()
                .uri("/api/auth/validate")
                .header(HeaderNames.API_KEY, apiKey)
                .header(HeaderNames.TRACE_ID, finalTraceId)
                .exchangeToMono(response -> {

                    // Ako je auth uspešno odgovorio
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(AuthValidationResponse.class)
                                .flatMap(authResponse -> {
                                    if (authResponse == null || !authResponse.isValid()) {
                                        return errorResponseWriter.write(
                                                exchange.getResponse(),
                                                HttpStatus.UNAUTHORIZED,
                                                "Invalid API key",
                                                finalTraceId
                                        );
                                    }

                                    List<String> roles = authResponse.getRoles();
                                    HttpMethod method = exchange.getRequest().getMethod();

                                    if (roles == null || roles.isEmpty()) {
                                        return errorResponseWriter.write(
                                                exchange.getResponse(),
                                                HttpStatus.FORBIDDEN,
                                                "User has no roles assigned",
                                                finalTraceId
                                        );
                                    }

                                    if (!routeRoleService.isAllowed(path, method, roles)) {
                                        return errorResponseWriter.write(
                                                exchange.getResponse(),
                                                HttpStatus.FORBIDDEN,
                                                "Access denied",
                                                finalTraceId
                                        );
                                    }

                                    String primaryRole = roles.get(0);

                                    ServerHttpRequest mutatedRequest = exchange.getRequest()
                                            .mutate()
                                            .header(HeaderNames.AUTHENTICATED_USER, authResponse.getUsername())
                                            .header(HeaderNames.USER_ROLE, primaryRole)
                                            .header(HeaderNames.TRACE_ID, finalTraceId)
                                            .build();

                                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                                });
                    }

                    // Ako auth vrati 401 -> ključ nije validan
                    if (response.statusCode() == HttpStatus.UNAUTHORIZED) {
                        return errorResponseWriter.write(
                                exchange.getResponse(),
                                HttpStatus.UNAUTHORIZED,
                                "Invalid API key",
                                finalTraceId
                        );
                    }

                    // Ako auth vrati 403
                    if (response.statusCode() == HttpStatus.FORBIDDEN) {
                        return errorResponseWriter.write(
                                exchange.getResponse(),
                                HttpStatus.FORBIDDEN,
                                "Authentication forbidden",
                                finalTraceId
                        );
                    }

                    // Sve ostalo iz auth servisa tretiramo kao problem dostupnosti / interni problem
                    return errorResponseWriter.write(
                            exchange.getResponse(),
                            HttpStatus.SERVICE_UNAVAILABLE,
                            "Authentication service unavailable",
                            finalTraceId
                    );
                })
                // Ovo je SAMO za pravi pad auth servisa: connection refused, timeout, itd.
                .onErrorResume(ex -> errorResponseWriter.write(
                        exchange.getResponse(),
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "Authentication service unavailable",
                        finalTraceId
                ));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}