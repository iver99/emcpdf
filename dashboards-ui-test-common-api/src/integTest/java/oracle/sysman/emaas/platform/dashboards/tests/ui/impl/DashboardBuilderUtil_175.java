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
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.NoSuchElementException;


public abstract class DashboardBuilderUtil_175 extends DashboardBuilderUtil_171
{
	@Override
	public void maximizeWidget(WebDriver driver, String widgetName, int index)
	{
		driver.getLogger().info("DashboardBuilderUtil.maximizeWidget started for widgetName=" + widgetName + ", index=" + index);

		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);
		clickTileMaximizeButton(driver, widgetName, index);

		driver.getLogger().info("DashboardBuilderUtil.maxizeWidget completed");
	}

	@Override
	public void restoreWidget(WebDriver driver, String widgetName, int index)
	{
		driver.getLogger().info("DashboardBuilderUtil.restoreWidget started for widgetName=" + widgetName + ", index=" + index);

		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);
		clickTileRestoreButton(driver, widgetName, index);

		driver.getLogger().info("DashboardBuilderUtil.restoreWidget completed");
	}

	/**
	 * @param driver
	 * @param widgetName
	 * @param index
	 */
	private void clickTileMaximizeButton(WebDriver driver, String widgetName, int index)
	{
		driver.getLogger().info("Start to find widget with widgetName=" + widgetName + ", index=" + index);

		int widgetTitleIndex = getTileTitleElement(driver, widgetName, index);
		if (widgetTitleIndex == 0) {
			throw new NoSuchElementException("Widget with title=" + widgetName + ", index=" + index + " is not found");
		}
		driver.getLogger().info("Found widget with name=" + widgetName + ", index =" + index + " before opening widget link");
		driver.getLogger().info("Found widget max button");
		driver.getLogger().info("Now moving to the widget title bar");
		String widgetAttribute = driver.getAttribute("xpath=(" + DashBoardPageId_190.BUILDERTILEHEADERLOCATOR + ")[" + widgetTitleIndex + "]" + DashBoardPageId.BUILDERTILEMAXMINLOCATOR + "@title");
		if ("Restore".equalsIgnoreCase(widgetAttribute)) {
			driver.getLogger().info("The widget is maximized already");
		}
		else {
			driver.waitForElementEnabled("xpath=(" + DashBoardPageId_190.BUILDERTILEHEADERLOCATOR + ")[" + widgetTitleIndex + "]" + DashBoardPageId.BUILDERTILEMAXMINLOCATOR);
			driver.click("xpath=(" + DashBoardPageId_190.BUILDERTILEHEADERLOCATOR + ")[" + widgetTitleIndex + "]" + DashBoardPageId.BUILDERTILEMAXMINLOCATOR);
		}
	}

	/**
	 * @param driver
	 * @param widgetName
	 * @param index
	 */
	private void clickTileRestoreButton(WebDriver driver, String widgetName, int index)
	{
		driver.getLogger().info("Start to find widget with widgetName=" + widgetName + ", index=" + index);
		int widgetTitleIndex = getTileTitleElement(driver, widgetName, index);
		if (widgetTitleIndex == 0) {
			throw new NoSuchElementException("Widget with title=" + widgetName + ", index=" + index + " is not found");
		}
		driver.getLogger().info("Found widget with name=" + widgetName + ", index =" + index + " before opening widget link");
		driver.getLogger().info("Found widget restore button");
		driver.getLogger().info("Now moving to the widget title bar");

		String widgetAttribute = driver.getAttribute("xpath=(" + DashBoardPageId_190.BUILDERTILEHEADERLOCATOR + ")[" + widgetTitleIndex + "]" + DashBoardPageId.BUILDERTILEMAXMINLOCATOR + "@title");
		if ("Maximize".equalsIgnoreCase(widgetAttribute)) {
			driver.getLogger().info("The widget is restored already");
		}
		else {
			driver.waitForElementEnabled("xpath=(" + DashBoardPageId_190.BUILDERTILEHEADERLOCATOR + ")[" + widgetTitleIndex + "]" + DashBoardPageId.BUILDERTILEMAXMINLOCATOR);
			driver.click("xpath=(" + DashBoardPageId_190.BUILDERTILEHEADERLOCATOR + ")[" + widgetTitleIndex + "]" + DashBoardPageId.BUILDERTILEMAXMINLOCATOR);
		}
	}

	private int getTileTitleElement(WebDriver driver, String widgetName, int index)
	{
		driver.waitForElementPresent(DashBoardPageId_190.BUILDERTILESEDITAREA);
	
		driver.moveToElement("xpath=" + DashBoardPageId_190.BUILDERTILESEDITAREA);

		String titleTitlesLocator = String.format(DashBoardPageId_190.BUILDERTILETITLELOCATOR, widgetName);
		int i = 0;
		
		int tileTitlesCount = driver.getElementCount("xpath=" + titleTitlesLocator);
		if (tileTitlesCount <= index) {
			throw new NoSuchElementException("Tile with title=" + widgetName + ", index=" + index + " is not found");
		}
		
		driver.moveToElement("xpath=(" + titleTitlesLocator + ")[" + (index + 1) + "]");
		int tileCount = driver.getElementCount("xpath=" + DashBoardPageId_190.BUILDERTILELOCATOR);
		int j = 1;
		boolean isExist = false;
		for (j=1; j<=tileCount; j++) {
			driver.getLogger().info("!!!!Widget Name: " + driver.getText("xpath=(" + DashBoardPageId_190.BUILDERTILELOCATOR + ")[" + j + "]").trim());
			if (widgetName.equals(driver.getText("xpath=(" + DashBoardPageId_190.BUILDERTILELOCATOR + ")[" + j + "]").trim())) {
				i++;
				if ( i == (index + 1) ) {
					isExist = true;
					break;
				}
			}
		}
		if (isExist) {
			return j;
		}
		else
			return 0;
	}

}
