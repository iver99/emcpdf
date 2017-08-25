package oracle.sysman.emaas.platform.dashboards.test.ui.util;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1150;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1180;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1200;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.oracle.omc.omctf.testsdk.context.TenantResolver;

public class DashBoardUtils
{
	private static WebDriver driver;
	public static final String OMCEE = "OMCEE";
	public static final String OMCLOG = "OMCLOG";
	public static final String OMCSE = "OMCSE";
	public static final String SECSE = "SECSE";
	public static final String SECSMA = "SECSMA";
	public static final String OMCTrail = "OMCTrail";
	public static final String OSMACCTrail = "OSMACCTrail";

	public static void apmOobExist(WebDriver driver)
	{
		driver.getLogger().info("Wait for dashboards loading...");
		DashboardHomeUtil.waitForDashboardPresent(driver, "Application Performance Monitoring");

		driver.getLogger().info("Verify below APM OOB dashboard exist...");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Application Performance Monitoring"));
	}

	public static void apmOobNotExist(WebDriver driver)
	{
		driver.getLogger().info("Verify below APM OOB don't exist...");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Application Performance Monitoring"));
	}

	public static void clickDashboardLinkInBrandingBar(WebDriver webdriver)
	{
		if (DashBoardUtils.isHamburgerMenuEnabled(webdriver)) {
			if (!BrandingBarUtil.isHamburgerMenuDisplayed(webdriver)) {
				BrandingBarUtil.toggleHamburgerMenu(webdriver);
			}
			webdriver.getLogger().info("Click Dashboards menu item to back to dashboard home page");
			webdriver.getWebDriver().findElement(By.cssSelector("#omc_root_dashboards>span")).click();
		}
		else {
			webdriver.getLogger().info("Click Compass icon to display menu of branding bar");
			webdriver.getWebDriver().findElement(By.xpath(PageId.COMPASSICON)).click();
			WebDriverWait wait = new WebDriverWait(webdriver.getWebDriver(), 900L);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(PageId.DASHBOARDLINK)));
			webdriver.getLogger().info("Click Dashboard link to back to dashboard home page");
			webdriver.getWebDriver().findElement(By.xpath(PageId.DASHBOARDLINK)).click();
		}
	}

	public static void closeOverviewPage()
	{
		//		driver.getLogger().info("before clicking overview button");
		//		driver.click(PageId.OVERVIEWCLOSEID);
		//		driver.getLogger().info("after clicking overview button");
	}

	public static void deleteDashboard(WebDriver webdriver, String DashboardName)
	{
		try {
			Validator.notEmptyString("DashboardName", DashboardName);
			DashboardHomeUtil.search(webdriver, DashboardName);
			String searchresult = webdriver.getElement("css=" + PageId.DASHBOARDDISPLAYPANELCSS)
					.getAttribute("childElementCount");
			int dbnumber = Integer.parseInt(searchresult);
			for (int i = 0; i < dbnumber; i++) {
				if (DashboardHomeUtil.isDashboardExisted(webdriver, DashboardName)) {
					webdriver.getLogger().info("Start to delete the dashboard: " + DashboardName);
					DashboardHomeUtil.deleteDashboard(webdriver, DashboardName, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
				}
			}
			webdriver.getLogger().info("Verify the dashboard: " + DashboardName + " has been deleted");
			Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webdriver, DashboardName), "Delete dashboard "
					+ DashboardName + " failed!");
			webdriver.getLogger().info("Delete the dashboard: " + DashboardName + " finished");
		}
		catch (IllegalArgumentException ex) {
			webdriver.getLogger().info(ex.getLocalizedMessage());
		}
	}

	public static String generateTimeStamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}

	public static String getTenantName(String lookupName)
	{
		return TenantResolver.getDefault().getTenantName(lookupName);
	}

	public static void handleAlert(WebDriver webdriver)
	{
		WebDriverWait wait = new WebDriverWait(webdriver.getWebDriver(), 900L);
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());

		//Accepting alert.
		webdriver.getLogger().info("foucus on the alert");
		alert = webdriver.getWebDriver().switchTo().alert();

		webdriver.getLogger().info("click button on the dialog, should navigate to the home page");
		alert.accept();
	}

	/**
	 * @param driver
	 * @param filteroption
	 *            option value - la, ita, apm, orchestration, security
	 * @return
	 */
	public static boolean isFilterOptionExisted(WebDriver driver, String filteroption)
	{
		boolean isExisted = false;
		driver.getLogger().info("isFilterOptionExisted filter: " + filteroption);
		Validator.notEmptyString("filter", filteroption);
		switch (filteroption) {
			case "ita":
				isExisted = driver.getElement(DashBoardPageId.FILTERITALOCATOR).isDisplayed();
				break;
			case "la":
				isExisted = driver.getElement(DashBoardPageId.FILTERLALOCATOR).isDisplayed();
				break;
			case "apm":
				isExisted = driver.getElement(DashBoardPageId.FILTERAPMLOCATOR).isDisplayed();
				break;
			case "orchestration":
				isExisted = driver.getElement(DashBoardPageId.FILTERORCHESTRATIONLOCATOR).isDisplayed();
				break;
			case "security":
				isExisted = driver.getElement(DashBoardPageId.FILTERSECURITYLOCATOR).isDisplayed();
				break;
			default:
				throw new IllegalArgumentException("Unkonw filter option: " + filteroption);
		}
		return isExisted;
	}

	public static boolean isHamburgerMenuEnabled(WebDriver driver)
	{
		if (driver.isElementPresent("css=" + DashBoardPageId_1180.HAMBURGERMENU_ICON_CSS)) {
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean isHomePageExploreItemExisted(WebDriver driver, String listItem)
	{
		driver.getLogger().info("Check if '" + listItem + "' existed in Home page Explore drop down list");
		driver.getLogger().info("click Explore drop down list");
		driver.click("name=" + DashBoardPageId.EXPLOREDATABTNID);

		if (listItem.trim().equals(DashboardHomeUtil.EXPLOREDATA_MENU_DATAEXPLORER)) {
			return driver.isDisplayed(DashBoardPageId_1150.XPATH_EXPLORE_Search);
		}
		else if (listItem.trim().equals(DashboardHomeUtil.EXPLOREDATA_MENU_LOGEXPLORER)) {
			return driver.isDisplayed(DashBoardPageId_1150.XPATH_EXPLORE_LOG);
		}
		return false;
	}

	/**
	 * @param driver
	 * @param listItem
	 *            Data Explorer|Log Explorer
	 * @return
	 */

	public static boolean isWelcomePageDataExplorerItemExisted(WebDriver driver, String listItem)
	{
		driver.getLogger().info("Check if '" + listItem + "' existed in Welcome page Explorers drop down list");
		driver.getLogger().info("Click IT Analytics drop down list if it existed");
		if (driver.isElementPresent("id=oj-select-choice-" + DashBoardPageId.WELCOME_DATAEXP_SELECTID)
				&& driver.isDisplayed("id=oj-select-choice-" + DashBoardPageId.WELCOME_DATAEXP_SELECTID)) {
			driver.click("id=oj-select-choice-" + DashBoardPageId.WELCOME_DATAEXP_SELECTID);

			driver.getLogger().info("Check if drop down list item: '" + listItem + "' existed");
			List<WebElement> webe = driver.getWebDriver().findElements(
					By.cssSelector("#oj-listbox-results-dataExp_options>li>div"));
			if (webe == null || webe.isEmpty()) {
				throw new NoSuchElementException("drop down listitem is not found");
			}
			for (WebElement nav : webe) {
				if (nav.getText().trim().equals(listItem) && nav.isEnabled()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param driver
	 * @param listItem
	 *            selection Select | Performance Analytics - Database | Performance Analytics Application Server | Resource
	 *            Analytics - Database | Resource Analytics - Middleware | Resource Analytics - Host | Application Performance
	 *            Analytics| Availability Analytics | Data Explorer
	 * @return
	 */

	public static boolean isWelcomePageITADataExplorerItemExisted(WebDriver driver, String listItem)
	{
		driver.getLogger().info("Check if '" + listItem + "' existed in Welcome page IT Analytics drop down list");
		driver.getLogger().info("Click IT Analytics drop down list if it existed");
		if (driver.isElementPresent("id=oj-select-choice-" + DashBoardPageId.WELCOME_ITA_SELECTID)
				&& driver.isDisplayed("id=oj-select-choice-" + DashBoardPageId.WELCOME_ITA_SELECTID)) {
			driver.click("id=oj-select-choice-" + DashBoardPageId.WELCOME_ITA_SELECTID);

			driver.getLogger().info("Check if drop down list item: '" + listItem + "' existed");
			List<WebElement> webe = driver.getWebDriver().findElements(By.cssSelector("#oj-listbox-results-ITA_options>li>div"));
			if (webe == null || webe.isEmpty()) {
				throw new NoSuchElementException("drop down listitem is not found");
			}
			for (WebElement nav : webe) {
				if (nav.getText().trim().equals(listItem) && nav.isEnabled()) {
					return true;
				}
			}
		}
		return false;
	}

	public static void itaOobExist(WebDriver driver)
	{
		driver.getLogger().info("Wait for dashboards loading...");
		DashboardHomeUtil.waitForDashboardPresent(driver, "Performance Analytics: Database");

		driver.getLogger().info("Verify below IT Analytics OOB dashboard Set and Dashboards exist...");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Exadata Health"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Enterprise Health"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "UI Gallery"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Application Performance Analytics"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Availability Analytics"));

		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics Application Server"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics: Database"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Database"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Host"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Middleware"));
	}

	public static void itaOobNotExist(WebDriver driver)
	{
		driver.getLogger().info("Verify below IT Analytics OOB dashboard Set and Dashboards don't exist...");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Exadata Health"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "UI Gallery"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Enterprise Health"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Application Performance Analytics"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Availability Analytics"));

		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics Application Server"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics: Database"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Database"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Host"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Middleware"));

		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Categorical"));
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Others"));
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Overview"));
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Performance"));
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Timeseries"));
		//
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Entities"));
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Summary"));
	}

	public static void itaOobNotExist_v2v3(WebDriver driver)
	{
		driver.getLogger().info("Verify below IT Analytics OOB dashboard Set and Dashboards don't exist...");

		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Application Performance Analytics"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Availability Analytics"));

		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics Application Server"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics: Database"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Database"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Host"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Middleware"));
		//
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Categorical"));
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Others"));
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Overview"));
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Performance"));
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Timeseries"));
		//
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Entities"));
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Summary"));
	}

	public static void itaOobNotExistV4(WebDriver driver)
	{
		driver.getLogger().info("Verify below IT Analytics OOB dashboard Set and Dashboards don't exist...");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Exadata Health"));

		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Application Performance Analytics"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Availability Analytics"));

		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics Application Server"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics: Database"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Database"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Host"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Middleware"));

	}

	public static void laOobExist(WebDriver driver)
	{
		driver.getLogger().info("Wait for dashboards loading...");
		DashboardHomeUtil.waitForDashboardPresent(driver, "Database Operations");

		driver.getLogger().info("Verify below Log Analytics OOB dashboards exist...");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Database Operations"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Host Operations"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Middleware Operations"));
	}

	public static void laOobNotExist(WebDriver driver)
	{
		driver.getLogger().info("Verify below Log Analytics OOB don't exist...");

		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Database Operations"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Host Operations"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Middleware Operations"));
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

	public static void noOOBCheck(WebDriver driver)
	{
		//verify all the oob dashboard not exsit
		driver.getLogger().info("verify all the oob dashboard not exsit");
		DashBoardUtils.apmOobNotExist(driver);
		DashBoardUtils.itaOobNotExist(driver);
		DashBoardUtils.laOobNotExist(driver);
		DashBoardUtils.orchestrationOobNotExist(driver);
		DashBoardUtils.securityOobNotExist(driver);
		DashBoardUtils.outDateOob(driver);
	}

	public static void orchestrationOobExist(WebDriver driver)
	{
		driver.getLogger().info("Wait for dashboards loading...");
		DashboardHomeUtil.waitForDashboardPresent(driver, "Orchestration Workflows");

		driver.getLogger().info("Verify below Orchestration OOB dashboard exist...");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Orchestration Workflows"));
	}

	public static void orchestrationOobNotExist(WebDriver driver)
	{
		driver.getLogger().info("Verify below Orchestration OOB don't exist...");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Orchestration Workflows"));
	}

	public static void outDateOob(WebDriver driver)
	{
		driver.getLogger().info("Verify below IT Analytics OOB don't exist...");
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Database Configuration and Storage By Version"));
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Enterprise Overview"));
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Host Inventory by Platform"));
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Top 25 Databases by Resource Consumption"));
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Top 25 WebLogic Servers by Heap Usage"));
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Top 25 WebLogic Servers by Load"));
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "WebLogic Servers by JDK Version"));
		//
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Database Health Summary"));
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Host Health Summary"));
		//		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "WebLogic Health Summary"));
	}

	public static void securityOobExist(WebDriver driver)
	{
		driver.getLogger().info("Wait for dashboards loading...");
		DashboardHomeUtil.waitForDashboardPresent(driver, "DNS");

		driver.getLogger().info("Verify below Security OOB dashboards exist...");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "DNS"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Firewall"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Oracle Database Security"));
	}

	public static void securityOobNotExist(WebDriver driver)
	{
		driver.getLogger().info("Verify below Security OOB dashboards do not exist...");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "DNS"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Firewall"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Oracle Database Security"));
	}

	public static void udeOobExist(WebDriver driver)
	{
		driver.getLogger().info("Wait for dashboards loading...");
		DashboardHomeUtil.waitForDashboardPresent(driver, "Exadata Health");

		driver.getLogger().info("Verify below UDE OOB dashboard Set exist...");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Exadata Health"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Enterprise Health"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "UI Gallery"));
	}

	public static void udeOobExistV4(WebDriver driver)
	{
		driver.getLogger().info("Wait for dashboards loading...");
		DashboardHomeUtil.waitForDashboardPresent(driver, "Enterprise Health");

		driver.getLogger().info("Verify below UDE OOB dashboard Set exist...");
		//		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Exadata Health"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Enterprise Health"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "UI Gallery"));
	}

	public static boolean verfiyShareOptionDisabled()
	{
		driver.getLogger().info("Click the option icon of Dashboard Set");
		driver.waitForElementPresent("css=" + PageId.DASHBOARDSETOPTIONS_CSS);
		driver.click("css=" + PageId.DASHBOARDSETOPTIONS_CSS);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.getLogger().info("Click Edit icon");
		driver.waitForElementPresent("css=" + PageId.DASHBOARDSETOPTIONSEDIT_CSS);
		driver.click("css=" + PageId.DASHBOARDSETOPTIONSEDIT_CSS);

		driver.getLogger().info("Expand Share options");
		driver.waitForElementPresent("css=" + PageId.RIGHTDRAWEREDITSINGLEDBSHARE_CSS);
		driver.click("css=" + PageId.RIGHTDRAWEREDITSINGLEDBSHARE_CSS);

		driver.getLogger().info("Verify the options are disabled or not");
		WebElement ShareOption = driver.getWebDriver().findElement(By.cssSelector(PageId.DASHBOARDSETSHARE_CSS));
		WebElement NotShareOption = driver.getWebDriver().findElement(By.cssSelector(PageId.DASHBOARDSETNOTSHARE_CSS));
		if (!ShareOption.isEnabled() & !NotShareOption.isEnabled()) {
			driver.getLogger().info("The Options are disabled!");
			return true;
		}
		else {
			driver.getLogger().info("Have the option enabled");
			return false;
		}
	}

	public static void verifyAddLinkButton(WebDriver webdriver, String widgetName, int index)
	{
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);

		webdriver.waitForElementPresent(DashBoardPageId_190.BUILDERTILESEDITAREA);
		WebDriverWait wait = new WebDriverWait(webdriver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId_190.BUILDERTILESEDITAREA)));
		WaitUtil.waitForPageFullyLoaded(webdriver);

		String titleTitlesLocator = String.format(DashBoardPageId_190.BUILDERTILETITLELOCATOR, widgetName);

		List<WebElement> tileTitles = webdriver.getWebDriver().findElements(By.xpath(titleTitlesLocator));
		if (tileTitles == null || tileTitles.size() <= index) {
			throw new NoSuchElementException("Tile with title=" + widgetName + ", index=" + index + " is not found");
		}

		String titleXpath = tileTitles.get(index).toString();

		new Actions(webdriver.getWebDriver()).moveToElement(tileTitles.get(index)).perform();
		webdriver.waitForServer();
		webdriver.takeScreenShot();
		webdriver.savePageToFile();

		WebElement tileTitle = tileTitles.get(index);
		WebElement tileConfig = tileTitle.findElement(By.xpath(DashBoardPageId_190.BUILDERTILECONFIGLOCATOR));
		if (tileConfig == null) {
			throw new NoSuchElementException("Tile config menu for title=" + widgetName + ", index=" + index + " is not found");
		}

		Actions builder = new Actions(webdriver.getWebDriver());
		builder.moveToElement(tileTitle).perform();
		builder.moveToElement(tileConfig).click().perform();

		//click edit button in widget config menu
		webdriver.waitForElementPresent(DashBoardPageId_1200.BUILDERTILEEDITLOCATOR);
		webdriver.click(DashBoardPageId_1200.BUILDERTILEEDITLOCATOR);

		webdriver.waitForElementPresent("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTAREACSSLOCATOR);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.cssSelector(DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTAREACSSLOCATOR)));

		WebElement searchInput = webdriver.getElement("css="
				+ DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBOXCSSLOCATOR);
		Assert.assertFalse(searchInput.isEnabled(), "'Search' input box is not diabled");
	}

	public static void verifyBrandingBarWithTenant(WebDriver webdriver, String tenantType)
	{
		Validator.notEmptyString("TenantType", tenantType);

		//verify Data Explorer in branding bar
		webdriver.getLogger().info("Verify Data Explorer link should be in Branding Bar");
		Assert.assertTrue(BrandingBarUtil.isVisualAnalyzerLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_DATAEXPLORER),
				"'Data Explorer' link should be in Branding Bar");

		//verify Log Explorer in branding bar
		if (OMCLOG.equals(tenantType) || OMCTrail.equals(tenantType)) {
			webdriver.getLogger().info("Verify Log Explorer link should be in Branding Bar");
			Assert.assertTrue(BrandingBarUtil.isVisualAnalyzerLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_LOGEXPLORER),
					"'Log Explorer' link should be in Branding Bar");
		}
		else {
			webdriver.getLogger().info("Verify Log Explorer link should NOT be in Branding Bar");
			Assert.assertFalse(BrandingBarUtil.isVisualAnalyzerLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_LOGEXPLORER),
					"'Log Explorer' link should not be in Branding Bar");
		}

		//verify cloud service according to Edition
		switch (tenantType) {
			case OMCEE:
				webdriver.getLogger().info(
						"'APM', 'Monitoring', 'IT Analytics' and 'Orchestraion' displayed for OMC Enterprise Edition");
				Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_APM),
						"'APM' should in clould service link");
				Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_ITA),
						"'IT Analytics' should in clould service link");
				Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_OCS),
						"'Orchestration' should in clould service link");
				Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_IM),
						"'Monitoring' should in clould service link");

				webdriver.getLogger()
						.info("'Compliance','Log Analytics' and 'Security' NOT displayed for OMC Enterprise Edition");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_LA),
						"'Log Analytics' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_COMP),
						"'Compliance' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_SECU),
						"'Security' should not in clould service link");
				break;
			case OMCSE:
				webdriver.getLogger().info("'APM' and 'Monitoring' displayed for OMC Standard Edition");
				Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_APM),
						"'APM' should in clould service link");
				Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_IM),
						"'Monitoring' should in clould service link");

				webdriver
						.getLogger()
				.info("'Log Analytics','Compliance', 'IT Analytics', 'Orchestraion' and 'Security' NOT displayed for OMC Standard Edition");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_COMP),
						"'Compliance' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_SECU),
						"'Security' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_ITA),
						"'IT Analytics' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_LA),
						"'Log Analytics' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_OCS),
						"'Orchestration' should not in clould service link");
				break;
			case OMCLOG:
				webdriver.getLogger().info("'Log Analytics' displayed for OMC Log Edition");
				webdriver.getLogger().info("Check if having hamburger menu");
				if (DashBoardUtils.isHamburgerMenuEnabled(webdriver)) {
					webdriver.getLogger().info("Hamburger Menu Enable");
					if (BrandingBarUtil.ROOT_MENU_LA.equals(BrandingBarUtil.getCurrentMenuHeader(webdriver).trim())) {
						webdriver.getLogger().info("Now in LA service menu, need to back to root menu");
						BrandingBarUtil.goBackToParentMenu(webdriver);
					}
				}

				Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_LA),
						"'Log Analytics' should in clould service link");

				webdriver
						.getLogger()
				.info("'APM','Compliance','Monitoring', 'IT Analytics', 'Orchestraion' and 'Security' NOT displayed for OMC Log Edition");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_APM),
						"'APM' should in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_ITA),
						"'IT Analytics' should in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_OCS),
						"'Orchestration' should in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_IM),
						"'Monitoring' should in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_COMP),
						"'Compliance' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_SECU),
						"'Security' should not in clould service link");
				break;
			case SECSE:
				//verify cloud service according to Edition
				webdriver.getLogger().info("'Compliance' displayed for OSMACC Compliance Edition");
				Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_COMP),
						"'Compliance' should in clould service link");

				webdriver
						.getLogger()
						.info("'APM','Log Analytics','Monitoring', 'IT Analytics', 'Orchestraion' and 'Security' NOT displayed for OSMACC Trail Edition");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_APM),
						"'APM' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_ITA),
						"'IT Analytics' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_LA),
						"'Log Analytics' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_OCS),
						"'Orchestration' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_IM),
						"'Monitoring' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_SECU),
						"'Security' should not in clould service link");
				break;
			case SECSMA:
				webdriver.getLogger().info("'Security' displayed for OSMACC Security Edition");
				Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_SECU),
						"'Security' should in clould service link");

				webdriver
						.getLogger()
						.info("'APM','Log Analytics','Monitoring', 'IT Analytics', 'Orchestraion' and 'Compliance' NOT displayed for OSMACC Trail Edition");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_APM),
						"'APM' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_ITA),
						"'IT Analytics' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_LA),
						"'Log Analytics' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_OCS),
						"'Orchestration' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_IM),
						"'Monitoring' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_COMP),
						"'Compliance' should not in clould service link");
				break;
			case OMCTrail:
				webdriver.getLogger().info(
						"'APM','Log Analytics','Monitoring', 'IT Analytics' and 'Orchestraion' displayed for OMC Trail Edition");
				Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_APM),
						"'APM' should in clould service link");
				Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_ITA),
						"'IT Analytics' should in clould service link");
				Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_LA),
						"'Log Analytics' should in clould service link");
				Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_OCS),
						"'Orchestration' should in clould service link");
				Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_IM),
						"'Monitoring' should in clould service link");

				webdriver.getLogger().info("'Compliance' and 'Security' NOT displayed for OMC Trail Edition");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_COMP),
						"'Compliance' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_SECU),
						"'Security' should not in clould service link");
				break;
			case OSMACCTrail:
				webdriver.getLogger().info("'Compliance' and 'Security' displayed for OSMACC Trail Edition");
				Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_COMP),
						"'Compliance' should in clould service link");
				Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_SECU),
						"'Security' should in clould service link");

				webdriver
				.getLogger()
				.info("'APM','Log Analytics','Monitoring', 'IT Analytics' and 'Orchestraion' NOT displayed for OSMACC Trail Edition");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_APM),
						"'APM' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_ITA),
						"'IT Analytics' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_LA),
						"'Log Analytics' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_OCS),
						"'Orchestration' should not in clould service link");
				Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webdriver, BrandingBarUtil.NAV_LINK_TEXT_CS_IM),
						"'Monitoring' should not in clould service link");
				break;
			default:
				webdriver.getLogger().info("The tenantType is: " + tenantType);
		}
	}

	public static void verifyBuilderPageWithTenant(WebDriver webdriver, String dbName, String dbDesc, String WidgetName)
	{
		//create a dashboard
		webdriver.getLogger().info("Create a dashboard");
		DashboardHomeUtil.createDashboard(webdriver, dbName, dbDesc);

		//check the ude widget displayed in widget list
		webdriver.getLogger().info("Add UDE widget to the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webdriver, WidgetName);
		DashboardBuilderUtil.saveDashboard(webdriver);

		//check the 'Open In Data Explore' icon displayed in widget title
		Assert.assertTrue(DashBoardUtils.verifyOpenInIconExist(webdriver, WidgetName),
				"The 'Open In Data Explorer' icon should display");

		//back to the home page then delete the created dashboard
		webdriver.getLogger().info("Back to the home page");
		BrandingBarUtil.visitDashboardHome(webdriver);
		webdriver.getLogger().info("Delete the dashboard:" + dbName);
		DashboardHomeUtil.deleteDashboard(webdriver, dbName, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
	}

	public static boolean verifyDashboardInfoInHomePage(WebDriver driver, String Name, String Description, String view)
	{
		//click the info icon
		driver.click(DashBoardPageId.INFOBTNID);

		//verify the name and description displayed in the info box
		String str_name = "";
		String str_desc = "";

		if (view.equals("gridview")) {
			str_name = driver.getText("css=" + PageId.DASHBOARDINFO_NAME_CSS).trim();
			str_desc = driver.getText("css=" + PageId.DASHBOARDINFO_DESC_CSS).trim();
			if (str_name.equals(Name) && str_desc.equals(Description)) {
				return true;

			}
			else {
				return false;
			}
		}
		else if (view.equals("listview")) {
			str_desc = driver.getText("css=" + PageId.DASHBOARDINFO_DESC_CSS).trim();
			if (str_desc.equals(Description)) {
				return true;

			}
			else {
				return false;
			}
		}
		return false;
	}

	public static void verifyHomePageWithTenant(WebDriver webdriver, String tenantType)
	{
		Validator.notEmptyString("TenantType", tenantType);

		//verify Data Explorer in Explore drop down in home page
		webdriver.getLogger().info("Verify Data Explorer drop down list item should be in Home page");
		Assert.assertTrue(DashBoardUtils.isHomePageExploreItemExisted(webdriver, "Data Explorer"),
				"'Data Explorer' item should be in drop down list");

		//verify Log Explorer in branding bar
		if (OMCLOG.equals(tenantType) || OMCTrail.equals(tenantType)) {
			webdriver.getLogger().info("Verify Log Explorer drop down list item should not be in Home page");
			Assert.assertTrue(DashBoardUtils.isHomePageExploreItemExisted(webdriver, "Log Explorer"),
					"'Log Explorer' item should be in drop down list");
		}
		else {
			webdriver.getLogger().info("Verify Log Explorer drop down list item should not be in Home page");
			Assert.assertFalse(DashBoardUtils.isHomePageExploreItemExisted(webdriver, "Log Explorer"),
					"'Log Explorer' item should not be in drop down list");
		}

		//verify the Service in Filter options
		switch (tenantType) {
			case OMCEE:
				webdriver.getLogger().info("'APM', 'IT Analytics' and 'Orchestraion' displayed for OMC Enterprise Edition");
				Assert.assertTrue(DashBoardUtils.isFilterOptionExisted(webdriver, "ita"),
						"IT Analytics option should in Cloud Service filter");
				Assert.assertTrue(DashBoardUtils.isFilterOptionExisted(webdriver, "apm"),
						"APM option should in Cloud Service filter");
				Assert.assertTrue(DashBoardUtils.isFilterOptionExisted(webdriver, "orchestration"),
						"Orchestration option should in Cloud Service filter");

				webdriver.getLogger().info("'Log Analytics' and 'Security' not displayed for OMC Enterprise Edition");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "la"),
						"Log Analytics option should not in Cloud Service filter");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "security"),
						"Security Analytics option should not in Cloud Service filter");
				break;
			case OMCSE:
				webdriver.getLogger().info("'APM' displayed for OMC Standard Edition");
				Assert.assertTrue(DashBoardUtils.isFilterOptionExisted(webdriver, "apm"),
						"APM option should in Cloud Service filter");

				webdriver.getLogger().info(
						"'Log Analytics', 'IT Analytics', 'Security' and 'Orchestraion' NOT displayed for OMC Standard Edition");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "la"),
						"Log Analytics option should not in Cloud Service filter");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "ita"),
						"IT Analytics option should not in Cloud Service filter");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "orchestration"),
						"Orchestration option should not in Cloud Service filter");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "security"),
						"Security Analytics option should not in Cloud Service filter");
				break;
			case OMCLOG:
				webdriver.getLogger().info("'Log Analytics' displayed for OMC Enterprise Edition");
				Assert.assertTrue(DashBoardUtils.isFilterOptionExisted(webdriver, "la"),
						"Log Analytics option should in Cloud Service filter");

				webdriver.getLogger().info(
						"'APM', 'IT Analytics', 'Security' and 'Orchestraion' not displayed for OMC Enterprise Edition");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "ita"),
						"IT Analytics option should not in Cloud Service filter");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "apm"),
						"APM option should not in Cloud Service filter");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "orchestration"),
						"Orchestration option should not in Cloud Service filter");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "security"),
						"Security Analytics option should not in Cloud Service filter");
				break;
			case SECSE:
				webdriver
				.getLogger()
				.info("'APM','Log Analytics', 'IT Analytics', 'Orchestraion' and 'Security' NOT displayed for OSMACC Compliance Edition");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "la"),
						"Log Analytics option should not in Cloud Service filter");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "ita"),
						"IT Analytics option should not in Cloud Service filter");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "apm"),
						"APM option should not in Cloud Service filter");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "orchestration"),
						"Orchestration option should not in Cloud Service filter");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "security"),
						"Security Analytics option should not in Cloud Service filter");
				break;
			case SECSMA:
				webdriver.getLogger().info("'Security' displayed for OSMACC Compliance Edition");
				Assert.assertTrue(DashBoardUtils.isFilterOptionExisted(webdriver, "security"),
						"Security Analytics option should in Cloud Service filter");

				webdriver.getLogger().info(
						"'APM','Log Analytics', 'IT Analytics' and 'Orchestraion' NOT displayed for OSMACC Security Edition");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "la"),
						"Log Analytics option should not in Cloud Service filter");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "ita"),
						"IT Analytics option should not in Cloud Service filter");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "apm"),
						"APM option should not in Cloud Service filter");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "orchestration"),
						"Orchestration option should not in Cloud Service filter");
				break;
			case OMCTrail:
				webdriver.getLogger().info(
						"'APM','Log Analytics', 'IT Analytics' and 'Orchestraion' displayed for OMC Trail Edition");
				Assert.assertTrue(DashBoardUtils.isFilterOptionExisted(webdriver, "la"),
						"Log Analytics option should in Cloud Service filter");
				Assert.assertTrue(DashBoardUtils.isFilterOptionExisted(webdriver, "ita"),
						"IT Analytics option should in Cloud Service filter");
				Assert.assertTrue(DashBoardUtils.isFilterOptionExisted(webdriver, "apm"),
						"APM option should in Cloud Service filter");
				Assert.assertTrue(DashBoardUtils.isFilterOptionExisted(webdriver, "orchestration"),
						"Orchestration option should in Cloud Service filter");

				webdriver.getLogger().info("'Security' NOT displayed for OSMACC Compliance Edition");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "security"),
						"Security Analytics option should not in Cloud Service filter");
				break;
			case OSMACCTrail:
				webdriver.getLogger().info(
						"'APM','Log Analytics', 'IT Analytics' and 'Orchestraion' NOT displayed for OSMACC Trail Edition");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "la"),
						"Log Analytics option should not in Cloud Service filter");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "ita"),
						"IT Analytics option should not in Cloud Service filter");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "apm"),
						"APM option should not in Cloud Service filter");
				Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webdriver, "orchestration"),
						"Orchestration option should not in Cloud Service filter");

				webdriver.getLogger().info("'Security' displayed for OSMACC Compliance Edition");
				Assert.assertTrue(DashBoardUtils.isFilterOptionExisted(webdriver, "security"),
						"Security Analytics option should in Cloud Service filter");
				break;
			default:
				webdriver.getLogger().info("The tenantType is: " + tenantType);
		}
	}

	public static void verifyOOBInHomeWithTenant(WebDriver webdriver, String tenantType)
	{
		Validator.notEmptyString("TenantType", tenantType);

		switch (tenantType) {
			case OMCEE:
				DashBoardUtils.apmOobExist(webdriver);
				DashBoardUtils.itaOobExist(webdriver);
				DashBoardUtils.orchestrationOobExist(webdriver);
				DashBoardUtils.laOobNotExist(webdriver);
				DashBoardUtils.securityOobNotExist(webdriver);
				break;
			case OMCSE:
				DashBoardUtils.apmOobExist(webdriver);
				DashBoardUtils.udeOobExist(webdriver);
				DashBoardUtils.itaOobNotExist_v2v3(webdriver);
				DashBoardUtils.orchestrationOobNotExist(webdriver);
				DashBoardUtils.laOobNotExist(webdriver);
				DashBoardUtils.securityOobNotExist(webdriver);
				break;
			case OMCLOG:
				DashBoardUtils.apmOobNotExist(webdriver);
				DashBoardUtils.itaOobNotExist_v2v3(webdriver);
				DashBoardUtils.orchestrationOobNotExist(webdriver);
				DashBoardUtils.laOobExist(webdriver);
				DashBoardUtils.securityOobNotExist(webdriver);
				break;
			case SECSE:
				DashBoardUtils.securityOobNotExist(webdriver);
				DashBoardUtils.apmOobNotExist(webdriver);
				DashBoardUtils.itaOobNotExist_v2v3(webdriver);
				DashBoardUtils.orchestrationOobNotExist(webdriver);
				DashBoardUtils.laOobNotExist(webdriver);
				DashBoardUtils.udeOobExist(webdriver);
				break;
			case SECSMA:
				DashBoardUtils.apmOobNotExist(webdriver);
				DashBoardUtils.itaOobNotExist_v2v3(webdriver);
				DashBoardUtils.orchestrationOobNotExist(webdriver);
				DashBoardUtils.laOobNotExist(webdriver);
				DashBoardUtils.udeOobExist(webdriver);
				DashBoardUtils.securityOobExist(webdriver);
				break;
			case OMCTrail:
				DashBoardUtils.apmOobExist(webdriver);
				DashBoardUtils.itaOobExist(webdriver);
				DashBoardUtils.orchestrationOobExist(webdriver);
				DashBoardUtils.laOobExist(webdriver);
				DashBoardUtils.securityOobNotExist(webdriver);
				break;
			case OSMACCTrail:
				DashBoardUtils.apmOobNotExist(webdriver);
				DashBoardUtils.itaOobNotExist_v2v3(webdriver);
				DashBoardUtils.orchestrationOobNotExist(webdriver);
				DashBoardUtils.laOobNotExist(webdriver);
				DashBoardUtils.udeOobExist(webdriver);
				DashBoardUtils.securityOobExist(webdriver);
				break;
			default:
				webdriver.getLogger().info("The tenantType is: " + tenantType);
		}
	}

	public static boolean verifyOpenInIconExist(WebDriver driver, String widgetName)
	{
		return DashBoardUtils.verifyOpenInIconExist(driver, widgetName, 1);
	}

	public static boolean verifyOpenInIconExist(WebDriver driver, String widgetName, int index)
	{
		//verify the index, it should equal or larger than 1
		Validator.equalOrLargerThan("index", index, 1);

		//find the widget
		driver.waitForElementPresent(DashBoardPageId_190.BUILDERTILESEDITAREA);
		driver.click(DashBoardPageId_190.BUILDERTILESEDITAREA);

		String titleTitlesLocator = String.format(DashBoardPageId_190.BUILDERTILETITLELOCATOR, widgetName);
		List<WebElement> tileTitles = driver.getWebDriver().findElements(By.xpath(titleTitlesLocator));
		if (tileTitles == null || tileTitles.size() < index) {
			throw new NoSuchElementException("Tile with title=" + widgetName + ", index=" + index + " is not found");
		}
		tileTitles.get(index - 1).click();
		driver.takeScreenShot();
		driver.savePageToFile();

		//move the mouse to the title of the widget

		driver.getLogger().info("Start to find widget with widgetName=" + widgetName + ", index=" + index);
		WebElement widgetTitle = tileTitles.get(index - 1);
		if (widgetTitle == null) {
			throw new NoSuchElementException("Widget with title=" + widgetName + ", index=" + index + " is not found");
		}
		driver.getLogger().info("Found widget with name=" + widgetName + ", index =" + index + " before opening widget link");

		try {
			WebElement widgetDataExplore = widgetTitle.findElement(By.xpath(DashBoardPageId_190.BUILDERTILEDATAEXPLORELOCATOR));
			if (widgetDataExplore == null) {
				//				driver.getLogger().info("Can't find Data Explorer element in DOM");
				//				return false;
				throw new NoSuchElementException("Widget data explorer link for title=" + widgetName + ", index=" + index
						+ " is not found");
			}

			driver.getLogger().info("Found widget configure button");
			Actions builder = new Actions(driver.getWebDriver());
			driver.getLogger().info("Now moving to the widget title bar");
			builder.moveToElement(widgetTitle).perform();
			driver.takeScreenShot();
			driver.savePageToFile();
			driver.getLogger().info("and clicks the widget config button");

			return widgetDataExplore.isDisplayed();
		}
		catch (NoSuchElementException ex) {
			driver.getLogger().info("Can't find Data Explorer element in DOM");
			return false;
		}
	}

	public static void verifyServiceAlwaysDisplayedInWelcomePage(WebDriver webdriver)
	{
		webdriver.getLogger().info("'Dashboards','Explorers' and 'Learn More' always disaplyed...");
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webdriver, "dashboards"));
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webdriver, "dataExplorers"));
		Assert.assertTrue(WelcomeUtil.isLearnMoreItemExisted(webdriver, "getStarted"));
		Assert.assertTrue(WelcomeUtil.isLearnMoreItemExisted(webdriver, "videos"));
		Assert.assertTrue(WelcomeUtil.isLearnMoreItemExisted(webdriver, "serviceOfferings"));
	}

	public static void verifyURL(WebDriver webdriver, String url)
	{
		String currurl = webdriver.getWebDriver().getCurrentUrl();
		webdriver.getLogger().info("the origin url = " + currurl);
		String tmpurl = DashBoardUtils.trimUrlParameters(currurl.substring(currurl.indexOf("emsaasui") + 9));
		webdriver.getLogger().info("the url without para = " + tmpurl);
		Assert.assertEquals(tmpurl, url);
	}

	public static void verifyURL_WithPara(WebDriver webdriver, String url)
	{
		webdriver.getLogger().info("the expected relative url = " + url);
		String currurl = webdriver.getWebDriver().getCurrentUrl();
		webdriver.getLogger().info("the current url = " + currurl);
		String tmpurl = currurl.substring(currurl.indexOf("emsaasui") + 9);
		webdriver.getLogger().info("the relative url to compare = " + tmpurl);
		Assert.assertTrue(tmpurl.contains(url), tmpurl + " does NOT contain " + url);
	}

	public static void verifyWelcomePageWithTenant(WebDriver webdriver, String tenantType)
	{
		Validator.notEmptyString("TenantType", tenantType);

		webdriver.getLogger().info("Start to verify the service links in welome page...");
		//verify the service links always displayed in welcome page
		DashBoardUtils.verifyServiceAlwaysDisplayedInWelcomePage(webdriver);

		switch (tenantType) {
			case OMCEE:
				//verify the service link according to edition
				webdriver.getLogger().info(
						"'APM','Monitoring', 'IT Analytics' and 'Orchestraion' displayed for OMC Enterprise Edition");
				Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webdriver, "APM"), "'APM' servie should in welcome page");
				Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webdriver, "ITA"),
						"'IT Analytics' servie should in welcome page");
				Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webdriver, "infraMonitoring"),
						"'Monitoring' servie should in welcome page");
				Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webdriver, "orchestration"),
						"'Orchestration' servie should in welcome page");

				webdriver.getLogger().info(
						"'Log Analytics', 'Compliance' and 'Security' NOT displayed for OMC Enterprise Edition");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "LA"),
						"'Log Analytics' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "compliance"),
						"'Compliance' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "securityAnalytics"),
						"'Security' servie should not in welcome page");

				webdriver.getLogger().info("Verify the service links in welome page end.");

				//verify the data explorer item in ITA
				webdriver.getLogger().info("Verify the Data Explorer item in ITA in welcome page");
				Assert.assertFalse(DashBoardUtils.isWelcomePageITADataExplorerItemExisted(webdriver, "Data Explorer"),
						"'Data Explorer' should not in ITA");

				//verify the data explorer item in Explorers
				webdriver.getLogger().info("Verify the Data Explorer item in Explorers in welcome page");
				Assert.assertTrue(DashBoardUtils.isWelcomePageDataExplorerItemExisted(webdriver, "Data Explorer"),
						"'Data Explorer' should in Explorers");
				break;
			case OMCSE:
				webdriver.getLogger().info("'APM' and 'Monitoring' displayed for OMC Standard Edition");
				Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webdriver, "APM"), "'APM' servie should in welcome page");
				Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webdriver, "infraMonitoring"),
						"'Monitoring' servie should in welcome page");

				webdriver
						.getLogger()
				.info("'Compliance', 'Log Analytics', 'IT Analytics' , 'Orchestraion'  and 'Security' NOT displayed for OMC Standard Edition");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "LA"),
						"'Log Analytics' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "ITA"),
						"'IT Analytics' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "compliance"),
						"'Compliance' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "securityAnalytics"),
						"'Security' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "orchestration"),
						"'Orchestration' servie should not in welcome page");

				webdriver.getLogger().info("Verify the service links in welome page end.");

				//verify the data explorer item in Explorers
				webdriver.getLogger().info("Verify the Data Explorer item in Explorers in welcome page");
				Assert.assertTrue(DashBoardUtils.isWelcomePageDataExplorerItemExisted(webdriver, "Data Explorer"),
						"'Data Explorer' should in Explorers");
				break;
			case OMCLOG:
				webdriver.getLogger().info("'Log Analytics' displayed for OMC Log Edition");
				Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webdriver, "LA"),
						"'Log Analytics' servie should in welcome page");

				webdriver
						.getLogger()
				.info("'APM','Monitoring', 'IT Analytics', 'Orchestraion','Compliance' and 'Security' NOT displayed for OMC Log Edition");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "APM"),
						"'APM' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "ITA"),
						"'IT Analytics' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "infraMonitoring"),
						"'Monitoring' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "orchestration"),
						"'Orchestration' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "compliance"),
						"'Compliance' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "securityAnalytics"),
						"'Security' servie should not in welcome page");

				webdriver.getLogger().info("Verify the service links in welome page end.");

				//verify the data explorer item in Explorers
				webdriver.getLogger().info("Verify the Data Explorer item in Explorers in welcome page");
				Assert.assertTrue(DashBoardUtils.isWelcomePageDataExplorerItemExisted(webdriver, "Data Explorer"),
						"'Data Explorer' should in Explorers");
				break;
			case SECSE:
				webdriver.getLogger().info("'Compliance' displayed for OSMACC Compliance Edition");
				Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webdriver, "compliance"),
						"'Compliance' servie should in welcome page");

				webdriver
						.getLogger()
						.info("'APM','Log Analytics','Monitoring', 'IT Analytics', 'Orchestraion' and 'Security' NOT displayed for OSMACC Compliance Edition");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "APM"),
						"'APM' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "LA"),
						"'Log Analytics' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "ITA"),
						"'IT Analytics' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "infraMonitoring"),
						"'Monitoring' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "orchestration"),
						"'Orchestration' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "securityAnalytics"),
						"'Security' servie should not in welcome page");

				webdriver.getLogger().info("Verify the service links in welome page end.");

				//verify the data explorer item in Explorers
				webdriver.getLogger().info("Verify the Data Explorer item in Explorers in welcome page");
				Assert.assertTrue(DashBoardUtils.isWelcomePageDataExplorerItemExisted(webdriver, "Data Explorer"),
						"'Data Explorer' should in Explorers");
				break;
			case SECSMA:
				webdriver.getLogger().info("'Security' displayed for OSMACC Security Edition");
				Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webdriver, "securityAnalytics"),
						"'Security' servie should in welcome page");

				webdriver
						.getLogger()
						.info("'APM','Log Analytics','Monitoring', 'IT Analytics', 'Orchestraion' and 'Compliance' NOT displayed for OSMACC Security Edition");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "compliance"),
						"'Compliance' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "APM"),
						"'APM' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "LA"),
						"'Log Analytics' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "ITA"),
						"'IT Analytics' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "infraMonitoring"),
						"'Monitoring' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "orchestration"),
						"'Orchestration' servie should not in welcome page");

				webdriver.getLogger().info("Verify the service links in welome page end.");

				//verify the data explorer item in Explorers
				webdriver.getLogger().info("Verify the Data Explorer item in Explorers in welcome page");
				Assert.assertTrue(DashBoardUtils.isWelcomePageDataExplorerItemExisted(webdriver, "Data Explorer"),
						"'Data Explorer' should in Explorers");
				break;
			case OMCTrail:
				webdriver.getLogger().info(
						"'APM','Log Analytics','Monitoring', 'IT Analytics' and 'Orchestraion' displayed for OMC Trail Edition");
				Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webdriver, "APM"), "'APM' servie should in welcome page");
				Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webdriver, "LA"),
						"'Log Analytics' servie should in welcome page");
				Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webdriver, "ITA"),
						"'IT Analytics' servie should in welcome page");
				Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webdriver, "infraMonitoring"),
						"'Monitoring' servie should in welcome page");
				Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webdriver, "orchestration"),
						"'Orchestration' servie should in welcome page");

				webdriver.getLogger().info("'Compliance' and 'Security' NOT displayed for OMC Trail Edition");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "compliance"),
						"'Compliance' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "securityAnalytics"),
						"'Security' servie should not in welcome page");

				webdriver.getLogger().info("Verify the service links in welome page end.");

				//verify the data explorer item in ITA
				webdriver.getLogger().info("Verify the Data Explorer item in ITA in welcome page");
				Assert.assertFalse(DashBoardUtils.isWelcomePageITADataExplorerItemExisted(webdriver, "Data Explorer"),
						"'Data Explorer' should not in ITA");

				//verify the data explorer item in Explorers
				webdriver.getLogger().info("Verify the Data Explorer item in Explorers in welcome page");
				Assert.assertTrue(DashBoardUtils.isWelcomePageDataExplorerItemExisted(webdriver, "Data Explorer"),
						"'Data Explorer' should in Explorers");
				break;
			case OSMACCTrail:
				webdriver.getLogger().info("'Compliance' and 'Security' displayed for OMC Trail Edition");
				Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webdriver, "compliance"),
						"'Compliance' servie should in welcome page");
				Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webdriver, "securityAnalytics"),
						"'Security' servie should in welcome page");

				webdriver
						.getLogger()
				.info("'APM','Log Analytics','Monitoring', 'IT Analytics' and 'Orchestraion' NOT displayed for OSMACC Trail Edition");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "APM"),
						"'APM' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "LA"),
						"'Log Analytics' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "ITA"),
						"'IT Analytics' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "infraMonitoring"),
						"'Monitoring' servie should not in welcome page");
				Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webdriver, "orchestration"),
						"'Orchestration' servie should not in welcome page");

				webdriver.getLogger().info("Verify the service links in welome page end.");

				//verify the data explorer item in Explorers
				webdriver.getLogger().info("Verify the Data Explorer item in Explorers in welcome page");
				Assert.assertTrue(DashBoardUtils.isWelcomePageDataExplorerItemExisted(webdriver, "Data Explorer"),
						"'Data Explorer' should in Explorers");
				break;
			default:
				webdriver.getLogger().info("The tenantType is: " + tenantType);
		}
	}

	private static String trimUrlParameters(String url)
	{
		String baseUrl = null;
		if (url != null) {
			String[] urlComponents = url.split("\\#|\\?");
			baseUrl = urlComponents[0];
		}

		return baseUrl;
	}
}
