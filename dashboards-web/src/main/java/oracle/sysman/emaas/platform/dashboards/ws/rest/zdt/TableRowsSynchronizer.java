/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest.zdt;

import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.zdt.DataManager;
import oracle.sysman.emaas.platform.dashboards.core.zdt.exception.SyncException;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.List;

/**
 * @author guochen
 */
public class TableRowsSynchronizer {
    private static final Logger logger = LogManager.getLogger(TableRowsSynchronizer.class);

    public String sync(TableRowsEntity data) throws SyncException {
        if (data == null) {
            logger.error("Failed to sync for input data is null");
            throw new SyncException("Errors occurred while sync for tables, input data is null...");
        }
        EntityManager em = null;
        try {
        	DashboardServiceFacade dsf = new DashboardServiceFacade();
			em = dsf.getEntityManager();
			if (!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
            //make sure sync in order that will not break DB constraints
            syncPreferenceTableRows(em, data.getEmsPreference());
            syncDashboardTableRows(em, data.getEmsDashboard());
            syncDashboardSetTableRows(em, data.getEmsDashboardSet());
            syncDashboardUserOptionsTableRows(em, data.getEmsDashboardUserOptions());
            syncDashboardTileTableRows(em, data.getEmsDashboardTile());
            syncDashboardTileParamsTableRows(em, data.getEmsDashboardTileParams());
            em.getTransaction().commit();
            return "sync is successful";
        }catch (SyncException e){
            if(em!=null){
                em.getTransaction().rollback();
            }
            logger.error("SyncException occurred while sync for tables...");
            logger.error(e);
            throw e;
        }catch (Exception e) {
            if(em!=null){
                em.getTransaction().rollback();
            }
            logger.error("Errors occurred while sync for tables...");
            logger.error(e);
            logger.error(e.getCause());
            throw new SyncException("Errors occurred while sync for tables...");
        } finally {
			if (em != null) {
				em.close();
			}
		}
    }


    private int syncDashboardSetTableRows(EntityManager em,List<DashboardSetRowEntity> rows) throws SyncException {
        if (rows == null || rows.isEmpty()) {
            logger.warn("DashboardSetRows is empty or null!");
            return 0;
        }
        int result = 0;
        for (DashboardSetRowEntity dashboardSetRowEntity : rows) {
        	
            result += DataManager.getInstance().syncDashboardSet(em,new BigInteger(dashboardSetRowEntity.getDashboardSetId()), dashboardSetRowEntity.getTenantId(),
            		new BigInteger(dashboardSetRowEntity.getSubDashboardId()), dashboardSetRowEntity.getPosition(), dashboardSetRowEntity.getCreationDate(), dashboardSetRowEntity.getLastModificationDate(), new BigInteger(dashboardSetRowEntity.getDeleted()));
        	
        }
        return result;
    }

    private int syncDashboardTableRows(EntityManager em,List<DashboardRowEntity> dashboardRows) throws SyncException {
        if (dashboardRows == null || dashboardRows.isEmpty()) {
            logger.warn("dashboardRows is null or empty!");
            return 0;
        }
        int result = 0;
        for (DashboardRowEntity dashboardRowEntity : dashboardRows) {
        	
            result += DataManager.getInstance().syncDashboardTableRow(em,new BigInteger(dashboardRowEntity.getDashboardId()), dashboardRowEntity.getName(), dashboardRowEntity.getType(), dashboardRowEntity.getDescription()
                    , dashboardRowEntity.getCreationDate(), dashboardRowEntity.getLastModificationDate(), dashboardRowEntity.getLastModifiedBy(), dashboardRowEntity.getOwner()
                    , dashboardRowEntity.getIsSystem(), dashboardRowEntity.getApplicationType(), dashboardRowEntity.getEnableTimeRange(), dashboardRowEntity.getScreenShot()
                    , new BigInteger(dashboardRowEntity.getDeleted()), dashboardRowEntity.getTenantId(), dashboardRowEntity.getEnableRefresh(), dashboardRowEntity.getSharePublic()
                    , dashboardRowEntity.getEnableEntityFilter(), dashboardRowEntity.getEnableDescription(), dashboardRowEntity.getExtendedOptions(), dashboardRowEntity.getShowInHome());
        }
        return result;
    }


    private int syncDashboardTileParamsTableRows(EntityManager em,List<DashboardTileParamsRowEntity> rows) throws SyncException {
        if (rows == null || rows.isEmpty()) {
            logger.warn("TileParamsRows is null or empty!");
            return 0;
        }
        int result = 0;
        for (DashboardTileParamsRowEntity dashboardTileParamsRowEntity : rows) {
        	
            result += DataManager.getInstance().syncDashboardTileParam(em,dashboardTileParamsRowEntity.getTileId(), dashboardTileParamsRowEntity.getParamName()
                    , dashboardTileParamsRowEntity.getTenantId(), dashboardTileParamsRowEntity.getIsSystem(), dashboardTileParamsRowEntity.getParamType()
                    , dashboardTileParamsRowEntity.getParamValueStr(), dashboardTileParamsRowEntity.getParamValueNum(), dashboardTileParamsRowEntity.getParamValueTimestamp()
                    , dashboardTileParamsRowEntity.getCreationDate(), dashboardTileParamsRowEntity.getLastModificationDate(),dashboardTileParamsRowEntity.getDeleted());
        }
        return result;
    }

    private int syncDashboardTileTableRows(EntityManager em,List<DashboardTileRowEntity> rows) throws SyncException {
        if (rows == null || rows.isEmpty()) {
            logger.warn("TileRows is null or empty!");
            return 0;
        }
        int result = 0;
        for (DashboardTileRowEntity dashboardTileRowEntity : rows) {
        	
            result += DataManager.getInstance().syncDashboardTile(em,dashboardTileRowEntity.getTileId(), new BigInteger(dashboardTileRowEntity.getDashboardId()), dashboardTileRowEntity.getCreationDate()
                    , dashboardTileRowEntity.getLastModificationDate(), dashboardTileRowEntity.getLastModifiedBy(), dashboardTileRowEntity.getOwner(), dashboardTileRowEntity.getTitle()
                    , dashboardTileRowEntity.getHeight(), dashboardTileRowEntity.getWidth(), dashboardTileRowEntity.getIsMaximized(), dashboardTileRowEntity.getPosition()
                    , dashboardTileRowEntity.getTenantId(), dashboardTileRowEntity.getWidgetUniqueId(), dashboardTileRowEntity.getWidgetName(), dashboardTileRowEntity.getWidgetDescription()
                    , dashboardTileRowEntity.getWidgetGroupName(), dashboardTileRowEntity.getWidgetIcon(), dashboardTileRowEntity.getWidgetHistogram(), dashboardTileRowEntity.getWidgetOwner()
                    , dashboardTileRowEntity.getWidgetCreationTime(), dashboardTileRowEntity.getWidgetSource(), dashboardTileRowEntity.getWidgetKocName(), dashboardTileRowEntity.getWidgetViewmode()
                    , dashboardTileRowEntity.getWidgetTemplate(), dashboardTileRowEntity.getProviderName(), dashboardTileRowEntity.getProviderVersion(), dashboardTileRowEntity.getProviderAssetRoot()
                    , dashboardTileRowEntity.getTileRow(), dashboardTileRowEntity.getTileColumn(), dashboardTileRowEntity.getType(), dashboardTileRowEntity.getWidgetSupportTimeControl()
                    , dashboardTileRowEntity.getWidgetLinkedDashboard(),  dashboardTileRowEntity.getWidgetDeleted(),dashboardTileRowEntity.getWidgetDeletionDate(),dashboardTileRowEntity.getDeleted());
        }
        return result;
    }

    private int syncDashboardUserOptionsTableRows(EntityManager em,List<DashboardUserOptionsRowEntity> rows) throws SyncException {
        if (rows == null || rows.isEmpty()) {
            logger.warn("DashboardUserOptionsRows is null or empty!");
            return 0;
        }
        int result = 0;
        for (DashboardUserOptionsRowEntity dashboardUserOptionsRowEntity : rows) {
        	
            result += DataManager.getInstance().syncDashboardUserOption(em,dashboardUserOptionsRowEntity.getUserName(), dashboardUserOptionsRowEntity.getTenantId()
                    , new BigInteger(dashboardUserOptionsRowEntity.getDashboardId()), dashboardUserOptionsRowEntity.getAutoRefreshInterval(), dashboardUserOptionsRowEntity.getAccessDate()
                    , dashboardUserOptionsRowEntity.getIsFavorite(), dashboardUserOptionsRowEntity.getExtendedOptions(), dashboardUserOptionsRowEntity.getCreationDate(), dashboardUserOptionsRowEntity.getLastModificationDate(),dashboardUserOptionsRowEntity.getDeleted());
        }
        return result;
    }

    private int syncPreferenceTableRows(EntityManager em,List<PreferenceRowEntity> rows) throws SyncException {
        if (rows == null || rows.isEmpty()) {
            logger.warn("PreferenceRows is null or empty!");
            return 0;
        }
        int result = 0;
        for (PreferenceRowEntity preferenceRowEntity : rows) {
        	
            result += DataManager.getInstance().syncPreferences(em,preferenceRowEntity.getUserName(), preferenceRowEntity.getPrefKey(), preferenceRowEntity.getPrefValue()
                    , preferenceRowEntity.getTenantId(), preferenceRowEntity.getCreationDate(), preferenceRowEntity.getLastModificationDate(), new Integer(preferenceRowEntity.getDeleted()));
        }
        return result;
    }
}
