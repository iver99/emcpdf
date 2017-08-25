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

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WelcomeUtil_171 extends WelcomeUtil_Version implements IWelcomeUtil
{
	public final static String DATA_EXPLORERS_LOG = "log";

	public final static String DATA_EXPLORERS_ANALYZE = "analyze";

	public final static String DATA_EXPLORERS_SEARCH = "search";

	public final static String ITA_DEFAULT = "default";

	public final static String ITA_PERFORMANCE_ANALYTICS_DATABASE = "performanceAnalyticsDatabase";

	public final static String ITA_PERFORMANCE_ANALYTICS_MIDDLEWARE = "performanceAnalyticsMiddleware";

	public final static String ITA_RESOURCE_ANALYTICS_DATABASE = "resourceAnalyticsDatabase";

	public final static String ITA_RESOURCE_ANALYTICS_MIDDLEWARE = "resourceAnalyticsMiddleware";

	public final static String ITA_DATA_EXPLORER_ANALYZE = "dataExplorerAnalyze";

	public final static String ITA_DATA_EXPLORER = "dataExplorer";

	public final static String LEARN_MORE_GET_STARTED = "getStarted";

	public final static String LEARN_MORE_VIDEOS = "videos";

	public final static String LEARN_MORE_SERVICE_OFFERINGS = "serviceOfferings";

	public final static String SERVICE_NAME_APM = "APM";
	public static final String SERVICE_NAME_DATA_EXPLORERS_EXPECTEDNAME = "Data Explorers";
	public final static String SERVICE_NAME_LA = "LA";
	public final static String SERVICE_NAME_ITA = "ITA";
	public static final String SERVICE_NAME_INFRA_MONITORING = "infraMonitoring";
	public static final String SERVICE_NAME_COMPLIANCE = "compliance";
	public static final String SERVICE_NAME_SECURITY_ANALYTICS = "securityAnalytics";
	public static final String SERVICE_NAME_ORCHESTRATION = "orchestration";
	public static final String SERVICE_NAME_DASHBOARDS = "dashboards";
	public static final String SERVICE_NAME_DATA_EXPLORERS = "dataExplorers";;

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#dataExplorers(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void dataExplorers(WebDriver driver, String selection)
	{
		String eleXpath = null;
		driver.getLogger().info("Visiting Data Explorer-" + selection + " from Welcome Page...");

		Validator.fromValidValues("dataExplorersSelection", selection, DATA_EXPLORERS_LOG, DATA_EXPLORERS_ANALYZE,
				DATA_EXPLORERS_SEARCH);

		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent("id=oj-select-choice-" + DashBoardPageId.WELCOME_DATAEXP_SELECTID);
		driver.click("id=oj-select-choice-" + DashBoardPageId.WELCOME_DATAEXP_SELECTID);

		switch (selection) {
			case DATA_EXPLORERS_LOG:
				eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_DATAEXP_SELECTID, DashBoardPageId.WELCOME_DATAEXP_LOG);
				break;
			case DATA_EXPLORERS_ANALYZE:
				eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_DATAEXP_SELECTID,
						DashBoardPageId.WELCOME_DATAEXP_ANALYZE);
				break;
			case DATA_EXPLORERS_SEARCH:
				eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_DATAEXP_SELECTID,
						DashBoardPageId.WELCOME_DATAEXP_SEARCH);
				break;
			default:
				break;

		}
		driver.getWebDriver().findElement(By.xpath(eleXpath)).click();
		driver.takeScreenShot();
		driver.savePageToFile();
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

		WaitUtil.waitForPageFullyLoaded(driver);

		boolean isExisted = false;
		String itemId = getLearnMoreItemId(itemName);
		String nameExpected = getExpectedText(itemName);

		if (driver.isDisplayed("id=" + itemId) && driver.isTextPresent(nameExpected, "id=" + itemId)) {
			isExisted = true;
		}
		driver.getLogger().info("Check of learn more item: " + itemName + " existence is finished! Result: " + isExisted);
		return isExisted;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#isServiceExistedInWelcome(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isServiceExistedInWelcome(WebDriver driver, String serviceName)
	{
		driver.getLogger().info("Start to check if service: " + serviceName + " is existed in welcome page...");

		Validator.fromValidValues("serviceName", serviceName, SERVICE_NAME_APM, SERVICE_NAME_LA, SERVICE_NAME_ITA,
				SERVICE_NAME_INFRA_MONITORING, SERVICE_NAME_COMPLIANCE, SERVICE_NAME_SECURITY_ANALYTICS,
				SERVICE_NAME_ORCHESTRATION, SERVICE_NAME_DASHBOARDS, SERVICE_NAME_DATA_EXPLORERS);

		WaitUtil.waitForPageFullyLoaded(driver);

		boolean isExisted = false;
		String serviceWrapperId = getServiceWrapperId(serviceName);
		String xpath = "//*[@id='"
				+ serviceWrapperId
				+ "']/div[@class='service-box-wrapper']/div[@class='landing-home-box-content']/div[@class='landing-home-box-content-head']";

		String nameExpected = getExpectedText(serviceName);

		if (driver.isDisplayed(xpath) && driver.isTextPresent(nameExpected, xpath)) {
			isExisted = true;
		}
		driver.getLogger().info(
				"Check of service: " + serviceName + " existence in welcome page is finished! Result: " + isExisted);
		return isExisted;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#learnMoreHow(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void learnMoreHow(WebDriver driver)
	{
		driver.getLogger().info("Visiting 'Learn More-How to get started' from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.click("id=" + DashBoardPageId.WELCOME_LEARNMORE_GETSTARTED);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#learnMoreServiceOffering(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void learnMoreServiceOffering(WebDriver driver)
	{
		driver.getLogger().info("Visiting 'Learn More-Service Offerings' from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.click("id=" + DashBoardPageId.WELCOME_LEARNMORE_SERVICEOFFERING);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#learnMoreVideo(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void learnMoreVideo(WebDriver driver)
	{
		driver.getLogger().info("Visiting 'Learn More-Videos' from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.click("id=" + DashBoardPageId.WELCOME_LEARNMORE_VIDEOS);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#visitAPM(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitAPM(WebDriver driver)
	{
		driver.getLogger().info("Visit APM from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.click("id=" + DashBoardPageId.WELCOME_APMLINKCSS);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#visitCompliance(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitCompliance(WebDriver driver)
	{
		driver.getLogger().warning("Visit Compliance from Welcome Page is not supported in 1.7.1");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#visitDashboards(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitDashboards(WebDriver driver)
	{
		driver.getLogger().info("Visit Dashboards from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.click("id=" + DashBoardPageId.WELCOME_DASHBOARDSLINKID);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#visitInfraMonitoring(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitInfraMonitoring(WebDriver driver)
	{
		driver.getLogger().warning("Visit Infrastructure Monitoring from Welcome Page is not supported in 1.7.1");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#visitITA(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void visitITA(WebDriver driver, String selection)
	{
		driver.getLogger().info("Visiting ITA-" + selection + " from Welcome Page...");

		Validator.fromValidValues("ITASelection", selection, ITA_DEFAULT, ITA_PERFORMANCE_ANALYTICS_DATABASE,
				ITA_PERFORMANCE_ANALYTICS_MIDDLEWARE, ITA_RESOURCE_ANALYTICS_DATABASE, ITA_RESOURCE_ANALYTICS_MIDDLEWARE,
				ITA_DATA_EXPLORER_ANALYZE, ITA_DATA_EXPLORER);

		WaitUtil.waitForPageFullyLoaded(driver);

		if (ITA_DEFAULT.equals(selection)) {
			driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_ITALINKID);
			driver.click("id=" + DashBoardPageId.WELCOME_ITALINKID);			
		}
		else {
			String eleXpath = null;
			driver.click("id=oj-select-choice-" + DashBoardPageId.WELCOME_ITA_SELECTID);
			switch (selection) {
				case ITA_PERFORMANCE_ANALYTICS_DATABASE:
					eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
							DashBoardPageId.WELCOME_ITA_PADATABASE);
					break;
				case ITA_PERFORMANCE_ANALYTICS_MIDDLEWARE:
					eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
							DashBoardPageId.WELCOME_ITA_PAMIDDLEWARE);
					break;
				case ITA_RESOURCE_ANALYTICS_DATABASE:
					eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
							DashBoardPageId.WELCOME_ITA_RADATABASE);
					break;
				case ITA_RESOURCE_ANALYTICS_MIDDLEWARE:
					eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
							DashBoardPageId.WELCOME_ITA_RAMIDDLEWARE);
					break;
				case ITA_DATA_EXPLORER_ANALYZE:
					eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID, DashBoardPageId.WELCOME_ITA_DEANALYZE);
					break;
				case ITA_DATA_EXPLORER:
					eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID, DashBoardPageId.WELCOME_ITA_DE);
					break;
				default:
					break;
			}
			driver.click(eleXpath);			
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#visitLA(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitLA(WebDriver driver)
	{
		driver.getLogger().info("Visiting LA from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.click("id=" + DashBoardPageId.WELCOME_LALINKCSS);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#visitOrchestration(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitOrchestration(WebDriver driver)
	{
		driver.getLogger().warning("Visit Orchestration from Welcome Page is not supported in 1.7.1");

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#visitSecurity(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitSecurity(WebDriver driver)
	{
		driver.getLogger().warning("Visit Security from Welcome Page is not supported in 1.7.1");

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
				itemId = DashBoardPageId.WELCOME_LEARNMORE_GETSTARTED;
				break;
			case LEARN_MORE_VIDEOS:
				itemId = DashBoardPageId.WELCOME_LEARNMORE_VIDEOS;
				break;
			case LEARN_MORE_SERVICE_OFFERINGS:
				itemId = DashBoardPageId.WELCOME_LEARNMORE_SERVICEOFFERING;
				break;
			default:
				break;
		}
		return itemId;
	}

	/**
	 * Get text of each service or item shown in welcome page.
	 *
	 * @param serviceName
	 *            APM | LA | ITA | infraMonitoring | compliance | securityAnalytics | orchestration |dashboards | dataExplorers |
	 *            getStarted | videos | serviceOfferings
	 * @return
	 */
	protected String getExpectedText(String serviceName)
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
			case SERVICE_NAME_INFRA_MONITORING:
				expectedName = "Infrastructure Monitoring";
				break;
			case SERVICE_NAME_COMPLIANCE:
				expectedName = "Compliance";
				break;
			case SERVICE_NAME_SECURITY_ANALYTICS:
				expectedName = "Security Monitoring and Analytics";
				break;
			case SERVICE_NAME_ORCHESTRATION:
				expectedName = "Orchestration";
				break;
			case SERVICE_NAME_DASHBOARDS:
				expectedName = "Dashboards";
				break;
			case SERVICE_NAME_DATA_EXPLORERS:
				expectedName = SERVICE_NAME_DATA_EXPLORERS_EXPECTEDNAME;
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
			default:
				break;
		}
		return expectedName;
	}

	/**
	 * Get jet's option xpath according to "select" id and "option" id
	 *
	 * @param driver
	 * @param selectId
	 * @param optionId
	 * @return @
	 */
	protected String getOptionXpath(WebDriver driver, String selectId, String optionId)
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
	 *            APM | LA | ITA | infraMonitoring | compliance | securityAnalytics | orchestration | dashboards | dataExplorers
	 * @return
	 */
	protected String getServiceWrapperId(String serviceName)
	{
		String serviceWrapperId = null;
		switch (serviceName) {
			case SERVICE_NAME_APM:
				serviceWrapperId = DashBoardPageId.WELCOME_APMLINKCSS;
				break;
			case SERVICE_NAME_LA:
				serviceWrapperId = DashBoardPageId.WELCOME_LALINKCSS;
				break;
			case SERVICE_NAME_ITA:
				serviceWrapperId = DashBoardPageId.WELCOME_ITALINKID;
				break;
			case SERVICE_NAME_INFRA_MONITORING:
				serviceWrapperId = DashBoardPageId.WELCOME_INFRAMONITORINGID;
				break;
			case SERVICE_NAME_COMPLIANCE:
				serviceWrapperId = DashBoardPageId.WELCOME_COMPLIANCEID;
				break;
			case SERVICE_NAME_SECURITY_ANALYTICS:
				serviceWrapperId = DashBoardPageId.WELCOME_SECURITYANALYTICSID;
				break;
			case SERVICE_NAME_ORCHESTRATION:
				serviceWrapperId = DashBoardPageId.WELCOME_ORCHESTRATIONID;
				break;
			case SERVICE_NAME_DASHBOARDS:
				serviceWrapperId = DashBoardPageId.WELCOME_DASHBOARDSLINKID;
				break;
			case SERVICE_NAME_DATA_EXPLORERS:
				serviceWrapperId = DashBoardPageId.WELCOME_DATAEXP;
				break;
			default:
				break;
		}
		return serviceWrapperId;
	}

}
