/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.testsdk;

import java.sql.SQLException;



import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

/**
 * @author cawei
 */
public class DashboardSyncCompareTest
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
		HOSTNAME = ct.getHOSTNAME1();
		portno = ct.getPortno();
		serveruri = ct.getServeruri();
		authToken = ct.getAuthToken();
		tenantid = ct.getTenantid();
		remoteuser = ct.getRemoteUser();
	}

	@Test
	public void testFullCompare() throws SQLException
	{
		DatabaseUtil.Cloud1InsertData();
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8022";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "OAM_REMOTE_USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/compare");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			Assert.assertNotNull(res.jsonPath().get("comparisonDateTime"));
			Assert.assertNotNull(res.jsonPath().get("differentRowNum"));
			Assert.assertNotNull(res.jsonPath().get("totalRowNum"));
			Assert.assertNotNull(res.jsonPath().get("divergencePercentage"));

		}
		catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(dependsOnMethods = { "testFullCompare" })
	public void testFullCompareStatus()
	{
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8022";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "OAM_REMOTE_USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/compare/status");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			Assert.assertNotNull(res.jsonPath().get("divergencePercentage"));
			Assert.assertNotNull(res.jsonPath().get("lastComparisonDateTime"));
			Assert.assertNotNull(res.jsonPath().get("nextScheduledComparisonDateTime"));
		}
		catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(dependsOnMethods = { "testFullCompareStatus" })
	public void testFullSync()
	{
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8022";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "OAM_REMOTE_USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/sync");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			Assert.assertEquals(res.jsonPath().get("omc1"), "Sync is successful!");
			Assert.assertEquals(res.jsonPath().get("omc2"), "Sync is successful!");

		}
		catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(dependsOnMethods = { "testFullSync" })
	public void testFullSyncStatus()
	{
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8022";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "OAM_REMOTE_USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/sync/status");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			Assert.assertNotNull(res.jsonPath().get("lastSyncDateTime"));
			Assert.assertNotNull(res.jsonPath().get("syncType"));
			Assert.assertNotNull(res.jsonPath().get("nextScheduledSyncDateTime"));
			Assert.assertNotNull(res.jsonPath().get("divergencePercentage"));

		}
		catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(dependsOnMethods = { "testFullSyncStatus" })
	public void testIncrementalCompare()
	{
		try {
			DatabaseUtil.Cloud1DeleteData();
			DatabaseUtil.Cloud2DeleteData();
			DatabaseUtil.Cloud2InsertData();
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8022";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "OAM_REMOTE_USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/compare");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			Assert.assertNotNull(res.jsonPath().get("comparisonDateTime"));
			Assert.assertNotNull(res.jsonPath().get("differentRowNum"));
			Assert.assertNotNull(res.jsonPath().get("totalRowNum"));
			Assert.assertNotNull(res.jsonPath().get("divergencePercentage"));
			Assert.assertNotNull(res.jsonPath().get("divergenceSummary"));

		}
		catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(dependsOnMethods = { "testIncrementalCompare" })
	public void testIncrementalCompareStatus()
	{
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8022";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "OAM_REMOTE_USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/compare/status");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			Assert.assertNotNull(res.jsonPath().get("divergencePercentage"));
			Assert.assertNotNull(res.jsonPath().get("lastComparisonDateTime"));
			Assert.assertNotNull(res.jsonPath().get("nextScheduledComparisonDateTime"));
		}
		catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(dependsOnMethods = { "testIncrementalCompareStatus" })
	public void testIncrementalSync()
	{
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8022";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "OAM_REMOTE_USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/sync");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			Assert.assertEquals(res.jsonPath().get("omc1"), "Sync is successful!");
			Assert.assertEquals(res.jsonPath().get("omc2"), "Sync is successful!");

		}
		catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(dependsOnMethods = { "testIncrementalSync" })
	public void testIncrementalSyncStatus()
	{
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8022";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "OAM_REMOTE_USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/sync/status");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			Assert.assertNotNull(res.jsonPath().get("lastSyncDateTime"));
			Assert.assertNotNull(res.jsonPath().get("syncType"));
			Assert.assertNotNull(res.jsonPath().get("nextScheduledSyncDateTime"));
			Assert.assertNotNull(res.jsonPath().get("divergencePercentage"));
			DatabaseUtil.Cloud1DeleteData();
			DatabaseUtil.Cloud2DeleteData();

		}
		catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}
}
