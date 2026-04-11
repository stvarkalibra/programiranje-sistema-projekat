package org.example.gateway.filter;

import org.example.gateway.config.GatewayCustomProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ProcessingTimeFilter implements GlobalFilter, Ordered {

    private final GatewayCustomProperties properties;

    public ProcessingTimeFilter(GatewayCustomProperties properties) {
        this.properties = properties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long start = System.currentTimeMillis();

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    long duration = System.currentTimeMillis() - start;

                    String headerName = properties.getProcessingTime().getHeaderName();
                    if (headerName == null || headerName.isBlank()) {
                        headerName = "X-Processing-Time-ms";
                    }

                    exchange.getResponse().getHeaders().set(headerName, String.valueOf(duration));
                }));
    }

    @Override
    public int getOrder() {
        return 100;
    }
}