/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['knockout',
        'jquery',
        'dashboards/dashboard-tile-model',
        'dfutil',
        'ojs/ojcore',
        'ojs/ojtree',
        'ojs/ojvalidation',
        'ojs/ojknockout-validation',
        'ojs/ojbutton',
        'ojs/ojselectcombobox',
        'ojs/ojpopup'
    ],
    
    function(ko, $, dtm, dfu)
    {
        // dashboard type to keep the same with return data from REST API
        var SINGLEPAGE_TYPE = "SINGLEPAGE";
        
        function getPlaceHolder(columns) {
            return $('<div class="dbd-tile oj-col oj-sm-' + columns + ' oj-md-' + columns + ' oj-lg-' + columns + ' dbd-tile-placeholder' + '"><div class="dbd-tile-header dbd-tile-header-placeholder">placeholder</div><div class="dbd-tile-placeholder-inner"></div></div>');
        }
            
        function createNewTileFromSearchObject(dtm, searchObject) {
            return new dtm.DashboardTile(
                    searchObject.getAttribute("name"), 
                    "", 
                    1,
                    searchObject.getAttribute("url"),
                    searchObject.getAttribute("chartType"));
        }
            
        function DashboardTilesView(dtm) {
            var self = this;
            self.dtm = dtm;
            
            self.disableSortable = function() {
                $(self.element).sortable("disable");
            };
            
            self.enableSortable = function(element, list) {
                if (!self.element)
                    self.element = element;
                if (!self.list)
                    self.list = list;
                if (!self.flag) {
                    $(element).sortable({
                        update: function(event, ui) {
                            if (ui.item.hasClass('dbd-tile')) {
                                var itemData = ko.dataFor(ui.item[0]);
                                var position = ko.utils.arrayIndexOf(ui.item.parent().children(), ui.item[0]);
                                if (position >= 0) {
                                    list.remove(itemData);
                                    list.splice(position, 0, itemData);
                                }
                            }
                            else {
                                var position = ko.utils.arrayIndexOf(ui.item.parent().children(), ui.item[0]);
                                if (position >= 0) {
                                    if (self.searchObject !== undefined) {
                                        var newTile = createNewTileFromSearchObject(self.dtm, self.searchObject);
                                        list.splice(position, 0, newTile);
                                    }
                                }
                            }
                            ui.item.remove();

                            /*var message = "Model layer sequence is: ";
                            for (var i = 0; i < list().length; i++) {
                                if (i !== 0)
                                    message += ",";
                                message += list()[i].title();
                            }
                            console.log(message);*/
                        },
                        dropOnEmpty: true,
                        forcePlaceholderSize: true,
                        placeholder: {
                            element: function(clone, ui) {
                                var itemWidth = 1;
                                if (clone.hasClass('dbd-tile')) {
                                    var itemData = ko.dataFor(clone[0]);
                                    itemWidth = itemData.width();
                                }
                                return getPlaceHolder(itemWidth * 3);
                            },
                            update: function() {
                                return;
                            }
                        },
                        handle: '.dbd-tile-header',
                        revert: true,
                        opacity: 0.5,
                        scroll: true,
                        tolerance: 'pointer'
                    });
                    self.flag = true;
                }
                else {
                    $(self.element).sortable("enable");
                }
            };
            
            self.disableDraggable = function() {
                $(".tile-container .oj-tree-leaf a").draggable("disable");
            };
            
            self.enableDraggable = function() {
                if (!self.init) {
                    $(".tile-container .oj-tree-leaf a").draggable({
                        helper: "clone",
                        scroll: false,
                        containment: '#tiles-row',
                        connectToSortable: '#tiles-row'
                    });
                    self.init = true;
                }
                else {
                    $(".tile-container .oj-tree-leaf a").draggable("enable");
                }
            };
        }
        
        function TileUrlEditView() {
            var self = this;
            self.tileToChange = ko.observable();
            self.url = ko.observable();
            self.tracker = ko.observable();
            
            self.setEditedTile = function(tile) {
                self.tileToChange(tile);
                self.originalUrl = tile.url();
            };
            
            self.applyUrlChange = function() {
                var trackerObj = ko.utils.unwrapObservable(self.tracker),
                    hasInvalidComponents = trackerObj["invalidShown"];
                if (hasInvalidComponents) {
                    trackerObj.showMessages();
                    trackerObj.focusOnFirstInvalid();
                } else
                    $('#urlChangeDialog').ojDialog('close');
            };
            
            self.cancelUrlChange = function() {
                self.tileToChange().url(self.originalUrl);
                $('#urlChangeDialog').ojDialog('close');
            };
        }
        
        function TimeSliderDisplayView() {
            var self = this;
            self.bindingExists = false;
            
            self.showOrHideTimeSlider = function(show) {
               var timeControl = $('#global-time-control');
               if (show){
                   timeControl.show();
               }else{
                   timeControl.hide();
               }
            };
        }
        
        function ToolBarModel(dashboard, tilesViewModel) {
            var self = this;
            self.tilesViewModel = tilesViewModel;
            
            self.includeTimeRangeFilter = ko.pureComputed({
                read: function() {
                    if (dashboard.enableTimeRange()) {
                        return ["ON"];
                    }else{
                        return ["OFF"];
                    }
                },
                write: function(value) {
                    if (value && value.indexOf("ON") >= 0) {
                        dashboard.enableTimeRange(true);
                    }
                    else {
                        dashboard.enableTimeRange(false);
                    }
                }
            });
            
            if (dashboard.id && dashboard.id())
                self.dashboardId = dashboard.id();
            else
                self.dashboardId = 9999; // id is expected to be available always
                    
            if(dashboard.name && dashboard.name()){
                self.dashboardName = ko.observable(dashboard.name());
            }else{
                self.dashboardName = ko.observable("Sample Dashboard");
            }
            self.dashboardNameEditing = ko.observable(self.dashboardName());
            if(dashboard.description && dashboard.description()){
                self.dashboardDescription = ko.observable(dashboard.description());
            }else{
                self.dashboardDescription = ko.observable("Description of sample dashboard. You can use dashboard builder to view/edit dashboard");
            }
            self.dashboardDescriptionEditing = ko.observable(self.dashboardDescription());
            self.editDisabled = ko.observable(dashboard.type() === SINGLEPAGE_TYPE);
            
            self.rightButtonsAreaClasses = ko.computed(function() {
                var css = "dbd-pull-right " + (self.editDisabled() ? "dbd-gray" : "");
                return css;
            });
            
            this.classNames = ko.observableArray(["oj-toolbars", 
                                          "oj-toolbar-top-border", 
                                          "oj-toolbar-bottom-border", 
                                          "oj-button-half-chrome"]);

            this.classes = ko.computed(function() {
                return this.classNames().join(" ");
            }, this);
            
            self.editDashboardName = function() {
                if (!$('#builder-dbd-description').hasClass('editing')) {
                    $('#builder-dbd-name').addClass('editing');
                    $('#builder-dbd-name-input').focus();
                }
            };
            
            self.okChangeDashboardName = function() {
                if (!$('#builder-dbd-name-input')[0].value) {
                    $('#builder-dbd-name-input').focus();
                    return;
                }
                self.dashboardName(self.dashboardNameEditing());
                if ($('#builder-dbd-name').hasClass('editing')) {
                    $('#builder-dbd-name').removeClass('editing');
                }
                dashboard.name(self.dashboardName());
            };
            
            self.cancelChangeDashboardName = function() {
                self.dashboardNameEditing(self.dashboardName());
                if ($('#builder-dbd-name').hasClass('editing')) {
                    $('#builder-dbd-name').removeClass('editing');
                }
            };
            
            self.editDashboardDescription = function() {
                if (!$('#builder-dbd-name').hasClass('editing')) {
                    $('#builder-dbd-description').addClass('editing');
                    $('#builder-dbd-description-input').focus();
                }
            };
            
            self.okChangeDashboardDescription = function() {
                if (!$('#builder-dbd-description-input')[0].value) {
                    $('#builder-dbd-description-input').focus();
                    return;
                }
                self.dashboardDescription(self.dashboardDescriptionEditing());
                if ($('#builder-dbd-description').hasClass('editing')) {
                    $('#builder-dbd-description').removeClass('editing');
                }
                dashboard.description(self.dashboardDescription());
            };
            
            self.cancelChangeDashboardDescription = function() {
                self.dashboardDescriptionEditing(self.dashboardDescription());
                if ($('#builder-dbd-description').hasClass('editing')) {
                    $('#builder-dbd-description').removeClass('editing');
                }
            };
            
            self.handleSettingsDialogOpen = function() {
                $('#settings-dialog').ojDialog('open');
            };
            
            self.handleSettingsDialogOKClose = function() {
                $("#settings-dialog").ojDialog("close");
            };
            
            self.messageToParent = ko.observable("Text message");
            
            self.handleMessageDialogOpen = function() {
                $("#parent-message-dialog").ojDialog("open");
            };
            
            self.getSummary = function(dashboardId, name, description, tilesViewModel) {
                function dashboardSummary(name, description) {
                    var self = this;
                    self.dashboardId = dashboardId;
                    self.dashboardName = name;
                    self.dashboardDescription = description;
                    self.widgets = [];
                };
                
                var summaryData = new dashboardSummary(name, description);
                if (tilesViewModel) {
                    for (var i = 0; i < tilesViewModel.dashboard.tiles().length; i++) {
                        var tile = tilesViewModel.dashboard.tiles()[i];
                        var widget = {"title": tile.title()};
                        summaryData.widgets.push(widget);
                    }
                }
                return summaryData;
            };
            
            self.handleGobackHomepage = function() {
                if (window.opener) {
                    var goBack = window.open('', 'dashboardhome');
                    goBack.focus();
                }
            };
            
            //Temp codes for widget test for integrators -- start, to be removed in release version
            self.resultTitle=ko.observable("");
            self.resultMsg=ko.observable("");
            self.categoryList=ko.observableArray([]);
            self.useAbsolutePathForUrls=ko.observable(false);
            self.providerInfoNeeded = ko.observable(true);
            var ssfUrl = dfu.discoverSavedSearchServiceUrl();
            var allCategories = [];
            if (ssfUrl === null && ssfUrl !== "") {
                console.log("Saved Search service is not available! Try again later.");
            }
            else {
                var categoryUrl = ssfUrl + '/categories';
                $.ajax({type: 'GET', contentType:'application/json',url: categoryUrl,
                    headers: dfu.getSavedSearchServiceRequestHeader(), 
                    async: false,
                    success: function(data, textStatus){
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                allCategories.push({label:data[i].name, value:data[i].id});
                            }
                            self.categoryList(allCategories);
                        }
                    },
                    error: function(data, textStatus){
                        console.log('Failed to query categories!');
                    }
                });
            }
            
