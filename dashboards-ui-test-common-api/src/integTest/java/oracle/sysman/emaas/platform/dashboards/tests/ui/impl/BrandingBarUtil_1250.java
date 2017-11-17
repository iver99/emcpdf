package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1180;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.NoSuchElementException;

/**
 * Created by xiadai on 2017/8/30.
 */
public class BrandingBarUtil_1250 extends BrandingBarUtil_1230 {
    @Override
    public void clickMenuItem(WebDriver driver, String menuitem)
    {
        boolean isExisted = false;
        Validator.notEmptyString("menuitem in [clickMenuItem]", menuitem);

		if (!isHamburgerMenuDisplayed(driver)) {
			driver.getLogger().info("Not displayed hamburger menu, need to show it");
			clickHamburgerMenuIcon(driver);
		}
		
		int menuitemCount = driver.getElementCount("xpath=" + DashBoardPageId_1180.HAMBURGERMENU_MENUITEM_LABEL_XPATH);
		if (menuitemCount <= 0) {
			throw new NoSuchElementException("clickMenuItem: the menuitem element is not found");
		}
		
		for (int i=1; i<=menuitemCount; i++) {
			if (menuitem.equals(driver.getText("xpath=(" + DashBoardPageId_1180.HAMBURGERMENU_MENUITEM_LABEL_XPATH + ")[" + i +"]").trim()) &&
			    driver.isDisplayed("xpath=(" + DashBoardPageId_1180.HAMBURGERMENU_MENUITEM_LABEL_XPATH + ")[" + i +"]") &&
			    isElementEnabled(driver, i)) {
			    	isExisted = true;
					boolean viewportScreenshot = false;
					try {
						driver.click("xpath=(" + DashBoardPageId_1180.HAMBURGERMENU_MENUITEM_LABEL_XPATH + ")[" + i +"]");
					}
					catch (Exception e) {
						viewportScreenshot = true;
						throw e;
					}
					finally {
						driver.capturePageState(viewportScreenshot);
					}

					WaitUtil.waitForPageFullyLoaded(driver);
					driver.getLogger().info("clickMenuItem has click on the given menu item: " + menuitem);
					break;
			}
		}
		if (!isExisted) {
			throw new NoSuchElementException("clickMenuItem: the menuitem '" + menuitem + "' is not found");
		}
    }

    @Override
    public boolean isHamburgerMenuDisplayed(WebDriver driver)
    {
        WaitUtil.waitForPageFullyLoaded(driver);
        driver.takeScreenShot();
        driver.savePageToFile();
        if (driver.isDisplayed("css=" + DashBoardPageId_1180.HAMBURGERMENU_CONTAINER_CSS)) {
            return true;
        }
        else {
            return false;
        }
    }

}
