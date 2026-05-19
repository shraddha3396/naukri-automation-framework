package com.shraddha.automation.utils;

import org.openqa.selenium.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.io.FileUtils;

public class ScreenshotUtil {

    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    public static String capture(WebDriver driver, String name) {

        try {
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            String safeName = name.replaceAll("[^a-zA-Z0-9_\\-]", "_");
            String path = "screenshots/" + safeName + "_" + timestamp + ".png";

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File(path);

            dest.getParentFile().mkdirs();
            FileUtils.copyFile(src, dest);

            return path;

        } catch (Exception e) {
            throw new RuntimeException("Screenshot failed: " + e.getMessage(), e);
        }
    }
}