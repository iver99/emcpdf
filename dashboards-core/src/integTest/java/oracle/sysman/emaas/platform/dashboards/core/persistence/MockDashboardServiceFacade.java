/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import mockit.Mock;
import mockit.MockUp;
import oracle.sysman.emaas.platform.dashboards.core.util.FacadeUtil;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardFavorite;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardLastAccess;
import oracle.sysman.emaas.platform.dashboards.entity.EmsPreference;

/**
 * @author wenjzhu
 */
public class MockDashboardServiceFacade extends MockUp<DashboardServiceFacade>
{
	static class EmsDashboardFavoriteSelector implements EntitySelector<EmsDashboardFavorite>
	{

		private final String username;
		private final Long dashboardId;

		public EmsDashboardFavoriteSelector(String username, Long dashboardId)
		{
			this.username = username;
			this.dashboardId = dashboardId;
		}

		@Override
		public boolean selectEntity(EmsDashboardFavorite entity)
		{
			if (dashboardId != null) {
				if (entity.getDashboard() == null || !entity.getDashboard().getDashboardId().equals(dashboardId)) {
					return false;
				}
			}
			if (username != null) {
				if (!entity.getUserName().equals(username)) {
					return false;
				}
			}
			return true;
		}
	}

	static class EmsDashboardLastAccessSelector implements EntitySelector<EmsDashboardLastAccess>
	{

		private final String username;
		private final Long dashboardId;

		public EmsDashboardLastAccessSelector(String username, Long dashboardId)
		{
			this.username = username;
			this.dashboardId = dashboardId;
		}

		@Override
		public boolean selectEntity(EmsDashboardLastAccess entity)
		{
			if (dashboardId != null) {
				if (!entity.getDashboardId().equals(dashboardId)) {
					return false;
				}
			}
			if (username != null) {
				if (!entity.getAccessedBy().equals(username)) {
					return false;
				}
			}
			return true;
		}
	}

	static class EmsDashboardSelector implements EntitySelector<EmsDashboard>
	{

		private final String dashboardName;
		private boolean checkName = false;
		private final Long dashboardId;
		private boolean checkId = false;
		private final String username;
		private boolean checkUser = false;
		private final Boolean notSoftDeleted;

		public EmsDashboardSelector(Long dashboardId, String dashboardName, String username, Boolean notSoftDeleted)
		{
			this.dashboardName = dashboardName;
			if (this.dashboardName != null) {
				checkName = true;
			}
			this.dashboardId = dashboardId;
			if (this.dashboardId != null) {
				checkId = true;
			}

			this.username = username;
			if (this.username != null) {
				checkUser = true;
			}
			this.notSoftDeleted = notSoftDeleted;
		}

		@Override
		public boolean selectEntity(EmsDashboard entity)
		{
			if (checkId && !entity.getDashboardId().equals(dashboardId)) {
				return false;
			}
			if (checkName && !entity.getName().equals(dashboardName)) {
				return false;
			}
			if (checkUser && !entity.getOwner().equals(username)) {
				return false;
			}
			if (notSoftDeleted != null) {
				if (notSoftDeleted) {
					if (entity.getDeleted() != null && entity.getDeleted() > 0) {
						return false;
					}
				}
				else {
					if (entity.getDeleted() == null || entity.getDeleted() == 0L) {
						return false;
					}
				}
			}
			return true;
		}
	}

	static class EmsPreferenceSelector implements EntitySelector<EmsPreference>
	{

		private boolean checkKey = false;
		private final String username;
		private String key;

		public EmsPreferenceSelector(String username)
		{
			this.username = username;
		}

		public EmsPreferenceSelector(String username, String key)
		{
			this.username = username;
			this.key = key;
			checkKey = true;
		}

		@Override
		public boolean selectEntity(EmsPreference entity)
		{
			if (checkKey) {
				if (!entity.getPrefKey().equals(key)) {
					return false;
				}
			}
			if (!entity.getUserName().equals(username)) {
				return false;
			}
			return true;
		}
	}

	static interface EntitySelector<T>
	{
		public boolean selectEntity(T entity);
	}

	private long dashboardId = 1001;
	private Long currentTenant;

