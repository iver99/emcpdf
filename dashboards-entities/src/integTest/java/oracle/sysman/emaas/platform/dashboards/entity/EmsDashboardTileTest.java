package oracle.sysman.emaas.platform.dashboards.entity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;



@Test(groups={"s1"})
public class EmsDashboardTileTest { 
    Date now = new Date();
    private EmsDashboard emsDashboard = new EmsDashboard(now,BigInteger.valueOf(10L),BigInteger.valueOf(10L),"elephant",10,10,10,10,10,10, now,
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
            1,
            new Date(),
			0);

    private EmsDashboardTileParams emsDashboardTileParams = new
            EmsDashboardTileParams(
            10,
            "elephant",
            10,
            10,
            "dolphine",
            now,
            emsDashboardTile);
    @Test
    public void testAddEmsDashboardTileParams() {
    	Assert.assertEquals(emsDashboardTile.addEmsDashboardTileParams(emsDashboardTileParams),emsDashboardTileParams);
    }

    @Test
    public void testGetColumn() {
        EmsDashboardTile emsDashboardTile1 = new EmsDashboardTile();
        emsDashboardTile1.setColumn(10);
        Assert.assertEquals(emsDashboardTile1.getColumn(),new Integer(10));

    }

    @Test
    public void testGetCreationDate() { 
        emsDashboardTile.setCreationDate(now);
        Assert.assertEquals(emsDashboardTile.getCreationDate(), now); 
    }

    @Test
    public void testGetDashboard() {
        emsDashboardTile.setDashboard(emsDashboard);
        Assert.assertEquals(emsDashboardTile.getDashboard(),emsDashboard);
    }

    @Test
    public void testGetDashboardTileParamsList(){

        List<EmsDashboardTileParams> list = new ArrayList<EmsDashboardTileParams>();
        emsDashboardTile.setDashboardTileParamsList(list);
        Assert.assertEquals(emsDashboardTile.getDashboardTileParamsList(),list);

    }

    @Test
    public void testGetHeight() {
        emsDashboardTile.setHeight(10);
        Assert.assertEquals(emsDashboardTile.getHeight(),new Integer(10));
    }

    @Test
    public void testGetIsMaximized() {
        emsDashboardTile.setIsMaximized(10);
        Assert.assertEquals(emsDashboardTile.getIsMaximized(),new Integer(10));
    }

    @Test
    public void testGetLastModificationDate() {
        emsDashboardTile.setLastModificationDate(now);
        Assert.assertEquals(emsDashboardTile.getLastModificationDate(), now);
    }

    @Test
    public void testGetLastModifiedBy() {
        emsDashboardTile.setLastModifiedBy("elephant");
        Assert.assertEquals(emsDashboardTile.getLastModifiedBy(),"elephant");
    }

    @Test
    public void testGetOwner() {
        emsDashboardTile.setOwner("elephant");
        Assert.assertEquals(emsDashboardTile.getOwner(),"elephant");
    }

    @Test
    public void testGetPosition() {
        emsDashboardTile.setPosition(10);
        Assert.assertEquals(emsDashboardTile.getPosition(),new Integer(0));

    }

    @Test
    public void testGetProviderAssetRoot() {
        emsDashboardTile.setProviderAssetRoot("elephant");
        Assert.assertEquals(emsDashboardTile.getProviderAssetRoot(),"elephant");
    }

    @Test
    public void testGetProviderName() {
        emsDashboardTile.setProviderName("elephant");
        Assert.assertEquals(emsDashboardTile.getProviderName(),"elephant");
    }

    @Test
    public void testGetProviderVersion() {
        emsDashboardTile.setProviderVersion("elephant");
        Assert.assertEquals(emsDashboardTile.getProviderVersion(),"elephant");

    }

    @Test
    public void testGetRow() {
        emsDashboardTile.setRow(10);
        Assert.assertEquals(emsDashboardTile.getRow(),new Integer(10));
    }

    @Test
    public void testGetTileId() {
    	Assert.assertEquals(emsDashboardTile.getTileId(), "10");

    }

