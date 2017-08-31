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

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.webutils.metadata.MetadataRetriever;
import oracle.sysman.emaas.platform.dashboards.webutils.metadata.MetadataStorer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author reliang
 *
 */
public class OobRefreshRunnable extends MetadataRefreshRunnable {
    private static final Logger LOGGER = LogManager.getLogger(OobRefreshRunnable.class);
    @Override
    public void run() {
        LOGGER.info("Starting a new thread for fresh {} OOB.", serviceName);
        if(serviceName == null || serviceName.isEmpty()) {
            LOGGER.error("OobRefreshRunnable: there is no service name!");
            return;
        }
        
        int loopNum = 0;  // how many times DF has already tried to fetch the OOB
        while(!isServiceAvailable(serviceName, "1.0+") && (loopNum < MAX_LOOP_NUM)) {
            try {
                LOGGER.warn("{} time failed to fecth OOB dashboards from {}", loopNum++, serviceName);
                Thread.sleep(INTERVAL_TIME); // 60s
            } catch (InterruptedException e) {
                LOGGER.error(e.getLocalizedMessage(), e);
            }
        }
        
        List<Dashboard> oobList = new ArrayList<Dashboard>();
        MetadataRetriever oobRetriever = new MetadataRetriever();
        try {
            oobList = oobRetriever.getOobDashboardsByService(serviceName);
        } catch (DashboardException e) {
            LOGGER.error("Error when retrieving OOB from {} : {}", serviceName, e.getLocalizedMessage());
        }
        
        MetadataStorer oobStorer = new MetadataStorer();
        try {
            oobStorer.storeOobDashboards(oobList);
        } catch (DashboardException e) {
            LOGGER.error("Error when storing OOB into database for {} : {}", serviceName, e.getLocalizedMessage());
        }
    }

}
