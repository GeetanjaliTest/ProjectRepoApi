Feature: Health Check API

  Scenario: Verify health endpoint is up
    When user sends GET request to health endpoint
    Then status code should be 200
    And response body should be:
      | status |
      | up     |
    And response headers should include content-length, content-type, date, and server


