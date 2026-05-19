package com.shraddha.automation.pages;

import org.openqa.selenium.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.shraddha.automation.utils.WaitUtils;
import com.shraddha.automation.TestData;
import com.shraddha.automation.locators.BotHandlerLocators;
import java.util.List;

public class BotHandler {

    private WebDriver driver;
    private static final Logger log = LogManager.getLogger(BotHandler.class);

    public BotHandler(WebDriver driver) {
        this.driver = driver;
    }

    // ================= LOCATOR HELPER =================

    private By getLocator(String locatorString) {
        String[] parts = locatorString.split(":", 2);
        String type = parts[0];
        String value = parts[1];

        switch (type) {
            case "id": return By.id(value);
            case "xpath": return By.xpath(value);
            case "css": return By.cssSelector(value);
            case "className": return By.className(value);
            case "name": return By.name(value);
            case "tagName": return By.tagName(value);
            default: return By.xpath(value);
        }
    }

    // ================= LOCATORS =================

    private By questionLocator = getLocator(BotHandlerLocators.QUESTION_LOCATOR);
    private By textInput = getLocator(BotHandlerLocators.TEXT_INPUT);
    private By radioOptions = getLocator(BotHandlerLocators.RADIO_OPTIONS);
    private By chipItems = getLocator(BotHandlerLocators.CHIP_ITEMS);
    private By checkboxLabels = getLocator(BotHandlerLocators.CHECKBOX_LABELS);
    private By checkboxInputs = getLocator(BotHandlerLocators.CHECKBOX_INPUTS);
    private By submitBtn = getLocator(BotHandlerLocators.SUBMIT_BUTTON);
    private By skipBtn = getLocator(BotHandlerLocators.SKIP_BUTTON);
    private By botSuccess = getLocator(BotHandlerLocators.BOT_SUCCESS);

    // ================= MAIN FLOW =================

    public boolean handleBotChallenge() {
        String currentUrl = driver.getCurrentUrl();

        try {

            while (true) {
                WaitUtils.waitForEither(driver, questionLocator, botSuccess, 15);

                String question = getQuestion();

                if (question == null || question.trim().isEmpty()) {
                    Thread.sleep(3000);
                    if (WaitUtils.isElementPresent(driver, botSuccess)) {
                        return true;
                    }
                    WaitUtils.waitForEither(driver, questionLocator, botSuccess, 10);
                    continue;
                }

                String lowerQ = question.toLowerCase();
                log.info("→ Bot Question: " + question);

                // Exit condition
                if (lowerQ.contains("thank you") || lowerQ.contains("thank you for your responses.")) {
                    return true;
                }

                // ❌ Walk-in skip
                if (isWalkInQuestion(lowerQ)) {
                    TestData.skippedCount();
                    return false;
                }

                boolean handled = handleText(question);
                if (!handled) handled = handleRadio(question);
                if (!handled) handled = handleCheckbox(question);
                if (!handled) handled = handleChipItems();
                if (!handled) handled = handleSkip();

                if (!handled) {
                    log.info("❌ Could not handle question." + " Job url: " + currentUrl);
                    TestData.skippedCount();
                    return false;
                }

                handleSubmitAndWait();

                if (WaitUtils.isElementPresent(driver, botSuccess)) {
                    return true;
                }

                // continue to next question loop
            }

        } catch (Exception e) {
            log.error("Bot flow failed: " + " Job url: " + currentUrl + " Error: " + e.getMessage());
            return false;
        }
    }

    // ================= QUESTION =================

    private String getQuestion() {
        try {
            WaitUtils.waitForEither(driver, botSuccess, questionLocator, 10);
            List<WebElement> q = driver.findElements(questionLocator);
            return q.isEmpty() ? null : q.get(0).getText();
        } catch (StaleElementReferenceException e) {
            return getQuestion();
        }
    }

    private boolean isWalkInQuestion(String q) {
        return q.contains("walk-in") || q.contains("walk in")
                || q.contains("walkin") || q.contains("face to face")
                || q.contains("in person");
    }

    // ================= TEXT =================

    private boolean handleText(String question) {

        try {
            List<WebElement> input = driver.findElements(textInput);

            if (!input.isEmpty() && input.get(0).isDisplayed()) {

                String answer = TestData.getAnswerForQuestion(question);

                if (!answer.isEmpty()) {

                    WebElement el = input.get(0);

                    el.click();
                    el.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
                    el.sendKeys(answer);

                    log.info("✓ Text Answer: " + answer);
                    return true;
                }
            }

        } catch (StaleElementReferenceException e) {
            return handleText(question);
        }

        return false;
    }

