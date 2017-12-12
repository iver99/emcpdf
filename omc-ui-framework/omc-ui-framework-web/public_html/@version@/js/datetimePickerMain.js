/**
 * Example of Require.js boostrap javascript
 */


requirejs.config({
    bundles: ((window.DEV_MODE !==null && typeof window.DEV_MODE ==="object") ||
                (window.gradleDevMode !==null && typeof window.gradleDevMode ==="boolean")) ? undefined : {
        'uifwk/@version@/js/uifwk-impl-partition-cached': 
            [
            'uifwk/js/util/ajax-util',
            'uifwk/js/util/df-util',
            'uifwk/js/util/logging-util',
            'uifwk/js/util/message-util',
            'uifwk/js/util/mobile-util',
            'uifwk/js/util/preference-util',
            'uifwk/js/util/screenshot-util',
            'uifwk/js/util/typeahead-search',
            'uifwk/js/util/usertenant-util',
            'uifwk/js/util/zdt-util',
            'uifwk/js/sdk/context-util',
            'uifwk/js/widgets/aboutbox/js/aboutbox',
            'uifwk/js/widgets/brandingbar/js/brandingbar',
            'uifwk/js/widgets/datetime-picker/js/datetime-picker',
            'uifwk/js/widgets/navlinks/js/navigation-links',
            'uifwk/js/widgets/timeFilter/js/timeFilter',
            'uifwk/js/widgets/widgetselector/js/widget-selector',
            'text!uifwk/js/widgets/aboutbox/html/aboutbox.html',
            'text!uifwk/js/widgets/navlinks/html/navigation-links.html',
            'text!uifwk/js/widgets/brandingbar/html/brandingbar.html',
            'text!uifwk/js/widgets/widgetselector/html/widget-selector.html',
            'text!uifwk/js/widgets/timeFilter/html/timeFilter.html',
            'text!uifwk/js/widgets/datetime-picker/html/datetime-picker.html'
            ]
    },
    // Path mappings for the logical module names
    paths: {
        'knockout': '../../libs/@version@/js/oraclejet/js/libs/knockout/knockout-3.4.0',
        'jquery': '../../libs/@version@/js/oraclejet/js/libs/jquery/jquery-2.1.3.min',
        'jqueryui': '../../libs/@version@/js/oraclejet/js/libs/jquery/jquery-ui-1.11.4.custom.min',
        'jqueryui-amd': '../../libs/@version@/js/oraclejet/js/libs/jquery/jqueryui-amd-1.11.4.min',
        'promise': '../../libs/@version@/js/oraclejet/js/libs/es6-promise/promise-1.0.0.min',
        'hammerjs': '../../libs/@version@/js/oraclejet/js/libs/hammer/hammer-2.0.4.min',
        'ojdnd': '../../libs/@version@/js/oraclejet/js/libs/dnd-polyfill/dnd-polyfill-1.0.0.min',
        'ojs': '../../libs/@version@/js/oraclejet/js/libs/oj/v2.0.2/min',
        'ojL10n': '../../libs/@version@/js/oraclejet/js/libs/oj/v2.0.2/ojL10n',
        'ojtranslations': '../../libs/@version@/js/oraclejet/js/libs/oj/v2.0.2/resources',
        'signals': '../../libs/@version@/js/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../../libs/@version@/js/oraclejet/js/libs/crossroads/crossroads.min',
        'history': '../../libs/@version@/js/oraclejet/js/libs/history/history.iegte8.min',
        'text': '../../libs/@version@/js/oraclejet/js/libs/require/text',
	'uifwk': '/emsaasui/uifwk',
        'emsaasui': '/emsaasui',
        'emcta': '/emsaasui/emcta/ta/js'
    },
    // Shim configurations for modules that do not expose AMD
    shim: {
        'jquery': {
            exports: ['jQuery', '$']
        },'crossroads': {
            deps: ['signals'],
            exports: 'crossroads'
        }
    },
    // This section configures the i18n plugin. It is merging the Oracle JET built-in translation
    // resources with a custom translation file.
    // Any resource file added, must be placed under a directory named "nls". You can use a path mapping or you can define
    // a path that is relative to the location of this main.js file.
    config: {
        ojL10n: {
            merge: {
               // 'ojtranslations/nls/ojtranslations': 'resources/nls/dashboardsMsgBundle'
            }
        },text: {
            useXhr: function (url, protocol, hostname, port) {
              // allow cross-domain requests
              // remote server allows CORS
              return true;
            }
          }
    },
    waitSeconds: 300
});




