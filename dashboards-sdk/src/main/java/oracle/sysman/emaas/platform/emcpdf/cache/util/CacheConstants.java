package oracle.sysman.emaas.platform.emcpdf.cache.util;

/**
 * Created by chehao on 2016/12/16.
 */
public class CacheConstants {
    public static final String CACHES_SCREENSHOT_CACHE = "screenshotCache";
    public static final String CACHES_ETERNAL_CACHE = "eternalCache";
    public static final String CACHES_ADMIN_LINK_CACHE = "adminLinkCache";
    public static final String CACHES_CLOUD_SERVICE_LINK_CACHE = "cloudServiceLinkCache";
    public static final String CACHES_HOME_LINK_CACHE = "homeLinkCache";
    public static final String CACHES_VISUAL_ANALYZER_LINK_CACHE = "visualAnalyzerLinkCache";
    public static final String CACHES_VANITY_BASE_URL_CACHE = "vanityBaseUrlCache";
    public static final String CACHES_DOMAINS_DATA_CACHE = "domainsDataCache";
    public static final String CACHES_TENANT_APP_MAPPING_CACHE = "tenantAppMappingCache";
    public static final String CACHES_SSO_LOGOUT_CACHE = "SsoLogoutCache";
    public static final String CACHES_SUBSCRIBED_SERVICE_CACHE = "subscribeCache";
    public static final String CACHES_ASSET_ROOT_CACHE = "assetRootCache";
    public static final String CACHES_REGISTRY_CACHE = "registryCache";
    public static final String CACHES_TENANT_USER_CACHE = "tenantUserCache";
    public static final String CACHES_OOB_DASHBOARD_SAVEDSEARCH_CACHE = "oobDashboardSavedSearchCache";
//    public static final String CACHES_TENANT_SUBSCRIPTION_INFO_CACHE = "tenantSubscriptionInfoCache";
    public static final String CACHES_SERVICE_MENU_CACHE = "serviceMenuCache";
	public static final String CACHES_USER_ROLE_CACHE = "userRolesCache";
	public static final String CACHES_USER_GRANT_CACHE = "userGrantsCache";

	public static final String LOOKUP_CACHE_KEY_SUBSCRIBED_APPS = "subscribedApps";
	public static final String LOOKUP_CACHE_KEY_SUBSCRIBED_APPS_UIFWK = "subscribedApps_uifwk";
	public static final String LOOKUP_CACHE_KEY_EXTERNAL_LINK = "externalLink";
	public static final String LOOKUP_CACHE_KEY_INTERNAL_LINK = "internalLink";
	public static final String LOOKUP_CACHE_KEY_VANITY_BASE_URL = "vanityBaseUrl";
	public static final String LOOKUP_CACHE_KEY_CLOUD_SERVICE_LINKS = "cloudServiceLinks";
	public static final String LOOKUP_CACHE_KEY_ADMIN_LINKS = "adminLinks";
	public static final String LOOKUP_CACHE_KEY_HOME_LINKS = "homeLinks";
	public static final String LOOKUP_CACHE_KEY_VISUAL_ANALYZER = "visualAnalyzer";
	public static final String LOOKUP_CACHE_KEY_ASSET_ROOTS = "assetRoots";
	public static final String LOOKUP_CACHE_KEY_SSO_LOGOUT_URL = "ssoLogoutUrl";
	public static final String LOOKUP_CACHE_KEY_REGISTRY = "registry";
	public static final String LOOKUP_CACHE_KEY_TENANT_USER = "tenantUser";
	public static final String LOOKUP_CACHE_KEY_OOB_DASHBOARD_SAVEDSEARCH = "oobDashboardSavedSearch";
	public static final String LOOKUP_CACHE_KEY_SERVICE_MENU = "serviceMenus";
//    public static final String LOOKUP_CACHE_KEY_TENANT_SUBSCRIPTION_INFO = "tenantSubscriptionInfo";
	public static final String LOOKUP_CACHE_KEY_USER_ROLE= "userRoles";
	public static final String LOOKUP_CACHE_KEY_USER_GRANT= "userGrants";
    
    // Load OOB metadata at runtime
    public static final String CACHES_OOB_DASHBOARD_CACHE = "oobDashboardCache";
    public static final String CACHES_OOB_SAVEDSEARCH_CACHE = "oobSavedSearchCache";

	public static final String SCREENSHOT_BASE64_PNG_PREFIX = "data:image/png;base64,";
	public static final String SCREENSHOT_BASE64_JPG_PREFIX = "data:image/jpeg;base64,";

	//log cache status time interval
	public static final Long LOG_INTERVAL = 300000L;
	//refresh cache interval
	public static final Long REFRESH_CACHE_INTERVAL = 20000L;

	public static int DEFAULT_CAPACITY = 5000;
	public static long DEFAULT_EXPIRATION = 0L;
}