    // ================= RADIO =================

    private boolean handleRadio(String question) {

        try {
            List<WebElement> radios = driver.findElements(radioOptions);

            if (!radios.isEmpty()) {
                String answer = TestData.getRadioButtonAnswer(question);

                if (!answer.isEmpty()) {
                    return selectRadioAdvanced(answer);
                }
            }

        } catch (StaleElementReferenceException e) {
            return handleRadio(question);
        }

        return false;
    }

    private boolean selectRadioAdvanced(String answer) {

        String ans = answer.toLowerCase().trim();

        try {
            List<WebElement> labels = driver.findElements(
                    By.xpath("//label[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + ans + "')]")
            );

            for (WebElement label : labels) {
                if (label.isDisplayed()) {
                    label.click();
                    log.info("✓ Radio selected: " + answer);
                    return true;
                }
            }

            // fallback
            List<WebElement> radios = driver.findElements(By.xpath("//*[@class='singleselect-radiobutton-container']/div[1]/label"));
            for (WebElement radio : radios) {
                if (radio.isDisplayed() && !radio.isSelected()) {
                    radio.click();
                    log.info("✓ Radio fallback selected " + radio.getText());
                    return true;
                }
            }

        } catch (Exception e) {
        }

        return false;
    }

    // ================= CHECKBOX (PUNE PRIORITY) =================

    private boolean handleCheckbox(String question) {

        try {
            List<WebElement> labels = driver.findElements(checkboxLabels);
            String answer = TestData.getAnswerForQuestion(question).toLowerCase();

            // ✅ Match answer
            for (WebElement label : labels) {
                if (!label.isDisplayed()) continue;

                String text = label.getText().toLowerCase();
                if (!answer.isEmpty() && text.contains(answer)) {
                    label.click();
                    log.info("✓ Checkbox matched: " + text);
                    return true;
                }
            }

            // ⭐ FORCE PUNE (as per your requirement)
            for (WebElement label : labels) {
                String text = label.getText().toLowerCase();
                if (text.contains("pune")) {
                    label.click();
                    log.info("✓ Pune checkbox selected");
                    return true;
                }
            }

            // fallback first visible
            for (WebElement label : labels) {
                if (label.isDisplayed()) {
                    label.click();
                    log.info("✓ Checkbox fallback selected");
                    return true;
                }
            }

            // fallback input
            List<WebElement> inputs = driver.findElements(checkboxInputs);
            if (!inputs.isEmpty()) {
                WebElement first = inputs.get(0);
                if (first.isDisplayed() && !first.isSelected()) {
                    first.click();
                    log.info("✓ Checkbox input fallback");
                    return true;
                }
            }

        } catch (Exception e) {
            log.info("Checkbox error: " + e.getMessage());
        }

        return false;
    }

    // ================= CHIP ITEMS =================

    private boolean handleChipItems() {

        try {
            List<WebElement> chipItem = driver.findElements(chipItems);

             if (!chipItem.isEmpty() && chipItem.get(0).isDisplayed()) {
                chipItem.get(0).click();
                log.info("✓ Chip item selected");
                return true;
            }

        } catch (Exception ignored) {}

        return false;
    }

    // ================= SKIP =================

    private boolean handleSkip() {

        try {
            List<WebElement> skip = driver.findElements(skipBtn);

            if (!skip.isEmpty() && skip.get(0).isDisplayed()) {
                skip.get(0).click();
                log.info("✓ Skipped question");
                return true;
            }

        } catch (Exception ignored) {}

        return false;
    }

    // ================= SUBMIT =================

    private void handleSubmitAndWait() {

        try {
            List<WebElement> submit = driver.findElements(submitBtn);

            if (!submit.isEmpty()) {
                submit.get(0).click();

                // wait for either success OR next question
                WaitUtils.waitForEither(driver, botSuccess, questionLocator, 10);

                boolean success = WaitUtils.isElementPresent(driver, botSuccess);
                String nextQuestion = getQuestion();
                log.debug("Post-submit bot state -> success=" + success + ", nextQuestion='" + (nextQuestion == null ? "<none>" : nextQuestion) + "'");
            }
        } catch (Exception e) {
            log.info("Submit error: " + e.getMessage());
        }
    }

    public By getSuccessLocator() {
        return botSuccess;
    }

    public By getQuestionLocator() {
        return questionLocator;
    }
}