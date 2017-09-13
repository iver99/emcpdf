package oracle.sysman.emaas.platform.dashboards.comparator.timer;

import org.testng.annotations.Test;

@Test(groups = { "s1" })
public class DashboardComparatorHandlerNotificationTest {
	@Test
	public void testHandleNotification() {
		DashboardComparatorHandlerNotification handler = new DashboardComparatorHandlerNotification();
		handler.handleNotification(null, null);
	}

}
