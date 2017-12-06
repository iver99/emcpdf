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
                var versionedViewModel = window.getSDKVersionFile ?
                        window.getSDKVersionFile('emsaasui/emcpdfui/js/widgets/htmlwidget/js/htmlwidget.js') : null;
                var kocVM = versionedViewModel ? (versionedViewModel.lastIndexOf('.js') === versionedViewModel.length - 3 ?
                        versionedViewModel.substring(0, versionedViewModel.length - 3) : versionedViewModel) :
                        'emsaasui/emcpdfui/js/widgets/htmlwidget/js/htmlwidget';
                var versionedTopoTemplate = window.getSDKVersionFile ?
                        window.getSDKVersionFile('emsaasui/emcpdfui/js/widgets/htmlwidget/htmlwidget.html') : null;
                var kocTemplate = versionedTopoTemplate ? versionedTopoTemplate :
                        'emsaasui/emcpdfui/js/widgets/htmlwidget/htmlwidget.html';
//                if (window.DEV_MODE !== null && typeof window.DEV_MODE === "object") {
//                    var _pre = '/emsaasui/emcpdfui/@version@/';
//                    kocVM = _pre + 'js/widgets/htmlwidget/js/htmlwidget.js';
//                    kocTemplate = _pre + 'js/widgets/htmlwidget/htmlwidget.html';
//                }
                ko.components.register('EMCPDF_HTMLWIDGET_V1', {
                    viewModel: {require: kocVM},
                    template: {require: 'text!' + kocTemplate}
                });
            }

            var dfu_model = new dfumodel(dfu.getUserName(), dfu.getTenantName());
            var menuUtil = new menuModel();
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
                
                self.htmlWdgtTestSrc1 = '<p>This dashboar<span style="color:#FF0000">d is int</span><span style="color:#FFA500">ende</span><span style="color:#FFFF00">d to pr</span><span style="color:#00FF00">ovide a</span>n overview of WLS errors in SaaS - Fusion Apps.</p><p>By default th<span style="font-size:18px">e time picker</span> to the right of this panel is set to one year and accor<strong>dingly all the char</strong>ts in this dashboards are displaying one year stats.<em> <strong>Such time spa</strong></em><strong>n</strong> can be manipulated using the timepicker and the charts will be adjusted automatically.<p><br/><br/><p>Use the links at the top of panel below to navigate to the details of a specific domain<p>';
                self.htmlWdgtTestSrc2 = '<img style="padding-top:52px" height="92" src="https://www.google.co.jp/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png" width="272" alt="Google" id="hplogo" title="Google" onload="typeof google===\'object\'&amp;&amp;google.aft&amp;&amp;google.aft(this)">';
                self.htmlWdgtTestSrc3 = '<a href="https://www.google.com">Vist Google</a>';
                self.htmlWdgtTestSrc4 = '<li class="box-list-li box-list-a"><a id="APM_wrapper" data-bind="attr: {href: \'javascript: this.click()\'}, click: openAPM" href="javascript: this.click()"><div class="service-box-wrapper"><div class="landing-home-box-img APM-box"><div class="landing-home-box-img-inner-container"><!--AppPerfMonitoring_w_92px--><img src="@version@/images/welcomeSprite.png" width="92" style="background: url(\'1.25.0-171121.171127/images/welcomeSprite.png\') no-repeat 0px -220px;" data-bind="attr: {alt: APM}" alt="Application Performance Monitoring"></div></div><div class="landing-home-box-content"><div class="landing-home-box-content-head" data-bind="text: APM">Application Performance Monitoring</div><div class="landing-home-box-content-desc" data-bind="text: APMDesc">Rapidly identify, response, and resolve your software roadblocks</div></div></div></a></li>';
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
