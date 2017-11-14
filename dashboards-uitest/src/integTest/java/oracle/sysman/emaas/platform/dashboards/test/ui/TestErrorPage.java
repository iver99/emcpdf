package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestErrorPage extends LoginAndLogout
{
	private String url = "";
	private String servicename = "";

	private final String KEY_COMMON_PAGE_NO_SUBS = "COMMON_ERROR_PAGE_NOT_FOUND_NO_SUBS_MSG";
	private final String KEY_COMMON_PAGE_NOT_FOUND = "COMMON_ERROR_PAGE_NOT_FOUND_MSG";
	private final String KEY_COMMON_ACCESS_NO_PERMISSION = "COMMON_ERROR_PAGE_NO_ACCESS_NO_PERMISSION_MSG";
	private final String KEY_COMMON_ACCESS_NO_SUBS = "COMMON_ERROR_PAGE_NO_ACCESS_NO_SUBS_MSG";

	private final String KEY_DBS_PAGE_NOT_FOUND = "DBS_ERROR_PAGE_NOT_FOUND_MSG";
	private final String KEY_DBS_PAGE_NO_SUBS = "DBS_ERROR_PAGE_NOT_FOUND_NO_SUBS_MSG";
	private final String KEY_DBS_INVALID = "DBS_ERROR_ORA_EMSAAS_USERNAME_AND_TENANTNAME_INVALID";
	private final String KEY_DBS_INTERNAL = "DBS_ERROR_INTERNAL_ERROR";

	private final String MSG_DBS_PAGE_NOT_FOUND = "Sorry, the page you have requested either doesn't exist or you do not have access to it.";
	private final String MSG_DBS_PAGE_NO_SUBS = "Sorry, the page you have requested either doesn't exist or you do not have access to it. Reason: \"No service is subscribed\"";
	private final String MSG_DBS_INVALID = "Error: failed to retrieve user or tenant.";
	private final String MSG_DBS_INTERNAL = "Oracle Management Cloud service received a bad request and had an internal error. Try to access your service later.";

	private final String MSG_COMMON_PAGE_NOT_FOUND = "Sorry, the page you've requested is not available.";

	private final String MSG_COMMON_PAGE_NO_SUBS = "You currently don't have access to this page because you're not subscribed to the service being accessed.";
	private final String MSG_COMMON_PAGE_NO_SUBS_WITH_SERVICESNAME = "You currently don't have access to this page because you're not subscribed to ";

	private final String MSG_COMMON_ACCESS_NO_PERMISSION = "You don't have permissions to access to this page. Contact your Administrator.";
	private final String MSG_COMMON_ACCESS_NO_PERMISSION_WITH_SERVICESNAME = "You don't have permissions to access to this page. Contact your ";

	private final String MSG_COMMON_ACCESS_NO_SUBS = "You currently don't have access to this page because you're not subscribed to the service being accessed.";
	private final String MSG_COMMON_ACCESS_NO_SUBS_WITH_SERVICESNAME = "You currently don't have access to this page because you're not subscribed to ";

	private final String MSG_URL = "Requested URL is: https://invalidurl.com";

	@BeforeClass
	public void getURL()
	{
		String tmpURL = "";
		try {
			tmpURL = oracle.sysman.emsaas.login.utils.Utils.getProperty("OHS_URL");
			url = tmpURL + "/emsaasui/emcpdfui/error.html";
		}
		catch (Exception e) {
			url = "";
		}

	}

	public void initTest(String testName, String urlPara) throws Exception
	{
		loginErrorPage(this.getClass().getName() + "." + testName, url + urlPara);
		DashBoardUtils.loadWebDriver(webd);
	}

	@Test(alwaysRun = true)
	public void testDefaultErrorPage()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_DBS_PAGE_NOT_FOUND);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testDefaultErrorPage_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?invalidUrl=https://invalidurl.com");

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_DBS_PAGE_NOT_FOUND);
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_PERMISSION()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_PERMISSION);

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_PERMISSION);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_PERMISSION_APM()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_PERMISSION
					+ "&service=APM");
			servicename = "Application Performance Monitoring";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_PERMISSION_WITH_SERVICESNAME
					+ servicename + " Administrator.");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_PERMISSION_APM_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_PERMISSION
					+ "&service=APM&invalidUrl=https://invalidurl.com");
			servicename = "Application Performance Monitoring";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_PERMISSION_WITH_SERVICESNAME
					+ servicename + " Administrator.");
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_PERMISSION_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_PERMISSION
					+ "&invalidUrl=https://invalidurl.com");

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_PERMISSION);
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_PERMISSION_ITA()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_PERMISSION
					+ "&service=ITAnalytics");
			servicename = "IT Analytics";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_PERMISSION_WITH_SERVICESNAME
					+ servicename + " Administrator.");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_PERMISSION_ITA_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_PERMISSION
					+ "&service=ITAnalytics&invalidUrl=https://invalidurl.com");
			servicename = "IT Analytics";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_PERMISSION_WITH_SERVICESNAME
					+ servicename + " Administrator.");
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_PERMISSION_LA()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_PERMISSION
					+ "&service=LogAnalytics");
			servicename = "Log Analytics";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_PERMISSION_WITH_SERVICESNAME
					+ servicename + " Administrator.");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_PERMISSION_LA_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_PERMISSION
					+ "&service=LogAnalytics&invalidUrl=https://invalidurl.com");
			servicename = "Log Analytics";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_PERMISSION_WITH_SERVICESNAME
					+ servicename + " Administrator.");
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}
	
	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_PERMISSION_SECURITY()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_PERMISSION
					+ "&service=SecurityAnalytics");
			servicename = "Security Monitoring and Analytics";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_PERMISSION_WITH_SERVICESNAME
					+ servicename + " Administrator.");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_PERMISSION_SECURITY_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_PERMISSION
					+ "&service=SecurityAnalytics&invalidUrl=http");
			servicename = "Security Monitoring and Analytics";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_PERMISSION_WITH_SERVICESNAME
					+ servicename + " Administrator.");
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_SUBS()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_SUBS);

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_SUBS);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_SUBS_APM()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_SUBS
					+ "&service=APM");
			servicename = "Application Performance Monitoring";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_SUBS_WITH_SERVICESNAME
					+ servicename + ".");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_SUBS_APM_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_SUBS
					+ "&service=APM&invalidUrl=https://invalidurl.com");
			servicename = "Application Performance Monitoring";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_SUBS_WITH_SERVICESNAME
					+ servicename + ".");
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_SUBS_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_SUBS
					+ "&invalidUrl=https://invalidurl.com");

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_SUBS);
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_SUBS_ITA()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_SUBS
					+ "&service=ITAnalytics");
			servicename = "IT Analytics";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_SUBS_WITH_SERVICESNAME
					+ servicename + ".");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_SUBS_ITA_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_SUBS
					+ "&service=ITAnalytics&invalidUrl=https://invalidurl.com");
			servicename = "IT Analytics";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_SUBS_WITH_SERVICESNAME
					+ servicename + ".");
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_SUBS_LA()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_SUBS
					+ "&service=LogAnalytics");
			servicename = "Log Analytics";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_SUBS_WITH_SERVICESNAME
					+ servicename + ".");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_SUBS_LA_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_SUBS
					+ "&service=LogAnalytics&invalidUrl=https://invalidurl.com");
			servicename = "Log Analytics";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_SUBS_WITH_SERVICESNAME
					+ servicename + ".");
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}
	
	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_SUBS_SECURITY()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_SUBS
					+ "&service=SecurityAnalytics");
			servicename = "Security Monitoring and Analytics";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_SUBS_WITH_SERVICESNAME
					+ servicename + ".");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_ACCESS_NO_SUBS_SECURITY_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_ACCESS_NO_SUBS
					+ "&service=SecurityAnalytics&invalidUrl=https://invalidurl.com");
			servicename = "Security Monitoring and Analytics";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_ACCESS_NO_SUBS_WITH_SERVICESNAME
					+ servicename + ".");
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_PAGE_NO_SUBS()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_PAGE_NO_SUBS);

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_PAGE_NO_SUBS);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_PAGE_NO_SUBS_APM()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_PAGE_NO_SUBS
					+ "&service=APM");
			servicename = "Application Performance Monitoring";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_PAGE_NO_SUBS_WITH_SERVICESNAME
					+ servicename + ".");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_PAGE_NO_SUBS_APM_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_PAGE_NO_SUBS
					+ "&service=APM&invalidUrl=https://invalidurl.com");
			servicename = "Application Performance Monitoring";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_PAGE_NO_SUBS_WITH_SERVICESNAME
					+ servicename + ".");
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_PAGE_NO_SUBS_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_PAGE_NO_SUBS
					+ "&invalidUrl=https://invalidurl.com");

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_PAGE_NO_SUBS);
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_PAGE_NO_SUBS_ITA()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_PAGE_NO_SUBS
					+ "&service=ITAnalytics");
			servicename = "IT Analytics";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_PAGE_NO_SUBS_WITH_SERVICESNAME
					+ servicename + ".");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_PAGE_NO_SUBS_ITA_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_PAGE_NO_SUBS
					+ "&service=ITAnalytics&invalidUrl=https://invalidurl.com");
			servicename = "IT Analytics";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_PAGE_NO_SUBS_WITH_SERVICESNAME
					+ servicename + ".");
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_PAGE_NO_SUBS_LA()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_PAGE_NO_SUBS
					+ "&service=LogAnalytics");
			servicename = "Log Analytics";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_PAGE_NO_SUBS_WITH_SERVICESNAME
					+ servicename + ".");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_PAGE_NO_SUBS_LA_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_PAGE_NO_SUBS
					+ "&service=LogAnalytics&invalidUrl=https://invalidurl.com");
			servicename = "Log Analytics";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_PAGE_NO_SUBS_WITH_SERVICESNAME
					+ servicename + ".");
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}
	
	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_PAGE_NO_SUBS_SECURITY()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_PAGE_NO_SUBS
					+ "&service=SecurityAnalytics");
			servicename = "Security Monitoring and Analytics";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_PAGE_NO_SUBS_WITH_SERVICESNAME
					+ servicename + ".");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_PAGE_NO_SUBS_SECURITY_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_PAGE_NO_SUBS
					+ "&service=SecurityAnalytics&invalidUrl=https://invalidurl.com");
			servicename = "Security Monitoring and Analytics";

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_PAGE_NO_SUBS_WITH_SERVICESNAME
					+ servicename + ".");
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_PAGE_NOT_FOUND()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_PAGE_NOT_FOUND);

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_PAGE_NOT_FOUND);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_COMMON_PAGE_NOT_FOUND_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_COMMON_PAGE_NOT_FOUND
					+ "&invalidUrl=https://invalidurl.com");

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_COMMON_PAGE_NOT_FOUND);
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_DBS_INTERNAL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_DBS_INTERNAL);

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_DBS_INTERNAL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_DBS_INTERNAL_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_DBS_INTERNAL + "&invalidUrl=https://invalidurl.com");

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_DBS_INTERNAL);
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_DBS_INVALID()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_DBS_INVALID);

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_DBS_INVALID);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_DBS_INVALID_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_DBS_INVALID + "&invalidUrl=https://invalidurl.com");

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_DBS_INVALID);
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_DBS_PAGE_NO_SUBS()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_DBS_PAGE_NO_SUBS);

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_DBS_PAGE_NO_SUBS);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_DBS_PAGE_NO_SUBS_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_DBS_PAGE_NO_SUBS
					+ "&invalidUrl=https://invalidurl.com");

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_DBS_PAGE_NO_SUBS);
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_DBS_PAGE_NOT_FOUND()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_DBS_PAGE_NOT_FOUND);

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_DBS_PAGE_NOT_FOUND);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPage_DBS_PAGE_NOT_FOUND_invalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_DBS_PAGE_NOT_FOUND
					+ "&invalidUrl=https://invalidurl.com");

			//verify the error message
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS), MSG_DBS_PAGE_NOT_FOUND);
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), MSG_URL);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(alwaysRun = true)
	public void testErrorPageInsecurityInvalidURL()
	{
		try {
			//init test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?msg=" + KEY_DBS_PAGE_NOT_FOUND
					+ "&invalidUrl=invalidurl.com");
			webd.waitForElementPresent("css=" + PageId.ERRORMESSAGE_CSS);
			Assert.assertFalse(webd.isDisplayed("css=" + PageId.ERRORURL_CSS));
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}
}