package oracle.sysman.emaas.platform.dashboards.ws.rest.zdt;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;
import oracle.sysman.emaas.platform.dashboards.core.zdt.DataManager;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardSetRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardTileParamsRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardTileRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardUserOptionsRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.PreferenceRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.TableRowsEntity;

import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import static org.testng.Assert.*;

/**
 * Created by xiadai on 2016/8/11.
 */
@Test(groups = {"s1"})
public class TableRowsSynchronizerTest {
    @Mocked
    DataManager dataManager;


    private TableRowsSynchronizer tableRowsSynchronizer = new TableRowsSynchronizer();
    @Test
    public void testSync(@Mocked final DashboardServiceFacade serviceFacade, 
			@Mocked final EntityManager em) throws Exception {
        TableRowsEntity tableRowsEntity = new TableRowsEntity();

        PreferenceRowEntity preferenceRowEntity = new PreferenceRowEntity();
        DashboardSetRowEntity dashboardSetRowEntity = new DashboardSetRowEntity();
        DashboardUserOptionsRowEntity dashboardUserOptionsRowEntity = new DashboardUserOptionsRowEntity();
        DashboardTileRowEntity dashboardTileRowEntity = new DashboardTileRowEntity();
        DashboardTileParamsRowEntity dashboardTileParamsRowEntity = new DashboardTileParamsRowEntity();
        DashboardRowEntity dashboardRowEntity = new DashboardRowEntity();

        dashboardRowEntity.setDashboardId("1");
        dashboardRowEntity.setName("name");
        dashboardRowEntity.setType(1L);
        dashboardRowEntity.setDescription("desciption") ;
        dashboardRowEntity.setCreationDate("2016-08-11 13:00:00");
        dashboardRowEntity.setLastModificationDate("2016-08-11 13:30:11");
        dashboardRowEntity.setLastModifiedBy("ORACLE");
        dashboardRowEntity.setOwner("ORACLE") ;
        dashboardRowEntity.setIsSystem(1);
        dashboardRowEntity.setApplicationType(1);
        dashboardRowEntity.setEnableTimeRange(1);
        dashboardRowEntity.setScreenShot("") ;
        dashboardRowEntity.setDeleted("0");
        dashboardRowEntity.setTenantId(1L);
        dashboardRowEntity.setEnableRefresh(1);
        dashboardRowEntity.setSharePublic(1) ;
        dashboardRowEntity.setEnableEntityFilter(1);
        dashboardRowEntity.setEnableDescription(1);
        dashboardRowEntity.setExtendedOptions("options");
        dashboardRowEntity.setShowInHome(1);

        dashboardTileParamsRowEntity.setTileId("1");
        dashboardTileParamsRowEntity.setParamName("name");
        dashboardTileParamsRowEntity.setTenantId(1L);
        dashboardTileParamsRowEntity.setIsSystem(1);
        dashboardTileParamsRowEntity.setParamType(1L);
        dashboardTileParamsRowEntity.setParamValueStr("value");
        dashboardTileParamsRowEntity.setParamValueNum(1L);
        dashboardTileParamsRowEntity.setParamValueTimestamp("2016-08-11 13:31:00");
        dashboardTileParamsRowEntity.setCreationDate("2016-08-11 13:31:00");
        dashboardTileParamsRowEntity.setLastModificationDate("2016-08-11 13:31:00");
        dashboardTileParamsRowEntity.setDeleted(0);

        dashboardTileRowEntity.setTileId("1");
        dashboardTileRowEntity.setDashboardId("1");
        dashboardTileRowEntity.setCreationDate("2016-08-11 13:31:00");
        dashboardTileRowEntity.setLastModificationDate("2016-08-11 13:31:00");
        dashboardTileRowEntity.setLastModifiedBy("ORACLE");
        dashboardTileRowEntity.setOwner("ORACLE");
        dashboardTileRowEntity.setTitle("title");
        dashboardTileRowEntity.setHeight(1L);
        dashboardTileRowEntity.setWidth(1L);
        dashboardTileRowEntity.setIsMaximized(1);
        dashboardTileRowEntity.setPosition(1L);
        dashboardTileRowEntity.setTenantId(1L);
        dashboardTileRowEntity.setWidgetUniqueId("1");
        dashboardTileRowEntity.setWidgetName("name");
        dashboardTileRowEntity.setWidgetDescription("description");
        dashboardTileRowEntity.setWidgetGroupName("name");
        dashboardTileRowEntity.setWidgetIcon("icon");
        dashboardTileRowEntity.setWidgetHistogram("histogram");
        dashboardTileRowEntity.setWidgetOwner("ORACLE");
        dashboardTileRowEntity.setWidgetCreationTime("2016-08-11 13:31:00");
        dashboardTileRowEntity.setWidgetSource(1L);
        dashboardTileRowEntity.setWidgetKocName("name");
        dashboardTileRowEntity.setWidgetViewmode("viewmode");
        dashboardTileRowEntity.setWidgetTemplate("template");
        dashboardTileRowEntity.setProviderName("name");
        dashboardTileRowEntity.setProviderVersion("version");
        dashboardTileRowEntity.setProviderAssetRoot("assetroot");
        dashboardTileRowEntity.setTileRow(1L);
        dashboardTileRowEntity.setTileColumn(1L);
        dashboardTileRowEntity.setType(1L);
        dashboardTileRowEntity.setWidgetSupportTimeControl(1);
        dashboardTileRowEntity.setWidgetLinkedDashboard(1L);
        dashboardTileRowEntity.setWidgetDeletionDate("2016-08-11 13:31:00");
        dashboardTileRowEntity.setWidgetDeleted(0);
        dashboardTileRowEntity.setDeleted(0);

        dashboardUserOptionsRowEntity.setUserName("name");
        dashboardUserOptionsRowEntity.setTenantId(1L);
        dashboardUserOptionsRowEntity.setDashboardId("1");
        dashboardUserOptionsRowEntity.setAutoRefreshInterval(1L);
        dashboardUserOptionsRowEntity.setAccessDate("2016-08-11 13:31:00");
        dashboardUserOptionsRowEntity.setIsFavorite(1);
        dashboardUserOptionsRowEntity.setExtendedOptions("options");
        dashboardUserOptionsRowEntity.setCreationDate("2016-08-11 13:31:00");
        dashboardUserOptionsRowEntity.setLastModificationDate("2016-08-11 13:31:00");
        dashboardUserOptionsRowEntity.setDeleted(0);

        preferenceRowEntity.setUserName("name");
        preferenceRowEntity.setPrefKey("key");
        preferenceRowEntity.setPrefValue("value");
        preferenceRowEntity.setTenantId(1L);
        preferenceRowEntity.setCreationDate("2016-08-11 13:31:00");
        preferenceRowEntity.setLastModificationDate("2016-08-11 13:31:00");
        preferenceRowEntity.setDeleted("0");

        dashboardSetRowEntity.setDashboardSetId("1");
        dashboardSetRowEntity.setTenantId(1L);
        dashboardSetRowEntity.setSubDashboardId("1");
        dashboardSetRowEntity.setPosition(1L);
        dashboardSetRowEntity.setCreationDate("2016-08-11 13:31:00");
        dashboardSetRowEntity.setLastModificationDate("2016-08-11 13:31:00");
        dashboardSetRowEntity.setDeleted("0");

        List<PreferenceRowEntity> preferenceRowEntities = new ArrayList<>();
        preferenceRowEntities.add(preferenceRowEntity);
        List<DashboardRowEntity> dashboardRowEntities = new ArrayList<>();
        dashboardRowEntities.add(dashboardRowEntity);
        List<DashboardSetRowEntity> dashboardSetRowEntities = new ArrayList<>();
        dashboardSetRowEntities.add(dashboardSetRowEntity);
        List<DashboardUserOptionsRowEntity> dashboardUserOptionsRowEntities = new ArrayList<>();
        dashboardUserOptionsRowEntities.add(dashboardUserOptionsRowEntity);
        List<DashboardTileRowEntity> dashboardTileRowEntities = new ArrayList<>();
        dashboardTileRowEntities.add(dashboardTileRowEntity);
        List<DashboardTileParamsRowEntity> dashboardTileParamsRowEntities = new ArrayList<>();
        dashboardTileParamsRowEntities.add(dashboardTileParamsRowEntity);

        new Expectations(){
            {
                DataManager.getInstance();
                result = dataManager;
                dataManager.syncPreferences(em,anyString, anyString, anyString, anyLong, anyString, anyString,(Integer)any);
                result = 1;
                dataManager.syncDashboardTableRow(em,(BigInteger)any, anyString, anyLong, anyString, anyString,anyString, anyString, anyString, (Integer)any, (Integer)any,(Integer)any, anyString, (BigInteger)any, anyLong, (Integer)any, (Integer)any,(Integer)any, (Integer)any, anyString,(Integer)any);
                result = 1;
                dataManager.syncDashboardTile(em,anyString, (BigInteger)any, anyString, anyString, anyString, anyString, anyString, anyLong, anyLong, (Integer)any, anyLong,anyLong, anyString, anyString, anyString, anyString, anyString,anyString, anyString, anyString, anyLong,
                        anyString, anyString, anyString, anyString, anyString, anyString, anyLong, anyLong, anyLong, (Integer)any, anyLong,(Integer)any,anyString,(Integer)any);
                result = 1;
                dataManager.syncDashboardSet(em,(BigInteger)any, anyLong, (BigInteger)any, anyLong, anyString, anyString, (BigInteger)any);
                result = 1;
                dataManager.syncDashboardTileParam(em,anyString, anyString, anyLong, (Integer)any, anyLong, anyString, anyLong, anyString, anyString, anyString, (Integer)any);
                result = 1;
                dataManager.syncDashboardUserOption(em,anyString,anyLong, (BigInteger)any, anyLong, anyString, (Integer)any, anyString, anyString, anyString, (Integer)any);
                result = 1;
            }
        };

        tableRowsSynchronizer.sync(tableRowsEntity);

        tableRowsSynchronizer.sync(null);

        tableRowsEntity.setEmsDashboard(dashboardRowEntities);
        tableRowsEntity.setEmsDashboardSet(dashboardSetRowEntities);
        tableRowsEntity.setEmsDashboardTile(dashboardTileRowEntities);
        tableRowsEntity.setEmsDashboardTileParams(dashboardTileParamsRowEntities);
        tableRowsEntity.setEmsDashboardUserOptions(dashboardUserOptionsRowEntities);
        tableRowsEntity.setEmsPreference(preferenceRowEntities);

        tableRowsSynchronizer.sync(tableRowsEntity);


    }

}