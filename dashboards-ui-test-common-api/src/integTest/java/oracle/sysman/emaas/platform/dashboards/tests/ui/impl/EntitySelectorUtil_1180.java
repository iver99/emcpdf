package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * Created by miao on 17/4/6.
 */
public class EntitySelectorUtil_1180 extends EntitySelectorUtil_1170
{
	@Override
	public ArrayList<String> getPillContents(WebDriver driver, Logger logger)
	{
		ArrayList<String> displayNames = new ArrayList<>();
		logger.log(Level.INFO, "Get list of pills in Global Context bar");
		
		int pillsCount = driver.getElementCount("xpath=" + DashBoardPageId.EntSelPills);
		for (int i=1; i<=pillsCount; i++) {
			displayNames.add(driver.getText("xpath=(" + DashBoardPageId.EntSelPills + ")[" + i + "]").replace("Composite: ", ""));
		}
//                List<WebElement> pills = driver.getWebDriver().findElements(By.xpath(DashBoardPageId.EntSelPills));
//                for (WebElement pill : pills) {
//                    //If the pill represents a composite entity, it will include text "Composite: "
//                    //which must be removed in order to get the display name only
//                    displayNames.add(pill.getText().replace("Composite: ", ""));
//                }
//                int count = pills.size();
        logger.log(Level.INFO, "{0} {1} found in Global Context bar: {2}", new Object[]{ pillsCount, 
                		pillsCount == 1 ? "pill was" : "pills were", displayNames.toString() });
        return displayNames;
	}
	
	
	@Override
	public boolean verifyPillExistsByDisplayName(WebDriver driver, Logger logger, String displayName)
	{
		logger.log(Level.INFO, "Check if a pill containing text \"{0}\" exists in Global Context bar.", new Object[]{ displayName });
		WaitUtil.waitForPageFullyLoaded(driver);
		
		int pillsCount = driver.getElementCount("xpath=" + MessageFormat.format(DashBoardPageId.ENTSEL_PILL_BYCONTAINSTEXT, displayName));
		boolean exists = pillsCount > 0;
//		List<WebElement> pills = driver.getWebDriver().findElements(By.xpath(MessageFormat.format(DashBoardPageId.ENTSEL_PILL_BYCONTAINSTEXT, displayName)));
//                boolean exists = pills.size() > 0;
        logger.log(Level.INFO, "Pill with text \"{0}\" exists? {1}", new Object[]{ displayName, exists });
        return exists;
    }
	
	@Override
	public boolean verifyPillExistsByIndex(WebDriver driver, Logger logger, int pillIndex, String displayName)
	{
		logger.log(Level.INFO, "Check if a pill containing text \"{0}\" exists in Global Context bar at position [{1}].", new Object[]{ displayName, pillIndex });
		WaitUtil.waitForPageFullyLoaded(driver);
		
		int pillsCount = driver.getElementCount("xpath=" + MessageFormat.format(DashBoardPageId.ENTSEL_PILL_CONTAINSTEXT_BYINDEX, ++pillIndex, displayName));
		boolean exists = pillsCount == 1;
		logger.log(Level.INFO, "Pill with text \"{0}\" at position [{1}] exists? {2}", new Object[]{ displayName, --pillIndex, exists });
        return exists;
		
                //Take in consideration that XPath uses 1-based indexing
//                List<WebElement> pills = driver.getWebDriver().findElements(By.xpath(MessageFormat.format(DashBoardPageId.ENTSEL_PILL_CONTAINSTEXT_BYINDEX, ++pillIndex, displayName)));
//                boolean exists = pills.size() == 1;
                
    }
}