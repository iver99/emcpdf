package oracle.sysman.emaas.platform.dashboards.test.dashboard;

import java.math.BigInteger;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class DashboardImportExport {

	/**
	 * Calling CommonTest.java to Set up RESTAssured defaults & Reading the inputs from the testenv.properties file before
	 * executing test cases
	 */

	static String HOSTNAME;
	static String portno;
	static String serveruri;
	static String authToken;
	static String tenantid;
	static String tenantid_2;
	static String remoteuser;
	
	String dashboard_id1 = "";
	String dashboard_id2 = "";
	String dashboard_id3 = "";
	String widget_id = "";
	
	private static final Logger LOGGER = LogManager.getLogger(DashboardCRUD.class);

	@BeforeClass
	public static void setUp()
	{
		CommonTest ct = new CommonTest();
		HOSTNAME = ct.getHOSTNAME();
		portno = ct.getPortno();
		serveruri = ct.getServeruri();
		authToken = ct.getAuthToken();
		tenantid = ct.getTenantid();
		tenantid_2 = ct.getTenantid2();
		remoteuser = ct.getRemoteUser();
	}	
	
	@Test(groups = "Group1")
	public void dashboardSetCreated()
	{
		try {
			
			String jsonString1 = "{\"type\":\"SET\",\"name\":\"non_oob_dashboardSet\",\"showInHome\":true,\"description\":\"\",\"enableTimeRange\":\"TRUE\",\"enableRefresh\":true,\"federationSupported\":\"NON_FEDERATION_ONLY\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			
			Assert.assertTrue(res1.getStatusCode() == 201);

			dashboard_id1 = res1.jsonPath().getString("id");
			
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/" + dashboard_id1);
			
			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertEquals(res2.jsonPath().get("name"), "non_oob_dashboardSet");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
	}
	/*
	 * Export multi-dashboards(oob+non oob, oob+oob)
	 * Export when with wrong dashboard name
	 * Export when with wrong name/blank in dashboard name
	 */
	//@Test(dependsOnMethods = { "dashboardSetCreated" })
	@Test(groups = "Group2", dependsOnGroups = {"Group1"})
	public void exportMultiDashboards()
	{
		try {

			String jsonString0 = "[ \"UI Gallery\",\"Host Operations\"]";

			Response res0 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString0).when().put("/dashboards/export");
		
			Assert.assertTrue(res0.getStatusCode() == 200);
			
			String bodyAsString0 = res0.getBody().asString();
			Assert.assertEquals(bodyAsString0.contains("UI Gallery"), true);
			Assert.assertEquals(bodyAsString0.contains("Host Operation"), true);
			
			
			String jsonString1 = "[ \"non_oob_dashboardSet\",\"Host Operations\"]";

			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("/dashboards/export");
		
			Assert.assertTrue(res1.getStatusCode() == 200);
			
			String bodyAsString1 = res1.getBody().asString();
			Assert.assertEquals(bodyAsString1.contains("non_oob_dashboardSet"), true);
			Assert.assertEquals(bodyAsString1.contains("Host Operation"), true);
			
			
			String jsonString2 = "[ \"test_dashboard_tile1\",\"UI Gallery\"]";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().put("/dashboards/export");
			

			System.out.print("***Iris***");
			System.out.print(res2.getStatusCode());
	
			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertEquals(res2.jsonPath().get("Dashboard[0].name[0]"), "Timeseries - Line Basic");
			Assert.assertEquals(res2.jsonPath().get("Savedsearch[0].name[0]"), "Line Chart");
			//Assert.assertEquals(res2.jsonPath().getString("$[0].Dashboard[0].name[0]"), "Timeseries - Line Basic");
	

			String jsonString3 = "[ \"test_dashboard_tile1\"]";
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString3).when().put("/dashboards/export");
			
			Assert.assertTrue(res3.getStatusCode() == 404);
			Assert.assertEquals(res3.jsonPath().get("errorCode"), 20001);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"), "Specified dashboard is not found");
			
			String jsonString4 = "[]";
			Response res4 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString4).when().put("/dashboards/export");
			
			Assert.assertTrue(res4.getStatusCode() == 400);
			
			Assert.assertEquals(res4.jsonPath().get("success"), false);
			Assert.assertEquals(res4.jsonPath().get("msg"), "Error to export dashboard as no input dashboard names");
			
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}

	}
	
	/*
	 * Export OOB dashboard, verify dashboard fields(name, title number, searches number...etc.).
	 * Import OOB dashboard + oob search + override = false ==> 201 created
	 * Import OOB dashboard + oob search + override = true ==> 400 error
	 * Export non OOB dashboard, verify dashboard fields(name, title number, searches number...etc.)
	 * */
	@Test(groups = "Group4",dependsOnGroups = {"Group3"}) 
	public void exportImportDashboard()
	{
		try {
			
			String jsonString1 = "[\"Host Operations\"]";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("/dashboards/export");
		
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertEquals(res1.jsonPath().get("Dashboard[0].name[0]"), "Host Operations");
			
			//Verify the tiles number
			List <String> tile_list = res1.jsonPath().getList("Dashboard[0].tiles[0].title");			
			Assert.assertEquals(4, tile_list.size());
			
			//Verify the searches number
			List <String> saveSearch_list = res1.jsonPath().getList("Savedsearch[0].name");
			Assert.assertEquals(4, saveSearch_list.size());
			
			String jsonBodyString1 = "";
			String jsonBodyString2 = "";
			String jsonBodyString3 = "";
			String jsonString2 = "";
			
		    jsonBodyString1 = res1.getBody().asString();

			jsonBodyString2 = jsonBodyString1.replace("\"owner\":\"Oracle\"", "\"owner\":\"Oracle1\"");
			jsonBodyString3 = jsonBodyString2.replace("\"owner\":\"ORACLE\"", "\"owner\":\"ORACLE1\"");
			jsonString2 = jsonBodyString3.replace("\"name\":\"Host Operations\"", "\"name\":\"Host Operations_Custom\"");											  
		    		    
		    Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().put("/dashboards/import?override=false");
		
			Assert.assertTrue(res2.getStatusCode() == 201);
			
			Response res3 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).body(jsonString2).when().put("/dashboards/import?override=true");
			
			Assert.assertTrue(res3.getStatusCode() == 400);
			
			Assert.assertEquals(res3.jsonPath().get("success"), false);
			Assert.assertEquals(res3.jsonPath().get("msg"), "Could not import SSF data successfully!(#1.Are you attempting to override OOB dashboards or search? If yes, please specify override=false and try again. #2.Did you increase the max connection pool size in weblogic console? If not, please increase it into 200 then retry.)");
		
			//Export the non-oob dashboard imported from res2
			String jsonStringTmp = "";
			String str = "[\"";
			String jsonString3 = "";
			String jsonString_dashboardName = res2.jsonPath().get("name[0]");
			jsonStringTmp = jsonString_dashboardName.concat("\"]");
			jsonString3 = str.concat(jsonStringTmp);
			System.out.print("***"+jsonString3+"***");
			
			//Record the tiles number
			List <String> tile_list_res2 = res2.jsonPath().getList("tiles[0].title");			
			
			Response res4 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString3).when().put("/dashboards/export");
		
			Assert.assertTrue(res4.getStatusCode() == 200);
			Assert.assertEquals(res4.jsonPath().get("Dashboard[0].name[0]"), jsonString_dashboardName);
			
			//Verify the tiles number
			List <String> tile_list_res4 = res4.jsonPath().getList("Dashboard[0].tiles[0].title");
			Assert.assertEquals(tile_list_res2.size(),tile_list_res4.size());
		}
		catch (Exception e) {
			System.out.print("***This is output in catch***");
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}

	}
	
	/*
	 * Export OOB Dashboard SET, verify dashboard fields(name, title number, searches number...etc)
	 */
	@Test(groups = "Group3", dependsOnGroups = {"Group2"})
	public void exportOOBDashboardSet()
	{
		try {			
			String jsonString1 = "[\"UI Gallery\"]";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("/dashboards/export");				
	
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertEquals(res1.jsonPath().get("Dashboard[0].name[0]"), "Timeseries - Line Basic");
			Assert.assertEquals(res1.jsonPath().get("Savedsearch[0].name[0]"), "Line Chart");
			
			//Verify the tiles number under "Timeseries - Line Basic" tab
			List <String> tile_list = res1.jsonPath().getList("Dashboard[0].tiles[0].title");	
			//System.out.print("***"+tile_list.size()+"***");
			Assert.assertEquals(4, tile_list.size());			
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}

	}
	
	/*
	 * Export non OOB dashboard SET, verify dashboard fields(name, title number, searches number...etc.)
	 */
