/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.timepicker.test.ui;

import java.text.ParseException;
import java.util.Calendar;

import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeUnit;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.CommonUIUtils;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.UIControls;

import org.testng.annotations.Test;

/**
 * @author shangwan
 */

public class TestTimePicker_FlexibleDate extends LoginAndLogout
{
	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName, "datetimePickerIndex.html");
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
	public void testTimePicker_Compact_FlexibleDate_Day() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Compact_FlexibleDate_Day");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, 1, TimeUnit.Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_COMPACT, UIControls.SENDTEXT_COMPACT,
				false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Compact_FlexibleDate_FewDays() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Compact_FlexibleDate_FewDays");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, 5, TimeUnit.Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_COMPACT, UIControls.SENDTEXT_COMPACT,
				false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Compact_FlexibleDate_FewHours() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Compact_FlexibleDate_FewHours");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, 23, TimeUnit.Hour);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_COMPACT, UIControls.SENDTEXT_COMPACT,
				false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Compact_FlexibleDate_FewMinutes() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Compact_FlexibleDate_FewMinutes");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, 60, TimeUnit.Minute);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_COMPACT, UIControls.SENDTEXT_COMPACT,
				false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Compact_FlexibleDate_FewMonths() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Compact_FlexibleDate_FewMonths");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, 11, TimeUnit.Month);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_COMPACT, UIControls.SENDTEXT_COMPACT,
				false, false);

		webd.shutdownBrowser(true);
	}

	//@Test(alwaysRun = true)
	public void testTimePicker_Compact_FlexibleDate_FewSeconds() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Compact_FlexibleDate_FewSeconds");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, 20, TimeUnit.Second);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_COMPACT, UIControls.SENDTEXT_COMPACT,
				false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Compact_FlexibleDate_FewWeeks() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Compact_FlexibleDate_FewWeeks");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, 4, TimeUnit.Week);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_COMPACT, UIControls.SENDTEXT_COMPACT,
				false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Compact_FlexibleDate_FewYears() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Compact_FlexibleDate_FewYears");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, 2, TimeUnit.Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_COMPACT, UIControls.SENDTEXT_COMPACT,
				false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Compact_FlexibleDate_Hour() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Compact_FlexibleDate_Hour");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, 1, TimeUnit.Hour);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_COMPACT, UIControls.SENDTEXT_COMPACT,
				false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Compact_FlexibleDate_Minute() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Compact_FlexibleDate_Minute");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, 1, TimeUnit.Minute);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_COMPACT, UIControls.SENDTEXT_COMPACT,
				false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Compact_FlexibleDate_Month() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Compact_FlexibleDate_Month");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, 1, TimeUnit.Month);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_COMPACT, UIControls.SENDTEXT_COMPACT,
				false, false);

		webd.shutdownBrowser(true);
	}

	//@Test(alwaysRun = true)
	public void testTimePicker_Compact_FlexibleDate_Second() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testtestTimePicker_Compact_FlexibleDate_SecondFilterDays");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, 1, TimeUnit.Second);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_COMPACT, UIControls.SENDTEXT_COMPACT,
				false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Compact_FlexibleDate_Week() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Compact_FlexibleDate_Week");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, 1, TimeUnit.Week);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_COMPACT, UIControls.SENDTEXT_COMPACT,
				false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Compact_FlexibleDate_Year() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Compact_FlexibleDate_Year");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, 1, TimeUnit.Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 2, returnDate, UIControls.SSTARTTEXT_COMPACT, UIControls.SENDTEXT_COMPACT,
				false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_FlexibleDate_Day() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_FlexibleDate_Day");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 3, 1, TimeUnit.Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 3, returnDate, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_FlexibleDate_FewDays() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_FlexibleDate_FewDays");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 3, 3, TimeUnit.Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 3, returnDate, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_FlexibleDate_FewMonths() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_FlexibleDate_FewMonths");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 3, 6, TimeUnit.Month);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 3, returnDate, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_FlexibleDate_FewWeeks() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_FlexibleDate_FewWeeks");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 3, 4, TimeUnit.Week);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 3, returnDate, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_FlexibleDate_FewYears() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_FlexibleDate_FewYears");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 3, 4, TimeUnit.Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 3, returnDate, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_FlexibleDate_Month() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_FlexibleDate_Month");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 3, 1, TimeUnit.Month);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 3, returnDate, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_FlexibleDate_Week() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_FlexibleDate_Week");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 3, 1, TimeUnit.Week);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 3, returnDate, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_FlexibleDate_Year() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_FlexibleDate_Year");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, 3, 1, TimeUnit.Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 3, returnDate, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Day() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Day");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 1, TimeUnit.Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_FewDays() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_FewDays");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, TimeUnit.Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_FewHours() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_FewHours");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, TimeUnit.Hour);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_FewMinutes() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_FewMinutes");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, TimeUnit.Minute);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_FewMonths() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_FewMonths");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, TimeUnit.Month);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_FewWeeks() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_FewWeeks");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, TimeUnit.Week);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_FewYears() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_FewYears");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, TimeUnit.Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Hour() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Hour()");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 1, TimeUnit.Hour);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Minute() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Minute");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 1, TimeUnit.Minute);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Month() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Month()");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 1, TimeUnit.Month);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Week() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Week");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 1, TimeUnit.Week);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Year() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Year");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 1, TimeUnit.Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		CommonUIUtils.verifyResult_FlexibleDate(webd, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false, false);

		webd.shutdownBrowser(true);
	}
}
