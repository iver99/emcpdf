/*
 * Copyright (C) 2016 Oracle
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
import oracle.sysman.emaas.platform.dashboards.tests.ui.*;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author cawei
 */
public class TestGlobalContext_TimeRange extends LoginAndLogout
{
	public static String DSBNAME = "";
	public static String StartTimeStamp = "";
	public static String EndTimeStamp = "";
	public static String dsb_4757 = "";
	public static String dsb_4757_withWidget = "";

	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);

		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Reset all filter options");
		DashboardHomeUtil.resetFilterOptions(webd);

	}

	@BeforeClass
	public void createTestData()
	{
		DSBNAME = "DASHBOARD GLOBALTESTING TimeRange - " + DashBoardUtils.generateTimeStamp();
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());

		//create test data
		webd.getLogger().info("Start to create test data");
		DashboardHomeUtil.createDashboard(webd, DSBNAME, null, DashboardHomeUtil.DASHBOARD);

		webd.getLogger().info("Respect GC Time Range");
		DashboardBuilderUtil.respectGCForTimeRange(webd);

		webd.getLogger().info("Set custom time range");
		String returnTimeRange = GlobalContextUtil.setCustomTime(webd, "07/06/2016 07:22 PM", "05/07/2017 09:22 PM");

		//convert date to timestamp
		SimpleDateFormat fmt = new SimpleDateFormat("MMM d, yyyy h:mm a");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date dTmpStart = new Date();
		Date dTmpEnd = new Date();

		String tmpReturnDate = returnTimeRange.substring(7);

		String[] tmpDate = tmpReturnDate.split("-");

		String tmpStartDate = tmpDate[0].trim();
		String tmpEndDate = tmpDate[1].trim();

		try {
			dTmpStart = fmt.parse(tmpStartDate);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			dTmpEnd = fmt.parse(tmpEndDate);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}

		String tmpStartDateNew = df.format(dTmpStart);
		String tmpEndDateNew = df.format(dTmpEnd);
		webd.getLogger().info(tmpStartDateNew);
		webd.getLogger().info(tmpEndDateNew);

		StartTimeStamp = String.valueOf(Timestamp.valueOf(tmpStartDateNew).getTime());
		EndTimeStamp = String.valueOf(Timestamp.valueOf(tmpEndDateNew).getTime());

		webd.getLogger().info(String.valueOf(StartTimeStamp));
		webd.getLogger().info(String.valueOf(EndTimeStamp));
	}

	@AfterClass
	public void RemoveDashboard()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to remove test data");

		//delete dashboard
		webd.getLogger().info("Start to remove the test data...");

		DashBoardUtils.deleteDashboard(webd, DSBNAME);
		DashBoardUtils.deleteDashboard(webd, dsb_4757);
		DashBoardUtils.deleteDashboard(webd, dsb_4757_withWidget);

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	@Test(alwaysRun = true)
	public void testGlocalContext_TimeRange_APM()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testGlocalContext_TimeRange_APM");

		//open the dashboard
		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd,DSBNAME);

		//open the APM service
		webd.getLogger().info("Open APM service page");
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_APM);

		webd.getLogger().info("Verify the URL");
		urlVerification("apmUi/index.html", StartTimeStamp,EndTimeStamp);
	}

	@Test(alwaysRun = true)
	public void testGlocalContext_TimeRange_LA()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testGlocalContext_TimeRange_LA");

		//open the dashboard
		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd,DSBNAME);

		//open the LA service
		webd.getLogger().info("Open LA service page");
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_LA);

		webd.getLogger().info("Verify the URL");
		urlVerification("emlacore/html/log-analytics-search.html", StartTimeStamp,EndTimeStamp);
	}

	@Test(alwaysRun = true)
	public void testGlocalContext_TimeRange_ITA()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testGlocalContext_TimeRange_ITA");

		//open the dashboard
		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd,DSBNAME);

		//open the ITA service
		webd.getLogger().info("Open ITA service page");
		BrandingBarUtil.visitApplicationVisualAnalyzer(webd,BrandingBarUtil.NAV_LINK_TEXT_VA_TA);

		webd.getLogger().info("Verify the URL");
		urlVerification("emcta/ta/analytics.html", StartTimeStamp,EndTimeStamp);
	}

	@Test(alwaysRun = true)
	public void testGlocalContext_TimeRange_Monitoring()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testGlocalContext_TimeRange_Monitoring");

		//open the dashboard
		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd,DSBNAME);

		//open the Monitoring service
		webd.getLogger().info("Open Monitoring service page");
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_IM);

		webd.getLogger().info("Verify the URL");
		urlVerification("monitoringservicesui/cms/index.html", StartTimeStamp,EndTimeStamp);
	}

	@Test(alwaysRun = true)
	public void testGlocalContext_TimeRange_Security()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testGlocalContext_TimeRange_Security");

		//open the dashboard
		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd,DSBNAME);

		//open the Security service
		webd.getLogger().info("Open Security service page");
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_SECU);

		webd.getLogger().info("Verify the URL");
		urlVerification("saui/web/index.html", StartTimeStamp,EndTimeStamp);
	}

	@Test(alwaysRun = true)
	public void testGlocalContext_TimeRange_Compliance()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testGlocalContext_TimeRange_Compliance");

		//open the dashboard
		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd,DSBNAME);

		//open the Compliance service
		webd.getLogger().info("Open Compliance service page");
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_COMP);

		webd.getLogger().info("Verify the URL");
		urlVerification("complianceuiservice/index.html", StartTimeStamp,EndTimeStamp);
	}

	@Test(alwaysRun = true)
	public void testEMCPDF_4757_withoutWidget()
	{
		dsb_4757 = "Test EMCPDF-4757 - " + DashBoardUtils.generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testEMCPDF_4757_withoutWidget");

		//switch to grid view
		webd.getLogger().info("In Grid View");
		DashboardHomeUtil.gridView(webd);

		//create a dashboard
		webd.getLogger().info("Create a dashboard");
		DashboardHomeUtil.createDashboard(webd, dsb_4757,"");

		//configure glocal context
		webd.getLogger().info("Respect GC of Entities");
		DashboardBuilderUtil.respectGCForEntity(webd);

		webd.getLogger().info("Set Time Selector as : Use time range on dashboard page");
		DashboardBuilderUtil.showTimeRangeFilter(webd, true);

		//get the time range of the time selector
		webd.getLogger().info("Get the time range set in the time selector");
		String timeRange_in_df = TimeSelectorUtil.getTimeRangeLabel(webd).trim().trim().split(":")[0];
		webd.getLogger().info("Time Range in DF: "+ timeRange_in_df);

		//navigate to UDE page
		webd.getLogger().info("Navigate to UDE page");
		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.ROOT_MENU_DATAEXPLORER);

		//change the time range in UDE
		webd.getLogger().info("Change the time range in UDE page");
		TimeSelectorUtil.setTimeRange(webd, ITimeSelectorUtil.TimeRange.Last90Days);

		//back to Dahboard home page
		webd.getLogger().info("Back to dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//open the dashboard
		webd.getLogger().info("Open the dashboard created");
		DashboardHomeUtil.selectDashboard(webd, dsb_4757);

		//verify the time range not changed
		webd.getLogger().info("Verify the time range not changed");
		String currentTimeRange = TimeSelectorUtil.getTimeRangeLabel(webd).trim().trim().split(":")[0];
		Assert.assertEquals(timeRange_in_df, currentTimeRange);

		//verify the URL in builder page
		webd.getLogger().info("Verify the URL");
		String currentUrl = webd.getCurrentUrl();
		if (!currentUrl.substring(currentUrl.indexOf("emsaasui") + 9).contains("timePeriod%3DLAST_90_DAY%26")) {
			Assert.fail("Wrong time period");
		}

		//change the time range in dashboard
		webd.getLogger().info("Change the time range");
		TimeSelectorUtil.setTimeRange(webd, ITimeSelectorUtil.TimeRange.NewLast7Days);

		currentUrl = webd.getCurrentUrl();
		if (!currentUrl.substring(currentUrl.indexOf("emsaasui") + 9).contains("timePeriod%3DLAST_90_DAY%26")) {
			Assert.fail("Wrong time period");
		}
	}

	@Test(alwaysRun = true)
	public void testEMCPDF_4757_withWidget()
	{
		dsb_4757_withWidget = "Test EMCPDF-4757 With Widget - " + DashBoardUtils.generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testEMCPDF_4757_withWidget");

		//switch to grid view
		webd.getLogger().info("In Grid View");
		DashboardHomeUtil.gridView(webd);

		//create a dashboard
		webd.getLogger().info("Create a dashboard");
		DashboardHomeUtil.createDashboard(webd, dsb_4757_withWidget,"");

		webd.getLogger().info("Add widget to the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, "Treemap");
		DashboardBuilderUtil.saveDashboard(webd);

		//configure glocal context
		webd.getLogger().info("Respect GC of Entities");
		DashboardBuilderUtil.respectGCForEntity(webd);

		webd.getLogger().info("Set Time Selector as : Use time range on dashboard page");
		DashboardBuilderUtil.showTimeRangeFilter(webd, true);

		//get the time range of the time selector
		webd.getLogger().info("Get the time range set in the time selector");
		String timeRange_in_df = TimeSelectorUtil.getTimeRangeLabel(webd).trim().split(":")[0];
		webd.getLogger().info("Time Range in DF: "+ timeRange_in_df);

		//navigate to UDE page
		webd.getLogger().info("Navigate to UDE page");
		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.ROOT_MENU_DATAEXPLORER);

		//change the time range in UDE
		webd.getLogger().info("Change the time range in UDE page");
		TimeSelectorUtil.setTimeRange(webd, ITimeSelectorUtil.TimeRange.Last90Days);

		//back to Dahboard home page
		webd.getLogger().info("Back to dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//open the dashboard
		webd.getLogger().info("Open the dashboard created");
		DashboardHomeUtil.selectDashboard(webd, dsb_4757_withWidget);

		//verify the time range not changed
		webd.getLogger().info("Verify the time range not changed");
		String currentTimeRange = TimeSelectorUtil.getTimeRangeLabel(webd).trim().split(":")[0];
		Assert.assertEquals(timeRange_in_df, currentTimeRange);

		//verify the time in widget
		webd.getLogger().info("Verify the time in the widget");
		String timerange = webd.evalJavascript("return window._contextPassedToWidgetsAtPageLoad && window._contextPassedToWidgetsAtPageLoad.timeSelector && window._contextPassedToWidgetsAtPageLoad.timeSelector.viewTimePeriod()");
		webd.getLogger().info(timerange);
		Assert.assertEquals(timerange, "LAST_14_DAY");

		//verify the URL in builder page
		webd.getLogger().info("Verify the URL");
		String currentUrl = webd.getCurrentUrl();
		if (!currentUrl.substring(currentUrl.indexOf("emsaasui") + 9).contains("timePeriod%3DLAST_90_DAY%26")) {
			Assert.fail("Wrong time period");
		}

		//change the time range in dashboard
		webd.getLogger().info("Change the time range");
		TimeSelectorUtil.setTimeRange(webd, ITimeSelectorUtil.TimeRange.NewLast7Days);
		webd.waitForServer();

		//timerange = webd.evalJavascript("return window._contextPassedToWidgetsAtPageLoad && window._contextPassedToWidgetsAtPageLoad.timeSelector && window._contextPassedToWidgetsAtPageLoad.timeSelector.viewTimePeriod()");
		//webd.getLogger().info(timerange);
		//Assert.assertEquals(timerange, "LAST_7_DAY");

		currentUrl = webd.getCurrentUrl();
		if (!currentUrl.substring(currentUrl.indexOf("emsaasui") + 9).contains("timePeriod%3DLAST_90_DAY%26")) {
			Assert.fail("Wrong time period");
		}
	}
	
	private void urlVerification(String serviceURL, String StartTimeStamp, String EndTimeStamp)
	{
		String url = webd.getCurrentUrl();
		webd.getLogger().info("url = " + url);
		if (!url.substring(url.indexOf("emsaasui") + 9).contains(serviceURL)) {
			Assert.fail("Wrong Service URL");
		}
		if (!url.substring(url.indexOf("emsaasui") + 9).contains(
				"startTime%3D" + String.valueOf(StartTimeStamp) + "%26endTime%3D" + String.valueOf(EndTimeStamp))) {
			Assert.fail("Wrong Time Range");
		}
	}
}
