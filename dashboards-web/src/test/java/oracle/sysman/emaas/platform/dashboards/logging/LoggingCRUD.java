/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.logging;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

/**
 * @author shangwan
 */
public class LoggingCRUD
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

	@BeforeClass
	public static void setUp()
	{
		CommonTest ct = new CommonTest();
		HOSTNAME = ct.getHOSTNAME();
		portno = ct.getPortno();
		serveruri = ct.getServeruri();
		authToken = ct.getAuthToken();
		tenantid = ct.getTenantid();
		remoteuser = ct.getRemoteUser();
	}

	@Test
	public void logging_create()
	{
		try {
			System.out.println("------------------------------------------");
			String jsonString1 = "{\"tenantId\":\"TenantOPC1.emaasadmin\",\"logs\":{\"logArray\":[{\"logLevel\":3,\"log\":\"2015-03-16T02:20:13.161Z: Dashboard: [The targets] is open from Dashboard Home\"}]}}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/logging/logs");
			System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("currentLogLevel"));
			System.out.println("											");
			
			String jsonString2 = "{\"tenantId\":\"TenantOPC1.emaasadmin\",\"logs\":{\"logArray\":[{\"logLevel\":1,\"log\":\"2015-03-16T02:20:13.161Z: Dashboard: [The targets] is open from Dashboard Home\"}]}}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().post("/logging/logs");
			System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertNotNull(res2.jsonPath().get("currentLogLevel"));
			System.out.println("											");
			
			String jsonString3 = "{\"tenantId\":\"TenantOPC1.emaasadmin\",\"logs\":{\"logArray\":[{\"logLevel\":2,\"log\":\"2015-03-16T02:20:13.161Z: Dashboard: [The targets] is open from Dashboard Home\"}]}}";
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString3).when().post("/logging/logs");
			System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 200);
			Assert.assertNotNull(res3.jsonPath().get("currentLogLevel"));
			System.out.println("											");
			
			String jsonString4 = "{\"tenantId\":\"TenantOPC1.emaasadmin\",\"logs\":{\"logArray\":[{\"logLevel\":0,\"log\":\"2015-03-16T02:20:13.161Z: Dashboard: [The targets] is open from Dashboard Home\"}]}}";
			Response res4 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString4).when().post("/logging/logs");
			System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res4.getStatusCode() == 200);
			Assert.assertNotNull(res4.jsonPath().get("currentLogLevel"));
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	//@Test
}
