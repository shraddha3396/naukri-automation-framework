package com.shraddha.automation.pages;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;

import com.shraddha.automation.TestData;
import com.shraddha.automation.utils.*;

public class RecommendedJobsApplyFlow {

    WebDriver driver;
    private static final Logger log = LogManager.getLogger(RecommendedJobsApplyFlow.class);

    static int lastProcessedIndex = -1;

    BotHandler bot;

    public RecommendedJobsApplyFlow(WebDriver driver) {
        this.driver = driver;
        this.bot = new BotHandler(driver);
    }

    // ========================= LOCATORS =========================

    private By recommendedJobsPage = By.xpath("//*[@class='nI-gNb-menus']/li[1]/a/div/span");
    private By jobItems = By.xpath("//div[@class='list']/article");
    private By titles = By.xpath("//div[@class='jobTupleHeader']//p");
    private By expiredJob = By.xpath("//*[text()='Job you are looking for is ']");
    private By appliedBtn = By.xpath("//section[@id='job_header']//div[contains(@class,'apply-button-container')]/span");
    private By jobHeader = By.xpath("//h1[contains(@class,'styles_jd-header-title')]");
    private By company = By.xpath("//*[contains(@class,'styles_job-header-container')]/div[1]/div[1]/div/a");
    private By applyBtn = By.xpath("//section[@id='job_header']//div[contains(@class,'apply-button-container')]/button[2]");
    private By workExperienceMatch = By.xpath("//*[@class='styles_jhc__exp__k_giM']/span[contains(normalize-space(),'4')] | //*[@class='ni-icon-check_circle']/following-sibling::span[text()='Work Experience']");
    private By requiredSkills = By.xpath("//*[@class='styles_left-section-container__btAcB']/section[2]");
    private By location = By.xpath("//*[@id='job_header']//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'pune') " + "or contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'remote')]");
    private By recentActivityTab = By.xpath("//div[@class='tabs-container']/div/div[@id='activity']");
    private By preferenceTab = By.xpath("//div[@class='tabs-container']/div/div[@id='preference']");
    private By appliesTab = By.xpath("//div[@class='tabs-container']/div/div[@id='apply']");
    private By topCandidateTab = By.xpath("//div[@class='tabs-container']/div/div[@id='top_candidate']");
    private By youMayLikeTab = By.xpath("//div[@class='tabs-container']/div/div[@id='similar_jobs']");

    // ========================= PAGE METHODS =========================

    public void clickRecommendedJobs() {
        WaitUtils.click(driver, recommendedJobsPage);
        log.info("Clicked Recommended Jobs");
    }

    public boolean openAppliesTab() {
        return openTab(appliesTab, "Applies Tab");
    }

    public boolean openRecentActivityTab() {
        return openTab(recentActivityTab, "Recent Activity Tab");
    }

    public boolean openTopCandidateTab() {
        return openTab(topCandidateTab, "Top Candidate Tab");
    }

    public boolean openPreferenceTab() {
        return openTab(preferenceTab, "Preference Tab");
    }

    public boolean openYouMayLikeTab() {
        return openTab(youMayLikeTab, "You May Like Tab");
    }

    private boolean openTab(By locator, String tabName) {

        try {

            if (!WaitUtils.isElementPresent(driver, locator)) {
                log.info(tabName + " not present, skipping");
                return false;
            }

            WaitUtils.click(driver, locator);
            log.info("Opened " + tabName);
            return true;

        } catch (Exception e) {
            log.info("Could not open " + tabName + ": " + e.getMessage());
            return false;
        }
    }

