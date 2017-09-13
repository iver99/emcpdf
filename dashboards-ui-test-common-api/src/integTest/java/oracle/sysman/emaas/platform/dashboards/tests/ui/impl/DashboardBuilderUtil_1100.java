package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1100;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class DashboardBuilderUtil_1100 extends DashboardBuilderUtil_190
{
	public static final String TILE_UP = "up";
	public static final String TILE_DOWN = "down";
	public static final String TILE_LEFT = "left";
	public static final String TILE_RIGHT = "right";

	@Override
	public void moveWidget(WebDriver driver, String widgetName, int index, String moveOption)
	{
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);
		Validator.fromValidValues("moveOption", moveOption, TILE_UP, TILE_DOWN, TILE_LEFT, TILE_RIGHT);

		WebElement widgetEl = null;
		try {
			widgetEl = getWidgetByName(driver, widgetName, index);
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        focusOnWidgetHeader(driver, widgetEl);
        driver.takeScreenShot();
        driver.savePageToFile();

        String tileMoveCSS = null;
        switch (moveOption) {
            case TILE_UP:
            	tileMoveCSS = DashBoardPageId_1100.UpTileCSS;
                break;
            case TILE_DOWN:
            	tileMoveCSS = DashBoardPageId_1100.DownTileCSS;
                break;
            case TILE_LEFT:
            	tileMoveCSS = DashBoardPageId_1100.LeftTileCSS;
                break;
            case TILE_RIGHT:
            	tileMoveCSS = DashBoardPageId_1100.RightTileCSS;
                break;
            default:
                break;
        }
        if (null == tileMoveCSS) {
            return;
        }

        widgetEl.findElement(By.cssSelector(DashBoardPageId_1100.ConfigTileCSS)).click();
        driver.click("css=" + tileMoveCSS);
        driver.getLogger().info("Move the widget: " + moveOption);
   
    }
    
    @Override
    public void moveWidget(WebDriver driver, String widgetName, String moveOption)
    {
    	moveWidget(driver, widgetName, 0, moveOption);
    }
    
    protected void focusOnWidgetHeader(WebDriver driver, WebElement widgetElement)
    {
        if (null == widgetElement) {
            driver.getLogger().info("Fail to find the widget element");
            driver.takeScreenShot();
            driver.savePageToFile();
            throw new NoSuchElementException("Widget config menu is not found");
        }

        WebElement widgetHeader = widgetElement.findElement(By.cssSelector(DashBoardPageId_1100.TileTitleCSS));
        Actions actions = new Actions(driver.getWebDriver());
        actions.moveToElement(widgetHeader).build().perform();
        driver.getLogger().info("Focus to the widget");
    }

	protected WebElement getWidgetByName(WebDriver driver, String widgetName, int index) throws InterruptedException
	{
		if (widgetName == null) {
			return null;
		}

		List<WebElement> widgets = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId_1100.WidgetTitleCSS));
		WebElement widget = null;
		int counter = 0;
		for (WebElement widgetElement : widgets) {
			WebElement widgetTitle = widgetElement.findElement(By.cssSelector(DashBoardPageId_1100.TileTitleCSS));
			Validator.notNull("widgetTitle", widgetTitle);
			String widgetAttribute = widgetTitle.getAttribute("data-tile-title");
			Validator.notNull("widgetTitleAttribute", widgetAttribute);

			if (widgetAttribute.trim().equals(widgetName)) {
				if (counter == index) {
					widget = widgetElement;
					break;
				}
				counter++;
			}
		}
		return widget;
	}

}
