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
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TimeSelectorUtil_1130 extends TimeSelectorUtil_175
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

		return str_timerangelable;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil#setCustomTime(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, java.lang.String, java.lang.String)
	 */
	@Override
	public String setCustomTime(WebDriver webd, int index, String startDateTime, String endDateTime)
	{

		String start = null;
		try {
			start = timeFormatChange(webd, startDateTime, "MM/dd/yy hh:mm a", "MM/dd/yyyy hh:mm a");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String end = null;
		try {
			end = timeFormatChange(webd, endDateTime, "MM/dd/yy hh:mm a", "MM/dd/yyyy hh:mm a");
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
		//		WebDriverWait wdwait = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		//		wdwait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(TimeSelectorUIControls.sPickPanel)));
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

		//		if (webd.isDisplayed(TimeSelectorUIControls.sErrorMsg)) {
		//			try {
		//				throw new Exception(webd.getText(TimeSelectorUIControls.sErrorMsg));
		//			}
		//			catch (Exception e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//			return null;
		//		}
		//		else {
		try {
			clickApplyButton(webd);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String returnTimeRange = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn))
				.get(index - 1).getText();
		return dateConvert(webd, returnTimeRange, TimeRange.Custom, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a", index);
		//		}

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil#setCustomTime(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	//Date MM/dd/yyyy
	//Time hh:mm a
	@Override
	public String setCustomTime(WebDriver webd, String startDateTime, String endDateTime)
	{
		return setCustomTime(webd, 1, startDateTime, endDateTime);
	}

	@Override
	public String setCustomTimeWithDateOnly(WebDriver webd, int index, String startDate, String endDate)
	{
		try {
			startDate = timeFormatChange(webd, startDate, "MM/dd/yy", "MM/dd/yyyy");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			endDate = timeFormatChange(webd, endDate, "MM/dd/yy", "MM/dd/yyyy");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		webd.getLogger().info("the start date in dashboard is:" + startDate + ",the end date in dashboard is:" + endDate);
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

		//set start date time and end date time
		webd.getLogger().info("Verify if custom panpel displayed...");
		WebDriverWait wdwait = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wdwait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(TimeSelectorUIControls.sPickPanel)));
		//webd.isDisplayed(TimeSelectorUIControls.sPickPanel);
		webd.takeScreenShot();
		webd.savePageToFile();
		webd.getLogger().info("Input the start date and end date...");
		webd.click("css=" + TimeSelectorUIControls.sStartDateInput);
		webd.clear("css=" + TimeSelectorUIControls.sStartDateInput);
		webd.sendKeys("css=" + TimeSelectorUIControls.sStartDateInput, startDate);
		webd.click("css=" + TimeSelectorUIControls.sEndDateInput);
		webd.clear("css=" + TimeSelectorUIControls.sEndDateInput);
		webd.sendKeys("css=" + TimeSelectorUIControls.sEndDateInput, endDate);
		webd.click("css=" + TimeSelectorUIControls.sEndDateInput);

		//		if (webd.isDisplayed(TimeSelectorUIControls.sErrorMsg)) {
		//			try {
		//				throw new Exception(webd.getText(TimeSelectorUIControls.sErrorMsg));
		//			}
		//			catch (Exception e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//			return null;
		//		}
		//		else {
		try {
			clickApplyButton(webd);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String returnTimeRange = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn))
				.get(index - 1).getText();
		return dateConvert(webd, returnTimeRange, TimeRange.Custom, "MM/dd/yyyy", "MMM d, yyyy", index);
		//		}
	}

	@Override
	public String setCustomTimeWithDateOnly(WebDriver webd, String startDate, String endDate)
	{
		return setCustomTimeWithDateOnly(webd, 1, startDate, endDate);
	}

	@Override
	public String setFlexibleRelativeTimeRange(WebDriver driver, int index, int relTimeVal, TimeUnit relTimeUnit)
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

		//		if (driver.isDisplayed(TimeSelectorUIControls.sErrorMsg)) {
		//			try {
		//				throw new Exception(driver.getText(TimeSelectorUIControls.sErrorMsg));
		//			}
		//			catch (Exception e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//			return null;
		//		}
		//		else {
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
			return dateConvert(driver, returnTimeRange, TimeRange.Custom, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a", index);
		}
		else {
			String returnStartDate = driver.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sStartDateInput))
					.get(index - 1).getAttribute("value")
					+ " "
					+ driver.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sStartTimeInput)).get(index - 1)
							.getAttribute("value");
			String returnEndDate = driver.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sEndDateInput))
					.get(index - 1).getAttribute("value")
					+ " "
					+ driver.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sEndTimeInput)).get(index - 1)
							.getAttribute("value");

			returnStartDate = timeFormatChange(driver, returnStartDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
			returnEndDate = timeFormatChange(driver, returnEndDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");

			String returnDate = returnTimeRange + ": " + returnStartDate + " - " + returnEndDate;

			return dateConvert(driver, returnDate, TimeRange.Custom, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a", index);
		}
		//		}
	}

	@Override
	public String setFlexibleRelativeTimeRange(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		return setFlexibleRelativeTimeRange(webd, 1, relTimeVal, relTimeUnit);
	}

	@Override
	public String setFlexibleRelativeTimeRangeWithDateOnly(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit)
	{
		webd.getLogger().info("Start to setFlexibleRelativeTimeRangeWithDateOnly...");

		Validator.fromValidValues("relTimeUnit", relTimeUnit, TimeUnit.Day, TimeUnit.Week, TimeUnit.Month, TimeUnit.Year);

		// open time selector
		clickTimePicker(webd, index);

		// click "Custom" option to open panel
		webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_Custom);
		webd.click("css=" + TimeSelectorUIControls.sTimeRange_Custom);

		webd.waitForElementPresent("css=" + TimeSelectorUIControls.sLastRadio);
		webd.click("css=" + TimeSelectorUIControls.sLastRadio);

		webd.waitForElementPresent("css=" + TimeSelectorUIControls.sFlexRelTimeVal);
		webd.getElement("css=" + TimeSelectorUIControls.sFlexRelTimeVal).clear();
		webd.click("css=" + TimeSelectorUIControls.sFlexRelTimeVal);
		webd.sendKeys("css=" + TimeSelectorUIControls.sFlexRelTimeVal, String.valueOf(relTimeVal));

		webd.waitForElementPresent("css=" + TimeSelectorUIControls.sFlexRelTimeOpt);
		webd.click("css=" + TimeSelectorUIControls.sFlexRelTimeOpt);
		String optionLocator = getOptionsLocator(webd, relTimeUnit.getTimeUnit());
		webd.click("css=" + optionLocator);

		//		if (webd.isDisplayed(TimeSelectorUIControls.sErrorMsg)) {
		//			try {
		//				throw new Exception(webd.getText(TimeSelectorUIControls.sErrorMsg));
		//			}
		//			catch (Exception e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//			return null;
		//		}
		//		else {
		try {
			clickApplyButton(webd);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String returnTimeRange = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn))
				.get(index - 1).getText();

		if (returnTimeRange.startsWith("Last") && returnTimeRange.indexOf(":") > -1) {
			return dateConvert(webd, returnTimeRange, TimeRange.Custom, "MM/dd/yyyy", "MMM d, yyyy", index);
		}
		else {
			String returnStartDate = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sStartDateInput))
					.get(index - 1).getAttribute("value")
					+ " "
					+ webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sStartTimeInput)).get(index - 1)
							.getAttribute("value");
			String returnEndDate = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sEndDateInput))
					.get(index - 1).getAttribute("value")
					+ " "
					+ webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sEndTimeInput)).get(index - 1)
							.getAttribute("value");

			returnStartDate = timeFormatChange(webd, returnStartDate, "MM/dd/yyyy", "MMM d, yyyy");
			returnEndDate = timeFormatChange(webd, returnEndDate, "MM/dd/yyyy", "MMM d, yyyy");

			String returnDate = returnTimeRange + ": " + returnStartDate + " - " + returnEndDate;

			return dateConvert(webd, returnDate, TimeRange.Custom, "MM/dd/yyyy", "MMM d, yyyy", index);
		}
		//		}
	}

	@Override
	public String setFlexibleRelativeTimeRangeWithDateOnly(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		return setFlexibleRelativeTimeRangeWithDateOnly(webd, 1, relTimeVal, relTimeUnit);
	}

	@Override
	public String setTimeRange(WebDriver webd, int Index, TimeRange rangeoption)
	{
		clickTimePicker(webd, Index);
		switch (rangeoption) {
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
			case Latest:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_Latest);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_Latest);

				return webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn)).get(Index - 1)
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
				.get(Index - 1).getText();

		if (returnTimeRange.startsWith(rangeoption.getRangeOption() + ":")) {
			return dateConvert(webd, returnTimeRange, rangeoption, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a", Index);
		}
		else {
			String returnStartDate = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sStartDateInput))
					.get(Index - 1).getAttribute("value")
					+ " "
					+ webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sStartTimeInput)).get(Index - 1)
					.getAttribute("value");
			String returnEndDate = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sEndDateInput))
					.get(Index - 1).getAttribute("value")
					+ " "
					+ webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sEndTimeInput)).get(Index - 1)
					.getAttribute("value");

			returnStartDate = timeFormatChange(webd, returnStartDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
			returnEndDate = timeFormatChange(webd, returnEndDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");

			String returnDate = returnTimeRange + ": " + returnStartDate + " - " + returnEndDate;
			return dateConvert(webd, returnDate, rangeoption, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a", Index);
		}

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil#setTimeRange(oracle.sysman.qatool.uifwk.webdriver.WebDriver, oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange)
	 */
	@Override
	public String setTimeRange(WebDriver webd, TimeRange rangeoption)
	{
		return setTimeRange(webd, 1, rangeoption);
	}

	private String getOptionsLocator(WebDriver driver, String option)
	{
		String optionLocator = TimeSelectorUIControls.sFlexRelTimeOptList + "[value='" + option + "']";
		WebElement li = driver.getWebDriver().findElement(By.cssSelector(optionLocator));
		List<WebElement> list = driver.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sFlexRelTimeOptList));
		int index = list.indexOf(li);

		return TimeSelectorUIControls.sFlexRelTimeOptStart + (index + 1) + TimeSelectorUIControls.sFlexRelTimeOptEnd;
	}
}
