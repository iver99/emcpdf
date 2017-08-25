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

import org.testng.annotations.Test;

/**
 * @author reliang
 *
 */
public class MetadataRefreshAPITest {
    MetadataRefreshAPI api = new MetadataRefreshAPI();
    
    @Test(groups = {"s1"})
    public void testRefreshOOB() {
        api.refreshOOB("");
        api.refreshOOB("serviceName");
    }

    @Test(groups = {"s1"})
    public void testRefreshNLS() {
        api.refreshNLS("");
        api.refreshNLS("serviceName");
    }
    
    @Test(groups = {"s1"})
    public void testExpireOOBWidgetCache() {
        api.expireOOBWidgetCache("serviceName");
    }
}
