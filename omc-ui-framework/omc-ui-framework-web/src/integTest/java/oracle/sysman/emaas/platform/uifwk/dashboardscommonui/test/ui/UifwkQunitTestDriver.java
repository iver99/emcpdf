package oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui;


import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.logging.EMTestLogger;
import org.testng.annotations.Test;

import static oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils.initWebDriver;


/**
 * Created by xiadai on 2017/8/10.
 */
public class UifwkQunitTestDriver {
    @Test
    public void openQUnitTestPage( ) {
        EMTestLogger.getLogger(UifwkQunitTestDriver.class.getName());
        String qunitTestUrl = System.getProperty("uifwkQunitTestUrl");
        WebDriver webdriver = initWebDriver(UifwkQunitTestDriver.class.getName());
        webdriver.openQunitTests(qunitTestUrl);
    }
}
