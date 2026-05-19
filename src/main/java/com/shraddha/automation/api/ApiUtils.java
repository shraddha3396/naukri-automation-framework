package com.shraddha.automation.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

public class ApiUtils {

    private static final Logger log = LogManager.getLogger(ApiUtils.class);

    // ================= API BASE CONFIGURATION =================

    public static void setBaseURI(String baseURI) {
        RestAssured.baseURI = baseURI;
        log.info("API Base URI set to: {}", baseURI);
    }

    public static void setBasePath(String basePath) {
        RestAssured.basePath = basePath;
        log.info("API Base Path set to: {}", basePath);
    }

    // ================= HTTP METHODS =================

    public static Response get(String endpoint) {
        log.info("Performing GET request to: {}", endpoint);
        Response response = given()
                .when()
                .get(endpoint);
        logResponse(response);
        return response;
    }

    public static Response get(String endpoint, Map<String, String> headers) {
        log.info("Performing GET request to: {} with headers", endpoint);
        RequestSpecification request = given();
        if (headers != null) {
            request.headers(headers);
        }
        Response response = request.when().get(endpoint);
        logResponse(response);
        return response;
    }

    public static Response post(String endpoint, Object body) {
        log.info("Performing POST request to: {}", endpoint);
        Response response = given()
                .contentType("application/json")
                .body(body)
                .when()
                .post(endpoint);
        logResponse(response);
        return response;
    }

    public static Response post(String endpoint, Object body, Map<String, String> headers) {
        log.info("Performing POST request to: {} with headers", endpoint);
        RequestSpecification request = given().contentType("application/json");
        if (headers != null) {
            request.headers(headers);
        }
        Response response = request.body(body).when().post(endpoint);
        logResponse(response);
        return response;
    }

    public static Response put(String endpoint, Object body) {
        log.info("Performing PUT request to: {}", endpoint);
        Response response = given()
                .contentType("application/json")
                .body(body)
                .when()
                .put(endpoint);
        logResponse(response);
        return response;
    }

    public static Response delete(String endpoint) {
        log.info("Performing DELETE request to: {}", endpoint);
        Response response = given()
                .when()
                .delete(endpoint);
        logResponse(response);
        return response;
    }

    // ================= RESPONSE VALIDATION =================

    public static void validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        if (actualStatusCode != expectedStatusCode) {
            throw new AssertionError("Expected status code: " + expectedStatusCode +
                                   ", but got: " + actualStatusCode +
                                   ". Response: " + response.getBody().asString());
        }
        log.info("Status code validation passed: {}", actualStatusCode);
    }

    public static void validateResponseTime(Response response, long maxTimeInSeconds) {
        long responseTime = response.getTimeIn(TimeUnit.SECONDS);
        if (responseTime > maxTimeInSeconds) {
            throw new AssertionError("Response time exceeded: " + responseTime +
                                   " seconds (max allowed: " + maxTimeInSeconds + " seconds)");
        }
        log.info("Response time validation passed: {} seconds", responseTime);
    }

    public static String getJsonValue(Response response, String jsonPath) {
        return response.jsonPath().getString(jsonPath);
    }

    // ================= AUTHENTICATION =================

    public static void setBasicAuth(String username, String password) {
        RestAssured.authentication = RestAssured.basic(username, password);
        log.info("Basic authentication set for user: {}", username);
    }

    public static void setBearerToken(String token) {
        RestAssured.authentication = RestAssured.oauth2(token);
        log.info("Bearer token authentication set");
    }

    // ================= UTILITY METHODS =================

    private static void logResponse(Response response) {
        log.info("Response Status: {}", response.getStatusCode());
        log.info("Response Time: {} ms", response.getTime());
        log.debug("Response Body: {}", response.getBody().asString());
    }

    public static void resetRestAssured() {
        RestAssured.reset();
        log.info("RestAssured configuration reset");
    }
}