//            self.newWidget = ko.observable({ 
//                                providerName: "Log Analytics",
//                                version: "1.0",
//                                assetRoot: "assetRoot",
//                                name: "TestWidget_001",
//                                description: "Widget for test",
//                                queryStr: "* | stats count by 'target type','log source'",
//                                categoryId: allCategories[0].value+"",
//                                kocName: "test-la-widget-1",
//                                vmUrl: "../dependencies/demo/logAnalyticsWidget/js/demo-log-analytics.js",
//                                templateUrl: "../dependencies/demo/logAnalyticsWidget/demo-log-analytics.html",
//                                iconUrl: "",
//                                histogramUrl: ""});
            self.newWidget = ko.observable({
                                providerName: "",
                                version: "",
                                assetRoot: "",
                                name: "",
                                description: "",
                                queryStr: "",
                                categoryId: allCategories[0].value+"",
                                kocName: "",
                                vmUrl: "",
                                templateUrl: "",
                                iconUrl: "",
                                histogramUrl: ""});
            
            self.categoryOptionChangeHandler = function(event, data) {
                if (data.option === "value") {
                    if (data.value[0]===999 || data.value[0] === '999') {
                        self.useAbsolutePathForUrls(true);
                        self.providerInfoNeeded(false);
                    }
                    else {
                        self.useAbsolutePathForUrls(false);
                        self.providerInfoNeeded(true);
                    }
                }
            };
            
            function showResultInfoDialog(title, msg) {
                self.resultTitle(title);
                self.resultMsg(msg);
                $("#resultInfoDialog").ojDialog("open");
            };
            
            self.createNewWidget = function() {
                $("#createWidgetDialog").ojDialog("open");
            };
            
            self.saveWidget = function() {
//                var ssfUrl = dfu.discoverSavedSearchServiceUrl();
                if (ssfUrl === null && ssfUrl !== "") {
                    console.log("Saved Search service is not available! Failed to create the widget.");
                    alert("Saved Search service is not available! Failed to create the widget.");
                    return;
                }
                else {
                    var widgetToSave = ko.toJS(self.newWidget);
                    var params = [];
                    if (self.providerInfoNeeded()===true) {
                        if (widgetToSave.providerName && widgetToSave.providerName !== "") {
                            params.push({name: "PROVIDER_NAME", type: "STRING", value: widgetToSave.providerName});
                        }
                        if (widgetToSave.version && widgetToSave.version !== "") {
                            params.push({name: "PROVIDER_VERSION", type: "STRING", value: widgetToSave.version});
                        }
                        if (widgetToSave.assetRoot && widgetToSave.assetRoot !== "") {
                            params.push({name: "PROVIDER_ASSET_ROOT", type: "STRING", value: widgetToSave.assetRoot});
                        }
                    }
                    if (widgetToSave.kocName && widgetToSave.kocName !== "") {
                        params.push({name: "WIDGET_KOC_NAME", type: "STRING", value: widgetToSave.kocName});
                    }
                    if (widgetToSave.vmUrl && widgetToSave.vmUrl !== "") {
                        params.push({name: "WIDGET_VIEWMODEL", type: "STRING", value: widgetToSave.vmUrl});
                    }
                    if (widgetToSave.templateUrl && widgetToSave.templateUrl !== "") {
                        params.push({name: "WIDGET_TEMPLATE", type: "STRING", value: widgetToSave.templateUrl});
                    }
                    if (widgetToSave.iconUrl && widgetToSave.iconUrl !== "") {
                        params.push({name: "WIDGET_ICON", type: "STRING", value: widgetToSave.iconUrl});
                    }
                    if (widgetToSave.histogramUrl && widgetToSave.histogramUrl !== "") {
                        params.push({name: "WIDGET_HISTOGRAM", type: "STRING", value: widgetToSave.histogramUrl});
                    }
                    params.push({name: "WIDGET_INTG_TESTING", type: "STRING", value: "YES"});
                    var searchToSave = {name: widgetToSave.name, 
                        category:{id:(widgetToSave.categoryId instanceof Array ? widgetToSave.categoryId[0] : widgetToSave.categoryId)},
                                        folder:{id: 999}, description: widgetToSave.description, 
                                        queryStr: widgetToSave.queryStr, parameters: params, isWidget:true};
                    var saveSearchUrl = ssfUrl + "/search";
                    $.ajax({type: 'POST', contentType:'application/json',url: saveSearchUrl, 
                        headers: dfu.getSavedSearchServiceRequestHeader(), data: ko.toJSON(searchToSave), async: false,
                        success: function(data, textStatus){
                            $('#createWidgetDialog').ojDialog('close');
                            var msg = "Widget created successfully!";
                            console.log(msg);
                            showResultInfoDialog("Success", msg);
                        },
                        error: function(data, textStatus){
                            $('#createWidgetDialog').ojDialog('close');
                            var msg = "Failed to create the widget! \nStatus: " + 
                                    data.status + "("+data.statusText+"), \nResponseText: "+data.responseText;
                            console.log(msg);
                            showResultInfoDialog("Error", msg);
                        }
                    });
                    
                    refreshWidgets();
                }
            };
            //Temp codes for widget test for integrators -- end
            
            self.handleDashboardSave = function() {
                var outputData = self.getSummary(self.dashboardId, self.dashboardName(), self.dashboardDescription(), self.tilesViewModel);
                outputData.eventType = "SAVE";
                var nodesToRecover = [];
                var nodesToRemove = [];
                var elems = $('#tiles-row').find('svg');
                elems.each(function(index, node) {
                    var parentNode = node.parentNode;
                    var width = $(node).width();
                    var height = $(node).height();
                    var svg = '<svg width="' + width + 'px" height="' + height + 'px">' + node.innerHTML + '</svg>';
                    var canvas = document.createElement('canvas');
                    canvg(canvas, svg);
                    nodesToRecover.push({
                        parent: parentNode,
                        child: node
                    });
                    parentNode.removeChild(node);
                    nodesToRemove.push({
                        parent: parentNode,
                        child: canvas
                    });
                    parentNode.appendChild(canvas);
                });
                html2canvas($('#tiles-row'), {
                    onrendered: function(canvas) {
                        var ctx = canvas.getContext('2d');
                        ctx.webkitImageSmoothingEnabled = false;
                        ctx.mozImageSmoothingEnabled = false;
                        ctx.imageSmoothingEnabled = false;
                        var data = canvas.toDataURL();
                        nodesToRemove.forEach(function(pair) {
                            pair.parent.removeChild(pair.child);
                        });
                        nodesToRecover.forEach(function(pair) {
                            pair.parent.appendChild(pair.child);
                        });
                        outputData.screenShot = data;
                        if (window.opener && window.opener.childMessageListener) {
                            var jsonValue = JSON.stringify(outputData);
                            console.log(jsonValue);
                            window.opener.childMessageListener(jsonValue);
                        }
                        tilesViewModel.dashboard.screenShot = ko.observable(data);
                        var dashboardJSON = ko.mapping.toJSON(tilesViewModel.dashboard, {
                            'include': ['screenShot', 'description', 'height', 
                                'isMaximized', 'title', 'type', 'width', 
                                'tileParameters', 'name', 'systemParameter', 
                                'tileId', 'value'],
                            'ignore': ["createdOn", "href", "owner", 
                                "screenShotHref", "systemDashboard",
                                "customParameters", "clientGuid", "dashboard", 
                                "fireDashboardItemChangeEvent", "getParameter", 
                                "maximizeEnabled", "narrowerEnabled", 
                                "onDashboardItemChangeEvent", "restoreEnabled", 
                                "setParameter", "shouldHide", "systemParameters", 
                                "tileDisplayClass", "widerEnabled", "widget"]
                        });
                        var dashboardId = tilesViewModel.dashboard.id();
                        dtm.updateDashboard(dashboardId, dashboardJSON, null, function(error) {
                            console.log(error.errorMessage());
                        });
                    }  
                });
            };
            
            self.isFavorite = ko.observable(false);
            self.initializeIsFavorite = function() {
                dtm.loadIsFavorite(self.dashboardId, function(isFavorite){
                    self.isFavorite(isFavorite);
                }, function(e) {
                    console.log(e.errorMessage());
                });
            }();  
            
            self.addToFavorites = function() {
                dtm.setAsFavorite(self.dashboardId, function() {
                    self.isFavorite(true);
//                    var outputData = self.getSummary(self.dashboardId, self.dashboardName(), self.dashboardDescription(), self.tilesViewModel);
//                    outputData.eventType = "ADD_TO_FAVORITES";
//                    if (window.opener && window.opener.childMessageListener) {
//                        var jsonValue = JSON.stringify(outputData);
//                        console.log(jsonValue);
//                        window.opener.childMessageListener(jsonValue);
//                        if (window.opener.navigationsModelCallBack())
//                        {
//                            navigationsModel(window.opener.navigationsModelCallBack());
//                        }
//                    }
                }, function(e) {
                    console.log(e.errorMessage());
                });
            };
            self.deleteFromFavorites = function() {
                dtm.removeFromFavorite(self.dashboardId, function() {
                    self.isFavorite(false);
//                    var outputData = self.getSummary(self.dashboardId, self.dashboardName(), self.dashboardDescription(), self.tilesViewModel);
//                    outputData.eventType = "REMOVE_FROM_FAVORITES";
//                    if (window.opener && window.opener.childMessageListener) {
//                        var jsonValue = JSON.stringify(outputData);
//                        console.log(jsonValue);
//                        window.opener.childMessageListener(jsonValue);
//                        if (window.opener.navigationsModelCallBack())
//                        {
//                            navigationsModel(window.opener.navigationsModelCallBack());
//                        }
//                    }
                }, function(e) {
                    console.log(e.errorMessage());
                });
            };
            
            //Add widget dialog
            self.categoryValue=ko.observableArray();
            self.widgetGroup=ko.observable();
            self.widgetGroupValue=ko.observable({providerName:"all",providerVersion:"all",name:"all"});
            self.widgetGroups=ko.observableArray();
            var widgetArray = [];
