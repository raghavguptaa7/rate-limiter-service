package com.raghav.ratelimiter.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class RateLimiterMetrics {

    private final Counter totalRequests;
    private final Counter allowedRequests;
    private final Counter blockedRequests;
    private final Timer requestTimer;

    public RateLimiterMetrics(MeterRegistry registry) {

        totalRequests = Counter.builder("rate_limiter_requests_total")
                .description("Total incoming requests")
                .register(registry);

        allowedRequests = Counter.builder("rate_limiter_allowed_total")
                .description("Allowed requests")
                .register(registry);

        blockedRequests = Counter.builder("rate_limiter_blocked_total")
                .description("Blocked requests")
                .register(registry);

        requestTimer = Timer.builder("rate_limiter_request_latency")
                .description("Request latency")
                .register(registry);
    }

    public void incrementTotalRequests() {
        totalRequests.increment();
    }

    public void incrementAllowedRequests() {
        allowedRequests.increment();
    }

    public void incrementBlockedRequests() {
        blockedRequests.increment();
    }

    public Timer getRequestTimer() {
        return requestTimer;
    }
}