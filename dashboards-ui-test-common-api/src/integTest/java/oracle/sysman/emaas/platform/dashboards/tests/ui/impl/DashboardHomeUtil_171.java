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

import java.util.ArrayList;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import com.oracle.sysman.uifwk.qatools.jetutils.*;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class DashboardHomeUtil_171 extends DashboardHomeUtil_Version implements IDashboardHomeUtil
{
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#closeOverviewPage(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void closeOverviewPage(WebDriver driver)
	{
		if (driver.isDisplayed(DashBoardPageId.OVERVIEWCLOSEID)) {
			driver.getLogger().info("before clicking overview button");
			driver.click(DashBoardPageId.OVERVIEWCLOSEID);
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#createDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public void createDashboard(WebDriver driver, String name, String descriptions)
	{
		createDashboard(driver, name, descriptions, TYPE_DASHBOARD);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#createDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void createDashboard(WebDriver driver, String name, String descriptions, String type)
	{
		driver.getLogger().info("[DashboardHomeUtil] call createDashboard : " + name);
		driver.click(convertID(DashBoardPageId.CREATEDSBUTTONID));

		if (name != null && !name.isEmpty()) {
			driver.sendKeys(convertID(DashBoardPageId.DASHBOARDNAMEBOXID), name);
		}
		if (descriptions != null && !descriptions.isEmpty()) {
			driver.sendKeys(convertID(DashBoardPageId.DASHBOARDDESCBOXID), descriptions);
		}
		if (DASHBOARD.equalsIgnoreCase(type)) {
			driver.check(convertID(DashBoardPageId.DASHBOARDTYPE_SINGLE));
		}
		else if (DASHBOARDSET.equalsIgnoreCase(type)) {
			driver.check(convertID(DashBoardPageId.DASHBOARDTYPE_SET));
		}

		boolean isDisplayed = driver.isDisplayed(convertID(DashBoardPageId.DASHOKBUTTONID));
		driver.getLogger().info("isDisplayed:" + isDisplayed);
		driver.click(convertID(DashBoardPageId.DASHOKBUTTONID));
		driver.setPageLoadDetector(BuildPageLoadDetector.class);
		driver.setPageLoadDetector(null);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#createDashboardSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public void createDashboardSet(WebDriver driver, String name, String descriptions)
	{
		createDashboard(driver, name, descriptions, TYPE_DASHBOARDSET);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#deleteDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteDashboard(WebDriver driver, String dashboardName, String view)
	{

		driver.getLogger().info("[DashboardHomeUtil] call delete dashboardName : " + dashboardName);
		Validator.notEmptyString("dashboardName", dashboardName);
		search(driver, dashboardName);
		if (DASHBOARDS_GRID_VIEW.equals(view)) {
			gridView(driver);
			deleteDashboardInGrid(driver, dashboardName);
		}

		if (DASHBOARDS_LIST_VIEW.equals(view)) {
			listView(driver);
			deleteDashboardInList(driver, dashboardName);
		}
		WaitUtil.waitForPageFullyLoaded(driver);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#filterOptions(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void filterOptions(WebDriver driver, String filter)
	{
		driver.getLogger().info("[DashboardHomeUtil] call filterOptions filter: " + filter);
		Validator.notEmptyString("filter", filter);
		String[] fs = filter.split(",");
		List<String> trimedFs = new ArrayList<String>();
		for (String s : fs) {
			trimedFs.add(s.trim());
		}
		if (trimedFs.contains("apm")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERAPMLOCATOR);
			driver.click(DashBoardPageId.FILTERAPMLOCATOR);

		}
		if (trimedFs.contains("la")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERLALOCATOR);
			driver.click(DashBoardPageId.FILTERLALOCATOR);

		}
		if (trimedFs.contains("ita")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERITALOCATOR);
			driver.click(DashBoardPageId.FILTERITALOCATOR);

		}
		if (trimedFs.contains("oracle")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERORACLELOCATOR);
			driver.click(DashBoardPageId.FILTERORACLELOCATOR);

		}
		if (trimedFs.contains("share")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERSHARELOCATOR);
			driver.click(DashBoardPageId.FILTERSHARELOCATOR);

		}
		if (trimedFs.contains("me")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERMELOCATOR);
			driver.click(DashBoardPageId.FILTERMELOCATOR);

		}
		if (trimedFs.contains("favorites")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERFAVORITELOCATOR);
			driver.click(DashBoardPageId.FILTERFAVORITELOCATOR);

		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IUiTestCommonAPI#getApiVersion(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public String getApiVersion(WebDriver wdriver)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#gotoDataExplorer(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void gotoDataExplorer(WebDriver driver, String option)
	{
		driver.getLogger().info("[DashboardHomeUtil] call exploreData -> " + option);

		Validator.notEmptyString("option", option);

		driver.click(convertName(DashBoardPageId.EXPLOREDATABTNID));
		//WebElement menu = driver.getElement(convertName(DashBoardPageId.EXPLOREDATAMENU));

		if (IDashboardHomeUtil.EXPLOREDATA_MENU_LOG.equals(option)) {
			driver.click(DashBoardPageId.EXPLORE_LOG);
		}
		else {
			driver.click(DashBoardPageId.EXPLORE_Search);
		}

	}

	/*List<WebElement> menuList = menu.findElements(By.tagName("li"));
	for (WebElement menuItem : menuList) {
		if (option.equals(menuItem.getText())) {
			menuItem.click();
			break;
		}
	}
	}*/

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#gridView(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void gridView(WebDriver driver)
	{
		driver.getLogger().info("[DashboardHomeUtil] call gridView");
		driver.waitForElementPresent(DashBoardPageId.DASHBOARDSGRIDVIEWLOCATOR);

		driver.click(DashBoardPageId.DASHBOARDSGRIDVIEWLOCATOR);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#isDashboardExisted(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isDashboardExisted(WebDriver driver, String dashboardName)
	{
		driver.getLogger().info("[DashboardHomeUtil] call isDashboardExists dashboardName: " + dashboardName);
		Validator.notEmptyString("dashboardName", dashboardName);
		String indicator = DashBoardPageId.DASHBOARDNAMELOCATOR.replace("_name_", dashboardName);
		if (!driver.isElementPresent(indicator)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#isFilterOptionSelected(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isFilterOptionSelected(WebDriver driver, String filter)
	{
		driver.getLogger().info("[DashboardHomeUtil] call isFilterOptionSelected filter: " + filter);
		Validator.notEmptyString("filter", filter);
		if ("apm".equals(filter)) {
			return driver.isSelected(DashBoardPageId.FILTERAPMLOCATOR);
		}
		else if ("la".equals(filter)) {
			return driver.isSelected(DashBoardPageId.FILTERLALOCATOR);
		}
		else if ("ita".equals(filter)) {
			return driver.isSelected(DashBoardPageId.FILTERITALOCATOR);
		}
		else if ("oracle".equals(filter)) {
			return driver.isSelected(DashBoardPageId.FILTERORACLELOCATOR);
		}
		else if ("share".equals(filter)) {
			return driver.isSelected(DashBoardPageId.FILTERSHARELOCATOR);
		}
		else if ("me".equals(filter)) {
			return driver.isSelected(DashBoardPageId.FILTERMELOCATOR);
		}
		else if ("favorites".equals(filter)) {
			return driver.isSelected(DashBoardPageId.FILTERFAVORITELOCATOR);
		}
		else {
			throw new IllegalArgumentException("Unkonw filter option: " + filter);
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#listDashboardNames(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public List<String> listDashboardNames(WebDriver driver)
	{
		WaitUtil.waitForPageFullyLoaded(driver);
		
		List<String> names = new ArrayList<String>();
		int eles = driver.getElementCount(DashBoardPageId.DASHBOARDNAMECONTAINERS);
		String locator = null;
		for (int i = 1; i <= eles; i++) {
			locator = DashBoardPageId.DASHBOARDNAMEINDEXLOCATOR.replaceFirst("_index_", String.valueOf(i));
			driver.getLogger().info("Get dahsbord name for: " + locator);
			driver.waitForElementPresent(locator);
			
			WaitUtil.waitForPageFullyLoaded(driver);	

			String name = driver.getAttribute(locator + "@aria-label");
			if (name == null) {
				name = driver.getText(locator);
			}
			names.add(name);
		}		
		return names;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#listView(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void listView(WebDriver driver)
	{
		driver.getLogger().info("[DashboardHomeUtil] call listView");
		driver.waitForElementPresent(DashBoardPageId.DASHBOARDSLISTVIEWLOCATOR);

		driver.click(DashBoardPageId.DASHBOARDSLISTVIEWLOCATOR);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#resetFilterOptions(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void resetFilterOptions(WebDriver driver)
	{

		driver.getLogger().info("[DashboardHomeUtil] call resetFilterOptions");
		driver.waitForElementPresent(DashBoardPageId.FILTERAPMLOCATOR);
		
		if (driver.isSelected(DashBoardPageId.FILTERAPMLOCATOR)) {
			driver.click(DashBoardPageId.FILTERAPMLOCATOR);
		}
		
		if (driver.isSelected(DashBoardPageId.FILTERLALOCATOR)) {
			driver.click(DashBoardPageId.FILTERLALOCATOR);
		}
		
		if (driver.isSelected(DashBoardPageId.FILTERITALOCATOR)) {
			driver.click(DashBoardPageId.FILTERITALOCATOR);
		}
		
		if (driver.isSelected(DashBoardPageId.FILTERORCHESTRATIONLOCATOR)) {
			driver.click(DashBoardPageId.FILTERORCHESTRATIONLOCATOR);
		}
		
		if (driver.isSelected(DashBoardPageId.FILTERORACLELOCATOR)) {
			driver.click(DashBoardPageId.FILTERORACLELOCATOR);
		}
		
		if (driver.isSelected(DashBoardPageId.FILTERSHARELOCATOR)) {
			driver.click(DashBoardPageId.FILTERSHARELOCATOR);
		}
		
		if (driver.isSelected(DashBoardPageId.FILTERMELOCATOR)) {
			driver.click(DashBoardPageId.FILTERMELOCATOR);
		}
		
		if (driver.isSelected(DashBoardPageId.FILTERFAVORITELOCATOR)) {
			driver.click(DashBoardPageId.FILTERFAVORITELOCATOR);
		}
		WaitUtil.waitForPageFullyLoaded(driver);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#search(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void search(WebDriver driver, String searchString) 
    {
		driver.getLogger().info("[DashboardHomeUtil] call search searchString: " + searchString);
		Validator.notEmptyString("searchString", searchString);
		driver.getLogger().info("[DashboardHomeUtil] call search");
		driver.waitForElementPresent(DashBoardPageId.SEARCHDASHBOARDINPUTLOCATOR);
		driver.getElement(DashBoardPageId.SEARCHDASHBOARDINPUTLOCATOR).clear();
		driver.click(DashBoardPageId.SEARCHDASHBOARDINPUTLOCATOR);
		driver.sendKeys(DashBoardPageId.SEARCHDASHBOARDINPUTLOCATOR, searchString);
		WaitUtil.waitForPageFullyLoaded(driver);
		
		driver.evalJavascript("arguments[0].click();", driver.getElement(DashBoardPageId.SEARCHDASHBOARDSEARCHBTNLOCATOR));
		WaitUtil.waitForPageFullyLoaded(driver);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#selectDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void selectDashboard(WebDriver driver, String dashboardName)
	{
		driver.getLogger().info("[DashboardHomeUtil] call selectDashboard dashboardName: " + dashboardName);
		String indicator = DashBoardPageId.DASHBOARDNAMELOCATOR.replace("_name_", dashboardName);
		driver.setPageLoadDetector(BuildPageLoadDetector.class);
		driver.waitForElementEnabled(indicator);
		driver.click(indicator);
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.setPageLoadDetector(null);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#selectOOB(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void selectOOB(WebDriver driver, String dashboardName)
	{
		driver.getLogger().info("[DashboardHomeUtil] call selectOOB dashboardName: " + dashboardName);
		Validator.notEmptyString("dashboardName", dashboardName);
		String indicator = driver.isElementPresent(DashBoardPageId.OOBDASHBOARDNAMELOCATORLISTVIEW) ? DashBoardPageId.OOBDASHBOARDNAMELOCATOR
				.replace("_name_", dashboardName) : DashBoardPageId.OOBDASHBOARD_LIST_LINK.replace("_name_", dashboardName);
		if (!driver.isElementPresent(indicator)) {
			throw new NoSuchElementException("Dashboard not exists. Name: " + dashboardName);
		}
		driver.click(indicator);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#sortBy(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void sortBy(WebDriver driver, String option)
	{
		driver.getLogger().info("[DashboardHomeUtil] call sortBy option: " + option);
		Validator.notEmptyString("option", option);
		driver.waitForElementPresent(DashBoardPageId.SORTBYSELECTLOCATOR);
		driver.click(DashBoardPageId.SORTBYSELECTLOCATOR);

		if ("default".equals(option) || DASHBOARD_QUERY_ORDER_BY_DEFAULT.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SORTBYDEFAULTLOCATOR);
			driver.click(DashBoardPageId.SORTBYDEFAULTLOCATOR);

		}
		else if (DASHBOARD_QUERY_ORDER_BY_NAME_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SORTBYNAMEASCLOCATOR);
			driver.click(DashBoardPageId.SORTBYNAMEASCLOCATOR);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_NAME_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SORTBYNAMEDSCLOCATOR);
			driver.click(DashBoardPageId.SORTBYNAMEDSCLOCATOR);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_OWNER_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SORTBYCREATEDBYASCLOCATOR);
			driver.click(DashBoardPageId.SORTBYCREATEDBYASCLOCATOR);

		}
		else if (DASHBOARD_QUERY_ORDER_BY_OWNER_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SORTBYCREATEDBYDSCLOCATOR);
			driver.click(DashBoardPageId.SORTBYCREATEDBYDSCLOCATOR);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SORTBYCREATEDATEASCLOCATOR);
			driver.click(DashBoardPageId.SORTBYCREATEDATEASCLOCATOR);

		}
		else if (DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SORTBYCREATEDATEDSCLOCATOR);
			driver.click(DashBoardPageId.SORTBYCREATEDATEDSCLOCATOR);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SORTBYLASTMODIFIEDASCLOCATOR);
			driver.click(DashBoardPageId.SORTBYLASTMODIFIEDASCLOCATOR);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SORTBYLASTMODIFIEDDSCLOCATOR);
			driver.click(DashBoardPageId.SORTBYLASTMODIFIEDDSCLOCATOR);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SORTBYLASTACCESSASCLOCATOR);
			driver.click(DashBoardPageId.SORTBYLASTACCESSASCLOCATOR);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SORTBYLASTACCESSDSCLOCATOR);
			driver.click(DashBoardPageId.SORTBYLASTACCESSDSCLOCATOR);
		}
		else {
			throw new IllegalArgumentException("Unknow Sort by option: " + option);
		}

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#sortListViewByCreateBy(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void sortListViewByCreateBy(WebDriver driver, String sortBy)
	{	
		driver.getLogger().info("[DashboardHomeUtil] call clickListViewTableCreatedByHeader");	
		driver.waitForElementPresent(DashBoardPageId.LISTVIEWTABLECREATEDBYHEADERLOCATOR);	
//		WebElement tableHeader = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.LISTVIEWTABLECREATEDBYHEADERLOCATOR));	
//		WebElement tableSort = tableHeader.findElement(By.cssSelector(DashBoardPageId.LISTVIEWSORTLOCATORCSS));	
//		Actions actions = new Actions(driver.getWebDriver());	
		driver.getLogger().info("Focus to the table header");	
		driver.moveToElement("xpath=" + DashBoardPageId.LISTVIEWTABLECREATEDBYHEADERLOCATOR);
//		actions.moveToElement(tableHeader).build().perform();	
		driver.getLogger().info("Click Sort icon");		
		driver.click("xpath=" + DashBoardPageId.LISTVIEWTABLECREATEDBYHEADERLOCATOR + DashBoardPageId.LISTVIEWSORTLOCATORXPATH);
//		actions.moveToElement(tableSort).click().perform();	
//		driver.takeScreenShot();	
				
		WaitUtil.waitForPageFullyLoaded(driver);		
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#sortListViewByLastModified(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void sortListViewByLastModified(WebDriver driver, String sortBy)
	{
		driver.getLogger().info("[DashboardHomeUtil] call clickListViewTableLastModifiedHeader");
		driver.waitForElementPresent(DashBoardPageId.LISTVIEWTABLELASTMODIFIEDHEADERLOCATOR);
		
//		WebElement tableHeader = driver.getWebDriver().findElement(
//				By.xpath(DashBoardPageId.LISTVIEWTABLELASTMODIFIEDHEADERLOCATOR));
//		WebElement tableSort = tableHeader.findElement(By.cssSelector(DashBoardPageId.LISTVIEWSORTLOCATORCSS));
		
//		Actions actions = new Actions(driver.getWebDriver());
		driver.getLogger().info("Focus to the table header");	
		driver.moveToElement("xpath=" + DashBoardPageId.LISTVIEWTABLELASTMODIFIEDHEADERLOCATOR);
//		actions.moveToElement(tableHeader).build().perform();	
		driver.getLogger().info("Click Sort icon");		
//		actions.moveToElement(tableSort).click().perform();	
		driver.click("xpath=" + DashBoardPageId.LISTVIEWTABLELASTMODIFIEDHEADERLOCATOR + DashBoardPageId.LISTVIEWSORTLOCATORXPATH);
		WaitUtil.waitForPageFullyLoaded(driver);
			
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#sortListViewByName(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void sortListViewByName(WebDriver driver, String sortBy)
	{
		driver.getLogger().info("[DashboardHomeUtil] call clickListViewTableNameHeader");
		driver.waitForElementPresent(DashBoardPageId.LISTVIEWTABLENAMEHEADERLOCATOR);	
//		WebElement tableHeader = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.LISTVIEWTABLENAMEHEADERLOCATOR));	
//		WebElement tableSort = tableHeader.findElement(By.cssSelector(DashBoardPageId.LISTVIEWSORTLOCATORCSS));	
//		Actions actions = new Actions(driver.getWebDriver());	
		driver.getLogger().info("Focus to the table header");	
		driver.moveToElement("xpath=" + DashBoardPageId.LISTVIEWTABLENAMEHEADERLOCATOR);
//		actions.moveToElement(tableHeader).build().perform();		
		driver.getLogger().info("Click Sort icon");	
		driver.click("xpath=" + DashBoardPageId.LISTVIEWTABLENAMEHEADERLOCATOR + DashBoardPageId.LISTVIEWSORTLOCATORXPATH);
//		actions.moveToElement(tableSort).click().perform();			
		
		WaitUtil.waitForPageFullyLoaded(driver);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#waitForDashboardPresent(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void waitForDashboardPresent(WebDriver driver, String dashboardName)
	{
		Validator.notEmptyString("dashboardName", dashboardName);
		driver.getLogger().info("[DashboardHomeUtil] call waitForDashboardPresent dashboardName: " + dashboardName);
		String indicator = DashBoardPageId.DASHBOARDNAMELOCATOR.replace("_name_", dashboardName);
		driver.waitForElementPresent(indicator);
	}

	private String convertCss(String cssName)
	{
		return "css=" + cssName;
	}

	private String convertID(String id)
	{
		return "id=" + id;
	}

	private void deleteDashboardInGrid(WebDriver driver, String dashboardName)
	{
		driver.waitForElementVisible(convertCss(DashBoardPageId.DASHBOARD_GRID_TABLE_CSS));
		
		int dashboardsCount = driver.getElementCount("xpath=" + DashBoardPageId.DASHBOARD_IN_GRID_TABLE_XPATH);
		for(int i=1; i<=dashboardsCount; i++) {
			if (dashboardName.equals(driver.getAttribute("xpath=(" + DashBoardPageId.DASHBOARD_IN_GRID_TABLE_XPATH +")[" + i + "]@aria-label"))) {
				driver.click("xpath=(" + DashBoardPageId.DASHBOARD_IN_GRID_TABLE_XPATH +")[" + i + "]//button"); // click "i" button

				driver.click(convertName(DashBoardPageId.DASHBOARD_HOME_DELETE_BUTTON)); // click delete

				driver.waitForElementPresent(convertCss(DashBoardPageId.DASHBOARD_HOME_DELETE_DIALOG));
				driver.getLogger().info("foucus on the delete button");
				driver.sendKeys(convertName(DashBoardPageId.DASHBOARD_HOME_DELETE_CONFIRM), Keys.TAB);
				
				driver.getLogger().info("click on the delete button");
				driver.click(convertName(DashBoardPageId.DASHBOARD_HOME_DELETE_CONFIRM)); // confirm to delete

				driver.getLogger().info("wait for the popup dialog close");				
				driver.waitForElementNotVisible("css=" + DashBoardPageId.DASHBOARD_HOME_DELETE_DIALOG);
				break;
			}
		}
//		WebElement gridTable = driver.getElement(convertCss(DashBoardPageId.DASHBOARD_GRID_TABLE_CSS));
//		List<WebElement> dashboardList = gridTable.findElements(By.tagName("div"));
//		for (WebElement dashboard : dashboardList) {
//			if (dashboardName.equals(dashboard.getAttribute("aria-label"))) {
//				dashboard.findElement(By.cssSelector("button")).click(); // click "i" button
//
//				driver.click(convertName(DashBoardPageId.DASHBOARD_HOME_DELETE_BUTTON)); // click delete
//
//				driver.waitForElementPresent(convertCss(DashBoardPageId.DASHBOARD_HOME_DELETE_DIALOG));
//				driver.getLogger().info("foucus on the delete button");
//				driver.sendKeys(convertName(DashBoardPageId.DASHBOARD_HOME_DELETE_CONFIRM), Keys.TAB);
//				
//				driver.getLogger().info("click on the delete button");
//				driver.click(convertName(DashBoardPageId.DASHBOARD_HOME_DELETE_CONFIRM)); // confirm to delete
//
//				driver.getLogger().info("wait for the popup dialog close");				
//				driver.waitForElementNotVisible("css=" + DashBoardPageId.DASHBOARD_HOME_DELETE_DIALOG);
//				break;
//			}
//		}
	}

	private void deleteDashboardInList(WebDriver driver, String dashboardName)
	{		
		// find table
		WebElement listTable = driver.getElement(convertCss(DashBoardPageId.DASHBOARD_LIST_TABLE));
		// find the column index of both "Name" & button
		WebElement headRow = listTable.findElement(By.tagName("thead")).findElement(By.tagName("tr"));
		List<WebElement> headColList = headRow.findElements(By.tagName("th"));
		int buttonColIndex = headColList.size() - 1;
		int nameColIndex = 0;
		for (int i = 0; i < headColList.size(); i++) {
			WebElement headCol = headColList.get(i);
			if ("Name".equals(headCol.getAttribute("title"))) {
				nameColIndex = i;
				break;
			}
		}
		// find the row whose name is equal with dashboardName and click the delete
		List<WebElement> rowList = listTable.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
		for (WebElement row : rowList) {
			List<WebElement> rowColList = row.findElements(By.tagName("td"));
			if (dashboardName.equals(rowColList.get(nameColIndex).getText())) {
				rowColList.get(buttonColIndex).findElement(By.tagName("button")).click(); // click "i" button
				driver.click(convertName(DashBoardPageId.DASHBOARD_HOME_DELETE_BUTTON)); // click delete

				driver.waitForElementPresent(convertCss(DashBoardPageId.DASHBOARD_HOME_DELETE_DIALOG));
				driver.getLogger().info("foucus on the delete button");
				driver.getWebDriver().findElement(By.name(DashBoardPageId.DASHBOARD_HOME_DELETE_CONFIRM)).sendKeys(Keys.TAB);

				driver.getLogger().info("click on the delete button");
				driver.click(convertName(DashBoardPageId.DASHBOARD_HOME_DELETE_CONFIRM)); // confirm to delete
				driver.waitForElementNotVisible("css=" + DashBoardPageId.DASHBOARD_HOME_DELETE_DIALOG);
				break;
			}
		}
	}	

	protected String convertName(String name)
	{
		return "name=" + name;
	}
}
