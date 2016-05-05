/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.webutils.timer;

import java.util.ArrayList;
import java.util.List;

import javax.management.Notification;
import javax.management.NotificationListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.dashboards.core.DBConnectionManager;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.StringUtil;
import oracle.sysman.emaas.platform.dashboards.targetmodel.services.GlobalStatus;
import oracle.sysman.emaas.platform.dashboards.webutils.services.RegistryServiceManager;

/**
 * @author guobaochen
 */
public class AvailabilityNotification implements NotificationListener
{
	private static final String ENTITY_NAMING_SERVICE_NAME = "EntityNaming";

	private static final String ENTITY_NAMING_SERVICE_VERSION = "1.0+";
	private static final String ENTITY_NAMING_SERVICE_REL = "collection/domains";
	private final Logger logger = LogManager.getLogger(AvailabilityNotification.class);

	private final RegistryServiceManager rsm;

	public AvailabilityNotification(RegistryServiceManager rsManager)
	{
		rsm = rsManager;
	}

	/* (non-Javadoc)
	 * @see javax.management.NotificationListener#handleNotification(javax.management.Notification, java.lang.Object)
	 */
	@Override
	public void handleNotification(Notification notification, Object handback)
	{
		logger.debug("Time triggered handler method. sequenceNumber={}", notification.getSequenceNumber());
		if (rsm.isRegistrationComplete() == null) {
			logger.warn("RegistryServiceManager hasn't registered. Check registry service next time");
			return;
		}
		// check if service manager is up and registration is complete
		if (!rsm.isRegistrationComplete() && !rsm.registerService()) {
			logger.warn(
					"Dashboards service registration is not completed. Ignore database or other dependant services availability checking");
			return;
		}

		// check database available
		boolean isDBAvailable = true;
		try {
			isDBAvailable = isDatabaseAvailable();
		}
		catch (Exception e) {
			isDBAvailable = false;
			logger.error(e.getLocalizedMessage(), e);
		}
		if (!isDBAvailable) {
			List<String> otherReasons = new ArrayList<String>();
			otherReasons.add("Dashboard-API service database is unavailable");
			rsm.markOutOfService(null, null, otherReasons);
			GlobalStatus.setDashboardDownStatus();
			logger.error("Dashboards service is out of service because database is unavailable");
			return;
		}

		// check entity naming availibility
		boolean isEntityNamingAvailable = true;
		try {
			isEntityNamingAvailable = isEntityNamingAvailable();
		}
		catch (Exception e) {
			isEntityNamingAvailable = false;
			logger.error(e.getLocalizedMessage(), e);
		}
		if (!isEntityNamingAvailable) {
			List<InstanceInfo> services = new ArrayList<InstanceInfo>();
			InstanceInfo ii = new InstanceInfo();
			ii.setServiceName(ENTITY_NAMING_SERVICE_NAME);
			ii.setVersion(ENTITY_NAMING_SERVICE_VERSION);
			services.add(ii);
			rsm.markOutOfService(services, null, null);
			GlobalStatus.setDashboardDownStatus();
			logger.error("Dashboards service is out of service because entity naming service is unavailable");
			return;
		}

		// now all checking is OK
		try {
			rsm.markServiceUp();
			logger.debug("Dashboards service is up");
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	private boolean isDatabaseAvailable()
	{
		DBConnectionManager dbcm = DBConnectionManager.getInstance();
		return dbcm.isDatabaseConnectionAvailable();
	}

	private boolean isEntityNamingAvailable()
	{
		Link lk = RegistryLookupUtil.getServiceInternalLink(ENTITY_NAMING_SERVICE_NAME, ENTITY_NAMING_SERVICE_VERSION,
				ENTITY_NAMING_SERVICE_REL, null);
		return lk != null && !StringUtil.isEmpty(lk.getHref());
	}

}
