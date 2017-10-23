package oracle.sysman.emaas.platform.dashboards.test.ui.util;

public class PageId
{
	/*
	 *
	 *The following variable define the all elements id of Dashboard page, including the home page, create page
	 *
	 *
	 *
	 * */
	//home page
	//public static final String DashboardDisplayPanelID = "dtabhomesc";
	public static final String DASHBOARDDISPLAYPANELCSS = ".dbs-summaries-container";

	public static final String DASHBOARDCSS = "div#dtabhomesc div.oj-panel";
	public static final String ERRORPAGESINGOUTBTNCSS = "button[title='Sign Out']";
	public static final String OVERVIEWCLOSEID = "overviewClose";
	public static final String WIDGETSEARCHBTNID = "dbd-left-panel-header-search-btn";
	public static final String ADASHBOARDTESTBYARIALABEL = "//*[@aria-label='ADashboard Test']";
	public static final String ENTERPRISEOVERVIEWBYARIALABEL = "//*[@aria-label='Enterprise Overview']";
	public static final String SORTBYID = "oj-select-choice-mainContentsortcb";
	public static final String DASHBOARDINFO_NAME_CSS = ".dsbinfopop [data-bind=\"with: selectedDashboard\"] [data-bind=\"text: dashboard.name\"]";
	public static final String DASHBOARDINFO_DESC_CSS = ".dsbinfopop [data-bind=\"with: selectedDashboard\"] [data-bind*=\"html: dashboard.description\"]";
	public static final String DASHBOARD_HOME_ERRMSG_CSS = ".oj-message.oj-message-error";
	public static final String DASHBOARD_CREATE_DIALOG_CSS = "#ojDialogWrapper-cDsbDialog";

	//OOB dashboards ID
	public static final String APPLICATION_PERFORMANCE_MONITORING_ID = "//div[@aria-dashboard='14']";

	//public static final String Database_Health_Summary_ID = "//div[@aria-dashboard='11']";
	//public static final String Host_Health_Summary_ID = "//div[@aria-dashboard='13']";
	public static final String DATABASE_PERFORMANCE_ANALYTICS_ID = "//div[@aria-dashboard='2']";
	public static final String MIDDLEWARE_PERFORMANCE_ANALYTICS_ID = "//div[@aria-dashboard='4']";
	public static final String DATABASE_RESOURCE_ANALYTICS_ID = "//div[@aria-dashboard='3']";
	public static final String MIDDLEWARE_RESOURCE_ANALYTICS_ID = "//div[@aria-dashboard='18']";
	//public static final String WebLogic_Health_Summary_ID = "//div[@aria-dashboard='12']";
	public static final String DATABASE_CONFIGURATION_AND_STORAGE_BY_VERSION_ID = "//div[@aria-dashboard='6']";
	public static final String ENTERPRISE_OVERVIEW_ID = "//div[@aria-dashboard='1']";
	public static final String HOST_INVENTORY_BY_PLATFORM_ID = "//div[@aria-dashboard='5']";
	public static final String TOP_25_DATABASES_BY_RESOURCE_CONSUMPTION_ID = "//div[@aria-dashboard='8']";
	public static final String TOP_25_WEBLOGIC_SERVERS_BY_HEAP_USAGE_ID = "//div[@aria-dashboard='9']";
	public static final String TOP_25_WEBLOGIC_SERVERS_BY_LOAD_ID = "//div[@aria-dashboard='10']";
	public static final String WEBLOGIC_SERVERS_BY_JDK_VERSION_ID = "//div[@aria-dashboard='7']";
	public static final String DATABASE_OPERATIONS_ID = "//div[@aria-dashboard='15']";
	public static final String HOST_OPERATIONS_ID = "//div[@aria-dashboard='16']";
	public static final String MIDDLEWARE_OPERATIONS_ID = "//div[@aria-dashboard='17']";
	
	public static final String DASHBOARDEDITBUTTON = "//button[@title='Edit Settings']";
	public static final String DASHBOARDNAME = ".//*[@id='dbd-edit-settings-container']/span[2]";
	public static final String DASHBOARDNAME_CSS = ".edit-setting-link.edit-dsb-link";
	public static final String DASHBOARDFILTERS = "//span[contains(text(),'Dashboard Filters')]";
	public static final String DASHBOARDENTITIES = "//label[text()='Entities']";
	
