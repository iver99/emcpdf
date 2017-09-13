package oracle.sysman.emaas.platform.dashboards.tests.ui.util;

public class DashBoardPageId
{
	/*
	 *
	 *The following variable define the all elements id of Dashboard page, including the home page, create page
	 *
	 *
	 *
	 * */
	public static final String CREATEDSBUTTONID = "cbtn";
	public static final String BUILDERTILEMAXMINLOCATOR = "following-sibling::*//button[contains(@class, 'dbd-tile-maxmin')]";
	public static final String CREATEDSOFDIALOGID = "createDsb";
	public static final String DASHBOARDNAMEBOXID = "dbsHNameIn";
	public static final String DASHBOARDDESCBOXID = "dbsHDpIn";
	public static final String DASHOKBUTTONID = "createDsb";
	public static final String DASHSAVEBUTTONID = "createDsb-2";
	public static final String DASHBOARDTYPE_SINGLE = "dashboardtype-single";
	public static final String DASHBOARDTYPE_SET = "dashboardtype-set";
	public static final String ERRORMSGSUMMARYCSS = ".oj-message-summary";
	public static final String ERRORMSGDETAILCSS = ".oj-message-detail";
	//public static final String WidgetAddButtonID = "add-widget-button";
	public static final String OPTIONSID = "dashboardOptsBtn";
	public static final String DASHBOARDSAVEID = "dashboard-screenshot";
	public static final String WIDGETADDBUTTON = "emcpdf_dsbopts_add";//"//div[@id='ojDialogWrapper-widgetDetailsDialog']/div[5]/button";////div[18]/div[5]/button/span[text()='Add']";
	public static final String DASHBOARDEDIT = "//*[@id='emcpdf_dsbopts_edit']";

	public static final String DASHBOARDDUPLICATE = "//*[@id='emcpdf_dsbopts_duplicate']";
	public static final String DASHBOARDDELETE = "//*[@id='emcpdf_dsbopts_delete']";
	public static final String DASHBOARDHOME = "//*[@id='emcpdf_dsbopts_home']";
	public static final String DASHBOARDFAVORITE = "//*[@id='emcpdf_dsbopts_favorites']";
	public static final String WIDGETDIALOGCLOSEBUTTONID = "div[15]/div[3]/div/span";
	public static final String DASHBOARDID = "//div[@class='oj-panel dbs-summary-container']";// and @aria-dashboard='1127']";
	public static final String ELEMENTID = "//*[@id=\"dtabhomesc\"]/div[1]";
	public static final String DELETEBTNID = "//*[@id=\"dtabhomesc\"]/div[1]/div[1]/div[2]/button";//div[@id='dtabhomesc']/div/div/div[2]/button";///html/body/div[2]/div/div/div/div/div[3]/div/div/div[2]/button";//"//button[@class='oj-button-half-chrome oj-sm-float-end oj-button oj-component oj-enabled oj-button-icon-only oj-component-initnode oj-default']/span[@class='oj-button-icon oj-start icon-delete-ena-16 oj-fwk-icon']";
	public static final String DELETEBTNID_DIALOG = "/html/body/div[1]/div[2]/div/div[3]/button[1]";//"//div[@id='ojDialogWrapper-dbs_cfmDialog']/div[5]/button";
	public static final String OVERVIEWCLOSEID = "overviewClose";
	//	public static final String DashBoardName = "//span[@id='builder-dbd-name-display-hover-area']";
	public static final String ADDBTN = "widget-selector-okbtn";//
	public static final String CLOSEBTNID = "/html/body/div[1]/div[2]/div/div[1]/div/span";///html/body/div[1]/div[2]/div/div[3]/div/span";//div[contains(@id,'ojDialogWrapper-ui-id') and @class='oj-dialog oj-component oj-draggable']/div[3]/div/span"; // ";";
	public static final String LINKID = "linksButton";
	public static final String DASHBOARDLINKID = "link=All Dashboards";
	public static final String DELETEBTN_CFMDIALOG = "/html/body/div[1]/div[2]/div/div[3]/button[1]";
	public static final String DASHBOARDSERACHRESULT_PANELID = "dtabhomesc";
	public static final String WIDGETSEARCHINPUTID = "widget-search-input";

	public static final String WIDGETSEARCHBTNID = "dbd-left-panel-header-search-btn";
	//OOB dashboards ID
	//OOB dashboards ID
	public static final String APPLICATION_PERFORMANCE_MONITORING_CSS = ".dbs-summary-page-image[alt='Application Performance Monitoring']";

	//public static final String Database_Health_Summary_CSS = ".dbs-summary-page-image[alt='Database Health Summary']";
	//public static final String Host_Health_Summary_CSS = ".dbs-summary-page-image[alt='Host Health Summary']";
	public static final String DATABASE_PERFORMANCE_ANALYTICS_CSS = ".dbs-summary-page-image[alt='Performance Analytics: Database']";
	public static final String MIDDLEWARE_PERFORMANCE_ANALYTICS_CSS = ".dbs-summary-page-image[alt='Performance Analytics Application Server']";
	//public static final String Middleware_Datasource_Performance_Analytics_ID = "//div[@aria-dashboard='19']";
	public static final String DATABASE_RESOURCE_ANALYTICS_CSS = ".dbs-summary-page-image[alt='Resource Analytics: Database']";
	public static final String MIDDLEWARE_RESOURCE_ANALYTICS_CSS = ".dbs-summary-page-image[alt='Resource Analytics: Middleware']";
	//public static final String WebLogic_Health_Summary_CSS = ".dbs-summary-page-image[alt='WebLogic Health Summary']";
	public static final String DATABASE_CONFIGURATION_AND_STORAGE_BY_VERSION_CSS = ".dbs-summary-page-image[alt='Database Configuration and Storage By Version']";
	public static final String ENTERPRISE_OVERVIEW_CSS = ".dbs-summary-page-image[alt='Enterprise Overview']";
	//	public static final String Host_Inventory_By_Platform_CSS = ".dbs-summary-page-image[alt='Host Inventory By Platform']";
	//	public static final String Top_25_Databases_by_Resource_Consumption_CSS = ".dbs-summary-page-image[alt='Top 25 Databases by Resource Consumption']";
	//	public static final String Top_25_WebLogic_Servers_by_Heap_Usage_CSS = ".dbs-summary-page-image[alt='Top 25 WebLogic Servers by Heap Usage']";
	//	public static final String Top_25_WebLogic_Servers_by_Load_CSS = ".dbs-summary-page-image[alt='Top 25 WebLogic Servers by Load']";
	//	public static final String WebLogic_Servers_by_JDK_Version_CSS = ".dbs-summary-page-image[alt='WebLogic Servers by JDK Version']";
	public static final String DATABASE_OPERATIONS_CSS = ".dbs-summary-page-image[alt='Database Operations']";
	public static final String HOST_OPERATIONS_CSS = ".dbs-summary-page-image[alt='Host Operations']";
	public static final String MIDDLEWARE_OPERATIONS_CSS = ".dbs-summary-page-image[alt='Middleware Operations']";
	public static final String SORTDROPLISTID = "//*[@id='ojChoiceId_sortcb_selected']";//ojChoiceId_sortcb";//ojChoiceId_sortcb_selected";//sortcb";

