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
    public static final String CACHES_SERVICE_EXTERNAL_LINK_CACHE = "externalLinkCache";
    public static final String CACHES_SERVICE_INTERNAL_LINK_CACHE = "internalLinkCache";
    public static final String CACHES_VANITY_BASE_URL_CACHE = "vanityBaseUrlCache";
    public static final String CACHES_DOMAINS_DATA_CACHE = "domainsDataCache";
    public static final String CACHES_TENANT_APP_MAPPING_CACHE = "tenantAppMappingCache";
    public static final String CACHES_SSO_LOGOUT_CACHE = "SsoLogoutCache";
    public static final String CACHES_SUBSCRIBED_SERVICE_CACHE = "subscribeCache";
    public static final String CACHES_ASSET_ROOT_CACHE = "assetRootCache";
    public static final String CACHES_REGISTRY_CACHE = "registryCache";
    public static final String CACHES_TENANT_USER_CACHE = "tenantUserCache";
    public static final String CACHES_OOB_DASHBOARD_SAVEDSEARCH_CACHE = "oobDashboardSavedSearchCache";


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


    //below are cache status related message
    public static final String NO_NEED_TO_CHANGE_CACHE_STATUS="No need to change cache status.";
    public static final String CAN_NOT_CHANGE_CACHE_STATUS="Can not change cache status.";
    public static final String CHANGE_CACHE_STATUS_SUCCESSFULLY="Change cache status successfully.";
    public static final String CHANGE_CACHE_STATUS_FAILED="Change cache status failed.";

    public static final String SCREENSHOT_BASE64_PNG_PREFIX = "data:image/png;base64,";
    public static final String SCREENSHOT_BASE64_JPG_PREFIX = "data:image/jpeg;base64,";

    public static final String ZERO_PERCENTAGE = "0.0%";
    public static final String ONE_HUNDRED_PERCENTAGE = "100%";

    public static final String LRU_CACHE_MANAGER="LRUCacheManager";
    public static final String LRU_SCREENSHOT_MANAGER="LRUScreenshotCacheManager";
}
