/**
 * @preserve Copyright (c) 2014, Oracle and/or its affiliates.
 * All rights reserved.
 */

define([
    'dashboards/datasourcefactory',
    'dashboards/dbstablesource',
    'dashboards/dbsfilter',
    'ojs/ojcore',
    'knockout',
    'jquery',
    'dfutil',
    'uifwk/js/util/preference-util',
    'uifwk/js/util/mobile-util',
	'uifwk/js/util/zdt-util',
    'uifwk/js/sdk/context-util',
    'ojs/ojknockout',
    'ojs/ojpagingcontrol',
    'ojs/ojpagingcontrol-model'
],
function(dsf, dts, dft, oj, ko, $, dfu, pfu, mbu, zdtUtilModel, cxtModel)
{
    var SHOW_WELCOME_PREF_KEY = "Dashboards.showWelcomeDialog",
            DASHBOARDS_FILTER_PREF_KEY = "Dashboards.dashboardsFilter",
            DASHBOARDS_VIEW_PREF_KEY = "Dashboards.dashboardsView",
            DASHBOARDS_REST_URL = dfu.getDashboardsUrl(),
            PREFERENCES_REST_URL = dfu.getPreferencesUrl(),
            SUBSCIBED_APPS_REST_URL = dfu.getSubscribedappsUrl();
    var cxtUtil = new cxtModel();
    var extPrefUtil = new pfu(PREFERENCES_REST_URL, dfu.getDashboardsRequestHeader());

    function createDashboardDialogModel() {
        var self = this;
        self.name = ko.observable(undefined);
        self.nameInputed = ko.observable(undefined); //read only input text
        self.description = ko.observable('');
        self.timeRangeFilterValue = ko.observable(["ON"]);//for now ON always and hide option in UI
        self.targetFilterValue = ko.observable(["OFF"]);
        self.selectType =  ko.observable("NORMAL");
        self.showHideDescription=ko.observable(false);
        self.singleVisible = ko.observable(true);
        self.setVisible=ko.observable(false);
        if(!$('#dbd-set-tabs')[0]){
            self.underSet=false;
        }else{
            self.underSet=true;
        }
        
        self.dashboardtypeSelectFuc=function(){
            if(self.selectType()==="NORMAL"){
                self.singleVisible(true);
                self.setVisible(false);
            }else{
                self.singleVisible(false);
                self.setVisible(true);
            }
            return true;
        };

        self.isDisabled = ko.computed(function() {
            if (self.nameInputed() && self.nameInputed().length > 0)
            {
                return false;
            }
            return true;
        });

        self.clear = function() {
            self.name(undefined);
            self.description('');
            self.timeRangeFilterValue(["ON"]);
            self.targetFilterValue(["OFF"]);
        };

        self.isEnableTimeRange = function() {
            if (self.timeRangeFilterValue()  === "ON" ||
                    self.timeRangeFilterValue()[0] === "ON")
            {
                return true;
            }
            return false;
        };

    }

    function confirmDialogModel(parentElementId, title, okLabel, message, okFunction) {
        var self = this;
        self.parentElementId = parentElementId;
        self.getElementByCss = function(cssSelector) {
            if (cssSelector && cssSelector !== null)
            {
                if (self.parentElementId && self.parentElementId.trim().length > 0)
                {
                    return $("#"+self.parentElementId+" "+cssSelector);
                }
                return $(cssSelector);
            }
            return null;
        };
        self.title = ko.observable(title || '');
        self.okLabel = ko.observable(okLabel || '');
        self.message = ko.observable(message || '');
        self.showCancel = ko.observable(false);

        self.okFunction = (okFunction && $.isFunction(okFunction)) ? okFunction : function() {};

        self.show = function (title, okLabel, message, okFunction, showCancel) {
            self.title(title || '');
            self.okLabel(okLabel || '');
            self.message(message || '');
            self.showCancel(showCancel || false);
            self.okFunction = function () {
                var _okfunc = (okFunction && $.isFunction(okFunction)) ? okFunction : function() {};
                _okfunc();
            };
            self.getElementByCss( ".dbs_cfmDialog" ).ojDialog( "open" );
        };

        self.close = function () {
            $( ".dbs_cfmDialog" ).ojDialog( "close" );
        };

        self.keydown = function (d, e) {
           if (e.keyCode === 13) {
             self.close();
           }
        };
    }

    function welcomeDialogModel(prefUtil, showWel) {
        var self = this;
        self.userName = dfu.getUserName();
        self.prefUtil = prefUtil;
        self.showWelcome = showWel;
        if (showWel === undefined)
        {
            self.showWelcome = true;
        }

        self.browseClicked = function() {
            $('#overviewDialog').ojDialog('close');
        };
        self.buildClicked = function() {
            $('#overviewDialog').ojDialog('close');
            $('#cbtn').focus();
        };
        self.exploreClicked = function() {
            $('#overviewDialog').ojDialog('close');
            $('#exploreDataBtn').focus();
        };
        self.gotClicked = function() {
            self.showWelcome = false;
            prefUtil.setPreference(SHOW_WELCOME_PREF_KEY, "false");
            $('#overviewDialog').ojDialog('close');
        };

    }
    
    function getUrlParam(name) {
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(window.location.search);
        return results === null ? "" : results[1];
    };

    function ViewModel(predata, parentElementId, defaultFilters, dashboardSetItem, isSet) {

        var self = this, showWelcome = false; //(predata === null ? false : predata.getShowWelcomePref());

        self.parentElementId = parentElementId;
        self.getElementByCss = function(cssSelector) {
            if (cssSelector && cssSelector !== null)
            {
                if (self.parentElementId && self.parentElementId.trim().length > 0)
                {
                    return $("#"+self.parentElementId+" "+cssSelector);
                }
                return $(cssSelector);
            }
            return null;
        };
        self.exploreDataLinkList = ko.observableArray(dfu.discoverVisualAnalyzerLinks());
        self.exploreDataLinkList().forEach(function(ele){
        if(ele.serviceName === "LogAnalyticsUI"){
            ele.name = getNlsString("LANDING_HOME_LOG_EXPLORER");
        }else if(ele.serviceName === "TargetAnalytics"){
            ele.name = getNlsString("LANDING_HOME_DATA_EXPLORER")
        }});
        self.exploreDataLinkList.sort(function(left,right){
            return left.name<=right.name?-1:1;
        });

        //welcome
        self.prefUtil = extPrefUtil;
        self.welcomeDialogModel = new welcomeDialogModel(self.prefUtil, showWelcome);

        //dashboards
        self.isDashboardSet = isSet === true ? true : false;
        self.userName = dfu.getUserName();
        self.isMobileDevice = ko.observable( (new mbu()).isSmallDevice );
        self.currentDashboardSetItem=dashboardSetItem;
        self.dashboardInTabs=ko.observable(false);

        var filterSelection;
        
        if (predata !== null)
        {
            self.filter = predata.getDashboardsFilter({'prefUtil' : self.prefUtil,
                'filterPrefKey': DASHBOARDS_FILTER_PREF_KEY,
                'filterChange': function(event) {
                    if (self.dsFactory)
                    {
                        self.dsFactory.filter = self.filter.toFilterString();
                        self._forceSearch();
                    }
                }
            });
            
            if(self.filter.filter) {
                filterSelection = self.filter.filter;
            }else {
                filterSelection = "allnofilter";
            }
        }
        else
        {
            self.filter = null;
        }
                                
        if (localStorage.deleteHomeDbd ==='true') {
            dfu.showMessage({
                type: 'info',
                summary: oj.Translations.getTranslatedString("DBS_HOME_MSG_DELETED_HOME_DASHBOARD"),
                detail: '',
                removeDelayTime: 10000});
            localStorage.deleteHomeDbd = false;
        }
        self.dataExploreBtnVisible = function(){
            return self.exploreDataLinkList().length > 0;
        };
        self.subscription = predata&&predata.sApplications ? predata.sApplications['applications'] : [];
        self.showTilesMsg = ko.observable(false);
        self.tilesMsg = ko.observable("");
        self.showExploreDataBtn = ko.observable(self.dataExploreBtnVisible());
        self.showSeachClear = ko.observable(false);
        self.tilesViewGridId = self.parentElementId+'gridtview';
        self.tilesViewListId = self.parentElementId+'listview';
        self.tilesViewGrid = 'gridtview';
        self.tilesViewList = 'listview';
        self.isTilesView = ko.observable(predata === null ? 'gridtview' : predata.getDashboardsViewPref());
        self.tracker = ko.observable();
        self.createMessages = ko.observableArray([]);
        self.selectedDashboard = ko.observable(null);
        self.filterById = self.parentElementId+'filtercb';
        self.filterBy = ko.observable(filterSelection);
        
        self.showITAFilter = ko.observable(false);
        self.showLAFilter = ko.observable(false);
        self.showSECFilter = ko.observable(false);
        for(var i in self.subscription) {
            if(self.subscription[i].id === "ITAnalytics") {
                self.showITAFilter(true);
            }
            if(self.subscription[i].id === "LogAnalytics") {
                self.showLAFilter(true);
            }
            if(self.subscription[i].id === "SecurityAnalytics") {
                self.showSECFilter(true);
            }
        }
        
        self.sortById = self.parentElementId+'sortcb';
        self.sortBy = ko.observable(['default']);
        self.createDashboardModel = new createDashboardDialogModel();
        self.confirmDialogModel = new confirmDialogModel(parentElementId);
		var zdtUtil = new zdtUtilModel();
        self.zdtStatus = ko.observable(false); 
        self.dataBaseDown = ko.observable(false); 
        self.createBtnTitle = ko.observable(getNlsString('DBS_HOME_CREATE_BTN_TT_CONTENT'));
        zdtUtil.detectPlannedDowntime(function (isUnderPlannedDowntime) {
//            self.zdtStatus(true);
            self.zdtStatus(isUnderPlannedDowntime);
        });
        
        dfu.getDatabaseStatus(function (databaseStatus) {
            self.dataBaseDown(databaseStatus);
            self.dataBaseDown() && self.createBtnTitle("");
        });
        
        self.createBtnPopup = function () {
            if ($('#cbtn').hasClass("oj-disabled")) {
                var popup = $('#unableCreate');
                popup.ojPopup('open', '#create-button-wrapper', {'at': 'center bottom', 'my': 'start top'});
            }
        };

        self.pageSize = ko.observable(120);

        self.serviceURL = DASHBOARDS_REST_URL;

        self.pagingDatasource = ko.observable(new oj.ArrayPagingDataSource([]));
        self.dashboards = ko.computed(function() {
            return (self.pagingDatasource().getWindowObservable())();
        });
        self.dashboardsTS = ko.observable(new dts.DashboardArrayTableSource([], {idAttribute: 'id'}));
        self.showPaging = ko.computed(function() {
            var _pds = ko.utils.unwrapObservable(self.pagingDatasource());
            if (_pds instanceof  oj.ArrayPagingDataSource) {
                return false;
            }
            var _spo = ko.utils.unwrapObservable(self.pagingDatasource().getShowPagingObservable());
            return _spo;
        });


        var filterString =  self.filter !== null ? self.filter.toFilterString() : null;
        if(defaultFilters && Array.isArray(defaultFilters)){
           filterString = filterString === null ? defaultFilters.join(",") : filterString +","+ defaultFilters.join(",");
        }

        var showFederationInHM = extPrefUtil.getHMItemShowPreferenceSync("uifwk.hm.federation.show");
        var federationFeatureShowInUi = false;
        showFederationInHM.done(function(showInUI) {
            if (showInUI && showInUI.toUpperCase() === "TRUE") {
                federationFeatureShowInUi=true;
            }
        });
        self.dsFactory = new dsf.DatasourceFactory(self.serviceURL, self.sortBy(), filterString, federationFeatureShowInUi);
        self.datasourceCallback = function (_event) {
                    var _i = 0, _rawdbs = [];
                    if (_event['data'])
                    {
                        for (_i = 0; _i < _event['data'].length; _i++)
                        {
                            var _datai = _event['data'][_i].attributes;
                            if (!_datai['lastModifiedOn'])
                            {
                                _datai['lastModifiedOn'] = _datai['createdOn'];
                            }
                            _datai['lastModifiedOnStr'] = getDateString(_datai['lastModifiedOn']);
                            _rawdbs.push(_datai);
                        }
                    }

                    if (_rawdbs.length < 1)
                    {
                        self.showTilesMsg(true);
                        self.tilesMsg(getNlsString('DBS_HOME_TILES_NO_DASHBOARDS'));
                    }
                    else
                    {
                        self.showTilesMsg(false);
                    }

                    if (self.dashboardsTS() && self.dashboardsTS() !== null)
                    {
                        self.dashboardsTS().reset(_rawdbs);
                    }
                    else
                    {
                        self.dashboardsTS(new dts.DashboardArrayTableSource(_rawdbs, {idAttribute: 'id'}));
                    }
                };
        self.datasource = self.dsFactory.build("", self.pageSize());

        self.datasource['pagingDS'].setPage(0, {
            'silent': true,
            'success': function() {
                self.datasource['serverError'] = false;
                self.refreshPagingSource();
                if (self.datasource['pagingDS'].totalSize() <= 0 && (self.welcomeDialogModel.showWelcome === false)){
                    $('#cbtn-tooltip').ojPopup('open', "#cbtn");
                }
            },
            'error': function(jqXHR, textStatus, errorThrown) {
                self.datasource['serverError'] = true;
                oj.Logger.error("Error when fetching data for paginge data source. " + (jqXHR ? jqXHR.responseText : ""));
            }
        } );

        self.refreshPagingSource = function() {
            self.datasource['pagingDS'].on("sync", self.datasourceCallback);
            self.pagingDatasource( self.datasource['pagingDS'] );
        };

        self.handleDashboardClicked = function(event, data) {
            var _dmodel = data['dashboardModel'];
            if (data['id'])
            {
                _dmodel = self.datasource['pagingDS'].getModelFromWindow(data['id']);
            }
            if (_dmodel && _dmodel !== null)
            {
                oj.Logger.info("Dashboard: [id="+_dmodel.get("id")+", name="+_dmodel.get("name")+"] is open from Dashboard Home",true);
                _dmodel.openDashboardPage();
            }
        };

        self.handleShowDashboardPop = function(event, data) {
            var popup = self.getElementByCss(".dashboard-picker:visible .dsbinfopop");
            var isOpen = !popup.ojPopup("isOpen");
            if (!isOpen)
            {
                popup.ojPopup("close");
            }
            if (data['id'])
            {
                data['dashboardModel'] = self.datasource['pagingDS'].getModelFromWindow(data['id']);
                if(  data['dashboardModel'] )
                {
                    data['dashboard'] = data['dashboardModel'].attributes;
                }
            }
            if(data.dashboard.description)
            {
                var rulerElem = $("#ruler");
                rulerElem.show();
                if(!data.dashboard.hasLongDscrpt && rulerElem.html(data.dashboard.description).height()>32){
                    data.dashboard.hasLongDscrpt = true;
                }
                rulerElem.hide();
                data.dashboard.description = data.dashboard.description.toString().replace(/\n/g,"<br>");
            }
            self.selectedDashboard(data);
            if (data.element)
            {
                self.dashboardInTabs(false);
                if (typeof(self.currentDashboardSetItem)!=='undefined') {
                    self.currentDashboardSetItem().forEach(function (item) {
                        if (item.name() === data.dashboard.name) {
                            self.dashboardInTabs(true);
                        }
                    });
                }
                popup.ojPopup('open', data.element, {'at': 'right center', 'my': 'start center'});
            }
        };

        self.handleCloseDashboardPop = function(event, data) {
            var popup = $(".dashboard-picker:visible .dsbinfopop");
            var isOpen = !popup.ojPopup("isOpen");
            if (!isOpen)
            {
                popup.ojPopup("close");
            }
        };

        self.handleDashboardDeleted = function(event, data) {
            var _sd = self.selectedDashboard();
            var _url = dfu.isDevMode() ? dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint, "dashboards/") : "/sso.static/dashboards.service/";
            dfu.ajaxWithRetry(_url + _sd.dashboard.id + "/dashboardsets", {
                type: 'GET',
                headers: dfu.getDashboardsRequestHeader(),
                success: function (resp) {
                    var isMemberOfDashboards = resp.dashboardSets && resp.dashboardSets.length > 0;
                    var linkedDashboardList = resp.linkedDashboardList && resp.linkedDashboardList.length>0;                    
                    if(isMemberOfDashboards || linkedDashboardList){
                        var _name =  _sd.dashboard.name,_sets =  resp.dashboardSets,_linkedDbd=resp.linkedDashboardList;
                        var _message =  getNlsString('COMMON_DELETE_USED_LINKED_DASHBOARD_MSG_HEAD',_name) + "<br>";
                        for(var i = 0; i < _sets.length ; i++ ){
                            _message += "<br>"+ _sets[i].name;
                        }
                        for(var i = 0; i < _linkedDbd.length ; i++ ){
                             _message += "<br>"+ _linkedDbd[i];
                        }
                        _message += "<br><br>"+ getNlsString('COMMON_DELETE_USED_DASHBOARD_MSG_TAILE', _name);
                        self.confirmDialogModel.show(
                                getNlsString('DBS_HOME_CFM_DLG_DELETE_DSB'),
                                getNlsString('COMMON_BTN_DELETE'),
                                _message,
                                self.confirmDashboardDelete, true);
                    }else{
                         self.confirmDialogModel.show(getNlsString('DBS_HOME_CFM_DLG_DELETE_DSB'),
                         getNlsString('COMMON_BTN_DELETE'),
                         getNlsString('COMMON_DELETE_DASHBOARD_MSG', _sd.dashboard.name),
                         self.confirmDashboardDelete, true);
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log(errorThrown);
                }
            });
        };

        self.confirmDashboardDelete = function() {
            if ( !self.selectedDashboard() || self.selectedDashboard() === null ) {
                return;
            }
            var conformButton = $("button[name*='dbs_cfmDialog_delete']");
            conformButton.attr('disabled','disabled'); 
            self.datasource['pagingDS'].remove(self.selectedDashboard().dashboardModel,
                   {
                        success: function () {
                            conformButton.removeAttr('disabled');
                            self.confirmDialogModel.close();
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            conformButton.removeAttr('disabled');
                            var _m = "";
                            if (jqXHR && jqXHR[0] && jqXHR[0].responseJSON && jqXHR[0].responseJSON.errorMessage)
                            {
                                 _m = jqXHR[0].responseJSON.errorMessage;
                            }else if (jqXHR && jqXHR.responseJSON && jqXHR.responseJSON.errorMessage)
                            {
                                _m = jqXHR.responseJSON.errorMessage;
                            }
                            oj.Logger.error("Error when deleting dashboard. " + (jqXHR ? jqXHR.responseText : ""));
                            self.confirmDialogModel.show(getNlsString('COMMON_TEXT_ERROR'), getNlsString('COMMON_BTN_OK'),
                                    getNlsString('DBS_HOME_CFM_DLG_DELETE_DSB_ERROR') + " " +_m,
                                    function () {self.confirmDialogModel.close();});
                        }
                    });
        };


        self.exploreDataMenuItemSelect = function( event, ui ) {
            if (ui.item.children("a")[0] && ui.item.children("a")[0].value)
            {
                if (dfu.isDevMode()){
                    window.location = cxtUtil.appendOMCContext(dfu.getRelUrlFromFullUrl(ui.item.children("a")[0].value));
                }else{
                    window.location = cxtUtil.appendOMCContext(ui.item.children("a")[0].value);
                }
            }
        };

        self.createDashboardClicked = function()
        {
            self.createDashboardModel.clear();
            $( "#cDsbDialog" ).ojDialog( "open" );
        };

        self.cancelDashboardCreate = function(){
            $( "#cDsbDialog" ).ojDialog( "open" );
        };

        self.afterConfirmDashboardCreate = function(_model, _resp, _options) {
            _model.openDashboardPage();
        };

        self.confirmDashboardCreate = function()
        {
            var _trackObj = ko.utils.unwrapObservable(self.tracker),
            hasInvalidComponents = _trackObj ? _trackObj["invalidShown"] : false,
            hasInvalidHidenComponents = _trackObj ? _trackObj["invalidHidden"] : false;

            if (hasInvalidComponents || hasInvalidHidenComponents)
            {
                _trackObj.showMessages();
                _trackObj.focusOnFirstInvalid();
                return;
            }
            //clear tracker
            self.createMessages.removeAll();

            var _addeddb = { "type":self.createDashboardModel.selectType(),
                            "name": self.createDashboardModel.name(),
                            "showInHome":self.createDashboardModel.underSet ? false : true,
                            "description": self.createDashboardModel.description(),
                            "enableTimeRange": self.createDashboardModel.isEnableTimeRange() ? "TRUE" : "FALSE",
                            "enableRefresh": self.createDashboardModel.isEnableTimeRange(),
                            "federationSupported": "NON_FEDERATION_ONLY"};
            if (!_addeddb['name'] || _addeddb['name'] === "" || _addeddb['name'].length > 64)
            {
                self.createMessages.push(new oj.Message(getNlsString('DBS_HOME_CREATE_DLG_INVALID_NAME')));
                _trackObj.showMessages();
                _trackObj.focusOnFirstInvalid();
                return;
            }

            $( "#cDsbDialog" ).css("cursor", "progress");
            self.datasource['pagingDS'].create(_addeddb, {
                        'contentType': 'application/json',

                        success: function(_model, _resp, _options) {
                            if(_resp.errorCode && _resp.errorCode === 10001){
                                $( "#cDsbDialog" ).css("cursor", "default");
                                var _m = getNlsString('COMMON_DASHBAORD_SAME_NAME_ERROR');
                                var _mdetail = getNlsString('COMMON_DASHBAORD_SAME_NAME_ERROR_DETAIL');
                                _trackObj = new oj.InvalidComponentTracker();
                                self.tracker(_trackObj);
                                self.createMessages.push(new oj.Message(_m, _mdetail));
                                _trackObj.showMessages();
                                _trackObj.focusOnFirstInvalid();
                            }else{
                                $( "#cDsbDialog" ).css("cursor", "default");
                                $( "#cDsbDialog" ).ojDialog( "close" );
                                self.afterConfirmDashboardCreate(_model, _resp, _options);
                            }
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            $( "#cDsbDialog" ).css("cursor", "default");
                            var _m = null;
                            var _mdetail;
                            if (jqXHR && jqXHR[0] && jqXHR[0].responseJSON && jqXHR[0].responseJSON.errorCode === 10001)
                            {
                                 _m = getNlsString('COMMON_DASHBAORD_SAME_NAME_ERROR');
                                 _mdetail = getNlsString('COMMON_DASHBAORD_SAME_NAME_ERROR_DETAIL');
                            }else if (jqXHR && jqXHR.responseJSON && jqXHR.responseJSON.errorCode === 10001)
                            {
                                _m = getNlsString('COMMON_DASHBAORD_SAME_NAME_ERROR');
                                _mdetail = getNlsString('COMMON_DASHBAORD_SAME_NAME_ERROR_DETAIL');
                            }
                            else
                            {
                                dfu.showMessage({
                                    type: 'error',
                                    summary: oj.Translations.getTranslatedString("DBS_Home_FAILED_TO_CREATE_DASHBOARD",jqXHR ? jqXHR.responseText : ""),
                                    detail: '',
                                    removeDelayTime: 8000});

                                // a server error record
                                oj.Logger.error("Error when creating dashboard. " + (jqXHR ? jqXHR.responseText : ""));
                            }
                            if (_m !== null)
                            {
                                _trackObj = new oj.InvalidComponentTracker();
                                self.tracker(_trackObj);
                                self.createMessages.push(new oj.Message(_m, _mdetail));
                                _trackObj.showMessages();
                                _trackObj.focusOnFirstInvalid();
                                $( "#cDsbDialog" ).css("cursor", "default");
                            }
                            else
                            {                            
                                $( "#cDsbDialog" ).css("cursor", "default");
                                $( "#cDsbDialog" ).ojDialog( "close" );
                            }
                        }
                    });
        };

        self.cancelDashboardCreate = function()
        {
            $( "#cDsbDialog" ).ojDialog( "close" );
        };

        self.handleViewChanged = function (event, data) {
            var _option = data.option, _value = data.value;
            if ( _option === "checked" )
            {
                if (self.isDashboardSet !== true  && !self.zdtStatus())
                {
                    self.prefUtil.setPreference(DASHBOARDS_VIEW_PREF_KEY, _value);
                }
                if (data.value === 'listview')
                {
                    var __sortui = self._getListTableSortUi(self.sortBy()),  _ts = self.dashboardsTS();
                    if ( _ts && _ts !== null && __sortui !== null )
                    {
                        _ts.handleEvent(oj.TableDataSource.EventType['SORT'], __sortui);
                    }
                }

            }
        };

        self.handleListColumnSort = function( event, ui ) {
            if (ui)
            {
                var _option = ui.header + (ui.direction === 'descending' ? '_dsc' : '_asc');
                self.sortBy([_option]);
            }
        };
        
        self.handleFilterByChanged = function(context, valueParam) {
            var _value = valueParam.value;
            if(valueParam.option === "value") {
                var filterByValue = _value[0];
                if(filterByValue === "allnofilter") {
                    self.filter.serviceFilter([]);
                    self.filter.creatorFilter([]);
                    self.filter.favoritesFilter([]);
                }else if(filterByValue === "favorites") {
                    self.filter.serviceFilter([]);
                    self.filter.creatorFilter([]);
                    self.filter.favoritesFilter(["favorites"]);
                }else if($.inArray(filterByValue, ["ita", "la", "sec"])) {
                    self.filter.serviceFilter([filterByValue]);
                    self.filter.creatorFilter([]);
                    self.filter.favoritesFilter([]);
                }else {
                    self.filter.serviceFilter([]);
                    self.filter.creatorFilter([filterByValue]);
                    self.filter.favoritesFilter([]);
                }
                if(!self.zdtStatus()){
                    self.filter.saveFilter();
                }
                self.filter.handleFilterChange({filterType: 'serviceFilter', newValue: filterByValue});
            }
        };
        
        self.handleSortByChanged = function (context, valueParam) {
            var _preValue = valueParam.previousValue, _value = valueParam.value, _ts = self.dashboardsTS();
            if ( valueParam.option === "value" )
            {
                self.dsFactory.sortBy = _value[0];
                if (valueParam.optionMetadata.writeback === 'shouldNotWrite')
                {
                    // change by set self.sortBy triggered by list table sort
                    self.forceSearch();
                }
                else
                {
                    // selected by user
                    if (_ts && _ts !== null)
                    {
                            var __sortui = self._getListTableSortUi(_value[0]);
                            if (__sortui === null)
                            {
                                //sort column not in table, clear the table header sorting icon
                                var _headercolumns = self.getElementByCss('.oj-table-column-header-cell');
                                _headercolumns.data('sorted', null);
                                var headerColumnAscLink =  self.getElementByCss('.oj-table-column-header-asc-link.oj-enabled');
                                headerColumnAscLink.addClass('oj-disabled');
                                headerColumnAscLink.removeClass('oj-enabled');
                                var headerColumnDscLink =  self.getElementByCss('.oj-table-column-header-dsc-link.oj-enabled');
                                headerColumnDscLink.addClass('oj-disabled');
                                headerColumnDscLink.removeClass('oj-enabled');
                            }
                            else
                            {
                                self.sortBy(__sortui.header + (__sortui.direction === 'descending' ? '_dsc' : '_asc'));
                                _ts.handleEvent(oj.TableDataSource.EventType['SORT'], __sortui);
                            }
                    }
                    setTimeout(function() { self._forceSearch();}, 0);
                }
            }
        };

        self._getListTableSortUi = function(sortOption)
        {
            if (sortOption === 'name_asc')
            {
                return {'header': 'name', 'direction': 'ascending'};
            }

            if (sortOption === 'name_dsc')
            {
                return {'header': 'name', 'direction': 'descending'};
            }

            if (sortOption === 'owner_asc')
            {
                return {'header': 'owner', 'direction': 'ascending'};
            }

            if (sortOption === 'owner_dsc')
            {
                return {'header': 'owner', 'direction': 'descending'};
            }

            if (sortOption === 'last_modification_date_asc')
            {
                return {'header': 'last_modification_date', 'direction': 'ascending'};
            }

            if (sortOption === 'last_modification_date_dsc')
            {
                return {'header': 'last_modification_date', 'direction': 'descending'};
            }

            return null;
        };

        self.typeaheadSearchStart = function (event, data)
        {
            var  _ts = self.dashboardsTS();
            if ( _ts && _ts !== null )
            {
                _ts.handleEvent(oj.TableDataSource.EventType['REQUEST']);
            }
        };

        self.acceptInput = function (event, data)
        {
            if (data && data.length > 0)
            {
                self.showSeachClear(true);
            }
            else
            {
                self.showSeachClear(false);
            }
        };

        self.searchResponse = function (event, data)
        {
            self.datasource = data.content;
            self.refreshPagingSource();
        };

        self.forceSearch = function (event, data)
        {
            self._forceSearch();
        };

        self._forceSearch = function (silent, endcallback)
        {
            var  _ts = self.dashboardsTS();
            if (silent !== true && ( _ts && _ts !== null ))
            {
                _ts.handleEvent(oj.TableDataSource.EventType['REQUEST']);
            }
            self.getElementByCss(".dbs-sinput").dbsTypeAhead("forceSearch", endcallback);
        };

        self.clearSearch = function (event, data)
        {
            self.getElementByCss(".dbs-sinput").dbsTypeAhead("clearInput");
        };

        self.listNameRender = function (context)
        {
            var _dmodel = self.datasource['pagingDS'].getModelFromWindow(context.row.id);
            var _link = $(document.createElement('a')).addClass( "dbs-dsbnameele" ).attr("href",_dmodel.get('buildPageUrl'))
                    .on('click', function(event) {
                        //prevent event bubble
                        event.stopPropagation();
                        self.handleDashboardClicked(event, {'id': context.row.id, 'element': _link,'name':context.row.name});
                    });
            _link.text(context.row.name);
            if (context.row.systemDashboard === true)
            {
                _link.addClass( "dbs-dsbsystem" );
            }
            else
            {
                _link.addClass( "dbs-dsbnormal" );
            }
            $(context.cellContext.parentElement).append(_link);
        };

        self.listInfoRender = function (context)
        {
            var _info = $("<button data-bind=\"ojComponent: { component:'ojButton', chroming: 'half', display: 'icons', label: getNlsString('DBS_HOME_DSB_PAGE_INFO_LABEL'), icons: {start: 'icon-locationinfo-16 oj-fwk-icon dbs-icon-size-16'}}\"></button>")
                    .addClass("oj-button-half-chrome oj-sm-float-end")
                    .on('click', function(event) {
                        //prevent event bubble
                        event.stopPropagation();
                        self.handleShowDashboardPop(event, {'id': context.row.id, 'element': _info});
                    });
            $(context.cellContext.parentElement).append(_info);
            ko.applyBindings({}, _info[0]);
        };

        self.updateDashboard = function (dsb)
        {
            var _id = dsb.id;
            if (_id && self.datasource['pagingDS'])
            {
                self.datasource['pagingDS'].refreshModel(_id, {
                    success: function(model) {
                        var _e = $(".dbs-summary-container[aria-dashboard=\""+_id+"\"]");
                        if (_e && _e.length > 0) {
                            _e.dbsDashboardPanel("refresh");
                        }
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        oj.Logger.error("Error when updating dashboard. " + (jqXHR ? jqXHR.responseText : ""));
                    }
                });
            }
        };

    }

    function PredataModel() {
        var self = this;
        self.preferences = undefined;
        self.sApplications = undefined;
        
        self.getDashboardsFilter = function (options) {
            var _options = options || {}, _filterPref = self.getDashboardsFilterPref(), _filterUrlParam=getUrlParam("filter");
            if (_filterUrlParam && _filterUrlParam.trim().length > 0)
            {
                _options['saveFilterPref'] = false;
                _filterPref = _filterUrlParam.toLowerCase();
            }
            else
            {
                _options['saveFilterPref'] = true;
                //Use saved filters if it is only 1 filter
                var _serviceFilters = ["apm", "ocs"]; //Do not support service filter for APM and OCS
                if(_filterPref) {
                    if(_filterPref.split(",").length > 1) {
                        _filterPref = null;
                    }else if(_filterPref.split(",").length === 1) {
                        if(_serviceFilters.indexOf(_filterPref) !== -1) {
                            _filterPref = null;
                        }
                    }
                }
            }
            if (_filterPref && _filterPref.trim().slice(0, 1) === '{')
            {
                _filterPref = null;
            }
            return new dft.DashboardsFilter(_filterPref, self.sApplications ? self.sApplications['applications'] : [], _options);
        };

        self.getDashboardsFilterPref = function () {
            var filter = self.getPreferenceValue(DASHBOARDS_FILTER_PREF_KEY);
            if (filter === undefined || filter.length === 0) {
                return undefined;
            }
            filter = $("<div/>").html(filter).text();
            return filter;
        };

        self.getShowWelcomePref = function () {
            var showWelcome = self.getPreferenceValue(SHOW_WELCOME_PREF_KEY);
            if (showWelcome === "false")
            {
                showWelcome = false;
            }
            else
            {
                showWelcome = true;
            }
            return showWelcome;
        };

        self.getDashboardsViewPref = function () {
            var _view = self.getPreferenceValue(DASHBOARDS_VIEW_PREF_KEY);
            if (_view !== "listview")
            {
                _view = "gridtview";
            }
            return _view;
        };

        self.getPreferenceValue = function(key) {
            if (self.preferences === undefined) {
                return undefined;
            }
            var arr;
            arr = $.grep(self.preferences, function( pref ) {
                if (pref !== undefined && pref['key'] === key) {
                    return true;
                }
                return false;
            });
            if (arr !== undefined && arr.length > 0) {
                return arr[0]['value'];
            }
            return undefined;
        };

        self.loadPreferences = function() {
            return $.ajax({
                        url: PREFERENCES_REST_URL,
                        type: 'GET',
                        headers: dfu.getDashboardsRequestHeader(),
                        success: function (result) {
                            self.preferences = result;
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            oj.Logger.error("Error when load APM setup status. " + (jqXHR ? jqXHR.responseText : ""));
                        }
                    });
        };

        self.loadSubscribedApplications = function() {
            dfu.getSubscribedApps2WithEdition(
                function(subscribedapps) {
                    self.sApplications = subscribedapps;
                },
                function(jqXHR, textStatus, errorThrown) {
                    oj.Logger.error("Error when load subscribed applications. " + (jqXHR ? jqXHR.responseText : ""));
                }
            );
        }

        self.loadAll = function() {
            return $.when(extPrefUtil.getHMItemShowPreferenceSync("uifwk.hm.federation.show"),self.loadPreferences(), self.loadSubscribedApplications());
        };
    }

    return {'ViewModel': ViewModel, 'PredataModel': PredataModel};
});
