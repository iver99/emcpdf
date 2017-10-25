package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class VerifyWidget extends LoginAndLogout
{
	private String dbName_WithWidget = "";

	private final String WidgetName_LA = "Database Errors Trend";
	private final String WidgetName_ITA = "Treemap";

	@BeforeClass
	public void createTestDashboard()
	{
		dbName_WithWidget = "withWidget-" + DashBoardUtils.generateTimeStamp();
		String dbDesc = "Dashboard with Widget";

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in createTestDashboard");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//create dashboard
		webd.getLogger().info("Start to create dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, dbName_WithWidget, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_WithWidget, dbDesc, true), "Create dashboard failed!");

		//add widget
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, WidgetName_LA);
		DashboardBuilderUtil.addWidgetToDashboard(webd, WidgetName_ITA);
		webd.getLogger().info("Add widget finished");

		//verify if the widget added successfully
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, WidgetName_LA), "Widget '" + WidgetName_LA + "' not found");
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, WidgetName_ITA), "Widget '" + WidgetName_ITA + "' not found");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		LoginAndLogout.logoutMethod();
	}

	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);
	}

	@AfterClass
	public void removeTestData()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: removeTestData");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to the grid view
		webd.getLogger().info("Swtich to the grid view");
		DashboardHomeUtil.gridView(webd);

		//remove the test data
		webd.getLogger().info("Start to remove the test data...");

		DashBoardUtils.deleteDashboard(webd, dbName_WithWidget);

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	//open a ITA widget in dashboard builder page
	@Test
	public void testOpenITAWidget()
	{
		//init test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testOpenITAWidget");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to Grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboard
		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_WithWidget);

		//open the widget
		webd.getLogger().info("Open the widget");
		DashboardBuilderUtil.openWidget(webd, WidgetName_ITA);

		webd.switchToWindow();
		webd.getLogger().info("Wait for the widget loading....");
		
		webd.waitForElementVisible("//*[@id='save_widget_btn']");
		
		//verify the open url
		DashBoardUtils.verifyURL_WithPara(webd, "emcta/ta/analytics.html");
	}

	//open a LA widget in dashboard builder page
	@Test
	public void testOpenLAWidget()
	{
		//init test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testOpenLAWidget");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to Grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboard
		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_WithWidget);

		//open the widget
		webd.getLogger().info("Open the widget");
		DashboardBuilderUtil.openWidget(webd, WidgetName_LA);

		webd.switchToWindow();
		webd.getLogger().info("Wait for the widget loading....");
		
		webd.waitForElementVisible("//*[@id='srchSrch']");
		
		//verify the open url
		DashBoardUtils.verifyURL_WithPara(webd, "emlacore/html/log-analytics-search.html?widgetId=2013&dashboardId");
	}
}