	public static final String ACCESS_DATE_ID = "/html/body/div/div/div/ul/li[2]/div";
	public static final String BRANDINGBARDASHBOARDLINKLOCATOR = "//*[@id='links_menu']//*[contains(@data-bind, 'dashboardLinkLabel')]";

	// filter check boxes
	public static final String FILTERAPMLOCATOR = "//*[@id='apmopt']";

	public static final String FILTERITALOCATOR = "//*[@id='itaopt']";
	public static final String FILTERLALOCATOR = "//*[@id='laopt']";
	public static final String FILTERORCHESTRATIONLOCATOR = "//*[@id='ocsopt']";
	public static final String FILTERSECURITYLOCATOR = "//*[@id='secopt']";
	public static final String FILTERORACLELOCATOR = "//*[@id='oracleopt']";
	public static final String FILTERSHARELOCATOR = "//*[@id='shareopt']";
	public static final String FILTERMELOCATOR = "//*[@id='otheropt']";
	public static final String FILTERFAVORITELOCATOR = "//*[@id='myfavorites']";

	// sort by
	public static final String SORTBYSELECTLOCATOR = "//div[not(contains(@style,'display:none'))]//*[contains(@class, 'dsb-sortcb')]//*[contains(@class, 'oj-select-chosen')]";
	public static final String SORTBYDEFAULTLOCATOR = "/html/body/div/div/div/ul/li[1]/div";
	public static final String SORTBYNAMEASCLOCATOR = "/html/body/div/div/div/ul/li[2]/div";
	public static final String SORTBYNAMEDSCLOCATOR = "/html/body/div/div/div/ul/li[3]/div";
	public static final String SORTBYCREATEDBYASCLOCATOR = "/html/body/div/div/div/ul/li[4]/div";
	public static final String SORTBYCREATEDBYDSCLOCATOR = "/html/body/div/div/div/ul/li[5]/div";
	public static final String SORTBYCREATEDATEASCLOCATOR = "/html/body/div/div/div/ul/li[6]/div";
	public static final String SORTBYCREATEDATEDSCLOCATOR = "/html/body/div/div/div/ul/li[7]/div";
	public static final String SORTBYLASTMODIFIEDASCLOCATOR = "/html/body/div/div/div/ul/li[8]/div";
	public static final String SORTBYLASTMODIFIEDDSCLOCATOR = "/html/body/div/div/div/ul/li[9]/div";
	public static final String SORTBYLASTACCESSASCLOCATOR = "/html/body/div/div/div/ul/li[10]/div";
	public static final String SORTBYLASTACCESSDSCLOCATOR = "/html/body/div/div/div/ul/li[11]/div";
	//search
	//public static final String SearchDSBoxID = "sinput";
	public static final String SEARCHDASHBOARDINPUTLOCATOR = "//div[not(contains(@style,'display:none'))]//*[contains(@class, 'dbs-sinput')]";

	public static final String SEARCHDASHBOARDSEARCHBTNLOCATOR = "//div[not(contains(@style,'display:none'))]//*[contains(@class, 'dbs-search-icon')]";
	//Dahobrd home Views
	public static final String DASHBOARDSGRIDVIEWLOCATOR = "//div[not(contains(@style,'display:none'))]//*[contains(@class, 'icon-gridview-16')]";

	public static final String DASHBOARDSLISTVIEWLOCATOR = "//div[not(contains(@style,'display:none'))]//*[contains(@class, 'icon-listview-16')]";
	public static final String LISTVIEWTABLENAMEHEADERLOCATOR = "//div[not(contains(@style,'display:none'))]//*[contains(@class, 'oj-table-column-header-cell') and @abbr='Name']";
	public static final String LISTVIEWTABLECREATEDBYHEADERLOCATOR = "//div[not(contains(@style,'display:none'))]//*[contains(@class, 'oj-table-column-header-cell') and @abbr='Created By']";
	public static final String LISTVIEWTABLELASTMODIFIEDHEADERLOCATOR = "//div[not(contains(@style,'display:none'))]//*[contains(@class, 'oj-table-column-header-cell') and @abbr='Last Modified']";
	public static final String LISTVIEWSORTLOCATORCSS = "a.oj-table-column-header-asc-link";
	//Dashboard
	public static final String DASHBOARDNAMELOCATOR = "//div[not(contains(@style,'display:none'))]//*[contains(@class, 'dbs-dsbnameele') and (@aria-label = '_name_' or text() = '_name_')]";//[contains(@class, 'dbs-dsbnameele')]//text()[. = '_name_']";//*[contains(@class, 'icon-listview-16')]";

	public static final String OOBDASHBOARDNAMELOCATOR = "//div[not(contains(@style,'display:none'))]//*[contains(@class, 'dbs-dsbsystem') and (@aria-label = '_name_' or text() = '_name_')]";
	public static final String DASHBOARDNAMECONTAINERS = "//div[not(contains(@style,'display:none'))]//*[contains(@class, 'dbs-dsbnameele')]";
	public static final String DASHBOARDNAMEINDEXLOCATOR = "xpath=(//div[not(contains(@style,'display:none'))]//*[contains(@class, 'dbs-dsbnameele')])[_index_]";
	
	public static final String DASHBOARDOFLONGNAMELOCATOR = "//div[contains(@style, 'word-wrap: break-word') and @data-bind='text: dashboard.name']";
	//Branding Bar links
	//Home Link
	//public static final String HomeLinkID = "/html/body/div[*]/header/div/div[1]/div[3]/div/div/div[1]/div[2]/div[1]/a";
	public static final String HOMELINKID = "//a[contains(text(),'Home')]";

	//dashboard home link
	//public static final String DashBoardHomeLinkID = "/html/body/div[*]/header/div/div[1]/div[3]/div/div/div[1]/div[2]/div[2]/a";
	public static final String DASHBOARDHOMELINKID = "//a[contains(text(),'Dashboards')]";

	//My Favorites link
	//public static final String MyFavoritesLinkID = "/html/body/div[*]/header/div/div[1]/div[3]/div/div/div[1]/div[2]/div[3]/a";
	public static final String MYFAVORITESLINKID = "//a[contains(text(),'My Favorites')]";

