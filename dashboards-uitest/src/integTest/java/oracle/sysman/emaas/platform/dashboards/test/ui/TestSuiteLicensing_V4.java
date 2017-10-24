/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author cawei
 */
public class TestSuiteLicensing_V4 extends LoginAndLogout
{
	public void initTest(String testName)
	{

		loginV4(this.getClass().getName() + "." + testName, "home");
		DashBoardUtils.loadWebDriver(webd);
	}

	@Test(alwaysRun = true)
	public void testExploreData_LALink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testExploreData_LALink");

		DashboardHomeUtil.gotoDataExplorer(webd, DashboardHomeUtil.EXPLOREDATA_MENU_LOG);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emlacore/html/log-analytics-search.html");
	}

	@Test(alwaysRun = true)
	public void testExploreData_SearchLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testExploreData_SearchLink");

		DashboardHomeUtil.gotoDataExplorer(webd, DashboardHomeUtil.EXPLOREDATA_MENU_SEARCH);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcta/ta/analytics.html");
	}

	/**
	 * @param string
	 */

	@Test(alwaysRun = true)
	public void testOpenAPMPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening APM in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitAPM(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "apmUi/index.html");

		webd.getLogger().info("Test open APM in welcome page finished!!!");
	}

	@Test(alwaysRun = true)
	public void testOpenCompliancePage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening Compliance in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitCompliance(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL_WithPara(webd, "complianceuiservice/index.html");

		webd.getLogger().info("Test open Compliance in welcome page finished!!!");
	}

	@Test(alwaysRun = true)
	public void testOpenDashboardPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening Dashboards in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitDashboards(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcpdfui/home.html");

		webd.getLogger().info("Test open dashboards in welcome page finished!!!");
	}

	@Test(alwaysRun = true)
	public void testOpenDE_LAPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening Data Explorers-Log in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.dataExplorers(webd, "log");

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emlacore/html/log-analytics-search.html");

		webd.getLogger().info("Test opening Data Explorers-Log in welcome page finished!!!");
	}

	@Test(alwaysRun = true)
	public void testOpenDE_SearchPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening Data Explorers-Search in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.dataExplorers(webd, "search");

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcta/ta/analytics.html");

		webd.getLogger().info("Test opening Data Explorers-Search in welcome page finished!!!");
	}

	@Test(alwaysRun = true)
	public void testOpenInfrastructureMonitoringPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening Infrastructure Monitoring in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitInfraMonitoring(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "monitoringservicesui/cms/index.html");

		webd.getLogger().info("Test opening Infrastructure Monitoring in welcome page finished!!!");
	}

	@Test(alwaysRun = true)
	public void testOpenITA_ApPrefPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening ITA: Application Performance Analytics in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "applicationPerformanceAnalytic");

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcitas/ap-analytics-war/html/ap-perf-analytics.html");

		webd.getLogger().info("Test opening ITA: Application Performance Analytics in welcome page finished!!!");
	}

	@Test(alwaysRun = true)
	public void testOpenITA_AvailPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening ITA: Availability Analytics in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "availabilityAnalytics");

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcitas/avail-analytics-war/html/avail-analytics-home.html");

		webd.getLogger().info("Test opening ITA: Availability Analytics in welcome page finished!!!");
	}

	//@Test(alwaysRun = true)
	public void testOpenITA_DEPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening ITA: Data Explorer in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "dataExplorer");

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcta/ta/analytics.html");

		webd.getLogger().info("Test opening ITA: Data Explorer in welcome page finished!!!");
	}

	@Test(alwaysRun = true)
	public void testOpenITA_PADatabasePage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening ITA: Performance Analytics-Database in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "performanceAnalyticsDatabase");

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcitas/db-performance-analytics/html/db-performance-analytics.html");

		webd.getLogger().info("Test opening ITA: Performance Analytics-Database in welcome page finished!!!");
	}

	//@Test(alwaysRun = true)
	public void testOpenITA_PAMiddlewarePage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening ITA: Performance Analytics-Middleware in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "performanceAnalyticsMiddleware");

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcitas/mw-analytics-war/html/mw-perf-analytics.html");

		webd.getLogger().info("Test opening ITA: Performance Analytics-Middleware in welcome page finished!!!");
	}

	@Test(alwaysRun = true)
	public void testOpenITA_RADatabasePage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening ITA: Resource Analytics-Database in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "resourceAnalyticsDatabase");

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcitas/db-analytics-war/html/db-analytics-resource-planner.html");

		webd.getLogger().info("Test opening ITA: Resource Analytics-Database in welcome page finished!!!");
	}

	@Test(alwaysRun = true)
	public void testOpenITA_RAHostPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening ITA: Resource Analytics-Host in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "resourceAnalyticsHost");

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcitas/resource-analytics/html/server-resource-analytics.html");

		webd.getLogger().info("Test opening ITA: Resource Analytics-Host in welcome page finished!!!");
	}

	@Test(alwaysRun = true)
	public void testOpenITA_RAMiddlewarePage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening ITA: Resource Analytics-Middleware in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "resourceAnalyticsMiddleware");

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcitas/mw-analytics-war/html/mw-analytics-resource-planner.html");

		webd.getLogger().info("Test opening ITA: Resource Analytics-Middleware in welcome page finished!!!");
	}

	@Test(alwaysRun = true)
	public void testOpenITA_ServerPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening ITA: Application Server Analytics in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "performanceAnalyticsApplicationServer");

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcitas/mw-analytics-war/html/mw-perf-dashboard.html");

		webd.getLogger().info("Test opening ITA: Application Server Analytics in welcome page finished!!!");
	}

	@Test(alwaysRun = true)
	public void testOpenITAPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening ITA in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "default");

		//verify the url of opened page
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/home.html?filter=ita");
				
		Assert.assertTrue(webd.isSelected("id=" + PageId.ITA_BOXID));
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.outDateOob(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.apmOobNotExist(webd);
		webd.getLogger().info("Test open ITA in welcome page finished!!!");
	}

	@Test(alwaysRun = true)
	public void testOpenLAPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening LA in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitLA(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emlacore/html/log-analytics-search.html");

		webd.getLogger().info("Test open LA in welcome page finished!!!");
	}

	@Test(alwaysRun = true)
	public void testOpenOrchestrationPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening Orchestration in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitOrchestration(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/home.html?filter=ocs");

		webd.getLogger().info("Test open Security Analytics in welcome page finished!!!");
	}

	@Test(alwaysRun = true)
	public void testOpenSecurityPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening Security Analytics in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitSecurity(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL_WithPara(webd, "saui/web/index.html");

		webd.getLogger().info("Test open Security Analytics in welcome page finished!!!");
	}

	@Test(alwaysRun = true)
	public void testUserMenu()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testUserMenu");

		BrandingBarUtil.userMenuOptions(webd, BrandingBarUtil.USERMENU_OPTION_ABOUT);

	}

	@Test(alwaysRun = true)
	public void testWelcomepage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		BrandingBarUtil.visitWelcome(webd);

		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "APM"));
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "LA"));
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "ITA"));
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "dashboards"));
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "dataExplorers"));
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "infraMonitoring"));
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "securityAnalytics"));
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "orchestration"));
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "compliance"));

		Assert.assertTrue(WelcomeUtil.isLearnMoreItemExisted(webd, "getStarted"));
		Assert.assertTrue(WelcomeUtil.isLearnMoreItemExisted(webd, "videos"));
		Assert.assertTrue(WelcomeUtil.isLearnMoreItemExisted(webd, "serviceOfferings"));
	}

	@Test(alwaysRun = true)
	public void verify_allOOB_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_allOOB_GridView");

		//switch to list view
		webd.getLogger().info("Switch to Grid View");
		DashboardHomeUtil.gridView(webd);

		//verify all the oob display
		DashBoardUtils.apmOobExist(webd);
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);
		DashBoardUtils.securityOobExist(webd);
		DashBoardUtils.outDateOob(webd);
	}

	@Test(alwaysRun = true)
	public void verify_allOOB_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_allOOB_ListView");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//verify all the oob display
		DashBoardUtils.apmOobExist(webd);
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);
		DashBoardUtils.securityOobExist(webd);
		DashBoardUtils.outDateOob(webd);
	}

	@Test(alwaysRun = true)
	public void verify_APMOOB_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_APMOOB_GridView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as APM");
		DashboardHomeUtil.filterOptions(webd, "apm");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//verify APM oob display
		DashBoardUtils.apmOobExist(webd);
		DashBoardUtils.itaOobNotExistV4(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.udeOobExistV4(webd);
		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_APMOOB_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_APMOOB_ListView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as APM");
		DashboardHomeUtil.filterOptions(webd, "apm");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		//verify APM oob display
		DashBoardUtils.apmOobExist(webd);
		DashBoardUtils.itaOobNotExistV4(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.udeOobExistV4(webd);
		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_CreatedBy_Me_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_CreatedBy_Me_GridView");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//click Created By Oracle checkbox
		webd.getLogger().info("select Created By as Me");
		DashboardHomeUtil.filterOptions(webd, "me");

		//verify all the oob not exsit
		DashBoardUtils.noOOBCheck(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_CreatedBy_Me_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_CreatedBy_Me_ListView");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		//click Created By Oracle checkbox
		webd.getLogger().info("select Created By as Me");
		DashboardHomeUtil.filterOptions(webd, "me");

		//verify all the oob not exsit
		DashBoardUtils.noOOBCheck(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_CreatedBy_Oracle_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_CreatedBy_Oracle_GridView");

		//switch to list view
		webd.getLogger().info("Switch to Grid View");
		DashboardHomeUtil.gridView(webd);

		//click Created By Oracle checkbox
		webd.getLogger().info("select Created By as Oracle");
		DashboardHomeUtil.filterOptions(webd, "oracle");

		//verify all the oob display
		DashBoardUtils.apmOobExist(webd);
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);
		DashBoardUtils.securityOobExist(webd);
		DashBoardUtils.outDateOob(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_CreatedBy_Oracle_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_CreatedBy_Oracle_ListView");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//click Created By Oracle checkbox
		webd.getLogger().info("select Created By as Oracle");
		DashboardHomeUtil.filterOptions(webd, "oracle");

		//verify all the oob display
		DashBoardUtils.apmOobExist(webd);
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);
		DashBoardUtils.securityOobExist(webd);
		DashBoardUtils.outDateOob(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_ITAOOB_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_ITAOOB_GridView");

		//select Cloud Services as IT Analytics
		webd.getLogger().info("select Cloud Services as IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//verify ITA oob display
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.outDateOob(webd);
		DashBoardUtils.udeOobExistV4(webd);
		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_ITAOOB_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_ITAOOB_GridView");

		//select Cloud Services as IT Analytics
		webd.getLogger().info("select Cloud Services as IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		//verify ITA oob display
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.outDateOob(webd);
		DashBoardUtils.udeOobExistV4(webd);
		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_LAOOB_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_LAOOB_GridView");

		//select Cloud Services as Log Analytics
		webd.getLogger().info("select Cloud Services as Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//verify LA oob display
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExistV4(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.outDateOob(webd);
		DashBoardUtils.udeOobExistV4(webd);
		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_LAOOB_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_LAOOB_GridView");

		//select Cloud Services as Log Analytics
		webd.getLogger().info("select Cloud Services as Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		//verify LA oob display
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExistV4(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.outDateOob(webd);
		DashBoardUtils.udeOobExistV4(webd);
		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_OrchestrationOOB_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_OrchestrationOOB_GridView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as Orchestration");
		DashboardHomeUtil.filterOptions(webd, "orchestration");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//verify Orchestration oob display
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExistV4(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, "Exadata Health"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, "Enterprise Health"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, "UI Gallery"));

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_OrchestrationOOB_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_OrchestrationOOB_ListView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as Orchestration");
		DashboardHomeUtil.filterOptions(webd, "orchestration");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		//verify Orchestration oob display
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExistV4(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, "Exadata Health"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, "Enterprise Health"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, "UI Gallery"));
		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_SecurityOOB_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_SecurityOOB_GridView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as Security");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//verify security oob display
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExistV4(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobExist(webd);
		DashBoardUtils.udeOobExistV4(webd);
		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);

	}

	@Test(alwaysRun = true)
	public void verify_SecurityOOB_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_SecurityOOB_ListView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as Security");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		//verify APM oob display
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExistV4(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobExist(webd);
		DashBoardUtils.udeOobExistV4(webd);
		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

}
