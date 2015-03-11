/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.exception.functional;

import oracle.sysman.emaas.platform.dashboards.core.DashboardErrorConstants;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;

/**
 * The class {@code CommonFunctionalException} and its subclasses are a form of {@code DashboardException} that indicates error
 * conditions of UI functional.
 *
 * @author guobaochen
 */
public class CommonFunctionalException extends DashboardException
{
	private static final long serialVersionUID = 4069848538099583269L;
	public static final String DASHBOARD_QUERY_INVALID_OFFSET = "DASHBOARD_QUERY_INVALID_OFFSET";
	public static final String DASHBOARD_QUERY_INVALID_LIMIT = "DASHBOARD_QUERY_INVALID_LIMIT";
	public static final String DASHBOARD_CREATE_SAME_ID_ERROR = "DASHBOARD_CREATE_SAME_ID_ERROR";
	public static final String DASHBOARD_INVALID_TYPE = "DASHBOARD_INVALID_TYPE";
	public static final String DASHBOARD_TILE_INVALID_ID = "DASHBOARD_TILE_INVALID_ID";
	public static final String DASHBOARD_TILE_TITLE_REQUIRED = "DASHBOARD_TILE_TITLE_REQUIRED";
	public static final String TILE_PARAM_NAME_REQUIRED = "TILE_PARAM_NAME_REQUIRED";
	public static final String TILE_PARAM_INVALID_TYPE = "TILE_PARAM_INVALID_TYPE";
	public static final String TILE_PARAM_INVALID_NUMBER_VALUE = "TILE_PARAM_INVALID_NUMBER_VALUE";

	// i18n resource message for invalid dashboard for update
	public static final String WIDGET_UNIQUE_ID_REQUIRED = "WIDGET_UNIQUE_ID_REQUIRED";
	public static final String WIDGET_NAME_REQUIRED = "WIDGET_NAME_REQUIRED";
	public static final String WIDGET_ICON_REQUIRED = "WIDGET_ICON_REQUIRED";
	public static final String WIDGET_HISTOGRAM_REQUIRED = "WIDGET_HISTOGRAM_REQUIRED";
	public static final String WIDGET_OWNER_REQUIRED = "WIDGET_OWNER_REQUIRED";
	public static final String WIDGET_CREATIONTIME_REQUIRED = "WIDGET_CREATIONTIME_REQUIRED";
	public static final String WIDGET_SOURCE_REQUIRED = "WIDGET_SOURCE_REQUIRED";
	public static final String WIDGET_KOC_NAME_REQUIRED = "WIDGET_KOC_NAME_REQUIRED";
	public static final String WIDGET_VIEW_MODEL_REQUIRED = "WIDGET_VIEW_MODEL_REQUIRED";
	public static final String WIDGET_TEMPLATE_REQUIRED = "WIDGET_TEMPLATE_REQUIRED";
	public static final String PROVIDER_NAME_REQUIRED = "PROVIDER_NAME_REQUIRED";
	public static final String PROVIDER_VERSION_REQUIRED = "PROVIDER_VERSION_REQUIRED";
	public static final String PROVIDER_ASSET_ROOT_REQUIRED = "PROVIDER_ASSET_ROOT_REQUIRED";

	// preference messages
	public static final String PREFERENCE_INVALID_KEY = "PREFERENCE_INVALID_KEY";
	public static final String PREFERENCE_INVALID_VALUE = "PREFERENCE_INVALID_VALUE";

	/**
	 * Constructs a new <code>CommonFunctionalException</code> with the specified detail message.
	 */
	public CommonFunctionalException(String message)
	{
		super(DashboardErrorConstants.DASHBOARD_COMMON_UI_ERROR_CODE, message);
	}

	/**
	 * Constructs a new <code>CommonFunctionalException</code> with the specified detail message and cause.
	 *
	 * @param message
	 * @param t
	 */
	public CommonFunctionalException(String message, Throwable t)
	{
		super(DashboardErrorConstants.DASHBOARD_COMMON_UI_ERROR_CODE, message, t);
	}

}