	//IT Analytics link
	//public static final String ITALinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[2]/div[2]/div[2]/a";
	public static final String ITALINKID = "//a[contains(text(),'IT Analytics')]";
	//Log Analytics link
	//public static final String LALinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[2]/div[2]/div[3]/a";
	public static final String LALINKID = "//a[contains(text(),'Log Analytics')]";
	//APM link
	//public static final String APMLinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[2]/div[2]/div[4]/a";
	public static final String APMLINKID = "//a[contains(text(),'APM')]";
	//Log link
	//public static final String LOGLinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[3]/div[2]/div[2]/a";
	public static final String LOGLINKID = "//a[contains(text(),'Log')]";
	//AWR Analytics link
	//public static final String AWRALinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[3]/div[2]/div[3]/a";
	//Analyze link
	//public static final String AnalyzeLinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[3]/div[2]/div[3]/a";
	public static final String ANALYZELINKID = "//a[contains(text(),'Analyze')]";

	//Search link
	//public static final String SearchLinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[3]/div[2]/div[4]/a";
	public static final String SEARCHLINKID = "//a[contains(text(),'Search')]";
	//Agents link
	//public static final String AgentsLinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[4]/div[2]/div[3]/a";
	public static final String AGENTSLINKID = "//a[contains(text(),'Agents')]";
	//IT Analytics Administration link
	//public static final String ITA_Admin_LinkID ="/html/body/div[*]/header/div/div/div[3]/div/div/div[4]/div[2]/div[4]/a";
	public static final String ITA_ADMIN_LINKID = "//a[contains(text(),'IT Analytics Administration')]";
	//check box
	public static final String ITA_BOXID = "itaopt";

	public static final String LA_BOXID = "laopt";
	public static final String APM_BOXID = "apmopt";
	public static final String ORACLE_BOXID = "oracleopt";
	public static final String OTHER_BOXID = "otheropt";
	public static final String SHARE_BOXID = "shareopt";
	public static final String FAVORITE_BOXID = "myfavorites";
	//id of dashboard name /html/body/div[2]/div/div/div/div/div/span/h1 /html/body/div[2]/div/div/div/div[2]/div/span/span,/html/body/div[3]/div/div/div/div/div/span/h1;/html/body/div[2]/div/div/div/div[2]/div/span/span
	public static final String DASHBOARDNAMEID = "/html/body/div[*]/div/div/div/div/div/span/h1";

	public static final String MDASHBOARDNAMEID = "/html/body/div[3]/div/div/div/div/div/span/h1";
	//id of dashboard desc
	public static final String DASHBOARDDESCID = "/html/body/div[*]/div/div/div/div[2]/div/span/span";
	public static final String MDASHBOARDDESCID = "/html/body/div[3]/div/div/div/div[2]/div/span/span";
	//remove dashboard/html/body/div[3]/div/div/div/div/div[2]/div[3]/div/div[2]/div[2]/button
	//Note: below xpath is used to identify this 1st dashboard only
	public static final String INFOBTNID = "//button[@title='Dashboard Information']";//"/html/body/div[*]/div/div[1]/div/div/div[2]/div[2]/div/div[2]/div[2]/button";//"/html/body/div[*]/div/div[1]/div/div/div[2]/div[2]/div/div[2]/div[2]/button";///html/body/div[*]/div/div/div/div/div[2]/div[3]/div/div[2]/div[2]/button";

	public static final String RMBTNID = ".dbs-summary-page-info-pop button[name=dsbinfopop_delete]";
	//Explore Data
	public static final String EXPLOREDATABTNID = "exploreDataBtn";

	public static final String EXPLOREDATAMENU = "exploreDataMenu";

	//Log button
	public static final String EXPLORE_LOG = "//a[text()='Log Visual Analyzer' and @role='menuitem']";

	//Search button in explore menu
	public static final String EXPLORE_Search = "//a[text()='Search' and @role='menuitem']";

	//help id and about id
	public static final String MENUBTNID = "menubutton";

	public static final String HELPID = "emcpdf_oba_help";
	public static final String ABOUTID = "emcpdf_oba_about";
	public static final String SIGNOUTID = "emcpdf_oba_logout";
	public static final String HELPCONTENTID = "get_started";//task
	public static final String ABOUTCONTENTID = "/html/body/div[1]/div[2]/div/div[2]/div[2]/div[2]/p[2]";//"/html/body/div[1]/div[2]/div/div[4]/div[2]/div[2]/p[2]";
	public static final String ABOUTCLOSEID = "okButton";
	//edit dashboard
	public static final String TIMERANGEID = "/html/body/div[2]/div[2]/div/div/div[1]/div[1]/div/div/button";
	public static final String EDITBTNCSS = "button[id^='rightpanel-pencil_']";

	public static final String BUILDERNAMETEXTLOCATOR = "//div[contains(@class, 'builder-dbd-name')]//h1";
	public static final String BUILDERDESCRIPTIONTEXTLOCATOR = "//div[contains(@class, 'builder-dbd-description')]//span[contains(@class, 'dbd-display-area-text')]";
	public static final String NAMEEDITID = "builder-dbd-name-editor-btn";
	public static final String DESCEDITID = "builder-dbd-description-editor-btn";
	public static final String NAMEINPUTID = "builder-dbd-name-input";
	public static final String DESCINPUTID = "builder-dbd-description-input";
	public static final String NAMEEDITOKID = "builder-dbd-name-ok";
	public static final String DESCEDITOKID = "builder-dbd-description-ok";
	// builder time picker
	public static final String BUILDERDATETIMEPICKERLOCATOR = "//div[@data-show-time-picker]";

	//dashboard builder tile edit area
	public static final String BUILDERTILESEDITAREA = "//div[contains(@class, 'tiles-wrapper')]";

	public static final String BUILDERTILETITLELOCATOR = "//h2[contains(@class, 'dbd-tile-title') and @data-tile-title='%s']";
	public static final String BUILDERTILECONFIGLOCATOR = "following-sibling::*//button[contains(@class, 'dbd-tile-action')]";
	public static final String BUILDERTILEDATAEXPLORELOCATOR = "following-sibling::*//button[contains(@class, 'dbd-data-explore')]";
	public static final String BUILDERTILESHOWLOCATOR = "//ul[not(contains(@style,'display:none'))]/li[@data-option='showhide-title']/a[@data-show-hide-title='show']";
	public static final String BUILDERTILEHIDELOCATOR = "//ul[not(contains(@style,'display:none'))]/li[@data-option='showhide-title']/a[@data-show-hide-title='hide']";
	//dashboard builder options
	public static final String BUILDEROPTIONSMENULOCATOR = ".df-toolbar .dashboardOptsBtn[title=\"Options\"]";

