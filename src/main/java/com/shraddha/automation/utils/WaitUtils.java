package com.shraddha.automation.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class WaitUtils {

    private static final int DEFAULT_TIMEOUT = 15;
    private static final int DEFAULT_RETRY = 3;

    // ========================= CORE WAIT =========================

    private static WebDriverWait getWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    // ========================= BASIC WAITS =========================

    public static WebElement waitForElement(WebDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForElement(WebDriver driver, By locator, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static List<WebElement> waitForAllVisible(WebDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static List<WebElement> waitForAllVisible(WebDriver driver, By locator, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static List<WebElement> findElements(WebDriver driver, By locator) {
        return driver.findElements(locator);
    }

    // ========================= ACTIONS (SAFE + RETRY) =========================

    // 🔥 Default safe click
    public static void click(WebDriver driver, By locator) {
        clickWithRetry(driver, locator, DEFAULT_RETRY);
    }

    public static void clickWithRetry(WebDriver driver, By locator, int attempts) {

        for (int i = 0; i < attempts; i++) {
            try {
                waitForClickable(driver, locator).click();
                return;

            } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
                if (i == attempts - 1) {
                    throw e;
                }
            }
        }
    }

    // 🔥 SendKeys safe
    public static void sendKeys(WebDriver driver, By locator, String value) {
        sendKeysWithRetry(driver, locator, value, DEFAULT_RETRY);
    }

    public static void sendKeysWithRetry(WebDriver driver, By locator, String value, int attempts) {

        for (int i = 0; i < attempts; i++) {
            try {
                WebElement element = waitForElement(driver, locator);
                element.clear();
                element.sendKeys(value);
                return;

            } catch (StaleElementReferenceException e) {
                if (i == attempts - 1) {
                    throw e;
                }
            }
        }
    }

    // ========================= TEXT / STATE =========================

    public static String getText(WebDriver driver, By locator) {
        return waitForElement(driver, locator).getText();
    }

    public static boolean isElementPresent(WebDriver driver, By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    // ========================= ADVANCED WAITS =========================

    public static void waitForEither(WebDriver driver, By locator1, By locator2, int timeoutSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT)).until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(locator1),ExpectedConditions.presenceOfElementLocated(locator2)));
        } catch (Exception ignored) {}
    }

    public static WebElement fluentWait(WebDriver driver, By locator) {

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(20))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class);

        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}