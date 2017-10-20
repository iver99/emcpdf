/*
 * Copyright (C) 2014 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.webutils.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;

import oracle.sysman.emSDK.emaas.platform.tenantmanager.TenantInfoClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InfoManager;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo.InstanceStatus;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.NonServiceResource;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationManager;
import oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.AbstractApplicationLifecycleService;
import oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.lru.LRUCacheManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import weblogic.application.ApplicationLifecycleEvent;

public class RegistryServiceManager implements ApplicationServiceManager
{
	interface Builder
	{
		Properties build();
	}

	class ServiceConfigBuilder implements Builder
	{
		private final Map<String, String> serviceConfigMap = new HashMap<String, String>();

		@Override
		public Properties build()
		{
			Properties propSaver = new Properties();
			propSaver.putAll(serviceConfigMap);
			return propSaver;
		}

		/**
		 * @param canonicalEndpoints
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder canonicalEndpoints(String canonicalEndpoints)
		{
			serviceConfigMap.put("canonicalEndpoints", canonicalEndpoints);
			return this;
		}

		/**
		 * @param characteristics
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder characteristics(String characteristics)
		{
			if (characteristics != null) {
				serviceConfigMap.put("characteristics", characteristics);
			}
			return this;
		}

		/**
		 * @param controlledDataTypes
		 *            example "LogFile, Target Delta"
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder controlledDatatypes(String controlledDataTypes)
		{
			serviceConfigMap.put("controlledDataTypes", controlledDataTypes);
			return this;
		}

		/**
		 * @param dataTypes
		 *            example "LogFile"
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder datatypes(String dataTypes)
		{
			serviceConfigMap.put("dataTypes", dataTypes);
			return this;
		}

		/**
		 * @param leaseRenewalInterval
		 *            example 30000
		 * @param timeUnit
		 *            TimeUnit.SECONDS
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder leaseRenewalInterval(long leaseRenewalInterval, TimeUnit timeUnit)
		{
			long leaseRenewalIntervalInSecs = TimeUnit.SECONDS.convert(leaseRenewalInterval, timeUnit);
			serviceConfigMap.put("leaseInfo.renewalIntervalInSecs", String.valueOf(leaseRenewalIntervalInSecs));
			return this;
		}

		/**
		 * @param loadScore
		 *            example "0.9"
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder loadScore(double loadScore)
		{
			serviceConfigMap.put("loadScore", String.valueOf(loadScore));
			return this;
		}

		/**
		 * @param registryUrls
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder registryUrls(String registryUrls)
		{
			serviceConfigMap.put("registryUrls", registryUrls);
			return this;
		}

		/**
		 * @param serviceName
		 *            example "DataReceiver.storage"
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder serviceName(String serviceName)
		{
			serviceConfigMap.put("serviceName", serviceName);
			return this;
		}

		/**
		 * @param serviceUrls
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder serviceUrls(String serviceUrls)
		{
			serviceConfigMap.put("serviceUrls", serviceUrls);
			return this;
		}

		/**
		 * @param supportedTargetTypes
		 *            example "LogFile, Target Delta"
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder supportedTargetTypes(String supportedTargetTypes)
		{
			serviceConfigMap.put("supportedTargetTypes", supportedTargetTypes);
			return this;
		}

		/**
		 * @param version
		 *            example "1.0"
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder version(String version)
		{
			serviceConfigMap.put("version", version);
			return this;
		}

		/**
		 * @param virtualEndpoints
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder virtualEndpoints(String virtualEndpoints)
		{
			serviceConfigMap.put("virtualEndpoints", virtualEndpoints);
			return this;
		}
	}

	enum UrlType
	{
		HTTP, HTTPS
	}

	//	private static final String NAV_CONTEXT_ROOT = "/emcpdf";
	private static final String NAV_API_BASE = "/emcpdf/api/v1/";
	private static final String NAV_STATIC_DASHBOARDS = NAV_API_BASE + "dashboards";
	private static final String NAV_STATIC_PREFERENCE = NAV_API_BASE + "preferences";
	private static final String NAV_STATIC_SUBSCRIBEDAPPS = NAV_API_BASE + "subscribedapps";
	private static final String NAV_STATIC_SUBSCRIBEDAPPS_V2 = NAV_API_BASE + "subscribedapps2";
	private static final String NAV_STATIC_LOGGING = NAV_API_BASE + "logging";
	private static final String NAV_STATIC_REGISTRY = NAV_API_BASE + "registry";
	private static final String NAV_STATIC_CONFIGURATIONS = NAV_API_BASE + "configurations";
	private static final String NAV_LOGGING_CONFIG = NAV_API_BASE + "_logging/configs";
	private static final String NAV_ZDT_COUNTS = NAV_API_BASE + "zdt/counts";
	private static final String NAV_ZDT_TABLEROWS = NAV_API_BASE + "zdt/tablerows";
	private static final String NAV_ZDT_SYNC = NAV_API_BASE + "zdt/sync";
	private static final String NAV_ZDT_TENANTS = NAV_API_BASE + "zdt/tenants";
	
	private static final String NAV_ZDT_SYNC_STATUS = NAV_API_BASE + "zdt/sync/status";
	private static final String NAV_ZDT_COMPARE_STATUS = NAV_API_BASE + "zdt/compare/status";
	private static final String NAV_ZDT_COMPARE_RESULT = NAV_API_BASE + "zdt/compare/result";
	
	private static final String NAV_STATIC_OMCSTATUS = NAV_API_BASE + "omcstatus";
	private static final String NAV_WIDGET_NOTIFY = NAV_API_BASE + "widgetnotification";
	private static final String NAV_SSF_LIFECYCLE = NAV_API_BASE + "ssflifecycle.ntf";
	private static final String NAV_CACHE = NAV_API_BASE + "cache";
	private static final String NAV_REFRESH_OOB = NAV_API_BASE + "refresh/oob";
	private static final String NAV_REFRESH_NLS = NAV_API_BASE + "refresh/nls";
	private static final String NAV_REFRESH_WIDGET_CACHE = NAV_API_BASE + "refresh/widgetcache";
	private static final String NAV_TOOL = NAV_API_BASE + "tool";

	public static final ObjectName WLS_RUNTIME_SERVICE_NAME;

	static {
		try {
			WLS_RUNTIME_SERVICE_NAME = ObjectName
					.getInstance("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
		}
		catch (Exception e) {
			throw new Error("Well-known JMX names are corrupt - code bug", e);
		}
	}

	private static String getApplicationUrl(UrlType urlType) throws Exception
	{
		InitialContext ctx = new InitialContext();
		try {
			MBeanServer runtimeServer = (MBeanServer) ctx.lookup("java:comp/jmx/runtime");
			ObjectName serverRuntimeName = (ObjectName) runtimeServer.getAttribute(WLS_RUNTIME_SERVICE_NAME, "ServerRuntime");
			switch (urlType) {
				case HTTP:
					return (String) runtimeServer.invoke(serverRuntimeName, "getURL", new Object[] { "http" },
							new String[] { String.class.getName() });
				case HTTPS:
					return (String) runtimeServer.invoke(serverRuntimeName, "getURL", new Object[] { "https" },
							new String[] { String.class.getName() });
				default:
					throw new IllegalStateException("Unknown UrlType, ServerRuntime URL lookup failed");
			}
		}
		finally {
			ctx.close();
		}
	}

	private Boolean registrationComplete = null;

	private final static Logger LOGGER = LogManager
			.getLogger(AbstractApplicationLifecycleService.APPLICATION_LOGGER_SUBSYSTEM + ".serviceregistry");
	@Override
	public String getName()
	{
		return "Service Registry Service";
	}

	public Boolean isRegistrationComplete()
	{
		return registrationComplete;
	}

	/**
	 * Update dashboards service status to out of service on service manager
	 */
	public void markOutOfService(List<InstanceInfo> services, List<NonServiceResource> resources, List<String> otherReasons)
	{
		RegistrationManager.getInstance().getRegistrationClient().outOfServiceCausedBy(services, resources, otherReasons);
		//		RegistrationManager.getInstance().getRegistrationClient().updateStatus(InstanceStatus.OUT_OF_SERVICE);
	}

	/**
	 * Update dashboards service status to up on service manager
	 */
	public void markServiceUp()
	{
		RegistrationManager.getInstance().getRegistrationClient().updateStatus(InstanceStatus.UP);
	}

	@Override
	public void postStart(ApplicationLifecycleEvent evt) throws Exception
	{
		LOGGER.info("Post-starting 'Service Registry' application service");
		registerService();
	}

	@Override
	public void postStop(ApplicationLifecycleEvent evt) throws Exception
	{
		// do nothing
	}

	@Override
	public void preStart(ApplicationLifecycleEvent evt) throws Exception
	{
		// do nothing
	}

	@Override
	public void preStop(ApplicationLifecycleEvent evt) throws Exception
	{
		LOGGER.info("Pre-stopping 'Service Registry' application service");
		RegistrationManager.getInstance().getRegistrationClient().shutdown();
		LOGGER.debug("Pre-stopped 'Service Regsitry'");
		LOGGER.info("Pre-stopping cache");
		LRUCacheManager.getInstance().close();
		LOGGER.debug("Pre-stopped cache");
	}

	public boolean registerService()
	{
		try {
			LOGGER.info("Registering service...");
			String applicationUrlHttp = RegistryServiceManager.getApplicationUrl(UrlType.HTTP);
			LOGGER.debug("Application URL(http) to register with 'Service Registry': " + applicationUrlHttp);
			String applicationUrlHttps = RegistryServiceManager.getApplicationUrl(UrlType.HTTPS);
			LOGGER.debug("Application URL(https) to register with 'Service Registry': " + applicationUrlHttps);

			LOGGER.info("Building 'Service Registry' configuration");
			Properties serviceProps = PropertyReader.loadProperty(PropertyReader.SERVICE_PROPS);

			LOGGER.info("Initialize lookup manager");
			LookupManager.getInstance().initComponent(Arrays.asList(serviceProps.getProperty("serviceUrls")));
                        TenantInfoClient.getInstance().initComponent();

			//			LOGGER.info("Checking RegistryService");
			//			if (RegistryLookupUtil.getServiceInternalLink("RegistryService", "1.0+", "collection/instances", null) == null) {
			//				setRegistrationComplete(Boolean.FALSE);
			//				LOGGER.error("Failed to found registryService. Dashboard-API registration is not complete.");
			//				return false;
			//			}

			ServiceConfigBuilder builder = new ServiceConfigBuilder();
			builder.serviceName(serviceProps.getProperty("serviceName")).version(serviceProps.getProperty("version"))
			.characteristics(serviceProps.getProperty("characteristics"));
			StringBuilder virtualEndPoints = new StringBuilder();
			StringBuilder canonicalEndPoints = new StringBuilder();
			if (applicationUrlHttp != null) {
				virtualEndPoints.append(applicationUrlHttp + NAV_API_BASE);
				canonicalEndPoints.append(applicationUrlHttp + NAV_API_BASE);
			}
			if (applicationUrlHttps != null) {
				if (virtualEndPoints.length() > 0) {
					virtualEndPoints.append(",");
					canonicalEndPoints.append(",");
				}
				virtualEndPoints.append(applicationUrlHttps + NAV_API_BASE);
				canonicalEndPoints.append(applicationUrlHttps + NAV_API_BASE);
			}

			builder.virtualEndpoints(virtualEndPoints.toString()).canonicalEndpoints(canonicalEndPoints.toString());
			builder.registryUrls(serviceProps.getProperty("registryUrls")).loadScore(0.9)
			.leaseRenewalInterval(3000, TimeUnit.SECONDS).serviceUrls(serviceProps.getProperty("serviceUrls"));

			LOGGER.info("Initializing RegistrationManager");
			RegistrationManager.getInstance().initComponent(builder.build());

			List<Link> links = new ArrayList<Link>();
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("base").withHref(applicationUrlHttp + NAV_API_BASE));
			}
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("base").withHref(applicationUrlHttps + NAV_API_BASE));
			}
			// introduce static links
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("static/dashboards.service").withHref(applicationUrlHttp + NAV_STATIC_DASHBOARDS));
			}
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("static/dashboards.service").withHref(applicationUrlHttps + NAV_STATIC_DASHBOARDS));
			}
			//introduce serviceapi
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("serviceapi/dashboards.service").withHref(applicationUrlHttps + NAV_STATIC_DASHBOARDS));
			}
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("static/dashboards.preferences")
						.withHref(applicationUrlHttp + NAV_STATIC_PREFERENCE));
			}
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("static/dashboards.preferences").withHref(
						applicationUrlHttps + NAV_STATIC_PREFERENCE));
			}
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("static/dashboards.subscribedapps").withHref(
						applicationUrlHttp + NAV_STATIC_SUBSCRIBEDAPPS));
			}
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("static/dashboards.subscribedapps").withHref(
						applicationUrlHttps + NAV_STATIC_SUBSCRIBEDAPPS));
			}
			//introduce serviceapi
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("serviceapi/dashboards.subscribedapps").withHref(
						applicationUrlHttps + NAV_STATIC_SUBSCRIBEDAPPS));
			}
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("static/dashboards.subscribedapps2").withHref(
						applicationUrlHttp + NAV_STATIC_SUBSCRIBEDAPPS_V2));
			}
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("static/dashboards.subscribedapps2").withHref(
						applicationUrlHttps + NAV_STATIC_SUBSCRIBEDAPPS_V2));
			}
			//introduce serviceapi
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("serviceapi/dashboards.subscribedapps2").withHref(
						applicationUrlHttps + NAV_STATIC_SUBSCRIBEDAPPS_V2));
			}
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("static/dashboards.logging").withHref(applicationUrlHttp + NAV_STATIC_LOGGING));
			}
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("static/dashboards.logging").withHref(applicationUrlHttps + NAV_STATIC_LOGGING));
			}
			//introduce serviceapi
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("serviceapi/dashboards.logging").withHref(applicationUrlHttps + NAV_STATIC_LOGGING));
			}
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("static/dashboards.registry").withHref(applicationUrlHttp + NAV_STATIC_REGISTRY));
			}
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("static/dashboards.registry").withHref(applicationUrlHttps + NAV_STATIC_REGISTRY));
			}
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("static/dashboards.configurations").withHref(
						applicationUrlHttp + NAV_STATIC_CONFIGURATIONS));
			}
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("static/dashboards.configurations").withHref(
						applicationUrlHttps + NAV_STATIC_CONFIGURATIONS));
			}
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("static/dashboards.omcstatus").withHref(applicationUrlHttp + NAV_STATIC_OMCSTATUS));
			}
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("static/dashboards.omcstatus").withHref(applicationUrlHttps + NAV_STATIC_OMCSTATUS));
			}
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("log/configuration").withHref(applicationUrlHttp + NAV_LOGGING_CONFIG));
			}
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("log/configuration").withHref(applicationUrlHttps + NAV_LOGGING_CONFIG));
			}
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("zdt/counts").withHref(applicationUrlHttp + NAV_ZDT_COUNTS));
			}
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("zdt/counts").withHref(applicationUrlHttps + NAV_ZDT_COUNTS));
			}
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("zdt/tablerows").withHref(applicationUrlHttp + NAV_ZDT_TABLEROWS));
			}
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("zdt/tablerows").withHref(applicationUrlHttps + NAV_ZDT_TABLEROWS));
			}
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("zdt/sync").withHref(applicationUrlHttp + NAV_ZDT_SYNC));
			}
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("zdt/sync").withHref(applicationUrlHttps + NAV_ZDT_SYNC));
			}
			
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("zdt/sync/status").withHref(applicationUrlHttp + NAV_ZDT_SYNC_STATUS));
			}
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("zdt/sync/status").withHref(applicationUrlHttps + NAV_ZDT_SYNC_STATUS));
			}
				
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("zdt/compare/status").withHref(applicationUrlHttp + NAV_ZDT_COMPARE_STATUS));
			}
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("zdt/compare/status").withHref(applicationUrlHttps + NAV_ZDT_COMPARE_STATUS));
			}
			
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("zdt/compare/result").withHref(applicationUrlHttp + NAV_ZDT_COMPARE_RESULT));
			}
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("zdt/compare/result").withHref(applicationUrlHttps + NAV_ZDT_COMPARE_RESULT));
			}
			
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("zdt/tenants").withHref(applicationUrlHttp + NAV_ZDT_TENANTS));
			}
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("zdt/tenants").withHref(applicationUrlHttps + NAV_ZDT_TENANTS));
			}
			
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("ssf.widget.changed").withHref(applicationUrlHttp + NAV_WIDGET_NOTIFY));
			}
			if (applicationUrlHttps != null) {
				links.add(new Link().withRel("ssf.widget.changed").withHref(applicationUrlHttps + NAV_WIDGET_NOTIFY));
			}
			if (applicationUrlHttp != null) {
                links.add(new Link().withRel("ssf.lifecycle.notify").withHref(applicationUrlHttp + NAV_SSF_LIFECYCLE));
            }
            if (applicationUrlHttps != null) {
                links.add(new Link().withRel("ssf.lifecycle.notify").withHref(applicationUrlHttps + NAV_SSF_LIFECYCLE));
            }
			if (applicationUrlHttp != null) {
				links.add(new Link().withRel("dashboards.cache").withHref(applicationUrlHttp + NAV_CACHE));
			}
            if (applicationUrlHttp != null) {
                links.add(new Link().withRel("refresh/oob").withHref(applicationUrlHttp + NAV_REFRESH_OOB));
            }
            if (applicationUrlHttps != null) {
                links.add(new Link().withRel("refresh/oob").withHref(applicationUrlHttps + NAV_REFRESH_OOB));
            }
            if (applicationUrlHttp != null) {
                links.add(new Link().withRel("refresh/resource_bundle").withHref(applicationUrlHttp + NAV_REFRESH_NLS));
            }
            if (applicationUrlHttps != null) {
                links.add(new Link().withRel("refresh/resource_bundle").withHref(applicationUrlHttps + NAV_REFRESH_NLS));
            }
            if (applicationUrlHttp != null) {
                links.add(new Link().withRel("expire/widgetcache").withHref(applicationUrlHttp + NAV_REFRESH_WIDGET_CACHE));
            }
            if (applicationUrlHttps != null) {
                links.add(new Link().withRel("expire/widgetcache").withHref(applicationUrlHttps + NAV_REFRESH_WIDGET_CACHE));
            }
            if (applicationUrlHttp != null) {
                links.add(new Link().withRel("tool").withHref(applicationUrlHttp + NAV_TOOL));
            }

			InfoManager.getInstance().getInfo().setLinks(links);

			LOGGER.info("Registering service with 'Service Registry'");
			RegistrationManager.getInstance().getRegistrationClient().register();
			RegistrationManager.getInstance().getRegistrationClient().updateStatus(InstanceStatus.UP);
			//register OAuth ready status with Service Registry
			RegistrationManager.getInstance().getRegistrationClient().setOauthReady(true);

			setRegistrationComplete(Boolean.TRUE);
			LOGGER.info("Service manager is up. Completed dashboard-API registration");
		}
		catch (Exception e) {
			setRegistrationComplete(Boolean.FALSE);
			LOGGER.error(
					"Errors occurrs in registration. Service manager might be down. Dashboard-API registration is not complete.");
			LOGGER.error(e.getLocalizedMessage(), e);
			//			throw e;
		}

		//		LOGGER.info("Post-starting service, attempting to create entityimanager factory");
		//		PersistenceManager.getInstance().createEntityManagerFactory();
		//		LOGGER.info("Post-starting service, entityimanager factory created");
		return registrationComplete;
	}

	public void setRegistrationComplete(Boolean isComplete)
	{
		registrationComplete = isComplete;
	}
}
