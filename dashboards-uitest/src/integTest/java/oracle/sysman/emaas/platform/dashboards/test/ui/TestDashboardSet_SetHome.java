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
import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestDashboardSet_SetHome extends LoginAndLogout
{
	private String dbsetName_setHome = "";

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
		DashBoardUtils.deleteDashboard(webd, dbsetName_setHome);

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	@Test(dependsOnMethods = { "testSetHome" })
	public void testRemoveHome()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testRemoveHome");

		//verify is in the builder page
		webd.getLogger().info("Start the test case: testSetHome");
		String url = webd.getCurrentUrl();
		webd.getLogger().info("current url = " + url);
		if (!url.substring(url.indexOf("emsaasui") + 9).contains("builder.html?dashboardId=")) {
			Assert.fail("not open the builder page");
		}
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_setHome), "Not the correct home page");

		//remove the home page
		DashboardBuilderUtil.toggleHomeDashboardSet(webd);
		//verify the home page
		webd.getLogger().info("Go to home page and verify if the dashboard set is home page");
		BrandingBarUtil.visitMyHome(webd);
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, WelcomeUtil.SERVICE_NAME_DASHBOARDS),
				"It is NOT the home page!");
	}

	@Test
	public void testSetHome()
	{
		dbsetName_setHome = "DashboardSet_TestSetHome-" + DashBoardUtils.generateTimeStamp();
		String dbsetDesc = "Test the dashboardset set home";

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testSetHome");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboardset
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbsetName_setHome, dbsetDesc, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_setHome), "Dashboard set NOT found!");

		//set the dashboard as home
		webd.getLogger().info("Set the created dashborad set as Home");
		DashboardBuilderUtil.toggleHomeDashboardSet(webd);

		//verify the home page
		webd.getLogger().info("Go to home page and verify if the dashboard set is home page");
		BrandingBarUtil.visitMyHome(webd);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_setHome), "DashboarSet is NOT set to home page");
	}
}
