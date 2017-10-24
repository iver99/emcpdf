define('uifwk/@version@/js/widgets/widgetselector/widget-selector-dialog-impl',[
    'knockout',
    'jquery',
    'uifwk/@version@/js/util/df-util-impl', 
    'ojs/ojcore',
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg',
    'uifwk/@version@/js/util/typeahead-search-impl', 
    'uifwk/@version@/js/util/mobile-util-impl',
    'ojs/ojselectcombobox',
    'ojs/ojdialog',
    'ojs/ojinputtext',
    'ojs/ojbutton'
    ],
        function (ko, $, dfumodel, oj, nls, typeaheadsearch, mbu) {
            function WidgetSelectorDialogViewModel(params) {
                var self = this;
                new typeaheadsearch(); //Initialize typeahead search
                // Get input parameters and set UI strings
                var affirmativeTxt = $.isFunction(params.affirmativeButtonLabel) ? params.affirmativeButtonLabel() : params.affirmativeButtonLabel;
                var dialogTitle = $.isFunction(params.dialogTitle) ? params.dialogTitle() : params.dialogTitle;
                var widgetProviderName = $.isFunction(params.providerName) ? params.providerName() : params.providerName;
                var widgetProviderVersion = $.isFunction(params.providerVersion) ? params.providerVersion() : params.providerVersion;
                var includeDashboardIneligible = $.isFunction(params.includeDashboardIneligible) ? params.includeDashboardIneligible() :
                        (params.includeDashboardIneligible ? params.includeDashboardIneligible : false);
                self.userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                self.tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
                self.dialogId = $.isFunction(params.dialogId) ? params.dialogId() :
                        (params.dialogId ? params.dialogId : 'widgetSelectorDialog');
                self.widgetHandler = params.widgetHandler;
                self.autoCloseDialog = $.isFunction(params.autoCloseDialog) ? params.autoCloseDialog() : params.autoCloseDialog;
                self.widgetSelectorTitle = dialogTitle ? dialogTitle : nls.WIDGET_SELECTOR_DEFAULT_DIALOG_TITLE;
                self.widgetGroupLabel = nls.WIDGET_SELECTOR_WIDGET_GROUP_LABEL;
                self.searchBoxPlaceHolder = nls.WIDGET_SELECTOR_SEARCH_BOX_PLACEHOLDER;
                self.searchButtonLabel = nls.WIDGET_SELECTOR_SEARCH_BTN_LABEL;
                self.clearButtonLabel = nls.WIDGET_SELECTOR_CLEAR_BTN_LABEL;
                self.affirmativeButtonLabel = affirmativeTxt ? affirmativeTxt : nls.WIDGET_SELECTOR_DEFAULT_AFFIRMATIVE_BTN_LABEL;
                self.widgetScreenShotPageTitle = nls.WIDGET_SELECTOR_WIDGET_NAVI_SCREENSHOT_TITLE;
                self.widgetDescPageTitle = nls.WIDGET_SELECTOR_WIDGET_NAVI_DESC_TITLE;
                self.widgetsLoadingHints = nls.WIDGET_SELECTOR_WIDGETS_LOADING_HINT;
                self.widgetGroupFilterVisible = ko.observable(widgetProviderName && widgetProviderVersion ? false : true);
                self.searchText = ko.observable("");
                self.clearButtonVisible = ko.computed(function(){return self.searchText() && '' !== self.searchText() ? true : false;});
                var dfu = new dfumodel(self.userName, self.tenantName);
                //Append uifwk css file into document head
                dfu.loadUifwkCss();
                // Initialize widget group and widget data
                var labelAll = nls.WIDGET_SELECTOR_WIDGET_GROUP_ALL;
                var groupValueAll = 'all|all|All';
                var groupAll = {value: groupValueAll, label: labelAll};
                self.categoryValue=ko.observableArray([groupValueAll]);
                self.widgetGroup=ko.observable();
                self.widgetGroupValue=ko.observable({providerName:"all", providerVersion:"all", name:"all"});
                self.widgetGroups=ko.observableArray([{value: groupValueAll, label: labelAll}]);
                var widgetArray = [];
                var curGroupWidgets = [];
                var widgetGroupList = [];
                var curPageWidgets=[];
                var searchResultArray = [];
                var index=0;
                var pageSize = 8;
                var curPage = 1;
                var totalPage = 0;
                var naviFromSearchResults = false;
                var widgetIndex = 0;
                var queryWidgetsForAllSubscribedApps = false;
                var availableWidgetGroups = [];
                self.widgetList = ko.observableArray(widgetArray);
                self.curPageWidgetList = ko.observableArray(curPageWidgets);
                self.naviPreBtnEnabled=ko.observable(curPage === 1 ? false : true);
                self.naviNextBtnEnabled=ko.observable(totalPage > 1 && curPage!== totalPage ? true:false);
                self.currentWidget = ko.observable();
                self.confirmBtnDisabled = ko.observable(true);
                self.widgetOnLoading = ko.observable(true);
                // Initialize data and refresh
                self.beforeOpenDialog = function(event, ui) {
                    refreshWidgets();
                };
                // Widget group selector value change handler
                self.optionChangedHandler = function(data, event) {
                    if (event.option === "value") {
                        var preValue = event.previousValue;
                        var value = event.value;
                        if (preValue !== null && $.isArray(preValue)) {
                            preValue = preValue[0];
                        }
                        if (value !== null && $.isArray(value)) {
                            value = value[0];
                        }
                        if (preValue !== value) {
                            self.searchWidgets();
                        }
                    }
                };
                // Show widgets data of previous page
                self.naviPrevious = function(data, event) {
                    console.log(event);
                    if(event.type === "click" || (event.type === "keypress" && event.keyCode === 13)) {
                    if (curPage === 1) {
                        self.naviPreBtnEnabled(false);
                    }else if (curPage === 2 && event.type === "keypress") {
                        $("#nextButton").focus();
                            curPage--;
                        }
                        else {
                            curPage--;
                        }
                        if (naviFromSearchResults) {
                            fetchWidgetsForCurrentPage(searchResultArray);
                        }
                        else {
                            fetchWidgetsForCurrentPage(getAvailableWidgets());
                        }
                        self.curPageWidgetList(curPageWidgets);
                        refreshNaviButton();
                        refreshWidgetAndButtonStatus();
                    }
                };
                // Show widget data of next page
                self.naviNext = function(data, event) {
                    if (event.type === "click" || (event.type === "keypress" && event.keyCode === 13)) {
                        if (curPage === totalPage) {
                            self.naviNextBtnEnabled(false);
                        }else if (curPage === (totalPage-1) && event.type === "keypress") {
                            $("#preButton").focus();
                            curPage++;
                        }
                        else {
                            curPage++;
                        }
                        if (naviFromSearchResults) {
                            fetchWidgetsForCurrentPage(searchResultArray);
                        }
                        else {
                            fetchWidgetsForCurrentPage(getAvailableWidgets());
                        }
                        self.curPageWidgetList(curPageWidgets);
                        refreshNaviButton();
                        refreshWidgetAndButtonStatus();
                    }
                };
                // Search widgets by selected widget group and search text(name, description)
                self.searchWidgets = function() {
                    searchResultArray = [];
                    var allWidgets = getAvailableWidgets();
                    var searchtxt = $.trim(ko.toJS(self.searchText));
                    if (searchtxt === '') {
                        searchResultArray = allWidgets;
                    }
                    else {
                        for (var i=0; i<allWidgets.length; i++) {
                            if (allWidgets[i].WIDGET_NAME.toLowerCase().indexOf(searchtxt.toLowerCase()) > -1 ||
                                    (allWidgets[i].WIDGET_DESCRIPTION && allWidgets[i].WIDGET_DESCRIPTION.toLowerCase().indexOf(searchtxt.toLowerCase()) > -1)) {
                                searchResultArray.push(allWidgets[i]);
                            }
                        }
                    }
                    curPageWidgets=[];
                    curPage = 1;
                    totalPage = (searchResultArray.length%pageSize === 0 ? searchResultArray.length/pageSize : Math.floor(searchResultArray.length/pageSize) + 1);
                    fetchWidgetsForCurrentPage(searchResultArray);
                    self.curPageWidgetList(curPageWidgets);
                    refreshNaviButton();
                    naviFromSearchResults = true;
                    refreshWidgetAndButtonStatus();
                };
                self.clearSearchText = function() {
                    self.searchText('');
                    self.searchWidgets();
                };
                //Refresh widget selection status and confirm button status
                function refreshWidgetAndButtonStatus() {
                    var curWidget = self.currentWidget();
                    if (curWidget) {
                        widgetArray[curWidget.index].isSelected(false);
                        self.currentWidget(null);
                    }
                    self.confirmBtnDisabled(true);
                };
                // Get widget data to be shown in current page
                function fetchWidgetsForCurrentPage(allWidgets) {
                    curPageWidgets=[];
                    for (var i=(curPage-1)*pageSize;i < curPage*pageSize && i < allWidgets.length;i++) {
                        loadWidgetScreenshot(allWidgets[i]);
                        curPageWidgets.push(allWidgets[i]);
                    }
                };
                // Get available widgets to be searched from
                function getAvailableWidgets() {
                    var availWidgets = [];
                    var category = ko.toJS(self.categoryValue);
                    if (category === null || category === '' || category.length === 0) {
                        category = 'all|all|All';
                    }
                    else {
                        category = category[0];
                    }
                    var wg = category.split('|');
                    var providerName = wg[0];
                    var providerVersion = wg[1];
                    var groupName = wg[2];
                    if (providerName==='all' && providerVersion==='all' && groupName === 'All') {
                        availWidgets = widgetArray;
                    }
                    else {
                        for (i = 0; i < widgetArray.length; i++) {
                            var widget = widgetArray[i];
                            if (//widget.PROVIDER_NAME === providerName &&
                    //                widget.PROVIDER_VERSION === providerVersion &&
                                    widget.WIDGET_GROUP_NAME === groupName) {
                                availWidgets.push(widget);
                            }
                        }
                    }
                    return availWidgets;
                };
                // Refresh pagination button status
                function refreshNaviButton() {
                    self.naviPreBtnEnabled(curPage === 1 ? false : true);
                    self.naviNextBtnEnabled(totalPage > 1 && curPage!== totalPage ? true:false);
                };
                // Return search result of type ahead search
                self.searchFilterFunc = function (arr, value) {
                    self.searchText(value);
                    return searchResultArray;
                };
                // Handler for type ahead search
                self.searchResponse = function (event, data) {
                    self.searchWidgets();
                };
                // Handler for navigation to widget screen shot page
                self.widgetNaviScreenShotClicked = function(data, event) {
                    data.isScreenShotPageDisplayed(true);
                };
                // Handler for navigation to widget description page
                self.widgetNaviDescClicked = function(data, event) {
                    data.isScreenShotPageDisplayed(false);
                };
                // Widget box click handler
                self.widgetBoxClicked = function(data, event) {
                    var curWidget = self.currentWidget();
                    if (curWidget && (curWidget.PROVIDER_NAME !== data.PROVIDER_NAME ||
                            curWidget.WIDGET_UNIQUE_ID !== data.WIDGET_UNIQUE_ID)) {
                        widgetArray[curWidget.index].isSelected(false);
                        data.isSelected(true);
                        self.currentWidget(data);
                    }
                    else if (!curWidget) {
                        data.isSelected(true);
                        self.currentWidget(data);
                    }
                    if (self.confirmBtnDisabled() === true){
                        self.confirmBtnDisabled(false);
                    }
                };
                // Widget handler for selected widget
                self.widgetSelectionConfirmed = function() {
                    //Close dialog if autoCloseDialog is true or not set
                    if (self.autoCloseDialog !== false){
                        $('#'+self.dialogId).ojDialog('close');
                    }
                    if (self.widgetHandler && $.isFunction(self.widgetHandler)) {
                        var selectedWidget = self.currentWidget();
                        self.widgetHandler(selectedWidget);
                    }
                    else {
                        oj.Logger.warn('No widget handler is available to call back!');
                    }
                };
                function getWidgets() {
                    var widgetsBaseUrl = '/sso.static/savedsearch.widgets';
                    var widgetsUrl = widgetsBaseUrl;
                    if (dfu.isDevMode()){
                        widgetsBaseUrl = dfu.buildFullUrl(dfu.getDevData().ssfRestApiEndPoint,"/widgets");
                        widgetsUrl = widgetsBaseUrl;
                        if (includeDashboardIneligible) {
                            widgetsUrl = widgetsBaseUrl + "?includeDashboardIneligible=true";
                        }
                    }
                    if (queryWidgetsForAllSubscribedApps) {
                        return dfu.ajaxWithRetry({
                            url: widgetsUrl,
                            headers: dfu.getSavedSearchServiceRequestHeader(),
                            success: function(data, textStatus, jqXHR) {
                                loadWidgets(data);
                            },
                            error: function(xhr, textStatus, errorThrown){
                                oj.Logger.error('Error when fetching widgets by URL: '+widgetsUrl+'.');
                            },
                            async: true
                        });
                    }
                    else {
                        var ajaxCallDfd = $.Deferred();
                        var loadedCnt = 0;
                        for (var i = 0; i < availableWidgetGroups.length; i++) {
                            //Get widgets by widget group id
                            widgetsUrl = widgetsBaseUrl + "?widgetGroupId=" + availableWidgetGroups[i].WIDGET_GROUP_ID;
                            if (includeDashboardIneligible) {
                                widgetsUrl = widgetsUrl + "&includeDashboardIneligible=true";
                            }
                            dfu.ajaxWithRetry({
                                url: widgetsUrl,
                                headers: dfu.getSavedSearchServiceRequestHeader(),
                                success: function(data, textStatus, jqXHR) {
                                    loadWidgets(data);
                                    loadedCnt++;
                                    if (loadedCnt === availableWidgetGroups.length) {
                                        ajaxCallDfd.resolve(data, textStatus, jqXHR);
                                    }
                                },
                                error: function(jqXHR, textStatus, errorThrown){
                                    loadedCnt++;
                                    if (loadedCnt === availableWidgetGroups.length) {
                                        ajaxCallDfd.reject(jqXHR, textStatus, errorThrown);
                                    }
                                    oj.Logger.error('Error when fetching widgets by URL: '+widgetsUrl+'.');
                                },
                                async: true
                            });
                        }
                        return ajaxCallDfd;
                    }
                };
                function getWidgetGroups() {
                    var widgetgroupsUrl = '/sso.static/savedsearch.widgetgroups';
                    if (dfu.isDevMode()){
                        widgetgroupsUrl=dfu.buildFullUrl(dfu.getDevData().ssfRestApiEndPoint,"/widgetgroups");
                        if (includeDashboardIneligible) {
                            widgetgroupsUrl = widgetgroupsUrl + "?includeDashboardIneligible=true";
                        }
                    }
                    return dfu.ajaxWithRetry({
                            url: widgetgroupsUrl,
                            headers: dfu.getSavedSearchServiceRequestHeader(),
                            success: function(data, textStatus) {
                                widgetGroupList = loadWidgetGroups(data);
                                if (widgetProviderName && widgetProviderVersion && widgetGroupList.length <= 2) {
                                    self.widgetGroupFilterVisible(false);
                                }
                                else {
                                    self.widgetGroupFilterVisible(true);
                                }
                                if ($.isArray(data) && availableWidgetGroups.length === data.length) {
                                    queryWidgetsForAllSubscribedApps = true;
                                }
                            },
                            error: function(xhr, textStatus, errorThrown){
                                oj.Logger.error('Error when fetching widget groups by URL: '+widgetgroupsUrl+'.');
                            },
                            async: true
                        });
                };
                function refreshPageData() {
                    curPage = 1;
                    totalPage = (widgetArray.length%pageSize === 0 ? widgetArray.length/pageSize : Math.floor(widgetArray.length/pageSize) + 1);
                    naviFromSearchResults = false;
                    self.widgetGroups(widgetGroupList);
                    var selectedGroup = widgetGroupList.length > 0 ? widgetGroupList[0].value : "";
                    self.categoryValue([selectedGroup]);
                    self.widgetList(widgetArray);
                    self.curPageWidgetList(curPageWidgets);
                    self.naviPreBtnEnabled(curPage === 1 ? false : true);
                    self.naviNextBtnEnabled(totalPage > 1 && curPage!== totalPage ? true:false);
                };
                // Refresh widget/widget group data and UI displaying
                function refreshWidgets() {
                    widgetArray = [];
                    curGroupWidgets = [];
                    curPageWidgets=[];
                    searchResultArray = [];
                    index=0;
                    widgetIndex = 0;
                    widgetGroupList = [groupAll];
                    availableWidgetGroups = [];
                    self.currentWidget(null);
                    self.confirmBtnDisabled(true);
                    self.searchText("");
                    self.widgetOnLoading(true);
                    refreshPageData();
                    getWidgetGroups().done(function(data, textStatus, jqXHR){
                        oj.Logger.info("Finished loading widget groups. Start to load widgets.");
                        getWidgets().done(function(data, textStatus, jqXHR){
                            oj.Logger.info("Finished loading widget groups and widgets. Start to load page display data.");
                            //If already has search text input during widgets loading, then do a search after widgets loading finished
                            if (self.searchText() && $.trim(self.searchText()) !== "") {
                                self.widgetGroups(widgetGroupList);
                                var selectedGroup = widgetGroupList.length > 0 ? widgetGroupList[0].value : "";
                                self.categoryValue([selectedGroup]);
                                self.searchWidgets();
                            }
                            else {
                                refreshPageData();
                            }
                            self.widgetOnLoading(false);
                        })
                        .fail(function(xhr, textStatus, errorThrown){
                            oj.Logger.error("Failed to fetch widgets.");
                        });
                    })
                    .fail(function(xhr, textStatus, errorThrown){
                        oj.Logger.error("Failed to fetch widget groups.");
                    });
                };
                // Load widgets from ajax call result data
                function loadWidgets(data) {
                    if (data && data.length > 0) {
                        for (var i = 0; i < data.length; i++) {
                            if ((!widgetProviderName /*&& !widgetProviderVersion*/) ||
                                    (widgetProviderName === data[i].PROVIDER_NAME /*&& widgetProviderVersion === data[i].PROVIDER_VERSION*/)) {
                                var widget = data[i];
                                widget.index = widgetIndex;
                                widget.WIDGET_VISUAL = ko.observable();
                                widget.imgWidth = ko.observable("190px");
                                widget.imgHeight = ko.observable("140px");
                                if (!widget.WIDGET_DESCRIPTION){
                                    widget.WIDGET_DESCRIPTION = "";
                                }
                                widget.isSelected = ko.observable(false);
                                widget.isScreenShotPageDisplayed = ko.observable(true);
                                widget.isScreenshotLoaded = false;
                                widget.modificationDateString = getLastModificationTimeString(widget.WIDGET_CREATION_TIME);
                                if (index < pageSize) {
                                    loadWidgetScreenshot(widget);
                                    curPageWidgets.push(widget);
                                    index++;
                                }
                                widgetArray.push(widget);
                                widgetIndex++;
                            }
                        }
                    }
                };
                function loadWidgetScreenshot(widget) {
                    var url = widget.WIDGET_SCREENSHOT_HREF;
                    if (!url) { // backward compility if SSF doesn't support .png screenshot. to be removed once SSF changes are merged
                        loadWidgetBase64Screenshot(widget);
                        return;
                    }
                    if (!dfu.isDevMode()){
                        url = dfu.getRelUrlFromFullUrl(url);
                    }
                    widget && !widget.WIDGET_VISUAL && (widget.WIDGET_VISUAL = ko.observable(''));
                    url && widget.WIDGET_VISUAL(url);
                    if (!widget.WIDGET_VISUAL()) {
                        var laImagePath = "/emsaasui/uifwk/@version@/images/widgets/sample-widget-histogram.png";
                        var taImagePath = "/emsaasui/uifwk/@version@/images/widgets/sample-widget-histogram.png";
                        var itaImagePath = "/emsaasui/uifwk/@version@/images/widgets/sample-widget-histogram.png";
                        if ('LoganService' === widget.PROVIDER_NAME) {
                            widget.WIDGET_VISUAL(laImagePath);
                        }
                        else if ('TargetAnalytics' === widget.PROVIDER_NAME) {
                            widget.WIDGET_VISUAL(taImagePath);
                        }
                        else if ('EmcitasApplications' === widget.PROVIDER_NAME) {
                            widget.WIDGET_VISUAL(itaImagePath);
                        }else{
                            widget.WIDGET_VISUAL(itaImagePath); //default image
                        }
                    }
                    //resize widget screenshot according to aspect ratio
                    dfu.getScreenshotSizePerRatio(190, 140, widget.WIDGET_VISUAL(), function(imgWidth, imgHeight) {
                        widget.imgWidth(imgWidth + "px");
                        widget.imgHeight(imgHeight + "px");
                    });
                }
                function loadWidgetBase64Screenshot(widget) {
                    if (!widget.isScreenshotLoaded) {
                        var widgetsUrl = '/sso.static/savedsearch.widgets';
                        if (dfu.isDevMode()){
                            widgetsUrl=dfu.buildFullUrl(dfu.getDevData().ssfRestApiEndPoint,"/widgets");
                        }
                        var widgetScreenshotUrl = widgetsUrl + "/" + widget.WIDGET_UNIQUE_ID + "/screenshot";
                        dfu.ajaxWithRetry({
                            url: widgetScreenshotUrl,
                            headers: dfu.getSavedSearchServiceRequestHeader(),
                            success: function(data, textStatus) {
                                if (data && data.screenShot){
                                    widget.WIDGET_VISUAL(data.screenShot);
                                }
                                else {
                                    var laImagePath = "/emsaasui/uifwk/@version@/images/widgets/sample-widget-histogram.png";
                                    var taImagePath = "/emsaasui/uifwk/@version@/images/widgets/sample-widget-histogram.png";
                                    var itaImagePath = "/emsaasui/uifwk/@version@/images/widgets/sample-widget-histogram.png";
                                    if ('LogAnalyticsUI' === widget.PROVIDER_NAME) {
                                        widget.WIDGET_VISUAL(laImagePath);
                                    }
                                    else if ('TargetAnalytics' === widget.PROVIDER_NAME) {
                                        widget.WIDGET_VISUAL(taImagePath);
                                    }
                                    else if ('emcitas-ui-apps' === widget.PROVIDER_NAME) {
                                        widget.WIDGET_VISUAL(itaImagePath);
                                    }else{
                                        widget.WIDGET_VISUAL(itaImagePath); //default image
                                    }
                                }
                                widgetArray[widget.index].WIDGET_VISUAL(widget.WIDGET_VISUAL());
                                widget.isScreenshotLoaded = true;
                                widgetArray[widget.index].isScreenshotLoaded = true;
                            },
                            error: function(xhr, textStatus, errorThrown){
                                oj.Logger.error('Error when fetching widget screen shot by URL: '+widgetScreenshotUrl+'.');
                            },
                            async: true
                        });
                    }
                };
                // Load widget groups from ajax call result data
                function loadWidgetGroups(data) {
                    var targetWidgetGroupArray = [];
                    var pname = null;
                    var pversion = null;
                    var gname = null;
                    var groupId = null;
                    targetWidgetGroupArray.push(groupAll);
                    if (data && data.length > 0) {
                        for (var i = 0; i < data.length; i++) {
                            groupId = data[i].WIDGET_GROUP_ID;
                            pname = data[i].PROVIDER_NAME;
                            pversion = data[i].PROVIDER_VERSION;
                            gname = data[i].WIDGET_GROUP_NAME;
                            // Disable ITA widget group since no ITA widgets for now. (group id === 3)
                            if ((!widgetProviderName && groupId !== 3 /*&& !widgetProviderVersion */) ||
                                    widgetProviderName === pname || (widgetProviderName === 'TargetAnalytics' && groupId === 7)
                                    || (widgetProviderName === 'LogAnalyticsUI' && groupId === 6)
                                /*    && widgetProviderVersion === pversion */) {
                                    var widgetGroup = {value:pname+'|'+pversion+'|'+gname, 
                                                        label: groupId === 2 ? nls.WIDGET_SELECTOR_WIDGET_GROUP_NAME_TA : gname};
                                    targetWidgetGroupArray.push(widgetGroup);
                                    availableWidgetGroups.push(data[i]);
                            }
                        }
                    }
                    return targetWidgetGroupArray;
                };
                // Calculate the time difference between current date and the last modification date
                function getLastModificationTimeString(lastModifiedDate) {
                    var result = "";
                    if (lastModifiedDate) {
                        var currentDate = new Date().getTime();
                        var modificationDate = new Date(Date.parse(lastModifiedDate)).getTime();
                        var timediff = currentDate - modificationDate;
                        var min = 60*1000;
                        var hour = 60*min;
                        var day = 24*hour;
                        var month = 30*day;
                        var year = 12*month;
                        var agoText = " "+ nls.WIDGET_SELECTOR_TIME_DIFF_AGO;
                        var diffCount = null;
                        var diffUnit = null;
                        if (timediff < 60*1000) {
                            result = nls.WIDGET_SELECTOR_TIME_DIFF_A_MOMENT + agoText;
                        }
                        else if (timediff >= min && timediff < hour) {
                            diffCount = Math.round(timediff/min);
                            diffUnit = diffCount > 1 ? nls.WIDGET_SELECTOR_TIME_MINS :
                                    nls.WIDGET_SELECTOR_TIME_MIN;
                            result = diffCount + " " + diffUnit + agoText;
                        }
                        else if (timediff >= hour && timediff < day) {
                            diffCount = Math.round(timediff/hour);
                            diffUnit = diffCount > 1 ? nls.WIDGET_SELECTOR_TIME_HOURS :
                                    nls.WIDGET_SELECTOR_TIME_HOUR;
                            result = diffCount + " " + diffUnit + agoText;
                        }
                        else if (timediff >= day && timediff < month) {
                            diffCount = Math.round(timediff/day);
                            diffUnit = diffCount > 1 ? nls.WIDGET_SELECTOR_TIME_DAYS :
                                    nls.WIDGET_SELECTOR_TIME_DAY;
                            result = diffCount + " " + diffUnit + agoText;
                        }
                        else if (timediff >= month && timediff < year) {
                            diffCount = Math.round(timediff/month);
                            diffUnit = diffCount > 1 ? nls.WIDGET_SELECTOR_TIME_MONTHS :
                                    nls.WIDGET_SELECTOR_TIME_MONTH;
                            result = diffCount + " " + diffUnit + agoText;
                        }
                        else if (timediff >= year) {
                            diffCount = Math.round(timediff/year);
                            diffUnit = diffCount > 1 ? nls.WIDGET_SELECTOR_TIME_YEARS :
                                    nls.WIDGET_SELECTOR_TIME_YEAR;
                            result = diffCount + " " + diffUnit + agoText;
                        }
                    }
                    return result;
                };
                var message = {'tag': 'EMAAS_WIDGETSELECTOR_INITIALIZED'};
                window.postMessage(message, window.location.href);
            }
            return WidgetSelectorDialogViewModel;
        });
