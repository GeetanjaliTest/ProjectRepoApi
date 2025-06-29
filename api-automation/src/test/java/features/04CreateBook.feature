Feature: Create Book API

  Scenario: Create a book with valid data
    When user sends POST request to create book endpoint with valid book details
    Then status code should be 200
    And response body should match the book request data
    And response headers should include content-length, content-type, date, and server