    @Test
    public void testGetTitle() {
        emsDashboardTile.setTitle("elephant");
        Assert.assertEquals(emsDashboardTile.getTitle(),"elephant");

    }

    @Test
    public void testGetType() {
        emsDashboardTile.setType(10);
        Assert.assertEquals(emsDashboardTile.getType(),new Integer(10));

    }

    @Test
    public void testGetWidgetCreationTime() {
        emsDashboardTile.setWidgetCreationTime("elephant");
        Assert.assertEquals(emsDashboardTile.getWidgetCreationTime(),"elephant");
    }

    @Test
    public void testGetWidgetDescription() {
        emsDashboardTile.setWidgetDescription("elephant");
        Assert.assertEquals(emsDashboardTile.getWidgetDescription(),"elephant");

    }

    @Test
    public void testGetWidgetGroupName() {
        emsDashboardTile.setWidgetGroupName("elephant");
        Assert.assertEquals(emsDashboardTile.getWidgetGroupName(),"elephant");

    }

    @Test
    public void testGetWidgetHistogram() {
        emsDashboardTile.setWidgetHistogram("elephant");
        Assert.assertEquals(emsDashboardTile.getWidgetHistogram(),"elephant");
    }

    @Test
    public void testGetWidgetIcon() {
        emsDashboardTile.setWidgetIcon("elephant");
        Assert.assertEquals(emsDashboardTile.getWidgetIcon(),"elephant");
    }

    @Test
    public void testGetWidgetKocName() {
        emsDashboardTile.setWidgetKocName("elephant");
        Assert.assertEquals(emsDashboardTile.getWidgetKocName(),"elephant");
    }

    @Test
    public void testGetWidgetLinkedDashboard() {
        emsDashboardTile.setWidgetLinkedDashboard(BigInteger.valueOf(10L));
        Assert.assertEquals(emsDashboardTile.getWidgetLinkedDashboard(),BigInteger.valueOf(10L));

    }

    @Test
    public void testGetWidgetName() {
        emsDashboardTile.setWidgetName("elephant");
        Assert.assertEquals(emsDashboardTile.getWidgetName(),"elephant");

    }

    @Test
    public void testGetWidgetOwner() {
        emsDashboardTile.setWidgetOwner("elephant");
        Assert.assertEquals(emsDashboardTile.getWidgetOwner(),"elephant");
    }

    @Test
    public void testGetWidgetSource() {
        emsDashboardTile.setWidgetSource(10);
        Assert.assertEquals(emsDashboardTile.getWidgetSource(),new Integer(10));

    }

    @Test
    public void testGetWidgetSupportTimeControl() {
        emsDashboardTile.setWidgetSupportTimeControl(10);
        Assert.assertEquals(emsDashboardTile.getWidgetSupportTimeControl(),new Integer(10));

    }

    @Test
    public void testGetWidgetTemplate() {
        emsDashboardTile.setWidgetTemplate("elephant");
        Assert.assertEquals(emsDashboardTile.getWidgetTemplate(),"elephant");
    }

    @Test
    public void testGetWidgetUniqueId() {
        emsDashboardTile.setWidgetUniqueId("123456");
        Assert.assertEquals(emsDashboardTile.getWidgetUniqueId(),"123456");

    }

    @Test
    public void testGetWidgetViewmode() {
        emsDashboardTile.setWidgetViewmode("elephant");
        Assert.assertEquals(emsDashboardTile.getWidgetViewmode(),"elephant");
    }

    @Test
    public void testGetWidth() {
        emsDashboardTile.setWidth(10);
        Assert.assertEquals(emsDashboardTile.getWidth(),new Integer(10));
    }

    @Test
    public void testRemoveEmsDashboardTileParams() {
        List<EmsDashboardTileParams> list = new ArrayList<EmsDashboardTileParams>();
        list.add(emsDashboardTileParams);
        emsDashboardTile.setDashboardTileParamsList(list);
        emsDashboardTile.removeEmsDashboardTileParams(emsDashboardTileParams);
    }
}
