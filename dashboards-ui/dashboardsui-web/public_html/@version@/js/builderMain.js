/**
 * @preserve Copyright (c) 2014, Oracle and/or its affiliates.
 * All rights reserved.
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
        'jqueryui': '../../libs/@version@/js/oraclejet/js/libs/jquery/jquery-ui-1.11.4.custom.min',
        'jqueryui-amd':'../../libs/@version@/js/oraclejet/js/libs/jquery/jqueryui-amd-1.11.4.min',
        'hammerjs': '../../libs/@version@/js/oraclejet/js/libs/hammer/hammer-2.0.4.min',
        'ojs': '../../libs/@version@/js/oraclejet/js/libs/oj/v2.0.2/min',
        'ojL10n': '../../libs/@version@/js/oraclejet/js/libs/oj/v2.0.2/ojL10n',
        'ojtranslations': '../../libs/@version@/js/oraclejet/js/libs/oj/v2.0.2/resources',
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
        'uiutil':'internaldfcommon/js/util/ui-util',
        'idfbcutil':'internaldfcommon/js/util/internal-df-browser-close-util',
        'd3':'../../libs/@version@/js/d3/d3.min',
        'emsaasui':'/emsaasui',
        'emcta':'/emsaasui/emcta/ta/js',
//        'emcta': '/emsaasui/emcta/ta/@version@/js', //for DEV_MODE
        'emcla':'/emsaasui/emlacore/js',
        'emcsutl': '/emsaasui/uifwk/emcsDependencies/uifwk/js/util', // why we need this?
        'uifwk': '/emsaasui/uifwk',
        'ckeditor': '../../libs/@version@/js/ckeditor/ckeditor'
    },
    bundles: function() {
        if (window.DEV_MODE !==null && typeof window.DEV_MODE ==="object") {
            // we init bootstrap data part variables for local machine DEV mode here
            // for normal mode or gradle dev mode, $.Deferred() and resolve() will be injected into html
            window.dfBootstrapDataReceived  = $.Deferred();
            window.dfBootstrapDataReceived.resolve();
            return {};
	    }
        if (window.gradleDevMode !==null && typeof window.gradleDevMode ==="boolean") {
            return {};
	    }
        var bundles = {'builder/builder.jet.partition': [
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
            'ojs/internal-deps/dvt/DvtChart',
            'ojs/ojdvt-base',
            'ojs/ojcheckboxset',
            'ojs/ojpopupcore',
            'ojs/ojmessaging',
            'ojs/ojgauge',
            'ojs/ojdatasource-common',
            'ojs/ojinputtext',
            'ojs/ojtabs',
            'ojs/ojcollapsible',
            'ojs/ojradioset',
            'ojs/ojpagingtabledatasource',
            'ojs/ojcomposite',
            'ojs/ojnavigationlist',
            'ojs/ojslider',
            'ojs/ojconveyorbelt',
            'ojs/ojindexer',
            'ojs/ojnbox',
            'ojs/ojsunburst',
            'ojs/ojinputnumber',
            'ojs/ojoffcanvas',
            'ojs/ojswipetoreveal',
            'ojs/ojaccordion',
            'ojs/ojcube',
            'ojs/ojpagingcontrol-model',	
            'ojs/ojswitch',
            'ojs/ojarraydatagriddatasource',
            'ojs/ojdatacollection-common',
            'ojs/ojjquery-hammer',
            'ojs/ojtable-model',
            'ojs/ojarraypagingdatasource',
            'ojs/ojdatagrid-model',
            'ojs/ojjsontreedatasource',
            'ojs/ojpagingdatagriddatasource',
            'ojs/ojtable',
            'ojs/ojarraytabledatasource',
            'ojs/ojdatagrid',
            'ojs/ojpictochart',
            'ojs/ojtagcloud',
            'ojs/ojthematicmap',
            'ojs/ojdiagram',
            'ojs/ojlegend',
            'ojs/ojtimeline',
            'ojs/ojlistview',
            'ojs/ojprogressbar',
            'ojs/ojcollectiondatagriddatasource',
            'ojs/ojdomscroller',
            'ojs/ojmasonrylayout',	
            'ojs/ojpulltorefresh',
            'ojs/ojtouchproxy',
            'ojs/ojcollectionpagingdatasource',
            'ojs/ojradiocheckbox',
            'ojs/ojtrain',
            'ojs/ojcollectiontabledatasource',
            'ojs/ojtree-model',
            'ojs/ojcollectiontreedatasource',
            'ojs/ojfilmstrip',
            'ojs/ojrouter',
            'ojs/ojflattenedtreedatagriddatasource',
            'ojs/ojmodule',
            'ojs/ojrowexpander',
            'ojs/ojtreemap',
            'ojs/ojflattenedtreetabledatasource',
            'ojs/ojmoduleanimations',
            'ojdnd',
            'promise',
            'knockout',
            'jquery',
            'ojL10n']};
        var versionedUifwkPartition = window.getSDKVersionFile ? window.getSDKVersionFile("emsaasui/uifwk/js/uifwk-partition") : "uifwk/js/uifwk-partition";
        bundles[versionedUifwkPartition] = [
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
            'uifwk/js/sdk/menu-util',
            'uifwk/js/widgets/aboutbox/js/aboutbox',
            'uifwk/js/widgets/brandingbar/js/brandingbar',
            'uifwk/js/widgets/datetime-picker/js/datetime-picker',
            'uifwk/js/widgets/navlinks/js/navigation-links',
            'uifwk/js/widgets/timeFilter/js/timeFilter',
            'uifwk/js/widgets/widgetselector/js/widget-selector',
            'text!uifwk/js/widgets/aboutbox/html/aboutbox.html',
            'text!uifwk/js/widgets/navlinks/html/navigation-links.html',
            'text!uifwk/js/widgets/brandingbar/html/brandingbar.html',
            'text!uifwk/js/widgets/timeFilter/html/timeFilter.html',
            'text!uifwk/js/widgets/datetime-picker/html/datetime-picker.html',
            'text!uifwk/js/widgets/widgetselector/html/widget-selector.html'
            ];
        return bundles;
    }(),
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
    'knockout.mapping',
    'uifwk/js/util/df-util',
    'uifwk/js/util/logging-util',
    'ojs/ojcore',
    /*'ojs/ojcomponentcore',
    'ojs/ojchart',
    'ojs/ojlegend',
    'ojs/ojprogressbar',
    'ojs/ojcomponents',
    'ojs/ojarraytabledatasource',*/
    'builder/builder.functions',
