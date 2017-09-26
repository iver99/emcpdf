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
public class TimeSelectorUIControls
{
	// Begin: Text
	public static final String sErrorMsg = "div.errorMsg";
	public static final String sJetErrorMsg = "div.oj-message-error";
	public static final String sJetErrorMsgDetail = "div.oj-message-error .oj-message-content .oj-message-detail span";
	//public static final String sFilterInfoLabel = "//*[@id='filterInfo']";
	// End: Text

	// Begin: InputText
	public static final String sStartDateInput = "input[id^='inputStartDate_']";

	public static final String sStartTimeInput = "input[id^='inputStartTime_'].oj-inputdatetime-input";
	public static final String sEndDateInput = "input[id^='inputEndDate_']";
	public static final String sEndTimeInput = "input[id^='inputEndTime_'].oj-inputdatetime-input";
	//public static final String sTimeExcludedInput = "input.oj-col.oj-sm-10.oj-md-10.oj-inputtext-input";
	// End: InputText
	// Begin: Button
	public static final String sApplyBtn = "button[id^='applyButton']";

	public static final String sCancelBtn = "button[id^='cancelButton']";
	public static final String sDateTimePick = "[id^='dateTimePicker_']";

	public static final String sTimeRangeBtn = ".oj-select-choice[id^='dropDown']";//".dropdown[id^='dropDown']";"[id^='dateTimePicker_'] .oj-select-choice .oj-select-arrow";
	public static final String sDisplayDateTime = "[id^='dateTimePicker_'] .oj-select-choice .oj-select-chosen";
	//	public static final String sFilterInfoIndicator = "[id^='tfInfoIndicator_'].time-filter-indicator";
	//	public static final String sFilterInfo = "div[id^='tfInfo_'] [data-bind='html: timeFilterInfo']";
	public static final String sTimeRange_15Min = "a[data-bind*='timePeriodLast15mins']";

	public static final String sTimeRange_30Min = "a[data-bind*='timePeriodLast30mins']";
	public static final String sTimeRange_60Min = "a[data-bind*='timePeriodLast60mins']";
	public static final String sTimeRange_2Hour = "a[data-bind*='timePeriodLast2hour']";
	public static final String sTimeRange_4Hour = "a[data-bind*='timePeriodLast4hours']";
	public static final String sTimeRange_6Hour = "a[data-bind*='timePeriodLast6hours']";
	public static final String sTimeRange_1Day = "a[data-bind*='timePeriodLast1day']";
	public static final String sTimeRange_7Days = "a[data-bind*='timePeriodLast7days']";
	public static final String sTimeRange_14Days = "a[data-bind*='timePeriodLast14days']";
	public static final String sTimeRange_30Days = "a[data-bind*='timePeriodLast30days']";
	public static final String sTimeRange_90Days = "a[data-bind*='timePeriodLast90days']";
	public static final String sTimeRange_1Year = "a[data-bind*='timePeriodLast1year']";
	public static final String sTimeRange_Custom = "a[data-bind*='timePeriodCustom']";
	public static final String sTimeRange_Latest = "a[data-bind*='timePeriodLatest']";
	public static final String sTimeRange_24Hours = "a[data-bind*='timePeriodLast24hours']";
	public static final String sTimeRange_12Months = "a[data-bind*='timePeriodLast12months']";
	public static final String sTimeRange_8Hours = "a[data-bind*='timePeriodLast8hours']";
	public static final String sTimeRange_New60Mins = "a[data-bind*='timePeriodLast60mins']";
	public static final String sTimeRange_New7Days = "a[data-bind*='timePeriodLast7days']";
	//	public static final String sTimeFilterIcon = "img[alt='Time Filter']";
	// Begin: Icon
	public static final String sStartTimeSelectIcon = "div[id^='divStartTime_'] .oj-inputdatetime-time-only .oj-inputdatetime-input-container .oj-inputdatetime-input-trigger .oj-inputdatetime-time-icon";

