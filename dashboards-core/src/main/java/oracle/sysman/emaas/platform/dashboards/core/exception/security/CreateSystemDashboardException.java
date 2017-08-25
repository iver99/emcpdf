package oracle.sysman.emaas.platform.dashboards.core.exception.security;

import oracle.sysman.emaas.platform.dashboards.core.DashboardErrorConstants;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;

/**
 * Created by guochen on 8/6/17.
 */
public class CreateSystemDashboardException extends DashboardException {
	/**
	 * Constructs a new <code>CreateSystemDashboardException</code>
	 */
	public CreateSystemDashboardException()
	{
		super(DashboardErrorConstants.NOT_SUPPORT_CREATE_SYSTEM_DASHBOARD_ERROR_CODE, MessageUtils
				.getDefaultBundleString(CommonSecurityException.NOT_SUPPORT_CREATE_SYSTEM_DASHBOARD_ERROR));
	}
}
