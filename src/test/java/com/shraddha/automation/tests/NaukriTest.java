package com.shraddha.automation.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.shraddha.automation.base.BaseTest;
import com.shraddha.automation.listeners.TestListener;
import com.shraddha.automation.pages.NaukriPage;
import com.shraddha.automation.pages.SearchedJobsApplyFlow;
import com.shraddha.automation.pages.RecommendedJobsApplyFlow;

public class NaukriTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(NaukriTest.class);

    NaukriPage page;
    SearchedJobsApplyFlow searchedJobs;
    RecommendedJobsApplyFlow recJobs;

    @BeforeMethod(alwaysRun = true)
    public void initPageObjects() {
        log.debug("Initializing Page Objects");

        page = new NaukriPage(driver);
        searchedJobs = new SearchedJobsApplyFlow(driver);
        recJobs = new RecommendedJobsApplyFlow(driver);

        log.debug("All Page Objects initialized successfully");
    }

    @Test(groups = {"regression"})
    public void applyJobsTest() {

        TestListener.logStep("Executing full Naukri flow");

        // ================= FULL FLOW =================

        TestListener.logStep("Login + Profile update + Job search started");
        page.executeFullFlow();

       TestListener.logStep("Applying jobs from searched jobs section");
        searchedJobs.applyJobs();

        TestListener.logStep("Applying jobs from recommended jobs section");
        recJobs.executeFullFlow();

    }
}