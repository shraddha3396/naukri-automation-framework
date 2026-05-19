package com.shraddha.automation.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.shraddha.automation.TestData;
import com.shraddha.automation.utils.*;
import java.util.List;

public class SearchedJobsApplyFlow {

    private WebDriver driver;
    private static final Logger log = LogManager.getLogger(SearchedJobsApplyFlow.class);
    private int lastProcessedIndex = -1;
    private boolean lastOpenedInNewWindow = false;

    public SearchedJobsApplyFlow(WebDriver driver) {
        this.driver = driver;
    }

    // ================= LOCATORS =================

    private By jobCards = By.xpath("//div[@class='srp-jobtuple-wrapper']");
    private By titles = By.xpath("//div[@class='srp-jobtuple-wrapper']//h2/a");
    private By jobTitle = By.xpath("//h1[contains(@class,'styles_jd-header-title')]");
    private By company = By.xpath("//*[contains(@class,'styles_job-header-container')]/div[1]/div[1]/div/a");
    private By applyBtn = By.xpath("//section[@id='job_header']//div[contains(@class,'apply-button-container')]/button[2]");
    private By appliedBtn = By.xpath("//section[@id='job_header']//div[contains(@class,'apply-button-container')]/span");
    private By requiredSkills = By.xpath("//*[@class='styles_left-section-container__btAcB']/section[2]");
    private By nextBtn = By.xpath("//*[@id='lastCompMark']/a/span[text()='Next']");
    private By workExperienceMatch = By.xpath("//*[@class='styles_jhc__exp__k_giM']/span[contains(normalize-space(),'4')] | //*[@class='ni-icon-check_circle']/following-sibling::span[text()='Work Experience']");
    private By location = By.xpath("//*[@id='job_header']//*[text()='Remote' or text()='Pune']");

    // ================= MAIN FLOW =================

    public void applyJobs() {
        log.info("Total searched jobs found: " + driver.findElements(jobCards).size());
        while (true) {

            try {

                WaitUtils.waitForEither(driver, titles, jobCards, 15);

            } catch (Exception e) {
            }

            int i = 0;
            boolean processedAny = false;

            while (true) {

                List<WebElement> titleElements = driver.findElements(titles);

                // Exit when no more jobs
                if (i >= titleElements.size()) {
                    break;
                }

                try {

                    WebElement titleElement = titleElements.get(i);
                    String listingTitle = titleElement.getText().toLowerCase();

                    // ================= TITLE FILTER =================

                    if (!isRelevantTitle(listingTitle)) {
                        i++;
                        continue;
                    }

                    processedAny = true;
                    openJob(i);
                    processJob();
                    closeAndSwitchBack();
                    i++;

                } catch (StaleElementReferenceException e) {

                    log.info("Stale → retry same index: " + i);

                } catch (Exception e) {

                    log.error("Error processing job: " + e.getMessage());
                    TestData.skippedCount();
                    i++;
                }
            }

            if (!processedAny || !goToNextPage()) {

                log.info("No more pages → exiting");
                break;                
            }
        }
    }

    // ================= PAGE ACTIONS =================

    private void openJob(int index) {

        String originalWindow = driver.getWindowHandle();
        By jobLink = By.xpath("(//div[@class='srp-jobtuple-wrapper']//h2/a)[" + (index + 1) + "]");

        // Prevent invalid click
        if (!WaitUtils.isElementPresent(driver, jobLink)) {
            return;
        }

        WaitUtils.waitForClickable(driver, jobLink);
        WebElement job = driver.findElement(jobLink);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", job);

        lastOpenedInNewWindow = switchToNewWindow(originalWindow);
        WaitUtils.waitForElement(driver, jobTitle);
    }

    private void processJob() {

        String title = WaitUtils.waitForElement(driver, jobTitle).getText();
        String comp = WaitUtils.waitForElement(driver, company).getText();
        String url = driver.getCurrentUrl();
        log.info("[" + (TestData.getTotal() + 1) + "]Processing: "  + title + " | " + comp);
        List<WebElement> applied = driver.findElements(appliedBtn);
        List<WebElement> apply = driver.findElements(applyBtn);
        List<WebElement> workExperience = driver.findElements(workExperienceMatch);
        List<WebElement> locationElements = WaitUtils.findElements(driver, location);
        String skills = WaitUtils.waitForElement(driver, requiredSkills).getText().toLowerCase();
        String btnText = apply.isEmpty() ? "" : apply.get(0).getText().toLowerCase();
        String jobTitles = title.toLowerCase();

        // Already applied
        if (!applied.isEmpty() && applied.get(0).isDisplayed()) {

            TestData.skippedCount();
            return;
        }

        // Apply button missing
        if (apply.isEmpty()) {

            TestData.skippedCount();
            return;
        }

        // FINAL CONDITIONS
        if (isRelevant(skills, jobTitles) && !workExperience.isEmpty() && !locationElements.isEmpty()) {

            /*if (isWalkIn(title, btnText)) {

                CSVUtil.writeToCSV(title, comp, url, "Walk-in job");
                TestData.skippedCount();
                return;
            }*/

            if (btnText.contains("apply on company site")) {

                CSVUtil.writeToCSV(title, comp, url, "External apply");
                TestData.skippedCount();
                return;

            } else {

                apply.get(0).click();
                log.info("Clicked Apply");
                BotHandler bot = new BotHandler(driver);

                WaitUtils.waitForEither(driver, bot.getSuccessLocator(), bot.getQuestionLocator(), 15);

                if (WaitUtils.isElementPresent(driver, bot.getSuccessLocator())) {

                    log.info("Apply success detected immediately");
                    TestData.appliedCount();

                } else if (WaitUtils.isElementPresent(driver, bot.getQuestionLocator())) {

                    bot.handleBotChallenge();
                    verifySuccess(bot);

                } else {

                    log.info("Apply outcome not detected after click");
                    TestData.skippedCount();
                }
            }

        } else {

            TestData.skippedCount();
        }
    }

