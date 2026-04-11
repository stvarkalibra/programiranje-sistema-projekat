package org.example.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "gateway")
public class GatewayCustomProperties {

    private Header trace = new Header();
    private Header processingTime = new Header();
    private RateLimit rateLimit = new RateLimit();

    public Header getTrace() {
        return trace;
    }

    public void setTrace(Header trace) {
        this.trace = trace;
    }

    public Header getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Header processingTime) {
        this.processingTime = processingTime;
    }

    public RateLimit getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(RateLimit rateLimit) {
        this.rateLimit = rateLimit;
    }

    public static class Header {
        private String headerName;

        public String getHeaderName() {
            return headerName;
        }

        public void setHeaderName(String headerName) {
            this.headerName = headerName;
        }
    }

    public static class RateLimit {
        private boolean enabled;
        private int maxRequests;
        private int windowSeconds;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getMaxRequests() {
            return maxRequests;
        }

        public void setMaxRequests(int maxRequests) {
            this.maxRequests = maxRequests;
        }

        public int getWindowSeconds() {
            return windowSeconds;
        }

        public void setWindowSeconds(int windowSeconds) {
            this.windowSeconds = windowSeconds;
        }
    }
}