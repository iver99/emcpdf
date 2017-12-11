package oracle.sysman.emaas.platform.dashboards.ws.rest;

import org.testng.annotations.Test;

@Test(groups={"s1"})
public class DashboardStatusAPITest {

    @Test
    public void testGetDBstatus(){
        new DashboardStatusAPI().getDBstatus(null, null,null);
    }
}
