define(['knockout',
'jquery',
'ojs/ojcore',
'dfutil',
'uifwk/js/util/mobile-util',
'uiutil',
'ojs/ojradioset',
'ojs/ojcollapsible'
],
function (ko, $, oj, dfu, mbu, uiutil) {
    function rightPanelControl($b,selectedContent) {
        var self = this;
        self.$b = ko.observable($b);
        self.normalMode = new Builder.NormalEditorMode();
        self.tabletMode = new Builder.TabletEditorMode();
        self.modeType = Builder.isSmallMediaQuery() ? self.tabletMode : self.normalMode;
        self.isMobileDevice = self.modeType.editable === true ? 'false' : 'true';
        self.dashboardEditDisabled = ko.observable(self.$b().getToolBarModel&&self.$b().getToolBarModel() ? self.$b().getToolBarModel().editDisabled() : true);
        self.showRightPanelToggler = ko.observable(self.isMobileDevice !== 'true');
        self.initializeRightPanel= ko.observable(false);
        self.showRightPanel = ko.observable(false);
        self.rightPanelIcon = ko.observable(self.$b().getToolBarModel && self.$b().getToolBarModel() && $b.getDashboardTilesViewModel().isEmpty() ? "wrench" : "none");
        self.completelyHidden = ko.observable(false);
        self.editPanelContent = ko.observable("settings");
        self.scrollbarWidth = uiutil.getScrollbarWidth();
        self.lastHighlightWigetIndex=null;

        self.expandDBEditor = function (target, isToExpand) {
            if ("singleDashboard-edit" === target) {
                $('.dbd-right-panel-editdashboard-general').ojCollapsible("option", "expanded", isToExpand);
            } else if ("dashboardset-edit" === target) {
                $('.dbd-right-panel-editdashboard-set-general').ojCollapsible("option", "expanded", isToExpand);
            }
        };

        self.editRightpanelLinkage = function (target,param,isShowHideTitleChange) {
            if(isShowHideTitleChange){
                resetTileHighlighted();
                self.editPanelContent()==='editcontent' && selectedContent && selectedContent(param);
                return;
            }
            var highlightIcon = "pencil";
            self.completelyHidden(false);
            var panelTarget;
            if (target === "singleDashboard-edit") {
                panelTarget = "editdashboard";
            } else if (target === "dashboardset-edit") {
                panelTarget = "editset";
            } else if (target === "editcontent") {
                panelTarget = "editcontent";
            }

            self.rightPanelIcon(highlightIcon);
            if (!self.showRightPanel()) {
                self.toggleLeftPanel();
                self.editPanelContent(panelTarget);
                self.expandDBEditor(target, true);
            } else {
                self.editPanelContent(panelTarget);
                self.expandDBEditor(target, true);
                $(".dashboard-picker-container:visible").addClass("df-collaps");
            }

            if (panelTarget === "editcontent") {
                $('.dbd-right-panel-editcontent-title').ojCollapsible("option", "expanded", true);
                resetTileHighlighted();
                selectedContent && selectedContent(param);
            }
                self.resetEmptyDashboardName && self.resetEmptyDashboardName(); //reset empty dashboard name when user click edit button on tile menu

            self.$b().triggerBuilderResizeEvent('resize right panel');
        };

        self.toggleRightPanel = function (data, event, target) {
            var clickedIcon;
            if ($(event.currentTarget).hasClass('rightpanel-pencil')) {
                clickedIcon = "pencil";
            } else if ($(event.currentTarget).hasClass('rightpanel-wrench')) {
                clickedIcon = "wrench";
            }
            resetTileHighlighted();

            var _changeRightPanelTab=self.showRightPanel() && clickedIcon !== self.rightPanelIcon();
            var _closeRightPanel=self.showRightPanel();

            if (_changeRightPanelTab) {
                self.rightPanelIcon(clickedIcon);
                if(clickedIcon === "pencil" && self.editPanelContent() === "editcontent"){
                    setTileHightlighted(self.lastHighlightWigetIndex);
                }
            } else if (_closeRightPanel) {
                self.rightPanelIcon("none");
                self.toggleLeftPanel();
                if ("NORMAL" !== self.$b().dashboard.type() || self.$b().dashboard.systemDashboard()) {
                    self.completelyHidden(true);
                }
            } else {
                self.rightPanelIcon(clickedIcon);
                self.toggleLeftPanel();
                if(clickedIcon === "pencil" && self.editPanelContent() === "editcontent"){
                    setTileHightlighted(self.lastHighlightWigetIndex);
                }
            }
            self.resetEmptyDashboardName && self.resetEmptyDashboardName(); //reset empty dashboard name when user toggle right panel
            $b.triggerBuilderResizeEvent('show right panel');
        };

        self.toggleLeftPanel = function () {
            if (!self.showRightPanel()) {
                $(".dbd-left-panel").animate({width: "320px"}, "normal");
                $(".right-panel-toggler").animate({right: (323 + self.scrollbarWidth) + 'px'}, 'normal', function () {
                    self.showRightPanel(true);
                    self.initializeRightPanel(true);
                    $(".dashboard-picker-container:visible").addClass("df-collaps");
                    self.$b().triggerBuilderResizeEvent('show right panel');
                });
            } else {
                $(".dbd-left-panel").animate({width: 0});
                $(".right-panel-toggler").animate({right: self.scrollbarWidth + 3 + 'px'}, 'normal', function () {
                    self.expandDBEditor(true);
                    self.showRightPanel(false);
                    self.initDraggable();
                    $(".dashboard-picker-container:visible").removeClass("df-collaps");
                    self.$b().triggerBuilderResizeEvent('hide right panel');
                });
            }
        };

        self.switchEditPanelContent = function (data, event) {
            var koData = ko.dataFor(event.currentTarget);
            if ($(event.currentTarget).hasClass('edit-dsb-link')) {
                self.editPanelContent("editdashboard");
                self.expandDBEditor("singleDashboard-edit", true);
            } else if($(event.currentTarget).hasClass('edit-content-link')){
                self.editPanelContent("editcontent");
                $('.dbd-right-panel-editcontent-title').ojCollapsible("option", "expanded", true);
                selectedContent && selectedContent(koData);
            } else if ($(event.currentTarget).hasClass('edit-dsbset-link')) {
                self.editPanelContent("editset");
                self.expandDBEditor("dashboardset-edit", true);
            } else {
                self.editPanelContent("settings");
            }
            self.resetEmptyDashboardName && self.resetEmptyDashboardName(); //reset empty dashboard name when user click back icon or other settings
            self.$b().triggerBuilderResizeEvent('OOB dashboard detected and hide left panel');
        };

        self.editPanelContent.subscribe(function () {
            if (self.editPanelContent() !== "editcontent") {
               resetTileHighlighted();
            }
        });

        self.completelyHidden.subscribe(function () {
            resetTileHighlighted();
        });

        function resetTileHighlighted() {
            if(self.$b && self.$b().dashboard && self.$b().dashboard.tiles){
                var tilesArray = self.$b().dashboard.tiles();
                tilesArray.forEach(function resetObject(element, index) {
                    if (element.outlineHightlight() === true) {
                        self.lastHighlightWigetIndex = index;
                    }
                    element.outlineHightlight(false);
                });
            }
        }

        function setTileHightlighted(targetIndex) {
            var tilesArray = self.$b().dashboard.tiles();
            tilesArray.forEach(function resetObject(element, index) {
                if (index === targetIndex) {
                    element.outlineHightlight(true);
                }
            });
        }


        function rightPanelChange(status,param,isShowHideTitleChange) {
            if(status==="complete-hidden-rightpanel"){
                self.completelyHidden(true);
                self.$b().triggerBuilderResizeEvent('hide right panel');
            }else{
                if (!self.initializeRightPanel() && !isShowHideTitleChange) {
                    self.initializeRightPanel(true);
                }
                self.editRightpanelLinkage(status,param,isShowHideTitleChange);
            }          
        }
        
        Builder.registerFunction(rightPanelChange, 'rightPanelChange');
        
        self.initDraggable = function() {
                self.initWidgetDraggable();
            };
            
        self.initWidgetDraggable = function () {
            $(".dbd-left-panel-widget-text").draggable({
                helper: "clone",
                scroll: false,
                start: function (e, t) {
                    self.$b().triggerEvent(self.$b().EVENT_NEW_WIDGET_START_DRAGGING, null, e, t);
                },
                drag: function (e, t) {
                    self.$b().triggerEvent(self.$b().EVENT_NEW_WIDGET_DRAGGING, null, e, t);
                },
                stop: function (e, t) {
                    self.$b().triggerEvent(self.$b().EVENT_NEW_WIDGET_STOP_DRAGGING, null, e, t);
                }
            });
        };

        self.initializeCollapsible = function(){
            $('.dbd-right-panel-editdashboard-filters').ojCollapsible({"expanded": false});
            $('.dbd-right-panel-editdashboard-share').ojCollapsible({"expanded": false});
            $('.dbd-right-panel-editdashboard-general').ojCollapsible({"expanded": false});

            $('.dbd-right-panel-editdashboard-general').on({
                "ojexpand": function (event, ui) {
                    $('.dbd-right-panel-editdashboard-filters').ojCollapsible("option", "expanded", false);
                    $('.dbd-right-panel-editdashboard-share').ojCollapsible("option", "expanded", false);
                }
            });

            $('.dbd-right-panel-editdashboard-set-general').on({
                "ojexpand": function (event, ui) {
                    $('.dbd-right-panel-editdashboard-set-share').ojCollapsible("option", "expanded", false);
                }
            });

            $('.dbd-right-panel-editdashboard-filters').on({
                "ojexpand": function (event, ui) {
                    $('.dbd-right-panel-editdashboard-general').ojCollapsible("option", "expanded", false);
                    $('.dbd-right-panel-editdashboard-share').ojCollapsible("option", "expanded", false);
                }
            });

            $('.dbd-right-panel-editdashboard-share').on({
                "ojexpand": function (event, ui) {
                    $('.dbd-right-panel-editdashboard-filters').ojCollapsible("option", "expanded", false);
                    $('.dbd-right-panel-editdashboard-general').ojCollapsible("option", "expanded", false);
                }
            });

        $('.dbd-right-panel-editdashboard-set-share').on({
            "ojexpand": function (event, ui) {
                $('.dbd-right-panel-editdashboard-set-general').ojCollapsible("option", "expanded", false);
            }
        });

        $('.dbd-right-panel-editcontent-title').ojCollapsible({"expanded": false});
        $('.dbd-right-panel-editcontent-filters').ojCollapsible({"expanded": false});

        $('.dbd-right-panel-editcontent-filters').on({
            "ojexpand": function (event, ui) {
                $('.dbd-right-panel-editcontent-title').ojCollapsible("option", "expanded", false);
            }
        });

        $('.dbd-right-panel-editcontent-title').on({
            "ojexpand": function (event, ui) {
                $('.dbd-right-panel-editcontent-filters').ojCollapsible("option", "expanded", false);
            }
        });

        };
    }
    return {"rightPanelControl": rightPanelControl};
}
);

