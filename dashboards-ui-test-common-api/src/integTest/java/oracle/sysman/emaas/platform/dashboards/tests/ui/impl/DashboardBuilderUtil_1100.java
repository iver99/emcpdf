package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1100;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

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
		driver.getLogger().info("Move widget");
				
		int elementCount = driver.getElementCount(DashBoardPageId_1100.WidgetTitleXpath);
		driver.getLogger().info("Find Elemement number is :" + String.valueOf(elementCount));
		int count = 1;
		int i = 0;
		String widgetName_temp = "";
		for(i=1; i<elementCount+1; i++)
		{			
			widgetName_temp = driver.getAttribute(DashBoardPageId_1100.WidgetTitleXpath+"["+i+"]@data-tile-name");
			driver.getLogger().info("Widget name found: " + widgetName_temp);
			if(widgetName.equals(widgetName_temp))
			{
				if(count==index+1)
				{
					driver.getLogger().info("start to move the mouse");
					driver.moveToElement(DashBoardPageId_1100.WidgetTitleXpath+"["+i+"]"+DashBoardPageId_1100.WidgetTileTitleXpath);
					driver.getLogger().info("move end");
					break;
				}
				count++;
			}
			
		}
//		
//
//		WebElement widgetEl = null;
//		try {
//			widgetEl = getWidgetByName(driver, widgetName, index);
//		}
//		catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//        focusOnWidgetHeader(driver, widgetEl);
//        driver.takeScreenShot();
//        driver.savePageToFile();

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
        driver.click(DashBoardPageId_1100.WidgetTitleXpath+"["+i+"]" + DashBoardPageId_1100.WidgetConfigTileXpath);

        driver.click("css=" + tileMoveCSS);
        driver.getLogger().info("Move the widget: " + moveOption);
   
    }
    
    @Override
    public void moveWidget(WebDriver driver, String widgetName, String moveOption)
    {
    	moveWidget(driver, widgetName, 0, moveOption);
    } 

}