	public static final String ENABLEENTITYFILTER = "//*[@id='enableEntityFilter']";
	public static final String ENABLEGCENTITYFILTER = "//*[@id='enableGCEntities']";
	public static final String DISABLEENTITYFILTER = "//*[@id='disableEntityFilter']";
	public static final String ENTITYBUTTON = "//*[@aria-label='Tartget Seletor']";
	public static final String SELECTBUTTON = "//*[@id='emcta_tgtSel0_ok']";
	public static final String CANCELBUTTON = "//*[@id='emcta_tgtSel0_cancel']";	
	
	//check box
	public static final String ITA_BOXID = "itaopt";

	//help id and about id
	public static final String MENUBTNID = "menubutton";

	public static final String SIGNOUTID = "emcpdf_oba_logout";
	//Branding bar
	public static final String COMPASSICON = "//*[@id='linksButton']";

	public static final String DASHBOARDLINK = "//*[@id='obbNavDsbHome']";
	//Branding bar link text
	public static final String BRANDINGBARLINKTEXT_CS_ITA = "IT Analytics";

	public static final String BRANDINGBARLINKTEXT_CS_APM = "APM";
	public static final String BRANDINGBARLINKTEXT_CS_LA = "Log Analytics";
	public static final String BRANDINGBARLINKTEXT_VA_ITA = "Analyze";
	public static final String BRANDINGBARLINKTEXT_VA_TA = "Search";
	public static final String BRANDINGBARLINKTEXT_VA_LA = "Log";
	public static final String BRANDINGBARLINKTEXT_ADMIN_AGENT = "Agents";
	public static final String BRANDINGBARLINKTEXT_ADMIN_ITA = "IT Analytics Administration";
	//builder page
	public static final String DASHBOARDSETADDDASHBOARDICON_CSS = ".dbd-icon-add";

	public static final String DATETIMEPICK_CSS = "[id^='dateTimePicker_']";
	public static final String TARGETSELECTOR_CSS = ".df-target-selector.df-target-selector";
	public static final String DASHBOARDSETOPTIONS_CSS = "#tabs-caret";
	public static final String DASHBOARDSETOPTIONSEDIT_CSS = "#dbs-edit [dashboardset-option='Edit']";
	public static final String RIGHTDRAWEREDITSINGLEDBSHARE_CSS = ".dbd-right-panel-editdashboard-set-share .dbd-right-panel-editdashboard-innertitle.oj-collapsible-header";
	public static final String DASHBOARDSETSHARE_CSS = "#dashboardSetSharingShared";
	public static final String DASHBOARDSETNOTSHARE_CSS = "#dashboardSetSharingNotShared";
	public static final String DASHBOARDOPTIONS_CSS = ".dashboard-content.page-break:not([style='visibility: visible; display: none;']) button[id^='dashboardOptsBtn_'].dashboardOptsBtn";//".dashboard-content.page-break[style='visibility: visible;'] button[id^='dashboardOptsBtn_'].dashboardOptsBtn"; //"button.dashboardOptsBtn";
	public static final String DASHBOARDTITLE_CSS = "#pageTitle";

	public static final String DATETIMEPICKER_OOB_CSS = "#dateTimePicker_APA";

	public static final String DASHBOARD_BUILDER_ERRMSG_CSS = ".emaas-appheader-message-box.emaas-appheader-message-box-error";

	//error page
	public static final String ERRORMESSAGE_CSS = ".dbd-error-msg";
	public static final String ERRORURL_CSS = "div[data-bind=\"visible: invalidUrl\"]";
	public static final String DEFAULTHOME_CSS = "div[data-bind=\"visible: defaultHomeLinkVisible\"]";
	public static final String DEFAULTHOMEURL_CSS = ".dbd-error-url[href=\"/emsaasui/emcpdfui/welcome.html\"]";
	
	//UDE page
	public static final String SAVEBUTTON_UDE = "//*[@id='save_widget_btn']";
	//LA page
	public static final String RUNBUTTON_LA = "//*[@id='srchSrch']";
	public static final String NOTIFICATIONBUTTON_LA = "//*[@id='notificationButton']";

	
	public static final String DASHBOARD_CREATE_DIALOG_CANCEL_CSS = "#cancelCreateDsb";

	//get started icon in builder page
	public static final String DASHBOARD_GET_START_ICON_CSS = ".tooltip-add-widget div";
}
