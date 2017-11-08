package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1180;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

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

        List<WebElement> webd_menuitem = driver.getWebDriver().findElements(
                By.cssSelector(DashBoardPageId_1180.HAMBURGERMENU_MENUITEM_LABEL_CSS));
        if (webd_menuitem == null || webd_menuitem.isEmpty()) {
            throw new NoSuchElementException("clickMenuItem: the menuitem element is not found");
        }
        for (WebElement nav : webd_menuitem) {
            if (nav.getText().trim().equals(menuitem) && nav.isDisplayed() && isElementEnabled(driver, nav)) {
                isExisted = true;
                boolean viewportScreenshot = false;
                try {
                    nav.click();
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

}
