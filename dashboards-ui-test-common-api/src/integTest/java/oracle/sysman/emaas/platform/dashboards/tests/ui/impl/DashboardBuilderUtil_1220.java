package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1220;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;

public class DashboardBuilderUtil_1220 extends DashboardBuilderUtil_1200{
    @Override
    public void addWidgetToDashboard(WebDriver driver, String widgetName){
        Validator.notNull("widgetName", widgetName);
        Validator.notEmptyString("widgetName", widgetName);
        if(isEmpty(widgetName)) return;

        WebDriverWait webDriverWait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        driver.waitForElementVisible(generateCssLocator(DashBoardPageId_190.RIGHTDRAWERCSS));
        WaitUtil.waitForPageFullyLoaded(driver);

        driver.getLogger().info("[DashboardHomeUtil] call addWidgetToDashboard with widget Name as " + widgetName);
        showRightDrawer(driver, WRENCH);

        WebElement searchInput = driver.getElement(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERSEARCHINPUTCSS));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(searchInput));
        Actions actions = new Actions(driver.getWebDriver());
        actions.moveToElement(searchInput).build().perform();
        searchInput.clear();
        WaitUtil.waitForPageFullyLoaded(driver);
        actions.moveToElement(searchInput).build().perform();
        driver.click(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERSEARCHINPUTCSS));
        searchInput.sendKeys(widgetName);
        driver.takeScreenShot();
        driver.savePageToFile();
        Assert.assertEquals(searchInput.getAttribute("value"), widgetName);
        WebElement searchButton = driver.getElement(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERSEARCHBUTTONCSS));
        driver.waitForElementPresent(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERSEARCHBUTTONCSS));
        searchButton.click();
        driver.getLogger().info("[DashboardHomeUtil] start to add widget from right drawer");
        List<WebElement> matchingWidgets = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId_1220.RIGHTDRAWERWIDGETCSS));
        if (matchingWidgets == null || matchingWidgets.isEmpty()) {
            throw new NoSuchElementException("Right drawer widget for widget name =" + widgetName + " is not found");
        }
        WaitUtil.waitForPageFullyLoaded(driver);

        Actions builder = new Actions(driver.getWebDriver());
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(DashBoardPageId_1220.RIGHTDRAWERWIDGETCSS)));
            builder.moveToElement(driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId_1220.RIGHTDRAWERWIDGETCSS)))
                    .build().perform();
        }        catch (IllegalArgumentException e) {
            throw new NoSuchElementException("Widget for " + widgetName + " is not found");
        }
        driver.getLogger().info("Focus to the widget");
        driver.takeScreenShot();
        driver.savePageToFile();
        //driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId_190.RIGHDRAWER_WIDGET_ADD_CSS)).click();
        driver.click(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERWIDGETCSS));
        driver.getLogger().info("Add the widget");
        driver.getWebDriver().switchTo().activeElement().sendKeys(Keys.ENTER);
        driver.takeScreenShot();
        driver.savePageToFile();
        driver.getLogger().info("[DashboardHomeUtil] finish adding widget from right drawer");
        //clear search box
        searchInput.clear();
        WaitUtil.waitForPageFullyLoaded(driver);
        driver.takeScreenShot();
        driver.savePageToFile();
        hideRightDrawer(driver);
    }

    public void showRightDrawer(WebDriver driver, String buttonName)
    {
        driver.waitForElementPresent("css=" + DashBoardPageId_190.RIGHTDRAWERCSS);
        if (isRightDrawerVisible(driver) != false) {
            hideRightDrawer(driver);
        }
        switch (buttonName) {
            case PENCIL:
                driver.click("css=" + DashBoardPageId_190.RIGHTDRAWERTOGGLEPENCILBTNCSS);
                break;
            case WRENCH:
                driver.click("css=" + DashBoardPageId_190.RIGHTDRAWERTOGGLEWRENCHBTNCSS);
                break;
            default:
                driver.takeScreenShot();
                driver.savePageToFile();
                return;
        }
        driver.getLogger().info("[DashboardBuilderUtil] triggered showRightDrawer and show build dashboard.");
        WaitUtil.waitForPageFullyLoaded(driver);
        driver.takeScreenShot();
        driver.savePageToFile();
    }

    public boolean isRightDrawerVisible(WebDriver driver)
    {
        WebElement rightDrawerPanel = driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId_190.RIGHTDRAWERPANELCSS));
        boolean isDisplayed = "none".equals(rightDrawerPanel.getCssValue("display")) != true;
        driver.getLogger().info("DashboardBuilderUtil.isRightDrawerVisible,the isDisplayed value is " + isDisplayed);

        boolean isWidthValid = "0px".equals(rightDrawerPanel.getCssValue("width")) != true;
        driver.getLogger().info("DashboardBuilderUtil.isRightDrawerVisible,the isWidthValid value is " + isWidthValid);

        return isDisplayed && isWidthValid;
    }

    public String generateCssLocator(String cssLocator){
        if(isEmpty(cssLocator)) return "";
        return "css="+cssLocator;
    }
    public boolean isEmpty(String target){
        return (target == null || target.length() == 0) ;
    }
}
