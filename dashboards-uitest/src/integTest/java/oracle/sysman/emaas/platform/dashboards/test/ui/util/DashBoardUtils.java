package oracle.sysman.emaas.platform.dashboards.test.ui.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.sysman.emsaas.login.LoginUtils;
import oracle.sysman.emsaas.login.PageUtils;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import org.testng.Assert;

public class DashBoardUtils {

	private static WebDriver driver;

     	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	{
		driver = webDriver;
	}

	public static void loadWebDriver(WebDriver webDriver) throws Exception
	{
		driver=webDriver;
		waitForMilliSeconds(10000);
		if(doesWebElementExist(DashBoardPageId.OverviewCloseID))  closeOverviewPage();
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		Assert.assertFalse(doesWebElementExist(DashBoardPageId.OverviewCloseID));
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		driver.takeScreenShot();
	}
	
	public static void closeOverviewPage() throws Exception
	{
		driver.getLogger().info("before clicking overview button");
		driver.click(DashBoardPageId.OverviewCloseID);
		driver.getLogger().info("after clicking overview button");
	}
	
	public static void openDBCreatePage() throws Exception
	{
	//TODO: Add page check

		if(driver==null)
			throw new Exception("The WebDriver variable has not been initialized,please initialize it first");
		driver.click(DashBoardPageId.CreateDSButtonID);
	}
	
	public static void inputDashBoardInfo(String dbName,String dbDesc) throws Exception
	{		
		
		driver.sendKeys(DashBoardPageId.DashBoardNameBoxID, dbName);
		driver.sendKeys(DashBoardPageId.DashBoardDescBoxID, dbDesc);
	}
	
	public static void clickOKButton() throws Exception
	{
		driver.click(DashBoardPageId.DashOKButtonID);
	}
	
	public static void clickLVButton() throws Exception
	{
		driver.click(DashBoardPageId.ListViewID);
	}
	
	public static void clickGVButton() throws Exception
	{
		driver.click(DashBoardPageId.GridViewID);
	}
	
	public static void clickNavigatorLink() throws Exception
	{
		driver.click(DashBoardPageId.LinkID);
	}
	
	public static void clickAddButton() throws Exception
	{		
		driver.click(DashBoardPageId.AddBtn);	
	}
	
	public static void clickAddButton_final() throws Exception
	{
		driver.click(DashBoardPageId.AddBtn);
	}
	
	public static void clickDeleteButton() throws Exception
	{
		//add verify if we are into deleting dialog
		//Assert.assertEquals("//div/p[text()=Do you want to delete the selected dashboard 'test'?", "Do you want to delete the selected dashboard 'test'?","we are not in delete dialog");
		driver.click(DashBoardPageId.DeleteBtnID_Dialog);
	}
	
	public static void clickLVDeleteButton() throws Exception
	{
		//add verify if we are into deleting dialog
		//Assert.assertEquals("//div/p[text()=Do you want to delete the selected dashboard 'test'?", "Do you want to delete the selected dashboard 'test'?","we are not in delete dialog");
		driver.click(DashBoardPageId.LV_DeleteBtnID_Dialog);
	}
	
	public static void clickCloseButton() throws Exception
	{
		driver.click(DashBoardPageId.closeBtnID);
	}
	
	public static void clickSaveButton() throws Exception
	{
		boolean exist = doesWebElementExist(DashBoardPageId.DashBoardSaveID);
		if(!exist) return;
		driver.click(DashBoardPageId.DashBoardSaveID);
	}
	
	public static void clickDashBoardName() throws Exception
	{
		driver.click(DashBoardPageId.DashBoardName);
	}
	
