/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.test.ui.util;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.GlobalContextUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.testng.Assert;

public class TestGlobalContextUtil
{
	public static void CreateDashboardWithGC(WebDriver driver, String dashboardName, String gccontent, String dashboardType)
	{
		driver.getLogger().info("Go to Dashobard Home page");
		BrandingBarUtil.visitDashboardHome(driver);
		
		driver.getLogger().info("Switch to Grid View");
		DashboardHomeUtil.gridView(driver);
		
		driver.getLogger().info("Create a dashboard/dashboard set");
		if(dashboardType.equals(DashboardHomeUtil.DASHBOARD))
		{
			DashboardHomeUtil.createDashboard(driver, dashboardName, null, DashboardHomeUtil.DASHBOARD);		
			DashboardBuilderUtil.verifyDashboard(driver, dashboardName, null, false);
			
			driver.getLogger().info("Verify the global context");
			Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(driver), "The global context exists in builder Page");
			
			driver.getLogger().info("Respect GC");
			DashboardBuilderUtil.respectGCForEntity(driver);
			//Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(driver), "The global context NOT exists in builder Page");
			//String displaygc = GlobalContextUtil.getGlobalContextName(driver).trim();
			//driver.getLogger().info(displaygc);
			//driver.getLogger().info(gccontent);
			//Assert.assertEquals(displaygc, gccontent);
		}
		else if(dashboardType.equals(DashboardHomeUtil.DASHBOARDSET))
		{
			DashboardHomeUtil.createDashboard(driver, dashboardName, null, DashboardHomeUtil.DASHBOARDSET);		
			DashboardBuilderUtil.verifyDashboardSet(driver, dashboardName);
			
			driver.getLogger().info("Verify the global context");
			Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(driver), "The global context exists in builder Page");
		}
		else
		{
			Assert.assertTrue(false, "Not correct dashboard type: "+ dashboardType);
		}
	}
	
	public static void visitServiceWithGC(WebDriver driver, String servicename, String gccontent)
	{
		BrandingBarUtil.visitApplicationCloudService(driver, servicename);
		//Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(driver), "The global context exists in "+servicename+" Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(driver), gccontent);		
	}
	
	/**
	 * @param pageType home|dashboards|welcome
	 **/
	public static void visitDashboardPageWithGC(WebDriver driver, String pageType)
	{
		//visit home page
		switch(pageType)
		{
			case "home":		
				BrandingBarUtil.visitMyHome(driver);
				Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(driver), "The global context doesn't exist in DashboardHome");
				break;
			case "dashboards":
				BrandingBarUtil.visitDashboardHome(driver);
				Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(driver), "The global context doesn't exist in Dashboard Home page");
				break;			
			case "welcome":
				BrandingBarUtil.visitWelcome(driver);
				Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(driver), "The global context doesn't exist in Welcome page");
				break;
			default:
		}			
	}
	
	public static void visitOOBWithGC(WebDriver driver, String dbname, String gccontent)
	{
		driver.getLogger().info("Go to Dashobard Home page");
		BrandingBarUtil.visitDashboardHome(driver);
		
		driver.getLogger().info("Switch to Grid View");
		DashboardHomeUtil.gridView(driver);
		
		driver.getLogger().info("Open a OOB dashboard/dashboard set");
		DashboardHomeUtil.selectDashboard(driver, dbname);
		
		driver.getLogger().info("Start to verify GC in OOB");
		String currenturl = driver.getCurrentUrl();
		if(currenturl.contains("builder.html"))
		{
			Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(driver), "The global context exists in OOB Dashboard Set"+ dbname);			
		}
		else
		{
			//Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(driver), "The global context doesn't exist in OO Dashboard"+ dbname);
			//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(driver), gccontent);
		}
	}
	
	/**
	 * 
	 * @param driver
	 * @param ITASection
	 * 		default | performanceAnalyticsDatabase | performanceAnalyticsApplicationServer | 		
	 * 		resourceAnalyticsDatabase | resourceAnalyticsMiddleware | resourceAnalyticsHost | 
	 *  	applicationPerformanceAnalytic | availabilityAnalytics | dataExplorer
	 * @param gccontent
	 */
	public static void visitITAWithGC(WebDriver driver, String ITASection, String gccontent)
	{
		BrandingBarUtil.visitWelcome(driver);
		WelcomeUtil.visitITA(driver, ITASection);		
		
		//Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(driver), "The global context not exists in ITA Page");
		//String displaygc= GlobalContextUtil.getGlobalContextName(driver).trim();
		//driver.getLogger().info(displaygc);
		//driver.getLogger().info(gccontent);
		//Assert.assertEquals(displaygc, gccontent);
	}
	
	public static void visitApplicationVisualAnalyzer(WebDriver driver, String visualAnalyzerLinkName, String gccontent )
	{
		BrandingBarUtil.visitApplicationVisualAnalyzer(driver, visualAnalyzerLinkName);
		//Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(driver), "The global context not exists in "+visualAnalyzerLinkName+" Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(driver), gccontent);		
	}
	
	public static void openWidgetWithGC(WebDriver driver, String dbName, String widgetName, String gccontent)
	{
		//visit home page
		BrandingBarUtil.visitDashboardHome(driver);
		DashboardHomeUtil.gridView(driver);
		DashboardHomeUtil.createDashboard(driver, dbName, null);
		DashboardBuilderUtil.verifyDashboard(driver, dbName, null, false);
		
		driver.getLogger().info("Respect GC");
		DashboardBuilderUtil.respectGCForEntity(driver);
		
		DashboardBuilderUtil.addWidgetToDashboard(driver, widgetName);
		DashboardBuilderUtil.saveDashboard(driver);	
		DashboardBuilderUtil.openWidget(driver, widgetName);
		
		driver.switchToWindow();
		//driver.getLogger().info("Wait for the widget loading....");
		//wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='save_widget_btn']")));
		//driver.waitForElementPresent(PageId.SAVEBUTTON_UDE, WaitUtil.WAIT_TIMEOUT);
		
		//verify the open url
		DashBoardUtils.verifyURL_WithPara(driver, "omcCtx=");	
		//Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(driver),"The global context isn't exists in widget: "+ widgetName );
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(driver), gccontent);		
	}

	public static void deleteDashboardWithGC(WebDriver driver, String newdbName, String existdbname, String gccontent)
	{
		//visit home page
		BrandingBarUtil.visitDashboardHome(driver);
		DashboardHomeUtil.gridView(driver);
		DashboardHomeUtil.createDashboard(driver, newdbName, null);
		DashboardBuilderUtil.verifyDashboard(driver, newdbName, null, false);
		
		driver.getLogger().info("Respect GC");
		DashboardBuilderUtil.respectGCForEntity(driver);
				
		DashboardBuilderUtil.deleteDashboard(driver);

		//verify omcCtx exist in the url
		String url1 = driver.getCurrentUrl();
		driver.getLogger().info("start to verify omcCtx exist in the dashboard home url");
		Assert.assertTrue(url1.contains("omcCtx="), "The global context infomation in URL is lost");
		
		//open an existed dashboard which respect gc
		driver.getLogger().info("open the dashboard: "+ existdbname);
		DashboardHomeUtil.selectDashboard(driver, existdbname);		
		
		String url2 = driver.getCurrentUrl();		
		driver.getLogger().info("start to verify omcCtx exist in the OOB dashboard url");	
		Assert.assertTrue(url2.contains("omcCtx="), "The global context infomation in URL is lost in OOB dashboard page");	
		//Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(driver),"The global context exists in opened dashboard");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(driver), gccontent);	
	}
	
	public static void deleteDashboardSetWithGC(WebDriver driver, String dbsetName, String dbName, String gccontent)
	{
		BrandingBarUtil.visitDashboardHome(driver);
		DashboardHomeUtil.gridView(driver);
		DashboardHomeUtil.createDashboardSet(driver, dbsetName, null);
		DashboardBuilderUtil.verifyDashboardSet(driver, dbsetName);
		
		DashboardBuilderUtil.createDashboardInsideSet(driver, dbName, null);
		
		DashboardBuilderUtil.deleteDashboardSet(driver);
	
		//verify omcCtx exist in the url
		String url1 = driver.getCurrentUrl();
		driver.getLogger().info("start to verify omcCtx exist in the dashboard home url");
		Assert.assertTrue(url1.contains("omcCtx="), "The global context infomation in URL is lost");
		
		//open the dashboard, eg: Host Operations in the home page, then verify omcCtx exist in the url
		driver.getLogger().info("open the dashboard created in dashboard set");
		DashboardHomeUtil.selectDashboard(driver, dbName);
		
		driver.getLogger().info("Respect GC");
		DashboardBuilderUtil.respectGCForEntity(driver);
		
		String url2 = driver.getCurrentUrl();		
		driver.getLogger().info("start to verify omcCtx exist in the OOB dashboard url");	
		Assert.assertTrue(url2.contains("omcCtx="), "The global context infomation in URL is lost in OOB dashboard page");	
		
		//Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(driver),"The global context exists in opened dashboard");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(driver), gccontent);
	}

	public static void switchEntity(WebDriver driver, String dbName, String gccontent)
	{
		//visit home page
		BrandingBarUtil.visitDashboardHome(driver);
		DashboardHomeUtil.gridView(driver);
		DashboardHomeUtil.createDashboard(driver, dbName, null);
		DashboardBuilderUtil.verifyDashboard(driver, dbName, null, false);
		
		DashboardBuilderUtil.addWidgetToDashboard(driver, "Analytics Line");
		DashboardBuilderUtil.saveDashboard(driver);	
		
		//find "GC entities" radio button, then select it
		DashboardBuilderUtil.respectGCForEntity(driver);
		
		//Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(driver), "The global context isn't exists when select GC entities filter");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(driver), gccontent);
		//Assert.assertTrue(webd.isDisplayed(PageId.ENTITYBUTTON),"All Entities button isn't display on the top-left cornor, when select GC entities filter");
	
		//find "Use dashboard entities" radio button, then select it
		DashboardBuilderUtil.showEntityFilter(driver, true);	
		
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(driver),"The global context isn't exists when select dashboard entities filter");
		Assert.assertTrue(driver.isDisplayed(PageId.ENTITYBUTTON),"All Entities button isn't display on the top-left cornor, when select dashboard entities");
		
		//find "Use entities defined by content" radio button, then select it
		DashboardBuilderUtil.showEntityFilter(driver,false);
		
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(driver),"The global context isn't exists when select disable entities filter");
		Assert.assertFalse(driver.isDisplayed(PageId.ENTITYBUTTON), "All Entities button is present on the top-left cornor, when select disable entities fileter");	
	}
}