	public static final String BUILDEROPTIONSAUTOREFRESHLOCATOR = "//li[@data-singledb-option='Auto-refresh']/a";
	public static final String BUILDEROPTIONSAUTOREFRESHOFFLOCATOR = "//li[@data-singledb-option='Off']/a";
	public static final String BUILDEROPTIONSAUTOREFRESHON5MINLOCATOR = "//li[@data-singledb-option=\"On (Every 5 Minutes)\"]/a";
	public static final String BUILDEROPTIONSDELETELOCATOR = "button.dbd-delete-single-dashboard";
	//	public static final String BuilderOptionsDeleteMenuLocator = "//a[@id='emcpdf_dsbopts_delete']";
	public static final String BUILDEROPTIONSPRINTLOCATORCSS = "li[data-singledb-option=\"Print\"] a";
	public static final String BUILDEROPTIONSEDITLOCATORCSS = "li[data-singledb-option=\"Edit\"] a";
	public static final String BUILDEROPTIONSEDITNAMECSS = ".dbd-right-panel-editdashboard #dbsHNameIn";
	public static final String BUILDEROPTIONSEDITDESCRIPTIONCSS = ".dbd-right-panel-editdashboard #editDbdDscp";
	public static final String BUILDEROPTIONSEDITSHOWDESCRIPTIONCSS = ".dbd-right-panel-editdashboard #showdbDescription";
	public static final String BUILDEROPTIONSEDITSAVECSS = ".createDsb-2";
	public static final String ENTITYFILTERLOCATOR = "ckbxEntityFilter";
	public static final String BUILDEROPTIONSSHARELOCATORCSS = "li[data-singledb-option=\"Share\"] a";
	public static final String BUILDEROPTIONSUNSHARELOCATORCSS = "li[data-singledb-option=\"Stop Sharing\"] a";
	public static final String BUILDEROPTIONSFAVORITELOCATORCSS = "li[data-singledb-option=\"Add Favorite\"] a";
	public static final String BUILDEROPTIONSREMOVEFAVORITELOCATORCSS = "li[data-singledb-option=\"Remove Favorite\"] a";
	public static final String BUILDEROPTIONSSETHOMELOCATORCSS = "li[data-singledb-option=\"Set as Home\"] a";
	public static final String BUILDEROPTIONSREMOVEHOMELOCATORCSS = "li[data-singledb-option=\"Remove as Home\"] a";
	public static final String BUILDEROPTIONSSETHOMESAVECSS = "#btnComfirmSetAsHome";
	public static final String BUILDEROPTIONSDUPLICATELOCATORCSS = "li[data-singledb-option=\"Duplicate\"] a";
	public static final String BUILDEROPTIONSDUPLICATETOSETCSS = "li[data-singledb-option=\"Add to set\"] a";
	public static final String BUILDEROPTIONSDUPLICATENOTTOSETCSS = "li[data-singledb-option=\"Do not add to set\"] a";
	public static final String BUILDEROPTIONSDUPLICATENAMECSS = "dupDsbNameIn";
	public static final String BUILDEROPTIONSDUPLICATEDESCRIPTIONCSS = "dupDsbDescIn";
	public static final String BUILDEROPTIONSDUPLICATESAVECSS = ".createDsb-1";
	public static final String DASHBOARDSETCONTAINERCSS = "div.dashboard-content";
	public static final String DASHBOARDNAMECSS = ".builder-dbd-name-display-hover-area.dbd-display-hover-area>h1";
	// dashboard set name
	public static final String DASHBOARDSETNAMETEXTLOCATOR = "//div[@id='dbd-set-name']/span";
	//dashboard set options
	public static final String DASHBOARDSETNAVSCONTAINERCSS = "#dbd-set-tabs";
	public static final String DASHBOARDSETNAVSCSS = ".dbd-tabs-nav .oj-tabs-tab";
	public static final String DASHBOARDSETNAVREMOVEBTNCSS = ".oj-tabs-close-icon";
	public static final String DASHBOARDSETNAVADDBTNCSS = "#add-nav";
	public static final String DASHBOARDSETOPTIONBTN = "//button[contains(@id, 'tabs-caret')]";
	public static final String DASHBOARDSETOPTIONSAUTOREFRESHLOCATOR = "id=dbs-refresh";
	public static final String DASHBOARDSETOPTIONSAUTOREFRESHOFFLOCATOR = "id=refresh-off";
	public static final String DASHBOARDSETAUTOREFRESHOFFSELECTEDLOCATOR = "//li[@id='refresh-off']/a/span[contains(@class, 'dbd-icon-check')]";
	public static final String DASHBOARDSETOPTIONSAUTOREFRESHON5MINLOCATOR = "id=refresh-time";
	public static final String DASHBOARDSETAUTOREFRESHON5MINSELECTEDLOCATOR = "//li[@id='refresh-time']/a/span[contains(@class, 'dbd-icon-check')]";
	public static final String DASHBOARDSETOPTIONSDELETELOCATOR = "//button[contains(@class,'dbd-delete-dashboardset')]";
	public static final String DASHBOARDSETDELETEDIALOGLOCATOR = "id=deleteDashboardset";
	public static final String DASHBOARDSETDELETEDIALOGDELETEBTNLOCATOR = "//button[@data-delete-set-dialog='Delete']";
	public static final String DASHBOARDSETPRINTLOADINGID = "printLoading";
	public static final String DASHBOARDSETOPTIONSMENUID = "tabs-caret";
	public static final String DASHBOARDSETOPTIONSEDITCSS = "#dbs-edit a";
	public static final String DASHBOARDSETOPTIONSEDITSAVEID = "saveDsbInfo";
	public static final String DASHBOARDSETOPTIONSEDITDIALOGID = "ojDialogWrapper-changeDashboardsetInfo";
	public static final String DASHBOARDSETOPTIONSNAMECOLLAPSIBLECSS = "#nameDescription a.oj-collapsible-open-icon";
	public static final String DASHBOARDSETOPTIONSSHAREDIAOPENCSS = "#shareSettings .oj-collapsible-wrapper";
	public static final String DASHBOARDSETOPTIONSSHAREONJUDGECSS = "#shareSettings #share-on.oj-selected";
	public static final String DASHBOARDSETOPTIONSEDITNAMECSS = "input#dbsSetHNameIn";
	public static final String DASHBOARDSETOPTIONSEDITDESCRIPTIONCSS = "textarea#editDbdSetDscp";
	public static final String DASHBOARDSETOPTIONSSHARECONTENTCSS = ".dbd-right-panel-editdashboard-set-share .oj-collapsible-wrapper";
	public static final String DASHBOARDSETOPTIONSSHARECOLLAPSIBLECSS = ".dbd-right-panel-editdashboard-set-share >span";
	public static final String DASHBOARDSETOPTIONSSHARESTATUSCSS = "#dashboardSetSharingShared.oj-selected";
	public static final String DASHBOARDSETOPTIONSSHARECSS = "#dashboardSetSharingShared";
	public static final String DASHBOARDSETOPTIONSUNSHARECSS = "#dashboardSetSharingNotShared";
	public static final String DASHBOARDSETOPTIONSPRINTCSS = "#dbs-print a";
	public static final String DASHBOARDSETOPTIONSFAVORITECSS = "#dbs-favorite a";
	public static final String DASHBOARDSETOPTIONSREMOVEFAVORITECSS = "#dbs-favorite a[dashboardset-option=\"Remove Favorite\"]";
	public static final String DASHBOARDSETOPTIONSHOMECSS = "#dbs-home a";
	public static final String DASHBOARDSETOPTIONSADDHOMECSS = "#dbs-home a[dashboardset-option=\"Set as Home\"]";
	public static final String DASHBOARDSETFWKICONCSS = "div.dbd-right-panel-editdashboardset>div>a.oj-fwk-icon-caret-start.oj-fwk-icon.back-to-edit-settings";

