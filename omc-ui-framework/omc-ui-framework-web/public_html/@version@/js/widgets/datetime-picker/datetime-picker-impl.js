define('uifwk/@version@/js/widgets/datetime-picker/datetime-picker-impl',["knockout", "jquery", "uifwk/@version@/js/util/message-util-impl", "uifwk/@version@/js/util/df-util-impl", 'uifwk/@version@/js/sdk/context-util-impl', "ojs/ojcore", "ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg", "ojs/ojdatetimepicker", "ojs/ojinputnumber"],
        function (ko, $, msgUtilModel, dfuModel, contextModel, oj, nls) {

            //Firefox ignores milliseconds when converting a date to Date object, while it doesn't when converting a number.
            //The funciton is used to keep the milliseconds no matter it is a Date Object or number so that when calculating time periods, it is precise.
            function newDateWithMilliseconds(date) {
                if(date instanceof Date) {
                    var year = date.getFullYear();
                    var month = date.getMonth();
                    var day = date.getDate();
                    var hours = date.getHours();
                    var minutes = date.getMinutes();
                    var seconds = date.getSeconds();
                    var milliseconds = date.getMilliseconds();
                    return new Date(year, month, day, hours, minutes, seconds, milliseconds);
                }else if(!isNaN(parseInt(date))) {
                    return new Date(parseInt(date));
                }else {
                    return new Date();
                }
            }

            /**
             *
             * @param {String} string the string to search
             * @param {Array or Object} array the array in which to search the string
             * @returns {String} if the string exist in array, return it. if not return ""
             */
            function in_array(string, array) {
                for (var i in array) {
                    if (array[i][1] === string) {
                        return i;
                    }
                }
                return "";
            }

            function isArray(obj) {
                return Object.prototype.toString.call(obj) === "[object Array]";
            }
            
            /**
             * 
             * @param {type} timePeriod
             * @returns {unresolved} Turn timePeriod to "Last X unit/units"
             */
            function informalizeTimePeriod(timePeriod) {
                if(!timePeriod) {
                    return null;
                }
                
                var tp = timePeriod.toLowerCase();
                tp = (tp.slice(0, 1)).toUpperCase() + tp.slice(1);
                var arr = tp.split("_");
                if(parseInt(arr[1]) >1) {
                    arr[2] = arr[2] + "s";
                }
                tp = arr.join(" ");
                return tp;
            }
            
            function isValidDateInput(date) {
                if(date instanceof Date) {
                    return true;
                }else if(!isNaN(parseInt(date))){
                    return true;
                }else {
                    return false;
                }
            }
            
            /**
             * Convvert time period id for "Last hour"/"Last week"
             * In 1.16 or before, quick pick shows "Last hour" and "Last week", but their ids are "LAST_60_MINUTE" nad "LAST_7_DAY". 
             * Need to change their ids so that "Last" radio keeps consistent with quick pick label
             * 
             * In 1.17 after integrators cut over to new sets of quick picks, it will show "Last 60 mins" and "Last 7 days".
             * The labels are consistent with their ids. So no need to convert time period ids.
             * 
             * @param {type} quickPickId
             * @param {type} shouldConvert Flag to decide whether the id should be converted. If time selector gets params.timePeriodsSet, then we shouldn't do convert.
             * @returns {String}
             */
            function convertTPIdForQuickPick(quickPickId, shouldConvert) {
                if(!shouldConvert) {
                    return quickPickId;
                }
                if(quickPickId === "LAST_60_MINUTE") {
                    return "LAST_1_HOUR";
                }
                if(quickPickId === "LAST_7_DAY") {
                    return "LAST_1_WEEK";
                }
                return quickPickId;
            }

            /**
             *
             * @param {Object} params parameters passed in for this knockout component.
             * @returns {undefined}
             */
            function dateTimePickerViewModel(params) {
                //Append uifwk css file into document head
                var dfu = new dfuModel();
                dfu.loadUifwkCss();
                
                var self = this;
                self.nls = nls;
                var msgUtil = new msgUtilModel();
                var ctxUtil = new contextModel();
                var omcContext = ctxUtil.getOMCContext();
                var eventSourceTimeSelector = ctxUtil.OMCEventSourceConstants.GLOBAL_TIME_SELECTOR;
                self.badgeTimePeriod = ko.observable();
                self.badgeMsgTitle = ko.observable();
                console.log("Initialize date time picker! The params are: ");
                if(ko.mapping && ko.mapping.toJS) {
                    console.log(ko.mapping.toJS(params));
                }else {
                    console.log(params);
                }

                var start, end;
                var timeDiff, dateTimeDiff;
                
                var daysArray = ["1", "2", "3", "4", "5", "6", "7"];
                var monthsArray = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"];

                self.tfInstance = {"showTimeFilterError": ko.observable(false), "timeFilterValue": ko.observable("0-23"), "daysChecked": ko.observableArray(daysArray), "monthsChecked": ko.observableArray(monthsArray)};
                self.showTimeFilterError = ko.observable(self.tfInstance.showTimeFilterError());

                if(params.appId) {
                    self.randomId = params.appId;
                }else {
                    self.randomId = new Date().getTime();
                }

                self.changeLabel = ko.observable(true);
                if(params.changeLabel) {
                    self.changeLabel = params.changeLabel;
                }

                self.postbox = new ko.subscribable();
                self.postbox.subscribe(function(newValue) {
                    self.tfInstance = newValue;
                    self.showTimeFilterError(self.tfInstance.showTimeFilterError());
                }, null, "tfChanged");
                                
                /**
                *    Make input of timeperiod backward compatible for old time period format.
                *    The result is "LAST_X_UNIT"
                */
                function formalizeTimePeriod(timePeriod) {
                    return ctxUtil.formalizeTimePeriod(timePeriod);
                }
                
                /**
                 * Check if a time period is a valid time period with format "LAST_X_UNIT"
                 * @param {type} timePeriod
                 * @returns {unresolved} true/false
                 */
                function isValidFlexRelTimePeriod(timePeriod) {
                    if(params.hideLastRange === true) {
                        return false;
                    }
                    return ctxUtil.isValidTimePeriod(timePeriod);
                }

                self.showPanel = ko.observable(false);
                self.showTimeFilterInfoPopUp = ko.observable(false);
                self.enableTimeFilter = ko.observable(false);
                self.tfInfoIndicatorVisible = ko.observable(false);
                self.timeFilterInfo = ko.observable();
                self.calendarDateFormat="mm/dd/yyyy";
                self.wrapperId = "#dateTimePicker_" + self.randomId;
                self.panelId = "#panel_" + self.randomId;
                self.pickerPanelId = "#pickerPanel_" + self.randomId;
                self.timeFilterId = "#timeFilter_" + self.randomId;

                var dateTimeOption = {formatType: "datetime", dateFormat: "medium"};
                var dateOption = {formatType: "date", dateFormat: "medium"};
                var dateOption2 = {pattern: "MM/dd/yyyy"};
                var timeOption = {formatType: "time", timeFormat: "short"};
                self.dateTimeConverter = oj.Validation.converterFactory("dateTime").createConverter(dateTimeOption);
                self.dateConverter = oj.Validation.converterFactory("dateTime").createConverter(dateOption);
                self.dateConverter2 = oj.Validation.converterFactory("dateTime").createConverter(dateOption2);
//                self.timeConverter = oj.Validation.converterFactory("dateTime").createConverter(timeOption);
                self.timeConverterMinute = oj.Validation.converterFactory("dateTime").createConverter({pattern: 'hh:mm a'});
                self.timeConverterSecond = oj.Validation.converterFactory("dateTime").createConverter({pattern: 'hh:mm:ss a'});
                self.timeConverterMillisecond = oj.Validation.converterFactory("dateTime").createConverter({pattern: 'hh:mm:ss:SSS a'});
                self.timeConverter = ko.observable(self.timeConverterMinute);
                self.showTimeAtMillisecond = ko.observable(false);
                self.showLatestOnCustomPanel = ko.observable(false);

                self.timeIncrement = ko.observable("00:10:00:00");

                self.longMonths = oj.LocaleData.getMonthNames("wide");
                self.longDaysOfWeek = oj.LocaleData.getDayNames("wide");
                self.dropDownAlt = nls.DATETIME_PICKER_DROP_DOWN;
                self.tfIndicatorLabel = nls.DATATIME_PICKER_TF_INDICATOR_LABEL;
                self.timeFilterAlt = nls.DATETIME_PICKER_TIME_FILTER;
                self.timeFilterIconTitle = nls.DATETIME_FILTER_TIME_FILTER_ICON_TITLE;
                self.startDateLabel = nls.DATETIME_PICKER_START_DATE_LABEL;
                self.startTimeLabel = nls.DATETIME_PICKER_START_TIME_LABEL;
                self.endDateLabel = nls.DATETIME_PICKER_END_DATE_LABEL;
                self.endTimeLabel = nls.DATETIME_PICKER_END_TIME_LABEL;
                self.timePeriodLast15mins = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_15_MINS;
                self.timePeriodLast30mins = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_30_MINS;
                self.timePeriodLast2hours = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_2_HOURS;
                self.timePeriodLast4hours = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_4_HOURS;
                self.timePeriodLast6hours = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_6_HOURS;
                self.timePeriodLast8hours = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_8_HOURS;
                self.timePeriodLast24hours = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_24_HOURS;
                self.timePeriodLast14days = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_14_DAYS;
                self.timePeriodLast30days = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_30_DAYS;
                self.timePeriodLast90days = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_90_DAYS;
                self.timePeriodLast12months = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_12_MONTHS;
                self.timePeriodToday = nls.DATETIME_PICKER_SHOW_TODAY;
                self.timePeriodCustom = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_CUSTOM;
                self.timePeriodLatest = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LATEST;
                self.timePeriodRecent = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_RECENT;
                self.today = nls.DATETIME_PICKER_SHOW_TODAY;
                self.yesterday = nls.DATETIME_PICKER_SHOW_YESTERDAY;
                self.tfHoursExcludedMsg = nls.DATETIME_PICKER_TIME_FILTER_INFO_HOURS_EXCLUDED;
                self.tfDaysExcludedMsg = nls.DATETIME_PICKER_TIME_FILTER_INFO_DAYS_EXCLUDED;
                self.tfMonthsExcludedMsg = nls.DATETIME_PICKER_TIME_FILTER_INFO_MONTHS_EXCLUDED;
                if(params.timePeriodsSet) { //show new labels for "Last unit"
                    self.timePeriodLast1day = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_1_DAY;
                    self.timePeriodLast1year = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_1_YEAR;
                    self.timePeriodLast60mins = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_60_MINS;
                    self.timePeriodLast7days = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_7_DAYS;
                    self.showLatestOnCustomPanel(true);
                }else { //show old labels for "Last unit"
                    self.timePeriodLast1day = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_DAY;
                    self.timePeriodLast1year = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_YEAR;
                    self.timePeriodLast60mins = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_1_HOUR;
                    self.timePeriodLast7days = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_1_WEEK;
                }
                
                if (ko.isObservable(params.enableLatestOnCustomPanel)) {
                    self.enableLatestOnCustomPanel = params.enableLatestOnCustomPanel;
                } else {
                    self.enableLatestOnCustomPanel = ko.observable(ko.unwrap(params.enableLatestOnCustomPanel) === true ? true : false);
                }

                self.timePeriodSetShortTerm = "SHORT_TERM";
                self.timePeriodSetLongTerm = "LONG_TERM";

                self.lrCtrlVal = ko.observable("timeLevelCtrl");
                self.flexRelTimeVal = ko.observable(1);
                self.flexRelTimeOpt = ko.observable(["DAY"]);
//                self.timeLevelOpt = ko.observable(["MINUTE"]);

                self.recentList = ko.observableArray([]);

                self.timePeriodsNlsObject = {};
                self.timePeriodsNlsObject[ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_15_MINUTE] = self.timePeriodLast15mins;
                self.timePeriodsNlsObject[ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_30_MINUTE] = self.timePeriodLast30mins;
                self.timePeriodsNlsObject[ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_60_MINUTE] = self.timePeriodLast60mins;
                self.timePeriodsNlsObject[ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_2_HOUR] = self.timePeriodLast2hours;
                self.timePeriodsNlsObject[ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_4_HOUR] = self.timePeriodLast4hours;
                self.timePeriodsNlsObject[ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_6_HOUR] = self.timePeriodLast6hours;
                self.timePeriodsNlsObject[ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_8_HOUR] = self.timePeriodLast8hours;
                self.timePeriodsNlsObject[ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_24_HOUR] = self.timePeriodLast24hours;
                self.timePeriodsNlsObject[ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_1_DAY] = self.timePeriodLast1day;
                self.timePeriodsNlsObject[ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_7_DAY] = self.timePeriodLast7days;
                self.timePeriodsNlsObject[ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_14_DAY] = self.timePeriodLast14days;
                self.timePeriodsNlsObject[ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_30_DAY] = self.timePeriodLast30days;
                self.timePeriodsNlsObject[ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_90_DAY] = self.timePeriodLast90days;
                self.timePeriodsNlsObject[ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_12_MONTH] = self.timePeriodLast12months;
                self.timePeriodsNlsObject[ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_1_YEAR] = self.timePeriodLast1year;
                self.timePeriodsNlsObject[ctxUtil.OMCTimeConstants.QUICK_PICK.LATEST] = self.timePeriodLatest;
                self.timePeriodsNlsObject[ctxUtil.OMCTimeConstants.QUICK_PICK.CUSTOM] = self.timePeriodCustom;

                self.last15minsNotToShow = ko.observable(false);
                self.last30minsNotToShow = ko.observable(false);
                self.last60minsNotToShow = ko.observable(false);
                self.last2hoursNotToShow = ko.observable(false);
                self.last4hoursNotToShow = ko.observable(false);
                self.last6hoursNotToShow = ko.observable(false);
                self.last8hoursNotToShow = ko.observable(true);
                self.last24hoursNotToShow = ko.observable(true);
                self.last1dayNotToShow = ko.observable(false);
                self.last7daysNotToShow = ko.observable(false);
                self.last14daysNotToShow = ko.observable(false);
                self.last30daysNotToShow = ko.observable(false);
                self.last90daysNotToShow = ko.observable(false);
                self.last12monthsNotToShow = ko.observable(true);
                self.last1yearNotToShow = ko.observable(false);
                self.todayNotToShow = ko.observable(false);
                self.latestNotToShow = ko.observable(false);

                self.last15minsChosen = ko.observable(false);
                self.last30minsChosen = ko.observable(false);
                self.last60minsChosen = ko.observable(false);
                self.last2hoursChosen = ko.observable(false);
                self.last4hoursChosen = ko.observable(false);
                self.last6hoursChosen = ko.observable(false);
                self.last8hoursChosen = ko.observable(false);
                self.last24hoursChosen = ko.observable(false);
                self.last1dayChosen = ko.observable(false);
                self.last7daysChosen = ko.observable(false);
                self.last14daysChosen = ko.observable(false);
                self.last30daysChosen = ko.observable(false);
                self.last90daysChosen = ko.observable(false);
                self.last12monthsChosen = ko.observable(false);
                self.last1yearChosen = ko.observable(false);
                self.todayChosen = ko.observable(false);
                self.latestChosen = ko.observable(false);
                self.recentChosen = ko.observable(false);
                self.customChosen = ko.observable(false);
                
                self.last15minsDisabled = ko.observable(false);
                self.last30minsDisabled = ko.observable(false);
                self.last60minsDisabled = ko.observable(false);
                self.last8hoursDisabled = ko.observable(false);
                
                self.last24hoursDisabled = ko.observable(false);
                self.last7daysDisabled = ko.observable(false);
                self.last14daysDisabled = ko.observable(false);
                
                self.last30daysDisabled = ko.observable(false);
                self.last90daysDisabled = ko.observable(false);
                self.last12monthsDisabled = ko.observable(false);

                //initialize class assuming that dtpicker is on the left of page
                self.drawerChosen = ko.observable("leftDrawerChosen");
                self.timeFilterIconCss = ko.observable("float-right");
                self.pickerPanelCss = ko.observable("picker-panel-padding-right");

                self.hideTimeSelection = ko.observable(false);

                self.last15minsCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last15minsNotToShow() ? " drawerNotToShow": "";
                    css += self.last15minsChosen() ? (" "+self.drawerChosen()) : "";
                    css += self.last15minsDisabled() ? " drawerDisabled" : "";
                    return css;
                }, self);
                self.last30minsCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last30minsNotToShow() ? " drawerNotToShow": "";
                    css += self.last30minsChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    css += self.last30minsDisabled() ? " drawerDisabled" : "";
                    return css;
                }, self);
                self.last60minsCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last60minsNotToShow() ? " drawerNotToShow": "";
                    css += self.last60minsChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    css += self.last60minsDisabled() ? " drawerDisabled" : "";
                    return css;
                }, self);
                self.last2hoursCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last2hoursNotToShow() ? " drawerNotToShow": "";
                    css += self.last2hoursChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.last4hoursCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last4hoursNotToShow() ? " drawerNotToShow": "";
                    css += self.last4hoursChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.last6hoursCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last6hoursNotToShow() ? " drawerNotToShow": "";
                    css += self.last6hoursChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.last8hoursCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last8hoursNotToShow() ? " drawerNotToShow": "";
                    css += self.last8hoursChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    css += self.last8hoursDisabled() ? " drawerDisabled" : "";
                    return css;
                }, self);
                self.last24hoursCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last24hoursNotToShow() ? " drawerNotToShow": "";
                    css += self.last24hoursChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    css += self.last24hoursDisabled() ? " drawerDisabled" : "";
                    return css;
                }, self);
                self.last1dayCss = ko.computed(function() {
                    var css = "drawer";
                    css  += self.last1dayNotToShow() ? " drawerNotToShow": "";
                    css += self.last1dayChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.last7daysCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last7daysNotToShow() ? " drawerNotToShow": "";
                    css += self.last7daysChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    css += self.last7daysDisabled() ? " drawerDisabled" : "";
                    return css;
                }, self);
                self.last14daysCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last14daysNotToShow() ? " drawerNotToShow": "";
                    css += self.last14daysChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    css += self.last14daysDisabled() ? " drawerDisabled" : "";
                    return css;
                }, self);
                self.last30daysCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last30daysNotToShow() ? " drawerNotToShow": "";
                    css += self.last30daysChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    css += self.last30daysDisabled() ? " drawerDisabled" : "";
                    return css;
                }, self);
                self.last90daysCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last90daysNotToShow() ? " drawerNotToShow": "";
                    css += self.last90daysChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    css += self.last90daysDisabled() ? " drawerDisabled" : "";
                    return css;
                }, self);
                self.last12monthsCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last12monthsNotToShow() ? " drawerNotToShow": "";
                    css += self.last12monthsChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    css += self.last12monthsDisabled() ? " drawerDisabled" : "";
                    return css;
                }, self);
                self.last1yearCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last1yearNotToShow() ? " drawerNotToShow": "";
                    css += self.last1yearChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.todayCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.todayNotToShow() ? " drawerNotToShow": "";
                    css += self.todayChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.latestCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.latestNotToShow() ? " drawerNotToShow" : "";
                    css += self.latestChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.recentCss = ko.computed(function() {
                    var css = "drawer";
                    if(self.recentList().length>0) {
                        css += self.recentChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    }else {
                        css += " drawerNotToShow";
                    }
                    return css;
                }, self);
                self.customCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.customChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);

                self.startDateFocus = ko.observable(false);
                self.endDateFocus = ko.observable(false);
                self.startTimeFocus = ko.observable(false);
                self.endTimeFocus = ko.observable(false);

                self.startDateError = ko.observable(false);
                self.endDateError = ko.observable(false);
                self.startTimeError = ko.observable(false);
                self.endTimeError = ko.observable(false);
                self.timeValidateError = ko.observable(false);
                self.timeValidateFutureError = ko.observable(false);
                self.beyondWindowLimitError = ko.observable(false);
                self.flexRelTimeValError = ko.observable(false);

                self.showErrorMsg = ko.computed(function () {
                    return self.startDateError() || self.endDateError() || self.startTimeError() || self.endTimeError();
                }, self);

                self.showTimeValidateErrorMsg = ko.computed(function() {
                    return !self.showErrorMsg() && self.timeValidateError();
                }, self);
                
                self.showTimeValidateFutureErrorMsg = ko.computed(function() {
                    return !self.showErrorMsg() && !self.showTimeValidateErrorMsg() && self.timeValidateFutureError();
                }, self);

                self.showBeyondWindowLimitError = ko.computed(function() {
                    return !self.showErrorMsg() && !self.showTimeValidateErrorMsg() && !self.showTimeValidateFutureErrorMsg() && self.beyondWindowLimitError();
                }, self);

                self.applyButtonDisable = ko.computed(function() {
                    return self.startDateError() || self.endDateError() || self.startTimeError() || self.endTimeError() ||
                            self.timeValidateError() || self.timeValidateFutureError() || self.beyondWindowLimitError() || self.showTimeFilterError() || self.flexRelTimeValError();
                }, self);
                
                self.lrCtrlVal.subscribe(function(value) {
                    if(value === "flexRelTimeCtrl") {
                        var num = self.flexRelTimeVal();
                        var opt = self.flexRelTimeOpt()[0];
                        self.setFlexRelTime(num, opt);
                        self.setTimePeriodChosen(self.timePeriodCustom);
                        customClick(1);
                    }else if(value === "latestOnCustom"){
                        var event = {};
                        event.extended = self.timePeriodLatest;
                        event.target = {};
                        event.target.innerHTML = self.timePeriodLatest;
                        self.chooseTimePeriod(undefined, event);
                        self.setTimePeriodChosen("Custom");
                    }else {
                        self.autoFocus("inputStartDate_" + self.randomId);
                        self.lastFocus(1);
                        
                        self.gotoCalendarMonth(self.startDateISO().slice(0, 10));
                        self.updateRange(self.startDate(), self.endDate());
                        self.setTimePeriodChosen("Custom");
                        customClick(1);
                    }
                });

                self.startDateSubscriber = ko.computed(function () {
                    return self.startDateFocus();
                }, self);
                self.startDateSubscriber.subscribe(function (value) {
                    if (value) {
                        self.autoFocus("inputStartDate_" + self.randomId);
                        self.lastFocus(1);
                    } else {
                        if (!(self.endDateFocus() && self.startTimeFocus() && self.endTimeFocus())) {
                            self.autoFocus("inputStartDate_" + self.randomId);
                            self.lastFocus(1);
                        } else {
                            $("#inputStartDate_" + self.randomId).removeClass("input-focus");
                        }
                    }
                });

                self.endDateSubscriber = ko.computed(function () {
                    return self.endDateFocus();
                }, self);
                self.endDateSubscriber.subscribe(function (value) {
                    if (value) {
                        self.autoFocus("inputEndDate_" + self.randomId);
                        self.lastFocus(2);
                    } else {
                        if (!(self.startDateFocus() && self.startTimeFocus() && self.endTimeFocus())) {
                            self.autoFocus("inputEndDate_" + self.randomId);
                            self.lastFocus(2);
                        } else {
                            $("#inputEndDate_" + self.randomId).removeClass("input-focus");
                        }
                    }
                });

                self.startTimeSubscriber = ko.computed(function () {
                    return self.startTimeFocus();
                }, self);
                self.startTimeSubscriber.subscribe(function (value) {
                    if (value) {
                        self.autoFocus("divStartTime_" + self.randomId);
                        self.lastFocus(0);
                    } else {
                        if (!(self.startDateFocus() && self.endDateFocus() && self.endTimeFocus())) {
                            self.autoFocus("inputStartDate_" + self.randomId);
                            //when the focus is on start time, set self.lastFocus(1) to make sure when user clicks calendar, start date will be changed
                            self.lastFocus(1);
                        }
                        $("#divStartTime_" + self.randomId).removeClass("input-focus");
                    }
                });

                self.endTimeSubscriber = ko.computed(function () {
                    return self.endTimeFocus();
                }, self);
                self.endTimeSubscriber.subscribe(function (value) {
                    if (value) {
                        self.autoFocus("divEndTime_" + self.randomId);
                        self.lastFocus(0);
                    } else {
                        if (!(self.startDateFocus() && self.endDateFocus() && self.startTimeFocus())) {
                            self.autoFocus("inputEndDate_" + self.randomId);
                            //when the focus is on end time, set self.lastFocus(1) to make sure when user clicks calendar, start date will be changed
                            self.lastFocus(1);
                        }
                        $("#divEndTime_" + self.randomId).removeClass("input-focus");
                    }
                });

                self.minDate = ko.observable(null);
                self.maxDate = ko.observable(oj.IntlConverterUtils.dateToLocalIso(new Date()).slice(0, 10));
                self.timePeriodObject = ko.observable();
                self.monthObject = ko.observable();

                self.errorMsg = nls.DATETIME_PICKER_ERROR;
                self.formatErrorMsg = nls.DATETIME_PICKER_FORMAT_ERROR_MSG;
                self.timeValidateErrorMsg = nls.DATETIME_PICKER_TIME_VALIDATE_ERROR_MSG;
                self.timeValidateFutureErrorMsg = nls.DATETIME_PICKER_TIME_VALIDATE_FUTURE_ERROR_MSG;
                self.beyondWindowLimitErrorMsg = nls.DATETIME_PICKER_BEYOND_WINDOW_LIMIT_ERROR_MSG;
                self.felRelTimeValError = nls.DATETIME_PICKER_FLEX_REL_TIME_VALUE_ERROR_MSG;
                self.timeRangeMsg = nls.DATETIME_PICKER_TIME_RANGE;
                self.applyButton = nls.DATETIME_PICKER_BUTTONS_APPLY_BUTTON;
                self.cancelButton = nls.DATETIME_PICKER_BUTTONS_CANCEL_BUTTON;
                self.timeZone = ko.observable(null);
                              
                var timezone;
                var timezoneOffset = new Date().getTimezoneOffset();
                var hoursOffset = parseInt(Math.abs(timezoneOffset) / 60);
                if(hoursOffset < 10) {
                    hoursOffset = "0" + hoursOffset;
                }
                var minuteOffset = parseInt(Math.abs(timezoneOffset) % 60);
                if(minuteOffset < 10) {
                    minuteOffset = "0" + minuteOffset;
                }
                
                if(timezoneOffset > 0) {
                    timezone = msgUtil.formatMessage(nls.DATETIME_PICKER_BUTTONS_REPORTING_TIMEZONE, "-", hoursOffset, minuteOffset);
                }else {
                    timezone = msgUtil.formatMessage(nls.DATETIME_PICKER_BUTTONS_REPORTING_TIMEZONE, "+", hoursOffset, minuteOffset);
                }
                self.timeZone(timezone);
                
                //calculate dtpicker position automatically
                self.calDtpickerPosition = function() {
                    var eleOffset = $(self.wrapperId).offset(); //get element position(top & left) relative to the document
                    var bodyWidth = $("body").width();
                    var leftDrawerWidth = $(self.panelId).width() ? $(self.panelId).width() : 104;
                    var calendarPanelWidth = $(self.pickerPanelId).width() ? $(self.pickerPanelId).width() : 562;
                    var tfPanelWidth = $(self.timeFilterId).width() ? $(self.timeFilterId).width() : 562;
                    var panelWidth = leftDrawerWidth + calendarPanelWidth;
                    if(params.enableTimeFilter && params.enableTimeFilter === true) {
                        panelWidth += tfPanelWidth;
                    }
                    if((bodyWidth-eleOffset.left) < panelWidth) {
                        return "right";
                    }else {
                        return "left";
                    }
                };

                self.setDtpickerPositionToLeft = function() {
                    self.drawerChosen("leftDrawerChosen");
                    self.drawerHover = "leftDrawerHover";
                    self.timeFilterIconCss("float-right");
                    self.pickerPanelCss("picker-panel-padding-right");
                    //the position of panel popup relative to dropdown button
                    self.panelPosition = {"my": "start top+16", "at": "start  bottom", "collision": "none", "of": "#dropDown_"+self.randomId};
                    self.pickerPanelPosition = {"my": "start top", "at": "end top", "collision": "none", "of": "#drawers_"+self.randomId};
                    self.timeFilterPosition = {"my": "start center", "at": "end center", "collision": "none"};
                };

                self.setDtpickerPositionToRight = function() {
                    self.drawerChosen("rightDrawerChosen");
                    self.drawerHover = "rightDrawerHover";
                    self.timeFilterIconCss("float-left");
                    self.pickerPanelCss("picker-panel-padding-left");
                    //the position of panel popup relative to dropdown button
                    self.panelPosition = {"my": "end top+16", "at": "end  bottom", "collision": "none", "of": "#dropDown_"+self.randomId};
                    self.pickerPanelPosition = {"my": "end top", "at": "start top", "collision": "none", "of": "#drawers_"+self.randomId};
                    self.timeFilterPosition = {"my": "end center", "at": "start center", "collision": "none"};
                };

                self.setAllTimePeriodsNotChosen = function() {
                    self.last15minsChosen(false);
                    self.last30minsChosen(false);
                    self.last60minsChosen(false);
                    self.last2hoursChosen(false);
                    self.last4hoursChosen(false);
                    self.last6hoursChosen(false);
                    self.last8hoursChosen(false);
                    self.last24hoursChosen(false);
                    self.last1dayChosen(false);
                    self.last7daysChosen(false);
                    self.last14daysChosen(false);
                    self.last30daysChosen(false);
                    self.last90daysChosen(false);
                    self.last12monthsChosen(false);
                    self.last1yearChosen(false);
                    self.todayChosen(false);
                    self.latestChosen(false);
                    self.recentChosen(false);
                    self.customChosen(false);
                };


                self.setTimePeriodChosen = function(timePeriod) {
                    self.setAllTimePeriodsNotChosen();
                    switch(timePeriod) {
                        case self.timePeriodLast15mins:
                            self.last15minsChosen(true);
                            break;
                        case self.timePeriodLast30mins:
                            self.last30minsChosen(true);
                            break;
                        case self.timePeriodLast60mins:
                            self.last60minsChosen(true);
                            break;
                        case self.timePeriodLast2hours:
                            self.last2hoursChosen(true);
                            break;
                        case self.timePeriodLast4hours:
                            self.last4hoursChosen(true);
                            break;
                        case self.timePeriodLast6hours:
                            self.last6hoursChosen(true);
                            break;
                        case self.timePeriodLast8hours:
                            self.last8hoursChosen(true);
                            break;
                        case self.timePeriodLast24hours:
                            self.last24hoursChosen(true);
                            break;
                        case self.timePeriodLast1day:
                            self.last1dayChosen(true);
                            break;
                        case self.timePeriodLast7days:
                            self.last7daysChosen(true);
                            break;
                         case self.timePeriodLast14days:
                            self.last14daysChosen(true);
                            break;
                        case self.timePeriodLast30days:
                            self.last30daysChosen(true);
                            break;
                        case self.timePeriodLast90days:
                            self.last90daysChosen(true);
                            break;
                        case self.timePeriodLast12months:
                            self.last12monthsChosen(true);
                            break;
                        case self.timePeriodLast1year:
                            self.last1yearChosen(true);
                            break;
                        case self.timePeriodLatest:
                            self.latestChosen(true);
                            break;
                        case self.timePeriodToday:
                            self.todayChosen(true);
                            break;
                        case self.timePeriodCustom:
                            self.customChosen(true);
                            break;
                        default:
                            break;
                    }
                };

                self.setAllTimePeriodsToShow = function() {
                    self.last15minsNotToShow(false);
                    self.last30minsNotToShow(false);
                    self.last60minsNotToShow(false);
                    self.last2hoursNotToShow(false);
                    self.last4hoursNotToShow(false);
                    self.last6hoursNotToShow(false);
                    self.last8hoursNotToShow(false);
                    self.last24hoursNotToShow(false);
                    self.last1dayNotToShow(false);
                    self.last7daysNotToShow(false);
                    self.last14daysNotToShow(false);
                    self.last30daysNotToShow(false);
                    self.last90daysNotToShow(false);
                    self.last12monthsNotToShow(false);
                    self.last1yearNotToShow(false);
                    self.todayNotToShow(false);
                    self.latestNotToShow(false);
                    if(!params.timePeriodsSet) {
                        self.last8hoursNotToShow(true);
                        self.last24hoursNotToShow(true);
                        self.last12monthsNotToShow(true);
                    }
                };

                self.setTimePeriodNotToShow = function(timePeriod) {
                    switch(timePeriod) {
                        case self.timePeriodLast15mins:
                            self.last15minsNotToShow(true);
                            break;
                        case self.timePeriodLast30mins:
                            self.last30minsNotToShow(true);
                            break;
                        case self.timePeriodLast60mins:
                            self.last60minsNotToShow(true);
                            break;
                        case self.timePeriodLast2hours:
                            self.last2hoursNotToShow(true);
                            break;
                        case self.timePeriodLast4hours:
                            self.last4hoursNotToShow(true);
                            break;
                        case self.timePeriodLast6hours:
                            self.last6hoursNotToShow(true);
                            break;
                        case self.timePeriodLast8hours:
                            self.last8hoursNotToShow(true);
                            break;
                        case self.timePeriodLast24hours:
                            self.last24hoursNotToShow(true);
                            break;
                        case self.timePeriodLast1day:
                            self.last1dayNotToShow(true);
                            break;
                        case self.timePeriodLast7days:
                            self.last7daysNotToShow(true);
                            break;
                        case self.timePeriodLast14days:
                            self.last14daysNotToShow(true);
                            break;
                        case self.timePeriodLast30days:
                            self.last30daysNotToShow(true);
                            break;
                        case self.timePeriodLast90days:
                            self.last90daysNotToShow(true);
                            break;
                        case self.timePeriodLast12months:
                            self.last12monthsNotToShow(true);
                            break;
                        case self.timePeriodLast1year:
                            self.last1yearNotToShow(true);
                            break;
                        case self.timePeriodToday:
                            self.todayNotToShow(true);
                            break;
                        case self.timePeriodLatest:
                            self.latestNotToShow(true);
                        default:
                            break;
                    }
                };

                self.getParam = function(param) {
                    var p = ko.isObservable(param) ? param() : param;
                    return p;
                };

                if(params.timePeriodsSet){
                    if(params.timePeriodsSet === self.timePeriodSetShortTerm){
                        self.setAllTimePeriodsToShow();
                        params.timePeriodsNotToShow = [ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_2_HOUR,
                                                    ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_4_HOUR,
                                                    ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_6_HOUR,
                                                    ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_1_DAY,
                                                    ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_30_DAY,
                                                    ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_90_DAY,
                                                    ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_12_MONTH,
                                                    ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_1_YEAR,
                                                    ctxUtil.OMCTimeConstants.QUICK_PICK.LATEST];
                    }else if(params.timePeriodsSet === self.timePeriodSetLongTerm){
                        self.setAllTimePeriodsToShow();
                        params.timePeriodsNotToShow = [ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_15_MINUTE,
                                                    ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_30_MINUTE,
                                                    ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_60_MINUTE,
                                                    ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_2_HOUR,
                                                    ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_4_HOUR,
                                                    ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_6_HOUR,
                                                    ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_8_HOUR,
                                                    ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_1_DAY,
                                                    ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_1_YEAR,
                                                    ctxUtil.OMCTimeConstants.QUICK_PICK.LATEST];
                    }
                }

                if(self.getParam(params.timePeriod)) {
                    if(ko.isObservable(params.timePeriod)) {
                        self.timePeriodPresetInNls = ko.computed(function() {
                            return self.timePeriodsNlsObject[formalizeTimePeriod(params.timePeriod())];
                        }, self);
                        params.timePeriod.subscribe(function(value) {
                            if(self.timePeriodsNlsObject[formalizeTimePeriod(value)] || isValidFlexRelTimePeriod(value)) {
                                self.initialize && self.initialize();                                
                            }else {
                                throw new Error("The config param - "  + value + " for time period is invalid!");
                            }
                        });
                    }else {
                        self.timePeriodPresetInNls = self.timePeriodsNlsObject[formalizeTimePeriod(params.timePeriod)];
                    }

                    if(self.getParam(self.timePeriodPresetInNls) || isValidFlexRelTimePeriod(self.getParam(params.timePeriod))) { //check if it is valid time period
                        self.isTimePeriodPreset = true;
                    }else {
                        self.isTimePeriodPreset = false;
                    }
                }

                if(params.startDateTime) {
                    if(ko.isObservable(params.startDateTime)) {
                        self.startDateTime = ko.computed(function() {
                            return params.startDateTime();
                        }, self);
                        self.startDateTime.subscribe(function(value) {
                            self.initialize && self.initialize();
                        });
                    }else {
                        self.startDateTime = params.startDateTime;
                    }
                }

                if(params.endDateTime) {
                    if(ko.isObservable(params.endDateTime)) {
                        self.endDateTime = ko.computed(function() {
                            return params.endDateTime();
                        }, self);
                        self.endDateTime.subscribe(function(value) {
                            self.initialize && self.initialize();
                        });
                    }else {
                        self.endDateTime = params.endDateTime;
                    }
                }

                //hide time periods according to params.timePeriodsNotToShow
                if(params.timePeriodsNotToShow) {
                    if(isArray(params.timePeriodsNotToShow) && params.timePeriodsNotToShow.length>0) {
                        self.timePeriodsNotToShow = [];
                        var l = params.timePeriodsNotToShow.length;
                        for(var i=0; i<l; i++) {
                            var tp = formalizeTimePeriod(params.timePeriodsNotToShow[i]);
                            self.timePeriodsNotToShow.push(self.timePeriodsNlsObject[tp]);
                            self.setTimePeriodNotToShow(self.timePeriodsNlsObject[tp]);
                        }
                    }else if(ko.isObservable(params.timePeriodsNotToShow)) {
                        self.timePeriodsNotToShow = ko.computed(function() {
                            var tmp = [];
                            var i, j;
                            var tp;
                            self.setAllTimePeriodsToShow();
                            var l = params.timePeriodsNotToShow().length;
                            for(i=0; i<l; i++) {
                                tp = formalizeTimePeriod(params.timePeriodsNotToShow()[i]);
                                tmp.push(self.timePeriodsNlsObject[tp]);
                                self.setTimePeriodNotToShow(self.timePeriodsNlsObject[tp]);
                                //remove time periods from "Recently Used" list
                                self.recentList.remove(function(data) {return data.timePeriod === tp});
                            }
                            return tmp;
                        }, self);
                        self.timePeriodsNotToShow.subscribe(function(value) {
                            var tp = self.timePeriod();
                            if($.inArray(tp, value) >= 0) {
                                customClick(0);
                                self.dateTimeInfo(self.getDateTimeInfo(self.startDateISO().slice(0, 10), self.endDateISO().slice(0, 10), self.startTime(), self.endTime(), self.timePeriod()));
                            }
                        });
                    }
                }
                
                self.setTimePeriodDisabled = function(tpDisabledId) {
                    var quickPick = ctxUtil.OMCTimeConstants.QUICK_PICK;
                    switch(tpDisabledId) {
                        case quickPick.LAST_15_MINUTE:
                            self.last15minsDisabled(true);
                            break;
                        case quickPick.LAST_30_MINUTE:
                            self.last30minsDisabled(true);
                            break;
                        case quickPick.LAST_60_MINUTE:
                            self.last60minsDisabled(true);
                            break;
                        case quickPick.LAST_8_HOUR:
                            self.last8hoursDisabled(true);
                            break;
                        case quickPick.LAST_24_HOUR:
                            self.last24hoursDisabled(true);
                            break;
                        case quickPick.LAST_7_DAY:
                            self.last7daysDisabled(true);
                            break;
                        case quickPick.LAST_14_DAY:
                            self.last14daysDisabled(true);
                            break;
                        case quickPick.LAST_30_DAY:
                            self.last30daysDisabled(true);
                            break;
                        case quickPick.LAST_90_DAY:
                            self.last90daysDisabled(true);
                            break;
                        case quickPick.LAST_12_MONTH:
                            self.last12monthsDisabled(true);
                            break;
                        default:
                            break;
                    }
                };
                
                self.setAllTimePeriodsEnabled = function() {
                    self.last15minsDisabled(false);
                    self.last30minsDisabled(false);
                    self.last60minsDisabled(false);
                    self.last8hoursDisabled(false);

                    self.last24hoursDisabled(false);
                    self.last7daysDisabled(false);
                    self.last14daysDisabled(false);

                    self.last30daysDisabled(false);
                    self.last90daysDisabled(false);
                    self.last12monthsDisabled(false);
                };
                
                if(params.timePeriodsSet && params.timePeriodsToBeDisabled) {
                    if(isArray(params.timePeriodsToBeDisabled) && params.timePeriodsToBeDisabled.length>0) {
                        for(var i=0; i<params.timePeriodsToBeDisabled.length; i++) {
                            var tpDisabledId = params.timePeriodsToBeDisabled[i];
                            self.setTimePeriodDisabled(tpDisabledId);
                        }
                    }else if(ko.isObservable(params.timePeriodsToBeDisabled)) {
                        self.timePeriodsToBeDisabled = ko.computed(function() {
                            self.setAllTimePeriodsEnabled();
                            for(var i=0; i<params.timePeriodsToBeDisabled().length; i++) {
                                var tpDisabledId = params.timePeriodsToBeDisabled()[i];
                                self.setTimePeriodDisabled(tpDisabledId);
                                //remove time periods from "Recently Used" list
                                self.recentList.remove(function(data) {return data.timePeriod === tpDisabledId});
                            }
                        }, self);
                    }
                }
                
                self.shouldShowTimeLevel = function(timeLevel) {
                    if(!params.timeLevelsNotToShow) {
                        return true;
                    }
                    //timeLevelsNotToShow param support both lowercase and uppercase
                    if(self.getParam(params.timeLevelsNotToShow).indexOf(timeLevel) >= 0 || self.getParam(params.timeLevelsNotToShow).indexOf(timeLevel.toUpperCase()) >= 0) {
                        return false;
                    }else {
                        return true;
                    }
                };                
                
                //show time at millisecond level or minute level in ojInputTime component
                self.getTimeConverter = function() {
                    if(self.getParam(params.showTimeAtMillisecond) === true) {
                        self.showTimeAtMillisecond(true);
                        self.timeConverter(self.timeConverterMillisecond);
                    }else {
                        self.timeConverter(self.timeConverterMinute);
                    }
                    return self.timeConverter();
                }
                
                //Call this first as popup binding is deferred. getDateTimeInfo function need this to know how to show time label(minite ot millisecond)
                self.getTimeConverter();

                self.convertWindowSizeToDays = function(nls) {
                    var windowSize;
                    var totalMins = self.customWindowLimit / (60*1000);
                    var days = Math.floor(totalMins / (24*60));
                    var hours = Math.floor((totalMins - days*24*60) / 60);
                    var mins = totalMins - days*24*60 - hours*60;
                    if(days > 0) {
                        windowSize = msgUtil.formatMessage(nls.DATETIME_PICKER_WINDOW_SIZE_WITH_DAYS, days, hours, mins);
                    }else if(hours > 0) {
                        windowSize = msgUtil.formatMessage(nls.DATETIME_PICKER_WINDOW_SIZE_WITH_HOURS, hours, mins);
                    }else{
                        windowSize = msgUtil.formatMessage(nls.DATETIME_PICKER_WINDOW_SIZE_WITH_MINUTES, mins);
                    }
                    return windowSize;
                };

                //Meet the requirement of displaying relative time vs. absolute time
                //Set flag "timeDisplay" to determine whether relative time or absolute time should be displayed in the drop-down.
                //If "timeDisplay" is set as "long", both relative time("Last X") and absolute time will be displayed in drop-down.
                //If "timeDisplay" is set as "short", ONLY relative time will be displayed when the user selects a relative time option and ONLY absolute time will be displayed when the user choose "Custom".
                if(params.timeDisplay) {
                    if(ko.isObservable(params.timeDisplay)) {
                        self.timeDisplay = ko.computed(function() {
                            return params.timeDisplay();
                        }, self);
                        self.timeDisplay.subscribe(function(value) {
                            self.dateTimeInfo(self.getDateTimeInfo(self.startDateISO().slice(0, 10), self.endDateISO().slice(0, 10), self.startTime(), self.endTime(), self.timePeriod()));
                        });
                    }else {
                        self.timeDisplay = params.timeDisplay;
                    }
                }else {
                    self.timeDisplay = "long";
                }

                //In custom mode, limit the size of window, expressed as milliseconds
                if(params.customWindowLimit && params.customWindowLimit>60*1000) {
                    self.customWindowLimit = params.customWindowLimit;
                    if(self.customWindowLimit) {
                        self.beyondWindowLimitErrorMsg = self.beyondWindowLimitErrorMsg + self.convertWindowSizeToDays(nls);
                    }
                }

                //When user choose Last xx, adjust time using function 'adjustLastX'
                if(params.adjustLastX && typeof params.adjustLastX === "function") {
                    self.adjustLastX = params.adjustLastX;
                }

                /**
                 * restrict date range accroding to current date, customTimeBack, startDateISO and endDateISO
                 * @param {type} minDate
                 * @param {type} maxDate
                 * @returns {undefined}
                 */
                self.setMinMaxDate = function(minDate, maxDate) {
                    var today = new Date(new Date().toDateString());
                    if(!minDate) {
                        if(self.customTimeBack) {
                            minDate = oj.IntlConverterUtils.dateToLocalIso(new Date(today - self.customTimeBack)).slice(0, 10);
                        }else {
                            minDate = null;
                        }
                    }

                    if(!maxDate) {
                        maxDate = oj.IntlConverterUtils.dateToLocalIso(today).slice(0, 10);
                    }

                    self.minDate(minDate);
                    self.maxDate(maxDate);
                };

                //the max timestamp of how far the user can pick the date from, expressed as milliseconds
                if(params.customTimeBack && params.customTimeBack>0) {
                    self.customTimeBack = params.customTimeBack;
                    self.setMinMaxDate(null, null);
                }

                if(params.hideMainLabel && params.hideMainLabel === true) {
                    self.hideMainLabel = "none";
                }else{
                    self.hideMainLabel = "table-cell";
                }

                if(params.hideRangeLabel && params.hideRangeLabel === true) {
                    self.hideRangeLabel = "none";
                    self.pickerTopCss = "text-align: center; padding-bottom: 5px;";
                }else {
                    self.hideRangeLabel = "inline-block";
                    self.pickerTopCss = "text-align: left; padding-bottom: 5px;";
                }

                if(params.hideTimeSelection && params.hideTimeSelection === true) {
                    if(!params.timePeriodsSet) {
                        self.last15minsNotToShow(true);
                        self.last30minsNotToShow(true);
                        self.last60minsNotToShow(true);
                        self.last2hoursNotToShow(true);
                        self.last4hoursNotToShow(true);
                        self.last6hoursNotToShow(true);
                    }
                    self.hideTimeSelection(true);
                }

                if(params.enableTimeFilter && params.enableTimeFilter === true) {
                    self.enableTimeFilter(true);
                }
                
                self.hideLastRange = ko.observable(false);
                if(params.hideLastRange && params.hideLastRange === true) {
                    self.hideLastRange(true);
                }
                
                if(params.timePeriodsSet){
                    if(params.timePeriodsSet === self.timePeriodSetShortTerm){
                        self.defaultTimePeriod = ko.observable(ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_60_MINUTE);
                    }else if(params.timePeriodsSet === self.timePeriodSetLongTerm){
                        self.defaultTimePeriod = ko.observable(ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_30_DAY);
                    }
                }else{
                    self.defaultTimePeriod = ko.observable(ctxUtil.OMCTimeConstants.QUICK_PICK.LAST_15_MINUTE);
                }
                if(params.defaultTimePeriod) {
                    var defaultTP = formalizeTimePeriod(ko.unwrap(params.defaultTimePeriod));
                    if(self.timePeriodsNlsObject[defaultTP] || isValidFlexRelTimePeriod(defaultTP)) {
                        if(ko.isObservable(params.defaultTimePeriod)) {
                            self.defaultTimePeriod = params.defaultTimePeriod;
                        }else {
                            self.defaultTimePeriod(defaultTP);
                        }
                    }
                }

                if(ko.isObservable(params.showBadge)) {
                    self.showBadge = params.showBadge;
                    var defaultTP = formalizeTimePeriod(ko.unwrap(self.defaultTimePeriod));
                    if (self.timePeriodsNlsObject[defaultTP] || isValidFlexRelTimePeriod(defaultTP)) {
                        self.badgeTimePeriod = self.defaultTimePeriod;
                    }
                }else {
                    self.showBadge = ko.observable(params.showBadge);
                }

                if(self.getParam(params.showBadge)){
                    var defaultTP = formalizeTimePeriod(ko.unwrap(self.defaultTimePeriod));
                    if(self.timePeriodsNlsObject[defaultTP] || isValidFlexRelTimePeriod(defaultTP)) {
                        self.badgeTimePeriod = self.defaultTimePeriod;
                    }
                }
                
                self.getTranslatedTimePeriod = function(tpId) {
                    return ctxUtil.getTranslatedTimePeriod(tpId);
                };
                
                self.badgeMsgTitle(msgUtil.formatMessage(nls.DATETIME_PICKER_BADGE_MESSAGE_TITLE, self.getTranslatedTimePeriod(self.badgeTimePeriod())));

                if(!ko.components.isRegistered("time-filter")) {
                    ko.components.register("time-filter", {
                        template: {require: "text!uifwk/js/widgets/timeFilter/html/timeFilter.html"},
                        viewModel: {require: "uifwk/js/widgets/timeFilter/js/timeFilter"}//{instance: self.tfInstance}
                    });
                }

                if (params.callbackAfterApply && typeof params.callbackAfterApply === "function") {
                    self.callbackAfterApply = params.callbackAfterApply;
                }

                if(params.callbackAfterCancel && typeof params.callbackAfterCancel === "function") {
                    self.callbackAfterCancel = params.callbackAfterCancel;
                }

                self.timePeriodObject = ko.computed(function () {
                    var tmp = {};
                    tmp[self.timePeriodLast15mins] = [0, 15 * 60 * 1000];
                    tmp[self.timePeriodLast30mins] = [1, 30 * 60 * 1000];
                    tmp[self.timePeriodLast60mins] = [2, 60 * 60 * 1000];
                    tmp[self.timePeriodLast2hours] = [3, 2 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast4hours] = [4, 4 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast6hours] = [5, 6 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast8hours] = [12, 8 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast24hours] = [13, 24 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast1day] = [6, 24 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast7days] = [7, 7 * 24 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast14days] = [8, 14 * 24 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast30days] = [9, 30 * 24 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast90days] = [10, 90 * 24 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast12months] = [14, 365 * 24 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast1year] = [11, 365 * 24 * 60 * 60 * 1000]; //do not use this to calculate last a year
                    return tmp;
                }, self);

                self.monthObject = ko.computed(function () {
                    var tmp = {};
                    for (var i = 0; i < 12; i++) {
                        tmp[self.longMonths[i]] = i + 1;
                    }
                    return tmp;
                }, self);

                self.value = ko.observable(oj.IntlConverterUtils.dateToLocalIso(new Date()));
                self.lastFocus = ko.observable();
                self.startTime = ko.observable("T00:00:00");
                self.endTime = ko.observable("T00:00:00");
                self.startDate = ko.observable(new Date()).extend({rateLimit: 0});
                self.endDate = ko.observable(new Date()).extend({rateLimit: 0});
                self.timePeriod = ko.observable();
                self.dateTimeInfo = ko.observable();
                self.selectByDrawer = ko.observable(false);
                self.showRightPanel = ko.observable(false);
                self.showCalendar = ko.observable(false);

                self.random = ko.observable(new Date().getTime());
                self.random1 = ko.observable(new Date().getTime());

                self.lastStartDate = ko.observable();
                self.lastEndDate = ko.observable();
                self.lastStartTime = ko.observable();
                self.lastEndTime = ko.observable();
                self.lastTimePeriod = ko.observable();
                self.lastLrCtrlVal = ko.observable();
                self.lastFlexRelTimeVal = ko.observable();
                self.lastFlexRelTimeOpt = ko.observable(["DAY"]);
                if(self.enableTimeFilter()) {
                    self.lastTimeFilterValue = ko.observable();
                    self.lastDaysChecked = ko.observableArray();
                    self.lastMonthsChecked = ko.observableArray();
                }

                self.leftDrawerHeight = ko.computed(function() {
                    if(self.showRightPanel() === true) {
                        return "465px";
                    }else {
                        return "auto";
                    }
                }, self);
                
                self.valueSubscriber = ko.computed(function () {
                    return self.random();
//                    return self.value() + self.random();
                });
                self.iconSubscriber = ko.computed(function () {
                    return self.random1();
                });

                self.iconSubscriber.subscribe(function (value) {
                    self.updateRange(self.startDate(), self.endDate());
                });
                self.valueSubscriber.subscribe(function (value) {
                    if (self.startDate() === self.dateConverter2.format(self.value())) {
                        if (self.lastFocus() === 1) {
                            self.setMinMaxDate(self.startDateISO(), null);
                            setTimeout(function() {self.updateRange(self.startDate(), self.endDate());}, 0);
                            self.endDateFocus(true);
                        } else if (self.lastFocus() === 2) {
                            self.endDateISO(self.value());
                            self.setMinMaxDate(null, self.value());
                            self.startDateFocus(true);
                        } else {
                            self.updateRange(self.startDate(), self.endDate());
                        }
                    } else if (self.endDate() === self.dateConverter2.format(self.value())) {
                        if (self.lastFocus() === 2) {
                            self.setMinMaxDate(null, self.endDateISO());
                            setTimeout(function() {self.updateRange(self.startDate(), self.endDate());}, 0);
                            self.startDateFocus(true);
                        } else if (self.lastFocus() === 1) {
                            self.startDateISO(self.value());
                            self.setMinMaxDate(self.value(), null);
                            self.endDateFocus(true);
                        } else {
                            self.updateRange(self.startDate(), self.endDate());
                        }
                    } else {
                        var tmp = self.value();
                        if (self.lastFocus() === 1) {
                            self.startDateISO(tmp);
                            self.setMinMaxDate(tmp, null);
                            self.endDateFocus(true);
                        } else if (self.lastFocus() === 2) {
                            self.endDateISO(tmp);
                            self.setMinMaxDate(null, tmp);
                            self.startDateFocus(true);
                        } else {
                            self.updateRange(self.startDate(), self.endDate());
                            console.log("Should focus on an input");
                        }
                    }
                });
                self.valueSubscriber.extend({rateLimit: 0});
                self.iconSubscriber.extend({rateLimit: 0});

                self.startDateISO = ko.computed({
                    read: function () {
                        if(isNaN(new Date(self.startDate()))) {
                            return ko.unwrap(self.startDateISO);
                        }
                        return oj.IntlConverterUtils.dateToLocalIso(new Date(self.startDate()));
                    },
                    write: function (value) {
                        self.startDate(self.dateConverter2.format(value));
                    },
                    owner: self
                });

                self.endDateISO = ko.computed({
                    read: function () {
                        if(isNaN(new Date(self.endDate()))) {
                             return ko.unwrap(self.endDateISO);
                        }
                        return oj.IntlConverterUtils.dateToLocalIso(new Date(self.endDate()));
                    },
                    write: function (value) {
                        self.endDate(self.dateConverter2.format(value));
                    },
                    owner: self
                });

                self.setTimePeriodToLastX = function(timePeriod, start, end, shouldCallAdjustLastX) {
                    self.timePeriod(timePeriod);
                    self.selectByDrawer(true);

                    if(shouldCallAdjustLastX && self.adjustLastX && start && end) {
                        var adjustedTime = self.adjustLastX(start, end);
                        start = adjustedTime.start;
                        end = adjustedTime.end;
                    }

                    return {
                        start: start,
                        end: end
                    };
                };

                self.isCustomBeyondWindowLimit = function() {
                    var start = self.startDateISO().slice(0, 10) + self.startTime();
                    var end = self.endDateISO().slice(0, 10) + self.endTime();
                    timeDiff = new Date(end) - new Date(start);
                    if(timeDiff > self.customWindowLimit) {
                        self.beyondWindowLimitError(true);
                    }else{
                        self.beyondWindowLimitError(false);
                    }
                };

                self.adjustDateMoreFriendly = function(date) {
                    var today = oj.IntlConverterUtils.dateToLocalIso(new Date()).slice(0, 10);
                    var yesterday = oj.IntlConverterUtils.dateToLocalIso(new Date(new Date()-24*60*60*1000)).slice(0, 10);
                    if(today === date) {
                        return self.today;
                    }else if(yesterday === date) {
                        return self.yesterday;
                    }else {
                        return self.dateConverter.format(date);
                    }
                };

                self.isTimePeriodLessThan1day = function(timePeriod) {
                    if(timePeriod===self.timePeriodLast15mins || timePeriod===self.timePeriodLast30mins || timePeriod===self.timePeriodLast60mins ||
                                timePeriod===self.timePeriodLast2hours || timePeriod===self.timePeriodLast4hours || timePeriod===self.timePeriodLast6hours || timePeriod===self.timePeriodLast8hours) {
                        return true;
                    }
                    return false;
                };

                self.isFlexRelTimePeriodLessThan1day = function() {
                    var val = self.flexRelTimeVal();
                    var opt = self.flexRelTimeOpt()[0];
                    if(opt === ctxUtil.OMCTimeConstants.TIME_UNIT.SECOND) {
                        if(val < 24*60*60) {
                            return true;
                        }
                    }else if(opt === ctxUtil.OMCTimeConstants.TIME_UNIT.MINUTE) {
                        if(val < 24*60) {
                            return true;
                        }
                    }else if(opt === ctxUtil.OMCTimeConstants.TIME_UNIT.HOUR) {
                        if(val < 24) {
                            return true;
                        }
                    }
                    return false;
                }

                /**
                 *
                 * @param {type} date: Date object. The date to get timezone
                 * @returns {String|Number} return timezone. The format is "GMT+X"/"GMT-X"
                 */
                self.getGMTTimezone = function(date) {
                    var timezoneOffset = date.getTimezoneOffset()/60;
                    timezoneOffset = timezoneOffset>0 ? ("GMT-"+timezoneOffset) : ("GMT+"+Math.abs(timezoneOffset));
                    return timezoneOffset;
                };
                
                self.getTranslatedFlexTimePeriod = function(num, opt) {
                    return ctxUtil.getTranslatedFlexTimePeriod(num, opt);
                }

                /**
                 *
                 * @param {type} startDate
                 * @param {type} endDate
                 * @param {type} startTime
                 * @param {type} endTime
                 * @returns {String} return the dateTime info format
                 */
                self.getDateTimeInfo = function(startDate, endDate, startTime, endTime, timePeriod) {
                    var dateTimeInfo;
                    var hyphenDisplay = "display: inline;";
                    var start = self.adjustDateMoreFriendly(startDate);
                    var end = self.adjustDateMoreFriendly(endDate);
                    //show "Today/Yesterday" only once
                    if(start === end) {
                        end = "";
                    }

                    if(self.hideTimeSelection() === false) {
                        start = start + " " + self.timeConverter().format(startTime);
                        end = end + " " + self.timeConverter().format(endTime);

                        //add timezone for time ranges less than 1 day if the start&end time are in different timezone due to daylight saving time.
                        var tmpStart = oj.IntlConverterUtils.isoToLocalDate(startDate+startTime);
                        var tmpEnd = oj.IntlConverterUtils.isoToLocalDate(endDate+endTime);
                        if(tmpStart.getTimezoneOffset() !== tmpEnd.getTimezoneOffset() && self.lrCtrlVal() === "flexRelTimeCtrl" && (self.isTimePeriodLessThan1day(timePeriod) || self.isFlexRelTimePeriodLessThan1day())) {
                            start += " (" + self.getGMTTimezone(tmpStart) + ")";
                            end += " (" + self.getGMTTimezone(tmpEnd) + ")";
                        }
                    }else {
                        //hide hyphen when time range is "Today-Today"/"Yestarday-Yesterday"
                        hyphenDisplay = end ? hyphenDisplay : "display: none;";
                    }

                    //For "Latest" quick pick
                    if(timePeriod === self.timePeriodLatest) {
                        if(self.getParam(self.timeDisplay) === "short") {
                            dateTimeInfo = "<span style='font-weight: bold; padding-right: 5px; display: inline-block;' class='show-individual-time-span1-short'>" + timePeriod + "</span>";
                        }else{
                            dateTimeInfo = "<span style='font-weight: bold; padding-right: 5px; display: inline-block;' class='show-individual-time-span1-latest'>" + timePeriod + "</span>";
                        }
                        return dateTimeInfo;
                    }


                    //For "Custom" time period, including custom relative time and custom absolute time
                    if(timePeriod === self.timePeriodCustom) {
                        if(self.lrCtrlVal() === "flexRelTimeCtrl") { //For custom relative time
                            if(self.getParam(self.timeDisplay) === "short") {
                                dateTimeInfo = "<span style='font-weight:bold; padding-right: 5px; display: inline-block;' class='show-individual-time-span1-short'>" + self.getTranslatedFlexTimePeriod(self.flexRelTimeVal(), self.flexRelTimeOpt()[0])  + "</span>";
                            }else {
                                dateTimeInfo = "<span style='font-weight:bold; padding-right: 5px; display:" + self.hideRangeLabel + ";' " + "class='show-individual-time-span1'>" + self.getTranslatedFlexTimePeriod(self.flexRelTimeVal(), self.flexRelTimeOpt()[0]) + ": </span>";
                                dateTimeInfo += "<span class='time-range-overflow'>" + start + "<span style='font-weight:bold; " + hyphenDisplay + "' "+ "class='show-individual-time-span2'>" +" - </span>" + end + "</span>";
                            }
                        }else if(self.lrCtrlVal() === "latestOnCustom") {
                            if(self.getParam(self.timeDisplay) === "short") {
                                dateTimeInfo = "<span style='font-weight: bold; padding-right: 5px; display: inline-block;' class='show-individual-time-span1-short'>" + self.timePeriodLatest + "</span>";
                            }else{
                                dateTimeInfo = "<span style='font-weight: bold; padding-right: 5px; display: inline-block;' class='show-individual-time-span1-latest'>" + self.timePeriodLatest + "</span>";
                            }
                        }else {
                        
                            if(self.getParam(self.timeDisplay) === "short") {
                                dateTimeInfo = start + "<span style='font-weight:bold;' class='show-individual-time-span1-short'" + hyphenDisplay + "'> - </span>" + end;
                            }else {
                                dateTimeInfo = "<span style='font-weight:bold; padding-right: 5px; display:" + self.hideRangeLabel + ";' " + "class='show-individual-time-span1'>" + timePeriod + ": </span>" +
                                    "<span class='time-range-overflow'>" + 
                                    start +
                                    "<span style='font-weight:bold; " + hyphenDisplay + "' " + "class='show-individual-time-span2'>" +" - </span>" +
                                    end +
                                    "</span>";
                            }
                        }
                        return dateTimeInfo;
                    }
                    
                    //generate dateTimeInfo for "Recent" List
                    if(!timePeriod) {
                        dateTimeInfo = dateTimeInfo = start + "<span style='font-weight:bold; " + hyphenDisplay + "' " + "class='show-individual-time-span2'>" +" - </span>" + end;
                        return dateTimeInfo;
                    }

                    if(self.getParam(self.timeDisplay) === "short") {
                        dateTimeInfo = "<span style='font-weight:bold; padding-right: 5px; display: inline-block;' class='show-individual-time-span1-short'>" + timePeriod + "</span>";
                    }else {
                        dateTimeInfo = "<span style='font-weight:bold; padding-right: 5px; display:" + self.hideRangeLabel + ";' " + "class='show-individual-time-span1'>" + timePeriod + ": </span>";
                        dateTimeInfo += "<span class='time-range-overflow'>" + start + "<span style='font-weight:bold; " + hyphenDisplay + "' " + "class='show-individual-time-span2'>" +" - </span>" + end + "<span>";
                    }
                    return dateTimeInfo;
                };
                
                self.getRecentDateTimeInfo = function(data) {
                    var recent = data;
                    var start;
                    var end;
                    var tmp;
                    var info;
                    if(!recent.timePeriod) {
                        console.log("getRecentDateTimeInfo: timePeriod is invalid in " + JSON.stringify(recent));
                        return;
                    }
                    
                    if(recent.timePeriod === ctxUtil.OMCTimeConstants.QUICK_PICK.CUSTOM) { // for custom time range
                        start = oj.IntlConverterUtils.dateToLocalIso(recent.start);
                        end = oj.IntlConverterUtils.dateToLocalIso(recent.end);
                        return self.getDateTimeInfo(start.slice(0, 10), end.slice(0, 10), start.slice(10), end.slice(10));
                    }else if(self.timePeriodsNlsObject[recent.timePeriod]) { //for quick picks
                        return self.timePeriodsNlsObject[recent.timePeriod];
                    }else { // for flexible relative time range
                        var tp = ctxUtil.parseTimePeriodToUnitAndDuration(recent.timePeriod);
                        if(tp) {
                            return self.getTranslatedFlexTimePeriod(tp.duration, tp.unit);
                        }
                    }
                }

                var curDate;
                self.init = true;
                self.initialize = function() {
                    curDate = new Date();
                    start = newDateWithMilliseconds(curDate - 15 * 60 * 1000);
                    end = new Date();
                    var tpNotToShow = self.getParam(self.timePeriodsNotToShow);
                    var sdt, edt, range, tp, parsedTp;

                    if(self.isTimePeriodPreset) {
                        if(self.getParam(self.timePeriodPresetInNls)) {
                            tp = self.getParam(self.timePeriodPresetInNls);
                            if(tp === self.timePeriodToday) {
                                start = new Date(curDate.getFullYear(), curDate.getMonth(), curDate.getDate());
                                end = curDate;
                                self.setTimePeriodChosen(tp);
                                self.setTimePeriodToLastX(tp, start, end, 1);
                            }else if(tp === self.timePeriodLatest) {
                                start = curDate;
                                end = curDate;
                                if (self.showLatestOnCustomPanel()) {
                                    self.lrCtrlVal("latestOnCustom");
                                } else {
                                    self.setTimePeriodChosen(tp);
                                    self.setTimePeriodToLastX(tp, start, end, 1);
                                }
                            }else if(tp === self.timePeriodCustom) {
                                if(self.startDateTime && self.endDateTime) {
                                    sdt = self.getParam(self.startDateTime);
                                    edt = self.getParam(self.endDateTime);
                                    start = newDateWithMilliseconds(sdt);
                                    end = newDateWithMilliseconds(edt);
                                    self.lrCtrlVal("timeLevelCtrl");
                                    customClick(0);
                                }else {
                                    console.error('Error: set timePeriod to "Custom" without time range specified!');
                                    return;
                                }
                            }else {
                                self.lrCtrlVal("flexRelTimeCtrl");
                                parsedTp = ctxUtil.parseTimePeriodToUnitAndDuration(convertTPIdForQuickPick(formalizeTimePeriod(ko.unwrap(params.timePeriod)), !params.timePeriodsSet));
                                var tmp = self.getTimeRangeForFlexRelTime(ko.unwrap(params.timePeriod));
                                start = oj.IntlConverterUtils.isoToLocalDate(tmp.start);
                                end = oj.IntlConverterUtils.isoToLocalDate(tmp.end);

                                if(parsedTp) {
                                    self.flexRelTimeVal(parsedTp.duration);
                                    self.flexRelTimeOpt([parsedTp.unit]);
                                }
                                
                                if($.inArray(tp, tpNotToShow) === -1) {
                                    self.setTimePeriodChosen(tp);
                                    range = self.setTimePeriodToLastX(tp, start, end, 1);
                                }else {
                                    customClick(0);
                                }
                            }
                        }else {
                            self.lrCtrlVal("flexRelTimeCtrl");
                            
                            var parsedTp = ctxUtil.parseTimePeriodToUnitAndDuration(ko.unwrap(params.timePeriod));
                            var tmp = self.getTimeRangeForFlexRelTime(ko.unwrap(params.timePeriod));
                            start = oj.IntlConverterUtils.isoToLocalDate(tmp.start);
                            end = oj.IntlConverterUtils.isoToLocalDate(tmp.end);
                            
                            if(parsedTp) {
                                self.flexRelTimeVal(parsedTp.duration);
                                self.flexRelTimeOpt([parsedTp.unit]);
                            }

                            if(!params.timePeriodsSet && ko.unwrap(params.timePeriod) === "LAST_1_WEEK") {
                                tp = self.timePeriodLast7days;
                                if($.inArray(tp, tpNotToShow) === -1) {
                                    self.setTimePeriodChosen(tp);
                                    range = self.setTimePeriodToLastX(tp, start, end, 1);
                                }else {
                                    customClick(0);
                                }
                            }else if(!params.timePeriodsSet && ko.unwrap(params.timePeriod) === "LAST_1_HOUR") {
                                tp = self.timePeriodLast60mins;
                                if($.inArray(tp, tpNotToShow) === -1) {
                                    self.setTimePeriodChosen(tp);
                                    range = self.setTimePeriodToLastX(tp, start, end, 1);
                                }else {
                                    customClick(0);
                                }
                            }else {
                                self.timePeriod(self.timePeriodCustom);
                            }
                        }
                    }else {
                        if(isValidDateInput(self.getParam(self.startDateTime)) && isValidDateInput(self.getParam(self.endDateTime))) {
                            curDate = new Date();
                            //users input start date and end date
                            sdt = self.getParam(self.startDateTime);
                            edt = self.getParam(self.endDateTime);
                            start = newDateWithMilliseconds(sdt);
                            end = newDateWithMilliseconds(edt);
                            console.log("Param endDateTime is " + end);
                            console.log("Initializing time is " + curDate);
                            if(Math.abs(end.getTime()-curDate.getTime())>60*1000 || end.getMinutes() !== curDate.getMinutes()) {
                                var t_timePeriod = self.timePeriodCustom;
                                self.lrCtrlVal("timeLevelCtrl");
                                customClick(0);
                            }else {
                                dateTimeDiff = end - start;
                                var t_timePeriod = in_array(dateTimeDiff, self.timePeriodObject());
                                if (t_timePeriod && $.inArray(t_timePeriod, tpNotToShow)<0) {
                                    self.setTimePeriodChosen(t_timePeriod);
                                    range = self.setTimePeriodToLastX(t_timePeriod, start, end, 0);
                                    start = range.start;
                                    end = range.end;
                                } else {
                                    self.lrCtrlVal("timeLevelCtrl");
                                    customClick(0);
                                }
                            }
                        } else if (!isValidDateInput(self.getParam(self.startDateTime)) && isValidDateInput(self.getParam(self.endDateTime))) {
                            if($.inArray(self.timePeriodLast15mins, tpNotToShow)<0) {
                                self.setTimePeriodChosen(self.timePeriodLast15mins);
                                range = self.setTimePeriodToLastX(self.timePeriodLast15mins, start, end, 0);
                                start = range.start;
                                end = range.end;
                            }else {
                                customClick(0);
                            }
                            //print warning...
                            oj.Logger.warn("The user just input end time");
                        } else if (isValidDateInput(self.getParam(self.startDateTime)) && !isValidDateInput(self.getParam(self.endDateTime))) {
                            self.lrCtrlVal("timeLevelCtrl");
                            customClick(0);
                            sdt = self.getParam(self.startDateTime);
                            start = newDateWithMilliseconds(sdt);
                            end = new Date();
                        } else if(omcContext.time && omcContext.time.timePeriod && 
                                (((tp = self.timePeriodsNlsObject[formalizeTimePeriod(omcContext.time.timePeriod)]) && tp !== self.timePeriodCustom) || isValidFlexRelTimePeriod(omcContext.time.timePeriod))) {
                            if(tp) {
                                if(tp === self.timePeriodToday) {
                                    start = new Date(curDate.getFullYear(), curDate.getMonth(), curDate.getDate());
                                    end = curDate;
                                    self.setTimePeriodChosen(tp);
                                    self.setTimePeriodToLastX(tp, start, end, 1);
                                }else if(tp === self.timePeriodLatest) {
                                    start = curDate;
                                    end = curDate;
                                    if (self.showLatestOnCustomPanel()) {
                                        self.lrCtrlVal("latestOnCustom");
                                    } else {
                                        self.setTimePeriodChosen(tp);
                                        self.setTimePeriodToLastX(tp, start, end, 1);
                                    }
                                }else {
                                    self.lrCtrlVal("flexRelTimeCtrl");
                                    parsedTp = ctxUtil.parseTimePeriodToUnitAndDuration(convertTPIdForQuickPick(formalizeTimePeriod(ko.unwrap(omcContext.time.timePeriod)), !params.timePeriodsSet));
                                    var tmp = self.getTimeRangeForFlexRelTime(ko.unwrap(omcContext.time.timePeriod));
                                    start = oj.IntlConverterUtils.isoToLocalDate(tmp.start);
                                    end = oj.IntlConverterUtils.isoToLocalDate(tmp.end);

                                    if(parsedTp) {
                                        self.flexRelTimeVal(parsedTp.duration);
                                        self.flexRelTimeOpt([parsedTp.unit]);
                                    }

                                    if($.inArray(tp, tpNotToShow) === -1) {
                                        self.setTimePeriodChosen(tp);
                                        range = self.setTimePeriodToLastX(tp, start, end, 1);
                                    }else {
                                        customClick(0);
                                    }
                                }
                            }else {
                                self.lrCtrlVal("flexRelTimeCtrl");
                                
                                var parsedTp = ctxUtil.parseTimePeriodToUnitAndDuration(omcContext.time.timePeriod);
                                var tmp = self.getTimeRangeForFlexRelTime(omcContext.time.timePeriod);
                                start = oj.IntlConverterUtils.isoToLocalDate(tmp.start);
                                end = oj.IntlConverterUtils.isoToLocalDate(tmp.end);
                                
                                if(parsedTp) {
                                    self.flexRelTimeVal(parsedTp.duration);
                                    self.flexRelTimeOpt([parsedTp.unit]);
                                }

                                if(!params.timePeriodsSet && ko.unwrap(omcContext.time.timePeriod) === "LAST_1_WEEK") {
                                    tp = self.timePeriodLast7days;
                                    if($.inArray(tp, tpNotToShow) === -1) {
                                        self.setTimePeriodChosen(tp);
                                        range = self.setTimePeriodToLastX(tp, start, end, 1);
                                    }else {
                                        customClick(0);
                                    }
                                }else if(!params.timePeriodsSet && ko.unwrap(omcContext.time.timePeriod) === "LAST_1_HOUR") {
                                    tp = self.timePeriodLast60mins;
                                    if($.inArray(tp, tpNotToShow) === -1) {
                                        self.setTimePeriodChosen(tp);
                                        range = self.setTimePeriodToLastX(tp, start, end, 1);
                                    }else {
                                        customClick(0);
                                    }
                                }else {
                                    self.timePeriod(self.timePeriodCustom);
                                }
                            }
                        } else if(omcContext.time && omcContext.time.startTime && omcContext.time.endTime && 
                                !isNaN(parseInt(omcContext.time.startTime)) && !isNaN(parseInt(omcContext.time.endTime))) {
                            curDate = new Date();
                            //users input start date and end date
                            sdt = new Date(parseInt(omcContext.time.startTime));
                            edt = new Date(parseInt(omcContext.time.endTime));
                            start = newDateWithMilliseconds(sdt);
                            end = newDateWithMilliseconds(edt);
                            console.log("startTime from global context is " + start + ", endDateTime from gloal context is " + end);
                            console.log("Initializing time is " + curDate);
                            if(Math.abs(end.getTime()-curDate.getTime())>60*1000 || end.getMinutes() !== curDate.getMinutes()) {
                                var t_timePeriod = self.timePeriodCustom;
                                self.lrCtrlVal("timeLevelCtrl");
                                customClick(0);
                            }else {
                                dateTimeDiff = end - start;
                                var t_timePeriod = in_array(dateTimeDiff, self.timePeriodObject());
                                if (t_timePeriod && $.inArray(t_timePeriod, tpNotToShow)<0) {
                                    self.setTimePeriodChosen(t_timePeriod);
                                    range = self.setTimePeriodToLastX(t_timePeriod, start, end, 0);
                                    start = range.start;
                                    end = range.end;
                                } else {
                                    self.lrCtrlVal("timeLevelCtrl");
                                    customClick(0);
                                }
                            }
                        }else {
                            //set default time period if there is
                            if(self.timePeriodsNlsObject[self.defaultTimePeriod()]) { //for quick picks
                                tp = self.timePeriodsNlsObject[self.defaultTimePeriod()];
                                if(tp === self.timePeriodToday) {
                                    start = new Date(curDate.getFullYear(), curDate.getMonth(), curDate.getDate());
                                    end = curDate;
                                    self.setTimePeriodChosen(tp);
                                    self.setTimePeriodToLastX(tp, start, end, 1);
                                }else if(tp === self.timePeriodLatest) {
                                    start = curDate;
                                    end = curDate;
                                    if (self.showLatestOnCustomPanel()) {
                                        self.lrCtrlVal("latestOnCustom");
                                    } else {
                                        self.setTimePeriodChosen(tp);
                                        self.setTimePeriodToLastX(tp, start, end, 1);
                                    }
                                }else if(tp === self.timePeriodCustom) {
                                    if(params.defaultStartDateTime && params.defaultEndDateTime) {
                                        sdt = self.getParam(params.defaultStartDateTime);
                                        edt = self.getParam(params.defaultEndDateTime);
                                        start = newDateWithMilliseconds(sdt);
                                        end = newDateWithMilliseconds(edt);
                                        customClick(0);
                                    }else {
                                        console.error("Error: set defaultTimePeriod to 'Custom' without time range specified");
                                        return;
                                    }
                                } else {
                                    self.lrCtrlVal("flexRelTimeCtrl");

                                    parsedTp = ctxUtil.parseTimePeriodToUnitAndDuration(convertTPIdForQuickPick(formalizeTimePeriod(self.defaultTimePeriod()), !params.timePeriodsSet));

                                    var tmp = self.getTimeRangeForFlexRelTime(self.defaultTimePeriod());
                                    start = oj.IntlConverterUtils.isoToLocalDate(tmp.start);
                                    end = oj.IntlConverterUtils.isoToLocalDate(tmp.end);

                                    if(parsedTp) {
                                        self.flexRelTimeVal(parsedTp.duration);
                                        self.flexRelTimeOpt([parsedTp.unit]);
                                    }

                                    if($.inArray(tp, tpNotToShow) === -1) {
                                        self.setTimePeriodChosen(tp);
                                        range = self.setTimePeriodToLastX(tp, start, end, 1);
                                    }else {
                                        customClick(0);
                                    }
                                }
                                
                                //update url with default time period when there is no global time context in url
                                if(tp === self.timePeriodCustom) {
                                    //update url with default time period when there is no global time context in url
                                    ctxUtil.setStartAndEndTime(start.getTime(), end.getTime(), eventSourceTimeSelector);
                                }else {
                                    ctxUtil.setTimePeriod(self.defaultTimePeriod(), eventSourceTimeSelector);
                                }
                            }else if(isValidFlexRelTimePeriod(self.defaultTimePeriod())){ //for flexible relative time
                                self.lrCtrlVal("flexRelTimeCtrl");
                                
                                var parsedTp = ctxUtil.parseTimePeriodToUnitAndDuration(self.defaultTimePeriod());
                                var tmp = self.getTimeRangeForFlexRelTime(self.defaultTimePeriod());
                                start = oj.IntlConverterUtils.isoToLocalDate(tmp.start);
                                end = oj.IntlConverterUtils.isoToLocalDate(tmp.end);
                                
                                if(parsedTp) {
                                    self.flexRelTimeVal(parsedTp.duration);
                                    self.flexRelTimeOpt([parsedTp.unit]);
                                }

                                if(!params.timePeriodsSet && self.defaultTimePeriod() === "LAST_1_WEEK") {
                                    tp = self.timePeriodLast7days;
                                    if($.inArray(tp, tpNotToShow) === -1) {
                                        self.setTimePeriodChosen(tp);
                                        range = self.setTimePeriodToLastX(tp, start, end, 1);
                                    }else {
                                        customClick(0);
                                    }
                                }else if(!params.timePeriodsSet && self.defaultTimePeriod() === "LAST_1_HOUR") {
                                    tp = self.timePeriodLast60mins;
                                    if($.inArray(tp, tpNotToShow) === -1) {
                                        self.setTimePeriodChosen(tp);
                                        range = self.setTimePeriodToLastX(tp, start, end, 1);
                                    }else {
                                        customClick(0);
                                    }
                                }else {
                                    self.timePeriod(self.timePeriodCustom);
                                }
                                
                                //update url with default time period when there is no global time context in url
                                ctxUtil.setTimePeriod(self.defaultTimePeriod(), eventSourceTimeSelector);
                            }else {
                                //users input nothing
                                if($.inArray(self.timePeriodLast15mins, tpNotToShow)<0) {
                                    self.setTimePeriodChosen(self.timePeriodLast15mins);
                                    range = self.setTimePeriodToLastX(self.timePeriodLast15mins, start, end, 0);
                                    start = range.start;
                                    end = range.end;
                                }else{
                                    customClick(0);
                                }
                            }
                        }
                    }

                    if (start.getTime() > end.getTime()) {
                        if($.inArray(self.timePeriodLast15mins, tpNotToShow)<0) {
                            self.setTimePeriodChosen(self.timePeriodLast15mins);
                            range = self.setTimePeriodToLastX(self.timePeriodLast15mins, start, end, 0);
                            start = range.start;
                            end = range.end;
                        }else {
                            customClick(0);
                        }
                        //print warning...
                        oj.Logger.warn("Start time is larger than end time. Change time range to default time range");
                    }

                    start = oj.IntlConverterUtils.dateToLocalIso(start);
                    end = oj.IntlConverterUtils.dateToLocalIso(end);

                    self.startDate(self.dateConverter2.format(start));
                    self.endDate(self.dateConverter2.format(end));

                    self.startTime(start.slice(10));
                    self.endTime(end.slice(10));

                    self.dateTimeInfo(self.getDateTimeInfo(start.slice(0, 10), end.slice(0, 10), self.startTime(), self.endTime(), self.timePeriod()));
                    
                    self.lastStartDate(self.startDate());
                    self.lastEndDate(self.endDate());
                    self.lastStartTime(self.startTime());
                    self.lastEndTime(self.endTime());
                    self.lastTimePeriod(self.timePeriod());
                    self.lastLrCtrlVal(self.lrCtrlVal());
                    self.lastFlexRelTimeVal(self.flexRelTimeVal());
                    self.lastFlexRelTimeOpt([self.flexRelTimeOpt()[0]]);
                    if(self.enableTimeFilter()) {
                        self.lastTimeFilterValue(self.tfInstance.timeFilterValue());
                        self.lastDaysChecked(self.tfInstance.daysChecked());
                        self.lastMonthsChecked(self.tfInstance.monthsChecked());
                    }
            };

            /**
             * type: 0 for initialize time picker, 1 for user's action. This param is used for not validating window limit when initialized.
             * @returns {undefined}
             */
                function customClick(type) {
                    self.timePeriod(self.timePeriodCustom);
                    self.selectByDrawer(false);

                    self.customChosen(true);
                    self.showCalendar(true);

                    // Do not validate window limit when initialized
                    if(type === 0) {
                        return;
                    }

                    if(self.customWindowLimit) {
                        self.isCustomBeyondWindowLimit();
                    }
                }

                self.setFocusOnInput = function (idToFocus) {
                    if(self.lrCtrlVal()!=='timeLevelCtrl') { //do not set focus on inputs if they are diasbled
                        return;
                    }
                    var id;
                    var ele = $(self.pickerPanelId + " .input-focus");
                    if (ele.length > 0) {
                        id = ele[0].id;
                        $(self.pickerPanelId + " #" + id).removeClass("input-focus");
                    }
                    if (idToFocus && ((idToFocus === "divStartTime_" + self.randomId && !self.startTimeError()) || (idToFocus === "divEndTime_" + self.randomId && !self.endTimeError()) ||
                                idToFocus === "inputStartDate_" + self.randomId || idToFocus === "inputEndDate_" + self.randomId)) {
                            $(self.pickerPanelId + " #" + idToFocus).addClass("input-focus");
                    }
                };

                self.focusOnStartDate = function (data, event) {
                    self.setMinMaxDate(null, self.endDateISO());
                    self.gotoCalendarMonth(self.startDateISO().slice(0, 10));
                    setTimeout(function() {                        
                        self.updateRange(self.startDate(), self.endDate());
                    }, 0);
                    self.selectByDrawer(false);
                    self.setFocusOnInput(event.target.id);
                    self.lastFocus(1);
                };

                self.focusOnEndDate = function (data, event) {
                    self.setMinMaxDate(self.startDateISO(), null);
                    self.gotoCalendarMonth(self.endDateISO().slice(0, 10));
                    setTimeout(function() {                        
                        self.updateRange(self.startDate(), self.endDate());
                    }, 0);
                    self.selectByDrawer(false);
                    self.setFocusOnInput(event.target.id);
                    self.lastFocus(2);
                };

                self.focusOnStartTime = function (data, event) {
                    self.selectByDrawer(false);
                    self.gotoCalendarMonth(self.startDateISO().slice(0, 10));
                    
                    //when the focus is on start time, users can set start date using calendar.
                    self.setMinMaxDate(null, self.endDateISO());
                    setTimeout(function() {                        
                        self.updateRange(self.startDate(), self.endDate());
                        self.setFocusOnInput(event.target.parentNode.parentNode.parentNode.id);
                        self.lastFocus(0);
                    }, 0);
                };
                self.focusOnEndTime = function (data, event) {
                    self.selectByDrawer(false);
                    self.gotoCalendarMonth(self.endDateISO().slice(0, 10));
                    
                    //when the focus is on end time, users can set start date using calendar.
                    self.setMinMaxDate(null, self.endDateISO());
                    setTimeout(function() {
                        self.updateRange(self.startDate(), self.endDate());
                        self.setFocusOnInput(event.target.parentNode.parentNode.parentNode.id);
                        self.lastFocus(0);
                    }, 0);
                    
                };

                self.autoFocus = function (id) {
                    self.selectByDrawer(false);
                    self.setFocusOnInput(id);
                };

                /**
                 * 
                 * @param {type} flexRelTime
                 * @returns {start: <start time in ISO format>, end: <end time in ISO format>}
                 */
                self.getTimeRangeForFlexRelTime = function(flexRelTime) {
                    var timeRange;
                    var start;
                    var end;
                    timeRange = ctxUtil.getStartEndTimeFromTimePeriod(flexRelTime);
                    start = timeRange.start;
                    end = timeRange.end;
                    
                    if(self.adjustLastX) {
                        timeRange = self.adjustLastX(start, end);
                        start = timeRange.start;
                        end = timeRange.end;
                    }
                    
                    start = oj.IntlConverterUtils.dateToLocalIso(start);
                    end = oj.IntlConverterUtils.dateToLocalIso(end);
                    
                    return {
                        start: start,
                        end: end
                    }
                }
                
                self.setFlexRelTime = function(num, opt) {
                    var tp = ctxUtil.generateTimePeriodFromUnitAndDuration(opt, num);
                    var timeRange = self.getTimeRangeForFlexRelTime(tp);
//                    var timeRange = self.getTimeRangeForFlexRelTime("LAST_"+num+"_"+opt);
                    var start = timeRange.start;
                    var end = timeRange.end;
                    
                    self.startDate(self.dateConverter2.format(start));
                    self.endDate(self.dateConverter2.format(end));
                    
                    self.startTime(start.slice(10));
                    self.endTime(end.slice(10));
                }
                
                self.numberValidator = {
                    'validate': function(value) {
                        //TO DO: Need to confirm with Juan what is the biggest number allowed
                       if(value >= 1 && $.isNumeric(value) && (parseInt(value) === value) && value.toString().length<4) {
                           return true;
                       }else {
                           throw new Error(self.felRelTimeValError);
                       }
                    }
                };
                     
                self.flexRelTimeValChanged = function(event, data) {
                    if(data.option === "messagesShown" && isArray(data.value) && data.value.length>0) {
                        self.flexRelTimeValError(true);
                    }else {
                        self.flexRelTimeValError(false);
                    }
                    if(self.init || data.option !== "value") {
                        return;
                    }
                    var num = data.value;
                    var opt = self.flexRelTimeOpt()[0];
                    
                    //show sigular/plural time level options
                    switch(opt) {
                        case ctxUtil.OMCTimeConstants.TIME_UNIT.SECOND:
                            if(num === 1) {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_SECOND);
                            }else {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_SECONDS);
                            }
                            break;
                        case ctxUtil.OMCTimeConstants.TIME_UNIT.MINUTE:
                            if(num === 1) {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_MINUTE);
                            }else {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_MINUTES);
                            }
                            break;
                        case ctxUtil.OMCTimeConstants.TIME_UNIT.HOUR:
                            if(num === 1) {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_HOUR);
                            }else {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_HOURS);
                            }
                            break;
                        case ctxUtil.OMCTimeConstants.TIME_UNIT.DAY:
                            if(num === 1) {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_DAY);
                            }else {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_DAYS);
                            }
                            break;
                        case ctxUtil.OMCTimeConstants.TIME_UNIT.WEEK:
                            if(num === 1) {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_WEEK);
                            }else {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_WEEKS);
                            }
                            break;
                        case ctxUtil.OMCTimeConstants.TIME_UNIT.MONTH:
                            if(num === 1) {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_MONTH);
                            }else {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_MONTHS);
                            }
                            break;
                        case ctxUtil.OMCTimeConstants.TIME_UNIT.YEAR:
                            if(num === 1) {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_YEAR);
                            }else {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_YEARS);
                            }
                            break;
                        default:
                            throw new Error("error in flexRelTimeValChanged. Flexible relative time option-"+opt+" is invalid");
                            
                    }
                    
                    console.log("flexRelTimeValChanged: num: " + num + ", opt: " + opt);
                    self.setFlexRelTime(num, opt);
                                        
                };
                
                self.flexRelTimeOptChanged = function(event, data) {
                    if(self.init || data.option !== "value") {
                        return;
                    }
                    
                    if(data.previousValue && data.value && (data.previousValue[0]===data.value[0])) {
                        return;
                    }
                    
                    var opt = data.value[0];
                    var num = self.flexRelTimeVal();
                    console.log("flexRelTimeOptChanged: num: " + num + ", opt: " + opt);
                    self.setFlexRelTime(num, opt);                   
                };
                
