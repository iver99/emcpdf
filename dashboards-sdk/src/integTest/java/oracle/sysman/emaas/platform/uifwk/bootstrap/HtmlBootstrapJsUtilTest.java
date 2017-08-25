/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ashkak
 */
package oracle.sysman.emaas.platform.uifwk.bootstrap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.authz.bean.LoginDataStore;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emaas.platform.emcpdf.cache.util.StringUtil;
import oracle.sysman.emaas.platform.uifwk.util.DataAccessUtil;

import org.testng.Assert;
import org.testng.annotations.Test;

public class HtmlBootstrapJsUtilTest
{
	@Test(groups = { "s2" })
	public void testGetAllBootstrapJS(@Mocked final HttpServletRequest httpReq, @Mocked final DataAccessUtil dataAccessUtil,
			@Mocked final LookupManager lookupManager, @Mocked final LookupClient lookupClient,
			@Mocked final InstanceInfo instanceInfo)
	{
		final String userInfo = "{\"currentUser\":\"emaastesttenant1.emcsadmin\",\"userRoles\":[\"APM Administrator\",\"APM User\"]}";
		final String registration = "{\"cloudServices\":[],\"visualAnalyzers\":[],\"assetRoots\":[],\"adminLinks\":[],\"homeLinks\":[],\"sessionExpiryTime\":\"20170214103126\",\"ssoLogoutUrl\":\"https://host:port/ssoLogout\"}";
		final String sdkVersionJS = "if(!window.sdkFilePath){window.sdkFilePath={};}window.sdkFilePath={};window.getSDKVersionFile=function(nonCacheableVersion){console.log(\"getSDKVersionFile() for: \"+nonCacheableVersion);var versionFile=nonCacheableVersion;if(window.sdkFilePath){versionFile=window.sdkFilePath[nonCacheableVersion];}if(!versionFile){versionFile=nonCacheableVersion;}console.log(\"getSDKVersionFile(), found version: \"+versionFile);return versionFile;};";
		final String subscribedApps = "{\"applications\":[\"APM\",\"LogAnalytics\",\"ITAnalytics\"]}";
        final String brandingBarData = "window._uifwk.cachedData.userInfo="+userInfo+"window._uifwk.cachedData.registrations="+registration+"window._uifwk.cachedData.subscribedapps="+subscribedApps;
		new Expectations() {
			{
				httpReq.getHeader("referer");
				result = "referer";
				httpReq.getHeader("SESSION_EXP");
				result = "20170214103126";
				httpReq.getHeader("OAM_REMOTE_USER");
				result = "tenant.user";
				DataAccessUtil.getBrandingBarData(anyString, anyString, anyString, anyString);
				result = brandingBarData;

				lookupClient.getInstancesWithLinkRelPrefix(anyString, anyString);
				result = new ArrayList<>(Arrays.asList(instanceInfo));
				instanceInfo.getServiceName();
				result = "UIFWK";
				instanceInfo.getLinksWithRelPrefixWithProtocol(anyString, anyString);
				result = new ArrayList<Link>();
			}
		};
		String newJs = HtmlBootstrapJsUtil.getAllBootstrapJS(httpReq);
		String fullBrandingBarDataJs = "if(!window._uifwk){window._uifwk={};}if(!window._uifwk.cachedData){window._uifwk.cachedData={};}" + brandingBarData;
                Assert.assertTrue(newJs.indexOf(sdkVersionJS) != -1);
                Assert.assertTrue(newJs.indexOf(fullBrandingBarDataJs) != -1);
	}

	@Test(groups = { "s2" })
	public void testGetBrandingDataJS(@Mocked final HttpServletRequest httpReq, @Mocked final DataAccessUtil dataAccessUtil)
	{
		final String userInfo = "{\"currentUser\":\"emaastesttenant1.emcsadmin\",\"userRoles\":[\"APM Administrator\",\"APM User\"]}";
		final String registration = "{\"cloudServices\":[],\"visualAnalyzers\":[],\"assetRoots\":[],\"adminLinks\":[],\"homeLinks\":[],\"sessionExpiryTime\":\"20170214103126\",\"ssoLogoutUrl\":\"https://host:port/ssoLogout\"}";
		final String subscribedApps = "{\"applications\":[\"APM\",\"LogAnalytics\",\"ITAnalytics\"]}";
        final String brandingBarData = "window._uifwk.cachedData.userInfo="+userInfo+"window._uifwk.cachedData.registrations="+registration+"window._uifwk.cachedData.subscribedapps="+subscribedApps;
        new Expectations() {
			{
				httpReq.getHeader("referer");
				result = "referer";
				httpReq.getHeader("SESSION_EXP");
				result = "20170214103126";
				httpReq.getHeader("OAM_REMOTE_USER");
				result = "tenant.user";
				DataAccessUtil.getBrandingBarData(anyString, anyString, anyString, anyString);
				result = brandingBarData;
			}
		};
		String newJs = HtmlBootstrapJsUtil.getBrandingDataJS(httpReq);
		String expectedJs = "if(!window._uifwk){window._uifwk={};}if(!window._uifwk.cachedData){window._uifwk.cachedData={};}" + brandingBarData;
                Assert.assertTrue(newJs.indexOf(expectedJs) != -1);

		new Expectations() {
			{
				httpReq.getHeader("referer");
				result = "referer";
				httpReq.getHeader("SESSION_EXP");
				result = "20170214103126";
				httpReq.getHeader("OAM_REMOTE_USER");
				result = null;
			}
		};
		newJs = HtmlBootstrapJsUtil.getBrandingDataJS(httpReq);
		Assert.assertNull(newJs);
	}

