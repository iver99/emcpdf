package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;

import org.openqa.selenium.StaleElementReferenceException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class TestDashboard_NegativeCases extends LoginAndLogout
{
	private String dbName = "";
	private String dbName_2 = "";
	private String dbSetName = "";
	private final String dbDesc = "Test the nagetive case";

	@BeforeClass
	public void createTestData()
	{
		dbName = "Dashboard Test Negative Case-" + DashBoardUtils.generateTimeStamp();

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
		webd.getLogger().info("Create a new dashboard");
		DashboardHomeUtil.createDashboard(webd, dbName, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName, dbDesc, true), "Dashboard set NOT found!");

		//		//back to home page and continue to create dashboard
		//		webd.getLogger().info("Back to home page and continue to create dashboard");
		//		BrandingBarUtil.visitDashboardHome(webd);
		//
		//		//create dashboardset
		//		webd.getLogger().info("Create a new dashboard");
		//		DashboardHomeUtil.createDashboard(webd, dbName_2, dbDesc, DashboardHomeUtil.DASHBOARD);
		//
		//		//verify the dashboardset
		//		webd.getLogger().info("Verify if the dashboard existed in builder page");
		//		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_2, dbDesc, true), "Dashboard set NOT found!");

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
		webd.getLogger().info("Start to remove the test data...");

		DashBoardUtils.deleteDashboard(webd, dbName);

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	@Test(alwaysRun = true)
	public void testCreateDashboardSetWithSameNameDesc()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testCreateDashboardWithSameNameDesc");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: no description, with time refresh");
		DashboardHomeUtil.createDashboardSet(webd, dbName, dbDesc);

		//verify the dashboard set can not be created successfully
		webd.getLogger().info("Verify the dashboard set not created successfuly, the error message displayed");
		if (webd.isDisplayed("css=" + PageId.DASHBOARD_HOME_ERRMSG_CSS)) {
			Assert.assertTrue(webd.isDisplayed("css=" + PageId.DASHBOARD_HOME_ERRMSG_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.DASHBOARD_HOME_ERRMSG_CSS),
					"Name already exists.Provide a unique name.");
			 webd.click("css="+PageId.DASHBOARD_CREATE_DIALOG_CANCEL_CSS);
		}
		else {
			Assert.fail("Test: testCreateDashboardWithSameNameDesc failed due to dashbord created successfully!");
		}
	}

	@Test(alwaysRun = true)
	public void testCreateDashboardSetWithSameNameDiffDesc()
	{
		String dbNewDesc = "dashboard set with different description";
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testCreateDashboardWithSameNameDiffDesc");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: no description, with time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName, dbNewDesc, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboard can be created successfully
		webd.getLogger().info("Verify the dashboard can be created successfuly");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbName), "Not create dashboard successfully!");
	}

	@Test(alwaysRun = true)
	public void testCreateDashboardWithSameNameDesc()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testCreateDashboardWithSameNameDesc");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: no description, with time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify the dashboard can not be created successfully
		webd.getLogger().info("Verify the dashboard not created successfuly, the error message displayed");
		if (webd.isDisplayed("css=" + PageId.DASHBOARD_HOME_ERRMSG_CSS)) {
			Assert.assertTrue(webd.isDisplayed("css=" + PageId.DASHBOARD_HOME_ERRMSG_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.DASHBOARD_HOME_ERRMSG_CSS),
					"Name already exists.Provide a unique name.");
			webd.click("css="+PageId.DASHBOARD_CREATE_DIALOG_CANCEL_CSS);
		}
		else {
			Assert.fail("Test: testCreateDashboardWithSameNameDesc failed due to dashbord created successfully!");
		}
	}

	@Test(alwaysRun = true)
	public void testCreateDashboardWithSameNameDiffDesc()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testCreateDashboardWithSameNameDiffDesc");
		String dbNewDesc = "different description";

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: no description, with time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName, dbNewDesc, DashboardHomeUtil.DASHBOARD);

		//verify the dashboard can be created successfully
		webd.getLogger().info("Verify the dashboard can be created successfuly");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName, dbNewDesc, true),
				"Not create dashboard successfully!");
	}

	@Test(alwaysRun = true)
	public void testModifyDashboardSetWithSameNameDesc()
	{
		dbSetName = "Dashboard Set Same Name-" + DashBoardUtils.generateTimeStamp();
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testModifyDashboardSetWithSameNameDesc");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboardset
		webd.getLogger().info("Create a new dashboard");
		DashboardHomeUtil.createDashboard(webd, dbSetName, dbDesc, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbSetName), "Dashboard set NOT found!");

		//modify the dashboard
		webd.getLogger().info("Modify the dashboard");
		DashboardBuilderUtil.editDashboardSet(webd, dbName, dbDesc);

		//verify the dashboard can't be modified successfully
		webd.getLogger().info("Verify the dashboard set not modified successfuly, the error message displayed");
		if (webd.isDisplayed("css=" + PageId.DASHBOARD_BUILDER_ERRMSG_CSS)) {
			Assert.assertTrue(webd.isDisplayed("css=" + PageId.DASHBOARD_BUILDER_ERRMSG_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.DASHBOARD_BUILDER_ERRMSG_CSS),
					"Name already exists. Provide a unique name.");
		}
		else {
			Assert.fail("Test: testModifyDashboardSetWithSameNameDesc failed due to dashbord set created successfully!");
		}

	}

	@Test(dependsOnMethods = { "testModifyDashboardSetWithSameNameDesc" })
	public void testModifyDashboardSetWithSameNameDiffDesc()
	{
		String dbDesc_New = "a differtent description to modify the dashboard set";

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testModifyDashboardSetWithSameNameDiffDesc");

		//edit dashboard
		webd.getLogger().info("Start to edit dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		webd.getLogger().info("Search dashboard");
		DashboardHomeUtil.search(webd, dbSetName);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbSetName), "Dashboard NOT found!");

		//Open the dashboard
		webd.getLogger().info("Open the dashboard to edit name and description");
		DashboardHomeUtil.selectDashboard(webd, dbSetName);

		//edit the dashboard
		webd.getLogger().info("Edit the dashboard's name and description, display the description");
		DashboardBuilderUtil.editDashboardSet(webd, dbName, dbDesc_New);

		//Verify the dashboard edited successfully
		webd.getLogger().info("Verify the dashboard edited successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbName), "Dashboard edit failed!");
	}

	@Test(alwaysRun = true)
	public void testModifyDashboardWithSameNameDesc() throws InterruptedException
	{
		String errormsg="";
		dbName_2 = "Dashboard Same Name-" + DashBoardUtils.generateTimeStamp();
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testModifyDashboardWithSameNameDesc");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboardset
		webd.getLogger().info("Create a new dashboard");
		DashboardHomeUtil.createDashboard(webd, dbName_2, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_2, dbDesc, true), "Dashboard set NOT found!");

		//modify the dashboard
		webd.getLogger().info("Modify the dashboard");
		DashboardBuilderUtil.editDashboard(webd, dbName, dbDesc, false);

		//verify the dashboard can't be modified successfully
		webd.getLogger().info("Verify the dashboard set not modified successfuly, the error message displayed");
		if (webd.isDisplayed("css=" + PageId.DASHBOARD_BUILDER_ERRMSG_CSS)) {
			try{
				errormsg = webd.getText("css=" + PageId.DASHBOARD_BUILDER_ERRMSG_CSS);
			}
			catch(StaleElementReferenceException ex){
				wait(1000);
				webd.getLogger().info("having StaleElementReferenceException, then need to wait 1 sec and retry");
				errormsg = webd.getText("css=" + PageId.DASHBOARD_BUILDER_ERRMSG_CSS);				
			}
			finally{
				Assert.assertEquals(errormsg, "Name already exists. Provide a unique name.");
			}			
		}
		else {
			Assert.fail("Test: testModifyDashboardWithSameNameDesc failed due to dashbord created successfully!");
		}

	}

	@Test(dependsOnMethods = { "testModifyDashboardWithSameNameDesc" })
	public void testModifyDashboardWithSameNameDiffDesc()
	{
		String dbDesc_New = "a differtent description to modify the dashboard";

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testModifyDashboardWithSameNameDiffDesc");

		//edit dashboard
		webd.getLogger().info("Start to edit dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		webd.getLogger().info("Search dashboard");
		DashboardHomeUtil.search(webd, dbName_2);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_2), "Dashboard NOT found!");

		//Open the dashboard
		webd.getLogger().info("Open the dashboard to edit name and description");
		DashboardHomeUtil.selectDashboard(webd, dbName_2);

		//edit the dashboard
		webd.getLogger().info("Edit the dashboard's name and description, display the description");
		DashboardBuilderUtil.editDashboard(webd, dbName, dbDesc_New, false);

		//Verify the dashboard edited successfully
		webd.getLogger().info("Verify the dashboard edited successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName, dbDesc_New, true), "Dashboard edit failed!");
	}

}
