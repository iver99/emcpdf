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
             * Build jet html for error message 
             * 
             * @param {type} summary
             * @param {type} detail
             * @returns {String}
             */
            function buildErrorMsgHtml(summary, detail) {
                    return '<div class="oj-messaging-inline-container omc-time-picker">\n\
                        <div class="oj-message oj-message-error">\n\
                            <span class="oj-component-icon oj-message-status-icon oj-message-error-icon" title="Error" role="img">\n\
                            </span>\n\
                            <span class="oj-message-content">\n\
                                <div class="oj-message-summary">' + summary +'</div>\n\
                                <div class="oj-message-detail">\n\
                                    <span>' + detail + '</span>\n\
                                </div>\n\
                            </span>\n\
                        </div>\n\
                    </div>';
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
                var quickPicks = ctxUtil.OMCTimeConstants.QUICK_PICK;
                var timeUnits = ctxUtil.OMCTimeConstants.TIME_UNIT;
                self.badgeTimePeriod = ko.observable();
                self.badgeMsgTitle = ko.observable();
                console.log("Initialize date time picker! The params are: ");
                if(ko.mapping && ko.mapping.toJS) {
                    console.log(ko.mapping.toJS(params));
                }else {
                    console.log(params);
                }

                var start, end, curStartDate, curEndDate;
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

                self.postbox = new ko.subscribable();
                self.postbox.subscribe(function(newValue) {
                    self.tfInstance = newValue;
                    self.showTimeFilterError(self.tfInstance.showTimeFilterError());
                }, null, "tfChanged");
                                
                /**
                *    Make input of timeperiod backward compatible for old time period format.
                *    The result is "LAST_X_UNIT"
                *    
                *    @param {type} timePeriod
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
                    return ctxUtil.isValidTimePeriod(timePeriod);
                }

                self.showPanel = ko.observable(false);
                self.showTimeFilterInfoPopUp = ko.observable(false);
                self.enableTimeFilter = ko.observable(false);
                self.tfInfoIndicatorVisible = ko.observable(false);
                self.timeFilterInfo = ko.observable();
                self.wrapperId = "#dateTimePicker_" + self.randomId;
                self.panelId = "#panel_" + self.randomId;
                self.pickerPanelId = "#pickerPanel_" + self.randomId;
                self.timeFilterId = "#timeFilter_" + self.randomId;

                var dateOption = {formatType: "date", dateFormat: "medium"};
                var inputDateOption = {year: 'numeric', month: '2-digit', day: '2-digit'};
                self.inputDateConverter = oj.Validation.converterFactory("dateTime").createConverter(inputDateOption);
                self.dateConverter = oj.Validation.converterFactory("dateTime").createConverter(dateOption);
                self.timeConverterMinute = oj.Validation.converterFactory("dateTime").createConverter({pattern: 'hh:mm a'});
                self.timeConverterMillisecond = oj.Validation.converterFactory("dateTime").createConverter({pattern: 'hh:mm:ss:SSS a'});
                self.timeConverter = ko.observable(self.timeConverterMinute);
                self.showTimeAtMillisecond = ko.observable(false);
                self.showLatestOnCustomPanel = ko.observable(false);

                self.timeIncrement = ko.observable("00:10:00:00");

                self.longMonths = oj.LocaleData.getMonthNames("wide");
                self.longDaysOfWeek = oj.LocaleData.getDayNames("wide");
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

                self.recentList = ko.observableArray([]);

                self.timePeriodsNlsObject = {};
                self.timePeriodsNlsObject[quickPicks.LAST_15_MINUTE] = self.timePeriodLast15mins;
                self.timePeriodsNlsObject[quickPicks.LAST_30_MINUTE] = self.timePeriodLast30mins;
                self.timePeriodsNlsObject[quickPicks.LAST_60_MINUTE] = self.timePeriodLast60mins;
                self.timePeriodsNlsObject[quickPicks.LAST_2_HOUR] = self.timePeriodLast2hours;
                self.timePeriodsNlsObject[quickPicks.LAST_4_HOUR] = self.timePeriodLast4hours;
                self.timePeriodsNlsObject[quickPicks.LAST_6_HOUR] = self.timePeriodLast6hours;
                self.timePeriodsNlsObject[quickPicks.LAST_8_HOUR] = self.timePeriodLast8hours;
                self.timePeriodsNlsObject[quickPicks.LAST_24_HOUR] = self.timePeriodLast24hours;
                self.timePeriodsNlsObject[quickPicks.LAST_1_DAY] = self.timePeriodLast1day;
                self.timePeriodsNlsObject[quickPicks.LAST_7_DAY] = self.timePeriodLast7days;
                self.timePeriodsNlsObject[quickPicks.LAST_14_DAY] = self.timePeriodLast14days;
                self.timePeriodsNlsObject[quickPicks.LAST_30_DAY] = self.timePeriodLast30days;
                self.timePeriodsNlsObject[quickPicks.LAST_90_DAY] = self.timePeriodLast90days;
                self.timePeriodsNlsObject[quickPicks.LAST_12_MONTH] = self.timePeriodLast12months;
                self.timePeriodsNlsObject[quickPicks.LAST_1_YEAR] = self.timePeriodLast1year;
                self.timePeriodsNlsObject[quickPicks.LATEST] = self.timePeriodLatest;
                self.timePeriodsNlsObject[quickPicks.CUSTOM] = self.timePeriodCustom;
                
                //initialize class assuming that dtpicker is on the left of page
                self.drawerChosen = ko.observable("leftDrawerChosen");
                self.timeFilterIconCss = ko.observable("float-right");

                self.hideTimeSelection = ko.observable(false);

                self.startDateError = ko.observable(0);
                self.endDateError = ko.observable(0);
                self.startTimeError = ko.observable(0);
                self.endTimeError = ko.observable(0);
                self.flexRelTimeValError = ko.observable(0);
                
                var timeInputErrorObj = {
                    "startDate": self.startDateError,
                    "endDate": self.endDateError,
                    "startTime": self.startTimeError,
                    "endTime": self.endTimeError
                };
                /**
                 * 1. Set given input box to error box and set error message
                 * 2. Remove error border and error message from other input boxes
                 * 3. When "inputId" is set to null, clear error border and message from all input boxes
                 * 
                 * @param {type} inputId
                 * @param {type} msgDetail
                 * @returns {undefined}
                 */
                function setTimeInputError(inputId, msgDetail) {
                    var eleId = "#" + inputId + "_" + self.randomId;
                    if($(eleId) && !$(eleId).hasClass("oj-invalid")) {
                        $(eleId).addClass('oj-invalid'); //Use jet class to set red border
                        //add error message for date/time input box
                        $(eleId + " .oj-inputdatetime-input-container").after(buildErrorMsgHtml(self.errorMsg, msgDetail));
                    }
                    for(var i in timeInputErrorObj) {
                       if(i !== inputId) {
                           timeInputErrorObj[i](0);
                           //remove error message of corrsponding date/time input box
                           $("#" + i + "_" + self.randomId + " .oj-messaging-inline-container.omc-time-picker").remove();
                       }
                    }
                    
                }
                
                /**
                 * Remove error border and error messages from given input box
                 * 
                 * @param {type} inputId
                 * @returns {undefined}
                 */
                function removeTimeInputError(inputId) {
                    var eleId = "#" + inputId + "_" + self.randomId;
                    if($(eleId) && $(eleId).hasClass("oj-invalid")) {
                        $(eleId).removeClass("oj-invalid");
                        $(eleId + " .oj-messaging-inline-container.omc-time-picker").remove();
                    }
                }

                /**
                 * 0: No error
                 * 1: Start is later than end error
                 * 2: Selected time range exceed "customWindowLimit" Error
                 */
                self.startDateError.subscribe(function(value) {
                    if(value === 0) {
                        removeTimeInputError("startDate");
                    }else if(value === 1) {
                        setTimeInputError("startDate", nls.DATETIME_PICKER_TIME_VALIDATE_ERROR_MSG);
                    }else if(value === 2) {
                        setTimeInputError("startDate", self.beyondWindowLimitErrorMsg);
                    }
                });
                
                self.endDateError.subscribe(function(value) {
                    if(value === 0) {
                        removeTimeInputError("endDate");
                    }else if(value === 1) {
                        setTimeInputError("endDate", nls.DATETIME_PICKER_TIME_VALIDATE_ERROR_MSG);
                    }else if(value === 2) {
                        setTimeInputError("endDate", self.beyondWindowLimitErrorMsg);
                    }
                });
                
                self.startTimeError.subscribe(function(value) {
                    if(value === 0) {
                        removeTimeInputError("startTime");
                    }else if(value === 1) {
                        setTimeInputError("startTime", nls.DATETIME_PICKER_TIME_VALIDATE_ERROR_MSG);
                    }else if(value === 2) {
                        setTimeInputError("startTime", self.beyondWindowLimitErrorMsg);
                    }
                });
                
                self.endTimeError.subscribe(function(value) {
                    if(value === 0) {
                        removeTimeInputError("endTime");
                    }else if(value === 1) {
                        setTimeInputError("endTime", nls.DATETIME_PICKER_TIME_VALIDATE_ERROR_MSG);
                    }else if(value === 2) {
                        setTimeInputError("endTime", self.beyondWindowLimitErrorMsg);
                    }else if(value === 3) {
                        setTimeInputError("endTime", nls.DATETIME_PICKER_TIME_VALIDATE_FUTURE_ERROR_MSG);
                    }
                });
                
                self.flexRelTimeValError.subscribe(function(value) {
                    var eleId = "#lastNumber_" + self.randomId;
                    if(value === 0) {
                        $(eleId).removeClass("oj-invalid");
                        $(eleId + " .oj-messaging-inline-container.omc-time-picker").remove();
                    }else if(value === 1) {
                        //show Jet's own validation error message
                    }
                    if(value === 2) {
                        //show custom error message of "beyond window limit"
                        if($(eleId) && !$(eleId).hasClass("oj-invalid")) {
                            $(eleId).addClass("oj-invalid");
                            $(eleId + " .oj-inputnumber-wrapper").after(buildErrorMsgHtml(self.errorMsg, self.beyondWindowLimitErrorMsg));
                        }
                    }
                });

                self.applyButtonDisable = ko.computed(function() {
                    var applyButtonDisable = self.startDateError() || self.endDateError() || self.startTimeError() || self.endTimeError() ||
                            self.showTimeFilterError() || self.flexRelTimeValError();
                    return applyButtonDisable;
                }, self);
                
//                self.applyButtonDisable.subscribe(function(value) {
//                    if(value > 0) {
//                        $(self.pickerPanelId + " .buttons").addClass("apply-disabled");
//                    }else {
//                        $(self.pickerPanelId + " .buttons").removeClass("apply-disabled");
//                    }
//                });
                
                self.lrCtrlVal.subscribe(function(value) {
                    if(value === "flexRelTimeCtrl") {
                        var num = self.flexRelTimeVal();
                        var opt = self.flexRelTimeOpt()[0];
                        self.setFlexRelTime(num, opt);
                        self.setTimePeriodChosen(quickPicks.CUSTOM);
                        customClick(1);
                    }else if(value === "latestOnCustom"){
                        var event = {};
                        event.extended = quickPicks.LATEST;
                        event.target = {};
                        self.chooseTimePeriod(undefined, event);
                        self.setTimePeriodChosen(quickPicks.CUSTOM);
                    }else {
                        self.setTimePeriodChosen(quickPicks.CUSTOM);
                        customClick(1);
                    }
                });

                self.timePeriodObject = ko.observable();

                self.errorMsg = nls.DATETIME_PICKER_ERROR;
                self.beyondWindowLimitErrorMsg = nls.DATETIME_PICKER_BEYOND_WINDOW_LIMIT_ERROR_MSG;
                self.flexRelTimeValErrorMsg = nls.DATETIME_PICKER_FLEX_REL_TIME_VALUE_ERROR_MSG;
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
                    var calendarPanelWidth = $(self.pickerPanelId).width() ? $(self.pickerPanelId).width() : 567;
                    var tfPanelWidth = $(self.timeFilterId).width() ? $(self.timeFilterId).width() : 567;
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
                    self.timeFilterIconCss("float-right");
                    //the position of panel popup relative to dropdown button
                    self.panelPosition = {"my": "start top+16", "at": "start  bottom", "collision": "none", "of": "#dropDown_"+self.randomId};
                    self.pickerPanelPosition = {"my": "start top", "at": "end top", "collision": "none", "of": "#drawers_"+self.randomId};
                    self.timeFilterPosition = {"my": "start center", "at": "end center", "collision": "none"};
                };

                self.setDtpickerPositionToRight = function() {
                    self.timeFilterIconCss("float-left");
                    //the position of panel popup relative to dropdown button
                    self.panelPosition = {"my": "end top+16", "at": "end  bottom", "collision": "none", "of": "#dropDown_"+self.randomId};
                    self.pickerPanelPosition = {"my": "end top", "at": "start top", "collision": "none", "of": "#drawers_"+self.randomId};
                    self.timeFilterPosition = {"my": "end center", "at": "start center", "collision": "none"};
                };

                self.setTimePeriodChosen = function(timePeriodId) {
                    $(self.panelId + " a").each(function() {
                        var tpEle = $(this);
                        if(tpEle.attr("data-tp-id") !== timePeriodId) {
                            if(tpEle.hasClass("drawerChosen")) {
                                tpEle.removeClass("drawerChosen");
                            }
                        }else {
                            if(!tpEle.hasClass("drawerChosen")) {
                                tpEle.addClass("drawerChosen");
                            }
                        }
                    });
                };

                self.setTimePeriodsNotToShow = function(timePeriodsId) {
                    if(!timePeriodsId) {
                        timePeriodsId = [];
                    }
                    //hide "LAST_8_HOUR", "LAST_24_HOUR", "LAST_12_MONTH" if it is not long/short term
                    if(!params.timePeriodsSet) {
                        timePeriodsId.push(quickPicks.LAST_8_HOUR);
                        timePeriodsId.push(quickPicks.LAST_24_HOUR);
                        timePeriodsId.push(quickPicks.LAST_12_MONTH);
                    }
                    if(params.hideTimeSelection && params.hideTimeSelection === true && !params.timePeriodsSet) {
                        timePeriodsId.push(quickPicks.LAST_15_MINUTE);
                        timePeriodsId.push(quickPicks.LAST_30_MINUTE);
                        timePeriodsId.push(quickPicks.LAST_60_MINUTE);
                        timePeriodsId.push(quickPicks.LAST_2_HOUR);
                        timePeriodsId.push(quickPicks.LAST_4_HOUR);
                        timePeriodsId.push(quickPicks.LAST_6_HOUR);
                    }
                    $(self.panelId + " a").each(function() {
                        var tpEle = $(this);
                        var tpId = tpEle.attr("data-tp-id");
                        if($.inArray(tpId, timePeriodsId) >= 0) {
                            if(!tpEle.hasClass("drawerNotToShow")) {
                                tpEle.addClass("drawerNotToShow");
                            }
                        }else {
                            if(tpId === "RECENT") {
                                if(self.recentList().length > 0) {
                                    tpEle.removeClass("drawerNotToShow");
                                }else {
                                    tpEle.addClass("drawerNotToShow");
                                }
                            }else {
                                if(tpEle.hasClass("drawerNotToShow")) {
                                    tpEle.removeClass("drawerNotToShow");
                                }
                            }
                        }
                    });
                }

                self.getParam = function(param) {
                    var p = ko.isObservable(param) ? param() : param;
                    return p;
                };

                if(params.timePeriodsSet){
                    if(params.timePeriodsSet === self.timePeriodSetShortTerm){
                        params.timePeriodsNotToShow = [quickPicks.LAST_2_HOUR,
                                                    quickPicks.LAST_4_HOUR,
                                                    quickPicks.LAST_6_HOUR,
                                                    quickPicks.LAST_1_DAY,
                                                    quickPicks.LAST_30_DAY,
                                                    quickPicks.LAST_90_DAY,
                                                    quickPicks.LAST_12_MONTH,
                                                    quickPicks.LAST_1_YEAR,
                                                    quickPicks.LATEST];
                    }else if(params.timePeriodsSet === self.timePeriodSetLongTerm){
                        params.timePeriodsNotToShow = [quickPicks.LAST_15_MINUTE,
                                                    quickPicks.LAST_30_MINUTE,
                                                    quickPicks.LAST_60_MINUTE,
                                                    quickPicks.LAST_2_HOUR,
                                                    quickPicks.LAST_4_HOUR,
                                                    quickPicks.LAST_6_HOUR,
                                                    quickPicks.LAST_8_HOUR,
                                                    quickPicks.LAST_1_DAY,
                                                    quickPicks.LAST_1_YEAR,
                                                    quickPicks.LATEST];
                    }
                }

                if(self.getParam(params.timePeriod)) {
                    if(ko.isObservable(params.timePeriod)) {
                        self.timePeriodPreset = ko.computed(function() {
                            return formalizeTimePeriod(params.timePeriod());
                        }, self);
                        params.timePeriod.subscribe(function(value) {
                            if(self.timePeriodsNlsObject[formalizeTimePeriod(value)] || isValidFlexRelTimePeriod(value)) {
                                self.initialize && self.initialize();                                
                            }else {
                                throw new Error("The config param - "  + value + " for time period is invalid!");
                            }
                        });
                    }else {
                        self.timePeriodPreset = formalizeTimePeriod(params.timePeriod);
                    }

                    if(self.getParam(self.timePeriodPreset) || isValidFlexRelTimePeriod(self.getParam(params.timePeriod))) { //check if it is valid time period
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
                            var tpId = formalizeTimePeriod(params.timePeriodsNotToShow[i]);
                            self.timePeriodsNotToShow.push(tpId);
                        }
                    }else if(ko.isObservable(params.timePeriodsNotToShow)) {
                        self.timePeriodsNotToShow = ko.computed(function() {
                            var tmp = [];
                            var i, j;
                            var tpId;
                            var l = params.timePeriodsNotToShow().length;
                            for(i=0; i<l; i++) {
                                tpId = formalizeTimePeriod(params.timePeriodsNotToShow()[i]);
                                tmp.push(tpId);
                                //remove time periods from "Recently Used" list
                                self.recentList.remove(function(data) {return data.timePeriod === tpId});
                            }
                            return tmp;
                        }, self);
                        self.timePeriodsNotToShow.subscribe(function(value) {
                            var tpId = self.timePeriod();
                            if($.inArray(tpId, value) >= 0) {
                                customClick(0);
                                self.dateTimeInfo(self.getDateTimeInfo(self.startDateISO().slice(0, 10), self.endDateISO().slice(0, 10), self.startTime(), self.endTime(), self.timePeriod()));
                            }
                        });
                    }
                }
                
                self.setAllTimePeriodsEnabled = function() {
                    $(self.panelId + " a").each(function() {
                        var tpEle = $(this);
                        if(tpEle.hasClass("drawerDisabled")) {
                            tpEle.removeClass("drawerDisabled");
                        }
                    });
                };
                
                self.setTimePeriodsDisabled = function() {
                    if(params.timePeriodsSet && params.timePeriodsToBeDisabled) {
                        var timePeriodsToBeDiasabled = ko.unwrap(params.timePeriodsToBeDisabled);
                        self.setAllTimePeriodsEnabled();
                        $(self.panelId + " a").each(function() {
                            var tpEle = $(this);
                            var tpId = tpEle.attr("data-tp-id");
                            if($.inArray(tpId, timePeriodsToBeDiasabled) > -1) {
                                if(!tpEle.hasClass("drawerDisabled")) {
                                    tpEle.addClass("drawerDisabled");
                                }
                                self.recentList.remove(function(data) {return data.timePeriod === tpId});
                            }
                        });
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
                };
                
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

                //the max timestamp of how far the user can pick the date from, expressed as milliseconds
                if(params.customTimeBack && params.customTimeBack>0) {
                    self.customTimeBack = params.customTimeBack;
                }

                if(params.hideTimeSelection && params.hideTimeSelection === true) {
                    self.hideTimeSelection(true);
                }

                if(params.enableTimeFilter && params.enableTimeFilter === true) {
                    self.enableTimeFilter(true);
                }
                
                if(params.timePeriodsSet){
                    if(params.timePeriodsSet === self.timePeriodSetShortTerm){
                        self.defaultTimePeriod = ko.observable(quickPicks.LAST_60_MINUTE);
                    }else if(params.timePeriodsSet === self.timePeriodSetLongTerm){
                        self.defaultTimePeriod = ko.observable(quickPicks.LAST_30_DAY);
                    }
                }else{
                    self.defaultTimePeriod = ko.observable(quickPicks.LAST_15_MINUTE);
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
                    tmp[quickPicks.LAST_15_MINUTE] = [0, 15 * 60 * 1000];
                    tmp[quickPicks.LAST_30_MINUTE] = [1, 30 * 60 * 1000];
                    tmp[quickPicks.LAST_60_MINUTE] = [2, 60 * 60 * 1000];
                    tmp[quickPicks.LAST_2_HOUR] = [3, 2 * 60 * 60 * 1000];
                    tmp[quickPicks.LAST_4_HOUR] = [4, 4 * 60 * 60 * 1000];
                    tmp[quickPicks.LAST_6_HOUR] = [5, 6 * 60 * 60 * 1000];
                    tmp[quickPicks.LAST_8_HOUR] = [12, 8 * 60 * 60 * 1000];
                    tmp[quickPicks.LAST_24_HOUR] = [13, 24 * 60 * 60 * 1000];
                    tmp[quickPicks.LAST_1_DAY] = [6, 24 * 60 * 60 * 1000];
                    tmp[quickPicks.LAST_7_DAY] = [7, 7 * 24 * 60 * 60 * 1000];
                    tmp[quickPicks.LAST_14_DAY] = [8, 14 * 24 * 60 * 60 * 1000];
                    tmp[quickPicks.LAST_30_DAY] = [9, 30 * 24 * 60 * 60 * 1000];
                    tmp[quickPicks.LAST_90_DAY] = [10, 90 * 24 * 60 * 60 * 1000];
                    tmp[quickPicks.LAST_12_MONTH] = [14, 365 * 24 * 60 * 60 * 1000];
                    tmp[quickPicks.LAST_1_YEAR] = [11, 365 * 24 * 60 * 60 * 1000]; //do not use this to calculate last a year
                    return tmp;
                }, self);

                self.startDateISO = ko.observable();
                self.endDateISO = ko.observable();
                
                //Limit min and max value for start date and end date
                self.startDateMin = ko.observable(null);
                if(self.customTimeBack) {
                    self.startDateMin = ko.observable(oj.IntlConverterUtils.dateToLocalIso(new Date(new Date() - self.customTimeBack)).slice(0, 10));
                }

                self.endDateMin = ko.computed(function() {
                    var _startDate = oj.IntlConverterUtils.isoToLocalDate(self.startDateISO());
                    var _startDateMin = oj.IntlConverterUtils.isoToLocalDate(self.startDateMin());
                    var _endDateMin = Math.max(new Date(_startDate), new Date(_startDateMin));
                    return oj.IntlConverterUtils.dateToLocalIso(new Date(_endDateMin)).slice(0, 10);
                });
                self.endDateMax = ko.observable(oj.IntlConverterUtils.dateToLocalIso(new Date()).slice(0, 10));
                
                self.startTime = ko.observable("T00:00:00");
                self.endTime = ko.observable("T00:00:00");
                
                self.rawStartDate = ko.observable();
                self.rawStartTime = ko.observable();
                self.rawEndDate = ko.observable();
                self.rawEndTime = ko.observable();
                
                self.timePeriod = ko.observable();
                self.dateTimeInfo = ko.observable();
                self.selectByDrawer = ko.observable(false);
                self.showRightPanel = ko.observable(false);

                self.lastStartDateISO = ko.observable();
                self.lastEndDateISO = ko.observable();
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
                        return "279px";
                    }else {
                        return "auto";
                    }
                }, self);

                self.setTimePeriodToLastX = function(timePeriodId, start, end, shouldCallAdjustLastX) {
                    self.timePeriod(timePeriodId);
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
                
                self.isFormatError = function() {
                    if(self.startDateError() === 3 || self.startTimeError() === 3 || self.endDateError() === 3 || self.endTimeError() === 3) {
                        //if error meassage summary contains the string in input box. If it is, then it's a format error
                        var eles = $(".oj-message-error .oj-message-summary");
                        for(var i=0; i<eles.length; i++) {
                            var ele = $(eles[i]);
                            if(ele.text().search(self.rawStartDate()) > 0 || ele.text().search(self.rawStartTime()) > 0 
                                    || ele.text().search(self.rawEndDate()) > 0 || ele.text().search(self.rawEndTime()) > 0) {
                                return true;
                            }
                        }
                        return false;
                    }else {
                        return false;
                    }
                };
                
//                self.isStartLaterThanEnd = function(start, end) {
//                    if(start > end) {
//                        return true;
//                    }else {
//                        return false;
//                    }
//                };
                
                self.isCustomBeyondWindowLimit = function() {
                    if(!self.customWindowLimit) {
                        return false;
                    }
                    var start = self.startDateISO() + self.startTime();
                    var end = self.endDateISO() + self.endTime();
                    var timeDiff = new Date(end) - new Date(start);
                    if(timeDiff > self.customWindowLimit) {
                        return true;
                    }else {
                        return false;
                    }
                };
                
                self.isEndTimeLaterThanCurrentTime = function() {
                    var end = self.endDateISO() + self.endTime();
                    if(new Date(end) > new Date()) {
                        return true;
                    }else {
                        return false;
                    }
                }
                
                self.isStartLaterThanEnd = function() {
//                    var start = self.startDateISO() + self.startTime();
//                    var end = self.endDateISO() + self.endTime();
                    //rawValue of JET inputDate/inputTime is not latest value if user SELECT date/time instead of type them directly
                    //value doesn't get the latest value if users' inputs doesn't pass JET converter and validator
                    //So using rawValue/value doesn't get latest value in input boxes, Here we use jquery to get the value
                    var startDate = $("#inputStartDate_"+self.randomId).val();
                    var endDate = $("#inputEndDate_"+self.randomId).val();
                    var startTime = $("#inputStartTime_"+self.randomId).val();
                    var endTime = $("#inputEndTime_"+self.randomId).val();
                    var start = self.inputDateConverter.parse(startDate) + self.timeConverter().parse(startTime);
                    var end = self.inputDateConverter.parse(endDate) + self.timeConverter().parse(endTime);
                    if(new Date(start) > new Date(end)) {
                        return true;
                    }else {
                        return false;
                    }
                };
                
                self.startDateChanged = function(event, data) {
                    if(data.option === "messagesShown" && isArray(data.value) && data.value.length>0) {
                        self.startDateError(3); //Jet's own error message
                        return;
                    }
                    if(data.option !== "value" && data.option !== "disabled") {
                        return;
                    }
                    if(data.option === "disabled") {
                        if(data.value === true) {
                            //remove error message
                            self.startDateError(0);
                            return;
                        }else {
                            self.flexRelTimeValError(0);
                            //set flexRelTimeVal to its previous value doesn't work, so set it like below to make sure the value is changed
                            self.flexRelTimeVal(self.flexRelTimeVal() + 1);
                            self.flexRelTimeVal(self.flexRelTimeVal() - 1);
                        }
                    }
                    if(self.lrCtrlVal() !== "timeLevelCtrl") {
                        self.startDateError(0);
                        return;
                    }
                    if(self.isFormatError() === true) {
                        return;
                    }
                    if(self.isStartLaterThanEnd() === true) {
                        self.startDateError(1);
                    }else if(self.isCustomBeyondWindowLimit() === true) {
                        self.startDateError(2);
                    }else {
                        setTimeInputError(null);
                    }
                };
                self.endDateChanged = function(event, data) {
                    if(data.option === "messagesShown" && isArray(data.value) && data.value.length>0) {
                        self.endDateError(3); //Jet's own error message
                        return;
                    }
                    if(data.option !== "value") {
                        return;
                    }
                    if(self.lrCtrlVal() !== "timeLevelCtrl") {
                        return;
                    }
                    if(self.isFormatError() === true) {
                        return;
                    }
                    if(self.isStartLaterThanEnd() === true) {
                        self.endDateError(1);
                    }else if(self.isCustomBeyondWindowLimit() === true) {
                        self.endDateError(2);
                    }else {
                        setTimeInputError(null);
                    }
                };
                self.startTimeChanged = function(event, data) {
                    if(data.option === "messagesShown") {
                        self.startTimeError(3); //Jet's own error message
                        return;
                    }
                    if(data.option !== "value") {
                        return;
                    }
                    if(self.lrCtrlVal() !== "timeLevelCtrl") {
                        return;
                    }
                    if(self.isFormatError() === true) {
                        return;
                    }
                    if(self.isStartLaterThanEnd() === true) {
                        self.startTimeError(1);
                    }else if(self.isEndTimeLaterThanCurrentTime() === true) {
                        self.endTimeError(3);
                    }else if(self.isCustomBeyondWindowLimit() === true) {
                        self.startTimeError(2);
                    }else {
                        setTimeInputError(null);
                    }
                };
                self.endTimeChanged = function(event, data) {
                    if(data.option === "messagesShown") {
                        self.endTimeError(3); //Jet's own error message
                        return;
                    }
                    if(data.option !== "value") {
                        return;
                    }
                    if(self.lrCtrlVal() !== "timeLevelCtrl") {
                        return;
                    }
                    if(self.isFormatError() === true) {
                        return;
                    }
                    if(self.isStartLaterThanEnd() === true) {
                        self.endTimeError(1);
                    }else if(self.isEndTimeLaterThanCurrentTime() === true) {
                        self.endTimeError(3);
                    }else if(self.isCustomBeyondWindowLimit() === true) {
                        self.endTimeError(2);
                    }else {
                        setTimeInputError(null);
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

                self.isRelTimePeriodLessThan1day = function(relTimePeriodId) {
                    var parsedTimePeriod = ctxUtil.parseTimePeriodToUnitAndDuration(relTimePeriodId);
                    if(!parsedTimePeriod) {
                        return false;
                    }
                    var val = parsedTimePeriod.duration;
                    var opt = parsedTimePeriod.unit;
                    if(opt === timeUnits.SECOND) {
                        if(val < 24*60*60) {
                            return true;
                        }
                    }else if(opt === timeUnits.MINUTE) {
                        if(val < 24*60) {
                            return true;
                        }
                    }else if(opt === timeUnits.HOUR) {
                        if(val < 24) {
                            return true;
                        }
                    }
                    return false;
                };

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
                self.getDateTimeInfo = function(startDate, endDate, startTime, endTime, timePeriodId) {
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
                        if(tmpStart.getTimezoneOffset() !== tmpEnd.getTimezoneOffset() && self.lrCtrlVal() === "flexRelTimeCtrl" && self.isRelTimePeriodLessThan1day(timePeriodId)) {
                            start += " (" + self.getGMTTimezone(tmpStart) + ")";
                            end += " (" + self.getGMTTimezone(tmpEnd) + ")";
                        }
                    }else {
                        //hide hyphen when time range is "Today-Today"/"Yestarday-Yesterday"
                        hyphenDisplay = end ? hyphenDisplay : "display: none;";
                    }

                    //For "Latest" quick pick
                    if(timePeriodId === quickPicks.LATEST) {
                        if(self.getParam(self.timeDisplay) === "short") {
                            dateTimeInfo = "<span class='show-individual-time-span1-short'>" + self.timePeriodsNlsObject[timePeriodId] + "</span>";
                        }else{
                            dateTimeInfo = "<span class='show-individual-time-span1-latest'>" + self.timePeriodsNlsObject[timePeriodId] + "</span>";
                        }
                        return dateTimeInfo;
                    }


                    //For "Custom" time period, including custom relative time and custom absolute time
                    if(timePeriodId === quickPicks.CUSTOM) {
                        if(self.lrCtrlVal() === "flexRelTimeCtrl") { //For custom relative time
                            if(self.getParam(self.timeDisplay) === "short") {
                                dateTimeInfo = "<span class='show-individual-time-span1-short'>" + self.getTranslatedFlexTimePeriod(self.flexRelTimeVal(), self.flexRelTimeOpt()[0])  + "</span>";
                            }else {
                                dateTimeInfo = "<span class='show-individual-time-span1'>" + self.getTranslatedFlexTimePeriod(self.flexRelTimeVal(), self.flexRelTimeOpt()[0]) + ": </span>";
                                dateTimeInfo += "<span class='time-range-overflow'>" + start + "<span style='font-weight:bold; " + hyphenDisplay + "'>" + " - </span>" + end + "</span>";
                            }
                        }else if(self.lrCtrlVal() === "latestOnCustom") {
                            if(self.getParam(self.timeDisplay) === "short") {
                                dateTimeInfo = "<span class='show-individual-time-span1-short'>" + self.timePeriodLatest + "</span>";
                            }else{
                                dateTimeInfo = "<span class='show-individual-time-span1-latest'>" + self.timePeriodLatest + "</span>";
                            }
                        }else {
                        
                            if(self.getParam(self.timeDisplay) === "short") {
                                dateTimeInfo = start + "<span class='show-individual-time-span1-short'" + hyphenDisplay + "'> - </span>" + end;
                            }else {
                                dateTimeInfo = "<span class='show-individual-time-span1'>" + self.timePeriodsNlsObject[timePeriodId] + ": </span>" +
                                    "<span class='time-range-overflow'>" + 
                                    start +
                                    "<span style='font-weight:bold; " + hyphenDisplay + "'>" + " - </span>" +
                                    end +
                                    "</span>";
                            }
                        }
                        return dateTimeInfo;
                    }
                    
                    //generate dateTimeInfo for "Recent" List
                    if(!timePeriodId) {
                        dateTimeInfo = start + "<span style='font-weight:bold; " + hyphenDisplay + "'>" + " - </span>" + end;
                        return dateTimeInfo;
                    }

                    if(self.getParam(self.timeDisplay) === "short") {
                        dateTimeInfo = "<span class='show-individual-time-span1-short'>" + self.timePeriodsNlsObject[timePeriodId] + "</span>";
                    }else {
                        dateTimeInfo = "<span class='show-individual-time-span1'>" + self.timePeriodsNlsObject[timePeriodId] + ": </span>";
                        dateTimeInfo += "<span class='time-range-overflow'>" + start + "<span style='font-weight:bold; " + hyphenDisplay + "'>" + " - </span>" + end + "</span>";
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
                    
                    if(recent.timePeriod === quickPicks.CUSTOM) { // for custom time range
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
                };
                
                /**
                 * Initialize time selector to quick picks like "LAST_x_UNIT", or "LATEST"
                 * @param {type} timePeriodId
                 * @param {type} curDate
                 * @returns {start: <start in Date>, end: <end in Date>}
                 */
                self.initToRelTimePeriod = function(timePeriodId, curDate, convertTPId) {
                    var start, end, parsedTp, range;
                    var tpNotToShow = self.getParam(self.timePeriodsNotToShow);
                    if (timePeriodId === quickPicks.LATEST) {
                        start = curDate;
                        end = curDate;
                        if (self.showLatestOnCustomPanel()) {
                            self.lrCtrlVal("latestOnCustom");
                        } else {
                            self.setTimePeriodToLastX(timePeriodId, start, end, 1);
                        }
                    } else {
                        self.lrCtrlVal("flexRelTimeCtrl");
                        parsedTp = ctxUtil.parseTimePeriodToUnitAndDuration(convertTPIdForQuickPick(timePeriodId, !params.timePeriodsSet));
                        var tmp = self.getTimeRangeForRelTimePeriod(timePeriodId);
                        start = oj.IntlConverterUtils.isoToLocalDate(tmp.start);
                        end = oj.IntlConverterUtils.isoToLocalDate(tmp.end);

                        if (parsedTp) {
                            self.flexRelTimeVal(parsedTp.duration);
                            self.flexRelTimeOpt([parsedTp.unit]);
                        }
                        
                        if(convertTPId) {
                            if (!params.timePeriodsSet && (timePeriodId === "LAST_1_WEEK" || timePeriodId === "LAST_1_HOUR")) {
                                if(timePeriodId === "LAST_1_WEEK") {
                                    timePeriodId = quickPicks.LAST_7_DAY;
                                }else {
                                    timePeriodId = quickPicks.LAST_60_MINUTE;
                                }
                                
                                if ($.inArray(timePeriodId, tpNotToShow) === -1) {
                                    self.setTimePeriodToLastX(timePeriodId, start, end, 1);
                                } else {
                                    customClick(0);
                                }
                            } else {
                                self.timePeriod(quickPicks.CUSTOM);
                            }
                        }else {
                            if ($.inArray(timePeriodId, tpNotToShow) === -1) {
                                self.setTimePeriodToLastX(timePeriodId, start, end, 1);
                            } else {
                                customClick(0);
                            }
                        }
                    }
                    
                    return {
                        start: start,
                        end: end
                    }
                }
                
                var curDate;
                self.init = true;
                self.initialize = function() {
                    curDate = new Date();
                    start = newDateWithMilliseconds(curDate - 15 * 60 * 1000);
                    end = new Date();
                    var tpNotToShow = self.getParam(self.timePeriodsNotToShow);
                    var sdt, edt, range, tp, tpId;

                    if(self.isTimePeriodPreset) {
                        /*** start: timePeriod Param is set ***/
                        tpId = self.getParam(self.timePeriodPreset);
                        if(self.getParam(self.timePeriodPreset) && self.timePeriodsNlsObject[ko.unwrap(self.timePeriodPreset)]) {
                            if(tpId === quickPicks.CUSTOM) {
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
                            } else { //For quick picks
                                range = self.initToRelTimePeriod(tpId, curDate);
                                start = range.start;
                                end = range.end;
                            }
                        }else { //For flex rel time
                            range = self.initToRelTimePeriod(tpId, curDate, true);
                            start = range.start;
                            end = range.end;
                        }
                        /*** end: timePeriod Param is set ***/
                    }else {
                        if(isValidDateInput(self.getParam(self.startDateTime)) && isValidDateInput(self.getParam(self.endDateTime))) {
                            /*** start: startDateTime & endDateTime Params are set ***/
                            curDate = new Date();
                            //users input start date and end date
                            sdt = self.getParam(self.startDateTime);
                            edt = self.getParam(self.endDateTime);
                            start = newDateWithMilliseconds(sdt);
                            end = newDateWithMilliseconds(edt);
                            console.log("Param endDateTime is " + end);
                            console.log("Initializing time is " + curDate);
                            self.lrCtrlVal("timeLevelCtrl");
                            customClick(0);
                            /*** start: startDateTime & endDateTime Params are set ***/
                        } else if (!isValidDateInput(self.getParam(self.startDateTime)) && isValidDateInput(self.getParam(self.endDateTime))) {
                            /*** start: endDateTime Param is set ***/
                            if($.inArray(quickPicks.LAST_15_MINUTE, tpNotToShow)<0) {
                                range = self.setTimePeriodToLastX(quickPicks.LAST_15_MINUTE, start, end, 0);
                                start = range.start;
                                end = range.end;
                            }else {
                                customClick(0);
                            }
                            //print warning...
                            oj.Logger.warn("The user just input end time");
                            /*** end: endDateTime Param is set ***/
                        } else if (isValidDateInput(self.getParam(self.startDateTime)) && !isValidDateInput(self.getParam(self.endDateTime))) {
                            /*** start: startDateTime Param is set ***/
                            self.lrCtrlVal("timeLevelCtrl");
                            customClick(0);
                            sdt = self.getParam(self.startDateTime);
                            start = newDateWithMilliseconds(sdt);
                            end = new Date();
                            /*** end: startDateTime Param is set ***/
                        } else if(omcContext.time && omcContext.time.timePeriod && 
                                (((tp = self.timePeriodsNlsObject[formalizeTimePeriod(omcContext.time.timePeriod)]) && tp !== self.timePeriodCustom) || isValidFlexRelTimePeriod(omcContext.time.timePeriod))) {
                            /*** start: omc context has timePeriod set ***/
                            if(tp) {//For quick picks
                                tpId = formalizeTimePeriod(omcContext.time.timePeriod);
                                range = self.initToRelTimePeriod(tpId, curDate);
                                start = range.start;
                                end = range.end;
                            }else {//For flex rel time
                                tpId = formalizeTimePeriod(omcContext.time.timePeriod);
                                
                                range = self.initToRelTimePeriod(tpId, curDate, true);
                                start = range.start;
                                end = range.end;
                            }
                            /*** end: omc context has timePeriod set ***/
                        } else if(omcContext.time && omcContext.time.startTime && omcContext.time.endTime && 
                                !isNaN(parseInt(omcContext.time.startTime)) && !isNaN(parseInt(omcContext.time.endTime))) {
                            /*** start: omc context has startTime & endTime set ***/
                            curDate = new Date();
                            //users input start date and end date
                            sdt = new Date(parseInt(omcContext.time.startTime));
                            edt = new Date(parseInt(omcContext.time.endTime));
                            start = newDateWithMilliseconds(sdt);
                            end = newDateWithMilliseconds(edt);
                            console.log("startTime from global context is " + start + ", endDateTime from gloal context is " + end);
                            console.log("Initializing time is " + curDate);
                            self.lrCtrlVal("timeLevelCtrl");
                            customClick(0);
                            /*** end: omc context has startTime & endTime set ***/
                        }else {
                            //set default time period if there is
                            if(self.timePeriodsNlsObject[self.defaultTimePeriod()]) { //for quick picks
                                tpId = formalizeTimePeriod(self.defaultTimePeriod());
                                if(tpId === quickPicks.CUSTOM) {
                                    if(params.defaultStartDateTime && params.defaultEndDateTime) {
                                        sdt = self.getParam(params.defaultStartDateTime);
                                        edt = self.getParam(params.defaultEndDateTime);
                                        start = newDateWithMilliseconds(sdt);
                                        end = newDateWithMilliseconds(edt);
                                        customClick(0);
                                        //update url with default time period when there is no global time context in url
                                        ctxUtil.setStartAndEndTime(start.getTime(), end.getTime(), eventSourceTimeSelector);
                                    }else {
                                        console.error("Error: set defaultTimePeriod to 'Custom' without time range specified");
                                        return;
                                    }
                                }else {
                                    range = self.initToRelTimePeriod(tpId, curDate);
                                    start = range.start;
                                    end = range.end;
                                    
                                    //update url with default time period when there is no global time context in url
                                    ctxUtil.setTimePeriod(tpId, eventSourceTimeSelector);
                                }
                            }else if(isValidFlexRelTimePeriod(self.defaultTimePeriod())){ //for flexible relative time
                                tpId = formalizeTimePeriod(self.defaultTimePeriod());
                                
                                range = self.initToRelTimePeriod(tpId, curDate, true);
                                start = range.start;
                                end = range.end;

                                //update url with default time period when there is no global time context in url
                                ctxUtil.setTimePeriod(tpId, eventSourceTimeSelector);
                            }else {
                                //users input nothing
                                if($.inArray(quickPicks.LAST_15_MINUTE, tpNotToShow)<0) {
                                    range = self.setTimePeriodToLastX(quickPicks.LAST_15_MINUTE, start, end, 0);
                                    start = range.start;
                                    end = range.end;
                                }else{
                                    customClick(0);
                                }
                            }
                        }
                    }

                    if (start.getTime() > end.getTime()) {
                        if($.inArray(quickPicks.LAST_15_MINUTE, tpNotToShow)<0) {
                            range = self.setTimePeriodToLastX(quickPicks.LAST_15_MINUTE, start, end, 0);
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
                    
                    self.startDateISO(start.slice(0, 10));
                    self.endDateISO(end.slice(0, 10));

                    self.startTime(start.slice(10));
                    self.endTime(end.slice(10));
                    
                    curStartDate = self.startDateISO();
                    curEndDate = self.endDateISO();

                    self.dateTimeInfo(self.getDateTimeInfo(self.startDateISO(), self.endDateISO(), self.startTime(), self.endTime(), self.timePeriod()));
                    
                    self.lastStartDateISO(self.startDateISO());
                    self.lastEndDateISO(self.endDateISO());
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
                 * 
                 * @param {type} type: 0 for initialize time picker, 1 for user's action. This param is used for not validating window limit when initialized.
                 * @returns {undefined}
                 */
                function customClick(type) {
                    self.timePeriod(quickPicks.CUSTOM);
                    self.selectByDrawer(false);

                    self.setTimePeriodChosen(quickPicks.CUSTOM);

                    // Do not validate window limit when initialized
                    if(type === 0) {
                        return;
                    }
                }

                /**
                 * 
                 * @param {type} relTimePeriodId relative time period id, including "LATEST" and "LAST_X_UNIT"
                 * @returns {start: <start time in ISO format>, end: <end time in ISO format>}
                 */
//                self.getTimeRangeForRelTimePeriod = function(flexRelTime) {
                self.getTimeRangeForRelTimePeriod = function(relTimePeriodId) {
                    var timeRange;
                    var start;
                    var end;
                    timeRange = ctxUtil.getStartEndTimeFromTimePeriod(relTimePeriodId);
                    
                    if(relTimePeriodId !== quickPicks.LATEST) {
                        if(self.adjustLastX) { //Adjust "LAST_X_UNIT"
                            timeRange = self.adjustLastX(timeRange.start, timeRange.end);
                        }
                    }
                    
                    start = timeRange.start;
                    end = timeRange.end;
                    
                    start = oj.IntlConverterUtils.dateToLocalIso(start);
                    end = oj.IntlConverterUtils.dateToLocalIso(end);
                    
                    return {
                        start: start,
                        end: end
                    }
                };
                
                self.setFlexRelTime = function(num, opt) {
                    var tp = ctxUtil.generateTimePeriodFromUnitAndDuration(opt, num);
                    var timeRange = self.getTimeRangeForRelTimePeriod(tp);
                    var start = timeRange.start;
                    var end = timeRange.end;
                    
                    if(self.lrCtrlVal() !== "timeLevelCtrl") {
                        self.startDateISO(start.slice(0, 10));
                        self.endDateISO(end.slice(0, 10));

                        self.startTime(start.slice(10));
                        self.endTime(end.slice(10));
                    }
                    
                    if(self.showRightPanel() === true && self.isCustomBeyondWindowLimit() === true) {
                        //In above code, we set the start/end date/time. Jet may need some time to render calender and time and somehow override our error.
                        // We need to use setTimeout to make sure below error is correctly rendered.
                        setTimeout(function() {self.flexRelTimeValError(2)}, 0);
                    }else {
                        setTimeout(function() {self.flexRelTimeValError(0)}, 0);
                    }
                };
                
                self.numberValidator = {
                    'validate': function(value) {
                        //TO DO: Need to confirm with Juan what is the biggest number allowed
//                       if(value >= 1 && $.isNumeric(value) && (parseInt(value) === value) && value.toString().length<4) {
                    if($.isNumeric(value) && (parseInt(value) === value)) { //Number will be limited by min&max attr of ojInputNumber. So only valid if it is an int number.
                           return true;
                       }else {
                           throw new Error(self.flexRelTimeValErrorMsg);
                       }
                    }
                };
                
                /**
                 * Recover start and end date in input box to avoid error message shown
                 * It is used in 2 places:
                 * 1. switch radio from "Range" to "Last"
                 * 2. close panel and reopen it
                 * @returns {undefined}
                 */
                self.recoverDates = function() {
                    var date = oj.IntlConverterUtils.dateToLocalIso(new Date());
                    var origStartDate = self.lastStartDateISO() ? self.lastStartDateISO() : self.startDateISO();
                    var origEndDate = self.lastEndDateISO() ? self.lastEndDateISO() : self.endDateISO();
                    
                    self.startDateISO(date);
                    self.endDateISO(date);
                    
                    self.startDateISO(origStartDate);
                    self.endDateISO(origEndDate);
                }
                     
                self.flexRelTimeValChanged = function(event, data) {
                    if(data.option === "messagesShown" && isArray(data.value) && data.value.length>0) {
                        self.flexRelTimeValError(1);
                    }else{
                        self.flexRelTimeValError(0);
                    }
                    if(self.init || (data.option !== "value" && data.option !== "disabled")) {
                        return;
                    }
                    
                    if(data.option === "value" && data.previousValue === data.value) {
                        return;
                    }
                    
                    if(data.option === "disabled") {
                        if(data.value === true) {
                            self.flexRelTimeValError(0);
                            return;
                        }else {
                            //recover date and time inputs
                            self.startDateError(0);
                            self.endDateError(0);
                            self.startTimeError(0);
                            self.endTimeError(0);
                            self.recoverDates();
                            if(self.showRightPanel() === true && self.isCustomBeyondWindowLimit() === true) {
                                self.flexRelTimeValError(2);
                            }
                            return;
                        }
                    }
                    
                    var num = data.value;
                    var opt = self.flexRelTimeOpt()[0];
                    
                    //show sigular/plural time level options
                    switch(opt) {
                        case timeUnits.SECOND:
                            if(num === 1) {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_SECOND);
                            }else {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_SECONDS);
                            }
                            break;
                        case timeUnits.MINUTE:
                            if(num === 1) {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_MINUTE);
                            }else {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_MINUTES);
                            }
                            break;
                        case timeUnits.HOUR:
                            if(num === 1) {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_HOUR);
                            }else {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_HOURS);
                            }
                            break;
                        case timeUnits.DAY:
                            if(num === 1) {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_DAY);
                            }else {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_DAYS);
                            }
                            break;
                        case timeUnits.WEEK:
                            if(num === 1) {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_WEEK);
                            }else {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_WEEKS);
                            }
                            break;
                        case timeUnits.MONTH:
                            if(num === 1) {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_MONTH);
                            }else {
                                $(self.pickerPanelId+" .oj-select-chosen").text(nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_MONTHS);
                            }
                            break;
                        case timeUnits.YEAR:
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
                        
                        //Set style of self.timePeriod() to "not shown", "chosen", "disabled"
                        self.setTimePeriodsNotToShow(ko.unwrap(self.timePeriodsNotToShow));
                        self.setTimePeriodChosen(self.timePeriod());
                        self.setTimePeriodsDisabled();
                        self.showRightPanel(false);

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
                        if(!self.removeClickListenerOnCalendar) {
                            self.removeClickListenerOnCalendar = true;
                            $(".oj-datepicker-popup").each(function() {
                                this.addEventListener("click", function(_event) {
                                    _event.stopPropagation();
                                    return true;
                                });
                            }, false);
                        }
                    }
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
                    
                    self.recoverDates();
