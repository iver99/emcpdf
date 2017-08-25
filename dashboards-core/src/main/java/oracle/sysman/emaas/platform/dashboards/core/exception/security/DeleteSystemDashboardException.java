/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.exception.security;

import oracle.sysman.emaas.platform.dashboards.core.DashboardErrorConstants;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;

/**
 * @author guobaochen
 */
public class DeleteSystemDashboardException extends DashboardException
{
	private static final long serialVersionUID = 5123899685789229699L;
	private static final String NOT_SUPPORT_DELETE_SYSTEM_DASHBOARD_ERROR = CommonSecurityException.NOT_SUPPORT_DELETE_SYSTEM_DASHBOARD_ERROR;

	/**
	 * Constructs a new <code>DeleteSystemDashboardException</code>
	 */
	public DeleteSystemDashboardException()
	{
		super(DashboardErrorConstants.NOT_SUPPORT_DELETE_SYSTEM_DASHBOARD_ERROR_CODE, MessageUtils
				.getDefaultBundleString(NOT_SUPPORT_DELETE_SYSTEM_DASHBOARD_ERROR));
	}

}
