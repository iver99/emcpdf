/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest.util;

import java.io.IOException;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.ws.rest.model.RoleNamesEntity;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.exception.ExecutionException;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CacheManagers;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.DefaultKeyGenerator;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Keys;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Tenant;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil.VersionedLink;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author aduan
 */
public class PrivilegeChecker
{
	private static final Logger LOGGER = LogManager.getLogger(PrivilegeChecker.class);

	public static final String ADMIN_ROLE_NAME_APM = "APM Administrator";
	public static final String ADMIN_ROLE_NAME_ITA = "IT Analytics Administrator";
	public static final String ADMIN_ROLE_NAME_LA = "Log Analytics Administrator";
	public static final String ADMIN_ROLE_NAME_MONITORING = "Monitoring Service Administrator";
	public static final String ADMIN_ROLE_NAME_SECURITY = "Security Analytics Administrator";
	public static final String ADMIN_ROLE_NAME_COMPLIANCE = "Compliance Administrator";
	public static final String ADMIN_ROLE_NAME_ORCHESTRATION = "Orchestration Administrator";
	public static final String ADMIN_ROLE_NAME_OMC = "OMC Administrator";
	public static final String SECURITY_AUTH_ROLE_CHECK_API = "api/v1/roles/grants/getRoles?grantee=";
	public static final String SECURITY_AUTH_USER_GRANTS_CHECK_API = "api/v1/priv/grants/getUserGrants?granteeUser=";
	private static final String SECURITY_AUTHORIZATION_SERVICENAME = "SecurityAuthorization";
	private static final String SECURITY_AUTHORIZATION_VERSION = "1.0+";

	public static String getUserGrants(String tenantName, String userName)
	{
		String userGrants = null;
		ICacheManager iCacheManager = CacheManagers.getInstance().build();
		ICache cache = iCacheManager.getCache(CacheConstants.CACHES_USER_GRANT_CACHE);
		Object cacheKey = DefaultKeyGenerator.getInstance().generate(new Tenant(tenantName),
				new Keys(CacheConstants.LOOKUP_CACHE_KEY_USER_GRANT, tenantName, userName));
		if (tenantName != null && userName != null) {
			//try to retrieve from cache
			try {
				userGrants = (String) cache.get(cacheKey);
				if(userGrants !=null){
					LOGGER.info("Retrieved user grants data from cache successfully...");
					LOGGER.debug("Retrieved user grants data from cache content is {}", userGrants);
					return userGrants;
				}
			} catch (ExecutionException e) {
				LOGGER.error("Error occurred when retrieve data from cache.");
			}
			//try to retrieve from AuthorizationService
			LOGGER.info("Try to load user grants from Authorization Service...");
			try {
				VersionedLink link = RegistryLookupUtil.getServiceInternalEndpoint(SECURITY_AUTHORIZATION_SERVICENAME,
						SECURITY_AUTHORIZATION_VERSION, tenantName);
				if (link == null || link.getHref() == null) {
					LOGGER.error("Failed to discover SecurityAuthorization service URL for user grants checking.");
				}
				else {
					String tenantDotUser = tenantName + "." + userName;
					String endPoint = link.getHref();
					String secAuthUserGrantsApiUrl = endPoint.endsWith("/") ? endPoint + SECURITY_AUTH_USER_GRANTS_CHECK_API + tenantDotUser
							: endPoint + "/" + SECURITY_AUTH_USER_GRANTS_CHECK_API + tenantDotUser;
					RestClient rc = new RestClient();
					rc.setHeader(RestClient.X_USER_IDENTITY_DOMAIN_NAME,tenantName);
					rc.setHeader(RestClient.OAM_REMOTE_USER,tenantDotUser);
					rc.setType(null);
					rc.setAccept(null);
					userGrants = rc.get(secAuthUserGrantsApiUrl, tenantName, link.getAuthToken());
					LOGGER.debug("Checking user grants for tenant user (" + tenantDotUser + "). The response is " + userGrants);
					if(userGrants!=null){
						LOGGER.info("Put user grants into cache...");
						cache.put(cacheKey, userGrants);
					}
				}
			} catch (Exception e) {
				LOGGER.error(e);
			}
		}
		else {
			LOGGER.warn("Tenant name or user name is empty. User grants checking is skipped.");
		}

		return userGrants;
	}
	
