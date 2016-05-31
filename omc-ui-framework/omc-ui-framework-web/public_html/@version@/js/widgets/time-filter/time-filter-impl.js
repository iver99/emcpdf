define([
    'knockout',
    'jquery',
    'ojs/ojcore',
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg',
    'ojs/ojcheckboxset'
], function (ko, $, oj, nls) {
    function AdvancedViewModel(params) {
        var self = this;
        var postbox = params ? params.postbox : null;
        self.randomId = params ? params.randomId : "";
        self.timeFilterTitle = nls.TIME_FILTER_TITLE;
        self.timeFilterLabel = nls.TIME_FILTER_TIME_LABEL;
        self.hintForTimeFilter = nls.TIME_FILTER_TIME_HINT;
        self.daysFilterLabel = nls.TIME_FILTER_DAYS_LABEL;
        self.monthsFilterLabel = nls.TIME_FILTER_MONTHS_LABEL;
        self.timeFilterOptionAll = nls.TIME_FILTER_OPTION_ALL;

        self.daysFilterOptions = oj.LocaleData.getDayNames("wide");
        self.monthsFilterOptions = oj.LocaleData.getMonthNames("wide");

        self.daysArray = ["1", "2", "3", "4", "5", "6", "7"];
        self.monthsArray = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"]; //[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];

        self.timeFilterValue = ko.observable('0-23');
        self.daysChecked = ko.observableArray(self.daysArray);
        self.monthsChecked = ko.observableArray(self.monthsArray);
        
        self.showHoursFilterErrorMsg = ko.observable(true);
        self.hoursFilterErrorMsg = nls.TIME_FILTER_HOURS_FILTER_MSG;
        
        self.tfChangedCallback = params ? params.tfChangedCallback : null;

        self.daysOptionAllChecked = ko.computed({
            read: function() {
                if(self.daysChecked && self.daysChecked().length === self.daysArray.length) {
                    return ["all"];
                }else {
                    return [];
                }
            },
            write: function(value) {
                if(value && value.length === 1 && value[0] === "all") {
                    self.daysChecked(self.daysArray);
                }else {
                    self.daysChecked([]);
                }
            },
            owner: self
        });
        
        self.monthsOptionAllChecked = ko.computed({
            read: function() {
                if(self.monthsChecked && self.monthsChecked().length === self.monthsArray.length) {
                    return ["all"];
                }else {
                    return [];
                }
            },
            write: function(value) {
                if(value && value.length === 1 && value[0] === "all") {
                    self.monthsChecked(self.monthsArray);
                }else {
                    self.monthsChecked([]);
                }
            },
            owner: self
        });
        
        self.isHoursFilterValid = function(hoursFilterValue) {
            var hoursFilterArray = hoursFilterValue.split(",");
            for(var i=0; i<hoursFilterArray.length; i++) {
                var tmp = hoursFilterArray[i];
                var tmpArray = tmp.split("-");
                if(tmpArray.length !== 2) {
                    return false;
                }
                var tmpStart = tmpArray[0].trim();
                var tmpEnd = tmpArray[1].trim();
                
                if(!(/^\d+$/.test(tmpStart))) {
                    return false;
                }else {
                    tmpStart = parseInt(tmpStart);
                }
                if(!(/^\d+$/.test(tmpEnd))) {
                    return false;
                }else {
                    tmpEnd = parseInt(tmpEnd);
                }
                
                if(tmpStart>23 || tmpStart<0 || tmpEnd>23 || tmpEnd<0 || tmpStart>tmpEnd) {
                    return false;
                }
            }
            return true;
        };
        
        self.tfChangedSubscriber = ko.computed(function() {
            if(!self.isHoursFilterValid(self.timeFilterValue())) {
                self.showHoursFilterErrorMsg(true);
                if(!$("#hoursFilter_"+self.randomId+" input").hasClass("tf-errorBorder")) {
                    $("#hoursFilter_"+self.randomId+" input").addClass("tf-errorBorder");
                }
            }else {
                self.showHoursFilterErrorMsg(false);
                if($("#hoursFilter_"+self.randomId+" input").hasClass("tf-errorBorder")) {
                    $("#hoursFilter_"+self.randomId+" input").removeClass("tf-errorBorder");
                }
            }
            postbox && postbox.notifySubscribers({"showTimeFilterError": self.showHoursFilterErrorMsg, "timeFilterValue": self.timeFilterValue, "daysChecked": self.daysChecked, "monthsChecked": self.monthsChecked}, "tfChanged");
            return {
                "timeFilterValule": self.timeFilterValue(),
                "daysChecked" : self.daysChecked(),
                "monthsChecked" : self.monthsChecked()
            }
        });
        self.tfChangedSubscriber.subscribe(function() {
            self.tfChangedCallback && self.tfChangedCallback();
        })
    }
    return AdvancedViewModel;
});