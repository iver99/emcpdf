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
//		WebElement widgetTitle = getTileTitleElement(driver, widgetName, index);
		
		int widgetTitleIndex = getTileTitleElement(driver, widgetName, index);
		if (widgetTitleIndex == 0) {
			throw new NoSuchElementException("Widget with title=" + widgetName + ", index=" + index + " is not found");
		}
		driver.getLogger().info("Found widget with name=" + widgetName + ", index =" + index + " before opening widget link");
		
//		WebElement widgetMaxMin = widgetTitle.findElement(By.xpath(DashBoardPageId.BUILDERTILEMAXMINLOCATOR));
//		if (widgetMaxMin == null) {
//			throw new NoSuchElementException("Widget maxize link for title=" + widgetName + ", index=" + index + " is not found");
//		}
		driver.getLogger().info("Found widget max button");
//		Actions builder = new Actions(driver.getWebDriver());
		driver.getLogger().info("Now moving to the widget title bar");
		driver.moveToElement("xpath=(" + DashBoardPageId.BUILDERTILEMAXMINLOCATOR + ")[" + widgetTitleIndex + "]");
//		builder.moveToElement(widgetTitle).perform();
//		driver.takeScreenShot();
//		driver.savePageToFile();
//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		String widgetAttribute = driver.getAttribute("xpath=(" + DashBoardPageId.BUILDERTILEMAXMINLOCATOR + ")[" + widgetTitleIndex + "]@title");
		if ("Restore".equalsIgnoreCase(widgetAttribute)) {
			driver.getLogger().info("The widget is maximized already");
		}
		else {
//			wait.until(ExpectedConditions.elementToBeClickable(widgetMaxMin));
			driver.waitForElementEnabled("xpath=(" + DashBoardPageId.BUILDERTILEMAXMINLOCATOR + ")[" + widgetTitleIndex + "]");
			driver.click("xpath=(" + DashBoardPageId.BUILDERTILEMAXMINLOCATOR + ")[" + widgetTitleIndex + "]");
//			driver.takeScreenShot();
//			driver.savePageToFile();
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
//		WebElement widgetMaxMin = widgetTitle.findElement(By.xpath(DashBoardPageId.BUILDERTILEMAXMINLOCATOR));
//		if (widgetMaxMin == null) {
//			throw new NoSuchElementException("Widget restore link for title=" + widgetName + ", index=" + index + " is not found");
//		}
		driver.getLogger().info("Found widget restore button");
//		Actions builder = new Actions(driver.getWebDriver());
		driver.getLogger().info("Now moving to the widget title bar");
		driver.moveToElement("xpath=(" + DashBoardPageId.BUILDERTILEMAXMINLOCATOR + ")[" + widgetTitleIndex + "]");
//		builder.moveToElement(widgetTitle).perform();
//		driver.takeScreenShot();
//		driver.savePageToFile();
//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		String widgetAttribute = driver.getAttribute("xpath=(" + DashBoardPageId.BUILDERTILEMAXMINLOCATOR + ")[" + widgetTitleIndex + "]@title");
		if ("Maximize".equalsIgnoreCase(widgetAttribute)) {
			driver.getLogger().info("The widget is restored already");
		}
		else {
//			wait.until(ExpectedConditions.elementToBeClickable(widgetMaxMin));
			driver.waitForElementEnabled("xpath=(" + DashBoardPageId.BUILDERTILEMAXMINLOCATOR + ")[" + widgetTitleIndex + "]");
			driver.click("xpath=(" + DashBoardPageId.BUILDERTILEMAXMINLOCATOR + ")[" + widgetTitleIndex + "]");
//			driver.takeScreenShot();
//			driver.savePageToFile();
		}
	}

	private int getTileTitleElement(WebDriver driver, String widgetName, int index)
	{
		driver.waitForElementPresent(DashBoardPageId_190.BUILDERTILESEDITAREA);
		//driver.click(DashBoardPageId_190.BUILDERTILESEDITAREA);
//		driver.getWebDriver().findElement(By.xpath(DashBoardPageId_190.BUILDERTILESEDITAREA));
//		new Actions(driver.getWebDriver()).moveToElement(
//				driver.getWebDriver().findElement(By.xpath(DashBoardPageId_190.BUILDERTILESEDITAREA))).perform();
//		driver.takeScreenShot();
//		driver.savePageToFile();
		
		driver.moveToElement("xpath=" + DashBoardPageId_190.BUILDERTILESEDITAREA);

		String titleTitlesLocator = String.format(DashBoardPageId_190.BUILDERTILETITLELOCATOR, widgetName);
		int i = 0;
		
		int tileTitlesCount = driver.getElementCount("xpath=" + titleTitlesLocator);
//		List<WebElement> tileTitles = driver.getWebDriver().findElements(By.xpath(titleTitlesLocator));
		if (tileTitlesCount <= index) {
			throw new NoSuchElementException("Tile with title=" + widgetName + ", index=" + index + " is not found");
		}

		i = index + 1;
		driver.moveToElement("xpath=(" + titleTitlesLocator + ")[" + i + "]");
//		new Actions(driver.getWebDriver()).moveToElement(tileTitles.get(index)).perform();
		//	tileTitles.get(index).click();
//		driver.takeScreenShot();
//		driver.savePageToFile();
		return i;
	}

}
