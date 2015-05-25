/**
 * @preserve Copyright (c) 2014, Oracle and/or its affiliates.
 * All rights reserved.
 */

/**
 * @preserve Copyright 2013 jQuery Foundation and other contributors
 * Released under the MIT license.
 * http://jquery.org/license
 */

define([
    'dbs/datasourcefactory',
    'ojs/ojcore', 
    'knockout', 
    'jquery', 
    'dfutil',
    'prefutil',
    'ojs/ojknockout', 
    'ojs/ojpagingcontrol',
    'ojs/ojpagingcontrol-model'
],
function(dsf, oj, ko, $, dfu, pfu)
{
    
    function createDashboardDialogModel() {
        var self = this;
        self.name = ko.observable(undefined);
        self.description = ko.observable('');
        self.timeRangeFilterValue = ko.observable(["ON"]);//for now ON always and hide option in UI
        self.targetFilterValue = ko.observable(["OFF"]);
        self.isDisabled = ko.observable(false);
        
        self.clear = function() {
            self.name(undefined);
            self.description('');
            self.timeRangeFilterValue(["ON"]);
            self.targetFilterValue(["OFF"]);
            self.isDisabled(false);
        };
        
        self.isEnableTimeRange = function() {
            if (self.timeRangeFilterValue()  === "ON" || 
                    self.timeRangeFilterValue()[0] === "ON")
            {
                return true;
            }
            return false;
        };
        
        self.keydown = function (d, e) {
           if (e.keyCode === 13) {
              $( "#cDsbDialog" ).ojDialog( "close" );
           }
        };
    };
        
    function confirmDialogModel(title, okLabel, message, okFunction) {
        var self = this;
        //self.style = ko.observable('min-width: 450px; min-height:150px;');
        self.title = ko.observable(title || '');
        self.okLabel = ko.observable(okLabel || '');
        self.message = ko.observable(message || '');
        
        self.okFunction = (okFunction && $.isFunction(okFunction)) ? okFunction : function() {}; 
        
        self.show = function (title, okLabel, message, okFunction) {
            self.title(title || '');
            self.okLabel(okLabel || '');
            self.message(message || '');
            self.okFunction = function () {
                var _okfunc = (okFunction && $.isFunction(okFunction)) ? okFunction : function() {};
                _okfunc();
                //self.close();
            };
            $( "#dbs_cfmDialog" ).ojDialog( "open" );
        };
        
        self.close = function () {
            $( "#dbs_cfmDialog" ).ojDialog( "close" );
        };
        
        self.keydown = function (d, e) {
           if (e.keyCode === 13) {
             self.close();
           }
        };
    }; 

    function comingsoonDialogModel() {
        var self = this;
       
        self.close = function () {
            $( "#dbs_comingsoonDialog" ).ojDialog( "close" );
        };
    };
    
    function welcomeDialogModel(prefUtil) {
        var self = this;
        self.showWelcomePrefKey = "Dashboards.showWelcomeDialog";
        self.userName = dfu.getUserName();
        self.prefUtil = prefUtil;
        self.showWelcome = true;
        (function() {
            prefUtil.getPreference(self.showWelcomePrefKey, {
                async: false,
                success: function (res) {
                    if (res['value'] === "true")
                    {
                        self.showWelcome = true;
                    }
                    if (res['value'] === "false")
                    {
                        self.showWelcome = false;
                    }
                },
                error: function() {
                    oj.Logger.info("Preference of Show Welcome Dialog is not set. The defualt value 'true' is applied.");
                }
            });
        })();
        
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
            prefUtil.setPreference(self.showWelcomePrefKey, "false");
            $('#overviewDialog').ojDialog('close');
        };    
        
    };
    
    function ViewModel() {
        
        var self = this;
        self.exploreDataLinkList = ko.observableArray(dfu.discoverVisualAnalyzerLinks());
//        self.dfRestApiUrl = dfu.discoverDFRestApiUrl();
        //welcome
        self.prefUtil = new pfu("/sso.static/dashboards.preferences"/*dfu.buildFullUrl(self.dfRestApiUrl,'preferences')*/, dfu.getDashboardsRequestHeader());
        self.welcomeDialogModel = new welcomeDialogModel(self.prefUtil);
        
        //dashboards
        self.showSeachClear = ko.observable(false);
        self.tracker = ko.observable();
        self.createMessages = ko.observableArray([]);
        self.selectedDashboard = ko.observable(null);
        self.sortBy = ko.observable('access_Date');
        self.createDashboardModel = new createDashboardDialogModel();
        self.confirmDialogModel = new confirmDialogModel();
        self.comingsoonDialogModel = new comingsoonDialogModel();
        
        self.pageSize = ko.observable(120);
        
        self.serviceURL = "http://slc04wjl.us.oracle.com:7001/emcpdf/api/v1/dashboards/";//"/sso.static/dashboards.service";//dfu.buildFullUrl(self.dfRestApiUrl,"dashboards");
        //console.log("Service url: "+self.serviceURL);
        
        self.pagingDatasource = ko.observable(new oj.ArrayPagingDataSource([]));
        self.dashboards = ko.computed(function() {
            return (self.pagingDatasource().getWindowObservable())();
        });
        self.showPaging = ko.computed(function() {
            var _pds = ko.utils.unwrapObservable(self.pagingDatasource());
            if (_pds instanceof  oj.ArrayPagingDataSource) return false;
            var _spo = ko.utils.unwrapObservable(self.pagingDatasource().getShowPagingObservable());
            return _spo;
        });
        
        self.dsFactory = new dsf.DatasourceFactory(self.serviceURL, self.sortBy());
        self.datasource = self.dsFactory.build("", self.pageSize());
        self.datasource['pagingDS'].fetch({'startIndex': 0, 'fetchType': 'init', 
            'success': function() {
                self.pagingDatasource( self.datasource['pagingDS'] );
                if (self.datasource['pagingDS'].totalSize() <= 0)
                {
                    if (self.welcomeDialogModel.showWelcome === false 
                        && self.datasource['pagingDS'].totalSize() <= 0)
                    {
                        $('#cbtn-tooltip').ojPopup('open', "#cbtn");
                    }
                }
            },
            'error': function(jqXHR, textStatus, errorThrown) {
                oj.Logger.error("Error when fetching data for paginge data source. " + (jqXHR ? jqXHR.responseText : ""));
            }
        } );
                
        self.handleDashboardClicked = function(event, data) {
            //console.log(data);
            //data.dashboard.openDashboard();
            oj.Logger.info("Dashboard: [id="+data.dashboardModel.get("id")+", name="+data.dashboardModel.get("name")+"] is open from Dashboard Home",true);
            data.dashboardModel.openDashboardPage();
        };
        
        self.handleDashboardDeleted = function(event, data) {
            //console.log(data);
            self.selectedDashboard(data);
            self.confirmDialogModel.show(getNlsString('DBS_HOME_CFM_DLG_DELETE_DSB'), 
                         getNlsString('COMMON_BTN_DELETE'), 
                         getNlsString('DBS_HOME_CFM_DLG_DELETE_DSB_MSG', data.dashboard.name),
                         self.confirmDashboardDelete);
        };
        
        self.confirmDashboardDelete = function() {
            if ( !self.selectedDashboard() || self.selectedDashboard() === null ) return;
            
            self.datasource['pagingDS'].remove(self.selectedDashboard().dashboardModel,
                   {
                        success: function () {
                            self.confirmDialogModel.close();
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
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
            //window.open(ui.item.children("a")[0].value);
            if (ui.item.children("a")[0] && ui.item.children("a")[0].value)
            {
                window.location = ui.item.children("a")[0].value;
            }
        };
        
        self.createDashboardClicked = function()
        {
            self.createDashboardModel.clear();
            $( "#cDsbDialog" ).ojDialog( "open" );
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
            //self.tracker(undefined);
            self.createMessages.removeAll();
            
            var _addeddb = {"name": self.createDashboardModel.name(), 
                            "description": self.createDashboardModel.description(),
                            "enableTimeRange": self.createDashboardModel.isEnableTimeRange() };
            
            if (!_addeddb['name'] || _addeddb['name'] === "" || _addeddb['name'].length > 64)
            {
                //_trackObj = new oj.InvalidComponentTracker();
                //self.tracker(_trackObj);
                self.createMessages.push(new oj.Message(getNlsString('DBS_HOME_CREATE_DLG_INVALID_NAME')));
                _trackObj.showMessages();
                _trackObj.focusOnFirstInvalid();
                return;
            }
            
            self.createDashboardModel.isDisabled(true);
            $( "#cDsbDialog" ).css("cursor", "progress");
            self.datasource['pagingDS'].create(_addeddb, {
                        'contentType': 'application/json',
                        
                        success: function(response) {
                            //console.log( " success ");
                            $( "#cDsbDialog" ).css("cursor", "default");
                            $( "#cDsbDialog" ).ojDialog( "close" );
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            //console.log('Error in Create: ' + textStatus);
                            $( "#cDsbDialog" ).css("cursor", "default");
                            self.createDashboardModel.isDisabled(false);
                            var _m = getNlsString('COMMON_SERVER_ERROR');
                            if (jqXHR && jqXHR[0] && jqXHR[0].responseJSON && jqXHR[0].responseJSON.errorMessage)
                            {
                                 _m = jqXHR[0].responseJSON.errorMessage;
                            }else if (jqXHR && jqXHR.responseJSON && jqXHR.responseJSON.errorMessage)
                            {
                                _m = jqXHR.responseJSON.errorMessage;
                            }
                            else
                            {
                                // a server error record
                                 oj.Logger.error("Error when creating dashboard. " + (jqXHR ? jqXHR.responseText : ""));
                            }
                            _trackObj = new oj.InvalidComponentTracker();
                            self.tracker(_trackObj);
                            self.createMessages.push(new oj.Message(_m));
                            _trackObj.showMessages();
                            _trackObj.focusOnFirstInvalid();
                            /*
                            $( "#cDsbDialog" ).ojDialog( "close" );
                            self.confirmDialogModel.show("Error", "Ok", 
                                    "Error on creating dashboard." + " " +_m,
                                    function () {self.confirmDialogModel.close()});*/
                        }
                    });
        };
        
        self.cancelDashboardCreate = function()
        {
            $( "#cDsbDialog" ).ojDialog( "close" );
        };
        
        self.handleSortByChanged = function (context, valueParam) {
            var _preValue = valueParam.previousValue, _value = valueParam.value;
            if ( valueParam.option === "value" && _value[0] !== _preValue[0] )
            {
                self.dsFactory.sortBy = _value[0];
                $("#sinput").dbsTypeAhead("forceSearch");
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
            //console.log("searchResponse: "+data.content.collection.length);
            self.datasource = data.content;
            self.pagingDatasource(data.content['pagingDS']);
        };
        
        self.forceSearch = function (event, data)
        {
            $("#sinput").dbsTypeAhead("forceSearch");
        };
        
        self.clearSearch = function (event, data)
        {
            $("#sinput").dbsTypeAhead("clearInput");
        };
        
        self.updateDashboard = function (dsb)
        {
            var _id = dsb.id;
            if (_id && self.datasource['pagingDS'])
            {
                self.datasource['pagingDS'].refreshModel(_id, {
                    success: function(model) {
                        var _e = $(".dbs-summary-container[aria-dashboard=\""+_id+"\"]");
                        if (_e && _e.length > 0) _e.dbsDashboardPanel("refresh");
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        //console.log("Error on update dashboard");
                        oj.Logger.error("Error when updating dashboard. " + (jqXHR ? jqXHR.responseText : ""));
                    }
                });
            }
        };
        
        self.getDashboard = function (id)
        {
           
        };
        
    };
    
    function PredataModel() {
        var self = this, itaSetupUrl = "test", 
                laSetupUrl = "test", 
                apmSetupUrl = "test", 
                sApplictionsUrl = "/sso.static/dashboards.subscribedapps";
        self.itaResponse = { "eligibleODSTargetCount" : 0, "warehouseTargetCount": 0, "warehouseLoading": true};
        self.laResponse = {"other": {"#agents": 0}};
        self.apmResponse = "false";
        self.sApplications = undefined;
        
        self.getItaSetupStatus = function() {
            if (itaResponse !== undefined)
            {
                if (itaResponse['eligibleODSTargetCount'] === 0) return 1;
                if (itaResponse['eligibleODSTargetCount'] > 0 
                        && itaResponse['warehouseTargetCount'] === 0)
                {
                    return 2;
                }
                if (itaResponse['eligibleODSTargetCount'] > 1
                        && itaResponse['warehouseTargetCount'] > 1)
                {
                    return 3;
                }
            }
            return 0;
        };
        
        self.getLaSetupStatus = function() {
            if (laResponse !== undefined)
            {
                if (laResponse['other']['#agents'] === 0) return 1;
            }
            return 0;
        };
        
        self.getApmSetupStatus = function() {
            if (apmResponse !== undefined)
            {
                if (apmResponse === "false") return 1;
            }
            return 0;
        };
        
        self.loadItaSetup = function() {
            return $.ajax({
                        url: itaSetupUrl,
                        type: 'GET',
                        headers: dfu.getDashboardsRequestHeader(), 
                        success: function (result) {
                            self.itaResponse = result;
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            oj.Logger.error("Error when load ITA setup status. " + (jqXHR ? jqXHR.responseText : ""));
                        }
                    });
        };
        
        self.loadLaSetup = function() {
            return $.ajax({
                        url: laSetupUrl,
                        type: 'GET',
                        headers: dfu.getDashboardsRequestHeader(), 
                        success: function (result) {
                            self.laResponse = result;
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            oj.Logger.error("Error when load LA setup status. " + (jqXHR ? jqXHR.responseText : ""));
                        }
                    });
        };
        
        self.loadApmSetup = function() {
            return $.ajax({
                        url: apmSetupUrl,
                        type: 'GET',
                        headers: dfu.getDashboardsRequestHeader(), 
                        success: function (result) {
                            self.apmResponse = result;
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            oj.Logger.error("Error when load APM setup status. " + (jqXHR ? jqXHR.responseText : ""));
                        }
                    });
        };
        
        self.loadSubscribedApplications = function() {
            return $.ajax({
                        url: sApplictionsUrl,
                        type: 'GET',
                        headers: dfu.getDashboardsRequestHeader(), 
                        success: function (result) {
                            self.sApplications = result;
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            oj.Logger.error("Error when load subscribed applications. " + (jqXHR ? jqXHR.responseText : ""));
                        }
                    });
        };
        
        self.loadAll = function() {
            return $.when(self.loadItaSetup(), self.loadLaSetup(), 
                          self.loadApmSetup(), self.loadSubscribedApplications());
        };
    }
    
    
    return {'ViewModel': ViewModel, 'PredataModel': PredataModel};
});
