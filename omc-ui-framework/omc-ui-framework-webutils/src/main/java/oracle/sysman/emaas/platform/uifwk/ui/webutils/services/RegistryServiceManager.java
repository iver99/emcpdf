/*
 * Copyright (C) 2014 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.webutils.services;

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
import oracle.sysman.emaas.platform.uifwk.ui.webutils.wls.lifecycle.AbstractApplicationLifecycleService;
import oracle.sysman.emaas.platform.uifwk.ui.webutils.wls.lifecycle.ApplicationServiceManager;

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

	private static final String NAV_BASE = "/emsaasui/uifwk";

	private static final String NAV_BASE_HOME = "/emsaasui/uifwk/test.html";
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

	private static final Logger LOGGER = LogManager.getLogger(AbstractApplicationLifecycleService.APPLICATION_LOGGER_SUBSYSTEM
			+ ".serviceregistry");

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
	 * Update dashboards common UI service status to out of service on service manager
	 */
	public void markOutOfService(List<InstanceInfo> services, List<NonServiceResource> resources, List<String> otherReasons)
	{
		RegistrationManager.getInstance().getRegistrationClient().outOfServiceCausedBy(services, resources, otherReasons);
		//		RegistrationManager.getInstance().getRegistrationClient().updateStatus(InstanceStatus.OUT_OF_SERVICE);
	}

	/**
	 * Update dashboards common UI service status to up on service manager
	 */
	public void markServiceUp()
	{
		RegistrationManager.getInstance().getRegistrationClient().updateStatus(InstanceStatus.UP);
	}

	@Override
	public void postStart(ApplicationLifecycleEvent evt) throws Exception
	{
		if (new java.io.File("/var/opt/ORCLemaas/DEVELOPER_MODE-DISABLE_SM_REGISTRY").exists()) {
			LOGGER.info("In DEVMODE :: Post-starting DO NOT register application service with 'Service Registry'");
			return;
		}
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
		if (new java.io.File("/var/opt/ORCLemaas/DEVELOPER_MODE-DISABLE_SM_REGISTRY").exists()) {
			LOGGER.info("In DEVMODE :: Pre-stopping unregister NOT needed with 'Service Registry'");
			return;
		}
		LOGGER.info("Pre-stopping 'Service Registry' application service");
		RegistrationManager.getInstance().getRegistrationClient().shutdown();
		LOGGER.debug("Pre-stopped 'Service Regsitry'");
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
			//				LOGGER.error("Failed to found registryService. OMC-UI-Framework registration is not complete.");
			//				return false;
			//			}

			ServiceConfigBuilder builder = new ServiceConfigBuilder();
			builder.serviceName(serviceProps.getProperty("serviceName")).version(serviceProps.getProperty("version"))
			.characteristics(serviceProps.getProperty("characteristics"));
			StringBuilder virtualEndPoints = new StringBuilder();
			StringBuilder canonicalEndPoints = new StringBuilder();
			if (applicationUrlHttp != null) {
				virtualEndPoints.append(applicationUrlHttp + NAV_BASE);
				canonicalEndPoints.append(applicationUrlHttp + NAV_BASE);
			}
			if (applicationUrlHttps != null) {
				if (virtualEndPoints.length() > 0) {
					virtualEndPoints.append(",");
					canonicalEndPoints.append(",");
				}
				virtualEndPoints.append(applicationUrlHttps + NAV_BASE);
				canonicalEndPoints.append(applicationUrlHttps + NAV_BASE);
			}

			builder.virtualEndpoints(virtualEndPoints.toString()).canonicalEndpoints(canonicalEndPoints.toString());
			builder.registryUrls(serviceProps.getProperty("registryUrls")).loadScore(0.9)
			.leaseRenewalInterval(3000, TimeUnit.SECONDS).serviceUrls(serviceProps.getProperty("serviceUrls"));

			LOGGER.info("Initializing RegistrationManager");
			RegistrationManager.getInstance().initComponent(builder.build());

			Properties uifwkProps = PropertyReader.loadProperty(PropertyReader.UIFWK_CONFIG);
			final String buildId = uifwkProps.getProperty("buildId");
			final String sdkRel = "versionLookupSDK/emsaasui/uifwk/js/uifwk-partition";
			final String sdkImplUrl = NAV_BASE + "/" + buildId + "/js/uifwk-impl-partition-cached";
			if (buildId == null) {
				LOGGER.error("buildId is null, please check /opt/ORCLemaas/Applications/OMC-UI-Framework/init/servicemanager.properties file!");
			}
			else {
				LOGGER.info("buildId is " + buildId);
			}

			List<Link> links = new ArrayList<Link>();
			if (applicationUrlHttp != null) {
				if (buildId != null) {
					links.add(new Link().withRel(sdkRel).withHref(applicationUrlHttp + sdkImplUrl));
				}
				links.add(new Link().withRel("test").withHref(applicationUrlHttp + NAV_BASE_HOME));
			}
			if (applicationUrlHttps != null) {
				if (buildId != null) {
					links.add(new Link().withRel(sdkRel).withHref(applicationUrlHttps + sdkImplUrl));
				}
				links.add(new Link().withRel("test").withHref(applicationUrlHttps + NAV_BASE_HOME));
			}

			InfoManager.getInstance().getInfo().setLinks(links);

			LOGGER.info("Registering service with 'Service Registry'");
			RegistrationManager.getInstance().getRegistrationClient().register();
			RegistrationManager.getInstance().getRegistrationClient().updateStatus(InstanceStatus.UP);

			setRegistrationComplete(Boolean.TRUE);
			LOGGER.info("Service manager is up. Completed OMC-UI-Framework registration");
		}
		catch (Exception e) {
			setRegistrationComplete(Boolean.FALSE);
			LOGGER.error("Errors occurrs in registration. Service manager might be down. OMC-UI-Framework registration is not complete.");
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
