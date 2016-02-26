/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import oracle.sysman.emsaas.login.LoginUtils;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import org.testng.Assert;

/**
 * @author shangwan
 */
public class CommonUIUtils
{
	static String sTenantId = CommonUIUtils.getEmaasPropertyValue("TENANT_ID");
	static String sOhsUrl = CommonUIUtils.getEmaasPropertyValue("OHS_URL");
	static String sRegistryUrl = CommonUIUtils.getEmaasPropertyValue("OHS_REGISTRY_URL");
	static String sSsoUserName = CommonUIUtils.getEmaasPropertyValue("SSO_USERNAME");
	static String sSsoPassword = CommonUIUtils.getEmaasPropertyValue("SSO_PASSWORD");
	static String sAuthToken = CommonUIUtils.getEmaasPropertyValue("SAAS_AUTH_TOKEN");
	static String sAPIUrl = CommonUIUtils.getEmaasPropertyValue("DASHBOARD_API_ENDPOINT");

	static String sRolesUrl = CommonUIUtils.getEmaasPropertyValue("TARGETMODEL_SERVICE_SHARD_ENDPOINT");

	static String sCommonUiUrlSuffix = CommonUIUtils.getEmaasPropertyValue("COMMON_UI_URL_SUFFIX");

	static String sAppName = "";
	
	static Boolean isAPMAdmin = false;
	static Boolean isITAAdmin = false;
	static Boolean isLAAdmin = false;
	static Boolean isDSAdmin = false;

	public static void commonUITestLog(String sDesc)
	{
		String sStr = "*** Dashboards Common UI TestLog ***:  " + sDesc;
		System.out.println(sStr);
	}

