package oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui.util;

import java.util.Arrays;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1180;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

/**
 * @author shangwan
 */
public class CommonUIUtils
{
	private static final Logger LOGGER = LogManager.getLogger(CommonUIUtils.class);
	private static WebDriver driver;

	public static void addWidget(WebDriver driver)
	{
		//click Add Widget icon
		driver.getLogger().info("Verify if Add Widgets icon displayed");
		Assert.assertTrue(driver.isDisplayed(UIControls.SADDWIDGETICON));
		driver.getLogger().info("The buton is:  " + driver.getText(UIControls.SADDWIDGETICON));
		Assert.assertEquals(driver.getText(UIControls.SADDWIDGETICON), "Add");

		driver.getLogger().info("Click the Add Widgets icon");
		driver.waitForElementPresent(UIControls.SADDWIDGETICON);
		driver.click(UIControls.SADDWIDGETICON);

		driver.getLogger().info("Verify the Add Widgets window is opened");
		Assert.assertTrue(driver.isDisplayed("css=" + UIControls.SWIDGETWINDOWTITLE_CSS));
		driver.getLogger().info("The window title is:  " + driver.getText("css=" + UIControls.SWIDGETWINDOWTITLE_CSS));
		Assert.assertTrue(driver.isTextPresent("Add Widgets", "css=" + UIControls.SWIDGETWINDOWTITLE_CSS));
		driver.takeScreenShot();
		driver.savePageToFile();
		driver.getLogger().info("Verify the Add Widgets button is disabled");
		driver.getLogger().info("The button is:  " + driver.getText(UIControls.SADDWIDGETBTN));
		Assert.assertEquals(driver.getText(UIControls.SADDWIDGETBTN), "Add");
		driver.getLogger().info("The button has been:  " + driver.getAttribute(UIControls.SADDWIDGETBTN + "@disabled"));
		Assert.assertNotNull(driver.getAttribute(UIControls.SADDWIDGETBTN + "@disabled"));
		driver.getLogger().info("Verify the select category drop-down list in Add Widgets button is displayed");
		Assert.assertTrue(driver.isElementPresent(UIControls.SCATEGORYSELECT));

		//Add a widget
		driver.getLogger().info("Select a widget and add it to the main page");
		driver.getLogger().info("Select a widget");
		driver.waitForElementPresent(UIControls.SWIDGETSELECT);
		driver.click(UIControls.SWIDGETSELECT);
		driver.getLogger().info("Click Add button");
		driver.waitForElementPresent(UIControls.SADDWIDGETBTN);
		driver.click(UIControls.SADDWIDGETBTN);
		

		driver.getLogger().info("Close the Add Widget window");
		driver.waitForElementPresent(UIControls.SCLOSEWIDGET);
		driver.click(UIControls.SCLOSEWIDGET);
		

		driver.getLogger().info("Verify the widget has been added to main page");
		Assert.assertTrue(driver.isElementPresent(UIControls.SWIDGET));
		
	}

	public static void commonUITestLog(String sDesc)
	{
		//String sStr = "*** Dashboards Common UI TestLog ***:  " + sDesc;
		LOGGER.info(sDesc);
	}

	public static String getAppName()
	{
		String sTenant = oracle.sysman.emsaas.login.utils.Utils.getProperty("TENANT_ID");
		String sUser = oracle.sysman.emsaas.login.utils.Utils.getProperty("SSO_USERNAME");
		return CommonUIUtils.getAppName(sTenant, sUser);
	}

	public static String getAppName(String Tenant, String User)
	{
		String AppName = ""; //With fix to EMCPDF-1220, the application name in Dashboard pages will be blank
		CommonUIUtils.commonUITestLog("The App Name is:" + AppName);
		return AppName;
	}

	public static boolean[] getRoles()
	{
		String sTenant = oracle.sysman.emsaas.login.utils.Utils.getProperty("TENANT_ID");
		String sUser = oracle.sysman.emsaas.login.utils.Utils.getProperty("SSO_USERNAME");
		return CommonUIUtils.getRoles(sTenant, sUser);
	}

