package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts.CountsEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts.DashboardCountsComparator;

import org.testng.annotations.Test;

public class DashboardCountComparatorTest {
	
	@Test
	public void testcompareInstancesCounts() {
		CountsEntity entity1 = new CountsEntity();
		entity1.setCountOfDashboards(10L);
		entity1.setCountOfPreference(11L);
		entity1.setCountOfUserOptions(13L);
		
		CountsEntity entity2 = new CountsEntity();
		entity2.setCountOfDashboards(15L);
		entity2.setCountOfPreference(16L);
		entity2.setCountOfUserOptions(17L);
		
		DashboardCountsComparator comparator = new DashboardCountsComparator();
		comparator.compareInstancesCounts("key1", null, entity1,
				"key2", null, entity2);
	}

}