	@Test(groups = { "s2" })
	public void testGetLoggedInUser(@Mocked final LoginDataStore loginDataStore)
	{
		new Expectations() {
			{
				LoginDataStore.getUserName();
				result = "tenant.user";
			}
		};
		String loggedInUser = HtmlBootstrapJsUtil.getLoggedInUser();
		Assert.assertEquals(loggedInUser, "{currentUser:\"tenant.user\"}");
	}

	@Test(groups = { "s2" })
	public void testGetSDKVersionJSToAddNoLinks(@Mocked final LookupManager lookupManager,
			@Mocked final LookupClient lookupClient, @Mocked final InstanceInfo instanceInfo) throws Exception
	{

		new Expectations() {
			{
				lookupClient.getInstancesWithLinkRelPrefix(anyString, anyString);
				result = new ArrayList<>(Arrays.asList(instanceInfo));
				instanceInfo.getServiceName();
				result = "UIFWK";
				instanceInfo.getLinksWithRelPrefixWithProtocol(anyString, anyString);
				result = new ArrayList<Link>();
			}
		};
		String newJs = HtmlBootstrapJsUtil.getSDKVersionJS();
		String expectedJs = "if(!window.sdkFilePath){window.sdkFilePath={};}window.sdkFilePath={};window.getSDKVersionFile=function(nonCacheableVersion){console.log(\"getSDKVersionFile() for: \"+nonCacheableVersion);var versionFile=nonCacheableVersion;if(window.sdkFilePath){versionFile=window.sdkFilePath[nonCacheableVersion];}if(!versionFile){versionFile=nonCacheableVersion;}console.log(\"getSDKVersionFile(), found version: \"+versionFile);return versionFile;};";
                Assert.assertTrue(newJs.indexOf(expectedJs) != -1);
	}

	@Test(groups = { "s2" })
	public void testGetSDKVersionJSWithLinks(@Mocked final LookupManager lookupManager, @Mocked final LookupClient lookupClient,
			@Mocked final InstanceInfo instanceInfo) throws Exception
	{

		new Expectations() {
			{
				lookupClient.getInstancesWithLinkRelPrefix(anyString, anyString);
				result = new ArrayList<>(Arrays.asList(instanceInfo));
				instanceInfo.getServiceName();
				result = "UIFWK";
				instanceInfo.getLinksWithRelPrefixWithProtocol(anyString, anyString);
				result = getLinks();
			}
		};
		String newJs = HtmlBootstrapJsUtil.getSDKVersionJS();
		String expectedJs = "if(!window.sdkFilePath){window.sdkFilePath={};}window.sdkFilePath={\"emsaasui/emcta/ta/js/sdk/topology/emcta-topology\":\"emsaasui/emcta/ta/1.15.0-170202.111534/js/shared-components/emcta-topology-impl\",\"emsaasui/emcta/ta/js/sdk/entitycard/EntityCardUtil\":\"emsaasui/emcta/ta/1.15.0-170202.111534/js/entitycard/EntityCardRegistryImpl\"};window.getSDKVersionFile=function(nonCacheableVersion){console.log(\"getSDKVersionFile() for: \"+nonCacheableVersion);var versionFile=nonCacheableVersion;if(window.sdkFilePath){versionFile=window.sdkFilePath[nonCacheableVersion];}if(!versionFile){versionFile=nonCacheableVersion;}console.log(\"getSDKVersionFile(), found version: \"+versionFile);return versionFile;};";
                Assert.assertTrue(newJs.indexOf(expectedJs) != -1);
	}

	private List<Link> getLinks()
	{
		ArrayList<Link> al = new ArrayList<Link>();
		Link link1 = new Link();
		link1.withHref("https://den02dtb.us.oracle.com:7004/emsaasui/emcta/ta/1.15.0-170202.111534/js/shared-components/emcta-topology-impl");
		link1.withRel("versionLookupSDK/emsaasui/emcta/ta/js/sdk/topology/emcta-topology");
		al.add(link1);
		Link link2 = new Link();
		link2.withHref("https://den02dtb.us.oracle.com:7004/emsaasui/emcta/ta/1.15.0-170202.111534/js/entitycard/EntityCardRegistryImpl");
		link2.withRel("versionLookupSDK/emsaasui/emcta/ta/js/sdk/entitycard/EntityCardUtil");
		al.add(link2);
		return al;
	}

}