	public static boolean[] getRoles(String sTenant, String sUser)
	{
		boolean[] roles = new boolean[4];
		Arrays.fill(roles, false);
		String sAuthToken = oracle.sysman.emsaas.login.utils.Utils.getProperty("SAAS_AUTH_TOKEN");
		String sRolesUrl = oracle.sysman.emsaas.login.utils.Utils.getProperty("TARGETMODEL_SERVICE_SHARD_ENDPOINT");
		if (sRolesUrl == null) {
			CommonUIUtils
					.commonUITestLog("The TARGETMODEL_SERVICE_SHARD_ENDPOINT property value is null ... set it to a different value.");
			sRolesUrl = oracle.sysman.emsaas.login.utils.Utils.getProperty("EMCS_NODE2_HOSTNAME");
			if (sRolesUrl == null) {
				sRolesUrl = null;
			}
			else {
				CommonUIUtils.commonUITestLog("The TARGETMODEL_SERVICE_SHARD_ENDPOINT property is '" + sRolesUrl + "'.");

				sRolesUrl = "http://" + sRolesUrl + ":7004/tm-mgmt/api/v1.1";
			}
		}
		CommonUIUtils.commonUITestLog("The Roles URL is:" + sRolesUrl);

		//String sTempRolsUrl = sRolesUrl.substring(0, sRolesUrl.length() - 16);
		String sTempRolsUrl = sRolesUrl.substring(0, sRolesUrl.indexOf("tm-mgmt"));

		CommonUIUtils.commonUITestLog("The Roles URL is:" + sTempRolsUrl);
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.baseURI = sTempRolsUrl + "authorization/ws/api/v1/";
		CommonUIUtils.commonUITestLog("The base URL is:" + sTempRolsUrl + "authorization/ws/api/v1/");

		Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
				.headers("OAM_REMOTE_USER", sTenant + "." + sUser, "Authorization", sAuthToken).when()
				.get("/roles/grants/getRoles?grantee=" + sTenant + "." + sUser);
		CommonUIUtils.commonUITestLog("The statu code is:" + res1.getStatusCode() + ", The response content is "
				+ res1.jsonPath().get("roleNames"));
		String s_rolename = res1.jsonPath().getString("roleNames");

		String[] ls_rolename = s_rolename.split(",");
		for (int i = 0; i < ls_rolename.length; i++) {
			CommonUIUtils.commonUITestLog(i + " : " + ls_rolename[i]);
			if (ls_rolename[i].contains("APM Administrator")) {
				roles[0] = true;
			}
			else if (ls_rolename[i].contains("IT Analytics Administrator")) {
				roles[1] = true;
			}
			else if (ls_rolename[i].contains("Log Analytics Administrator")) {
				roles[2] = true;
			}
		}
		if (roles[0] || roles[1] || roles[2]) {
			roles[3] = true;
		}

		return roles;
	}

	public static void loadWebDriver(WebDriver webDriver)
	{
		driver = webDriver;
	}

	public static void openWidget(WebDriver driver, boolean isEnabled)
	{
		if (isEnabled) {
			//click Open Widget icon
			driver.getLogger().info("Verify if Open Widgets icon displayed");
			Assert.assertTrue(driver.isDisplayed(UIControls.SADDWIDGETICON));
			driver.getLogger().info("The buton is:  " + driver.getText(UIControls.SADDWIDGETICON));
			Assert.assertEquals(driver.getText(UIControls.SADDWIDGETICON), "Open");

			driver.getLogger().info("Click the Open icon");
			driver.waitForElementPresent(UIControls.SADDWIDGETICON);
			driver.click(UIControls.SADDWIDGETICON);

			driver.getLogger().info("Verify the Open Widgets window is opened");
			Assert.assertTrue(driver.isDisplayed("css=" + UIControls.SWIDGETWINDOWTITLE_CSS));
			driver.getLogger().info("The window title is:  " + driver.getText("css=" + UIControls.SWIDGETWINDOWTITLE_CSS));
			Assert.assertTrue(driver.isTextPresent("Open", "css=" + UIControls.SWIDGETWINDOWTITLE_CSS));

			driver.getLogger().info("Verify the Open button is disabled");
			driver.getLogger().info("The button is:  " + driver.getText(UIControls.SADDWIDGETBTN));
			Assert.assertEquals(driver.getText(UIControls.SADDWIDGETBTN), "Open");
			driver.getLogger().info("The button has been:  " + driver.getAttribute(UIControls.SADDWIDGETBTN + "@disabled"));
			Assert.assertNotNull(driver.getAttribute(UIControls.SADDWIDGETBTN + "@disabled"));
			driver.getLogger().info("Verify the select category drop-down list in Add Widgets button is displayed");
			try {
				driver.getLogger().info("the category display is: " + driver.isDisplayed(UIControls.SCATEGORYSELECT));
			}
			catch (RuntimeException re) {				
				Assert.fail(re.getLocalizedMessage());
			}
			//Assert.assertFalse(driver.isElementPresent(UIControls.sCategorySelect));

			//Open a widget
			if (!"0".equals(driver.getAttribute("css=" + UIControls.SWIDGETDISPLAY + "@childElementCount"))) {
				driver.getLogger().info("Select a widget and open it in the main page");
				driver.waitForElementPresent(UIControls.SWIDGETSELECT);
				driver.click(UIControls.SWIDGETSELECT);

				driver.getLogger().info("Click Open button");
				driver.waitForElementPresent(UIControls.SADDWIDGETBTN);
				driver.click(UIControls.SADDWIDGETBTN);			

				driver.getLogger().info("Verify the widget has been opened in main page");
				Assert.assertTrue(driver.isElementPresent(UIControls.SWIDGET));
			}
		}
		else {
			//verify the Open widget icon is disabled
			driver.getLogger().info("Verify the Open widget icon");
			Assert.assertTrue(driver.isDisplayed(UIControls.SADDWIDGETICON));
			driver.getLogger().info("The buton is:  " + driver.getText(UIControls.SADDWIDGETICON));
			Assert.assertEquals(driver.getText(UIControls.SADDWIDGETICON), "Open");
			driver.getLogger().info("Verify the Open widget icon is disabled");
			driver.getLogger().info("The icon has been:  " + driver.getAttribute(UIControls.SADDWIDGETICON + "@disabled"));
			Assert.assertNotNull(driver.getAttribute(UIControls.SADDWIDGETICON + "@disabled"));
		}
	}

