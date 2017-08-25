define([
//    'dashboards/dbsmodel',
    'knockout',
    'jquery',
    'dfutil',
    'uifwk/js/util/logging-util',
    'idfbcutil',
    'ojs/ojcore',
    'jqueryui',
//    'builder/builder.jet.partition',
    'builder/builder.core',
    'builder/right.panel.model',
    'builder/builder.functions',
    'builder/dashboard.tile.model',
    'builder/dashboard.tile.view',
    'builder/tool.bar.model',
    'builder/integrate/builder.integrate',
    'dashboards/dbstypeahead',
    'builder/integrate/dashboard.widget.api',
    'builder/dashboardDataSource/dashboard.datasource'],
    function (/*model, */ko, $, dfu, _emJETCustomLogger, idfbcutil, oj) {

        function DashboardsetPanelsModel(dashboardsetToolBarModel) {
            if (!dashboardsetToolBarModel) {
                throw "dashboardsetToolBarModel must be sent to the DashboardsetPanelsModel constructor";
            }

            var self = this;

            var dashboardInstMap =dashboardsetToolBarModel.dashboardInstMap = {};
            var options = {"autoRefreshInterval":dashboardsetToolBarModel.autoRefreshInterval};
            var hasPopDialogBinded = false;

            window.selectedDashboardInst = self.selectedDashboardInst = ko.observable(null);

            self.windowWidth=ko.observable($(window).width());
            self.windowHeight=ko.observable($(window).height());

            self.rightPanelModel = null;
            self.loadRightPanelModel = function (toolBarModel, tilesViewModel, $b) {
                if (self.rightPanelModel) {
                    self.rightPanelModel.$b = $b;
                    self.rightPanelModel.loadToolBarModel(toolBarModel,$b);
                    self.rightPanelModel.loadTilesViewModel(tilesViewModel);
                } else {
                    var rightPanelModel = new Builder.RightPanelModel($b, tilesViewModel, toolBarModel, dashboardsetToolBarModel);
                    ko.applyBindings(rightPanelModel, $('.df-right-panel')[0]);
                    self.rightPanelModel = rightPanelModel;
                }
                self.rightPanelModel.initialize();
            };
            
            self.showDashboard = function (dashboardsetToolBarModel) {
                document.activeElement && document.activeElement.blur();//to blur the focused item on another tab
                var dashboardItem=dashboardsetToolBarModel.selectedDashboardItem(),               
                    dashboardId = dashboardItem.dashboardId,
                    divId = "dashboard-" + dashboardId,
                    $showDashboard = $("#" + divId),
                    alreadyLoaded = $("#" + divId).length > 0;
                
                //hide the right panel
                if (self.rightPanelModel) {
                    //resize right panel before shown
                    Builder.rightPanelChange("complete-hidden-rightpanel");                 
                    $(".dashboard-picker-container").removeClass("df-collaps");
                }
                
                var headerWrapper = $("#headerWrapper")[0];
                if(headerWrapper) {
                    var headerViewModel = ko.dataFor(headerWrapper);
                }
                
                if(dashboardsetToolBarModel.isDashboardSet()) {
                    headerViewModel && headerViewModel.brandingbarParams.showGlobalContextBanner(false);
                    headerViewModel && headerViewModel.brandingbarParams.showTimeSelector(false);
                    headerViewModel && headerViewModel.brandingbarParams.showEntitySelector(false);
                }else {
                    if(dashboardsetToolBarModel.dashboardInst.enableEntityFilter()==="GC" || dashboardsetToolBarModel.dashboardInst.enableTimeRange()!=="FALSE") {    
                        headerViewModel && headerViewModel.brandingbarParams.showGlobalContextBanner(true);
                        if(dashboardsetToolBarModel.dashboardInst.enableTimeRange()==="FALSE") {
                            headerViewModel && headerViewModel.brandingbarParams.showTimeSelector(false);
                        }else {
                            headerViewModel && headerViewModel.brandingbarParams.showTimeSelector(true);
                        }
                        if(dashboardsetToolBarModel.dashboardInst.enableEntityFilter()==="FALSE" || dashboardsetToolBarModel.dashboardInst.enableEntityFilter()==="TRUE") {
                            headerViewModel && headerViewModel.brandingbarParams.showEntitySelector(false);
                        }else {
                            headerViewModel && headerViewModel.brandingbarParams.showEntitySelector(true);
                        }
                    }else {
                        headerViewModel && headerViewModel.brandingbarParams.showGlobalContextBanner(false);
                        headerViewModel && headerViewModel.brandingbarParams.showTimeSelector(false);
                        headerViewModel && headerViewModel.brandingbarParams.showEntitySelector(false);
                    }
                }

                if (alreadyLoaded) {
                    $showDashboard.show();
                    self.selectedDashboardInst(dashboardInstMap[dashboardId]);
                    var _isIncludingDbsHome=self.selectedDashboardInst().type === "new";
                    if (!_isIncludingDbsHome) {
                        resetContainerScroll();
                        var _dashboard = self.selectedDashboardInst();
                        var $b = _dashboard.$b;
                        var tilesView = $b.getDashboardTilesView();
                        tilesView.disableMovingTransition();
                        $b.triggerEvent($b.EVENT_DASHBOARD_INIT_FIRST_RESIZE, "Init first resize when tab selected");
                        $b.triggerBuilderResizeEvent("Resize and re-render after tab selected");
                        tilesView.enableMovingTransition();
                        setTimeout(function() {
                            $(window).trigger("resize");
                        }, 200);
                        self.loadRightPanelModel(_dashboard.toolBarModel,_dashboard.tilesViewModel,_dashboard.$b);
                    }else{
                        var $target =$('#dashboard-'+dashboardsetToolBarModel.selectedDashboardItem().dashboardId);
                        homeScrollbarReset($target);
                        var $b = new Builder.DashboardBuilder(dashboardsetToolBarModel.dashboardInst, $($("#dashboard-content-template").text()));
                        self.loadRightPanelModel(null, null, $b);
                    }
                } else {
                    var _isIncludingDbsHome=dashboardItem.type === "new";
                    if (_isIncludingDbsHome) {
                        self.includingDashboard(dashboardId);
                        //new dashboard home css change:align
                        setTimeout(function () {
                            var $target = $('#dashboard-' + dashboardsetToolBarModel.selectedDashboardItem().dashboardId);
                            homeScrollbarReset($target);
                        }, 2000);
                        var $b = new Builder.DashboardBuilder(dashboardsetToolBarModel.dashboardInst, $($("#dashboard-content-template").text()));
                        self.loadRightPanelModel(null, null, $b);
                    } else {
                        resetContainerScroll();
                        self.loadDashboard(dashboardsetToolBarModel);
                    }
                }
            };

            self.includingDashboard = function (guid) {
                require(['dashboards/dashboardhome-impl'], function(dashboardhome_impl) {
                    if (!ko.components.isRegistered('df-oracle-dashboard-list')) {
                        ko.components.register("df-oracle-dashboard-list",{
                            viewModel:dashboardhome_impl,
                            template:{require:'text!/emsaasui/emcpdfui/@version@/html/dashboardhome.html'}
                        });
                    }

                    var $includingEl = $($("#dashboard-include-template").text());
                    $("#dashboards-tabs-contents").append($includingEl);
                    $includingEl.attr("id", "dashboard-" + guid);


                    function init() {
                        require(['dashboards/dbsmodel'], function(model) {
                            var dashboardsViewModle = new model.ViewModel(null, "dashboard-" + guid , ['Me','Oracle','NORMAL','Share','ShowAll'], dashboardsetToolBarModel.reorderedDbsSetItems, true);

                            dashboardsViewModle.showExploreDataBtn(false);

                            dashboardsViewModle.handleDashboardClicked = function(event, data) {
                                if(event){
                                    event.preventDefault();
                                }
                                var hasDuplicatedDashboard = false;
                                var isCreator=dashboardsetToolBarModel.dashboardsetConfig.isCreator();
                                var dataId;
                                var dataName;
                                    if(typeof(data.dashboard)!=='undefined'){
                                       dataId= data.dashboard.id;
                                       dataName=data.dashboard.name;
                                    }else{
                                       dataId= data.id;
                                       dataName=data.name;
                                    }

                                    if (!isCreator) {
                                        dfu.showMessage({
                                            type: 'warn',
                                            summary: oj.Translations.getTranslatedString("DBS_BUILDER_DASHBOARD_CANNOT_SELECT_DASHBOARD"),
                                            detail: '',
                                            removeDelayTime: 5000});
                                    }

                                dashboardsetToolBarModel.dashboardsetItems.forEach(function(dashboardItem) {
                                    if (dashboardItem.dashboardId === dataId) {
                                        hasDuplicatedDashboard = true;
                                            dfu.showMessage({
                                                type: 'warn',
                                                summary: oj.Translations.getTranslatedString("DBS_BUILDER_DASHBOARD_SET_DUPLICATED_DASHBOARD", dataName),
                                                detail: '',
                                                removeDelayTime: 5000});
                                        }
                                });

                                if (!hasDuplicatedDashboard && isCreator) {
                                    dashboardsetToolBarModel.pickDashboard(selectedDashboardInst().guid, {
                                        id: ko.observable(dataId),
                                        name: ko.observable(dataName)
                                    });
                                }
                            };

                            dashboardsViewModle.afterConfirmDashboardCreate = function(_model, _resp, _options) {
                                var __data = {dashboard: {id : _model.get("id"), name: _model.get("name")}};
                                dashboardsViewModle.handleDashboardClicked(null, __data);
                                $('#dashboardTab-'+__data.dashboard.id).find('.tabs-name').text(__data.dashboard.name);
                            };
                            ko.applyBindings(dashboardsViewModle, $includingEl[0]);
                        });
                    };

                    var dashboardInst = {
                        type: "new",
                        guid:guid
                    };
                    dashboardInstMap[guid] = dashboardInst;
                    self.selectedDashboardInst(dashboardInst);

                    init();
                });
            };

            self.loadDashboard = function (dashboardsetToolBarModel) {
                $("#loading").show();
                var dashboardItem = dashboardsetToolBarModel.selectedDashboardItem(),
                    dashboardId = dashboardItem.dashboardId;
            
                new Builder.DashboardDataSource().loadDashboardData(dashboardId, function (dashboard) {
                    initializeSingleDashboard(dashboard, dashboardId);
                }, function (e) {
                    $("#loading").hide();
                    console.log(e.errorMessage());
                    if (e.errorCode && e.errorCode() === 20001) {
                        oj.Logger.error("Dashboard not found. Redirect to dashboard error page", true);
                        window.location.href = "./error.html?invalidUrl=" + encodeURIComponent(window.location.href);
                    }
                });
            };

            self.hideAllDashboards = function () {
                $(".dashboard-content").hide();
            };
            
            function initDashboard() {
                self.hideAllDashboards();
                dashboardsetToolBarModel.selectedDashboardItem() && 
                        self.showDashboard(dashboardsetToolBarModel);
            }
            
            function initializeSingleDashboard(dashboard,dsbId){
                var $dashboardEl = $($("#dashboard-content-template").text());
                    $("#dashboards-tabs-contents").append($dashboardEl);
                    $dashboardEl.attr("id", "dashboard-" + dsbId);

                    var $b = new Builder.DashboardBuilder(dashboard, $dashboardEl);
                    var tilesView = new Builder.DashboardTilesView($b);
                    /*var wrapperEleemnt = $b.find('.dbd-tile-widget-wrapper');
                    if (wrapperEleemnt) {
                        var el = $($("#dashboard-tile-widget-template").text());
                        el.appendTo(wrapperEleemnt[0]);
                    }*/

                    var normalMode = new Builder.NormalEditorMode();
                    var tabletMode = new Builder.TabletEditorMode();
                    var mode = Builder.isSmallMediaQuery() ? tabletMode : normalMode;
                    var timeSelectorModel = new Builder.TimeSelectorModel();
                    var targets = ko.observable({"criteria":"{\"version\":\"1.0\",\"criteriaList\":[]}"});

                $.when(Builder.eagerLoadDahshboardTilesAtPageLoad(dfu, ko, normalMode, tabletMode, mode, dashboardsetToolBarModel.isDashboardSet(), timeSelectorModel, targets)).done(function() {
                    var tilesViewModel = new Builder.DashboardTilesViewModel($b, dashboardsetToolBarModel.dashboardInst/*, tilesView, urlChangeView*/);
                    var toolBarModel = new Builder.ToolBarModel($b, options);
                    tilesViewModel.toolbarModel = toolBarModel;
                        
                    //change dashboard name
                    toolBarModel.dashboardName.subscribe(function (dashboardName) {
                        var currentDashboardId = self.selectedDashboardInst().toolBarModel.dashboardId;
                        dashboardsetToolBarModel.dashboardsetItems.filter(function(value) {
                            if(value.dashboardId===currentDashboardId){
                                value.name(dashboardName);
                                $('#dashboardTab-'+currentDashboardId).find('.tabs-name').text(dashboardName);
                                $('#dashboardTab-'+currentDashboardId).attr("data-tabs-name",dashboardName);
                                $('#dashboardTab-'+currentDashboardId).attr("data-dashboard-name-in-set",dashboardName);
                            }
                        });
                        dashboardsetToolBarModel.reorderedDbsSetItems().filter(function(value) {
                            if(value.dashboardId===currentDashboardId){
                                value.name(dashboardName);
                            }
                        });
                    });
                    
                    if (dashboard.tiles && dashboard.tiles()) {
                        for (var i = 0; i < dashboard.tiles().length; i++) {
                            var tile = dashboard.tiles()[i];
                            console.log('Initialize tile before getting its configuration');
                            Builder.initializeTileAfterLoad(tilesViewModel.editor.mode, dashboard, tile, tilesViewModel.timeSelectorModel, tilesViewModel.targets, true, dashboardsetToolBarModel.dashboardInst);
                            Builder.getTileConfigure(tilesViewModel.editor.mode, dashboard, tile, tilesViewModel.timeSelectorModel, tilesViewModel.targets, dashboardsetToolBarModel.dashboardInst);
                        }
                    }

                    ko.bindingHandlers.sortableList = {
                        init: function (element, valueAccessor) {
                            var list = valueAccessor();
                            tilesView.enableSortable(element, list);
                        }
                    };
                    ko.bindingHandlers.stopBinding = {
                        init: function () {
                            return {controlsDescendantBindings: true};
                        }
                    };
                    ko.bindingHandlers.enterpress = {
                        init: function (element, valueAccessor, allBindingsAccessor, viewModel) {
                            var allBindings = allBindingsAccessor();
                            $(element).keypress(function (event) {
                                var keyCode = (event.which ? event.which : event.keyCode);
                                if (keyCode === 13) {
                                    allBindings.enterpress.call(viewModel);
                                    return false;
                                }
                                return true;
                            });
                        }
                    };
                    ko.virtualElements.allowedBindings.stopBinding = true;

                    ko.applyBindings(toolBarModel, $dashboardEl.find('.head-bar-container')[0]);
                    tilesViewModel.initialize();

                    dashboardInstMap[dsbId] = {
                        type: "included",
                        $b: $b,
                        toolBarModel: toolBarModel,
                        tilesViewModel: tilesViewModel,
                        dashboardsetToolBar:dashboardsetToolBarModel,
                        dashboardSets: ko.observable(null),
                        linkedDashboardList : ko.observable(null)
                    };
                    self.selectedDashboardInst(dashboardInstMap[dsbId]);

                    ko.applyBindings(tilesViewModel, $dashboardEl.find('.dashboard-content-main')[0]);

                    self.loadRightPanelModel(toolBarModel,tilesViewModel,$b);

                    $("#loading").hide();
                    $('#globalBody').show();
                    $dashboardEl.css("visibility", "visible");
                    if (dashboardsetToolBarModel.isDashboardSet()) {
                        $b.findEl('.head-bar-container').css("border-bottom", "0");

                        $b.addEventListener($b.EVENT_DASHBOARD_OPTION_MENU_RENDERED, function(){
                        //hide some drop-down menu options
                        $b.findEl('.dropdown-menu>li').each(function (index, element) {
                            if (($(element).attr('data-singledb-option') !== 'Edit') && ($(element).attr('data-singledb-option') !== 'Print') && ($(element).attr('data-singledb-option') !== 'Duplicate')) {
                                $(element).css({display: "none"});
                            }
                        });

                        // hide the options button if there are no menu items in the menu.
                        var allMenusHidden = true;
                        $b.findEl('.dropdown-menu li').each(function () {
                            if ($(this).css("display") !== 'none') {
                                allMenusHidden = false;
                            }
                        });
                        if (allMenusHidden) {
                            $b.findEl(".dashboardOptsBtn").hide();
                        }
                    });
                    }

                    tilesView.enableDraggable();
                    tilesViewModel.show();

                    $b.triggerEvent($b.EVENT_POST_DOCUMENT_SHOW);
                    tilesView.enableMovingTransition();
                    idfbcutil.hookupBrowserCloseEvent(function () {
                        oj.Logger.warn("Dashboard: [id=" + dashboard.id() + ", name=" + dashboard.name() + "] is closed", true, false);          
                    });

                    $("#loading").hide();
                });
                    /*
                     * Code to test df_util_widget_lookup_assetRootUrl
                     var testvalue = df_util_widget_lookup_assetRootUrl('SavedSearch','0.1','search');
                     console.log('value for asetRootUrl(search) is ' + testvalue + ', and the expected value is + http://slc08upg.us.oracle.com:7001/savedsearch/v1/search');
                     */                
            }

            dashboardsetToolBarModel.selectedDashboardItem.subscribe(initDashboard);

            initDashboard();

            //resize function
            $(window).resize(function () {
                if (self.windowWidth() === $(window).width()) {
                    self.windowHeight($(window).height());
                } else {
                    self.windowWidth($(window).width());
                }
            });

            self.windowWidth.extend({rateLimit: 200, method: 'notifyWhenChangesStop '});
            self.windowHeight.extend({rateLimit: 200, method: 'notifyWhenChangesStop '});

            self.windowHeight.subscribe(function () {
                windowResizeProcess();
            });
            self.windowWidth.subscribe(function () {
                windowResizeProcess();
            });

            function windowResizeProcess() {
                if ($('.dbs-list-container').length !== 0 && self.selectedDashboardInst().type === 'new') {
                    var $target = $('.dashboard-picker-container:visible');
                    homeScrollbarReset($target);
                    self.rightPanelModel.$b.triggerBuilderResizeEvent();
                } else if (self.selectedDashboardInst() && self.selectedDashboardInst().type === 'included') {
                    self.selectedDashboardInst().tilesViewModel.notifyWindowResize();
                    self.selectedDashboardInst().$b.triggerBuilderResizeEvent();
                }
            }
            function homeScrollbarReset(target) {
                var bodyHeight = $(window).height(),
                    titleToolbarHeight = target.position().top,
                    newHeight = Number(bodyHeight) - Number(titleToolbarHeight),
                    targetContainer = target.closest('#dashboards-tabs-contents');
                targetContainer.css({'height': newHeight});
                targetContainer.css({'overflow-y': 'scroll'});
            }
            function resetContainerScroll() {
                var $container = $('#dashboards-tabs-contents');
                $container.removeAttr("style");
            }
            
            //binding pop-up dialog
            function startBindingPopupDialog() {
                if(!hasPopDialogBinded){
                   ko.applyBindings(self, document.getElementById('popUp-dialog'));
                   hasPopDialogBinded=true;
                }             
            }

            Builder.registerFunction(startBindingPopupDialog, 'startBindingPopupDialog');
        };
        Builder.registerModule(DashboardsetPanelsModel, 'DashboardsetPanelsModel');
        return DashboardsetPanelsModel;
    }
);
