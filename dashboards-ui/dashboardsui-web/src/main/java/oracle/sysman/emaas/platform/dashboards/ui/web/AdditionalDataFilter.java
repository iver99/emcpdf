package oracle.sysman.emaas.platform.dashboards.ui.web;

import oracle.sysman.emaas.platform.dashboards.ui.web.additionaldata.AdditionalDataProvider;
import oracle.sysman.emaas.platform.dashboards.ui.web.additionaldata.HtmlFragmentCache;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.DashboardDataAccessUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.StringUtil;

import oracle.sysman.emaas.platform.uifwk.nls.filter.NLSFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import java.io.*;

/**
 * Created by guochen on 11/18/16.
 */
public class AdditionalDataFilter implements Filter {
    private final static Logger LOGGER = LogManager.getLogger(AdditionalDataFilter.class);

    private static final String LANG_ATTR_TO_REPLACE = "lang=\"en-US\"";
    private static final String ADDITIONA_DATA_TO_REPLACE = "////ADDITIONALDATA////";

    public static String HOME_URI = "/emsaasui/emcpdfui/home.html";
    public static String WELCOME_URI = "/emsaasui/emcpdfui/welcome.html";
    public static String BUILDER_URI = "/emsaasui/emcpdfui/builder.html";
    public static String ERROR_URI = "/emsaasui/emcpdfui/error.html";
    public static String TEST_URI = "/emsaasui/emcpdfui/testdashboard.html";

    private static class CaptureWrapper extends HttpServletResponseWrapper
    {
        private final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        private final ServletOutputStream outputStream = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException
            {
                byteStream.write(b);
            }
        };

        private PrintWriter writer = null;
        {
            try {
				writer = new PrintWriter(new OutputStreamWriter(byteStream, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("Failed to encode outputStream", e);
			}
        }

        public CaptureWrapper(HttpServletResponse response)
        {
            super(response);
        }

        public String getResponseText()
        {
            String result;
            try {
            	outputStream.flush();
                outputStream.close();
                result = byteStream.toString("UTF-8");
            }
            catch (IOException e) {
                LOGGER.error("Failed to decode outputStream", e);
                result = null;
            }
            return result;
        }

        @Override
        public ServletOutputStream getOutputStream()
        {
            return outputStream;
        }

        @Override
        public PrintWriter getWriter()
        {
            return writer;
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        LOGGER.debug("Now enter the AdditionalDataFilter");
        HttpServletRequest httpReq = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setCharacterEncoding("utf-8");
        httpResponse.setContentType("text/html;charset=utf-8");

        final CaptureWrapper wrapper = new CaptureWrapper(httpResponse);
        HtmlFragmentCache hfc = HtmlFragmentCache.getInstance();
        String uri = httpReq.getRequestURI();
        LOGGER.info("The request uri is {}", uri);
        if (HtmlFragmentCache.getInstance().isHtmlFragmentElementsCached(uri)) {
            // previously the html static resource (start part&end part) has been cached,
            // so no need to parse the static resource again or go to the next filter
            // just concatinate the data string into one
            String langAttr = NLSFilter.getLangAttr(httpReq);
            String dashboardData = AdditionalDataProvider.getPreloadDataForRequest(httpReq);
            String newResponseText = getPreloadTextFromCachedHtmls(uri, langAttr, dashboardData);
            LOGGER.debug("After getting cached static html fragment, contactinating the data and inserting into html, the response text is {}", newResponseText);
            updateResponseWithAdditionDataText(response, newResponseText);
			wrapper.getWriter().flush();

			String postData = AdditionalDataProvider.getPostloadDataForRequest(httpReq);
			updateResponseWithAdditionDataText(response, postData);
			wrapper.getWriter().flush();
			wrapper.getWriter().close();
            return;
        }


        chain.doFilter(request, wrapper);

        final String responseText = wrapper.getResponseText();
        assert (responseText != null);
        String newResponseText = responseText;
        LOGGER.debug("Before inserting additional data, the response text is {}", newResponseText);

        int idxLangAttr = responseText.indexOf(LANG_ATTR_TO_REPLACE);
        String beforeLangAttrPart = responseText.substring(0, idxLangAttr);
        LOGGER.debug("Before lang attr part is {}", beforeLangAttrPart);
        int idxDashboard = responseText.indexOf(ADDITIONA_DATA_TO_REPLACE);
        String beforeDashboardDataPart = responseText.substring(idxLangAttr + LANG_ATTR_TO_REPLACE.length(), idxDashboard);
        LOGGER.debug("Before dashboard&after lang attr part is {}", beforeDashboardDataPart);
		int idxBodyEnd = responseText.indexOf("</body>");
        String afterDashboardDataPart = responseText.substring(idxDashboard + ADDITIONA_DATA_TO_REPLACE.length(), idxBodyEnd);
		LOGGER.debug("After part is {}", afterDashboardDataPart);
        hfc.cacheElementsForRequest(uri, beforeLangAttrPart, beforeDashboardDataPart, afterDashboardDataPart);

        String langAttr = NLSFilter.getLangAttr(httpReq);
        String preData = AdditionalDataProvider.getPreloadDataForRequest(httpReq);
        newResponseText = getPreloadTextFromCachedHtmls(uri, langAttr, preData);
        LOGGER.debug("After inserting additional data, the response text is {}", newResponseText);
        updateResponseWithAdditionDataText(response, newResponseText);
		wrapper.getWriter().flush();

		String postData = AdditionalDataProvider.getPostloadDataForRequest(httpReq);
		updateResponseWithAdditionDataText(response, postData);
		wrapper.getWriter().flush();
		wrapper.getWriter().close();
    }

    /**
     *  Update the response string with the specified response text
     * 
     * @param response
     * @param newResponseText
     * @throws IOException
     */
    private void updateResponseWithAdditionDataText(ServletResponse response, String newResponseText) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
			writer.println(newResponseText);
		}
    }

    /**
     *  get the final response text by concatenating the cached before part+dashboard data+cached end part
     * 
     * @param dashboardData
     */
    private String getPreloadTextFromCachedHtmls(String uri, String langAttr, String dashboardData) {
        String newResponseText;
        HtmlFragmentCache.CachedHtml ce = HtmlFragmentCache.getInstance().getCachedElementsForRequest(uri);
        StringBuilder sb = new StringBuilder(ce.getBeforeLangAttrPart());
        if (StringUtil.isEmpty(langAttr)) {
            sb.append(LANG_ATTR_TO_REPLACE);
        } else {
            sb.append(langAttr);
        }
        sb.append(ce.getBeforeAdditionalDataPart());
        if (!StringUtil.isEmpty(dashboardData)) {
            sb.append(dashboardData);
        }
        sb.append(ce.getAfterAdditionalDataPart());
        newResponseText = sb.toString();
        return newResponseText;
    }

    @Override
    public void destroy() {

    }
}
