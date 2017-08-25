package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.UtilLoader;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class DashboardBuilderUtil
{
	public static final String REFRESH_DASHBOARD_SETTINGS_OFF = IDashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF;

	public static final String REFRESH_DASHBOARD_SETTINGS_5MIN = IDashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_5MIN;
	public static final String TILE_WIDER = IDashboardBuilderUtil.TILE_WIDER;
	public static final String TILE_NARROWER = IDashboardBuilderUtil.TILE_NARROWER;
	public static final String TILE_TALLER = IDashboardBuilderUtil.TILE_TALLER;
	public static final String TILE_SHORTER = IDashboardBuilderUtil.TILE_SHORTER;
	public static final String TILE_UP = IDashboardBuilderUtil.TILE_UP;
	public static final String TILE_DOWN = IDashboardBuilderUtil.TILE_DOWN;
	public static final String TILE_LEFT = IDashboardBuilderUtil.TILE_LEFT;
	public static final String TILE_RIGHT = IDashboardBuilderUtil.TILE_RIGHT;

	public static void addNewDashboardToSet(WebDriver driver, String dashboardName)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.addNewDashboardToSet(driver, dashboardName);
	}
	
	public static void addTextWidgetToDashboard(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.addTextWidgetToDashboard(driver);
	}

	public static void addImageInTextWidget(WebDriver driver, int index, String url, String alternativeText)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.addImageInTextWidget(driver, index, url, alternativeText);
	}
	
	public static void addWidgetToDashboard(WebDriver driver, String searchString)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.addWidgetToDashboard(driver, searchString);
	}

	public static void createDashboardInsideSet(WebDriver driver, String name, String descriptions)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.createDashboardInsideSet(driver, name, descriptions);
	}

	public static void deleteDashboard(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.deleteDashboard(driver);
	}

	public static void deleteDashboardInsideSet(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.deleteDashboardInsideSet(driver);
	}

	public static void deleteDashboardSet(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.deleteDashboardSet(driver);
	}

	public static void duplicateDashboard(WebDriver driver, String name, String descriptions)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.duplicateDashboard(driver, name, descriptions);
	}

	public static void duplicateDashboardInsideSet(WebDriver driver, String name, String descriptions, boolean addToSet)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.duplicateDashboardInsideSet(driver, name, descriptions, addToSet);
	}

	public static void editDashboard(WebDriver driver, String name, String descriptions)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.editDashboard(driver, name, descriptions);
	}

	public static void editDashboard(WebDriver driver, String name, String descriptions, Boolean showDesc)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.editDashboard(driver, name, descriptions, showDesc);
	}

	public static void editDashboardSet(WebDriver driver, String name, String descriptions)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.editDashboardSet(driver, name, descriptions);
	}
	
	public static void editTextWidgetAddContent(WebDriver driver, int index, String content)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.editTextWidgetAddContent(driver, index, content);
	}

	public static void addLinkInTextWidget(WebDriver driver, int index, String url, String option)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.addLinkInTextWidget(driver, index, url, option);
	}
	

	public static Boolean favoriteOption(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.favoriteOption(driver);
	}

	public static Boolean favoriteOptionDashboardSet(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.favoriteOptionDashboardSet(driver);
	}

	public static void gridView(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.gridView(driver);
	}

	public static boolean isRefreshSettingChecked(WebDriver driver, String refreshSettings)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.isRefreshSettingChecked(driver, refreshSettings);
	}

	public static boolean isRefreshSettingCheckedForDashbaordSet(WebDriver driver, String refreshSettings)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.isRefreshSettingCheckedForDashbaordSet(driver, refreshSettings);
	}

	public static void listView(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.listView(driver);
	}

	public static void maximizeWidget(WebDriver driver, String widgetName, int index)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.maximizeWidget(driver, widgetName, index);
	}

	public static void moveWidget(WebDriver driver, String widgetName, int index, String moveOption)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.moveWidget(driver, widgetName, index, moveOption);
	}

	public static void moveWidget(WebDriver driver, String widgetName, String moveOption)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.moveWidget(driver, widgetName, moveOption);
	}

	public static void openWidget(WebDriver driver, String widgetName)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.openWidget(driver, widgetName);
	}

	public static void openWidget(WebDriver driver, String widgetName, int index)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.openWidget(driver, widgetName, index);
	}

	public static void printDashboard(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.printDashboard(driver);
	}

	public static void printDashboardSet(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.printDashboardSet(driver);
	}

	public static void refreshDashboard(WebDriver driver, String refreshSettings)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.refreshDashboard(driver, refreshSettings);
	}

	public static void refreshDashboardSet(WebDriver driver, String refreshSettings)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.refreshDashboardSet(driver, refreshSettings);
	}

	public static void removeDashboardFromSet(WebDriver driver, String dashboardName)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.removeDashboardFromSet(driver, dashboardName);
	}

	public static void removeWidget(WebDriver driver, String widgetName)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.removeWidget(driver, widgetName);
	}

	public static void removeWidget(WebDriver driver, String widgetName, int index)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.removeWidget(driver, widgetName, index);
	}

	public static void resizeWidget(WebDriver driver, String widgetName, int index, String resizeOptions)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.resizeWidget(driver, widgetName, index, resizeOptions);
	}

	public static void resizeWidget(WebDriver driver, String widgetName, String resizeOptions)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.resizeWidget(driver, widgetName, resizeOptions);
	}

	public static boolean respectGCForEntity(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.respectGCForEntity(driver);
	}

	public static boolean respectGCForTimeRange(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.respectGCForTimeRange(driver);
	}

	public static void restoreWidget(WebDriver driver, String widgetName, int index)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.restoreWidget(driver, widgetName, index);
	}

	public static void saveDashboard(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.saveDashboard(driver);
	}

	public static void search(WebDriver driver, String searchString)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.search(driver, searchString);
	}

	public static void selectDashboard(WebDriver driver, String dashboardName)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.selectDashboard(driver, dashboardName);
	}

	public static void selectDashboardInsideSet(WebDriver driver, String dashboardName)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.selectDashboardInsideSet(driver, dashboardName);
	}

	public static void setEntitySupport(WebDriver driver, String mode)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.setEntitySupport(driver, mode);
	}

	public static boolean showEntityFilter(WebDriver driver, boolean showEntityFilter)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.showEntityFilter(driver, showEntityFilter);
	}

	public static boolean showTimeRangeFilter(WebDriver driver, boolean showTimeRangeFilter)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.showTimeRangeFilter(driver, showTimeRangeFilter);
	}

	public static void showWidgetTitle(WebDriver driver, String widgetName, boolean visibility)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.showWidgetTitle(driver, widgetName, visibility);
	}

	public static void showWidgetTitle(WebDriver driver, String widgetName, int index, boolean visibility)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.showWidgetTitle(driver, widgetName, index, visibility);
	}

	public static void addLinkToWidgetTitle(WebDriver driver, String widgetName, String dashboardName)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.addLinkToWidgetTitle(driver, widgetName, dashboardName);
	}

	public static void addLinkToWidgetTitle(WebDriver driver, String widgetName, int index, String dashboardName)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.addLinkToWidgetTitle(driver, widgetName, index, dashboardName);
	}

	public static boolean verifyLinkOnWidgetTitle(WebDriver driver, String widgetName, String dashboardName)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.verifyLinkOnWidgetTitle(driver, widgetName, dashboardName);
	}

	public static boolean verifyLinkOnWidgetTitle(WebDriver driver, String widgetName, int index, String dashboardName) {
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.verifyLinkOnWidgetTitle(driver, widgetName, index, dashboardName);
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
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.sortBy(driver, option);
	}

	public static Boolean toggleHome(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.toggleHome(driver);
	}

	public static Boolean toggleHomeDashboardSet(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.toggleHomeDashboardSet(driver);
	}

	public static Boolean toggleShareDashboard(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.toggleShareDashboard(driver);
	}

	public static Boolean toggleShareDashboardset(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.toggleShareDashboardset(driver);
	}

	public static boolean verifyDashboard(WebDriver driver, String dashboardName, String description, boolean showTimeSelector)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.verifyDashboard(driver, dashboardName, description, showTimeSelector);
	}

	public static boolean verifyDashboardInsideSet(WebDriver driver, String dashboardName)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.verifyDashboardInsideSet(driver, dashboardName);
	}

	public static boolean verifyDashboardSet(WebDriver driver, String dashboardSetName)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.verifyDashboardSet(driver, dashboardSetName);
	}

	public static boolean verifyWidget(WebDriver driver, String widgetName)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.verifyWidget(driver, widgetName);
	}

	public static boolean verifyWidget(WebDriver driver, String widgetName, int index)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.verifyWidget(driver, widgetName, index);
	}
	
	public static void clickLinkOnWidgetTitle(WebDriver driver, String widgetName)
	{		
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.clickLinkOnWidgetTitle(driver, widgetName);
	}

	/**
	 * @param driver
	 * @param widgetName
	 * @param index
	 * @return
	 */
	public static void clickLinkOnWidgetTitle(WebDriver driver, String widgetName, int index)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.clickLinkOnWidgetTitle(driver, widgetName, index);	
		
	}

	public static boolean hasWidgetLink(WebDriver driver, String widgetName)
	{		
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.hasWidgetLink(driver, widgetName, 0);
	}

	/**
	 * @param driver
	 * @param widgetName
	 * @param index
	 * @return
	 */
	public static boolean hasWidgetLink(WebDriver driver, String widgetName, int index)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.hasWidgetLink(driver, widgetName, index);	
		
	}
	private DashboardBuilderUtil()
	{
	}
}
