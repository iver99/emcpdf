/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui.util;

/**
 * @author shangwan
 */
public class UIControls
{
	//Begin: Text
	public static final String SPRODUCTTEXT_CSS = ".emaas-appheader-appname[data-bind=\"text: productName\"]";
	public static final String SAPPTEXT_CSS = ".emaas-appheader-appname[data-bind=\"text: appName\"]";
	public static final String SPAGETEXT = "//*[@id='toolbar-left']";
	public static final String SHOMELABEL = "//*[@id='obbNavHomeLinks']/div[1]";
	public static final String SCLOUDSERVICELABEL = "//*[@id='obbNavCsLinks']/div[1]";
	public static final String SANALYZERLABEL = "//*[@id='obbNavVaLinks']/div[1]";
	public static final String SADMINLABEL = "//*[@id='obbNavAdminLinks']/div[1]";
	public static final String SWIDGETNAME = "/html/body/div[3]/div/div[2]/div/div[1]";
	public static final String SWIDGETDESC = "/html/body/div[3]/div/div[2]/div/div[2]";
	//public static final String SWIDGETWINDOWTITLE = "//*[@id='exploreDataMenu-mainContentsortcb']/li[1]/a";
	public static final String SWIDGETWINDOWTITLE_CSS = ".oj-dialog-header>span";
	//End: Text

	//Begin: Button
	public static final String SADDWIDGETBTN = "//*[@id='widget-selector-okbtn']";
	public static final String SCLOSEWIDGET = "/html/body/div[1]/div[2]/div/div[1]/div";
	public static final String SGENERATEMENUBTN = ".oj-button[title='_name_']";
	//End: Button

	//Begin: Icon
	public static final String SCOMPASSICON = "//*[@id='linksButton']";
	public static final String SADDWIDGETICON = "//*[@id='add-widget-button']";
	public static final String SHOMEICON = "//*[@id='iconHome']";
	public static final String SCLOUDSERVICEICON = "//*[@id='iconCloudServices']";
	public static final String SANALYZERICON = "//*[@id='iconAnalyzer']";
	public static final String SADMINICON = "//*[@id='iconCogs']";
	//End: Icon

	//Begin: Images
	public static final String SORACLEIMAGE = ".emaas-appheader-appname-table-cell-baseline>div";///html/body/div[2]/header/div/div[1]/div[1]/div[1]/div[1]/img";
	public static final String SWIDGETSELECT = "/html/body/div[1]/div[2]/div/div[2]/div[1]/div[2]/ul/li[1]/a/div";
	//End: Images

	//Begin: Links
	public static final String SHOMELINK = "//*[@id='obbNavDsbHome']";
	public static final String SCLOUDSERVICELINK = "//*[@id='obbNavCsLinks']/div[2]/a";
	public static final String SANALYZERLINK = "//*[@id='obbNavVaLinks']/div[2]/a";
	public static final String SADMINLINK = "//*[@id='obbNavAdminLinks']/div[2]/a";
	//End: Links

	//Begin: Components
	public static final String SLINKSMENU = "//*[@id='links_menu']";
	public static final String SHOME = "//*[@class='links-content-container']/div[1]";
	public static final String SCLOUDSERVICE = "//*[@class='links-content-container']/div[2]";
	public static final String SANALYZER = "//*[@class='links-content-container']/div[3]";
	public static final String SADMIN = "//*[@class='links-content-container']/div[4]";
	public static final String SWIDGET = "//*[@id='widgets-container']";
	public static final String SCATEGORYSELECT = "//*[@id='ojChoiceId_categorySelect_selected']";
	public static final String SWIDGETDISPLAY = "#widget-selector-widgets>ul";//"/html/body/div[1]/div[2]/div/div[2]/div[1]/div[2]/ul";
	//End: Components
}
