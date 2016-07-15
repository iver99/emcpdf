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

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class BrandingBarUtil_171 extends BrandingBarUtil_Version implements IBrandingBarUtil
{

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#isAdmin(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean isAdmin(WebDriver driver) throws Exception
	{
		driver.getLogger().info("start validating admin tab");
		//Open the navigation bar if it's not displayed
		openNavigationBar(driver);
		boolean isDisplayed = driver.isDisplayed("id=" + DashBoardPageId.BrandingBarAdminLinksID);
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
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#isCloudServiceLinkExisted(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isCloudServiceLinkExisted(WebDriver driver, String cloudServiceLinkName)
	{
		Validator.notEmptyString("cloudServiceLinkName in [isCloudServiceLinkExisted]", cloudServiceLinkName);
		driver.getLogger()
				.info("Start to check if cloud service link is existed in navigation bar. Link name: " + cloudServiceLinkName);
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
		String locator = "id=" + DashBoardPageId.BrandingBarDashboardHomeLinkID;
		isExisted = isApplicationLinkExisted(driver, locator);
		driver.getLogger().info("Existence check for 'Dashboards' link is completed. Result: " + isExisted);
		return isExisted;

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
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#isMyFavoritesLinkExisted(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean isMyFavoritesLinkExisted(WebDriver driver)
	{
		driver.getLogger().info("Start to check if 'My Favorites' link is existed in navigation bar.");
		boolean isExisted = false;
		String locator = "id=" + DashBoardPageId.BrandingBarMyFavoritesLinkID;
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
		String locator = "id=" + DashBoardPageId.BrandingBarMyHomeLinkID;
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
		String locator = "id=" + DashBoardPageId.BrandingBarWelcomeLinkID;
		isExisted = isApplicationLinkExisted(driver, locator);
		driver.getLogger().info("Existence check for 'Welcome' link is completed. Result: " + isExisted);
		return isExisted;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#userMenuOptions(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void userMenuOptions(WebDriver driver, String option) throws Exception
	{
		Validator.fromValidValues("option", option, USERMENU_OPTION_HELP, USERMENU_OPTION_ABOUT, USERMENU_OPTION_SIGNOUT);

		driver.getLogger().info("Start to do branding bar user menu option: " + option);
		driver.waitForElementPresent(DashBoardPageId.Brand_Bar_User_Menu);
		driver.getLogger().info("Click branding bar user menu button.");
		driver.click(DashBoardPageId.Brand_Bar_User_Menu);

		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 900L);
		By locator = By.id(DashBoardPageId.UserMenuPopupId);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		driver.takeScreenShot();
		Assert.assertTrue(driver.isDisplayed("id=" + DashBoardPageId.UserMenuPopupId));
		driver.getLogger().info("User menu popup is displayed.");

		switch (option) {
			case USERMENU_OPTION_HELP:
				driver.takeScreenShot();
				driver.getLogger().info("Click Help menu.");
				driver.click(DashBoardPageId.Option_Help);
				break;
			case USERMENU_OPTION_ABOUT:
				driver.takeScreenShot();
				driver.getLogger().info("Click About menu.");
				driver.click(DashBoardPageId.Option_About);
				driver.takeScreenShot();
				driver.getLogger().info("Close the About dialog.");
				driver.click(DashBoardPageId.AboutDialogClose);
				break;
			case USERMENU_OPTION_SIGNOUT:
				driver.getLogger().info("Click Sign Out menu.");
				driver.click(DashBoardPageId.Option_Logout);
				break;
		}
		driver.takeScreenShot();
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#visitApplicationAdministration(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void visitApplicationAdministration(WebDriver driver, String adminLinkName) throws Exception
	{
		Validator.notEmptyString("adminLinkName in [visitApplicationAdministration]", adminLinkName);
		driver.getLogger().info("Start to visit admin link from branding bar. Link name: " + adminLinkName);
		visitApplicationLink(driver, "admin", adminLinkName);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#visitApplicationCloudService(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void visitApplicationCloudService(WebDriver driver, String cloudServiceLinkName) throws Exception
	{
		Validator.notEmptyString("cloudServiceLinkName in [visitApplicationCloudService]", cloudServiceLinkName);
		driver.getLogger().info("Start to visit cloud service link from branding bar. Link name: " + cloudServiceLinkName);
		visitApplicationLink(driver, "cs", cloudServiceLinkName);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#visitApplicationHome(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void visitApplicationHome(WebDriver driver, String homeLinkName) throws Exception
	{
		Validator.notEmptyString("homeLinkName in [visitApplicationHome]", homeLinkName);
		driver.getLogger().info("Start to visit home link from branding bar. Link name: " + homeLinkName);
		visitApplicationLink(driver, "home", homeLinkName);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#visitApplicationVisualAnalyzer(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void visitApplicationVisualAnalyzer(WebDriver driver, String visualAnalyzerLinkName) throws Exception
	{
		Validator.notEmptyString("visualAnalyzerLinkName in [visitApplicationVisualAnalyzer]", visualAnalyzerLinkName);
		driver.getLogger().info("Start to visit visual analyzer link from branding bar. Link name: " + visualAnalyzerLinkName);
		visitApplicationLink(driver, "va", visualAnalyzerLinkName);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#visitDashboardHome(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */

	@Override
	public void visitDashboardHome(WebDriver driver) throws Exception
	{
		driver.getLogger().info("Start to visit 'Dashboards' link from branding bar.");
		visitApplicationLink(driver, DashBoardPageId.BrandingBarDashboardLinkLocator);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#visitMyFavorites(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitMyFavorites(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[visitMyFavorites] start to visit 'My Favorites' link from branding bar.");
		visitApplicationLink(driver, DashBoardPageId.BrandingBarMyFavoritesLinkID);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#visitMyHome(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitMyHome(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[visitMyHome] start to visit 'Home' link from branding bar.");
		visitApplicationLink(driver, DashBoardPageId.BrandingBarMyHomeLinkID);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#visitWelcome(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitWelcome(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[visitWelcome] start to visit 'Welcome' link from branding bar.");
		visitApplicationLink(driver, DashBoardPageId.BrandingBarWelcomeLinkID);
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
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 900L);
		By locator = By.id(DashBoardPageId.BrandingBarNavLinksId);
		if (visible) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			driver.takeScreenShot();
			Assert.assertTrue(driver.isDisplayed("id=" + DashBoardPageId.BrandingBarNavLinksId));
		}
		else {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
			driver.takeScreenShot();
			Assert.assertFalse(driver.isDisplayed("id=" + DashBoardPageId.BrandingBarNavLinksId));
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
				parentId = DashBoardPageId.BrandingBarHomeLinksID;
				break;
			//Cloud service links
			case "cs":
				parentId = DashBoardPageId.BrandingBarCloudServiceLinksID;
				break;
			//Visual analyzer links
			case "va":
				parentId = DashBoardPageId.BrandingBarVisualAnalyzerLinksID;
				break;
			//Administration links
			case "admin":
				parentId = DashBoardPageId.BrandingBarAdminLinksID;
				break;
		}
		return parentId;
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
	private boolean isApplicationLinkExisted(WebDriver driver, String locator)
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
			driver.click("id=" + DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
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
	private boolean isApplicationLinkExisted(WebDriver driver, String linkType, String linkName)
	{
		String parentId = getApplicationLinkParentId(linkType);
		//Find link element by parent id and link name
		String xpath = "//div[@id='" + parentId + "']/descendant::a[text()='" + linkName + "']";
		return isApplicationLinkExisted(driver, xpath);
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
		boolean isDisplayed = driver.isDisplayed("id=" + DashBoardPageId.BrandingBarNavLinksId);
		driver.getLogger().info("Check if the navigation bar is displayed or not. Result: " + isDisplayed);
		return isDisplayed;
	}

	/**
	 * Open the navigation bar
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	private void openNavigationBar(WebDriver driver)
	{
		//Open the navigation bar if it's not displayed
		if (!isNavigationBarDisplayed(driver)) {
			String navBarId = "id=" + DashBoardPageId.LinkID;
			driver.waitForElementPresent(navBarId);
			driver.click(navBarId);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		driver.takeScreenShot();
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
	private void visitApplicationLink(WebDriver driver, final String locator)
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
	private void visitApplicationLink(WebDriver driver, String linkType, String linkName)
	{
		String parentId = getApplicationLinkParentId(linkType);
		//Find link element by parent id and link name
		String xpath = "//div[@id='" + parentId + "']/descendant::a[text()='" + linkName + "']";
		visitApplicationLink(driver, xpath);
	}
}