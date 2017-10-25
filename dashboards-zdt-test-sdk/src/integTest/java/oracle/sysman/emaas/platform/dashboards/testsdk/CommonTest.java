/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.testsdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.util.JsonUtil;
import oracle.sysman.qatool.uifwk.utils.Utils;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.LogConfig;

public class CommonTest
{

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class SchemaDeploymentUrls
	{
		private List<String> virtualEndpoints;
		private List<String> canonicalEndpoints;

		/**
		 * @return the canonicalEndpoints
		 */
		public List<String> getCanonicalEndpoints()
		{
			return canonicalEndpoints;
		}

		/**
		 * @return the virtualEndpoints
		 */
		public List<String> getVirtualEndpoints()
		{
			return virtualEndpoints;
		}

		/**
		 * @param canonicalEndpoints
		 *            the canonicalEndpoints to set
		 */
		public void setCanonicalEndpoints(List<String> canonicalEndpoints)
		{
			this.canonicalEndpoints = canonicalEndpoints;
		}

		/**
		 * @param virtualEndpoints
		 *            the virtualEndpoints to set
		 */
		public void setVirtualEndpoints(List<String> virtualEndpoints)
		{
			this.virtualEndpoints = virtualEndpoints;
		}

	}

	private String HOSTNAME;
	private String HOSTNAME1;
	private String portno;
	private String serveruri;
	private String authToken;
	private String tenantid;
	private String remoteuser;
	private String tenantid_2;
	private static final String SERVICE_NAME = "Dashboard-API";
	private static final String DOMAIN = "www.";
	private static final String DSB_DEPLOY_URL = "/instances?servicename=Dashboard-API";
	private static final String AUTHORIZATION = "Authorization";

	private static final String AUTH_STRING = "Basic d2VibG9naWM6d2VsY29tZTE=";

	public static List<String> getDeploymentUrl(String json)
	{
		if (json == null || "".equals(json)) {
			return null;
		}

		java.util.HashSet<String> urlSet = new java.util.HashSet<String>();

		try {
			JsonUtil ju = JsonUtil.buildNormalMapper();

			List<SchemaDeploymentUrls> sdlist = ju.fromJsonToList(json, SchemaDeploymentUrls.class, "items");
			if (sdlist == null | sdlist.isEmpty()) {
				return null;
			}
			for (SchemaDeploymentUrls sd : sdlist) {
				for (String temp : sd.getCanonicalEndpoints()) {
					if (temp.contains("https")) {
						continue;
					}
					urlSet.add(temp);
				}
				for (String temp : sd.getVirtualEndpoints()) {
					if (temp.contains("https")) {
						continue;
					}
					urlSet.add(temp);
				}

			}
		}
		catch (Exception e) {

			//	logger.error("an error occureed while getting schema name", e);
			return null;
		}
		List<String> urls = new ArrayList<String>();
		urls.addAll(urlSet);
		return urls;
	}

	/**
	 * Sets up RESTAssured defaults before executing test cases Enables logging Reading the inputs from the testenv.properties
	 * file
	 *
	 * @throws URISyntaxException
	 */

	/*public static void main(String ar[]) throws Exception
	{
		String  name = "http://slc08twq.us.oracle.com:7004/registry/servicemanager/registry/v1";
		name = name + DSB_DEPLOY_URL;
		String data = getData(name);
		List<String>  url=  getDeploymentUrl(data);
		System.out.println(url.get(0));

		System.out.println(getDomainName(url.get(0)));
		System.out.println(getPort(url.get(0)));
	}*/

	public static String getDomainName(String url) throws URISyntaxException
	{
		URI uri = new URI(url);
		String domain = uri.getHost();
		return domain.startsWith(DOMAIN) ? domain.substring(4) : domain;
	}

	public static int getPort(String url) throws URISyntaxException
	{
		URI uri = new URI(url);
		int port = uri.getPort();
		return port;
	}

	private static String getServiceManagerUrl()
	{
		return Utils.getProperty("SERVICE_MANAGER_URL");
	}

	public CommonTest()
	{

		try {
			String name = CommonTest.getServiceManagerUrl();
			name = name + DSB_DEPLOY_URL;
			String data = getData(name);

			List<String> url = CommonTest.getDeploymentUrl(data);
			HOSTNAME = CommonTest.getDomainName(url.get(0));
			//HOSTNAME = prop.getProperty("hostname");
			HOSTNAME1 = Utils.getProperty("EMCS_NODE3_HOSTNAME");
			//portno = prop.getProperty("port");
			portno = CommonTest.getPort(url.get(0)) + "";

			//	authToken = prop.getProperty("authToken");
			authToken = Utils.getProperty("SAAS_AUTH_TOKEN");
			//tenantid = prop.getProperty("tenantid");
			tenantid = Utils.getProperty("TENANT_ID");
			//tenantid_2 = prop.getProperty("tenantid_2");
			//remoteuser = prop.getProperty("RemoteUser");
			remoteuser = Utils.getProperty("SSO_USERNAME");
			serveruri = "http://" + HOSTNAME + ":" + portno;
			RestAssured.useRelaxedHTTPSValidation();
			RestAssured.baseURI = serveruri;
			RestAssured.basePath = "/emcpdf/api/v1";
			RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().enablePrettyPrinting(false));
		}
		catch (Exception e) {
			System.out.println("An error occurred while retriving deployment details:" + e.toString() + " " + e.getCause());
		}

	}

	public String getAuthToken()
	{
		return authToken;
	}

	public String getData(String url)
	{

		if (url == null || url.trim().equals("")) {
			return null;
		}

		BufferedReader in = null;
		InputStreamReader inReader = null;
		StringBuffer response = new StringBuffer();
		try {
			URL schema_dep_url = new URL(url);
			HttpURLConnection con = (HttpURLConnection) schema_dep_url.openConnection();
			con.setRequestProperty(AUTHORIZATION, AUTH_STRING);
			//int responseCode = con.getResponseCode();
			inReader = new InputStreamReader(con.getInputStream());
			in = new BufferedReader(inReader);
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
		}
		catch (IOException e) {

			System.out.println("an error occureed while getting details by url" + " ::" + url + "  " + e.toString());

		}
		finally {

			try {
				if (in != null) {
					in.close();
				}

				if (inReader != null) {
					inReader.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}

		}
		return response.toString();
	}

	public String getHOSTNAME()
	{
		return HOSTNAME;
	}
	public String getHOSTNAME1()
	{
		return HOSTNAME1;
	}

	public String getPortno()
	{
		return portno;
	}

	public String getRemoteUser()
	{
		return remoteuser;
	}

	public String getServeruri()
	{
		return serveruri;
	}

	public String getTenantid()
	{
		return tenantid;
	}

	public String getTenantid_2()
	{
		return tenantid_2;
	}
}
