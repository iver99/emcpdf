package oracle.sysman.emaas.platform.dashboards.testsdk.util;

import oracle.sysman.emsaas.login.LoginUtils;
import oracle.sysman.emsaas.login.PageUtils;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import oracle.sysman.qatool.uifwk.utils.Utils;

public class LoginAndLogout
{
	protected static WebDriver webd = null;

	@AfterMethod
	public static void logoutMethod()
	{
		if (webd != null) {
			webd.getLogger().info("start to logout");
			LoginUtils.doLogout(webd);
		}
	}

	public void login(String testName)
	{
		login(testName, "home");
	}

	public void login(String testName, String rel)
	{
		String tenantID = null, username = null, password = null;
		try {
			tenantID = Utils.getProperty("TENANT_ID");
		}
		catch (Exception e) {
			e.printStackTrace();
			tenantID = "emaastesttenant1";
		}

		try {
			username = Utils.getProperty("SSO_USERNAME");

		}
		catch (Exception e) {
			e.printStackTrace();
			username = "emcsadmin";
		}
		try {
			password = Utils.getProperty("SSO_PASSWORD");

		}
		catch (Exception e) {
			e.printStackTrace();
			password = "Welcome1!";
		}

		login(testName, username, password, tenantID, rel, "Dashboard-UI");
	}

	public void login(String testName, String username, String password, String tenantId, String rel, String servicename)
	{
		webd = WebDriverUtils.initWebDriver(testName);
		String url = null;
		webd.getLogger().info("before::start to test in LoginAndOut");
		try {
			url = Utils.getProperty("OMCS_DASHBOARD_URL");
		}
		catch (Exception e) {
			e.printStackTrace();
			//			url = oracle.sysman.emsaas.login.utils.Utils.getProperty("OMCS_DASHBOARD_URL");
			url = Utils.getProperty("OHS_URL") + "/emsaasui/emcpdfui/home.html";
		}

		String testPropertiesFile = System.getenv("EMAAS_PROPERTIES_FILE");
		webd.getLogger().info("url is " + url + "   properties file is " + testPropertiesFile);
		webd.getLogger().info("after::start to test in LoginAndOut");
		// if the ui have been login, do not login ,again
		//		if (!webd.getWebDriver().getCurrentUrl().equals(url)) {
		if (!webd.getWebDriver().getCurrentUrl().equals(url) && !webd.getWebDriver().getCurrentUrl().contains("omcCtx=")) {
			//Append omc context into login url
			if (!url.contains("omcCtx=")) {
				url = url
						+ (url.indexOf("?") > 0 ? "&" : "?")
						+ "omcCtx=compositeType%3Domc_generic_system%26compositeName%3D%252FSOA1213_base_domain%252Fbase_domain%252Fsoa_server1%252Fsoa-infra_System%26compositeMEID%3D8426448730BDF663A9806A69AA2C445B%26entityMEIDs%3D132DBDE75046DEBC18DDC79CFCB04729";
				webd.getLogger().info("New url with OMC global context appended is: " + url);
			}
			LoginUtils.doLogin(webd, username, password, tenantId, url);
		}
	}
}