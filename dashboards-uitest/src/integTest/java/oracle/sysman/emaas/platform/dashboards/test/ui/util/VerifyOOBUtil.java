package oracle.sysman.emaas.platform.dashboards.test.ui.util;

import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class VerifyOOBUtil
{
	public static void verifyApplicationServers(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Application Servers opened correctly");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Application Server Status");
		VerifyOOBUtil.verifyIconInWidget(webd, "Application Server: Fatal Alerts");
		VerifyOOBUtil.verifyIconInWidget(webd, "Application Server: Critical Alerts");
		VerifyOOBUtil.verifyIconInWidget(webd, "Application Server: Warning Alerts");
		VerifyOOBUtil.verifyIconInWidget(webd, "Application Servers: Top 5 by Alert Count");
		VerifyOOBUtil.verifyIconInWidget(webd, "Application Server Performance Metrics");
		VerifyOOBUtil.verifyIconInWidget(webd, "Average Memory Usage");
		VerifyOOBUtil.verifyIconInWidget(webd, "CPU Utilization by Application Server Type");
		VerifyOOBUtil.verifyIconInWidget(webd, "Application Servers Grouped by Web Request Rate");
		VerifyOOBUtil.verifyIconInWidget(webd, "Application Server Log Records");

		webd.getLogger().info("Verification end...");
	}

	public static void verifyCategoricalAdvanced(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Categorical - Advanced opened correctly");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Bar Chart with Top N");
		VerifyOOBUtil.verifyIconInWidget(webd, "Bar Chart with Color and Group by option");
		VerifyOOBUtil.verifyIconInWidget(webd, "Bar Chart with Color option");
		VerifyOOBUtil.verifyIconInWidget(webd, "Pareto Chart");

		webd.getLogger().info("Verification end...");
	}

	public static void verifyCategoricalBasic(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Categorical - Basic opened correctly");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Donut");
		VerifyOOBUtil.verifyIconInWidget(webd, "Treemap");
		VerifyOOBUtil.verifyIconInWidget(webd, "Histogram");
		VerifyOOBUtil.verifyIconInWidget(webd, "Analytics Line - Categorical");
		VerifyOOBUtil.verifyIconInWidget(webd, "Bar Chart");
		VerifyOOBUtil.verifyIconInWidget(webd, "Stacked Bar Chart");

		webd.getLogger().info("Verification end...");
	}

	public static void verifyDatabaseOperations(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=15");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Database Operations opened correctly");

		webd.getLogger().info("Verify the dashboard titile...");
		DashboardBuilderUtil.verifyDashboard(webd, "Database Operations", "", true);

		webd.getLogger().info("Verify the OOB Dashboard - Database Operations opened finished");
	}

	public static void verifyDatabaseOperations_Details(WebDriver webd)
	{
		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Database Log Trends");
		VerifyOOBUtil.verifyIconInWidget(webd, "Database Critical Incidents by Target Type");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top Database Targets with Log Errors");
		VerifyOOBUtil.verifyIconInWidget(webd, "Database Top Errors");

		webd.getLogger().info("Verification end...");
	}

	public static void verifyDatabases(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Databases opened correctly");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Database Status");
		VerifyOOBUtil.verifyIconInWidget(webd, "Databases: Fatal Alerts");
		VerifyOOBUtil.verifyIconInWidget(webd, "Databases: Critical Alerts");
		VerifyOOBUtil.verifyIconInWidget(webd, "Databases: Warning Alerts");
		VerifyOOBUtil.verifyIconInWidget(webd, "Databases: Top 5 by Alert Count");
		VerifyOOBUtil.verifyIconInWidget(webd, "Database Performance Metrics");
		VerifyOOBUtil.verifyIconInWidget(webd, "Database Log Trends");
		VerifyOOBUtil.verifyIconInWidget(webd, "Space Used by Database Type");
		VerifyOOBUtil.verifyIconInWidget(webd, "Databases Grouped by Transactions");

		webd.getLogger().info("Verification end...");
	}

	public static void verifyDatabaseSecurity(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");

		//verify the url of opened page
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=48");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 Oracle DBs by Threats");
		VerifyOOBUtil.verifyIconInWidget(webd, "Threat Trend on Oracle DBs");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 Oracle DBs by Activity");
		VerifyOOBUtil.verifyIconInWidget(webd, "Activity Trend on Oracle DBs");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 Oracle DBs with Account Modifications on High privileges");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 Oracle DBs with Sensitive Object Accesses");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 Oracle DBs by Startups / Shutdowns");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 Oracle DBs with Account Modifications");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 Oracle DBs with Schema Changes");	
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 Oracle DBs by Anomalies");

		webd.getLogger().info("Verification end...");
	}

	public static void verifyDNS(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");

		//verify the url of opened page
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=40");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Total DNS Messages");
		VerifyOOBUtil.verifyIconInWidget(webd, "Unique DNS Queries");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 DNS Domains");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 DNS Sources");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 DNS Non-Standard TLDs");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 DNS Sources with TXT Lookup");
		VerifyOOBUtil.verifyIconInWidget(webd, "DNS Queries Per Domain");
		VerifyOOBUtil.verifyIconInWidget(webd, "DNS Responses by Type");

		webd.getLogger().info("Verification end...");
	}

	public static void verifyEnterpriseHealth(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=31");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Enterprise Health opened correctly");

		webd.getLogger().info("Verify the dashboard set titile...");
		DashboardBuilderUtil.verifyDashboardSet(webd, "Enterprise Health");
	}

	public static void verifyEnterpriseHealth_Details(WebDriver webd)
	{
		webd.getLogger().info("Verify the icon in dashboard set -- <Enterprise Health>");
		VerifyOOBUtil.verifyIconInOobDashboardSet(webd);

		//verify each dashboard
		webd.getLogger().info("Verify Dashboard <Summary> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Summary");
		VerifyOOBUtil.verifySummary(webd);

		webd.getLogger().info("Verify Dashboard <Hosts> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Hosts");
		VerifyOOBUtil.verifyHosts(webd);

		webd.getLogger().info("Verify Dashboard <Databases> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Databases");
		VerifyOOBUtil.verifyDatabases(webd);

		webd.getLogger().info("Verify Dashboard <Application Servers> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Application Servers");
		VerifyOOBUtil.verifyApplicationServers(webd);

		webd.getLogger().info("Verify Dashboard <Entities> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Entities");
		VerifyOOBUtil.verifyEntities(webd);

		webd.getLogger().info("Verification end...");
	}

	public static void verifyEntities(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Entities opened correctly");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Entities");

		webd.getLogger().info("Verification end...");
	}

	public static void verifyExadataHealth(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=28");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Exadata Health opened correctly");

		webd.getLogger().info("Verify the dashboard set titile...");
		DashboardBuilderUtil.verifyDashboardSet(webd, "Exadata Health");
	}

	public static void verifyExadataHealth_Details(WebDriver webd)
	{
		webd.getLogger().info("Verify the icon in dashboard set -- <Exadata Health>");
		VerifyOOBUtil.verifyIconInOobDashboardSet(webd);

		//verify each dashboard
		webd.getLogger().info("Verify Dashboard <Overview> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Overview");
		VerifyOOBUtil.verifyOverview(webd);

		webd.getLogger().info("Verify Dashboard <Performance> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Performance");
		VerifyOOBUtil.verifyPerformance(webd);

		webd.getLogger().info("Verification end...");
	}

	public static void verifyFirewall(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");

		//verify the url of opened page
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=47");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 Denied Sources");
		VerifyOOBUtil.verifyIconInWidget(webd, "Denied Connections by Source");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 Denied Destination Ports");
		VerifyOOBUtil.verifyIconInWidget(webd, "Denied Connections by Destination Port");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 Sources connected to Insecure Ports");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 Sources Connected to Unassigned Internal IP");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 Connected Sources");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 Sources by Bytes Transferred");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 Destinations by Bytes Transferred");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 10 Sources Connect to External Resources");
		VerifyOOBUtil.verifyIconInWidget(webd, "Last Network Configuration Change");

		webd.getLogger().info("Verification end...");
	}

	public static void verifyHostOperations(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=16");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Host Operations opened correctly");

		webd.getLogger().info("Verify the dashboard titile...");
		DashboardBuilderUtil.verifyDashboard(webd, "Host Operations", "", true);

		webd.getLogger().info("Verify the OOB Dashboard - Host Operations opened finished");
	}

	public static void verifyHostOperations_Details(WebDriver webd)
	{
		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Host Logs Trend");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top Host Log Sources");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top Host Log Entries by Service");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top SUDO Users");

		webd.getLogger().info("Verification end...");
	}

	public static void verifyHosts(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Hosts opened correctly");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Host Status");
		VerifyOOBUtil.verifyIconInWidget(webd, "Hosts: Fatal Alerts");
		VerifyOOBUtil.verifyIconInWidget(webd, "Hosts: Critical Alerts");
		VerifyOOBUtil.verifyIconInWidget(webd, "Hosts: Warning Alerts");
		VerifyOOBUtil.verifyIconInWidget(webd, "Hosts: Top 5 by Alert Count");
		VerifyOOBUtil.verifyIconInWidget(webd, "Host Performance Metrics");
		VerifyOOBUtil.verifyIconInWidget(webd, "Hosts Grouped by Memory Utilization");
		VerifyOOBUtil.verifyIconInWidget(webd, "CPU Utilization by Host Type");
		VerifyOOBUtil.verifyIconInWidget(webd, "Avg Disk I/O Request Rate");
		VerifyOOBUtil.verifyIconInWidget(webd, "Host Log Trend");

		webd.getLogger().info("Verification end...");
	}

	public static void verifyIconInOobDashboard(WebDriver webd)
	{
		webd.getLogger().info("Verify the save icon is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.DASHBOARDSAVECSS), "Save icon is displayed in OOB Dashboard");

		WebDriverWait wait = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(PageId.DASHBOARDOPTIONS_CSS)));
		webd.click("css=" + PageId.DASHBOARDOPTIONS_CSS);
		
		webd.getLogger().info("Verify the edit menu is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css" + DashBoardPageId.BUILDEROPTIONSEDITLOCATORCSS),
				"Edit menu is displayed in OOB Dashboard");
	}

	public static void verifyIconInOobDashboardSet(WebDriver webd)
	{
		//verify the edit menu & save icon are not displayed in OOB
		webd.getLogger().info("Verify the save icon is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.DASHBOARDSAVECSS), "Save icon is displayed in OOB");

		WebDriverWait wait = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(PageId.DASHBOARDSETOPTIONS_CSS)));
		webd.click("css=" + PageId.DASHBOARDSETOPTIONS_CSS);
		
		webd.getLogger().info("Verify the edit menu is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css" + PageId.DASHBOARDSETOPTIONSEDIT_CSS), "Edit menu is displayed in OOB");
	}

	public static void verifyIconInWidget(WebDriver driver, String widgetname)
	{
		driver.click(DashBoardPageId_190.BUILDERTILESEDITAREA);
		
		String titleTitlesLocator = String.format(DashBoardPageId.BUILDERTILETITLELOCATOR, widgetname);
		
		WebElement tileTitle = null;
		try
		{
			tileTitle = driver.getWebDriver().findElement(By.xpath(titleTitlesLocator));
		}
		catch (NoSuchElementException e) {			
			Assert.assertTrue(false,"verifyIconInWidget failed: "+ e.getLocalizedMessage());			
		}
		if (tileTitle == null) {
			Assert.assertTrue(false,"verifyIconInWidget failed: Not find expected widget "+ widgetname);			
		}

		//tileTitle.click();
		
		Actions builder = new Actions(driver.getWebDriver());
		builder.moveToElement(tileTitle).perform();
		driver.waitForServer();
		//verify the config icon not exist
		Assert.assertFalse(driver.isDisplayed(DashBoardPageId.BUILDERTILECONFIGLOCATOR),
				"widiget configuration icon is displayed");
	}

	public static void verifyMiddlewareOperations(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=17");

		//verify the Middleware Operations open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Middleware Operations opened correctly");

		webd.getLogger().info("Verify the dashboard titile...");
		DashboardBuilderUtil.verifyDashboard(webd, "Entities", "", true);

		webd.getLogger().info("Verify the OOB Dashboard - Middleware Operations opened finished");
	}

	public static void verifyMiddlewareOperations_Details(WebDriver webd)
	{
		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Middleware Logs Trend");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top Middleware Error Codes");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top Middleware Targets with Errors");
		VerifyOOBUtil.verifyIconInWidget(webd, "Web Server Top Accessed Pages");

		webd.getLogger().info("Verification end...");
	}

	public static void verifyOrchestration(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");

		//verify the url of opened page
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=37");

		DashboardBuilderUtil.verifyWidget(webd, "Overview");
		DashboardBuilderUtil.verifyWidget(webd, "Execution Details");

		webd.getLogger().info("Verification end...");
	}

	public static void verifyOverview(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Overview opened correctly");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Entities by Database Machine");
		VerifyOOBUtil.verifyIconInWidget(webd, "Exadata Inventory");
		VerifyOOBUtil.verifyIconInWidget(webd, "Entity Types in Database Machines");
		VerifyOOBUtil.verifyIconInWidget(webd, "Exadata Capacity by Disk Type");

		webd.getLogger().info("Verification end...");
	}

	public static void verifyPerformance(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Performance opened correctly");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Oracle Exadata Storage Server Read Response Time");
		VerifyOOBUtil.verifyIconInWidget(webd, "Top 5 Databases by Active Sessions");
		VerifyOOBUtil.verifyIconInWidget(webd, "Host CPU Utilization and Memory Utilization");
		VerifyOOBUtil.verifyIconInWidget(webd, "Oracle Exadata Storage Server Infiniband Network Performance");
		VerifyOOBUtil.verifyIconInWidget(webd, "Oracle Exadata Storage Server Write Response Time");
		VerifyOOBUtil.verifyIconInWidget(webd, "Oracle Exadata Storage Server Read/Write Response Times");
		VerifyOOBUtil.verifyIconInWidget(webd, "Oracle Exadata Storage Server I/O Utilization by DB Machine");

		webd.getLogger().info("Verification end...");
	}

	public static void verifySummary(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Summary opened correctly");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Entity Status");
		VerifyOOBUtil.verifyIconInWidget(webd, "Entity Count");
		VerifyOOBUtil.verifyIconInWidget(webd, "Entities: Fatal Alerts");
		VerifyOOBUtil.verifyIconInWidget(webd, "Entities: Critical Alerts");
		VerifyOOBUtil.verifyIconInWidget(webd, "Entities: Warning Alerts");
		VerifyOOBUtil.verifyIconInWidget(webd, "Host Status by Type");
		VerifyOOBUtil.verifyIconInWidget(webd, "Host CPU Metrics");
		VerifyOOBUtil.verifyIconInWidget(webd, "Database Status by Type");
		VerifyOOBUtil.verifyIconInWidget(webd, "Database I/O Metrics");
		VerifyOOBUtil.verifyIconInWidget(webd, "Application Server Status by Type");
		VerifyOOBUtil.verifyIconInWidget(webd, "Application Server Load Metrics");
		VerifyOOBUtil.verifyIconInWidget(webd, "Load Balancer Status by Type");
		VerifyOOBUtil.verifyIconInWidget(webd, "Load Balancer Performance Metrics");

		webd.getLogger().info("Verification end...");
	}

	public static void verifyTimeseriesArea(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Timeseries - Area opened correctly");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Area Chart");
		VerifyOOBUtil.verifyIconInWidget(webd, "Stacked Area Chart");
		VerifyOOBUtil.verifyIconInWidget(webd, "Stacked Area Chart with Group By");

		webd.getLogger().info("the verification end...");
	}

	public static void verifyTimeseriesLineAdvanced(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Timeseries - Line Advanced opened correctly");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Line Chart with Color");
		VerifyOOBUtil.verifyIconInWidget(webd, "Stacked Line Chart with Group By");
		VerifyOOBUtil.verifyIconInWidget(webd, "Stacked Line Chart with Color and Group by");

		webd.getLogger().info("the verification end...");
	}

	public static void verifyTimeseriesLineBasic(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Timeseries - Line Basic opened correctly");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Line Chart");
		VerifyOOBUtil.verifyIconInWidget(webd, "Analytics Line");
		VerifyOOBUtil.verifyIconInWidget(webd, "Line Chart with Shared Y-axis");
		VerifyOOBUtil.verifyIconInWidget(webd, "Line Chart with Reference Line");

		webd.getLogger().info("the verification end...");
	}

	public static void verifyTrendAndForecasting(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard -Trend and Forecasting opened correctly");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Analytics Line with Trend and Forecasting");
		VerifyOOBUtil.verifyIconInWidget(webd, "Line Chart with Trend and Forecasting");

		webd.getLogger().info("Verification end...");
	}

	public static void verifyUIGallery(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=24");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - UI Gallery opened correctly");

		webd.getLogger().info("Verify the dashboard set titile...");
		DashboardBuilderUtil.verifyDashboardSet(webd, "UI Gallery");
	}

	public static void verifyUIGallery_Details(WebDriver webd)
	{
		webd.getLogger().info("Verify the icon in dashboard set --<UIGallery>");
		VerifyOOBUtil.verifyIconInOobDashboardSet(webd);

		//verify each dashboard
		webd.getLogger().info("Verify Dashboard <Timeseries - Line Basic> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Timeseries - Line Basic");
		VerifyOOBUtil.verifyTimeseriesLineBasic(webd);

		webd.getLogger().info("Verify Dashboard <Timeseries - Line Advanced> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Timeseries - Line Advanced");
		VerifyOOBUtil.verifyTimeseriesLineAdvanced(webd);

		webd.getLogger().info("Verify Dashboard <Timeseries - Area> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Timeseries - Area");
		VerifyOOBUtil.verifyTimeseriesArea(webd);

		webd.getLogger().info("Verify Dashboard <Categorical - Basic> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Categorical - Basic");
		VerifyOOBUtil.verifyCategoricalBasic(webd);

		webd.getLogger().info("Verify Dashboard <Categorical - Advanced> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Categorical - Advanced");
		VerifyOOBUtil.verifyCategoricalAdvanced(webd);

		webd.getLogger().info("Verify Dashboard <Trend and Forecasting> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Trend and Forecasting");
		VerifyOOBUtil.verifyTrendAndForecasting(webd);
				
		webd.getLogger().info("Verify Dashboard <Others> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Others");
		verifyOthers(webd);

		webd.getLogger().info("Verification end...");
	}

	public static void verifyOthers(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Others opened correctly");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Table");
		VerifyOOBUtil.verifyIconInWidget(webd, "Scatter Chart");

		webd.getLogger().info("Verification end...");
	}
}