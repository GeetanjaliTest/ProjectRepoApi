package stepdefs;

import io.cucumber.java.en.*;
import io.restassured.response.Response;

import java.util.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import utils.ConfigManager;
import utils.TokenManager;

public class StepDefs {
    Response response;
    String baseUrl = ConfigManager.getProperty("baseUrl");
    String email = ConfigManager.getProperty("email");
    String password = ConfigManager.getProperty("password");
    Map<String, Object> bookData = new HashMap<>();

    int uniqueId = 10000 + new Random().nextInt(90000); // ID between 10000–99999

    private static final Map<String, String> fieldKeyToJsonField = Map.of(
            "book.updated_name", "name",
            "book.updated_author", "author",
            "book.updated_summary", "book_summary",
            "book.updated_published_year", "published_year"
    );

    @When("user sends POST request with valid signup details")
    public void validSignup() {
        Map<String, Object> body = new HashMap<>();
        body.put("id", uniqueId);
        body.put("email", email);
        body.put("password", password);

        response = given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post(baseUrl + "/signup");

        System.out.println("Signup Request Body: " + body);
        System.out.println("Response for valid signup: " + response.asString());
    }

    @Then("status code should be {int}")
    public void validateStatusCode(int expectedStatusCode) {
        int actual = response.getStatusCode();
        System.out.println("Expected: " + expectedStatusCode + ", Actual: " + actual);
        response.then().statusCode(expectedStatusCode);
    }


    @When("user sends POST request with empty email and valid password")
    public void signupWithEmptyEmail() {
        Map<String, Object> body = Map.of("email", "", "password", password);

        response = given()
                .header("Content-Type", "application/json")
                .body(body)
                .post(baseUrl + "/signup");

        System.out.println("Request with empty email: " + body);
        System.out.println("Response: " + response.asString());
    }

    @When("user sends POST request with valid email and empty password")
    public void signupWithEmptyPassword() {
        Map<String, Object> body = Map.of("email", email, "password", "");

        response = given()
                .header("Content-Type", "application/json")
                .body(body)
                .post(baseUrl + "/signup");

        System.out.println("Request with empty password: " + body);
        System.out.println("Response: " + response.asString());
    }

    @When("user sends POST request to login endpoint with valid credentials")
    public void sendLoginRequest() {
        Map<String, Object> requestBody = Map.of("email", email, "password", password);

        response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post(baseUrl + "/login");

        System.out.println("Login request body: " + requestBody);
        System.out.println("Login response: " + response.asString());
    }

    @Then("response body should contain a valid access token and token type")
    public void validateResponseBody() {
        String accessToken = response.jsonPath().getString("access_token");
        String tokenType = response.jsonPath().getString("token_type");

        response.then()
                .body("access_token", notNullValue())
                .body("access_token", instanceOf(String.class))
                .body("token_type", equalTo("bearer"));

        TokenManager.saveToken(accessToken, tokenType);
        System.out.println("Saved access_token and token_type to file.");
    }

    @Then("response headers should include content-length, content-type, date, and server")
    public void validateResponseHeaders() {
        response.then()
                .header("content-length", notNullValue())
                .header("content-type", equalTo("application/json"))
                .header("date", notNullValue())
                .header("server", containsString("uvicorn"));
    }

    @When("user sends POST request to create book endpoint with valid book details")
    public void createBookWithAuthorization() {
        bookData.clear();

        String name = ConfigManager.getProperty("book.name");
        String author = ConfigManager.getProperty("book.author");
        int publishedYear = Integer.parseInt(ConfigManager.getProperty("book.published_year"));
        String summary = ConfigManager.getProperty("book.summary");
        int bookId = 10000 + new Random().nextInt(90000);

        bookData.put("id", bookId);
        bookData.put("name", name);
        bookData.put("author", author);
        bookData.put("published_year", publishedYear);
        bookData.put("book_summary", summary);

        String token = TokenManager.getAccessToken();
        String tokenType = TokenManager.getTokenType();

        response = given().redirects().follow(true)
                .header("Content-Type", "application/json")
                .header("Authorization", tokenType + " " + token)
                .body(bookData)
                .post(baseUrl + "/books/");

        System.out.println("Create Book Request Body: " + bookData);
        System.out.println("Create Book Response: " + response.asString());

        utils.BookManager.saveBookId(bookId);
    }

