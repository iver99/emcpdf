package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities;

import java.math.BigInteger;

import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts.CountsEntity;
import org.testng.Assert;
import org.testng.annotations.Test;
@Test(groups = { "s1" })
public class DashboardRowEntityTest
{
	@Test
	public void testEquals()
	{
		DashboardRowEntity dre1 = new DashboardRowEntity();
		DashboardRowEntity dre2 = new DashboardRowEntity();
		Assert.assertEquals(dre1, dre2);
		dre1.setDashboardId("1");
		dre2.setDashboardId("1");
		Assert.assertEquals(dre1, dre2);
		dre1.setName("dashboard1");
		dre2.setName("dashboard1");
		Assert.assertEquals(dre1, dre2);
		dre1.setType(1L);
		dre2.setType(1L);
		Assert.assertEquals(dre1, dre2);
		dre1.setDescription("desc");
		dre2.setDescription("desc");
		Assert.assertEquals(dre1, dre2);
		dre1.setLastModifiedBy("emcsadmin");
		dre2.setLastModifiedBy("emcsadmin");
		Assert.assertEquals(dre1, dre2);
		dre1.setOwner("emcsadmin");
		dre2.setOwner("emcsadmin");
		Assert.assertEquals(dre1, dre2);
		dre1.setIsSystem(1);
		dre2.setIsSystem(1);
		Assert.assertEquals(dre1, dre2);
		dre1.setApplicationType(1);
		dre2.setApplicationType(1);
		Assert.assertEquals(dre1, dre2);
		dre1.setEnableTimeRange(1);
		dre2.setEnableTimeRange(1);
		Assert.assertEquals(dre1, dre2);
		dre1.setScreenShot("screenshot");
		dre2.setScreenShot("screenshot");
		Assert.assertEquals(dre1, dre2);
		dre1.setDeleted("1");
		dre2.setDeleted("1");
		Assert.assertEquals(dre1, dre2);
		dre1.setTenantId(1L);
		dre2.setTenantId(1L);
		Assert.assertEquals(dre1, dre2);
		dre1.setEnableRefresh(1);
		dre2.setEnableRefresh(1);
		Assert.assertEquals(dre1, dre2);
		dre1.setSharePublic(1);
		dre2.setSharePublic(1);
		Assert.assertEquals(dre1, dre2);
		dre1.setEnableEntityFilter(1);
		dre2.setEnableEntityFilter(1);
		Assert.assertEquals(dre1, dre2);
		dre1.setEnableDescription(1);
		dre2.setEnableDescription(1);
		Assert.assertEquals(dre1, dre2);
		dre1.setExtendedOptions("options");
		dre2.setExtendedOptions("options");
		Assert.assertEquals(dre1, dre2);

		dre2.setDashboardId("2");
		Assert.assertNotEquals(dre1, dre2);
		dre2.setDashboardId("1");
		dre2.setName("dashboard2");
		Assert.assertNotEquals(dre1, dre2);
		dre2.setName("dashboard1");
		dre2.setType(0L);
		Assert.assertNotEquals(dre1, dre2);
		dre2.setType(1L);
		dre2.setDescription("desc2");
		Assert.assertNotEquals(dre1, dre2);
		dre2.setDescription("desc");
		dre2.setLastModifiedBy("emcsadmin2");
		Assert.assertNotEquals(dre1, dre2);
		dre2.setLastModifiedBy("emcsadmin");
		dre2.setOwner("emcsadmin2");
		Assert.assertNotEquals(dre1, dre2);
		dre2.setOwner("emcsadmin");
		dre2.setIsSystem(0);
		Assert.assertNotEquals(dre1, dre2);
		dre2.setIsSystem(1);
		dre2.setApplicationType(0);
		Assert.assertNotEquals(dre1, dre2);
		dre2.setApplicationType(1);
		dre2.setEnableTimeRange(0);
		Assert.assertNotEquals(dre1, dre2);
		dre2.setEnableTimeRange(1);
		dre2.setScreenShot("screenshot1");
		Assert.assertNotEquals(dre1, dre2);
		dre2.setScreenShot("screenshot");
		dre2.setDeleted("0");
		Assert.assertNotEquals(dre1, dre2);
		dre2.setDeleted("1");
		dre2.setTenantId(2L);
		Assert.assertNotEquals(dre1, dre2);
		dre2.setTenantId(1L);
		dre2.setEnableRefresh(0);
		Assert.assertNotEquals(dre1, dre2);
		dre2.setEnableRefresh(1);
		dre2.setSharePublic(0);
		Assert.assertNotEquals(dre1, dre2);
		dre2.setSharePublic(1);
		dre2.setEnableEntityFilter(0);
		Assert.assertNotEquals(dre1, dre2);
		dre2.setEnableEntityFilter(1);
		dre2.setEnableDescription(0);
		Assert.assertNotEquals(dre1, dre2);
		dre2.setEnableDescription(1);
		dre2.setExtendedOptions("options 1");
		Assert.assertNotEquals(dre1, dre2);
		dre2.setExtendedOptions("options");
		Assert.assertEquals(dre1, dre2);
	}

