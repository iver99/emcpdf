package oracle.sysman.emaas.platform.dashboards.core.cache.lru;


import java.util.logging.Logger;

import oracle.sysman.emaas.platform.dashboards.core.cache.lru.CacheUnit;
import oracle.sysman.emaas.platform.dashboards.core.cache.lru.Element;

import org.testng.Assert;
import org.testng.annotations.ExpectedExceptions;
import org.testng.annotations.Test;
/**
 * @author chendonghao
 *
 */
@SuppressWarnings("all")
public class CacheUnitTest {
	
	public static final Logger LOGGER=Logger.getLogger(CacheUnitTest.class.getName());

	/**
	 * test put action begins
	 */

	@Test(groups = { "s2" },dataProvider="default_cache_unit",dataProviderClass=DataProviderClass.class)
	public void testPutCache(CacheUnit cacheUnit){
		cacheUnit.put("1", new Element("1","one"));
		Assert.assertNotNull(cacheUnit.get("1"));
	}
	
	@Test(groups = { "s2" },dataProvider="default_cache_unit",dataProviderClass=DataProviderClass.class)
	public void testPutWhenEmpty(CacheUnit cacheUnit){
		cacheUnit.put("1", new Element("1","One"));
	}
	
	@Test(groups = { "s2" },dataProvider="default_cache_unit_three_elements",dataProviderClass=DataProviderClass.class)
	public void testPutWhenNotEmpty(CacheUnit cacheUnit){
		cacheUnit.put("1", new Element("1","second one"));
	}
	
	@Test(groups = { "s2" },dataProvider="default_cache_unit_three_elements",dataProviderClass=DataProviderClass.class)
	public void testPutIfAbsent(CacheUnit cacheUnit){
		cacheUnit.put("4", new Element("4","four"));
	}
	
	@Test(groups = { "s2" },dataProvider="default_cache_unit",dataProviderClass=DataProviderClass.class)
	@ExpectedExceptions(value = { IllegalArgumentException.class })
	public void testPutNullKey(CacheUnit cacheUnit){
		cacheUnit.put(null, new Element("1","one"));
	}
	@ExpectedExceptions(value = { IllegalArgumentException.class })
	@Test(groups = { "s2" },dataProvider="default_cache_unit",dataProviderClass=DataProviderClass.class)
	public void testPutNullValue(CacheUnit cacheUnit){
		cacheUnit.put("1", null);
	}
	
	/**
	 * test get action begins 
	 */
	
	@Test(groups = { "s2" })
	public void testGetCache(){
		CacheUnit cacheUnit=new CacheUnit();
		cacheUnit.put("1", new Element("1","one"));
		cacheUnit.put("2", new Element("2", "two"));
		cacheUnit.put("3", new Element("3", "three"));
		Assert.assertEquals(cacheUnit.get("2"), "two");
		Assert.assertEquals(cacheUnit.get("1"), "one");
		Assert.assertEquals(cacheUnit.get("3"), "three");
		Assert.assertEquals(cacheUnit.get("4"), null);
	}
	@Test(groups = { "s2" },dataProvider="default_cache_unit",dataProviderClass=DataProviderClass.class)
	public void testGetWhenEmpty(CacheUnit cacheUnit){
		Assert.assertEquals(cacheUnit.isEmpty(), true);
		Assert.assertEquals(cacheUnit.get("1"), null);
	}
	@Test(groups = { "s2" })
	public void testGetWhenNotEmpty(){
		CacheUnit cu=new CacheUnit();
		cu.put("1", new Element("1","one"));
		cu.put("2", new Element("2", "two"));
		cu.put("3", new Element("3", "three"));
		Assert.assertEquals(cu.isEmpty(), false);
		Assert.assertNotEquals(cu.get("1"),null);
	}
	
	/**
	 *	test remove action begins 
	 */

