/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeUnit;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author cawei
 */
public class GlobalContextUtil_1150 extends GlobalContextUtil_1130
{
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#getTimeRangeLabel(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public String getTimeRangeLabel(WebDriver webd)
	{
		return TimeSelectorUtil.getTimeRangeLabel(webd);

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#getTimeRangeLabel(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int)
	 */
	@Override
	public String getTimeRangeLabel(WebDriver webd, int index)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.getTimeRangeLabel(webd, index);
	}
	
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#getTimeRangeLabel(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public String getTimeRangeLabel_V2(WebDriver webd)
	{
		return TimeSelectorUtil.getTimeRangeLabel_V2(webd);

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#getTimeRangeLabel(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int)
	 */
	@Override
	public String getTimeRangeLabel_V2(WebDriver webd, int index)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.getTimeRangeLabel_V2(webd, index);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setCustomTime(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, java.lang.String, java.lang.String)
	 */
	@Override
	public String setCustomTime(WebDriver webd, int index, String startDateTime, String endDateTime)
	{
		// TODO Auto-generated method stub
		String CUSTOM = null;
		try {
			CUSTOM = TimeSelectorUtil.setCustomTime(webd, index, startDateTime, endDateTime);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return CUSTOM;

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setCustomTime(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public String setCustomTime(WebDriver webd, String startDateTime, String endDateTime)
	{
		return TimeSelectorUtil.setCustomTime(webd, startDateTime, endDateTime);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setCustomTimeWithDateOnly(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, java.lang.String, java.lang.String)
	 */
	@Override
	public String setCustomTimeWithDateOnly(WebDriver webd, int index, String startDate, String endDate)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setCustomTimeWithDateOnly(webd, index, startDate, endDate);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setCustomTimeWithDateOnly(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public String setCustomTimeWithDateOnly(WebDriver webd, String startDate, String endDate)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setCustomTimeWithDateOnly(webd, startDate, endDate);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setFlexibleRelativeTimeRange(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeUnit)
	 */
	@Override
	public String setFlexibleRelativeTimeRange(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, index, relTimeVal, relTimeUnit);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setFlexibleRelativeTimeRange(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeUnit)
	 */
	@Override
	public String setFlexibleRelativeTimeRange(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, relTimeVal, relTimeUnit);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setFlexibleRelativeTimeRangeWithDateOnly(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeUnit)
	 */
	@Override
	public String setFlexibleRelativeTimeRangeWithDateOnly(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, index, relTimeVal, relTimeUnit);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setFlexibleRelativeTimeRangeWithDateOnly(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeUnit)
	 */
	@Override
	public String setFlexibleRelativeTimeRangeWithDateOnly(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, relTimeVal, relTimeUnit);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setTimeFilter(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, java.lang.String, int[], int[])
	 */
	@Override
	public String setTimeFilter(WebDriver webd, int index, String hoursToExclude, int[] daysToExclude, int[] monthsToExclude)
			throws Exception
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setTimeFilter(webd, index, hoursToExclude, daysToExclude, monthsToExclude);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setTimeFilter(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, int[], int[])
	 */
	@Override
	public String setTimeFilter(WebDriver webd, String hoursToExclude, int[] daysToExclude, int[] monthsToExclude)
			throws Exception
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setTimeFilter(webd, hoursToExclude, daysToExclude, monthsToExclude);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setTimeRange(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeRange)
	 */
	@Override
	public String setTimeRange(WebDriver webd, int Index, TimeRange rangeoption)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setTimeRange(webd, Index, rangeoption);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setTimeRange(oracle.sysman.qatool.uifwk.webdriver.WebDriver, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeRange)
	 */
	@Override
	public String setTimeRange(WebDriver webd, TimeRange rangeoption)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setTimeRange(webd, rangeoption);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setTimeRangeWithDateOnly(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeRange)
	 */
	@Override
	public String setTimeRangeWithDateOnly(WebDriver webd, int index, TimeRange rangeOption)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setTimeRangeWithDateOnly(webd, index, rangeOption);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setTimeRangeWithDateOnly(oracle.sysman.qatool.uifwk.webdriver.WebDriver, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeRange)
	 */
	@Override
	public String setTimeRangeWithDateOnly(WebDriver webd, TimeRange rangeOption)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setTimeRangeWithDateOnly(webd, rangeOption);
	}
}
