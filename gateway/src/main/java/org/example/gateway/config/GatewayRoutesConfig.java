package org.example.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Value("${services.guide.url}")
    private String guideService;

    @Value("${services.adventure.url}")
    private String adventureService;

    @Value("${services.rezervacije.url}")
    private String rezervacijeService;

    @Value("${services.finansije.url}")
    private String finansijeService;

    @Value("${services.auth.url}")
    private String authService;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("guide-service", r -> r
                        .path(
                                "/api/guides/**",
                                "/api/languages/**",
                                "/api/guide-languages/**",
                                "/api/certifications/**"
                        )
                        .uri(guideService))

                .route("adventure-service", r -> r
                        .path(
                                "/api/adventures/**",
                                "/api/reviews/**",
                                "/api/categories/**",
                                "/api/gear/**",
                                "/api/time-slots/**"
                        )
                        .uri(adventureService))

                .route("rezervacije-service", r -> r
                        .path(
                                "/api/bookings/**",
                                "/api/addon-items/**",
                                "/api/booking-status-logs/**",
                                "/api/cancellation-policies/**",
                                "/api/guests/**"
                        )
                        .uri(rezervacijeService))

                .route("finansije-service", r -> r
                        .path(
                                "/api/invoices/**",
                                "/api/payment-methods/**",
                                "/api/taxrates/**",
                                "/api/transactions/**"
                        )
                        .uri(finansijeService))

                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .uri(authService))

                .build();
    }
}