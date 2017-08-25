/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.test.ui.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.testng.annotations.Test;

public class OnboardV4Tenant
{
	public static String T_WORK = System.getenv("T_WORK");

	public static String getInstanceID()
	{
		return "instance1";
	}

	public static String getTenantID()
	{
		return "emcpdftenant";
	}

	WebDriver webdriver;

	String currentTestName;

	@Test(groups = "OnboardTenant-group")
	public void onboardv4Tenant() throws InterruptedException, IOException, Exception
	{

		WebDriver webd = WebDriverUtils.initWebDriver("currentTestName");
		webd.getLogger().info("Start of tenant onboading script");
		webd.getLogger().info("Tenant Id is  emcpdftenant, Service Name is :OMC , Service Edition is standard");
		String cmd = "sh " + T_WORK + "/testClasses/tenant_onboard.sh -propertiesFile " + T_WORK
				+ "/emaas.properties.log -tenantId emcpdftenant -service_type OMC -service_edition Standard -instance instance1";

		webd.getLogger().info("COMMAND IS " + cmd);
		Runtime run = Runtime.getRuntime();
		Process proc = run.exec(cmd);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		// read the output from the command
		String s = null;
		while ((s = stdInput.readLine()) != null) {
			webd.getLogger().info(s);
		}
		// read any errors from the attempted command
		while ((s = stdError.readLine()) != null) {
			webd.getLogger().info(s);
		}
		webd.getLogger().info("COMMAND EXECUTION OVER");
	}

}