	public static  void addWidget(int i,String parentHandle,String dbname,String dbdesc) throws Exception
	{
		WidgetAddPage widgetAddPage;
		String widgetName;
		
		driver.getLogger().info("start to test in addWidget");
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		driver.waitForElementPresent(DashBoardPageId.WidgetAddButtonID);
		
		driver.getLogger().info("add widget button is found");
		driver.takeScreenShot();
		waitForMilliSeconds(3*DashBoardPageId.Delaytime_long);
		//verify title and desc of dashboard
		/*if( getText(DashBoardPageId.DashboardNameID) == null)
		{
			Assert.assertEquals(getText(DashBoardPageId.MDashboardNameID),"AAA_testDashboard");
			Assert.assertEquals(getText(DashBoardPageId.MDashboardDescID),"AAA_testDashBoard desc");
		}
		else*/{
			Assert.assertEquals(getText(DashBoardPageId.DashboardNameID),dbname);//"AAA_testDashboard");
			Assert.assertEquals(getText(DashBoardPageId.DashboardDescID),dbdesc);//"AAA_testDashBoard desc");
		}
		driver.getLogger().info("before clicking add widget button");		
		driver.takeScreenShot();
		driver.click(DashBoardPageId.WidgetAddButtonID);
		
		driver.getLogger().info("after clicking add widget button");		
		driver.takeScreenShot();
		widgetName = WidgetPageId.widgetName;
		
		widgetAddPage = new WidgetAddPage(driver);

		//search widget
		widgetAddPage.searchWidget(widgetName);
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);			
		
