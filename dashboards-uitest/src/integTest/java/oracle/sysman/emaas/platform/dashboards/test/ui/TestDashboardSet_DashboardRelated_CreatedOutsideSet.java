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
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestDashboardSet_DashboardRelated_CreatedOutsideSet extends LoginAndLogout
{
	private String dbsetName = "";
	private String dbName = "";

	@BeforeClass
	public void createTestData()
	{
		dbsetName = "DashboardSet_DashboardRelatedTest-OutsideSet" + DashBoardUtils.generateTimeStamp();
		dbName = "Dashboard_CreatedOutsideSet-" + DashBoardUtils.generateTimeStamp();
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

		//back to the home page, continue to create dashboard
		webd.getLogger().info("Go back to the home page, continue to create dashboard fo testing");
		BrandingBarUtil.visitDashboardHome(webd);
		WaitUtil.waitForPageFullyLoaded(webd);
		webd.getLogger().info("Create a new dashboard");
		DashboardHomeUtil.createDashboard(webd, dbName, "", DashboardHomeUtil.DASHBOARD);

		//verify the dashboard
		webd.getLogger().info("Verify if the dashboard existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName, "", true), "Dashboard NOT found!");

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
		DashBoardUtils.deleteDashboard(webd, dbName);
		DashBoardUtils.deleteDashboard(webd, dbsetName);

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	@Test
	public void testAddDashboardInGridView()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testAddDashboardInGridView");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		//add a dashboard to the dashboard set
		webd.getLogger().info("Add a dashboard into the dashborad set");
		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);
		DashboardBuilderUtil.addNewDashboardToSet(webd, dbName);

		//verify the dashboard has been added to the dashboard set
		webd.getLogger().info("Verify if the dashboard exists in the dashborad set");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName), "Dashboard is NOT in the dashboard set");

	}

	@Test(dependsOnMethods = { "testRemoveDashboardFromDashboardSet" })
	public void testAddDashboardInListView()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testAddDashboardInListView");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		//add a dashboard to the dashboard set
		webd.getLogger().info("Add a dashboard into the dashborad set");

		//switch to list view
		webd.getLogger().info("Switch to the list view");
		DashboardHomeUtil.listView(webd);
		DashboardBuilderUtil.addNewDashboardToSet(webd, dbName);

		//verify the dashboard has been added to the dashboard set
		webd.getLogger().info("Verify if the dashboard exists in the dashborad set");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName), "Dashboard is NOT in the dashboard set");

	}

	//this case is for EMCPDF-2801, regression test
	@Test(dependsOnMethods = { "testAddDashboardInListView" })
	public void testDeleteDashboardSelectedBySetFromHomePage()
	{

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testDeleteDashboardSelectedBySetFromHomePage");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//remove the dashboard selected by dashboard set in home page
		webd.getLogger().info("Remove the dashboard selected by dashboard set in home page");
		DashboardHomeUtil.deleteDashboard(webd, dbName, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.search(webd, dbsetName);
		DashboardHomeUtil.selectDashboard(webd, dbsetName);

		//verify the dashboard set can be opened successfully
		webd.getLogger().info("Verify the dashboard set can be opened successfully");
		DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName);
	}

	@Test(dependsOnMethods = { "testAddDashboardInGridView" })
	public void testModifyDashboardInSet_dbCreatedOutsideSet()
	{

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testModifyDashboardInSet_dbCreatedOutsideSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName);

		webd.getLogger().info("Select the created dashboard in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, dbName);

		dbName = dbName + "-Modify";
		webd.getLogger().info("Modify the created dashboard in set");
		DashboardBuilderUtil.editDashboard(webd, dbName, "Modify the dasbboard in set", true);

		webd.getLogger().info("Verify the modified dashboard in set");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName), "Modified dashboard not in set");

		//click Add dashboard icon
		webd.getLogger().info("Click Add Dashboard Icon");
		webd.click("css=" + PageId.DASHBOARDSETADDDASHBOARDICON_CSS);

		//search the dashboard modified in set
		DashboardHomeUtil.search(webd, dbName);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName), "Expected dashboard '" + dbName + "' NOT found");

		//back to home page and verify the dashboard
		webd.getLogger().info("Back to the dashboard home page and verify the modified dashboard");
		BrandingBarUtil.visitDashboardHome(webd);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName), "Expected dashboard '" + dbName
				+ "' NOT found in dashboard home page");

	}

	@Test(dependsOnMethods = { "testModifyDashboardInSet_dbCreatedOutsideSet" })
	public void testRemoveDashboardFromDashboardSet()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testRemoveDashboardFromDashboardSet");

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

		//remove a dashboard from the dashboard set
		webd.getLogger().info("Remove a dashboard from the dashborad set");
		DashboardBuilderUtil.removeDashboardFromSet(webd, dbName);

		//verify the dashboard has been added to the dashboard set
		webd.getLogger().info("Verify if the dashboard exists in the dashborad set");
		Assert.assertFalse(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName), "Dashboard is in the dashboard set");

	}
}
