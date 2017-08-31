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
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.*;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
	
	private void urlVerification(String serviceURL, String StartTimeStamp, String EndTimeStamp)
	{
		String url = webd.getWebDriver().getCurrentUrl();
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
