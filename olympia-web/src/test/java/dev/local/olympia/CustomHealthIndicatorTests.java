package dev.local.olympia;

import dev.local.olympia.health.CustomHealthIndicator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomHealthIndicatorTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CustomHealthIndicator indicator;

    @BeforeEach
    void setup() throws Exception {
        // use reflection to inject the mock RestTemplate into your indicator
        Field rtField = CustomHealthIndicator.class.getDeclaredField("restTemplate");
        rtField.setAccessible(true);
        rtField.set(indicator, restTemplate);
        // also set timeout/url fields via reflection or constructor in a testable design
    }

    @Test
    void healthUpWhen200() {
        ResponseEntity<String> ok = new ResponseEntity<>("{\"status\":\"UP\"}", HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(ok);

        Health h = indicator.health();
        assertEquals(Status.UP, h.getStatus());
    }

    @Test
    void healthDownOnException() {
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenThrow(new RestClientException("boom"));
        Health h = indicator.health();
        assertEquals(Status.DOWN, h.getStatus());
    }
}
