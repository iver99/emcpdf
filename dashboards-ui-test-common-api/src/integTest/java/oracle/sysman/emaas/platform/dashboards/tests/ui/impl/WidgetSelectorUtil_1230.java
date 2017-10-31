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

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.*;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;

public class WidgetSelectorUtil_1230 extends WidgetSelectorUtil_175
{
	@Override
	protected void searchWidget(WebDriver driver, String widgetName)
	{
		driver.waitForElementPresent(DashBoardPageId.WIDGET_SELECTOR_WIDGET_AREA);
		driver.clear(DashBoardPageId_1230.WIDGET_SELECTOR_SEARCH_INPUT_LOCATOR);
		driver.takeScreenShot();
		driver.savePageToFile();
		driver.sendKeys(DashBoardPageId_1230.WIDGET_SELECTOR_SEARCH_INPUT_LOCATOR, widgetName);
		driver.click(DashBoardPageId.WIDGET_SELECTOR_SEARCH_BTN);
	}
}
