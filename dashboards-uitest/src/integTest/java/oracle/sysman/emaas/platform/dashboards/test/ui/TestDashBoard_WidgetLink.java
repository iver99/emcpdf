/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.test.ui;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1200;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

/**
 * @author shangwan
 *
 */
public class TestDashBoard_WidgetLink extends LoginAndLogout
{
	private String dbName_Test = "";
	private String dbName2_Test = "";
	private String dbName3_Test = "";
	private String dbName4_Test = "";
	private String dbName5_Test = "";
	private String dbDesc_Test = "";
	
	private final String widgetName = "Top Hosts by Log Entries";
	private final String OOBName = "Middleware Operations";
	
	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);

		//reset all the checkboxes
		DashboardHomeUtil.resetFilterOptions(webd);
	}
	
	@BeforeClass
	public void createTestDashboard()
	{
		dbName_Test = "Test Widget Link-" + DashBoardUtils.generateTimeStamp();
		dbDesc_Test = "test widget link";
		
		dbName2_Test = "link dashboard - " + DashBoardUtils.generateTimeStamp();
		
		dbName4_Test = "delete dashboard link to widget - " + DashBoardUtils.generateTimeStamp();

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in createTestDashboard");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//create dashboard
		webd.getLogger().info("Start to create dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, dbName_Test, dbDesc_Test, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_Test, dbDesc_Test, true), "Create dashboard failed!");
		
		//add widget
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, widgetName);
		webd.getLogger().info("Add widget finished");

		//verify if the widget added successfully
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, widgetName),	"Widget '" + widgetName + "' not found");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);
		
		//back to home page and create another dashboard
		webd.getLogger().info("Back to home page and create another dashboard");
		BrandingBarUtil.visitDashboardHome(webd);
		
		DashboardHomeUtil.createDashboard(webd, dbName2_Test, dbDesc_Test, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName2_Test, dbDesc_Test, true), "Create dashboard failed!");
		
		//back to home page and create another dashboard
		webd.getLogger().info("Back to home page and create another dashboard");
		BrandingBarUtil.visitDashboardHome(webd);
				
		DashboardHomeUtil.createDashboard(webd, dbName4_Test, dbDesc_Test, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName4_Test, dbDesc_Test, true), "Create dashboard failed!");
		
		LoginAndLogout.logoutMethod();
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
		DashBoardUtils.deleteDashboard(webd, dbName_Test);
		DashBoardUtils.deleteDashboard(webd, dbName2_Test);
		DashBoardUtils.deleteDashboard(webd, dbName3_Test);
		DashBoardUtils.deleteDashboard(webd, dbName4_Test);	
		DashBoardUtils.deleteDashboard(webd, dbName5_Test);	

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}
	
	@Test
	public void testAddWidgetLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testAddWidgetLink");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);
		
		//open a dashboard
		webd.getLogger().info("Open a dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);
		
		//add a link to the widget
		webd.getLogger().info("Add Widget Link");
		DashboardBuilderUtil.addLinkToWidgetTitle(webd, widgetName, OOBName);
		
		//click the widget link
		webd.getLogger().info("Click the link added to the widget");
		Assert.assertTrue(DashboardBuilderUtil.verifyLinkOnWidgetTitle(webd, widgetName, OOBName));		
	}
	
	@Test(dependsOnMethods = {"testAddWidgetLink"})
	public void testEditWidgetLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testAddWidgetLink");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);
		
		//open a dashboard
		webd.getLogger().info("Open a dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);
		
		//edit the link in the widget
		webd.getLogger().info("Add Widget Link");
//		DashboardBuilderUtil.addLinkToWidgetTitle(webd, widgetName, dbName2_Test);
//		add test case	for EMCPDF-4723
		webd.click("css=" + DashBoardPageId_190.RIGHTDRAWERTOGGLEPENCILBTNCSS);
		String XPATH = ".//*[@id='dbd-edit-settings-container']/span[text()='"+widgetName+"']";
		webd.click("xpath=" + XPATH);
		
		WebDriverWait wait = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		webd.waitForElementPresent("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTAREACSSLOCATOR);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTAREACSSLOCATOR)));

		//remove link if widget title is linked
		if(webd.isElementPresent("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTREMOVELINKCSSLOCATOR)){
			webd.click("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTREMOVELINKCSSLOCATOR);
			webd.waitForElementPresent("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBOXCSSLOCATOR);
		}

		if(dbName2_Test != null && !dbName2_Test.isEmpty()){
			WebElement searchInput = webd.getElement("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBOXCSSLOCATOR);
			// focus on search input box
			wait.until(ExpectedConditions.elementToBeClickable(searchInput));

			Actions actions = new Actions(webd.getWebDriver());
			actions.moveToElement(searchInput).build().perform();
			searchInput.clear();
			WaitUtil.waitForPageFullyLoaded(webd);
			actions.moveToElement(searchInput).build().perform();
			webd.click("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBOXCSSLOCATOR);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHGETCSSLOCATOR)));
			searchInput.sendKeys(dbName2_Test);
			webd.waitForServer();
			webd.takeScreenShot();
			//verify input box value
			Assert.assertEquals(searchInput.getAttribute("value"), dbName2_Test);

			WebElement searchButton = webd.getElement("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBTNCSSLOCATOR);
			webd.waitForElementPresent("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBTNCSSLOCATOR);
			searchButton.click();
			//wait for ajax resolved
			WaitUtil.waitForPageFullyLoaded(webd);
			webd.takeScreenShot();

			webd.getLogger().info("[DashboardHomeUtil] start to add link");
			List<WebElement> matchingWidgets = webd.getWebDriver().findElements(
					By.cssSelector(DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHGETCSSLOCATOR));
			if (matchingWidgets == null || matchingWidgets.isEmpty()) {
				throw new NoSuchElementException("Right drawer content for search string =" + dbName2_Test + " is not found");
			}
			WaitUtil.waitForPageFullyLoaded(webd);

			Actions builder = new Actions(webd.getWebDriver());
			try {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHGETCSSLOCATOR)));
				webd.click("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHGETCSSLOCATOR);

				webd.waitForElementPresent("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTADDBTNCSSLOCATOR);
				webd.click("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTADDBTNCSSLOCATOR);

				webd.getLogger().info("Content added");			
			}
			catch (IllegalArgumentException e) {
				throw new NoSuchElementException("Content for " + dbName2_Test + " is not found");
			}
		}
		
		
		
		
		//click the widget link
		webd.getLogger().info("Click the link added to the widget");
		DashboardBuilderUtil.clickLinkOnWidgetTitle(webd, widgetName);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName2_Test, dbDesc_Test, true), "Not open the correct dashboard");
	}
	
	@Test(dependsOnMethods = {"testEditWidgetLink"})
	public void testRemoveWidgetLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testAddWidgetLink");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);
		
		//open a dashboard
		webd.getLogger().info("Open a dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);
		
		//edit the link in the widget
		webd.getLogger().info("Add Widget Link");
		DashboardBuilderUtil.addLinkToWidgetTitle(webd, widgetName, "");				
			
		//click the widget link
		webd.getLogger().info("Verify the link in the widget has been removed");
		Assert.assertFalse(DashboardBuilderUtil.hasWidgetLink(webd, widgetName));		
	}
	
	@Test(dependsOnMethods = {"testRemoveWidgetLink"})
	public void testWidgetTitleHide()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testAddWidgetLink");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);
		
		//open a dashboard
		webd.getLogger().info("Open a dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);
		
		//verify the widget
		webd.getLogger().info("Verify the widget");
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, widgetName), "Not have widget '" + widgetName + "'");
		
		//hide the title
		webd.getLogger().info("Hide the widget title");
		DashboardBuilderUtil.showWidgetTitle(webd, widgetName, false);
		
		//verify that Add link is disabled
		webd.getLogger().info("Verify that the link can't be added");
		DashBoardUtils.verifyAddLinkButton(webd, widgetName, 0);
		
		//show the title
		webd.getLogger().info("Show the widget title");
		DashboardBuilderUtil.showWidgetTitle(webd, widgetName, true);
		
		//add the widget link
		webd.getLogger().info("Add the link to the widget");
		DashboardBuilderUtil.addLinkToWidgetTitle(webd, widgetName, 0, dbName2_Test);
		
		//verify the link has been added
		webd.getLogger().info("Verify the link has been removed");
		Assert.assertTrue(DashboardBuilderUtil.hasWidgetLink(webd, widgetName, 0), "Widget has link");
		
		//hide the title
		webd.getLogger().info("Hide the widget title");
		DashboardBuilderUtil.showWidgetTitle(webd, widgetName, false);
		
		//remove the link
		webd.getLogger().info("Remove the link");
		DashboardBuilderUtil.addLinkToWidgetTitle(webd, widgetName, 0, "");
		
		//show the title
		webd.getLogger().info("Show the widget title");
		DashboardBuilderUtil.showWidgetTitle(webd, widgetName, true);
		
		//verify the link has been removed
		webd.getLogger().info("Verify the link has been removed");
		Assert.assertFalse(DashboardBuilderUtil.hasWidgetLink(webd, widgetName, 0), "Widget has link");	
		
		//save the dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);
	}
	
	@Test(alwaysRun = true)
	public void testMultipleWidget()
	{
		dbName3_Test = "test multiple widget - " + DashBoardUtils.generateTimeStamp();
		dbDesc_Test = "test widget link";
		
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in createTestDashboard");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//create dashboard
		webd.getLogger().info("Start to create dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, dbName3_Test, dbDesc_Test, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName3_Test, dbDesc_Test, true), "Create dashboard failed!");
		
		//add widget
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, widgetName);
		DashboardBuilderUtil.addWidgetToDashboard(webd, widgetName);
		webd.getLogger().info("Add widget finished");
		
		//save the dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);
		
		//add link to the second widget
		webd.getLogger().info("Add link to the second widget");
		DashboardBuilderUtil.addLinkToWidgetTitle(webd, widgetName, 1, dbName2_Test);
		
		//verify the widget
		webd.getLogger().info("Verify the widgets");
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, widgetName));
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, widgetName, 1));
		
		//click the link in widget
		webd.getLogger().info("Click the widget link");
		DashboardBuilderUtil.clickLinkOnWidgetTitle(webd, widgetName, 0);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName2_Test, dbDesc_Test, true), "Not open the correct dashboard");
	}
	
	@Test(alwaysRun = true)
	public void testDeleteDashboardLinkToWidget()
	{
		dbName5_Test = "test delete dashboard linked to widget - " + DashBoardUtils.generateTimeStamp();
		dbDesc_Test = "test widget link";
		
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in createTestDashboard");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//create dashboard
		webd.getLogger().info("Start to create dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, dbName5_Test, dbDesc_Test, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName5_Test, dbDesc_Test, true), "Create dashboard failed!");
		
		//add widget
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, widgetName);
		webd.getLogger().info("Add widget finished");		

		//add link to the second widget
		webd.getLogger().info("Add link to the widget");
		DashboardBuilderUtil.addLinkToWidgetTitle(webd, widgetName, dbName4_Test);
		
		//back to the home page
		webd.getLogger().info("Back to the home page");
		BrandingBarUtil.visitDashboardHome(webd);
		
		//delete the linked dashboard
		webd.getLogger().info("Delete the linked dashboard");
		DashboardHomeUtil.deleteDashboard(webd, dbName4_Test, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
		
		//refresh the home page
		webd.getLogger().info("Refresh the home page");
		BrandingBarUtil.visitDashboardHome(webd);
		
		//open the dashboard has widget link
		webd.getLogger().info("Open the dashboard has widget link");
		DashboardHomeUtil.selectDashboard(webd, dbName5_Test);
		
		//verify the link in widget
		webd.getLogger().info("Verify the widget link has been removed");
		Assert.assertFalse(DashboardBuilderUtil.hasWidgetLink(webd, widgetName), "The widget link still exists!");
	}
}
