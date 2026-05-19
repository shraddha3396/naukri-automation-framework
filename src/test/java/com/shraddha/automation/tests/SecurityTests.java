package com.shraddha.automation.tests;

import org.testng.annotations.Test;
import com.shraddha.automation.base.BaseTest;
import com.shraddha.automation.security.SecurityUtils;
import com.shraddha.automation.accessibility.AccessibilityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SecurityTests extends BaseTest {

    private static final Logger log = LogManager.getLogger(SecurityTests.class);

    @Test(groups = {"security"})
    public void inputValidationTest() {
        log.info("Running input validation test");

        // Test valid inputs
        SecurityUtils.validateInput("test@example.com", "email");
        SecurityUtils.validateInput("John Doe", "name");

        // Test invalid inputs (these should throw exceptions)
        try {
            SecurityUtils.validateInput("", "email");
            assert false : "Empty email should have failed validation";
        } catch (IllegalArgumentException e) {
            log.info("Correctly caught validation error: {}", e.getMessage());
        }
    }

    @Test(groups = {"security"})
    public void sqlInjectionPreventionTest() {
        log.info("Running SQL injection prevention test");

        // Test safe inputs
        SecurityUtils.checkForSQLInjection("normal input");
        SecurityUtils.checkForSQLInjection("user search query");

        // Test suspicious inputs (should be logged as warnings)
        SecurityUtils.checkForSQLInjection("'; DROP TABLE users; --");
        SecurityUtils.checkForSQLInjection("SELECT * FROM users");
    }

    @Test(groups = {"security"})
    public void xssPreventionTest() {
        log.info("Running XSS prevention test");

        // Test safe inputs
        SecurityUtils.checkForXSS("normal text");
        SecurityUtils.checkForXSS("user comment");

        // Test suspicious inputs (should be logged as warnings)
        SecurityUtils.checkForXSS("<script>alert('xss')</script>");
        SecurityUtils.checkForXSS("javascript:alert('test')");
    }
}