/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.RestClientProxy;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.AbstractComparator;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.InstancesComparedData;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.InstanceData;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;
/**
 * @author guochen
 */
public class DashboardCountsComparator extends AbstractComparator
{
	private static final Logger logger = LogManager.getLogger(DashboardCountsComparator.class);

	public InstancesComparedData<CountsEntity> compare(String tenantId, String userTenant)
	{
		try {
			logger.info("Starts to compare the two DF OMC instances");
			HashMap<String, LookupClient> instances = getOMCInstances();
			if (instances == null) {
				logger.error("Failed to retrieve ZDT OMC instances");
				return null;
			}
			String key1 = null;
			String key2 = null;
			LookupClient client1 = null;
			LookupClient client2 = null;
			for (String key : instances.keySet()) {
				if (client1 == null) {
					client1 = instances.get(key);
					key1 = key;
				} else {
					if (client2 == null)
					client2 = instances.get(key);
					key2 = key;
				}
			}

			CountsEntity ze1 = retrieveCountsForSingleInstance(tenantId, userTenant,client1);
			if (ze1 == null) {
				logger.error("Failed to retrieve ZDT count entity for instance {}", key1);
				logger.info("Completed to compare the two DF OMC instances");
				return null;
			}

			CountsEntity ze2 = retrieveCountsForSingleInstance(tenantId, userTenant,client2);
			if (ze2 == null) {
				logger.error("Failed to retrieve ZDT count entity for instance {}", key2);
				logger.info("Completed to compare the two DF OMC instances");
				return null;
			}

			// now compare
			InstancesComparedData<CountsEntity> cd = compareInstancesCounts(key1, client1, ze1, key2, client2, ze2);
			logger.info("Completed to compare the two DF OMC instances");
			return cd;
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return null;
		}
	}

	/**
	 * Compare the data/counts from the 2 instances<br>
	 * The returned <code>ComparedData</code> will contain only the different counts, and leave the same counts null
	 *
	 * @param ze1
	 * @param ze2
	 */
	public InstancesComparedData<CountsEntity> compareInstancesCounts(String key1, LookupClient client1, CountsEntity ze1,
			String key2, LookupClient client2, CountsEntity ze2)
	{
		CountsEntity differentCountsForInstance1 = new CountsEntity();
		CountsEntity differentCountsForInstance2 = new CountsEntity();
		boolean allMatch = true;
		if (ze1.getCountOfDashboards() != ze2.getCountOfDashboards()) {
			logger.info("Dashboards count does not match. In instance \"{}\", count is {}. In instance \"{}\", count is {}",
					key1, ze1.getCountOfDashboards(), key2, ze2.getCountOfDashboards());
			differentCountsForInstance1.setCountOfDashboards(ze1.getCountOfDashboards());
			differentCountsForInstance2.setCountOfDashboards(ze2.getCountOfDashboards());
			allMatch = false;
		}
		if (ze1.getCountOfUserOptions() != ze2.getCountOfUserOptions()) {
			logger.info("Favorites count does not match. In instance \"{}\", count is {}. In instance \"{}\", count is {}",
					key1, ze1.getCountOfUserOptions(), key2, ze2.getCountOfUserOptions());
			differentCountsForInstance1.setCountOfUserOptions(ze1.getCountOfUserOptions());
			differentCountsForInstance2.setCountOfUserOptions(ze2.getCountOfUserOptions());
			allMatch = false;
		}
		if (ze1.getCountOfPreference() != ze2.getCountOfPreference()) {
			logger.info("Preferences count does not match. In instance \"{}\", count is {}. In instance \"{}\", count is {}",
					key1, ze1.getCountOfPreference(), key2, ze2.getCountOfPreference());
			differentCountsForInstance1.setCountOfPreference(ze1.getCountOfPreference());
			differentCountsForInstance2.setCountOfPreference(ze2.getCountOfPreference());
			allMatch = false;
		}
		if (allMatch) {
			logger.info("All counts from both instances (instance \"{}\" and instance \"{}\") match!", key1,
					key2);
		}
		InstanceData<CountsEntity> instance1 = new InstanceData<CountsEntity>(key1, client1, differentCountsForInstance1);
		InstanceData<CountsEntity> instance2 = new InstanceData<CountsEntity>(key2, client2, differentCountsForInstance2);
		InstancesComparedData<CountsEntity> cd = new InstancesComparedData<CountsEntity>(instance1, instance2);
		return cd;
	}

	/**
	 * @throws Exception
	 * @throws IOException
	 */
	private CountsEntity retrieveCountsForSingleInstance(String tenantId, String userTenant,LookupClient lc) throws Exception, IOException
	{
		Link lk = getSingleInstanceUrl(lc, "zdt/counts", "http");
		if (lk == null) {
			logger.warn("Get a null or empty link for one single instance!");
			return null;
		}
		logger.info("lookup link is {}", lk.getHref());
		//String response = new TenantSubscriptionUtil.RestClient().get(lk.getHref(), tenantId, userTenant);
		
		
		RestClient rc = RestClientProxy.getRestClient();
		rc.setHeader(RestClient.X_USER_IDENTITY_DOMAIN_NAME,tenantId);
		//rc.setHeader(RestClient.X_REMOTE_USER,userTenant);
		
		char[] authToken = LookupManager.getInstance().getAuthorizationToken();
		//String response = rc.get(lk.getHref(),tenantId,new String(authToken));
		String response = rc.get(lk.getHref(),tenantId,new String(authToken));
		logger.info("Checking sync reponse. Response is " + response);
		
		JsonUtil ju = JsonUtil.buildNormalMapper();
		CountsEntity ze = ju.fromJson(response, CountsEntity.class);
		if (ze == null) {
			logger.warn("Checking dashboard OMC instance counts: null/empty entity retrieved.");
			return null;
		}
		// TODO: for the 1st step implementation, let's log in log files then
		logger.info(
				"Retrieved counts for dashboards OMC instance: dashboard count - {}, favorites count - {}, preference count - {}",
				ze.getCountOfDashboards(), ze.getCountOfUserOptions(), ze.getCountOfPreference());
		return ze;
	}
}
