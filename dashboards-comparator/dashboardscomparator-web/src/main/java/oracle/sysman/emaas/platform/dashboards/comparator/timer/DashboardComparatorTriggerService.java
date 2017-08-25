package oracle.sysman.emaas.platform.dashboards.comparator.timer;

import oracle.sysman.emaas.platform.dashboards.comparator.webutils.wls.lifecycle.AbstractApplicationLifecycleService;

public class DashboardComparatorTriggerService extends AbstractApplicationLifecycleService {
	
	public DashboardComparatorTriggerService()
	{		
		addApplicationServiceManager(new DashboardComparatorServiceManager());
	}

}
