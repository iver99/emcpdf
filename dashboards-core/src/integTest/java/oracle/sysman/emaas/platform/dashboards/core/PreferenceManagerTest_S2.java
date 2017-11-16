/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core;

import java.util.Arrays;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.model.Preference;
import oracle.sysman.emaas.platform.dashboards.core.persistence.MockDashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author wenjzhu
 */
public class PreferenceManagerTest_S2 extends BaseTest
{
	private static final long TENANT_ID = 0L;
	private static final long TENANT_ID_1 = 1L;
	private static final String TEST_USER = "PREF_TEST";

	private static PreferenceManager pm;

	//@AfterMethod
	public void afterMethod()
	{
		if (pm != null) {
			pm.removeAllPreferences(TENANT_ID);
			pm = null;
		}
	}

	//@BeforeMethod
	public void beforeMethod() throws DashboardException
	{
		//load mock
		new MockDashboardServiceFacade();

		UserContext.setCurrentUser(TEST_USER);
		pm = PreferenceManager.getInstance();
		Preference p1 = new Preference();
		p1.setKey("p1");
		p1.setValue("p1");
		pm.savePreference(p1, TENANT_ID);
		Preference p2 = new Preference();
		p2.setKey("p2");
		p2.setValue("p2");
		pm.savePreference(p2, TENANT_ID);
		Preference p3 = new Preference();
		p3.setKey("p2");
		p3.setValue("p2");
		pm.savePreference(p3, TENANT_ID_1);
	}

	@Test(groups = { "s2" })
	public void testGetPreference() throws DashboardException
	{
		beforeMethod();
		Preference p = pm.getPreferenceByKey("p2", TENANT_ID);
		Assert.assertEquals(p.getKey(), "p2");
		afterMethod();
	}

	@Test(groups = { "s2" })
	public void testGetPreferencesByMultipleKeys() throws DashboardException
	{
		beforeMethod();
		List<String> keys = Arrays.asList("p1", "p2");
		List<Preference> psList = pm.getPreferenceByMultipleKeys(keys, TENANT_ID);
		Assert.assertEquals(psList.size(), 2);
		Assert.assertEquals("p1", psList.get(0).getValue());
		Assert.assertEquals("p2", psList.get(1).getValue());
		afterMethod();
	}

	@Test(groups = { "s2" })
	public void testGetPreferenceWithTenant() throws DashboardException
	{
		beforeMethod();
		List<Preference> tps = pm.listPreferences(TENANT_ID);
		Assert.assertEquals(tps.size(), 2);
		List<Preference> t1ps = pm.listPreferences(TENANT_ID_1);
		Assert.assertEquals(t1ps.size(), 1);
		Preference p = pm.getPreferenceByKey("p2", TENANT_ID);
		Assert.assertEquals(p.getKey(), "p2");
		p = pm.getPreferenceByKey("p2", TENANT_ID_1);
		Assert.assertEquals(p.getKey(), "p2");
		afterMethod();
	}

	@Test(groups = { "s2" })
	public void testListPreferences() throws DashboardException
	{
		beforeMethod();
		List<Preference> ps = pm.listPreferences(TENANT_ID);
		Assert.assertEquals(ps.size() >= 1, true);
		afterMethod();
	}

	@Test(groups = { "s2" })
	public void testRemovePreference() throws DashboardException
	{
		beforeMethod();
		pm.removePreference("p1", TENANT_ID);
		List<Preference> ps = pm.listPreferences(TENANT_ID);
		Assert.assertEquals(ps.size() == 1, true);
		afterMethod();
	}

	@Test(groups = { "s2" })
	public void testUpdatePreference() throws DashboardException
	{
		beforeMethod();
		Preference p = pm.getPreferenceByKey("p2", TENANT_ID);
		p.setValue("p2_update");
		pm.savePreference(p, TENANT_ID);
		p = pm.getPreferenceByKey("p2", TENANT_ID);
		Assert.assertEquals(p.getValue(), "p2_update");
		afterMethod();
	}

}
