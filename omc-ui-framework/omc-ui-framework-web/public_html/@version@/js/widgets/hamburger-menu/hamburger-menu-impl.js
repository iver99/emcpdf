define('uifwk/@version@/js/widgets/hamburger-menu/hamburger-menu-impl', [
    'jquery', 
    'ojs/ojcore', 
    'knockout',
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg',
    'uifwk/@version@/js/util/df-util-impl',
    'uifwk/@version@/js/util/preference-util-impl', 
    'uifwk/@version@/js/sdk/context-util-impl',
    'uifwk/@version@/js/sdk/menu-util-impl',
    'uifwk/@version@/js/sdk/SessionCacheUtil',
    'uifwk/@version@/js/util/usertenant-util-impl',
    'uifwk/@version@/js/util/message-util-impl'
    //'ojs/ojnavigationlist',
    //'ojs/ojjsontreedatasource'
    ],
        function ($, oj, ko, nls, dfumodel, pfumodel, ctxmodel, menumodel, sessionCacheModel, utModel, msgModel) {
            function HamburgerMenuViewModel(params) {
                var self = this;
                var userName = params.userName;
                var tenantName = params.tenantName;
                var dfu = new dfumodel(userName, tenantName);
                var ctxUtil = new ctxmodel();
                var prefUtil = new pfumodel(dfu.getPreferencesUrl(), dfu.getDashboardsRequestHeader());
                var userTenantUtil = new utModel();
                var prefKeyHomeDashboardId = "Dashboards.homeDashboardId";
                var isSetAsHomeChecked = false;
                var omcHomeUrl = null;
                var menuUtil = new menumodel();
                var rootCompositeMenuid = 'omc_root_composite';
                var menuSessionCacheName = '_uifwk_servicemenucache';
                var sessionCacheAllMenusKey = 'omc_hamburger_menu';
                var sessionCacheOmcMenusDataKey = 'omc_menus';
                var sessionCacheServiceMenuDataKey = 'service_menu_data';
                var sessionCacheOmcMenusServiceLinksKey = 'service_links';
                var sessionCacheOmcMenusPrivilegeKey = 'privilege_list';
                var sessionCacheOmcMenusSubscribedAppsKey = 'subscribed_apps';
                var sessionCacheBaseVanityUrlsKey = 'base_vanity_urls';
                var sessionCacheUserRolesKey = 'user_roles';
                var sessionCacheAllServiceDataKey = 'all_service_data';
                var sessionCacheFavoriteDashboardKey = 'favorite_dashboards';
                var omcMenuSeparatorId = 'omc_service_menu_separator';
                
                var userName = params.userName;
                var tenantName = params.tenantName;
                var serviceAppId = params.appId;
                self.selectedItem = ko.observable();
                self.expanded = ko.observableArray([]);
                
                //
                // sessionStorage cache
                //
                var sessionCaches = [];
                var sessionCacheNames = [menuSessionCacheName];
                for (var i = 0; i < sessionCacheNames.length; i++) {
                    sessionCaches.push(new sessionCacheModel(sessionCacheNames[i], 1));
                }
                if (window.performance) {
                    //We should only clear the cache once during a page refresh, otherwise
                    //it may cause cached data lost though service menus already fetched
                    if (window.performance.navigation.type === 1 && !window._uifwk.isOmcServiceMenuCacheCleared) {
                        for (var i = 0; i < sessionCaches.length; i++) {
                            sessionCaches[i].clearCache();
                        }
                        window._uifwk.isOmcServiceMenuCacheCleared = true;
                    }
                }
                
                //Clear old composite menu items before show up a new composite menu
                function clearCompositeMenuItems() {
                    var size = omcMenus.length;
                    if (omcMenus[size-1] && omcMenus[size-1].attr.id === rootCompositeMenuid) {
                        omcMenus.pop();
                    }
                    if (self.serviceMenuData[size-1] && self.serviceMenuData[size-1].id === rootCompositeMenuid) {
                        self.serviceMenuData.pop();
                    }
                    currentCompositeParentId = null;
                }
                
                var currentCompositeParentId = null;
                var processingObjMenuName = null;
                //Show up a composite menu
                function jumpToCompositeMenu(parentMenuId, rootMenuLabel, menuJson) {
                    if (menuJson && menuJson.serviceCompositeMenus && rootMenuLabel !== processingObjMenuName) {
                        processingObjMenuName = rootMenuLabel;
                        clearCompositeMenuItems();
                        currentCompositeParentId = parentMenuId;
                        if (menuJson.serviceMenuMsgBundle) {
                            var url = menuJson.serviceMenuMsgBundle;
                            url = url.substring(url.indexOf('/emsaasui/') + 1, url.length - 3);
                            
                            require(['ojL10n!' + url], function (_nls) {
                                var rootCompositMenuItem = {'id': rootCompositeMenuid, 
                                                    'labelKey': rootMenuLabel, 
                                                    'externalUrl': '#', 
                                                    'children': menuJson.serviceCompositeMenus};
                                rootCompositMenuItem = applyNlsOnMenu(rootCompositMenuItem, _nls, serviceAppId);
                                self.serviceMenuData.push(rootCompositMenuItem);
                                var compositeMenu = getMenuItem(rootCompositMenuItem);
                                omcMenus.push(compositeMenu);
                                self.expanded([rootCompositeMenuid]);
                                self.dataSource(new oj.JsonTreeDataSource(omcMenus));
//                                $("#omcMenuNavList").ojNavigationList("refresh");
                                //Set current composite menu item if set
                                if (window._uifwk && window._uifwk.currentOmcMenuItemId) {
                                    var currentMenuId = window._uifwk.currentOmcMenuItemId;
                                    var underOmcAdmin = window._uifwk.underOmcAdmin;
                                    var foundCompositeItem = findItem(rootCompositMenuItem, currentMenuId);
                                    if (foundCompositeItem) {
                                        menuUtil.setCurrentMenuItem(currentMenuId, underOmcAdmin);
                                    }
                                }
                                processingObjMenuName = null;
                            });
                        }
                        else {
                            processingObjMenuName = null;
                        }
                    }                  
                }
                menuUtil.subscribeCompositeMenuDisplayEvent(jumpToCompositeMenu);
                
                //Register service menus at runtime when UIFWK registerServiceMenus() API is called
                function registerServiceMenus(menuJson) {
                    if (menuJson) {
                        var serviceItem = {};
                        serviceItem.appId = serviceAppId;
                        serviceItem.serviceMenus = menuJson.serviceMenus;
                        serviceItem.serviceAdminMenus = menuJson.serviceAdminMenus;
                        var msgBundleUrl = menuJson.serviceMenuMsgBundle;
                        if (msgBundleUrl) {
                            msgBundleUrl = msgBundleUrl.substring(msgBundleUrl.indexOf('/emsaasui/') + 1, msgBundleUrl.length - 3);
                        }
                        //Load resource bundle file for NLSed strings
                        require(['ojL10n!' + msgBundleUrl], function (_nls) {
                            serviceItem.serviceMenuMsgBundle = _nls;
                            serviceItem.serviceMenus = applyNlsOnMenu(serviceItem.serviceMenus, _nls, serviceAppId);
                            serviceItem.serviceAdminMenus = applyNlsOnMenu(serviceItem.serviceAdminMenus, _nls, serviceAppId);
                            
                            var menuId = findAppItemIndex(rootMenuData, 'omc_root_' + serviceAppId);
                            if (self.serviceMenuData[menuId]) {
                                if (serviceItem.serviceMenus) {
                                    self.serviceMenuData[menuId].children = serviceItem.serviceMenus;
                                    var menuItem = getMenuItem(self.serviceMenuData[menuId]);
                                    omcMenus[menuId] = menuItem;
                                }
                                if (serviceItem.serviceAdminMenus && serviceItem.serviceAdminMenus.children) {
                                    if (serviceItem.serviceMenus && serviceItem.serviceMenus.length > 0) {
                                        var lastServiceMenuItem = serviceItem.serviceMenus[serviceItem.serviceMenus.length - 1];
                                        if (lastServiceMenuItem.type !== 'divider') {
                                            var dividerItem = generateDividerItem('omc_' + serviceItem.appId);
                                            self.serviceMenuData[menuId].children.push(dividerItem);
                                            omcMenus[menuId].children.push(getMenuItem(dividerItem));
                                        }
                                    }
                                    self.serviceMenuData[menuId].children.push(serviceItem.serviceAdminMenus);
                                    omcMenus[menuId].children.push(getMenuItem(serviceItem.serviceAdminMenus));
                                    var adminMenuId = findAppItemIndex(self.serviceMenuData,'omc_root_admin');
                                    var adminSubMenuId = findAppItemIndex(self.serviceMenuData[adminMenuId].children, 'omc_root_admin_grp_'+serviceItem.appId);
                                    if (adminSubMenuId > -1) {
                                        var serviceAdminItem = getMenuItem(serviceItem.serviceAdminMenus);
                                        if (serviceItem.serviceAdminMenus.children) {
                                            self.serviceMenuData[adminMenuId].children[adminSubMenuId].children = serviceItem.serviceAdminMenus.children;
                                            omcMenus[adminMenuId].children[adminSubMenuId].attr['disabled'] = false;
                                            //Update parent admin menu's external url
                                            if (serviceAdminItem.attr.externalUrl && serviceAdminItem.attr.externalUrl !== '#') {
                                                self.serviceMenuData[adminMenuId].children[adminSubMenuId].externalUrl = serviceAdminItem.attr.externalUrl;
                                                self.serviceMenuData[adminMenuId].children[adminSubMenuId].disabled = false;
                                                omcMenus[adminMenuId].children[adminSubMenuId].attr.externalUrl = serviceAdminItem.attr.externalUrl;
                                                omcMenus[adminMenuId].children[adminSubMenuId].attr.serviceNameForVanityUrl = serviceAdminItem.attr.serviceNameForVanityUrl;
                                            }
                                            omcMenus[adminMenuId].children[adminSubMenuId].children = serviceAdminItem.children;
                                        }
                                        else {
                                            delete self.serviceMenuData[adminMenuId].children[adminSubMenuId]['children'];
                                            omcMenus[adminMenuId].children[adminSubMenuId].attr['disabled'] = true;
                                            //Update parent admin menu's external url
                                            self.serviceMenuData[adminMenuId].children[adminSubMenuId].externalUrl = serviceAdminItem.attr.externalUrl;
                                            self.serviceMenuData[adminMenuId].children[adminSubMenuId].disabled = true;
                                            omcMenus[adminMenuId].children[adminSubMenuId].attr.externalUrl = serviceAdminItem.attr.externalUrl;
                                            omcMenus[adminMenuId].children[adminSubMenuId].attr.serviceNameForVanityUrl = serviceAdminItem.attr.serviceNameForVanityUrl;
                                            delete omcMenus[adminMenuId].children[adminSubMenuId]['children'];
                                        }
                                    }
                                    else {
                                        if (!self.serviceMenuData[adminMenuId].children) {
                                            self.serviceMenuData[adminMenuId].children = [];
                                        }
                                        self.serviceMenuData[adminMenuId].children.push($.extend(true, {}, serviceItem.serviceAdminMenus));
                                        self.serviceMenuData[adminMenuId].children[self.serviceMenuData[adminMenuId].children.length-1].id = 'omc_root_admin_grp_'+serviceItem.appId;
                                        omcMenus[adminMenuId].children.push(getMenuItem(self.serviceMenuData[adminMenuId].children[self.serviceMenuData[adminMenuId].children.length-1]));
                                    }
                                }
                                
                                self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                            }
                            
                        });                        
                    }
                }
                
                menuUtil.subscribeServiceMenuRegisterEvent(registerServiceMenus);
                
                self.hamburgerRootMenuLabel = nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_LABEL;
                var rootMenuData = [
                    {'id': 'omc_root_home', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_HOME_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_alerts', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_ALERTS_LABEL, 'externalUrl': '#'},
//                    {'id': 'omc_root_applications', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_APPS_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_dashboards', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_DASHBOARDS_LABEL, 'externalUrl': '#'},
//                    {'id': 'omc_root_savedsearches', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_SAVEDSEARCH_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_dataexplorer', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_DATAEXPLORER_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_divider', type: 'divider', 'label': '', 'externalUrl': '#'},
                    {'id': 'omc_root_APM', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_APM_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_Monitoring', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_MONITORING_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_LogAnalytics', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_LA_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_ITAnalytics', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_ITA_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_Orchestration', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_ORCHESTRATION_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_SecurityAnalytics', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_SECURITY_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_Compliance', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_COMPLIANCE_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_divider1', type: 'divider', 'label': '', 'externalUrl': '#'},
                    {'id': 'omc_root_admin', type: 'menu_group', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_ADMIN_LABEL, 'externalUrl': '#', children: [
                            {'id': 'omc_root_admin_divider0', type: 'divider', 'label': '', 'externalUrl': '#'},
                            {'id': 'omc_root_admin_alertrules', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ADMIN_ALERTRULES_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_notificationChannels', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ADMIN_NOTIFICATIONCHANNELS_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_agents', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ADMIN_AGENTS_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_addentity', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ADMIN_ADDENTITY_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_clouddiscoveryprofiles', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ADMIN_CLOUDDISCOVERYPROFILES_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_entitiesconfig', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_ADMIN_ENTITIESCONFIG_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_divider', type: 'divider', 'label': '', 'externalUrl': '#'},
                            {'id': 'omc_root_admin_grp_APM', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_APM_ADMIN_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_grp_Monitoring', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_MONITORING_ADMIN_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_grp_LogAnalytics', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_LOG_ADMIN_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_grp_SecurityAnalytics', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_SECURITY_ADMIN_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_grp_Compliance', type: 'menu_item', 'label': nls.BRANDING_BAR_HAMBURGER_MENU_COMPLIANCE_ADMIN_LABEL, 'externalUrl': '#'}
                    ]}
                ];
                
                var defaultMenuIds = ['omc_root_home',
                    'omc_root_alerts',
                    'omc_root_dashboards',
                    'omc_root_admin', 
                    'omc_root_admin_alertrules', 
                    'omc_root_admin_notificationChannels',
                    'omc_root_admin_agents',
                    'omc_root_admin_addentity',
//                    'omc_root_admin_entitiesconfig',
                    rootCompositeMenuid
                ];

                self.privilegeList = null;
                self.subscribedApps = [];
                self.serviceMenuData = [];
                self.baseVanityUrls = null;
                self.userRoles = null;
                self.dataSource = ko.observable();
                var omcMenus = [];
                var globalMenuIdHrefMapping = null;
                var msgUtil = new msgModel();
                var hitErrorsWhenLoading = false;

                //Get role names for current user
                function getUserRoles() {
                    var dfdGetUserRoles = $.Deferred();
                    if (!self.userRoles) {
                        userTenantUtil.getUserRoles(function(data) {
                            if (data) {
                                self.userRoles = data;
                            }
                            else {
                                hitErrorsWhenLoading = true;
                                oj.Logger.error("Failed to get user roles during hamburger menu loading.");
                            }
                            dfdGetUserRoles.resolve();
                        }, true);
                    }
                    else {
                        dfdGetUserRoles.resolve();
                    }
                    
                    return dfdGetUserRoles;
                }
                
                //Get favorite dashboards
                function getFavoriteDsb(forceRefresh) {
                    var dfdGetFavoriteDsb = $.Deferred();
                    if(!forceRefresh && self.favoriteDsb){
                        dfdGetFavoriteDsb.resolve();//use the cached data
                        return dfdGetFavoriteDsb;
                    }
                    self.favoriteDsb = [];
                    var header = dfu.getDefaultHeader();
                    var url = dfu.isDevMode() ? dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint, "dashboards") : '/sso.static/dashboards.service';
                    url+='?offset=0&orderBy=default&filter=favorites';
                    dfu.ajaxWithRetry(url, {
                        type: 'get',
                        dataType: 'json',
                        contentType: "application/json",
                        headers: header,
                        success: function (data) {
                            $.each(data.dashboards, function (idx, dsb) {
                                self.favoriteDsb.push({name: dsb.name, href: "/emsaasui/emcpdfui/builder.html?dashboardId="+dsb.id});
                            });
                            dfdGetFavoriteDsb.resolve();
                        },
                        error: function (xhr, textStatus, errorThrown) {
                            oj.Logger.error("Failed to load service menus due to error: " + textStatus);
                            dfdGetFavoriteDsb.resolve();
                        }
                    });
                    return dfdGetFavoriteDsb;
                }

                function updateFavoriteDsb(toRefresh){
                    function updateFavoriteDsbInServiceMenuData(){
                        var menuId = findAppItemIndex(rootMenuData, 'omc_root_dashboards');
                        if(self.favoriteDsb){
                            self.serviceMenuData[menuId].children = [];
                            $.each(self.favoriteDsb, function (idx, dsb) {
                                self.serviceMenuData[menuId].children.push({'appId': 'Dashboard', 'id': 'omc_dashboards_grp_' + idx, type: 'menu_item', 'label': dsb.name, 'externalUrl': dsb.href, 'disabled': false});
                            });
                        }else{
                            delete self.serviceMenuData[menuId].children;
                        }
                    }
                    if(toRefresh){
                        $.when(getFavoriteDsb(toRefresh)).done(function(){
                            updateFavoriteDsbInServiceMenuData();
                            var menuId = findAppItemIndex(rootMenuData, 'omc_root_dashboards');
                            var item = self.serviceMenuData[menuId];
                            var menuItem = getMenuItem(item);
                            $.each(omcMenus, function(idx, _item){
                                if(menuItem.attr && _item.attr && _item.attr.id === menuItem.attr.id){
                                    omcMenus[idx] = menuItem;
                                }
                            });
                            self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                            $("#omcMenuNavList").ojNavigationList("refresh");
                            sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheOmcMenusDataKey, omcMenus);
                            sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheServiceMenuDataKey, self.serviceMenuData);
                        });
                    }else{
                        updateFavoriteDsbInServiceMenuData();
                    }
                }
                menuUtil.subscribeFavoriteDsbChangedEvent(function(){
                    updateFavoriteDsb(true);
                });

                //Get vanity base URLs for all subscribed services
                function fetchBaseVanityUrls() {
                    var dfdFetchBaseVanityUrls = $.Deferred();
                    var host = window.location.host;
                    if (host.indexOf(tenantName + '.') === 0) {
                        menuUtil.getServiceBaseVanityUrls(function(data){
                            if (!data) {
                                hitErrorsWhenLoading = true;
                                oj.Logger.error("Failed to get base vanity URLs during hamburger menu loading.");
                            }
                            self.baseVanityUrls = data;
                            dfdFetchBaseVanityUrls.resolve();
                        });
                    }
                    else {
                        dfdFetchBaseVanityUrls.resolve();
                    }
                    return dfdFetchBaseVanityUrls;
                }
                
                //Determine which services menu need to be initially loaded
                function determineInitialServiceMenusToLoad(allServiceMenus) {
                    var currentMenuId = $.isFunction(params.omcCurrentMenuId) ? params.omcCurrentMenuId() : params.omcCurrentMenuId;
                    self.serviceMenuToLoad = [];
                    self.serviceMenuDeferLoad = [];
                    if (allServiceMenus && allServiceMenus.length > 0) {
                        if ($.inArray(serviceAppId, ['Error', 'Dashboard']) === -1) {
                            if (currentMenuId && currentMenuId.indexOf('omc_root_admin_') === 0) {
                                self.serviceMenuToLoad = $.extend([], allServiceMenus);
                            }
                            else {
                                for (var i = 0; i < allServiceMenus.length; i++) {
                                    var menuDefItem = allServiceMenus[i];
                                    if (menuDefItem && menuDefItem.appId === serviceAppId) {
                                        self.serviceMenuToLoad.push(menuDefItem);
                                    }
                                    else {
                                        self.serviceMenuDeferLoad.push(menuDefItem);
                                    }
                                }
                            }
                        }
                        else {
                            self.serviceMenuDeferLoad = $.extend([], allServiceMenus);
                        }
                    }
                }

                function markLoadedServiceMenus(appId) {
                    if (self.serviceLinks && self.serviceLinks.length > 0) {
                        var serviceMenus = self.serviceLinks;
                        for (var i = 0; i < serviceMenus.length; i++) {
                            var menuDefItem = serviceMenus[i];
                            if (menuDefItem && menuDefItem.appId === appId) {
                                menuDefItem.loaded = true;
                                break;
                            }
                        }
                    }
                }

                function isServiceMenuLoaded(appId) {
                    if (self.serviceLinks && self.serviceLinks.length > 0) {
                        var serviceMenus = self.serviceLinks;
                        var serviceMenuDef = null;
                        for (var i = 0; i < serviceMenus.length; i++) {
                            var menuDefItem = serviceMenus[i];
                            if (menuDefItem && menuDefItem.appId === appId) {
                                serviceMenuDef = menuDefItem;
                                break;
                            }
                        }
                        if (!serviceMenuDef || serviceMenuDef.loaded) {
                            return true;
                        }
                        return false;
                    }
                    return true;
                }

                function findServiceMenuLinkByAppId(appId) {
                    if (self.serviceLinks && self.serviceLinks.length > 0) {
                        var serviceMenus = self.serviceLinks;
                        for (var i = 0; i < serviceMenus.length; i++) {
                            var menuDefItem = serviceMenus[i];
                            if (menuDefItem && menuDefItem.appId === appId) {
                                return menuDefItem;
                            }
                        }
                    }
                    return null;
                }

                function loadDummyDeferServiceMenus() {
                    if(!self.allServiceData || self.allServiceData.length < 1){
                        self.allServiceData = [];
                    }
                    $.each(self.serviceMenuDeferLoad, function(idx, linkItem){
                        var serviceItem = {};
                        serviceItem.appId = linkItem.appId;
                        serviceItem.serviceName = linkItem.serviceName;
                        serviceItem.version = linkItem.version;
                        serviceItem.serviceMenuMsgBundle = {};
                        serviceItem.serviceMenus = [{"id": "omc_service_menu_separator"}];
                        serviceItem.serviceAdminMenus = {};
                        self.allServiceData.push(serviceItem);
                    });
                }

                function getServiceMenuDefIndex(appId) {
                    if (self.allServiceData && self.allServiceData.length > 0) {
                        for (var i = 0; i < self.allServiceData.length; i++) {
                            var menuDefItem = self.allServiceData[i];
                            if (menuDefItem && menuDefItem.appId === appId) {
                                return i;
                            }
                        }
                    }
                    return -1;
                }

                function loadServiceMenuData(singleServiceData) {
                    //Find index for parent root menu item
                    var menuId = findAppItemIndex(rootMenuData, 'omc_root_' + singleServiceData.appId);
                    if (self.serviceMenuData[menuId]) {
                        if (singleServiceData.serviceMenus) {
                            self.serviceMenuData[menuId].children = singleServiceData.serviceMenus;
                        }
                        //Load service admin menu items
                        if (singleServiceData.serviceAdminMenus && singleServiceData.serviceAdminMenus.children) {
                            //Add a separator between service menus and admin menus if there is no one defined in JSON
                            if (singleServiceData.serviceMenus && singleServiceData.serviceMenus.length > 0) {
                                var lastServiceMenuItem = singleServiceData.serviceMenus[singleServiceData.serviceMenus.length - 1];
                                if (lastServiceMenuItem.type !== 'divider') {
                                    var dividerItem = generateDividerItem('omc_' + singleServiceData.appId);
                                    self.serviceMenuData[menuId].children.push(dividerItem);
                                }
                            }
                            //Append service admin menus to global service admin menus
                            self.serviceMenuData[menuId].children.push(singleServiceData.serviceAdminMenus);
                            var adminMenuId = findAppItemIndex(self.serviceMenuData,'omc_root_admin');
                            var adminSubMenuId = findAppItemIndex(self.serviceMenuData[adminMenuId].children, 'omc_root_admin_grp_'+singleServiceData.appId);
                            if (adminSubMenuId > -1) {
                                var adminExternalUrl = singleServiceData.serviceAdminMenus.externalUrl;
                                if (adminExternalUrl && adminExternalUrl !== '#') {
                                    self.serviceMenuData[adminMenuId].children[adminSubMenuId].externalUrl = adminExternalUrl;
                                    self.serviceMenuData[adminMenuId].children[adminSubMenuId].serviceNameForVanityUrl = singleServiceData.serviceAdminMenus.serviceNameForVanityUrl;
                                }
                                self.serviceMenuData[adminMenuId].children[adminSubMenuId].requiredPrivileges = singleServiceData.serviceAdminMenus.requiredPrivileges;
                                self.serviceMenuData[adminMenuId].children[adminSubMenuId].children = [];
                                        for(var i = 0; i< singleServiceData.serviceAdminMenus.children.length; ++i){
                                self.serviceMenuData[adminMenuId].children[adminSubMenuId].children.push($.extend(true, {}, singleServiceData.serviceAdminMenus.children[i]));
                                        }
                            }
                            else {
                                if (!self.serviceMenuData[adminMenuId].children) {
                                    self.serviceMenuData[adminMenuId].children = [];
                                }
                                self.serviceMenuData[adminMenuId].children.push($.extend(true, {}, singleServiceData.serviceAdminMenus));
                                self.serviceMenuData[adminMenuId].children[rootMenuData[adminMenuId].children.length].id = 'omc_root_admin_grp_'+singleServiceData.appId;
                                adminSubMenuId = rootMenuData[adminMenuId].children.length;
                            }
                            self.serviceMenuData[adminMenuId].children[adminSubMenuId].children = addPrefixForRootAdminSubMenu(self.serviceMenuData[adminMenuId].children[adminSubMenuId].children);
                        }
                    }
                }

                function updateServiceMenuCache() {
                    sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheOmcMenusServiceLinksKey, self.serviceLinks);
                    sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheOmcMenusDataKey, omcMenus);
                    sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheServiceMenuDataKey, self.serviceMenuData);
                }

                function loadSingleServiceMenuJson(linkItem) {
                    var dfdLoadSingleSvcMenu = $.Deferred();
                    if (linkItem) {
//                        if (needCount && !self.loadedServiceCnt) {
//                            self.loadedServiceCnt = ko.observable(0);
//                        }
                        var serviceItem = {};
                        serviceItem.appId = linkItem.appId;
                        serviceItem.serviceName = linkItem.serviceName;
                        serviceItem.version = linkItem.version;
                        var header = dfu.getDefaultHeader();

                        var url = linkItem.metaDataHref;
                        url = url.substring(url.indexOf('/emsaasui/'));
                        dfu.ajaxWithRetry(url, {
                            type: 'get',
                            dataType: 'json',
                            contentType: "application/json",
                            headers: header,
                            success: function (data) {
                                serviceItem.serviceMenus = data.serviceMenus;
                                serviceItem.serviceAdminMenus = data.serviceAdminMenus;
                                url = data.serviceMenuMsgBundle;
                                if (!url){
                                    hitErrorsWhenLoading = true;
                                    oj.Logger.error("No message file is defined in the service menu json file. Service name: " + serviceItem.serviceName);
                                    dfdLoadSingleSvcMenu.resolve();
//                                    if (needCount) {
//                                        self.loadedServiceCnt(self.loadedServiceCnt() + 1);
//                                    }
                                    return;
                                }
                                url = url.substring(url.indexOf('/emsaasui/') + 1, url.length - 3);

                                //Load resource bundle files
                                require(['ojL10n!' + url], function (_nls) {
                                    serviceItem.serviceMenuMsgBundle = _nls;
                                    serviceItem.serviceMenus = applyNlsOnMenu(serviceItem.serviceMenus, _nls, serviceItem.appId);
                                    serviceItem.serviceAdminMenus = applyNlsOnMenu(serviceItem.serviceAdminMenus, _nls, serviceItem.appId);
                                    var menuDefIndex = getServiceMenuDefIndex(linkItem.appId);
                                    if (menuDefIndex > -1) {
                                        self.allServiceData[menuDefIndex] = serviceItem;
                                    }
                                    else {
                                        self.allServiceData.push(serviceItem);
                                    }
                                    markLoadedServiceMenus(linkItem.appId);
                                    dfdLoadSingleSvcMenu.resolve();
//                                    if (needCount) {
//                                        self.loadedServiceCnt(self.loadedServiceCnt() + 1);
//                                    }
                                },
                                function() {
                                    hitErrorsWhenLoading = true;
                                    oj.Logger.error("Failed to load message file for service menus. Service name: " + serviceItem.serviceName);
                                    dfdLoadSingleSvcMenu.resolve();
//                                    if (needCount) {
//                                        self.loadedServiceCnt(self.loadedServiceCnt() + 1);
//                                    }
                                });
                            },
                            error: function (xhr, textStatus, errorThrown) {
//                                if (needCount) {
//                                    self.loadedServiceCnt(self.loadedServiceCnt() + 1);
//                                }
                                dfdLoadSingleSvcMenu.resolve();
                                hitErrorsWhenLoading = true;
                                oj.Logger.error("Failed to load service menu json file due to error: " + textStatus + ". Service name: " + serviceItem.serviceName);
                            }
                        });
                    }
                    else {
                        dfdLoadSingleSvcMenu.resolve();
                    }
                    return dfdLoadSingleSvcMenu;
                }

                function findAllUnloadedServiceMenus() {
                    var unloadedMenus = [];
                    if(self.serviceLinks && self.serviceLinks.length > 0){
                        $.each(self.serviceLinks, function(idx, linkItem){
                            if (!isServiceMenuLoaded(linkItem.appId)) {
                                unloadedMenus.push(linkItem);
                            }
                        });
                    }
                    return unloadedMenus;
                }

                function refreshSingleServiceMenu(appId, key, callback) {
                    var svcMenuLink = findServiceMenuLinkByAppId(appId);
                    loadSingleServiceMenuJson(svcMenuLink).done(function() {
                        omcMenus = [];
                        var singleServiceData = null;
                        var menuDefIndex = getServiceMenuDefIndex(appId);
                        if (menuDefIndex > -1) {
                            singleServiceData = self.allServiceData[menuDefIndex];
                            loadServiceMenuData(singleServiceData);
                            for (var j = 0; j < self.serviceMenuData.length; j++) {
                                var item = self.serviceMenuData[j];
                                var menuItem = getMenuItem(item);
                                omcMenus.push(menuItem);
                            }
                            if (key) {
                                self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                                setTimeout(function(){$("#hamburgerMenu #navlistcontainer>div").ojNavigationList("expand", key, true);}, 1);
                            }
                            updateServiceMenuCache();
                            if (callback) {
                                callback();
                            }
                        }
                    });
                }

                function refreshAllUnloadedServiceMenus() {
                    var dfdRefreshUnloadedMenus = $.Deferred();
                    var allUnloadedMenus = findAllUnloadedServiceMenus();
                    if (allUnloadedMenus && allUnloadedMenus.length > 0) {
                        loadServiceData(allUnloadedMenus).done(function() {
                            omcMenus = [];
                            $.each(allUnloadedMenus, function(idx, linkItem){
                                var singleServiceData = null;
                                var menuDefIndex = getServiceMenuDefIndex(linkItem.appId);
                                if (menuDefIndex > -1) {
                                    singleServiceData = self.allServiceData[menuDefIndex];
                                    loadServiceMenuData(singleServiceData);
                                }
                            });

                            for (var j = 0; j < self.serviceMenuData.length; j++) {
                                var item = self.serviceMenuData[j];
                                var menuItem = getMenuItem(item);
                                omcMenus.push(menuItem);
                            }
                            self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                            updateServiceMenuCache();
                            dfdRefreshUnloadedMenus.resolve();
                        });
                    }
                    else {
                        dfdRefreshUnloadedMenus.resolve();
                    }
                    return dfdRefreshUnloadedMenus;
                }

                //Load service menus from service registry JSON files
                function loadServiceMenus() {
                    var dfdLoadServiceMenus = $.Deferred();
                        dfu.getRegistrations(function(data){
                            self.registration = data;
                            fetchGlobalMenuLinks(self.registration);
                            if (dfu.isDevMode()) {
                                self.serviceLinks = [];
                                if (dfu.getDevData().globalMenuJSON && serviceAppId) {
                                    self.serviceLinks = [{
                                        'appId': serviceAppId,
                                        'metaDataHref': dfu.getDevData().globalMenuJSON
                                    }];
                                }
                            }
                            else {
                                self.serviceLinks = data.serviceMenus;
                            }
                            determineInitialServiceMenusToLoad(self.serviceLinks);
                            if (self.serviceMenuToLoad && self.serviceMenuToLoad.length > 0) {
                                loadServiceData(self.serviceMenuToLoad).done(function() {
                                    //Load dummy service menus if any
                                    loadDummyDeferServiceMenus();
                                    dfdLoadServiceMenus.resolve();
                                });
                            }
//                            if (self.serviceLinks && self.serviceLinks.length > 0) {
//                                loadServiceData(dfdLoadServiceMenus, self.serviceLinks);
//                            }
                            else {
                                //Load dummy service menus if any
                                loadDummyDeferServiceMenus();
                                dfdLoadServiceMenus.resolve();
                            }
                        }, true, function(){
                            hitErrorsWhenLoading = true;
                            oj.Logger.error("Failed to get registration data during hamburger menu loading.");
                            dfdLoadServiceMenus.resolve();
                        });
                    return dfdLoadServiceMenus;
                }
                
                //Get all privileges that have been granted to current user
                function getUserGrants() {
                    var dfdGetUserGrants = $.Deferred();
                    if (!self.privilegeList) {
                        function userGrantsCallback(data) {
                            if (!data) {
                                hitErrorsWhenLoading = true;
                                oj.Logger.error("Failed to get user granted privileges during hamburger menu loading.");
                            }
                            self.privilegeList = data;
                            dfdGetUserGrants.resolve();
                        }
                        userTenantUtil.getUserGrants(userGrantsCallback);
                    }
                    else {
                        dfdGetUserGrants.resolve();
                    }
                    return dfdGetUserGrants;
                }
                
                //Fetch all subscribed application names for current tenant user
                function getSubscribedApps() {
                    var dfdGetSubscribedApps = $.Deferred();
                    if (!self.subscribedApps || self.subscribedApps.length < 1) {
                        function subscribedAppsCallback(data) {
                            if (!data) {
                                dfdGetSubscribedApps.resolve();
                            }
                            else {
                                self.subscribedApps = data.applications;
                                dfdGetSubscribedApps.resolve();
                            }
                        }
                        dfu.getSubscribedApps2WithEdition(subscribedAppsCallback, function(){
                            hitErrorsWhenLoading = true;
                            oj.Logger.error("Failed to get user subscribed applications during hamburger menu loading.");
                            dfdGetSubscribedApps.resolve();});
                    }
                    else {
                        dfdGetSubscribedApps.resolve();
                    }
                    return dfdGetSubscribedApps;
                }
                
                //Load service menus meta-data from registered JSON files
                function loadServiceData(serviceLinks) {
                    var dfdLoadSvcMenuData = $.Deferred();
                    if(!self.allServiceData || self.allServiceData.length < 1){
                        self.allServiceData = [];
                    }
                        self.loadedServiceCnt = ko.observable(0);
                        self.loadedServiceCnt.subscribe(function(cnt) {
                            if (cnt === serviceLinks.length) {
                                dfdLoadSvcMenuData.resolve();
                            }
                        });
                        //Load json files
                        $.each(serviceLinks, function(idx, linkItem){
                            loadSingleServiceMenuJson(linkItem).done(function(){
                                self.loadedServiceCnt(self.loadedServiceCnt() + 1);
                            });
                        });
                    return dfdLoadSvcMenuData;
                }
                
                //Generate a menu separator item
                function generateDividerItem(prefix) {
                    var dividerId = prefix + '_' + [].toString.apply(window.crypto&&window.crypto.getRandomValues(new Uint32Array(1))||window.msCrypto&&window.msCrypto.getRandomValues(new Uint32Array(1)));
                    return {'id': dividerId, type: 'divider', 'labelKey': '', 'externalUrl': '#'};
                };
                
                function getGlobalMenuUrls(menuItem) {
                    if (menuItem) {
                        menuItem.externalUrl = globalMenuIdHrefMapping[menuItem.id] ? globalMenuIdHrefMapping[menuItem.id] : '#';
                        if (menuItem.children && menuItem.children.length > 0) {
                            for (var i = 0; i < menuItem.children.length; i++) {
                                getGlobalMenuUrls(menuItem.children[i]);
                            }
                        }
                    }
                }
                
                //If omc service menus have been cached in window session storage, get it directly from cache
                var cachedMenus = sessionCaches[0].retrieveDataFromCache(sessionCacheAllMenusKey);
                self.hamburgerMenuLoaded = ko.observable(false);
                if (cachedMenus && cachedMenus[sessionCacheOmcMenusDataKey] && cachedMenus[sessionCacheOmcMenusServiceLinksKey] && cachedMenus[sessionCacheOmcMenusPrivilegeKey] && cachedMenus[sessionCacheOmcMenusSubscribedAppsKey]) {
                    omcMenus = cachedMenus[sessionCacheOmcMenusDataKey];
                    self.subscribedApps = cachedMenus[sessionCacheOmcMenusSubscribedAppsKey];
                    self.serviceLinks = cachedMenus[sessionCacheOmcMenusServiceLinksKey];
                    self.privilegeList = cachedMenus[sessionCacheOmcMenusPrivilegeKey];
                    self.serviceMenuData = cachedMenus[sessionCacheServiceMenuDataKey];
                    self.baseVanityUrls = cachedMenus[sessionCacheBaseVanityUrlsKey];
                    self.userRoles = cachedMenus[sessionCacheUserRolesKey];
                    self.favoriteDsb = cachedMenus[sessionCacheFavoriteDashboardKey];
                    self.allServiceData = cachedMenus[sessionCacheAllServiceDataKey];

                    function refreshHamburgerMenuFromCache() {
                        cachedMenus = sessionCaches[0].retrieveDataFromCache(sessionCacheAllMenusKey);
                        omcMenus = cachedMenus[sessionCacheOmcMenusDataKey];
                        self.serviceMenuData = cachedMenus[sessionCacheServiceMenuDataKey];
                        self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                        self.hamburgerMenuLoaded(true);
                        //$("#omcHamburgerMenuInnerComp").show();
                        menuUtil.fireServiceMenuLoadedEvent();
                    }
                    if (!isServiceMenuLoaded(serviceAppId)) {
                        refreshSingleServiceMenu(serviceAppId, null, refreshHamburgerMenuFromCache);
                    }
                    else {
                        refreshHamburgerMenuFromCache();
                    }
                }
                //otherwise, get all service menus from service registries
                else {
                    $.when(loadServiceMenus(), getUserGrants(), getSubscribedApps(), fetchBaseVanityUrls(), getUserRoles(), getFavoriteDsb()).done(function() {
                        if (hitErrorsWhenLoading) {
                            //Show error message to warn user
                            msgUtil.showMessage({
                                "type": "warn",
                                "summary": nls.BRANDING_BAR_HAMBURGER_MENU_ERR_LOADING_SUMMARY,
                                "detail": nls.BRANDING_BAR_HAMBURGER_MENU_ERR_LOADING_DETAIL
                            });
                        }

                        fetchGlobalMenuLinks(self.registration);
                        for (var k = 0; k < rootMenuData.length; ++k) {
//                            rootMenuData[k].externalUrl = globalMenuIdHrefMapping[rootMenuData[k].id] ? globalMenuIdHrefMapping[rootMenuData[k].id] : '#';
                            getGlobalMenuUrls(rootMenuData[k]);
                            self.serviceMenuData.push($.extend(true, {}, rootMenuData[k]));
                        }
                        self.allServiceData && $.each(self.allServiceData, function (idx, singleServiceData) {
                            loadServiceMenuData(singleServiceData);
                        });
                        var toRefreshFavoriteDsb = false;
                        updateFavoriteDsb(toRefreshFavoriteDsb);
                        //Construct menu items as data source for jet ojNavigationList component
                        for (var j = 0; j < self.serviceMenuData.length; j++) {
                            var item = self.serviceMenuData[j];
                            var menuItem = getMenuItem(item);
                            omcMenus.push(menuItem);
                        }
                        //Cache the entire OMC menus and related data into window session storage
                        sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheOmcMenusSubscribedAppsKey, self.subscribedApps);
                        sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheOmcMenusServiceLinksKey, self.serviceLinks);
                        sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheOmcMenusPrivilegeKey, self.privilegeList);
                        sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheOmcMenusDataKey, omcMenus);
                        sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheServiceMenuDataKey, self.serviceMenuData);
                        sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheBaseVanityUrlsKey, self.baseVanityUrls);
                        sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheUserRolesKey, self.userRoles);
                        sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheFavoriteDashboardKey, self.favoriteDsb);
                        sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheAllServiceDataKey, self.allServiceData);
                        self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                        self.hamburgerMenuLoaded(true);
                        //$("#omcHamburgerMenuInnerComp").show();
                        menuUtil.fireServiceMenuLoadedEvent();
                    });
                }
                
                //Do privilege check
                function checkPrivilege(requiredPrivilege) {
                    if (!requiredPrivilege) {
                        return true;
                    }
                    if (dfu.isDevMode() && !dfu.getDevData().userGrants) {
                        return true;
                    }
                    
                    //Determine check mode to see it's role check or privilege check
                    var userPrivRoleList = null;
                    if (requiredPrivilege.checkMode && requiredPrivilege.checkMode.toUpperCase() === 'ROLE') {
                        userPrivRoleList = self.userRoles;
                    }
                    else if (requiredPrivilege.checkMode && requiredPrivilege.checkMode.toUpperCase() === 'PRIVILEGE') {
                        userPrivRoleList = self.privilegeList;
                    }
                    var checkList = requiredPrivilege.checkList;
                    if (!userPrivRoleList && checkList && checkList.length > 0) {
                        return false;
                    }
                    if (Array.isArray(checkList)) {
                        for (var _idx = 0; _idx < checkList.length; ++_idx) {
                            if (userPrivRoleList.indexOf(checkList[_idx]) < 0) {
                                return false;
                            }
                        }
                        return true;
                    } 
                    else {
                        if (userPrivRoleList.indexOf(checkList) < 0) {
                                return false;
                        }
                        else {
                            return true;
                        }
                    }
                }
                
                //Check whether a application is subscribed by current tenant
                function isAppSubscribed(appId) {
                    if (self.subscribedApps) {
                        for (var i = 0; i < self.subscribedApps.length; i++) {
                            if (self.subscribedApps[i].id === appId || 'Dashboard' === appId) {
                                return true;
                            }
                        }
                        return false;
                    }
                    else {
                        return false;
                    }
                }
                
                //Do privilege check and subscription check to disable/hide menu items
                function filterAuthorizedMenuItem(rawMenuObj){
                    if (!Array.isArray(rawMenuObj)) {
                        var menuItem = rawMenuObj;
                        //Some root menus should be always be there, like Home, Dashboards, Alerts etc.
                        if (defaultMenuIds.indexOf(menuItem.id) > -1) {
                            return menuItem;
                        }
                        //Disable Entities Config menu item if current user is not an admin
                        if (menuItem.id === 'omc_root_admin_entitiesconfig') {
                            if (!userTenantUtil.isAdminUser()) {
                                menuItem.disabled = true;
                                menuItem.externalUrl = '#';
                            }
                            return menuItem;
                        }
                        
                        //Subscription check
                        if (menuItem.id.indexOf("omc_root_") > -1) {
                            var appId = null;
                            //Do subscription check for Data Explorer, which is included in ITAnalytics service
                            if (menuItem.id === 'omc_root_dataexplorer') {
                                if(dfu.isV1ServiceTypes(self.subscribedApps)){
                                    appId = 'ITAnalytics';
                                }else{
                                    return menuItem;
                                }
                            }
                            else if(menuItem.id === 'omc_root_admin_clouddiscoveryprofiles'){
                                appId = "Monitoring";
                            }
                            else {
                                //If no service admin menus, disable the root admin menu item for that service
                                if (menuItem.id.indexOf('omc_root_admin_grp_') > -1) {
                                    if (!menuItem.children || menuItem.children.length === 0) {
                                        menuItem.disabled = true;
                                        return menuItem;
                                    }
                                    appId = menuItem.id.substring(19);
                                }
                                else {
                                    appId = menuItem.id.substring(9);
                                }
                            }
                            
                            //Disable menu item if service is not subscribed
                            if (!isAppSubscribed(appId)){
                                menuItem.disabled = true;
                                if (menuItem.children) {
                                    delete menuItem.children;
                                }
                                return menuItem;
                            }
                        }
                        
                        //Privilege check and disable/hide according to disableMode specified in JSON meta-data
                        if (menuItem && menuItem.id && menuItem.requiredPrivileges) {
                            if (!checkPrivilege(menuItem.requiredPrivileges)) {
                                if (menuItem.id.indexOf('omc_root') < 0 && menuItem.disableMode === "hidden"){
                                    return false;
                                }
                                //By default, disableMode will be disabled
                                else {
                                    menuItem.disabled = true;
                                    //if parent menu is disabled,remove sub menus
                                    if (menuItem.children) {
                                        delete menuItem.children;
                                    }
                                    return menuItem;
                                }
                            }
                        }
                        //Check child menu items
                        if (menuItem.children) {
                            for (var _idx = 0; _idx < menuItem.children.length; ++_idx) {
                                var childItem = menuItem.children[_idx];
                                if (!checkPrivilege(childItem.requiredPrivileges)) {
                                    if (childItem.disableMode === "hidden") {
                                        menuItem.children.splice(_idx, 1);
                                    }
                                    else {
                                        menuItem.children[_idx].disabled = true;
                                    }
                                }
                            }
                            if (menuItem.children.length === 0) {
                                delete menuItem.children;
                            }
                            else {
                                menuItem.disabled = false;
                            }
                        }
                        return menuItem;
                    }
//                    else {
//                        var menuItemList = [];
//                        for(_idx = 0; _idx < rawMenuObj.length; ++_idx){
//                            var menuItem = $.extend(true,{},rawMenuObj[_idx]);
//                            if(checkPrivilege(menuItem.children[_idx].id, menuItem.children[_idx].requiredPrivileges)){
//                                menuItemList.push(menuItem);
//                            }else if(menuItem.children[_idx].disableMode && menuItem.children[_idx].disableMode === "disabled"){
//                                menuItem.disabled = true;
//                                menuItemList.push(menuItem);
//                            }
//                        }
//                        return menuItemList;
//                    }
                }
                
                function getNlsString(key, bundle)
                {
                  // Account for dot separated nested keys
                  var keys = key ? key.split(".") : [], iteration = keys.length, index = 0, subkey = keys[index];

                  // even though we start with a valid bundle it's possible that part or all of the key is invalid, 
                  // so check we have a valid bundle in the while loop
                  while (--iteration > 0 && bundle) 
                  {
                    // if we have a key like a.b.c
                    bundle = bundle[subkey];
                    index++;
                    subkey = keys[index];
                  }

                  return bundle ? (bundle[subkey] || null) : null;
                }
                
                //Get NLSed string for menu item label and tooltips
                function applyNlsOnMenu(rawMenuObj, nlsObj, appId){
                    var _idx;
                    if(rawMenuObj && !Array.isArray(rawMenuObj)){
                        var menuItem = $.extend(true,{},rawMenuObj);
                        menuItem.appId = appId;
                        if (menuItem.id === omcMenuSeparatorId) {
                            menuItem.type = 'divider';
                            return menuItem;
                        }
                        else {
                            menuItem.type = 'menu_item';
                        }
                        if(menuItem && menuItem.labelKey){
                            var _labelKey = menuItem.labelKey;
                            var _labelValue = getNlsString(_labelKey, nlsObj);
                            menuItem.label = _labelValue ? _labelValue : _labelKey;
                        }
                        if(menuItem && menuItem.tooltipKey){
                            var _tooltipKey = menuItem.tooltipKey;
                            var _tooltipValue = getNlsString(_tooltipKey, nlsObj);
                            menuItem.tooltip = _tooltipValue ? _tooltipValue : _tooltipKey;
                        }
                        if(menuItem.children && menuItem.children.length > 0){
                            //Add a separator on the top of the child menu items
                            if (menuItem.children[0].id !== omcMenuSeparatorId) {
                                menuItem.children.unshift({'id': omcMenuSeparatorId});
                            }
                            for(_idx = 0; _idx < menuItem.children.length; ++_idx){
                                menuItem.children[_idx] = applyNlsOnMenu(menuItem.children[_idx], nlsObj, appId);
                            }
                        }
                        return menuItem;
                    } else if (rawMenuObj) {
                        var menuItemList = [];
                        if (rawMenuObj.length > 0 && rawMenuObj[0].id !== omcMenuSeparatorId) {
                            //Add a separator on the top of the child menu items
                            rawMenuObj.unshift({'id': omcMenuSeparatorId});
                        }
                        for(_idx = 0; _idx < rawMenuObj.length; ++_idx){
                            var menuItem = $.extend(true,{},rawMenuObj[_idx]);
                            menuItemList.push(applyNlsOnMenu(menuItem, nlsObj, appId));
                        }
                        return menuItemList;
                    }
                }
                
                function findAppItemIndex(items, id) {
                    if (id && items && items.length > 0) {
                        for (var i = 0; i < items.length; i++) {
                            if (id === items[i].id) {
                                return i;
                                break;
                            }
                        }
                    }
                    return -1;
                }
                
