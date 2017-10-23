/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.Assert;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeUnit;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author cawei
 */
public class GlobalContextUtil_1130 extends GlobalContextUtil_Version implements IGlobalContextUtil
{
	public static final String GLBCTXTID = "emaas-appheader-globalcxt";
	public static final String GLBCTXTFILTERPILL = "globalBar_pillWrapper";
	public static final String GLBCTXTBUTTON = "buttonShowTopology";
	public static final String DSBNAME = "DASHBOARD_GLOBALTESTING";
	public static final String DSBSETNAME = "DASHBOARDSET_GLOBALTESTING";
	public static final String GLBCTXTPLDIV = "ude-topology-div";

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

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#generateGlbalContextUrl(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public String generateUrlWithGlobalContext(WebDriver driver, String baseUrl, String compositeMeid, String timePeriod,
			Date startTime, Date endTime, List<String> entityMeids)
	{

		String generatedUrl = baseUrl;
		try {
			String omcCtx = "";
			if (compositeMeid != null) {
				omcCtx = omcCtx + "compositeMEID=" + URLEncoder.encode(compositeMeid, "UTF-8") + "&";
			}
			if (timePeriod != null) {
				omcCtx = omcCtx + "timePeriod=" + URLEncoder.encode(timePeriod, "UTF-8") + "&";
			}
			if (startTime != null) {
				omcCtx = omcCtx + "startTime=" + startTime.getTime() + "&";
			}
			if (endTime != null) {
				omcCtx = omcCtx + "endTime=" + endTime.getTime() + "&";
			}
			if (entityMeids != null && entityMeids.size() > 0) {
				String meids = "";
				for (int i = 0; i < entityMeids.size(); i++) {
					if (i == entityMeids.size() - 1) {
						meids = meids + entityMeids.get(i);
					}
					else {
						meids = meids + entityMeids.get(i) + ",";
					}
				}
				omcCtx = omcCtx + "entityMEIDs=" + URLEncoder.encode(meids, "UTF-8");
			}
			if (!omcCtx.equals("") || baseUrl.contains("omcCtx=")) {
				if (omcCtx.endsWith("&")) {
					omcCtx = omcCtx.substring(0, omcCtx.length() - 1);
				}
				generatedUrl = GlobalContextUtil_1130.addOrUpdateUrlParam(baseUrl, "omcCtx", URLEncoder.encode(omcCtx, "UTF-8"));
			}
		}
		catch (Exception e) {
			//Log error message if need
			driver.getLogger().warning("Exception occurred when generating URL with OMC global context.");
		}

		return generatedUrl;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#getGlbalContextMeid(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public String getGlobalContextMeid(WebDriver driver, String baseurl)
	{
		String meid = baseurl.substring(baseurl.indexOf("MEID") + 5, baseurl.indexOf("MEID") + 39);
		return meid;

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#getGolbalContextMeid(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#getGlobalContextName(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public String getGlobalContextName(WebDriver driver)
	{
		return driver.getText(GLBCTXTFILTERPILL);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#getTimeRangeLabel(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public String getTimeRangeLabel(WebDriver webd)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#getTimeRangeLabel(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int)
	 */
	@Override
	public String getTimeRangeLabel(WebDriver webd, int index)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}
	
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#getTimeRangeLabel(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public String getTimeRangeLabel_V2(WebDriver webd)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#getTimeRangeLabel(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int)
	 */
	@Override
	public String getTimeRangeLabel_V2(WebDriver webd, int index)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#hideTopology(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void hideTopology(WebDriver driver)
	{

		if (driver.isDisplayed(GLBCTXTPLDIV)) {
			driver.click(GLBCTXTBUTTON);
		}
		else {
			driver.getLogger().info("the topology already got hidden");
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#isGlobalContextExisted(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean isGlobalContextExisted(WebDriver driver)
	{
		Boolean isGlobalContextExists = false;
		if (driver.isDisplayed(GLBCTXTID)) {
			driver.getLogger().info("the global context bar is  visible. Continue valiation");
			if (driver.isDisplayed(GLBCTXTFILTERPILL) && driver.isDisplayed(GLBCTXTFILTERPILL)) {
				isGlobalContextExists = true;
			}
			else {
				driver.getLogger().info("the global context bar items are not visible");
			}
		}
		else {
			driver.getLogger().info("the global context bar is not visible");
		}
		return isGlobalContextExists;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setCustomTime(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, java.lang.String, java.lang.String)
	 */
	@Override
	public String setCustomTime(WebDriver webd, int index, String startDateTime, String endDateTime)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setCustomTime(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public String setCustomTime(WebDriver webd, String startDateTime, String endDateTime)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setCustomTimeWithDateOnly(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, java.lang.String, java.lang.String)
	 */
	@Override
	public String setCustomTimeWithDateOnly(WebDriver webd, int index, String startDate, String endDate)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setCustomTimeWithDateOnly(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public String setCustomTimeWithDateOnly(WebDriver webd, String startDate, String endDate)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}
	
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setCustomTimeWithMillisecond(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, java.lang.String, java.lang.String)
	 */
	@Override
	public String setCustomTimeWithMillisecond(WebDriver webd, int index, String startDate, String endDate)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setCustomTimeWithMillisecond(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public String setCustomTimeWithMillisecond(WebDriver webd, String startDate, String endDate)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setFlexibleRelativeTimeRange(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeUnit)
	 */
	@Override
	public String setFlexibleRelativeTimeRange(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setFlexibleRelativeTimeRange(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeUnit)
	 */
	@Override
	public String setFlexibleRelativeTimeRange(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setFlexibleRelativeTimeRangeWithDateOnly(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeUnit)
	 */
	@Override
	public String setFlexibleRelativeTimeRangeWithDateOnly(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setFlexibleRelativeTimeRangeWithDateOnly(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeUnit)
	 */
	@Override
	public String setFlexibleRelativeTimeRangeWithDateOnly(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setFlexibleRelativeTimeRangeWithMillisecond(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeUnit)
	 */
	@Override
	public String setFlexibleRelativeTimeRangeWithMillisecond(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setFlexibleRelativeTimeRangeWithMillisecond(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeUnit)
	 */
	@Override
	public String setFlexibleRelativeTimeRangeWithMillisecond(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}
	
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setTimeFilter(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, java.lang.String, int[], int[])
	 */
	@Override
	public String setTimeFilter(WebDriver webd, int index, String hoursToExclude, int[] daysToExclude, int[] monthsToExclude)
			throws Exception
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setTimeFilter(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, int[], int[])
	 */
	@Override
	public String setTimeFilter(WebDriver webd, String hoursToExclude, int[] daysToExclude, int[] monthsToExclude)
			throws Exception
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setTimeRange(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeRange)
	 */
	@Override
	public String setTimeRange(WebDriver webd, int Index, TimeRange rangeoption)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setTimeRange(oracle.sysman.qatool.uifwk.webdriver.WebDriver, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeRange)
	 */
	@Override
	public String setTimeRange(WebDriver webd, TimeRange rangeoption)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setTimeRangeWithDateOnly(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeRange)
	 */
	@Override
	public String setTimeRangeWithDateOnly(WebDriver webd, int index, TimeRange rangeOption)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setTimeRangeWithDateOnly(oracle.sysman.qatool.uifwk.webdriver.WebDriver, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeRange)
	 */
	@Override
	public String setTimeRangeWithDateOnly(WebDriver webd, TimeRange rangeOption)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}
	
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setTimeRangeWithMillisecond(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeRange)
	 */
	@Override
	public String setTimeRangeWithMillisecond(WebDriver webd, int index, TimeRange rangeOption)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setTimeRangeWithMillisecond(oracle.sysman.qatool.uifwk.webdriver.WebDriver, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeRange)
	 */
	@Override
	public String setTimeRangeWithMillisecond(WebDriver webd, TimeRange rangeOption)
	{
		Assert.assertTrue(false, "This method is not available in the current version");
		webd.getLogger().info("Method not available in the current version");
		return "";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#showTopology(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void showTopology(WebDriver driver)
	{
		if (driver.isDisplayed(GLBCTXTPLDIV)) {
			driver.getLogger().info("the topology already got displayed");
		}
		else {
			driver.click(GLBCTXTBUTTON);
		}

	}

}
