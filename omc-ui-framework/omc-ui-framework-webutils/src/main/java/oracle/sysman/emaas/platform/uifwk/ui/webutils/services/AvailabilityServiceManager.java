/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.webutils.services;

import java.util.Date;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.uifwk.ui.webutils.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.uifwk.ui.webutils.util.StringUtil;
import oracle.sysman.emaas.platform.uifwk.ui.webutils.wls.lifecycle.ApplicationServiceManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import oracle.sysman.emaas.platform.uifwk.ui.target.services.GlobalStatus;
import weblogic.application.ApplicationLifecycleEvent;
import weblogic.management.timer.Timer;

/**
 * @author aduan
 */
public class AvailabilityServiceManager implements ApplicationServiceManager, NotificationListener
{
	private final Logger logger = LogManager.getLogger(AvailabilityServiceManager.class);

	private static final long PERIOD = Timer.ONE_MINUTE;

	private static final String DASHBOARD_API_SERVICE_NAME = "Dashboard-API";
	private static final String DASHBOARD_API_SERVICE_VERSION = "0.1";
	private static final String DASHBOARD_API_SERVICE_REL = "base";

	private static final String SAVED_SEARCH_SERVICE_NAME = "SavedSearch";
	private static final String SAVED_SEARCH_SERVICE_VERSION = "0.1";
	private static final String SAVED_SEARCH_SERVICE_REL = "search";

	private Timer timer;
	private Integer notificationId;
	private final RegistryServiceManager rsm;

	public AvailabilityServiceManager(RegistryServiceManager rsm)
	{
		this.rsm = rsm;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.uifwk.ui.webutils.wls.lifecycle.ApplicationServiceManager#getName()
	 */
	@Override
	public String getName()
	{
		return "OMC UI Framework Timer Service";
	}

	/* (non-Javadoc)
	 * @see javax.management.NotificationListener#handleNotification(javax.management.Notification, java.lang.Object)
	 */
	@Override
	public void handleNotification(Notification notification, Object handback)
	{
		logger.debug("Time triggered handler method. sequenceNumber={}, notificationId={}", notification.getSequenceNumber(),
				notificationId);
		if (rsm.isRegistrationComplete() == null) {
			logger.info("RegistryServiceManager hasn't registered. Check registry service next time");
			return;
		}
		// check if service manager is up and registration is complete
		if (!rsm.isRegistrationComplete() && !rsm.registerService()) {
			logger.info("OMC UI Framework service registration is not completed. Ignore dependant services availability checking");
			return;

		}
		// check ssf's avaibility
		boolean isSSFAvailable = true;
		try {
			isSSFAvailable = isSavedSearchAvailable();
		}
		catch (Exception e) {
			isSSFAvailable = false;
			logger.error(e.getLocalizedMessage(), e);
		}
		if (!isSSFAvailable) {
			rsm.markOutOfService();
			GlobalStatus.setOmcUiDownStatus();
			logger.info("OMC UI Framework service is out of service because Saved Search API service is unavailable");
			return;
		}

		// check df api service's availability
		boolean isDFApiAvailable = true;
		try {
			isDFApiAvailable = isDashboardAPIAvailable();
		}
		catch (Exception e) {
			isDFApiAvailable = false;
			logger.error(e.getLocalizedMessage(), e);
		}
		if (!isDFApiAvailable) {
			rsm.markOutOfService();
			GlobalStatus.setOmcUiDownStatus();
			logger.info("OMC UI Framework service is out of service because Dashboard API service is unavailable");
			return;
		}

		// now all checking is OK
		try {
			rsm.markServiceUp();
			GlobalStatus.setOmcUiUpStatus();
			logger.debug("OMC UI Framework service is up");
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.uifwk.ui.webutils.wls.lifecycle.ApplicationServiceManager#postStart(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void postStart(ApplicationLifecycleEvent evt) throws Exception
	{
		timer = new Timer();
		timer.addNotificationListener(this, null, null);
		Date timerTriggerAt = new Date(new Date().getTime() + 10000L);
		notificationId = timer.addNotification("OmcUiFrameworkServiceTimer", null, this, timerTriggerAt, PERIOD, 0);
		timer.start();
		logger.info("Timer for OMC UI Framework service dependencies checking started. notificationId={}", notificationId);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.uifwk.ui.webutils.wls.lifecycle.ApplicationServiceManager#postStop(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void postStop(ApplicationLifecycleEvent evt) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.uifwk.ui.webutils.wls.lifecycle.ApplicationServiceManager#preStart(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void preStart(ApplicationLifecycleEvent evt) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.uifwk.ui.webutils.wls.lifecycle.ApplicationServiceManager#preStop(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void preStop(ApplicationLifecycleEvent evt) throws Exception
	{
		logger.info("Pre-stopping availability service");
		try {
			timer.stop();
			timer.removeNotification(notificationId);
			logger.info("Timer for OMC UI Framework dependencies checking stopped, notificationId={}", notificationId);
		}
		catch (InstanceNotFoundException e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	private boolean isDashboardAPIAvailable()
	{
		Link lk = RegistryLookupUtil.getServiceInternalLink(DASHBOARD_API_SERVICE_NAME, DASHBOARD_API_SERVICE_VERSION,
				DASHBOARD_API_SERVICE_REL, null);
		return lk != null && !StringUtil.isEmpty(lk.getHref());
	}

	private boolean isSavedSearchAvailable()
	{
		Link lk = RegistryLookupUtil.getServiceInternalLink(SAVED_SEARCH_SERVICE_NAME, SAVED_SEARCH_SERVICE_VERSION,
				SAVED_SEARCH_SERVICE_REL, null);
		return lk != null && !StringUtil.isEmpty(lk.getHref());
	}

}
