/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.MessageUtil;
import oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.CommonUIUtils;

import org.testng.annotations.Test;

/**
 * @author cawei
 */
public class TestMessageBanner extends LoginAndLogout
{
	public void initTest(String testName, String queryString)
	{
		loginMessageBanner(this.getClass().getName() + "." + testName, queryString);
		CommonUIUtils.loadWebDriver(webd);
	}

	@Test
	public void testConfirmBanner()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("Start the test case: testConfirmBanner");
		webd.click("css=" + "#confirm-message-button");
		MessageUtil.verifyConfirmMessage(webd, "Warning message shown.");
		MessageUtil.closeMessage(webd);

	}

	@Test
	public void testErrorBanner()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("Start the test case: testErrorBanner");
		webd.click("css=" + "#error-message-button");
		MessageUtil.verifyErrorMessage(webd, "Error message shown.");
		MessageUtil.closeMessage(webd);

	}

	@Test
	public void testWarningBanner()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("Start the test case: testWarningBanner");
		webd.click("css=" + "#warn-message-button");
		MessageUtil.verifyWarningMessage(webd, "Warning message shown.");
		MessageUtil.closeMessage(webd);

	}

}
