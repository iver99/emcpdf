package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class TestBrandingBar_BasicTest extends LoginAndLogout
{

	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);
	}

	@Test(alwaysRun = true)
	public void testAdminConsoleLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAdminConsoleLink");
		WaitUtil.waitForPageFullyLoaded(webd);
		// Administration link for Administration Console UI
		BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ADMINCONSOLE);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "admin-console/ac/adminConsole.html");
	}

	//@Test(alwaysRun = true)
	public void testAdminLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAdminLink");
		WaitUtil.waitForPageFullyLoaded(webd);
		if (!DashBoardUtils.isHamburgerMenuEnabled(webd)) {

		}
		else {
			BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.ROOT_MENU_ADMIN);
			WaitUtil.waitForPageFullyLoaded(webd);
			DashBoardUtils.verifyURL(webd, "eventUi/rules/html/rules-dashboard.html");
		}
	}

	@Test(alwaysRun = true)
	public void testAgentsLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAgentsLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Agents link
		BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "tenantmgmt/services/customersoftware");
	}

	@Test(alwaysRun = true)
	public void testAlertRules()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAlertRules");
		WaitUtil.waitForPageFullyLoaded(webd);

		// alert rules link
		BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "eventUi/rules/html/rules-dashboard.html");
	}

	@Test(alwaysRun = true)
	public void testAlerts()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAlerts");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Alets link
		BrandingBarUtil.visitApplicationHome(webd, BrandingBarUtil.NAV_LINK_TEXT_HOME_ALERTS);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "eventUi/console/html/event-dashboard.html");
	}

	@Test(alwaysRun = true)
	public void testAPMLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAPMLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// APM link
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_APM);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "apmUi/index.html");
	}

	@Test(alwaysRun = true)
	public void testComplianceLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testComplianceLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		//check if Compliance link is available
		Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_COMP));

		// Compliance link
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_COMP);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL_WithPara(webd, "complianceuiservice/index.html");
	}

	@Test(alwaysRun = true)
	public void testDashBoardHomeLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testDashBoardHomeLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// dashboardHome link
		BrandingBarUtil.visitDashboardHome(webd);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcpdfui/home.html");
	}

	@Test(alwaysRun = true)
	public void testHomeLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomeLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Home link
		BrandingBarUtil.visitMyHome(webd);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcpdfui/welcome.html");
	}

	@Test(alwaysRun = true)
	public void testInfrastructureMonitoringLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in InfrastructureMonitoringLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		//check if Monitoring link is available
		Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_IM));

		// Monitoring link
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_IM);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "monitoringservicesui/cms/index.html");
	}

	@Test(alwaysRun = true)
	public void testITALink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testITALink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// IT Analytics link,check checkbox
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_ITA);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/home.html");
	}

	@Test(alwaysRun = true)
	public void testLALink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testLALink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Log Analytics link
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_LA);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emlacore/html/log-analytics-search.html");
	}

	@Test(alwaysRun = true)
	public void testLogLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testLogLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Log link
		BrandingBarUtil.visitApplicationVisualAnalyzer(webd, BrandingBarUtil.NAV_LINK_TEXT_VA_LA);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emlacore/html/log-analytics-search.html");
	}

	@Test(alwaysRun = true)
	public void testOrchestrationLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testOrchestrationLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		//check if Orchestration link is available
		Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_OCS));

		// Orchestration link
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_OCS);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL_WithPara(webd, "cosUi/wfDashboard.html");
	}

	@Test(alwaysRun = true)
	public void testSearchLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testSearchLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Target link
		BrandingBarUtil.visitApplicationVisualAnalyzer(webd, BrandingBarUtil.NAV_LINK_TEXT_VA_TA);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcta/ta/analytics.html");
	}

	@Test(alwaysRun = true)
	public void testSecurityLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testSecurityLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		//check if Security link is available
		Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_SECU));

		// Security link
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_SECU);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL_WithPara(webd, "saui/web/index.html");
	}
}
