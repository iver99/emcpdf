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

import oracle.sysman.emaas.platform.dashboards.tests.ui.WidgetSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui.util.CommonUIUtils;
import oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui.util.UIControls;

import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class BrandingBar_TestDashboardPage extends LoginAndLogout
{	
	private static String sAppName = "";
	private static boolean roles[] = { false, false, false, false };

	public void initTest(String testName, String queryString)
	{

		loginBrandingBar(this.getClass().getName() + "." + testName, queryString);
		CommonUIUtils.loadWebDriver(webd);
		sAppName = CommonUIUtils.getAppName();
		roles = CommonUIUtils.getRoles();
	}

	@Test
	public void testDashboardPage_noPara()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("Start the test case: testDashboardPage_noPara");

		CommonUIUtils.verifyPageContent(webd, sAppName);

		//click the compass icon
		webd.getLogger().info("Click the Application navigator icon");
		webd.waitForElementPresent(UIControls.SCOMPASSICON);
		webd.click(UIControls.SCOMPASSICON);

		CommonUIUtils.verifyMenu(webd, roles[3]);

		//click the compass icon again
		webd.getLogger().info("Click the Application navigator icon again");
		webd.waitForElementPresent(UIControls.SCOMPASSICON);
		webd.click(UIControls.SCOMPASSICON);

		webd.getLogger().info("Verify the Links menu disappeared");
		CommonUIUtils.verifyNoLinksMenu(webd);

		//Add a widget
		CommonUIUtils.addWidget(webd);
	}

	@Test
	public void testDashboardPage_withPara()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?appId=Dashboard");
		webd.getLogger().info("Start the test case: testDashboardPage_withPara");

		CommonUIUtils.verifyPageContent(webd, sAppName);

		//click the compass icon
		webd.getLogger().info("Click the Application navigator icon");
		webd.waitForElementPresent(UIControls.SCOMPASSICON);
		webd.click(UIControls.SCOMPASSICON);

		CommonUIUtils.verifyMenu(webd, roles[3]);

		//click the compass icon again
		webd.getLogger().info("Click the Application navigator icon again");
		webd.waitForElementPresent(UIControls.SCOMPASSICON);
		webd.click(UIControls.SCOMPASSICON);

		webd.getLogger().info("Verify the Links menu disappeared");
		CommonUIUtils.verifyNoLinksMenu(webd);

		//Add a widget
		CommonUIUtils.addWidget(webd);
	}

	//Testcase for adding widget using widgetselector
	@Test
	public void testWidgetSelector()
	{
		String WidgetName_1 = "Database Errors Trend";

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("Start the test case: testWidgetSelector");

		CommonUIUtils.verifyPageContent(webd, sAppName);
		
		webd.waitForServer();

		//click on Add button
		webd.getLogger().info("Click the Add icon");
		webd.waitForElementEnabled("id=" + DashBoardPageId.WIDGETSELECTOR_ADDBUTTONID);
		webd.click("id=" + DashBoardPageId.WIDGETSELECTOR_ADDBUTTONID);

		//Adding widgets using widgetSElector dialog
		webd.getLogger().info("satrt widget selector dialog box opens");
		webd.waitForElementPresent("css=div[id^='ojDialogWrapper-'].oj-dialog");

		webd.getLogger().info("Add widget");
		WidgetSelectorUtil.addWidget(webd, WidgetName_1);
	}
}
