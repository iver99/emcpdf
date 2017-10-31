package oracle.sysman.emaas.platform.dashboards.test.ui;


import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.logging.EMTestLogger;
import org.testng.annotations.Test;

import static oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils.initWebDriver;


/**
 * Created by xiadai on 2017/8/10.
 */
public class DashboardQunitTestDriver {
    @Test
    public void openQUnitTestPage( ) {
        EMTestLogger.getLogger(DashboardQunitTestDriver.class.getName());
        String qunitTestUrl = System.getProperty("qunitTestUrl");
        WebDriver webdriver = initWebDriver(DashboardQunitTestDriver.class.getName());
        webdriver.openQunitTests(qunitTestUrl);
    }
}
