package com.shraddha.automation.tests;

import org.testng.annotations.Test;
import com.shraddha.automation.base.BaseTest;
import com.shraddha.automation.pages.NaukriPage;
import com.shraddha.automation.utils.AssertUtil;
import com.shraddha.automation.performance.PerformanceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PerformanceTests extends BaseTest {

    private static final Logger log = LogManager.getLogger(PerformanceTests.class);
    private NaukriPage naukriPage;

    @Override
    protected void onSetupComplete() {
        naukriPage = new NaukriPage(driver);
    }

    @Test(groups = {"performance"})
    public void pageLoadPerformanceTest() {
        log.info("Running page load performance test");

        long startTime = System.currentTimeMillis();

        // Navigate and measure
        driver.get("https://naukri.com");
        PerformanceUtils.monitorPageLoadTime(driver, "Naukri Homepage");

        long endTime = System.currentTimeMillis();
        long loadTime = endTime - startTime;

        log.info("Total page load time: {} ms", loadTime);
        assert loadTime < 10000 : "Page should load within 10 seconds";
    }

    @Test(groups = {"performance"})
    public void memoryUsageTest() {
        log.info("Running memory usage test");

        // Perform some actions
        driver.get("https://naukri.com");
        naukriPage.login("test@example.com", "password");

        // Check memory usage
        PerformanceUtils.checkMemoryUsage(driver);
    }

    @Test(groups = {"performance"})
    public void searchPerformanceTest() {
        log.info("Running search performance test");

        driver.get("https://naukri.com");

        long startTime = System.currentTimeMillis();
        naukriPage.searchJobs("automation", "Pune");
        long endTime = System.currentTimeMillis();

        long searchTime = endTime - startTime;
        log.info("Search operation took: {} ms", searchTime);

        AssertUtil.assertElementPresent(driver, naukriPage.getSearchBar(),
            "Search should complete and show results");
    }
}