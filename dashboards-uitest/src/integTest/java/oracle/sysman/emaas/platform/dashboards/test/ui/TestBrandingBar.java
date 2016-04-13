package oracle.sysman.emaas.platform.dashboards.test.ui;

import org.testng.annotations.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.*;

import java.util.Set;
import org.testng.Assert;

/**
 *  @version
 *  @author  charles.c.chen
 *  @since   release specific (what release of product did this appear in)
 */

public class TestBrandingBar extends LoginAndLogout{

	public void initTest(String testName) throws Exception
	{
		login(this.getClass().getName()+"."+testName);
		DashBoardUtils.loadWebDriver(webd);
	}
	
	@Test
	public void testHomeLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomeLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		//Home link
		webd.click(PageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcpdfui/welcome.html");
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}
	
	@Test
	public void testDashBoardHomeLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testDashBoardHomeLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		//dashboardHome link
		webd.click(PageId.DashBoardHomeLinkID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcpdfui/home.html");
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}
	
	@Test
	public void testITALink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testITALink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		//IT Analytics link,check checkbox
		webd.click(PageId.ITALinkID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcpdfui/home.html?filter=ita");
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);		
	}
	
	@Test
	public void testLALink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testLALink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		//Log Analytics link
		webd.click(PageId.LALinkID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emlacore/html/log-analytics-search.html");
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}
	
	@Test
	public void testAPMLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAPMLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		//APM link
		webd.click(PageId.APMLinkID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"apmUi/index.html");
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}
	
	@Test
	public void testLogLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testLogLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		//Log link
		webd.click(PageId.LOGLinkID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emlacore/html/log-analytics-search.html");
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}
	
	@Test
	public void testSearchLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testSearchLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		//Target link
		webd.click(PageId.SearchLinkID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		String sub_str = url.substring(url.indexOf("emsaasui")+9);
		Assert.assertEquals(sub_str.substring(0,23),"emcta/ta/analytics.html");
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}
	
	//the below test removed from sprint47_df	
	/*
	@Test
	public void testAWRLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAWRLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//AWR Analytics link		
		webd.click(DashBoardPageId.AWRALinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcitas/flex-analyzer/html/displaying/new-chart-config.html");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	}
	*/
	
	@Test
	public void testAnalyzeLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAnalyzeLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		//Analyze link
		webd.click(PageId.AnalyzeLinkID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcitas/flex-analyzer/html/displaying/new-chart-config.html");
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}

	@Test
	public void testAgentsLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAgentsLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		//Agents link
		webd.click(PageId.AgentsLinkID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"tenantmgmt/services/customersoftware");
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}
	
	@Test
	public void testAdminLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAdminLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		//IT Analytics Administration link
		webd.click(PageId.ITA_Admin_LinkID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcitas/warehouseadmin/html/admin-sources.html");
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}	
}
