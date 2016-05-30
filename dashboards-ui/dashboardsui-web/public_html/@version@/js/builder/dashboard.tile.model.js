/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'ojs/ojcore',
        'knockout.mapping',
        'dfutil',
        'uiutil',
        'uifwk/js/util/df-util',
        'mobileutil',
        'jquery',
        'builder/builder.core',
        'builder/time.selector.model',
        'builder/editor/editor.component',
        'builder/editor/editor',
        'builder/editor/editor.mode',
        'builder/widget/widget.model',
        'jqueryui',
        'builder/builder.jet.partition'
//        'ckeditor'
    ],
    
    function(ko, oj, km, dfu, uiutil, dfumodel, mbu, $)
    {
        ko.mapping = km;
        var draggingTileClass = 'dbd-tile-in-dragging';
        
        function DashboardTilesViewModel($b) {        
        
            var widgetAreaWidth = 0;
            var widgetAreaContainer = null;

            var dragStartRow = null;
        
            var self = this;
            $b.registerObject(self, 'DashboardTilesViewModel');
            self.isMobileDevice = ((new mbu()).isMobile === true ? 'true' : 'false');
            self.scrollbarWidth = uiutil.getScrollbarWidth();
            
            widgetAreaContainer = $b.findEl('.widget-area');
            
            self.normalMode = new Builder.NormalEditorMode();
            self.tabletMode = new Builder.TabletEditorMode();
            
            self.editor = new Builder.TilesEditor($b, Builder.isSmallMediaQuery() ? self.tabletMode : self.normalMode);
            self.editor.tiles = $b.dashboard.tiles;
            widgetAreaWidth = widgetAreaContainer.width();
            
            self.previousDragCell = null;
                        
            self.dashboard = $b.dashboard;
            self.loginUser = ko.observable(dfu.getUserName());
            var dfu_model = new dfumodel(dfu.getUserName(), dfu.getTenantName());

            self.targets = ko.observable(null);
//            self.targetsFromParam = dfu_model.getUrlParam("targets");
//            self.targets = ko.observable(null);
//            if(self.targetsFromParam) {
//                self.targets(JSON.parse(decodeURI(self.targetsFromParam)));
////                console.log("***");
////                console.log(JSON.stringify(self.targets()));
//            }
//            
//            //decompress targets obtained from url
//            self.targetszFromParam = dfu_model.getUrlParam("targetsz");
//            if(self.targetszFromParam) {
//                var deCompressedTargets = self.targetszFromParam;
//                require(["emsaasui/uifwk/libs/emcstgtsel/js/tgtsel/api/TargetSelectorUtils"], function(TargetSelectorUtils) {
//                    if(TargetSelectorUtils.decompress) {
//                        deCompressedTargets = TargetSelectorUtils.decompress(self.targetszFromParam);
//                    }
//                    self.targets(JSON.parse(decodeURI(deCompressedTargets)));
//                    console.log("***"+self.targets());
//                });
//            }

            self.timeSelectorModel = new Builder.TimeSelectorModel();
            self.tilesView = $b.getDashboardTilesView();
            self.isOnePageType = (self.dashboard.type() === Builder.SINGLEPAGE_TYPE);
//            self.linkName = ko.observable();
//            self.linkUrl = ko.observable();
            self.isCreator =  dfu.getUserName() === self.dashboard.owner();
            
            self.disableTilesOperateMenu = ko.observable(self.isOnePageType);
            self.showTimeRange = ko.observable(false);
            self.showWidgetTitle = ko.observable(true);
            self.showRightPanelToggler = ko.observable(true);
            
            self.resizingTile = ko.observable();
            self.resizingOptions = ko.observable().extend({ rateLimit: { timeout: 100, method: "always" } });
            self.resizingMonitor = ko.computed(function () {
                ko.mapping.fromJS(self.resizingOptions());
                if (self.resizingTile() && self.resizingOptions() && self.resizingOptions().mode) {
                    var hasChanged = self.editor.resizeTile(self.resizingTile(), self.resizingOptions());
                    if (hasChanged) {
                        self.notifyTileChange(self.resizingTile(), new Builder.TileChange("DRAG_RESIZE")); // todo
                        self.show();
                    }
                   
                }
            });

            self.isEmpty = function() {
                return !self.editor.tiles() || self.editor.tiles().length === 0;
            };
            
            self.isDefaultTileExist = function() {
                for(var i in self.dashboard.tiles()){
                    if(self.dashboard.tiles()[i].type() === "DEFAULT") {
                        return true;
                    }
                }
                return false;
            };
            
            self.openAddWidgetDialog = function() {
                $('#dashboardBuilderAddWidgetDialog').ojDialog('open');
            };
            
            self.rightPanelShown = ko.observable(self.isEmpty());
            
            self.rightPanelIcon = ko.observable(function(){
                var icon="none";
                if (self.isEmpty()){
                    icon="wrench";
                }
                return icon;  
            }());  
            
            self.toggleRightPanel = function(data,event,target) {
                    var clickedIcon;
                if(!event){
                    if("edit" === target && !self.rightPanelShown()){
                        clickedIcon = "pencil";
                        self.rightPanelIcon(clickedIcon);
                        $b.getRightPanelModel().toggleLeftPanel();
                        self.rightPanelShown(!self.rightPanelShown());
                    }
                }else{
                    if ($(event.currentTarget).hasClass('rightpanel-pencil')) {
                        clickedIcon = "pencil";
                    } else if ($(event.currentTarget).hasClass('rightpanel-wrench')) {
                        clickedIcon = "wrench";
                    }

                    if (self.rightPanelShown() && clickedIcon !== self.rightPanelIcon()) {
                        self.rightPanelIcon(clickedIcon);
                    }
                    else if (self.rightPanelShown()) {
                        self.rightPanelIcon("none");
                        $b.getRightPanelModel().toggleLeftPanel();
                        self.rightPanelShown(!self.rightPanelShown());
                    } else {
                        self.rightPanelIcon(clickedIcon);
                        $b.getRightPanelModel().toggleLeftPanel();
                        self.rightPanelShown(!self.rightPanelShown());
                    }              
                }
            };
            
//            self.appendTextTile = function () {
//                var newTextTile;
//                var widget = Builder.createTextWidget(self.editor.mode.MODE_MAX_COLUMNS);
//                
//                var newTextTile = new Builder.DashboardTextTile($b, widget, self.show, self.editor.deleteTile);
//                var textTileCell = new Builder.Cell(0, 0);
//                newTextTile.row(textTileCell.row);
//                newTextTile.column(textTileCell.column);
//                self.editor.tiles.unshift(newTextTile);
//                self.editor.tilesReorder(newTextTile);
//                self.tilesView.enableDraggable(newTextTile);
//                self.show();
//            };
            
            self.appendNewTile = function(name, description, width, height, widget) {
                if (widget) {
                    var newTile = self.editor.createNewTile(name, description, width, height, widget, self.timeSelectorModel, self.targets, true);
                    if (newTile){
                       self.editor.tiles.push(newTile);
                       self.show();
                       $b.triggerEvent($b.EVENT_TILE_ADDED, null, newTile);
                       self.triggerTileTimeControlSupportEvent((newTile.type() === 'DEFAULT' && newTile.WIDGET_SUPPORT_TIME_CONTROL())?true:null);
                    }
                }
                else {
                    oj.Logger.error("Null widget passed to a tile");
                }
            };
            
            self.initialize = function() {
                $b.addNewWidgetDraggingListener(self.onNewWidgetDragging);
                $b.addNewWidgetStopDraggingListener(self.onNewWidgetStopDragging);
//                $b.addNewTextDraggingListener(self.onNewTextDragging);
//                $b.addNewTextStopDraggingListener(self.onNewTextStopDragging);
//                $b.addNewLinkDraggingListener(self.onNewLinkDragging);
//                $b.addNewLinkStopDraggingListener(self.onNewLinkStopDragging);
                $b.addBuilderResizeListener(self.onBuilderResize);
                $b.addEventListener($b.EVENT_POST_DOCUMENT_SHOW, self.postDocumentShow);
//                $b.addEventListener($b.EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL, self.dashboardTileSupportTimeControlHandler);
//                $b.addEventListener($b.EVENT_DSB_ENABLE_TIMERANGE_CHANGED, self.dashboardTimeRangeChangedHandler);
                $b.addEventListener($b.EVENT_ENTER_NORMAL_MODE, self.enterNormalModeHandler);
                $b.addEventListener($b.EVENT_ENTER_TABLET_MODE, self.enterTabletModeHandler);
                $b.addEventListener($b.EVENT_TILE_MAXIMIZED, self.dashboardMaximizedHandler);
                $b.addEventListener($b.EVENT_TILE_RESTORED, self.dashboardRestoredHandler);
                self.initializeTiles();
            };
            
            self.onBuilderResize = function(width, height, leftWidth, topHeight) {
                widgetAreaWidth = Math.min(widgetAreaContainer.width(), $b.findEl(".tiles-col-container").width()-25);
                window.DEV_MODE && console.debug('widget area width is ' + widgetAreaWidth);
                self.show();
            };
            
            self.showPullRightBtn = function(clientGuid, data, event) {
                $("#tile"+clientGuid+" .dbd-btn-group").css("display", "inline-block");
                $("#tile"+clientGuid+" .dbd-btn-editor").css("display", "flex");
                $("#tile"+clientGuid+" .dbd-btn-maxminToggle").css("display", "flex");
            };
            
            self.hidePullRightBtn = function (clientGuid, data, event) {
                if ($("#tileMenu" + clientGuid).css("display") === "none") {
                    $("#tile" + clientGuid + " .dbd-btn-group").css("display", "none");
                }
                $("#tile" + clientGuid + " .dbd-btn-editor").css("display", "none");
                $("#tile" + clientGuid + " .dbd-btn-maxminToggle").css("display", "none");
            };
            self.openInDataExplorer = function (event, ui) {
                var tile = ko.dataFor(ui.currentTarget);
                self.editor.configure(tile);
            };
            
            self.maxMinToggle = function (event, ui) {
                var tile = ko.dataFor(ui.currentTarget);
                if (event.maximizeEnabled()) {
                    self.maximize(tile);
                    self.notifyTileChange(tile, new Builder.TileChange("POST_MAXIMIZE"));
                    $b.triggerEvent($b.EVENT_TILE_MAXIMIZED, null, tile);
                } else {
                    self.restore(tile);
                    self.notifyTileChange(tile, new Builder.TileChange("POST_RESTORE"));
                    $b.triggerEvent($b.EVENT_TILE_RESTORED, null, tile);
                }
            };
            
            self.menuItemSelect = function (event, ui) {
                var tile = ko.dataFor(ui.item[0]);
                if (!tile) {
                    oj.Logger.error("Error: could not find tile from the ui data");
                    return;
                }
                switch (ui.item.data("option")) {
                    case "showhide-title":
                        self.editor.showHideTitle(tile);
                        self.show();
                        self.notifyTileChange(tile, new Builder.TileChange("POST_HIDE_TITLE"));
                        break;                      
                    case "remove":
                        self.editor.deleteTile(tile);
                        self.show();
                        self.notifyTileChange(tile, new Builder.TileChange("POST_DELETE"));
                        $b.triggerEvent($b.EVENT_TILE_RESTORED, 'triggerred by tile deletion', tile);
                        $b.triggerEvent($b.EVENT_TILE_DELETED, null, tile);
                        self.triggerTileTimeControlSupportEvent();
                        break;
                    case "wider":
                        self.editor.broadenTile(tile);
                        self.show();
                        self.notifyTileChange(tile, new Builder.TileChange("POST_WIDER"));
                        break;
                    case "narrower":
                        self.editor.narrowTile(tile);
                        self.show();
                        self.notifyTileChange(tile, new Builder.TileChange("POST_NARROWER"));
                        break;
                    case "taller":
                        self.editor.tallerTile(tile);
                        self.show();
                        self.notifyTileChange(tile, new Builder.TileChange("POST_TALLER"));
                        break;
                    case "shorter":
                        self.editor.shorterTile(tile);
                        self.show();
                        self.notifyTileChange(tile, new Builder.TileChange("POST_SHORTER"));
                        break;                
                }
                
                $b.triggerEvent($b.EVENT_TILE_RESIZED, null, tile);
            };

//           self.openEditTileLinkDialog = function(tile) { 
//               self.tileToEdit = ko.observable(tile);
//               self.linkName(tile.linkText());
//               self.linkUrl(tile.linkUrl());
//               $("#tilesLinkEditorDialog").ojDialog("open");
//           };
//           
//           self.closeEditTileLinkDialog = function() {
//               if(self.tileToEdit && self.tileToEdit()) {
//                   var tile = self.tileToEdit();
//                   if(!tile.linkText() || !tile.linkUrl()) {
//                       tile.linkText(null);
//                       tile.linkUrl(null);
//                   }
//               }
//           };
//           
//           self.deleteTileLink = function(tile) {
//               tile.linkText(null);
//               tile.linkUrl(null);
//           };
           
//           self.linkNameValidated = true;
//           self.linkNameValidator = {
//               'validate': function(value){
//                   if(Builder.isContentLengthValid(value, Builder.LINK_NAME_MAX_LENGTH)) {
//                       self.linkNameValidated = true;
//                       return true;
//                   }else {
//                      self.linkNameValidated = false; 
//                      throw new oj.ValidatorError(oj.Translations.getTranslatedString("DBS_BUILDER_EDIT_WIDGET_LINK_NAME_VALIDATE_ERROR"));
//                   }
//               }
//           };
           
//           self.linkURLValidated = true;
//           self.linkURLValidator = {
//               'validate': function(value) {
//                    if(isURL(value)) {                       
//                        if(Builder.isContentLengthValid(value, Builder.LINK_URL_MAX_LENGTH)) {
//                            self.linkURLValidated = true;
//                        }else {
//                            self.linkURLValidated = false;
//                            throw new oj.ValidatorError(oj.Translations.getTranslatedString("DBS_BUILDER_EDIT_WIDGET_LINK_URL_LENGTH_VALIDATE_ERROR"));
//                        }
//                    }else {
//                        self.linkURLValidated = false;
//                        throw new oj.ValidatorError(oj.Translations.getTranslatedString("DBS_BUILDER_EDIT_WIDGET_LINK_URL_VALIDATE_ERROR"));
//                    }
//               }
//           };
           
//           self.editTileLinkConfirmed = function() {
//               if(!self.linkName() || !self.linkUrl() || !self.linkNameValidated || !self.linkURLValidated) {
//                   $("#tilesLinkEditorDialog").ojDialog("close");
//                   return false;
//               }
//               if(self.tileToEdit && self.tileToEdit()) {
//                  var tile = self.tileToEdit();
//                  tile.linkText(self.linkName());
//                  tile.linkUrl(self.linkUrl());
//               }               
//               self.linkName(null);
//               self.linkUrl(null);
//               $("#tilesLinkEditorDialog").ojDialog("close");
//           };
           
           self.initializeTiles = function() {
                if(self.editor.tiles && self.editor.tiles()) {
                    for(var i=0; i< self.editor.tiles().length; i++) {
                        var tile = self.editor.tiles()[i];
                        self.editor.tilesGrid.registerTileToGrid(tile);
                    }
                }
            };
            
            self.calculateTilesRowHeight = function() {
                var tilesRow = $b.findEl('.widget-area');
                var tilesRowSpace = parseInt(tilesRow.css('margin-top'), 0) +  
                        parseInt(tilesRow.css('margin-bottom'), 0) + 
                        parseInt(tilesRow.css('padding-top'), 0) + 
                        parseInt(tilesRow.css('padding-bottom'), 0);
                var tileSpace = parseInt($('.dbd-tile-maximized').css('margin-bottom'), 0) + 
                        parseInt($('.dbd-tile-maximized').css('padding-bottom'), 0) + 
                        parseInt($('.dbd-tile-maximized').css('padding-top'), 0);
                return $(window).height() - $('#headerWrapper').outerHeight() - 
                       $b.findEl('.head-bar-container').outerHeight() - $('#global-time-slider').outerHeight() - 
                       (isNaN(tilesRowSpace) ? 0 : tilesRowSpace) - (isNaN(tileSpace) ? 0 : tileSpace);
            };
            
            self.showMaximizedTile = function(tile, width, height) {
                if(!tile) {
                    return;
                }
                var columnWidth = widgetAreaWidth / self.editor.mode.MODE_MAX_COLUMNS;
                var baseLeft = widgetAreaContainer.position().left;
                var top = widgetAreaContainer.position().top;
                var tileHeight = height*Builder.DEFAULT_HEIGHT;
                tile.cssWidth(width*columnWidth);
                tile.cssHeight(tileHeight);
                tile.left(baseLeft);
                tile.top(top);
                self.tilesView.disableDraggable();
                $b.findEl('.tiles-wrapper').height(tileHeight);
            };
            
            self.maximize = function(tile) {
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var eachTile = self.editor.tiles()[i];
                    if (eachTile !== tile)
                        eachTile.shouldHide(true);
                }
                tile.shouldHide(false);
                tile.isMaximized(true);
                var maximizedTileHeight = self.calculateTilesRowHeight()/Builder.DEFAULT_HEIGHT;
                if(maximizedTileHeight === 0) {
                    maximizedTileHeight = 1;
                }
                $(window).scrollTop(0);
                self.showMaximizedTile(tile, self.editor.mode.MODE_MAX_COLUMNS, maximizedTileHeight);
            };
            
            self.initializeMaximization = function() {
                var maximized = self.editor.getMaximizedTile();
                if (maximized) {
                    self.maximize(maximized);
                    $b.triggerEvent($b.EVENT_TILE_MAXIMIZED, null, maximized);
                }
            };
            
            self.restore = function(tile) {                
                tile.isMaximized(false);
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var eachTile = self.editor.tiles()[i];
                    eachTile.shouldHide(false);
                }
                self.tilesView.enableDraggable();
                self.show();
            };
            
            self.notifyTileChange = function(tile, change){
                var tChange = null;
                if (change instanceof Builder.TileChange){
                    tChange = change;
                }
                var dashboardItemChangeEvent = new Builder.DashboardItemChangeEvent(new Builder.DashboardTimeRangeChange(self.timeSelectorModel.viewStart(),self.timeSelectorModel.viewEnd()), self.targets, null,tChange, self.dashboard.enableTimeRange(), self.dashboard.enableEntityFilter());
                Builder.fireDashboardItemChangeEventTo(tile, dashboardItemChangeEvent); 
            };
            
            self.refreshThisWidget = function(tile) {
                self.notifyTileChange(tile, new Builder.TileChange("PRE_REFRESH"));
            };
                        
            self.show = function() {
                self.showTiles();
                $('.dbd-widget').on('dragstart', self.handleStartDragging);
                $('.dbd-widget').on('drag', self.handleOnDragging);
                $('.dbd-widget').on('dragstop', self.handleStopDragging);
                
                $('.dbd-resize-handler').on('mousedown', function (event) {
                    var targetHandler = $(event.currentTarget),resizeMode = null;
                    if ($(targetHandler).hasClass('dbd-resize-handler-right')) {
                        resizeMode = 'x';
                    } else if ($(targetHandler).hasClass('dbd-resize-handler-bottom')) {
                        resizeMode = 'y';
                    } else if($(targetHandler).hasClass('dbd-resize-handler-right-bottom')){
                        resizeMode = 'xy';
                    }
                    
                    var isResizing =  resizeMode !== null;
                    if(isResizing) {
                        self.resizingTile(ko.dataFor(targetHandler.closest('.dbd-widget')[0]));
                        self.resizingOptions({mode:resizeMode});
                    }
                    self.tilesView.disableDraggable();

                });

                $('#globalBody').on('mousemove', function (event) {
                   if (self.resizingOptions()) {
                        if(self.resizingOptions().mode === 'x') {
                            $(this).css('cursor','ew-resize');
                        }else if(self.resizingOptions().mode === 'y'){
                            $(this).css('cursor','ns-resize');
                        }else if(self.resizingOptions().mode === 'xy'){
                            $(this).css('cursor','se-resize');
                        }
                        var clonedTarget = $.extend(self.resizingOptions(), {left: event.clientX, top: event.clientY});
                        self.resizingOptions(clonedTarget);
                    }
                }).on('mouseup', function (event) {
                        self.resizingTile(null);
                        self.resizingOptions(null);
                        $(this).css('cursor','default');
                        self.tilesView.enableDraggable();
                });;
            };
            
            self.isDraggingCellChanged = function(pos) {
                if (!self.previousDragCell)
                    return true;
                return pos.row !== self.previousDragCell.row || pos.column !== self.previousDragCell.column;
            };
            
            self.getDisplayWidthForTile = function(width) {
                var columnWidth = widgetAreaWidth / self.editor.mode.MODE_MAX_COLUMNS;
                return width * columnWidth;
            };
            
            self.getDisplayHeightForTile = function(height) {
                return height * Builder.DEFAULT_HEIGHT;
            };
            
            self.getDisplayLeftForTile = function(column) {
                var baseLeft = widgetAreaContainer.position().left;
                var columnWidth = widgetAreaWidth / self.editor.mode.MODE_MAX_COLUMNS;
                return baseLeft + column * columnWidth;
            };
            
            self.getDisplayTopForTile = function(row) {
                var top = widgetAreaContainer.position().top;
                for (var i = 0; i < row; i++) {
                    top += self.editor.getRowHeight(i);
                }
                return top;
            };
                        
            self.getDisplaySizeForTiles = function() {
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var tile = self.editor.tiles()[i];
                    tile.cssWidth(self.getDisplayWidthForTile(self.editor.mode.getModeWidth(tile)));
                    tile.cssHeight(self.getDisplayHeightForTile(self.editor.mode.getModeHeight(tile)));
                }
            };
            
            self.getDisplayPositionForTiles = function() {
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var tile = self.editor.tiles()[i];
                    tile.left(self.getDisplayLeftForTile(self.editor.mode.getModeColumn(tile)));
                    tile.top(self.getDisplayTopForTile(self.editor.mode.getModeRow(tile)));
                }
            };
            
            self.showTiles = function() {
                if(!(self.editor.tiles && self.editor.tiles())) {
                    return;
                }
                var tile;
                for (var i=0; i< self.editor.tiles().length; i++) {
                    tile = self.editor.tiles()[i];
                    if(tile.isMaximized()) {
                        self.maximize(tile);
                        return;
                    }
                }
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var tile = self.editor.tiles()[i];
//                    if(tile.type() === "TEXT_WIDGET") {
//                       tile.shouldHide(true); 
//                    }                    
                    tile.cssWidth(self.getDisplayWidthForTile(self.editor.mode.getModeWidth(tile)));
                    tile.cssHeight(self.getDisplayHeightForTile(self.editor.mode.getModeHeight(tile)));
                    tile.left(self.getDisplayLeftForTile(self.editor.mode.getModeColumn(tile)));
                    tile.top(self.getDisplayTopForTile(self.editor.mode.getModeRow(tile)));
                    tile.shouldHide(false);
//                    if (tile.type() === 'TEXT_WIDGET') {
//                        var displayHeight = tile.displayHeight();
//                        if (!displayHeight)
//                            self.detectTextTileRender(tile);
//                        self.editor.setRowHeight(self.editor.mode.getModeRow(tile), displayHeight);
//                    }
//                    else {
                        for (var j = 0; j < self.editor.mode.getModeHeight(tile); j++) {
                            self.editor.setRowHeight(self.editor.mode.getModeRow(tile) + j);
                        }
//                    }
                }
                self.tilesView.enableDraggable();
                var height = self.editor.tilesGrid.getHeight();
                $b.findEl('.tiles-wrapper').height(height);
            };