	private final Map<Long, Map<Class<?>, List<?>>> storages = new HashMap<Long, Map<Class<?>, List<?>>>();

	@Mock
	public void $init(Long tenantId)
	{
		System.out.println("[MockDashboardServiceFacade] $init called");
		currentTenant = tenantId;
		Map<Class<?>, List<?>> tenantStorage = storages.get(currentTenant);
		if (tenantStorage == null) {
			storages.put(currentTenant, new HashMap<Class<?>, List<?>>());
			System.out.println("Local storage created for tenant: " + currentTenant);
		}
		//System.out.println("dashboard id: " + newDashboardId());
	}

	@Mock
	public void commitTransaction()
	{
		System.out.println("[MockDashboardServiceFacade] commitTransaction called");
	}

	@Mock
	public EmsDashboard getEmsDashboardById(Long dashboardId)
	{
		System.out.println("[MockDashboardServiceFacade] getEmsDashboardById called");
		List<EmsDashboard> ps = this.localFind(EmsDashboard.class, new EmsDashboardSelector(dashboardId, null, null, null));
		return ps.isEmpty() ? null : ps.get(0);
	}

	@Mock
	public EmsDashboard getEmsDashboardByName(String name, String owner)
	{
		System.out.println("[MockDashboardServiceFacade] getEmsDashboardByName called");
		List<EmsDashboard> ps = this.localFind(EmsDashboard.class, new EmsDashboardSelector(null, name, owner, true));
		return ps.isEmpty() ? null : ps.get(0);
	}

	@Mock
	public EmsDashboardFavorite getEmsDashboardFavoriteByPK(Long dashboardId, String username)
	{
		System.out.println("[MockDashboardServiceFacade] getEmsDashboardFavoriteByPK called");
		List<EmsDashboardFavorite> ps = this.localFind(EmsDashboardFavorite.class, new EmsDashboardFavoriteSelector(username,
				dashboardId));
		return ps.isEmpty() ? null : ps.get(0);
	}

	@Mock
	public List<EmsDashboard> getEmsDashboardFindAll()
	{
		System.out.println("[MockDashboardServiceFacade] getEmsDashboardFindAll called");
		return this.localFind(EmsDashboard.class, null);
	}

	@Mock
	public EmsDashboardLastAccess getEmsDashboardLastAccessByPK(Long dashboardId, String username)
	{
		System.out.println("[MockDashboardServiceFacade] getEmsDashboardLastAccessByPK called");
		List<EmsDashboardLastAccess> ps = this.localFind(EmsDashboardLastAccess.class, new EmsDashboardLastAccessSelector(
				username, dashboardId));
		return ps.isEmpty() ? null : ps.get(0);
	}

	@Mock
	public EmsPreference getEmsPreference(String username, String prefKey)
	{
		System.out.println("[MockDashboardServiceFacade] getEmsDashboardFindAll called");
		//		List<EmsPreference> st = this.getLocalStorage(EmsPreference.class);
		//		for (EmsPreference p : st) {
		//			if (p.getUserName().equals(username) && p.getPrefKey().equals(prefKey)) {
		//				return FacadeUtil.cloneEmsPreference(p);
		//			}
		//		}
		//		return null;
		List<EmsPreference> ps = this.localFind(EmsPreference.class, new EmsPreferenceSelector(username, prefKey));
		return ps.isEmpty() ? null : ps.get(0);
	}

	@Mock
	public List<EmsPreference> getEmsPreferenceFindAll(String username)
	{
		System.out.println("[MockDashboardServiceFacade] getEmsPreferenceFindAll called");
		//		List<EmsPreference> st = this.getLocalStorage(EmsPreference.class);
		//		ArrayList<EmsPreference> result = new ArrayList<EmsPreference>();
		//		for (EmsPreference p : st) {
		//			if (p.getUserName().equals(username)) {
		//				result.add(FacadeUtil.cloneEmsPreference(p));
		//			}
		//		}
		//		return result;
		return this.localFind(EmsPreference.class, new EmsPreferenceSelector(username));
	}

