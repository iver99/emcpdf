define([
    'knockout',
    'ojs/ojcore',
    'jquery',
    'builder/right-panel/right.panel.util',
    'uifwk/js/util/screenshot-util',
    'uifwk/js/sdk/context-util',
    'dfutil'/*,    
    'emsaasui/emcta/ta/js/sdk/tgtsel/api/TargetSelectorUtils'*/
    ], function(ko, oj, $, rpu, ssu, contextModel, dfu/*, TargetSelectorUtils*/) {
        
        function RightPanelFilterModel($b, isDashboardSet) {
            var self = this;
            
            self.dashboard = $b.dashboard;
            self.rightPanelUtil = new rpu.RightPanelUtil();
            var ctxUtil = new contextModel();
            var omcContext = null;
            self.tilesViewModel = $b.getDashboardTilesViewModel();
            self.isDashboardSet = isDashboardSet;
            
            
            self.getFilterEnabledState = function(enableFilterValue) {
                if(self.isDashboardSet) {
                    if(enableFilterValue === 'TRUE' || enableFilterValue === 'GC') {
                        return 'ON';
                    }else if(enableFilterValue === 'FALSE') {
                        return 'OFF';
                    }else {
                        return null;
                    }
                }else {
                    if(enableFilterValue === 'TRUE') {
                        return 'ON';
                    }else if(enableFilterValue === 'FALSE') {
                        return 'OFF';
                    }else if(enableFilterValue === 'GC') {
                        return 'GC'
                    }else {
                        return null;
                    }
                }
            };
            
            self.getFilterEnabledValue = function(enableFilterState) {
                if(self.isDashboardSet) {
                    if(enableFilterState === 'ON' || enableFilterState === 'GC') {
                        return 'TRUE';
                    }else if(enableFilterState === 'OFF') {
                        return 'FALSE';
                    }else {
                        return null;
                    }
                }else {
                    if(enableFilterState === 'ON') {
                        return 'TRUE';
                    }else if(enableFilterState === 'OFF') {
                        return 'FALSE';
                    }else if(enableFilterState === 'GC') {
                        return 'GC';
                    }else {
                        return null;
                    }
                }
            };
            
            var defaultSettings = {
                    tsel:
                        {entitySupport: "byCriteria", entityContext: ""},
                    timeSel:
                        {defaultValue: "last14days", start: "", end: ""},
                    autoRefresh:
                        {defaultValue: 300000}
            };
            self.extendedOptions = self.dashboard.extendedOptions ? JSON.parse(self.dashboard.extendedOptions()) : defaultSettings;
            self.extendedOptions.tsel = self.extendedOptions.tsel ? self.extendedOptions.tsel : defaultSettings.tsel;
            self.extendedOptions.timeSel = self.extendedOptions.timeSel ? self.extendedOptions.timeSel : defaultSettings.timeSel;

            //set entity support/selectionMode
            self.extendedOptions.tsel && self.extendedOptions.tsel.entitySupport && $b.getDashboardTilesViewModel && $b.getDashboardTilesViewModel().selectionMode(self.extendedOptions.tsel.entitySupport);
            
            self.defaultEntityContext = ko.observable(self.extendedOptions.tsel.entityContext);
            self.defaultTimeRangeValue = ko.observable([self.extendedOptions.timeSel.defaultValue]);
            var endTimeNow = new Date().getTime();
            self.defaultStartTime = ko.observable(parseInt(self.extendedOptions.timeSel.start===""? (""+endTimeNow-14*24*3600*1000):self.extendedOptions.timeSel.start));
            self.defaultEndTime = ko.observable(parseInt(self.extendedOptions.timeSel.end===""? (""+endTimeNow):self.extendedOptions.timeSel.end));
            
//            self.enableEntityFilter = ko.observable((self.dashboard.enableEntityFilter() === 'TRUE')?'ON':'OFF');
//            self.enableTimeRangeFilter = ko.observable(self.dashboard.enableTimeRange && (self.dashboard.enableTimeRange() === 'TRUE'?'ON':'OFF'));
            self.enableEntityFilter = ko.observable(self.getFilterEnabledState(self.dashboard.enableEntityFilter()));
            self.enableTimeRangeFilter = ko.observable(self.getFilterEnabledState(self.dashboard.enableTimeRange ? self.dashboard.enableTimeRange() : 'TRUE'));

            self.entitySupport = ko.observable(true);
            if($b.getDashboardTilesViewModel) {
                if($b.getDashboardTilesViewModel().selectionMode()==="byCriteria") {
                    self.entitySupport(true);
                }else {
                    self.entitySupport(false);
                }
            }
            
            self.enableEntityFilter.subscribe(function(val){
                val = self.getFilterEnabledValue(val);
                self.dashboard.enableEntityFilter(val);
                //show/hide GC bar accordingly
                var headerWrapper = $("#headerWrapper")[0];
                var headerViewModel =  null;
                if(headerWrapper) {
                    headerViewModel = ko.dataFor(headerWrapper);
                }
                if(val === "GC") {
                    headerViewModel.brandingbarParams.showGlobalContextBanner(true);
                }else {
                    headerViewModel.brandingbarParams.showGlobalContextBanner(false);
                }
                //1. reset respectOMCApplicationContext flag and respectOMCEntityContext flag, get entity context info
                //2. update/refresh value of entity seletor accordingly
                //3. fire event to widgets
                //
                //1. reset respectOMCApplicationContext flag and respectOMCEntityContext flag, get entity context info
                var entityContext = null;
                var dashboardTilesViewModel = self.tilesViewModel;                
                entityContext = dashboardTilesViewModel.getEntityContext(dashboardTilesViewModel, val);
                //2. update/refresh value of entity seletor accordingly
                //to do... check if updating inputCriteria updates entity selector
                entityContext && dashboardTilesViewModel.targets(entityContext);
                //3. fire event to widgets
                dashboardTilesViewModel.timeSelectorModel.timeRangeChange(true);
            });
            
            self.enableTimeRangeFilter.subscribe(function(val){
                val = self.getFilterEnabledValue(val);
                self.dashboard.enableTimeRange(val);
                //1. reset respectOMCTimeContext flag and get time context infp
                //2. update/refresh value of entity seletor accordingly
                //3. fire event to widgets
                //
                //1. reset respectOMCTimeContext flag and get time context info
                var timePeriod = null;
                var start = null;
                var end = null;
                var dashboardTilesViewModel = self.tilesViewModel;

                var timeContext = dashboardTilesViewModel.getTimeContext(dashboardTilesViewModel, val);
                start = timeContext.start;
                end = timeContext.end;
                timePeriod = timeContext.timePeriod;
                
                //2. update/refresh value of entity seletor accordingly
                if(ctxUtil.formalizeTimePeriod(timePeriod) && ctxUtil.formalizeTimePeriod(timePeriod) !== "CUSTOM") {
                    dashboardTilesViewModel.timePeriod(timePeriod);
                }else if(ctxUtil.formalizeTimePeriod(timePeriod) === "CUSTOM" && start instanceof Date && end instanceof Date) {
                    dashboardTilesViewModel.initStart(start);
                    dashboardTilesViewModel.initEnd(end);
                }
                //3. change time context in timeSelectorModel and fire event to widgets
                var viewStart = start;
                var viewEnd = end;
                var viewTimePeriod = (timePeriod === null) ? "Last 14 days" : timePeriod;
                if(ctxUtil.formalizeTimePeriod(timePeriod)) {
                    if(ctxUtil.formalizeTimePeriod(timePeriod) !== "CUSTOM") { //get start and end time for relative time period
                        var tmp = ctxUtil.getStartEndTimeFromTimePeriod(ctxUtil.formalizeTimePeriod(timePeriod));
                        if(tmp) {
                            viewStart = tmp.start;
                            viewEnd = tmp.end;
                        }
                    }
                }

                if(!(viewStart instanceof Date && viewEnd instanceof Date)) {
                    var current = new Date();
                    viewStart = new Date(current - 14*24*60*60*1000);
                    viewEnd = current;
                }
                dashboardTilesViewModel.timeSelectorModel.viewStart(viewStart);
                dashboardTilesViewModel.timeSelectorModel.viewEnd(viewEnd);
                dashboardTilesViewModel.timeSelectorModel.viewTimePeriod(viewTimePeriod);
                dashboardTilesViewModel.timeSelectorModel.timeRangeChange(true);
            });
            
            //reset default entity value and entity context when entity support is changed
            self.entitySupport.subscribe(function(val) {
                val = val?"byCriteria":"single";
                self.extendedOptions.tsel.entitySupport = val;
                window.selectedDashboardInst().tilesViewModel.selectionMode(val);

            });

            //default auto-refresh and filter settings displayed in share section
            self.defaultAutoRefreshValue = ko.observable("every5minutes");
            if(self.extendedOptions.autoRefresh) {
                if(parseInt(self.extendedOptions.autoRefresh.defaultValue) === 0) {
                    self.defaultAutoRefreshValue("off");
                }else if(parseInt(self.extendedOptions.autoRefresh.defaultValue) === 300000) {
                    self.defaultAutoRefreshValue("every5minutes");
                }
            }else {
                self.extendedOptions.autoRefresh = {defaultValue: 300000};
            }

            self.defaultAutoRefreshValueText = ko.computed(function() {
                if(self.defaultAutoRefreshValue() === "off") {
                    return getNlsString("DBS_BUILDER_AUTOREFRESH_OFF");
                }else if(self.defaultAutoRefreshValue() === "every5minutes") {
                    return getNlsString("DBS_BUILDER_AUTOREFRESH_ON");
                }
            });
            
            self.defaultTimeRangeValueText = ko.computed(function() {
                if((self.defaultTimeRangeValue()[0] !== "custom") && (self.defaultTimeRangeValue()[0] !== "custom1")) {
                    return self.rightPanelUtil.getDefaultTimeRangeValueText(self.defaultTimeRangeValue()[0]);
                }else {
                    return self.rightPanelUtil.getTimeInfo(self.defaultStartTime(), self.defaultEndTime());
                }
            });

            self.defaultEntityValueText = ko.observable(getNlsString("DBS_BUILDER_ALL_ENTITIES"));
            self.labelInited = false;
            self.defaultEntityValueChanged = ko.computed(function() {
                if(!self.dashboard.sharePublic() || !self.labelInited) {
                    var val = self.defaultEntityContext();

                    if(!val) {
                        self.defaultEntityValueText(getNlsString("DBS_BUILDER_ALL_ENTITIES"));
                        return getNlsString("DBS_BUILDER_ALL_ENTITIES");
                    }

                    var tselId = "tsel_"+self.dashboard.id();
                    var label;
                    var labelIntervalId = setInterval(function() {
                        if(self.labelInited || self.enableEntityFilter() === "OFF") {
                            clearInterval(labelIntervalId);
                        }
                       if($("#"+tselId).children().get(0) && ko.contextFor($('#' + tselId).children().get(0)).$component.cm.dropdownInitialLabel()) {
                            label =  ko.contextFor($('#' + tselId).children().get(0)).$component.getDropdownLabelForContext(tselId, val);
                            self.labelInited = true;
                        }else {
                            label = getNlsString("DBS_BUILDER_ALL_ENTITIES");
                        }
                        self.defaultEntityValueText(label);
                        return label;
                    }, 500);
                }
            });
            
            self.defaultValueChanged = ko.observable(new Date());
            //handle with auto-saving of filter setting in right drawer
            self.dsbRtDrFiltersSaveDelay = ko.computed(function() {
                return self.enableEntityFilter() + self.entitySupport() + self.defaultEntityContext() +
                        self.enableTimeRangeFilter() + self.defaultTimeRangeValue() + self.defaultValueChanged();
            });

            self.dsbRtDrFiltersSaveDelay.extend({rateLimit: {method: "notifyWhenChangesStop", timeout: 800}});

            self.dsbRtDrFiltersSaveDelay.subscribe(function() {
                if(self.dashboard.systemDashboard() || self.dashboard.owner() !== dfu.getUserName()) {
                    console.log("This is an OOB dashboard or the current user is not owner of the dashboard");
                    return;
                }
                var fieldsToUpdate = {
                    "enableEntityFilter": self.dashboard.enableEntityFilter(),
                    "extendedOptions": JSON.stringify(self.extendedOptions),
                    "enableTimeRange": self.dashboard.enableTimeRange()
                };

                if (self.dashboard.tiles() && self.dashboard.tiles().length > 0) {
                    var elem = $(".tiles-wrapper:visible");
                    var clone = Builder.createScreenshotElementClone(elem);
                    ssu.getBase64ScreenShot(clone, 314, 165, 0.8, function(data) {
                        Builder.removeScreenshotElementClone(clone);
                        self.dashboard.screenShot = ko.observable(data);
                        self.handleSaveDsbFilterSettings(fieldsToUpdate);
                    });
                }
                else {
                    self.dashboard.screenShot = ko.observable(null);
                    self.handleSaveDsbFilterSettings(fieldsToUpdate);
                }
            });

            self.handleSaveDsbFilterSettings = function(fieldsToUpdate) {
                self.saveDsbFilterSettings(fieldsToUpdate, function() {
                    if(!self.dashboard.extendedOptions) {
                        self.dashboard.extendedOptions = ko.observable();
                    }
                    self.dashboard.extendedOptions(JSON.stringify(self.extendedOptions));
                },
                function() {
                    console.log("Error occurred when handle saving dashboard filter settings");
                });
            };

            self.saveDsbFilterSettings = function(fieldsToUpdate, succCallback, errorCallback) {
                var newDashboardJs = ko.mapping.toJS(self.dashboard, {
                    'include': ['screenShot', 'description', 'height',
                        'isMaximized', 'title', 'type', 'width',
                        'tileParameters', 'name', 'systemParameter',
                        'tileId', 'value', 'content', 'linkText',
                        'WIDGET_LINKED_DASHBOARD', 'linkUrl'],
                    'ignore': ["createdOn", "href", "owner", "modeWidth", "modeHeight",
                        "modeColumn", "modeRow", "screenShotHref", "systemDashboard",
                        "customParameters", "clientGuid", "dashboard",
                        "fireDashboardItemChangeEvent", "getParameter",
                        "maximizeEnabled", "narrowerEnabled",
                        "onDashboardItemChangeEvent", "restoreEnabled",
                        "setParameter", "shouldHide", "systemParameters",
                        "tileDisplayClass", "widerEnabled", "widget",
                        "WIDGET_DEFAULT_HEIGHT", "WIDGET_DEFAULT_WIDTH"]
                });

                $.extend(newDashboardJs, fieldsToUpdate);
                new Builder.DashboardDataSource().updateDashboardData(self.dashboard.id(), JSON.stringify(newDashboardJs), succCallback, errorCallback);
            };
            
            self.loadRightPanelFilter = function(tilesViewModel) {
                self.tilesViewModel = tilesViewModel;
                var dashboard = tilesViewModel.dashboard;
                if(!dashboard.extendedOptions) {
                    dashboard.extendedOptions = ko.observable("{\"tsel\": {\"entitySupport\": \"byCriteria\", \"entityContext\": \"\"}, \"timeSel\": {\"defaultValue\": \"last14days\", \"start\": 0, \"end\": 0}}");
                }
                self.dashboard = dashboard;
                var extendedOptions = JSON.parse(dashboard.extendedOptions());
                self.extendedOptions = extendedOptions ? extendedOptions : self.extendedOptions;
                var tsel = extendedOptions && extendedOptions.tsel ? extendedOptions.tsel : {};
                var timeSel = extendedOptions && extendedOptions.timeSel ? extendedOptions.timeSel : {};
                //1. reset tsel in right drawer
//                self.enableEntityFilter((dashboard.enableEntityFilter() === 'TRUE')?'ON':'OFF');
                self.enableEntityFilter(self.getFilterEnabledState(dashboard.enableEntityFilter()));
                self.entitySupport(tsel.entitySupport?(tsel.entitySupport==="byCriteria"?true:false):true);
                self.defaultEntityContext(tsel.entityContext ? tsel.entityContext : {});
                tilesViewModel.selectionMode(self.entitySupport()?"byCriteria":"single");
                window.DashboardWidgetAPI && window.DashboardWidgetAPI.setTargetSelectionContext(tilesViewModel.targets());
                //2. reset timeSel in right drawer
//                self.enableTimeRangeFilter((dashboard.enableTimeRange() === 'TRUE')?'ON':'OFF');
                self.enableTimeRangeFilter(self.getFilterEnabledState(dashboard.enableTimeRange()));
                self.defaultTimeRangeValue([timeSel.defaultValue]);
                self.defaultStartTime(parseInt(timeSel.start));
                self.defaultEndTime(parseInt(timeSel.end));
            };
            
            self.setDefaultValuesWhenSharing = function (creatorExtendedOptions) {
                //creator settings is used as default dashboard settings when the dashboard is shared
                //set what to show for filters/auto-refresh in share area & set what filter/auto_refreh settings to save when this dashboard is shared
                //1. timeSel
                var timeSel = creatorExtendedOptions.timeSel;
                if(!$.isEmptyObject(timeSel)) {
                    self.defaultTimeRangeValue([timeSel.timePeriod]);
                    self.defaultStartTime(timeSel.start);
                    self.defaultEndTime(timeSel.end);
                    
                    self.extendedOptions.timeSel.start = timeSel.start;
                    self.extendedOptions.timeSel.end = timeSel.end;
                    self.extendedOptions.timeSel.defaultValue = timeSel.timePeriod;
                }
                //2. tsel
                var tsel = creatorExtendedOptions.tsel;
                if(!$.isEmptyObject(tsel)) {                    
                    self.defaultEntityContext(tsel.entityContext);
                    
                    self.extendedOptions.tsel.entityContext = tsel.entityContext;
                }
                //3. auto-refresh
                var interval = creatorExtendedOptions.autoRefresh.defaultValue;
                var intervalValue;
                if (interval === 0) {
                    intervalValue = "off";
                } else if (interval === 300000) {
                    intervalValue = "every5minutes";
                }
                self.defaultAutoRefreshValue(intervalValue);
                self.extendedOptions.autoRefresh.defaultValue = interval;

                self.defaultValueChanged(new Date());
            };
        }
        
        return {'RightPanelFilterModel': RightPanelFilterModel};
});