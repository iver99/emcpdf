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
	'uifwk': '/emsaasui/uifwk'
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
    'uifwk/js/sdk/context-util',
//    'uifwk/js/widgets/timeFilter/js/timeFilter',
    'ojs/ojknockout',
    'ojs/ojchart',
    'ojs/ojbutton'
],
        function (oj, ko, $, _emJETCustomLogger, userTenantUtilModel, dfuModel, ctxModel/*, timeFilter*/) // this callback gets executed when all required modules are loaded
        {
            var userTenantUtil = new userTenantUtilModel(); 
            var dfu = new dfuModel();
            var ctxUtil = new ctxModel();
            
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
                //Mockup page for EMCPDF-4076
                var self = this;
                var start = new Date(new Date() - 24 * 60 * 60 * 1000);
                var end = new Date();
                var dateTimeOption = {year: 'numeric', month: '2-digit', day: '2-digit'};
                var dateOption = {formatType: "date", dateFormat: "medium"};
                
                self.buttonLabel = 'Change disabled quick picks';
                
                self.floatPosition1 = "left";
                self.dateTimeConverter1 = oj.Validation.converterFactory("dateTime").createConverter(dateTimeOption);
                self.dateConverter = oj.Validation.converterFactory("dateTime").createConverter(dateOption);
                self.timeConverter = oj.Validation.converterFactory("dateTime").createConverter({pattern: 'h:mm a'});

                var tmpStart = oj.IntlConverterUtils.dateToLocalIso(start);
                var tmpEnd = oj.IntlConverterUtils.dateToLocalIso(end)
                self.start1 = ko.observable(self.dateConverter.format(tmpStart.slice(0, 10)) + " " + self.timeConverter.format(tmpStart.slice(10)));
                self.end1 = ko.observable(self.dateConverter.format(tmpEnd.slice(0, 10)) + " " + self.timeConverter.format(tmpEnd.slice(10)));
                
                self.start2 = ko.observable(self.dateConverter.format(tmpStart.slice(0, 10)) + " " + self.timeConverter.format(tmpStart.slice(10)));
                self.end2 = ko.observable(self.dateConverter.format(tmpEnd.slice(0, 10)) + " " + self.timeConverter.format(tmpEnd.slice(10)));
                self.initStart = ko.observable(start);
                self.initEnd = ko.observable(end);
                self.timePeriodsNotToShow = ko.observableArray([]);
                self.timeLevelsNotToShow = ko.observable(["second"]);
                self.showTimeAtMillisecond = ko.observable(false);
                self.timeDisplay = ko.observable("short");
                self.timePeriodPre = ko.observable("LAST_7_DAY");
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
                    enableTimeFilter: true,
                    dtpickerPosition: self.floatPosition1,
                    timePeriodsSet: "SHORT_TERM",
                    defaultTimePeriod: "LAST_1_DAY",
                    timePeriodsToBeDisabled: ko.observableArray(["LAST_7_DAY"]),
                    enableLatestOnCustomPanel: true,
                    callbackAfterApply: function (start, end, tp, tf, relTimeVal, relTimeUnit) {
                        var appliedStart = oj.IntlConverterUtils.dateToLocalIso(start);
                        var appliedEnd = oj.IntlConverterUtils.dateToLocalIso(end);
                        if((self.isTimePeriodLessThan1day(tp) || relTimeUnit==="SECOND" || relTimeUnit==="MINUTE" || relTimeUnit === "HOUR") && (start.getTimezoneOffset() !== end.getTimezoneOffset())) {
                            self.start1(self.dateConverter.format(appliedStart.slice(0, 10)) + " " + self.timeConverter.format(appliedStart.slice(10))+" ("+self.getGMTTimezone(start)+")");
                            self.end1(self.dateConverter.format(appliedEnd.slice(0, 10)) + " " + self.timeConverter.format(appliedEnd.slice(10))+" ("+self.getGMTTimezone(end)+")");
                        }else {
                            self.start1(self.dateConverter.format(appliedStart.slice(0, 10)) + " " + self.timeConverter.format(appliedStart.slice(10)));
                            self.end1(self.dateConverter.format(appliedEnd.slice(0, 10)) + " " + self.timeConverter.format(appliedEnd.slice(10)));
                        }
                    },
                    callbackAfterCancel: function() { //calback after "Cancel" is clicked
                        console.log("***");
                    }
                };

                self.timeParams2 = {
                    enableTimeFilter: true,
                    dtpickerPosition: self.floatPosition1,
                    timePeriodsSet: "LONG_TERM",
                    defaultTimePeriod: "LAST_1_DAY",
                    timePeriodsToBeDisabled: ko.observableArray(["LAST_24_HOUR"]),
                    enableLatestOnCustomPanel: true,
                    callbackAfterApply: function (start, end, tp, tf, relTimeVal, relTimeUnit) {
                        var appliedStart = oj.IntlConverterUtils.dateToLocalIso(start);
                        var appliedEnd = oj.IntlConverterUtils.dateToLocalIso(end);
                        if((self.isTimePeriodLessThan1day(tp) || relTimeUnit==="SECOND" || relTimeUnit==="MINUTE" || relTimeUnit === "HOUR") && (start.getTimezoneOffset() !== end.getTimezoneOffset())) {
                            self.start2(self.dateConverter.format(appliedStart.slice(0, 10)) + " " + self.timeConverter.format(appliedStart.slice(10))+" ("+self.getGMTTimezone(start)+")");
                            self.end2(self.dateConverter.format(appliedEnd.slice(0, 10)) + " " + self.timeConverter.format(appliedEnd.slice(10))+" ("+self.getGMTTimezone(end)+")");
                        }else {
                            self.start2(self.dateConverter.format(appliedStart.slice(0, 10)) + " " + self.timeConverter.format(appliedStart.slice(10)));
                            self.end2(self.dateConverter.format(appliedEnd.slice(0, 10)) + " " + self.timeConverter.format(appliedEnd.slice(10)));
                        }
                    }
                };
                
                self.changeOption1 = function() {
                    self.timeParams1.timePeriodsToBeDisabled(["LAST_7_DAY", "LAST_14_DAY"]);
                };
                
                self.changeOption2 = function() {
                    self.timeParams2.timePeriodsToBeDisabled(["LAST_30_DAY", "LAST_12_MONTH"]);
                };
            }
            ko.applyBindings(new MyViewModel(), document.getElementById("dateTimePicker"));
        }
);
