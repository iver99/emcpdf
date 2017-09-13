package oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows;


import org.testng.Assert;
import org.testng.annotations.Test;

public class ZDTComparatorSyncStatusRowEntityTest {
	
	@Test
	public void testZdtComparatorRowsEntityGetterSetter() {
		ZDTSyncStatusRowEntity entity = new ZDTSyncStatusRowEntity( "comparisonDate", 
				"comparisonType",
				"nextComparisonDate", 
				0.99);
		
		Assert.assertEquals(entity.getLastSyncDateTime(), "comparisonDate");
		Assert.assertEquals(entity.getSyncType(), "comparisonType");
		Assert.assertEquals(entity.getNextScheduledSyncDateTime(), "nextComparisonDate");
		Assert.assertEquals(entity.getDivergencePercentage(), 0.99);
		
		entity.setLastSyncDateTime("");
		entity.setSyncType("");
		entity.setDivergencePercentage(0.0);
		entity.setNextScheduledSyncDateTime("");
	}

}
