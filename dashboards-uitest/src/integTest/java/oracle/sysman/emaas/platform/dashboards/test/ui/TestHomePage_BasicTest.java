package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashboardPageId_1230;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestHomePage_BasicTest extends LoginAndLogout
{
	private String dbName = "";
	private String dbSetName = "";
	private String dbSetName_2 = "";

	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);

		//reset all the checkboxes
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@BeforeClass
	public void createTestData()
	{
		dbName = "Test Dashboaard for Filter - "+ DashBoardUtils.generateTimeStamp();
		dbSetName = "Test Dashboard set for Filter - " + DashBoardUtils.generateTimeStamp();
		dbSetName_2 = "Test Dashboard set with empty dashboard - " + DashBoardUtils.generateTimeStamp();

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to create test data for test");

		//create a dashboard
		webd.getLogger().info("Create a dashboard without any widget");
		DashboardHomeUtil.createDashboard(webd, dbName, "", DashboardHomeUtil.DASHBOARD);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName, "", true),"Failed to create dashboard");

		webd.getLogger().info("back to home page and create a dashboard set without dashboard included");
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.createDashboard(webd, dbSetName, "", DashboardHomeUtil.DASHBOARDSET);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbSetName),"Failed to create dashboard set");

		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.createDashboard(webd, dbSetName_2, "", DashboardHomeUtil.DASHBOARDSET);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbSetName_2),"Failed to create dashboard set");
		DashboardBuilderUtil.addNewDashboardToSet(webd, dbName);

		LoginAndLogout.logoutMethod();
	}

	@AfterClass
	public void removeDashboard()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to remove the test data for test");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//delete the test data
		webd.getLogger().info("Delete the test data");
		DashBoardUtils.deleteDashboard(webd, dbSetName);
		DashBoardUtils.deleteDashboard(webd, dbSetName_2);
		DashBoardUtils.deleteDashboard(webd, dbName);

		webd.getLogger().info("All test data have been removed");

		//logout
		LoginAndLogout.logoutMethod();
	}

	@Test(alwaysRun = true)
	public void testUserMenu()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testUserMenu");

		BrandingBarUtil.userMenuOptions(webd, BrandingBarUtil.USERMENU_OPTION_ABOUT);
	}
	@Test(alwaysRun = true)
	public void testUserMenuItem(){
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testUserMenu");
		BrandingBarUtil.verifyUserMenuItemByTest(webd, DashboardPageId_1230.BRANDINGBARUSERMENUITEMHELP, "Help");
		BrandingBarUtil.verifyUserMenuItemByTest(webd, DashboardPageId_1230.BRANDINGBARUSERMENUITEMABOUT, "About");
		BrandingBarUtil.verifyUserMenuItemByTest(webd, DashboardPageId_1230.BRANDINGBARUSERMENUITEMLOGOUT, "Sign Out");
	}

	@Test(alwaysRun = true)
	public void verify_allOOB_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_allOOB_GridView");

		//switch to list view
		webd.getLogger().info("Switch to Grid View");
		DashboardHomeUtil.gridView(webd);

		//verify all the oob display
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.udeOobExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);
		DashBoardUtils.securityOobExist(webd);

		//verify below oob dashboards not displayed in the home page
		DashBoardUtils.outDateOob(webd);

		//verify below oob dashboards not displayed in the home page, due to EMCPDF-4327
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExist_v2v3(webd);
	}

	@Test(alwaysRun = true)
	public void verify_allOOB_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_allOOB_ListView");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//verify all the oob display
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.udeOobExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);
		DashBoardUtils.securityOobExist(webd);

		//verify below oob dashboards not displayed in the home page
		DashBoardUtils.outDateOob(webd);

		//verify below oob dashboards not displayed in the home page, due to EMCPDF-4327
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExist_v2v3(webd);
	}

	@Test(alwaysRun = true)
	public void verify_CreatedBy_Me_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_CreatedBy_Me_GridView");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//click Created By Oracle checkbox
		webd.getLogger().info("select Created By as Me");
		DashboardHomeUtil.filterOptions(webd, "me");

		//verify all the oob not exsit
		DashBoardUtils.noOOBCheck(webd);

		//verify created dashboard & dashboard set displayed
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbSetName));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbSetName_2));

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_CreatedBy_Me_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_CreatedBy_Me_ListView");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		//click Created By Oracle checkbox
		webd.getLogger().info("select Created By as Me");
		DashboardHomeUtil.filterOptions(webd, "me");

		//verify all the oob not exsit
		DashBoardUtils.noOOBCheck(webd);

		//verify created dashboard & dashboard set displayed
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbSetName));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbSetName_2));

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_CreatedBy_Oracle_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_CreatedBy_Oracle_GridView");

		//switch to list view
		webd.getLogger().info("Switch to Grid View");
		DashboardHomeUtil.gridView(webd);

		//click Created By Oracle checkbox
		webd.getLogger().info("select Created By as Oracle");
		DashboardHomeUtil.filterOptions(webd, "oracle");

		//verify all the oob display
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.udeOobExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);
		DashBoardUtils.securityOobExist(webd);

		//verify below oob dashboards not displayed in the home page
		DashBoardUtils.outDateOob(webd);

		//verify below oob dashboards not displayed in the home page, due to EMCPDF-4327
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExist_v2v3(webd);

		//verify created dashboard not displayed
		verifyCreatedDashboardNotExisted();

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_CreatedBy_Oracle_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_CreatedBy_Oracle_ListView");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//click Created By Oracle checkbox
		webd.getLogger().info("select Created By as Oracle");
		DashboardHomeUtil.filterOptions(webd, "oracle");

		//verify all the oob display
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.udeOobExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);
		DashBoardUtils.securityOobExist(webd);

		//verify below oob dashboards not displayed in the home page
		DashBoardUtils.outDateOob(webd);

		//verify below oob dashboards not displayed in the home page, due to EMCPDF-4327
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExist_v2v3(webd);

		//verify created dashboard not displayed
		verifyCreatedDashboardNotExisted();

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_filterby_relogin()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_filterby_relogin");

		//reset all filter options
		DashboardHomeUtil.resetFilterOptions(webd);

		//check ita box
		DashboardHomeUtil.filterOptions(webd, "favorites");

		//signout menu
		webd.click(PageId.MENUBTNID);
		webd.click(PageId.SIGNOUTID);

		login(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		//check ita box
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "favorites"));
		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	private void verifyCreatedDashboardNotExisted()
	{		//verify created dashboard & dashboard set displayed
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbSetName));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbSetName_2));
	}
}
