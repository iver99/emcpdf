package oracle.sysman.emaas.platform.dashboards.ws.rest.ssfDatautil;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;


import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil.VersionedLink;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author pingwu
 *
 */
public class SSFDataUtil {
	
	private static final String SERVICE_NAME = "SavedSearch";
	private static final String VERSION = "1.0";
	private static final String PATH = "search";
	private static final String GET_SEARCH_DATA_URI = "all";
	private static final String SAVE_SEARCH_DATA_URI = "import";
	private final static Logger LOGGER = LogManager.getLogger(SSFDataUtil.class);
	
	public static String getSSFData(String userTenant, String requestEntity) {
		return accessSSFWebService(userTenant,GET_SEARCH_DATA_URI,requestEntity);
	}
	
	public static String saveSSFData(String userTenant, String requestEntity, boolean override) {
		String queryParam = override?"?override=true":"?override=false";
		return accessSSFWebService(userTenant, SAVE_SEARCH_DATA_URI + queryParam,requestEntity);
	}

	private static String accessSSFWebService(String remoteUser,String uri,String data)
	{
		
		RestClient rc = new RestClient();
        Link ssfLink = RegistryLookupUtil.getServiceInternalLink(SERVICE_NAME, VERSION, PATH, null);
        if (ssfLink == null) {
        	LOGGER.error("Can not get ssf link from ServiceRegistry!");
        	return null;
        }
        String tenantHref = ssfLink.getHref() + "/" + uri;
        String tenantName = TenantContext.getCurrentTenant();
        String savedSearchResponse = null;
        try {
			rc.setHeader(RestClient.X_USER_IDENTITY_DOMAIN_NAME, tenantName);
			rc.setHeader(RestClient.OAM_REMOTE_USER, remoteUser);
        	savedSearchResponse = rc.put(tenantHref, data, tenantName, 
        	        ((VersionedLink) ssfLink).getAuthToken());
        	LOGGER.info("Response of request SSF service is {} ", savedSearchResponse);
        }catch (Exception e) {
        	LOGGER.error(e);
        }
        return savedSearchResponse;
	}

}
