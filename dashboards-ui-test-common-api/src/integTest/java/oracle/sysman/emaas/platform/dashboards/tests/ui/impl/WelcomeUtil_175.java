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

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class WelcomeUtil_175 extends WelcomeUtil_171
{
	public final static String ITA_PERFORMANCE_ANALYTICS_APPLICATION_SERVER = "performanceAnalyticsApplicationServer";

	public final static String ITA_RESOURCE_ANALYTICS_MIDDLEWARE = "resourceAnalyticsMiddleware";

	public final static String ITA_RESOURCE_ANALYTICS_HOST = "resourceAnalyticsHost";

	public final static String ITA_APPLICATION_PERFORMANCE_ANALYTICS = "applicationPerformanceAnalytic";

	public final static String ITA_AVAILABILITY_ANALYTICS = "availabilityAnalytics";

	@Override
	public void visitITA(WebDriver driver, String selection)
	{
		driver.getLogger().info("Visiting ITA-" + selection + " from Welcome Page...");

		Validator
		.fromValidValues("ITASelection", selection, ITA_DEFAULT, ITA_PERFORMANCE_ANALYTICS_DATABASE,
				ITA_PERFORMANCE_ANALYTICS_APPLICATION_SERVER, ITA_RESOURCE_ANALYTICS_DATABASE,
				ITA_RESOURCE_ANALYTICS_MIDDLEWARE, ITA_RESOURCE_ANALYTICS_HOST, ITA_APPLICATION_PERFORMANCE_ANALYTICS,
				ITA_AVAILABILITY_ANALYTICS, ITA_DATA_EXPLORER);

		WaitUtil.waitForPageFullyLoaded(driver);

		if (ITA_DEFAULT.equals(selection)) {
			driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_ITALINKID);
			driver.click("id=" + DashBoardPageId.WELCOME_ITALINKID);
		}
		else {
			String eleXpath = null;
			driver.waitForElementPresent("id=oj-select-choice-" + DashBoardPageId.WELCOME_ITA_SELECTID);
			driver.click("id=oj-select-choice-" + DashBoardPageId.WELCOME_ITA_SELECTID);
			switch (selection) {
				case ITA_PERFORMANCE_ANALYTICS_DATABASE:
					eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
							DashBoardPageId.WELCOME_ITA_PADATABASE);
					break;
				case ITA_PERFORMANCE_ANALYTICS_APPLICATION_SERVER:
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
				case ITA_RESOURCE_ANALYTICS_HOST:
					eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID, DashBoardPageId.WELCOME_ITA_RA_HOST);
					break;
				case ITA_APPLICATION_PERFORMANCE_ANALYTICS:
					eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID, DashBoardPageId.WELCOME_ITA_PANALYTIC);
					break;
				case ITA_AVAILABILITY_ANALYTICS:
					eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
							DashBoardPageId.WELCOME_ITA_AVANALYTIC);
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
	
	/**
	 * Visit "Infrustructure Monitoring" in welcome
	 * 
	 * @param driver
	 * @
	 */
	@Override
	public void visitInfraMonitoring(WebDriver driver) 
	{
		driver.getLogger().info("Visit Infrastructure Monitoring from Welcome page..");
		WaitUtil.waitForPageFullyLoaded(driver);
		
		driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_INFRAMONITORINGID);
		driver.click("id=" + DashBoardPageId.WELCOME_INFRAMONITORINGID);
	}
	
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#visitCompliance(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitCompliance(WebDriver driver)
	{
		driver.getLogger().info("Visiting Compliance from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_COMPLIANCEID);
		driver.click("id=" + DashBoardPageId.WELCOME_COMPLIANCEID);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#visitOrchestration(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitOrchestration(WebDriver driver)
	{
		driver.getLogger().info("Visiting Orchestration from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_ORCHESTRATIONID);
		driver.click("id=" + DashBoardPageId.WELCOME_ORCHESTRATIONID);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#visitSecurity(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitSecurity(WebDriver driver)
	{
		driver.getLogger().info("Visiting Security from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_SECURITYANALYTICSID);
		driver.click("id=" + DashBoardPageId.WELCOME_SECURITYANALYTICSID);
	}	

	@Override
	public boolean isServiceExistedInWelcome(WebDriver driver, String serviceName)
	{
		driver.getLogger().info("Start to check if service: " + serviceName + " is existed in welcome page...");

		Validator.fromValidValues("serviceName", serviceName, SERVICE_NAME_APM, SERVICE_NAME_LA, SERVICE_NAME_ITA,
				SERVICE_NAME_INFRA_MONITORING, SERVICE_NAME_COMPLIANCE, SERVICE_NAME_SECURITY_ANALYTICS, SERVICE_NAME_ORCHESTRATION, SERVICE_NAME_DASHBOARDS, SERVICE_NAME_DATA_EXPLORERS);

		WaitUtil.waitForPageFullyLoaded(driver);
		
		boolean isExisted = false;
		String serviceWrapperId = getServiceWrapperId(serviceName);
		String xpath = "//*[@id='"
				+ serviceWrapperId
				+ "']/div[@class='service-box-wrapper']/div[@class='landing-home-box-content']/div[@class='landing-home-box-content-head']";

		String nameExpected = getExpectedText(serviceName);
		if ("Compliance".equals(nameExpected)){
			//in 1.13.0, compliance name is changed, but we didn't update api version, so have to support both old name and new name here
			String newNameExpected = "Configuration and Compliance";
			isExisted = driver.isDisplayed(xpath) && (driver.isTextPresent(nameExpected, xpath) || driver.isTextPresent(newNameExpected, xpath));
		}else{
			isExisted = driver.isDisplayed(xpath) && driver.isTextPresent(nameExpected, xpath);
		}
		driver.getLogger().info(
				"Check of service: " + serviceName + " existence in welcome page is finished! Result: " + isExisted);
		return isExisted;
		
	}
}
