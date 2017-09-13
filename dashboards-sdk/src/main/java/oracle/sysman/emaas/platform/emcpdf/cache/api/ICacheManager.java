package oracle.sysman.emaas.platform.emcpdf.cache.api;

import java.io.Closeable;
import java.util.Collection;

/**
 * Created by chehao on 2016/12/19.
 */
public interface ICacheManager extends Closeable {

    /**
     * Return the cache associated with the given name.
     * @param name
     * @return
     */
    public ICache getCache(String name);

    public ICache getCache(String name, Integer capacity, Long timeToLive);

    public ICache createNewCache(String name, Integer capacity, Long timeToLive);

    public ICache createNewCache(String name);

    public void init(String configFileName);
}
