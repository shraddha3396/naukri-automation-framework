package com.shraddha.automation.locators;

/**
 * Common Locators - Contains reusable locator constants used across multiple pages
 * Following Page Object Model best practices with centralized locator management
 */
public class CommonLocators {

    // ================= BROWSER ELEMENTS =================
    public static final String PAGE_TITLE = "tagName:title";
    public static final String BODY = "tagName:body";
    public static final String HTML = "tagName:html";

    // ================= COMMON BUTTONS =================
    public static final String SUBMIT_BUTTON = "xpath://input[@type='submit'] | //button[@type='submit'] | //button[contains(text(),'Submit')]";
    public static final String CANCEL_BUTTON = "xpath://button[contains(text(),'Cancel')] | //a[contains(text(),'Cancel')]";
    public static final String CLOSE_BUTTON = "xpath://button[contains(@class,'close')] | //span[contains(@class,'close')] | //a[contains(text(),'×')]";
    public static final String BACK_BUTTON = "xpath://button[contains(text(),'Back')] | //a[contains(text(),'Back')]";

    // ================= COMMON FORM ELEMENTS =================
    public static final String INPUT_FIELDS = "xpath://input[@type='text'] | //input[@type='email'] | //input[@type='password'] | //textarea";
    public static final String DROPDOWN_SELECTS = "xpath://select";
    public static final String CHECKBOXES = "xpath://input[@type='checkbox']";
    public static final String RADIO_BUTTONS = "xpath://input[@type='radio']";

    // ================= LOADING AND WAITING =================
    public static final String LOADING_SPINNER = "xpath://div[contains(@class,'loading')] | //div[contains(@class,'spinner')] | //div[contains(@class,'progress')]";
    public static final String LOADING_OVERLAY = "xpath://div[contains(@class,'overlay') and contains(@class,'loading')]";

    // ================= ERROR AND SUCCESS MESSAGES =================
    public static final String ERROR_MESSAGES = "xpath://div[contains(@class,'error')] | //span[contains(@class,'error')] | //p[contains(@class,'error')]";
    public static final String SUCCESS_MESSAGES = "xpath://div[contains(@class,'success')] | //span[contains(@class,'success')] | //p[contains(@class,'success')]";
    public static final String WARNING_MESSAGES = "xpath://div[contains(@class,'warning')] | //span[contains(@class,'warning')] | //p[contains(@class,'warning')]";

    // ================= NAVIGATION =================
    public static final String BREADCRUMB = "xpath://nav[contains(@class,'breadcrumb')] | //ol[contains(@class,'breadcrumb')]";
    public static final String PAGINATION = "xpath://div[contains(@class,'pagination')] | //nav[contains(@class,'pagination')]";

    // ================= MODALS AND POPUPS =================
    public static final String MODAL_OVERLAY = "xpath://div[contains(@class,'modal')] | //div[contains(@class,'popup')] | //div[contains(@class,'overlay')]";
    public static final String MODAL_CLOSE = "xpath://button[contains(@class,'modal-close')] | //span[contains(@class,'modal-close')]";

    // ================= TABLES =================
    public static final String TABLE_ROWS = "xpath://table//tr | //tbody//tr";
    public static final String TABLE_HEADERS = "xpath://table//th | //thead//th";
    public static final String TABLE_CELLS = "xpath://table//td | //tbody//td";

    // ================= IMAGES AND MEDIA =================
    public static final String IMAGES = "xpath://img";
    public static final String BROKEN_IMAGES = "xpath://img[@src='' or not(@src)]";

    // ================= ACCESSIBILITY =================
    public static final String MISSING_ALT_IMAGES = "xpath://img[not(@alt) or @alt='']";
    public static final String EMPTY_LINKS = "xpath://a[not(text()) and not(@title)]";
    public static final String MISSING_LABEL_INPUTS = "xpath://input[not(@id) or not(@aria-label) or not(@aria-labelledby)]";

    // ================= DYNAMIC LOCATORS =================
    public static String getElementById(String id) {
        return "id:" + id;
    }

    public static String getElementByClass(String className) {
        return "className:" + className;
    }

    public static String getElementByName(String name) {
        return "name:" + name;
    }

    public static String getElementByText(String text) {
        return "xpath://*[contains(text(),'" + text + "')]";
    }

    public static String getButtonByText(String text) {
        return "xpath://button[contains(text(),'" + text + "')] | //input[@type='button' and @value='" + text + "'] | //a[contains(text(),'" + text + "')]";
    }

    public static String getLinkByText(String text) {
        return "xpath://a[contains(text(),'" + text + "')]";
    }

    public static String getInputByPlaceholder(String placeholder) {
        return "xpath://input[@placeholder='" + placeholder + "'] | //textarea[@placeholder='" + placeholder + "']";
    }

    public static String getElementByDataAttribute(String attribute, String value) {
        return "xpath://*[@data-" + attribute + "='" + value + "']";
    }
}