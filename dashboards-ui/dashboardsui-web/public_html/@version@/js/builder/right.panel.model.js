/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'jquery',
        'dfutil',
        'uifwk/js/util/mobile-util',
        'uiutil',
        'ojs/ojcore',
        'builder/tool-bar/edit.dialog',
        'uifwk/js/util/screenshot-util',
        'builder/right-panel/right.panel.control.model',
        'builder/right-panel/right.panel.filter',
        'builder/right-panel/right.panel.widget',
        'builder/right-panel/right.panel.edit',
        'builder/right-panel/right.panel.edit.content',
        'uifwk/js/util/zdt-util',
        'jqueryui',
        'builder/builder.core',
        'builder/widget/widget.model'
    ],
    function(ko, $, dfu, mbu, uiutil, oj, ed, ssu, rpc, rpf, rpw, rpe, rpec, zdtUtilModel) {
        function ResizableView($b) {
            var self = this;

            self.initialize = function() {
                $b.addBuilderResizeListener(self.onResizeFitSize);
            };

            self.onResizeFitSize = function(width, height, leftWidth, topHeight) {
                self.rebuildElementSet();
                self.$list.each(function() {
                    var elem = $(this)
                    ,_topHeight = topHeight
                    , v_siblings = elem.siblings(".fit-size-vertical-sibling:visible")
                    , h = 52;
                    if(v_siblings && 1 === v_siblings.length && v_siblings.hasClass("dbd-right-panel-title")){
                        h = 0;
                    }
                    if($(".builder-dbd-select-tip:visible").outerHeight()>0){
                        _topHeight = _topHeight-2-$(".builder-dbd-select-tip:visible").height();
                    }
                    if (v_siblings && v_siblings.length > 0) {
                        for (var i = 0; i < v_siblings.length; i++) {
                            h += $(v_siblings[i]).outerHeight();
                        }
                        elem.height(height - _topHeight - h);
                    }
                    if(elem.hasClass("dbd-left-panel")||elem.hasClass("right-panel-toggler")){
                        if(v_siblings && 0 === v_siblings.length){
                            elem.height(height - _topHeight);
                        }
                        if(ko.dataFor($('.df-right-panel')[0]).isDashboardSet()){
                            elem.css("top",_topHeight-99);
                        }else{
                            elem.css("top",_topHeight-91);
                        }
                    }
                });
            };

            self.rebuildElementSet = function() {
                self.$list = $([].concat.apply($b.findEl(".fit-size"),$(".df-right-panel .fit-size")));
            };

            if(!window._uifwk){
                window._uifwk={};
            }
            if (!window._uifwk.brandingbar_css_load_callback) {
                window._uifwk.brandingbar_css_load_callback = function(){
                    $b.triggerBuilderResizeEvent('uifwk-common-alta.css loaded');
                };
            }
            self.initialize();
        }

        function RightPanelModel($b, tilesViewModel, toolBarModel, dashboardsetToolBarModel) {
            var self = this;
            self.dashboardsetToolBarModel = dashboardsetToolBarModel;
            self.dashboard = $b.dashboard;
            self.tilesViewModel = tilesViewModel;
            self.toolBarModel = toolBarModel;
            self.editDashboardDialogModel = ko.observable(null);
            self.sortedTiles = ko.computed(function(){
//                //add for detecting dashboard tabs switching in set
                self.editDashboardDialogModel();
                var tiles = [];
                if (self.dashboard.tiles && self.dashboard.tiles()) {
                    for (var i = 0; i < self.dashboard.tiles().length; i++) {
                        if(self.dashboard.tiles()[i].type() !== "TEXT_WIDGET") {
                            tiles.push(self.dashboard.tiles()[i]);
                        }
                    }
                    tiles.sort(function(tile1, tile2) {
                        return tile1.WIDGET_NAME() > tile2.WIDGET_NAME() ? 1 : (tile1.WIDGET_NAME() < tile2.WIDGET_NAME() ? -1 : 0);
                    });
                }
                return tiles;
//                return self.dashboard.tiles && self.dashboard.tiles() ? self.dashboard.tiles().sort(function (tileA, tileB) {
//                    return tileA.WIDGET_NAME() > tileB.WIDGET_NAME()?1:(tileA.WIDGET_NAME() < tileB.WIDGET_NAME()?-1:0);
//                }):[];
            });

            $b.registerObject(this, 'RightPanelModel');

            self.$b = $b;
            self.selectedContent = ko.observable();
            self.isDashboardSet = dashboardsetToolBarModel.isDashboardSet;
            self.rightPanelControl=new rpc.rightPanelControl(self.$b,self.selectedContent);
            self.rightPanelFilter = new rpf.RightPanelFilterModel(self.$b, ko.unwrap(self.isDashboardSet));
            self.rightPanelWidget= new rpw.rightPanelWidget(self.$b);
            self.rightPanelEdit=new rpe.rightPanelEditModel(self.$b,self.dashboardsetToolBarModel);
            self.rightPanelEditContent=new rpec.rightPanelEditContentModel(self.$b,self.dashboardsetToolBarModel,self.selectedContent);
            self.selectedDashboard = ko.observable(self.dashboard);
            self.normalMode = new Builder.NormalEditorMode();
            self.tabletMode = new Builder.TabletEditorMode();
            self.modeType = Builder.isSmallMediaQuery() ? self.tabletMode : self.normalMode;
            self.isMobileDevice = self.modeType.editable === true ? 'false' : 'true';
            self.isOobDashboardset=dashboardsetToolBarModel.isOobDashboardset; 
            self.emptyDashboard = tilesViewModel && tilesViewModel.isEmpty();
            self.maximized = ko.observable(false);
            var zdtUtil = new zdtUtilModel();
            self.zdtStatus = ko.observable(false);
            zdtUtil.detectPlannedDowntime(function (isUnderPlannedDowntime) {
                self.zdtStatus(isUnderPlannedDowntime);
//                self.zdtStatus(true)
            });

            self.loadToolBarModel = function(toolBarModel,_$b){
                self.toolBarModel = toolBarModel;
                self.dashboard = _$b.dashboard;
                self.editDashboardDialogModel(new ed.EditDashboardDialogModel(_$b,toolBarModel));
                self.rightPanelControl.$b(_$b);
                self.rightPanelEdit.$b(_$b);
                self.rightPanelEdit.editDashboardDialogModel(self.editDashboardDialogModel());
                if(toolBarModel) {
                    self.rightPanelControl.dashboardEditDisabled(toolBarModel.editDisabled()) ;
                }else{
                    self.rightPanelControl.dashboardEditDisabled(true) ;
                }
            };
            
            self.loadRightPanelWidget = function(_$b){
                self.rightPanelWidget.change$bContext(_$b);
            };

            self.loadToolBarModel(toolBarModel,self.$b);

            self.loadTilesViewModel = function(tilesViewModel){
                if(!tilesViewModel) {
                    return;
                }
                self.tilesViewModel = tilesViewModel;
                self.emptyDashboard = tilesViewModel && tilesViewModel.isEmpty();
                self.rightPanelControl.rightPanelIcon(self.emptyDashboard ? "wrench" : "none");

                //reset filter settings in right drawer when selected dashboard is changed
                self.rightPanelFilter.loadRightPanelFilter(tilesViewModel);

                self.rightPanelEdit.dashboardSharing(self.dashboard.sharePublic() ? "shared" : "notShared");      
            };
          
            self.initialize = function() {
                    if (self.isMobileDevice === 'true' || self.isOobDashboardset() || self.zdtStatus()) {
                        self.rightPanelControl.completelyHidden(true);
                        self.$b.triggerBuilderResizeEvent('OOB dashboard detected and hide right panel');
                    } else {
                        self.rightPanelControl.completelyHidden(false);
                        if("editcontent" === self.rightPanelControl.editPanelContent()){
                            self.rightPanelControl.editPanelContent("settings");
                        }
                        if (self.emptyDashboard) {
                            self.rightPanelControl.showRightPanel(true);
                        } else {
                            self.rightPanelControl.showRightPanel(false);
                        }

                        if ("NORMAL" !== self.$b.dashboard.type()
                                || true === self.$b.dashboard.systemDashboard()
                                || false === self.dashboardsetToolBarModel.dashboardsetConfig.isCreator()) {
                            self.rightPanelControl.completelyHidden(true);
                        }
                        self.$b.triggerBuilderResizeEvent('Initialize right panel');
                    }
                    resetRightPanelWidth();
                    if (self.isDashboardSet()) {
                        self.dashboardsetToolBarModel.reorderedDbsSetItems.subscribe(function () {
                            var isOnlyDashboardPicker = self.dashboardsetToolBarModel.dashboardsetItems.length === 1 && self.dashboardsetToolBarModel.dashboardsetItems[0].type === "new";
                            self.rightPanelEdit.dashboardsetShareDisabled(isOnlyDashboardPicker);
                        });
                    }


                    self.initEventHandlers();
                    
                    if(self.rightPanelControl.completelyHidden() === false && self.rightPanelWidget.isWidgetLoaded()===false) {
                        //load widgets only when right panel is editable and have not loaded widget before
                        self.rightPanelWidget.loadWidgets(null,function successCallback(){
                            initRightPanelDragAndTile();
                        });
                    }

                    self.rightPanelControl.initializeRightPanel.subscribe(function (newValue) {
                        newValue && self.rightPanelControl.initializeCollapsible();
                        newValue && initRightPanelDragAndTile();
                    });

                    self.emptyDashboard && self.rightPanelControl.initializeRightPanel(true);
                    initRightPanelDragAndTile();
                    if(!self.rightPanelControl.resetEmptyDashboardName){
                        if(self.rightPanelEdit && self.rightPanelEdit.resetEmptyDashboardName){
                            self.rightPanelControl.resetEmptyDashboardName = self.rightPanelEdit.resetEmptyDashboardName;
                        }else if(self.editDashboardDialogModel()){
                            self.rightPanelControl.resetEmptyDashboardName = self.editDashboardDialogModel().resetEmptyDashboardName;
                        }
                        self.rightPanelControl.resetEmptyDashboardName && $("#dbd-tabs-container")[0] && $("#dbd-tabs-container").on( "ojbeforeselect", function( event, ui ) { //reset empty dashboard name when user switch tabs in dashboard set
                            self.rightPanelControl.resetEmptyDashboardName()
                        } );
                    }
            };

            self.initEventHandlers = function() {
                self.$b.addEventListener(self.$b.EVENT_TILE_MAXIMIZED, self.tileMaximizedHandler);
                self.$b.addEventListener(self.$b.EVENT_TILE_RESTORED, self.tileRestoredHandler);
                self.$b.addEventListener(self.$b.EVENT_DASHBOARD_SHARE_CHANGED, self.dashboardShareChanged)
            };

            self.initDraggable = function() {
                self.initWidgetDraggable();
            };

            self.initWidgetDraggable = function() {
                $(".dbd-left-panel-widget-text").draggable({
                    helper: "clone",
                    scroll: false,
                    start: function(e, t) {
                        self.$b.triggerEvent(self.$b.EVENT_NEW_WIDGET_START_DRAGGING, null, e, t);
                    },
                    drag: function(e, t) {
                        self.$b.triggerEvent(self.$b.EVENT_NEW_WIDGET_DRAGGING, null, e, t);
                    },
                    stop: function(e, t) {
                        self.$b.triggerEvent(self.$b.EVENT_NEW_WIDGET_STOP_DRAGGING, null, e, t);
                    }
                });
            };
                        
            self.tileMaximizedHandler = function() {
                self.maximized(true);
                self.rightPanelControl.completelyHidden(true);
                self.$b.triggerBuilderResizeEvent('tile maximized and completely hide left panel');
            };

            self.tileRestoredHandler = function() {
                self.maximized(false);
                if(self.isMobileDevice !== 'true' && !self.isOobDashboardset() && !self.zdtStatus()) {
                    self.rightPanelControl.completelyHidden(false);
                }

                self.initDraggable();
                self.$b.triggerBuilderResizeEvent('hide left panel because restore');
            };
            
            self.dashboardShareChanged = function(value) {
                if(!tilesViewModel) {
                    return;
                }
                if(value === true) {                    
                    self.rightPanelFilter.setDefaultValuesWhenSharing(tilesViewModel.userExtendedOptions);                    
                }
            };
            
            self.zdtStatus.subscribe(function (newZdtStatus) {
                if (newZdtStatus) {
                    self.rightPanelControl.completelyHidden(true);
                    self.$b.triggerBuilderResizeEvent('OOB dashboard detected and hide right panel');
                }
            });

            function resetRightPanelWidth() {
                $('.dbd-left-panel-show').css('width', '320px');
                $('.dbd-left-panel-hide').css('width', '0');
            }

            function initRightPanelDragAndTile() {
                self.initDraggable();
                self.rightPanelWidget.tilesViewModel(self.tilesViewModel);
                ResizableView(self.$b);
            }
        }

        Builder.registerModule(RightPanelModel, 'RightPanelModel');
        Builder.registerModule(ResizableView, 'ResizableView');

        return {"RightPanelModel": RightPanelModel, "ResizableView": ResizableView};
    }
);

