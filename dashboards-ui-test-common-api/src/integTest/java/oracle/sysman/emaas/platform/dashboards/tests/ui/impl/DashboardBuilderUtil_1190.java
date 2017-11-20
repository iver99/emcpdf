package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.*;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class DashboardBuilderUtil_1190 extends DashboardBuilderUtil_1150
{
	@Override
	public boolean verifyDashboard(WebDriver driver, String dashboardName, String description, boolean showTimeSelector) {
		driver.getLogger().info(
				"DashboardBuilderUtil.verifyDashboard started for name=\"" + dashboardName + "\", description=\"" + description
						+ "\", showTimeSelector=\"" + showTimeSelector + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);

		driver.waitForElementPresent(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR);
		driver.waitForElementVisible(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR);
		driver.click(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR);
		
		String realName = driver.getAttribute(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR + "@title");
		if (!dashboardName.equals(realName)) {
			driver.getLogger().info(
					"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected dashboard name is "
							+ dashboardName + ", actual dashboard name is " + realName);
			return false;
		}

		if (driver.isElementPresent(DashBoardPageId_1120.BUILDERDESCRIPTIONTEXTLOCATOR)) {
			String realDesc = driver.getAttribute(DashBoardPageId_1120.BUILDERDESCRIPTIONTEXTLOCATOR + "@title");
			if (description == null || "".equals(description)) {
				if (realDesc != null && !"".equals(realDesc.trim())) {
					driver.getLogger().info(
							"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected description is "
									+ description + ", actual dashboard description is " + realDesc);
					return false;
				}
			} else {
				if (!description.equals(realDesc)) {
					driver.getLogger().info(
							"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected description is "
									+ description + ", actual dashboard description is " + realDesc);
					return false;
				}
			}
		}else{
			driver.getLogger().info(
					"DashboardBuilderUtil.verifyDashboard: description is disabled.");
		}

		boolean actualTimeSelectorShown = driver.isDisplayed(DashBoardPageId_1150.BUILDERDATETIMEPICKERLOCATOR);
		if (actualTimeSelectorShown != showTimeSelector) {
			driver.getLogger().info(
					"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected showTimeSelector is "
							+ showTimeSelector + ", actual dashboard showTimeSelector is " + actualTimeSelectorShown);
			return false;
		}

		driver.getLogger().info("DashboardBuilderUtil.verifyDashboard compelted and returns true");
		return true;
	}
}
