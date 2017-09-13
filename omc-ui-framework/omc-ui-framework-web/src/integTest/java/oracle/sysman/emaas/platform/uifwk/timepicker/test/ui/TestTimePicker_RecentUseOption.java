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

import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.CommonUIUtils;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.UIControls;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestTimePicker_RecentUseOption extends LoginAndLogout
{
	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName, "datetimePickerIndex.html");
		CommonUIUtils.loadWebDriver(webd);
	}

	@Test(alwaysRun = true)
	public void testInCompactMode()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testInCompactMode");

		//click the time picker and verify that there is no Recent Use option
		webd.getLogger().info("Click TimePicker");
		CommonUIUtils.clickTimePicker(webd, 2);
		webd.getLogger().info("Verify no Recent Use");
		Assert.assertFalse(verifyRecentUseExist(webd, 1), "Recent Use Option should not be displayed");

		CommonUIUtils.clickTimePicker(webd, 2);

		//set time range
		webd.getLogger().info("set timerange as Last 7 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 2, TimeRange.Last7Days);

		webd.getLogger().info("Verify that the newly selected time range has been added into Recent Use");

		//click the time picker and verify that there is Recent Use option
		webd.getLogger().info("Click TimePicker");
		CommonUIUtils.clickTimePicker(webd, 2);
		webd.getLogger().info("Verify has Recent Use");
		Assert.assertTrue(verifyRecentUseExist(webd, 1), "Recent Use Option should be displayed");
		String[] context = verifyRecentUseContent(webd, 1);
		Assert.assertEquals(context[0], "Last week");
		;

		//add more time range to the Recent Use and verify them
		CommonUIUtils.clickTimePicker(webd, 2);
		webd.getLogger().info("set timerange as Last 2 hours");
		TimeSelectorUtil.setTimeRange(webd, 2, TimeRange.Last2Hours);

		webd.getLogger().info("Verify the context in Recent Use");
		CommonUIUtils.clickTimePicker(webd, 2);
		context = verifyRecentUseContent(webd, 1);
		Assert.assertEquals(context[1], "Last week");
		Assert.assertEquals(context[0], "Last 2 hours");

		webd.shutdownBrowser(true);

	}

	@Test(alwaysRun = true)
	public void testInDateOnly()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testInDateOnly");

		//click the time picker and verify that there is no Recent Use option
		webd.getLogger().info("Click TimePicker");
		CommonUIUtils.clickTimePicker(webd, 3);
		webd.getLogger().info("Verify no Recent Use");
		Assert.assertFalse(verifyRecentUseExist(webd, 1), "Recent Use Option should not be displayed");

		CommonUIUtils.clickTimePicker(webd, 3);

		//set time range
		webd.getLogger().info("set timerange as Last 14 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 3, TimeRange.Last1Year);

		webd.getLogger().info("Verify that the newly selected time range has been added into Recent Use");

		//click the time picker and verify that there is Recent Use option
		webd.getLogger().info("Click TimePicker");
		CommonUIUtils.clickTimePicker(webd, 3);
		webd.getLogger().info("Verify has Recent Use");
		Assert.assertTrue(verifyRecentUseExist(webd, 1), "Recent Use Option should be displayed");
		String[] context = verifyRecentUseContent(webd, 1);
		Assert.assertEquals(context[0], "Last year");

		//add more time range to the Recent Use and verify them
		CommonUIUtils.clickTimePicker(webd, 3);
		webd.getLogger().info("set timerange as Last 30 days");
		TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 3, TimeRange.Last30Days);

		webd.getLogger().info("Verify the context in Recent Use");
		CommonUIUtils.clickTimePicker(webd, 3);
		context = verifyRecentUseContent(webd, 1);
		Assert.assertEquals(context[1], "Last year");
		Assert.assertEquals(context[0], "Last 30 days");

		webd.shutdownBrowser(true);

	}

	@Test(alwaysRun = true)
	public void testInNormalMode()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testInNormalMode");

		//click the time picker and verify that there is no Recent Use option
		webd.getLogger().info("Click TimePicker");
		CommonUIUtils.clickTimePicker(webd, 1);
		webd.getLogger().info("Verify no Recent Use");
		Assert.assertFalse(verifyRecentUseExist(webd, 1), "Recent Use Option should not be displayed");

		CommonUIUtils.clickTimePicker(webd, 1);

		//set time range
		webd.getLogger().info("set timerange as Last 14 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, TimeRange.Last14Days);

		webd.getLogger().info("Verify that the newly selected time range has been added into Recent Use");

		//click the time picker and verify that there is Recent Use option
		webd.getLogger().info("Click TimePicker");
		CommonUIUtils.clickTimePicker(webd, 1);
		webd.getLogger().info("Verify has Recent Use");
		Assert.assertTrue(verifyRecentUseExist(webd, 1), "Recent Use Option should be displayed");
		String[] context = verifyRecentUseContent(webd, 1);
		Assert.assertEquals(context[0], "Last 14 days");

		//add more time range to the Recent Use and verify them
		CommonUIUtils.clickTimePicker(webd, 1);
		webd.getLogger().info("set timerange as Last 15 minutes");
		TimeSelectorUtil.setTimeRange(webd, 1, TimeRange.Last15Mins);

		webd.getLogger().info("Verify the context in Recent Use");
		CommonUIUtils.clickTimePicker(webd, 1);
		context = verifyRecentUseContent(webd, 1);
		Assert.assertEquals(context[1], "Last 14 days");
		Assert.assertEquals(context[0], "Last 15 mins");

		webd.shutdownBrowser(true);
	}

	private void clickTimePicker(WebDriver webd, int Index)
	{
		//click the datetimepicker component
		webd.waitForElementPresent("css=" + UIControls.TIMERANGEBTN_CSS);
		webd.getWebDriver().findElements(By.cssSelector(UIControls.TIMERANGEBTN_CSS)).get(Index - 1).click();

		webd.takeScreenShot();
		webd.savePageToFile();
	}

	private String[] verifyRecentUseContent(WebDriver webd, int Index)
	{
		//click Recent Use option
		webd.getLogger().info("Click Recent Use Option");
		webd.getWebDriver().findElements(By.cssSelector(UIControls.RECENTUSE_CSS)).get(Index - 1).click();
		webd.takeScreenShot();
		webd.savePageToFile();

		//check the expand options
		webd.getLogger().info("Get the context");
		webd.waitForElementPresent("css=" + UIControls.RECENTUSECONTEXT_CSS);
		String context1 = webd.getElement("css=" + UIControls.RECENTUSECONTEXT_CSS).getText();

		context1 = context1.replaceAll("[\\r\\n]", ";");

		if (context1.contains(";")) {
			String[] tmp_context = context1.split(";");
			return tmp_context;
		}
		else {
			String[] tmp_context1 = { context1 };
			return tmp_context1;
		}

	}

	private boolean verifyRecentUseExist(WebDriver webd, int Index)
	{
		//verify Recent Use option is displayed or not
		return webd.getWebDriver().findElements(By.cssSelector(UIControls.RECENTUSE_CSS)).get(Index - 1).isDisplayed();
	}

}
