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
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestDashboardSet_OtherFeatures extends LoginAndLogout
{
	private String dbsetName = "";
	private String dbsetName_Favorite = "";
	private String dbsetName_Share = "";
	private String dbset_testMaximizeRestore = "";
	private String dbName_indbSet = "";
	private String dbName_ShareStatus = "";
	private String dbSetName_ShareStatus = "";
	private String dbSetName_TimeRange = "";

	private String dbName_TimeRange1 = "";
	private String dbName_TimeRange2 = "";

	private static String OOBAddToSet = "Database Operations";
	private static String OOBSetName = "Exadata Health";

	@BeforeClass
	public void createTestData()
	{
		dbsetName = "DashboardSet_FeatureTest-" + DashBoardUtils.generateTimeStamp();
		String dbsetDesc = "Test the dashboard set";

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
		DashboardHomeUtil.createDashboard(webd, dbsetName, dbsetDesc, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName), "Dashboard set NOT found!");

		//back to home page
		webd.getLogger().info("Back to home page");
		BrandingBarUtil.visitDashboardHome(webd);

		dbName_ShareStatus= "test db status - " + DashBoardUtils.generateTimeStamp();
		//create test dashboard
		webd.getLogger().info("Create a new dashboard");
		DashboardHomeUtil.createDashboard(webd, dbName_ShareStatus, "", DashboardHomeUtil.DASHBOARD);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_ShareStatus, "", true), "Dashboard NOT found!");

		//back to home page
		webd.getLogger().info("Back to home page");
		BrandingBarUtil.visitDashboardHome(webd);

		dbName_TimeRange1 = "Test Time Range in Set 1 - " + DashBoardUtils.generateTimeStamp();
		//create test dashboard
		webd.getLogger().info("Create a new dashboard for test change timeRange of dashboard in Dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbName_TimeRange1, "", DashboardHomeUtil.DASHBOARD);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_TimeRange1, "", true), "Dashboard NOT found!");

		//back to home page
		webd.getLogger().info("Back to home page");
		BrandingBarUtil.visitDashboardHome(webd);

		dbName_TimeRange2 = "Test Time Range in Set 2 - " + DashBoardUtils.generateTimeStamp();
		//create test dashboard
		webd.getLogger().info("Create a new dashboard for test change timeRange of dashboard in Dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbName_TimeRange2, "", DashboardHomeUtil.DASHBOARD);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_TimeRange2, "", true), "Dashboard NOT found!");

		//back to home page
		webd.getLogger().info("Back to home page");
		BrandingBarUtil.visitDashboardHome(webd);

		dbSetName_TimeRange = "Test Time Range Set - " + DashBoardUtils.generateTimeStamp();
		//create dashboardset
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbSetName_TimeRange, "", DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbSetName_TimeRange), "Dashboard set NOT found!");

		//add dashboard to the set
		webd.getLogger().info("Add dashboard into set");
		DashboardBuilderUtil.addNewDashboardToSet(webd, dbName_TimeRange1);
		DashboardBuilderUtil.addNewDashboardToSet(webd, dbName_TimeRange2);

		DashboardBuilderUtil.selectDashboardInsideSet(webd, dbName_TimeRange1);

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
		DashBoardUtils.deleteDashboard(webd, dbsetName_Favorite);
		DashBoardUtils.deleteDashboard(webd, dbsetName);
		DashBoardUtils.deleteDashboard(webd, dbsetName_Share);
		DashBoardUtils.deleteDashboard(webd, dbset_testMaximizeRestore);
		DashBoardUtils.deleteDashboard(webd, dbName_indbSet);
		DashBoardUtils.deleteDashboard(webd, dbSetName_ShareStatus);
		DashBoardUtils.deleteDashboard(webd, dbName_ShareStatus);

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
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

	@Test (alwaysRun = true)
	public void testFavorite_OOBSET()
	{
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testFavorite_OOBSET");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//open an OOB dashboard
		webd.getLogger().info("Open an OOB dashboard set");
		DashboardHomeUtil.selectDashboard(webd, OOBSetName);

		//set the OOB as my favorite
		webd.getLogger().info("Set the OOB dashboard set as My Favorite");
		Assert.assertTrue(DashboardBuilderUtil.favoriteOptionDashboardSet(webd),"Fail to set the OOB set as My Favorite");

		//verify the dashboard is favorite
		webd.getLogger().info("Visit my favorite page");
		BrandingBarUtil.visitMyFavorites(webd);

		webd.getLogger().info("Verfiy the favortie checkbox is checked");
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "favorites"), "My Favorites option is NOT checked");

		webd.getLogger().info("Verfiy the dashboard is favorite");
		//DashboardHomeUtil.search(webd, dbName_favorite);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, OOBSetName), "Can not find the dashboard");

		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, OOBSetName);
		webd.getLogger().info("Verify the dashboard set in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, OOBSetName), "Verify OOB dashboard set failed!");

		//set it to not favorite
		webd.getLogger().info("set the dashboard to not favorite");
		Assert.assertFalse(DashboardBuilderUtil.favoriteOptionDashboardSet(webd), "Set to not my favorite dashboard failed!");

		//verify the dashboard is not favoite
		webd.getLogger().info("visit my favorite page");
		BrandingBarUtil.visitMyFavorites(webd);
		webd.getLogger().info("Verfiy the favortie checkbox is checked");
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "favorites"), "My Favorites option is NOT checked");

		webd.getLogger().info("Verfiy the dashboard is not favorite");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, OOBSetName),"The dashboard is still my favorite dashboard");
	}

	//test maxmize/restore widget in OOB Dashboard Set
	@Test(groups = "second run", dependsOnGroups = { "first run" })
	public void testMaximizeRestoreWidget_OOB_DbSet()
	{
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testMaximize/RestoreWidget");

		//open the OOB Dashboard Set, eg: Exadata Health in the dashboard home page
		webd.getLogger().info("open the dashboard set");
		DashboardHomeUtil.selectDashboard(webd, "Exadata Health");

		//widget operation
		webd.getLogger().info("maximize/restore the widget");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Overview");
		DashboardBuilderUtil.maximizeWidget(webd, "Entities by Database Machine", 0);
		DashboardBuilderUtil.restoreWidget(webd, "Entities by Database Machine", 0);

		//verify the edit/add button not displayed in the page
		if(webd.getElementCount("//button[@title='Add Content']")>0)
		{
			Assert.fail("Unexpected: Add button be displayed in system dashboard set");
		}
		
		if(webd.getElementCount("//button[@title='Edit Settings']")>0)
		{
			Assert.fail("Unexpected: Edit button be displayed in system dashboard set");
		}
	}

	//test maxmize/restore widget in self created dashboard set, and self creating a dashboard to test add/edit button
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
		Assert.assertTrue(webd.isDisplayed("//button[@title='Add Content']"), "Add button isn't displayed in self dashboard which in self dashboard set");
		Assert.assertTrue(webd.isDisplayed("//button[@title='Edit Settings']"), "Edit button isn't displayed in self dashboard which in self dashboard set");
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
		Assert.assertFalse(webd.isElementPresent("//button[@title='Add Content']"),
				"Add button be displayed in system dashboard set");

		//verify the edit button displayed in the page		
		Assert.assertTrue(webd.isDisplayed("//button[@title='Edit Settings']"), "Edit button isn't displayed in self dashboard set");
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
		DashboardHomeUtil.selectDashboard(webd, dbsetName);

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
		DashboardHomeUtil.selectDashboard(webd, dbsetName);

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

	@Test(groups = "first run")
	public void testShareWithoutDashboardInSet()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testShareWithoutDashboardInSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the share options are diabled
		Assert.assertTrue(DashBoardUtils.verfiyShareOptionDisabled(), "The options are enabled!");
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

	@Test(alwaysRun = true)
	public void testCheckShareStatus()
	{
		dbSetName_ShareStatus = "DashboardSet-Share-Status-" + DashBoardUtils.generateTimeStamp();
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testCheckShareStatus");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//verify the dashboard is not shared
		webd.getLogger().info("Verify the dashboard '" + dbName_ShareStatus + "' is not shared");
		DashboardHomeUtil.selectDashboard(webd, dbName_ShareStatus);
		Assert.assertFalse(DashBoardUtils.isDashboardShared(webd), "The dashboard has been shared");

		//back to the home page
		webd.getLogger().info("Back to dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//create dashboardset
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbSetName_ShareStatus, null, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbSetName_ShareStatus), "Dashboard set NOT found!");

		//add the dashboard into the dashboard set
		webd.getLogger().info("Add dashboard '" +  dbName_ShareStatus + "' into the set");
		DashboardBuilderUtil.addNewDashboardToSet(webd, dbName_ShareStatus);

		//share the dashboardset
		webd.getLogger().info("Share the dashboard");
		Assert.assertTrue(DashboardBuilderUtil.toggleShareDashboardset(webd), "Share the dashboard set failed!");

		//back to the home page
		webd.getLogger().info("Back to dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//open the dashboard
		webd.getLogger().info("Open the dashboard '" +  dbName_ShareStatus+ "'");
		DashboardHomeUtil.selectDashboard(webd, dbName_ShareStatus);

		//verify the dashboard is shared
		webd.getLogger().info("Verify the dashboard '" + dbName_ShareStatus + "' is shared");
		Assert.assertTrue(DashBoardUtils.isDashboardShared(webd), "The dashboard has not been shared");

		//back to the home page
		webd.getLogger().info("Back to dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//open the dashboard
		webd.getLogger().info("Open the dashboard set '" +  dbSetName_ShareStatus+ "'");
		DashboardHomeUtil.selectDashboard(webd, dbSetName_ShareStatus);

		//not share the dashboard set
		webd.getLogger().info("Unshare the dashboard");
		Assert.assertFalse(DashboardBuilderUtil.toggleShareDashboardset(webd), "Unshare the dashboard set failed!");

		//back to the home page
		webd.getLogger().info("Back to dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//open the dashboard
		webd.getLogger().info("Open the dashboard '" +  dbName_ShareStatus+ "'");
		DashboardHomeUtil.selectDashboard(webd, dbName_ShareStatus);

		//verify the dashboard is shared
		webd.getLogger().info("Verify the dashboard '" + dbName_ShareStatus + "' is shared");
		Assert.assertTrue(DashBoardUtils.isDashboardShared(webd), "The dashboard has not been shared");
	}

	@Test(alwaysRun = true)
	public void testChangeDashboardTimeRange()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testChangeDashboardTimeRange");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboard set
		webd.getLogger().info("Open the dashboard set");
		DashboardHomeUtil.selectDashboard(webd, dbSetName_TimeRange);

		//select the dashboard
		webd.getLogger().info("Select the dashboard : <" + dbName_TimeRange1 + ">");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, dbName_TimeRange1);

		//get the default time range in dashboard
		webd.getLogger().info("Get the default time rang of dashboard : <" + dbName_TimeRange1 + ">");
		String timeRange_1 = TimeSelectorUtil.getTimeRangeLabel(webd);
		//configure the time range
		webd.getLogger().info("Set the Time Range of dashboard  : <" + dbName_TimeRange1 + ">");
		TimeSelectorUtil.setTimeRange(webd, ITimeSelectorUtil.TimeRange.Last24Hours);
		String timeRangeNew_1 = TimeSelectorUtil.getTimeRangeLabel(webd);

		//select the dashboard
		webd.getLogger().info("Select the dashboard : <" + dbName_TimeRange2 + ">");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, dbName_TimeRange2);

		//get the default time range in dashboard
		webd.getLogger().info("Get the default time rang of dashboard : <" + dbName_TimeRange2 + ">");
		String timeRange_2 = TimeSelectorUtil.getTimeRangeLabel(webd,2);
		Assert.assertNotEquals(timeRangeNew_1, timeRange_2);
		//configure the time range
		webd.getLogger().info("Set the Time Range of dashboard  : <" + dbName_TimeRange1 + ">");
		TimeSelectorUtil.setTimeRange(webd,2,ITimeSelectorUtil.TimeRange.Last8Hours);
		String timeRangeNew_2 = TimeSelectorUtil.getTimeRangeLabel(webd,2);

		//select the dashboard
		webd.getLogger().info("Select the dashboard : <" + dbName_TimeRange1 + ">");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, dbName_TimeRange1);
		Assert.assertEquals(timeRangeNew_1, TimeSelectorUtil.getTimeRangeLabel(webd));
		Assert.assertNotEquals(timeRangeNew_1,timeRangeNew_2);
	}
}