	public static final String DASHBOARDFWKICON = "//span[text()='Edit Dashboard']/../a";

	//dashboard tool bar
	public static final String DASHBOARDSAVECSS = "button.dashboardSaveBtn";
	public static final String DASHBOARDADDTEXTWIDGETCSS = "button.dashboardTextBtn";

	//dashboard delete dialog
	public static final String BUILDERDELETEDIALOGLOCATOR = "id=ojDialogWrapper-delete-dashboard";

	public static final String BUILDERDELETEDIALOGDELETEBTNLOCATOR = "//button[@data-delete-dialog='Delete']";
	// auto refresh menu items
	public static final String BUILDERAUTOREFRESHOFFSELECTEDLOCATOR = "//li[@data-singledb-option='Off']/a/span[contains(@class, 'fa-check')]";

	public static final String BUILDERAUTOREFRESHON5MINSELECTEDLOCATOR = "//li[@data-singledb-option='On (Every 5 Minutes)']/a/span[contains(@class, 'fa-check')]";
	// for R1.6, following ids are not used any more
	//refresh //*[@id='ojChoiceId_autoRefreshSelect_selected
	public static final String AUTOREFRESHID = "//*[@id='ojChoiceId_autoRefreshSelect_selected']";

	public static final String AUTOREFRESHBY_15_SECS_ID = "/html/body/div[*]/div/div/ul/li[2]/div";//"/html/body/div[1]/div/div/ul/li[2]/div";//oj-listbox-result-label-2";
	public static final String AUTOREFRESHBY_30_SECS_ID = "/html/body/div[*]/div/div/ul/li[3]/div";//"/html/body/div[1]/div/div/ul/li[3]/div";//oj-listbox-result-label-3";
	public static final String AUTOREFRESHBY_1_MIN_ID = "/html/body/div[*]/div/div/ul/li[4]/div";//"/html/body/div[1]/div/div/ul/li[4]/div";//oj-listbox-result-label-4";
	public static final String AUTOREFRESHBY_15_MINS_ID = "/html/body/div[*]/div/div/ul/li[5]/div";//"/html/body/div[1]/div/div/ul/li[5]/div";//oj-listbox-result-label-5";
	//tile operation
	public static final String TILETITLE = "/html/body/div[*]/div[2]/div[1]/div[*]/div/div[2]/div/div/div[*]/h2";//"/html/body/div[*]/div[2]/div[2]/div[*]/div/div[2]/div[*]/div/div[*]/h2";

	public static final String CONFIGTILEID = "/html/body/div[*]/div[2]/div[1]/div[*]/div/div[2]/div/div/div[1]/div/div[2]/button";// "/html/body/div[*]/div[2]/div[2]/div[*]/div/div[2]/div[*]/div/div[1]/div/div[2]/button";
	public static final String OPENTILEID = "/html/body/div[*]/div[2]/div[1]/div[*]/div/div[2]/div/div/div[1]/div/div[1]/button";
	public static final String HIDETILEID = "/html/body/div[1]/div/ul/li[1]/a/span[2]";
	public static final String WIDERTILEID = "/html/body/div[1]/div/ul/li[3]/a/span[2]";
	public static final String NARROWERTILEID = "/html/body/div[1]/div/ul/li[4]/a/span[2]";
	public static final String TALLERTILEID = "/html/body/div[1]/div/ul/li[5]/a/span[2]";
	public static final String SHORTERTILEID = "/html/body/div[1]/div/ul/li[6]/a/span[2]";
	public static final String MAXIMIZETILEID = "/html/body/div[1]/div/ul/li[7]/a/span[2]";
	public static final String RESTORETILEID = "/html/body/div[1]/div/ul/li[8]/a/span[2]";
	public static final String REMOVETILEID = "/html/body/div[1]/div/ul/li[10]/a/span[2]";
	public static final String MAXIMIZEICON = "//button[@title='Maximize']";
	//tile css locators -> css=<locator>
	public static final String DASHBOARDTITLEBARCSS = ".oj-toolbars.oj-toolbar-bottom-border.oj-btton-half-chrome.head-bar-container.fit-size-vertical-sibling";
	
	public static final String WIDGETTITLECSS = ".dbd-widget";
	public static final String TEXTWIDGETCONTENTCSS = "div[id^='textContentWrapper_']";
	public static final String TEXTWIDGETEDITORCSS = "div[id^='textEditorWrapper_'] .cke_editable";
	public static final String TEXTCONTENTCSS = "div[data-bind=\"html: content()\"]";

	public static final String TEXTCONTENT1 = "//div[contains(@id, 'textContentWrapper_')]/div/a[1]";
	public static final String TEXTCONTENT2 = "//div[contains(@id, 'textContentWrapper_')]/div/a[2]";
	public static final String LINKICONCSS = ".cke_button__link";         // "//a[@title='Link']";
	public static final String LINKDIALOGCSS = "div.cke_dialog_body";
	public static final String PROTOCOLOPTION = "//div[contains(@class, 'cke_dialog_ui_select')]/label[text()='Protocol']/following-sibling::*//div[contains(@class, 'cke_dialog_ui_input_select')]";
	public static final String PROTOCOLOPTION_HTTP = "http://";
	public static final String PROTOCOLOPTION_HTTPS = "https://";
	public static final String PROTOCOLOPTION_FTP = "ftp://";
	public static final String PROTOCOLOPTION_NEWS = "news://";
	public static final String PROTOCOLOPTION_OTHER = "";
	public static final String HTTPCSS = ".cke_dialog_ui_input_select option[value='http://']";
	public static final String HTTPSCSS = ".cke_dialog_ui_input_select option[value='https://']";
	public static final String FTPCSS = ".cke_dialog_ui_input_select option[value='ftp://']";
	public static final String NEWSCSS = ".cke_dialog_ui_input_select option[value='news://']";
	public static final String OTHERXPATH = "//div[contains(@class, 'cke_dialog_ui_select')]/label[text()='Protocol']/following-sibling::*//div[contains(@class, 'cke_dialog_ui_input_select')]/select/option[@value='']";
	public static final String URLINPUT = "//label[text()='URL']/following-sibling::*//div/input[@type='text']";
	public static final String OKBTNCSS = "a.cke_dialog_ui_button_ok .cke_dialog_ui_button";
	
	public static final String IMAGEICONCSS = ".cke_button_icon.cke_button__image_icon";
	public static final String IMAGEDIALOGCSS = "div.cke_dialog_body";
	public static final String IMAGEURLINPUT = "//div[text()='Image Properties']/following-sibling::*//label[text()='URL']/following-sibling::*//div/input[@type='text']";
	public static final String ALTERNATIVEINPUT = "//label[text()='Alternative Text']/following-sibling::*//div/input[@type='text']";

