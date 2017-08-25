/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.zdt.DataManager;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.TableRowsSynchronizer;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.ZDTEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.*;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.PreferenceRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.TableRowsEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.ZDTComparatorStatusRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.ZDTSyncStatusRowEntity;


@Path("/v1/zdt")
public class ZDTAPI extends APIBase
{
	private static final Logger logger = LogManager.getLogger(ZDTAPI.class);

	private static final String TABLE_DATA_KEY_DASHBOARD = "EMS_DASHBOARD";
	private static final String TABLE_DATA_KEY_DASHBOARD_SET = "EMS_DASHBOARD_SET";
	private static final String TABLE_DATA_KEY_DASHBOARD_TILES = "EMS_DASHBOARD_TILE";
	private static final String TABLE_DATA_KEY_DASHBOARD_TILE_PARAMS = "EMS_DASHBOARD_TILE_PARAMS";
	private static final String TABLE_DATA_KEY_DASHBOARD_USER_OPTIONS = "EMS_DASHBOARD_USER_OPTIONS";
	private static final String TABLE_DATA_KEY_DASHBOARD_PREFERENCES = "EMS_PREFERENCE";

	public ZDTAPI()
	{
		super();
	}
	
	@GET
	@Path("tenants")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTenants() {
		infoInteractionLogAPIIncomingCall(null, null, "Service call to [GET] /v1/zdt/tenants");

		EntityManager em = null;
		JSONObject obj = new JSONObject();
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade();
			em = dsf.getEntityManager();
			String lastComparisonDate = DataManager.getInstance().getLatestComparisonDateForCompare(em);
			List<Object> tenants = DataManager.getInstance().getAllTenants(em);
			JSONArray array = new JSONArray();
			if (tenants != null) {
				for (Object tenant:tenants) {
					array.put(tenant.toString());
				}
			}
			boolean flag = true;
			if (lastComparisonDate == null) {
				flag =  false;
			}
			obj.put("isCompared", flag);
			obj.put("tenants", array);
			return Response.status(Status.OK).entity(obj).build();
		} catch (Exception e) {
			logger.error("errors in getting all tenants:"+e.getLocalizedMessage());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Errors:" + e.getLocalizedMessage()).build();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}
	

	public Date getCurrentUTCTime()
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		long localNow = System.currentTimeMillis();
		long offset = cal.getTimeZone().getOffset(localNow);
		Date utcDate = new Date(localNow - offset);
		
		return utcDate;
	}
	

	@GET
	@Path("tablerows")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTableData(@QueryParam("comparisonType") String type, @QueryParam("maxComparedDate") String maxComparedDate,
			@QueryParam("tenant") String tenant)
	{
		infoInteractionLogAPIIncomingCall(null, null, "Service call to [GET] /v1/zdt/tablerows?comparisonType=");

		JSONObject obj = new JSONObject();
		EntityManager em = null;
		if (type == null) {
			type = "incremental";
		}
		
		if (maxComparedDate == null) {
			Date date = getCurrentUTCTime();
			maxComparedDate = getTimeString(date);
		}
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade();
			em = dsf.getEntityManager();
			String lastComparisonDate = DataManager.getInstance().getLatestComparisonDateForCompare(em);
			JSONArray tableData = getDashboardTableData(em,type, lastComparisonDate,maxComparedDate, tenant);
			obj.put(TABLE_DATA_KEY_DASHBOARD, tableData);
			tableData = getDashboardSetTableData(em,type, lastComparisonDate,maxComparedDate, tenant);
			obj.put(TABLE_DATA_KEY_DASHBOARD_SET, tableData);
			tableData = getDashboardTileTableData(em,type, lastComparisonDate,maxComparedDate, tenant);
			obj.put(TABLE_DATA_KEY_DASHBOARD_TILES, tableData);
			tableData = getDashboardTileParamsTableData(em,type, lastComparisonDate,maxComparedDate, tenant);
			obj.put(TABLE_DATA_KEY_DASHBOARD_TILE_PARAMS, tableData);
			tableData = getDashboardUserOptionsTableData(em,type, lastComparisonDate,maxComparedDate, tenant);
			obj.put(TABLE_DATA_KEY_DASHBOARD_USER_OPTIONS, tableData);
			tableData = getPreferenceTableData(em,type, lastComparisonDate,maxComparedDate, tenant);
			obj.put(TABLE_DATA_KEY_DASHBOARD_PREFERENCES, tableData);
		}
		catch (JSONException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Errors:" + e.getLocalizedMessage()).build();
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
		return Response.status(Status.OK).entity(obj).build();
	}

