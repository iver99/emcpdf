package oracle.sysman.emaas.platform.dashboards.core.util;

/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//import org.apache.commons.lang.StringUtils;
import oracle.sysman.emaas.platform.emcpdf.util.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author guobaochen
 */
public class SchemaUtil
{

	//added constant

	private static class SchemaDeployment
	{
		private String deploymentId;
		private String deploymentUUID;
		private String containerName;
		private String softwareName;
		private String softwareVersion;
		private String deploymentTs;
		private String deploymentType;
		private String deploymentStatus;
		private String schemaId;
		private String schemaUser;
		private String entityDomainName;
		private String entityDomainKey;
		private String entityDomainValue;

		/**
		 * @return the containerName
		 */
		public String getContainerName()
		{
			return containerName;
		}

		/**
		 * @return the deploymentId
		 */
		public String getDeploymentId()
		{
			return deploymentId;
		}

		/**
		 * @return the deploymentStatus
		 */
		public String getDeploymentStatus()
		{
			return deploymentStatus;
		}

		/**
		 * @return the deploymentTs
		 */
		public String getDeploymentTs()
		{
			return deploymentTs;
		}

		/**
		 * @return the deploymentType
		 */
		public String getDeploymentType()
		{
			return deploymentType;
		}

		/**
		 * @return the deploymentUUID
		 */
		public String getDeploymentUUID()
		{
			return deploymentUUID;
		}

		/**
		 * @return the entityDomainKey
		 */
		public String getEntityDomainKey()
		{
			return entityDomainKey;
		}

		/**
		 * @return the entityDomainName
		 */
		public String getEntityDomainName()
		{
			return entityDomainName;
		}

		/**
		 * @return the entityDomainValue
		 */
		public String getEntityDomainValue()
		{
			return entityDomainValue;
		}

		/**
		 * @return the schemaId
		 */
		public String getSchemaId()
		{
			return schemaId;
		}

		/**
		 * @return the schemaUser
		 */
		public String getSchemaUser()
		{
			return schemaUser;
		}

		/**
		 * @return the softwareName
		 */
		public String getSoftwareName()
		{
			return softwareName;
		}

		/**
		 * @return the softwareVersion
		 */
		public String getSoftwareVersion()
		{
			return softwareVersion;
		}

		/**
		 * @param containerName
		 *            the containerName to set
		 */
		public void setContainerName(String containerName)
		{
			this.containerName = containerName;
		}

		/**
		 * @param deploymentId
		 *            the deploymentId to set
		 */
		public void setDeploymentId(String deploymentId)
		{
			this.deploymentId = deploymentId;
		}

		/**
		 * @param deploymentStatus
		 *            the deploymentStatus to set
		 */
		public void setDeploymentStatus(String deploymentStatus)
		{
			this.deploymentStatus = deploymentStatus;
		}

		/**
		 * @param deploymentTs
		 *            the deploymentTs to set
		 */
		public void setDeploymentTs(String deploymentTs)
		{
			this.deploymentTs = deploymentTs;
		}

		/**
		 * @param deploymentType
		 *            the deploymentType to set
		 */
		public void setDeploymentType(String deploymentType)
		{
			this.deploymentType = deploymentType;
		}

		/**
		 * @param deploymentUUID
		 *            the deploymentUUID to set
		 */
		public void setDeploymentUUID(String deploymentUUID)
		{
			this.deploymentUUID = deploymentUUID;
		}

		/**
		 * @param entityDomainKey
		 *            the entityDomainKey to set
		 */
		public void setEntityDomainKey(String entityDomainKey)
		{
			this.entityDomainKey = entityDomainKey;
		}

		/**
		 * @param entityDomainName
		 *            the entityDomainName to set
		 */
		public void setEntityDomainName(String entityDomainName)
		{
			this.entityDomainName = entityDomainName;
		}

		/**
		 * @param entityDomainValue
		 *            the entityDomainValue to set
		 */
		public void setEntityDomainValue(String entityDomainValue)
		{
			this.entityDomainValue = entityDomainValue;
		}

		/**
		 * @param schemaId
		 *            the schemaId to set
		 */
		public void setSchemaId(String schemaId)
		{
			this.schemaId = schemaId;
		}

		/**
		 * @param schemaUser
		 *            the schemaUser to set
		 */
		public void setSchemaUser(String schemaUser)
		{
			this.schemaUser = schemaUser;
		}

		/**
		 * @param softwareName
		 *            the softwareName to set
		 */
		public void setSoftwareName(String softwareName)
		{
			this.softwareName = softwareName;
		}

		/**
		 * @param softwareVersion
		 *            the softwareVersion to set
		 */
		public void setSoftwareVersion(String softwareVersion)
		{
			this.softwareVersion = softwareVersion;
		}
	}

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

	private static String ITEMS = "items";
	private static final String AUTHORIZATION = "Authorization";

	private static final String AUTH_STRING = "Basic d2VibG9naWM6d2VsY29tZTE=";

	private static final Logger LOGGER = LogManager.getLogger(SchemaUtil.class);

	public static List<String> getDeploymentUrl(String json)
	{
		if (json == null || "".equals(json)) {
			return Collections.emptyList();
		}

		java.util.HashSet<String> urlSet = new java.util.HashSet<String>();

		try {
			JsonUtil ju = JsonUtil.buildNormalMapper();

			List<SchemaDeploymentUrls> sdlist = ju.fromJsonToList(json, SchemaDeploymentUrls.class, ITEMS);
			if (sdlist == null | sdlist.isEmpty()) {
				return Collections.emptyList();
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

			LOGGER.error("an error occureed while getting schema name", e);
			return Collections.emptyList();
		}
		List<String> urls = new ArrayList<String>();
		urls.addAll(urlSet);
		return urls;
	}

	public String get(String url)
	{
		if (url == null || "".equals(url.trim())) {
			return null;
		}

		InputStream is = null;
		BufferedReader in = null;
		InputStreamReader inReader = null;
		StringBuffer response = new StringBuffer();
		try {
			URL schema_dep_url = new URL(url);
			HttpURLConnection con = (HttpURLConnection) schema_dep_url.openConnection();
			con.setRequestProperty(AUTHORIZATION, AUTH_STRING);
			//int responseCode = con.getResponseCode();
			is = con.getInputStream();
			inReader = new InputStreamReader(is, "UTF-8");
			in = new BufferedReader(inReader);
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
		}
		catch (IOException e) {
			LOGGER.error("an error occureed while getting details by url" + " ::" + url + "  " + e.toString());
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (in != null) {
					in.close();
				}
				if (inReader != null) {
					inReader.close();
				}
			}
			catch (IOException ioEx) {
				//ignore
				LOGGER.info("context",ioEx);
			}
		}
		return response.toString();
	}

	public String getSchemaUserBySoftwareName(String json, String softwareName)
	{
		if (json == null || "".equals(json) || softwareName == null || "".equals(softwareName)) {
			return null;
		}
		JsonUtil ju = JsonUtil.buildNormalMapper();
		try {
			List<SchemaDeployment> sdlist = ju.fromJsonToList(json, SchemaDeployment.class);
			if (sdlist == null | sdlist.isEmpty()) {
				return null;
			}
			for (SchemaDeployment sd : sdlist) {
				if (softwareName.equals(sd.getSoftwareName())) {
					return sd.getSchemaUser();
				}
			}
		}
		catch (IOException e) {
			LOGGER.error("an error occureed while getting schema user by software name", e);
		}
		return null;
	}
}
