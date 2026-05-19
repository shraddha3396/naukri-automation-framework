package com.shraddha.automation.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataProviderUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // ================= EXCEL DATA PROVIDER =================

    public static Object[][] getExcelData(String filePath, String sheetName) {
        List<Map<String, String>> testData = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in " + filePath);
            }

            // Get header row
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Header row not found in sheet");
            }

            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue());
            }

            // Get data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> dataMap = new HashMap<>();
                for (int j = 0; j < headers.size() && j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    String value = getCellValueAsString(cell);
                    dataMap.put(headers.get(j), value);
                }
                testData.add(dataMap);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file: " + e.getMessage());
        }

        // Convert to Object[][]
        Object[][] data = new Object[testData.size()][1];
        for (int i = 0; i < testData.size(); i++) {
            data[i][0] = testData.get(i);
        }

        return data;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue().toString();
                } else {
                    yield String.valueOf((int) cell.getNumericCellValue());
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

    // ================= JSON DATA PROVIDER =================

    public static Object[][] getJsonData(String filePath, String arrayKey) {
        List<Map<String, Object>> testData = new ArrayList<>();

        try {
            JsonNode rootNode = objectMapper.readTree(new java.io.File(filePath));
            JsonNode dataArray = rootNode.get(arrayKey);

            if (dataArray != null && dataArray.isArray()) {
                for (JsonNode node : dataArray) {
                    Map<String, Object> dataMap = objectMapper.convertValue(node, Map.class);
                    testData.add(dataMap);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file: " + e.getMessage());
        }

        Object[][] data = new Object[testData.size()][1];
        for (int i = 0; i < testData.size(); i++) {
            data[i][0] = testData.get(i);
        }

        return data;
    }

    // ================= CSV DATA PROVIDER =================

    public static Object[][] getCsvData(String filePath) {
        List<Map<String, String>> testData = new ArrayList<>();

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(filePath))) {
            String line = reader.readLine();
            if (line == null) return new Object[0][0];

            String[] headers = line.split(",");

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> dataMap = new HashMap<>();

                for (int i = 0; i < headers.length && i < values.length; i++) {
                    dataMap.put(headers[i].trim(), values[i].trim());
                }

                testData.add(dataMap);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file: " + e.getMessage());
        }

        Object[][] data = new Object[testData.size()][1];
        for (int i = 0; i < testData.size(); i++) {
            data[i][0] = testData.get(i);
        }

        return data;
    }
}