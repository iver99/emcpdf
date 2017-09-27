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
	
	@Test
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
							"Authorization", authToken).when().delete("/tool/offboard/tenantid");
			
			Assert.assertTrue(res3.getStatusCode() == 200);					
		}
		catch (Exception e) {
			LOGGER.info("context",e);
			Assert.fail(e.getLocalizedMessage());
		}

	}
}
