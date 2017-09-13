/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.emcpdf.registry;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceQuery;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.SanitizedInstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CacheManagers;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.DefaultKeyGenerator;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Keys;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Tenant;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import oracle.sysman.emaas.platform.emcpdf.cache.util.StringUtil;
import oracle.sysman.emaas.platform.emcpdf.registry.model.EndpointEntity;
import oracle.sysman.emaas.platform.uifwk.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author aduan
 */
public class RegistryLookupUtil
{
	private static final Logger LOGGER = LogManager.getLogger(RegistryLookupUtil.class);
	private static Logger itrLogger = LogUtil.getInteractionLogger();
	// keep the following the same with service name
	public static final String APM_SERVICE = "ApmUI";

	public static final String ITA_SERVICE = "emcitas-ui-apps";
	public static final String LA_SERVICE = "LogAnalyticsUI";
	public static final String TA_SERVICE = "TargetAnalytics";
	public static final String MONITORING_SERVICE = "MonitoringServiceUI";
	public static final String SECURITY_ANALYTICS_SERVICE = "SecurityAnalyticsUI";
	public static final String COMPLIANCE_SERVICE = "ComplianceUIService";
	public static final String ORCHESTRATION_SERVICE = "CosUIService";
	
	public static final String DEFAULT_VERSION = "1.0+";

	private static final String HTTP = "http";
	private static final String HTTPS = "https";

    /**
     * get internal link for <b>non-tenant</b> request by 
     * @param serviceName
     * @param rel
     * @return
     */
    public static VersionedLink getServiceInternalLink(String serviceName, String rel)
    {
        return RegistryLookupUtil.getServiceInternalLink(serviceName, rel, null);
    }
    
    /**
     * get internal link for <b>tenantName</b> by 
     * @param serviceName
    
     * @param rel
     * @param tenantName
     * @return
     */
    public static VersionedLink getServiceInternalLink(String serviceName, String rel, String tenantName)
    {
        return RegistryLookupUtil.getServiceInternalLink(serviceName, DEFAULT_VERSION, rel, tenantName);
    }
	
	public static VersionedLink getServiceInternalLink(String serviceName, String version, String rel, String tenantName)
	{
		return RegistryLookupUtil.getServiceInternalLink(serviceName, version, rel, false, tenantName);
	}

	public static VersionedLink getServiceExternalLink(String serviceName, String version, String rel, String tenantName)
	{
		return RegistryLookupUtil.getServiceExternalLink(serviceName, version, rel, false, tenantName);
	}

	public static VersionedLink getServiceExternalLinkWithRelPrefix(String serviceName, String version, String rel,
																	String tenantName){
		return RegistryLookupUtil.getServiceExternalLink(serviceName, version, rel, true, tenantName);
	}

