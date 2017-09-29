/**
 *
 */
package oracle.sysman.emaas.platform.dashboards.core.persistence;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import oracle.sysman.emaas.platform.dashboards.core.BaseTest;
import oracle.sysman.emaas.platform.dashboards.core.model.FederationSupportedType;
import oracle.sysman.emaas.platform.dashboards.core.util.DateUtil;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTileParams;
import oracle.sysman.emaas.platform.dashboards.entity.EmsPreference;
import oracle.sysman.emaas.platform.dashboards.entity.EmsUserOptions;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author wenjzhu
 */
public class DashboardServiceFacadeTest_S2 extends BaseTest
{
	private static int testSeq = 1;

	//private static final String CURRENT_USER = "SYSMAN";
	private static final long TENANT_ID = 100L;

	private static void assertEmsDashboard(EmsDashboard emsdashboard)
	{
		Assert.assertNotNull(emsdashboard.getCreationDate());
		Assert.assertNotNull(emsdashboard.getDashboardId());
		Assert.assertNotNull(emsdashboard.getDeleted());
		Assert.assertNotNull(emsdashboard.getDescription());
		Assert.assertNotNull(emsdashboard.getEnableTimeRange());
		Assert.assertNotNull(emsdashboard.getIsSystem());
		//		Assert.assertNotNull(emsdashboard.getLastModificationDate());
		//		Assert.assertNotNull(emsdashboard.getLastModifiedBy());
		Assert.assertNotNull(emsdashboard.getName());
		Assert.assertNotNull(emsdashboard.getOwner());
		//		Assert.assertNotNull(emsdashboard.getScreenShot());
		//Assert.assertNotNull("tenantId = " + emsdashboard.getTenantId());
		Assert.assertNotNull(emsdashboard.getType());

		Assert.assertNotNull(emsdashboard.getDashboardTileList());
	}

	private static void assertEmsDashboardTile(EmsDashboardTile emsdashboardtile)
	{
		Assert.assertNotNull(emsdashboardtile.getCreationDate());
		Assert.assertNotNull(emsdashboardtile.getHeight());
		Assert.assertNotNull(emsdashboardtile.getIsMaximized());
		//		Assert.assertNotNull(emsdashboardtile.getLastModificationDate());
		//		Assert.assertNotNull(emsdashboardtile.getLastModifiedBy());
		Assert.assertNotNull(emsdashboardtile.getOwner());
		Assert.assertNotNull(emsdashboardtile.getPosition());
		Assert.assertNotNull(emsdashboardtile.getProviderAssetRoot());
		Assert.assertNotNull(emsdashboardtile.getProviderName());
		Assert.assertNotNull(emsdashboardtile.getProviderVersion());
		//Assert.assertNotNull("tenantId = " + emsdashboardtile.getTenantId());
		Assert.assertNotNull(emsdashboardtile.getTileId());
		Assert.assertNotNull(emsdashboardtile.getTitle());
		Assert.assertNotNull(emsdashboardtile.getWidgetCreationTime());
		Assert.assertNotNull(emsdashboardtile.getWidgetDescription());
		//		Assert.assertNotNull(emsdashboardtile.getWidgetGroupName());
		//		Assert.assertNotNull(emsdashboardtile.getWidgetHistogram());
		//		Assert.assertNotNull(emsdashboardtile.getWidgetIcon());
		Assert.assertNotNull(emsdashboardtile.getWidgetKocName());
		Assert.assertNotNull(emsdashboardtile.getWidgetName());
		Assert.assertNotNull(emsdashboardtile.getWidgetOwner());
		Assert.assertNotNull(emsdashboardtile.getWidgetSource());
		Assert.assertNotNull(emsdashboardtile.getWidgetTemplate());
		Assert.assertNotNull(emsdashboardtile.getWidgetUniqueId());
		Assert.assertNotNull(emsdashboardtile.getWidgetViewmode());
		Assert.assertNotNull(emsdashboardtile.getWidth());
		Assert.assertNotNull(emsdashboardtile.getDashboard());
		Assert.assertNotNull(emsdashboardtile.getDashboardTileParamsList());
	}

	private static void assertEmsDashboardTileParams(EmsDashboardTileParams emsdashboardtileparams)
	{
		Assert.assertNotNull(emsdashboardtileparams.getIsSystem());
		Assert.assertNotNull(emsdashboardtileparams.getParamName());
		Assert.assertNotNull(emsdashboardtileparams.getParamType());
		//it is not reasonable that a parameter has non-null value of all NUM/STR/TIME column
		//After QA test case execution and run DEV test case again, below will fail.
		//		Assert.assertNotNull(emsdashboardtileparams.getParamValueNum());
		//		Assert.assertNotNull(emsdashboardtileparams.getParamValueStr());
		//		Assert.assertNotNull(emsdashboardtileparams.getParamValueTimestamp());
		Integer num = emsdashboardtileparams.getParamValueNum();
		String str = emsdashboardtileparams.getParamValueStr();
		Date time = emsdashboardtileparams.getParamValueTimestamp();
		switch (emsdashboardtileparams.getParamType().intValue()) {
			case 1:
				Assert.assertNotNull(str);
				break;
			case 2:
				Assert.assertNotNull(num);
				break;
			case 3:
				Assert.assertNotNull(time);
				break;
			default:
				break;
		}
	}

