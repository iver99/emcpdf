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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.TimeSelectorUIControls;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeUnit;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TimeSelectorUtil_1240 extends TimeSelectorUtil_1170
{
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil#getTimeRangeLabel(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public String getTimeRangeLabel(WebDriver webd)
	{
		return getTimeRangeLabel(webd, 1);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil#getTimeRangeLabel(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int)
	 */
	//index: start from 1
	@Override
	public String getTimeRangeLabel(WebDriver webd, int index)
	{
		//verify the index, it should equal or larger than 1
		Validator.equalOrLargerThan("index", index, 1);

		//locate the datetimepicker component
		webd.waitForElementPresent("css=" + TimeSelectorUIControls.sTimeRangeBtn);
		webd.takeScreenShot();
		webd.savePageToFile();
		String str_timerangelable = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn))
				.get(index - 1).getText();
		
		//Convert hh:mn:ss.SSS to hh:mm:ss.SSS in order not to break LA lrg(EMCPDF-4057)
		str_timerangelable = str_timerangelable.replace(".", ":");
		
		return str_timerangelable;
	}
	
	@Override
	public String getTimeRangeLabel_V2(WebDriver webd)
	{
		return getTimeRangeLabel_V2(webd, 1);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil#getTimeRangeLabel(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int)
	 */
	//index: start from 1
	@Override
	public String getTimeRangeLabel_V2(WebDriver webd, int index)
	{
		//verify the index, it should equal or larger than 1
		Validator.equalOrLargerThan("index", index, 1);

		//locate the datetimepicker component
		webd.waitForElementPresent("css=" + TimeSelectorUIControls.sTimeRangeBtn);
		webd.takeScreenShot();
		webd.savePageToFile();
		String str_timerangelable = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn))
				.get(index - 1).getText();
		
		return str_timerangelable;
	}
	
	@Override
	public String setCustomTimeWithMillisecond(WebDriver webd, int index, String startDateTime, String endDateTime)
	{
		String start = null;
		String[] tmpTime;
		int lastIndex;
		try {
			tmpTime = startDateTime.split(":");
			lastIndex = startDateTime.lastIndexOf(":");
			if(tmpTime.length > 3) { //convert old time format of 01/01/14 00:00:00:000 AM to 01/01/14 00:00:00.000 AM
				StringBuilder tmpStartDateTime = new StringBuilder(startDateTime);
				startDateTime = tmpStartDateTime.replace(lastIndex, lastIndex+1, ".").toString();
			}
			start = timeFormatChange(webd, startDateTime, "MM/dd/yy hh:mm:ss.SSS a", "MM/dd/yyyy hh:mm:ss.SSS a");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String end = null;
		try {
			tmpTime = endDateTime.split(":");
			lastIndex = endDateTime.lastIndexOf(":");
			if(tmpTime.length > 3) { //convert old time format of 01/01/14 00:00:00:000 AM to 01/01/14 00:00:00.000 AM
				StringBuilder tmpEndDateTime = new StringBuilder(endDateTime);
				endDateTime = tmpEndDateTime.replace(lastIndex, lastIndex+1, ".").toString();
			}
			end = timeFormatChange(webd, endDateTime, "MM/dd/yy hh:mm:ss.SSS a", "MM/dd/yyyy hh:mm:ss.SSS a");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		webd.getLogger().info("the start time in dashboard is:" + start + ",the end time in dashboard is:" + end);
		webd.getLogger().info("we are going to set the custom time in dashboard page");

		try {
			clickTimePicker(webd, index);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_Custom);
		webd.click("css=" + TimeSelectorUIControls.sTimeRange_Custom);

		webd.waitForElementPresent("css=" + TimeSelectorUIControls.sRangeRadio);
		webd.click("css=" + TimeSelectorUIControls.sRangeRadio);

		String regex = "(.*?)\\s+(.*)";
		Pattern p = Pattern.compile(regex);
		Matcher mStart = p.matcher(start);
		Matcher mEnd = p.matcher(end);
		String startDate = null;
		String startTime = null;
		String endDate = null;
		String endTime = null;

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

		//set start date time and end date time
		webd.getLogger().info("Verify if custom panpel displayed...");
		//WebDriverWait wdwait = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		//	wdwait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(TimeSelectorUIControls.sPickPanel)));
		webd.waitForElementVisible("css=" + TimeSelectorUIControls.sPickPanel);
		//webd.isDisplayed(TimeSelectorUIControls.sPickPanel);
		webd.takeScreenShot();
		webd.savePageToFile();

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
		webd.takeScreenShot();
		webd.savePageToFile();

		try {
			clickApplyButton(webd);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String returnTimeRange = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn))
				.get(index - 1).getText();
		return dateConvert(webd, returnTimeRange, TimeRange.Custom, "MM/dd/yyyy hh:mm:ss.SSS a", "MMM d, yyyy hh:mm:ss.SSS a",
				index);
	}

	@Override
	public String setCustomTimeWithMillisecond(WebDriver webd, String startDateTime, String endDateTime)
	{
		return setCustomTimeWithMillisecond(webd, 1, startDateTime, endDateTime);
	}
	
	@Override
	public String setFlexibleRelativeTimeRangeWithMillisecond(WebDriver driver, int index, int relTimeVal, TimeUnit relTimeUnit)
	{
		//open time selector
		clickTimePicker(driver, index);

		//click "Custom" option to open panel
		driver.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_Custom);
		driver.click("css=" + TimeSelectorUIControls.sTimeRange_Custom);

		driver.waitForElementPresent("css=" + TimeSelectorUIControls.sLastRadio);
		driver.click("css=" + TimeSelectorUIControls.sLastRadio);

		driver.waitForElementPresent("css=" + TimeSelectorUIControls.sFlexRelTimeVal);
		driver.getElement("css=" + TimeSelectorUIControls.sFlexRelTimeVal).clear();
		driver.click("css=" + TimeSelectorUIControls.sFlexRelTimeVal);
		driver.sendKeys("css=" + TimeSelectorUIControls.sFlexRelTimeVal, String.valueOf(relTimeVal));

		driver.waitForElementPresent("css=" + TimeSelectorUIControls.sFlexRelTimeOpt);
		driver.click("css=" + TimeSelectorUIControls.sFlexRelTimeOpt);
		String optionLocator = getOptionsLocator(driver, relTimeUnit.getTimeUnit());
		driver.click("css=" + optionLocator);

		try {
			clickApplyButton(driver);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String returnTimeRange = driver.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn))
				.get(index - 1).getText();

		if (returnTimeRange.startsWith("Last") && returnTimeRange.indexOf(":") > -1) {
			return dateConvert(driver, returnTimeRange, TimeRange.Custom, "MM/dd/yyyy hh:mm:ss.SSS a",
					"MMM d, yyyy hh:mm:ss.SSS a", index);
		}
		else {
			String returnStartDate = driver.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick))
					.get(index - 1).findElement(By.cssSelector(TimeSelectorUIControls.sStartDateInput)).getAttribute("value")
					+ " "
					+ driver.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick)).get(index - 1)
							.findElement(By.cssSelector(TimeSelectorUIControls.sStartTimeInput)).getAttribute("value");
			String returnEndDate = driver.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick))
					.get(index - 1).findElement(By.cssSelector(TimeSelectorUIControls.sEndDateInput)).getAttribute("value")
					+ " "
					+ driver.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick)).get(index - 1)
							.findElement(By.cssSelector(TimeSelectorUIControls.sEndTimeInput)).getAttribute("value");

			returnStartDate = timeFormatChange(driver, returnStartDate, "MM/dd/yyyy hh:mm:ss.SSS a", "MMM d, yyyy hh:mm:ss.SSS a");
			returnEndDate = timeFormatChange(driver, returnEndDate, "MM/dd/yyyy hh:mm:ss.SSS a", "MMM d, yyyy hh:mm:ss.SSS a");

			String returnDate = returnTimeRange + ": " + returnStartDate + " - " + returnEndDate;

			return dateConvert(driver, returnDate, TimeRange.Custom, "MM/dd/yyyy hh:mm:ss.SSS a", "MMM d, yyyy hh:mm:ss.SSS a",
					index);
		}
		//		}
	}

	@Override
	public String setFlexibleRelativeTimeRangeWithMillisecond(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		return setFlexibleRelativeTimeRangeWithMillisecond(webd, 1, relTimeVal, relTimeUnit);
	}
	
	@Override
	public String setTimeRangeWithMillisecond(WebDriver webd, int index, TimeRange rangeOption)
	{
		clickTimePicker(webd, index);
		switch (rangeOption) {
			case Last15Mins:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_15Min);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_15Min);

				break;
			case Last30Mins:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_30Min);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_30Min);

				break;
			case Last60Mins:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_60Min);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_60Min);

				break;
			case Last2Hours:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_2Hour);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_2Hour);

				break;
			case Last4Hours:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_4Hour);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_4Hour);

				break;
			case Last6Hours:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_6Hour);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_6Hour);

				break;
			case Last1Day:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_1Day);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_1Day);

				break;
			case Last7Days:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_7Days);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_7Days);

				break;
			case Last14Days:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_14Days);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_14Days);

				break;
			case Last30Days:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_30Days);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_30Days);

				break;
			case Last90Days:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_90Days);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_90Days);

				break;
			case Last1Year:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_1Year);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_1Year);

				break;
			case Last24Hours:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_24Hours);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_24Hours);

				break;
			case Last12Months:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_12Months);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_12Months);

				break;
			case Last8Hours:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_8Hours);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_8Hours);

				break;
			case NewLast60Mins:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_New60Mins);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_New60Mins);

				break;
			case NewLast7Days:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_New7Days);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_New7Days);

				break;

			case Latest:
				if (webd.isDisplayed("css=" + TimeSelectorUIControls.sTimeRange_Latest)) {
					webd.click("css=" + TimeSelectorUIControls.sTimeRange_Latest);

				}
				else {
					webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_Custom);
					webd.click("css=" + TimeSelectorUIControls.sTimeRange_Custom);

					webd.waitForElementPresent("css=" + TimeSelectorUIControls.sLatestRadio);
					webd.click("css=" + TimeSelectorUIControls.sLatestRadio);

					clickApplyButton(webd);
				}
				return webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn)).get(index - 1)
						.getText();
			case Custom:
				try {
					throw new Exception("Please use setCustomTime API to set Custom Range");
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			default:
				break;

		}
		String returnTimeRange = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn))
				.get(index - 1).getText();

		if (returnTimeRange.startsWith(rangeOption.getRangeOption() + ":")) {
			return dateConvert(webd, returnTimeRange, rangeOption, "MM/dd/yyyy hh:mm:ss.SSS a", "MMM d, yyyy hh:mm:ss.SSS a",
					index);
		}
		else {
			String returnStartDate = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick))
					.get(index - 1).findElement(By.cssSelector(TimeSelectorUIControls.sStartDateInput)).getAttribute("value")
					+ " "
					+ webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick)).get(index - 1)
					.findElement(By.cssSelector(TimeSelectorUIControls.sStartTimeInput)).getAttribute("value");
			String returnEndDate = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick))
					.get(index - 1).findElement(By.cssSelector(TimeSelectorUIControls.sEndDateInput)).getAttribute("value")
					+ " "
					+ webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick)).get(index - 1)
					.findElement(By.cssSelector(TimeSelectorUIControls.sEndTimeInput)).getAttribute("value");

			returnStartDate = timeFormatChange(webd, returnStartDate, "MM/dd/yyyy hh:mm:ss.SSS a", "MMM d, yyyy hh:mm:ss.SSS a");
			returnEndDate = timeFormatChange(webd, returnEndDate, "MM/dd/yyyy hh:mm:ss.SSS a", "MMM d, yyyy hh:mm:ss.SSS a");

			String returnDate = returnTimeRange + ": " + returnStartDate + " - " + returnEndDate;
			return dateConvert(webd, returnDate, rangeOption, "MM/dd/yyyy hh:mm:ss.SSS a", "MMM d, yyyy hh:mm:ss.SSS a", index);
		}
	}

	@Override
	public String setTimeRangeWithMillisecond(WebDriver webd, TimeRange rangeOption)
	{
		return setTimeRangeWithMillisecond(webd, 1, rangeOption);
	}

}
