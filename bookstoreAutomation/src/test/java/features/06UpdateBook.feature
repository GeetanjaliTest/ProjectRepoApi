Feature: Update Book API

  Scenario: Update only book name
    When user updates the book with fields:
      | book.updated_name |
    Then status code should be 200
    And response body should reflect the updated fields:
      | book.updated_name |
    And response headers should include content-length, content-type, date, and server

  Scenario: Update only author
    When user updates the book with fields:
      | book.updated_author |
    Then status code should be 200
    And response body should reflect the updated fields:
      | book.updated_author |
    And response headers should include content-length, content-type, date, and server

  Scenario: Update only published year
    When user updates the book with fields:
      | book.updated_published_year |
    Then status code should be 200
    And response body should reflect the updated fields:
      | book.updated_published_year |
    And response headers should include content-length, content-type, date, and server

  Scenario: Update only book summary
    When user updates the book with fields:
      | book.updated_summary |
    Then status code should be 200
    And response body should reflect the updated fields:
      | book.updated_summary |
    And response headers should include content-length, content-type, date, and server

  Scenario: Update name and author
    When user updates the book with fields:
      | book.updated_name  |
      | book.updated_author |
    Then status code should be 200
    And response body should reflect the updated fields:
      | book.updated_name  |
      | book.updated_author |
    And response headers should include content-length, content-type, date, and server

  Scenario: Update author and summary
    When user updates the book with fields:
      | book.updated_author  |
      | book.updated_summary |
    Then status code should be 200
    And response body should reflect the updated fields:
      | book.updated_author  |
      | book.updated_summary |
    And response headers should include content-length, content-type, date, and server

  Scenario: Update published year and name
    When user updates the book with fields:
      | book.updated_published_year |
      | book.updated_name           |
    Then status code should be 200
    And response body should reflect the updated fields:
      | book.updated_published_year |
      | book.updated_name           |
    And response headers should include content-length, content-type, date, and server

  Scenario: Update summary and published year
    When user updates the book with fields:
      | book.updated_summary        |
      | book.updated_published_year |
    Then status code should be 200
    And response body should reflect the updated fields:
      | book.updated_summary        |
      | book.updated_published_year |
    And response headers should include content-length, content-type, date, and server

  Scenario: Update name, author, and summary
    When user updates the book with fields:
      | book.updated_name    |
      | book.updated_author  |
      | book.updated_summary |
    Then status code should be 200
    And response body should reflect the updated fields:
      | book.updated_name    |
      | book.updated_author  |
      | book.updated_summary |
    And response headers should include content-length, content-type, date, and server

  Scenario: Update all fields
    When user updates the book with fields:
      | book.updated_name           |
      | book.updated_author         |
      | book.updated_summary        |
      | book.updated_published_year |
    Then status code should be 200
    And response body should reflect the updated fields:
      | book.updated_name           |
      | book.updated_author         |
      | book.updated_summary        |
      | book.updated_published_year |
    And response headers should include content-length, content-type, date, and server






