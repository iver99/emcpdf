package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.*;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;

/**
 * Created by xiadai on 2017/9/28.
 */
public class WidgetSelectorUtil_1240 extends WidgetSelectorUtil_1230 {
    @Override
    protected void searchWidget(WebDriver driver, String widgetName)
    {
        driver.waitForElementPresent(DashBoardPageId.WIDGET_SELECTOR_WIDGET_AREA);
        driver.clear(DashBoardPageId_1230.WIDGET_SELECTOR_SEARCH_INPUT_LOCATOR);
        driver.takeScreenShot();
        driver.savePageToFile();
        driver.sendKeys(DashBoardPageId_1230.WIDGET_SELECTOR_SEARCH_INPUT_LOCATOR, widgetName);
        driver.click(DashBoardPageId.WIDGET_SELECTOR_SEARCH_BTN);
    }

    @Override
    public void addWidget(WebDriver driver, String widgetName){
        driver.getLogger().info("addWidget started, widgetName=" + widgetName);
        Validator.notEmptyString("widgetName", widgetName);

        boolean isDialog = driver.isDisplayed("id=" + "widget-selector-widgetgroups");
        if(isDialog){
            addWidgetInDialog(driver, widgetName);
        }else{
            addWidgetInPopUp(driver, widgetName);
        }
        driver.getLogger().info("addWidget finished, widgetName=" + widgetName);
    }

    public void addWidgetInPopUp(WebDriver driver, String widgetName){
        driver.getLogger().info("go into widget list popup");
        WebDriverWait webDriverWait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);

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
        }catch (IllegalArgumentException e) {
            throw new NoSuchElementException("Widget for " + widgetName + " is not found");
        }

        driver.getLogger().info("Focus to the widget");
        driver.takeScreenShot();
        driver.savePageToFile();
        driver.click(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERWIDGETCSS));
    }

    public void addWidgetInDialog(WebDriver driver, String widgetName){
        driver.getLogger().info("go into widget list dialog");
        searchWidget(driver, widgetName);
        String autoCloseCheck = driver.getElement(DashBoardPageId.WIDGET_SELECTOR_WIDGET_AREA).getAttribute("data-wgt-slt-auto-close");
        Boolean autoClose = Boolean.valueOf(autoCloseCheck);
        getWidgetElementByTitle(driver, widgetName, 0);
        driver.click(DashBoardPageId.WIDGET_SELECTOR_OK_BTN_LOCATOR);
        if (!autoClose) {
            try {
                closeDialog(driver);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public WebElement getWidgetElementByTitle(WebDriver driver, String widgetName, int index) {
        String xpath = XPathLiteral.getXPath(widgetName, driver.getLogger());
        String widgetItemByNameLocator = String.format(DashBoardPageId.WIDGET_SELECTOR_WIDGET_ITEMS_BY_TITLE, xpath);

        driver.click(widgetItemByNameLocator);

        List<WebElement> tileTitles = driver.getWebDriver().findElements(By.xpath(widgetItemByNameLocator));
        if (tileTitles == null || tileTitles.size() <= index) {
            throw new NoSuchElementException("Widget with widgetName=" + widgetName + ", index=" + index + " is not found");
        }
        tileTitles.get(index).click();
        driver.takeScreenShot();
        driver.savePageToFile();
        return tileTitles.get(index);
    }

    public void closeDialog(WebDriver driver){
        driver.getLogger().info("(Internal method) closeDialog started");
        driver.click(DashBoardPageId.WIDGET_SELECTOR_CLOSE_BTN_LOCATOR);

        driver.waitForNotElementPresent(DashBoardPageId.WIDGET_SELECTOR_CLOSE_BTN_LOCATOR);

        driver.getLogger().info("(Internal method) closeDialog completed");
    }

    public String generateCssLocator(String cssLocator){
        if(isEmpty(cssLocator)) return "";
        return "css="+cssLocator;
    }

    public boolean isEmpty(String target){
        return (target == null || target.length() == 0) ;
    }
}
