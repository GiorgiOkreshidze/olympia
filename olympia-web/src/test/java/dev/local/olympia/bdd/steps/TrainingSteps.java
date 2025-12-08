package dev.local.olympia.bdd.steps;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingSteps {

    @Autowired
    private JmsTemplate jmsTemplate;

    private Response response;

    @Given("the ActiveMQ broker is running")
    public void checkBroker() {
        // Handled by Testcontainers
    }

    @When("I send a POST request to {string} with the following data:")
    public void sendPostRequest(String endpoint, Map<String, String> data) {
        response = RestAssured.given()
                .contentType("application/json")
                .body(data)
                .post(endpoint);
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int code) {
        assertEquals(code, response.getStatusCode());
    }

    @Then("a message should be published to the {string}")
    public void verifyMessage(String queueName) {
        jmsTemplate.setReceiveTimeout(2000);
        Object message = jmsTemplate.receiveAndConvert(queueName);
        assertNotNull(message, "Message was not found in queue");
    }
}
