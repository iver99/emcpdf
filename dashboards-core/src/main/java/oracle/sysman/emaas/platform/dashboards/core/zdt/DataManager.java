/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.zdt;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.util.StringUtil;

import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.zdt.exception.HalfSyncException;
import oracle.sysman.emaas.platform.dashboards.core.zdt.exception.NoComparedResultException;
import oracle.sysman.emaas.platform.dashboards.core.zdt.exception.SyncException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.config.ResultType;

/**
 * @author guochen
 */
public class DataManager
{
	private static final Logger logger = LogManager.getLogger(DataManager.class);

	private static final String SQL_INSERT_TO_ZDT_COMPARISON_TABLE = "insert into ems_zdt_comparator (comparison_date,next_schedule_comparison_date, comparison_type, comparison_result, divergence_percentage) "
			+ "values (to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), ?, ?, ?)";
	
	private static final String SQL_GET_COMPARISON_STATUS = "select * from (SELECT to_char(COMPARISON_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as COMPARISON_DATE, to_char(NEXT_SCHEDULE_COMPARISON_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as NEXT_SCHEDULE_COMPARISON_DATE,COMPARISON_TYPE, divergence_percentage "
			+ "from ems_zdt_comparator order by comparison_date desc) where rownum = 1";
	
	private static final String SQL_INSERT_TO_ZDT_SYNC_TABLE = "insert into ems_zdt_sync (sync_date,next_schedule_sync_date, sync_type, sync_result, divergence_percentage, LAST_COMPARISON_DATE) "
			+ "values (to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), ?, ?, ?, to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'))";

	private static final String SQL_GET_LAST_COMPARISON_DATE_FOR_SYNC = "SELECT * FROM (SELECT to_char(LAST_COMPARISON_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as LAST_COMPARISON_DATE FROM EMS_ZDT_SYNC WHERE SYNC_RESULT = 'SUCCESSFUL' ORDER BY SYNC_DATE DESC) WHERE ROWNUM = 1";
	
	private static final String SQL_GET_LATEST_COMPARISON_DATE = "SELECT * FROM (SELECT to_char(COMPARISON_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as COMPARISON_DATE FROM EMS_ZDT_COMPARATOR WHERE COMPARISON_RESULT IS NOT NULL ORDER BY COMPARISON_DATE DESC) WHERE ROWNUM = 1";
	
	private static final String SQL_GET_SYNC_STATUS = "select * from (SELECT to_char(SYNC_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as SYNC_DATE, to_char(NEXT_SCHEDULE_SYNC_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as NEXT_SCHEDULE_SYNC_DATE,SYNC_TYPE, divergence_percentage from ems_zdt_sync order by sync_date desc) where rownum = 1";
	
	private static final String SQL_GET_COMPARED_DATA_TO_SYNC_BY_DATE = "SELECT to_char(COMPARISON_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as COMPARISON_DATE, comparison_result from ems_zdt_comparator where comparison_date > to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') and comparison_result is not null";
	
	private static final String SQL_GET_COMPARED_DATA_TO_SYNC = "SELECT to_char(COMPARISON_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as COMPARISON_DATE, comparison_result from ems_zdt_comparator where comparison_result is not null";
	
	private static final String SQL_GET_ALL_TENANTS = "select distinct tenant_id from ems_dashboard where is_system <>1";

	private static final String SQL_GET_HALF_SYNC_RECORD = "SELECT * FROM EMS_ZDT_SYNC WHERE SYNC_TYPE = 'half'";

	private static final String SQL_GET_HALF_SYNC_COMPARED_DATA_TO_SYNC = "SELECT comparison_result from EMS_ZDT_COMPARATOR where comparison_date = to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')";

	private static DataManager instance = new DataManager();
	/**
	 * Returns the singleton instance for zdt data manager
	 *
	 * @return
	 */
	public static DataManager getInstance()
	{
		return instance;
	}
	
	public List<Object> getAllTenants(EntityManager em) {
		List<Object> result =  getSingleTableData(em, SQL_GET_ALL_TENANTS);
		return result;
	}
	
