package oracle.sysman.emaas.platform.dashboards.test.DVdashboard;

//import oracle.sysman.emaas.platform.dashboards.test.DPdashboard.*;
import oracle.sysman.emaas.platform.dashboards.test.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.GlobalContextUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeUnit;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @version
 * @author siakula
 * @since release specific (what release of product did this appear in)
 */

public class TestDashBoard extends LoginAndLogout
{
	//private String dbName_withWidgetGrid = "";
	private String dbName_setHome = "";
	private String dbName_favorite = "";

	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		try {
			DashBoardUtils.loadWebDriver(webd);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testFavorite()
	{
		dbName_favorite = "favoriteDashboard-" + generateTimeStamp();
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
		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

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

	@Test
	public void testModifyDashboard_LA()
	{

		//init test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testModifyDashboard_LA");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to Grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboard
		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd,
				oracle.sysman.emaas.platform.dashboards.test.DPdashboard.TestDashBoard.dbName_WithWidget);

		//open the widget
		webd.getLogger().info("Open the widget");
		DashboardBuilderUtil.openWidget(webd,
				oracle.sysman.emaas.platform.dashboards.test.DPdashboard.TestDashBoard.WidgetName_LA);

		webd.switchToWindow();
		webd.getLogger().info("Wait for the widget loading....");
		WebDriverWait wait1 = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='srchSrch']")));

	}

	@Test
	public void testModifyDashboard_UDE()
	{

		//init test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testModifyDashboard_UDE");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to Grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboard
		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd,
				oracle.sysman.emaas.platform.dashboards.test.DPdashboard.TestDashBoard.dbName_WithWidget);

		//open the widget
		webd.getLogger().info("Open the widget");
		DashboardBuilderUtil.openWidget(webd,
				oracle.sysman.emaas.platform.dashboards.test.DPdashboard.TestDashBoard.WidgetName_UDE);

		webd.switchToWindow();
		webd.getLogger().info("Wait for the widget loading....");
		WebDriverWait wait1 = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@id,'save_widget_btn')]")));

	}

	@Test
	public void testNoGCinURL()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testNoGCinURL");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to Grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboard
		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd,
				oracle.sysman.emaas.platform.dashboards.test.DPdashboard.TestDashBoard.dbName_DisableEntitiesTime);

		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd));

		String currenturl = webd.getWebDriver().getCurrentUrl();

		Assert.assertFalse(currenturl.contains("omcCtx="), "The global context infomation is in URL");
	}

	@Test
	public void testNoGCinURL_EnableEntitiesTimeSelector()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testNoGCinURL_EnableEntitiesTimeSelector");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to Grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboard
		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd,
				oracle.sysman.emaas.platform.dashboards.test.DPdashboard.TestDashBoard.dbName_EnableEntitiesTime);

		//verify the gc context
		webd.getLogger().info("Start to verify the global context related information");
		webd.getLogger().info("Verify 'All Entities' in global context bar doesn't displayed");
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd),
				"Shouldn't have 'All Entities' in global context bar in the page");
		String currenturl = webd.getWebDriver().getCurrentUrl();
		Assert.assertFalse(currenturl.contains("omcCtx="), "The global context infomation is in URL");

		webd.getLogger().info("Set time selector");
		TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 6, TimeUnit.Hour);
		currenturl = webd.getWebDriver().getCurrentUrl();
		Assert.assertFalse(currenturl.contains("omcCtx="), "The global context infomation is in URL");
	}

	@Test
	public void testSetHome()
	{
		dbName_setHome = "setHomeDashboard-" + generateTimeStamp();
		String dbDesc = "SetHome_testDashboard desc";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testCreateDashboad_noWidget_GridView");

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

	@Test
	public void testDashboard_GCenabled() 
	{						
		//init test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test dashboard GC enabled");
	
		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);
	
		//switch to Grid View
		webd.getLogger().info("Switch to Grid view");
		DashboardHomeUtil.gridView(webd);
		
        //open the dashboard
		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, oracle.sysman.emaas.platform.dashboards.test.DPdashboard.TestDashBoard.dbName_GCenabled);
	
		String baseUrl = null;
		String newUrl1 = null;
		String newUrl2 = null;
		String newUrl3 = null;
		
		baseUrl = webd.getWebDriver().getCurrentUrl();
		
		//Verify time range in dashboard should change
		newUrl1 = GlobalContextUtil.generateUrlWithGlobalContext(webd, baseUrl, null, "LAST_5_MINUTE", null, null, null);		
		webd.open(newUrl1);
				
		Assert.assertTrue(webd.isDisplayed("//span[text()='Last 5 minutes: ']"), "The time range displayed isn't right");
		
		//Verify dashboard when url without omcCtx
		newUrl2 = newUrl1.split("&")[0];
		webd.open(newUrl2);
		
		//Assert.assertTrue(webd.isDisplayed("//*[@id='emcta-ctxtSel_entCount']"), "Entities in GC isn't displayed in dashboard page");
		//Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "Shouldn't have 'All Entities' in global context bar in the page");
		Assert.assertFalse(webd.isDisplayed("//*[@id='globalBar_pillWrapper']"), "All Entities is displayed in dashboard page");
		
		//Verify dashboard when url having composite
		newUrl3 = GlobalContextUtil.generateUrlWithGlobalContext(webd, baseUrl, "8426448730BDF663A9806A69AA2C445B", "LAST_5_MINUTE", null, null, null);
		webd.open(newUrl3);
		
		Assert.assertTrue(webd.isDisplayed("//span[text()='Composite: /SOA1213_base_domain/base_domain/soa_server1/soa-infra_System']"), "The composite isn't displayed in GC bar");
	} 
	
	private String generateTimeStamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}
}
