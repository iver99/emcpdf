package oracle.sysman.emaas.platform.dashboards.test.ui.util;

import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.testng.Assert;

public class VerifyOOBUtil
{
	public static void verifyAPM(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "apmUi/index.html");
		//verify the APM open correctly
		//TODO

		webd.getLogger().info("Verification end...");
	}

	public static void verifyApplicationPerfAnalytics(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL(webd, "emcitas/ap-analytics-war/html/ap-perf-analytics.html");

		//verify the dashboard title & time picker
		webd.getLogger().info("Verify the dashboard title");
		webd.isTextPresent("Application Performance Analytics", "css=" + PageId.DASHBOARDTITLE_CSS);
		webd.getLogger().info("Verify the time picker");
		webd.isDisplayed("css=" + PageId.DATETIMEPICKER_OOB_CSS);

		//verify all the widgets displayed
		//TODO

		webd.getLogger().info("Verify end...");
	}

	public static void verifyApplicationServers(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Application Servers opened correctly");

		//verify all the widgets displayed
//		webd.getLogger().info("Verify all the widgets in dashboard");
//		DashboardBuilderUtil.verifyWidget(webd, "Application Server Status");
//		DashboardBuilderUtil.verifyWidget(webd, "Application Server: Fatal Alerts");
//		DashboardBuilderUtil.verifyWidget(webd, "Application Server: Critical Alerts");
//		DashboardBuilderUtil.verifyWidget(webd, "Application Server: Warning Alerts");
//		DashboardBuilderUtil.verifyWidget(webd, "Application Servers: Top 5 by Alert Count");
//		DashboardBuilderUtil.verifyWidget(webd, "Application Server Performance Metrics");
//		DashboardBuilderUtil.verifyWidget(webd, "Average Memory Usage");
//		DashboardBuilderUtil.verifyWidget(webd, "CPU Utilization by Application Server Type");
//		DashboardBuilderUtil.verifyWidget(webd, "Application Servers Grouped by Web Request Rate");
//		DashboardBuilderUtil.verifyWidget(webd, "Application Server Log Records");

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

	public static void verifyAvailabilityAnalytics(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL(webd, "emcitas/avail-analytics-war/html/avail-analytics-home.html");

		//verify all the widgets displayed
		//TODO

		webd.getLogger().info("Verification end...");
	}

	public static void verifyCategoricalAdvanced(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Categorical - Advanced opened correctly");

		//verify all the widgets displayed
//		webd.getLogger().info("Verify all the widgets in dashboard");
//		DashboardBuilderUtil.verifyWidget(webd, "Bar Chart with Top N");
//		DashboardBuilderUtil.verifyWidget(webd, "Bar Chart with Color and Group by option");
//		DashboardBuilderUtil.verifyWidget(webd, "Bar Chart with Color option");
//		DashboardBuilderUtil.verifyWidget(webd, "Pareto Chart");

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

		//verify all the widgets displayed
//		webd.getLogger().info("Verify all the widgets in dashboard");
//		DashboardBuilderUtil.verifyWidget(webd, "Donut");
//		DashboardBuilderUtil.verifyWidget(webd, "Treemap");
//		DashboardBuilderUtil.verifyWidget(webd, "Histogram");
//		DashboardBuilderUtil.verifyWidget(webd, "Analytics Line - Categorical");
//		DashboardBuilderUtil.verifyWidget(webd, "Bar Chart");
//		DashboardBuilderUtil.verifyWidget(webd, "Stacked Bar Chart");

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
		//verify all the widgets displayed
//		webd.getLogger().info("Verify all the widgets in dashboard -- <Database Operations>");
//		DashboardBuilderUtil.verifyWidget(webd, "Database Log Trends");
//		DashboardBuilderUtil.verifyWidget(webd, "Database Critical Incidents by Target Type");
//		DashboardBuilderUtil.verifyWidget(webd, "Top Database Targets with Log Errors");
//		DashboardBuilderUtil.verifyWidget(webd, "Database Top Errors");

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

		//verify all the widgets displayed
//		webd.getLogger().info("Verify all the widgets in dashboard");
//		DashboardBuilderUtil.verifyWidget(webd, "Database Status");
//		DashboardBuilderUtil.verifyWidget(webd, "Databases: Fatal Alerts");
//		DashboardBuilderUtil.verifyWidget(webd, "Databases: Critical Alerts");
//		DashboardBuilderUtil.verifyWidget(webd, "Databases: Warning Alerts");
//		DashboardBuilderUtil.verifyWidget(webd, "Databases: Top 5 by Alert Count");
//		DashboardBuilderUtil.verifyWidget(webd, "Database Performance Metrics");
//		DashboardBuilderUtil.verifyWidget(webd, "Database Log Trends");
//		DashboardBuilderUtil.verifyWidget(webd, "Space Used by Database Type");
//		DashboardBuilderUtil.verifyWidget(webd, "Databases Grouped by Transactions");

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

//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 Oracle DBs by Threats");
//		DashboardBuilderUtil.verifyWidget(webd, "Threat Trend on Oracle DBs");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 Oracle DBs by Activity");
//		DashboardBuilderUtil.verifyWidget(webd, "Activity Trend on Oracle DBs");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 Oracle DBs with Account Modifications on High privileges");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 Oracle DBs with Sensitive Object Accesses");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 Oracle DBs by Startups / Shutdowns");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 Oracle DBs with Account Modifications");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 Oracle DBs with Schema Changes");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 Oracle DBs with Anomalies");

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

//		DashboardBuilderUtil.verifyWidget(webd, "Total DNS Messages");
//		DashboardBuilderUtil.verifyWidget(webd, "Unique DNS Queries");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 DNS Domains");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 DNS Sources");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 DNS Non-Standard TLDs");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 DNS Sources with TXT Lookup");
//		DashboardBuilderUtil.verifyWidget(webd, "DNS Queries Per Domain");
//		DashboardBuilderUtil.verifyWidget(webd, "DNS Responses by Type");

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

		//verify the dashboards in set
		//comment below verify, if there is no expected dashboard in set, selectDashboardInsideSet api would throw error
		//so I think below verification are duplicated
//		webd.getLogger().info("Verify the dashboards in set");
//		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Summary");
//		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Hosts");
//		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Databases");
//		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Application Servers");
//		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Entities");

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

		webd.getLogger().info("Verify all the widgets in dashboard");
//		DashboardBuilderUtil.verifyWidget(webd, "Entities");

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

		//verify the dashboards in set
		//comment below verify, if there is no expected dashboard in set, selectDashboardInsideSet api would throw error
		//so I think below verification are duplicated		
//		webd.getLogger().info("Verify the dashboards in set");
//		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Overview");
//		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Performance");

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

//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 Denied Sources");
//		DashboardBuilderUtil.verifyWidget(webd, "Denied Connections by Source");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 Denied Destination Ports");
//		DashboardBuilderUtil.verifyWidget(webd, "Denied Connections by Destination Port");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 Sources connected to Insecure Ports");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 Sources Connected to Unassigned Internal IP");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 Connected Sources");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 Sources by Bytes Transferred");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 Destinations by Bytes Transferred");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 10 Sources Connect to External Resources");
//		DashboardBuilderUtil.verifyWidget(webd, "Last Network Configuration Change");

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
		//verify all the widgets displayed
//		webd.getLogger().info("Verify all the widgets in dashboard -- <Host Operations>");
//		DashboardBuilderUtil.verifyWidget(webd, "Host Logs Trend");
//		DashboardBuilderUtil.verifyWidget(webd, "Top Host Log Sources");
//		DashboardBuilderUtil.verifyWidget(webd, "Top Host Log Entries by Service");
//		DashboardBuilderUtil.verifyWidget(webd, "Top SUDO Users");

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

		webd.getLogger().info("Verify all the widgets in dashboard");
//		DashboardBuilderUtil.verifyWidget(webd, "Host Status");
//		DashboardBuilderUtil.verifyWidget(webd, "Hosts: Fatal Alerts");
//		DashboardBuilderUtil.verifyWidget(webd, "Hosts: Critical Alerts");
//		DashboardBuilderUtil.verifyWidget(webd, "Hosts: Warning Alerts");
//		DashboardBuilderUtil.verifyWidget(webd, "Hosts: Top 5 by Alert Count");
//		DashboardBuilderUtil.verifyWidget(webd, "Host Performance Metrics");
//		DashboardBuilderUtil.verifyWidget(webd, "Hosts Grouped by Memory Utilization");
//		DashboardBuilderUtil.verifyWidget(webd, "CPU Utilization by Host Type");
//		DashboardBuilderUtil.verifyWidget(webd, "Avg Disk I/O Request Rate");
//		DashboardBuilderUtil.verifyWidget(webd, "Host Log Trend");

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

		webd.click("css=" + PageId.DASHBOARDSETOPTIONS_CSS);
		
		webd.getLogger().info("Verify the edit menu is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css" + PageId.DASHBOARDSETOPTIONSEDIT_CSS), "Edit menu is displayed in OOB");
	}

	public static void verifyIconInWidget(WebDriver driver, String widgetname)
	{
		driver.click(DashBoardPageId_190.BUILDERTILESEDITAREA);
		
		String titleTitlesLocator = String.format(DashBoardPageId.BUILDERTILETITLELOCATOR, widgetname);
		
//		WebElement tileTitle = null;
//		try
//		{
//			tileTitle = driver.getWebDriver().findElement(By.xpath(titleTitlesLocator));
//		}
//		catch (NoSuchElementException e) {			
//			Assert.assertTrue(false,"verifyIconInWidget failed: "+ e.getLocalizedMessage());			
//		}
//		if (tileTitle == null) {
//			Assert.assertTrue(false,"verifyIconInWidget failed: Not find expected widget "+ widgetname);			
//		}
//
//		//tileTitle.click();
//		
//		Actions builder = new Actions(driver.getWebDriver());
//		builder.moveToElement(tileTitle).perform();
		
		driver.moveToElement(titleTitlesLocator);
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
//		webd.getLogger().info("Verify all the widgets in dashboard  -- <Middleware Operations>");
//		DashboardBuilderUtil.verifyWidget(webd, "Middleware Logs Trend");
//		DashboardBuilderUtil.verifyWidget(webd, "Top Middleware Error Codes");
//		DashboardBuilderUtil.verifyWidget(webd, "Top Middleware Targets with Errors");
//		DashboardBuilderUtil.verifyWidget(webd, "Web Server Top Accessed Pages");

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

//		webd.getLogger().info("Verify all the widgets in dashboard");
//		DashboardBuilderUtil.verifyWidget(webd, "Entities by Database Machine");
//		DashboardBuilderUtil.verifyWidget(webd, "Exadata Inventory");
//		DashboardBuilderUtil.verifyWidget(webd, "Entity Types in Database Machines");
//		DashboardBuilderUtil.verifyWidget(webd, "Exadata Capacity by Disk Type");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
		VerifyOOBUtil.verifyIconInWidget(webd, "Entities by Database Machine");
		VerifyOOBUtil.verifyIconInWidget(webd, "Exadata Inventory");
		VerifyOOBUtil.verifyIconInWidget(webd, "Entity Types in Database Machines");
		VerifyOOBUtil.verifyIconInWidget(webd, "Exadata Capacity by Disk Type");

		webd.getLogger().info("Verification end...");
	}

	public static void verifyPerfAnalyticsApplication(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL(webd, "emcitas/mw-analytics-war/html/mw-perf-dashboard.html");

		//verify all the widgets displayed
		//TODO

		webd.getLogger().info("Verification end...");
	}

	public static void verifyPerfAnalyticsDatabase(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL(webd, "emcitas/db-performance-analytics/html/db-performance-analytics.html");

		//verify all the widgets displayed
		//TODO

		webd.getLogger().info("Verification end...");
	}

	public static void verifyPerformance(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Performance opened correctly");

//		webd.getLogger().info("Verify all the widgets in dashboard");
//		DashboardBuilderUtil.verifyWidget(webd, "Oracle Exadata Storage Server Read Response Time");
//		DashboardBuilderUtil.verifyWidget(webd, "Top 5 Databases by Active Sessions");
//		DashboardBuilderUtil.verifyWidget(webd, "Host CPU Utilization and Memory Utilization");
//		DashboardBuilderUtil.verifyWidget(webd, "Oracle Exadata Storage Server Infiniband Network Performance");
//		DashboardBuilderUtil.verifyWidget(webd, "Oracle Exadata Storage Server Write Response Time");
//		DashboardBuilderUtil.verifyWidget(webd, "Oracle Exadata Storage Server Read/Write Response Times");
//		DashboardBuilderUtil.verifyWidget(webd, "Oracle Exadata Storage Server I/O Utilization by DB Machine");

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

	public static void verifyResourceAnalyticsDatabase(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL(webd, "emcitas/db-analytics-war/html/db-analytics-resource-planner.html");

		//verify all the widgets displayed
		//TODO

		webd.getLogger().info("Verification end...");
	}

	public static void verifyResourceAnalyticsHost(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcitas/resource-analytics/html/server-resource-analytics.html");

		//verify all the widgets displayed
		//TODO
		webd.getLogger().info("Verification end...");
	}

	public static void verifyResourceAnalyticsMiddleware(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL(webd, "emcitas/mw-analytics-war/html/mw-analytics-resource-planner.html");

		//verify all the widgets displayed
		//TODO

		webd.getLogger().info("Verification end...");

	}

	public static void verifySummary(WebDriver webd)
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Summary opened correctly");

//		webd.getLogger().info("Verify all the widgets in dashboard");
//		DashboardBuilderUtil.verifyWidget(webd, "Entity Status");
//		DashboardBuilderUtil.verifyWidget(webd, "Entity Count");
//		DashboardBuilderUtil.verifyWidget(webd, "Entities: Fatal Alerts");
//		DashboardBuilderUtil.verifyWidget(webd, "Entities: Critical Alerts");
//		DashboardBuilderUtil.verifyWidget(webd, "Entities: Warning Alerts");
//		DashboardBuilderUtil.verifyWidget(webd, "Host Status by Type");
//		DashboardBuilderUtil.verifyWidget(webd, "Host CPU Metrics");
//		DashboardBuilderUtil.verifyWidget(webd, "Database Status by Type");
//		DashboardBuilderUtil.verifyWidget(webd, "Database I/O Metrics");
//		DashboardBuilderUtil.verifyWidget(webd, "Application Server Status by Type");
//		DashboardBuilderUtil.verifyWidget(webd, "Application Server Load Metrics");
//		DashboardBuilderUtil.verifyWidget(webd, "Load Balancer Status by Type");
//		DashboardBuilderUtil.verifyWidget(webd, "Load Balancer Performance Metrics");

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

//		webd.getLogger().info("Verify all the widgets in dashboard");
//		DashboardBuilderUtil.verifyWidget(webd, "Area Chart");
//		DashboardBuilderUtil.verifyWidget(webd, "Stacked Area Chart");
//		DashboardBuilderUtil.verifyWidget(webd, "Stacked Area Chart with Group By");

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

//		webd.getLogger().info("Verify all the widgets in dashboard");
//		DashboardBuilderUtil.verifyWidget(webd, "Line Chart with Color");
//		DashboardBuilderUtil.verifyWidget(webd, "Stacked Line Chart with Group By");
//		DashboardBuilderUtil.verifyWidget(webd, "Stacked Line Chart with Color and Group by");

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

//		webd.getLogger().info("Verify all the widgets in dashboard");
//		DashboardBuilderUtil.verifyWidget(webd, "Line Chart");
//		DashboardBuilderUtil.verifyWidget(webd, "Analytics Line");
//		DashboardBuilderUtil.verifyWidget(webd, "Line Chart with Shared Y-axis");
//		DashboardBuilderUtil.verifyWidget(webd, "Line Chart with Reference Line");

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

		//verify all the widgets displayed
//		webd.getLogger().info("Verify all the widgets in dashboard");
//		DashboardBuilderUtil.verifyWidget(webd, "Analytics Line with Trend and Forecasting");
//		DashboardBuilderUtil.verifyWidget(webd, "Line Chart with Trend and Forecasting");
		
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

		//verify the dashboards in set
		//comment below verify, if there is no expected dashboard in set, selectDashboardInsideSet api would throw error
		//so I think below verification are duplicated
//		webd.getLogger().info("Verify the dashboards in set");
//		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Timeseries - Line Basic");
//		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Timeseries - Line Advanced");
//		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Timeseries - Area");
//		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Categorical - Basic");
//		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Categorical - Advanced");
//		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Trend and Forecasting");
//		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Others");

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

//		webd.getLogger().info("Verify all the widgets in dashboard");
////		DashboardBuilderUtil.verifyWidget(webd, "Label");
////		DashboardBuilderUtil.verifyWidget(webd, "Circular Gauge");
//		DashboardBuilderUtil.verifyWidget(webd, "Table");
////		DashboardBuilderUtil.verifyWidget(webd, "Vertical Gauge");
////		DashboardBuilderUtil.verifyWidget(webd, "Horizontal Gauge");
//		DashboardBuilderUtil.verifyWidget(webd, "Scatter Chart");

		webd.getLogger().info("Verify the icon in OOB");
		VerifyOOBUtil.verifyIconInOobDashboard(webd);

		webd.getLogger().info("Verify the icon in widget");
//		VerifyOOBUtil.verifyIconInWidget(webd, "Label");
//		VerifyOOBUtil.verifyIconInWidget(webd, "Circular Gauge");
		VerifyOOBUtil.verifyIconInWidget(webd, "Table");
//		VerifyOOBUtil.verifyIconInWidget(webd, "Vertical Gauge");
//		VerifyOOBUtil.verifyIconInWidget(webd, "Horizontal Gauge");
		VerifyOOBUtil.verifyIconInWidget(webd, "Scatter Chart");

		webd.getLogger().info("Verification end...");
	}
}
