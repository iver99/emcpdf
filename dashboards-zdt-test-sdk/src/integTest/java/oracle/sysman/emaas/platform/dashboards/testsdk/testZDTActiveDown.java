package oracle.sysman.emaas.platform.dashboards.testsdk;

import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.testsdk.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.testsdk.util.ZDTUtil;
import oracle.sysman.emaas.platform.dashboards.testsdk.util.PageId;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by shangwan on 2017/8/21.
 */
public class testZDTActiveDown extends LoginAndLogout
{
    public void initTest(String testName)
    {
        login(this.getClass().getName() + "." + testName);
        ZDTUtil.loadWebDriver(webd);
    }

    @Test(alwaysRun = true)
    public void testBuilderPage_DashboardSet()
    {
        //Initialize the test
        initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
        webd.getLogger().info("Start the test case: testBuilderPage_DashboardSet");

        //reset the home page
        webd.getLogger().info("Reset all filter options in the home page");
        DashboardHomeUtil.resetFilterOptions(webd);

        //switch to the grid view
        webd.getLogger().info("Swtich to the grid view");
        DashboardHomeUtil.gridView(webd);

        //open an existing dashboard
        webd.getLogger().info("Open dashboard set");
        DashboardHomeUtil.selectDashboard(webd, "Exadata Health");

        //verify that the warn message displayed
        webd.getLogger().info("Verify the warn message displayed");
        Assert.assertTrue(webd.isDisplayed("css=" + PageId.WARNMSG_CSS), "The warn message doesn't show up");
        Assert.assertEquals(webd.getText("css=" + PageId.WARNMSG_CSS).trim(),
                "Oracle Management Cloud is under maintenance. Create and edit operations are unavailable.");

        webd.getLogger().info("Click the option icon of Dashboard Set");
        webd.waitForElementPresent("css=" + PageId.DASHBOARDSETOPTIONS_CSS);
        webd.click("css=" + PageId.DASHBOARDSETOPTIONS_CSS);

        //verify the edit button is disabled
        webd.getLogger().info("Verify Edit icon is disabled");
        Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.DASHBOARDSETOPTIONSEDITCSS), "Edit menu is displayed");

        webd.getLogger().info("Verify Auto-refresh is disabled");
        Assert.assertFalse(webd.isDisplayed(DashBoardPageId.DASHBOARDSETOPTIONSAUTOREFRESHLOCATOR),"Auto-refresh menu is displayed");

        webd.getLogger().info("Verify Print icon is enabled");
        Assert.assertTrue(webd.isDisplayed("css=" + DashBoardPageId.DASHBOARDSETOPTIONSPRINTCSS),"Print menu is not displayed");

        webd.getLogger().info("Verify Add Favorite is disabled");
        Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.DASHBOARDSETOPTIONSFAVORITECSS),"Add Favorite menu is displayed");

        webd.getLogger().info("Verify Set as Home is disabled");
        Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.DASHBOARDSETOPTIONSHOMECSS),"Set as Home menu is displayed");
    }

    //@Test
    public void testBuilderPage_SingleDashboard()
    {
        //Initialize the test
        initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
        webd.getLogger().info("Start the test case: testBuilderPage_SingleDashboard");

        //reset the home page
        webd.getLogger().info("Reset all filter options in the home page");
        DashboardHomeUtil.resetFilterOptions(webd);

        //switch to the grid view
        webd.getLogger().info("Swtich to the grid view");
        DashboardHomeUtil.gridView(webd);

        //open an existing dashboard
        webd.getLogger().info("Open the dashboard");
        DashboardHomeUtil.selectDashboard(webd, "Host Operations");

        //verify that the warn message displayed
        webd.getLogger().info("Verify the warn message displayed");
        Assert.assertTrue(webd.isDisplayed("css=" + PageId.WARNMSG_CSS), "The warn message doesn't show up");
        Assert.assertEquals(webd.getText("css=" + PageId.WARNMSG_CSS).trim(),
                "Oracle Management Cloud is under maintenance. Create and edit operations are unavailable.");

        //verify the edit button is disabled
        webd.getLogger().info("Click the option icon of Dashboard Set");
        webd.waitForElementPresent("css=" + PageId.DASHBOARDOPTIONS_CSS);
        webd.click("css=" + PageId.DASHBOARDOPTIONS_CSS);

        webd.getLogger().info("Verify Edit icon is disabled");
        Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.BUILDEROPTIONSEDITLOCATORCSS),"Edit is enabled");

        webd.getLogger().info("Verify Auto-refresh is disabled");
        Assert.assertFalse(webd.isDisplayed(DashBoardPageId.BUILDEROPTIONSAUTOREFRESHLOCATOR),"Auto-refresh is enabled");

        webd.getLogger().info("Verify Print icon is enabled");
        Assert.assertTrue(webd.isDisplayed("css=" + DashBoardPageId.BUILDEROPTIONSPRINTLOCATORCSS),"Print is disabled");

        webd.getLogger().info("Verify Duplicate is disabled");
        Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.BUILDEROPTIONSDUPLICATELOCATORCSS),"Duplicate is enabled");

        webd.getLogger().info("Verify Add Favorite is disabled");
        Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.BUILDEROPTIONSFAVORITELOCATORCSS),"Add Favorite is enabled");

        webd.getLogger().info("Verify Set as Home is disabled");
        Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.BUILDEROPTIONSSETHOMELOCATORCSS),"Set as Home is enabled");

        //verify the right panel is disabled
        webd.getLogger().info("Verify the right panel is disabled");
        Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.RIGHTDRAWERTOGGLEWRENCHBTNCSS),"Right Panel is enabled");
        Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.RIGHTDRAWERTOGGLEPENCILBTNCSS),"Right Panel is enabled");
        Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.RIGHTDRAWERPANELCSS),"Right Panel is enabled");
    }

    @Test(alwaysRun = true)
    public void testHomePage()
    {
        //Initialize the test
        initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
        webd.getLogger().info("Start the test case: testHomePage");

        //verify that the warn message displayed
        webd.getLogger().info("Verify the warn message displayed");
        Assert.assertTrue(webd.isDisplayed("css=" + PageId.WARNMSG_CSS), "The warn message doesn't show up");
        Assert.assertEquals(webd.getText("css=" + PageId.WARNMSG_CSS).trim(),
                "Oracle Management Cloud is under maintenance. Create and edit operations are unavailable.");

        //verify that the create dashboard button is disabled
        webd.getLogger().info("Verify the create dashboard button is disabled");
        Assert.assertFalse(webd.getElement(DashBoardPageId.CREATEDSBUTTONID).isEnabled(),
                "The 'Create Dashboard' button is enabled");

        //verify the OOB dashboard in home page
        webd.getLogger().info("Verify all the OOB dashboard in the home page");
        ZDTUtil.apmOobExist(webd);
        ZDTUtil.itaOobExist(webd);
        ZDTUtil.laOobExist(webd);
        ZDTUtil.orchestrationOobExist(webd);
        ZDTUtil.securityOobExist(webd);

        //verify the search works
        webd.getLogger().info("Verify that the search feature works");
        DashboardHomeUtil.search(webd, "Application Performance Monitoring");
        webd.click(DashBoardPageId.INFOBTNID);
        if (webd.getElement("css=" + DashBoardPageId.RMBTNID).getAttribute("disabled").equals("true")) {
            Assert.assertTrue(true);
        } else {
            Assert.assertFalse(true, "delete is enabled for user created dashboard");
        }
    }
}