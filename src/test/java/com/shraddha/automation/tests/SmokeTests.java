package com.shraddha.automation.tests;

import org.testng.annotations.Test;
import com.shraddha.automation.base.BaseTest;
import com.shraddha.automation.pages.NaukriPage;
import com.shraddha.automation.utils.AssertUtil;
import com.shraddha.automation.performance.PerformanceUtils;
import com.shraddha.automation.accessibility.AccessibilityUtils;
import com.shraddha.automation.security.SecurityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SmokeTests extends BaseTest {

    private static final Logger log = LogManager.getLogger(SmokeTests.class);
    private NaukriPage naukriPage;

    @Override
    protected void onSetupComplete() {
        naukriPage = new NaukriPage(driver);
    }

    @Test(groups = {"smoke"})
    public void homepageLoadTest() {
        log.info("Running homepage load test");

        AssertUtil.assertTitleContains(driver, "Naukri", "Page title should contain Naukri");
        AssertUtil.assertElementPresent(driver, naukriPage.getLoginLayer(), "Login layer should be visible");

        PerformanceUtils.monitorPageLoadTime(driver, "Homepage");
    }

    @Test(groups = {"smoke"})
    public void accessibilityCheck() {
        log.info("Running smoke accessibility check");

        AccessibilityUtils.validatePageStructure(driver);
        AccessibilityUtils.checkImageAltTags(driver);
    }

    @Test(groups = {"smoke"})
    public void securityValidation() {
        log.info("Running smoke security validation");

        SecurityUtils.validateInput("test@example.com", "email");
        SecurityUtils.validateSessionTimeout(driver, driver.getCurrentUrl());
    }
}