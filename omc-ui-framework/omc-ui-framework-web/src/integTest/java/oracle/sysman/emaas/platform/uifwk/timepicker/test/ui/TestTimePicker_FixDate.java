/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.timepicker.test.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.CommonUIUtils;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.UIControls;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestTimePicker_FixDate extends LoginAndLogout
{
	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName, "datetimePickerIndex.html");
		CommonUIUtils.loadWebDriver(webd);
		CommonUIUtils.checkCrossDay();
	}

	@Test(alwaysRun = true)
	public void testFilterDays() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testFilterDays");

		//configure filter time
		int[] excludedDays = { 1, 3, 5, 7 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webd, null, excludedDays, null);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webd.getText(UIControls.SFILTERINFO));

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testFilterDays_compact() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testFilterDays_compact()");

		//configure filter time
		int[] excludedDays = { 2 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webd, 2, null, excludedDays, null);

		//verify the result

		Assert.assertEquals(returnFilterInfo, webd.getText(UIControls.SFILTERINFO_COMPACT));

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testFilterDaysMonths() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testFilterDaysMonths");

		//configure filter time
		int[] excludedDays = { 3, 5, 7, 2 };
		int[] excludedMonths = { 1, 2, 8, 7, 9, 11 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webd, null, excludedDays, excludedMonths);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webd.getText(UIControls.SFILTERINFO));

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testFilterDaysMonths_compact() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testFilterDaysMonths_compact");

		//configure filter time
		int[] excludedDays = { 2, 4, 6 };
		int[] excludedMonths = { 3, 6, 9, 12 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webd, 2, null, excludedDays, excludedMonths);

		//verify the result

		Assert.assertEquals(returnFilterInfo, webd.getText(UIControls.SFILTERINFO_COMPACT));

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testFilterHours() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testFilterHours");

		//configure filter time
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webd, "0-12,20-22", null, null);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webd.getText(UIControls.SFILTERINFO));

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testFilterHours_compact() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testFilterHours_compact");

		//configure filter time
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webd, 2, "5-12,20-22", null, null);

		//verify the result

		Assert.assertEquals(returnFilterInfo, webd.getText(UIControls.SFILTERINFO_COMPACT));

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testFilterHoursDays() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testFilterHoursDays");

		//configure filter time
		int[] excludedDays = { 1, 2, 3, 4 };

		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webd, "0-6,9-15", excludedDays, null);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webd.getText(UIControls.SFILTERINFO));

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testFilterHoursDays_compact() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testFilterHoursDays_compact");

		//configure filter time
		int[] excludedDays = { 4, 6 };

		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webd, 2, "0-6,9-15", excludedDays, null);

		//verify the result

		Assert.assertEquals(returnFilterInfo, webd.getText(UIControls.SFILTERINFO_COMPACT));

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testFilterHoursDaysMonths() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testFilterHoursDaysMonths");

		//configure filter time
		int[] excludedDays = { 1, 5, 6 };
		int[] excludedMonths = { 1, 2, 6, 7, 9, 11 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webd, "7-12,21-23", excludedDays, excludedMonths);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webd.getText(UIControls.SFILTERINFO));

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testFilterHoursDaysMonths_compact() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testFilterHoursDaysMonths_compact");

		//configure filter time
		int[] excludedDays = { 6 };
		int[] excludedMonths = { 2, 6, 8 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webd, 2, "1-3,7-9,21-23", excludedDays, excludedMonths);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webd.getText(UIControls.SFILTERINFO_COMPACT));

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testFilterHoursMonths() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testFilterHoursMonths");

		//configure filter time

		int[] excludedMonths = { 1, 5, 7, 9, 12 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webd, "0-5,19-22", null, excludedMonths);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webd.getText(UIControls.SFILTERINFO));

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testFilterHoursMonths_compact() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testFilterHoursMonths_compact");

		//configure filter time
		int[] excludedMonths = { 2, 3, 6, 9, 12, 11 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webd, 2, "0-5,19-22", null, excludedMonths);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webd.getText(UIControls.SFILTERINFO_COMPACT));

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testFilterMonths() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testFilterMonths");

		//configure filter time
		int[] excludedMonths = { 1, 2, 5, 6, 7, 11 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webd, null, null, excludedMonths);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webd.getText(UIControls.SFILTERINFO));

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testFilterMonths_compact() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testFilterMonths_compact");

		//configure filter time
		int[] excludedMonths = { 2, 5, 8, 11, 3, 12 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webd, 2, null, null, excludedMonths);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webd.getText(UIControls.SFILTERINFO_COMPACT));

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Custom() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Custom");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setCustomTime(webd, "04/14/2016 12:00 AM", "04/14/2016 12:30 PM");

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Custom, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false,
				false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Custom_compact() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Custom_compact");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setCustomTime(webd, 2, "04/14/2015 12:00 AM", "04/14/2016 12:30 PM");

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");

		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Custom, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false, false);

		webd.shutdownBrowser(true);
	}
	
	@Test(alwaysRun = true)
	public void testTimePicker_Custom_currentDate() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Custom_currentDate");
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		String currentDate = dateFormat.format( now ); 
		
		webd.getLogger().info("Set the end date as current day");
		String returnDate = TimeSelectorUtil.setCustomTime(webd, "04/14/2016 12:00 AM", currentDate);			
		webd.getLogger().info("Return Date:  " + returnDate);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Custom, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false,
				false);
		
		webd.getLogger().info("Set the stard date and end date as current day");
		returnDate = TimeSelectorUtil.setCustomTime(webd, currentDate, currentDate);			
		webd.getLogger().info("Return Date:  " + returnDate);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Custom, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false,
				false);

		webd.shutdownBrowser(true);			
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_Custom() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_Custom");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setCustomTimeWithDateOnly(webd, 3, "04/14/2016", "11/14/2016");

		webd.getLogger().info("Return Date:  " + returnDate);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 3, returnDate, TimeRange.Custom, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_Last12Months() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_Last12Months");

		//set time range
		webd.getLogger().info("set timerange as Last 12 Months");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 6, TimeRange.Last12Months);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 6, returnDate, TimeRange.Last12Months, UIControls.SSTARTTEXT6, UIControls.SENDTEXT6,
				true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_Last14Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_Last14Days");

		//set time range
		webd.getLogger().info("set timerange as Last 14 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 3, TimeRange.Last14Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 3, returnDate, TimeRange.Last14Days, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_Last1Day() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_Last1Day");

		//set time range
		webd.getLogger().info("set timerange as Last 1 day");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 3, TimeRange.Last1Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 3, returnDate, TimeRange.Last1Day, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_Last1Year() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_Last1Year");

		//set time range
		webd.getLogger().info("set timerange as Last 1 year");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 3, TimeRange.Last1Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 3, returnDate, TimeRange.Last1Year, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_Last24Hours() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_Last24Hours");

		//set time range
		webd.getLogger().info("set timerange as Last 24 Hours");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 6, TimeRange.Last24Hours);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 6, returnDate, TimeRange.Last24Hours, UIControls.SSTARTTEXT6, UIControls.SENDTEXT6,
				true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_Last30Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_Last30Days");

		//set time range
		webd.getLogger().info("set timerange as Last 30 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 3, TimeRange.Last30Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 3, returnDate, TimeRange.Last30Days, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_Last7Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_Last7Days");

		//set time range
		webd.getLogger().info("set timerange as Last 7 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 3, TimeRange.Last7Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 3, returnDate, TimeRange.Last7Days, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true, false);

		webd.shutdownBrowser(true);
	}

	public void testTimePicker_DateOnly_Last8Hours() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_Last8Hours");

		//set time range
		webd.getLogger().info("set timerange as Last 8 Hours");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 4, TimeRange.Last8Hours);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 4, returnDate, TimeRange.Last8Hours, UIControls.SSTARTTEXT4, UIControls.SENDTEXT4, true,
				false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_Last90Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_Last90Days");

		//set time range
		webd.getLogger().info("set timerange as Last 90 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 3, TimeRange.Last90Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 3, returnDate, TimeRange.Last90Days, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_Latest() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_Latest");

		//set time range
		webd.getLogger().info("set timerange as Latest");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 3, TimeRange.Latest);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 3, returnDate, TimeRange.Latest, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_LatestNew() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_LatestNew");

		//set time range
		webd.getLogger().info("set timerange as Latest");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 6, TimeRange.Latest);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 6, returnDate, TimeRange.Latest, UIControls.SSTARTTEXT6, UIControls.SENDTEXT6, true,
				false);

		webd.shutdownBrowser(true);
	}

	public void testTimePicker_DateOnly_NewLast60Mins() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_NewLast60Mins");

		//set time range
		webd.getLogger().info("set timerange as New Last 60 Mins");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 4, TimeRange.NewLast60Mins);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 4, returnDate, TimeRange.NewLast60Mins, UIControls.SSTARTTEXT4, UIControls.SENDTEXT4,
				true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_NewLast7Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_NewLast7Days");

		//set time range
		webd.getLogger().info("set timerange as New Last7 Days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 6, TimeRange.NewLast7Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 6, returnDate, TimeRange.NewLast7Days, UIControls.SSTARTTEXT6, UIControls.SENDTEXT6,
				true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last12Months() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last12Months");

		//set time range
		webd.getLogger().info("set timerange as Last 12 Months");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 5, TimeRange.Last12Months);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 5, returnDate, TimeRange.Last12Months, UIControls.SSTARTTEXT5, UIControls.SENDTEXT5,
				false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last14Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last14Days");

		//set time range
		webd.getLogger().info("set timerange as Last 14 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, TimeRange.Last14Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last14Days, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false,
				false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last14Days_compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last14Days_compact()");

		//set time range
		webd.getLogger().info("set timerange as Last 14 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 2, TimeRange.Last14Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");

		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Last14Days, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last15Mins() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last15Mins()");

		//set time range
		webd.getLogger().info("set timerange as Last 15 minutes");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, TimeRange.Last15Mins);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last15Mins, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false,
				false);

		webd.shutdownBrowser(true);

	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last15Mins_compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last15Mins_compact");

		//set time range
		webd.getLogger().info("set timerange as Last 15 minutes");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 2, TimeRange.Last15Mins);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");

		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Last15Mins, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false, false);

		webd.shutdownBrowser(true);

	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last1Day() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last1Day()");

		//set time range
		webd.getLogger().info("set timerange as Last 1 day");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, TimeRange.Last1Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last1Day, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false,
				false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last1Day_compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last1Day_compact()");

		//set time range
		webd.getLogger().info("set timerange as Last 1 day");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 2, TimeRange.Last1Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");

		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Last1Day, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last1Year() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last1Year()");

		//set time range
		webd.getLogger().info("set timerange as Last year");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, TimeRange.Last1Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last1Year, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false,
				false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last1Year_compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last1Year_compact()");

		//set time range
		webd.getLogger().info("set timerange as Last year");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 2, TimeRange.Last1Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");

		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Last1Year, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last24Hours() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last24Hours");

		//set time range
		webd.getLogger().info("set timerange as Last 24 hours");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 4, TimeRange.Last24Hours);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 4, returnDate, TimeRange.Last24Hours, UIControls.SSTARTTEXT4, UIControls.SENDTEXT4,
				false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last30Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last30Days()");

		//set time range
		webd.getLogger().info("set timerange as Last 30 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, TimeRange.Last30Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");

		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last30Days, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false,
				false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last30Days_compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last30Days_compact()");

		//set time range
		webd.getLogger().info("set timerange as Last 30 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 2, TimeRange.Last30Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");

		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Last30Days, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last30Mins() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last30Mins()");

		//set time range
		webd.getLogger().info("set timerange as Last 30 minutes");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 1, TimeRange.Last30Mins);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last30Mins, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false,
				false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last30Mins_compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last30Mins_compact()");

		//set time range
		webd.getLogger().info("set timerange as Last 30 minutes");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 2, TimeRange.Last30Mins);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");

		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Last30Mins, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last4Hours() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last4Hours()");

		//set time range
		webd.getLogger().info("set timerange as Last 4 hours");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, TimeRange.Last4Hours);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last4Hours, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false,
				false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last4Hours_compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last4Hours_compact()");

		//set time range
		webd.getLogger().info("set timerange as Last 4 hours");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 2, TimeRange.Last4Hours);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");

		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Last4Hours, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last60Mins() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last60Mins()");

		//set time range
		webd.getLogger().info("set timerange as Last 60 minutes");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, TimeRange.Last60Mins);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last60Mins, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false,
				false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last60Mins_compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last60Mins_compact()");

		//set time range
		webd.getLogger().info("set timerange as Last 60 minutes");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 2, TimeRange.Last60Mins);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");

		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Last60Mins, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last6Hours() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last6Hours()");

		//set time range
		webd.getLogger().info("set timerange as Last 6 hours");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, TimeRange.Last6Hours);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last6Hours, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false,
				false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last6Hours_compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last6Hours_compact()");

		//set time range
		webd.getLogger().info("set timerange as Last 6 hours");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 2, TimeRange.Last6Hours);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");

		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Last6Hours, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last7Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last7Days()");

		//set time range
		webd.getLogger().info("set timerange as Last 7 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, TimeRange.Last7Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last7Days, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false,
				false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last7Days_compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last7Days_compact()");

		//set time range
		webd.getLogger().info("set timerange as Last 7 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 2, TimeRange.Last7Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");

		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Last7Days, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last8Hours() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last8Hours()");

		//set time range
		webd.getLogger().info("set timerange as Last 8 hours");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 4, TimeRange.Last8Hours);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 4, returnDate, TimeRange.Last8Hours, UIControls.SSTARTTEXT4, UIControls.SENDTEXT4,
				false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last90Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last90Days()");

		//set time range
		webd.getLogger().info("set timerange as Last 90 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, TimeRange.Last90Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last90Days, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false,
				false);
		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last90Days_compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last90Days_compact()");

		//set time range
		webd.getLogger().info("set timerange as Last 90 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 2, TimeRange.Last90Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");

		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Last90Days, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Latest() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Latest()");

		//set time range
		webd.getLogger().info("set timerange as Latest");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, TimeRange.Latest);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Latest, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false,
				false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Latest_compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Latest_compact()");

		//set time range
		webd.getLogger().info("set timerange as Latest");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 2, TimeRange.Latest);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");

		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Latest, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_LatestNew() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_LatestNew()");

		//set time range
		webd.getLogger().info("set timerange as Latest");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 4, TimeRange.Latest);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 4, returnDate, TimeRange.Latest, UIControls.SSTARTTEXT4, UIControls.SENDTEXT4, false,
				false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_NewLast60Mins() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_NewLast60Mins()");

		//set time range
		webd.getLogger().info("set timerange as Last 60 minutes");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 4, TimeRange.NewLast60Mins);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 4, returnDate, TimeRange.NewLast60Mins, UIControls.SSTARTTEXT4, UIControls.SENDTEXT4,
				false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_NewLast7Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_NewLast7Days()");

		//set time range
		webd.getLogger().info("set timerange as new Last 7 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webd, 5, TimeRange.NewLast7Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 5, returnDate, TimeRange.NewLast7Days, UIControls.SSTARTTEXT5, UIControls.SENDTEXT5,
				false, false);

		webd.shutdownBrowser(true);
	}
}