//                function findMenuTreeItemIndex(items, id) {
//                    if (id && items && items.length > 0) {
//                        for (var i = 0; i < items.length; i++) {
//                            if (id === items[i].attr['id']) {
//                                return i;
//                                break;
//                            }
//                        }
//                    }
//                    return -1;
//                }
                
                //Construct a valid menu item structure as data source for ojNavigationList
                function getMenuItem(item) {
                    if (item) {
                        if (item.type && item.type !== 'divider') {
                            item = filterAuthorizedMenuItem(item);
//                            if (item && (!item.externalUrl || item.externalUrl === '#') && item.children && item.children.length > 0) {
//                                item.serviceNameForVanityUrl = item.children[0].serviceNameForVanityUrl;
//                                item.externalUrl = item.children[0].externalUrl;
//                            }
                            //If vanity URL is required, construct the external URL to an vanity URL
                            if (item && item.externalUrl && item.serviceNameForVanityUrl) {
                                if (self.baseVanityUrls) {
                                    item.externalUrl = menuUtil.generateVanityUrl(item.externalUrl, self.baseVanityUrls[item.serviceNameForVanityUrl]);
                                }
                            }
                        }
                        var menuItem = {'attr': {'appId': item.appId, 'id': item.id, 'type': item.type, 'label': item.label, 'externalUrl': item.externalUrl, 
                                'disabled': item.disabled, 'selfHandleMenuSelection': item.selfHandleMenuSelection, 
                                'tooltip': item.tooltip, 'serviceNameForVanityUrl': item.serviceNameForVanityUrl}};
                        if (item && item.children && item.children.length > 0) {
                            menuItem.children = [];
                            for (var i = 0; i < item.children.length; i++) {
                                var itemWithChildren = getMenuItem(item.children[i]);
                                if (itemWithChildren) {
                                    menuItem.children.push(itemWithChildren);
                                }
                            }
                        }
                        return menuItem;
                    }
                    return null;
                }

                function findItem(item, menuId) {
                    if (item && item.id === menuId) {
                        return item;
                    }   
                    else if (item && item.children) {
                        var foundItem = null;
                        for (var i = 0; i < item.children.length; i++) {
                            foundItem = findItem(item.children[i], menuId);
                            if (foundItem) return foundItem;
                        }
                        return foundItem;
                    }
                }
                
                function findItemTrack(item, menuId) {
                    var itemStack = [];
                    function _findItem(_item){
                        if (_item && _item.id === menuId) {
                            itemStack.push(_item);
                            return true;
                        }
                        else if (_item && _item.children) {
                            for (var i = 0; i < _item.children.length; i++) {
                                itemStack.push(_item);
                                var foundItem = _findItem(_item.children[i], menuId);
                                if (foundItem){
                                    return true;
                                }else{
                                    itemStack.pop();
                                }
                            }
                        }
                    }
                    _findItem(item, menuId);
                    return itemStack;
                }
                
                //Add prefix for menu items in root admin menu to prevent duplicated id to the service admin menu
                function addPrefixForRootAdminSubMenu(rootAdminSubMenuList){
                    var _idx;
                    if(rootAdminSubMenuList && !Array.isArray(rootAdminSubMenuList)){
                        var menuItem = $.extend(true,{},rootAdminSubMenuList);
                        if (!menuItem || !menuItem.id || menuItem.id === omcMenuSeparatorId) {
                            return menuItem;
                        }
                        menuItem.id = "omcadmin_"+menuItem.id;
                        if(menuItem.children){
                            for(_idx = 0; _idx < menuItem.children.length; ++_idx){
                                menuItem.children[_idx] = addPrefixForRootAdminSubMenu(menuItem.children[_idx]);
                            }
                        }
                        return menuItem;
                    } else if (rootAdminSubMenuList) {
                        var menuItemList = [];
                        for(_idx = 0; _idx < rootAdminSubMenuList.length; ++_idx){
                            var menuItem = $.extend(true,{},rootAdminSubMenuList[_idx]);
                            menuItemList.push(addPrefixForRootAdminSubMenu(menuItem));
                        }
                        return menuItemList;
                    }
                }

                //Menu selection handler when a menu is clicked
                self.selectionHandler = function(data, event) {
                    self.selectedItem(data.id);
                    if (event.type === 'click' && ((data.id.indexOf('omc_root_') !== -1 || data.selfHandleMenuSelection === 'false' 
                            || !serviceAppId || data.appId !== serviceAppId))||(data.id.indexOf('omc_dashboards_grp_') !== -1)) {
                        handleMenuSelection(true, data);
                    }
                    else {
                        handleMenuSelection(false, data);
                    }
                };
                
                //Event handler before a menu item is collapsed
                self.beforeCollapse = function(event, ui) {
                    if (ui.key === rootCompositeMenuid) {
//                        currentCompositeParentId && self.selectedItem(currentCompositeParentId);
                        clearCompositeMenuItems();
                        self.expanded([]);
                        self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                        window._uifwk.isCompositeMenuShown = false;
                        //$("#omcMenuNavList").ojNavigationList("refresh");
                        if (window._uifwk.compositeMenuCollapseCallback) {
                            var callback = window._uifwk.compositeMenuCollapseCallback;
                            callback();
                        }
                    }
                    return true;
                    
                };
                
