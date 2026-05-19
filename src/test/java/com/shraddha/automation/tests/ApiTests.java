package com.shraddha.automation.tests;

import org.testng.annotations.Test;
import com.shraddha.automation.base.BaseTest;
import com.shraddha.automation.api.ApiUtils;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApiTests extends BaseTest {

    private static final Logger log = LogManager.getLogger(ApiTests.class);

    @Test(groups = {"api"})
    public void getPostsTest() {
        log.info("Running GET posts API test");

        ApiUtils.setBaseURI("https://jsonplaceholder.typicode.com");

        Response response = ApiUtils.get("/posts");
        ApiUtils.validateStatusCode(response, 200);
        ApiUtils.validateResponseTime(response, 5);
    }

    @Test(groups = {"api"})
    public void getSinglePostTest() {
        log.info("Running GET single post API test");

        ApiUtils.setBaseURI("https://jsonplaceholder.typicode.com");

        Response response = ApiUtils.get("/posts/1");
        ApiUtils.validateStatusCode(response, 200);

        String title = ApiUtils.getJsonValue(response, "title");
        assert title != null && !title.isEmpty() : "Post title should not be empty";
    }

    @Test(groups = {"api"})
    public void createPostTest() {
        log.info("Running POST create post API test");

        ApiUtils.setBaseURI("https://jsonplaceholder.typicode.com");

        String requestBody = """
            {
                "title": "Test Post",
                "body": "This is a test post",
                "userId": 1
            }
            """;

        Response response = ApiUtils.post("/posts", requestBody);
        ApiUtils.validateStatusCode(response, 201);

        String postId = ApiUtils.getJsonValue(response, "id");
        assert postId != null : "Created post should have an ID";
    }

    @Test(groups = {"api"})
    public void updatePostTest() {
        log.info("Running PUT update post API test");

        ApiUtils.setBaseURI("https://jsonplaceholder.typicode.com");

        String requestBody = """
            {
                "id": 1,
                "title": "Updated Test Post",
                "body": "This is an updated test post",
                "userId": 1
            }
            """;

        Response response = ApiUtils.put("/posts/1", requestBody);
        ApiUtils.validateStatusCode(response, 200);
    }

    @Test(groups = {"api"})
    public void deletePostTest() {
        log.info("Running DELETE post API test");

        ApiUtils.setBaseURI("https://jsonplaceholder.typicode.com");

        Response response = ApiUtils.delete("/posts/1");
        ApiUtils.validateStatusCode(response, 200);
    }
}