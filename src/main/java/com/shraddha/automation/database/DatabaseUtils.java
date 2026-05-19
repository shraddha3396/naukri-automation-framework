package com.shraddha.automation.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseUtils {

    private static final Logger log = LogManager.getLogger(DatabaseUtils.class);
    private static Connection connection;

    // ================= CONNECTION MANAGEMENT =================

    public static void connect(String url, String username, String password) {
        try {
            connection = DriverManager.getConnection(url, username, password);
            log.info("Database connection established successfully");
        } catch (SQLException e) {
            log.error("Failed to connect to database: {}", e.getMessage());
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public static void connectH2(String dbPath) {
        String url = "jdbc:h2:" + dbPath;
        try {
            connection = DriverManager.getConnection(url);
            log.info("H2 Database connection established: {}", dbPath);
        } catch (SQLException e) {
            log.error("Failed to connect to H2 database: {}", e.getMessage());
            throw new RuntimeException("H2 Database connection failed", e);
        }
    }

    public static void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                log.info("Database connection closed");
            } catch (SQLException e) {
                log.error("Error closing database connection: {}", e.getMessage());
            }
        }
    }

    // ================= QUERY EXECUTION =================

    public static ResultSet executeQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            log.info("Executing query: {}", query);
            return statement.executeQuery(query);
        } catch (SQLException e) {
            log.error("Query execution failed: {}", e.getMessage());
            throw new RuntimeException("Query execution failed", e);
        }
    }

    public static int executeUpdate(String query) {
        try {
            Statement statement = connection.createStatement();
            log.info("Executing update: {}", query);
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            log.error("Update execution failed: {}", e.getMessage());
            throw new RuntimeException("Update execution failed", e);
        }
    }

    public static ResultSet executePreparedQuery(String query, Object... params) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            log.info("Executing prepared query: {}", query);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            log.error("Prepared query execution failed: {}", e.getMessage());
            throw new RuntimeException("Prepared query execution failed", e);
        }
    }

    // ================= DATA RETRIEVAL =================

    public static List<Map<String, Object>> getQueryResultsAsList(String query) {
        List<Map<String, Object>> results = new ArrayList<>();
        try (ResultSet resultSet = executeQuery(query)) {
            java.sql.ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }
        } catch (SQLException e) {
            log.error("Error retrieving query results: {}", e.getMessage());
        }
        return results;
    }

    public static Map<String, Object> getSingleRow(String query) {
        List<Map<String, Object>> results = getQueryResultsAsList(query);
        return results.isEmpty() ? null : results.get(0);
    }

    public static Object getSingleValue(String query) {
        try (ResultSet resultSet = executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getObject(1);
            }
        } catch (SQLException e) {
            log.error("Error retrieving single value: {}", e.getMessage());
        }
        return null;
    }

    // ================= VALIDATION METHODS =================

    public static boolean verifyRecordExists(String tableName, String columnName, Object value) {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
        try (ResultSet resultSet = executePreparedQuery(query, value)) {
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                log.info("Record existence check: {} records found for {} = {}", count, columnName, value);
                return count > 0;
            }
        } catch (SQLException e) {
            log.error("Error verifying record existence: {}", e.getMessage());
        }
        return false;
    }

    public static int getRecordCount(String tableName) {
        String query = "SELECT COUNT(*) FROM " + tableName;
        Object result = getSingleValue(query);
        return result != null ? ((Number) result).intValue() : 0;
    }

    // ================= UTILITY METHODS =================

    public static void createTable(String createTableSQL) {
        try {
            executeUpdate(createTableSQL);
            log.info("Table created successfully");
        } catch (RuntimeException e) {
            log.error("Table creation failed: {}", e.getMessage());
            throw new RuntimeException("Table creation failed", e);
        }
    }

    public static void insertTestData(String insertSQL, Object... params) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            preparedStatement.executeUpdate();
            log.info("Test data inserted successfully");
        } catch (SQLException e) {
            log.error("Test data insertion failed: {}", e.getMessage());
            throw new RuntimeException("Test data insertion failed", e);
        }
    }
}