	@Test(groups = { "s2" })
	public void testRemoveCache(){
		CacheUnit cu=new CacheUnit();
		cu.put("1", new Element("1","one"));
		cu.put("2", new Element("2", "two"));
		cu.put("3", new Element("3", "three"));
		Assert.assertNotEquals(cu.get("2"), null);
		cu.remove("2");
		Assert.assertEquals(cu.get("2"), null);
		Assert.assertNotEquals(cu.get("1"), null);
	}
	@Test(groups = { "s2" },dataProvider="default_cache_unit",dataProviderClass=DataProviderClass.class)
	public void testRemoveWhenEmpty(CacheUnit cacheUnit){
		Assert.assertEquals(cacheUnit.isEmpty(), true);
		Assert.assertEquals(cacheUnit.remove("1"), false);
	}
	@Test(groups = { "s2" })
	public void testRemoveWhenNotEmpty(){
		CacheUnit cu=new CacheUnit();
		cu.put("1", new Element("1","one"));
		cu.put("2", new Element("2", "two"));
		cu.put("3", new Element("3", "three"));
		Assert.assertEquals(cu.isEmpty(), false);
		Assert.assertEquals(cu.remove("1"), true);
	}
	@Test(groups = { "s2" })
	public void testRemoveWhenExists(){
		CacheUnit cu=new CacheUnit();
		cu.put("1", new Element("1","one"));
		cu.put("2", new Element("2", "two"));
		cu.put("3", new Element("3", "three"));
		Assert.assertEquals(cu.remove("1"), true);
	}
	@Test(groups = { "s2" },dataProvider="default_cache_unit_three_elements",dataProviderClass=DataProviderClass.class)
	public void testRemoveWhenNotExists(CacheUnit cacheUnit){
		Assert.assertEquals(cacheUnit.remove("4"), false);
	}
	/**
	 * 	test clear action begins 
	 */
	@Test(groups = { "s2" },dataProvider="default_cache_unit",dataProviderClass=DataProviderClass.class)
	public void testClearWhenEmpty(CacheUnit cacheUnit){
		cacheUnit.clearCache();
	}
	@Test(groups = { "s2" },dataProvider="default_cache_unit_three_elements",dataProviderClass=DataProviderClass.class)
	public void testClearWhenNotEmpty(CacheUnit cacheUnit){
		cacheUnit.clearCache();
	}
	
	/**
	 *	test get capacity actions begins 
	 */
	
	@Test(groups = { "s2" },dataProvider="default_cache_unit",dataProviderClass=DataProviderClass.class)
	public void testCacheUnitCapacity(CacheUnit cacheUnit){
		Assert.assertEquals(cacheUnit.getCacheCapacity(), CacheUnit.DEFAULT_CACHE_CAPACITY);
	}
	@Test(groups = { "s2" },dataProvider="default_cache_unit",dataProviderClass=DataProviderClass.class)
	public void testCapacityWhenEmpty(CacheUnit cacheUnit){
		cacheUnit.getCacheCapacity();
	}
	@Test(groups = { "s2" },dataProvider="default_cache_unit_three_elements",dataProviderClass=DataProviderClass.class)
	public void testCapacityWhenNotEmpty(CacheUnit cacheUnit){
		cacheUnit.getCacheCapacity();
	}
	
	@Test(groups = { "s2" })
	public void testGetBeforeExpiration(){
		CacheUnit cu=new CacheUnit(2);
		cu.put("1", new Element("1","one"));
		cu.put("2", new Element("2", "two"));
		cu.put("3", new Element("3", "three"));
		Assert.assertNotNull(cu.get("1"));
	}
	@Test(groups = { "s2" },dataProvider="cache_unit_2_elements_2sec",dataProviderClass=DataProviderClass.class)
	public void testGetAfterExpiration(CacheUnit cacheUnit) {
		try {
			Thread.currentThread().sleep(2100);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
	@Test(groups = { "s2" },dataProvider="cache_unit_2_elements_2sec",dataProviderClass=DataProviderClass.class)
	public void testPutBeforeExpiration(CacheUnit cacheUnit) {
		cacheUnit.put("4", new Element("4","four"));
	}
	@Test(groups = { "s2" })
	public void testPutAfterExpiration() {
		CacheUnit cu=new CacheUnit(2);
		cu.put("1", new Element("1","one"));
		cu.put("2", new Element("2", "two"));
		cu.put("3", new Element("3", "three"));
		Assert.assertNotNull(cu.get("1"));
		try {
			Thread.currentThread().sleep(2100);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		Assert.assertNull(cu.get("1"));
	}
	@Test(groups = { "s2" },dataProvider="cache_unit_2_elements_2sec",dataProviderClass=DataProviderClass.class)
	public void testRemoveBeforeExpiration(CacheUnit cacheUnit) {
		cacheUnit.remove("1");
	}
	@Test(groups = { "s2" },dataProvider="cache_unit_2_elements_2sec",dataProviderClass=DataProviderClass.class)
	public void testClearBeforeExpiration(CacheUnit cacheUnit) {
		cacheUnit.clearCache();
	}
	@Test(groups = { "s2" },dataProvider="cache_unit_2_elements_2sec",dataProviderClass=DataProviderClass.class)
	public void testClearAfterExpiration(CacheUnit cacheUnit) {
		try {
			Thread.currentThread().sleep(2100);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		cacheUnit.clearCache();
	}
	
	
}