	public static void verifyMenu(WebDriver driver, boolean isAdmin)
	{
		driver.takeScreenShot();
		driver.savePageToFile();
		//verify the menus
		driver.getLogger().info("Verify the Links menu displayed");
		driver.getLogger().info("The Link menu is:  " + driver.getAttribute(UIControls.SLINKSMENU + "@style"));
		Assert.assertNotEquals(driver.getAttribute(UIControls.SLINKSMENU + "@style"), "display: none;");

		if (isAdmin) {

			Assert.assertTrue(driver.isElementPresent(UIControls.SHOME));
			Assert.assertTrue(driver.isElementPresent(UIControls.SHOMEICON));
			Assert.assertTrue(driver.isElementPresent(UIControls.SHOMELABEL));
			Assert.assertTrue(driver.isElementPresent(UIControls.SHOMELINK));

			Assert.assertTrue(driver.isElementPresent(UIControls.SCLOUDSERVICE));
			Assert.assertTrue(driver.isElementPresent(UIControls.SCLOUDSERVICEICON));
			Assert.assertTrue(driver.isElementPresent(UIControls.SCLOUDSERVICELABEL));
			Assert.assertTrue(driver.isElementPresent(UIControls.SCLOUDSERVICELINK));

			Assert.assertTrue(driver.isElementPresent(UIControls.SANALYZER));
			Assert.assertTrue(driver.isElementPresent(UIControls.SANALYZERICON));
			Assert.assertTrue(driver.isElementPresent(UIControls.SANALYZERLABEL));
			Assert.assertTrue(driver.isElementPresent(UIControls.SANALYZERLINK));

			Assert.assertTrue(driver.isElementPresent(UIControls.SADMIN));
			Assert.assertTrue(driver.isElementPresent(UIControls.SADMINICON));
			Assert.assertTrue(driver.isElementPresent(UIControls.SADMINLABEL));
			Assert.assertTrue(driver.isElementPresent(UIControls.SADMINLINK));
		}
		else {
			Assert.assertTrue(driver.isElementPresent(UIControls.SHOME));
			Assert.assertTrue(driver.isElementPresent(UIControls.SHOMEICON));
			Assert.assertTrue(driver.isElementPresent(UIControls.SHOMELABEL));
			Assert.assertTrue(driver.isElementPresent(UIControls.SHOMELINK));

			Assert.assertTrue(driver.isElementPresent(UIControls.SCLOUDSERVICE));
			Assert.assertTrue(driver.isElementPresent(UIControls.SCLOUDSERVICEICON));
			Assert.assertTrue(driver.isElementPresent(UIControls.SCLOUDSERVICELABEL));
			Assert.assertTrue(driver.isElementPresent(UIControls.SCLOUDSERVICELINK));

			Assert.assertTrue(driver.isElementPresent(UIControls.SANALYZER));
			Assert.assertTrue(driver.isElementPresent(UIControls.SANALYZERICON));
			Assert.assertTrue(driver.isElementPresent(UIControls.SANALYZERLABEL));
			Assert.assertTrue(driver.isElementPresent(UIControls.SANALYZERLINK));

			Assert.assertFalse(driver.isElementPresent(UIControls.SADMIN));
			Assert.assertFalse(driver.isElementPresent(UIControls.SADMINICON));
			Assert.assertFalse(driver.isElementPresent(UIControls.SADMINLABEL));
			Assert.assertFalse(driver.isElementPresent(UIControls.SADMINLINK));
		}
	}

