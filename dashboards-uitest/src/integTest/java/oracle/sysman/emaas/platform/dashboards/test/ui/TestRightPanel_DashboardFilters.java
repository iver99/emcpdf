package oracle.sysman.emaas.platform.dashboards.test.ui;

import java.util.logging.Logger;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.EntitySelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.GlobalContextUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class TestRightPanel_DashboardFilters extends LoginAndLogout
{
	private String dbRespectGCTimeRangeName_UDE = "";
	private String dbRespectGCTimeRangeName_LA = "";
	private String dbRespectGCEntityName = "";
	private String dbName_UDEWidget = "";
	private String dbName_LAWidget = "";

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
		DashBoardUtils.deleteDashboard(webd, dbRespectGCTimeRangeName_UDE);
		DashBoardUtils.deleteDashboard(webd, dbRespectGCTimeRangeName_LA);
		DashBoardUtils.deleteDashboard(webd, dbRespectGCEntityName);
		DashBoardUtils.deleteDashboard(webd, dbName_LAWidget);
		DashBoardUtils.deleteDashboard(webd, dbName_UDEWidget);

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	@Test
	public void testRespectGCEntities()
	{
		dbRespectGCEntityName = "respectGC Entity-" + DashBoardUtils.generateTimeStamp();

		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test testRespectGCEntities");

		//Create dashboard
		DashboardHomeUtil.createDashboard(webd, dbRespectGCEntityName, null, DashboardHomeUtil.DASHBOARD);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbRespectGCEntityName, null, true),
				"Create dashboard failed!");

		//add widget to the dashboard
		webd.getLogger().info("Add widget to the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, widgetName_UDE);

		//respect GC for Entity
		webd.getLogger().info("Respect GC for Entity");
		DashboardBuilderUtil.respectGCForEntity(webd);

		//save the dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		webd.evalJavascript("scroll(0,0)");

		webd.getLogger().info("Verify Entity displayed in glocal context bar");
		Logger Logger = java.util.logging.Logger.getLogger("entities");
		Assert.assertFalse(EntitySelectorUtil.validateReadOnlyMode(webd, Logger), "Entity not in GC bar");
	}

	@Test
	public void testRespectGCTimeRange_LAWidget() throws InterruptedException
	{
		dbRespectGCTimeRangeName_LA = "respectGC TimeRange LAWidget-" + DashBoardUtils.generateTimeStamp();
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test testRespectGCTimeRange_LAWidget");

		//Create dashboard
		DashboardHomeUtil.createDashboard(webd, dbRespectGCTimeRangeName_LA, null, DashboardHomeUtil.DASHBOARD);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbRespectGCTimeRangeName_LA, null, true),
				"Create dashboard failed!");

		//add widget to the dashboard
		webd.getLogger().info("Add widget to the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, widgetName_LA);

		//respect GC for TimeRange
		webd.getLogger().info("Respect GC for Time Range");
		DashboardBuilderUtil.respectGCForTimeRange(webd);

		//change the time range, verify the url will be changed
		webd.getLogger().info("Change the time range, verify the url will be changed");
		GlobalContextUtil.setFlexibleRelativeTimeRange(webd, 90, TimeUnit.Day);

		//save the dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		String currentURL = webd.getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_90_DAY"), true);

		//open the widget
		webd.getLogger().info("Open the widget");
		DashboardBuilderUtil.openWidget(webd, widgetName_LA);

		webd.waitForServer();

		//Verify the time range in UDE page
		webd.getLogger().info("Verify the time range in LA page");
		Assert.assertEquals(GlobalContextUtil.getTimeRangeLabel_V2(webd).contains("Last 90 days"), true);

		//to verify jira EMCPDF-3338
		//back to dashboard page and verify the time in widget
		webd.getLogger().info("Back to dashboard home page");

		webd.getLogger().info("Check if having hamburger menu");
		if (DashBoardUtils.isHamburgerMenuEnabled(webd)) {
			webd.getLogger().info("Hamburger Menu Enable");
			BrandingBarUtil.goBackToParentMenu(webd);
			BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.ROOT_MENU_DASHBOARDS);
		}
		else {
			BrandingBarUtil.visitDashboardHome(webd);
		}

		DashboardHomeUtil.selectDashboard(webd, dbRespectGCTimeRangeName_LA);

		webd.getLogger().info("Verify the time in widget");
		webd.waitForServer();

		//verify if the widget exsits then execute js to get time range in widget
		String timerange = webd.evalJavascript("return window._contextPassedToWidgetsAtPageLoad && window._contextPassedToWidgetsAtPageLoad.timeSelector && window._contextPassedToWidgetsAtPageLoad.timeSelector.viewTimePeriod()").toString();
		webd.getLogger().info(timerange);
		Assert.assertEquals(timerange, "LAST_90_DAY");
	}

	@Test
	public void testRespectGCTimeRange_UDEWidget() throws InterruptedException
	{
		dbRespectGCTimeRangeName_UDE = "respectGC TimeRange UDEWidget-" + DashBoardUtils.generateTimeStamp();
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test testRespectGCTimeRange_UDEWidget");

		//Create dashboard
		DashboardHomeUtil.createDashboard(webd, dbRespectGCTimeRangeName_UDE, null, DashboardHomeUtil.DASHBOARD);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbRespectGCTimeRangeName_UDE, null, true),
				"Create dashboard failed!");

		//add widget to the dashboard
		webd.getLogger().info("Add widget to the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, widgetName_UDE);

		//respect GC for TimeRange
		webd.getLogger().info("Respect GC for Time Range");
		DashboardBuilderUtil.respectGCForTimeRange(webd);

		//change the time range, verify the url will be changed
		webd.getLogger().info("Change the time range, verify the url will be changed");
		GlobalContextUtil.setTimeRange(webd, TimeRange.Last7Days);

		//save the dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		String currentURL = webd.getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_7_DAY"), true);

		//open the widget
		webd.getLogger().info("Open the widget");
		DashboardBuilderUtil.openWidget(webd, widgetName_UDE);

		webd.waitForServer();

		//Verify the time range in UDE page
		webd.getLogger().info("Verify the time range in UDE page");
		Assert.assertEquals(GlobalContextUtil.getTimeRangeLabel_V2(webd).contains("Last week")
				|| GlobalContextUtil.getTimeRangeLabel_V2(webd).contains("Last 7 days"), true);

		//to verify jira EMCPDF-3338
		//back to dashboard page and verify the time in widget
		webd.getLogger().info("Back to dashboard home page");

		BrandingBarUtil.visitDashboardHome(webd);

		DashboardHomeUtil.selectDashboard(webd, dbRespectGCTimeRangeName_UDE);

		webd.getLogger().info("Verify the time in widget");
		webd.waitForServer();

		//verify if the widget exsits then execute js to get time range in widget
		String timerange = webd.evalJavascript("return window._contextPassedToWidgetsAtPageLoad && window._contextPassedToWidgetsAtPageLoad.timeSelector && window._contextPassedToWidgetsAtPageLoad.timeSelector.viewTimePeriod()").toString();
		webd.getLogger().info(timerange);
		Assert.assertEquals(timerange, "LAST_7_DAY");
	}

	//case to verify jira EMCPDF-3338
	@Test
	public void testShowTimeRange_LAWidget()
	{
		dbName_LAWidget = "TimeRange LAWidget-" + DashBoardUtils.generateTimeStamp();
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test testShowTimeRange_LAWidget");

		//Create dashboard
		DashboardHomeUtil.createDashboard(webd, dbName_LAWidget, null, DashboardHomeUtil.DASHBOARD);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_LAWidget, null, true), "Create dashboard failed!");

		//add widget to the dashboard
		webd.getLogger().info("Add widget to the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, widgetName_LA);

		//respect GC for TimeRange
		webd.getLogger().info("Respect GC for Time Range");
		DashboardBuilderUtil.showTimeRangeFilter(webd, true);

		//change the time range, verify the url will be changed
		webd.getLogger().info("Change the time range, verify the url will be changed");
		GlobalContextUtil.setFlexibleRelativeTimeRange(webd, 90, TimeUnit.Day);

		//save the dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		String currentURL = webd.getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_90_DAY"), false);

		//open the widget
		webd.getLogger().info("Open the widget");
		DashboardBuilderUtil.openWidget(webd, widgetName_LA);

		webd.waitForServer();

		//Verify the time range in UDE page
		webd.getLogger().info("Verify the time range in LA page");
		Assert.assertEquals(GlobalContextUtil.getTimeRangeLabel_V2(webd).contains("Last 90 days"), true);

		//to verify jira EMCPDF-3338
		//back to dashboard page and verify the time in widget
		webd.getLogger().info("Back to dashboard home page");
		webd.getLogger().info("Check if having hamburger menu");
		if (DashBoardUtils.isHamburgerMenuEnabled(webd)) {
			webd.getLogger().info("Hamburger Menu Enable");
			BrandingBarUtil.goBackToParentMenu(webd);
			BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.ROOT_MENU_DASHBOARDS);
		}
		else {
			BrandingBarUtil.visitDashboardHome(webd);
		}

		DashboardHomeUtil.selectDashboard(webd, dbName_LAWidget);

		webd.getLogger().info("Verify the time in widget");
		webd.waitForServer();

		//verify if the widget exsits then execute js to get time range in widget
		String timerange = webd.evalJavascript("return window._contextPassedToWidgetsAtPageLoad && window._contextPassedToWidgetsAtPageLoad.timeSelector && window._contextPassedToWidgetsAtPageLoad.timeSelector.viewTimePeriod()").toString();
		webd.getLogger().info(timerange);
		Assert.assertEquals(timerange, "LAST_90_DAY");
	}

	//case to verify jira EMCPDF-3338
	@Test
	public void testShowTimeRange_UDEWidget()
	{
		dbName_UDEWidget = "TimeRange UDEWidget-" + DashBoardUtils.generateTimeStamp();
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test testRespectGCTimeRange_UDEWidget");

		//Create dashboard
		DashboardHomeUtil.createDashboard(webd, dbName_UDEWidget, null, DashboardHomeUtil.DASHBOARD);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_UDEWidget, null, true), "Create dashboard failed!");

		//add widget to the dashboard
		webd.getLogger().info("Add widget to the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, widgetName_UDE);

		//respect GC for TimeRange
		webd.getLogger().info("Respect GC for Time Range");
		DashboardBuilderUtil.showTimeRangeFilter(webd, true);

		//change the time range, verify the url will be changed
		webd.getLogger().info("Change the time range, verify the url will be changed");
		GlobalContextUtil.setTimeRange(webd, TimeRange.Last7Days);

		//save the dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		String currentURL = webd.getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_7_DAY"), false);

		//open the widget
		webd.getLogger().info("Open the widget");
		DashboardBuilderUtil.openWidget(webd, widgetName_UDE);

		webd.waitForServer();

		//Verify the time range in UDE page
		webd.getLogger().info("Verify the time range in UDE page");
		Assert.assertEquals(GlobalContextUtil.getTimeRangeLabel_V2(webd).contains("Last week")
				|| GlobalContextUtil.getTimeRangeLabel_V2(webd).contains("Last 7 days"), true);

		//to verify jira EMCPDF-3338
		//back to dashboard page and verify the time in widget
		webd.getLogger().info("Back to dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		DashboardHomeUtil.selectDashboard(webd, dbName_UDEWidget);

		webd.getLogger().info("Verify the time in widget");
		webd.waitForServer();

		//verify if the widget exsits then execute js to get time range in widget
		String timerange = webd.evalJavascript("return window._contextPassedToWidgetsAtPageLoad && window._contextPassedToWidgetsAtPageLoad.timeSelector && window._contextPassedToWidgetsAtPageLoad.timeSelector.viewTimePeriod()").trim();
		webd.getLogger().info(timerange);
		Assert.assertEquals(timerange, "LAST_7_DAY");
	}
}
