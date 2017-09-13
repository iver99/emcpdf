package oracle.sysman.emaas.platform.dashboards.comparator.timer;

import org.testng.annotations.Test;

@Test(groups = { "s1" })
public class DashboardComparatorServiceManagerTest {
	
	@Test
	public void testGetName() {
		DashboardComparatorServiceManager manager = new DashboardComparatorServiceManager();
		manager.getName();
	}
	
	@Test
	public void testpostStart() throws Exception {
		DashboardComparatorServiceManager manager = new DashboardComparatorServiceManager();
		manager.postStart(null);
	}
	

}
