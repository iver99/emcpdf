/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.util.concurrent.ExecutorService;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import oracle.sysman.emaas.platform.dashboards.webutils.ParallelThreadPool;
import oracle.sysman.emaas.platform.dashboards.ws.rest.thread.MetadataRefreshRunnable;
import oracle.sysman.emaas.platform.dashboards.ws.rest.thread.NlsRefreshRunnable;
import oracle.sysman.emaas.platform.dashboards.ws.rest.thread.OobRefreshRunnable;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author reliang
 *
 */
@Path("/v1/refresh")
public class MetadataRefreshAPI extends APIBase
{
    private static final Logger LOGGER = LogManager.getLogger(MetadataRefreshAPI.class);
    
    @PUT
    @Path("oob/{serviceName}")
    public Response refreshOOB(@PathParam("serviceName") String serviceName) {
        infoInteractionLogAPIIncomingCall("Dashbaord API", "Service call to [PUT] /v1/refresh/oob/{}", serviceName);
        MetadataRefreshRunnable oobRunnable = new OobRefreshRunnable();
        oobRunnable.setServiceName(serviceName);
        ExecutorService pool = ParallelThreadPool.getThreadPool();
        pool.submit(oobRunnable);
        return Response.ok().build();
    }
    
    @PUT
    @Path("nls/{serviceName}")
    public Response refreshNLS(@PathParam("serviceName") String serviceName) {
        infoInteractionLogAPIIncomingCall("Dashbaord API", "Service call to [PUT] /v1/refresh/nls/{}", serviceName);
        MetadataRefreshRunnable nlsRunnable = new NlsRefreshRunnable();
        nlsRunnable.setServiceName(serviceName);
        ExecutorService pool = ParallelThreadPool.getThreadPool();
        pool.submit(nlsRunnable);
        return Response.ok().build();
    }
    
    @POST
    @Path("widgetcache")
    public Response expireOOBWidgetCache(String fromService) {
        infoInteractionLogAPIIncomingCall("Dashbaord API", "Service call to [POST] /v1/refresh/widgetcache from {}", fromService);
        // This will clear the dashboard-api side OOB widget cache
        CacheUtil.clearCacheGroup(CacheConstants.CACHES_OOB_DASHBOARD_SAVEDSEARCH_CACHE);
        LOGGER.info("Cached OOB widget data now is cleaned after getting notification from SSF side");
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
