/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.test.UserOptions;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

/**
 * @author cawei
 */
public class UserOptionsCRUDV4
{
	/**
	 * Calling CommonTest.java to Set up RESTAssured defaults & Reading the inputs from the testenv.properties file before
	 * executing test cases
	 */
	private static String dashboard_id = "";
	static String HOSTNAME;
	static String portno;
	static String serveruri;
	static String authToken;
	static String tenantid;
	static String remoteuser;

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

		//create simple dashboard for testing options API
		String jsonString = "{ \"name\":\"Test_Dashboard_Options\"}";
		Response res = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.log()
				.everything()
				.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization",
						authToken).body(jsonString).when().post("/dashboards");

		Assert.assertTrue(res.getStatusCode() == 201);
		dashboard_id = res.jsonPath().getString("id");
	}

	@Test
	public void optionCreate_invalidDashboardID()
	{
		try {
			String jsonString1 = "{  \"autoRefreshInterval\": 0}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("dashboards/99999/options");

			Assert.assertTrue(res1.getStatusCode() == 404);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 20001);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"), "Specified dashboard is not found");
		}
		catch (Exception ex) {
			Assert.fail(ex.getLocalizedMessage());
		}
	}

	@Test
	public void optionQuery_invalidDashboardID()
	{
		try {
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("dashboards/99999/options");

			Assert.assertTrue(res1.getStatusCode() == 404);

			Assert.assertEquals(res1.jsonPath().get("errorCode"), 20001);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"), "Specified dashboard is not found");
		}
		catch (Exception ex) {
			Assert.fail(ex.getLocalizedMessage());
		}
	}

	@Test
	public void optionsCRUD()
	{
		try {
			//create the options
			String jsonString1 = "{  \"autoRefreshInterval\": 0}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("dashboards/" + dashboard_id + "/options");

			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertEquals(res1.jsonPath().getString("dashboardId"), dashboard_id);
			Assert.assertEquals(res1.jsonPath().getInt("autoRefreshInterval"), 0);

			//update the options
			String jsonString2 = "{  \"autoRefreshInterval\": 30000}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().put("dashboards/" + dashboard_id + "/options");

			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertEquals(res2.jsonPath().getString("dashboardId"), dashboard_id);
			Assert.assertEquals(res2.jsonPath().getInt("autoRefreshInterval"), 30000);

			//get the options
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("dashboards/" + dashboard_id + "/options");
			Assert.assertTrue(res3.getStatusCode() == 200);
			Assert.assertEquals(res3.jsonPath().getString("dashboardId"), dashboard_id);
			Assert.assertEquals(res3.jsonPath().getInt("autoRefreshInterval"), 30000);

		}
		catch (Exception ex) {
			Assert.fail(ex.getLocalizedMessage());
		}
	}

	@Test
	public void optionUpdate_invalidDashboardID()
	{
		try {
			String jsonString1 = "{  \"autoRefreshInterval\": 0}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("dashboards/99999/options");
			Assert.assertTrue(res1.getStatusCode() == 404);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 20001);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"), "Specified dashboard is not found");
		}
		catch (Exception ex) {
			Assert.fail(ex.getLocalizedMessage());
		}

	}

	@Test
	public void remotUser_headerCheck()
	{
		try {

			Response res = RestAssured.given().log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when()
					.get("/configurations/registration");
			Assert.assertTrue(res.getStatusCode() == 403);
			Assert.assertEquals(res.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@AfterClass
	public void removeDate()
	{
		if (!dashboard_id.equals("")) {
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
