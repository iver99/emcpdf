package oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui.util;

import oracle.sysman.emsaas.login.LoginUtils;
import oracle.sysman.emsaas.login.PageUtils;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.AfterMethod;

public class LoginAndLogout
{
	protected static WebDriver webd = null;

	@AfterMethod
	public static void logoutMethod()
	{
		if (webd != null) {			
			LoginUtils.doLogout(webd);
			try {
				webd.shutdownBrowser(true);
			}
			catch (Exception e) {				
				webd.getLogger().warning("Failed to shutdown browser: " + e.getMessage());
			}
		}
	}

	public void login(String testName, String rel, String queryString)
	{
		String tenantID = null, username = null, password = null;
		try {
			tenantID = oracle.sysman.emsaas.login.utils.Utils.getProperty("TENANT_ID");
		}
		catch (Exception e) {			
			tenantID = "emaastesttenant1";
			webd.getLogger().warning("Failed to get tenantID: " + e.getMessage());
		}

		try {
			username = oracle.sysman.emsaas.login.utils.Utils.getProperty("SSO_USERNAME");
		}
		catch (Exception e) {			
			username = "emcsadmin";
			webd.getLogger().warning("Failed to get username: " + e.getMessage());
		}
		try {
			password = oracle.sysman.emsaas.login.utils.Utils.getProperty("SSO_PASSWORD");
		}
		catch (Exception e) {			
			password = "Welcome1!";
			webd.getLogger().warning("Failed to get password: " + e.getMessage());
		}

		login(testName, username, password, tenantID, rel, "OMC-UI-Framework", queryString);
	}

	public void login(String testName, String username, String password, String tenantId, String rel, String servicename,
			String queryString)
	{
		webd = WebDriverUtils.initWebDriver(testName);
		String url = null;
		webd.getLogger().info("before::start to test in LoginAndOut");
		try {
			url = PageUtils.getServiceLink(tenantId, rel, servicename);
		}
		catch (Exception e) {
			webd.getLogger().warning("Failed to get url: " + e.getMessage());
			url = oracle.sysman.emsaas.login.utils.Utils.getProperty("OHS_URL");
			url = url + "/emsaasui/uifwk/test.html";
		}

		String testPropertiesFile = System.getenv("EMAAS_PROPERTIES_FILE");
		webd.getLogger().info("url is " + url + "   properties file is " + testPropertiesFile);
		webd.getLogger().info("after::start to test in LoginAndOut");
		
		if (queryString != null && !queryString.equals("")) {
			url = url + queryString;
			webd.getLogger().info("New url with OMC global context appended is: " + url);
		}
		LoginUtils.doLogin(webd, username, password, tenantId, url);
	}

	public void loginBrandingBar(String testName, String queryString)
	{
		login(testName, "test", queryString);
	}

	public void loginHamburgerMenu(String testName, String queryString)
	{
		webd = WebDriverUtils.initWebDriver(testName);
		String url = null;
		webd.getLogger().info("before::start to test in LoginAndOut");
		url = oracle.sysman.emsaas.login.utils.Utils.getProperty("OHS_URL");
		url = url + "/emsaasui/uifwk/testHamburgerMenu.html";

		String testPropertiesFile = System.getenv("EMAAS_PROPERTIES_FILE");
		webd.getLogger().info("url is " + url + "   properties file is " + testPropertiesFile);
		webd.getLogger().info("after::start to test in LoginAndOut");
		
		if (queryString != null && !queryString.equals("")) {
			url = url + queryString;
			webd.getLogger().info("New url with OMC global context appended is: " + url);
		}
		LoginUtils.doLogin(webd, "emcsadmin", "Welcome1!", "emaastesttenant1", url);
	}
}
