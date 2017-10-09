package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class TestDashBoard_SimpleCRUD extends LoginAndLogout
{
	private String dbName_noDesc = "";
	private String dbName_noWidgetGrid = "";
	private String dbName_noWidgetList = "";
	private String dbName_withWidgetGrid = "";

	private final String WidgetName_1 = "Top Hosts by Log Entries";
	private final String WidgetName_2 = "Access Log Error Status Codes";

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
		DashBoardUtils.deleteDashboard(webd, dbName_noDesc);
		DashBoardUtils.deleteDashboard(webd, dbName_noWidgetGrid);
		DashBoardUtils.deleteDashboard(webd, dbName_noWidgetList);
		DashBoardUtils.deleteDashboard(webd, dbName_withWidgetGrid);

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	//@Test(dependsOnMethods = { "testCreateDashboard_noWidget_ListView" })
	/*
	    public void Test_targetselector()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards  in list view");

		//search the dashboard
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, "noWidgetListView");

		//open the dashboard in builder page
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_noWidgetList);

		//edit the dashboard in Target selector page
		DashboardBuilderUtil.EditDashboard_targetselctor(webd, "noWidgetListView", "noWidgetListView desc2");

	}
	 */

	@Test(groups = "Group1")
	public void testCreateDashboad_noDesc_GridView()
	{
		dbName_noDesc = "NoDesc-" + generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testCreateDashboad_noDesc_GridView");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: no description, with time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_noDesc, null, DashboardHomeUtil.DASHBOARD);
		webd.getLogger().info("verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_noDesc, null, true), "Create dashboard failed!");

		//verify the refresh option
		webd.getLogger().info("Verify the default refersh option is 5 mins");
		Assert.assertTrue(
				DashboardBuilderUtil.isRefreshSettingChecked(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_5MIN),
				"Refresh option is NOT set to 5 mins!");

		webd.getLogger().info("Turn off the refresh option and check the option is checked");
		DashboardBuilderUtil.refreshDashboard(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);
		Assert.assertTrue(
				DashboardBuilderUtil.isRefreshSettingChecked(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF),
				"Refresh option is NOT set to OFF!");

		webd.getLogger().info("Turn on the refresh option to 5 mins and check the option is checked");
		DashboardBuilderUtil.refreshDashboard(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_5MIN);
		Assert.assertTrue(
				DashboardBuilderUtil.isRefreshSettingChecked(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_5MIN),
				"Refresh option is NOT set to 5 mins!");

		//delete the dashboard
		webd.getLogger().info("start to delete dashboard in builder page");
		DashboardBuilderUtil.deleteDashboard(webd);
	}

	@Test(groups = "Group4", dependsOnGroups = { "Group3" })
	public void testCreateDashboad_noWidget_GridView()
	{
		dbName_noWidgetGrid = "NoWidgetGridView-" + generateTimeStamp();
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

	@Test(groups = "Group2", dependsOnGroups = { "Group1" })
	public void testCreateDashboard_noWidget_ListView()
	{
		dbName_noWidgetList = "noWidgetListView-" + generateTimeStamp();
		String dbDesc = "Test Dashboard no Widget description";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testCreateDashboard_noWidget_ListView");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to list view");
		DashboardHomeUtil.listView(webd);

		//create dashboard
		webd.getLogger().info("create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_noWidgetList, dbDesc, DashboardHomeUtil.DASHBOARD);
		webd.getLogger().info("verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_noWidgetList, dbDesc, true),
				"Create dashboard failed!");

	}

	@Test(groups = "Group3", dependsOnGroups = { "Group2" })
	public void testCreateDashboard_withWidget_GridView()
	{
		dbName_withWidgetGrid = "withWidget-" + generateTimeStamp();
		String dbDesc = "AAA_testDashBoard desc";

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testCreateDashboard_withWidget_GridView");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//create dashboard
		webd.getLogger().info("Start to create dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, dbName_withWidgetGrid, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_withWidgetGrid, dbDesc, true),
				"Create dashboard failed!");

		//add widget
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, "Database Errors Trend");
		webd.getLogger().info("Add widget finished");

		//verify if the widget added successfully
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, "Database Errors Trend"),
				"Widget 'Database Errors Trend' not found");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);
	}

	@Test(groups = "Group4", dependsOnMethods = { "testCreateDashboad_noWidget_GridView" })
	public void testModifyDashboard_namedesc()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testModifyDashboard_namedesc");

		//edit dashboard
		webd.getLogger().info("Start to edit dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		webd.getLogger().info("Search dashboard");
		DashboardHomeUtil.search(webd, dbName_noWidgetGrid);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetGrid), "Dashboard NOT found!");

		//Open the dashboard
		webd.getLogger().info("Open the dashboard to edit name and description");
		DashboardHomeUtil.selectDashboard(webd, dbName_noWidgetGrid);

		//edit the dashboard
		webd.getLogger().info("Edit the dashboard's name and description, display the description");
		DashboardBuilderUtil.editDashboard(webd, dbName_noWidgetGrid + "-modify",
				"Test_Dashboard_no_Widget_GridView desc modify-displayed", true);

		//Verify the dashboard edited successfully
		webd.getLogger().info("Verify the dashboard edited successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_noWidgetGrid + "-modify",
				"Test_Dashboard_no_Widget_GridView desc modify-displayed", true), "Dashboard edit failed!");

		//edit the dashboard
		webd.getLogger().info("Edit the dashboard's name and description, display the description");
		DashboardBuilderUtil.editDashboard(webd, dbName_noWidgetGrid + "-modify",
				"Test_Dashboard_no_Widget_GridView desc modify-not displayed", false);

		//Verify the dashboard edited successfully
		webd.getLogger().info("Verify the dashboard edited successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_noWidgetGrid + "-modify",
				"Test_Dashboard_no_Widget_GridView desc modify-not displayed", true), "Dashboard edit failed!");
	}

	@Test(groups = "Group3", dependsOnMethods = { "testCreateDashboard_withWidget_GridView" })
	public void testModifyDashboard_widget()
	{
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testModifyDashboard_widget");
		DashboardHomeUtil.gridView(webd);

		//search the dashboard want to modify
		webd.getLogger().info("Search dashboard");
		DashboardHomeUtil.search(webd, dbName_withWidgetGrid);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_withWidgetGrid), "Dashboard NOT found!");

		//open the dashboard in the builder page
		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_withWidgetGrid);

		//add the widget into the dashboard
		webd.getLogger().info("Start to add Widget into the dashboard");

		webd.getLogger().info("Add Widget '" + WidgetName_1 + "' into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, WidgetName_1);
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, WidgetName_1), "Widget '" + WidgetName_1 + "' not found");

		webd.getLogger().info("Add Widget '" + WidgetName_2 + "' into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, WidgetName_2);
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, WidgetName_2), "Widget '" + WidgetName_2 + "' not found");

		webd.getLogger().info("Add Widget '" + WidgetName_1 + "' into the dashboard again");
		DashboardBuilderUtil.addWidgetToDashboard(webd, WidgetName_1);
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, WidgetName_1, 1), "The second Widget '" + WidgetName_1
				+ "' not found");

		webd.getLogger().info("Add widget finished");

		//save dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);
	}

	@Test(groups = "Group4", dependsOnMethods = { "testModifyDashboard_namedesc" })
	public void testRemoveDashboard_GridView()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testRemoveDashboard_GridView");

		//search dashboard
		webd.getLogger().info("Search the dashboard want to delete in grid view");
		DashboardHomeUtil.gridView(webd);
		webd.getLogger().info("Search dashboard");
		DashboardHomeUtil.search(webd, dbName_noWidgetGrid + "-modify");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetGrid + "-modify"), "Dashboard NOT found!");

		//delte dashboard
		webd.getLogger().info("Start to delete dashboard in grid view");
		DashboardHomeUtil.deleteDashboard(webd, dbName_noWidgetGrid + "-modify", DashboardHomeUtil.DASHBOARDS_GRID_VIEW);

		//verify the dashboard deleted
		webd.getLogger().info("Verify the dashboard is deleted");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetGrid + "-modify"), "Delete failed!");

	}

	@Test(groups = "Group2", dependsOnMethods = { "testCreateDashboard_noWidget_ListView" })
	public void testRemoveDashboard_ListView()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testRemoveDashboard_ListView");

		//search dashboard
		webd.getLogger().info("Search the dashboard want to delete in grid view");
		DashboardHomeUtil.listView(webd);
		webd.getLogger().info("Search dashboard");
		DashboardHomeUtil.search(webd, dbName_noWidgetList);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetList), "Dashboard NOT found!");

		//delte dashboard
		webd.getLogger().info("Start to delete dashboard in grid view");
		DashboardHomeUtil.deleteDashboard(webd, dbName_noWidgetList, DashboardHomeUtil.DASHBOARDS_LIST_VIEW);

		//verify the dashboard deleted
		webd.getLogger().info("Verify the dashboard is deleted");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetGrid + "-modify"), "Delete failed!");

	}

	@Test(groups = "Group3", dependsOnMethods = { "testWidgetConfiguration" })
	public void testRemoveDashboardInBuilderPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testRemoveDashboardInBuilderPage");

		//search dashboard
		webd.getLogger().info("Search the dashboard want to delete in grid view");
		DashboardHomeUtil.gridView(webd);
		webd.getLogger().info("Search dashboard");
		DashboardHomeUtil.search(webd, dbName_withWidgetGrid);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_withWidgetGrid), "Dashboard NOT found!");

		//open the dashboard in builder page
		DashboardHomeUtil.selectDashboard(webd, dbName_withWidgetGrid);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_withWidgetGrid, "AAA_testDashBoard desc", true),
				"Dashboard NOT found!");

		//delte the dashboard in builder page
		webd.getLogger().info("start to delete dashboard in builder page");
		DashboardBuilderUtil.deleteDashboard(webd);

		//verify if in the home page
		webd.getLogger().info("verify delete successfully and back to the home page");
		webd.waitForElementPresent("css=" + PageId.DASHBOARDDISPLAYPANELCSS);
	}

	@Test(groups = "Group3", dependsOnMethods = { "testModifyDashboard_widget" })
	public void testWidgetConfiguration()
	{
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testWidgetConfiguration");

		//search the dashboard want to modify
		webd.getLogger().info("search dashboard");
		DashboardHomeUtil.search(webd, dbName_withWidgetGrid);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_withWidgetGrid), "Dashboard NOT found!");

		//open the dashboard in the builder page
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_withWidgetGrid);

		//widget operation
		webd.getLogger().info("hide/show the widget title");
		DashboardBuilderUtil.showWidgetTitle(webd, WidgetName_1, true);
		DashboardBuilderUtil.showWidgetTitle(webd, WidgetName_1, false);

		webd.getLogger().info("Resize the widge");
		DashboardBuilderUtil.resizeWidget(webd, WidgetName_1, DashboardBuilderUtil.TILE_WIDER);
		DashboardBuilderUtil.resizeWidget(webd, WidgetName_1, DashboardBuilderUtil.TILE_NARROWER);
		DashboardBuilderUtil.resizeWidget(webd, WidgetName_1, DashboardBuilderUtil.TILE_TALLER);
		DashboardBuilderUtil.resizeWidget(webd, WidgetName_1, DashboardBuilderUtil.TILE_SHORTER);

		webd.getLogger().info("Move the widget");
		DashboardBuilderUtil.moveWidget(webd, WidgetName_1, DashboardBuilderUtil.TILE_RIGHT);
		DashboardBuilderUtil.moveWidget(webd, WidgetName_1, DashboardBuilderUtil.TILE_LEFT);
		DashboardBuilderUtil.moveWidget(webd, WidgetName_1, DashboardBuilderUtil.TILE_DOWN);
		DashboardBuilderUtil.moveWidget(webd, WidgetName_1, DashboardBuilderUtil.TILE_UP);

		webd.getLogger().info("Remove the widget");
		DashboardBuilderUtil.removeWidget(webd, WidgetName_1);
	//	DashboardBuilderUtil.removeWidget(webd, WidgetName_1);

		//save the dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		//verify the current bashboard has one widget
		webd.getLogger().info("Verify the dashboard");
	//    Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, WidgetName_1), "Widget '" + WidgetName_1 + "' not found");
	//	Assert.assertFalse(DashboardBuilderUtil.verifyWidget(webd, WidgetName_2), "Widget '" + WidgetName_2 + "' found");
	}

	private String generateTimeStamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}

}
