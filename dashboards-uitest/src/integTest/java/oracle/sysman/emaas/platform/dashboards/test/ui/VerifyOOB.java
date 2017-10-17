package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.VerifyOOBUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;

import org.testng.annotations.Test;

public class VerifyOOB extends LoginAndLogout
{

	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);

		//reset all the checkboxes
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verifyAPM_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in verifyAPM_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open APM
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Monitoring");

		VerifyOOBUtil.verifyAPM(webd);
	}

	@Test(alwaysRun = true)
	public void verifyAPM_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in verifyAPM_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open APM
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Monitoring");

		VerifyOOBUtil.verifyAPM(webd);
	}

	@Test(alwaysRun = true)
	public void verifyAPM_withFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyAPM_withFilter_GridView");

		//click Filter-APM
		webd.getLogger().info("Click Cloud Services - APM");
		DashboardHomeUtil.filterOptions(webd, "apm");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open APM
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Monitoring");

		//verify APM
		VerifyOOBUtil.verifyAPM(webd);
	}

	@Test(alwaysRun = true)
	public void verifyAPM_withFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyAPM_withFilter_ListView");

		//click Filter-APM
		webd.getLogger().info("Click Cloud Services - APM");
		DashboardHomeUtil.filterOptions(webd, "apm");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open APM
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Monitoring");

		//verify APM
		VerifyOOBUtil.verifyAPM(webd);
	}

	@Test(alwaysRun = true)
	public void verifyApplicationPerfAnalytics_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyApplicationPerfAnalytics_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Application Performance Analytics
		webd.getLogger().info("Open the OOB dashboard---Application Performance Analytics");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Analytics");

		//verify Application PerfAnalytics
		VerifyOOBUtil.verifyApplicationPerfAnalytics(webd);
	}

	@Test(alwaysRun = true)
	public void verifyApplicationPerfAnalytics_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyApplicationPerfAnalytics_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Application Performance Analytics
		webd.getLogger().info("Open the OOB dashboard---Application Performance Analytics");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Analytics");

		//verify Application PerfAnalytics
		VerifyOOBUtil.verifyApplicationPerfAnalytics(webd);
	}

	@Test(alwaysRun = true)
	public void verifyApplicationPerfAnalytics_withFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyApplicationPerfAnalytics_withFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Application Performance Analytics
		webd.getLogger().info("Open the OOB dashboard---Application Performance Analytics");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Analytics");

		//verify Application PerfAnalytics
		VerifyOOBUtil.verifyApplicationPerfAnalytics(webd);
	}

	@Test(alwaysRun = true)
	public void verifyApplicationPerfAnalytics_withFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyApplicationPerfAnalytics_withFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Application Performance Analytics
		webd.getLogger().info("Open the OOB dashboard---Application Performance Analytics");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Analytics");

		//verify Application PerfAnalytics
		VerifyOOBUtil.verifyApplicationPerfAnalytics(webd);
	}

	@Test(alwaysRun = true)
	public void verifyAvailabilityAnalytics_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyAvailabilityAnalytics_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Availability Analytics
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Availability Analytics");

		//verify Availability nalytics
		VerifyOOBUtil.verifyAvailabilityAnalytics(webd);
	}

	@Test(alwaysRun = true)
	public void verifyAvailabilityAnalytics_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyAvailabilityAnalytics_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Availability Analytics
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Availability Analytics");

		//verify Availability nalytics
		VerifyOOBUtil.verifyAvailabilityAnalytics(webd);
	}

	@Test(alwaysRun = true)
	public void verifyAvailabilityAnalytics_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyAvailabilityAnalytics_WithFilter_GridView");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Availability Analytics
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Availability Analytics");

		//verify Availability nalytics
		VerifyOOBUtil.verifyAvailabilityAnalytics(webd);
	}

	@Test(alwaysRun = true)
	public void verifyAvailabilityAnalytics_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyAvailabilityAnalytics_WithFilter_ListView");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Availability Analytics
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Availability Analytics");

		//verify Availability Analytics
		VerifyOOBUtil.verifyAvailabilityAnalytics(webd);
	}

	@Test(alwaysRun = true)
	public void verifyDatabaseOperations_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyDatabaseOperations_GridView");

		//lick on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Database Operations
		webd.getLogger().info("Open the OOB dashboard---Database Operations");
		DashboardHomeUtil.selectDashboard(webd, "Database Operations");

		//verify Database Operations
		VerifyOOBUtil.verifyDatabaseOperations(webd);
		VerifyOOBUtil.verifyDatabaseOperations_Details(webd);
	}

	@Test(alwaysRun = true)
	public void verifyDatabaseOperations_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyDatabaseOperations_ListView");

		//lick on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Database Operations
		webd.getLogger().info("Open the OOB dashboard---Database Operations");
		DashboardHomeUtil.selectDashboard(webd, "Database Operations");

		//verify Database Operations
		VerifyOOBUtil.verifyDatabaseOperations(webd);
	}

	@Test(alwaysRun = true)
	public void verifyDatabaseOperations_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyDatabaseOperations_WithFilter_GridView");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//lick on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Database Operations
		webd.getLogger().info("Open the OOB dashboard---Database Operations");
		DashboardHomeUtil.selectDashboard(webd, "Database Operations");

		//verify Database Operations
		VerifyOOBUtil.verifyDatabaseOperations(webd);
	}

	@Test(alwaysRun = true)
	public void verifyDatabaseOperations_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyDatabaseOperations_WithFilter_ListView");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//lick on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Database Operations
		webd.getLogger().info("Open the OOB dashboard---Database Operations");
		DashboardHomeUtil.selectDashboard(webd, "Database Operations");

		//verify Database Operations
		VerifyOOBUtil.verifyDatabaseOperations(webd);
	}

	//@Test(alwaysRun = true)
	public void verifyDatabaseSecurity_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyDatabaseSecurity_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Oracle Database Security
		webd.getLogger().info("Open the OOB dashboard---Oracle Database Security");
		DashboardHomeUtil.selectDashboard(webd, "Oracle Database Security");

		//verify Database Security
		VerifyOOBUtil.verifyDatabaseSecurity(webd);
	}

	//@Test(alwaysRun = true)
	public void verifyDatabaseSecurity_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyDatabaseSecurity_ListView");

		//click on Grid View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Oracle Database Security
		webd.getLogger().info("Open the OOB dashboard---Oracle Database Security");
		DashboardHomeUtil.selectDashboard(webd, "Oracle Database Security");

		//verify Database Security
		VerifyOOBUtil.verifyDatabaseSecurity(webd);
	}

	//@Test(alwaysRun = true)
	public void verifyDatabaseSecurity_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyDatabaseSecurity_WithFilter_GridView");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - Security Analytics");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Oracle Database Security
		webd.getLogger().info("Open the OOB dashboard---Oracle Database Security");
		DashboardHomeUtil.selectDashboard(webd, "Oracle Database Security");

		//verify Database Security
		VerifyOOBUtil.verifyDatabaseSecurity(webd);
	}

	//@Test(alwaysRun = true)
	public void verifyDatabaseSecurity_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyDatabaseSecurity_WithFilter_ListView");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - Security Analytics");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click on Grid View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Oracle Database Security
		webd.getLogger().info("Open the OOB dashboard---Oracle Database Security");
		DashboardHomeUtil.selectDashboard(webd, "Oracle Database Security");

		//verify Database Security
		VerifyOOBUtil.verifyDatabaseSecurity(webd);
	}

	//@Test(alwaysRun = true)
	public void verifyDNS_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyDNS_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---DNS");
		DashboardHomeUtil.selectDashboard(webd, "DNS");

		//verify DNS
		VerifyOOBUtil.verifyDNS(webd);
	}

	//@Test(alwaysRun = true)
	public void verifyDNS_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyDNS_ListView");

		//click on Grid View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---DNS");
		DashboardHomeUtil.selectDashboard(webd, "DNS");

		//verify DNS
		VerifyOOBUtil.verifyDNS(webd);
	}

	//@Test(alwaysRun = true)
	public void verifyDNS_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyDNS_WithFilter_GridView");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - Security Analytics");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---DNS");
		DashboardHomeUtil.selectDashboard(webd, "DNS");

		//verify DNS
		VerifyOOBUtil.verifyDNS(webd);
	}

	//@Test(alwaysRun = true)
	public void verifyDNS_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyDNS_WithFilter_ListView");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - Security Analytics");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click on Grid View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---DNS");
		DashboardHomeUtil.selectDashboard(webd, "DNS");

		//verify DNS
		VerifyOOBUtil.verifyDNS(webd);
	}

	@Test(alwaysRun = true)
	public void verifyEnterpriseHealth_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyEnterpriseHealth_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Enterprise Health
		webd.getLogger().info("Open the OOB dashboard---Enterprise Health");
		DashboardHomeUtil.selectDashboard(webd, "Enterprise Health");

		//verify Enterprise Health
		VerifyOOBUtil.verifyEnterpriseHealth(webd);
		VerifyOOBUtil.verifyEnterpriseHealth_Details(webd);
	}

	@Test(alwaysRun = true)
	public void verifyEnterpriseHealth_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyEnterpriseHealth_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Enterprise Health
		webd.getLogger().info("Open the OOB dashboard---Enterprise Health");
		DashboardHomeUtil.selectDashboard(webd, "Enterprise Health");

		//verify Enterprise Health
		VerifyOOBUtil.verifyEnterpriseHealth(webd);
	}

	@Test(alwaysRun = true)
	public void verifyEnterpriseHealth_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyEnterpriseHealth_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Enterprise Health
		webd.getLogger().info("Open the OOB dashboard---Enterprise Health");
		DashboardHomeUtil.selectDashboard(webd, "Enterprise Health");

		//verify Enterprise Health
		VerifyOOBUtil.verifyEnterpriseHealth(webd);
	}

	@Test(alwaysRun = true)
	public void verifyEnterpriseHealth_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyEnterpriseHealth_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Enterprise Health
		webd.getLogger().info("Open the OOB dashboard---Enterprise Health");
		DashboardHomeUtil.selectDashboard(webd, "Enterprise Health");

		//verify Enterprise Health
		VerifyOOBUtil.verifyEnterpriseHealth(webd);
	}

	@Test(alwaysRun = true)
	public void verifyExadataHealth_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyExadataHealth_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Exadata Health
		webd.getLogger().info("Open the OOB dashboard---Exadata Health");
		DashboardHomeUtil.selectDashboard(webd, "Exadata Health");

		//verify Exadata Health
		VerifyOOBUtil.verifyExadataHealth(webd);
		VerifyOOBUtil.verifyExadataHealth_Details(webd);
	}

	@Test(alwaysRun = true)
	public void verifyExadataHealth_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyExadataHealth_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Exadata Health
		webd.getLogger().info("Open the OOB dashboard---Exadata Health");
		DashboardHomeUtil.selectDashboard(webd, "Exadata Health");

		//verify Exadata Health
		VerifyOOBUtil.verifyExadataHealth(webd);
	}

	@Test(alwaysRun = true)
	public void verifyExadataHealth_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyExadataHealth_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Exadata Health
		webd.getLogger().info("Open the OOB dashboard---Exadata Health");
		DashboardHomeUtil.selectDashboard(webd, "Exadata Health");

		//verify Exadata Health
		VerifyOOBUtil.verifyExadataHealth(webd);
	}

	@Test(alwaysRun = true)
	public void verifyExadataHealth_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyExadataHealth_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Exadata Health
		webd.getLogger().info("Open the OOB dashboard---Exadata Health");
		DashboardHomeUtil.selectDashboard(webd, "Exadata Health");

		//verify Exadata Health
		VerifyOOBUtil.verifyExadataHealth(webd);
	}

	//@Test(alwaysRun = true)
	public void verifyFirewall_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyFirewall_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Firewall");
		DashboardHomeUtil.selectDashboard(webd, "Firewall");

		//verify Firewall
		VerifyOOBUtil.verifyFirewall(webd);
	}

	//@Test(alwaysRun = true)
	public void verifyFirewall_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyFirewall_ListView");

		//click on Grid View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Firewall");
		DashboardHomeUtil.selectDashboard(webd, "Firewall");

		//verify Firewall
		VerifyOOBUtil.verifyFirewall(webd);
	}

	//@Test(alwaysRun = true)
	public void verifyFirewall_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyFirewall_WithFilter_GridView");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - Security Analytics");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Firewall");
		DashboardHomeUtil.selectDashboard(webd, "Firewall");

		//verify Firewall
		VerifyOOBUtil.verifyFirewall(webd);
	}

	//@Test(alwaysRun = true)
	public void verifyFirewall_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyFirewall_WithFilter_ListView");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - Security Analytics");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click on Grid View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Firewall");
		DashboardHomeUtil.selectDashboard(webd, "Firewall");

		//verify Firewall
		VerifyOOBUtil.verifyFirewall(webd);
	}

	@Test(alwaysRun = true)
	public void verifyHostOperations_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyHostOperations_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Host Operations
		webd.getLogger().info("Open the OOB dashboard---Host Operations");
		DashboardHomeUtil.selectDashboard(webd, "Host Operations");

		//verify Host Operations
		VerifyOOBUtil.verifyHostOperations(webd);
		VerifyOOBUtil.verifyHostOperations_Details(webd);
	}

	@Test(alwaysRun = true)
	public void verifyHostOperations_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyHostOperations_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Host Operations
		webd.getLogger().info("Open the OOB dashboard---Host Operations");
		DashboardHomeUtil.selectDashboard(webd, "Host Operations");

		//verify Host Operations
		VerifyOOBUtil.verifyHostOperations(webd);
	}

	@Test(alwaysRun = true)
	public void verifyHostOperations_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyHostOperations_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Host Operations
		webd.getLogger().info("Open the OOB dashboard---Host Operations");
		DashboardHomeUtil.selectDashboard(webd, "Host Operations");

		//verify Host Operations
		VerifyOOBUtil.verifyHostOperations(webd);
	}

	@Test(alwaysRun = true)
	public void verifyHostOperations_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyHostOperations_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Host Operations
		webd.getLogger().info("Open the OOB dashboard---Host Operations");
		DashboardHomeUtil.selectDashboard(webd, "Host Operations");

		//verify Host Operations
		VerifyOOBUtil.verifyHostOperations(webd);
	}

	@Test(alwaysRun = true)
	public void verifyMiddlewareOperations_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyMiddlewareOperations_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Middleware Operations
		webd.getLogger().info("Open the OOB dashboard---Middleware Operations");
		DashboardHomeUtil.selectDashboard(webd, "Middleware Operations");

		//verify Middleware Operations
		VerifyOOBUtil.verifyMiddlewareOperations(webd);
		VerifyOOBUtil.verifyMiddlewareOperations_Details(webd);
	}

	@Test(alwaysRun = true)
	public void verifyMiddlewareOperations_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyMiddlewareOperations_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Middleware Operations
		webd.getLogger().info("Open the OOB dashboard---Middleware Operations");
		DashboardHomeUtil.selectDashboard(webd, "Middleware Operations");

		//verify Middleware Operations
		VerifyOOBUtil.verifyMiddlewareOperations(webd);
	}

	@Test(alwaysRun = true)
	public void verifyMiddlewareOperations_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyMiddlewareOperations_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Middleware Operations
		webd.getLogger().info("Open the OOB dashboard---Middleware Operations");
		DashboardHomeUtil.selectDashboard(webd, "Middleware Operations");

		//verify Middleware Operations
		VerifyOOBUtil.verifyMiddlewareOperations(webd);
	}

	@Test(alwaysRun = true)
	public void verifyMiddlewareOperations_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyMiddlewareOperations_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Middleware Operations
		webd.getLogger().info("Open the OOB dashboard---Middleware Operations");
		DashboardHomeUtil.selectDashboard(webd, "Middleware Operations");

		//verify Middleware Operations
		VerifyOOBUtil.verifyMiddlewareOperations(webd);
	}

	@Test(alwaysRun = true)
	public void verifyOrchestration_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyOrchestration_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Orchestration Workflows
		webd.getLogger().info("Open the OOB dashboard---Orchestration Workflows");
		DashboardHomeUtil.selectDashboard(webd, "Orchestration Workflows");

		//verify Orchestration Workflows
		VerifyOOBUtil.verifyOrchestration(webd);
	}

	@Test(alwaysRun = true)
	public void verifyOrchestration_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyOrchestration_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Orchestration Workflows
		webd.getLogger().info("Open the OOB dashboard---Orchestration Workflows");
		DashboardHomeUtil.selectDashboard(webd, "Orchestration Workflows");

		//verify Orchestration Workflows
		VerifyOOBUtil.verifyOrchestration(webd);
	}

	@Test(alwaysRun = true)
	public void verifyOrchestration_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyOrchestration_GridView");

		//click Filter-orchestration
		webd.getLogger().info("Click Cloud Services - orchestration");
		DashboardHomeUtil.filterOptions(webd, "orchestration");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Orchestration Workflows
		webd.getLogger().info("Open the OOB dashboard---Orchestration Workflows");
		DashboardHomeUtil.selectDashboard(webd, "Orchestration Workflows");

		//verify Orchestration Workflows
		VerifyOOBUtil.verifyOrchestration(webd);
	}

	@Test(alwaysRun = true)
	public void verifyOrchestration_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyOrchestration_ListView");

		//click Filter-orchestration
		webd.getLogger().info("Click Cloud Services - orchestration");
		DashboardHomeUtil.filterOptions(webd, "orchestration");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Orchestration Workflows
		webd.getLogger().info("Open the OOB dashboard---Orchestration Workflows");
		DashboardHomeUtil.selectDashboard(webd, "Orchestration Workflows");

		//verify Orchestration Workflows
		VerifyOOBUtil.verifyOrchestration(webd);
	}

	@Test(alwaysRun = true)
	public void verifyPerfAnalyticsApplication_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyPerfAnalyticsApplication_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Performance Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics Application Server");

		//verify Perf Analytics Application
		VerifyOOBUtil.verifyPerfAnalyticsApplication(webd);
	}

	@Test(alwaysRun = true)
	public void verifyPerfAnalyticsApplication_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyPerfAnalyticsApplication_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Performance Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics Application Server");

		//verify Perf Analytics Application
		VerifyOOBUtil.verifyPerfAnalyticsApplication(webd);
	}

	@Test(alwaysRun = true)
	public void verifyPerfAnalyticsApplication_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyPerfAnalyticsApplication_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Performance Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics Application Server");

		//verify Perf Analytics Application
		VerifyOOBUtil.verifyPerfAnalyticsApplication(webd);
	}

	@Test(alwaysRun = true)
	public void verifyPerfAnalyticsApplication_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyPerfAnalyticsApplication_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Performance Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics Application Server");

		//verify Perf Analytics Application
		VerifyOOBUtil.verifyPerfAnalyticsApplication(webd);
	}

	@Test(alwaysRun = true)
	public void verifyPerfAnalyticsDatabase_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyPerfAnalyticsDatabase_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Performance Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics: Database");

		//verify Perf Analytics Database
		VerifyOOBUtil.verifyPerfAnalyticsDatabase(webd);
	}

	@Test(alwaysRun = true)
	public void verifyPerfAnalyticsDatabase_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyPerfAnalyticsDatabase_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Performance Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics: Database");

		//verify Perf Analytics Database
		VerifyOOBUtil.verifyPerfAnalyticsDatabase(webd);
	}

	@Test(alwaysRun = true)
	public void verifyPerfAnalyticsDatabase_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyPerfAnalyticsDatabase_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Performance Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics: Database");

		//verify Perf Analytics Database
		VerifyOOBUtil.verifyPerfAnalyticsDatabase(webd);
	}

	@Test(alwaysRun = true)
	public void verifyPerfAnalyticsDatabase_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyPerfAnalyticsDatabase_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Performance Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics: Database");

		//verify Perf Analytics Database
		VerifyOOBUtil.verifyPerfAnalyticsDatabase(webd);
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsDatabase_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsDatabase_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Database");

		//verify Resource Analytics Database
		VerifyOOBUtil.verifyResourceAnalyticsDatabase(webd);
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsDatabase_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsDatabase_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Database");

		//verify Resource Analytics Database
		VerifyOOBUtil.verifyResourceAnalyticsDatabase(webd);
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsDatabase_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsDatabase_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Database");

		//verify Resource Analytics Database
		VerifyOOBUtil.verifyResourceAnalyticsDatabase(webd);
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsDatabase_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsDatabase_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Database");

		//verify Resource Analytics Database
		VerifyOOBUtil.verifyResourceAnalyticsDatabase(webd);
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsHost_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsHost_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Host");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Host");

		//verify Resource Analytics Host
		VerifyOOBUtil.verifyResourceAnalyticsHost(webd);
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsHost_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsHost_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Host");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Host");

		//verify Resource Analytics Host
		VerifyOOBUtil.verifyResourceAnalyticsHost(webd);
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsHost_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsHost_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Host");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Host");

		//verify Resource Analytics Host
		VerifyOOBUtil.verifyResourceAnalyticsHost(webd);
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsHost_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsHost_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Host");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Host");

		//verify Resource Analytics Host
		VerifyOOBUtil.verifyResourceAnalyticsHost(webd);
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsMiddleware_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsMiddleware_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Middleware");

		VerifyOOBUtil.verifyResourceAnalyticsMiddleware(webd);
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsMiddleware_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsMiddleware_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Middleware");

		//verify Resource Analytics Middleware
		VerifyOOBUtil.verifyResourceAnalyticsMiddleware(webd);
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsMiddleware_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsMiddleware_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Middleware");

		//verify Resource Analytics Middleware
		VerifyOOBUtil.verifyResourceAnalyticsMiddleware(webd);
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsMiddleware_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsMiddleware_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Middleware");

		//verify Resource Analytics Middleware
		VerifyOOBUtil.verifyResourceAnalyticsMiddleware(webd);
	}

	@Test(alwaysRun = true)
	public void verifyUIGallery_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyUIGallery_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---UI Gallery
		webd.getLogger().info("Open the OOB dashboard---UI Gallery");
		DashboardHomeUtil.selectDashboard(webd, "UI Gallery");

		//verify UI Gallery
		VerifyOOBUtil.verifyUIGallery(webd);
		VerifyOOBUtil.verifyUIGallery_Details(webd);
	}

	@Test(alwaysRun = true)
	public void verifyUIGallery_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyUIGallery_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---UI Gallery
		webd.getLogger().info("Open the OOB dashboard---UI Gallery");
		DashboardHomeUtil.selectDashboard(webd, "UI Gallery");

		//verify UI Gallery
		VerifyOOBUtil.verifyUIGallery(webd);
	}

	@Test(alwaysRun = true)
	public void verifyUIGallery_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyUIGallery_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---UI Gallery
		webd.getLogger().info("Open the OOB dashboard---UI Gallery");
		DashboardHomeUtil.selectDashboard(webd, "UI Gallery");

		//verify UI Gallery
		VerifyOOBUtil.verifyUIGallery(webd);
	}

	@Test(alwaysRun = true)
	public void verifyUIGallery_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyUIGallery_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---UI Gallery
		webd.getLogger().info("Open the OOB dashboard---UI Gallery");
		DashboardHomeUtil.selectDashboard(webd, "UI Gallery");

		//verify UI Gallery
		VerifyOOBUtil.verifyUIGallery(webd);
	}
}
