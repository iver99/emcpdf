package oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util;

import oracle.sysman.emsaas.login.LoginUtils;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.openqa.selenium.JavascriptExecutor;

public class LoginAndLogout
{
	protected static WebDriver webd = null;

	//@AfterMethod
	public static void logoutMethod()
	{
		if (webd != null) {			
			LoginUtils.doLogout(webd);
			try {
				webd.shutdownBrowser(true);
			}
			catch (Exception e) {				
				webd.getLogger().warning("Failed to shutdown browser" + e.getMessage());
			}
		}
	}

	public void login(String testName, String mockupPageURL)
	{
		webd = WebDriverUtils.initWebDriver(testName);
		String url = null;
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
		webd.getLogger().info("before::start to test in LoginAndOut");
		url = oracle.sysman.emsaas.login.utils.Utils.getProperty("OHS_URL");
		url = url + "/emsaasui/uifwk/";

		String testPropertiesFile = System.getenv("EMAAS_PROPERTIES_FILE");
		webd.getLogger().info("url is " + url + "   properties file is " + testPropertiesFile);
		webd.getLogger().info("after::start to test in LoginAndOut");

		if (mockupPageURL != null && !mockupPageURL.equals("")) {
			url = url + mockupPageURL;
			webd.getLogger().info("New url with OMC global context appended is: " + url);
		}
		LoginUtils.doLogin(webd, username, password, tenantID, url);
	}
}
