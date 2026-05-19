package com.shraddha.automation.utils;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.shraddha.automation.config.ConfigReader;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driver =
            new ThreadLocal<>();

    public static WebDriver initDriver() {

        String browser = ConfigReader.get("browser");
        String headless = ConfigReader.get("headless");

        System.out.println("Browser : " + browser);
        System.out.println("Headless : " + headless);

        if (browser.equalsIgnoreCase("chrome")) {

            Map<String, Object> prefs = new HashMap<>();
            prefs.put("profile.managed_default_content_settings.images", 2);
            prefs.put("profile.default_content_setting_values.geolocation",2);
            prefs.put("profile.default_content_setting_values.notifications",2);

            ChromeOptions options = new ChromeOptions();

            options.setExperimentalOption("prefs", prefs);

            if (headless.equalsIgnoreCase("true")) {

                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
            }

            options.addArguments("--incognito");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-geolocation");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-popup-blocking");
            options.addArguments("--disable-background-networking");
            options.addArguments("--disable-background-timer-throttling");
            options.addArguments("--disable-renderer-backgrounding");
            options.addArguments("--disable-features=TranslateUI");
            options.addArguments("--blink-settings=imagesEnabled=false");

            driver.set(new ChromeDriver(options));
        }

        else if (browser.equalsIgnoreCase("firefox")) {

            FirefoxOptions options = new FirefoxOptions();

            if (headless.equalsIgnoreCase("true")) {

                options.addArguments("-headless");
                options.addArguments("--width=1920");
                options.addArguments("--height=1080");
            }

            options.addArguments("-private");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--disable-gpu");
            options.addArguments("--start-maximized");
            options.addPreference("dom.webnotifications.enabled",false);
            options.addPreference("media.volume_scale","0.0");
            options.addPreference("permissions.default.geo",2);
            options.addPreference("permissions.default.desktop-notification",2);
            options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/147.0.0.0 Safari/537.36");
            driver.set(new FirefoxDriver(options));
        }


        else {

            throw new RuntimeException(
                    "Unsupported browser: " + browser);
        }

        if (!ConfigReader.get("headless").equalsIgnoreCase("true")) {
            getDriver().manage().window().maximize();
        }

        return getDriver();
    }

    public static WebDriver getDriver() {

        return driver.get();
    }

    public static void quitDriver() {

        if (driver.get() != null) {

            getDriver().quit();
            driver.remove();
        }
    }
}