package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.UtilLoader;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class DashboardHomeUtil
{
	public static final String DASHBOARD = IDashboardHomeUtil.DASHBOARD;

	public static final String DASHBOARDSET = IDashboardHomeUtil.DASHBOARDSET;
	public static final String EXPLOREDATA_MENU_ANALYZE = IDashboardHomeUtil.EXPLOREDATA_MENU_ANALYZE;
	public static final String EXPLOREDATA_MENU_LOG = IDashboardHomeUtil.EXPLOREDATA_MENU_LOG;
	public static final String EXPLOREDATA_MENU_SEARCH = IDashboardHomeUtil.EXPLOREDATA_MENU_SEARCH;
	public static final String EXPLOREDATA_MENU_LOGEXPLORER = IDashboardHomeUtil.EXPLOREDATA_MENU_LOGEXPLORER;
	public static final String EXPLOREDATA_MENU_DATAEXPLORER = IDashboardHomeUtil.EXPLOREDATA_MENU_DATAEXPLORER;
	public static final String DASHBOARDS_GRID_VIEW = IDashboardHomeUtil.DASHBOARDS_GRID_VIEW;
	public static final String DASHBOARDS_LIST_VIEW = IDashboardHomeUtil.DASHBOARDS_LIST_VIEW;
	public static final String DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_ASC = IDashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_ASC;
	public static final String DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_DSC = IDashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_DSC;
	public static final String DASHBOARD_QUERY_ORDER_BY_NAME_ASC = IDashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_NAME_ASC;
	public static final String DASHBOARD_QUERY_ORDER_BY_NAME_DSC = IDashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_NAME_DSC;
	public static final String DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_ASC = IDashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_ASC;
	public static final String DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_DSC = IDashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_DSC;
	public static final String DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_ASC = IDashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_ASC;
	public static final String DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_DSC = IDashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_DSC;
	public static final String DASHBOARD_QUERY_ORDER_BY_OWNER_ASC = IDashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_OWNER_ASC;
	public static final String DASHBOARD_QUERY_ORDER_BY_OWNER_DSC = IDashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_OWNER_DSC;
	public static final String DASHBOARD_QUERY_ORDER_BY_DEFAULT = IDashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_DEFAULT;

	public static void closeOverviewPage(WebDriver driver)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		dhu.closeOverviewPage(driver);
	}

	/**
	 * Create one Dashboard
	 *
	 * @param driver
	 * @param name
	 *            dashboard name
	 * @param descriptions
	 *            dashboard description(optional) @
	 */
	public static void createDashboard(WebDriver driver, String name, String descriptions)
	{
		DashboardHomeUtil.createDashboard(driver, name, descriptions, IDashboardHomeUtil.TYPE_DASHBOARD);
	}

	/**
	 * Create one Dashboard or Dashbaord Set
	 *
	 * @param driver
	 * @param name
	 *            dashboard name
	 * @param descriptions
	 *            dashboard description(optional)
	 * @param type
	 *            dashboard | dashboardSet @
	 */
	public static void createDashboard(WebDriver driver, String name, String descriptions, String type)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		dhu.createDashboard(driver, name, descriptions, type);
	}

	/**
	 * Create one Dashboard Set
	 *
	 * @param driver
	 * @param name
	 *            dashboard set name
	 * @param descriptions
	 *            dashboard set description(optional) @
	 */
	public static void createDashboardSet(WebDriver driver, String name, String descriptions)
	{
		DashboardHomeUtil.createDashboard(driver, name, descriptions, IDashboardHomeUtil.TYPE_DASHBOARDSET);
	}

	/**
	 * Delete one dashboard by name
	 *
	 * @param dashboardName
	 *            dashboard name
	 * @param view
	 *            dashboards_grid_view | dashboards_list_view @
	 */
	public static void deleteDashboard(WebDriver driver, String dashboardName, String view)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		dhu.deleteDashboard(driver, dashboardName, view);
	}

	/**
	 * add filter
	 *
	 * @param driver
	 * @param filter
	 *            filter name - apm,la,ita,orchestration,security,oracle,share,me,favorites(multiple choice and split with comma) @
	 */
	public static void filterOptions(WebDriver driver, String filter)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		dhu.filterOptions(driver, filter);
	}

	/**
	 * goto the link in Data Explorer by displayed name
	 *
	 * @param option
	 *            Analyze | Log | Search @
	 */
	public static void gotoDataExplorer(WebDriver driver, String option)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		dhu.gotoDataExplorer(driver, option);
	}

	/**
	 * choose grid view
	 *
	 * @param driver
	 *            @
	 */
	public static void gridView(WebDriver driver)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		dhu.gridView(driver);
	}

	/**
	 * check if the dashboard is existing or not by name
	 *
	 * @param driver
	 * @param dashboardName
	 * @return @
	 */
	public static boolean isDashboardExisted(WebDriver driver, String dashboardName)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		return dhu.isDashboardExisted(driver, dashboardName);
	}

	/**
	 * check if the filter is selected by filter name
	 *
	 * @param driver
	 * @param filter
	 *            filer name - apm, la , ita, oracle, security, share, me favorites(single choice)
	 * @return @
	 */
	public static boolean isFilterOptionSelected(WebDriver driver, String filter)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		return dhu.isFilterOptionSelected(driver, filter);
	}

	public static List<String> listDashboardNames(WebDriver driver)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		return dhu.listDashboardNames(driver);
	}

	/**
	 * choose the list view
	 *
	 * @param driver
	 *            @
	 */
	public static void listView(WebDriver driver)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		dhu.listView(driver);
	}

	/**
	 * reset the filters
	 *
	 * @param driver
	 *            @
	 */
	public static void resetFilterOptions(WebDriver driver)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		dhu.resetFilterOptions(driver);
	}

	/**
	 * search dashboard
	 *
	 * @param driver
	 * @param searchString
	 *            @
	 */
	public static void search(WebDriver driver, String searchString)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		dhu.search(driver, searchString);
	}

	/**
	 * Select an any-type dashboard by name, including OOB dashboard & user created dashboard
	 *
	 * @param driver
	 * @param dashboardName
	 *            @
	 */
	public static void selectDashboard(WebDriver driver, String dashboardName)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		dhu.selectDashboard(driver, dashboardName);
	}

	/**
	 * Select an OOB dashboard by name
	 *
	 * @param driver
	 * @param dashboardName
	 *            @
	 */
	public static void selectOOB(WebDriver driver, String dashboardName)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		dhu.selectOOB(driver, dashboardName);
	}

	/**
	 * sort dashboards
	 *
	 * @param driver
	 * @param option
	 *            sort by - default, access_date_asc, access_date_dsc, name_asc, name_dsc, creation_date_asc, creation_date_dsc,
	 *            last_modification_date_asc, last_modification_date_dsc, owner_asc, owner_dsc @
	 */
	public static void sortBy(WebDriver driver, String option)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		dhu.sortBy(driver, option);
	}

	/**
	 * @param driver
	 *            @
	 */
	public static void sortListViewByCreateBy(WebDriver driver)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		dhu.sortListViewByCreateBy(driver);
	}

	/**
	 * @param driver
	 *            @
	 */
	public static void sortListViewByLastModified(WebDriver driver)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		dhu.sortListViewByLastModified(driver);
	}

	/**
	 * @param driver
	 *            @
	 */
	public static void sortListViewByName(WebDriver driver)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		dhu.sortListViewByName(driver);
	}

	/**
	 * wait the dashboard by name
	 *
	 * @param driver
	 * @param dashboardName
	 *            @
	 */
	public static void waitForDashboardPresent(WebDriver driver, String dashboardName)
	{
		IDashboardHomeUtil dhu = new UtilLoader<IDashboardHomeUtil>().loadUtil(driver, IDashboardHomeUtil.class);
		dhu.waitForDashboardPresent(driver, dashboardName);
	}

	private DashboardHomeUtil()
	{
	}
}
