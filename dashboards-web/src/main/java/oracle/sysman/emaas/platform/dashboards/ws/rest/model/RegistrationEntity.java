/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.SanitizedInstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.metadata.ApplicationEditionConverter.ApplicationOPCName;
import oracle.sysman.emaas.platform.dashboards.core.cache.CacheManager;
import oracle.sysman.emaas.platform.dashboards.core.cache.ICacheFetchFactory;
import oracle.sysman.emaas.platform.dashboards.core.cache.Tenant;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.StringUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;

/**
 * @author miao
 */
public class RegistrationEntity implements Serializable
{
	private static final long serialVersionUID = 7632586542760891331L;

	private static final Logger logger = LogManager.getLogger(RegistrationEntity.class);

	public static final String NAME_REGISTRYUTILS = "registryUrls";
	public static final String NAME_SSF_SERVICENAME = "SavedSearch";
	public static final String NAME_SSF_VERSION = "1.0+";
	public static final String NAME_DASHBOARD_API_SERVICENAME = "Dashboard-API";
	public static final String NAME_DASHBOARD_API_VERSION = "1.0+";
	public static final String NAME_QUICK_LINK = "quickLink";
	public static final String NAME_HOME_LINK = "homeLink";
	public static final String NAME_VISUAL_ANALYZER = "visualAnalyzer";
	public static final String NAME_ADMIN_LINK = "administration";
	public static final String NAME_DASHBOARD_UI_SERVICENAME = "Dashboard-UI";
	public static final String NAME_DASHBOARD_UI_VERSION = "1.0+";
	public static final String NAME_REGISTRY_SERVICENAME = "RegistryService";
	public static final String NAME_REGISTRY_VERSION = "1.0+";
	public static final String NAME_REGISTRY_REL_SSO = "sso.endpoint/virtual";

	public static final String APM_SERVICENAME = "ApmUI";
	public static final String APM_VERSION = "1.0+";
	public static final String APM_HOME_LINK = "sso.home";
	//	public static final String APM_URL = "/emsaasui/apmUi/index.html";
	public static final String LA_SERVICENAME = "LoganService";
	public static final String LA_VERSION = "1.0+";
	public static final String LA_HOME_LINK = "sso.search";
	//no home link is needed
	//	public static final String LA_URL = "/emsaasui/emlacore/html/log-analytics-search.html";
	public static final String ITA_SERVICENAME = "EmcitasApplications";
	public static final String ITA_VERSION = "1.0+";
	public static final String ITA_URL = "/emsaasui/emcpdfui/home.html?filter=ita";
	public static final String TA_SERVICENAME = "TargetAnalytics";
	//	public static final String TA_URL = "/emsaasui/emcta/ta/analytics.html";
	public static final String TMUI_SERVICENAME = "TenantManagementUI";
	public static final String EVENTUI_SERVICENAME = "EventUI";

	private static final Logger _logger = LogManager.getLogger(RegistrationEntity.class);
	//	private String registryUrls;

	static boolean successfullyInitialized = false;

	static {
		try {
			//.initComponent() reads the default "looup-client.properties" file in class path
			//.initComponent(List<String> urls) can override the default Registry urls with a list of urls
			if (LookupManager.getInstance().getLookupClient() == null) {
				// to ensure initComponent is only called once during the entire lifecycle
				LookupManager.getInstance().initComponent();
			}
			successfullyInitialized = true;
		}
		catch (Exception exception) {
			//			exception.printStackTrace();
			_logger.error("Failed to initialize Lookup Manager", exception);
		}
	}

	private String sessionExpirationTime = null;

	//Default constructor
	public RegistrationEntity()
	{

	}

	//Constructor with session expiration time
	public RegistrationEntity(String sessionExpirationTime)
	{
		this.sessionExpirationTime = sessionExpirationTime;
	}

