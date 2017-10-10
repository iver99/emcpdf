/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.testsdk.util;

import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import org.testng.Assert;

public class ZDTUtil
{
	private static WebDriver driver;

	public static void loadWebDriver(WebDriver webDriver) {
		driver = webDriver;
	}

	public static String generateTimeStamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}

	public static void apmOobExist(WebDriver driver)
	{
		driver.getLogger().info("Wait for dashboards loading...");
		DashboardHomeUtil.waitForDashboardPresent(driver, "Application Performance Monitoring");

		driver.getLogger().info("Verify below APM OOB dashboard exist...");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Application Performance Monitoring"));
	}

	public static void itaOobExist(WebDriver driver)
	{
		driver.getLogger().info("Wait for dashboards loading...");
		DashboardHomeUtil.waitForDashboardPresent(driver, "Performance Analytics: Database");

		driver.getLogger().info("Verify below IT Analytics OOB dashboard Set and Dashboards exist...");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Exadata Health"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Enterprise Health"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "UI Gallery"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Application Performance Analytics"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Availability Analytics"));

		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics Application Server"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics: Database"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Database"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Host"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Middleware"));
	}

	public static void laOobExist(WebDriver driver)
	{
		driver.getLogger().info("Wait for dashboards loading...");
		DashboardHomeUtil.waitForDashboardPresent(driver, "Database Operations");

		driver.getLogger().info("Verify below Log Analytics OOB dashboards exist...");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Database Operations"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Host Operations"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Middleware Operations"));
	}

	public static void securityOobExist(WebDriver driver)
	{
		driver.getLogger().info("Wait for dashboards loading...");
		DashboardHomeUtil.waitForDashboardPresent(driver, "DNS");

		driver.getLogger().info("Verify below Security OOB dashboards exist...");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "DNS"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Firewall"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Oracle Database Security"));
	}

	public static void orchestrationOobExist(WebDriver driver)
	{
		driver.getLogger().info("Wait for dashboards loading...");
		DashboardHomeUtil.waitForDashboardPresent(driver, "Orchestration Workflows");

		driver.getLogger().info("Verify below Orchestration OOB dashboard exist...");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Orchestration Workflows"));
	}
}
