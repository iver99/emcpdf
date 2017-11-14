/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IMessageUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.UtilLoader;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author cawei
 */
public class MessageUtil
{
	/**
	 * Verified Warning Message
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public static void closeMessage(WebDriver driver)
	{
		IMessageUtil mu = new UtilLoader<IMessageUtil>().loadUtil(driver, IMessageUtil.class);

	}

	/**
	 * Verified ConfirmMessage
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public static boolean verifyConfirmMessage(WebDriver driver)
	{
		IMessageUtil mu = new UtilLoader<IMessageUtil>().loadUtil(driver, IMessageUtil.class);
		return mu.verifyConfirmMessage(driver);
	}

	/**
	 * Verified Confirm Message
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public static boolean verifyConfirmMessage(WebDriver driver, String message)
	{
		IMessageUtil mu = new UtilLoader<IMessageUtil>().loadUtil(driver, IMessageUtil.class);
		return mu.verifyConfirmMessage(driver, message);
	}

	/**
	 * Verified Error Message
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public static boolean verifyErrorMessage(WebDriver driver)
	{
		IMessageUtil mu = new UtilLoader<IMessageUtil>().loadUtil(driver, IMessageUtil.class);
		return mu.verifyErrorMessage(driver);
	}

	/**
	 * Verified error Message
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public static boolean verifyErrorMessage(WebDriver driver, String message)
	{
		IMessageUtil mu = new UtilLoader<IMessageUtil>().loadUtil(driver, IMessageUtil.class);
		return mu.verifyErrorMessage(driver, message);
	}

	/**
	 * Verified Warning Message
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public static boolean verifyWarningMessage(WebDriver driver)
	{
		IMessageUtil mu = new UtilLoader<IMessageUtil>().loadUtil(driver, IMessageUtil.class);
		return mu.verifyWarningMessage(driver);
	}

	/**
	 * Verified warning Message
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public static boolean verifyWarningMessage(WebDriver driver, String message)
	{
		IMessageUtil mu = new UtilLoader<IMessageUtil>().loadUtil(driver, IMessageUtil.class);
		return mu.verifyWarningMessage(driver, message);
	}

}
