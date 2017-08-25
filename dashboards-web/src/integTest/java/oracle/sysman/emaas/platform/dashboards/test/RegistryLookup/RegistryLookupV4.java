/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.test.RegistryLookup;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

/**
 * @author cawei
 */
public class RegistryLookupV4
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

	private static final Logger LOGGER = LogManager.getLogger(RegistryLookup.class);

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
	public void invalidUrl()
	{
		try {

			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("registry/lookup/endpoint");

			Assert.assertTrue(res.getStatusCode() == 404);
			Assert.assertEquals(res.jsonPath().get("errorCode"), 2002);
			Assert.assertEquals(res.jsonPath().get("errorMessage"), "End Point Not Found: serviceName=null, version=null");

			Response res1 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("registry/lookup/link");

			Assert.assertTrue(res1.getStatusCode() == 404);

			Assert.assertEquals(res1.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"), "Link Not Found: serviceName=null, version=null, rel=null");
			Response res2 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("registry/lookup/linkWithRelPrefix");

			Assert.assertTrue(res2.getStatusCode() == 404);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 2003);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"Link (with rel Prefix) Not Found: serviceName=null, version=null, rel prefix=null");

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void lookupEndPoint()
	{
		try {

			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
					.get("registry/lookup/endpoint?serviceName=Dashboard-UI&version=1.0");

			Assert.assertTrue(res.getStatusCode() == 200);
			Assert.assertEquals(res.jsonPath().get("serviceName"), "Dashboard-UI");
			Assert.assertEquals(res.jsonPath().get("version"), "1.0");
			Assert.assertNotNull(res.jsonPath().get("href"));
			Response res1 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("registry/lookup/endpoint?serviceName=Dashboard-UI&version=");

			Assert.assertTrue(res1.getStatusCode() == 200);

			Assert.assertEquals(res1.jsonPath().get("serviceName"), "Dashboard-UI");
			Assert.assertEquals(res1.jsonPath().get("version"), "");
			Assert.assertNotNull(res1.jsonPath().get("href"));

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void lookupEndPointInvalidPara()
	{
		try {

			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("registry/lookup/endpoint?serviceName=&version=1.0");

			Assert.assertTrue(res.getStatusCode() == 404);
			Assert.assertEquals(res.jsonPath().get("errorCode"), 2002);
			Assert.assertEquals(res.jsonPath().get("errorMessage"), "End Point Not Found: serviceName=, version=1.0");
			Response res2 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("registry/lookup/endpoint?serviceName=abc&version=1.0");

			Assert.assertTrue(res2.getStatusCode() == 404);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 2002);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"), "End Point Not Found: serviceName=abc, version=1.0");

			Response res3 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
					.get("registry/lookup/endpoint?serviceName=Dashboard-UI&version=999");

			Assert.assertTrue(res3.getStatusCode() == 404);
			Assert.assertEquals(res3.jsonPath().get("errorCode"), 2002);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"), "End Point Not Found: serviceName=Dashboard-UI, version=999");

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void lookupLink()
	{
		try {
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
					.get("registry/lookup/link?serviceName=Dashboard-UI&version=1.0&rel=sso.home");

			Assert.assertTrue(res.getStatusCode() == 200);
			Assert.assertEquals(res.jsonPath().get("rel"), "sso.home");
			Assert.assertNotNull(res.jsonPath().get("href"));
			// auth token shouldn't be sent to UI
			Assert.assertNull(res.jsonPath().get("authToken"));
		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void lookupLinkInvalidPara()
	{
		try {

			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("registry/lookup/link?serviceName=&version=1.0&rel=sso.home");

			Assert.assertTrue(res.getStatusCode() == 404);
			Assert.assertEquals(res.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res.jsonPath().get("errorMessage"), "Link Not Found: serviceName=, version=1.0, rel=sso.home");

			Response res1 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
					.get("registry/lookup/link?serviceName=Dashboard-UI&version=&rel=sso.home");

			Assert.assertTrue(res1.getStatusCode() == 404);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Link Not Found: serviceName=Dashboard-UI, version=, rel=sso.home");

			Response res2 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
					.get("registry/lookup/link?serviceName=Dashboard-UI&version=1.0&rel=");

			Assert.assertTrue(res2.getStatusCode() == 404);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"Link Not Found: serviceName=Dashboard-UI, version=1.0, rel=");

			Response res3 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
					.get("registry/lookup/link?serviceName=abc&version=1.0&rel=sso.home");

			Assert.assertTrue(res3.getStatusCode() == 404);
			Assert.assertEquals(res3.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"), "Link Not Found: serviceName=abc, version=1.0, rel=sso.home");

			Response res4 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
					.get("registry/lookup/link?serviceName=Dashboard-UI&version=999&rel=sso.home");

			Assert.assertTrue(res4.getStatusCode() == 404);
			Assert.assertEquals(res4.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res4.jsonPath().get("errorMessage"),
					"Link Not Found: serviceName=Dashboard-UI, version=999, rel=sso.home");

			Response res5 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
					.get("registry/lookup/link?serviceName=Dashboard-UI&version=1.0&rel=abc");

			Assert.assertTrue(res5.getStatusCode() == 404);
			Assert.assertEquals(res5.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res5.jsonPath().get("errorMessage"),
					"Link Not Found: serviceName=Dashboard-UI, version=1.0, rel=abc");

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void lookupLinkWithRelPrefix()
	{
		try {

			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix?serviceName=Dashboard-UI&version=1.0&rel=quickLink");

			Assert.assertTrue(res.getStatusCode() == 200);
			Assert.assertEquals(res.jsonPath().get("rel"), "quickLink/Dashboard Home");
			Assert.assertNotNull(res.jsonPath().get("href"));

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void lookupLinkWithRelPrefix_invalidPara()
	{
		try {

			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix?serviceName=&version=1.0&rel=quickLink");

			Assert.assertTrue(res.getStatusCode() == 404);
			Assert.assertEquals(res.jsonPath().get("errorCode"), 2003);
			Assert.assertEquals(res.jsonPath().get("errorMessage"),
					"Link (with rel Prefix) Not Found: serviceName=, version=1.0, rel prefix=quickLink");

			Response res1 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix?serviceName=Dashboard-UI&version=&rel=quickLink");

			Assert.assertTrue(res1.getStatusCode() == 404);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 2003);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Link (with rel Prefix) Not Found: serviceName=Dashboard-UI, version=, rel prefix=quickLink");

			Response res2 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix?serviceName=Dashboard-UI&version=1.0&rel=");

			Assert.assertTrue(res2.getStatusCode() == 404);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 2003);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"Link (with rel Prefix) Not Found: serviceName=Dashboard-UI, version=1.0, rel prefix=");

			Response res3 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix?serviceName=wrong&version=1.0&rel=quickLink");

			Assert.assertTrue(res3.getStatusCode() == 404);
			Assert.assertEquals(res3.jsonPath().get("errorCode"), 2003);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"),
					"Link (with rel Prefix) Not Found: serviceName=wrong, version=1.0, rel prefix=quickLink");

			Response res4 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix?serviceName=Dashboard-UI&version=999&rel=quickLink");

			Assert.assertTrue(res4.getStatusCode() == 404);
			Assert.assertEquals(res4.jsonPath().get("errorCode"), 2003);
			Assert.assertEquals(res4.jsonPath().get("errorMessage"),
					"Link (with rel Prefix) Not Found: serviceName=Dashboard-UI, version=999, rel prefix=quickLink");

			Response res5 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix?serviceName=Dashboard-UI&version=1.0&rel=abc");

			Assert.assertTrue(res5.getStatusCode() == 404);
			Assert.assertEquals(res5.jsonPath().get("errorCode"), 2003);
			Assert.assertEquals(res5.jsonPath().get("errorMessage"),
					"Link (with rel Prefix) Not Found: serviceName=Dashboard-UI, version=1.0, rel prefix=abc");

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void multiTenantHeaderCheck()
	{
		try {

			Response res = RestAssured.given().log().everything()
					.headers("X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).when()
					.get("registry/lookup/endpoint?serviceName=Dashboard-UI&version=1.0");

			Assert.assertTrue(res.getStatusCode() == 500);
			Response res1 = RestAssured.given().log().everything()
					.headers("X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).when()
					.get("registry/lookup/link?serviceName=Dashboard-UI&version=1.0&rel=sso.home");

			Assert.assertTrue(res1.getStatusCode() == 500);
			Response res2 = RestAssured.given().log().everything()
					.headers("X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix?serviceName=Dashboard-UI&version=1.0&rel=quickLink");

			Assert.assertTrue(res2.getStatusCode() == 500);

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
			Response res = RestAssured.given().log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when()
					.get("registry/lookup/endpoint?serviceName=Dashboard-UI&version=1.0");

			Assert.assertTrue(res.getStatusCode() == 403);
			Assert.assertEquals(res.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

			Response res1 = RestAssured.given().log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when()
					.get("registry/lookup/link?serviceName=Dashboard-UI&version=1.0&rel=sso.home");

			Assert.assertTrue(res1.getStatusCode() == 403);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

			Response res2 = RestAssured.given().log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix?serviceName=Dashboard-UI&version=1.0&rel=quickLink");

			Assert.assertTrue(res2.getStatusCode() == 403);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void serviceAPI()
	{
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "7004";
			RestAssured.basePath = "/registry/servicemanager/registry/v1/instances";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("?serviceName=Dashboard-API");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			System.out.println("The text" + res.jsonPath().get("items.serviceName"));
			System.out.println("The text" + res.jsonPath().get("items[0].links[0].rel"));
			Assert.assertEquals(res.jsonPath().get("items[0].links[4].rel"), "serviceapi/dashboards.service");
			Assert.assertEquals(res.jsonPath().get("items[0].links[9].rel"), "serviceapi/dashboards.subscribedapps");
			Assert.assertEquals(res.jsonPath().get("items[0].links[12].rel"), "serviceapi/dashboards.subscribedapps2");
			Assert.assertEquals(res.jsonPath().get("items[0].links[15].rel"), "serviceapi/dashboards.logging");
		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}
}
