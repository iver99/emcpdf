package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

import org.testng.annotations.Test;

@Test(groups={"s1"})
public class DashboardDependencyStatusTest {
    @Test
    public void testDashboardDependencyStatus(){
        DashboardDependencyStatus dashboardDependencyStatus = new DashboardDependencyStatus();
        dashboardDependencyStatus.setEntity_naming_status(null);
        dashboardDependencyStatus.setDb_status(null);
        dashboardDependencyStatus.getDb_status();
        dashboardDependencyStatus.getEntity_naming_status();
    }
}
