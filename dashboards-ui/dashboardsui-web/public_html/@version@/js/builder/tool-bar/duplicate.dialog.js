/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout', 
        'jquery',
        'ojs/ojcore',
        'dfutil',
        'uifwk/js/util/screenshot-util'
    ], 
    function(ko, $, oj, dfu, ssu) {       
        function DuplicateDashboardModel($b) {
            var self = this;
            self.tilesViewModel = $b.getDashboardTilesViewModel();
            self.tracker = ko.observable();
            self.errorMessages = ko.observableArray([]);
            self.name = ko.observable();
            self.description = ko.observable();
            self.saveBtnDisabled = ko.observable(true);
            
            self.beforeOpenDialog = function(event, ui) {
                $("#dupDsbNameIn").ojInputText("option", "value", null);
                $("#dupDsbDescIn").ojTextArea("option", "value", null);
                self.saveBtnDisabled(true);
                self.errorMessages.removeAll();
            };
            
            self.nameOptionChanged = function(event, data) {
                if (data.option === 'value' || data.option === 'rawValue') {
                    if (data.value && $.trim(data.value) !== '') {
                        self.saveBtnDisabled(false);
                    }
                    else {
                        self.saveBtnDisabled(true);
                    }
                }
            };
            
            self.duplicateDashboardConfirmed = function() {
                var trackObj = ko.utils.unwrapObservable(self.tracker), 
                hasInvalidComponents = trackObj ? trackObj["invalidShown"] : false,
                hasInvalidHidenComponents = trackObj ? trackObj["invalidHidden"] : false;

                if (hasInvalidComponents || hasInvalidHidenComponents) 
                {
                    trackObj.showMessages();
                    trackObj.focusOnFirstInvalid();
                    return;
                }
                
                self.errorMessages.removeAll();
                var name = self.name();
                if (!name || name === "" || name.length > 64)
                {
                    self.createMessages.push(new oj.Message(getNlsString('DBS_HOME_CREATE_DLG_INVALID_NAME')));
                    trackObj.showMessages();
                    trackObj.focusOnFirstInvalid();
                    return;
                }
                
                $( "#duplicateDsbDialog" ).css("cursor", "progress");
                self.duplicateDashboard();
            };
            
            self.duplicateDashboard = function() {
                var origDashboard = $b.dashboard;
                var newDashboard = $.extend(true, {}, origDashboard);
                newDashboard.id = undefined;
                newDashboard.name = self.name();
                newDashboard.description = self.description();
                var enableTimeRange = $.isFunction(origDashboard.enableTimeRange) ? origDashboard.enableTimeRange() : origDashboard.enableTimeRange;
                var enableRefresh = $.isFunction(origDashboard.enableRefresh) ? origDashboard.enableRefresh() : origDashboard.enableRefresh;
                var systemDashboard = $.isFunction(origDashboard.systemDashboard) ? origDashboard.systemDashboard() : origDashboard.systemDashboard;
                newDashboard.enableTimeRange = (enableTimeRange === true || enableTimeRange === "TRUE" || enableTimeRange === "true") ? "true" : "false";
                newDashboard.enableRefresh = (enableRefresh === true || enableRefresh === "TRUE" || enableRefresh === "true") ? "true" : "false";
                newDashboard.systemDashboard = "false";
                if (systemDashboard === true) {
                    var succCallback = function(data) {
                        if (data && data.screenShot) {
                            newDashboard.screenShot = data.screenShot;
                        }
                        self.saveDuplicatedDashboardToServer(newDashboard);
                    };
                    var errorCallback = function(error) {
                        newDashboard.screenShot = null;
                        self.saveDuplicatedDashboardToServer(newDashboard);
                    };
                    Builder.fetchDashboardScreenshot(origDashboard.id(), succCallback, errorCallback);
                }
                else {
                    if (newDashboard.tiles() && newDashboard.tiles().length > 0) {
                        ssu.getBase64ScreenShot('.tiles-wrapper', 314, 165, 0.8, function(data) {
                            newDashboard.screenShot = data;  
                            self.saveDuplicatedDashboardToServer(newDashboard);
                        });                
                    }
                    else {
                        newDashboard.screenShot = null;
                        self.saveDuplicatedDashboardToServer(newDashboard);
                    }
                }
            };
            
            self.saveDuplicatedDashboardToServer = function(newDashboard) {
                var succCallback = function(data) {
                    $('#duplicateDsbDialog').ojDialog('close');
                    if (data && data.id) {
                        window.location.href = "/emsaasui/emcpdfui/builder.html?dashboardId=" + data.id;
                    }
                };
                var errorCallback = function(error) {
                    var errorCode = error && $.isFunction(error.errorCode) ? error.errorCode() : error.errorCode;
                    if (errorCode === 10001)
                    {
                        var trackObj = ko.utils.unwrapObservable(self.tracker);
                        self.errorMessages.push(new oj.Message(getNlsString('DBS_BUILDER_MSG_ERROR_NAME_DUPLICATED_SUMMARY'),
                                                                getNlsString('DBS_BUILDER_MSG_ERROR_NAME_DUPLICATED_DETAIL')));
                        trackObj.showMessages();
                        trackObj.focusOnFirstInvalid();
                        return;
                    }
                    else {
                        $('#duplicateDsbDialog').ojDialog('close');
                        error && error.errorMessage() && dfu.showMessage({type: 'error', 
                            summary: getNlsString('DBS_BUILDER_MSG_ERROR_IN_DUPLICATING'), 
                            detail: ''});
                    }
                };
                var dbdJs = ko.mapping.toJS(newDashboard, {
                    'include': ['screenShot', 'description', 'height', 
                        'isMaximized', 'title', 'type', 'width', 
                        'tileParameters', 'name', 'systemParameter', 
                        'value', 'content', 'linkText', "systemDashboard",
                        'WIDGET_LINKED_DASHBOARD', 'linkUrl'],
                    'ignore': ["createdOn", "href", "owner", "modeWidth","sharePublic", "modeHeight", 
                        "lastModifiedBy", "lastModifiedOn", "tileId",
                        "modeColumn", "modeRow", "screenShotHref", 
                        "customParameters", "clientGuid", "dashboard", 
                        "fireDashboardItemChangeEvent", "getParameter", 
                        "maximizeEnabled", "narrowerEnabled", 
                        "onDashboardItemChangeEvent", "restoreEnabled", 
                        "setParameter", "shouldHide", "systemParameters", 
                        "tileDisplayClass", "widerEnabled", "widget", 
                        "WIDGET_DEFAULT_HEIGHT", "WIDGET_DEFAULT_WIDTH"]
                });
                var dashboardJSON = JSON.stringify(dbdJs);
                //console.log("dashboard to dup: "+dashboardJSON);
                Builder.duplicateDashboard(dashboardJSON, function(data) {
                    succCallback && succCallback(data);
                }, function(error) {
                    errorCallback && errorCallback(error);
                });
            };
        }
        
        return {"DuplicateDashboardModel": DuplicateDashboardModel};
    }
);
