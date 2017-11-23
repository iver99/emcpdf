/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IMessageUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author cawei
 */
public class MessageUtil_1250 extends MessageUtil_Version implements IMessageUtil
{

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IMessageUtil#closeMessage(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void closeMessage(WebDriver driver)
	{
		if (driver.isElementPresent("css=" + IMessageUtil.CLOSECSS)) {
			driver.click("css=" + IMessageUtil.CLOSECSS);
		}
		else {
			driver.getLogger().info("The message banner is closed already");
		}

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IMessageUtil#verifyConfirmMessage(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean verifyConfirmMessage(WebDriver driver)
	{
		boolean b = driver.isDisplayed("css=" + IMessageUtil.CONFIRMMESSAGECSS);
		return b;

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IMessageUtil#verifyConfirmMessage(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean verifyConfirmMessage(WebDriver driver, String message)
	{
		verifyConfirmMessage(driver);
		return driver.getText("css=" + IMessageUtil.MESSAGECONTENT).equalsIgnoreCase(message);

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IMessageUtil#verifyErrorMessage(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean verifyErrorMessage(WebDriver driver)
	{
		boolean b = driver.isDisplayed("css=" + IMessageUtil.ERRORMESSAGECSS);
		return b;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IMessageUtil#verifyErrorMessage(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean verifyErrorMessage(WebDriver driver, String message)
	{
		verifyErrorMessage(driver);
		return driver.getText("css=" + IMessageUtil.MESSAGECONTENT).equalsIgnoreCase(message);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IMessageUtil#verifyWarningMessage(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean verifyWarningMessage(WebDriver driver)
	{
		boolean b = driver.isDisplayed("css=" + IMessageUtil.WARNINGMESSAGECSS);
		return b;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IMessageUtil#verifyWarningMessage(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean verifyWarningMessage(WebDriver driver, String message)
	{
		verifyWarningMessage(driver);
		return driver.getText("css=" + IMessageUtil.MESSAGECONTENT).equalsIgnoreCase(message);
	}

}
