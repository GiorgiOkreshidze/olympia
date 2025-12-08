Feature: End-to-End Training Flow

  Scenario: User books training and workload updates
    Given the entire system is running
    When User "John.Doe" books a training for 120 minutes
    Then the Olympia service returns 200 OK
    And the Workload Service eventually processes the message