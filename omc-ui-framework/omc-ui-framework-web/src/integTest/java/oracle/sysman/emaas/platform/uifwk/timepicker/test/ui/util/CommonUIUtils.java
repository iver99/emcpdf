/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.TimeSelectorUIControls;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

/**
 * @author shangwan
 */
public class CommonUIUtils
{
	private static WebDriver driver;

	public static void loadWebDriver(WebDriver webDriver)
	{
		driver = webDriver;
	}

	public static void verifyResult(WebDriver driver, int index, String returnDate, TimeRange option, String StartLabelLocator,
			String EndLabelLocator, boolean DateOnly, boolean MillionSeconds) throws ParseException
	{
		driver.getLogger().info("Start to verify Results...");
		Validator.equalOrLargerThan("index", index, 1);
		WaitUtil.waitForPageFullyLoaded(driver);

		String strTimePickerText = driver.getWebDriver().findElements(By.cssSelector(UIControls.TIMERANGEBTN_CSS)).get(index - 1)
				.getText();

		driver.getLogger().info("TimePickerLabel: " + strTimePickerText);
		Assert.assertEquals(strTimePickerText, TimeSelectorUtil.getTimeRangeLabel(driver, index));

		String timeRange = option.getRangeOption();

		String sTmpStartDateTime = driver.getText(StartLabelLocator);
		String sTmpEndDateTime = driver.getText(EndLabelLocator);

		SimpleDateFormat fmt = null;

		if (DateOnly) {
			fmt = new SimpleDateFormat("MMM d, yyyy");
		}
		else {
			if (MillionSeconds) {
				fmt = new SimpleDateFormat("MMM d, yyyy h:mm:ss:SSS a");
			}
			else {
				fmt = new SimpleDateFormat("MMM d, yyyy h:mm a");
			}
		}

		Date dTmpStart = new Date();
		Date dTmpEnd = new Date();

		String tmpReturnDate = "";

		if (!"Latest".equals(timeRange)) {
			tmpReturnDate = returnDate.substring(timeRange.length() + 1);
			driver.getLogger().info("timerange: " + timeRange);
			driver.getLogger().info("returnDate: " + tmpReturnDate);

			String[] tmpDate = tmpReturnDate.split("-");

			String tmpStartDate = tmpDate[0].trim();
			String tmpEndDate = tmpDate[1].trim();

			Assert.assertEquals(tmpStartDate, sTmpStartDateTime);
			Assert.assertEquals(tmpEndDate, sTmpEndDateTime);
		}

		driver.getLogger().info("Verify the result in label");

		dTmpStart = fmt.parse(sTmpStartDateTime);
		dTmpEnd = fmt.parse(sTmpEndDateTime);

		TimeZone tz = TimeZone.getDefault();

		int starttz = tz.getOffset(dTmpStart.getTime());
		int endtz = tz.getOffset(dTmpEnd.getTime());

		driver.getLogger().info("sStartText: " + sTmpStartDateTime);
		driver.getLogger().info("sEndText: " + sTmpEndDateTime);

		long lTimeRange = dTmpEnd.getTime() - dTmpStart.getTime() + (endtz - starttz);

		Calendar calStart = Calendar.getInstance();
		calStart.setTime(dTmpStart);

		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(dTmpEnd);

		//verify the time range is expected
		switch (timeRange) {
			case "Last 15 mins":
				Assert.assertEquals(lTimeRange / (60 * 1000), 15);
				break;
			case "Last 30 mins":
				Assert.assertEquals(lTimeRange / (60 * 1000), 30);
				break;
			case "Last hour":
				Assert.assertEquals(lTimeRange / (60 * 1000), 60);
				break;
			case "Last 4 hours":
				Assert.assertEquals(lTimeRange / (60 * 60 * 1000), 4);
				break;
			case "Last 6 hours":
				Assert.assertEquals(lTimeRange / (60 * 60 * 1000), 6);
				break;
			case "Last day":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000), 1);
				break;
			case "Last 24 hours":
				Assert.assertEquals(lTimeRange / (60 * 60 * 1000), 24);
				break;
			case "Last 12 months":
				Assert.assertEquals(calStart.get(Calendar.YEAR) + 1, calEnd.get(Calendar.YEAR));
				Assert.assertEquals(calStart.get(Calendar.MONTH), calEnd.get(Calendar.MONTH));
				Assert.assertEquals(calStart.get(Calendar.DAY_OF_MONTH), calEnd.get(Calendar.DAY_OF_MONTH));
				break;
			case "Last 8 hours":
				Assert.assertEquals(lTimeRange / (60 * 60 * 1000), 8);
				break;
			case "Last 7 days":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000), 7);
				break;
			case "Last 60 mins":
				Assert.assertEquals(lTimeRange / (60 * 1000), 60);
				break;
			case "Last week":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000), 7);
				break;
			case "Last 14 days":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000), 14);
				break;
			case "Last 30 days":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000), 30);
				break;
			case "Last 90 day":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000), 90);
				break;
			case "Last year":
				Assert.assertEquals(calStart.get(Calendar.YEAR) + 1, calEnd.get(Calendar.YEAR));
				Assert.assertEquals(calStart.get(Calendar.MONTH), calEnd.get(Calendar.MONTH));
				Assert.assertEquals(calStart.get(Calendar.DAY_OF_MONTH), calEnd.get(Calendar.DAY_OF_MONTH));
				break;
			case "Latest":
				Assert.assertEquals(lTimeRange, 0);
				break;
			default:
				break;
		}

		driver.getLogger().info("Verify Result Pass!");

	}

	public static void verifyResult_FlexibleDate(WebDriver driver, int index, String returnDate, String StartLabelLocator,
			String EndLabelLocator, boolean DateOnly, boolean MillionSeconds) throws ParseException
	{
		driver.getLogger().info("Start to verify Results...");
		Validator.equalOrLargerThan("index", index, 1);
		WaitUtil.waitForPageFullyLoaded(driver);

		String strTimePickerText = driver.getWebDriver().findElements(By.cssSelector(UIControls.TIMERANGEBTN_CSS)).get(index - 1)
				.getText();

		driver.getLogger().info("TimePickerLabel: " + strTimePickerText);
		Assert.assertEquals(strTimePickerText, TimeSelectorUtil.getTimeRangeLabel(driver, index));

		String timeRange = "";

		if (!strTimePickerText.equals("") && strTimePickerText != null) {
			String[] tmpArray = strTimePickerText.split(":");
			timeRange = tmpArray[0];
		}

		driver.getLogger().info("timerange: " + timeRange);
		driver.getLogger().info("returnDate: " + returnDate);

		String str_Range = "";
		String str_TimeInterval = "";
		String str_TimeUnit = "";

		if (timeRange.startsWith("Last")) {
			str_Range = timeRange.substring(5);
			driver.getLogger().info("Range:  " + str_Range);
			if (Character.isDigit(str_Range.charAt(0))) {
				String[] str_Temp = str_Range.split(" ");
				str_TimeInterval = str_Temp[0];
				str_TimeUnit = str_Temp[1];
			}
			else {
				str_TimeInterval = "1";
				str_TimeUnit = str_Range;
			}
		}

		String sTmpStartDateTime = driver.getText(StartLabelLocator);
		String sTmpEndDateTime = driver.getText(EndLabelLocator);

		SimpleDateFormat fmt = null;

		if (DateOnly) {
			fmt = new SimpleDateFormat("MMM d, yyyy");
		}
		else {
			if (MillionSeconds) {
				fmt = new SimpleDateFormat("MMM d, yyyy h:mm:ss:SSS a");
			}
			else {
				fmt = new SimpleDateFormat("MMM d, yyyy h:mm a");
			}
		}

		Date dTmpStart = new Date();
		Date dTmpEnd = new Date();

		String tmpReturnDate = "";

		if (!"Latest".equals(timeRange)) {
			tmpReturnDate = returnDate.substring(timeRange.length() + 1);
			driver.getLogger().info("timerange: " + timeRange);
			driver.getLogger().info("returnDate: " + tmpReturnDate);

			String[] tmpDate = tmpReturnDate.split("-");

			String tmpStartDate = tmpDate[0].trim();
			String tmpEndDate = tmpDate[1].trim();

			Assert.assertEquals(tmpStartDate, sTmpStartDateTime);
			Assert.assertEquals(tmpEndDate, sTmpEndDateTime);
		}

		driver.getLogger().info("Verify the result in label");

		dTmpStart = fmt.parse(sTmpStartDateTime);
		dTmpEnd = fmt.parse(sTmpEndDateTime);

		TimeZone tz = TimeZone.getDefault();

		int starttz = tz.getOffset(dTmpStart.getTime());
		int endtz = tz.getOffset(dTmpEnd.getTime());

		driver.getLogger().info("sStartText: " + sTmpStartDateTime);
		driver.getLogger().info("sEndText: " + sTmpEndDateTime);

		long lTimeRange = dTmpEnd.getTime() - dTmpStart.getTime() + (endtz - starttz);

		driver.getLogger().info(Long.toString(lTimeRange));

		Calendar calStart = Calendar.getInstance();
		calStart.setTime(dTmpStart);

		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(dTmpEnd);

		int i = Integer.parseInt(str_TimeInterval);

		//verify the time range is expected
		switch (str_TimeUnit) {
			case "second":
				Assert.assertEquals(lTimeRange / 1000, 1);
				break;
			case "seconds":
				Assert.assertEquals(lTimeRange / 1000, i);
				break;
			case "minute":
				Assert.assertEquals(lTimeRange / (60 * 1000), 1);
				break;
			case "minutes":
				Assert.assertEquals(lTimeRange / (60 * 1000), i);
				break;
			case "hour":
				Assert.assertEquals(lTimeRange / (60 * 60 * 1000), 1);
				break;
			case "hours":
				Assert.assertEquals(lTimeRange / (60 * 60 * 1000), i);
				break;
			case "day":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000), 1);
				break;
			case "days":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000), i);
				break;
			case "week":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000 * 7), 1);
				break;
			case "weeks":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000 * 7), i);
				break;
			case "month":
				Assert.assertEquals(calEnd.get(Calendar.YEAR) * 12 + calEnd.get(Calendar.MONTH) - calStart.get(Calendar.YEAR)
						* 12 - calStart.get(Calendar.MONTH), 1);
				//below assertion not always true
				//e.g. Last 1 Month, Now is March 31, 2017, the start time is Feb 28, 2017 (there is no 31 in Feb, 2017)
				//TODO calculate correct start date separately and compare
				//Assert.assertEquals(calStart.get(Calendar.DAY_OF_MONTH), calEnd.get(Calendar.DAY_OF_MONTH));
				break;
			case "months":
				Assert.assertEquals(calEnd.get(Calendar.YEAR) * 12 + calEnd.get(Calendar.MONTH) - calStart.get(Calendar.YEAR)
						* 12 - calStart.get(Calendar.MONTH), i);
				//below assertion not always true
				//e.g. Last 11 Months, Now is March 31, 2017, the start time is Feb 29, 2016 (there is no 31 in Feb, 2016)
				//TODO calculate correct start date separately and compare
				//Assert.assertEquals(calStart.get(Calendar.DAY_OF_MONTH), calEnd.get(Calendar.DAY_OF_MONTH));
				break;
			case "year":
				Assert.assertEquals(calStart.get(Calendar.YEAR) + 1, calEnd.get(Calendar.YEAR));
				Assert.assertEquals(calStart.get(Calendar.MONTH), calEnd.get(Calendar.MONTH));
				//below assertion not always true
				//e.g. Last 1 Year, Now is Feb 29, 2016, the start time is Feb 28, 2015 (there is no 29 in Feb, 2015)
				//TODO calculate correct start date separately and compare
				//Assert.assertEquals(calStart.get(Calendar.DAY_OF_MONTH), calEnd.get(Calendar.DAY_OF_MONTH));
				break;
			case "years":
				Assert.assertEquals(calStart.get(Calendar.YEAR) + i, calEnd.get(Calendar.YEAR));
				Assert.assertEquals(calStart.get(Calendar.MONTH), calEnd.get(Calendar.MONTH));
				//below assertion not always true
				//e.g. Last 2 Years, Now is Feb 29, 2016, the start time is Feb 28, 2014 (there is no 29 in Feb, 2014)
				//TODO calculate correct start date separately and compare
				//Assert.assertEquals(calStart.get(Calendar.DAY_OF_MONTH), calEnd.get(Calendar.DAY_OF_MONTH));
				break;
			default:
				break;
		}

		driver.getLogger().info("Verify Result Pass!");

	}
	
	public static String verifyErrorMsg(WebDriver webd, int index, String startDateTime, String endDateTime, boolean DateOnly, boolean MillionSecond)
	{
		String start = null;
		String end = null;
		String startDate = null;
		String startTime = null;
		String endDate = null;
		String endTime = null;
		
		try {
			if(DateOnly){
			startDate = timeFormatChange(webd, startDateTime, "MM/dd/yy", "MM/dd/yyyy");
			}
			else if(MillionSecond){
				start = timeFormatChange(webd, startDateTime, "MM/dd/yy hh:mm:ss:SSS a", "MM/dd/yyyy hh:mm:ss:SSS a");
			}
			else
			{
				start = timeFormatChange(webd, startDateTime, "MM/dd/yy hh:mm a", "MM/dd/yyyy hh:mm a");
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			webd.getLogger().info(e.getLocalizedMessage());			
		}		
		try {
			if(DateOnly){
				endDate = timeFormatChange(webd, endDateTime, "MM/dd/yy", "MM/dd/yyyy");
				}
				else if(MillionSecond){
					end = timeFormatChange(webd, endDateTime, "MM/dd/yy hh:mm:ss:SSS a", "MM/dd/yyyy hh:mm:ss:SSS a");
				}
				else
				{
					end = timeFormatChange(webd, endDateTime, "MM/dd/yy hh:mm a", "MM/dd/yyyy hh:mm a");
				}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			webd.getLogger().info(e.getLocalizedMessage());
		}
		
		try {
			//click the datetimepicker component
			webd.waitForElementPresent("css=" + TimeSelectorUIControls.sTimeRangeBtn);
			webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn)).get(index - 1).click();

			webd.takeScreenShot();
			webd.savePageToFile();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			webd.getLogger().info(e.getLocalizedMessage());
		}
		
		webd.getLogger().info("start:"+start);
		webd.getLogger().info("end:"+end);
		
		if(!DateOnly)
		{
			String regex = "(.*?)\\s+(.*)";
			Pattern p = Pattern.compile(regex);
			Matcher mStart = p.matcher(start);
			Matcher mEnd = p.matcher(end);		
	
			if (mStart.find()) {
				startDate = mStart.group(1);
				startTime = mStart.group(2);
				webd.getLogger().info("start date is:" + startDate + ",start time is:" + startTime);
	
			}
			if (mEnd.find()) {
				endDate = mEnd.group(1);
				endTime = mEnd.group(2);
				webd.getLogger().info("end date is:" + endDate + ",end time is:" + endTime);
			}
		}
		
		webd.getLogger().info("the start date in dashboard is:" + startDate + ",the end date in dashboard is:" + endDate);
		webd.getLogger().info("we are going to set the custom time in dashboard page");
				
		webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_Custom);
		webd.click("css=" + TimeSelectorUIControls.sTimeRange_Custom);

		webd.waitForElementPresent("css=" + TimeSelectorUIControls.sRangeRadio);
		webd.click("css=" + TimeSelectorUIControls.sRangeRadio);


		//set start date time and end date time
		webd.getLogger().info("Verify if custom panpel displayed...");
		webd.waitForElementVisible("css=" + TimeSelectorUIControls.sPickPanel);

		if(DateOnly)
		{
			webd.getLogger().info("Input the start date and end date...");
			webd.click("css=" + TimeSelectorUIControls.sStartDateInput);
			webd.clear("css=" + TimeSelectorUIControls.sStartDateInput);
			webd.sendKeys("css=" + TimeSelectorUIControls.sStartDateInput, startDate);
			webd.click("css=" + TimeSelectorUIControls.sEndDateInput);
			webd.clear("css=" + TimeSelectorUIControls.sEndDateInput);
			webd.sendKeys("css=" + TimeSelectorUIControls.sEndDateInput, endDate);
			webd.click("css=" + TimeSelectorUIControls.sEndDateInput);
		}
		else
		{		
			webd.getLogger().info("Input the start date time and end date time...");
			webd.click("css=" + TimeSelectorUIControls.sStartDateInput);
			webd.clear("css=" + TimeSelectorUIControls.sStartDateInput);
			webd.sendKeys("css=" + TimeSelectorUIControls.sStartDateInput, startDate);
			webd.click("css=" + TimeSelectorUIControls.sStartTimeInput);
			webd.clear("css=" + TimeSelectorUIControls.sStartTimeInput);
			webd.sendKeys("css=" + TimeSelectorUIControls.sStartTimeInput, startTime);
			webd.click("css=" + TimeSelectorUIControls.sEndDateInput);
			webd.clear("css=" + TimeSelectorUIControls.sEndDateInput);
			webd.sendKeys("css=" + TimeSelectorUIControls.sEndDateInput, endDate);
			webd.click("css=" + TimeSelectorUIControls.sEndTimeInput);
			webd.clear("css=" + TimeSelectorUIControls.sEndTimeInput);
			webd.sendKeys("css=" + TimeSelectorUIControls.sEndTimeInput, endTime);
			webd.click("css=" + TimeSelectorUIControls.sEndTimeInput);
		}
		webd.takeScreenShot();
		
		webd.isElementPresent("css=" + TimeSelectorUIControls.sApplyBtn);
		if (webd.isDisplayed("css=" + TimeSelectorUIControls.sErrorMsg) || webd.isDisplayed("css=" + TimeSelectorUIControls.sJetErrorMsg)) {			
			Assert.assertTrue("true".equals(webd.getAttribute("css=" + TimeSelectorUIControls.sApplyBtn + "@disabled")));
			if(webd.isDisplayed("css=" + TimeSelectorUIControls.sErrorMsg)) {
				return webd.getText("css=" + TimeSelectorUIControls.sErrorMsg);
			}else {
				return webd.getText("css=" + TimeSelectorUIControls.sJetErrorMsgDetail);
			}
		}
		else
		{
			return "Set time format is correct";
		}
	}
	
	public static String timeFormatChange(WebDriver driver, String testTime, String inputDateFormat, String outputDateFormat)
	{
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputDateFormat, Locale.US);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputDateFormat,Locale.US);
		Date date = null;
		try {
			date = inputFormat.parse(testTime);
		}
		catch (ParseException e) {
			driver.getLogger().info(e.getLocalizedMessage());
		}

		return outputFormat.format(date);
	}

	public static void clickTimePicker(WebDriver webd, int Index)
	{
		//click the datetimepicker component
		webd.waitForElementPresent("css=" + UIControls.TIMERANGEBTN_CSS);
		webd.getWebDriver().findElements(By.cssSelector(UIControls.TIMERANGEBTN_CSS)).get(Index - 1).click();

		webd.takeScreenShot();
	}
	
	public void getCurrentDate()
	{
		Date now = new Date();
	}
}
