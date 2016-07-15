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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class WelcomeUtil_171 extends WelcomeUtil_Version implements IWelcomeUtil
{
	public final String DATA_EXPLORERS_LOG = "log";

	public final String DATA_EXPLORERS_ANALYZE = "analyze";

	public final String DATA_EXPLORERS_SEARCH = "search";

	public final String ITA_DEFAULT = "default";

	public final String ITA_PERFORMANCE_ANALYTICS_DATABASE = "performanceAnalyticsDatabase";

	public final String ITA_PERFORMANCE_ANALYTICS_MIDDLEWARE = "performanceAnalyticsMiddleware";

	public final String ITA_RESOURCE_ANALYTICS_DATABASE = "resourceAnalyticsDatabase";

	public final String ITA_RESOURCE_ANALYTICS_MIDDLEWARE = "resourceAnalyticsMiddleware";

	public final String ITA_DATA_EXPLORER_ANALYZE = "dataExplorerAnalyze";

	public final String ITA_DATA_EXPLORER = "dataExplorer";

	public final String LEARN_MORE_GET_STARTED = "getStarted";

	public final String LEARN_MORE_VIDEOS = "videos";

	public final String LEARN_MORE_SERVICE_OFFERINGS = "serviceOfferings";

	public final String SERVICE_NAME_APM = "APM";

	public final String SERVICE_NAME_LA = "LA";
	public final String SERVICE_NAME_ITA = "ITA";
	public final String SERVICE_NAME_DASHBOARDS = "dashboards";

	public final String SERVICE_NAME_DATA_EXPLORERS = "dataExplorers";

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#dataExplorers(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void dataExplorers(WebDriver driver, String selection) throws Exception
	{
		String eleXpath = null;
		driver.getLogger().info("Visiting Data Explorer-" + selection + " from Welcome Page...");

		Validator.fromValidValues("dataExplorersSelection", selection, DATA_EXPLORERS_LOG, DATA_EXPLORERS_ANALYZE,
				DATA_EXPLORERS_SEARCH);

		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent("id=oj-select-choice-" + DashBoardPageId.Welcome_DataExp_SelectID);
		driver.click("id=oj-select-choice-" + DashBoardPageId.Welcome_DataExp_SelectID);
		driver.takeScreenShot();
		switch (selection) {
			case DATA_EXPLORERS_LOG:
				eleXpath = getOptionXpath(driver, DashBoardPageId.Welcome_DataExp_SelectID, DashBoardPageId.Welcome_DataExp_Log);
				break;
			case DATA_EXPLORERS_ANALYZE:
				eleXpath = getOptionXpath(driver, DashBoardPageId.Welcome_DataExp_SelectID,
						DashBoardPageId.Welcome_DataExp_Analyze);
				break;
			case DATA_EXPLORERS_SEARCH:
				eleXpath = getOptionXpath(driver, DashBoardPageId.Welcome_DataExp_SelectID,
						DashBoardPageId.Welcome_DataExp_Search);
				break;
		}
		driver.getWebDriver().findElement(By.xpath(eleXpath)).click();
		driver.takeScreenShot();
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#isLearnMoreItemExisted(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isLearnMoreItemExisted(WebDriver driver, String itemName)
	{
		driver.getLogger().info("Start to check if learn more item: " + itemName + " is existed in welcome page...");

		Validator.fromValidValues("learMoreItem", itemName, LEARN_MORE_GET_STARTED, LEARN_MORE_VIDEOS,
				LEARN_MORE_SERVICE_OFFERINGS);

		boolean isExisted = false;
		String itemId = getLearnMoreItemId(itemName);
		String nameExpected = getExpectedText(itemName);
		driver.waitForElementPresent("id=" + itemId);
		driver.waitForText("id=" + itemId, nameExpected);
		String nameDisplayed = driver.getWebDriver().findElement(By.id(itemId)).getText();
		if (nameDisplayed.equals(nameExpected)) {
			isExisted = true;
		}
		driver.getLogger().info("Check of learn more item: " + itemName + " existence is finished! Result: " + isExisted);
		return isExisted;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#isServiceExistedInWelcome(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isServiceExistedInWelcome(WebDriver driver, String serviceName) throws Exception
	{
		driver.getLogger().info("Start to check if service: " + serviceName + " is existed in welcome page...");

		Validator.fromValidValues("serviceName", serviceName, SERVICE_NAME_APM, SERVICE_NAME_LA, SERVICE_NAME_ITA,
				SERVICE_NAME_DASHBOARDS, SERVICE_NAME_DATA_EXPLORERS);

		boolean isExisted = false;
		String serviceWrapperId = getServiceWrapperId(serviceName);
		String xpath = "//*[@id='" + serviceWrapperId
				+ "']/div[@class='service-box-wrapper']/div[@class='landing-home-box-content']/div[@class='landing-home-box-content-head']";

		String nameExpected = getExpectedText(serviceName);
		driver.waitForElementPresent(xpath);
		driver.waitForText(xpath, nameExpected);
		String nameDisplayed = driver.getWebDriver().findElement(By.xpath(xpath)).getText();
		if (nameDisplayed.equals(nameExpected)) {
			isExisted = true;
		}
		driver.getLogger()
				.info("Check of service: " + serviceName + " existence in welcome page is finished! Result: " + isExisted);
		return isExisted;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#learnMoreHow(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void learnMoreHow(WebDriver driver) throws Exception
	{
		driver.getLogger().info("Visiting 'Learn More-How to get started' from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("id=" + DashBoardPageId.Welcome_LearnMore_getStarted);
		driver.click("id=" + DashBoardPageId.Welcome_LearnMore_getStarted);
		driver.takeScreenShot();
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#learnMoreServiceOffering(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void learnMoreServiceOffering(WebDriver driver) throws Exception
	{
		driver.getLogger().info("Visiting 'Learn More-Service Offerings' from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("id=" + DashBoardPageId.Welcome_LearnMore_ServiceOffering);
		driver.click("id=" + DashBoardPageId.Welcome_LearnMore_ServiceOffering);
		driver.takeScreenShot();
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#learnMoreVideo(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void learnMoreVideo(WebDriver driver) throws Exception
	{
		driver.getLogger().info("Visiting 'Learn More-Videos' from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("id=" + DashBoardPageId.Welcome_LearnMore_Videos);
		driver.click("id=" + DashBoardPageId.Welcome_LearnMore_Videos);
		driver.takeScreenShot();
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#visitAPM(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitAPM(WebDriver driver) throws Exception
	{
		driver.getLogger().info("Visit APM from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent("id=" + DashBoardPageId.Welcome_APMLinkCSS);
		driver.click("id=" + DashBoardPageId.Welcome_APMLinkCSS);
		driver.takeScreenShot();
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#visitDashboards(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitDashboards(WebDriver driver) throws Exception
	{
		driver.getLogger().info("Visit Dashboards from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent("id=" + DashBoardPageId.Welcome_DashboardsLinkID);
		driver.click("id=" + DashBoardPageId.Welcome_DashboardsLinkID);
		driver.takeScreenShot();
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#visitITA(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void visitITA(WebDriver driver, String selection) throws Exception
	{
		driver.getLogger().info("Visiting ITA-" + selection + " from Welcome Page...");

		Validator.fromValidValues("ITASelection", selection, ITA_DEFAULT, ITA_PERFORMANCE_ANALYTICS_DATABASE,
				ITA_PERFORMANCE_ANALYTICS_MIDDLEWARE, ITA_RESOURCE_ANALYTICS_DATABASE, ITA_RESOURCE_ANALYTICS_MIDDLEWARE,
				ITA_DATA_EXPLORER_ANALYZE, ITA_DATA_EXPLORER);

		WaitUtil.waitForPageFullyLoaded(driver);

		if (ITA_DEFAULT.equals(selection)) {
			driver.waitForElementPresent("id=" + DashBoardPageId.Welcome_ITALinkID);
			driver.click("id=" + DashBoardPageId.Welcome_ITALinkID);
			driver.takeScreenShot();
		}
		else {
			String eleXpath = null;
			driver.waitForElementPresent("id=oj-select-choice-" + DashBoardPageId.Welcome_ITA_SelectID);
			driver.click("id=oj-select-choice-" + DashBoardPageId.Welcome_ITA_SelectID);
			driver.takeScreenShot();
			switch (selection) {
				case ITA_PERFORMANCE_ANALYTICS_DATABASE:
					eleXpath = getOptionXpath(driver, DashBoardPageId.Welcome_ITA_SelectID,
							DashBoardPageId.Welcome_ITA_PADatabase);
					break;
				case ITA_PERFORMANCE_ANALYTICS_MIDDLEWARE:
					eleXpath = getOptionXpath(driver, DashBoardPageId.Welcome_ITA_SelectID,
							DashBoardPageId.Welcome_ITA_PAMiddleware);
					break;
				case ITA_RESOURCE_ANALYTICS_DATABASE:
					eleXpath = getOptionXpath(driver, DashBoardPageId.Welcome_ITA_SelectID,
							DashBoardPageId.Welcome_ITA_RADatabase);
					break;
				case ITA_RESOURCE_ANALYTICS_MIDDLEWARE:
					eleXpath = getOptionXpath(driver, DashBoardPageId.Welcome_ITA_SelectID,
							DashBoardPageId.Welcome_ITA_RAMiddleware);
					break;
				case ITA_DATA_EXPLORER_ANALYZE:
					eleXpath = getOptionXpath(driver, DashBoardPageId.Welcome_ITA_SelectID,
							DashBoardPageId.Welcome_ITA_DEAnalyze);
					break;
				case ITA_DATA_EXPLORER:
					eleXpath = getOptionXpath(driver, DashBoardPageId.Welcome_ITA_SelectID, DashBoardPageId.Welcome_ITA_DE);
					break;
			}
			driver.click(eleXpath);
			driver.takeScreenShot();
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#visitLA(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitLA(WebDriver driver) throws Exception
	{
		driver.getLogger().info("Visiting LA from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent("id=" + DashBoardPageId.Welcome_LALinkCSS);
		driver.click("id=" + DashBoardPageId.Welcome_LALinkCSS);
		driver.takeScreenShot();
	}

	/**
	 * Get text of each service or item shown in welcome page.
	 *
	 * @param serviceName
	 *            APM | LA | ITA | dashboards | dataExplorers | getStarted | videos | serviceOfferings
	 * @return
	 */
	private String getExpectedText(String serviceName)
	{
		String expectedName = null;
		switch (serviceName) {
			case SERVICE_NAME_APM:
				expectedName = "Application Performance Monitoring";
				break;
			case SERVICE_NAME_LA:
				expectedName = "Log Analytics";
				break;
			case SERVICE_NAME_ITA:
				expectedName = "IT Analytics";
				break;
			case SERVICE_NAME_DASHBOARDS:
				expectedName = "Dashboards";
				break;
			case SERVICE_NAME_DATA_EXPLORERS:
				expectedName = "Data Explorers";
				break;
			case LEARN_MORE_GET_STARTED:
				expectedName = "How to get started";
				break;
			case LEARN_MORE_VIDEOS:
				expectedName = "Videos";
				break;
			case LEARN_MORE_SERVICE_OFFERINGS:
				expectedName = "Service Offerings";
				break;
		}
		return expectedName;
	}

	/**
	 * Get id of items in Learn More
	 *
	 * @param itemName
	 *            getstarted | videos | serviceOfferings
	 * @return
	 */
	private String getLearnMoreItemId(String itemName)
	{
		String itemId = null;
		switch (itemName) {
			case LEARN_MORE_GET_STARTED:
				itemId = DashBoardPageId.Welcome_LearnMore_getStarted;
				break;
			case LEARN_MORE_VIDEOS:
				itemId = DashBoardPageId.Welcome_LearnMore_Videos;
				break;
			case LEARN_MORE_SERVICE_OFFERINGS:
				itemId = DashBoardPageId.Welcome_LearnMore_ServiceOffering;
				break;
		}
		return itemId;
	}

	/**
	 * Get jet's option xpath according to "select" id and "option" id
	 *
	 * @param driver
	 * @param selectId
	 * @param optionId
	 * @return
	 * @throws Exception
	 */
	private String getOptionXpath(WebDriver driver, String selectId, String optionId) throws Exception
	{
		String optionXpath;
		WebElement li = driver.getWebDriver().findElement(By.id(optionId));
		List<WebElement> list = driver.getWebDriver().findElements(By.xpath("//select[@id='" + selectId + "']/option"));
		//get the index of option in select dropdown
		int index = list.indexOf(li);
		//get option's xpath generated by jet
		optionXpath = "//ul[@id='oj-listbox-results-" + selectId + "']/li[" + (index + 1) + "]/div";
		return optionXpath;
	}

	/**
	 * Get wrapper id of each service
	 *
	 * @param serviceName
	 *            APM | LA | ITA | dashboards | dataExplorers
	 * @return
	 */
	private String getServiceWrapperId(String serviceName)
	{
		String serviceWrapperId = null;
		switch (serviceName) {
			case SERVICE_NAME_APM:
				serviceWrapperId = DashBoardPageId.Welcome_APMLinkCSS;
				break;
			case SERVICE_NAME_LA:
				serviceWrapperId = DashBoardPageId.Welcome_LALinkCSS;
				break;
			case SERVICE_NAME_ITA:
				serviceWrapperId = DashBoardPageId.Welcome_ITALinkID;
				break;
			case SERVICE_NAME_DASHBOARDS:
				serviceWrapperId = DashBoardPageId.Welcome_DashboardsLinkID;
				break;
			case SERVICE_NAME_DATA_EXPLORERS:
				serviceWrapperId = DashBoardPageId.Welcome_DataExp;
				break;
		}
		return serviceWrapperId;
	}
}