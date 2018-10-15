package com.ebsco.ebsconext.resilience4jsample;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Resilience4jService {

  private CircuitBreaker circuitBreaker;
  private TimeLimiter timeLimiter;
  private ExecutorService executorService = Executors.newFixedThreadPool(10);

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
//    CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);

    circuitBreaker = CircuitBreaker.ofDefaults("apiCall");
    // For example, you want to restrict the execution of a long running task to 60 seconds.
    TimeLimiterConfig config = TimeLimiterConfig.custom()
        .timeoutDuration(Duration.ofSeconds(10))
        .cancelRunningFuture(true)
        .build();
    // Create TimeLimiter
    timeLimiter = TimeLimiter.of(config);
  }

  public String callApi(String api) {
    return restTemplate.getForObject("http://localhost:8888/" + api, String.class);
  }
}
