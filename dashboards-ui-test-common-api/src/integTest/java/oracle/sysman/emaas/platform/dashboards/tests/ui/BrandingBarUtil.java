package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.UtilLoader;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class BrandingBarUtil
{
	// values for 'option' for user menu
	public static final String USERMENU_OPTION_HELP = IBrandingBarUtil.USERMENU_OPTION_HELP;

	public static final String USERMENU_OPTION_ABOUT = IBrandingBarUtil.USERMENU_OPTION_ABOUT;
	public static final String USERMENU_OPTION_SIGNOUT = IBrandingBarUtil.USERMENU_OPTION_SIGNOUT;
	//Branding bar link text
	//home
	public static final String NAV_LINK_TEXT_HOME_ALERTS = IBrandingBarUtil.NAV_LINK_TEXT_HOME_ALERTS;

	//Cloud Services
	public static final String NAV_LINK_TEXT_CS_ITA = IBrandingBarUtil.NAV_LINK_TEXT_CS_ITA;
	public static final String NAV_LINK_TEXT_CS_APM = IBrandingBarUtil.NAV_LINK_TEXT_CS_APM;
	public static final String NAV_LINK_TEXT_CS_LA = IBrandingBarUtil.NAV_LINK_TEXT_CS_LA;
	public static final String NAV_LINK_TEXT_CS_IM = IBrandingBarUtil.NAV_LINK_TEXT_CS_IM;
	public static final String NAV_LINK_TEXT_CS_OCS = IBrandingBarUtil.NAV_LINK_TEXT_CS_OCS;
	public static final String NAV_LINK_TEXT_CS_SECU = IBrandingBarUtil.NAV_LINK_TEXT_CS_SECU;
	public static final String NAV_LINK_TEXT_CS_COMP = IBrandingBarUtil.NAV_LINK_TEXT_CS_COMP;
	//Visual Analyzers
	public static final String NAV_LINK_TEXT_VA_ITA = IBrandingBarUtil.NAV_LINK_TEXT_VA_ITA;
	public static final String NAV_LINK_TEXT_VA_TA = IBrandingBarUtil.NAV_LINK_TEXT_VA_TA;
	public static final String NAV_LINK_TEXT_VA_LA = IBrandingBarUtil.NAV_LINK_TEXT_VA_LA;
	public static final String NAV_LINK_TEXT_DATAEXPLORER = IBrandingBarUtil.NAV_LINK_TEXT_DATAEXPLORER;
	public static final String NAV_LINK_TEXT_LOGEXPLORER = IBrandingBarUtil.NAV_LINK_TEXT_LOGEXPLORER;
	//Administration
	public static final String NAV_LINK_TEXT_ADMIN_AGENT = IBrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT;
	public static final String NAV_LINK_TEXT_ADMIN_ALERT = IBrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT;
	public static final String NAV_LINK_TEXT_ADMIN_ADMINCONSOLE = IBrandingBarUtil.NAV_LINK_TEXT_ADMIN_ADMINCONSOLE;
	//Hamburger Menu
	public static final String ROOT_MENU_TITLE = IBrandingBarUtil.ROOT_MENU_TITLE;
	public static final String ROOT_MENU_HOME = IBrandingBarUtil.ROOT_MENU_HOME;
	public static final String ROOT_MENU_ALERTS = IBrandingBarUtil.ROOT_MENU_ALERTS;
	public static final String ROOT_MENU_DASHBOARDS = IBrandingBarUtil.ROOT_MENU_DASHBOARDS;
	public static final String ROOT_MENU_DATAEXPLORER = IBrandingBarUtil.ROOT_MENU_DATAEXPLORER;
	public static final String ROOT_MENU_APM = IBrandingBarUtil.ROOT_MENU_APM;
	public static final String ROOT_MENU_MONITORING = IBrandingBarUtil.ROOT_MENU_MONITORING;
	public static final String ROOT_MENU_LA = IBrandingBarUtil.ROOT_MENU_LA;
	public static final String ROOT_MENU_ITA = IBrandingBarUtil.ROOT_MENU_ITA;
	public static final String ROOT_MENU_ORCHESTRATION = IBrandingBarUtil.ROOT_MENU_ORCHESTRATION;
	public static final String ROOT_MENU_SECURITY = IBrandingBarUtil.ROOT_MENU_SECURITY;
	public static final String ROOT_MENU_COMPLIANCE = IBrandingBarUtil.ROOT_MENU_COMPLIANCE;
	public static final String ROOT_MENU_ADMIN = IBrandingBarUtil.ROOT_MENU_ADMIN;
	public static final String GLOBAL_ADMIN_MENU_ALERT_RULES = IBrandingBarUtil.GLOBAL_ADMIN_MENU_ALERT_RULES;
	public static final String GLOBAL_ADMIN_MENU_AGENTS = IBrandingBarUtil.GLOBAL_ADMIN_MENU_AGENTS;
	public static final String GLOBAL_ADMIN_MENU_ENTITIES_CFG = IBrandingBarUtil.GLOBAL_ADMIN_MENU_ENTITIES_CFG;
	public static final String GLOBAL_ADMIN_MENU_NOTIFICATION_CHANNELS = IBrandingBarUtil.GLOBAL_ADMIN_MENU_NOTIFICATION_CHANNELS;
	public static final String GLOBAL_ADMIN_MENU_ADD_ENTITY = IBrandingBarUtil.GLOBAL_ADMIN_MENU_ADD_ENTITY;
	public static final String GLOBAL_ADMIN_MENU_CLOUD_DISCOVERY = IBrandingBarUtil.GLOBAL_ADMIN_MENU_CLOUD_DISCOVERY;

	/**
	 * Click the Hierarchical menu item
	 *
	 * @param driver
	 *            WebDriver instances
	 * @param menuitem
	 *            the menu item want to click, must give the exact string of the menu item
	 */
	public static void clickHierarchicalMenu(WebDriver driver, String menuitem)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.clickHierarchicalMenu(driver, menuitem);
	}

	public static void clickMenuItem(WebDriver driver, String menuitem)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.clickMenuItem(driver, menuitem);
	}

	public static void expandSubMenu(WebDriver driver, String menuitem)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.expandSubMenu(driver, menuitem);
	}

	public static String getCurrentMenuHeader(WebDriver driver)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.getCurrentMenuHeader(driver);
	}

	public static void goBackToParentMenu(WebDriver driver)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.goBackToParentMenu(driver);
	}

	public static boolean hasSubMenu(WebDriver driver, String menuitem)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.hasSubMenu(driver, menuitem);
	}

	/**
	 * Check whether admin field exist
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static boolean isAdmin(WebDriver driver)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isAdmin(driver);
	}

	/**
	 * Check if the specified administration link is existed or not by given name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param adminLinkName
	 *            Name of the link
	 * @return
	 */
	public static boolean isAdminLinkExisted(WebDriver driver, String adminLinkName)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isAdminLinkExisted(driver, adminLinkName);
	}

	/**
	 * Check if the alert link is existed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static boolean isAlertLinkExisted(WebDriver driver)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isAlertsLinkExisted(driver);
	}

	/**
	 * @param driver
	 * @param servicename
	 * @return
	 */
	public static boolean isBrandingBarServiceNamePresent(WebDriver driver, String servicename)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isBrandingBarServiceNamePresent(driver, servicename);
	}

	/**
	 * Check if the specified cloud service link is existed or not by given name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param cloudServiceLinkName
	 *            Name of the link
	 * @return
	 */
	public static boolean isCloudServiceLinkExisted(WebDriver driver, String cloudServiceLinkName)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isCloudServiceLinkExisted(driver, cloudServiceLinkName);
	}

	/**
	 * Check if the dashboard home link is existed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static boolean isDashboardHomeLinkExisted(WebDriver driver)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isDashboardHomeLinkExisted(driver);
	}

	public static boolean isHamburgerMenuDisplayed(WebDriver driver)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isHamburgerMenuDisplayed(driver);
	}

	/**
	 * Check if the specified home link is existed or not by given name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param homeLinkName
	 *            Name of the link
	 * @return
	 */
	public static boolean isHomeLinkExisted(WebDriver driver, String homeLinkName)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isHomeLinkExisted(driver, homeLinkName);
	}

	public static boolean isMenuItemEnabled(WebDriver driver, String menuitem)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isMenuItemEnabled(driver, menuitem);
	}

	public static boolean isMenuItemExisted(WebDriver driver, String menuitem)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isMenuItemExisted(driver, menuitem);
	}

	/**
	 * Check if the favorites link is existed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static boolean isMyFavoritesLinkExisted(WebDriver driver)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isMyFavoritesLinkExisted(driver);
	}

	/**
	 * Check if the Home link is existed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static boolean isMyHomeLinkExisted(WebDriver driver)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isMyHomeLinkExisted(driver);
	}

	/**
	 * Check if the specified visual analyzer link is existed or not by given name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param visualAnalyzerLinkName
	 *            Name of the link
	 * @return
	 */
	public static boolean isVisualAnalyzerLinkExisted(WebDriver driver, String visualAnalyzerLinkName)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isVisualAnalyzerLinkExisted(driver, visualAnalyzerLinkName);
	}

	/**
	 * Check if the welcome link is existed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static boolean isWelcomeLinkExisted(WebDriver driver)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isWelcomeLinkExisted(driver);
	}

	/**
	 * Show or hide hamburger menu
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static boolean toggleHamburgerMenu(WebDriver driver)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.toggleHamburgerMenu(driver);
	}

	/**
	 * Do the specified user option by the given option name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param option
	 *            Name of the option, should one of "about", "help", "signout"
	 * @return
	 */
	public static void userMenuOptions(WebDriver driver, String option)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.userMenuOptions(driver, option);
	}

	public static void verifyUserMenuItemByTest(WebDriver driver, String item, String text){
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.verifyUserMenuItemByText(driver, item, text);
	}
	/**
	 * Navigate to the specified administration page by given link name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param adminLinkName
	 *            Name of the link
	 * @return
	 */
	public static void visitApplicationAdministration(WebDriver driver, String adminLinkName)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.visitApplicationAdministration(driver, adminLinkName);
	}

	/**
	 * Navigate to the specified cloud service page by given link name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param cloudServiceLinkName
	 *            Name of the link
	 * @return
	 */
	public static void visitApplicationCloudService(WebDriver driver, String cloudServiceLinkName)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.visitApplicationCloudService(driver, cloudServiceLinkName);
	}

	public static void visitApplicationHome(WebDriver driver, String homeLinkName)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.visitApplicationHome(driver, homeLinkName);
	}

	/**
	 * Navigate to the specified visual analyzer page by given link name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param visualAnalyzerLinkName
	 *            Name of the link
	 * @return
	 */
	public static void visitApplicationVisualAnalyzer(WebDriver driver, String visualAnalyzerLinkName)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.visitApplicationVisualAnalyzer(driver, visualAnalyzerLinkName);
	}

	/**
	 * Navigate to the dashboard home page
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static void visitDashboardHome(WebDriver driver)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.visitDashboardHome(driver);
	}

	/**
	 * Navigate to My Favorites page
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static void visitMyFavorites(WebDriver driver)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.visitMyFavorites(driver);
	}

	/**
	 * Navigate to my home page
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static void visitMyHome(WebDriver driver)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.visitMyHome(driver);
	}

	/**
	 * Navigate to the welcome page
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static void visitWelcome(WebDriver driver)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.visitWelcome(driver);
	}

	private BrandingBarUtil()
	{
	}

}
