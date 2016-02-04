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

import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emaas.platform.dashboards.core.DashboardConstants;
import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.DashboardsFilter;
import oracle.sysman.emaas.platform.dashboards.core.UserOptionsManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.DeleteSystemDashboardException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.PaginatedDashboards;
import oracle.sysman.emaas.platform.dashboards.core.model.UserOptions;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.core.util.StringUtil;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.DashboardAPIUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author wenjzhu
 * @author guobaochen introduce API to query single dashboard by id, and update specified dashboard
 */
@Path("/v1/dashboards")
public class DashboardAPI extends APIBase
{
	private static final Logger logger = LogManager.getLogger(DashboardAPI.class);

	public DashboardAPI()
	{
		super();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDashboard(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			JSONObject dashboard)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [POST] /v1/dashboards");
		try {
			logkeyHeaders("createDashboard()", userTenant, tenantIdParam);
			Dashboard d = getJsonUtil().fromJson(dashboard.toString(), Dashboard.class);
			DashboardManager manager = DashboardManager.getInstance();
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			d = manager.saveNewDashboard(d, tenantId);
			updateDashboardAllHref(d, tenantIdParam);
			return Response.status(Status.CREATED).entity(getJsonUtil().toJson(d)).build();
		}
		catch (IOException e) {
			logger.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			//e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@DELETE
	@Path("{id: [1-9][0-9]*}")
	public Response deleteDashboard(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") Long dashboardId)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [DELETE] /v1/dashboards/{}", dashboardId);
		DashboardManager manager = DashboardManager.getInstance();
		try {
			logkeyHeaders("deleteDashboard()", userTenant, tenantIdParam);
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			Dashboard dsb = manager.getDashboardById(dashboardId, tenantId);
			if (dsb != null && dsb.getIsSystem() != null && dsb.getIsSystem()) {
				throw new DeleteSystemDashboardException();
			}
			manager.deleteDashboard(dashboardId, tenantId);
			return Response.status(Status.NO_CONTENT).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			//e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@GET
	@Path("{id: [1-9][0-9]*}/screenshot")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDashboardBase64ScreenShot(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") Long dashboardId)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/dashboards/{}/screenshot",
				dashboardId);
		try {
			logkeyHeaders("getDashboardBase64ScreenShot()", userTenant, tenantIdParam);
			DashboardManager manager = DashboardManager.getInstance();
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			String ss = manager.getDashboardBase64ScreenShotById(dashboardId, tenantId);
			//String screenShotUrl = uriInfo.getBaseUri() + "v1/dashboards/" + dashboardId + "/screenshot";
			String externalBase = DashboardAPIUtil.getExternalDashboardAPIBase(tenantIdParam);
			String screenShotUrl = externalBase + (externalBase.endsWith("/") ? "" : "/") + dashboardId + "/screenshot";
			return Response.ok(getJsonUtil().toJson(new ScreenShotEntity(screenShotUrl, ss))).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			//e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}

	}

	@GET
	@Path("{id: [1-9][0-9]*}/options")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDashboardUserOptions(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
									 @HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer, @PathParam("id") Long dashboardId) {
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/dashboards/{}/options", dashboardId);
		UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
		try {
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			UserOptions options = userOptionsManager.getOptionsById(dashboardId, tenantId);
			return Response.ok(getJsonUtil().toJson(options)).build();
		} catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		} catch (BasicServiceMalfunctionException e) {
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		} finally {
			clearUserContext();
		}
	}

	@GET
	@Path("{id: [1-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryDashboardById(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") long dashboardId) throws DashboardException
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/dashboards/{}", dashboardId);
		DashboardManager dm = DashboardManager.getInstance();
		try {
			logkeyHeaders("queryDashboardById()", userTenant, tenantIdParam);
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			Dashboard dbd = dm.getDashboardById(dashboardId, tenantId);
			updateDashboardAllHref(dbd, tenantIdParam);
			return Response.ok(getJsonUtil().toJson(dbd)).build();
		}
		catch (DashboardException e) {
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryDashboards(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@QueryParam("queryString") String queryString, @DefaultValue("") @QueryParam("limit") Integer limit,
			@DefaultValue("0") @QueryParam("offset") Integer offset,
			@DefaultValue(DashboardConstants.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME) @QueryParam("orderBy") String orderBy,
			@QueryParam("types") String types, @QueryParam("appTypes") String appTypes, @QueryParam("owners") String owners,
			@QueryParam("onlyFavorites") Boolean onlyFavorites)
	{
		infoInteractionLogAPIIncomingCall(
				tenantIdParam,
				referer,
				"Service call to [GET] /v1/dashboards?queryString={}&limit={}&offset={}&orderBy={}&types={}&appTypes={}&owners={}",
				queryString, limit, offset, orderBy, types, appTypes, owners);
		logkeyHeaders("queryDashboards()", userTenant, tenantIdParam);
		String qs = null;
		try {
			qs = queryString == null ? null : java.net.URLDecoder.decode(queryString, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			logger.error(e.getLocalizedMessage(), e);
		}

		try {
			DashboardManager manager = DashboardManager.getInstance();
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			DashboardsFilter filter = new DashboardsFilter();
			filter.setIncludedAppsFromString(appTypes);
			filter.setIncludedOwnersFromString(owners);
			filter.setIncludedTypesFromString(types);
			filter.setIncludedFavorites(onlyFavorites);
			PaginatedDashboards pd = manager.listDashboards(qs, offset, limit, tenantId, true, orderBy, filter);
			if (pd != null && pd.getDashboards() != null) {
				for (Dashboard d : pd.getDashboards()) {
					updateDashboardAllHref(d, tenantIdParam);
				}
			}
			return Response.ok(getJsonUtil().toJson(pd)).build();
		}
		catch (DashboardException e) {
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@PUT
	@Path("{id: [1-9][0-9]*}/quickUpdate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response quickUpdateDashboard(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") long dashboardId, JSONObject inputJson)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [PUT] /v1/dashboards/{}/quickUpdate",
				dashboardId);
		logkeyHeaders("quickUpdateDashboard()", userTenant, tenantIdParam);
		String name = null;
		String description = null;
		Boolean share = null;
		try {
			if (inputJson.has("name")) {
				name = inputJson.getString("name");
			}
			if (inputJson.has("description")) {
				description = inputJson.getString("description");
			}
			if (inputJson.has("sharePublic")) {
				share = inputJson.getBoolean("sharePublic");
			}
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(new IOException("Can't parse input parameters.", e));
			return buildErrorResponse(error);
		}

		DashboardManager dm = DashboardManager.getInstance();
		try {
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			Dashboard input = dm.getDashboardById(dashboardId, tenantId);
			if (input.getIsSystem() != null && input.getIsSystem()) {
				throw new CommonSecurityException(
						MessageUtils.getDefaultBundleString(CommonSecurityException.NOT_SUPPORT_UPDATE_SYSTEM_DASHBOARD_ERROR));
			}
			if (name != null) {
				input.setName(name);
			}
			if (description != null) {
				input.setDescription(description);
			}
			if (share != null) {
				input.setSharePublic(share);
			}
			String screenShot = dm.getDashboardBase64ScreenShotById(dashboardId, tenantId);
			input.setScreenShot(screenShot); //set screen shot back otherwise it will be cleared
			Dashboard dbd = dm.updateDashboard(input, tenantId);
			updateDashboardAllHref(dbd, tenantIdParam);
			return Response.ok(getJsonUtil().toJson(dbd)).build();
		}
		catch (DashboardException e) {
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}
    @PUT
    @Path("{id: [1-9][0-9]*}/options")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserOptions(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
                                      @HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
                                      @PathParam("id") Long dashboardId, JSONObject inputJson) {
        infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [PUT] /v1/dashboards/{}/options/", dashboardId);
        UserOptions userOption = null;
        try {
            userOption = getJsonUtil().fromJson(inputJson.toString(), UserOptions.class);
            if (userOption != null && userOption.getAutoRefreshInterval() != null) {
                userOption.setAutoRefreshInterval(userOption.getAutoRefreshInterval());
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            ErrorEntity error = new ErrorEntity(e);
            return buildErrorResponse(error);
        }

        UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
        try {
            Long tenantId = getTenantId(tenantIdParam);
            initializeUserContext(tenantIdParam, userTenant);
            userOption.setDashboardId(dashboardId);//override id in comsumned json if exist;
            userOptionsManager.updateUserOptions(userOption, tenantId);
            return Response.ok(getJsonUtil().toJson(userOption)).build();
        } catch (DashboardException e) {
            return buildErrorResponse(new ErrorEntity(e));
        } catch (BasicServiceMalfunctionException e) {
            logger.error(e.getLocalizedMessage(), e);
            return buildErrorResponse(new ErrorEntity(e));
        } finally {
            clearUserContext();
        }
    }

	@PUT
	@Path("{id: [1-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDashboard(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") long dashboardId, JSONObject inputJson)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [PUT] /v1/dashboards/{}", dashboardId);
		logkeyHeaders("updateDashboard()", userTenant, tenantIdParam);
		Dashboard input = null;
		try {
			input = getJsonUtil().fromJson(inputJson.toString(), Dashboard.class);
		}
		catch (IOException e) {
			logger.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}

		DashboardManager dm = DashboardManager.getInstance();
		try {
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			input.setDashboardId(dashboardId);
			if (input.getIsSystem() != null && input.getIsSystem()) {
				throw new CommonSecurityException(
						MessageUtils.getDefaultBundleString(CommonSecurityException.NOT_SUPPORT_UPDATE_SYSTEM_DASHBOARD_ERROR));
			}
			Dashboard dbd = dm.updateDashboard(input, tenantId);
			updateDashboardAllHref(dbd, tenantIdParam);
			return Response.ok(getJsonUtil().toJson(dbd)).build();
		}
		catch (DashboardException e) {
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	private void logkeyHeaders(String api, String x_remote_user, String domain_name)
	{
		logger.info("Headers of " + api + ": X-REMOTE-USER=" + x_remote_user + ", X-USER-IDENTITY-DOMAIN-NAME=" + domain_name);
	}

	/*
	 * Updates the specified dashboard by generating all href fields
	 */
	private Dashboard updateDashboardAllHref(Dashboard dbd, String tenantName)
	{
		updateDashboardHref(dbd, tenantName);
		updateDashboardScreenshotHref(dbd, tenantName);
        updateDashboardOptionsHref(dbd, tenantName);
		return dbd;
	}

	private Dashboard updateDashboardScreenshotHref(Dashboard dbd, String tenantName)
	{
		if (dbd == null) {
			return null;
		}
		//		String screenShotUrl = uriInfo.getBaseUri() + "v1/dashboards/" + dbd.getDashboardId() + "/screenshot";
		String externalBase = DashboardAPIUtil.getExternalDashboardAPIBase(tenantName);
		if (StringUtil.isEmpty(externalBase)) {
			return null;
		}
		String screenShotUrl = externalBase + (externalBase.endsWith("/") ? "" : "/") + dbd.getDashboardId() + "/screenshot";
		dbd.setScreenShotHref(screenShotUrl);
		return dbd;
	}

    private Dashboard updateDashboardOptionsHref(Dashboard dbd, String tenantName)
    {
        if (dbd == null) {
            return null;
        }
        String externalBase = DashboardAPIUtil.getExternalDashboardAPIBase(tenantName);
        if (StringUtil.isEmpty(externalBase)) {
            return null;
        }
        String optionsUrl = externalBase + (externalBase.endsWith("/") ? "" : "/") + dbd.getDashboardId() + "/options";
        dbd.setOptionsHref(optionsUrl);
        return dbd;
    }
}
