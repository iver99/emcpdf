/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.test.favoritedashboards;

import java.util.ArrayList;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

/**
 * @author cawei
 */
public class FavoriteDashboardCRUDV4
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
		tenantid = "instance1-emcpdftenant";
		remoteuser = ct.getRemoteUser();
	}

	@Test
	public void favoriteCreateInvalidId()
	{
		try {

			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().post("/dashboards/favorites/0");

			Assert.assertTrue(res1.getStatusCode() == 404);

			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().post("/dashboards/favorites/9999999999");

			Assert.assertTrue(res2.getStatusCode() == 404);
			Assert.assertEquals(res2.jsonPath().getString("errorCode"), "20001");
			Assert.assertEquals(res2.jsonPath().getString("errorMessage"), "Specified dashboard is not found");

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void favoriteCRUD()
	{
		String dashboard_id = "";
		try {

			String jsonString = "{ \"name\":\"Test_Favorite_Dashboard\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");

			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");

			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().post("/dashboards/favorites/" + dashboard_id);

			Assert.assertTrue(res1.getStatusCode() == 204);

			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/favorites/" + dashboard_id);

			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertEquals(res2.jsonPath().getBoolean("isFavorite"), true);

			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/dashboards/favorites/" + dashboard_id);

			Assert.assertTrue(res1.getStatusCode() == 204);

			Response res4 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/favorites/" + dashboard_id);

			Assert.assertTrue(res4.getStatusCode() == 200);
			Assert.assertEquals(res4.jsonPath().getBoolean("isFavorite"), false);

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			if (!"".equals(dashboard_id)) {

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
	public void favoriteDeleteInvalidId()
	{
		try {

			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/dashboards/favorites/0");

			Assert.assertTrue(res1.getStatusCode() == 404);

			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/dashboards/favorites/9999999999");

			Assert.assertTrue(res2.getStatusCode() == 404);
			Assert.assertEquals(res2.jsonPath().getString("errorCode"), "20001");
			Assert.assertEquals(res2.jsonPath().getString("errorMessage"), "Specified dashboard is not found");

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void favoriteQueryAll()
	{
		String dashboard_id = "";
		try {

			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/favorites");

			Assert.assertTrue(res.getStatusCode() == 200);
			List<Integer> origin_id = new ArrayList<Integer>();
			if (res.jsonPath().getString("id") != null && !"".equals(res.jsonPath().getString("id"))) {
				origin_id = res.jsonPath().get("id");
			}

			String jsonString = "{ \"name\":\"Test_Favorite_Dashboard\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");

			Assert.assertTrue(res1.getStatusCode() == 201);

			dashboard_id = res1.jsonPath().getString("id");

			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().post("/dashboards/favorites/" + dashboard_id);

			Assert.assertTrue(res2.getStatusCode() == 204);

			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/favorites");

			Assert.assertTrue(res3.getStatusCode() == 200);
			List<String> name = new ArrayList<String>();
			name = res3.jsonPath().get("name");
			List<Integer> id = new ArrayList<Integer>();
			id = res3.jsonPath().get("id");

			if (id.size() - origin_id.size() == 1) {
				for (int i = 0; i < id.size(); i++) {
					if ("Test_Favorite_Dashboard".equals(name.get(i)) && id.get(i).toString().equals(dashboard_id)) {
						Assert.assertTrue(true);
					}
				}
			}
			else {
				Assert.fail("Get a list of all favorite dashboards failed");

			}

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			if (!"".equals(dashboard_id)) {

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
	public void favoriteQueryInvalidId()
	{
		try {

			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).when()
					.get("/dashboards/favorites/0");
			Assert.assertTrue(res1.getStatusCode() == 404);
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).when()
					.get("/dashboards/favorites/9999999999");
			Assert.assertTrue(res2.getStatusCode() == 404);
			Assert.assertEquals(res2.jsonPath().getString("errorCode"), "20001");
			Assert.assertEquals(res2.jsonPath().getString("errorMessage"), "Specified dashboard is not found");

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void multiTenantFavoriteCRUDInvalidTenant()
	{
		String dashboard_id = "";
		try {

			String jsonString = "{ \"name\":\"Test_Favorite_Dashboard_multitenant\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");

			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");

			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "errortenant", "X-REMOTE-USER", "errortenant." + remoteuser,
							"Authorization", authToken).when().post("/dashboards/favorites/" + dashboard_id);

			Assert.assertTrue(res1.getStatusCode() == 403);
			Assert.assertEquals(res1.jsonPath().getString("errorCode"), "30000");
			Assert.assertEquals(res1.jsonPath().getString("errorMessage"), "Tenant Name is not recognized: errortenant");

			Response res1_1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().post("/dashboards/favorites/" + dashboard_id);

			Assert.assertTrue(res1_1.getStatusCode() == 204);

			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/favorites/" + dashboard_id);

			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertEquals(res2.jsonPath().getBoolean("isFavorite"), true);

			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "errortenant", "X-REMOTE-USER", "errortenant." + remoteuser,
							"Authorization", authToken).when().delete("/dashboards/favorites/" + dashboard_id);

			Assert.assertTrue(res3.getStatusCode() == 403);
			Assert.assertEquals(res3.jsonPath().getString("errorCode"), "30000");
			Assert.assertEquals(res3.jsonPath().getString("errorMessage"), "Tenant Name is not recognized: errortenant");

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			if (!"".equals(dashboard_id)) {

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
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.header("Authorization", authToken).when().post("/dashboards/favorites/1");
			Assert.assertTrue(res1.getStatusCode() == 403);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"\"X-USER-IDENTITY-DOMAIN-NAME\" is missing in request header");

			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.header("Authorization", authToken).when().delete("/dashboards/favorites/1");

			Assert.assertTrue(res2.getStatusCode() == 403);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"\"X-USER-IDENTITY-DOMAIN-NAME\" is missing in request header");

			Response res3 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.header("Authorization", authToken).when().get("/dashboards/favorites/1");

			Assert.assertTrue(res3.getStatusCode() == 403);
			Assert.assertEquals(res3.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"),
					"\"X-USER-IDENTITY-DOMAIN-NAME\" is missing in request header");

			Response res4 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.header("Authorization", authToken).when().get("/dashboards/favorites");

			Assert.assertTrue(res4.getStatusCode() == 403);
			Assert.assertEquals(res4.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res4.jsonPath().get("errorMessage"),
					"\"X-USER-IDENTITY-DOMAIN-NAME\" is missing in request header");

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void remoteUserHeaderCheck()
	{
		try {

			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when()
					.post("/dashboards/favorites/1");
			Assert.assertTrue(res1.getStatusCode() == 403);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when()
					.delete("/dashboards/favorites/1");
			Assert.assertTrue(res2.getStatusCode() == 403);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

			Response res3 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when()
					.get("/dashboards/favorites/1");
			Assert.assertTrue(res3.getStatusCode() == 403);
			Assert.assertEquals(res3.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

			Response res4 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when()
					.get("/dashboards/favorites");
			Assert.assertTrue(res4.getStatusCode() == 403);
			Assert.assertEquals(res4.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res4.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}
}
