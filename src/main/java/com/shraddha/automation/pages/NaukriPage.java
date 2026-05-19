package com.shraddha.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;

import com.shraddha.automation.utils.WaitUtils;
import com.shraddha.automation.utils.JavaScriptUtils;
import com.shraddha.automation.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NaukriPage {

    private WebDriver driver;
    private static final Logger log = LogManager.getLogger(NaukriPage.class);

    // ================= PAGE FACTORY ELEMENTS =================

    @FindBy(id = "login_Layer")
    private WebElement loginLayer;

    @FindBy(xpath = "//input[@placeholder='Enter your active Email ID / Username']")
    private WebElement usernameField;

    @FindBy(xpath = "//input[@placeholder='Enter your password']")
    private WebElement passwordField;

    @FindBy(xpath = "//button[contains(text(),'Login')]")
    private WebElement loginButton;

    @FindBy(xpath = "//*[@class='view-profile-wrapper']/a")
    private WebElement viewProfile;

    @FindBy(xpath = "//*[@class='icon edit ']")
    private WebElement editProfile;

    @FindBy(xpath = "//*[@id='saveBasicDetailsBtn']")
    private WebElement saveProfile;

    @FindBy(xpath = "//*[@class='ltCont']/div[2]/div/span")
    private WebElement closePopup;

    @FindBy(xpath = "//div[contains(@class, 'nI-gNb-sb__main')]")
    private WebElement searchBar;

    @FindBy(xpath = "//div[@class='nI-gNb-sb__keywords']//input")
    private WebElement keywordInput;

    @FindBy(xpath = "//*[@class='ni-gnb-icn ni-gnb-icn-expand-more']")
    private WebElement expandMore;

    @FindBy(xpath = "//*[@id='sa-dd-scrollexperienceDD']//li[@title='4 years']/div/span")
    private WebElement experience4Years;

    @FindBy(xpath = "//*[@placeholder='Enter location']")
    private WebElement locationInput;

    @FindBy(xpath = "//*[@class='nI-gNb-sb__icon-wrapper']")
    private WebElement searchIcon;

    @FindBy(xpath = "//*[text()='Role category']/parent::div/following-sibling::div/a/span")
    private WebElement roleCategory;

    @FindBy(xpath = "//*[@placeholder='Search Role category']")
    private WebElement roleSearch;

    @FindBy(xpath = "//*[@id='tooltip']/div[2]/div[2]/div/div/div[1]//i")
    private WebElement filterOption1;

    @FindBy(xpath = "//*[@id='tooltip']/div[2]/div[2]/div/div/div[2]//i")
    private WebElement filterOption2;

    @FindBy(xpath = "//*[@id='tooltip']/div[2]//div[@class='styles_filter-apply-btn__MDAUd ']")
    private WebElement applyFilter;

    @FindBy(xpath = "//div[@data-filter-id='freshness']")
    private WebElement freshnessFilter;

    @FindBy(xpath = "//a[@data-id='filter-freshness-1']")
    private WebElement last1Day;

    private By jobResultCards = By.xpath("//div[contains(@class,'srp-jobtuple-wrapper')]");   
    static long errorTime = System.currentTimeMillis();

    public NaukriPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public WebElement getLoginLayer() {
        return loginLayer;
    }

    public WebElement getUsernameField() {
        return usernameField;
    }

    public WebElement getPasswordField() {
        return passwordField;
    }

    public WebElement getClosePopup() {
        return closePopup;
    }

    public WebElement getSearchBar() {
        return searchBar;
    }

    // ================= BUSINESS FLOWS =================

    public void login(String username, String password) {
        try {
            // Click login layer
            WaitUtils.click(driver, By.id("login_Layer"));

            // Wait for and fill username
            WaitUtils.waitForElement(driver, By.xpath("//input[@placeholder='Enter your active Email ID / Username']"));
            usernameField.clear();
            usernameField.sendKeys(username);

            // Fill password
            passwordField.clear();
            passwordField.sendKeys(password);

            // Click login button
            WaitUtils.click(driver, By.xpath("//button[contains(text(),'Login')]"));

            // Wait for successful login - look for profile elements or dashboard
            try {
                WaitUtils.waitForElement(driver, By.xpath("//*[@class='view-profile-wrapper']/a"), 10);
               } catch (Exception e) {
                // Try alternative locators for successful login
                try {
                    WaitUtils.waitForElement(driver, By.xpath("//a[contains(@href,'/mnjuser/profile')]"), 5);
                    } catch (Exception e2) {
                    }
            }

        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            throw e;
        }
    }

    public void updateProfile() {
        try {
            // Dismiss chatbot overlay if present
            dismissChatbotOverlay();

            // Wait for and click view profile
            WaitUtils.waitForClickable(driver, By.xpath("//*[@class='view-profile-wrapper']/a"));
            viewProfile.click();

            // Wait for and click edit profile
            WaitUtils.waitForClickable(driver, By.xpath("//*[@class='icon edit ']"));
            editProfile.click();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                log.warn("Thread interrupted during sleep");
            }

            // Scroll and save
            WaitUtils.waitForClickable(driver, By.xpath("//*[@id='saveBasicDetailsBtn']"), 10);
            JavaScriptUtils.scrollDown(driver, 400);
            // Use JavaScript click to avoid element interception issues
            JavaScriptUtils.clickElement(driver, driver.findElement(By.xpath("//*[@id='saveBasicDetailsBtn']")));

            // Close popup if it appears
            try {
                WaitUtils.waitForClickable(driver, By.xpath("//*[@class='ltCont']/div[2]/div/span"), 5);
                closePopup.click();
            } catch (Exception e) {
                log.info("Profile popup close button not found or not clicked");
            }

        } catch (Exception e) {
            throw e;
        }
    }

    private void dismissChatbotOverlay() {
        try {
            // Check if chatbot overlay is present
            By overlayLocator = By.xpath("//div[contains(@class,'chatbot_Overlay') and contains(@class,'show')]");
            if (WaitUtils.isElementPresent(driver, overlayLocator)) {

                // Try to click the close button if available
                try {
                    By closeButton = By.xpath("//div[contains(@class,'chatbot_Overlay')]//button[contains(@class,'close') or contains(@title,'close') or contains(text(),'×')]");
                    if (WaitUtils.isElementPresent(driver, closeButton)) {
                        WaitUtils.click(driver, closeButton);
                        return;
                    }
                } catch (Exception e) {
                    log.debug("No close button found on chatbot overlay");
                }

                // Try to click outside the overlay to dismiss
                try {
                    JavaScriptUtils.clickElement(driver, driver.findElement(By.xpath("//body")));
                } catch (Exception e) {
                    log.warn("Could not dismiss chatbot overlay: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.debug("No chatbot overlay present or already dismissed");
        }
    }

    public void searchJobs(String keyword, String location) {
        try {
            // Click search bar
            WaitUtils.waitForClickable(driver, By.xpath("//div[contains(@class, 'nI-gNb-sb__main')]"));
            // Use JavaScript click to avoid element interception issues
            JavaScriptUtils.clickElement(driver, driver.findElement(By.xpath("//div[contains(@class, 'nI-gNb-sb__main')]")));

            // Enter keyword
            WaitUtils.waitForElement(driver, By.xpath("//div[@class='nI-gNb-sb__keywords']//input"));
            keywordInput.clear();
            keywordInput.sendKeys(keyword);

            // Select experience
            WaitUtils.waitForClickable(driver, By.xpath("//*[@class='ni-gnb-icn ni-gnb-icn-expand-more']"));
            expandMore.click();
            WaitUtils.waitForClickable(driver, By.xpath("//*[@id='sa-dd-scrollexperienceDD']//li[@title='4 years']/div/span"));
            experience4Years.click();

            // Enter location
            WaitUtils.waitForElement(driver, By.xpath("//*[@placeholder='Enter location']"));
            locationInput.clear();
            locationInput.sendKeys(location);

            // Click search
            WaitUtils.waitForClickable(driver, By.xpath("//*[@class='nI-gNb-sb__icon-wrapper']"));
            searchIcon.click();

            WaitUtils.waitForElement(driver, jobResultCards);

            // Apply additional filters
            try {
                JavaScriptUtils.scrollDown(driver, 600);

                // Role category filter
                WaitUtils.waitForClickable(driver, By.xpath("//*[text()='Role category']/parent::div/following-sibling::div/a/span"), 5);
                roleCategory.click();

                WaitUtils.waitForElement(driver, By.xpath("//*[@placeholder='Search Role category']"), 5);
                roleSearch.clear();
                roleSearch.sendKeys("Quality assurance");

                WaitUtils.waitForClickable(driver, By.xpath("//*[@id='tooltip']/div[2]/div[2]/div/div/div[1]//i"), 5);
                filterOption1.click();

                WaitUtils.waitForClickable(driver, By.xpath("//*[@id='tooltip']/div[2]/div[2]/div/div/div[2]//i"), 5);
                filterOption2.click();

                WaitUtils.waitForClickable(driver, By.xpath("//*[@id='tooltip']/div[2]//div[@class='styles_filter-apply-btn__MDAUd ']"), 5);
                applyFilter.click();
                WaitUtils.waitForElement(driver, jobResultCards);

                // Freshness filter
                By freshnessLocator = By.xpath("//div[@data-filter-id='freshness']");
                By last1DayLocator = By.xpath("//a[@data-id='filter-freshness-1']");

                // Wait for page stabilization after role filter
                WaitUtils.waitForElement(driver, jobResultCards, 15);

                try {

                    // Retry because Naukri refreshes DOM frequently
                    for (int attempt = 1; attempt <= 3; attempt++) {

                        try {

                            WebElement freshnessElement = driver.findElement(freshnessLocator);

                            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});",freshnessElement);

                            WaitUtils.waitForClickable(driver, freshnessLocator, 10);
                            driver.findElement(freshnessLocator).click();
                            WaitUtils.waitForClickable(driver, last1DayLocator, 10);
                            WebElement oldCard = driver.findElement(jobResultCards);
                            driver.findElement(last1DayLocator).click();
                            new WebDriverWait(driver, java.time.Duration.ofSeconds(15)).until(ExpectedConditions.stalenessOf(oldCard));
                            WaitUtils.waitForElement(driver, jobResultCards, 15);

                            log.info("Freshness filter applied successfully");
                            break;

                        } catch (StaleElementReferenceException e) {

                            log.warn("Retrying freshness filter due to stale element - Attempt {}", attempt);

                            if (attempt == 3) {
                                throw e;
                            }
                        }
                    }

                } catch (Exception e) {

                    log.warn("Some filters could not be applied: {}", e.getMessage());
                    return;
                }
            } catch (Exception e) {
                log.warn("Some filters could not be applied: {}", e.getMessage());
                return;                
            }
        } catch (Exception e) {
            log.error("Job search failed: {}", e.getMessage());
            throw e;
        }
    }

    public void executeFullFlow() {
        login(ConfigReader.get("username"), ConfigReader.get("password"));
        updateProfile();
        searchJobs("selenium", "Pune");
    }

    public void executeFullFlow(String keyword, String location) {
        login(ConfigReader.get("username"), ConfigReader.get("password"));
        updateProfile();
        searchJobs(keyword, location);
    }
}