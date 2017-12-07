/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * @author guobaochen
 */
@ApplicationPath("api")
public class DashboardsApplication extends Application
{
	@Override
	public Set<Class<?>> getClasses()
	{
		Set<Class<?>> hs = new HashSet<Class<?>>();
		hs.add(DashboardAPI.class);
		hs.add(FavoriteAPI.class);
		hs.add(PreferenceAPI.class);
		hs.add(LoggingAPI.class);
		hs.add(LoggingConfigAPI.class);
		hs.add(TenantSubscriptionsAPI.class);
		hs.add(ConfigurationAPI.class);
		hs.add(RegistryLookupAPI.class);
		hs.add(ZDTAPI.class);
		hs.add(OMCStatusAPI.class);
		hs.add(WidgetNotificationAPI.class);
		hs.add(SSFLifeCycleNotificationAPI.class);
		hs.add(CacheAPI.class);
		hs.add(TenantSubscriptionsAPIv2.class);
		hs.add(MetadataRefreshAPI.class);
		hs.add(InternalToolAPI.class);
		hs.add(DashboardStatusAPI.class);
		return hs;
	}
}
