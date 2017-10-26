package oracle.sysman.emaas.platform.dashboards.ui.web;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.ui.web.additionaldata.AdditionalDataProvider;
import oracle.sysman.emaas.platform.dashboards.ui.web.additionaldata.HtmlFragmentCache;
import oracle.sysman.emaas.platform.uifwk.nls.filter.NLSFilter;

import org.testng.annotations.Test;

/**
 * Created by guochen on 11/29/16.
 */
public class AdditionalDataFilterTest {
    @Mocked
    HttpServletRequest request;
    @Mocked
    HttpServletResponse response;
    @Mocked
    FilterChain chain;
    @Mocked
    HtmlFragmentCache htmlFragmentCache;
    @Mocked
    HtmlFragmentCache.CachedHtml cachedHtml;
    @Mocked
    NLSFilter nlsFilter;
    @Mocked
    AdditionalDataProvider additionalDataProvider;
    private AdditionalDataFilter additionalDataFilter;
    @Test(groups = { "s2" })
    public void testDoFilter() throws IOException, ServletException {
        AdditionalDataFilter additionalDataFilter = new AdditionalDataFilter();
        new Expectations(){
            {
                HtmlFragmentCache.getInstance();
                result = htmlFragmentCache;
                htmlFragmentCache.isHtmlFragmentElementsCached(anyString);
                result = true;
                NLSFilter.getLangAttr((HttpServletRequest)any);
                result = "langAttr";
                AdditionalDataProvider.getPreloadDataForRequest((HttpServletRequest)any);
                result = "newResponseText";
                htmlFragmentCache.getCachedElementsForRequest(anyString);
                result = cachedHtml;
                cachedHtml.getBeforeLangAttrPart();
                result = "stringbuffer";
                cachedHtml.getBeforeAdditionalDataPart();
                result = "stringbuffer";
                cachedHtml.getAfterAdditionalDataPart();
                result = "strignbufer";
            }
        };
        additionalDataFilter.doFilter(request, response, chain);
    }

    @Test(groups = {"s2"})
    public void testDoFilter2(@Mocked final  ByteArrayOutputStream byteStream) throws IOException, ServletException{
        AdditionalDataFilter additionalDataFilter = new AdditionalDataFilter();
        new Expectations(){
            {
                HtmlFragmentCache.getInstance();
                result = htmlFragmentCache;
                htmlFragmentCache.isHtmlFragmentElementsCached(anyString);
                result = false;

                byteStream.toString(anyString);
                result = "lang=\"en-US\"////ADDITIONALDATA////kaishen</body>";

                NLSFilter.getLangAttr((HttpServletRequest)any);
                result = "langAttr";
                AdditionalDataProvider.getPreloadDataForRequest((HttpServletRequest)any);
                result = "newResponseText";

                htmlFragmentCache.getCachedElementsForRequest(anyString);
                result = cachedHtml;
                cachedHtml.getBeforeLangAttrPart();
                result = "stringbuffer";
                cachedHtml.getBeforeAdditionalDataPart();
                result = "stringbuffer";
                cachedHtml.getAfterAdditionalDataPart();
                result = "strignbufer";
            }
        };
        additionalDataFilter.doFilter(request, response, chain);
    }



}
