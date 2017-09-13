package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ZDTComparatorRowsEntityTest {
	
	@Test
	public void testZdtComparatorRowsEntityGetterSetter() {
		ZDTComparatorStatusRowEntity entity = new ZDTComparatorStatusRowEntity( "comparisonDate", 
				"comparisonType",
				"nextComparisonDate", 
				0.99, 
				"comparisonResult");
		
		Assert.assertEquals(entity.getComparisonDate(), "comparisonDate");
		Assert.assertEquals(entity.getComparisonType(), "comparisonType");
		Assert.assertEquals(entity.getNextComparisonDate(), "nextComparisonDate");
		Assert.assertEquals(entity.getDivergencePercentage(), 0.99);
		
		entity.setComparisonDate("");
		entity.setComparisonType("");
		entity.setDivergencePercentage(0.0);
		entity.setNextComparisonDate("");
	}

}
