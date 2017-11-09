/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DatabaseDependencyUnavailableException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.wls.management.AppLoggingManageMXBean;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.rest.loggingconfig.LogFeatureItem;
import oracle.sysman.emaas.platform.emcpdf.util.JsonUtil;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Path("/v1/logging")
public class LoggingAPI extends APIBase
{
	private @Context HttpServletRequest request;
	private static Logger mLogger = LogManager.getLogger(LoggingAPI.class.getName());
	private static final String FEATURE_LOG_NAME = "oracle.sysman.emaas.platform.dashboards.feature.log";
	private static Logger fLogger = LogManager.getLogger(FEATURE_LOG_NAME);
	/**
	 * Receive the logs and log them.
	 */
	@POST
	@Path("/logs")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject logMsg(JSONObject jsonReceived)
	{
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				mLogger.error("Error to call [POST] /v1/logging: database is down");
				throw new DatabaseDependencyUnavailableException();
			}
			String tenantId = jsonReceived.getString("tenantId");
			String remoteIpAddress = request.getRemoteAddr();
			String remoteAgent = request.getHeader("User-Agent");
			String preferredLang = request.getLocale() == null ? "" : request.getLocale().toString();
			String remoteInfo = "remote Ip Address=" + remoteIpAddress + ", remote forwarded Ip Address="
					+ request.getHeader("X-Forwarded-For") + ", remote Agent=" + remoteAgent + ", preferred language="
					+ preferredLang;

			JSONArray logArray = jsonReceived.getJSONObject("logs").getJSONArray("logArray");
			for (int i = 0; i < logArray.length(); i++) {
				JSONObject element = logArray.getJSONObject(i);
				String level = element.getString("logLevel");
				String log = element.getString("log");

				Level logLevel = Level.ERROR;
				if ("1".equalsIgnoreCase(level)) {
					logLevel = Level.ERROR;
				}
				else if ("2".equalsIgnoreCase(level)) {
					logLevel = Level.WARN;
				}
				else if ("3".equalsIgnoreCase(level)) {
					logLevel = Level.INFO;
				}
				else if ("4".equalsIgnoreCase(level)) {
					logLevel = Level.DEBUG;
				}
				else if ("0".equalsIgnoreCase(level)) {
					logLevel = Level.OFF;
				}
				else {
					mLogger.log(Level.ERROR, "Log received from client is corrupt.");
				}

				// TODO: More log metadata is needed, for example, tenant ID, host, etc.  Let's wait until discussions
				// in https://confluence.oraclecorp.com/confluence/display/EMS/Logging+Recommendations+for+improving++diagnosability
				// settle down.

				mLogger.log(logLevel, "tenantId::: = " + tenantId + " - " + log + System.getProperty("line.separator")
						+ remoteInfo);
			}
		}
		catch(DashboardException e){
			mLogger.error(e.getLocalizedMessage(), e);
			JSONObject errorJson=new JSONObject();
			try {
				errorJson.put("errorCode",e.getErrorCode());
				errorJson.put("message",e.getMessage());
			} catch (JSONException e1) {
				mLogger.log(Level.ERROR, "Exception thrown when returning error information: ", e1);
			}
			return errorJson;
		}
		catch (JSONException e1) {
			mLogger.log(Level.ERROR, "Exception thrown when receiving logs: ", e1);
		}

		// Return the current log level so clients can set, in case it changed.
		// TODO: This is currently not used by client.
		JSONObject currentLogLevel = new JSONObject();
		try {
			currentLogLevel.put("currentLogLevel", new AppLoggingManageMXBean().getLogLevels());
		}
		catch (JSONException e) {
			mLogger.log(Level.ERROR, "Exception thrown when creating a reply in response to receiving logs: ", e);
		}

		return currentLogLevel;
	}

	/**
	 * EMCPDF-3673, Log the Feature Usage of Menu Items
	 * @param jsonReceived
	 * @return
	 */
	@POST
	@Path("/feature/logs")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response logFeatureMsg(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer, JSONObject jsonReceived){
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [POST] /v1/logging/feature/logs");
		try {
			String tenantName = jsonReceived.getString("tenantName");
			String userName = jsonReceived.getString("userName");
			initializeUserContext(tenantIdParam, userTenant);
			List<LogFeatureItem> logList =JsonUtil.buildNormalMapper().fromJsonToList(jsonReceived.getString("logArray"), LogFeatureItem.class);
			if(logList == null || logList.isEmpty()){
				fLogger.warn("Log arrays are null or empty");
				return Response.status(Response.Status.OK).entity("{\"msg\": \"Log arrays are null or empty\"}").build();
			}
			for(LogFeatureItem item : logList){
				fLogger.info("[{}] [{}] click [{}] [{}]",tenantName, userName,item.getType(),item.getLogMsg());
			}
		} catch (JSONException | CommonSecurityException | IOException e) {
			fLogger.error(e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"msg\": \"Error occurred when save feature log\"}").build();
		}
		return Response.status(Response.Status.OK).entity("{\"msg\": \"Save feature log successfully\"}").build();
	}
}
