package dev.local.olympia.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class CucumberSpringConfiguration {

    // Start ActiveMQ in a container
    @Container
    static GenericContainer<?> activeMq = new GenericContainer<>("rmohr/activemq:5.15.9")
            .withExposedPorts(61616);

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.activemq.broker-url",
                () -> "tcp://" + activeMq.getHost() + ":" + activeMq.getMappedPort(61616));
    }
}
