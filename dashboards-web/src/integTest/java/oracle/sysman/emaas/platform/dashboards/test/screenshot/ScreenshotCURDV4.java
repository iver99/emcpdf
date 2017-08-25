/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.test.screenshot;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;
import oracle.sysman.qatool.uifwk.webdriver.logging.EMTestLogger;

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
public class ScreenshotCURDV4
{
	/**
	 * Calling CommonTest.java to Set up RESTAssured defaults & Reading the inputs from the testenv.properties file before
	 * executing test cases
	 */
	private static final Logger LOGGER = LogManager.getLogger(ScreenshorCRUD.class);
	static String HOSTNAME;
	static String portno;
	static String serveruri;
	static String authToken;
	static String tenantid;
	static String tenantid_2;
	static String remoteuser;
	private static String screenshotRelUrl;

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
		screenshotRelUrl = ScreenshotCURDV4.getScreenshotRelURLForDashboard("2");
	}

	private static String getScreenshotRelURLForDashboard(String dashboardId)
	{
		Response res = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.log()
				.everything()
				.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization",
						authToken).when().get("/dashboards/" + dashboardId);
		String screenshotUrl = res.jsonPath().getString("screenShotHref");
		EMTestLogger.getLogger("getScreenshotURLForDashboard").info(
				"Retrieved screenshot URL=\"" + screenshotUrl + "\" for dashboard id=" + dashboardId);
		return ScreenshotCURDV4.getScreenshotRelURLForScreenshotUrl(dashboardId, screenshotUrl);
	}

	private static String getScreenshotRelURLForScreenshotUrl(String dashboardId, String screenshotUrl)
	{
		int index = screenshotUrl.indexOf("/sso.static/dashboards.service");
		String relUrl = "/dashboards" + screenshotUrl.substring(index + "/sso.static/dashboards.service".length());
		EMTestLogger.getLogger("getScreenshotURLForDashboard").info(
				"Retrieved screenshot rel URL=\"" + screenshotUrl + "\" for dashboard id=" + dashboardId);
		return relUrl;
	}

	@Test
	public void multiTenantHeaderCheck()
	{
		try {
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.header("Authorization", authToken).when().get(screenshotRelUrl);
			Assert.assertTrue(res1.getStatusCode() == 500);

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void multiTenantScreenshotQueryInvalidTenant()
	{
		String dashboard_id = "";
		try {
			String jsonString = "{ \"name\":\"Test_Dashboard_ScreenShot_multitenant\", \"screenShot\": \"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABYwAAAJACAYAAA\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");

			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");
			String ssUrl = res.jsonPath().getString("screenShotHref");
			String ssRelUrl = ScreenshotCURDV4.getScreenshotRelURLForScreenshotUrl(dashboard_id, ssUrl);

			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "errortenant", "X-REMOTE-USER", "errortenant" + "." + remoteuser,
							"Authorization", authToken).when().get(ssRelUrl);
			Assert.assertTrue(res2.getStatusCode() == 403);
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
	public void remoteUserHeaderCheck()
	{
		try {
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when().get(screenshotRelUrl);
			Assert.assertTrue(res1.getStatusCode() == 403);

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void remoteUserScreenshotQuery()
	{
		String dashboard_id = "";
		try {

			String jsonString = "{ \"name\":\"Test_Dashboard_ScreenShot_multitenant\", \"screenShot\": \"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABYwAAAJACAYAAA\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");
			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");
			String ssUrl = res.jsonPath().getString("screenShotHref");

			String ssRelUrl = ScreenshotCURDV4.getScreenshotRelURLForScreenshotUrl(dashboard_id, ssUrl);
			System.out.println("											");

			System.out.println("Verify that the other user can't query if the dashboard has screen shot...");
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + ".userA", "Authorization",
							authToken).when().get(ssRelUrl);
			Assert.assertTrue(res2.getStatusCode() == 404);

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
	public void screenshotQuery()
	{
		String dashboard_id = "";
		try {
			String jsonString = "{ \"name\":\"Test_Dashboard_ScreenShot\", \"screenShot\": \"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABYwAAAJACAYAAA\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");

			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");
			String ssUrl = res.jsonPath().getString("screenShotHref");

			String ssRelUrl = ScreenshotCURDV4.getScreenshotRelURLForScreenshotUrl(dashboard_id, ssUrl);

			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get(ssRelUrl);
			Assert.assertTrue(res2.getStatusCode() == 200);

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
	public void screenshotQueryInvalidId()
	{
		try {

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void screenshotQueryNoScreenShot()
	{
		String dashboard_id = "";
		try {

			String jsonString = "{ \"name\":\"Test_Dashboard_ScreenShot\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");

			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");
			String ssUrl = res.jsonPath().getString("screenShotHref");

			String ssRelUrl = ScreenshotCURDV4.getScreenshotRelURLForScreenshotUrl(dashboard_id, ssUrl);

			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get(ssRelUrl);
			Assert.assertTrue(res2.getStatusCode() == 200);

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

}
