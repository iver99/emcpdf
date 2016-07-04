package oracle.sysman.emaas.platform.dashboards.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mockit.Deencapsulation;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo.Builder;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceQuery;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.SanitizedInstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;

import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegistryLookupUtilTest
{
	private static final String END_POINT_HTTP = "http://sample.endpoint.com";
	private static final String END_POINT_HTTPS = "https://sample.endpoint.com";

	@Test(groups = { "s2" })
	public void testGetLinksWithRelPrefix_S2()
	{
		final List<Link> links = new ArrayList<Link>();
		Link lk1 = new Link();
		lk1.withRel("Test1");
		links.add(lk1);
		Link lk2 = new Link();
		lk2.withRel("Others 2");
		links.add(lk2);
		Link lk3 = new Link();
		lk3.withRel("Test 3");
		links.add(lk3);
		SanitizedInstanceInfo sii = new MockUp<SanitizedInstanceInfo>() {
			@Mock
			List<Link> getLinks()
			{
				return links;
			}
		}.getMockInstance();

		List<Link> rtn = RegistryLookupUtil.getLinksWithRelPrefix("Test", sii);
		Assert.assertEquals(rtn.size(), 2);
		Assert.assertTrue(rtn.contains(lk1));
		Assert.assertFalse(rtn.contains(lk2));
		Assert.assertTrue(rtn.contains(lk3));
	}

	@Test(groups = { "s2" })
	public void testGetServiceExternalEndPoint_ExceptionOccurred_S2(@Mocked final Builder anyBuilder,
			@Mocked final InstanceInfo anyInstanceInfo, @Mocked final LookupManager anyLockupManager,
			@Mocked final LookupClient anyClient, @Injectable final Logger anyLogger) throws Exception
	{
		Logger logger = (Logger) Deencapsulation.getField(RegistryLookupUtil.class, "logger");
		Deencapsulation.setField(RegistryLookupUtil.class, "logger", anyLogger);
		try {
			new Expectations() {
				{
					InstanceInfo.Builder.newBuilder().withServiceName(anyString).withVersion(anyString).build();
					result = anyInstanceInfo;
					anyClient.getInstanceForTenant(anyInstanceInfo, anyString);
					result = new Exception();
					anyLogger.error(anyString, (Throwable) any);
				}
			};
			// test scenario for exception
			String endpoint = RegistryLookupUtil.getServiceExternalEndPoint("test_service", "test_version", "test_tenant");
			Assert.assertNull(endpoint);
		}
		finally {
			Deencapsulation.setField(RegistryLookupUtil.class, "logger", logger);
		}
	}

	@Test(groups = { "s2" })
	public void testGetServiceExternalEndPoint_S2(@Mocked final Builder anyBuilder, @Mocked final InstanceInfo anyInstanceInfo,
			@Mocked final LookupManager anyLockupManager, @Mocked final LookupClient anyClient) throws Exception
	{
		new Expectations() {
			{
				InstanceInfo.Builder.newBuilder().withServiceName(anyString).withVersion(anyString).build();
				result = anyInstanceInfo;
				LookupManager.getInstance().getLookupClient().getInstance(anyInstanceInfo);
				result = anyInstanceInfo;
				anyClient.getSanitizedInstanceInfo((InstanceInfo) any);
				result = new Delegate<SanitizedInstanceInfo>() {
					@SuppressWarnings("unused")
					SanitizedInstanceInfo getSanitizedInstanceInfo(InstanceInfo instanceInfo)
					{
						return new SanitizedInstanceInfo() {
							@Override
							public List<String> getCanonicalEndpoints()
							{
								return Arrays.asList(END_POINT_HTTP);
							}

							@Override
							public List<String> getVirtualEndpoints()
							{
								return Arrays.asList(END_POINT_HTTPS);
							}
						};
					}
				};
			}
		};
		// test scenario without tenant
		String endpoint = RegistryLookupUtil.getServiceExternalEndPoint("test_service", "test_version", null);
		// https is returned if it exists
		Assert.assertEquals(endpoint, END_POINT_HTTPS);

		new Expectations() {
			{
				anyClient.getInstanceForTenant(anyInstanceInfo, anyString);
				result = anyInstanceInfo;
				anyClient.getSanitizedInstanceInfo((InstanceInfo) any);
				result = new Delegate<SanitizedInstanceInfo>() {
					@SuppressWarnings("unused")
					SanitizedInstanceInfo getSanitizedInstanceInfo(InstanceInfo instanceInfo)
					{
						return new SanitizedInstanceInfo() {
							@Override
							public List<String> getCanonicalEndpoints()
							{
								return Arrays.asList(END_POINT_HTTP);
							}

							@Override
							public List<String> getVirtualEndpoints()
							{
								return Arrays.asList(END_POINT_HTTP);
							}
						};
					}
				};
			}
		};
		// test scenario with tenant
		endpoint = RegistryLookupUtil.getServiceExternalEndPoint("test_service", "test_version", "test_tenant");
		// http is returned if https does not exist
		Assert.assertEquals(endpoint, END_POINT_HTTP);
	}

	@Test(groups = { "s2" })
	public void testGetServiceExternalEndPointEntity_S2()
	{
		String href = "htt://www.test.com", serviceName = "serviceName", version = "version", tenantName = "tenantName";
		final Link link = new Link();
		link.withHref("htt://www.test.com");
		new Expectations(RegistryLookupUtil.class) {
			{
				RegistryLookupUtil.getServiceExternalLink(anyString, anyString, anyString, anyString);
				result = link;
			}
		};
		EndpointEntity ee = RegistryLookupUtil.getServiceExternalEndPointEntity(serviceName, version, tenantName);
		Assert.assertEquals(ee.getServiceName(), serviceName);
		Assert.assertEquals(ee.getVersion(), version);
		Assert.assertEquals(ee.getHref(), href);
	}

	@Test(groups = { "s2" })
	public void testGetServiceExternalLink_NoTenant_S2(@Mocked final Builder anyBuilder,
			@Mocked final InstanceInfo anyInstanceInfo, @Mocked final InstanceQuery anyInstanceQuery,
			@Mocked final LookupManager anyLockupManager, @Mocked final SanitizedInstanceInfo anySanitizedInfo) throws Exception
	{
		String testHref = "https://den00yse.us.oracle.com:7005/emsaasui/emlacore/html/log-analytics-search.html";
		String testRel = "search";
		final List<Link> links = new ArrayList<Link>();
		final Link lk1 = new Link();
		lk1.withHref(testHref);
		lk1.withRel(testRel);
		links.add(lk1);
		new Expectations() {
			{
				InstanceInfo.Builder.newBuilder().withServiceName(anyString);
				result = anyBuilder;
				anyBuilder.withVersion(anyString);
				result = anyBuilder;
				anyBuilder.build();
				result = anyInstanceInfo;
				new InstanceQuery(anyInstanceInfo);
				result = anyInstanceQuery;
				LookupManager.getInstance().getLookupClient().lookup(anyInstanceQuery);
				result = anyInstanceInfo;
				LookupManager.getInstance().getLookupClient().getSanitizedInstanceInfo((InstanceInfo) any);
				result = anySanitizedInfo;
				anySanitizedInfo.getLinks(anyString);
				result = new Delegate<List<Link>>() {
					@SuppressWarnings("unused")
					List<Link> getLinks(String rel)
					{
						return links;
					}
				};
			}
		};
		Link lk = RegistryLookupUtil.getServiceExternalLink("LoganService", "0.1", "search", null);
		Assert.assertEquals(lk.getHref(), testHref);
		Assert.assertEquals(lk.getRel(), testRel);
	}

	@Test(groups = { "s2" })
	public void testGetServiceExternalLink_S2(@Mocked final Builder anyBuilder, @Mocked final InstanceInfo anyInstanceInfo,
			@Mocked final LookupManager anyLockupManager, @Mocked final SanitizedInstanceInfo anySanitizedInfo) throws Exception
	{
		String testHref = "https://test1.link.com";
		final List<Link> links = new ArrayList<Link>();
		final Link lk1 = new Link();
		lk1.withHref(testHref);
		links.add(lk1);
		new Expectations() {
			{
				InstanceInfo.Builder.newBuilder().withServiceName(anyString);
				result = anyBuilder;
				anyBuilder.withVersion(anyString);
				result = anyBuilder;
				anyBuilder.build();
				result = anyInstanceInfo;
				LookupManager.getInstance().getLookupClient().getInstanceForTenant(anyInstanceInfo, anyString);
				result = anyInstanceInfo;
				LookupManager.getInstance().getLookupClient().getSanitizedInstanceInfo((InstanceInfo) any, anyString);
				result = anySanitizedInfo;
				anySanitizedInfo.getLinks(anyString);
				result = new Delegate<List<Link>>() {
					@SuppressWarnings("unused")
					List<Link> getLinks(String rel)
					{
						return links;
					}
				};
			}
		};
		Link lk = RegistryLookupUtil.getServiceExternalLink("ApmUI", "0.1", "home", "emaastesttenant1");
		Assert.assertEquals(lk.getHref(), testHref);
	}

	@Test(groups = { "s2" })
	public void testGetServiceExternalLinkWithRelPrefix_S2(@Mocked final Builder anyBuilder,
			@Mocked final InstanceInfo anyInstanceInfo, @Mocked final LookupManager anyLockupManager,
			@Mocked final SanitizedInstanceInfo anySanitizedInfo) throws Exception
	{
		String testHref = "http://den00yse.us.oracle.com:7004/emsaasui/emlacore/resources/";
		String testRel = "loganService";
		final List<Link> links = new ArrayList<Link>();
		final Link lk1 = new Link();
		lk1.withHref(testHref);
		lk1.withRel(testRel);
		links.add(lk1);
		new Expectations() {
			{
				InstanceInfo.Builder.newBuilder().withServiceName(anyString);
				result = anyBuilder;
				anyBuilder.withVersion(anyString);
				result = anyBuilder;
				anyBuilder.build();
				result = anyInstanceInfo;
				LookupManager.getInstance().getLookupClient().getInstanceForTenant(anyInstanceInfo, anyString);
				result = anyInstanceInfo;
				LookupManager.getInstance().getLookupClient().getSanitizedInstanceInfo((InstanceInfo) any, anyString);
				result = anySanitizedInfo;
				anySanitizedInfo.getLinks();
				result = new Delegate<List<Link>>() {
					@SuppressWarnings("unused")
					List<Link> getLinks()
					{
						return links;
					}
				};
			}
		};
		Link lk = RegistryLookupUtil.getServiceExternalLinkWithRelPrefix("LoganService", "0.1", "logan", "emaastesttenant1");
		Assert.assertEquals(lk.getHref(), testHref);
		Assert.assertEquals(lk.getRel(), testRel);
	}

	@Test(groups = { "s2" })
	public void testGetServiceInternalLink(@Mocked final Builder anyBuilder, @Mocked final InstanceInfo anyInstanceInfo,
			@Mocked final LookupManager anyLockupManager, @Mocked final LookupClient anyClient,
			@Mocked final InstanceQuery anyInstanceQuery) throws Exception
	{
		final String serviceName = "ApmUI";
		final String version = "0.1";
		new Expectations() {
			{
				InstanceInfo.Builder.newBuilder().withServiceName(withEqual(serviceName)).withVersion(withEqual(version)).build();
				result = anyInstanceInfo;

				new InstanceQuery((InstanceInfo) any);
				LookupManager.getInstance();
				result = anyLockupManager;
				anyLockupManager.getLookupClient();
				result = anyClient;
				anyClient.lookup((InstanceQuery) any);
				result = new Delegate<List<InstanceInfo>>() {
					@SuppressWarnings("unused")
					List<InstanceInfo> lookup(InstanceQuery query)
					{
						List<InstanceInfo> list = new ArrayList<InstanceInfo>();
						for (int i = 0; i < 3; i++) {
							list.add(anyInstanceInfo);
						}
						return list;
					}
				};
				Link lkAPM = new Link();
				lkAPM.withHref("http://den00hvb.us.oracle.com:7028/emsaasui/apmUi/index.html");
				lkAPM.withRel("home");
				Link lkITA = new Link();
				lkITA.withHref("http://den00hvb.us.oracle.com:7019/emsaasui/emcitas/worksheet/html/displaying/worksheet-list.html");
				lkITA.withRel("home");
				Link lkLA = new Link();
				lkLA.withHref("http://den00yse.us.oracle.com:7004/emsaasui/emlacore/resources/");
				lkLA.withRel("loganService");
				anyInstanceInfo.getLinksWithProtocol(anyString, anyString);
				returns(Arrays.asList(lkAPM), Arrays.asList(lkITA), Arrays.asList(lkLA));
			}
		};
		Link lk = RegistryLookupUtil.getServiceInternalLink(serviceName, version, "home", null);
		Assert.assertEquals(lk.getHref(), "http://den00hvb.us.oracle.com:7028/emsaasui/apmUi/index.html");
	}

	@Test(groups = { "s2" })
	public void testReplaceWithVanityUrlForEndpointEntity_S2(@Mocked final Builder anyBuilder,
			@Mocked final InstanceInfo anyInstanceInfo, @Mocked final LookupManager anyLockupManager,
			@Mocked final LookupClient anyClient, @Mocked final InstanceQuery anyInstanceQuery) throws Exception
	{
		testReplaceWithVanityUrlExpectations(anyBuilder, anyInstanceInfo, anyLockupManager, anyClient, anyInstanceQuery);

		EndpointEntity ee = new EndpointEntity("APM", "0.1", "https://tenant1.apm.original.link/somepage.html");
		EndpointEntity replacedEntity = RegistryLookupUtil.replaceWithVanityUrl(ee, "tenant1", RegistryLookupUtil.APM_SERVICE);
		Assert.assertEquals(replacedEntity.getHref(), "https://tenant1.apm.replaced.link/somepage.html");

	}

	@Test(groups = { "s2" })
	public void testReplaceWithVanityUrlForLink_S2(@Mocked final Builder anyBuilder, @Mocked final InstanceInfo anyInstanceInfo,
			@Mocked final LookupManager anyLockupManager, @Mocked final LookupClient anyClient,
			@Mocked final InstanceQuery anyInstanceQuery) throws Exception
	{
		testReplaceWithVanityUrlExpectations(anyBuilder, anyInstanceInfo, anyLockupManager, anyClient, anyInstanceQuery);

		Link lk = new Link();
		lk.withHref("https://tenant2.la.original.link/somepage.html");
		Link replacedLink = RegistryLookupUtil.replaceWithVanityUrl(lk, "tenant2", RegistryLookupUtil.LA_SERVICE);
		Assert.assertEquals(replacedLink.getHref(), "https://tenant2.la.replaced.link/somepage.html");

		replacedLink = RegistryLookupUtil.replaceWithVanityUrl(lk, "tenant2", "");
		Assert.assertEquals(replacedLink, lk);
	}

	@Test(groups = { "s2" })
	public void testReplaceWithVanityUrlForString_S2(@Mocked final Builder anyBuilder,
			@Mocked final InstanceInfo anyInstanceInfo, @Mocked final LookupManager anyLockupManager,
			@Mocked final LookupClient anyClient, @Mocked final InstanceQuery anyInstanceQuery) throws Exception
	{
		testReplaceWithVanityUrlExpectations(anyBuilder, anyInstanceInfo, anyLockupManager, anyClient, anyInstanceQuery);

		String href = "https://tenant3.ita.original.link/somepage.html";
		String replacedHref = RegistryLookupUtil.replaceWithVanityUrl(href, "tenant3", RegistryLookupUtil.ITA_SERVICE);
		Assert.assertEquals(replacedHref, "https://tenant3.ita.replaced.link/somepage.html");

		testReplaceWithVanityUrlExpectations(anyBuilder, anyInstanceInfo, anyLockupManager, anyClient, anyInstanceQuery);
		href = "https://tenant4.monitoring.original.link/somepage.html";
		replacedHref = RegistryLookupUtil.replaceWithVanityUrl(href, "tenant4", RegistryLookupUtil.MONITORING_SERVICE);
		Assert.assertEquals(replacedHref, "https://tenant4.monitoring.replaced.link/somepage.html");

		testReplaceWithVanityUrlExpectations(anyBuilder, anyInstanceInfo, anyLockupManager, anyClient, anyInstanceQuery);
		href = "https://tenant5.security.original.link/somepage.html";
		replacedHref = RegistryLookupUtil.replaceWithVanityUrl(href, "tenant5", RegistryLookupUtil.SECURITY_ANALYTICS_SERVICE);
		Assert.assertEquals(replacedHref, "https://tenant5.security.replaced.link/somepage.html");
	}

	private void testReplaceWithVanityUrlExpectations(final Builder anyBuilder, final InstanceInfo anyInstanceInfo,
			final LookupManager anyLockupManager, final LookupClient anyClient, final InstanceQuery anyInstanceQuery)
			throws Exception
	{
		new Expectations() {
			{
				InstanceInfo.Builder.newBuilder();
				result = anyBuilder;
				anyBuilder.withServiceName(anyString);
				result = anyBuilder;
				anyBuilder.build();
				result = anyInstanceInfo;

				new InstanceQuery((InstanceInfo) any);
				LookupManager.getInstance();
				result = anyLockupManager;
				anyLockupManager.getLookupClient();
				result = anyClient;
				anyClient.lookup((InstanceQuery) any);
				result = new Delegate<List<InstanceInfo>>() {
					@SuppressWarnings("unused")
					List<InstanceInfo> lookup(InstanceQuery query)
					{
						List<InstanceInfo> list = new ArrayList<InstanceInfo>();
						for (int i = 0; i < 5; i++) {
							list.add(anyInstanceInfo);
						}
						return list;
					}
				};
				Link lkAPM = new Link();
				lkAPM.withHref("https://apm.replaced.link");
				Link lkITA = new Link();
				lkITA.withHref("https://ita.replaced.link");
				Link lkLA = new Link();
				lkLA.withHref("https://la.replaced.link");
				Link lkMonitoring = new Link();
				lkMonitoring.withHref("https://monitoring.replaced.link");
				Link lkSecurity = new Link();
				lkSecurity.withHref("https://security.replaced.link");
				anyInstanceInfo.getLinksWithProtocol(anyString, anyString);
				returns(Arrays.asList(lkAPM), Arrays.asList(lkITA), Arrays.asList(lkLA), Arrays.asList(lkMonitoring),
						Arrays.asList(lkSecurity));
			}
		};
	}
}
