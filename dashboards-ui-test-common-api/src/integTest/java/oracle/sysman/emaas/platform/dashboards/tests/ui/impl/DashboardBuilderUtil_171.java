/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DelayedPressEnterThread;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class DashboardBuilderUtil_171 extends DashboardBuilderUtil_Version implements IDashboardBuilderUtil
{
	private static final Logger LOGGER = LogManager.getLogger(DashboardBuilderUtil_171.class);
	private final static String DASHBOARD_SELECTION_TAB_NAME = "Dashboard";

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#addNewDashboardToSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void addNewDashboardToSet(WebDriver driver, String dashboardName)
	{
		driver.getLogger().info("addNewDashboardToSet started for name=\"" + dashboardName + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);

		WebElement dashboardSetContainer = driver.getWebDriver().findElement(
				By.cssSelector(DashBoardPageId.DASHBOARDSETNAVSCONTAINERCSS));
		if (dashboardSetContainer == null) {
			throw new NoSuchElementException("removeDashboardInSet: the dashboard navigator container is not found");
		}

		//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		//		wait.until(ExpectedConditions.visibilityOf(dashboardSetContainer));
		driver.waitForElementVisible("css=" + DashBoardPageId.DASHBOARDSETNAVSCONTAINERCSS);
		driver.takeScreenShot();
		driver.savePageToFile();

		boolean isSelectionTabExist = false;
		List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId.DASHBOARDSETNAVSCSS));
		if (navs == null || navs.isEmpty()) {
			throw new NoSuchElementException("addNewDashboardToSet: the dashboard navigators is not found");
		}

		for (WebElement nav : navs) {
			if (nav.getAttribute("data-tabs-name").trim().equals(DASHBOARD_SELECTION_TAB_NAME)) {
				isSelectionTabExist = true;
				nav.click();
				WaitUtil.waitForPageFullyLoaded(driver);
				driver.takeScreenShot();
				driver.savePageToFile();
				driver.getLogger().info("addNewDashboardToSet has click on the dashboard selection tab");
				break;
			}
		}

		if (isSelectionTabExist == false) {
			WebElement addNav = driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.DASHBOARDSETNAVADDBTNCSS));
			if (addNav == null) {
				throw new NoSuchElementException("removeDashboardInSet: the dashboard 'add' button  is not found");
			}
			//			addNav.click();
			driver.click("css=" + DashBoardPageId.DASHBOARDSETNAVADDBTNCSS);
		}

		driver.takeScreenShot();
		driver.savePageToFile();
		try {
			DashboardHomeUtil.selectDashboard(driver, dashboardName);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.info("context", e);
			e.printStackTrace();
		}
		driver.getLogger().info("removeDashboardInSet has selected the dashboard named with \"" + dashboardName + "\"");

		driver.takeScreenShot();
		driver.savePageToFile();
		driver.getLogger().info("addNewDashboardToSet completed and returns true");
	}

	@Override
	public void addTextWidgetToDashboard(WebDriver driver)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}
	
	@Override
	public void addImageInTextWidget(WebDriver driver, int index, String url, String alternativeText)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}
	@Override
	public void addLinkInTextWidget(WebDriver driver, int index, String url, String option)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}
	
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#addWidgetToDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void addWidgetToDashboard(WebDriver driver, String searchString)
	{
		Validator.notNull("widgetName", searchString);
		Validator.notEmptyString("widgetName", searchString);

		if (searchString == null) {
			return;
		}

		//		By locatorOfKeyEl = By.cssSelector(DashBoardPageId.RIGHTDRAWERCSS);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		//		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfKeyEl));
		driver.waitForElementVisible("css=" + DashBoardPageId.RIGHTDRAWERCSS);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.getLogger().info("[DashboardHomeUtil] call addWidgetToDashboard with search string as " + searchString);

		//show right drawer if it is hidden
		showRightDrawer(driver);

		WebElement searchInput = driver.getElement("css=" + DashBoardPageId.RIGHTDRAWERSEARCHINPUTCSS);
		// focus to search input box
		wait.until(ExpectedConditions.elementToBeClickable(searchInput));
		Actions actions = new Actions(driver.getWebDriver());
		actions.moveToElement(searchInput).build().perform();
		searchInput.clear();
		actions.moveToElement(searchInput).build().perform();
		driver.click("css=" + DashBoardPageId.RIGHTDRAWERSEARCHINPUTCSS);
		searchInput.sendKeys(searchString);
		driver.takeScreenShot();
		driver.savePageToFile();
		//verify input box value
		Assert.assertEquals(searchInput.getAttribute("value"), searchString);

		//		WebElement searchButton = driver.getElement("css=" + DashBoardPageId.RIGHTDRAWERSEARCHBUTTONCSS);
		//		driver.waitForElementPresent("css=" + DashBoardPageId.RIGHTDRAWERSEARCHBUTTONCSS);
		//		searchButton.click();
		//		//wait for ajax resolved
		//		WaitUtil.waitForPageFullyLoaded(driver);
		//		driver.takeScreenShot();
		driver.click("css=" + DashBoardPageId.RIGHTDRAWERSEARCHBUTTONCSS);
		driver.getLogger().info("[DashboardHomeUtil] start to add widget from right drawer");
		List<WebElement> matchingWidgets = driver.getWebDriver().findElements(
				By.cssSelector(DashBoardPageId.RIGHTDRAWERWIDGETCSS));
		if (matchingWidgets == null || matchingWidgets.isEmpty()) {
			throw new NoSuchElementException("Right drawer widget for search string =" + searchString + " is not found");
		}

		//drag and drop not working
		//      WebElement tilesContainer = driver.getElement("css=" + DashBoardPageId.RightDrawerWidgetToAreaCSS);
		//      CommonActions.dragAndDropElement(driver, matchingWidgets.get(0), tilesContainer);

		// focus to  the first matching  widget
		driver.getLogger().info("Focus on the searched widget");
		driver.getWebDriver().switchTo().activeElement().sendKeys(Keys.TAB);
		driver.takeScreenShot();
		driver.savePageToFile();
		// check if the searched widget has the focus
		driver.getLogger().info("Check if the searched widget get the focus");

		if (driver.getWebDriver().switchTo().activeElement()
				.equals(driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.RIGHTDRAWERWIDGETCSS)))) {
			driver.getLogger().info("Press Enter button...");
			driver.getWebDriver().switchTo().activeElement().sendKeys(Keys.ENTER);
			driver.takeScreenShot();
			driver.savePageToFile();
		}
		else {
			driver.getLogger().info("Widget didn't get the focus, need to focus on it");
			driver.getWebDriver().switchTo().activeElement().sendKeys(Keys.TAB);
			driver.takeScreenShot();
			driver.savePageToFile();
			driver.getLogger().info("Press Enter button...");
			driver.getWebDriver().switchTo().activeElement().sendKeys(Keys.ENTER);
			driver.takeScreenShot();
			driver.savePageToFile();

		}

		driver.getLogger().info("[DashboardHomeUtil] finish adding widget from right drawer");

		hideRightDrawer(driver);// hide drawer;
	}

	@Override
	public void createDashboardInsideSet(WebDriver driver, String name, String descriptions)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#deleteDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void deleteDashboard(WebDriver driver)
	{
		driver.getLogger().info("deleteDashboard started");
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		//	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.BUILDEROPTIONSMENULOCATOR)));
		driver.waitForElementVisible("xpath=" + DashBoardPageId.BUILDEROPTIONSMENULOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.click(DashBoardPageId.BUILDEROPTIONSMENULOCATOR);
		driver.click(DashBoardPageId.BUILDEROPTIONSDELETELOCATOR);
		driver.click(DashBoardPageId.BUILDERDELETEDIALOGDELETEBTNLOCATOR);
		driver.waitForElementPresent(DashBoardPageId.SEARCHDASHBOARDINPUTLOCATOR);

		driver.getLogger().info("deleteDashboard completed");
	}

	@Override
	public void deleteDashboardInsideSet(WebDriver driver)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#deleteDashboardSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void deleteDashboardSet(WebDriver driver)
	{
		driver.getLogger().info("deleteDashboardSet started");

		//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.DASHBOARDSETOPTIONBTN)));
		driver.waitForElementVisible("xpath=" + DashBoardPageId.DASHBOARDSETOPTIONBTN);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.click(DashBoardPageId.DASHBOARDSETOPTIONBTN);

		driver.click(DashBoardPageId.DASHBOARDSETOPTIONSDELETELOCATOR);

		driver.click(DashBoardPageId.DASHBOARDSETDELETEDIALOGDELETEBTNLOCATOR);

		driver.waitForElementPresent(DashBoardPageId.SEARCHDASHBOARDINPUTLOCATOR);

		driver.getLogger().info("deleteDashboardSet completed");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#duplicateDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public void duplicateDashboard(WebDriver driver, String name, String descriptions)
	{
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		Validator.notNull("duplicatename", name);
		Validator.notEmptyString("duplicatename", name);
		Validator.notEmptyString("duplicatedescription", descriptions);

		Validator.notEmptyString("duplicatename", name);

		driver.getLogger().info("duplicate started");
		driver.click(DashBoardPageId.BUILDEROPTIONSMENULOCATOR);
		driver.click("css=" + DashBoardPageId.BUILDEROPTIONSDUPLICATELOCATORCSS);
		driver.waitForElementPresent("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATENAMECSS);
		//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ojDialogWrapper-duplicateDsbDialog")));
		driver.waitForElementVisible("ojDialogWrapper-duplicateDsbDialog");
		//add name and description
		driver.getElement("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATENAMECSS).clear();
		driver.click("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATENAMECSS);
		By locatorOfDuplicateNameEl = By.id(DashBoardPageId.BUILDEROPTIONSDUPLICATENAMECSS);

		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfDuplicateNameEl));
		driver.sendKeys("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATENAMECSS, name);
		driver.getElement("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATEDESCRIPTIONCSS).clear();
		driver.click("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATEDESCRIPTIONCSS);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfDuplicateNameEl));
		if (descriptions == null) {
			driver.sendKeys("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATEDESCRIPTIONCSS, "");
		}
		else {
			driver.sendKeys("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATEDESCRIPTIONCSS, descriptions);
		}
		driver.takeScreenShot();
		driver.savePageToFile();

		//press ok button
		By locatorOfDuplicateSaveEl = By.cssSelector(DashBoardPageId.BUILDEROPTIONSDUPLICATESAVECSS);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfDuplicateSaveEl));
		wait.until(ExpectedConditions.elementToBeClickable(locatorOfDuplicateSaveEl));

		By locatorOfDuplicateDesEl = By.id(DashBoardPageId.BUILDEROPTIONSDUPLICATEDESCRIPTIONCSS);
		driver.getWebDriver().findElement(locatorOfDuplicateDesEl).sendKeys(Keys.TAB);

		WebElement saveButton = driver.getElement("css=" + DashBoardPageId.BUILDEROPTIONSDUPLICATESAVECSS);
		Actions actions = new Actions(driver.getWebDriver());
		actions.moveToElement(saveButton).build().perform();

		driver.takeScreenShot();
		driver.savePageToFile();
		driver.getLogger().info("duplicate save button has been focused");

		driver.click("css=" + DashBoardPageId.BUILDEROPTIONSDUPLICATESAVECSS);

		//wait for redirect
		String newTitleLocator = ".dbd-display-hover-area h1[title='" + name + "']";
		driver.getLogger().info("DashboardBuilderUtil.duplicate : wait for redirect" + newTitleLocator);
		//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(newTitleLocator)));
		driver.waitForElementPresent("css=" + newTitleLocator);
		driver.takeScreenShot();
		driver.savePageToFile();
		driver.getLogger().info("duplicate completed");

	}

	@Override
	public void duplicateDashboardInsideSet(WebDriver driver, String name, String descriptions, boolean addToSet)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public void editDashboard(WebDriver driver, String name, String descriptions)
	{
		Validator.notNull("editname", name);
		Validator.notEmptyString("editname", name);

		driver.getLogger().info("edit started");

		driver.click("css=" + DashBoardPageId.BUILDEROPTIONSMENULOCATOR);

		driver.click("css=" + DashBoardPageId.BUILDEROPTIONSEDITLOCATORCSS);

		//wait for 900s
		//		By locatorOfEditDesEl = By.id(DashBoardPageId.BUILDEROPTIONSEDITDESCRIPTIONCSS);

		//add name and description
		driver.getElement("id=" + DashBoardPageId.BUILDEROPTIONSEDITNAMECSS).clear();
		driver.click("id=" + DashBoardPageId.BUILDEROPTIONSEDITNAMECSS);
		//		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
		driver.waitForElementVisible(DashBoardPageId.BUILDEROPTIONSEDITDESCRIPTIONCSS);

		driver.sendKeys("id=" + DashBoardPageId.BUILDEROPTIONSEDITNAMECSS, name);

		driver.getElement("id=" + DashBoardPageId.BUILDEROPTIONSEDITDESCRIPTIONCSS).clear();
		driver.click("id=" + DashBoardPageId.BUILDEROPTIONSEDITDESCRIPTIONCSS);

		driver.waitForElementVisible(DashBoardPageId.BUILDEROPTIONSEDITDESCRIPTIONCSS);
		driver.sendKeys("id=" + DashBoardPageId.BUILDEROPTIONSEDITDESCRIPTIONCSS, descriptions);
		driver.waitForElementVisible(DashBoardPageId.BUILDEROPTIONSEDITDESCRIPTIONCSS);
		driver.takeScreenShot();
		driver.savePageToFile();

		//press ok button
		driver.waitForElementPresent("css=" + DashBoardPageId.BUILDEROPTIONSEDITSAVECSS);
		driver.click("css=" + DashBoardPageId.BUILDEROPTIONSEDITSAVECSS);
		driver.getLogger().info("edit complete");
	}

	@Override
	public void editDashboard(WebDriver driver, String name, String descriptions, Boolean toShowDscptn)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editDashboardSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public void editDashboardSet(WebDriver driver, String name, String descriptions)
	{
		Validator.notNull("editname", name);
		Validator.notEmptyString("editname", name);

		//open the edit dialog
		driver.getLogger().info("editDashboardSet started");
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.click("id=" + DashBoardPageId.DASHBOARDSETOPTIONSMENUID);

		driver.click("css=" + DashBoardPageId.DASHBOARDSETOPTIONSEDITCSS);

		driver.waitForElementPresent("id=" + DashBoardPageId.DASHBOARDSETOPTIONSEDITDIALOGID);

		//edit name and description
		boolean editNameDescriptionElem = driver.isDisplayed("css=" + DashBoardPageId.DASHBOARDSETOPTIONSNAMECOLLAPSIBLECSS);
		if (!editNameDescriptionElem) {
			driver.click("id=" + DashBoardPageId.DASHBOARDSETOPTIONSNAMECOLLAPSIBLECSS);
		}
		//wait obj
		//		By locatorOfEditDesEl = By.cssSelector(DashBoardPageId.DASHBOARDSETOPTIONSEDITNAMECSS);
		//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		//		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));

		driver.waitForElementVisible("css=" + DashBoardPageId.DASHBOARDSETOPTIONSEDITNAMECSS);
		//edit name
		driver.getLogger().info("editDashboardSet start editing name");
		driver.getElement("css=" + DashBoardPageId.DASHBOARDSETOPTIONSEDITNAMECSS).clear();
		driver.click("css=" + DashBoardPageId.DASHBOARDSETOPTIONSEDITNAMECSS);
		driver.sendKeys("css=" + DashBoardPageId.DASHBOARDSETOPTIONSEDITNAMECSS, name);

		//edit description
		driver.getLogger().info("editDashboardSet start editing description");
		driver.getElement("css=" + DashBoardPageId.DASHBOARDSETOPTIONSEDITDESCRIPTIONCSS).clear();
		driver.click("css=" + DashBoardPageId.DASHBOARDSETOPTIONSEDITDESCRIPTIONCSS);
		driver.sendKeys("css=" + DashBoardPageId.DASHBOARDSETOPTIONSEDITDESCRIPTIONCSS, descriptions);
		driver.takeScreenShot();
		driver.savePageToFile();
		//press save button
		driver.waitForElementPresent("id=" + DashBoardPageId.DASHBOARDSETOPTIONSEDITSAVEID);
		driver.click("id=" + DashBoardPageId.DASHBOARDSETOPTIONSEDITSAVEID);
		driver.getLogger().info("editDashboardSet complete");
	}
	
	@Override
	public void editTextWidgetAddContent(WebDriver driver, int index, String content)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#favoriteOption(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public Boolean favoriteOption(WebDriver driver)
	{
		driver.getLogger().info("favoriteOption started");

		driver.click(DashBoardPageId.BUILDEROPTIONSMENULOCATOR);

		boolean favoriteElem = driver.isDisplayed("css=" + DashBoardPageId.BUILDEROPTIONSFAVORITELOCATORCSS);
		if (favoriteElem) {
			driver.click("css=" + DashBoardPageId.BUILDEROPTIONSFAVORITELOCATORCSS);
			driver.getLogger().info("DashboardBuilderUtil add favorite completed");
			return true;
		}
		else {
			driver.click("css=" + DashBoardPageId.BUILDEROPTIONSREMOVEFAVORITELOCATORCSS);
			driver.getLogger().info("DashboardBuilderUtil remove favorite completed");
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#favoriteOptionDashboardSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public Boolean favoriteOptionDashboardSet(WebDriver driver)
	{
		driver.getLogger().info("favoriteOptionDashboardSet started");
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.click("id=" + DashBoardPageId.DASHBOARDSETOPTIONSMENUID);

		boolean dashboardsetFavoriteElem = driver.isDisplayed("css=" + DashBoardPageId.DASHBOARDSETOPTIONSREMOVEFAVORITECSS);
		driver.waitForElementPresent("css=" + DashBoardPageId.DASHBOARDSETOPTIONSFAVORITECSS);
		driver.click("css=" + DashBoardPageId.DASHBOARDSETOPTIONSFAVORITECSS);
		if (dashboardsetFavoriteElem) {
			driver.getLogger().info("DashboardBuilderUtil remove favorite dashboardset completed");
			return false;
		}
		else {
			driver.getLogger().info("DashboardBuilderUtil add favorite dashboardset completed");
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#gridView(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void gridView(WebDriver driver)
	{
		try {
			DashboardHomeUtil.gridView(driver);
		}
		catch (Exception e) {
			LOGGER.info("context", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#isRefreshSettingChecked(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isRefreshSettingChecked(WebDriver driver, String refreshSettings)
	{
		driver.getLogger().info("isRefreshSettingChecked started for refreshSettings=" + refreshSettings);

		Validator.fromValidValues("refreshSettings", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
				REFRESH_DASHBOARD_SETTINGS_5MIN);

		driver.waitForElementPresent(DashBoardPageId.BUILDEROPTIONSMENULOCATOR);
		//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.BUILDEROPTIONSMENULOCATOR)));
		driver.waitForElementVisible("xpath=" + DashBoardPageId.BUILDEROPTIONSMENULOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.click(DashBoardPageId.BUILDEROPTIONSMENULOCATOR);

		driver.click(DashBoardPageId.BUILDEROPTIONSAUTOREFRESHLOCATOR);

		driver.waitForElementPresent(DashBoardPageId.BUILDEROPTIONSAUTOREFRESHOFFLOCATOR);
		if (REFRESH_DASHBOARD_SETTINGS_OFF.equals(refreshSettings)) {
			boolean checked = driver.isDisplayed(DashBoardPageId.BUILDERAUTOREFRESHOFFSELECTEDLOCATOR);
			driver.getLogger().info("isRefreshSettingChecked completed, return result is " + checked);
			return checked;
		}
		else {//REFRESH_DASHBOARD_PARAM_5MIN:
			boolean checked = driver.isDisplayed(DashBoardPageId.BUILDERAUTOREFRESHON5MINSELECTEDLOCATOR);
			driver.getLogger().info("isRefreshSettingChecked completed, return result is " + checked);
			return checked;
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#isRefreshSettingCheckedForDashbaordSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isRefreshSettingCheckedForDashbaordSet(WebDriver driver, String refreshSettings)
	{
		driver.getLogger().info("isRefreshSettingCheckedForDashbaordSet started for refreshSettings=" + refreshSettings);

		Validator.fromValidValues("refreshDashboardSet", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
				REFRESH_DASHBOARD_SETTINGS_5MIN);

		driver.waitForElementPresent(DashBoardPageId.DASHBOARDSETOPTIONBTN);
		//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.DASHBOARDSETOPTIONBTN)));
		driver.waitForElementVisible("xpath=" + DashBoardPageId.DASHBOARDSETOPTIONBTN);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.click(DashBoardPageId.DASHBOARDSETOPTIONBTN);

		driver.click(DashBoardPageId.DASHBOARDSETOPTIONSAUTOREFRESHLOCATOR);

		driver.waitForElementPresent(DashBoardPageId.DASHBOARDSETOPTIONSAUTOREFRESHOFFLOCATOR);
		driver.takeScreenShot();
		driver.savePageToFile();
		if (REFRESH_DASHBOARD_SETTINGS_OFF.equals(refreshSettings)) {
			boolean checked = driver.isDisplayed(DashBoardPageId.DASHBOARDSETAUTOREFRESHOFFSELECTEDLOCATOR);
			driver.getLogger().info("isRefreshSettingCheckedForDashbaordSet completed, return result is " + checked);
			return checked;
		}
		else {// REFRESH_DASHBOARD_SETTINGS_5MIN:
			boolean checked = driver.isDisplayed(DashBoardPageId.DASHBOARDSETAUTOREFRESHON5MINSELECTEDLOCATOR);
			driver.getLogger().info("isRefreshSettingCheckedForDashbaordSet completed, return result is " + checked);
			return checked;
		}
	}

	//	public void loadWebDriverOnly(WebDriver webDriver)
	//	{
	//		driver = webDriver;
	//	}
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#listView(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void listView(WebDriver driver)
	{
		try {
			DashboardHomeUtil.listView(driver);
		}
		catch (Exception e) {
			LOGGER.info("context", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#maximizeWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, int)
	 */
	@Override
	public void maximizeWidget(WebDriver driver, String widgetName, int index)
	{
		driver.getLogger().info("Method not available in 1.7.1 version");

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#moveWidget(WebDriver driver, String widgetName, int index, String moveOption)
	 */
	@Override
	public void moveWidget(WebDriver driver, String widgetName, int index, String moveOption)
	{
		Assert.assertTrue(false, "This method is not available in 1.7.1 version");
		driver.getLogger().info("Method not available in 1.7.1 version");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#moveWidget(WebDriver driver, String widgetName, String moveOption)
	 */
	@Override
	public void moveWidget(WebDriver driver, String widgetName, String moveOption)
	{
		moveWidget(driver, widgetName, 0, moveOption);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#openWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void openWidget(WebDriver driver, String widgetName)
	{
		openWidget(driver, widgetName, 0);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#openWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, int)
	 */
	@Override
	public void openWidget(WebDriver driver, String widgetName, int index)
	{
		driver.getLogger().info("openWidget started for widgetName=" + widgetName + ", index=" + index);

		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);
		clickTileOpenInDataExplorerButton(driver, widgetName, index);

		driver.getLogger().info("openWidget completed");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#printDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void printDashboard(WebDriver driver)
	{
		driver.getLogger().info("DashboardBuilderUtil print dashboard started");
		driver.click(DashBoardPageId.BUILDEROPTIONSMENULOCATOR);
		DelayedPressEnterThread thr = new DelayedPressEnterThread("DelayedPressEnterThread", 5000);
		driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.BUILDEROPTIONSPRINTLOCATORCSS)).click();
		driver.takeScreenShot();
		driver.savePageToFile();
		driver.getLogger().info("DashboardBuilderUtil print completed");
	}

	@Override
	public void printDashboardSet(WebDriver driver)
	{
		driver.getLogger().info("DashboardBuilderUtil print dashboard set started");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("id=" + DashBoardPageId.DASHBOARDSETOPTIONSMENUID);
		int waitTime = 5000;

		//click all tabs
		WebElement dashboardSetContainer = driver.getWebDriver().findElement(
				By.cssSelector(DashBoardPageId.DASHBOARDSETNAVSCONTAINERCSS));
		if (dashboardSetContainer == null) {
			throw new NoSuchElementException("removeDashboardInSet: the dashboard navigator container is not found");
		}

		List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId.DASHBOARDSETNAVSCSS));
		if (navs == null || navs.isEmpty()) {
			throw new NoSuchElementException("addNewDashboardToSet: the dashboard navigators is not found");
		}
		for (WebElement nav : navs) {
			nav.click();
			WaitUtil.waitForPageFullyLoaded(driver);
			driver.takeScreenShot();
			driver.savePageToFile();
			driver.getLogger().info("printDashboardSet has click on the dashboard selection tab named");
		}

		//click print
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("id=" + DashBoardPageId.DASHBOARDSETOPTIONSMENUID);
		driver.click("id=" + DashBoardPageId.DASHBOARDSETOPTIONSMENUID);
		driver.waitForElementPresent("css=" + DashBoardPageId.DASHBOARDSETOPTIONSPRINTCSS);
		driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.DASHBOARDSETOPTIONSPRINTCSS)).click();
		//have to use thread sleep to wait for the print window(windows dialog) to appear
		try {
			Thread.sleep(waitTime);
		}
		catch (InterruptedException e) {
			LOGGER.info("context", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.getLogger().info("DashboardBuilderUtil print set completed");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#refreshDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void refreshDashboard(WebDriver driver, String refreshSettings)
	{
		driver.getLogger().info("refreshDashboard started for refreshSettings=" + refreshSettings);

		Validator.fromValidValues("refreshSettings", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
				REFRESH_DASHBOARD_SETTINGS_5MIN);

		driver.waitForElementPresent(DashBoardPageId.BUILDEROPTIONSMENULOCATOR);
		//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.BUILDEROPTIONSMENULOCATOR)));
		driver.waitForElementPresent("xpath=" + DashBoardPageId.BUILDEROPTIONSMENULOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.click(DashBoardPageId.BUILDEROPTIONSMENULOCATOR);

		driver.click(DashBoardPageId.BUILDEROPTIONSAUTOREFRESHLOCATOR);

		driver.waitForElementPresent(DashBoardPageId.BUILDEROPTIONSAUTOREFRESHOFFLOCATOR);
		switch (refreshSettings) {
			case REFRESH_DASHBOARD_SETTINGS_OFF:
				driver.check(DashBoardPageId.BUILDEROPTIONSAUTOREFRESHOFFLOCATOR);
				driver.waitForElementPresent(DashBoardPageId.BUILDERAUTOREFRESHOFFSELECTEDLOCATOR);
				driver.takeScreenShot();
				driver.savePageToFile();
				break;
			case REFRESH_DASHBOARD_SETTINGS_5MIN:
				driver.check(DashBoardPageId.BUILDEROPTIONSAUTOREFRESHON5MINLOCATOR);
				driver.waitForElementPresent(DashBoardPageId.BUILDERAUTOREFRESHON5MINSELECTEDLOCATOR);
				driver.takeScreenShot();
				driver.savePageToFile();
				break;
			default:
				break;
		}
		driver.getLogger().info("refreshDashboard completed");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#refreshDashboardSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void refreshDashboardSet(WebDriver driver, String refreshSettings)
	{
		driver.getLogger().info("refreshDashboardSet started for refreshSettings=" + refreshSettings);

		Validator.fromValidValues("refreshDashboardSet", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
				REFRESH_DASHBOARD_SETTINGS_5MIN);

		driver.waitForElementPresent(DashBoardPageId.DASHBOARDSETOPTIONBTN);
		//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		//		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(DashBoardPageId.DASHBOARDSETOPTIONBTN)));

		WaitUtil.waitForPageFullyLoaded(driver);

		driver.click(DashBoardPageId.DASHBOARDSETOPTIONBTN);
		driver.click(DashBoardPageId.DASHBOARDSETOPTIONSAUTOREFRESHLOCATOR);

		driver.waitForElementPresent(DashBoardPageId.DASHBOARDSETOPTIONSAUTOREFRESHOFFLOCATOR);
		switch (refreshSettings) {
			case REFRESH_DASHBOARD_SETTINGS_OFF:
				driver.check(DashBoardPageId.DASHBOARDSETOPTIONSAUTOREFRESHOFFLOCATOR);
				break;
			case REFRESH_DASHBOARD_SETTINGS_5MIN:
				driver.check(DashBoardPageId.DASHBOARDSETOPTIONSAUTOREFRESHON5MINLOCATOR);
				break;
			default:
				break;
		}
		driver.getLogger().info("refreshDashboardSet completed");
	}

	@Override
	public void removeDashboardFromSet(WebDriver driver, String dashboardName)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#removeWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void removeWidget(WebDriver driver, String widgetName)
	{
		removeWidget(driver, widgetName, 0);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#removeWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, int)
	 */
	@Override
	public void removeWidget(WebDriver driver, String widgetName, int index)
	{
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);

		WebElement widgetEl = null;
		try {
			widgetEl = getWidgetByName(driver, widgetName, index);
		}
		catch (InterruptedException e) {
			LOGGER.info("context", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		focusOnWidgetHeader(driver, widgetEl);
		driver.takeScreenShot();
		driver.savePageToFile();
		widgetEl.findElement(By.cssSelector(DashBoardPageId.CONFIGTILECSS)).click();
		driver.click("css=" + DashBoardPageId.REMOVETILECSS);
		driver.getLogger().info("Remove the widget");

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#resizeWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, int, java.lang.String)
	 */
	@Override
	public void resizeWidget(WebDriver driver, String widgetName, int index, String resizeOptions)
	{
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);
		Validator.fromValidValues("resizeOptions", resizeOptions, TILE_NARROWER, TILE_WIDER, TILE_SHORTER, TILE_TALLER);

		WebElement widgetEl = null;
		try {
			widgetEl = getWidgetByName(driver, widgetName, index);
		}
		catch (InterruptedException e) {
			LOGGER.info("context", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		focusOnWidgetHeader(driver, widgetEl);
		driver.takeScreenShot();
		driver.savePageToFile();
		String tileResizeCSS = null;
		switch (resizeOptions) {
			case TILE_WIDER:
				tileResizeCSS = DashBoardPageId.WIDERTILECSS;
				break;
			case TILE_NARROWER:
				tileResizeCSS = DashBoardPageId.NARROWERTILECSS;
				break;
			case TILE_SHORTER:
				tileResizeCSS = DashBoardPageId.SHORTERTILECSS;
				break;
			case TILE_TALLER:
				tileResizeCSS = DashBoardPageId.TALLERTILECSS;
				break;
			default:
				break;
		}
		if (null == tileResizeCSS) {
			return;
		}

		widgetEl.findElement(By.cssSelector(DashBoardPageId.CONFIGTILECSS)).click();
		driver.click("css=" + tileResizeCSS);
		driver.getLogger().info("Resize the widget");
		

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#resizeWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public void resizeWidget(WebDriver driver, String widgetName, String resizeOptions)
	{
		resizeWidget(driver, widgetName, 0, resizeOptions);
	}

	@Override
	public boolean respectGCForEntity(WebDriver driver)
	{
		Assert.assertTrue(false, "This method is not available in 1.7.1 version");
		driver.getLogger().info("Method not available in 1.7.1 version");
		return false;
	}

	@Override
	public boolean respectGCForTimeRange(WebDriver driver)
	{
		Assert.assertTrue(false, "This method is not available in 1.7.1 version");
		driver.getLogger().info("Method not available in 1.7.1 version");
		return false;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#restoreWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, int)
	 */
	@Override
	public void restoreWidget(WebDriver driver, String widgetName, int index)
	{
		driver.getLogger().info("Method not available in 1.7.1 version");

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#saveDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void saveDashboard(WebDriver driver)
	{
		driver.getLogger().info("save started");

		driver.click("css=" + DashBoardPageId.DASHBOARDSAVECSS);

		driver.getLogger().info("save compelted");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#search(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void search(WebDriver driver, String searchString)
	{
		try {
			DashboardHomeUtil.search(driver, searchString);
		}
		catch (Exception e) {
			LOGGER.info("context", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#selectDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void selectDashboard(WebDriver driver, String dashboardName)
	{
		try {
			DashboardHomeUtil.selectDashboard(driver, dashboardName);
		}
		catch (Exception e) {
			LOGGER.info("context", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void selectDashboardInsideSet(WebDriver driver, String dashboardName)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	@Override
	public void setEntitySupport(WebDriver driver, String mode)
	{
		Assert.assertTrue(false, "This method is not available in 1.7.1 version");
		driver.getLogger().info("Method not available in 1.7.1 version");
	}

	@Override
	public boolean showEntityFilter(WebDriver driver, boolean showEntityFilter)
	{
		Assert.assertTrue(false, "This method is not available in 1.7.1 version");
		driver.getLogger().info("Method not available in 1.7.1 version");
		return false;
	}

	@Override
	public boolean showTimeRangeFilter(WebDriver driver, boolean showTimeRangeFilter)
	{
		Assert.assertTrue(false, "This method is not available in 1.7.1 version");
		driver.getLogger().info("Method not available in 1.7.1 version");
		return false;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#showWidgetTitle(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, boolean)
	 */
	@Override
	public void showWidgetTitle(WebDriver driver, String widgetName, boolean visibility)
	{
		showWidgetTitle(driver, widgetName, 0, visibility);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#showWidgetTitle(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, int, boolean)
	 */
	@Override
	public void showWidgetTitle(WebDriver driver, String widgetName, int index, boolean visibility)
	{
		driver.getLogger().info(
				"showWidgetTitle started for widgetName=" + widgetName + ", index=" + index + ", visibility=" + visibility);
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);

		driver.waitForElementPresent(DashBoardPageId.BUILDERTILESEDITAREA);
		//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.BUILDERTILESEDITAREA)));
		driver.waitForElementVisible("xpath=" + DashBoardPageId.BUILDERTILESEDITAREA);
		WaitUtil.waitForPageFullyLoaded(driver);

		clickTileConfigButton(driver, widgetName, index);

		if (visibility) {
			if (driver.isDisplayed(DashBoardPageId.BUILDERTILEHIDELOCATOR)) {
				driver.takeScreenShot();
				driver.savePageToFile();
				driver.getLogger().info("showWidgetTitle completed as title is shown already");
				return;
			}
			driver.click(DashBoardPageId.BUILDERTILESHOWLOCATOR);
		}
		else {
			if (driver.isDisplayed(DashBoardPageId.BUILDERTILESHOWLOCATOR)) {
				driver.takeScreenShot();
				driver.savePageToFile();
				driver.getLogger().info("showWidgetTitle completed as title is hidden already");
				return;
			}
			driver.click(DashBoardPageId.BUILDERTILEHIDELOCATOR);

		}
		driver.getLogger().info("showWidgetTitle completed");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#sortBy(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void sortBy(WebDriver driver, String option)
	{
		try {
			DashboardHomeUtil.sortBy(driver, option);
		}
		catch (Exception e) {
			LOGGER.info("context", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#toggleHome(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public Boolean toggleHome(WebDriver driver)
	{
		driver.getLogger().info("asHomeOption started");
		driver.click(DashBoardPageId.BUILDEROPTIONSMENULOCATOR);
		boolean homeElem = driver.isDisplayed("css=" + DashBoardPageId.BUILDEROPTIONSSETHOMELOCATORCSS);
		driver.takeScreenShot();
		driver.savePageToFile();
		if (homeElem) {
			driver.click("css=" + DashBoardPageId.BUILDEROPTIONSSETHOMELOCATORCSS);
			boolean comfirmDialog = driver.isDisplayed("css=" + DashBoardPageId.BUILDEROPTIONSSETHOMESAVECSS);
			if (comfirmDialog) {
				driver.click("css=" + DashBoardPageId.BUILDEROPTIONSSETHOMESAVECSS);

			}
			driver.getLogger().info("DashboardBuilderUtil set home completed");
			return true;
		}
		else {
			driver.click("css=" + DashBoardPageId.BUILDEROPTIONSREMOVEHOMELOCATORCSS);
			driver.getLogger().info("DashboardBuilderUtil remove home completed");
			return false;
		}

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#toggleHomeDashboardSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public Boolean toggleHomeDashboardSet(WebDriver driver)
	{
		driver.getLogger().info("toggleHomeOptionDashboardSet started");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("id=" + DashBoardPageId.DASHBOARDSETOPTIONSMENUID);
		driver.click("id=" + DashBoardPageId.DASHBOARDSETOPTIONSMENUID);

		boolean homeElem = driver.isDisplayed("css=" + DashBoardPageId.DASHBOARDSETOPTIONSADDHOMECSS);
		driver.takeScreenShot();
		driver.savePageToFile();

		driver.click("css=" + DashBoardPageId.DASHBOARDSETOPTIONSHOMECSS);

		if (homeElem) {
			driver.getLogger().info("DashboardBuilderUtil set home in dashboard set completed");
			return true;
		}
		else {
			driver.getLogger().info("DashboardBuilderUtil remove home in dashboard set completed");
			return false;
		}

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#toggleShareDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public Boolean toggleShareDashboard(WebDriver driver)
	{
		driver.getLogger().info("sharedashboard started");

		driver.click(DashBoardPageId.BUILDEROPTIONSMENULOCATOR);

		boolean shareElem = driver.isDisplayed("css=" + DashBoardPageId.BUILDEROPTIONSSHARELOCATORCSS);
		if (shareElem) {

			driver.click("css=" + DashBoardPageId.BUILDEROPTIONSSHARELOCATORCSS);

			driver.getLogger().info("DashboardBuilderUtil share dashboard");
			return true;
		}
		else {

			driver.click("css=" + DashBoardPageId.BUILDEROPTIONSUNSHARELOCATORCSS);

			driver.getLogger().info("DashboardBuilderUtil unshare dashboard");
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#toggleShareDashboardset(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public Boolean toggleShareDashboardset(WebDriver driver)
	{
		driver.getLogger().info("toggleShareDashboardset started");
		WaitUtil.waitForPageFullyLoaded(driver);

		//open the edit/share dialog
		driver.getLogger().info("toggleShareDashboardset open share/edit dialog");

		driver.click("id=" + DashBoardPageId.DASHBOARDSETOPTIONSMENUID);

		driver.click("css=" + DashBoardPageId.DASHBOARDSETOPTIONSEDITCSS);

		driver.waitForElementPresent("id=" + DashBoardPageId.DASHBOARDSETOPTIONSEDITDIALOGID);

		//open share collapsible
		boolean editShareElem = driver.isDisplayed("css=" + DashBoardPageId.DASHBOARDSETOPTIONSSHAREDIAOPENCSS);

		if (!editShareElem) {

			driver.click("css=" + DashBoardPageId.DASHBOARDSETOPTIONSSHARECOLLAPSIBLECSS);
		}
		driver.getLogger().info("toggleShareDashboardset dialog has opened");

		//toggle share dashboardset
		boolean shareFlagElem = driver.isDisplayed("css=" + DashBoardPageId.DASHBOARDSETOPTIONSSHAREONJUDGECSS);
		if (shareFlagElem) {
			driver.waitForElementPresent("css=" + DashBoardPageId.DASHBOARDSETOPTIONSUNSHARECSS);
			driver.click("css=" + DashBoardPageId.DASHBOARDSETOPTIONSUNSHARECSS);

			driver.getLogger().info("DashboardBuilderUtil unshare dashboardset");

			driver.click("id=" + DashBoardPageId.DASHBOARDSETOPTIONSEDITSAVEID);
			driver.getLogger().info("DashboardBuilderUtil toggleShareDashboardset completed");
			return false;
		}
		else {

			driver.click("css=" + DashBoardPageId.DASHBOARDSETOPTIONSSHARECSS);

			driver.getLogger().info("DashboardBuilderUtil share dashboardset");

			driver.click("id=" + DashBoardPageId.DASHBOARDSETOPTIONSEDITSAVEID);
			driver.getLogger().info("DashboardBuilderUtil toggleShareDashboardset completed");
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#verifyDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public boolean verifyDashboard(WebDriver driver, String dashboardName, String description, boolean showTimeSelector)
	{
		driver.getLogger().info(
				"verifyDashboard started for name=\"" + dashboardName + "\", description=\"" + description
						+ "\", showTimeSelector=\"" + showTimeSelector + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);

		driver.waitForElementPresent(DashBoardPageId.BUILDERNAMETEXTLOCATOR);
		//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.BUILDERNAMETEXTLOCATOR)));
		driver.waitForElementVisible("xpath=" + DashBoardPageId.BUILDERNAMETEXTLOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.click(DashBoardPageId.BUILDERNAMETEXTLOCATOR);

		String realName = driver.getElement(DashBoardPageId.BUILDERNAMETEXTLOCATOR).getAttribute("title");
		if (!dashboardName.equals(realName)) {
			driver.getLogger().info(
					"verifyDashboard compelted and returns false. Expected dashboard name is " + dashboardName
							+ ", actual dashboard name is " + realName);
			return false;
		}

		driver.waitForElementPresent(DashBoardPageId.BUILDERDESCRIPTIONTEXTLOCATOR);
		String realDesc = driver.getElement(DashBoardPageId.BUILDERDESCRIPTIONTEXTLOCATOR).getAttribute("title");
		if (description == null || "".equals(description)) {
			if (realDesc != null && !"".equals(realDesc.trim())) {
				driver.getLogger().info(
						"verifyDashboard compelted and returns false. Expected description is " + description
								+ ", actual dashboard description is " + realDesc);
				return false;
			}
		}
		else {
			if (!description.equals(realDesc)) {
				driver.getLogger().info(
						"verifyDashboard compelted and returns false. Expected description is " + description
								+ ", actual dashboard description is " + realDesc);
				return false;
			}
		}

		boolean actualTimeSelectorShown = driver.isDisplayed(DashBoardPageId.BUILDERDATETIMEPICKERLOCATOR);
		if (actualTimeSelectorShown != showTimeSelector) {
			driver.getLogger().info(
					"verifyDashboard compelted and returns false. Expected showTimeSelector is " + showTimeSelector
							+ ", actual dashboard showTimeSelector is " + actualTimeSelectorShown);
			return false;
		}

		driver.getLogger().info("verifyDashboard compelted and returns true");
		return true;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#verifyDashboardInsideSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean verifyDashboardInsideSet(WebDriver driver, String dashboardName)
	{
		driver.getLogger().info("verifyDashboardInsideSet started for name=\"" + dashboardName + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);

		WebElement dashboardSetContainer = driver.getWebDriver().findElement(
				By.cssSelector(DashBoardPageId.DASHBOARDSETNAVSCONTAINERCSS));
		if (dashboardSetContainer == null) {
			throw new NoSuchElementException("verifyDashboardInsideSet: the dashboard navigator container is not found");
		}

		//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		//		wait.until(ExpectedConditions.visibilityOf(dashboardSetContainer));
		driver.waitForElementVisible("css=" + DashBoardPageId.DASHBOARDSETNAVSCONTAINERCSS);
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.takeScreenShot();
		driver.savePageToFile();
		boolean hasFound = false;
		List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId.DASHBOARDSETNAVSCSS));
		if (navs == null || navs.isEmpty()) {
			throw new NoSuchElementException("verifyDashboardInsideSet: the dashboard navigators is not found");
		}

		for (WebElement nav : navs) {
			if (nav.getAttribute("data-dashboard-name-in-set") != null
					&& nav.getAttribute("data-dashboard-name-in-set").trim().equals(dashboardName)) {
				hasFound = true;
				break;
			}
		}

		if (hasFound) {
			driver.getLogger().info("verifyDashboardInsideSet name=\"" + dashboardName + "\" has found");
		}
		else {
			driver.getLogger().info("verifyDashboardInsideSet name=\"" + dashboardName + "\" has not found");
		}
		driver.getLogger().info("removeDashboardInSet completed");
		return hasFound;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#verifyDashboardSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean verifyDashboardSet(WebDriver driver, String dashboardSetName)
	{
		driver.getLogger().info("verifyDashboard started for name=\"" + dashboardSetName + "\"");
		Validator.notEmptyString("dashboardSetName", dashboardSetName);

		driver.waitForElementPresent(DashBoardPageId.DASHBOARDSETNAMETEXTLOCATOR);
		//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.DASHBOARDSETNAMETEXTLOCATOR)));
		driver.waitForElementVisible("xpath=" + DashBoardPageId.DASHBOARDSETNAMETEXTLOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.click(DashBoardPageId.DASHBOARDSETNAMETEXTLOCATOR);

		String realName = driver.getElement(DashBoardPageId.DASHBOARDSETNAMETEXTLOCATOR).getText();
		if (!dashboardSetName.equals(realName)) {
			driver.getLogger().info(
					"verifyDashboardSet compelted and returns false. Expected dashboard set name is " + dashboardSetName
							+ ", actual dashboard set name is " + realName);
			return false;
		}

		driver.getLogger().info("verifyDashboardSet compelted and returns true");
		return true;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#verifyWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean verifyWidget(WebDriver driver, String widgetName)
	{
		return verifyWidget(driver, widgetName, 0);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#verifyWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, int)
	 */
	@Override
	public boolean verifyWidget(WebDriver driver, String widgetName, int index)
	{
		driver.getLogger().info("verifyWidget started for name=\"" + widgetName + "\", index=\"" + index + "\"");
		Validator.notEmptyString("dashboardName", widgetName);

		WebElement we = null;
		try {
			WaitUtil.waitForPageFullyLoaded(driver);
			we = getTileTitleElement(driver, widgetName, index);
		}
		catch (NoSuchElementException e) {
			LOGGER.info("context", e);
			driver.getLogger().info("verifyWidget compelted and returns false");
			return false;
		}
		if (we == null) {
			driver.getLogger().info("verifyWidget compelted and returns false");
			return false;
		}

		driver.getLogger().info("verifyWidget compelted and returns true");
		return true;
	}

	private WebElement clickTileConfigButton(WebDriver driver, String widgetName, int index)
	{
		WebElement tileTitle = getTileTitleElement(driver, widgetName, index);
		WebElement tileConfig = tileTitle.findElement(By.xpath(DashBoardPageId.BUILDERTILECONFIGLOCATOR));
		if (tileConfig == null) {
			throw new NoSuchElementException("Tile config menu for title=" + widgetName + ", index=" + index + " is not found");
		}
		Actions builder = new Actions(driver.getWebDriver());
		builder.moveToElement(tileTitle).perform();
		builder.moveToElement(tileConfig).click().perform();
		return tileConfig;
	}

	private void clickTileOpenInDataExplorerButton(WebDriver driver, String widgetName, int index)
	{
		driver.getLogger().info("Start to find widget with widgetName=" + widgetName + ", index=" + index);
		WebElement widgetTitle = getTileTitleElement(driver, widgetName, index);
		if (widgetTitle == null) {
			throw new NoSuchElementException("Widget with title=" + widgetName + ", index=" + index + " is not found");
		}
		driver.getLogger().info("Found widget with name=" + widgetName + ", index =" + index + " before opening widget link");
		WebElement widgetDataExplore = widgetTitle.findElement(By.xpath(DashBoardPageId.BUILDERTILEDATAEXPLORELOCATOR));
		if (widgetDataExplore == null) {
			throw new NoSuchElementException("Widget data explorer link for title=" + widgetName + ", index=" + index
					+ " is not found");
		}
		driver.getLogger().info("Found widget configure button");
		Actions builder = new Actions(driver.getWebDriver());
		driver.getLogger().info("Now moving to the widget title bar");
		builder.moveToElement(widgetTitle).perform();
		driver.takeScreenShot();
		driver.savePageToFile();
		driver.getLogger().info("and clicks the widget config button");
		//		builder.moveToElement(widgetDataExplore).click().perform();
		widgetDataExplore.click();
		driver.takeScreenShot();
		driver.savePageToFile();
	}

	private void focusOnWidgetHeader(WebDriver driver, WebElement widgetElement)
	{
		if (null == widgetElement) {
			driver.getLogger().info("Fail to find the widget element");
			driver.takeScreenShot();
			driver.savePageToFile();
			throw new NoSuchElementException("Widget config menu is not found");
		}

		WebElement widgetHeader = widgetElement.findElement(By.cssSelector(DashBoardPageId.TILETITLECSS));
		Actions actions = new Actions(driver.getWebDriver());
		actions.moveToElement(widgetHeader).build().perform();
		driver.getLogger().info("Focus to the widget");
	}

	private WebElement getTileTitleElement(WebDriver driver, String widgetName, int index)
	{

		driver.click(DashBoardPageId.BUILDERTILESEDITAREA);

		String titleTitlesLocator = String.format(DashBoardPageId.BUILDERTILETITLELOCATOR, widgetName);
		List<WebElement> tileTitles = driver.getWebDriver().findElements(By.xpath(titleTitlesLocator));
		if (tileTitles == null || tileTitles.size() <= index) {
			throw new NoSuchElementException("Tile with title=" + widgetName + ", index=" + index + " is not found");
		}
		tileTitles.get(index).click();
		driver.takeScreenShot();
		driver.savePageToFile();
		return tileTitles.get(index);
	}

	private WebElement getWidgetByName(WebDriver driver, String widgetName, int index) throws InterruptedException
	{
		if (widgetName == null) {
			return null;
		}

		List<WebElement> widgets = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId.WIDGETTITLECSS));
		WebElement widget = null;
		int counter = 0;
		for (WebElement widgetElement : widgets) {
			WebElement widgetTitle = widgetElement.findElement(By.cssSelector(DashBoardPageId.TILETITLECSS));
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

	private boolean isRightDrawerVisible(WebDriver driver)
	{
		WebElement rightDrawerPanel = driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.RIGHTDRAWERPANELCSS));
		boolean isDisplayed = "none".equals(rightDrawerPanel.getCssValue("display")) != true;
		driver.getLogger().info("isRightDrawerVisible,the isDisplayed value is " + isDisplayed);

		boolean isWidthValid = "0px".equals(rightDrawerPanel.getCssValue("width")) != true;
		driver.getLogger().info("isRightDrawerVisible,the isWidthValid value is " + isWidthValid);

		return isDisplayed && isWidthValid;
	}

	private void showRightDrawer(WebDriver driver)
	{
		driver.waitForElementPresent("css=" + DashBoardPageId.RIGHTDRAWERCSS);
		if (isRightDrawerVisible(driver) == false) {
			driver.click("css=" + DashBoardPageId.RIGHTDRAWERTOGGLEBTNCSS);
			driver.getLogger().info("[DashboardBuilderUtil] triggered showRightDrawer.");

		}

	}

	protected void hideRightDrawer(WebDriver driver)
	{
		driver.waitForElementPresent("css=" + DashBoardPageId.RIGHTDRAWERCSS);
		if (isRightDrawerVisible(driver) == true) {
			driver.click("css=" + DashBoardPageId.RIGHTDRAWERTOGGLEBTNCSS);
			driver.getLogger().info("[DashboardBuilderUtil] triggered hideRightDrawer.");
		}

	}

	@Override
	public void addLinkToWidgetTitle(WebDriver driver, String widgetName, int index, String dashboardName)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	@Override
	public void addLinkToWidgetTitle(WebDriver driver, String widgetName, String dashboardName){
		addLinkToWidgetTitle(driver, widgetName, 0, dashboardName);
	}

	@Override
	public boolean verifyLinkOnWidgetTitle(WebDriver driver, String widgetName, int index, String dashboardName){
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
		return false;
	}

	@Override
	public boolean verifyLinkOnWidgetTitle(WebDriver driver, String widgetName, String dashboardName){
		return verifyLinkOnWidgetTitle(driver, widgetName, 0, dashboardName);
	}

	/**
	 * @param driver
	 * @param widgetName
	 * @return
	 */
	@Override
	public boolean hasWidgetLink(WebDriver driver, String widgetName)
	{		
		return hasWidgetLink(driver, widgetName, 0);
	}

	/**
	 * @param driver
	 * @param widgetName
	 * @param index
	 * @return
	 */
	@Override
	public boolean hasWidgetLink(WebDriver driver, String widgetName, int index)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
		return false;
	}

	/**
	 * @param driver
	 * @param widgetName
	 * @param index
	 */
	@Override
	public void clickLinkOnWidgetTitle(WebDriver driver, String widgetName, int index)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	/**
	 * @param driver
	 * @param widgetName
	 */
	@Override
	public void clickLinkOnWidgetTitle(WebDriver driver, String widgetName)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");		
	}
}
