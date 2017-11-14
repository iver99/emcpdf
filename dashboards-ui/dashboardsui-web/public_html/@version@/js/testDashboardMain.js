/**
 * Example of Require.js boostrap javascript
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
    // Path mappings for the logical module names
    paths: {
        'knockout': '../../libs/@version@/js/oraclejet/js/libs/knockout/knockout-3.4.0',
        'jquery': '../../libs/@version@/js/oraclejet/js/libs/jquery/jquery-2.1.3.min',
        'jqueryui': '../../libs/@version@/js/oraclejet/js/libs/jquery/jquery-ui-1.11.4.custom.min',
        'jqueryui-amd': '../../libs/@version@/js/oraclejet/js/libs/jquery/jqueryui-amd-1.11.4.min',
        'promise': '../../libs/@version@/js/oraclejet/js/libs/es6-promise/promise-1.0.0.min',
        'require':'../../libs/@version@/js/oraclejet/js/libs/require/require',
        'hammerjs': '../../libs/@version@/js/oraclejet/js/libs/hammer/hammer-2.0.4.min',
        'ojs': '../../libs/@version@/js/oraclejet/js/libs/oj/v2.0.2/min',
        'ojL10n': '../../libs/@version@/js/oraclejet/js/libs/oj/v2.0.2/ojL10n',
        'ojtranslations': '../../libs/@version@/js/oraclejet/js/libs/oj/v2.0.2/resources',
        'ojdnd': '../../libs/@version@/js/oraclejet/js/libs/dnd-polyfill/dnd-polyfill-1.0.0.min',
        'signals': '../../libs/@version@/js/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../../libs/@version@/js/oraclejet/js/libs/crossroads/crossroads.min',
        'text': '../../libs/@version@/js/oraclejet/js/libs/require/text',
        'dfutil': 'internaldfcommon/js/util/internal-df-util',
        'uifwk': '/emsaasui/uifwk',
        'emsaasui':'/emsaasui',
        'emcta':'/emsaasui/emcta/ta/js'
    },
    // Shim configurations for modules that do not expose AMD
    shim: {
        'jquery': {
            exports: ['jQuery', '$']
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
require(['ojs/ojcore',
    'knockout',
    'jquery',
    'dfutil',
    'uifwk/js/util/df-util',
    'uifwk/js/util/logging-util',
    'uifwk/js/sdk/context-util',
    'uifwk/js/sdk/menu-util',
    'ojs/ojknockout',
    'ojs/ojselectcombobox',
    'common.uifwk'
],
        function (oj, ko, $, dfu, dfumodel, _emJETCustomLogger, cxtModel, menuModel) // this callback gets executed when all required modules are loaded
        {
            var dfu_model = new dfumodel(dfu.getUserName(), dfu.getTenantName());
            var cxtUtil = new cxtModel();
            var menuUtil = new menuModel();
            var logger = new _emJETCustomLogger();
            var logReceiver = dfu.getLogUrl();

            logger.initialize(logReceiver, 60000, 20000, 8, dfu.getUserTenant().tenantUser);
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
                ko.components.register('df-oracle-branding-bar', {
                    viewModel: {require: 'uifwk/js/widgets/brandingbar/js/brandingbar'},
                    template: {require: 'text!uifwk/js/widgets/brandingbar/html/brandingbar.html'}
                });
            }

            if (!ko.components.isRegistered('EMCPDF_HTMLWIDGET_V1')) {
                ko.components.register('EMCPDF_HTMLWIDGET_V1', {
                    viewModel: {require: './widgets/htmlwidget/js/htmlwidget'},
                    template: {require: 'text!widgets/htmlwidget/htmlwidget.html'}
                });
            }

            function HeaderViewModel() {
                var self = this;
                self.userName = dfu.getUserName();
                self.tenantName = dfu.getTenantName();
                self.appId = "Dashboard";
                self.brandingbarParams = {
                    userName: self.userName,
                    tenantName: self.tenantName,
                    appId: self.appId,
                    isAdmin: true,
                    showGlobalContextBanner: false,
                    omcHamburgerMenuOptIn: true,
                    omcCurrentMenuId: menuUtil.OMCMenuConstants.GLOBAL_HOME
                };
            }

            function TitleViewModel(){
               var self = this;
               self.testDashboardTestTitle = 'Dashboard Test Page';
           }

            var headerViewModel = new HeaderViewModel();
            var titleViewModel = new TitleViewModel();

            function TestDashboardModel() {
                var self = this;

                self.brandingbarParams = headerViewModel.brandingbarParams;
            }

            function TestHTMLWidgetModel(){
                var self = this;
                
                self.htmlWdgtTestSrc = '<DIV>哈哈哈哈</div>';
            }

            $(document).ready(function () {
                ko.applyBindings({LOADING : getNlsString('DBS_HOME_LOADING')}, $('#loading')[0]);  //to make text binding on loading work
                dfu.getSubscribedApps2WithEdition(function(apps) {
                    if (apps && (!apps.applications || apps.applications.length === 0)) {
                        oj.Logger.error("Tenant subscribes to no service. Redirect to dashboard error page", true);
                        location.href = "./error.html?msg=DBS_ERROR_PAGE_NOT_FOUND_NO_SUBS_MSG";
                    }else {
                        ko.applyBindings(titleViewModel, $("title")[0]);
                        ko.applyBindings(headerViewModel, $('#headerWrapper')[0]);
                        ko.applyBindings(new TestHTMLWidgetModel(), $('#test-html-widget-container')[0]);
                        $("#loading").hide();
                        $("#globalBody").show();
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

function getNlsString(key, args) {
    return oj.Translations.getTranslatedString(key, args);
}
