/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

/**
 * @author cawei
 */
public class EntitySelectorUtil_1140 extends EntitySelectorUtil_Version implements IEntitySelectorUtil
{
	private static final int UNTIL_TIMEOUT = 900;

	@Override
        public void clearContext(WebDriver driver, Logger logger)
        {
                logger.log(Level.INFO, "Clear global context by removing every pill.");
        
                //Remove all pills from global context bar
                int total = getNumberOfPills(driver, logger);
                for (int i = total - 1; i >= 0; i--) {
                    removePill(driver, logger, i);
                }
                
                int pillCountAfterClear = getNumberOfPills(driver, logger);
                logger.log(Level.INFO, "Global context has been cleared, current pill count: {0}", new Object[] { pillCountAfterClear });
        }

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#getNumberOfPills(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public int getNumberOfPills(WebDriver driver, Logger logger)
	{		
		int count = driver.getElementCount(DashBoardPageId.EntSelPills);
		logger.log(Level.INFO, "{0} pills found", new Object[] { count });
		return count;
	}
	
	@Override
	public ArrayList<String> getPillContents(WebDriver driver, Logger logger)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		logger.info("Method not available in the current version");
		return null;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#openEntitySelector(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void openEntitySelector(WebDriver driver, Logger logger)
	{ //Open Entity Selector to display the suggestions
		logger.log(Level.INFO, "Click Global Context Entity Selector to display suggestions");
		String xpath = "xpath=" + DashBoardPageId.EntSelTypeAheadField;
		driver.click(xpath);

		//Verify suggestions popup is visible
		logger.log(Level.INFO, "Verify Entity Selector suggestions are visible");
		driver.waitForElementVisible("xpath=" + DashBoardPageId.EntSelSuggestionPopup);
		WaitUtil.waitForPageFullyLoaded(driver);
		logger.log(Level.INFO, "Entity Selector options are displayed");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#removePill(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int)
	 */
	@Override
	public void removePill(WebDriver driver, Logger logger, int indexOfPillToRemove)
	{
		logger.log(Level.INFO, "Remove pill from Entity Selector");
		//Take in consideration that XPath uses 1-based indexing
		indexOfPillToRemove++;
		final int prevPillCount = getNumberOfPills(driver, logger);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
		driver.waitForElementEnabled("xpath=" + MessageFormat.format(DashBoardPageId.EntSelPillToRemoveByIndex, indexOfPillToRemove));
//		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(MessageFormat.format(
//				DashBoardPageId.EntSelPillToRemoveByIndex, indexOfPillToRemove))));
		logger.log(Level.INFO, "Click button to remove pill from Entity Selector ");
		driver.click("xpath=" + MessageFormat.format(DashBoardPageId.EntSelPillToRemoveByIndex, indexOfPillToRemove));
		//Wait until the pill is removed
		final WebDriver finalDriver = driver;
                final Logger finalLogger = logger;
		wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(org.openqa.selenium.WebDriver driver)
			{
				return getNumberOfPills(finalDriver, finalLogger) < prevPillCount;
			}
		});
                driver.takeScreenShot();
		logger.log(Level.INFO, "The pill at index {0} has been removed", new Object[] { indexOfPillToRemove });

	}
        
        /* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#replacePillSelection(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, String, String, String)
	 */
	@Override
        public void replacePillSelection(WebDriver driver, Logger logger, int pillIndex, String entityName, String entityType, String category) 
        {
		logger.log(Level.INFO, "Replace selection from pill [{0}]. Number of pills before replace = {1}", new Object[]{ pillIndex, getNumberOfPills(driver, logger) });
                
                //Find pill at desired position
                WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
                //Check if pill can be edited: If link is not clickable, then the pill cannot be edited
                logger.log(Level.INFO, "Check if pill [{0}] is editable. If the link is not available the pill cannot be edited.", new Object[]{pillIndex});
                //Take in consideration that XPath uses 1-based indexing
                pillIndex++;
                WebElement pill = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(MessageFormat.format(DashBoardPageId.EntSelPillLinkToReplaceByIndex, pillIndex))));
                logger.log(Level.INFO, "Click pill to enable editing mode");
                pill.click();
                
                //Write the new entity name
                searchText(driver, logger, entityName);
                
                //Select first option matching entity name, type and category
                selectFirstSuggestionByCategory(driver, logger, category, entityName, entityType, true);
                driver.takeScreenShot();
                logger.log(Level.INFO, "New selection for pill is ''{0}''.", new Object[]{entityName});
        }

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#searchText(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void searchText(WebDriver driver, Logger logger, final String entityName)
	{
		//Write text in entity selector
		logger.log(Level.INFO, "Waiting for Entity Selector input to be clickable");
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By
				.xpath(DashBoardPageId.EntSelTypeAheadFieldInput)));
		logger.log(Level.INFO, "Searching value ''{0}'' in Entity Selector", entityName);
		element.click();
                //Wait until suggestions are displayed before typing the text to avoid timing issues
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.EntSelSuggestionPopup)));
		element.clear();
		element.sendKeys(entityName);
		driver.takeScreenShot();

		//Wait until the results are displayed
		logger.log(Level.INFO, "Waiting for results to be displayed for text ''{0}''", entityName);
                final Logger finalLogger = logger;
		wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(org.openqa.selenium.WebDriver webdriver)
			{
				List<WebElement> resultItems = webdriver.findElements(By.xpath(DashBoardPageId.EntSelSearchResultsItem));
				int count = resultItems.size();
				finalLogger.log(Level.INFO, "Waiting for search results to be updated. Current items displayed = {0}", count);
				List<WebElement> resultItemsByText = webdriver.findElements(By.xpath(MessageFormat.format(
						DashBoardPageId.EntSelSearchResultsItemByText, entityName)));

				return count == resultItemsByText.size();
			}
		});

		driver.takeScreenShot();
		logger.log(Level.INFO, "Results for ''{0}'' are available", entityName);

	}
        
        @Override
	public void searchText(WebDriver driver, Logger logger, final String entityName, final String entityType, final String category)
        {
                Assert.assertTrue(false, "This method is not available in the current version");
                logger.info("Method not available in the current version");
        }

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#selectCompositeEntity(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void selectCompositeEntity(WebDriver driver, Logger logger, String entityName, String entityType)
	{
		//search text in entity selector
		searchText(driver, logger, entityName);

		//select the composite entity found with that description
		selectFirstSuggestionByCategory(driver, logger, CATEGORY_COMPOSITE, entityName, entityType, false);

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#selectEntity(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void selectEntity(WebDriver driver, Logger logger, String entityName, String entityType)
	{
		//search text in entity selector
		searchText(driver, logger, entityName);

		//select the entity found with that description
		selectFirstSuggestionByCategory(driver, logger, CATEGORY_ENTITIES, entityName, entityType, false);

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#validateReadOnlyMode(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean validateReadOnlyMode(WebDriver driver, Logger logger)
	{
                logger.log(Level.INFO, "Check if Global Context is in read-only mode");
		List<WebElement> readOnlyPill = driver.getWebDriver().findElements(By.xpath(DashBoardPageId.EntSelReadOnlyPill));
		boolean isReadOnly = !readOnlyPill.isEmpty();
                logger.log(Level.INFO, "Global Context read-only mode = {0}", isReadOnly);

		return isReadOnly;
	}
        
        @Override
        public void verifyCompositePillContent(WebDriver driver, Logger logger, String text)
        {
                Assert.assertTrue(false, "This method is not available in the current version");
                logger.info("Method not available in the current version");
        }
        
        @Override
        public void verifyEntityPillContent(WebDriver driver, Logger logger, String text)
        {
                Assert.assertTrue(false, "This method is not available in the current version");
                logger.info("Method not available in the current version");
        }
        
        @Override
        public void verifyPillContains(WebDriver driver, Logger logger, String text)
        {
                Assert.assertTrue(false, "This method is not available in the current version");
                logger.info("Method not available in the current version");
        }
        
        @Override
        public void verifyPillContentByIndex(WebDriver driver, Logger logger, int pillIndex, String text)
        {
                Assert.assertTrue(false, "This method is not available in the current version");
                logger.info("Method not available in the current version");  
        }
        
        @Override
        public boolean verifyPillExistsByDisplayName(WebDriver driver, Logger logger, String displayName)
        {
                Assert.assertTrue(false, "This method is not available in the current version");
                logger.info("Method not available in the current version"); 
                return false;
        }
        
        @Override
        public boolean verifyPillExistsByIndex(WebDriver driver, Logger logger, int pillIndex, String displayName)
        {
                Assert.assertTrue(false, "This method is not available in the current version");
                logger.info("Method not available in the current version"); 
                return false;
        }
        
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#selectFirstSuggestionByCategory(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	private void selectFirstSuggestionByCategory(WebDriver driver, Logger logger, String category, String entityName, String entityType, boolean isEditingPill)
	{
		//select the first composite entity that matches category and entity type
		logger.log(Level.INFO, "Waiting for the matching suggestion to be clickable");
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
		String xpath = category == CATEGORY_COMPOSITE ? MessageFormat.format(DashBoardPageId.EntSelSuggestionByCompositeCategory,
				entityType) : MessageFormat.format(DashBoardPageId.EntSelSuggestionByEntitiesCategory, entityType);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		logger.log(Level.INFO, "Click on first available suggestion");
                // TODO Auto-generated method stub
		final int prevCount = getNumberOfPills(driver, logger);
		element.click();
                
                if (!isEditingPill) {
                    //Wait until the page is done loading and the new pill is displayed
                    waitForNewPill(driver, logger, prevCount);
                } else {
                    //Wait for the edited pill to be refreshed
                    waitForEditedPill(driver, logger, prevCount, entityName);
                }

		logger.log(Level.INFO, "Option ''{0}'' (''{1}'') was successfully selected.", new Object[]{ entityName, entityType });
	}
        
        /* (non-Javadoc)
	* @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#waitForEditedPill(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, String)
	*/
        private void waitForEditedPill(WebDriver driver, Logger logger, final int pillCount, String text) 
        {
                logger.log(Level.INFO, "Waiting for the type ahead input to disappear");
                //Wait for typeahead input to dissappear
                driver.waitForElementNotVisible("xpath=" + DashBoardPageId.EntSelTypeAheadFieldInput);
                
                logger.log(Level.INFO, "Waiting for the edited pill to be updated");
                WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
                final WebDriver finalDriver = driver;
                final Logger finalLogger = logger;
                //make sure the amount of pills didn't change
                wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(org.openqa.selenium.WebDriver driver)
			{
                                return getNumberOfPills(finalDriver, finalLogger) == pillCount;
			}
		});
                
                //search for the new value in the edited pill
                driver.waitForElementVisible("xpath=" + MessageFormat.format(DashBoardPageId.EntSelEditedPillByText, text));
                logger.log(Level.INFO, "The pill was successfully edited with value: ''{0}''.", new Object[]{text});
                driver.takeScreenShot();
        }

	/* (non-Javadoc)
	* @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#waitForNewPill(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	*/
	private void waitForNewPill(WebDriver driver, Logger logger, final int prevPillCount)
	{
		logger.log(Level.INFO, "Waiting for new pill to be displayed");
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
		final WebDriver finalDriver = driver;
                final Logger finalLogger = logger;
                //wait until the number of pills is updated
		wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(org.openqa.selenium.WebDriver driver)
			{
				return getNumberOfPills(finalDriver, finalLogger) == prevPillCount + 1;
			}
		});
		logger.log(Level.INFO, "A new pill has been added to the Global Context bar");
		driver.takeScreenShot();

	}

}
