define(['dashboards/dbsmodel',
    'knockout',
    'ojs/ojcore',
    'jquery',
    'dfutil'
], function (dbsmodel, ko, oj, $, dfu) {

    function rightPanelEditContentModel($b, dashboardsetToolBarModel, selectedContent) {
        var self = this;
        self.$b = ko.observable($b);
        self.dashboardsetToolBarModel=dashboardsetToolBarModel;
        self.isDashboardSet = self.dashboardsetToolBarModel.isDashboardSet;
        self.dashboardTilesViewModel = $b.getDashboardTilesViewModel && $b.getDashboardTilesViewModel();
        self.dashboard = ko.computed(function () {
            return self.$b && self.$b().dashboard;
        });
        self.toolbarModel = ko.computed(function () {
            return self.$b && self.$b().getToolBarModel && self.$b().getToolBarModel();
        });
        self.isChangeDbdInSet = ko.observable(false);

        self.$b.subscribe(function () {
            self.isChangeDbdInSet(true);
        });

//        var predataModel = new dbsmodel.PredataModel();
//        var dashboardsViewModel = new dbsmodel.ViewModel(predataModel, "mainContent");
        self.dashboards = ko.observableArray();
        self.selectedDashboardId = ko.observable();
        self.allDashboards = ko.observableArray();
        self.LOADDASHBOARDLIMIT = 60;
        var loadDashboardReqSent = false;
        var totalResults = 0;
        var currentQueryStr = '';
        var currentPageNo = 1;
        
        function resetPaging(){
            totalResults = 0;
            currentPageNo = 1;
            loadDashboardReqSent = false;
            self.allDashboards.removeAll();
            self.dashboards.removeAll();
        }
        
        function loadDashboardList(sucCallback, queryStr, page){
            if(loadDashboardReqSent){
                return;
            }
            page = page || currentPageNo;
            if(currentQueryStr && !queryStr){
                queryStr = currentQueryStr;
            }
            var offset = (page-1)*self.LOADDASHBOARDLIMIT;
            var serviceUrl = "/sso.static/dashboards.service?limit=" + self.LOADDASHBOARDLIMIT + "&offset=" + offset + "&orderBy=default";
            if (dfu.isDevMode()) {
                var serviceUrl = dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint,"dashboards?limit=" + self.LOADDASHBOARDLIMIT + "&offset=" + offset + "&orderBy=default");
            }
            loadDashboardReqSent = true;
            dfu.ajaxWithRetry({
                            url: queryStr? serviceUrl + '&queryString=' + queryStr : serviceUrl,
                            headers: dfu.getDashboardsRequestHeader(),
                            contentType:'application/json',
                            success: function(data, textStatus) {
                                if(!loadDashboardReqSent){
                                    return;
                                }
                                totalResults = data.totalResults;
                                if(page === 1){
                                    !queryStr && self.allDashboards(data.dashboards);
                                    self.dashboards(data.dashboards.slice(0));
                                }else{
                                    if(!queryStr){
                                        self.allDashboards(self.allDashboards().concat(data.dashboards));
                                    }
                                    self.dashboards(self.dashboards().concat(data.dashboards.slice(0)));
                                }
                                loadDashboardReqSent = false;
                                sucCallback && sucCallback();
                            },
                            error: function(xhr, textStatus, errorThrown){
                                oj.Logger.error('Failed to get service instances by URL: '+serviceUrl);
                                loadDashboardReqSent = false;
                            },
                            async: page===1?false:true
                        });
        }
        

        function loadSingleDashboard(sucCallback, dashboardId){
            var singleServiceUrl = "/sso.static/dashboards.service/";
            if (dfu.isDevMode()) {
                var singleServiceUrl = dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint,"dashboards/");
            }
            dfu.ajaxWithRetry({
                            url: singleServiceUrl + dashboardId,
                            headers: dfu.getDashboardsRequestHeader(),
                            contentType:'application/json',
                            success: function(data, textStatus) {
                                sucCallback(data);
                            },
                            error: function(xhr, textStatus, errorThrown){
                                oj.Logger.error('Failed to get service instances by URL: '+singleServiceUrl);
                            },
                            async: false
                        });
        }

        self.addToTitleAbled = ko.observable(false);
        self.selectedContent = selectedContent;
        self.hideTitle = self.selectedContent()?ko.observable(("true" === self.selectedContent().hideTitle()||true === self.selectedContent().hideTitle())? ["hideTitle"]:[]):ko.observable([]);
//        self.hideTitle = ko.observable([]);
        self.prevContentHideTitleSubscription = null;
        self.hasLinkedToTitle = ko.observable(false);
        self.selectedContent.subscribe(function(tile){
            if(self.prevContentHideTitleSubscription){
                self.prevContentHideTitleSubscription.dispose();
            }
            if(!tile){
                return;
            }
            self.dashboardTilesViewModel = ko.dataFor($(".tiles-wrapper:visible")[0]);
            resetAddLinkToTitle();
//            self.hideTitle("true" === tile.hideTitle()?["hideTitle"]:[]);
            self.hideTitle(("true" === self.selectedContent().hideTitle()||true === self.selectedContent().hideTitle())? ["hideTitle"]:[]);
            self.prevContentHideTitleSubscription = tile.hideTitle.subscribe(function(val){
                if(val === "true"){
                    self.hideTitle(["hideTitle"]);
                    resetAddLinkToTitle(true);
                }else if(val === "false"){
                    self.hideTitle([]);
                    $("input.dashboard-search-input").ojInputText({"disabled": false});
                }
            });
            tile.outlineHightlight(true);
            if(tile.WIDGET_LINKED_DASHBOARD && tile.WIDGET_LINKED_DASHBOARD()){
                self.selectedDashboardId(tile.WIDGET_LINKED_DASHBOARD());
                loadSingleDashboard(function(dsbData){
                    self.selectedDashboard(dsbData);
                    self.hasLinkedToTitle(true);
                },self.selectedDashboardId());
            }
        });






        self.onContentSearching = ko.observable(false);
        var initialed = false;
        self.onContentSearching.subscribe(function(val){
            if(val){
                if(!initialed){
                    var lastTime = 0;
                    var delayTime = 700;
                    $('.search-content-dropdown-list-container>div').scroll(function(){
                        var currentTime = +new Date();
                        if (currentTime - lastTime > delayTime){
                            var indexOfFirstItemOnFormerPage = (currentPageNo - 1) * self.LOADDASHBOARDLIMIT + 1;
                            if ($('.search-content-dropdown-list-container>div').scrollTop() + $('.search-content-dropdown-list-container>div').height() >= $('#search-content-dropdown-list li:eq(' + indexOfFirstItemOnFormerPage + ')').position().top) {
                                var loadedDashboardsNum = self.dashboards().length;
                                var nextPageNo = currentPageNo + 1;
                                if (loadedDashboardsNum < totalResults) {
                                    loadDashboardList(function () {
                                        currentPageNo = nextPageNo;
                                    }, null, nextPageNo);
                                }
                            }
                          lastTime = currentTime ;
                        }
                    });
                    initialed = true;
                }
                
                if(!self.allDashboards() || self.allDashboards().length === 0){
                    !loadDashboardReqSent && loadDashboardList(function(){
                        $(".search-content-dropdown-list-container ul").ojListView( "refresh" );
                    });
                }else{
                    $(".search-content-dropdown-list-container ul").ojListView( "refresh" );
                }
            }else{
                $(".search-content-dropdown-list-container ul").ojListView({"selection":[]});
            }
        });


        self.dataSource = new oj.ArrayTableDataSource(self.dashboards, {idAttribute: "id"});
        self.keyword = ko.observable("");
        
        self.keywordInput = ko.observable("");
        self.keywordInput.subscribe(function(val){
            if(!val){
                self.clearContentSearch(false);
            }else{
                self.clearContentSearch(true);
            }
        });
        self.keywordInputComputed = ko.computed(function(){
            return self.keywordInput();
        });
        self.keywordInputComputed.extend({ rateLimit: 800 });
        self.keywordInputComputed.subscribe(function(val){
            self.autoSearchDashboard(val?val:"");
        });
        
        self.selectedDashboardLastModifiedTime = ko.observable();
        self.selectedDashboard = ko.observable();
        self.selectedDashboard.subscribe(function(dashboard){
            if(dashboard){
                var time = new Date(dashboard.lastModifiedOn);
                var month = (time.getMonth()+1)<10?"0"+(time.getMonth()+1):(time.getMonth()+1);
                var date = time.getDate()<10?"0"+time.getDate():time.getDate();
                self.selectedDashboardLastModifiedTime(month + '/' + date + '/' + time.getFullYear());
            }
        });
        self.showSelectedDashboard = ko.observable(false);
        self.logSelected = function(event, ui)
        {
            if (ui.option === 'selection')
            {
                self.selectedDashboardId(ui.value[0]);
                self.dashboards().forEach(function(dashboard){
                    if(self.selectedDashboardId() === dashboard.id){
                        self.selectedDashboard(dashboard);
                        self.keyword(dashboard.name);
                        self.showSelectedDashboard(true);
                        self.addToTitleAbled(true);
                        self.clearContentSearch(true);
                    }
                });
            }
        };

        self.searchDashboardClicked = function(data, event){
            self.onContentSearching(true);
            event.stopPropagation();
        };
        
        if(!self.closeSearchListOnClickingOut){
            self.closeSearchListOnClickingOut = document.addEventListener("click",function(_evnt){
                if (true===self.onContentSearching()) {
                    self.onContentSearching(false);
                }
            });
        }
        
        self.clearContentSearch = ko.observable();
        self.autoSearchDashboard = function (searchTerm) {
            currentQueryStr = searchTerm && searchTerm.toLowerCase().trim();
            resetPaging();
            self.dashboards.removeAll();
            if(searchTerm !== null){
                loadDashboardList(null, currentQueryStr);
            }
        };
        
        self.clearDashboardSearchInputClicked = function () {
            if (self.keyword()) {
                self.keyword("");
                self.autoSearchDashboard("");
                self.showSelectedDashboard(false);
                self.selectedDashboardId(null);
                self.addToTitleAbled(false);
                self.clearContentSearch(false);
            }
        };



        self.searchDashboardInputKeypressed = function (e, d) {
            if (d.keyCode === 13) {
                self.autoSearchDashboard(self.keyword()?self.keyword():"");
                return false;
            }
            return true;
        };
        
        self.addLinkToTitleClicked = function(e, d){
            if(!self.selectedContent().WIDGET_LINKED_DASHBOARD){
                self.selectedContent().WIDGET_LINKED_DASHBOARD = ko.observable();
            }
            self.selectedContent().WIDGET_LINKED_DASHBOARD(self.selectedDashboard()?self.selectedDashboard().id:null);
            self.hasLinkedToTitle(true);
            $b.triggerEvent($b.EVENT_TILE_LINK_CHANGED, null);
            saveDashboard();
        };

        self.removeLinkToTitleClicked = function(e, d){
            self.selectedContent().WIDGET_LINKED_DASHBOARD(null);
            saveDashboard();
            resetPaging();
            self.hasLinkedToTitle(false);
            resetAddLinkToTitle();
            $b.triggerEvent($b.EVENT_TILE_LINK_CHANGED, null);
        };

        function resetAddLinkToTitle(holdHasLinkedToTitle){
            self.keyword("");
            self.autoSearchDashboard(null);
            self.showSelectedDashboard(false);
            self.selectedDashboardId(null);
            self.addToTitleAbled(false);
            self.clearContentSearch(false);
            if(!holdHasLinkedToTitle){
                self.hasLinkedToTitle(false);
            }
            $(".search-content-dropdown-list-container #listview li").removeClass("oj-selected");
        }

        function saveDashboard(){
            self.toolbarModel().handleSaveUpdateToServer(function(){
                self.toolbarModel().isUpdated(false);
            }, function(error){
//                if (error && error.errorCode && error.errorCode() === 10002) {
//                    dfu.showMessage({type: 'error', summary: getNlsString('DBS_BUILDER_MSG_ERROR_IN_SAVING_TEXT_WIDGET_EMPTY_CONTENT'), detail: '', removeDelayTime: 5000});
//                } else 
                if (error && error.errorCode && error.errorCode() === 10003) {
                    dfu.showMessage({type: 'error', summary: getNlsString('DBS_BUILDER_MSG_ERROR_IN_SAVING_TEXT_WIDGET_TOO_LONG_CONTENT'), detail: '', removeDelayTime: 5000});

                } else {
                    error && error.errorMessage() && dfu.showMessage({type: 'error', summary: getNlsString('DBS_BUILDER_MSG_ERROR_IN_SAVING'), detail: '', removeDelayTime: 5000});
                }
            });
        }
    }
    return {'rightPanelEditContentModel': rightPanelEditContentModel};
});