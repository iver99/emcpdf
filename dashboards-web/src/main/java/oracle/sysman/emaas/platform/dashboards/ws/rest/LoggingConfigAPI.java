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
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.CommonResourceException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.util.DateUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.LogUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.core.util.StringUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.loggingconfig.LoggingItem;
import oracle.sysman.emaas.platform.dashboards.ws.rest.loggingconfig.LoggingItems;
import oracle.sysman.emaas.platform.dashboards.ws.rest.loggingconfig.UpdatedLoggerLevel;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.codehaus.jettison.json.JSONObject;

/**
 * @author guobaochen
 */
@Path("/v1/_logging/configs")
public class LoggingConfigAPI extends APIBase
{
	private static final Logger LOGGER = LogManager.getLogger(LoggingConfigAPI.class);

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeRootLoggerLevel(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, JSONObject inputJson)
	{
		return changeSpecificLoggerLevel(tenantIdParam, userTenant, "", inputJson);

	}

	@PUT
	@Path("{LOGGERName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeSpecificLoggerLevel(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @PathParam("LOGGERName") String LOGGERName,
			JSONObject inputJson)
	{
		UpdatedLoggerLevel input = null;
		try {
			initializeUserContext(tenantIdParam, userTenant);
			input = getJsonUtil().fromJson(inputJson.toString(), UpdatedLoggerLevel.class);
			if (input == null || StringUtil.isEmpty(input.getLevel())) {
				throw new CommonResourceException(MessageUtils.getDefaultBundleString(
						CommonResourceException.LOGGER_LEVEL_NOT_FOUND_TO_CONFIG, input == null ? null : input.getLevel(), LOGGERName));
			}
			Level level = Level.getLevel(input.getLevel().toUpperCase());
			if (level == null) {
				throw new CommonResourceException(MessageUtils.getDefaultBundleString(
						CommonResourceException.LOGGER_LEVEL_NOT_FOUND_TO_CONFIG, input.getLevel(), LOGGERName));
			}
			LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
			Configuration cfg = ctx.getConfiguration();
			Map<String, LoggerConfig> LOGGERs = cfg.getLoggers();
			LoggingItem li = null;
			for (LoggerConfig lc : LOGGERs.values()) {
				if (lc.getName().equalsIgnoreCase(LOGGERName)) {
					lc.setLevel(level);
					Long timestamp = DateUtil.getCurrentUTCTime().getTime();
					LogUtil.setLoggerUpdateTime(cfg, lc, timestamp);
					ctx.updateLoggers();
					li = new LoggingItem(lc, timestamp);
					LOGGER.info("Level for LOGGER \"{}\" has been updated to \"{}\" by user \"{}\" from REST interface",
							LOGGERName, input.getLevel(), UserContext.getCurrentUser());
					break;
				}
			}
			if (li == null) {
				throw new CommonResourceException(MessageUtils.getDefaultBundleString(
						CommonResourceException.LOGGER_NOT_FOUND_TO_CONFIG, LOGGERName));
			}
			return Response.ok(getJsonUtil().toJson(li)).build();
		}
		catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllLoggerLevels(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant)
	{
		try {
			initializeUserContext(tenantIdParam, userTenant);
			LOGGER.info("User \"{}\" is getting all LOGGER levels", UserContext.getCurrentUser());
			LoggerContext LOGGERContext = (LoggerContext) LogManager.getContext(false);
			Configuration cfg = LOGGERContext.getConfiguration();
			Map<String, LoggerConfig> LOGGERs = cfg.getLoggers();
			LoggingItems items = new LoggingItems();
			for (LoggerConfig tempLogger : LOGGERs.values()) {
				Long timestamp = LogUtil.getLoggerUpdateTime(cfg, tempLogger);
				items.addLoggerConfig(tempLogger, timestamp);
			}
			return Response.ok(getJsonUtil().toJson(items)).build();
		}
		catch (CommonSecurityException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
	}
}
