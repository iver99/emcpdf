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

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWidgetSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.XPathLiteral;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.NoSuchElementException;

public class WidgetSelectorUtil_171 extends WidgetSelectorUtil_Version implements IWidgetSelectorUtil
{

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWidgetSelectorUtil#addWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void addWidget(WebDriver driver, String widgetName)
	{
		driver.getLogger().info("addWidget started, widgetName=" + widgetName);
		Validator.notEmptyString("widgetName", widgetName);

		try {
			searchWidget(driver, widgetName);
		}
		catch (Exception e) {
			driver.getLogger().info(e.getLocalizedMessage());
		}
		String autoCloseCheck = driver.getAttribute(DashBoardPageId.WIDGET_SELECTOR_WIDGET_AREA + "@data-wgt-slt-auto-close");
		Boolean autoClose = Boolean.valueOf(autoCloseCheck);
		getWidgetElementByTitle(driver, widgetName, 0);

		driver.click(DashBoardPageId.WIDGET_SELECTOR_OK_BTN_LOCATOR);

		// automatically close the dialog then
		if (!autoClose) {
			try {
				closeDialog(driver);
			}
			catch (Exception e) {
				driver.getLogger().info(e.getLocalizedMessage());
			}
		}

		driver.getLogger().info("addWidget completed");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWidgetSelectorUtil#page(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int)
	 */
	@Override
	public void page(WebDriver driver, int pageNo) throws IllegalAccessException
	{
		throw new IllegalAccessException("Not implemented: page(WebDriver driver, int pageNo)");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWidgetSelectorUtil#pagingNext(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void pagingNext(WebDriver driver) throws IllegalAccessException
	{
		throw new IllegalAccessException("Not implemented:pagingNext(WebDriver driver)");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWidgetSelectorUtil#pagingPrevious(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void pagingPrevious(WebDriver driver) throws IllegalAccessException
	{
		throw new IllegalAccessException("Not implemented:pagingPrevious(WebDriver driver)");
	}

	private void closeDialog(WebDriver driver)
	{
		driver.getLogger().info("(Internal method) closeDialog started");
		driver.click(DashBoardPageId.WIDGET_SELECTOR_CLOSE_BTN_LOCATOR);

		driver.waitForNotElementPresent(DashBoardPageId.WIDGET_SELECTOR_CLOSE_BTN_LOCATOR);

		driver.getLogger().info("(Internal method) closeDialog completed");
	}

	private int getWidgetElementByTitle(WebDriver driver, String widgetName, int index)
	{
		String xpath = XPathLiteral.getXPath(widgetName, driver.getLogger());
		String widgetItemByNameLocator = String.format(DashBoardPageId.WIDGET_SELECTOR_WIDGET_ITEMS_BY_TITLE, xpath);

		driver.click(widgetItemByNameLocator);
		
		int tileTitlesCount = driver.getElementCount("xpath=" + widgetItemByNameLocator);

//		List<WebElement> tileTitles = driver.getWebDriver().findElements(By.xpath(widgetItemByNameLocator));
		if (tileTitlesCount <= index) {
			throw new NoSuchElementException("Widget with widgetName=" + widgetName + ", index=" + index + " is not found");
		}
//		tileTitles.get(index).click();
//		driver.takeScreenShot();
//		driver.savePageToFile();
		
		int i = index + 1;
		
		driver.click("xpath=(" + widgetItemByNameLocator + ")[" + index + "]");
//		return tileTitles.get(index);
		return i;
	}

	protected void searchWidget(WebDriver driver, String widgetName)
	{
		driver.waitForElementPresent(DashBoardPageId.WIDGET_SELECTOR_WIDGET_AREA);
		driver.clear(DashBoardPageId.WIDGET_SELECTOR_SEARCH_INPUT_LOCATOR);
		driver.takeScreenShot();
		driver.savePageToFile();
		driver.sendKeys(DashBoardPageId.WIDGET_SELECTOR_SEARCH_INPUT_LOCATOR, widgetName);
		driver.click(DashBoardPageId.WIDGET_SELECTOR_SEARCH_BTN);
	}
}