	public static final String IMAGESCSS = "[id^='textContentWrapper_']>div>img";
	
	public static final String TILETITLECSS = "h2.dbd-tile-title";
	public static final String CONFIGTILECSS = "button[id^=actionButton]";
	public static final String WIDERTILECSS = "li[data-option=wider]";
	public static final String NARROWERTILECSS = "li[data-option=narrower]";
	public static final String SHORTERTILECSS = "li[data-option=shorter]";
	public static final String TALLERTILECSS = "li[data-option=taller]";
	public static final String REMOVETILECSS = "li[data-option=remove]";
	public static final String DELETETILE = "//ul[@title='Content Settings']/li/a/span[text()='Delete']";
	public static final String TILESLISTCSS = "div[id^=tile]";
	public static final String TEXTWIDGETCSS = "div[data-tile-name='Text Widget']";
	
	//right drawer css locators
	public static final String RIGHTDRAWERTOGGLEBTNCSS = ".right-panel-toggler button.toggle-right-panel-btn";

	public static final String RIGHTDRAWERCSS = ".right-panel-toggler";
	public static final String RIGHTDRAWERPANELCSS = ".dbd-left-panel";
	public static final String RIGHTDRAWERTOGGLEWRENCHBTNCSS = ".right-panel-toggler button.toggle-right-panel-btn.rightpanel-wrench";
	public static final String RIGHTDRAWERTOGGLEPENCILBTNCSS = ".right-panel-toggler button.toggle-right-panel-btn.rightpanel-pencil";
	public static final String RIGHTDRAWEREDITSINGLEDBBTNCSS = ".edit-setting-link.edit-dsb-link";
	public static final String RIGHTDRAWEREDITSINGLEDBSETBTNCSS = ".edit-setting-link.edit-dsbset-link";
	public static final String RIGHTDRAWEREDITSINGLEDBSHARECSS = ".dbd-right-panel-editdashboard-share>span";
	public static final String RIGHTDRAWEREDITDBFILTERCSS = ".dbd-right-panel-editdashboard-filters>span";
	public static final String RIGHTDRAWERSEARCHINPUTCSS = ".dbd-left-panel input.widget-search-input";
	public static final String RIGHTDRAWEREDITGENERNALCSS = ".dbd-right-panel-editdashboard-general>span";

	public static final String RIGHTDRAWERSEARCHBUTTONCSS = ".dbd-left-panel button.dbd-left-panel-header-search-btn";
	public static final String RIGHTDRAWERWIDGETTOAREACSS = ".tiles-wrapper";
	public static final String RIGHTDRAWERWIDGETCSS = ".dbd-left-panel .dbd-left-panel-widget";
	//right drawer setting
	public static final String RIGHTDRAWEREDITDBDEFAULTTIMERANGESELECT = "div[id^='oj-select-choice-defaultTimeRange_']";

	public static final String RIGHTDRAWEREDITDBDEFAULTTIMERANGERESULTLABEL = "span[id^='ojChoiceId_defaultTimeRange_']";
	public static final String RIGHTDRAWEREDITDBDEFAULTTIMERANGELAST15MINS = "/html/body/div/div/div/ul/li[1]/div";
	public static final String RIGHTDRAWEREDITDBDEFAULTTIMERANGELAST30MINS = "/html/body/div/div/div/ul/li[2]/div";
	public static final String RIGHTDRAWEREDITDBDEFAULTTIMERANGELAST60MINS = "/html/body/div/div/div/ul/li[3]/div";
	public static final String RIGHTDRAWEREDITDBDEFAULTTIMERANGELAST4HOURS = "/html/body/div/div/div/ul/li[4]/div";
	public static final String RIGHTDRAWEREDITDBDEFAULTTIMERANGELAST6HOURS = "/html/body/div/div/div/ul/li[5]/div";
	public static final String RIGHTDRAWEREDITDBDEFAULTTIMERANGELAST1DAY = "/html/body/div/div/div/ul/li[6]/div";
	public static final String RIGHTDRAWEREDITDBDEFAULTTIMERANGELAST7DAYS = "/html/body/div/div/div/ul/li[7]/div";
	public static final String RIGHTDRAWEREDITDBDEFAULTTIMERANGELAST14DAYS = "/html/body/div/div/div/ul/li[8]/div";
	public static final String RIGHTDRAWEREDITDBDEFAULTTIMERANGELAST30DAYS = "/html/body/div/div/div/ul/li[9]/div";
	public static final String RIGHTDRAWEREDITDBDEFAULTTIMERANGELAST90DAYS = "/html/body/div/div/div/ul/li[10]/div";
	public static final String RIGHTDRAWEREDITDBDEFAULTTIMERANGELAST1YEAR = "/html/body/div/div/div/ul/li[11]/div";
	public static final String RIGHTDRAWEREDITDBDEFAULTTIMERANGELATEST = "/html/body/div/div/div/ul/li[12]/div";
	public static final String RIGHTDRAWEREDITDBDEFAULTTIMERANGECUSTOM = "/html/body/div/div/div/ul/li[13]/div";
	public static final String RIGHTDRAWEREDITDBDEFAULTTIMERANGECUSOMRESULT = "/html/body/div/div/div/ul/li[14]/div";
	public static final String RIGHTDRAWEREDITSINGLEDBTOSHARESELECTEDCSS = "#dashboardSharingShared.oj-selected";

	public static final String RIGHTDRAWEREDITSINGLEDBTOSHARECSS = "#dashboardSharingShared";
	public static final String RIGHTDRAWEREDITSINGLEDBNOTSHARECSS = "#dashboardSharingNotShared";
	//time picker
	public static final String TIMEPICKERID = "/html/body/div[*]/div[2]/div[1]/div[1]/div/div[2]/div/span/span";//"/html/body/div[*]/div[2]/div[2]/div[2]/div[1]/div[2]/div/button";//"/html/body/div[3]/div[2]/div[2]/div[2]/div[1]/div[2]/div/button";///html/body/div[*]/div[2]/div/div/div[1]/div[1]/div/div/button";//"

	public static final String CUSTOMDATETIMEID = "/html/body/div[1]/div/div/div[1]/div/div[1]/div/a[11]";
	public static final String APPLYBTNID = "applyButton";
	public static final String CANCELBTNID = "cancelButton";
	public static final String DATEID1 = "/html/body/div[1]/div/div[2]/div/div/div/div[2]/div/div/div/div[1]/table/tbody/tr[4]/td[2]/a";//"/html/body/div[1]/div/div/div[1]/div/div[2]/div[2]/div/div/div[1]/table/tbody/tr[4]/td[2]/a";
	public static final String DATEID2 = "/html/body/div[1]/div/div/div[1]/div/div[2]/div[2]/div/div/div[2]/table/tbody/tr[4]/td[3]/a";
	//check external link
	public static final String EXTERNALLINK = "/html/body/div[*]/header/div/div[1]/div[1]/div[1]/div[3]/span";