	@Mock
	public EntityManager getEntityManager()
	{
		System.out.println("[MockDashboardServiceFacade] getEntityManager called");
		//return null;
		return new MockEntityManager();
		/*
		return new MockUp<EntityManager>() {

			@Mock
			public void close()
			{
				System.out.println("[MockEntityManager] close called");
			}

			@Mock
			public Query createNamedQuery(String qlString)
			{
				throw new RuntimeException("Unspported mock call. Please change your case.");
			}

			@Mock
			public Query createNativeQuery(String qlString)
			{
				throw new RuntimeException("Unspported mock call. Please change your case.");
			}

			@SuppressWarnings("rawtypes")
			@Mock
			public Query createNativeQuery(String sqlString, Class resultClass)
			{
				throw new RuntimeException("Unspported mock call. Please change your case.");
			}

			@Mock
			public Query createNativeQuery(String sqlString, String resultClass)
			{
				throw new RuntimeException("Unspported mock call. Please change your case.");
			}

			@Mock
			public Query createQuery(String qlString)
			{
				throw new RuntimeException("Unspported mock call. Please change your case.");
			}

			@Mock
			public void refresh(Object entity)
			{
				throw new RuntimeException("Unspported mock call. Please change your case.");
			}

		}.getMockInstance();*/
		//return null;
	}

	@Mock
	public List<EmsDashboard> getFavoriteEmsDashboards(String username)
	{
		System.out.println("[MockDashboardServiceFacade] getFavoriteEmsDashboards called");
		List<EmsDashboardFavorite> ps = this.localFind(EmsDashboardFavorite.class, new EmsDashboardFavoriteSelector(username,
				null));
		ArrayList<EmsDashboard> result = new ArrayList<EmsDashboard>();
		for (EmsDashboardFavorite p : ps) {
			if (p.getDashboard() != null && p.getDashboard().getDashboardId() != null) {
				EmsDashboard d = getEmsDashboardById(p.getDashboard().getDashboardId());
				if (d != null) {
					result.add(d);
				}
			}
		}
		return result;
	}

	@Mock
	public EmsDashboard mergeEmsDashboard(EmsDashboard emsDashboard)
	{
		System.out.println("[MockDashboardServiceFacade] mergeEmsDashboard called");
		if (emsDashboard.getLastModificationDate() == null) {
			emsDashboard.setLastModificationDate(new Date());
		}
		return this.localMerge(emsDashboard, new EmsDashboardSelector(emsDashboard.getDashboardId(), null, null, null));
	}

	@Mock
	public EmsDashboardFavorite mergeEmsDashboardFavorite(EmsDashboardFavorite emsDashboardFavorite)
	{
		System.out.println("[MockDashboardServiceFacade] mergeEmsDashboardFavorite called");
		return this.localMerge(emsDashboardFavorite, new EmsDashboardFavoriteSelector(emsDashboardFavorite.getUserName(),
				emsDashboardFavorite.getDashboard().getDashboardId()));
	}

	@Mock
	public EmsDashboardLastAccess mergeEmsDashboardLastAccess(EmsDashboardLastAccess emsDashboardLastAccess)
	{
		System.out.println("[MockDashboardServiceFacade] mergeEmsDashboardLastAccess called");
		return this.localMerge(emsDashboardLastAccess, new EmsDashboardLastAccessSelector(emsDashboardLastAccess.getAccessedBy(),
				emsDashboardLastAccess.getDashboardId()));
	}

	@Mock
	public EmsPreference mergeEmsPreference(EmsPreference emsPreference)
	{
		System.out.println("[MockDashboardServiceFacade] mergeEmsPreference called");
		//		List<EmsPreference> st = this.getLocalStorage(EmsPreference.class);
		//		EmsPreference merge = null;
		//		for (EmsPreference p : st) {
		//			if (p.getUserName().equals(emsPreference.getUserName()) && p.getPrefKey().equals(emsPreference.getPrefKey())) {
		//				merge = p;
		//				break;
		//			}
		//		}
		//		if (merge != null) {
		//			st.remove(merge);
		//			st.add(FacadeUtil.cloneEmsPreference(emsPreference));
		//			return FacadeUtil.cloneEmsPreference(emsPreference);
		//		}
		//		return null;
		return this.localMerge(emsPreference, new EmsPreferenceSelector(emsPreference.getUserName(), emsPreference.getPrefKey()));
	}

