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
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestDashboardSet_DashboardRelated_CreatedInsideSet extends LoginAndLogout
{
	private String dbsetName_Test1 = "";
	private String dbsetName_Test2 = "";
	private String dbName_InSet1 = "";
	private String dbName_InSet2 = "";

	private static String OOBAddToSet = "Database Operations";

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

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

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
		DashBoardUtils.deleteDashboard(webd, dbName_InSet1);
		DashBoardUtils.deleteDashboard(webd, dbName_InSet2);

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	@Test
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

	@Test
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
}
