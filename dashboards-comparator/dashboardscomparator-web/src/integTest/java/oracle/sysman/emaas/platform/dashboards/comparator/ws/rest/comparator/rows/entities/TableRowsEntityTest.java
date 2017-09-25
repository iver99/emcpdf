package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities;

import org.testng.annotations.Test;

import java.util.ArrayList;

/**
 * Created by chehao on 9/22/2017 15:51.
 */
@Test(groups = { "s1" })
public class TableRowsEntityTest {
   /* @Test
    public void testTableRowsEntity(){
        TableRowsEntity rowsEntityTest = new TableRowsEntity();
        rowsEntityTest
    }*/
   @Test
   public void testEquals() throws Exception {
       tableRowsEntity.setEmsPreference(new ArrayList<PreferenceRowEntity>());
       tableRowsEntity.getEmsPreference();
       tableRowsEntity.setEmsDashboardUserOptions(new ArrayList<DashboardUserOptionsRowEntity>());
       tableRowsEntity.getEmsDashboardUserOptions();
       tableRowsEntity.setEmsDashboard(new ArrayList<DashboardRowEntity>());
       tableRowsEntity.getEmsDashboard();
       tableRowsEntity.setEmsDashboardTile(new ArrayList<DashboardTileRowEntity>());
       tableRowsEntity.getEmsDashboardTile();
       tableRowsEntity.setEmsDashboardSet(new ArrayList<DashboardSetRowEntity>());
       tableRowsEntity.getEmsDashboardSet();
       tableRowsEntity.setEmsDashboardTileParams(new ArrayList<DashboardTileParamsRowEntity>());
       tableRowsEntity.getEmsDashboardTileParams();
       TableRowsEntity instance = new TableRowsEntity();
       instance.setEmsPreference(new ArrayList<PreferenceRowEntity>());
       instance.setEmsDashboardUserOptions(new ArrayList<DashboardUserOptionsRowEntity>());
       instance.setEmsDashboard(new ArrayList<DashboardRowEntity>());
       instance.setEmsDashboardTile(new ArrayList<DashboardTileRowEntity>());
       instance.setEmsDashboardSet(new ArrayList<DashboardSetRowEntity>());
       instance.setEmsDashboardTileParams(new ArrayList<DashboardTileParamsRowEntity>());
       tableRowsEntity.equals(instance);
       tableRowsEntity.equals(null);
       tableRowsEntity.equals(new Integer(1));
       tableRowsEntity.toString();
       tableRowsEntity.hashCode();
       tableRowsEntity = new TableRowsEntity();
       instance = new TableRowsEntity();
       tableRowsEntity.equals(instance);
   }

