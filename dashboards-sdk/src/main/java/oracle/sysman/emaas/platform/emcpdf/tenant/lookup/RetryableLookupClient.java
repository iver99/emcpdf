package oracle.sysman.emaas.platform.emcpdf.tenant.lookup;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceQuery;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emaas.platform.emcpdf.cache.util.StringUtil;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by guochen on 3/8/17.
 */
public class RetryableLookupClient<T> {
    private static final Logger LOGGER = LogManager.getLogger(RetryableLookupClient.class);


    public static class RetryableLookupException extends Exception {
        public RetryableLookupException(Exception e) {
            super(e);
        }
    }

    public static abstract class RetryableRunner<T> {
        public abstract T runWithLink(RegistryLookupUtil.VersionedLink lk) throws Exception;
    }

    public T connectAndDoWithRetry(String serviceName, String version, String rel, boolean prefixMatch, String tenantName, RetryableRunner<T> runner) {
        if (runner == null) {
            LOGGER.error("Null runner to retrieve service internal link for service: \"{}\", version: \"{}\", rel: \"{}\", prefixMatch: \"{}\", tenant: \"{}\"",
                    serviceName, version, rel, prefixMatch, tenantName);
            return null;
        }

        // here is the logic for doing somework
        int retry = 0;
        //int retry_on_same = 0;
        //as discussed in review meeting, we will retry for ~(2+4+8+16+32+64)s
        // above comment is deprecated due to EMCPDF-4447 change max retry to 4 times(include 1st request, and 3 retry.)
        int MAX_TOTAL_RETRY = 4;
//        int MAX_RETRY_ON_SAME_INSTANCE = 10; // 10 exponential retries will get us close to 6 minutes
        double delaySecs = 2;

//        InstanceInfo used = null;
//        LogUtil.setInteractionLogThreadContext(tenantName, "Retristry lookup client", LogUtil.InteractionLogDirection.OUT);
        RegistryLookupUtil.VersionedLink lk = null;

        Random delayRand = new Random(System.currentTimeMillis());
        while ((retry++) < MAX_TOTAL_RETRY /*&& (retry_on_same < MAX_RETRY_ON_SAME_INSTANCE)*/) {
            if(retry == 1){
                LOGGER.debug("First time to request.");
            }else{
                LOGGER.info("Retry for the {} time", retry - 1);
            }

            try {
                List<InstanceInfo> result = getServiceInstances(serviceName, version, tenantName);
                if (result != null && !result.isEmpty()) {
                    // [EMCPDF-733] Rest client can't handle https currently, so http protocol is enough for internal use
                    //https link is not found, then find http link
                    for (InstanceInfo internalInstance : result) {
                        List<Link> links = null;
                        if (prefixMatch) {
                            links = internalInstance.getLinksWithRelPrefixWithProtocol(rel, "http");
                        } else {
                            links = internalInstance.getLinksWithProtocol(rel, "http");
                        }
                        if (version == null) {
                            version = internalInstance.getVersion();
                        }
                        if (links != null && !links.isEmpty()) {
                            lk = new RegistryLookupUtil.VersionedLink(links.get(0), version, RegistryLookupUtil.getAuthorizationAccessToken(internalInstance));
//                            itrLogger.debug("Retrieved link {}", lk == null ? null : lk.getHref());
                            break;
                        }
                    }
                }
                if (lk == null) {
                    LOGGER.error("Retrieved null link for service {}, version {}, rel {}, prefixMatch {}, tenant {}. Will retry after delay",
                            serviceName, version, rel, prefixMatch, tenantName); // log the exception and go ahead to retry
                } else {
                    T rtn = runner.runWithLink(lk); // this method may raise RetryableLookupException (for retry case) or other exception that'll stop the retry procedure
                    LOGGER.info("Successful done the work for link {}, complete the procedure", lk.getHref());
                    return rtn;
                }
            } catch (RetryableLookupException e) {
                LOGGER.warn(e); // log the exception and go ahead to retry
            } catch (Exception e) {
                LOGGER.error(e);
                return null;
            }

            // Delay between retries grows over time
            LOGGER.info("Waiting for {}s before next retry", delaySecs);
            try {
                Thread.currentThread().sleep((long) (delaySecs * 1000));
            } catch (InterruptedException ie) {
                LOGGER.error("Interrupted Exception get thrown during retry logic...");
                LOGGER.error(ie);
            }
            LOGGER.info("After waiting, will retry or abort the retry procedure");
            delaySecs = delaySecs * 2 * (0.9 + delayRand.nextDouble() * 0.2); // Grow by 1.8 to 2.2 each time
        }
        LOGGER.info("Exceeds the maxmium retry times. Stop retry logic");
        return null;
    }

    private List<InstanceInfo> getServiceInstances(String serviceName, String version, String tenantName) throws Exception {
        List<InstanceInfo> result = null;
        InstanceInfo info = InstanceInfo.Builder.newBuilder().withServiceName(serviceName).withVersion(version).build();
        if (!StringUtil.isEmpty(tenantName)) {
            InstanceInfo ins = LookupManager.getInstance().getLookupClient().getInstanceForTenant(info, tenantName);
            if (ins == null) {
                LOGGER.error(
                        "Error: retrieved null instance info with getInstanceForTenant. Details: serviceName={}, version={}, tenantName={}",
                        serviceName, version, tenantName);
            } else {
                result = new ArrayList<InstanceInfo>();
                result.add(ins);
            }

        } else {
            result = LookupManager.getInstance().getLookupClient().lookup(new InstanceQuery(info));
            if (result == null || result.isEmpty()) {
                LOGGER.error("Retrieve null or empty instance list from lookup client lookup method for tenant {}, service {}, version {}"
                        , tenantName, serviceName, version);
            }
        }
        return result;
    }
}
