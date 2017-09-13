package oracle.sysman.emaas.platform.emcpdf.cache.support;

import oracle.sysman.emaas.platform.emcpdf.cache.api.CacheLoader;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.exception.ExecutionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by chehao on 2016/12/22.
 */
public abstract class AbstractCache implements ICache {
    private static final Logger LOGGER = LogManager.getLogger(AbstractCache.class);

    protected SimpleCacheCounter cacheCounter = new SimpleCacheCounter();

    public SimpleCacheCounter getCacheCounter() {
        return cacheCounter;
    }

    @Override
    public Object get(Object key) throws ExecutionException {
        return get(key, null);
    }

    @Override
    public Object get(Object key, CacheLoader factory) throws ExecutionException {
        LOGGER.debug("AbstractCache.get is called for key {} and CacheLoader {}!", key, factory);
        checkNotNull(key);
        CachedItem value = lookup(key);
        Object valueFromFactory = null;
        cacheCounter.recordRequest(1L);
        if (value != null) {
            if (isExpired(value)) {
                LOGGER.debug("AbstractCache: Evicting cached item key = {} value = {}", key, value);
                evict(key);
            } else {
                LOGGER.debug("AbstractCache.get is called for key {}. Value isnot null or expired, so return the value {}!", key, value.getValue());
                cacheCounter.recordHit(1L);
                LOGGER.debug("AbstractCache: Returning cached item key = {} value = {}", key, value);
                return value.getValue();
            }
        }

        if (factory != null) {
            try {
                valueFromFactory = factory.load(key);
                LOGGER.debug("AbstractCache.get is called for key {}. Value is {}, factory is {}, valueFromFactory is {}", key, value, factory, valueFromFactory);
                if (valueFromFactory != null) {
                    put(key, valueFromFactory);
                }
            } catch (Exception e) {
                LOGGER.error("Error occurred when retrieving data from cache factory!");
                LOGGER.error(e);
                throw new ExecutionException(e);
            }
        }
        LOGGER.debug("AbstractCache: Returning key = {}, value = {} from cache..", key, valueFromFactory);
        return valueFromFactory;
    }

    /*@Override
    public Object refreshAfterGet(final Object key, final CacheLoader factory) throws ExecutionException {
        ScheduledExecutorService pool = CacheThreadPools.getThreadPool();
        LOGGER.info("Refresh after get action begin...");
        pool.schedule(new TimerTask() {
            @Override
            public void run() {
                LOGGER.info("Refresh...");
                Object obj = null;
                try {
                    obj = get(key, factory);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                put(key, new CachedItem(key, obj));
            }
        }, 30, TimeUnit.SECONDS);
        LOGGER.info("Refresh after get action end...");
        return get(key, factory);
    }*/

    @Override
    public void evict(Object key) {
        cacheCounter.recordEviction(1L);
    }

    @Override
    public void clear() {
        LOGGER.info("All Cache status data is reset!");
        cacheCounter.reset();
    }

    protected abstract CachedItem lookup(Object key);

    private <T> T checkNotNull(T reference) {
        if (reference == null) {
            LOGGER.error("Null Pointer Exception occurred!");
            throw new NullPointerException();
        }
        return reference;
    }

    public interface CacheCounter {

        void recordHit(long count);
        void recordRequest(long count);

        void recordEviction(long count);

        String getHitRate();

        void reset();

    }
    //A simple cache counter

    /**
     * Attention: This cache counter is not synchronized, so there might be deviation of the statistic data.
     */
    public class SimpleCacheCounter implements CacheCounter {

        private volatile long hitCount;

        private volatile long requestCount;
        private volatile long evictionCount;
        public SimpleCacheCounter() {
            hitCount = 0L;
            requestCount = 0L;
            evictionCount = 0L;
        }

        @Override
        public void recordHit(long count) {
            hitCount += count;
        }

        @Override
        public void recordRequest(long count) {
            requestCount += count;
        }

        @Override
        public void recordEviction(long count) {
            evictionCount += count;
        }

        @Override
        public String getHitRate() {
            if (hitCount == 0L || requestCount == 0L) {
                return "0%";
            }
            if (hitCount > requestCount) {
                return "100.00%";
            }
            Double rate = Double.valueOf(hitCount) / Double.valueOf(requestCount);
            rate *= 100;
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(rate) + "%";
        }

        @Override
        public void reset() {
            hitCount = 0L;
            requestCount = 0L;
            evictionCount = 0L;
        }

        public long getHitCount() {
            return hitCount;
        }

        public void setHitCount(long hitCount) {
            this.hitCount = hitCount;
        }

        public long getRequestCount() {
            return requestCount;
        }

        public void setRequestCount(long requestCount) {
            this.requestCount = requestCount;
        }

        public long getEvictionCount() {
            return evictionCount;
        }

        public void setEvictionCount(long evictionCount) {
            this.evictionCount = evictionCount;
        }

    }

}
