package com.shraddha.automation.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;

public class AssertUtil {

    private static final int DEFAULT_TIMEOUT = 10;

    // ================= ELEMENT ASSERTIONS =================

    public static void assertElementPresent(WebDriver driver, WebElement element, String message) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
            wait.until(ExpectedConditions.visibilityOf(element));
            Assert.assertTrue(element.isDisplayed(), message + " - Element not visible");
        } catch (Exception e) {
            Assert.fail(message + " - " + e.getMessage());
        }
    }

    public static void assertElementNotPresent(WebDriver driver, WebElement element, String message) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
            boolean isInvisible = wait.until(ExpectedConditions.invisibilityOf(element));
            Assert.assertTrue(isInvisible, message + " - Element still visible");
        } catch (Exception e) {
            Assert.fail(message + " - " + e.getMessage());
        }
    }

    public static void assertElementClickable(WebDriver driver, WebElement element, String message) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
            wait.until(ExpectedConditions.elementToBeClickable(element));
            Assert.assertTrue(element.isEnabled(), message + " - Element not clickable");
        } catch (Exception e) {
            Assert.fail(message + " - " + e.getMessage());
        }
    }

    // ================= TEXT ASSERTIONS =================

    public static void assertTextEquals(WebElement element, String expectedText, String message) {
        String actualText = element.getText().trim();
        Assert.assertEquals(actualText, expectedText, message);
    }

    public static void assertTextContains(WebElement element, String expectedText, String message) {
        String actualText = element.getText();
        Assert.assertTrue(actualText.contains(expectedText),
            message + " - Expected: '" + expectedText + "', Actual: '" + actualText + "'");
    }

    // ================= URL ASSERTIONS =================

    public static void assertUrlContains(WebDriver driver, String expectedUrlPart, String message) {
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(expectedUrlPart),
            message + " - URL: " + currentUrl + " does not contain: " + expectedUrlPart);
    }

    public static void assertUrlEquals(WebDriver driver, String expectedUrl, String message) {
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, expectedUrl, message);
    }

    // ================= TITLE ASSERTIONS =================

    public static void assertTitleEquals(WebDriver driver, String expectedTitle, String message) {
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle, expectedTitle, message);
    }

    public static void assertTitleContains(WebDriver driver, String expectedTitlePart, String message) {
        String actualTitle = driver.getTitle();
        Assert.assertTrue(actualTitle.contains(expectedTitlePart),
            message + " - Title: " + actualTitle + " does not contain: " + expectedTitlePart);
    }

    // ================= ATTRIBUTE ASSERTIONS =================

    public static void assertAttributeEquals(WebElement element, String attribute, String expectedValue, String message) {
        String actualValue = element.getAttribute(attribute);
        Assert.assertEquals(actualValue, expectedValue,
            message + " - Attribute '" + attribute + "' value: " + actualValue + " != " + expectedValue);
    }

    public static void assertAttributeContains(WebElement element, String attribute, String expectedValue, String message) {
        String actualValue = element.getAttribute(attribute);
        Assert.assertTrue(actualValue != null && actualValue.contains(expectedValue),
            message + " - Attribute '" + attribute + "' value: " + actualValue + " does not contain: " + expectedValue);
    }

    // ================= CUSTOM ASSERTIONS =================

    public static void assertPageLoadComplete(WebDriver driver, String message) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
            .executeScript("return document.readyState").equals("complete"));
    }

    public static void assertNoJavaScriptErrors(WebDriver driver, String message) {
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
        Object result = js.executeScript(
            "return window.jsErrors || [];");
        Assert.assertTrue(result.toString().equals("[]"),
            message + " - JavaScript errors found: " + result);
    }
}