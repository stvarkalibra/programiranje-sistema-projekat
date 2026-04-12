package org.example.gateway.filter;

import org.example.gateway.config.GatewayCustomProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class TraceIdFilter implements GlobalFilter, Ordered {

    private final GatewayCustomProperties properties;

    public TraceIdFilter(GatewayCustomProperties properties) {
        this.properties = properties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String headerName = properties.getTrace().getHeaderName();
        if (headerName == null || headerName.isBlank()) {
            headerName = "X-Trace-Id";
        }

        String traceId = exchange.getRequest().getHeaders().getFirst(headerName);
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString();
        }

        final String finalHeaderName = headerName;
        final String finalTraceId = traceId;

        ServerHttpRequest mutatedRequest = exchange.getRequest()
                .mutate()
                .header(finalHeaderName, finalTraceId)
                .build();

        exchange.getResponse().getHeaders().set(finalHeaderName, finalTraceId);

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return -200;
    }
}