//                self.timeLevelOptChanged = function(event, data) {
//                    if(self.init || data.option !== "value") {
//                        return;
//                    }
//                    
//                    var opt = data.value[0];
//                    var tc;
//                    console.log("timeLevelOptChanged: opt: " + opt);
//                    switch(data.value[0]) {
//                        case "minute":
//                            tc = self.timeConverterMinute;
//                            break;
//                        case "second":
//                            tc = self.timeConverterSecond;
//                            break;
//                        case "millisecond":
//                            tc = self.timeConverterMillisecond;
//                            break;
//                        default:
//                            throw new Error("error in time level option change: option-" + "");
//                    }
//                    self.timeConverter(tc);
//                }

                self.changeDate = function (event, data, value) {
                    if(data.option !== "value") {
                        return;
                    }
                    try {
                        if(isNaN(new Date(data.value))) {
                            throw true;
                        }
                        //make sure the date is valid.
                        var convertedDate = self.dateConverter2.format(oj.IntlConverterUtils.dateToLocalIso(new Date(data.value)));
                        if(convertedDate !== data.value) {
                            throw true;
                        }

                        $(event.target).removeClass("input-error");
                        if (value === 1) {
                            self.startDateError(false);
                        } else if (value === 2) {
                            self.endDateError(false);
                        }
                        if (data.option === "value" && !self.selectByDrawer()) {
                            if(self.init === true) {
                               customClick(0);
                            }else {
                                self.setTimePeriodChosen(self.timePeriodCustom);
                                customClick(1);
                            }
                            if(value === 1) {
                                self.gotoCalendarMonth(self.startDateISO().slice(0, 10));
                            }else if(value === 2) {
                                self.gotoCalendarMonth(self.endDateISO().slice(0, 10));
                            }
                            self.updateRange(self.startDate(), self.endDate());
                        }
                        timeValidate();
                    } catch (e) {
                        if (value === 1) {
                            self.startDateError(true);
                        } else if (value === 2) {
                            self.endDateError(true);
                        }
                        $(event.target).addClass("input-error");
                        if (self.pickerPanelId) {
                            $(self.pickerPanelId + " #applyButton_"+self.randomId).ojButton({"disabled": true});
                        }
                    }
                };

                self.changeTimeCorrect = function(event, data, whichTime) {
                    var timeErrorType, eleId;
                    if(whichTime === 1) {
                        timeErrorType = self.startTimeError;
                        eleId = "divStartTime_" + self.randomId;
                    }else if(whichTime === 2) {
                        timeErrorType = self.endTimeError;
                        eleId = "divEndTime_" + self.randomId;
                    }

                    timeErrorType(false);
                    self.restoreBorderForTime(event.target);

                    if (data.option === "value" && !self.selectByDrawer()) {
                        self.setFocusOnInput(eleId);
                        self.lastFocus(0);
                        self.setTimePeriodChosen(self.timePeriodCustom);
                        customClick(1);
                    }
                    timeValidate();
                };

                self.changeTimeError = function (event, data, whichTime) {
                    var timeErrorType;
                    if(whichTime === 1) {
                        timeErrorType = self.startTimeError;
                    }else if(whichTime === 2) {
                        timeErrorType = self.endTimeError;
                    }

                    timeErrorType(true);
                    $(event.target.parentNode.parentNode.parentNode).removeClass("input-focus");
                    if (data.value === null || data.value.length === 0 || data.value.length === 1) {
                        self.setErrorBorderForTime(event.target);
                    }
                    if (self.pickerPanelId) {
                        $(self.pickerPanelId + " #applyButton_"+self.randomId).ojButton({"disabled": true});
                    }
                };

                self.changeStartTime = function (event, data) {
                    if(data.option === "messagesShown" && oj.Message.getMaxSeverity(data.value)>=4) { //messagesShown means input time format is incorrect
                        self.changeTimeError(event, data, 1);
                        return;
                    }
                    if((data.option !== "value" && data.option !== "rawValue") || (data.option === "rawValue" && !data.previousValue)) {
                        return;
                    }
                    if (typeof data.value === "string") {
                        self.changeTimeCorrect(event, data, 1);
                    }else {
                        self.changeTimeError(event, data, 1);
                    }
                };

                self.changeEndTime = function (event, data) {
                    if(data.option === "messagesShown" && oj.Message.getMaxSeverity(data.value)>=4) {
                        self.changeTimeError(event, data, 1);
                        return;
                    }
                    if((data.option !== "value" && data.option !== "rawValue") || (data.option === "rawValue" && !data.previousValue)) {
                        return;
                    }
                    if (typeof data.value === "string") {
                        self.changeTimeCorrect(event, data, 2);
                    }else {
                        self.changeTimeError(event, data, 2);
                    }
                };

                function timeValidate() {
                    var start = self.startDateISO().slice(0, 10) + self.startTime();
                    var end = self.endDateISO().slice(0, 10) + self.endTime();
                    if(start > end) {
                        self.timeValidateError(true);
                    }else {
                        self.timeValidateError(false);
                    }
                    
                    var endDate = oj.IntlConverterUtils.dateToLocalIso(new Date(self.endDate())).slice(0, 10);
                    if(!params.hideTimeSelection) {
                        endDate = endDate + self.endTime();
                    }
                    
                    var current = oj.IntlConverterUtils.dateToLocalIso(new Date());
                    if(endDate > current) {
                        self.timeValidateFutureError(true);
                    }else {
                        self.timeValidateFutureError(false);
                    }
                }

                self.setErrorBorderForTime = function (target) {
                    $(target).css("border-width", "2px 0px 2px 2px").next().css("border-width", "2px 2px 2px 0px");
                    $(target).css("border-color", "#d66").next().css("border-color", "#d66");
                };
                self.restoreBorderForTime = function(target) {
                    $(target).css("border-width", "1px 0px 1px 1px").next().css("border-width", "1px 1px 1px 0px");
                    $(target).css("border-color", "#dfe4e7").next().css("border-color", "#dfe4e7");
                };

                /**
                 *
                 * @param {Date} startRange start date
                 * @param {Date} endRange end date
                 * @returns {undefined}
                 */
                self.updateRange = function (startRange, endRange) {
                    if(!startRange || !endRange) {
                        startRange = self.startDate();
                        endRange = self.endDate();
                    }
                    self.renderDateRange(startRange, endRange);

                };
                
                /**
                 * Shift calendar to start or end date month by setting calendar value
                 * @param {type} dateISO
                 * @returns {undefined}
                 */
                self.gotoCalendarMonth = function(dateISO) {
                    self.value(dateISO);
                };

                self.closeAllPopups = function() {
                    // The flag "shouldSetLastDatas" is used to solve popup closing issues in Safari. It is set to true only when popups are to be closed. And at this time, self.setLastDatas should be called.
                    // Not sure why self.setLastDatas is called when popup is not closed.
                    self.shouldSetLastDatas = true;
                    $(self.pickerPanelId).ojPopup('close');
                    $(self.panelId).ojPopup('close');
                };

                //contol whether the panel should popup or not
                self.panelControl = function (data,event) {
                    event.stopPropagation();
                    if(self.showPanel() === false) {
                        self.showPanel(true);
                    }
                    if(!self.dtpickerPosition) {
                        if(params.dtpickerPosition) {
                            if(params.dtpickerPosition === "right") {
                                self.setDtpickerPositionToRight();
                            }else{
                                self.setDtpickerPositionToLeft();
                            }
                            self.dtpickerPosition = params.dtpickerPosition;
                        }else{
                            if(self.calDtpickerPosition() === "right") {
                                self.setDtpickerPositionToRight();
                            }else{
                                self.setDtpickerPositionToLeft();
                            }
                            self.dtpickerPosition = self.calDtpickerPosition();
                        }
                    }
                    
                    if(self.init === true) {
                        self.init = false;
                    }
                    if ($(self.panelId).ojPopup('isOpen')) {
                        self.closeAllPopups();
                    } else {
                        //Close overflowed label popup and badge info popup
                        $("#overflowedLabelInfo_"+self.randomId).ojPopup('close');
                        $('.badge-popup-message').ojPopup("close");

                        self.recentChosen(false);
                        self.showRightPanel(false);
                        self.autoFocus("inputStartDate_" + self.randomId);
                        self.lastFocus(1);

                        self.gotoCalendarMonth(self.startDateISO().slice(0, 10));
                        self.updateRange(self.startDate(), self.endDate());

                        $(self.panelId).ojPopup('open', self.wrapperId + ' #dropDown_' + self.randomId, self.panelPosition);
                        if(!self.closePanelOnClickingOut){
                            self.closePanelOnClickingOut = document.addEventListener("click",function(_evnt){
                                if ($(self.panelId).ojPopup('isOpen')) {
                                    self.closeAllPopups();
                                }
                            },false);
                        }
                        if(!self.removeClickListenerOnChosenTime){
                            self.removeClickListenerOnChosenTime = true;
                            $(".datetimepicker-dropdown").each(function(){
                                this.addEventListener("click",function(_evnt){
                                    _evnt.stopPropagation();
                                    return true;
                                },false);
                            });
                        }
                    }
                };

                /**
                 * The function is used to launch right panel with calender to specific position
                 *
                 * @param {type} ele The right panel popup will apear relative to this element
                 * @param {type} position The location of the right panel popup
                 * @returns {undefined}
                 */
                self.launchTimePickerCustom = function(ele, position, option) {
                    if(self.init === true) {
                        self.init = false;
                    }
                    self.autoFocus("inputStartDate_" + self.randomId);
                    self.lastFocus(1);

                    self.gotoCalendarMonth(self.startDateISO().slice(0, 10));
                    setTimeout(function(){self.updateRange(self.startDate(), self.endDate());}, 0);

                    self.showRightPanel(true);

                    $("#pickerPanel_"+self.randomId).ojPopup("option", option);
                    $("#pickerPanel_"+self.randomId).ojPopup("open", ele,  position);

                    self.showCalendar(true);
                };

                self.isEnterPressed = function(data, event) {
                    var keyCode = event.which ? event.which : event.keyCode;
                    if(keyCode === 13) {
                        return true;
                    }else {
                        return false;
                    }
                };

                /**
                 * set everyting to original state if not applied
                 * @returns {undefined}
                 */
                self.setLastDatas = function (event, ui) {
                    if(self.shouldSetLastDatas !== true) {
                        event.preventDefault();
                        self.shouldSetLastDatas = false;
                        return;
                    }
                    self.startDate(self.lastStartDate());
                    self.endDate(self.lastEndDate());
                    self.startTime(self.lastStartTime());
                    self.endTime(self.lastEndTime());
                    self.timePeriod(self.lastTimePeriod());
                    self.lrCtrlVal(self.lastLrCtrlVal());
                    self.flexRelTimeVal(self.lastFlexRelTimeVal());
                    self.flexRelTimeOpt([self.lastFlexRelTimeOpt()[0]]);
                    if(self.enableTimeFilter()) {
                        self.tfInstance.timeFilterValue(self.lastTimeFilterValue());
                        self.tfInstance.daysChecked(self.lastDaysChecked());
                        self.tfInstance.monthsChecked(self.lastMonthsChecked());
                    }
                    self.setMinMaxDate(null, null);

                    if(self.lastTimePeriod() !== self.timePeriodCustom) {
                        self.showCalendar(false);
                        self.beyondWindowLimitError(false);
                        if (self.showLatestOnCustomPanel() && self.lastTimePeriod()===self.timePeriodLatest) {
                            self.lrCtrlVal("latestOnCustom");
                        } else {
                            self.setTimePeriodChosen(self.lastTimePeriod());
                            self.setTimePeriodToLastX(self.lastTimePeriod(), null, null, 0);
                        }
                    }else{
                        self.showCalendar(true);
                        var lastBeyondWindowLimitError = self.beyondWindowLimitError();
                        self.init = !lastBeyondWindowLimitError;
                        customClick(1);
                        self.beyondWindowLimitError(lastBeyondWindowLimitError);
                    }

                    self.shouldSetLastDatas = false;
                };

                self.hoverOnDrawer = function(data, event) {
                    if(!$(event.target).hasClass(self.drawerChosen())) {
                        $(event.target).addClass(self.drawerHover);
                    }
                };

                self.hoverOutDrawer = function(data, event) {
                    $(event.target).removeClass(self.drawerHover);
                };
                
                self.chooseByRecent = function(data, event) {
                    event.stopPropagation();
                    var tmp;
                    var start;
                    var end;
                    var parsedTp;
                    var tp;
                    var tpNls = self.timePeriodsNlsObject[data.timePeriod];
                    var tpNotToShow = self.getParam(self.timePeriodsNotToShow);
                    if(data.timePeriod === ctxUtil.OMCTimeConstants.QUICK_PICK.CUSTOM) { // for custom time range
                        self.lrCtrlVal("timeLevelCtrl");
                        start = oj.IntlConverterUtils.dateToLocalIso(data.start);
                        end = oj.IntlConverterUtils.dateToLocalIso(data.end);
                        self.timePeriod(tpNls);
                    }else if(tpNls) { // for quick picks
                        if(tpNls === self.timePeriodLatest) {
                            self.lrCtrlVal("timeLevelCtrl");
                        }else {
                            self.lrCtrlVal("flexRelTimeCtrl");
                            tp = data.timePeriod;

                            tp = convertTPIdForQuickPick(tp, !params.timePeriodsSet);

                            parsedTp = ctxUtil.parseTimePeriodToUnitAndDuration(tp);
                            if(parsedTp) {
                                self.flexRelTimeVal(parsedTp.duration);
                                self.flexRelTimeOpt([parsedTp.unit]);
                            }
                        }
                        tmp = self.getTimeRangeForQuickPick(tpNls);
                        start = tmp.start;
                        end = tmp.end;                       
                        
                        self.selectByDrawer(true);
                        self.timePeriod(tpNls);
                        self.setTimePeriodChosen(self.timePeriod());
                    }else {
                        parsedTp = ctxUtil.parseTimePeriodToUnitAndDuration(data.timePeriod);
                        tmp = self.getTimeRangeForFlexRelTime(data.timePeriod);
                        start = tmp.start;
                        end = tmp.end;

                        self.lrCtrlVal("flexRelTimeCtrl");
                        if(parsedTp) {
                            self.flexRelTimeVal(parsedTp.duration);
                            self.flexRelTimeOpt([parsedTp.unit]);
                        }

                        tp = data.timePeriod;
                        if(!params.timePeriodsSet && tp === "LAST_1_WEEK") {
                            tp = self.timePeriodLast7days;
                            if($.inArray(tp, tpNotToShow) === -1) {
                                self.setTimePeriodChosen(tp);
                                self.setTimePeriodToLastX(tp, new Date(start), new Date(end), 1);
                            }else {
                                customClick(0);
                            }
                        }else if(!params.timePeriodsSet && tp === "LAST_1_HOUR") {
                            tp = self.timePeriodLast60mins;
                            if($.inArray(tp, tpNotToShow) === -1) {
                                self.setTimePeriodChosen(tp);
                                self.setTimePeriodToLastX(tp, new Date(start), new Date(end), 1);
                            }else {
                                customClick(0);
                            }
                        }else {
                            self.timePeriod(self.timePeriodCustom);
                        }
                    }
                    
                    self.startDate(self.dateConverter2.format(start));
                    self.endDate(self.dateConverter2.format(end));

                    self.startTime(start.slice(10));
                    self.endTime(end.slice(10));
                    
                    self.recentList.remove(data);
                    setTimeout(function() {self.applyClick();}, 0);
                };
                
                /**
                 * 
                 * @param {type} timePeriod - should be nls string
                 * @returns ISO format start and end time for specific quick pick
                 */
                self.getTimeRangeForQuickPick = function(timePeriod) {
                    var curDate;
                    var start;
                    var end;
//                    curDate = new Date(2016, 2, 13, 3, 0, 0);
                    curDate = new Date();
                    if (timePeriod === self.timePeriodLatest) {
                        start = curDate;
                        end = curDate;
                    } else if (timePeriod === self.timePeriodToday) { //set time range from mid-night to current point
                        start = new Date(curDate.getFullYear(), curDate.getMonth(), curDate.getDate());
                        end = curDate;
                    } else if (timePeriod === self.timePeriodLast1year) {
                        start = new Date(curDate.getFullYear() - 1, curDate.getMonth(), curDate.getDate(), curDate.getHours(), curDate.getMinutes(), curDate.getSeconds(), curDate.getMilliseconds());
                        end = curDate;
                    } else if (self.isTimePeriodLessThan1day(timePeriod)) {  //if timerange is less than 1 day, do not adjust start and end during daylight saving time
                        start = new Date(curDate - self.timePeriodObject()[timePeriod][1]);
                        end = curDate;
                        if (self.adjustLastX) {
                            var adjustedTime = self.adjustLastX(start, end);
                            start = adjustedTime.start;
                            end = adjustedTime.end;
                        }
                    } else {
                        start = new Date(curDate - self.timePeriodObject()[timePeriod][1]);
                        //calculate timezone difference to solve issues caused by daylight saving.
                        var timezoneDiffInMillis = (curDate.getTimezoneOffset() - start.getTimezoneOffset()) * 60 * 1000;
                        start = new Date(start.getTime() - timezoneDiffInMillis);
                        end = curDate;
                        if (self.adjustLastX) {
                            var adjustedTime = self.adjustLastX(start, end);
                            start = adjustedTime.start;
                            end = adjustedTime.end;
                        }
                    }

                    start = oj.IntlConverterUtils.dateToLocalIso(start);
                    end = oj.IntlConverterUtils.dateToLocalIso(end);
                    
                    return {
                        start: start,
                        end: end
                    }
                }

                //select time period
                self.chooseTimePeriod = function (data, event) {
                    var timeRange;
                    var start;
                    var end;
                    var tpId;
                    var parsedTp;

                    var chosenPeriod;
                    if (!data) {
                        //choose the radio latest in the custom panel
                        chosenPeriod = event.extended;
                    } else {
                        chosenPeriod = $(event.target).text();
                    }
                    self.setFocusOnInput("inputStartDate_" + self.randomId);
                    self.lastFocus(1);
                    
                    if(chosenPeriod === self.timePeriodRecent) {
                        if(self.recentList().length>0) {
                            self.setAllTimePeriodsNotChosen();
                            self.recentChosen(true);
                            $("#recentPanel_"+self.randomId).ojPopup("open", "#drawer14_"+self.randomId, {"my": "start top", "at": "end top"});
                        }
                        return;
                    }
                    
                    if($(event.target).hasClass("drawerDisabled")) {
                        return;
                    }

                    if (chosenPeriod !== self.timePeriodCustom) {
                        //just show window limit error in custom mode
                        self.beyondWindowLimitError(false);
                        self.setMinMaxDate(null, null);

                        tpId = self.getTimePeriodString(chosenPeriod);
                        tpId =  convertTPIdForQuickPick(tpId, !params.timePeriodsSet);
                        parsedTp = ctxUtil.parseTimePeriodToUnitAndDuration(tpId);

                        if(parsedTp) {
                            self.lrCtrlVal("flexRelTimeCtrl");
                            self.flexRelTimeVal(parsedTp.duration);
                            self.flexRelTimeOpt([parsedTp.unit]);
                        }else {
                            if(chosenPeriod === self.timePeriodLatest && self.showLatestOnCustomPanel()) {
                                self.lrCtrlVal("latestOnCustom");
                            }else{
                                self.lrCtrlVal("timeLevelCtrl");
                            }
                        }

                        timeRange = self.getTimeRangeForQuickPick(chosenPeriod);
                        start = timeRange.start;
                        end = timeRange.end;

                        self.startDate(self.dateConverter2.format(start));
                        self.endDate(self.dateConverter2.format(end));

                        self.startTime(start.slice(10));
                        self.endTime(end.slice(10));

                        self.selectByDrawer(true);

                        self.timePeriod(event.target.innerHTML);
                        self.hoverOutDrawer(data, event); //remove hover style when ele is clicked for firefox

                        if (chosenPeriod !== self.timePeriodLatest || !self.showLatestOnCustomPanel()) {
                            self.setTimePeriodChosen(self.timePeriod());
                            setTimeout(function () {
                                self.applyClick();
                            }, 0);
                        }else{
                            customClick(1);
                        }
                    }else {
                        self.showRightPanel(true);
                        //set modality to "modeless" in case it is set as "modal" in launchTimePickerCucstom function
                        $("#pickerPanel_"+self.randomId).ojPopup("option", "modality", "modeless");
                        $("#pickerPanel_"+self.randomId).ojPopup("open", "#dateTimePicker_"+self.randomId+" .drawers", self.pickerPanelPosition);
                        self.showCalendar(true);
                    }

                    self.gotoCalendarMonth(self.startDateISO().slice(0, 10));

                    setTimeout(function(){self.updateRange(self.startDate(), self.endDate());}, 0);
                    $(event.target).focus();
                };


                self.getTimePeriodString = function(tp) {
                    for(var i in self.timePeriodsNlsObject) {
                        if(self.timePeriodsNlsObject[i] === tp) {
                            return i;
                        }
                    }
                    return "";
                };

                self.applyClick = function (shouldSetOmcCtx) {
                    var flexRelTimeVal = null;
                    var flexRelTimeOpt = null;
                    var flexRelTimePeriodId = null;
                    var recentTimePeriodId = null;
                    if(shouldSetOmcCtx !== false && ko.isObservable(params.showBadge)) {
                        params.showBadge(false);
                    }
                    self.timeFilter = ko.observable(null);

                    self.lastStartDate(self.startDate());
                    self.lastEndDate(self.endDate());
                    self.lastStartTime(self.startTime());
                    self.lastEndTime(self.endTime());
                    self.lastTimePeriod(self.timePeriod());
                    self.lastLrCtrlVal(self.lrCtrlVal());
                    self.lastFlexRelTimeVal(self.flexRelTimeVal());
                    self.lastFlexRelTimeOpt([self.flexRelTimeOpt()[0]]);

                    self.setMinMaxDate(null, null);
                    var start, end;
                    
                    if(self.lrCtrlVal() === "flexRelTimeCtrl") {
                        flexRelTimeVal = self.flexRelTimeVal();
                        flexRelTimeOpt = self.flexRelTimeOpt()[0];
                        flexRelTimePeriodId = ctxUtil.generateTimePeriodFromUnitAndDuration(flexRelTimeOpt, flexRelTimeVal);

                        if(!params.timePeriodsSet && flexRelTimePeriodId === "LAST_1_HOUR") {
                            self.timePeriod(self.timePeriodLast60mins);
                        }else if(!params.timePeriodsSet && flexRelTimePeriodId === "LAST_1_WEEK") {
                            self.timePeriod(self.timePeriodLast7days);
                        }else if (self.timePeriodsNlsObject[flexRelTimePeriodId] && $.inArray(self.timePeriodsNlsObject[flexRelTimePeriodId], self.getParam(self.timePeriodsNotToShow)) === -1) {
                            self.timePeriod(self.timePeriodsNlsObject[flexRelTimePeriodId]);
                        }

                        self.lastTimePeriod(self.timePeriod());
                    }

                    if(self.enableTimeFilter()) {
                        self.lastTimeFilterValue(self.tfInstance.timeFilterValue());
                        self.lastDaysChecked(self.tfInstance.daysChecked());
                        self.lastMonthsChecked(self.tfInstance.monthsChecked());
                    }

                    if(self.hideTimeSelection() === false) {
                        start = oj.IntlConverterUtils.isoToLocalDate(self.startDateISO().slice(0, 10) + self.startTime());
                        end = oj.IntlConverterUtils.isoToLocalDate(self.endDateISO().slice(0, 10) + self.endTime());
                    }else {
                        start = oj.IntlConverterUtils.isoToLocalDate(self.startDateISO().slice(0, 10));
                        end = oj.IntlConverterUtils.isoToLocalDate(self.endDateISO().slice(0, 10));
                    }

                    if(self.getParam(self.changeLabel)) {
                        self.dateTimeInfo(self.getDateTimeInfo(self.startDateISO().slice(0, 10), self.endDateISO().slice(0, 10), self.startTime(), self.endTime(), self.timePeriod()));
                    }
                    self.closeAllPopups();
                    var timePeriod = self.getTimePeriodString(self.timePeriod());
                    
                    //get time period value and unit
                    var tp = ctxUtil.parseTimePeriodToUnitAndDuration(timePeriod);
                    if(tp) {
                        flexRelTimeVal = tp.duration;
                        flexRelTimeOpt = tp.unit;
                    }

                    //if time filter is enabled, pass time info in JSON format.
                    if(self.enableTimeFilter()) {
                        var hourOfDay = self.getHourOfDayInArray(self.tfInstance.timeFilterValue());
                        
                        self.timeFilter({
                            "STARTTIME": new Date(start),
                            "ENDTIME": new Date(end),
                            "DAYOFMONTH": [],
                            "DAYOFWEEK": self.tfInstance.daysChecked(),
                            "HOUROFDAY": hourOfDay,
                            "MONTHOFQUARTER": [],
                            "MONTHOFYEAR": self.tfInstance.monthsChecked(),
                            "QUARTEROFYEAR": [],
                            "WEEKOFERA": [],
                            "YEAROFERA": []
                        });
                        self.timeFilterInfo(self.generateTfTooltip(hourOfDay, self.tfInstance.daysChecked(), self.tfInstance.monthsChecked()));
                    }
                    
                    //reset time params in global context and update recent list
                    if(shouldSetOmcCtx !== false) {
                        var tmpList = ko.observableArray(self.recentList());
    //                    tmpList.unshift({start: new Date(start), end: new Date(end), timePeriod: timePeriod, 
    //                                    timeFilter: self.timeFilter(), flexRelTimeVal: flexRelTimeVal, flexRelTimeOpt: flexRelTimeOpt});
                        if(timePeriod === ctxUtil.OMCTimeConstants.QUICK_PICK.CUSTOM && flexRelTimeVal && flexRelTimeOpt) {
                            recentTimePeriodId =  flexRelTimePeriodId;
                        }else if(self.lrCtrlVal() === "latestOnCustom") {
                            recentTimePeriodId = ctxUtil.OMCTimeConstants.QUICK_PICK.LATEST;
                        }else {
                            recentTimePeriodId = timePeriod;
                        }
                        tmpList.remove(function(data) {return (data.timePeriod === recentTimePeriodId) && (recentTimePeriodId !== ctxUtil.OMCTimeConstants.QUICK_PICK.CUSTOM);});
                        tmpList.unshift({start: new Date(start), end: new Date(end), timePeriod: recentTimePeriodId, 
                                        timeFilter: self.timeFilter()});
                        self.recentList(tmpList.slice(0, 5));
                        
                        if(timePeriod === ctxUtil.OMCTimeConstants.QUICK_PICK.CUSTOM) {
                            if(flexRelTimeVal && flexRelTimeOpt) {
                                ctxUtil.setTimePeriod(flexRelTimePeriodId, eventSourceTimeSelector);
                            }else if(self.lrCtrlVal() === "latestOnCustom") {
                                ctxUtil.setTimePeriod(recentTimePeriodId, eventSourceTimeSelector);
                            }else {
                                ctxUtil.setStartAndEndTime(newDateWithMilliseconds(start).getTime(), newDateWithMilliseconds(end).getTime(),eventSourceTimeSelector);
                            }
                        }else {
                            ctxUtil.setTimePeriod(timePeriod, eventSourceTimeSelector);
                        }
                    }

                    if (self.callbackAfterApply) {
                        setTimeout(function () {
                                console.log("Returned values from date/time picker are: ");
                                console.log("start: "+new Date(start));
                                console.log("end: "+new Date(end));
                                if(flexRelTimePeriodId) {
                                    console.log("time period: " + flexRelTimePeriodId);
                                }else {
                                    console.log("time period: " + timePeriod);
                                }
                                console.log("time filter: "+JSON.stringify(self.timeFilter()));
                                console.log("flexible relative time value: "+flexRelTimeVal+", option: "+flexRelTimeOpt);
                                if(flexRelTimePeriodId) {
                                    self.callbackAfterApply(newDateWithMilliseconds(start), newDateWithMilliseconds(end), flexRelTimePeriodId, self.timeFilter(), flexRelTimeVal, flexRelTimeOpt);
                                }else {
                                    self.callbackAfterApply(newDateWithMilliseconds(start), newDateWithMilliseconds(end), timePeriod, self.timeFilter(), flexRelTimeVal, flexRelTimeOpt);
                                }
                        },0);
                    }
                    return false;
                };

                self.cancelClick = function () {
                    self.closeAllPopups();
                    if(self.callbackAfterCancel) {
                        setTimeout(function () {
                                self.callbackAfterCancel();
                        },0);
                    }
                    return false;
                };

                self.renderDateRange = function (startRange, endRange) {
                    var calendarId;
                    if ($(self.pickerPanelId + " #datePicker_" + self.randomId).children()[0]) {
                        calendarId = $(self.pickerPanelId + " #datePicker_" + self.randomId).children()[0].id;
                    } else {
                        return;
                    }

                    var uuid = calendarId.split("_")[0];
                    var curYears = [];
                    var tmpMonths = [];
                    var curDay;
                    var regExp = new RegExp(/\d{4}/);
                    var firstDayOfWeek = oj.LocaleData.getFirstDayOfWeek();

                    $(self.pickerPanelId + " .oj-datepicker-year").each(function () {
                        var year = $(this).text();
                        curYears.push(Number(year.match(regExp)[0]));
                    });
                    $(self.pickerPanelId + " .oj-datepicker-month").each(function () {
                        tmpMonths.push(self.monthObject()[$(this).text()]);
                    });

                    for (var i = 0; i < 2; i++) {
                        for (var j = 0; j < 6; j++) {
                            for (var k = 0; k < 7; k++) {
                                var ele = $(self.pickerPanelId + " #oj-dp-" + uuid + "-" + j + "-" + k + "-0" + "-" + i);
                                ele.removeClass("date-selected");
                                if (ele.hasClass("oj-datepicker-current-day")) {
                                    ele.removeClass("oj-datepicker-current-day");
                                    $(self.pickerPanelId + " #oj-dp-" + uuid + "-" + j + "-" + k + "-0" + "-" + i + " a").removeClass("oj-selected");
                                }
                                curDay = ele.text();

                                var isDayInDaysChecked = true;
                                var isDayInMonthsChecked = true;
                                if(self.enableTimeFilter()){
                                    var kDay;
                                    kDay = (k+firstDayOfWeek)%7 + 1;
                                    isDayInDaysChecked = ($.inArray(kDay.toString(), self.tfInstance.daysChecked()) === -1) ? false : true;
                                    isDayInMonthsChecked = ($.inArray(tmpMonths[i].toString(), self.tfInstance.monthsChecked()) === -1) ? false : true;
                                }
                                if (!ele.hasClass("oj-datepicker-other-month") &&
                                        (isDayInDaysChecked && isDayInMonthsChecked) && ((new Date(curYears[i], tmpMonths[i] - 1, curDay).getTime() >= new Date(startRange).getTime()) && (new Date(curYears[i], tmpMonths[i] - 1, curDay).getTime() <= new Date(endRange).getTime()))) {
                                        ele.addClass("date-selected");
                                }

                                //Set dates post today as inactive. To be continued
                            }
                        }
                    }
                };
                self.setFocusOnInput("inputStartDate_" + self.randomId);
                self.lastFocus(1);

                self.calendarOptionChanged = function(event, data) { //handle calender rendering when option "disabled" is changed
                    setTimeout(function() {self.updateRange(self.startDate(), self.endDate())}, 0);
                };

                self.calendarClicked = function (data, event) {
                    if ($(event.target).hasClass("oj-datepicker-prev-icon") || $(event.target).hasClass("oj-datepicker-next-icon") ||
                            $(event.target).hasClass("oj-datepicker-title") || $(event.target).hasClass("oj-datepicker-header") ||
                            $(event.target).hasClass("oj-datepicker-group") || $(event.target).hasClass("oj-datepicker-other-month") ||
                            $(event.target).hasClass("oj-disabled") || $(event.target).hasClass("oj-datepicker-content")) {
                        self.random1(new Date().getTime());
                    } else {
                        var ele = $(event.target);
                        var year = parseInt(ele.parent().attr("data-year"));
                        var month = parseInt(ele.parent().attr("data-month"));
                        var day = parseInt(ele.text());
                        
                        if(isNaN(year) || isNaN(month) || isNaN(day)){
                            self.random1(new Date().getTime());
                            return;
                        }
                        
                        self.value(oj.IntlConverterUtils.dateToLocalIso(new Date(year, month, day)));
                        self.random(new Date().getTime());
                        self.setTimePeriodChosen(self.timePeriodCustom);
                    }
                };

                self.timeFilterControl = function() {
                    if($(self.timeFilterId).ojPopup("isOpen")) {
                        $(self.timeFilterId).ojPopup("close");
                    }else {
                        $(self.timeFilterId).ojPopup("open", "#pickerPanel_"+self.randomId, self.timeFilterPosition);
                    }
                };

                self.showTimeFilterInfo = function() {
                    if(self.showTimeFilterInfoPopUp() === false) {
                        self.showTimeFilterInfoPopUp(true);
                    }
                    $("#tfInfo_"+self.randomId).ojPopup("open", "#tfInfoIndicator_"+self.randomId);
                    if ($(self.panelId).ojPopup('isOpen')) {
                        self.closeAllPopups();
                    }
                };

                self.hideTimeFilterInfo = function() {
                    $("#tfInfo_"+self.randomId).ojPopup("close");
                };

                self.toggleTimeFilterInfo = function() {
                    if(self.showTimeFilterInfoPopUp() === false) {
                        self.showTimeFilterInfoPopUp(true);
                    }
                    if($("#tfInfo_"+self.randomId).ojPopup("isOpen")) {
                        $("#tfInfo_"+self.randomId).ojPopup("close");
                    }else {
                        $("#tfInfo_"+self.randomId).ojPopup("open", "#tfInfoIndicator_"+self.randomId);
                        if ($(self.panelId).ojPopup('isOpen')) {
                            self.closeAllPopups();
                        }
                    }
                };

                self.getHourOfDayInArray = function(timeFilterValue) {
                    var hourOfDay = [];
                    var hourRangesOfDay = timeFilterValue.split(",");
                    for (var i = 0; i < hourRangesOfDay.length; i++) {
                        var hourRange = hourRangesOfDay[i].split("-");
                        if (hourRange.length !== 2) {
                            console.error("wrong input for time filter!");
                            return;
                        }
                        var hourRangeStart = parseInt(hourRange[0].trim());
                        var hourRangeEnd = parseInt(hourRange[1].trim());
                        for (var j = hourRangeStart; j <= hourRangeEnd; j++) {
                            hourOfDay.push(j.toString());
                        }
                    }
                    return hourOfDay;
                }

                self.generateTfTooltip = function(hoursOfDay, daysOfWeek, monthsOfYear) {
                    var i;
                    var tfTooltip = "";
                    if(hoursOfDay.length === 24 && daysOfWeek.length === 7 && monthsOfYear.length === 12) {
                        self.tfInfoIndicatorVisible(false);
                        return null;
                    }else {
//                        $("#dateTimePicker_"+self.randomId+" .oj-select-chosen").css("padding-right", "44px");
                        $(".oj-select-chosen").css("padding-right", "44px");
                        self.tfInfoIndicatorVisible(true);
                        //get hours excluded
                        var hoursExcludedInfo = "";
                        var hoursExcluded = [];
                        var hoursExcludedStart = [];
                        var hoursExcludedEnd = [];
                        if(hoursOfDay.length < 24) {
                            hoursExcludedInfo += self.tfHoursExcludedMsg;
                            for(i=0; i<24; i++) {
                                if($.inArray(i.toString(), hoursOfDay) === -1) {
                                    hoursExcluded.push(i);
                                }
                            }
                            hoursExcludedStart.push(hoursExcluded[0]);
                            for(i=1; i<hoursExcluded.length; i++) {
                                if(i === hoursExcluded.length-1) {
                                    if(hoursExcluded[i] - hoursExcluded[i-1] === 1) {
                                        hoursExcludedEnd.push(hoursExcluded[i]);
                                    }else {
                                        hoursExcludedEnd.push(hoursExcluded[i-1]);
                                        hoursExcludedStart.push(hoursExcluded[i]);
                                        hoursExcludedEnd.push(hoursExcluded[i]);
                                    }

                                    break;
                                }
                                if(hoursExcluded[i] - hoursExcluded[i-1] !== 1) {
                                    hoursExcludedEnd.push(hoursExcluded[i-1]);
                                    hoursExcludedStart.push(hoursExcluded[i]);
                                }
                            }

                            for(i=0; i<hoursExcludedStart.length; i++) {
                                hoursExcludedInfo += hoursExcludedStart[i] + "-" +hoursExcludedEnd[i];
                                if(i === hoursExcludedStart.length-1) {
                                    hoursExcludedInfo += "";
                                }else {
                                    hoursExcludedInfo += ", ";
                                }
                            }

                        }
                        //get days excluded
                        var daysExcludedInfo = "";
                        var excludedDays = [];
                        if(daysOfWeek.length < 7){
                            daysExcludedInfo += self.tfDaysExcludedMsg;
                            for(i=0; i<7; i++) {
                                if($.inArray((i+1).toString(), daysOfWeek) === -1) {
                                    excludedDays.push(i);
                                }
                            }
                            for(i=0; i<excludedDays.length; i++) {
                                var dayName = self.longDaysOfWeek[excludedDays[i]];
                                daysExcludedInfo += dayName;
                                if(i === excludedDays.length-1) {
                                    daysExcludedInfo += "";
                                }else{
                                     daysExcludedInfo += ", ";
                                }
                            }
                        }
                        //get months excluded
                        var monthsExcludedInfo = "";
                        var excludedMonths = [];
                        if(monthsOfYear.length < 12) {
                            monthsExcludedInfo += self.tfMonthsExcludedMsg;
                            for(i=0; i<12; i++) {
                                if($.inArray((i+1).toString(), monthsOfYear) === -1) {
                                    excludedMonths.push(i);
                                }
                            }
                            for(i=0; i<excludedMonths.length; i++) {
                                var monthName = self.longMonths[excludedMonths[i]];
                                monthsExcludedInfo += monthName;
                                if(i === excludedMonths.length-1) {
                                    monthsExcludedInfo += "";
                                }else{
                                     monthsExcludedInfo += ", ";
                                }

                            }
                        }

                        var period1="", period2="";
                        if(((hoursExcludedInfo!=="") && (daysExcludedInfo!=="")) || ((hoursExcludedInfo!=="") && (monthsExcludedInfo!==""))) {
                            period1 = ". ";
                        }
                        if((daysExcludedInfo!=="") && (monthsExcludedInfo!=="")) {
                            period2 = ". ";
                        }
                        tfTooltip = hoursExcludedInfo + period1 + daysExcludedInfo + period2 + monthsExcludedInfo;
                        return tfTooltip;
                    }
                };
                                    
                if(params.timeFilterParams) {
                    self.timeFilterParams = params.timeFilterParams;
                    if(self.enableTimeFilter()) {
                        var timeFilterValue = self.timeFilterParams.hoursIncluded ? self.timeFilterParams.hoursIncluded : "0-23";
                        var hourOfDay = self.getHourOfDayInArray(timeFilterValue);
                        var daysChecked = self.timeFilterParams.daysIncluded ? self.timeFilterParams.daysIncluded : daysArray;
                        var monthsChecked = self.timeFilterParams.monthsIncluded ? self.timeFilterParams.monthsIncluded : monthsArray;
                        self.timeFilterInfo(self.generateTfTooltip(hourOfDay, daysChecked, monthsChecked));
                        self.tfInstance.timeFilterValue(timeFilterValue);
                        self.tfInstance.daysChecked(daysChecked);
                        self.tfInstance.monthsChecked(monthsChecked);
                    }
                }else {
                    self.timeFilterParams = null;
                }
                    
                self.tfParams = {
                    postbox: self.postbox,
                    randomId: self.randomId,
                    timeFilterParams: self.timeFilterParams,
                    tfChangedCallback: self.updateRange
                };
                
                function convertTimeToDesiredPrecision(timeStamp) {
                    var resTime;
                    if(!timeStamp) {
                        return;
                    }
                    timeStamp = newDateWithMilliseconds(timeStamp).getTime();
                    if(isNaN(parseInt(timeStamp))) {
                        return;
                    }
                    timeStamp = parseInt(timeStamp);
                    if(self.hideTimeSelection() === false) {
                        return timeStamp;
                    }else {
                        resTime = oj.IntlConverterUtils.dateToLocalIso(new Date(timeStamp));
                        resTime = oj.IntlConverterUtils.isoToLocalDate(resTime.slice(0, 10));
                    }
                    
                    return resTime.getTime();
                }
                
                function setDateTimeForRelativeTime(ctxEventTp, ctxEventStart, ctxEventEnd) {
                    var timeRange = null;
                    var parsedTimePeriod = null;
                    tp = self.timePeriodsNlsObject[ctxEventTp];
                    timeRange = ctxUtil.getStartEndTimeFromTimePeriod(ctxEventTp);
                    start = ctxEventStart ? new Date(ctxEventStart) : timeRange.start;
                    end = ctxEventEnd ? new Date(ctxEventEnd) : timeRange.end;
                    start = oj.IntlConverterUtils.dateToLocalIso(start);
                    end = oj.IntlConverterUtils.dateToLocalIso(end);
                    if (tp) { //For quick picks
                        ctxEventTp = convertTPIdForQuickPick(ctxEventTp, !params.timePeriodsSet);
                        parsedTimePeriod = ctxUtil.parseTimePeriodToUnitAndDuration(ctxEventTp);
//
                        if(parsedTimePeriod) {
                            self.lrCtrlVal("flexRelTimeCtrl");
                            self.flexRelTimeVal(parsedTimePeriod.duration);
                            self.flexRelTimeOpt([parsedTimePeriod.unit]);
                        }else {
                            self.lrCtrlVal("timeLevelCtrl");
                        }
                        self.selectByDrawer(true);
                        self.timePeriod(tp);
                        self.setTimePeriodChosen(self.timePeriod());
                    } else { //for flexible time period
                        parsedTimePeriod = ctxUtil.parseTimePeriodToUnitAndDuration(ctxEventTp);

                        self.lrCtrlVal("flexRelTimeCtrl");
                        if (parsedTimePeriod) {
                            self.flexRelTimeVal(parsedTimePeriod.duration);
                            self.flexRelTimeOpt([parsedTimePeriod.unit]);
                        }

                        if(!params.timePeriodsSet && ctxEventTp === "LAST_1_HOUR") {
                            tp = self.timePeriodLast60mins;
                            self.timePeriod(tp);
                            self.setTimePeriodChosen(self.timePeriod());
                        }else if(!params.timePeriodsSet && ctxEventTp === "LAST_1_WEEK") {
                            tp = self.timePeriodLast7days;
                            self.timePeriod(tp);
                            self.setTimePeriodChosen(self.timePeriod());
                        }else {
                            self.timePeriod(self.timePeriodCustom);
                            self.setTimePeriodChosen(self.timePeriod());
                        }
                    }

                    self.startDate(self.dateConverter2.format(start));
                    self.endDate(self.dateConverter2.format(end));

                    self.startTime(start.slice(10));
                    self.endTime(end.slice(10));
                }
                
                function setDateTimeForCustomTime(startTimeStamp, endTimeStamp) {
                    var start;
                    var end;
                    self.lrCtrlVal("timeLevelCtrl");
                    start = new Date(startTimeStamp);
                    end = new Date(endTimeStamp);
                    start = oj.IntlConverterUtils.dateToLocalIso(start);
                    end = oj.IntlConverterUtils.dateToLocalIso(end);

                    self.timePeriod(self.timePeriodCustom);

                    self.startDate(self.dateConverter2.format(start));
                    self.endDate(self.dateConverter2.format(end));

                    self.startTime(start.slice(10));
                    self.endTime(end.slice(10));
                }
                
                function callbackForOmcCtxChange(ctxChangeEvent) {
                    if (ctxChangeEvent && ctxChangeEvent.source === eventSourceTimeSelector) {
                        return;
                    }
                    var start;
                    var end;
                    var ctxStart = null;
                    var ctxEnd = null;
                    var tp;
                    var ctxTime;
                    var timeRange;
//                    var timeRange;
//                    var parsedTimePeriod;
                    //refresh time selector when time context is updated by ctxUtil setters
                    if(ctxChangeEvent && ctxChangeEvent.tag && ctxChangeEvent.tag === "EMAAS_OMC_GLOBAL_CONTEXT_UPDATED") {
                        if(ctxChangeEvent.contextName !== "All" && ctxChangeEvent.contextName !== "startTime" && ctxChangeEvent.contextName !== "endTime" 
                                && ctxChangeEvent.contextName !== "timePeriod" && ctxChangeEvent.contextName!=="startEndTime") {
                            return;
                        }
                        
                        if(!ctxChangeEvent.currentValue) {
                            return;
                        }
                        
                        console.log("**********OMC context change event is ");
                        console.log(ctxChangeEvent);
                        
                        if(self.hideTimeSelection() === false) {
                            start = oj.IntlConverterUtils.isoToLocalDate(self.startDateISO().slice(0, 10) + self.startTime());
                            end = oj.IntlConverterUtils.isoToLocalDate(self.endDateISO().slice(0, 10) + self.endTime());
                        }else {
                            start = oj.IntlConverterUtils.isoToLocalDate(self.startDateISO().slice(0, 10));
                            end = oj.IntlConverterUtils.isoToLocalDate(self.endDateISO().slice(0, 10));
                        }
                        start = start.getTime();
                        end = end.getTime();
                        if(self.lrCtrlVal() === "timeLevelCtrl") {
                            tp = self.getTimePeriodString(self.timePeriod());
                        }else if(self.lrCtrlVal() === "flexRelTimeCtrl") {
                            tp = ctxUtil.generateTimePeriodFromUnitAndDuration(self.flexRelTimeOpt()[0], self.flexRelTimeVal());
                        }
                        
                        if(ctxChangeEvent.contextName === "All") {
                            if(ctxChangeEvent.currentValue && ctxChangeEvent.currentValue.time) {
                                ctxTime = ctxChangeEvent.currentValue.time;
                                timeRange = {
                                    start: ctxTime.startTime,
                                    end: ctxTime.endTime
                                };
                                if(ctxTime.startTime && ctxTime.endTime && self.adjustLastX) {
                                    timeRange = self.adjustLastX(new Date(ctxTime.startTime), new Date(ctxTime.endTime));
                                }
                                if(ctxTime.timePeriod && (isValidFlexRelTimePeriod(ctxTime.timePeriod) || ctxTime.timePeriod === "LATEST")) {
                                    if(ctxTime.timePeriod !== tp
                                            || (timeRange.start && convertTimeToDesiredPrecision(timeRange.start) !== start)
                                            || (timeRange.end && convertTimeToDesiredPrecision(timeRange.end) !== end)) {
                                        setDateTimeForRelativeTime(ctxTime.timePeriod, timeRange.start, timeRange.end);
                                        setTimeout(function() {self.applyClick(false);}, 0);
                                    }
                                }else if(ctxTime.startTime && ctxTime.endTime && convertTimeToDesiredPrecision(ctxTime.startTime) !== start && convertTimeToDesiredPrecision(ctxTime.endTime) !== end){
                                    ctxStart = convertTimeToDesiredPrecision(ctxTime.startTime);
                                    ctxEnd = convertTimeToDesiredPrecision(ctxTime.endTime)
                                    setDateTimeForCustomTime(ctxStart, ctxEnd);
                                    setTimeout(function() {self.applyClick(false);}, 0);
                                }
                            }else {
                                return;
                            }
                        }else if(ctxChangeEvent.contextName === "timePeriod" && ctxChangeEvent.currentValue.timePeriod && (isValidFlexRelTimePeriod(ctxChangeEvent.currentValue.timePeriod) || ctxChangeEvent.currentValue.timePeriod === "LATEST")) {
                            timeRange = {
                                start: ctxChangeEvent.currentValue.startTime,
                                end: ctxChangeEvent.currentValue.endTime
                            };
                            if(timeRange.start && timeRange.end && self.adjustLastX) {
                                timeRange = self.adjustLastX(new Date(timeRange.start), new Date(timeRange.end));
                            }
                            if(ctxChangeEvent.currentValue.timePeriod !== tp
                                    || (timeRange.start && convertTimeToDesiredPrecision(timeRange.start) !== start)
                                    || (timeRange.end && convertTimeToDesiredPrecision(timeRange.end) !== end)) {
                                    setDateTimeForRelativeTime(ctxChangeEvent.currentValue.timePeriod, timeRange.start, timeRange.end);
                                    setTimeout(function() {self.applyClick(false);}, 0);
                            }
                        }else if(ctxChangeEvent.contextName === "startEndTime" && ctxChangeEvent.currentValue.startTime && ctxChangeEvent.currentValue.endTime && 
                                (convertTimeToDesiredPrecision(ctxChangeEvent.currentValue.startTime) < convertTimeToDesiredPrecision(ctxChangeEvent.currentValue.endTime)) && 
                                (convertTimeToDesiredPrecision(ctxChangeEvent.currentValue.startTime) !== start || convertTimeToDesiredPrecision(ctxChangeEvent.currentValue.endTime) !== end)) {
                            ctxStart = convertTimeToDesiredPrecision(ctxChangeEvent.currentValue.startTime);
                            ctxEnd = convertTimeToDesiredPrecision(ctxChangeEvent.currentValue.endTime)
                            setDateTimeForCustomTime(ctxStart, ctxEnd);
                            setTimeout(function() {self.applyClick(false);}, 0);
                        }else if(ctxChangeEvent.contextName === "startTime" && (ctxUtil.getTimePeriod() === ctxUtil.OMCTimeConstants.QUICK_PICK.CUSTOM) && ctxUtil.getEndTime() && 
                                (parseInt(ctxChangeEvent.currentValue)<ctxUtil.getEndTime())) {
                            setDateTimeForCustomTime(ctxChangeEvent.currentValue, ctxUtil.getEndTime());
                            setTimeout(function() {self.applyClick(false);}, 0);
                        }else if(ctxChangeEvent.contextName === "endTime" && (ctxUtil.getTimePeriod() === ctxUtil.OMCTimeConstants.QUICK_PICK.CUSTOM) && ctxUtil.getStartTime() && 
                                (ctxUtil.getStartTime()<parseInt(ctxChangeEvent.currentValue))) {
                            setDateTimeForCustomTime(ctxUtil.getStartTime(), ctxChangeEvent.currentValue);
                            setTimeout(function() {self.applyClick(false);}, 0);
                        }
                        
                    }
                }

                self.badgeTimeInfo = ko.observable();
                self.badgeMouseOverHandler = function (widget, event) {
                    var _time;
                    var _startTime;
                    var _endTime;
                    
                    self.badgeMsgTitle(msgUtil.formatMessage(nls.DATETIME_PICKER_BADGE_MESSAGE_TITLE, self.getTranslatedTimePeriod(self.badgeTimePeriod())));
                    
                    if(!self.badgeTimePeriod()) {
                        return;
                    }
                    
                    if(self.badgeTimePeriod() === ctxUtil.OMCTimeConstants.QUICK_PICK.CUSTOM) {
                        _time = {
                            start: oj.IntlConverterUtils.dateToLocalIso(newDateWithMilliseconds(ko.unwrap(params.defaultStartDateTime))),
                            end: oj.IntlConverterUtils.dateToLocalIso(newDateWithMilliseconds(ko.unwrap(params.defaultEndDateTime)))
                        };
                    }else {
                        _time = self.getTimeRangeForFlexRelTime(self.badgeTimePeriod());
                    }

                    _startTime = _time.start.slice(10);
                    _endTime = _time.end.slice(10);

                    var _timeDisplay = self.timeDisplay;    //change self.timeDisplay temporary to get right time rage info
                    self.timeDisplay = ko.observable("long");
                    var _timeInfoStr = self.getDateTimeInfo(_time.start.slice(0, 10), _time.end.slice(0, 10), _startTime, _endTime);
                    self.timeDisplay = _timeDisplay;

                    var _timeInfoNode = $("<div>" + _timeInfoStr + "</div>")[0];
                    self.badgeTimeInfo($(_timeInfoNode).text());

                    var popupContent = $('.badge-popup-message');
                    popupContent.ojPopup("close");
                    if (!popupContent.ojPopup("isOpen")) {
                        $(popupContent).ojPopup("open", $(".info-badge"),
                                {
                                    my: "end top", at: "left bottom"
                                });
                        setTimeout(function(){
                            self.badgeMouseOutHandler();
                        },4000);
                    }
                };
                self.badgeMouseOutHandler = function (widget, event) {
                    if ($($('.badge-popup-message')).ojPopup("isOpen")) {
                        $('.badge-popup-message').ojPopup("close");
                    }
                };

                self.overflowedLabelInfo = ko.observable(null);
                self.showOverFlowedLabelPopUp = ko.observable(false);
                self.labelMouseOverHandler = function() {
                    var labelEle = $(self.wrapperId + " .oj-select-chosen")[0];
                    if(!labelEle) {
                        return;
                    }

                    self.showOverFlowedLabelPopUp(true);
                    var overflowedLabel = $(labelEle).text();
                    if(!params.hideRangeLabel) {
                        overflowedLabel = overflowedLabel.substring(overflowedLabel.indexOf(":")+1);
                    }
                    self.overflowedLabelInfo(overflowedLabel);
                    //create an invisible ele to get the width of time range content
                    var ele = "<span id='labelContent' style='font-size:10px;'>"+overflowedLabel+"</span>";
                    $("body").append(ele);
                    var contentWidth = $("#labelContent").width();
                    var labelWidth = $(labelEle).width();
                    $("#labelContent").remove();
                    if(labelWidth < contentWidth) {
                        if(!$("#overflowedLabelInfo_"+self.randomId).ojPopup("isOpen")) {
                            $("#overflowedLabelInfo_"+self.randomId).ojPopup("open", $(labelEle),
                                {
                                    my: "end top", at: "right bottom"
                                }
                            );
                        }
                    }
                }

                self.labelMouseOutHandler = function() {
                    if($("#overflowedLabelInfo_"+self.randomId).ojPopup("isOpen")) {
                        $("#overflowedLabelInfo_"+self.randomId).ojPopup("close");
                    }
                }

                ctxUtil.subscribeOMCContextChangeEvent(callbackForOmcCtxChange);
                
                self.initialize();
            }
            return dateTimePickerViewModel;
        });