	public static String getEmaasPropertyValue(String sProperty)
	{

		File emaasPropertiesFile = new File(System.getenv("T_WORK") + "/emaas.properties.log");

		Properties emaasProp = new Properties();

		String sUrl = "";

		String sPropertyValue = "";

		InputStream input = null;

		try {
			input = new FileInputStream(emaasPropertiesFile);

			emaasProp.load(input);

			CommonUIUtils.commonUITestLog("Get the " + sProperty + " property value.");

			if (sProperty.equals("TENANT_ID")) {
				CommonUIUtils.commonUITestLog("Get the TENANT_ID property value.");
				sPropertyValue = emaasProp.getProperty("TENANT_ID");
				if (sPropertyValue == null) {
					CommonUIUtils.commonUITestLog("The TENANT_ID property value is null ... set it to a different value.");
					sPropertyValue = "emaastesttenant1";
				}
			}
			else if (sProperty.equals("OHS_REGISTRY_URL")) {
				sPropertyValue = emaasProp.getProperty("OHS_REGISTRY_URL");
				if (sPropertyValue == null) {
					CommonUIUtils.commonUITestLog("The OHS_REGISTRY_URL property value is null ... set it to a different value.");
					sOhsUrl = CommonUIUtils.getEmaasPropertyValue("OHS_URL");
					if (sOhsUrl == null) {
						sPropertyValue = null;
					}
					else {
						CommonUIUtils.commonUITestLog("The OHS_URL property is '" + sOhsUrl + "'.");
						sPropertyValue = sOhsUrl + "/registry";
					}
				}
			}
			else if (sProperty.equals("SSO_USERNAME")) {
				sPropertyValue = emaasProp.getProperty("SSO_USERNAME");
				if (sPropertyValue == null) {
					CommonUIUtils
					.commonUITestLog("The SSO_USERNAME property value is null ... set it to a different value -- 'emcsadmin'.");
					sPropertyValue = "emcsadmin";
				}
			}
			else if (sProperty.equals("SSO_PASSWORD")) {
				sPropertyValue = emaasProp.getProperty("SSO_PASSWORD");
				if (sPropertyValue == null) {
					CommonUIUtils
					.commonUITestLog("The SSO_PASSWORD property value is null ... set it to a different value -- 'Welcome1!'.");
					sPropertyValue = "Welcome1!";
				}
			}
			else if (sProperty.equals("COMMON_UI_URL_SUFFIX")) {
				sPropertyValue = emaasProp.getProperty("COMMON_UI_URL_SUFFIX");
				if (sPropertyValue == null) {
					CommonUIUtils
					.commonUITestLog("The COMMON_UI_URL_SUFFIX property value is null ... set it to a different value -- '/emsaasui/uifwk/test.html'.");
					sPropertyValue = "/emsaasui/uifwk/test.html";
				}
			}
			else if (sProperty.equals("SAAS_AUTH_TOKEN")) {
				sPropertyValue = emaasProp.getProperty("SAAS_AUTH_TOKEN");
				if (sPropertyValue == null) {
					CommonUIUtils
							.commonUITestLog("The DASHBOARD_API_ENDPOINT property value is null ... set it to a different value -- 'welcome1'.");
					sPropertyValue = "Basic d2VibG9naWM6d2VsY29tZTE=";

				}
				else if (sProperty.equals("DASHBOARD_API_ENDPOINT")) {
					sPropertyValue = emaasProp.getProperty("DASHBOARD_API_ENDPOINT");
					if (sPropertyValue == null) {
						CommonUIUtils
								.commonUITestLog("The SAAS_AUTH_TOKEN property value is null ... set it to a different value .");
						sPropertyValue = sOhsUrl + "/emcpdf/api/v1/";
					}
				}
			}
			else if (sProperty.equals("TARGETMODEL_SERVICE_SHARD_ENDPOINT")) {
				sPropertyValue = emaasProp.getProperty("TARGETMODEL_SERVICE_SHARD_ENDPOINT");
				if (sPropertyValue == null) {
					CommonUIUtils.commonUITestLog("The TARGETMODEL_SERVICE_SHARD_ENDPOINT property value is null ... set it to a different value.");
					sRolesUrl = CommonUIUtils.getEmaasPropertyValue("EMCS_NODE2_HOSTNAME");
					if (sRolesUrl == null) {
						sPropertyValue = null;
					}
					else {
						CommonUIUtils.commonUITestLog("The TARGETMODEL_SERVICE_SHARD_ENDPOINT property is '" + sRolesUrl + "'.");
						sPropertyValue = "http://"+sRolesUrl + ":7004/targetmodel";
					}
				}
			}
			else {
				sPropertyValue = emaasProp.getProperty(sProperty);
			}

		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		finally {
			if (input != null) {
				try {
					input.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (sPropertyValue == null) {
			CommonUIUtils.commonUITestLog("WARNING:  The property value for " + sProperty + " is null.");
		}
		else {
			CommonUIUtils.commonUITestLog("The property value for " + sProperty + " is '" + sPropertyValue + "'.");
		}

		return sPropertyValue;

	}

	public static void getRoles(String sTenant, String sUser)
	{
		CommonUIUtils.commonUITestLog("The Roles URL is:" + sRolesUrl);
		String sTempRolsUrl = sRolesUrl.substring(0,sRolesUrl.length()-11);
		CommonUIUtils.commonUITestLog("The Roles URL is:" + sTempRolsUrl);
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.baseURI = sTempRolsUrl+"authorization/ws/api/v1/";
		CommonUIUtils.commonUITestLog("The base URL is:" + sTempRolsUrl+"authorization/ws/api/v1/");

		Response res1 = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.log()
				.everything()
				.headers("OAM_REMOTE_USER", sTenant + "." + sUser,
						"Authorization", sAuthToken).when().get("/roles/grants/getRoles?grantee="+sTenant+"."+sUser);
		CommonUIUtils.commonUITestLog("The statu code is:" + res1.getStatusCode()+", The response content is " + res1.jsonPath().get("roleNames"));
		String s_rolename = res1.jsonPath().getString("roleNames");
		//CommonUIUtils.commonUITestLog("The response content is:" + s_rolename);
		String[] ls_rolename = s_rolename.split(",");
		for (int i = 0; i < ls_rolename.length; i++) {
			CommonUIUtils.commonUITestLog(i + " : " + ls_rolename[i]);
			if (ls_rolename[i].contains("APM Administrator")) {
				isAPMAdmin = true;
			}
			else if (ls_rolename[i].contains("IT Analytics Administrator")) {
				isITAAdmin = true;
			}
			else if (ls_rolename[i].contains("Log Analytics Administrator")) {
				isLAAdmin = true;
			}
		}
		if(isAPMAdmin || isITAAdmin || isLAAdmin)
		{
			isDSAdmin = true;
		}
	}

	public static String getAppName(String sTenant, String sUser)
	{
		//String sAppName = "";
		
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.baseURI = sAPIUrl;

		Response res1 = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.log()
				.everything()
				.headers("X-USER-IDENTITY-DOMAIN-NAME", sTenant, "X-REMOTE-USER", sTenant + "." + sUser,
						"Authorization", sAuthToken).when().get("/subscribedapps");
		CommonUIUtils.commonUITestLog("The statu code is:" + res1.getStatusCode() + res1.jsonPath().get("applications"));
		String s_appname = res1.jsonPath().getString("applications");
		CommonUIUtils.commonUITestLog("The response content is:" + s_appname);
		String[] ls_appname = s_appname.split(",");
		for (int i = 0; i < ls_appname.length; i++) {
			CommonUIUtils.commonUITestLog(i + " : " + ls_appname[i]);
			if (ls_appname[i].contains("APM")) {
				if (sAppName.equals("")) {
					sAppName = "Application Performance Monitoring";
				}
				else {
					sAppName = sAppName + " | Application Performance Monitoring";
				}
			}
			else if (ls_appname[i].contains("LogAnalytics")) {
				if (sAppName.equals("")) {
					sAppName = "Log Analytics";
				}
				else {
					sAppName = sAppName + " | Log Analytics";
				}
			}
			else if (ls_appname[i].contains("ITAnalytics")) {
				if (sAppName.equals("")) {
					sAppName = "IT Analytics";
				}
				else {
					sAppName = sAppName + " | IT Analytics";
				}
			}
		}
		CommonUIUtils.commonUITestLog("The App Name is:" + sAppName);
		return sAppName;
		
	}
	

	public static Boolean loginCommonUI(WebDriver driver, String sTenant, String sUser, String sPassword)
	{

		String sCommonUiUrl = "";

		if (sOhsUrl == null) {
			driver.getLogger().info("sUrl is null ... return false from loginCommonUI().");
			return false;
		}
		else {
			sCommonUiUrl = sOhsUrl + sCommonUiUrlSuffix;
			driver.getLogger().info("sCommonUiUrl is " + sCommonUiUrl);
		}

		driver.getLogger().info("Supply credentials and doLogin()");
		LoginUtils.doLogin(driver, sUser, sPassword, sTenant, sCommonUiUrl);

		return true;

	}

	public static Boolean loginCommonUI(WebDriver driver, String parameters, String sTenant, String sUser, String sPassword)
	{

		String sCommonUiUrl = "";

		if (sOhsUrl == null) {
			driver.getLogger().info("sUrl is null ... return false from loginCommonUI().");
			return false;
		}
		else {
			sCommonUiUrl = sOhsUrl + sCommonUiUrlSuffix + parameters;
			driver.getLogger().info("sCommonUiUrl is " + sCommonUiUrl);
		}

		driver.getLogger().info("Supply credentials and doLogin()");
		LoginUtils.doLogin(driver, sUser, sPassword, sTenant, sCommonUiUrl);

		return true;

	}

	public static void logoutCommonUI(WebDriver driver)
	{
		if (driver != null) {
			LoginUtils.doLogout(driver);
			driver.shutdownBrowser(true);
		}
	}

	public static void verifyPageContent(WebDriver driver, String sAppName) throws Exception
	{
		//verify the product name,app name,content of page
		driver.getLogger().info("Verify the page content");
		Assert.assertTrue(driver.isElementPresent(UIControls.sOracleImage));
		Assert.assertEquals(driver.getAttribute(UIControls.sOracleImage + "@alt"), "Oracle");
		Assert.assertTrue(driver.isElementPresent(UIControls.sProductText));
		driver.getLogger().info("The Product is:  " + driver.getText(UIControls.sProductText));
		Assert.assertEquals(driver.getText(UIControls.sProductText), "Management Cloud");
		Assert.assertTrue(driver.isElementPresent(UIControls.sAppText));
		driver.getLogger().info("The App is:  " + sAppName);
		Assert.assertEquals(driver.getText(UIControls.sAppText), sAppName);
		Assert.assertTrue(driver.isElementPresent(UIControls.sPageText));
		driver.getLogger().info("The page content is:  " + driver.getText(UIControls.sPageText));
		Assert.assertEquals(driver.getText(UIControls.sPageText),
		"Sample page for OMC UI Framework components testing only");
		Assert.assertTrue(driver.isElementPresent(UIControls.sCompassIcon));
		Assert.assertTrue(driver.isElementPresent(UIControls.sAddWidgetIcon));		
	}

	public static void verifyMenu(WebDriver driver, boolean isAdmin) throws Exception
	{
		//verify the menus
		driver.getLogger().info("Verify the Links menu displayed");
		driver.getLogger().info("The Link menu is:  " + driver.getAttribute(UIControls.sLinksMenu + "@style"));
		Assert.assertNotEquals(driver.getAttribute(UIControls.sLinksMenu + "@style"), "display: none;");

		if(isAdmin){

			Assert.assertTrue(driver.isElementPresent(UIControls.sHome));
			Assert.assertTrue(driver.isElementPresent(UIControls.sHomeIcon));
			Assert.assertTrue(driver.isElementPresent(UIControls.sHomeLabel));
			Assert.assertTrue(driver.isElementPresent(UIControls.sHomeLink));

			Assert.assertTrue(driver.isElementPresent(UIControls.sCloudService));
			Assert.assertTrue(driver.isElementPresent(UIControls.sCloudServiceIcon));
			Assert.assertTrue(driver.isElementPresent(UIControls.sCloudServiceLabel));
			Assert.assertTrue(driver.isElementPresent(UIControls.sCloudServiceLink));

			Assert.assertTrue(driver.isElementPresent(UIControls.sAnalyzer));
			Assert.assertTrue(driver.isElementPresent(UIControls.sAnalyzerIcon));
			Assert.assertTrue(driver.isElementPresent(UIControls.sAnalyzerLabel));
			Assert.assertTrue(driver.isElementPresent(UIControls.sAnalyzerLink));

			Assert.assertTrue(driver.isElementPresent(UIControls.sAdmin));
			Assert.assertTrue(driver.isElementPresent(UIControls.sAdminIcon));
			Assert.assertTrue(driver.isElementPresent(UIControls.sAdminLabel));
			Assert.assertTrue(driver.isElementPresent(UIControls.sAdminLink));
		}
		else
		{
			Assert.assertTrue(driver.isElementPresent(UIControls.sHome));
			Assert.assertTrue(driver.isElementPresent(UIControls.sHomeIcon));
			Assert.assertTrue(driver.isElementPresent(UIControls.sHomeLabel));
			Assert.assertTrue(driver.isElementPresent(UIControls.sHomeLink));

			Assert.assertTrue(driver.isElementPresent(UIControls.sCloudService));
			Assert.assertTrue(driver.isElementPresent(UIControls.sCloudServiceIcon));
			Assert.assertTrue(driver.isElementPresent(UIControls.sCloudServiceLabel));
			Assert.assertTrue(driver.isElementPresent(UIControls.sCloudServiceLink));

			Assert.assertTrue(driver.isElementPresent(UIControls.sAnalyzer));
			Assert.assertTrue(driver.isElementPresent(UIControls.sAnalyzerIcon));
			Assert.assertTrue(driver.isElementPresent(UIControls.sAnalyzerLabel));
			Assert.assertTrue(driver.isElementPresent(UIControls.sAnalyzerLink));

			Assert.assertFalse(driver.isElementPresent(UIControls.sAdmin));
			Assert.assertFalse(driver.isElementPresent(UIControls.sAdminIcon));
			Assert.assertFalse(driver.isElementPresent(UIControls.sAdminLabel));
			Assert.assertFalse(driver.isElementPresent(UIControls.sAdminLink));
		}		
	}

	public static void addWidget(WebDriver driver) throws Exception
	{
		//click Add Widget icon
		driver.getLogger().info("Verify if Add Widgets icon displayed");
		Assert.assertTrue(driver.isDisplayed(UIControls.sAddWidgetIcon));
		driver.getLogger().info("The buton is:  " + driver.getText(UIControls.sAddWidgetIcon));
		Assert.assertEquals(driver.getText(UIControls.sAddWidgetIcon), "Add");

		driver.getLogger().info("Click the Add Widgets icon");
		driver.click(UIControls.sAddWidgetIcon);
		Thread.sleep(15000);

		driver.getLogger().info("Verify the Add Widgets window is opened");
		Assert.assertTrue(driver.isElementPresent(UIControls.sWidgetWindowTitle));
		driver.getLogger().info("The window title is:  " + driver.getText(UIControls.sWidgetWindowTitle));
		Assert.assertTrue(driver.isTextPresent("Add Widgets", UIControls.sWidgetWindowTitle));
		driver.takeScreenShot();
		driver.getLogger().info("Verify the Add Widgets button is disabled");
		driver.getLogger().info("The button is:  " + driver.getText(UIControls.sAddWidgetBtn));
		Assert.assertEquals(driver.getText(UIControls.sAddWidgetBtn), "Add");
		driver.getLogger().info("The button has been:  " + driver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
		Assert.assertNotNull(driver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
		driver.getLogger().info("Verify the select category drop-down list in Add Widgets button is displayed");
		Assert.assertTrue(driver.isElementPresent(UIControls.sCategorySelect));

		//Add a widget
		driver.getLogger().info("Select a widget and add it to the main page");
		driver.getLogger().info("Select a widget");
		driver.click(UIControls.sWidgetSelct);
		Thread.sleep(15000);
		driver.getLogger().info("Click Add button");
		driver.click(UIControls.sAddWidgetBtn);
		driver.takeScreenShot();
		Thread.sleep(5000);
		driver.getLogger().info("Close the Add Widget window");
		driver.click(UIControls.sCloseWidget);
		driver.takeScreenShot();
		Thread.sleep(15000);

		driver.getLogger().info("Verify the widget has been added to main page");
		Assert.assertTrue(driver.isElementPresent(UIControls.sWidget));
		driver.takeScreenShot();
	}
	
	public static void openWidget(WebDriver driver, boolean isEnabled) throws Exception
	{
		if(isEnabled){
			//click Open Widget icon
			driver.getLogger().info("Verify if Open Widgets icon displayed");
			Assert.assertTrue(driver.isDisplayed(UIControls.sAddWidgetIcon));
			driver.getLogger().info("The buton is:  " + driver.getText(UIControls.sAddWidgetIcon));
			Assert.assertEquals(driver.getText(UIControls.sAddWidgetIcon), "Open");

			driver.getLogger().info("Click the Open icon");
			driver.click(UIControls.sAddWidgetIcon);
			Thread.sleep(10000);

			driver.getLogger().info("Verify the Open Widgets window is opened");
			Assert.assertTrue(driver.isElementPresent(UIControls.sWidgetWindowTitle));
			driver.getLogger().info("The window title is:  " + driver.getText(UIControls.sWidgetWindowTitle));
			Assert.assertTrue(driver.isTextPresent("Open", UIControls.sWidgetWindowTitle));
			driver.takeScreenShot();
			driver.getLogger().info("Verify the Open button is disabled");
			driver.getLogger().info("The button is:  " + driver.getText(UIControls.sAddWidgetBtn));
			Assert.assertEquals(driver.getText(UIControls.sAddWidgetBtn), "Open");
			driver.getLogger().info("The button has been:  " + driver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			Assert.assertNotNull(driver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			driver.getLogger().info("Verify the select category drop-down list in Add Widgets button is displayed");
			try {
				driver.getLogger().info("the category display is: " + driver.isDisplayed(UIControls.sCategorySelect));
			}
			catch (RuntimeException re) {
				Assert.fail(re.getLocalizedMessage());
			}
			//Assert.assertFalse(driver.isElementPresent(UIControls.sCategorySelect));

			//Open a widget
			if (!driver.getAttribute(UIControls.sWidgetDiplay + "@childElementCount").equals("0"))
			{
				driver.getLogger().info("Select a widget and open it in the main page");
				driver.getLogger().info("Select a widget");
				driver.click(UIControls.sWidgetSelct);
				Thread.sleep(5000);
				driver.getLogger().info("Click Open button");
				driver.click(UIControls.sAddWidgetBtn);
				driver.takeScreenShot();
				Thread.sleep(5000);

				driver.getLogger().info("Verify the widget has been opened in main page");
				Assert.assertTrue(driver.isElementPresent(UIControls.sWidget));
				driver.takeScreenShot();
			}			
		}
		else
		{
			//verify the Open widget icon is disabled
			driver.getLogger().info("Verify the Open widget icon");
			Assert.assertTrue(driver.isDisplayed(UIControls.sAddWidgetIcon));
			driver.getLogger().info("The buton is:  " + driver.getText(UIControls.sAddWidgetIcon));
			Assert.assertEquals(driver.getText(UIControls.sAddWidgetIcon), "Open");
			driver.getLogger().info("Verify the Open widget icon is disabled");
			driver.getLogger().info("The icon has been:  " + driver.getAttribute(UIControls.sAddWidgetIcon + "@disabled"));
			Assert.assertNotNull(driver.getAttribute(UIControls.sAddWidgetIcon + "@disabled"));
		}		
	}
}
