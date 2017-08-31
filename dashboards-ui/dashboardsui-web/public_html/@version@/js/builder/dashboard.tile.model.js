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
        'uifwk/js/util/mobile-util',
        'uifwk/js/sdk/context-util',
        'jquery',
//        'emsaasui/emcta/ta/js/sdk/tgtsel/api/TargetSelectorUtils',
        'builder/builder.core',
        'builder/time.selector.model',
        'builder/editor/editor.component',
        'builder/editor/editor',
        'builder/editor/editor.mode',
        'builder/dashboardDataSource/dashboard.datasource',
        'builder/widget/widget.model',
        'jqueryui'
//        'builder/builder.jet.partition'
//        'ckeditor'
    ],

    function(ko, oj, km, dfu, uiutil, dfumodel, mbu, contextModel, $/*, TargetSelectorUtils*/)
    {
        ko.mapping = km;
        var draggingTileClass = 'dbd-tile-in-dragging';

        function DashboardTilesViewModel($b, dashboardInst) {

            var widgetAreaWidth = 0;
            var widgetAreaContainer = null;
            var ctxUtil = new contextModel();
            var omcTimeConstants = ctxUtil.OMCTimeConstants;
            var omcContext = ctxUtil.getOMCContext();

            var dragStartRow = null;

            var self = this;
            $b.registerObject(self, 'DashboardTilesViewModel');
            self.scrollbarWidth = uiutil.getScrollbarWidth();
            self.isUnderSet = ko.unwrap(dashboardInst.type) === "SET" ? true : false;

            widgetAreaContainer = $b.findEl('.widget-area');

            self.dashboard = $b.dashboard;
            
            var ddsDashboard = new Builder.DashboardDataSource().dataSource[self.dashboard.id()];
            var eagerCreated = ddsDashboard.eagerCreated ? ddsDashboard.eagerCreated : null;
            self.normalMode = (eagerCreated && eagerCreated.normalMode) ? eagerCreated.normalMode : new Builder.NormalEditorMode();
            self.tabletMode = (eagerCreated && eagerCreated.tabletMode) ? eagerCreated.tabletMode : new Builder.TabletEditorMode();

            self.editor = new Builder.TilesEditor($b, Builder.isSmallMediaQuery() ? self.tabletMode : self.normalMode);
            self.editor.tiles = $b.dashboard.tiles;
            self.isMobileDevice = self.editor.mode.editable === true ? 'false' : 'true';
            widgetAreaWidth = widgetAreaContainer.width();

            self.previousDragCell = null;

            self.loginUser = ko.observable(dfu.getUserName());
            var dfu_model = new dfumodel(dfu.getUserName(), dfu.getTenantName());

            self.targets = eagerCreated ? eagerCreated.targets : ko.observable(null);
            self.timeSelectorModel = eagerCreated ? eagerCreated.timeSelector : null;
            
            self.tilesView = $b.getDashboardTilesView();
            self.isOnePageType = (self.dashboard.type() === Builder.SINGLEPAGE_TYPE);

            self.isCreator =  dfu.getUserName() === self.dashboard.owner();

            self.disableTilesOperateMenu = ko.observable(self.isOnePageType);
            self.showTimeRange = ko.observable(false);
            self.showWidgetTitle = ko.observable(true);

            self.resizingTile = ko.observable();
            self.resizingTileCopy = null;
            self.resizingOptions = ko.observable().extend({rateLimit: {timeout: 100, method: "always"}});
            self.beforeResizeWidth = null;
            self.beforeResizeHeight = null;
            self.currentWigedtWidth = ko.observable();
            self.currentWigedtHeight = ko.observable();
            self.currentWigedtWidth.extend({rateLimit: 1000, method: 'notifyWhenChangesStop '});
            self.currentWigedtHeight.extend({rateLimit: 1000, method: 'notifyWhenChangesStop '});

            self.currentWigedtWidth.subscribe(function () {
                notifyDragChangeProcess();
            });
            self.currentWigedtHeight.subscribe(function () {
                notifyDragChangeProcess();
            });

            self.resizingMonitor = ko.computed(function () {
                ko.mapping.fromJS(self.resizingOptions());
                if (self.resizingTile() && self.resizingOptions() && self.resizingOptions().mode) {
                    var hasChanged = self.editor.resizeTile(self.resizingTile(), self.resizingOptions());
                    if (hasChanged) {
                        self.show();
                    }
                }
            });

            function notifyDragChangeProcess() {
                if (self.beforeResizeWidth === null || self.beforeResizeHeight === null) {
                    return;
                }
                if (self.currentWigedtWidth() > self.beforeResizeWidth) {
                    self.notifyTileChange(self.resizingTileCopy, new Builder.TileChange("POST_WIDER"));
                } else if (self.currentWigedtWidth() < self.beforeResizeWidth) {
                    self.notifyTileChange(self.resizingTileCopy, new Builder.TileChange("POST_NARROWER"));
                } else if (self.currentWigedtHeight() > self.beforeResizeHeight) {
                    self.notifyTileChange(self.resizingTileCopy, new Builder.TileChange("POST_TALLER"));
                } else if (self.currentWigedtHeight() < self.beforeResizeHeight) {
                    self.notifyTileChange(self.resizingTileCopy, new Builder.TileChange("POST_SHORTER"));
                }
            }

            self.isEmpty = function() {
                return !self.editor.tiles() || self.editor.tiles().length === 0;
            };

            self.isDefaultTileExist = function() {
                for(var i in self.dashboard.tiles()){
                    if(!self.dashboard.tiles()[i]){
                            continue;
                    }
                    if(self.dashboard.tiles()[i].type() === "DEFAULT") {
                        return true;
                    }
                }
                return false;
            };

            self.appendNewTile = function(name, description, width, height, widget) {
                if (widget) {
                    var newTile = self.editor.createNewTile(name, description, width, height, widget, self.timeSelectorModel, self.targets, true, dashboardInst);
                    Builder.eagerLoadDahshboardSingleTileAtPageLoad(dfu, ko, newTile);                    
                    if (newTile){
                       self.editor.tiles.push(newTile);
                       if(newTile.type() === "TEXT_WIDGET") {
                            self.editor.draggingTile = newTile;
                            self.editor.moveTileTo(new Builder.Cell(0, 0), newTile);
                       }
                       self.show();
                       Builder.getTileConfigure(self.editor.mode, self.dashboard, newTile, self.timeSelectorModel, self.targets, dashboardInst);
                       $b.triggerEvent($b.EVENT_TILE_ADDED, null, newTile);
                    }
                }
                else {
                    oj.Logger.error("Null widget passed to a tile");
                }
            };

            self.initialize = function() {
                $b.addNewWidgetDraggingListener(self.onNewWidgetDragging);
                $b.addNewWidgetStopDraggingListener(self.onNewWidgetStopDragging);
                $b.addBuilderResizeListener(self.onBuilderResize);
                $b.addEventListener($b.EVENT_POST_DOCUMENT_SHOW, self.postDocumentShow);
                $b.addEventListener($b.EVENT_ENTER_NORMAL_MODE, self.enterNormalModeHandler);
                $b.addEventListener($b.EVENT_ENTER_TABLET_MODE, self.enterTabletModeHandler);
                $b.addEventListener($b.EVENT_TILE_MAXIMIZED, self.dashboardMaximizedHandler);
                $b.addEventListener($b.EVENT_TILE_RESTORED, self.dashboardRestoredHandler);
                $b.addEventListener($b.EVENT_AUTO_REFRESH_CHANGED, self.autoRefreshChanged);
                $b.addEventListener($b.EVENT_AUTO_REFRESHING_PAGE, self.autoRefreshingPage);
                $b.addEventListener($b.EVENT_DASHBOARD_CLEANUP_DELETED_WIDGETS, self.removeTilesForDeletedWidgets);
                self.initializeTiles();
            };

            self.onBuilderResize = function(width, height, leftWidth, topHeight) {
                widgetAreaWidth = Math.min(widgetAreaContainer.width(), $b.findEl(".tiles-col-container").width()-25);
                window.DEV_MODE && console.debug('widget area width is ' + widgetAreaWidth);
                self.show();
            };

            self.initTileKoRightBtnsResizeHdls = ko.observable(false);
            self.showPullRightBtn = function(clientGuid, data, event) {
                self.initTileKoRightBtnsResizeHdls(true);
                
                var tileRightBtns = $("#tile"+clientGuid).find('.dbd-pull-right');
                if (!tileRightBtns || !tileRightBtns.length) {
                    console.debug("initialize tile buttons");
                    var el = $($("#tile-widget-right-btns-template").text());
                    el.appendTo($("#tile"+clientGuid).find('.dbd-tile-header')[0]);
                    ko.applyBindings({$root: self, $data: data}, el[2]);
                }
                
                if (self.isResizingSupported()) {
                    var resizeHandlers = $("#tile"+clientGuid).find('.dbd-resize-handler-instance');
                    if (!resizeHandlers || !resizeHandlers.length) {
                        console.debug("initialize tile resizing handlers");
                        var el = $($("#tile-widget-resize-handlers-template").text());
                        el.appendTo($("#tile"+clientGuid).find('.dbd-tile-element')[0]);
                        ko.applyBindings(self, el[2]);
                        $("#tile"+clientGuid).find('.dbd-resize-handler-placeholder').remove();
                        self.enableTilesResizing();
                    }
                }
                
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
            self.prevFocusedClientGuid = ko.observable(null);
            self.focusOnTile = function(clientGuid, data, event) {
                self.showPullRightBtn(clientGuid, data, event);
                if(self.prevFocusedClientGuid()!==clientGuid) {
                    self.hidePullRightBtn(self.prevFocusedClientGuid(), data, event);
                    self.prevFocusedClientGuid(clientGuid);
                }
            };
            self.openInDataExplorer = function (event, ui) {
		if (!self.dashboard.systemDashboard() && self.isCreator){
                    $b.getToolBarModel().handleDashboardSave();
                }
                var iId = setInterval(function() {
                    if (!$b.isDashboardUpdated()) {
                        clearInterval(iId);
                        var tile = ko.dataFor(ui.currentTarget).$data;
                        self.editor.configure(tile);
                    }
                }, 300);
            };

            self.maxMinToggle = function (event, ui) {
                var tile = ko.dataFor(ui.currentTarget).$data;
                if (event.$data.maximizeEnabled()) {
                    self.maximize(tile);
                    self.notifyTileChange(tile, new Builder.TileChange("POST_MAXIMIZE"));
                    $b.triggerEvent($b.EVENT_TILE_MAXIMIZED, null, tile);
                } else {
                    self.restore(tile);
                    self.notifyTileChange(tile, new Builder.TileChange("POST_RESTORE"));
                    $b.triggerEvent($b.EVENT_TILE_RESTORED, null, tile);
                }
            };

            self.editingWidgetId = null;

            self.widgetMenuOpen = function(event, ui) {
                self.editingWidgetId = event.target.id;
            };

            self.menuItemSelect = function (event, ui) {
                var tile = ko.dataFor(ui.item[0]).$data;
                if (!tile) {
                    oj.Logger.error("Error: could not find tile from the ui data");
                    return;
                }
                switch (ui.item.data("option")) {
                    case "showhide-title":
                        self.editor.showHideTitle(tile);
                        self.show();
                        self.editor.editTile(tile, true);
                        self.notifyTileChange(tile, new Builder.TileChange("POST_HIDE_TITLE"));
                        break;
                    case "remove":
                        self.editor.deleteTile(tile);
                        self.show();
                        self.notifyTileChange(tile, new Builder.TileChange("POST_DELETE"));
                        $b.triggerEvent($b.EVENT_TILE_RESTORED, 'triggerred by tile deletion', tile);
                        $b.triggerEvent($b.EVENT_TILE_DELETED, null, tile);
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
                    case "edit":
                        self.editor.editTile(tile);
                        self.show();
                        self.notifyTileChange(tile, new Builder.TileChange("POST_EDIT"));
                        break;
                    case "up":
                        self.editor.moveUpTile(tile);
                        self.show();
                        tile.upEnabled(self.editor.mode.getModeRow(tile) > 0);
                        break;
                    case "down":
                        self.editor.moveDownTile(tile);
                        self.show();
                        tile.upEnabled(self.editor.mode.getModeRow(tile) > 0);
                        break;
                    case "left":
                        self.editor.moveLeftTile(tile);
                        self.show();
                        tile.leftEnabled(self.editor.mode.getModeColumn(tile) > 0);
                        tile.rightEnabled(self.editor.mode.getModeColumn(tile)+self.editor.mode.getModeWidth(tile) < self.editor.mode.MODE_MAX_COLUMNS);
                        break;
                    case "right":
                        self.editor.moveRightTile(tile);
                        self.show();
                        tile.leftEnabled(self.editor.mode.getModeColumn(tile) > 0);
                        tile.rightEnabled(self.editor.mode.getModeColumn(tile)+self.editor.mode.getModeWidth(tile) < self.editor.mode.MODE_MAX_COLUMNS);
                        break;
                    default:
                        break;
                }
                if(ui.item.data("option") !== 'edit'){
                    $b.triggerEvent($b.EVENT_TILE_RESIZED, null, tile);
                }
            };


           self.initializeTiles = function() {
                if(self.editor.tiles && self.editor.tiles()) {
                    for(var i=0; i< self.editor.tiles().length; i++) {
                        if(!self.editor.tiles()[i]){
                            continue;
                        }
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
                    if (eachTile !== tile){
                        eachTile.shouldHide(true);
                    }
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
                var dashboardItemChangeEvent = new Builder.DashboardItemChangeEvent(new Builder.DashboardTimeRangeChange(self.timeSelectorModel.viewStart(),self.timeSelectorModel.viewEnd(), self.timeSelectorModel.viewTimePeriod()), self.targets, null,tChange, self.dashboard.enableTimeRange(), self.dashboard.enableEntityFilter());
                Builder.fireDashboardItemChangeEventTo(tile, dashboardItemChangeEvent);
            };

            self.refreshThisWidget = function(tile) {
                self.notifyTileChange(tile, new Builder.TileChange("PRE_REFRESH"));
            };
            
            self.isResizingSupported = function() {
                if (self.dashboard.systemDashboard()) {
                    return false;
                }
                if (self.dashboard.sharePublic() && self.dashboard.owner() !== self.loginUser()) {
                    return false;
                }
                if (self.isMobileDevice === true) {
                    return false;
                }
                return true;
            };

            self.show = function() {
                self.showTiles();
                //The show function will be called frequently during drag and drop.
                //Thus the events will be attached to same elements many time which will cause performance issue,
                //especially for drag and mouse move events.
                //So we remove events first to make sure the event is attached only one time.
                $('.dbd-widget').off('dragstart', self.handleStartDragging);
                $('.dbd-widget').off('drag', self.handleOnDragging);
                $('.dbd-widget').off('dragstop', self.handleStopDragging);

                $('.dbd-widget').on('dragstart', self.handleStartDragging);
                $('.dbd-widget').on('drag', self.handleOnDragging);
                $('.dbd-widget').on('dragstop', self.handleStopDragging);

                self.enableTilesResizing();

                //close widget menu if the page is moved up/down by scroll bar
                $(".tiles-col-container").off("scroll");
                $(".tiles-col-container").on("scroll", function() {
                    if(self.editingWidgetId && $("#"+self.editingWidgetId).css("display")!=="none") {
                        $("#"+self.editingWidgetId).trigger({
                            type: 'keydown',
                            keyCode: 27
                        });
                        self.editingWidgetId && self.hidePullRightBtn(self.editingWidgetId.substring(8));
                    }
                });
            };
            
            self.enableTilesResizing = function() {
                if (self.isResizingSupported()) {
                    $('.dbd-resize-handler').off("mousedown");
                    $('.dbd-resize-handler').on('mousedown', function (event) {
                        var targetHandler = $(event.currentTarget),resizeMode = null;
                        if ($(targetHandler).hasClass('dbd-resize-handler-right')) {
                            resizeMode = self.editor.RESIZE_OPTIONS.EAST;
                        } else if ($(targetHandler).hasClass('dbd-resize-handler-left')) {
                            resizeMode = self.editor.RESIZE_OPTIONS.WEST;
                        } else if ($(targetHandler).hasClass('dbd-resize-handler-bottom')) {
                            resizeMode = self.editor.RESIZE_OPTIONS.SOUTH;
                        } else if ($(targetHandler).hasClass('dbd-resize-handler-right-bottom')) {
                            resizeMode = self.editor.RESIZE_OPTIONS.SOUTH_EAST;
                        }

                        var isResizing =  resizeMode !== null;
                        if(isResizing) {
                            $('#globalBody').addClass('none-user-select');
                            self.resizingTile(ko.dataFor(targetHandler.closest('.dbd-widget')[0]));
                            var changedingTarget=targetHandler.closest('.dbd-widget');
                            self.resizingOptions({mode:resizeMode,containerTop:changedingTarget.offset().top,containerLeft:changedingTarget.offset().left});
                            self.beforeResizeWidth = self.resizingTile().cssWidth();
                            self.beforeResizeHeight = self.resizingTile().cssHeight();
                            self.currentWigedtWidth(self.resizingTile().cssWidth());
                            self.currentWigedtHeight(self.resizingTile().cssHeight());                  
                            $b.triggerEvent($b.EVENT_TILE_RESIZED);
                        }
                        self.tilesView.disableDraggable();
                    });

                    $('#globalBody').off("mousemove").off("mouseup");
                    $('#globalBody').on('mousemove', function (event) {
                        if (self.resizingOptions()) {
                            if (self.resizingOptions().mode === self.editor.RESIZE_OPTIONS.EAST) {
                                $(this).css('cursor', 'ew-resize');
                            } else if (self.resizingOptions().mode === self.editor.RESIZE_OPTIONS.WEST) {
                                $(this).css('cursor', 'ew-resize');
                            } else if (self.resizingOptions().mode === self.editor.RESIZE_OPTIONS.SOUTH) {
                                $(this).css('cursor', 'ns-resize');
                            } else if (self.resizingOptions().mode === self.editor.RESIZE_OPTIONS.SOUTH_EAST) {
                                $(this).css('cursor', 'se-resize');
                            }
                            if($('.tiles-wrapper').length > 0 && event.clientX < $('.tiles-wrapper').offset().left){
                                event.clientX = $('.tiles-wrapper').offset().left;
                            }
                            var clonedTarget = $.extend(self.resizingOptions(), {left: event.clientX, top: event.clientY});
                            self.resizingOptions(clonedTarget);
                        }
                    }).on('mouseup', function (event) {
                        if (self.resizingOptions() !== null && typeof (self.resizingOptions()) !== 'undefined') {
                            self.currentWigedtWidth(self.resizingTile().cssWidth());
                            self.currentWigedtHeight(self.resizingTile().cssHeight());
                            self.resizingTileCopy = self.resizingTile();

                            //set move options enabld/disabled after resizing tile
                            self.resizingTileCopy.upEnabled(self.editor.mode.getModeRow(self.resizingTileCopy) > 0);
                            self.resizingTileCopy.leftEnabled(self.editor.mode.getModeColumn(self.resizingTileCopy) > 0);
                            self.resizingTileCopy.rightEnabled(self.editor.mode.getModeColumn(self.resizingTileCopy)+self.editor.mode.getModeWidth(self.resizingTileCopy) < self.editor.mode.MODE_MAX_COLUMNS);
                        }
                        self.resizingTile(null);
                        self.resizingOptions(null);
                        $(this).css('cursor', 'default');
                        $('#globalBody').removeClass('none-user-select');       
                        self.tilesView.enableDraggable();                 
                    });
                }
            };

            self.isDraggingCellChanged = function(pos) {
                if (!self.previousDragCell){
                    return true;
                }
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
                    tile.cssWidth(self.getDisplayWidthForTile(self.editor.mode.getModeWidth(tile)));
                    tile.cssHeight(self.getDisplayHeightForTile(self.editor.mode.getModeHeight(tile)));
                    tile.left(self.getDisplayLeftForTile(self.editor.mode.getModeColumn(tile)));
                    tile.top(self.getDisplayTopForTile(self.editor.mode.getModeRow(tile)));
                    tile.shouldHide(false);
                        for (var j = 0; j < self.editor.mode.getModeHeight(tile); j++) {
                            self.editor.setRowHeight(self.editor.mode.getModeRow(tile) + j);
                        }
                }
                self.tilesView.enableDraggable();
                var height = self.editor.tilesGrid.getHeight();
                $b.findEl('.tiles-wrapper').height(height);
            };

            self.reRender = function() {
                self.tilesView.disableMovingTransition();
                self.show();
                self.tilesView.enableMovingTransition();
            };
            var startTime, startX, startY;
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
                var originalRow = self.editor.mode.getModeRow(tile);
                var originalCol = self.editor.mode.getModeColumn(tile);

                var dragStartRow = self.editor.mode.getModeRow(tile);
                var cell = self.editor.getCellFromPosition(widgetAreaWidth, ui.helper.position());
//                if(tile.content) {
//                    cell.column = 0;
//                }

                $b.findEl('.tile-dragging-placeholder').css({
                    left: tile.left() - 5,
                    top: tile.top() - 5,
                    width: ui.helper.width() - 10,
                    height: ui.helper.height() - 10
                }).show();

                if((self.previousDragCell) && cell.column+self.editor.mode.getModeWidth(tile) <= self.editor.mode.MODE_MAX_COLUMNS) {
                    var cellsOccupiedByTile = self.editor.getCellsOccupied(cell.row, cell.column, self.editor.mode.getModeWidth(tile), self.editor.mode.getModeHeight(tile));
                    var tilesUnderCell = self.editor.getTilesUnder(cellsOccupiedByTile, tile);
                    var tilesBelowOriginalCell = self.editor.getTilesBelow(tile);
                    self.editor.draggingTile = tile;
                    var rowDiff, iTile;
                    for(var i in tilesUnderCell) {
                        if(!tilesUnderCell[i]){
                            continue;
                        }
                        iTile = tilesUnderCell[i];
                        rowDiff = cell.row - self.editor.mode.getModeRow(iTile) + self.editor.mode.getModeHeight(tile);
                        self.editor.moveTileDown(iTile, rowDiff);
                    }

                    self.editor.updateTilePosition(tile, cell.row, cell.column);

                    rowDiff = Math.abs(cell.row - dragStartRow);
                    for(i in tilesBelowOriginalCell) {
                        if(!tilesBelowOriginalCell[i]){
                            continue;
                        }
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
                if (!self.previousDragCell){
                    return;
                }
                var tile = ko.dataFor(ui.helper[0]);
                var cell = self.editor.getCellFromPosition(widgetAreaWidth, ui.helper.position());
//                if(tile.content) {
//                    cell.column = 0;
//                }
                ui.helper.css({left:tile.left(), top:tile.top()});

                $(ui.helper).css("opacity", 1);

                $b.findEl('.tile-dragging-placeholder').hide();
                if ($(ui.helper).hasClass(draggingTileClass)) {
                    $(ui.helper).removeClass(draggingTileClass);
                }

                tile.upEnabled(self.editor.mode.getModeRow(tile) > 0);
                tile.leftEnabled(self.editor.mode.getModeColumn(tile) > 0);
                tile.rightEnabled(self.editor.mode.getModeColumn(tile)+self.editor.mode.getModeWidth(tile) < self.editor.mode.MODE_MAX_COLUMNS);

                dragStartRow = null;
                self.previousDragCell = null;
                self.editor.draggingTile = null;
                $b.triggerEvent($b.EVENT_TILE_MOVE_STOPED, null);
            };

            self.onNewWidgetDragging = function(e, u) {
                var tcc = $b.findEl(".tiles-col-container");
                var rpt = $(".right-panel-toggler");
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
                    }else {
                        //set position of placeholder when start dragging tile from right drawer
                        $b.findEl('.tile-dragging-placeholder').css({
                            left: pos.left,
                            top: pos.top
                        });
                    }
                if (e.clientY <= tcc.offset().top || e.clientX >= rpt.offset().left) {
                    if (self.isEmpty()) {
                        $b.findEl('.tile-dragging-placeholder').hide();
                        $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new (default) widget dragging out of edit area", false);
                    }
                    $b.findEl('.tile-dragging-placeholder').hide();
                    return;
                }else {
                    if (self.isEmpty()){
                        $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new (default) widget dragging into edit area (stopped dragging)", true);
                    }
                    //use tile's left as the cursor's left to calculate the cell so that placeholder closely follow users' mouse
                    var cellPos = {};
                    cellPos.left = pos.left;
                    cellPos.top = pos.top;
                    if(tile) {
                        cellPos.left = cellPos.left - tile.cssWidth()/2;
                    }
                    var cell = self.editor.getCellFromPosition(widgetAreaWidth, cellPos);
                    if (!cell) {
                        return;
                    }

                    if(self.previousDragCell && self.previousDragCell.row === cell.row && self.previousDragCell.column === cell.column) {
                        return;
                    }
                    if(!self.previousDragCell) {
                        self.previousDragCell = cell;
                    }
                    var widget = ko.mapping.toJS(ko.dataFor(u.helper[0]));
                    var width = Builder.getTileDefaultWidth(widget, self.editor.mode), height = Builder.getTileDefaultHeight(widget, self.editor.mode);
                    if(cell.column>self.editor.mode.MODE_MAX_COLUMNS-width) {
                        cell.column = self.editor.mode.MODE_MAX_COLUMNS-width;
                    }
                    if (!tile) {
                        tile = self.editor.createNewTile(widget.WIDGET_NAME, null, width, height, widget, self.timeSelectorModel, self.targets, true, dashboardInst);
                        Builder.eagerLoadDahshboardSingleTileAtPageLoad(dfu, ko, tile);
                        
                        //fix EMCPDF-4722 by setting tile's display info here so that the semitransparent helper has correct width and height and is always attached to mouse
                        tile.cssWidth(self.getDisplayWidthForTile(self.editor.mode.getModeWidth(tile)));
                        tile.cssHeight(self.getDisplayHeightForTile(self.editor.mode.getModeHeight(tile)));
                        tile.left(pos.left-tile.cssWidth()/2);
                        tile.top(pos.top-15);
                        
                        u.helper.tile = tile;
                        self.editor.tiles.push(tile);
                        $b.triggerEvent($b.EVENT_TILE_ADDED, null, tile);
                    }

                    var tileInCell = self.editor.tilesGrid.tileGrid[cell.row] ? self.editor.tilesGrid.tileGrid[cell.row][cell.column] :null;
                    if(tileInCell && self.editor.mode.getModeRow(tileInCell) !== cell.row) {
                        return;
                    }

                    self.editor.draggingTile = tile;
                    var cells = self.editor.getCellsOccupied(cell.row, cell.column, width, height);
                    var tilesToMove = self.editor.getTilesUnder(cells, tile);
                    var tilesBelowOriginalCell = self.editor.getTilesBelow(tile);
                    for(var i in tilesToMove) {
                        if(!tilesToMove[i]){
                            continue;
                        }
                        var rowDiff = cell.row-self.editor.mode.getModeRow(tilesToMove[i])+self.editor.mode.getModeHeight(tile);
                        self.editor.moveTileDown(tilesToMove[i], rowDiff);
                    }

                    self.editor.updateTilePosition(tile, cell.row, cell.column);

                    var rowDiff = Math.abs(cell.row - self.editor.mode.getModeRow(tile));
                    for(i in tilesBelowOriginalCell) {
                        if(!tilesBelowOriginalCell[i]){
                            continue;
                        }
                        var iTile = tilesBelowOriginalCell[i];
                        rowDiff = (rowDiff===0) ? self.editor.mode.getModeHeight(tile) : rowDiff;
                        self.editor.moveTileUp(iTile, rowDiff);
                    }

                    self.editor.tilesReorder();
                    self.show();

                    //restore simulated helper after show()
                    tile.left(pos.left-tile.cssWidth()/2);
                    tile.top(pos.top-15);
                    $('#tile'+tile.clientGuid).css("opacity", 0.6);
                    if(!$('#tile'+tile.clientGuid).hasClass(draggingTileClass)) {
                        $('#tile'+tile.clientGuid).addClass(draggingTileClass);
                    }

                   $b.findEl('.tile-dragging-placeholder').css({
                            left: self.getDisplayLeftForTile(self.editor.mode.getModeColumn(tile)) - 5,
                            top: self.getDisplayTopForTile(self.editor.mode.getModeRow(tile)) - 5,
                            width: self.getDisplayWidthForTile(width) - 10,
                            height: self.getDisplayHeightForTile(height)  - 10
                        }).show();

                    self.previousDragCell = cell;
                }

            };

            self.onNewWidgetStopDragging = function(e, u) {
                var tcc = $b.findEl(".tiles-col-container");
                var rpt = $(".right-panel-toggler");
                var tile = u.helper.tile;

                if(u.helper.tile && ($('#tile'+u.helper.tile.clientGuid).hasClass(draggingTileClass))) {
                    $('#tile'+u.helper.tile.clientGuid).removeClass(draggingTileClass);
                }
                if (e.clientY <= tcc.offset().top || e.clientX >= rpt.offset().left) {
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
                Builder.getTileConfigure(self.editor.mode, self.dashboard, tile, self.timeSelectorModel, self.targets, dashboardInst);
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

            var globalTimer = null;
            self.postDocumentShow = function() {
                self.initializeMaximization();
                $b.triggerEvent($b.EVENT_TILE_EXISTS_CHANGED, null, self.editor.tiles().length > 0);
                //avoid brandingbar disappear when set font-size of text
                $("#globalBody").addClass("globalBody");
                self.editor.initializeMode();
                $b.triggerBuilderResizeEvent('resize builder after document show');
            };

            self.notifyWindowResize = function() {
                for(var i=0; i<self.editor.tiles().length; i++) {
                    var tile = self.editor.tiles()[i];
                    if(tile.type() === "DEFAULT") {
                        self.notifyTileChange(tile, new Builder.TileChange("POST_WINDOWRESIZE"));
                    }
                }
            };
            
            self.removeTilesForDeletedWidgets = function() {
                // find deleted tiles
                var deletedTiles = [];
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    if (self.editor.tiles()[i].widgetDeleted && self.editor.tiles()[i].widgetDeleted())
                        deletedTiles.push(self.editor.tiles()[i]);
                }

                deletedTiles.sort(function(tile1, tile2) {
                    if (!tile1.widgetDeletionTime || !tile1.widgetDeletionTime() || tile2.widgetDeletionTime || !tile2.widgetDeletionTime()) return 0;
                    return new Date(tile1.widgetDeletionTime()) - new Date(tile2.widgetDeletionTime());
                });

                for (var i = 0; i < deletedTiles.length; i++) {
                    var tile = deletedTiles[i];
                    self.editor.deleteTile(tile);
                    console.info("Dashboard page handle the deleted widgets: widget ID is: " + tile.WIDGET_UNIQUE_ID() + ", widget name is: " + tile.WIDGET_NAME());
                    self.notifyTileChange(tile, new Builder.TileChange("POST_DELETE"));
                }
            };

            self.initUserFilterOptions = function() {
                Builder.loadDashboardUserOptions(self.dashboard.id(), self);
            };

            self.applyClickedByAutoRefresh = ko.observable(false);
            self.initUserFilterOptions();
            self.dashboardExtendedOptions = self.dashboard.extendedOptions ? JSON.parse(self.dashboard.extendedOptions()) : null;

            self.returnFromPageTsel = function(targets) {
                self.targets(targets);
                var dashboardItemChangeEvent = new Builder.DashboardItemChangeEvent(new Builder.DashboardTimeRangeChange(self.timeSelectorModel.viewStart(),self.timeSelectorModel.viewEnd(), self.timeSelectorModel.viewTimePeriod()), self.targets, null, null, self.dashboard.enableTimeRange(), self.dashboard.enableEntityFilter());
                Builder.fireDashboardItemChangeEvent(self.dashboard.tiles(), dashboardItemChangeEvent);

                if(!self.userExtendedOptions.tsel) {
                    self.userExtendedOptions.tsel = {};
                }
                
                if(self.toolbarModel.zdtStatus()){
                    return;
                }
                
                self.userExtendedOptions.tsel.entityContext = targets;

                self.saveUserFilterOptions(function(data) { //update userExtendedOptions
                    self.initUserFilterOptions();
                });
            };

            self.entitySelectorInited = false;
            self.selectionMode = ko.observable("byCriteria");
            self.returnMode = ko.observable('criteria');
            self.dropdownInitialLabel = ko.observable(getNlsString("DBS_BUILDER_ALL_ENTITIES"));
            self.dropdownResultLabel = ko.observable(getNlsString("DBS_BUILDER_ENTITIES_SELECTED"));

            self.getInputCriteria = ko.computed(function() {
                if(self.targets && self.targets()) {
                    return self.targets().criteria;
                }
                return '';
            });

            self.returnFromTsel = function(targets) {
                window.DashboardWidgetAPI.setTargetSelectionContext(targets);
                    self.returnFromPageTsel(targets);
            };
            
            self.initializedCallback = function() {               
                if(self.entitySelectorInited) {
                   return;
                }                
                //apply criteriaList to widgets at load time
                for(var i=0; i<self.dashboard.tiles().length; i++) {
                    var tile = self.dashboard.tiles()[i]; 
                    tile.dashboardItemChangeEvent && (tile.dashboardItemChangeEvent.targets = self.targets());
                }
                self.entitySelectorInited = true;
            };
            
            if(!self.targets()) {
                $.when(Builder.loadEntityContext(self, self.dashboard.enableEntityFilter(), self.isUnderSet)).done(function(initTargets) {
                    initTargets && self.targets(initTargets);
                });
            }
            
            function callbackForOmcCtxChange(ctxChangeEvent) {
                //handle entity context changed by selecting GC entity selector
                if(!ctxChangeEvent || !ctxChangeEvent.contextName) {
                    return;
                }
                if(ctxChangeEvent.contextName !== "compositeMEID" && ctxChangeEvent.contextName !== "entityMEIDs" && ctxChangeEvent.contextName !== "composite" && ctxChangeEvent.contextName !== "entity") {
                    console.log("***callbackForOmcCtxChange: contextName is " + ctxChangeEvent.contextName + ". It is not entity context change, so return***");
                    return;
                }
                
                Builder.requireTargetSelectorUtils(true, function(TargetSelectorUtils) {
                    if (TargetSelectorUtils) {
                        TargetSelectorUtils.registerComponents();
                    }
                    $.when(TargetSelectorUtils.getCriteriaFromOmcContext()).done(function (criteria) {
                        var entityContext = {criteria: criteria};
                        //save users' selection of entity context by GC entity selector
                        self.returnFromPageTsel(entityContext);
                    });
                }); 
                    
            }
            
            ctxUtil.subscribeOMCContextChangeEvent(callbackForOmcCtxChange);
            
            if(!self.timeSelectorModel) {
                self.timeSelectorModel = new Builder.TimeSelectorModel();
                var timeContext = Builder.loadTimeContext(self, self.dashboard.enableTimeRange(), self.isUnderSet);
                var initStart = timeContext.start;
                var initEnd = timeContext.end;
                var timePeriod = timeContext.timePeriod;
                var current = new Date();

                if(ctxUtil.formalizeTimePeriod(timePeriod)) {
                    if(ctxUtil.formalizeTimePeriod(timePeriod) !== omcTimeConstants.QUICK_PICK.CUSTOM) { //get start and end time for relative time period
                        var tmp = ctxUtil.getStartEndTimeFromTimePeriod(ctxUtil.formalizeTimePeriod(timePeriod));
                        if(tmp) {
                            initStart = tmp.start;
                            initEnd = tmp.end;
                        }
                    }
                }

                if(!(initStart instanceof Date && initEnd instanceof Date)) {
                    initStart = new Date(current - 14*24*60*60*1000);
                    initEnd = current;
                }
                self.timeSelectorModel.viewStart(initStart);
                self.timeSelectorModel.viewEnd(initEnd);
                self.timeSelectorModel.viewTimePeriod(timePeriod);
            }

            var timeSelectorChangelistener = ko.computed(function(){
                return {
                    timeRangeChange:self.timeSelectorModel.timeRangeChange()
                };
            });

            timeSelectorChangelistener.subscribe(function (value) {
                if (value.timeRangeChange){
                    var dashboardItemChangeEvent = new Builder.DashboardItemChangeEvent(new Builder.DashboardTimeRangeChange(self.timeSelectorModel.viewStart(),self.timeSelectorModel.viewEnd(), self.timeSelectorModel.viewTimePeriod()),self.targets, null, null, self.dashboard.enableTimeRange(), self.dashboard.enableEntityFilter());
                    Builder.fireDashboardItemChangeEvent(self.dashboard.tiles(), dashboardItemChangeEvent);
                    self.timeSelectorModel.timeRangeChange(false);
                }
            });
         
            self.initStart = ko.observable(self.timeSelectorModel.viewStart());
            self.initEnd = ko.observable(self.timeSelectorModel.viewEnd());
            self.timePeriod = ko.observable(self.timeSelectorModel.viewTimePeriod());
           
            var dashboardExdedOpt = self.dashboard.extendedOptions && JSON.parse(ko.unwrap(self.dashboard.extendedOptions()));
            dashboardExdedOpt && dashboardExdedOpt.timePeriodNotShow ? self.timePeriodsNotToShow = dashboardExdedOpt.timePeriodNotShow :self.timePeriodsNotToShow = [];
            //whether to enable "Latest" on custom panel
            var enableLatestOnCustomPanel = ($.inArray(ctxUtil.OMCTimeConstants.QUICK_PICK.LATEST, self.timePeriodsNotToShow) >=0) ? false : true;
            if(self.isUnderSet) {
                self.datetimePickerParams = {
                    startDateTime: self.initStart,
                    endDateTime: self.initEnd,
                    timePeriod: self.timePeriod,
                    hideMainLabel: true,
                    timePeriodsSet: ctxUtil.OMCTimeConstants.timePeriodsSet.SHORT_TERM,
                    enableLatestOnCustomPanel: enableLatestOnCustomPanel,
                    dtpickerPosition: "left",
                    callbackAfterApply: function(start, end, tp) {
                        callbackAfterApply(start, end, tp);   
                    }
                };
            }else {
                var headerWrapper = $("#headerWrapper")[0];
                if(headerWrapper) {
                    var headerViewModel = ko.dataFor(headerWrapper);
                    var headerViewModel = ko.dataFor(headerWrapper);
                    headerViewModel.brandingbarParams.timeSelectorParams.startDateTime(ko.unwrap(self.initStart));
                    headerViewModel.brandingbarParams.timeSelectorParams.endDateTime(ko.unwrap(self.initEnd));
                    headerViewModel.brandingbarParams.timeSelectorParams.timePeriod(ko.unwrap(self.timePeriod));
                    headerViewModel.brandingbarParams.timeSelectorParams.enableLatestOnCustomPanel(enableLatestOnCustomPanel);
                    headerViewModel.brandingbarParams.timeSelectorParams.callbackAfterApply = function(start, end, tp) {
                        callbackAfterApply(start, end, tp);
                    }
                }
            }
            
            function callbackAfterApply(start, end, tp) {
                self.timeSelectorModel.viewStart(start);
                self.timeSelectorModel.viewEnd(end);
                self.timeSelectorModel.viewTimePeriod(tp);
                if (tp === omcTimeConstants.QUICK_PICK.CUSTOM) {
                    self.initStart(start);
                    self.initEnd(end);
                    self.timePeriod(tp);
                } else {
                    self.timePeriod(tp);
                }
                self.timeSelectorModel.timeRangeChange(true);

                if (!self.applyClickedByAutoRefresh() && !self.toolbarModel.zdtStatus()) {
                    if (!self.userExtendedOptions.timeSel) {
                        self.userExtendedOptions.timeSel = {};
                    }
                    self.userExtendedOptions.timeSel.timePeriod = Builder.getTimePeriodValue(tp) ? Builder.getTimePeriodValue(tp) : tp;
                    self.userExtendedOptions.timeSel.start = start.getTime();
                    self.userExtendedOptions.timeSel.end = end.getTime();
                    self.saveUserFilterOptions(function (data) { //update userExtendedOptions
                        self.initUserFilterOptions();
                    });

                        //                            $b.triggerEvent($b.EVENT_TIME_SELECTION_CHANGED, "time selection is changed by selecting date/time picker", Builder.getTimePeriodValue(tp), start.getTime(), end.getTime());
                }
                self.applyClickedByAutoRefresh(false);
            }

            self.saveUserFilterOptions = function (succCallback) {
                if (!self.toolbarModel.zdtStatus()) {
                    var userFilterOptions = {
                        dashboardId: self.dashboard.id(),
                        extendedOptions: JSON.stringify(self.userExtendedOptions),
                        autoRefreshInterval: self.userExtendedOptions.autoRefresh.defaultValue
                    };

                    new Builder.DashboardDataSource().saveDashboardUserOptions(userFilterOptions, succCallback);
                }
            };
            
            self.autoRefreshChanged = function(interval) {
                self.userExtendedOptions.autoRefresh = {"defaultValue": interval};                
                self.saveUserFilterOptions();
            };
            
            self.autoRefreshingPage = function() {
                self.applyClickedByAutoRefresh(true);
            };
        }

        Builder.registerModule(DashboardTilesViewModel, 'DashboardTilesViewModel');
        return {"DashboardTilesViewModel": DashboardTilesViewModel};
    }
);

// tile used to wrapper the only widget inside one page dashboard
var onePageTile;
