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

public class DashboardHomeUtil_1260 extends DashboardHomeUtil_1240
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
		Validator.fromValidValues("filterOptions", filter, "favorites", "me", "oracle", "share", "ita", "la", "security");

		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent("id=oj-select-choice-" + DashBoardPageId_1240.FILTER_SELECT_ID);
		driver.click("id=oj-select-choice-" + DashBoardPageId_1240.FILTER_SELECT_ID);

		switch (filter) {
			case "favorites":
				eleXpath = getOptionXpath(driver, DashBoardPageId_1240.FILTER_SELECT_ID, DashBoardPageId_1240.FILTER_FAVORITE);
				break;
			case "me":
				eleXpath = getOptionXpath(driver, DashBoardPageId_1240.FILTER_SELECT_ID, DashBoardPageId_1240.FILTER_ME);
				break;
			case "oracle":
				eleXpath = getOptionXpath(driver, DashBoardPageId_1240.FILTER_SELECT_ID, DashBoardPageId_1240.FILTER_ORACLE);
				break;
			case "share":
				eleXpath = getOptionXpath(driver, DashBoardPageId_1240.FILTER_SELECT_ID, DashBoardPageId_1240.FILTER_OTHER);
				break;
			case "ita":
				eleXpath = getOptionXpath(driver, DashBoardPageId_1240.FILTER_SELECT_ID, DashBoardPageId_1240.FILTER_ITA);
				break;
			case "la":
				eleXpath = getOptionXpath(driver, DashBoardPageId_1240.FILTER_SELECT_ID, DashBoardPageId_1240.FILTER_LA);
				break;
			case "security":
				eleXpath = getOptionXpath(driver, DashBoardPageId_1240.FILTER_SELECT_ID, DashBoardPageId_1240.FILTER_SECURITY);
				break;
			default:
				break;
		}
		driver.click(eleXpath);
	}

	@Override
	public boolean isFilterOptionSelected(WebDriver driver, String filter)
	{
		String filterby = null;
		boolean isSelected = false;
		driver.getLogger().info("[DashboardHomeUtil] call isFilterOptionSelected filter: " + filter);
		Validator.fromValidValues("filterOptions", filter, "favorites", "me", "oracle", "share", "ita", "la", "security");

		filterby = driver.getText("id=oj-select-choice-" + DashBoardPageId_1240.FILTER_SELECT_ID).trim();

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
			case "ita":
				if("IT Analytics".equals(filterby)) isSelected=true;
				break;
			case "la":
				if("Log Analytics".equals(filterby)) isSelected=true;
				break;
			case "security":
				if("Security Analytics".equals(filterby)) isSelected=true;
				break;
			default:
				break;
		}
		return isSelected;
	}

}
