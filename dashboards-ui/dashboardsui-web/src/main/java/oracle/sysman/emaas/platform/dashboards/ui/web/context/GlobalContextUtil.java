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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author aduan
 */
public class GlobalContextUtil
{
	private final static Logger LOGGER = LogManager.getLogger(GlobalContextUtil.class);
	// TODO should be the real domain here
	private final static String[] WHITELIST = {"error.html", "builder.html", "emsaasui"};
	public final static String ERRORPAGE = "./error.html?msg=DBS_ERROR_PAGE_NOT_FOUND_MSG";

	public static String generateUrlWithGlobalContext(String baseUrl, HttpServletRequest request)
	{
	    // checking the white list to avoid fortify issue "Open Redirect"
	    if(baseUrl == null)
	        return null;
	    boolean isDubious = true;
	    for(int i = 0; i < WHITELIST.length; i ++) {
	        if(baseUrl.contains(WHITELIST[i])) {
	            isDubious = false;
	            break;
	        }
	    }
	    if(isDubious) {
	        return ERRORPAGE;
	    }
	    
		String urlWithContext = baseUrl;
		try {
			String omcCtx = null;
			if (request != null && baseUrl != null && !baseUrl.isEmpty()) {
				omcCtx = request.getParameter("omcCtx");
			}
			if (omcCtx != null && !omcCtx.isEmpty()) {
				urlWithContext = GlobalContextUtil.addOrUpdateUrlParam(baseUrl, "omcCtx", URLEncoder.encode(omcCtx, "UTF-8"));
			}
		}
		catch (UnsupportedEncodingException uee) {
			LOGGER.error("Failed to encode URL parameters.");
		}

		return urlWithContext;
	}

	private static String addOrUpdateUrlParam(String url, String paramName, String paramValue)
			throws UnsupportedEncodingException
	{
		String newUrl = null;
		//Handle the case anchor section ('#') exists in the given URL
		int anchorIdx = url.indexOf("#");
		String hash = "";
		//Retrieve hash string from the URL and append to the end of the URL after appending context string
		if (anchorIdx != -1) {
			hash = url.substring(anchorIdx);
			url = url.substring(0, anchorIdx);
		}

		String regex = "([?&])" + URLEncoder.encode(paramName, "UTF-8") + "=.*?(&|$|#)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			newUrl = url.replaceAll(regex, "$1" + paramName + "=" + paramValue + "$2") + hash;
		}
		else {

			newUrl = url + (url.indexOf('?') > 0 ?
					//Handle case that an URL ending with a question mark only
					url.lastIndexOf("?") == url.length() - 1 ? "" : "&"
						: "?") + paramName + "=" + paramValue + hash;
		}

		return newUrl;
	}
}