	@Test
	public void testHashCode()
	{
		DashboardRowEntity dre1 = new DashboardRowEntity();
		DashboardRowEntity dre2 = new DashboardRowEntity();
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());
		dre1.setDashboardId("1");
		dre2.setDashboardId("1");
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());
		dre1.setName("dashboard1");
		dre2.setName("dashboard1");
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());
		dre1.setType(1L);
		dre2.setType(1L);
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());
		dre1.setDescription("desc");
		dre2.setDescription("desc");
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());
		dre1.setLastModifiedBy("emcsadmin");
		dre2.setLastModifiedBy("emcsadmin");
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());
		dre1.setOwner("emcsadmin");
		dre2.setOwner("emcsadmin");
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());
		dre1.setIsSystem(1);
		dre2.setIsSystem(1);
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());
		dre1.setApplicationType(1);
		dre2.setApplicationType(1);
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());
		dre1.setEnableTimeRange(1);
		dre2.setEnableTimeRange(1);
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());
		dre1.setScreenShot("screenshot");
		dre2.setScreenShot("screenshot");
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());
		dre1.setDeleted("1");
		dre2.setDeleted("1");
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());
		dre1.setTenantId(1L);
		dre2.setTenantId(1L);
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());
		dre1.setEnableRefresh(1);
		dre2.setEnableRefresh(1);
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());
		dre1.setSharePublic(1);
		dre2.setSharePublic(1);
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());
		dre1.setEnableEntityFilter(1);
		dre2.setEnableEntityFilter(1);
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());
		dre1.setEnableDescription(1);
		dre2.setEnableDescription(1);
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());
		dre1.setExtendedOptions("options");
		dre2.setExtendedOptions("options");
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());

		dre2.setDashboardId("2");
		Assert.assertNotEquals(dre1.hashCode(), dre2.hashCode());
		dre2.setDashboardId("1");
		dre2.setName("dashboard2");
		Assert.assertNotEquals(dre1.hashCode(), dre2.hashCode());
		dre2.setName("dashboard1");
		dre2.setType(0L);
		Assert.assertNotEquals(dre1.hashCode(), dre2.hashCode());
		dre2.setType(1L);
		dre2.setDescription("desc2");
		Assert.assertNotEquals(dre1.hashCode(), dre2.hashCode());
		dre2.setDescription("desc");
		dre2.setLastModifiedBy("emcsadmin2");
		Assert.assertNotEquals(dre1.hashCode(), dre2.hashCode());
		dre2.setLastModifiedBy("emcsadmin");
		dre2.setOwner("emcsadmin2");
		Assert.assertNotEquals(dre1.hashCode(), dre2.hashCode());
		dre2.setOwner("emcsadmin");
		dre2.setIsSystem(0);
		Assert.assertNotEquals(dre1.hashCode(), dre2.hashCode());
		dre2.setIsSystem(1);
		dre2.setApplicationType(0);
		Assert.assertNotEquals(dre1.hashCode(), dre2.hashCode());
		dre2.setApplicationType(1);
		dre2.setEnableTimeRange(0);
		Assert.assertNotEquals(dre1.hashCode(), dre2.hashCode());
		dre2.setEnableTimeRange(1);
		dre2.setScreenShot("screenshot1");
		Assert.assertNotEquals(dre1.hashCode(), dre2.hashCode());
		dre2.setScreenShot("screenshot");
		dre2.setDeleted("0");
		Assert.assertNotEquals(dre1.hashCode(), dre2.hashCode());
		dre2.setDeleted("1");
		dre2.setTenantId(2L);
		Assert.assertNotEquals(dre1.hashCode(), dre2.hashCode());
		dre2.setTenantId(1L);
		dre2.setEnableRefresh(0);
		Assert.assertNotEquals(dre1.hashCode(), dre2.hashCode());
		dre2.setEnableRefresh(1);
		dre2.setSharePublic(0);
		Assert.assertNotEquals(dre1.hashCode(), dre2.hashCode());
		dre2.setSharePublic(1);
		dre2.setEnableEntityFilter(0);
		Assert.assertNotEquals(dre1.hashCode(), dre2.hashCode());
		dre2.setEnableEntityFilter(1);
		dre2.setEnableDescription(0);
		Assert.assertNotEquals(dre1.hashCode(), dre2.hashCode());
		dre2.setEnableDescription(1);
		dre2.setExtendedOptions("options 1");
		Assert.assertNotEquals(dre1.hashCode(), dre2.hashCode());
		dre2.setExtendedOptions("options");
		Assert.assertEquals(dre1.hashCode(), dre2.hashCode());
	}
	private CountsEntity countsEntity;
	@Test
	public void testCountsEntity(){
		countsEntity = new CountsEntity(1L,1L,1L,2L,3L,4L);
		countsEntity.setCountOfDashboards(1L);
		countsEntity.setCountOfUserOptions(1L);
		countsEntity.setCountOfPreference(1L);
		

		countsEntity.getCountOfDashboards();
		countsEntity.getCountOfUserOptions();
		countsEntity.getCountOfPreference();
	}
}