    private DashboardRowEntity dashboardRowEntity = new DashboardRowEntity();
    @Test
    public void testDashboardRowEntity(){
        dashboardRowEntity.setDashboardId("1");
        dashboardRowEntity.getDashboardId();
        dashboardRowEntity.setName("name");
        dashboardRowEntity.getName();
        dashboardRowEntity.setType(1L);
        dashboardRowEntity.getType();
        dashboardRowEntity.setDescription("desc");
        dashboardRowEntity.getDescription();
        dashboardRowEntity.setCreationDate("creationdate");
        dashboardRowEntity.getCreationDate();
        dashboardRowEntity.setLastModificationDate("last");
        dashboardRowEntity.getLastModificationDate();
        dashboardRowEntity.setLastModifiedBy("modifiedby");
        dashboardRowEntity.getLastModifiedBy();
        dashboardRowEntity.setOwner("owner");
        dashboardRowEntity.getOwner();
        dashboardRowEntity.setIsSystem(1);
        dashboardRowEntity.getIsSystem();
        dashboardRowEntity.setApplicationType(1);
        dashboardRowEntity.getApplicationType();
        dashboardRowEntity.setEnableTimeRange(1);
        dashboardRowEntity.getEnableTimeRange();
        dashboardRowEntity.setScreenShot("screenshot");
        dashboardRowEntity.getScreenShot();
        dashboardRowEntity.setDeleted("1");
        dashboardRowEntity.getDeleted();
        dashboardRowEntity.setTenantId(1L);
        dashboardRowEntity.getTenantId();
        dashboardRowEntity.setEnableRefresh(1);
        dashboardRowEntity.getEnableRefresh();
        dashboardRowEntity.setSharePublic(1);
        dashboardRowEntity.getSharePublic();
        dashboardRowEntity.setEnableEntityFilter(1);
        dashboardRowEntity.getEnableEntityFilter();
        dashboardRowEntity.setEnableDescription(1);
        dashboardRowEntity.getEnableDescription();
        dashboardRowEntity.setExtendedOptions("options");
        dashboardRowEntity.getExtendedOptions();
        dashboardRowEntity.hashCode();
        dashboardRowEntity.toString();
        DashboardRowEntity instance = dashboardRowEntity;
        dashboardRowEntity.equals(instance);
        instance = new DashboardRowEntity();
        dashboardRowEntity.equals(instance);
        dashboardRowEntity.equals(new Integer(1));
        instance = new DashboardRowEntity();
        instance.setDashboardId("1");
        instance.setName("name");
        instance.setType(1L);
        instance.setDescription("desc");
        instance.setCreationDate("creationdate");
        instance.setLastModificationDate("last");
        instance.setLastModifiedBy("modifiedby");
        instance.setOwner("owner");
        instance.setIsSystem(1);
        instance.setApplicationType(1);
        instance.setEnableTimeRange(1);
        instance.setScreenShot("screenshot");
        instance.setDeleted("1");
        instance.setTenantId(1L);
        instance.setEnableRefresh(1);
        instance.setSharePublic(1);
        instance.setEnableEntityFilter(1);
        instance.setEnableDescription(1);
        instance.setExtendedOptions("options");
        dashboardRowEntity.equals(instance);
        dashboardRowEntity = new DashboardRowEntity();
        instance = new DashboardRowEntity();
        dashboardRowEntity.equals(instance);
        dashboardRowEntity.equals(null);
    }

    private DashboardSetRowEntity dashboardSetRowEntity = new DashboardSetRowEntity();
    @Test
    public void testDashboardSetRowEntity(){
        dashboardSetRowEntity.setDashboardSetId( "1");
        dashboardSetRowEntity.getDashboardSetId();
        dashboardSetRowEntity.setTenantId(1L);
        dashboardSetRowEntity.getTenantId();
        dashboardSetRowEntity.setSubDashboardId("1");
        dashboardSetRowEntity.getSubDashboardId();
        dashboardSetRowEntity.setPosition(1L);
        dashboardSetRowEntity.getPosition();
        dashboardSetRowEntity.setCreationDate("creationdate");
        dashboardSetRowEntity.getCreationDate();
        dashboardSetRowEntity.setLastModificationDate("last");
        dashboardSetRowEntity.getLastModificationDate();
        dashboardSetRowEntity.hashCode();
        dashboardSetRowEntity.toString();
        dashboardSetRowEntity.equals(null);
        dashboardSetRowEntity.equals(new DashboardSetRowEntity());
        dashboardSetRowEntity.equals(dashboardRowEntity);
        dashboardSetRowEntity.equals(new Integer(1));
        dashboardSetRowEntity = new DashboardSetRowEntity();
        dashboardSetRowEntity.equals(new DashboardSetRowEntity());
    }
    private DashboardTileParamsRowEntity dashboardTileParamsRowEntity = new DashboardTileParamsRowEntity();
    @Test
    public void testDashboardTileParamsRowEntity(){
        dashboardTileParamsRowEntity.setTileId("1");
        dashboardTileParamsRowEntity.setParamName("name");
        dashboardTileParamsRowEntity.setTenantId(1L);
        dashboardTileParamsRowEntity.setIsSystem(1);
        dashboardTileParamsRowEntity.setParamType(1L);
        dashboardTileParamsRowEntity.setParamValueStr("value");
        dashboardTileParamsRowEntity.setParamValueNum(1L);
        dashboardTileParamsRowEntity.setParamValueTimestamp("timestamp");
        dashboardTileParamsRowEntity.setCreationDate("creation");
        dashboardTileParamsRowEntity.setLastModificationDate("lastmodification");

        dashboardTileParamsRowEntity.getTileId();
        dashboardTileParamsRowEntity.getParamName();
        dashboardTileParamsRowEntity.getTenantId();
        dashboardTileParamsRowEntity.getIsSystem();
        dashboardTileParamsRowEntity.getParamType();
        dashboardTileParamsRowEntity.getParamValueStr();
        dashboardTileParamsRowEntity.getParamValueNum();
        dashboardTileParamsRowEntity.getParamValueTimestamp();
        dashboardTileParamsRowEntity.getCreationDate();
        dashboardTileParamsRowEntity.getLastModificationDate();

        dashboardTileParamsRowEntity.hashCode();
        dashboardTileParamsRowEntity.toString();
        dashboardTileParamsRowEntity.equals(null);
        dashboardTileParamsRowEntity.equals(new DashboardTileParamsRowEntity());
        dashboardTileParamsRowEntity.equals(dashboardTileParamsRowEntity);
        dashboardTileParamsRowEntity = new DashboardTileParamsRowEntity();
        dashboardTileParamsRowEntity.equals(new DashboardTileParamsRowEntity());
        dashboardTileParamsRowEntity.equals(new Integer(1));

    }

