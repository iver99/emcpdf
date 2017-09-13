package oracle.sysman.emaas.platform.emcpdf.cache.support;

import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.lru.LRUCacheManager;

/**
 * Created by chehao on 2016/12/27.
 */
public class CacheManagers {


    private static CacheManagers instance=new CacheManagers();

    private CacheManagers() {
    }

    public static CacheManagers getInstance(){
        return instance;
    }

    public ICacheManager build(){
        return LRUCacheManager.getInstance();
    }

    public ICacheManager build(String configFileName){
        return LRUCacheManager.getInstance(configFileName);
    }

}