	public static final String EXTERNALTARGETLINKID = "/html/body/div[4]/div[3]/div/div/div[7]/div/div[2]/div[2]/div[1]/div/div[1]/span";
	//grid view and list view id
	public static final String OOBDASHBOARDNAMELOCATORLISTVIEW = "//div[not(contains(@style, 'display:none'))]//*[contains(@class, 'dbs-summaries-container')]";
	public static final String GRIDVIEWID = "/html/body/div[*]/div/div[1]/div/div/div[2]/div[1]/span[2]/div[3]/span[1]/label";

	public static final String LISTVIEWID = "/html/body/div[*]/div/div[1]/div/div/div[2]/div[1]/span[2]/div[3]/span[2]/label";
	public static final String OOBDASHBOARD_LIST_LINK = "//a[contains(text(), '_name_') and contains(@class, 'dbs-dsbsystem')]";
	public static final String DASHBOARDLISTVIEWDASHBOARDID = "/html/body/div[2]/div/div[1]/div/div/div[2]/div[2]/table/tbody/tr/td[2]/a";

	public static final String DASHBOARDINFOID = "/html/body/div[*]/div/div[1]/div/div/div[2]/div[2]/table/tbody/tr/td[5]/button";
	public static final String DASHBOARDDELETEID = "/html/body/div[1]/div/div/div[1]/div/div/div/button";
	public static final String LV_DELETEBTNID_DIALOG = "/html/body/div[1]/div[2]/div/div[3]/button[1]";//"/html/body/div[1]/div[2]/div/div[5]/button[1]";
	public static final String WELCOMEID = "/html/body/div[2]/div/div[1]";//div[@class='welcome-slogan']";//"";

	//welcome page verify
	public static final String WELCOME_APMLINKCSS = "APM_wrapper"; //".landing-home-box-content-head[data-bind='text: APM']";

	public static final String WELCOME_LALINKCSS = "LA_wrapper"; //".landing-home-box-content-head[data-bind='text: LA']";
	public static final String WELCOME_ITALINKID = "ITA_wrapper"; //".landing-home-box-content-head[data-bind='text: ITA']";
	public static final String WELCOME_INFRAMONITORINGID = "infra_mon_wrapper";
	public static final String WELCOME_COMPLIANCEID = "compliance_wrapper";
	public static final String WELCOME_SECURITYANALYTICSID = "security_analytics_wrapper";
	public static final String WELCOME_ORCHESTRATIONID = "orchestration_wrapper";
	public static final String WELCOME_DASHBOARDSLINKID = "dashboards_wrapper"; //"/html/body/div[2]/div/div[2]/ul/li[4]/a/div/div[2]/div[1]";
	public static final String WELCOME_DATAEXP = "data_explore_wrapper"; //"/html/body/div[2]/div/div[2]/ul/li[5]/div/div/div[2]/div[1]";
	public static final String WELCOME_ITA_SELECTID = "ITA_options"; //"ojChoiceId_ITA_options_selected";
	public static final String WELCOME_DATAEXP_SELECTID = "dataExp_options";//"dataExp_Select"; //"ojChoiceId_autogen1_selected";
	public static final String WELCOME_LEARNMORE_GETSTARTED = "getStarted"; //"//a[contains(text(),'How to get started')]";
	public static final String WELCOME_LEARNMORE_VIDEOS = "videos"; //"//a[contains(text(),'Videos')]";
	public static final String WELCOME_LEARNMORE_SERVICEOFFERING = "service_offerings"; //"//a[contains(text(),'Service Offerings')]";
	public static final String WELCOME_ITA_PADATABASE = "ITA_DB_perf"; //"/html/body/div[1]/div/div/ul/li[2]/div";//"oj-listbox-result-label-8";//ITA Select Item : Performance Analytics - Database
	public static final String WELCOME_ITA_PAMIDDLEWARE = "ITA_mw_perf"; //"/html/body/div[1]/div/div/ul/li[3]/div";//"oj-listbox-result-label-9";//ITA Select Item : Performance Analytics - Middleware
	public static final String WELCOME_ITA_RADATABASE = "ITA_DB_resource"; //"/html/body/div[1]/div/div/ul/li[4]/div";//"oj-listbox-result-label-10";//ITA Select Item : Resource Analytics - Database
	public static final String WELCOME_ITA_RAMIDDLEWARE = "ITA_mw_resource"; //"/html/body/div[1]/div/div/ul/li[5]/div";//"oj-listbox-result-label-11";//ITA Select Item : Resource Analytics - Middleware
	public static final String WELCOME_ITA_RA_HOST = "ITA_svr_resource";
	public static final String WELCOME_ITA_PANALYTIC = "ITA_app_perf";
	public static final String WELCOME_ITA_AVANALYTIC = "ITA_avail_analytics";
	public static final String WELCOME_ITA_DEANALYZE = "ITA_Analyze"; //"/html/body/div[1]/div/div/ul/li[6]/div";//"oj-listbox-result-label-12";//ITA Select Item : Data Explorer - Analyze
	public static final String WELCOME_ITA_DE = "ITA_Search"; //"/html/body/div[1]/div/div/ul/li[7]/div";//"oj-listbox-result-label-13";//ITA Select Item : Data Explorer
	public static final String WELCOME_DATAEXP_LOG = "dataExp_Log"; //"/html/body/div[1]/div/div/ul/li[2]/div";//"oj-listbox-result-label-4";//Data Explorers Select Item : Log
	public static final String WELCOME_DATAEXP_ANALYZE = "dataExp_Analyze"; //"/html/body/div[1]/div/div/ul/li[3]/div";//"oj-listbox-result-label-5";//Data Explorers Select Item : Analyze
	public static final String WELCOME_DATAEXP_SEARCH = "dataExp_Search"; //"/html/body/div[1]/div/div/ul/li[4]/div";//"oj-listbox-result-label-6";//Data Explorers Select Item : Search
	//Sharing and stoping dashboard
	public static final String OPTION = "dashboardOptsBtn";

	public static final String DASHBOARDSHARE = "emcpdf_dsbopts_share";//"//*[@id='ui-id-5']/span[2]";
	public static final String STOPSHARE_BTN = "emcpdf_dsbopts_share";//"//*[@id='ui-id-5']/span[2]";
	// widget selector popup dialog
	public static final String WIDGET_SELECTOR_WIDGET_AREA = "//div[@id='widget-selector-widgets']";