	/**
	 * @return Administration links discovered from service manager
	 */
	@SuppressWarnings("unchecked")
	public List<LinkEntity> getAdminLinks()
	{
		Tenant cacheTenant = new Tenant(TenantContext.getCurrentTenant());
		try {
			return (List<LinkEntity>) CacheManager.getInstance().getCacheable(cacheTenant, CacheManager.CACHES_LOOKUP_CACHE,
					CacheManager.LOOKUP_CACHE_KEY_ADMIN_LINKS, new ICacheFetchFactory() {
						@Override
						public Object fetchCachable(Object key) throws Exception
						{
							return lookupLinksWithRelPrefix(NAME_ADMIN_LINK, true);
						}
					});
		}
		catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * @return the authorizationHeader
	 */
	//	public String getAuthToken()
	//	{
	//		return new String(LookupManager.getInstance().getAuthorizationToken());
	//	}

	@SuppressWarnings("unchecked")
	public List<LinkEntity> getCloudServices()
	{
		String tenantName = TenantContext.getCurrentTenant();
		Tenant cacheTenant = new Tenant(tenantName);
		List<LinkEntity> list = null;
		try {
			list = (List<LinkEntity>) CacheManager.getInstance().getCacheable(cacheTenant, CacheManager.CACHES_LOOKUP_CACHE,
					CacheManager.LOOKUP_CACHE_KEY_CLOUD_SERVICE_LINKS);
			if (list != null) {
				return list;
			}
		}
		catch (Exception e) {
			logger.error(e);
		}
		list = new ArrayList<LinkEntity>();
		Set<String> subscribedApps = getTenantSubscribedApplicationSet(false);
		for (String app : subscribedApps) {
			try {
				if (APM_SERVICENAME.equals(app)) {
					Link l = RegistryLookupUtil.getServiceExternalLink(APM_SERVICENAME, APM_VERSION, APM_HOME_LINK, tenantName);
					if (l == null) {
						throw new Exception("Link for " + app + "return null");
					}
					LinkEntity le = new LinkEntity(ApplicationOPCName.APM.toString(), l.getHref(), APM_SERVICENAME, APM_VERSION);
					le = replaceWithVanityUrl(le, tenantName, APM_SERVICENAME);
					list.add(le);
				}
				else if (LA_SERVICENAME.equals(app)) {
					Link l = RegistryLookupUtil.getServiceExternalLink(LA_SERVICENAME, LA_VERSION, LA_HOME_LINK, tenantName);
					if (l == null) {
						throw new Exception("Link for " + app + "return null");
					}
					LinkEntity le = new LinkEntity(ApplicationOPCName.LogAnalytics.toString(), l.getHref(), LA_SERVICENAME,
							LA_VERSION);
					le = replaceWithVanityUrl(le, tenantName, LA_SERVICENAME);
					list.add(le);
				}
				else if (ITA_SERVICENAME.equals(app)) {
					list.add(new LinkEntity(ApplicationOPCName.ITAnalytics.toString(), ITA_URL, ITA_SERVICENAME, ITA_VERSION)); //version is hard coded now

				}
			}
			catch (Exception e) {
				_logger.error("Failed to discover link of cloud service: " + app, e);
			}
		}
		CacheManager.getInstance().putCacheable(cacheTenant, CacheManager.CACHES_LOOKUP_CACHE,
				CacheManager.LOOKUP_CACHE_KEY_CLOUD_SERVICE_LINKS, list);
		return list;
	}

	//	private String ssfServiceName;
	//	private String ssfVersion;

	//	public RegistrationEntity(String regValue, String ssfServiceName, String ssfVersion)
	//	{
	//		setRegistryUrls(regValue);
	//		setSsfServiceName(ssfServiceName);
	//		setSsfVersion(ssfVersion);
	//	}

	//	public RegistrationEntity(String regValue)
	//	{
	//		setRegistryUrls(regValue);
	//	}

	/**
	 * @return Home links discovered from service manager
	 */
	@SuppressWarnings("unchecked")
	public List<LinkEntity> getHomeLinks()
	{
		Tenant cacheTenant = new Tenant(TenantContext.getCurrentTenant());
		try {
			return (List<LinkEntity>) CacheManager.getInstance().getCacheable(cacheTenant, CacheManager.CACHES_LOOKUP_CACHE,
					CacheManager.LOOKUP_CACHE_KEY_HOME_LINKS, new ICacheFetchFactory() {
						@Override
						public Object fetchCachable(Object key) throws Exception
						{
							return lookupLinksWithRelPrefix(NAME_HOME_LINK);
						}
					});
		}
		catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public String getSessionExpiryTime()
	{
		return sessionExpirationTime;
	}

	/**
	 * @return the rest API end point for dashboard framework
	 * @throws Exception
	 */
	//	public String getDfRestApiEndPoint()
	//	{
	//		EndpointEntity entity = RegistryLookupUtil.getServiceExternalEndPoint(NAME_DASHBOARD_API_SERVICENAME,
	//				NAME_DASHBOARD_API_VERSION, TenantContext.getCurrentTenant());
	//		return entity != null ? entity.getHref() : null;
	//	}

	/**
	 * @return the registryUrl
	 */
	//	public String getRegistryUrl()
	//	{
	//		Link link = RegistryLookupUtil.getServiceExternalLink(NAME_REGISTRY_SERVICENAME, NAME_REGISTRY_VERSION,
	//				NAME_REGISTRY_REL_SSO);
	//		return link != null ? link.getHref() : null;
	//	}

	//	/*
	//	 * @return Quick links discovered from service manager
	//	 */
	//	public List<LinkEntity> getQuickLinks()
	//	{
	//		return lookupLinksWithRelPrefix(NAME_QUICK_LINK);
	//	}

	//	/**
	//	 * @return the ssfServiceName
	//	 */
	//	public String getSsfServiceName()
	//	{
	//		return ssfServiceName;
	//	}
	//
	//	/**
	//	 * @return the ssfVersion
	//	 */
	//	public String getSsfVersion()
	//	{
	//		return ssfVersion;
	//	}

	//	/**
	//	 * @return the rest API end point for SSF
	//	 * @throws Exception
	//	 */
	//	public String getSsfRestApiEndPoint() throws Exception
	//	{
	//		EndpointEntity entity = RegistryLookupUtil.getServiceExternalEndPoint(NAME_SSF_SERVICENAME, NAME_SSF_VERSION,
	//				TenantContext.getCurrentTenant());
	//		return entity != null ? entity.getHref() : null;
	//		//		if (true) {
	//		//			return "https://slc07hcn.us.oracle.com:4443/microservice/2875e44b-1a71-4bf2-9544-82ddc3b2d486";
	//		//		}
	//	}

	/**
	 * @return Visual analyzer links discovered from service manager
	 */
	@SuppressWarnings("unchecked")
	public List<LinkEntity> getVisualAnalyzers()
	{
		Tenant cacheTenant = new Tenant(TenantContext.getCurrentTenant());
		try {
			return (List<LinkEntity>) CacheManager.getInstance().getCacheable(cacheTenant, CacheManager.CACHES_LOOKUP_CACHE,
					CacheManager.LOOKUP_CACHE_KEY_VISUAL_ANALYZER, new ICacheFetchFactory() {
						@Override
						public Object fetchCachable(Object key) throws Exception
						{
							return lookupLinksWithRelPrefix(NAME_VISUAL_ANALYZER);
						}
					});
		}
		catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	private void addToLinksMap(Map<String, LinkEntity> linksMap, List<Link> links, String serviceName, String version)
	{
		String tenantName = TenantContext.getCurrentTenant();
		for (Link link : links) {
			String key = serviceName + "_" + version + "_" + link.getRel();
			if (!linksMap.containsKey(key)) {
				LinkEntity le = new LinkEntity(getLinkName(link.getRel()), link.getHref(), serviceName, version);
				le = replaceWithVanityUrl(le, tenantName, serviceName);
				linksMap.put(key, le);
			}
			else if (linksMap.get(key).getHref().toLowerCase().startsWith("http://")
					&& link.getHref().toLowerCase().startsWith("https://")) {
				LinkEntity le = new LinkEntity(getLinkName(link.getRel()), link.getHref(), serviceName, version);
				le = replaceWithVanityUrl(le, tenantName, serviceName);
				linksMap.put(key, le);
			}
		}
	}

	//	/**
	//	 * @param ssfServiceName
	//	 *            the ssfServiceName to set
	//	 */
	//	public void setSsfServiceName(String ssfServiceName)
	//	{
	//		this.ssfServiceName = ssfServiceName;
	//	}
	//
	//	/**
	//	 * @param ssfVersion
	//	 *            the ssfVersion to set
	//	 */
	//	public void setSsfVersion(String ssfVersion)
	//	{
	//		this.ssfVersion = ssfVersion;
	//	}
	//	/**
	//	 * @param registryUrls
	//	 *            the registryUrls to set
	//	 */
	//	public void setRegistryUrls(String registryUrls)
	//	{
	//		this.registryUrls = registryUrls;
	//	}
	private String getLinkName(String rel)
	{
		String name = "";
		if (rel.indexOf("/") > 0) {
			String[] relArray = rel.split("/");
			name = relArray[1];
		}

		return name;
	}

	/**
	 * This method returns a set of SM(service manager) services names represents the subscribed services for specified tenant
	 *
	 * @param isAdmin
	 * @return
	 */
	private Set<String> getTenantSubscribedApplicationSet(boolean isAdmin)
	{
		String tenantName = TenantContext.getCurrentTenant();
		Set<String> appSet = new HashSet<String>();
		if (StringUtil.isEmpty(tenantName)) {
			return appSet;
		}
		List<String> apps = TenantSubscriptionUtil.getTenantSubscribedServices(tenantName);
		if (apps == null || apps.isEmpty()) {
			return appSet;
		}
		for (String app : apps) {
			if (ApplicationOPCName.APM.toString().equals(app)) {
				appSet.add(APM_SERVICENAME);
			}
			else if (ApplicationOPCName.ITAnalytics.toString().equals(app)) {
				appSet.add(ITA_SERVICENAME);
				appSet.add(TA_SERVICENAME);
			}
			else if (ApplicationOPCName.LogAnalytics.toString().equals(app)) {
				appSet.add(LA_SERVICENAME);
			}
		}
		//if any of APM/LA/TA is subscribed, TenantManagementUI/EventUI should be subscribed accordingly as agreement now
		if (appSet.size() > 0) {
			if (isAdmin) {
				appSet.add(TMUI_SERVICENAME);
			}
			appSet.add(EVENTUI_SERVICENAME);
		}
		return appSet;
	}

	private List<LinkEntity> lookupLinksWithRelPrefix(String linkPrefix)
	{
		return lookupLinksWithRelPrefix(linkPrefix, false);
	}

	private List<LinkEntity> lookupLinksWithRelPrefix(String linkPrefix, boolean isAdminLink)
	{
		_logger.info("lookupLinksWithRelPrefix(" + linkPrefix + "," + isAdminLink + ")");
		List<LinkEntity> linkList = new ArrayList<LinkEntity>();

		LookupClient lookUpClient = LookupManager.getInstance().getLookupClient();
		List<InstanceInfo> instanceList = lookUpClient.getInstancesWithLinkRelPrefix(linkPrefix);

		Set<String> subscribedApps = getTenantSubscribedApplicationSet(isAdminLink);
		_logger.info("Got Subscribed applications:", subscribedApps != null ? subscribedApps.toString() : "null");
		Map<String, LinkEntity> linksMap = new HashMap<String, LinkEntity>();
		Map<String, LinkEntity> dashboardLinksMap = new HashMap<String, LinkEntity>();
		String tenantName = TenantContext.getCurrentTenant();
		for (InstanceInfo internalInstance : instanceList) {
			List<Link> links = internalInstance.getLinksWithRelPrefix(linkPrefix);
			try {
				SanitizedInstanceInfo sanitizedInstance = null;
				if (!StringUtil.isEmpty(tenantName)) {
					sanitizedInstance = LookupManager.getInstance().getLookupClient().getSanitizedInstanceInfo(internalInstance,
							tenantName);
					logger.debug("Retrieved sanitizedInstance {} by using getSanitizedInstanceInfo for tenant {}",
							sanitizedInstance, tenantName);
				}
				else {
					sanitizedInstance = LookupManager.getInstance().getLookupClient().getSanitizedInstanceInfo(internalInstance);
				}
				if (sanitizedInstance != null) {
					links = RegistryLookupUtil.getLinksWithRelPrefix(linkPrefix, sanitizedInstance);
				}
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				//				e.printStackTrace();
				_logger.error("Error to get SanitizedInstanceInfo", e);
			}
			if (NAME_DASHBOARD_UI_SERVICENAME.equals(internalInstance.getServiceName())
					&& NAME_DASHBOARD_UI_VERSION.equals(internalInstance.getVersion())) {
				addToLinksMap(dashboardLinksMap, links, internalInstance.getServiceName(), internalInstance.getVersion());
			}
			else if (subscribedApps != null && subscribedApps.contains(internalInstance.getServiceName())) {
				addToLinksMap(linksMap, links, internalInstance.getServiceName(), internalInstance.getVersion());
			}

		}
		_logger.info("dashboardLinksMap: " + dashboardLinksMap);
		Iterator<Map.Entry<String, LinkEntity>> iterDashboardLinks = dashboardLinksMap.entrySet().iterator();
		while (iterDashboardLinks.hasNext()) {
			Map.Entry<String, LinkEntity> entry = iterDashboardLinks.next();
			LinkEntity val = entry.getValue();
			val = replaceWithVanityUrl(val, tenantName, val.getServiceName());
			linkList.add(val);
		}

		_logger.info("linksMap: " + dashboardLinksMap);
		Iterator<Map.Entry<String, LinkEntity>> iterLinks = linksMap.entrySet().iterator();
		while (iterLinks.hasNext()) {
			Map.Entry<String, LinkEntity> entry = iterLinks.next();
			LinkEntity val = entry.getValue();
			_logger.debug("Retrieved link for RegistrationEntity from linksMap. service name is {}, and href is {}",
					val.getServiceName(), val.getHref());
			if (!dashboardLinksMap.containsKey(entry.getKey())) {
				val = replaceWithVanityUrl(val, tenantName, val.getServiceName());
				linkList.add(val);
			}
		}
		_logger.info("Got links matching prefix:" + linkPrefix, linkList.toString());
		return linkList;
	}

	private LinkEntity replaceWithVanityUrl(LinkEntity lk, String tenantName, String serviceName)
	{
		String href = RegistryLookupUtil.replaceWithVanityUrl(lk.getHref(), tenantName, serviceName);
		lk.setHref(href);
		return lk;
	}
}