//                //Check user preferences to determine whether a dashboard has been set as Home
//                if (!isSetAsHomeChecked) {
//                    checkDashboardAsHomeSettings();
//                }
                
                function handleMenuSelection(uifwkControlled, data) {
                    var item = null;
                    var expandedIdList = $("#omcMenuNavList").ojNavigationList("getExpanded");
                    //If from composite menu
                    if (expandedIdList.length > 0 && expandedIdList[0] === rootCompositeMenuid) {
                        var found = findItem(self.serviceMenuData[self.serviceMenuData.length - 1], data.id);
                        if (found) {
                            item = found;
                        }
                    }
                    else {
                        //Otherwise from service menu
                        for (var j = 0; j < self.serviceMenuData.length; j++) {
                            var found = findItem(self.serviceMenuData[j], data.id);
                            if (found) {
                                item = found;
                                break;
                            }
                        }
                    }
                    
                    if (item && /*((item.id.indexOf("omc_root")>-1 && item.id.indexOf("omc_root_admin")<0) ||!item.children) &&*/ !item.disabled) {
                        if (!item.children || item.children.length <= 0) {
                            if (!window._uifwk) {
                                window._uifwk = {};
                            }
                            if (window._uifwk.stayInComposite && window._uifwk.compositeMenuJson) {
                                var rootCompositMenuItem = {'id': rootCompositeMenuid, 
                                                            'labelKey': window._uifwk.compositeMenuName, 
                                                            'externalUrl': '#', 
                                                            'children': window._uifwk.compositeMenuJson.serviceCompositeMenus};
                                if (!findItem(rootCompositMenuItem, data.id)) {
                                    window._uifwk.stayInComposite = false;
                                }
                            }
                            else {
                                window._uifwk.stayInComposite = false;
                            }
                            window._uifwk.currentOmcMenuItemId = data.id;
                        }
                        if(item.externalUrl && item.externalUrl !== '#' && item.children && item.children.length > 0){
                            self.preventExpandForAPMLabel = true;
                        }
                        else {
                            self.preventExpandForAPMLabel = false;
                        }
                        //Auto close hamburger menu when it's not in pinned status
                        if($("#omcHamburgerMenu").hasClass("oj-offcanvas-overlay") && 
                                (self.preventExpandForAPMLabel ||  !item.children || item.children.length <= 0)) {
                            oj.OffcanvasUtils.close({
                                "edge": "start",
                                "displayMode": "overlay",
                                "selector": "#omcHamburgerMenu",
                                "autoDismiss": "focusLoss"
                            });
                        }
                        if (item.selfHandleMenuSelection !== 'true' && item.children && item.children.length > 0) {
                            uifwkControlled = true;
                        }
                        if (uifwkControlled) {
                            var linkHref = item.externalUrl;
                            if(self.hrefMap && self.hrefMap[data.id]){
                                $("a#"+data.id)[0].href = self.hrefMap[data.id];
                                linkHref = self.hrefMap[data.id];
                                delete self.hrefMap[data.id];
                            }
                            if (data.id === 'omc_root_home') {
                                var dfdHomeSetting = checkDashboardAsHomeSettings();
                                dfdHomeSetting.done(function(){
                                    linkHref = omcHomeUrl ? omcHomeUrl : '/emsaasui/emcpdfui/welcome.html';
                                    window.location.href = ctxUtil.appendOMCContext(linkHref, true, true, true);
                                    omcHomeUrl = null;
                                    return false;
                                })
                                .fail(function() {
                                    linkHref = '/emsaasui/emcpdfui/welcome.html';
                                    window.location.href = ctxUtil.appendOMCContext(linkHref, true, true, true);
                                    omcHomeUrl = null;
                                    return false;
                                });
                            }
                            else {
                                if (linkHref && linkHref !== '#' && linkHref !== window.location+'#') {
                                    window.location.href = ctxUtil.appendOMCContext(linkHref, true, true, true);
                                    return false;
                                }
                            }
                        }
                        else {
                            fireMenuSelectionEvent(item);
                        }
                    }
                    return false;
                }
                
                //Build mapping from menu item id and service links fetched from dashborad registration data
                function fetchGlobalMenuLinks(data) {
                    globalMenuIdHrefMapping = {};
                    globalMenuIdHrefMapping['omc_root_home'] = omcHomeUrl ? omcHomeUrl : '/emsaasui/emcpdfui/welcome.html';
                    globalMenuIdHrefMapping['omc_root_alerts'] = fetchLinkFromRegistrationData(data, 'homeLinks', 'EventUI');
                    globalMenuIdHrefMapping['omc_root_dashboards'] = '/emsaasui/emcpdfui/home.html';
                    globalMenuIdHrefMapping['omc_root_dataexplorer'] = fetchLinkFromRegistrationData(data, 'visualAnalyzers', 'TargetAnalytics');
                    globalMenuIdHrefMapping['omc_root_APM'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'ApmUI');
                    globalMenuIdHrefMapping['omc_root_Monitoring'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'MonitoringServiceUI');
                    globalMenuIdHrefMapping['omc_root_LogAnalytics'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'LogAnalyticsUI');
                    globalMenuIdHrefMapping['omc_root_ITAnalytics'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'emcitas-ui-apps');
                    globalMenuIdHrefMapping['omc_root_Orchestration'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'CosUIService');
                    globalMenuIdHrefMapping['omc_root_SecurityAnalytics'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'SecurityAnalyticsUI');
                    globalMenuIdHrefMapping['omc_root_Compliance'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'ComplianceUIService');
//                    globalMenuIdHrefMapping['omc_root_admin'] = fetchLinkFromRegistrationData(data, 'adminLinks', 'EventUI');
                    globalMenuIdHrefMapping['omc_root_admin_alertrules'] = fetchLinkFromRegistrationData(data, 'adminLinks', 'EventUI', 'Alert Rules');
                    globalMenuIdHrefMapping['omc_root_admin_notificationChannels'] = fetchLinkFromRegistrationData(data, 'adminLinks', 'EventUI', 'Notification Channels')?fetchLinkFromRegistrationData(data, 'adminLinks', 'EventUI', 'Notification Channels'):'/emsaasui/eventUi/channels/html/channels-dashboard.html';
                    globalMenuIdHrefMapping['omc_root_admin_agents'] = fetchLinkFromRegistrationData(data, 'adminLinks', 'TenantManagementUI');
                    var registeredAddEntityLink = fetchLinkFromRegistrationData(data, 'adminLinks', 'MonitoringServiceUI', 'Add Entity');
                    var registeredCloudDiscoveryProfilesLink = fetchLinkFromRegistrationData(data, 'adminLinks', 'MonitoringServiceUI', 'Cloud Discovery Profiles');
                    var monitoringSvcUrl = fetchLinkFromRegistrationData(data, 'cloudServices', 'MonitoringServiceUI');
                    //In case Monitoring service's 'Add Entity', 'Cloud Discovery Profiles' are still not registered as admin links(EMCMS-8087)
                    //construct the URLs from base Monitoring URL
                    if (!registeredAddEntityLink) {
                        registeredAddEntityLink = monitoringSvcUrl ? monitoringSvcUrl + "?root=cmsDiscoveryStatus" : null;
                        if(registeredAddEntityLink && registeredAddEntityLink.indexOf('global-index.html') < 0 && registeredAddEntityLink.indexOf('index.html') > 0) {
                            registeredAddEntityLink = registeredAddEntityLink.replace('index.html','global-index.html');
                        }
                    }
                    if (!registeredCloudDiscoveryProfilesLink) {
                        registeredCloudDiscoveryProfilesLink = monitoringSvcUrl ? monitoringSvcUrl + "?root=cmsCloudProfilesDashboard" : null;
                        if(registeredCloudDiscoveryProfilesLink && registeredCloudDiscoveryProfilesLink.indexOf('global-index.html') < 0 && registeredCloudDiscoveryProfilesLink.indexOf('index.html') > 0) {
                            registeredCloudDiscoveryProfilesLink = registeredCloudDiscoveryProfilesLink.replace('index.html','global-index.html');
                        }
                    }
                    
                    globalMenuIdHrefMapping['omc_root_admin_addentity'] = registeredAddEntityLink ? registeredAddEntityLink : null;
                    globalMenuIdHrefMapping['omc_root_admin_clouddiscoveryprofiles'] = registeredCloudDiscoveryProfilesLink ? registeredCloudDiscoveryProfilesLink : null;
                    globalMenuIdHrefMapping['omc_root_admin_entitiesconfig'] = fetchLinkFromRegistrationData(data, 'adminLinks', 'AdminConsoleSaaSUi');
                }
                
                function fetchLinkFromRegistrationData(data, linkType, serviceName, linkName) {
                    if (data) {
                        var links = data[linkType];
                        if (links && links.length > 0) {
                            for (var i = 0; i < links.length; i++) {
                                if (links[i].serviceName === serviceName && (
                                                !linkName || (linkName && links[i].name && links[i].name.indexOf(linkName)>-1))) {
                                    return links[i].href;
                                }
                            }
                        }
                    }
                    return null;
                }
                
                //fire menu selection event to integrators
                function fireMenuSelectionEvent(data) {
                    var message = {'tag': 'EMAAS_OMC_GLOBAL_MENU_SELECTION'};
                    message.data = data;
                    window.postMessage(message, window.location.href);
                }
                
                function checkDashboardAsHomeSettings() {
                    var dfdCheckDashboardAsHomeSettings = $.Deferred();
                    function succCallback(data) {
                        var homeDashboardId = prefUtil.getPreferenceValue(data, prefKeyHomeDashboardId);
                        if (homeDashboardId) {
                            omcHomeUrl = "/emsaasui/emcpdfui/builder.html?dashboardId=" + homeDashboardId;
                        }
                        else {
                            omcHomeUrl = null;
                        }
                        isSetAsHomeChecked = true;
                        dfdCheckDashboardAsHomeSettings.resolve();
                    }
                    function errorCallback(jqXHR, textStatus, errorThrown) {
                        omcHomeUrl = null;
                        dfdCheckDashboardAsHomeSettings.reject();
                    }
                    var options = {
                        success: succCallback,
                        error: errorCallback
                    };
                    prefUtil.getAllPreferences(options);
                    return dfdCheckDashboardAsHomeSettings;
                }