//            self.detectTextTileRender = function(textTile) {
//                if (!textTile)
//                    return;
//                var elem = self.tilesView.getTileElement(textTile);
//                var lastHeight = elem.css('height');
//                
//                function checkForChanges() {
//                    if (elem.css('height') !== lastHeight) {
//                        self.reRender();
//                        return;
//                    }
//                    setTimeout(checkForChanges, 100);
//                };
//                checkForChanges();
//            };
            
            // trigger an event to indicates if there is tile(s) supporting time control or not
            self.triggerTileTimeControlSupportEvent = function(exists) {
                if (exists === true || exists === false) {
                    $b.triggerEvent($b.EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL, null, exists);
                    return;
                }
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var tile = self.editor.tiles()[i];
                    if (tile && tile.type() === 'DEFAULT' && tile.WIDGET_SUPPORT_TIME_CONTROL()) {
                        $b.triggerEvent($b.EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL, null, true);
                        return;
                    }
                }
                $b.triggerEvent($b.EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL, null, false);
            };
            
            self.reRender = function() {
                self.tilesView.disableMovingTransition();
                self.show();
                self.tilesView.enableMovingTransition();
            };
            var startTime, tilesToBeOccupied, startX, startY;
            self.handleStartDragging = function(event, ui) {
                if(!ui) {
                    console.log(ui);
                    return;
                }
                startTime = new Date().getTime();
                var tile = ko.dataFor(ui.helper[0]);                
                dragStartRow = self.editor.mode.getModeRow(tile);
                startX = tile.left();
                startY = tile.top();
                self.previousDragCell = new Builder.Cell(self.editor.mode.getModeRow(tile), self.editor.mode.getModeColumn(tile));
                if (!$(ui.helper).hasClass(draggingTileClass)) {
                    $(ui.helper).addClass(draggingTileClass);
                }
            };
            
            self.handleOnDragging = function(event, ui) {
                if(!ui) {
                    return;
                }
                var tile = ko.dataFor(ui.helper[0]);
//                self.editor.tilesGrid.unregisterTileInGrid(tile);
                var originalRow = self.editor.mode.getModeRow(tile);
                var originalCol = self.editor.mode.getModeColumn(tile);
                
                var dragStartRow = self.editor.mode.getModeRow(tile);
                var cell = self.editor.getCellFromPosition(widgetAreaWidth, ui.helper.position());
                if(tile.content) {
                    cell.column = 0;
                }

                $b.findEl('.tile-dragging-placeholder').css({
                    left: tile.left() - 5,
                    top: tile.top() - 5,
                    width: ui.helper.width() - 10,
                    height: ui.helper.height() - 10
                }).show();

                var tileInCell = self.editor.tilesGrid.tileGrid[cell.row] ? self.editor.tilesGrid.tileGrid[cell.row][cell.column] : null;
                if((self.previousDragCell) && cell.column+self.editor.mode.getModeWidth(tile) <= self.editor.mode.MODE_MAX_COLUMNS && 
                        (!tileInCell || (tileInCell && tileInCell !== tile && self.editor.mode.getModeRow(tileInCell) === cell.row && self.editor.mode.getModeColumn(tileInCell) === cell.column))) {                                      
                    var cellsOccupiedByTile = self.editor.getCellsOccupied(cell.row, cell.column, self.editor.mode.getModeWidth(tile), self.editor.mode.getModeHeight(tile));
                    var tilesUnderCell = self.editor.getTilesUnder(cellsOccupiedByTile, tile);
                    var tilesBelowOriginalCell = self.editor.getTilesBelow(tile);
                    self.editor.draggingTile = tile;
                    var rowDiff, iTile;
                    for(var i in tilesUnderCell) {
                        iTile = tilesUnderCell[i];
                        rowDiff = cell.row - self.editor.mode.getModeRow(iTile) + self.editor.mode.getModeHeight(tile);
                        self.editor.moveTileDown(iTile, rowDiff);
                    }
                    self.editor.draggingTile = null;
                    self.editor.updateTilePosition(tile, cell.row, cell.column);
                    
                    rowDiff = Math.abs(cell.row - dragStartRow);
                    for(i in tilesBelowOriginalCell) {
                        iTile = tilesBelowOriginalCell[i];
                        rowDiff = (rowDiff===0) ? self.editor.mode.getModeHeight(tile) : rowDiff;
                        self.editor.moveTileUp(iTile, rowDiff);
                    }
                    
                    self.editor.tilesReorder();
                    self.showTiles();
                    $(ui.helper).css("opacity", 0.6);
                
                    if(originalRow !== cell.row || originalCol !== cell.column) {
                        $b.findEl('.tile-dragging-placeholder').hide();
                        $b.findEl('.tile-dragging-placeholder').css({
                            left: tile.left() - 5,
                            top: tile.top() - 5,
                            width: ui.helper.width() - 10,
                            height: ui.helper.height() - 10
                        }).show();
                    }
                }
            };
            
            self.handleStopDragging = function(event, ui) {
                if(!ui) {
                    return;
                }
                if (!self.previousDragCell)
                    return;
                var tile = ko.dataFor(ui.helper[0]);
//                var dragStartRow = tile.row();
                var cell = self.editor.getCellFromPosition(widgetAreaWidth, ui.helper.position());
                if(tile.content) {
                    cell.column = 0;
                }
                ui.helper.css({left:tile.left(), top:tile.top()});

                self.editor.draggingTile = null;
                self.editor.tilesReorder();
                self.showTiles();
                $(ui.helper).css("opacity", 1);
                
                $b.findEl('.tile-dragging-placeholder').hide();
                if ($(ui.helper).hasClass(draggingTileClass)) {
                    $(ui.helper).removeClass(draggingTileClass);
                }
                dragStartRow = null;
                self.previousDragCell = null;
//                tilesToBeOccupied && self.editor.unhighlightTiles(tilesToBeOccupied);
                $b.triggerEvent($b.EVENT_TILE_MOVE_STOPED, null);
            };
           
            self.onNewWidgetDragging = function(e, u) {
                var tcc = $b.findEl(".tiles-col-container");
                var rpt = $b.findEl(".right-panel-toggler");
                var tile = null;
                var pos = {top: u.helper.offset().top - $b.findEl('.tiles-wrapper').offset().top, left: u.helper.offset().left - $b.findEl('.tiles-wrapper').offset().left};

                tile = u.helper.tile;
                //use newly created tile to simulate helper attached to mouse
                    if(tile) {
                        tile.left(pos.left-tile.cssWidth()/2);
                        tile.top(pos.top-15);
                        $('#tile'+tile.clientGuid).css("opacity", 0.6);
                        if(!$('#tile'+tile.clientGuid).hasClass(draggingTileClass)) {
                            $('#tile'+tile.clientGuid).addClass(draggingTileClass);
                        }
                    }
                if (e.clientY <= tcc.offset().top || e.clientY >= tcc.offset().top + tcc.height() || e.clientX >= rpt.offset().left) {
                    if (self.isEmpty()) {
                        $b.findEl('.tile-dragging-placeholder').hide();
                        $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new (default) widget dragging out of edit area", false);
                    }
                    $b.findEl('.tile-dragging-placeholder').hide();
                    return;
                }else {
                    if (self.isEmpty()) $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new (default) widget dragging into edit area (stopped dragging)", true);
                    var cell = self.editor.getCellFromPosition(widgetAreaWidth, pos); 
                    if (!cell) return;
                    if(!self.previousDragCell) self.previousDragCell = cell;
                    if(self.previousDragCell && self.previousDragCell.row === cell.row && self.previousDragCell.column === cell.column) {
                        return;
                    }
                    var widget = ko.mapping.toJS(ko.dataFor(u.helper[0]));
                    var width = Builder.getTileDefaultWidth(widget, self.editor.mode), height = Builder.getTileDefaultHeight(widget, self.editor.mode);
                    if(cell.column>self.editor.mode.MODE_MAX_COLUMNS-width) {
                        cell.column = self.editor.mode.MODE_MAX_COLUMNS-width;
                    }
                    if (!tile) {
                        tile = self.editor.createNewTile(widget.WIDGET_NAME, null, width, height, widget, self.timeSelectorModel, self.targets, true);                        
                        Builder.initializeTileAfterLoad(self.editor.mode, self.dashboard, tile, self.timeSelectorModel, self.targets, true);
                        u.helper.tile = tile;
                        self.editor.tiles.push(tile);
                        $b.triggerEvent($b.EVENT_TILE_ADDED, null, tile);
                    }
                    
                    var tileInCell = self.editor.tilesGrid.tileGrid[cell.row] ? self.editor.tilesGrid.tileGrid[cell.row][cell.column] :null;
                    if(tileInCell && self.editor.mode.getModeRow(tileInCell) !== cell.row) {
                        return;
                    }
                    var cells = self.editor.getCellsOccupied(cell.row, cell.column, width, height);
                    var tilesToMove = self.editor.getTilesUnder(cells, tile);
                    for(var i in tilesToMove) {
                        var rowDiff = cell.row-self.editor.mode.getModeRow(tilesToMove[i])+self.editor.mode.getModeHeight(tile);
                        self.editor.moveTileDown(tilesToMove[i], rowDiff);
                    }
                    self.editor.updateTilePosition(tile, cell.row, cell.column);

                    self.editor.tilesReorder();
                    self.show();
                    
                    //restore simulated helper after show()
                    tile.left(pos.left-tile.cssWidth()/2);
                    tile.top(pos.top-15);
                    $('#tile'+tile.clientGuid).css("opacity", 0.6);
                    if(!$('#tile'+tile.clientGuid).hasClass(draggingTileClass)) {
                        $('#tile'+tile.clientGuid).addClass(draggingTileClass);
                    }
                    
                    //move show() out of setTimeout to fix the issue that $b.findEl('.tile-dragging-placeholder').hide();doesn't work in onNewWidgetStopDragging
                    $b.findEl('.tile-dragging-placeholder').show();
                    setTimeout(function() {
                        $b.findEl('.tile-dragging-placeholder').css({
                            left: self.getDisplayLeftForTile(self.editor.mode.getModeColumn(tile)) - 5,
                            top: self.getDisplayTopForTile(self.editor.mode.getModeRow(tile)) - 5,
                            width: self.getDisplayWidthForTile(width) - 10,
                            height: self.getDisplayHeightForTile(height) - 10
                        });
                    }, 200);
                    self.previousDragCell = cell;
                    self.editor.draggingTile = tile;
                }
                
            };
            
            self.onNewWidgetStopDragging = function(e, u) {
                var tcc = $b.findEl(".tiles-col-container");
                var rpt = $b.findEl(".right-panel-toggler");
                var tile = null; 
                
                if(u.helper.tile) {
                    if($('#tile'+u.helper.tile.clientGuid).hasClass(draggingTileClass)) {
                        $('#tile'+u.helper.tile.clientGuid).removeClass(draggingTileClass);
                    }
                }
                if (e.clientY <= tcc.offset().top || e.clientY >= tcc.offset().top + tcc.height() || e.clientX >= rpt.offset().left) {
                    if (self.isEmpty()) {
                        $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new (default) widget dragging out of edit area (stopped dragging)", false);
                    }
                    if (u.helper.tile) {
                        var idx = self.editor.tiles.indexOf(u.helper.tile);
                        self.editor.tilesGrid.unregisterTileInGrid(u.helper.tile);
                        self.editor.tiles.splice(idx, 1);
                    }
                }
                self.editor.tilesReorder();
                self.show();
                
                $b.findEl('.tile-dragging-placeholder').hide();              
                self.editor.draggingTile = null;
                u.helper.tile = null;
                self.previousDragCell = null;
                tile && tile.WIDGET_SUPPORT_TIME_CONTROL && self.triggerTileTimeControlSupportEvent(tile.WIDGET_SUPPORT_TIME_CONTROL()?true:null);
            };
            
