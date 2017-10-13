package oracle.sysman.emaas.platform.dashboards.test.offboard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;
import oracle.sysman.emaas.platform.dashboards.test.favoritedashboards.FavoriteDashboardCRUD;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
public class offboardTenant 
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
	
	String dashboard_id = "";

	private static final Logger LOGGER = LogManager.getLogger(FavoriteDashboardCRUD.class);


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
	
	@Test(dependsOnMethods = { "createDashboard" })
	public void offBoardForTenant()
	{
		
		try {
			
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "CloudServices",
							"Authorization", authToken).when().delete("/tool/offboard/wrongTenant");
			
			Assert.assertTrue(res1.getStatusCode() == 404);
			Assert.assertEquals(res1.jsonPath().getString("errorMsg"), "Tenant Id [wrongTenant] does not exist.");
			
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "CloudServices",
							"Authorization", authToken).when().delete("/tool/offboard/");
			
			Assert.assertTrue(res2.getStatusCode() == 404);
			
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "CloudServices",
							"Authorization", authToken).when().delete("/tool/offboard/" + tenantid);
	
			Assert.assertTrue(res3.getStatusCode() == 200);					
		}
		catch (Exception e) {
			LOGGER.info("context",e);
			Assert.fail(e.getLocalizedMessage());
		}

	}
	
	@Test
	public void createDashboard()
	{

		/* create a dashboard with required field
		 *
		 */
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
	}
	
	@Test(dependsOnMethods = {"offBoardForTenant"})
	public void verifyDataCleaned()
	{
		Response res4 = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.log()
				.everything()
				.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
						"Authorization", authToken).when().get("/dashboards/" + dashboard_id);
		
		Assert.assertTrue(res4.getStatusCode() == 404);
		Assert.assertEquals(res4.jsonPath().getString("errorCode"), "20001");
		Assert.assertEquals(res4.jsonPath().getString("errorMessage"), "Specified dashboard is not found");
	}
	
}
