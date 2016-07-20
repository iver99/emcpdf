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

import java.util.List;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public interface IDashboardHomeUtil extends IUiTestCommonAPI
{
	public static final String TYPE_DASHBOARD = "dashboard";
	public static final String TYPE_DASHBOARDSET = "dashboardSet";

	public static final String DASHBOARD = "dashboard";
	public static final String DASHBOARDSET = "dashboardSet";
	public static final String EXPLOREDATA_MENU_ANALYZE = "Analyze";
	public static final String EXPLOREDATA_MENU_LOG = "Log Visual Analyzer";
	public static final String EXPLOREDATA_MENU_SEARCH = "Search";
	public static final String DASHBOARDS_GRID_VIEW = "dashboards_grid_view";
	public static final String DASHBOARDS_LIST_VIEW = "dashboards_list_view";
	public static final String DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_ASC = "access_date_asc";
	public static final String DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_DSC = "access_date_dsc";
	public static final String DASHBOARD_QUERY_ORDER_BY_NAME_ASC = "name_asc";
	public static final String DASHBOARD_QUERY_ORDER_BY_NAME_DSC = "name_dsc";
	public static final String DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_ASC = "creation_date_asc";
	public static final String DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_DSC = "creation_date_dsc";
	public static final String DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_ASC = "last_modification_date_asc";
	public static final String DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_DSC = "last_modification_date_dsc";
	public static final String DASHBOARD_QUERY_ORDER_BY_OWNER_ASC = "owner_asc";
	public static final String DASHBOARD_QUERY_ORDER_BY_OWNER_DSC = "owner_dsc";

	public void closeOverviewPage(WebDriver driver) throws Exception;

	/**
	 * Create one Dashboard
	 *
	 * @param driver
	 * @param name
	 *            dashboard name
	 * @param descriptions
	 *            dashboard description(optional)
	 * @throws Exception
	 */
	public void createDashboard(WebDriver driver, String name, String descriptions) throws Exception;

	/**
	 * Create one Dashboard or Dashbaord Set
	 *
	 * @param driver
	 * @param name
	 *            dashboard name
	 * @param descriptions
	 *            dashboard description(optional)
	 * @param type
	 *            dashboard | dashboardSet
	 * @throws Exception
	 */
	public void createDashboard(WebDriver driver, String name, String descriptions, String type) throws Exception;

	/**
	 * Create one Dashbaord Set
	 *
	 * @param driver
	 * @param name
	 *            dashboard set name
	 * @param descriptions
	 *            dashboard set description(optional)
	 * @throws Exception
	 */
	public void createDashboardSet(WebDriver driver, String name, String descriptions) throws Exception;

	/**
	 * Delete one dashboard by name
	 *
	 * @param dashboardName
	 *            dashboard name
	 * @param view
	 *            dashboards_grid_view | dashboards_list_view
	 * @throws Exception
	 */
	public void deleteDashboard(WebDriver driver, String dashboardName, String view) throws Exception;

	/**
	 * add filter
	 *
	 * @param driver
	 * @param filter
	 *            filter name - apm,la,ita,oracle,share,me,favorites(multiple choice and split with comma)
	 * @throws Exception
	 */
	public void filterOptions(WebDriver driver, String filter) throws Exception;

	/**
	 * goto the link in Data Explorer by displayed name
	 *
	 * @param option
	 *            Analyze | Log Visual Analyzer | Search
	 * @throws Exception
	 */
	public void gotoDataExplorer(WebDriver driver, String option) throws Exception;

	/**
	 * choose grid view
	 *
	 * @param driver
	 * @throws Exception
	 */
	public void gridView(WebDriver driver) throws Exception;

	/**
	 * check if the dashboard is existing or not by name
	 *
	 * @param driver
	 * @param dashboardName
	 * @return
	 * @throws Exception
	 */
	public boolean isDashboardExisted(WebDriver driver, String dashboardName) throws Exception;

	/**
	 * check if the filter is selected by filter name
	 *
	 * @param driver
	 * @param filter
	 *            filer name - apm, la , ita, oracle, share, me favorites(single choice)
	 * @return
	 * @throws Exception
	 */
	public boolean isFilterOptionSelected(WebDriver driver, String filter) throws Exception;

	public List<String> listDashboardNames(WebDriver driver) throws Exception;

	/**
	 * choose the list view
	 *
	 * @param driver
	 * @throws Exception
	 */
	public void listView(WebDriver driver) throws Exception;

	/**
	 * reset the filters
	 *
	 * @param driver
	 * @throws Exception
	 */
	public void resetFilterOptions(WebDriver driver) throws Exception;

	/**
	 * search dashboard
	 *
	 * @param driver
	 * @param searchString
	 * @throws Exception
	 */
	public void search(WebDriver driver, String searchString) throws Exception;

	/**
	 * Select an any-type dashboard by name, including OOB dashboard & user created dashboard
	 *
	 * @param driver
	 * @param dashboardName
	 * @throws Exception
	 */
	public void selectDashboard(WebDriver driver, String dashboardName) throws Exception;

	/**
	 * Select an OOB dashboard by name
	 *
	 * @param driver
	 * @param dashboardName
	 * @throws Exception
	 */
	public void selectOOB(WebDriver driver, String dashboardName) throws Exception;

	/**
	 * sort dashboards
	 *
	 * @param driver
	 * @param option
	 *            sort by - default, access_date_asc, access_date_dsc, name_asc, name_dsc, creation_date_asc, creation_date_dsc,
	 *            last_modification_date_asc, last_modification_date_dsc, owner_asc, owner_dsc
	 * @throws Exception
	 */
	public void sortBy(WebDriver driver, String option) throws Exception;

	/**
	 * @param driver
	 * @throws Exception
	 */
	public void sortListViewByCreateBy(WebDriver driver) throws Exception;

	/**
	 * @param driver
	 * @throws Exception
	 */
	public void sortListViewByLastModified(WebDriver driver) throws Exception;

	/**
	 * @param driver
	 * @throws Exception
	 */
	public void sortListViewByName(WebDriver driver) throws Exception;

	/**
	 * wait the dashboard by name
	 *
	 * @param driver
	 * @param dashboardName
	 * @throws Exception
	 */
	public void waitForDashboardPresent(WebDriver driver, String dashboardName) throws Exception;

}
