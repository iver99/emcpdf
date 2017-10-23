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
import oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui.util.CommonUIUtils;
import oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui.util.UIControls;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class HamburgerMenu_RegisterServiceMenu extends LoginAndLogout
{
	public void initTest(String testName, String queryString)
	{
		loginHamburgerMenu(this.getClass().getName() + "." + testName, queryString);
		CommonUIUtils.loadWebDriver(webd);
	}

	@Test
	public void verifyRegisterServiceMenu_APM()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?appId=APM");
		webd.getLogger().info("Start the test case: verifyRegisterServiceMenu_APM");

		CommonUIUtils.verifyPageContent(webd, "Application Performance Monitoring", true);

		verifyRegisterServiceMenu(BrandingBarUtil.ROOT_MENU_APM, "APM Admin");
	}

	@Test
	public void verifyRegisterServiceMenu_Compliance()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?appId=Compliance");
		webd.getLogger().info("Start the test case: verifyRegisterServiceMenu_Compliance");

		CommonUIUtils.verifyPageContent(webd, "Configuration and Compliance", true);

		verifyRegisterServiceMenu(BrandingBarUtil.ROOT_MENU_COMPLIANCE, "Compliance Admin");
	}

	@Test
	public void verifyRegisterServiceMenu_ITA()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?appId=ITAnalytics");
		webd.getLogger().info("Start the test case: verifyRegisterServiceMenu_ITA");

		CommonUIUtils.verifyPageContent(webd, "IT Analytics", true);

		verifyRegisterServiceMenu(BrandingBarUtil.ROOT_MENU_ITA, "Sample Admin");
	}

	@Test
	public void verifyRegisterServiceMenu_LA()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?appId=LogAnalytics");
		webd.getLogger().info("Start the test case: verifyRegisterServiceMenu_LA");

		CommonUIUtils.verifyPageContent(webd, "Log Analytics", true);

		verifyRegisterServiceMenu(BrandingBarUtil.ROOT_MENU_LA, "Log Admin");
	}

	@Test
	public void verifyRegisterServiceMenu_Monitoring()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?appId=Monitoring");
		webd.getLogger().info("Start the test case: verifyRegisterServiceMenu_Monitoring");

		CommonUIUtils.verifyPageContent(webd, "Infrastructure Monitoring", true);

		verifyRegisterServiceMenu(BrandingBarUtil.ROOT_MENU_MONITORING, "Monitoring Admin");
	}

	@Test
	public void verifyRegisterServiceMenu_Orchestration()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?appId=Orchestration");
		webd.getLogger().info("Start the test case: verifyRegisterServiceMenu_Orchestration");

		CommonUIUtils.verifyPageContent(webd, "Orchestration", true);

		verifyRegisterServiceMenu(BrandingBarUtil.ROOT_MENU_ORCHESTRATION, "Sample Admin");
	}

	@Test
	public void verifyRegisterServiceMenu_Security()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?appId=SecurityAnalytics");
		webd.getLogger().info("Start the test case: verifyRegisterServiceMenu_Security");

		CommonUIUtils.verifyPageContent(webd, "Security Monitoring and Analytics", true);

		verifyRegisterServiceMenu(BrandingBarUtil.ROOT_MENU_SECURITY, "Security Admin");
	}

	private void verifyRegisterServiceMenu(String serviceMenuItem, String servieAdminMenu)
	{
		if(BrandingBarUtil.isHamburgerMenuDisplayed(webd))
		{
			BrandingBarUtil.toggleHamburgerMenu(webd);
		}
		//click the 'Show Composite' button
		webd.getLogger().info("Click the 'Show Composite' button");
		String indicator = UIControls.SGENERATEMENUBTN.replace("_name_", "Register Service Menu");
		
		webd.click("css=" + indicator);
		
		//check the hamburger menu
		webd.getLogger().info("Check the menu items in hamburger menu");
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, serviceMenuItem));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, serviceMenuItem));

		//check the current header
		webd.getLogger().info("Check the Menu Header");
		Assert.assertEquals(BrandingBarUtil.getCurrentMenuHeader(webd), BrandingBarUtil.ROOT_MENU_TITLE);

		//expand sub menu
		webd.getLogger().info("Click Compliance to expand sub menu");
		BrandingBarUtil.expandSubMenu(webd, serviceMenuItem);

		//check the menu
		webd.getLogger().info("Check the menu items in '" + serviceMenuItem + "' menu");
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Service menu 1"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Service menu 2"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Service menu 3"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Sample Admin"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, "Service menu 1"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, "Service menu 2"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, "Service menu 3"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, "Sample Admin"));

		//check the current header
		webd.getLogger().info("Check the Menu Header");
		Assert.assertEquals(BrandingBarUtil.getCurrentMenuHeader(webd), serviceMenuItem);

		//click Admin to expand sub menu
		webd.getLogger().info("Click 'Sample Admin' to expand sub menu");
		BrandingBarUtil.expandSubMenu(webd, "Sample Admin");

		//check the menu
		webd.getLogger().info("Check the menu items in Sample Admin menu");
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Sample admin menu 1"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, "Sample admin menu 1"));

		//Click Hierarchical to back to root menu
		webd.getLogger().info("Click Hierarchical to back to root menu");
		BrandingBarUtil.clickHierarchicalMenu(webd, BrandingBarUtil.ROOT_MENU_TITLE);

		//check the current header
		webd.getLogger().info("Check the Menu Header");
		Assert.assertEquals(BrandingBarUtil.getCurrentMenuHeader(webd), BrandingBarUtil.ROOT_MENU_TITLE);

		//click Adminitration
		BrandingBarUtil.expandSubMenu(webd, BrandingBarUtil.ROOT_MENU_ADMIN);

		webd.getLogger().info("Check the menu items in Administration menu");
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, servieAdminMenu));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, servieAdminMenu));

		BrandingBarUtil.expandSubMenu(webd, servieAdminMenu);

		//check the menu
		webd.getLogger().info("Check the menu items in '" + servieAdminMenu + "' menu");
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Sample admin menu 1"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, "Sample admin menu 1"));
	}
}
