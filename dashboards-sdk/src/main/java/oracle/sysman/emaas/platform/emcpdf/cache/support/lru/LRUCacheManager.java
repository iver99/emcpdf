package oracle.sysman.emaas.platform.emcpdf.cache.support.lru;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.support.AbstractCacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.CacheConfig;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheSAXParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * Created by chehao on 2016/12/22.
 */
public class LRUCacheManager extends AbstractCacheManager{

    private static final Logger LOGGER = LogManager.getLogger(LRUCacheManager.class);
    private static LRUCacheManager instance = null;
    private static final String DEFAULT_CONIFIGURATION_FILE_NAME = "cache-config.xml";

    private LRUCacheManager(String configFileName) {
        LOGGER.info("Initializing Cache Manager with custom configuration file named {}...", configFileName);
        init(configFileName);
    }

    public synchronized static LRUCacheManager getInstance(){
        if(instance == null){
             instance = new LRUCacheManager(null);
        }
        return instance;
    }

    public synchronized static LRUCacheManager getInstance(String configFileName){
        if(instance == null){
            instance = new LRUCacheManager(configFileName);
        }
        return instance;
    }
    @Override
    public ICache createNewCache(String name, Integer capacity, Long timeToLive) {
        ICache<Object,Object> cache =new LinkedHashMapCache(name,capacity,timeToLive);
        return cache;
    }
    @Override
    public ICache createNewCache(String name){
      return this.createNewCache(name, CacheConstants.DEFAULT_CAPACITY,CacheConstants.DEFAULT_EXPIRATION);
    }

    /**
     * Return a collection of the cache names known by this manager.
     *
     * @return the names of all caches known by the cache manager
     */
    @Override
    public void init(String configFileName) {
        super.init(configFileName);
        //init default cache group
        LOGGER.info("Initialing LRU CacheManager...");
        //parse cache config
        parseCacheConfig(configFileName);
        LOGGER.info("cache config size "+CacheConfig.cacheConfigList.size());
        for(CacheConfig cacheConfig : CacheConfig.cacheConfigList){
            getCache(cacheConfig.getName(), cacheConfig.getCapacity(), cacheConfig.getExpiry());
        }
    }

    private void parseCacheConfig(String configFileName){
        SAXParserFactory factory = SAXParserFactory.newInstance();
        String fileName = configFileName == null ? DEFAULT_CONIFIGURATION_FILE_NAME : configFileName;
        LOGGER.info("Detected cache Configuration file name is {}", configFileName);
        try {
            SAXParser parser = factory.newSAXParser();
            InputStream f = LRUCacheManager.class.getClassLoader().getResourceAsStream(fileName);
            CacheSAXParser dh = new CacheSAXParser();
            parser.parse(f, dh);
        } catch (ParserConfigurationException e) {
            LOGGER.error(e);
        } catch (SAXException e) {
            LOGGER.error(e);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        LOGGER.info("LRU CacheManager is closing...");
        super.close();
    }
}