/**
 * A top-level require call executed by the Application.
 * Although 'ojcore' and 'knockout' would be loaded in any case (they are specified as dependencies
 * by the modules themselves), we are listing them explicitly to get the references to the 'oj' and 'ko'
 * objects in the callback
 */
require(['ojs/ojcore',
    'knockout',
    'jquery',
    'uifwk/js/util/logging-util',
    'uifwk/js/util/usertenant-util',
    'uifwk/js/util/df-util',
//    'uifwk/js/widgets/timeFilter/js/timeFilter',
    'ojs/ojknockout',
    'ojs/ojchart',
    'ojs/ojbutton'
],
        function (oj, ko, $, _emJETCustomLogger, userTenantUtilModel, dfuModel/*, timeFilter*/) // this callback gets executed when all required modules are loaded
        {
            var userTenantUtil = new userTenantUtilModel(); 
            var dfu = new dfuModel();
            
            ko.components.register("date-time-picker", {
                viewModel: {require: "uifwk/js/widgets/datetime-picker/js/datetime-picker"},
                template: {require: "text!uifwk/js/widgets/datetime-picker/html/datetime-picker.html"}
            });
            
            function getLogUrl(){
                //change value to 'data/servicemanager.json' for local debugging, otherwise you need to deploy app as ear
                if (dfu.isDevMode()){
                    return dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint,"logging/logs");
                }else{
                    return '/sso.static/dashboards.logging/logs';
                }
            };           
                       
            var userTenant = userTenantUtil.getUserTenant();           
            
            var logger = new _emJETCustomLogger();
            var logReceiver = getLogUrl();

            logger.initialize(logReceiver, 60000, 20000, 8, userTenant.tenantUser);
            logger.setLogLevel(oj.Logger.LEVEL_WARN);
        
            window.onerror = function (msg, url, lineNo, columnNo, error)
            {
                var msg = "Accessing " + url + " failed. " + "Error message: " + msg + ". Line: " + lineNo + ". Column: " + columnNo;
                if(error.stack) {
                    msg = msg + ". Error: " + JSON.stringify(error.stack);
                }
                oj.Logger.error(msg, true);

                return false; 
            }

            function MyViewModel() {
                var self = this;
                var start = new Date(new Date() - 24 * 60 * 60 * 1000);
                var end = new Date();
                var dateTimeOption = {formatType: "datetime", dateFormat: "medium"};
                var dateOption = {formatType: "date", dateFormat: "medium"};
                self.floatPosition1 = "left";
                self.floatPosition3 = "right";
                self.dateTimeConverter1 = oj.Validation.converterFactory("dateTime").createConverter(dateTimeOption);
                self.dateConverter = oj.Validation.converterFactory("dateTime").createConverter(dateOption);

                self.start = ko.observable(self.dateTimeConverter1.format(oj.IntlConverterUtils.dateToLocalIso(start)));
                self.end = ko.observable(self.dateTimeConverter1.format(oj.IntlConverterUtils.dateToLocalIso(end)));
                self.filterInfo = ko.observable();
                self.filterInfo3 = ko.observable();
                self.start2 = ko.observable(self.dateConverter.format(oj.IntlConverterUtils.dateToLocalIso(start)));
                self.end2 = ko.observable(self.dateConverter.format(oj.IntlConverterUtils.dateToLocalIso(end)));
                self.start3 = ko.observable(self.dateTimeConverter1.format(oj.IntlConverterUtils.dateToLocalIso(start)));
                self.end3 = ko.observable(self.dateTimeConverter1.format(oj.IntlConverterUtils.dateToLocalIso(end)));
                self.initStart = ko.observable(start);
                self.initEnd = ko.observable(end);
                self.timePeriodsNotToShow = ko.observableArray([]);
                self.timeLevelsNotToShow = ko.observable(["second"]);
                self.showTimeAtMillisecond = ko.observable(false);
                self.timeDisplay = ko.observable("short");
                self.timePeriodPre = ko.observable("Last 7 days");
                self.changeLabel = ko.observable(true);
                self.timeFilterParams = {hoursIncluded: "8-18", daysIncluded: ["2", "3", "4", "5", "6"], monthsIncluded: ["2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"]};

                self.isTimePeriodLessThan1day = function(timePeriod) {
                    if(timePeriod==="Last 15 minutes" || timePeriod==="Last 30 minutes" || timePeriod==="Last 60 minutes" ||
                                timePeriod==="Last 4 hours" || timePeriod==="Last 6 hours") {
                        return true;
                    }
                    return false;
                };

                self.getGMTTimezone = function(date) {
                    var timezoneOffset = date.getTimezoneOffset()/60;
                    timezoneOffset = timezoneOffset>0 ? ("GMT-"+timezoneOffset) : ("GMT+"+Math.abs(timezoneOffset));
                    return timezoneOffset;
                };
                
                self.adjustTime = function(start, end) {
                    var adjustedStart, adjustedEnd;
                    adjustedStart =start - 60*60*1000;
                    adjustedEnd = end.getTime()+ 60*60*1000;
                    return {
                        start: new Date(adjustedStart),
                        end: new Date(adjustedEnd)
                    };
                };
                
                self.timeParams1 = {
                    startDateTime: /*self.initStart,*/ start,
                    endDateTime: self.initEnd, //end,
                    timePeriodsNotToShow: /*["Last 30 days", "Last 90 days"],*/ self.timePeriodsNotToShow,
                    timeLevelsNotToShow: self.timeLevelsNotToShow, //custom relative time levels not to show. Support lower and upper case time units
                    showTimeAtMillisecond: self.showTimeAtMillisecond, //show time at minute or millisecond level in ojInputTime component
                    enableTimeFilter: true,
                    hideMainLabel: true,
                    dtpickerPosition: self.floatPosition1,
                    timePeriod: "Last 1 day", //self.timePeriodPre,
//                    timeFilterParams: self.timeFilterParams,
                    callbackAfterApply: function (start, end, tp, tf, relTimeVal, relTimeUnit) {
                        var appliedStart = oj.IntlConverterUtils.dateToLocalIso(start);
                        var appliedEnd = oj.IntlConverterUtils.dateToLocalIso(end);
                        if((self.isTimePeriodLessThan1day(tp) || relTimeUnit==="SECOND" || relTimeUnit==="MINUTE" || relTimeUnit === "HOUR") && (start.getTimezoneOffset() !== end.getTimezoneOffset())) {
                            self.start(self.dateTimeConverter1.format(appliedStart)+" ("+self.getGMTTimezone(start)+")");
                            self.end(self.dateTimeConverter1.format(appliedEnd)+" ("+self.getGMTTimezone(end)+")");
                        }else {
                            self.start(self.dateTimeConverter1.format(appliedStart));
                            self.end(self.dateTimeConverter1.format(appliedEnd));
                        }
                        var eles = $('span').filter(function() {return this.id.match(/tfInfoIndicator_.*\d$/);});
                        $(eles[0]).click();
                        $(eles[0]).click();
                        eles = $('div').filter(function(){return this.id.match(/tfInfo_.*\d$/);});
                        self.filterInfo($(eles[0]).find("span").text());
                        self.generateData(start, end);
                    },
                    callbackAfterCancel: function() { //calback after "Cancel" is clicked
                        console.log("***");
                    }
                };

                self.timeParams3 = {
                    startDateTime: start,
                    endDateTime: end,
                    enableTimeFilter: true,
                    hideMainLabel: true,
                    timeDisplay: self.timeDisplay,
                    dtpickerPosition: self.floatPosition3,
                    timePeriod: "Last 1 day", //self.timePeriodPre,
                    callbackAfterApply: function (start, end, tp, tf, relTimeVal, relTimeUnit) {
                        var appliedStart = oj.IntlConverterUtils.dateToLocalIso(start);
                        var appliedEnd = oj.IntlConverterUtils.dateToLocalIso(end);
                        if((self.isTimePeriodLessThan1day(tp) || relTimeUnit==="SECOND" || relTimeUnit==="MINUTE" || relTimeUnit === "HOUR") && (start.getTimezoneOffset() !== end.getTimezoneOffset())) {
                            self.start3(self.dateTimeConverter1.format(appliedStart)+" ("+self.getGMTTimezone(start)+")");
                            self.end3(self.dateTimeConverter1.format(appliedEnd)+" ("+self.getGMTTimezone(end)+")");
                        }else {
                            self.start3(self.dateTimeConverter1.format(appliedStart));
                            self.end3(self.dateTimeConverter1.format(appliedEnd));
                        }
                        var eles = $('span').filter(function() {return this.id.match(/tfInfoIndicator_.*\d$/);});
                        $(eles[0]).click();
                        $(eles[0]).click();
                        eles = $('div').filter(function(){return this.id.match(/tfInfo_.*\d$/);});
                        console.log($(eles[0]).find("span").text());
                        self.filterInfo3($(eles[0]).find("span").text());
                    }
                };

                self.changeOption = function() {
                    self.showTimeAtMillisecond(true);
                    self.timeLevelsNotToShow([]);
                    return;
                    self.changeLabel(false);
                    self.initStart(new Date(new Date() - 48*60*60*1000));
                    self.initEnd(new Date(new Date() - 3*60*60*1000));
                    self.timePeriodsNotToShow(["Last 90 days", "Latest"]);
                    self.timeDisplay("long");
                    self.timePeriodPre("Last 90 days");
                };
                
                self.changeOption1 = function() {
                    self.showTimeAtMillisecond(false);
                    self.timeLevelsNotToShow(["second"]);
                }

                self.lineSeriesValues = ko.observableArray();
                self.lineGroupsValues = ko.observableArray();

                self.generateData = function (start, end) {
                    var lineSeries = [];
                    var lineGroups = [];
                    var timeInterval, dateTimeDiff;

                    var dateTimeOption = {formatType: "datetime", dateFormat: "short"};
                    self.dateTimeConverter = oj.Validation.converterFactory("dateTime").createConverter(dateTimeOption);

                    var timeOption = {formatType: "time", timeFormat: "short"};
                    self.timeConverter = oj.Validation.converterFactory("dateTime").createConverter(timeOption);

                    dateTimeDiff = new Date(end).getTime() - new Date(start).getTime();
                    //time range is less than 1 hour
                    if (dateTimeDiff <= 60 * 60 * 1000) {
                        timeInterval = 60 * 1000;  //1 min
                    } else if (dateTimeDiff <= 24 * 60 * 60 * 1000) {
                        timeInterval = 60 * 60 * 1000; //60 min
                    } else {
                        timeInterval = 24 * 60 * 60 * 1000; //1 day
                    }

                    //groups
                    start = new Date(start).getTime();
                    end = new Date(end).getTime();

                    var day, tmp;
                    var n = 0;

                    if (timeInterval === 60 * 1000) {
                        lineGroups.push(self.dateTimeConverter.format(start));
                        n++;
                        while ((start + n * timeInterval) <= end) {
                            lineGroups.push(self.timeConverter.format(start + n * timeInterval));
                            n++;
                        }
                    } else if (timeInterval === 60 * 60 * 1000) {
                        lineGroups.push(self.dateTimeConverter.format(start));
                        n++;
                        day = new Date(start).getDate();
                        while ((start + n * timeInterval) <= end) {
                            tmp = new Date(start + n * timeInterval);
                            if (tmp.getDate() === day) {
                                lineGroups.push(self.timeConverter.format(oj.IntlConverterUtils.dateToLocalIso(tmp)));
                            } else {
                                lineGroups.push(self.dateTimeConverter.format(oj.IntlConverterUtils.dateToLocalIso(tmp)));
                                day = tmp.getDate();
                            }
                            n++;
                        }
                    } else {
                        while ((start + n * timeInterval) <= end) {
                            lineGroups.push(self.dateTimeConverter.format(start + n * timeInterval));
                            n++;
                        }
                    }

                    //series
                    function securedRandom(){
                        var arr = new Uint32Array(1);
                        var crypto = window.crypto || window.msCrypto;
                        crypto.getRandomValues(arr);
                        var result = arr[0] * Math.pow(2,-32);
                        return result;
                    }
                    var seriesNames = ["p1", "p2", "p3"];
                    var seriesMax = [30, 50, 100];
                    var seriesNumber = seriesNames.length;
                    for (var i = 0; i < seriesNumber; i++) {
                        var max = seriesMax[i];
                        var itemsValues = [];
                        for (var j = 0; j < n; j++) {
                            itemsValues.push(Math.floor(securedRandom() * max));
                        }
                        lineSeries.push({name: seriesNames[i], items: itemsValues});
                    }

                    self.lineSeriesValues(lineSeries);
                    self.lineGroupsValues(lineGroups);
                };

                self.generateData(self.timeParams1.startDateTime, self.timeParams1.endDateTime);

                self.timeParams2 = {
                    startDateTime: new Date(new Date() - 24 * 60 * 60 * 1000),
                    endDateTime: new Date(),
                    timePeriod: "LAST_1_DAY",
//                      appId: "APM",
                    hideTimeSelection: true, //hides time selection
                    callbackAfterApply: function (start, end, tp, tf, relTimeVal, relTimeUnit) {
                        var appliedStart = oj.IntlConverterUtils.dateToLocalIso(start);
                        var appliedEnd = oj.IntlConverterUtils.dateToLocalIso(end);
                        self.start2(self.dateConverter.format(appliedStart));
                        self.end2(self.dateConverter.format(appliedEnd));
                    }
                };
                
                self.start4 = ko.observable(self.dateTimeConverter1.format(oj.IntlConverterUtils.dateToLocalIso(new Date(end-60*60*1000))));
                self.end4 = ko.observable(self.dateTimeConverter1.format(oj.IntlConverterUtils.dateToLocalIso(end)));
                
                self.timeParams4 = {
                    hideMainLabel: true,
                    dtpickerPosition: self.floatPosition1,
                    timePeriodsSet: "SHORT_TERM",
                    enableLatestOnCustomPanel: true,
                    callbackAfterApply: function (start, end, tp, tf, relTimeVal, relTimeUnit) {
                        var appliedStart = oj.IntlConverterUtils.dateToLocalIso(start);
                        var appliedEnd = oj.IntlConverterUtils.dateToLocalIso(end);
                        if((self.isTimePeriodLessThan1day(tp) || relTimeUnit==="SECOND" || relTimeUnit==="MINUTE" || relTimeUnit === "HOUR") && (start.getTimezoneOffset() !== end.getTimezoneOffset())) {
                            self.start4(self.dateTimeConverter1.format(appliedStart)+" ("+self.getGMTTimezone(start)+")");
                            self.end4(self.dateTimeConverter1.format(appliedEnd)+" ("+self.getGMTTimezone(end)+")");
                        }else {
                            self.start4(self.dateTimeConverter1.format(appliedStart));
                            self.end4(self.dateTimeConverter1.format(appliedEnd));
                        }
                    }
                };
                
                
                self.start5 = ko.observable(self.dateTimeConverter1.format(oj.IntlConverterUtils.dateToLocalIso(new Date(end-30*24*60*60*1000))));
                self.end5 = ko.observable(self.dateTimeConverter1.format(oj.IntlConverterUtils.dateToLocalIso(end)));
                self.timeParams5 = {
                    hideMainLabel: true,
                    dtpickerPosition: self.floatPosition1,
                    timePeriodsSet: "LONG_TERM",
                    enableLatestOnCustomPanel: true,
                    callbackAfterApply: function (start, end, tp, tf, relTimeVal, relTimeUnit) {
                        var appliedStart = oj.IntlConverterUtils.dateToLocalIso(start);
                        var appliedEnd = oj.IntlConverterUtils.dateToLocalIso(end);
                        if((self.isTimePeriodLessThan1day(tp) || relTimeUnit==="SECOND" || relTimeUnit==="MINUTE" || relTimeUnit === "HOUR") && (start.getTimezoneOffset() !== end.getTimezoneOffset())) {
                            self.start5(self.dateTimeConverter1.format(appliedStart)+" ("+self.getGMTTimezone(start)+")");
                            self.end5(self.dateTimeConverter1.format(appliedEnd)+" ("+self.getGMTTimezone(end)+")");
                        }else {
                            self.start5(self.dateTimeConverter1.format(appliedStart));
                            self.end5(self.dateTimeConverter1.format(appliedEnd));
                        }
                    }
                };
                
                self.start6 = ko.observable(self.dateConverter.format(oj.IntlConverterUtils.dateToLocalIso(new Date(end-30*24*60*60*1000))));
                self.end6 = ko.observable(self.dateConverter.format(oj.IntlConverterUtils.dateToLocalIso(end)));
                self.timeParams6 = {
                    hideMainLabel: true,
                    dtpickerPosition: self.floatPosition1,
                    timePeriodsSet: "LONG_TERM",
                    enableLatestOnCustomPanel: true,
                    hideTimeSelection: true,
                    callbackAfterApply: function (start, end, tp, tf, relTimeVal, relTimeUnit) {
                        var appliedStart = oj.IntlConverterUtils.dateToLocalIso(start);
                        var appliedEnd = oj.IntlConverterUtils.dateToLocalIso(end);
                        self.start6(self.dateConverter.format(appliedStart));
                        self.end6(self.dateConverter.format(appliedEnd));
                    }
                };

                self.timeParams = {
//                    startDateTime: "2015-05-17T00:00:00",
//                    endDateTime: "2015-05-16T13:00:00"
                      startDateTime: new Date(new Date() - 24 * 60 * 60 * 1000),
                      endDateTime: new Date(),
//                      appId: "APM",
                      timePeriodsNotToShow: ["Last 90 days", "Last 30 days", "Latest"], //an array of what not to show
                      customWindowLimit: 4*24*60*60*1000-12*60*60*1000, //in custom mode, limit the size of window
                      adjustLastX: self.adjustTime, //used to adjust times when user choose "Last X"
                      customTimeBack: 7*24*60*60*1000, //the max timestamp of how far the user can pick the date from
                      hideMainLabel: true, //hides the main label "Time Range", defaults to "false"
                      hideRangeLabel: true //hides the selected time range, like "Last 30 minutes" inside the time selection, defaults to "false"
//                      hideTimeSelection: true //hides time selection
                };
            }
            ko.applyBindings(new MyViewModel(), document.getElementById("dateTimePicker"));
        }
);
