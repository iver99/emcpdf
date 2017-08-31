/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.tenant.TenantIdProcessor;
import oracle.sysman.emaas.platform.dashboards.comparator.exception.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.exception.ZDTErrorConstants;
import oracle.sysman.emaas.platform.dashboards.comparator.exception.ZDTException;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts.CountsEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts.DashboardCountsComparator;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.DashboardRowsComparator;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.InstanceData;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.InstancesComparedData;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.TableRowsEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.ZDTComparatorStatusRowEntity;


@Path("/v1/comparator")
public class ZDTAPI
{
	public static class InstanceCounts
	{
		private String instanceName;
		private CountsEntity counts;

		public InstanceCounts(InstanceData<CountsEntity> data)
		{
			instanceName = data.getKey();
			counts = data.getData();
		}

		/**
		 * @return the counts
		 */
		public CountsEntity getCounts()
		{
			return counts;
		}

		/**
		 * @return the instanceName
		 */
		public String getInstanceName()
		{
			return instanceName;
		}

		/**
		 * @param counts
		 *            the counts to set
		 */
		public void setCounts(CountsEntity counts)
		{
			this.counts = counts;
		}

		/**
		 * @param instanceName
		 *            the instanceName to set
		 */
		public void setInstanceName(String instanceName)
		{
			this.instanceName = instanceName;
		}
	}

	public static class InstancesComapredCounts
	{
		private InstanceCounts instance1;
		private InstanceCounts instance2;

		/**
		 * @param instance1
		 * @param instance2
		 */
		public InstancesComapredCounts(InstanceCounts instance1, InstanceCounts instance2)
		{
			super();
			this.instance1 = instance1;
			this.instance2 = instance2;
		}

		/**
		 * @return the instance1
		 */
		public InstanceCounts getInstance1()
		{
			return instance1;
		}

		/**
		 * @return the instance2
		 */
		public InstanceCounts getInstance2()
		{
			return instance2;
		}

		/**
		 * @param instance1
		 *            the instance1 to set
		 */
		public void setInstance1(InstanceCounts instance1)
		{
			this.instance1 = instance1;
		}

		/**
		 * @param instance2
		 *            the instance2 to set
		 */
		public void setInstance2(InstanceCounts instance2)
		{
			this.instance2 = instance2;
		}
	}

	private static final Logger logger = LogManager.getLogger(ZDTAPI.class);

	public ZDTAPI()
	{
		super();
	}

	/**
	 * Compare data in 2 clouds, will list the counts of each table of 2 clouds.
	 * @param tenantIdParam
	 * @param userTenant
	 * @return
	 */
	@GET
	@Deprecated
	@Produces(MediaType.APPLICATION_JSON)
	public Response compareOnDF(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam)
         //   @HeaderParam(value = "X-REMOTE-USER") String userTenant)
	{
		logger.info("There is an incoming call from ZDT comparator API to compare");
		if (tenantIdParam == null){
			tenantIdParam = "CloudServices";
		}
		// this comparator invokes the 2 instances REST APIs and retrieves the counts for objects (like dashboards), and return the counts for each instance
		DashboardCountsComparator dcc = new DashboardCountsComparator();
		InstancesComparedData<CountsEntity> result = dcc.compare(tenantIdParam, null);  // changed
		InstancesComapredCounts ic = new InstancesComapredCounts(new InstanceCounts(result.getInstance1()),
				new InstanceCounts(result.getInstance2()));

		return Response.status(Status.OK).entity(JsonUtil.buildNormalMapper().toJson(ic)).build();
	}
	
	public Date getCurrentUTCTime()
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		long localNow = System.currentTimeMillis();
		long offset = cal.getTimeZone().getOffset(localNow);
		Date utcDate = new Date(localNow - offset);
		
