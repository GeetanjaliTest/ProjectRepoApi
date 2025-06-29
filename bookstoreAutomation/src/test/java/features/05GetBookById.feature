Feature: Get Book by ID API

  Scenario: Retrieve a book by its ID
    When user sends GET request to get book endpoint with a valid book ID
    Then status code should be 200
    And response body should contain valid book details
    And response headers should include content-length, content-type, date, and server