		driver.getLogger().info("before clicking widget button");		
		driver.takeScreenShot();		
		//select widget
		widgetAddPage.clickWidgetOnTable(widgetName);
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		driver.takeScreenShot();	
		clickAddButton();
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		clickCloseButton();
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);		
		driver.getLogger().info("before clicking save widget button");		
		driver.takeScreenShot();
		//save dashboard
		clickSaveButton();
		driver.getLogger().info("after clicking save widget button");		
		driver.takeScreenShot();
 		 		
	}
	
	public static  void addWidget(int i,String parentHandle,String widgetName,String dbname,String dbdesc) throws Exception
	{
		WidgetAddPage widgetAddPage,widgetAddPage2;
				
		driver.getLogger().info("start to test in addWidget");
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		driver.waitForElementPresent(DashBoardPageId.WidgetAddButtonID);
		
		driver.getLogger().info("add widget button is found");
		driver.takeScreenShot();
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//verify title and desc of dashboard
		/*if( getText(DashBoardPageId.DashboardNameID) == null)
		{
			Assert.assertEquals(getText(DashBoardPageId.MDashboardNameID),"AAA_testDashboard");
			Assert.assertEquals(getText(DashBoardPageId.MDashboardDescID),"AAA_testDashBoard desc");
		}
		else{
			Assert.assertEquals(getText(DashBoardPageId.DashboardNameID),"AAA_testDashboard");
			Assert.assertEquals(getText(DashBoardPageId.DashboardDescID),"AAA_testDashBoard desc");
		}*/
		//modify name and desc
		modifyDashboardInfo(dbname,dbdesc);
		
		driver.takeScreenShot();
		driver.click(DashBoardPageId.WidgetAddButtonID);
		
				
		driver.takeScreenShot();
		
		widgetAddPage = new WidgetAddPage(driver);

		//search widget
		widgetAddPage.searchWidget(widgetName);
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);			
		
			
		driver.takeScreenShot();		
		//select widget
		widgetAddPage.clickWidgetOnTable(widgetName);
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		driver.takeScreenShot();	
		clickAddButton();
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		widgetAddPage2 = new WidgetAddPage(driver);
		//search widget
		widgetAddPage2.searchWidget("Top 10 Listeners by Load");//Database Top Errors");//");
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);			
		
			
		driver.takeScreenShot();		
		//select widget
		widgetAddPage2.clickWidgetOnTable("Top 10 Listeners by Load");//Database Top Errors");//Top 10 Listeners by Load");
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		driver.takeScreenShot();	
		clickAddButton();
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		clickCloseButton();
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);		
				
		driver.takeScreenShot();
		//save dashboard
		clickSaveButton();
			
		driver.takeScreenShot();
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//add autorefresh
		clickRefreshItem();
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//add time selector
		clickTimePicker();
		
		TileManager tg = new TileManager(driver);
		
	    tg.tileOpen();
		tg.tileDelete();
		//save dashboard
		clickSaveButton();
		tg.tileOpen();
		tg.tileMaximize();
		tg.tileOpen();
		tg.tileRestore();
		tg.tileOpen();
		tg.tileWider();
		tg.tileOpen();
		tg.tileNarrower();
		tg.tileOpen();
		tg.tileRefresh();
		
	}
	
	
	
	public static void modifyDashboardInfo(String dbname,String dbdesc) throws Exception
	{
		
		WebElement mainelement = driver.getElement(DashBoardPageId.DashboardNameID);
		WebElement editNamebutton = driver.getElement(DashBoardPageId.NameEditID);
        Actions builder = new Actions(driver.getWebDriver());
        builder.moveToElement(mainelement).moveToElement(editNamebutton).click().perform();        
        driver.getElement(DashBoardPageId.NameInputID).clear();
		driver.sendKeys(DashBoardPageId.NameInputID, dbname);//"DBA_Name_Modify");
		driver.click(DashBoardPageId.NameEditOKID);
		
		mainelement = driver.getElement(DashBoardPageId.DashboardDescID);
		WebElement editDescbutton = driver.getElement(DashBoardPageId.DescEditID);
        builder = new Actions(driver.getWebDriver());
        builder.moveToElement(mainelement).moveToElement(editDescbutton).click().perform();  
        driver.getElement(DashBoardPageId.DescInputID).clear();
		driver.sendKeys(DashBoardPageId.DescInputID, dbdesc);//"DBA_DESC_MODIFY");
		driver.click(DashBoardPageId.DescEditOKID);
		
	}
	
	public static void clickRefreshItem() throws Exception
	{
		WebElement Box = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.AutoRefreshID));//*[@id='oj-listbox-drop']"));//));
		Box.click();

		waitForMilliSeconds(DashBoardPageId.Delaytime_short);
		
		driver.takeScreenShot();
		WebElement DivisionList1 = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.AutoRefreshBy_15_Secs_ID));//*[contains(@id,'oj-listbox-result-label')]")); //and contains(text(),'Last Accessed')]"));
		DivisionList1.click();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
		Box = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.AutoRefreshID));//*[@id='oj-listbox-drop']"));//));
		Box.click();
		WebElement DivisionList2 = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.AutoRefreshBy_30_Secs_ID));//*[contains(@id,'oj-listbox-result-label')]")); //and contains(text(),'Last Accessed')]"));
		DivisionList2.click();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
		Box = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.AutoRefreshID));//*[@id='oj-listbox-drop']"));//));
		Box.click();
		WebElement DivisionList3 = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.AutoRefreshBy_1_Min_ID));//*[contains(@id,'oj-listbox-result-label')]")); //and contains(text(),'Last Accessed')]"));
		DivisionList3.click();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
		Box = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.AutoRefreshID));//*[@id='oj-listbox-drop']"));//));
		Box.click();
		WebElement DivisionList4 = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.AutoRefreshBy_15_Mins_ID));//*[contains(@id,'oj-listbox-result-label')]")); //and contains(text(),'Last Accessed')]"));
		DivisionList4.click();
		
		
	}
	public static void clickTimePicker() throws Exception
	{
		WebElement Box = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.TimePickerID));//*[@id='oj-listbox-drop']"));//));
		Box.click();
		
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		driver.click(DashBoardPageId.DateID1);
		//driver.click(DashBoardPageId.DateID2);
		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		driver.click(DashBoardPageId.ApplyBtnID);
	}

	
	public static void navigateWidget(String parentHandle) throws Exception
	{
			WidgetAddPage widgetAddPage;
			String widgetName;			       
			         
			driver.getLogger().info("start to test in navigateWidget");
			waitForMilliSeconds(2*DashBoardPageId.Delaytime_long);
			
			//verify title and desc of dashboard
			/*if( getText(DashBoardPageId.DashboardNameID) == null)
			{
				Assert.assertEquals(getText(DashBoardPageId.MDashboardNameID),"AAA_testDashboard");
				Assert.assertEquals(getText(DashBoardPageId.MDashboardDescID),"AAA_testDashBoard desc");
			}
			else*/{
				Assert.assertEquals(getText(DashBoardPageId.DashboardNameID),"AAA_testDashboard");
				Assert.assertEquals(getText(DashBoardPageId.DashboardDescID),"AAA_testDashBoard desc");
			}
	
			driver.waitForElementPresent(DashBoardPageId.WidgetAddButtonID);
			driver.click(DashBoardPageId.WidgetAddButtonID);					
			
			driver.getLogger().info("before select");
			WebElement Box = driver.getWebDriver().findElement(By.xpath(WidgetPageId.dropListID));//*[@id='oj-listbox-drop']"));//));
			Box.click();

			DashBoardUtils.waitForMilliSeconds(2*DashBoardPageId.Delaytime_long);
			
			driver.takeScreenShot();
			//WebElement DivisionList = driver.getWebDriver().findElement(By.xpath(WidgetPageId.LAListID));//*[contains(@id,'oj-listbox-result-label')]")); //and contains(text(),'Last Accessed')]"));
			//DivisionList.click();
			DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
			//get text and grab number,then determine how many pages we should navigate 
			WebElement leftButton = driver.getWebDriver().findElement(By.xpath(WidgetPageId.leftNavigatorBtnID));
			WebElement rightButton = driver.getWebDriver().findElement(By.xpath(WidgetPageId.rightNavigatorBtnID));
			driver.getLogger().info("after select");
								
			while(rightButton.isEnabled()){
				rightButton.click();
			}
			driver.getLogger().info("after right btn");
			Assert.assertFalse(rightButton.isEnabled());
			DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
			Assert.assertTrue(leftButton.isEnabled());
			driver.getLogger().info("after enabled 1");
			//navigate to left
			while(leftButton.isEnabled()){
				leftButton.click();
			}
			driver.getLogger().info("after left btn");		
			Assert.assertFalse(leftButton.isEnabled());
			DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
			Assert.assertTrue(rightButton.isEnabled());
			driver.getLogger().info("after enabled 2");
			clickCloseButton();
			DashBoardUtils.waitForMilliSeconds(2*DashBoardPageId.Delaytime_long);
	}
		
		
	
	
		
	public static void saveWidget() throws Exception
	{
		clickSaveButton();
	}
	
	public static void waitForMilliSeconds(long millisSec) throws Exception
	{
		Thread.sleep(millisSec);
	}
	
	public static void clickDBOnTable(String dbID) throws Exception
	{
		driver.click("//div[@aria-dashboard="+dbID+"]");
	}
	
	public static boolean doesWebElementExist(String selector) throws Exception
	{
		                             
		        WebElement el=driver.getWebDriver().findElement(By.id(selector));
			   //boolean b = driver.isElementPresent(selector);
			
		       if(el.isDisplayed()){
		    	   driver.getLogger().info("can get element");
		    	   return true;              
		       }		                                    
		       else{
		 
		    	   driver.getLogger().info("can not get element");
		    	   return false;              
		       }         

	}
	
	public static boolean doesWebElementExistByXPath(String xpath) throws Exception
	{
		                             
		        WebElement el=driver.getWebDriver().findElement(By.xpath(xpath));
			
		       if(el.isDisplayed()){
		    	   driver.getLogger().info("xpath:can get element");
		    	   return true;              
		       }		                                    
		       else{
		 
		    	   driver.getLogger().info("xpath:can not get element");
		    	   return false;              
		       }         

	}
	
	public static void clickDashBoard() throws Exception
	{
		driver.click(DashBoardPageId.DashBoardID);
	}
	
	public static void clickLVDashBoard() throws Exception
	{
		driver.click(DashBoardPageId.DashBoardListViewDashBoardID);
	}
	 
	public static void clickToSortByLastAccessed() throws Exception
	{		
		WebElement Box = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.SortDropListID));//*[@id='oj-listbox-drop']"));//));
		Box.click();

		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
		
		driver.takeScreenShot();
		WebElement DivisionList = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.Access_Date_ID));//*[contains(@id,'oj-listbox-result-label')]")); //and contains(text(),'Last Accessed')]"));
		DivisionList.click();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
		
		driver.takeScreenShot();
	}
	public static void searchDashBoard(String board) throws Exception
	{
		driver.takeScreenShot();
		driver.getLogger().info("go into search DashBoard");
		driver.sendKeys(DashBoardPageId.SearchDSBoxID, board);
		driver.click("/html/body/div[*]/div/div[1]/div/div/div[2]/div[1]/span[1]/button[2]");
		driver.takeScreenShot();
		
	}
	public static void checkBrandingBarLink() throws Exception
	{
		clickNavigatorLink();
		waitForMilliSeconds(DashBoardPageId.Delaytime_short);
		driver.takeScreenShot();
		//Home link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.HomeLinkID)).getText(),"Home");
		//IT Analytics link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.ITALinkID)).getText(),"IT Analytics");
		//Log Analytics link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.LALinkID)).getText(),"Log Analytics");
		//APM link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.APMLinkID)).getText(),"APM");
		//Log link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.LOGLinkID)).getText(),"Log");
		//AWR Analytics link
		driver.takeScreenShot();
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.AWRALinkID)).getText(),"Analyze");
		//Flex link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.FlexLinkID)).getText(),"AWR");
		//Target link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.TargetLinkID)).getText(),"Search");
		//Customer Software link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.CustomLinkID)).getText(),"Agents");
		//IT Analytics Administration link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.ITA_Admin_LinkID)).getText(),"IT Analytics Administration");
	}
	public static void clickCheckBox() throws Exception
	{
		
		//check APM cloud service 
		driver.getWebDriver().findElement(By.id(DashBoardPageId.APM_BoxID)).click();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		Assert.assertTrue(doesWebElementExistByXPath(DashBoardPageId.Application_Performance_Monitoring_ID));
		driver.getWebDriver().findElement(By.id(DashBoardPageId.APM_BoxID)).click();
		//check ita box
		driver.getWebDriver().findElement(By.id(DashBoardPageId.ITA_Check_BoxID)).click();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Database_Performance_Analytics_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Database_Resource_Planning_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Garbage_Collection_Overhead_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Host_Inventory_By_Platform_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Database_Configuration_and_Storage_By_Version_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.WebLogic_Servers_by_JDK_Version_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Top_25_Databases_by_Resource_Consumption_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Top_25_WebLogic_Servers_by_Heap_Usage_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Top_25_WebLogic_Servers_by_Load_ID));
		driver.getWebDriver().findElement(By.id(DashBoardPageId.ITA_Check_BoxID)).click();
		//check la box
		driver.getWebDriver().findElement(By.id(DashBoardPageId.LA_BoxID)).click();
		driver.getWebDriver().findElement(By.id(DashBoardPageId.LA_BoxID)).click();
		//check oracle created
		driver.getWebDriver().findElement(By.id(DashBoardPageId.Oracle_BoxID)).click();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Application_Performance_Monitoring_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Database_Performance_Analytics_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Database_Resource_Planning_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Garbage_Collection_Overhead_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Host_Inventory_By_Platform_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Database_Configuration_and_Storage_By_Version_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.WebLogic_Servers_by_JDK_Version_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Top_25_Databases_by_Resource_Consumption_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Top_25_WebLogic_Servers_by_Heap_Usage_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Top_25_WebLogic_Servers_by_Load_ID));		
		driver.getWebDriver().findElement(By.id(DashBoardPageId.Oracle_BoxID)).click();
		//check me created
		driver.getWebDriver().findElement(By.id(DashBoardPageId.Other_BoxID)).click();
		driver.getWebDriver().findElement(By.id(DashBoardPageId.Other_BoxID)).click();
		
		
	}
	public static String getText(String id)
	{
		WebElement we = driver.getWebDriver().findElement(By.xpath(id));
		return we.getText();
	}
	public static String getTextByID(String id)
	{
		WebElement we = driver.getWebDriver().findElement(By.id(id));
		return we.getText();
	}
}