	public static String getServiceExternalEndPoint(String serviceName, String version, String tenantName)
	{
		LOGGER.debug(
				"/getServiceExternalEndPoint/ Trying to retrieve service external end point for service: \"{}\", version: \"{}\", tenant: \"{}\"",
				serviceName, version, tenantName);
		InstanceInfo queryInfo = getInstanceInfo(serviceName, version);
		LogUtil.setInteractionLogThreadContext(tenantName, "Retristry lookup client", LogUtil.InteractionLogDirection.OUT);
		itrLogger.debug("Retrieved instance {}", queryInfo);
		SanitizedInstanceInfo sanitizedInstance;
		InstanceInfo internalInstance = null;
		try {
			if (!StringUtil.isEmpty(tenantName)) {
				internalInstance = LookupManager.getInstance().getLookupClient().getInstanceForTenant(queryInfo, tenantName);
				itrLogger.debug("#1. Retrieved instance {} by using getInstanceForTenant for tenant {}", internalInstance, tenantName);
				if (internalInstance == null) {
					LOGGER.error(
							"#1.Error: retrieved null instance info with getInstanceForTenant. Details: serviceName={}, version={}, tenantName={}",
							serviceName, version, tenantName);
				}
			}
			else {
				internalInstance = LookupManager.getInstance().getLookupClient().getInstance(queryInfo);
				itrLogger.debug("Retrieved internal instance {} by using LookupClient.getInstance");
			}
			sanitizedInstance = LookupManager.getInstance().getLookupClient().getSanitizedInstanceInfo(internalInstance);
			itrLogger.debug("Retrieved sanitized instance {} by using LookupClient.getSanitizedInstanceInfo");
			if (sanitizedInstance == null) {
				return RegistryLookupUtil.getInternalEndPoint(internalInstance);
				//				return "https://slc07hcn.us.oracle.com:4443/microservice/c8c62151-e90d-489a-83f8-99c741ace530/";
				// this happens when
				//    1. no instance exists based on the query criteria
				// or
				//    2. the selected instance does not expose any safe endpoints that are externally routeable (e.g., no HTTPS virtualEndpoints)
				//
				// In this case, need to trigger the failover scheme, or alternatively, one could use the plural form of the lookup, and loop through the returned instances
			}
			else {
				return RegistryLookupUtil.getExternalEndPoint(sanitizedInstance);
			}
		}
		catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			if (internalInstance != null) {
				return RegistryLookupUtil.getInternalEndPoint(internalInstance);
			}
		}
		return null;
	}

	public static EndpointEntity getServiceExternalEndPointEntity(String serviceName, String version, String tenantName)
	{
		Link link = RegistryLookupUtil.getServiceExternalLink(serviceName, version, "sso.endpoint/virtual", tenantName);
		if (link != null) {
			return new EndpointEntity(serviceName, version, link.getHref());
		}
		else {
			return null;
		}
	}

	public static VersionedLink getServiceInternalEndpoint(String serviceName, String version, String tenantName)
	{
		LOGGER.debug(
				"/getServiceInternalEndpoint/ Trying to retrieve service internal link for service: \"{}\", version: \"{}\", tenant: \"{}\"",
				serviceName, version, tenantName);
		InstanceInfo info = getInstanceInfo(serviceName, version);
		VersionedLink link = new VersionedLink();
		try {
			List<InstanceInfo> result = null;
			if (!StringUtil.isEmpty(tenantName)) {
				InstanceInfo ins = LookupManager.getInstance().getLookupClient().getInstanceForTenant(info, tenantName);
				itrLogger.debug("#2. Retrieved instance {} by using getInstanceForTenant for tenant {}", ins, tenantName);
				if (ins == null) {
					LOGGER.error(
							"#2.Error: retrieved null instance info with getInstanceForTenant. Details: serviceName={}, version={}, tenantName={}",
							serviceName, version, tenantName);
				}
				else {
					result = new ArrayList<InstanceInfo>();
					result.add(ins);
				}

			}
			else {
				result = LookupManager.getInstance().getLookupClient().lookup(new InstanceQuery(info));
			}
			String endpoint = null;
			if (result != null && !result.isEmpty()) {
				// [EMCPDF-733] Rest client can't handle https currently, so http protocol is enough for internal use
				//https link is not found, then find http link
				for (InstanceInfo internalInstance : result) {
					endpoint = RegistryLookupUtil.getHttpInternalEndPoint(internalInstance);
					if (endpoint != null) {
						break;
					}
				}
			}
			link.withHref(endpoint);
			link.setAuthToken(getAuthorizationAccessToken(info));
			return link;
		}
		catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return link;
		}
	}

	private static EndpointEntity replaceVanityUrlDomainForEndpointEntity(String domainPort, EndpointEntity ee, String tenantName)
	{
		LOGGER.debug("/replaceDomainForEndpointEntity/ Trying to replace endpoint entity \"{}\" with domain \"{}\"",
				ee != null ? ee.getHref() : null, domainPort);
		if (StringUtil.isEmpty(domainPort) || ee == null || StringUtil.isEmpty(ee.getHref())) {
			return ee;
		}
		String replacedHref = RegistryLookupUtil.replaceVanityUrlDomainForUrl(domainPort, ee.getHref(), tenantName);
		LOGGER.debug("/replaceDomainForEndpointEntity/ Endpoint entity \"{}\" URL (after replaced) is \"{}\"", ee.getHref(),
				replacedHref);
		ee.setHref(replacedHref);
		return ee;
	}

	private static VersionedLink getServiceInternalLink(String serviceName, String version, String rel, boolean prefixMatch,
			String tenantName)
	{
		LOGGER.debug(
				"/getServiceInternalLink/ Trying to retrieve service internal link for service: \"{}\", version: \"{}\", rel: \"{}\", prefixMatch: \"{}\", tenant: \"{}\"",
				serviceName, version, rel, prefixMatch, tenantName);
		InstanceInfo info = getInstanceInfo(serviceName, version);
		VersionedLink lk = null;
		try {
			List<InstanceInfo> result = null;
			if (null != tenantName && !"".equals(tenantName.trim())) {
				InstanceInfo ins = LookupManager.getInstance().getLookupClient().getInstanceForTenant(info, tenantName);
				LOGGER.debug("#3. Retrieved instance {} by using getInstanceForTenant for tenant {}", ins, tenantName);
				if (ins == null) {
					LOGGER.error(
							"#3.Error: retrieved null instance info with getInstanceForTenant. Details: serviceName={}, version={}, tenantName={}",
							serviceName, version, tenantName);
				}
				else {
					result = new ArrayList<InstanceInfo>();
					result.add(ins);
				}

			}
			else {
				result = LookupManager.getInstance().getLookupClient().lookup(new InstanceQuery(info));
			}
			if (result != null && !result.isEmpty()) {
				// [EMCPDF-733] Rest client can't handle https currently, so http protocol is enough for internal use
				// find http link only
				for (InstanceInfo internalInstance : result) {
					List<Link> links = null;
					if (prefixMatch) {
						links = internalInstance.getLinksWithRelPrefixWithProtocol(rel, HTTP);
					}
					else {
						links = internalInstance.getLinksWithProtocol(rel, HTTP);
					}
					if (links != null && !links.isEmpty()) {
						lk = new VersionedLink(links.get(0), getAuthorizationAccessToken(internalInstance));
						return lk;
					}
				}
			}
			return lk;
		}
		catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return lk;
		}
	}

	private static VersionedLink getServiceExternalLink(String serviceName, String version, String rel, boolean prefixMatch,
														String tenantName)
	{
		LOGGER.debug(
				"/getServiceExternalLink/ Trying to retrieve service external link for service: \"{}\", version: \"{}\", rel: \"{}\", tenant: \"{}\"",
				serviceName, version, rel, tenantName);
		InstanceInfo info = getInstanceInfo(serviceName, version);
		VersionedLink lk = null;
		try {
			List<InstanceInfo> result = null;
			if (!StringUtil.isEmpty(tenantName)) {
				InstanceInfo ins = LookupManager.getInstance().getLookupClient().getInstanceForTenant(info, tenantName);
				LOGGER.debug("#4. Retrieved instance {} by using getInstanceForTenant for tenant {}", ins, tenantName);
				if (ins == null) {
					LOGGER.error(
							"#4.Error: retrieved null instance info with getInstanceForTenant. Details: serviceName={}, version={}, tenantName={}",
							serviceName, version, tenantName);
					//TODO not sure if this is necessary, some RegistryLookupUtil have this line of code, some not.
					result = LookupManager.getInstance().getLookupClient().lookup(new InstanceQuery(info));
				}
				else {
					result = new ArrayList<InstanceInfo>();
					result.add(ins);
				}

			}
			else {
				result = LookupManager.getInstance().getLookupClient().lookup(new InstanceQuery(info));
			}
			if (result != null && !result.isEmpty()) {

				//find https link first
				for (InstanceInfo internalInstance : result) {
					List<Link> links = null;
					if (prefixMatch) {
						links = internalInstance.getLinksWithRelPrefixWithProtocol(rel, "https");
					}
					else {
						links = internalInstance.getLinksWithProtocol(rel, "https");
					}
					if (version == null) {
						version = internalInstance.getVersion();
						LOGGER.debug(
								"Input version is null. Retrieved version from internalInstance for service: \"{}\" is \"{}\", rel: \"{}\", tenant: \"{}\"",
								serviceName, version, rel, tenantName);
					}

					SanitizedInstanceInfo sanitizedInstance = RegistryLookupUtil.findSaniInsInfo(tenantName, internalInstance);
					if (sanitizedInstance != null) {
						if (prefixMatch) {
							links = RegistryLookupUtil.getLinksWithRelPrefixWithProtocol("https", rel,
									sanitizedInstance.getLinks());
						}
						else {
							links = RegistryLookupUtil.getLinksWithProtocol("https", sanitizedInstance.getLinks(rel));
						}
						if (version == null) {
							version = sanitizedInstance.getVersion();
							LOGGER.debug(
									"Input version is null. Retrieved version from sanitizedInstance for service: \"{}\" is \"{}\", rel: \"{}\", tenant: \"{}\"",
									serviceName, version, rel, tenantName);
						}
					}
					if (links != null && !links.isEmpty()) {
						lk = new VersionedLink(links.get(0), getAuthorizationAccessToken(internalInstance));
						break;
					}
				}

				if (lk != null) {
					LOGGER.debug(
							"[branch 1] Retrieved link: \"{}\" for service: \"{}\", version: \"{}\", rel: \"{}\", tenant: \"{}\"",
							lk.getHref(), serviceName, version, rel, tenantName);
					return lk;
				}

				//https link is not found, then find http link
				for (InstanceInfo internalInstance : result) {
					List<Link> links = null;
					if (prefixMatch) {
						links = internalInstance.getLinksWithRelPrefixWithProtocol(rel, HTTP);
					}
					else {
						links = internalInstance.getLinksWithProtocol(rel, HTTP);
					}
					if (version == null) {
						version = internalInstance.getVersion();
					}

					SanitizedInstanceInfo sanitizedInstance = RegistryLookupUtil.findSaniInsInfo(tenantName, internalInstance);
					if (sanitizedInstance != null) {
						if (prefixMatch) {
							links = RegistryLookupUtil.getLinksWithRelPrefixWithProtocol(HTTP, rel,
									sanitizedInstance.getLinks());
						}
						else {
							links = RegistryLookupUtil.getLinksWithProtocol(HTTP, sanitizedInstance.getLinks(rel));
						}
						if (version == null) {
							version = sanitizedInstance.getVersion();
						}
					}
					if (links != null && !links.isEmpty()) {
						lk = new VersionedLink(links.get(0), getAuthorizationAccessToken(internalInstance));
						LOGGER.debug(
								"[branch 2] Retrieved link: \"{}\" for service: \"{}\", version: \"{}\", rel: \"{}\", tenant: \"{}\"",
								lk == null ? null : lk.getHref(), serviceName, version, rel, tenantName);
						return lk;
					}
				}
			}
			return lk;
		}
		catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return lk;
		}
	}

	private static List<Link> getLinksWithProtocol(String protocol, List<Link> links)
	{
		if (protocol == null || links == null || protocol.length() == 0 || links.isEmpty()) {
			if (links == null) {
				return new ArrayList<Link>();
			}
			return links;
		}
		List<Link> protocoledLinks = new ArrayList<Link>();
		for (Link link : links) {
			try {
				URI uri = URI.create(link.getHref());
				if (protocol.equalsIgnoreCase(uri.getScheme())) {
					protocoledLinks.add(link);
				}
			}
			catch (Exception thr) {
				LOGGER.error(thr.getLocalizedMessage(), thr);
				return protocoledLinks;
			}
		}

		return protocoledLinks;
	}

	private static List<Link> getLinksWithRelPrefixWithProtocol(String protocol, String relPrefix, List<Link> links)
	{
		if (protocol == null || relPrefix == null || links == null || protocol.length() == 0 || links.isEmpty()) {
			if (links == null) {
				return new ArrayList<Link>();
			}
			return links;
		}
		List<Link> protocoledLinks = new ArrayList<Link>();
		for (Link link : links) {
			try {
				URI uri = URI.create(link.getHref());
				if (protocol.equalsIgnoreCase(uri.getScheme()) && link.getRel() != null && link.getRel().indexOf(relPrefix) == 0) {
					protocoledLinks.add(link);
				}
			}
			catch (Exception thr) {
				LOGGER.error(thr.getLocalizedMessage(), thr);
				return protocoledLinks;
			}
		}

		return protocoledLinks;
	}
	
    public static String getAuthorizationAccessToken(InstanceInfo instanceInfo)
    {
        char[] authToken = LookupManager.getInstance().getAuthorizationAccessToken(instanceInfo);
        return new String(authToken);
    }

	private static SanitizedInstanceInfo findSaniInsInfo(String tenantName, InstanceInfo internalInstance)
	{
		SanitizedInstanceInfo sanitizedInstance = null;
		try {
			if (!StringUtil.isEmpty(tenantName)) {
				sanitizedInstance = LookupManager.getInstance().getLookupClient()
						.getSanitizedInstanceInfo(internalInstance, tenantName);
				LOGGER.debug("Retrieved sanitizedInstance {} by using getSanitizedInstanceInfo for tenant {}", sanitizedInstance,
						tenantName);
			}
			else {
				LOGGER.warn("Failed to retrieve tenant when getting external end point. Using tenant non-specific APIs to get sanitized instance");
				sanitizedInstance = LookupManager.getInstance().getLookupClient().getSanitizedInstanceInfo(internalInstance);
			}
		}
		catch (Exception ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
		}
		return sanitizedInstance;
	}

	public static Map<String, String> getVanityBaseURLs(String tenantName)
	{
        ICacheManager cm = CacheManagers.getInstance().build();
        LOGGER.debug("/getVanityBaseURLs/ trying to get base vanity URLs for tenant: \"{}\"", tenantName);
        Tenant cacheTenant = new Tenant(tenantName);
        Object cacheKey = DefaultKeyGenerator.getInstance().generate(cacheTenant, new Keys(CacheConstants.LOOKUP_CACHE_KEY_VANITY_BASE_URL));
        Map<String, String> map = null;
        try {
            map = (Map<String, String>) cm.getCache(CacheConstants.CACHES_VANITY_BASE_URL_CACHE).get(cacheKey);
            if (map != null) {
                LOGGER.debug("Retrieved vanity base url from cache for tenant {}",tenantName);
                return map;
            }
        }
        catch (Exception e) {
            LOGGER.error(e);
        }

		LOGGER.debug("/getVanityBaseURLs/ Trying to retrieve service internal link for tenant: \"{}\"", tenantName);
		InstanceInfo info = getInstanceInfo("OHS", null);
		Link lk = null;
		map = new HashMap<String, String>();
		try {
			List<InstanceInfo> result = LookupManager.getInstance().getLookupClient().lookup(new InstanceQuery(info));
			if (result != null && !result.isEmpty()) {
				for (InstanceInfo internalInstance : result) {
					if (map.containsKey(APM_SERVICE) && map.containsKey(ITA_SERVICE) && map.containsKey(LA_SERVICE)
							&& map.containsKey(MONITORING_SERVICE) && map.containsKey(SECURITY_ANALYTICS_SERVICE)
							&& map.containsKey(COMPLIANCE_SERVICE) && map.containsKey(ORCHESTRATION_SERVICE)) {
						break;
					}
					if (!map.containsKey(APM_SERVICE)) {
						List<Link> links = internalInstance.getLinksWithProtocol("vanity/apm", HTTPS);
						links = RegistryLookupUtil.getLinksWithProtocol(HTTPS, links);

						if (links != null && !links.isEmpty()) {
							lk = links.get(0);
							LOGGER.debug("Retrieved base vanity URL for apm: {} ", lk.getHref());
							String url = RegistryLookupUtil.insertTenantIntoVanityBaseUrl(tenantName, lk.getHref());
							LOGGER.debug("Tenant id is inserted into the base vanity URL for apm. The URL is {}", url);
							map.put(APM_SERVICE, url);
						}
					}
					if (!map.containsKey(ITA_SERVICE)) {
						List<Link> links = internalInstance.getLinksWithProtocol("vanity/ita", HTTPS);
						links = RegistryLookupUtil.getLinksWithProtocol(HTTPS, links);

						if (links != null && !links.isEmpty()) {
							lk = links.get(0);
							LOGGER.debug("Retrieved base vanity URL for ita: {} ", lk.getHref());
							String url = RegistryLookupUtil.insertTenantIntoVanityBaseUrl(tenantName, lk.getHref());
							LOGGER.debug("Tenant id is inserted into the base vanity URL for ita. The URL is {}", url);
							map.put(ITA_SERVICE, url);
							// ta/ita has the same URL pattern
							map.put(TA_SERVICE, url);
						}
					}
					if (!map.containsKey(LA_SERVICE)) {
						List<Link> links = internalInstance.getLinksWithProtocol("vanity/la", HTTPS);
						links = RegistryLookupUtil.getLinksWithProtocol(HTTPS, links);

						if (links != null && !links.isEmpty()) {
							lk = links.get(0);
							LOGGER.debug("Retrieved base vanity URL for la: {} ", lk.getHref());
							String url = RegistryLookupUtil.insertTenantIntoVanityBaseUrl(tenantName, lk.getHref());
							LOGGER.debug("Tenant id is inserted into the base vanity URL for la. The URL is {}", url);
							map.put(LA_SERVICE, url);
						}
					}
					if (!map.containsKey(MONITORING_SERVICE)) {
						List<Link> links = internalInstance.getLinksWithProtocol("vanity/monitoring", HTTPS);
						links = RegistryLookupUtil.getLinksWithProtocol(HTTPS, links);

						if (links != null && !links.isEmpty()) {
							lk = links.get(0);
							LOGGER.debug("Retrieved base vanity URL for monitoring service: {} ", lk.getHref());
							String url = RegistryLookupUtil.insertTenantIntoVanityBaseUrl(tenantName, lk.getHref());
							LOGGER.debug("Tenant id is inserted into the base vanity URL for monitoring service. The URL is {}",
									url);
							map.put(MONITORING_SERVICE, url);
						}
					}
					if (!map.containsKey(SECURITY_ANALYTICS_SERVICE)) {
						List<Link> links = internalInstance.getLinksWithProtocol("vanity/security", HTTPS);
						links = RegistryLookupUtil.getLinksWithProtocol(HTTPS, links);

						if (links != null && !links.isEmpty()) {
							lk = links.get(0);
							LOGGER.debug("Retrieved base vanity URL for Security Analytics service: {} ", lk.getHref());
							String url = RegistryLookupUtil.insertTenantIntoVanityBaseUrl(tenantName, lk.getHref());
							LOGGER.debug(
									"Tenant id is inserted into the base vanity URL for Security Analytics service. The URL is {}",
									url);
							map.put(SECURITY_ANALYTICS_SERVICE, url);
						}
					}
					if (!map.containsKey(COMPLIANCE_SERVICE)) {
						List<Link> links = internalInstance.getLinksWithProtocol("vanity/compliance", HTTPS);
						links = RegistryLookupUtil.getLinksWithProtocol(HTTPS, links);

						if (links != null && !links.isEmpty()) {
							lk = links.get(0);
							LOGGER.debug("Retrieved base vanity URL for Compliance service: {} ", lk.getHref());
							String url = RegistryLookupUtil.insertTenantIntoVanityBaseUrl(tenantName, lk.getHref());
							LOGGER.debug("Tenant id is inserted into the base vanity URL for Compliance service. The URL is {}",
									url);
							map.put(COMPLIANCE_SERVICE, url);
						}
					}
					if (!map.containsKey(ORCHESTRATION_SERVICE)) {
						List<Link> links = internalInstance.getLinksWithProtocol("vanity/ocs", HTTPS);
						links = RegistryLookupUtil.getLinksWithProtocol(HTTPS, links);

						if (links != null && !links.isEmpty()) {
							lk = links.get(0);
							LOGGER.debug("Retrieved base vanity URL for Orchestration service: {} ", lk.getHref());
							String url = RegistryLookupUtil.insertTenantIntoVanityBaseUrl(tenantName, lk.getHref());
							LOGGER.debug(
									"Tenant id is inserted into the base vanity URL for Orchestration service. The URL is {}",
									url);
							map.put(ORCHESTRATION_SERVICE, url);
						}
					}
				}
			}
		}
		catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}

		if (LOGGER.isDebugEnabled() && !map.isEmpty()) {
			LOGGER.debug("Printing out vanity URLs map:");
			for (Map.Entry service : map.entrySet()) {
				LOGGER.debug("service name is {}, and url is {}", service.getKey(), service.getValue());
			}
		}
        cm.getCache(CacheConstants.CACHES_VANITY_BASE_URL_CACHE).put(cacheKey, map);
		return map;
	}

	private static String replaceVanityUrlDomainForUrl(String vanityBaseUrl, String targetUrl, String tenantName)
	{
		if (StringUtil.isEmpty(vanityBaseUrl) || StringUtil.isEmpty(targetUrl) || targetUrl.indexOf("://") == -1) {
			return targetUrl;
		}
		// replace URLs started with tenant only
		String[] splittedProtocol = targetUrl.split("://");
		if (splittedProtocol == null || splittedProtocol.length < 2) {
			LOGGER.warn("Specified url \"{}\" is invalid, can't splitted into multiple parts by '://'", targetUrl);
			return targetUrl;
		}
		if (splittedProtocol[1] == null || !splittedProtocol[1].startsWith(tenantName)) {
			LOGGER.debug(
					"Do not need to replace the url with vanity URL, because the URL \"{}\" doesn't start with opc tenant id",
					targetUrl);
			return targetUrl;
		}
		LOGGER.info("Replacing with vanity base URL for target url. Vanity url is {}, url is {}", vanityBaseUrl, targetUrl);
		String domainToReplace = vanityBaseUrl;
		if (domainToReplace.indexOf("://") != -1) {
			String[] splittedDomain = domainToReplace.split("://");
			if (splittedDomain != null && splittedDomain.length > 1) {
				domainToReplace = splittedDomain[1];
			}
		}
		LOGGER.info("Replacing with vanity base url for url. Vanity url w/o protocol is {}", vanityBaseUrl);
		StringBuilder sb = new StringBuilder();

		sb.append(splittedProtocol[0]);
		sb.append("://");
		sb.append(domainToReplace);

		if (splittedProtocol[1].indexOf('/') != -1) {
			String[] splitted = splittedProtocol[1].split("/");
			if (splitted.length > 1) {
				for (int i = 1; i < splitted.length; i++) {
					sb.append("/");
					sb.append(splitted[i]);
				}
			}
		}
		LOGGER.info("After replacing with vanity url, the target url is: \"{}\"", sb.toString());
		return sb.toString();
	}

	private static String insertTenantIntoVanityBaseUrl(String tenantName, String vanityBaseUrl)
	{
		LOGGER.debug("/insertTenantIntoVanityBaseUrl/ Trying to insert tenant \"{}\" to base vanity url \"{}\"", tenantName,
				vanityBaseUrl);
		if (StringUtil.isEmpty(tenantName) || StringUtil.isEmpty(vanityBaseUrl)) {
			return vanityBaseUrl;
		}

		if (vanityBaseUrl.indexOf("://") != -1) {
			String[] splittedProtocol = vanityBaseUrl.split("://");
			StringBuilder sb = new StringBuilder();
			sb.append(splittedProtocol[0]);
			sb.append("://");
			sb.append(tenantName);
			sb.append(".");
			for (int i = 1; i < splittedProtocol.length; i++) {
				sb.append(splittedProtocol[i]);
				if (i != splittedProtocol.length - 1) {
					sb.append("://");
				}
			}
			LOGGER.debug("/insertTenantIntoVanityBaseUrl/ URL \"{}\" is updated to \"{}\"", vanityBaseUrl, sb.toString());
			return sb.toString();
		}
		return vanityBaseUrl;
	}

	private static String getExternalEndPoint(SanitizedInstanceInfo instance)
	{
		if (instance == null) {
			return null;
		}
		String endPoint = null;
		List<String> endpoints = new ArrayList<String>();
		// virtual end points contains the URLs to the service that may be reached from outside the cloud
		List<String> virtualEndpoints = instance.getVirtualEndpoints();
		endpoints.addAll(virtualEndpoints);
		List<String> canonicalEndpoints = instance.getCanonicalEndpoints();
		endpoints.addAll(canonicalEndpoints);
		if (!endpoints.isEmpty()) {
			for (String ep : endpoints) {
				if (ep.startsWith("https://")) {
					return ep;
				}
				if (endPoint == null) {
					endPoint = ep;
				}
			}
		}

		return endPoint;
	}

	private static String getHttpInternalEndPoint(InstanceInfo instance)
	{
		if (instance == null) {
			return null;
		}
		List<String> endpoints = new ArrayList<String>();
		/**
		 * canonicalEndpoints and virtualEndpoints are the service endpoints Canonical endpoints contains the URLs to the service
		 * that is reachable internally Virtual end points contains the URLs to the service that may be reached from outside the
		 * cloud
		 **/
		List<String> canonicalEndpoints = instance.getCanonicalEndpoints();
		endpoints.addAll(canonicalEndpoints);
		List<String> virtualEndpoints = instance.getVirtualEndpoints();
		endpoints.addAll(virtualEndpoints);
		if (!endpoints.isEmpty()) {
			for (String ep : endpoints) {
				if (ep.startsWith("http://")) {
					return ep;
				}
			}
		}

		return null;
	}

	private static String getInternalEndPoint(InstanceInfo instance)
	{
		if (instance == null) {
			return null;
		}
		String endPoint = null;
		List<String> endpoints = new ArrayList<String>();
		// virtual end points contains the URLs to the service that may be reached from outside the cloud
		List<String> virtualEndpoints = instance.getVirtualEndpoints();
		endpoints.addAll(virtualEndpoints);
		List<String> canonicalEndpoints = instance.getCanonicalEndpoints();
		endpoints.addAll(canonicalEndpoints);
		if (!endpoints.isEmpty()) {
			for (String ep : endpoints) {
				if (ep.startsWith("https://")) {
					return ep;
				}
				if (endPoint == null) {
					endPoint = ep;
				}
			}
		}

		return endPoint;
	}

	public static List<Link> getLinksWithRelPrefix(String relPrefix, SanitizedInstanceInfo instance)
	{
		List<Link> matched = new ArrayList<Link>();
		if (relPrefix != null) {
			for (Link link : instance.getLinks()) {
				if (link.getRel() != null ? link.getRel().startsWith(relPrefix) : "".startsWith(relPrefix)) {
					matched.add(link);
				}
			}
		}
		return matched;
	}

	public static EndpointEntity replaceWithVanityUrl(EndpointEntity ep, String tenantName, String serviceName)
	{
		if (ep == null || StringUtil.isEmpty(serviceName)) {
			return ep;
		}
		Map<String, String> vanityBaseUrls = RegistryLookupUtil.getVanityBaseURLs(tenantName);
		if (vanityBaseUrls != null && vanityBaseUrls.containsKey(serviceName)) {
			ep = RegistryLookupUtil.replaceVanityUrlDomainForEndpointEntity(vanityBaseUrls.get(serviceName), ep, tenantName);
			LOGGER.debug("#1.Completed to (try to) replace URL with vanity URL. Updated url is {}", ep.getHref());
		}
		return ep;
	}
	public static String replaceWithVanityUrl(String url, String tenantName, String serviceName)
	{
		if (url == null || StringUtil.isEmpty(serviceName)) {
			return url;
		}
		Map<String, String> vanityBaseUrls = RegistryLookupUtil.getVanityBaseURLs(tenantName);
		if (vanityBaseUrls != null && vanityBaseUrls.containsKey(serviceName)) {
			url = RegistryLookupUtil.replaceVanityUrlDomainForUrl(vanityBaseUrls.get(serviceName), url, tenantName);
			LOGGER.debug("#2. Completed to (try to) replace URL with vanity URL. Updated url is {}", url);
		}
		return url;
	}

	private static Link replaceVanityUrlDomainForLink(String domainPort, Link lk, String tenantName)
	{
		LOGGER.debug("/replaceDomainForLink/ Trying to replace link url \"{}\" with domain \"{}\"", lk != null ? lk.getHref()
				: null, domainPort);
		if (StringUtil.isEmpty(domainPort) || lk == null || StringUtil.isEmpty(lk.getHref())) {
			return lk;
		}
		String replacedHref = RegistryLookupUtil.replaceVanityUrlDomainForUrl(domainPort, lk.getHref(), tenantName);
		LOGGER.debug("/replaceDomainForLink/ Link \"{}\" URL (after replaced) is \"{}\"", lk.getHref(), replacedHref);
		lk.withHref(replacedHref);
		return lk;
	}

	public static Link replaceWithVanityUrl(Link lk, String tenantName, String serviceName)
	{
		if (lk == null || StringUtil.isEmpty(serviceName)) {
			return lk;
		}
		Map<String, String> vanityBaseUrls = RegistryLookupUtil.getVanityBaseURLs(tenantName);
		if (vanityBaseUrls != null && vanityBaseUrls.containsKey(serviceName)) {
			lk = RegistryLookupUtil.replaceVanityUrlDomainForLink(vanityBaseUrls.get(serviceName), lk, tenantName);
			LOGGER.debug("#3. Completed to (try to) replace URL with vanity URL. Updated url is {}", lk.getHref());
		}
		return lk;
	}


	private static InstanceInfo getInstanceInfo(String serviceName, String version) {
		InstanceInfo.Builder builder = InstanceInfo.Builder.newBuilder().withServiceName(serviceName);
		if (!StringUtil.isEmpty(version)) {
			builder = builder.withVersion(version);
		}
		return builder.build();
	}
	
    public static class VersionedLink extends Link {
		private String version;
    	@JsonIgnore
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

		public VersionedLink(Link link, String version, String authToken)
		{
			withHref(link.getHref());
			withOverrideTypes(link.getOverrideTypes());
			withRel(link.getRel());
			withTypesStr(link.getTypesStr());
			this.version = version;
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

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}
	}

	//Below method is not used by DF now(will be used by SSF)
	public static List<VersionedLink> getAllServicesInternalLinksByRel(String rel) throws IOException
	{
		LOGGER.debug("/getAllServicesInternalLinksByRel/ Trying to retrieve service internal link with rel: \"{}\"", rel);
		//.initComponent() reads the default "looup-client.properties" file in class path
		//.initComponent(List<String> urls) can override the default Registry urls with a list of urls
		if (LookupManager.getInstance().getLookupClient() == null) {
			// making sure the initComponent is only called once during the client lifecycle
			LookupManager.getInstance().initComponent();
		}
		List<InstanceInfo> instanceList = LookupManager.getInstance().getLookupClient().getInstancesWithLinkRelPrefix(rel,
				HTTP);
		if (instanceList == null) {
			LOGGER.warn("Found no instances with specified http rel {}", rel);
			return Collections.emptyList();
		}
		Map<String, VersionedLink> serviceLinksMap = new HashMap<String, VersionedLink>();
		for (InstanceInfo ii : instanceList) {
			List<Link> links = null;
			try {
				links = ii.getLinksWithRelPrefix(rel);
				if (links == null || links.isEmpty()) {
					LOGGER.warn("Found no links for InstanceInfo for service {}", ii.getServiceName());
					continue;
				}
				LOGGER.debug("Retrieved {} links for service {}. Links list: {}", links == null ? 0 : links.size(),
						ii.getServiceName(), StringUtil.arrayToCommaDelimitedString(links.toArray()));
				for (Link link : links) {
					if (link.getHref().startsWith("http://")) {
						serviceLinksMap.put(ii.getServiceName(), new VersionedLink(links.get(0), getAuthorizationAccessToken(ii)));
					}
				}
			}
			catch (Exception e) {
				LOGGER.error("Error to get links!", e);
			}
		}
		if (serviceLinksMap.isEmpty()) {
			LOGGER.warn("Found no internal widget notification links for rel {}", rel);
			return Collections.emptyList();
		}
		else {
			LOGGER.info("Widget notification links: {}", serviceLinksMap);
			return new ArrayList<VersionedLink>(serviceLinksMap.values());
		}
	}


}
