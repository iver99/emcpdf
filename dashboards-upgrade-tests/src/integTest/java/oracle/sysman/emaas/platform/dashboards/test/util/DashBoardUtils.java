package oracle.sysman.emaas.platform.dashboards.test.util;

import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.testng.Assert;

public class DashBoardUtils
{
	private DashBoardUtils() {
	  }

	private static WebDriver driver;

	public static void apmOobGrid() 
	{
		DashboardHomeUtil.gridView(driver);
		DashboardHomeUtil.waitForDashboardPresent(driver, "Application Performance Monitoring");

		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Application Performance Monitoring"));
	}	

	public static void itaOobGridView() 
	{
		DashboardHomeUtil.gridView(driver);
		DashboardHomeUtil.waitForDashboardPresent(driver, "Database Health Summary");

		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Database Health Summary"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Host Health Summary"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics: Database"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics: Middleware"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Database"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Middleware"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "WebLogic Health Summary"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Database Configuration and Storage By Version"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Enterprise Overview"));
//		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Host Inventory By Platform"));
//		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Top 25 Databases by Resource Consumption"));
//		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Top 25 WebLogic Servers by Heap Usage"));
//		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Top 25 WebLogic Servers by Load"));
//		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "WebLogic Servers by JDK Version"));
	}

	public static void laOobGridView() 
	{
		DashboardHomeUtil.gridView(driver);
		DashboardHomeUtil.waitForDashboardPresent(driver, "Database Operations");

		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Database Operations"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Host Operations"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Middleware Operations"));
	}

	public static void loadWebDriver(WebDriver webDriver) 
	{
		driver = webDriver;

//		if (driver.isDisplayed(PageId.OVERVIEWCLOSEID)) {
//			DashBoardUtils.closeOverviewPage();
//		}
//
//		Assert.assertFalse(driver.isDisplayed(PageId.OVERVIEWCLOSEID));
//
//		driver.takeScreenShot();
	}

	public static void loadWebDriverOnly(WebDriver webDriver) 
	{
		driver = webDriver;
	}

	public static void noOOBCheckGridView() 
	{
		//verify all the oob dashboard not exsit
		driver.getLogger().info("verify all the oob dashboard not exsit");
		Assert.assertFalse(driver.isElementPresent(PageId.APPLICATION_PERFORMANCE_MONITORING_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.DATABASE_HEALTH_SUMMARY_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.HOST_HEALTH_SUMMARY_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.DATABASE_PERFORMANCE_ANALYTICS_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.MIDDLEWARE_PERFORMANCE_ANALYTICS_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.DATABASE_RESOURCE_ANALYTICS_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.MIDDLEWARE_RESOURCE_ANALYTICS_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.WEBLOGIC_HEALTH_SUMMARY_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.DATABASE_CONFIGURATION_AND_STORAGE_BY_VERSION_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.ENTERPRISE_OVERVIEW_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.HOST_INVENTORY_BY_PLATFORM_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.TOP_25_DATABASES_BY_RESOURCE_CONSUMPTION_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.TOP_25_WEBLOGIC_SERVERS_BY_HEAP_USAGE_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.TOP_25_WEBLOGIC_SERVERS_BY_LOAD_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.WEBLOGIC_SERVERS_BY_JDK_VERSION_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.DATABASE_OPERATIONS_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.HOST_OPERATIONS_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.MIDDLEWARE_OPERATIONS_ID));
	}
	
	public static String generateTimeStamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}

	//Sharing and stopping dashbaord
	//	public static void sharedashboard() 
	//	{
	//		driver.click(PageId.option);
	//		driver.click(PageId.dashboardshare);
	//	}
	//
	//	public static void sharestopping() 
	//	{
	//		driver.click(PageId.option);
	//		driver.click(PageId.stopshare_btn);
	//	}

	//	public static void waitForMilliSeconds(long millisSec) 
	//	{
	//		Thread.sleep(millisSec);
	//	}

}

