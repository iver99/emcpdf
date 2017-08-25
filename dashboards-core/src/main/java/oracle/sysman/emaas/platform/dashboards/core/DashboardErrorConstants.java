/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core;

/**
 * Class to define constants that represent the error code returned to the end user For specific error code that has corresponding
 * error message, better please keep the constants name consistent with the message key. e.g.: error message resource key for
 * DASHBOARD_SAME_NAME_EXISTS_ERROR_CODE is DASHBOARD_SAME_NAME_EXISTS_ERROR
 *
 * @author guobaochen
 */
public class DashboardErrorConstants
{
	private DashboardErrorConstants() {
	  }

	// error code for configuration/registry
	public static final int CONFIGURATIONS_GENERIC_ERROR_CODE = 1000;
	public static final int CONFIGURATIONS_REGISTRATION_ERROR_CODE = 1100;
	public static final int CONFIGURATIONS_REGISTRATION_REGISTRYURLS_NOT_FOUND_ERROR_CODE = 1101;
	public static final int CONFIGURATIONS_REGISTRATION_SSF_SERVICENAME_NOT_FOUND_ERROR_CODE = 1102;
	public static final int CONFIGURATIONS_REGISTRATION_SSF_VERSION_NOT_FOUND_ERROR_CODE = 1103;
	public static final int REGISTRY_LOOKUP_GENERIC_ERROR_CODE = 2000;
	public static final int REGISTRY_LOOKUP_LINK_NOT_FOUND_ERROR_CODE = 2001;
	public static final int REGISTRY_LOOKUP_ENDPOINT_NOT_FOUND_ERROR_CODE = 2002;
	public static final int REGISTRY_LOOKUP_LINK_WIT_REL_PREFIX_NOT_FOUND_ERROR_CODE = 2003;
	public static final int REGISTRY_LOOKUP_VANITY_URL_NOT_FOUND_ERROR_CODE = 2004;

	public static final int UNKNOWN_ERROR_CODE = 9999;

	public static final Integer DASHBOARD_COMMON_UI_ERROR_CODE = 10000;
	public static final Integer DASHBOARD_SAME_NAME_EXISTS_ERROR_CODE = 10001;
	public static final Integer DASHBOARD_TEXT_WIDGET_EMPTY_CONTENT_ERROR_CODE = 10002;
	public static final Integer DASHBOARD_TEXT_WIDGET_CONTENT_TOO_LONG_ERROR_CODE = 10003;

	// important: don't assign value larger than this value to dashboard ui request errors
	public static final Integer DASHBOARD_UI_MAX_ERROR_CODE = 19999;

	public static final Integer DASHBOARD_COMMON_RESOURCE_ERROR_CODE = 20000;
	public static final Integer DASHBOARD_NOT_FOUND_ERROR_CODE = 20001;
	public static final Integer TENANT_NO_SUBSCRIPTION_ERROR_CODE = 20002;
	public static final Integer USER_OPTIONS_NOT_FOUND_ERROR_CODE = 20003;
	public static final Integer JSON_FORMAT_ERROR_CODE = 20004;
	public static final Integer WIDGET_NOT_EXISTED_EXCEPTION_CODE = 20005;
	
	public static final Integer DEPENDENCY_UNAVAILABLE_ERROR_CODE = 20006;
	public static final Integer ENTITY_NAMING_DEPENDENCY_UNAVAILABLE_ERROR_CODE = DEPENDENCY_UNAVAILABLE_ERROR_CODE;
	public static final Integer DATABASE_DEPENDENCY_UNAVAILABLE_ERROR_CODE = DEPENDENCY_UNAVAILABLE_ERROR_CODE;

	// important: don't assign value larger than this value to dashboard resource errors
	public static final Integer DASHBOARD_RESOURCE_MAX_ERROR_CODE = 29999;

	public static final Integer DASHBOARD_COMMON_SECURITY_ERROR_CODE = 30000;
	public static final Integer NOT_SUPPORT_DELETE_SYSTEM_DASHBOARD_ERROR_CODE = 30001;
	public static final Integer NOT_SUPPORT_CREATE_SYSTEM_DASHBOARD_ERROR_CODE = 30002;
	public static final Integer NOT_SUPPORT_UPDATE_SYSTEM_DASHBOARD_ERROR_CODE = 30003;

	// important: don't assign value larger than this value to dashboard security errors
	public static final Integer DASHBOARD_SECURITY_MAX_ERROR_CODE = 39999;

	//keep cache code same with SSF side
	public static final Integer DASHBOARD_CACHE_ERROR_CODE=50000;
	public static final Integer DASHBOARD_CACHE_GROUP_NOT_FOUND_ERROR_CODE=50001;
	public static final Integer DASHBOARD_CACHE_GROUP_NAME_EMPTY_ERROR_CODE=50002;
	// important: don't assign value larger than this value to dashboard cache errors
	public static final Integer DASHBOARD_CACHE_MAX_ERROR_CODE=59999;
}
