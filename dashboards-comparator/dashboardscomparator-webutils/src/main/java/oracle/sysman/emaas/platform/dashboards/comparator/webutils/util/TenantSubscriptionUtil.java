/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.webutils.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.metadata.ApplicationEditionConverter;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.json.AppMappingCollection;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.json.AppMappingEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.json.DomainEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.json.DomainsEntity;

/**
 * @author guobaochen
 */
public class TenantSubscriptionUtil
{
	
	private static Logger logger = LogManager.getLogger(TenantSubscriptionUtil.class);
	private static Logger itrLogger = LogUtil.getInteractionLogger();

	public static List<String> getTenantSubscribedServices(String tenant)
	{
		if (tenant == null) {
			return null;
		}
		Link domainLink = RegistryLookupUtil.getServiceInternalLink("EntityNaming", "1.0+", "collection/domains", tenant);
		if (domainLink == null || StringUtils.isEmpty(domainLink.getHref())) {
			logger.warn("Checking tenant (" + tenant
					+ ") subscriptions: null/empty entity naming service collection/domains is retrieved.");
			return null;
		}
		logger.info("Checking tenant (" + tenant + ") subscriptions. The entity naming href is " + domainLink.getHref());
		String domainHref = domainLink.getHref();
		RestClient rc = RestClientProxy.getRestClient();
		String domainsResponse = rc.get(domainHref, tenant, ((VersionedLink)domainLink).getAuthToken());
		logger.info("Checking tenant (" + tenant + ") subscriptions. Domains list response is " + domainsResponse);
		JsonUtil ju = JsonUtil.buildNormalMapper();
		try {
			DomainsEntity de = ju.fromJson(domainsResponse, DomainsEntity.class);
			if (de == null || de.getItems() == null || de.getItems().size() <= 0) {
				logger.warn(
						"Checking tenant (" + tenant + ") subscriptions: null/empty domains entity or domains item retrieved.");
				return null;
			}
			String tenantAppUrl = null;
			for (DomainEntity domain : de.getItems()) {
				if ("TenantApplicationMapping".equals(domain.getDomainName())) {
					tenantAppUrl = domain.getCanonicalUrl();
					break;
				}
			}
			if (tenantAppUrl == null || "".equals(tenantAppUrl)) {
				logger.warn("Checking tenant (" + tenant + ") subscriptions. 'TenantApplicationMapping' not found");
				return null;
			}
			String appMappingUrl = tenantAppUrl + "/lookups?opcTenantId=" + tenant;
			logger.info(
					"Checking tenant (" + tenant + ") subscriptions. tenant application mapping lookup URL is " + appMappingUrl);
			String appMappingJson = rc.get(appMappingUrl, tenant, null);
			logger.info("Checking tenant (" + tenant + ") subscriptions. application lookup response json is " + appMappingJson);
			if (appMappingJson == null || "".equals(appMappingJson)) {
				return null;
			}
			AppMappingCollection amec = ju.fromJson(appMappingJson, AppMappingCollection.class);
			if (amec == null || amec.getItems() == null || amec.getItems().isEmpty()) {
				logger.error("Checking tenant (" + tenant + ") subscriptions. Empty application mapping items are retrieved");
				return null;
			}
			AppMappingEntity ame = null;
			for (AppMappingEntity entity : amec.getItems()) {
				if (entity.getValues() == null) {
					continue;
				}
				for (AppMappingEntity.AppMappingValue amv : entity.getValues()) {
					if (tenant.equals(amv.getOpcTenantId())) {
						ame = entity;
						break;
					}

				}
			}
			if (ame == null || ame.getValues() == null || ame.getValues().isEmpty()) {
				logger.error("Checking tenant (" + tenant
						+ ") subscriptions. Failed to get an application mapping for the specified tenant");
				return null;
			}
			String apps = null;
			for (AppMappingEntity.AppMappingValue amv : ame.getValues()) {
				if (tenant.equals(amv.getOpcTenantId())) {
					apps = amv.getApplicationNames();
					break;
				}
			}
			logger.info("Checking tenant (" + tenant + ") subscriptions. applications for the tenant are " + apps);
			if (apps == null || "".equals(apps)) {
				return null;
			}
			List<String> origAppsList = Arrays
					.asList(apps.split(ApplicationEditionConverter.APPLICATION_EDITION_ELEMENT_DELIMINATOR));
			return origAppsList;

		}
		catch (IOException e) {
			logger.error(e);
			return null;
		}
	}

	public static boolean isAPMServiceOnly(List<String> services)
	{
		if (services == null || services.size() != 1) {
			return false;
		}
		String svc = services.get(0);
		if (svc == null) {
			return false;
		}
		if (svc.equals(ApplicationEditionConverter.ApplicationOPCName.APM.toString())) {
			return true;
		}
		return false;
	}

	public static boolean isMonitoringServiceOnly(List<String> services)
	{
		if (services == null || services.size() != 1) {
			return false;
		}
		String svc = services.get(0);
		if (svc == null) {
			return false;
		}
		//TODO update to use ApplicationEditionConverter.ApplicationOPCName once it's updated in tenant sdk
		if (svc.equals("Monitoring")) {
			return true;
		}
		return false;
	}

	public static class VersionedLink extends Link {
		private String authToken;

		public VersionedLink() {

		}

		public VersionedLink(Link link, String authToken) {
			withHref(link.getHref());
			withOverrideTypes(link.getOverrideTypes());
			withRel(link.getRel());
			withTypesStr(link.getTypesStr());
			this.authToken = authToken;
		}

		/**
		 * @return the authToken
		 */
		public String getAuthToken()
		{
			return authToken;
		}

		/**
		 * @param authToken
		 *            the authToken to set
		 */
		public void setAuthToken(String authToken)
		{
			this.authToken = authToken;
		}
	}

}
