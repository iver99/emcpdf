package oracle.sysman.emaas.platform.dashboards.test.ui;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class TestDashBoard_OtherFeatures extends LoginAndLogout
{
	private String dbName_favorite = "";
	private String dbName_timepicker = "";
	private String dbName_columncheck = "";
	private String dbName_ITADashboard = "";
	private String dbName_LADashboard = "";
	private String dbName_testMaximizeRestore = "";
	private String dbName_CustomWidget = "";
	private String dbName_Test = "";
	private String dbDesc_Test = "";
	private String dbName_duplicate = "";
	private String dbName_duplicateOOB = "";
	private String dbName_saveConfirmation = "";
	private String dbName_textWidget = "";
	private String dbName_textWidget_image = "";
	private String dbName_textWidget_link = "";
	private String dbName_textWidget_multiLink = "";
	
	private final String customWidgetName = "Execution Details";
	private final String OOBName = "Middleware Operations";
	private final String OOBDesc = "Displays the current health of your Oracle middleware ecosystem";

	@BeforeClass
	public void createTestDashboard()
	{
		dbName_Test = "Dashboard_withWidget-" + DashBoardUtils.generateTimeStamp();
		dbDesc_Test = "test dashboard";

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in createTestDashboard");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//create dashboard
		webd.getLogger().info("Start to create dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, dbName_Test, dbDesc_Test, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_Test, dbDesc_Test, true), "Create dashboard failed!");
		DashboardBuilderUtil.saveDashboard(webd);
		LoginAndLogout.logoutMethod();
	
	}

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
		DashBoardUtils.deleteDashboard(webd, dbName_timepicker);
		DashBoardUtils.deleteDashboard(webd, dbName_saveConfirmation);
		DashBoardUtils.deleteDashboard(webd, dbName_columncheck);
		DashBoardUtils.deleteDashboard(webd, dbName_ITADashboard);
		DashBoardUtils.deleteDashboard(webd, dbName_LADashboard);
		DashBoardUtils.deleteDashboard(webd, dbName_testMaximizeRestore);
		DashBoardUtils.deleteDashboard(webd, dbName_CustomWidget);
		DashBoardUtils.deleteDashboard(webd, dbName_Test);
		DashBoardUtils.deleteDashboard(webd, dbName_duplicate);
		DashBoardUtils.deleteDashboard(webd, dbName_duplicateOOB);
		DashBoardUtils.deleteDashboard(webd, dbName_textWidget);
		DashBoardUtils.deleteDashboard(webd, dbName_textWidget_link);
		DashBoardUtils.deleteDashboard(webd, dbName_textWidget_multiLink);

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	@Test
	public void testDashboardWith12Columns()
	{
		dbName_columncheck = "DashboardWith12Columns-" + DashBoardUtils.generateTimeStamp();
		String desc = "Description for " + dbName_columncheck;

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testDashboardWith12Columns");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//create dashboard
		webd.getLogger().info("Start to create dashboard");
		DashboardHomeUtil.createDashboard(webd, dbName_columncheck, desc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_columncheck, desc, true),
				"Failed to verify the created dashboard. Probably the creation failed, or name/description is wrongly specified!");

		String widgetName = "Database Errors Trend";
		//add widget
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, widgetName);
		webd.getLogger().info("Add widget finished");

		webd.getLogger().info("Get narrower widgets");
		for (int i = 1; i <= 4; i++) {
			DashboardBuilderUtil.resizeWidget(webd, widgetName, DashboardBuilderUtil.TILE_NARROWER);


		}
		webd.getLogger().info("Finished to get narrower widgets");

		webd.getLogger().info("Get wider widgets");
		for (int i = 1; i <= 10; i++) {
			DashboardBuilderUtil.resizeWidget(webd, widgetName, DashboardBuilderUtil.TILE_WIDER);

		}
		webd.getLogger().info("Finished to get wider widgets");

		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);
	}

	@Test
	public void testDeleteOOB()
	{
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testDeleteOOB");

		//reset all filter options
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);
		//switch to grid view
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//check OOB delete protection
		webd.getLogger().info("Verfiy if the OOB dashboard can be deleted");
		DashboardHomeUtil.search(webd, "Application Performance Monitoring");
		webd.click(DashBoardPageId.INFOBTNID);
		WebElement removeButton = webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.RMBTNID));
		Assert.assertFalse(removeButton.isEnabled(), "delete is enabled for OOB dashboard");
	}

	@Test
	public void testDuplicateDashboard()
	{
		dbName_duplicate = dbName_Test + "-duplicated";
		String dbDesc = "Test_Dashboard_duplicate_desc";

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testDuplicateDashboard");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_Test);
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_Test), "Dashboard NOT found");

		//open the dashboard
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_Test, dbDesc_Test, true), "Dashboard NOT found");

		//Duplicate dashbaord
		DashboardBuilderUtil.duplicateDashboard(webd, dbName_duplicate, dbDesc);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_duplicate, dbDesc, true));

		//go back to home page
		webd.getLogger().info("Go to dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//verify the duplilcated dashboard in home page
		webd.getLogger().info("Verify the duplilcated dashboard in home page");
		DashboardHomeUtil.isDashboardExisted(webd, dbName_duplicate);
	}

	@Test
	public void testDuplicateOOBDashboard()
	{
		dbName_duplicateOOB = OOBName + "-duplicated-" + DashBoardUtils.generateTimeStamp();
		String dbDesc = "Test_Dashboard_duplicate_oob_dashboard description";

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testDuplicateOOBDashboard");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboard
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, OOBName);

		//Duplicate dashbaord
		DashboardBuilderUtil.duplicateDashboard(webd, dbName_duplicateOOB, dbDesc);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_duplicateOOB, dbDesc, true));

		//go back to home page
		webd.getLogger().info("Go to dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//verify the duplilcated dashboard in home page
		webd.getLogger().info("Verify the duplilcated dashboard in home page");
		DashboardHomeUtil.isDashboardExisted(webd, dbName_duplicateOOB);
	}

	@Test
	public void testFavorite()
	{
		dbName_favorite = "favoriteDashboard-" + DashBoardUtils.generateTimeStamp();
		String dbDesc = "favorite_testDashboard desc";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testFavorite");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_favorite, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_favorite, dbDesc, true), "Create dashboard failed!");

		//set it to favorite
		webd.getLogger().info("Set the dashboard to favorite");
		Assert.assertTrue(DashboardBuilderUtil.favoriteOption(webd));

		//verify the dashboard is favorite
		webd.getLogger().info("Visit my favorite page");
		BrandingBarUtil.visitMyFavorites(webd);

		webd.getLogger().info("Verfiy the favortie checkbox is checked");
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "favorites"), "My Favorites option is NOT checked");
		//		WebElement el = webd.getWebDriver().findElement(By.id(DashBoardPageId.Favorite_BoxID));
		//		Assert.assertTrue(el.isSelected());

		webd.getLogger().info("Verfiy the dashboard is favorite");
		//DashboardHomeUtil.search(webd, dbName_favorite);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_favorite), "Can not find the dashboard");

		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_favorite);
		webd.getLogger().info("Verify the dashboard in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_favorite, dbDesc, true), "Create dashboard failed!");

		//set it to not favorite
		webd.getLogger().info("set the dashboard to not favorite");
		Assert.assertFalse(DashboardBuilderUtil.favoriteOption(webd), "Set to not my favorite dashboard failed!");

		//verify the dashboard is not favoite
		webd.getLogger().info("visit my favorite page");
		BrandingBarUtil.visitMyFavorites(webd);
		webd.getLogger().info("Verfiy the favortie checkbox is checked");
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "favorites"), "My Favorites option is NOT checked");
		//		el = webd.getWebDriver().findElement(By.id(DashBoardPageId.Favorite_BoxID));
		//		Assert.assertTrue(el.isSelected());
		webd.getLogger().info("Verfiy the dashboard is not favorite");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_favorite),
				"The dashboard is still my favorite dashboard");
		//		DashboardHomeUtil.search(webd, dbName_favorite);
		//		Assert.assertEquals(webd.getAttribute(DashBoardPageId.DashboardSerachResult_panelID + "@childElementCount"), "0");
		//		webd.getLogger().info("no favorite dashboard");

		//delete the dashboard
		webd.getLogger().info("start to delete the dashboard");

		WebElement el = webd.getWebDriver().findElement(By.id(DashBoardPageId.FAVORITE_BOXID));
		if (el.isSelected()) {
			el.click();
		}
		DashboardHomeUtil.deleteDashboard(webd, dbName_favorite, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
		webd.getLogger().info("the dashboard has been deleted");
	}

	@Test (alwaysRun = true)
	public void testFavorite_OOB()
	{
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testFavorite_OOB");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//open an OOB dashboard
		webd.getLogger().info("Open an OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, OOBName);

		//set the OOB as my favorite
		webd.getLogger().info("Set the OOB dashboard as My Favorite");
		Assert.assertTrue(DashboardBuilderUtil.favoriteOption(webd),"Fail to set the OOB as My Favorite");

		//verify the dashboard is favorite
		webd.getLogger().info("Visit my favorite page");
		BrandingBarUtil.visitMyFavorites(webd);

		webd.getLogger().info("Verfiy the favortie checkbox is checked");
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "favorites"), "My Favorites option is NOT checked");

		webd.getLogger().info("Verfiy the dashboard is favorite");
		//DashboardHomeUtil.search(webd, dbName_favorite);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, OOBName), "Can not find the dashboard");

		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, OOBName);
		webd.getLogger().info("Verify the dashboard in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, OOBName, OOBDesc, true), "Verify OOB dashboard failed!");

		//set it to not favorite
		webd.getLogger().info("set the dashboard to not favorite");
		Assert.assertFalse(DashboardBuilderUtil.favoriteOption(webd), "Set to not my favorite dashboard failed!");

		//verify the dashboard is not favoite
		webd.getLogger().info("visit my favorite page");
		BrandingBarUtil.visitMyFavorites(webd);
		webd.getLogger().info("Verfiy the favortie checkbox is checked");
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "favorites"), "My Favorites option is NOT checked");

		webd.getLogger().info("Verfiy the dashboard is not favorite");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, OOBName),"The dashboard is still my favorite dashboard");
	}

	@Test
	public void testFilterITADashboard()
	{
		dbName_ITADashboard = "ITADashboard-" + DashBoardUtils.generateTimeStamp();
		String dbDesc = "test filter ITA works for custom dashboard";
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testFilterITADashboard");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_ITADashboard, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_ITADashboard, dbDesc, true),
				"Create dashboard failed!");

		//Add the widget to the dashboard
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, "Analytics Line - Categorical");
		webd.getLogger().info("Add widget finished");

		//save dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		//back to home page
		webd.getLogger().info("Back to dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//set filter option, cloud services="IT Analytics" created by ME
		webd.getLogger().info("set filter option, cloud services='IT Analytics' and Created by ME");
		DashboardHomeUtil.filterOptions(webd, "ita");
		DashboardHomeUtil.filterOptions(webd, "me");
		webd.getLogger().info("Verify the created dashboard exists");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_ITADashboard), "The dashboard NOT exists");

		//reset filter options
		webd.getLogger().info("Reset filter options");
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test
	public void testFilterLADashboard()
	{
		dbName_LADashboard = "LADashboard-" + DashBoardUtils.generateTimeStamp();
		String dbDesc = "test filter LA works for custom dashboard";
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testFilterLADashboard");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_LADashboard, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_LADashboard, dbDesc, true),
				"Create dashboard failed!");

		//Add the widget to the dashboard
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, "Database Errors Trend");
		webd.getLogger().info("Add widget finished");

		//save dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		//back to home page
		webd.getLogger().info("Back to dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//set filter option, cloud services="IT Analytics" created by ME
		webd.getLogger().info("set filter option, cloud services='LT Analytics' and Created by ME");
		DashboardHomeUtil.filterOptions(webd, "la");
		DashboardHomeUtil.filterOptions(webd, "me");
		webd.getLogger().info("Verify the created dashboard exists");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_LADashboard), "The dashboard NOT exists");

		//reset filter options
		webd.getLogger().info("Reset filter options");
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test
	public void testHideEntityFilter()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHideEntityFilter");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_Test);
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_Test), "Dashboard NOT found!");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);

		//hide entity filter in dashboard
		webd.getLogger().info("hide entity filter");
		Assert.assertFalse(DashboardBuilderUtil.showEntityFilter(webd, false), "hide entity filter failed");

		//verify the target selector not displayed in the page
		webd.getLogger().info("Verify the target selecotr not diplayed in the page");
		Assert.assertFalse(webd.isDisplayed("css=" + PageId.TARGETSELECTOR_CSS), "The target selector is in the page");
	}

	@Test
	public void testHideOpenInIconForCustomWidget()
	{
		dbName_CustomWidget = "DashboardWithCustomWidget-" + DashBoardUtils.generateTimeStamp();
		String dbDesc = "test hide Open In icon for custom widget";
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testHideOpenInIconForCustomWidget");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_CustomWidget, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_CustomWidget, dbDesc, true),
				"Create dashboard failed!");

		//Add the widget to the dashboard
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, customWidgetName);
		webd.getLogger().info("Add widget finished");

		//save dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		//verify the the Open In Icon is hidden
		webd.getLogger().info("Start to verify the the Open In Icon is hidden");
		Assert.assertFalse(DashBoardUtils.verifyOpenInIconExist(webd, customWidgetName),
				"The 'Open In' icon is displayed for the custom widget");
	}

	@Test
	public void testHideTimeRangeFilter()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHideTimeRangeFilter");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_Test);
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_Test), "Dashboard NOT found!");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);

		//hide time range filter in dashboard
		webd.getLogger().info("hide time range filter");
		Assert.assertFalse(DashboardBuilderUtil.showTimeRangeFilter(webd, false), "hide time range filter failed");

		//verify the time range filter not displayed in the page
		webd.getLogger().info("Verify the time range filter not diplayed in the page");
		Assert.assertFalse(webd.isDisplayed("css=" + PageId.DATETIMEPICK_CSS), "The time range filter is in the page");
	}

	@Test
	public void testMaximizeRestoreWidgetOOBDb()
	{
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testMaximize/RestoreWidget");

		//open the dashboard, eg: Host Operations in the home page
		webd.getLogger().info("open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Host Operations");

		//widget operation
		webd.getLogger().info("maximize/restore the widget");

		DashboardBuilderUtil.maximizeWidget(webd, "Host Logs Trend", 0);
		DashboardBuilderUtil.restoreWidget(webd, "Host Logs Trend", 0);

		//verify the edit/add button not displayed in the page: [changed] element does not exist
		List<WebElement> addButtons = webd.getWebDriver().findElements(By.xpath("//button[@title='Add Content']"));
		Assert.assertFalse(addButtons != null && addButtons.size() > 0, "Unexpected: Add button exists in system dashboard set");

		List<WebElement> editButtons = webd.getWebDriver().findElements(By.xpath("//button[@title='Edit Settings']"));
		Assert.assertFalse(editButtons != null && editButtons.size() > 0,
				"Unexpected: Edit button exists in system dashboard set");
	}

	//test maxmize/restore widget in self created dashboard
	@Test
	public void testMaximizeRestoreWidgetSelfDashboard()
	{
		dbName_testMaximizeRestore = "selfDb-" + DashBoardUtils.generateTimeStamp();

		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testMaximize/RestoreWidget");

		//Create dashboard
		DashboardHomeUtil.createDashboard(webd, dbName_testMaximizeRestore, null, DashboardHomeUtil.DASHBOARD);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_testMaximizeRestore, null, true),
				"Create dashboard failed!");

		//Add two widgets
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, "Access Log Error Status Codes");
		DashboardBuilderUtil.addWidgetToDashboard(webd, "All Logs Trend");
		webd.getLogger().info("Add widget finished");

		//verify if the widget added successfully
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, "Access Log Error Status Codes"),
				"Widget 'Access Log Error Status Codes' not found");
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, "All Logs Trend"), "Widget 'All Logs Trend' not found");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		//widget operation
		webd.getLogger().info("maximize/restore the widget");
		DashboardBuilderUtil.maximizeWidget(webd, "Access Log Error Status Codes", 0);
		Assert.assertFalse(webd.isDisplayed("css=" + ".dbd-widget[data-tile-name=\"All Logs Trend\"]"),
				"Widget 'All Logs Trend' is still displayed");

		DashboardBuilderUtil.restoreWidget(webd, "Access Log Error Status Codes", 0);
		Assert.assertTrue(webd.isDisplayed("css=" + ".dbd-widget[data-tile-name=\"All Logs Trend\"]"),
				"Widget 'All Logs Trend' is not displayed");

		//verify the edit/add button displayed in the page
		WebElement addButton = webd.getWebDriver().findElement(By.xpath("//button[@title='Add Content']"));
		Assert.assertTrue(addButton.isDisplayed(), "Add button isn't displayed in system dashboard set");

		WebElement editButton = webd.getWebDriver().findElement(By.xpath("//button[@title='Edit Settings']"));
		Assert.assertTrue(editButton.isDisplayed(), "Edit button isn't displayed in system dashboard set");
	}

	@Test
	public void testSaveConfirmation()
	{
		dbName_saveConfirmation = "TestSaveConfirmation-" + DashBoardUtils.generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testSaveConfimation");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create a dashboard
		webd.getLogger().info("create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_saveConfirmation, null, DashboardHomeUtil.DASHBOARD);
		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_saveConfirmation, null, true),
				"Create dashboard failed!");

		//edit the dashboard
		webd.getLogger().info("add a widget to the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, "Database Errors Trend");

		//leave the builder page
		webd.getLogger().info("return to dashboard home page");
		DashBoardUtils.clickDashboardLinkInBrandingBar(webd);

		webd.getLogger().info("the warning dialog pop up");
		DashBoardUtils.handleAlert(webd);

		//verify if in the home page
		webd.getLogger().info("Verify if in the home page");
		WebDriverWait wait1 = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait1.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(PageId.DASHBOARDDISPLAYPANELCSS)));
	}

	@Test
	public void testShareDashboard()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testShareDashboard");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_Test);
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_Test), "Dashboard NOT found!");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);

		//sharing dashbaord
		webd.getLogger().info("Share the dashboard");
		Assert.assertTrue(DashboardBuilderUtil.toggleShareDashboard(webd), "Share dashboard failed!");

	}

	@Test
	public void testShowEntityFilter()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testShowEntityFilter");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_Test);
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_Test), "Dashboard NOT found!");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);

		//show entity filter in dashboard
		webd.getLogger().info("show entity filter");
		Assert.assertTrue(DashboardBuilderUtil.showEntityFilter(webd, true), "show entity filter failed");

		//verify the target selector not displayed in the page
		webd.getLogger().info("Verify the target selecotr diplayed in the page");
		Assert.assertTrue(webd.isDisplayed("css=" + PageId.TARGETSELECTOR_CSS), "The target selector is NOT in the page");
	}

	@Test
	public void testShowTimeRangeFilter()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testShowTimeRangeFilter");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_Test);
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_Test), "Dashboard NOT found!");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);

		//show time range filter in dashboard
		webd.getLogger().info("Show time range filter");
		Assert.assertTrue(DashboardBuilderUtil.showTimeRangeFilter(webd, true), "Show time range filter failed");

		//verify the time range filter displayed in the page
		webd.getLogger().info("Verify the time range filter diplayed in the page");
		Assert.assertTrue(webd.isDisplayed("css=" + PageId.DATETIMEPICK_CSS), "The time range filter is NOT in the page");

		//add test case for EMCPDF-1412

		webd.getLogger().info("change time range filter");
		Assert.assertFalse(DashboardBuilderUtil.showTimeRangeFilter(webd, false), "Show time range filter failed");

		//verify the time range filter not displayed in the page
		webd.getLogger().info("Verify the time range filter is invisible in the page");
		Assert.assertFalse(webd.isDisplayed("css=" + PageId.DATETIMEPICK_CSS), "The time range filter is displayed in the page");

	}

	@Test(dependsOnMethods = { "testShareDashboard" })
	public void testStopShareDashboard()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testStopShareDashboard");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_Test);
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_Test), "Dashboard NOT found!");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);

		//stop sharing dashbaord
		webd.getLogger().info("Stop share the dashboard");
		Assert.assertFalse(DashboardBuilderUtil.toggleShareDashboard(webd), "Stop sharing the dashboard failed!");
	}

	@Test
	public void testTimePicker()
	{
		dbName_timepicker = "TestDashboardTimeselector-" + DashBoardUtils.generateTimeStamp();
		String dbDesc = "Test Dashboard timeselector description";

		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testTimePicker");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_timepicker, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_timepicker, dbDesc, true), "Create dashboard failed!");

		//disable time selector
		DashboardBuilderUtil.showTimeRangeFilter(webd, false);
		//verify the dashboard, the time selector no display
		webd.getLogger().info("Verify the time selector is not diplayed in dashboard");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_timepicker, dbDesc, false),
				"Time selector component is still displayed!");
		//enable time selector
		DashboardBuilderUtil.showTimeRangeFilter(webd, true);
		//verify the dashboard, the time selector is displayed
		webd.getLogger().info("Verify the time selector is diplayed in dashboard");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_timepicker, dbDesc, true),
				"Time selector component is not displayed!");

		//Add the widget to the dashboard
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, "Database Errors Trend");
		webd.getLogger().info("Add widget finished");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		//set time range for time picker
		webd.getLogger().info("Set the time range");
		TimeSelectorUtil.setTimeRange(webd, TimeRange.Last15Mins);
		String returnTimeRange = TimeSelectorUtil.setCustomTime(webd, "04/06/2015 07:22 PM", "04/07/2016 09:22 PM");

		//convert date to timestamp
		SimpleDateFormat fmt = new SimpleDateFormat("MMM d, yyyy h:mm a");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date dTmpStart = new Date();
		Date dTmpEnd = new Date();

		String tmpReturnDate = returnTimeRange.substring(7);

		String[] tmpDate = tmpReturnDate.split("-");

		String tmpStartDate = tmpDate[0].trim();
		String tmpEndDate = tmpDate[1].trim();

		try {
			dTmpStart = fmt.parse(tmpStartDate);
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dTmpEnd = fmt.parse(tmpEndDate);
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String tmpStartDateNew = df.format(dTmpStart);
		String tmpEndDateNew = df.format(dTmpEnd);
		webd.getLogger().info(tmpStartDateNew);
		webd.getLogger().info(tmpEndDateNew);

		long StartTimeStamp = Timestamp.valueOf(tmpStartDateNew).getTime();
		long EndTimeStamp = Timestamp.valueOf(tmpEndDateNew).getTime();

		webd.getLogger().info(String.valueOf(StartTimeStamp));
		webd.getLogger().info(String.valueOf(EndTimeStamp));

		//open the widget
		webd.getLogger().info("Open the widget");
		DashboardBuilderUtil.openWidget(webd, "Database Errors Trend");

		//verify the url
		webd.switchToWindow();
		//WaitUtil.waitForPageFullyLoaded(webd);
		webd.getLogger().info("Wait for the widget loading....");
		WebDriverWait wait1 = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='srchSrch']")));

		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		if (!url.substring(url.indexOf("emsaasui") + 9).contains(
				"startTime%3D" + String.valueOf(StartTimeStamp) + "%26endTime%3D" + String.valueOf(EndTimeStamp))) {
			Assert.fail("not open the correct widget");
		}
	}
	
	@Test
	public void testTextWidget()
	{
		dbName_textWidget = "Dashboard_textWidget-" + DashBoardUtils.generateTimeStamp();
		
		String dbDesc = "Add text widget into dashboard";
		String content = "This is the dashboard which is used to test the new feature of adding text widget";
		//String content_Hyperlink = "";
		
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testTextWidget");

		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Create the dashboard, then to add text widget");
		DashboardHomeUtil.createDashboard(webd, dbName_textWidget, dbDesc, DashboardHomeUtil.DASHBOARD);
		
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_textWidget, dbDesc, true), "Create dashboard failed!");		
		
		DashboardBuilderUtil.addTextWidgetToDashboard(webd);
		
		//Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, widgetName), "text widget isn't added into the dashboard successfully");
		DashboardBuilderUtil.editTextWidgetAddContent(webd, 1, content);
		
		DashboardBuilderUtil.saveDashboard(webd);
		
		//Verify the content is added successfully
		webd.getLogger().info("Verify the content is added successfully");
		
		WebElement textContent = webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.TEXTCONTENTCSS));
		Assert.assertEquals(textContent.getText(), content);
	}
	
	@Test
	public void testTextWidget_Image()
	{
		dbName_textWidget_image = "Dashboard_textWidgetImage-" + DashBoardUtils.generateTimeStamp();
		
		String dbDesc = "Add text widget into dashboard, test the image feature";
		String urlString = "emsaasui/uifwk/images/o_logo.png";
		String url = "";
		String alternativeText = "test_image";
		
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test image in testTextWidget");

		DashboardHomeUtil.gridView(webd);
		
		String currentUrl = webd.getWebDriver().getCurrentUrl();
		url = (currentUrl.substring(0, currentUrl.indexOf("emsaasui"))).concat(urlString);

		webd.getLogger().info("Create the dashboard, then to add text widget");
		DashboardHomeUtil.createDashboard(webd, dbName_textWidget_image, dbDesc, DashboardHomeUtil.DASHBOARD);
		
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_textWidget_image, dbDesc, true), "Create dashboard failed!");		
		
		DashboardBuilderUtil.addTextWidgetToDashboard(webd);				
		
		DashboardBuilderUtil.addImageInTextWidget(webd, 1, url, alternativeText);
		webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);
		
		DashboardBuilderUtil.addImageInTextWidget(webd, 1, url, null);
		webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);

		List<WebElement> images = webd.getWebDriver().findElements(By.cssSelector(DashBoardPageId.IMAGESCSS));	
		
		for (WebElement img : images)
		{
			Assert.assertEquals(img.isDisplayed(), true);
		}
		
		DashboardBuilderUtil.saveDashboard(webd);	
	}

	public void testTextWidget_Link()
	{
		dbName_textWidget_link = "Dashboard_textWidgetURL-" + DashBoardUtils.generateTimeStamp();
		
		String dbDesc = "Add text widget into dashboard, test the link feature";
		String url = "www.baidu.com";
		
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test link in testTextWidget");

		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Create the dashboard, then to add text widget");
		DashboardHomeUtil.createDashboard(webd, dbName_textWidget_link, dbDesc, DashboardHomeUtil.DASHBOARD);
		
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_textWidget_link, dbDesc, true), "Create dashboard failed!");		
		
		DashboardBuilderUtil.addTextWidgetToDashboard(webd);				
		
		//Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, widgetName), "text widget isn't added into the dashboard successfully");
		DashboardBuilderUtil.addLinkInTextWidget(webd, 1, url, DashBoardPageId.PROTOCOLOPTION_HTTP);
		webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);
		//Verify the content is added successfully
		webd.getLogger().info("Verify the url is added successfully");	
		WebElement textContent = webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.TEXTCONTENTCSS));
		webd.getLogger().info(textContent.getText());
		Assert.assertEquals(textContent.getText(), DashBoardPageId.PROTOCOLOPTION_HTTP + url);		
		
		DashboardBuilderUtil.addLinkInTextWidget(webd, 1, url, DashBoardPageId.PROTOCOLOPTION_HTTPS);
		webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);
		//Verify the content is added successfully
		webd.getLogger().info("Verify the url is added successfully");			
		textContent = webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.TEXTCONTENTCSS));
		webd.getLogger().info(textContent.getText());
		Assert.assertEquals(textContent.getText(), DashBoardPageId.PROTOCOLOPTION_HTTPS + url);				
		
		DashboardBuilderUtil.addLinkInTextWidget(webd, 1, url, DashBoardPageId.PROTOCOLOPTION_FTP);
		webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);
		//Verify the content is added successfully
		webd.getLogger().info("Verify the url is added successfully");	
		textContent = webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.TEXTCONTENTCSS));
		webd.getLogger().info(textContent.getText());
		Assert.assertEquals(textContent.getText(), DashBoardPageId.PROTOCOLOPTION_FTP + url);		
		
		DashboardBuilderUtil.addLinkInTextWidget(webd, 1, url, DashBoardPageId.PROTOCOLOPTION_NEWS);
		webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);
		//Verify the content is added successfully
		webd.getLogger().info("Verify the url is added successfully");				
		textContent = webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.TEXTCONTENTCSS));
		webd.getLogger().info(textContent.getText());
		Assert.assertEquals(textContent.getText(), DashBoardPageId.PROTOCOLOPTION_NEWS + url);		
		
		DashboardBuilderUtil.addLinkInTextWidget(webd, 1, url, DashBoardPageId.PROTOCOLOPTION_OTHER);
		webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);
		//Verify the content is added successfully
		webd.getLogger().info("Verify the url is added successfully");		
		textContent = webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.TEXTCONTENTCSS));
		webd.getLogger().info(textContent.getText());
		Assert.assertEquals(textContent.getText(), DashBoardPageId.PROTOCOLOPTION_OTHER + url);		
		
		DashboardBuilderUtil.saveDashboard(webd);				
	}
	
	@Test
	public void testTextWidget_multiLink()
	{
		dbName_textWidget_multiLink = "Dashboard_textWidgetMultiURL-" + DashBoardUtils.generateTimeStamp();
		
		String dbDesc = "Add text widget into dashboard, add multi link";
		String url = "www.baidu.com";
		
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test link in testTextWidget");

		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Create the dashboard, then to add text widget");
		DashboardHomeUtil.createDashboard(webd, dbName_textWidget_multiLink, dbDesc, DashboardHomeUtil.DASHBOARD);
		
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_textWidget_multiLink, dbDesc, true), "Create dashboard failed!");		
		
		DashboardBuilderUtil.addTextWidgetToDashboard(webd);				
		
		DashboardBuilderUtil.addLinkInTextWidget(webd, 1, url, DashBoardPageId.PROTOCOLOPTION_HTTP);	
		webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);
		
		WebElement textContent = webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.TEXTCONTENTCSS));
		textContent.click();
		webd.getWebDriver().switchTo().activeElement().sendKeys(Keys.ENTER);
		webd.getWebDriver().switchTo().activeElement().sendKeys(Keys.ARROW_UP);
		webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);

		DashboardBuilderUtil.addLinkInTextWidget(webd, 1, url, DashBoardPageId.PROTOCOLOPTION_HTTPS);	
		webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);
		
		//Verify the content is added successfully
		webd.getLogger().info("Verify the two urls are added successfully");	
		WebElement textContent1 = webd.getWebDriver().findElement(By.xpath(DashBoardPageId.TEXTCONTENT1));
		Assert.assertEquals(textContent1.getText(), DashBoardPageId.PROTOCOLOPTION_HTTPS + url);			
					
		WebElement textContent2 = webd.getWebDriver().findElement(By.xpath(DashBoardPageId.TEXTCONTENT2));
		Assert.assertEquals(textContent2.getText(), DashBoardPageId.PROTOCOLOPTION_HTTP + url);		
		
		DashboardBuilderUtil.saveDashboard(webd);	
	}
}
