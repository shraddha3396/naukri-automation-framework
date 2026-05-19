package com.shraddha.automation.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.shraddha.automation.TestData;

public class SummaryUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static long startTime;

    public static void startTimer(){
        startTime = System.currentTimeMillis();
    }
    public static void Summary() {

    long endTime = System.currentTimeMillis();
    long durationMs = endTime - startTime;

    long minutes = durationMs / 60000;
    long seconds = (durationMs % 60000) / 1000;
    long milliseconds = durationMs % 1000;

    int applied = TestData.getApplied();
    int skipped = TestData.getSkipped();
    int total = applied + skipped;

    System.out.println("\n============================================================");
    System.out.println("JOB APPLICATION SUMMARY");
    System.out.println("Completed at : " + LocalDateTime.now().format(FORMATTER));
    System.out.println("Total Jobs   : " + total);
    System.out.println("Applied      : " + applied);
    System.out.println("Skipped      : " + skipped);
    System.out.println("Duration     : " + minutes + "m " + seconds + "s " + milliseconds + "ms");
    System.out.println("============================================================\n");
}
}