    private DashboardTileRowEntity dashboardTileRowEntity = new DashboardTileRowEntity();
    @Test
    public void testDashboardTileRowEntity(){
        dashboardTileRowEntity.setTenantId(1L);
        dashboardTileRowEntity.setDashboardId("1");
        dashboardTileRowEntity.setCreationDate("creation");
        dashboardTileRowEntity.setLastModificationDate("last");
        dashboardTileRowEntity.setLastModifiedBy("lastmodified");
        dashboardTileRowEntity.setOwner("owner");
        dashboardTileRowEntity.setTitle("title");
        dashboardTileRowEntity.setHeight(1L);
        dashboardTileRowEntity.setWidth(1L);
        dashboardTileRowEntity.setIsMaximized(1);
        dashboardTileRowEntity.setPosition(1L);
        dashboardTileRowEntity.setTenantId(1L);
        dashboardTileRowEntity.setWidgetUniqueId("uniqueId");
        dashboardTileRowEntity.setWidgetName("name");
        dashboardTileRowEntity.setWidgetGroupName("groupname");
        dashboardTileRowEntity.setWidgetDescription("desc");
        dashboardTileRowEntity.setWidgetIcon("icon");
        dashboardTileRowEntity.setWidgetHistogram("hisrogram");
        dashboardTileRowEntity.setWidgetOwner("owner");
        dashboardTileRowEntity.setWidgetCreationTime("time");
        dashboardTileRowEntity.setWidgetSource(1L);
        dashboardTileRowEntity.setWidgetKocName("name");
        dashboardTileRowEntity.setWidgetViewmode("viewmode");
        dashboardTileRowEntity.setWidgetTemplate("template");
        dashboardTileRowEntity.setProviderName("name");
        dashboardTileRowEntity.setProviderVersion("version");
        dashboardTileRowEntity.setProviderAssetRoot("assetroot");
        dashboardTileRowEntity.setTileRow(1L);
        dashboardTileRowEntity.setTileColumn(1L);
        dashboardTileRowEntity.setTenantId(1L);
        dashboardTileRowEntity.setWidgetSupportTimeControl(1);
        dashboardTileRowEntity.setWidgetLinkedDashboard(1L);
        dashboardTileRowEntity.setTileId("1");
        dashboardTileRowEntity.setType(1L);
        dashboardTileRowEntity.getTenantId();
        dashboardTileRowEntity.getDashboardId();
        dashboardTileRowEntity.getCreationDate();
        dashboardTileRowEntity.getLastModificationDate();
        dashboardTileRowEntity.getLastModifiedBy();
        dashboardTileRowEntity.getOwner();
        dashboardTileRowEntity.getTitle();
        dashboardTileRowEntity.getHeight();
        dashboardTileRowEntity.getWidth();
        dashboardTileRowEntity.getIsMaximized();
        dashboardTileRowEntity.getPosition();
        dashboardTileRowEntity.getTenantId();
        dashboardTileRowEntity.getWidgetUniqueId();
        dashboardTileRowEntity.getWidgetName();
        dashboardTileRowEntity.getWidgetGroupName();
        dashboardTileRowEntity.getWidgetDescription();
        dashboardTileRowEntity.getWidgetIcon();
        dashboardTileRowEntity.getWidgetHistogram();
        dashboardTileRowEntity.getWidgetOwner();
        dashboardTileRowEntity.getWidgetCreationTime();
        dashboardTileRowEntity.getWidgetSource();
        dashboardTileRowEntity.getWidgetKocName();
        dashboardTileRowEntity.getWidgetViewmode();
        dashboardTileRowEntity.getWidgetTemplate();
        dashboardTileRowEntity.getProviderName();
        dashboardTileRowEntity.getProviderVersion();
        dashboardTileRowEntity.getProviderAssetRoot();
        dashboardTileRowEntity.getTileRow();
        dashboardTileRowEntity.getTileColumn();
        dashboardTileRowEntity.getTenantId();
        dashboardTileRowEntity.getWidgetSupportTimeControl();
        dashboardTileRowEntity.getWidgetLinkedDashboard();
        dashboardTileRowEntity.getType();
        dashboardTileRowEntity.getTileId();

        dashboardTileRowEntity.hashCode();
        dashboardTileRowEntity.toString();
        dashboardTileRowEntity.equals(new DashboardTileRowEntity());
        dashboardTileRowEntity.equals(null);
        dashboardTileRowEntity = new DashboardTileRowEntity();
        dashboardTileRowEntity.equals(new Integer(1));
        dashboardTileRowEntity.equals(new DashboardTileRowEntity());


    }

