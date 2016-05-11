package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.TimeSelectorExludedDayMonth;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.TimeSelectorUIControls;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TimeSelectorUtil
{
	public enum TimeRange
	{
		Last15Mins("Last 15 mins"), Last30Mins("Last 30 mins"), Last60Mins("Last hour"), Last4Hours("Last 4 hours"), Last6Hours(
				"Last 6 hours"), Last1Day("Last day"), Last7Days("Last week"), Last14Days("Last 14 days"), Last30Days(
						"Last 30 days"), Last90Days("Last 90 days"), Last1Year("Last year"), Latest("Latest"), Custom("Custom");
		private final String timerange;

		private TimeRange(String timerange)
		{
			this.timerange = timerange;
		}

		public String getRangeOption()
		{
			return timerange;
		}

	}

	public static String[] days = { TimeSelectorExludedDayMonth.EXCLUDE_SUNDAY, TimeSelectorExludedDayMonth.EXCLUDE_MONDAY,
		TimeSelectorExludedDayMonth.EXCLUDE_TUESDAY, TimeSelectorExludedDayMonth.EXCLUDE_WEDNESDAY,
		TimeSelectorExludedDayMonth.EXCLUDE_THURSDAY, TimeSelectorExludedDayMonth.EXCLUDE_FRIDAY,
		TimeSelectorExludedDayMonth.EXCLUDE_SATURDAY };

	public static String[] months = { TimeSelectorExludedDayMonth.EXCLUDE_JANUARY, TimeSelectorExludedDayMonth.EXCLUDE_FEBRUARY,
			TimeSelectorExludedDayMonth.EXCLUDE_MARCH, TimeSelectorExludedDayMonth.EXCLUDE_APRIL,
			TimeSelectorExludedDayMonth.EXCLUDE_MAY, TimeSelectorExludedDayMonth.EXCLUDE_JUNE,
			TimeSelectorExludedDayMonth.EXCLUDE_JULY, TimeSelectorExludedDayMonth.EXCLUDE_AUGUST,
			TimeSelectorExludedDayMonth.EXCLUDE_SEPTEMBER, TimeSelectorExludedDayMonth.EXCLUDE_OCTOBER,
			TimeSelectorExludedDayMonth.EXCLUDE_NOVEMBER, TimeSelectorExludedDayMonth.EXCLUDE_DECEMBER };

	public static String setCustomTime(WebDriver webd, int index, String startDateTime, String endDateTime) throws Exception
	{

		String start = TimeSelectorUtil.timeFormatChange(webd, startDateTime, "MM/dd/yy hh:mm a", "MM/dd/yyyy hh:mm a");
		String end = TimeSelectorUtil.timeFormatChange(webd, endDateTime, "MM/dd/yy hh:mm a", "MM/dd/yyyy hh:mm a");
		webd.getLogger().info("the start time in dashboard is:" + start + ",the end time in dashboard is:" + end);
		webd.getLogger().info("we are going to set the custom time in dashboard page");

		TimeSelectorUtil.clickTimePicker(webd, index);
		webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_Custom);
		webd.click("css=" + TimeSelectorUIControls.sTimeRange_Custom);
		webd.takeScreenShot();

		String regex = "(.*?)\\s+(.*)";
		Pattern p = Pattern.compile(regex);
		Matcher mStart = p.matcher(start);
		Matcher mEnd = p.matcher(end);
		String startDate = null;
		String startTime = null;
		String endDate = null;
		String endTime = null;

		if (mStart.find()) {
			if (mStart.group(1) == null || mStart.group(1).isEmpty() || mStart.group(2) == null || mStart.group(2).isEmpty()) {
				throw new Exception("the start date or the start time is empty.Please check:\n" + start);
			}
			startDate = mStart.group(1);
			startTime = mStart.group(2);
			webd.getLogger().info("start date is:" + startDate + ",start time is:" + startTime);

		}
		if (mEnd.find()) {
			if (mEnd.group(1) == null || mEnd.group(1).isEmpty() || mEnd.group(2) == null || mEnd.group(2).isEmpty()) {
				throw new Exception("the end date or the end time is empty.Please check:\n" + end);
			}
			endDate = mEnd.group(1);
			endTime = mEnd.group(2);
			webd.getLogger().info("end date is:" + endDate + ",end time is:" + endTime);
		}

		//set start date time and end date time
		webd.getLogger().info("Verify if custom panpel displayed...");
		WebDriverWait wdwait = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wdwait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(TimeSelectorUIControls.sPickPanel)));
		//webd.isDisplayed(TimeSelectorUIControls.sPickPanel);
		webd.takeScreenShot();

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

		if (webd.isDisplayed(TimeSelectorUIControls.sErrorMsg)) {
			throw new Exception(webd.getText(TimeSelectorUIControls.sErrorMsg));
		}
		else {
			TimeSelectorUtil.clickApplyButton(webd);
			String returnTimeRange = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn))
					.get(index - 1).getText();
			return TimeSelectorUtil.dateConvert(webd, returnTimeRange, TimeRange.Custom);
		}

	}

	//Date MM/dd/yyyy
	//Time hh:mm a
	public static String setCustomTime(WebDriver webd, String startDateTime, String endDateTime) throws Exception
	{
		return TimeSelectorUtil.setCustomTime(webd, 1, startDateTime, endDateTime);
	}

	public static String setTimeFilter(WebDriver webd, int index, String hoursToExclude, int[] daysToExclude,
			int[] monthsToExclude) throws Exception
	{
		TimeSelectorUtil.clickTimePicker(webd, index);
		TimeSelectorUtil.clickTimeFilterIcon(webd);
		TimeSelectorUtil.setHoursToExclude(webd, hoursToExclude);
		TimeSelectorUtil.setDaysToExclude(webd, daysToExclude);
		TimeSelectorUtil.setMonthsToExclude(webd, monthsToExclude);
		TimeSelectorUtil.clickApplyButton(webd);

		String result = "";
		if (hoursToExclude == null || "".equals(hoursToExclude)) {
			result += "";
		}
		else {
			result += "Hours excluded: " + hoursToExclude + ". ";
		}
		if (null == daysToExclude || daysToExclude.length == 0) {
			result += "";
		}
		else {
			result += "Days excluded: ";
			for (int i = 0; i < daysToExclude.length; i++) {
				int value = daysToExclude[i];
				if (i == daysToExclude.length - 1) {
					result += days[value - 1] + ". ";
				}
				else {
					result += days[value - 1] + ", ";
				}
			}

		}
		if (null == monthsToExclude || monthsToExclude.length == 0) {
			result += "";
		}
		else {
			result += "Months excluded: ";
			for (int i = 0; i < monthsToExclude.length; i++) {
				int value = monthsToExclude[i];
				if (i == monthsToExclude.length - 1) {
					result += months[value - 1] + ". ";
				}
				else {
					result += months[value - 1] + ", ";
				}
			}
		}
		result = result.substring(0, result.length() - 2);
		return result;
	}

	public static String setTimeFilter(WebDriver webd, String hoursToExclude, int[] daysToExclude, int[] monthsToExclude)
			throws Exception
	{
		return TimeSelectorUtil.setTimeFilter(webd, 1, hoursToExclude, daysToExclude, monthsToExclude);
	}

	public static String setTimeRange(WebDriver webd, int Index, TimeRange rangeoption) throws Exception
	{
		TimeSelectorUtil.clickTimePicker(webd, Index);
		switch (rangeoption) {
			case Last15Mins:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_15Min);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_15Min);
				webd.takeScreenShot();
				break;
			case Last30Mins:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_30Min);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_30Min);
				webd.takeScreenShot();
				break;
			case Last60Mins:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_60Min);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_60Min);
				webd.takeScreenShot();
				break;
			case Last4Hours:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_4Hour);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_4Hour);
				webd.takeScreenShot();
				break;
			case Last6Hours:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_6Hour);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_6Hour);
				webd.takeScreenShot();
				break;
			case Last1Day:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_1Day);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_1Day);
				webd.takeScreenShot();
				break;
			case Last7Days:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_7Days);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_7Days);
				webd.takeScreenShot();
				break;
			case Last14Days:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_14Days);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_14Days);
				webd.takeScreenShot();
				break;
			case Last30Days:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_30Days);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_30Days);
				webd.takeScreenShot();
				break;
			case Last90Days:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_90Days);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_90Days);
				webd.takeScreenShot();
				break;
			case Last1Year:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_1Year);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_1Year);
				webd.takeScreenShot();
				break;
			case Latest:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_Latest);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_Latest);
				webd.takeScreenShot();
				return webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn)).get(Index - 1)
						.getText();
			case Custom:
				throw new Exception("Please use setCustomTime API to set Custom Range");

		}
		String returnTimeRange = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn))
				.get(Index - 1).getText();

		if (returnTimeRange.startsWith(rangeoption.getRangeOption() + ":")) {
			return TimeSelectorUtil.dateConvert(webd, returnTimeRange, rangeoption);
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

			returnStartDate = TimeSelectorUtil.timeFormatChange(webd, returnStartDate, "MM/dd/yyyy hh:mm a",
					"MMM d, yyyy hh:mm a");
			returnEndDate = TimeSelectorUtil.timeFormatChange(webd, returnEndDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");

			String returnDate = returnTimeRange + ": " + returnStartDate + " - " + returnEndDate;

			return TimeSelectorUtil.dateConvert(webd, returnDate, rangeoption);
		}

	}

	public static String setTimeRange(WebDriver webd, TimeRange rangeoption) throws Exception
	{
		return TimeSelectorUtil.setTimeRange(webd, 1, rangeoption);
	}

	private static void clickApplyButton(WebDriver webd) throws Exception
	{
		//click Apply button
		webd.getLogger().info("Click Apply button");
		webd.isElementPresent("css=" + TimeSelectorUIControls.sApplyBtn);

		if (webd.getAttribute("css=" + TimeSelectorUIControls.sApplyBtn + "@disabled") != null) {
			throw new Exception("the Apply Button is disabled, can't be clicked");
		}
		else {
			webd.click("css=" + TimeSelectorUIControls.sApplyBtn);
			WaitUtil.waitForPageFullyLoaded(webd);
			webd.takeScreenShot();
		}
	}

	private static void clickCancleButton(WebDriver webd) throws Exception
	{
		//click Calcel button
		webd.getLogger().info("Click Calcel button");
		webd.isElementPresent("css=" + TimeSelectorUIControls.sCancelBtn);
		if (webd.getAttribute("css=" + TimeSelectorUIControls.sCancelBtn + "@disabled") != null) {
			throw new Exception("the Cancel Button is disabled, can't be clicked");
		}
		else {
			webd.click("css=" + TimeSelectorUIControls.sCancelBtn);
			webd.takeScreenShot();
		}
	}

	private static void clickTimeFilterIcon(WebDriver webd) throws Exception
	{
		webd.getLogger().info("Click custom option...");
		webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_Custom);
		webd.click("css=" + TimeSelectorUIControls.sTimeRange_Custom);
		webd.takeScreenShot();
		webd.getLogger().info("Click custom option finished!");

		webd.getLogger().info("Click time fitler icon...");
		webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeFilterIcon);
		webd.click("css=" + TimeSelectorUIControls.sTimeFilterIcon);
		webd.takeScreenShot();
		webd.getLogger().info("Click time filter icon finished!");
	}

	private static void clickTimePicker(WebDriver webd) throws Exception
	{
		//click the datetimepicker component
		webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRangeBtn);
		webd.click("css=" + TimeSelectorUIControls.sTimeRangeBtn);
		webd.takeScreenShot();
	}

	private static void clickTimePicker(WebDriver webd, int Index) throws Exception
	{
		//click the datetimepicker component
		webd.waitForElementPresent("css=" + TimeSelectorUIControls.sTimeRangeBtn);
		webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn)).get(Index - 1).click();

		webd.takeScreenShot();
	}

	private static String dateConvert(WebDriver driver, String convertDate, TimeRange timerange) throws Exception
	{
		String timeRange = timerange.getRangeOption();
		String tmpDate = "";
		String returnStartDate = "";
		String returnEndDate = "";

		if (convertDate.startsWith(timeRange)) {
			tmpDate = convertDate.substring(timeRange.length() + 1);
		}
		else {
			tmpDate = convertDate;
		}
		System.out.println("tmpDate:" + tmpDate);
		String[] date = tmpDate.split("-");
		String StartDate = date[0].trim();
		String EndDate = date[1].trim();
		String tmpStartDate = StartDate;
		String[] tmpStart = StartDate.split(" ");

		Calendar current = Calendar.getInstance();
		Calendar today = Calendar.getInstance(); //today
		today.clear();
		today.set(Calendar.YEAR, current.get(Calendar.YEAR));
		today.set(Calendar.MONTH, current.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));

		Calendar yesterday = Calendar.getInstance(); //yesterday
		yesterday.clear();
		yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);

		int actualTodayMonth = today.get(Calendar.MONTH) + 1;
		int actualYesterdayMonth = yesterday.get(Calendar.MONTH) + 1;

		if (StartDate.startsWith("Today")) {
			StartDate = StartDate.replace("Today",
					actualTodayMonth + "/" + today.get(Calendar.DAY_OF_MONTH) + "/" + today.get(Calendar.YEAR));
			returnStartDate = TimeSelectorUtil.timeFormatChange(driver, StartDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");

		}
		else if (StartDate.startsWith("Yesterday")) {
			StartDate = StartDate.replace("Yesterday", actualYesterdayMonth + "/" + yesterday.get(Calendar.DAY_OF_MONTH) + "/"
					+ yesterday.get(Calendar.YEAR));
			returnStartDate = TimeSelectorUtil.timeFormatChange(driver, StartDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
		}
		else {
			returnStartDate = StartDate;
		}

		if (EndDate.startsWith("Today")) {
			EndDate = EndDate.replace("Today",
					actualTodayMonth + "/" + today.get(Calendar.DAY_OF_MONTH) + "/" + today.get(Calendar.YEAR));
			returnEndDate = TimeSelectorUtil.timeFormatChange(driver, EndDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");

		}
		else if (EndDate.startsWith("Yesterday")) {
			EndDate = EndDate.replace("Yesterday", actualYesterdayMonth + "/" + yesterday.get(Calendar.DAY_OF_MONTH) + "/"
					+ yesterday.get(Calendar.YEAR));
			returnEndDate = TimeSelectorUtil.timeFormatChange(driver, EndDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
		}
		else if (Character.isDigit(EndDate.charAt(0))) {
			if (tmpStartDate.startsWith("Today")) {
				EndDate = actualTodayMonth + "/" + today.get(Calendar.DAY_OF_MONTH) + "/" + today.get(Calendar.YEAR) + " "
						+ EndDate;
				returnEndDate = TimeSelectorUtil.timeFormatChange(driver, EndDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
			}
			else if (tmpStartDate.startsWith("Yesterday")) {
				EndDate = actualYesterdayMonth + "/" + yesterday.get(Calendar.DAY_OF_MONTH) + "/" + yesterday.get(Calendar.YEAR)
						+ " " + EndDate;
				returnEndDate = TimeSelectorUtil.timeFormatChange(driver, EndDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
			}
			else {
				returnEndDate = tmpStart[0] + " " + tmpStart[1] + " " + tmpStart[2] + " " + EndDate;
			}
		}
		else {
			returnEndDate = EndDate;

		}

		String[] tmpReturnStartDate = returnStartDate.split(" ");
		String[] tmpReturnEndDate = returnEndDate.split(" ");

		if (tmpReturnStartDate[3].startsWith("0")) {
			returnStartDate = tmpReturnStartDate[0] + " " + tmpReturnStartDate[1] + " " + tmpReturnStartDate[2] + " "
					+ tmpReturnStartDate[3].substring(1) + " " + tmpReturnStartDate[4];
		}
		if (tmpReturnEndDate[3].startsWith("0")) {
			returnEndDate = tmpReturnEndDate[0] + " " + tmpReturnEndDate[1] + " " + tmpReturnEndDate[2] + " "
					+ tmpReturnEndDate[3].substring(1) + " " + tmpReturnEndDate[4];
		}

		return timeRange + ": " + returnStartDate + " - " + returnEndDate;

	}

	private static String getInputForHoursFilter(WebDriver webd, String hoursToExclude)
	{
		String hoursInput = "";

		//get separate hours to include
		String[] hoursToExcludeRanges = hoursToExclude.split(",");
		ArrayList<Integer> hoursToIncludeList = new ArrayList<Integer>();
		for (int i = 0; i < 24; i++) {
			hoursToIncludeList.add(i);
		}
		ArrayList<Integer> hoursToExcludeList = new ArrayList<Integer>();
		for (String tmpRange : hoursToExcludeRanges) {
			String[] bounds = tmpRange.split("-");
			int start = Integer.parseInt(bounds[0].trim());
			int end = Integer.parseInt(bounds[1].trim());
			for (int j = start; j <= end; j++) {
				if (hoursToExcludeList.contains(j)) {
					continue;
				}
				else {
					hoursToExcludeList.add(j);
				}
			}
		}

		hoursToIncludeList.removeAll(hoursToExcludeList);
		Collections.sort(hoursToIncludeList);

		//get hours filter input from separate hours to include
		ArrayList<Integer> hoursIncludedStarts = new ArrayList<Integer>();
		ArrayList<Integer> hoursIncludedEnds = new ArrayList<Integer>();
		hoursIncludedStarts.add(hoursToIncludeList.get(0));
		for (int i = 1; i < hoursToIncludeList.size(); i++) {
			if (i == hoursToIncludeList.size() - 1) {
				if (hoursToIncludeList.get(i) - hoursToIncludeList.get(i - 1) == 1) {
					hoursIncludedEnds.add(hoursToIncludeList.get(i));
				}
				else {
					hoursIncludedEnds.add(hoursToIncludeList.get(i - 1));
					hoursIncludedStarts.add(hoursToIncludeList.get(i));
					hoursIncludedEnds.add(hoursToIncludeList.get(i));
				}
				break;
			}
			if (hoursToIncludeList.get(i) - hoursToIncludeList.get(i - 1) == 1) {
				continue;
			}
			else {
				hoursIncludedEnds.add(hoursToIncludeList.get(i - 1));
				hoursIncludedStarts.add(hoursToIncludeList.get(i));
			}
		}

		for (int i = 0; i < hoursIncludedStarts.size(); i++) {
			hoursInput = hoursInput + hoursIncludedStarts.get(i) + "-" + hoursIncludedEnds.get(i);
			if (i == hoursIncludedStarts.size() - 1) {
				hoursInput = hoursInput + "";
			}
			else {
				hoursInput = hoursInput + ",";
			}
		}
		return hoursInput;
	}

	private static void setDaysToExclude(WebDriver webd, int[] daysToExclude)
	{
		webd.getLogger().info("Start to set days to exclude...");

		if (daysToExclude == null || daysToExclude.length == 0) {
			daysToExclude = new int[] {};
		}

		//check all days first
		webd.waitForElementPresent("css=" + TimeSelectorUIControls.sTimeFilterDaysFilterAll);
		if (!webd.getElement("css=" + TimeSelectorUIControls.sTimeFilterDaysFilterAll).isSelected()) {
			webd.click("css=" + TimeSelectorUIControls.sTimeFilterDaysFilterAll);
		}
		//uncheck excluded days
		for (int tmpValue : daysToExclude) {
			String eleLocator = "css=" + TimeSelectorUIControls.sTimeFilterDaysMonthsFilterPrefix + days[tmpValue - 1]
					+ TimeSelectorUIControls.sTimeFilterDaysMonthsFilterSuffix;
			webd.waitForElementPresent(eleLocator);
			webd.click(eleLocator);
		}
		webd.getLogger().info("Set days to exclude finished!");
		webd.takeScreenShot();
	}

	private static void setHoursToExclude(WebDriver webd, String hoursToExclude) throws Exception
	{
		webd.getLogger().info("Start to set hours to exclude...");

		String hoursInput = "";
		if (hoursToExclude == null || "".equals(hoursToExclude)) {
			hoursInput = "0-23";
		}
		else {
			if (!hoursToExclude.matches("(\\s*\\d{1,2}-\\s*\\d{1,2},?)*")) {
				throw new Exception("The format of hours to exclude is incorrect. Format should be 's1-e1,s2-e2,...'");
			}
			hoursInput = TimeSelectorUtil.getInputForHoursFilter(webd, hoursToExclude);
		}

		webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeFilterHoursFilter);
		webd.click("css=" + TimeSelectorUIControls.sTimeFilterHoursFilter);
		webd.clear("css=" + TimeSelectorUIControls.sTimeFilterHoursFilter);
		webd.sendKeys("css=" + TimeSelectorUIControls.sTimeFilterHoursFilter, hoursInput);
		webd.getLogger().info("Set hours to exclude finished!");
		webd.takeScreenShot();
	}

	//    public static String setExcludeHour(WebDriver webd, int Index, String hours) throws Exception
	//    {
	//     	//click the datetimepicker component
	//    	clickTimePicker(webd, Index);
	//
	//    	//select time range as Custom
	//    	setTimeRange(webd, Index, TimeSelectorTimeRange.CUSTOM);
	//
	//    	//click time filter icon
	//    	webd.getLogger().info("Click Time Filter Icon");
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeFilterIcon)).click();
	//		webd.takeScreenShot();
	//
	//
	//    	//set exclude hours
	//		webd.getLogger().info("Enter excluded time");
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeExcludedInput)).clear();
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeExcludedInput)).sendKeys(hours);
	//		webd.takeScreenShot();
	//		//click apply button
	//		webd.getLogger().info("Click Apply button");
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sApplyBtn)).click();
	//		webd.takeScreenShot();
	//
	//		return webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sFilterInfo)).get(Index).getText();
	//    }
	//
	//    public static String setExcludeHour(WebDriver webd, String hours) throws Exception
	//    {
	//     	//click the datetimepicker component
	//    	clickTimePicker(webd);
	//
	//    	//select time range as Custom
	//    	setTimeRange(webd, TimeSelectorTimeRange.CUSTOM);
	//
	//    	//click time filter icon
	//    	webd.getLogger().info("Click Time Filter Icon");
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeFilterIcon)).click();
	//		webd.takeScreenShot();
	//
	//    	//set exclude hours
	//		webd.getLogger().info("Enter excluded time");
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeExcludedInput)).clear();
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeExcludedInput)).sendKeys(hours);
	//		webd.takeScreenShot();
	//		//click apply button
	//		webd.getLogger().info("Click Apply button");
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sApplyBtn)).click();
	//		webd.takeScreenShot();
	//
	//		return webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sFilterInfo)).getText();
	//    }
	//
	//    public static void setExcludeDay(WebDriver webd, String ExcludeDays) throws Exception
	//    {
	//    	//click the datetimepicker component
	//    	clickTimePicker(webd);
	//
	//    	//select time range as Custom
	//    	setTimeRange(webd, TimeSelectorTimeRange.CUSTOM);
	//
	//    	//click time filter icon
	//    	webd.getLogger().info("Click Time Filter Icon");
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeFilterIcon)).click();
	//		webd.takeScreenShot();
	//
	//		//select excluded days
	//		WebElement webe =webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sDayMonday));
	//		if(webe.isSelected()){
	//			webe.click();
	//		};
	//		webd.takeScreenShot();
	//    }
	//
	//    public static void setExcludeMonth(WebDriver webd, String ExcludeMonths) throws Exception
	//    {
	//    	//click the datetimepicker component
	//    	clickTimePicker(webd);
	//
	//    	//select time range as Custom
	//    	setTimeRange(webd, TimeSelectorTimeRange.CUSTOM);
	//
	//    	//click time filter icon
	//    	webd.getLogger().info("Click Time Filter Icon");
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeFilterIcon)).click();
	//		webd.takeScreenShot();
	//
	//		//select excluded months
	//		WebElement webe =webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sDayMonday));
	//		if(webe.isSelected()){
	//			webe.click();
	//		};
	//		webd.takeScreenShot();
	//    }

	private static void setMonthsToExclude(WebDriver webd, int[] monthsToExclude)
	{
		webd.getLogger().info("Start to set days to exclude...");

		if (monthsToExclude == null || monthsToExclude.length == 0) {
			monthsToExclude = new int[] {};
		}

		//check all months first
		webd.waitForElementPresent("css=" + TimeSelectorUIControls.sTimeFilterMonthsFilterAll);
		if (!webd.getElement("css=" + TimeSelectorUIControls.sTimeFilterMonthsFilterAll).isSelected()) {
			webd.click("css=" + TimeSelectorUIControls.sTimeFilterMonthsFilterAll);
		}
		//uncheck excluded months
		for (int tmpValue : monthsToExclude) {
			String eleLocator = "css=" + TimeSelectorUIControls.sTimeFilterDaysMonthsFilterPrefix + months[tmpValue - 1]
					+ TimeSelectorUIControls.sTimeFilterDaysMonthsFilterSuffix;
			webd.waitForElementPresent(eleLocator);
			webd.click(eleLocator);
		}
		webd.getLogger().info("Set months to exclude finished!");
		webd.takeScreenShot();
	}

	private static String timeFormatChange(WebDriver driver, String testTime, String inputDateFormat, String outputDateFormat)
			throws Exception
	{
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputDateFormat);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputDateFormat);
		Date date = null;
		try {
			date = inputFormat.parse(testTime);
		}
		catch (ParseException e) {
			driver.getLogger().info(e.getLocalizedMessage());
		}

		return outputFormat.format(date);
	}
}
