package dev.local.olympia.client;

import dev.local.olympia.dto.WorkloadRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// "trainer-workload-service" must match the spring.application.name of the new service
@FeignClient(name = "trainer-workload-service", path = "/workload")
public interface WorkloadClient {

    Logger log = LoggerFactory.getLogger(WorkloadClient.class);

    @PostMapping
    @CircuitBreaker(name = "workload-service", fallbackMethod = "fallbackUpdateWorkload")
    void updateWorkload(@RequestBody WorkloadRequest request);

    // Fallback method executed if the Workload Service is down
    default void fallbackUpdateWorkload(WorkloadRequest request, Throwable t) {
        log.error("Workload Service is unavailable. Failed to update workload for trainer: {}. Error: {}",
                request.getUsername(), t.getMessage());
        // Note: In a real scenario, you might save this to a 'failed_events' table to retry later.
    }
}
