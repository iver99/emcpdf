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

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestDashboardSet_SimpleCRUD extends LoginAndLogout
{
	private String dbsetName_Test_NoDesc = "";
	private String dbsetDesc_Test = "";
	private String dbsetName_Simple_CRUD = "";
	private String dbsetDesc_Simple_CRUD = "";
	private String dbsetName_withOOB = "";

	private static String OOBAddToSet = "Database Operations";
	private static String OOBAddToSet_VerifyDelete = "Entities";

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
		DashBoardUtils.deleteDashboard(webd, dbsetName_Test_NoDesc);
		DashBoardUtils.deleteDashboard(webd, dbsetName_Simple_CRUD);
		DashBoardUtils.deleteDashboard(webd, dbsetName_Test_NoDesc + "-Modify");
		DashBoardUtils.deleteDashboard(webd, dbsetName_Simple_CRUD + "-Modify");

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	//This test is to test creating dashboard set
	//create a dashboard set with name and description in grid view
	@Test
	public void testCreateDashboardSet()
	{
		dbsetName_Simple_CRUD = "DashboardSet-WithDesc" + DashBoardUtils.generateTimeStamp();
		dbsetDesc_Simple_CRUD = "Test the dashboard set with description";

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testCreateDashboardSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboardset
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbsetName_Simple_CRUD, dbsetDesc_Simple_CRUD, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_Simple_CRUD), "Dashboard set NOT found!");

	}

	//This test is to test creating dashboard set
	//create a dashboard set with name in list view
	@Test
	public void testCreateDashboardWithoutDesc()
	{
		dbsetName_Test_NoDesc = "DashboardSet-NoDesc" + DashBoardUtils.generateTimeStamp();

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testCreateDashboardWithoutDesc");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the list view");
		DashboardHomeUtil.listView(webd);

		//create dashboardset
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbsetName_Test_NoDesc, dbsetDesc_Test, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_Test_NoDesc), "Dashboard set NOT found!");
	}

	@Test
	public void testDeleteDashboardSetWithOOBDashboard()
	{
		dbsetName_withOOB = "DashboardSet With OOB-" + DashBoardUtils.generateTimeStamp();

		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test testDeleteDashboardSetWithOOBDashboard");

		//Create dashboard set
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbsetName_withOOB, null, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboard set
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_withOOB), "Create dashboard set failed!");

		//Select a dashboard and open it
		webd.getLogger().info("select and open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, OOBAddToSet_VerifyDelete);

		//delete the dashboard set
		webd.getLogger().info("Delete the dashboard set");
		DashboardBuilderUtil.deleteDashboardSet(webd);

		//back to dashboard home page
		webd.getLogger().info("Back to dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//verify the OOB dashboard not in home page
		webd.getLogger().info("Verify the OOB dashboard not in home page");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, OOBAddToSet_VerifyDelete), "The dashboard '" + OOBAddToSet
				+ "' should not in home page!");

	}

	@Test(dependsOnMethods = { "testCreateDashboardSet" })
	public void testModifyDashboardSet()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testModifyDashboardSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//verify the dashboard in home page, verify the dashboard set name and desc displayed in info
		webd.getLogger().info("Verify the dashboard in home page, verify the dashboard set name and desc displayed in info");
		DashboardHomeUtil.search(webd, dbsetName_Simple_CRUD);
		Assert.assertTrue(
				DashBoardUtils.verifyDashboardInfoInHomePage(webd, dbsetName_Simple_CRUD, dbsetDesc_Simple_CRUD, "gridview"),
				"The dashboard set info is not correct");

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Simple_CRUD);

		//edit the dashboardSet
		webd.getLogger().info("Edit the dashboard set");
		dbsetDesc_Simple_CRUD = "Modify the dashboard set description";
		DashboardBuilderUtil.editDashboardSet(webd, dbsetName_Simple_CRUD + "-Modify", dbsetDesc_Simple_CRUD);

		//verify the dashboardSet
		webd.getLogger().info("Verify if the dashboard set has been modified in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_Simple_CRUD + "-Modify"),
				"Dashboard set NOT found!");

	}

	@Test(dependsOnMethods = { "testCreateDashboardWithoutDesc" })
	public void testModifyDashboardSet_AddDesc()
	{

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testModifyDashboardSet_AddDesc");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to list view
		webd.getLogger().info("Switch to the list view");
		DashboardHomeUtil.listView(webd);

		//verify the dashboard in home page, verify the dashboard set name and desc displayed in info
		webd.getLogger().info("Verify the dashboard in home page, verify the dashboard set name and desc displayed in info");
		DashboardHomeUtil.search(webd, dbsetName_Test_NoDesc);
		Assert.assertTrue(DashBoardUtils.verifyDashboardInfoInHomePage(webd, dbsetName_Test_NoDesc, dbsetDesc_Test, "listview"),
				"The dashboard set info is not correct");

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Test_NoDesc);

		//edit the dashboardSet
		webd.getLogger().info("Edit the dashboard set");
		dbsetDesc_Test = "Add description to the dashboard set";
		DashboardBuilderUtil.editDashboardSet(webd, dbsetName_Test_NoDesc + "-Modify", dbsetDesc_Test);

		//verify the dashboardSet
		webd.getLogger().info("Verify if the dashboard set has been modified in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_Test_NoDesc + "-Modify"),
				"Dashboard set NOT found!");

	}

	@Test(dependsOnMethods = { "testSearchDashboardSet" })
	public void testRemoveDashboardSetFromBuilderPage()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testRemoveDashboardSetFromBuilderPage");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard set in builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Simple_CRUD + "-Modify");

		//delete the dashboard set
		webd.getLogger().info("Deleted the dashboard set in builder page");
		DashboardBuilderUtil.deleteDashboardSet(webd);

		//verify if in the home page
		webd.getLogger().info("Verify delete successfully and back to the home page");
		webd.waitForServer();
		webd.waitForElementPresent("css=" + PageId.DASHBOARDDISPLAYPANELCSS);

		//verify if in the dashboar set has been deleted
		webd.getLogger().info("Verify if the dashboard has been deleted");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbsetName_Simple_CRUD + "-Modify"),
				"Dashboard Set NOT removed");
	}

	@Test(dependsOnMethods = { "testModifyDashboardSet_AddDesc" })
	public void testRemoveDashboardSetFromHomePage()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testRemoveDashboardSetFromHomePage");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to the list view
		webd.getLogger().info("Swtich to the list view");
		DashboardHomeUtil.listView(webd);

		//remove the test data
		webd.getLogger().info("Start to delete the dashboard set: " + dbsetName_Test_NoDesc);
		DashboardHomeUtil.deleteDashboard(webd, dbsetName_Test_NoDesc, DashboardHomeUtil.DASHBOARDS_LIST_VIEW);
		webd.getLogger().info("Verify the dashboard set: " + dbsetName_Test_NoDesc + " has been deleted");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbsetName_Test_NoDesc), "Delete dashboard "
				+ dbsetName_Test_NoDesc + " failed!");
		webd.getLogger().info("Delete the dashboard set: " + dbsetName_Test_NoDesc + " finished");

	}

	@Test(dependsOnMethods = { "testModifyDashboardSet" })
	public void testSearchDashboardSet()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testSearchDashboardSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//search dashboard set
		webd.getLogger().info("Start to search dashboard set with previous name");
		DashboardHomeUtil.search(webd, dbsetName_Simple_CRUD);
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbsetName_Simple_CRUD), "The dashboard set named '"
				+ dbsetName_Simple_CRUD + "' should NOT exist!!!");

		//verify the info
		webd.getLogger().info("Verify the dashboard set info");
		DashBoardUtils.verifyDashboardInfoInHomePage(webd, dbsetName_Simple_CRUD, dbsetDesc_Simple_CRUD, "gridview");

		webd.getLogger().info("Start to search dashboard set with name name");
		DashboardHomeUtil.search(webd, dbsetName_Simple_CRUD + "-Modify");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbsetName_Simple_CRUD + "-Modify"),
				"The dashboard set named '" + dbsetName_Simple_CRUD + "-Modify' should exist!!!");

	}
}