	public static void verifyNoLinksMenu(WebDriver driver)
	{
		driver.waitForElementNotVisible(UIControls.SLINKSMENU);		
		Assert.assertEquals(driver.getAttribute(UIControls.SLINKSMENU + "@style"), "display: none;");
	}

	public static void verifyPageContent(WebDriver driver, String sAppName)
	{
		verifyPageContent(driver, sAppName, false);
	}

	public static void verifyPageContent(WebDriver driver, String sAppName, boolean hamburgerEnabled)
	{
		//wait for the mockup page loaded
		driver.getLogger().info("Wait for the mockup page loaded...");
		WaitUtil.waitForPageFullyLoaded(driver);

		//verify the product name,app name,content of page
		driver.getLogger().info("Verify the page content");
		//Oracle logo
		Assert.assertTrue(driver.isElementPresent("css=" + UIControls.SORACLEIMAGE));
		Assert.assertEquals(driver.getAttribute("css=" + UIControls.SORACLEIMAGE + "@alt"), "Oracle");
		//Product title
		Assert.assertTrue(driver.isElementPresent("css=" + UIControls.SPRODUCTTEXT_CSS));
		String productTitle = "MANAGEMENT CLOUD";
		driver.waitForText("css=" + UIControls.SPRODUCTTEXT_CSS, productTitle);
		driver.getLogger().info("The Product is:  " + driver.getText("css=" + UIControls.SPRODUCTTEXT_CSS));
		Assert.assertEquals(driver.getText("css=" + UIControls.SPRODUCTTEXT_CSS), productTitle);
		//Application names
		Assert.assertTrue(driver.isElementPresent("css=" + UIControls.SAPPTEXT_CSS));
		driver.waitForText("css=" + UIControls.SAPPTEXT_CSS, sAppName);
		driver.getLogger().info("The App is:  " + sAppName);
		Assert.assertEquals(driver.getText("css=" + UIControls.SAPPTEXT_CSS), sAppName);
		//Page title
		Assert.assertTrue(driver.isElementPresent(UIControls.SPAGETEXT));
		String pageTitle = "Sample page for OMC UI Framework components testing only";
		driver.waitForText(UIControls.SPAGETEXT, pageTitle);
		driver.getLogger().info("The page content is:  " + driver.getText(UIControls.SPAGETEXT));
		Assert.assertEquals(driver.getText(UIControls.SPAGETEXT), pageTitle);
		//Buttons
		if (hamburgerEnabled) {
			Assert.assertTrue(driver.isElementPresent("css=" + DashBoardPageId_1180.HAMBURGERMENU_ICON_CSS));
		}
		else {
			Assert.assertTrue(driver.isElementPresent(UIControls.SCOMPASSICON));
		}
		Assert.assertTrue(driver.isElementPresent(UIControls.SADDWIDGETICON));
	}

	public static void verifyURL(WebDriver webdriver, String url)
	{
		String currurl = webdriver.getCurrentUrl();
		webdriver.getLogger().info("the origin url = " + currurl);
		String tmpurl = trimUrlParameters(currurl.substring(currurl.indexOf("emsaasui") + 9));
		webdriver.getLogger().info("the url without para = " + tmpurl);
		Assert.assertEquals(tmpurl, url);
	}

	public static void verifyURL_WithPara(WebDriver webdriver, String url)
	{
		webdriver.getLogger().info("the expected relative url = " + url);
		String currurl = webdriver.getCurrentUrl();
		webdriver.getLogger().info("the current url = " + currurl);
		String tmpurl = currurl.substring(currurl.indexOf("emsaasui") + 9);
		webdriver.getLogger().info("the relative url to compare = " + tmpurl);
		Assert.assertTrue(tmpurl.contains(url), tmpurl + " does NOT contain " + url);
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
