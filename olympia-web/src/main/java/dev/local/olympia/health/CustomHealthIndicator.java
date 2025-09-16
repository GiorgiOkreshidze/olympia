package dev.local.olympia.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import jakarta.annotation.PostConstruct;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Value("${health.training-manager.url:http://localhost:8080/api/trainings/training-types}")
    private String userMgmtUrl;

    @Value("${health.training-manager.timeout-ms:2000}")
    private int timeoutMs;

    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(timeoutMs);
        requestFactory.setReadTimeout(timeoutMs);
        this.restTemplate = new RestTemplate(requestFactory);
    }

    @Override
    public Health health() {
        try {
            ResponseEntity<String> resp = restTemplate.getForEntity(userMgmtUrl, String.class);
            if (resp.getStatusCode().is2xxSuccessful()) {
                return Health.up()
                        .withDetail("TrainingManager", "reachable")
                        .withDetail("statusCode", resp.getStatusCodeValue())
                        .build();
            } else {
                return Health.down()
                        .withDetail("TrainingManager", "non-2xx")
                        .withDetail("statusCode", resp.getStatusCodeValue())
                        .build();
            }
        } catch (RestClientException e) {
            return Health.down(e).withDetail("TrainingManager", "error").build();
        }
    }
}

