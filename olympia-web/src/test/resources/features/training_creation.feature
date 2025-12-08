Feature: Training Session Management

  Scenario: Successfully create a training session (Positive)
    Given the ActiveMQ broker is running
    And a trainer with username "John.Doe" exists
    When I send a POST request to "/trainings" with the following data:
      | trainerUsername | John.Doe   |
      | trainingDate    | 2023-10-10 |
      | duration        | 60         |
      | actionType      | ADD        |
    Then the response status code should be 200
    And a message should be published to the "workload.queue"

  Scenario: Fail to create training with invalid data (Negative)
    When I send a POST request to "/trainings" with missing authentication
    Then the response status code should be 401