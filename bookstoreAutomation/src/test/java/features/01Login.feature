Feature: Login API Token Generation

  Scenario: Valid login with correct email and password
    When user sends POST request to login endpoint with valid credentials
    Then status code should be 200
    And response body should contain a valid access token and token type
    And response headers should include content-length, content-type, date, and server

  Scenario: Login with missing email
    When user sends POST request to login with missing email
    Then status code should be 400 or 422
    And response should contain validation error for "email"


  Scenario: Login with missing password
    When user sends POST request to login with missing password
    Then response should contain validation error for "password" with message "field required" and type "value_error.missing"

