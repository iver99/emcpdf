/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.metadata.ApplicationEditionConverter;
import oracle.sysman.emaas.platform.dashboards.core.restclient.AppMappingCollection;
import oracle.sysman.emaas.platform.dashboards.core.restclient.AppMappingEntity;
import oracle.sysman.emaas.platform.dashboards.core.restclient.DomainEntity;
import oracle.sysman.emaas.platform.dashboards.core.restclient.DomainsEntity;
import oracle.sysman.emaas.platform.dashboards.core.util.LogUtil.InteractionLogDirection;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil.VersionedLink;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CacheManagers;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.DefaultKeyGenerator;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Keys;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Tenant;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;


/**
 * @author guobaochen
 */
public class TenantSubscriptionUtil
{
	public static class RestClient
	{
		private static final String HTTP_HEADER_OAM_REMOTE_USER = "OAM_REMOTE_USER";
		private static final String HTTP_HEADER_X_USER_IDENTITY_DOMAIN_NAME = "X-USER-IDENTITY-DOMAIN-NAME";

		public RestClient()
		{
		}

		public String get(String url, String tenant, String auth)
		{
			if (url == null || "".equals(url)) {
				return null;
			}

			ClientConfig cc = new DefaultClientConfig();
			Client client = Client.create(cc);
			if (StringUtil.isEmpty(auth)) {
				LOGGER.warn("Warning: RestClient get an empty auth token when connection to url {}", url);
			}
			else {
				LogUtil.setInteractionLogThreadContext(tenant, url, InteractionLogDirection.OUT);
				itrLogger
						.info("RestClient is connecting to get response after getting authorization token from registration manager.");
			}
			try {
				Builder builder = client.resource(UriBuilder.fromUri(url).build()).header(HttpHeaders.AUTHORIZATION, auth)
						.header(HTTP_HEADER_X_USER_IDENTITY_DOMAIN_NAME, tenant).type(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON);
				return builder.get(String.class);
			}
			catch (Exception e) {
				LOGGER.info("context", e);
				itrLogger.error("Exception when RestClient trying to get response from specified service. Message:"
						+ e.getLocalizedMessage());
				return null;
			}
		}

		public String get(String url, String tenantName, String userName, String auth)
		{
			if (url == null || "".equals(url)) {
				return null;
			}

			ClientConfig cc = new DefaultClientConfig();
			Client client = Client.create(cc);
			if (StringUtil.isEmpty(auth)) {
				LOGGER.warn("Warning: RestClient get an empty auth token when connection to url {}", url);
			}
			else {
				LogUtil.setInteractionLogThreadContext(tenantName, url, InteractionLogDirection.OUT);
				itrLogger
						.info("RestClient is connecting to get response after getting authorization token {} from registration manager.", auth);
			}
			try {
				Builder builder = client.resource(UriBuilder.fromUri(url).build()).header(HttpHeaders.AUTHORIZATION, auth)
						.header(HTTP_HEADER_OAM_REMOTE_USER, tenantName + "." + userName)
						.header(HTTP_HEADER_X_USER_IDENTITY_DOMAIN_NAME, tenantName);
				ClientResponse cr = builder.get(ClientResponse.class);
				String response = cr.getEntity(String.class);
				itrLogger.info("Response returned from Rest Client call: " + response);
				return response;
				//				return builder.get(String.class);
			}
			catch (Exception e) {
				LOGGER.info("context", e);
				itrLogger.error("Exception when RestClient trying to get response from specified service. Message:"
						+ e.getLocalizedMessage());
				return null;
			}

		}

		public String put(String url, Map<String, Object> headers, Object requestEntity, String tenant, String auth)
		{
			if (StringUtil.isEmpty(url)) {
				LOGGER.error("Unable to post to an empty URL for requestEntity: \"{}\", tenant: \"{}\"", requestEntity, tenant);
				return null;
			}
			if (requestEntity == null || "".equals(requestEntity)) {
				LOGGER.error("Unable to post an empty request entity");
				return null;
			}

			ClientConfig cc = new DefaultClientConfig();
			cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
			Client client = Client.create(cc);
			if (StringUtil.isEmpty(auth)) {
				LOGGER.warn("Warning: RestClient get an empty auth token when connection to url {}", url);
			}
			else {
				LogUtil.setInteractionLogThreadContext(tenant, url, InteractionLogDirection.OUT);
				LOGGER.info(
						"RestClient is connecting to {} after getting authorization token from registration manager. HTTP method is post.",
						url);
			}
			Builder builder = client.resource(UriBuilder.fromUri(url).build()).header(HttpHeaders.AUTHORIZATION, auth)
					.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
			if (headers != null) {
				for (String key : headers.keySet()) {
					Object value = headers.get(key);
					if (value == null) {
						continue;
					}
					builder.header(key, value);
				}
			}
			return builder.put(requestEntity.getClass(), requestEntity).toString();
		}
	}

	private static Boolean IS_TEST_ENV = null;
	private static Object lock = new Object();
	private static final Logger LOGGER = LogManager.getLogger(TenantSubscriptionUtil.class);
	private static Logger itrLogger = LogUtil.getInteractionLogger();

