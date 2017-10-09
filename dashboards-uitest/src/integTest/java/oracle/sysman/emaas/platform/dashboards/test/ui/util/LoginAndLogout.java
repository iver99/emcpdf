package oracle.sysman.emaas.platform.dashboards.test.ui.util;

import oracle.sysman.emsaas.login.LoginUtils;
import oracle.sysman.emsaas.login.PageUtils;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.testng.annotations.AfterMethod;

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

	public void customlogin(String testName, String customUser)
	{
		String tenantID = null, username = null;
		try {
			tenantID = oracle.sysman.emsaas.login.utils.Utils.getProperty("TENANT_ID");
		}
		catch (Exception e) {			
			tenantID = "emaastesttenant1";			
		}
		username = customUser;
		login(testName, username, "Welcome1!", tenantID, "home", "Dashboard-UI");

	}

	//added by Iris begin
	public void customlogin(String testName, String customUser, String newTenantID)
	{
		String tenantID = null, username = null;
		try {
			tenantID = newTenantID;
		}
		catch (Exception e) {			
			tenantID = "emaastesttenant1";
		}
		username = customUser;
		login(testName, username, "Welcome1!", tenantID, "home", "Dashboard-UI");
	}

	//added by Iris end

	public void login(String testName)
	{
		login(testName, "home");
	}

	public void login(String testName, String rel)
	{
		String tenantID = null, username = null, password = null;
		try {
			tenantID = oracle.sysman.emsaas.login.utils.Utils.getProperty("TENANT_ID");
		}
		catch (Exception e) {			
			tenantID = "emaastesttenant1";
		}

		try {
			username = oracle.sysman.emsaas.login.utils.Utils.getProperty("SSO_USERNAME");
		}
		catch (Exception e) {
			username = "emcsadmin";
		}
		try {
			password = oracle.sysman.emsaas.login.utils.Utils.getProperty("SSO_PASSWORD");
		}
		catch (Exception e) {			
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
			url = PageUtils.getServiceLink(tenantId, rel, servicename);
		}
		catch (Exception e) {
			url = oracle.sysman.emsaas.login.utils.Utils.getProperty("OHS_URL") + "/emsaasui/emcpdfui/home.html";
		}

		String testPropertiesFile = System.getenv("EMAAS_PROPERTIES_FILE");
		webd.getLogger().info("url is " + url + "   properties file is " + testPropertiesFile);
		webd.getLogger().info("after::start to test in LoginAndOut");
		
		if (!webd.getCurrentUrl().equals(url) && !webd.getCurrentUrl().contains("omcCtx=")) {
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

	public void loginErrorPage(String testName, String url)
	{
		String tenantID = null, username = null, password = null;
		try {
			tenantID = oracle.sysman.emsaas.login.utils.Utils.getProperty("TENANT_ID");
		}
		catch (Exception e) {
			tenantID = "emaastesttenant1";
		}

		try {
			username = oracle.sysman.emsaas.login.utils.Utils.getProperty("SSO_USERNAME");
		}
		catch (Exception e) {
			username = "emcsadmin";
		}
		try {
			password = oracle.sysman.emsaas.login.utils.Utils.getProperty("SSO_PASSWORD");
		}
		catch (Exception e) {
			password = "Welcome1!";
		}

		webd = WebDriverUtils.initWebDriver(testName);
		
		webd.getLogger().info("after::start to test in LoginAndOut");
		
		if (!webd.getCurrentUrl().equals(url) && !webd.getCurrentUrl().contains("omcCtx=")) {
			//Append omc context into login url
			if (!url.contains("omcCtx=")) {
				url = url
						+ (url.indexOf("?") > 0 ? "&" : "?")
						+ "omcCtx=startTime%3D1478045700000%26endTime%3D1478240100000%26compositeMEID%3DEE380645A9711FFF881A146A00C98324";
				webd.getLogger().info("New url with OMC global context appended is: " + url);
			}
			LoginUtils.doLogin(webd, username, password, tenantID, url);
		}
	}

	public void loginV4(String testName, String rel)
	{
		String tenantID = null, username = null, password = null;
		try {
			tenantID = OnboardV4Tenant.getTenantID();
		}
		catch (Exception e) {
			tenantID = "emaastesttenant1";
		}

		try {
			username = oracle.sysman.emsaas.login.utils.Utils.getProperty("SSO_USERNAME");
		}
		catch (Exception e) {
			username = "emcsadmin";
		}
		try {
			password = oracle.sysman.emsaas.login.utils.Utils.getProperty("SSO_PASSWORD");
		}
		catch (Exception e) {
			password = "Welcome1!";
		}

		loginV4(testName, username, password, tenantID, rel, "Dashboard-UI");
	}

	public void loginV4(String testName, String username, String password, String tenantId, String rel, String servicename)
	{
		webd = WebDriverUtils.initWebDriver(testName);
		String url = null;
		webd.getLogger().info("before::start to test in LoginAndOut");
		try {
			url = PageUtils.getServiceLink(tenantId, rel, servicename);
		}
		catch (Exception e) {
			url = oracle.sysman.emsaas.login.utils.Utils.getProperty("OHS_URL") + "/emsaasui/emcpdfui/home.html";
		}

		String testPropertiesFile = System.getenv("EMAAS_PROPERTIES_FILE");
		webd.getLogger().info("url is " + url + "   properties file is " + testPropertiesFile);
		webd.getLogger().info("after::start to test in LoginAndOut");
		
		url = url + "?DOMAIN-INSTANCE-NAME=" + OnboardV4Tenant.getInstanceID();
		if (!webd.getCurrentUrl().equals(url) && !webd.getCurrentUrl().contains("omcCtx=")) {
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