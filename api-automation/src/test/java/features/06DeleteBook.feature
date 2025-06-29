Feature: Delete Book API

  Scenario: Delete a book by ID
    When user sends DELETE request to delete book endpoint with a valid book ID
    Then status code should be 200
    And response headers should include content-length, content-type, date, and server





