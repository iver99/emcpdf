define('uifwk/@version@/js/sdk/menu-util-impl', [
    'ojs/ojcore',
    'knockout',
    'jquery',
    'uifwk/@version@/js/util/ajax-util-impl',
    'uifwk/@version@/js/util/df-util-impl',
    'uifwk/@version@/js/util/usertenant-util-impl',
    'uifwk/@version@/js/sdk/SessionCacheUtil'
],
    function (oj, ko, $, ajaxUtilModel, dfuModel, userTenantModel, sessionCacheModel)
    {
        function UIFWKGlobalMenuUtil() {
            var self = this;
            var ajaxUtil = new ajaxUtilModel();
            var dfu = new dfuModel();
            var userTenantUtil = new userTenantModel();
            var host = window.location.host;
            var tenantName = userTenantUtil.getTenantName();
            
            //Global menu id constants
            self.OMCMenuConstants = {
                'GLOBAL_HOME': 'omc_root_home',
                'GLOBAL_ALERTS': 'omc_root_alerts',
                'GLOBAL_DASHBOARDS': 'omc_root_dashboards',
                'GLOBAL_DATAEXPLORER': 'omc_root_dataexplorer',
                'GLOBAL_APM': 'omc_root_APM',
                'GLOBAL_MONITORING': 'omc_root_Monitoring',
                'GLOBAL_LOGANALYTICS': 'omc_root_LogAnalytics',
                'GLOBAL_ITANALYTICS': 'omc_root_ITAnalytics',
                'GLOBAL_ORCHESTRATION': 'omc_root_Orchestration',
                'GLOBAL_SECURITY': 'omc_root_SecurityAnalytics',
                'GLOBAL_COMPLIANCE': 'omc_root_Compliance',
                'GLOBAL_ADMIN': 'omc_root_admin',
                'GLOBAL_ADMIN_ALERTRULES': 'omc_root_admin_alertrules',
                'GLOBAL_ADMIN_NOTIFICATION_CHANNELS': 'omc_root_admin_notificationChannels',
                'GLOBAL_ADMIN_AGENTS': 'omc_root_admin_agents',
                'GLOBAL_ADMIN_ADDENTITY': 'omc_root_admin_addentity',
                'GLOBAL_ADMIN_CLOUDDISCOVERYPROFILES': 'omc_root_admin_clouddiscoveryprofiles',
                'GLOBAL_ADMIN_ENTITIESCONFIG': 'omc_root_admin_entitiesconfig',
                'GLOBAL_ADMIN_GRP_APM': 'omc_root_admin_grp_APM',
                'GLOBAL_ADMIN_GRP_MONITORING': 'omc_root_admin_grp_Monitoring',
                'GLOBAL_ADMIN_GRP_LOGANALYTICS': 'omc_root_admin_grp_LogAnalytics',
                'GLOBAL_ADMIN_GRP_SECURITY': 'omc_root_admin_grp_SecurityAnalytics',
                'GLOBAL_ADMIN_GRP_COMPLIANCE': 'omc_root_admin_grp_Compliance'
            };
            //freeze every constant object
            Object.freeze(self.OMCMenuConstants);
            
            /**
             * Fire event to show composite menu in OMC hamburger menu bar
             * 
             * @param {String} parentMenuId Parent menu item id
             * @param {String} objMenuName Composite menu header
             * @param {Object} menuJson JSON object for composite menus
             * 
             * @returns
             */
            function fireCompositeMenuDisplayEvent(parentMenuId, objMenuName, menuJson) {
                var message = {'tag': 'EMAAS_OMC_GLOBAL_MENU_COMPOSITE_DISPLAY'};
                message.compositeParentMenuId = parentMenuId;
                message.compositeRootMenuLabel = objMenuName;
                message.compositeMenuJson = menuJson;
                window.postMessage(message, window.location.href);
            }
            
            /**
             * Fire event to indicate that OMC hamburger menu bar has finished loading
             * 
             * @returns
             */
            self.fireServiceMenuLoadedEvent = function(){
                if (!window._uifwk) {
                    window._uifwk = {};
                }
                window._uifwk.serviceMenuLoaded = true;
                var message = {'tag': 'EMAAS_OMC_GLOBAL_MENU_SERVICE_MENU_LOADED'};
                window.postMessage(message, window.location.href);
            };
            
            /**
             * Add listener to repond to event when OMC hamburger menu bar has finished loading
             * 
             * @param {Function} callback Function to be invoked when OMC hamburger menu bar has finished loading
             * 
             * @returns
             */
            self.subscribeServiceMenuLoadedEvent = function(callback) {
                function onServiceMenuLoaded(event) {
                    if (event.origin !== window.location.protocol + '//' + window.location.host) {
                        return;
                    }
                    var eventData = event.data;
                    //Only handle received message for global menu selection
                    if (eventData && eventData.tag && eventData.tag === 'EMAAS_OMC_GLOBAL_MENU_SERVICE_MENU_LOADED') {
                        if ($.isFunction(callback)) {
                            callback(eventData);
                        }
                    }
                };
                window.addEventListener("message", onServiceMenuLoaded, false);
            };

            /**
             * Fire event to indicate that favorite dashboards have been changed
             * 
             * @returns
             */
            self.fireFavoriteDsbChangedEvent = function(setFavorite){
                if (!window._uifwk) {
                    window._uifwk = {};
                }
                var message = {'tag': 'EMAAS_OMC_GLOBAL_MENU_FAVORITE_DSB_CHANGED'};
                message.setFavorite = setFavorite;
                window.postMessage(message, window.location.href);
            };
            
            /**
             * Add listener to repond favorite dashboard changed event.
             * 
             * @param {Function} callback Callback to be invoked
             * 
             * @returns
             */
            self.subscribeFavoriteDsbChangedEvent = function(callback) {
                function onFavoriteDsbChanged(event) {
                    if (event.origin !== window.location.protocol + '//' + window.location.host) {
                        return;
                    }
                    var eventData = event.data;
                    //Only handle received message for service menu registering
                    if (eventData && eventData.tag && eventData.tag === 'EMAAS_OMC_GLOBAL_MENU_FAVORITE_DSB_CHANGED') {
                        if ($.isFunction(callback)) {
                            callback(eventData.setFavorite);
                        }
                    }
                };
                window.addEventListener("message", onFavoriteDsbChanged, false);
            };

            /**
             * Set current menu item
             * 
             * @param {String} menuItemId Id of the menu item which should be set in selected and highlighted status
             * 
             * @returns
             */
            self.setCurrentMenuItem = function(menuItemId, underOmcAdmin) {
                if (!window._uifwk) {
                    window._uifwk = {};
                }
                window._uifwk.currentOmcMenuItemId = menuItemId;
                window._uifwk.underOmcAdmin = underOmcAdmin;
                var message = {'tag': 'EMAAS_OMC_GLOBAL_MENU_SET_CURRENT_ITEM'};
                message.menuItemId = menuItemId;
                message.underOmcAdmin = underOmcAdmin;
                window.postMessage(message, window.location.href);
            };
            
            /**
             * Provide service menus meta-data at runtime to make it available in OMC hamburger menu bar
             * 
             * @param {Object} serviceMenuJson JSON object for service menus
             * 
             * @returns
             */
            self.registerServiceMenus = function(serviceMenuJson) {
                var message = {'tag': 'EMAAS_OMC_GLOBAL_MENU_REGISTER_SERVICE_MENU'};
                message.serviceMenuJson = serviceMenuJson;
                window.postMessage(message, window.location.href);
            };
            
            /**
             * Add listener to repond service menu registering event.
             * 
             * @param {Function} callback Callback to be invoked
             * 
             * @returns
             */
            self.subscribeServiceMenuRegisterEvent = function(callback) {
                function onServiceMenuRegister(event) {
                    if (event.origin !== window.location.protocol + '//' + window.location.host) {
                        return;
                    }
                    var eventData = event.data;
                    //Only handle received message for service menu registering
                    if (eventData && eventData.tag && eventData.tag === 'EMAAS_OMC_GLOBAL_MENU_REGISTER_SERVICE_MENU') {
                        if ($.isFunction(callback)) {
                            callback(eventData.serviceMenuJson);
                        }
                    }
                };
                window.addEventListener("message", onServiceMenuRegister, false);
            };
            
            /**
             * Show composite object menu
             * 
             * @param {String} parentMenuId Parent menu item id
             * @param {String} objMenuName Composite menu header
             * @param {Object} menuJson JSON object for composite menus
             * @param {Function} collapseCallback Callback function to be invoked composite object menu is collapsed
             * 
             * @returns
             */
            self.showCompositeObjectMenu = function(parentMenuId, objMenuName, menuJson, collapseCallback) {
                if ($.isFunction(collapseCallback)) {
                    if (!window._uifwk) {
                        window._uifwk = {};
                    }
                    window._uifwk.compositeMenuCollapseCallback = collapseCallback;
                }
                window._uifwk.compositeMenuParentId = parentMenuId;
                window._uifwk.compositeMenuName = objMenuName;
                window._uifwk.compositeMenuJson = menuJson;
                window._uifwk.isCompositeMenuShown = true;
                window._uifwk.stayInComposite = true;
                if (window._uifwk.serviceMenuLoaded) {
                    fireCompositeMenuDisplayEvent(parentMenuId, objMenuName, menuJson);
                }
            };
            
            /**
             * Add listener when show composite object menu event is fired
             * 
             * @param {Function} callback Callback function to be invoked
             * 
             * @returns
             */
            self.subscribeCompositeMenuDisplayEvent = function(callback) {
                function onCompositeMenuDisplay(event) {
                    if (event.origin !== window.location.protocol + '//' + window.location.host) {
                        return;
                    }
                    var eventData = event.data;
                    //Only handle received message for composite menu display
                    if (eventData && eventData.tag && eventData.tag === 'EMAAS_OMC_GLOBAL_MENU_COMPOSITE_DISPLAY') {
                        if ($.isFunction(callback)) {
                            callback(eventData.compositeParentMenuId, eventData.compositeRootMenuLabel, eventData.compositeMenuJson);
                        }
                    }
                };
                window.addEventListener("message", onCompositeMenuDisplay, false);
            };
            
            /**
             * Add listener to respond to menu selection event
             * 
             * @param {Function} callback Callback function to be invoked
             * 
             * @returns
             */
            self.subscribeMenuSelectionEvent = function(callback) {
                function onMenuSelection(event) {
                    if (event.origin !== window.location.protocol + '//' + window.location.host) {
                        return;
                    }
                    var eventData = event.data;
                    //Only handle received message for global menu selection
                    if (eventData && eventData.tag && eventData.tag === 'EMAAS_OMC_GLOBAL_MENU_SELECTION') {
                        if ($.isFunction(callback)) {
                            callback(eventData.data);
                        }
                    }
                };
                window.addEventListener("message", onMenuSelection, false);
            };
            
            /**
             * Set up a custom KO stopBinding handler
             * 
             * @returns
             */
            self.setupCustomKOStopBinding = function() {
                ko.bindingHandlers.stopBinding = {
                    init: function() {
                        return {controlsDescendantBindings: true};
                    }
                };
                ko.virtualElements.allowedBindings.stopBinding = true;
            };
            
            /**
             * Get base vanity URLs for all services
             * 
             * @param {Function} callback Callback function to be invoked when base vanity URLs are fetched
             * 
             * @returns
             */
            self.getServiceBaseVanityUrls = function(callbackForVanityUrls) {
                if (window._uifwk && window._uifwk.cachedData && window._uifwk.cachedData.baseVanityUrls && 
                        ($.isFunction(window._uifwk.cachedData.baseVanityUrls) ? window._uifwk.cachedData.baseVanityUrls() : true)) {
                    callbackForVanityUrls($.isFunction(window._uifwk.cachedData.baseVanityUrls) ? window._uifwk.cachedData.baseVanityUrls() : 
                            window._uifwk.cachedData.baseVanityUrls);
                } else {
                    if (!window._uifwk) {
                        window._uifwk = {};
                    }
                    if (!window._uifwk.cachedData) {
                        window._uifwk.cachedData = {};
                    }
                    if (!window._uifwk.cachedData.isFetchingVanityUrls) {
                        window._uifwk.cachedData.isFetchingVanityUrls = true;
                        if (!window._uifwk.cachedData.baseVanityUrls) {
                            window._uifwk.cachedData.baseVanityUrls = ko.observable();
                        }

                        function doneCallback(data, textStatus, jqXHR) {
                            window._uifwk.cachedData.baseVanityUrls(data);
                            window._uifwk.cachedData.isFetchingVanityUrls = false;
                            callbackForVanityUrls(data);
                        }

                        var lookupVanityUrl = '/sso.static/dashboards.registry';
                        if (dfu.isDevMode()) {
                            var dfBaseUrl = dfu.getDevData().dfRestApiEndPoint;
                            lookupVanityUrl = dfBaseUrl.substring(0, dfBaseUrl.indexOf('/emcpdf/')) + '/emcpdf/api/v1/registry';
                        }
                        lookupVanityUrl = lookupVanityUrl + '/lookup/baseVanityUrls';
                        ajaxUtil.ajaxWithRetry({type: 'GET', contentType: 'application/json', url: lookupVanityUrl,
                            dataType: 'json',
                            headers: dfu.getDefaultHeader(),
                            async: true,
                            success: function (data, textStatus, jqXHR) {
                                doneCallback(data, textStatus, jqXHR);
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                console.log('Failed to get vanity URLs!');
                                window._uifwk.cachedData.isFetchingVanityUrls = false;
                                callbackForVanityUrls(null);
                            }
                        });
                    } else {
                        window._uifwk.cachedData.baseVanityUrls.subscribe(function (data) {
                            callbackForVanityUrls(data);
                        });
                    }
                }
            };
            
            /**
             * Generate a vanity url based on specified base vanity URL and original URL
             * 
             * @param {String} originalUrl Original URL
             * @param {String} baseVanityUrl Base vanity URL
             * 
             * @returns Full vanity URL constructed by original URL and base vanity URL
             */
            self.generateVanityUrl = function(originalUrl, baseVanityUrl) {
                var targetUrl = originalUrl;
                if (baseVanityUrl && host.indexOf(tenantName + '.') === 0) {
                    if (baseVanityUrl.indexOf('://') > -1) {
                        var urlSplitted = baseVanityUrl.split('://');
                        if (urlSplitted.length === 2) {
                            var idx = originalUrl.indexOf('/emsaasui');
                            if (idx > -1 && idx !== 0) {
                                originalUrl = originalUrl.substring(idx);
                            }
                            targetUrl = dfu.buildFullUrl(urlSplitted[0] + '://' + tenantName + '.' + urlSplitted[1], originalUrl);
                        }
                    }
                }
                return targetUrl;
            };
            
            /**
             * Get service base vanity URL
             * 
             * @param {String} originalUrl Original URL
             * @param {String} serviceName Service name
             * @param {Function} callback Callback function to be invoked when base vanity URL is fetched
             * 
             * @returns
             */
            self.getVanityUrlByServiceName = function(originalUrl, serviceName, callback) {
                if (!originalUrl || !serviceName) {
                    oj.Logger.error("Error: Failed to get vanity URL: serviceName=" + serviceName + ', originalUrl=' + originalUrl);
                }
                
                if (host.indexOf(tenantName + '.') === 0) {
                    self.getServiceBaseVanityUrls(fetchServiceVanityUrl);
                }
                else {
                    callback(originalUrl);
                }
                
                function fetchServiceVanityUrl(urls) {
                    var targetUrl = originalUrl;
                    if (urls && urls[serviceName]) {
                        targetUrl = generateVanityUrl(originalUrl, urls[serviceName]);
                    }
                    callback(targetUrl);
                }
            };
            
            self.xlargeScreen = oj.ResponsiveKnockoutUtils.createMediaQueryObservable('(min-width: 1440px)');
            
            var menuStatusSessionCacheName = '_uifwk_hamburgermenustatuscache';
            var menuStatusSessionCacheDataKey = 'hamburger_menu_is_open';
            var menuStatusSessionCache = new sessionCacheModel(menuStatusSessionCacheName, 1);
            function getHamburgerMenuInitialStatus(){
                return menuStatusSessionCache && menuStatusSessionCache.retrieveDataFromCache(menuStatusSessionCacheName) && menuStatusSessionCache.retrieveDataFromCache(menuStatusSessionCacheName)[menuStatusSessionCacheDataKey];
            }
            
            /**
             * Check cached hamburger menu status to see if need to open hamburger menu by default
             * 
             * @returns {boolean} true: show by default, false: hide by default
             */
            self.showHamburgerMenuByDefault = function() {
                var menuInitialStatus = getHamburgerMenuInitialStatus();
                return menuInitialStatus !== 'closed';
            };
            
            /**
             * Initialize hamburger menu layout
             * 
             * @returns
             */
            self.initializeHamburgerMenuLayout = function() {
                function setOverlayHamburgerMenuStyles() {
                    $("#uifwkLayoutHbgmenuPlaceHolder").removeClass('oj-flex-item');
                    $("#uifwkLayoutMainContainer").removeClass('oj-flex-item'); 
                    $("#uifwkLayoutMainContainer").addClass('oj-web-applayout-scrollable');
                    $("#uifwkLayoutMainContainer").addClass('oj-web-applayout-page');
                    $("#offcanvasInnerContainer").removeClass('oj-flex');
                    $("#offcanvasInnerContainer").removeClass('oj-flex-items-pad');
                }
                
                if(!self.xlargeScreen()) {
                    setOverlayHamburgerMenuStyles();
                }
                else {
                    if (self.showHamburgerMenuByDefault()) {
                        $('#uifwkLayoutMainContainer').width($(window).width() - 250);
                    }
                    else {
                        $('#uifwkLayoutHbgmenuPlaceHolder').hide();
                        $("#omcHamburgerMenu").hide();
                        $('#uifwkLayoutMainContainer').width($(window).width());
                    }
                    $('#uifwkLayoutHbgmenuPlaceHolder').width(250);
                }
            };
            
            /**
             * Add listener when hamburger menu is opened or closed in large screen
             * 
             * @param {Function} callback Callback function to be invoked
             * 
             * @returns
             */
            self.subscribeHamburgerMenuToggleEvent = function(callback) {
                function onHamburgerMenuToggle(event) {
                    if (event.origin !== window.location.protocol + '//' + window.location.host) {
                        return;
                    }
                    var eventData = event.data;
                    //Only handle received message for composite menu display
                    if (eventData && eventData.tag && eventData.tag === 'EMAAS_OMC_GLOBAL_MENU_TOGGLE_STATUS') {
                        if ($.isFunction(callback)) {
                            callback(eventData.toggleType);
                        }
                    }
                };
                window.addEventListener("message", onHamburgerMenuToggle, false);
            };
            
            /**
             * Resize hamburger menu layout
             * 
             * @returns
             */
            self.resizeHamburgerMenuLayout = function() {
                if(!self.xlargeScreen()) {
                    $("#offcanvasInnerContainer").width($(window).width());
                    $("#uifwkLayoutMainContainer").width($(window).width());
                }
                else {
                    if ($("#omcHamburgerMenu").length > 0 && $("#omcHamburgerMenu").is(':visible')) {
                        $('#uifwkLayoutMainContainer').width($(window).width() - 250);
                    }
                    else {
                        $("#uifwkLayoutMainContainer").width($(window).width());
                    }
                    $("#offcanvasInnerContainer").width($(window).width());
                }
            };
        }
        return UIFWKGlobalMenuUtil;
    }
);

