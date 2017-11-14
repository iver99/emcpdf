package oracle.sysman.emaas.platform.dashboards.entity;

import java.math.BigInteger;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * Created by Troy on 2016/1/20.
 */
@Test(groups = {"s1"})
public class EmsDashboardTileParamsTest { 
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
            0,
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
    public void testGetDashboardTile() {
        emsDashboardTileParams.setDashboardTile(emsDashboardTile);
        Assert.assertEquals(emsDashboardTileParams.getDashboardTile(),emsDashboardTile);
    }

    @Test
    public void testGetIsSystem() {
        emsDashboardTileParams.setIsSystem(10);
        Assert.assertEquals(emsDashboardTileParams.getIsSystem(),new Integer(10));
    }

    @Test
    public void testGetParamName() {
        emsDashboardTileParams.setParamName("elephant");
        Assert.assertEquals(emsDashboardTileParams.getParamName(),"elephant");
    }

    @Test
    public void testGetParamType() {
        emsDashboardTileParams.setParamType(10);
        Assert.assertEquals(emsDashboardTileParams.getParamType(),new Integer(10));
    }

    @Test
    public void testGetParamValueNum() {
        emsDashboardTileParams.setParamValueNum(10);
        Assert.assertEquals(emsDashboardTileParams.getParamValueNum(),new Integer(10));

    }

    @Test
    public void testGetParamValueStr() {
        emsDashboardTileParams.setParamValueStr("elephant");
        Assert.assertEquals(emsDashboardTileParams.getParamValueStr(),"elephant");
    }

    @Test
    public void testGetParamValueTimestamp() {
        emsDashboardTileParams = new EmsDashboardTileParams();
        emsDashboardTileParams.setParamValueTimestamp(now);
        Assert.assertEquals(emsDashboardTileParams.getParamValueTimestamp(), now);
    }
}
