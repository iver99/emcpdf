/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.CommonResourceException;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author reliang
 */
public class TenantManager {

    private static final Logger LOGGER = LogManager.getLogger(TenantManager.class);
    private static final TenantManager TENANT_MANAGER = new TenantManager();

    public static TenantManager getInstance() {
        return TENANT_MANAGER;
    }

    public void cleanTenant(Long internalTenantId) throws DashboardException {
        LOGGER.info("Start cleanTenant : {}", internalTenantId);
        EntityManager em = null;
        EntityTransaction entityTransaction = null;
        DashboardServiceFacade dsf = new DashboardServiceFacade();
        try {
            em = dsf.getEntityManager();
            entityTransaction = em.getTransaction();
            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }
            int deletedLastAccessCount = em.createNativeQuery("delete from EMS_DASHBOARD_LAST_ACCESS where tenant_id = ?1")
                    .setParameter(1, internalTenantId).executeUpdate();
            int deletedTileParamsCount = em.createNativeQuery("delete from ems_dashboard_tile_params where tenant_id = ?1")
                    .setParameter(1, internalTenantId).executeUpdate();
            int deletedTileCount = em.createNativeQuery("delete from ems_dashboard_tile where tenant_id = ?1")
                    .setParameter(1, internalTenantId).executeUpdate();
            int deletedUserOptionCount = em.createNativeQuery("delete from ems_dashboard_user_options where tenant_id = ?1")
                    .setParameter(1, internalTenantId).executeUpdate();
            int deletedDashboardSetParamsCount = em.createNativeQuery("delete from ems_dashboard_set where tenant_id = ?1")
                    .setParameter(1, internalTenantId).executeUpdate();
            int deletedDashboardCount = em.createNativeQuery("delete from ems_dashboard where tenant_id = ?1")
                    .setParameter(1, internalTenantId).executeUpdate();
            int deletedPreferenceCount = em.createNativeQuery("delete from ems_preference where tenant_id = ?1")
                    .setParameter(1, internalTenantId).executeUpdate();

            // remove all compare/sync data
            int deletedCompareCount = 0;
            @SuppressWarnings("unchecked")
            List<String> lastCompareDate = (List<String>) em
                    .createNativeQuery(
                            "SELECT LAST_COMPARISON_DATE FROM (SELECT to_char(LAST_COMPARISON_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as LAST_COMPARISON_DATE FROM EMS_ZDT_SYNC WHERE SYNC_RESULT = 'SUCCESSFUL' ORDER BY SYNC_DATE DESC) WHERE ROWNUM = 1")
                    .getResultList();
            if (lastCompareDate != null && !lastCompareDate.isEmpty()) {
                deletedCompareCount = em
                        .createNativeQuery(
                                "delete from EMS_ZDT_COMPARATOR where comparison_date > to_timestamp(?1,'yyyy-mm-dd hh24:mi:ss.ff')")
                        .setParameter(1, lastCompareDate.get(0)).executeUpdate();
            }

            entityTransaction.commit();
            LOGGER.info(
                    "End cleanTenant : {} last access, {} tile parmas, {} tiles, {} user options, {} dashboard sets, {} dashboards, "
                            + "{} preference and {} compare/sync have been deleted!", deletedLastAccessCount,
                    deletedTileParamsCount, deletedTileCount, deletedUserOptionCount, deletedDashboardSetParamsCount,
                    deletedDashboardCount, deletedPreferenceCount, deletedCompareCount);
        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }

            LOGGER.error("Fail to delete user data of tenant {} because: ", internalTenantId, e.getMessage());
            throw new CommonResourceException("Fail to delete user data of tenant " + internalTenantId);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