	@GET
	@Path("counts")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEntitiesCount(@QueryParam("maxComparedDate") String maxComparedData)
	{
		infoInteractionLogAPIIncomingCall(null, null, "Service call to [GET] /v1/zdt/counts");
		EntityManager em = null;
		ZDTEntity zdte = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade();
			em = dsf.getEntityManager();
		/*	if (maxComparedData == null) {
				Date currentDate = new Date();
				SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
				maxComparedData = format.format(currentDate);
			}*/
			long dashboardCount = DataManager.getInstance().getAllDashboardsCount(em,maxComparedData);
			long userOptionsCount = DataManager.getInstance().getAllUserOptionsCount(em,maxComparedData);
			long preferenceCount = DataManager.getInstance().getAllPreferencessCount(em,maxComparedData);
			long dashboardSetCount = DataManager.getInstance().getAllDashboardSetCount(em,maxComparedData);
			long tileCount = DataManager.getInstance().getAllTileCount(em,maxComparedData);
			long tileParamCount = DataManager.getInstance().getAllTileParamsCount(em,maxComparedData);
			logger.debug("ZDT counters: dashboards count - {}, favorite count - {}, preference count - {}", dashboardCount,
					userOptionsCount, preferenceCount);
			zdte = new ZDTEntity(dashboardCount, userOptionsCount, preferenceCount,
					dashboardSetCount,tileCount,tileParamCount);
		
		} catch (Exception e) {
			logger.error("error while getting count of tables:",e);
		} finally {
			if (em != null) {
				em.close();
			}

		}
		return Response.ok(getJsonUtil().toJson(zdte)).build();
	}
	
	private  List<List<?>> splitList(List<?> list, int len) {
		if (list == null || list.size() == 0 || len < 1) {
			return null;
		}		 
		List<List<?>> result = new ArrayList<List<?>>();		 		 
		int size = list.size();
		int count = (size + len - 1) / len;		 	 
		for (int i = 0; i < count; i++) {			
			List<?> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
			result.add(subList);
		}
			return result;
		}
	
	public List<TableRowsEntity> splitTableRowEntity(TableRowsEntity originalEntity){
		List<TableRowsEntity> entities = new ArrayList<TableRowsEntity>();
		if (originalEntity != null) {
			List<List<?>> splitDashboard = null;
			List<List<?>> splitDashboardSet = null;
			List<List<?>> splitTile = null;
			List<List<?>> splitTileParams = null;
			List<List<?>> splitUserOptions = null;
			List<List<?>> splitPreference = null;
			// for each connection, we just sync 1000 rows
			int length = 1000;
			if (originalEntity.getEmsDashboard() != null) {
				splitDashboard = splitList(originalEntity.getEmsDashboard(), length);
			}
			
			if (originalEntity.getEmsDashboardSet() != null) {
				splitDashboardSet = splitList(originalEntity.getEmsDashboardSet(),length);
			}
			
			if (originalEntity.getEmsDashboardTile() != null) {
				splitTile = splitList(originalEntity.getEmsDashboardTile(),length);
			}
			
			if (originalEntity.getEmsDashboardTileParams() != null) {
				splitTileParams = splitList(originalEntity.getEmsDashboardTileParams(),length);
			}
			
			if (originalEntity.getEmsDashboardUserOptions() != null) {
				splitUserOptions = splitList(originalEntity.getEmsDashboardUserOptions(),length);
			}
			
			if (originalEntity.getEmsPreference() != null) {
				splitPreference = splitList(originalEntity.getEmsPreference(),length);
			}
			
			// sync search table first and then sync parameter table to avoid key constraints
			if (splitDashboard != null) {
				for (List<?> DashboardList : splitDashboard) {
					TableRowsEntity rowEntity = new TableRowsEntity();
					rowEntity.setEmsDashboard((List<DashboardRowEntity>) DashboardList);
					entities.add(rowEntity);
				}
			}
			if (splitDashboardSet != null) {
				for (List<?> dashboardSetList : splitDashboardSet) {					
					TableRowsEntity rowEntity = new TableRowsEntity();
					rowEntity.setEmsDashboardSet((List<DashboardSetRowEntity>) dashboardSetList);
					entities.add(rowEntity);
				}
			}
			
			if (splitTile != null) {
				for (List<?> tileList : splitTile) {
					TableRowsEntity rowEntity = new TableRowsEntity();
					rowEntity.setEmsDashboardTile((List<DashboardTileRowEntity>) tileList);
					entities.add(rowEntity);
				}
			}
			
			if (splitTileParams != null) {
				for (List<?> tileParams : splitTileParams) {
					TableRowsEntity rowEntity = new TableRowsEntity();
					rowEntity.setEmsDashboardTileParams((List<DashboardTileParamsRowEntity>) tileParams);
					entities.add(rowEntity);
				}
			}
			
			if (splitUserOptions != null) {
				for (List<?> userOptionsList : splitUserOptions) {
					TableRowsEntity rowEntity = new TableRowsEntity();
					rowEntity.setEmsDashboardUserOptions((List<DashboardUserOptionsRowEntity>) userOptionsList);
					entities.add(rowEntity);
				}
			}
			
			if (splitPreference != null) {
				for (List<?> preferenceList : splitPreference) {
					TableRowsEntity rowEntity = new TableRowsEntity();
					rowEntity.setEmsPreference((List<PreferenceRowEntity>) preferenceList);
					entities.add(rowEntity);
				}
			}
		}
		return entities;
	}

	@GET
	@Path("sync")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sync(@QueryParam("syncType") String type, @QueryParam("syncDate") String syncDate)
	{
		infoInteractionLogAPIIncomingCall(null, null, "Service call to (GET) /v1/zdt/sync?syncType=xx&syncDate=xx");
		logger.info("Service call to /v1/zdt/sync");
		TableRowsEntity data = null;
		String lastCompareDate = null;
		if (type == null) {
			type = "full";
		}
		logger.info("syncDate="+syncDate);
		EntityManager em = null;
		String lastComparisonDateForSync = null;
		List<Map<String, Object>> comparedDataToSync = null;
		try {			
			DashboardServiceFacade dsf = new DashboardServiceFacade();
			em = dsf.getEntityManager();
			lastComparisonDateForSync = DataManager.getInstance().getLastComparisonDateForSync(em);
			logger.info("lastComparisonDateForSync="+lastComparisonDateForSync);
			comparedDataToSync = DataManager.getInstance().getComparedDataToSync(em, lastComparisonDateForSync);
			logger.info("comparedDataToSync="+comparedDataToSync);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return Response.status(400).entity(e.getLocalizedMessage()).build();				
		} finally {
			if (em != null) {
				em.close();
			}
		}
		try {	
			if (comparedDataToSync != null && !comparedDataToSync.isEmpty()) {
				for (Map<String, Object> dataMap : comparedDataToSync) {
					Object compareResult = dataMap.get("COMPARISON_RESULT");
					Object compareDate = dataMap.get("COMPARISON_DATE");
					data = getJsonUtil().fromJson(compareResult.toString(), TableRowsEntity.class);
					List<TableRowsEntity> entities = splitTableRowEntity(data);
					String response = null;
					if (entities != null) {
						for (TableRowsEntity entity : entities) {
							response = new TableRowsSynchronizer().sync(entity);
						}
					}
					if (lastCompareDate != null) {
						if (lastCompareDate.compareTo( (String) compareDate) < 0) {
							lastCompareDate = (String) compareDate;
						}
					} else {
						lastCompareDate = (String) compareDate;
					}
					
					if (response != null && response.contains("Errors:")) {
						saveToSyncTable(syncDate, type, "FAILED",lastCompareDate);
						return Response.status(500).entity(response).build();
					}
				}
				int flag = saveToSyncTable(syncDate, type, "SUCCESSFUL",lastCompareDate);
				if (flag < 0) {
					return Response.status(500).entity("Fail to save sync status data").build();
				}
			} else {
				return Response.ok("Nothing to sync as no compared data").build();
			}
						
			return Response.ok("Sync is successful!").build();
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Errors:" + e.getLocalizedMessage()).build();
		} 
	}
	
	private int saveToSyncTable(String syncDateStr, String type, String syncResult, String lastComparisonDate) {
		Date syncDate = null;
		try {  
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");  
		    syncDate = sdf.parse(syncDateStr);  
		} catch (ParseException e) {  
		    logger.error(e);
		}
		String nextScheduleDateStr = null;
		Calendar cal = Calendar.getInstance();
		if (syncDate != null) {
			cal.setTime(syncDate);
			cal.add(Calendar.HOUR_OF_DAY, 6);
			Date nextScheduleDate = cal.getTime();
			nextScheduleDateStr = getTimeString(nextScheduleDate);
		}
		
		double divergencePercentage = 0.0; 
		return DataManager.getInstance().saveToSyncTable(syncDateStr, nextScheduleDateStr, type, syncResult, divergencePercentage, lastComparisonDate);		
	}
	
	private String getTimeString(Date date)
	{
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");  
		String dateStr = sdf.format(date);
		return dateStr;
	}
	
	@GET
	@Path("sync/status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSyncStatus() {
		infoInteractionLogAPIIncomingCall(null, null, "Service calling to (GET) /v1/zdt/sync/status");
		EntityManager em = null;
		String message = null;
		int statusCode = 200;
		String sync_date = "";
		String sync_type = "";
		String next_schedule_date = "";
		double percentage = 0.0;
		em =  new DashboardServiceFacade().getEntityManager();
		List<Map<String, Object>> result = DataManager.getInstance().getSyncStatus(em);
		if (result != null && result.size() == 1) {
				Map<String, Object> resultMap = result.get(0);
				sync_date = resultMap.get("SYNC_DATE").toString();
				sync_type = resultMap.get("SYNC_TYPE").toString();
				next_schedule_date =  resultMap.get("NEXT_SCHEDULE_SYNC_DATE").toString();
				percentage = Double.parseDouble(resultMap.get("DIVERGENCE_PERCENTAGE").toString());
		}
		ZDTSyncStatusRowEntity syncStatus = new ZDTSyncStatusRowEntity(sync_date,sync_type, next_schedule_date, percentage);
		try {
			message = getJsonUtil().toJson(syncStatus);
			return Response.status(statusCode).entity(message).build();
		} catch (Exception e) {
			message = "Errors:" + e.getLocalizedMessage();
			statusCode = 500;
			logger.error("Errors while transfer sync status object to json string {}",e.getLocalizedMessage());
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return Response.status(statusCode).entity(message).build();
	}
	
	@GET
	@Path("compare/status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getComparisonStatus() {
		infoInteractionLogAPIIncomingCall(null, null, "Service calling to (GET) /v1/zdt/compare/status");
		EntityManager em = null;
		String message = null;
		int statusCode = 200;
		String comparison_date = "";
		String comparison_type = "";
		//String comparison_result = null;
		String next_schedule_date = "";
		String percentage = "0.0";
		em =  new DashboardServiceFacade().getEntityManager();
		List<Map<String, Object>> result = DataManager.getInstance().getComparatorStatus(em);
		if (result != null && result.size() == 1) {
				Map<String, Object> resultMap = result.get(0);
				comparison_date = resultMap.get("COMPARISON_DATE").toString();
				comparison_type = resultMap.get("COMPARISON_TYPE").toString();
				next_schedule_date =  resultMap.get("NEXT_SCHEDULE_COMPARISON_DATE").toString();
				//	comparison_result = resultMap.get("").toString();
				percentage = resultMap.get("DIVERGENCE_PERCENTAGE").toString();
		}
		ZDTComparatorStatusRowEntity comparatorStatus = new ZDTComparatorStatusRowEntity(comparison_date,comparison_type, next_schedule_date, percentage);
		try {
			message = getJsonUtil().toJson(comparatorStatus);
			return Response.status(statusCode).entity(message).build();
		} catch (Exception e) {
			message = "Errors:" + e.getLocalizedMessage();
			statusCode = 500;
			logger.error("Errors while transfer comparator status object to json string {}",e.getLocalizedMessage());
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return Response.status(statusCode).entity(message).build();
	}

	@PUT
	@Path("compare/result")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveComparatorData(JSONObject jsonObj) {
		infoInteractionLogAPIIncomingCall(null, null, "Service calling to (PUT) /v1/zdt/compare/result");
		EntityManager em = null;
		String message = null;
		int statusCode = 200;
		String comparisonDate = null;
		String nextScheduleDate = null;
		String comparisonType = null;
		String comparisonResult = null;
		double divergencePercentage = 0;
		if (jsonObj != null) {
			try {
				if (jsonObj.getString("lastComparisonDateTime") == null) {
					message = "comparison date time can not be null";
					statusCode = 500;
				} else {
					try {
						comparisonDate = jsonObj.getString("lastComparisonDateTime");
						comparisonType = jsonObj.getString("comparisonType");
						comparisonResult = jsonObj.getString("comparisonResult");
						divergencePercentage = jsonObj.getDouble("divergencePercentage");
						nextScheduleDate = jsonObj.getString("nextScheduledComparisonDateTime");
						em =  new DashboardServiceFacade().getEntityManager();
						int result = DataManager.getInstance().saveToComparatorTable(em, comparisonDate,nextScheduleDate,
								comparisonType, comparisonResult, divergencePercentage);
						if (result < 0) {
							message = "Error: error occurs while saving data to zdt comparator table";
							statusCode = 500;
						} else {
							message = "succeed to save data to zdt comparator table";
						}		
					} catch (Exception e) {
						statusCode = 500;
						logger.error("could not save data to comparator table, "+e.getLocalizedMessage());
						return Response.status(statusCode).entity("could not save data to comparator table").build();						
					} finally {
						if (em != null) {
							em.close();
						}
					}
				}
			} catch (JSONException e) {
				statusCode = 500;
				logger.error("could not save data to comparator table, "+e.getLocalizedMessage());
				return Response.status(statusCode).entity("could not save data to comparator table").build();	
			}
		}
	
		return Response.status(statusCode).entity(message).build();
	}

	

	private JSONArray getDashboardSetTableData(EntityManager em, String type, String date, String maxComparedData, String tenant)
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardSetTableData(em, type,date,maxComparedData, tenant);
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_SET, list);
	}

	private JSONArray getDashboardTableData(EntityManager em, String type, String date, String maxComparedData, String tenant)
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardTableData(em, type,date,maxComparedData, tenant);
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD, list);
	}

	private JSONArray getDashboardTileParamsTableData(EntityManager em, String type, String date, String maxComparedData, String tenant)
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardTileParamsTableData(em ,type,date,maxComparedData, tenant);
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_TILE_PARAMS, list);
	}

	private JSONArray getDashboardTileTableData(EntityManager em, String type, String date, String maxComparedData, String tenant)
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardTileTableData(em, type,date,maxComparedData, tenant);
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_TILES, list);
	}

	private JSONArray getDashboardUserOptionsTableData(EntityManager em, String type, String date, String maxComparedData, String tenant)
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardUserOptionsTableData(em, type,date,maxComparedData, tenant);
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_USER_OPTIONS, list);
	}

	/**
	 * @param list
	 * @return
	 */
	private JSONArray getJSONArrayForListOfObjects(String dataName, List<Map<String, Object>> list)
	{
		if (list == null) {
			logger.warn("Trying to get a JSON object for {} from a null object/list. Returning null JSON object", dataName);
			return  new JSONArray();
		}
		JSONArray array = new JSONArray();
		for (Map<String, Object> row : list) {
			array.put(row);
		}
		logger.debug("Retrieved table data for {} is \"{}\"", dataName, array.toString());
		return array;
	}

	private JSONArray getPreferenceTableData(EntityManager em, String type, String date, String maxComparedData, String tenant)
	{
		List<Map<String, Object>> list = DataManager.getInstance().getPreferenceTableData(em, type,date,maxComparedData, tenant);
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_PREFERENCES, list);
	}
}
