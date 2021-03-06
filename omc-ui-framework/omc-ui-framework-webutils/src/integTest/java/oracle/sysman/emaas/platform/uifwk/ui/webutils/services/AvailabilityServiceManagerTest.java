/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.webutils.services;

import java.util.ArrayList;
import java.util.List;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo.Builder;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emaas.platform.uifwk.ui.target.services.GlobalStatus;

import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil.VersionedLink;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import weblogic.management.timer.Timer;

/**
 * @author aduan
 */
public class AvailabilityServiceManagerTest
{
	private final RegistryServiceManager rsm = new RegistryServiceManager();
	private final AvailabilityServiceManager asm = new AvailabilityServiceManager(rsm);
	private final static String DASHBOARD_API_SERVICE_NAME = "Dashboard-API";
	private final static String DASHBOARD_API_SERVICE_VERSION = "1.0+";
	private final static String DASHBOARD_API_SERVICE_REL = "base";

	private final static String SAVED_SEARCH_SERVICE_NAME = "SavedSearch";
	private final static String SAVED_SEARCH_SERVICE_VERSION = "1.0+";
	private final static String SAVED_SEARCH_SERVICE_REL = "search";

	@Test(groups = { "s1" })
	public void testGetName()
	{
		AssertJUnit.assertEquals(asm.getName(), "OMC UI Framework Timer Service");
	}

	@Test(groups = { "s2" })
	public void testHandleNotification(@Mocked final Notification anyNoti, @Mocked final RegistryServiceManager anyRsm,
			@Mocked final RegistryLookupUtil anyLookupUtil, @Mocked final LookupManager lookupmgr,
			@Mocked final LookupClient rsClient, @Mocked final InstanceInfo anyInstanceInfo, @Mocked final Builder anyBuilder) throws Exception
	{
		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = null;
				anyNoti.getSequenceNumber();
				result = 123456789;
			}
		};
		asm.handleNotification(anyNoti, null);

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.FALSE;
			}
		};
		asm.handleNotification(anyNoti, null);

		final List<InstanceInfo> instanceInfos = new ArrayList<InstanceInfo>();
		instanceInfos.add(new InstanceInfo());
		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				RegistryLookupUtil.getServiceInternalLink(SAVED_SEARCH_SERVICE_NAME, SAVED_SEARCH_SERVICE_VERSION,
						SAVED_SEARCH_SERVICE_REL, null);
				result = new VersionedLink(new Link().withRel("search").withHref("http://den00zyr.us.oracle.com:7019/savedsearch/v1/search"), null);
				RegistryLookupUtil.getServiceInternalLink(DASHBOARD_API_SERVICE_NAME, DASHBOARD_API_SERVICE_VERSION,
						DASHBOARD_API_SERVICE_REL, null);
				result = new VersionedLink(new Link().withRel("base").withHref("http://den00zyr.us.oracle.com:7019/emcpdf/api/v1/"), null);
			}
		};
		asm.handleNotification(anyNoti, null);
		Assert.assertTrue(GlobalStatus.isOmcUiUp());

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				RegistryLookupUtil.getServiceInternalLink(SAVED_SEARCH_SERVICE_NAME, SAVED_SEARCH_SERVICE_VERSION,
						SAVED_SEARCH_SERVICE_REL, null);
				result = null;
			}
		};
		asm.handleNotification(anyNoti, null);
		Assert.assertTrue(GlobalStatus.isOmcUiUp());

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				RegistryLookupUtil.getServiceInternalLink(SAVED_SEARCH_SERVICE_NAME, SAVED_SEARCH_SERVICE_VERSION,
						SAVED_SEARCH_SERVICE_REL, null);
				result = new VersionedLink(new Link().withRel("search").withHref("http://den00zyr.us.oracle.com:7019/savedsearch/v1/search"), null);
				RegistryLookupUtil.getServiceInternalLink(DASHBOARD_API_SERVICE_NAME, DASHBOARD_API_SERVICE_VERSION,
						DASHBOARD_API_SERVICE_REL, null);
				result = null;
			}
		};
		asm.handleNotification(anyNoti, null);
		Assert.assertTrue(GlobalStatus.isOmcUiUp());

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				RegistryLookupUtil.getServiceInternalLink(SAVED_SEARCH_SERVICE_NAME, SAVED_SEARCH_SERVICE_VERSION,
						SAVED_SEARCH_SERVICE_REL, null);
				result = new Exception();
			}
		};
		asm.handleNotification(anyNoti, null);
		Assert.assertTrue(GlobalStatus.isOmcUiUp());

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				RegistryLookupUtil.getServiceInternalLink(SAVED_SEARCH_SERVICE_NAME, SAVED_SEARCH_SERVICE_VERSION,
						SAVED_SEARCH_SERVICE_REL, null);
				result = new VersionedLink(new Link().withRel("search").withHref("http://den00zyr.us.oracle.com:7019/savedsearch/v1/search"), null);
				RegistryLookupUtil.getServiceInternalLink(DASHBOARD_API_SERVICE_NAME, DASHBOARD_API_SERVICE_VERSION,
						DASHBOARD_API_SERVICE_REL, null);
				result = new Exception();
			}
		};
		asm.handleNotification(anyNoti, null);
		Assert.assertTrue(GlobalStatus.isOmcUiUp());

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				RegistryLookupUtil.getServiceInternalLink(SAVED_SEARCH_SERVICE_NAME, SAVED_SEARCH_SERVICE_VERSION,
						SAVED_SEARCH_SERVICE_REL, null);
				result = new VersionedLink(new Link().withRel("search").withHref("http://den00zyr.us.oracle.com:7019/savedsearch/v1/search"), null);
				RegistryLookupUtil.getServiceInternalLink(DASHBOARD_API_SERVICE_NAME, DASHBOARD_API_SERVICE_VERSION,
						DASHBOARD_API_SERVICE_REL, null);
				result = new VersionedLink(new Link().withRel("base").withHref("http://den00zyr.us.oracle.com:7019/emcpdf/api/v1/"), null);
				anyRsm.markServiceUp();
				result = new Exception();
			}
		};
		asm.handleNotification(anyNoti, null);
		Assert.assertTrue(GlobalStatus.isOmcUiUp());
	}

	@Test(groups = { "s2" })
	public void testStartStop(@Mocked final Timer anyTimer) throws Exception
	{
		new Expectations() {
			{
				new Timer();
				times = 1;
				anyTimer.addNotificationListener((NotificationListener) any, null, null);
				times = 1;
				anyTimer.start();
				times = 1;
				anyTimer.stop();
				times = 1;
				anyTimer.removeNotification((Integer) any);
				times = 1;
			}
		};

		asm.postStart(null);
		asm.preStop(null);
	}

	@Test(groups = { "s2" })
	public void testStartStopException(@Mocked final Timer anyTimer) throws Exception
	{
		new Expectations() {
			{
				anyTimer.removeNotification((Integer) any);
				result = new InstanceNotFoundException();
				times = 1;
			}
		};

		asm.preStop(null);
	}
}
