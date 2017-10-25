package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeUnit;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.UtilLoader;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class TimeSelectorUtil
{
	public static String getTimeRangeLabel(WebDriver webd)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.getTimeRangeLabel(webd);
	}

	public static String getTimeRangeLabel(WebDriver webd, int index)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.getTimeRangeLabel(webd, index);
	}
	
	public static String getTimeRangeLabel_V2(WebDriver webd)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.getTimeRangeLabel_V2(webd);
	}

	public static String getTimeRangeLabel_V2(WebDriver webd, int index)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.getTimeRangeLabel_V2(webd, index);
	}

	public static String setCustomTime(WebDriver webd, int index, String startDateTime, String endDateTime) throws Exception
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setCustomTime(webd, index, startDateTime, endDateTime);
	}

	//Date MM/dd/yyyy
	//Time hh:mm a
	public static String setCustomTime(WebDriver webd, String startDateTime, String endDateTime)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setCustomTime(webd, startDateTime, endDateTime);
	}

	public static String setCustomTimeWithDateOnly(WebDriver webd, int index, String startDateTime, String endDateTime)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setCustomTimeWithDateOnly(webd, index, startDateTime, endDateTime);
	}

	public static String setCustomTimeWithDateOnly(WebDriver webd, String startDateTime, String endDateTime)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setCustomTimeWithDateOnly(webd, startDateTime, endDateTime);
	}
	
	public static String setCustomTimeWithMillisecond(WebDriver webd, int index, String startDateTime, String endDateTime)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setCustomTimeWithMillisecond(webd, index, startDateTime, endDateTime);
	}

	public static String setCustomTimeWithMillisecond(WebDriver webd, String startDateTime, String endDateTime)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setCustomTimeWithMillisecond(webd, startDateTime, endDateTime);
	}

	public static String setFlexibleRelativeTimeRange(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setFlexibleRelativeTimeRange(webd, index, relTimeVal, relTimeUnit);
	}

	public static String setFlexibleRelativeTimeRange(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setFlexibleRelativeTimeRange(webd, relTimeVal, relTimeUnit);
	}

	public static String setFlexibleRelativeTimeRangeWithDateOnly(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setFlexibleRelativeTimeRangeWithDateOnly(webd, index, relTimeVal, relTimeUnit);
	}

	public static String setFlexibleRelativeTimeRangeWithDateOnly(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setFlexibleRelativeTimeRangeWithDateOnly(webd, relTimeVal, relTimeUnit);
	}
	
	public static String setFlexibleRelativeTimeRangeWithMillisecond(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setFlexibleRelativeTimeRangeWithMillisecond(webd, index, relTimeVal, relTimeUnit);
	}

	public static String setFlexibleRelativeTimeRangeWithMillisecond(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setFlexibleRelativeTimeRangeWithMillisecond(webd, relTimeVal, relTimeUnit);
	}

	public static String setTimeFilter(WebDriver webd, int index, String hoursToExclude, int[] daysToExclude,
			int[] monthsToExclude) throws Exception
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setTimeFilter(webd, index, hoursToExclude, daysToExclude, monthsToExclude);
	}

	public static String setTimeFilter(WebDriver webd, String hoursToExclude, int[] daysToExclude, int[] monthsToExclude)
			throws Exception

	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setTimeFilter(webd, hoursToExclude, daysToExclude, monthsToExclude);
	}

	public static String setTimeRange(WebDriver webd, int index, TimeRange rangeoption)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setTimeRange(webd, index, rangeoption);
	}

	public static String setTimeRange(WebDriver webd, TimeRange rangeoption)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setTimeRange(webd, rangeoption);
	}

	public static String setTimeRangeWithDateOnly(WebDriver webd, int index, TimeRange rangeOption)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setTimeRangeWithDateOnly(webd, index, rangeOption);
	}

	public static String setTimeRangeWithDateOnly(WebDriver webd, TimeRange rangeOption)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setTimeRangeWithDateOnly(webd, rangeOption);
	}
	
	public static String setTimeRangeWithMillisecond(WebDriver webd, int index, TimeRange rangeOption)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setTimeRangeWithMillisecond(webd, index, rangeOption);
	}

	public static String setTimeRangeWithMillisecond(WebDriver webd, TimeRange rangeOption)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setTimeRangeWithMillisecond(webd, rangeOption);
	}
}
