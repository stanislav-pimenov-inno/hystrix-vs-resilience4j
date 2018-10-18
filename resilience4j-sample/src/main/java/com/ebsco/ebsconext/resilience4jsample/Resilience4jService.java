package com.ebsco.ebsconext.resilience4jsample;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import java.time.Duration;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Resilience4jService {

  private final CircuitBreaker circuitBreaker;
  private final Bulkhead bulkhead;
  private final RateLimiter rateLimiter;
  private static final String STUB_URI = "http://apistub:8888/";
  // TODO: 18.10.18 Make configurable thru application.yaml

  //private TimeLimiter timeLimiter;
  //private ExecutorService executorService = Executors.newFixedThreadPool(10);
  private final Function<String, String> chainedCallable;

  @Autowired
  private RestTemplate restTemplate;

  public Resilience4jService() {

//    // Create a custom configuration for a CircuitBreaker
//    CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
//        .failureRateThreshold(50)
//        .waitDurationInOpenState(Duration.ofMillis(10000))
//        .ringBufferSizeInHalfOpenState(2)
//        .ringBufferSizeInClosedState(4)
//        .enableAutomaticTransitionFromOpenToHalfOpen()
//        .build();
//    // Create a CircuitBreakerRegistry with a custom global configuration
    //CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.(circuitBreakerConfig);
//
    bulkhead = Bulkhead.of("apiCall", BulkheadConfig.ofDefaults());
    chainedCallable = Bulkhead.decorateFunction(bulkhead, this::restrictedCall);

    circuitBreaker = CircuitBreaker.of("apiCall", CircuitBreakerConfig.custom()
        .failureRateThreshold(50)
        .waitDurationInOpenState(Duration.ofMillis(1000))
        .ringBufferSizeInHalfOpenState(2)
        .ringBufferSizeInClosedState(2)
        .build());
    //chainedCallable = CircuitBreaker.decorateFunction(circuitBreaker, this::restrictedCall);

    rateLimiter = RateLimiter.of("apiCall", RateLimiterConfig.custom()
        .limitRefreshPeriod(Duration.ofSeconds(1))
        .limitForPeriod(10)
        .timeoutDuration(Duration.ofSeconds(2))
        .build());
    //chainedCallable = RateLimiter.decorateFunction(rateLimiter, this::restrictedCall);
  }

  public String callApi(String api) {
    return chainedCallable.apply(api);
  }

  private String restrictedCall(String api) {
    return restTemplate.getForObject(STUB_URI + api, String.class);
  }
}
