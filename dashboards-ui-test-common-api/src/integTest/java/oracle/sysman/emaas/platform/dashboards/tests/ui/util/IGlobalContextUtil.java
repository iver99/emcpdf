/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.util;

import java.util.Date;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeUnit;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public interface IGlobalContextUtil extends IUiTestCommonAPI
{

	/**
	 * Generate Global Context Url
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public String generateUrlWithGlobalContext(WebDriver driver, String baseUrl, String compositeMeid, String timePeriod,
			Date startTime, Date endTime, List<String> entityMeids);

	/**
	 * Get GlobalContext Meid
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public String getGlobalContextMeid(WebDriver driver, String baseurl);

	/**
	 * Get the name of global context
	 *
	 * @param driver
	 *            WebDriver instance
	 */

	public String getGlobalContextName(WebDriver driver);

	public String getTimeRangeLabel(WebDriver webd);

	public String getTimeRangeLabel(WebDriver webd, int index);

	public String getTimeRangeLabel_V2(WebDriver webd);

	public String getTimeRangeLabel_V2(WebDriver webd, int index);
	
	/**
	 * Hide Topology
	 *
	 * @param driver
	 *            WebDriver instance
	 */

	public void hideTopology(WebDriver driver);

	/**
	 * Check whether Global Context Existed
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean isGlobalContextExisted(WebDriver driver);

	public String setCustomTime(WebDriver webd, int index, String startDateTime, String endDateTime);

	//Date MM/dd/yyyy
	//Time hh:mm a
	public String setCustomTime(WebDriver webd, String startDateTime, String endDateTime);

	public String setCustomTimeWithDateOnly(WebDriver webd, int index, String startDate, String endDate);

	public String setCustomTimeWithDateOnly(WebDriver webd, String startDate, String endDate);

	public String setCustomTimeWithMillisecond(WebDriver webd, int index, String startDateTime, String endDateTime);

	public String setCustomTimeWithMillisecond(WebDriver webd, String startDateTime, String endDateTime);

	public String setFlexibleRelativeTimeRange(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit);

	public String setFlexibleRelativeTimeRange(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit);

	public String setFlexibleRelativeTimeRangeWithDateOnly(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit);

	public String setFlexibleRelativeTimeRangeWithDateOnly(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit);

	public String setFlexibleRelativeTimeRangeWithMillisecond(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit);

	public String setFlexibleRelativeTimeRangeWithMillisecond(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit);

	public String setTimeFilter(WebDriver webd, int index, String hoursToExclude, int[] daysToExclude, int[] monthsToExclude)
			throws Exception;

	public String setTimeFilter(WebDriver webd, String hoursToExclude, int[] daysToExclude, int[] monthsToExclude)
			throws Exception;

	public String setTimeRange(WebDriver webd, int Index, TimeRange rangeoption);

	public String setTimeRange(WebDriver webd, TimeRange rangeoption);

	public String setTimeRangeWithDateOnly(WebDriver webd, int index, TimeRange rangeOption);

	public String setTimeRangeWithDateOnly(WebDriver webd, TimeRange rangeOption);
	
	public String setTimeRangeWithMillisecond(WebDriver webd, int index, TimeRange rangeOption);

	public String setTimeRangeWithMillisecond(WebDriver webd, TimeRange rangeOption);

	/**
	 * Show Topology
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public void showTopology(WebDriver driver);

}
