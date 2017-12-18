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

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author cawei
 *
 */
public class DashboardBuilderUtil_1250 extends DashboardBuilderUtil_1230
{
	@Override
	public void toggleHTMLWidget(WebDriver driver)
	{
		// TODO Auto-generated method stub
		toggleHTMLWidget(driver,1);	
	}
	@Override
	public void toggleHTMLWidget(WebDriver driver,int index)
	{
		switchTextWidgetToEditMode(driver, index);	
		if(driver.isDisplayed("css=" + DashBoardPageId.LINKICONCSS))
		
		{ driver.getLogger().info("the editor is in visual mode");
		  
		  driver.click(DashBoardPageId_190.HTMLSOURCEXPATH);
			
		}
		else
		{
			driver.getLogger().info("the editor is in source mode");
			driver.click(DashBoardPageId_190.HTMLVISUALXPATH);
		}
	}
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetAlign(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, java.lang.String)
	 */
	@Override
	public void editHTMLWidgetAlign(WebDriver driver, int index, String align)
	{
		switchTextWidgetToEditMode(driver, index);	
		switch(align)
	 	{
	 		case "left":
	 			driver.getLogger().info("Click Align Left");	 
	 			driver.click("css=" + DashBoardPageId_190.ALIGNLEFTCSS);	    
	 			break;
	 		case "center":
	 			driver.getLogger().info("Click Central");	 			
	 			driver.click("css=" + DashBoardPageId_190.ALIGNCENTRALCSS);
	 			break;	
	 		case "right":
	 			driver.getLogger().info("Click Align Right");		
	 			driver.click("css=" + DashBoardPageId_190.ALIGNRIGHTCSS);
	 			break;
	 		default:
	 				break;
	 	}
				
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetAlign(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void editHTMLWidgetAlign(WebDriver driver, String align)
	{
		// TODO Auto-generated method stub
		editHTMLWidgetAlign(driver,1, align);
		
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetFont(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, java.lang.String)
	 */
	@Override
	public void editHTMLWidgetFont(WebDriver driver, int index, String fontname)
	{
		// TODO Auto-generated method stub
		switchTextWidgetToEditMode(driver, index);	

		driver.click(DashBoardPageId_190.HTMLFONTARROW);
		driver.click("//*[contains(@id, '_option') and contains(@title,'"+fontname+"')]");
		
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetFont(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void editHTMLWidgetFont(WebDriver driver, String fontname)
	{
		// TODO Auto-generated method stub
		editHTMLWidgetFont(driver,1,fontname);	
		
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetFontSize(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int)
	 */
	@Override
	public void editHTMLWidgetFontSize(WebDriver driver, int fontsize)
	{
		// TODO Auto-generated method stub
		
		editHTMLWidgetFontSize(driver, 1,fontsize);	
		
		
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetFontSize(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, int)
	 */
	@Override
	public void editHTMLWidgetFontSize(WebDriver driver, int index, int fontsize)
	{
		// TODO Auto-generated method stub
		switchTextWidgetToEditMode(driver, index);
		driver.click(DashBoardPageId_190.HTMLFONTSIZEARROW);
		driver.click("//*[contains(@id, '_option') and contains(@title,'"+fontsize+"')]");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetFontBold(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, java.lang.Boolean)
	 */
	@Override
	public void editHTMLWidgetFontBold(WebDriver driver, int index, Boolean bold)
	{
		// TODO Auto-generated method stub
		switchTextWidgetToEditMode(driver, index);	
		driver.click("css="+ DashBoardPageId_190.HTMLBOLDCSS);
		
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetFontBold(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.Boolean)
	 */
	@Override
	public void editHTMLWidgetFontBold(WebDriver driver, Boolean bold)
	{
		
		editHTMLWidgetFontBold(driver,1, bold);	
		
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editHTMLWidgetFontItalic(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.Boolean)
	 */
	@Override
	public void editHTMLWidgetFontItalic(WebDriver driver, Boolean italic)
	{
		// TODO Auto-generated method stub
		editHTMLWidgetFontItalic(driver,1, italic);	
	}

	/**
	 * @param driver
	 * @param i
	 * @param bold
	 */
	public void editHTMLWidgetFontItalic(WebDriver driver, int i, Boolean bold)
	{	
		
		editHTMLWidgetFontBold(driver,1, bold);	
		
	}
	@Override
	public void editHTMLWidgetSourceContent(WebDriver driver, int index, String content)
	{
		// TODO Auto-generated method stub
		switchTextWidgetToEditMode(driver, index);	
		toggleHTMLWidget(driver);
		driver.waitForElementPresent("xpath=" + DashBoardPageId_190.HTMLSOURCECONTENT);
		
		driver.clear("xpath=" + DashBoardPageId_190.HTMLSOURCECONTENT);
		driver.click("xpath=" + DashBoardPageId_190.HTMLSOURCECONTENT);
		driver.sendKeys("xpath=" + DashBoardPageId_190.HTMLSOURCECONTENT, content);
		WaitUtil.waitForPageFullyLoaded(driver);		
	}
	
	@Override
	public void editHTMLWidgetSourceContent(WebDriver driver,String content)
	{
		// TODO Auto-generated method stub
		editHTMLWidgetSourceContent(driver, 1, content);	
	}
	
	@Override
	public void editTextWidgetAddContent(WebDriver driver, int index, String content)
	{
		driver.getLogger().info("editTextWidgetAddContent started");
		//find current dashboard
		switchTextWidgetToEditMode(driver, index);
		
		driver.evalJavascript("$('iframe')[0].id='iframe'");
		driver.selectFrame("iframe");
		
		driver.clear("xpath="+"/html/body");
		driver.click("xpath="+"/html/body");
		driver.sendKeys("xpath="+"/html/body", content);
		WaitUtil.waitForPageFullyLoaded(driver);
		
		driver.getLogger().info("editTextWidgetAddContent completed");
	}
	
}