    @Then("response body should match the book request data")
    public void responseBodyShouldMatchBookRequest() {
        response.then()
                .body("id", equalTo(bookData.get("id")))
                .body("name", equalTo(bookData.get("name")))
                .body("author", equalTo(bookData.get("author")))
                .body("published_year", equalTo(bookData.get("published_year")))
                .body("book_summary", equalTo(bookData.get("book_summary")));

        System.out.println("Book response validated against request body.");
    }

    @When("user updates the book with fields:")
    public void updateBookWithFields(List<String> fieldKeys) {
        bookData.clear();
        int bookId = utils.BookManager.getBookId();
        bookData.put("id", bookId);

        for (String fieldKey : fieldKeys) {
            String jsonField = fieldKeyToJsonField.get(fieldKey);
            String fieldValue = ConfigManager.getProperty(fieldKey);

            if (jsonField == null || fieldValue == null) {
                throw new RuntimeException("Missing or invalid config for: " + fieldKey);
            }

            Object value = jsonField.equals("published_year") ?
                    Integer.parseInt(fieldValue) : fieldValue;

            bookData.put(jsonField, value);
        }

        String token = TokenManager.getAccessToken();
        String tokenType = TokenManager.getTokenType();

        response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", tokenType + " " + token)
                .body(bookData)
                .put(baseUrl + "/books/" + bookId);

        System.out.println("Update Book Request Body: " + bookData);
        System.out.println("Update Book Response: " + response.asString());
    }

    @Then("response body should reflect the updated fields:")
    public void validateUpdatedFields(List<String> fieldKeys) {
        int bookId = utils.BookManager.getBookId();
        response.then().body("id", equalTo(bookId));

        for (String fieldKey : fieldKeys) {
            String jsonField = fieldKeyToJsonField.get(fieldKey);
            String expectedValue = ConfigManager.getProperty(fieldKey);

            if (jsonField == null || expectedValue == null) {
                throw new IllegalArgumentException("Invalid or missing field: " + fieldKey);
            }

            if (jsonField.equals("published_year")) {
                response.then().body(jsonField, equalTo(Integer.parseInt(expectedValue)));
            } else {
                response.then().body(jsonField, equalTo(expectedValue));
            }
        }

        System.out.println("Validated updated fields: " + fieldKeys);
    }

    @When("user sends DELETE request to delete book endpoint with a valid book ID")
    public void deleteBookById() {
        int bookId = utils.BookManager.getBookId();

        String token = TokenManager.getAccessToken();
        String tokenType = TokenManager.getTokenType();

        response = given()
                .header("Authorization", tokenType + " " + token)
                .delete(baseUrl + "/books/" + bookId);

        System.out.println("Delete Book Request ID: " + bookId);
        System.out.println("Delete Book Response: " + response.asString());
    }

    @When("user sends GET request to get book endpoint with a valid book ID")
    public void getBookById() {
        int bookId = utils.BookManager.getBookId(); // Ensure book ID is set from previous create

        String token = TokenManager.getAccessToken();
        String tokenType = TokenManager.getTokenType();

        response = given()
                .header("Authorization", tokenType + " " + token)
                .get(baseUrl + "/books/" + bookId);

        System.out.println("GET Book ID: " + bookId);
        System.out.println("GET Book Response: " + response.asString());
    }

    @Then("response body should contain valid book details")
    public void validateGetBookResponseBody() {
        int expectedId = utils.BookManager.getBookId();

        response.then()
                .body("id", equalTo(expectedId))
                .body("name", notNullValue())
                .body("author", notNullValue())
                .body("published_year", instanceOf(Integer.class))
                .body("book_summary", notNullValue());

        System.out.println("GET book response validated.");
    }

    @When("user sends GET request to health endpoint")
    public void getHealthCheck() {
        response = given()
                .header("Accept", "application/json")
                .get(baseUrl + "/health");

        System.out.println("Health check response: " + response.asString());
    }

    @Then("response body should be:")
    public void validateHealthResponse(io.cucumber.datatable.DataTable table) {
        Map<String, String> expected = table.asMaps().get(0);

        response.then()
                .body("status", equalTo(expected.get("status")));

        System.out.println("Validated health status: " + expected.get("status"));
    }

    @When("user sends GET request to root endpoint")
    public void sendGetRootRequest() {
        response = given()
                .get(baseUrl + "/");

        System.out.println("GET / Response: " + response.asString());
    }

