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

import oracle.sysman.emaas.platform.dashboards.tests.ui.GlobalContextUtil;
import oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui.util.CommonUIUtils;
import oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui.util.LoginAndLogout;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class BrandingBar_TestGlobalContext extends LoginAndLogout
{	
	private static boolean roles[] = { false, false, false, false };

	public void initTest(String testName, String queryString)
	{
		loginBrandingBar(this.getClass().getName() + "." + testName, queryString);
		CommonUIUtils.loadWebDriver(webd);

		roles = CommonUIUtils.getRoles();
	}

	@Test
	public void testAddWidget()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("Start the test case: testAddWidget");

		//Add a widget
		CommonUIUtils.addWidget(webd);

		//verify the result
		String timeRange = GlobalContextUtil.getTimeRangeLabel_V2(webd).trim();
		Assert.assertTrue(timeRange.contains("Last year"));

		String currurl = webd.getCurrentUrl();

		webd.getLogger().info("the origin url = " + currurl);

		String tmpurl = currurl.substring(currurl.indexOf("emsaasui") + 9);

		webd.getLogger().info("the url want to compare = " + tmpurl);
		String url = "uifwk/test.html?omcCtx=timePeriod%3DLAST_1_YEAR";

		Assert.assertTrue(tmpurl.contains(url), "Not open the expected url: " + url);

	}
}
