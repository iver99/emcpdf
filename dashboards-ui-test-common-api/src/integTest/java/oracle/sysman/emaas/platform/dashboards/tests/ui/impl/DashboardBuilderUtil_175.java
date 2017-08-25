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

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
		WebElement widgetTitle = getTileTitleElement(driver, widgetName, index);
		if (widgetTitle == null) {
			throw new NoSuchElementException("Widget with title=" + widgetName + ", index=" + index + " is not found");
		}
		driver.getLogger().info("Found widget with name=" + widgetName + ", index =" + index + " before opening widget link");
		WebElement widgetMaxMin = widgetTitle.findElement(By.xpath(DashBoardPageId.BUILDERTILEMAXMINLOCATOR));
		if (widgetMaxMin == null) {
			throw new NoSuchElementException("Widget maxize link for title=" + widgetName + ", index=" + index + " is not found");
		}
		driver.getLogger().info("Found widget max button");
		Actions builder = new Actions(driver.getWebDriver());
		driver.getLogger().info("Now moving to the widget title bar");
		builder.moveToElement(widgetTitle).perform();
		driver.takeScreenShot();
		driver.savePageToFile();
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		String widgetAttribute = widgetTitle.getAttribute("title");
		if ("Restore".equalsIgnoreCase(widgetAttribute)) {
			driver.getLogger().info("The widget is maximized already");
		}
		else {
			wait.until(ExpectedConditions.elementToBeClickable(widgetMaxMin));
			widgetMaxMin.click();
			driver.takeScreenShot();
			driver.savePageToFile();
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
		WebElement widgetTitle = getTileTitleElement(driver, widgetName, index);
		if (widgetTitle == null) {
			throw new NoSuchElementException("Widget with title=" + widgetName + ", index=" + index + " is not found");
		}
		driver.getLogger().info("Found widget with name=" + widgetName + ", index =" + index + " before opening widget link");
		WebElement widgetMaxMin = widgetTitle.findElement(By.xpath(DashBoardPageId.BUILDERTILEMAXMINLOCATOR));
		if (widgetMaxMin == null) {
			throw new NoSuchElementException("Widget restore link for title=" + widgetName + ", index=" + index + " is not found");
		}
		driver.getLogger().info("Found widget restore button");
		Actions builder = new Actions(driver.getWebDriver());
		driver.getLogger().info("Now moving to the widget title bar");
		builder.moveToElement(widgetTitle).perform();
		driver.takeScreenShot();
		driver.savePageToFile();
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		String widgetAttribute = widgetTitle.getAttribute("title");
		if ("Maximize".equalsIgnoreCase(widgetAttribute)) {
			driver.getLogger().info("The widget is restored already");
		}
		else {
			wait.until(ExpectedConditions.elementToBeClickable(widgetMaxMin));
			widgetMaxMin.click();
			driver.takeScreenShot();
			driver.savePageToFile();
		}

	}

	private WebElement getTileTitleElement(WebDriver driver, String widgetName, int index)
	{
		driver.waitForElementPresent(DashBoardPageId_190.BUILDERTILESEDITAREA);
		//driver.click(DashBoardPageId_190.BUILDERTILESEDITAREA);
		driver.getWebDriver().findElement(By.xpath(DashBoardPageId_190.BUILDERTILESEDITAREA));
		new Actions(driver.getWebDriver()).moveToElement(
				driver.getWebDriver().findElement(By.xpath(DashBoardPageId_190.BUILDERTILESEDITAREA))).perform();
		driver.takeScreenShot();
		driver.savePageToFile();

		String titleTitlesLocator = String.format(DashBoardPageId_190.BUILDERTILETITLELOCATOR, widgetName);
		List<WebElement> tileTitles = driver.getWebDriver().findElements(By.xpath(titleTitlesLocator));
		if (tileTitles == null || tileTitles.size() <= index) {
			throw new NoSuchElementException("Tile with title=" + widgetName + ", index=" + index + " is not found");
		}

		new Actions(driver.getWebDriver()).moveToElement(tileTitles.get(index)).perform();
		//	tileTitles.get(index).click();
		driver.takeScreenShot();
		driver.savePageToFile();
		return tileTitles.get(index);
	}

}
