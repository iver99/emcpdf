/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.test.TenantSubscriptions;

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
public class TenantSubscriptionsV4
{

	/**
	 * Calling CommonTest.java to Set up RESTAssured defaults & Reading the inputs from the testenv.properties file before
	 * executing test cases
	 */
	private static final Logger LOGGER = LogManager.getLogger(TenantSubscriptions.class);
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
		tenantid = "instance1-emcpdftenant";
		tenantid_2 = ct.getTenantid2();
		remoteuser = ct.getRemoteUser();
	}

	@Test
	public void getSubscribedapps()
	{
		try {
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps");
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("applications"));
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps?withEdition=false");
			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertNotNull(res2.jsonPath().get("applications"));
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps?withEdition=true");
			Assert.assertTrue(res3.getStatusCode() == 200);
			Assert.assertNotNull(res3.jsonPath().get("applications.application[0]"));
			Assert.assertNotNull(res3.jsonPath().get("applications.edition[0]"));

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	//@Test
	public void getSubscribedapps_license()
	{
		try {
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps2");
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("applications"));

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	//@Test
	//	public void getSubscribedapps_license_OMCStandard()
	//	{
	//		tenantid = "df_omc_standard";
	//		try {
	//			Response res1 = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
	//							"Authorization", authToken).when().get("/subscribedapps2");
	//			Assert.assertTrue(res1.getStatusCode() == 200);
	//			Assert.assertNotNull(res1.jsonPath().get("applications"));
	//
	//		}
	//		catch (Exception e) {
	//			LOGGER.info("context", e);
	//			Assert.fail(e.getLocalizedMessage());
	//		}
	//	}

	@Test
	public void getSubscribedappsWrongTenant()
	{
		try {
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "wrongtenant", "X-REMOTE-USER", "wrongtenant" + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps");
			Assert.assertTrue(res1.getStatusCode() == 404);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 20002);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"), "Specified tenant does not subscribe to any service");
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "wrongtenant", "X-REMOTE-USER", "wrongtenant" + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps?withEdition=false");
			Assert.assertTrue(res2.getStatusCode() == 404);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 20002);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"), "Specified tenant does not subscribe to any service");

			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "wrongtenant", "X-REMOTE-USER", "wrongtenant" + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps?withEdition=true");
			Assert.assertTrue(res3.getStatusCode() == 404);
			Assert.assertEquals(res3.jsonPath().get("errorCode"), 20002);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"), "Specified tenant does not subscribe to any service");

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}

	}
}
