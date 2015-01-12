package oracle.sysman.emaas.platform.dashboards.core;

import java.util.ArrayList;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Performance test cases created for dashbaord manager
 * 
 * @author guobaochen
 */
public class DashboardManagerPerfTest
{
	private static final long NUM_DASHBOARDS_FOR_PERF_TEST = 10000L;

	private final String tenantId = "tenant1";
	private Long dashboard1stId;
	private final DashboardManager dm = DashboardManager.getInstance();
	private final List<Dashboard> dashboards = new ArrayList<Dashboard>();

	static {
		PersistenceManager.setTestEnv(true);
	}

	@AfterClass
	public void afterClass()
	{
		dm.deleteDashboard(dashboard1stId, true, tenantId);
	}

	@BeforeClass
	public void beforeClass() throws DashboardException
	{
		Dashboard dbd1 = new Dashboard();
		String name = "1st dashboard in beforeClass() for perf test " + System.currentTimeMillis();
		dbd1.setName(name);
		dbd1 = dm.saveNewDashboard(dbd1, tenantId);
		dashboard1stId = dbd1.getDashboardId();
	}

	@Test
	public void test1stCreateDashboard() throws DashboardException
	{
		try {
			long start = System.currentTimeMillis();
			for (int i = 0; i < NUM_DASHBOARDS_FOR_PERF_TEST; i++) {
				Dashboard dbd = new Dashboard();
				String name = "dashboards created for perf test " + System.currentTimeMillis();
				dbd.setName(name);
				dbd = dm.saveNewDashboard(dbd, tenantId);
				dashboards.add(dbd);
			}
			System.out.println("Time to create " + NUM_DASHBOARDS_FOR_PERF_TEST + " is " + (System.currentTimeMillis() - start)
					+ "ms");
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void test2ndQueryDashboardById() throws DashboardException
	{
		try {
			long start = System.currentTimeMillis();
			Dashboard dbd = null;
			for (int i = 0; i < dashboards.size(); i++) {
				dbd = dm.getDashboardById(dashboards.get(i).getDashboardId(), tenantId);
			}
			Assert.assertNotNull(dbd);
			System.out.println("Time to query " + NUM_DASHBOARDS_FOR_PERF_TEST + " is " + (System.currentTimeMillis() - start)
					+ "ms");
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void test3rdQueryDashboardByIdMultiThread()
	{
		try {
			DashboardManagerTestMockup.executeRepeatedly(100, 10, new Runnable() {
				@Override
				public void run()
				{
					dm.getDashboardById(dashboards.get(dashboards.size() - 1).getDashboardId(), tenantId);
				}
			}, null, null);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void test4thQueryDashboardByString() throws DashboardException
	{
		try {
			long start = System.currentTimeMillis();
			Dashboard dbd = dm.getDashboardByName(dashboards.get(dashboards.size() - 1).getName(), tenantId);
			System.out.println("Time to query dashboards from " + NUM_DASHBOARDS_FOR_PERF_TEST + " dashboards is "
					+ (System.currentTimeMillis() - start) + "ms");
			Assert.assertNotNull(dbd);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void test5thSimgleQueryPerformance() throws DashboardException
	{
		Long dashboardId = 999L;
		String dashboardName = "dashboard updated";
		long start = System.currentTimeMillis();
		dm.getDashboardById(dashboardId, tenantId);
		long end = System.currentTimeMillis();
		System.out.println("Time spent to query one dashboard by ID from " + NUM_DASHBOARDS_FOR_PERF_TEST + " dashboards: "
				+ (end - start) + "ms");

		start = System.currentTimeMillis();
		dm.getDashboardByName(dashboardName, tenantId);
		end = System.currentTimeMillis();
		System.out.println("Time spent to query one dashboard by name from " + NUM_DASHBOARDS_FOR_PERF_TEST + " dashboards: "
				+ (end - start) + "ms");
	}

	@Test
	public void test6thUpdateDashboard() throws DashboardException, InterruptedException
	{
		try {
			for (int i = 0; i < dashboards.size(); i++) {
				Dashboard dbd = dashboards.get(i);
				Thread.sleep(1);
				dbd.setName("dashboard updated for perf test " + System.currentTimeMillis());
			}
			long start = System.currentTimeMillis();
			Dashboard dbd = null;
			for (int i = 0; i < dashboards.size(); i++) {
				dbd = dm.updateDashboard(dashboards.get(i), tenantId);
			}
			System.out.println("Time to update " + NUM_DASHBOARDS_FOR_PERF_TEST + " is " + (System.currentTimeMillis() - start)
					+ "ms");
			Assert.assertNotNull(dbd);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void test7thDeletePerformance() throws DashboardException
	{
		try {
			long start = System.currentTimeMillis();
			int size = dashboards.size();
			for (int i = 0; i < size; i++) {
				dm.deleteDashboard(dashboards.get(i).getDashboardId(), tenantId);
			}
			System.out.println("Time to delete " + size + " dashboards is " + (System.currentTimeMillis() - start) + "ms");
			System.out.println("Average time to delete one dashboard is " + (System.currentTimeMillis() - start)
					/ Double.valueOf(size) + "ms");

			// hard deletion
			start = System.currentTimeMillis();
			for (int i = 0; i < size; i++) {
				dm.deleteDashboard(dashboards.get(i).getDashboardId(), true, tenantId);
			}
			System.out.println("Time to delete (hard deletion) " + size + " dashboards is "
					+ (System.currentTimeMillis() - start) + "ms");

		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
}
