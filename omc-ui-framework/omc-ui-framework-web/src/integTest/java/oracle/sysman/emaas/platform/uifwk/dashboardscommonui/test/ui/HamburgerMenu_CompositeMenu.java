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

import org.openqa.selenium.By;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class HamburgerMenu_CompositeMenu extends LoginAndLogout
{
	public void initTest(String testName, String queryString)
	{

		loginHamburgerMenu(this.getClass().getName() + "." + testName, queryString);
		CommonUIUtils.loadWebDriver(webd);
	}

	@Test
	public void verifyCompositeMenu()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("Start the test case: verifyCompositeMenu");

		if(BrandingBarUtil.isHamburgerMenuDisplayed(webd))
		{
			BrandingBarUtil.toggleHamburgerMenu(webd);
		}
		//click the 'Show Composite' button
		webd.getLogger().info("Click the 'Show Composite' button");
		String indicator = UIControls.SGENERATEMENUBTN.replace("_name_", "Show Composite");
		
		webd.click("css=" + indicator);
		//check the hamburger menu
		webd.getLogger().info("Check the menu items in hamburger menu");
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Composite Menu 1"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Composite Menu 2"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Composite Menu 3"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Composite Menu 4"));

		//check the current header
		webd.getLogger().info("Check the Menu Header");
		Assert.assertEquals(BrandingBarUtil.getCurrentMenuHeader(webd), "Test Application Name");

		//back to the parent menu
		webd.getLogger().info("Back to the parent menu");
		BrandingBarUtil.goBackToParentMenu(webd);

		//check the composite menu doesn't existed
		webd.getLogger().info("Check the composite menu doesn't exist");
		Assert.assertFalse(BrandingBarUtil.isMenuItemExisted(webd, "Test Application Name"));
	}
}
