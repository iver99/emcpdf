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
public class HamburgerMenu_ShowCurrentMenu extends LoginAndLogout
{
	public void initTest(String testName, String queryString)
	{
		loginHamburgerMenu(this.getClass().getName() + "." + testName, queryString);
		CommonUIUtils.loadWebDriver(webd);
	}

	@Test
	public void verifyCurrentMenu()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("Start the test case: verifyCurrentMenu");
		if(BrandingBarUtil.isHamburgerMenuDisplayed(webd))
		{
			BrandingBarUtil.toggleHamburgerMenu(webd);
		}
		//click the 'Show Composite' button
		webd.getLogger().info("Click the 'Set Current Menu' button");
		String indicator = UIControls.SGENERATEMENUBTN.replace("_name_", "Set Current Menu");
		webd.click("css=" + indicator);
		
		//check the hamburger menu
		webd.getLogger().info("Check the menu items in hamburger menu");
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Alert Rules"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Agents"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Entities Configuration"));

		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "APM Admin"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Monitoring Admin"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Log Admin"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Security Admin"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Compliance Admin"));

		webd.getLogger().info("Check the menu enabled");
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, "Alert Rules"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, "Agents"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, "Entities Configuration"));
		//check the current header
		webd.getLogger().info("Check the Menu Header");
		Assert.assertEquals(BrandingBarUtil.getCurrentMenuHeader(webd), "Administration");
	}
}
