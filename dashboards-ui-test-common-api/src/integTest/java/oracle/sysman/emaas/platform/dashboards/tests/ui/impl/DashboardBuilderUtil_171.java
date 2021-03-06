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

import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DelayedPressEnterThread;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
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
		
		int dashboardSetContainerCount = driver.getElementCount("css=" + DashBoardPageId.DASHBOARDSETNAVSCONTAINERCSS);
		if (dashboardSetContainerCount <= 0) {
			throw new NoSuchElementException(
					"DashboardBuilderUtil.addNewDashboardToSet: the dashboard navigator container is not found");
		}

		driver.waitForElementVisible("css=" + DashBoardPageId.DASHBOARDSETNAVSCONTAINERCSS);
		driver.takeScreenShot();
		driver.savePageToFile();

		boolean isSelectionTabExist = false;
		int dashboardTabCount = driver.getElementCount("css=" + DashBoardPageId.DASHBOARDSETNAVSCSS);
		if(dashboardTabCount <= 0)
		{
			throw new NoSuchElementException("DashboardBuilderUtil.addNewDashboardToSet: the dashboard navigators is not found");
		}
		
		for(int i=1; i<=dashboardTabCount; i++)
		{
			if(DASHBOARD_SELECTION_TAB_NAME.equals(driver.getAttribute("xpath=(" + DashBoardPageId.DASHBOARDSETNAVSXPATH +")[" + i +"]@data-tabs-name")))
			{
				isSelectionTabExist = true;
				driver.click("xpath=(" + DashBoardPageId.DASHBOARDSETNAVSXPATH +")[" + i +"]");
				WaitUtil.waitForPageFullyLoaded(driver);
				driver.getLogger().info("DashboardBuilderUtil.addNewDashboardToSet has click on the dashboard selection tab");
				break;
			}
		}

		if (isSelectionTabExist == false) {
			driver.click("css=" + DashBoardPageId.DASHBOARDSETNAVADDBTNCSS);
		}

		try {
			DashboardHomeUtil.selectDashboard(driver, dashboardName);
		}
		catch (Exception e) {
			driver.getLogger().info("Exception: " + e.getLocalizedMessage());
		}
		driver.getLogger().info("addNewDashboardToSet has selected the dashboard named with \"" + dashboardName + "\"");

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

		driver.waitForElementVisible("css=" + DashBoardPageId.RIGHTDRAWERCSS);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.getLogger().info("[DashboardHomeUtil] call addWidgetToDashboard with search string as " + searchString);

		//show right drawer if it is hidden
		showRightDrawer(driver);
		
		// focus to search input box		
		driver.waitForElementEnabled("css=" + DashBoardPageId.RIGHTDRAWERSEARCHINPUTCSS);
		driver.moveToElement("css=" + DashBoardPageId.RIGHTDRAWERSEARCHINPUTCSS);
		driver.clear("css=" + DashBoardPageId.RIGHTDRAWERSEARCHINPUTCSS);
		driver.moveToElement("css=" + DashBoardPageId.RIGHTDRAWERSEARCHINPUTCSS);
		driver.click("css=" + DashBoardPageId.RIGHTDRAWERSEARCHINPUTCSS);
		driver.sendKeys("css=" + DashBoardPageId.RIGHTDRAWERSEARCHINPUTCSS, searchString);

		//verify input box value
		Assert.assertEquals(driver.getAttribute("css=" + DashBoardPageId.RIGHTDRAWERSEARCHINPUTCSS + "@value"), searchString);

		driver.click("css=" + DashBoardPageId.RIGHTDRAWERSEARCHBUTTONCSS);
		
		driver.getLogger().info("[DashboardHomeUtil] start to add widget from right drawer");
		
		int matchingWidgetsCount = driver.getElementCount("css=" + DashBoardPageId.RIGHTDRAWERWIDGETCSS);
		if (matchingWidgetsCount <= 0) {
			throw new NoSuchElementException("Right drawer widget for search string =" + searchString + " is not found");
		}

		// focus to  the first matching  widget
		driver.getLogger().info("Focus on the searched widget");
		
		driver.getWebDriver().switchTo().activeElement().sendKeys(Keys.TAB);
		driver.takeScreenShot();
		driver.savePageToFile();
		// check if the searched widget has the focus
		driver.getLogger().info("Check if the searched widget get the focus");

		if (driver.getWebDriver().switchTo().activeElement()
				.equals(driver.getElement("css=" + DashBoardPageId.RIGHTDRAWERWIDGETCSS))) {
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
		Validator.notNull("duplicatename", name);
		Validator.notEmptyString("duplicatename", name);
		Validator.notEmptyString("duplicatedescription", descriptions);

		Validator.notEmptyString("duplicatename", name);

		driver.getLogger().info("duplicate started");
		driver.click(DashBoardPageId.BUILDEROPTIONSMENULOCATOR);
		driver.click("css=" + DashBoardPageId.BUILDEROPTIONSDUPLICATELOCATORCSS);
		driver.waitForElementPresent("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATENAMECSS);

		driver.waitForElementVisible("ojDialogWrapper-duplicateDsbDialog");
		//add name and description
		driver.getElement("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATENAMECSS).clear();
		driver.click("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATENAMECSS);
		
		driver.waitForElementVisible("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATENAMECSS);
		driver.sendKeys("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATENAMECSS, name);
		driver.getElement("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATEDESCRIPTIONCSS).clear();
		driver.click("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATEDESCRIPTIONCSS);
		driver.waitForElementVisible("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATENAMECSS);
		
		if (descriptions == null) {
			driver.sendKeys("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATEDESCRIPTIONCSS, "");
		}
		else {
			driver.sendKeys("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATEDESCRIPTIONCSS, descriptions);
		}

		//press ok button
		driver.waitForElementVisible("css=" + DashBoardPageId.BUILDEROPTIONSDUPLICATESAVECSS);
		driver.waitForElementEnabled("css=" + DashBoardPageId.BUILDEROPTIONSDUPLICATESAVECSS);
				
		driver.sendKeys("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATEDESCRIPTIONCSS, Keys.chord(Keys.TAB));
		

		driver.moveToElement("css=" + DashBoardPageId.BUILDEROPTIONSDUPLICATESAVECSS);
	
		driver.takeScreenShot();
		driver.savePageToFile();
		driver.getLogger().info("duplicate save button has been focused");

		driver.click("css=" + DashBoardPageId.BUILDEROPTIONSDUPLICATESAVECSS);

		//wait for redirect
		String newTitleLocator = ".dbd-display-hover-area h1[title='" + name + "']";
		driver.getLogger().info("DashboardBuilderUtil.duplicate : wait for redirect" + newTitleLocator);
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

		//add name and description
		driver.getElement("id=" + DashBoardPageId.BUILDEROPTIONSEDITNAMECSS).clear();
		driver.click("id=" + DashBoardPageId.BUILDEROPTIONSEDITNAMECSS);
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
		driver.click("css=" + DashBoardPageId.BUILDEROPTIONSPRINTLOCATORCSS);
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
		driver.clickAll("css=" + DashBoardPageId.DASHBOARDSETNAVSCSS);

		//click print
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("id=" + DashBoardPageId.DASHBOARDSETOPTIONSMENUID);
		driver.click("id=" + DashBoardPageId.DASHBOARDSETOPTIONSMENUID);
		driver.waitForElementPresent("css=" + DashBoardPageId.DASHBOARDSETOPTIONSPRINTCSS);
		driver.click("css=" + DashBoardPageId.DASHBOARDSETOPTIONSPRINTCSS);

		//have to use thread sleep to wait for the print window(windows dialog) to appear
		try {
			Thread.sleep(waitTime);
		}
		catch (InterruptedException e) {
			driver.getLogger().info(e.getLocalizedMessage());
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
		driver.waitForElementPresent("xpath=" + DashBoardPageId.BUILDEROPTIONSMENULOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.click(DashBoardPageId.BUILDEROPTIONSMENULOCATOR);

		driver.click(DashBoardPageId.BUILDEROPTIONSAUTOREFRESHLOCATOR);

		driver.waitForElementPresent(DashBoardPageId.BUILDEROPTIONSAUTOREFRESHOFFLOCATOR);
		switch (refreshSettings) {
			case REFRESH_DASHBOARD_SETTINGS_OFF:
				driver.check(DashBoardPageId.BUILDEROPTIONSAUTOREFRESHOFFLOCATOR);
				driver.waitForElementPresent(DashBoardPageId.BUILDERAUTOREFRESHOFFSELECTEDLOCATOR);
				break;
			case REFRESH_DASHBOARD_SETTINGS_5MIN:
				driver.check(DashBoardPageId.BUILDEROPTIONSAUTOREFRESHON5MINLOCATOR);
				driver.waitForElementPresent(DashBoardPageId.BUILDERAUTOREFRESHON5MINSELECTEDLOCATOR);
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
		int widgetElIndex = 0;
		try {
			widgetElIndex = getWidgetByName(driver, widgetName, index);
		}
		catch (InterruptedException e) {
			driver.getLogger().info(e.getLocalizedMessage());
		}

		focusOnWidgetHeader(driver, widgetElIndex);
		
		driver.click("xpath=(" + DashBoardPageId.CONFIGTILEXPATH + ")[" + widgetElIndex + "]");
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

		int widgetElIndex = 0;
		try {
			widgetElIndex = getWidgetByName(driver, widgetName, index);
		}
		catch (InterruptedException e) {
			driver.getLogger().info(e.getLocalizedMessage());
		}

		focusOnWidgetHeader(driver, widgetElIndex);
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

		driver.click("xpath=(" + DashBoardPageId.CONFIGTILEXPATH + ")[" + widgetElIndex + "]");
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
	public void switchTextWidgetToEditMode(WebDriver driver, int index) {
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

		driver.waitForElementVisible("css=" + DashBoardPageId.DASHBOARDSETNAVSCONTAINERCSS);
		WaitUtil.waitForPageFullyLoaded(driver);
		
		boolean hasFound = false;
		driver.waitForElementVisible("id=" + DashBoardPageId.BUILDEROPTIONSDUPLICATENAMECSS);
		
		int navCount = driver.getElementCount("xpath=" + DashBoardPageId.DASHBOARDSETNAVSXPATH);
		if (navCount <= 0) {
			throw new NoSuchElementException("verifyDashboardInsideSet: the dashboard navigators is not found");
		}
		
		String navAttr = null;
		for(int i=0; i<navCount; i++) {
			navAttr = driver.getAttribute("xpath=(" + DashBoardPageId.DASHBOARDSETNAVSXPATH + ")[" + (i + 1) + "]@data-dashboard-name-in-set").trim();
			
			if(navAttr != null && dashboardName.equals(navAttr)) {
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

		int widgetIndex = getTileTitleElement(driver, widgetName, index);		
		if(widgetIndex > 0)
		{
			driver.getLogger().info("DashboardBuilderUtil.verifyWidget compelted and returns true");
			return true;
		}
		else
		{
			driver.getLogger().info("DashboardBuilderUtil.verifyWidget compelted and returns false");
			return false;
		}
	}

	private void clickTileConfigButton(WebDriver driver, String widgetName, int index)
	{
		int i = getTileTitleElement(driver, widgetName, index);
		driver.click("xpath=(" + DashBoardPageId_190.BUILDERTILEHEADERLOCATOR + ")[" + i + "]" + DashBoardPageId_190.BUILDERTILECONFIGLOCATOR);
	}

	private void clickTileOpenInDataExplorerButton(WebDriver driver, String widgetName, int index)
	{
		driver.getLogger().info("Start to find widget with widgetName=" + widgetName + ", index=" + index);
		int widgetTitleIndex = getTileTitleElement(driver, widgetName, index);
		if (widgetTitleIndex == 0) {
			throw new NoSuchElementException("Widget with title=" + widgetName + ", index=" + index + " is not found");
		}
		driver.getLogger().info("Found widget with name=" + widgetName + ", index =" + index + " before opening widget link");
		
		driver.click("xpath=(" + DashBoardPageId.BUILDERTILEHEADERLOCATOR + ")[" + widgetTitleIndex + "]" + DashBoardPageId.BUILDERTILEDATAEXPLORELOCATOR);
	}

	private void focusOnWidgetHeader(WebDriver driver, int index)
	{
		if (index == 0) {
			driver.getLogger().info("Fail to find the widget element");
			throw new NoSuchElementException("Widget config menu is not found");
		}
		
		driver.moveToElement("xpath=(" + DashBoardPageId.TILETITLEXPATH + ")[" + index +"]");
		driver.getLogger().info("Focus to the widget");
	}

	private int getTileTitleElement(WebDriver driver, String widgetName, int index)
	{	
		driver.click(DashBoardPageId.BUILDERTILESEDITAREA);

		String titleTitlesLocator = String.format(DashBoardPageId.BUILDERTILETITLELOCATOR, widgetName);
		int tileTitlesCount = driver.getElementCount("xpath=" + titleTitlesLocator);
		if (tileTitlesCount <= 0) {
			return 0;
		}
		
		int i = 0;
		driver.moveToElement("xpath=("+ titleTitlesLocator +")[" + (index + 1) + "]");
		WaitUtil.waitForPageFullyLoaded(driver);
		
		int tileCount = driver.getElementCount("xpath=" + DashBoardPageId.BUILDERTILELOCATOR);
		int j = 1;
		boolean isExist = false;
		for (j=1; j<=tileCount; j++) {
			if (widgetName.equals(driver.getText("xpath=(" + DashBoardPageId.BUILDERTILELOCATOR + ")[" + j + "]").trim())) {
				i++;
				if ( i == (index + 1) ) {
					isExist = true;
					break;
				}
			}
		}
		if (isExist) {
			return j;
		}
		else
			return 0;
	}

	private int getWidgetByName(WebDriver driver, String widgetName, int index) throws InterruptedException
	{
		if (widgetName == null) {
			return 0;
		}
		
		int widgetsCount = driver.getElementCount("css=" + DashBoardPageId.WIDGETTITLECSS);
		int counter = 0;
		
		int i=1;
		for(i=1; i<=widgetsCount; i++) {
			String widgetAttribute = driver.getAttribute("xpath=(" + DashBoardPageId.TILETITLEXPATH + ")[" + i + "]@data-tile-title");
			Validator.notNull("widgetTitleAttribute", widgetAttribute);

			if (widgetAttribute.trim().equals(widgetName)) {
				if (counter == index) {
					break;
				}
				counter++;
			}
		}
		return i;
	}

	private boolean isRightDrawerVisible(WebDriver driver)
	{
		boolean isDisplayed = "none".equals(driver.getCssValue("css=" + DashBoardPageId.RIGHTDRAWERPANELCSS, "display")) != true;
        driver.getLogger().info("DashboardBuilderUtil.isRightDrawerVisible,the isDisplayed value is " + isDisplayed);

        boolean isWidthValid = "0px".equals(driver.getCssValue("css=" + DashBoardPageId.RIGHTDRAWERPANELCSS, "width")) != true;
        driver.getLogger().info("DashboardBuilderUtil.isRightDrawerVisible,the isWidthValid value is " + isWidthValid);
        
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

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#toggleHTMLWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void toggleHTMLWidget(WebDriver driver)
	{
		// TODO Auto-generated method stub
		driver.getLogger().info("Method not available in the current version");		
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetAlign(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, java.lang.String)
	 */
	@Override
	public void editHTMLWidgetAlign(WebDriver driver, int index, String align)
	{
		// TODO Auto-generated method stub
		driver.getLogger().info("Method not available in the current version");	
		
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetAlign(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void editHTMLWidgetAlign(WebDriver driver, String align)
	{
		// TODO Auto-generated method stub
		driver.getLogger().info("Method not available in the current version");
		
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetFont(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, java.lang.String)
	 */
	@Override
	public void editHTMLWidgetFont(WebDriver driver, int index, String fontname)
	{
		// TODO Auto-generated method stub
		driver.getLogger().info("Method not available in the current version");	
		
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetFont(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void editHTMLWidgetFont(WebDriver driver, String fontname)
	{
		// TODO Auto-generated method stub
		driver.getLogger().info("Method not available in the current version");	
		
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetFontSize(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int)
	 */
	@Override
	public void editHTMLWidgetFontSize(WebDriver driver, int fontsize)
	{
		// TODO Auto-generated method stub
		driver.getLogger().info("Method not available in the current version");	
		
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetFontSize(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, int)
	 */
	@Override
	public void editHTMLWidgetFontSize(WebDriver driver, int index, int fontsize)
	{
		// TODO Auto-generated method stub
		driver.getLogger().info("Method not available in the current version");	
		
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetFontBold(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, java.lang.Boolean)
	 */
	@Override
	public void editHTMLWidgetFontBold(WebDriver driver, int index, Boolean bold)
	{
		// TODO Auto-generated method stub
		driver.getLogger().info("Method not available in the current version");	
		
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetFontBold(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.Boolean)
	 */
	@Override
	public void editHTMLWidgetFontBold(WebDriver driver, Boolean bold)
	{
		// TODO Auto-generated method stub
		driver.getLogger().info("Method not available in the current version");	
		
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetFontItalic(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.Boolean)
	 */
	@Override
	public void editHTMLWidgetFontItalic(WebDriver driver, Boolean bold)
	{
		// TODO Auto-generated method stub
		driver.getLogger().info("Method not available in the current version");	
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#toggleHTMLWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int)
	 */
	@Override
	public void toggleHTMLWidget(WebDriver driver,int index)
	{
		// TODO Auto-generated method stub
		driver.getLogger().info("Method not available in the current version");		
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetSourceContent(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, java.lang.String)
	 */
	@Override
	public void editHTMLWidgetSourceContent(WebDriver driver, int index, String content)
	{
		// TODO Auto-generated method stub
		driver.getLogger().info("Method not available in the current version");	
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetSourceContent(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void editHTMLWidgetSourceContent(WebDriver driver, String content)
	{
		// TODO Auto-generated method stub
		driver.getLogger().info("Method not available in the current version");
	}

}
