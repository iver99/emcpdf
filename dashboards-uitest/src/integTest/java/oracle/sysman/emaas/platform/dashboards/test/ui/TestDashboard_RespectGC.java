package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.GlobalContextUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeUnit;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class TestDashboard_RespectGC extends LoginAndLogout
{
	private String dbRespectGCTimeRangeName = "";
	private String dbRespectGCTimeRangeName_URLCheck = "";
	private final String dbRespectGCEntityName = "";

	private final String widgetName_LA = "Database Errors Trend";
	private final String widgetName_UDE = "Area Chart";

	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@AfterClass
	public void RemoveDashboard()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to remove test data");

		//delete dashboard
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Start to remove the test data...");
		DashBoardUtils.deleteDashboard(webd, dbRespectGCTimeRangeName);
		DashBoardUtils.deleteDashboard(webd, dbRespectGCTimeRangeName_URLCheck);

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	@Test(alwaysRun = true)
	public void testRespectGCTimeRange() throws InterruptedException
	{
		dbRespectGCTimeRangeName = "respectGC TimeRange-" + DashBoardUtils.generateTimeStamp();

		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger()
				.info("start to test testRespectGCTimeRange");

		//Create dashboard
		DashboardHomeUtil.createDashboard(webd, dbRespectGCTimeRangeName, null, DashboardHomeUtil.DASHBOARD);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbRespectGCTimeRangeName, null, true),
				"Create dashboard failed!");

		//add widget to the dashboard
		webd.getLogger().info("Add widget to the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, widgetName_UDE);

		//respect GC for TimeRange
		webd.getLogger().info("Respect GC for Time Range");
		DashboardBuilderUtil.respectGCForTimeRange(webd);

		//save the dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		//change the time range, verify the url will be changed
		webd.getLogger().info("Change the time range, verify the url will be changed");
		GlobalContextUtil.setTimeRange(webd, TimeRange.NewLast7Days);

		String currentURL = webd.getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_7_DAY"), true);

		//open the widget
		webd.getLogger().info("Open the widget");
		DashboardBuilderUtil.openWidget(webd, widgetName_UDE);

		WaitUtil.waitForPageFullyLoaded(webd);

		Assert.assertEquals(GlobalContextUtil.getTimeRangeLabel_V2(webd).contains("Last 7 days"), true);
	}
	
	@Test(alwaysRun = true)
	public void testRespectGCTimeRange_URLCheck()
	{
		dbRespectGCTimeRangeName_URLCheck = "respectGC TimeRange URL-" + DashBoardUtils.generateTimeStamp();

		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger()
				.info("start to test testRespectGCTimeRange_URLCheck");

		//Create dashboard
		webd.getLogger().info("Create dashboard");
		DashboardHomeUtil.createDashboard(webd, dbRespectGCTimeRangeName_URLCheck, null, DashboardHomeUtil.DASHBOARD);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbRespectGCTimeRangeName_URLCheck, null, true),
				"Create dashboard failed!");
		
		webd.getLogger().info("Respect GC");
		Assert.assertTrue(DashboardBuilderUtil.respectGCForTimeRange(webd), "Fail to configure respect GC");
		
		webd.getLogger().info("Start to set time range then check the URL change as well");
		
		webd.getLogger().info("Set Last 15 mins");
		TimeSelectorUtil.setTimeRange(webd, TimeRange.Last15Mins);
		String currentURL = webd.getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_15_MINUTE"), true);

		webd.getLogger().info("Set Last 30 mins");
		TimeSelectorUtil.setTimeRange(webd, TimeRange.Last30Mins);
		currentURL = webd.getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_30_MINUTE"), true);
		
		webd.getLogger().info("Set Last 60 mins");
		TimeSelectorUtil.setTimeRange(webd, TimeRange.NewLast60Mins);
		currentURL = webd.getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_60_MINUTE"), true);
		
		webd.getLogger().info("Set Last 8 hours");
		TimeSelectorUtil.setTimeRange(webd, TimeRange.Last8Hours);
		currentURL = webd.getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_8_HOUR"), true);
		
		webd.getLogger().info("Set Last 24 hours");
		TimeSelectorUtil.setTimeRange(webd, TimeRange.Last24Hours);
		currentURL = webd.getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_24_HOUR"), true);
		
		webd.getLogger().info("Set Last 7 days");
		TimeSelectorUtil.setTimeRange(webd, TimeRange.NewLast7Days);
		currentURL = webd.getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_7_DAY"), true);
		
		webd.getLogger().info("Set Last 14 days");
		TimeSelectorUtil.setTimeRange(webd, TimeRange.Last14Days);
		currentURL = webd.getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_14_DAY"), true);
		
		webd.getLogger().info("Set custom Last 8 mins");
		TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 8, TimeUnit.Minute);
		currentURL = webd.getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_8_MINUTE"), true);
		
		webd.getLogger().info("Set custom Last 14 hours");
		TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 14, TimeUnit.Hour);
		currentURL = webd.getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_14_HOUR"), true);
		
		webd.getLogger().info("Set custom Last 5 days");
		TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 5, TimeUnit.Day);
		currentURL = webd.getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_5_DAY"), true);
		
		webd.getLogger().info("Set custom Last 3 weeks");
		TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 3, TimeUnit.Week);
		currentURL = webd.getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_3_WEEK"), true);
		
		webd.getLogger().info("Set custom Last 6 months");
		TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 6, TimeUnit.Month);
		currentURL = webd.getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_6_MONTH"), true);
		
		webd.getLogger().info("Set custom Last 2 years");
		TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, TimeUnit.Year);
		currentURL = webd.getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_2_YEAR"), true);		
	}
}
