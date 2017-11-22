package oracle.sysman.emaas.platform.dashboards.entity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups={"s1"})
public class EmsDashboardTest {
     Date now = new Date();
    private EmsDashboard emsDashboard = new EmsDashboard(now,BigInteger.valueOf(10L), BigInteger.valueOf(10L),"elephant",10,10,10,10,10,10, now,
             "elephant","elephant","elephant","elephant",10,10,1,"{}", 0);

    private EmsDashboardTile emsDashboardTile = new EmsDashboardTile(
            now,
            emsDashboard,
            10,
            10,
            10,
            10,
            10,
            now,
            "elephant",
            "elephant", /*Integer position, */
            "elephant",
            "elephant",
            "dolphine",
            "10",
            "dolphine",
            "dolphine",
            "dolphine",
            "kitten",
            "kitten",
            "kitten",
            "kitten",
            "kitten",
            "lion",
            10,
            "lion",
            "lion",
            "lion",
            10,
            10,
            BigInteger.valueOf(10L),
            0,
            new Date(),
			0);
    @Test
    public void testAddEmsDashboardTile() {
        emsDashboard = new EmsDashboard();
        EmsDashboardTile tile = new EmsDashboardTile();
        emsDashboard.addEmsDashboardTile(tile);
        Assert.assertEquals(emsDashboard.getDashboardTileList().get(0),tile);
    }

    @Test
    public void testGetApplicationType() {
        emsDashboard.setApplicationType(10);
        Assert.assertEquals(emsDashboard.getApplicationType(),new Integer(10));

    }

    @Test
    public void testGetCreationDate() { 
        emsDashboard.setCreationDate(now);
        Assert.assertEquals(emsDashboard.getCreationDate(), now); 
    }

    @Test
    public void testGetDashboardId() {
        emsDashboard.setDashboardId(BigInteger.valueOf(10L));
        Assert.assertEquals(emsDashboard.getDashboardId(),BigInteger.valueOf(10L));
    }

    @Test
    public void testGetDashboardTileList() {

        List<EmsDashboardTile> list = new ArrayList<EmsDashboardTile>();
        list.add(emsDashboardTile);
        emsDashboard.setDashboardTileList(list);
        Assert.assertEquals(emsDashboard.getDashboardTileList(),list);

    }

    @Test
    public void testGetDeleted() {
        emsDashboard.setDeleted(BigInteger.valueOf(19L));
        Assert.assertEquals(emsDashboard.getDeleted(),BigInteger.valueOf(19L));

    }

    @Test
    public void testGetDescription() {
        emsDashboard.setDescription("elephant");
        Assert.assertEquals(emsDashboard.getDescription(),"elephant");
    }

    @Test
    public void testGetEnableRefresh() {
        emsDashboard.setEnableRefresh(10);
        Assert.assertEquals(emsDashboard.getEnableRefresh(),new Integer(10));
    }

    @Test
    public void testGetEnableDescription() {
        emsDashboard.setEnableDescription(10);
        Assert.assertEquals(emsDashboard.getEnableDescription(),new Integer(10));
    }
    
    @Test
    public void testGetEnableEntityFilter() {
        emsDashboard.setEnableEntityFilter(10);
        Assert.assertEquals(emsDashboard.getEnableEntityFilter(),new Integer(10));
    }
    
    @Test
    public void testGetExtendedOptions() {
    	emsDashboard.setExtendedOptions("elephant");
    	Assert.assertEquals(emsDashboard.getExtendedOptions(), "elephant");
    }
    
    @Test
    public void testGetEnableTimeRange() {
        emsDashboard.setEnableTimeRange(10);
        Assert.assertEquals(emsDashboard.getEnableTimeRange(),new Integer(10));
    }

    @Test
    public void testGetIsSystem() {
        emsDashboard.setIsSystem(10);
        Assert.assertEquals(emsDashboard.getIsSystem(),new Integer(10));

    }

    @Test
    public void testGetLastModificationDate() {  
        emsDashboard.setLastModificationDate(now);
        Assert.assertEquals(emsDashboard.getLastModificationDate(), now); 
    }

    @Test
    public void testGetLastModifiedBy() {
        emsDashboard.setLastModifiedBy("elephant");
        Assert.assertEquals(emsDashboard.getLastModifiedBy(),"elephant");
    }

    @Test
    public void testGetName() {
        emsDashboard.setName("elephant");
        Assert.assertEquals(emsDashboard.getName(),"elephant");
    }

    @Test
    public void testGetOwner() {
        emsDashboard.setOwner("elephant");
        Assert.assertEquals(emsDashboard.getOwner(),"elephant");
    }

    @Test
    public void testGetScreenShot() {
        emsDashboard.setScreenShot("elephant");
        Assert.assertEquals(emsDashboard.getScreenShot(),"elephant");

    }

    @Test
    public void testGetSharePublic() {
        emsDashboard.setSharePublic(10);
        Assert.assertEquals(emsDashboard.getSharePublic(),new Integer(10));

    }

    @Test
    public void testGetTenantId() {
        emsDashboard.setTenantId(10L);
        Assert.assertEquals(emsDashboard.getTenantId(),new Long(10));
	}

    @Test
    public void testGetType() {
        emsDashboard.setType(10);
        Assert.assertEquals(emsDashboard.getType(), new Integer(10));
    }

    @Test
    public void testRemoveEmsDashboardTile() {
        List<EmsDashboardTile> list = new ArrayList<EmsDashboardTile>();
        list.add(emsDashboardTile);
        emsDashboard.setDashboardTileList(list);
        Assert.assertEquals(emsDashboard.removeEmsDashboardTile(emsDashboardTile),emsDashboardTile);
    }
}

