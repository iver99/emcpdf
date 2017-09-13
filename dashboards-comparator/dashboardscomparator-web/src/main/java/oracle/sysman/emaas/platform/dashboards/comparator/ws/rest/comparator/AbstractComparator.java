/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emInternalSDK.rproxy.lookup.CloudLookups;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceQuery;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;

/**
 * @author guochen
 */
public abstract class AbstractComparator
{
	private static final Logger logger = LogManager.getLogger(AbstractComparator.class);

	protected static final String DF_SERVICE_NAME = "Dashboard-API";
	protected static final String DF_VERSION = "1.0+";
	protected static final int ZDT_INSTANCES_AMOUNT = 2;

	/**
	 * TODO: From this wiki
	 * page(https://confluence.oraclecorp.com/confluence/display/EMS/ZDT+-+API+Gateway+Manual+Deployment+and+Developer+Docs), I
	 * got the description: It will publish this information over an omcinstances rest api so that other services in this tier can
	 * get this information We will also publish an SDK with the following signature (that uses the rest sdk) HashMap<String,
	 * LookupClient> getOMCInstanceClients();
	 */
	public HashMap<String, LookupClient> getOMCInstances()
	{

		try {
			logger.info("Starts to compare the two DF OMC instances");
			// TODO: code is from EMCPSM-1975, need to verify in real ZDT environment
			HashMap<String, LookupClient> lcMap = CloudLookups.getCloudLookupClients();
			Set<Entry<String, LookupClient>> entrySet = lcMap.entrySet();
			logger.info("how many entries {}", entrySet.size());
			for (String key : lcMap.keySet()) {
				logger.info("key= {}",key);
				   LookupClient client = lcMap.get(key);
				   logger.info("look up client is {}",client.toString());
				  }
			if (entrySet.size() != ZDT_INSTANCES_AMOUNT) {
				logger.error(
						"Unexpected: retrieved unexpected amount of OMC instances from SDK method. Expected amount is {}, and actually amount is {}",
						ZDT_INSTANCES_AMOUNT, entrySet.size());
				logger.info("Completed to compare the two DF OMC instances");
				return null;
			}
			return lcMap;
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		return null;
	}

	protected Link getSingleInstanceUrl(LookupClient lc, String rel, String protocol) throws Exception
	{
		logger.info("start to get url");
		if (lc == null) {
			logger.info("lookup client is null");
			logger.error("Failed to lookup instance for specified lookup client is null!");
			return null;
		}
		if (rel == null) {
			logger.info("rel is null");
			logger.error("Failed to lookup instance for specified lookup rel is null!");
			return null;
		}
		if (protocol == null) {
			logger.info("protocol is null");
			logger.error("Failed to lookup instance for specified lookup protocol is null!");
			return null;
		}
		// TODO: following are testing code only!
		InstanceInfo info = InstanceInfo.Builder.newBuilder().withServiceName(DF_SERVICE_NAME).withVersion(DF_VERSION).build();
		logger.info("info is "+info);
		List<InstanceInfo> result = lc.lookup(new InstanceQuery(info));
		logger.info("result size is "+(result==null?null:result.size()));
		Link lk = null;
		if (result != null && result.size() > 0) {
			// [EMCPDF-733] Rest client can't handle https currently, so http protocol is enough for internal use
			// find http link only
			for (InstanceInfo internalInstance : result) {
				List<Link> links = internalInstance.getLinksWithProtocol(rel, protocol);
				logger.info("links size is "+(links==null?null:links.size()));
				if (links != null && links.size() > 0) {
					lk = links.get(0);
					return lk;
				}
			}
		}
		return lk;
	}
}
