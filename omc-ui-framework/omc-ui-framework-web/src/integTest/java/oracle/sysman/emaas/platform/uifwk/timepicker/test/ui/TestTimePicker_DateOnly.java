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

import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeUnit;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.CommonUIUtils;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.UIControls;

import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author shangwan
 */
public class TestTimePicker_DateOnly extends LoginAndLogout
{
	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName, "timeSelectorDateOnly.html");
		CommonUIUtils.loadWebDriver(webd);
		Calendar now = Calendar.getInstance();
		int minute = now.get(Calendar.MINUTE);
		if(minute>=55)
		{
			try {
				Thread.sleep(300000);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
	}
	
	@Test(alwaysRun = true)
	public void testTimePicker_Custom() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Custom");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setCustomTimeWithDateOnly(webd, 1, "04/14/2016", "11/14/2016");

		webd.getLogger().info("Return Date:  " + returnDate);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Custom, UIControls.SSTARTTEXT_DATEONLY1,
				UIControls.SENDTEXT_DATEONLY1, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Custom_Compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Custom_Compact");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setCustomTimeWithDateOnly(webd, 2, "04/14/2016", "11/14/2016");

		webd.getLogger().info("Return Date:  " + returnDate);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Custom, UIControls.SSTARTTEXT_DATEONLY2,
				UIControls.SENDTEXT_DATEONLY2, true, false);

		webd.shutdownBrowser(true);
	}
	
	@Test(alwaysRun = true)
	public void testTimePicker_Custom_CurrentDate() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Custom_CurrentDate");
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String currentDate = dateFormat.format( now ); 
		
		webd.getLogger().info("Set the end date as current day");
		String returnDate = TimeSelectorUtil.setCustomTimeWithDateOnly(webd, 3, "04/14/2016", currentDate);			
		webd.getLogger().info("Return Date:  " + returnDate);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Custom, UIControls.SSTARTTEXT_DATEONLY3,
				UIControls.SENDTEXT_DATEONLY3, true, false);
		
		webd.getLogger().info("Set the stard date and end date as current day");
		returnDate = TimeSelectorUtil.setCustomTimeWithDateOnly(webd, 3, currentDate, currentDate);			
		webd.getLogger().info("Return Date:  " + returnDate);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Custom, UIControls.SSTARTTEXT_DATEONLY3,
				UIControls.SENDTEXT_DATEONLY3, true, false);

		webd.shutdownBrowser(true);	
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Day() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Day");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 1, 1, TimeUnit.Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_DATEONLY1,
				UIControls.SENDTEXT_DATEONLY1, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Day_Compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Day_Compact");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 2, 1, TimeUnit.Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_DATEONLY2,
				UIControls.SENDTEXT_DATEONLY2, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_FewDays() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_FewDays");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 3, TimeUnit.Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_DATEONLY1,
				UIControls.SENDTEXT_DATEONLY1, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_FewDays_Compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_FewDays_Compact");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 2, 3, TimeUnit.Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_DATEONLY2,
				UIControls.SENDTEXT_DATEONLY2, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_FewMonths() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_FewMonths");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 6, TimeUnit.Month);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_DATEONLY1,
				UIControls.SENDTEXT_DATEONLY1, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_FewMonths_Compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_FewMonths_Compact");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 2, 6, TimeUnit.Month);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_DATEONLY2,
				UIControls.SENDTEXT_DATEONLY2, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_FewWeeks() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_FewWeeks");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 4, TimeUnit.Week);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_DATEONLY1,
				UIControls.SENDTEXT_DATEONLY1, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_FewWeeks_Compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_FewWeeks_Compact");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 2, 4, TimeUnit.Week);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_DATEONLY2,
				UIControls.SENDTEXT_DATEONLY2, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_FewYears() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_FewYears");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 1, 4, TimeUnit.Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_DATEONLY1,
				UIControls.SENDTEXT_DATEONLY1, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_FewYears_Compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_FewYears_Compact");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 2, 4, TimeUnit.Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_DATEONLY2,
				UIControls.SENDTEXT_DATEONLY2, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Month() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Month");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 1, TimeUnit.Month);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_DATEONLY1,
				UIControls.SENDTEXT_DATEONLY1, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Month_Compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Month_Compact");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 2, 1, TimeUnit.Month);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_DATEONLY2,
				UIControls.SENDTEXT_DATEONLY2, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Week() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Week");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 1, 1, TimeUnit.Week);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_DATEONLY1,
				UIControls.SENDTEXT_DATEONLY1, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Week_Compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Week_Compact");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 2, 1, TimeUnit.Week);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_DATEONLY2,
				UIControls.SENDTEXT_DATEONLY2, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Year() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Year");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 1, 1, TimeUnit.Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT_DATEONLY1,
				UIControls.SENDTEXT_DATEONLY1, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Year_Compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Year_Compact");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 2, 1, TimeUnit.Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_DATEONLY2,
				UIControls.SENDTEXT_DATEONLY2, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last12Months() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last12Months");

		//set time range
		webd.getLogger().info("set timerange as Last 12 Months");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, TimeRange.Last12Months);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last12Months, UIControls.SSTARTTEXT_DATEONLY1,
				UIControls.SENDTEXT_DATEONLY1, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last12Months_Compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last12Months_Compact");

		//set time range
		webd.getLogger().info("set timerange as Last 12 Months");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 2, TimeRange.Last12Months);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Last12Months, UIControls.SSTARTTEXT_DATEONLY2,
				UIControls.SENDTEXT_DATEONLY2, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last14Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last14Days");

		//set time range
		webd.getLogger().info("set timerange as Last 14 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, TimeRange.Last14Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Last14Days, UIControls.SSTARTTEXT_DATEONLY1,
				UIControls.SENDTEXT_DATEONLY1, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last14Days_Compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last14Days_Compact");

		//set time range
		webd.getLogger().info("set timerange as Last 14 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 2, TimeRange.Last14Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Last14Days, UIControls.SSTARTTEXT_DATEONLY2,
				UIControls.SENDTEXT_DATEONLY2, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last24Hours() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last24Hours");

		//set time range
		webd.getLogger().info("set timerange as Last 24 Hours");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 1, TimeRange.Last24Hours);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last24Hours, UIControls.SSTARTTEXT_DATEONLY1,
				UIControls.SENDTEXT_DATEONLY1, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last24Hours_Compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last24Hours_Compact");

		//set time range
		webd.getLogger().info("set timerange as Last 24 Hours");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 2, TimeRange.Last24Hours);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Last24Hours, UIControls.SSTARTTEXT_DATEONLY2,
				UIControls.SENDTEXT_DATEONLY2, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last30Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last30Days");

		//set time range
		webd.getLogger().info("set timerange as Last 30 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 1, TimeRange.Last30Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last30Days, UIControls.SSTARTTEXT_DATEONLY1,
				UIControls.SENDTEXT_DATEONLY1, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last30Days_Compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last30Days_Compact");

		//set time range
		webd.getLogger().info("set timerange as Last 30 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 2, TimeRange.Last30Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Last30Days, UIControls.SSTARTTEXT_DATEONLY2,
				UIControls.SENDTEXT_DATEONLY2, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last7Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last7Days");

		//set time range
		webd.getLogger().info("set timerange as New Last7 Days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 1, TimeRange.NewLast7Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.NewLast7Days, UIControls.SSTARTTEXT_DATEONLY1,
				UIControls.SENDTEXT_DATEONLY1, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last7Days_Compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last7Days_Compact");

		//set time range
		webd.getLogger().info("set timerange as New Last7 Days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 2, TimeRange.NewLast7Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.NewLast7Days, UIControls.SSTARTTEXT_DATEONLY2,
				UIControls.SENDTEXT_DATEONLY2, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last90Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last90Days");

		//set time range
		webd.getLogger().info("set timerange as Last 90 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, TimeRange.Last90Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 1, returnDate, TimeRange.Last90Days, UIControls.SSTARTTEXT_DATEONLY1,
				UIControls.SENDTEXT_DATEONLY1, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last90Days_Compact() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last90Days_Compact");

		//set time range
		webd.getLogger().info("set timerange as Last 90 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webd, 2, TimeRange.Last90Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult(webd, 2, returnDate, TimeRange.Last90Days, UIControls.SSTARTTEXT_DATEONLY2,
				UIControls.SENDTEXT_DATEONLY2, true, false);

		webd.shutdownBrowser(true);
	}
}
