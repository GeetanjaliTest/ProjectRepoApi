Feature: Get Book by ID API

  Scenario: Retrieve a book by its ID
    When user sends GET request to get book endpoint with a valid book ID
    Then status code should be 200
    And response body should contain valid book details
    And response headers should include content-length, content-type, date, and server

  Scenario: Get a book without authentication token
    When user sends GET request to get book endpoint without token
    Then status code should be 403
    And response should contain error message "ErrMsg"

  Scenario: Get a book with invalid token
    When user sends GET request to get book endpoint with invalid token
    Then status code should be 403
    And response should contain err msg "ErrMsg"

  Scenario: Get a book with non-existent book ID
    When user sends GET request to get book endpoint with non-existent book ID
    Then status code should be 404
    And response should contain error message for book "Book not found"



