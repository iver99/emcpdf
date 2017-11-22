package oracle.sysman.emaas.platform.dashboards.ui.web.additionaldata;

import oracle.sysman.emaas.platform.dashboards.ui.web.AdditionalDataFilter;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by guochen on 2/10/17.
 */
public class HtmlFragmentCache {
    private final static Logger LOGGER = LogManager.getLogger(HtmlFragmentCache.class);

    public static class CachedHtml {
        private String beforeLangAttrPart;
        private String beforeAdditionalDataPart;
        private String afterAdditionalDataPart;

        public String getBeforeLangAttrPart(){
            return beforeLangAttrPart;
        }
        public String getBeforeAdditionalDataPart() {
            return beforeAdditionalDataPart;
        }
        public String getAfterAdditionalDataPart() {
            return afterAdditionalDataPart;
        }

        public synchronized boolean isCached() {
            return !StringUtil.isEmpty(beforeLangAttrPart) && !StringUtil.isEmpty(beforeAdditionalDataPart) && !StringUtil.isEmpty(afterAdditionalDataPart);
        }

        public synchronized void cacheHtmlFragments(String beforeLangAttrPart, String beforeAdditionalDataPart, String afterAdditionalDataPart) {
            this.beforeLangAttrPart = beforeLangAttrPart;
            this.beforeAdditionalDataPart = beforeAdditionalDataPart;
            this.afterAdditionalDataPart = afterAdditionalDataPart;
        }
    }

    private CachedHtml welcomePageCache = null;
    private CachedHtml homePageCache = null;
    private CachedHtml builderPageCache = null;
    private CachedHtml errorPageCache = null;
    private CachedHtml testPageCache = null;
    private Map<String, CachedHtml> urlToCacheMap = null;

    private static final HtmlFragmentCache instance = new HtmlFragmentCache();

    private HtmlFragmentCache() {
        welcomePageCache = new CachedHtml();
        homePageCache = new CachedHtml();
        builderPageCache = new CachedHtml();
        errorPageCache = new CachedHtml();
        testPageCache = new CachedHtml();
        urlToCacheMap = new ConcurrentHashMap<String, CachedHtml>();
        urlToCacheMap.put(AdditionalDataFilter.HOME_URI, homePageCache);
        urlToCacheMap.put(AdditionalDataFilter.WELCOME_URI, welcomePageCache);
        urlToCacheMap.put(AdditionalDataFilter.BUILDER_URI, builderPageCache);
        urlToCacheMap.put(AdditionalDataFilter.ERROR_URI, errorPageCache);
        urlToCacheMap.put(AdditionalDataFilter.TEST_URI, testPageCache);
        LOGGER.info("URL for dashboard page fragment caching is initialized");
    }

    public static HtmlFragmentCache getInstance() {
        return instance;
    }

    public boolean isHtmlFragmentElementsCached(String uri) {
        if (StringUtil.isEmpty(uri)) {
            LOGGER.error("Get an empty url when checking if fragment element is cached or not");
            return false;
        }
        CachedHtml pageCache = urlToCacheMap.get(uri.toLowerCase());
        if (pageCache == null) {
            LOGGER.error("Didn't find the cached url map for url {}", uri);
            return false;
        }
        return pageCache.isCached();
    }

    public CachedHtml getCachedElementsForRequest(String uri) {
        if (StringUtil.isEmpty(uri))
            return null;
        return urlToCacheMap.get(uri.toLowerCase());
    }

    public void cacheElementsForRequest(String uri, String beforeLangPart, String beforeAdditionalDataPart, String afterAdditionalDataPart) {
        if (StringUtil.isEmpty(uri))
            return;
        CachedHtml pageCache = urlToCacheMap.get(uri.toLowerCase());
        if (pageCache == null) {
            LOGGER.error("Didn't find the cached url map for url {}", uri);
            return;
        }
        if (StringUtil.isEmpty(beforeLangPart) || StringUtil.isEmpty(beforeAdditionalDataPart) || StringUtil.isEmpty(afterAdditionalDataPart)) {
            LOGGER.warn("Any of the content to be cached is empty, so avoid caching them: beforelangpart is {}, beforeadditionalDataPart is {}, afterAdditionalDataPart is {}", beforeLangPart, beforeAdditionalDataPart, afterAdditionalDataPart);
            return;
        }
        pageCache.cacheHtmlFragments(beforeLangPart, beforeAdditionalDataPart, afterAdditionalDataPart);
        LOGGER.debug("Cached html framements for url {}, beforelangpart is {}, beforeadditionalDataPart is {}, afterAdditionalDataPart is {}",
                uri, beforeLangPart, beforeAdditionalDataPart, afterAdditionalDataPart);
    }
}
