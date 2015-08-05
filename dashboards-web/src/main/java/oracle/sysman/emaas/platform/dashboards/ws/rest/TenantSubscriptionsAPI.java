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
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.TenantWithoutSubscriptionException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.subappedition.ServiceEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.subappedition.TenantDetailEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.subappedition.TenantEditionEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jersey.api.client.UniformInterfaceException;

/**
 * @author guobaochen
 */
@Path("/v1/subscribedapps")
public class TenantSubscriptionsAPI extends APIBase
{
	public static class SubscribedAppsEntity<E>
	{
		private List<E> applications;

		public SubscribedAppsEntity(List<E> apps)
		{
			applications = apps;
		}

		public List<E> getApplications()
		{
			return applications;
		}

		public void setApplications(List<E> applications)
		{
			this.applications = applications;
		}
	}

	private static Logger logger = LogManager.getLogger(TenantSubscriptionsAPI.class.getName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubscribedApplications(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @QueryParam("withEdition") String withEdition)
	{
		if (withEdition != null && withEdition.toLowerCase().equals("true")) { // subscriptions with edition
			return getSubscribedApplicationsWithEdition(tenantIdParam, userTenant);
		}

		// handling normal requests without edition
		try {
			initializeUserContext(tenantIdParam, userTenant);
			String tenantName = TenantContext.getCurrentTenant();
			List<String> apps = TenantSubscriptionUtil.getTenantSubscribedServices(tenantName);
			if (apps == null || apps.isEmpty()) {
				throw new TenantWithoutSubscriptionException();
			}
			SubscribedAppsEntity<String> sae = new SubscribedAppsEntity<String>(apps);
			return Response.ok(getJsonUtil().toJson(sae)).build();
		}
		catch (CommonSecurityException e) {
			logger.error(e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (TenantWithoutSubscriptionException e) {
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	private Response getSubscribedApplicationsWithEdition(String tenantIdParam, String userTenant)
	{
		try {
			initializeUserContext(tenantIdParam, userTenant);
			String tenantName = TenantContext.getCurrentTenant();
			// normal behavior here
			Link tenantsLink = RegistryLookupUtil.getServiceInternalLink("TenantService", "0.1", "collection/tenants", null);
			if (tenantsLink == null || tenantsLink.getHref() == null || "".equals(tenantsLink.getHref())) {
				throw new TenantWithoutSubscriptionException();
			}
			logger.debug("Checking tenant (" + tenantName + ") subscriptions with edition. The tenant service href is "
					+ tenantsLink.getHref());
			String tenantHref = tenantsLink.getHref() + "/" + tenantName;
			TenantSubscriptionUtil.RestClient rc = new TenantSubscriptionUtil.RestClient();
			String tenantResponse = rc.get(tenantHref);
			logger.debug("Checking tenant (" + tenantName + ") subscriptions with edition. Tenant response is " + tenantResponse);
			JsonUtil ju = JsonUtil.buildNormalMapper();
			TenantDetailEntity de = ju.fromJson(tenantResponse, TenantDetailEntity.class);
			if (de == null || de.getServices() == null) {
				throw new TenantWithoutSubscriptionException();
			}
			List<TenantEditionEntity> teeList = new ArrayList<TenantEditionEntity>();
			for (ServiceEntity se : de.getServices()) {
				TenantEditionEntity tee = new TenantEditionEntity(se.getServiceType(), se.getEdition());
				teeList.add(tee);
			}
			SubscribedAppsEntity<TenantEditionEntity> sae = new SubscribedAppsEntity<TenantEditionEntity>(teeList);
			return Response.ok(getJsonUtil().toJson(sae)).build();
		}
		catch (CommonSecurityException | TenantWithoutSubscriptionException e) {
			logger.error(e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (IOException | UniformInterfaceException e) {
			logger.error(e);
			return buildErrorResponse(new ErrorEntity(new TenantWithoutSubscriptionException()));
		}
	}
}
