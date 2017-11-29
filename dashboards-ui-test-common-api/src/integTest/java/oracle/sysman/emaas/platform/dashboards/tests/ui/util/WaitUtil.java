/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.util;

/**
 * @author wenjzhu
 */
public class WaitUtil
{
	public static final long WAIT_TIMEOUT = 300; //same as default webdriver timeout

	public static void waitForPageFullyLoaded(final oracle.sysman.qatool.uifwk.webdriver.WebDriver webd)
	{
		webd.getLogger().info("Start waitForServer and Capture the screenshots and page status");
		webd.waitForServer();
		webd.capturePageState(true);
		webd.getLogger().info("WaitForServer ends...");
	}
}