//            var laWidgetArray = [];
//            var taWidgetArray = [];
//            var itaWidgetArray = [];
//            var demoWidgetArray = [];
            var curGroupWidgets = [];
            var integratorWidgets = [];
            var widgetGroupList = [];
            var dbsWidgetArray = [];
            var curPageWidgets=[];
            var searchResultArray = [];
            var index=0;
            var pageSize = 6;
            var ssfUrl = dfu.discoverSavedSearchServiceUrl();
            var curPage = 1;
            var totalPage = 0;
            var naviFromSearchResults = false;
            var dbsBuiltinWidgets = [{
                    "WIDGET_UNIQUE_ID": 1,
                    "WIDGET_NAME": "Generic URL Widget",
                    "WIDGET_DESCRIPTION": "A generic widget to show a web page by a given URL",
                    "WIDGET_OWNER": "SYSMAN",
                    "WIDGET_CREATION_TIME": "2015-01-20T07:07:07.405Z",
                    "WIDGET_SOURCE": 0,
                    "WIDGET_GROUP_NAME": "Dashboards Built-In",
                    "WIDGET_TEMPLATE": "../dependencies/widgets/iFrame/widget-iframe.html",
                    "WIDGET_KOC_NAME": "DF_V1_WIDGET_IFRAME",
                    "WIDGET_VIEWMODEL": "../dependencies/widgets/iFrame/js/widget-iframe",
                    "PROVIDER_NAME": "DashboardFramework",
                    "PROVIDER_ASSET_ROOT": "asset",
                    "PROVIDER_VERSION": "1.0"}];
            self.widgetList = ko.observableArray(widgetArray);
            self.curPageWidgetList = ko.observableArray(curPageWidgets);
            self.searchText = ko.observable("");