    private DashboardUserOptionsRowEntity dashboardUserOptionsRowEntity = new DashboardUserOptionsRowEntity();
    @Test
    public void testDashboardUserOptionsRowEntity(){
        dashboardUserOptionsRowEntity.setUserName("name");
        dashboardUserOptionsRowEntity.setTenantId(1L);
        dashboardUserOptionsRowEntity.setDashboardId("1");
        dashboardUserOptionsRowEntity.setAutoRefreshInterval(1L);
        dashboardUserOptionsRowEntity.setAccessDate("sccessdate");
        dashboardUserOptionsRowEntity.setIsFavorite(1);
        dashboardUserOptionsRowEntity.setExtendedOptions("extend");
        dashboardUserOptionsRowEntity.setIsFavorite(1);
        dashboardUserOptionsRowEntity.setCreationDate("creation");
        dashboardUserOptionsRowEntity.setLastModificationDate("last");

        dashboardUserOptionsRowEntity.getUserName();
        dashboardUserOptionsRowEntity.getTenantId();
        dashboardUserOptionsRowEntity.getDashboardId();
        dashboardUserOptionsRowEntity.getAutoRefreshInterval();
        dashboardUserOptionsRowEntity.getAccessDate();
        dashboardUserOptionsRowEntity.getIsFavorite();
        dashboardUserOptionsRowEntity.getExtendedOptions();
        dashboardUserOptionsRowEntity.getIsFavorite();
        dashboardUserOptionsRowEntity.getCreationDate();
        dashboardUserOptionsRowEntity.getLastModificationDate();

        dashboardUserOptionsRowEntity.hashCode();
        dashboardUserOptionsRowEntity.toString();
        dashboardUserOptionsRowEntity.equals(null);
        dashboardUserOptionsRowEntity.equals(new DashboardUserOptionsRowEntity());
        dashboardUserOptionsRowEntity = new DashboardUserOptionsRowEntity();
        dashboardUserOptionsRowEntity.equals(new DashboardUserOptionsRowEntity());
        dashboardUserOptionsRowEntity.equals(new Integer(1));

    }

    private PreferenceRowEntity preferenceRowEntity = new PreferenceRowEntity();
    @Test
    public void testPreferenceRowEntity(){
        preferenceRowEntity.setUserName("name");
        preferenceRowEntity.setPrefKey("key");
        preferenceRowEntity.setPrefValue("value");
        preferenceRowEntity.setTenantId(1L);
        preferenceRowEntity.setCreationDate("creation");
        preferenceRowEntity.setLastModificationDate("lastdate");

        preferenceRowEntity.getUserName();
        preferenceRowEntity.getPrefKey();
        preferenceRowEntity.getPrefValue();
        preferenceRowEntity.getTenantId();
        preferenceRowEntity.getCreationDate();
        preferenceRowEntity.getLastModificationDate();

        preferenceRowEntity.hashCode();
        preferenceRowEntity.toString();
        preferenceRowEntity.equals(null);
        preferenceRowEntity.equals(new PreferenceRowEntity());
        preferenceRowEntity.equals(new Integer(1));
        preferenceRowEntity = new PreferenceRowEntity();
        preferenceRowEntity.equals(new PreferenceRowEntity());

    }
    private TableRowsEntity tableRowsEntity = new TableRowsEntity();
}
