/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IUiTestCommonAPI;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author miao
 */
public class TimeSelectorUtil_Version implements IUiTestCommonAPI
{

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IUiTestCommonAPI#getApiVersion(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public String getApiVersion(WebDriver wdriver)
	{
		By locatorOfKeyEl = By.xpath("//div[contains(@data-bind, 'dateTimePicker_')]");
		WebDriverWait wait = new WebDriverWait(wdriver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfKeyEl));
		String version = wdriver.getWebDriver().findElement(locatorOfKeyEl).getAttribute(VERSION_ATTR);
		if (version == null || "".equals(version.trim())) {
			//1.7.1 or earlier
			return "171";
		}
		else {
			version = version.replace(".", "");
		}
		return version;
	}
}
