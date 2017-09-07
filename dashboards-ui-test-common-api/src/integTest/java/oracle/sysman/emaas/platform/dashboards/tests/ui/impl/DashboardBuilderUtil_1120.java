package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1120;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * Created by xiadai on 2016/10/12.
 */
public class DashboardBuilderUtil_1120 extends DashboardBuilderUtil_1100
{
	@Override
	public boolean verifyDashboard(WebDriver driver, String dashboardName, String description, boolean showTimeSelector)
	{
		driver.getLogger().info(
				"DashboardBuilderUtil.verifyDashboard started for name=\"" + dashboardName + "\", description=\"" + description
						+ "\", showTimeSelector=\"" + showTimeSelector + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);

		driver.waitForElementVisible(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR);
		driver.click(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR);
		String realName = driver.getElement(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR).getAttribute("title");
		if (!dashboardName.equals(realName)) {
			driver.getLogger().info(
					"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected dashboard name is "
							+ dashboardName + ", actual dashboard name is " + realName);
			return false;
		}

		driver.waitForElementPresent(DashBoardPageId_1120.BUILDERDESCRIPTIONTEXTLOCATOR);		
		String realDesc = driver.getElement(DashBoardPageId_1120.BUILDERDESCRIPTIONTEXTLOCATOR).getAttribute("title");
		if (description == null || "".equals(description)) {
			if (realDesc != null && !"".equals(realDesc.trim())) {
				driver.getLogger().info(
						"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected description is "
								+ description + ", actual dashboard description is " + realDesc);
				return false;
			}
		}
		else {
			if (!description.equals(realDesc)) {
				driver.getLogger().info(
						"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected description is "
								+ description + ", actual dashboard description is " + realDesc);
				return false;
			}
		}

		boolean actualTimeSelectorShown = driver.isDisplayed(DashBoardPageId_190.BUILDERDATETIMEPICKERLOCATOR);
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
