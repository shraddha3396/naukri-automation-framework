package com.shraddha.automation.locators;

/**
 * Locators for BotHandler - Contains all web element locators for Naukri bot interactions
 * Following Page Object Model best practices with centralized locator management
 */
public class BotHandlerLocators {

    // ================= BOT CHAT INTERFACE =================
    public static final String QUESTION_LOCATOR = "xpath://div[contains(@id,'userInput')]/ancestor::div[contains(@class,'footerInputBoxWrapper')]/preceding::li[contains(@class,'botItem')][1]//span";
    public static final String TEXT_INPUT = "xpath://*[@class='chatbot_MessageContainer']//div[@class='footerInputBoxWrapper']//div[@contenteditable='true' or @data-placeholder='Type message here...' or @class='textArea']";

    // ================= RADIO BUTTONS =================
    public static final String RADIO_OPTIONS = "xpath://*[@class='singleselect-radiobutton']/div/div/div | //*[@class='ssrc__radio-btn-container']/label";

    // ================= CHIP ITEMS =================
    public static final String CHIP_ITEMS = "xpath://*[@class='chatbot_MessageContainer']//div[@class='chatbot_Chip chipInRow chipItem']//span";

    // ================= CHECKBOXES =================
    public static final String CHECKBOX_LABELS = "xpath://input[@type='checkbox']/following-sibling::label | //label[.//input[@type='checkbox']]";
    public static final String CHECKBOX_INPUTS = "xpath://input[@type='checkbox']";

    // ================= ACTION BUTTONS =================
    public static final String SUBMIT_BUTTON = "xpath://*[@class='sendMsg']";
    public static final String SKIP_BUTTON = "xpath://button[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'skip') or contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'later') or contains(text(),'Let me apply')]";

    // ================= SUCCESS INDICATORS =================
    public static final String BOT_SUCCESS = "xpath://*[contains(text(),'Applied to')]";

    // ================= DYNAMIC LOCATORS =================
    public static String getRadioOptionByText(String text) {
        return "xpath://label[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + text.toLowerCase() + "')]";
    }

    public static String getCheckboxByText(String text) {
        return "xpath://label[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + text.toLowerCase() + "')]//input[@type='checkbox']";
    }

    public static String getSuccessMessageByJobTitle(String jobTitle) {
        return "xpath://*[contains(text(),'Applied to') and contains(text(),'" + jobTitle + "')]";
    }
}