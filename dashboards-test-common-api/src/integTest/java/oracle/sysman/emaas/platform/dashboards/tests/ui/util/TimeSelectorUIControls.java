/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.util;

/**
 * @author shangwan
 */
public class TimeSelectorUIControls {
	// Begin: Text
	public static final String sErrorMsg = "div.errorMsg";
	 public static final String sFilterInfoLabel = "//*[@id='filterInfo']";
	// End: Text

	// Begin: InputText
	public static final String sStartDateInput = "input[id^='inputStartDate_'].oj-inputtext-input";
	public static final String sStartTimeInput = "input[id^='inputStartTime_'].oj-inputdatetime-input";
	public static final String sEndDateInput = "input[id^='inputEndDate_'].oj-inputtext-input";
	public static final String sEndTimeInput = "input[id^='inputEndTime_'].oj-inputdatetime-input";
	 public static final String sTimeExcludedInput =
	 "input.oj-col.oj-sm-10.oj-md-10.oj-inputtext-input";
	// End: InputText

	// Begin: Button
	public static final String sApplyBtn = "button[id^='applyButton']";
	public static final String sCancelBtn = "button[id^='cancelButton']";

	public static final String sDateTimePick = "[id^='dateTimePicker_']";
	public static final String sTimeRangeBtn = "[id^='dateTimePicker_'] .oj-select-choice .oj-select-arrow";
	public static final String sDisplayDateTime = "[id^='dateTimePicker_'] .oj-select-choice .oj-select-chosen";
	 public static final String sFilterInfoIndicator =
	 "[id^='tfInfoIndicator_'].time-filter-indicator";
	 public static final String sFilterInfo =
	 "div[id^='tfInfo_'] [data-bind='html: timeFilterInfo']";

	public static final String sTimeRange_15Min = "a[data-bind*='timePeriodLast15mins']";
	public static final String sTimeRange_30Min = "a[data-bind*='timePeriodLast30mins']";
	public static final String sTimeRange_60Min = "a[data-bind*='timePeriodLast60mins']";
	public static final String sTimeRange_4Hour = "a[data-bind*='timePeriodLast4hours']";
	public static final String sTimeRange_6Hour = "a[data-bind*='timePeriodLast6hours']";
	public static final String sTimeRange_1Day = "a[data-bind*='timePeriodLast1day']";
	public static final String sTimeRange_7Days = "a[data-bind*='timePeriodLast7days']";
	public static final String sTimeRange_30Days = "a[data-bind*='timePeriodLast30days']";
	public static final String sTimeRange_90Days = "a[data-bind*='timePeriodLast90days']";
	 public static final String sTimeRange_1Year =
	 "a[data-bind*='timePeriodLast1year']";
	public static final String sTimeRange_Custom = "a[data-bind*='timePeriodCustom']";
	public static final String sTimeRange_Latest = "a[data-bind*='timePeriodLatest']";
	 public static final String sTimeFilterIcon = "img[alt='Time Filter']";

	 public static final String sDayAll =
	 "input[id^='daysOptionAll_'].oj-checkbox";
	 public static final String sDayMonday =
	 "input[id^='Monday'].oj-checkbox";
	 public static final String sDayTuesday =
	 "input[id^='Tuesday'].oj-checkbox";
	 public static final String sDayWednesday =
	 "input[id^='Wednesday'].oj-checkbox";
	 public static final String sDayThursday =
	 "input[id^='Thursday'].oj-checkbox";
	 public static final String sDayFriday =
	 "input[id^='Friday'].oj-checkbox";
	 public static final String sDaySaturday =
	 "input[id^='Saturday'].oj-checkbox";
	
	 public static final String sMonthAll =
	 "input[id^='monthCheckAll_'].oj-checkbox";
	 public static final String sMonthJanuary =
	 "input[id^='January'].oj-checkbox";
	 public static final String sMonthFebruary =
	 "input[id^='February'].oj-checkbox";
	 public static final String sMonthMarch =
	 "input[id^='March'].oj-checkbox";
	 public static final String sMonthApril =
	 "input[id^='April'].oj-checkbox";
	 public static final String sMonthMay = "input[id^='May'].oj-checkbox";
	 public static final String sMonthJune = "input[id^='June'].oj-checkbox";
	 public static final String sMonthJuly = "input[id^='July'].oj-checkbox";
	 public static final String sMonthAugust =
	 "input[id^='August'].oj-checkbox";
	 public static final String sMonthSeptember =
	 "input[id^='September'].oj-checkbox";
	 public static final String sMonthOctober =
	 "input[id^='October'].oj-checkbox";
	 public static final String sMonthNovember =
	 "input[id^='Nobember'].oj-checkbox";
	 public static final String sMonthDecember =
	 "input[id^='December'].oj-checkbox";

	// Begin: Icon
	public static final String sStartTimeSelectIcon = "div[id^='divStartTime_'] .oj-inputdatetime-time-only .oj-inputdatetime-input-container .oj-inputdatetime-input-trigger .oj-inputdatetime-time-icon";
	public static final String sEndTimeSelectIcon = "div[id^='divEndTime_'] .oj-inputdatetime-time-only .oj-inputdatetime-input-container .oj-inputdatetime-input-trigger .oj-inputdatetime-time-icon";
	// End: Icon

	// Begin: Components
	public static final String sPickPanel = "[id^='pickerPanel_'].pickerPanel";
	// End: Components
}