    private void switchToNewWindow(String originalWindow) {

        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    // ========================= MAIN FLOW =========================

    public void applyRecommendedJobsBasedOnProfile() {

        WaitUtils.waitForElement(driver, jobItems);
        List<WebElement> jobs = WaitUtils.waitForAllVisible(driver, jobItems, 15);
        log.info("Total recommended jobs found: " + jobs.size());
        while (true) {
            try {

                if (jobs.isEmpty()) {
                    break;
                }

                boolean processedAny = false;

                for (int i = lastProcessedIndex + 1; i < jobs.size(); i++) {

                    try {

                        processJob(i);

                        processedAny = true;

                    } catch (StaleElementReferenceException e) {

                        i--;

                    } catch (Exception e) {

                        log.info("Error processing job: " + e.getMessage());

                        TestData.skippedCount();

                        break;
                    }
                }

                if (!processedAny) {
                    break;
                }

                JavaScriptUtils.scrollDown(driver, 500);
                WaitUtils.waitForAllVisible(driver, jobItems);

            } catch (Exception e) {

                log.info("Loop error: " + e.getMessage());

                break;
            }
        }
    }

    private void processJob(int index) throws InterruptedException {

        List<WebElement> titlesList = WaitUtils.waitForAllVisible(driver, titles);

        if (titlesList.size() <= index) {
            return;
        }

        WebElement titleLink = titlesList.get(index);

        String listingTitle = titleLink.getText().toLowerCase();

        // ========================= TITLE FILTER =========================

        if (!isRelevantTitle(listingTitle)) {
            TestData.skippedCount();
            return;
        }

        lastProcessedIndex = index;

        String originalWindow = driver.getWindowHandle();

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", titleLink);

        switchToNewWindow(originalWindow);

        WaitUtils.waitForEither(driver, jobHeader, expiredJob, 15);
        List<WebElement> expired = WaitUtils.findElements(driver, expiredJob);
        List<WebElement> applied = WaitUtils.findElements(driver, appliedBtn);

        if ((expired.size() > 0 && expired.get(0).isDisplayed()) || (applied.size() > 0 && applied.get(0).isDisplayed())) {

            log.info("Job expired or already applied");
            closeAndReturn(originalWindow);
            return;
        }

        String jobTitle = WaitUtils.getText(driver, jobHeader);
        String companyName = WaitUtils.getText(driver, company);

        log.info("[" + (TestData.getTotal() + 1) + "] Processing: "+ jobTitle + " at " + companyName);

        handleApply();
        driver.close();
        driver.switchTo().window(originalWindow);
        WaitUtils.waitForAllVisible(driver, jobItems);
    }

    private void closeAndReturn(String originalWindow) {

        try {
            driver.close();
        } catch (Exception ignored) {
        }

        try {
            driver.switchTo().window(originalWindow);
        } catch (Exception ignored) {
        }
    }

    // ========================= APPLY LOGIC =========================

    private void handleApply() throws InterruptedException {

        String jobTitle = WaitUtils.getText(driver, jobHeader).toLowerCase();
        String companyName = WaitUtils.getText(driver, company);
        String skills = WaitUtils.getText(driver, requiredSkills).toLowerCase();
        String currentUrl = driver.getCurrentUrl();
        List<WebElement> applyButton = WaitUtils.findElements(driver, applyBtn);
        String buttonText = applyButton.get(0).getText().toLowerCase();
        List<WebElement> workExp = WaitUtils.findElements(driver, workExperienceMatch);
        List<WebElement> locationElements = WaitUtils.findElements(driver, location);

        // ========================= FINAL CONDITIONS =========================

        boolean expMatch = workExp.size() > 0;
        boolean locationMatch = locationElements.size() > 0;
        boolean skillsMatch = isRelevant(skills);

        log.info("Experience Match: " + expMatch);
        log.info("Location Match: " + locationMatch);
        log.info("Skills Match: " + skillsMatch);

        if (expMatch && locationMatch && skillsMatch)
        //if (workExp.size() > 0 && locationElements.size() > 0 && isRelevant(skills)) 
            {

            /*if (isWalkIn(jobTitle, buttonText)) { 
            
            CSVUtil.writeToCSV(jobTitle, companyName, currentUrl, "Walk-in job"); 
            TestData.skippedCount(); 
            } else*/
             if (buttonText.contains("apply on company site")) {

                CSVUtil.writeToCSV(jobTitle, companyName, currentUrl, "Apply on company site");
                log.info("Apply on company site");
                TestData.skippedCount();

            } else {

                WaitUtils.click(driver, applyBtn);

                log.info("Clicked Apply");

                WaitUtils.waitForEither(driver, bot.getSuccessLocator(), bot.getQuestionLocator(), 15);

                if (WaitUtils.isElementPresent(driver, bot.getSuccessLocator())) {

                    log.info("Apply success detected immediately");
                    TestData.appliedCount();

                } else if (WaitUtils.isElementPresent(driver, bot.getQuestionLocator())) {

                    bot.handleBotChallenge();
                    Thread.sleep(2000);
                    verifySuccess();

                } else {

                    log.info("Apply outcome not detected");
                    TestData.skippedCount();
                }
            }

        } else {

            TestData.skippedCount();
            return;
        }
    }

    // ========================= TITLE FILTER =========================

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

    // ========================= SKILL FILTER =========================

    private boolean isRelevant(String skills) {

        return (skills.contains("selenium"))
                && !(skills.contains("robot")
                || skills.contains("spring")
                || skills.contains("aws")
                || skills.contains("salesforce"));
    }

    private void verifySuccess() {

        List<WebElement> success = WaitUtils.findElements(driver, bot.getSuccessLocator());

        if (success.size() > 0 && success.get(0).isDisplayed()) {

            log.info("Application submitted!");
            TestData.appliedCount();

        } else {

            log.info("Apply failed");
            TestData.skippedCount();
        }
    }

    // ========================= TAB FLOWS =========================

    public void applyRecommendedJobsBasedOnApplies() {

        if (!openAppliesTab()) {
            return;
        }

        lastProcessedIndex = -1;

        if (!safeWaitForJobs()) {
            return;
        }

        applyRecommendedJobsBasedOnProfile();
    }

    public void applyRecommendedJobsBasedOnRecentActivity() {

        if (!openRecentActivityTab()) {
            return;
        }

        lastProcessedIndex = -1;

        if (!safeWaitForJobs()) {
            return;
        }

        applyRecommendedJobsBasedOnProfile();
    }

    public void applyRecommendedJobsBasedOnTopCandidate() {

        if (!openTopCandidateTab()) {
            return;
        }

        lastProcessedIndex = -1;

        if (!safeWaitForJobs()) {
            return;
        }

        applyRecommendedJobsBasedOnProfile();
    }

    public void applyRecommendedJobsBasedOnPreferences() {

        if (!openPreferenceTab()) {
            return;
        }

        lastProcessedIndex = -1;

        if (!safeWaitForJobs()) {
            return;
        }

        applyRecommendedJobsBasedOnProfile();
    }

    public void applyRecommendedJobsBasedOnYouMayLike() {

        if (!openYouMayLikeTab()) {
            return;
        }

        lastProcessedIndex = -1;

        if (!safeWaitForJobs()) {
            return;
        }

        applyRecommendedJobsBasedOnProfile();
    }

    private boolean safeWaitForJobs() {

        try {

            WaitUtils.waitForAllVisible(driver, jobItems);
            return true;

        } catch (Exception e) {

            log.info("No jobs visible after opening tab");
            return false;
        }
    }

    // ========================= COMPLETE FLOW =========================

    public void executeFullFlow() {

        clickRecommendedJobs();
        applyRecommendedJobsBasedOnProfile();
        applyRecommendedJobsBasedOnApplies();
        applyRecommendedJobsBasedOnTopCandidate();
        applyRecommendedJobsBasedOnPreferences();
        applyRecommendedJobsBasedOnYouMayLike();
        applyRecommendedJobsBasedOnRecentActivity();
    }
}