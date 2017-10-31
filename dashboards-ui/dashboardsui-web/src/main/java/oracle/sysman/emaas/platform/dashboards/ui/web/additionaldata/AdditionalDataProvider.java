package oracle.sysman.emaas.platform.dashboards.ui.web.additionaldata;

import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;

import oracle.sysman.emaas.platform.dashboards.ui.web.AdditionalDataFilter;
import oracle.sysman.emaas.platform.dashboards.ui.web.DashboardsUiCORSFilter;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.DashboardDataAccessUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.StringUtil;
import oracle.sysman.emaas.platform.uifwk.bootstrap.HtmlBootstrapJsUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by guochen on 2/10/17.
 */
public class AdditionalDataProvider
{
	private final static Logger LOGGER = LogManager.getLogger(AdditionalDataProvider.class);

	public static String getPreloadDataForRequest(HttpServletRequest httpReq)
	{
		String userTenant = httpReq.getHeader(DashboardsUiCORSFilter.OAM_REMOTE_USER_HEADER);
		// TODO: check session expiry header
		String sesExp = httpReq.getHeader("SESSION_EXP");
		LOGGER.debug("Trying to get SESSION_EXP from builder.html file, its value is: {}", sesExp);
		if (!StringUtil.isEmpty(userTenant) && userTenant.indexOf(".") > 0) {
			int pos = userTenant.indexOf(".");
			String tenant = userTenant.substring(0, pos);
			String user = userTenant.substring(pos + 1);
			LOGGER.info("Retrieved tenant is {} and user is {} from userTenant {}", tenant, user, userTenant);
			if (StringUtil.isEmpty(tenant) || StringUtil.isEmpty(user)) {
				LOGGER.warn("Retrieved null tenant or user");
				return null;
			}

			return AdditionalDataProvider.getPreloadData(httpReq, tenant, user, httpReq.getHeader("referer"), sesExp,
					httpReq.getRequestURI());
		}
		return null;
	}

	private static String getPreloadData(HttpServletRequest httpReq, String tenant, String user, String referer,
										 String sessionExp, String uri)
	{
		if (StringUtil.isEmpty(tenant) || StringUtil.isEmpty(user)) {
			LOGGER.warn(
					"tenant {}/user {} is null or empty or invalid, so do not update additional data for dashboard page then",
					tenant, user);
			return null;
		}
		StringBuilder sb = new StringBuilder();
		if (AdditionalDataFilter.BUILDER_URI.equals(uri)) { // only builder page needs dashbaord data
			long start = System.currentTimeMillis();
			sb.append("window.dfBootstrapDataReceived=$.Deferred();");
			String sdkJS = HtmlBootstrapJsUtil.getSDKVersionJS();
			long end = System.currentTimeMillis();
			if (sdkJS != null) {
				sb.append(HtmlBootstrapJsUtil.getSDKVersionJS());
			}
			sb.append("/* SDKVersionJS() time: ").append(end - start).append("ms */");
			long startPageLoadEvent = System.currentTimeMillis();
			LOGGER.debug("Generating Page Load Event.");
			HtmlBootstrapJsUtil.generatePageLoadEvent(tenant, user, referer, sessionExp);
			long endPageLoadEvent = System.currentTimeMillis();
			sb.append("/* PAGE_LOAD_EVENT_API_COST : ").append(endPageLoadEvent - startPageLoadEvent).append("ms */");
			String result = sb.toString();
			LOGGER.debug("Builder page SDKVersionJS data is {}", result);
			return result;
		}else{
			//Get necessary data for branding bar
			String bootstrapJS = HtmlBootstrapJsUtil.getAllBootstrapJS(httpReq);
			if (StringUtil.isEmpty(bootstrapJS)) {
				LOGGER.warn("Retrieved null or empty bootstrap JS for tenant {} user {}", tenant, user);
			}
			else {
				LOGGER.debug("Retrieved bootstrap js: " + bootstrapJS);
				sb.append(bootstrapJS);
			}
			return sb.toString();
		}
	}

	public static String getPostloadDataForRequest(HttpServletRequest httpReq)
	{
		String uri = httpReq.getRequestURI();
		if (!AdditionalDataFilter.BUILDER_URI.equals(uri)) {
			return "</body></html>";
		}

		// for dashboard builder page
		StringBuilder sb = new StringBuilder("<script>");
		long start = System.currentTimeMillis();
		String dashboardData = getDashboardData(httpReq);
		long end = System.currentTimeMillis();
		if (dashboardData != null) {
			sb.append(dashboardData);
		}
		sb.append("window.dfBootstrapDataReceived.resolve()</script>")
				.append("<!-- Dashboard meta data: ").append(end - start).append("ms --></body></html>");
		String result = sb.toString();
		LOGGER.debug("Builder page dashboard meta data is {}", result);
		return result;
	}

	private static String getDashboardData(HttpServletRequest httpReq) {
		String userTenant = httpReq.getHeader(DashboardsUiCORSFilter.OAM_REMOTE_USER_HEADER);
		// TODO: check session expiry header
		String sesExp = httpReq.getHeader("SESSION_EXP");
		LOGGER.debug("Trying to get SESSION_EXP from builder.html file, its value is: {}", sesExp);
		if (!StringUtil.isEmpty(userTenant) && userTenant.indexOf(".") > 0) {
			int pos = userTenant.indexOf(".");
			String tenant = userTenant.substring(0, pos);
			String user = userTenant.substring(pos + 1);
			LOGGER.info("Retrieved tenant is {} and user is {} from userTenant {}", tenant, user, userTenant);
			if (StringUtil.isEmpty(tenant) || StringUtil.isEmpty(user)) {
				LOGGER.warn("Retrieved null tenant or user");
				return null;
			}

			final String dashboardIdStr = httpReq.getParameter("dashboardId");
			BigInteger dashboardId = new BigInteger(dashboardIdStr);
			return DashboardDataAccessUtil.getCombinedData(tenant, userTenant, httpReq, sesExp, dashboardId);
		}
		return null;
	}
}