    @Then("response body should contain {string} as {string}")
    public void validateJsonKeyValue(String key, String expectedValue) {
        response.then().body(key, equalTo(expectedValue));
        System.out.println("Validated: " + key + " = " + expectedValue);
    }

    @When("user sends POST request with missing email")
    public void signupWithMissingEmail() {
        Map<String, Object> body = new HashMap<>();
        body.put("password", password); // Email is omitted

        response = given()
                .header("Content-Type", "application/json")
                .body(body)
                .post(baseUrl + "/signup");

        System.out.println("Request (missing email): " + body);
        System.out.println("Response: " + response.asString());
    }

    @Then("response body should contain exact validation error for missing email")
    public void validateValidationErrorForMissingEmail() {
        response.then()
                .body("detail[0].loc", hasItems("body", "email"))
                .body("detail[0].msg", equalTo("field required"))
                .body("detail[0].type", equalTo("value_error.missing"));

        System.out.println("Validated 422 error for missing email.");
    }

    @When("user sends POST request with missing password")
    public void signupWithMissingPassword() {
        Map<String, Object> body = new HashMap<>();
        body.put("email", email); // Password is omitted

        response = given()
                .header("Content-Type", "application/json")
                .body(body)
                .post(baseUrl + "/signup");

        System.out.println("Request (missing password): " + body);
        System.out.println("Response: " + response.asString());
    }

    @Then("response body should contain exact validation error for missing password")
    public void validateValidationErrorForMissingPassword() {
        response.then()
                .body("detail[0].loc", hasItems("body", "password"))
                .body("detail[0].msg", equalTo("field required"))
                .body("detail[0].type", equalTo("value_error.missing"));

        System.out.println("Validated 422 error for missing password.");
    }

    @When("user sends POST request to login with missing password")
    public void loginWithMissingPassword() {
        String email = ConfigManager.getProperty("email");

        Map<String, Object> requestBody = new HashMap<>();
        // Missing password
        requestBody.put("email", email);

        response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(baseUrl + "/login");
    }
    @Then("response should contain validation error for {string} with message {string} and type {string}")
    public void validateFieldIn422ErrorWithDetails(String missingField, String expectedMsg, String expectedType) {
        Object detail = response.jsonPath().get("detail");

        if (detail instanceof List) {
            List<Map<String, Object>> errorList = (List<Map<String, Object>>) detail;

            boolean matchFound = false;

            for (Map<String, Object> error : errorList) {
                Object locObj = error.get("loc");

                if (locObj instanceof List) {
                    List<?> loc = (List<?>) locObj;

                    if (loc.contains(missingField)) {
                        String msg = String.valueOf(error.get("msg"));
                        String type = String.valueOf(error.get("type"));

                        // Assertions
                        assert msg.equals(expectedMsg) : "Expected msg='" + expectedMsg + "', but got '" + msg + "'";
                        assert type.equals(expectedType) : "Expected type='" + expectedType + "', but got '" + type + "'";

                        System.out.printf("✔ Validation error matched: field=%s, msg=%s, type=%s%n", missingField, msg, type);
                        matchFound = true;
                        break;
                    }
                }
            }

            assert matchFound : "Validation error for field '" + missingField + "' not found.\nResponse: " + response.asString();

        } else {
            throw new IllegalStateException("Expected 'detail' to be a list but got: " + detail.getClass() + "\nResponse: " + response.asString());
        }


    }

    @When("user sends POST request to login with missing email")
    public void loginWithMissingEmail() {
        Map<String, Object> payload = new HashMap<>();
        // payload.put("email", ""); // omitting email
        payload.put("password", "test123");

        response = given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/login"); // replace with your actual endpoint
    }

    @Then("status code should be 400 or 422")
    public void validateStatusCode400Or422() {
        int statusCode = response.getStatusCode();
        if (statusCode != 400 && statusCode != 422) {
            throw new AssertionError("Expected status code 400 or 422, but was " + statusCode);
        }
    }

    @Then("response should contain validation error for {string}")
    public void validateFieldIn422Error(String fieldName) {
        boolean found = response.jsonPath()
                .getList("detail").stream()
                .anyMatch(err -> ((Map<String, ?>) err)
                        .get("loc")
                        .toString()
                        .contains(fieldName));

        if (!found) {
            throw new AssertionError("Validation error for field '" + fieldName + "' not found.");
        }
    }
    }