	@Mock
	public EmsDashboard persistEmsDashboard(EmsDashboard emsDashboard)
	{
		System.out.println("[MockDashboardServiceFacade] persistEmsDashboard called");
		emsDashboard.setDashboardId(newDashboardId());
		if (emsDashboard.getCreationDate() == null) {
			emsDashboard.setCreationDate(new Date());
		}
		return localPersist(emsDashboard);
	}

	@Mock
	public EmsDashboardFavorite persistEmsDashboardFavorite(EmsDashboardFavorite emsDashboardFavorite)
	{
		System.out.println("[MockDashboardServiceFacade] mergeEmsDashboardFavorite called");
		if (emsDashboardFavorite.getCreationDate() == null) {
			emsDashboardFavorite.setCreationDate(new Date());
		}
		return localPersist(emsDashboardFavorite);
	}

	@Mock
	public EmsDashboardLastAccess persistEmsDashboardLastAccess(EmsDashboardLastAccess emsDashboardLastAccess)
	{
		System.out.println("[MockDashboardServiceFacade] persistEmsDashboardLastAccess called");
		if (emsDashboardLastAccess.getAccessDate() == null) {
			emsDashboardLastAccess.setAccessDate(new Date());
		}
		return localPersist(emsDashboardLastAccess);
	}

	@Mock
	public EmsPreference persistEmsPreference(EmsPreference emsPreference)
	{
		System.out.println("[MockDashboardServiceFacade] persistEmsPreference called");
		return localPersist(emsPreference);
	}

	@Mock
	public void removeAllEmsPreferences(String username)
	{
		System.out.println("[MockDashboardServiceFacade] removeAllEmsPreferences called");
		//		List<EmsPreference> st = this.getLocalStorage(EmsPreference.class);
		//		ArrayList<EmsPreference> removed = new ArrayList<EmsPreference>();
		//		for (EmsPreference p : st) {
		//			if (p.getUserName().equals(username)) {
		//				removed.add(p);
		//			}
		//		}
		//
		//		st.removeAll(removed);
		this.localRemove(EmsPreference.class, new EmsPreferenceSelector(username));
	}

	@Mock
	public void removeEmsDashboard(EmsDashboard emsDashboard)
	{
		System.out.println("[MockDashboardServiceFacade] mergeEmsDashboardFavorite called");
		this.localRemove(emsDashboard.getClass(), new EmsDashboardSelector(emsDashboard.getDashboardId(), null, null, null));
	}

	@Mock
	public void removeEmsDashboardFavorite(EmsDashboardFavorite emsDashboardFavorite)
	{
		System.out.println("[MockDashboardServiceFacade] mergeEmsDashboardFavorite called");
		this.localRemove(emsDashboardFavorite.getClass(), new EmsDashboardFavoriteSelector(emsDashboardFavorite.getUserName(),
				emsDashboardFavorite.getDashboard().getDashboardId()));
		//return null;
	}

	@Mock
	public void removeEmsDashboardLastAccess(EmsDashboardLastAccess emsDashboardLastAccess)
	{
		System.out.println("[MockDashboardServiceFacade] mergeEmsDashboardFavorite called");
		this.localRemove(
				emsDashboardLastAccess.getClass(),
				new EmsDashboardLastAccessSelector(emsDashboardLastAccess.getAccessedBy(), emsDashboardLastAccess
						.getDashboardId()));
	}

	@Mock
	public void removeEmsPreference(EmsPreference emsPreference)
	{
		System.out.println("[MockDashboardServiceFacade] mergeEmsDashboardFavorite called");
		//		List<EmsPreference> st = this.getLocalStorage(EmsPreference.class);
		//		ArrayList<EmsPreference> removed = new ArrayList<EmsPreference>();
		//		for (EmsPreference p : st) {
		//			if (emsPreference != null) {
		//				if (emsPreference.getUserName().equals(p.getUserName()) && emsPreference.getPrefKey().equals(p.getPrefKey())) {
		//					removed.add(p);
		//				}
		//			}
		//		}
		//
		//		st.removeAll(removed);
		this.localRemove(emsPreference.getClass(),
				new EmsPreferenceSelector(emsPreference.getUserName(), emsPreference.getPrefKey()));
	}

