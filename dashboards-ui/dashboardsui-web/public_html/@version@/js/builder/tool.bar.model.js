/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'jquery',
        'dfutil',
        'idfbcutil',
        'uifwk/js/util/screenshot-util',
        'uifwk/js/sdk/context-util',
        'ojs/ojcore',
        'builder/tool-bar/edit.dialog',
        'builder/tool-bar/duplicate.dialog',
        'uifwk/js/util/preference-util',
        'uifwk/js/util/zdt-util',
        'builder/builder.core',
        'builder/dashboardDataSource/dashboard.datasource'
    ],
    function(ko, $, dfu, idfbcutil, ssu, cxtModel, oj, ed, dd, pfu,zdtUtilModel) {
        // dashboard type to keep the same with return data from REST API
        var SINGLEPAGE_TYPE = "SINGLEPAGE";
        var DEFAULT_AUTO_REFRESH_INTERVAL = 300000;

        function ToolBarModel($b,dashboardSetOptions) {
            var self = this;
            self.$b = $b;
            $b.registerObject(self, 'ToolBarModel');
            self.dashboard = $b.dashboard;
            self.isUpdated = $b.isDashboardUpdated;
            self.isDeletingDbd = ko.observable(false);
            self.tilesViewModel = $b.getDashboardTilesViewModel();
            self.currentUser = dfu.getUserName();
            self.openRightPanelByBuild = ko.observable(true);
            self.duplicateDashboardModel = new dd.DuplicateDashboardModel($b);
            self.toolBarGuid = Builder.getGuid();
            self.isUnderSet = ko.dataFor($("#dbd-set-tabs")[0]).isDashboardSet();
            self.isInOOBDashboardSet=ko.dataFor($("#dbd-set-tabs")[0]).isOobDashboardset();
            self.duplicateInSet = ko.observable(false);
            var zdtUtil = new zdtUtilModel();
            self.zdtStatus = ko.observable(false);
            self.notZdtStatus = ko.observable(true);
            zdtUtil.detectPlannedDowntime(function (isUnderPlannedDowntime) {
//                 self.zdtStatus(true);
//                 self.notZdtStatus(false);
                self.zdtStatus(isUnderPlannedDowntime);
                self.notZdtStatus(!isUnderPlannedDowntime);
            });   
            
            if (self.dashboard.id && self.dashboard.id()){
                self.dashboardId = self.dashboard.id();
            }
            else{
                self.dashboardId = 9999; // id is expected to be available always
            }

            if(self.dashboard.name && self.dashboard.name()){
                self.dashboardName = ko.observable(self.dashboard.name());
            }else{
                self.dashboardName = ko.observable("Sample Dashboard");
            }
            self.dashboardNameEditing = ko.observable(self.dashboardName());
            if(self.dashboard.description && self.dashboard.description()){
                self.dashboardDescription = ko.observable(self.dashboard.description());
            }else{
                self.dashboardDescription = ko.observable();
            }
            if(self.dashboard.enableDescription && self.dashboard.enableDescription()) {
                self.dashboardDescriptionEnabled = ko.observable(self.dashboard.enableDescription());
            }else {
                self.dashboardDescriptionEnabled = ko.observable("FALSE");
            }
            self.dashboardDescriptionEditing = ko.observable(self.dashboardDescription());
            self.editDisabled = ko.observable(self.dashboard.type() === SINGLEPAGE_TYPE || self.dashboard.systemDashboard() || self.currentUser !== self.dashboard.owner());
            self.disableSave = ko.observable(false);
            
            if(self.isUnderSet && dashboardSetOptions && ko.isObservable(dashboardSetOptions.autoRefreshInterval)){
                self.autoRefreshInterval = ko.observable(ko.unwrap(dashboardSetOptions.autoRefreshInterval));
            }else {                
                new Builder.DashboardDataSource().loadDashboardUserOptionsData(self.dashboard.id(),
                    function(data) {
                        if(data["extendedOptions"] && JSON.parse(data["extendedOptions"]).autoRefresh) {
                            self.autoRefreshInterval = ko.observable(parseInt(JSON.parse(data["extendedOptions"]).autoRefresh.defaultValue));
                        }else if(self.tilesViewModel.dashboardExtendedOptions && self.tilesViewModel.dashboardExtendedOptions.autoRefresh) {
                            self.autoRefreshInterval = ko.observable(parseInt(self.tilesViewModel.dashboardExtendedOptions.autoRefresh.defaultValue));
                        }else {
                            self.autoRefreshInterval = ko.observable(DEFAULT_AUTO_REFRESH_INTERVAL);
                        }
                    },
                    function(jqXHR, textStatus, errorThrown) {
                        self.autoRefreshInterval = ko.observable(DEFAULT_AUTO_REFRESH_INTERVAL);
                    });
            }
            
            setAutoRefreshInterval(self.autoRefreshInterval());
            
            self.autoRefreshInterval.subscribe(function (value) {
                //save user options if it is in single dashboard mode
                if (!self.isUnderSet) {
                    $b.triggerEvent($b.EVENT_AUTO_REFRESH_CHANGED, "auto-refresh changed to "+value, value);
                }
                setAutoRefreshInterval(value);
            });

            if (window.DEV_MODE) { // for dev mode debug only
                self.changeMode = function() {
                    self.tilesViewModel.editor.changeMode(self.tilesViewModel.editor.mode === self.tilesViewModel.tabletMode ? self.tilesViewModel.normalMode : self.tilesViewModel.tabletMode);
                    self.tilesViewModel.show();
                };
            }

            function showConfirmLeaveDialog(event) {
                var _msg = getNlsString('DBS_BUILDER_CONFIRM_LEAVE_DIALOG_CONTENT');

                if (event && $b.isDashboardUpdated() === true && self.isDeletingDbd()===false && self.notZdtStatus())
                {
                    event.returnValue = _msg;
                }
                if ($b.isDashboardUpdated() === true && self.isDeletingDbd()===false && self.notZdtStatus())
                {
                    $b.findEl('.dashboard-screenshot').focus();
                    return _msg;
                }
            };
            $(window).bind("beforeunload", showConfirmLeaveDialog);

            self.intervalID = null;
            function setAutoRefreshInterval(interval) {
                self.intervalID && clearInterval(self.intervalID); // clear interval if exists
                if (interval) {
                    if (window.DEV_MODE) {
                        interval = 60000;
                    }
                    self.intervalID = setInterval(function () {
                        new Builder.DashboardDataSource().loadDashboardData(
                                $b.dashboard.id(),
                                function (dashboardInst) {
                                    self.dashboardName(dashboardInst.name());
                                    self.dashboardDescription((dashboardInst.description && dashboardInst.description()) || "");
                                }, function () {
                            console.log("update dashboard name && description  failed !");
                        });
                        if($b.getDashboardTilesViewModel().timePeriod()!=="Custom") {
                            $b.getDashboardTilesViewModel().initEnd(new Date());
                        }
                        if($("#dtpicker_"+self.dashboardId).children().get(0)) {
                            $b.triggerEvent($b.EVENT_AUTO_REFRESHING_PAGE, "auto-refreshing page");
                            ko.contextFor($("#dtpicker_"+self.dashboardId).children().get(0)).$component.applyClick();
                        }                       
                    }, interval);
                }
            };

            self.nameValidated = true;
            self.noSameNameValidator = {
                'validate' : function (value) {
                    self.nameValidated = true;
                    if (self.dashboardName() === value){
                        return true;
                    }
                    value = value + "";

                    if (value && Builder.isDashboardNameExisting(value)) {
                        $b.findEl('.builder-dbd-name-input').focus();
                        self.nameValidated = false;
                        throw new oj.ValidatorError(oj.Translations.getTranslatedString("DBS_BUILDER_SAME_NAME_EXISTS_ERROR"));
                    }
                    return true;
                }
            };

            self.handleDeleteDashboardClicked = function() {
               var cxtUtil = new cxtModel();
		 var _url="/sso.static/dashboards.service/";
                if (dfu.isDevMode()){
                    _url=dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint,"dashboards/");
                }
                dfu.ajaxWithRetry(_url + self.dashboard.id(), {
                    type: 'DELETE',
                    headers: dfu.getDashboardsRequestHeader(),
                    success: function (result) {
                        if (selectedDashboardInst().toolBarModel.isUnderSet) {
                            var removeId = selectedDashboardInst().toolBarModel.dashboardId;
                            var selectedTab = $('#dashboardTab-' + removeId);
                            $('#delete-dashboard').ojDialog("close");
                            selectedDashboardInst().dashboardsetToolBar.removeDashboardInSet(removeId, selectedTab, true);
                            $("#dbd-tabs-container").ojTabs("refresh");
                        } else {
                            if (self.isHomeDashboard) {                                
                                localStorage.deleteHomeDbd=true;
                            }
                            window.location = cxtUtil.appendOMCContext( document.location.protocol + '//' + document.location.host + '/emsaasui/emcpdfui/home.html');
                        }     
                    },
                    error: function(jqXHR, textStatus, errorThrown) {}
                });
            };

            self.handleDeleteDashboardCancelled = function() {
                $('#delete-dashboard').ojDialog( "close" );
            };
            
            self.handleUnshareDashboardClicked = function() {
              self.handleShareUnshare(false);
              $('#share-dashboard').ojDialog( "close" ); 
            };
            
            self.handleUnshareDashboardCancelled = function() {
                // revert change
                var dashboardSharing = ko.dataFor($(".share-settings")[0]).dashboardSharing;
                dashboardSharing("shared");
                $('#share-dashboard').ojDialog( "close" );
            };

            self.isNameUnderEdit = function() {
                return $b.findEl('.builder-dbd-name').hasClass('editing');
            };

            self.isDescriptionUnderEdit = function() {
                return $b.findEl('.builder-dbd-description').hasClass('editing');
            };

            /*self.handleStartEditText = function () {
                self.disableSave(true);
                self.tilesViewModel.tilesView.disableDraggable();
            };

            self.handleStopEditText = function (showErrorMsg) {
                if (showErrorMsg) {
                    self.disableSave(true);
                } else {
                    self.disableSave(false);
                }
                self.tilesViewModel.tilesView.enableDraggable();
            };*/

            self.getSummary = function(dashboardId, name, description, tilesViewModel) {
                function dashboardSummary(name, description) {
                    var self = this;
                    self.dashboardId = dashboardId;
                    self.dashboardName = name;
                    self.dashboardDescription = description;
                    self.widgets = [];
                }

                var summaryData = new dashboardSummary(name, description);
                if (tilesViewModel) {
                    for (var i = 0; i < tilesViewModel.dashboard.tiles().length; i++) {
                        var tile = tilesViewModel.dashboard.tiles()[i];
                        var widget = {"title": tile.title()};
                        summaryData.widgets.push(widget);
                    }
                }
                return summaryData;
            };

/*  functions that are declared but never used:
            self.setAncestorsOverflowVisible = function() {
                $("#tiles-col-container").css("overflow", "visible");
                $("body").css("overflow", "visible");
                $("html").css("overflow", "visible");
            };

            self.resetAncestorsOverflow = function() {
                $("#tiles-col-container").css("overflow-x", "hidden");
                $("#tiles-col-container").css("overflow-y", "auto");
                $("body").css("overflow", "hidden");
                $("html").css("overflow", "hidden");
            };
*/


            self.handleDashboardSave = function() {
                var outputData = self.getSummary(self.dashboardId, self.dashboardName(), self.dashboardDescription(), self.tilesViewModel);
                outputData.eventType = "SAVE";

                if (self.tilesViewModel.dashboard.tiles() && self.tilesViewModel.dashboard.tiles().length > 0) {
                    var elem = $b.findEl('.tiles-wrapper');
                    var clone = Builder.createScreenshotElementClone(elem);
                    ssu.getBase64ScreenShot(clone, 314, 165, 0.8, function(data) {
                        outputData.screenShot = data;
                        Builder.removeScreenshotElementClone(clone);
                        self.tilesViewModel.dashboard.screenShot = ko.observable(data);
                        self.handleSaveUpdateDashboard(outputData);
                    });
                }
                else {
                    self.tilesViewModel.dashboard.screenShot = ko.observable(null);
                    self.handleSaveUpdateDashboard(outputData);
                }
            };

            self.handleSaveUpdateDashboard = function(outputData) {
                self.handleSaveUpdateToServer(function() {
                    self.isUpdated(false);
                    dfu.showMessage({
                            type: 'confirm',
                            summary: getNlsString('DBS_BUILDER_MSG_CHANGES_SAVED'),
                            detail: '',
                            removeDelayTime: 5000
                    });
                }, function(error) {
                    error && error.errorMessage() && dfu.showMessage({type: 'error', summary: getNlsString('DBS_BUILDER_MSG_ERROR_IN_SAVING'), detail: '', removeDelayTime: 5000});
                });
            };

            self.handleSaveUpdateToServer = function(succCallback, errorCallback) {
                if(self.isUnderSet){
                   console.log("This is a dashboard in set, send its parent set id...");
                   self.tilesViewModel.dashboard.dupDashboardId=selectedDashboardInst().dashboardsetToolBar.dashboardInst.id()
                }                
                var dbdJs = ko.mapping.toJS(self.tilesViewModel.dashboard, {
                    'include': ['screenShot', 'description', 'height',
                        'isMaximized', 'title', 'type', 'width',
                        'tileParameters', 'name', 'systemParameter',
                        'tileId', 'value', 'content', 'linkText',
                        'WIDGET_LINKED_DASHBOARD', 'linkUrl','dupDashboardId'],
                    'ignore': ["createdOn", "href", "owner", "modeWidth", "modeHeight",
                        "modeColumn", "modeRow", "screenShotHref", "systemDashboard",
                        "customParameters", "clientGuid", "dashboard",
                        "fireDashboardItemChangeEvent", "getParameter",
                        "maximizeEnabled", "narrowerEnabled",
                        "onDashboardItemChangeEvent", "restoreEnabled",
                        "setParameter", "shouldHide", "systemParameters",
                        "tileDisplayClass", "widerEnabled", "widget",
                        "WIDGET_DEFAULT_HEIGHT", "WIDGET_DEFAULT_WIDTH"]
                });
                dbdJs.tiles.forEach(function(oneTile){
                    if(oneTile.isMaximized){
                        oneTile.isMaximized=false;
                    }
                });
                var dashboardJSON = JSON.stringify(dbdJs);
                var dashboardId = self.tilesViewModel.dashboard.id();
                new Builder.DashboardDataSource().updateDashboardData(dashboardId, dashboardJSON, function() {
                        succCallback && succCallback();
                }, function(error) {
                    console.error(error.errorMessage());
                    errorCallback && errorCallback(error);
                });
            };
                  
            var prefUtil = new pfu(dfu.getPreferencesUrl(), dfu.getDashboardsRequestHeader());
            var addFavoriteLabel = getNlsString('DBS_BUILDER_BTN_FAVORITES_ADD');
            var addFavoriteName = "Add Favorite";
            var removeFavoriteLabel = getNlsString('DBS_BUILDER_BTN_FAVORITES_REMOVE');
            var removeFavoriteName = "Remove Favorite";
            var setAsHomeLabel = getNlsString('DBS_BUILDER_BTN_HOME_SET');
            var removeAsHomeLabel = getNlsString('DBS_BUILDER_BTN_HOME_REMOVE');
            var setAsHomeName = "Set as Home";
            var removeAsHomeName = "Remove as Home";
            var prefKeyHomeDashboardId = "Dashboards.homeDashboardId";
            var cssSetDsbAsHome = "dbd-toolbar-icon-home";
            var cssRemoveDsbAsHome = "dbd-toolbar-icon-home";
            var cssAddFavorite = "fa-star dbd-toolbar-fa-icon";
            var cssRemoveFavorite = "fa-star-o dbd-toolbar-fa-icon";
            var shareDashboardLabel = getNlsString('COMMON_TEXT_SHARE');
            var unshareDashboardLabel = getNlsString('COMMON_TEXT_UNSHARE');
            var shareDashboardTitle = getNlsString('COMMON_TEXT_SHARE_TITLE');
            var unshareDashboardTitle = getNlsString('COMMON_TEXT_UNSHARE_TITLE');
            var cssShareDashboard = "dbd-toolbar-icon-share dbd-toolbar-fa-icon dbd-toolbar-fa-icon-16";
            var cssUnshareDashboard = "dbd-toolbar-icon-unshare dbd-toolbar-fa-icon";
            self.sharePublicLabel = ko.observable(self.dashboard.sharePublic() === false ? shareDashboardLabel : unshareDashboardLabel);
            self.sharePublicTitle = ko.observable(self.dashboard.sharePublic() === false ? shareDashboardTitle : unshareDashboardTitle);
            self.cssSharePublic = ko.observable(self.dashboard.sharePublic() === false ? cssShareDashboard : cssUnshareDashboard);
            self.dashboardsAsHomeIcon = ko.observable(cssSetDsbAsHome);
            self.favoritesIcon = ko.observable(cssAddFavorite);
            self.isSystemDashboard = self.dashboard.systemDashboard();
            self.favoriteLabel = ko.observable(addFavoriteLabel);
            self.favoriteName = ko.observable(addFavoriteName);
            self.dashboardAsHomeLabel = ko.observable(setAsHomeLabel);
            self.dashboardAsHomeName= ko.observable(setAsHomeName);
            self.isFavoriteDashboard = false;
            self.isHomeDashboard = false;
            self.hasAnotherDashboardSetAsHome = false;

            //Check dashboard favorites
            checkDashboardFavorites();

            //Check home dashboard preferences
            checkDashboardAsHomeSettings();

            self.handleShareUnshare = function(isToShare) {
                var _shareState = self.dashboard.sharePublic();
                if(_shareState === isToShare ) {
                    return ;
                }
                var _url = dfu.isDevMode() ? dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint, "dashboards/") : "/sso.static/dashboards.service/";
                self.dashboard.sharePublic(isToShare);               
                dfu.ajaxWithRetry(_url + self.dashboard.id() + "/quickUpdate", {
                        type: 'PUT',
                        dataType: "json",
                        contentType: 'application/json',
                        data: JSON.stringify({sharePublic: isToShare}),
                        headers: dfu.getDashboardsRequestHeader(),
                        success: function (result) {
                            if (self.dashboard.sharePublic() === true)
                            {
                                self.sharePublicLabel(unshareDashboardLabel);
                                self.sharePublicTitle(unshareDashboardTitle);
                                self.cssSharePublic(cssUnshareDashboard);
                                dfu.showMessage({type: 'confirm', summary: getNlsString('COMMON_TEXT_SHARE_CONFIRM_SUMMARY'), detail: getNlsString('COMMON_TEXT_SHARE_CONFIRM_DETAIL'), removeDelayTime: 5000});
                            }
                            else
                            {
                                self.sharePublicLabel(shareDashboardLabel);
                                self.sharePublicTitle(shareDashboardTitle);
                                self.cssSharePublic(cssShareDashboard);
                                dfu.showMessage({type: 'confirm', summary: getNlsString('COMMON_TEXT_UNSHARE_CONFIRM_SUMMARY'), detail: getNlsString('COMMON_TEXT_UNSHARE_CONFIRM_DETAIL'), removeDelayTime: 5000});
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            self.dashboard.sharePublic(!isToShare); 
                            dfu.showMessage({type: 'error', summary: getNlsString('DBS_BUILDER_MSG_ERROR_IN_SAVING'), detail: '', removeDelayTime: 5000});
                        }
                    });
            };
            
            self.openDashboardEditDialog = function() {
                self.notifyRightPanelChange("singleDashboard-edit");      
            };
            
            self.notifyRightPanelChange = function (changeEvent) {
                Builder.rightPanelChange(changeEvent);
            };
            
            self.openDashboardDuplicateDialog = function() {
                $('#duplicateDsbDialog').ojDialog('open');
            };
            self.openDashboardDeleteConfirmDialog = function() {
                self.isDeletingDbd(true);
                $('#delete-dashboard').ojDialog( "open" );
                $('#delete-dashboard').focus();
            };
            self.openDashboardUnshareConfirmDialog = function() {
                $('#share-dashboard').ojDialog( "open" );
                $('#share-dashboard').focus();
            };

            self.addDashboardToFavorites = function() {
                function succCallback(data) {
                    dfu.showMessage({
                            type: 'confirm',
                            summary: getNlsString('DBS_BUILDER_MSG_ADD_FAVORITE_SUCC', self.dashboard.name()),
                            detail: '',
                            removeDelayTime: 5000
                    });
                    self.favoriteLabel(removeFavoriteLabel);
                    self.favoritesIcon(cssRemoveFavorite);
                    self.favoriteName(removeFavoriteName);
                    self.isFavoriteDashboard = true;
                }
                function errorCallback(jqXHR, textStatus, errorThrown) {
                    dfu.showMessage({
                            type: 'error',
                            summary: getNlsString('DBS_BUILDER_MSG_ADD_FAVORITE_FAIL'),
                            detail: ''
                    });
                }
                new Builder.DashboardDataSource().addDashboardToFavorites(self.dashboard.id(), succCallback, errorCallback);
            };
            self.removeDashboardFromFavorites = function() {
                function succCallback(data) {
                    dfu.showMessage({
                            type: 'confirm',
                            summary: getNlsString('DBS_BUILDER_MSG_REMOVE_FAVORITE_SUCC', self.dashboard.name()),
                            detail: '',
                            removeDelayTime: 5000
                    });
                    self.favoriteLabel(addFavoriteLabel);
                    self.favoritesIcon(cssAddFavorite);
                    self.favoriteName(addFavoriteName);
                    self.isFavoriteDashboard = false;
                }
                function errorCallback(jqXHR, textStatus, errorThrown) {
                    dfu.showMessage({
                            type: 'error',
                            summary: getNlsString('DBS_BUILDER_MSG_REMOVE_FAVORITE_FAIL'),
                            detail: ''
                    });
                }
                new Builder.DashboardDataSource().removeDashboardFromFavorites(self.dashboard.id(), succCallback, errorCallback);
            };
            self.handleDashboardFavorites = function() {
                if (self.isFavoriteDashboard) {
                    self.removeDashboardFromFavorites();
                }
                else {
                    self.addDashboardToFavorites();
                }
            };
            self.setAsHomeConfirmed = function() {
                self.setDashboardAsHome();
                $("#setAsHomeCfmDialog").ojDialog("close");
            };
            self.setAsHomeCancelled = function() {
                $("#setAsHomeCfmDialog").ojDialog("close");
            };
            self.setDashboardAsHome = function() {
                function succCallback(data) {
                    dfu.showMessage({
                            type: 'confirm',
                            summary: getNlsString('DBS_BUILDER_MSG_SET_AS_HOME_SUCC', self.dashboard.name()),
                            detail: '',
                            removeDelayTime: 5000
                    });
                    self.dashboardAsHomeLabel(removeAsHomeLabel);
                    self.dashboardAsHomeName(removeAsHomeName);
                    self.dashboardsAsHomeIcon(cssRemoveDsbAsHome);
                    self.isHomeDashboard = true;
                    self.hasAnotherDashboardSetAsHome = false;
                }
                function errorCallback(jqXHR, textStatus, errorThrown) {
                    dfu.showMessage({
                            type: 'error',
                            summary: getNlsString('DBS_BUILDER_MSG_SET_AS_HOME_FAIL'),
                            detail: ''
                    });
                }
                var options = {
                    success: succCallback,
                    error: errorCallback
                };
                prefUtil.setPreference(prefKeyHomeDashboardId, self.dashboard.id(), options);
            };
            self.removeDashboardAsHome = function() {
                function succCallback(data) {
                    dfu.showMessage({
                            type: 'confirm',
                            summary: getNlsString('DBS_BUILDER_MSG_REMOVE_AS_HOME_SUCC', self.dashboard.name()),
                            detail: '',
                            removeDelayTime: 5000
                    });
                    self.dashboardAsHomeLabel(setAsHomeLabel);
                    self.dashboardAsHomeName(setAsHomeName);
                    self.dashboardsAsHomeIcon(cssSetDsbAsHome);
                    self.isHomeDashboard = false;
                    self.hasAnotherDashboardSetAsHome = false;
                }
                function errorCallback(jqXHR, textStatus, errorThrown) {
                    dfu.showMessage({
                            type: 'error',
                            summary: getNlsString('DBS_BUILDER_MSG_REMOVE_AS_HOME_FAIL'),
                            detail: ''
                    });
                }
                var options = {
                    success: succCallback,
                    error: errorCallback
                };
                prefUtil.removePreference(prefKeyHomeDashboardId, options);
            };
            self.handleDashboardAsHome = function() {
                if (self.isHomeDashboard) {
                    self.removeDashboardAsHome();
                }
                else {
                    if (self.hasAnotherDashboardSetAsHome) {
                        $("#setAsHomeCfmDialog").ojDialog("open");
                        $("#btnCancelSetAsHome").focus();
                    }
                    else {
                        self.setDashboardAsHome();
                    }
                }
            };

            function checkDashboardFavorites() {
                function succCallback(data) {
                    if (data && data.isFavorite === true) {
                        self.favoriteLabel(removeFavoriteLabel);
                        self.favoritesIcon(cssRemoveFavorite);
                        self.favoriteName(removeFavoriteName);
                        self.isFavoriteDashboard = true;
                    }
                    else {
                        self.favoriteLabel(addFavoriteLabel);
                        self.favoritesIcon(cssAddFavorite);
                        self.favoriteName(addFavoriteName);
                        self.isFavoriteDashboard = false;
                    }
                }
                function errorCallback(jqXHR, textStatus, errorThrown) {
                    self.favoriteLabel(addFavoriteLabel);
                    self.favoritesIcon(cssAddFavorite);
                    self.favoriteName(addFavoriteName);
                    self.isFavoriteDashboard = false;
                }
                new Builder.DashboardDataSource().checkDashboardFavorites(self.dashboard.id(), succCallback, errorCallback);
            }
            function checkDashboardAsHomeSettings() {
                function succCallback(data) {
                    var homeDashboardId;
                    new Builder.DashboardDataSource().getHomeDashboardPreference(self.dashboard.id(), function (resp) {
                        if(typeof(resp) !== "undefined") {
                            homeDashboardId = resp.value;
                        }
                    });
                    if (homeDashboardId && homeDashboardId === (self.dashboard.id())) {
                        self.dashboardAsHomeLabel(removeAsHomeLabel);
                        self.dashboardAsHomeName(removeAsHomeName);
                        self.dashboardsAsHomeIcon(cssRemoveDsbAsHome);
                        self.isHomeDashboard = true;
                    }
                    else if (homeDashboardId){
                        self.dashboardAsHomeLabel(setAsHomeLabel);
                        self.dashboardAsHomeName(setAsHomeName);
                        self.dashboardsAsHomeIcon(cssSetDsbAsHome);
                        self.isHomeDashboard = false;
                        self.hasAnotherDashboardSetAsHome = true;
                    }
                    else {
                        self.dashboardAsHomeLabel(setAsHomeLabel);
                        self.dashboardAsHomeName(setAsHomeName);
                        self.dashboardsAsHomeIcon(cssSetDsbAsHome);
                        self.isHomeDashboard = false;
                        self.hasAnotherDashboardSetAsHome = false;
                    }
                }
                function errorCallback(jqXHR, textStatus, errorThrown) {
                    self.dashboardAsHomeLabel(setAsHomeLabel);
                    self.dashboardAsHomeName(setAsHomeName);
                    self.dashboardsAsHomeIcon(cssSetDsbAsHome);
                    self.isHomeDashboard = false;
                    self.hasAnotherDashboardSetAsHome = false;
                }
                var options = {
                    success: succCallback,
                    error: errorCallback
                };
                if(!self.isUnderSet){
                	new Builder.DashboardDataSource().getHomeDashboardPreference(self.dashboard.id(), options.success, options.error);
//                    prefUtil.getAllPreferences(options);
                }
            }

            self.optionMenuItemSelect = function (event,data) {
                var $clickTarget=data.item;
                var clickTargetName = $clickTarget.attr('data-singledb-option');
                switch (clickTargetName) {
                    case "Edit":
                        self.editDisabled() === true ? "" : self.openDashboardEditDialog();
                        break;
                    case "Print":
                        $(".dashboard-content-main:hidden").each(function (index, currentValue) {
                            $(currentValue).addClass("no-print");
                        });
                        window.print();
                        $(".dashboard-content-main").each(function (index, currentValue) {
                            $(currentValue).removeClass("no-print");
                        });
                        break;
                    case "Duplicate":
                        if ((!self.isUnderSet) || (self.currentUser !== self.dashboard.owner())) {
                            self.openDashboardDuplicateDialog();
                        }
                        break;
                    case "Add to set":
                        self.duplicateInSet(true);
                        self.openDashboardDuplicateDialog();
                        break;
                    case "Do not add to set":
                        self.duplicateInSet(false);
                        self.openDashboardDuplicateDialog();
                        break;
                    case "Add Favorite":
                        self.handleDashboardFavorites();
                        break;
                    case "Remove Favorite":
                        self.handleDashboardFavorites();
                        break;
                    case "Set as Home":
                        self.handleDashboardAsHome();
                        break;
                    case "Remove as Home":
                        self.handleDashboardAsHome();
                        break;
                    //refresh off
                    case "Off":
                        $clickTarget.closest("ul").find(".oj-menu-item-icon").removeClass("fa-check");
                        $clickTarget.find(".oj-menu-item-icon").addClass("fa-check");
                        event.stopPropagation();
                        self.autoRefreshInterval(0);
                        dfu.showMessage({type: 'confirm', summary: getNlsString('DBS_BUILDER_MSG_AUTO_REFRESH_OFF'), detail: '', removeDelayTime: 5000});
                        break;
                    case "On (Every 5 Minutes)":
                        $clickTarget.closest("ul").find(".oj-menu-item-icon").removeClass("fa-check");
                        $clickTarget.find(".oj-menu-item-icon").addClass("fa-check");
                        event.stopPropagation();
                        self.autoRefreshInterval(DEFAULT_AUTO_REFRESH_INTERVAL);// 5 minutes
                        dfu.showMessage({type: 'confirm', summary: getNlsString('DBS_BUILDER_MSG_AUTO_REFRESH_ON'), detail: '', removeDelayTime: 5000});
                        break;
                    default:
                        break;
                }
            };

            self.dashboardOptsMenuItems = [         
                {
                    "label": getNlsString('COMMON_BTN_EDIT'),
                    "url": "#",
                    "id": "emcpdf_dsbopts_edit" + self.toolBarGuid,
                    "name":"Edit",
                    "icon": "dbd-toolbar-icon-edit",
                    "title": "",
                    "disabled": self.editDisabled() === true,
                    "showOnMobile": self.tilesViewModel.isMobileDevice !== "true",
                    "showSubMenu": false,
                    "showInZdt":self.notZdtStatus,
                    "endOfGroup": false
                },
                {
                    "label": getNlsString('COMMON_BTN_PRINT'),
                    "url": "#",
                    "id": "emcpdf_dsbopts_print" + self.toolBarGuid,
                    "name":"Print",
                    "icon": "dbd-toolbar-icon-print",
                    "title": "",
                    "disabled": false,
                    "showOnMobile": true,
                    "showSubMenu": false,
                    "showInZdt":true,
                    "endOfGroup": false
                },
                {
                    "label": getNlsString('DBS_BUILDER_BTN_DUPLICATE'),
                    "url": "#",
                    "id": "emcpdf_dsbopts_duplicate" + self.toolBarGuid,
                    "name":"Duplicate",
                    "icon": "dbd-toolbar-icon-duplicate",
                    "title": "",
                    "disabled": false,
                    "showOnMobile": self.tilesViewModel.isMobileDevice !== "true",
                    "showInZdt":self.notZdtStatus,
                    "endOfGroup": true && self.notZdtStatus,
                    "showSubMenu": function () {
                        if (self.currentUser !== self.dashboard.owner() && "Oracle" !== self.dashboard.owner()) {
                            return false;
                        } else if(self.isInOOBDashboardSet){
                            return false;
                        } else{
                            return self.isUnderSet;
                        }
                    }(),
                    "subItems": [
                        {
                            "label": getNlsString('DBS_BUILDER_ADDTOSET'),
                            "url": "#",
                            "id": "emcpdf_dsbopts_addToSet" + self.toolBarGuid,
                            "name":"Add to set",
                            "icon": "",
                            "title": "",
                            "disabled": false,
                            "showOnMobile": true,
                            "showSubMenu": false,
                            "showInZdt":self.notZdtStatus,
                            "endOfGroup": false
                        },
                        {
                            "label": getNlsString('DBS_BUILDER_NOT_ADDTOSET'),
                            "url": "#",
                            "id": "emcpdf_dsbopts_notAddToSet" + self.toolBarGuid,
                            "name":"Do not add to set",
                            "icon": "",
                            "title": "",
                            "disabled": false,
                            "showOnMobile": true,
                            "showSubMenu": false,
                            "showInZdt":self.notZdtStatus,
                            "endOfGroup": false
                        }
                    ]
                },
                {
                    "label": self.favoriteLabel,
                    "url": "#",
                    "id": "emcpdf_dsbopts_favorites" + self.toolBarGuid,
                    "name":self.favoriteName,
                    "icon": self.favoritesIcon, //"dbd-toolbar-icon-favorites",
                    "title": "", //self.favoriteLabel,
                    "disabled": false,
                    "showOnMobile": true,
                    "showSubMenu": false,
                    "showInZdt":self.notZdtStatus,
                    "endOfGroup": false
                },
                {
                    "label": self.dashboardAsHomeLabel,
                    "url": "#",
                    "id": "emcpdf_dsbopts_home" + self.toolBarGuid,
                    "name":self.dashboardAsHomeName,
                    "icon": self.dashboardsAsHomeIcon,
                    "title": "", //self.setAsHomeLabel,
                    "disabled": false,
                    "showOnMobile": true,
                    "showSubMenu": false,
                    "showInZdt":self.notZdtStatus,
                    "endOfGroup": false
                },
                {
                    "label": getNlsString('DBS_BUILDER_AUTOREFRESH_REFRESH'),
                    "url": "#",
                    "id": "emcpdf_dsbopts_refresh" + self.toolBarGuid,
                    "name":"Auto-refresh",
                    "icon": "dbd-toolbar-icon-refresh",
                    "title": "",
                    "disabled": false,
                    "showOnMobile": true,
                    "showSubMenu": true,
                    "showInZdt":self.notZdtStatus,
                    "endOfGroup": false,
                    "subItems": [
                        {
                            "label": getNlsString('DBS_BUILDER_AUTOREFRESH_OFF'),
                            "url": "#",
                            "id": "emcpdf_dsbopts_refresh_off" + self.toolBarGuid,
                            "name":"Off",
                            "icon": ko.computed(function () {
                                return self.autoRefreshInterval() === 0 ? "fa-check" : "";
                            }),
                            "title": "",
                            "disabled": false,
                            "showOnMobile": true,
                            "showSubMenu": false,
                            "showInZdt":self.notZdtStatus,
                            "endOfGroup": false
                        },
                        {
                            "label": getNlsString('DBS_BUILDER_AUTOREFRESH_ON'),
                            "url": "#",
                            "id": "emcpdf_dsbopts_refresh_on" + self.toolBarGuid,
                            "name":"On (Every 5 Minutes)",
                            "icon": ko.computed(function () {
                                return self.autoRefreshInterval() ? "fa-check" : "";
                            }),
                            "title": "",
                            "disabled": false,
                            "showOnMobile": true,
                            "showSubMenu": false,
                            "showInZdt":self.notZdtStatus,
                            "endOfGroup": false
                        }
                    ]
                }
            ];
        }

        Builder.registerModule(ToolBarModel, 'ToolBarModel');
        return ToolBarModel;
    }
);