	public static List<String> getUserRoles(String tenantName, String userName)
	{
		List<String> roleNames = null;
		ICacheManager iCacheManager = CacheManagers.getInstance().build();
		ICache cache = iCacheManager.getCache(CacheConstants.CACHES_USER_ROLE_CACHE);
		Object cacheKey = DefaultKeyGenerator.getInstance().generate(new Tenant(tenantName),
				new Keys(CacheConstants.LOOKUP_CACHE_KEY_USER_ROLE, tenantName, userName));
		if (tenantName != null && userName != null) {
			//try to load from cache
			try {
				roleNames = (List<String>) cache.get(cacheKey);
				if(roleNames !=null){
					LOGGER.info("Retrieved user roles from cache successfully, content is {}", roleNames);
					return roleNames;
				}
			} catch (ExecutionException e) {
				LOGGER.error("Error occurred when retrieve data from cache.");
			}
			//try to load from Authorization Service
			LOGGER.info("Try to load from Authorization Service...");
			try {
				VersionedLink link = RegistryLookupUtil.getServiceInternalEndpoint(SECURITY_AUTHORIZATION_SERVICENAME,
						SECURITY_AUTHORIZATION_VERSION, tenantName);
				if (link == null || link.getHref() == null) {
					LOGGER.error("Failed to discover SecurityAuthorization service URL for privilege checking.");
				}
				else {
					String tenantDotUser = tenantName + "." + userName;
					String endPoint = link.getHref();
					String secAuthRolesApiUrl = endPoint.endsWith("/") ? endPoint + SECURITY_AUTH_ROLE_CHECK_API + tenantDotUser
							: endPoint + "/" + SECURITY_AUTH_ROLE_CHECK_API + tenantDotUser;
					RestClient rc = new RestClient();
					rc.setHeader(RestClient.X_USER_IDENTITY_DOMAIN_NAME,tenantName);
					rc.setHeader(RestClient.OAM_REMOTE_USER,tenantDotUser);
					rc.setType(null);
					rc.setAccept(null);
					String roleCheckResponse = rc.get(secAuthRolesApiUrl, tenantName, link.getAuthToken());
					LOGGER.debug("Checking roles for tenant user (" + tenantDotUser + "). The response is " + roleCheckResponse);
					JsonUtil ju = JsonUtil.buildNormalMapper();
					RoleNamesEntity rne = ju.fromJson(roleCheckResponse, RoleNamesEntity.class);
					if (rne != null && rne.getRoleNames() != null) {
						roleNames = rne.getRoleNames();
						LOGGER.info("Put user roles into cache...{}",roleNames);
						cache.put(cacheKey, roleNames);
					}
				}
			}catch (IOException e) {
				LOGGER.error(e);
			}
		}
		else {
			LOGGER.warn("Tenant name or user name is empty. isAdminUser checking is skipped.");
		}

		return roleNames;
	}

	public static boolean isAdminUser(List<String> userRoles)
	{
		boolean isAdmin = false;
		if (userRoles != null && !userRoles.isEmpty()) {
			if (userRoles.contains(ADMIN_ROLE_NAME_OMC) || userRoles.contains(ADMIN_ROLE_NAME_APM)
					|| userRoles.contains(ADMIN_ROLE_NAME_ITA) || userRoles.contains(ADMIN_ROLE_NAME_LA)
					|| userRoles.contains(ADMIN_ROLE_NAME_MONITORING) || userRoles.contains(ADMIN_ROLE_NAME_SECURITY)
					|| userRoles.contains(ADMIN_ROLE_NAME_COMPLIANCE) || userRoles.contains(ADMIN_ROLE_NAME_ORCHESTRATION)) {
				isAdmin = true;
			}
		}
		return isAdmin;
	}

	private PrivilegeChecker()
	{
	}
}
