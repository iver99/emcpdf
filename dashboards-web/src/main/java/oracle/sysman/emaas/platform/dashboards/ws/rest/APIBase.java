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

import java.util.Locale;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.tenant.TenantIdProcessor;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.EntityNamingDependencyUnavailableException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.LogUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.LogUtil.InteractionLogContext;
import oracle.sysman.emaas.platform.dashboards.core.util.LogUtil.InteractionLogDirection;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.core.util.StringUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.DashboardAPIUtil;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author wenjzhu
 */
public class APIBase
{
	private static final Logger LOGGER = LogManager.getLogger(APIBase.class);

	@Context
	protected UriInfo uriInfo;
	private final JsonUtil jsonUtil;

	public APIBase()
	{
		super();
		jsonUtil = JsonUtil.buildNonNullMapper();
		jsonUtil.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	}

	public Response buildErrorResponse(ErrorEntity error)
	{
		if (error == null) {
			LOGGER.error("Returning a empty response because specified error entity is empty");
			return null;
		}
		return Response.status(error.getStatusCode()).entity(getJsonUtil().toJson(error)).build();
	}

	public void clearUserContext()
	{
		UserContext.clear();
		TenantContext.clearCurrentUser();
	}

	public JsonUtil getJsonUtil()
	{
		return jsonUtil;
	}

	public Long getTenantId(String tenantId) throws CommonSecurityException, BasicServiceMalfunctionException,DashboardException
	{
		if (tenantId == null || "".equals(tenantId)) {
			throw new CommonSecurityException(
					MessageUtils.getDefaultBundleString(CommonSecurityException.X_USER_IDENTITY_DOMAIN_REQUIRED));
		}

		try {
			if (!DependencyStatus.getInstance().isEntityNamingUp())  {
				LOGGER.error("Error to get tenantId: EntityNaming service is down");
				throw new EntityNamingDependencyUnavailableException();
			}
			long internalTenantId = TenantIdProcessor.getInternalTenantIdFromOpcTenantId(tenantId);
			LOGGER.info("Get internal tenant id {} from opc tenant id {}", internalTenantId, tenantId);
			return internalTenantId;
		}
		catch(DashboardException e){
			LOGGER.error(e.getLocalizedMessage(), e);
			throw e;
		}
		catch (BasicServiceMalfunctionException e) {
			throw e;
		}
		catch (Exception e) {
			throw new CommonSecurityException(MessageUtils.getDefaultBundleString(
					CommonSecurityException.TENANT_NAME_NOT_RECOGNIZED, StringEscapeUtils.escapeHtml4(tenantId)), e);
		}

	}
	
	public void initializeUserContext(String opcTenantId, String userTenant) throws CommonSecurityException {
	    initializeUserContext(opcTenantId, userTenant, null);
	}

	public void initializeUserContext(String opcTenantId, String userTenant, Locale userLocale) throws CommonSecurityException
	{
		if (opcTenantId == null || "".equals(opcTenantId)) {
			throw new CommonSecurityException(
					MessageUtils.getDefaultBundleString(CommonSecurityException.X_USER_IDENTITY_DOMAIN_REQUIRED));
		}
		if (userTenant == null || "".equals(userTenant)) {
			throw new CommonSecurityException(
					MessageUtils.getDefaultBundleString(CommonSecurityException.VALID_X_REMOTE_USER_REQUIRED));
		}
		int idx = userTenant.indexOf('.');
		if (idx <= 0) {
			throw new CommonSecurityException(
					MessageUtils.getDefaultBundleString(CommonSecurityException.VALID_X_REMOTE_USER_REQUIRED));
		}
		String userName = userTenant.substring(idx + 1, userTenant.length());
		if (userName == null || "".equals(userName)) {
			throw new CommonSecurityException(
					MessageUtils.getDefaultBundleString(CommonSecurityException.VALID_X_REMOTE_USER_REQUIRED));
		}
		UserContext.setUserTenant(userTenant);
		String tenantName = userTenant.substring(0, idx);
		TenantContext.setCurrentTenant(tenantName);
		
		if(userLocale != null) {
		    UserContext.setLocale(userLocale);
		}
	}

	protected void infoInteractionLogAPIIncomingCall(String tenantId, String serviceInvoked, String msg, Object... params)
	{
		InteractionLogContext ilc = LogUtil.setInteractionLogThreadContext(tenantId, serviceInvoked, InteractionLogDirection.IN);
		LogUtil.getInteractionLogger().info(msg, params);
		// recover the previous log thread context
		LogUtil.setInteractionLogThreadContext(ilc);
	}

	/*
	 * Updates the specified dashboard by generating all href fields
	 */
	protected Dashboard updateDashboardHref(Dashboard dbd, String tenantName)
	{
		if (dbd == null) {
			LOGGER.error("Error: updating href for a null dashboard");
			return null;
		}
		String externalBase = DashboardAPIUtil.getExternalDashboardAPIBase(tenantName);
		if (StringUtil.isEmpty(externalBase)) {
			LOGGER.error("Error: updating href for a dashboard with null or empty base external url");
			return null;
		}
		String href = externalBase + (externalBase.endsWith("/") ? "" : "/") + dbd.getDashboardId();
		//		String href = uriInfo.getBaseUri() + "v1/dashboards/" + dbd.getDashboardId();
		dbd.setHref(href);
		return dbd;
	}
}
