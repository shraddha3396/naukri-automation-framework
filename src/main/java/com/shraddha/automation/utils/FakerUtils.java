package com.shraddha.automation.utils;

import com.github.javafaker.Faker;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FakerUtils {

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    // ================= USER DATA =================

    public static Map<String, String> generateUserData() {
        Map<String, String> userData = new HashMap<>();
        userData.put("firstName", faker.name().firstName());
        userData.put("lastName", faker.name().lastName());
        userData.put("email", faker.internet().emailAddress());
        userData.put("phone", faker.phoneNumber().phoneNumber());
        userData.put("address", faker.address().fullAddress());
        userData.put("company", faker.company().name());
        userData.put("jobTitle", faker.job().title());
        return userData;
    }

    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    public static String generateName() {
        return faker.name().fullName();
    }

    public static String generatePhoneNumber() {
        return faker.phoneNumber().phoneNumber();
    }

    // ================= JOB SEARCH DATA =================

    public static Map<String, String> generateJobSearchData() {
        Map<String, String> jobData = new HashMap<>();
        String[] keywords = {"automation", "software testing", "qa engineer", "selenium", "java developer"};
        String[] locations = {"Pune", "Mumbai", "Bangalore", "Delhi", "Hyderabad"};

        jobData.put("keyword", keywords[random.nextInt(keywords.length)]);
        jobData.put("location", locations[random.nextInt(locations.length)]);
        jobData.put("experience", String.valueOf(random.nextInt(10) + 1));
        return jobData;
    }

    // ================= COMPANY DATA =================

    public static Map<String, String> generateCompanyData() {
        Map<String, String> companyData = new HashMap<>();
        companyData.put("name", faker.company().name());
        companyData.put("industry", faker.company().industry());
        companyData.put("website", faker.internet().url());
        companyData.put("size", String.valueOf((random.nextInt(10) + 1) * 100));
        return companyData;
    }

    // ================= TEST DATA GENERATION =================

    public static String generateRandomString(int length) {
        return faker.lorem().characters(length);
    }

    public static String generateRandomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static String generateRandomAlphabetic(int length) {
        return faker.lorem().characters(length, false, false);
    }

    // ================= CONDITIONAL DATA =================

    public static String generateValidEmail() {
        return faker.internet().emailAddress();
    }

    public static String generateInvalidEmail() {
        String[] invalidEmails = {
            "invalid-email",
            "@example.com",
            "user@",
            "user.example.com",
            "user@.com"
        };
        return invalidEmails[random.nextInt(invalidEmails.length)];
    }

    public static String generateValidPassword() {
        return faker.internet().password(8, 16, true, true, true);
    }

    public static String generateWeakPassword() {
        return faker.lorem().word(); // Too short
    }
}