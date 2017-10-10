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

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author shangwan
 */
public class TestTimePicker_ErrorMsg extends LoginAndLogout
{
	private static String errormsg_1 = "Date and time must be on or sooner than current date and time.";
	private static String errormsg_2 = "The start date/time is after the end date/time.";
	private static String errormsg_3 = "Date and time must be on or later than";
	private static String errormsg_4 = "Date and time must be on or sooner than";
	
	public void initTest(String testName, String url)
	{
		login(this.getClass().getName() + "." + testName, url);
		CommonUIUtils.loadWebDriver(webd);
	}
	
	@Test(alwaysRun = true)
	public void testTimePicker_StartDateAfterCurrentDate() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "datetimePickerIndex.html");
		webd.getLogger().info("Start the test case: testTimePicker_StartDateAfterCurrentDate");
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		String currentDate = dateFormat.format( now ); 
		
		Calendar c = Calendar.getInstance();
		
		c.setTime(now);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day+1);
		
		String dayAfter = dateFormat.format(c.getTime());
		
		webd.getLogger().info("Set the end date as current day");
		String returnmsg = CommonUIUtils.verifyErrorMsg(webd, 1, dayAfter, currentDate, false, false);			
		webd.getLogger().info("Return Date:  " + returnmsg);
		
		Assert.assertTrue(errormsg_2.equals(returnmsg.trim()) || returnmsg.trim().contains(errormsg_4));

		webd.shutdownBrowser(true);	
	}
	
	@Test(alwaysRun = true)
	public void testTimePicker_EndDateAfterCurrentDate() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "datetimePickerIndex.html");
		webd.getLogger().info("Start the test case: testTimePicker_EndDateAfterCurrentDate");
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		String currentDate = dateFormat.format( now ); 
		
		Calendar c = Calendar.getInstance();
		
		c.setTime(now);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day+1);
		
		String dayAfter = dateFormat.format(c.getTime());
		
		webd.getLogger().info("Set the end date as current day");
		String returnmsg = CommonUIUtils.verifyErrorMsg(webd, 1, currentDate, dayAfter, false, false);			
		webd.getLogger().info("Return Date:  " + returnmsg);
		
		Assert.assertTrue(errormsg_1.equals(returnmsg.trim()) || returnmsg.trim().contains(errormsg_4));

		webd.shutdownBrowser(true);	
	}
	
	@Test(alwaysRun = true)
	public void testTimePicker_EndDateBeforeStartDate() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "datetimePickerIndex.html");
		webd.getLogger().info("Start the test case: testTimePicker_EndDateBeforeStartDate");
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		String currentDate = dateFormat.format( now ); 
		
		Calendar c = Calendar.getInstance();
		
		c.setTime(now);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day-1);
		
		String dayBefore = dateFormat.format(c.getTime());
		
		webd.getLogger().info("Set the end date as current day");
		String returnmsg = CommonUIUtils.verifyErrorMsg(webd, 1, currentDate, dayBefore, false, false);			
		webd.getLogger().info("Return Date:  " + returnmsg);
		
		Assert.assertTrue(errormsg_2.equals(returnmsg.trim()) || returnmsg.trim().contains(errormsg_3));

		webd.shutdownBrowser(true);	
	}	
	
	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_StartDateAfterCurrentDate() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(),"timeSelectorDateOnly.html");
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_StartDateAfterCurrentDate");
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String currentDate = dateFormat.format( now ); 
		
		Calendar c = Calendar.getInstance();
		
		c.setTime(now);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day+1);
		
		String dayAfter = dateFormat.format(c.getTime());
		
		webd.getLogger().info("Set the end date as current day");
		String returnmsg = CommonUIUtils.verifyErrorMsg(webd, 3, dayAfter, currentDate, true, false);			
		webd.getLogger().info("Return Date:  " + returnmsg);
		
		Assert.assertTrue(errormsg_2.equals(returnmsg.trim()) || returnmsg.trim().contains(errormsg_4));

		webd.shutdownBrowser(true);	
	}
	
	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_EndDateAfterCurrentDate() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(),"timeSelectorDateOnly.html");
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_EndDateAfterCurrentDate");
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String currentDate = dateFormat.format( now ); 
		
		Calendar c = Calendar.getInstance();
		
		c.setTime(now);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day+2);
		
		String dayAfter = dateFormat.format(c.getTime());		
		
		webd.getLogger().info("Set the end date as current day");
		String returnmsg = CommonUIUtils.verifyErrorMsg(webd, 3, currentDate, dayAfter,true, false);			
		webd.getLogger().info("Return Date:  " + returnmsg);
		
		Assert.assertTrue(errormsg_1.equals(returnmsg.trim()) || returnmsg.trim().contains(errormsg_4));

		webd.shutdownBrowser(true);	
	}
	
	@Test(alwaysRun = true)
	public void testTimePicker_DateOnly_EndDateBeforeStartDate() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(),"timeSelectorDateOnly.html");
		webd.getLogger().info("Start the test case: testTimePicker_DateOnly_EndDateBeforeStartDate");
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String currentDate = dateFormat.format( now ); 
		
		Calendar c = Calendar.getInstance();
		
		c.setTime(now);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day-1);
		
		String dayBefore = dateFormat.format(c.getTime());
		
		webd.getLogger().info("Set the end date as current day");
		String returnmsg = CommonUIUtils.verifyErrorMsg(webd, 3, currentDate, dayBefore,true, false);			
		webd.getLogger().info("Return Date:  " + returnmsg);
		
		Assert.assertTrue(errormsg_2.equals(returnmsg.trim()) || returnmsg.trim().contains(errormsg_4));

		webd.shutdownBrowser(true);	
	}
	
	@Test(alwaysRun = true)
	public void testTimePicker_MillionSecond_StartDateAfterCurrentDate() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "timeSelectorMilliseconds.html");
		webd.getLogger().info("Start the test case: testTimePicker_MillionSecond_StartDateAfterCurrentDate");
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss:SSS a");
		String currentDate = dateFormat.format( now ); 
		
		Calendar c = Calendar.getInstance();
		
		c.setTime(now);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day+1);
		
		String dayAfter = dateFormat.format(c.getTime());
		
		webd.getLogger().info("Set the end date as current day");
		String returnmsg = CommonUIUtils.verifyErrorMsg(webd, 1, dayAfter, currentDate, false, true);			
		webd.getLogger().info("Return Date:  " + returnmsg);
		
		Assert.assertTrue(errormsg_2.equals(returnmsg.trim()) || returnmsg.trim().contains(errormsg_4));

		webd.shutdownBrowser(true);	
	}
	
	@Test(alwaysRun = true)
	public void testTimePicker_MillionSecond_EndDateAfterCurrentDate() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "timeSelectorMilliseconds.html");
		webd.getLogger().info("Start the test case: testTimePicker_MillionSecond_EndDateAfterCurrentDate");
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss:SSS a");
		String currentDate = dateFormat.format( now ); 
		
		Calendar c = Calendar.getInstance();
		
		c.setTime(now);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day+1);
		
		String dayAfter = dateFormat.format(c.getTime());
		
		webd.getLogger().info("Set the end date as current day");
		String returnmsg = CommonUIUtils.verifyErrorMsg(webd, 1, currentDate, dayAfter,false, true);			
		webd.getLogger().info("Return Date:  " + returnmsg);
		
		Assert.assertTrue(errormsg_1.equals(returnmsg.trim()) || returnmsg.trim().contains(errormsg_4) || errormsg_2.equals(returnmsg.trim()));

		webd.shutdownBrowser(true);	
	}
	
	@Test(alwaysRun = true)
	public void testTimePicker_MillionSecond_EndDateBeforeStartDate() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "timeSelectorMilliseconds.html");
		webd.getLogger().info("Start the test case: testTimePicker_MillionSecond_EndDateBeforeStartDate");
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss:SSS a");
		String currentDate = dateFormat.format( now ); 
		
		Calendar c = Calendar.getInstance();
		
		c.setTime(now);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day-1);
		
		String dayBefore = dateFormat.format(c.getTime());
		
		webd.getLogger().info("Set the end date as current day");
		String returnmsg = CommonUIUtils.verifyErrorMsg(webd, 1, currentDate, dayBefore,false, true);			
		webd.getLogger().info("Return Date:  " + returnmsg);
		
		Assert.assertTrue(errormsg_2.equals(returnmsg.trim()) || returnmsg.trim().contains(errormsg_3));

		webd.shutdownBrowser(true);	
	}
}
