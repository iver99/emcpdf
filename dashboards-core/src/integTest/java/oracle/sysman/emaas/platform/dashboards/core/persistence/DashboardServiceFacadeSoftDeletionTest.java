package oracle.sysman.emaas.platform.dashboards.core.persistence;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import oracle.sysman.emaas.platform.dashboards.core.model.FederationSupportedType;
import org.testng.Assert;
import org.testng.annotations.Test;

import oracle.sysman.emaas.platform.dashboards.core.util.DateUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.IdGenerator;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTileParams;
import oracle.sysman.emaas.platform.dashboards.entity.EmsPreference;
import oracle.sysman.emaas.platform.dashboards.entity.EmsSubDashboard;

public class DashboardServiceFacadeSoftDeletionTest
{
	private static final long TENANT_ID = 12345l;

	@Test
	public void testDashboardSet()
	{
		BigInteger setId1 = IdGenerator.getDashboardId(UUID.randomUUID());
		BigInteger subId1 = IdGenerator.getDashboardId(UUID.randomUUID());
		EntityManager em = null;
		DashboardServiceFacade dsf = new DashboardServiceFacade(TENANT_ID);
		try {
			int position = 0;

			EmsDashboard d = new EmsDashboard();
			d.setDashboardId(setId1);
			d.setName("dashboard set 1 " + System.currentTimeMillis());
			d.setCreationDate(DateUtil.getCurrentUTCTime());
			d.setDescription("test");
			d.setEnableEntityFilter(0);
			d.setOwner("emcsadmin");
			d.setIsSystem(0);
			d.setDeleted(BigInteger.valueOf(0));
			d.setType(2);// dashboard setEmsDashboard d = new EmsDashboard();
			dsf.persistEmsDashboard(d);
			EmsDashboard sub1 = new EmsDashboard();
			sub1.setDashboardId(subId1);
			sub1.setName("sub dashboard 1 " + System.currentTimeMillis());
			sub1.setCreationDate(DateUtil.getCurrentUTCTime());
			sub1.setDescription("sub dashboard 1");
			sub1.setOwner("emcsadmin");
			sub1.setIsSystem(0);
			sub1.setDeleted(BigInteger.valueOf(0));
			sub1.setEnableEntityFilter(0);
			sub1.setType(1);
			sub1.setDeleted(BigInteger.valueOf(0));
			dsf.persistEmsDashboard(sub1);
			EmsSubDashboard p = new EmsSubDashboard(setId1, subId1, position);
			d.addEmsSubDashboard(p);
			dsf.mergeEmsDashboard(d);

			//			Assert.assertNotNull(p);
			//			Assert.assertNotNull(dashboardServiceFacade.getEmsPreferenceFindAll("test"));

			d.removeEmsSubDashboard(p);
			dsf.mergeEmsDashboard(d);
			String sql = "select count(*) from EMS_DASHBOARD_SET p where p.DASHBOARD_SET_ID=? and p.SUB_DASHBOARD_ID=? and p.TENANT_ID=? and p.DELETED=?";
			Integer deletedCount = sqlCheckCountsWithSql(sql, Arrays.asList(setId1, subId1, TENANT_ID, 1));
			Assert.assertEquals(deletedCount, Integer.valueOf(0));

			// after soft deletion, re-create
			p = new EmsSubDashboard(setId1, subId1, position);
			d.addEmsSubDashboard(p);
			dsf.mergeEmsDashboard(d);
			deletedCount = sqlCheckCountsWithSql(sql, Arrays.asList(setId1, subId1, TENANT_ID, 1));
			Assert.assertEquals(deletedCount, Integer.valueOf(0));
			deletedCount = sqlCheckCountsWithSql(sql, Arrays.asList(setId1, subId1, TENANT_ID, 0));
			Assert.assertEquals(deletedCount, Integer.valueOf(1));

			// cleanup
			sqlExecuteUpdate(
					"delete from EMS_DASHBOARD_SET p where p.DASHBOARD_SET_ID=? and p.SUB_DASHBOARD_ID=? and p.TENANT_ID=?",
					Arrays.asList(setId1, subId1, TENANT_ID));
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Test
	public void testDashboardTile()
	{
		BigInteger dashboardId = IdGenerator.getDashboardId(UUID.randomUUID());
		String tileId1 = IdGenerator.getDashboardId(UUID.randomUUID()).toString();
		String tileId2 = IdGenerator.getDashboardId(UUID.randomUUID()).toString();
		EntityManager em = null;
		DashboardServiceFacade dsf = new DashboardServiceFacade(TENANT_ID);
		try {
			EmsDashboard d = new EmsDashboard();
			d.setDashboardId(dashboardId);
			d.setName("dashboard set 1 " + System.currentTimeMillis());
			d.setCreationDate(DateUtil.getCurrentUTCTime());
			d.setDescription("test");
			d.setEnableEntityFilter(0);
			d.setOwner("emcsadmin");
			d.setIsSystem(0);
			d.setEnableTimeRange(0);
			d.setEnableRefresh(0);
			d.setSharePublic(0);
			d.setEnableDescription(0);
			d.setDeleted(BigInteger.valueOf(0));
			d.setType(1);
			EmsDashboardTile tile1 = createTestTile(tileId1);
			d.addEmsDashboardTile(tile1);
			dsf.persistEmsDashboard(d);

			d.getDashboardTileList().remove(tile1);
			dsf.mergeEmsDashboard(d);
			String sql = "select count(*) from EMS_DASHBOARD_TILE p where p.TILE_ID=? and p.TENANT_ID=? and p.DELETED=?";
			Integer deletedCount = sqlCheckCountsWithSql(sql, Arrays.asList(tile1.getTileId(), TENANT_ID, 1));
			Assert.assertEquals(deletedCount, Integer.valueOf(1));

			// after soft deletion, create a new one
			EmsDashboardTile tile2 = createTestTile(tileId2);
			d.addEmsDashboardTile(tile2);
			dsf.mergeEmsDashboard(d);
			deletedCount = sqlCheckCountsWithSql(sql, Arrays.asList(tileId1, TENANT_ID, 1));
			Assert.assertEquals(deletedCount, Integer.valueOf(1));
			deletedCount = sqlCheckCountsWithSql(sql, Arrays.asList(tileId2, TENANT_ID, 0));
			Assert.assertEquals(deletedCount, Integer.valueOf(1));

			// cleanup
			sqlExecuteUpdate("delete from EMS_DASHBOARD_TILE p where p.TILE_ID=? and p.TENANT_ID=?",
					Arrays.asList(tile1.getTileId(), TENANT_ID));
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Test
	public void testDashboardTileParams()
	{
		BigInteger dashboardId = IdGenerator.getDashboardId(UUID.randomUUID());
		String tileId = IdGenerator.getDashboardId(UUID.randomUUID()).toString();
		String paramName = "paramName";
		String paramValue = "paramName";
		EntityManager em = null;
		DashboardServiceFacade dsf = new DashboardServiceFacade(TENANT_ID);
		try {
			EmsDashboard d = new EmsDashboard();
			d.setDashboardId(dashboardId);
			d.setName("dashboard set 1 " + System.currentTimeMillis());
			d.setCreationDate(DateUtil.getCurrentUTCTime());
			d.setDescription("test");
			d.setOwner("emcsadmin");
			d.setIsSystem(0);
			d.setDeleted(BigInteger.valueOf(0));
			d.setType(1);
			EmsDashboardTile tile = createTestTile(tileId);
			d.addEmsDashboardTile(tile);
			EmsDashboardTileParams p = new EmsDashboardTileParams();
			p.setIsSystem(0);
			p.setParamName(paramName);
			p.setParamType(1);
			p.setParamValueStr(paramValue);
			tile.addEmsDashboardTileParams(p);
			dsf.persistEmsDashboard(d);

			tile.getDashboardTileParamsList().remove(p);
			dsf.mergeEmsDashboard(d);
			dsf.getEntityManager().detach(p);
			String sql = "select count(*) from EMS_DASHBOARD_TILE_PARAMS p where p.TILE_ID=? and p.PARAM_NAME=? and p.TENANT_ID=? and p.DELETED=?";
			Integer deletedCount = sqlCheckCountsWithSql(sql, Arrays.asList(tile.getTileId(), paramName, TENANT_ID, 1));
			Assert.assertEquals(deletedCount, Integer.valueOf(1));

			// after soft deletion, re-create
			p = new EmsDashboardTileParams();
			p.setIsSystem(1);
			p.setParamName(paramName);
			p.setParamType(1);
			p.setParamValueStr(paramValue);
			tile.addEmsDashboardTileParams(p);
			dsf.mergeEmsDashboard(d);
			deletedCount = sqlCheckCountsWithSql(sql, Arrays.asList(tile.getTileId(), paramName, TENANT_ID, 1));
			Assert.assertEquals(deletedCount, Integer.valueOf(0));
			deletedCount = sqlCheckCountsWithSql(sql, Arrays.asList(tile.getTileId(), paramName, TENANT_ID, 0));
			Assert.assertEquals(deletedCount, Integer.valueOf(1));

			// cleanup
			sqlExecuteUpdate("delete from EMS_DASHBOARD_TILE_PARAMS p where p.TILE_ID=? and p.PARAM_NAME=? and p.TENANT_ID=?",
					Arrays.asList(tile.getTileId(), paramName, TENANT_ID));
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Test
	public void testPrefecence()
	{
		EntityManager em = null;
		try {
			String key = "prefKey1 " + System.currentTimeMillis();
			String value = "prefValue";
			String user = "test user";
			DashboardServiceFacade dashboardServiceFacade = new DashboardServiceFacade(TENANT_ID);
			em = dashboardServiceFacade.getEntityManager();
			EmsPreference p = new EmsPreference(key, value, user);
			dashboardServiceFacade.persistEmsPreference(p);

			p = dashboardServiceFacade.getEmsPreference(user, key);
			Assert.assertNotNull(p);
			Assert.assertNotNull(dashboardServiceFacade.getEmsPreferenceFindAll("test"));

			dashboardServiceFacade.removeEmsPreference(p);
			String sql = "select count(*) from EMS_PREFERENCE p where p.USER_NAME=? and p.PREF_KEY=? and p.TENANT_ID=? and p.DELETED=?";
			Integer deletedCount = sqlCheckCountsWithSql(sql, Arrays.asList(user, key, TENANT_ID, 1));
			Assert.assertEquals(deletedCount, Integer.valueOf(1));

			// after soft deletion, re-create
			p = new EmsPreference(key, value, user);
			dashboardServiceFacade.persistEmsPreference(p);
			deletedCount = sqlCheckCountsWithSql(sql, Arrays.asList(user, key, TENANT_ID, 1));
			Assert.assertEquals(deletedCount, Integer.valueOf(0));
			deletedCount = sqlCheckCountsWithSql(sql, Arrays.asList(user, key, TENANT_ID, 0));
			Assert.assertEquals(deletedCount, Integer.valueOf(1));
			p.setPrefValue("new value");
			dashboardServiceFacade.mergeEmsPreference(p);
			deletedCount = sqlCheckCountsWithSql(sql, Arrays.asList(user, key, TENANT_ID, 1));
			Assert.assertEquals(deletedCount, Integer.valueOf(0));
			deletedCount = sqlCheckCountsWithSql(sql, Arrays.asList(user, key, TENANT_ID, 0));
			Assert.assertEquals(deletedCount, Integer.valueOf(1));

			// cleanup
			sqlExecuteUpdate("delete from EMS_PREFERENCE p where p.USER_NAME=? and p.PREF_KEY=? and p.TENANT_ID=?",
					Arrays.asList(user, key, TENANT_ID));
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * @param tileId
	 * @return
	 */
	private EmsDashboardTile createTestTile(String tileId)
	{
		EmsDashboardTile tile = new EmsDashboardTile();
		tile.setTileId(tileId);
		tile.setTitle("test title");
		tile.setCreationDate(DateUtil.getCurrentUTCTime());
		tile.setHeight(1);
		tile.setWidth(23);
		tile.setIsMaximized(1);
		tile.setLastModificationDate(DateUtil.getCurrentUTCTime());
		tile.setLastModifiedBy("test");
		tile.setOwner("test");
		tile.setPosition(1);
		tile.setProviderAssetRoot("ssss");
		tile.setProviderName("sss");
		tile.setProviderVersion("providerVersion");
		tile.setWidgetCreationTime("widgetCreationTime");
		tile.setWidgetDescription("widgetDescription");
		tile.setWidgetGroupName("widgetGroupName");
		tile.setWidgetIcon("widgetIcon");
		tile.setWidgetKocName("widgetKocName");
		tile.setWidgetName("widgetName");
		tile.setWidgetOwner("widgetOwner");
		tile.setWidgetSource(1);
		tile.setWidgetTemplate("widgetTemplate");
		tile.setWidgetUniqueId("widgetUniqueId");
		tile.setWidgetViewmode("widgetViewmode");
		tile.setWidgetHistogram("widgetHistogram");
		tile.setWidgetSupportTimeControl(1);
		tile.setWidgetLinkedDashboard(BigInteger.valueOf(1L));
		tile.setFederationSupported(FederationSupportedType.NON_FEDERATION_ONLY.getValue());
		return tile;
	}

	private void initializeQueryParams(Query query, List<? extends Object> paramList)
	{
		if (query == null || paramList == null) {
			return;
		}
		for (int i = 0; i < paramList.size(); i++) {
			Object value = paramList.get(i);
			query.setParameter(i + 1, value);
		}
	}

	private Integer sqlCheckCountsWithSql(String sql, List<? extends Object> params)
	{
		EntityManager em = null;
		try {
			DashboardServiceFacade dashboardServiceFacade = new DashboardServiceFacade(TENANT_ID);
			em = dashboardServiceFacade.getEntityManager();
			Query query = em.createNativeQuery(sql);
			initializeQueryParams(query, params);
			Object result = null;
			List results = query.getResultList();
			if(results != null && results.size() >0){
				result = results.get(0);
			}
			final EntityTransaction entityTransaction = em.getTransaction();
			if (!entityTransaction.isActive()) {
				entityTransaction.begin();
			}
			em.flush();
			if (result instanceof BigDecimal) {
				return ((BigDecimal) result).intValue();
			}
			else if (result instanceof Integer) {
				return ((Integer) result).intValue();
			}
			return null;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	private void sqlExecuteUpdate(String sql, List<? extends Object> params)
	{
		EntityManager em = null;
		try {
			DashboardServiceFacade dashboardServiceFacade = new DashboardServiceFacade(TENANT_ID);
			em = dashboardServiceFacade.getEntityManager();
			Query query = em.createNativeQuery(sql);
			initializeQueryParams(query, params);
			final EntityTransaction entityTransaction = em.getTransaction();
			if (!entityTransaction.isActive()) {
				entityTransaction.begin();
			}
			query.executeUpdate();
			entityTransaction.commit();
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}
}