//            self.naviPreBtnVisible=ko.observable(false);
//            self.naviNextBtnVisible=ko.observable(false);
            self.naviPreBtnVisible=ko.observable(curPage === 1 ? false : true);
            self.naviNextBtnVisible=ko.observable(totalPage > 1 && curPage!== totalPage ? true:false);

            self.widgetsCount = ko.observable(0);
            self.summaryMsg = ko.computed(function(){return "Search from " + self.widgetsCount() + " available widgets for your dashboard";}, this);

            self.currentWidget = ko.observable();
            var widgetClickTimer = null; 
            
            refreshWidgets();
            
            function refreshWidgets() {
                widgetArray = [];
//                laWidgetArray = [];
//                taWidgetArray = [];
//                itaWidgetArray = [];
//                demoWidgetArray = [];
                curGroupWidgets = [];
                curPageWidgets=[];
                searchResultArray = [];
                index=0;
                widgetGroupList = [];
                if (ssfUrl && ssfUrl !== '') {
//                    var laSearchesUrl = ssfUrl + '/searches?categoryId=1';
//                    var taSearchesUrl = ssfUrl + '/searches?categoryId=2';
//                    var itaSearchesUrl = ssfUrl + '/searches?categoryId=3';
//                    var demoSearchesUrl = ssfUrl + '/searches?categoryId=999';
                    var widgetsUrl = ssfUrl + '/widgets';
                    var widgetgroupsUrl = ssfUrl + '/widgetgroups';
                    $.ajax({
                        url: widgetgroupsUrl,
                        headers: dfu.getSavedSearchServiceRequestHeader(),
                        success: function(data, textStatus) {
                            widgetGroupList = loadWidgetGroups(data);
                        },
                        error: function(xhr, textStatus, errorThrown){
                            console.log('Error when fetching widgets!');
                        },
                        async: false
                    });
                    
                    $.ajax({
                        url: widgetsUrl,
                        headers: dfu.getSavedSearchServiceRequestHeader(),
                        success: function(data, textStatus) {
                            integratorWidgets = loadWidgets(data);
//                            curGroupWidgets = loadWidgets(data);
                        },
                        error: function(xhr, textStatus, errorThrown){
                            console.log('Error when fetching widgets!');
                        },
                        async: false
                    });

//                    $.ajax({
//                        url: taSearchesUrl,
//                        headers: getAuthorizationRequestHeader(),
//                        success: function(data, textStatus) {
//                            taWidgetArray = loadWidgets(data);
//                        },
//                        error: function(xhr, textStatus, errorThrown){
//                            console.log('Error when querying target analytics searches!');
//                        },
//                        async: false
//                    });
//
//                    $.ajax({
//                        url: itaSearchesUrl,
//                        headers: getAuthorizationRequestHeader(),
//                        success: function(data, textStatus) {
//                            itaWidgetArray = loadWidgets(data);
//                        },
//                        error: function(xhr, textStatus, errorThrown){
//                            console.log('Error when querying IT analytics searches!');
//                        },
//                        async: false
//                    });    
//                    $.ajax({
//                        url: demoSearchesUrl,
//                        headers: getAuthorizationRequestHeader(),
//                        success: function(data, textStatus) {
//                            demoWidgetArray = loadWidgets(data);
//                        },
//                        error: function(xhr, textStatus, errorThrown){
//                            console.log('Error when querying IT analytics searches!');
//                        },
//                        async: false
//                    });                      
                    
                    dbsWidgetArray = loadWidgets(dbsBuiltinWidgets);
                }

                curPage = 1;
                totalPage = (widgetArray.length%pageSize === 0 ? widgetArray.length/pageSize : Math.floor(widgetArray.length/pageSize) + 1);
                naviFromSearchResults = false;
                self.widgetGroups(widgetGroupList);
                self.widgetList(widgetArray);
                self.curPageWidgetList(curPageWidgets);
                self.searchText("");
                self.naviPreBtnVisible(curPage === 1 ? false : true);
                self.naviNextBtnVisible(totalPage > 1 && curPage!== totalPage ? true:false);
                self.widgetsCount(widgetArray.length);
            };
            
            function loadWidgets(data) {
                var targetWidgetArray = [];
                if (data && data.length > 0) {
                    for (var i = 0; i < data.length; i++) {
                        var widget = data[i];
                        targetWidgetArray.push(widget);
                        widgetArray.push(widget);
                        if (index < pageSize) {
                            curPageWidgets.push(widget);
                            index++;
                        }
                    }
                }
                return targetWidgetArray;
            };
            
            function loadWidgetGroups(data) {
                var targetWidgetGroupArray = [];
                var groupAll = {value:'all|all|All', label:'All'};
                var groupDashboardBuiltIn = {value: 'DashboardFramework|1.0|Dashboards Built-In', label:'Dashboards Built-In'};
                targetWidgetGroupArray.push(groupAll);
                targetWidgetGroupArray.push(groupDashboardBuiltIn);
                if (data && data.length > 0) {
                    for (var i = 0; i < data.length; i++) {
                        var widgetGroup = {value:data[i].PROVIDER_NAME+'|'+data[i].PROVIDER_VERSION+'|'+data[i].WIDGET_GROUP_NAME, 
                            label:data[i].WIDGET_GROUP_NAME};
                        targetWidgetGroupArray.push(widgetGroup);
                    }
                }
                return targetWidgetGroupArray;
            };

 
            self.openAddWidgetDialog = function() {
                $('#addWidgetDialog').ojDialog('open');
            };
            
            self.closeAddWidgetDialog = function() {
                $('#addWidgetDialog').ojDialog('close');
            };
            
            self.showAddWidgetTooltip = function() {
                if (tilesViewModel.isEmpty()) {
                   $('#add-widget-tooltip').ojPopup('open', "#add-widget-button");
                }
            };
            
            self.optionChangedHandler = function(data, event) {
                if (event.option === "value") {
                    curPageWidgets=[];
                    curPage = 1;
//                    var curWidgetGroup = event.value[0];
//                    var wg = curWidgetGroup.split('|');
//                    var pname = wg[0];
//                    var pversion = wg[1];
//                    var gname = wg[2];
                    var curGroupWidgets = getAvailableWidgets();
                    totalPage = (curGroupWidgets.length%pageSize === 0 ? curGroupWidgets.length/pageSize : Math.floor(curGroupWidgets.length/pageSize) + 1);
//                    if (curWidgetGroupName==='all') {
//                        totalPage = (widgetArray.length%pageSize === 0 ? widgetArray.length/pageSize : Math.floor(widgetArray.length/pageSize) + 1);
//                    }
//                    else if (event.value[0]==='la') {
//                        totalPage = (laWidgetArray.length%pageSize === 0 ? laWidgetArray.length/pageSize : Math.floor(laWidgetArray.length/pageSize) + 1);
//                    }
//                    else if (event.value[0]==='ta') {
//                        totalPage = (taWidgetArray.length%pageSize === 0 ? taWidgetArray.length/pageSize : Math.floor(taWidgetArray.length/pageSize) + 1);
//                    }
//                    else if (event.value[0] === 'ita') {
//                        totalPage = (itaWidgetArray.length%pageSize === 0 ? itaWidgetArray.length/pageSize : Math.floor(itaWidgetArray.length/pageSize) + 1);
//                    }
//                    else if (event.value[0] === 'demo') {
//                        totalPage = (demoWidgetArray.length%pageSize === 0 ? demoWidgetArray.length/pageSize : Math.floor(demoWidgetArray.length/pageSize) + 1);
//                    }                    
//                    else if (event.value[0] === 'dbs') {
//                        totalPage = (dbsWidgetArray.length%pageSize === 0 ? dbsWidgetArray.length/pageSize : Math.floor(dbsWidgetArray.length/pageSize) + 1);
//                    }
                    
                    fetchWidgetsForCurrentPage(curGroupWidgets);
                    self.curPageWidgetList(curPageWidgets);
                    refreshNaviButton();
                    naviFromSearchResults = false;
                }
            };
            
            self.gotoCreateNewWidget = function(){
                return window.open("http://slc08upj.us.oracle.com:7201/emlacore/faces/core-logan-observation-search");
            };
            
            self.naviPrevious = function() {
                if (curPage === 1) {
                    self.naviPreBtnVisible(false);
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
            };
            
            self.naviNext = function() {
                if (curPage === totalPage) {
                    self.naviNextBtnVisible(false);
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
            };
            
            self.widgetDbClicked = function(data, event) {
                clearTimeout(widgetClickTimer);
                self.tilesViewModel.appendNewTile(data.WIDGET_NAME, "", 2, data);
            };
            
            self.widgetClicked = function(data, event) {
                clearTimeout(widgetClickTimer);
                widgetClickTimer = setTimeout(function (){
                    var _data = ko.toJS(data);
                    _data.WIDGET_DESCRIPTION = '';
                    _data.QUERY_STR = '';
                    if (_data.WIDGET_GROUP_NAME !== 'Dashboards Built-In') {
                        if (ssfUrl && ssfUrl !== '') {
                            $.ajax({
                                url: ssfUrl+'/search/'+data.WIDGET_UNIQUE_ID,
                                headers: dfu.getSavedSearchServiceRequestHeader(),
                                success: function(widget, textStatus) {
                                    _data.WIDGET_DESCRIPTION = widget.description ? widget.description : '';
                                    _data.QUERY_STR = widget.queryStr ? widget.queryStr : '';
                                },
                                error: function(xhr, textStatus, errorThrown){
                                    console.log('Error when querying saved searches!');
                                },
                                async: false
                            });
                        }
                    }
                    
                    self.currentWidget(_data);
                    $('#widgetDetailsDialog').ojDialog('open');
                }, 300); 
            };
            
            self.closeWidgetDetailsDialog = function() {
                $('#widgetDetailsDialog').ojDialog('close');
            };
            
            self.addWidgetToDashboard = function() {
                $('#widgetDetailsDialog').ojDialog('close');
                self.tilesViewModel.appendNewTile(self.currentWidget().WIDGET_NAME, "", 2, self.currentWidget());
            };
            
            self.enterSearch = function(d,e){
                if(e.keyCode === 13){
                    self.searchWidgets();  
                }
                return true;
            };
            
            self.searchWidgets = function() {
                searchResultArray = [];
                var allWidgets = getAvailableWidgets();
                var searchtxt = $.trim(ko.toJS(self.searchText));
//                var category = ko.toJS(self.categoryValue);
//                if (!category || category.length === 0) {
//                    category = 'all';
//                }
//                else {
//                    category = category[0];
//                }
//                if (category === 'all') {
//                    allWidgets = widgetArray;
//                }
//                else if (category === 'la') {
//                    allWidgets = laWidgetArray;
//                }
//                else if (category === 'ta') {
//                    allWidgets = taWidgetArray;
//                }
//                else if (category === 'ita') {
//                    allWidgets = itaWidgetArray;
//                }
//                else if (category === 'demo') {
//                    allWidgets = demoWidgetArray;
//                }                
//                else if (category === 'dbs') {
//                    allWidgets = dbsWidgetArray;
//                }
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
            };
            
            function fetchWidgetsForCurrentPage(allWidgets) {
                curPageWidgets=[];
                for (var i=(curPage-1)*pageSize;i < curPage*pageSize && i < allWidgets.length;i++) {
                    curPageWidgets.push(allWidgets[i]);
                }
            };
            
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
                        if (widget.PROVIDER_NAME === providerName &&
                                widget.PROVIDER_VERSION === providerVersion &&
                                widget.WIDGET_GROUP_NAME === groupName) {
                            availWidgets.push(widget);
                        }
                    }
                }
