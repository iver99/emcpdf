/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.test.Configuration;

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
public class GetConfigurationV4
{
	/**
	 * Calling CommonTest.java to Set up RESTAssured defaults & Reading the inputs from the testenv.properties file before
	 * executing test cases
	 */
	private static final Logger LOGGER = LogManager.getLogger(GetConfiguration.class);
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

	}

	@Test
	public void getServiceManagerProp()
	{
		try {
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/configurations/registration");

			Assert.assertTrue(res.getStatusCode() == 200);

			Assert.assertNotNull(res.jsonPath().get("cloudServices"));

			Assert.assertNotNull(res.jsonPath().get("visualAnalyzers"));

			Assert.assertNotNull(res.jsonPath().get("homeLinks"));

			Assert.assertNotNull(res.jsonPath().get("adminLinks"));

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void remotUserHeaderCheck()
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
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}

	}
}
