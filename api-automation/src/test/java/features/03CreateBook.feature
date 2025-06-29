Feature: Create Book API

  Scenario: Create a book with valid data
    When user sends POST request to create book endpoint with valid book details
    Then status code should be 200
    And response body should match the book request data
    And response headers should include content-length, content-type, date, and server

  Scenario: Create a book without authentication token
    When user sends POST request to create book endpoint without token
    Then status code should be 403
    And response should contain error message "ErrMsg"

  Scenario: Create a book with missing name field
    When user sends POST request to create book endpoint with missing name
    Then status code should be 422

  Scenario: Create a book with missing author field
    When user sends POST request to create book endpoint with missing author
    Then status code should be 422

  Scenario: Create a book with missing published_year field
    When user sends POST request to create book endpoint with missing published_year
    Then status code should be 422