//    'builder/builder.jet.partition',
    'uifwk/js/util/df-util',
    'uifwk/js/util/logging-util',
//    'dashboards/dashboardhome-impl',
    'ojs/ojtabs',
    'jqueryui',
    'common.uifwk',
    'builder/builder.core',
    'dashboards/dbstypeahead',
    'builder/dashboardset.toolbar.model',
    'builder/dashboardset.panels.model',
    'builder/dashboardDataSource/dashboard.datasource'
],
    function(ko, $, dfu,km) {
        window.dfBootstrapDataReceived.done(function () {
            ko.mapping = km;
            var dsbId = dfu.getUrlParam("dashboardId");
            var dashboard = null;
            var mode = null, normalMode = null, tabletMode = null;
            var timeSelectorModel = null;
            // default targets for 'all entities',for both target selector shown or hidden scenarios
            var targets = ko.observable({"criteria":"{\"version\":\"1.0\",\"criteriaList\":[]}"});
            
            $(document).ready(function () {
                dfu.getSubscribedApps2WithEdition(function(apps) {
                    if (apps && (!apps.applications || apps.applications.length == 0)) {
                        oj.Logger.error("Tenant subscribes to no service. Redirect to dashboard error page", true);
                        location.href = "./error.html?msg=DBS_ERROR_PAGE_NOT_FOUND_NO_SUBS_MSG";
                    }
                }, function(e) {
                    console.log(e.responseText);
                    if (e.responseJSON && e.responseJSON.errorCode == 20002) {
                        oj.Logger.error("Tenant subscribes to no service. Redirect to dashboard error page", true);
                        location.href = "./error.html?msg=DBS_ERROR_PAGE_NOT_FOUND_NO_SUBS_MSG";
                    }
                    else {
                        oj.Logger.error("Failed to get tenant subscribed applications. Redirect to dashboard error page", true);
                        location.href = "./error.html?msg=DBS_ERROR_PAGE_NOT_FOUND_MSG";
                    }
                });

                Builder.initializeFromCookie();
                new Builder.DashboardDataSource().loadDashboardData(dsbId, function (kodb) {
                    dashboard = kodb;
                    var isUnderSet = ko.unwrap(dashboard.type) === "SET" ? true : false;;
                    normalMode = new Builder.NormalEditorMode();
                    tabletMode = new Builder.TabletEditorMode();

                    mode = Builder.isSmallMediaQuery() ? tabletMode : normalMode;
                    timeSelectorModel = new Builder.TimeSelectorModel();
                    Builder.eagerLoadDahshboardTilesAtPageLoad(dfu, ko, normalMode, tabletMode, mode, isUnderSet, timeSelectorModel, targets);

                    require(['uifwk/js/util/df-util',
                        'uifwk/js/util/logging-util',
                        'uifwk/js/sdk/menu-util',
//                        'dashboards/dashboardhome-impl',
                        'jqueryui',
                        'common.uifwk',
                        'builder/builder.core',
                        'dashboards/dbstypeahead',
                        'builder/dashboardset.toolbar.model',
                        'builder/dashboardset.panels.model',
                        'builder/dashboardDataSource/dashboard.datasource'
                    ],
                        function(dfumodel, _emJETCustomLogger, menuModel/*, dashboardhome_impl*/) // this callback gets executed when all required modules are loaded
                        {
                            var logger = new _emJETCustomLogger();
                            var menuUtil = new menuModel();
                            var logReceiver = dfu.getLogUrl();
                            //require(['emsaasui/emcta/ta/js/sdk/tgtsel/api/TargetSelectorUtils'], function(TargetSelectorUtils) {
                            //TargetSelectorUtils.registerComponents();
                            logger.initialize(logReceiver, 300000, 20000, 80, dfu.getUserTenant().tenantUser);
                            // TODO: Will need to change this to warning, once we figure out the level of our current log calls.
                            // If you comment the line below, our current log calls will not be output!
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

                            if (!ko.components.isRegistered('df-oracle-branding-bar')) {
                                ko.components.register("df-oracle-branding-bar",{
                                    viewModel:{require:'uifwk/js/widgets/brandingbar/js/brandingbar'},
                                    template:{require:'text!uifwk/js/widgets/brandingbar/html/brandingbar.html'}
                                });
                            }
                            if(!ko.components.isRegistered('df-datetime-picker')) {
                                ko.components.register("df-datetime-picker",{
                                    viewModel: {require: 'uifwk/js/widgets/datetime-picker/js/datetime-picker'},
                                    template: {require: 'text!uifwk/js/widgets/datetime-picker/html/datetime-picker.html'}
                                });
                            }
                            if (!ko.components.isRegistered('df-widget-selector')) {
                                ko.components.register("df-widget-selector",{
                                    viewModel:{require:'uifwk/js/widgets/widgetselector/js/widget-selector'},
                                    template:{require:'text!uifwk/js/widgets/widgetselector/html/widget-selector.html'}
                                });
                            }
                            /*ko.components.register("DF_V1_WIDGET_TEXT", {
                                viewModel: textwidget,
                                template: {require: 'text!./widgets/textwidget/textwidget.html'}
                            });*/

//                        if (!ko.components.isRegistered('df-oracle-dashboard-list')) {
//                            ko.components.register("df-oracle-dashboard-list",{
//                                viewModel:dashboardhome_impl,
//                                template:{require:'text!/emsaasui/emcpdfui/@version@/html/dashboardhome.html'}
//                            });
//                        }

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
				    isAdmin:true,
				    showGlobalContextBanner: ko.observable(false),
                                    omcHamburgerMenuOptIn: true,
                                    omcCurrentMenuId: menuUtil.OMCMenuConstants.GLOBAL_DASHBOARDS,
                                    showTimeSelector: ko.observable(false),
				    timeSelectorParams: {
				        startDateTime: ko.observable(null),
				        endDateTime: ko.observable(null),
				        timePeriod: ko.observable("LAST_14_DAY"),
                                        timePeriodsSet: "SHORT_TERM",
                                        enableLatestOnCustomPanel: ko.observable(true),
				        hideMainLabel: true,
				        callbackAfterApply: null
				    },
				    showEntitySelector: ko.observable(false),
				    entityContextParams: {
				        readOnly: false
				    }
			        };

                                function onElementHeightChange($node, callback){
                                    var lastHeight = $node.height(), newHeight;
                                    (function run(){
                                        newHeight = $node.height();
                                        if( lastHeight != newHeight ) {
                                            callback();
                                        }
                                        lastHeight = newHeight;

                                        if( self.onElementHeightChangeTimer )
                                            clearTimeout(self.onElementHeightChangeTimer);

                                        self.onElementHeightChangeTimer = setTimeout(run, 1000);
                                    })();
                                };

                                onElementHeightChange($("#headerWrapper"), function() {
                                    var height = $("#headerWrapper").height();
                                    if (!self.headerHeight){
                                        self.headerHeight = height;
                                    }
                                    if (self.headerHeight === height){
                                        return;
                                    }
                                    var $visibleHeaderBar = $(".dashboard-content:visible .head-bar-container");
                                    var $visibleRightDrawer = $(".dbd-left-panel:visible");
                                    if ($visibleHeaderBar.length > 0 && ko.dataFor($visibleHeaderBar[0])) {
                                        var $b = ko.dataFor($visibleHeaderBar[0]).$b;
                                        $b && $b.triggerBuilderResizeEvent('header wrapper bar height changed');
                                    }
                                    if ($visibleRightDrawer.length > 0 && ko.dataFor($visibleRightDrawer[0])) {
                                        var $b = ko.dataFor($visibleRightDrawer[0]).$b;
                                        $b && $b.triggerBuilderResizeEvent('header wrapper bar height changed, right drawer resized');
                                    }
                                    self.headerHeight = height;
                                });
                            };

                            var dsbId = dfu.getUrlParam("dashboardId");
                            console.warn("TODO: validate valid dashboard id format");

                            ko.bindingHandlers.stopDataBinding = {
                                init: function(elem, valueAccessor) {
                                        var value = ko.unwrap(valueAccessor());
                                        return {controlsDescendantBindings: value};
                                }
                            };

                            //Builder.initializeFromCookie();

                            //$(document).ready(function () {
                                //Check if uifwk css file has been loaded already or not, if not then load it
                                if (!$('#dashboardMainCss').length) {
                                    //Append uifwk css file into document head
                                    $('head').append('<link id="dashboardMainCss" rel="stylesheet" href="/emsaasui/emcpdfui/@version@/css/dashboards-main.css" type="text/css"/>');
                                }

                                var headerViewModel = new DashboardsetHeaderViewModel();
                                ko.applyBindings({}, $('#loading')[0]);  //to make text binding on loading work
                                ko.applyBindings(headerViewModel, $('#headerWrapper')[0]);

                                //new Builder.DashboardDataSource().loadDashboardData(dsbId, function (dashboard) {
                                    var targetSelectorNeeded;
                                    if(dashboard.enableEntityFilter&&(dashboard.enableEntityFilter()==="TRUE" || dashboard.enableEntityFilter()==="GC")) {
                                        targetSelectorNeeded = true;
                                    }else {
                                        targetSelectorNeeded = false;
                                    }
                                    Builder.requireTargetSelectorUtils(targetSelectorNeeded, function(TargetSelectorUtils) {
                                        if (TargetSelectorUtils) {
                                            TargetSelectorUtils.registerComponents();
                                        }
                                        var dashboardTitleModel = new DashboardTitleModel(dashboard);
                                        ko.applyBindings(dashboardTitleModel, $("title")[0]);
                                        var dashboardsetToolBarModel = new Builder.DashboardsetToolBarModel(dashboard);
                                        var dashboardsetPanelsModel = new Builder.DashboardsetPanelsModel(dashboardsetToolBarModel);
                                        ko.applyBindings(dashboardsetToolBarModel, document.getElementById('dbd-set-tabs'));
                                        dashboardsetToolBarModel.initializeDashboardset();
                                        //Builder.attachEagerLoadedDahshboardTilesAtPageLoad();

                                        $("#loading").hide();
                                        $('#globalBody').show(function(){
                                            if(!$("#hamburgerButton")[0]){
                                                return; //hamburger menu disabled
                                            }
                                            var triggerBuilderResize = function(delayTime){
                                                var $b;
                                                if(!delayTime){
                                                    delayTime = 100;
                                                }
                                                $("#omcMenuNavList").addClass("df-computed-content-width");
                                                var $visibleHeaderBar = $(".dashboard-content:visible .head-bar-container");
                                                var $visibleRightDrawer = $(".dbd-left-panel:visible");
                                                if ($visibleHeaderBar.length > 0 && ko.dataFor($visibleHeaderBar[0])) {
                                                    $b = ko.dataFor($visibleHeaderBar[0]).$b;
                                                    $b && $b.triggerBuilderResizeEvent('hamburger menu show/hide status changed');
                                                }else if ($visibleRightDrawer.length > 0 && ko.dataFor($visibleRightDrawer[0])) {
                                                    $b = ko.dataFor($visibleRightDrawer[0]).$b;
                                                    $b && $b.triggerBuilderResizeEvent('hamburger menu show/hide status changed');
                                                }
                                                setTimeout(function(){
                                                    $b && $b.triggerBuilderResizeEvent('hamburger menu show/hide status changed');
                                                }, delayTime);
                                            };
                                            $("#omcHamburgerMenu").on("ojopen", function(event, offcanvas) {
                                                triggerBuilderResize();
                                            });
                                            $("#omcHamburgerMenu").on("ojclose", function (event, offcanvas) {
                                                triggerBuilderResize();
                                            });
                                            $(window).trigger('resize');    //initialize content page width when hamburger menu enabled
                                        });
                                    });
                                /*}, function(e) {
                                    console.log(e.errorMessage());
                                    if (e.errorCode && e.errorCode() === 20001) {
                                        oj.Logger.error("Dashboard not found. Redirect to dashboard error page", true);
                                        location.href = "./error.html?invalidUrl=" + encodeURIComponent(location.href);
                                    }
                                });*/
                            //});
                            //});
                        }
                    );

                }, function(e) {
                    console.log(e.errorMessage());
                    if (e.errorCode && e.errorCode() === 20001) {
                        oj.Logger.error("Dashboard not found. Redirect to dashboard error page", true);
                        location.href = "./error.html?invalidUrl=" + encodeURIComponent(location.href);
                    }
                });
            });
        });
    }
);

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
        if ( _tlocation <= 0 ){
            _tlocation = length;
        }
        return str.substring(0, _tlocation) + "...";
    }
    return str;
}


function getNlsString(key, args) {
    return oj.Translations.getTranslatedString(key, args);
}

function getDateString(isoString) {
    if (isoString && isoString.length > 0)
    {
        var s = isoString.split(/[\-\.\+: TZ]/g);
        if (s.length > 1)
        {
            return new Date(s[0], parseInt(s[1], 10) - 1, s[2], s[3], s[4], s[5], s[6]).toLocaleDateString();
        }
    }
    return "";
}

window.addEventListener("message", updateOnePageHeight, false);

