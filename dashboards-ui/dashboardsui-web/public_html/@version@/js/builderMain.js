/**
 * @preserve Copyright (c) 2014, Oracle and/or its affiliates.
 * All rights reserved.
 */

/**
 * @preserve Copyright 2013 jQuery Foundation and other contributors
 * Released under the MIT license.
 * http://jquery.org/license
 */
requirejs.config({
    // Setup module id mapping
    map: {
        'emcla' : {'emcsutl/df-util': 'uifwk/js/util/df-util'},
        '*': {
              'emcsutl/ajax-util': 'uifwk/js/util/ajax-util',
              'emcsutl/message-util': 'uifwk/js/util/message-util',
              'ajax-util': 'uifwk/js/util/ajax-util',
              'message-util': 'uifwk/js/util/message-util',
              'df-util': 'uifwk/js/util/df-util',
              'prefutil':'uifwk/js/util/preference-util'
             }        
    },
    // Path mappings for the logical module names
    paths: {
        'knockout': '../../libs/@version@/js/oraclejet/js/libs/knockout/knockout-3.4.0',
        'knockout.mapping': '../../libs/@version@/js/oraclejet/js/libs/knockout/knockout.mapping-latest',
        'jquery': '../../libs/@version@/js/oraclejet/js/libs/jquery/jquery-2.1.3.min',
        'jqueryui': '../../libs/@version@/js/jquery/jquery-ui-1.11.4.custom.min',
        'jqueryui-amd':'../../libs/@version@/js/oraclejet/js/libs/jquery/jqueryui-amd-1.11.4.min',
        'hammerjs': '../../libs/@version@/js/oraclejet/js/libs/hammer/hammer-2.0.4.min',
        'ojs': '../../libs/@version@/js/oraclejet/js/libs/oj/v1.2.0/min',
        'ojL10n': '../../libs/@version@/js/oraclejet/js/libs/oj/v1.2.0/ojL10n',
        'ojtranslations': '../../libs/@version@/js/oraclejet/js/libs/oj/v1.2.0/resources',
        'ojdnd': '../../libs/@version@/js/oraclejet/js/libs/dnd-polyfill/dnd-polyfill-1.0.0.min',
        'signals': '../../libs/@version@/js/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../../libs/@version@/js/oraclejet/js/libs/crossroads/crossroads.min',
        'history': '../../libs/@version@/js/oraclejet/js/libs/history/history.iegte8.min',
        'text': '../../libs/@version@/js/oraclejet/js/libs/require/text',
        'promise': '../../libs/@version@/js/oraclejet/js/libs/es6-promise/promise-1.0.0.min',
        'require':'../../libs/@version@/js/oraclejet/js/libs/require/require',
        'dashboards': '.',
        'builder': './builder',
        'dfutil':'internaldfcommon/js/util/internal-df-util',
        'prefutil':'/emsaasui/uifwk/js/util/preference-util',
        'loggingutil':'/emsaasui/uifwk/js/util/logging-util',
        'mobileutil':'/emsaasui/uifwk/js/util/mobile-util',
        'uiutil':'internaldfcommon/js/util/ui-util',
        'idfbcutil':'internaldfcommon/js/util/internal-df-browser-close-util',
        'd3':'../../libs/@version@/js/d3/d3.min',
        'emsaasui':'/emsaasui',
        'emcta':'/emsaasui/emcta/ta/js',
        'emcla':'/emsaasui/emlacore/js',
        'emcsutl': '/emsaasui/uifwk/emcsDependencies/uifwk/js/util',
        'ckeditor': '../../libs/@version@/js/ckeditor/ckeditor',
        'uifwk': '/emsaasui/uifwk'
    },
    bundles: (window.DEV_MODE ? undefined : {
        'builder/builder.jet.partition': [
            'ojs/ojcore',
            'ojs/ojknockout',
            'ojs/ojmenu',
            'ojs/ojtree',
            'ojs/ojvalidation',
            'ojs/ojknockout-validation',
            'ojs/ojbutton',
            'ojs/ojselectcombobox',
            'ojs/ojpopup',
            'ojs/ojchart',
            'ojs/ojcomponents',   
            'ojs/ojcomponentcore',
            'ojs/ojdialog',
            'ojs/ojdatetimepicker',
            'ojs/ojmodel',
            'ojs/ojknockout-model',
            'ojs/ojtoolbar',
            'ojs/ojpagingcontrol',
            'ojs/ojeditablevalue',
            'ojs/ojdatetimepicker',
            'ojs/internal-deps/dvt/DvtChart',
            'ojs/ojdvt-base',
            'ojs/ojcheckboxset',
            'ojs/ojpopup',
            'ojs/ojpopupcore',
            'ojs/ojmessaging',
            'ojs/ojgauge',
            'knockout',
            'jquery',
            'ojL10n']
    }),
    // Shim configurations for modules that do not expose AMD
    shim: {
        'jquery': {
            exports: ['jQuery', '$']
        },
        'jqueryui': {
            deps: ['jquery']
        },
        'crossroads': {
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
                'ojtranslations/nls/ojtranslations': 'resources/nls/dashboardsUiMsg'
            }
        },
        text: {
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
 */
require(['knockout',
    'jquery',
    'dfutil',
    'uifwk/js/util/df-util',
    'loggingutil',
    'ojs/ojcore',
    'dashboards/widgets/autorefresh/js/auto-refresh',
    'dashboards/widgets/textwidget/js/textwidget',
    'dashboards/dashboardhome-impl',
    'jqueryui',
    'common.uifwk',
    'builder/builder.jet.partition',
    'builder/builder.core',
    'dashboards/dbstypeahead',
    'builder/dashboardset.toolbar.model',
    'builder/dashboardset.panels.model'
],
    function(ko, $, dfu, dfumodel, _emJETCustomLogger, oj, auto_refresh, textwidget, dashboardhome_impl) // this callback gets executed when all required modules are loaded
    {
        var logger = new _emJETCustomLogger();
        var logReceiver = dfu.getLogUrl();
        logger.initialize(logReceiver, 60000, 20000, 8, dfu.getUserTenant().tenantUser);
        // TODO: Will need to change this to warning, once we figure out the level of our current log calls.
        // If you comment the line below, our current log calls will not be output!
        logger.setLogLevel(oj.Logger.LEVEL_WARN);

        if (!ko.components.isRegistered('df-oracle-branding-bar')) {
            ko.components.register("df-oracle-branding-bar",{
                viewModel:{require:'/emsaasui/uifwk/js/widgets/brandingbar/js/brandingbar.js'},
                template:{require:'text!/emsaasui/uifwk/js/widgets/brandingbar/html/brandingbar.html'}
            });
        }
        if (!ko.components.isRegistered('df-widget-selector')) {
            ko.components.register("df-widget-selector",{
                viewModel:{require:'/emsaasui/uifwk/js/widgets/widgetselector/js/widget-selector.js'},
                template:{require:'text!/emsaasui/uifwk/js/widgets/widgetselector/html/widget-selector.html'}
            });
        }
        ko.components.register("df-datetime-picker",{
            viewModel: {require: '/emsaasui/uifwk/js/widgets/datetime-picker/js/datetime-picker.js'},
            template: {require: 'text!/emsaasui/uifwk/js/widgets/datetime-picker/html/datetime-picker.html'}
        });
        ko.components.register("df-auto-refresh",{
            viewModel:auto_refresh,
            template:{require:'text!./widgets/autorefresh/auto-refresh.html'}
        });
        ko.components.register("DF_V1_WIDGET_TEXT", {
            viewModel: textwidget,
            template: {require: 'text!./widgets/textwidget/textwidget.html'}
        });
            
        if (!ko.components.isRegistered('df-oracle-dashboard-list')) {
            ko.components.register("df-oracle-dashboard-list",{
                viewModel:dashboardhome_impl,
                template:{require:'text!/emsaasui/emcpdfui/dashboardhome.html'}
            });
        }
        
        function DashboardTitleModel(dashboard) {
            var self = this;
            var dfu_model = new dfumodel(dfu.getUserName(), dfu.getTenantName());
            self.builderTitle = dfu_model.generateWindowTitle(dashboard.name(), null, null, getNlsString("DBS_HOME_TITLE_DASHBOARDS"));
        }

        function DashboardsetHeaderViewModel() {
            var self = this;
            self.userName = dfu.getUserName();
            self.tenantName = dfu.getTenantName();
            self.appId = "Dashboard";
            self.brandingbarParams = {
                userName: self.userName,
                tenantName: self.tenantName,
                appId: self.appId,
                isAdmin:true
            };

            $("#headerWrapper").on("DOMSubtreeModified", function() {
                var height = $("#headerWrapper").height();
                if (!self.headerHeight)
                    self.headerHeight = height;
                if (self.headerHeight === height)
                    return;
                var $visibleHeaderBar = $(".dashboard-content:visible .head-bar-container");
                if ($visibleHeaderBar.length > 0) {
                    var $b = ko.dataFor($visibleHeaderBar[0]) && ko.dataFor($visibleHeaderBar[0]).$b;
                    $b && $b.triggerBuilderResizeEvent('header wrapper bar height changed');
                }
                self.headerHeight = height;
            });
        };
         
        var dsbId = dfu.getUrlParam("dashboardId");
        console.warn("TODO: validate valid dashboard id format");
//                oj.Logger.error("dashboardId is not specified or invalid. Redirect to dashboard error page", true);
//                window.location.href = "./error.html?invalidUrl=" + encodeURIComponent(window.location.href) + "&msg=DBS_ERROR_DASHBOARD_ID_NOT_FOUND_MSG";
       

        Builder.initializeFromCookie();

        $(document).ready(function () {

            var headerViewModel = new DashboardsetHeaderViewModel();
            ko.applyBindings(headerViewModel, $('#headerWrapper')[0]);

            Builder.loadDashboard(dsbId, function (dashboard) {
                
                var dashboardTitleModel = new DashboardTitleModel(dashboard);
                ko.applyBindings(dashboardTitleModel, $("title")[0]);
                var dashboardsetToolBarModel = new Builder.DashboardsetToolBarModel(dashboard);
                var dashboardsetPanelsModel = new Builder.DashboardsetPanelsModel(dashboardsetToolBarModel);
                ko.applyBindings(dashboardsetToolBarModel, document.getElementById('dbd-set-tabs'));
                ko.applyBindings(dashboardsetPanelsModel, document.getElementById('popUp-dialog'));
                dashboardsetToolBarModel.initializeDashboardset();
                $("#loading").hide();
                $('#globalBody').show();
            }, function(e) {
                console.log(e.errorMessage());
                if (e.errorCode && e.errorCode() === 20001) {
                    oj.Logger.error("Dashboard not found. Redirect to dashboard error page", true);
                    location.href = "./error.html?invalidUrl=" + encodeURIComponent(location.href);
                }
            });
        });
    }
);

// method to be called by page inside iframe (especially inside one page type dashboard)
function updateOnePageHeight(event) {
    if (event && event.data && event.data.messageType === 'onePageWidgetHeight') {
        onePageTile.height(event.data.height);
        console.log('one page tile height is set to ' + event.data.height);
        oj.Logger.log('one page tile height is set to ' + event.data.height);
    }
}

            
function truncateString(str, length) {
    if (str && length > 0 && str.length > length)
    {
        var _tlocation = str.indexOf(' ', length);
        if ( _tlocation <= 0 )
            _tlocation = length;
        return str.substring(0, _tlocation) + "...";
    }
    return str;
}


function getNlsString(key, args) {
    return oj.Translations.getTranslatedString(key, args);
}

function getDateString(isoString) {
    //console.log(isoString);
    if (isoString && isoString.length > 0)
    {
        var s = isoString.split(/[\-\.\+: TZ]/g);
        //console.log(s);
        if (s.length > 1)
        {
            return new Date(s[0], parseInt(s[1], 10) - 1, s[2], s[3], s[4], s[5], s[6]).toLocaleDateString();
        }
    }
    return "";
}

window.addEventListener("message", updateOnePageHeight, false);