	private static EmsDashboard newDashboard()
	{
		EmsDashboard d = new EmsDashboard();
		d.setName("test" + System.currentTimeMillis());
		d.setCreationDate(DateUtil.getCurrentUTCTime());
		d.setDeleted(BigInteger.ZERO);
		d.setDescription("test");
		d.setEnableTimeRange(1);
		d.setEnableRefresh(1);
		d.setIsSystem(1);
		d.setLastModificationDate(DateUtil.getCurrentUTCTime());
		d.setOwner("test");
		d.setLastModifiedBy("test");
		//d.setTenantId("11");
		d.setScreenShot("slslslslsl");
		d.setType(0);
		return d;
	}

	private static EmsDashboardTile newTile()
	{
		EmsDashboardTile tile = new EmsDashboardTile();
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
		//tile.setTenantId("tenantId");
		tile.setTitle("title" + System.currentTimeMillis());
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

	private static EmsDashboardTileParams newTileParams(Integer type)
	{
		EmsDashboardTileParams p = new EmsDashboardTileParams();
		p.setIsSystem(0);
		p.setParamName("paramName");
		p.setParamType(type);
		switch (type.intValue()) {
			case 1:
				p.setParamValueStr("paramValueStr");
				break;
			case 2:
				p.setParamValueNum(1);
				break;
			case 3:
				p.setParamValueTimestamp(DateUtil.getCurrentUTCTime());
				break;
			default:
				break;
		}
		return p;
	}

	private DashboardServiceFacade dashboardServiceFacade;

	private EmsDashboard d;

	private EmsDashboardTile t;

	private EmsDashboardTileParams emDashboardTileParam;

	private EmsUserOptions uo;

	/**
	 * @throws java.lang.Exception
	 */
	//@BeforeMethod
	public void setUp()
	{
		//load mock
		new MockDashboardServiceFacade();
		//		new Expectations() {
		//			{
		//				new DashboardServiceFacade(anyLong);
		//				returns(new MockDashboardServiceFacade().getMockInstance());
		//			}
		//		};

		// tenant id updated to number type
		EntityManager em = null;
		try {
			dashboardServiceFacade = new DashboardServiceFacade(TENANT_ID);
			em = dashboardServiceFacade.getEntityManager();
			d = DashboardServiceFacadeTest_S2.newDashboard();
			t = DashboardServiceFacadeTest_S2.newTile();
			emDashboardTileParam = DashboardServiceFacadeTest_S2.newTileParams(testSeq++ % 3 + 1);
			t.addEmsDashboardTileParams(emDashboardTileParam);
			d.addEmsDashboardTile(t);
			//f = DashboardServiceFacadeTest_S2.newFavorite(d);

			d = dashboardServiceFacade.persistEmsDashboard(d);
			//dashboardServiceFacade.persistEmsDashboardFavorite(f);
			dashboardServiceFacade.commitTransaction();
			//dashboardServiceFacade.getEntityManager().refresh(d);
			uo = new EmsUserOptions();
			uo.setDashboardId(d.getDashboardId());
			uo.setUserName(d.getOwner());
			uo.setIsFavorite(1);
			uo = dashboardServiceFacade.persistEmsUserOptions(uo);
			dashboardServiceFacade.commitTransaction();
			//a = DashboardServiceFacadeTest_S2.newLastAccess(d.getDashboardId());
			//dashboardServiceFacade.persistEmsDashboardLastAccess(a);
			//dashboardServiceFacade.commitTransaction();
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	//@AfterMethod
	public void tearDown()
	{
		EntityManager em = null;
		try {
			dashboardServiceFacade = new DashboardServiceFacade(TENANT_ID);
			em = dashboardServiceFacade.getEntityManager();
			if (uo != null) {
				dashboardServiceFacade.removeEmsUserOptions(uo);
			}
			if (d != null) {
				dashboardServiceFacade.removeEmsDashboard(d);
			}
			dashboardServiceFacade.commitTransaction();
			//UserContext.clearCurrentUser();
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Test(groups = { "s2" })
	public void testAll()
	{
		setUp();
		testGetEmsUserOptions();
		testGetEmsDashboardById();
		testGetEmsDashboardFindAll();
		testMergeEmsDashboard();
		//testGetEmsDashboardLastAccessByPK();
		testPrefecence();
		tearDown();
	}

	@Test(groups = { "s2" })
	public void testCC(@Mocked final PersistenceManager mockpm, @Mocked final EntityManager mockem,
			@Mocked final EntityManagerFactory mockemf, @Mocked final EntityTransaction mocket)
	{
		new NonStrictExpectations() {
			{
				PersistenceManager.getInstance();
				result = mockpm;
				mockpm.getEntityManagerFactory();
				result = mockemf;
				mockemf.createEntityManager();
				result = mockem;
				mockem.getTransaction();
				result = mocket;
				mocket.isActive();
				result = false;
				mockem.find((Class<?>) any, any);
				result = null;
				mockem.persist(any);
				result = null;
				mockem.merge(any);
				result = null;
			}
		};

		DashboardServiceFacade dsf = new DashboardServiceFacade(1L);
		dsf.commitTransaction();
		dsf.getEmsDashboardById(BigInteger.ZERO);
		dsf.getEmsDashboardFindAll();
		dsf.getEmsDashboardByName("ss");
		dsf.getEmsPreference("ss", "ss");
		dsf.getEntityManager();
		dsf.getEmsPreferenceFindAll("test");
		dsf.getFavoriteEmsDashboards("");
		dsf.mergeEmsDashboard(new EmsDashboard());
		dsf.mergeEmsPreference(new EmsPreference());
		dsf.persistEmsDashboard(new EmsDashboard());
		dsf.persistEmsPreference(new EmsPreference());
		dsf.removeAllEmsPreferences("");
		EmsDashboard rd = new EmsDashboard();
		rd.setDashboardId(BigInteger.ZERO);
		dsf.removeEmsDashboard(rd);
		EmsPreference rp = new EmsPreference();
		rp.setPrefKey("");
		rp.setUserName("");
		dsf.removeEmsPreference(rp);
	}

	/**
	 * Test method for
	 * {@link oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade#getEmsDashboardById(java.lang.Long)}
	 * .
	 */
	public void testGetEmsDashboardById()
	{
		EntityManager em = null;
		try {
			dashboardServiceFacade = new DashboardServiceFacade(TENANT_ID);
			EmsDashboard rd = dashboardServiceFacade.getEmsDashboardById(d.getDashboardId());
			DashboardServiceFacadeTest_S2.assertEmsDashboard(rd);

		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Test method for
	 * {@link oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade#getEmsDashboardFindAll()}.
	 */
	public void testGetEmsDashboardFindAll()
	{
		EntityManager em = null;
		try {
			dashboardServiceFacade = new DashboardServiceFacade(TENANT_ID);
			for (EmsDashboard emsdashboard : dashboardServiceFacade.getEmsDashboardFindAll()) {
				DashboardServiceFacadeTest_S2.assertEmsDashboard(emsdashboard);
			}
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void testGetEmsUserOptions()
	{
		EntityManager em = null;
		try {
			dashboardServiceFacade = new DashboardServiceFacade(TENANT_ID);
			EmsUserOptions rd = dashboardServiceFacade.getEmsUserOptions(uo.getUserName(), uo.getDashboardId());
			Assert.assertNotNull(rd);
			List<EmsDashboard> ds = dashboardServiceFacade.getFavoriteEmsDashboards(uo.getUserName());
			Assert.assertEquals(!ds.isEmpty(), true);
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Test method for
	 * {@link oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade#mergeEmsDashboard(oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard)}
	 * .
	 */
	public void testMergeEmsDashboard()
	{
		EntityManager em = null;
		try {
			dashboardServiceFacade = new DashboardServiceFacade(TENANT_ID);
			d = dashboardServiceFacade.getEmsDashboardById(d.getDashboardId());
			d.setLastModificationDate(DateUtil.getCurrentUTCTime());
			d.setDeleted(BigInteger.valueOf(1L));
			dashboardServiceFacade.mergeEmsDashboard(d);
			//List<EmsDashboard> ds = dashboardServiceFacade.getEmsDashboardFindAll();
			Assert.assertNull(dashboardServiceFacade.getEmsDashboardByName(d.getName()));
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void testPrefecence()
	{
		EntityManager em = null;
		try {
			dashboardServiceFacade = new DashboardServiceFacade(TENANT_ID);
			em = dashboardServiceFacade.getEntityManager();
			EmsPreference p = new EmsPreference("prefKey1", "prefValue", "test");
			dashboardServiceFacade.persistEmsPreference(p);
			dashboardServiceFacade.commitTransaction();

			p = dashboardServiceFacade.getEmsPreference("test", "prefKey1");
			Assert.assertNotNull(p);
			Assert.assertNotNull(dashboardServiceFacade.getEmsPreferenceFindAll("test"));

			dashboardServiceFacade.removeEmsPreference(p);
			dashboardServiceFacade.commitTransaction();
			dashboardServiceFacade.removeAllEmsPreferences("test");
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

}
