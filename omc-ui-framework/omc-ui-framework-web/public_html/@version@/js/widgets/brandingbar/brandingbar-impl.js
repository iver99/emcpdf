define('uifwk/@version@/js/widgets/brandingbar/brandingbar-impl', [
    'knockout',
    'jquery',
    'uifwk/@version@/js/util/df-util-impl',
    'uifwk/@version@/js/util/message-util-impl',
    'uifwk/@version@/js/sdk/context-util-impl',
    'ojs/ojcore',
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg',
    'uifwk/@version@/js/util/zdt-util-impl',
    'uifwk/@version@/js/sdk/menu-util-impl',
    'uifwk/@version@/js/sdk/SessionCacheUtil',
    'ojs/ojknockout',
    'ojs/ojtoolbar',
    'ojs/ojmenu',
    'ojs/ojbutton',
    'ojs/ojdialog',
    'ojs/ojoffcanvas'
],
    function (ko, $, dfumodel, msgUtilModel, contextModel, oj, nls, zdtUtilModel, menuModel, sessionCacheModel) {
        function BrandingBarViewModel(params) {
            var self = this;
            var msgUtil = new msgUtilModel();
            var cxtUtil = new contextModel();
            var menuStatusSessionCacheName = '_uifwk_hamburgermenustatuscache';
            var menuStatusSessionCacheDataKey = 'hamburger_menu_is_open';
            var menuStatusSessionCache = new sessionCacheModel(menuStatusSessionCacheName, 1);
            if (window.performance) {
                    //We should only clear the cache once during a page refresh, otherwise
                    //it may cause cached data lost though service menus already fetched
                    if (window.performance.navigation.type === 1) {
                        menuStatusSessionCache.clearCache();
                    }
                }
            function storeHamburgerMenuStatus(status){
                menuStatusSessionCache && menuStatusSessionCache.updateCacheData(menuStatusSessionCacheName, menuStatusSessionCacheDataKey, status);
            }
            function retrieveHmaburgerMenuStatus(){
                return menuStatusSessionCache && menuStatusSessionCache.retrieveDataFromCache(menuStatusSessionCacheName) && menuStatusSessionCache.retrieveDataFromCache(menuStatusSessionCacheName)[menuStatusSessionCacheDataKey];
            }
            var NO_HIGHLIGHT = 'NO_HIGHLIGHT';
            var CONTEXT_CHANGE = 'CONTEXT_CHANGE';
            // clear topologyParams first from global context
            cxtUtil.clearTopologyParams();

            self.compositeCxtText = ko.observable();
            self.entitiesList = ko.observableArray();
            self.timeCxtText = ko.observable();

            self.renderEmaasAppheaderGlobalNavMenu = ko.observable(false);

            self.userName = $.isFunction(params.userName) ? params.userName() : params.userName;
            self.tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
            self.isTopologyDisplayed = ko.observable(false);
            self.topologyDisabled = ko.observable(false);
            self.isTopologyCompRegistered = ko.observable(false);
            if (ko.isObservable(params.showGlobalContextBanner)) {
                self.showGlobalContextBanner = params.showGlobalContextBanner;
            } else {
                self.showGlobalContextBanner = ko.observable(ko.unwrap(params.showGlobalContextBanner) === false ? false : true);
            }

            self.entityContextParams = ko.unwrap(params.entityContextParams);
            if (self.entityContextParams) {
                if (ko.isObservable(self.entityContextParams.readOnly)) {
                    self.entityContextReadOnly = self.entityContextParams.readOnly;
                } else {
                    self.entityContextReadOnly = ko.observable(ko.unwrap(self.entityContextParams.readOnly) === false ? false : true);
                }
                if (ko.isObservable(self.entityContextParams.enableReadOnlyRemove)) {
                    self.showReadOnlyPillRemove = self.entityContextParams.enableReadOnlyRemove;
                } else {
                    self.showReadOnlyPillRemove = ko.observable(ko.unwrap(self.entityContextParams.enableReadOnlyRemove) === true ? true : false);
                }
            }
            if (!self.entityContextReadOnly) {
                self.entityContextReadOnly = ko.observable(true);
            }
            if (!self.showReadOnlyPillRemove) {
                self.showReadOnlyPillRemove = ko.observable(false);
            }
            self.showEntityContextSelector = ko.observable(false);
            self.isEntityContextLoaded = ko.observable(false);
            if (ko.isObservable(params.showEntitySelector)) {
                self.showEntitySelector = params.showEntitySelector;
            } else {
                self.showEntitySelector = ko.observable(ko.unwrap(params.showEntitySelector) === false ? false : true);
            }

            //respond to change to entityContextReadOnly
            self.entityContextReadOnly.subscribe(function () {
                if (!self.entityContextReadOnly()) {
                    self.showEntityContextSelector(true);
                    var versionedContextSelectorUtils = window.getSDKVersionFile ?
                        window.getSDKVersionFile('emsaasui/emcta/ta/js/sdk/contextSelector/api/ContextSelectorUtils') : null;
                    var contextSelectorUtil = versionedContextSelectorUtils ? versionedContextSelectorUtils :
                        '/emsaasui/emcta/ta/js/sdk/contextSelector/api/ContextSelectorUtils.js';

                    require([contextSelectorUtil], function (EmctaContextSelectorUtil) {
                        EmctaContextSelectorUtil.registerComponents();
                        self.isEntityContextLoaded(true);
                    });
                } else {
                    self.showEntityContextSelector(false);
                }
            });
            self.entityContextReadOnly.notifySubscribers();

            //Set showTimeSelector config. Default value is true. It can be set as an knockout observable and be changed after page is loaded
            if (ko.isObservable(params.showTimeSelector)) {
                self.showTimeSelector = params.showTimeSelector;
            } else {
                self.showTimeSelector = ko.observable(ko.unwrap(params.showTimeSelector) === true ? true : false);
            }
//            self.showTimeSelector = ko.observable(false);
            //
            // topology paramters
            //
            self.entities = ko.observable([]);
            self.queryVars = ko.observable();
            self.associations = ko.observable();
            self.layout = ko.observable();
            self.customNodeDataLoader = ko.observable();
            self.customEventHandler = ko.observable();
            self.miniEntityCardActions = ko.observable();
            self.highlightedEntities = ko.observableArray([NO_HIGHLIGHT]);
            self.topologyData = function (data) {
                self.udeTopologyData = data;
            };
            self.updateGlobalContextByTopologySelection = params.updateGlobalContextByTopologySelection;
            self.showEnterpriseTopology = true;
            if (params) {
                self.associations(params.associations);
                self.layout(params.layout);
                self.customNodeDataLoader(params.customNodeDataLoader);
                self.customEventHandler(params.customEventHandler);
                self.miniEntityCardActions(params.miniEntityCardActions);
            }

            var dfu = new dfumodel(self.userName, self.tenantName);
            //Append uifwk css file into document head
            dfu.loadUifwkCss();
            if (!ko.components.isRegistered('emctas-globalbar'))
            {
                var versionedTemplate = window.getSDKVersionFile ?
                    window.getSDKVersionFile('emsaasui/emcta/ta/js/sdk/globalcontextbar/emctas-globalbar.html') : null;
                var template = versionedTemplate ? versionedTemplate :
                    'emsaasui/emcta/ta/js/sdk/globalcontextbar/emctas-globalbar.html';
                ko.components.register('emctas-globalbar', {
                    viewModel: function () {
                    },
                    template: {require: 'text!' + template}
                });
            }
            self.emctasGlobalBarCallback = function () {
                //noop, this can be deleted once the reference to this method is removed
                //from emctas
            };

            if (self.showGlobalContextBanner() === true) {
                refreshOMCContext();
            }
            self.showGlobalContextBanner.subscribe(function (newValue) {
                if (newValue === true) {
                    //In case showGlobalContextBanner is initialized to false, and updated to true during page loading,
                    //we need to restore topology display status from window session storage
                    if (!self.isTopologyDisplayed()) {
                        restoreTopologyDisplayStatus();
                    }
                    refreshOMCContext();
                }
            });

            self.removeROCompositePill = function () {
                handleROPills('composite');
            };

            self.removeROEntityPill = function (data) {
                handleROPills('entity', data);
            };

            /**
             * Function prompts for context type to delete depending on the X button pressed.
             * If composite clicked, it calls clearCompositeContext() directly; if entity
             * RO clicked, it gets the entity array from context and slpices that object
             * before resetting it.
             * @param {type} context
             * @param {type} data
             * @returns {undefined}
             */
            function handleROPills(context, data) {
                if (context === 'composite') {
                    cxtUtil.clearCompositeContext();
                    return;
                } else if (context === 'entity') {
                    var toDelete = data.meId;
                    var entities = cxtUtil.getEntities();
                    for (var x = 0; x < entities.length; x++) {
                        if (entities[x].meId === toDelete) {
                            entities.splice(x, 1);
                            break;
                        }
                    }
                    if (entities.length === 0) {
                        cxtUtil.clearEntityContext();
                    } else {
                        cxtUtil.setEntityMeIds(entities.map(function (val) {
                            return val.meId;
                        }));
                    }
                }
            }
            ;

            function handleShowHideTopology() {
                if (self.topologyNeedRefresh && !self.isTopologyDisplayed()) {
                    //when expanding the topology, do a refresh if needed
                    console.log("**************topology displayed");
                    console.log("**************refreshTopologyParams");
                    refreshTopologyParams();
                }
                $("#ude-topology-div").slideToggle("fast", function () {
                    self.isTopologyDisplayed(!self.isTopologyDisplayed());

                    if (self.isTopologyDisplayed()) {
                        var entityMeIds = cxtUtil.getEntityMeIds();
                        if (entityMeIds && entityMeIds.length) {
                            self.highlightedEntities(entityMeIds.concat([CONTEXT_CHANGE, NO_HIGHLIGHT]));
                        } else {
                            self.highlightedEntities([NO_HIGHLIGHT]);
                        }
                        self.topologyInitialized = true;
                        //$(".ude-topology-in-brandingbar .oj-diagram").ojDiagram("refresh");
                    }
                    //set brandingbar_cache information for Topology expanded state
                    var brandingBarCache = {isTopologyDisplayed: self.isTopologyDisplayed()};
                    window.sessionStorage._uifwk_brandingbar_cache = JSON.stringify(brandingBarCache);
                    var $b = $(".right-panel-toggler:visible")[0] && ko.dataFor($(".right-panel-toggler:visible")[0]).$b;
                    $b && $b.triggerBuilderResizeEvent('OOB dashboard detected and hide right panel');
                    fireTopologyStatusChangeEvent(!self.isTopologyDisplayed() ? 'Close' : 'Open');
                });
            }

            function registerTopologyComponent(callback) {
                if (!self.isTopologyCompRegistered()) {
                    require(['ojs/ojdiagram'], function () {
                        if (!ko.components.isRegistered('emctas-topology')) {
                            var versionedTopoViewModel = window.getSDKVersionFile ?
                                window.getSDKVersionFile('emsaasui/emcta/ta/js/sdk/topology/emcta-topology.js') : null;
                            var topoViewModel = versionedTopoViewModel ? (versionedTopoViewModel.lastIndexOf('.js') === versionedTopoViewModel.length - 3 ?
                                versionedTopoViewModel.substring(0, versionedTopoViewModel.length - 3) : versionedTopoViewModel) :
                                'emsaasui/emcta/ta/js/sdk/topology/emcta-topology';
                            var versionedTopoTemplate = window.getSDKVersionFile ?
                                window.getSDKVersionFile('emsaasui/emcta/ta/js/sdk/topology/emcta-topology.html') : null;
                            var topoTemplate = versionedTopoTemplate ? versionedTopoTemplate :
                                'emsaasui/emcta/ta/js/sdk/topology/emcta-topology.html';
                            ko.components.register('emctas-topology', {
                                viewModel: {require: topoViewModel},
                                template: {require: 'text!' + topoTemplate}
                            });
                        }
                        self.isTopologyCompRegistered(true);
                        if ($.isFunction(callback)) {
                            callback();
                        }
                    });
                }
                else if ($.isFunction(callback)) {
                    callback();
                }
            }

            self.showTopology = function () { // listener to the button
                registerTopologyComponent(handleShowHideTopology);
                //handleShowHideTopology();
            };

            self.isTopologyButtonChecked = ko.observableArray([]);
            self.isTopologyDisplayed.subscribe(function () {
                if (self.isTopologyDisplayed()) {
                    self.isTopologyButtonChecked(['buttonShowTopology']);
                } else {
                    self.isTopologyButtonChecked([]);
                }
            });

            //Restore topology display status from window session storage
            restoreTopologyDisplayStatus();

            function restoreTopologyDisplayStatus() {
                if (window.sessionStorage._uifwk_brandingbar_cache) {
                    var brandingBarCache = JSON.parse(window.sessionStorage._uifwk_brandingbar_cache);
                    if (brandingBarCache && brandingBarCache.isTopologyDisplayed) {
                        if (self.showGlobalContextBanner()) {
                            registerTopologyComponent(function () {
                                refreshTopologyParams();
                                if (self.topologyDisabled() === false) {
                                    self.isTopologyDisplayed(true);
                                    fireTopologyStatusChangeEvent('Open');
                                }
                            });
                        }
                    }
                }
            }

            /*
             * Called by UDE if topology is maximized or restored
             */
            self.topologyResizeCallback = function (isMaximized) {
                var $b = $(".right-panel-toggler:visible")[0] && ko.dataFor($(".right-panel-toggler:visible")[0]).$b;
                if (isMaximized) {
                    $b && $b.triggerBuilderResizeEvent('Topology is maximized!');
                } else {
                    $b && $b.triggerBuilderResizeEvent('Topology is restored!');
                }
            };

            //NLS strings
            self.productName = nls.BRANDING_BAR_MANAGEMENT_CLOUD;
            self.preferencesMenuLabel = nls.BRANDING_BAR_MENU_PREFERENCES;
            self.helpMenuLabel = nls.BRANDING_BAR_MENU_HELP;
            self.aboutMenuLabel = nls.BRANDING_BAR_MENU_ABOUT;
            self.signOutMenuLabel = nls.BRANDING_BAR_MENU_SIGN_OUT;
            self.linkBtnLabel = nls.BRANDING_BAR_LINKS_BTN_LABEL;
            self.textOracle = nls.BRANDING_BAR_TEXT_ORACLE;
            self.textAppNavigator = nls.BRANDING_BAR_TEXT_APP_NAVIGATOR;
            self.toolbarLabel = nls.BRANDING_BAR_TOOLBAR_LABEL;
            self.textNotifications = nls.BRANDING_BAR_TEXT_NOTIFICATIONS;
            self.altTextError = nls.BRANDING_BAR_MESSAGE_BOX_ICON_ALT_TEXT_ERROR;
            self.altTextWarn = nls.BRANDING_BAR_MESSAGE_BOX_ICON_ALT_TEXT_WARN;
            self.altTextConfirm = nls.BRANDING_BAR_MESSAGE_BOX_ICON_ALT_TEXT_CONFIRM;
            self.altTextInfo = nls.BRANDING_BAR_MESSAGE_BOX_ICON_ALT_TEXT_INFO;
            self.altTextClear = nls.BRANDING_BAR_MESSAGE_BOX_ICON_ALT_TEXT_CLEAR;
            self.topologyBtnLabel = nls.BRANDING_BAR_GLOBAL_CONTEXT_TOPOLOGY;
            self.topologyMaximizeLabel = nls.BRANDING_BAR_TOPOLOGY_MAXIMIZE;
            self.topologyRestoreLabel = nls.BRANDING_BAR_TOPOLOGY_RESTORE;
            self.gcAllEntities = nls.BRANDING_BAR_GLOBAL_CONTEXT_ALL_ENTITIES;
            self.removePillTitle = nls.PILL_REMOVE_TITLE;
            self.appName = ko.observable();

            self.hasMessages = ko.observable(false);
            self.messageList = ko.observableArray();
            self.clearMessageIcon = "/emsaasui/uifwk/@version@/images/widgets/clearEntry_ena.png";
            var errorMessageIcon = "/emsaasui/uifwk/@version@/images/widgets/stat_error_16.png";
            var warnMessageIcon = "/emsaasui/uifwk/@version@/images/widgets/stat_warn_16.png";
            var confirmMessageIcon = "/emsaasui/uifwk/@version@/images/widgets/stat_confirm_16.png";
            var infoMessageIcon = "/emsaasui/uifwk/@version@/images/widgets/stat_info_16.png";
            var messageIconSprite = "/emsaasui/uifwk/@version@/images/uifwkSprite.png";
            var hiddenMessages = [];

            self.navLinksNeedRefresh = ko.observable(false);
            self.aboutBoxNeedRefresh = ko.observable(false);
            var dfWelcomeUrl = dfu.discoverWelcomeUrl();
            var subscribedApps = null;
            var appIdAPM = "APM";
            var appIdDBPerfDiag = "DBPerfDiag";
            var appIdITAnalytics = "ITAnalytics";
            var appIdLogAnalytics = "LogAnalytics";
            var appIdDashboard = "Dashboard";
            var appIdTenantManagement = "TenantManagement";
            var appIdError = "Error";
            self.SERVICE_VERSION = encodeURIComponent('1.0+');
            self.MONITORING_SERVICE_VERSION = encodeURIComponent('1.5+');
            self.COMPLIANCE_SERVICE_VERSION = encodeURIComponent(null);
            var appIdEventUI = "EventUI";
            var appIdMonitoring = "Monitoring";
            var appIdSecurityAnalytics = "SecurityAnalytics";
            var appIdCompliance = "Compliance";
            var appIdOcs = "Orchestration";
            var appMap = {};
            appMap[appIdAPM] = {
                "appId": "APM",
                "appName": "BRANDING_BAR_APP_NAME_APM",
                "serviceDisplayName": "BRANDING_BAR_CLOUD_SERVICE_NAME_APM",
                "serviceName": "ApmUI",
                "version": self.SERVICE_VERSION,
                "helpTopicId": "em_apm_gs"
            };
            appMap[appIdITAnalytics] = {
                "appId": "ITAnalytics",
                "appName": "BRANDING_BAR_APP_NAME_IT_ANALYTICS",
                "serviceName": "emcitas-ui-apps",
                "version": self.SERVICE_VERSION,
                "helpTopicId": "em_it_gs"
            };
            appMap[appIdDBPerfDiag] = {
                "appId": "DBPerfDiag",
                "appName": "BRANDING_BAR_APP_NAME_DB_PERF_DIAGNOSTICS",
                "serviceName": "emcitas-dbcsperf",
                "version": self.SERVICE_VERSION,
                "helpTopicId": "em_dbperfdiag_gs"
            };
            appMap[appIdLogAnalytics] = {
                "appId": "LogAnalytics",
                "appName": "BRANDING_BAR_APP_NAME_LOG_ANALYTICS",
                "serviceName": "LogAnalyticsUI",
                "version": self.SERVICE_VERSION,
                "helpTopicId": "em_log_gs"
            };
            appMap[appIdDashboard] = {
                "appId": "Dashboard",
                "appName": "BRANDING_BAR_APP_NAME_DASHBOARD",
                "serviceName": "Dashboard-UI",
                "version": self.SERVICE_VERSION,
                "helpTopicId": "em_home_gs"
            };
            appMap[appIdTenantManagement] = {
                "appId": "TenantManagement",
                "appName": "BRANDING_BAR_APP_NAME_TENANT_MANAGEMENT_UI",
                "serviceName": "TenantManagementUI",
                "version": self.SERVICE_VERSION,
                "helpTopicId": "em_home_gs"
            };
            appMap[appIdError] = {
                "appId": "Error",
                "appName": "",
                "serviceName": "Error",
                "version": self.SERVICE_VERSION,
                "helpTopicId": "em_home_gs"
            };
            appMap[appIdEventUI] = {
                "appId": "EventUI",
                "appName": "",
                "serviceName": "EventUI",
                "version": self.SERVICE_VERSION,
                "helpTopicId": ""
            };
            appMap[appIdMonitoring] = {
                "appId": appIdMonitoring,
                "appName": "BRANDING_BAR_APP_NAME_MONITORING",
                "serviceName": "MonitoringServiceUI",
                "version": self.MONITORING_SERVICE_VERSION,
                "helpTopicId": "em_moncs"
            };
            appMap[appIdSecurityAnalytics] = {
                "appId": appIdSecurityAnalytics,
                "appName": "BRANDING_BAR_APP_NAME_SECURITY_ANALYTICS",
                "serviceDisplayName": "BRANDING_BAR_APP_NAME_SECURITY_ANALYTICS",
                "serviceName": "SecurityAnalyticsUI",
                "version": self.SERVICE_VERSION,
                "helpTopicId": "em_samcs"
            };
            appMap[appIdCompliance] = {
                "appId": appIdCompliance,
                "appName": "BRANDING_BAR_APP_NAME_COMPLIANCE",
                "serviceDisplayName": "BRANDING_BAR_APP_SHORT_NAME_COMPLIANCE",
                "serviceName": "ComplianceUIService",
                "version": self.COMPLIANCE_SERVICE_VERSION,
                "helpTopicId": "em_compl"
            };
            appMap[appIdOcs] = {
                "appId": appIdOcs,
                "appName": "BRANDING_BAR_APP_NAME_ORCHESTRATION",
                "serviceDisplayName": "BRANDING_BAR_APP_NAME_ORCHESTRATION",
                "serviceName": "CosUIService",
                "version": self.SERVICE_VERSION,
                "helpTopicId": "em_home_gs"
            };

            var omcHamburgerMenuOptIn = $.isFunction(params.omcHamburgerMenuOptIn) ? params.omcHamburgerMenuOptIn() : params.omcHamburgerMenuOptIn;
            self.appId = $.isFunction(params.appId) ? params.appId() : params.appId;
            self.relNotificationCheck = $.isFunction(params.relNotificationCheck) ? params.relNotificationCheck() : params.relNotificationCheck;
            self.relNotificationShow = $.isFunction(params.relNotificationShow) ? params.relNotificationShow() : params.relNotificationShow;
            self.notificationVisible = ko.observable(false);
            self.notificationDisabled = ko.observable(true);
            self.notificationPageUrl = null;
            self.navLinksVisible = omcHamburgerMenuOptIn ? false : true;
            self.navLinksImmediateLoading = ko.observable(false);

            var isAppIdNotEmpty = self.appId && $.trim(self.appId) !== "";
            var appProperties = isAppIdNotEmpty && appMap[self.appId] ? appMap[self.appId] : {};

            var maxMsgDisplayCnt = $.isFunction(params.maxMessageDisplayCount) ? params.maxMessageDisplayCount() :
                (typeof (params.maxMessageDisplayCount) === "number" && params.maxMessageDisplayCount > 0 ?
                    params.maxMessageDisplayCount : 3);
            self.showMoreLinkTxt = nls.BRANDING_BAR_MESSAGE_BOX_TEXT_SHOW_MORE;
            self.showMoreLinkTitle = nls.BRANDING_BAR_MESSAGE_BOX_TITLE_SHOW_MORE;
            self.showFirstNOnlyTxt = msgUtil.formatMessage(nls.BRANDING_BAR_MESSAGE_BOX_TEXT_SHOW_FIRST, maxMsgDisplayCnt);
            self.showFirstNOnlyTitle = msgUtil.formatMessage(nls.BRANDING_BAR_MESSAGE_BOX_TITLE_SHOW_FIRST, maxMsgDisplayCnt);
            self.sessionTimeoutWarnDialogTitle = nls.BRANDING_BAR_SESSION_TIMEOUT_DIALOG_TILE;
            self.sessionTimeoutMsg = nls.BRANDING_BAR_SESSION_TIMEOUT_MSG;
            self.sessionTimeoutBtnOK = nls.BRANDING_BAR_SESSION_TIMEOUT_DIALOG_BTN_OK;
            self.sessionTimeoutWarnDialogId = 'sessionTimeoutWarnDialog';
            self.renderSessionTimeoutDialog = ko.observable(false);
            self.sessionTimeoutWarnCssStyle = "background:url('" + messageIconSprite + "') no-repeat 0px -46px;height:16px; width:16px; display: inline-block;";

            //Fetch and set sso logout url and session expiry time
            dfu.getRegistrations(function (data) {
                //Setup timer to handle session timeout
                if (!dfu.isDevMode()) {
                    dfu.setupSessionLifecycleTimeoutTimer(data.sessionExpiryTime, self.sessionTimeoutWarnDialogId);
                }

                if (data.ssoLogoutUrl) {
                    window.cachedSSOLogoutUrl = data.ssoLogoutUrl;
                }
            }, true, null);

            self.clearMessage = function (data, event) {
                removeMessage(data);
            };

//                //Get subscribed application names
//                getSubscribedApplications();
            refreshAppName();

            var urlNotificationCheck = null;
            var urlNotificationShow = null;

            self.checkNotificationAvailability = function () {
                oj.Logger.info("Start to check available notifications by URL:" + urlNotificationCheck, false);
                dfu.ajaxWithRetry(urlNotificationCheck, {
                    success: function (data, textStatus, jqXHR) {
                        oj.Logger.info("The count of available notifications is: " + data, false);
                        if (data && parseInt(data) > 0) {
                            if (self.relNotificationShow.indexOf("/") === 0) {
                                urlNotificationShow = self.relNotificationShow;
                            }
                            if (urlNotificationShow) {
                                self.notificationDisabled(false);
                                self.notificationPageUrl = urlNotificationShow;
                            }
                            else {
                                oj.Logger.warn("The notifications page URL (relNotificationShow) provided by current application is invalid: " + self.relNotificationShow, false);
                                self.notificationDisabled(true);
                            }
                        }
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        oj.Logger.error('There were errors while checking available notifications by URL: ' + urlNotificationCheck);
                        self.notificationDisabled(true);
                    }
                });
            };

            //Check notifications
            checkNotifications();

            //TODO:need to find a way to get exact idleTimeout settings in OAM and improve the idleTimeout handling
            //For now, set interval to extend current user session automatically every 10 mins
            if (!dfu.isDevMode()) {
                window.intervalToExtendCurrentUserSession = setInterval(function () {
                    dfu.ajaxWithRetry("/emsaasui/uifwk/@version@/html/empty.html", {showMessages: "none"});
                }, 10 * 60 * 1000);
            }

//                //Discover logout url, which will be cached and used for session timeout handling
//                dfu.discoverLogoutUrlAsync(function(logoutUrl){window.cachedSSOLogoutUrl = logoutUrl;});

            //SSO logout handler
            self.handleSignout = function () {
                //Clear interval for extending user session
                /* globals clearInterval*/
                if (window.intervalToExtendCurrentUserSession) {
                    clearInterval(window.intervalToExtendCurrentUserSession);
                }

                dfu.clearSessionCache();

                var ssoLogoutEndUrl = encodeURI(window.location.protocol + '//' + window.location.host + dfWelcomeUrl);
                var logoutUrlDiscovered = window.cachedSSOLogoutUrl ? window.cachedSSOLogoutUrl : dfu.discoverLogoutUrl();
                //If session timed out, redirect to sso login page and go to home page after re-login.
                if (window.currentUserSessionExpired === true && logoutUrlDiscovered === null) {
                    window.location.href = ssoLogoutEndUrl;
                }
                //Else handle normal logout
                else {
                    if (logoutUrlDiscovered === null) {
                        oj.Logger.error('SSO logout URL is not discovered. Sign Out may not work properly.');
//                            logoutUrlDiscovered = window.cachedSSOLogoutUrl;
                    }
                    var logoutUrl = logoutUrlDiscovered + "?endUrl=" + encodeURI(ssoLogoutEndUrl);
                    window.location.href = logoutUrl;
                }
            };

            //Go to home page
            self.gotoHomePage = function () {
                var welcomeUrl = dfu.discoverWelcomeUrl();
                oj.Logger.info("Go to welcome page by URL: " + welcomeUrl, false);
                window.location.href = cxtUtil.appendOMCContext(welcomeUrl, true, true, true);
            };

            //Open about box
            //aboutbox id
            self.aboutBoxId = 'aboutBox';
            self.aboutBoxImmediateLoading = ko.observable(false);
            self.openAboutBox = function () {
                $('#' + self.aboutBoxId).ojDialog('open');
            };

            //Open help link
            var helpBaseUrl = "http://www.oracle.com/pls/topic/lookup?ctx=cloud&id=";
            var helpTopicId = appProperties["helpTopicId"] ? appProperties["helpTopicId"] : "em_home_gs";
            self.openHelpLink = function () {
                oj.Logger.info("Open help link: " + helpBaseUrl + helpTopicId);
                window.open(helpBaseUrl + helpTopicId);
            };

            self.globalNavItems = [
                //Hide Preferences menu for V1 and will re-enable it in V1.1
//                    {"label": self.preferencesMenuLabel,
//                        "url": "#",
//                        "onclick": ""
//                    },
                {
                    "label": self.helpMenuLabel,
                    "url": "#",
                    "id": "emcpdf_oba_help",
                    "iconClass":"oj-menu-item-icon oj-fwk-icon fa-navmenu-help",
                    "linkClass":"emaas-appheader-help-link"
                },
                {
                    "label": self.aboutMenuLabel,
                    "url": "#",
                    "id": "emcpdf_oba_about",
                    "iconClass":null,
                    "linkClass":null
                },
                {
                    "label": self.signOutMenuLabel,
                    "url": "#",
                    "id": "emcpdf_oba_logout",
                    "iconClass":null,
                    "linkClass":null
                }
            ];

            self.globalNavMenuItemSelect = function (event, ui) {
                var itemId = $(ui.item).children("a").attr("id");
                switch (itemId) {
                    case "emcpdf_oba_help":
                        self.openHelpLink();
                        break;
                    case "emcpdf_oba_about":
                        self.openAboutBox();
                        break;
                    case "emcpdf_oba_logout":
                        self.handleSignout();
                        break;
                    default:
                        break;
                }
            };

            self.emaasAppheaderGlobalNavMenuBeforeOpen = function(){
                if(!self.renderEmaasAppheaderGlobalNavMenu()){
                    self.renderEmaasAppheaderGlobalNavMenu(true);
                    $('#emaasAppheaderGlobalNavMenuId').ojMenu("refresh");
                    self.aboutBoxImmediateLoading(true);
                }
            };

            var templatePath = "uifwk/js/widgets/navlinks/html/navigation-links.html";
            var vmPath = "uifwk/js/widgets/navlinks/js/navigation-links";

            //Parameters for navigation links ko component
            self.navLinksKocParams = {
                navLinksNeedRefresh: self.navLinksNeedRefresh,
                userName: self.userName,
                tenantName: self.tenantName,
                nlsStrings: nls,
                appMap: appMap,
                app: appProperties,
                appDashboard: appMap[appIdDashboard],
                appTenantManagement: appMap[appIdTenantManagement],
                appEventUI: appMap[appIdEventUI],
                sessionTimeoutWarnDialogId: self.sessionTimeoutWarnDialogId
            };
            //Register a Knockout component for navigation links
            if (!ko.components.isRegistered('df-oracle-nav-links') && self.navLinksVisible) {
                ko.components.register("df-oracle-nav-links", {
                    viewModel: {require: vmPath},
                    template: {require: 'text!' + templatePath}
                });
            }

            //Parameters for about dialog ko component
            self.aboutBoxKocParams = {
                id: self.aboutBoxId,
                nlsStrings: nls};
            //Register a Knockout component for about box
            var aboutTemplatePath = "uifwk/js/widgets/aboutbox/html/aboutbox.html";
            var aboutVmPath = "uifwk/js/widgets/aboutbox/js/aboutbox";
            if (!ko.components.isRegistered('df-oracle-about-box')) {
                ko.components.register("df-oracle-about-box", {
                    viewModel: {require: aboutVmPath},
                    template: {require: 'text!' + aboutTemplatePath}
                });
            }

            //Parameters for time selector
            if (params.timeSelectorParams) {
                self.timeSelectorParams = params.timeSelectorParams;
            } else {
                var start = cxtUtil.getStartTime() ? new Date(parseInt(cxtUtil.getStartTime())) : null;
                var end = cxtUtil.getEndTime() ? new Date(parseInt(cxtUtil.getEndTime())) : null;
                var timePeriod = cxtUtil.getTimePeriod() ? cxtUtil.getTimePeriod() : null;

                self.timeSelectorParams = {
                    startDateTime: ko.observable(start),
                    endDateTime: ko.observable(end),
                    timePeriod: ko.observable(timePeriod),
                    hideMainLabel: true,
                    dtpickerPosition: 'right'
                };
            }

            var timeSelectorVmPath = 'uifwk/js/widgets/datetime-picker/js/datetime-picker';
            var timeSelectorTemplatePath = 'uifwk/js/widgets/datetime-picker/html/datetime-picker.html';
            //Register a knockout component for time selector
            if (!ko.components.isRegistered('df-datetime-picker')) {
                ko.components.register("df-datetime-picker", {
                    viewModel: {require: timeSelectorVmPath},
                    template: {require: 'text!' + timeSelectorTemplatePath}
                });
            }

            /**
             * Navigation links button click handler
             */
            self.linkMenuHandler = function (event, item) {
                self.navLinksNeedRefresh(true);
                self.navLinksImmediateLoading(true);
                $("#links_menu").slideToggle('normal');
                item.stopImmediatePropagation();
            };
            
            var avoidPageResizeOptIn = $('#uifwkLayoutHbgmenuPlaceHolder').length > 0 ? true : false;
            function injectHamburgerMenuComponent() {
                //Check if hamburger menu has been created or not, if not create it
                if (!$('#omcHamburgerMenu').length) {
                    var hamburgerDiv = $($("#omc-hamburger-menu-template").text());
                    if (avoidPageResizeOptIn) {
                        var menuInitialStatus = retrieveHmaburgerMenuStatus();
                        if (menuInitialStatus === 'closed') {
                            hamburgerDiv.css('display', 'none');
                        }
                        $('#uifwkLayoutHbgmenuPlaceHolder').replaceWith(hamburgerDiv);
                    }
                    else {
                       $('#offcanvasInnerContainer').append(hamburgerDiv);
                    }
                    ko.applyBindings(self, hamburgerDiv[0]);
                }
            }
            
            function triggerDashboardResizeEvent(message) {
                if (window.selectedDashboardInst && window.selectedDashboardInst() && window.selectedDashboardInst().$b) {
                    window.selectedDashboardInst().$b.triggerBuilderResizeEvent(message);
                }
                else if ($(".right-panel-toggler")[0] && ko.dataFor($(".right-panel-toggler")[0])) {
                    var $b = ko.dataFor($(".right-panel-toggler")[0]).$b;
                    $b && $b.triggerBuilderResizeEvent(message);
                }
                else {
                    $(window).trigger('resize');
                }
            }

            function triggerHamburgerMenuToggleEvent(toggleType) {
                var message = {'tag': 'EMAAS_OMC_GLOBAL_MENU_TOGGLE_STATUS'};
                message.toggleType = toggleType;
                window.postMessage(message, window.location.href);
            }

            self.hamburgerMenuEnabled = omcHamburgerMenuOptIn ? true : false;
            self.renderHamburgerMenu = omcHamburgerMenuOptIn && (!window._uifwk || !window._uifwk.hideHamburgerMenuOnPage) ? true : false;
            self.isHamburgerMenuRegistered = ko.observable(false);
            self.hamburgerBtnLabel = nls.BRANDING_BAR_HAMBURGER_BTN_LABEL;
            if (self.renderHamburgerMenu) {
                self.menuParams = {'appId': self.appId, 'userName': self.userName, 'tenantName': self.tenantName, 'omcCurrentMenuId': params.omcCurrentMenuId};
                if (!self.isHamburgerMenuRegistered()) {
                    require(['ojs/ojnavigationlist','ojs/ojjsontreedatasource'], function () {
                    //Register a Knockout component for hamburger menu
                        var hamburgerVMPath = "uifwk/js/widgets/hamburger-menu/js/hamburger-menu";
                        var hamburgerTemplatePath = "uifwk/js/widgets/hamburger-menu/html/hamburger-menu.html";
                        if (!ko.components.isRegistered('omc-uifwk-hamburger-menu')) {
                            ko.components.register("omc-uifwk-hamburger-menu", {
                                viewModel: {require: hamburgerVMPath},
                                template: {require: 'text!' + hamburgerTemplatePath}
                            });
                            self.isHamburgerMenuRegistered(true);
                        }     
                        injectHamburgerMenuComponent();
                    });
                }
                else {
                    self.isHamburgerMenuRegistered(true);
                    injectHamburgerMenuComponent();
                }
                
                function resetCurrentHamburgerMenu() {
                    //Show composite menu if it's called before hamburger menu finished loading
                    if (window._uifwk && window._uifwk.compositeMenuName && window._uifwk.compositeMenuJson
                            && window._uifwk.stayInComposite && !window._uifwk.isCompositeMenuShown) {
                        menuUtil.showCompositeObjectMenu(window._uifwk.compositeMenuParentId,
                                                        window._uifwk.compositeMenuName,
                                                        window._uifwk.compositeMenuJson,
                                                        window._uifwk.compositeMenuCollapseCallback);
                    }
                    //Set current menu item if specified by API call
                    if (window._uifwk && window._uifwk.currentOmcMenuItemId) {
                        menuUtil.setCurrentMenuItem(window._uifwk.currentOmcMenuItemId, window._uifwk.underOmcAdmin);
                    }
                    else {
                        //Set current menu item if specified from branding bar params
                        var selectedMenuId = params.omcCurrentMenuId;
                        if (selectedMenuId) {
                            menuUtil.setCurrentMenuItem(selectedMenuId);
                        }
                    }
                }

                (function() {
                    if (!window._uifwk) {
                        window._uifwk = {};
                    }
                    var beforePrint = function() {
                        window._uifwk.isUnderPrint = true;
                    };

                    var afterPrint = function() {
                        window._uifwk.isUnderPrint = false;
                    };

                    if (window.matchMedia) {
                        var mediaQueryList = window.matchMedia('print');
                        mediaQueryList.addListener(function(mql) {
                            if (mql.matches) {
                                beforePrint();
                            } else {
                                afterPrint();
                            }
                        });
                    }

                    window.onbeforeprint = beforePrint;
                    window.onafterprint = afterPrint;

                }());

                function setPinnedHamburgerMenuStyles() {
                    $("#omcHamburgerMenu").removeClass('oj-offcanvas-start');
                    $("#omcHamburgerMenu").removeClass('oj-offcanvas-open');
                    $("#omcHamburgerMenu").removeClass('oj-offcanvas-overlay');
                    $("#omcHamburgerMenu").removeClass('oj-offcanvas-transition');
                    //$("#uifwkLayoutMainContainer").addClass('oj-flex-item');
                    $("#uifwkLayoutMainContainer").removeClass('oj-web-applayout-page');
                    $("#offcanvasInnerContainer").addClass('oj-flex');
                    $("#offcanvasInnerContainer").addClass('oj-flex-items-pad');
                }

                function setOverlayHamburgerMenuStyles() {
                    $("#omcHamburgerMenu").css('display', '');
                    $("#omcHamburgerMenu").addClass('oj-offcanvas-start');
                    $("#omcHamburgerMenu").removeClass('oj-flex-item');
                    $("#uifwkLayoutMainContainer").removeClass('oj-flex-item');
                    $("#uifwkLayoutMainContainer").addClass('oj-web-applayout-page');
                    $("#offcanvasInnerContainer").removeClass('oj-flex');
                    $("#offcanvasInnerContainer").removeClass('oj-flex-items-pad');
                }

                self.xlargeScreen = oj.ResponsiveKnockoutUtils.createMediaQueryObservable('(min-width: 1440px)');

                self.xlargeScreen.subscribe(function(isXlarge){
                    if (window._uifwk && (window._uifwk.isUnderPrint || window._uifwk.resizeTriggeredByPrint)) {
                        return;
                    }
                    if(!isXlarge){
                        if (avoidPageResizeOptIn) {
                            setOverlayHamburgerMenuStyles();
                            menuUtil.resizeHamburgerMenuLayout();
                            triggerDashboardResizeEvent('Hamburger menu closed.');
                            triggerHamburgerMenuToggleEvent('close');
                        }
                        else {
                            if($("#omcHamburgerMenu").hasClass("oj-offcanvas-open")){
                                oj.OffcanvasUtils.close({
                                        "edge": "start",
                                        "displayMode": "push",
                                        "selector": "#omcHamburgerMenu"
                                    });
                            }
                        }

                    }else{
                        if (avoidPageResizeOptIn) {
                            if($("#omcHamburgerMenu").hasClass("oj-offcanvas-open")){
                                oj.OffcanvasUtils.close({
                                        "edge": "start",
                                        "displayMode": "overlay",
                                        "selector": "#omcHamburgerMenu"
                                    });
                            }
                            var menuInitialStatus = retrieveHmaburgerMenuStatus();
                            if (menuInitialStatus !== 'closed') {
                                $("#omcHamburgerMenu").show();
                                $("#uifwkLayoutMainContainer").width($(window).width() - 250);
                            }
                            else {
                                $("#omcHamburgerMenu").hide();
                                $("#uifwkLayoutMainContainer").width($(window).width());
                            }
                            setPinnedHamburgerMenuStyles();
                            resetCurrentHamburgerMenu();
                            triggerHamburgerMenuToggleEvent('open');
                        }
                        else {
                            $("#omcHamburgerMenu").addClass('oj-offcanvas-start');
                            var menuInitialStatus = retrieveHmaburgerMenuStatus();
                            if(!$("#omcHamburgerMenu").hasClass("oj-offcanvas-open") && menuInitialStatus !== 'closed'){
                                oj.OffcanvasUtils.toggle({
                                        "edge": "start",
                                        "displayMode": "push",
                                        "selector": "#omcHamburgerMenu",
                                        "autoDismiss": "none"
                                    });
                                resetCurrentHamburgerMenu();
                            }else if($("#omcHamburgerMenu").hasClass("oj-offcanvas-overlay")){
                                    oj.OffcanvasUtils.close({
                                        "edge": "start",
                                        "displayMode": "overlay",
                                        "selector": "#omcHamburgerMenu",
                                        "autoDismiss": "focusLoss"
                                    });
                                if(menuInitialStatus !== 'closed'){
                                    setTimeout(function(){
                                            oj.OffcanvasUtils.open({
                                                "edge": "start",
                                                "displayMode": "push",
                                                "selector": "#omcHamburgerMenu",
                                                "autoDismiss": "none"
                                            });
                                            resetCurrentHamburgerMenu();
                                    },500);
                                }
                            }
                        }
                    }
                });

                self.toggleHamburgerMenu = function() {
                    if (avoidPageResizeOptIn) {
                        if (self.xlargeScreen()) {
                            $("#omcHamburgerMenu").toggle();
                            if ($("#omcHamburgerMenu").is(':visible')) {
                                $("#uifwkLayoutMainContainer").width($(window).width() - 250);
                                storeHamburgerMenuStatus("opened");
                                resetCurrentHamburgerMenu();
                                triggerHamburgerMenuToggleEvent('open');
                                triggerDashboardResizeEvent('Hamburger menu opened.');
                            }
                            else {
                                storeHamburgerMenuStatus("closed");
                                $("#uifwkLayoutMainContainer").width($(window).width());
                                triggerHamburgerMenuToggleEvent('close');
                                triggerDashboardResizeEvent('Hamburger menu closed.');
                            }
                        }
                        else {
                            oj.OffcanvasUtils.open({
                                "edge": "start",
                                "displayMode": "overlay",
        //                      "content": "#main-container",
                                "selector": "#omcHamburgerMenu",
                                "autoDismiss": "focusLoss"
                            });
                            if($("#omcHamburgerMenu").hasClass("oj-offcanvas-open")) {
                                resetCurrentHamburgerMenu();
                            }
                        }
                    }
                    else {
                        if(self.xlargeScreen()){
                            if($("#omcHamburgerMenu").hasClass("oj-offcanvas-open")){
                                storeHamburgerMenuStatus("closed");
                            }else{
                                storeHamburgerMenuStatus("opened");
                            }
                        }
                        oj.OffcanvasUtils.toggle({
                            "edge": "start",
                            "displayMode": self.xlargeScreen() ? "push" : "overlay",
    //                      "content": "#main-container",
                            "selector": "#omcHamburgerMenu",
                            "autoDismiss": self.xlargeScreen() ? "none" : "focusLoss"
                        });
                        if($("#omcHamburgerMenu").hasClass("oj-offcanvas-open")) {
                            resetCurrentHamburgerMenu();
                        }
                    }
                };

                var menuUtil = new menuModel();
                if (!window._uifwk) {
                    window._uifwk = {};
                }
                if (!window._uifwk.obbMenuLoadedListenerRegistered) {
                menuUtil.subscribeServiceMenuLoadedEvent(function(){
                    $("#omcHamburgerMenuInnerComp").show();
                    if (!avoidPageResizeOptIn) {
                        $("#omcHamburgerMenu").on("ojopen", function(event, offcanvas) {
                            if(offcanvas.displayMode === "push") {
                                $("#offcanvasInnerContainer").width(document.body.clientWidth-250);
                                triggerDashboardResizeEvent('Hamburger menu opened.');
                            }});

                        $("#omcHamburgerMenu").on("ojclose", function(event, offcanvas) {
                            $("#offcanvasInnerContainer").width(document.body.clientWidth);
                            triggerDashboardResizeEvent('Hamburger menu closed.');
                        });
                    }

                    $(window).resize(function() {
                        if (window._uifwk.isUnderPrint) {
                            window._uifwk.resizeTriggeredByPrint = true;
                        }
                        else {
                            window._uifwk.resizeTriggeredByPrint = false;
                        }
                        if (!avoidPageResizeOptIn) {
                            if ($("#omcHamburgerMenu").hasClass("oj-offcanvas-open") && !$("#omcHamburgerMenu").hasClass("oj-offcanvas-overlay")) {
                                $("#offcanvasInnerContainer").width(document.body.clientWidth - 250);
                            } else {
                                $("#offcanvasInnerContainer").width(document.body.clientWidth);
                            }
                        }
                        else {
                            menuUtil.resizeHamburgerMenuLayout();
                        }
                    });

                    if (!avoidPageResizeOptIn) {
                        $("#omcHamburgerMenu").addClass('oj-offcanvas-start');
                        $("#omcHamburgerMenu").removeClass('oj-flex-item');
                        var menuInitialStatus = retrieveHmaburgerMenuStatus();
                        if(self.xlargeScreen() && menuInitialStatus !== 'closed'){
                            $((function(){
                                oj.OffcanvasUtils.open({
                                    "edge": "start",
                                    "displayMode": "push",
                                    "selector": "#omcHamburgerMenu",
                                    "autoDismiss": "none"
                                });
                                triggerDashboardResizeEvent('Hamburger menu opened.');
                                storeHamburgerMenuStatus("opened");
                            })());
                        }
                    }
                    else {
                        if(!self.xlargeScreen()) {
                            setOverlayHamburgerMenuStyles();
                        }
                        else {
                            var menuInitialStatus = retrieveHmaburgerMenuStatus();
                            if (menuInitialStatus !== 'closed') {
                                $("#omcHamburgerMenu").show();
                            }
                            else {
                                $("#omcHamburgerMenu").hide();
                                triggerHamburgerMenuToggleEvent('close');
                            }
                            menuUtil.resizeHamburgerMenuLayout();
                        }
                    }


                    //Show composite menu if it's called before hamburger menu finished loading
                    if (window._uifwk && window._uifwk.compositeMenuName && window._uifwk.compositeMenuJson) {
                        menuUtil.showCompositeObjectMenu(window._uifwk.compositeMenuParentId,
                                                        window._uifwk.compositeMenuName,
                                                        window._uifwk.compositeMenuJson,
                                                        window._uifwk.compositeMenuCollapseCallback);
                    }

                    //Set current menu item if specified by API call
                    if (window._uifwk && window._uifwk.currentOmcMenuItemId) {
                        menuUtil.setCurrentMenuItem(window._uifwk.currentOmcMenuItemId, window._uifwk.underOmcAdmin);
                    }
                    else {
                        //Set current menu item if specified from branding bar params
                        var selectedMenuId = params.omcCurrentMenuId;
                        if (selectedMenuId) {
                            menuUtil.setCurrentMenuItem(selectedMenuId);
                        }
                    }
                });
                    window._uifwk.obbMenuLoadedListenerRegistered = true;
                }
            }
            
            /**
             * Notifications button click handler
             */
            self.notificationMenuHandler = function (event, item) {
                if (self.notificationPageUrl !== null && self.notificationPageUrl !== "") {
                    oj.Logger.info("Open notifications page: " + self.notificationPageUrl);
                    window.open(cxtUtil.appendOMCContext(self.notificationPageUrl, true, true, true));
                }
            };

            $('body').click(function () {
                $("#links_menu").slideUp('normal');
            });

            var displayMessages = [];
            var displayMessageCount = 0; //Count messages displayed except the retry in progress message
            var retryingMessageIds = [];
            var currentRetryingMsgId = null;
            var currentRetryFailMsgId = null;
            var currentPlannedDowntimeMsgId = null;
            var catRetryInProgress = "retry_in_progress";
            var catRetryFail = "retry_fail";
            var catPlannedDowntime = "omc_planned_downtime";
            self.hasHiddenMessages = ko.observable(false);
            self.hiddenMessagesExpanded = ko.observable(false);

            //Add listener to receive messages
            window.addEventListener("message", receiveMessage, false);

            //Expand all messages
            self.expandAllMessages = function (data, event) {
                for (var i = 0; i < hiddenMessages.length; i++) {
                    displayMessages.push(hiddenMessages[i]);
                    displayMessageCount++;
                }
                hiddenMessages = [];
                self.messageList(displayMessages);
                self.hasHiddenMessages(false);
                self.hiddenMessagesExpanded(true);
            };

            //Collapse messages and show first N messages only
            self.collapseMessages = function (data, event) {
                displayMessageCount = maxMsgDisplayCnt;
                var displayMsgCnt = maxMsgDisplayCnt;
                if (currentRetryingMsgId !== null) {
                    displayMsgCnt++;
                }

                for (var i = displayMsgCnt; i < displayMessages.length; i++) {
                    hiddenMessages.push(displayMessages[i]);
                }
                displayMessages.splice(displayMsgCnt, displayMessages.length - displayMsgCnt);
                self.messageList(displayMessages);
                self.hasHiddenMessages(true);
                self.hiddenMessagesExpanded(false);
            };

            //Reload current page which will redirect to sso login page when session has expired
            self.sessionTimeoutConfirmed = function () {
                $('#' + self.sessionTimeoutWarnDialogId).ojDialog('close');
                self.handleSignout();
            };

            //Set up timer to handle session timeout
            //Normally the timer will be setup in links navigator, when links navigator is not visible (e.g. in common error page),
            //the session timeout timer need to be set inside here.
//                if (self.navLinksVisible === false) {
//                    setupTimerForSessionTimeout();
//                }

//                function setupTimerForSessionTimeout() {
//                    if (!dfu.isDevMode()){
//                        var serviceUrl = "/sso.static/dashboards.configurations/registration";
//                        dfu.ajaxWithRetry({
//                            url: serviceUrl,
//                            headers: dfu.getDefaultHeader(),
//                            contentType:'application/json',
//                            success: function(data, textStatus) {
//                                dfu.setupSessionLifecycleTimeoutTimer(data.sessionExpiryTime, self.sessionTimeoutWarnDialogId);
//                            },
//                            error: function(xhr, textStatus, errorThrown){
//                                oj.Logger.error('Failed to get session expiry time by URL: '+serviceUrl);
//                            },
//                            async: true
//                        });
//                    }
//                }

            function receiveMessage(event)
            {
                if (event.origin !== window.location.protocol + '//' + window.location.host) {
                    return;
                }
                var data = event.data;
                //Only handle received message for showing page level messages
                if (data && data.tag && data.tag === 'EMAAS_SHOW_PAGE_LEVEL_MESSAGE') {
                    if (data.action) {
                        if (data.action.toUpperCase() === 'SHOW') {
                            showMessage(data);
                        }
                        else if (data.action.toUpperCase() === 'REMOVE') {
                            removeMessage(data);
                        }
                        else if (data.action.toUpperCase() === 'CLEAR') {
                            displayMessageCount = 0;
                            hiddenMessages = [];
                            displayMessages = [];
                            self.messageList(displayMessages);
                            self.hasHiddenMessages(false);
                            self.hasMessages(false);
                            self.hiddenMessagesExpanded(false);
                        }
                    }
                    //Show message by default
                    else {
                        showMessage(data);
                    }
                }
                else if (data && data.tag && data.tag === 'EMAAS_OMC_GLOBAL_CONTEXT_UPDATED') {
                    console.log("**************omc context change message received");
                    if (self.showGlobalContextBanner() === true) {
                        console.log("****************call refreshOMCContext");
                        refreshOMCContext();
                    }
                }
                else if (data && data.tag && data.tag === 'EMAAS_OMC_SESSION_TIME_OUT') {
                    self.renderSessionTimeoutDialog(true);
                    dfu.showSessionTimeoutWarningDialog(self.sessionTimeoutWarnDialogId);
                }
            }

            function fireTopologyStatusChangeEvent(actionType) {
                var intervalId = setInterval(function () {
                    if ($('div#ude-topology-div div[id^=emcta-topology-diagram]').length > 0) {
                        var prevStatus = null;
                        var currentStatus = null;
                        if (actionType === 'Open') {
                            prevStatus = 'Closed';
                            currentStatus = 'Open';
                        }
                        else if (actionType === 'Close') {
                            prevStatus = 'Open';
                            currentStatus = 'Closed';
                        }
                        else if (actionType === 'Refresh') {
                            prevStatus = 'Open';
                            currentStatus = 'Open';
                        }

                        var message = {'tag': 'EMAAS_OMC_TOPOLOGY_STATUS_UPDATED',
                            'eventType': actionType,
                            'previousStatus': prevStatus, // Open or Closed
                            'currentStatus': currentStatus // Closed or Open
                        };
                        window.postMessage(message, window.location.href);
                        clearInterval(intervalId);
                    }
                }, 100);
            }

            function fireMessageChangeEvent(eventType, msgId) {
                var message = {'tag': 'EMAAS_OMC_PAGE_LEVEL_MESSAGE_UPDATED',
                    'eventType': eventType, //Add or Remove
                    'messageId': msgId
                };
                window.postMessage(message, window.location.href);
            }

            function showMessage(data) {
                if (data) {
                    var message = {};
                    self.hasMessages(true);
                    message.id = data.id ? data.id : dfu.getGuid();
                    message.type = data.type;
                    message.summary = data.summary;
                    message.detail = data.detail;
                    message.category = data.category;
                    if (data.type && data.type.toUpperCase() === 'ERROR') {
                        message.iconAltText = self.altTextError;
                        message.imgCssStyle = "background:url('" + messageIconSprite + "') no-repeat 0px -78px;height:16px; width:16px; display: inline-block;";
                    }
                    else if (data.type && data.type.toUpperCase() === 'WARN') {
                        message.iconAltText = self.altTextWarn;
                        message.imgCssStyle = "background:url('" + messageIconSprite + "') no-repeat 0px -46px;height:16px; width:16px; display: inline-block;";
                    }
                    else if (data.type && data.type.toUpperCase() === 'CONFIRM') {
                        message.iconAltText = self.altTextConfirm;
                        message.imgCssStyle = "background:url('" + messageIconSprite + "') no-repeat 0px -30px; height:16px; width:16px; display: inline-block;";
                    }
                    else if (data.type && data.type.toUpperCase() === 'INFO') {
                        message.iconAltText = self.altTextInfo;
                        message.imgCssStyle = "background:url('" + messageIconSprite + "') no-repeat 0px -62px;height:16px; width:16px; display: inline-block;";
                    }

                    if (message.category === catPlannedDowntime) {
                        if (currentPlannedDowntimeMsgId === null) {
                            currentPlannedDowntimeMsgId = message.id;
                            displayMessages.splice(0, 0, message);
                        }
                    }
                    else if (message.category === catRetryInProgress) {
                        if (retryingMessageIds.length === 0) {
                            if (currentPlannedDowntimeMsgId === null) {
                                displayMessages.splice(0, 0, message);
                            }
                            else {
                                displayMessages.splice(1, 0, message);
                            }
                            currentRetryingMsgId = message.id;
                        }
                        retryingMessageIds.push(message.id);
                    }
                    else {
                        var isMsgNeeded = true;
                        if (message.category === catRetryFail && currentRetryFailMsgId !== null) {
                            isMsgNeeded = false;
                        }
                        else if (message.category === catRetryFail) {
                            currentRetryFailMsgId = message.id;
                        }

                        if (isMsgNeeded === true) {
                            if (displayMessageCount < maxMsgDisplayCnt || self.hiddenMessagesExpanded()) {
                                displayMessages.unshift(message);
                                displayMessageCount++;
                            }
                            else {
                                displayMessages.unshift(message);
                                hiddenMessages.unshift(displayMessages.pop());
                            }
                        }
                    }

                    self.messageList(displayMessages);
                    if (hiddenMessages.length > 0) {
                        self.hasHiddenMessages(true);
                    }
                    else {
                        self.hasHiddenMessages(false);
                    }

                    //Remove message automatically if remove delay time is set
                    if (data.removeDelayTime && typeof (data.removeDelayTime) === 'number') {
                        setTimeout(function () {
                            removeMessage(message);
                        }, data.removeDelayTime);
                    }

                    //Fire message change event
                    fireMessageChangeEvent('Create', message.id);
                }
            }

            function removeMessage(data) {
                if (data.category === catRetryInProgress) {
                    retryingMessageIds = removeItemByValue(retryingMessageIds, data.id);
                    if (retryingMessageIds.length === 0 && currentRetryingMsgId !== null) {
                        displayMessages = removeItemByPropertyValue(displayMessages, 'id', currentRetryingMsgId);
                        currentRetryingMsgId = null;
                    }
                }
                else if (data && data.id) {
                    var originDispMsgCnt = displayMessages.length;
                    hiddenMessages = removeItemByPropertyValue(hiddenMessages, 'id', data.id);
                    displayMessages = removeItemByPropertyValue(displayMessages, 'id', data.id);
                    if (originDispMsgCnt > displayMessages.length) {
                        displayMessageCount--;
                        if (hiddenMessages.length > 0) {
                            var newMsg = hiddenMessages[0];
                            displayMessages.push(newMsg);
                            hiddenMessages = removeItemByPropertyValue(hiddenMessages, 'id', newMsg.id);
                            displayMessageCount++;
                        }
                    }
                    if (data.category === catRetryFail) {
                        currentRetryFailMsgId = null;
                    }
                    if (data.category === catPlannedDowntime) {
                        currentPlannedDowntimeMsgId = null;
                    }
                }else if(data && data.category){
                    var originDispMsgCnt = displayMessages.length;
                    hiddenMessages = removeItemByPropertyValue(hiddenMessages, 'category', data.category);
                    displayMessages = removeItemByPropertyValue(displayMessages, 'category', data.category);
                    if (originDispMsgCnt > displayMessages.length) {
                        displayMessageCount--;
                        if (hiddenMessages.length > 0) {
                            var newMsg = hiddenMessages[0];
                            displayMessages.push(newMsg);
                            hiddenMessages = removeItemByPropertyValue(hiddenMessages, 'category', newMsg.category);
                            displayMessageCount++;
                        }
                    }
                    if (data.category === catRetryFail) {
                        currentRetryFailMsgId = null;
                    }
                    if (data.category === catPlannedDowntime) {
                        currentPlannedDowntimeMsgId = null;
                    }
                }

                self.messageList(displayMessages);
                if (hiddenMessages.length > 0) {
                    self.hasHiddenMessages(true);
                    self.hiddenMessagesExpanded(false);
                }
                else {
                    self.hasHiddenMessages(false);
                    if (displayMessageCount <= maxMsgDisplayCnt) {
                        self.hiddenMessagesExpanded(false);
                    }
                }

                //Fire message change event
                fireMessageChangeEvent('Delete', data.id);
            }

            function removeItemByValue(obj, value)
            {
                return obj.filter(function (val) {
                    return val !== value;
                });
            }

            function removeItemByPropertyValue(obj, prop, value)
            {
                return obj.filter(function (val) {
                    return val[prop] !== value;
                });
            }

            function checkNotifications() {
                oj.Logger.info("Start to check notifications for branding bar. relNotificationCheck: " +
                    self.relNotificationCheck + ", relNotificationShow: " + self.relNotificationShow, false);
                if (self.relNotificationCheck && self.relNotificationShow) {
                    self.notificationVisible(true);
                    if (urlNotificationCheck === null) {
                        if (self.relNotificationCheck.indexOf("/") === 0) {
                            urlNotificationCheck = self.relNotificationCheck;
                        }
                        else if (self.relNotificationCheck.indexOf("sso.static/") === 0) {
                            urlNotificationCheck = "/" + self.relNotificationCheck;
                        }
                        else if (self.relNotificationCheck.indexOf("static/") === 0) {
                            urlNotificationCheck = "/sso." + self.relNotificationCheck;
                        }

                        if (urlNotificationCheck) {
                            self.checkNotificationAvailability();
                            //Check notifications every 5 minutes
                            oj.Logger.info("Set timer to check notifications every 5 minutes.", false);
                            var interval = 5 * 60 * 1000;
                            setInterval(self.checkNotificationAvailability, interval);
                        }
                        else {
                            oj.Logger.warn("The notification check URL (relNotificationCheck) provided by current application is invalid: " + self.relNotificationCheck, false);
                            self.notificationDisabled(true);
                        }
                    }
                }
            }

//                function getSubscribedAppsCallback(apps) {
//                    oj.Logger.info("Finished getting subscribed applications for branding bar.", false);
//                    subscribedApps = apps;
//                    refreshAppName();
//                }
//
//                function getSubscribedApplications() {
//                    oj.Logger.info("Start to get subscribed applications for branding bar.", false);
//                    dfu.checkSubscribedApplications(getSubscribedAppsCallback);
//                }

            function refreshAppName() {
                var subscribedServices = null;
                //For app pages like LA or ITA or APM: only show name of LA or ITA or APM in Branding Bar.
                //Even other apps are subscribed to current tenant as well, we don't show them
                if (!isAppIdNotEmpty || self.appId === 'Dashboard' || self.appId === 'Error' || self.appId === 'EventUI') {
                    subscribedApps = [];
                }
                else {
                    subscribedApps = [self.appId];
                }

                if (subscribedApps && subscribedApps.length > 0) {
                    //Alphabetically sort the subscribed application names
                    subscribedApps.sort();
                    for (var i = 0; i < subscribedApps.length; i++) {
                        var appProps = appMap[subscribedApps[i]];
                        if (appProps) {
                            var servicename = nls[appProps['appName']] ? nls[appProps['appName']] : "";
                            if (i === 0) {
                                subscribedServices = servicename;
                            }
                            else {
                                subscribedServices = subscribedServices + " | " + servicename;
                            }
                        }
                    }
                }
                self.appName(subscribedServices);
            }
            function refreshTopologyParams(fireTopoChangeEvent) {
                if (self.isTopologyCompRegistered()) {
                    var refreshTopology = true;
                    var omcContext = cxtUtil.getOMCContext();
                    var entityMeIds = cxtUtil.getEntityMeIds() ? cxtUtil.getEntityMeIds().sort().join() : null;
                    var currentCompositeId = cxtUtil.getCompositeMeId() || entityMeIds;
                    console.log("************currentCompositeId" + currentCompositeId);
                    if (currentCompositeId) {
                        if (self.topologyInitialized === true && currentCompositeId === omcContext.previousCompositeMeId) {
                            console.log("****************self.topologyInitialized" + self.topologyInitialized);
                            console.log("************previousCompositeMeId" + omcContext.previousCompositeMeId);
                            refreshTopology = false;
                        }
                        else {
                            console.log("*******************refresh topology entities");
                            var compositeId = [];
                            compositeId.push(currentCompositeId);
                            self.entities(compositeId);
                            omcContext.previousCompositeMeId = currentCompositeId;
                            self.topologyInitialized = true;
                        }
                        self.topologyDisabled(false);
                    }
                    else {




                    }
//                    else {
//                        self.topologyDisabled(true);
//                        if (cxtUtil.getCompositeName() && cxtUtil.getCompositeType()) {
//                            self.queryVars({entityName: cxtUtil.getCompositeName(), entityType: cxtUtil.getCompositeType()});
//                        }
//                        else {
//                            var entityMeIds = cxtUtil.getEntityMeIds();
//                            if (entityMeIds) {
//                                //cxtUtil.getEntityMeIds() will return a list of meIds
//                                self.entities(entityMeIds);
//                            } else {
//                                entityMeIds = [];
//                                self.entities(entityMeIds);
//                            }
//                            if (omcContext.previousEntityMeIds) {
//                                if (omcContext.previousEntityMeIds.sort().join() === entityMeIds.sort().join()) {
//                                    refreshTopology = false;
//                                }
//
//                            }
//                        }
//                    }
                    var topologyParams = cxtUtil.getTopologyParams();
                    if (topologyParams && !self.topologyParamsSet) {
                        refreshTopology = true;
                    }
                    var entityMEIds = cxtUtil.getEntityMeIds();
                    console.log("***********updating highlightedEntities");
                    if (entityMEIds && entityMEIds.length) {
                        console.log("***********entityMEIds are not empty");
                        self.highlightedEntities(entityMEIds.concat([CONTEXT_CHANGE, NO_HIGHLIGHT]));
                    } else {
                        self.highlightedEntities([NO_HIGHLIGHT]);
                    }

                    if (refreshTopology) {
                        if (topologyParams) {
                            self.associations(topologyParams.associations);
                            self.layout(topologyParams.layout);
                            self.customNodeDataLoader(topologyParams.customNodeDataLoader);
                            self.customEventHandler(topologyParams.customEventHandler);
                            self.miniEntityCardActions(topologyParams.miniEntityCardActions);
                            self.topologyParamsSet = true;
                        }
                        else {
                            self.associations(null);
                            self.customNodeDataLoader(null);
                            self.customEventHandler(null);
                            self.miniEntityCardActions(true);
                            self.topologyParamsSet = true;
                        }
                        if (self.showEnterpriseTopology) { // set queryvar with   non-empty for enterprise topology
                            self.layout("LINEAR");
                            self.queryVars({entityName: "All Entities", entityType: "Enterprise Topology"});
                        }
                        else {
                            self.layout("TIERED");
                            self.queryVars(null);
                        }

                        $(".ude-topology-in-brandingbar .oj-diagram").ojDiagram("refresh");
                        if (self.isTopologyDisplayed()) {
                            console.log("***************topology initialied");
                            self.topologyInitialized = true;
                        }
                        if (fireTopoChangeEvent) {
                            fireTopologyStatusChangeEvent('Refresh');
                        }
                    }
                    //Clear dirty flag for topology after refreshing done
                    console.log("***********set self.topologyNeedRefresh to false");
                    self.topologyNeedRefresh = false;
                }
            }
            function refreshOMCContext() {
                //added suppport for single entity
                self.cxtCompositeMeId = cxtUtil.getCompositeMeId() || (cxtUtil.getEntities()[0] ? cxtUtil.getEntities()[0]['meId'] : cxtUtil.getEntities()[0]);
//                self.cxtCompositeType = cxtUtil.getCompositeType();
                self.cxtCompositeDisplayName = cxtUtil.getCompositeDisplayName() || (cxtUtil.getEntities()[0] ? cxtUtil.getEntities()[0]['displayName'] : cxtUtil.getEntities()[0]);
                self.cxtCompositeName = cxtUtil.getCompositeName() || (cxtUtil.getEntities()[0] ? cxtUtil.getEntities()[0]['entityName'] : cxtUtil.getEntities()[0]);
                self.cxtComposite = cxtUtil.getCompositeEntity();
//                self.cxtStartTime = cxtUtil.getStartTime();
//                self.cxtEndTime = cxtUtil.getEndTime();
                //self.cxtEntityMeId = cxtUtil.getEntityMeId();
//                self.cxtEntityType = cxtUtil.getEntityType();
//                self.cxtEntityName = cxtUtil.getEntityName();
//                self.cxtTimePeriod = cxtUtil.getTimePeriod();
//                self.cxtEntityMeIds = cxtUtil.getEntityMeIds();
//                self.cxtEntityTypeDisplayName = self.cxtEntityType;
                //Refresh topology button status
                if (self.cxtCompositeMeId) {
                    self.topologyDisabled(false);

                    if (!cxtUtil.getCompositeMeId()) {
                        window.centernodeid_diagram = cxtUtil.getEntities()[0]['meId'];
                    }
                    self.showEnterpriseTopology = false;
                    window.globalpillsempty = false;
                }
                //When no compositeMEID exists, disable topology button
                else {
                    //show enterprise topology
                    self.showEnterpriseTopology = true;
                    window.globalpillsempty = true;
                    self.topologyDisabled(false);
                }


//                if (!self.cxtCompositeName && self.cxtCompositeMeId) {
//                    //fetch composite name from WS API by compositeMeId
//                    queryODSEntityByMeId(self.cxtCompositeMeId, 'composite', queryOdsEntityCallback);
//                }
//                else {
//                    refreshEntityContextText();
//                }
//                if (!self.cxtEntityName && self.cxtEntityMeId) {
//                    //fetch entity name from WS API by entityMeId
//                    queryODSEntityByMeId(self.cxtEntityMeId, 'entity', queryOdsEntityCallback);
//                }
//                else if (!self.cxtEntityName && self.cxtEntityType) {
//                    //fetch entity type display name
//                    queryTargetModelMetaType(self.cxtEntityType, queryTmMetypeCallback);
//                }

                refreshEntityContextText();
//                refreshTimeCtxText();

                //Set a dirty flag for topology to be refreshed
                console.log("***********set self.topologyNeedRefresh to true");
                self.topologyNeedRefresh = true;
                if (self.isTopologyDisplayed()) {
                    // update parameters for topology 
                    console.log("***********calling refreshTopologyParams");
                    refreshTopologyParams(true);
                }
            }

            function refreshEntityContextText() {
//                //A composite entity & no member entity
//                if (self.cxtCompositeName && self.cxtEntityName) {
//                    self.compositeCxtText(msgUtil.formatMessage(nls.BRANDING_BAR_GLOBAL_CONTEXT_COMPOSITE_ENTITY, 
//                                                        self.cxtCompositeName, self.cxtEntityName));
//                }
//                //A composite entity & a member entity (e.g. Rideshare App & slc01.us.oracle.com)
//                else if (self.cxtCompositeName && self.cxtEntityType) {
//                    self.compositeCxtText(msgUtil.formatMessage(nls.BRANDING_BAR_GLOBAL_CONTEXT_COMPOSITE_ENTITY_TYPE, 
//                                                        self.cxtCompositeName, self.cxtEntityTypeDisplayName));
//                }
//                else if (self.cxtCompositeName) {
//                    var entityMeIds = self.cxtEntityMeIds ? self.cxtEntityMeIds.split(',') : null;
//                    //A composite entity & multiple entities
//                    if (entityMeIds && entityMeIds.length > 0) {
//                        if (entityMeIds.length === 1) {
//                            self.compositeCxtText(msgUtil.formatMessage(nls.BRANDING_BAR_GLOBAL_CONTEXT_COMPOSITE_SINGLE_ENTITY, 
//                                                        self.cxtCompositeName));
//                        }
//                        else {
//                            self.compositeCxtText(msgUtil.formatMessage(nls.BRANDING_BAR_GLOBAL_CONTEXT_COMPOSITE_ENTITIES, 
//                                                        self.cxtCompositeName, entityMeIds.length));
//                        }
//                    }
//                    //A composite entity & no member entity
//                    else {
//                        self.compositeCxtText(self.cxtCompositeName);
//                    }
//                }
                //For now, only show composite context text on banner UI, and single entity
                self.compositeCxtText('');
                self.entitiesList.removeAll();

                var displayCompositeName = self.cxtCompositeMeId
                    && self.cxtCompositeDisplayName;

                var displayEntitiesName = cxtUtil.getEntityMeIds()
                    && !cxtUtil.getEntitiesType()
                    && cxtUtil.getEntityMeIds().length > 0
                    && cxtUtil.getEntities().length > 0;
                displayEntitiesName = false; // disable emctas-5151/emcpdf-2773 for 1.14

                if (displayCompositeName) {
                    self.compositeCxtText(self.cxtCompositeDisplayName);
                }
                if (displayEntitiesName)
                {
                    self.entitiesList(cxtUtil.getEntities());
                }
                if (!displayCompositeName && !displayEntitiesName)
                {
                    //No composite entity & no entities
                    self.compositeCxtText(nls.BRANDING_BAR_GLOBAL_CONTEXT_ALL_ENTITIES);
                    self.cxtComposite.isEnabled(true);
                }
            }

//            function refreshTimeCtxText() {
//                if (self.cxtTimePeriod) {
//                    self.timeCxtText(self.cxtTimePeriod);
//                }
//                else {
//                    var dateStartTime = self.cxtStartTime ? new Date(parseInt(self.cxtStartTime)) : null;
//                    var dateEndTime = self.cxtEndTime ? new Date(parseInt(self.cxtEndTime)) : null;
//                    var dateStartTimeText = formatDateTime(dateStartTime);
//                    var dateEndTimeText = formatDateTime(dateEndTime);
//                    if (dateStartTimeText || dateEndTimeText) {
//                        self.timeCxtText(msgUtil.formatMessage(nls.BRANDING_BAR_GLOBAL_CONTEXT_TIME,
//                                                            dateStartTimeText ? dateStartTimeText : '', 
//                                                            dateEndTimeText ? dateEndTimeText : ''));
//                    }
//                    else {
//                        self.timeCxtText(nls.BRANDING_BAR_GLOBAL_CONTEXT_TIME_ALL);
//                    }
//                }
//            }
//
//            function formatDateTime(dateTime) {
//                if (dateTime) {
//                    var dateTimeOption = {formatType: "datetime", dateFormat: "medium"};
//                    if (!self.dateTimeConverter) {
//                        self.dateTimeConverter = oj.Validation.converterFactory("dateTime").createConverter(dateTimeOption);
//                    }
//                    return self.dateTimeConverter.format(oj.IntlConverterUtils.dateToLocalIso(dateTime));
//                }
//                return null;
//            }

//            function queryOdsEntityCallback(data, ctxType) {
//                if (data && data['rows']) {
//                    var dataRows = data['rows'];
//                    if (dataRows.length > 0) {
//                        var entity = dataRows[0];
//                        if (entity.length === 4) {
//                            if (ctxType === 'composite') {
//                                self.cxtCompositeName = entity[2];
//                                self.cxtCompositeType = entity[3];
//                            }
//                            else if (ctxType === 'entity') {
//                                self.cxtEntityName = entity[2];
//                                self.cxtEntityType = entity[3];
//                            }
//                        }
//                    }
//                }
//                refreshEntityContextText();
//            }
//
//            function queryTmMetypeCallback(data) {
//                if (data && data['typeDisplayName']) {
//                    self.cxtEntityTypeDisplayName = data['typeDisplayName'];
//                }
//                refreshEntityContextText();
//            }
//
//            function queryODSEntityByMeId(meId, ctxType, callback) {
//                var jsonOdsQuery = {"ast":{"query":"simple","distinct":false,"select":[{"item":{"expr":"column","table":"me","column":"meId"}},
//                        {"item":{"expr":"column","table":"me","column":"entityName"}},
//                        {"item":{"expr":"column","table":"me","column":"displayName"}},
//                        {"item":{"expr":"column","table":"me","column":"entityType"}}],
//                    "from":[{"table":"virtual","name":"ManageableEntity","alias":"me"}],
//                    "where":{"cond":"inExpr","lhs":{"expr":"column","table":"me","column":"meId"},
//                    "rhs":[{"expr":"str","val":""}]}}};
//                var odsQueryUrl = getODSEntityQueryUrl();
//                jsonOdsQuery['ast']['where']['rhs'][0]['val'] = meId; 
//                oj.Logger.info("Start to get ODS entity by entity ID by URL:" + odsQueryUrl, false);
//                dfu.ajaxWithRetry(odsQueryUrl,{
//                    type: 'POST',
//                    data: JSON.stringify(jsonOdsQuery),
//                    contentType: 'application/json',
//                    headers: dfu.getDefaultHeader(),
//                    success:function(data, textStatus,jqXHR) {
//                        callback(data, ctxType);
//                    },
//                    error:function(xhr, textStatus, errorThrown){
//                        oj.Logger.error("Error: Failed to fetch ODS entity by ID due to error: " + textStatus);
//                    }
//                });
//            }
//
//            function queryTargetModelMetaType(metype, callback) {
//                var tmQueryUrl = dfu.buildFullUrl(getTargetModelMetypeUrl(), metype);
//                oj.Logger.info("Start to get ODS entity by entity ID by URL:" + tmQueryUrl, false);
//                dfu.ajaxWithRetry(tmQueryUrl,{
//                    type: 'GET',
//                    contentType: 'application/json',
//                    headers: dfu.getDefaultHeader(),
//                    success:function(data, textStatus,jqXHR) {
//                        callback(data);
//                    },
//                    error:function(xhr, textStatus, errorThrown){
//                        oj.Logger.error("Error: Failed to fetch Target Model meta type by ID due to error: " + textStatus);
//                    }
//                });
//            }

//            function getODSEntityQueryUrl() {
//                var odsUrl = '/sso.static/datamodel-query';
//                if (dfu.isDevMode()){
//                    odsUrl = dfu.buildFullUrl(dfu.getDevData().odsRestApiEndPoint,"query");
//                }
//                return odsUrl;
//            }
//
//            function getTargetModelMetypeUrl() {
//                var tmUrl = '/sso.static/datamodel-metadata/metypes';
//                if (dfu.isDevMode()){
//                    tmUrl = dfu.buildFullUrl(dfu.getDevData().tmRestApiEndPoint,"metadata/metypes");
//                }
//                return tmUrl;
//            }
            //
            // send message when brandingbar is instantiated
            //
            if(!window._uiwfk){
                window._uiwfk = {};
            }
            window._uiwfk.brandingbar_initialized = true;
            var message = {'tag': 'EMAAS_BRANDINGBAR_INSTANTIATED'};
            window.postMessage(message, window.location.href);

            //Detect planned downtime
            var zdtUtil = new zdtUtilModel();
            zdtUtil.detectPlannedDowntime(function () {
            });

            ko.bindingHandlers.stopDataBinding = {
                init: function (elem, valueAccessor) {
                    var value = ko.unwrap(valueAccessor());
                    return {controlsDescendantBindings: value};
                }
            };
        }

        return BrandingBarViewModel;
    });




