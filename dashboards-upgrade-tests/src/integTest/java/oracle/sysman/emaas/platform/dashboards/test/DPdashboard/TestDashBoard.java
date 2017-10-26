package oracle.sysman.emaas.platform.dashboards.test.DPdashboard;

import oracle.sysman.emaas.platform.dashboards.test.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @version
 * @author siakula
 * @since release specific (what release of product did this appear in)
 */

public class TestDashBoard extends LoginAndLogout
{
	private String dbName_noWidgetGrid = "";
	public static String dbName_WithWidget = "pre#_$dashboard%^&中文";
	public static String dbName_EnableEntitiesTime = "Test Entities and Time Selector Enabled";
	public static String dbName_DisableEntitiesTime = "Test Entities and Time Selector Disabled";
	public static String WidgetName_LA = "Database Errors Trend";
	public static String WidgetName_UDE = "Area Chart";
	public static String dbName_GCenabled = "dbGCenabled";

	@Test
	public void createTestDashboard()
	{
		//dbName_WithWidget = "dashbaord$!@#-" + generateTimeStamp();
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
		DashboardBuilderUtil.addWidgetToDashboard(webd, WidgetName_UDE);
		webd.getLogger().info("Add widget finished");

		//verify if the widget added successfully
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, WidgetName_LA), "Widget '" + WidgetName_LA + "' not found");
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, WidgetName_UDE), "Widget '" + WidgetName_UDE + "' not found");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);
	}

	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		try {
			DashBoardUtils.loadWebDriver(webd);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateDashboad_noWidget_GridView()
	{
		dbName_noWidgetGrid = "NoWidgetGridView-" + DashBoardUtils.generateTimeStamp();
		String dbDesc = "Test Dashboard no Widget description";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testCreateDashboad_noWidget_GridView");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_noWidgetGrid, dbDesc, DashboardHomeUtil.DASHBOARD);
		webd.getLogger().info("verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_noWidgetGrid, dbDesc, true),
				"Create dashboard failed!");
	}

	@Test
	public void testDisableEntitiesTimeSelector()
	{
		//dbName_DisableEntitiesTime = dbName_DisableEntitiesTime + " - " + generateTimeStamp();
		String dbDesc = "This dshboard is test Entities Selector and Time Selector disabled";
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testDisableEntitiesTimeSelector");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//create dashboard
		webd.getLogger().info("Start to create dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, dbName_DisableEntitiesTime, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_DisableEntitiesTime, dbDesc, true),
				"Create dashboard failed!");

		//add widget
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, WidgetName_LA);
		webd.getLogger().info("Add widget finished");

		//verify if the widget added successfully
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, WidgetName_LA), "Widget '" + WidgetName_LA + "' not found");

		//disable Entities selector
		webd.getLogger().info("Disable Entites Selector - Use entities defined by content on page");
		DashboardBuilderUtil.showEntityFilter(webd, false);

		//disable Time Selector
		webd.getLogger().info("Disable Time Selector - Use time range defined by content on page");
		DashboardBuilderUtil.showTimeRangeFilter(webd, false);

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

	}

	@Test(dependsOnMethods = { "testCreateDashboad_noWidget_GridView", "testModifyDashboard_namedesc" })
	public void testDuplicateDashboard()
	{
		String dbName = "Test_Dashboard_duplicate";
		String dbDesc = "Test_Dashboard_duplicate_desc";

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testDuplicateDashboard");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_noWidgetGrid + "-modify");
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetGrid + "-modify"), "Dashboard NOT found");

		//open the dashboard
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_noWidgetGrid + "-modify");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_noWidgetGrid + "-modify",
				"Test_Dashboard_no_Widget_GridView desc modify", true), "Dashboard NOT found");

		//Duplicate dashbaord
		DashboardBuilderUtil.duplicateDashboard(webd, dbName, dbDesc);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName, dbDesc, true), "Duplicate failed!");
	}

	@Test
	public void testEnabledEntitiesTimeSelector()
	{
		//dbName_EnableEntitiesTime = dbName_EnableEntitiesTime + " - " + generateTimeStamp();
		String dbDesc = "This dashboard is test Entities Selector and Time Selector enabled";
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testDisableEntitiesTimeSelector");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		DashboardHomeUtil.createDashboard(webd, dbName_EnableEntitiesTime, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_EnableEntitiesTime, dbDesc, true),
				"Create dashboard failed!");

		//add widget
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, WidgetName_UDE);
		webd.getLogger().info("Add widget finished");

		//verify if the widget added successfully
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, WidgetName_UDE), "Widget '" + WidgetName_UDE + "' not found");

		//enable Entities selector
		webd.getLogger().info("Enable Entites Selector - Use entities on dashboard page");
		DashboardBuilderUtil.showEntityFilter(webd, true);

		//disable Time Selector
		webd.getLogger().info("Enable Time Selector - Use time range on dashboard page");
		DashboardBuilderUtil.showTimeRangeFilter(webd, true);

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);
	}

	@Test(dependsOnMethods = { "testCreateDashboad_noWidget_GridView" })
	public void testModifyDashboard_namedesc()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testModifyDashBoard");

		//edit dashboard
		webd.getLogger().info("Start to edit dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		webd.getLogger().info("Search dashboard");
		DashboardHomeUtil.search(webd, dbName_noWidgetGrid);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetGrid), "Dashboard NOT found!");

		//Open the dashboard
		webd.getLogger().info("Open the dashboard to edit name and description");
		DashboardHomeUtil.selectDashboard(webd, dbName_noWidgetGrid);
		DashboardBuilderUtil.editDashboard(webd, dbName_noWidgetGrid + "-modify",
				"Test_Dashboard_no_Widget_GridView desc modify", false);

		//Verify the dashboard edited successfully
		webd.getLogger().info("Verify the dashboard edited successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_noWidgetGrid + "-modify",
				"Test_Dashboard_no_Widget_GridView desc modify", true), "Dashboard edit failed!");
	}

	@Test
    public void createDashboard_GCenabled()
	{
		String dbDesc = "Dashboard with GC enabled";

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in createTestDashboard");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//create dashboard
		webd.getLogger().info("Start to create dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, dbName_GCenabled, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_GCenabled, dbDesc, true), "Create dashboard failed!");

		//add widget
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, WidgetName_LA);
		webd.getLogger().info("Add widget finished");

		//verify if the widget added successfully
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, WidgetName_LA), "Widget '" + WidgetName_LA + "' not found");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);	
		
		//Enable the GC 
		DashboardBuilderUtil.respectGCForEntity(webd);
		DashboardBuilderUtil.respectGCForTimeRange(webd);						
	}
}