//                $("#omcMenuNavList").on("ojbeforecurrentitem", function (event, ui) {
//                    // verify that the component firing the event is a component of interest ,
//                    //  verify whether the event is fired by js
//                    if ($(event.target).is("#omcMenuNavList") && !event.originalEvent) {
//                        $("#omcMenuNavList li").removeClass("oj-selected oj-focus");
//                        if(!self.onMenuItemExpand){
//                            $(ui.item[0]).addClass("oj-selected oj-focus");
//                        }else{
//                            self.onMenuItemExpand = false;
//                        }
//                    }
//                });
                $("#omcMenuNavList").on("ojbeforeselect", function (event, ui) {
                    // verify that the component firing the event is a component of interest ,
                    //  verify whether the event is fired by js
                    if ($(event.target).is("#omcMenuNavList")) {
                        if(!self.hrefMap) self.hrefMap = {};
                        var href = $("a#"+ui.key)[0].href;
                        if (href !== '#' && href !== window.location && href !== window.location+'#') {
                            self.hrefMap[ui.key] = href;
                        }
                        $("a#"+ui.key)[0].href = "#";
                    }
//                    if(ui.key.indexOf("omc_root") > -1 && ui.key.indexOf("omc_root_admin")<0){
//                        event.preventDefault();
//                        event.stopPropagation();
//                        event.stopImmediatePropagation();
//                        return false;
//                    }
                });
                $("#omcMenuNavList").on("ojbeforeexpand", function (event, ui) {
                    // verify that the component firing the event is a component of interest ,
                    //  verify whether the event is fired by js
                    if(/*ui.key.indexOf("omc_root")>-1 && ui.key.indexOf("omc_root_admin")<0 && */self.preventExpandForAPMLabel){
                        event.preventDefault();
                        event.stopPropagation();
                        self.preventExpandForAPMLabel = false;
                        return false;
                    }
                    else if (ui.key.indexOf("omc_root_") > -1 && ui.key.indexOf("omc_root_admin") < 0) {
                        var appId = ui.key.substring(9);
                        if (!isServiceMenuLoaded(appId)) {
                            event.preventDefault();
                            event.stopPropagation();
                            refreshSingleServiceMenu(appId, ui.key);
//                            $("#hamburgerMenu #navlistcontainer>div").ojNavigationList("expand",ui.key, true);
                        }
                    }
                    else if (ui.key === "omc_root_admin") {
                        //If not all service menus have been loaded, then load them
                        if (findAllUnloadedServiceMenus().length > 0) {
                            event.preventDefault();
                            event.stopPropagation();
                            refreshAllUnloadedServiceMenus().done(function(){
                                setTimeout(function(){$("#hamburgerMenu #navlistcontainer>div").ojNavigationList("expand", ui.key, true);});
                            });
                        }
                    }
                });
                
                window.addEventListener("contextmenu", function(event){
                    if (event.button === 2) {
                        if (($(event.target).is("#omcMenuNavList a") && $(event.target)[0].href === window.location+'#') ||
                                ($(event.target).is("#omcMenuNavList span") && $(event.target).parent().is("#omcMenuNavList a") && 
                                $(event.target).parent()[0].href === window.location+'#')) {
                            event.preventDefault();
                        }
                    }
                }, false);
                
                window.addEventListener("mousedown", function(event){
                    if (event.button === 2) {
                        if(!self.hrefMap) self.hrefMap = {};
                        if($(event.target).is("#omcMenuNavList li")){
                            if(self.hrefMap[$(event.target).find("a")[0].id]){
                                $(event.target).find("a")[0].href = ctxUtil.appendOMCContext(self.hrefMap[$(event.target).find("a")[0].id], true, true, true);
                            }else{
                                $(event.target).find("a")[0].href = ctxUtil.appendOMCContext($(event.target).find("a")[0].href, true, true, true);
                            }
                        }else if($(event.target).is("#omcMenuNavList a")){
                            if(self.hrefMap[$(event.target)[0].id]){
                                $(event.target)[0].href = ctxUtil.appendOMCContext(self.hrefMap[$(event.target)[0].id], true, true, true);
                            }else{
                                $(event.target)[0].href = ctxUtil.appendOMCContext($(event.target)[0].href, true, true, true);
                            }
                        }else if($(event.target).parent().is("#omcMenuNavList a")){
                            if(self.hrefMap[$(event.target).parent()[0].id]){
                                $(event.target).parent()[0].href = ctxUtil.appendOMCContext(self.hrefMap[$(event.target).parent()[0].id], true, true, true);
                            }else{
                                $(event.target).parent()[0].href = ctxUtil.appendOMCContext($(event.target).parent()[0].href, true, true, true);
                            }
                        }
                    }
                    return true;
                });
                //Set current menu item
                function listenToSetCurrentMenuItem() {
                    var messageTag = 'EMAAS_OMC_GLOBAL_MENU_SET_CURRENT_ITEM';
                    function onSetCurrentMenuItem(event) {
                        if (event.origin !== window.location.protocol + '//' + window.location.host) {
                            return;
                        }
                        var eventData = event.data;
                        //Only handle received message for global menu selection
                        if (eventData && eventData.tag && eventData.tag === messageTag) {
                            if(eventData.menuItemId){
                                function setCurrentMenuItem() {
                                    if(eventData.underOmcAdmin){
                                        for(var Key in menuUtil.OMCMenuConstants){
                                            if(menuUtil.OMCMenuConstants[Key] === eventData.menuItemId){
                                                eventData.underOmcAdmin = false;
                                            }
                                        }
                                    }
                                    if(eventData.underOmcAdmin){
                                        eventData.menuItemId = "omcadmin_" + eventData.menuItemId;
                                    }
                                    var itemTrack;
                                    for (var j = 0; j < self.serviceMenuData.length; j++) {
                                        itemTrack = findItemTrack(self.serviceMenuData[j], eventData.menuItemId);
                                        if (itemTrack.length>0) {
                                            break;
                                        }else{
                                            itemTrack = null;
                                        }
                                    }
                                    if(itemTrack){
                                        var expandedIdList = $("#omcMenuNavList").ojNavigationList("getExpanded");
                                        var trackIdList = [];
                                        while(itemTrack.length>0){
                                            trackIdList.push(itemTrack.shift().id);
                                        }
                                        while(expandedIdList.length>0 && trackIdList.length > 0 && expandedIdList[0] === trackIdList[0]){
                                            expandedIdList.shift();
                                            trackIdList.shift();
                                        }
                                        while(expandedIdList.length>0){
                                            var parentItemId = expandedIdList.pop();
                                            $("#hamburgerMenu #navlistcontainer>div").ojNavigationList("collapse",parentItemId, true);
                                        }
                                        setTimeout(function(){
                                        while(trackIdList.length>1){
                                            var parentItemId = trackIdList.shift();
                                            $("#hamburgerMenu #navlistcontainer>div").ojNavigationList("expand",parentItemId, true);
                                        }
                                        $("#hamburgerMenu #navlistcontainer>div").ojNavigationList("option", "selection", eventData.menuItemId);
                                        $("#hamburgerMenu #navlistcontainer>div").ojNavigationList("option", "currentItem", eventData.menuItemId);
                                        },0);
                                    }
                                }
                                if (eventData.menuItemId.indexOf("omc_root_admin_") > -1 && findAllUnloadedServiceMenus().length > 0) {
                                    //If not all service menus have been loaded, then load them
                                    refreshAllUnloadedServiceMenus().done(function(){
                                        setCurrentMenuItem();
//                                        setTimeout(function(){$("#hamburgerMenu #navlistcontainer>div").ojNavigationList("expand", ui.key, true);});
                                    });
                                }
                                else {
                                    setCurrentMenuItem();
                                }

                            }
                        }
                    }
                    window.addEventListener("message", onSetCurrentMenuItem, false);
                }
                
                listenToSetCurrentMenuItem();
            }
            return HamburgerMenuViewModel;
        });


