# API Test Automation Setup

This project implements a BDD-style API Test Automation Framework for a FastAPI backend using Java, Cucumber, REST Assured, and Allure, fully integrated with GitHub Actions CI/CD.


## Technologies Used

- **Java 21** – Core language for test logic
- **Cucumber 6** – BDD testing framework
- **JUnit 4** – Test runner integration with Cucumber
- **REST Assured** – API testing library
- **Allure** – Reporting framework for test results
- **JaCoCo** – Code coverage tool
- **Maven** – Dependency and build management
- **GitHub Actions** – CI pipeline to run tests and generate reports
- **FastAPI** – Python backend under test


## Setup Instructions

### 1. Prerequisites
- Java 17+
- Maven installed
- Python 3.13 for FastAPI backend
- Allure CLI (for local reporting)

### 2. Run FastAPI Backend
```bash
cd fastapi-backend
pip install -r requirements.txt
uvicorn main:app --host 127.0.0.1 --port 8000
````

### 3. Run API Tests

```bash
cd api-automation
mvn clean test -Dallure.results.directory=target/allure-results
```

### 4. Generate Allure Report

```bash
allure generate target/allure-results --clean -o target/allure-report
allure open target/allure-report
```

### 5. Generate Code Coverage

```bash
mvn verify
# Output: target/site/jacoco/index.html
```

## Dependency Overview

* **`cucumber-java`**: Used to define step definitions in BDD-style feature files.
* **`cucumber-junit`**: Integrates Cucumber with JUnit 4 for test execution.
* **`junit`**: Provides the test runner and core test functionality.
* **`rest-assured`**: Simplifies writing and sending HTTP requests in API tests.
* **`allure-cucumber6-jvm`**: Acts as a bridge between Cucumber and Allure for generating test reports.
* **`slf4j-simple`**: Lightweight logging framework used to log outputs during test execution.
* **`jacoco-maven-plugin`**: Used to generate test coverage reports (JaCoCo format) as part of the Maven build lifecycle.

## Maven Plugin Overview

* **`maven-compiler-plugin`**
  Compiles Java source code using **JDK 17**, ensuring compatibility with Java features and language level.

* **`maven-surefire-plugin`**
  Executes tests written in **JUnit** and **Cucumber**, acting as the main test runner during the Maven lifecycle.

* **`allure-maven`**
  Generates **Allure-compatible test result files** and provides integration to produce rich HTML reports from test executions.

* **`jacoco-maven-plugin`**
  Captures **code coverage** during test execution and generates detailed **HTML coverage reports** to analyze test completeness.

## GitHub Actions CI Overview

Workflow (`ci.yml`) automates:

* Python FastAPI setup and launch
* Java + Maven test execution
* Allure & JaCoCo report generation
* Artifact uploads (test report, coverage)
* Optional: Deploy Allure HTML to GitHub Pages (on `gh-pages` branch)


## Testing Strategy

### Test Flow Design

* Used Cucumber with Gherkin syntax to create clear and human-readable test scenarios.
* Grouped scenarios logically based on API features and endpoints.
* Reused common step definitions to avoid duplication.

### Reliability and Maintainability

* Ensured test isolation and consistent test data for reliability.
* Integrated Allure reporting and JaCoCo for visibility and metrics.
* Structured codebase to separate test logic from configuration and data.

### Challenges and Resolutions

* Faced FastAPI availability issues; resolved by starting it in the CI pipeline.
* Fixed Cucumber feature path errors by correcting runner configuration.
* Configured CI to continue execution after test failures for full report generation.

### Covered Endpoints

### Book Management
* POST /books/: Create a new book.

* PUT /books/{book_id}: Update a book by ID.

* DELETE /books/{book_id}: Delete a book by ID.

* GET /books/{book_id}: Get a book by ID.


### User Authentication
* POST /signup: Sign up a new user.

* POST /login: Log in and receive an access token.

### Health Check
* GET /health: Check the health of the API.



