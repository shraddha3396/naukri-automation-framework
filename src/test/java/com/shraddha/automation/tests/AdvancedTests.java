package com.shraddha.automation.tests;

import org.testng.annotations.Test;
import com.shraddha.automation.base.BaseTest;
import com.shraddha.automation.api.ApiUtils;
import com.shraddha.automation.database.DatabaseUtils;
import com.shraddha.automation.mobile.MobileUtils;
import com.shraddha.automation.performance.PerformanceUtils;
import com.shraddha.automation.security.SecurityUtils;
import com.shraddha.automation.accessibility.AccessibilityUtils;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdvancedTests extends BaseTest {

    private static final Logger log = LogManager.getLogger(AdvancedTests.class);

    @Test(groups = {"api"})
    public void apiTest() {
        log.info("Running API test");

        ApiUtils.setBaseURI("https://jsonplaceholder.typicode.com");

        Response response = ApiUtils.get("/posts/1");
        ApiUtils.validateStatusCode(response, 200);
        ApiUtils.validateResponseTime(response, 5);

        String title = ApiUtils.getJsonValue(response, "title");
        assert title != null : "Title should not be null";
    }

    @Test(groups = {"database"})
    public void databaseTest() {
        log.info("Running database test");

        DatabaseUtils.connectH2("./target/testdb");

        DatabaseUtils.createTable(
            "CREATE TABLE test_users (id INT PRIMARY KEY, name VARCHAR(50), email VARCHAR(50))"
        );

        DatabaseUtils.insertTestData(
            "INSERT INTO test_users (id, name, email) VALUES (?, ?, ?)",
            1, "John Doe", "john@example.com"
        );

        boolean exists = DatabaseUtils.verifyRecordExists("test_users", "name", "John Doe");
        assert exists : "User should exist in database";

        DatabaseUtils.disconnect();
    }

    @Test(groups = {"performance"})
    public void performanceTest() {
        log.info("Running performance test");

        // Monitor page load time
        PerformanceUtils.monitorPageLoadTime(driver, "Naukri Homepage");

        // Check memory usage
        PerformanceUtils.checkMemoryUsage(driver);

        // Run simple load test (commented out to avoid external calls)
        // PerformanceUtils.createSimpleLoadTest("https://naukri.com", "target/performance-results.jtl", 10, 5, 20);
    }

    @Test(groups = {"security"})
    public void securityTest() {
        log.info("Running security test");

        // Validate input
        SecurityUtils.validateInput("test@example.com", "email");
        SecurityUtils.validateInput("John Doe", "name");

        // Check for common vulnerabilities
        SecurityUtils.checkForSQLInjection("SELECT * FROM users");
        SecurityUtils.checkForXSS("<script>alert('test')</script>");

        // Session validation
        SecurityUtils.validateSessionTimeout(driver, "https://naukri.com");
    }

    @Test(groups = {"accessibility"})
    public void accessibilityTest() {
        log.info("Running accessibility test");

        // Run full accessibility scan
        AccessibilityUtils.runAccessibilityScan(driver);

        // Check specific accessibility aspects
        AccessibilityUtils.checkColorContrast(driver);
        AccessibilityUtils.checkKeyboardNavigation(driver);
        AccessibilityUtils.checkImageAltText(driver);
        AccessibilityUtils.checkFormLabels(driver);

        // Validate page structure
        AccessibilityUtils.validatePageStructure(driver);
    }

    @Test(groups = {"mobile"}, enabled = false) // Disabled by default - requires Appium server
    public void mobileTest() {
        log.info("Running mobile test");

        // This would require Appium server running
        // MobileUtils.initAndroidDriver("http://127.0.0.1:4723/wd/hub",
        //                              "emulator-5554", "11.0", "/path/to/app.apk");

        // Mobile gestures would be tested here
        // MobileUtils.swipeUp(driver, 500, 100, 300);
    }
}