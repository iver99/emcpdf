/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'jquery',
        'ojs/ojcore',
        'dfutil',
        'uifwk/js/util/screenshot-util',
        'uifwk/js/sdk/context-util',
        'uifwk/js/sdk/menu-util'
    ],
    function(ko, $, oj, dfu, ssu, contextModel, menuModel) {
        function DuplicateDashboardModel($b) {
            var self = this;
            var ctxUtil = new contextModel();
            var menuUtil = new menuModel();
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

                $("#duplicateDsbDialog").css("cursor", "progress");
                self.duplicateDashboard();
            };

            self.cancelDuplicateDashboard = function () {
                selectedDashboardInst().toolBarModel.duplicateInSet(false);
                $('#duplicateDsbDialog').ojDialog('close');
            };

            self.duplicateDashboard = function() {
                var origDashboard = $b.dashboard;
                var newDashboard = $.extend(true, {}, origDashboard);
                newDashboard.id = undefined;
                newDashboard.name = self.name();
                newDashboard.description = self.description();
                var federationEnabled = Builder.isRunningInFederationMode();
                if (federationEnabled) {
                    newDashboard.federationSupported = "FEDERATION_ONLY";
                } else {
                    newDashboard.federationSupported = "NON_FEDERATION_ONLY";
                }
                var enableRefresh = $.isFunction(origDashboard.enableRefresh) ? origDashboard.enableRefresh() : origDashboard.enableRefresh;
                var systemDashboard = $.isFunction(origDashboard.systemDashboard) ? origDashboard.systemDashboard() : origDashboard.systemDashboard;
                newDashboard.enableRefresh = (enableRefresh === true || enableRefresh === "TRUE" || enableRefresh === "true") ? "true" : "false";
                newDashboard.systemDashboard = "false";
                newDashboard.showInHome=false;
                //If dup a dashboard in federated mode, the new dashboard can only run in federated mode. In this mode, entity selector should be disabled.
                if(Builder.isRunningInFederationMode()) {
                    newDashboard.enableEntityFilter = "FALSE";
                }
                if (!selectedDashboardInst().toolBarModel.duplicateInSet()) {
                    newDashboard.showInHome = true;
                }
                if (systemDashboard === true) {
                    newDashboard.dupDashboardId = ko.unwrap(origDashboard.id);
                    self.saveDuplicatedDashboardToServer(newDashboard, federationEnabled);
                }
                else {
                    if (newDashboard.tiles() && newDashboard.tiles().length > 0) {
                        var elem = $b.findEl('.tiles-wrapper');
                        var clone = Builder.createScreenshotElementClone(elem);
                        ssu.getBase64ScreenShot(clone, 314, 165, 0.8, function(data) {
                            newDashboard.screenShot = data;
                            Builder.removeScreenshotElementClone(clone);
                            self.saveDuplicatedDashboardToServer(newDashboard, federationEnabled);
                        });
                    }
                    else {
                        newDashboard.screenShot = null;
                        self.saveDuplicatedDashboardToServer(newDashboard, federationEnabled);
                    }
                }
            };

            self.saveDuplicatedDashboardToServer = function(newDashboard, federationEnabled) {
                var succCallback = function (data) {
                    if (selectedDashboardInst().toolBarModel.duplicateInSet()) {
                        selectedDashboardInst().dashboardsetToolBar.toolbarDuplcateInSet(data);
                    } else {
                        selectedDashboardInst().toolBarModel.duplicateInSet(false);
                        $('#duplicateDsbDialog').ojDialog('close');
                        if (data && data.id()) {
                            var url = "/emsaasui/emcpdfui/builder.html?dashboardId=" + data.id();
                            console.log("As currently it's running in " + (federationEnabled?'federation ':'non-federation ') + 'mode, duplication operation is navigating to the same mode' );
                            if (federationEnabled) {
                                url = url + "&federationEnabled=true";
                                menuUtil.fireFederatedDsbChangedEvent();
                            }
                            window.location.href = ctxUtil.appendOMCContext(url, true, true, true);
                        }
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
                        'WIDGET_LINKED_DASHBOARD', 'linkUrl','dupDashboardId'],
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
                new Builder.DashboardDataSource().duplicateDashboard(dashboardJSON, function(data) {
                    succCallback && succCallback(data);
                }, function(error) {
                    errorCallback && errorCallback(error);
                });
            };
        }

        return {"DuplicateDashboardModel": DuplicateDashboardModel};
    }
);
