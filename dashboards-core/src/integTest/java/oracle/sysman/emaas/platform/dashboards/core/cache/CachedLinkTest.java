package oracle.sysman.emaas.platform.dashboards.core.cache;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil.VersionedLink;
@Test(groups = { "s2" })
public class CachedLinkTest
{
	private final static String testHref = "test href";
	private final static String testRel = "test rel";
	private VersionedLink link;

	@BeforeMethod
	public void beforeMethod()
	{
		link = new VersionedLink();
		link.withHref(testHref);
		link.withRel(testRel);
	}

	@Test
	public void testCachedLink()
	{
		CachedLink cl = new CachedLink(link);
		Assert.assertEquals(cl.getHref(), testHref);
		Assert.assertEquals(cl.getRel(), testRel);

		Link testLink = cl.getLink();
		Assert.assertEquals(testLink.getHref(), link.getHref());
		Assert.assertEquals(testLink.getRel(), link.getRel());
	}
	@Test
	public void testGetLink(){
		CachedLink cl = new CachedLink(link);
		cl.getLink();
	}
}
