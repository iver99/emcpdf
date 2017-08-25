package oracle.sysman.emaas.platform.dashboards.core.exception.security;

import oracle.sysman.emaas.platform.dashboards.core.DashboardErrorConstants;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;

/**
 * Created by guochen on 8/7/17.
 */
public class UpdateSystemDashboardException extends DashboardException {
	/**
	 * Constructs a new <code>UpdateSystemDashboardException</code>
	 */
	public UpdateSystemDashboardException()
	{
		super(DashboardErrorConstants.NOT_SUPPORT_UPDATE_SYSTEM_DASHBOARD_ERROR_CODE, MessageUtils
				.getDefaultBundleString(CommonSecurityException.NOT_SUPPORT_UPDATE_SYSTEM_DASHBOARD_ERROR));
	}
}
