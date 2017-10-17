/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui.util.CommonUIUtils;
import oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui.util.LoginAndLogout;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class HamburgerMenu_BasicTest extends LoginAndLogout
{
	public void initTest(String testName, String queryString)
	{

		loginHamburgerMenu(this.getClass().getName() + "." + testName, queryString);
		CommonUIUtils.loadWebDriver(webd);
	}

	@Test(alwaysRun = true)
	public void testClickAdminMenuItem()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("start to test in testClickAdminMenuItem");
		WaitUtil.waitForPageFullyLoaded(webd);

		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.ROOT_MENU_ADMIN);
		WaitUtil.waitForPageFullyLoaded(webd);
		Assert.assertEquals(BrandingBarUtil.getCurrentMenuHeader(webd), "Administration");
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, BrandingBarUtil.GLOBAL_ADMIN_MENU_AGENTS));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, BrandingBarUtil.GLOBAL_ADMIN_MENU_ALERT_RULES));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, BrandingBarUtil.GLOBAL_ADMIN_MENU_ENTITIES_CFG));
		//CommonUIUtils.verifyURL(webd, "eventUi/rules/html/rules-dashboard.html");
	}

	@Test(alwaysRun = true)
	public void testClickAgentsMenuItem()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("start to test in testClickAgentsMenuItem");
		WaitUtil.waitForPageFullyLoaded(webd);

		BrandingBarUtil.expandSubMenu(webd, BrandingBarUtil.ROOT_MENU_ADMIN);
		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.GLOBAL_ADMIN_MENU_AGENTS);
		WaitUtil.waitForPageFullyLoaded(webd);
		//verify the url of opened page
		CommonUIUtils.verifyURL(webd, "tenantmgmt/services/customersoftware");
	}

	@Test(alwaysRun = true)
	public void testClickAlertRulesMenuItem()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("start to test in testClickAlertRulesMenuItem()");
		WaitUtil.waitForPageFullyLoaded(webd);

		BrandingBarUtil.expandSubMenu(webd, BrandingBarUtil.ROOT_MENU_ADMIN);
		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.GLOBAL_ADMIN_MENU_ALERT_RULES);
		WaitUtil.waitForPageFullyLoaded(webd);
		CommonUIUtils.verifyURL(webd, "eventUi/rules/html/rules-dashboard.html");
	}

	@Test(alwaysRun = true)
	public void testClickNotificationChannelsMenuItem()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("start to test in testClickNotificationChannelsMenuItem()");
		WaitUtil.waitForPageFullyLoaded(webd);

		BrandingBarUtil.expandSubMenu(webd, BrandingBarUtil.ROOT_MENU_ADMIN);
		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.GLOBAL_ADMIN_MENU_NOTIFICATION_CHANNELS);
		WaitUtil.waitForPageFullyLoaded(webd);
		CommonUIUtils.verifyURL_WithPara(webd, "eventUi/channels/html/channels-dashboard.html");
	}

	@Test(alwaysRun = true)
	public void testClickAddEntityMenuItem()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
	    webd.getLogger().info("start to test in testClickAddEntityMenuItem()");
	    WaitUtil.waitForPageFullyLoaded(webd);
	    BrandingBarUtil.expandSubMenu(webd, BrandingBarUtil.ROOT_MENU_ADMIN);
	    webd.capturePageState();
	    String url = webd.getAttribute("omc_root_admin_addentity@href");
	    webd.getLogger().info("The URL is:"+ url);
	    webd.capturePageState();
	    BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.GLOBAL_ADMIN_MENU_ADD_ENTITY);
	    WaitUtil.waitForPageFullyLoaded(webd);
	    String currurl = webd.getWebDriver().getCurrentUrl();
	    String currurl1 = currurl.substring(0,currurl.indexOf("&"));
	    if(currurl1.equalsIgnoreCase(url)){
	    	webd.getLogger().info("The url is correct");	
	    }
	    else{
	    Assert.assertTrue(currurl1.contains("cmsConfigureEntityDiscovery"), "The url is incorrect");
	    }
	}

	@Test(alwaysRun = true)
	public void testClickCloudDiscoveryMenuItem()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("start to test in testClickCloudDiscoveryMenuItem()");
		WaitUtil.waitForPageFullyLoaded(webd);

		BrandingBarUtil.expandSubMenu(webd, BrandingBarUtil.ROOT_MENU_ADMIN);
		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.GLOBAL_ADMIN_MENU_CLOUD_DISCOVERY);
		WaitUtil.waitForPageFullyLoaded(webd);
		CommonUIUtils.verifyURL_WithPara(webd, "monitoringservicesui/cms/global-index.html?root=cmsCloudProfilesDashboard");
	}

	@Test(alwaysRun = true)
	public void testClickAlertsMenuItem()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("start to test in testClickAlertRulesMenuItem()");
		WaitUtil.waitForPageFullyLoaded(webd);

		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.ROOT_MENU_ALERTS);
		WaitUtil.waitForPageFullyLoaded(webd);
		CommonUIUtils.verifyURL(webd, "eventUi/console/html/event-dashboard.html");
	}

	@Test(alwaysRun = true)
	public void testClickAPMMenuItem()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("start to test in testClickAPMMenuItem");
		WaitUtil.waitForPageFullyLoaded(webd);

		// APM MenuItem
		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.ROOT_MENU_APM);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		CommonUIUtils.verifyURL(webd, "apmUi/index.html");
	}

	@Test(alwaysRun = true)
	public void testClickComplianceMenuItem()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("start to test in testClickComplianceMenuItem");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Monitoring MenuItem
		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.ROOT_MENU_COMPLIANCE);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		CommonUIUtils.verifyURL_WithPara(webd, "complianceuiservice/index.html");
	}

	@Test(alwaysRun = true)
	public void testClickDashboardsMenuItem()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("start to test in testClickDashboardsMenuItem");
		WaitUtil.waitForPageFullyLoaded(webd);

		// dashboardHome MenuItem
		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.ROOT_MENU_DASHBOARDS);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		CommonUIUtils.verifyURL(webd, "emcpdfui/home.html");
	}

	@Test(alwaysRun = true)
	public void testClickEntitiedCfgMenuItem()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("start to test in testClickEntitiedCfgMenuItem");
		WaitUtil.waitForPageFullyLoaded(webd);
		BrandingBarUtil.expandSubMenu(webd, BrandingBarUtil.ROOT_MENU_ADMIN);
		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.GLOBAL_ADMIN_MENU_ENTITIES_CFG);
		WaitUtil.waitForPageFullyLoaded(webd);
		CommonUIUtils.verifyURL(webd, "admin-console/ac/adminConsole.html");
	}

	@Test(alwaysRun = true)
	public void testClickHomeMenuItem()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("start to test in testClickHomeMenuItem");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Home MenuItem
		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.ROOT_MENU_HOME);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		CommonUIUtils.verifyURL(webd, "emcpdfui/welcome.html");
	}

	@Test(alwaysRun = true)
	public void testClickITAMenuItem()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("start to test in testClickITAMenuItem");
		WaitUtil.waitForPageFullyLoaded(webd);

		// IT Analytics MenuItem,check checkbox
		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.ROOT_MENU_ITA);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		CommonUIUtils.verifyURL_WithPara(webd, "emcpdfui/home.html?filter=ita");
	}

	@Test(alwaysRun = true)
	public void testClickLAMenuItem()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("start to test in testClickLAMenuItem");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Log Analytics MenuItem
		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.ROOT_MENU_LA);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		CommonUIUtils.verifyURL(webd, "emlacore/html/log-analytics-search.html");
	}

	//@Test(alwaysRun = true)
	public void testClickMonitoringMenuItem()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("start to test in testClickMonitoringMenuItem");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Monitoring MenuItem
		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.ROOT_MENU_MONITORING);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		CommonUIUtils.verifyURL(webd, "monitoringservicesui/cms/index.html");
	}

	@Test(alwaysRun = true)
	public void testClickOrchestrationMenuItem()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("start to test in testClickOrchestrationMenuItem");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Monitoring MenuItem
		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.ROOT_MENU_ORCHESTRATION);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		CommonUIUtils.verifyURL_WithPara(webd, "emcpdfui/home.html?filter=ocs");
	}

	@Test(alwaysRun = true)
	public void testDataExplorerMenuItem()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("start to test in testDataExplorerMenuItem");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Target link
		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.ROOT_MENU_DATAEXPLORER);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		CommonUIUtils.verifyURL(webd, "emcta/ta/analytics.html");
	}

	@Test(alwaysRun = true)
	public void testSecurityMenuItem()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("start to test in testSecurityMenuItem");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Monitoring MenuItem
		BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.ROOT_MENU_SECURITY);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		CommonUIUtils.verifyURL_WithPara(webd, "saui/web/index.html");
	}
}
