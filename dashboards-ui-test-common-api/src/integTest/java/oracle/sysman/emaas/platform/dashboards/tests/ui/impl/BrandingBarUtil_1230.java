package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1180;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

/**
 * Created by xiadai on 2017/8/30.
 */
public class BrandingBarUtil_1230 extends BrandingBarUtil_1180 {
    @Override
    public void verifyUserMenuItemByText(WebDriver driver, String item, String text){
        if (isHamburgerMenuEnabled(driver)) {
            if (isHamburgerMenuDisplayed(driver)) {
                driver.getLogger().info("Hamburger menu displayed, need to hide it");
                Assert.assertFalse(toggleHamburgerMenu(driver), "Hide Hamburger Menu failed!");
            }
        }
        driver.getLogger().info("Start to verify branding bar user menu item with text: " + text);
        driver.waitForElementPresent(DashBoardPageId.BRAND_BAR_USER_MENU);
        driver.getLogger().info("Click branding bar user menu button.");
        driver.click(DashBoardPageId.BRAND_BAR_USER_MENU);
        Assert.assertTrue(driver.isDisplayed("id=" + DashBoardPageId.USERMENUPOPUPID));
        driver.getLogger().info("User menu popup is displayed.");
        WebElement menuItem = driver.getWebDriver().findElement(By.cssSelector(item));
        driver.takeScreenShot();
        Assert.assertTrue(text.equals(menuItem.getText()));
        driver.getLogger().info("verifyUserMenuItemByText finished");
    }

    public boolean isHamburgerMenuEnabled(WebDriver driver)
    {
        if (driver.isElementPresent("css=" + DashBoardPageId_1180.HAMBURGERMENU_ICON_CSS)) {
            return true;
        }
        else {
            return false;
        }
    }

}
