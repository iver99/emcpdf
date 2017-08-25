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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import oracle.sysman.emSDK.emaas.authz.bean.LoginDataStore;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emaas.platform.emcpdf.cache.util.StringUtil;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;
import oracle.sysman.emaas.platform.uifwk.util.DataAccessUtil;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil.VersionedLink;


import org.apache.logging.log4j.LogManager;
import org.codehaus.jackson.map.ObjectMapper;

public class HtmlBootstrapJsUtil
{
	/**
	 * Utility to write map values as JSON
	 */
	public static class JsonWriteUtil
	{

		private static final ObjectMapper mapper = new ObjectMapper();

		public static String writeValueAsString(Object value)
		{
			try {
				return mapper.writeValueAsString(value);
			}
			catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	private static final String SDK_FILE = "versionLookupSDK";
	private static final String HTTP = "http";
	private static final String HTTPS = "https";
	private static final String OAM_REMOTE_USER_HEADER = "OAM_REMOTE_USER";

	private static org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(HtmlBootstrapJsUtil.class.getName());

	@Deprecated
	public static String getAllBootstrapJS()
	{
		return HtmlBootstrapJsUtil.getSDKVersionJS();
	}

	public static String getAllBootstrapJS(HttpServletRequest httpReq)
	{
		StringBuilder bootstrapJS = new StringBuilder();
		bootstrapJS.append(HtmlBootstrapJsUtil.getSDKVersionJS()).append(HtmlBootstrapJsUtil.getBrandingDataJS(httpReq));
		return bootstrapJS.toString();
	}

	/**
	 * Returns javascript that is to be added for branding bar. This script contains: 1.Registration links 2.Session expiry time
	 * 3.User info 4.SSO logout URL
	 *
	 * @return String
	 */
	public static String getBrandingDataJS(HttpServletRequest httpReq)
	{
                long startBdJS = System.currentTimeMillis();
		LOGGER.debug("Start to get branding bar bootstrap js...");
		StringBuilder sb = new StringBuilder();
		String referer = httpReq.getHeader("referer");
		String sessionExp = httpReq.getHeader("SESSION_EXP");
		LOGGER.info("Retrieved referer: " + referer + ", SESSION_EXP: " + sessionExp);
		String userTenant = httpReq.getHeader(OAM_REMOTE_USER_HEADER);
		String tenant = null;
		String user = null;
		if (!StringUtil.isEmpty(userTenant) && userTenant.indexOf(".") > 0) {
			int pos = userTenant.indexOf(".");
			tenant = userTenant.substring(0, pos);
			user = userTenant.substring(pos + 1);
			LOGGER.info("Retrieved tenant is {} and user is {} from userTenant {}", tenant, user, userTenant);
			if (StringUtil.isEmpty(tenant) || StringUtil.isEmpty(user)) {
				LOGGER.warn("Retrieved null tenant or user");
				return null;
			}
		}
		else {
			LOGGER.warn("Retrieved null or invalid tenant user");
			return null;
		}
               
                long startPageLoadEvent = System.currentTimeMillis(); 
                LOGGER.debug("Generating Page Load Event.");
                generatePageLoadEvent(tenant, user, referer, sessionExp);
		long endPageLoadEvent = System.currentTimeMillis(); 		

		//user info
		LOGGER.debug("Start to get brandingbar data.");
		String brandingbarData = DataAccessUtil.getBrandingBarData(tenant, user, referer, sessionExp);
		//append uifwk cache data structure
        sb.append("if(!window._uifwk){window._uifwk={};}if(!window._uifwk.cachedData){window._uifwk.cachedData={};}");
        if (!StringUtil.isEmpty(brandingbarData)) {
            // the returned value from dashboard-api side could be injected into html directly
            sb.append(brandingbarData);
        }
        //In some cases http request to get brandingbar data failed with status like 401 (user don't have any role/privilege)
        //We still want the logged in tenant, user info and the sso logout URL to be available 
        else {
        	//append uifwk cache data structure
            sb.append("if(!window._uifwk){window._uifwk={};}if(!window._uifwk.cachedData){window._uifwk.cachedData={};}window._uifwk.cachedData.loggedInUser=");
            sb.append(getLoggedInUser());
            sb.append(";");
            String ssoLogoutUrl = getSsoLogoutUrl();
            if (!StringUtil.isEmpty(ssoLogoutUrl)) {
            	sb.append("window.cachedSSOLogoutUrl=\"");
            	sb.append(ssoLogoutUrl);
            	sb.append("\";");
            }
        }
		String injectableJS = sb.toString();
                injectableJS = "/* PAGE_LOAD_EVENT_API_COST : " +  (endPageLoadEvent - startPageLoadEvent) + "ms */" + injectableJS;
                injectableJS = "/* GET_BRANDING_BAR_DATA_API_COST : " + (System.currentTimeMillis() - startBdJS) + "ms */" + injectableJS; 
		LOGGER.info("getBrandingDataJS(), injectableJS: " + injectableJS);
		return injectableJS;
	}

	public static String getLoggedInUser()
	{
		String currentUser = LoginDataStore.getUserName();
		String userName = "{currentUser:\"" + currentUser + "\"}";
		return userName;
	}

	/**
	 * Returns javascript that is to be added. This script contains 1. map for SDK version files - "window.sdkFilePath" 2.
	 * function to get the SDK version files - "window.getSDKVersionFile()" "https://den01fjq.us.oracle.com:7005/". Use this to
	 * form correct after context URL for the MAP to be returned (i.e remove host:port information from the value received from
	 * registry manager) RegistryManager returns -
	 * https://den01fjq.us.oracle.com:7005/emsaasui/uifwk/1.15.0-170118.012237/js/uifwk-impl-partition remove host:port to create
	 * value as- "emsaasui/uifwk/1.15.0-170118.012237/js/uifwk-impl-partition" As this is what require uses in javascript to load
	 * modules
	 *
	 * @return String
	 */
	public static String getSDKVersionJS()
	{
                long start = System.currentTimeMillis();
		List<Link> sdkFileLinks = HtmlBootstrapJsUtil.lookupLinksWithRelPrefix(SDK_FILE, HTTPS);
		Map sdkFilesMap = HtmlBootstrapJsUtil.getSdkFilesMap(sdkFileLinks);
		String mapResponse = JsonWriteUtil.writeValueAsString(sdkFilesMap);

		String sdkVersionDefinitionJS = "if(!window.sdkFilePath){window.sdkFilePath={};}window.sdkFilePath=" + mapResponse + ";";
		String getSDKVersionFunctionJS = "window.getSDKVersionFile=function(nonCacheableVersion){console.log(\"getSDKVersionFile() for: \"+nonCacheableVersion);var versionFile=nonCacheableVersion;if(window.sdkFilePath){versionFile=window.sdkFilePath[nonCacheableVersion];}if(!versionFile){versionFile=nonCacheableVersion;}console.log(\"getSDKVersionFile(), found version: \"+versionFile);return versionFile;};";

		String injectableJS = sdkVersionDefinitionJS + getSDKVersionFunctionJS;
                injectableJS = "/* GET_SDK_VERSION_JS_API_COST : " +  (System.currentTimeMillis() - start) + "ms */" + injectableJS;
		LOGGER.debug("VersionFilesSDK(), injectableJS: " + injectableJS);
		return injectableJS;
	}

	/**
	 * private Retrieves list of all links that have "sdkVersionFile". Remove "sdkVersionFile/" from "rel" to get the KEY VALUE is
	 * "href" without the host:port info LINK 1 rel: sdkVersionFile/emsaasui/emcta/ta/js/sdk/entitycard/EntityCardUtil href:
	 * https://den01fjq.us.oracle.com:7005/emsaasui/emcta/ta/1.15.0-201620012335/js/entitycard/EntityCardRegistryImpl LINK 2 rel:
	 * sdkVersionFile/emsaasui/uifwk href:
	 * https://den01fjq.us.oracle.com:7005/emsaasui/uifwk/1.15.0-170118.012237/js/uifwk-impl-partition Return Map like this for
	 * above KEY- emsaasui/emcta/ta/js/sdk/entitycard/EntityCardUtil; VALUE:
	 * emsaasui/emcta/ta/1.15.0-201620012335/js/entitycard/EntityCardRegistryImpl KEY- emsaasui/uifwk; VALUE:
	 * emsaasui/uifwk/1.15.0-170118.012237/js/uifwk-impl-partition
	 *
	 * @param relLinks
	 *            : list of all links that have "sdkVersionFile" registered with serviceregistry
	 * @return Map<K,V>
	 */
	private static Map<String, String> getSdkFilesMap(List<Link> relLinks)
	{
		Map<String, String> sdkVersionFilesMap = new HashMap<>();
		if (relLinks != null && !relLinks.isEmpty()) {
			for (Link link : relLinks) {
				try {
					URL url = new URL(link.getHref());
					String path = url.getPath();
					String key = link.getRel().substring(SDK_FILE.length() + 1); // plus one to accomodate "/"
					LOGGER.info("getSdkFilesMap() key: " + key);
					String value = path.substring(1); // to neglect the preceeding "/"
					if (!sdkVersionFilesMap.containsKey(key)) {
						sdkVersionFilesMap.put(key, value);
						LOGGER.info("getSdkFilesMap() value: " + value);
					}
					else {
						LOGGER.info("getSdkFilesMap() key-value is present already: " + sdkVersionFilesMap.get(key));
					}
				}
				catch (MalformedURLException me) {
					LOGGER.error("Malformed URL getSdkFilesMap(" + relLinks.toString() + "): ", me);
				}
			}
		}
		LOGGER.debug("getSdkFilesMap(" + relLinks.toString() + ") for: ", sdkVersionFilesMap.toString());
		return sdkVersionFilesMap;
	}

	/**
	 * private Returns List of Links that have a particular linkPrefix which in our case is "sdkVersionFile" if protocol is not
	 * specified, it fetches both HTTP and HTTPS links, otherwise only those protocol links are fetched
	 *
	 * @param linkPrefix
	 *            : like "sdkVersionFile"
	 * @param protocol
	 *            : HTTP or HTTPS or both (""/null)
	 * @return List<Link>
	 */
	private static List<Link> lookupLinksWithRelPrefix(String linkPrefix, String protocol)
	{
		LOGGER.info("lookupLinksWithRelPrefix(" + linkPrefix + "), protocol: " + protocol);
		List<Link> linkList = new ArrayList<>();

		LookupClient lookUpClient = LookupManager.getInstance().getLookupClient();
		List<InstanceInfo> instanceList;
		if (protocol == null || protocol.isEmpty()) {
			instanceList = lookUpClient.getInstancesWithLinkRelPrefix(linkPrefix);
		}
		else {
			instanceList = lookUpClient.getInstancesWithLinkRelPrefix(linkPrefix, protocol);
		}
		// TODO: Maybe check for app subbscriptions??
		if (instanceList != null && !instanceList.isEmpty()) {
			for (InstanceInfo internalInstance : instanceList) {
				LOGGER.info("lookupLinksWithRelPrefix for Service: " + internalInstance.getServiceName() + ", protocol: "
						+ protocol);
				List<Link> links;
				if (protocol == null || protocol.isEmpty()) {
					links = internalInstance.getLinksWithRelPrefix(linkPrefix);
				}
				else {
					links = internalInstance.getLinksWithRelPrefixWithProtocol(linkPrefix, protocol);
				}
				if (links != null && !links.isEmpty()) {
					linkList.addAll(links);
				}
			}
		}
		LOGGER.debug("lookupLinksWithRelPrefix(" + linkPrefix + "): ", linkList.toString());
		return linkList;
	}

        private static void generatePageLoadEvent(String tenantName, String userName, String referer, String sessionExp)
        {
            String userTenant = tenantName + "." + userName;
            long start = System.currentTimeMillis();

            try {
                Link uieventLink = RegistryLookupUtil.getServiceInternalLink("TargetAnalytics", "1.1+",
                                    "static/entitycard_uievent", null);
                if (uieventLink == null || StringUtil.isEmpty(uieventLink.getHref())) {
                    LOGGER.warn("Retrieving UI Event Link for tenant {}: null/empty UI Event Link retrieved from service registry.");
                }
                String uieventHref = uieventLink.getHref();
                RestClient rc = new RestClient();
                rc.setHeader(RestClient.X_USER_IDENTITY_DOMAIN_NAME, tenantName);
                rc.setHeader(RestClient.X_REMOTE_USER, userTenant);
                rc.setHeader(RestClient.OAM_REMOTE_USER, userTenant);
                if (!StringUtil.isEmpty(referer)) {
                    rc.setHeader(RestClient.REFERER, referer);
                }
                if (!StringUtil.isEmpty(sessionExp)) {
                    rc.setHeader(RestClient.SESSION_EXP, sessionExp);
                }
                rc.post(uieventHref, tenantName, ((VersionedLink) uieventLink).getAuthToken());
                LOGGER.info("It takes {}ms to complete the UI Event REST API", System.currentTimeMillis() - start);
            } catch (Exception e) {
                LOGGER.error("Error in generating the page load event", e);
            }
        }

	/**
	 * Discover SSO logout URL from service registry
	 *
	 * @return String
	 */
	private static String getSsoLogoutUrl()
	{
		String securityServiceName = "SecurityService";
		String securityServiceVersion = "1.0+";
		String relSsoLogout = "sso.logout";
		String ssoLogoutUrl = null;
		String tenantName = LoginDataStore.getTenantName();
		Link lk = RegistryLookupUtil.getServiceExternalLink(securityServiceName, securityServiceVersion, relSsoLogout, tenantName);
		lk = RegistryLookupUtil.replaceWithVanityUrl(lk, tenantName, securityServiceName);
		if (lk != null) {
			ssoLogoutUrl = lk.getHref();
		}
		else {
			String errorMsg = "Failed to discover SSO logout URL from service registry. Service name: " + securityServiceName + 
					", Service version: " + securityServiceVersion + ", rel: " + relSsoLogout + ", Tenant name: " + tenantName;
			LOGGER.error(errorMsg);
		}
		return ssoLogoutUrl;
	}
}
