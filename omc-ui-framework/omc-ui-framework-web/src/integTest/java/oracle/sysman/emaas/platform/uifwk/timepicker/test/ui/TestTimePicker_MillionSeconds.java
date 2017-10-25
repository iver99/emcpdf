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
import java.util.Date;

import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeUnit;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.CommonUIUtils;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.UIControls;

import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestTimePicker_MillionSeconds extends LoginAndLogout
{
	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName, "timeSelectorMilliseconds.html");
		CommonUIUtils.loadWebDriver(webd);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Custom_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Custom_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setCustomTimeWithMillisecond(webd, 2, "10/10/2016 12:00:00.000 AM",
				"12/14/2016 12:30:59.000 PM");

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Custom, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}
	
	@Test(alwaysRun = true)
	public void testTimePicker_Custom_LongTerm_currentDate() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Custom_LongTerm_currentDate");
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS a");
		String currentDate = dateFormat.format( now ); 
		
		webd.getLogger().info("Set the end date as current day");
		String returnDate = TimeSelectorUtil.setCustomTimeWithMillisecond(webd, 2, "10/10/2016 12:00:00.000 AM", currentDate);			
		webd.getLogger().info("Return Date:  " + returnDate);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Custom, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);
		
		webd.getLogger().info("Set the stard date and end date as current day");
		returnDate = TimeSelectorUtil.setCustomTimeWithMillisecond(webd, 2, currentDate, currentDate);		
		webd.getLogger().info("Return Date:  " + returnDate);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Custom, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Custom_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Custom");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setCustomTimeWithMillisecond(webd, "04/14/2016 12:00:00.000 AM",
				"04/14/2016 12:30:59.000 PM");

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Custom, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}
	
	@Test(alwaysRun = true)
	public void testTimePicker_Custom_ShortTerm_currentDate() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Custom_ShortTerm_currentDate");
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS a");
		String currentDate = dateFormat.format( now ); 
		
		webd.getLogger().info("Set the end date as current day");
		String returnDate = TimeSelectorUtil.setCustomTimeWithMillisecond(webd, "04/14/2016 12:00:00.000 AM", currentDate);			
		webd.getLogger().info("Return Date:  " + returnDate);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Custom, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);
		
		webd.getLogger().info("Set the stard date and end date as current day");
		returnDate = TimeSelectorUtil.setCustomTimeWithMillisecond(webd, 1, currentDate, currentDate);		
		webd.getLogger().info("Return Date:  " + returnDate);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Custom, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Day_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Day_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 1, TimeUnit.Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Day_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Day_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 1, TimeUnit.Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Days_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Days_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 12, TimeUnit.Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Days_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Days_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, TimeUnit.Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Hour_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Hour_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 1, TimeUnit.Hour);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Hour_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Hour_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 1, TimeUnit.Hour);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Hours_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Hours_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 27, TimeUnit.Hour);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Hours_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Hours_ShortTerm()");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, TimeUnit.Hour);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Minute_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Minute_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 1, TimeUnit.Minute);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Minute_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Minute_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 1, TimeUnit.Minute);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Minutes_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Minutes_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 28, TimeUnit.Minute);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Minutes_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Minutes_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, TimeUnit.Minute);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Month_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Month_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 1, TimeUnit.Month);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Month_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Month_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 1, TimeUnit.Month);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Months_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Months_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 5, TimeUnit.Month);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Months_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Months_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, TimeUnit.Month);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Second_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Second_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 1, TimeUnit.Second);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Second_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Second_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 1, TimeUnit.Second);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Seconds_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Seconds_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 79, TimeUnit.Second);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Seconds_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Seconds_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, TimeUnit.Second);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Week_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Week_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 1, TimeUnit.Week);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Week_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Week_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 1, TimeUnit.Week);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Weeks_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Weeks_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 4, TimeUnit.Week);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Weeks_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Weeks_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, TimeUnit.Week);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Year_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Year_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 1, TimeUnit.Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Year_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Year_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 1, TimeUnit.Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Years_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Years_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 2, TimeUnit.Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Years_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Years_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, TimeUnit.Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last12Months() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last12Months");

		//set time range
		webd.getLogger().info("set timerange as Last 12 Months");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, 2, TimeRange.Last12Months);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last12Months, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last14Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last14Days");

		//set time range
		webd.getLogger().info("set timerange as Last 14 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, 2, TimeRange.Last14Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last14Days, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last15Mins() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last15Mins");

		//set time range
		webd.getLogger().info("set timerange as Last 15 minutes");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, TimeRange.Last15Mins);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last15Mins, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last24Hours() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last24Hours");

		//set time range
		webd.getLogger().info("set timerange as Last 24 hours");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, 1, TimeRange.Last24Hours);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last24Hours, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last30Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last30Days");

		//set time range
		webd.getLogger().info("set timerange as Last 30 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, 2, TimeRange.Last30Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last30Days, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last30Mins() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last30Mins");

		//set time range
		webd.getLogger().info("set timerange as Last 30 minutes");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, TimeRange.Last30Mins);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last30Mins, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last60Mins() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last60Mins");

		//set time range
		webd.getLogger().info("set timerange as Last 60 minutes");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, TimeRange.NewLast60Mins);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.NewLast60Mins, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last7Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last7Days");

		//set time range
		webd.getLogger().info("set timerange as new Last 7 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, 2, TimeRange.NewLast7Days);
		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.NewLast7Days, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last8Hours() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last8Hours");

		//set time range
		webd.getLogger().info("set timerange as Last 8 hours");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, TimeRange.Last8Hours);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last8Hours, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last90Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last90Days");

		//set time range
		webd.getLogger().info("set timerange as Last 90 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, 2, TimeRange.Last90Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last90Days, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);
		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Latest_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Latest_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Latest");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, 2, TimeRange.Latest);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Latest, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false, true);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Latest_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Latest_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Latest");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, TimeRange.Latest);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Latest, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false, true);

		webd.shutdownBrowser(true);
	}
}
