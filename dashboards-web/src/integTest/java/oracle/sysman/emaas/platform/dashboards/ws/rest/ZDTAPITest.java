package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;
import oracle.sysman.emaas.platform.dashboards.core.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.core.zdt.DataManager;
import oracle.sysman.emaas.platform.dashboards.core.zdt.exception.HalfSyncException;
import oracle.sysman.emaas.platform.dashboards.core.zdt.exception.NoComparedResultException;
import oracle.sysman.emaas.platform.dashboards.core.zdt.exception.SyncException;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.TableRowsSynchronizer;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardSetRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardTileParamsRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardTileRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardUserOptionsRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.PreferenceRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.TableRowsEntity;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import static org.testng.Assert.*;

/**
 * Created by xiadai on 2017/1/9.
 */

@Test(groups = { "s2" })
public class ZDTAPITest {
    private ZDTAPI zdtapi = new ZDTAPI();
    @Mocked
    DataManager dataManager;
    @Mocked
    Throwable throwable;
    @Test
    public void testGetAllTableData(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager em) throws Exception {
        final List<Map<String, Object>> list = new ArrayList<>();
        new Expectations(){
            {
                DataManager.getInstance();
                result = dataManager;
                dataManager.getDashboardSetTableData(em, anyString,anyString,anyString,anyString);
                result = list;
            }
        };
        zdtapi.getAllTableData("incremental",null,"");
        zdtapi.getAllTableData("full", "2017-05-27", null);
    }

    @Test
    public void testGetAllTableDataException(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager em) throws Exception {
        final List<Map<String, Object>> list = new ArrayList<>();
        new Expectations(){
            {
                DataManager.getInstance();
                result = dataManager;
                dataManager.getDashboardSetTableData(em, anyString, anyString, anyString, anyString);
                result = new JSONException(throwable);
            }
        };
        zdtapi.getAllTableData("incremental", "2017-05-27", "");
        zdtapi.getAllTableData("full", "2017-05-27","");
    }

    @Test
    public void testGetEntitiesCoung(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager em) throws Exception {
        new Expectations(){
            {
                DataManager.getInstance();
                result = dataManager;
                dataManager.getAllDashboardsCount(em, "2017-05-27");
                result = 1;
                dataManager.getAllUserOptionsCount(em, "2017-05-27");
                result = 1;
                dataManager.getAllPreferencessCount(em, "2017-05-27");
                result = 1;
            }
        };
        zdtapi.getEntitiesCount( "2017-05-27");
    }

   /* @Test
    public void testSync(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager em) throws Exception {
        zdtapi.sync();
    }*/
    
    @Test
    public void testSplitTableRowEntity() {
    	TableRowsEntity entity = new TableRowsEntity();
    	List<DashboardRowEntity> dashboards = new ArrayList<DashboardRowEntity>();
    	dashboards.add(new DashboardRowEntity());
    	List<DashboardSetRowEntity> dashboardSets = new ArrayList<DashboardSetRowEntity>();
    	dashboardSets.add(new DashboardSetRowEntity());
    	List<DashboardTileRowEntity> tiles = new ArrayList<DashboardTileRowEntity>();
    	tiles.add(new DashboardTileRowEntity());
    	List<DashboardTileParamsRowEntity> tileParams = new ArrayList<DashboardTileParamsRowEntity>();
    	tileParams.add(new DashboardTileParamsRowEntity());
    	List<DashboardUserOptionsRowEntity> userOptions = new ArrayList<DashboardUserOptionsRowEntity>();
    	userOptions.add(new DashboardUserOptionsRowEntity());
    	List<PreferenceRowEntity> preference = new ArrayList<PreferenceRowEntity>();
    	preference.add(new PreferenceRowEntity());
    	entity.setEmsDashboard(dashboards);
    	entity.setEmsDashboardSet(dashboardSets);
    	entity.setEmsDashboardTile(tiles);
    	entity.setEmsDashboardTileParams(tileParams);
    	entity.setEmsDashboardUserOptions(userOptions);
    	entity.setEmsPreference(preference);
    	zdtapi.splitTableRowEntity(entity);
    }
    
