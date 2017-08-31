package oracle.sysman.emaas.platform.emcpdf.cache.support;

import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.lru.LinkedHashMapCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by chehao on 2016/12/22.
 */
public abstract class AbstractCacheManager implements ICacheManager{
    private final ConcurrentMap<String, ICache> cacheMap = new ConcurrentHashMap<>(16);
    private final Logger LOGGER= LogManager.getLogger(AbstractCacheManager.class);
    /**
     * Return the cache associated with the given name.
     *
     * @param name
     * @return
     */
    @Override
    public ICache getCache(String name) {
        return getCache(name,null,null);
    }
    @Override
    public ICache getCache(String name, Integer capacity, Long timeToLive){
        ICache cache = this.cacheMap.get(name);
        if (cache != null) {
            LOGGER.debug("getCache got a non-null cache for name {}, capacity {} and timeToLive {}", name, capacity, timeToLive);
            return cache;
        }
        else {
            //create a new cache if cache is not existing.
            synchronized (this.cacheMap) {
                cache = this.cacheMap.get(name);
                if (cache == null) {
                    if(capacity == null && timeToLive == null){
                        LOGGER.debug("Creating a cache for name {}, capacity and timeToLive with default size.", name, capacity, timeToLive);
                        cache = createNewCache(name);
                    }else{
                        LOGGER.debug("Creating a cache for name {}, capacity {} and timeToLive {}.", name, capacity, timeToLive);
                        cache = createNewCache(name,capacity,timeToLive);
                    }
                    if (cache != null) {
                        LOGGER.debug("AbstractCacheManager.getCache: after creating a cache for name {}, capacity {} and timeToLive {}, put it to cacheMap", name, capacity, timeToLive);
                        this.cacheMap.put(name, cache);
                    }
                }
                return cache;
            }
        }
    }
    @Override
    public abstract ICache createNewCache(String name, Integer capacity, Long timeToLive);

    @Override
    public abstract ICache createNewCache(String name);

    @Override
    public void init(String configFileName){
        //EMPTY
    }

    @Override
    public void close() throws IOException{
        for(ICache cache:this.cacheMap.values()){
            LinkedHashMapCache cacheGroup = (LinkedHashMapCache)cache;
            cacheGroup.getTimer().cancel();
            cacheGroup.getTimer().purge();
            LOGGER.info("Cache group {} timer is cancelled and purged!",cacheGroup.getName());

        }
        this.cacheMap.clear();

    }

    public ConcurrentMap<String, ICache> getCacheMap() {
        return cacheMap;
    }
}
