package com.shraddha.automation.tests;

import org.testng.annotations.Test;
import com.shraddha.automation.base.BaseTest;
import com.shraddha.automation.mobile.MobileUtils;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MobileTests extends BaseTest {

    private static final Logger log = LogManager.getLogger(MobileTests.class);
    private AppiumDriver mobileDriver;

    @Test(groups = {"mobile"}, enabled = false) // Disabled by default - requires Appium server
    public void androidBasicTest() {
        log.info("Running Android basic test");

        try {
            // Initialize Android driver
            mobileDriver = MobileUtils.initAndroidDriver(
                "http://127.0.0.1:4723/wd/hub",
                "emulator-5554",
                "11.0",
                "/path/to/your/app.apk" // Replace with actual APK path
            );

            // Basic mobile interactions
            MobileUtils.swipeUp(mobileDriver, 800, 200, 540);
            Thread.sleep(2000);

            MobileUtils.swipeDown(mobileDriver, 200, 800, 540);
            Thread.sleep(2000);

            // Take screenshot
            MobileUtils.takeScreenshot("mobile_test_screenshot");

            // Get device info
            String deviceTime = MobileUtils.getDeviceTime();
            log.info("Device time: {}", deviceTime);

        } catch (Exception e) {
            log.error("Android test failed: {}", e.getMessage());
            throw new RuntimeException("Mobile test failed", e);
        } finally {
            if (mobileDriver != null) {
                MobileUtils.quitDriver();
            }
        }
    }

    @Test(groups = {"mobile"}, enabled = false) // Disabled by default - requires Appium server
    public void iosBasicTest() {
        log.info("Running iOS basic test");

        try {
            // Initialize iOS driver
            mobileDriver = MobileUtils.initIOSDriver(
                "http://127.0.0.1:4723/wd/hub",
                "iPhone Simulator",
                "15.0",
                "/path/to/your/app.app" // Replace with actual app path
            );

            // Basic iOS interactions
            MobileUtils.swipeLeft(mobileDriver, 300, 100, 400);
            Thread.sleep(2000);

            MobileUtils.swipeRight(mobileDriver, 100, 300, 400);
            Thread.sleep(2000);

            // Device rotation
            MobileUtils.rotateDevice("LANDSCAPE");
            Thread.sleep(2000);
            MobileUtils.rotateDevice("PORTRAIT");

        } catch (Exception e) {
            log.error("iOS test failed: {}", e.getMessage());
            throw new RuntimeException("iOS test failed", e);
        } finally {
            if (mobileDriver != null) {
                MobileUtils.quitDriver();
            }
        }
    }
}