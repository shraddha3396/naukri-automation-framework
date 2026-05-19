package com.shraddha.automation.locators;

/**
 * Locators for NaukriPage - Contains all web element locators for Naukri website interactions
 * Following Page Object Model best practices with centralized locator management
 */
public class NaukriPageLocators {

    // ================= LOGIN SECTION =================
    public static final String LOGIN_LAYER = "id:login_Layer";
    public static final String USERNAME_FIELD = "xpath://input[@placeholder='Enter your active Email ID / Username']";
    public static final String PASSWORD_FIELD = "xpath://input[@placeholder='Enter your password']";
    public static final String LOGIN_BUTTON = "xpath://button[contains(text(),'Login')]";

    // ================= PROFILE SECTION =================
    public static final String VIEW_PROFILE = "xpath://*[@class='view-profile-wrapper']/a";
    public static final String EDIT_PROFILE = "xpath://*[@class='icon edit ']";
    public static final String SAVE_PROFILE = "xpath://*[@id='saveBasicDetailsBtn']";
    public static final String CLOSE_POPUP = "xpath://*[@class='ltCont']/div[2]/div/span";

    // ================= SEARCH SECTION =================
    public static final String SEARCH_BAR = "xpath://div[contains(@class, 'nI-gNb-sb__main')]";
    public static final String KEYWORD_INPUT = "xpath://div[@class='nI-gNb-sb__keywords']//input";
    public static final String LOCATION_INPUT = "xpath://div[@class='nI-gNb-sb__locations']//input";
    public static final String SEARCH_BUTTON = "xpath://button[contains(@class, 'nI-gNb-sb__icon-wrapper')]";

    // ================= JOB LISTINGS =================
    public static final String JOB_CARDS = "xpath://article[contains(@class, 'jobTuple')]";
    public static final String JOB_TITLES = "xpath://article[contains(@class, 'jobTuple')]//a[contains(@class, 'title')]";
    public static final String COMPANY_NAMES = "xpath://article[contains(@class, 'jobTuple')]//a[contains(@class, 'company')]";
    public static final String APPLY_BUTTONS = "xpath://article[contains(@class, 'jobTuple')]//button[contains(text(),'Apply')]";

    // ================= FILTERS =================
    public static final String FRESHNESS_FILTER = "xpath://div[@data-filter-id='freshness']";
    public static final String LAST_1_DAY = "xpath://a[@data-id='filter-freshness-1']";
    public static final String SALARY_FILTER = "xpath://div[@data-filter-id='salaryRange']";
    public static final String EXPERIENCE_FILTER = "xpath://div[@data-filter-id='experience']";

    // ================= PAGINATION =================
    public static final String NEXT_PAGE = "xpath://a[contains(@class, 'next')]";
    public static final String PREVIOUS_PAGE = "xpath://a[contains(@class, 'prev')]";
    public static final String PAGE_NUMBERS = "xpath://div[contains(@class, 'pagination')]//a";

    // ================= NOTIFICATIONS =================
    public static final String NOTIFICATION_BELL = "xpath://*[@class='notification']";
    public static final String NOTIFICATION_COUNT = "xpath://*[@class='notification-count']";

    // ================= NAVIGATION =================
    public static final String HOME_LINK = "xpath://a[contains(text(),'Home')]";
    public static final String MY_PROFILE_LINK = "xpath://a[contains(text(),'My Profile')]";
    public static final String LOGOUT_LINK = "xpath://a[contains(text(),'Logout')]";

    // ================= DYNAMIC LOCATORS =================
    public static String getJobCardByIndex(int index) {
        return "xpath:(//article[contains(@class, 'jobTuple')])[" + index + "]";
    }

    public static String getApplyButtonByJobTitle(String jobTitle) {
        return "xpath://article[contains(@class, 'jobTuple') and .//a[contains(@class, 'title') and contains(text(),'" + jobTitle + "')]]//button[contains(text(),'Apply')]";
    }

    public static String getCompanyLinkByName(String companyName) {
        return "xpath://a[contains(@class, 'company') and contains(text(),'" + companyName + "')]";
    }
}