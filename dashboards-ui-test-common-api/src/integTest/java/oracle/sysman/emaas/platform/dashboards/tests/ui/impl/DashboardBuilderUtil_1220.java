package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1220;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;

public class DashboardBuilderUtil_1220 extends DashboardBuilderUtil_1200
{
    @Override
    public void addWidgetToDashboard(WebDriver driver, String widgetName)
    {
        Validator.notNull("widgetName", widgetName);
        Validator.notEmptyString("widgetName", widgetName);
        if(isEmpty(widgetName)) return;
        
        driver.waitForElementVisible(generateCssLocator(DashBoardPageId_190.RIGHTDRAWERCSS));
        WaitUtil.waitForPageFullyLoaded(driver);

        driver.getLogger().info("[DashboardHomeUtil] call addWidgetToDashboard with widget Name as " + widgetName);
        showRightDrawer(driver, WRENCH);
        
        driver.getLogger().info("Start searching the widget: "+ widgetName);
        
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
        driver.moveToElement(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERWIDGETCSS));
        
        driver.getLogger().info("Add the widget");
        
        driver.sendKeys(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERWIDGETCSS), Keys.chord(Keys.ENTER));
      
        driver.getLogger().info("[DashboardHomeUtil] finish adding widget from right drawer");
        
        //clear search box
        driver.clear(generateCssLocator(DashBoardPageId_1220.RIGHTDRAWERSEARCHINPUTCSS));
        
        WaitUtil.waitForPageFullyLoaded(driver);        
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
                driver.capturePageState();
                return;
        }
        driver.getLogger().info("[DashboardBuilderUtil] triggered showRightDrawer and show build dashboard.");
        WaitUtil.waitForPageFullyLoaded(driver);    
    }    

    public String generateCssLocator(String cssLocator){
        if(isEmpty(cssLocator)) return "";
        return "css="+cssLocator;
    }
    public boolean isEmpty(String target){
        return (target == null || target.length() == 0) ;
    }
}
