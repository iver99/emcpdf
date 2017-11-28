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

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author cawei
 *
 */
public class DashboardBuilderUtil_1250 extends DashboardBuilderUtil_1230
{
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#toggleHTMLWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void toggleHTMLWidget(WebDriver driver)
	{
     driver.waitForElementPresent("CSS="+".cke_editable");
		
	}
	
	
	
}