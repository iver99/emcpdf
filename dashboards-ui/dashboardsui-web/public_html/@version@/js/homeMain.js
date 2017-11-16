/**
 * @preserve Copyright (c) 2014, Oracle and/or its affiliates.
 * All rights reserved.
 */

requirejs.config({
    bundles: function() {
        if ((window.DEV_MODE !==null && typeof window.DEV_MODE ==="object") ||
                (window.gradleDevMode !==null && typeof window.gradleDevMode ==="boolean")) {
            return {};
	}
        var versionedUifwkPartition = window.getSDKVersionFile ? window.getSDKVersionFile("emsaasui/uifwk/js/uifwk-partition") : "uifwk/js/uifwk-partition";
        var bundles = {};
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
            'text!uifwk/js/widgets/aboutbox/html/aboutbox.html',
            'text!uifwk/js/widgets/navlinks/html/navigation-links.html',
            'text!uifwk/js/widgets/brandingbar/html/brandingbar.html',
            'text!uifwk/js/widgets/timeFilter/html/timeFilter.html',
            'text!uifwk/js/widgets/datetime-picker/html/datetime-picker.html'
            ];
        return bundles; 
    }(),
    //Set up module mapping
    map: {
        '*':
              {'prefutil':'uifwk/js/util/preference-util'}
    },
    // Path mappings for the logical module names
    paths: {
        'knockout': '../../libs/@version@/js/oraclejet/js/libs/knockout/knockout-3.4.0',
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
        'dfutil':'internaldfcommon/js/util/internal-df-util',
        'uifwk': '/emsaasui/uifwk',
        'emsaasui':'/emsaasui',
        'emcta':'/emsaasui/emcta/ta/js'
    },
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
require(['dashboards/dbsmodel',
    'knockout',
    'jquery',
    'ojs/ojcore',
    'dfutil',
    'uifwk/js/util/df-util',
    'dashboards/dashboardhome-impl',
    'uifwk/js/util/logging-util',
    'uifwk/js/sdk/menu-util',
    'common.uifwk',
    'ojs/ojmodel',
    'ojs/ojknockout',
    'ojs/ojknockout-model',
    'ojs/ojcomponents',
    'ojs/ojvalidation',
    'ojs/ojbutton',
    'ojs/ojinputtext',
    'ojs/ojknockout-validation',
    'ojs/ojpopup',
    'dashboards/dbstypeahead',
    'dashboards/dbsdashboardpanel',
    'ojs/ojselectcombobox',
    'ojs/ojmenu',
    'ojs/ojtable'
],
        function(model, ko, $, oj, dfu, dfumodel, dashboardhome_impl, _emJETCustomLogger, menuModel) // this callback gets executed when all required modules are loaded
        {
            var logger = new _emJETCustomLogger();
            var logReceiver = dfu.getLogUrl();
                logger.initialize(logReceiver, 60000, 20000, 8, dfu.getUserTenant().tenantUser);
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
            };


            if (!ko.components.isRegistered('df-oracle-branding-bar')) {
                ko.components.register("df-oracle-branding-bar",{
                    viewModel:{require:'uifwk/js/widgets/brandingbar/js/brandingbar'},
                    template:{require:'text!uifwk/js/widgets/brandingbar/html/brandingbar.html'}
                });
            }

            if (!ko.components.isRegistered('df-oracle-dashboard-list')) {
                ko.components.register("df-oracle-dashboard-list",{
                    viewModel:dashboardhome_impl,
                    template:{require:'text!/emsaasui/emcpdfui/@version@/html/dashboardhome.html'}
                });
            }
            ko.bindingHandlers.stopBinding = {
                init: function () {
                    return {controlsDescendantBindings: true};
                }
            };
            ko.virtualElements.allowedBindings.stopBinding = true;

            var dfu_model = new dfumodel(dfu.getUserName(), dfu.getTenantName());
            var menuUtil = new menuModel();
            var filterId = dfu_model.getUrlParam('filter');
            var defaultMenuId = menuUtil.OMCMenuConstants.GLOBAL_DASHBOARDS;
            if (filterId === 'ocs') {
                defaultMenuId = menuUtil.OMCMenuConstants.GLOBAL_ORCHESTRATION;
            } 
            else if (filterId === 'ita') {
                defaultMenuId = menuUtil.OMCMenuConstants.GLOBAL_ITANALYTICS;
            }
            
            function resizeHomeMainLayout(hbgmenuStatus) {
                if (hbgmenuStatus === 'open') {
                    $('.main-content-area').removeClass('dbs-main-large-width');
                }
                else if (hbgmenuStatus === 'close') {
                    if ($('.main-content-area')) {
                        $('.main-content-area').addClass('dbs-main-large-width');
                    }
                }
            }
            
            menuUtil.subscribeHamburgerMenuToggleEvent(function(toggleType) {
                resizeHomeMainLayout(toggleType);
            });
            
            $(window).resize(function() {
                var hbgmenuStatus = $("#omcHamburgerMenu").length > 0 && $("#omcHamburgerMenu").is(':visible') ? 'open' : 'close';
                resizeHomeMainLayout(hbgmenuStatus);
            });
            
            function HeaderViewModel() {
                var self = this;
                self.userName = dfu.getUserName();
                self.tenantName = dfu.getTenantName();
                self.appId = "Dashboard";
                self.brandingbarParams = {
                    userName: self.userName,
                    tenantName: self.tenantName,
                    appId: self.appId,
                    isAdmin:true,
                    showGlobalContextBanner: false,
                    omcHamburgerMenuOptIn: true,
                    omcCurrentMenuId: defaultMenuId
                };
            }

           function TitleViewModel(){
               var self = this;
               self.homeTitle = dfu_model.generateWindowTitle(getNlsString("DBS_HOME_TITLE_HOME"), null, null, getNlsString("DBS_HOME_TITLE_DASHBOARDS"));
           }
            var headerViewModel = new HeaderViewModel();
            var titleVM = new TitleViewModel();

            $(document).ready(function() {
                resizeHomeMainLayout(menuUtil.showHamburgerMenuByDefault() ? 'open' : 'close');
                dfu.getSubscribedApps2WithEdition(function(apps) {
                    if (apps && (!apps.applications || apps.applications.length === 0)) {
                        oj.Logger.error("Tenant subscribes to no service. Redirect to dashboard error page", true);
                        location.href = "./error.html?msg=DBS_ERROR_PAGE_NOT_FOUND_NO_SUBS_MSG";
                    }else {
                        ko.applyBindings({}, $('#loading')[0]);  //to make text binding on loading work
                        ko.applyBindings(titleVM,$("title")[0]);
                        //Caution: need below line to enable KO binding, otherwise KOC inside headerWrapper doesn't work
                        ko.applyBindings(headerViewModel, document.getElementById('headerWrapper'));
                        $("#loading").hide();
                        $('#globalBody').show();

                        var predataModel = new model.PredataModel();
                        function init() {
                            var dashboardsViewModle = new model.ViewModel(predataModel, "mainContent");
                            ko.applyBindings(dashboardsViewModle, document.getElementById('mainContent'));
                            $('#mainContent').show();

                        }
                        predataModel.loadAll().then(init, init); //nomatter there is error in predata loading, initiating
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
                
                

            });
        }
);

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
            return new Date(s[0], parseInt(s[1], 10) - 1, s[2], s[3], s[4], s[5], s[6]).toLocaleDateString(document.documentElement.lang);
        }
    }
    return "";
}