//                    self.startDateISO(self.lastStartDateISO());
//                    self.endDateISO(self.lastEndDateISO());
                    self.startTime(self.lastStartTime());
                    self.endTime(self.lastEndTime());
                    self.timePeriod(self.lastTimePeriod());
                    self.lrCtrlVal(self.lastLrCtrlVal());
                    var lastflexRelTimeVal = self.lastFlexRelTimeVal();
                    self.flexRelTimeVal(lastflexRelTimeVal - 1);
                    self.flexRelTimeVal(lastflexRelTimeVal);
                    self.flexRelTimeOpt([self.lastFlexRelTimeOpt()[0]]);
                    if(self.enableTimeFilter()) {
                        self.tfInstance.timeFilterValue(self.lastTimeFilterValue());
                        self.tfInstance.daysChecked(self.lastDaysChecked());
                        self.tfInstance.monthsChecked(self.lastMonthsChecked());
                    }

                    if(self.lastTimePeriod() !== quickPicks.CUSTOM) {
                        if (self.showLatestOnCustomPanel() && self.lastTimePeriod()===quickPicks.LATEST) {
                            self.lrCtrlVal("latestOnCustom");
                        } else {
                            self.setTimePeriodChosen(self.lastTimePeriod());
                            self.setTimePeriodToLastX(self.lastTimePeriod(), null, null, 0);
                        }
                    }else{
                        customClick(1);
                    }

                    self.shouldSetLastDatas = false;
                };

                self.hoverOnDrawer = function(data, event) {
                    if(!$(event.target).attr("data-tp-id") && !$(event.target).hasClass("recent-list")) { //mouse over on horizonal separator line
                        return;
                    }
                    if(!$(event.target).hasClass("drawerChosen")) {
                        $(event.target).addClass("drawerHover");
                    }
                }
                
                self.hoverOutDrawer = function(data, event) {
                    if(!$(event.target).attr("data-tp-id") && !$(event.target).hasClass("recent-list")) { //mouse over on horizonal separator line
                        return;
                    }
                    $(event.target).removeClass("drawerHover");
                }
                
                self.chooseByRecent = function(data, event) {
                    event.stopPropagation();
                    var tmp;
                    var start;
                    var end;
                    var parsedTp;
                    var tp;
                    var tpId = data.timePeriod;
                    var tpNls = self.timePeriodsNlsObject[tpId];
                    var tpNotToShow = self.getParam(self.timePeriodsNotToShow);
                    if(tpId === quickPicks.CUSTOM) { // for custom time range
                        self.lrCtrlVal("timeLevelCtrl");
                        start = oj.IntlConverterUtils.dateToLocalIso(data.start);
                        end = oj.IntlConverterUtils.dateToLocalIso(data.end);
                        self.timePeriod(tpId);
                    }else if(tpNls) { // for quick picks
                        if(tpId === quickPicks.LATEST) {
                            self.lrCtrlVal("timeLevelCtrl");
                        }else {
                            self.lrCtrlVal("flexRelTimeCtrl");

                            parsedTp = ctxUtil.parseTimePeriodToUnitAndDuration(convertTPIdForQuickPick(tpId, !params.timePeriodsSet));
                            if(parsedTp) {
                                self.flexRelTimeVal(parsedTp.duration);
                                self.flexRelTimeOpt([parsedTp.unit]);
                            }
                        }
                        tmp = self.getTimeRangeForRelTimePeriod(tpId);
                        start = tmp.start;
                        end = tmp.end;                       
                        
                        self.selectByDrawer(true);
                        self.timePeriod(tpId);
                        self.setTimePeriodChosen(tpId);
                    }else {
                        parsedTp = ctxUtil.parseTimePeriodToUnitAndDuration(tpId);
                        tmp = self.getTimeRangeForRelTimePeriod(tpId);
                        start = tmp.start;
                        end = tmp.end;

                        self.lrCtrlVal("flexRelTimeCtrl");
                        if(parsedTp) {
                            self.flexRelTimeVal(parsedTp.duration);
                            self.flexRelTimeOpt([parsedTp.unit]);
                        }

                        if(!params.timePeriodsSet && tpId === "LAST_1_WEEK") {
                            tpId = quickPicks.LAST_7_DAY;
                            if($.inArray(tpId, tpNotToShow) === -1) {
                                self.setTimePeriodChosen(tpId);
                                self.setTimePeriodToLastX(tpId, new Date(start), new Date(end), 1);
                            }else {
                                customClick(0);
                            }
                        }else if(!params.timePeriodsSet && tpId === "LAST_1_HOUR") {
                            tpId = quickPicks.LAST_60_MINUTE;
                            if($.inArray(tpId, tpNotToShow) === -1) {
                                self.setTimePeriodChosen(tpId);
                                self.setTimePeriodToLastX(tpId, new Date(start), new Date(end), 1);
                            }else {
                                customClick(0);
                            }
                        }else {
                            self.timePeriod(quickPicks.CUSTOM);
                        }
                    }
                    
                    self.startDateISO(start.slice(0, 10));
                    self.endDateISO(end.slice(0,  10));

                    self.startTime(start.slice(10));
                    self.endTime(end.slice(10));
                    
                    self.recentList.remove(data);
                    setTimeout(function() {self.applyClick();}, 0);
                };
                
                //select time period
                self.chooseTimePeriod = function (data, event) {
                    var timeRange;
                    var start;
                    var end;
                    var parsedTp;

                    var chosenPeriodId;
                    //Get time period id
                    if (!data) {
                        //choose the radio latest in the custom panel
                        chosenPeriodId = event.extended;
                    } else {
                        chosenPeriodId = $(event.target).attr("data-tp-id");
                    }
                    
                    if(!chosenPeriodId) { //click on horizonal separator line
                        return;
                    }
                    
                    if(chosenPeriodId === "RECENT") {
                        if(self.recentList().length>0) {
                            self.setTimePeriodChosen("RECENT");
                            $("#recentPanel_"+self.randomId).ojPopup("open", "#drawer14_"+self.randomId, {"my": "start top", "at": "end top"});
                        }
                        return;
                    }
                    
                    if($(event.target).hasClass("drawerDisabled")) {
                        return;
                    }

                    if (chosenPeriodId !== quickPicks.CUSTOM) {
                        parsedTp = ctxUtil.parseTimePeriodToUnitAndDuration(convertTPIdForQuickPick(chosenPeriodId, !params.timePeriodsSet));

                        if(parsedTp) {
                            self.lrCtrlVal("flexRelTimeCtrl");
                            self.flexRelTimeVal(parsedTp.duration);
                            self.flexRelTimeOpt([parsedTp.unit]);
                        }else {
                            if(chosenPeriodId === quickPicks.LATEST && self.showLatestOnCustomPanel()) {
                                self.lrCtrlVal("latestOnCustom");
                            }else{
                                self.lrCtrlVal("timeLevelCtrl");
                            }
                        }

                        timeRange = self.getTimeRangeForRelTimePeriod(chosenPeriodId);
                        start = timeRange.start;
                        end = timeRange.end;

                        self.startDateISO(start.slice(0, 10));
                        self.endDateISO(end.slice(0, 10));

                        self.startTime(start.slice(10));
                        self.endTime(end.slice(10));

                        self.selectByDrawer(true);

                        self.timePeriod(chosenPeriodId);
                        self.hoverOutDrawer(data, event); //remove hover style when ele is clicked for firefox

                        if (chosenPeriodId !== quickPicks.LATEST || !self.showLatestOnCustomPanel()) {
                            self.setTimePeriodChosen(self.timePeriod());
                            setTimeout(function () {
                                self.applyClick();
                            }, 0);
                        }else{
                            customClick(1);
                        }
                    }else {
                        self.setTimePeriodChosen(chosenPeriodId);
                        self.showRightPanel(true);
                        //set modality to "modeless" in case it is set as "modal" in launchTimePickerCucstom function
                        $("#pickerPanel_"+self.randomId).ojPopup("option", "modality", "modeless");
                        $("#pickerPanel_"+self.randomId).ojPopup("open", "#dateTimePicker_"+self.randomId+" .drawers", self.pickerPanelPosition);
                    }

//                    $(event.target).focus();
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

                    self.lastStartDateISO(self.startDateISO());
                    self.lastEndDateISO(self.endDateISO());
                    self.lastStartTime(self.startTime());
                    self.lastEndTime(self.endTime());
                    self.lastTimePeriod(self.timePeriod());
                    self.lastLrCtrlVal(self.lrCtrlVal());
                    self.lastFlexRelTimeVal(self.flexRelTimeVal());
                    self.lastFlexRelTimeOpt([self.flexRelTimeOpt()[0]]);

                    var start, end;
                    
                    if(self.lrCtrlVal() === "flexRelTimeCtrl") {
                        flexRelTimeVal = self.flexRelTimeVal();
                        flexRelTimeOpt = self.flexRelTimeOpt()[0];
                        flexRelTimePeriodId = ctxUtil.generateTimePeriodFromUnitAndDuration(flexRelTimeOpt, flexRelTimeVal);

                        if(!params.timePeriodsSet && flexRelTimePeriodId === "LAST_1_HOUR") {
                            self.timePeriod(quickPicks.LAST_60_MINUTE);
                        }else if(!params.timePeriodsSet && flexRelTimePeriodId === "LAST_1_WEEK") {
                            self.timePeriod(quickPicks.LAST_7_DAY);
                        }else if (self.timePeriodsNlsObject[flexRelTimePeriodId] && $.inArray(flexRelTimePeriodId, self.getParam(self.timePeriodsNotToShow)) === -1) {
                            self.timePeriod(flexRelTimePeriodId);
                        }else {
                            self.setTimePeriodChosen(quickPicks.CUSTOM);
                            self.timePeriod(quickPicks.CUSTOM);
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

                    self.dateTimeInfo(self.getDateTimeInfo(self.startDateISO().slice(0, 10), self.endDateISO().slice(0, 10), self.startTime(), self.endTime(), self.timePeriod()));
                    
                    self.closeAllPopups();
                    var timePeriod = self.timePeriod();
                    
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
                        if(timePeriod === quickPicks.CUSTOM && flexRelTimeVal && flexRelTimeOpt) {
                            recentTimePeriodId =  flexRelTimePeriodId;
                        }else if(self.lrCtrlVal() === "latestOnCustom") {
                            recentTimePeriodId = quickPicks.LATEST;
                        }else {
                            recentTimePeriodId = timePeriod;
                        }
                        tmpList.remove(function(data) {return (data.timePeriod === recentTimePeriodId) && (recentTimePeriodId !== quickPicks.CUSTOM);});
                        tmpList.unshift({start: new Date(start), end: new Date(end), timePeriod: recentTimePeriodId, 
                                        timeFilter: self.timeFilter()});
                        self.recentList(tmpList.slice(0, 5));
                        
                        if(timePeriod === quickPicks.CUSTOM) {
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
                    timeFilterParams: self.timeFilterParams
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
                    var tp = self.timePeriodsNlsObject[ctxEventTp];
                    timeRange = ctxUtil.getStartEndTimeFromTimePeriod(ctxEventTp);
                    start = ctxEventStart ? new Date(ctxEventStart) : timeRange.start;
                    end = ctxEventEnd ? new Date(ctxEventEnd) : timeRange.end;
                    start = oj.IntlConverterUtils.dateToLocalIso(start);
                    end = oj.IntlConverterUtils.dateToLocalIso(end);
                    if (tp) { //For quick picks
                        ctxEventTp = convertTPIdForQuickPick(ctxEventTp, !params.timePeriodsSet);
                        if(ctxEventTp === quickPicks.LATEST) {
                            if(self.showLatestOnCustomPanel()) {
                                self.lrCtrlVal("latestOnCustom");
                            }
                        }else {
                            parsedTimePeriod = ctxUtil.parseTimePeriodToUnitAndDuration(ctxEventTp);
    //
                            if(parsedTimePeriod) {
                                self.lrCtrlVal("flexRelTimeCtrl");
                                self.flexRelTimeVal(parsedTimePeriod.duration);
                                self.flexRelTimeOpt([parsedTimePeriod.unit]);
                            }else {
                                self.lrCtrlVal("timeLevelCtrl");
                            }
                        }
                        self.selectByDrawer(true);
                        self.timePeriod(ctxEventTp);
                        self.setTimePeriodChosen(self.timePeriod());
                    } else { //for flexible time period
                        parsedTimePeriod = ctxUtil.parseTimePeriodToUnitAndDuration(ctxEventTp);

                        self.lrCtrlVal("flexRelTimeCtrl");
                        if (parsedTimePeriod) {
                            self.flexRelTimeVal(parsedTimePeriod.duration);
                            self.flexRelTimeOpt([parsedTimePeriod.unit]);
                        }

                        if(!params.timePeriodsSet && ctxEventTp === "LAST_1_HOUR") {
                            self.timePeriod(quickPicks.LAST_60_MINUTE);
                            self.setTimePeriodChosen(self.timePeriod());
                        }else if(!params.timePeriodsSet && ctxEventTp === "LAST_1_WEEK") {
                            self.timePeriod(quickPicks.LAST_7_DAY);
                            self.setTimePeriodChosen(self.timePeriod());
                        }else {
                            self.timePeriod(quickPicks.CUSTOM);
                            self.setTimePeriodChosen(self.timePeriod());
                        }
                    }

                    self.startDateISO(start.slice(0, 10));
                    self.endDateISO(end.slice(0, 10));

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

                    self.timePeriod(quickPicks.CUSTOM);

                    self.startDateISO(start.slice(0, 10));
                    self.endDateISO(end.slice(0, 10));

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
                            tp = self.timePeriod();
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
                        }else if(ctxChangeEvent.contextName === "startTime" && (ctxUtil.getTimePeriod() === quickPicks.CUSTOM) && ctxUtil.getEndTime() && 
                                (parseInt(ctxChangeEvent.currentValue)<ctxUtil.getEndTime())) {
                            setDateTimeForCustomTime(ctxChangeEvent.currentValue, ctxUtil.getEndTime());
                            setTimeout(function() {self.applyClick(false);}, 0);
                        }else if(ctxChangeEvent.contextName === "endTime" && (ctxUtil.getTimePeriod() === quickPicks.CUSTOM) && ctxUtil.getStartTime() && 
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
                        _time = self.getTimeRangeForRelTimePeriod(self.badgeTimePeriod());
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
                    overflowedLabel = overflowedLabel.substring(overflowedLabel.indexOf(":")+1);
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
                };

                self.labelMouseOutHandler = function() {
                    if($("#overflowedLabelInfo_"+self.randomId).ojPopup("isOpen")) {
                        $("#overflowedLabelInfo_"+self.randomId).ojPopup("close");
                    }
                };

                ctxUtil.subscribeOMCContextChangeEvent(callbackForOmcCtxChange);
                
                self.initialize();
            }
            return dateTimePickerViewModel;
        });
