package oracle.sysman.emaas.platform.dashboards.test.dashboard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class DashboardCRUD
{
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

	@Test
	public void dashboardCreateEmptyPara()
	{
		try {
			
			String jsonString1 = "{ \"name\":\"\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			
			Assert.assertTrue(res1.getStatusCode() == 400);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Invalid dashboard name. Name can not be empty and must be less than 64 characters");
			

			String jsonString2 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"\"}]}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().post("/dashboards");
			
			Assert.assertTrue(res2.getStatusCode() == 400);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"), "Title for tile is required");

			String jsonString3 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"\"}]}";
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString3).when().post("/dashboards");
			
			Assert.assertTrue(res3.getStatusCode() == 400);
			Assert.assertEquals(res3.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"), "\"WIDGET_NAME\" is required for tile(s) of the dashboard");

			String jsonString4 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"\"}]}";
			Response res4 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString4).when().post("/dashboards");
			
			Assert.assertTrue(res4.getStatusCode() == 400);
			Assert.assertEquals(res4.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res4.jsonPath().get("errorMessage"),
					"\"WIDGET_UNIQUE_ID\" is required for tile(s) of the dashboard");

			String jsonString5 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"\"}]}";
			Response res5 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString5).when().post("/dashboards");
			
			Assert.assertTrue(res5.getStatusCode() == 400);
			Assert.assertEquals(res5.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res5.jsonPath().get("errorMessage"), "\"WIDGET_OWNER\" is required for tile(s) of the dashboard");

			String jsonString6 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"\"}]}";
			Response res6 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString6).when().post("/dashboards");
			
			Assert.assertTrue(res6.getStatusCode() == 400);
			Assert.assertEquals(res6.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res6.jsonPath().get("errorMessage"),
					"\"WIDGET_CREATION_TIME\" is required for tile(s) of the dashboard");

			String jsonString7 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": \"\"}]}";
			Response res7 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString7).when().post("/dashboards");
			
			Assert.assertTrue(res7.getStatusCode() == 400);
			Assert.assertEquals(res7.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res7.jsonPath().get("errorMessage"), "\"WIDGET_SOURCE\" is required for tile(s) of the dashboard");

			String jsonString8 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"\"}]}";
			Response res8 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString8).when().post("/dashboards");
			
			Assert.assertTrue(res8.getStatusCode() == 400);
			Assert.assertEquals(res8.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res8.jsonPath().get("errorMessage"),
					"\"WIDGET_KOC_NAME\" is required for tile(s) of the dashboard");

			String jsonString9 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\":\"\"}]}";
			Response res9 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString9).when().post("/dashboards");
			
			Assert.assertTrue(res9.getStatusCode() == 400);
			Assert.assertEquals(res9.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res9.jsonPath().get("errorMessage"),
					"\"WIDGET_VIEWMODEL\" is required for tile(s) of the dashboard");

			String jsonString10 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\":\"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\":\"\"}]}";
			Response res10 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString10).when().post("/dashboards");
			
			Assert.assertTrue(res10.getStatusCode() == 400);
			Assert.assertEquals(res10.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res10.jsonPath().get("errorMessage"),
					"\"WIDGET_TEMPLATE\" is required for tile(s) of the dashboard");

			String jsonString11 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\":\"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\":\"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"\"}]}";
			Response res11 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString11).when().post("/dashboards");
			Assert.assertTrue(res11.getStatusCode() == 400);
			Assert.assertEquals(res11.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res11.jsonPath().get("errorMessage"),
					"\"PROVIDER_NAME\" is required for tile(s) of the dashboard");

			String jsonString12 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\":\"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\":\"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"X Analytics\",\"PROVIDER_VERSION\": \"\"}]}";
			Response res12 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString12).when().post("/dashboards");
			
			Assert.assertTrue(res12.getStatusCode() == 400);
			Assert.assertEquals(res12.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res12.jsonPath().get("errorMessage"),
					"\"PROVIDER_VERSION\" is required for tile(s) of the dashboard");

			String jsonString13 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\":\"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\":\"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"X Analytics\",\"PROVIDER_VERSION\": \"1.0\",\"PROVIDER_ASSET_ROOT\": \"\"}]}";
			Response res13 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString13).when().post("/dashboards");
			
			Assert.assertTrue(res13.getStatusCode() == 400);
			Assert.assertEquals(res13.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res13.jsonPath().get("errorMessage"),
					"\"PROVIDER_ASSET_ROOT\" is required for tile(s) of the dashboard");

			String jsonString14 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\":\"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\":\"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"X Analytics\",\"PROVIDER_VERSION\": \"1.0\",\"PROVIDER_ASSET_ROOT\": \"assetRoot\",\"tileParameters\": [{\"name\": \"\"}]}]}";
			Response res14 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString14).when().post("/dashboards");
			
			Assert.assertTrue(res14.getStatusCode() == 400);
			Assert.assertEquals(res14.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res14.jsonPath().get("errorMessage"), "\"Name\" is required for tile parameter(s) of the tile");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}

	}

	@Test
	public void dashboardCreateLongNameDesc()
	{
		try {
			
			String jsonString1 = "{ \"name\":\"long name long name long name long name long name long name long name longg\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			
			Assert.assertTrue(res1.getStatusCode() == 400);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Invalid dashboard name. Name can not be empty and must be less than 64 characters");
			
			String jsonString2 = "{ \"name\":\"long description\", \"description\":\"long description long description long description long description long description long description long description long description long description long description long description long description long description long description long description lo\"}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().post("/dashboards");
			
			Assert.assertTrue(res2.getStatusCode() == 400);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"Invalid dashboard description. Description must be less than 256 characters");

			
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}

	}

	@Test
	public void dashboardCreateSimple()
	{

		/* create a dashboard with required field
		 *
		 */
		String dashboard_id = "";
		try {
			

			String jsonString1 = "{ \"name\":\"Test_Dashboard4\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");		
			Assert.assertTrue(res1.getStatusCode() == 201);
			
			dashboard_id = res1.jsonPath().getString("id");

			
			Response res4 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/" + dashboard_id);
			
			Assert.assertTrue(res4.getStatusCode() == 200);
			Assert.assertEquals(res4.jsonPath().getString("name"), "Test_Dashboard4");
			Assert.assertEquals(res4.jsonPath().getString("type"), "NORMAL");
			Assert.assertEquals(res4.jsonPath().getString("id"), dashboard_id);
			Assert.assertEquals(res4.jsonPath().getBoolean("systemDashboard"), false);
			Assert.assertEquals(res4.jsonPath().getString("tiles"), "[]");
			
			String jsonString3 = "{ \"name\":\"Test_Dashboard4\"}";
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString3).when().post("/dashboards");
			
			Assert.assertTrue(res3.getStatusCode() == 400);
			Assert.assertEquals(res3.jsonPath().getString("errorCode"), "10001");
			Assert.assertEquals(res3.jsonPath().getString("errorMessage"), "Dashboard with the same name exists already");
			
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
		finally {
			if (!("").equals(dashboard_id)) {
				
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
			
		}
	}

	@Test
	public void dashboardCreateWithTile()
	{
		String dashboard_id = "";
		try {
			

			String jsonString1 = "{\"name\": \"test_dashboard_tile\",\"tiles\":[{\"title\":\"A sample tile for search\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_NAME\": \"iFrame\",\"WIDGET_DESCRIPTION\": \"A widget which can show another page inside a iFrame\",\"WIDGET_ICON\": \"dependencies/built-in/iFrame/images/icon.png\",\"WIDGET_HISTOGRAM\": \"dependencies/built-in/iFrame/images/histogram.png\",\"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\", \"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\": \"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\": \"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\": \"dependencies/built-in/iFrame/iFrame.html\", \"PROVIDER_NAME\": \"X Analytics\", \"PROVIDER_VERSION\": \"1.0\",\"PROVIDER_ASSET_ROOT\": \"asset\"}]}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			
			Assert.assertTrue(res1.getStatusCode() == 201);

			dashboard_id = res1.jsonPath().getString("id");
			
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/" + dashboard_id);
			
			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertEquals(res2.jsonPath().get("tiles.title[0]"), "A sample tile for search");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
		finally {
			if (!("").equals(dashboard_id)) {
				
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
			
		}
	}

	@Test
	public void dashboardCreateOOB()
	{
		try {
			String jsonString1 = "{ \"name\":\"New_OOB_Dashboard\", \"systemDashboard\": true}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			Assert.assertTrue(res1.getStatusCode() == 403);
			Assert.assertEquals(res1.jsonPath().getString("errorCode"), "30002");
			Assert.assertEquals(res1.jsonPath().getString("errorMessage"), "Not support to create system dashboard");
		}
		catch (Exception e){
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
	}

	@Test
	public void dashboardDeleteInvalidId()
	{
		try {
			
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/dashboards/99999999");
			
			Assert.assertTrue(res1.getStatusCode() == 404);
			Assert.assertEquals(res1.jsonPath().getString("errorCode"), "20001");
			Assert.assertEquals(res1.jsonPath().getString("errorMessage"), "Specified dashboard is not found");
			
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
	}

	@Test
	public void dashboardDeleteSytemDashboard()
	{
		try {
			

			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/dashboards/2");
			
			Assert.assertTrue(res1.getStatusCode() == 403);
			Assert.assertEquals(res1.jsonPath().getString("errorCode"), "30001");
			Assert.assertEquals(res1.jsonPath().getString("errorMessage"), "Not support to delete system dashboard");
		
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}

	}

	@Test
	public void dashboardLastAccess()
	{
		String dashboard_id = "";
		try {
			
			String jsonString1 = "{ \"name\":\"Test_LastAccess\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			
			Assert.assertTrue(res1.getStatusCode() == 201);
			

			dashboard_id = res1.jsonPath().getString("id");

			
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards");
			
			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertEquals(res2.jsonPath().get("dashboards.name[0]"), "Test_LastAccess");
			Assert.assertEquals(res2.jsonPath().getString("dashboards.id[0]"), dashboard_id);

			
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/2");
			
			Assert.assertTrue(res3.getStatusCode() == 200);

			Response res4 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards");
			
			Assert.assertTrue(res4.getStatusCode() == 200);
			

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
		finally {
			if (!("").equals(dashboard_id)) {
				
				Response res = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				
				Assert.assertTrue(res.getStatusCode() == 204);
			}
			
		}
	}

	

	@Test
	public void dashboardOrderByCreationDate()
	{
		String dashboard_id = "";
		try {
			
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards?offset=0&limit=240&orderBy=creation_Date");
			Assert.assertTrue(res1.getStatusCode() == 200);
			List<String> list_dashboards = res1.jsonPath().get("dashboards.createdOn");
			for (int i = 0; i < list_dashboards.size() - 1; i++) {
				if (list_dashboards.get(i).compareTo(list_dashboards.get(i + 1)) < 0) {
					Assert.fail("The order is wrong!!");
				}
			}

			
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
		finally {
			if (!("").equals(dashboard_id)) {
				Response res = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				Assert.assertTrue(res.getStatusCode() == 204);
			}
			
		}

	}

	@Test
	public void dashboardOrderByDefault()
	{
		try {
			
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards?offset=0&limit=240&orderBy=default");
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertEquals(res1.jsonPath().get("dashboards.name[0]"), "Application Performance Monitoring");
			
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
	}

	@Test
	public void dashboardOrderByLastAccess()
	{
		String dashboard_id = "";
		try {
			
			String jsonString1 = "{ \"name\":\"Test_LastAccess\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			
			Assert.assertTrue(res1.getStatusCode() == 201);
			

			dashboard_id = res1.jsonPath().getString("id");

			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards?offset=0&limit=240&orderBy=access_Date");
			
			Assert.assertTrue(res2.getStatusCode() == 200);
			Response res5 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/" + dashboard_id);
			Assert.assertTrue(res5.getStatusCode() == 200);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
		finally {
			if (!("").equals(dashboard_id)) {
				Response res = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				
				Assert.assertTrue(res.getStatusCode() == 204);
			}
			
		}
	}

	@Test
	public void dashboardOrderByName()
	{
		String dashboard_id = "";
		try {
			
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards?offset=0&limit=240&orderBy=name");
			
			Assert.assertTrue(res1.getStatusCode() == 200);
			List<String> list_dashboards = res1.jsonPath().get("dashboards.name");
			for (int i = 0; i < list_dashboards.size() - 1; i++) {
				if (list_dashboards.get(i).compareToIgnoreCase(list_dashboards.get(i + 1)) > 0) {
					Assert.fail("The order is wrong!!");
				}
				else if (list_dashboards.get(i).compareToIgnoreCase(list_dashboards.get(i + 1)) == 0) {
					if (list_dashboards.get(i).compareTo(list_dashboards.get(i + 1)) > 0) {
						Assert.fail("The order is wrong!!");
					}
				}
				else {
					//correct
				}
			}
			
			String jsonString1 = "{ \"name\":\"Test_SortByName\"}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			
			Assert.assertTrue(res2.getStatusCode() == 201);
			

			dashboard_id = res2.jsonPath().getString("id");

			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards?offset=0&limit=240&orderBy=name");
			
			Assert.assertTrue(res3.getStatusCode() == 200);
			list_dashboards = res3.jsonPath().get("dashboards.name");
			for (int i = 0; i < list_dashboards.size() - 1; i++) {
				if (list_dashboards.get(i).compareToIgnoreCase(list_dashboards.get(i + 1)) > 0) {
					Assert.fail("The order is wrong!!");
				}
				if (list_dashboards.get(i).compareToIgnoreCase(list_dashboards.get(i + 1)) == 0) {
					if (list_dashboards.get(i).compareTo(list_dashboards.get(i + 1)) > 0) {
						Assert.fail("The order is wrong!!");
					}
				}
			}
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
		finally {
			if (!("").equals(dashboard_id)) {
				
				Response res = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				
				Assert.assertTrue(res.getStatusCode() == 204);
			}
		
		}

	}

	@Test
	public void dashboardQueryMulti()
	{
		int totalResults = 0;
		int count = 0;
		int limit = 0;
		String dashboard_id = "";
		try {
			//get all dashboards, default is to display 20
			
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/");
			Assert.assertTrue(res1.getStatusCode() == 200);
			List<String> list_dashboards = res1.jsonPath().get("dashboards.name");
			totalResults = res1.jsonPath().getInt("totalResults");
			limit = res1.jsonPath().getInt("limit");
			count = res1.jsonPath().getInt("count");
			Assert.assertEquals(count, list_dashboards.size());

			String jsonString1 = "{ \"name\":\"Test_Dashboard_QueryAll\"}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
		
			Assert.assertTrue(res2.getStatusCode() == 201);

			dashboard_id = res2.jsonPath().getString("id");

			
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
					.get("/dashboards?queryString=Test_Dashboard_QueryAll&offset=0&limit=5");
			Assert.assertTrue(res3.getStatusCode() == 200);
			list_dashboards = res3.jsonPath().get("dashboards.name");
			totalResults = res3.jsonPath().getInt("totalResults");
			limit = res3.jsonPath().getInt("limit");
			count = res3.jsonPath().getInt("count");
			Assert.assertEquals(count, list_dashboards.size());
			Assert.assertEquals(count, 1);
			Assert.assertEquals(totalResults, 1);
			Assert.assertEquals(limit, 5);
			Assert.assertEquals(res3.jsonPath().getInt("offset"), 0);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
		finally {
			if (!("").equals(dashboard_id)) {
				
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
			
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
			
		}
	}

	@Test
	public void dashboardQueryMultiInvalidPara()
	{
		try {
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards?limit=0");
			Assert.assertTrue(res1.getStatusCode() == 400);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Specified limit for the dashboard query is invalid: numeric number larger than zero is expected");

			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards?limit=-1");
			Assert.assertTrue(res2.getStatusCode() == 400);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"Specified limit for the dashboard query is invalid: numeric number larger than zero is expected");

			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards?offset=-1");
			Assert.assertTrue(res3.getStatusCode() == 400);
			Assert.assertEquals(res3.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"),
					"Specified offset for the dashboard query is invalid: numeric number larger than or equal to zero is expected");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}

	}

	@Test
	public void dashboardUpdate()
	{
		String dashboard_id = "";
		try {
			//create a new dashboard
			String jsonString = "{ \"name\":\"Test_Update_Dashboard\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");
		
			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");

			//update the simple dashboard
			
			String jsonString1 = "{\"name\": \"Test_Update_Dashboard_Edit\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("/dashboards/" + dashboard_id);
			
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertEquals(res1.jsonPath().get("name"), "Test_Update_Dashboard_Edit");
			Assert.assertNotEquals(res1.jsonPath().get("createOn"), res1.jsonPath().get("lastModifiedOn"));

			//update the dashboard with tile
			
			String jsonString2 = "{\"name\":\"Test_Update_Dashboard_Edit_NewTile\",\"tiles\":[{\"title\":\"Another tile for search\", \"isMaximized\": false,\"width\": 2,\"height\": 220,\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_NAME\": \"iFrame\",\"WIDGET_DESCRIPTION\": \"A widget which can show another page inside a iFrame\",\"WIDGET_GROUP_NAME\": \"Built-in Widgets\",\"WIDGET_ICON\": \"dependencies/built-in/iFrame/images/icon.png\",\"WIDGET_HISTOGRAM\": \"dependencies/built-in/iFrame/images/histogram.png\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\": \"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\": \"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\": \"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"X Analytics\",\"PROVIDER_VERSION\": \"1.0\",\"PROVIDER_ASSET_ROOT\": \"asset\"}]}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().put("/dashboards/" + dashboard_id);
			
			Assert.assertTrue(res2.getStatusCode() == 200);
			String tile_id = res2.jsonPath().get("tiles.tileId[0]").toString();
			Assert.assertEquals(res2.jsonPath().get("name"), "Test_Update_Dashboard_Edit_NewTile");
			Assert.assertNotEquals(res2.jsonPath().get("createOn"), res2.jsonPath().get("lastModifiedOn"));
			Assert.assertEquals(res2.jsonPath().get("tiles.title[0]"), "Another tile for search");

			//update the existed tile in dashboard
			
			String jsonString3 = "{\"name\":\"Test_Update_Dashboard_Edit_ExistTile\",\"tiles\":[{\"tileId\": "
					+ tile_id
					+ ",\"title\":\"Another tile for search_edit\", \"isMaximized\": false,\"width\": 2,\"height\": 220,\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_NAME\": \"iFrame\",\"WIDGET_DESCRIPTION\": \"A widget which can show another page inside a iFrame\",\"WIDGET_GROUP_NAME\": \"Built-in Widgets\",\"WIDGET_ICON\": \"dependencies/built-in/iFrame/images/icon.png\",\"WIDGET_HISTOGRAM\": \"dependencies/built-in/iFrame/images/histogram.png\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\": \"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\": \"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\": \"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"X Analytics\",\"PROVIDER_VERSION\": \"1.0\",\"PROVIDER_ASSET_ROOT\": \"asset\",\"tileParameters\": [{\"name\": \"WIDGET_CREATED_BY\",\"systemParameter\":false, \"type\": \"STRING\",\"value\":\"SYSMAN\"}]}]}";
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString3).when().put("/dashboards/" + dashboard_id);
			
			Assert.assertTrue(res3.getStatusCode() == 200);
			Assert.assertEquals(res3.jsonPath().get("name"), "Test_Update_Dashboard_Edit_ExistTile");
			Assert.assertNotEquals(res3.jsonPath().get("createOn"), res3.jsonPath().get("lastModifiedOn"));
			Assert.assertEquals(res3.jsonPath().get("tiles.title[0]"), "Another tile for search_edit");
			Assert.assertEquals(res3.jsonPath().get("tiles.tileParameters[0].name[0]"), "WIDGET_CREATED_BY");
			Assert.assertEquals(res3.jsonPath().get("tiles.tileParameters[0].type[0]"), "STRING");
			Assert.assertEquals(res3.jsonPath().get("tiles.tileParameters[0].systemParameter[0]"), false);

			//update the dashboard with new tile
		
			String jsonString4 = "{\"name\":\"Test_Update_Dashboard_Edit_NewTile\",\"tiles\":[{\"title\":\"A sample tile for search\", \"isMaximized\": false,\"width\": 2,\"height\": 220,\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_NAME\": \"iFrame\",\"WIDGET_DESCRIPTION\": \"A widget which can show another page inside a iFrame\",\"WIDGET_GROUP_NAME\": \"Built-in Widgets\",\"WIDGET_ICON\": \"dependencies/built-in/iFrame/images/icon.png\",\"WIDGET_HISTOGRAM\": \"dependencies/built-in/iFrame/images/histogram.png\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\": \"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\": \"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\": \"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"X Analytics\",\"PROVIDER_VERSION\": \"1.0\",\"PROVIDER_ASSET_ROOT\": \"asset\",\"tileParameters\": [{\"name\": \"WIDGET_CREATED_BY\",\"systemParameter\":false, \"type\":\"STRING\",\"value\":\"SYSMAN\"}]}]}";
			Response res4 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString4).when().put("/dashboards/" + dashboard_id);
			
			Assert.assertTrue(res4.getStatusCode() == 200);
			String tile_newid = res4.jsonPath().get("tiles.tileId[0]").toString();
			
			Assert.assertEquals(res4.jsonPath().get("name"), "Test_Update_Dashboard_Edit_NewTile");
			Assert.assertNotEquals(res4.jsonPath().get("createOn"), res4.jsonPath().get("lastModifiedOn"));
			Assert.assertEquals(res4.jsonPath().get("tiles.title[0]"), "A sample tile for search");
			Assert.assertNotEquals(tile_newid, tile_id);
			Assert.assertEquals(res4.jsonPath().get("tiles.tileParameters[0].name[0]"), "WIDGET_CREATED_BY");
			Assert.assertEquals(res4.jsonPath().get("tiles.tileParameters[0].type[0]"), "STRING");
			Assert.assertEquals(res4.jsonPath().get("tiles.tileParameters[0].systemParameter[0]"), false);
			
			//add a widget to dashboard
			Response res5 =  RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString4).when().put("/dashboards/" + dashboard_id + "/addWidget/2001");
			
			Assert.assertTrue(res5.getStatusCode() == 200);
			tile_newid = res5.jsonPath().get("tiles.tileId[1]").toString();
			
			Assert.assertEquals(res5.jsonPath().get("name"), "Test_Update_Dashboard_Edit_NewTile");
			Assert.assertNotEquals(res5.jsonPath().get("createOn"), res5.jsonPath().get("lastModifiedOn"));
			Assert.assertEquals(res5.jsonPath().get("tiles.title[1]"), "All Logs Trend");
			Assert.assertNotEquals(tile_newid, tile_id);
			Assert.assertEquals(res5.jsonPath().get("tiles.WIDGET_UNIQUE_ID[1]"), "2001");
			Assert.assertEquals(res5.jsonPath().get("tiles.WIDGET_NAME[1]"), "All Logs Trend");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
		finally {
			if (!("").equals(dashboard_id)) {				
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
			
				Assert.assertTrue(res5.getStatusCode() == 204);
			}		
		}
	}

	@Test
	public void dashboardUpdateEmptyPara()
	{
		String dashboard_id = "";
		try {
		
			String jsonString = "{ \"name\":\"Test_Edit_Empty_Para_Dashboard\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");
		
			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");

			//update the simple dashboard
			String jsonString1 = "{ \"name\":\"\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("/dashboards/" + dashboard_id);
			
			Assert.assertTrue(res1.getStatusCode() == 400);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Invalid dashboard name. Name can not be empty and must be less than 64 characters");
			

			String jsonString2 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"\"}]}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().put("/dashboards/" + dashboard_id);
		
			Assert.assertTrue(res2.getStatusCode() == 400);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"), "Title for tile is required");

			String jsonString3 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"\"}]}";
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString3).when().put("/dashboards/" + dashboard_id);
			
			Assert.assertTrue(res3.getStatusCode() == 400);
			Assert.assertEquals(res3.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"), "\"WIDGET_NAME\" is required for tile(s) of the dashboard");

			String jsonString4 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"\"}]}";
			Response res4 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString4).when().put("/dashboards/" + dashboard_id);
			
			Assert.assertTrue(res4.getStatusCode() == 400);
			Assert.assertEquals(res4.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res4.jsonPath().get("errorMessage"),
					"\"WIDGET_UNIQUE_ID\" is required for tile(s) of the dashboard");

			String jsonString5 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"\"}]}";
			Response res5 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString5).when().put("/dashboards/" + dashboard_id);
		
			Assert.assertTrue(res5.getStatusCode() == 400);
			Assert.assertEquals(res5.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res5.jsonPath().get("errorMessage"), "\"WIDGET_OWNER\" is required for tile(s) of the dashboard");

			String jsonString6 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"\"}]}";
			Response res6 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString6).when().put("/dashboards/" + dashboard_id);
		
			Assert.assertTrue(res6.getStatusCode() == 400);
			Assert.assertEquals(res6.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res6.jsonPath().get("errorMessage"),
					"\"WIDGET_CREATION_TIME\" is required for tile(s) of the dashboard");

			String jsonString7 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": \"\"}]}";
			Response res7 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString7).when().put("/dashboards/" + dashboard_id);
		
			Assert.assertTrue(res7.getStatusCode() == 400);
			Assert.assertEquals(res7.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res7.jsonPath().get("errorMessage"), "\"WIDGET_SOURCE\" is required for tile(s) of the dashboard");

			String jsonString8 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"\"}]}";
			Response res8 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString8).when().put("/dashboards/" + dashboard_id);
	
			Assert.assertTrue(res8.getStatusCode() == 400);
			Assert.assertEquals(res8.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res8.jsonPath().get("errorMessage"),
					"\"WIDGET_KOC_NAME\" is required for tile(s) of the dashboard");

			String jsonString9 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\":\"\"}]}";
			Response res9 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString9).when().put("/dashboards/" + dashboard_id);
	
			Assert.assertTrue(res9.getStatusCode() == 400);
			Assert.assertEquals(res9.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res9.jsonPath().get("errorMessage"),
					"\"WIDGET_VIEWMODEL\" is required for tile(s) of the dashboard");

			String jsonString10 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\":\"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\":\"\"}]}";
			Response res10 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString10).when().put("/dashboards/" + dashboard_id);
		
			Assert.assertTrue(res10.getStatusCode() == 400);
			Assert.assertEquals(res10.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res10.jsonPath().get("errorMessage"),
					"\"WIDGET_TEMPLATE\" is required for tile(s) of the dashboard");

			String jsonString11 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\":\"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\":\"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"\"}]}";
			Response res11 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString11).when().put("/dashboards/" + dashboard_id);
			
			Assert.assertTrue(res11.getStatusCode() == 400);
			Assert.assertEquals(res11.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res11.jsonPath().get("errorMessage"),
					"\"PROVIDER_NAME\" is required for tile(s) of the dashboard");

			String jsonString12 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\":\"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\":\"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"X Analytics\",\"PROVIDER_VERSION\": \"\"}]}";
			Response res12 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString12).when().put("/dashboards/" + dashboard_id);
			Assert.assertTrue(res12.getStatusCode() == 400);
			Assert.assertEquals(res12.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res12.jsonPath().get("errorMessage"),
					"\"PROVIDER_VERSION\" is required for tile(s) of the dashboard");

			String jsonString13 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\":\"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\":\"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"X Analytics\",\"PROVIDER_VERSION\": \"1.0\",\"PROVIDER_ASSET_ROOT\": \"\"}]}";
			Response res13 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString13).when().put("/dashboards/" + dashboard_id);
			Assert.assertTrue(res13.getStatusCode() == 400);
			Assert.assertEquals(res13.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res13.jsonPath().get("errorMessage"),
					"\"PROVIDER_ASSET_ROOT\" is required for tile(s) of the dashboard");

			String jsonString14 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\":\"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\":\"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"X Analytics\",\"PROVIDER_VERSION\": \"1.0\",\"PROVIDER_ASSET_ROOT\": \"assetRoot\",\"tileParameters\": [{\"name\": \"\"}]}]}";
			Response res14 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString14).when().put("/dashboards/" + dashboard_id);
			Assert.assertTrue(res14.getStatusCode() == 400);
			Assert.assertEquals(res14.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res14.jsonPath().get("errorMessage"), "\"Name\" is required for tile parameter(s) of the tile");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
		finally {
			if (!("").equals(dashboard_id)) {
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
			
		}
	}

	@Test
	public void dashboardUpdateInvalidId()
	{
		try {
			
			String jsonString = "{ \"name\":\"Custom_DashboardEdit\" }";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/dashboards/999999999");

			Assert.assertTrue(res.getStatusCode() == 404);

			Assert.assertEquals(res.jsonPath().getString("errorCode"), "20001");
			Assert.assertEquals(res.jsonPath().getString("errorMessage"), "Specified dashboard is not found");
			
			//verify api /dashboards/<id>/addWidget/<widgetid>
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/dashboards/999999999/addWidget/2001");

			Assert.assertTrue(res1.getStatusCode() == 404);

			Assert.assertEquals(res1.jsonPath().getString("errorCode"), "20001");
			Assert.assertEquals(res1.jsonPath().getString("errorMessage"), "Specified dashboard is not found");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
	}
	
	@Test
	public void dashboardUpdateInvalidWidgetId()
	{
		String dashboard_id = "";
		try {
			String jsonString = "{ \"name\":\"Test_Long_Dashboard\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");
			
			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");
			
			Response res1 =  RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/dashboards/" + dashboard_id + "/addWidget/99999");
			
			Assert.assertTrue(res1.getStatusCode() == 404);

			Assert.assertEquals(res1.jsonPath().getString("errorCode"), "20005");
			Assert.assertEquals(res1.jsonPath().getString("errorMessage"), "Specified Widget is not found!");
			
			Response res2 =  RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/dashboards/" + dashboard_id + "/addWidget/0");
			
			Assert.assertTrue(res2.getStatusCode() == 404);

			Assert.assertEquals(res2.jsonPath().getString("errorCode"), "20005");
			Assert.assertEquals(res2.jsonPath().getString("errorMessage"), "Specified Widget is not found!");
			
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
		finally{
			if (!("").equals(dashboard_id)) {				
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
			
				Assert.assertTrue(res5.getStatusCode() == 204);
			}			
		}		
	}

	@Test
	public void dashboardUpdateLongNameDesc()
	{
		String dashboard_id = "";
		try {
			String jsonString = "{ \"name\":\"Test_Long_Dashboard\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");
			
			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");

			//update the simple dashboard
			String jsonString1 = "{ \"name\":\"long name long name long name long name long name long name long name longg\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("/dashboards/" + dashboard_id);
			
			Assert.assertTrue(res1.getStatusCode() == 400);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Invalid dashboard name. Name can not be empty and must be less than 64 characters");
			String jsonString2 = "{ \"name\":\"long description\", \"description\":\"long description long description long description long description long description long description long description long description long description long description long description long description long description long description long description lo\"}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().put("/dashboards/" + dashboard_id);
			Assert.assertTrue(res2.getStatusCode() == 400);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"Invalid dashboard description. Description must be less than 256 characters");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
		finally {
			if (!("").equals(dashboard_id)) {
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
		
		}
	}

	@Test
	public void dashboardUpdateSystemDashboard()
	{
		String dashboard_id = "";
		try {
			String jsonString = "{ \"name\":\"Custom_Dashboard_Edit\" }";
		
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/dashboards/2");

			
			Assert.assertTrue(res.getStatusCode() == 403);
			Assert.assertEquals(res.jsonPath().getString("errorCode"), "30003");
			Assert.assertEquals(res.jsonPath().getString("errorMessage"), "Not support to update system dashboard");
			String jsonString1 = "{ \"name\":\"Test_Custom_Dashboard\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			Assert.assertTrue(res1.getStatusCode() == 201);
			dashboard_id = res1.jsonPath().getString("id");
			String jsonString2 = "{ \"name\":\"Custom_Dashboard_Edit\", \"systemDashboard\":true }";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().put("/dashboards/" + dashboard_id);
			Assert.assertTrue(res2.getStatusCode() == 403);
			Assert.assertEquals(res2.jsonPath().getString("errorCode"), "30003");
			Assert.assertEquals(res2.jsonPath().getString("errorMessage"), "Not support to update system dashboard");


		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
		finally {
			if (!("").equals(dashboard_id)) {
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				Assert.assertTrue(res5.getStatusCode() == 204);
			}

		}

	}

	@Test
	public void dashboardsetCreateSimple()
	{
		String dashboardset_id = "";
		try {
			
			String jsonString1 = "{\"name\": \"DashboardSet Test\",\"type\":\"SET\" }";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			Assert.assertTrue(res1.getStatusCode() == 201);
			
			dashboardset_id = res1.jsonPath().getString("id");

			
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/" + dashboardset_id);
			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertEquals(res2.jsonPath().getString("name"), "DashboardSet Test");
			Assert.assertEquals(res2.jsonPath().getString("id"), dashboardset_id);
			Assert.assertEquals(res2.jsonPath().getString("type"), "SET");
			
			String jsonString3 = "{ \"name\":\"DashboardSet Test\",\"type\":\"SET\"}";
			Response res4 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString3).when().post("/dashboards");
			
			Assert.assertTrue(res4.getStatusCode() == 400);
			Assert.assertEquals(res4.jsonPath().getString("errorCode"), "10001");
			Assert.assertEquals(res4.jsonPath().getString("errorMessage"), "Dashboard with the same name exists already");
			

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
		finally {
			if (!("").equals(dashboardset_id)) {
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboardset_id);
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
			
		}
	}

	@Test
	public void dashboardsetCreateOOB()
	{
		try {
			String jsonString1 = "{ \"name\":\"New_OOB_DashboardSet\", \"type\": \"SET\", \"systemDashboard\": true}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			Assert.assertTrue(res1.getStatusCode() == 403);
			Assert.assertEquals(res1.jsonPath().getString("errorCode"), "30002");
			Assert.assertEquals(res1.jsonPath().getString("errorMessage"), "Not support to create system dashboard");
		}
		catch (Exception e){
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
	}

	@Test
	public void dashboardsetUpdate()
	{
		String dashboard_id = "";
		String dashboardset_id = "";
		try {
			//create a dashboard
			String jsonString1 = "{ \"name\":\"Dashboard for add into DashboardSet\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			Assert.assertTrue(res1.getStatusCode() == 201);
			dashboard_id = res1.jsonPath().getString("id");

			//create a dashboard set
			String jsonString2 = "{\"name\": \"DashboardSet Test Update\",\"type\":\"SET\" }";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().post("/dashboards");
			Assert.assertTrue(res2.getStatusCode() == 201);
			dashboardset_id = res2.jsonPath().getString("id");

			//update the dashboard set
			String jsonString3 = "{\"name\": \"Test_Update_DashboardSet_Edit\",\"description\":\"test update dashboard set\",\"type\":\"SET\",\"subDashboards\":[]}";
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString3).when().put("/dashboards/" + dashboardset_id);
			Assert.assertTrue(res3.getStatusCode() == 200);
			Assert.assertEquals(res3.jsonPath().get("name"), "Test_Update_DashboardSet_Edit");
			Assert.assertEquals(res3.jsonPath().get("description"), "test update dashboard set");
			Assert.assertNotEquals(res3.jsonPath().get("createOn"), res3.jsonPath().get("lastModifiedOn"));
			String jsonString4 = "{\"name\": \"Test_Update_DashboardSet_Edit\",\"description\":\"test update dashboard set\",\"type\":\"SET\",\"subDashboards\":[],\"sharePublic\":true}";
			Response res4 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString4).when().put("/dashboards/" + dashboardset_id);
			Assert.assertTrue(res4.getStatusCode() == 200);
			Assert.assertEquals(res4.jsonPath().getBoolean("sharePublic"), true);
			Assert.assertNotEquals(res4.jsonPath().get("createOn"), res4.jsonPath().get("lastModifiedOn"));

			

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
		finally {
			if (!("").equals(dashboardset_id)) {
				Response res8 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboardset_id);
				Assert.assertTrue(res8.getStatusCode() == 204);
			}
			if (!("").equals(dashboard_id)) {
				Response res9 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				Assert.assertTrue(res9.getStatusCode() == 204);
			}
			
		}
	}

	@Test
	public void dashboardsetUpdateSystemDashboard()
	{
		String dashboard_id = "";
		try {
			String jsonString = "{ \"name\":\"Custom_DashboardSet_Edit\", \"type\": \"SET\" }";

			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/dashboards/28");

			Assert.assertTrue(res.getStatusCode() == 403);
			Assert.assertEquals(res.jsonPath().getString("errorCode"), "30003");
			Assert.assertEquals(res.jsonPath().getString("errorMessage"), "Not support to update system dashboard");

			String jsonString1 = "{ \"name\":\"Test_Custom_DashboardSet\", \"type\": \"SET\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			Assert.assertTrue(res1.getStatusCode() == 201);
			dashboard_id = res1.jsonPath().getString("id");
			String jsonString2 = "{ \"name\":\"Custom_Dashboard_Edit\",\"type\": \"SET\", \"systemDashboard\":true }";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().put("/dashboards/" + dashboard_id);
			Assert.assertTrue(res2.getStatusCode() == 403);
			Assert.assertEquals(res2.jsonPath().getString("errorCode"), "30003");
			Assert.assertEquals(res2.jsonPath().getString("errorMessage"), "Not support to update system dashboard");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
		finally {
			if (!("").equals(dashboard_id)) {
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
		}
	}

	@Test
	public void multiTenantHeaderCheck()
	{
		try {
			String jsonString1 = "{ \"name\":\"test_header\"}";
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.header("Authorization", authToken).body(jsonString1).when().post("/dashboards");
			Assert.assertTrue(res1.getStatusCode() == 500);
			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.header("Authorization", authToken).body(jsonString1).when().put("/dashboards/1");
			Assert.assertTrue(res2.getStatusCode() == 500);
			Response res3 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.header("Authorization", authToken).when().get("/dashboards/1");
			Assert.assertTrue(res3.getStatusCode() == 500);
			Response res4 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.header("Authorization", authToken).when().delete("/dashboards/1");
			Assert.assertTrue(res4.getStatusCode() == 500);
			Response res5 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.header("Authorization", authToken).when().get("/dashboards");
			Assert.assertTrue(res5.getStatusCode() == 500);
			Response res6 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "", "X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization",
							authToken).when().get("/dashboards");
			Assert.assertTrue(res6.getStatusCode() == 500);
			Response res7 = RestAssured.given().redirects().follow(false).contentType(ContentType.JSON).log().everything()
					.headers("OAM_REMOTE_USER", tenantid + "abc." + remoteuser, "Authorization", authToken).when()
					.get("/dashboards");
			Assert.assertEquals(res7.getStatusCode(), 401);
			
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
	}



	@Test
	public void multiTenantQueryUpdateDeleteInvalidTenant()
	{
		String dashboard_id = "";
		try {
		
			String jsonString1 = "{ \"name\":\"Test_Dashboard_multitenant\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
		
			Assert.assertTrue(res1.getStatusCode() == 201);
			dashboard_id = res1.jsonPath().getString("id");

			Response res2 = RestAssured.given().redirects().follow(false).contentType(ContentType.JSON).log().everything()
					.headers("OAM_REMOTE_USER", tenantid + ".userA", "Authorization", authToken).when()
					.get("/dashboards/" + dashboard_id);
			Assert.assertEquals(res2.getStatusCode(), 401);
			String jsonString2 = "{ \"name\":\"Test_Dashboard_multitenant\"}";
			Response res3 = RestAssured.given().redirects().follow(false).contentType(ContentType.JSON).log().everything()
					.headers("OAM_REMOTE_USER", tenantid + ".userA", "Authorization", authToken).body(jsonString2).when()
					.put("/dashboards/" + dashboard_id);
			Assert.assertEquals(res3.getStatusCode(), 401);
			Response res4 = RestAssured.given().redirects().follow(false).contentType(ContentType.JSON).log().everything()
					.headers("OAM_REMOTE_USER", tenantid + ".userA", "Authorization", authToken).when()
					.delete("/dashboards/" + dashboard_id);
			Assert.assertEquals(res4.getStatusCode(), 401);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
		finally {
			if (!("").equals(dashboard_id)) {
				Response res5 = RestAssured.given().contentType(ContentType.JSON).log().everything()
						.headers("OAM_REMOTE_USER", tenantid + "." + remoteuser, "Authorization", authToken).when()
						.delete("/dashboards/" + dashboard_id);
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
		}
	}

	@Test
	public void remoteUserHeaderCheck()
	{
		try {
			String jsonString1 = "{ \"name\":\"test_header\"}";
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).body(jsonString1).when()
					.post("/dashboards");
			Assert.assertTrue(res1.getStatusCode() == 403);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");
			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).body(jsonString1).when()
					.put("/dashboards/1");
			Assert.assertTrue(res2.getStatusCode() == 403);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");
			
			Response res3 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when().get("/dashboards/1");
			Assert.assertTrue(res3.getStatusCode() == 403);
			Assert.assertEquals(res3.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");
			Response res4 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when().delete("/dashboards/1");
			Assert.assertTrue(res4.getStatusCode() == 403);
			Assert.assertEquals(res4.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res4.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");
			
			Response res5 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when().get("/dashboards");
			Assert.assertTrue(res5.getStatusCode() == 403);
			Assert.assertEquals(res5.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res5.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");
			Response res6 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", " ", "Authorization", authToken).when()
					.get("/dashboards");
			Assert.assertTrue(res6.getStatusCode() == 500);
			
			Response res7 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", remoteuser, "Authorization", authToken)
					.when().get("/dashboards");
			Assert.assertTrue(res7.getStatusCode() == 500);
			
			Response res8 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + ".", "Authorization", authToken)
					.when().get("/dashboards");
			Assert.assertTrue(res8.getStatusCode() == 403);
			Assert.assertEquals(res8.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res8.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");
			
			Response res9 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", "." + remoteuser, "Authorization",
							authToken).when().get("/dashboards");
			Assert.assertTrue(res9.getStatusCode() == 500);
			
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context",e);
		}
	}

}
