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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

/**
 * @author cawei
 */
public class EntitySelectorUtil_1160 extends EntitySelectorUtil_1150
{
	private static final int UNTIL_TIMEOUT = 900;

	@Override
        public void clearContext(WebDriver driver, Logger logger)
        {
                logger.log(Level.INFO, "Clear global context by removing every pill.");
                //wait until previous processes are completed
                WaitUtil.waitForPageFullyLoaded(driver);
        
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
		List<WebElement> entSelectorPills = driver.getWebDriver().findElements(By.xpath(DashBoardPageId.EntSelPills));
		int count = entSelectorPills.size();
		logger.log(Level.INFO, "{0} pills found", new Object[] { count });
		return count;
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
		WebDriverWait waitPopup = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
		waitPopup.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.EntSelSuggestionPopup)));
		driver.takeScreenShot();
		logger.log(Level.INFO, "Entity Selector options are displayed");

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#removePill(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int)
	 */
	@Override
	public void removePill(WebDriver driver, Logger logger, int indexOfPillToRemove)
	{
		logger.log(Level.INFO, "Remove pill at index {0} from Global Context bar", new Object[]{ indexOfPillToRemove });
		//Take in consideration that XPath uses 1-based indexing
		indexOfPillToRemove++;
                
                //Determine if user is trying to remove a regular pill or one being edited
                boolean isEditable = driver.isDisplayed("xpath=" + DashBoardPageId.EntSelTypeAheadFieldInput);
                
		final int prevPillCount = getNumberOfPills(driver, logger);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(MessageFormat.format(
				DashBoardPageId.EntSelPillToRemoveByIndex, indexOfPillToRemove))));
		logger.log(Level.INFO, "Click button to remove pill");
		element.click();
                
                if (isEditable) {
                    //editing hides the pill, therefore we cannot rely on the number 
                    //of pills to validate the element was removed
                    WaitUtil.waitForPageFullyLoaded(driver);
                } else {
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
                }
                
                driver.takeScreenShot();
                driver.savePageToFile();
                logger.log(Level.INFO, "The pill at index {0} has been removed", new Object[] { --indexOfPillToRemove });
	}
        
        /* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#replacePillSelection(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, String, String, String)
	 */
	@Override
        public void replacePillSelection(WebDriver driver, Logger logger, int pillIndex, String entityName, String entityType, String category) 
        {
		logger.log(Level.INFO, "Replace selection from pill [{0}]. Number of pills before replace = {1}", new Object[]{ pillIndex, getNumberOfPills(driver, logger) });
                //wait until previous processes are completed
                WaitUtil.waitForPageFullyLoaded(driver);
                
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
                searchText(driver, logger, entityName, entityType, category);
                
                //Select first option matching entity name, type and category
                selectSuggestionByCategory(driver, logger, category, entityName, entityType, true);
                driver.takeScreenShot();
                logger.log(Level.INFO, "New selection for pill is ''{0}''.", new Object[]{entityName});
        }

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#searchText(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void searchText(WebDriver driver, Logger logger, final String entityName, final String entityType, final String category)
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
                //metric with parenthesis in name loses the left parenthesis when added to data palette search box
                //see Selenium Issue 1723 - Firefox Driver send_keys won't send left parenthesis
                //Using workaround suggested in issue
                element.sendKeys(interceptStringForSearch(logger, entityName));
		driver.takeScreenShot();

		//Wait until the results are displayed
                logger.log(Level.INFO, "Waiting for results to be displayed for text ''{0}''", entityName);
                WaitUtil.waitForPageFullyLoaded(driver);
                waitForSuggestionsRefreshed(driver, logger, entityName, entityType, category);
                
		driver.takeScreenShot();
		logger.log(Level.INFO, "Results for ''{0}'' are available", entityName);

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#selectCompositeEntity(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void selectCompositeEntity(WebDriver driver, Logger logger, String entityName, String entityType)
	{
		//search text in entity selector
		searchText(driver, logger, entityName, entityType, CATEGORY_COMPOSITE);

		//select the first composite entity found with that description
		selectSuggestionByCategory(driver, logger, CATEGORY_COMPOSITE, entityName, entityType, false);

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#selectEntity(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void selectEntity(WebDriver driver, Logger logger, String entityName, String entityType)
	{
		//search text in entity selector
		searchText(driver, logger, entityName, entityType, CATEGORY_ENTITIES);

		//select the first entity found with that description
		selectSuggestionByCategory(driver, logger, CATEGORY_ENTITIES, entityName, entityType, false);

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
        public void verifyCompositePillContent(WebDriver driver, Logger logger, String displayName)
        {
                Assert.assertTrue(false, "This method is not available in the current version");
                logger.info("Method not available in the current version");
        }
        
        @Override
        public void verifyEntityPillContent(WebDriver driver, Logger logger, String displayName)
        {
                Assert.assertTrue(false, "This method is not available in the current version");
                logger.info("Method not available in the current version");
        }
        
        @Override
        public void verifyPillContains(WebDriver driver, Logger logger, String displayName)
        {
                Assert.assertTrue(false, "This method is not available in the current version");
                logger.info("Method not available in the current version");
        }
        
        @Override
        public void verifyPillContentByIndex(WebDriver driver, Logger logger, int pillIndex, String displayName)
        {
                Assert.assertTrue(false, "This method is not available in the current version");
                logger.info("Method not available in the current version");  
        }
        
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#selectFirstSuggestionByCategory(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	private void selectSuggestionByCategory(WebDriver driver, Logger logger, String category, String entityName, String entityType, boolean isEditingPill)
	{
		//select the composite entity that matches category and entity type
		logger.log(Level.INFO, "Waiting for the matching suggestion to be clickable");
                driver.takeScreenShot();
                driver.savePageToFile();
                
                //Click on the suggestion that matches the search parameters
                final int prevCount = getNumberOfPills(driver, logger);
                logger.log(Level.INFO, "Click on matching suggestion");
                WebElement element = getMatchingSuggestion(driver, logger, entityName, entityType, category);
		element.click();
                driver.takeScreenShot();
                driver.savePageToFile();
                
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
                driver.waitForNotElementPresent("xpath=" + DashBoardPageId.EntSelTypeAheadFieldInput);
                
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
                
                //Disable pill text verification since it's possible to have entity name different than 
                // entity display name, in which case a timeout will take place
                //Pill text verification can be done after the replacement using the "entSel_verifyPill..." methods, if so desired.
                //search for the new value in the edited pill
                //driver.waitForElementVisible("xpath=" + MessageFormat.format(DashBoardPageId.EntSelEditedPillByText, text));
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
        
        /* (non-Javadoc)
	* @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#interceptStringForSearch(java.util.logging.Logger, String)
	*/
        private String interceptStringForSearch(Logger logger, String text) {
                String correct = new String();
                for (int i = 0; i < text.length(); i++) {
                    if (text.charAt(i) == '(') {
                        logger.info("Replace left parenthesis with shift 9");
                        correct += new String(Keys.chord(Keys.SHIFT, "9"));
                    } else {
                        correct += text.charAt(i);
                    }
                }
                return correct;
        }
        
        private void waitForSuggestionsRefreshed(WebDriver driver, Logger logger, String entityName, String entityType, String category) {
                final String xpath = category.equals(CATEGORY_COMPOSITE) ? MessageFormat.format(DashBoardPageId.EntSelSuggestionByCompositeCategory,
                                    entityType) : MessageFormat.format(DashBoardPageId.EntSelSuggestionByEntitiesCategory, entityType);
                final String trimmed = entityName.replaceAll(" +", "");
                
                logger.log(Level.INFO, "Looking for value in typeahead: {0}", trimmed);
                driver.savePageToFile();
                driver.takeScreenShot();
                
                final Logger finalLogger = logger;
                WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
                // wait until suggest popup has been refreshed with new text
                // note that even after desired entry is found, check the remaining
                // entries just to make sure there are no stale elements because if
                // there are, then the popup is still in the process of being 
                // refreshed with the new entries
                wait.until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(org.openqa.selenium.WebDriver webdriver) {
                        List<WebElement> elements = webdriver.findElements(By.xpath(xpath));
                        boolean found = false;
                        for (WebElement elem : elements) {
                            try {
                                String value = elem.getText().replaceAll(" +", "").split("\n")[0];
                                finalLogger.log(Level.INFO, "Typeahead popup entry is: {0}", value);
                                if (value.contains(trimmed)) {
                                    finalLogger.log(Level.INFO, "Found {0} on typeahead popup (contains)", trimmed);
                                    found = true;
                                }
                            } catch (StaleElementReferenceException e) {
                                finalLogger.info("Element was stale, return false");
                                return false;
                            }
                        }
                        return found;
                    }
                });
        }
        
        private WebElement getMatchingSuggestion(WebDriver driver, Logger logger, String entityName, String entityType, String category) {
                String xpath = category.equals(CATEGORY_COMPOSITE) ? MessageFormat.format(DashBoardPageId.EntSelSuggestionByCompositeCategory,
                                    entityType) : MessageFormat.format(DashBoardPageId.EntSelSuggestionByEntitiesCategory, entityType);
                List<WebElement> suggestions = driver.getWebDriver().findElements(By.xpath(xpath));
                String trimmed = entityName.replaceAll(" +", "");
                logger.log(Level.INFO, "Select value in typeahead: {0}", trimmed);
                
                if (!suggestions.isEmpty()) {
                    logger.log(Level.INFO, "Check first for match in suggestions list.");
                    for (WebElement suggestion : suggestions) {
                        String value = suggestion.getText().replaceAll(" +", "").split("\n")[0];
                        if (value.contains(trimmed)) {
                            logger.log(Level.INFO, "Match found at index: {0}", suggestions.indexOf(suggestion));
                            return suggestion;
                        }                
                    }
                }
                logger.log(Level.INFO, "No results for: {0}", trimmed);
                return null;
        }

}
