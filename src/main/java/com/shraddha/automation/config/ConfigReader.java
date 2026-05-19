package com.shraddha.automation.config;

import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static final Properties prop = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {

        String env = System.getProperty("env", "qa");
        String fileName = "config/config-" + env + ".properties";

        try (InputStream fis =
                     ConfigReader.class.getClassLoader()
                             .getResourceAsStream(fileName)) {

            if (fis == null) {
                throw new RuntimeException(
                        "Config file not found: " + fileName);
            }

            prop.load(fis);

            log.info("Loaded config file: {}", fileName);

        } catch (Exception e) {

            log.error("Failed to load config file", e);

            throw new RuntimeException(
                    "Failed to load config file", e);
        }
    }

    public static String get(String key) {

        String systemValue = System.getProperty(key);

        if (systemValue != null && !systemValue.isBlank()) {
            log.info("Using system property for {} : {}", key, systemValue);
            return systemValue.trim();
        }

        String value = prop.getProperty(key);

        if (value == null) {
            throw new RuntimeException(
                    "Property not found: " + key);
        }

        return value.trim();
    }

    public static String get(String key, String defaultValue) {

        String systemValue = System.getProperty(key);

        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }

        return prop.getProperty(key, defaultValue).trim();
    }
}