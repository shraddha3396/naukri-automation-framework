package com.shraddha.automation.tests;

import org.testng.annotations.Test;
import com.shraddha.automation.base.BaseTest;
import com.shraddha.automation.database.DatabaseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseTests extends BaseTest {

    private static final Logger log = LogManager.getLogger(DatabaseTests.class);

    @Test(groups = {"database"})
    public void h2DatabaseTest() {
        log.info("Running H2 database test");

        DatabaseUtils.connectH2("./target/testdb");

        // Create table
        DatabaseUtils.createTable(
            "CREATE TABLE test_users (" +
            "id INT PRIMARY KEY, " +
            "name VARCHAR(100), " +
            "email VARCHAR(100), " +
            "created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
        );

        // Insert test data
        DatabaseUtils.insertTestData(
            "INSERT INTO test_users (id, name, email) VALUES (?, ?, ?)",
            1, "John Doe", "john@example.com"
        );

        DatabaseUtils.insertTestData(
            "INSERT INTO test_users (id, name, email) VALUES (?, ?, ?)",
            2, "Jane Smith", "jane@example.com"
        );

        // Verify data
        boolean userExists = DatabaseUtils.verifyRecordExists("test_users", "name", "John Doe");
        assert userExists : "User should exist in database";

        int recordCount = DatabaseUtils.getRecordCount("test_users");
        assert recordCount == 2 : "Should have 2 records in table";

        // Query data
        java.util.List<java.util.Map<String, Object>> users = DatabaseUtils.getQueryResultsAsList(
            "SELECT * FROM test_users ORDER BY id"
        );

        assert users.size() == 2 : "Should retrieve 2 users";
        assert "John Doe".equals(users.get(0).get("NAME")) : "First user should be John Doe";

        DatabaseUtils.disconnect();
    }

    @Test(groups = {"database"})
    public void dataValidationTest() {
        log.info("Running data validation test");

        DatabaseUtils.connectH2("./target/testdb");

        // Insert test data
        DatabaseUtils.insertTestData(
            "INSERT INTO test_users (id, name, email) VALUES (?, ?, ?)",
            3, "Bob Wilson", "bob@example.com"
        );

        // Validate single value
        Object email = DatabaseUtils.getSingleValue("SELECT email FROM test_users WHERE name = 'Bob Wilson'");
        assert "bob@example.com".equals(email) : "Email should match";

        // Validate record count
        int count = DatabaseUtils.getRecordCount("test_users");
        assert count >= 3 : "Should have at least 3 records";

        DatabaseUtils.disconnect();
    }
}