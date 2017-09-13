define('uifwk/@version@/js/widgets/widgetselector/widget-selector-popup-impl',[
    'knockout',
    'jquery',
    'uifwk/@version@/js/util/df-util-impl', 
    'ojs/ojcore',
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg',
    'uifwk/@version@/js/util/typeahead-search-impl', 
    'uifwk/@version@/js/util/mobile-util-impl',
    'ojs/ojselectcombobox',
    'ojs/ojpopup',
    'ojs/ojinputtext',
    'ojs/ojbutton',
    'ojs/ojlistview', 
    'ojs/ojjsontreedatasource'
    ],
        function (ko, $, dfumodel, oj, nls, typeaheadsearch, mbu) {
            function WidgetSelectorPopupViewModel(params) {
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
                self.useIn = $.isFunction(params.useIn) ? params.useIn() : (params.useIn?params.useIn:'normal');
                self.widgetHandler = params.widgetHandler;
                self.initWidgetDraggable = params.initWidgetDraggable;
                self.buildPageResize = params.buildPageResize;
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
                self.widgetLableSource = nls.WIDGET_SELECTOR_WIDGET_SOURCE;
                self.widgetLableCreatedBy = nls.WIDGET_SELECTOR_WIDGET_CREATED_BY;
                self.widgetLableLastModified = nls.WIDGET_SELECTOR_WIDGET_LAST_MODIFIED;
                self.widgetNoDescription = nls.WIDGET_SELECTOR_WIDGET_NO_DESCRIPTION;
                self.isMobileDevice = ko.observable( (new mbu()).isSmallDevice );
                self.hideScrollbar = ko.observable(true);
                self.isMobileDevice()?self.hideScrollbar(false):self.hideScrollbar(true);
                self.needRefreshWidgetList = ko.observable(true);
                self.widgetGroupFilterVisible = ko.observable(widgetProviderName && widgetProviderVersion ? false : true);
                self.searchText = ko.observable("");
                self.clearButtonVisible = ko.computed(function(){return self.searchText() && '' !== self.searchText() ? true : false;});

                var dfu = new dfumodel(self.userName, self.tenantName);
                //Append uifwk css file into document head
                dfu.loadUifwkCss();

                // Initialize widget group and widget data
                var labelAll = nls.WIDGET_SELECTOR_WIDGET_GROUP_ALL;
                var widgetArray = [];
                var searchResultArray = [];
                var widgetIndex = 0;
                var queryWidgetsForAllSubscribedApps = false;
                var availableWidgetGroups = [];
                self.currentWidget = ko.observable();
                self.widgetOnLoading = ko.observable(true);
                self.widgetsDataSource = ko.observable();
                // Initialize data and refresh
                self.beforeOpenDialog = function(event, ui) {
                    self.refreshWidgets();
                };
                self.itemOnly = function(context){
                        return context['leaf'];
                };
                self.isWidgetOwnerGroup = function(file, bindingContext){
                        return bindingContext.$itemContext.leaf ? 'widget_details' : 'widget_group';
                } ;
                function isGroupListView(){
                    return $("li[id^=created-by]").length > 0;
                };


                self.widgetListScroll = function(){ 
                        if ($('.oj-listview').scrollTop() + $('.widget-selector-widgets').height() >= $('#widget-selector').height()) {
                            console.debug("Scrolled to the bottom of widget list. Loading more forwardly...");
                            self.forwardRenderWidgets(self.DEFAULT_WIDGET_INCREMENT_AMOUNT,isGroupListView());
                        }
                        if ($('.widget-selector-widgets').scrollTop() <= 0) {
                            console.debug("Scrolled to the top of widget list. Loading more backwardly...");
                            self.backwardRenderWidgets(self.DEFAULT_WIDGET_INCREMENT_AMOUNT,isGroupListView());
                        } 
                };
                self.initLoadOnScroll = function() {
                    $('.widget-selector-widgets').scroll(function() {
                        if ($('.widget-selector-widgets').scrollTop() + $('.widget-selector-widgets').height() >= $('#widget-selector').height()) {
                            console.debug("Scrolled to the bottom of widget list. Loading more forwardly...");
                            self.forwardRenderWidgets(self.DEFAULT_WIDGET_INCREMENT_AMOUNT,isGroupListView());
                        }
                        if ($('.widget-selector-widgets').scrollTop() <= 0) {
                            console.debug("Scrolled to the top of widget list. Loading more backwardly...");
                            self.backwardRenderWidgets(self.DEFAULT_WIDGET_INCREMENT_AMOUNT,isGroupListView());
                        }
                    });
                };
                self.DEFAULT_WIDGET_INIT_AMOUNT = 40;
                self.DEFAULT_WIDGET_INCREMENT_AMOUNT = 20; 
                self.MAX_LOAD_WIDGET_WINDOW_SIZE = 100; 
                self.widgetsData = [];
                self.loadedWidgetStartIndex = -1;
                self.loadedWidgetEndIndex = -1;    
                self.widgetList = [];

                self.forwardRenderWidgets = function (amount, isOnSearching) {  
                    if ((!self.widgetsData || self.widgetsData.length <= 0)&&isOnSearching) {
                        console.warn("Failed to load widgets data incrementally for widgetArray is empty");
                        generateWidgetsDataSource(self.widgetsData, isOnSearching);
                    }
                    if (!self.widgetsData || self.widgetsData.length <= 0) {
                        console.warn("Failed to load widgets data incrementally for widgetArray is empty");
                        return;
                    }
                    if (self.loadedWidgetEndIndex >= self.widgetsData.length-1) {
                        console.log("Do not need to load widgets data forwardly as all last widgets have been loaded");
                        return;
                    }
                    if (amount <= 0) {
                        console.warn("Failed to load widgets data incrementally for invalid amout of widget. Amount value is " + amount);
                        return;
                    }
                    var sizeToLoad = Math.min(amount, self.widgetsData.length - self.loadedWidgetEndIndex - 1);  
                    console.debug("Current loadedEndIndex (before loading) is:"+self.loadedWidgetEndIndex+" and all widgets size is:"+self.widgetsData.length+", size to load is:"+sizeToLoad);
                    for (var i = self.loadedWidgetEndIndex + 1; i < self.loadedWidgetEndIndex + 1 + sizeToLoad; i++) {  
                        self.widgetList.push(self.widgetsData[i]);
                    }
                    self.loadedWidgetEndIndex += sizeToLoad;
                    if (self.loadedWidgetStartIndex === -1) {
                        self.loadedWidgetStartIndex = 0;
                    }
                    console.debug("New widgets rendered forwardly. Loaded size:"+sizeToLoad+". Start index:"+self.loadedWidgetStartIndex+",end index:"+self.loadedWidgetEndIndex);
                    // need keep the max loaded widgets size
                    var renderedSize = self.loadedWidgetEndIndex - self.loadedWidgetStartIndex + 1;
                    if (renderedSize > self.MAX_LOAD_WIDGET_WINDOW_SIZE) {
                        console.debug("Rendered widgets size exceeds window size. Rendered size:"+renderedSize);
                        var deRenderSize = renderedSize - self.MAX_LOAD_WIDGET_WINDOW_SIZE;
                        for (var i = 0; i < deRenderSize; i++) {
                            self.widgetList.shift();
                        }
                        self.loadedWidgetStartIndex += deRenderSize;
                        console.debug("The first widgets were removed to keep the max window size. New start index is:"+self.loadedWidgetStartIndex);
                        // need to scroll widget list element to the correct position
                        var widgetHeight = $('.dbd-left-panel-widget').height(); // single widget element height
                    }
                    generateWidgetsDataSource(self.widgetList, isOnSearching);
                };
                
                self.backwardRenderWidgets = function (amount, isOnSearching) { // this method inserts values at the begining and removes at the end if needed
                    if (self.loadedWidgetStartIndex  <= 0) {
                        console.debug("Do not need to backward load widgets data as all first widgets have been loaded");
                        return;
                    }
                    if (amount <= 0) {
                        console.warn("Failed to backward load widgets data for invalid amout of widget. Amount value is " + amount);
                        return;
                    }
                    if (!self.widgetsData || self.widgetsData.length <= 0) {
                        console.warn("Failed to backward load widgets data incrementally for widgetsData is empty");
                        return;
                    }
                    var sizeToLoad = Math.min(amount, self.loadedWidgetStartIndex);
                    console.debug("Current loadedWidgetStartIndex (before loading) is:"+self.loadedWidgetStartIndex+" and all widgets size is:"+self.widgetsData.length+", size to load is:"+sizeToLoad);
                    for (var i = self.loadedWidgetStartIndex - 1; i > self.loadedWidgetStartIndex - 1 - sizeToLoad; i--) {
                        self.widgetList.unshift(self.widgetsData[i]);
                    }
                    self.loadedWidgetStartIndex -= sizeToLoad;
                    console.debug("New widgets rendered backwardly. Loaded size:"+sizeToLoad+". Start index:"+self.loadedWidgetStartIndex+",end index:"+self.loadedWidgetEndIndex);
                    // need to scroll widget list element to the correct position
                    var widgetHeight = $('.dbd-left-panel-widget').height(); // single widget element height
                    $('.dbd-left-panel-widgets').scrollTop(widgetHeight * sizeToLoad);
                    // need keep the max loaded widgets size
                    var renderedSize = self.loadedWidgetEndIndex - self.loadedWidgetStartIndex + 1;
                    if (renderedSize > self.MAX_LOAD_WIDGET_WINDOW_SIZE) {
                        console.debug("Rendered widgets size exceeds window size. Rendered size:"+renderedSize);
                        var deRenderSize = renderedSize - self.MAX_LOAD_WIDGET_WINDOW_SIZE;
                        for (var i = 0; i < deRenderSize; i++) {
                            self.widgetList.pop();
                        }
                        self.loadedWidgetEndIndex -= deRenderSize;
                        console.debug("The last widgets were removed to keep the max window size. Now end index is:"+self.loadedWidgetEndIndex);
                    }
                    generateWidgetsDataSource(self.widgetList, isOnSearching);
                };
                // Search widgets by selected widget group and search text(name, description)
                self.searchWidgets = function() {
                    searchResultArray = [];
                    var searchingHightLightTemplate = "<span class='widget-selector-search-matching'>$&</span>";
                    var allWidgets = widgetArray;
                    var searchtxt = $.trim(ko.toJS(self.searchText));
                    if (searchtxt === '') {
                        searchResultArray = allWidgets;
                    }
                    else {
                        for (var i=0; i<allWidgets.length; i++) {
                            if (allWidgets[i].WIDGET_NAME.toLowerCase().indexOf(searchtxt.toLowerCase()) > -1 ||
                                    (allWidgets[i].WIDGET_DESCRIPTION && allWidgets[i].WIDGET_DESCRIPTION.toLowerCase().indexOf(searchtxt.toLowerCase()) > -1)||
                                    (allWidgets[i].WIDGET_GROUP_NAME && allWidgets[i].WIDGET_GROUP_NAME.toLowerCase().indexOf(searchtxt.toLowerCase()) > -1)||
                                    (allWidgets[i].WIDGET_OWNER && allWidgets[i].WIDGET_OWNER.toLowerCase().indexOf(searchtxt.toLowerCase()) > -1)) {
                                var singleWidget = $.extend(true,{},allWidgets[i]);
                                singleWidget.highlightedName = singleWidget.WIDGET_NAME.replace(new RegExp(searchtxt, 'gi'), searchingHightLightTemplate);
                                singleWidget.highlightedDescription = singleWidget.WIDGET_DESCRIPTION.replace(new RegExp(searchtxt, 'gi'), searchingHightLightTemplate);
                                singleWidget.highlightedSource = singleWidget.WIDGET_GROUP_NAME && (self.widgetLableSource + singleWidget.WIDGET_GROUP_NAME.replace(new RegExp(searchtxt, 'gi'), searchingHightLightTemplate));
                                singleWidget.highlightedOwner = singleWidget.WIDGET_OWNER && (self.widgetLableCreatedBy + singleWidget.WIDGET_OWNER.replace(new RegExp(searchtxt, 'gi'), searchingHightLightTemplate));
                                searchResultArray.push(singleWidget);
                            }
                        }
                    }


                    self.widgetList = [];
                    self.widgetsData = searchResultArray;                    
                    self.loadedWidgetStartIndex = -1;
                    self.loadedWidgetEndIndex = -1;  
                    searchtxt?self.forwardRenderWidgets(self.DEFAULT_WIDGET_INIT_AMOUNT,true):self.forwardRenderWidgets(self.DEFAULT_WIDGET_INIT_AMOUNT, false);
                };

 
                self.onSearchBoxBlur = function(){
                    if(self.needRefreshWidgetList()){
                        !($.trim(ko.toJS(self.searchText))) && self.forwardRenderWidgets(self.DEFAULT_WIDGET_INIT_AMOUNT, false);
                    }
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

                // Widget box click handler
                self.widgetBoxClicked = function(data, event) { 
                    self.needRefreshWidgetList(false);
                    if (event.type === "keydown" && event.keyCode === 13 || event.type === "mousedown") {
                        $('#widget-selector').children().removeClass('oj-selected oj-focus oj-hover');
                        $('li[id^=created-by] > ul').children().removeClass('oj-selected oj-focus oj-hover');
                        var curWidget = self.currentWidget();
                        if (curWidget && (curWidget.PROVIDER_NAME !== data.PROVIDER_NAME ||
                            curWidget.WIDGET_UNIQUE_ID !== data.WIDGET_UNIQUE_ID)) { 
                            self.currentWidget(data);
                        }
                        else if (!curWidget) {
                            self.currentWidget(data);
                        }
                        if(!(event.type === "mousedown" && self.useIn === 'builder')){
                            widgetSelectionConfirmed();
                        }
                    }else if (event.type === "keydown" && event.keyCode === 9) { 
                        if (event.shiftKey) { 
                            navigateWidgetList(event, false);
                        }else{ 
                            navigateWidgetList(event, true);
                        }
                    }else if(event.target.id === "searchTxt"){ 
                        return true;
                    }
                    self.needRefreshWidgetList(true);
                };



                
                function navigateListView(event ,fromWidget ,toWidget ,isDown ,topOfWidgetList){
                    if(event.target.id === "searchTxt"){
                        toWidget = topOfWidgetList;
                        if(isGroupListView()) toWidget = $(topOfWidgetList.find("ul").children()[0]);
                    }else{
                        if(isDown && fromWidget.nextElementSibling){
                            toWidget = $(fromWidget.nextElementSibling);
                        }else if(!isDown && (event.target).previousElementSibling){
                            toWidget = $(fromWidget.previousElementSibling);
                        }
                    }
                    return toWidget;
                };
                
                function jumpToNextPrevGroup(fromWidget ,isDown ,toWidget){
                    var toGroup;
                    var fromGroup = $(fromWidget).closest("li[id^=created-by]");
                    if(isDown && fromGroup.next("li[id^=created-by]").length > 0){
                        toGroup= $(fromGroup.next("li[id^=created-by]"));   
                        toWidget = $($(toGroup.find("ul")).children()[0]);
                    }else if (!isDown && fromGroup.prev("li[id^=created-by]").length > 0){
                        toGroup= fromGroup.prev("li[id^=created-by]"); 
                        toWidget = $($(toGroup.find("ul")).children().last());
                    }
                    return toWidget;
                };
                
                function focusListItem(element){
                    element.attr("tabindex","0");
                    element.addClass("oj-selected oj-focus oj-hover"); 
                    element.focus(); 
                };
                
                function blurListItem(element){
                    element.attr("aria-selected","false");
                    element.removeClass("oj-selected oj-focus oj-hover");                              
                    element.removeAttr("tabindex");   
                    element.blur();  
                };
                 
     
                function navigateWidgetList(event ,isDown){  
                    var fromWidget = event.target;
                    var toWidget;
                     if((event.target.id === "searchTxt")&&!isDown){
                        $(event.target).blur();
                        $("#menubutton").focus();    
                        return;
                     }
                    var topOfWidgetList = $($("#widget-selector").children()[0]);
                    toWidget = navigateListView(event ,fromWidget ,toWidget ,isDown ,topOfWidgetList);                    
                    if(!toWidget && isGroupListView())toWidget = jumpToNextPrevGroup(fromWidget ,isDown ,toWidget);
                    $('#widget-selector').children().removeClass('oj-selected oj-focus oj-hover');
                    $('li[id^=created-by] > ul').children().removeClass('oj-selected oj-focus oj-hover');
                    blurListItem($(fromWidget)); 
                    toWidget ? focusListItem(toWidget) : $("#searchTxt").focus();
                    if(!(event.target.id === "searchTxt")){
                        topOfWidgetList.attr("aria-selected","false");
                        topOfWidgetList.removeClass("oj-selected oj-focus oj-hover");
                        $(topOfWidgetList.find("ul").children()[0]).attr("aria-selected","false");
                        $(topOfWidgetList.find("ul").children()[0]).removeClass("oj-selected oj-focus oj-hover");
                    }
                };
                // Widget handler for selected widget
                function widgetSelectionConfirmed(){
                    //Close dialog if autoCloseDialog is true or not set
                    if (self.autoCloseDialog !== false){
                        $('#'+self.dialogId).ojPopup('close');
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

                };

 

                // Refresh widget/widget group data and UI displaying
                self.refreshWidgets = function() {
                    widgetArray = [];
                    searchResultArray = [];
                    self.searchText("");
                    self.widgetOnLoading(true);

                   oj.Logger.info("Start to load widgets.");
                   getWidgets().done(function(data, textStatus, jqXHR){ 
                        self.widgetsData = widgetArray;
                        self.forwardRenderWidgets(self.DEFAULT_WIDGET_INIT_AMOUNT, false);
                        oj.Logger.info("Finished loading widget groups and widgets. Start to load page display data.");
                        if (self.searchText() && $.trim(self.searchText()) !== "") {
                                self.searchWidgets();
                        } 
                        self.widgetOnLoading(false);
                        self.initLoadOnScroll();
                        if (self.buildPageResize) {
                            self.buildPageResize();
                            self.buildPageResize = null;
                        }
                        if(self.initWidgetDraggable){
                            self.initWidgetDraggable();
                            $(".widget-selector-in-builder-page #widget-selector").on("ojready", function( event, ui ) {
                                self.initWidgetDraggable();
                            });
                        }
                      })
                        .fail(function(xhr, textStatus, errorThrown){
                            oj.Logger.error("Failed to fetch widgets.");
                        });
                };
                
                function generateWidgetsDataSource(data, isOnSearching){ 
                    var createByMeTitle = {"id": "created-by-me", "name": "CREATED BY ME"};
                    var createByOracleTitle = {"id": "created-by-oracle", "name": "CREATED BY ORACLE"};
                    var createByOthersTitle = {"id": "created-by-others", "name": "CREATED BY OTHERS"};
                    var widgetsCreatedByOracle = []; 
                    var widgetsCreatedByME = []; 
                    var widgetsCreatedByOthers = [];
                    var titleListMap = [{title: createByMeTitle, list: widgetsCreatedByME}, {title: createByOracleTitle, list: widgetsCreatedByOracle}, {title: createByOthersTitle, list: widgetsCreatedByOthers}];
                    //FOR TEST ADD THE SYSTEM WIDGET TO EVERY GROUP TO BE MODIFIED
                    var initWidgetsDataSource = [];
                    if(isOnSearching){
                        $.each($.grep(data, function(n){return n.WIDGET_OWNER === "ORACLE"}),function(n,value){
                            widgetsCreatedByOracle.push({"attr":value});                        
                        });
                        $.each($.grep(data, function(n){return n.WIDGET_OWNER === self.userName}),function(n,value){
                            widgetsCreatedByME.push({"attr":value});                        
                        });
                        $.each($.grep(data, function(n){return (n.WIDGET_OWNER !== self.userName)&&(n.WIDGET_OWNER !== "ORACLE")}),function(n,value){
                            widgetsCreatedByOthers.push({"attr":value});                        
                        });
                        for(var i = 0; i < titleListMap.length; ++i)
                            if(titleListMap[i].list.length>0){
                            initWidgetsDataSource.push({
                             "attr":titleListMap[i].title,
                             "children":titleListMap[i].list
                            });
                        }
                    }else{
                        $.each(data,function(n,value){
                            initWidgetsDataSource.push({"attr":value});                        
                        });
                    }
                    self.widgetsDataSource(new oj.JsonTreeDataSource(initWidgetsDataSource));
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
                                widget.highlightedName = widget.WIDGET_NAME;
                                widget.highlightedDescription = widget.WIDGET_DESCRIPTION;
                                widget.highlightedSource =  self.widgetLableSource + widget.WIDGET_GROUP_NAME;
                                widget.highlightedOwner =  self.widgetLableCreatedBy + widget.WIDGET_OWNER;
                                
                                if (!widget.WIDGET_DESCRIPTION){
                                    widget.WIDGET_DESCRIPTION = "";
                                }
                                widget.isSelected = ko.observable(false);
                                widget.isScreenShotPageDisplayed = ko.observable(true);
                                widget.isScreenshotLoaded = false;
                                widget.modificationDateString = getLastModificationTimeString(widget.WIDGET_CREATION_TIME);
                                loadWidgetScreenshot(widget);
                                widgetArray.push(widget);
                                widgetIndex++;
                            }
                        }
                    }
                    widgetArray.sort(function(wdgtA, wdgtB){
                        if(wdgtA.WIDGET_NAME > wdgtB.WIDGET_NAME){
                            return 1;
                        }else if(wdgtA.WIDGET_NAME < wdgtB.WIDGET_NAME){
                            return -1;
                        }else if(wdgtA.WIDGET_NAME === wdgtB.WIDGET_NAME){
                            return 0;
                        }
                    });
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
                self.useIn === 'builder' && self.refreshWidgets();
                var message = {'tag': 'EMAAS_WIDGETSELECTOR_INITIALIZED'};
                window.postMessage(message, window.location.href);
            }

            return WidgetSelectorPopupViewModel;
        });

