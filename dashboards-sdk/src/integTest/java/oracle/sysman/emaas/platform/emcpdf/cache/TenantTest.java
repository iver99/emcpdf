package oracle.sysman.emaas.platform.emcpdf.cache;

import oracle.sysman.emaas.platform.emcpdf.cache.tool.Tenant;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by chehao on 2016/12/14.
 */
@Test(groups = {"s1"})
public class TenantTest {
    private Tenant tenant;

    @BeforeClass
    public void setUp(){
        tenant = new Tenant(1L);
    }
    @Test
    public void testEqualsTrue() throws Exception {
        tenant.setTenantName("tenantName");
        Tenant tenantIns = new Tenant(1L, "tenantName");
        tenant.equals(tenantIns);
    }

    @Test
    public void testEqualsFalse() throws Exception {
        tenant.setTenantName("tenantName1");
        Tenant tenantIns = new Tenant(2L, "tenantName");
        tenant.equals(tenantIns);
    }

    @Test
    public void testGetTenantId() throws Exception {
        Assert.assertNotNull(tenant.getTenantId());
    }

    @Test
    public void testGetTenantName() throws Exception {
        tenant.setTenantName("tenantName");
        Assert.assertNotNull(tenant.getTenantName());
    }

    @Test
    public void testSetTenantId() throws Exception {
        tenant.setTenantId(1L);
    }



    @Test
    public void testToString() throws Exception {
        Assert.assertNotNull(tenant.toString());
    }

}
