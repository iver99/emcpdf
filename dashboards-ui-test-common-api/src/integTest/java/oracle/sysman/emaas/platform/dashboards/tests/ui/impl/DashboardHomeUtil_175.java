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

import java.util.ArrayList;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class DashboardHomeUtil_175 extends DashboardHomeUtil_171
{
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#filterOptions(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void filterOptions(WebDriver driver, String filter)
	{
		driver.getLogger().info("[DashboardHomeUtil] call filterOptions filter: " + filter);
		Validator.notEmptyString("filter", filter);
		String[] fs = filter.split(",");
		List<String> trimedFs = new ArrayList<String>();
		for (String s : fs) {
			trimedFs.add(s.trim());
		}
		if (trimedFs.contains("apm")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERAPMLOCATOR);
			driver.click(DashBoardPageId.FILTERAPMLOCATOR);

		}
		if (trimedFs.contains("la")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERLALOCATOR);
			driver.click(DashBoardPageId.FILTERLALOCATOR);

		}
		if (trimedFs.contains("ita")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERITALOCATOR);
			driver.click(DashBoardPageId.FILTERITALOCATOR);

		}
		if (trimedFs.contains("orchestration")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERORCHESTRATIONLOCATOR);
			driver.click(DashBoardPageId.FILTERORCHESTRATIONLOCATOR);

		}
		if (trimedFs.contains("oracle")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERORACLELOCATOR);
			driver.click(DashBoardPageId.FILTERORACLELOCATOR);

		}
		if (trimedFs.contains("share")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERSHARELOCATOR);
			driver.click(DashBoardPageId.FILTERSHARELOCATOR);

		}
		if (trimedFs.contains("me")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERMELOCATOR);
			driver.click(DashBoardPageId.FILTERMELOCATOR);

		}
		if (trimedFs.contains("favorites")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERFAVORITELOCATOR);
			driver.click(DashBoardPageId.FILTERFAVORITELOCATOR);

		}
	}

	@Override
	public boolean isFilterOptionSelected(WebDriver driver, String filter)
	{
		driver.getLogger().info("[DashboardHomeUtil] call isFilterOptionSelected filter: " + filter);
		Validator.notEmptyString("filter", filter);
		if ("orchestration".equals(filter)) {
			return driver.getElement(DashBoardPageId.FILTERORCHESTRATIONLOCATOR).isSelected();
		}
		else {
			return super.isFilterOptionSelected(driver, filter);
		}

	}
}
