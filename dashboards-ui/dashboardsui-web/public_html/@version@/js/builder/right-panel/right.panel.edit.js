define([
    'knockout',
    'ojs/ojcore',
    'jquery',
    'dfutil'
], function (ko, oj, $, dfu) {

    function rightPanelEditModel($b, dashboardsetToolBarModel) {
        var self = this;
        self.$b = ko.observable($b);
        self.dashboardsetToolBarModel=dashboardsetToolBarModel;
        self.isDashboardSet = self.dashboardsetToolBarModel.isDashboardSet;
        self.dashboard = ko.computed(function () {
            return self.$b && self.$b().dashboard;
        });
        self.toolbarModel = ko.computed(function () {
            return self.$b && self.$b().getToolBarModel && self.$b().getToolBarModel();
        });
        self.isChangeDbdInSet = ko.observable(false);
        self.editDashboardDialogModel = ko.observable(null);

        self.$b.subscribe(function () {
            self.isChangeDbdInSet(true);
        });

        self.deleteDashboardClicked = function () {
            self.dashboardsetToolBarModel.renderDeletionDialogs(true);
            queryDashboardSetsBySubId(self.dashboard().id(), function (resp) {
                window.selectedDashboardInst().dashboardSets && window.selectedDashboardInst().dashboardSets(resp.dashboardSets || []);
                window.selectedDashboardInst().linkedDashboardList && window.selectedDashboardInst().linkedDashboardList(resp.linkedDashboardList);
                self.toolbarModel().openDashboardDeleteConfirmDialog();
            });
        };

        $("#delete-dashboard").on("ojclose", function (event, ui) {
            self.toolbarModel().isDeletingDbd(false);
        });

        self.deleteDashboardSetClicked = function () {
            self.dashboardsetToolBarModel.renderDeletionDialogs(true);
            $('#deleteDashboardset').ojDialog("open");
        };

        //edit function
        self.showdbOnHomePage = ko.observable([]);
        var dsbSaveDelay = ko.computed(function () {
            if (self.editDashboardDialogModel()) {
                return self.editDashboardDialogModel().showdbDescription() + self.editDashboardDialogModel().name() + self.editDashboardDialogModel().description() + self.showdbOnHomePage();
            }
        });
        dsbSaveDelay.extend({rateLimit: {method: "notifyWhenChangesStop", timeout: 800}});
        dsbSaveDelay.subscribe(function () {
            if (!self.dashboard().systemDashboard || !self.dashboard().systemDashboard()) {
                if (!self.isChangeDbdInSet()) {
                    if(self.editDashboardDialogModel()){
                        if(self.editDashboardDialogModel().name() && self.editDashboardDialogModel().name().trim()){
                            self.editDashboardDialogModel().save();
                        }else{
                            $("#dbsHNameIn:visible").ojInputText('validate');
                        }
                    }
                } else {
                    self.isChangeDbdInSet(false);
                }
            }
        });

        //edit dashboardset function
        if (self.isDashboardSet()) {
            self.dashboardsetName = ko.observable(self.dashboardsetToolBarModel.dashboardsetName());
            self.dashboardsetDescription = ko.observable(self.dashboardsetToolBarModel.dashboardsetDescription());
            self.dashboardsetNameInputed = ko.observable(self.dashboardsetName());
            self.dashboardsetDescriptionInputed = ko.observable(self.dashboardsetDescription());

            var prevSharePublic = self.dashboardsetToolBarModel.dashboardsetConfig.share();
            self.dashboardsetShare = ko.observable(prevSharePublic);
            var isOnlyDashboardPicker = self.dashboardsetToolBarModel.dashboardsetItems.length === 1 && self.dashboardsetToolBarModel.dashboardsetItems[0].type === "new";
            self.dashboardsetShareDisabled = ko.observable(isOnlyDashboardPicker);

            self.defaultSetAutoRefreshValue = ko.observable("every5minutes"); // todo get from instance

            self.dashboardsetNameInputed.subscribe(function (val) {
                self.dashboardsetName(val);
                self.dashboardsetToolBarModel.dashboardsetName(val);
            });
            
            self.resetEmptyDashboardName = function(){
                if(!self.dashboardsetNameInputed() || !self.dashboardsetNameInputed().trim()){
                    self.dashboardsetToolBarModel.dashboardsetName(self.dashboardsetToolBarModel.dashboardInst.name());
                    self.dashboardsetName(self.dashboardsetToolBarModel.dashboardInst.name());
                }
                if(self.editDashboardDialogModel()){
                    self.editDashboardDialogModel().resetEmptyDashboardName();
                }
            };
            
            self.dashboardsetDescriptionInputed.subscribe(function (val) {
                self.dashboardsetDescription(val);
            });
            var dsbSetSaveDelay = ko.computed(function () {
                return self.dashboardsetName() + self.dashboardsetDescription() + self.dashboardsetShare();
            });
            dsbSetSaveDelay.extend({rateLimit: {method: "notifyWhenChangesStop", timeout: 800}});
            
            self.saveMessageId = null;
            //todo called when refresh page
            dsbSetSaveDelay.subscribe(function () {
                if(prevSharePublic !== self.dashboardsetShare() && self.dashboardsetShare()==="on") {
                    var option = {"autoRefresh":{"defaultValue":self.dashboardsetToolBarModel.autoRefreshInterval()}};
                    if(!self.dashboardsetToolBarModel.dashboardInst.extendedOptions) {
                        self.dashboardsetToolBarModel.dashboardInst.extendedOptions = ko.observable();
                    }
                    self.dashboardsetToolBarModel.dashboardInst.extendedOptions(JSON.stringify(option));
                }
                if(!self.dashboardsetName() || !(self.dashboardsetName().trim())){
                    $("#dbsSetHNameIn:visible").ojInputText('validate');
                    return;
                }
                self.dashboardsetToolBarModel.saveDashboardSet(
                        {
                            "name": self.dashboardsetName(),
                            "description": self.dashboardsetDescription(),
                            "sharePublic": self.dashboardsetShare() === "on" ? true : false
                        },
                function (result) {
//                    dfu.showMessage({type: 'correct'});
                    if (self.saveMessageId) {
                        dfu.removeMessage(self.saveMessageId);
                    }
                    var sharePublic = result.sharePublic() === true ? "on" : "off";
                    if (sharePublic !== prevSharePublic) {
                        var shareMsgKey = result.sharePublic() ? 'DBS_BUILDER_DASHBOARD_SET_SHARE_SUCCESS' : 'DBS_BUILDER_DASHBOARD_SET_SHARE_ERROR';
                        dfu.showMessage({
                            type: 'confirm',
                            summary: getNlsString(shareMsgKey),
                            detail: '',
                            removeDelayTime: 5000
                        });
                        prevSharePublic = sharePublic;
                    }
                    

                    self.dashboardsetShare(result.sharePublic() === true ? "on" : "off");
                    self.notifyDashboardsetToolBarChange("dashboardsetName",result.name());
                    self.notifyDashboardsetToolBarChange("dashboardsetDes",result.description());
                }, 
                function (jqXHR, textStatus, errorThrown) {
                    if (self.saveMessageId) {
                        dfu.removeMessage(self.saveMessageId);
                    }
            		if (jqXHR.errorCode() === 10001)
            		{
            			_m = getNlsString('COMMON_DASHBAORD_SAME_NAME_ERROR');
            			_mdetail = getNlsString('COMMON_DASHBAORD_SAME_NAME_ERROR_DETAIL');
            			self.saveMessageId = dfu.showMessage({type: 'error', summary: _m, detail: _mdetail});
            		} else {
            			self.saveMessageId = dfu.showMessage({type: 'error', summary: getNlsString('DBS_BUILDER_MSG_ERROR_IN_SAVING'), detail: ''});                             
            		}
                         }
                );
            });
        }


        //share dashboard fucntion  
        self.dashboardSharing = ko.observable(self.dashboard().sharePublic() ? "shared" : "notShared");
        self.dashboardSharing.subscribe(function (val) {
            if (!self.toolbarModel || self.isDashboardSet()) {
                // return if current selected tab is dashboard picker
                return;
            }
            if ("notShared" === val) {
                queryDashboardSetsBySubId(self.dashboard().id(), function (resp) {
                    var currentUser = dfu.getUserName();
                    var setsSharedByOthers = resp.dashboardSets || [];
                    setsSharedByOthers = setsSharedByOthers.filter(function (dbs) {
                        return dbs.owner !== currentUser;
                    });
                    if (setsSharedByOthers.length > 0) {
                        window.selectedDashboardInst().dashboardSets && window.selectedDashboardInst().dashboardSets(setsSharedByOthers);
                        self.toolbarModel().openDashboardUnshareConfirmDialog(function (isShared) {
                            if (isShared) {
                                self.dashboardSharing("shared");
                            }
                        });
                    } else {
                        self.toolbarModel().handleShareUnshare(false);
                    }
                });
            } else {
                //set default filter/auto-refresh values when sharing
                $b.triggerEvent($b.EVENT_DASHBOARD_SHARE_CHANGED, "dashboard share settings is set to true", true);
                self.toolbarModel().handleShareUnshare(true);
            }
        });
        
        self.notifyDashboardsetToolBarChange = function (changeName,value) {
            Builder.dashboardsetToolBarChange(changeName,value);
        };

        function queryDashboardSetsBySubId(dashboardId, callback) {
            var _url = dfu.isDevMode() ? dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint, "dashboards/") : "/sso.static/dashboards.service/";
            dfu.ajaxWithRetry(_url + dashboardId + "/dashboardsets", {
                type: 'GET',
                headers: dfu.getDashboardsRequestHeader(), //{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},
                success: function (resp) {
                    callback(resp);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log(errorThrown);
                }
            });
        }
    }

    return {'rightPanelEditModel': rightPanelEditModel};
});