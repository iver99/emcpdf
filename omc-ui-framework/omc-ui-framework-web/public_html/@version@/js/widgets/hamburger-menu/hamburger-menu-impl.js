define([
    'jquery', 
    'ojs/ojcore', 
    'knockout',
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg',
    'uifwk/@version@/js/util/df-util-impl',
    'uifwk/@version@/js/util/preference-util-impl', 
    'uifwk/@version@/js/sdk/context-util-impl',
    'uifwk/@version@/js/sdk/menu-util-impl',
    'uifwk/@version@/js/sdk/SessionCacheUtil',
    'ojs/ojnavigationlist',
    'ojs/ojjsontreedatasource'],
        function ($, oj, ko, nls, dfumodel, pfumodel, ctxmodel, menumodel, sessionCacheModel) {
            function HamburgerMenuViewModel(params) {
                var self = this;
                var dfu = new dfumodel();
                var ctxUtil = new ctxmodel();
                var prefUtil = new pfumodel(dfu.getPreferencesUrl(), dfu.getDashboardsRequestHeader());
                var prefKeyHomeDashboardId = "Dashboards.homeDashboardId";
                var isSetAsHomeChecked = false;
                var omcHomeUrl = null;
                var menuUtil = new menumodel();
                var rootCompositeMenuid = 'omc_root_composite';
                var menuSessionCacheName = '_uifwk_servicemenucache';
                var sessionCacheAllMenusKey = 'omc_hamburger_menu';
                var sessionCacheOmcMenusDataKey = 'omc_menus';
                var sessionCacheServiceMenuDataKey = 'service_menu_data';
                var sessionCacheSelectedMenuIdKey = 'selected_menu_id';
                
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
                
                function clearCompositeMenuItems() {
                    var size = omcMenus.length;
                    if (omcMenus[size-1] && omcMenus[size-1].attr.id === rootCompositeMenuid) {
                        omcMenus.pop();
                    }
                    currentCompositeParentId = null;
                }
                
                var currentCompositeParentId = null;
                function jumpToCompositeMenu(parentMenuId, rootMenuLabel, menuJson) {
                    clearCompositeMenuItems();
                    currentCompositeParentId = parentMenuId;
                    var rootCompositMenuItem = {'id': rootCompositeMenuid, type: 'menu_item', 'labelKey': rootMenuLabel, 'externalUrl': '#', children: menuJson};
                    var compositeMenu = getMenuItem(rootCompositMenuItem);
//                    var parentMenuIndex = findTreeItemIndex(omcMenus, parentMenuId);
//                    if (parentMenuIndex > -1) {
//                        if (!omcMenus[parentMenuIndex].children) {
//                            omcMenus[parentMenuIndex].children = [];
//                        }
//                        omcMenus[parentMenuIndex].children.push(compositeMenu);
//                    }
                    
                    omcMenus.push(compositeMenu);
                    self.expanded([rootCompositeMenuid]);
                    self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                    $("#omcMenuNavList").ojNavigationList("refresh");
                    
//                    oj.OffcanvasUtils.toggle({
//                                                "edge": "start",
//                                                "displayMode": "push",
//                                //                "content": "#main-container",
//                                                "selector": "#omcHamburgerMenu"
//                                            });
//                    self.selectedItem('omc_composite_m1');
//                    $("omcMenuNavList").ojNavigationList("expand", {'key': rootCompositeMenuid, 'vetoable': true});                    
                }
                menuUtil.subscribeCompositeMenuDisplayEvent(jumpToCompositeMenu);
                
                self.hamburgerRootMenuLabel = nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_LABEL;
                var rootMenuData = [
                    {'id': 'omc_root_home', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_HOME_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_alerts', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_ALERTS_LABEL, 'externalUrl': '#'},
//                    {'id': 'omc_root_applications', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_APPS_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_dashboards', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_DASHBOARDS_LABEL, 'externalUrl': '#'},
//                    {'id': 'omc_root_savedsearches', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_SAVEDSEARCH_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_dataexplorer', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_DATAEXPLORER_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_divider', type: 'divider', 'labelKey': '', 'externalUrl': '#'},
                    {'id': 'omc_root_APM', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_APM_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_Monitoring', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_MONITORING_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_LogAnalytics', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_LA_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_ITAnalytics', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_ITA_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_Orchestration', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_ORCHESTRATION_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_SecurityAnalytics', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_SECURITY_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_Compliance', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_COMPLIANCE_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_divider1', type: 'divider', 'labelKey': '', 'externalUrl': '#'},
                    {'id': 'omc_root_admin', type: 'menu_group', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_ADMIN_LABEL, 'externalUrl': '#', children: [
                            {'id': 'omc_root_admin_alertrules', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ADMIN_ALERTRULES_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_agents', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ADMIN_AGENTS_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_entitiesconfig', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ADMIN_ENTITIESCONFIG_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_divider', type: 'divider', 'labelKey': '', 'externalUrl': '#'},
                            {'id': 'omc_root_admin_grp_APM', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_APM_ADMIN_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_grp_Monitoring', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_MONITORING_ADMIN_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_grp_LogAnalytics', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_LOG_ADMIN_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_grp_SecurityAnalytics', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_SECURITY_ADMIN_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_grp_Compliance', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_COMPLIANCE_ADMIN_LABEL, 'externalUrl': '#'}
                    ]}
                ];

                self.menuDataRequestingNum = ko.observable(0);
                function getServiceData(serviceLinks) {
                    if(!self.allServiceData || self.allServiceData.length < 1){
                        self.allServiceData = [];
                        $.each(serviceLinks, function(idx, linkItem){
                            var serviceItem = {};
                            serviceItem.appId = linkItem.appId;
                            serviceItem.serviceName = linkItem.serviceName;
                            serviceItem.version = linkItem.version;
                            var header = dfu.getDefaultHeader();

                            var url = linkItem.metaDataHref;
                            if (dfu.isDevMode()) {
                                url = url.replace("https://", "http://").replace("4443", "7019");
                            }
                            self.menuDataRequestingNum(self.menuDataRequestingNum()+1);
                            dfu.ajaxWithRetry(url, {
                                type: 'get',
                                dataType: 'json',
                                contentType: "application/json",
                                headers: header,
                                success: function (data) {
                                    serviceItem.serviceMenus = data.serviceMenus;
                                    serviceItem.serviceAdminMenus = data.serviceAdminMenus;
                                    url = data.serviceMenuMsgBundle;
                                    if(!url){
                                        return;
                                    }
                                    if (dfu.isDevMode()) {
                                        var dfBaseUrl = dfu.getDevData().dfRestApiEndPoint;
                                        url = dfBaseUrl.substring(0, dfBaseUrl.indexOf('/emcpdf/')) + url;
                                    }
                                    else {
                                        url = url.substring(url.indexOf('/emsaasui/') + 1, url.length - 3);
                                    }
                                    
//                                    var re = new RegExp('emsaasui.*\.');
//                                    url = re.exec(url);
                                    
//                                    url = url.subString(0,url.length);
                                    require(['ojL10n!' + url], function (_nls) {
                                        serviceItem.serviceMenuMsgBundle = _nls;
                                        serviceItem.serviceMenus = applyNlsOnMenu(serviceItem.serviceMenus, _nls);
                                        serviceItem.serviceAdminMenus = applyNlsOnMenu(serviceItem.serviceAdminMenus, _nls);
                                        self.allServiceData.push(serviceItem);
                                        self.menuDataRequestingNum(self.menuDataRequestingNum()-1);
                                    });
                                },
                                error: function (xhr, textStatus, errorThrown) {
                                    self.menuDataRequestingNum(self.menuDataRequestingNum()-1);
                                    oj.Logger.error("Failed to get subscribed applicatoins due to error: " + textStatus);
                                },
                                async: false
                            });
                        });
                    }
                    return self.allServiceData;
                }

                function applyNlsOnMenu(rawMenuObj, nlsObj){
                    var _idx;
                    if(rawMenuObj && !Array.isArray(rawMenuObj)){
                        var menuItem = $.extend(true,{},rawMenuObj);
                        if(menuItem && menuItem.labelKey){
                            var _labelKey = menuItem.labelKey;
                            menuItem.labelKey = (nlsObj&&nlsObj[_labelKey])?nlsObj[_labelKey]:_labelKey;
                        }
                        if(menuItem.children){
                            for(_idx = 0; _idx < menuItem.children.length; ++_idx){
                                menuItem.children[_idx] = applyNlsOnMenu(menuItem.children[_idx],nlsObj);
                            }
                        }
                        return menuItem;
                    } else if (rawMenuObj) {
                        var menuItemList = [];
                        for(_idx = 0; _idx < rawMenuObj.length; ++_idx){
                            var menuItem = $.extend(true,{},rawMenuObj[_idx]);
                            menuItemList.push(applyNlsOnMenu(menuItem,nlsObj));
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
                
//                function findTreeItemIndex(items, id) {
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
                
                self.serviceMenuData = [];
                var omcMenus = [];
                function getMenuItem(item) {
                    if (item) {
                        var menuItem = {'attr': {'id': item.id, 'type': item.type, 'labelKey': item.labelKey, 'externalUrl': item.externalUrl}};
                        if (item.children && item.children.length > 0) {
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
                
                self.menuDataRequestingNum.subscribe(function(num){
                    if(num!==0)return;
                    for (var k = 0; k < rootMenuData.length; ++k) {
                        self.serviceMenuData.push($.extend(true, {}, rootMenuData[k]));
                    }
                    self.allServiceData && $.each(self.allServiceData, function (idx, singleServiceData) {
                        var menuId = findAppItemIndex(rootMenuData, 'omc_root_' + singleServiceData.appId);
                        if (self.serviceMenuData[menuId]) {
                            self.serviceMenuData[menuId].children = singleServiceData.serviceMenus;
                            singleServiceData.serviceAdminMenus && self.serviceMenuData[menuId].children.push(singleServiceData.serviceAdminMenus);
                        }
                    });
                    for (var j = 0; j < self.serviceMenuData.length; j++) {
                        var item = self.serviceMenuData[j];
                        var menuItem = getMenuItem(item);
                        omcMenus.push(menuItem);
                    }
                    sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheOmcMenusDataKey, omcMenus);
                    sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheServiceMenuDataKey, self.serviceMenuData);
                    self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                    menuUtil.fireServiceMenuLoadedEvent();
                });
                
                self.dataSource = ko.observable();
                var cachedMenus = sessionCaches[0].retrieveDataFromCache(sessionCacheAllMenusKey);
                if (cachedMenus && cachedMenus[sessionCacheOmcMenusDataKey]) {
                    omcMenus = cachedMenus[sessionCacheOmcMenusDataKey];
                    self.serviceMenuData = cachedMenus[sessionCacheServiceMenuDataKey];
                    self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                    menuUtil.fireServiceMenuLoadedEvent();
                    var selectedMenuId = cachedMenus[sessionCacheSelectedMenuIdKey];
                    if (selectedMenuId) {
                        menuUtil.setCurrentMenuItem(selectedMenuId);
                    }
//                    self.expanded([rootCompositeMenuid]);
//                    self.dataSource(new oj.JsonTreeDataSource(omcMenus));
//                    $("#omcMenuNavList").ojNavigationList("refresh");
                }
                else {
                    dfu.getRegistrations(function(data){
                        self.serviceLinks = data.serviceMenus;
                        getServiceData(self.serviceLinks);
                    }, true);
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
                                    break;
                                }else{
                                    itemStack.pop();
                                }
                            }
                        }
                    }
                    _findItem(item, menuId);
                    return itemStack;
                }
                
                self.selectionHandler = function(data, event) {
                    self.selectedItem(data.id);
                    sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheSelectedMenuIdKey, data.id);
                    if (event.type === 'click' && data.id.indexOf('omc_root_') !== -1) {
                        handleMenuSelection(true, data);
                    }
                    else {
                        handleMenuSelection(false, data);
                    }
                };
                
                self.beforeCollapse = function(event, ui) {
                    if (ui.key === rootCompositeMenuid) {
//                        currentCompositeParentId && self.selectedItem(currentCompositeParentId);
                        clearCompositeMenuItems();
                        self.expanded([]);
                        self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                        $("#omcMenuNavList").ojNavigationList("refresh");
                    }
                    return true;
                    
                };
                
                if (!isSetAsHomeChecked) {
                    checkDashboardAsHomeSettings();
                }
                var globalMenuIdHrefMapping = null;
                fetchGlobalMenuLinks();
                
                function handleMenuSelection(uifwkControlled, data) {
                    var item = null;
                    for (var j = 0; j < self.serviceMenuData.length; j++) {
                        var found = findItem(self.serviceMenuData[j], data.id);
                        if (found) {
                            item = found;
                            break;
                        }
                    }
                    if (item && !item.children) {
                        if (uifwkControlled) {
                            var linkHref = globalMenuIdHrefMapping[data.id];
                            if (linkHref) {
                                window.location.href = ctxUtil.appendOMCContext(linkHref, true, true, true);
                            }
                        }
                        else {
                            fireMenuSelectionEvent(data);
                        }
                    }
                }
                
                function fetchGlobalMenuLinks() {
                    globalMenuIdHrefMapping = {};
                    var successCallback = function(data) {
                        globalMenuIdHrefMapping['omc_root_home'] = omcHomeUrl ? omcHomeUrl : '/emsaasui/emcpdfui/welcome.html';
                        globalMenuIdHrefMapping['omc_root_alerts'] = fetchLinkFromRegistrationData(data, 'homeLinks', 'EventUI');
                        globalMenuIdHrefMapping['omc_root_dashboards'] = '/emsaasui/emcpdfui/home.html';
                        globalMenuIdHrefMapping['omc_root_dataexplorer'] = fetchLinkFromRegistrationData(data, 'visualAnalyzers', 'LogAnalyticsUI');
                        globalMenuIdHrefMapping['omc_root_APM'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'ApmUI');
                        globalMenuIdHrefMapping['omc_root_Monitoring'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'EventUI');
                        globalMenuIdHrefMapping['omc_root_LogAnalytics'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'EventUI');
                        globalMenuIdHrefMapping['omc_root_ITAnalytics'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'emcitas-ui-apps');
                        globalMenuIdHrefMapping['omc_root_Orchestration'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'CosServiceUI');
                        globalMenuIdHrefMapping['omc_root_SecurityAnalytics'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'SecurityAnalyticsUI');
                        globalMenuIdHrefMapping['omc_root_Compliance'] = fetchLinkFromRegistrationData(data, 'homeLinks', 'ComplianceUIService');
                        globalMenuIdHrefMapping['omc_root_admin_alertrules'] = fetchLinkFromRegistrationData(data, 'adminLinks', 'EventUI');
                        globalMenuIdHrefMapping['omc_root_admin_agents'] = fetchLinkFromRegistrationData(data, 'adminLinks', 'TenantManagementUI');
                        globalMenuIdHrefMapping['omc_root_admin_entitiesconfig'] = fetchLinkFromRegistrationData(data, 'adminLinks', 'AdminConsoleSaaSUi');
                    };
                    dfu.getRegistrations(successCallback, true, null);
                }
                
                function fetchLinkFromRegistrationData(data, linkType, serviceName) {
                    var links = data[linkType];
                    if (links && links.length > 0) {
                        for (var i = 0; i < links.length; i++) {
                            if (links[i].serviceName === serviceName) {
                                return links[i].externalUrl;
                            }
                        }
                    }
                    return null;
                }

                function fireMenuSelectionEvent(data) {
                    var message = {'tag': 'EMAAS_OMC_GLOBAL_MENU_SELECTION'};
                    message.data = data;
                    window.postMessage(message, window.location.href);
                }
                
                function checkDashboardAsHomeSettings() {
                    function succCallback(data) {
                        var homeDashboardId = prefUtil.getPreferenceValue(data, prefKeyHomeDashboardId);
                        if (homeDashboardId) {
                            omcHomeUrl = "/emsaasui/emcpdfui/builder.html?dashboardId=" + homeDashboardId;
                        }
                        else {
                            omcHomeUrl = null;
                        }
                        isSetAsHomeChecked = true;
                    }
                    function errorCallback(jqXHR, textStatus, errorThrown) {
                        omcHomeUrl = null;
                    }
                    var options = {
                        success: succCallback,
                        error: errorCallback
                    };
                    prefUtil.getAllPreferences(options);
                }
                
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
                                    $.each(self.serviceMenuData,function(idx, listItem){
                                        $("#hamburgerMenu #navlistcontainer>div").ojNavigationList("collapse",listItem.id, true);
                                    });
                                    while(itemTrack.length>1){
                                        var parentItem = itemTrack.shift();
                                        $("#hamburgerMenu #navlistcontainer>div").ojNavigationList("expand",parentItem.id, true);
                                    }
                                    $("#hamburgerMenu #navlistcontainer>div").ojNavigationList("option", "selection", eventData.menuItemId);
                                    $("#hamburgerMenu #navlistcontainer>div").ojNavigationList("option", "currentItem", eventData.menuItemId);
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

