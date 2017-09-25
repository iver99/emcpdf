package oracle.sysman.emaas.platform.dashboards.comparator.webutils.util;

import org.testng.annotations.Test;

/**
 * Created by chehao on 8/28/2017 10:39.
 */
@Test(groups = {"s2"})
public class TimeUtilTest {
    @Test
    public void testGetCurrentTime() {
        TimeUtil.getCurrentUTCTime();
    }
}