		return utcDate;
	}
	
	public String getTimeString(Date date)
	{
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");  
		String dateStr = sdf.format(date);
		return dateStr;
	}

	/**
	 * this method is return the comparator status for omc instances.(will request zdt/compare/status API)
	 * @param tenantIdParam
	 * @param userTenant
	 * @return
	 */
	@GET
	@Path("compare/status")
	public Response getCompareStatus(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam)
          //  @HeaderParam(value = "X-REMOTE-USER") String userTenant) 
	{
		logger.info("incoming call from zdt comparator to get comparitor status");
		if (tenantIdParam == null){
			tenantIdParam = "CloudServices";
		}
		DashboardRowsComparator dcc = null;
		String response = null;
		try {
			dcc = new DashboardRowsComparator();
			response = dcc.retrieveComparatorStatusForOmcInstance(tenantIdParam, null); // changed
		} catch (ZDTException e1) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(e1))).build();
		} catch (Exception e2) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(e2))).build();
		}
		
		return Response.status(Status.OK).entity(response).build();
		
	}

	/**
	 * this method is return the sync status for omc instance.(Will request zdt/sync/status)
	 * @param tenantIdParam
	 * @param userTenant
	 * @return
	 */
	@GET
	@Path("sync/status")
	public Response getSyncStatus(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam)
            //@HeaderParam(value = "X-REMOTE-USER") String userTenant)
			{
		logger.info("incoming call from zdt comparator to get sync status");
		if (tenantIdParam == null){
			tenantIdParam = "CloudServices";
		}
		DashboardRowsComparator dcc = null;
		String response = null;
		try {
			dcc = new DashboardRowsComparator();
			response = dcc.retrieveSyncStatusForOmcInstance(tenantIdParam, null); // changed
		} catch (ZDTException e1) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(e1))).build();
		} catch (Exception e2) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(e2))).build();
		}
		
		return Response.status(Status.OK).entity(response).build();
		
	}
	
	private String getMaxTimeStampStr(int skipMinutes) {
		Date currentUtcDate = getCurrentUTCTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentUtcDate);
		cal.add(Calendar.MINUTE, (skipMinutes * (-1)));
		Date maxTimeStamp = cal.getTime();
		String maxTimeStampStr = getTimeString(maxTimeStamp);
		return maxTimeStampStr;
	}

	/**
	 *  will invoke compare action, #1.fetch data, #2.compare data, #3.store the different data into zdt table
	 * @param tenantIdParam
	 * @param userTenant
	 * @param compareType
	 * @param skipMinutes
	 * @return
	 */
	@GET
	@Path("compare")
	@Produces(MediaType.APPLICATION_JSON)
	public Response compareRows(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
           // @HeaderParam(value = "X-REMOTE-USER") String userTenant, 
            @QueryParam("type") @DefaultValue("incremental")  String compareType,
            @QueryParam("before") int skipMinutes) {
		if (tenantIdParam == null){
			tenantIdParam = "CloudServices";
		}
		
		logger.info("incoming call from zdt comparator to do row comparing");
		String message = "";
		int status = 200;
		if (compareType == null) {
			compareType = "incremental";
		}
		if (skipMinutes <= 0) {
			skipMinutes = 30;    // to check: what is the accurate default skipping time for comparator?
		}
		String maxComparedDate = null;
		if (skipMinutes > 0) {
			maxComparedDate = getMaxTimeStampStr(skipMinutes);
		}
		logger.info("the max compared date is "+maxComparedDate);
		
		try {
			DashboardRowsComparator dcc = new DashboardRowsComparator();
			String tenants1 = dcc.retrieveTenants(tenantIdParam, null,dcc.getClient1()); // changed			
			String tenants2 = dcc.retrieveTenants(tenantIdParam, null,dcc.getClient2()); // changed		
			if (tenants1 == null || tenants2 == null) {
				return Response.status(400).entity(new ErrorEntity(ZDTErrorConstants.NULL_TABLE_ROWS_ERROR_CODE, ZDTErrorConstants.NULL_TABLE_ROWS_ERROR_MESSAGE)).build();
			}
			JSONArray tenantArrayForClient1 = null;
			
			JSONObject obj1 = new JSONObject(tenants1);
			boolean iscomparedForClient1 = obj1.getBoolean("isCompared");
			logger.info("iscompared1=" + iscomparedForClient1);
			tenantArrayForClient1 = obj1.getJSONArray("tenants");
			logger.info("tenantArray size1 = " + tenantArrayForClient1.length());
			
			JSONArray tenantArrayForClient2 = null;
			
			JSONObject obj2 = new JSONObject(tenants2);
			boolean iscomparedForClient2 = obj2.getBoolean("isCompared");
			logger.info("iscompared2=" + iscomparedForClient2);
			tenantArrayForClient2 = obj2.getJSONArray("tenants");
			logger.info("tenantArray size2 = " + tenantArrayForClient2.length());
			
			if (tenantArrayForClient1.length() == 0 && tenantArrayForClient2.length() == 0) {
				return Response.status(status).entity("No user created dashboards, Nothing to compare").build();
			}
			
			Set<String> tenants = new HashSet<String>();
			for (int i = 0; i < tenantArrayForClient1.length(); i++) {
				String tenant = tenantArrayForClient1.get(i).toString();
				tenants.add(tenant);
			}
			for (int i = 0; i < tenantArrayForClient2.length(); i++) {
				String tenant = tenantArrayForClient2.get(i).toString();
	
				tenants.add(tenant);
			}
			
			boolean iscompared = true;
			if ((!iscomparedForClient1) || (!iscomparedForClient2)) {
				// any one of the clouds has not yet been compared, then they should be compared in full mode
				iscompared = false;							
			}
			InstancesComparedData<TableRowsEntity> result = null;
			int totalRowForClient1 = dcc.getTotalRowForOmcInstance(tenantIdParam, null,dcc.getClient1(), maxComparedDate);// changed		
			int totalRowForClient2 = dcc.getTotalRowForOmcInstance(tenantIdParam, null,dcc.getClient2(), maxComparedDate);// changed		
			int totalRow = totalRowForClient1 + totalRowForClient2;
			if (totalRow == 0) {
	
				return Response.status(status).entity("No user created dashboards, Nothing to compare").build();					
			}
			int totalDifferentRows = 0;
			
			if (!iscompared || compareType == "full") {
				int count = 0;
				JSONObject obj = null;
				JSONObject subObj = new JSONObject();
				StringBuffer sb1 = new StringBuffer();
				StringBuffer sb2 = new StringBuffer();
				//handle tenant one by one and save comparison result for each tenant
				for (String tenantStr : tenants) {
					count = count + 1;
					result = dcc.compare(tenantIdParam, null,compareType,maxComparedDate, // changed		
							iscompared, tenantStr);
					
					if (result != null) {
						int comparedDataNum = dcc.countForComparedRows(result.getInstance1().getData()) + dcc.countForComparedRows(result.getInstance2().getData());
						logger.info("comparedNum={}",comparedDataNum);
						
						totalDifferentRows = totalDifferentRows + comparedDataNum;
						
						double percentage = 0;
						Date currentUtcDate = getCurrentUTCTime();
						String comparisonDate = getTimeString(currentUtcDate);
						Calendar cal = Calendar.getInstance();
						cal.setTime(currentUtcDate);
						cal.add(Calendar.HOUR_OF_DAY, 12);
						Date nextScheduleDate = cal.getTime();
						String nextScheduleDateStr = getTimeString(nextScheduleDate);
						
						if (count == tenants.size()) {
							// the last tenant is fetched										
							double percen = (double)totalDifferentRows/(double)totalRow;
							DecimalFormat df = new DecimalFormat("#.##########");
							percentage = Double.parseDouble(df.format(percen));
						}
						
						JsonUtil jsonUtil = JsonUtil.buildNormalMapper();
						
						TableRowsEntity data1 = result.getInstance1().getData();
						String result1 = jsonUtil.toJson(data1);
						ZDTComparatorStatusRowEntity statusRow1 = new ZDTComparatorStatusRowEntity(comparisonDate,compareType,nextScheduleDateStr,percentage, result1);
										
						TableRowsEntity data2 = result.getInstance2().getData();
						String result2 = jsonUtil.toJson(data2);
						ZDTComparatorStatusRowEntity statusRow2 = new ZDTComparatorStatusRowEntity(comparisonDate,compareType,nextScheduleDateStr,percentage, result2);
						
						// save status information for client 1  -- switch data for sync
						LookupClient client1 = result.getInstance1().getClient();
						dcc.saveComparatorStatus(tenantIdParam,null, client1, statusRow2);// changed		
						
						// save status informantion for client 2 -- switch data for sync
						LookupClient client2 = result.getInstance2().getClient();
						dcc.saveComparatorStatus(tenantIdParam,null, client2, statusRow1);// changed		
						
						sb1.append(result1);
						
						
						sb2.append(result2);
						
						if (count == tenants.size()) {
							 obj = new JSONObject();
								obj.put("comparisonDateTime", comparisonDate);
								obj.put("comparisonType", compareType);
								obj.put("differentRowNum", totalDifferentRows);
								obj.put("totalRowNum", totalRow);
								obj.put("divergencePercentage", percentage);
								
								if (totalDifferentRows > 1000) {
									obj.put("divergenceSummary", "The number for different rows is more than 1000; There is too much content to display;");
								} else {
									
									subObj.put(result.getInstance1().getKey(), sb2.toString());
									subObj.put(result.getInstance2().getKey(), sb1.toString());
									obj.put("divergenceSummary", subObj);
								}	
						}						
						result = null;
						
					} else {
						Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(ZDTErrorConstants.NULL_LINK_ERROR_CODE, ZDTErrorConstants.NULL_LINK_ERROR_MESSAGE))).build();
					}
				}// end loop for tenants
				message = obj.toString(); 
			} else {
				result = dcc.compare(tenantIdParam, null,compareType,maxComparedDate,// changed		
						iscompared, null);
				
				if (result != null) {
					int comparedDataNum = dcc.countForComparedRows(result.getInstance1().getData()) + dcc.countForComparedRows(result.getInstance2().getData());
					logger.info("comparedNum={}",comparedDataNum);
					totalDifferentRows = totalDifferentRows + comparedDataNum;
					double percen = 0;
					if (comparedDataNum != 0) {
						percen = (double)comparedDataNum/(double)totalRow;
					}
					if (totalRow == 0) {
						return Response.status(status).entity("No user created dashboards, Nothing to compare").build();					
					}
					DecimalFormat df = new DecimalFormat("#.######");
					double percentage = Double.parseDouble(df.format(percen));
					Date currentUtcDate = getCurrentUTCTime();
					String comparisonDate = getTimeString(currentUtcDate);
					Calendar cal = Calendar.getInstance();
					cal.setTime(currentUtcDate);
					cal.add(Calendar.HOUR_OF_DAY, 12);
					Date nextScheduleDate = cal.getTime();
					String nextScheduleDateStr = getTimeString(nextScheduleDate);
					JsonUtil jsonUtil = JsonUtil.buildNormalMapper();
					
					TableRowsEntity data1 = result.getInstance1().getData();
					String result1 = jsonUtil.toJson(data1);
					ZDTComparatorStatusRowEntity statusRow1 = new ZDTComparatorStatusRowEntity(comparisonDate,compareType,nextScheduleDateStr,percentage, result1);
									
					TableRowsEntity data2 = result.getInstance2().getData();
					String result2 = jsonUtil.toJson(data2);
					ZDTComparatorStatusRowEntity statusRow2 = new ZDTComparatorStatusRowEntity(comparisonDate,compareType,nextScheduleDateStr,percentage, result2);
					
					// save status information for client 1  -- switch data for sync
					LookupClient client1 = result.getInstance1().getClient();
					dcc.saveComparatorStatus(tenantIdParam,null, client1, statusRow2);// changed		
					
					// save status informantion for client 2 -- switch data for sync
					LookupClient client2 = result.getInstance2().getClient();
					dcc.saveComparatorStatus(tenantIdParam,null, client2, statusRow1);// changed		
					
					JSONObject obj = new JSONObject();
					obj.put("comparisonDateTime", comparisonDate);
					obj.put("comparisonType", compareType);
					obj.put("differentRowNum", totalDifferentRows);
					obj.put("totalRowNum", totalRow);
					obj.put("divergencePercentage", percentage);
					
					JSONObject subObj = new JSONObject();
					subObj.put(result.getInstance1().getKey(), result2);
					subObj.put(result.getInstance2().getKey(), result1);
					obj.put("divergenceSummary", subObj);
				
					message = obj.toString();					
				} else {
					Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(ZDTErrorConstants.NULL_LINK_ERROR_CODE, ZDTErrorConstants.NULL_LINK_ERROR_MESSAGE))).build();
				}
			}
		} catch(ZDTException zdtE) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(zdtE))).build();
		}
		catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(e))).build();
		}
		
		return Response.status(status).entity(message).build();
	}

	/**
	 * will sync sync action, #1.retrieve data from zdt table, #2. sync action. #3. store sync status into sync table
	 * @param tenantIdParam
	 * @param userTenant
	 * @param syncType
	 * @return
	 */
	@GET
	@Path("sync")
	@Produces(MediaType.APPLICATION_JSON)
	public Response syncOnDF(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
            //@HeaderParam(value = "X-REMOTE-USER") String userTenant,
            @QueryParam("type") @DefaultValue("full")  String syncType)
	{
		logger.info("There is an incoming call from ZDT comparator API to sync");
		if (tenantIdParam == null){
			tenantIdParam = "CloudServices";
		}
		// this comparator invokes the 2 instances REST APIs and retrieves the different table rows for the 2 instances, and update the 2 instances accordingly
		DashboardRowsComparator dcc = null;
		if (syncType == null) {
			syncType = "full";
		}
		try {
			dcc = new DashboardRowsComparator();		
			Date currentUtcDate = getCurrentUTCTime();
			String syncDate = getTimeString(currentUtcDate);
			
			String message1 = dcc.syncForInstance(tenantIdParam,  null,  dcc.getClient1(),  syncType,  syncDate);// changed		
			String message2 = dcc.syncForInstance(tenantIdParam,  null,  dcc.getClient2(),  syncType,  syncDate);// changed		
			if (message1.contains("Errors") || message2.contains("Errors")) {
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(ZDTErrorConstants.FAIL_TO_SYNC_ERROR_CODE, ZDTErrorConstants.FAIL_TO_SYNC_ERROR_MESSAGE))).build();
			}
			JSONObject object = new JSONObject();
			object.put(dcc.getKey1(), message1);
			object.put(dcc.getKey2(), message2);
			return Response.ok(object).build();
	    } catch(ZDTException zdtE) {
 			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(zdtE))).build();
 		} catch (Exception e) {
 			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(e))).build();
 		}
		
	}

}
