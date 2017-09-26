/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.testsdk;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

/**
 * @author cawei
 */
public class DashboardOrderWriteCURD
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

	@BeforeClass
	public static void setUp()
	{
		CommonTest ct = new CommonTest();
		HOSTNAME = ct.getHOSTNAME();
		portno = ct.getPortno();
		serveruri = ct.getServeruri();
		authToken = ct.getAuthToken();
		tenantid = ct.getTenantid();
		tenantid_2 = ct.getTenantid_2();
		remoteuser = ct.getRemoteUser();
	}

	@Test
	public void dashboardCRUD()
	{
		String dashboard_id = "";
		try {
			System.out.println("------------------------------------------");
			//create the dashboard
			System.out.println("POST method is in-progress to create a new dashboard");

			String jsonString = "{ \"name\":\"Test_Dashboard\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");
			System.out.println(res.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 201);
			System.out.println("											");

			//update the simple dashboard
			dashboard_id = res.jsonPath().getString("id");
			System.out.println("PUT method is in-progress to update the newly created dashboard");

			System.out.println("											");
			System.out.println("Update the created dashboard...");

			String jsonString1 = "{\"name\": \"Test_Update_Dashboard_Edit\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("/dashboards/" + dashboard_id);
			System.out.println(res1.asString());
			System.out.println("==PUT operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertEquals(res1.jsonPath().get("name"), "Test_Update_Dashboard_Edit");
			Assert.assertNotEquals(res1.jsonPath().get("createOn"), res1.jsonPath().get("lastModifiedOn"));

			//update the dashboard with tile
			System.out.println("											");
			System.out.println("Update the dashboard with tile...");
			String jsonString2 = "{\"name\":\"Test_Update_Dashboard_Edit_NewTile\",\"tiles\":[{\"title\":\"Another tile for search\", \"isMaximized\": false,\"width\": 2,\"height\": 220,\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_NAME\": \"iFrame\",\"WIDGET_DESCRIPTION\": \"A widget which can show another page inside a iFrame\",\"WIDGET_GROUP_NAME\": \"Built-in Widgets\",\"WIDGET_ICON\": \"dependencies/built-in/iFrame/images/icon.png\",\"WIDGET_HISTOGRAM\": \"dependencies/built-in/iFrame/images/histogram.png\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\": \"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\": \"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\": \"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"X Analytics\",\"PROVIDER_VERSION\": \"1.0\",\"PROVIDER_ASSET_ROOT\": \"asset\"}]}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().put("/dashboards/" + dashboard_id);
			System.out.println(res2.asString());
			System.out.println("==PUT operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 200);
			String tile_id = res2.jsonPath().get("tiles.tileId[0]").toString();
			Assert.assertEquals(res2.jsonPath().get("name"), "Test_Update_Dashboard_Edit_NewTile");
			Assert.assertNotEquals(res2.jsonPath().get("createOn"), res2.jsonPath().get("lastModifiedOn"));
			Assert.assertEquals(res2.jsonPath().get("tiles.title[0]"), "Another tile for search");

			//update the existed tile in dashboard
			System.out.println("											");
			System.out.println("Update the exsited tile in dashboard");
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
			System.out.println(res3.asString());
			System.out.println("==PUT operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 200);
			Assert.assertEquals(res3.jsonPath().get("name"), "Test_Update_Dashboard_Edit_ExistTile");
			Assert.assertNotEquals(res3.jsonPath().get("createOn"), res3.jsonPath().get("lastModifiedOn"));
			Assert.assertEquals(res3.jsonPath().get("tiles.title[0]"), "Another tile for search_edit");
			Assert.assertEquals(res3.jsonPath().get("tiles.tileParameters[0].name[0]"), "WIDGET_CREATED_BY");
			Assert.assertEquals(res3.jsonPath().get("tiles.tileParameters[0].type[0]"), "STRING");
			Assert.assertEquals(res3.jsonPath().get("tiles.tileParameters[0].systemParameter[0]"), false);

			//update the dashboard with new tile
			System.out.println("											");
			System.out.println("Add a new tile in dashboard");
			String jsonString4 = "{\"name\":\"Test_Update_Dashboard_Edit_NewTile\",\"tiles\":[{\"title\":\"A sample tile for search\", \"isMaximized\": false,\"width\": 2,\"height\": 220,\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_NAME\": \"iFrame\",\"WIDGET_DESCRIPTION\": \"A widget which can show another page inside a iFrame\",\"WIDGET_GROUP_NAME\": \"Built-in Widgets\",\"WIDGET_ICON\": \"dependencies/built-in/iFrame/images/icon.png\",\"WIDGET_HISTOGRAM\": \"dependencies/built-in/iFrame/images/histogram.png\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\": \"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\": \"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\": \"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"X Analytics\",\"PROVIDER_VERSION\": \"1.0\",\"PROVIDER_ASSET_ROOT\": \"asset\",\"tileParameters\": [{\"name\": \"WIDGET_CREATED_BY\",\"systemParameter\":false, \"type\":\"STRING\",\"value\":\"SYSMAN\"}]}]}";
			Response res4 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString4).when().put("/dashboards/" + dashboard_id);
			System.out.println(res4.asString());
			System.out.println("==PUT operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res4.getStatusCode());
			Assert.assertTrue(res4.getStatusCode() == 200);
			String tile_newid = res4.jsonPath().get("tiles.tileId[0]").toString();
			System.out.println(tile_newid);
			Assert.assertEquals(res4.jsonPath().get("name"), "Test_Update_Dashboard_Edit_NewTile");
			Assert.assertNotEquals(res4.jsonPath().get("createOn"), res4.jsonPath().get("lastModifiedOn"));
			Assert.assertEquals(res4.jsonPath().get("tiles.title[0]"), "A sample tile for search");
			Assert.assertNotEquals(tile_newid, tile_id);
			Assert.assertEquals(res4.jsonPath().get("tiles.tileParameters[0].name[0]"), "WIDGET_CREATED_BY");
			Assert.assertEquals(res4.jsonPath().get("tiles.tileParameters[0].type[0]"), "STRING");
			Assert.assertEquals(res4.jsonPath().get("tiles.tileParameters[0].systemParameter[0]"), false);

			//query the dashboard
			System.out.println("Verify that the dashboard will be queried...");
			Response res5 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/" + dashboard_id);
			System.out.println(res5.asString());
			System.out.println("Status code is:  " + res5.getStatusCode());
			Assert.assertTrue(res4.getStatusCode() == 200);
			Assert.assertEquals(res4.jsonPath().getString("name"), "Test_Update_Dashboard_Edit_NewTile");
			Assert.assertEquals(res4.jsonPath().getString("type"), "NORMAL");
			Assert.assertEquals(res4.jsonPath().getString("id"), dashboard_id);
			Assert.assertEquals(res4.jsonPath().getBoolean("systemDashboard"), false);
			//Assert.assertEquals(res4.jsonPath().getString("tiles"), "[]");
			System.out.println("											");
		}
		catch (Exception ex) {
			Assert.fail(ex.getLocalizedMessage());
		}
		finally {
			System.out.println("cleaning up the dashboard that is created above using DELETE method");
			if (!dashboard_id.equals("")) {
				Response res = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				System.out.println(res.asString());
				System.out.println("Status code is: " + res.getStatusCode());
				Assert.assertTrue(res.getStatusCode() == 204);
			}
		}
	}

	@Test
	public void optionsCRUD()
	{
		String tmp_dsbid = "";
		try {
			System.out.println("------------------------------------------");
			//create simple dashboard for testing options API
			System.out.println("POST method is in-progress to create a new dashboard");

			String jsonString = "{ \"name\":\"Test_Dashboard_Options\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");
			System.out.println(res.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 201);
			System.out.println("											");
			tmp_dsbid = res.jsonPath().getString("id");

			//create the options
			String jsonString1 = "{  \"autoRefreshInterval\": 0}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("dashboards/" + tmp_dsbid + "/options");
			System.out.println(res1.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 200);
			System.out.println("											");
			Assert.assertEquals(res1.jsonPath().getString("dashboardId"), tmp_dsbid);
			Assert.assertEquals(res1.jsonPath().getInt("autoRefreshInterval"), 0);

			//update the options
			String jsonString2 = "{  \"autoRefreshInterval\": 30000}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().put("dashboards/" + tmp_dsbid + "/options");
			System.out.println(res2.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 200);
			System.out.println("											");
			Assert.assertEquals(res2.jsonPath().getString("dashboardId"), tmp_dsbid);
			Assert.assertEquals(res2.jsonPath().getInt("autoRefreshInterval"), 30000);

			//get the options
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("dashboards/" + tmp_dsbid + "/options");
			System.out.println(res3.asString());
			System.out.println("==GET operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 200);
			System.out.println("											");
			Assert.assertEquals(res3.jsonPath().getString("dashboardId"), tmp_dsbid);
			Assert.assertEquals(res3.jsonPath().getInt("autoRefreshInterval"), 30000);

		}
		catch (Exception ex) {
			Assert.fail(ex.getLocalizedMessage());
		}
		finally {
			if (!tmp_dsbid.equals("")) {
				Response res = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + tmp_dsbid);
				System.out.println(res.asString());
				System.out.println("Status code is: " + res.getStatusCode());
				Assert.assertTrue(res.getStatusCode() == 204);
			}
		}
	}

	@Test
	public void preferenceCRUD()
	{
		try {
			System.out.println("Create preferences");
			String jsonString = "{ \"value\":\"test preference\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/preferences/TestPreference");
			System.out.println(res1.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("href"));
			Assert.assertEquals(res1.jsonPath().get("key"), "TestPreference");
			Assert.assertEquals(res1.jsonPath().get("value"), "test preference");

			System.out.println("update the preference");
			String jsonString1 = "{ \"value\":\"test preference update\"}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("/preferences/TestPreference");
			System.out.println(res2.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertNotNull(res2.jsonPath().get("href"));
			Assert.assertEquals(res2.jsonPath().get("key"), "TestPreference");
			Assert.assertEquals(res2.jsonPath().get("value"), "test preference update");

			System.out.println("Delete the preference which created");
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/preferences/TestPreference");
			System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 204);
		}
		catch (Exception ex) {
			Assert.fail(ex.getLocalizedMessage());
		}

	}
}
