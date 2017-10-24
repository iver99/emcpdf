package oracle.sysman.emaas.platform.dashboards.test.ui;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sun.font.TrueTypeFont;

public class TestHomePage_SortBy extends LoginAndLogout
{
	private String dbName_lastAccessAsc = "";
	private String dbName_lastAccessDsc = "";
	private String dbName_SortDefault = "";

	@BeforeClass
	public void createDashboard()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to create a dashboard for test");

		//create a dashboard
		DashboardHomeUtil.createDashboard(webd, "ADashboard Test", "", DashboardHomeUtil.DASHBOARD);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, "ADashboard Test", "", true),"Failed to create dashboard");

		LoginAndLogout.logoutMethod();
	}

	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);

		//reset all the checkboxes
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@AfterClass
	public void removeDashboard()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to create a dashboard for test");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//delete the test data
		webd.getLogger().info("Delete the test data");
		DashBoardUtils.deleteDashboard(webd, "ADashboard Test");
		DashBoardUtils.deleteDashboard(webd, dbName_lastAccessAsc);
		DashBoardUtils.deleteDashboard(webd, dbName_lastAccessDsc);
		DashBoardUtils.deleteDashboard(webd, dbName_SortDefault);

		webd.getLogger().info("All test data have been removed");

		//logout
		LoginAndLogout.logoutMethod();
	}

	@Test(alwaysRun = true)
	public void TestSortBy_createdBy_ListView()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with created by in list view");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//verify the sort by is Default
		Assert.assertEquals(webd.getText(PageId.SORTBYID).trim(), "Default");

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by created by Ascending");
		DashboardHomeUtil.sortListViewByCreateBy(webd);

		//WaitUtil.waitForPageFullyLoaded(webd);

		//verify the result
		webd.getLogger().info("Verify the sort result -- Ascending");
		Assert.assertEquals(webd.getText(PageId.SORTBYID).trim(), "Created By Ascending");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");

		//webd.waitForElementPresent(PageId.ADashboardTestByAriaLabel);
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), "ADashboard Test");

		//sort the dashboard by name Descending
		webd.getLogger().info("Sort the dashboard by created by Descending");
		DashboardHomeUtil.sortListViewByCreateBy(webd);

		//verify the result
		webd.getLogger().info("Verify the sort result -- Descending");
		Assert.assertEquals(webd.getText(PageId.SORTBYID).trim(), "Created By Descending");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");

		//webd.waitForElementPresent(PageId.ADashboardTestByAriaLabel);
		names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 1), "ADashboard Test");
		webd.getLogger().info("The last dashboard sorted by 'Created By Decending' is " + names.get(names.size() - 1));

	}

	@Test(alwaysRun = true)
	public void testSortBy_createdByAscending()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with created by ascendingt");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by created by Ascending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_OWNER_ASC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");

		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), "ADashboard Test");
	}

	@Test(alwaysRun = true)
	public void testSortBy_createdByDecending()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with created by decending");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by created by Decending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_OWNER_DSC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 1), "ADashboard Test");
		webd.getLogger().info("The last dashboard sorted by 'Created By Decending' is " + names.get(names.size() - 1));
	}

	@Test(alwaysRun = true)
	public void testSortBy_creationDateAscending()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with creation date by ascendingt");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by creation date by Ascending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_ASC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		DashboardHomeUtil.waitForDashboardPresent(webd, "Performance Analytics: Database");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), "Performance Analytics: Database");
		webd.getLogger().info("The first dashboard sorted by 'Creation Date Ascending' is " + names.get(0));
	}

	@Test(alwaysRun = true)
	public void testSortBy_creationDateDecending()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with creation date by decending");

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by creation date by Decending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_DSC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		DashboardHomeUtil.waitForDashboardPresent(webd, "Performance Analytics: Database");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 1), "Performance Analytics: Database");
		webd.getLogger().info("The first dashboard sorted by 'Creation Date Ascending' is " + names.get(names.size() - 1));
	}

	@Test(alwaysRun = true)
	public void testSortBy_lastAccessedAscending()
	{
		dbName_lastAccessAsc = "Sort Last Access Asc - " + DashBoardUtils.generateTimeStamp();

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with last accessed by ascendingt");

		//switch to grid view
		webd.getLogger().info("Switch to Grid View");
		DashboardHomeUtil.gridView(webd);

		//create a dashboard
		webd.getLogger().info("Create a dashboard");
		DashboardHomeUtil.createDashboard(webd, dbName_lastAccessAsc, "", DashboardHomeUtil.DASHBOARD);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_lastAccessAsc, "", true),"Failed to create dashboard");

		//back to home page
		webd.getLogger().info("Back to home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by last accessed by Ascending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_ASC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 1), dbName_lastAccessAsc);
		webd.getLogger().info("The last dashboard sorted by 'Last Access Ascending' is " + names.get(names.size() - 1));

		//search the dashboard
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, "ADashboard Test");

		//open the dashboard in builder page
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "ADashboard Test");

		//back to home page
		webd.getLogger().info("back to the dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by last accessed by Ascending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_ASC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 1), "ADashboard Test");
		Assert.assertEquals(names.get(names.size() - 2), dbName_lastAccessAsc);
		webd.getLogger().info("The last 2 dashboards sorted by 'Last Access Ascending' is " + names.get(names.size() - 1) + "and " + names.get(names.size() - 2));

		//edit the dashboard
		webd.getLogger().info("Open dashboard: " + dbName_lastAccessAsc);
		DashboardHomeUtil.selectDashboard(webd, dbName_lastAccessAsc);
		DashboardBuilderUtil.editDashboard(webd,dbName_lastAccessAsc,"test");

		//back to the home page
		webd.getLogger().info("back to the dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by last accessed by Ascending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_ASC);
		//verify the result
		webd.getLogger().info("Verify the sort result");
		names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 2), "ADashboard Test");
		Assert.assertEquals(names.get(names.size() - 1), dbName_lastAccessAsc);
		webd.getLogger().info("The last 2 dashboards sorted by 'Last Access Ascending' is " + names.get(names.size() - 2) + "and " + names.get(names.size() - 1));

		//delete the dashboard
		webd.getLogger().info("Delete the dashboard: " + dbName_lastAccessAsc);
		DashboardHomeUtil.deleteDashboard(webd, dbName_lastAccessAsc, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by last accessed by Ascending");
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_ASC);
		//verify the result
		webd.getLogger().info("Verify the sort result");
		names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 1), "ADashboard Test");
		webd.getLogger().info("The last dashboards sorted by 'Last Access Ascending' is " + names.get(names.size() - 1));
	}

	@Test(alwaysRun = true)
	public void testSortBy_lastAccessedDecending()
	{
		dbName_lastAccessDsc = "Sort Last Access Dsc - " + DashBoardUtils.generateTimeStamp();

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with last accessed by descending");

		//switch to grid view
		webd.getLogger().info("Switch to Grid View");
		DashboardHomeUtil.gridView(webd);

		//create a dashboard
		webd.getLogger().info("Create a dashboard");
		DashboardHomeUtil.createDashboard(webd, dbName_lastAccessDsc, "", DashboardHomeUtil.DASHBOARD);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_lastAccessDsc, "", true),"Failed to create dashboard");

		//back to home page
		webd.getLogger().info("Back to home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by last accessed by Descending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_DSC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), dbName_lastAccessDsc);
		webd.getLogger().info("The last dashboard sorted by 'Last Access Descending' is " + names.get(0));

		//search the dashboard
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, "ADashboard Test");

		//open the dashboard in builder page
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "ADashboard Test");

		//back to home page
		webd.getLogger().info("back to the dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by last accessed by Descending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_DSC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), "ADashboard Test");
		Assert.assertEquals(names.get(1), dbName_lastAccessDsc);
		webd.getLogger().info("The last 2 dashboards sorted by 'Last Access Descending' is " + names.get(0) + "and " + names.get(1));

		//edit the dashboard
		webd.getLogger().info("Open dashboard: " + dbName_lastAccessDsc);
		DashboardHomeUtil.selectDashboard(webd, dbName_lastAccessDsc);
		DashboardBuilderUtil.editDashboard(webd,dbName_lastAccessDsc,"test");

		//back to the home page
		webd.getLogger().info("back to the dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by last accessed by Descending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_DSC);
		//verify the result
		webd.getLogger().info("Verify the sort result");
		names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(1), "ADashboard Test");
		Assert.assertEquals(names.get(0), dbName_lastAccessDsc);
		webd.getLogger().info("The last 2 dashboards sorted by 'Last Access Descending' is " + names.get(1) + "and " + names.get(0));

		//delete the dashboard
		webd.getLogger().info("Delete the dashboard: " + dbName_lastAccessDsc);
		DashboardHomeUtil.deleteDashboard(webd, dbName_lastAccessDsc, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by last accessed by Descending");
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_DSC);
		//verify the result
		webd.getLogger().info("Verify the sort result");
		names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), "ADashboard Test");
		webd.getLogger().info("The last dashboards sorted by 'Last Access Descending' is " + names.get(0));
	}

	@Test(alwaysRun = true)
	public void TestSortBy_LastModified_ListView()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with last modified in list view");

		//search the dashboard
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, "ADashboard Test");

		//open the dashboard in builder page
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "ADashboard Test");

		//edit the dashboard in builder page
		DashboardBuilderUtil.editDashboard(webd, "ADashboard Test", "ADashboard Test desc2", false);
		DashboardBuilderUtil.saveDashboard(webd);

		//back to home page
		webd.getLogger().info("back to the dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		DashboardHomeUtil.closeOverviewPage(webd);

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//verify the sort by is Default
		Assert.assertEquals(webd.getText(PageId.SORTBYID).trim(), "Default");

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by last modified Ascending");
		DashboardHomeUtil.sortListViewByLastModified(webd);

		//verify the result
		webd.getLogger().info("Verify the sort result -- Ascending");
		Assert.assertEquals(webd.getText(PageId.SORTBYID).trim(), "Last Modified Ascending");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 1), "ADashboard Test");
		webd.getLogger().info("The last dashboard sorted by 'Created By Decending' is " + names.get(names.size() - 1));

		//sort the dashboard by name Descending
		webd.getLogger().info("Sort the dashboard by last modifiedi Descending");
		DashboardHomeUtil.sortListViewByLastModified(webd);

		//verify the result
		webd.getLogger().info("Verify the sort result -- Descending");
		Assert.assertEquals(webd.getText(PageId.SORTBYID).trim(), "Last Modified Descending");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), "ADashboard Test");
	}

	@Test(alwaysRun = true)
	public void testSortBy_lastModifiedAscending()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with created by ascendingt");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//search the dashboard
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, "ADashboard Test");

		//open the dashboard in builder page
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "ADashboard Test");

		//edit the dashboard in builder page
		DashboardBuilderUtil.editDashboard(webd, "ADashboard Test", "ADashboard Test desc1", false);
		DashboardBuilderUtil.saveDashboard(webd);

		//back to home page
		webd.getLogger().info("back to the dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		//WaitUtil.waitForPageFullyLoaded(webd);

		DashboardHomeUtil.closeOverviewPage(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by last accessed by Ascending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_ASC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 1), "ADashboard Test");
		webd.getLogger().info("The last dashboard sorted by 'Last Modified Ascending' is " + names.get(names.size() - 1));
	}

	@Test(alwaysRun = true)
	public void testSortBy_lastModifiedDecending()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with created by ascendingt");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//search the dashboard
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, "ADashboard Test");

		//open the dashboard in builder page
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "ADashboard Test");

		//edit the dashboard in builder page
		DashboardBuilderUtil.editDashboard(webd, "ADashboard Test", "ADashboard Test desc2", false);
		DashboardBuilderUtil.saveDashboard(webd);

		//back to home page
		webd.getLogger().info("back to the dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		//WaitUtil.waitForPageFullyLoaded(webd);

		DashboardHomeUtil.closeOverviewPage(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by last accessed by Ascending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_DSC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), "ADashboard Test");
		webd.getLogger().info("The first dashboard sorted by 'Last Modified Decending' is " + names.get(0));
	}

	@Test(alwaysRun = true)
	public void TestSortBy_Name_ListView()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with name in list view");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//verify the sort by is Default
		Assert.assertEquals(webd.getText(PageId.SORTBYID).trim(), "Default");

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by name Ascending");
		DashboardHomeUtil.sortListViewByName(webd);

		//verify the result
		webd.getLogger().info("Verify the sort result -- Ascending");
		Assert.assertEquals(webd.getText(PageId.SORTBYID).trim(), "Name Ascending");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");

		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), "ADashboard Test");

		//sort the dashboard by name Descending
		webd.getLogger().info("Sort the dashboard by name Descending");
		DashboardHomeUtil.sortListViewByName(webd);

		//verify the result
		webd.getLogger().info("Verify the sort result -- Descending");
		Assert.assertEquals(webd.getText(PageId.SORTBYID).trim(), "Name Descending");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");

		names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 1), "ADashboard Test");
		webd.getLogger().info("The last dashboard sorted by 'Created By Decending' is " + names.get(names.size() - 1));

	}

	@Test(alwaysRun = true)
	public void testSortBy_nameAscending()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with name ascending");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by name Ascending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_NAME_ASC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), "ADashboard Test");
		webd.getLogger().info("The first dashboard sorted by 'Name Ascending' is " + names.get(0));
	}

	@Test(alwaysRun = true)
	public void testSortBy_nameDecending()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with name decending");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by name Decending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_NAME_DSC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 1), "ADashboard Test");
		webd.getLogger().info("The last dashboard sorted by 'Name Decending' is " + names.get(names.size() - 1));
	}

	@Test(alwaysRun = true)
	public void testSortBy_Default()
	{
		dbName_SortDefault = "Sort By Default - " + DashBoardUtils.generateTimeStamp();

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort dashboards by default");

		//switch to grid view
		webd.getLogger().info("Switch to Grid View");
		DashboardHomeUtil.gridView(webd);

		//create a dashboard
		webd.getLogger().info("Create a dashboard");
		DashboardHomeUtil.createDashboard(webd, dbName_SortDefault, "", DashboardHomeUtil.DASHBOARD);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_SortDefault, "", true),"Failed to create dashboard");

		//back to home page
		webd.getLogger().info("Back to home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), dbName_SortDefault);
		webd.getLogger().info("The last dashboard sorted by 'Default' is " + names.get(0));

		//search the dashboard
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, "ADashboard Test");

		//open the dashboard in builder page
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "ADashboard Test");

		//back to home page
		webd.getLogger().info("back to the dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), "ADashboard Test");
		Assert.assertEquals(names.get(1), dbName_SortDefault);
		webd.getLogger().info("The last 2 dashboards sorted by 'Default' is " + names.get(0) + "and " + names.get(1));

		//edit the dashboard
		webd.getLogger().info("Open dashboard: " + dbName_SortDefault);
		DashboardHomeUtil.selectDashboard(webd, dbName_SortDefault);
		DashboardBuilderUtil.editDashboard(webd,dbName_SortDefault,"test");

		//back to the home page
		webd.getLogger().info("back to the dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(1), "ADashboard Test");
		Assert.assertEquals(names.get(0), dbName_SortDefault);
		webd.getLogger().info("The last 2 dashboards sorted by 'Default' is " + names.get(1) + "and " + names.get(0));

		//delete the dashboard
		webd.getLogger().info("Delete the dashboard: " + dbName_SortDefault);
		DashboardHomeUtil.deleteDashboard(webd, dbName_SortDefault, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		BrandingBarUtil.visitDashboardHome(webd);
		names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), "ADashboard Test");
		webd.getLogger().info("The last dashboards sorted by 'Default' is " + names.get(0));
	}
}
