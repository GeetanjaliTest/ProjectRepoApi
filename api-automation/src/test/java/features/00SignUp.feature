Feature: Signup API Tests

  Scenario: Valid Signup
    When user sends POST request with valid signup details
    Then status code should be 200
    And response body should contain message "User created successfully"


  Scenario: Signup with missing email
    When user sends POST request with empty email and valid password
    Then status code should be 400
    And response body should contain exact validation error for missing email

  Scenario: Signup with missing password
    When user sends POST request with valid email and empty password
    Then status code should be 400
    And response body should contain exact validation error for missing password


