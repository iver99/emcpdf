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

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestDashboardSet_DuplicateDashboard extends LoginAndLogout
{
	private String dbsetName_Duplicate = "";
	private String dbsetName_DuplicateOOB = "";
	private String dbName_InSet = "";
	private String dbName_OutSet = "";
	private String dbName_DuplicateOOB = "";
	private String dbName_Duplicate = "";

	private static String OOBAddToSet = "Database Operations";
	private static String OOBDashboardSet = "Enterprise Health";
	private static String OOBDashboard = "Summary";

	@BeforeClass
	public void createTestData()
	{
		dbsetName_Duplicate = "DashboardSet_Duplicate_Test-" + DashBoardUtils.generateTimeStamp();
		dbsetName_DuplicateOOB = "DashboardSet_DuplicateOOB_Test-" + DashBoardUtils.generateTimeStamp();
		String dbsetDesc = "Test duplicated the dashboard set";

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
		DashboardHomeUtil.createDashboard(webd, dbsetName_Duplicate, dbsetDesc, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_Duplicate), "Dashboard set NOT found!");

		//back to home
		BrandingBarUtil.visitDashboardHome(webd);

		//create another dashboard set
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbsetName_DuplicateOOB, dbsetDesc, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_DuplicateOOB), "Dashboard set NOT found!");

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
		DashBoardUtils.deleteDashboard(webd, dbsetName_Duplicate);
		DashBoardUtils.deleteDashboard(webd, dbsetName_DuplicateOOB);
		BrandingBarUtil.visitDashboardHome(webd);
		webd.waitForServer();
		DashBoardUtils.deleteDashboard(webd, dbName_InSet);
		DashBoardUtils.deleteDashboard(webd, dbName_OutSet);
		DashBoardUtils.deleteDashboard(webd, dbName_OutSet + "-duplicate");
		DashBoardUtils.deleteDashboard(webd, dbName_DuplicateOOB);
		DashBoardUtils.deleteDashboard(webd, dbName_Duplicate);

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	@Test
	public void testDuplicateDashboardAddToSet()
	{
		dbName_InSet = "DashboardInSet-" + DashBoardUtils.generateTimeStamp();
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testDuplicateDashboardAddToSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Duplicate);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		//create a dashboard in the dashboard set
		webd.getLogger().info("create a dashboard in the set");
		DashboardBuilderUtil.createDashboardInsideSet(webd, dbName_InSet, null);

		//verify the dashboard is in the dashboard set
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_InSet);

		webd.waitForServer();
		
		//duplicate the dashboard in set
		webd.getLogger().info("duplicate the dashboard in the dashboard set");
		DashboardBuilderUtil.duplicateDashboardInsideSet(webd, dbName_InSet + "-duplicate", null, true);

		webd.getLogger().info("Verify the duplicated dashboard is in the dashboard set");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_InSet + "-duplicate"),
				"Duplicated dashboard failed!");

		//verify the duplicated dashboard not in home page
		webd.getLogger().info("Verify the duplicated dashboard not in home page");
		BrandingBarUtil.visitDashboardHome(webd);
		webd.waitForServer();
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_InSet + "-duplicate"), "Dashboard: " + dbName_InSet
				+ "-duplicate" + " shoud not be displayed in home page!");

		webd.getLogger().info("Open the dashboard set");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Duplicate);

		//delete the dashboard
		webd.getLogger().info("Delete the duplicate dashboard: " + dbName_InSet + "-duplicate");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, dbName_InSet + "-duplicate");
		DashboardBuilderUtil.deleteDashboardInsideSet(webd);
		webd.getLogger().info(
				"verify the dashboard: " + dbName_InSet + "-duplicate" + "has been deleted, and not in dashboard set as well");
		Assert.assertFalse(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_InSet + "-duplicate"), "The dashboard:"
				+ dbName_InSet + "-duplicate" + " is still in the dashboard set");
		BrandingBarUtil.visitDashboardHome(webd);
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_InSet + "-duplicate"), "Delete dashboard: "
				+ dbName_InSet + "-duplicate" + " failed!");

	}

	@Test
	public void testDuplicateDashboardInOOBDashboardSet()
	{
		dbName_Duplicate = OOBDashboard + "-duplicate-" + DashBoardUtils.generateTimeStamp();

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testDuplicateDashboardInOOBDashboardSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the OOB dashboard set in the builder page");
		DashboardHomeUtil.selectDashboard(webd, OOBDashboardSet);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		//select the OOB dashboard in the set
		webd.getLogger().info("Select the OOB dashboard in the set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, OOBDashboard);

		//verify the edit menu & save icon are not displayed in OOB
		webd.getLogger().info("Verify the save icon is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.DASHBOARDSAVECSS), "Save icon is displayed in OOB");
		webd.waitForServer();

		webd.waitForElementPresent("css=" + PageId.DASHBOARDSETOPTIONS_CSS);
		webd.click("css=" + PageId.DASHBOARDSETOPTIONS_CSS);
		webd.getLogger().info("Verify the edit menu is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css" + PageId.DASHBOARDSETOPTIONSEDIT_CSS), "Edit menu is displayed in OOB");

		webd.getLogger().info("Select the OOB dashboard in the set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, OOBDashboard);

		//duplicate the dashboard in set
		webd.getLogger().info("duplicate the dashboard in the dashboard set");
		DashboardBuilderUtil.duplicateDashboard(webd, dbName_Duplicate, null);

		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_Duplicate, null, true),
				"Duplicate OOB dashboard failed!");

		//back to home page
		webd.getLogger().info("Go to Home page");
		BrandingBarUtil.visitDashboardHome(webd);
		webd.waitForServer();
		//verify the duplicated dashboard in home page
		webd.getLogger().info("Verify the duplicated dashboard in home page");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_Duplicate), "Dashboard: " + dbName_Duplicate
				+ " should be displayed in home page!");

		//open the dashboard set
		webd.getLogger().info("Open the dashboard set: " + OOBDashboardSet);
		DashboardHomeUtil.selectDashboard(webd, OOBDashboardSet);

		//verify the duplicated dashboard
		webd.getLogger().info("Verify the duplicate dashboard: " + dbName_Duplicate + " is not in the dashboard set");
		Assert.assertFalse(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_Duplicate),
				"Dashboard has been duplicated and add to dashboard set");

	}

	@Test(dependsOnMethods = { "testDuplicateDashboardAddToSet" })
	public void testDuplicateDashboardNotAddToSet()
	{
		dbName_OutSet = "DashboardOutSet-" + DashBoardUtils.generateTimeStamp();
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testDuplicateDashboardNotAddToSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Duplicate);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		//create a dashboard in the dashboard set
		webd.getLogger().info("create a dashboard in the set");
		DashboardBuilderUtil.createDashboardInsideSet(webd, dbName_OutSet, null);

		webd.waitForServer();

		//verify the dashboard is in the dashboard set
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_OutSet), "Create dashboard in set failed!");

		//duplicate the dashboard in set
		webd.getLogger().info("duplicate the dashboard in the dashboard set");
		DashboardBuilderUtil.duplicateDashboardInsideSet(webd, dbName_OutSet + "-duplicate", null, false);

		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_OutSet + "-duplicate", null, true),
				"Duplicate dashboard not add to set failed!");

		//back to home page
		webd.getLogger().info("Go to Home page");
		BrandingBarUtil.visitDashboardHome(webd);
		webd.waitForServer();
		webd.getLogger().info("Verify the dashboard is displayed in home page");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_OutSet + "-duplicate"), "Dashboard: " + dbName_OutSet
				+ "-duplicate" + " should be displayed in home page!");

		//open the dashboard set
		webd.getLogger().info("Open the dashboard set: " + dbsetName_Duplicate);
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Duplicate);

		//verify the duplicated dashboard
		webd.getLogger().info("Verify the duplicate dashboard: " + dbName_OutSet + " is not in the dashboard set");
		Assert.assertFalse(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_OutSet + "-duplicate"),
				"Dashboard has been duplicated and add to dashboard set");
	}

	@Test(dependsOnMethods = { "testDuplicateDashboardNotAddToSet" })
	public void testDuplicateOOBAddToSet()
	{
		dbName_DuplicateOOB = "OOBDashboard-duplicate-" + DashBoardUtils.generateTimeStamp();

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testDuplicateOOBAddToSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_DuplicateOOB);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		//add an OOB dashboard in the dashboard set
		webd.getLogger().info("Add an OOB dashboard in the set");
		DashboardBuilderUtil.addNewDashboardToSet(webd, OOBAddToSet);

		webd.waitForServer();

		//verify the dashboard is in the dashboard set
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, OOBAddToSet),
				"The OOB dashboard is not added into dashboard set");

		//duplicate the dashboard in set
		webd.getLogger().info("duplicate the dashboard in the dashboard set");
		DashboardBuilderUtil.duplicateDashboardInsideSet(webd, dbName_DuplicateOOB, null, true);

		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_DuplicateOOB),
				"Duplicate OOB dashboard failed!");

		//verify the duplicated dashboard not in home page
		webd.getLogger().info("Verify the duplicated dashboard is in home page");
		BrandingBarUtil.visitDashboardHome(webd);
		webd.waitForServer();
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_DuplicateOOB), "Dashboard: " + dbName_DuplicateOOB
				+ " should be displayed in home page!");

		webd.getLogger().info("Open the dashboard set");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_DuplicateOOB);

		//delete the dashboard
		webd.getLogger().info("Delete the duplicate dashboard: " + dbName_DuplicateOOB);
		DashboardBuilderUtil.selectDashboardInsideSet(webd, dbName_DuplicateOOB);
		DashboardBuilderUtil.deleteDashboardInsideSet(webd);
		webd.getLogger().info(
				"verify the dashboard: " + dbName_DuplicateOOB + "has been deleted, and not in dashboard set as well");
		Assert.assertFalse(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_DuplicateOOB), "The dashboard:"
				+ dbName_DuplicateOOB + " is still in the dashboard set");
		BrandingBarUtil.visitDashboardHome(webd);
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_DuplicateOOB), "Delete dashboard: "
				+ dbName_DuplicateOOB + " failed!");

	}

	@Test(dependsOnMethods = { "testDuplicateOOBAddToSet" })
	public void testDuplicateOOBNotAddToSet()
	{
		dbName_DuplicateOOB = "OOBDashboard-duplicate-" + DashBoardUtils.generateTimeStamp();

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testDuplicateOOBNotAddToSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_DuplicateOOB);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		//select the OOB dashboard in the set
		webd.getLogger().info("Select the OOB dashboard in the set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, OOBAddToSet);

		//verify the edit menu & save icon are not displayed in OOB
		webd.getLogger().info("Verify the save icon is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.DASHBOARDSAVECSS), "Save icon is displayed in OOB");
		webd.waitForServer();

		webd.waitForElementPresent("css=" + PageId.DASHBOARDSETOPTIONS_CSS);
		webd.click("css=" + PageId.DASHBOARDSETOPTIONS_CSS);
		webd.getLogger().info("Verify the edit menu is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css" + PageId.DASHBOARDSETOPTIONSEDIT_CSS), "Edit menu is displayed in OOB");

		webd.getLogger().info("Select the OOB dashboard in the set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, OOBAddToSet);

		//duplicate the dashboard in set
		webd.getLogger().info("duplicate the dashboard in the dashboard set");
		DashboardBuilderUtil.duplicateDashboardInsideSet(webd, dbName_DuplicateOOB, null, false);

		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_DuplicateOOB, null, true),
				"Duplicate OOB dashboard failed!");

		//back to home page
		webd.getLogger().info("Go to Home page");
		BrandingBarUtil.visitDashboardHome(webd);
		webd.waitForServer();
		//verify the duplicated dashboard in home page
		webd.getLogger().info("Verify the duplicated dashboard in home page");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_DuplicateOOB), "Dashboard: " + dbName_DuplicateOOB
				+ " should be displayed in home page!");

		//open the dashboard set
		webd.getLogger().info("Open the dashboard set: " + dbsetName_DuplicateOOB);
		DashboardHomeUtil.selectDashboard(webd, dbsetName_DuplicateOOB);

		//verify the duplicated dashboard
		webd.getLogger().info("Verify the duplicate dashboard: " + dbName_DuplicateOOB + " is not in the dashboard set");
		Assert.assertFalse(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_DuplicateOOB),
				"Dashboard has been duplicated and add to dashboard set");
	}
}
