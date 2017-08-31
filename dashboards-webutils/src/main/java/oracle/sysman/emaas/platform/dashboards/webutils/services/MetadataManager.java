/*
 * Copyright (C) 2017 Oracle
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
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsResourceBundle;
import oracle.sysman.emaas.platform.dashboards.webutils.metadata.MetadataRetriever;
import oracle.sysman.emaas.platform.dashboards.webutils.metadata.MetadataStorer;
import oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import weblogic.application.ApplicationLifecycleEvent;

/**
 * @author reliang
 *
 */
public class MetadataManager implements ApplicationServiceManager
{
    private static final Logger LOGGER = LogManager.getLogger(MetadataManager.class);
    //TODO fetch all services by rel name
    private static final List<String> oobProvider = Arrays.asList(MetadataRetriever.SERVICENAME_APM,
            MetadataRetriever.SERVICENAME_ITA, MetadataRetriever.SERVICENAME_LA, MetadataRetriever.SERVICENAME_ORCHESTRATION,
            MetadataRetriever.SERVICENAME_SECURITY_ANALYTICS, MetadataRetriever.SERVICENAME_UDE, MetadataRetriever.SERVICENAME_EVENT);
    /* (non-Javadoc)
     * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#getName()
     */
    @Override
    public String getName()
    {
        return "Metadata Manager Service";
    }

    /* (non-Javadoc)
     * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#postStart(weblogic.application.ApplicationLifecycleEvent)
     */
    @Override
    public void postStart(ApplicationLifecycleEvent evt) throws Exception
    {
        LOGGER.info("Entry: MetadataManager.postStart");
        MetadataRetriever metadataRetriever = new MetadataRetriever();
        MetadataStorer metadataStorer = new MetadataStorer();
        
        for(String serviceName : oobProvider) {
            // Load OOB Dashboard from other services
            List<Dashboard> oobList = new ArrayList<Dashboard>();
            try {
                oobList = metadataRetriever.getOobDashboardsByService(serviceName);
            } catch (DashboardException e) {
                LOGGER.error("Error when retrieving OOB from {} :{}", serviceName, e.getLocalizedMessage());
            } catch (Exception ex) {
                // in case some unknow exception
                LOGGER.error(ex.getLocalizedMessage(), ex);
            }
            
            try {
                metadataStorer.storeOobDashboards(oobList);
            } catch (DashboardException e) {
                LOGGER.error("Error when storing OOB into database for {} : {}", serviceName, e.getLocalizedMessage());
            } catch (Exception ex) {
                // in case some unknow exception
                LOGGER.error(ex.getLocalizedMessage(), ex);
            }
            
            // Load resource bundle for OOB metadata from other services
            List<EmsResourceBundle> rbList = new ArrayList<EmsResourceBundle>();
            try {
                rbList = metadataRetriever.getResourceBundleByService(serviceName);
            } catch (DashboardException e) {
                LOGGER.error("Error when retrieving resource bundles from {} : {}", serviceName, e.getLocalizedMessage());
            } catch (Exception ex) {
                // in case some unknow exception
                LOGGER.error(ex.getLocalizedMessage(), ex);
            }
            
            try {
                metadataStorer.storeResourceBundle(rbList);
            } catch (DashboardException e ) {
                LOGGER.error("Error when saving resource bundles for {} : {}", serviceName, e.getLocalizedMessage());
            } catch (Exception ex) {
                // in case some unknow exception
                LOGGER.error(ex.getLocalizedMessage(), ex);
            }
            
        }
    }

    /* (non-Javadoc)
     * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#postStop(weblogic.application.ApplicationLifecycleEvent)
     */
    @Override
    public void postStop(ApplicationLifecycleEvent evt) throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#preStart(weblogic.application.ApplicationLifecycleEvent)
     */
    @Override
    public void preStart(ApplicationLifecycleEvent evt) throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#preStop(weblogic.application.ApplicationLifecycleEvent)
     */
    @Override
    public void preStop(ApplicationLifecycleEvent evt) throws Exception
    {
        // TODO Auto-generated method stub

    }

}
