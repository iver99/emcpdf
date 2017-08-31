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

/**
 * @author shangwan
 */
public class UIControls
{
	//Begin: Text
	public static final String SSTARTTEXT = "//*[@id='start']";
	public static final String SENDTEXT = "//*[@id='end']";

	public static final String SSTARTTEXT_COMPACT = "//*[@id='startInCompact']";
	public static final String SENDTEXT_COMPACT = "//*[@id='endInCompact']";

	public static final String SSTARTTEXT_DATEONLY = "//*[@id='startWithDateOnly']";
	public static final String SENDTEXT_DATEONLY = "//*[@id='endWithDateOnly']";

	public static final String SSTARTTEXT4 = "//*[@id='start4']";
	public static final String SENDTEXT4 = "//*[@id='end4']";

	public static final String SSTARTTEXT5 = "//*[@id='start5']";
	public static final String SENDTEXT5 = "//*[@id='end5']";
	public static final String SSTARTTEXT6 = "//*[@id='start6']";
	public static final String SENDTEXT6 = "//*[@id='end6']";
	public static final String SFILTERINFO = "//*[@id='filterInfo']";
	public static final String SFILTERINFO_COMPACT = "//*[@id='filterInfoInCompact']";

	public static final String SSTARTTEXT_MILLIONSEC1 = "//*[@id='start1']";
	public static final String SENDTEXT_MILLIONSEC1 = "//*[@id='end1']";

	public static final String SSTARTTEXT_MILLIONSEC2 = "//*[@id='start2']";
	public static final String SENDTEXT_MILLIONSEC2 = "//*[@id='end2']";

	public static final String SSTARTTEXT_DATEONLY1 = "//*[@id='start1']";
	public static final String SENDTEXT_DATEONLY1 = "//*[@id='end1']";
	public static final String SSTARTTEXT_DATEONLY2 = "//*[@id='start2']";
	public static final String SENDTEXT_DATEONLY2 = "//*[@id='end2']";
	public static final String SSTARTTEXT_DATEONLY3 = "//*[@id='start3']";
	public static final String SENDTEXT_DATEONLY3 = "//*[@id='end3']";

	public static final String TIMERANGEBTN_CSS = ".oj-select-choice[id^='dropDown']";
	public static final String RECENTUSE_CSS = "a[data-bind*=timePeriodRecent]";
	public static final String RECENTUSECONTEXT_CSS = "[id^=\"recentPanel_\"] .oj-popup-content";

	public static final String CHANGEOPT_BTN1_CSS = "#changeOption1";
	public static final String CHANGEOPT_BTN2_CSS = "#changeOption2";

	public static final String DISABLE_OPT_CSS = ".drawer.drawerNotChosen.drawerDisabled";
	public static final String ENABLE_OPT_CSS = ".drawer.drawerNotChosen";
}
