/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define({
  "root": {
//      "DBS_HOME_TITLE":"Dashboard Home - Oracle Enterprise Manager Cloud Service",
//      "DBS_BUILDER_TITLE":"{0} - Oracle Enterprise Manager Cloud Service",
      "DBS_HOME_TITLE_HOME": "Home",
      "DBS_HOME_TITLE_DASHBOARDS": "Dashboards",
      "COMMON_BTN_OK": "OK",
      "COMMON_BTN_CANCEL": "Cancel",
      "COMMON_BTN_SAVE": "Save",
      "COMMON_BTN_EDIT": "Edit",
      "COMMON_BTN_DELETE": "Delete",
      "COMMON_BTN_ADD": "Add",
      "COMMON_BTN_SEARCH": "Search",
      "COMMON_TEXT_YES": "Yes",
      "COMMON_TEXT_NO": "No",
      "COMMON_TEXT_CLOSE": "CLOSE",
      "COMMON_TEXT_CREATE": "Create",
      "COMMON_TEXT_ERROR": "Error",
      "COMMON_REQUIRE_ERROR": "{0} is required",
      "COMMON_SERVER_ERROR": "Error on accessing server",
      "DBS_HOME_SEARCH_ARIA_LABEL": "Search for a Dashboard",
      "DBS_HOME_SEARCH_PLACE_HODE": "Search",
      "DBS_HOME_SEARCH_BTN_LABEL": "Search",
      "DBS_HOME_CLEAN_BTN_LABEL": "Clear",
      "DBS_HOME_CREATE_BTN_LABEL": "Create Dashboard",
      "DBS_HOME_CREATE_BTN_TT_TITLE": "How do I use this page?",
      "DBS_HOME_SELECT_SORT_BY_LABEL": "Sort by",
      "DBS_HOME_SELECT_SORT_ACCESS_DATE_ASC": "Last Accessed Ascending",
      "DBS_HOME_SELECT_SORT_ACCESS_DATE_DSC": "Last Accessed Descending",
      "DBS_HOME_SELECT_SORT_NAME_ASC": "Name Ascending",
      "DBS_HOME_SELECT_SORT_NAME_DSC": "Name Descending",
      "DBS_HOME_SELECT_SORT_CREATION_TIME_ASC": "Creation Date Ascending",
      "DBS_HOME_SELECT_SORT_CREATION_TIME_DSC": "Creation Date Descending",
      "DBS_HOME_SELECT_SORT_LAST_MODIFIED_ASC": "Last Modified Ascending",
      "DBS_HOME_SELECT_SORT_LAST_MODIFIED_DSC": "Last Modified Descending",
      "DBS_HOME_SELECT_SORT_LAST_OWNER_ASC": "Created By Ascending",
      "DBS_HOME_SELECT_SORT_LAST_OWNER_DSC": "Created By Descending",
      "DBS_HOME_SELECT_SORT_DEFAULT": "Default",
      "DBS_HOME_CREATE_BTN_TT_CONTENT": " Click to create a dashboard and add widgets of your choice.",
      "DBS_HOME_EXPLORE_BTN_LABEL": "Explore Data",
      "DBS_HOME_EXPLORE_BTN_TT": "Click to select a Visual Analyzer which allows you to explore data.",
      "DBS_HOME_CREATE_DLG_TITLE": "Create Dashboard",
      "DBS_HOME_CREATE_DLG_NAME": "Name",
      "DBS_HOME_CREATE_DLG_DES": "Description",
      "DBS_HOME_CREATE_DLG_TIME_RANGE": "Show time selector & refresh control",
      "DBS_HOME_CREATE_DLG_INVALID_NAME_SUM": "Name is invalid",
      "DBS_HOME_CREATE_DLG_INVALID_NAME": "You must enter a non empty name and its length should be less than 64 characters.",
      "DBS_HOME_CREATE_DLG_INVALID_DES_SUM": "Description is invalid",
      "DBS_HOME_CREATE_DLG_INVALID_DES": "You must enter a description with less than 256 characters.",
      "DBS_HOME_FILTER_TYPE_LABEL": "Type",
      "DBS_HOME_FILTER_TYPE_APP": "App",
      "DBS_HOME_FILTER_TYPE_DSB": "Dashboard",
      "DBS_HOME_FILTER_SERVICE_LABEL": "Cloud Services",
      "DBS_HOME_FILTER_SERVICE_LA": "Log Analytics",
      "DBS_HOME_FILTER_SERVICE_APM": "Application Performance Monitoring ",
      "DBS_HOME_FILTER_SERVICE_APM_ABBR": "APM",
      "DBS_HOME_FILTER_SERVICE_ITA": "IT Analytics",
      "DBS_HOME_FILTER_CREATOR_LABEL": "Creator",
      "DBS_HOME_VIEW_LABEL": "Choose one view",
      "DBS_HOME_VIEW_LIST_LABEL": "List View",
      "DBS_HOME_VIEW_GRID_LABEL": "Grid View",
      "DBS_HOME_VIEW_LIST_NAME": "Name",
      "DBS_HOME_VIEW_LIST_CREATOR": "Created By",
      "DBS_HOME_VIEW_LIST_CREATEDATE": "Creation Date",
      "DBS_HOME_VIEW_LIST_MODIFIEDDATE": "Last Modified",
      "DBS_HOME_FILTER_CREATOR_ORACLE": "Oracle",
      "DBS_HOME_FILTER_CREATOR_OTHER": "Me",
      "DBS_HOME_COME_SOON_DLG_INFO": "Information",
      "DBS_HOME_COME_SOON_DLG_BODY": "Coming soon...",
      "DBS_HOME_CFM_DLG_DELETE_DSB": "Delete Dashboard",
      "DBS_HOME_CFM_DLG_DELETE_DSB_MSG": "Do you want to delete the selected dashboard '{0}'?",
      "DBS_HOME_CFM_DLG_DELETE_DSB_ERROR": "Error for deleting dashboard.",
      "DBS_HOME_WC_DLG_TILE": "Welcome, {0}!",
      "DBS_HOME_WC_DLG_BROWSE": "Browse",
      "DBS_HOME_WC_DLG_BROWSE_CONTENT": "through our rich gallery of analytics solutions.",
      "DBS_HOME_WC_DLG_BUILD": "Build",
      "DBS_HOME_WC_DLG_BUILD_CONTENT": "custom and personal dashboards using our out-of-the box widgets.",
      "DBS_HOME_WC_DLG_EXPLORE": "Explore",
      "DBS_HOME_WC_DLG_EXPLORE_CONTENT": "data, set visualization controls, and save widgets.",
      "DBS_HOME_WC_DLG_GOT_BTN": "Got it!",
      "DBS_HOME_DSB_PANEL_WIDGETS": "Widgets",
      "DBS_HOME_DSB_PAGE_SCREEN_SHOT": "Snapshot",
      "DBS_HOME_DSB_PAGE_DESCRIPTION": "Description",
      "DBS_HOME_DSB_PAGE_WIDGETS": "Widgets",    
      "DBS_HOME_DSB_PAGE_INFO_DESC": "Description: ",
      "DBS_HOME_DSB_PAGE_INFO_CREATE": "Created By: ",
      "DBS_HOME_DSB_PAGE_INFO_CDATE": "Creation Date: ",
      "DBS_HOME_DSB_PAGE_INFO_TYPE": "Type: ",
      "DBS_HOME_DSB_PAGE_INFO_TYPE_SYS": "System Dashboard",
      "DBS_HOME_DSB_PAGE_INFO_TYPE_USER": "User Dashboard",
      "DBS_HOME_DSB_PAGE_INFO_ISSYS": "System Dashboard: ",
      "DBS_HOME_DSB_PAGE_INFO_LABEL": "Dashboard Information",
      "DBS_HOME_DSB_PAGE_INFO_DELETE_LABEL": "Delete",
      "DBS_BUILDER_LOADING": "Loading...",
      "DBS_BUILDER_NAME_EDIT": "Double click to edit name",
      "DBS_BUILDER_SAME_NAME_EXISTS_ERROR": "Dashboard with the same name exists already",
      "DBS_BUILDER_REQUIRE_NAME": "Name is required",
      "DBS_BUILDER_BTN_ADD_WIDGET": "Add Widgets",
      "DBS_BUILDER_BTN_ADD": "Add",
      "DBS_BUILDER_BTN_ADD_TEXT": "Add Text",
      "DBS_BUILDER_TEXT_WIDGET_EDIT": "Double click here to edit text",
      "TEXT_LENGTH_ERROR_MSG": "Error: The Content is too long!",
//      "DBS_BUILDER_BTN_ADD_TEXT_TITLE": "Add Text Widget",
//      "DBS_BUILDER_BTN_ADD_HINT_TITLE": "How do I use this page?",
//      "DBS_BUILDER_BTN_ADD_HINT_DETAIL": "Click the + button and add new widgets to your dashboard",
      "DBS_BUILDER_BTN_ADD_HINT_TITLE": "Your dashboard has no data to display",
      "DBS_BUILDER_BTN_ADD_HINT_TEXT_LINE1": "Add one or more widgets to see data in the dashboard",
      "DBS_BUILDER_BTN_ADD_HINT_TEXT_LINE2_1": "Click ",
      "DBS_BUILDER_BTN_ADD_HINT_ADD_LINK": "Add",
      "DBS_BUILDER_BTN_ADD_HINT_TEXT_LINE2_2": " to get started",
      "DBS_BUILDER_BTN_SAVE_DASHBOARD": "Save Dashboard",
      "DBS_BUILDER_SETTINGS": "Settings",
      "DBS_BUILDER_FILTERS": "FILTERS",
      "DBS_BUILDER_INCLUDE_TIME_CONTROL": "Include time control (range and refresh)",
      "DBS_BUILDER_VIEW": "VIEW",
      "DBS_BUILDER_VIEW_DESC": "Configure builder view like widget height, etc.",
      "DBS_BUILDER_OTHERS": "OTHERS",
      "DBS_BUILDER_OTHERS_DESC": "Configure builder miscellaneous",
      "DBS_BUILDER_ADD_WIDGET_DLG_TITLE": "Add Widgets",
      "DBS_BUILDER_ADD_WIDGET_DLG_WIDGET_GROUP": "Widget Group",
      "DBS_BUILDER_ADD_WIDGET_DLG_CREATE_WIDGET": "Create Widget",
      "DBS_BUILDER_ADD_WIDGET_DLG_SEARCH_PLACEHOLDER": " Enter criteria to search for a widget.",
      "DBS_BUILDER_ADD_WIDGET_DLG_DBL_CLICK": "Double-click to add a widget, or click a widget to view details and add.",
      "DBS_BUILDER_WIDGET_DETAIL_TITLE": "Widget Details",
      "DBS_BUILDER_WIDGET_DETAIL_NAME": "Name",
      "DBS_BUILDER_WIDGET_DETAIL_DESC": "Description",
      "DBS_BUILDER_WIDGET_DETAIL_QUERY_STRING": "Query String",
      "DBS_BUILDER_WIDGET_DETAIL_OWNER": "Owner",
      "DBS_BUILDER_WIDGET_DETAIL_CREATION_DATE": "Creation Date",
      "DBS_BUILDER_CREATE_WIDGET_TITLE": "Create New Widget",
      "DBS_BUILDER_CREATE_WIDGET_HEADER": "Create Widget (for integration dev only)",
      "DBS_BUILDER_CREATE_WIDGET_PROVIDER_NAME": "Provider Name",
      "DBS_BUILDER_CREATE_WIDGET_VERSION": "Version",
      "DBS_BUILDER_CREATE_WIDGET_ASSET_ROOT": "Asset Root",
      "DBS_BUILDER_CREATE_WIDGET_NAME": "Name",
      "DBS_BUILDER_CREATE_WIDGET_DESC": "Description",
      "DBS_BUILDER_CREATE_WIDGET_QUR_STR": "Query String",
      "DBS_BUILDER_CREATE_WIDGET_CAT": "Category",
      "DBS_BUILDER_CREATE_WIDGET_INFO_ABS_1ST": "Enter the ",
      "DBS_BUILDER_CREATE_WIDGET_INFO_ABS_2ND": "FULL ABSOLUTE PATH",
      "DBS_BUILDER_CREATE_WIDGET_INFO_ABS_3RD": " for below URLs in this category. E.g. http://slc04pxi.us.oracle.com:7001/widgets/sample/sampleWidget.html",
      "DBS_BUILDER_CREATE_WIDGET_INFO_REL_1ST": "Enter the ",
      "DBS_BUILDER_CREATE_WIDGET_INFO_REL_2ND": "RELATIVE PATH",
      "DBS_BUILDER_CREATE_WIDGET_INFO_REL_3RD": " for below URLs in this category. E.g. /sample/sampleWidget.html",
      "DBS_BUILDER_CREATE_WIDGET_KOC_NAME": "KOC Name",
      "DBS_BUILDER_CREATE_WIDGET_VM_URL": "View Model URL",
      "DBS_BUILDER_CREATE_WIDGET_TMPL_URL": "Template URL",
      "DBS_BUILDER_CREATE_WIDGET_ICN_URL": "ICON URL",
      "DBS_BUILDER_CREATE_WIDGET_HISGRM_URL": "Histogram URL",
      "DBS_BUILDER_CHANGE_URL_TITLE": "Change URL",
      "DBS_BUILDER_TILE_ACTIONS": "Configure widget",
      "DBS_BUILDER_TILE_REFRESH": "Refresh",
      "DBS_BUILDER_TILE_DELETE": "Delete",
      "DBS_BUILDER_TILE_WIDER": "Wider",
      "DBS_BUILDER_TILE_NARROWER": "Narrower",
      "DBS_BUILDER_TILE_TALLER": "Taller",
      "DBS_BUILDER_TILE_SHORTER": "Shorter",
      "DBS_BUILDER_TILE_MAXIMIZE": "Maximize",
      "DBS_BUILDER_TILE_RESTORE": "Restore",
      "DBS_BUILDER_TILE_CFG": "Edit",
      "DBS_BUILDER_AUTOREFRESH_REFRESH":"Refresh:",
      "DBS_BUILDER_AUTOREFRESH_NONE":"None",
      "DBS_BUILDER_AUTOREFRESH_15SEC":"15 Seconds",
      "DBS_BUILDER_AUTOREFRESH_30SEC":"30 Seconds",
      "DBS_BUILDER_AUTOREFRESH_1MIN":"1 Minute",
      "DBS_BUILDER_AUTOREFRESH_15MIN":"15 Minutes",
      "DBS_BUILDER_MSG_CHANGES_SAVED": "Changes on the dashboard have been saved successfully.",
      "DBS_BUILDER_MSG_ERROR_IN_SAVING": "Error occurred when saving the dashboard. Check console log or server log for details",
      
      
      "TEXT_WIDGET_IFRAME_HINT":"Please change the URL and click \"Change\" to apply: ",
      "TEXT_WIDGET_IFRAME_CONFIGURATION":"Configuration",
      "TEXT_WIDGET_SUBSCRIBER_HINT":"You will see received message and what I will respond",
      "LABEL_WIDGET_IFRAME_CHANGE":"Change",
      "TEXT_WIDGET_PUBLISHER_HINT":"Please write your message and click \"Publish\" to send",
      "LABEL_WIDGET_PUBLISHER_PUBLISH":"Publish",
      
      "DBS_ERROR_PAGE_TITLE":"Error",
      "DBS_ERROR_PAGE_NOT_FOUND_MSG":"Sorry, the page you have requested either doesn't exist or you do not have access to it.",
      "DBS_ERROR_DASHBOARD_ID_NOT_FOUND_MSG":"Sorry, you must specify a valid dashboard id to open a dashboard.",
      "DBS_ERROR_PAGE_NOT_FOUND_NO_SUBS_MSG":"Sorry, the page you have requested either doesn't exist or you do not have access to it. Reason: \"No service is subscribed\"",
      "DBS_ERROR_ORA_EMSAAS_USERNAME_AND_TENANTNAME_INVALID": "Error: failed to retrieve user or tenant.",
      "DBS_ERROR_URL": "Requested URL is: ",
      "DBS_ERROR_BTN_SIGN_OUT": "Sign Out",
	  
      "LANDING_HOME_WINDOW_TITLE": "Landing Home",
      "LANDING_HOME_WELCOME_SLOGAN": "Welcome to Oracle Management Cloud",
      "LANDING_HOME_APM": "Application Performance Monitoring",
      "LANDING_HOME_APM_DESC": "Rapidly identify, response, and resolve your software roadblocks",
      "LANDING_HOME_LA": "Log Analytics",
      "LANDING_HOME_LA_DESC": "Topology aware log exploration and analytics for modern applications and infrastructure",
      "LANDING_HOME_ITA": "IT Analytics",
      "LANDING_HOME_ITA_DESC": "Operational big data intelligence for modern IT",
      "LANDING_HOME_ITA_DB_PERFORMANCE": "Performance Analytics - Database",
      "LANDING_HOME_SELECT": "Select",
      "LANDING_HOME_ITA_DB_RESOURCE": "Resource Analytics - Database",
      "LANDING_HOME_ITA_MIDDLEWARE_PERFORMANCE": "Performance Analytics - Middleware",
      "LANDING_HOME_ITA_SEARCH": "Data Explorer - Search",
      "LANDING_HOME_ITA_ANALYZE":"Data Explorer - Analyze",
      "LANDING_HOME_ITA_AWR": "Data Explorer - AWR",
      "LANDING_HOME_DASHBOARDS": "Dashboards",
      "LANDING_HOME_DASHBOARDS_DESC": "Build custom dashboards using out-of-the-box widgets or your own visualization of data",
      "LANDING_HOME_DATA_EXPLORERS": "Data Explorers",
      "LANDING_HOME_DATA_EXPLORERS_DESC": "Search, analyze, and visualize data",
      "LANDING_HOME_DATA_EXPLORERS_SEARCH": "Search",
      "LANDING_HOME_DATA_EXPLORERS_ANALYZE": "Analyze",
      "LANDING_HOME_DATA_EXPLORERS_AWR": "AWR",
      "LANDING_HOME_DATA_EXPLORERS_LOG_ANALYTICS": "Log Analytics",
      
      "LANDING_HOME_LEARN_MORE": "Learn More",
      "LANDING_HOME_GET_STARTED_LINK": "How to get started",
      "LANDING_HOME_VIDEOS_LINK": "Videos",
      "LANDING_HOME_COMMUNITY_LINK": "Management Cloud Community",
      
      "DBS_BUILDER_EDIT_WIDGET_LINK_DIALOG_TITLE": "Edit widget link",
      "DBS_BUILDER_EDIT_WIDGET_LINK_DIALOG_NAME_LABEL": "Name",
      "DBS_BUILDER_EDIT_WIDGET_LINK_DIALOG_URL_LABEL": "Url"
      
}
});