	//	public static final String sDayAll = "input[id^='daysOptionAll_'].oj-checkbox";
	//	public static final String sDayMonday = "input[id^='Monday'].oj-checkbox";
	//	public static final String sDayTuesday = "input[id^='Tuesday'].oj-checkbox";
	//	public static final String sDayWednesday = "input[id^='Wednesday'].oj-checkbox";
	//	public static final String sDayThursday = "input[id^='Thursday'].oj-checkbox";
	//	public static final String sDayFriday = "input[id^='Friday'].oj-checkbox";
	//	public static final String sDaySaturday = "input[id^='Saturday'].oj-checkbox";
	//
	//	public static final String sMonthAll = "input[id^='monthCheckAll_'].oj-checkbox";
	//	public static final String sMonthJanuary = "input[id^='January'].oj-checkbox";
	//	public static final String sMonthFebruary = "input[id^='February'].oj-checkbox";
	//	public static final String sMonthMarch = "input[id^='March'].oj-checkbox";
	//	public static final String sMonthApril = "input[id^='April'].oj-checkbox";
	//	public static final String sMonthMay = "input[id^='May'].oj-checkbox";
	//	public static final String sMonthJune = "input[id^='June'].oj-checkbox";
	//	public static final String sMonthJuly = "input[id^='July'].oj-checkbox";
	//	public static final String sMonthAugust = "input[id^='August'].oj-checkbox";
	//	public static final String sMonthSeptember = "input[id^='September'].oj-checkbox";
	//	public static final String sMonthOctober = "input[id^='October'].oj-checkbox";
	//	public static final String sMonthNovember = "input[id^='Nobember'].oj-checkbox";
	//	public static final String sMonthDecember = "input[id^='December'].oj-checkbox";

	public static final String sEndTimeSelectIcon = "div[id^='divEndTime_'] .oj-inputdatetime-time-only .oj-inputdatetime-input-container .oj-inputdatetime-input-trigger .oj-inputdatetime-time-icon";
	// End: Icon
	// Begin: Components
	public static final String sPickPanel = ".pickerPanel";//"[id^='pickerPanel_'].pickerPanel";
	// End: Components

	// Begin: Time Filter
	public static final String sTimeFilterIcon = "div[id^='timeFilterIcon_']";

	public static final String sTimeFilterHoursFilter = "div[id^='hoursFilter_']>div>input";
	public static final String sTimeFilterDaysFilterAll = "input[id^='daysOptionAll_']";
	public static final String sTimeFilterMonthsFilterAll = "input[id^='monthCheckAll_']";
	public static final String sTimeFilterDaysMonthsFilterPrefix = "input[id^='";
	public static final String sTimeFilterDaysMonthsFilterSuffix = "']";
	// End: Time Filter
	// Begin: Flexible Relative Time Period
	public static final String sLastRadio = "div[id^='pickerPanel'] input[value='flexRelTimeCtrl']";

	public static final String sFlexRelTimeVal = "div[id^='pickerPanel'][id$='wrapper'] input[id^='flexRelTimeVal']";
	public static final String sFlexRelTimeOpt = "div[id^='pickerPanel'][id$='wrapper'] div[id^='flexRelTimeOpt'] .oj-select-choice";
	public static final String sFlexRelTimeOptList = "div[id^=pickerPanel][id$='wrapper'] div[id^='pickerPanel'] select option";
	public static final String sFlexRelTimeOptStart = "div[id^='pickerPanel'][id$='_wrapper_layer'] div[data-oj-containerid^='flexRelTimeOpt'] ul li:nth-of-type(";
	public static final String sFlexRelTimeOptEnd = ")";
	public static final String sRangeRadio = "div[id^='pickerPanel'] input[value='timeLevelCtrl']";
	
	public static final String sLatestRadio = "div[id^='pickerPanel'] input[value='latestOnCustom']";
	// End: Flexible Relative Time Period

	private TimeSelectorUIControls()
	{
	}

}
