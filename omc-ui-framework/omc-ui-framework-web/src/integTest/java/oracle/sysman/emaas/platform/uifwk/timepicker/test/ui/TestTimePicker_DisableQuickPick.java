/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.timepicker.test.ui;

import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.CommonUIUtils;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.UIControls;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author shangwan
 */
public class TestTimePicker_DisableQuickPick extends LoginAndLogout
{
	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName, "timeSelectorDisableQuickPicks.html");
		CommonUIUtils.loadWebDriver(webd);
	}

	@Test(alwaysRun = true)
	public void testDisableShortTerm()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testDisableShortTerm");

		//click the time picker and verify that there is no Recent Use option
		webd.getLogger().info("Click TimePicker");
		CommonUIUtils.clickTimePicker(webd, 1);

		webd.getLogger().info("Verify 'Last 7 Days' option is disabled'");		
		List<WebElement> webelements = webd.getWebDriver().findElements(By.cssSelector(UIControls.DISABLE_OPT_CSS));
		if (webelements == null || webelements.isEmpty()) {
			throw new NoSuchElementException("Can not find the expected time options!!!");
		}

		for (WebElement nav : webelements) {
			if (nav.isDisplayed())
			{
				Assert.assertEquals(nav.getText().trim(),"Last 7 days");
			}
		}

		webd.getLogger().info("Click button to disable 'Last 14 Days'");
		CommonUIUtils.clickTimePicker(webd, 1);
		webd.click("css=" + UIControls.CHANGEOPT_BTN1_CSS);

		webd.getLogger().info("Click TimePicker");
		CommonUIUtils.clickTimePicker(webd, 1);

		webd.getLogger().info("Verify 'Last 7 Days' and 'Last 14 Days'options are disabled'");
		List<WebElement> navs = webd.getWebDriver().findElements(By.cssSelector(UIControls.DISABLE_OPT_CSS));
		if (navs == null || navs.isEmpty()) {
			throw new NoSuchElementException("Can not find the expected time options!!!");
		}

		for (WebElement nav : navs) {
			if (nav.isDisplayed() && nav.getText().equals("Last 7 days"))
			{
				Assert.assertTrue(true);
			}
			else if(nav.isDisplayed() && nav.getText().equals("Last 14 days"))
			{
				Assert.assertTrue(true);
			}
			else
			{
				Assert.assertTrue(false);
			}
		}
		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testDisableLongTerm()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testDisableLongTerm");

		//click the time picker and verify that there is no Recent Use option
		webd.getLogger().info("Click TimePicker");
		CommonUIUtils.clickTimePicker(webd, 2);

		webd.getLogger().info("Verify 'Last 24 hours' option is disabled'");
		List<WebElement> webelements = webd.getWebDriver().findElements(By.cssSelector(UIControls.DISABLE_OPT_CSS));
		if (webelements == null || webelements.isEmpty()) {
			throw new NoSuchElementException("Can not find the expected time options!!!");
		}

		for (WebElement nav : webelements) {
			if (nav.isDisplayed())
			{
				Assert.assertEquals(nav.getText().trim(),"Last 24 hours");
			}
		}

		webd.getLogger().info("Click button to disable 'Last 30 days' and 'Last 12 months'");
		CommonUIUtils.clickTimePicker(webd, 2);
		webd.click("css=" + UIControls.CHANGEOPT_BTN2_CSS);

		webd.getLogger().info("Click TimePicker");
		CommonUIUtils.clickTimePicker(webd, 2);

		webd.getLogger().info("Verify 'Last 30 days' and 'Last 12 months' options are disabled'");
		List<WebElement> navs = webd.getWebDriver().findElements(By.cssSelector(UIControls.DISABLE_OPT_CSS));
		if (navs == null || navs.isEmpty()) {
			throw new NoSuchElementException("Can not find the expected time options!!!");
		}
		for (WebElement nav : navs) {
			if (nav.isDisplayed() && nav.getText().equals("Last 30 days"))
			{
				Assert.assertTrue(true);
			}
			else if(nav.isDisplayed() && nav.getText().equals("Last 12 months"))
			{
				Assert.assertTrue(true);
			}
			else
			{
				Assert.assertTrue(false);
			}
		}

		webd.getLogger().info("Verify 'Last 24 hours' is enabled");
		boolean isEnabled = false;
		List<WebElement> enable_navs = webd.getWebDriver().findElements(By.cssSelector(UIControls.ENABLE_OPT_CSS));
		if (enable_navs == null || enable_navs.isEmpty()) {
			throw new NoSuchElementException("Can not find the expected time options!!!");
		}
		for (WebElement nav : enable_navs) {
			if (nav.isDisplayed() && nav.getText().equals("Last 24 hours"))
			{
				isEnabled = true;
				break;
			}
		}
		if(!isEnabled)
			Assert.assertTrue(false);

		webd.shutdownBrowser(true);
	}
}