	@SuppressWarnings("unchecked")
	private <T> T cloneEntity(T c)
	{
		if (EmsPreference.class.equals(c.getClass())) {
			System.out.println("[MockDashboardServiceFacade] cloneEntity: clone EmsPreference");
			return (T) FacadeUtil.cloneEmsPreference((EmsPreference) c);
		}

		if (EmsDashboardLastAccess.class.equals(c.getClass())) {
			System.out.println("[MockDashboardServiceFacade] cloneEntity: clone EmsDashboardLastAccess");
			return (T) FacadeUtil.cloneEmsDashboardLastAccess((EmsDashboardLastAccess) c);
		}

		if (EmsDashboardFavorite.class.equals(c.getClass())) {
			System.out.println("[MockDashboardServiceFacade] cloneEntity: clone EmsDashboardFavorite");
			return (T) FacadeUtil.cloneEmsDashboardFavorite((EmsDashboardFavorite) c);
		}

		if (EmsDashboard.class.equals(c.getClass())) {
			System.out.println("[MockDashboardServiceFacade] cloneEntity: clone EmsDashboard");
			return (T) FacadeUtil.cloneEmsDashboard((EmsDashboard) c);
		}

		throw new RuntimeException("Not clone executed for entity: " + c);
	}

	@SuppressWarnings({ "unchecked" })
	private <T> List<T> getLocalStorage(Class<T> clazz)
	{
		List<T> storage = (List<T>) storages.get(currentTenant).get(clazz);
		return storage == null ? new ArrayList<T>() : storage;
	}

	@SuppressWarnings({ "unchecked" })
	private <T> List<T> getLocalStorage(T entity)
	{
		List<T> storage = (List<T>) storages.get(currentTenant).get(entity.getClass());
		return storage == null ? new ArrayList<T>() : storage;
	}

	private <T> List<T> localFind(Class<T> clazz, EntitySelector<? super T> es)
	{
		List<T> storage = this.getLocalStorage(clazz);
		ArrayList<T> result = new ArrayList<T>();
		for (T entity : storage) {
			if (es == null || es.selectEntity(entity)) {
				result.add(cloneEntity(entity));
			}
		}
		return result;
	}

	@SuppressWarnings("unused")
	private <T> List<T> localFindAll(Class<T> clazz)
	{
		return localFind(clazz, null);
	}

	private <T> T localMerge(T entity, EntitySelector<? super T> es)
	{
		List<T> storage = this.getLocalStorage(entity);
		T result = null;
		for (T e : storage) {
			if (es.selectEntity(e)) {
				result = e;
			}
		}
		if (result != null) {
			storage.remove(result);
			storage.add(cloneEntity(entity));
		}
		return entity;
	}

	@SuppressWarnings({ "unchecked" })
	private <T> T localPersist(T entity)
	{
		if (entity == null) {
			throw new RuntimeException("[MockDashboardServiceFacade] localPersist: null entity");
		}
		List<T> storage = (List<T>) storages.get(currentTenant).get(entity.getClass());
		if (storage == null) {
			storage = new ArrayList<T>();
			storages.get(currentTenant).put(entity.getClass(), storage);
			System.out.println("[MockDashboardServiceFacade] localPersist: storage created for class: " + entity.getClass());
		}
		System.out.println("[MockDashboardServiceFacade] localPersist: persist " + entity);
		storage.add(cloneEntity(entity));
		return entity;
	}

	private <T> void localRemove(Class<T> clazz, EntitySelector<? super T> es)
	{
		List<T> storage = this.getLocalStorage(clazz);
		ArrayList<T> result = new ArrayList<T>();
		for (T entity : storage) {
			if (entity == null) {
				System.out.println("[MockDashboardServiceFacade] localRemove: null entity");
				throw new RuntimeException("[MockDashboardServiceFacade] localRemove: null entity");
			}
			if (entity != null) {
				if (es.selectEntity(entity)) {
					result.add(entity);
				}
			}
		}
		storage.removeAll(result);
	}

	private long newDashboardId()
	{
		return dashboardId++;
	}

}