//                else if (category === 'la') {
//                    allWidgets = laWidgetArray;
//                }
//                else if (category === 'ta') {
//                    allWidgets = taWidgetArray;
//                }
//                else if (category === 'ita') {
//                    allWidgets = itaWidgetArray;
//                }
//                else if (category === 'demo') {
//                    allWidgets = demoWidgetArray;
//                }                
//                else if (category === 'dbs') {
//                    allWidgets = dbsWidgetArray;
//                }
                
                return availWidgets;
            };
            
            function refreshNaviButton() {
                self.naviPreBtnVisible(curPage === 1 ? false : true);
                self.naviNextBtnVisible(totalPage > 1 && curPage!== totalPage ? true:false);
            };
            
            self.searchFilterFunc = function (arr, value)
            {/*
                    var _contains = function (s1, s2)
                    {
                        if (!s1 && !s2)
                            return true;
                        if (s1 && s2)
                        {
                            if (s1.toUpperCase().indexOf(s2.toUpperCase()) > -1)
                                return true;
                        }
                        return false;
                    };
                    console.log("Arrary length: " + arr.length);
                    console.log("Value: " + value);
                    var _filterArr = $.grep(_widgetArray, function (o) {
                        if (!value || value.length <= 0)
                            return true; //no filter
                        return _contains(o.name, value);
                    });
                    return _filterArr;*/
                console.log("Value: " + value);
                self.searchText(value);
                return searchResultArray;
            };

            self.searchResponse = function (event, data)
            {
                console.log("searchResponse: " + data.content.length);
                //self.widgetList(data.content);
                self.searchWidgets();
            };
            
            // code to be executed at the end after function defined
            tilesViewModel.registerTileRemoveCallback(self.showAddWidgetTooltip);
        }
        
        return {"DashboardTilesView": DashboardTilesView, 
            "TileUrlEditView": TileUrlEditView, 
            "TimeSliderDisplayView": TimeSliderDisplayView,
            "ToolBarModel": ToolBarModel};
    }
);
