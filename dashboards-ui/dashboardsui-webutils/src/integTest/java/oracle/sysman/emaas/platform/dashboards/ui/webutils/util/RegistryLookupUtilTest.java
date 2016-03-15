package oracle.sysman.emaas.platform.dashboards.ui.webutils.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import mockit.Delegate;
import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo.Builder;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceQuery;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.SanitizedInstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;

public class RegistryLookupUtilTest
{
	@Test(groups = { "s2" })
	public void testGetServiceExternalLink(@Mocked final Builder anyBuilder, @Mocked final InstanceInfo anyInstanceInfo,
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
	public void testGetServiceExternalLink_NoTenant(@Mocked final Builder anyBuilder, @Mocked final InstanceInfo anyInstanceInfo,
			@Mocked final InstanceQuery anyInstanceQuery, @Mocked final LookupManager anyLockupManager,
			@Mocked final SanitizedInstanceInfo anySanitizedInfo) throws Exception
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
				lkITA.withHref(
						"http://den00hvb.us.oracle.com:7019/emsaasui/emcitas/worksheet/html/displaying/worksheet-list.html");
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
	public void testReplaceWithVanityUrlForString(@Mocked final Builder anyBuilder, @Mocked final InstanceInfo anyInstanceInfo,
			@Mocked final LookupManager anyLockupManager, @Mocked final LookupClient anyClient,
			@Mocked final InstanceQuery anyInstanceQuery) throws Exception
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
						for (int i = 0; i < 3; i++) {
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
				anyInstanceInfo.getLinksWithProtocol(anyString, anyString);
				returns(Arrays.asList(lkAPM), Arrays.asList(lkITA), Arrays.asList(lkLA));
			}
		};

		String href = "https://tenant1.ita.original.link/somepage.html";
		String replacedHref = RegistryLookupUtil.replaceWithVanityUrl(href, "tenant1", RegistryLookupUtil.ITA_SERVICE);
		Assert.assertEquals(replacedHref, "https://tenant1.ita.replaced.link/somepage.html");
	}
}