    /*@Test
    public void testHalfSync(@Mocked final PersistenceManager persistenceManager,
                             @Mocked final EntityManager em, @Mocked final JsonUtil jsonUtil) throws Exception {
        final List<Map<String, Object>> comparedDataToSync = new ArrayList<Map<String, Object>>();
        final Map<String, Object> comparedData = new HashMap<String, Object>();
//        comparedData.put("COMPARISON_RESULT", "{ \"EMS_DASHBOARD_SET\": [{\"DASHBOARD_SET_ID\": \"888888\",\"TENANT_ID\": 1565220054,\"SUB_DASHBOARD_ID\": \"9999999\",\"POSITION\": 0,\"CREATION_DATE\": \"2017-03-17 07:46:51.07\",\"LAST_MODIFICATION_DATE\": \"2017-03-17 07:46:51.07\",\"DELETED\": \"0\"}]}");
        comparedData.put("SYNC_RESULT", 1);
        comparedData.put("COMPARISON_DATE", "2017-05-12 15:20:21");
        comparedDataToSync.add(comparedData);
        final Map<String, Object> halfSyncRecord = new HashMap<>();
        halfSyncRecord.put("SYNC_RESULT", 1);
        halfSyncRecord.put("COMPARISON_DATE", "2017-05-12 15:20:21");
        final TableRowsEntity data = new TableRowsEntity();
    	new Expectations() {
    		{
    			DataManager.getInstance();
                result = dataManager;
                dataManager.checkHalfSyncRecord(em);
                result = comparedData;
                dataManager.getHalfSyncedComparedData(em, anyString);
                result = halfSyncRecord;
                JsonUtil.buildNormalMapper();
                result = jsonUtil;

                jsonUtil.fromJson(anyString,TableRowsEntity.class);
                result = data;

//                dataManager.getComparedDataToSync(em, anyString);
//                result = comparedDataToSync;
    		}
    	};
    	zdtapi.sync();
    }*/
    
    @Test
    public void testGetAllTenants(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager em) {
    	final List<Object> tenants = new ArrayList<Object>();
    	tenants.add("tenant");
    	new Expectations() {
    		{
    			DataManager.getInstance();
    			result = dataManager;
    			dataManager.getLatestComparisonDateForCompare(em);
    			result = "date";
    			dataManager.getAllTenants(em);
    			result = tenants;
    		}
    	};
    	zdtapi.getAllTenants();
    }
    
    @Test
    public void testGetSyncStatus(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager em) {
    	 final List<Map<String, Object>> resultData = new ArrayList<Map<String, Object>>();
         Map<String, Object> data = new HashMap<String, Object>();
         data.put("SYNC_DATE", "2017-05-12 15:20:21");
         data.put("NEXT_SCHEDULE_SYNC_DATE", "2017-05-12 15:20:21");
         data.put("SYNC_TYPE", "full");
         data.put("DIVERGENCE_PERCENTAGE", 0.12);
         resultData.add(data);
    	
    	new Expectations() {
    		{
    			DataManager.getInstance();
                result = dataManager;
                dataManager.getSyncStatus(em);
                result = resultData;
    		}
    	};
    	zdtapi.getSyncStatus();
    }
    
    @Test
    public void testGetComparisonStatus(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager em) {
    	 final List<Map<String, Object>> resultData = new ArrayList<Map<String, Object>>();
         Map<String, Object> data = new HashMap<String, Object>();
         data.put("COMPARISON_DATE", "2017-05-12 15:20:21");
         data.put("NEXT_SCHEDULE_COMPARISON_DATE", "2017-05-12 15:20:21");
         data.put("COMPARISON_TYPE", "full");
         data.put("DIVERGENCE_PERCENTAGE", 0.12);
         resultData.add(data);
    	
    	new Expectations() {
    		{
    			DataManager.getInstance();
                result = dataManager;
                dataManager.getComparatorStatus(em);
                result = resultData;
    		}
    	};
    	zdtapi.getComparisonStatus();
    }
    
    @Test
    public void testSaveComparisonResult(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager em) throws JSONException {
    	String json  = "{\"lastComparisonDateTime\":\"2017-05-12 15:20:21\", \"comparisonType\":\"full\",\"comparisonResult\":\"{}\",\"divergencePercentage\":0.11,\"nextScheduledComparisonDateTime\":\"2017-05-12 15:20:21\"}";
    	JSONObject object = new JSONObject(json);
    	zdtapi.saveComparatorData(object);
    }
    
    @Test
    public void testSaveComparisonResult2(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager em) throws JSONException {
    	String json  = "{ \"comparisonType\":\"full\",\"comparisonResult\":\"{}\",\"divergencePercentage\":0.11,\"nextScheduledComparisonDateTime\":\"2017-05-12 15:20:21\"}";
    	JSONObject object = new JSONObject(json);
    	zdtapi.saveComparatorData(object);
    }

