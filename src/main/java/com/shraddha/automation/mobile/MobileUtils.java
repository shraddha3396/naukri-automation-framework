package com.shraddha.automation.mobile;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.URL;
import java.time.Duration;

public class MobileUtils {

    private static final Logger log = LogManager.getLogger(MobileUtils.class);
    private static AppiumDriver driver;

    // ================= DRIVER INITIALIZATION =================

    public static AppiumDriver initAndroidDriver(String appiumServerUrl,
                                                               String deviceName,
                                                               String platformVersion,
                                                               String appPath) {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("deviceName", deviceName);
            capabilities.setCapability("platformVersion", platformVersion);
            capabilities.setCapability("app", appPath);
            capabilities.setCapability("automationName", "UiAutomator2");
            capabilities.setCapability("noReset", true);
            capabilities.setCapability("newCommandTimeout", 300);

            driver = new AndroidDriver(new URL(appiumServerUrl), capabilities);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            log.info("Android driver initialized successfully for device: {}", deviceName);
            return driver;

        } catch (Exception e) {
            log.error("Failed to initialize Android driver: {}", e.getMessage());
            throw new RuntimeException("Android driver initialization failed", e);
        }
    }

    public static AppiumDriver initIOSDriver(String appiumServerUrl,
                                                           String deviceName,
                                                           String platformVersion,
                                                           String appPath) {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("platformName", "iOS");
            capabilities.setCapability("deviceName", deviceName);
            capabilities.setCapability("platformVersion", platformVersion);
            capabilities.setCapability("app", appPath);
            capabilities.setCapability("automationName", "XCUITest");
            capabilities.setCapability("noReset", true);
            capabilities.setCapability("newCommandTimeout", 300);

            driver = new IOSDriver(new URL(appiumServerUrl), capabilities);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            log.info("iOS driver initialized successfully for device: {}", deviceName);
            return driver;

        } catch (Exception e) {
            log.error("Failed to initialize iOS driver: {}", e.getMessage());
            throw new RuntimeException("iOS driver initialization failed", e);
        }
    }

    // ================= MOBILE GESTURES =================

    public static void swipeUp(AppiumDriver driver, int startY, int endY, int centerX) {
        try {
            // Use TouchAction for swipe in newer Appium versions
            new io.appium.java_client.TouchAction((io.appium.java_client.PerformsTouchActions) driver)
                .press(io.appium.java_client.touch.offset.PointOption.point(centerX, startY))
                .waitAction(io.appium.java_client.touch.WaitOptions.waitOptions(java.time.Duration.ofMillis(1000)))
                .moveTo(io.appium.java_client.touch.offset.PointOption.point(centerX, endY))
                .release()
                .perform();
            log.info("Performed swipe up gesture");
        } catch (Exception e) {
            log.error("Failed to perform swipe up: {}", e.getMessage());
        }
    }

    public static void swipeDown(AppiumDriver driver, int startY, int endY, int centerX) {
        try {
            new io.appium.java_client.TouchAction((io.appium.java_client.PerformsTouchActions) driver)
                .press(io.appium.java_client.touch.offset.PointOption.point(centerX, endY))
                .waitAction(io.appium.java_client.touch.WaitOptions.waitOptions(java.time.Duration.ofMillis(1000)))
                .moveTo(io.appium.java_client.touch.offset.PointOption.point(centerX, startY))
                .release()
                .perform();
            log.info("Performed swipe down gesture");
        } catch (Exception e) {
            log.error("Failed to perform swipe down: {}", e.getMessage());
        }
    }

    public static void swipeLeft(AppiumDriver driver, int startX, int endX, int centerY) {
        try {
            new io.appium.java_client.TouchAction((io.appium.java_client.PerformsTouchActions) driver)
                .press(io.appium.java_client.touch.offset.PointOption.point(startX, centerY))
                .waitAction(io.appium.java_client.touch.WaitOptions.waitOptions(java.time.Duration.ofMillis(1000)))
                .moveTo(io.appium.java_client.touch.offset.PointOption.point(endX, centerY))
                .release()
                .perform();
            log.info("Performed swipe left gesture");
        } catch (Exception e) {
            log.error("Failed to perform swipe left: {}", e.getMessage());
        }
    }

    public static void swipeRight(AppiumDriver driver, int startX, int endX, int centerY) {
        try {
            new io.appium.java_client.TouchAction((io.appium.java_client.PerformsTouchActions) driver)
                .press(io.appium.java_client.touch.offset.PointOption.point(endX, centerY))
                .waitAction(io.appium.java_client.touch.WaitOptions.waitOptions(java.time.Duration.ofMillis(1000)))
                .moveTo(io.appium.java_client.touch.offset.PointOption.point(startX, centerY))
                .release()
                .perform();
            log.info("Performed swipe right gesture");
        } catch (Exception e) {
            log.error("Failed to perform swipe right: {}", e.getMessage());
        }
    }

    public static void tap(AppiumDriver driver, int x, int y) {
        try {
            new io.appium.java_client.TouchAction((io.appium.java_client.PerformsTouchActions) driver)
                .tap(io.appium.java_client.touch.offset.PointOption.point(x, y))
                .perform();
            log.info("Performed tap at coordinates: ({}, {})", x, y);
        } catch (Exception e) {
            log.error("Failed to perform tap: {}", e.getMessage());
        }
    }

    public static void longPress(AppiumDriver driver, WebElement element, int duration) {
        try {
            new io.appium.java_client.TouchAction((io.appium.java_client.PerformsTouchActions) driver)
                .longPress(io.appium.java_client.touch.offset.ElementOption.element(element))
                .waitAction(io.appium.java_client.touch.WaitOptions.waitOptions(java.time.Duration.ofMillis(duration)))
                .release()
                .perform();
            log.info("Performed long press on element for {} ms", duration);
        } catch (Exception e) {
            log.error("Failed to perform long press: {}", e.getMessage());
        }
    }

    // ================= DEVICE ACTIONS =================

    public static void pressBackButton() {
        driver.navigate().back();
        log.info("Pressed back button");
    }

    public static void pressHomeButton() {
        driver.executeScript("mobile: pressKey", "{\"keycode\": 3}");
        log.info("Pressed home button");
    }

    public static void rotateDevice(String orientation) {
        try {
            // Rotation may not be available in all Appium versions
            if (driver instanceof io.appium.java_client.android.AndroidDriver) {
                if ("LANDSCAPE".equalsIgnoreCase(orientation)) {
                    ((io.appium.java_client.android.AndroidDriver) driver).rotate(org.openqa.selenium.ScreenOrientation.LANDSCAPE);
                } else {
                    ((io.appium.java_client.android.AndroidDriver) driver).rotate(org.openqa.selenium.ScreenOrientation.PORTRAIT);
                }
                log.info("Device rotated to: {}", orientation);
            } else {
                log.warn("Device rotation not supported for this platform");
            }
        } catch (Exception e) {
            log.error("Failed to rotate device: {}", e.getMessage());
        }
    }

    public static void hideKeyboard() {
        try {
            // Keyboard methods may not be available in all Appium versions
            if (driver instanceof io.appium.java_client.android.AndroidDriver) {
                ((io.appium.java_client.android.AndroidDriver) driver).hideKeyboard();
                log.info("Keyboard hidden");
            } else {
                log.warn("Hide keyboard not supported for this platform");
            }
        } catch (Exception e) {
            log.error("Failed to hide keyboard: {}", e.getMessage());
        }
    }

    // ================= UTILITY METHODS =================

    public static String getDeviceTime() {
        try {
            // Device time method may not be available in all Appium versions
            // return driver.getDeviceTime(); // Commented out - method not available
            return java.time.LocalDateTime.now().toString();
        } catch (Exception e) {
            log.error("Failed to get device time: {}", e.getMessage());
            return java.time.LocalDateTime.now().toString();
        }
    }

    public static void takeScreenshot(String fileName) {
        try {
            String screenshotPath = "mobile-screenshots/" + fileName + ".png";
            java.io.File screenshot = driver.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
            org.apache.commons.io.FileUtils.copyFile(screenshot, new java.io.File(screenshotPath));
            log.info("Screenshot saved: {}", screenshotPath);
        } catch (Exception e) {
            log.error("Failed to take screenshot: {}", e.getMessage());
        }
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            log.info("Mobile driver quit successfully");
        }
    }

    public static AppiumDriver getDriver() {
        return driver;
    }
}