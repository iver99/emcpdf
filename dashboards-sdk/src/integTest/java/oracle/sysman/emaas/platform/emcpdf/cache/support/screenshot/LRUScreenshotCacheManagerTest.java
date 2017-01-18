package oracle.sysman.emaas.platform.emcpdf.cache.support.screenshot;

import oracle.sysman.emaas.platform.emcpdf.cache.api.CacheLoader;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.exception.ExecutionException;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CachedItem;
import oracle.sysman.emaas.platform.emcpdf.cache.support.lru.LRUCacheManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by chehao on 2017/1/6.
 */
@Test(groups = { "s2" })
public class LRUScreenshotCacheManagerTest {

    ICacheManager cm;
    @BeforeClass
    public void setUp(){
        cm=LRUScreenshotCacheManager.getInstance();
    }

    @Test
    public void testGetInstance(){
        ICacheManager cm= LRUScreenshotCacheManager.getInstance();
    }
    @Test
    public void testGetCache(){
        cm.getCache("cache1");
    }
    @Test
    public void testGetCachedItem1() throws ExecutionException {
        Object o1=cm.getCache("cache1").get("two");
        Assert.assertNull(o1);
        cm.getCache("cache1").put("two",new CachedItem("two",2));
        Object o2=cm.getCache("cache1").get("two");
        Assert.assertEquals((Integer)(((CachedItem)o2).getValue()),new Integer(2));
    }
    @Test
    public void testGetCachedItem2() throws ExecutionException {
        Object o1=cm.getCache("cache1").get("factoryFetch", new CacheLoader() {
            @Override
            public Object load(Object key) throws Exception {
                return "FromFactory";
            }
        });

        Assert.assertEquals(o1.toString(),"FromFactory");
    }
    @Test
    public void testEviction() throws ExecutionException {
        cm.getCache("cache1");
        Assert.assertNull(cm.getCache("cache1").get("three"));
        cm.getCache("cache1").put("three",new CachedItem("three",3));
        Assert.assertNotNull(cm.getCache("cache1").get("three"));
        cm.getCache("cache1").evict("three");
        Assert.assertNull(cm.getCache("cache1").get("three"));
    }
    @Test
    public void testExpiration() throws ExecutionException {
        ICacheManager cm=LRUCacheManager.getInstance();
        cm.getCache("cache2",1000,2L);
        cm.getCache("cache2").put("four",new CachedItem("four",4));
        Assert.assertNotNull(cm.getCache("cache2").get("four"));
        try {
            Thread.currentThread().sleep(2100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertNull(cm.getCache("cache2").get("four"));

    }
    @Test
    public void testClear(){
        cm.getCache("cache1").clear();
    }
    @Test
    public void testCreateCache1(){
        cm.createNewCache("createCache1");
    }
    @Test
    public void testCreateCache2(){
        cm.createNewCache("createCache1",1000,1000L);
    }

    @Test
    public void testInit(){
        cm.init();
    }
    @Test
    public void testClose(){
        try {
            cm.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}