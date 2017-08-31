package oracle.sysman.emaas.platform.emcpdf.cache.support.lru;

import java.io.IOException;
import java.util.*;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.emcpdf.cache.api.CacheLoader;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.exception.ExecutionException;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CacheManagers;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CachedItem;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.CacheConfig;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.DefaultKeyGenerator;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Keys;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Tenant;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by chehao on 2016/12/23.
 */
@Test(groups = { "s2" })
public class LRUCacheManagerTest {

    @Test
    public void testGetInstance(){
        ICacheManager cm= LRUCacheManager.getInstance();
    }
    @Test
    public void testGetCache(){
        ICacheManager cm=LRUCacheManager.getInstance();
        cm.getCache("cache1");
    }
    @Test
    public void testGetCachedItem1() throws ExecutionException {
        ICacheManager cm=LRUCacheManager.getInstance();
        Object o1=cm.getCache("cache1").get("two");
        Assert.assertNull(o1);
        cm.getCache("cache1").put("two",new CachedItem("two",2));
        Object o2=cm.getCache("cache1").get("two");
        Assert.assertEquals((Integer)(((CachedItem)o2).getValue()),new Integer(2));
    }
    @Test
    public void testGetCachedItem2() throws ExecutionException {
        ICacheManager cm=LRUCacheManager.getInstance();
        Object o1=cm.getCache("cache1").get("factoryFetch", new CacheLoader() {
            @Override
            public Object load(Object key) throws Exception {
                return "FromFactory";
            }
        });

        Assert.assertEquals(o1.toString(),"FromFactory");
    }

    /**
     * EMCPDF-3116
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testGetCachedItem3() throws ExecutionException, InterruptedException {
        ICacheManager cm=LRUCacheManager.getInstance();
        ICache cache = cm.getCache("testCache1",100,2L);
        Tenant tenant=new Tenant("tenant1");
        Object key = DefaultKeyGenerator.getInstance().generate(tenant,new Keys("keys1"));
        TestCache tc1=(TestCache) cache.get(key, new CacheLoader() {
            @Override
            public Object load(Object key) throws Exception {
                return new TestCache();
            }
        });
        TestCache tc2 = (TestCache) cache.get(key, new CacheLoader() {
            @Override
            public Object load(Object key) throws Exception {
                return new TestCache();
            }
        });
        Assert.assertEquals(tc1,tc2);
        // make cachedItem expired
        Thread.currentThread().sleep(2100);

        TestCache tc3 = (TestCache) cache.get(key, new CacheLoader() {
            @Override
            public Object load(Object key) throws Exception {
                return new TestCache();
            }
        });
        Assert.assertNotEquals(tc1,tc3);
    }
    //Test Inner Class
    public class TestCache{

    }

    @Test
    public void testEviction() throws ExecutionException {
        ICacheManager cm=LRUCacheManager.getInstance();
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
        ICacheManager cm=LRUCacheManager.getInstance();
        cm.getCache("cache1").clear();
    }
    @Test
    public void testCreateCache1(){
        ICacheManager cm=LRUCacheManager.getInstance();
        cm.createNewCache("createCache1");
    }
    @Test
    public void testCreateCache2(){
        ICacheManager cm=LRUCacheManager.getInstance();
        cm.createNewCache("createCache1",1000,1000L);
    }

    @Test
    public void testInit(@Mocked final ICacheManager cacheManager){
    	List<CacheConfig> list = new ArrayList<CacheConfig>();
    	CacheConfig config = new CacheConfig();
    	config.setName("test");
    	config.setExpiry(1L);
    	config.setCapacity(1);
    	list.add(config);
    	Deencapsulation.setField(CacheConfig.class, "cacheConfigList", list);
        CacheManagers.getInstance().build().init(null);
    }
    @Test
    public void testClose(){
        ICacheManager cm=LRUCacheManager.getInstance();
        try {
            cm.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testUnModifiableCollections1(){
        List list = new ArrayList();
        list.add(new Object());
        List immutableList = Collections.unmodifiableList(list);
        immutableList.add(new Object());

    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testUnModifiableCollections2(){
        Set set = new HashSet<>();
        set.add(new Object());
        Set immutableSet = Collections.unmodifiableSet(set);

        immutableSet.add(new Object());

    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testUnModifiableCollections3(){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("test",new Object());
        Map immutableMap = Collections.unmodifiableMap(map);
        Assert.assertNotNull(immutableMap.get("test"));
        immutableMap.put("test",new Object());
    }
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testUnModifiableCache1() throws ExecutionException {
        ICacheManager cm=LRUCacheManager.getInstance();
        ICache cache = cm.getCache("testCache1",100,0L);
        Tenant tenant=new Tenant("tenant1");
        Object key = DefaultKeyGenerator.getInstance().generate(tenant,new Keys("keys1"));
        List<String> cachedList = new ArrayList<>();
        cachedList.add("one");
        cachedList.add("two");
        cache.put(key, cachedList);
        List newList = (List) cache.get(key);
        Assert.assertNotNull(newList);
        Assert.assertEquals(2, newList.size());
        newList.add("tree");//UnsupportedOperationException threw out
        Assert.assertEquals(2, newList.size());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testUnModifiableCache2() throws ExecutionException {
        ICacheManager cm=LRUCacheManager.getInstance();
        ICache cache = cm.getCache("testCache2",100,0L);
        Tenant tenant=new Tenant("tenant2");
        Object key = DefaultKeyGenerator.getInstance().generate(tenant,new Keys("keys2"));
        Map<String,Object> cachedMap = new HashMap<>();
        cachedMap.put("one", new Object());
        cachedMap.put("two", new Object());
        cache.put(key, cachedMap);
        Map newMap = (Map) cache.get(key);
        Assert.assertNotNull(newMap);
        Assert.assertEquals(2, newMap.size());
        newMap.put("tree", new Object());//UnsupportedOperationException threw out
        Assert.assertEquals(2, newMap.size());
    }


}
