package oracle.sysman.emaas.platform.dashboards.ws.rest.zdt;

import org.testng.annotations.Test;

import java.math.BigInteger;

import static org.testng.Assert.*;

/**
 * Created by xiadai on 2017/1/11.
 */
@Test(groups= {"s1"})
public class ZDTEntityTest {
    private ZDTEntity zdtEntity = new ZDTEntity();
    @Test
    public void testZDTEnity(){
        zdtEntity.hashCode();
        zdtEntity.toString();
        zdtEntity.setCountOfDashboards(1L);
        zdtEntity.setCountOfUserOptions(1L);
        zdtEntity.setCountOfPreference(1L);
        zdtEntity.getCountOfDashboards();
        zdtEntity.getCountOfUserOptions();
        zdtEntity.getCountOfPreference();
        zdtEntity.equals(new ZDTEntity());
        zdtEntity.equals(null);
        zdtEntity.equals(new BigInteger("1"));
        zdtEntity.equals(zdtEntity);
        zdtEntity = new ZDTEntity(1L,1l,1L,2L,3L,4L);
    }

}