	@SuppressWarnings("unchecked")
	public static List<String> getTenantSubscribedServices(final String tenant)
	{
		// for junit test only
		if (Boolean.TRUE.equals(IS_TEST_ENV)) {
			LOGGER.warn("In test environment, the subscribed applications for are tenants are specified to \"APM\" and \"ITAnalytics\"");
			return Arrays.asList(new String[] { "APM", "ITAnalytics" });
		}

		// normal behavior here
		if (tenant == null) {
			LOGGER.warn("This is usually unexpected: now it's trying to retrieve subscribed applications for null tenant");
			return Collections.emptyList();
		}
		ICacheManager cm= CacheManagers.getInstance().build();
		Tenant cacheTenant = new Tenant(tenant);
		List<String> cachedApps;
		try {
			cachedApps = (List<String>) cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).get(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)));
		}
		catch (Exception e) {
			LOGGER.error("context", e);
			return Collections.emptyList();
		}
		if (cachedApps != null) {
			LOGGER.debug(
					"retrieved subscribed apps for tenant {} from cache: "
							+ StringUtil.arrayToCommaDelimitedString(cachedApps.toArray()), tenant);
			return cachedApps;
		}

		Link domainLink = RegistryLookupUtil.getServiceInternalLink("EntityNaming", "1.0+", "collection/domains", null);
		if (domainLink == null || domainLink.getHref() == null || "".equals(domainLink.getHref())) {
			LOGGER.warn(
					"Failed to get entity naming service, or its rel (collection/domains) link is empty. Exists the retrieval of subscribed service for tenant {}",
					tenant);
			cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).evict(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)));
			return Collections.emptyList();
		}
		LOGGER.debug("Checking tenant (" + tenant + ") subscriptions. The entity naming href is " + domainLink.getHref());
		String domainHref = domainLink.getHref();
		RestClient rc = new RestClient();
		String domainsResponse = rc.get(domainHref, tenant, ((VersionedLink) domainLink).getAuthToken());
		LOGGER.debug("Checking tenant (" + tenant + ") subscriptions. Domains list response is " + domainsResponse);
		JsonUtil ju = JsonUtil.buildNormalMapper();
		try {
			DomainsEntity de = ju.fromJson(domainsResponse, DomainsEntity.class);
			if (de == null || de.getItems() == null || de.getItems().isEmpty()) {
				LOGGER.warn("Checking tenant (" + tenant
						+ ") subscriptions: null/empty domains entity or domains item retrieved.");
				cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).evict(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)));
				return Collections.emptyList();
			}
			String tenantAppUrl = null;
			for (DomainEntity domain : de.getItems()) {
				if ("TenantApplicationMapping".equals(domain.getDomainName())) {
					tenantAppUrl = domain.getCanonicalUrl();
					break;
				}
			}
			if (tenantAppUrl == null || "".equals(tenantAppUrl)) {
				LOGGER.warn("Checking tenant (" + tenant + ") subscriptions. 'TenantApplicationMapping' not found");
				cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).evict(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)));
				return Collections.emptyList();
			}
			String appMappingUrl = tenantAppUrl + "/lookups?opcTenantId=" + tenant;
			LOGGER.debug("Checking tenant (" + tenant + ") subscriptions. tenant application mapping lookup URL is "
					+ appMappingUrl);
			String appMappingJson = rc.get(appMappingUrl, tenant, ((VersionedLink) domainLink).getAuthToken());
			LOGGER.debug("Checking tenant (" + tenant + ") subscriptions. application lookup response json is " + appMappingJson);
			if (appMappingJson == null || "".equals(appMappingJson)) {
				cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).evict(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)));
				return Collections.emptyList();
			}
			AppMappingCollection amec = ju.fromJson(appMappingJson, AppMappingCollection.class);
			if (amec == null || amec.getItems() == null || amec.getItems().isEmpty()) {
				LOGGER.error("Checking tenant (" + tenant + ") subscriptions. Empty application mapping items are retrieved");
				cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).evict(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)));
				return Collections.emptyList();
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
				LOGGER.error("Checking tenant (" + tenant
						+ ") subscriptions. Failed to get an application mapping for the specified tenant");
				cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).evict(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)));
				return Collections.emptyList();
			}
			String apps = null;
			for (AppMappingEntity.AppMappingValue amv : ame.getValues()) {
				if (tenant.equals(amv.getOpcTenantId())) {
					apps = amv.getApplicationNames();
					break;
				}
			}
			LOGGER.debug("Checking tenant (" + tenant + ") subscriptions. applications for the tenant are " + apps);
			if (apps == null || "".equals(apps)) {
				cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).evict(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)));
				return Collections.emptyList();
			}
			List<String> origAppsList = Arrays.asList(apps
					.split(ApplicationEditionConverter.APPLICATION_EDITION_ELEMENT_DELIMINATOR));
			cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).put(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)),origAppsList);

			return origAppsList;

		}
		catch (IOException e) {
			LOGGER.error(e);
			return Collections.emptyList();
		}
	}

	public static boolean isAPMServiceOnly(List<String> services)
	{
		LOGGER.debug("Checking if only APM is subscribed, checked services are {}", services == null ? null : services.toString());
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

	public static void setTestEnv()
	{
		synchronized (lock) {
			if (IS_TEST_ENV == null) {
				IS_TEST_ENV = true;
			}
		}
	}

	private TenantSubscriptionUtil()
	{
	}
}
