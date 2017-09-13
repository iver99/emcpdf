package oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows;


import org.testng.Assert;
import org.testng.annotations.Test;

public class ZDTComparatorStatusRowEntityTest {
	
	@Test
	public void testZDTComparatorStatusRowEntityGetterSetter() {
		ZDTComparatorStatusRowEntity entity = new ZDTComparatorStatusRowEntity( "comparisonDate", 
				"comparisonType",
				"nextComparisonDate", 
				"0.99");
		
		Assert.assertEquals(entity.getComparisonDate(), "comparisonDate");
		Assert.assertEquals(entity.getComparisonType(), "comparisonType");
		Assert.assertEquals(entity.getNextComparisonDate(), "nextComparisonDate");
	//	Assert.assertEquals(entity.getDivergencePercentage(), "0.99");
		entity.getDivergencePercentage();
		entity.setComparisonDate("");
		entity.setComparisonType("");
		entity.setDivergencePercentage("0.0");
		entity.setNextComparisonDate("");
	}

}