	public static final String WIDGET_SELECTOR_SEARCH_INPUT_LOCATOR = "//div[@id='widget-selector-search-container']//input[@id='searchTxt']";
	public static final String WIDGET_SELECTOR_SEARCH_BTN = "//button[contains(@class, 'widget-selector-search-button')]";
	public static final String WIDGET_SELECTOR_WIDGET_ITEMS = "//li[contains(@class, 'widget-selector-li')]";
	public static final String WIDGET_SELECTOR_WIDGET_ITEMS_BY_TITLE = "//li[contains(@class, 'widget-selector-li')]/a[@data-widget-title=%s]";
	public static final String WIDGET_SELECTOR_OK_BTN_LOCATOR = "//button[@id='widget-selector-okbtn' and not(contains(@class, 'oj-disabled'))]";
	public static final String WIDGET_SELECTOR_CLOSE_BTN_LOCATOR = "//div[contains(@class, 'oj-dialog') and not(contains(@style, 'display: none'))]/div[contains(@class, 'widget-selector-main')]/preceding-sibling::div[contains(@class, 'oj-dialog-header')]/div[contains(@class, 'oj-dialog-header-close-wrapper')]";
	//Branding bar links id
	public static final String BRANDINGBARICON = "#emaas-appheader-bar";
	public static final String BRANDINGBARMYHOMELINKID = "obbNavHome";

	public static final String BRANDINGBARDASHBOARDHOMELINKID = "obbNavDsbHome";
	public static final String BRANDINGBARMYFAVORITESLINKID = "obbNavFavor";
	public static final String BRANDINGBARWELCOMELINKID = "obbNavWelcome";
	public static final String BRANDINGBARHOMELINKSID = "obbNavHomeLinks";
	public static final String BRANDINGBARCLOUDSERVICELINKSID = "obbNavCsLinks";
	public static final String BRANDINGBARVISUALANALYZERLINKSID = "obbNavVaLinks";
	public static final String BRANDINGBARADMINLINKSID = "obbNavAdminLinks";
	public static final String BRANDINGBARSERVICENAMECSS = ".emaas-appheader-appname-oneline";
	public static final String BRANDINGBARNAVLINKSID = "links_menu";
	//Brand Bar User Menu Option
	public static final String BRAND_BAR_USER_MENU = "//button[contains(@id,'menubutton')]";

	public static final String OPTION_HELP = "//a[contains(@id,'emcpdf_oba_help')]";
	public static final String OPTION_ABOUT = "//*[@id='emcpdf_oba_about']";
	public static final String OPTION_LOGOUT = "//*[@id='emcpdf_oba_logout']";
	public static final String ABOUTDIALOGCLOSE = "//button[contains(@id,'okButton')]";
	public static final String USERMENUPOPUPID = "emaasAppheaderGlobalNavMenuId";
	// Dashboard Home
	public static final String DASHBOARD_GRID_TABLE_CSS = ".dbs-summaries-container";

	public static final String DASHBOARD_LIST_TABLE = "table[aria-label='Dashboards Table']";
	public static final String DASHBOARD_HOME_DELETE_BUTTON = "dsbinfopop_delete";
	public static final String DASHBOARD_HOME_DELETE_CONFIRM = "dbs_cfmDialog_delete";
	public static final String DASHBOARD_HOME_DELETE_DIALOG = ".dbs_cfmDialog";
	public static final String DASHBOARD_HOME_CREATINSET_BUTTON = ".dashboard-picker-container .dbs-home-blue-button";
	//error page
	public static final String ERRORSIGNOUTBUTTONID = "dbd_sign_out_button";

	//Add button for widgetSelector
	public static final String WIDGETSELECTOR_ADDBUTTONID = "add-widget-button";

	//ude ID
	public static final String EntSelReadOnlyPill = "//div[@id='globalBar_pillWrapper']";
	public static final String EntSelTypeAheadField = "//div[@id='emcta-ctxtSel_bar']";
	public static final String EntSelTypeAheadFieldInput = "//div[@id='emaas-appheader-globalcxt']//input[contains(@id, 'pit_')]";
	public static final String EntSelSuggestionPopup = "//div[@id='emcta-ctxtSel_suggestPopup']";
	public static final String EntSelPills = "//div[@id='emaas-appheader-globalcxt']//pill";
	public static final String EntSelSearchResultsItem = "//div[@id='emcta-ctxtSel_suggestPopup']//ul//li[contains(@class,'oj-listbox-result-selectable')]";
	public static final String EntSelSearchResultsItemByText = "//div[@id=''emcta-ctxtSel_suggestPopup'']//ul//li[contains(@class,''oj-listbox-result-selectable'') and descendant::span[contains(text(),''{0}'')]]";
	//To get Composite results, substract all the 'Entities' category results from the entire list since there's no other way to determine if a result belongs to a certain category
	public static final String EntSelSuggestionByCompositeCategory = "((//div[@id=''emcta-ctxtSel_suggestPopup'']//ul//li[contains(@class,''oj-listbox-result-selectable'') and not(preceding-sibling::li[not(contains(@class,''oj-listbox-result-selectable''))]//span[text()=''Entities''])])/div[span[2]/descendant-or-self::span[normalize-space()=''{0}'']])";
	public static final String EntSelSuggestionByEntitiesCategory = "((//div[@id=''emcta-ctxtSel_suggestPopup'']//ul//li[contains(@class,''oj-listbox-result-selectable'') and preceding-sibling::li[not(contains(@class,''oj-listbox-result-selectable''))]//span[text()=''Entities'']])/div[span[2]/descendant-or-self::span[normalize-space()=''{0}'']])";
	public static final String EntSelPillToRemoveByIndex = "(//div[@id=''emaas-appheader-globalcxt'']//a[contains(@id,''_remove'')])[{0}]";
	public static final String EntSelPillLinkToReplaceByIndex = "//div[@id=\"emaas-appheader-globalcxt\"]//pill[{0}]//a[@class=\"pillLink\"]";
	public static final String EntSelEditedPillByText = "//div[@id=\"emaas-appheader-globalcxt\"]//pill//a[@class=\"pillLink\"]//span[contains(text(),\"{0}\")]";
	public static final String ENTSEL_CLEARALL_BUTTONID = "emcta-ctxtSel_clearAll";
	public static final String ENTSEL_COMPOSITE_PILL_BYTEXT = "//div[@id=\"emaas-appheader-globalcxt\"]//pill//span[text()=\"Composite: {0}\"]";
	public static final String ENTSEL_ENTITY_PILL_BYTEXT = "//div[@id=\"emaas-appheader-globalcxt\"]//pill//span[text()=\"{0}\"]";
	public static final String ENTSEL_PILL_BYCONTAINSTEXT = "//div[@id=\"emaas-appheader-globalcxt\"]//pill//span[contains(text(),\"{0}\")]";
	public static final String ENTSEL_PILL_CONTAINSTEXT_BYINDEX = "(//div[@id=\"emaas-appheader-globalcxt\"]//pill)[{0}]//span[contains(text(),\"{1}\")]";
	public static final String ENTSEL_SUGGESTIONLIST = "//div[@id='emcta-ctxtSel_suggestPopup']//li[contains(@class,\"oj-listbox-result-selectable\")]/div[@role='option']";

	private DashBoardPageId()
	{
	}

}
