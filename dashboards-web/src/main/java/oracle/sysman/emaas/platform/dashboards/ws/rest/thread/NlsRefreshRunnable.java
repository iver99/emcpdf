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

import java.util.ArrayList;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.CommonResourceException;
import oracle.sysman.emaas.platform.dashboards.entity.EmsResourceBundle;
import oracle.sysman.emaas.platform.dashboards.webutils.metadata.MetadataRetriever;
import oracle.sysman.emaas.platform.dashboards.webutils.metadata.MetadataStorer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author reliang
 *
 */
public class NlsRefreshRunnable extends MetadataRefreshRunnable {
    private static final Logger LOGGER = LogManager.getLogger(NlsRefreshRunnable.class);
    @Override
    public void run() {
        LOGGER.info("Starting a new thread for fresh {} resource bundles.", serviceName);
        if(serviceName == null || serviceName.isEmpty()) {
            LOGGER.error("NlsRefreshRunnable: there is no service name!");
            return;
        }
        List<EmsResourceBundle> rbList = new ArrayList<EmsResourceBundle>();
        MetadataRetriever nlsRetriever = new MetadataRetriever();
        try {
            rbList = nlsRetriever.getResourceBundleByService(serviceName);
        } catch (CommonFunctionalException e) {
            LOGGER.error("Error when retrieving resource bundles from {} : {}", serviceName, e.getLocalizedMessage());
        }
        
        MetadataStorer nlsStorer = new MetadataStorer();
        try {
            nlsStorer.storeResourceBundle(rbList);
        } catch (CommonResourceException e) {
            LOGGER.error("Error when saving resource bundles for {} :{}", serviceName, e.getLocalizedMessage());
        }
    }

}
