package oracle.sysman.emaas.platform.emcpdf.tenant;


import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.metadata.ApplicationEditionConverter;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.exception.CacheInconsistencyException;
import oracle.sysman.emaas.platform.emcpdf.cache.exception.ExecutionException;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CacheManagers;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.DefaultKeyGenerator;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Keys;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Tenant;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;
import oracle.sysman.emaas.platform.emcpdf.tenant.lookup.RetryableLookupClient;
import oracle.sysman.emaas.platform.emcpdf.tenant.subscription2.*;
import oracle.sysman.emaas.platform.emcpdf.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author chehao
 */
public class TenantSubscriptionUtil {
    private static Boolean IS_TEST_ENV = null;

    private static Object lock = new Object();

    private static final Logger LOGGER = LogManager.getLogger(TenantSubscriptionUtil.class);

    public static String getTenantSubscribedServicesString(final String tenant) {
        List<String> apps = TenantSubscriptionUtil.getTenantSubscribedServices(tenant, new TenantSubscriptionInfo());
        if (apps == null) {
            apps = Collections.<String>emptyList();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{\"applications\":[");
        for (int i = 0; i < apps.size(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append("\"").append(apps.get(i)).append("\"");
        }
        sb.append("]}");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public static List<String> getTenantSubscribedServices(final String tenant, final TenantSubscriptionInfo tenantSubscriptionInfo) {
        // for junit test only
        if (Boolean.TRUE.equals(IS_TEST_ENV)) {
            LOGGER.warn("In test environment, the subscribed applications for are tenants are specified to \"APM\" and \"ITAnalytics\"");
            return Arrays.asList(new String[]{"APM", "ITAnalytics"});
        }

		// normal behavior here
		if (tenant == null) {
			LOGGER.warn("This is usually unexpected: now it's trying to retrieve subscribed applications for null tenant");
			return Collections.emptyList();
		}
		final ICacheManager cm= CacheManagers.getInstance().build();
		final Tenant cacheTenant = new Tenant(tenant);
        final Object cacheKey = DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS));
        CachedTenantSubcriptionInfo cachedTenantSubcriptionInfo = null;
        List<String> cachedApps;
        try {
            LOGGER.info("Trying to retrieve tenant subscription info from cache....");
            cachedTenantSubcriptionInfo = (CachedTenantSubcriptionInfo) cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).get(cacheKey);

            if (cachedTenantSubcriptionInfo != null) {
                cachedApps = cachedTenantSubcriptionInfo.getSubscribedAppsList();
                TenantSubscriptionInfo tenantSubscriptionInfo1 = cachedTenantSubcriptionInfo.getTenantSubscriptionInfo();
                LOGGER.info("retrieved tenantSubscriptionInfo for tenant {} from cache,data is {}", tenant, tenantSubscriptionInfo1);
                LOGGER.info("retrieved subscribed apps for tenant {} from cache,data is {}", tenant, cachedApps);
                /**
                 * CachedApps in cache can be empty now. Empty subscribedapps are put into cache may due to some unknow reason cause DF cannot retrieve
                 * subscribedapps correctly, may be 404 of 'serviceRequest', or 503 error returned by 'serviceRequest'. Then we put empty subscribedapps
                 * into cache in order to save other threads/requests' time.
                 */
                if (cachedApps != null) {
                    if(tenantSubscriptionInfo1 != null){
                        copyTenantSubscriptionInfo(tenantSubscriptionInfo1, tenantSubscriptionInfo);
                    }
                    return cachedApps;
                }
            }
        } catch (ExecutionException e) {
            LOGGER.error("Error occurred when using cache...");
        }

        LOGGER.info("Trying to retrieve subscription info from /serviceRequest for tenant {}",tenant);
        List<String> apps = new RetryableLookupClient<List<String>>().connectAndDoWithRetry("TenantService", "1.0+", "collection/tenants", false, null, new RetryableLookupClient.RetryableRunner<List<String>>() {
            @Override
            public List<String> runWithLink(RegistryLookupUtil.VersionedLink lookupLink) throws Exception {
                if (lookupLink == null || lookupLink.getHref() == null || "".equals(lookupLink.getHref())) {
                    LOGGER.warn(
                            "Failed to get entity naming service, or its rel (collection/lookups) link is empty. Exists the retrieval of subscribed service for tenant {}",
                            tenant);
                    List<String> emptyStringList = Collections.emptyList();
                    cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).put(cacheKey, new CachedTenantSubcriptionInfo(emptyStringList, null));
                    return emptyStringList;
                }

                LOGGER.info("Checking tenant (" + tenant + ") subscriptions. The serviceRequest lookups href is " + lookupLink.getHref());
                String queryHref = lookupLink.getHref() + "/" + tenant + "/serviceRequest";
                LOGGER.info("query new serviceRequest url is {}", queryHref);
                RestClient rc = new RestClient();
                long subappQueryStart = System.currentTimeMillis();
                String appsResponse = null;
                try {
                    rc.setHeader("X-USER-IDENTITY-DOMAIN-NAME", tenant);
                    appsResponse = rc.getWithException(queryHref, tenant, lookupLink.getAuthToken());
                } catch (UniformInterfaceException e) {
                    // will print HTTP response code when response is not null.
                    if (e.getResponse() != null) {
                        LOGGER.error("Got HTTP response code {} when getting tenant {} subscribed apps", e.getResponse().getStatus(), tenant);
                        //only resp code 503 will trigger retry logic.
                        if(e.getResponse().getStatus() == 503){
                            LOGGER.error(e);
                            throw new RetryableLookupClient.RetryableLookupException(e);
                        }
                    }
                    LOGGER.error(e);
                    throw e;
                } catch (ClientHandlerException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof SocketTimeoutException) {
                        // don't want to retry if got Read Timeout.
                        LOGGER.error(e);
                        throw e;
                    }
                    LOGGER.error(e);
                    throw new RetryableLookupClient.RetryableLookupException(e);
                }
                String responseLog = appsResponse.length()>=120 ? appsResponse.substring(0,120) : appsResponse;
                //print part of the response, if cannot get the right information we need, then we print full response later.
                LOGGER.info("Retrieved data for tenant ({}) from serviceRequest API. URL is {}, part of the query response is {}.... It took {}ms", tenant, queryHref, responseLog, (System.currentTimeMillis() - subappQueryStart));
                JsonUtil ju = JsonUtil.buildNormalMapper();
                try {
                    List<ServiceRequestCollection> src = ju.fromJsonToList(appsResponse, ServiceRequestCollection.class);
                    if (src == null || src.isEmpty()) {
                        LOGGER.error("Checking tenant (" + tenant + ") subscriptions. Empty application mapping items are retrieved");
                        LOGGER.info("#1.Full response from /serviceRequest is {}", appsResponse);
                        List<String> emptyStringList = Collections.emptyList();
                        cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).put(cacheKey, new CachedTenantSubcriptionInfo(emptyStringList, null));
                        return emptyStringList;
                    }
                    List<SubscriptionApps> subAppsList = new ArrayList<SubscriptionApps>();
                    for (ServiceRequestCollection s : src) {
                        SubscriptionApps subscriptionApps = new SubscriptionApps();
                        if (s.getOrderComponents() != null) {
                            subscriptionApps.setTrial(s.isTrial());
                            subscriptionApps.setServiceType(s.getServiceType());
                            OrderComponents orderComponents = s.getOrderComponents();
                            if (orderComponents.getServiceComponent() != null && orderComponents.getServiceComponent().getComponent() != null) {
                                for (Component com : orderComponents.getServiceComponent().getComponent()) {
                                    EditionComponent editionComponent = new EditionComponent();
                                    editionComponent.setComponentId(orderComponents.getServiceComponent().getComponent_id());
                                    if (com != null && com.getComponent_parameter() != null && !com.getComponent_parameter().isEmpty()) {
                                        for (ComponentParameter componentParameter : com.getComponent_parameter()) {
                                            if ("APPLICATION_EDITION".equals(componentParameter.getKey())) {
                                                editionComponent.setEdition(componentParameter.getValue());
                                                subscriptionApps.addEditionComponent(editionComponent);
                                                LOGGER.info("EditionComponent with id {} and edition {} is added", editionComponent.getComponentId(), editionComponent.getEdition());
                                            }
                                        }
                                    }
                                }
                            }
                            subAppsList.add(subscriptionApps);
                        }
                    }
                    tenantSubscriptionInfo.setSubscriptionAppsList(subAppsList);
                    //Edition info integrity check...
                    if(!checkEditionInfoIntegrity(subAppsList)){
                        LOGGER.info("#2.Full response from /serviceRequest is {}", appsResponse);
                    }

                    List<String> subscribeAppsList = SubscriptionAppsUtil.getSubscribedAppsList(tenantSubscriptionInfo);
                    //Edition info integrity check...
                    if(!editionInfoIntegrityCheck(subAppsList)){
                        LOGGER.info("#2.Full response from /serviceRequest is {}", appsResponse);
                    }
                    LOGGER.info("After mapping Subscribed App list is {}", subscribeAppsList);
                    if (subscribeAppsList == null) {
                        LOGGER.error("After Mapping action,Empty subscription list found!");
                        LOGGER.info("#3.Full response from /serviceRequest is {}", appsResponse);
                        List<String> emptyStringList = Collections.emptyList();
                        cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).put(cacheKey, new CachedTenantSubcriptionInfo(emptyStringList, null));
                        return emptyStringList;
                    }
                    LOGGER.info("Put subscribe apps into cache,{},{}", subscribeAppsList, tenantSubscriptionInfo);
                    cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).put(cacheKey,new CachedTenantSubcriptionInfo(subscribeAppsList, tenantSubscriptionInfo));
                    return subscribeAppsList;

                } catch (IOException e) {
                    LOGGER.error(e);
                    List<String> emptyStringList = Collections.emptyList();
                    cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).put(cacheKey, new CachedTenantSubcriptionInfo(emptyStringList, null));
                    return emptyStringList;
                }
            }
        });
        if (apps == null) {
            apps = Collections.emptyList();
            cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).put(cacheKey, new CachedTenantSubcriptionInfo(apps, null));
            LOGGER.warn("Retrieved null list of subscribed apps for tenant {}", tenant);
        }
        return apps;
    }

    private static boolean checkEditionInfoIntegrity(List<SubscriptionApps> subAppsList) {
        if(!subAppsList.isEmpty()){
            LOGGER.info("Checking edition info's integrity");
            if(subAppsList.get(0).getEditionComponentsList()!=null && !subAppsList.get(0).getEditionComponentsList().isEmpty() ){
                String editionInfo = subAppsList.get(0).getEditionComponentsList().get(0).getEdition();
                if(!StringUtils.isEmpty(editionInfo)){
                    LOGGER.info("Integrity of edition info check passed...");
                    return true;
                }
            }
        }
        LOGGER.warn("Integrity of edition info check failed...");
        return false;
    }

    public static boolean isAPMServiceOnly(List<String> services) {
        LOGGER.debug("Checking if only APM is subscribed, checked services are {}", services == null ? null : services.toString());
        if (services == null || services.size() != 1) {
            return false;
        }
        String svc = services.get(0);
        if (svc == null) {
            return false;
        }
        if (svc.equals(ApplicationEditionConverter.ApplicationOPCName.APM.toString())) {
            return true;
        }
        return false;
    }

    public static boolean isMonitoringServiceOnly(List<String> services)
    {
        if (services == null || services.size() != 1) {
            return false;
        }
        String svc = services.get(0);
        if (svc == null) {
            return false;
        }
        //TODO update to use ApplicationEditionConverter.ApplicationOPCName once it's updated in tenant sdk
        if ("Monitoring".equals(svc)) {
            return true;
        }
        return false;
    }

    public static void setTestEnv() {
        synchronized (lock) {
            if (IS_TEST_ENV == null) {
                IS_TEST_ENV = true;
            }
        }
    }

    private static void copyTenantSubscriptionInfo(TenantSubscriptionInfo from, TenantSubscriptionInfo to) {
        if (from == null || to == null) {
            LOGGER.error("Cannot copy value into or from null object!");
            return;
        }
        LOGGER.info("Copying TenantSubscriptionInfo data...");
        List<AppsInfo> toAppsInfoList = new ArrayList<AppsInfo>();
        toAppsInfoList.addAll(from.getAppsInfoList());
        to.setAppsInfoList(toAppsInfoList);
    }
    private static boolean editionInfoIntegrityCheck(List<SubscriptionApps> subAppsList) {
        if(!subAppsList.isEmpty()){
            LOGGER.info("Checking edition info's integrity");

            if(subAppsList.get(0).getEditionComponentsList()!=null && !subAppsList.get(0).getEditionComponentsList().isEmpty() ){
                String editionInfo = subAppsList.get(0).getEditionComponentsList().get(0).getEdition();
                if(!StringUtils.isEmpty(editionInfo)){
                    LOGGER.info("Integrity of edition info check passed...");
                    return true;
                }
            }
        }
        LOGGER.warn("Integrity of edition info check failed...");
        return false;
    }

    private TenantSubscriptionUtil() {
    }
}
