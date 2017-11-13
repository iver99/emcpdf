/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.web.context;

import javax.servlet.http.HttpServletRequest;

import mockit.Expectations;
import mockit.Mocked;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class GlobalContextUtilTest
{
	@Test(groups = { "s2" })
	public void testGenerateUrlWithGlobalContext(@Mocked final HttpServletRequest request)
	{
		new Expectations() {
			{
				request.getParameter("omcCtx");
				result = "compositeMEID=7F97169E4D8D7A05715DBFA7F83995F4";
			}
		};
		String url = GlobalContextUtil.generateUrlWithGlobalContext(null, request);
		Assert.assertNull(url);
		
	    String urlNotInWhiteList = GlobalContextUtil.generateUrlWithGlobalContext("http://example.com", request);
	    Assert.assertEquals(urlNotInWhiteList, GlobalContextUtil.ERRORPAGE);

		String url1 = GlobalContextUtil.generateUrlWithGlobalContext("http://example.com/emsaasui", request);
		Assert.assertEquals(url1, "http://example.com/emsaasui?omcCtx=compositeMEID%3D7F97169E4D8D7A05715DBFA7F83995F4");

		String url2 = GlobalContextUtil.generateUrlWithGlobalContext("http://example.com/emsaasui?", request);
		Assert.assertEquals(url2, "http://example.com/emsaasui?omcCtx=compositeMEID%3D7F97169E4D8D7A05715DBFA7F83995F4");

		String url3 = GlobalContextUtil.generateUrlWithGlobalContext("http://example.com/emsaasui?param1=value1", request);
		Assert.assertEquals(url3, "http://example.com/emsaasui?param1=value1&omcCtx=compositeMEID%3D7F97169E4D8D7A05715DBFA7F83995F4");

		String url4 = GlobalContextUtil.generateUrlWithGlobalContext("http://example.com/emsaasui?param1=value1#hashparam=hashvalue",
				request);
		Assert.assertEquals(url4,
				"http://example.com/emsaasui?param1=value1&omcCtx=compositeMEID%3D7F97169E4D8D7A05715DBFA7F83995F4#hashparam=hashvalue");

		String url5 = GlobalContextUtil.generateUrlWithGlobalContext(
				"http://example.com/emsaasui?param1=value1&omcCtx=compositeMEID%3D7F97169E4D8D7A05715DBFA7F83995E5#hashparam=hashvalue",
				request);
		Assert.assertEquals(url5,
				"http://example.com/emsaasui?param1=value1&omcCtx=compositeMEID%3D7F97169E4D8D7A05715DBFA7F83995F4#hashparam=hashvalue");
	}
}