	public int saveToComparatorTable(EntityManager em, String comparisonDate, String nextCompareDate, String comparisonType,
			String comparisonResult, double divergencePercentage) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		try {
			em.createNativeQuery(SQL_INSERT_TO_ZDT_COMPARISON_TABLE).setParameter(1, comparisonDate).setParameter(2, nextCompareDate).setParameter(3, comparisonType)
			.setParameter(4, comparisonResult).setParameter(5, divergencePercentage).executeUpdate();
			em.getTransaction().commit();
			return 0;
		} catch (Exception e) {
			logger.error("errors occurs in saveToComparatorTalbe, "+e.getLocalizedMessage());
			return -1;
		}
	}
	
	public int saveToSyncTable(String syncDate, String nextSyncDate, String SyncType, 
			String syncResult, double divergencePercentage, String lastComparisonDate) {
		EntityManager em = new DashboardServiceFacade().getEntityManager();
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		try {
			em.createNativeQuery(SQL_INSERT_TO_ZDT_SYNC_TABLE).setParameter(1, syncDate).setParameter(2, nextSyncDate).setParameter(3, SyncType)
			.setParameter(4, syncResult).setParameter(5, divergencePercentage).setParameter(6, lastComparisonDate).executeUpdate();
			em.getTransaction().commit();
			return 0;
		} catch (Exception e) {
			logger.error("errors occurs in saveToComparatorTalbe, ", e.getLocalizedMessage());
			return -1;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}
	
	public List<Map<String, Object>> getComparedDataToSync(EntityManager em, String date) {		
		List<Map<String, Object>> result = null;
		try {
			if (date != null) {
				result = getDatabaseTableData(em,SQL_GET_COMPARED_DATA_TO_SYNC_BY_DATE, date,null,null);				
			} else {
				result = getDatabaseTableData(em,SQL_GET_COMPARED_DATA_TO_SYNC,null, null, null);
			}
			
		}catch(Exception e) {
			logger.error(e);
		}
		return result;
	} 
	
	public String getLastComparisonDateForSync(EntityManager em) {
		List<Object> result = getSingleTableData(em,SQL_GET_LAST_COMPARISON_DATE_FOR_SYNC);
		if (result != null && result.size() == 1) {
			return (String)result.get(0);
		}
		return null;
	}
	
	public String getLatestComparisonDateForCompare(EntityManager em) {
		List<Object> result = getSingleTableData(em,SQL_GET_LATEST_COMPARISON_DATE);
		if (result != null && result.size() == 1) {
			return (String)result.get(0);
		}
		return null;
	}
	
	public List<Map<String, Object>> getSyncStatus(EntityManager em) {
		try {
			List<Map<String, Object>> result = getDatabaseTableData(em, SQL_GET_SYNC_STATUS, null,null, null);
			return result;
		} catch (Exception e) {
			logger.error(e);
		} 
		return null;
	}
 	
	public List<Map<String, Object>> getComparatorStatus(EntityManager em) {
		try {
			List<Map<String, Object>> result = getDatabaseTableData(em, SQL_GET_COMPARISON_STATUS, null, null, null);
			return result;
		} catch (Exception e) {
			logger.error(e);
		} 
		return null;
	}
	
	private List<Object> getSingleTableData(EntityManager em, String nativeSql) {
		if (StringUtil.isEmpty(nativeSql)) {
			logger.error("can not query database with empty sql statement!");
			return null;
		}
		List<Object>  result = null;
		try {
			Query query = em.createNativeQuery(nativeSql);
			result = query.getResultList();
		}catch(Exception e) {
			logger.error(e);
			logger.error("error occurs while executing sql:" + nativeSql);
		}
		return result;
	}


	/**
	 * Retrieves total count for all dashbaord from all tenants
	 *
	 * @return
	 */
	public long getAllDashboardsCount(EntityManager em, String maxComparedDate)
	{
		try {
			String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD where LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')";
			Query query = em.createNativeQuery(sql).setParameter(1, maxComparedDate);
			List<Object> result = query.getResultList();
			if (result != null && result.size() == 1) {
				return ((Number)result.get(0)).longValue();
			}
		}catch(NoResultException e){
			logger.warn("Get all dashboards count did not retrieve any data!");
			return 0L;
		}
		return 0L;
	}

	/**
	 * Retrieves total count for all favorites from all tenants
	 *
	 * @return
	 */
	public long getAllUserOptionsCount(EntityManager em, String maxComparedDate)
	{
		try {
			String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD_USER_OPTIONS where LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')"
					+ " and dashboard_id in (select dashboard_Id from ems_dashboard)";
			Query query = em.createNativeQuery(sql).setParameter(1, maxComparedDate);
			List<Object> result = query.getResultList();
			if (result != null && result.size() == 1) {
				return ((Number)result.get(0)).longValue();
			}
		}catch(NoResultException e){
			logger.warn("Get all user options count did not retrieve any data!");
			return 0L;
		}
		return 0L;
	}
	
	/**
	 * Retrieves total count for all favorites from all tenants
	 *
	 * @return
	 */
	public long getAllTileCount(EntityManager em, String maxComparedDate)
	{
		try {
			String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD_TILE where LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')"
					+ " and dashboard_id in (select dashboard_id from ems_dashboard)";
			Query query = em.createNativeQuery(sql).setParameter(1, maxComparedDate);
			List<Object> result = query.getResultList();
			if (result != null && result.size() == 1) {
				return ((Number)result.get(0)).longValue();
			}
		}catch(NoResultException e){
			logger.warn("Get all tile count did not retrieve any data!");
			return 0L;
		}
		return 0L;
	}
	
	/**
	 * Retrieves total count for all favorites from all tenants
	 *
	 * @return
	 */
	public long getAllDashboardSetCount(EntityManager em, String maxComparedDate)
	{
		try {
			String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD_SET where LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') and"
					+ " dashboard_set_id in (select dashboard_id from ems_dashboard)";
			Query query = em.createNativeQuery(sql).setParameter(1, maxComparedDate);
			List<Object> result = query.getResultList();
			if (result != null && result.size() == 1) {
				return ((Number)result.get(0)).longValue();
			}
		}catch(NoResultException e){
			logger.warn("Get all DASHBOARD SET count did not retrieve any data!");
			return 0L;
		}
		return 0L;
	}
	
	/**
	 * Retrieves total count for all favorites from all tenants
	 *
	 * @return
	 */
	public long getAllTileParamsCount(EntityManager em, String maxComparedDate)
	{
		try {
			String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD_TILE_PARAMS where LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')";
			Query query = em.createNativeQuery(sql).setParameter(1, maxComparedDate);
			List<Object> result = query.getResultList();
			if (result != null && result.size() == 1) {
				return ((Number)result.get(0)).longValue();
			}
		}catch(NoResultException e){
			logger.warn("Get all tile params count did not retrieve any data!");
			return 0L;
		}
		return 0L;
	}

	/**
	 * Retrieves total count for all preferences from all tenants
	 *
	 * @return
	 */
	public long getAllPreferencessCount(EntityManager em,String maxComparedDate)
	{
		try {
			String sql = "SELECT COUNT(1) FROM EMS_PREFERENCE where LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')";
			Query query = em.createNativeQuery(sql).setParameter(1, maxComparedDate);
			List<Object> result = query.getResultList();
			if (result != null && result.size() == 1) {
				return ((Number)result.get(0)).longValue();
			}
		}catch(NoResultException e){
			logger.warn("Get all preference count did not retrieve any data!");
			return 0L;
		}
		return 0L;
	}

	

	/**
	 * Retrieves all dashboard set data rows for all tenant
	 *
	 * @return
	 */
	public List<Map<String, Object>> getDashboardSetTableData(EntityManager em,String type, String lastComparisonDate, String maxComparedDate, String tenant)
	{
		String sql = "SELECT TO_CHAR(DASHBOARD_SET_ID) AS DASHBOARD_SET_ID, TENANT_ID, TO_CHAR(SUB_DASHBOARD_ID) AS SUB_DASHBOARD_ID, "
				+ "POSITION, CREATION_DATE, LAST_MODIFICATION_DATE, TO_CHAR(DELETED) AS DELETED"
				+ " FROM EMS_DASHBOARD_SET where LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')"
				+ " and dashboard_set_id in (select dashboard_id from ems_dashboard where is_system <>1)";
		String sqlByTenant = "SELECT TO_CHAR(DASHBOARD_SET_ID) AS DASHBOARD_SET_ID, TENANT_ID, TO_CHAR(SUB_DASHBOARD_ID) AS SUB_DASHBOARD_ID, "
				+ "POSITION, CREATION_DATE, LAST_MODIFICATION_DATE, TO_CHAR(DELETED) AS DELETED"
				+ " FROM EMS_DASHBOARD_SET where LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')"
				+ " and dashboard_set_id in (select dashboard_id from ems_dashboard where is_system <>1) and tenant_Id =?";
		String sqlByDate = "SELECT TO_CHAR(DASHBOARD_SET_ID) AS DASHBOARD_SET_ID, TENANT_ID, TO_CHAR(SUB_DASHBOARD_ID) AS SUB_DASHBOARD_ID, "
				+ "POSITION, CREATION_DATE, LAST_MODIFICATION_DATE, TO_CHAR(DELETED) AS DELETED"
				+ " FROM EMS_DASHBOARD_SET WHERE LAST_MODIFICATION_DATE > to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') and LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')"
				+ " and dashboard_set_id in (select dashboard_id from ems_dashboard where is_system <>1)";
		return getDBTableRows(em, type, lastComparisonDate, maxComparedDate, tenant, sql, sqlByTenant, sqlByDate);

	}

	/**
	 * Retrieves all dashboard data rows for all tenant
	 *
	 * @return
	 */
	public List<Map<String, Object>> getDashboardTableData(EntityManager em, String type,String lastComparisonDate, String maxComparedDate, String tenant)
	{
		String sql = "SELECT  TO_CHAR(DASHBOARD_ID) AS DASHBOARD_ID,  NAME, TYPE, DESCRIPTION, CREATION_DATE, LAST_MODIFICATION_DATE, LAST_MODIFIED_BY,"
				+ " OWNER, IS_SYSTEM, APPLICATION_TYPE, ENABLE_TIME_RANGE, SCREEN_SHOT, TO_CHAR(DELETED) AS DELETED, TENANT_ID, ENABLE_REFRESH, "
				+ "SHARE_PUBLIC, ENABLE_ENTITY_FILTER, ENABLE_DESCRIPTION, EXTENDED_OPTIONS, SHOW_INHOME FROM EMS_DASHBOARD where LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')"
				+ " and  is_system <> 1";
		String sqlByTenant = "SELECT  TO_CHAR(DASHBOARD_ID) AS DASHBOARD_ID,  NAME, TYPE, DESCRIPTION, CREATION_DATE, LAST_MODIFICATION_DATE, LAST_MODIFIED_BY,"
				+ " OWNER, IS_SYSTEM, APPLICATION_TYPE, ENABLE_TIME_RANGE, SCREEN_SHOT, TO_CHAR(DELETED) AS DELETED, TENANT_ID, ENABLE_REFRESH, "
				+ "SHARE_PUBLIC, ENABLE_ENTITY_FILTER, ENABLE_DESCRIPTION, EXTENDED_OPTIONS, SHOW_INHOME FROM EMS_DASHBOARD where LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')"
				+ " and  is_system <> 1 and tenant_Id = ?";
		String sqlByDate = "SELECT  TO_CHAR(DASHBOARD_ID) AS DASHBOARD_ID,  NAME, TYPE, DESCRIPTION, CREATION_DATE, LAST_MODIFICATION_DATE, LAST_MODIFIED_BY,"
				+ " OWNER, IS_SYSTEM, APPLICATION_TYPE, ENABLE_TIME_RANGE, SCREEN_SHOT, TO_CHAR(DELETED) AS DELETED, TENANT_ID, ENABLE_REFRESH, "
				+ "SHARE_PUBLIC, ENABLE_ENTITY_FILTER, ENABLE_DESCRIPTION, EXTENDED_OPTIONS, SHOW_INHOME FROM EMS_DASHBOARD "
				+ "WHERE LAST_MODIFICATION_DATE > to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') and LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')"
				+ " and  is_system <> 1";
		return getDBTableRows(em, type, lastComparisonDate, maxComparedDate, tenant, sql, sqlByTenant, sqlByDate);
	}

	/**
	 * Retrieves all dashboard tile params data rows for all tenant
	 *
	 * @return
	 */
	public List<Map<String, Object>> getDashboardTileParamsTableData(EntityManager em, String type,String lastComparisonDate, String maxComparedDate, String tenant)
	{
		String sql = "SELECT TILE_ID, PARAM_NAME, TENANT_ID, IS_SYSTEM, PARAM_TYPE, PARAM_VALUE_STR, PARAM_VALUE_NUM, PARAM_VALUE_TIMESTAMP, "
				+ "CREATION_DATE, LAST_MODIFICATION_DATE, DELETED FROM EMS_DASHBOARD_TILE_PARAMS where LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')"
				+ " and is_system <>1";
		String sqlByTenant = "SELECT TILE_ID, PARAM_NAME, TENANT_ID, IS_SYSTEM, PARAM_TYPE, PARAM_VALUE_STR, PARAM_VALUE_NUM, PARAM_VALUE_TIMESTAMP, "
				+ "CREATION_DATE, LAST_MODIFICATION_DATE, DELETED FROM EMS_DASHBOARD_TILE_PARAMS where LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')"
				+ " and is_system <>1 and tenant_Id = ?";
		String sqlByDate = "SELECT TILE_ID, PARAM_NAME, TENANT_ID, IS_SYSTEM, PARAM_TYPE, PARAM_VALUE_STR, PARAM_VALUE_NUM, PARAM_VALUE_TIMESTAMP, "
				+ "CREATION_DATE, LAST_MODIFICATION_DATE, DELETED FROM EMS_DASHBOARD_TILE_PARAMS "
				+ "WHERE LAST_MODIFICATION_DATE > to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') and LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')"
				+ " and is_system <>1";
		return getDBTableRows(em, type, lastComparisonDate, maxComparedDate, tenant, sql, sqlByTenant, sqlByDate);
	}

	/**
	 * Retrieves all dashboard tile data rows for all tenant
	 *
	 * @return
	 */
	public List<Map<String, Object>> getDashboardTileTableData(EntityManager em,String type, String lastComparisonDate, String maxComparedDate, String tenant)
	{
		String sql = "SELECT TILE_ID, TO_CHAR(DASHBOARD_ID) AS DASHBOARD_ID, CREATION_DATE, LAST_MODIFICATION_DATE, LAST_MODIFIED_BY, OWNER, TITLE, HEIGHT, WIDTH, IS_MAXIMIZED, POSITION, TENANT_ID,"
				+ "WIDGET_UNIQUE_ID, WIDGET_NAME, WIDGET_DESCRIPTION, WIDGET_GROUP_NAME, WIDGET_ICON, WIDGET_HISTOGRAM, WIDGET_OWNER, "
				+ "WIDGET_CREATION_TIME, WIDGET_SOURCE, WIDGET_KOC_NAME, WIDGET_VIEWMODE, WIDGET_TEMPLATE, PROVIDER_NAME, PROVIDER_VERSION, PROVIDER_ASSET_ROOT, "
				+ "TILE_ROW, TILE_COLUMN, TYPE, WIDGET_SUPPORT_TIME_CONTROL, WIDGET_LINKED_DASHBOARD, WIDGET_DELETED, WIDGET_DELETION_DATE, DELETED FROM EMS_DASHBOARD_TILE where LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')"
				+ " and dashboard_id in (select dashboard_id from ems_dashboard where is_system <>1)";
		String sqlByTenant = "SELECT TILE_ID, TO_CHAR(DASHBOARD_ID) AS DASHBOARD_ID, CREATION_DATE, LAST_MODIFICATION_DATE, LAST_MODIFIED_BY, OWNER, TITLE, HEIGHT, WIDTH, IS_MAXIMIZED, POSITION, TENANT_ID,"
				+ "WIDGET_UNIQUE_ID, WIDGET_NAME, WIDGET_DESCRIPTION, WIDGET_GROUP_NAME, WIDGET_ICON, WIDGET_HISTOGRAM, WIDGET_OWNER, "
				+ "WIDGET_CREATION_TIME, WIDGET_SOURCE, WIDGET_KOC_NAME, WIDGET_VIEWMODE, WIDGET_TEMPLATE, PROVIDER_NAME, PROVIDER_VERSION, PROVIDER_ASSET_ROOT, "
				+ "TILE_ROW, TILE_COLUMN, TYPE, WIDGET_SUPPORT_TIME_CONTROL, WIDGET_LINKED_DASHBOARD, WIDGET_DELETED, WIDGET_DELETION_DATE, DELETED FROM EMS_DASHBOARD_TILE where LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')"
				+ " and dashboard_id in (select dashboard_id from ems_dashboard where is_system <>1) and tenant_Id = ?";
		String sqlByDate = "SELECT TILE_ID, TO_CHAR(DASHBOARD_ID) AS DASHBOARD_ID, CREATION_DATE, LAST_MODIFICATION_DATE, LAST_MODIFIED_BY, OWNER, TITLE, HEIGHT, WIDTH, IS_MAXIMIZED, POSITION, TENANT_ID,"
				+ "WIDGET_UNIQUE_ID, WIDGET_NAME, WIDGET_DESCRIPTION, WIDGET_GROUP_NAME, WIDGET_ICON, WIDGET_HISTOGRAM, WIDGET_OWNER, "
				+ "WIDGET_CREATION_TIME, WIDGET_SOURCE, WIDGET_KOC_NAME, WIDGET_VIEWMODE, WIDGET_TEMPLATE, PROVIDER_NAME, PROVIDER_VERSION, PROVIDER_ASSET_ROOT, "
				+ "TILE_ROW, TILE_COLUMN, TYPE, WIDGET_SUPPORT_TIME_CONTROL, WIDGET_LINKED_DASHBOARD, WIDGET_DELETED, WIDGET_DELETION_DATE, DELETED FROM EMS_DASHBOARD_TILE "
				+ "WHERE LAST_MODIFICATION_DATE > to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') and LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')"
				+ " and dashboard_id in (select dashboard_id from ems_dashboard where is_system <>1)";
		return getDBTableRows(em, type, lastComparisonDate, maxComparedDate, tenant, sql, sqlByTenant, sqlByDate);

	}

	/**
	 * Retrieves all dashboard user options data rows for all tenant
	 *
	 * @return
	 */
	public List<Map<String, Object>> getDashboardUserOptionsTableData(EntityManager em, String type,String lastComparisonDate, String maxComparedDate, String tenant)
	{
		String sql = "SELECT USER_NAME, TENANT_ID, TO_CHAR(DASHBOARD_ID) AS DASHBOARD_ID, AUTO_REFRESH_INTERVAL, ACCESS_DATE, IS_FAVORITE, EXTENDED_OPTIONS, CREATION_DATE, LAST_MODIFICATION_DATE,"
				+ " DELETED FROM EMS_DASHBOARD_USER_OPTIONS where LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')"
				+ " and  dashboard_id in (select dashboard_Id from ems_dashboard where is_system <>1)";
		String sqlByTenant = "SELECT USER_NAME, TENANT_ID, TO_CHAR(DASHBOARD_ID) AS DASHBOARD_ID, AUTO_REFRESH_INTERVAL, ACCESS_DATE, IS_FAVORITE, EXTENDED_OPTIONS, CREATION_DATE, LAST_MODIFICATION_DATE,"
				+ " DELETED FROM EMS_DASHBOARD_USER_OPTIONS where LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')"
				+ " and  dashboard_id in (select dashboard_Id from ems_dashboard where is_system <>1) and tenant_id = ?";
		String sqlByDate = "SELECT USER_NAME, TENANT_ID, TO_CHAR(DASHBOARD_ID) AS DASHBOARD_ID, AUTO_REFRESH_INTERVAL, ACCESS_DATE, IS_FAVORITE, EXTENDED_OPTIONS, CREATION_DATE, LAST_MODIFICATION_DATE,"
				+ " DELETED FROM EMS_DASHBOARD_USER_OPTIONS WHERE LAST_MODIFICATION_DATE > to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') and LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')"
				+ " and  dashboard_id in (select dashboard_Id from ems_dashboard where is_system <>1)";
		return getDBTableRows(em, type, lastComparisonDate, maxComparedDate, tenant, sql, sqlByTenant, sqlByDate);

	}

	/**
	 * Retrieves all preference data rows for all tenant
	 *
	 * @return
	 */
	public List<Map<String, Object>> getPreferenceTableData(EntityManager em,String type, String lastComparisonDate, String maxComparedDate, String tenant)
	{
		String sql = "SELECT USER_NAME, PREF_KEY, PREF_VALUE, TENANT_ID, CREATION_DATE, LAST_MODIFICATION_DATE,DELETED FROM EMS_PREFERENCE where LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')";
		String sqlByTenant = "SELECT USER_NAME, PREF_KEY, PREF_VALUE, TENANT_ID, CREATION_DATE, LAST_MODIFICATION_DATE,DELETED FROM EMS_PREFERENCE where LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') and tenant_Id=?";		
		String sqlByDate = "SELECT USER_NAME, PREF_KEY, PREF_VALUE, TENANT_ID, CREATION_DATE, LAST_MODIFICATION_DATE,DELETED FROM EMS_PREFERENCE "
				+ "WHERE LAST_MODIFICATION_DATE > to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') and LAST_MODIFICATION_DATE < to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')";
		return getDBTableRows(em, type, lastComparisonDate, maxComparedDate, tenant, sql, sqlByTenant, sqlByDate);

	}

	/**
	 * #1. if tenant is not null(full compare), return data that last modification date < max compared date data.
	 * #2. if comparison is incremental, and last comparison date is not null, return data that last modification date > last comparison date and last modification date < max compared date data
	 * #3. if comparison is incremental, and last comparison date is null(Or comparison is full and tenant is null),return data that last modification date < last compared date data.(rarely meet this case)
	 * @param em
	 * @param type
	 * @param lastComparisonDate
	 * @param maxComparedDate
	 * @param tenant
	 * @param sql
	 * @param sqlByTenant
	 * @param sqlByDate
	 * @return return the table rows need to be compared
	 */
	private List<Map<String, Object>> getDBTableRows(EntityManager em, String type, String lastComparisonDate, String maxComparedDate, String tenant, String sql, String sqlByTenant, String sqlByDate) {
		if (tenant != null) {
			return getDatabaseTableData(em,sqlByTenant,null,maxComparedDate, tenant);
		} else {
			if (type.equals("incremental") && lastComparisonDate != null) {
				return getDatabaseTableData(em,sqlByDate,lastComparisonDate,maxComparedDate, null);
			} else {
				//enter this else branch, there are 2 case: #1. Comparison is incremental but lastComparison is null, #2. comparison is full, but tenant is null
				//both above cases are abnormal
				return getDatabaseTableData(em,sql,null,maxComparedDate, null);
			}
		}
	}

	/**
	 * Retrieves database data rows for specific native SQL for all tenant
	 *
	 * @return
	 */
	private List<Map<String, Object>> getDatabaseTableData(EntityManager em, String nativeSql, String lastComparisonDate, String maxComparedDate, String tenant)
	{
		if (StringUtil.isEmpty(nativeSql)) {
			logger.error("Can't query database table with null or empty SQL statement!");
			return null;
		}
		List<Map<String, Object>> list = null;
		try {
			Query query = null;
			if (lastComparisonDate != null) {
				if (maxComparedDate != null) {
					query = em.createNativeQuery(nativeSql).setParameter(1, lastComparisonDate).setParameter(2, maxComparedDate);
				} else {
					query = em.createNativeQuery(nativeSql).setParameter(1, lastComparisonDate);
				}
				
			} else {
				if (maxComparedDate != null) {
					if (tenant != null) {
						query = em.createNativeQuery(nativeSql).setParameter(1, maxComparedDate).setParameter(2, tenant);
					} else {
						query = em.createNativeQuery(nativeSql).setParameter(1, maxComparedDate);
					}
					
				} else {
					query = em.createNativeQuery(nativeSql);
				}
				
			}		
			query.setHint(QueryHints.RESULT_TYPE, ResultType.Map);
			list = query.getResultList();
		}
		catch (Exception e) {
			logger.error("Error occured when execute SQL:[" + nativeSql + "]");
		}
		return list;
	}


	public int syncDashboardTableRow(EntityManager entityManager,BigInteger dashboardId, String name, Long type, String description, String creationDate, String lastModificationDate, String lastModifiedBy, String owner,
									 Integer isSystem, Integer applicationType, Integer enableTimeRange, String screenShot, BigInteger deleted, Long tenantId, Integer enableRefresh, Integer sharePublic,
									 Integer enableEntityFilter, Integer enableDescription, String extendedOptions, Integer showInHome) throws SyncException {
		logger.info("Calling the DataManager.syncDashboardTableRow");
		//these check are db constraint
		if (checkDbDashboardConstraint(dashboardId, name, type, creationDate, owner, isSystem, enableTimeRange, deleted, tenantId, enableRefresh, sharePublic, enableEntityFilter, enableDescription, showInHome))
			return 0;


		if (!entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().begin();
		}		
		try {
			List<Map<String, Object>> result = getLastAccessDateForDashboard(entityManager, dashboardId, tenantId, name,description, owner, deleted);
			if (result != null && result.size() > 0) {
				Map<String, Object> dateMap = result.get(0);
		    	String creationD  = dateMap.get("CREATION_DATE").toString();
		    	if (creationD == null) {
		    		logger.error("Creation date is null, will not sync dashboard table data!");
		    		return 0;
		    	}
		    	Object lastModifiedObj = dateMap.get("LAST_MODIFICATION_DATE");
		    	boolean check = false;
		    	if (lastModifiedObj == null || lastModificationDate == null) {
		    		check = isAfter(creationD,creationDate);
		    	} else {
		    		check = isAfter((String)lastModifiedObj, lastModificationDate);		    		
		    	}
				
				if (check) {
					logger.info("This lastModificationDate is earlier, there is no need to update");
					return 0;
				}
				return updateDashboard(entityManager, dashboardId, name, type, description, creationDate, lastModificationDate, lastModifiedBy, owner,
						isSystem, applicationType, enableTimeRange, screenShot, deleted, tenantId, enableRefresh, sharePublic,
						enableEntityFilter, enableDescription, extendedOptions, showInHome);
			}
			logger.info("Dashboard with id {} not exist insert now", dashboardId);
			return insertDashboard(entityManager, dashboardId, name, type, description, creationDate, lastModificationDate, lastModifiedBy, owner,
					isSystem, applicationType, enableTimeRange, screenShot, deleted, tenantId, enableRefresh, sharePublic, enableEntityFilter, enableDescription, extendedOptions, showInHome);
		} catch (Exception e) {
			logger.error(e);
			throw new SyncException("Error occur when sync dashboard set table..");
		}
	}

	private boolean checkDbDashboardConstraint(BigInteger dashboardId, String name, Long type, String creationDate, String owner, Integer isSystem, Integer enableTimeRange, BigInteger deleted, Long tenantId, Integer enableRefresh, Integer sharePublic, Integer enableEntityFilter, Integer enableDescription, Integer showInHome) {
		if (dashboardId == null) {
			logger.warn("dashboard id cannot be null!");
			return true;
		}
		if (StringUtil.isEmpty(name)) {
			logger.warn("name cannot be null!");
			return true;
		}
		if (type == null) {
			logger.warn("type cannot be null!");
			return true;
		}
		if ((StringUtil.isEmpty(creationDate))) {
			logger.warn("creation date cannot be null!");
			return true;
		}
		if ((StringUtil.isEmpty(owner))) {
			logger.warn("owner cannot be null or empty!");
			return true;
		}
		if (isSystem == null) {
			logger.warn("isSystem cannot be null!");
			return true;
		}
		if (enableTimeRange == null) {
			logger.warn("ENABLE_TIME_RANGE cannot be null!");
			return true;
		}
		if (deleted == null) {
			logger.warn("deleted cannot be null!");
			return true;
		}
		if (tenantId == null) {
			logger.warn("tenantId cannot be null!");
			return true;
		}
		if (enableRefresh == null) {
			logger.warn("enableRefresh cannot be null!");
			return true;
		}
		if (sharePublic == null) {
			logger.warn("sharePublic cannot be null!");
			return true;
		}
		if (enableEntityFilter == null) {
			logger.warn("enableEntityFilter cannot be null!");
			return true;
		}
		if (enableDescription == null) {
			logger.warn("enableDescription cannot be null!");
			return true;
		}
		if (showInHome == null) {
			logger.warn("showInHome cannot be null!");
			return true;
		}
		return false;
	}

	public int syncDashboardTile(EntityManager entityManager,String tileId, BigInteger dashboardId, String creationDate, String lastModificationDate, String lastModifiedBy, String owner, String title, Long height,
								 Long width, Integer isMaximized, Long position, Long tenantId, String widgetUniqueId, String widgetName, String widgetDescription, String widgetGroupName,
								 String widgetIcon, String widgetHistogram, String widgetOwner, String widgetCreationTime, Long widgetSource, String widgetKocName, String widgetViewmode, String widgetTemplate,
								 String providerName, String providerVersion, String providerAssetRoot, Long tileRow, Long tileColumn, Long type, Integer widgetSupportTimeControl, 
								 Long widgetLinkedDashboard, Integer widgetDeleted, String widgetDeletionDate, Integer deleted) throws SyncException {
		logger.info("Calling the DataManager,syncDashboardTile");
		//db constraint check
		if (checkDbDashboardTileConstraint(dashboardId, creationDate, owner, title, isMaximized, position, tenantId, widgetUniqueId, widgetName, widgetOwner, widgetCreationTime, widgetSource, widgetKocName, widgetViewmode, widgetTemplate, widgetSupportTimeControl, widgetDeleted, deleted))
			return 0;
		if (!entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().begin();
		}
		try {
			List<Map<String, Object>> result = getLastAccessDateForDashboardTile(entityManager, tileId, tenantId);
			if (result != null && result.size() > 0) {
				logger.info("Dashboard Tile with id {} exists", tileId);
				Map<String, Object> dateMap = result.get(0);
		    	String creationD  = dateMap.get("CREATION_DATE").toString();
		    	if (creationD == null) {
		    		return 0;
		    	}
		    	Object lastModifiedObj = dateMap.get("LAST_MODIFICATION_DATE");
		    	boolean check = false;
		    	if (lastModifiedObj == null || lastModificationDate == null) {
		    		check = isAfter(creationD,creationDate);
		    	} else {
		    		check = isAfter((String)lastModifiedObj, lastModificationDate);		    		
		    	}
				
				if (check) {
					logger.info("This lastModificationDate is earlier, there is no need to update");
					return 0;
				}
				logger.info("The lastModificationDate is later,do update now");
				return updateDashboardTile(entityManager, tileId, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
						width, isMaximized, position, tenantId, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
						widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, widgetSource, widgetKocName, widgetViewmode, widgetTemplate,
						providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard,
						widgetDeleted,widgetDeletionDate,  deleted);
			}
			logger.info("Tile with id {} not exist insert now", tileId);
			return insertDashboardTile(entityManager,tileId, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
					width, isMaximized, position, tenantId, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
					widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, widgetSource, widgetKocName, widgetViewmode, widgetTemplate,
					providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard,
					widgetDeleted,widgetDeletionDate,  deleted);
		} catch (Exception e) {
			logger.error(e);
			throw new SyncException("Error occur when sync dashboard tile table..");
		}
	}

	private boolean checkDbDashboardTileConstraint(BigInteger dashboardId, String creationDate, String owner, String title, Integer isMaximized, Long position, Long tenantId, String widgetUniqueId, String widgetName, String widgetOwner, String widgetCreationTime, Long widgetSource, String widgetKocName, String widgetViewmode, String widgetTemplate, Integer widgetSupportTimeControl, Integer widgetDeleted, Integer deleted) {
		if (dashboardId == null) {
			logger.warn("DASHBOARD_ID is null!");
			return true;
		}
		if (StringUtil.isEmpty(creationDate)) {
			logger.warn("CREATION_DATE is null or empty!");
			return true;
		}
		if (owner == null) {
			logger.warn("owner is null!");
			return true;
		}
		if (StringUtil.isEmpty(title)) {
			logger.warn("TITLE is null or empty!");
			return true;
		}
		if (isMaximized == null) {
			logger.warn("isMaximized is null!");
			return true;
		}
		if (position == null) {
			logger.warn("position is null!");
			return true;
		}
		if (tenantId == null) {
			logger.warn("tenantId is null!");
			return true;
		}
		if (widgetUniqueId == null) {
			logger.warn("widgetUniqueId is null!");
			return true;
		}
		if (widgetName == null) {
			logger.warn("widgetName is null!");
			return true;
		}
		if (widgetOwner == null) {
			logger.warn("widgetOwner is null!");
			return true;
		}
		if (widgetCreationTime == null) {
			logger.warn("widgetCreationTime is null!");
			return true;
		}
		if (widgetSource == null) {
			logger.warn("widgetSource is null!");
			return true;
		}
		if (widgetKocName == null) {
			logger.warn("widgetKocName is null!");
			return true;
		}
		if (widgetViewmode == null) {
			logger.warn("widgetViewmode is null!");
			return true;
		}
		if (widgetTemplate == null) {
			logger.warn("widgetTemplate is null!");
			return true;
		}
		if (widgetSupportTimeControl == null) {
			logger.warn("widgetSupportTimeControl is null!");
			return true;
		}
		if (widgetDeleted == null) {
			logger.warn("widgetDeleted is null!");
			return true;
		}
		if (deleted == null) {
			logger.warn("deleted is null!");
			return true;
		}
		return false;
	}

	public int syncDashboardTileParam(EntityManager entityManager, String tileId, String paramName,
									  Long tenantId, Integer isSystem, Long paramType, String paramValueStr,
									  Long paramValueNum, String paramValueTimestamp, String creationDate, String lastModificationDate, Integer deleted) throws SyncException {
		logger.info("Calling DataManager.syncDashboardTileParam");
		//check db constraint
		if (checkDbDashboardTileParamConstraint(paramName, tenantId, isSystem, paramType, deleted)) return 0;
		if (!entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().begin();
		}
		try {
			List<Map<String, Object>> result = getLastAccessDateForDashboardTileParam(entityManager, tileId, paramName, tenantId);
			if (result != null && result.size() > 0) {
				logger.info("DashboardTileParam with tile id {} exists", tileId);
				Map<String, Object> dateMap = result.get(0);
		    	String creationD  = dateMap.get("CREATION_DATE").toString();
		    	if (creationD == null) {
					logger.error("Creation date is null, will not sync dashboard tile table data!");
		    		return 0;
		    	}
		    	Object lastModifiedObj = dateMap.get("LAST_MODIFICATION_DATE");
		    	boolean check = false;
		    	if (lastModifiedObj == null || lastModificationDate == null) {
		    		check = isAfter(creationD,creationDate);
		    	} else {
		    		check = isAfter((String)lastModifiedObj, lastModificationDate);		    		
		    	}
				
				if (check) {
					logger.info("The lastModificationDate is earlier, no need to update");
					return 0;
				}
				logger.info("The lastModificationDate is later,do update now");
				return updateDashboardTileParam(entityManager, tileId, paramName, tenantId, isSystem,
						paramType, paramValueStr, paramValueNum, paramValueTimestamp, creationDate, lastModificationDate, deleted);
			}
			logger.info("Tile param with id {} does not exist insert now", tileId);
			return insertDashboardTileParam(entityManager, tileId, paramName, tenantId,
					isSystem, paramType, paramValueStr, paramValueNum,
					paramValueTimestamp, creationDate, lastModificationDate, deleted);
		}catch (Exception e){
			logger.error(e);
			throw new SyncException("Error occur when sync dashboard tile param table..");
		}
	}

	private boolean checkDbDashboardTileParamConstraint(String paramName, Long tenantId, Integer isSystem, Long paramType, Integer deleted) {
		if(paramName == null){
			logger.warn("PARAM_NAME is null!");
			return true;
		}
		if(tenantId == null){
			logger.warn("TENANT_ID is null!");
			return true;
		}
		if(isSystem == null){
			logger.warn("IS_SYSTEM is null!");
			return true;
		}
		if(paramType == null){
			logger.warn("PARAM_TYPE is null!");
			return true;
		}
		if(deleted == null){
			logger.warn("deleted is null!");
			return true;
		}
		return false;
	}


	public int syncDashboardUserOption(EntityManager entityManager,String userName, Long tenantId, BigInteger dashboardId,
									   Long autoRefreshInterval, String accessDate, Integer isFavorite, String extendedOptions,
									   String creationDate, String lastModificationDate, Integer deleted) throws SyncException {
		logger.info("Calling DataManager.syncDashboardUserOption");
		//check db constraint
		if (checkDbUserOptionConstraint(userName, tenantId, dashboardId, autoRefreshInterval, isFavorite, deleted))
			return 0;

		if (!entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().begin();
		}
		try {
			List<Map<String, Object>> result = getLastAccessDateForDashboardUserOption(entityManager, userName, tenantId, dashboardId);
			if (result != null && result.size() > 0) {
				logger.info("DashboardUserOption with dashboardId {} exists", dashboardId);
				Map<String, Object> dateMap = result.get(0);
		    	String creationD  = dateMap.get("CREATION_DATE").toString();
		    	if (creationD == null) {
					logger.error("Creation date is null, will not sync user option table data!");
		    		return 0;
		    	}
		    	Object lastModifiedObj = dateMap.get("LAST_MODIFICATION_DATE");
		    	boolean check = false;
		    	if (lastModifiedObj == null || lastModificationDate == null) {
		    		check = isAfter(creationD,creationDate);
		    	} else {
		    		check = isAfter((String)lastModifiedObj, lastModificationDate);		    		
		    	}
				
				if (check) {
					logger.info("The lastModificationDate is earlier, no need to update");
					return 0;
				}
				logger.info("The lastModificationDate is later, do update now");
				return updateDashboardUserOption(entityManager, userName, tenantId, dashboardId,
						autoRefreshInterval, accessDate, isFavorite, extendedOptions,
						creationDate, lastModificationDate, deleted);
			}
			logger.info("Dashboard User Option with id {} does not exist insert now", dashboardId);
			return insertDashboardUserOption(entityManager, userName, tenantId, dashboardId, autoRefreshInterval,
					accessDate, isFavorite, extendedOptions, creationDate, lastModificationDate, deleted);
		} catch (Exception e) {
			logger.error(e);
			throw new SyncException("Error occur when sync user option table..");
		}
	}

	private boolean checkDbUserOptionConstraint(String userName, Long tenantId, BigInteger dashboardId, Long autoRefreshInterval, Integer isFavorite, Integer deleted) {
		if (StringUtil.isEmpty(userName)) {
			logger.warn("USER_NAME is null!");
			return true;
		}
		if (tenantId == null) {
			logger.warn("TENANT_ID is null !");
			return true;
		}
		if (dashboardId == null) {
			logger.warn("DASHBOARD_ID is null !");
			return true;
		}
		if (autoRefreshInterval == null) {
			logger.warn("autoRefreshInterval is null !");
			return true;
		}
		if (isFavorite == null) {
			logger.warn("isFavorite is null !");
			return true;
		}
		if (deleted == null) {
			logger.warn("deleted is null !");
			return true;
		}
		return false;
	}

	public int syncDashboardSet(EntityManager entityManager,BigInteger dashboardSetId, Long tenantId, BigInteger subDashboardId,
								Long position, String creationDate, String lastModificationDate, BigInteger deleted) throws SyncException {
		logger.info("Calling DataManager.syncDashboardSet", dashboardSetId);
		//db constraint check
		if (checkDbDashboardSetConstraint(dashboardSetId, tenantId, subDashboardId, position, deleted)) return 0;
		if (!entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().begin();
		}
		try {
			List<Map<String, Object>> result = getLastAccessDateForDashboardSet(entityManager, dashboardSetId, tenantId, subDashboardId);
			if (result != null && result.size() > 0) {
				logger.info("DashboardSet with dashboardSetId {} exist", dashboardSetId);
				Map<String, Object> dateMap = result.get(0);
		    	String creationD  = dateMap.get("CREATION_DATE").toString();
		    	if (creationD == null) {
					logger.error("Creation date is null, will not sync dashboard set table data!");
		    		return 0;
		    	}
		    	Object lastModifiedObj = dateMap.get("LAST_MODIFICATION_DATE");
		    	boolean check = false;
		    	if (lastModifiedObj == null || lastModificationDate == null) {
		    		check = isAfter(creationD,creationDate);
		    	} else {
		    		check = isAfter((String)lastModifiedObj, lastModificationDate);		    		
		    	}
				
				if (check) {
					logger.info("The lastModification is earlier, no need to update");
					return 0;
				}
				logger.info("The lastModificationDate is later, do update now");
				return updateDashboardSet(entityManager, dashboardSetId, tenantId, subDashboardId, position, creationDate, lastModificationDate, deleted);
			}
			logger.info("Dashboard Set with id {} does not exist insert now", dashboardSetId);
			return insertDashboardSet(entityManager, dashboardSetId, tenantId, subDashboardId, position, creationDate, lastModificationDate, deleted);
		} catch (Exception e) {
			logger.error(e);
			throw new SyncException("Error occur when sync dashboard table..");
		}
	}

	private boolean checkDbDashboardSetConstraint(BigInteger dashboardSetId, Long tenantId, BigInteger subDashboardId, Long position, BigInteger deleted) {
		if (dashboardSetId == null) {
			logger.warn("DASHBOARD_SET_ID is null !");
			return true;
		}
		if (tenantId == null) {
			logger.warn("TENANT_ID is null !");
			return true;
		}
		if (subDashboardId == null) {
			logger.warn("SUB_DASHBOARD_ID is null !");
			return true;
		}
		if (position == null) {
			logger.warn("POSITION is null !");
			return true;
		}
		if (deleted == null) {
			logger.warn("deleted is null !");
			return true;
		}
		return false;
	}

	public int syncPreferences(EntityManager entityManager,String userName, String prefKey, String prefValue,
							   Long tenantId, String creationDate, String lastModificationDate, Integer deleted) throws SyncException {
		logger.info("Calling syncPreference");
		//these check are db constraint
		if (checkDbPreferenceConstraint(entityManager, userName, prefKey, prefValue, tenantId, deleted)) return 0;
		try {
			List<Map<String, Object>> result = getLastAccessDateForPreference(entityManager, userName, prefKey, tenantId);
			if (result != null && result.size() > 0) {
				logger.info("Preference with prefKey {} exists", prefKey);
				Map<String, Object> dateMap = result.get(0);
		    	String creationD  = dateMap.get("CREATION_DATE").toString();
		    	if (creationD == null) {
					logger.error("Creation date is null, will not sync preference table data!");
		    		return 0;
		    	}
		    	Object lastModifiedObj = dateMap.get("LAST_MODIFICATION_DATE");
		    	boolean check = false;
		    	if (lastModifiedObj == null || lastModificationDate == null) {
		    		check = isAfter(creationD,creationDate);
		    	} else {
		    		check = isAfter((String)lastModifiedObj, lastModificationDate);		    		
		    	}
				
				if (check) {
					logger.info("The lastModificationDate is earlier, no need to update");
					return 0;
				}
				logger.info("The lastModificationDate is later, do update now");
				return updatePreferences(entityManager, userName, prefKey, prefValue, tenantId, creationDate, lastModificationDate, deleted);
			}
			logger.info("Preference with prefKey {} does not exist insert now", prefKey);
			return insertPreferences(entityManager, userName, prefKey, prefValue, tenantId, creationDate, lastModificationDate, deleted);
		} catch (Exception e) {
			logger.error(e);
			throw new SyncException("Error occur when sync preference table..");
		}
	}

	private boolean checkDbPreferenceConstraint(EntityManager entityManager, String userName, String prefKey, String prefValue, Long tenantId, Integer deleted) {
		if (StringUtil.isEmpty(userName)) {
			logger.warn("USER_NAME is null or empty!");
			return true;
		}
		if (StringUtil.isEmpty(prefKey)) {
			logger.warn("PREF_KEY is null or empty!");
			return true;
		}
		if (StringUtil.isEmpty(prefValue)) {
			logger.warn("PREF_VALUE is null or empty!");
			return true;
		}
		if (tenantId == null) {
			logger.warn("TENANT_ID is null");
			return true;
		}
		if(deleted == null){
			logger.warn("DELETED is null");
			return true;
		}
		if (!entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().begin();
		}
		return false;
	}

	private int insertDashboard(EntityManager entityManager, BigInteger dashboardId, String name, Long type, String description, String creationDate, String lastModificationDate, String lastModifiedBy, String owner,
								Integer isSystem, Integer applicationType, Integer enableTimeRange, String screenShot, BigInteger deleted, Long tenantId, Integer enableRefresh, Integer sharePublic,
								Integer enableEntityFilter, Integer enableDescription, String extendedOptions, Integer showInHome) {
		logger.info("Calling the Datamanager.insertDashboard");
		int result;
		String sql = "INSERT INTO EMS_DASHBOARD(DASHBOARD_ID,  NAME, TYPE, DESCRIPTION, CREATION_DATE, LAST_MODIFICATION_DATE, LAST_MODIFIED_BY, OWNER, IS_SYSTEM, APPLICATION_TYPE, ENABLE_TIME_RANGE, SCREEN_SHOT, DELETED, TENANT_ID, ENABLE_REFRESH, SHARE_PUBLIC, ENABLE_ENTITY_FILTER, ENABLE_DESCRIPTION, EXTENDED_OPTIONS, SHOW_INHOME)values(?,?,?,?,to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') ,to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') ,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, dashboardId)
				.setParameter(2, name)
				.setParameter(3, type)
				.setParameter(4, description)
				.setParameter(5, creationDate)
				.setParameter(6, lastModificationDate)
				.setParameter(7, lastModifiedBy)
				.setParameter(8, owner)
				.setParameter(9, isSystem)
				.setParameter(10, applicationType)
				.setParameter(11, enableTimeRange)
				.setParameter(12, screenShot)
				.setParameter(13, deleted)
				.setParameter(14, tenantId)
				.setParameter(15, enableRefresh)
				.setParameter(16, sharePublic)
				.setParameter(17, enableEntityFilter)
				.setParameter(18, enableDescription)
				.setParameter(19, extendedOptions)
				.setParameter(20, showInHome);
		result = query.executeUpdate();
		return result;
	}

	private int insertDashboardTile(EntityManager entityManager, String tileId, BigInteger dashboardId, String creationDate, String lastModificationDate, String lastModifiedBy, String owner, String title, Long height,
									Long width, Integer isMaximized, Long position, Long tenantId, String widgetUniqueId, String widgetName, String widgetDescription, String widgetGroupName,
									String widgetIcon, String widgetHistogram, String widgetOwner, String widgetCreationTime, Long widgetSource, String widgetKocName, String widgetViewmode, String widgetTemplate,
									String providerName, String providerVersion, String providerAssetRoot, Long tileRow, Long tileColumn, Long type, Integer widgetSupportTimeControl, Long widgetLinkedDashboard
									,Integer widgetDeleted, String widgetDeletionDate, Integer deleted) {
		logger.info("Calling DataManager.insertDashboardTiles");
		int result;
		String sql = "INSERT INTO EMS_DASHBOARD_TILE(TILE_ID, DASHBOARD_ID, CREATION_DATE, LAST_MODIFICATION_DATE,"
				+ " LAST_MODIFIED_BY, OWNER, TITLE, HEIGHT, WIDTH, IS_MAXIMIZED, POSITION, TENANT_ID, WIDGET_UNIQUE_ID, "
				+ "WIDGET_NAME, WIDGET_DESCRIPTION, WIDGET_GROUP_NAME, WIDGET_ICON, WIDGET_HISTOGRAM, WIDGET_OWNER, "
				+ "WIDGET_CREATION_TIME, WIDGET_SOURCE, WIDGET_KOC_NAME, WIDGET_VIEWMODE, WIDGET_TEMPLATE, PROVIDER_NAME, "
				+ "PROVIDER_VERSION, PROVIDER_ASSET_ROOT, TILE_ROW, TILE_COLUMN, TYPE, WIDGET_SUPPORT_TIME_CONTROL,"
				+ " WIDGET_LINKED_DASHBOARD, WIDGET_DELETED, WIDGET_DELETION_DATE, DELETED)"
				+ "values(?,?,to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') ,to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') "
				+ ",?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,?)";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, tileId).setParameter(2, dashboardId)
				.setParameter(3, creationDate).setParameter(4, lastModificationDate)
				.setParameter(5, lastModifiedBy).setParameter(6, owner)
				.setParameter(7, title).setParameter(8, height)
				.setParameter(9, width).setParameter(10, isMaximized)
				.setParameter(11, position).setParameter(12, tenantId)
				.setParameter(13, widgetUniqueId).setParameter(14, widgetName)
				.setParameter(15, widgetDescription).setParameter(16, widgetGroupName)
				.setParameter(17, widgetIcon).setParameter(18, widgetHistogram)
				.setParameter(19, widgetOwner).setParameter(20, widgetCreationTime)
				.setParameter(21, widgetSource).setParameter(22, widgetKocName)
				.setParameter(23, widgetViewmode).setParameter(24, widgetTemplate)
				.setParameter(25, providerName).setParameter(26, providerVersion)
				.setParameter(27, providerAssetRoot).setParameter(28, tileRow)
				.setParameter(29, tileColumn).setParameter(30, type)
				.setParameter(31, widgetSupportTimeControl).setParameter(32, widgetLinkedDashboard)
				.setParameter(33, widgetDeleted)
				.setParameter(34, widgetDeletionDate)
				.setParameter(35, deleted);
		result = query.executeUpdate();
		return result;
	}

	private int insertDashboardTileParam(EntityManager entityManager, String tileId, String paramName, Long tenantId, Integer isSystem, Long paramType, String paramValueStr, Long paramValueNum, String paramValueTimestamp, String creationDate, String lastModificationDate, Integer deleted) {
		logger.info("Calling DataManager.insertDashboardTileParam");
		int result;
		String sql = "INSERT INTO EMS_DASHBOARD_TILE_PARAMS(TILE_ID, PARAM_NAME, TENANT_ID, IS_SYSTEM, PARAM_TYPE, PARAM_VALUE_STR,"
				+ " PARAM_VALUE_NUM, PARAM_VALUE_TIMESTAMP, CREATION_DATE, LAST_MODIFICATION_DATE, DELETED)"
				+ "values(?, ?, ?, ?, ?, ?, ?,"
				+ " to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), "
				+ "to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'),?)";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, tileId)
				.setParameter(2, paramName)
				.setParameter(3, tenantId)
				.setParameter(4, isSystem)
				.setParameter(5, paramType)
				.setParameter(6, paramValueStr)
				.setParameter(7, paramValueNum)
				.setParameter(8, paramValueTimestamp)
				.setParameter(9, creationDate)
				.setParameter(10, lastModificationDate)
				.setParameter(11, deleted);
		result = query.executeUpdate();
		return result;
	}


	private int insertDashboardUserOption(EntityManager entityManager, String userName, Long tenantId, BigInteger dashboardId, Long autoRefreshInterval, String accessDate,
										  Integer isFavorite, String extendedOptions, String creationDate, String lastModificationDate, Integer deleted) {
		logger.info("Calling DataManager.insertDashboardUserOptions");
		int result;
		String sql = "INSERT INTO EMS_DASHBOARD_USER_OPTIONS(USER_NAME, TENANT_ID, DASHBOARD_ID, AUTO_REFRESH_INTERVAL, "
				+ "ACCESS_DATE, IS_FAVORITE, EXTENDED_OPTIONS, CREATION_DATE, LAST_MODIFICATION_DATE, DELETED)"
				+ "values(?, ?, ?, ?, to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), ?, ?,"
				+ " to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'),?)";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, userName).setParameter(2, tenantId)
				.setParameter(3, dashboardId).setParameter(4, autoRefreshInterval)
				.setParameter(5, accessDate).setParameter(6, isFavorite)
				.setParameter(7, extendedOptions)
				.setParameter(8, creationDate)
				.setParameter(9, lastModificationDate)
				.setParameter(10,deleted);
		result = query.executeUpdate();
		return result;

	}

	private int insertDashboardSet(EntityManager entityManager, BigInteger dashboardSetId, Long tenantId, BigInteger subDashboardId, Long position, String creationDate, String lastModificationDate, BigInteger deleted) {
		logger.info("Calling DataManager.insertDashboardSet");
		int result;
		String sql = "INSERT INTO EMS_DASHBOARD_SET(DASHBOARD_SET_ID, TENANT_ID, SUB_DASHBOARD_ID, POSITION, CREATION_DATE, "
				+ "LAST_MODIFICATION_DATE, DELETED)values(?, ?, ?, ?, to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), "
				+ "to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'),?)";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, dashboardSetId)
				.setParameter(2, tenantId)
				.setParameter(3, subDashboardId)
				.setParameter(4, position)
				.setParameter(5, creationDate)
				.setParameter(6, lastModificationDate)
				.setParameter(7, deleted);
		result = query.executeUpdate();
		return result;
	}

	private int insertPreferences(EntityManager entityManager, String userName, String prefKey, String prefValue, Long tenantId, String creationDate, String lastModificationDate, Integer deleted) {
		logger.info("Calling DataManager.insertPreference");
		int result;
		String sql = "INSERT INTO EMS_PREFERENCE (USER_NAME, PREF_KEY, PREF_VALUE, TENANT_ID, CREATION_DATE, LAST_MODIFICATION_DATE,DELETED)"
				+ "values(?, ?, ?, ?, to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'),?)";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, userName)
				.setParameter(2, prefKey)
				.setParameter(3, prefValue)
				.setParameter(4, tenantId)
				.setParameter(5, creationDate)
				.setParameter(6, lastModificationDate)
				.setParameter(7, deleted);
		result = query.executeUpdate();
		return result;
	}

	private int updateDashboard(EntityManager entityManager, BigInteger dashboardId, String name, Long type, String description, String creationDate, String lastModificationDate, String lastModifiedBy, String owner,
								Integer isSystem, Integer applicationType, Integer enableTimeRange, String screenShot, BigInteger deleted, Long tenantId, Integer enableRefresh, Integer sharePublic,
								Integer enableEntityFilter, Integer enableDescription, String extendedOptions, Integer showInHome) {
		logger.info("Calling the Datamanager.updateDashboard");
		int result;
		String sql = "UPDATE EMS_DASHBOARD SET  NAME=?, TYPE=?, DESCRIPTION=?, CREATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'),"
				+ " LAST_MODIFICATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), LAST_MODIFIED_BY=?, OWNER=?, IS_SYSTEM=?, "
				+ "APPLICATION_TYPE=?, ENABLE_TIME_RANGE=?, SCREEN_SHOT=?, DELETED=?, ENABLE_REFRESH=?, SHARE_PUBLIC=?, ENABLE_ENTITY_FILTER=?,"
				+ " ENABLE_DESCRIPTION=?, EXTENDED_OPTIONS=?, SHOW_INHOME=? WHERE DASHBOARD_ID=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, name)
				.setParameter(2, type)
				.setParameter(3, description)
				.setParameter(4, creationDate)
				.setParameter(5, lastModificationDate)
				.setParameter(6, lastModifiedBy)
				.setParameter(7, owner)
				.setParameter(8, isSystem)
				.setParameter(9, applicationType)
				.setParameter(10, enableTimeRange)
				.setParameter(11, screenShot)
				.setParameter(12, deleted)
				.setParameter(13, enableRefresh)
				.setParameter(14, sharePublic)
				.setParameter(15, enableEntityFilter)
				.setParameter(16, enableDescription)
				.setParameter(17, extendedOptions)			
				.setParameter(18, showInHome)
				.setParameter(19, dashboardId)
				.setParameter(20, tenantId);
		result = query.executeUpdate();
		return result;
	}

	private int updateDashboardTile(EntityManager entityManager, String tileId, BigInteger dashboardId, String creationDate, String lastModificationDate, String lastModifiedBy, String owner, String title, Long height,
                                    Long width, Integer isMaximized, Long position, Long tenantId, String widgetUniqueId, String widgetName, String widgetDescription, String widgetGroupName,
                                    String widgetIcon, String widgetHistogram, String widgetOwner, String widgetCreationTime, Long widgetSource, String widgetKocName, String widgetViewmode, String widgetTemplate,
                                    String providerName, String providerVersion, String providerAssetRoot, Long tileRow, Long tileColumn, Long type, Integer widgetSupportTimeControl, Long widgetLinkedDashboard,
                                    Integer widgetDeleted, String widgetDeletionDate, Integer deleted) {
		logger.info("Calling Datamanager.updateDashboardTiles");
		int result;
		String sql = "UPDATE EMS_DASHBOARD_TILE SET CREATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') , LAST_MODIFICATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') , "
				+ "LAST_MODIFIED_BY=?, OWNER=?, TITLE=?, HEIGHT=?, WIDTH=?, IS_MAXIMIZED=?, POSITION=?, WIDGET_UNIQUE_ID=?, WIDGET_NAME=?, WIDGET_DESCRIPTION=?, WIDGET_GROUP_NAME=?,"
				+ " WIDGET_ICON=?, WIDGET_HISTOGRAM=?, WIDGET_OWNER=?, WIDGET_CREATION_TIME=?, WIDGET_SOURCE=?, WIDGET_KOC_NAME=?, WIDGET_VIEWMODE=?, WIDGET_TEMPLATE=?, "
				+ "PROVIDER_NAME=?, PROVIDER_VERSION=?, PROVIDER_ASSET_ROOT=?, TILE_ROW=?, TILE_COLUMN=?, TYPE=?, WIDGET_SUPPORT_TIME_CONTROL=?, WIDGET_LINKED_DASHBOARD=?,"
				+ "WIDGET_DELETED=?,WIDGET_DELETION_DATE=?,DELETED=? "
				+ "WHERE TILE_ID=? AND DASHBOARD_ID=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, creationDate)
				.setParameter(2, lastModificationDate)
				.setParameter(3, lastModifiedBy)
				.setParameter(4, owner)
				.setParameter(5, title)
				.setParameter(6, height)
				.setParameter(7, width)
				.setParameter(8, isMaximized)
				.setParameter(9, position)
				.setParameter(10, widgetUniqueId)
				.setParameter(11, widgetName)
				.setParameter(12, widgetDescription)
				.setParameter(13, widgetGroupName)
				.setParameter(14, widgetIcon)
				.setParameter(15, widgetHistogram)
				.setParameter(16, widgetOwner)
				.setParameter(17, widgetCreationTime)
				.setParameter(18, widgetSource)
				.setParameter(19, widgetKocName)
				.setParameter(20, widgetViewmode)
				.setParameter(21, widgetTemplate)
				.setParameter(22, providerName)
				.setParameter(23, providerVersion)
				.setParameter(24, providerAssetRoot)
				.setParameter(25, tileRow)
				.setParameter(26, tileColumn)
				.setParameter(27, type)
				.setParameter(28, widgetSupportTimeControl)
				.setParameter(29, widgetLinkedDashboard)				
				.setParameter(30, widgetDeleted)
				.setParameter(31, widgetDeletionDate)
				.setParameter(32, deleted)
				.setParameter(33, tileId)
				.setParameter(34, dashboardId)
				.setParameter(35, tenantId);
		result = query.executeUpdate();
		return result;
	}

	private int updateDashboardTileParam(EntityManager entityManager, String tileId, String paramName, Long tenantId,
										 Integer isSystem, Long paramType, String paramValueStr, Long paramValueNum,
										 String paramValueTimestamp, String creationDate, String lastModificationDate, Integer deleted) {
		logger.info("Calling DataManager.updateDashboardTileParam");
		int result;
		String sql = "UPDATE EMS_DASHBOARD_TILE_PARAMS SET IS_SYSTEM=?, PARAM_TYPE=?, PARAM_VALUE_STR=?, PARAM_VALUE_NUM=?, PARAM_VALUE_TIMESTAMP=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), "
				+ "CREATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), LAST_MODIFICATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'),DELETED=? "
				+ "WHERE TILE_ID=? AND PARAM_NAME=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, isSystem)
				.setParameter(2, paramType)
				.setParameter(3, paramValueStr)
				.setParameter(4, paramValueNum)
				.setParameter(5, paramValueTimestamp)
				.setParameter(6, creationDate)
				.setParameter(7, lastModificationDate)				
				.setParameter(8, deleted)
				.setParameter(9, tileId)
				.setParameter(10, paramName)
				.setParameter(11, tenantId);
		result = query.executeUpdate();
		return result;
	}
 
	private int updateDashboardUserOption(EntityManager entityManager, String userName, Long tenantId, BigInteger dashboardId, Long autoRefreshInterval, String accessDate,
										  Integer isFavorite, String extendedOptions, String creationDate, String lastModificationDate, Integer deleted) {
		logger.info("Calling DataManager.updateDashboardUserOption");
		int result;
		String sql = "UPDATE EMS_DASHBOARD_USER_OPTIONS SET CREATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), LAST_MODIFICATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'),"
				+ " AUTO_REFRESH_INTERVAL=?, ACCESS_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), IS_FAVORITE=?, EXTENDED_OPTIONS=?, DELETED=? "
				+ "WHERE USER_NAME=? AND TENANT_ID=? AND DASHBOARD_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, creationDate)
				.setParameter(2, lastModificationDate)
				.setParameter(3, autoRefreshInterval)
				.setParameter(4, accessDate)
				.setParameter(5, isFavorite)
				.setParameter(6, extendedOptions)				
				.setParameter(7, deleted)
				.setParameter(8, userName)
				.setParameter(9, tenantId)
				.setParameter(10, dashboardId);
		result = query.executeUpdate();
		return result;

	}

	private int updateDashboardSet(EntityManager entityManager, BigInteger dashboardSetId, Long tenantId, BigInteger subDashboardId,
								   Long position, String creationDate, String lastModificationDate,BigInteger deleted) {
		logger.info("Calling DataManager.updateDashboardSet");
		int result;
		String sql = "UPDATE EMS_DASHBOARD_SET SET CREATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), LAST_MODIFICATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'),"
				+ " POSITION=?,DELETED=? WHERE DASHBOARD_SET_ID=? AND TENANT_ID=? AND SUB_DASHBOARD_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, creationDate)
				.setParameter(2, lastModificationDate)
				.setParameter(3, position)				
				.setParameter(4, deleted)
				.setParameter(5, dashboardSetId)
				.setParameter(6, tenantId)
				.setParameter(7, subDashboardId);
		result = query.executeUpdate();
		return result;
	}

	private int updatePreferences(EntityManager entityManager, String userName, String prefKey,
								  String prefValue, Long tenantId, String creationDate, String lastModificationDate, Integer deleted) {
		logger.info("Calling DataManager.updatePreference");
		int result;
		String sql = "UPDATE EMS_PREFERENCE SET PREF_VALUE=?, CREATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), LAST_MODIFICATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'),"
				+ "DELETED=? WHERE USER_NAME=? AND PREF_KEY=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, prefValue)
				.setParameter(2, creationDate)
				.setParameter(3, lastModificationDate)			
				.setParameter(4, deleted)
				.setParameter(5, userName)
				.setParameter(6, prefKey)
				.setParameter(7, tenantId);
		result = query.executeUpdate();
		return result;
	}

	private List<Map<String, Object>> getLastAccessDateForDashboard(EntityManager entityManager, BigInteger dashboardId, Long tenantId, String name,String description, String owner, BigInteger deleted) {
		logger.info("Calling the Datamanager.gertLastAccessDayForDashboard");
		String sql = "SELECT to_char(CREATION_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as CREATION_DATE,to_char(LAST_MODIFICATION_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as LAST_MODIFICATION_DATE FROM EMS_DASHBOARD WHERE (DASHBOARD_ID=? AND TENANT_ID=?) "
				+ "or (NAME=? AND DESCRIPTION=? and OWNER=? AND TENANT_ID =? AND DELETED=?)";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, dashboardId)
				.setParameter(2, tenantId)
				.setParameter(3,name)
				.setParameter(4, description)
		.setParameter(5, owner)
		.setParameter(6, tenantId)
		.setParameter(7, deleted);
		query.setHint(QueryHints.RESULT_TYPE, ResultType.Map);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> result = query.getResultList();
		return result;

	}

	private List<Map<String, Object>> getLastAccessDateForDashboardTile(EntityManager entityManager, String tileId, Long tenantId) {
		logger.info("Calling Datamanager.getLastAccessDateForDashboardTile");
		String sql = "SELECT to_char(CREATION_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as CREATION_DATE,to_char(LAST_MODIFICATION_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as LAST_MODIFICATION_DATE FROM EMS_DASHBOARD_TILE WHERE TILE_ID=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, tileId)
				.setParameter(2, tenantId);
		query.setHint(QueryHints.RESULT_TYPE, ResultType.Map);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> result = query.getResultList();
		return result;

	}

	private List<Map<String, Object>> getLastAccessDateForDashboardTileParam(EntityManager entityManager, String tileId, String paramName, Long tenantId) {
		logger.info("Calling DataManager.getLastAccessDateForDashboardTileParam");
		String sql = "SELECT to_char(CREATION_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as CREATION_DATE,to_char(LAST_MODIFICATION_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as LAST_MODIFICATION_DATE FROM EMS_DASHBOARD_TILE_PARAMS WHERE TILE_ID=? AND PARAM_NAME=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, tileId)
				.setParameter(2, paramName)
				.setParameter(3, tenantId);
		query.setHint(QueryHints.RESULT_TYPE, ResultType.Map);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> result = query.getResultList();
		return result;
	}



	private List<Map<String, Object>> getLastAccessDateForDashboardUserOption(EntityManager entityManager, String userName, Long tenantId, BigInteger dashboardId) {
		logger.info("Calling DataManager.getLastAccessDateForDashboardUserOption");
		String sql = "SELECT to_char(CREATION_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as CREATION_DATE,to_char(LAST_MODIFICATION_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as LAST_MODIFICATION_DATE FROM EMS_DASHBOARD_USER_OPTIONS WHERE USER_NAME=? AND TENANT_ID=? AND DASHBOARD_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, userName)
				.setParameter(2, tenantId)
				.setParameter(3, dashboardId);
		query.setHint(QueryHints.RESULT_TYPE, ResultType.Map);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> result = query.getResultList();
		return result;
	}

	private List<Map<String, Object>> getLastAccessDateForDashboardSet(EntityManager entityManager, BigInteger dashboardSetId, Long tenantId, BigInteger subDashboardId) {
		logger.info("Calling DataManager.getLastAccessDateForDashboardSet");
		String sql = "SELECT to_char(CREATION_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as CREATION_DATE,to_char(LAST_MODIFICATION_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as LAST_MODIFICATION_DATE FROM EMS_DASHBOARD_SET WHERE DASHBOARD_SET_ID=? AND TENANT_ID=? AND SUB_DASHBOARD_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, dashboardSetId)
				.setParameter(2, tenantId)
				.setParameter(3, subDashboardId);
		query.setHint(QueryHints.RESULT_TYPE, ResultType.Map);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> result = query.getResultList();
		return result;
	}

	private List<Map<String, Object>> getLastAccessDateForPreference(EntityManager entityManager, String userName, String prefKey, Long tenantId) {
		logger.info("Calling DataManager.getLastAccessDateForPreference");
		String sql = "SELECT to_char(CREATION_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as CREATION_DATE,to_char(LAST_MODIFICATION_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as LAST_MODIFICATION_DATE FROM EMS_PREFERENCE WHERE USER_NAME=? AND PREF_KEY=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, userName)
				.setParameter(2, prefKey)
				.setParameter(3, tenantId);
		query.setHint(QueryHints.RESULT_TYPE, ResultType.Map);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> result = query.getResultList();
		
		return result;
	}

	private boolean isAfter(String thisDate, String comparedDate){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			return simpleDateFormat.parse(thisDate).after(simpleDateFormat.parse(comparedDate));
		}catch (Exception e){
			logger.error(e);
			return false;
		}
	}

	public Map<String, Object> checkHalfSyncRecord(EntityManager em) throws HalfSyncException {
		List<Object> result = getSingleTableData(em,SQL_GET_HALF_SYNC_RECORD);
		if(result == null || result.size() == 0){
			logger.info("No half sync record found in sync table!");
			return null;
		}
		if(result.size() > 1){
			logger.error("'SYNC_TYPE = half' record is more than 1, which is unexpected. ");
			throw new HalfSyncException("'SYNC_TYPE = half' record is more than 1, which is unexpected. ");
		}
		logger.info("half-sync record found in sync table...");
		//return the only 1 half record
		return (Map<String, Object>)(result.get(0));
	}

	public int updateHalfSyncStatus(String syncResult, String type){

		logger.info("Prepare to update half sync status.");
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade();
			em =dsf.getEntityManager();
			//check half-sync record number first
			checkHalfSyncRecord(em);
			if (!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
			String sql = "update EMS_ZDT_SYNC set SYNC_RESULT= ? where SYNC_TYPE = 'half'";
			String sql2 = "update EMS_ZDT_SYNC set SYNC_RESULT= ?, SYNC_TYPE=? where SYNC_TYPE = 'half'";
			if(type !=null){
				em.createNativeQuery(sql).setParameter(1, syncResult).setParameter(2, type).executeUpdate();
			}else{
				em.createNativeQuery(sql).setParameter(1, syncResult).executeUpdate();
			}
			em.createNativeQuery(sql).setParameter(1, syncResult).executeUpdate();
			em.getTransaction().commit();
		}catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("errors occurs in updateHalfSyncStatus,{}", e);
			return -1;
//			throw new HalfSyncException("Errors occurs in updateHalfSyncStatus");
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return 0;
	}

    public Map<String, Object> getHalfSyncedComparedData(EntityManager em, String lastComparisonDateForSync)throws HalfSyncException, NoComparedResultException {
        logger.info("Get half synced compared data for last compared date is {}", lastComparisonDateForSync);
        if(lastComparisonDateForSync == null){
            logger.error("lastComparisonDateForSync can not be null for getting half synced compared data");
            throw new HalfSyncException("lastComparisonDateForSync can not be null for getting half synced compared data");
        }

        List<Map<String, Object>> result = getDatabaseTableData(em,SQL_GET_HALF_SYNC_COMPARED_DATA_TO_SYNC, lastComparisonDateForSync,null, null);

        if(result == null){
            logger.error("No compared result found for last compared date {}", lastComparisonDateForSync);
            throw new NoComparedResultException("No compared result found for compared date "+ lastComparisonDateForSync);
        }
        //FIXME could throw NPE
        return result.get(0);
    }
}
