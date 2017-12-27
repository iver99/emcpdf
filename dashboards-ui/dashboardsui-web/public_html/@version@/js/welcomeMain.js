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
            }

            if (!ko.components.isRegistered('df-oracle-branding-bar')) {
                ko.components.register("df-oracle-branding-bar", {
                    viewModel: {require: 'uifwk/js/widgets/brandingbar/js/brandingbar'},
                    template: {require: 'text!uifwk/js/widgets/brandingbar/html/brandingbar.html'}
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

            function getNlsString(key, args) {
                return oj.Translations.getTranslatedString(key, args);
            }

            function TitleViewModel(){
               var self = this;
               self.landingHomeTitle = dfu_model.generateWindowTitle(getNlsString("LANDING_HOME_WINDOW_TITLE"), null, null, null);
           }

            var headerViewModel = new HeaderViewModel();
            var titleViewModel = new TitleViewModel();

            function landingHomeModel() {
                var self = this;
                
                self.brandingbarParams = headerViewModel.brandingbarParams;
                self.dashboardsUrl = "/emsaasui/emcpdfui/home.html";
                self.landingHomeUrls = null;
                self.baseUrl = "http://www.oracle.com/pls/topic/lookup?ctx=cloud&id=";
                self.gsID = "em_home_gs";
                self.videoID = "em_home_videos";
                self.getStartedUrl = self.baseUrl + self.gsID;
                self.videosUrl = self.baseUrl + self.videoID;
                self.communityUrl = "http://cloud.oracle.com/management";

                self.welcomeSlogan = getNlsString("LANDING_HOME_WELCOME_SLOGAN");
                self.APM = getNlsString("LANDING_HOME_APM");
                self.APMDesc = getNlsString("LANDING_HOME_APM_DESC");
                self.LA = getNlsString("LANDING_HOME_LA");
                self.LADesc = getNlsString("LANDING_HOME_LA_DESC");
                self.ITA = getNlsString("LANDING_HOME_ITA");
                self.ITADesc = getNlsString("LANDING_HOME_ITA_DESC");
                self.select = getNlsString("LANDING_HOME_SELECT");
                self.ITA_DB_Performance = getNlsString("LANDING_HOME_ITA_DB_PERFORMANCE");
                self.ITA_DB_Resource = getNlsString("LANDING_HOME_ITA_DB_RESOURCE");
                self.ITA_Middleware_Performance = getNlsString("LANDING_HOME_ITA_MIDDLEWARE_PERFORMANCE");
                self.ITA_Middleware_Resource = getNlsString("LANDING_HOME_ITA_MIDDLEWARE_RESOURCE");
                self.ITA_Server_Resource = getNlsString("LANDING_HOME_ITA_SERVER_RESOURCE");
                self.ITA_Application_Performance = getNlsString("LANDING_HOME_ITA_APPLICATION_PERFORMANCE");
                self.ITA_Avail_Analytics = getNlsString("LANDING_HOME_ITA_AVAIL_ANALYTICS");
                self.ITA_XA_Analytics = getNlsString("LANDING_HOME_ITA_XA_ANALYTICS"); 

                self.infraMonitoring = getNlsString("LANDING_HOME_INFRA_MONITORING");
                self.infraMonitoringDesc = getNlsString("LANDING_HOME_INFRA_MONITORING_DESC");

                self.dashboards = getNlsString("LANDING_HOME_DASHBOARDS");
                self.dashboardsDesc = getNlsString("LANDING_HOME_DASHBOARDS_DESC");

                self.compliance = getNlsString("LANDING_HOME_COMPLIANCE");
                self.complianceDesc = getNlsString("LANDING_HOME_COMPLIANCE_DESC");
                self.securityAnalytics = getNlsString("LANDING_HOME_SECURITY_ANALYTICS");
                self.securityAnalyticsDesc = getNlsString("LANDING_HOME_SECURITY_ANALYTICS_DESC");
                self.orchestration = getNlsString("LANDING_HOME_ORCHESTRATION");
                self.orchestrationDesc = getNlsString("LANDING_HOME_ORCHESTRATION_DESC");

                self.dataExplorers = getNlsString("LANDING_HOME_DATA_EXPLORERS");
                self.dataExplorersDesc = getNlsString("LANDING_HOME_DATA_EXPLORERS_DESC");
                self.dataExplorer = getNlsString("LANDING_HOME_DATA_EXPLORER");

                self.exploreDataLinkList = ko.observableArray();
                self.exploreDataInITA = ko.observableArray();

                self.learnMore = getNlsString("LANDING_HOME_LEARN_MORE");
                self.getStarted = getNlsString("LANDING_HOME_GET_STARTED_LINK");
                self.videos = getNlsString("LANDING_HOME_VIDEOS_LINK");
                self.community = getNlsString("LANDING_HOME_COMMUNITY_LINK");

                self.ITA_Type = "select";
                self.data_type = "select";

                self.showAPM = ko.observable(false);
                self.showLA = ko.observable(false);
                self.showITA = ko.observable(false);
                self.showDashboard = ko.observable(false);
                self.showDataExplorer = ko.observable(false);
                self.showLearnMore = ko.observable(false);
                
                self.showInfraMonitoring = ko.observable(false);
                self.showCompliance = ko.observable(false);
                self.showSecurityAnalytics = ko.observable(false);
                self.showOrchestration = ko.observable(false);
                
                self.getServiceUrls = function() {
                    dfu.getRegistrations(fetchServiceLinks, true, errorCallback);                
                };

                //get urls of databases and middleware
                self.getITAVerticalAppUrl = function(rel) {
                    var serviceName = "emcitas-ui-apps";
                    var version = "1.0";
                    var url = dfu_model.discoverUrl(serviceName, version, rel);
                    return url;
                };
                
                function errorCallback() {
                    self.showAPM(false);
                    self.showLA(false);
                    self.showITA(false);
                    self.showDashboard(true);
                    self.showDataExplorer(true);
                    self.showLearnMore(true);
                }
                
                function fetchServiceLinks(data) {
                    var landingHomeUrls = {};
                    var i;
                    
                    self.showDashboard(true);
                    self.showDataExplorer(true);
                    self.showLearnMore(true);
                    
                    if(data.cloudServices && data.cloudServices.length>0) {
                        var cloudServices = data.cloudServices;
                        var cloudServicesNum = cloudServices.length;
                        for(i=0; i<cloudServicesNum; i++) {
                            if(cloudServices[i].name === "APM") {
                                self.showAPM(true);
                            }
                            if(cloudServices[i].name === "LogAnalytics") {
                                self.showLA(true);
                            }
                            if(cloudServices[i].name === "ITAnalytics") {
                                self.showITA(true);
                            }
                            if(cloudServices[i].name === "Monitoring") {
                                self.showInfraMonitoring(true);
                            }
                            if(cloudServices[i].name === "Compliance") {
                                self.showCompliance(true);
                            }
                            if(cloudServices[i].name === "SecurityAnalytics") {
                                self.showSecurityAnalytics(true);
                            }
                            if(cloudServices[i].name === "Orchestration") {
                                self.showOrchestration(true);
                            }
                            landingHomeUrls[cloudServices[i].name] = cloudServices[i].href;
                        }
                    }
                    if(data.visualAnalyzers && data.visualAnalyzers.length>0) {
                        var dataExplorers = data.visualAnalyzers;
                        var dataExplorersNum = dataExplorers.length;
                        for(i=0; i<dataExplorersNum; i++) {
                            var originalName = dataExplorers[i].name;
                            dataExplorers[i].name = originalName.replace(/Visual Analyzer/i, '').replace(/^\s*|\s*$/g, '');
                            if(dataExplorers[i].serviceName === "LogAnalyticsUI"){
                                dataExplorers[i].name = getNlsString("LANDING_HOME_LOG_EXPLORER");
                            }else if(dataExplorers[i].serviceName === "TargetAnalytics"){
                                dataExplorers[i].name = getNlsString("LANDING_HOME_DATA_EXPLORER");
                            }
                            self.exploreDataLinkList.push(dataExplorers[i]);
                            self.exploreDataLinkList.sort(function(left,right){
                                return left.name<=right.name?-1:1;
                            });
                            landingHomeUrls[dataExplorers[i].name] = dataExplorers[i].href;
                            //change name of data explorer in ITA to "Data Explorer - Analyze" & "Data Explorer"

                            if(dataExplorers[i].serviceName === "emcitas-ui-apps") {
                                self.exploreDataInITA.push({id: 'ITA_Analyze', href: dataExplorers[i].href, name: self.dataExplorer+" - " +dataExplorers[i].name, serviceName: dataExplorers[i].serviceName, version: dataExplorers[i].version});
			    	landingHomeUrls[self.dataExplorer+" - " +dataExplorers[i].name] = dataExplorers[i].href;
                            }else if (dataExplorers[i].serviceName === "TargetAnalytics") {
                                var targetAnalytics = dataExplorers[i];
                                dfu.getSubscribedApps2WithEdition(
                                    //check service type. If there is v2/v3 tenant, do not show "Data Explorer" in ITA dropdown
                                    function(subscribedApps) {
                                       if(subscribedApps.applications) {
                                           if(dfu.isV1ServiceTypes(subscribedApps.applications)) {
                                                self.exploreDataInITA.push({id: 'ITA_Search', href: targetAnalytics.href, name: self.dataExplorer, serviceName: targetAnalytics.serviceName, version: targetAnalytics.version});
                                                landingHomeUrls[self.dataExplorer] = targetAnalytics.href;
                                           }
                                       }
                                    },
                                    function() {
                                        console.error("Failed to get subscribedApps info");
                                    });
                            }
                            //change name of data explorer in ITA starting with "Data Explorer - "
                        }
                    }
                    self.landingHomeUrls = landingHomeUrls;
                }
                self.getServiceUrls();

                self.openAPM = function() {
                    if(!self.landingHomeUrls) {
                        console.log("---fetching service links is not finished yet!---");
                        return;
                    }
                    oj.Logger.info('Trying to open APM by URL: ' + self.landingHomeUrls.APM);
                    if(self.landingHomeUrls.APM) {
                        window.location.href = cxtUtil.appendOMCContext(self.landingHomeUrls.APM);
                    }
                };
                self.openLogAnalytics = function (data, event) {
                    if(!self.landingHomeUrls) {
                        console.log("---fetching service links is not finished yet!---");
                        return;
                    }
                    oj.Logger.info('Trying to open Log Analytics by URL: ' + self.landingHomeUrls.LogAnalytics);
                    if (self.landingHomeUrls.LogAnalytics) {
                        window.location.href = cxtUtil.appendOMCContext(self.landingHomeUrls.LogAnalytics);
                    }

                };
                self.openITAnalytics = function(data, event) {
                    if (event.type === "click" || (event.type === "keypress" && event.keyCode === 13)) {
                        if(!self.landingHomeUrls) {
                            console.log("---fetching service links is not finished yet!---");
                            return;
                        }
                        oj.Logger.info('Trying to open IT Analytics by URL: ' + self.landingHomeUrls.ITAnalytics);
                        if(self.landingHomeUrls.ITAnalytics) {
                            window.location.href = cxtUtil.appendOMCContext(self.landingHomeUrls.ITAnalytics);
                        }
                    } else if (event.type === "keypress" && event.keyCode === 9) {  //keyboard handle for Firefox
                        if (event.shiftKey) {
                            if (event.target.id === "ITA_wrapper") {
                                $(event.target).blur();
                                $("#LA_wrapper").focus();
                            } else {
                                $(event.target).blur();
                                $("#ITA_wrapper").focus();
                            }
                        } else {
                            if(event.target.id==="ITA_wrapper") {
                                $(event.target).blur();
                                $("#ITA_options").focus();
                            }else {
                                $(event.target).blur();
                                $("#infra_mon_wrapper").focus();
                            }
                        }
                    }
                };
                self.ITAOptionChosen = function(event, data) {
                    var ITA_OPTION_MAP = {
                        "DB_perf": "verticalApplication.db-perf",
                        "DB_resource": "verticalApplication.db-resource",
                        "mw_perf": "verticalApplication.mw-perf",
                        "mw_resource": "verticalApplication.mw-resource",
                        "svr_resource": "verticalApplication.svr-resource",
                        "avail-analytics": "verticalApplication.avail-analytics",
                        "xa-analytics": "verticalApplication.xa-analytics",
                        "app_perf": "verticalApplication.app-perf-analytics"
                    };
                    var ITA_OPTION_NAME_MAP = {
                        "DB_perf": self.ITA_DB_Performance,
                        "DB_resource": self.ITA_DB_Resource,
                        "mw_perf": self.ITA_Middleware_Performance,
                        "mw_resource": self.ITA_Middleware_Resource,
                        "svr_resource": self.ITA_Server_Resource,
                        "avail-analytics": self.ITA_Avail_Analytics,
                        "xa-analytics": self.ITA_XA_Analytics,
                        "app_perf": self.ITA_Application_Performance
                    };
                    if(!self.landingHomeUrls) {
                        console.log("---fetching service links is not finished yet!---");
                        return;
                    }
                    if(data.value && data.value.indexOf("select")<0){
                        if(!self.landingHomeUrls[data.value]){
                            self.landingHomeUrls[data.value] = self.getITAVerticalAppUrl(ITA_OPTION_MAP[data.value]);
                        }
                        if(!self.landingHomeUrls[data.value]){
                            oj.Logger.error('Failed to open ' + data.value + '.');
                            //show error message to user when the link is empty
                            dfu.showMessage({
                                type: 'error',
                                summary: getNlsString('LANDING_HOME_URL_ERROR_MSG', ITA_OPTION_NAME_MAP[data.value]),
                                detail: '',
                                removeDelayTime: 5000
                            });
                        }else{
                            oj.Logger.info('Trying to open ' + data.value + ' by URL: ' + self.landingHomeUrls[data.value]);
                            window.location.href = cxtUtil.appendOMCContext(self.landingHomeUrls[data.value]);
                        }
                    }
                };
                self.openInfraMonitoring = function() {
                    if (self.landingHomeUrls && self.landingHomeUrls.Monitoring) {
                        oj.Logger.info("Trying to open Infrastructure Monitoring by URL: " + self.landingHomeUrls.Monitoring);
                        window.location.href = cxtUtil.appendOMCContext(self.landingHomeUrls.Monitoring);
                    }
                };
                self.openDashboards = function() {
                    oj.Logger.info('Trying to open dashboards by URL: ' + self.dashboardsUrl);
                    if(self.dashboardsUrl) {
                        window.location.href = cxtUtil.appendOMCContext(self.dashboardsUrl);
                    }
                };
                self.openCompliance = function() {
                    if(!self.landingHomeUrls) {
                        console.log("---fetching service links is not finished yet!---");
                        return;
                    }
                    oj.Logger.info('Trying to open Compliance by URL: ' + self.landingHomeUrls.Compliance);
                    if(self.landingHomeUrls.Compliance) {
                        window.location.href = cxtUtil.appendOMCContext(self.landingHomeUrls.Compliance);
                    }
                };
                self.openSecurityAnalytics = function() {
                    if(!self.landingHomeUrls) {
                        console.log("---fetching service links is not finished yet!---");
                        return;
                    }
                    if(self.landingHomeUrls.SecurityAnalytics) {
                        oj.Logger.info('Trying to open Security Analytics by URL: ' + self.landingHomeUrls.SecurityAnalytics);
                        window.location.href = cxtUtil.appendOMCContext(self.landingHomeUrls.SecurityAnalytics);
                    }
                };
                self.openOrchestration = function() {
                    if(!self.landingHomeUrls) {
                        console.log("---fetching service links is not finished yet!---");
                        return;
                    }
                    oj.Logger.info('Trying to open Orchestration by URL: ' + self.landingHomeUrls.Orchestration);
                    if(self.landingHomeUrls.Orchestration) {
                        window.location.href = cxtUtil.appendOMCContext(self.landingHomeUrls.Orchestration);
                    }
                };
                self.dataExplorersChosen = function (event, data) {
                    if(!self.landingHomeUrls) {
                        console.log("---fetching service links is not finished yet!---");
                        return;
                    }
                    if (data.value && self.landingHomeUrls[data.value]) {
                        oj.Logger.info('Trying to open ' + data.value + ' by URL: ' + self.landingHomeUrls[data.value]);
                        window.location.href = cxtUtil.appendOMCContext(self.landingHomeUrls[data.value]);
                    }
                };
                self.openGetStarted = function() {
                    oj.Logger.info('Trying to open get started page by URL: ' + self.getStartedUrl);
                    if(self.getStartedUrl) {
                        window.open(self.getStartedUrl, "_blank");
                    }
                };
                self.openVideos = function() {
                    oj.Logger.info('Trying to open Videos by URL: ' + self.videosUrl);
                    if(self.videosUrl) {
                        window.open(self.videosUrl, "_blank");
                    }
                };
                self.openCommunity = function() {
                    oj.Logger.info('Trying to open Management Cloud Community by URL: ' + self.communityUrl);
                    if(self.communityUrl) {
                        window.open(self.communityUrl, "_blank");
                    }
                };
            }

            $(document).ready(function () {
                ko.applyBindings({LOADING : getNlsString('DBS_HOME_LOADING')}, $('#loading')[0]);  //to make text binding on loading work
                dfu.getSubscribedApps2WithEdition(function(apps) {
                    if (apps && (!apps.applications || apps.applications.length === 0)) {
                        oj.Logger.error("Tenant subscribes to no service. Redirect to dashboard error page", true);
                        location.href = "./error.html?msg=DBS_ERROR_PAGE_NOT_FOUND_NO_SUBS_MSG";
                    }else {
                        ko.applyBindings(titleViewModel, $("title")[0]);
                        ko.applyBindings(new landingHomeModel(), document.getElementById("globalBody"));
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
