package org.example.gateway.filter;

import org.example.gateway.config.GatewayCustomProperties;
import org.example.gateway.util.ErrorResponseWriter;
import org.example.gateway.util.HeaderNames;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryRateLimitFilter implements GlobalFilter, Ordered {

    private final GatewayCustomProperties properties;
    private final ErrorResponseWriter errorResponseWriter;

    private final Map<String, RequestBucket> buckets = new ConcurrentHashMap<>();

    public InMemoryRateLimitFilter(GatewayCustomProperties properties,
                                   ErrorResponseWriter errorResponseWriter) {
        this.properties = properties;
        this.errorResponseWriter = errorResponseWriter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        GatewayCustomProperties.RateLimit rateLimit = properties.getRateLimit();

        if (!rateLimit.isEnabled()) {
            return chain.filter(exchange);
        }

        String path = exchange.getRequest().getURI().getPath();

        if (!isCriticalRequest(exchange)) {
            return chain.filter(exchange);
        }

        String clientIp = resolveClientIp(exchange);
        long now = Instant.now().getEpochSecond();

        RequestBucket bucket = buckets.computeIfAbsent(clientIp, ip -> new RequestBucket(now, 0));

        synchronized (bucket) {
            long windowStart = bucket.windowStart;
            long windowSeconds = rateLimit.getWindowSeconds();

            if (now - windowStart >= windowSeconds) {
                bucket.windowStart = now;
                bucket.requestCount = 0;
            }

            if (bucket.requestCount >= rateLimit.getMaxRequests()) {
                String traceId = exchange.getRequest().getHeaders().getFirst(HeaderNames.TRACE_ID);
                if (traceId == null || traceId.isBlank()) {
                    traceId = "N/A";
                }

                exchange.getResponse().getHeaders().set("X-Rate-Limit-Limit",
                        String.valueOf(rateLimit.getMaxRequests()));
                exchange.getResponse().getHeaders().set("X-Rate-Limit-Window-Seconds",
                        String.valueOf(rateLimit.getWindowSeconds()));

                return errorResponseWriter.write(
                        exchange.getResponse(),
                        HttpStatus.TOO_MANY_REQUESTS,
                        "Too many requests. Please try again later.",
                        traceId
                );
            }

            bucket.requestCount++;
        }

        return chain.filter(exchange);
    }

    private boolean isCriticalPath(String path) {
        return path.startsWith("/api/bookings")
                || path.startsWith("/api/transactions")
                || path.matches("^/api/time-slots/\\d+/book$");
    }

    private String resolveClientIp(ServerWebExchange exchange) {
        String forwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        if (exchange.getRequest().getRemoteAddress() != null
                && exchange.getRequest().getRemoteAddress().getAddress() != null) {
            return exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        }

        return "unknown";
    }

    @Override
    public int getOrder() {
        return -150;
    }

    private static class RequestBucket {
        long windowStart;
        int requestCount;

        RequestBucket(long windowStart, int requestCount) {
            this.windowStart = windowStart;
            this.requestCount = requestCount;
        }
    }

    private boolean isCriticalRequest(ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();
        var method = exchange.getRequest().getMethod();

        if (path.matches("^/api/time-slots/\\d+/book$")) {
            return method != null && method.name().equals("POST");
        }

        if (path.startsWith("/api/bookings")) {
            return method != null && method.name().equals("POST");
        }

        if (path.startsWith("/api/transactions")) {
            return method != null && method.name().equals("POST");
        }

        return false;
    }
}