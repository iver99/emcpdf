
define(['knockout', 
        'jquery',
        'ojs/ojcore',
        'dfutil'
    ], 
    function(ko, $, oj, dfu) {
        function EditDashboardDialogModel($b, tbModel) {
            var dsb = $b.dashboard;
            var self = this;
            self.dashboard = dsb;
            self.tbModel = tbModel;
            self.name = ko.observable(dsb.name());
            var savedName = dsb.name();
            self.nameInputed = ko.observable(undefined); //read only input text
            self.nameFocused = ko.observable(false);
            self.descriptionFocused = ko.observable(false);
            self.description = ko.observable(dsb.description ? dsb.description() : undefined);
            self.descriptionInputed = ko.observable(undefined);
            self.descriptionValue = ko.observable(dsb.enableDescription ? (dsb.enableDescription()==="TRUE"?["ON"]:["OFF"]) : ["OFF"]);
            var isEditDsbOptionEnabled = function(optionVal) {
                if(optionVal() === "ON" ||
                        optionVal()[0] === "ON") {
                    return true;
                }
                return false;
            };
            self.showdbDescription = ko.observable(isEditDsbOptionEnabled(self.descriptionValue) ?["showdbDescription"]:[]);
//            self.entityFilterValue = ko.observable(dsb.enableEntityFilter ? (dsb.enableEntityFilter()==="TRUE"?["ON"]:["OFF"]) : ["OFF"]);//ko.observable(["OFF"]);
            self.timeRangeFilterValue = ko.observable(dsb.enableTimeRange ? (dsb.enableTimeRange()==="TRUE"?["ON"]:["OFF"]) : ["ON"]); //ko.observable(["ON"]);//for now ON always and hide option in UI
//            self.targetFilterValue = ko.observable(["OFF"]);
            self.nameValidated = ko.observable(true);
            self.isDisabled = ko.computed(function() { 
                if (self.nameInputed() && self.nameInputed().length > 0)
                {
                    return false;
                }
                return true;
            });
            
//            self.onNameOrDescriptionEditing = ko.computed(function(){
//                return self.nameFocused()||self.descriptionFocused();
//            });
//            
//            self.onNameOrDescriptionEditing.subscribe(function(val){
//                if(val){
//                    self.tbModel.onNameOrDescriptionEditing = true;
//                }else{
////                    self.tbModel.onNameOrDescriptionEditing = false;
//                }
//            });
//            
            self.nameInputed.subscribe(function(val){
                self.dashboard.name(val);
                self.tbModel.dashboardName(val);
            } );
            
            self.descriptionInputed.subscribe(function(val){
                if (self.dashboard.description){
                    self.dashboard.description(val);
                }
                else{
                    self.dashboard.description = ko.observable(val);
                }
                self.tbModel.dashboardDescription(val);
            } );
            
            self.tbModel.dashboardDescription.subscribe(function(val){
                self.dashboard.description(val);
                if(!self.tbModel.dashboardDescription())
                {
                    self.showdbDescription([]);
                }else{
                    self.showdbDescription(["showdbDescription"]);
                }
            });
        
            self.showdbDescription.subscribe(function(val){
                if(val.indexOf("showdbDescription")>=0){
                    self.descriptionValue("ON");
                    self.tbModel.dashboardDescriptionEnabled("TRUE");
                }else{
                    self.descriptionValue("OFF");
                    self.tbModel.dashboardDescriptionEnabled("FALSE");
                }
            });
            
            self.noSameNameValidator = {
                    'validate' : function (value) {
                        self.nameValidated(true);
                        if (savedName === value)
                            return true;
                        value = value + "";

                        if (value && Builder.isDashboardNameExisting(value)) {
                            //$('#builder-dbd-name-input').focus();
                            self.nameValidated(false);
                            throw new oj.ValidatorError(oj.Translations.getTranslatedString("COMMON_DASHBAORD_SAME_NAME_ERROR"), 
                                             oj.Translations.getTranslatedString("COMMON_DASHBAORD_SAME_NAME_ERROR_DETAIL"));
                        }
                        return true;
                    }
            };

            self.save = function() {
                if (self.nameValidated() === false) 
                    return;
                
                var url = "/sso.static/dashboards.service/";
                if (dfu.isDevMode()) {
                        url = dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint, "dashboards/");
                }
//                self.tbModel.onNameOrDescriptionEditing = false;
                dfu.ajaxWithRetry(url + self.dashboard.id() + "/quickUpdate", {
                        type: 'PUT',
                        dataType: "json",
                        contentType: 'application/json',
                        data: JSON.stringify({name: self.name(), description: self.description(), enableDescription: isEditDsbOptionEnabled(self.descriptionValue) ? "TRUE" : "FALSE", /*enableEntityFilter: (isEditDsbOptionEnabled(self.entityFilterValue) ? "TRUE" : "FALSE"),*/ enableTimeRange: (isEditDsbOptionEnabled(self.timeRangeFilterValue) ? "TRUE" : "FALSE")}),
                        headers: dfu.getDashboardsRequestHeader(), //{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},
                        success: function (result) {
                            self.dashboard.name(self.name());
                            self.tbModel.dashboardName(self.name());
                            if (self.dashboard.description)
                            {
                                self.dashboard.description(self.description());
                            }
                            else
                            {
                                self.dashboard.description = ko.observable(self.description());
                            }
                            self.tbModel.dashboardDescription(self.description());
                            self.dashboard.enableDescription(isEditDsbOptionEnabled(self.descriptionValue) ? "TRUE" : "FALSE");
                            self.tbModel.dashboardDescriptionEnabled(isEditDsbOptionEnabled(self.descriptionValue) ? "TRUE" : "FALSE");
//                            self.dashboard.enableEntityFilter(self.isEditDsbOptionEnabled(self.entityFilterValue) ? "TRUE" : "FALSE");
                            self.dashboard.enableTimeRange(isEditDsbOptionEnabled(self.timeRangeFilterValue) ? "TRUE" : "FALSE");
                            savedName = self.name();
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            dfu.showMessage({type: 'error', summary: getNlsString('DBS_BUILDER_MSG_ERROR_IN_SAVING'), detail: '', removeDelayTime: 5000});
                        }
                    });
            };

            self.clear = function() {
                self.name(self.dashboard.name());
                self.description(self.dashboard.description ? self.dashboard.description() : undefined);
            };


        }
        
        return {"EditDashboardDialogModel": EditDashboardDialogModel};
    }
);
