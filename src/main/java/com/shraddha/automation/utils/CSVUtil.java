package com.shraddha.automation.utils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CSVUtil {

    private static final String runId = java.time.LocalDateTime.now().toString().replace(":", "-");

    public static void writeToCSV(String jobTitle, String company, String url, String status) {
        try {
            String csvFilename = "SkippedJobs/SkippedJobs_" + runId + ".csv";
            File file = new File(csvFilename);
            file.getParentFile().mkdirs();
            boolean fileExists = file.exists();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilename, true))) {
                if (!fileExists) {
                    writer.write("Job Title,Company,Apply URL,Status,Date Added\n");
                }
                writer.write(escapeCSV(jobTitle) + "," + escapeCSV(company) + "," + escapeCSV(url) + "," + escapeCSV(status) + "," + java.time.LocalDateTime.now() + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }  
    
    static String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) 
            return "\"" + value.replace("\"", "\"\"") + "\"";
        return value;
    }

    public static String generateSheetName() {
        // Generate sheet name with current date and time: "2024-04-08_14-30-45"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return LocalDateTime.now().format(formatter);
    }
}