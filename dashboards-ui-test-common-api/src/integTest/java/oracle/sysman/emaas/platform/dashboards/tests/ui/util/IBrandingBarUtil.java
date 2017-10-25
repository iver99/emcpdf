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

public interface IBrandingBarUtil extends IUiTestCommonAPI
{
	// values for 'option' for user menu
	public static final String USERMENU_OPTION_HELP = "help";
	public static final String USERMENU_OPTION_ABOUT = "about";
	public static final String USERMENU_OPTION_SIGNOUT = "signout";

	//Branding bar link text

	// home
	public static final String NAV_LINK_TEXT_HOME_ALERTS = "Alerts";
	//Cloud Services
	public static final String NAV_LINK_TEXT_CS_ITA = "IT Analytics";
	public static final String NAV_LINK_TEXT_CS_APM = "APM";
	public static final String NAV_LINK_TEXT_CS_LA = "Log Analytics";
	public static final String NAV_LINK_TEXT_CS_IM = "Infrastructure Monitoring";
	public static final String NAV_LINK_TEXT_CS_OCS = "Orchestration";
	public static final String NAV_LINK_TEXT_CS_SECU = "Security Monitoring and Analytics";
	public static final String NAV_LINK_TEXT_CS_COMP = "Configuration and Compliance";
	//Visual Analyzers, new name is Explorers
	public static final String NAV_LINK_TEXT_VA_ITA = "Analyze";
	public static final String NAV_LINK_TEXT_VA_TA = "Search";
	public static final String NAV_LINK_TEXT_VA_LA = "Log";
	public static final String NAV_LINK_TEXT_DATAEXPLORER = "Data Explorer";
	public static final String NAV_LINK_TEXT_LOGEXPLORER = "Log Explorer";

	//Administration
	public static final String NAV_LINK_TEXT_ADMIN_AGENT = "Agents";
	public static final String NAV_LINK_TEXT_ADMIN_ALERT = "Alert Rules";
	public static final String NAV_LINK_TEXT_ADMIN_ADMINCONSOLE = "Administration";

	//Hamburger menu items
	public static final String ROOT_MENU_TITLE = "Management Cloud";
	public static final String ROOT_MENU_HOME = "Home";
	public static final String ROOT_MENU_ALERTS = "Alerts";
	public static final String ROOT_MENU_DASHBOARDS = "Dashboards";
	public static final String ROOT_MENU_DATAEXPLORER = "Data Explorer";
	public static final String ROOT_MENU_APM = "APM";
	public static final String ROOT_MENU_MONITORING = "Monitoring";
	public static final String ROOT_MENU_LA = "Log Analytics";
	public static final String ROOT_MENU_ITA = "IT Analytics";
	public static final String ROOT_MENU_ORCHESTRATION = "Orchestration";
	public static final String ROOT_MENU_SECURITY = "Security Analytics";
	public static final String ROOT_MENU_COMPLIANCE = "Compliance";
	public static final String ROOT_MENU_ADMIN = "Administration";
	public static final String GLOBAL_ADMIN_MENU_ALERT_RULES = "Alert Rules";
	public static final String GLOBAL_ADMIN_MENU_AGENTS = "Agents";
	public static final String GLOBAL_ADMIN_MENU_ENTITIES_CFG = "Entities Configuration";
	public static final String GLOBAL_ADMIN_MENU_NOTIFICATION_CHANNELS = "Notification Channels";
	public static final String GLOBAL_ADMIN_MENU_ADD_ENTITY = "Add Entity";
	public static final String GLOBAL_ADMIN_MENU_CLOUD_DISCOVERY = "Cloud Discovery Profiles";

	public void clickHierarchicalMenu(WebDriver driver, String menuitem);

	public void clickMenuItem(WebDriver driver, String menuitem);

	/**
	 * @param driver
	 * @param menuitem
	 * @return
	 */
	public void expandSubMenu(WebDriver driver, String menuitem);

	public String getCurrentMenuHeader(WebDriver driver);

	public void goBackToParentMenu(WebDriver driver);

	/**
	 * Check if the menuitem if has sub-menu
	 * 
	 * @param driver
	 * @param menuitem
	 * @return
	 */
	public boolean hasSubMenu(WebDriver driver, String menuitem);

	/**
	 * Check whether admin field exist
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean isAdmin(WebDriver driver);

	public boolean isAdminLinkExisted(WebDriver driver, String adminLinkName);

	/**
	 * Check if the Alerts link is existed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean isAlertsLinkExisted(WebDriver driver);

	/**
	 * @param driver
	 * @param servicename
	 * @return
	 */
	public boolean isBrandingBarServiceNamePresent(WebDriver driver, String servicename);

	/**
	 * Check if the specified cloud service link is existed or not by given name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param cloudServiceLinkName
	 *            Name of the link
	 * @return
	 */
	public boolean isCloudServiceLinkExisted(WebDriver driver, String cloudServiceLinkName);

	/**
	 * Check if the dashboard home link is existed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean isDashboardHomeLinkExisted(WebDriver driver);

	public boolean isHamburgerMenuDisplayed(WebDriver driver);

	/**
	 * Check if the specified home link is existed or not by given name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param homeLinkName
	 *            Name of the link
	 * @return
	 */
	public boolean isHomeLinkExisted(WebDriver driver, String homeLinkName);

	public boolean isMenuItemEnabled(WebDriver driver, String menuitem);

	public boolean isMenuItemExisted(WebDriver driver, String menuitem);

	/**
	 * Check if the favorites link is existed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean isMyFavoritesLinkExisted(WebDriver driver);

	/**
	 * Check if the Home link is existed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean isMyHomeLinkExisted(WebDriver driver);

	/**
	 * Check if the specified visual analyzer link is existed or not by given name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param visualAnalyzerLinkName
	 *            Name of the link
	 * @return
	 */
	public boolean isVisualAnalyzerLinkExisted(WebDriver driver, String visualAnalyzerLinkName);

	/**
	 * Check if the welcome link is existed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean isWelcomeLinkExisted(WebDriver driver);

	/**
	 * Show or hide hamburger menu
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean toggleHamburgerMenu(WebDriver driver);

	/**
	 * Do the specified user option by the given option name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param option
	 *            Name of the option, should one of "about", "help", "signout"
	 * @return
	 */
	public void userMenuOptions(WebDriver driver, String option);

	public void verifyUserMenuItemByText(WebDriver driver, String item, String text);
	/**
	 * Navigate to the specified administration page by given link name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param adminLinkName
	 *            Name of the link
	 * @return
	 */
	public void visitApplicationAdministration(WebDriver driver, String adminLinkName);

	/**
	 * Navigate to the specified cloud service page by given link name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param cloudServiceLinkName
	 *            Name of the link
	 * @return
	 */
	public void visitApplicationCloudService(WebDriver driver, String cloudServiceLinkName);

	public void visitApplicationHome(WebDriver driver, String homeLinkName);

	/**
	 * Navigate to the specified visual analyzer page by given link name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param visualAnalyzerLinkName
	 *            Name of the link
	 * @return
	 */
	public void visitApplicationVisualAnalyzer(WebDriver driver, String visualAnalyzerLinkName);

	/**
	 * Navigate to the dashboard home page
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public void visitDashboardHome(WebDriver driver);

	/**
	 * Navigate to My Favorites page
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public void visitMyFavorites(WebDriver driver);

	/**
	 * Navigate to my home page
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public void visitMyHome(WebDriver driver);

	/**
	 * Navigate to the welcome page
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public void visitWelcome(WebDriver driver);
}