//            self.onNewTextDragging = function(e, u) {
//                var tcc = $("#tiles-col-container");
//                if (e.clientY <= tcc.offset().top || e.clientX <= tcc.offset().left || e.clientY >= tcc.offset().top + tcc.height() || e.clientX >= tcc.offset().left + tcc.width()) {
//                    if (self.isEmpty()) {
//                        $b.findEl('.tile-dragging-placeholder').hide();
//                        $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new text widget dragging out of edit area", false);
//                    }
//                    return;
//                }
//                if (self.isEmpty()) $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new text widget dragging into edit area", true);
//                var pos = {top: u.helper.offset().top - $b.findEl('.tiles-wrapper').offset().top, left: u.helper.offset().left - $b.findEl('.tiles-wrapper').offset().left};
//                var cell = self.editor.getCellFromPosition(widgetAreaWidth, pos); 
//                if (!cell) return;
//                cell.column = 0;
//
//                $b.findEl('.tile-dragging-placeholder').hide();
//                tilesToBeOccupied && self.editor.unhighlightTiles(tilesToBeOccupied);
//                tilesToBeOccupied = self.editor.getTilesToBeOccupied(cell, 8, 1);
//                tilesToBeOccupied && self.editor.highlightTiles(tilesToBeOccupied);
//                self.previousDragCell = cell;
//            };
//            
//            self.onNewTextStopDragging = function(e, u) {
//                var tcc = $("#tiles-col-container");
//                var tile = null;
//                if (e.clientY <= tcc.offset().top || e.clientX <= tcc.offset().left || e.clientY >= tcc.offset().top + tcc.height() || e.clientX >= tcc.offset().left + tcc.width()) {
//                    if (self.isEmpty()) {
//                        $b.findEl('.tile-dragging-placeholder').hide();
//                        $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new (text) widget dragging out of edit area (stopped dragging)", false);
//                    }
//                    if (u.helper.tile) {
//                        var idx = self.editor.tiles.indexOf(u.helper.tile);
//                        self.editor.tiles.splice(idx, 1);
//                    }
//                }
//                else {
//                    if (self.isEmpty()) $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new (text) widget dragging out of edit area (stopped dragging)", true);
//                    var pos = {top: u.helper.offset().top - $b.findEl('.tiles-wrapper').offset().top, left: u.helper.offset().left - $b.findEl('.tiles-wrapper').offset().left};
//                    var cell = self.editor.getCellFromPosition(widgetAreaWidth, pos); 
//                    if (!cell) return;
//                    cell.column = 0;
//                    tile = u.helper.tile;
//                    if (!u.helper.tile) {
//                        tile = new Builder.DashboardTextTile(self.editor.mode, $b, Builder.createTextWidget(self.editor.mode.MODE_MAX_COLUMNS), self.show, self.editor.deleteTile);
//                        u.helper.tile = tile;
//                        self.editor.tiles.push(tile);
//                    }
//                    if (!self.previousDragCell)
//                        return;
//                    
//                    var tileInCell = self.editor.tilesGrid.tileGrid[cell.row] ? self.editor.tilesGrid.tileGrid[cell.row][cell.column] : null;
//                    if(tileInCell && tileInCell.row() !== cell.row) {
//                        return;
//                    }
//                    var cells = self.editor.getCellsOccupied(cell.row, cell.column, 8, 1);
//                    var tilesToMove = self.editor.getTilesUnder(cells, tile);
//                    for(var i in tilesToMove) {
//                        var rowDiff = cell.row-tilesToMove[i].row()+tile.height();
//                        self.editor.moveTileDown(tilesToMove[i], rowDiff);
//                    }
//                    self.editor.updateTilePosition(tile, cell.row, cell.column);
//
//                    self.editor.tilesReorder();
//                    self.show();
//                }                
//                
//                $b.findEl('.tile-dragging-placeholder').hide();
//                self.previousDragCell = null;
//                tilesToBeOccupied && self.editor.unhighlightTiles(tilesToBeOccupied);
//                if (tile) {
//                    $(u.helper).css({left: tile.left(), top: tile.top()});
//                }
//            };
            
            self.dashboardTileSupportTimeControlHandler = function(exists) {
                window.DEV_MODE && console.debug('Received event EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL with value of ' + exists + '. ' + (exists?'Show':'Hide') + ' date time picker accordingly (self.dashboard.enableTimeRange() value is: ' + self.dashboard.enableTimeRange() + ')');
                self.showTimeRange(self.dashboard.enableTimeRange() !== 'FALSE' && exists);
            };
            
            self.dashboardTimeRangeChangedHandler = function() {
                self.showTimeRange(self.dashboard.enableTimeRange() === 'TRUE');
            };
            
            self.enterNormalModeHandler = function() {
                self.editor.changeMode(self.normalMode);
            };
            
            self.enterTabletModeHandler = function() {
                self.editor.changeMode(self.tabletMode);
            };
            
            self.dashboardMaximizedHandler = function() {
                self.showRightPanelToggler(false);
            };
            
            self.dashboardRestoredHandler = function() {
                self.showRightPanelToggler(true);
            };
            
            var globalTimer = null;
            self.postDocumentShow = function() {
                $b.triggerBuilderResizeEvent('resize builder after document show');
                self.initializeMaximization();               
                $b.triggerEvent($b.EVENT_TILE_EXISTS_CHANGED, null, self.editor.tiles().length > 0);
                self.triggerTileTimeControlSupportEvent();
                //avoid brandingbar disappear when set font-size of text
                $("#globalBody").addClass("globalBody");
                self.editor.initializeMode();
            };
            
            self.notifyWindowResize = function() {
                for(var i=0; i<self.editor.tiles().length; i++) {
                    var tile = self.editor.tiles()[i];
                    if(tile.type() === "DEFAULT") {                            
                        self.notifyTileChange(tile, new Builder.TileChange("POST_WINDOWRESIZE"));
                    }
                }
            };
            
            self.returnFromTargetSelector = function(targets) {
//                if(targets.targets) {
//                    if(targets.targets.length === 1) {
//                        self.tgtSelLabel(getNlsString('DBS_BUILDER_ONE_TARGET_SELECTED'));
//                    }else {
//                        self.tgtSelLabel(getNlsString('DBS_BUILDER_MULTI_TARGETS_SELECTED', targets.targets.length));
//                    }
//                }
                self.targets(targets);
                var dashboardItemChangeEvent = new Builder.DashboardItemChangeEvent(new Builder.DashboardTimeRangeChange(self.timeSelectorModel.viewStart(),self.timeSelectorModel.viewEnd()), self.targets, null, null, self.dashboard.enableTimeRange(), self.dashboard.enableEntityFilter());
                Builder.fireDashboardItemChangeEvent(self.dashboard.tiles(), dashboardItemChangeEvent);
            };
            
            self.selectionMode = ko.observable(["byCriteria"]);
            self.returnMode = ko.observable('criteria');
            self.dropdownInitialLabel = ko.observable(getNlsString("DBS_BUILDER_ALL_TARGETS"));
            self.dropdownResultLabel = ko.observable(getNlsString("DBS_BUILDER_TARGETS_SELECTED"));
            
            self.getInputCriteria = function() {
                if(self.targets()) {
                    return self.targets.criteria;
                }
                return '';
            }

            var timeSelectorChangelistener = ko.computed(function(){
                return {
                    timeRangeChange:self.timeSelectorModel.timeRangeChange()
                };
            });
                
            timeSelectorChangelistener.subscribe(function (value) {
                if (value.timeRangeChange){
                    var dashboardItemChangeEvent = new Builder.DashboardItemChangeEvent(new Builder.DashboardTimeRangeChange(self.timeSelectorModel.viewStart(),self.timeSelectorModel.viewEnd()),self.targets, null, null, self.dashboard.enableTimeRange(), self.dashboard.enableEntityFilter());
                    Builder.fireDashboardItemChangeEvent(self.dashboard.tiles(), dashboardItemChangeEvent);
                    self.timeSelectorModel.timeRangeChange(false);
                }
            });

            var current = new Date();
            var initStart = dfu_model.getUrlParam("startTime") ? new Date(parseInt(dfu_model.getUrlParam("startTime"))) : new Date(current - 24*60*60*1000);
            var initEnd = dfu_model.getUrlParam("endTime") ? new Date(parseInt(dfu_model.getUrlParam("endTime"))) : current;
            self.timeSelectorModel.viewStart(initStart);
            self.timeSelectorModel.viewEnd(initEnd);
            self.datetimePickerParams = {
                startDateTime: initStart,
                endDateTime: initEnd,
                hideMainLabel: true,
                callbackAfterApply: function(start, end) {
                    self.timeSelectorModel.viewStart(start);
                    self.timeSelectorModel.viewEnd(end);
                    self.timeSelectorModel.timeRangeChange(true);		
                }
            };
        }
        
        Builder.registerModule(DashboardTilesViewModel, 'DashboardTilesViewModel');
        return {"DashboardTilesViewModel": DashboardTilesViewModel};
    }
);

// tile used to wrapper the only widget inside one page dashboard
var onePageTile;