//	@Test(dependsOnMethods = { "dashboardSetCreated" })
	@Test(groups = "Group2", dependsOnGroups = {"Group1"})
	public void exportNonOOBDashboardSet()
	{
		try {
			
			String jsonString1 = "[ \"non_oob_dashboardSet\"]";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("/dashboards/export");							
	
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertEquals(res1.jsonPath().get("Dashboard[0].name[0]"), "non_oob_dashboardSet");
			//Assert.assertEquals(res1.jsonPath().get("Savedsearch[0].name[0]"), "Line Chart");
			//Assert.assertEquals(res2.jsonPath().getString("$[0].Dashboard[0].name[0]"), "Timeseries - Line Basic");			
			
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}

	}
	
	@Test(groups = "Group1")
	public void dashboardWithTileCreated()
	{
		try
		{
			String jsonString1 = "{\"type\":\"NORMAL\",\"name\":\"non_oob_dashboard\",\"showInHome\":true,\"description\":\"\",\"enableTimeRange\":\"TRUE\",\"enableRefresh\":true,\"federationSupported\":\"NON_FEDERATION_ONLY\"}";
			
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			
			
			Assert.assertTrue(res1.getStatusCode() == 201);
	
			dashboard_id2 = res1.jsonPath().getString("id");
			
			String jsonString2 = "{\"name\":\"non_oob_dashboard\",\"enableDescription\":\"FALSE\",\"enableTimeRange\":\"TRUE\",\"enableEntityFilter\":\"TRUE\",\"enableRefresh\":true,\"showInHome\":true,\"sharePublic\":false,\"type\":\"NORMAL\",\"federationSupported\":\"NON_FEDERATION_ONLY\",\"selectedSsData\":\"[]\",\"id\":" 
								 + dashboard_id2 
								 + ",\"tiles\":[{\"title\":\"Access Log Error Status Codes\",\"description\":null,\"isMaximized\":false,\"column\":0,\"row\":0,\"width\":6,\"height\":2,\"federationSupported\":\"NON_FEDERATION_ONLY\",\"WIDGET_UNIQUE_ID\":\"2017\",\"WIDGET_NAME\":\"Access Log Error Status Codes\",\"WIDGET_DESCRIPTION\":\"Top 4xx and 5xx errors codes in HTTP Access Logs. \",\"WIDGET_OWNER\":\"ORACLE\",\"WIDGET_CREATION_TIME\":\"2015-12-15T02:31:03.057Z\",\"WIDGET_SOURCE\":1,\"WIDGET_GROUP_NAME\":\"Log Analytics\",\"WIDGET_SCREENSHOT_HREF\":\"https://den01mjk.us.oracle.com:4443/sso.static/savedsearch.widgets/2017/screenshot/1.26.0-171205.185320/images/1450146663057_2017.png\",\"WIDGET_SUPPORT_TIME_CONTROL\":true,\"WIDGET_KOC_NAME\":\"emcla-visualization\",\"WIDGET_TEMPLATE\":\"/html/search/widgets/visualizationWidget.html\",\"WIDGET_VIEWMODEL\":\"/js/viewmodel/search/widget/VisualizationWidget.js\",\"PROVIDER_NAME\":\"LogAnalyticsUI\",\"PROVIDER_VERSION\":\"1.0\",\"PROVIDER_ASSET_ROOT\":\"assetRoot\",\"WIDGET_EDITABLE\":\"false\",\"index\":17,\"WIDGET_VISUAL\":\"/sso.static/savedsearch.widgets/2017/screenshot/1.26.0-171205.185320/images/1450146663057_2017.png\",\"imgWidth\":\"190px\",\"imgHeight\":\"138.64864864864865px\",\"highlightedName\":\"Access Log Error Status Codes\",\"highlightedDescription\":\"Top 4xx and 5xx errors codes in HTTP Access Logs. \",\"highlightedSource\":\"Source: Log Analytics\",\"highlightedOwner\":\"Created by: ORACLE\",\"isSelected\":false,\"isScreenShotPageDisplayed\":true,\"isScreenshotLoaded\":false,\"modificationDateString\":\"2 year ago\",\"id\":\"rid_1\",\"type\":\"DEFAULT\"}],\"screenShot\":\"data:image/jpeg;base64,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\",\"description\":\"\"}";
			
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().put("/dashboards/" + dashboard_id2);
			
			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertEquals(res2.jsonPath().get("name"), "non_oob_dashboard");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
	}
	
	/*
	 * Export non-oob dashboard + oob search, then
	 * Import non-oob dashboard + oob search + override = false ==> 201 created
	 * Import non-oob dashboard + oob search + override = true ==> 400 error
	 */
	//@Test(dependsOnMethods = { "dashboardWithTileCreated" })
	@Test(groups = "Group2", dependsOnGroups = {"Group1"})
	public void importNonOOBDashboardOOBSearch()
	{
		try {
			
			String jsonString1 = "[\"non_oob_dashboard\"]";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("/dashboards/export");
		
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertEquals(res1.jsonPath().get("Dashboard[0].name[0]"), "non_oob_dashboard");
			
			//Verify the tiles number
			List <String> tile_list = res1.jsonPath().getList("Dashboard[0].tiles[0].title");			
			Assert.assertEquals(1, tile_list.size());
			
			//Verify the searches number
			List <String> saveSearch_list = res1.jsonPath().getList("Savedsearch[0].name");
			Assert.assertEquals(1, saveSearch_list.size());
			
			String jsonBodyString1 = "";
			String jsonString2 = "";
			
		    jsonBodyString1 = res1.getBody().asString();

			jsonBodyString1 = jsonBodyString1.replace("\"owner\":\"ORACLE\"", "\"owner\":\"Oracle1\"");

			jsonString2 = jsonBodyString1.replace("\"name\":\"non_oob_dashboard\"", "\"name\":\"non_oob_dashboard_Custom\"");											  
		    		    
		    Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().put("/dashboards/import?override=false");
		
			Assert.assertTrue(res2.getStatusCode() == 201);
			
			Response res3 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).body(jsonString2).when().put("/dashboards/import?override=true");
			
			Assert.assertTrue(res3.getStatusCode() == 400);
			
			Assert.assertEquals(res3.jsonPath().get("success"), false);
			Assert.assertEquals(res3.jsonPath().get("msg"), "Could not import SSF data successfully!(#1.Are you attempting to override OOB dashboards or search? If yes, please specify override=false and try again. #2.Did you increase the max connection pool size in weblogic console? If not, please increase it into 200 then retry.)");
		}
		catch (Exception e) {
			System.out.print("***This is output in catch***");
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}

	}
	
	@Test(groups = "Group5",dependsOnGroups = {"Group4"})
	public void dashboardWithNonOOBTileCreated()
	{
		try
		{
		    //Create an non oob widget		
			String jsonString0 = "{\"name\":\"Custom_widget\",\"category\":{\"id\":2},\"folder\":{\"id\":4},\"description\":\"\",\"queryStr\":\"\",\"isWidget\":true,\"systemSearch\":false,\"parameters\":[{\"name\":\"PROVIDER_NAME\",\"type\":\"STRING\",\"value\":\"TargetAnalytics\"},{\"name\":\"PROVIDER_VERSION\",\"type\":\"STRING\",\"value\":\"1.1\"},{\"name\":\"PROVIDER_ASSET_ROOT\",\"type\":\"STRING\",\"value\":\"assetRoot\"},{\"name\":\"WIDGET_KOC_NAME\",\"type\":\"STRING\",\"value\":\"emcta-visualization\"},{\"name\":\"WIDGET_SOURCE\",\"value\":\"1\",\"type\":\"STRING\"},{\"name\":\"WIDGET_VIEWMODEL\",\"type\":\"STRING\",\"value\":\"/widget/visualizationWidget/js/VisualizationWidget.js\"},{\"name\":\"WIDGET_TEMPLATE\",\"type\":\"STRING\",\"value\":\"/widget/visualizationWidget/visualizationWidget.html\"},{\"name\":\"TA_STACKEDBAR_WIDGET_TILE_SHOW_THRESHOLD\",\"type\":\"STRING\",\"value\":false},{\"name\":\"TA_STACKEDBAR_WIDGET_OTHER_METRICS_CRITERIA\",\"type\":\"CLOB\",\"value\":\"[]\"},{\"name\":\"TA_STACKEDBAR_WIDGET_SHOW_CALLOUT\",\"type\":\"STRING\",\"value\":false},{\"name\":\"TA_STACKEDBAR_WIDGET_CALLOUT\",\"type\":\"STRING\",\"value\":\"none\"},{\"name\":\"TA_STACKEDBAR_WIDGET_SHOW_STACKED\",\"type\":\"STRING\",\"value\":\"off\"},{\"name\":\"TA_STACKEDBAR_WIDGET_SHOW_KEY_NAME_SELECTION\",\"type\":\"CLOB\",\"value\":[\"no\"]},{\"name\":\"TA_STACKEDBAR_WIDGET_SHARE_SAME_YAXIS\",\"type\":\"STRING\",\"value\":false},{\"name\":\"TA_STACKEDBAR_WIDGET_ENABLE_RANKING\",\"type\":\"STRING\",\"value\":false},{\"name\":\"TA_STACKEDBAR_WIDGET_TOP_N_CONFIG\",\"type\":\"CLOB\",\"value\":\"{}\"},{\"name\":\"VISUALIZATION_TYPE_KEY\",\"value\":\"STACKEDBAR\",\"type\":\"STRING\"},{\"name\":\"SELECTED_TIME_PERIOD\",\"value\":\"Last 30 days\",\"type\":\"STRING\"}]}";
			
			RestAssured.basePath = "/savedsearch/v1";
			Response res0 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString0).when().post("/search");
			
			RestAssured.basePath = "/emcpdf/api/v1";
			
			Assert.assertTrue(res0.getStatusCode() == 201);
			
		//	System.out.println("#1, id is "+ res0.jsonPath().getString("id"));
			BigInteger id = new BigInteger(res0.jsonPath().getString("id"));
			//widget_id = "\"" + id + "\"";
			widget_id = id.toString();
			System.out.println("#2, id is "+ widget_id);
			//Create an non oob dashboard
			String jsonString1 = "{\"type\":\"NORMAL\",\"name\":\"non_oob_dashboard_non_oob_tile\",\"showInHome\":true,\"description\":\"\",\"enableTimeRange\":\"TRUE\",\"enableRefresh\":true,\"federationSupported\":\"NON_FEDERATION_ONLY\"}";
			
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			
			
			Assert.assertTrue(res1.getStatusCode() == 201);
	
			dashboard_id3 = res1.jsonPath().getString("id");
			
			//Update the above dashboard, add the Custom_widget into this dashboard
			//String jsonString2 = "{\"name\":\"non_oob_dashboard_non_oob_tile\",\"enableDescription\":\"FALSE\",\"enableTimeRange\":\"TRUE\",\"enableEntityFilter\":\"TRUE\",\"enableRefresh\":true,\"showInHome\":true,\"sharePublic\":false,\"type\":\"NORMAL\",\"federationSupported\":\"NON_FEDERATION_ONLY\",\"selectedSsData\":\"[]\",\"id\":"
							//   + dashboard_id3
							//   + ",\"tiles\":[{\"title\":\"Custom_widget\",\"description\":\"\",\"isMaximized\":false,\"column\":0,\"row\":0,\"width\":4,\"height\":2,\"federationSupported\":\"NON_FEDERATION_ONLY\",\"WIDGET_UNIQUE_ID\":"
							//   + widget_id
							//   + ",\"WIDGET_NAME\":\"Custom_widget\",\"WIDGET_DESCRIPTION\":\"No description.\",\"WIDGET_OWNER\":\"emcsadmin\",\"WIDGET_CREATION_TIME\":\"2017-12-26T08:09:22.830Z\",\"WIDGET_SOURCE\":1,\"WIDGET_GROUP_NAME\":\"Data Explorer\",\"WIDGET_SCREENSHOT_HREF\":\"https://den01mjk.us.oracle.com:4443/sso.static/savedsearch.widgets/198385915441621118796883088737696717732/screenshot/1.26.0-171205.185320/images/1514275762830_198385915441621118796883088737696717732.png\",\"WIDGET_KOC_NAME\":\"emcta-visualization\",\"WIDGET_TEMPLATE\":\"/widget/visualizationWidget/visualizationWidget.html\",\"WIDGET_VIEWMODEL\":\"/widget/visualizationWidget/js/VisualizationWidget.js\",\"PROVIDER_NAME\":\"TargetAnalytics\",\"PROVIDER_VERSION\":\"1.0\",\"PROVIDER_ASSET_ROOT\":\"assetRoot\",\"WIDGET_EDITABLE\":\"true\",\"index\":303,\"WIDGET_VISUAL\":\"/sso.static/savedsearch.widgets/198385915441621118796883088737696717732/screenshot/1.26.0-171205.185320/images/1514275762830_198385915441621118796883088737696717732.png\",\"imgWidth\":\"190px\",\"imgHeight\":\"138.64864864864865px\",\"highlightedName\":\"<span class='widget-selector-search-matching'>Custom</span>_widget\",\"highlightedDescription\":\"No description.\",\"highlightedSource\":\"Source: Data Explorer\",\"highlightedOwner\":\"Created by: emcsadmin\",\"isSelected\":false,\"isScreenShotPageDisplayed\":true,\"isScreenshotLoaded\":false,\"modificationDateString\":\"a moment ago\",\"id\":\"rid_2\",\"type\":\"DEFAULT\"}],\"screenShot\":\"data:image/jpeg;base64,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\"}";	
			
			String jsonString2 = "{\"name\":\"non_oob_dashboard_non_oob_tile\",\"enableDescription\":\"FALSE\",\"enableTimeRange\":\"TRUE\",\"enableEntityFilter\":\"TRUE\",\"enableRefresh\":true,\"showInHome\":true,\"sharePublic\":false,\"type\":\"NORMAL\",\"federationSupported\":\"NON_FEDERATION_ONLY\",\"selectedSsData\":\"[]\",\"id\":"
					           + dashboard_id3
					           + ",\"tiles\":[{\"title\":\"Custom_widget\",\"description\":\"\",\"isMaximized\":false,\"column\":0,\"row\":0,\"width\":4,\"height\":2,\"federationSupported\":\"NON_FEDERATION_ONLY\",\"WIDGET_UNIQUE_ID\":"
					           + "\"" + id + "\"" //FIXME 
					           + ",\"WIDGET_NAME\":\"Custom_widget\",\"WIDGET_DESCRIPTION\":\"No description.\",\"WIDGET_OWNER\":\"emcsadmin\",\"WIDGET_CREATION_TIME\":\"2017-12-27T05:28:43.894Z\",\"WIDGET_SOURCE\":1,\"WIDGET_GROUP_NAME\":\"Data Explorer\",\"WIDGET_SCREENSHOT_HREF\":\"https://den01mjk.us.oracle.com:4443/sso.static/savedsearch.widgets/208182464360857870928739285911849887945/screenshot/1.26.0-171205.185320/images/1514352523894_208182464360857870928739285911849887945.png\",\"WIDGET_KOC_NAME\":\"emcta-visualization\",\"WIDGET_TEMPLATE\":\"/widget/visualizationWidget/visualizationWidget.html\",\"WIDGET_VIEWMODEL\":\"/widget/visualizationWidget/js/VisualizationWidget.js\",\"PROVIDER_NAME\":\"TargetAnalytics\",\"PROVIDER_VERSION\":\"1.1\",\"PROVIDER_ASSET_ROOT\":\"assetRoot\",\"WIDGET_EDITABLE\":\"true\",\"index\":327,\"WIDGET_VISUAL\":\"/sso.static/savedsearch.widgets/208182464360857870928739285911849887945/screenshot/1.26.0-171205.185320/images/1514352523894_208182464360857870928739285911849887945.png\",\"imgWidth\":\"190px\",\"imgHeight\":\"138.64864864864865px\",\"highlightedName\":\"<span class='widget-selector-search-matching'>Custom</span>_widget\",\"highlightedDescription\":\"No description.\",\"highlightedSource\":\"Source: Data Explorer\",\"highlightedOwner\":\"Created by: emcsadmin\",\"isSelected\":false,\"isScreenShotPageDisplayed\":true,\"isScreenshotLoaded\":false,\"modificationDateString\":\"9 minutes ago\",\"id\":\"rid_2\",\"type\":\"DEFAULT\"}],\"screenShot\":\"data:image/jpeg;base64,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\"}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().put("/dashboards/" + dashboard_id3);
			
			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertEquals(res2.jsonPath().get("name"), "non_oob_dashboard_non_oob_tile");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
	}
	
	/*
	 * Export non-oob dashboard + non-oob search, then
	 * Import non-oob dashboard + non-oob search + override = false ==> 201 created
	 * Import non-oob dashboard + non-oob search + override = true ==> 201
	 */
	@Test(groups = "Group6", dependsOnGroups = {"Group5"})
	public void importNonOOBDashboardNonOOBSearch()
	{
		try {
			
			String jsonString1 = "[\"non_oob_dashboard_non_oob_tile\"]";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("/dashboards/export");
		
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertEquals(res1.jsonPath().get("Dashboard[0].name[0]"), "non_oob_dashboard_non_oob_tile");
			
			//Verify the tiles number
			List <String> tile_list = res1.jsonPath().getList("Dashboard[0].tiles[0].title");			
			Assert.assertEquals(1, tile_list.size());
			
			//Verify the searches number
			List <String> saveSearch_list = res1.jsonPath().getList("Savedsearch[0].name");
			Assert.assertEquals(1, saveSearch_list.size());
			
			String jsonBodyString1 = "";
			String jsonString2 = "";
			
		    jsonBodyString1 = res1.getBody().asString();

			jsonString2 = jsonBodyString1.replace("\"name\":\"non_oob_dashboard_non_oob_tile\"", "\"name\":\"non_oob_dashboard_non_oob_tile_Custom\"");											  
		    		    
		    Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().put("/dashboards/import?override=false");
		
			Assert.assertTrue(res2.getStatusCode() == 201);
			
			Response res3 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).body(jsonString2).when().put("/dashboards/import?override=true");
			
			Assert.assertTrue(res3.getStatusCode() == 201);
			
			//Assert.assertEquals(res3.jsonPath().get("success"), false);
			//Assert.assertEquals(res3.jsonPath().get("msg"), "Could not import SSF data successfully!(#1.Are you attempting to override OOB dashboards or search? If yes, please specify override=false and try again. #2.Did you increase the max connection pool size in weblogic console? If not, please increase it into 200 then retry.)");
		}
		catch (Exception e) {
			System.out.print("***This is output in catch***");
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}

	}
	
	@AfterClass
	public void deleteData()
	{
		try{
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/dashboards/" + dashboard_id1);
		
			Assert.assertTrue(res1.getStatusCode() == 204);
			
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/dashboards/" + dashboard_id2);
		
			Assert.assertTrue(res2.getStatusCode() == 204);
			
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/dashboards/" + dashboard_id3);
		
			Assert.assertTrue(res3.getStatusCode() == 204);
			
			RestAssured.basePath = "/savedsearch/v1";
			Response res4 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/search/" + widget_id);
			
			RestAssured.basePath = "/emcpdf/api/v1";
			
			Assert.assertTrue(res4.getStatusCode() == 204);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
	}
}
