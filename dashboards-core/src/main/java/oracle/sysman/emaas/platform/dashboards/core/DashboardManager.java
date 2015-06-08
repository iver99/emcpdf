package oracle.sysman.emaas.platform.dashboards.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.DashboardSameNameException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DashboardNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.TenantWithoutSubscriptionException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.DashboardApplicationType;
import oracle.sysman.emaas.platform.dashboards.core.model.PaginatedDashboards;
import oracle.sysman.emaas.platform.dashboards.core.model.Tile;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.util.AppContext;
import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;
import oracle.sysman.emaas.platform.dashboards.core.util.DateUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardFavorite;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardFavoritePK;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardLastAccess;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardLastAccessPK;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DashboardManager
{
	/**
	 * Returns the singleton instance for dashboard manager
	 *
	 * @return
	 */
	public static DashboardManager getInstance()
	{
		return instance;
	}

	private static final Logger logger = LogManager.getLogger(DashboardManager.class);

	private static DashboardManager instance;

	static {
		instance = new DashboardManager();
	}

	private DashboardManager()
	{
	}

	//	private Map<Tile, EmsDashboardTile> updateDashboardTiles(List<Tile> tiles, EmsDashboard ed) {
	//		Map<Tile, EmsDashboardTile> rows = new HashMap<Tile, EmsDashboardTile>();
	//		// remove deleted tile row in dashboard row first
	//		List<EmsDashboardTile> edtList = ed.getDashboardTileList();
	//		if (edtList != null) {
	//			int edtSize = edtList.size();
	//			for (int i = edtSize - 1; i >= 0; i--) {
	//				EmsDashboardTile edt = edtList.get(i);
	//				boolean isDeleted = true;
	//				for (Tile tile: tiles) {
	//					if (tile.getTileId() != null && tile.getTileId().equals(edt.getTileId())) {
	//						isDeleted = false;
	//						rows.put(tile, edt);
	//						// remove existing props
	//						List<EmsDashboardTileParams> edtpList = edt.getDashboardTileParamsList();
	//						if (edtpList == null)
	//							break;
	//						while (!edt.getDashboardTileParamsList().isEmpty()) {
	//							EmsDashboardTileParams edtp = edt.getDashboardTileParamsList().get(0);
	////							dsf.removeEmsDashboardTileParams(edtp);
	//							edt.getDashboardTileParamsList().remove(edtp);
	////							edt.removeEmsDashboardTileParams(edtp);
	//						}
	//						break;
	//					}
	//				}
	//				if (isDeleted) {
	////					ed.removeEmsDashboardTile(edt);
	//					ed.getDashboardTileList().remove(edt);
	//				}
	//			}
	//		}
	//
	//		for (Tile tile: tiles) {
	//			EmsDashboardTile edt = null;
	//			if (!rows.containsKey(tile)) {
	//				edt = tile.getPersistenceEntity(null);
	//				ed.addEmsDashboardTile(edt);
	//				rows.put(tile, edt);
	////				dsf.persistEntity(edt);
	//			}
	//			else {
	//				edt = rows.get(tile);
	//			}
	//		}
	//		return rows;
	//	}

	/**
	 * Adds a dashboard as favorite
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @throws DashboardNotFoundException
	 */
	public void addFavoriteDashboard(Long dashboardId, Long tenantId) throws DashboardException
	{
		if (dashboardId == null || dashboardId <= 0) {
			throw new DashboardNotFoundException();
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null || ed.getDeleted() != null && ed.getDeleted() > 0) {
				logger.debug("Dashboard with id {} and tenant id {} is not found, or deleted already", dashboardId, tenantId);
				throw new DashboardNotFoundException();
			}
			if (!isDashboardAccessbyCurrentTenant(ed)) {// system dashboard
				logger.debug(
						"Dashboard with id {} and tenant id {} is a system dashboard and cannot be accessed by current tenant",
						dashboardId, tenantId);
				throw new DashboardNotFoundException();
			}
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			EmsDashboardFavoritePK edfpk = new EmsDashboardFavoritePK(currentUser, dashboardId);
			EmsDashboardFavorite edf = em.find(EmsDashboardFavorite.class, edfpk);
			if (edf == null) {
				edf = new EmsDashboardFavorite(DateUtil.getCurrentUTCTime(), ed, currentUser);
				dsf.persistEmsDashboardFavorite(edf);
			}
			//			else {
			//				//				edf.setCreationDate(DateUtil.getCurrentUTCTime());
			//				dsf.mergeEmsDashboardFavorite(edf);
			//			}
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Delete a dashboard specified by dashboard id for given tenant.
	 *
	 * @param dashboardId
	 *            id for the dashboard
	 * @param permanent
	 *            delete permanently or not
	 * @throws DashboardException
	 */
	public void deleteDashboard(Long dashboardId, boolean permanent, Long tenantId) throws DashboardException
	{
		if (dashboardId == null || dashboardId <= 0) {
			return;
		}
		DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
		EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
		if (ed == null) {
			throw new DashboardNotFoundException();
		}
		if (permanent == false && ed.getDeleted() != null && ed.getDeleted() > 0) {
			throw new DashboardNotFoundException();
		}
		if (!permanent && DataFormatUtils.integer2Boolean(ed.getIsSystem())) {
			throw new CommonSecurityException(
					MessageUtils.getDefaultBundleString(CommonSecurityException.NOT_SUPPORT_DELETE_SYSTEM_DASHBOARD_ERROR));
		}
		String currentUser = UserContext.getCurrentUser();
		// user can access owned or system dashboard
		if (!currentUser.equals(ed.getOwner()) && ed.getIsSystem() != 1) {
			throw new DashboardNotFoundException();
		}
		if (ed.getDeleted() == null || ed.getDeleted() == 0) {
			removeFavoriteDashboard(dashboardId, tenantId);
		}
		if (!permanent) {
			ed.setDeleted(dashboardId);
			dsf.mergeEmsDashboard(ed);
		}
		else {
			EmsDashboardLastAccess edla = getLastAccess(dashboardId, tenantId);
			if (edla != null) {
				dsf.removeEmsDashboardLastAccess(edla);
			}
			dsf.removeEmsDashboard(ed);
		}
	}

	/**
	 * Delete a dashboard specified by dashboard id for given tenant. Soft deletion is supported
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @throws DashboardNotFoundException
	 */
	public void deleteDashboard(Long dashboardId, Long tenantId) throws DashboardException
	{
		deleteDashboard(dashboardId, false, tenantId);
	}

	public String getDashboardBase64ScreenShotById(Long dashboardId, Long tenantId) throws DashboardException
	{
		EntityManager em = null;
		try {
			if (dashboardId == null || dashboardId <= 0) {
				throw new DashboardNotFoundException();
			}
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null) {
				throw new DashboardNotFoundException();
			}
			Boolean isDeleted = ed.getDeleted() == null ? null : ed.getDeleted() > 0;
			if (isDeleted != null && isDeleted.booleanValue()) {
				throw new DashboardNotFoundException();
			}
			String currentUser = UserContext.getCurrentUser();
			if (!currentUser.equals(ed.getOwner()) && ed.getIsSystem() != 1) {
				throw new DashboardNotFoundException();
			}
			if (!isDashboardAccessbyCurrentTenant(ed)) {
				throw new DashboardNotFoundException();
			}
			return ed.getScreenShot();
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Returns dashboard instance by specifying the id
	 *
	 * @param dashboardId
	 * @return
	 * @throws DashboardException
	 */
	public Dashboard getDashboardById(Long dashboardId, Long tenantId) throws DashboardException
	{
		EntityManager em = null;
		try {
			if (dashboardId == null || dashboardId <= 0) {
				logger.debug("Dashboard not found for id {} is invalid", dashboardId);
				throw new DashboardNotFoundException();
			}
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null) {
				logger.debug("Dashboard not found with the specified id {}", dashboardId);
				throw new DashboardNotFoundException();
			}
			Boolean isDeleted = ed.getDeleted() == null ? null : ed.getDeleted() > 0;
			if (isDeleted != null && isDeleted.booleanValue()) {
				logger.debug("Dashboard with id {} is not found for it's deleted already", dashboardId);
				throw new DashboardNotFoundException();
			}
			String currentUser = UserContext.getCurrentUser();
			// user can access owned or system dashboard
			if (!currentUser.equals(ed.getOwner()) && ed.getIsSystem() != 1) {
				logger.debug("Dashboard with id {} is not found for it's a non-OOB dashboard and not owned by current user {}",
						dashboardId, currentUser);
				throw new DashboardNotFoundException();
			}
			if (!isDashboardAccessbyCurrentTenant(ed)) {
				logger.debug("Dashboard with id {} is not found for it can't be accessed by current tenant", dashboardId);
				throw new DashboardNotFoundException();
			}
			updateLastAccessDate(dashboardId, tenantId, dsf);
			return Dashboard.valueOf(ed);
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Returns dashboard instance specified by name for current user Please note that same user under single tenant can't have
	 * more than one dashboards with same name, so this method return single dashboard instance
	 */
	public Dashboard getDashboardByName(String name, Long tenantId)
	{
		if (name == null || "".equals(name)) {
			logger.debug("Dashboard not found for name \"{}\" is invalid", name);
			return null;
		}
		String currentUser = UserContext.getCurrentUser();
		String jpql = "select d from EmsDashboard d where d.name = ?1 and d.owner = ?2 and d.deleted = ?3";
		Object[] params = new Object[] { name, currentUser, new Integer(0) };
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			Query query = em.createQuery(jpql);
			for (int i = 1; i <= params.length; i++) {
				query.setParameter(i, params[i - 1]);
			}
			EmsDashboard ed = (EmsDashboard) query.getSingleResult();
			return Dashboard.valueOf(ed);
		}
		catch (NoResultException e) {
			logger.debug("Dashboard not found for name \"{}\" because NoResultException is caught", name);
			return null;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Returns a list of all favorite dashboards for current user
	 *
	 * @param tenantId
	 * @return
	 */
	public List<Dashboard> getFavoriteDashboards(Long tenantId)
	{
		String currentUser = UserContext.getCurrentUser();
		String hql = "select d from EmsDashboard d join EmsDashboardFavorite f on d.dashboardId = f.dashboard.dashboardId and f.userName = '"
				+ currentUser + "'";
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			Query query = em.createQuery(hql);
			@SuppressWarnings("unchecked")
			List<EmsDashboard> edList = query.getResultList();
			List<Dashboard> dbdList = new ArrayList<Dashboard>(edList.size());
			for (EmsDashboard ed : edList) {
				dbdList.add(Dashboard.valueOf(ed));
			}
			return dbdList;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Retrieves last access for specified dashboard
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @return
	 */
	public EmsDashboardLastAccess getLastAccess(Long dashboardId, Long tenantId)
	{
		if (dashboardId == null || dashboardId <= 0) {
			return null;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null || ed.getDeleted() != null && ed.getDeleted().equals(1)) {
				return null;
			}
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			EmsDashboardLastAccessPK edlapk = new EmsDashboardLastAccessPK(currentUser, dashboardId);
			EmsDashboardLastAccess edla = em.find(EmsDashboardLastAccess.class, edlapk);
			return edla;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Retrieves last access date for specified dashboard
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @return
	 */
	public Date getLastAccessDate(Long dashboardId, Long tenantId)
	{
		EmsDashboardLastAccess edla = getLastAccess(dashboardId, tenantId);
		if (edla != null) {
			return edla.getAccessDate();
		}
		return null;
	}

	/**
	 * Check if the dashboard with spacified id is favorite dashboard or not
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @return
	 * @throws DashboardException
	 */
	public boolean isDashboardFavorite(Long dashboardId, Long tenantId) throws DashboardException
	{
		if (dashboardId == null || dashboardId <= 0) {
			throw new DashboardNotFoundException();
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			EmsDashboardFavoritePK edfpk = new EmsDashboardFavoritePK(currentUser, dashboardId);
			EmsDashboardFavorite edf = em.find(EmsDashboardFavorite.class, edfpk);
			return edf != null;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Returns all dashboards
	 *
	 * @param tenantId
	 * @return
	 */
	public List<Dashboard> listAllDashboards(Long tenantId)
	{
		DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
		List<EmsDashboard> edList = dsf.getEmsDashboardFindAll();
		List<Dashboard> dbdList = new ArrayList<Dashboard>(edList.size());
		for (EmsDashboard ed : edList) {
			dbdList.add(Dashboard.valueOf(ed));
		}
		return dbdList;
	}

	/**
	 * Returns dashboards for specified page with given row offset & page size
	 *
	 * @param offset
	 *            number to indicate row offset, started from 0
	 * @param pageSize
	 * @param tenantId
	 * @param ic
	 *            ignore case or not
	 * @return
	 * @throws DashboardException
	 */
	public PaginatedDashboards listDashboards(Integer offset, Integer pageSize, Long tenantId, boolean ic)
			throws DashboardException
	{
		return listDashboards(null, offset, pageSize, tenantId, ic);
	}

	/**
	 * Returns dashboards for specified query string, by providing page number and page size
	 *
	 * @param queryString
	 * @param offset
	 *            number to indicate row index, started from 0
	 * @param pageSize
	 * @param tenantId
	 * @param ic
	 *            ignore case or not
	 * @return
	 */
	public PaginatedDashboards listDashboards(String queryString, final Integer offset, Integer pageSize, Long tenantId,
			boolean ic) throws DashboardException
	{
		return listDashboards(queryString, offset, pageSize, tenantId, ic, null, null);
	}

	/**
	 * Returns dashboards for specified query string, by providing page number and page size
	 *
	 * @param queryString
	 * @param offset
	 *            number to indicate row index, started from 0
	 * @param pageSize
	 * @param tenantId
	 * @param ic
	 *            ignore case or not
	 * @return
	 */
	public PaginatedDashboards listDashboards(String queryString, final Integer offset, Integer pageSize, Long tenantId,
			boolean ic, String orderBy, DashboardsFilter filter) throws DashboardException
	{
		if (offset != null && offset < 0) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_QUERY_INVALID_OFFSET));
		}
		int firstResult = 0;
		if (offset != null) {
			firstResult = offset.intValue();
		}

		if (pageSize != null && pageSize <= 0) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_QUERY_INVALID_LIMIT));
		}
		int maxResults = DashboardConstants.DASHBOARD_QUERY_DEFAULT_LIMIT;
		if (pageSize != null) {
			maxResults = pageSize.intValue();
		}

		List<DashboardApplicationType> apps = getTenantApplications();
		if (apps == null || apps.isEmpty()) {
			throw new TenantWithoutSubscriptionException();
		}
		/*
		if (filter != null) {
			if (filter.getIncludedApplicationTypes() != null && !filter.getIncludedApplicationTypes().isEmpty()) {
				List<DashboardApplicationType> filteredTypes = new ArrayList<DashboardApplicationType>();
				for (DashboardApplicationType type : apps) {
					if (!filter.getIncludedApplicationTypes().contains(type)) {
						filteredTypes.add(type);
					}
				}

				for (DashboardApplicationType type : filteredTypes) {
					apps.remove(type);
				}
			}
		}*/

		StringBuilder sb = null;
		int index = 1;
		if (apps.isEmpty()) {
			// no subscribe apps
			sb = new StringBuilder(
					" from Ems_Dashboard p left join (select lae.dashboard_Id, lae.access_Date from Ems_Dashboard d, Ems_Dashboard_Last_Access lae "
							+ "where d.dashboard_Id=lae.dashboard_Id and lae.accessed_By=?1 and d.tenant_Id=?2 and lae.tenant_Id=d.tenant_id) le on p.dashboard_Id=le.dashboard_Id "
							+ "where p.dashboard_Id not in (11,12,13) and p.deleted = 0 and p.tenant_Id = ?3 and p.owner = ?4 ");
			index = 5;
		}
		else {
			StringBuilder sbApps = new StringBuilder();
			for (int i = 0; i < apps.size(); i++) {
				DashboardApplicationType app = apps.get(i);
				if (i != 0) {
					sbApps.append(",");
				}
				sbApps.append(String.valueOf(app.getValue()));
			}

			//11,12,13 are id for OOB ITA worksheet, hide them as requested and will recover later upon request
			sb = new StringBuilder(
					" from Ems_Dashboard p left join (select lae.dashboard_Id, lae.access_Date from Ems_Dashboard d, Ems_Dashboard_Last_Access lae "
							+ "where d.dashboard_Id=lae.dashboard_Id and lae.accessed_By=?1 and d.tenant_Id=?2 and lae.tenant_Id=d.tenant_id) le on p.dashboard_Id=le.dashboard_Id "
							+ "where p.dashboard_Id not in (11,12,13) and p.deleted = 0 and p.tenant_Id = ?3 and (p.owner = ?4 or (p.is_system = ?5 and p.application_type in ("
							+ sbApps.toString() + "))) ");
			index = 6;
		}
		List<Object> paramList = new ArrayList<Object>();
		String currentUser = UserContext.getCurrentUser();
		paramList.add(currentUser);
		paramList.add(tenantId);
		paramList.add(tenantId);
		paramList.add(currentUser);
		if (!apps.isEmpty()) {
			paramList.add(1);
		}

		if (filter != null) {
			if (filter.getIncludedTypeIntegers() != null && !filter.getIncludedTypeIntegers().isEmpty()) {
				sb.append(" and ( ");
				for (int i = 0; i < filter.getIncludedTypeIntegers().size(); i++) {
					if (i != 0) {
						sb.append(" or ");
					}
					sb.append(" p.type = ?" + index++);
					paramList.add(filter.getIncludedTypeIntegers().get(i));
				}
				sb.append(" ) ");
			}

			if (filter.getIncludedApplicationTypes() != null && !filter.getIncludedApplicationTypes().isEmpty()) {
				sb.append(" and ( ");
				for (int i = 0; i < filter.getIncludedApplicationTypes().size(); i++) {
					if (i != 0) {
						sb.append(" or ");
					}
					sb.append(" p.application_type = " + filter.getIncludedApplicationTypes().get(i).getValue() + " ");
					//paramList.add(filter.getIncludedApplicationTypes().get(i).getValue());
				}
				sb.append(" or p.is_system < 1 ");
				sb.append(" or p.dashboard_Id in (select t.dashboard_Id from Ems_Dashboard_Tile t where t.PROVIDER_NAME in ("
						+ filter.getIncludedWidgetProvidersString() + " )) ");
				sb.append(" ) ");
			}

			if (filter.getIncludedOwners() != null && !filter.getIncludedOwners().isEmpty()) {
				sb.append(" and ( ");
				if (filter.getIncludedOwners().contains("Oracle")) {
					sb.append(" p.owner = ?" + index++);
					paramList.add("Oracle");
				}
				if (filter.getIncludedOwners().contains("Others")) {
					if (filter.getIncludedOwners().contains("Oracle")) {
						sb.append(" or ");
					}
					sb.append(" p.owner != ?" + index++);
					paramList.add("Oracle");
				}
				/*
				for (int i = 0; i < filter.getIncludedOwners().size(); i++) {
					if (i != 0) {
						sb.append(" or ");
					}
					sb.append(" p.owner = ?" + index++);
					paramList.add(filter.getIncludedOwners().get(i));
					}
				 */

				sb.append(" ) ");
			}
		}

		if (queryString != null && !"".equals(queryString)) {
			Locale locale = AppContext.getInstance().getLocale();
			if (!ic) {
				sb.append(" and (p.name LIKE ?" + index++);
				paramList.add("%" + queryString + "%");
			}
			else {
				sb.append(" and (lower(p.name) LIKE ?" + index++);
				paramList.add("%" + queryString.toLowerCase(locale) + "%");
			}

			if (!ic) {
				sb.append(" or p.description like ?" + index++);
				paramList.add("%" + queryString + "%");
			}
			else {
				sb.append(" or lower(p.description) like ?" + index++);
				paramList.add("%" + queryString.toLowerCase(locale) + "%");
			}

			if (!ic) {
				sb.append(" or p.dashboard_Id in (select t.dashboard_Id from Ems_Dashboard_Tile t where t.title like ?" + index++
						+ " ) ");
				paramList.add("%" + queryString + "%");
			}
			else {
				sb.append(" or p.dashboard_Id in (select t.dashboard_Id from Ems_Dashboard_Tile t where lower(t.title) like ?"
						+ index++ + " ) ");
				paramList.add("%" + queryString.toLowerCase(locale) + "%");
			}

			if (!ic) {
				sb.append(" or p.dashboard_Id in (select t.dashboard_Id from Ems_Dashboard_Tile t where t.title like ?9 )) ");
				paramList.add("%" + queryString + "%");
			}
			else {
				sb.append(" or p.dashboard_Id in (select t.dashboard_Id from Ems_Dashboard_Tile t where lower(t.title) like ?9 )) ");
				paramList.add("%" + queryString.toLowerCase(locale) + "%");
			}

			//			sb.append(" or lower(p.owner) = :owner)");
			//			paramMap.put("owner", queryString.toLowerCase(locale));
			//			sb.append(" and p.deleted = 0 ");
		}

		if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_NAME.equals(orderBy)) {
			sb.append(" order by lower(p.name), p.name, p.dashboard_Id DESC");
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_CREATE_TIME.equals(orderBy)) {
			sb.append(" order by CASE WHEN p.creation_Date IS NULL THEN 0 ELSE 1 END DESC, p.creation_Date DESC, p.dashboard_Id DESC");
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME.equals(orderBy)) {
			sb.append(" order by CASE WHEN le.access_Date IS NULL THEN 0 ELSE 1 END DESC, le.access_Date DESC, p.dashboard_Id DESC");
		}
		else {
			//order by last access date
			sb.append(" order by p.application_Type, lower(p.name), p.name, CASE WHEN le.access_Date IS NULL THEN 0 ELSE 1 END DESC, le.access_Date DESC");
		}
		StringBuilder sbQuery = new StringBuilder(sb);
		sbQuery.insert(0, "select p.* ");
		String jpqlQuery = sbQuery.toString();
		logger.debug(jpqlQuery);
		DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
		EntityManager em = dsf.getEntityManager();
		Query listQuery = em.createNativeQuery(jpqlQuery, EmsDashboard.class);
		initializeQueryParams(listQuery, paramList);
		listQuery.setFirstResult(firstResult);
		listQuery.setMaxResults(maxResults);
		@SuppressWarnings("unchecked")
		List<EmsDashboard> edList = listQuery.getResultList();
		List<Dashboard> dbdList = new ArrayList<Dashboard>(edList.size());
		for (EmsDashboard ed : edList) {
			dbdList.add(Dashboard.valueOf(ed));
		}

		StringBuilder sbCount = new StringBuilder(sb);
		sbCount.insert(0, "select count(*) ");
		String jpqlCount = sbCount.toString();
		logger.debug(jpqlCount);
		Query countQuery = em.createNativeQuery(jpqlCount);
		initializeQueryParams(countQuery, paramList);
		Long totalResults = ((BigDecimal) countQuery.getSingleResult()).longValue();
		PaginatedDashboards pd = new PaginatedDashboards(totalResults, firstResult, dbdList == null ? 0 : dbdList.size(),
				maxResults, dbdList);
		return pd;
	}

	/**
	 * Removes a dashboard from favorite list
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @throws DashboardNotFoundException
	 */
	public void removeFavoriteDashboard(Long dashboardId, Long tenantId) throws DashboardNotFoundException
	{
		if (dashboardId == null || dashboardId <= 0) {
			throw new DashboardNotFoundException();
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null || ed.getDeleted() != null && ed.getDeleted() > 0) {
				logger.debug("Dashboard with id {} is not found for it does not exists or is deleted already", dashboardId);
				throw new DashboardNotFoundException();
			}
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			EmsDashboardFavoritePK edfpk = new EmsDashboardFavoritePK(currentUser, dashboardId);
			EmsDashboardFavorite edf = em.find(EmsDashboardFavorite.class, edfpk);
			if (edf != null) {
				dsf.removeEmsDashboardFavorite(edf);
			}
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Save a newly created dashboard for given tenant
	 *
	 * @param dbd
	 * @param tenantId
	 * @return the dashboard saved
	 */
	public Dashboard saveNewDashboard(Dashboard dbd, Long tenantId) throws DashboardException
	{
		if (dbd == null) {
			return null;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			if (dbd.getDashboardId() != null) {
				EmsDashboard sameId = dsf.getEmsDashboardById(dbd.getDashboardId());
				if (sameId != null && sameId.getDeleted() <= 0) {
					throw new CommonFunctionalException(
							MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_CREATE_SAME_ID_ERROR));
				}
			}
			//check dashboard name
			if (dbd.getName() == null || dbd.getName().trim() == "" || dbd.getName().length() > 64) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_INVALID_NAME_ERROR));
			}
			Dashboard sameName = getDashboardByName(dbd.getName(), tenantId);
			if (sameName != null && !sameName.getDashboardId().equals(dbd.getDashboardId())) {
				throw new DashboardSameNameException();
			}
			// init creation date, owner to prevent null insertion
			Date created = DateUtil.getCurrentUTCTime();
			if (dbd.getCreationDate() == null) {
				dbd.setCreationDate(created);
			}
			if (dbd.getOwner() == null) {
				dbd.setOwner(currentUser);
			}
			if (dbd.getTileList() != null) {
				for (Tile tile : dbd.getTileList()) {
					if (tile.getCreationDate() == null) {
						tile.setCreationDate(created);
					}
					if (tile.getOwner() == null) {
						tile.setOwner(currentUser);
					}
				}
			}
			EmsDashboard ed = dbd.getPersistenceEntity(null);
			ed.setCreationDate(dbd.getCreationDate());
			ed.setOwner(currentUser);
			dsf.persistEmsDashboard(ed);
			updateLastAccessDate(ed.getDashboardId(), tenantId);
			return Dashboard.valueOf(ed, dbd);
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Enables or disables the 'include time control' settings for specified dashboard
	 *
	 * @param dashboardId
	 * @param enable
	 * @param tenantId
	 */
	public void setDashboardIncludeTimeControl(Long dashboardId, boolean enable, Long tenantId)
	{
		if (dashboardId == null || dashboardId <= 0) {
			return;
		}
		DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
		EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
		if (ed == null) {
			return;
		}
		ed.setEnableTimeRange(DataFormatUtils.boolean2Integer(enable));
		dsf.mergeEmsDashboard(ed);
	}

	/**
	 * Update an existing dashboard for given tenant
	 *
	 * @param dbd
	 * @param tenantId
	 * @return the dashboard saved or updated
	 */
	public Dashboard updateDashboard(Dashboard dbd, Long tenantId) throws DashboardException
	{
		if (dbd == null) {
			return null;
		}
		EntityManager em = null;
		EmsDashboard ed = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			Dashboard sameName = getDashboardByName(dbd.getName(), tenantId);
			if (sameName != null && !sameName.getDashboardId().equals(dbd.getDashboardId())) {
				throw new DashboardSameNameException();
			}
			// init creation date, owner to prevent null insertion
			Date created = DateUtil.getCurrentUTCTime();
			//			if (dbd.getCreationDate() == null) {
			//				dbd.setCreationDate(created);
			//			}
			if (dbd.getOwner() == null) {
				dbd.setOwner(currentUser);
			}
			if (dbd.getTileList() != null) {
				for (Tile tile : dbd.getTileList()) {
					if (tile.getCreationDate() == null) {
						tile.setCreationDate(created);
					}
					if (tile.getOwner() == null) {
						tile.setOwner(currentUser);
					}
				}
			}

			ed = dsf.getEmsDashboardById(dbd.getDashboardId());
			if (ed == null) {
				throw new DashboardNotFoundException();
			}
			if (DataFormatUtils.integer2Boolean(ed.getIsSystem())) {
				throw new CommonSecurityException(
						MessageUtils.getDefaultBundleString(CommonSecurityException.NOT_SUPPORT_UPDATE_SYSTEM_DASHBOARD_ERROR));
			}
			ed = dbd.getPersistenceEntity(ed);
			ed.setLastModificationDate(DateUtil.getCurrentUTCTime());
			ed.setLastModifiedBy(currentUser);
			if (dbd.getOwner() != null) {
				ed.setOwner(dbd.getOwner());
			}
			dsf.mergeEmsDashboard(ed);
			return Dashboard.valueOf(ed, dbd);
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Updates last access date for specified dashboard
	 *
	 * @param dashboardId
	 * @param tenantId
	 */
	public void updateLastAccessDate(Long dashboardId, Long tenantId)
	{
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			updateLastAccessDate(dashboardId, tenantId, dsf);
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void updateLastAccessDate(Long dashboardId, Long tenantId, DashboardServiceFacade dsf)
	{
		if (dashboardId == null || dashboardId <= 0) {
			return;
		}
		EntityManager em = null;
		EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
		if (ed == null || ed.getDeleted() != null && ed.getDeleted().equals(1)) {
			return;
		}
		em = dsf.getEntityManager();
		String currentUser = UserContext.getCurrentUser();
		EmsDashboardLastAccessPK edlapk = new EmsDashboardLastAccessPK(currentUser, dashboardId);
		EmsDashboardLastAccess edla = em.find(EmsDashboardLastAccess.class, edlapk);
		if (edla == null) {
			edla = new EmsDashboardLastAccess(DateUtil.getCurrentUTCTime(), currentUser, dashboardId);
			dsf.persistEmsDashboardLastAccess(edla);
		}
		else {
			edla.setAccessDate(DateUtil.getCurrentUTCTime());
			dsf.mergeEmsDashboardLastAccess(edla);
		}
	}

	private List<DashboardApplicationType> getTenantApplications()
	{
		String opcTenantId = TenantContext.getCurrentTenant();
		if (opcTenantId == null || "".equals(opcTenantId)) {
			logger.warn("When trying to retrieve subscribed application, it's found the tenant context is not set (TenantContext.getCurrentTenant() == null)");
			return null;
		}
		List<String> appNames = TenantSubscriptionUtil.getTenantSubscribedServices(opcTenantId);
		if (appNames == null || appNames.isEmpty()) {
			return null;
		}
		List<DashboardApplicationType> apps = new ArrayList<DashboardApplicationType>();
		for (String appName : appNames) {
			DashboardApplicationType dat = DashboardApplicationType.fromJsonValue(appName);
			apps.add(dat);
		}
		return apps;
	}

	private void initializeQueryParams(Query query, List<Object> paramList)
	{
		if (query == null || paramList == null) {
			return;
		}
		for (int i = 0; i < paramList.size(); i++) {
			Object value = paramList.get(i);
			query.setParameter(i + 1, value);
			logger.debug("binding parameter [{}] as [{}]", i + 1, value);
		}
	}

	private boolean isDashboardAccessbyCurrentTenant(EmsDashboard ed) throws TenantWithoutSubscriptionException
	{
		if (ed == null) {
			return false;
		}
		List<DashboardApplicationType> datList = getTenantApplications();
		if (datList == null || datList.isEmpty()) { // accessible app list is empty
			throw new TenantWithoutSubscriptionException();
		}
		Boolean isSystem = DataFormatUtils.integer2Boolean(ed.getIsSystem());
		if (!isSystem) { // check system dashboard only
			return true;
		}
		Integer at = ed.getApplicationType();
		if (at == null) { // should be always available for system dashboard
			logger.error("Unexpected: application type for system dashboard with id {} is null", ed.getDashboardId());
			return false;
		}
		DashboardApplicationType app = DashboardApplicationType.fromValue(at.intValue());
		if (app == null) {
			logger.debug("Failed to retrieve a valid DashboardApplicationType from given application type internal value {}", at);
			return false;
		}
		for (DashboardApplicationType dat : datList) {
			if (dat.equals(app)) {
				return true;
			}
		}
		logger.debug("dashboard can't be accessed by current tenant as it's application type isn't in the subscribed application list");
		return false;
	}
}
