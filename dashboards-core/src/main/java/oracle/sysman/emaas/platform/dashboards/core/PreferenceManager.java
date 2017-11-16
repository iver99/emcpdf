/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.PreferenceNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.model.Preference;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;
import oracle.sysman.emaas.platform.dashboards.entity.EmsPreference;

/**
 * @author wenjzhu
 */
public class PreferenceManager
{
	private static final PreferenceManager instance = new PreferenceManager();

	public static PreferenceManager getInstance()
	{
		return instance;
	}

	private PreferenceManager()
	{
		super();
	}

	public Preference getPreferenceByKey(String key, Long tenantId) throws PreferenceNotFoundException
	{
		EntityManager em = null;
		try {
			String currentUser = UserContext.getCurrentUser();
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();

			EmsPreference ep = dsf.getEmsPreference(currentUser, key);
			if (ep == null) {
				throw new PreferenceNotFoundException();
			}

			return Preference.valueOf(ep);
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public List<Preference> getPreferenceByMultipleKeys(List<String> keys, Long tenantId) throws PreferenceNotFoundException
	{
		EntityManager em = null;
		try {
			String currentUser = UserContext.getCurrentUser();
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();

			List<EmsPreference> epList = dsf.getEmsPreferencesByKeys(currentUser, keys);
			if (epList == null) {
				return null;
			}

			List<Preference> prefs = new ArrayList<Preference>();
			for (EmsPreference ep : epList) {
				Preference p = Preference.valueOf(ep);
				prefs.add(p);
			}

			return prefs;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public List<Preference> listPreferences(Long tenantId)
	{
		EntityManager em = null;
		List<Preference> prefs = new ArrayList<Preference>();
		try {
			String currentUser = UserContext.getCurrentUser();
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();

			List<EmsPreference> emps = dsf.getEmsPreferenceFindAll(currentUser);
			if (emps != null) {
				for (EmsPreference ep : emps) {
					prefs.add(Preference.valueOf(ep));
				}
			}

			return prefs;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void removeAllPreferences(Long tenantId)
	{
		EntityManager em = null;
		try {
			String currentUser = UserContext.getCurrentUser();
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			dsf.removeAllEmsPreferences(currentUser);
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void removePreference(String key, Long tenantId) throws DashboardException
	{
		if (key == null) {
			return;
		}
		EntityManager em = null;
		try {
			String currentUser = UserContext.getCurrentUser();
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();

			EmsPreference ep = dsf.getEmsPreference(currentUser, key);
			if (ep == null) {
				throw new PreferenceNotFoundException();
			}

			dsf.removeEmsPreference(ep);
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void savePreference(Preference pref, Long tenantId) throws DashboardException
	{
		if (pref == null) {
			return;
		}
		EntityManager em = null;
		boolean isNew = true;
		try {
			String currentUser = UserContext.getCurrentUser();
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();

			EmsPreference ep = null;
			if (pref.getKey() != null) {
				ep = dsf.getEmsPreference(currentUser, pref.getKey());
				if (ep != null) {
					isNew = false;
				}
			}

			if (isNew) {
				// create
				ep = pref.toEntity(null, currentUser);
				dsf.persistEmsPreference(ep);
			}
			else {
				// update
				ep = pref.toEntity(ep, currentUser);
				dsf.mergeEmsPreference(ep);
			}
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

}
