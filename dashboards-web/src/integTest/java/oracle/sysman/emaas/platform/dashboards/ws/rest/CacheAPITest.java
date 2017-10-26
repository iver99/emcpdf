package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.ws.rest.model.AppsInfoWeb;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.support.lru.LRUCacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.lru.LinkedHashMapCache;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheStatus;
import oracle.sysman.emaas.platform.emcpdf.tenant.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.emcpdf.tenant.subscription2.AppsInfo;
import oracle.sysman.emaas.platform.emcpdf.tenant.subscription2.TenantSubscriptionInfo;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by xiadai on 2017/4/7.
 */
public class CacheAPITest {
    private CacheAPI cacheAPI;
    @Mocked
    LRUCacheManager lruCacheManager;
    @Mocked
    ICache iCache;

    @Test(groups = {"s2"})
    public void testStopCache() throws Exception {
        cacheAPI = new CacheAPI();
        final ConcurrentMap<String, ICache> map = new ConcurrentHashMap<String, ICache>();
        map.put("key", new LinkedHashMapCache("name", 1, 1L));
        new Expectations() {
            {
                LRUCacheManager.getInstance();
                result = lruCacheManager;
                lruCacheManager.getCacheMap();
                result = map;
            }
        };
        cacheAPI.stopCache();
    }

    @Test(groups = {"s2"})
    public void testStartCache() throws Exception {
        cacheAPI = new CacheAPI();
        final ConcurrentMap<String, ICache> map = new ConcurrentHashMap<String, ICache>();
        map.put("key", new LinkedHashMapCache("name", 1, 1L));
        new Expectations() {
            {
                LRUCacheManager.getInstance();
                result = lruCacheManager;
                lruCacheManager.getCacheMap();
                result = map;
            }
        };
        cacheAPI.startCache();
    }

    @Test(groups = {"s2"})
    public void testSuspendCache() throws Exception {
        cacheAPI = new CacheAPI();
        final ConcurrentMap<String, ICache> map = new ConcurrentHashMap<String, ICache>();
        map.put("key", new LinkedHashMapCache("name", 1, 1L));
        new Expectations() {
            {
                LRUCacheManager.getInstance();
                result = lruCacheManager;
                lruCacheManager.getCacheMap();
                result = map;
            }
        };
        cacheAPI.suspendCache();
    }

    @Test(groups = {"s2"})
    public void testResumeCache() throws Exception {
        cacheAPI = new CacheAPI();
        final ConcurrentMap<String, ICache> map = new ConcurrentHashMap<String, ICache>();
        map.put("key", new LinkedHashMapCache("name", 1, 1L));
        new Expectations() {
            {
                LRUCacheManager.getInstance();
                result = lruCacheManager;
                lruCacheManager.getCacheMap();
                result = map;
            }
        };
        cacheAPI.resumeCache();
    }


    @Test(groups = {"s2"})
    public void testResumeCacheNull() throws Exception {
        cacheAPI = new CacheAPI();
        final ConcurrentMap<String, ICache> map = new ConcurrentHashMap<String, ICache>();
        map.put("key", new LinkedHashMapCache("name", 1, 1L));
        new Expectations() {
            {
                LRUCacheManager.getInstance();
                result = lruCacheManager;
                lruCacheManager.getCacheMap();
                result = null;
            }
        };
        cacheAPI.resumeCache();
        cacheAPI.stopCache();

        AppsInfoWeb appsInfoWeb = new AppsInfoWeb();
        appsInfoWeb.getEditions();
        appsInfoWeb.getId();
        appsInfoWeb.getLicVersion();
        appsInfoWeb.setEditions(null);
        appsInfoWeb.setId(null);
        appsInfoWeb.setLicVersion(null);

    }

    @Mocked
    TenantContext tenantContext;
    @Mocked
    TenantSubscriptionUtil tenantSubscriptionUtil;
    @Mocked
    TenantSubscriptionInfo tenantSubscriptionInfo;

    @Test(groups = {"s2"})
    public void testTenantSubscriptionsAPIv2() throws Exception {
        final List<AppsInfo> appsInfoList = new ArrayList<>();
        appsInfoList.add(new AppsInfo("", "", null));
        TenantSubscriptionsAPIv2 tenantSubscriptionsAPIv2 = new TenantSubscriptionsAPIv2();
        tenantSubscriptionsAPIv2.getSubscribedApplications("1", "emaastesttenant1.emcsadmin", "");
        new Expectations() {
            {
                TenantContext.getCurrentTenant();
                result = "tenant";
                TenantSubscriptionUtil.getTenantSubscribedServices(anyString, (TenantSubscriptionInfo) any);
                tenantSubscriptionInfo.getAppsInfoList();
                result = new ArrayList<>();
                tenantSubscriptionInfo.getAppsInfoList();
                result = appsInfoList;
            }
        };
        tenantSubscriptionsAPIv2.getSubscribedApplications("1", "emaastesttenant1.emcsadmin", "");
        tenantSubscriptionsAPIv2.getSubscribedApplications("", "", "");

    }

    @Test(groups = {"s2"})
    public void testChangeCacheGroupStatusByName() throws Exception{
        cacheAPI = new CacheAPI();
        final ConcurrentMap<String, ICache> map = new ConcurrentHashMap<String, ICache>();
        map.put("key", new LinkedHashMapCache("name", 1, 1L));
        new Expectations() {
            {

                lruCacheManager.getCacheMap();
                result = map;
                result = new ConcurrentHashMap<>();
                result = null;
            }
        };
        Deencapsulation.invoke(cacheAPI,"changeCacheGroupStatusByName",lruCacheManager, CacheStatus.AVAILABLE,"key");
        Deencapsulation.invoke(cacheAPI,"changeCacheGroupStatusByName",lruCacheManager, CacheStatus.AVAILABLE,"key");
        Deencapsulation.invoke(cacheAPI,"changeCacheGroupStatusByName",lruCacheManager, CacheStatus.AVAILABLE,"key");
    }

    @Test(groups = {"s2"})
    public void testClearCacheGroupDataByName() throws Exception{
        cacheAPI = new CacheAPI();
        final ConcurrentMap<String, ICache> map = new ConcurrentHashMap<String, ICache>();
        map.put("key", new LinkedHashMapCache("name", 1, 1L));
        new Expectations() {
            {

                lruCacheManager.getCacheMap();
                result = map;
                result = new ConcurrentHashMap<>();
                result = null;
            }
        };
        Deencapsulation.invoke(cacheAPI,"clearCacheGroupDataByName",lruCacheManager,"key");
        Deencapsulation.invoke(cacheAPI,"clearCacheGroupDataByName",lruCacheManager,"key");
        Deencapsulation.invoke(cacheAPI,"clearCacheGroupDataByName",lruCacheManager,"key");
    }

    @Test(groups = {"s2"})
    public void testResetCacheStatisticsByName() throws Exception{
        cacheAPI = new CacheAPI();
        final ConcurrentMap<String, ICache> map = new ConcurrentHashMap<String, ICache>();
        map.put("key", new LinkedHashMapCache("name", 1, 1L));
        new Expectations() {
            {

                lruCacheManager.getCacheMap();
                result = map;
                result = new ConcurrentHashMap<>();
                result = null;
            }
        };
        Deencapsulation.invoke(cacheAPI,"resetCacheStatisticsByName",lruCacheManager,"key");
        Deencapsulation.invoke(cacheAPI,"resetCacheStatisticsByName",lruCacheManager,"key");
        Deencapsulation.invoke(cacheAPI,"resetCacheStatisticsByName",lruCacheManager,"key");
    }
}