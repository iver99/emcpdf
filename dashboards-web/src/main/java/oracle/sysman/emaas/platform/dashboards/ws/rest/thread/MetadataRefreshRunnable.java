/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.ws.rest.thread;

import java.util.List;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.SanitizedInstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author reliang
 *
 */
public abstract class MetadataRefreshRunnable implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(MetadataRefreshRunnable.class);
    protected static final int MAX_LOOP_NUM = 10;  // how many times to try to fetch the metadata
    protected static final int INTERVAL_TIME = 60 * 1000;  // how often(seconds) to try to fetch the metadata
	
    protected String serviceName;
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    protected static boolean isServiceAvailable(String serviceName, String version) {
        String url = getSanitizedServiceUrl(serviceName, version, null);
        return !url.isEmpty();
    }
    
    /**
     * Get service end point for external use. Pass a null for tenantId if it does not matter which tenant the service is for.
     * 
     * @param serviceName
     * @param version
     * @param link
     * @param tenantId
     * @return
     */
    private static String getSanitizedServiceUrl(String serviceName, String version, String tenantId) {
        InstanceInfo queryInfo = InstanceInfo.Builder.newBuilder().withServiceName(serviceName).withVersion(version).build();

        SanitizedInstanceInfo sanitizedInstance = null;
        String HINT = ".  OMC may be in bootstrap, or the service is currently not available.";
        try {
            InstanceInfo instance = LookupManager.getInstance().getLookupClient().getInstance(queryInfo);
            if (instance == null) {
                LOGGER.warn("Service instance is null for " + serviceName + " " + version + " " + tenantId + HINT);
                return "";
            }
            if (tenantId == null) {
                // Get available instance for any tenant
                sanitizedInstance = LookupManager.getInstance().getLookupClient().getSanitizedInstanceInfo(instance);
            } else {
                // Get available instance for a particular tenant
                sanitizedInstance = LookupManager.getInstance().getLookupClient().getSanitizedInstanceInfo(instance, tenantId);
            }
            if (sanitizedInstance == null) {
                LOGGER.warn("Sanitized service instances is null for " + serviceName + " " + version + " " + tenantId + HINT);
                return "";
            }
        } catch (Exception e) {
            LOGGER.warn("Getting service instance " + serviceName + " " + version + " " + tenantId + " failed" + HINT, e);
            return "";
        }

        List<Link> linkList = sanitizedInstance.getLinks("sso.endpoint/virtual");
        if (linkList == null || linkList.isEmpty()) {
            LOGGER.warn("Sanitized service instance has no link for " + serviceName + " " + version + " " + tenantId + HINT);
            return "";
        }
        String returnValue = linkList.get(0).getHref();
        return returnValue;
    }
}
