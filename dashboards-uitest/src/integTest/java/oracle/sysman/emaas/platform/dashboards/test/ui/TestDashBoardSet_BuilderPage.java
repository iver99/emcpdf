package oracle.sysman.emaas.platform.dashboards.test.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

public class TestDashBoardSet_BuilderPage extends LoginAndLogout
{

	private String dbsetName_Test1 = "";
	private String dbsetName_Test2 = "";
	private String dbName_InSet1 = "";
	private String dbName_InSet2 = "";
	private String dbsetName_Favorite = "";
	private String dbsetName_Share = "";
	private String dbName_indbSet = "";
	private String dbset_testMaximizeRestore = "";

	@BeforeClass
	public void createTestData()
	{
		dbsetName_Test2 = "DashboardSet_Test_Delete-" + DashBoardUtils.generateTimeStamp();
		String dbsetDesc = "Test delete the dashboard set";
		dbName_InSet2 = "DashboardInSet2-" + DashBoardUtils.generateTimeStamp();

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: createTestData");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboardset
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbsetName_Test2, dbsetDesc, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_Test2), "Dashboard set NOT found!");

		//create a dashboard in dashboard set
		webd.getLogger().info("Create a dashboard inside dashboard set");
		DashboardBuilderUtil.createDashboardInsideSet(webd, dbName_InSet2, null);

		//verify the dashboard is in the dashboard set
		webd.getLogger().info("Verify the created dashboard is in the dashboard set");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_InSet2), "Dashboard NOT found in the set");

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
		DashBoardUtils.deleteDashboard(webd, dbsetName_Test1);
		DashBoardUtils.deleteDashboard(webd, dbsetName_Test2);
		DashBoardUtils.deleteDashboard(webd, dbName_InSet1);
		DashBoardUtils.deleteDashboard(webd, dbName_InSet2);
		DashBoardUtils.deleteDashboard(webd, dbsetName_Favorite);		
		DashBoardUtils.deleteDashboard(webd, dbsetName_Share);
		DashBoardUtils.deleteDashboard(webd, dbName_indbSet);
		DashBoardUtils.deleteDashboard(webd, dbset_testMaximizeRestore);
	
		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	@Test(alwaysRun=true)
	public void testCreateDashboardInSet()
	{
		//create dashboard set
		dbsetName_Test1 = "DashboardSet_For_Test_DashboardInsideSet-" + DashBoardUtils.generateTimeStamp();
		String dbsetDesc = "create the dashboard set to test create dashboard in set";
		dbName_InSet1 = "DashboardInSet-" + DashBoardUtils.generateTimeStamp();

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testCreateDashboardInSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboardset
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbsetName_Test1, dbsetDesc, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_Test1), "Dashboard set NOT found!");

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		//create a dashboard in dashboard set
		webd.getLogger().info("Create a dashboard inside dashboard set");
		DashboardBuilderUtil.createDashboardInsideSet(webd, dbName_InSet1, null);

		//verify the dashboard is in the dashboard set
		webd.getLogger().info("Verify the created dashboard is in the dashboard set");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_InSet1), "Dashboard NOT found in the set");

	}

	@Test(dependsOnMethods = { "testModifyDashboardInSet_dbCreatedInsideSet" })
	public void testDeleteDashboardInSet()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testDeleteDashboardInSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Test1);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		//remove the dashboard from dashboard set
		webd.getLogger().info("Remove the dashboard from the dashboard set");
		DashboardBuilderUtil.removeDashboardFromSet(webd, dbName_InSet1);

		//search the dashboard created in set
		DashboardHomeUtil.search(webd, dbName_InSet1);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_InSet1), "Expected dashboard '" + dbName_InSet1
				+ "' NOT found");

		//delete the dashboard
		DashboardHomeUtil.deleteDashboard(webd, dbName_InSet1, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);

	}

	@Test(dependsOnMethods = { "testSearchDashboardInSet" })
	public void testModifyDashboardInSet_dbCreatedInsideSet()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testModifyDashboardInSet_dbCreatedInsideSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Test1);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		webd.getLogger().info("Select the created dashboard in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, dbName_InSet1);

		dbName_InSet1 = dbName_InSet1 + "-Modify";
		webd.getLogger().info("Modify the created dashboard in set");
		DashboardBuilderUtil.editDashboard(webd, dbName_InSet1, "Modify the dasbboard in set", true);

		webd.getLogger().info("Verify the modified dashboard in set");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_InSet1), "Modified dashboard "
				+ dbName_InSet1 + " not in set");

		//click Add dashboard icon
		webd.getLogger().info("Click Add Dashboard Icon");
		webd.click("css=" + PageId.DASHBOARDSETADDDASHBOARDICON_CSS);

		//search the dashboard modified in set
		DashboardHomeUtil.search(webd, dbName_InSet1);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_InSet1), "Expected dashboard '" + dbName_InSet1
				+ "' NOT found");

		//back to home page and verify the dashboard
		webd.getLogger().info("Back to the dashboard home page and verify the modified dashboard");
		BrandingBarUtil.visitDashboardHome(webd);
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_InSet1), "Expected dashboard '" + dbName_InSet1
				+ "' is found in dashboard home page");

	}

	@Test(alwaysRun=true)
	public void testRemoveDashboardSetWithDashboardCreatedInsideSet()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testRemoveDashboardSetWithDashboardCreatedInsideSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.deleteDashboard(webd, dbsetName_Test2, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);

		//refresh the home page
		webd.getLogger().info("Refresh the home page");
		BrandingBarUtil.visitDashboardHome(webd);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_InSet2), "The expected dashboard " + dbName_InSet1
				+ "NOT found in dashboard home page");

	}

	@Test(dependsOnMethods = { "testCreateDashboardInSet" })
	public void testSearchDashboardCreatedInSetFromHome()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testSearchDashboardCreatedInSetFromHome");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//search the dashboard which created in dashboard set in home page
		webd.getLogger().info("Search the dashboard which created in dashboard set in home page");
		DashboardHomeUtil.search(webd, dbName_InSet1);
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_InSet1), "Expected dashboard '" + dbName_InSet1
				+ "' has been found");

	}

	@Test(dependsOnMethods = { "testSearchDashboardCreatedInSetFromHome" })
	public void testSearchDashboardInSet()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testSearchDashboardInSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Test1);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		//click Add dashboard icon
		webd.getLogger().info("Click Add Dashboard Icon");
		webd.click("css=" + PageId.DASHBOARDSETADDDASHBOARDICON_CSS);

		//search the dashboard created in set
		DashboardHomeUtil.search(webd, dbName_InSet1);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_InSet1), "Expected dashboard '" + dbName_InSet1
				+ "' NOT found");

		//delete the dashboard
		String InfoBtn_xpath = "//div[contains(@aria-label, 'DashboardInSet')]//button";
		webd.getLogger().info("Verfiy the current dashboard can not be deleted");
		webd.click(InfoBtn_xpath);
		WebElement removeButton = webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.RMBTNID));
		Assert.assertFalse(removeButton.isEnabled(), "delete is enabled for current dashboard");
	}
	
	@Test(groups = "first run")
	public void testFavorite()
	{
		dbsetName_Favorite = "DashboardSet_Favorite-" + DashBoardUtils.generateTimeStamp();
		String dbsetDesc = "set the dashboard set as favorite";

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testFavorite");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboardset
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbsetName_Favorite, dbsetDesc, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_Favorite), "Dashboard set NOT found!");

		//set the dashboard set as favorite
		webd.getLogger().info("Set the dashboard as favorite");
		Assert.assertTrue(DashboardBuilderUtil.favoriteOptionDashboardSet(webd), "Set the dashboard set as favorite failed!");

		//verify the dashboard set if set as favorite
		webd.getLogger().info("Click my favorite link in the branding bar");
		BrandingBarUtil.visitMyFavorites(webd);
		WaitUtil.waitForPageFullyLoaded(webd);
		webd.getLogger().info("Verify if the dashboard set is favorite");
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "favorites"), "My Favorites options is NOT checked!");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbsetName_Favorite), "NOT find the dashboard set");

		//open the dashboard set and remove the favorite
		webd.getLogger().info("Open the dashboard set in builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Favorite);
		webd.getLogger().info("Set the dashboard set as not favorite");
		Assert.assertFalse(DashboardBuilderUtil.favoriteOptionDashboardSet(webd), "Set the dashboard set as NOT favorite failed!");

		//verify the dashboard set if not as favorite
		webd.getLogger().info("Click my favorite link in the branding bar");
		BrandingBarUtil.visitMyFavorites(webd);
		WaitUtil.waitForPageFullyLoaded(webd);
		webd.getLogger().info("Verify if the dashboard set is not favorite");
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "favorites"), "My Favorites options is NOT checked!");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbsetName_Favorite), "dashboard set is still favorite");
	}
	
	@Test(groups = "second run", dependsOnMethods = { "testMRWidgetSelfDbSet_sysBb" })
	public void testMRWidgetSelfDbSet_selfBb()
	{
		dbName_indbSet = "selfDb-" + DashBoardUtils.generateTimeStamp();

		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testMaximize/RestoreWidget in selt create db set");

		//open the created dashboard set
		webd.getLogger().info("select and open the dashboard set");
		DashboardHomeUtil.selectDashboard(webd, dbset_testMaximizeRestore);

		//add self created dashboard to the dashboard set
		webd.getLogger().info("Add another dashboard to set: create a new dashboard to this set");
		DashboardBuilderUtil.createDashboardInsideSet(webd, dbName_indbSet, "");

		webd.getLogger().info("Verify if the dashboard existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_indbSet),
				"Creat dashboard in dashboard set failed!");

		//Add widget
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, "Access Log Error Status Codes");
		DashboardBuilderUtil.addWidgetToDashboard(webd, "All Logs Trend");
		webd.getLogger().info("Add widget finished");

		//verify if the widget added successfully
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, "Access Log Error Status Codes"),
				"Widget 'Access Log Error Status Codes' not found");
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, "All Logs Trend"), "Widget 'All Logs Trend' not found");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		//widget operation
		webd.getLogger().info("maximize/restore the widget");
		DashboardBuilderUtil.maximizeWidget(webd, "Access Log Error Status Codes", 0);
		Assert.assertFalse(webd.isDisplayed("css=" + ".dbd-widget[data-tile-name=\"All Logs Trend\"]"),
				"Widget 'All Logs Trend' is still displayed");

		DashboardBuilderUtil.restoreWidget(webd, "Access Log Error Status Codes", 0);
		Assert.assertTrue(webd.isDisplayed("css=" + ".dbd-widget[data-tile-name=\"All Logs Trend\"]"),
				"Widget 'All Logs Trend' is not displayed");

		//verify the edit/add button displayed in the page
		WebElement addButton2 = webd.getWebDriver().findElement(By.xpath("//button[@title='Add Content']"));
		Assert.assertTrue(addButton2.isDisplayed(), "Add button isn't displayed in self dashboard which in self dashboard set");

		WebElement editButton2 = webd.getWebDriver().findElement(By.xpath("//button[@title='Edit Settings']"));
		Assert.assertTrue(editButton2.isDisplayed(), "Edit button isn't displayed in self dashboard which in self dashboard set");
	}

	//test maxmize/restore widget in self created dashboard set, and select a system dashboard to test edit button
	@Test(groups = "second run", dependsOnGroups = { "first run" })
	public void testMRWidgetSelfDbSet_sysBb()
	{
		dbset_testMaximizeRestore = "selfDbSet-" + DashBoardUtils.generateTimeStamp();

		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testMaximize/RestoreWidget in selt create db set");

		//Create dashboard set
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbset_testMaximizeRestore, null, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboard set
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbset_testMaximizeRestore),
				"Create dashboard set failed!");

		//Select a dashboard and open it
		webd.getLogger().info("select and open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Application Servers");

		//widget operation
		webd.getLogger().info("maximize/restore the widget");
		DashboardBuilderUtil.maximizeWidget(webd, "Application Server Status", 0);
		DashboardBuilderUtil.restoreWidget(webd, "Application Server Status", 0);

		//verify the add button not displayed in the page
		//WebElement addButton1 = webd.getWebDriver().findElement(By.xpath("//button[@title='Add Content']"));
		//Assert.assertFalse(addButton1.isDisplayed(), "Add button be displayed in system dashboard set");
		Assert.assertFalse(webd.isElementPresent("//button[@title='Add Content']"),
				"Add button be displayed in system dashboard set");

		//verify the edit button displayed in the page
		WebElement editButton1 = webd.getWebDriver().findElement(By.xpath("//button[@title='Edit Settings']"));
		Assert.assertTrue(editButton1.isDisplayed(), "Edit button isn't displayed in self dashboard set");
	}

	@Test(groups = "first run")
	public void testPrint()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testPrint");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard set in builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Test2);

		//print the dashboard set
		webd.getLogger().info("Print the dashboard set");
		DashboardBuilderUtil.printDashboardSet(webd);
	}

	@Test(groups = "first run")
	public void testRefresh()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testRefresh");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Test2);

		//verify the refresh status
		webd.getLogger().info("Verify the defaul refresh setting for the dashboard is 5 mins");
		Assert.assertTrue(DashboardBuilderUtil.isRefreshSettingCheckedForDashbaordSet(webd,
				DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_5MIN), "the default setting is not ON(5 mins)");

		//set the refresh setting to 5 min
		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);
		webd.getLogger().info("Verify the refresh setting for the dashboard is OFF");
		Assert.assertTrue(DashboardBuilderUtil.isRefreshSettingCheckedForDashbaordSet(webd,
				DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF), "the setting is not OFF");

		//set the refresh setting to OFF
		webd.getLogger().info("Set the refresh setting to 5 mins");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_5MIN);
		webd.getLogger().info("Verify the refresh setting for the dashboard is 5 mins");
		Assert.assertTrue(DashboardBuilderUtil.isRefreshSettingCheckedForDashbaordSet(webd,
				DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_5MIN), "the setting is not ON(5 mins)");
	}

	@Test(groups = "first run")
	public void testShare()
	{
		dbsetName_Share = "DashboardSet-Share-" + DashBoardUtils.generateTimeStamp();
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testShare");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboardset
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbsetName_Share, null, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_Share), "Dashboard set NOT found!");

		//add the ITA dashboard into the dashboard set
		webd.getLogger().info("Add a ITA oob dashboard into the set");
		DashboardBuilderUtil.addNewDashboardToSet(webd, "Categorical - Basic");

		//back to the home page
		webd.getLogger().info("Back to dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Share);

		//share the dashboardset
		webd.getLogger().info("Share the dashboard");
		Assert.assertTrue(DashboardBuilderUtil.toggleShareDashboardset(webd), "Share the dashboard set failed!");

	}
	
	@Test(groups = "first run", dependsOnMethods = { "testShare" })
	public void testStopSharing()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testStopSharing");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Share);

		//share the dashboardset
		webd.getLogger().info("Share the dashboard");
		Assert.assertFalse(DashboardBuilderUtil.toggleShareDashboardset(webd), "Stop sharing the dashboard set failed!");

	}
}
