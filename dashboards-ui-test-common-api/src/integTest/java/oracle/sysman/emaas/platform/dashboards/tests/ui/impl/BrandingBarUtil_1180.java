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

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1180;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class BrandingBarUtil_1180 extends BrandingBarUtil_1170
{
	@Override
	public void clickHierarchicalMenu(WebDriver driver, String menuitem)
	{
		boolean isExisted = false;
		Validator.notEmptyString("menuitem in [clickHierarchicalMenu]", menuitem);

		driver.waitForElementPresent("css=" + DashBoardPageId_1180.HAMBURGERMENU_CONTAINER_CSS);
		if (!isHamburgerMenuDisplayed(driver)) {
			driver.getLogger().info("Not displayed hamburger menu, need to show it");
			clickHamburgerMenuIcon(driver);
		}

		driver.getLogger().info("check if HierarchicalMenu existed or not");
		if (driver.isDisplayed("css=" + DashBoardPageId_1180.HAMBURGERMENU_HIERARCHICALBTN_CSS)) {
			driver.click("css=" + DashBoardPageId_1180.HAMBURGERMENU_HIERARCHICALBTN_CSS);
		}
		else {
			throw new NoSuchElementException("clickHierarchicalMenu: the Hierarchical Menu is not found");
		}

		driver.getLogger().info("Start to click menu itme in clickHierarchicalMenu");

		List<WebElement> webd_menuitem = driver.getWebDriver().findElements(
				By.cssSelector(DashBoardPageId_1180.HAMBURGERMENU_HIERARCHICALMENUITEM_CSS));
		if (webd_menuitem == null || webd_menuitem.isEmpty()) {
			throw new NoSuchElementException("clickHierarchicalMenu: the Hierarchical menuitem is not found");
		}
		for (WebElement nav : webd_menuitem) {
			if (nav.getText().trim().equals(menuitem) && nav.isEnabled()) {
				isExisted = true;
				nav.click();
				WaitUtil.waitForPageFullyLoaded(driver);
				driver.takeScreenShot();
				driver.savePageToFile();
				driver.getLogger().info("clickHierarchicalMenu has click on the given menu item: " + menuitem);
				break;
			}
		}
		if (!isExisted) {
			throw new NoSuchElementException("clickHierarchicalMenu: the Hierarchical menuitem '" + menuitem + "'is not found");
		}

	}

	@Override
	public void clickMenuItem(WebDriver driver, String menuitem)
	{
		boolean isExisted = false;
		Validator.notEmptyString("menuitem in [clickMenuItem]", menuitem);

		driver.waitForElementPresent("css=" + DashBoardPageId_1180.HAMBURGERMENU_CONTAINER_CSS);
		if (!isHamburgerMenuDisplayed(driver)) {
			driver.getLogger().info("Not displayed hamburger menu, need to show it");
			clickHamburgerMenuIcon(driver);
		}

		List<WebElement> webd_menuitem = driver.getWebDriver().findElements(
				By.cssSelector(DashBoardPageId_1180.HAMBURGERMENU_MENUITEM_LABEL_CSS));
		if (webd_menuitem == null || webd_menuitem.isEmpty()) {
			throw new NoSuchElementException("clickMenuItem: the menuitem element is not found");
		}
		for (WebElement nav : webd_menuitem) {
			if (nav.getText().trim().equals(menuitem) && nav.isDisplayed() && isElementEnabled(driver, nav)) {
				isExisted = true;
				boolean viewportScreenshot = false;
				try {
					nav.click();
				}
				catch (Exception e) {
					viewportScreenshot = true;
					throw e;
				}
				finally {
					driver.capturePageState(viewportScreenshot);
				}

				WaitUtil.waitForPageFullyLoaded(driver);
				driver.getLogger().info("clickMenuItem has click on the given menu item: " + menuitem);
				break;
			}
		}
		if (!isExisted) {
			throw new NoSuchElementException("clickMenuItem: the menuitem '" + menuitem + "' is not found");
		}
	}

	@Override
	public void expandSubMenu(WebDriver driver, String menuitem)
	{
		boolean isExisted = false;
		Validator.notEmptyString("menuitem in [expandSubMenu]", menuitem);

		driver.waitForElementPresent("css=" + DashBoardPageId_1180.HAMBURGERMENU_CONTAINER_CSS);
		if (!isHamburgerMenuDisplayed(driver)) {
			driver.getLogger().info("Not displayed hamburger menu, need to show it");
			clickHamburgerMenuIcon(driver);
		}

		List<WebElement> webd_menuitem = driver.getWebDriver().findElements(
				By.cssSelector(DashBoardPageId_1180.HAMBURGERMENU_MENUITEM_LABEL_CSS));
		if (webd_menuitem == null || webd_menuitem.isEmpty()) {
			throw new NoSuchElementException("clickMenuItem: the menuitem element is not found");
		}
		for (WebElement nav : webd_menuitem) {
			if (nav.getText().trim().equals(menuitem) && nav.isDisplayed() && isElementEnabled(driver, nav)) {
				isExisted = true;
				driver.getLogger().info("Find the expand sub menu icon of the " + menuitem);
				WebElement expandSubMenuIcon = nav.findElement(By.xpath("../..")).findElement(
						By.cssSelector(DashBoardPageId_1180.HAMBURGERMENU_EXPAND_SUBMENU_ICON_CSS));
				expandSubMenuIcon.click();
				break;
			}
		}
		if (!isExisted) {
			throw new NoSuchElementException("expandSubMenu: the menuitem '" + menuitem + "' is not found");
		}
	}

	@Override
	public String getCurrentMenuHeader(WebDriver driver)
	{
		if (!isHamburgerMenuDisplayed(driver)) {
			driver.click("css=" + DashBoardPageId_1180.HAMBURGERMENU_ICON_CSS);

		}
		return driver.getText("css=" + DashBoardPageId_1180.HAMBURGERMENU_CURRENTHEADER_CSS);
	}

	@Override
	public void goBackToParentMenu(WebDriver driver)
	{
		if (!isHamburgerMenuDisplayed(driver)) {
			driver.click("css=" + DashBoardPageId_1180.HAMBURGERMENU_ICON_CSS);

		}

		if (driver.isDisplayed("css=" + DashBoardPageId_1180.HAMBURGERMENU_PREVIOUSICON_CSS)) {
			driver.click("css=" + DashBoardPageId_1180.HAMBURGERMENU_PREVIOUSICON_CSS);

		}
		else {
			throw new NoSuchElementException("goBackToParentMenu: 'goBackToParentMenu'icon is not found");
		}
	}

	@Override
	public boolean hasSubMenu(WebDriver driver, String menuitem)
	{
		boolean isExisted = false;
		Validator.notEmptyString("menuitem in [hasSubMenu]", menuitem);

		driver.waitForElementPresent("css=" + DashBoardPageId_1180.HAMBURGERMENU_CONTAINER_CSS);
		if (!isHamburgerMenuDisplayed(driver)) {
			driver.getLogger().info("Not displayed hamburger menu, need to show it");
			clickHamburgerMenuIcon(driver);
		}

		List<WebElement> webd_menuitem = driver.getWebDriver().findElements(
				By.cssSelector(DashBoardPageId_1180.HAMBURGERMENU_MENUITEM_LABEL_CSS));
		if (webd_menuitem == null || webd_menuitem.isEmpty()) {
			throw new NoSuchElementException("clickMenuItem: the menuitem element is not found");
		}
		for (WebElement nav : webd_menuitem) {
			if (nav.getText().trim().equals(menuitem) && nav.isDisplayed() && isElementEnabled(driver, nav)) {
				driver.getLogger().info("Start to find the expand sub menu icon of the " + menuitem);
				try {
					nav.findElement(By.xpath("../..")).findElement(
							By.cssSelector(DashBoardPageId_1180.HAMBURGERMENU_EXPAND_SUBMENU_ICON_CSS));
					isExisted = true;
					driver.getLogger().info("'" + menuitem + "' has sub menu icon");
					return isExisted;
				}
				catch (NoSuchElementException ex) {
					driver.getLogger().info("'" + menuitem + "' doesn't have sub menu icon");
					return isExisted;
				}
			}
		}
		driver.getLogger().info("'" + menuitem + "' not existed or disabled");
		return isExisted;
	}

	@Override
	public boolean isAdmin(WebDriver driver)
	{
		boolean isDisplayed = false;
		driver.getLogger().info("isAdmin started");
		//check if hamburger menu icon exist
		if (isHamburgerMenuEnabled(driver)) {
			throw new NoSuchElementException("This Method is not supported in Hamburger Menu.");
		}
		else {
			//branding bar
			driver.getLogger().info("start validating admin tab");
			//Open the navigation bar if it's not displayed
			openNavigationBar(driver);
			isDisplayed = driver.isDisplayed("id=" + DashBoardPageId.BRANDINGBARADMINLINKSID);
		}
		driver.getLogger().info("isAdmin ended");
		return isDisplayed;
	}

	@Override
	public boolean isAdminLinkExisted(WebDriver driver, String adminLinkName)
	{
		boolean isExisted = false;
		Validator.notEmptyString("adminLinkName in [isAdminLinkExisted]", adminLinkName);
		driver.getLogger().info("isAdminLinkExisted started");
		//check if hamburger menu icon exist
		if (isHamburgerMenuEnabled(driver)) {
			if (NAV_LINK_TEXT_ADMIN_ADMINCONSOLE.equals(adminLinkName)) {
				expandSubMenu(driver, ROOT_MENU_ADMIN);
				isExisted = isMenuItemExisted(driver, GLOBAL_ADMIN_MENU_ENTITIES_CFG);
			}
			else if (NAV_LINK_TEXT_ADMIN_ALERT.equals(adminLinkName)) {
				expandSubMenu(driver, ROOT_MENU_ADMIN);
				isExisted = isMenuItemExisted(driver, GLOBAL_ADMIN_MENU_ALERT_RULES);
			}
			else if (NAV_LINK_TEXT_ADMIN_AGENT.equals(adminLinkName)) {
				expandSubMenu(driver, ROOT_MENU_ADMIN);
				isExisted = isMenuItemExisted(driver, GLOBAL_ADMIN_MENU_AGENTS);
			}
			else {
				throw new NoSuchElementException("This '" + adminLinkName + "' is not supported in Hamburger Menu.");
			}
		}
		else {
			//branding bar
			driver.getLogger().info("Start to check if admin link is existed in navigation bar. Link name: " + adminLinkName);
			isExisted = isApplicationLinkExisted(driver, "admin", adminLinkName);
		}
		driver.getLogger().info("Existence check for admin link is completed. Result: " + isExisted);
		driver.getLogger().info("isAdminLinkExisted ended");
		return isExisted;
	}

	@Override
	public boolean isAlertsLinkExisted(WebDriver driver)
	{
		boolean isExisted = false;
		driver.getLogger().info("isAlertsLinkExisted(WebDriver) started");
		//check if hamburger menu icon exist
		if (isHamburgerMenuEnabled(driver)) {
			driver.getLogger().info("Start to check if 'Alert' menu is existed in hamburger menu.");
			isExisted = isMenuItemExisted(driver, ROOT_MENU_ALERTS);
		}
		else {
			//branding bar
			driver.getLogger().info("Start to check if 'Alert' link is existed in navigation bar.");
			isExisted = isApplicationLinkExisted(driver, "home", BrandingBarUtil.NAV_LINK_TEXT_HOME_ALERTS);
		}
		driver.getLogger().info("Existence check for 'Alert' link is completed. Result: " + isExisted);
		driver.getLogger().info("isAlertsLinkExisted(WebDriver) ended");
		return isExisted;
	}

	@Override
	public boolean isCloudServiceLinkExisted(WebDriver driver, String cloudServiceLinkName)
	{
		boolean isExisted = false;
		String HBGMenuItem = "";
		Validator.notEmptyString("cloudServiceLinkName in [isCloudServiceLinkExisted]", cloudServiceLinkName);
		driver.getLogger().info("isCloudServiceLinkExisted started");
		//check if hamburger menu icon exist
		if (isHamburgerMenuEnabled(driver)) {
			HBGMenuItem = cloudServiceLinkToHamburgerMenuItem(driver, cloudServiceLinkName);
			driver.getLogger().info("Use NEW menu name: " + HBGMenuItem);

			driver.getLogger().info(
					"Start to check if cloud service link is existed in hamburger menu. Link name: " + HBGMenuItem);

			isExisted = isMenuItemEnabled(driver, HBGMenuItem);
		}
		else {
			//branding bar
			driver.getLogger().info(
					"Start to check if cloud service link is existed in navigation bar. Link name: " + cloudServiceLinkName);

			isExisted = isApplicationLinkExisted(driver, "cs", cloudServiceLinkName);
		}
		driver.getLogger().info("Existence check for cloud service link is completed. Result: " + isExisted);
		driver.getLogger().info("isCloudServiceLinkExisted ended");
		return isExisted;
	}

	@Override
	public boolean isHamburgerMenuDisplayed(WebDriver driver)
	{
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("css=" + DashBoardPageId_1180.HAMBURGERMENU_CONTAINER_CSS);
		driver.takeScreenShot();
		driver.savePageToFile();
		if (driver.isDisplayed("css=" + DashBoardPageId_1180.HAMBURGERMENU_CONTAINER_CSS)) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean isHomeLinkExisted(WebDriver driver, String homeLinkName)
	{
		boolean isExisted = false;
		Validator.notEmptyString("homeLinkName in [isHomeLinkExisted]", homeLinkName);
		driver.getLogger().info("isHomeLinkExisted started");
		//check if hamburger menu icon exist
		if (isHamburgerMenuEnabled(driver)) {
			if (NAV_LINK_TEXT_HOME_ALERTS.equals(homeLinkName) || "Home".equals(homeLinkName)) {
				isExisted = isMenuItemExisted(driver, homeLinkName);
			}
			else {
				driver.getLogger().info("'" + homeLinkName + "' can not be found in hamburger menu");
				throw new NoSuchElementException("The '" + homeLinkName + "' not supported in Hamburger Menu.");
			}
		}
		else {
			//branding bar
			driver.getLogger().info("Start to check if home link is existed in navigation bar. Link name: " + homeLinkName);
			isExisted = isApplicationLinkExisted(driver, "home", homeLinkName);
		}
		driver.getLogger().info("Existence check for home link is completed. Result: " + isExisted);
		driver.getLogger().info("isHomeLinkExisted ended");
		return isExisted;
	}

	@Override
	public boolean isMenuItemEnabled(WebDriver driver, String menuitem)
	{
		boolean isEnabled = false;
		Validator.notEmptyString("menuitem in [isMenuItemEnabled]", menuitem);

		driver.waitForElementPresent("css=" + DashBoardPageId_1180.HAMBURGERMENU_CONTAINER_CSS);
		if (!isHamburgerMenuDisplayed(driver)) {
			driver.getLogger().info("Not displayed hamburger menu, need to show it");
			clickHamburgerMenuIcon(driver);
		}

		List<WebElement> webd_menuitem = driver.getWebDriver().findElements(
				By.cssSelector(DashBoardPageId_1180.HAMBURGERMENU_MENUITEM_LABEL_CSS));
		if (webd_menuitem == null || webd_menuitem.isEmpty()) {
			throw new NoSuchElementException("isMenuItemEnabled: the menuitem element is not found");
		}
		driver.getLogger().info("Start to check if menu item is enabled in hamburger menu. Menu name: " + menuitem);
		for (WebElement nav : webd_menuitem) {
			if (nav.getText().trim().equals(menuitem) && nav.isDisplayed() && isElementEnabled(driver, nav)) {
				driver.getLogger().info("isMenuItemEnabled has found the given menu item: " + menuitem);
				isEnabled = true;
				break;
			}
		}
		driver.getLogger().info("Eanble check for menu item s completed. Result: " + isEnabled);
		return isEnabled;
	}

	@Override
	public boolean isMenuItemExisted(WebDriver driver, String menuitem)
	{
		boolean isExisted = false;
		Validator.notEmptyString("menuitem in [isMenuItemExisted]", menuitem);

		driver.waitForElementPresent("css=" + DashBoardPageId_1180.HAMBURGERMENU_CONTAINER_CSS);
		if (!isHamburgerMenuDisplayed(driver)) {
			driver.getLogger().info("Not displayed hamburger menu, need to show it");
			clickHamburgerMenuIcon(driver);
		}

		List<WebElement> webd_menuitem = driver.getWebDriver().findElements(
				By.cssSelector(DashBoardPageId_1180.HAMBURGERMENU_MENUITEM_LABEL_CSS));
		if (webd_menuitem == null || webd_menuitem.isEmpty()) {
			throw new NoSuchElementException("isMenuItemExisted: the menuitem element is not found");
		}
		driver.getLogger().info("Start to check if menu item is existed in hamburger menu. Menu name: " + menuitem);
		for (WebElement nav : webd_menuitem) {
			if (nav.getText().trim().equals(menuitem) && nav.isDisplayed()) {
				driver.getLogger().info("isMenuItemExisted has found the given menu item: " + menuitem);
				isExisted = true;
				break;
			}
		}
		driver.getLogger().info("Existence check for menu item s completed. Result: " + isExisted);
		return isExisted;
	}

	@Override
	public boolean isMyFavoritesLinkExisted(WebDriver driver)
	{
		boolean isExisted = false;
		driver.getLogger().info("isMyFavoritesLinkExisted started");
		//check if hamburger menu icon exist
		if (isHamburgerMenuEnabled(driver)) {
			driver.getLogger().info("'My Favorites' menu is not existed in hamburger menu");
			isExisted = false;
		}
		else {
			//branding bar
			driver.getLogger().info("Start to check if 'My Favorites' link is existed in navigation bar.");
			String locator = "id=" + DashBoardPageId.BRANDINGBARMYFAVORITESLINKID;
			isExisted = isApplicationLinkExisted(driver, locator);
			driver.getLogger().info("Existence check for 'My Favorites' link is completed. Result: " + isExisted);
		}
		driver.getLogger().info("isMyFavoritesLinkExisted ended");
		return isExisted;
	}

	@Override
	public boolean isMyHomeLinkExisted(WebDriver driver)
	{
		boolean isExisted = false;
		driver.getLogger().info("isMyHomeLinkExisted started");
		//check if hamburger menu icon exist
		if (isHamburgerMenuEnabled(driver)) {
			driver.getLogger().info("Start to check if 'Home' menu is existed in hamburger menu.");
			isExisted = isMenuItemExisted(driver, ROOT_MENU_HOME);
		}
		else {
			//branding bar
			driver.getLogger().info("Start to check if 'Home' link is existed in navigation bar.");
			String locator = "id=" + DashBoardPageId.BRANDINGBARMYHOMELINKID;
			isExisted = isApplicationLinkExisted(driver, locator);
		}
		driver.getLogger().info("Existence check for 'Home' link is completed. Result: " + isExisted);
		driver.getLogger().info("isMyHomeLinkExisted ended");
		return isExisted;
	}

	@Override
	public boolean isVisualAnalyzerLinkExisted(WebDriver driver, String visualAnalyzerLinkName)
	{
		Validator.notEmptyString("visualAnalyzerLinkName in [isVisualAnalyzerLinkExisted]", visualAnalyzerLinkName);
		//backward compatible mode
		if (NAV_LINK_TEXT_VA_TA.equals(visualAnalyzerLinkName)) {
			visualAnalyzerLinkName = NAV_LINK_TEXT_DATAEXPLORER;
			driver.getLogger().info("Use NEW Link name: " + visualAnalyzerLinkName);
		}
		else if (NAV_LINK_TEXT_VA_LA.equals(visualAnalyzerLinkName)) {
			visualAnalyzerLinkName = NAV_LINK_TEXT_LOGEXPLORER;
			driver.getLogger().info("Use NEW Link name: " + visualAnalyzerLinkName);
		}
		boolean isExisted = false;
		driver.getLogger().info("isVisualAnalyzerLinkExisted started");

		//check if hamburger menu icon exist
		if (isHamburgerMenuEnabled(driver)) {
			driver.getLogger().info(
					"Start to check if visual analyzer link is existed in hamburger menu. Link name: " + visualAnalyzerLinkName);
			if ("Log Explorer".equals(visualAnalyzerLinkName)) {
				if (hasSubMenu(driver, ROOT_MENU_LA)) {
					expandSubMenu(driver, ROOT_MENU_LA);
				}
				else {
					visualAnalyzerLinkName = ROOT_MENU_LA;
					driver.getLogger().info(
							"Log Explorer submenu is not yet provided by Log Analytics, use menu: " + ROOT_MENU_LA);
				}
			}
			isExisted = isMenuItemEnabled(driver, visualAnalyzerLinkName);
		}
		else {
			//branding bar
			driver.getLogger().info(
					"Start to check if visual analyzer link is existed in navigation bar. Link name: " + visualAnalyzerLinkName);

			isExisted = isApplicationLinkExisted(driver, "va", visualAnalyzerLinkName);
		}
		driver.getLogger().info("Existence check for visual analyzer link is completed. Result: " + isExisted);
		driver.getLogger().info("isVisualAnalyzerLinkExisted ended");
		return isExisted;
	}

	@Override
	public boolean isWelcomeLinkExisted(WebDriver driver)
	{
		boolean isExisted = false;
		driver.getLogger().info("isWelcomeLinkExisted started");
		//check if hamburger menu icon exist
		if (isHamburgerMenuEnabled(driver)) {
			driver.getLogger().info("There is no 'Welcome' menu item in hamburger menu");
			isExisted = false;
		}
		else {
			//the branding bar
			driver.getLogger().info("Start to check if 'Welcome' link is existed in navigation bar.");

			String locator = "id=" + DashBoardPageId.BRANDINGBARWELCOMELINKID;
			isExisted = isApplicationLinkExisted(driver, locator);
			driver.getLogger().info("Existence check for 'Welcome' link is completed. Result: " + isExisted);

		}
		driver.getLogger().info("isWelcomeLinkExisted ended");
		return isExisted;
	}

	@Override
	public boolean toggleHamburgerMenu(WebDriver driver)
	{
		driver.getLogger().info("toggleHamBurerMenu started");
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.takeScreenShot();
		driver.savePageToFile();

		//check if the hamburger menu container displayed
		//if hamburger menu container displayed, return true, else return false
		driver.waitForElementPresent("css=" + DashBoardPageId_1180.HAMBURGERMENU_CONTAINER_CSS);
		boolean hamburgerMenuElem = isHamburgerMenuDisplayed(driver);

		if (hamburgerMenuElem) {
			//hamburger menu container is displayed, click the menu icon to hide menu container
			driver.getLogger().info("Click hamburger menu icon to hide menu container");
			clickHamburgerMenuIcon(driver);
			return false;
		}
		else {
			//hamburger menu container is not displayed, click the menu icon to display menu container
			driver.getLogger().info("Click hamburger menu icon to show menu container");
			clickHamburgerMenuIcon(driver);
			return true;
		}
	}

	@Override
	public void userMenuOptions(WebDriver driver, String option)
	{
		Validator.fromValidValues("option", option, USERMENU_OPTION_HELP, USERMENU_OPTION_ABOUT, USERMENU_OPTION_SIGNOUT);

		if (isHamburgerMenuEnabled(driver)) {
			if (isHamburgerMenuDisplayed(driver)) {
				driver.getLogger().info("Hamburger menu displayed, need to hide it");
				Assert.assertFalse(toggleHamburgerMenu(driver), "Hide Hamburger Menu failed!");
			}
		}
		driver.getLogger().info("Start to do branding bar user menu option: " + option);
		driver.waitForElementPresent(DashBoardPageId.BRAND_BAR_USER_MENU);
		driver.getLogger().info("Click branding bar user menu button.");
		driver.click(DashBoardPageId.BRAND_BAR_USER_MENU);

		//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 900L);
		//		By locator = By.id(DashBoardPageId.USERMENUPOPUPID);
		//		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

		Assert.assertTrue(driver.isDisplayed("id=" + DashBoardPageId.USERMENUPOPUPID));
		driver.getLogger().info("User menu popup is displayed.");

		switch (option) {
			case USERMENU_OPTION_HELP:
				driver.getLogger().info("Click Help menu.");
				driver.click(DashBoardPageId.OPTION_HELP);
				break;
			case USERMENU_OPTION_ABOUT:
				driver.getLogger().info("Click About menu.");
				driver.click(DashBoardPageId.OPTION_ABOUT);
				driver.getLogger().info("Close the About dialog.");
				driver.click(DashBoardPageId.ABOUTDIALOGCLOSE);
				break;
			case USERMENU_OPTION_SIGNOUT:
				driver.getLogger().info("Click Sign Out menu.");
				driver.click(DashBoardPageId.OPTION_LOGOUT);
				break;
			default:
				break;
		}

	}

	@Override
	public void visitApplicationAdministration(WebDriver driver, String adminLinkName)
	{
		Validator.notEmptyString("adminLinkName in [visitApplicationAdministration]", adminLinkName);
		driver.getLogger().info("visitApplicationAdministration started");
		//check if hamburger menu icon exist
		if (isHamburgerMenuEnabled(driver)) {
			//verify if current menu is rootmenu
			while (!isRootMenu(driver)) {
				driver.getLogger().info("Current menu is not root menu, need to back to parent menu");
				goBackToParentMenu(driver);
			}
			if (NAV_LINK_TEXT_ADMIN_ADMINCONSOLE.equals(adminLinkName)) {
				expandSubMenu(driver, ROOT_MENU_ADMIN);
				clickMenuItem(driver, GLOBAL_ADMIN_MENU_ENTITIES_CFG);
			}
			else if (NAV_LINK_TEXT_ADMIN_ALERT.equals(adminLinkName)) {
				expandSubMenu(driver, ROOT_MENU_ADMIN);
				clickMenuItem(driver, GLOBAL_ADMIN_MENU_ALERT_RULES);
			}
			else if (NAV_LINK_TEXT_ADMIN_AGENT.equals(adminLinkName)) {
				expandSubMenu(driver, ROOT_MENU_ADMIN);
				clickMenuItem(driver, GLOBAL_ADMIN_MENU_AGENTS);
			}
			else {
				throw new NoSuchElementException("This '" + adminLinkName + "' is not supported in Hamburger Menu.");
			}
		}
		else {
			//the branding bar
			driver.getLogger().info("Start to visit admin link from branding bar. Link name: " + adminLinkName);
			visitApplicationLink(driver, "admin", adminLinkName);
		}
		driver.getLogger().info("visitApplicationAdministration ended");
	}

	@Override
	public void visitApplicationCloudService(WebDriver driver, String cloudServiceLinkName)
	{
		String HBGMenuItem = "";
		Validator.notEmptyString("cloudServiceLinkName in [visitApplicationCloudService]", cloudServiceLinkName);
		driver.getLogger().info("visitApplicationCloudService started");
		//check if hamburger menu icon exist
		if (isHamburgerMenuEnabled(driver)) {
			HBGMenuItem = cloudServiceLinkToHamburgerMenuItem(driver, cloudServiceLinkName);
			driver.getLogger().info("Start to visit cloud service link from hambuger menu. Link name: " + HBGMenuItem);
			//verify if current menu is rootmenu
			while (!isRootMenu(driver)) {
				driver.getLogger().info("Current menu is not root menu, need to back to parent menu");
				goBackToParentMenu(driver);
			}
			if (NAV_LINK_TEXT_ADMIN_AGENT.equals(cloudServiceLinkName)) {
				expandSubMenu(driver, ROOT_MENU_ADMIN);
				clickMenuItem(driver, GLOBAL_ADMIN_MENU_AGENTS);
			}
			else {
				clickMenuItem(driver, HBGMenuItem);
			}
		}
		else {
			//the branding bar
			driver.getLogger().info("Start to visit cloud service link from branding bar. Link name: " + cloudServiceLinkName);
			visitApplicationLink(driver, "cs", cloudServiceLinkName);
		}
		driver.getLogger().info("visitApplicationCloudService ended");
	}

	@Override
	public void visitApplicationHome(WebDriver driver, String homeLinkName)
	{
		driver.getLogger().info("visitApplicationHome started");
		//check if hamburger menu icon exist
		if (isHamburgerMenuEnabled(driver)) {
			//verify if current menu is rootmenu
			while (!isRootMenu(driver)) {
				driver.getLogger().info("Current menu is not root menu, need to back to parent menu");
				goBackToParentMenu(driver);
			}
			if (NAV_LINK_TEXT_HOME_ALERTS.equals(homeLinkName)) {
				clickMenuItem(driver, ROOT_MENU_ALERTS);
			}
			else if ("Home".equals(homeLinkName)) {
				clickMenuItem(driver, ROOT_MENU_HOME);
			}
			else if ("Welcome".equals(homeLinkName)) {
				visitWelcome(driver);
			}
			else {
				driver.getLogger().info("'" + homeLinkName + "' can not be found in hamburger menu");
				throw new NoSuchElementException("The '" + homeLinkName + "' not supported in Hamburger Menu.");
			}
		}
		else {
			//the branding bar
			Validator.notEmptyString("homeLinkName in [visitApplicationHome]", homeLinkName);
			driver.getLogger().info("Start to visit home link from branding bar. Link name: " + homeLinkName);
			visitApplicationLink(driver, "home", homeLinkName);
		}
		driver.getLogger().info("visitApplicationHome ended");
	}

	@Override
	public void visitApplicationVisualAnalyzer(WebDriver driver, String visualAnalyzerLinkName)
	{
		Validator.notEmptyString("visualAnalyzerLinkName in [visitApplicationVisualAnalyzer]", visualAnalyzerLinkName);
		//backward compatible mode
		if (NAV_LINK_TEXT_VA_TA.equals(visualAnalyzerLinkName)) {
			visualAnalyzerLinkName = NAV_LINK_TEXT_DATAEXPLORER;
			driver.getLogger().info("Use NEW Link name: " + visualAnalyzerLinkName);
		}
		else if (NAV_LINK_TEXT_VA_LA.equals(visualAnalyzerLinkName)) {
			visualAnalyzerLinkName = NAV_LINK_TEXT_LOGEXPLORER;
			driver.getLogger().info("Use NEW Link name: " + visualAnalyzerLinkName);
		}

		driver.getLogger().info("visitApplicationVisualAnalyzer started");
		//check if hamburger menu icon exist
		if (isHamburgerMenuEnabled(driver)) {
			driver.getLogger().info("Start to visit menu : " + visualAnalyzerLinkName + "from hamburger menu");
			//verify if current menu is rootmenu
			while (!isRootMenu(driver)) {
				driver.getLogger().info("Current menu is not root menu, need to back to parent menu");
				goBackToParentMenu(driver);
			}
			if (NAV_LINK_TEXT_LOGEXPLORER.equals(visualAnalyzerLinkName)) {
				clickMenuItem(driver, ROOT_MENU_LA);
			}
			else {
				clickMenuItem(driver, visualAnalyzerLinkName);
			}
		}
		else {
			//the branding bar
			driver.getLogger()
					.info("Start to visit visual analyzer link from branding bar. Link name: " + visualAnalyzerLinkName);
			visitApplicationLink(driver, "va", visualAnalyzerLinkName);
		}
		driver.getLogger().info("visitApplicationVisualAnalyzer ended");
	}

	@Override
	public void visitDashboardHome(WebDriver driver)
	{
		driver.getLogger().info("visitDashboardHome started");
		//check if hamburger menu icon exist
		if (isHamburgerMenuEnabled(driver)) {
			//click the menu "Dashboards" in hamburger menu
			driver.getLogger().info("[visitDashboardHome] start to visit 'Dashboards' menu in hamburger menu.");
			//verify if current menu is rootmenu
			while (!isRootMenu(driver)) {
				driver.getLogger().info("Current menu is not root menu, need to back to parent menu");
				goBackToParentMenu(driver);
			}
			clickMenuItem(driver, ROOT_MENU_DASHBOARDS);
		}
		else {
			//the branding bar
			driver.getLogger().info("Start to visit 'Dashboards' link from branding bar.");
			visitApplicationLink(driver, DashBoardPageId.BRANDINGBARDASHBOARDLINKLOCATOR);
		}
		driver.getLogger().info("visitDashboardHome started");
	}

	@Override
	public void visitMyFavorites(WebDriver driver)
	{
		driver.getLogger().info("visitMyFavorites started");
		//check if hamburger menu icon exist
		if (isHamburgerMenuEnabled(driver)) {
			//check the 'My Favorite' options in filter options in Dashboard home page
			driver.getLogger().info("[visitMyFavorites] start to visit 'My Favorites' link in home page.");
			//verify if current menu is rootmenu
			while (!isRootMenu(driver)) {
				driver.getLogger().info("Current menu is not root menu, need to back to parent menu");
				goBackToParentMenu(driver);
			}
			clickMenuItem(driver, ROOT_MENU_DASHBOARDS);
			if (!DashboardHomeUtil.isFilterOptionSelected(driver, "favorites")) {
				DashboardHomeUtil.filterOptions(driver, "favorites");
			}
		}
		else {
			//the branding bar
			driver.getLogger().info("[visitMyFavorites] start to visit 'My Favorites' link from branding bar.");
			visitApplicationLink(driver, DashBoardPageId.BRANDINGBARMYFAVORITESLINKID);
		}
		driver.getLogger().info("visitMyFavorites ended");
	}

	@Override
	public void visitMyHome(WebDriver driver)
	{
		driver.getLogger().info("visitMyHome started");
		//check if hamburger menu icon exist
		if (isHamburgerMenuEnabled(driver)) {
			//click the menu "Home" in hamburger menu
			driver.getLogger().info("[visitMyHome] start to visit 'Home' link from hamburger menu.");
			//verify if current menu is rootmenu
			while (!isRootMenu(driver)) {
				driver.getLogger().info("Current menu is not root menu, need to back to parent menu");
				goBackToParentMenu(driver);
			}
			clickMenuItem(driver, ROOT_MENU_HOME);
		}
		else {
			//the branding bar
			driver.getLogger().info("[visitMyHome] start to visit 'Home' link from branding bar.");
			visitApplicationLink(driver, DashBoardPageId.BRANDINGBARMYHOMELINKID);
		}
		driver.getLogger().info("visitMyHome ended");
	}

	@Override
	public void visitWelcome(WebDriver driver)
	{
		driver.getLogger().info("visitWelcome started");
		//check if hamburger menu icon exist
		if (isHamburgerMenuEnabled(driver)) {
			//click the Oracle logo to go to Welcome page
			driver.getLogger().info("[visitWelcome] start to click the Oracle logo from branding bar.");
			driver.waitForElementPresent("css=" + DashBoardPageId_1180.ORACLE_LOGO_IMG_CSS);
			if (driver.isDisplayed("css=" + DashBoardPageId_1180.ORACLE_LOGO_IMG_CSS)) {
				driver.click("css=" + DashBoardPageId_1180.ORACLE_LOGO_IMG_CSS);
			}
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else {
			//branding bar
			driver.getLogger().info("[visitWelcome] start to visit 'Welcome' link from branding bar.");
			visitApplicationLink(driver, DashBoardPageId.BRANDINGBARWELCOMELINKID);
		}
		driver.getLogger().info("visitWelcome ended");
	}

	private void clickHamburgerMenuIcon(WebDriver driver)
	{
		driver.getLogger().info("Click Hamburger Menu Icon");
		driver.click("css=" + DashBoardPageId_1180.HAMBURGERMENU_ICON_CSS);
	}

	private String cloudServiceLinkToHamburgerMenuItem(WebDriver driver, String cloudServiceLinkName)
	{
		String menuItemName = "";
		if (NAV_LINK_TEXT_CS_SECU.equals(cloudServiceLinkName)) {
			menuItemName = ROOT_MENU_SECURITY;
		}
		else if (NAV_LINK_TEXT_CS_COMP.equals(cloudServiceLinkName)) {
			menuItemName = ROOT_MENU_COMPLIANCE;
		}
		else if (NAV_LINK_TEXT_CS_APM.equals(cloudServiceLinkName)) {
			menuItemName = ROOT_MENU_APM;
		}
		else if (NAV_LINK_TEXT_ADMIN_AGENT.equals(cloudServiceLinkName)) {
			menuItemName = ROOT_MENU_ADMIN;
		}
		else if (NAV_LINK_TEXT_CS_IM.equals(cloudServiceLinkName)) {
			menuItemName = ROOT_MENU_MONITORING;
		}
		else {
			menuItemName = cloudServiceLinkName;
		}
		return menuItemName;
	}

	private boolean isElementEnabled(WebDriver driver, WebElement nav)
	{
		String existed = "";
		existed = nav.findElement(By.xpath("..")).getAttribute("aria-disabled");

		driver.getLogger().info("Existed: " + existed);
		if (existed == null) {
			driver.getLogger().info("Is Null");
		}
		if ("".equals(existed)) {
			driver.getLogger().info("Is Blank");
		}
		if (!"true".equals(existed)) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Check if the hamburgerMenu is enabled or not
	 *
	 * @param driver
	 *            WebDriver driver
	 * @return true - hamburger menu is enabled <br>
	 *         false - hamburger menu is disabled
	 */

	private boolean isHamburgerMenuEnabled(WebDriver driver)
	{
		if (driver.isElementPresent("css=" + DashBoardPageId_1180.HAMBURGERMENU_ICON_CSS)) {
			return true;
		}
		else {
			return false;
		}
	}

	private boolean isRootMenu(WebDriver driver)
	{
		if (driver.isDisplayed("css=" + DashBoardPageId_1180.HAMBURGERMENU_PREVIOUSICON_CSS)) {
			driver.getLogger().info("Current Menu is not root menu");
			return false;
		}
		else {
			driver.getLogger().info("Current Menu is root menu");
			return true;
		}
	}
}
