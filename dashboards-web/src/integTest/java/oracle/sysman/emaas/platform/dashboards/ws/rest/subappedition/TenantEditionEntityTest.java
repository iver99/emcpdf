package oracle.sysman.emaas.platform.dashboards.ws.rest.subappedition;

import mockit.Mocked;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by jishshi on 1/18/2016.
 */
@Test(groups = {"s2"})
public class TenantEditionEntityTest {
    TenantEditionEntity tenantEditionEntity;

    @BeforeMethod
    public void setUp() throws Exception {
        tenantEditionEntity = new TenantEditionEntity();
    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetApplication() throws Exception {
        Assert.assertNull(tenantEditionEntity.getApplication());

        tenantEditionEntity.setApplication("app");
        Assert.assertEquals(tenantEditionEntity.getApplication(),"app");

        tenantEditionEntity.setApplication(null);
        Assert.assertNull(tenantEditionEntity.getApplication());
    }

    @Test
    public void testGetEdition() throws Exception {
        Assert.assertNull(tenantEditionEntity.getEdition());

        tenantEditionEntity.setEdition("edition");
        Assert.assertEquals(tenantEditionEntity.getEdition(),"edition");

        tenantEditionEntity.setEdition(null);
        Assert.assertNull(tenantEditionEntity.getEdition());
    }

    @Test
    public  void testTenantEditionEntity(@Mocked String application,@Mocked String edition){
        tenantEditionEntity = new TenantEditionEntity(application,edition);
        Assert.assertEquals(tenantEditionEntity.getApplication(),application);
        Assert.assertEquals(tenantEditionEntity.getEdition(),edition);
    }
}