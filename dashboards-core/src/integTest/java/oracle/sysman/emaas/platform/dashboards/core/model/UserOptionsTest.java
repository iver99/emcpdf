package oracle.sysman.emaas.platform.dashboards.core.model;

import oracle.sysman.emaas.platform.dashboards.entity.EmsUserOptions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author jishshi
 * @since 2016/3/23.
 */
@Test(groups = {"s1"})
public class UserOptionsTest {
    UserOptions userOptions;

    @BeforeMethod
    public void setUp() throws Exception {
        userOptions = new UserOptions();

    }

    @Test
    public void testGetUserName() throws Exception {
        userOptions.setUserName("name");
        Assert.assertEquals(userOptions.getUserName(),"name");
    }

    @Test
    public void testGetDashboardId() throws Exception {
        Long id= 1L;
        userOptions.setDashboardId(id);
        Assert.assertEquals(userOptions.getDashboardId(),id);
    }

    @Test
    public void testGetAutoRefreshInterval() throws Exception {
        Long autoRefreshInterval = 30000L;
        userOptions.setAutoRefreshInterval(autoRefreshInterval);
        Assert.assertEquals(userOptions.getAutoRefreshInterval(),autoRefreshInterval);
    }

    @Test
    public void testValueOf() throws Exception {
        Assert.assertNull(UserOptions.valueOf(null));

        Long id = 1L;
        EmsUserOptions emsUserOptions = new EmsUserOptions();
        emsUserOptions.setUserName("name");
        emsUserOptions.setDashboardId(1L);
        emsUserOptions.setAutoRefreshInterval(30000L);

        userOptions = UserOptions.valueOf(emsUserOptions);
        Assert.assertEquals(userOptions.getUserName(),"name");
        Assert.assertEquals(userOptions.getDashboardId(),id);
        Assert.assertEquals(userOptions.getAutoRefreshInterval(),new Long(30000L));

    }

    @Test
    public void testToEntity() throws Exception {
        userOptions.setUserName("name");
        userOptions.setDashboardId(1L);
        userOptions.setAutoRefreshInterval(30000L);

        EmsUserOptions emsUserOptions = userOptions.toEntity(null,"new name");
        Assert.assertEquals(emsUserOptions.getUserName(),"new name");
        Assert.assertEquals(emsUserOptions.getDashboardId(),new Long(1L));
        Assert.assertEquals(emsUserOptions.getAutoRefreshInterval(),new Long(30000L));

    }


    @Test
    public void testToEntity1() throws Exception {
        try {
            userOptions.setDashboardId(null);
            userOptions.toEntity(null, "new name");
        }catch (Exception ignored){}

        try{
            userOptions.setDashboardId(1L);
            userOptions.setAutoRefreshInterval(null);
            userOptions.toEntity(null,"new name");
        }catch (Exception ignored){}
    }

}