/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.util.Date;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeUnit;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.UtilLoader;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author cawei
 */
public class GlobalContextUtil
{
	/**
	 * Generate Global Context Url
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public static String generateUrlWithGlobalContext(WebDriver driver, String baseUrl, String compositeMeid, String timePeriod,
			Date startTime, Date endTime, List<String> entityMeids)
	{
		IGlobalContextUtil gcu = new UtilLoader<IGlobalContextUtil>().loadUtil(driver, IGlobalContextUtil.class);
		return gcu.generateUrlWithGlobalContext(driver, baseUrl, compositeMeid, timePeriod, startTime, endTime, entityMeids);
	}

	/**
	 * Get GlobalContext Meid
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public static String getGlobalContextMeid(WebDriver driver, String baseurl)
	{
		IGlobalContextUtil gcu = new UtilLoader<IGlobalContextUtil>().loadUtil(driver, IGlobalContextUtil.class);
		return gcu.getGlobalContextMeid(driver, baseurl);
	}

	/**
	 * Get the name of global context
	 *
	 * @param driver
	 *            WebDriver instance
	 */

	public static String getGlobalContextName(WebDriver driver)
	{
		IGlobalContextUtil gcu = new UtilLoader<IGlobalContextUtil>().loadUtil(driver, IGlobalContextUtil.class);
		return gcu.getGlobalContextName(driver);
	}

	public static String getTimeRangeLabel(WebDriver webd)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.getTimeRangeLabel(webd);
	}

	public static String getTimeRangeLabel(WebDriver webd, int index)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.getTimeRangeLabel(webd, index);
	}
	
	public static String getTimeRangeLabel_V2(WebDriver webd)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.getTimeRangeLabel_V2(webd);
	}

	public static String getTimeRangeLabel_V2(WebDriver webd, int index)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.getTimeRangeLabel_V2(webd, index);
	}

	/**
	 * Hide Topology
	 *
	 * @param driver
	 *            WebDriver instance
	 */

	public static void hideTopology(WebDriver driver)
	{
		IGlobalContextUtil gcu = new UtilLoader<IGlobalContextUtil>().loadUtil(driver, IGlobalContextUtil.class);
		gcu.hideTopology(driver);
	}

	/**
	 * Check whether Global Context Existed
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static boolean isGlobalContextExisted(WebDriver driver)
	{
		IGlobalContextUtil gcu = new UtilLoader<IGlobalContextUtil>().loadUtil(driver, IGlobalContextUtil.class);
		return gcu.isGlobalContextExisted(driver);
	}

	public static String setCustomTime(WebDriver webd, int index, String startDateTime, String endDateTime) throws Exception
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setCustomTime(webd, index, startDateTime, endDateTime);
	}

	//Date MM/dd/yyyy
	//Time hh:mm a
	public static String setCustomTime(WebDriver webd, String startDateTime, String endDateTime)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setCustomTime(webd, startDateTime, endDateTime);
	}

	public static String setCustomTimeWithDateOnly(WebDriver webd, int index, String startDateTime, String endDateTime)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setCustomTimeWithDateOnly(webd, index, startDateTime, endDateTime);
	}

	public static String setCustomTimeWithDateOnly(WebDriver webd, String startDateTime, String endDateTime)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setCustomTimeWithDateOnly(webd, startDateTime, endDateTime);
	}
	
	public static String setCustomTimeWithMillisecond(WebDriver webd, int index, String startDateTime, String endDateTime)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setCustomTimeWithMillisecond(webd, index, startDateTime, endDateTime);
	}

	public static String setCustomTimeWithMillisecond(WebDriver webd, String startDateTime, String endDateTime)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setCustomTimeWithMillisecond(webd, startDateTime, endDateTime);
	}

	public static String setFlexibleRelativeTimeRange(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setFlexibleRelativeTimeRange(webd, index, relTimeVal, relTimeUnit);
	}

	public static String setFlexibleRelativeTimeRange(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setFlexibleRelativeTimeRange(webd, relTimeVal, relTimeUnit);
	}

	public static String setFlexibleRelativeTimeRangeWithDateOnly(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setFlexibleRelativeTimeRangeWithDateOnly(webd, index, relTimeVal, relTimeUnit);
	}

	public static String setFlexibleRelativeTimeRangeWithDateOnly(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setFlexibleRelativeTimeRangeWithDateOnly(webd, relTimeVal, relTimeUnit);
	}
	
	public static String setFlexibleRelativeTimeRangeWithMillisecond(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setFlexibleRelativeTimeRangeWithMillisecond(webd, index, relTimeVal, relTimeUnit);
	}

	public static String setFlexibleRelativeTimeRangeWithMillisecond(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setFlexibleRelativeTimeRangeWithMillisecond(webd, relTimeVal, relTimeUnit);
	}

	public static String setTimeFilter(WebDriver webd, int index, String hoursToExclude, int[] daysToExclude,
			int[] monthsToExclude) throws Exception
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setTimeFilter(webd, index, hoursToExclude, daysToExclude, monthsToExclude);
	}

	public static String setTimeFilter(WebDriver webd, String hoursToExclude, int[] daysToExclude, int[] monthsToExclude)
			throws Exception

	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setTimeFilter(webd, hoursToExclude, daysToExclude, monthsToExclude);
	}

	public static String setTimeRange(WebDriver webd, int index, TimeRange rangeoption)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setTimeRange(webd, index, rangeoption);
	}

	public static String setTimeRange(WebDriver webd, TimeRange rangeoption)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setTimeRange(webd, rangeoption);
	}

	public static String setTimeRangeWithDateOnly(WebDriver webd, int index, TimeRange rangeOption)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setTimeRangeWithDateOnly(webd, index, rangeOption);
	}

	public static String setTimeRangeWithDateOnly(WebDriver webd, TimeRange rangeOption)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setTimeRangeWithDateOnly(webd, rangeOption);
	}
	
	public static String setTimeRangeWithMillisecond(WebDriver webd, int index, TimeRange rangeOption)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setTimeRangeWithMillisecond(webd, index, rangeOption);
	}

	public static String setTimeRangeWithMillisecond(WebDriver webd, TimeRange rangeOption)
	{
		IGlobalContextUtil tsu = new UtilLoader<IGlobalContextUtil>().loadUtil(webd, IGlobalContextUtil.class);
		return tsu.setTimeRangeWithMillisecond(webd, rangeOption);
	}

	/**
	 * Show Topology
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public static void showTopology(WebDriver driver)
	{
		IGlobalContextUtil gcu = new UtilLoader<IGlobalContextUtil>().loadUtil(driver, IGlobalContextUtil.class);
		gcu.showTopology(driver);
	}

	private void GobalContextUtil()
	{
	}
}
