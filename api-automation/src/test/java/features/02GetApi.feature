Feature: Get API

  Scenario: Get root endpoint status
    When user sends GET request to root endpoint
    Then status code should be 200
    And response body should contain "status" as "API is running"
    And response headers should include content-length, content-type, date, and server



