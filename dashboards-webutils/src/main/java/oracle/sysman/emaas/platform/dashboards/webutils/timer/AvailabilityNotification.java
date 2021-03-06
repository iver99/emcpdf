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

import javax.management.Notification;
import javax.management.NotificationListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.dashboards.core.DBConnectionManager;
import oracle.sysman.emaas.platform.dashboards.core.util.StringUtil;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.targetmodel.services.GlobalStatus;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.webutils.services.RegistryServiceManager;


/**
 * @author guobaochen
 */
public class AvailabilityNotification implements NotificationListener
{
	private static final String ENTITY_NAMING_SERVICE_NAME = "EntityNaming";

	private static final String ENTITY_NAMING_SERVICE_VERSION = "1.0+";
	private static final String ENTITY_NAMING_SERVICE_REL = "collection/domains";
	private static final Logger LOGGER = LogManager.getLogger(AvailabilityNotification.class);

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
		LOGGER.debug("Time triggered handler method. sequenceNumber={}", notification.getSequenceNumber());
		if (rsm.isRegistrationComplete() == null) {
			LOGGER.warn("RegistryServiceManager hasn't registered. Check registry service next time");
			return;
		}
		// check if service manager is up and registration is complete
		if (!rsm.isRegistrationComplete() && !rsm.registerService()) {
			LOGGER.warn(
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
			LOGGER.error(e.getLocalizedMessage(), e);
		}
		if (!isDBAvailable) {
//			List<String> otherReasons = new ArrayList<String>();
//			otherReasons.add("Dashboard-API service database is unavailable");
			// we don't mark OUT_OF_SERVICE on service registry on monitoring EM, instead let's keep the status internally
//			rsm.markOutOfService(null, null, otherReasons);
//			GlobalStatus.setDashboardDownStatus();
			DependencyStatus.getInstance().setDatabaseUp(Boolean.FALSE);
			LOGGER.error("Dashboards service keeps running, although database is unavailable");
		} else {
			DependencyStatus.getInstance().setDatabaseUp(Boolean.TRUE);
			LOGGER.debug("DF database is UP");
		}

		// check entity naming availibility
		boolean isEntityNamingAvailable = true;
		try {
			isEntityNamingAvailable = isEntityNamingAvailable();
		}
		catch (Exception e) {
			isEntityNamingAvailable = false;
			LOGGER.error(e.getLocalizedMessage(), e);
		}
		if (!isEntityNamingAvailable) {
			// we don't mark OUT_OF_SERVICE on service registry on monitoring EM, instead let's keep the status internally
//			List<InstanceInfo> services = new ArrayList<InstanceInfo>();
//			InstanceInfo ii = new InstanceInfo();
//			ii.setServiceName(ENTITY_NAMING_SERVICE_NAME);
//			ii.setVersion(ENTITY_NAMING_SERVICE_VERSION);
//			services.add(ii);
//			rsm.markOutOfService(services, null, null);
//			GlobalStatus.setDashboardDownStatus();
			DependencyStatus.getInstance().setEntityNamingUp(Boolean.FALSE);
			LOGGER.error("Dashboards service keeps running, although entity naming service is OUT_OF_SERVICE");
		} else {
			DependencyStatus.getInstance().setEntityNamingUp(Boolean.TRUE);
			LOGGER.debug("Entity naming service is UP");
		}
		
		// now all checking is OK
		try {
			rsm.markServiceUp();
			GlobalStatus.setDashboardUpStatus();
			LOGGER.debug("Dashboards service is up");
		}
		catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}
	}

	private boolean isDatabaseAvailable()
	{
		DBConnectionManager dbcm = DBConnectionManager.getInstance();
		return dbcm.isDatabaseConnectionAvailable();
	}

	private boolean isEntityNamingAvailable()
	{
		//when checking entity naming  we don't use cache to make sure check is reliable
		Link lk = RegistryLookupUtil.getServiceInternalLink(ENTITY_NAMING_SERVICE_NAME, ENTITY_NAMING_SERVICE_VERSION,
				ENTITY_NAMING_SERVICE_REL, null);
		return lk != null && !StringUtil.isEmpty(lk.getHref());
	}

}
