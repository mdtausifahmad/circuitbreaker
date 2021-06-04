package com.tausif.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CustomCircuitBreakerConfig {

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {

        CircuitBreakerConfig config = CircuitBreakerConfig
                .custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .waitDurationInOpenState(Duration.ofSeconds(10))//specifies the time that the circuit breaker should wait before switching to a half-open state
                .slowCallRateThreshold(50.0f)//slow call rate in percentage
                .slowCallDurationThreshold(Duration.ofSeconds(2))//beyond which a call is considered slow.
                .maxWaitDurationInHalfOpenState(Duration.ofSeconds(10))//amount of time a circuit breaker can stay in the half-open state before switching back to the open state.
                .permittedNumberOfCallsInHalfOpenState(3)//configures the number of calls that will be allowed in the half-open state and
                .build();

        TimeLimiterConfig timeLimiter = TimeLimiterConfig
                .custom()
                .timeoutDuration(Duration.ofSeconds(5))
                .build();

        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiter)
                .circuitBreakerConfig(config)
                .build());
    }


}
