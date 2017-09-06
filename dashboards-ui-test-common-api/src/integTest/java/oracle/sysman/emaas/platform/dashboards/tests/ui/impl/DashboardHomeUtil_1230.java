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

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.*;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class DashboardHomeUtil_1230 extends DashboardHomeUtil_1170
{
	/**
	 * add filter
	 *
	 * @param driver
	 * @param filter
	 *            filter name - oracle,share,me,favorites
	 */
	@Override
	public void filterOptions(WebDriver driver, String filter)
	{
		String eleXpath = null;
		driver.getLogger().info("Selecting Filter - " + filter + " in home page...");
		Validator.fromValidValues("filterOptions", filter, "favorites", "me", "oracle", "share");

		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent("id=oj-select-choice-" + DashBoardPageId_1230.FILTER_SELECT_ID);
		driver.click("id=oj-select-choice-" + DashBoardPageId_1230.FILTER_SELECT_ID);

		switch (filter) {
			case "favorites":
				eleXpath = getOptionXpath(driver, DashBoardPageId_1230.FILTER_SELECT_ID, DashBoardPageId_1230.FILTER_FAVORITE);
				break;
			case "me":
				eleXpath = getOptionXpath(driver, DashBoardPageId_1230.FILTER_SELECT_ID, DashBoardPageId_1230.FILTER_ME);
				break;
			case "oracle":
				eleXpath = getOptionXpath(driver, DashBoardPageId_1230.FILTER_SELECT_ID, DashBoardPageId_1230.FILTER_ORACLE);
				break;
			case "share":
				eleXpath = getOptionXpath(driver, DashBoardPageId_1230.FILTER_SELECT_ID, DashBoardPageId_1230.FILTER_OTHER);
				break;
			default:
				break;
		}
		driver.click(eleXpath, WebDriver.ClickType.WEBELEMENT);
	}

	@Override
	public boolean isFilterOptionSelected(WebDriver driver, String filter)
	{
		String filterby = null;
		boolean isSelected = false;
		driver.getLogger().info("[DashboardHomeUtil] call isFilterOptionSelected filter: " + filter);
		Validator.fromValidValues("filterOptions", filter, "favorites", "me", "oracle", "share");

		filterby = driver.getText("id=oj-select-choice-" + DashBoardPageId_1230.FILTER_SELECT_ID).trim();

		switch (filter) {
			case "favorites":
				if("My Favorites".equals(filterby)) isSelected=true;
				break;
			case "me":
				if("Me".equals(filterby)) isSelected=true;
				break;
			case "oracle":
				if("Oracle".equals(filterby)) isSelected=true;
				break;
			case "share":
				if("Others".equals(filterby)) isSelected=true;
				break;
			default:
				break;
		}
		return isSelected;
	}

	@Override
	public void resetFilterOptions(WebDriver driver)
	{
		String eleXpath = null;
		driver.getLogger().info("[DashboardHomeUtil] call resetFilterOptions");
		driver.waitForElementPresent("id=oj-select-choice-" + DashBoardPageId_1230.FILTER_SELECT_ID);
		driver.click("id=oj-select-choice-" + DashBoardPageId_1230.FILTER_SELECT_ID);
		eleXpath = getOptionXpath(driver, DashBoardPageId_1230.FILTER_SELECT_ID, DashBoardPageId_1230.FILTER_ALL);
		driver.click(eleXpath, WebDriver.ClickType.WEBELEMENT);
		WaitUtil.waitForPageFullyLoaded(driver);
	}

	/* (non-Javadoc)
 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#sortBy(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
 */
	@Override
	public void sortBy(WebDriver driver, String option)
	{
		String eleXpath = null;

		driver.getLogger().info("[DashboardHomeUtil] call sortBy option: " + option);
		Validator.notEmptyString("option", option);

		driver.waitForElementPresent("id=oj-select-choice-" + DashBoardPageId_1230.SORT_SELECT_ID);
		driver.click("id=oj-select-choice-" + DashBoardPageId_1230.SORT_SELECT_ID);

		if ("default".equals(option)) {
			eleXpath = getOptionXpath(driver, DashBoardPageId_1230.SORT_SELECT_ID, DashBoardPageId_1230.SORT_DEFAULT);
			driver.click(eleXpath, WebDriver.ClickType.WEBELEMENT);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_NAME_ASC.equals(option)) {
			eleXpath = getOptionXpath(driver, DashBoardPageId_1230.SORT_SELECT_ID, DashBoardPageId_1230.SORT_NAME_ASC);
			driver.click(eleXpath, WebDriver.ClickType.WEBELEMENT);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_NAME_DSC.equals(option)) {
			eleXpath = getOptionXpath(driver, DashBoardPageId_1230.SORT_SELECT_ID, DashBoardPageId_1230.SORT_NAME_DSC);
			driver.click(eleXpath, WebDriver.ClickType.WEBELEMENT);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_OWNER_ASC.equals(option)) {
			eleXpath = getOptionXpath(driver, DashBoardPageId_1230.SORT_SELECT_ID, DashBoardPageId_1230.SORT_CREATEDBY_ASC);
			driver.click(eleXpath, WebDriver.ClickType.WEBELEMENT);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_OWNER_DSC.equals(option)) {
			eleXpath = getOptionXpath(driver, DashBoardPageId_1230.SORT_SELECT_ID, DashBoardPageId_1230.SORT_CREATEDBY_DSC);
			driver.click(eleXpath, WebDriver.ClickType.WEBELEMENT);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_ASC.equals(option)) {
			eleXpath = getOptionXpath(driver, DashBoardPageId_1230.SORT_SELECT_ID, DashBoardPageId_1230.SORT_CREATEDATE_ASC);
			driver.click(eleXpath, WebDriver.ClickType.WEBELEMENT);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_DSC.equals(option)) {
			eleXpath = getOptionXpath(driver, DashBoardPageId_1230.SORT_SELECT_ID, DashBoardPageId_1230.SORT_CREATEDATE_DSC);
			driver.click(eleXpath, WebDriver.ClickType.WEBELEMENT);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_ASC.equals(option)) {
			eleXpath = getOptionXpath(driver, DashBoardPageId_1230.SORT_SELECT_ID, DashBoardPageId_1230.SORT_LASTMODIFY_ASC);
			driver.click(eleXpath, WebDriver.ClickType.WEBELEMENT);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_DSC.equals(option)) {
			eleXpath = getOptionXpath(driver, DashBoardPageId_1230.SORT_SELECT_ID, DashBoardPageId_1230.SORT_LASTMODIFY_DSC);
			driver.click(eleXpath, WebDriver.ClickType.WEBELEMENT);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_ASC.equals(option)) {
			eleXpath = getOptionXpath(driver, DashBoardPageId_1230.SORT_SELECT_ID, DashBoardPageId_1230.SORT_LASTACCESS_ASC);
			driver.click(eleXpath, WebDriver.ClickType.WEBELEMENT);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_DSC.equals(option)) {
			eleXpath = getOptionXpath(driver, DashBoardPageId_1230.SORT_SELECT_ID, DashBoardPageId_1230.SORT_LASTACCESS_DSC);
			driver.click(eleXpath, WebDriver.ClickType.WEBELEMENT);
		}
		else {
			throw new IllegalArgumentException("Unknow Sort by option: " + option);
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#gotoDataExplorer(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void gotoDataExplorer(WebDriver driver, String option)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	protected String getOptionXpath(WebDriver driver, String selectId, String optionId)
	{
		String optionXpath;
		WebElement li = driver.getWebDriver().findElement(By.id(optionId));
		List<WebElement> list = driver.getWebDriver().findElements(By.xpath("//select[@id='" + selectId + "']/option"));
		//get the index of option in select dropdown
		int index = list.indexOf(li);
		//get option's xpath generated by jet
		optionXpath = "//ul[@id='oj-listbox-results-" + selectId + "']/li[" + (index + 1) + "]/div";
		return optionXpath;
	}
}
