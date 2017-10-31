package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class TestDashBoard_SetHome extends LoginAndLogout
{
	private String dbName_setHome = "";

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
		DashBoardUtils.deleteDashboard(webd, dbName_setHome);

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	@Test(dependsOnMethods = { "testDeleteHomeDashboard" })
	public void testAfterHomeDashboardRemoved()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testAfterHomeDashboardRemoved");
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the current page is home page
		webd.getLogger().info("Verify the error page displayed");
		String url = webd.getCurrentUrl();
		webd.getLogger().info("current url = " + url);
		if (!url.substring(url.indexOf("emsaasui") + 9).contains("home.html")) {
			Assert.fail("not open the dashboard home page");
		}
	}

	@Test
	public void testDeleteHomeDashboard()
	{
		String dbHomeDashboard = "HomeDashboard-" + DashBoardUtils.generateTimeStamp();
		String dbDesc = "Set the dashboard as home";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testDeleteHomeDashboard");
		WaitUtil.waitForPageFullyLoaded(webd);
		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create a dashboard & set it as home
		webd.getLogger().info("create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbHomeDashboard, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbHomeDashboard, dbDesc, true), "Create dashboard failed!");

		webd.getLogger().info("Set home page");
		Assert.assertTrue(DashboardBuilderUtil.toggleHome(webd), "Set the dasbhoard as Home failed!");

		//go to the home page & verify the home page
		webd.getLogger().info("Access to the home page");
		BrandingBarUtil.visitMyHome(webd);
		webd.getLogger().info("Verfiy the home page");		
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbHomeDashboard, dbDesc, true), "It is NOT the home page!");

		//delete the dashboard
		webd.getLogger().info("Delete the dashboard");
		DashboardBuilderUtil.deleteDashboard(webd);

		//go to the home page
		webd.getLogger().info("Access to the home page");
		BrandingBarUtil.visitMyHome(webd);

		webd.getLogger().info("Verify back to the dashboard home page");
		String url = webd.getCurrentUrl();
		webd.getLogger().info("current url = " + url);
		if (!url.substring(url.indexOf("emsaasui") + 9).contains("welcome.html")) {
			Assert.fail("not open the dashboard home page");
		}
		else {
			//TODO
			//verify the warn info displayed
			//info: The dashboard you set as your Home page has been deleted. You may want to choose a new Home page.
		}
	}

	@Test(dependsOnMethods = { "testAfterHomeDashboardRemoved" })
	public void testSetHome()
	{
		dbName_setHome = "setHomeDashboard-" + DashBoardUtils.generateTimeStamp();
		String dbDesc = "SetHome_testDashboard desc";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testSetHome");
		WaitUtil.waitForPageFullyLoaded(webd);
		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_setHome, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_setHome, dbDesc, true), "Create dashboard failed!");

		//set it as home
		webd.getLogger().info("Set home page");
		Assert.assertTrue(DashboardBuilderUtil.toggleHome(webd), "Set the dasbhoard as Home failed!");

		//check home page
		webd.getLogger().info("Access to the home page");
		BrandingBarUtil.visitMyHome(webd);
		webd.getLogger().info("Verfiy the home page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_setHome, dbDesc, true), "It is NOT the home page!");

		//set it not home
		webd.getLogger().info("Set not home page");
		Assert.assertFalse(DashboardBuilderUtil.toggleHome(webd), "Remove the dasbhoard as Home failed!");

		//check home page
		webd.getLogger().info("Access to the home page");
		BrandingBarUtil.visitMyHome(webd);
		webd.getLogger().info("Verfiy the home page");
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, WelcomeUtil.SERVICE_NAME_DASHBOARDS),
				"It is NOT the home page!");
	}
}
