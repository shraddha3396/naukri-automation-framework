package com.shraddha.automation.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;
import com.aventstack.extentreports.*;
import com.shraddha.automation.config.ConfigReader;
import com.shraddha.automation.reports.ExtentManager;
import com.shraddha.automation.utils.DriverFactory;
import com.shraddha.automation.utils.ScreenshotUtil;

public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);

    private static final ExtentReports extent = ExtentManager.getInstance();
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // ================= GET TEST INSTANCE =================
    public static ExtentTest getTest() {
        return test.get();
    }

    // ================= STEP LOGGING =================
    public static void logStep(String message) {
        if (test.get() != null) {
            test.get().info("➡ " + message);
        }
    }

    public static void logPass(String message) {
        if (test.get() != null) {
            test.get().pass("✅ " + message);
        }
        log.info("PASS: " + message);
    }

    public static void logFail(String message) {
        if (test.get() != null) {
            test.get().fail("❌ " + message);
        }
        log.error("FAIL: " + message);
    }

    // ================= TEST START =================
    @Override
    public void onTestStart(ITestResult result) {

        String testName = result.getMethod().getMethodName();
        String browser = ConfigReader.get("browser");

        ExtentTest extentTest = extent.createTest(testName).assignCategory(browser).assignCategory(result.getMethod().getGroups());

        test.set(extentTest);

        log.info("🚀 Test Started: " + testName);
        extentTest.info("🚀 Test Started: " + testName);

        // Custom flow log (kept from your logic)
        if ("applyJobsTest".equals(testName)) {
            log.info("🚀 Starting full job apply flow");
            extentTest.info("🚀 Starting full job apply flow");
        }
    }

    // ================= TEST SUCCESS =================
    @Override
    public void onTestSuccess(ITestResult result) {

        logPass("Test Passed Successfully");

        DriverFactory.quitDriver();
    }

    // ================= TEST FAILURE =================
    @Override
    public void onTestFailure(ITestResult result) {

        String errorMessage = result.getThrowable() != null
                ? result.getThrowable().getMessage()
                : "Unknown failure";

        logFail(errorMessage);

        if (test.get() != null) {
            test.get().fail("⚠ Failure Summary: " + errorMessage);
        }

        try {
            String path = ScreenshotUtil.capture(
                    DriverFactory.getDriver(),
                    result.getMethod().getMethodName()
            );

            if (test.get() != null) {
                test.get().addScreenCaptureFromPath(path, "Failure Screenshot");
            }

        } catch (Exception e) {
            logFail("Screenshot failed: " + e.getMessage());
        }

        DriverFactory.quitDriver();
    }

    // ================= TEST SKIPPED =================
    @Override
    public void onTestSkipped(ITestResult result) {

        logStep("⚠ Test Skipped");

        DriverFactory.quitDriver();
    }

    // ================= SUITE END =================
    @Override
    public void onFinish(ITestContext context) {

        log.info("📊 Flushing Extent Report...");
        extent.flush();
    }
}