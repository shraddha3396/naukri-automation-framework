package com.shraddha.automation.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import com.shraddha.automation.TestData;
import com.shraddha.automation.config.ConfigReader;
import com.shraddha.automation.utils.DriverFactory;
import com.shraddha.automation.utils.SummaryUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;
    
    @BeforeTest
    public void beforeTest() {
        log.info("Starting test suite: " + getClass().getSimpleName());
    }

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        SummaryUtil.startTimer();
        log.info("Initializing WebDriver and navigating to URL");
        driver = DriverFactory.initDriver();
        driver.get(ConfigReader.get("url"));
        onSetupComplete();  // Call hook for subclasses
    }

    // Hook method for subclasses to override
    protected void onSetupComplete() {
        // Override this in subclasses to do additional setup after driver initialization
    }

    @AfterMethod
    public void tearDown() {
        log.info("Closing browser");
        DriverFactory.quitDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void generateSummary() {
        log.info("Executing AfterSuite summary generation");
        SummaryUtil.Summary();
        TestData.clear();
    }
}