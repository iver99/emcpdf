/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.util;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public interface IDashboardBuilderUtil extends IUiTestCommonAPI
{
	public static final String REFRESH_DASHBOARD_SETTINGS_OFF = "OFF";
	public static final String REFRESH_DASHBOARD_SETTINGS_5MIN = "On (Every 5 Minutes)";
	public static final String TILE_WIDER = "wider";
	public static final String TILE_NARROWER = "narrower";
	public static final String TILE_TALLER = "taller";
	public static final String TILE_SHORTER = "shorter";
	public static final String TILE_UP = "up";
	public static final String TILE_DOWN = "down";
	public static final String TILE_LEFT = "left";
	public static final String TILE_RIGHT = "right";

	public void addNewDashboardToSet(WebDriver driver, String dashboardName);
	
	public void addTextWidgetToDashboard(WebDriver driver);
	
	public void addLinkInTextWidget(WebDriver driver, int index, String url, String option);

	public void addWidgetToDashboard(WebDriver driver, String searchString);
	
	public void addImageInTextWidget(WebDriver driver, int index, String url, String alternativeText);

	public void createDashboardInsideSet(WebDriver driver, String name, String descriptions);

	public void deleteDashboard(WebDriver driver);

	public void deleteDashboardInsideSet(WebDriver driver);

	public void deleteDashboardSet(WebDriver driver);

	public void duplicateDashboard(WebDriver driver, String name, String descriptions);

	public void duplicateDashboardInsideSet(WebDriver driver, String name, String descriptions, boolean addToSet);

	public void editDashboard(WebDriver driver, String name, String descriptions);

	public void editDashboard(WebDriver driver, String name, String descriptions, Boolean toShowDscptn);

	public void editDashboardSet(WebDriver driver, String name, String descriptions);
	
	public void editTextWidgetAddContent(WebDriver driver, int index, String content);

	public Boolean favoriteOption(WebDriver driver);

	public Boolean favoriteOptionDashboardSet(WebDriver driver);

	public void gridView(WebDriver driver);

	public boolean isRefreshSettingChecked(WebDriver driver, String refreshSettings);

	public boolean isRefreshSettingCheckedForDashbaordSet(WebDriver driver, String refreshSettings);

	//	public void loadWebDriverOnly(WebDriver webDriver)
	//	{
	//		driver = webDriver;
	//	}

	public void listView(WebDriver driver);

	/**
	 * @param driver
	 * @param widgetName
	 * @param index
	 */
	public void maximizeWidget(WebDriver driver, String widgetName, int index);

	public void moveWidget(WebDriver driver, String widgetName, int index, String moveOption);

	public void moveWidget(WebDriver driver, String widgetName, String moveOption);

	public void openWidget(WebDriver driver, String widgetName);

	public void openWidget(WebDriver driver, String widgetName, int index);

	public void printDashboard(WebDriver driver);

	public void printDashboardSet(WebDriver driver);

	public void refreshDashboard(WebDriver driver, String refreshSettings);

	public void refreshDashboardSet(WebDriver driver, String refreshSettings);

	public void removeDashboardFromSet(WebDriver driver, String dashboardName);

	public void removeWidget(WebDriver driver, String widgetName);

	public void removeWidget(WebDriver driver, String widgetName, int index);

	public void resizeWidget(WebDriver driver, String widgetName, int index, String resizeOptions);

	public void resizeWidget(WebDriver driver, String widgetName, String resizeOptions);

	public boolean respectGCForEntity(WebDriver driver);

	public boolean respectGCForTimeRange(WebDriver driver);

	/**
	 * @param driver
	 * @param widgetName
	 * @param index
	 */
	public void restoreWidget(WebDriver driver, String widgetName, int index);

	public void saveDashboard(WebDriver driver);

	public void search(WebDriver driver, String searchString);

	public void selectDashboard(WebDriver driver, String dashboardName);

	public void selectDashboardInsideSet(WebDriver driver, String dashboardName);

	public void setEntitySupport(WebDriver driver, String mode);
	
	public void switchTextWidgetToEditMode(WebDriver driver, int index);

	public boolean showEntityFilter(WebDriver driver, boolean showEntityFilter);

	public boolean showTimeRangeFilter(WebDriver driver, boolean showTimeRangeFilter);

	public void showWidgetTitle(WebDriver driver, String widgetName, boolean visibility);

	public void showWidgetTitle(WebDriver driver, String widgetName, int index, boolean visibility);

	public void addLinkToWidgetTitle(WebDriver driver, String widgetName, String dashboardName);

	public void addLinkToWidgetTitle(WebDriver driver, String widgetName, int index, String dashboardName);

	public boolean verifyLinkOnWidgetTitle(WebDriver driver, String widgetName, String dashboardName);

	public boolean verifyLinkOnWidgetTitle(WebDriver driver, String widgetName, int index, String dashboardName);

	/**
	 * sort dashboards
	 *
	 * @param driver
	 * @param option
	 *            sort by - default, access_date_asc, access_date_dsc, name_asc, name_dsc, creation_date_asc, creation_date_dsc,
	 *            last_modification_date_asc, last_modification_date_dsc, owner_asc, owner_dsc @
	 */
	public void sortBy(WebDriver driver, String option);

	public Boolean toggleHome(WebDriver driver);

	public Boolean toggleHomeDashboardSet(WebDriver driver);

	public Boolean toggleShareDashboard(WebDriver driver);

	public Boolean toggleShareDashboardset(WebDriver driver);

	public boolean verifyDashboard(WebDriver driver, String dashboardName, String description, boolean showTimeSelector);

	public boolean verifyDashboardInsideSet(WebDriver driver, String dashboardName);

	public boolean verifyDashboardSet(WebDriver driver, String dashboardSetName);

	public boolean verifyWidget(WebDriver driver, String widgetName);

	public boolean verifyWidget(WebDriver driver, String widgetName, int index);

	public boolean hasWidgetLink(WebDriver driver, String widgetName);
	/**
	 * @param driver
	 * @param widgetName
	 * @param index
	 * @return
	 */
	public boolean hasWidgetLink(WebDriver driver, String widgetName, int index);

	/**
	 * @param driver
	 * @param widgetName
	 * @param index
	 * @return
	 */	
	public void clickLinkOnWidgetTitle(WebDriver driver, String widgetName, int index);
	
	public void clickLinkOnWidgetTitle(WebDriver driver, String widgetName);
}
