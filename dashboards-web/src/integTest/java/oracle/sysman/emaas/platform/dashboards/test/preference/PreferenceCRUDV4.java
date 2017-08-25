/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.test.preference;

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
public class PreferenceCRUDV4
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

	static String remoteuser;

	private static final Logger LOGGER = LogManager.getLogger(PreferenceCRUD.class);

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
	public void multiTenantHeaderCheck()
	{
		try {

			//List All
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).when().get("/preferences");

			Assert.assertTrue(res1.getStatusCode() == 500);

			//list with key
			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).when()
					.get("/preferences/Test");
			Assert.assertTrue(res2.getStatusCode() == 500);

			//delete all
			Response res3 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).when()
					.delete("/preferences");
			Assert.assertTrue(res3.getStatusCode() == 500);

			//delete with key
			Response res4 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).when()
					.delete("/preferences/Test");
			Assert.assertTrue(res4.getStatusCode() == 500);

			//put
			String jsonString = "{ \"value\":\"test preference\"}";
			Response res5 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).body(jsonString).when()
					.put("/preferences/TestPreference");
			Assert.assertTrue(res5.getStatusCode() == 500);

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void preferenceCreate()
	{
		try {
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences/TestPreference");
			if (res.getStatusCode() == 404) {
				Assert.assertEquals(res.jsonPath().get("errorCode"), 20001);
				Assert.assertEquals(res.jsonPath().get("errorMessage"), "Specified preference key is not found");
			}
			else {
				Assert.fail("The preference you want to create exists!");
			}

			String jsonString = "{ \"value\":\"test preference\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/preferences/TestPreference");
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("href"));
			Assert.assertEquals(res1.jsonPath().get("key"), "TestPreference");
			Assert.assertEquals(res1.jsonPath().get("value"), "test preference");

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences/TestPreference");
			if (res.getStatusCode() == 200) {
				Response res1 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/preferences/TestPreference");
				Assert.assertTrue(res1.getStatusCode() == 204);
			}

		}
	}

	@Test
	public void preferenceCreateLongKey()
	{
		String str_prefname = "defghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij";
		String str_prefname_new = "efghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij";
		try {

			String jsonString = "{ \"value\":\"test preference\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/preferences/" + str_prefname);
			Assert.assertTrue(res.getStatusCode() == 400);
			Assert.assertEquals(res.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(
					res.jsonPath().get("errorMessage"),
					"\"Key\" is required, which should only contain characters 'a' to 'Z', '_', '.', and '-'. The length of key should be less than 256 characters.");

			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/preferences/" + str_prefname_new);
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("href"));
			Assert.assertEquals(res1.jsonPath().get("key"), str_prefname_new);
			Assert.assertEquals(res1.jsonPath().get("value"), "test preference");

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences/TestPreference");
			if (res.getStatusCode() == 200) {
				Response res1 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/preferences/" + str_prefname_new);
				Assert.assertTrue(res1.getStatusCode() == 204);
			}

		}

	}

	@Test
	public void preferenceDeleteAll()
	{
		try {
			String jsonString = "{ \"value\":\"test preference1\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/preferences/TestPreference1");
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("href"));
			Assert.assertEquals(res1.jsonPath().get("key"), "TestPreference1");
			Assert.assertEquals(res1.jsonPath().get("value"), "test preference1");

			String jsonString1 = "{ \"value\":\"test preference2\"}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("/preferences/TestPreference2");
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res2.jsonPath().get("href"));
			Assert.assertEquals(res2.jsonPath().get("key"), "TestPreference2");
			Assert.assertEquals(res2.jsonPath().get("value"), "test preference2");

			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/preferences");
			Assert.assertTrue(res3.getStatusCode() == 204);

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			for (int i = 1; i < 3; i++) {
				Response res = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().get("/preferences/TestPreference" + i);
				if (res.getStatusCode() == 200) {
					Response res1 = RestAssured
							.given()
							.contentType(ContentType.JSON)
							.log()
							.everything()
							.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
									"Authorization", authToken).when().delete("/preferences/TestPreference" + i);
					Assert.assertTrue(res1.getStatusCode() == 204);
				}
			}

		}

	}

	@Test
	public void preferenceDeleteInvalidKey()
	{
		try {
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/preferences/abc#");
			Assert.assertTrue(res1.getStatusCode() == 404);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 20001);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"), "Specified preference key is not found");

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void preferenceListAll()
	{
		try {
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences/TestPreference");
			if (res.getStatusCode() == 404) {
				Assert.assertEquals(res.jsonPath().get("errorCode"), 20001);
				Assert.assertEquals(res.jsonPath().get("errorMessage"), "Specified preference key is not found");
			}
			else {
				Assert.fail("The preference you want to create exists!");
			}

			String jsonString = "{ \"value\":\"test preference1\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/preferences/TestPreference1");
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("href"));
			Assert.assertEquals(res1.jsonPath().get("key"), "TestPreference1");
			Assert.assertEquals(res1.jsonPath().get("value"), "test preference1");

			String jsonString1 = "{ \"value\":\"test preference2\"}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("/preferences/TestPreference2");

			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res2.jsonPath().get("href"));
			Assert.assertEquals(res2.jsonPath().get("key"), "TestPreference2");
			Assert.assertEquals(res2.jsonPath().get("value"), "test preference2");

			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences");
			List<String> lst_preferences = res3.jsonPath().get("key");
			Assert.assertNotNull(res3.jsonPath().getBoolean("href[0]"));
			Assert.assertEquals(res3.jsonPath().get("key[0]"), "TestPreference1");
			Assert.assertEquals(res3.jsonPath().get("value[0]"), "test preference1");
			Assert.assertNotNull(res3.jsonPath().getBoolean("href[1]"));
			Assert.assertEquals(res3.jsonPath().get("key[1]"), "TestPreference2");
			Assert.assertEquals(res3.jsonPath().get("value[1]"), "test preference2");

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			for (int i = 1; i < 3; i++) {
				Response res = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().get("/preferences/TestPreference" + i);
				if (res.getStatusCode() == 200) {
					Response res1 = RestAssured
							.given()
							.contentType(ContentType.JSON)
							.log()
							.everything()
							.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
									"Authorization", authToken).when().delete("/preferences/TestPreference" + i);
					Assert.assertTrue(res1.getStatusCode() == 204);
				}
			}

		}
	}

	@Test
	public void preferenceMultiTenantCRUD()
	{
		try {
			String jsonString = "{ \"value\":\"test preference multi tenant\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/preferences/TestPreference_multiTenant");

			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("href"));
			Assert.assertEquals(res1.jsonPath().get("key"), "TestPreference_multiTenant");
			Assert.assertEquals(res1.jsonPath().get("value"), "test preference multi tenant");

			Response res2_1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "errortenant", "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences/TestPreference_multiTenant");
			Assert.assertTrue(res2_1.getStatusCode() == 500);

			Response res3_1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "errortenant", "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences/TestPreference_multiTenant");
			Assert.assertTrue(res3_1.getStatusCode() == 500);

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences/TestPreference_multiTenant");
			if (res.getStatusCode() == 200) {
				Response res1 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/preferences/TestPreference_multiTenant");
				Assert.assertTrue(res1.getStatusCode() == 204);
			}

		}
	}

	@Test
	public void preferenceUpdate()
	{
		try {

			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences/TestPreference");
			if (res.getStatusCode() == 404) {
				Assert.assertEquals(res.jsonPath().get("errorCode"), 20001);
				Assert.assertEquals(res.jsonPath().get("errorMessage"), "Specified preference key is not found");
			}
			else {
				Assert.fail("The preference you want to create exists!");
			}

			String jsonString = "{ \"value\":\"test preference\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/preferences/TestPreference");
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("href"));
			Assert.assertEquals(res1.jsonPath().get("key"), "TestPreference");
			Assert.assertEquals(res1.jsonPath().get("value"), "test preference");

			String jsonString1 = "{ \"value\":\"test preference update\"}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("/preferences/TestPreference");

			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertNotNull(res2.jsonPath().get("href"));
			Assert.assertEquals(res2.jsonPath().get("key"), "TestPreference");
			Assert.assertEquals(res2.jsonPath().get("value"), "test preference update");

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences/TestPreference");
			if (res.getStatusCode() == 200) {
				Response res1 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/preferences/TestPreference");
				Assert.assertTrue(res1.getStatusCode() == 204);
			}

		}
	}

	@Test
	public void preferneceCreateInvalidKey()
	{
		String[] str_prefname = new String[] { "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "+", "=", "~", "`", "{", "}",
				"[", "]", "|", "\\", ";", ":", "\"", "'", ",", "<", ">", "test name" };
		try {

			for (String element : str_prefname) {
				String jsonString = "{ \"value\":\"test preference\"}";
				Response res = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).body(jsonString).when().put("/preferences/" + element);
				Assert.assertTrue(res.getStatusCode() == 400);
				Assert.assertEquals(res.jsonPath().get("errorCode"), 10000);
				Assert.assertEquals(
						res.jsonPath().get("errorMessage"),
						"\"Key\" is required, which should only contain characters 'a' to 'Z', '_', '.', and '-'. The length of key should be less than 256 characters.");

			}
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
			//List All
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when().get("/preferences");
			Assert.assertTrue(res1.getStatusCode() == 403);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

			//list with key
			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when().get("/preferences/Test");
			Assert.assertTrue(res2.getStatusCode() == 403);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

			//delete all
			Response res3 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when().delete("/preferences");
			Assert.assertTrue(res3.getStatusCode() == 403);
			Assert.assertEquals(res3.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

			//delete with key
			Response res4 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when()
					.delete("/preferences/Test");
			Assert.assertTrue(res4.getStatusCode() == 403);
			Assert.assertEquals(res4.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res4.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

			//put
			String jsonString = "{ \"value\":\"test preference\"}";
			Response res5 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).body(jsonString).when()
					.put("/preferences/TestPreference");
			Assert.assertTrue(res5.getStatusCode() == 403);
			Assert.assertEquals(res5.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res5.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}

	}
}
