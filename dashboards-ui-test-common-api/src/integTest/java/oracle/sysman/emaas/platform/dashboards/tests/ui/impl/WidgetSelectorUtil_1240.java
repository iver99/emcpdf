package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.*;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;


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

        boolean isDialog = driver.isDisplayed("id=" + "widget-selector-okbtn");
        if(isDialog){
            addWidgetInDialog(driver, widgetName);
        }else{
            addWidgetInPopUp(driver, widgetName);
        }
        driver.getLogger().info("addWidget finished, widgetName=" + widgetName);
    }

    public void addWidgetInPopUp(WebDriver driver, String widgetName){
        driver.getLogger().info("go into widget list popup");

        driver.moveToElement(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERSEARCHINPUTCSS));
        driver.clear(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERSEARCHINPUTCSS));
        driver.moveToElement(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERSEARCHINPUTCSS));
        driver.click(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERSEARCHINPUTCSS));
        driver.sendKeys(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERSEARCHINPUTCSS), widgetName);

        Assert.assertEquals(driver.getAttribute(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERSEARCHINPUTCSS)+"@value"), widgetName);

        driver.waitForElementPresent(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERSEARCHBUTTONCSS));
        driver.click(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERSEARCHBUTTONCSS));

        WaitUtil.waitForPageFullyLoaded(driver);

        driver.getLogger().info("Searching widget finished!!");

        driver.getLogger().info("[DashboardHomeUtil] start to add widget from right drawer");

        int widgetCount= driver.getElementCount(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERWIDGETCSS));
        if (widgetCount <=0 ) {
            throw new NoSuchElementException("Right drawer widget for widget name =" + widgetName + " is not found");
        }
        WaitUtil.waitForPageFullyLoaded(driver);

        driver.waitForElementPresent(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERWIDGETCSS));

        driver.click(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERWIDGETCSS));
    }

    public void addWidgetInDialog(WebDriver driver, String widgetName){
        driver.getLogger().info("go into widget list dialog");
        searchWidget(driver, widgetName);
        String autoCloseCheck = driver.getAttribute(DashBoardPageId.WIDGET_SELECTOR_WIDGET_AREA + "@data-wgt-slt-auto-close");
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

    public int getWidgetElementByTitle(WebDriver driver, String widgetName, int index)
    {
        String xpath = XPathLiteral.getXPath(widgetName, driver.getLogger());
        String widgetItemByNameLocator = String.format(DashBoardPageId.WIDGET_SELECTOR_WIDGET_ITEMS_BY_TITLE, xpath);

        driver.click(widgetItemByNameLocator);

        int tileTitlesCount = driver.getElementCount("xpath=" + widgetItemByNameLocator);

        if (tileTitlesCount <= index) {
            throw new NoSuchElementException("Widget with widgetName=" + widgetName + ", index=" + index + " is not found");
        }

        int i = index + 1;

        driver.click("xpath=(" + widgetItemByNameLocator + ")[" + i + "]");
        return i;
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