    @Test
    public void testHalfSync1(@Mocked final PersistenceManager persistenceManager,
                              @Mocked final EntityManager em, @Mocked final JsonUtil jsonUtil) throws Exception {
        final List<Map<String, Object>> comparedDataToSync = new ArrayList<Map<String, Object>>();
        final Map<String, Object> comparedData = new HashMap<String, Object>();
//        comparedData.put("COMPARISON_RESULT", "{ \"EMS_DASHBOARD_SET\": [{\"DASHBOARD_SET_ID\": \"888888\",\"TENANT_ID\": 1565220054,\"SUB_DASHBOARD_ID\": \"9999999\",\"POSITION\": 0,\"CREATION_DATE\": \"2017-03-17 07:46:51.07\",\"LAST_MODIFICATION_DATE\": \"2017-03-17 07:46:51.07\",\"DELETED\": \"0\"}]}");
        comparedData.put("SYNC_RESULT", 1);
        comparedData.put("COMPARISON_DATE", "2017-05-12 15:20:21");
        comparedDataToSync.add(comparedData);
        final Map<String, Object> halfSyncRecord = new HashMap<>();
        halfSyncRecord.put("COMPARISON_RESULT","data...");
        final TableRowsEntity data = new TableRowsEntity();
        new Expectations() {
            {
                DataManager.getInstance();
                result = dataManager;
                dataManager.checkHalfSyncRecord(em);
                result = comparedData;
                dataManager.getHalfSyncedComparedData(em, anyString);
                result =halfSyncRecord;
                JsonUtil.buildNormalMapper();
                result = jsonUtil;

                jsonUtil.fromJson(anyString,TableRowsEntity.class);
                result = data;
            }
        };
        zdtapi.sync();
    }

    @Test
    public void testHalfSyncException(@Mocked final PersistenceManager persistenceManager,
                                      @Mocked final EntityManager em, @Mocked final JsonUtil jsonUtil) throws Exception {
        final List<Map<String, Object>> comparedDataToSync = new ArrayList<Map<String, Object>>();
        final Map<String, Object> comparedData = new HashMap<String, Object>();
//        comparedData.put("COMPARISON_RESULT", "{ \"EMS_DASHBOARD_SET\": [{\"DASHBOARD_SET_ID\": \"888888\",\"TENANT_ID\": 1565220054,\"SUB_DASHBOARD_ID\": \"9999999\",\"POSITION\": 0,\"CREATION_DATE\": \"2017-03-17 07:46:51.07\",\"LAST_MODIFICATION_DATE\": \"2017-03-17 07:46:51.07\",\"DELETED\": \"0\"}]}");
        comparedData.put("SYNC_RESULT", 1);
        comparedData.put("COMPARISON_DATE", "2017-05-12 15:20:21");
        comparedDataToSync.add(comparedData);
        final Map<String, Object> halfSyncRecord = new HashMap<>();
        halfSyncRecord.put("COMPARISON_RESULT","data...");
        final TableRowsEntity data = new TableRowsEntity();
        new Expectations() {
            {
                DataManager.getInstance();
                result = dataManager;
                dataManager.checkHalfSyncRecord(em);
                result = comparedData;
                dataManager.getHalfSyncedComparedData(em, anyString);
                result = new SyncException("error");
            }
        };
        zdtapi.sync();

        new Expectations() {
            {
                DataManager.getInstance();
                result = dataManager;
                dataManager.checkHalfSyncRecord(em);
                result = new HalfSyncException("error");
            }
        };
        zdtapi.sync();

        new Expectations() {
            {
                DataManager.getInstance();
                result = dataManager;
                dataManager.checkHalfSyncRecord(em);
                result = comparedData;
                dataManager.getHalfSyncedComparedData(em, anyString);
                result = new NoComparedResultException("error");
            }
        };
        zdtapi.sync();
    }

    @Test
    public void testSync2(@Mocked final PersistenceManager persistenceManager,
                          @Mocked final EntityManager em, @Mocked final JsonUtil jsonUtil,final @Mocked TableRowsSynchronizer zdtSynchronizer) throws HalfSyncException, IOException, SyncException, NoComparedResultException {
        final List<Map<String, Object>> comparedDataToSyn  = new ArrayList<>();
        final Map<String, Object> map = new HashMap<>();
        map.put("COMPARISON_RESULT","result");
        map.put("COMPARISON_DATE","date");
        comparedDataToSyn.add(map);
        final TableRowsEntity data = new TableRowsEntity();
        List<DashboardRowEntity> dashboardRowEntity = new ArrayList<>();
        dashboardRowEntity.add(new DashboardRowEntity());
        data.setEmsDashboard(dashboardRowEntity);
        new Expectations() {
            {
                DataManager.getInstance();
                result = dataManager;
                dataManager.checkHalfSyncRecord(em);
                result = null;
                dataManager.getComparedDataToSync(em, anyString);
                result = comparedDataToSyn;

            }
        };
        zdtapi.sync();

    }

    @Test
    public void testGetComparedDateforSync(){
        Deencapsulation.invoke(zdtapi, "getComparedDateforSync", String.class,String.class);
        Deencapsulation.invoke(zdtapi, "getComparedDateforSync", "date1","date2");
    }
   

}