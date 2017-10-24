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

import java.util.ArrayList;
import java.util.logging.Logger;

import oracle.sysman.emaas.platform.dashboards.tests.ui.EntitySelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;


public class GlobalContextUtil_1180 extends GlobalContextUtil_1170
{	
	public static final String GLBCTXTBUTTON = "//div[@id='topologyButtonWrapper']/span/label";
	
	@Override
	public String getGlobalContextName(WebDriver driver)
	{
		Logger logger = driver.getLogger();
		
		if (!EntitySelectorUtil.validateReadOnlyMode(driver, logger))
		{
			//Assert.assertTrue(false, "This method is not available in the current version, please use EntitySelectorUtil.getPillContents(WebDriver driver, Logger logger)");
			driver.getLogger().warning("This method not supported in the current version, please use EntitySelectorUtil.getPillContents(WebDriver driver, Logger logger)");
		
			ArrayList<String> gcname = EntitySelectorUtil.getPillContents(driver, logger);
			
			if (gcname.size()>0)
			{
				driver.getLogger().info("return the first global context pill: " + gcname.get(0));
				return gcname.get(0);
			}
			else
			{
				return "";
			}
		}
		else
		{
			driver.getLogger().info("It is global context read only mode");
			return driver.getText(DashBoardPageId.EntSelReadOnlyPill);
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#showTopology(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void showTopology(WebDriver driver)
	{
		if (driver.isDisplayed(GLBCTXTPLDIV)) {
			driver.getLogger().info("the topology already got displayed");
		}
		else {
			driver.click(GLBCTXTBUTTON);
			
		}
	}
	
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#hideTopology(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void hideTopology(WebDriver driver)
	{
		if (driver.isDisplayed(GLBCTXTPLDIV)) {
			driver.click(GLBCTXTBUTTON);
		}
		else {
			driver.getLogger().info("the topology already got hidden");
		}
	}
}