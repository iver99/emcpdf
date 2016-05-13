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
	public static final String DashboardDisplayPanelID = "dtabhomesc";
	public static final String DashboardCss = "div#dtabhomesc div.oj-panel";
	public static final String ErrorPageSingOutBtnCss = "button[title='Sign Out']";
	public static final String OverviewCloseID = "overviewClose";
	public static final String WidgetSearchBtnID = "dbd-left-panel-header-search-btn";
	public static final String ADashboardTestByAriaLabel = "//*[@aria-label='ADashboard Test']";
	public static final String EnterpriseOverviewByAriaLabel = "//*[@aria-label='Enterprise Overview']";

	//OOB dashboards ID
	public static final String Application_Performance_Monitoring_ID = "//div[@aria-dashboard='14']";
	public static final String Database_Health_Summary_ID = "//div[@aria-dashboard='11']";
	public static final String Host_Health_Summary_ID = "//div[@aria-dashboard='13']";
	public static final String Database_Performance_Analytics_ID = "//div[@aria-dashboard='2']";
	public static final String Middleware_Performance_Analytics_ID = "//div[@aria-dashboard='4']";
	public static final String Database_Resource_Analytics_ID = "//div[@aria-dashboard='3']";
	public static final String Middleware_Resource_Analytics_ID = "//div[@aria-dashboard='18']";
	public static final String WebLogic_Health_Summary_ID = "//div[@aria-dashboard='12']";
	public static final String Database_Configuration_and_Storage_By_Version_ID = "//div[@aria-dashboard='6']";
	public static final String Enterprise_OverView_ID = "//div[@aria-dashboard='1']";
	public static final String Host_Inventory_By_Platform_ID = "//div[@aria-dashboard='5']";
	public static final String Top_25_Databases_by_Resource_Consumption_ID = "//div[@aria-dashboard='8']";
	public static final String Top_25_WebLogic_Servers_by_Heap_Usage_ID = "//div[@aria-dashboard='9']";
	public static final String Top_25_WebLogic_Servers_by_Load_ID = "//div[@aria-dashboard='10']";
	public static final String WebLogic_Servers_by_JDK_Version_ID = "//div[@aria-dashboard='7']";
	public static final String Database_Operations_ID = "//div[@aria-dashboard='15']";
	public static final String Host_Operations_ID = "//div[@aria-dashboard='16']";
	public static final String Middleware_Operations_ID = "//div[@aria-dashboard='17']";

	//check box
	public static final String ITA_BoxID = "itaopt";

	//help id and about id
	public static final String MenuBtnID = "menubutton";
	public static final String SignOutID = "emcpdf_oba_logout";

	//Branding bar
	public static final String CompassIcon = "//*[@id='linksButton']";
	public static final String DashboardLink = "//*[@id='obbNavDsbHome']";

	//Branding bar link text
	public static final String BrandingBarLinkText_CS_ITA = "IT Analytics";
	public static final String BrandingBarLinkText_CS_APM = "APM";
	public static final String BrandingBarLinkText_CS_LA = "Log Analytics";
	public static final String BrandingBarLinkText_VA_ITA = "Analyze";
	public static final String BrandingBarLinkText_VA_TA = "Search";
	public static final String BrandingBarLinkText_VA_LA = "Log";
	public static final String BrandingBarLinkText_ADMIN_AGENT = "Agents";
	public static final String BrandingBarLinkText_ADMIN_ITA = "IT Analytics Administration";
	//=======
	//	public static final String HelpContentID = "get_started";//task
	//	public static final String AboutContentID = "/html/body/div[1]/div[2]/div/div[2]/div[2]/div[2]/p[2]";//"/html/body/div[1]/div[2]/div/div[4]/div[2]/div[2]/p[2]";
	//	public static final String AboutCloseID = "okButton";
	//
	//	//edit dashboard
	//	public static final String TimeRangeID = "/html/body/div[2]/div[2]/div/div/div[1]/div[1]/div/div/button";
	//	public static final String NameEditID = "builder-dbd-name-editor-btn";
	//	public static final String DescEditID = "builder-dbd-description-editor-btn";
	//	public static final String NameInputID = "builder-dbd-name-input";
	//	public static final String DescInputID = "builder-dbd-description-input";
	//	public static final String NameEditOKID = "builder-dbd-name-ok";
	//	public static final String DescEditOKID = "builder-dbd-description-ok";
	//
	//	//refresh //*[@id='ojChoiceId_autoRefreshSelect_selected
	//	public static final String AutoRefreshID = "//*[@id='ojChoiceId_autoRefreshSelect_selected']";
	//	public static final String AutoRefreshBy_15_Secs_ID = "/html/body/div[*]/div/div/ul/li[2]/div";//"/html/body/div[1]/div/div/ul/li[2]/div";//oj-listbox-result-label-2";
	//	public static final String AutoRefreshBy_30_Secs_ID = "/html/body/div[*]/div/div/ul/li[3]/div";//"/html/body/div[1]/div/div/ul/li[3]/div";//oj-listbox-result-label-3";
	//	public static final String AutoRefreshBy_1_Min_ID = "/html/body/div[*]/div/div/ul/li[4]/div";//"/html/body/div[1]/div/div/ul/li[4]/div";//oj-listbox-result-label-4";
	//	public static final String AutoRefreshBy_15_Mins_ID = "/html/body/div[*]/div/div/ul/li[5]/div";//"/html/body/div[1]/div/div/ul/li[5]/div";//oj-listbox-result-label-5";
	//
	//	//tile operation
	//	public static final String TileTitle = "/html/body/div[*]/div[2]/div[1]/div[*]/div/div[2]/div/div/div[*]/h2";//"/html/body/div[*]/div[2]/div[2]/div[*]/div/div[2]/div[*]/div/div[*]/h2";
	//	public static final String ConfigTileID = "/html/body/div[*]/div[2]/div[1]/div[*]/div/div[2]/div/div/div[1]/div/div[2]/button";// "/html/body/div[*]/div[2]/div[2]/div[*]/div/div[2]/div[*]/div/div[1]/div/div[2]/button";
	//	public static final String OpenTileID = "/html/body/div[*]/div[2]/div[1]/div[*]/div/div[2]/div/div/div[1]/div/div[1]/button";
	//	public static final String HideTileID = "/html/body/div[1]/div/ul/li[1]/a/span[2]";
	//	public static final String WiderTileID = "/html/body/div[1]/div/ul/li[3]/a/span[2]";
	//	public static final String NarrowerTileID = "/html/body/div[1]/div/ul/li[4]/a/span[2]";
	//	public static final String TallerTileID = "/html/body/div[1]/div/ul/li[5]/a/span[2]";
	//	public static final String ShorterTileID = "/html/body/div[1]/div/ul/li[6]/a/span[2]";
	//	public static final String MaximizeTileID = "/html/body/div[1]/div/ul/li[7]/a/span[2]";
	//	public static final String RestoreTileID = "/html/body/div[1]/div/ul/li[8]/a/span[2]";
	//	public static final String RemoveTileID = "/html/body/div[1]/div/ul/li[10]/a/span[2]";
	//
	//	//time picker
	//	public static final String TimePickerID = "/html/body/div[*]/div[2]/div[1]/div[1]/div/div[2]/div/span/span";//"/html/body/div[*]/div[2]/div[2]/div[2]/div[1]/div[2]/div/button";//"/html/body/div[3]/div[2]/div[2]/div[2]/div[1]/div[2]/div/button";///html/body/div[*]/div[2]/div/div/div[1]/div[1]/div/div/button";//"
	//	public static final String CustomDateTimeID = "/html/body/div[1]/div/div/div[1]/div/div[1]/div/a[11]";
	//	public static final String ApplyBtnID = "applyButton";
	//	public static final String CancelBtnID = "cancelButton";
	//	public static final String DateID1 = "/html/body/div[1]/div/div[2]/div/div/div/div[2]/div/div/div/div[1]/table/tbody/tr[4]/td[2]/a";//"/html/body/div[1]/div/div/div[1]/div/div[2]/div[2]/div/div/div[1]/table/tbody/tr[4]/td[2]/a";
	//	public static final String DateID2 = "/html/body/div[1]/div/div/div[1]/div/div[2]/div[2]/div/div/div[2]/table/tbody/tr[4]/td[3]/a";
	//
	//	//check external link
	//	public static final String ExternalLink = "/html/body/div[*]/header/div/div[1]/div[1]/div[1]/div[3]/span";
	//	public static final String ExternalTargetLinkID = "/html/body/div[4]/div[3]/div/div/div[7]/div/div[2]/div[2]/div[1]/div/div[1]/span";
	//
	//	//grid view and list view id
	//	public static final String GridViewID = "/html/body/div[*]/div/div[1]/div/div/div[2]/div[1]/span[2]/div[3]/span[1]/label";
	//	public static final String ListViewID = "/html/body/div[*]/div/div[1]/div/div/div[2]/div[1]/span[2]/div[3]/span[2]/label";
	//
	//	public static final String DashBoardListViewDashBoardID = "/html/body/div[2]/div/div[1]/div/div/div[2]/div[2]/table/tbody/tr/td[2]/a";
	//	public static final String DashBoardInfoID = "/html/body/div[*]/div/div[1]/div/div/div[2]/div[2]/table/tbody/tr/td[5]/button";
	//	public static final String DashBoardDeleteID = "/html/body/div[1]/div/div/div[1]/div/div/div/button";
	//	public static final String LV_DeleteBtnID_Dialog = "/html/body/div[1]/div[2]/div/div[3]/button[1]";//"/html/body/div[1]/div[2]/div/div[5]/button[1]";
	//
	//	public static final String WelcomeID = "/html/body/div[2]/div/div[1]";//div[@class='welcome-slogan']";//"";
	//
	//	//welcome page verify
	//	public static final String Welcome_APMLinkID = "/html/body/div[2]/div/div[2]/ul/li[1]/a/div/div[2]/div[1]";
	//	public static final String Welcome_LALinkID = "/html/body/div[2]/div/div[2]/ul/li[2]/a/div/div[2]/div[1]";
	//	public static final String Welcome_ITALinkID = "/html/body/div[2]/div/div[2]/ul/li[3]/div/div/div[2]/div[1]";
	//	public static final String Welcome_DashboardsLinkID = "/html/body/div[2]/div/div[2]/ul/li[4]/a/div/div[2]/div[1]";
	//	public static final String Welcome_DataExp = "/html/body/div[2]/div/div[2]/ul/li[5]/div/div/div[2]/div[1]";
	//	public static final String Welcome_ITA_SelectID = "ojChoiceId_ITA_options_selected";
	//	public static final String Welcome_DataExp_SelectID = "ojChoiceId_autogen1_selected";
	//	public static final String Welcome_LearnMore_getStarted = "//a[contains(text(),'How to get started')]";
	//	public static final String Welcome_LearnMore_Videos = "//a[contains(text(),'Videos')]";
	//	public static final String Welcome_LearnMore_ServiceOffering = "//a[contains(text(),'Service Offerings')]";
	//	public static final String Welcome_ITA_PADatabase = "/html/body/div[1]/div/div/ul/li[2]/div";//"oj-listbox-result-label-8";//ITA Select Item : Performance Analytics - Database
	//	public static final String Welcome_ITA_PAMiddleware = "/html/body/div[1]/div/div/ul/li[3]/div";//"oj-listbox-result-label-9";//ITA Select Item : Performance Analytics - Middleware
	//	public static final String Welcome_ITA_RADatabase = "/html/body/div[1]/div/div/ul/li[4]/div";//"oj-listbox-result-label-10";//ITA Select Item : Resource Analytics - Database
	//	public static final String Welcome_ITA_RAMiddleware = "/html/body/div[1]/div/div/ul/li[5]/div";//"oj-listbox-result-label-11";//ITA Select Item : Resource Analytics - Middleware
	//	public static final String Welcome_ITA_DEAnalyze = "/html/body/div[1]/div/div/ul/li[6]/div";//"oj-listbox-result-label-12";//ITA Select Item : Data Explorer - Analyze
	//	public static final String Welcome_ITA_DE = "/html/body/div[1]/div/div/ul/li[7]/div";//"oj-listbox-result-label-13";//ITA Select Item : Data Explorer
	//	public static final String Welcome_DataExp_Log = "/html/body/div[1]/div/div/ul/li[2]/div";//"oj-listbox-result-label-4";//Data Explorers Select Item : Log
	//	public static final String Welcome_DataExp_Analyze = "/html/body/div[1]/div/div/ul/li[3]/div";//"oj-listbox-result-label-5";//Data Explorers Select Item : Analyze
	//	public static final String Welcome_DataExp_Search = "/html/body/div[1]/div/div/ul/li[4]/div";//"oj-listbox-result-label-6";//Data Explorers Select Item : Search
	//
	//	//Sharing and stoping dashbaord
	//	public static final String option = "dashboardOptsBtn";
	//	public static final String dashboardshare = "emcpdf_dsbopts_share";//"//*[@id='ui-id-5']/span[2]";
	//	public static final String stopshare_btn = "emcpdf_dsbopts_share";//"//*[@id='ui-id-5']/span[2]";
	//>>>>>>> 160c9f7961de810ed40fc62d0e8080a544e5730e
}
