Feature: Login API Token Generation

  Scenario: Valid login with correct email and password
    When user sends POST request to login endpoint with valid credentials
    Then status code should be 200
    And response body should contain a valid access token and token type
    And response headers should include content-length, content-type, date, and server

  @bug
  Scenario: Login with missing email
    When user sends POST request to login with missing email
    Then status code should be 422

  @bug
  Scenario: Login with missing password
    When user sends POST request to login with missing password
    Then status code should be 422


