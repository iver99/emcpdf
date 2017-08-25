/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.test.dashboard;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

/**
 * @author cawei
 */
public class DashboardDeletionV4
{
	static String HOSTNAME;
	static String authToken;
	static String tenantid;
	static String remoteuser;

	static String dashboardId = "";

	static String dashboardSetId = "";

	private static final Logger LOGGER = LogManager.getLogger(DashboardDeletion.class);

	//@Test
	public static void createDashboard()
	{
		// insert data to ems_dashboard, ems_user_options and ems_dashboard_tile tables;
		try {
			String jsonString1 = "{\"name\": \"test_dashboard_deletion\",\"tiles\":[{\"title\":\"a tile\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_NAME\": \"iFrame\",\"WIDGET_DESCRIPTION\": \"A widget which can show another page inside a iFrame\",\"WIDGET_ICON\": \"dependencies/built-in/iFrame/images/icon.png\",\"WIDGET_HISTOGRAM\": \"dependencies/built-in/iFrame/images/histogram.png\",\"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2016-09-11T19:20:30.45Z\", \"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\": \"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\": \"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\": \"dependencies/built-in/iFrame/iFrame.html\", \"PROVIDER_NAME\": \"X Analytics\", \"PROVIDER_VERSION\": \"1.0\",\"PROVIDER_ASSET_ROOT\": \"asset\"}]}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");

			Assert.assertTrue(res1.getStatusCode() == 201);

			dashboardId = res1.jsonPath().getString("id");

			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/" + dashboardId);

			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertEquals(res2.jsonPath().get("tiles.title[0]"), "a tile");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
			LOGGER.info("context", e);
		}

		// insert data to ems_dashboard_set
		String jsonString2 = "{\"name\": \"add a dashboardSet\",\"type\":\"SET\" }";
		Response res3 = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.log()
				.everything()
				.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization",
						authToken).body(jsonString2).when().post("/dashboards");

		Assert.assertTrue(res3.getStatusCode() == 201);
		dashboardSetId = res3.jsonPath().getString("id");

		// insert data to ems_preference
		String jsonString4 = "{\"value\":\"test\"}";
		Response res4 = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.log()
				.everything()
				.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization",
						authToken).body(jsonString4).when().put("/preferences/addPreference");
		Assert.assertTrue(res4.getStatusCode() == 200);

	}

	@BeforeClass
	public static void setUp()
	{
		CommonTest ct = new CommonTest();
		HOSTNAME = ct.getHOSTNAME(); //"den00ytq.us.oracle.com";//
		authToken = ct.getAuthToken();//"Basic RU0wMV9FTUNTX0dMT0JBTF9JTlRFUl9TRVJWSUNFX0FQUElEOkN6czBwd2FkM3ZrX3Vo=";//
		tenantid = "instance1-emcpdftenant"; //"emaastesttenant1";
		remoteuser = ct.getRemoteUser();// "emcsadmin";//
	}

	//@Test(dependsOnMethods = { "createDashboard"})
	public void testDeleteAllByTenant()
	{
		Response res = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.log()
				.everything()
				.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization",
						authToken).when().delete("/dashboards/all");
		Assert.assertTrue(res.getStatusCode() == 204);

		Response res1 = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.log()
				.everything()
				.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization",
						authToken).when().get("/dashboards/" + dashboardId);
		Assert.assertTrue(res1.getStatusCode() == 404);

		Response res2 = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.log()
				.everything()
				.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization",
						authToken).when().get("/dashboards/" + dashboardSetId);
		Assert.assertTrue(res2.getStatusCode() == 404);

		Response res3 = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.log()
				.everything()
				.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization",
						authToken).when().get("/preferences/addPreference");
		Assert.assertTrue(res3.getStatusCode() == 404);
	}

}
