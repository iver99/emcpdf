package oracle.sysman.emaas.platform.dashboards.test.ui;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class TestDashBoard_TextWidget extends LoginAndLogout
{
	
	private String dbName_textWidget = "";
	private String dbName_textWidget_toolbar = "";
	private String dbName_textWidget_image = "";
	private String dbName_textWidget_link = "";
	private String dbName_textWidget_multiLink = "";
	private String dbName_textWidget_order = "";
	private String dbName_textWidget_empty = "";
        private String dbName_textWidget_clickLink1 = "";
	private String dbName_textWidget_clickLink2 = "";
	private String dbName_textWidget_clickImage = "";
	
	private final String customWidgetName = "Execution Details";
	
	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

	}

	@AfterClass
	public void RemoveDashboard()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to remove test data");

		//delete dashboard
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Start to remove the test data...");
		DashBoardUtils.deleteDashboard(webd, dbName_textWidget);
		DashBoardUtils.deleteDashboard(webd, dbName_textWidget_toolbar);
		DashBoardUtils.deleteDashboard(webd, dbName_textWidget_link);
		DashBoardUtils.deleteDashboard(webd, dbName_textWidget_multiLink);
		DashBoardUtils.deleteDashboard(webd, dbName_textWidget_order);
		
		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}
	
	@Test(alwaysRun = true)
	public void testTextWidget()
	{
		dbName_textWidget = "Dashboard_textWidget-" + DashBoardUtils.generateTimeStamp();
		
		String dbDesc = "Add text widget into dashboard";
		String content = "This is the dashboard which is used to test the new feature of adding text widget";
		//String content_Hyperlink = "";
		
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testTextWidget");

		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Create the dashboard, then to add text widget");
		DashboardHomeUtil.createDashboard(webd, dbName_textWidget, dbDesc, DashboardHomeUtil.DASHBOARD);
		
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_textWidget, dbDesc, true), "Create dashboard failed!");		
		
		DashboardBuilderUtil.addTextWidgetToDashboard(webd);
		
		//Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, widgetName), "text widget isn't added into the dashboard successfully");
		DashboardBuilderUtil.editTextWidgetAddContent(webd, 1, content);
		
		DashboardBuilderUtil.saveDashboard(webd);
		
		//Verify the content is added successfully
		webd.getLogger().info("Verify the content is added successfully");
				
		Assert.assertEquals(webd.getText("css=" + DashBoardPageId.TEXTCONTENTCSS), content);
	}
	
	 @Test(alwaysRun = true)
		public void testTextWidget_Image()
		{
			dbName_textWidget_image = "Dashboard_textWidgetImage-" + DashBoardUtils.generateTimeStamp();
			
			String dbDesc = "Add text widget into dashboard, test the image feature";
			String urlString = "emsaasui/uifwk/images/o_logo.png";
			String url = "";
			String alternativeText = "test_image";
			
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
			webd.getLogger().info("start to test image in testTextWidget");

			DashboardHomeUtil.gridView(webd);
			
			String currentUrl = webd.getCurrentUrl();
			url = (currentUrl.substring(0, currentUrl.indexOf("emsaasui"))).concat(urlString);

			webd.getLogger().info("Create the dashboard, then to add text widget");
			DashboardHomeUtil.createDashboard(webd, dbName_textWidget_image, dbDesc, DashboardHomeUtil.DASHBOARD);
			
			webd.getLogger().info("Verify the dashboard created Successfully");
			Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_textWidget_image, dbDesc, true), "Create dashboard failed!");		
			
			DashboardBuilderUtil.addTextWidgetToDashboard(webd);				
			
			DashboardBuilderUtil.addImageInTextWidget(webd, 1, url, alternativeText);
			webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);
			
			DashboardBuilderUtil.addImageInTextWidget(webd, 1, url, null);
			webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);

			List<WebElement> images = webd.getWebDriver().findElements(By.cssSelector(DashBoardPageId.IMAGESCSS));	
			
			for (WebElement img : images)
			{
				Assert.assertEquals(img.isDisplayed(), true);
			}
			
			DashboardBuilderUtil.saveDashboard(webd);	
		}

	 	@Test(alwaysRun = true)
		public void testTextWidget_Link()
		{
			dbName_textWidget_link = "Dashboard_textWidgetURL-" + DashBoardUtils.generateTimeStamp();
			
			String dbDesc = "Add text widget into dashboard, test the link feature";
			String url = "www.baidu.com";
			
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
			webd.getLogger().info("start to test link in testTextWidget");

			DashboardHomeUtil.gridView(webd);

			webd.getLogger().info("Create the dashboard, then to add text widget");
			DashboardHomeUtil.createDashboard(webd, dbName_textWidget_link, dbDesc, DashboardHomeUtil.DASHBOARD);
			
			webd.getLogger().info("Verify the dashboard created Successfully");
			Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_textWidget_link, dbDesc, true), "Create dashboard failed!");		
			
			DashboardBuilderUtil.addTextWidgetToDashboard(webd);				
			
			//Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, widgetName), "text widget isn't added into the dashboard successfully");
			DashboardBuilderUtil.addLinkInTextWidget(webd, 1, url, DashBoardPageId.PROTOCOLOPTION_HTTP);
			webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);
			//Verify the content is added successfully
			webd.getLogger().info("Verify the url is added successfully");	
			Assert.assertEquals(webd.getText("css=" + DashBoardPageId.TEXTCONTENTCSS), DashBoardPageId.PROTOCOLOPTION_HTTP + url);		
			
			DashboardBuilderUtil.addLinkInTextWidget(webd, 1, url, DashBoardPageId.PROTOCOLOPTION_HTTPS);
			webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);
			//Verify the content is added successfully
			webd.getLogger().info("Verify the url is added successfully");			
			Assert.assertEquals(webd.getText("css=" + DashBoardPageId.TEXTCONTENTCSS), DashBoardPageId.PROTOCOLOPTION_HTTPS + url);				
			
			DashboardBuilderUtil.addLinkInTextWidget(webd, 1, url, DashBoardPageId.PROTOCOLOPTION_FTP);
			webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);
			//Verify the content is added successfully
			webd.getLogger().info("Verify the url is added successfully");	
			Assert.assertEquals(webd.getText("css=" + DashBoardPageId.TEXTCONTENTCSS), DashBoardPageId.PROTOCOLOPTION_FTP + url);		
			
			DashboardBuilderUtil.addLinkInTextWidget(webd, 1, url, DashBoardPageId.PROTOCOLOPTION_NEWS);
			webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);
			//Verify the content is added successfully
			webd.getLogger().info("Verify the url is added successfully");				
			Assert.assertEquals(webd.getText("css=" + DashBoardPageId.TEXTCONTENTCSS), DashBoardPageId.PROTOCOLOPTION_NEWS + url);		
			
			DashboardBuilderUtil.addLinkInTextWidget(webd, 1, url, DashBoardPageId.PROTOCOLOPTION_OTHER);
			webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);
			//Verify the content is added successfully
			webd.getLogger().info("Verify the url is added successfully");		
			Assert.assertEquals(webd.getText("css=" + DashBoardPageId.TEXTCONTENTCSS), DashBoardPageId.PROTOCOLOPTION_OTHER + url);		
			
			DashboardBuilderUtil.saveDashboard(webd);				
		}
		
		@Test(alwaysRun = true)
		public void testTextWidget_multiLink()
		{
			dbName_textWidget_multiLink = "Dashboard_textWidgetMultiURL-" + DashBoardUtils.generateTimeStamp();
			
			String dbDesc = "Add text widget into dashboard, add multi link";
			String url = "www.baidu.com";
			
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
			webd.getLogger().info("start to test link in testTextWidget");

			DashboardHomeUtil.gridView(webd);

			webd.getLogger().info("Create the dashboard, then to add text widget");
			DashboardHomeUtil.createDashboard(webd, dbName_textWidget_multiLink, dbDesc, DashboardHomeUtil.DASHBOARD);
			
			webd.getLogger().info("Verify the dashboard created Successfully");
			Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_textWidget_multiLink, dbDesc, true), "Create dashboard failed!");		
			
			DashboardBuilderUtil.addTextWidgetToDashboard(webd);				
			
			DashboardBuilderUtil.addLinkInTextWidget(webd, 1, url, DashBoardPageId.PROTOCOLOPTION_HTTP);	
			webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);
			
			WebElement textContent = webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.TEXTCONTENTCSS));
		
			DashboardBuilderUtil.switchTextWidgetToEditMode(webd, 1);

			webd.getWebDriver().switchTo().activeElement().sendKeys(Keys.ENTER);
			webd.getWebDriver().switchTo().activeElement().sendKeys(Keys.ARROW_UP);
			webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);

			DashboardBuilderUtil.addLinkInTextWidget(webd, 1, url, DashBoardPageId.PROTOCOLOPTION_HTTPS);	
			webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);
			
			//Verify the content is added successfully
			webd.getLogger().info("Verify the two urls are added successfully");	
			Assert.assertEquals(webd.getText(DashBoardPageId.TEXTCONTENT1), DashBoardPageId.PROTOCOLOPTION_HTTPS + url);			
						
			Assert.assertEquals(webd.getText(DashBoardPageId.TEXTCONTENT2), DashBoardPageId.PROTOCOLOPTION_HTTP + url);		
			
			DashboardBuilderUtil.saveDashboard(webd);	
		}
		
		@Test(alwaysRun = true)
		public void testTextWidget_order()
		{
			dbName_textWidget_order = "Dashboard_textWidgetOrder-" + DashBoardUtils.generateTimeStamp();
			
			String dbDesc = "Add text widget into dashboard, and test its order";
			
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
			webd.getLogger().info("start to test the order of Text Widget");

			DashboardHomeUtil.gridView(webd);

			webd.getLogger().info("Create the dashboard");
			DashboardHomeUtil.createDashboard(webd, dbName_textWidget_order, dbDesc, DashboardHomeUtil.DASHBOARD);
			
			webd.getLogger().info("Verify the dashboard created Successfully");
			Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_textWidget_order, dbDesc, true), "Create dashboard failed!");		
			
			DashboardBuilderUtil.addWidgetToDashboard(webd, customWidgetName);
			Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, customWidgetName), "The widget added failed");
					
			DashboardBuilderUtil.addTextWidgetToDashboard(webd);	
			
			DashboardBuilderUtil.editTextWidgetAddContent(webd, 1, "This is a Text Widget");
			
			//Verify the Text Widget is ordered in the first place
			webd.getLogger().info("Verify the Text Widget is order in first place");	
			List<WebElement> widgets = webd.getWebDriver().findElements(By.cssSelector(DashBoardPageId.TILESLISTCSS)); 
			
			WebElement widget1 = webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.TEXTWIDGETCSS));
			Assert.assertTrue(widgets.get(0).equals(widget1), "The text widget isn't placed in the first place");

			DashboardBuilderUtil.saveDashboard(webd);				
		}
		
		@Test(alwaysRun = true)
		public void testTextWidget_toolbar()
		{		
			dbName_textWidget_toolbar = "Dashboard_textWidgetToolbar-" + DashBoardUtils.generateTimeStamp();
			String dbDesc = "Test whether text widget remove the Maximize/Remove icon and add delete icon";
			
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
			webd.getLogger().info("start to test in testTextWidget_toolbarIcon");

			DashboardHomeUtil.gridView(webd);

			webd.getLogger().info("Create the dashboard");
			DashboardHomeUtil.createDashboard(webd, dbName_textWidget_toolbar, dbDesc, DashboardHomeUtil.DASHBOARD);
			
			webd.getLogger().info("Verify the dashboard created Successfully");
			Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_textWidget_toolbar, dbDesc, true), "Create dashboard failed!");		
					
			DashboardBuilderUtil.addTextWidgetToDashboard(webd);

			webd.getLogger().info("Verify there is no Maximize icon in Text Widget");
			Assert.assertFalse(webd.isElementPresent(DashBoardPageId.MAXIMIZEICON), "There is Maximize icon in the text widget");	
					
			webd.moveToElement("css=" + DashBoardPageId.TILETITLECSS);
			
			webd.click("css=" + DashBoardPageId.CONFIGTILECSS);
			
			webd.getLogger().info("Verify remove icon is substituted by delete icon");
			Assert.assertTrue(webd.isDisplayed(DashBoardPageId.DELETETILE), "Don't find delete icon");
			Assert.assertFalse(webd.isDisplayed(DashBoardPageId.REMOVETILECSS), "Find the remove icon");
			
			webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);
			
			DashboardBuilderUtil.editTextWidgetAddContent(webd, 1, "This is a Text Widget");
			
			DashboardBuilderUtil.saveDashboard(webd);
		}

		@Test(alwaysRun = true)
		public void testEmptyTextWidget()
		{
			dbName_textWidget_empty= "Empty Text Widget - " + DashBoardUtils.generateTimeStamp();
			//initialize the test
			initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
			webd.getLogger().info("Start to test in testEmptyTextWidget");

			//reset all filter options
			webd.getLogger().info("Reset all filter options in the home page");
			DashboardHomeUtil.resetFilterOptions(webd);

			//switch to grid view
			webd.getLogger().info("Switch to grid view");
			DashboardHomeUtil.gridView(webd);

			//create a dashboard
			webd.getLogger().info("Create a dashboard");
			DashboardHomeUtil.createDashboard(webd, dbName_textWidget_empty, "", DashboardHomeUtil.DASHBOARD);

			//Add a text widget
			webd.getLogger().info("Add a empty text widget");
			DashboardBuilderUtil.addTextWidgetToDashboard(webd);

			//save the dashboard
			webd.getLogger().info("Save the dashboard");
			DashboardBuilderUtil.saveDashboard(webd);

			//back to the dashboard home page
			webd.getLogger().info("Back to the dashboard home page");
			BrandingBarUtil.visitDashboardHome(webd);

			//open the created dashboard
			webd.getLogger().info("Open the dashboard");
			DashboardHomeUtil.selectDashboard(webd, dbName_textWidget_empty);

			//verify the dashbaord
			webd.getLogger().info("Verify the empty dashboard was saved");
			DashboardBuilderUtil.verifyDashboard(webd, dbName_textWidget_empty, "", true);

			//verify the text widget
			webd.getLogger().info("Verify the text widget in the dashboard");
			Assert.assertEquals(webd.getText("css=" + DashBoardPageId.TEXTWIDGETCONTENTCSS).trim(), "Start typing...");
		}

	//@Test
	public void testTextWidget_clickLink()
	{
		dbName_textWidget_clickLink1 = "Dashboard_textWidget_clickLink-" + DashBoardUtils.generateTimeStamp();
		
		String dbDesc = "Add text widget into dashboard, test click link, open it with new window";
		
		String urlString = "emsaasui/uifwk/images/o_logo.png";
		String url = "";		
		
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test link in testTextWidget");

		DashboardHomeUtil.gridView(webd);
		
		String currentUrl = webd.getWebDriver().getCurrentUrl();
		url = (currentUrl.substring(8, currentUrl.indexOf("emsaasui"))).concat(urlString);

		webd.getLogger().info("Create the dashboard, then to add text widget");
		DashboardHomeUtil.createDashboard(webd, dbName_textWidget_clickLink1, dbDesc, DashboardHomeUtil.DASHBOARD);
		
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_textWidget_clickLink1, dbDesc, true), "Create dashboard failed!");		
		
		DashboardBuilderUtil.addTextWidgetToDashboard(webd);				
		
		DashboardBuilderUtil.addLinkInTextWidget(webd, 1, url, DashBoardPageId.PROTOCOLOPTION_HTTPS);	
		webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);
		
		DashboardBuilderUtil.saveDashboard(webd);	
		
		webd.click(DashBoardPageId.TEXTCONTENT1);	
		
		webd.switchToWindow();
		
		DashBoardUtils.verifyURL(webd, "uifwk/images/o_logo.png");
		webd.takeScreenShot();
		
		webd.switchToMainWindow();
		
		//webd.switchToParentWindow();
	}
	
	//@Test
	public void testTextWidget_clickImage()
	{
		dbName_textWidget_clickImage = "Dashboard_textWidgetClickImage-" + DashBoardUtils.generateTimeStamp();
		
		String dbDesc = "Add text widget into dashboard, test click image, open it with new window";
		String urlString = "emsaasui/uifwk/images/o_logo.png";
		String url = "";
		String alternativeText = "test_image";
		
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test image in testTextWidget");

		DashboardHomeUtil.gridView(webd);
		
		String currentUrl = webd.getWebDriver().getCurrentUrl();
		url = (currentUrl.substring(0, currentUrl.indexOf("emsaasui"))).concat(urlString);

		webd.getLogger().info("Create the dashboard, then to add text widget");
		DashboardHomeUtil.createDashboard(webd, dbName_textWidget_clickImage, dbDesc, DashboardHomeUtil.DASHBOARD);
		
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_textWidget_clickImage, dbDesc, true), "Create dashboard failed!");		
		
		DashboardBuilderUtil.addTextWidgetToDashboard(webd);				
		
		DashboardBuilderUtil.addImageInTextWidget(webd, 1, url, alternativeText);
		webd.click("css=" + DashBoardPageId.DASHBOARDTITLEBARCSS);	
		
		DashboardBuilderUtil.saveDashboard(webd);	
		
		webd.waitForServer();
		webd.click("css=" + DashBoardPageId.IMAGESCSS);
		
		webd.switchToWindow();
		
		DashBoardUtils.verifyURL(webd, "uifwk/images/o_logo.png");
		webd.takeScreenShot();
		
		webd.switchToMainWindow();
	//	webd.switchToParentWindow();
	}
}
 
