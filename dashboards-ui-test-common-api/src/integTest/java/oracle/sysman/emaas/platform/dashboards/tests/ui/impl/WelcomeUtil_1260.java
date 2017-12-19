package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * Created by shangwan on 2017/12/12.
 */
public class WelcomeUtil_1260 extends WelcomeUtil_1150 {
    public final static String ITA_EXADATA_ANALYTICS = "exadataAnalytics";

    /**
     *
     * @param driver
     * @param selection
     *      default | performanceAnalyticsDatabase | performanceAnalyticsApplicationServer |
     * 		resourceAnalyticsDatabase | resourceAnalyticsMiddleware | resourceAnalyticsHost |
     *  	applicationPerformanceAnalytic | availabilityAnalytics | dataExplorer | exadataAnalytics
     */
    @Override
    public void visitITA(WebDriver driver, String selection)
    {
        driver.getLogger().info("Visiting ITA-" + selection + " from Welcome Page...");

        Validator
                .fromValidValues("ITASelection", selection, ITA_DEFAULT, ITA_PERFORMANCE_ANALYTICS_DATABASE,
                        ITA_PERFORMANCE_ANALYTICS_APPLICATION_SERVER, ITA_RESOURCE_ANALYTICS_DATABASE,
                        ITA_RESOURCE_ANALYTICS_MIDDLEWARE, ITA_RESOURCE_ANALYTICS_HOST, ITA_APPLICATION_PERFORMANCE_ANALYTICS,
                        ITA_AVAILABILITY_ANALYTICS, ITA_DATA_EXPLORER,ITA_EXADATA_ANALYTICS);

        WaitUtil.waitForPageFullyLoaded(driver);

        if (ITA_DEFAULT.equals(selection)) {
            driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_ITALINKID);
            driver.click("id=" + DashBoardPageId.WELCOME_ITALINKID);
        }
        else {
            String eleXpath = null;
            driver.waitForElementPresent("id=oj-select-choice-" + DashBoardPageId.WELCOME_ITA_SELECTID);
            driver.click("id=oj-select-choice-" + DashBoardPageId.WELCOME_ITA_SELECTID);
            switch (selection) {
                case ITA_PERFORMANCE_ANALYTICS_DATABASE:
                    eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
                            DashBoardPageId.WELCOME_ITA_PADATABASE);
                    break;
                case ITA_PERFORMANCE_ANALYTICS_APPLICATION_SERVER:
                    eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
                            DashBoardPageId.WELCOME_ITA_PAMIDDLEWARE);
                    break;
                case ITA_RESOURCE_ANALYTICS_DATABASE:
                    eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
                            DashBoardPageId.WELCOME_ITA_RADATABASE);
                    break;
                case ITA_RESOURCE_ANALYTICS_MIDDLEWARE:
                    eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
                            DashBoardPageId.WELCOME_ITA_RAMIDDLEWARE);
                    break;
                case ITA_RESOURCE_ANALYTICS_HOST:
                    eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID, DashBoardPageId.WELCOME_ITA_RA_HOST);
                    break;
                case ITA_APPLICATION_PERFORMANCE_ANALYTICS:
                    eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID, DashBoardPageId.WELCOME_ITA_PANALYTIC);
                    break;
                case ITA_AVAILABILITY_ANALYTICS:
                    eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
                            DashBoardPageId.WELCOME_ITA_AVANALYTIC);
                    break;
                case ITA_DATA_EXPLORER:
                    eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID, DashBoardPageId.WELCOME_ITA_DE);
                    break;
                case ITA_EXADATA_ANALYTICS:
                    eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID, DashBoardPageId.WELCOME_ITA_EXADATAANALYTIC);
                    break;
                default:
                    break;
            }
            driver.click(eleXpath);
        }
    }
}
