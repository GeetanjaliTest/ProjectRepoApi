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

