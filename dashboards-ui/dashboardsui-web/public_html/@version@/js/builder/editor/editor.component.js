/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['knockout', 
        'jquery', 
        'ojs/ojcore',
        'dfutil',
        'builder/dashboard.tile.model',
        'builder/editor/editor.tiles'
    ], 
    function(ko, $, oj, dfu) {
        function Cell(row, column) {
            var self = this;
            
            self.row = row;
            self.column = column;
        }
        Builder.registerModule(Cell, 'Cell');
        
        /**
         * 
         * @param {Date} startTime: start time of new time range
         * @param {Date} endTime: end time of new time range
         * @returns {DashboardTimeRangeChange} instance
         */
        function DashboardTimeRangeChange(startTime, endTime){
            var self = this;
            if (startTime instanceof Date){
                self.viewStartTime = startTime;
            }
            if (endTime instanceof Date){
                self.viewEndTime = endTime;
            }
        }
        Builder.registerModule(DashboardTimeRangeChange, 'DashboardTimeRangeChange');

        function DashboardTargetContext(target, type, emsite) {
            var self = this;
            self.target = target;
            self.type = type;
            self.emsite = emsite;
        }
        Builder.registerModule(DashboardTargetContext, 'DashboardTargetContext');

        /**
         * 
         * @param {String} name: name of custome item
         * @param {Object} value: new value of custome item
         * @returns {undefined}
         */
        function DashboardCustomChange(name, value){
            var self = this;
            if (name){
                self.name = name.toString();
                self.value = value;
            }
        }
        Builder.registerModule(DashboardCustomChange, 'DashboardCustomChange');

        /**
         * 
         * @param {String} status: name of status
         * Event NAME:
         * PRE_REFRESH: Before refresh starts
         * POST_HIDE_TITLE: After title of tile is Hidden
         * POST_DELETE: After tile is deleted
         * POST_WIDER: After tile is wider
         * POST_NARROWER: After tile is narrower
         * POST_TALLER: After tile is taller
         * POST_SHORTER: After tile is shorter
         * POST_MAXIMIZE: After tile is maximized
         * POST_RESTORE: After tile is restored
         * POST_WINDOWRESIZE: After window resize
         * @returns {undefined}
         */
        function TileChange(status){
            var self = this;
            if (status){
                self.status = status.toString();
            }
        }
        Builder.registerModule(TileChange, 'TileChange');

        function DashboardItemChangeEvent(timeRangeChange, targetContext, customChanges, tileChange, timeRangeEnabled){
            var self = this;
            self.timeRangeChange = null;
            self.targetContext = null;
            self.customChanges = null;
            self.enableTimeRange = null;
            self.timeRangeEnabled = null;
//            if(enableTimeRange === "FALSE" && Builder.isTimeRangeAvailInUrl() === false) {
//                self.timeRangeChange = null;
//            }else{
//                if (timeRangeChange instanceof DashboardTimeRangeChange){
//                    self.timeRangeChange = timeRangeChange;
//                }
//            }
            if (timeRangeChange instanceof DashboardTimeRangeChange){
                self.timeRangeChange = timeRangeChange;
            }
            if(targetContext instanceof DashboardTargetContext) {
                self.targetContext = targetContext;
            }

            if (customChanges instanceof Array){
                for(var i=0;i<customChanges.length;i++){
                    var change = customChanges[i];
                    if (change instanceof DashboardCustomChange){
                        if (!self.customChanges){
                            self.customChanges = [];
                        }
                        self.customChanges.push(change);
                    }else{
                        console.log("ERROR: "+"invalid custom change: "+change);
                        oj.Logger.error("ERROR: "+"invalid custom change: "+change);
                    }
                }
            }
            if (tileChange instanceof TileChange){
                self.tileChange = tileChange;
            }
            if(timeRangeEnabled === "TRUE") {
                self.timeRangeEnabled = true;
            }else {
                self.timeRangeEnabled = false;
            }
        }
        Builder.registerModule(DashboardItemChangeEvent, 'DashboardItemChangeEvent');
        
        function DashboardTextTile(mode, $b, widget, funcShow, deleteTextCallback) {
            var self = this;
            self.dashboard = $b.dashboard;
            self.title = ko.observable("text widget title"); //to do 
            self.description = ko.observable();
            self.isMaximized = ko.observable(false);

            var kowidget;
            if(widget.type === "TEXT_WIDGET") {
                kowidget = new Builder.TextTileItem(widget);
            }else {
                kowidget = new Builder.TileItem(widget);
            }

            for (var p in kowidget)
                self[p] = kowidget[p];

            Builder.initializeTextTileAfterLoad(mode, $b, self, funcShow, deleteTextCallback, Builder.isContentLengthValid);            
        }
        Builder.registerModule(DashboardTextTile, 'DashboardTextTile');

        /**
         *  Object used to represents a dashboard tile created by clicking adding widget
         *  
         *  @param dashboard which dashboard the tile is inside
         *  @param type type of the dashboard
         *  @param title title for the tile
         *  @param description description string for the tile
         *  @param width width for the tile
         *  @param widget widget from which the tile is to be created
         */
        function DashboardTile(mode, dashboard, type, title, description, widget, timeSelectorModel, targetContext, loadImmediately) {
            var self = this;
            self.dashboard = dashboard;
            self.title = ko.observable(title);
            self.description = ko.observable(description);
            self.isMaximized = ko.observable(false);            

            var kowidget;
            if(widget.type === "TEXT_WIDGET") {
                kowidget = new Builder.TextTileItem(widget);
            }else {
                kowidget = new Builder.TileItem(widget);
            }
            for (var p in kowidget)
                self[p] = kowidget[p];
            if(self['WIDGET_SUPPORT_TIME_CONTROL']) {
                if (self['WIDGET_SUPPORT_TIME_CONTROL']() === '0')
                    self['WIDGET_SUPPORT_TIME_CONTROL'](false);
                else
                    self['WIDGET_SUPPORT_TIME_CONTROL'](true);
                window.DEV_MODE && console.debug("self['WIDGET_SUPPORT_TIME_CONTROL'] is set to " + self['WIDGET_SUPPORT_TIME_CONTROL']());
            }


            Builder.initializeTileAfterLoad(mode, dashboard, self, timeSelectorModel, targetContext, loadImmediately);
        }
        Builder.registerModule(DashboardTile, 'DashboardTile');

        function initializeTextTileAfterLoad(mode, $b, tile, funcShow, deleteTextCallback) {
            if(!tile) {
                return;
            }
            var dashboard = $b.dashboard;
            Builder.registerComponent(tile.WIDGET_KOC_NAME(), tile.WIDGET_VIEWMODEL(), tile.WIDGET_TEMPLATE());
            tile.shouldHide = ko.observable(false);            
            tile.toBeOccupied = ko.observable(false);
            var _currentUser = dfu.getUserName();
            tile.editDisabled = ko.computed(function() { //to do
                return dashboard.type() === "SINGLEPAGE" || dashboard.systemDashboard() || _currentUser !== dashboard.owner();
            });
            tile.params = {
                show: funcShow,
                deleteTextCallback: deleteTextCallback,
    //                reorder: funcReorder,
                tiles: dashboard.tiles,
                tile: tile,
                validator: Builder.isContentLengthValid,
                builder: $b
            };

            tile.tileDisplayClass = ko.computed(function() {
                var display = tile.shouldHide()?"none":"block";
                var tileBorder = tile.toBeOccupied() ? "border: 1px dashed black;": "";
                return tile.cssStyle() + "display:" + display + "; left: 10px;"+tileBorder;
            });
        }
        Builder.registerFunction(initializeTextTileAfterLoad, 'initializeTextTileAfterLoad');

        function initializeTileAfterLoad(mode, dashboard, tile, timeSelectorModel, targetContext, loadImmediately) {
            if (!tile)
                return;

            tile.shouldHide = ko.observable(false);            
            tile.toBeOccupied = ko.observable(false);

            var _currentUser = dfu.getUserName();
            tile.editDisabled = ko.computed(function() { //to do
                return dashboard.type() === "SINGLEPAGE" || dashboard.systemDashboard() || _currentUser !== dashboard.owner();
            });
            tile.widerEnabled = ko.computed(function() {
                return mode.getModeWidth(tile) < mode.MODE_MAX_COLUMNS;
            });
            tile.narrowerEnabled = ko.computed(function() {
                return mode.getModeWidth(tile) > 1;
            });
            tile.shorterEnabled = ko.computed(function() {
                return mode.getModeHeight(tile) > 1;
            });
            tile.maximizeEnabled = ko.computed(function() {
                return !tile.isMaximized();
            });
            tile.restoreEnabled = ko.computed(function() {
                return tile.isMaximized();
            });

            tile.configureEnabled = ko.computed(function() {
                return typeof(tile.configure)==="function";
            });
            tile.tileDisplayClass = ko.computed(function() {
                var css = 'oj-md-'+(mode.getModeWidth(tile)*3) + ' oj-sm-'+(mode.getModeWidth(tile)*3) + ' oj-lg-'+(mode.getModeWidth(tile)*3);
                css += tile.isMaximized() ? ' dbd-tile-maximized ' : '';
                css += tile.shouldHide() ? ' dbd-tile-no-display' : '';
                css += tile.editDisabled() ? ' dbd-tile-edit-disabled' : '';
                css += tile.WIDGET_LINKED_DASHBOARD && tile.WIDGET_LINKED_DASHBOARD() ? ' dbd-tile-linked-dashboard' : '';
//                css += tile.toBeOccupied() ? ' dbd-tile-to-be-occupuied' : '';
                return css;
            });
            tile.linkedDashboard = ko.computed(function() {
                if (tile.WIDGET_LINKED_DASHBOARD && tile.WIDGET_LINKED_DASHBOARD()) {
                    var link = '/emsaasui/emcpdfui/builder.html?dashboardId=' + tile.WIDGET_LINKED_DASHBOARD();
                    targetContext && targetContext.target && (link += '&target='+targetContext.target+'&type='+targetContext.type+'&emsite='+targetContext.emsite);
                    (dashboard.enableTimeRange()==="TRUE" || Builder.isTimeRangeAvailInUrl()===true)&& timeSelectorModel && timeSelectorModel.viewStart() && (link += '&startTime='+timeSelectorModel.viewStart().getTime()+'&endTime='+timeSelectorModel.viewEnd().getTime());
                    return link;
                } else
                    return "#";
            });
            tile.dashboardItemChangeEvent = new Builder.DashboardItemChangeEvent(new Builder.DashboardTimeRangeChange(timeSelectorModel.viewStart(), timeSelectorModel.viewEnd()), targetContext, null, null, dashboard.enableTimeRange());
            /**
             * Integrator needs to override below FUNCTION to respond to DashboardItemChangeEvent
             * e.g.
             * params.tile.onDashboardItemChangeEvent = function(dashboardItemChangeEvent) {...}
             * Note:
             * Integrator will get a parameter: params by which integrator can access tile related properties/method/function
             */
            tile.onDashboardItemChangeEvent = null;

            /**
             * Get value of tile Custom Parameter according to given name. This function only retrieves Custom Parameters.
             * Note:
             * Tile parameter has two types:
             * 1. System Parameter: internal parameter used by DF
             * 2. Custom Parameter: user defined parameter.
             * System parameters and custom parameters are stored in different pool, 
             * so it is possible that one System parameter has the same name as another customer parameter
             * @param {String} name
             * @returns {object} value of parameter. null if not found
             */
            tile.getParameter = function (name) {
                if (name===null || name===undefined){
                    return null;
                }
                if (tile.tileParameters){
                    var parameters = ko.toJS(tile.tileParameters);
                    for (var i=0;i<parameters.length;i++){
                        var tp = parameters[i];
                        if (tp.name===name){
                            return tp;
                        }
                    }
                }
                return null;

    //                if (name in self.customParameters) {
    //                    return self.customParameters[name];
    //                } else {
    //                    return null;
    //                }
            };

            /**
             * Set the value of one Custom Parameter
             * @param {String} name
             * @param {String} value
             * @returns {undefined}
             */
            tile.setParameter = function(name, value){
                if (name===undefined || name===null || value===undefined || value===null){
                    console.error("Invaild value: name=["+name,"] value=["+value+"]");
                    oj.Logger.error("Invaild value: name=["+name,"] value=["+value+"]");
                }else{
                    var found = false;
                    if (tile.tileParameters){
                        for (var i=0;i<tile.tileParameters.length;i++){
                            var tp = tile.tileParameters[i];
                            if (tp.name===name){
                                tp.value = value;
                                found =true;
                            }
                        } 
                        if (!found){
                            tile.tileParameters.push({"name":name,"type":"STRING","value":value,"systemParameter":false});
                        }
                    }else{
                        tile.tileParameters=[];
                        tile.tileParameters.push({"name":name,"type":"STRING","value":value,"systemParameter":false});
                    }
    //                    
    //                    tile.customParameters[name] = value;
                }
            };

            tile.fireDashboardItemChangeEvent = function(dashboardItemChangeEvent){
                tile.dashboard.fireDashboardItemChangeEvent(dashboardItemChangeEvent);
            };

            if (loadImmediately) {
                var assetRoot = dfu.df_util_widget_lookup_assetRootUrl(tile.PROVIDER_NAME(), tile.PROVIDER_VERSION(), tile.PROVIDER_ASSET_ROOT(), true);
                var kocVM = tile.WIDGET_VIEWMODEL();
                if (tile.WIDGET_SOURCE() !== Builder.WIDGET_SOURCE_DASHBOARD_FRAMEWORK)
                    kocVM = assetRoot + kocVM;
                var kocTemplate = tile.WIDGET_TEMPLATE();
                if (tile.WIDGET_SOURCE() !== Builder.WIDGET_SOURCE_DASHBOARD_FRAMEWORK)
                    kocTemplate = assetRoot + kocTemplate;
                Builder.registerComponent(tile.WIDGET_KOC_NAME(), kocVM, kocTemplate);

                if (tile.WIDGET_SOURCE() !== Builder.WIDGET_SOURCE_DASHBOARD_FRAMEWORK){
                    var versionPlus = encodeURIComponent(tile.PROVIDER_VERSION()+'+');
                    var url = Builder.getVisualAnalyzerUrl(tile.PROVIDER_NAME(), versionPlus);
                    if (url){
                        tile.configure = function(){
                            if(dashboard.enableTimeRange() === "FALSE" && Builder.isTimeRangeAvailInUrl() === false) {
                                window.open(url+"?widgetId="+tile.WIDGET_UNIQUE_ID()+"&dashboardId="+dashboard.id()+"&dashboardName="+dashboard.name());
                            }else{
                                var start = timeSelectorModel.viewStart().getTime();
                                var end = timeSelectorModel.viewEnd().getTime();
                                window.open(url+"?widgetId="+tile.WIDGET_UNIQUE_ID()+"&dashboardId="+dashboard.id()+"&dashboardName="+dashboard.name()+"&startTime="+start+"&endTime="+end);
                            }
                        };
                    }
                }         
            }
        }
        Builder.registerFunction(initializeTileAfterLoad, 'initializeTileAfterLoad');
    }
);
