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

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emaas.platform.dashboards.core.PreferenceManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DatabaseDependencyUnavailableException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.PreferenceNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.model.Preference;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.DashboardAPIUtil;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

/**
 * @author wenjzhu
 */
@Path("/v1/preferences")
public class PreferenceAPI extends APIBase
{
	private final Logger LOGGER = LogManager.getLogger(PreferenceAPI.class);

	@DELETE
	public Response deleteAllPreferenceByKey(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [DELETE] /v1/preferences");
		PreferenceManager pm = PreferenceManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [DELETE] /v1/preferences: database is down");
				throw new DatabaseDependencyUnavailableException();
			}
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			pm.removeAllPreferences(tenantId);
			return Response.status(Status.NO_CONTENT).build();
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@DELETE
	@Path("{key}")
	//@Path("{key: [\\w\\-\\.]{1,256}}")
	public Response deletePreferenceByKey(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("key") String key)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [DELETE] /v1/preferences/{}", key);
		PreferenceManager pm = PreferenceManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [DELETE] /v1/preferences/{}: database is down", key);
				throw new DatabaseDependencyUnavailableException();
			}
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			pm.removePreference(key, tenantId);
			return Response.status(Status.NO_CONTENT).build();
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@GET
	@Path("{key}")
	//@Path("{key: [\\w\\-\\.]{1,256}}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryPreferenceByKey(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("key") String key)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/preferences/{}", key);
		PreferenceManager pm = PreferenceManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [GET] /v1/preferences/{}: database is down", key);
				throw new DatabaseDependencyUnavailableException();
			}
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			Preference input = pm.getPreferenceByKey(key, tenantId);
			return Response.ok(getJsonUtil().toJson(input)).build();
		}
		catch (PreferenceNotFoundException e){
			//in order to suppress error information in log files, do not log stack trace info
			LOGGER.warn("Specific preference is not found for id {}",key);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryPreferences(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/preferences");
		PreferenceManager pm = PreferenceManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [GET] /v1/preferences: database is down");
				throw new DatabaseDependencyUnavailableException();
			}
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			List<Preference> ps = pm.listPreferences(tenantId);
			if (ps != null) {
				for (Preference p : ps) {
					updatePreferenceHref(p, tenantIdParam);
				}
			}
			return Response.ok(getJsonUtil().toJson(ps)).build();
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@PUT
	@Path("{key}")
	//@Path("{key: [\\w\\-\\.]{1,256}}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePreference(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("key") String key, JSONObject inputJson)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [PUT] /v1/preferences/{}", key);
		Preference input = null;
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [PUT] /v1/preferences/{}: database is down", key);
				throw new DatabaseDependencyUnavailableException();
			}
			input = getJsonUtil().fromJson(inputJson.toString(), Preference.class);
			if (input != null && input.getValue() != null) {
				// the preference value should be html escaped.
				input.setValue(StringEscapeUtils.escapeHtml4(input.getValue()));
			}
		}
		catch(DashboardException e){
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}

		PreferenceManager pm = PreferenceManager.getInstance();
		try {
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			input.setKey(key);

			pm.savePreference(input, tenantId);
			updatePreferenceHref(input, tenantIdParam);
			return Response.ok(getJsonUtil().toJson(input)).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	private void updatePreferenceHref(Preference pref, String tenantName)
	{
		if (pref == null) {
			return;
		}
		String externalBase = DashboardAPIUtil.getExternalPreferenceAPIBase(tenantName);
		String url = externalBase + (externalBase.endsWith("/") ? "" : "/") + pref.getKey();
		pref.setHref(url);
		//return pref;
	}
}
