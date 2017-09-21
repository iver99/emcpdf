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

import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class BrandingBarUtil_171 extends BrandingBarUtil_Version implements IBrandingBarUtil
{

	private static final Logger LOGGER = LogManager.getLogger(BrandingBarUtil_171.class);
	static final String BRANDINGBARSERVICENAMEXPATH = "//div[@id='emaas-appheader-bar']/div[@class='emaas-appheader-logo']/div[@class='emaas-appheader-appname emaas-appheader-appname-twoline']/span";

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#clickHierarchicalMenu(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void clickHierarchicalMenu(WebDriver driver, String menuitem)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#clickMenuItem(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void clickMenuItem(WebDriver driver, String menuitem)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	/**
	 * @param driver
	 * @param menuitem
	 */
	@Override
	public void expandSubMenu(WebDriver driver, String menuitem)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#getCurrentMenuHeader(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public String getCurrentMenuHeader(WebDriver driver)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
		return null;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#clickPreviousLink(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void goBackToParentMenu(WebDriver driver)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#hasSubMenu(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean hasSubMenu(WebDriver driver, String menuitem)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
		return false;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#isAdmin(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean isAdmin(WebDriver driver)
	{
		driver.getLogger().info("start validating admin tab");
		//Open the navigation bar if it's not displayed
		openNavigationBar(driver);
		boolean isDisplayed = driver.isDisplayed("id=" + DashBoardPageId.BRANDINGBARADMINLINKSID);
		return isDisplayed;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#isAdminLinkExisted(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isAdminLinkExisted(WebDriver driver, String adminLinkName)
	{
		Validator.notEmptyString("adminLinkName in [isAdminLinkExisted]", adminLinkName);
		driver.getLogger().info("Start to check if admin link is existed in navigation bar. Link name: " + adminLinkName);
		boolean isExisted = false;
		isExisted = isApplicationLinkExisted(driver, "admin", adminLinkName);
		driver.getLogger().info("Existence check for admin link is completed. Result: " + isExisted);
		return isExisted;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#isAlertsLinkExisted(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean isAlertsLinkExisted(WebDriver driver)
	{
		driver.getLogger().info("Start to check if 'Alert' link is existed in navigation bar.");
		boolean isExisted = false;
		isExisted = isApplicationLinkExisted(driver, "home", BrandingBarUtil.NAV_LINK_TEXT_HOME_ALERTS);

		driver.getLogger().info("Existence check for 'Alert' link is completed. Result: " + isExisted);
		return isExisted;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#isBrandingBarServiceNamePresents(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isBrandingBarServiceNamePresent(WebDriver driver, String servicename)
	{
		driver.getLogger().info("Start to check if 'service' link is existed in navigation bar.");
		boolean isExisted = false;
		isExisted = servicename.equalsIgnoreCase(driver.getText("xpath=" + BRANDINGBARSERVICENAMEXPATH));
		driver.getLogger().info("Existence check for 'service' link is completed. Result: " + isExisted);
		return isExisted;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#isCloudServiceLinkExisted(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isCloudServiceLinkExisted(WebDriver driver, String cloudServiceLinkName)
	{
		Validator.notEmptyString("cloudServiceLinkName in [isCloudServiceLinkExisted]", cloudServiceLinkName);
		driver.getLogger().info(
				"Start to check if cloud service link is existed in navigation bar. Link name: " + cloudServiceLinkName);
		boolean isExisted = false;
		isExisted = isApplicationLinkExisted(driver, "cs", cloudServiceLinkName);
		driver.getLogger().info("Existence check for cloud service link is completed. Result: " + isExisted);
		return isExisted;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#isDashboardHomeLinkExisted(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean isDashboardHomeLinkExisted(WebDriver driver)
	{
		driver.getLogger().info("Start to check if 'Dashboards' link is existed in navigation bar.");
		boolean isExisted = false;
		String locator = "id=" + DashBoardPageId.BRANDINGBARDASHBOARDHOMELINKID;
		isExisted = isApplicationLinkExisted(driver, locator);
		driver.getLogger().info("Existence check for 'Dashboards' link is completed. Result: " + isExisted);
		return isExisted;

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#isHamburgerMenuDisplayed(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean isHamburgerMenuDisplayed(WebDriver driver)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
		return false;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#isHomeLinkExisted(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isHomeLinkExisted(WebDriver driver, String homeLinkName)
	{
		Validator.notEmptyString("homeLinkName in [isHomeLinkExisted]", homeLinkName);
		driver.getLogger().info("Start to check if home link is existed in navigation bar. Link name: " + homeLinkName);
		boolean isExisted = false;
		isExisted = isApplicationLinkExisted(driver, "home", homeLinkName);
		driver.getLogger().info("Existence check for home link is completed. Result: " + isExisted);
		return isExisted;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#isMenuItemEnabled(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isMenuItemEnabled(WebDriver driver, String menuitem)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
		return false;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#isMenuItemExisted(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isMenuItemExisted(WebDriver driver, String menuitem)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
		return false;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#isMyFavoritesLinkExisted(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean isMyFavoritesLinkExisted(WebDriver driver)
	{
		driver.getLogger().info("Start to check if 'My Favorites' link is existed in navigation bar.");
		boolean isExisted = false;
		String locator = "id=" + DashBoardPageId.BRANDINGBARMYFAVORITESLINKID;
		isExisted = isApplicationLinkExisted(driver, locator);
		driver.getLogger().info("Existence check for 'My Favorites' link is completed. Result: " + isExisted);
		return isExisted;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#isMyHomeLinkExisted(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean isMyHomeLinkExisted(WebDriver driver)
	{
		driver.getLogger().info("Start to check if 'Home' link is existed in navigation bar.");
		boolean isExisted = false;
		String locator = "id=" + DashBoardPageId.BRANDINGBARMYHOMELINKID;
		isExisted = isApplicationLinkExisted(driver, locator);
		driver.getLogger().info("Existence check for 'Home' link is completed. Result: " + isExisted);
		return isExisted;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#isVisualAnalyzerLinkExisted(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isVisualAnalyzerLinkExisted(WebDriver driver, String visualAnalyzerLinkName)
	{
		Validator.notEmptyString("visualAnalyzerLinkName in [isVisualAnalyzerLinkExisted]", visualAnalyzerLinkName);
		driver.getLogger().info(
				"Start to check if visual analyzer link is existed in navigation bar. Link name: " + visualAnalyzerLinkName);
		boolean isExisted = false;
		isExisted = isApplicationLinkExisted(driver, "va", visualAnalyzerLinkName);
		driver.getLogger().info("Existence check for visual analyzer link is completed. Result: " + isExisted);
		return isExisted;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#isWelcomeLinkExisted(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean isWelcomeLinkExisted(WebDriver driver)
	{
		driver.getLogger().info("Start to check if 'Welcome' link is existed in navigation bar.");
		boolean isExisted = false;
		String locator = "id=" + DashBoardPageId.BRANDINGBARWELCOMELINKID;
		isExisted = isApplicationLinkExisted(driver, locator);
		driver.getLogger().info("Existence check for 'Welcome' link is completed. Result: " + isExisted);
		return isExisted;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#toggleHambugerMenu(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean toggleHamburgerMenu(WebDriver driver)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
		return false;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#userMenuOptions(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void userMenuOptions(WebDriver driver, String option)
	{
		Validator.fromValidValues("option", option, USERMENU_OPTION_HELP, USERMENU_OPTION_ABOUT, USERMENU_OPTION_SIGNOUT);

		driver.getLogger().info("Start to do branding bar user menu option: " + option);
		driver.waitForElementPresent(DashBoardPageId.BRAND_BAR_USER_MENU);
		driver.getLogger().info("Click branding bar user menu button.");
		driver.click(DashBoardPageId.BRAND_BAR_USER_MENU);
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

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#visitApplicationAdministration(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void visitApplicationAdministration(WebDriver driver, String adminLinkName)
	{
		Validator.notEmptyString("adminLinkName in [visitApplicationAdministration]", adminLinkName);
		driver.getLogger().info("Start to visit admin link from branding bar. Link name: " + adminLinkName);
		visitApplicationLink(driver, "admin", adminLinkName);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#visitApplicationCloudService(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void visitApplicationCloudService(WebDriver driver, String cloudServiceLinkName)
	{
		Validator.notEmptyString("cloudServiceLinkName in [visitApplicationCloudService]", cloudServiceLinkName);
		driver.getLogger().info("Start to visit cloud service link from branding bar. Link name: " + cloudServiceLinkName);
		visitApplicationLink(driver, "cs", cloudServiceLinkName);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#visitApplicationHome(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void visitApplicationHome(WebDriver driver, String homeLinkName)
	{
		Validator.notEmptyString("homeLinkName in [visitApplicationHome]", homeLinkName);
		driver.getLogger().info("Start to visit home link from branding bar. Link name: " + homeLinkName);
		visitApplicationLink(driver, "home", homeLinkName);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#visitApplicationVisualAnalyzer(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void visitApplicationVisualAnalyzer(WebDriver driver, String visualAnalyzerLinkName)
	{
		Validator.notEmptyString("visualAnalyzerLinkName in [visitApplicationVisualAnalyzer]", visualAnalyzerLinkName);
		driver.getLogger().info("Start to visit visual analyzer link from branding bar. Link name: " + visualAnalyzerLinkName);
		visitApplicationLink(driver, "va", visualAnalyzerLinkName);
	}

	@Override
	public void visitDashboardHome(WebDriver driver)
	{
		driver.getLogger().info("Start to visit 'Dashboards' link from branding bar.");
		visitApplicationLink(driver, DashBoardPageId.BRANDINGBARDASHBOARDLINKLOCATOR);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#visitMyFavorites(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitMyFavorites(WebDriver driver)
	{
		driver.getLogger().info("[visitMyFavorites] start to visit 'My Favorites' link from branding bar.");
		visitApplicationLink(driver, DashBoardPageId.BRANDINGBARMYFAVORITESLINKID);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#visitMyHome(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitMyHome(WebDriver driver)
	{
		driver.getLogger().info("[visitMyHome] start to visit 'Home' link from branding bar.");
		visitApplicationLink(driver, DashBoardPageId.BRANDINGBARMYHOMELINKID);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#visitWelcome(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitWelcome(WebDriver driver)
	{
		driver.getLogger().info("[visitWelcome] start to visit 'Welcome' link from branding bar.");
		visitApplicationLink(driver, DashBoardPageId.BRANDINGBARWELCOMELINKID);
	}

	/**
	 * Check the visibility of the navigation links panel
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param visible
	 *            The visibility of the navigation links panel
	 * @return
	 */
	private void checkLinksMenuVisibility(WebDriver driver, boolean visible)
	{
		if (visible) {
			Assert.assertTrue(driver.isDisplayed("id=" + DashBoardPageId.BRANDINGBARNAVLINKSID));
		}
		else {
			Assert.assertFalse(driver.isDisplayed("id=" + DashBoardPageId.BRANDINGBARNAVLINKSID));
		}

	}

	/**
	 * Find the parent div id for the specified link type
	 *
	 * @param linkType
	 *            Type of the link, should be one of "home", "cs", "va", "admin"
	 * @return
	 */
	private String getApplicationLinkParentId(String linkType)
	{
		String parentId = null;
		switch (linkType) {
			//Home links
			case "home":
				parentId = DashBoardPageId.BRANDINGBARHOMELINKSID;
				break;
			//Cloud service links
			case "cs":
				parentId = DashBoardPageId.BRANDINGBARCLOUDSERVICELINKSID;
				break;
			//Visual analyzer links
			case "va":
				parentId = DashBoardPageId.BRANDINGBARVISUALANALYZERLINKSID;
				break;
			//Administration links
			case "admin":
				parentId = DashBoardPageId.BRANDINGBARADMINLINKSID;
				break;
			default:
				break;
		}
		return parentId;
	}

	/**
	 * Check if a element is stale or not
	 *
	 * @param element
	 *            WebElement instance
	 * @return
	 */
	private boolean isElementStale(WebElement element)
	{
		try {
			element.getText();
			return false;
		}
		catch (StaleElementReferenceException e) {

			LOGGER.info("context", e);
			return true;
		}
	}

	/**
	 * Check if the navigation bar is displayed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	private boolean isNavigationBarDisplayed(WebDriver driver)
	{
		boolean isDisplayed = driver.isDisplayed("id=" + DashBoardPageId.BRANDINGBARNAVLINKSID);
		driver.getLogger().info("Check if the navigation bar is displayed or not. Result: " + isDisplayed);
		return isDisplayed;
	}

	/**
	 * Check if the specified link is exited or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param locator
	 *            Locator of the link
	 * @return
	 */
	protected boolean isApplicationLinkExisted(WebDriver driver, String locator)
	{
		Validator.notEmptyString("locator in [isApplicationLinkExisted]", locator);
		boolean isExisted = false;
		//Check if navigation bar is already displayed or not
		boolean isNavBarDisplayed = isNavigationBarDisplayed(driver);
		//If not, open the navigation bar, if yes, just keep it as it is
		openNavigationBar(driver);
		//Wait until the links menu is displayed
		checkLinksMenuVisibility(driver, true);
		isExisted = driver.isDisplayed(locator);
		//Recover the navigation bar to the original status
		if (!isNavBarDisplayed) {
			driver.click("id=" + DashBoardPageId.LINKID);
		}
		driver.takeScreenShot();
		driver.savePageToFile();
		return isExisted;
	}

	/**
	 * Check if the specified link is exited or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param linkType
	 *            Type of the link, should be one of "home", "cs", "va", "admin"
	 * @param linkName
	 *            Name of the link
	 * @return
	 */
	protected boolean isApplicationLinkExisted(WebDriver driver, String linkType, String linkName)
	{
		String parentId = getApplicationLinkParentId(linkType);
		//Find link element by parent id and link name
		String xpath = "//div[@id='" + parentId + "']/descendant::a[text()='" + linkName + "']";
		return isApplicationLinkExisted(driver, xpath);
	}

	/**
	 * Open the navigation bar
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	protected void openNavigationBar(WebDriver driver)
	{
		//Open the navigation bar if it's not displayed
		if (!isNavigationBarDisplayed(driver)) {
			String navBarId = "id=" + DashBoardPageId.LINKID;
			driver.waitForElementPresent(navBarId);
			driver.click(navBarId);
		}
		driver.takeScreenShot();
		driver.savePageToFile();
	}

	/**
	 * Navigate to the specified application page by locator
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param locator
	 *            Locator the link
	 * @return
	 */
	protected void visitApplicationLink(WebDriver driver, final String locator)
	{
		Validator.notEmptyString("locator in [visitApplicationLink]", locator);
		// Open navigation bar if it's not displayed
		openNavigationBar(driver);
		//Wait until the links menu is displayed
		checkLinksMenuVisibility(driver, true);
		driver.waitForElementPresent(locator);
		//Add try catch to handle StaleElementReferenceException
		try {
			driver.click(locator);
		}
		catch (StaleElementReferenceException e) {
			LOGGER.info("context", e);
			driver.getLogger().info("StaleElementReferenceException thrown, wait for element becoming not stale");
			// wait until element is not stale
			new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT).until(new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(org.openqa.selenium.WebDriver d)
				{
					WebElement element = d.findElement(By.xpath(locator));
					Boolean isStale = isElementStale(element);
					return !isStale;
				}
			});
			driver.click(locator);
		}

		driver.takeScreenShot();
		driver.savePageToFile();
	}

	/**
	 * Navigate to the specified application page
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param linkType
	 *            Type of the link, should be one of "home", "cs", "va", "admin"
	 * @param linkName
	 *            Name of the link
	 * @return
	 */
	protected void visitApplicationLink(WebDriver driver, String linkType, String linkName)
	{
		String parentId = getApplicationLinkParentId(linkType);
		//Find link element by parent id and link name
		String xpath = "//div[@id='" + parentId + "']/descendant::a[text()='" + linkName + "']";
		visitApplicationLink(driver, xpath);
	}
	@Override
	public void verifyUserMenuItemByText(WebDriver driver, String item, String text){
		// overrided in BrandingBarUtil_1230
	}
}
