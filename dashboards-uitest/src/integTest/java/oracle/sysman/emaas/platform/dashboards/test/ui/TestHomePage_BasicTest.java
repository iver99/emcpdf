package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
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
	public void testExploreData_LALink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testExploreData_LALink");

		DashboardHomeUtil.gotoDataExplorer(webd, DashboardHomeUtil.EXPLOREDATA_MENU_LOG);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emlacore/html/log-analytics-search.html");
	}

	@Test(alwaysRun = true)
	public void testExploreData_SearchLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testExploreData_SearchLink");

		DashboardHomeUtil.gotoDataExplorer(webd, DashboardHomeUtil.EXPLOREDATA_MENU_SEARCH);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcta/ta/analytics.html");
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
		DashBoardUtils.apmOobExist(webd);
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);
		DashBoardUtils.securityOobExist(webd);
		DashBoardUtils.outDateOob(webd);
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
		DashBoardUtils.apmOobExist(webd);
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);
		DashBoardUtils.securityOobExist(webd);
		DashBoardUtils.outDateOob(webd);
	}

	@Test(alwaysRun = true)
	public void verify_APMOOB_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_APMOOB_GridView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as APM");
		DashboardHomeUtil.filterOptions(webd, "apm");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//verify APM oob display
		DashBoardUtils.apmOobExist(webd);
		DashBoardUtils.itaOobNotExist(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_APMOOB_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_APMOOB_ListView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as APM");
		DashboardHomeUtil.filterOptions(webd, "apm");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		//verify APM oob display
		DashBoardUtils.apmOobExist(webd);
		DashBoardUtils.itaOobNotExist(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
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
		DashBoardUtils.apmOobExist(webd);
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);
		DashBoardUtils.securityOobExist(webd);
		DashBoardUtils.outDateOob(webd);

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
		DashBoardUtils.apmOobExist(webd);
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);
		DashBoardUtils.securityOobExist(webd);
		DashBoardUtils.outDateOob(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_ITAOOB_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_ITAOOB_GridView");

		//select Cloud Services as IT Analytics
		webd.getLogger().info("select Cloud Services as IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//verify ITA oob display
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.outDateOob(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_ITAOOB_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_ITAOOB_GridView");

		//select Cloud Services as IT Analytics
		webd.getLogger().info("select Cloud Services as IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		//verify ITA oob display
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.outDateOob(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_LAOOB_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_LAOOB_GridView");

		//select Cloud Services as Log Analytics
		webd.getLogger().info("select Cloud Services as Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//verify LA oob display
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.outDateOob(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_LAOOB_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_LAOOB_GridView");

		//select Cloud Services as Log Analytics
		webd.getLogger().info("select Cloud Services as Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		//verify LA oob display
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.outDateOob(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_OrchestrationOOB_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_OrchestrationOOB_GridView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as Orchestration");
		DashboardHomeUtil.filterOptions(webd, "orchestration");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//verify Orchestration oob display
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExist(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_OrchestrationOOB_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_OrchestrationOOB_ListView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as Orchestration");
		DashboardHomeUtil.filterOptions(webd, "orchestration");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		//verify Orchestration oob display
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExist(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_SecurityOOB_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_SecurityOOB_GridView");

		//select Cloud Services as Security
		webd.getLogger().info("select Cloud Services as Security");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//verify security oob display
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExist(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobExist(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_SecurityOOB_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_SecurityOOB_ListView");

		//select Cloud Services as Security
		webd.getLogger().info("select Cloud Services as Security");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		//verify APM oob display
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExist(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobExist(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_EmptyDashboard_Filter_APM_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_EmptyDashboard_Filter_APM_GridView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as APM");
		DashboardHomeUtil.filterOptions(webd, "apm");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Verify the created dashboard and dashboard set can be filtered by APM");
		verifyCustomDashboard();

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_EmptyDashboard_Filter_APM_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_EmptyDashboard_Filter_APM_ListView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as APM");
		DashboardHomeUtil.filterOptions(webd, "apm");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		webd.getLogger().info("Verify the created dashboard and dashboard set can be filtered by APM");
		verifyCustomDashboard();

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_EmptyDashboard_Filter_ITA_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_EmptyDashboard_Filter_ITA_GridView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as ITA");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Verify the created dashboard and dashboard set can be filtered by ITA");
		verifyCustomDashboard();

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_EmptyDashboard_Filter_ITA_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_EmptyDashboard_Filter_ITA_ListView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as ITA");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		webd.getLogger().info("Verify the created dashboard and dashboard set can be filtered by ITA");
		verifyCustomDashboard();

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_EmptyDashboard_Filter_LA_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_EmptyDashboard_Filter_LA_GridView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as LA");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Verify the created dashboard and dashboard set can be filtered by LA");
		verifyCustomDashboard();

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_EmptyDashboard_Filter_LA_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_EmptyDashboard_Filter_LA_ListView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as LA");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		webd.getLogger().info("Verify the created dashboard and dashboard set can be filtered by LA");
		verifyCustomDashboard();

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_EmptyDashboard_Filter_Orchestration_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_EmptyDashboard_Filter_Orchestration_GridView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as Orchestration");
		DashboardHomeUtil.filterOptions(webd, "orchestration");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Verify the created dashboard and dashboard set can be filtered by Orchestration");
		verifyCustomDashboard();

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_EmptyDashboard_Filter_Orchestration_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_EmptyDashboard_Filter_Orchestration_ListView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as Orchestration");
		DashboardHomeUtil.filterOptions(webd, "orchestration");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		webd.getLogger().info("Verify the created dashboard and dashboard set can be filtered by Orchestration");
		verifyCustomDashboard();

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_EmptyDashboard_Filter_Security_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_EmptyDashboard_Filter_Security_GridView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as Security");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Verify the created dashboard and dashboard set can be filtered by Security");
		verifyCustomDashboard();

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_EmptyDashboard_Filter_Security_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_EmptyDashboard_Filter_Security_ListView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as Security");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		webd.getLogger().info("Verify the created dashboard and dashboard set can be filtered by Security");
		verifyCustomDashboard();

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	private void verifyCustomDashboard()
	{
		webd.getLogger().info("Verify the dashboard");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName));
		webd.getLogger().info("Verify the dashboard set");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbSetName));
		webd.getLogger().info("Verify the dashboard set with empty dashboard");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbSetName_2));
	}
}