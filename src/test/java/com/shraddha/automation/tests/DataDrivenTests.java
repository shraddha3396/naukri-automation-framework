package com.shraddha.automation.tests;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import com.shraddha.automation.base.BaseTest;
import com.shraddha.automation.pages.NaukriPage;
import com.shraddha.automation.utils.AssertUtil;
import com.shraddha.automation.utils.DataProviderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;

public class DataDrivenTests extends BaseTest {

    private static final Logger log = LogManager.getLogger(DataDrivenTests.class);
    private NaukriPage naukriPage;

    @Override
    protected void onSetupComplete() {
        naukriPage = new NaukriPage(driver);
    }

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return DataProviderUtil.getExcelData("src/test/resources/testdata/LoginTestData.xlsx", "LoginData");
    }

    @DataProvider(name = "jobSearchData")
    public Object[][] getJobSearchData() {
        return DataProviderUtil.getJsonData("src/test/resources/testdata/JobSearchData.json", "jobSearches");
    }

    @Test(dataProvider = "loginData", groups = {"regression"})
    public void loginTest(Map<String, String> testData) {
        log.info("Running login test with data: {}", testData);

        naukriPage.login(testData.get("username"), testData.get("password"));

        AssertUtil.assertElementNotPresent(driver, naukriPage.getLoginLayer(),
            "Login should be successful for user: " + testData.get("username"));
    }

    @Test(dataProvider = "jobSearchData", groups = {"regression"})
    public void jobSearchTest(Map<String, Object> testData) {
        log.info("Running job search test with data: {}", testData);

        // Login first
        naukriPage.login("testuser@example.com", "password123");

        // Perform search
        naukriPage.searchJobs(
            (String) testData.get("keyword"),
            (String) testData.get("location")
        );

        // Verify results
        AssertUtil.assertElementPresent(driver, naukriPage.getSearchBar(),
            "Job search should show results for: " + testData.get("keyword"));
    }
}