    private void verifySuccess(BotHandler bot) {

        try {

            WebElement success = WaitUtils.waitForElement(driver, bot.getSuccessLocator());

            if (success.isDisplayed()) {

                log.info("Application success");
                TestData.appliedCount();
            }

        } catch (Exception e) {

            log.info("Application failed");
            TestData.skippedCount();
        }
    }

    private void closeAndSwitchBack() {

        if (lastOpenedInNewWindow) {

            driver.close();

            for (String handle : driver.getWindowHandles()) {

                driver.switchTo().window(handle);
                break;
            }

        } else {

            driver.navigate().back();
        }

        WaitUtils.waitForElement(driver, jobCards);
        WaitUtils.waitForAllVisible(driver, titles);
    }

    private boolean goToNextPage() {

    try {

        By nextButtonLocator = By.xpath("//*[@id='lastCompMark']/a");
        List<WebElement> nextButtons = driver.findElements(nextButtonLocator);

        // No next button
        if (nextButtons.isEmpty()) {

            log.info("No Next button found → last page");
            return false;
        }

        WebElement nextElement = nextButtons.get(0);

        // Disabled next button
        String disabled = nextElement.getAttribute("disabled");
        String classValue = nextElement.getAttribute("class");

        if (disabled != null || (classValue != null && classValue.contains("disabled"))) {

            return false;
        }

        // Not clickable/displayed
        if (!nextElement.isDisplayed() || !nextElement.isEnabled()) {

            log.info("Next button not clickable → last page");
            return false;
        }

        String oldUrl = driver.getCurrentUrl();

        JavaScriptUtils.scrollToElement(driver, nextElement);

        try {

            WaitUtils.waitForClickable(driver, nextButtonLocator, 10);
            nextElement.click();

        } catch (ElementClickInterceptedException e) {

            log.info("Normal click intercepted, using JS click");
            JavaScriptUtils.clickElement(driver, nextElement);
        }

        log.info("Clicked Next");

        new WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(ExpectedConditions.not(
                        ExpectedConditions.urlToBe(oldUrl)
                ));

        WaitUtils.waitForEither(driver, titles, jobCards, 15);

        lastProcessedIndex = -1;

        log.info("New page loaded");

        return true;

    } catch (Exception e) {

        log.info("Failed to navigate next: " + e.getMessage());
        return false;
    }
}
    // ================= HELPERS =================

    private boolean isWalkIn(String title, String btnText) {

        return title.toLowerCase().contains("walk-in")
                || title.toLowerCase().contains("face to face")
                || title.toLowerCase().contains("in person")
                || btnText.contains("walk-in")
                || btnText.contains("walk in")
                || btnText.contains("walkin")
                || btnText.contains("f2f");
    }

    // ================= TITLE FILTER =================

    private boolean isRelevantTitle(String title) {

        title = title.toLowerCase();

        return (title.contains("test")
                || title.contains("qa")
                || title.contains("quality")
                || title.contains("automation"))
                && !title.contains("api")
                && !title.contains("mobile")
                && !title.contains("python")
                && !title.contains("salesforce")
                && !title.contains("guidewire")
                && !title.contains("etl")
                && !title.contains("tosca")
                && !title.contains("c++")
                && !title.contains("c#")
                && !title.contains(".net")
                && !title.contains("sharepoint")
                && !title.contains("mainframe");
    }

    // ================= SKILL FILTER =================



    private boolean isRelevant(String skills, String jobTitles) {

        return (skills.contains("selenium"))
                && !(skills.contains("robot")
                || skills.contains("spring")
                || skills.contains("aws"));
    }

    private boolean switchToNewWindow(String original) {

        for (String handle : driver.getWindowHandles()) {

            if (!handle.equals(original)) {

                driver.switchTo().window(handle);
                return true;
            }
        }
        return false;
    }
}