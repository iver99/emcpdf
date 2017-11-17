package oracle.sysman.emaas.platform.dashboards.core;

import mockit.Deencapsulation;
import org.testng.annotations.Test;

@Test(groups ={"s2"})
public class DashboardConstantsTest {
    public void testDashboardConstants(){
        Deencapsulation.newInstance("oracle.sysman.emaas.platform.dashboards.core.DashboardConstants");
    }
}
