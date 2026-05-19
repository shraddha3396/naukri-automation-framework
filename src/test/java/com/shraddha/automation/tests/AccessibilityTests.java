package com.shraddha.automation.tests;

import org.testng.annotations.Test;
import com.shraddha.automation.base.BaseTest;
import com.shraddha.automation.accessibility.AccessibilityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccessibilityTests extends BaseTest {

    private static final Logger log = LogManager.getLogger(AccessibilityTests.class);

    @Test(groups = {"accessibility"})
    public void fullAccessibilityScanTest() {
        log.info("Running full accessibility scan test");

        AccessibilityUtils.runAccessibilityScan(driver);
    }

    @Test(groups = {"accessibility"})
    public void colorContrastTest() {
        log.info("Running color contrast test");

        AccessibilityUtils.checkColorContrast(driver);
    }

    @Test(groups = {"accessibility"})
    public void keyboardNavigationTest() {
        log.info("Running keyboard navigation test");

        AccessibilityUtils.checkKeyboardNavigation(driver);
    }

    @Test(groups = {"accessibility"})
    public void imageAltTextTest() {
        log.info("Running image alt text test");

        AccessibilityUtils.checkImageAltText(driver);
    }

    @Test(groups = {"accessibility"})
    public void formLabelsTest() {
        log.info("Running form labels test");

        AccessibilityUtils.checkFormLabels(driver);
    }

    @Test(groups = {"accessibility"})
    public void pageStructureTest() {
        log.info("Running page structure test");

        AccessibilityUtils.validatePageStructure(driver);
    }
}