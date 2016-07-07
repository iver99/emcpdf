/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['knockout', 
        'jquery', 
        'ojs/ojcore',
        'dfutil',        
        'builder/dashboard.tile.model',
        'uifwk/js/util/df-util',
        'builder/editor/editor.tiles'
    ], 
    function(ko, $, oj, dfu, dtm, dfumodel) {
        function getTileDefaultWidth(wgt, mode) {
            if (wgt && (typeof wgt.WIDGET_DEFAULT_WIDTH==='number') && (wgt.WIDGET_DEFAULT_WIDTH%1)===0 && wgt.WIDGET_DEFAULT_WIDTH >= mode.MODE_MIN_COLUMNS && wgt.WIDGET_DEFAULT_WIDTH <= mode.MODE_MAX_COLUMNS)
                    return wgt.WIDGET_DEFAULT_WIDTH;
            return Builder.BUILDER_DEFAULT_TILE_WIDTH;
        }
        Builder.registerFunction(getTileDefaultWidth, 'getTileDefaultWidth');

        function getTileDefaultHeight(wgt) {
            if (wgt && (typeof wgt.WIDGET_DEFAULT_HEIGHT==='number') && (wgt.WIDGET_DEFAULT_HEIGHT%1)===0 && wgt.WIDGET_DEFAULT_HEIGHT >= 1)
                    return wgt.WIDGET_DEFAULT_HEIGHT;
            return Builder.BUILDER_DEFAULT_TILE_HEIGHT;
        }
        Builder.registerFunction(getTileDefaultHeight, 'getTileDefaultHeight');

        function isURL(str_url) {
                var strRegex = "^((https|http|ftp|rtsp|mms)?://)";
    //                        + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"
    //                        + "(([0-9]{1,3}\.){3}[0-9]{1,3}"
    //                        + "|"
    //                        + "([0-9a-z_!~*'()-]+\.)*"
    //                        + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\."
    //                        + "[a-z]{2,6})"
    //                        + "(:[0-9]{1,4})?"
    //                        + "((/?)|"
    //                        + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
                var re = new RegExp(strRegex);
                return re.test(str_url);
            }
        Builder.registerFunction(isURL, 'isURL');

        function getVisualAnalyzerUrl(pName, pVersion) {
            var url = dfu.discoverQuickLink(pName, pVersion, "visualAnalyzer");
            if (url){
                if (dfu.isDevMode()){
                    url = dfu.getRelUrlFromFullUrl(url);  
                }
            }
            return url;
        }
        Builder.registerFunction(getVisualAnalyzerUrl, 'getVisualAnalyzerUrl');

        function encodeHtml(html) {
            var div = document.createElement('div');
            div.appendChild(document.createTextNode(html));
            return div.innerHTML;
        }
        Builder.registerFunction(encodeHtml, 'encodeHtml');

        function isContentLengthValid(content, maxLength) {
            if (!content)
                return false;
            var encoded = encodeHtml(content);
            return encoded.length > 0 && encoded.length <= maxLength;
        }
        Builder.registerFunction(isContentLengthValid, 'isContentLengthValid');

        function decodeHtml(data) {
            return data && $("<div/>").html(data).text();
        }
        Builder.registerFunction(decodeHtml, 'decodeHtml');

        function getBaseUrl() {
            return dfu.getDashboardsUrl();
        }
        Builder.registerFunction(getBaseUrl, 'getBaseUrl');

        function initializeFromCookie() {
            var userTenant= dfu.getUserTenant();
            if (userTenant){
                dtm.tenantName = userTenant.tenant;
                dtm.userTenant  =  userTenant.tenantUser;      
            }
        }
        Builder.registerFunction(initializeFromCookie, 'initializeFromCookie');

        function getDefaultHeaders() {
            var headers = {
                'Content-type': 'application/json',
                'X-USER-IDENTITY-DOMAIN-NAME': dtm.tenantName ? dtm.tenantName : ''
            };
            if (dtm.userTenant){
                headers['X-REMOTE-USER'] = dtm.userTenant;
            }else{
                console.log("Warning: user name is not found: "+dtm.userTenant);
                oj.Logger.warn("Warning: user name is not found: "+dtm.userTenant);
            }
            if (dfu.isDevMode()){
                headers.Authorization="Basic "+btoa(dfu.getDevData().wlsAuth);
            }
            return headers;
        }
        Builder.registerFunction(getDefaultHeaders, 'getDefaultHeaders');

        function loadDashboard(dashboardId, succCallBack, errorCallBack) {
            var url = dfu.buildFullUrl(getBaseUrl(), dashboardId);
            dfu.ajaxWithRetry(url, {
                type: 'get',
                dataType: "json",
                headers: getDefaultHeaders(),
                success: function(data) {
                    // If dashboad is single page app, success callback will be ignored
                    if (data.type === "SINGLEPAGE") {
                        try {
                            var tile = data.tiles[0];
                            var url = dfu.df_util_widget_lookup_assetRootUrl(tile["PROVIDER_NAME"], tile["PROVIDER_VERSION"], tile["PROVIDER_ASSET_ROOT"], false);
                            
                            if (dfu.isDevMode()) {
                                url = dfu.getRelUrlFromFullUrl(url);
                            }
                            window.location = url;
                            return ;
                        }catch(e){
                            oj.Logger.error(e);
                        }
                    }
                    

                    var mapping = {
                       "tiles": {
                           "create" : function(options) {
//                                if(options.data.type === "TEXT_WIDGET") {
//                                    return new Builder.TextTileItem(options.data);
//                                }else {
                                    return new Builder.TileItem(options.data);
//                                }
                           }
                       } 
                    };
                    if (data && data['name'] && data['name'] !== null)
                    {
                        data['name'] = $("<div/>").html(data['name']).text();
                    }
                    if (data && data['description'] && data['description'] !== null)
                    {
                        data['description'] = $("<div/>").html(data['description']).text();
                    }
                    var dsb = ko.mapping.fromJS(data, mapping);
                    dsb.isDefaultTileExist = function() {
                        for(var i in dsb.tiles()){
                            if(dsb.tiles()[i].type() === "DEFAULT") {
                                return true;
                            }
                        }
                        return false;
                    };
                    if (succCallBack)
                        succCallBack(dsb);
                },
                error: function(e) {
                    console.log(e.responseText);
                    oj.Logger.error("Error to load dashboard: "+e.responseText);
                    if (errorCallBack && e.responseText && e.responseText.indexOf("{") === 0)
                        errorCallBack(ko.mapping.fromJSON(e.responseText));
                }
            });
        }
        Builder.registerFunction(loadDashboard, 'loadDashboard');

        function isDashboardNameExisting(name) {
            if (!name)
                return false;
            var exists = false;
            var url = getBaseUrl() + "?queryString=" + name + "&limit=50&offset=0&owners=Me";
            $.ajax(url, {
                type: 'get',
                dataType: "json",
                headers: getDefaultHeaders(),
                success: function(data) {
                    if (data && data.dashboards && data.dashboards.length > 0) {
                        for (var i = 0; i < data.dashboards.length; i++) {
                            var __dname = $("<div/>").html(data.dashboards[i].name).text();
                            if (name === __dname) {
                                exists = true;
                                break;
                            }
                        }
                    }
                },
                error: function(e) {
                    console.log(e.responseText);
                },
                async: false
            });
            return exists;
        }
        Builder.registerFunction(isDashboardNameExisting, 'isDashboardNameExisting');

        function updateDashboard(dashboardId, dashboard, succCallBack, errorCallBack) {
            var url = dfu.buildFullUrl(getBaseUrl(), dashboardId);
            dfu.ajaxWithRetry(url, {
                type: 'put',
                dataType: "json",
                headers: getDefaultHeaders(),
                data: dashboard,
                success: function(data) {
                    if (data && data['name'] && data['name'] !== null)
                    {
                        data['name'] = $("<div/>").html(data['name']).text();
                    }
                    if (data && data['description'] && data['description'] !== null)
                    {
                        data['description'] = $("<div/>").html(data['description']).text();
                    }                    
                    if (succCallBack)
                        succCallBack(data);
                },
                error: function(e) {
                    oj.Logger.error("Error to update dashboard: "+e.responseText);
                    if (errorCallBack)
                        errorCallBack(ko.mapping.fromJSON(e.responseText));
                }
            });
        }
        Builder.registerFunction(updateDashboard, 'updateDashboard');

        function duplicateDashboard(dashboard, succCallBack, errorCallBack) {
            var url = dfu.buildFullUrl(getBaseUrl());
            dfu.ajaxWithRetry(url, {
                type: 'post',
                dataType: "json",
                headers: getDefaultHeaders(),
                data: dashboard,
                success: function(data) {
                    if (succCallBack)
                        succCallBack(data);
                },
                error: function(e) {
                    oj.Logger.error("Error to duplicate dashboard: "+e.responseText);
                    if (errorCallBack)
                        errorCallBack(ko.mapping.fromJSON(e.responseText));
                }
            });
        }
        Builder.registerFunction(duplicateDashboard, 'duplicateDashboard');

        function fetchDashboardScreenshot(dashboardId, succCallBack, errorCallBack) {
            var url = dfu.buildFullUrl(getBaseUrl(), dashboardId+"/screenshot");
            dfu.ajaxWithRetry(url, {
                type: 'get',
                dataType: "json",
                headers: getDefaultHeaders(),
                success: function(data) {
                    if (succCallBack)
                        succCallBack(data);
                },
                error: function(e) {
                    oj.Logger.error("Error to fetch dashboard screen shot: "+e.responseText);
                    if (errorCallBack)
                        errorCallBack(ko.mapping.fromJSON(e.responseText));
                }
            });
        }
        Builder.registerFunction(fetchDashboardScreenshot, 'fetchDashboardScreenshot');
        
        function checkDashboardFavorites(dashboardId, succCallBack, errorCallBack) {
            var url = dfu.buildFullUrl(getBaseUrl(), "favorites/" + dashboardId);
            dfu.ajaxWithRetry(url, {
                type: 'get',
                dataType: "json",
                headers: getDefaultHeaders(),
                success: function(data) {
                    if (succCallBack)
                        succCallBack(data);
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    if (errorCallBack)
                        errorCallBack(jqXHR, textStatus, errorThrown);
                }
            });
        }
        Builder.registerFunction(checkDashboardFavorites, 'checkDashboardFavorites');

        function addDashboardToFavorites(dashboardId, succCallBack, errorCallBack) {
            var url = dfu.buildFullUrl(getBaseUrl(), "favorites/" + dashboardId);
            dfu.ajaxWithRetry(url, {
                type: 'post',
                dataType: "json",
                headers: getDefaultHeaders(),
                success: function(data) {
                    if (succCallBack)
                        succCallBack(data);
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    if (errorCallBack)
                        errorCallBack(jqXHR, textStatus, errorThrown);
                }
            });
        }
        Builder.registerFunction(addDashboardToFavorites, 'addDashboardToFavorites');

        function removeDashboardFromFavorites(dashboardId, succCallBack, errorCallBack) {
            var url = dfu.buildFullUrl(getBaseUrl() , "favorites/" + dashboardId);
            dfu.ajaxWithRetry(url, {
                type: 'delete',
                dataType: "json",
                headers: getDefaultHeaders(),
                success: function(data) {
                    if (succCallBack)
                        succCallBack(data);
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    if (errorCallBack)
                        errorCallBack(jqXHR, textStatus, errorThrown);
                }
            });
        }
        Builder.registerFunction(removeDashboardFromFavorites, 'removeDashboardFromFavorites');

        function registerComponent(kocName, viewModel, template) {
            if (!ko.components.isRegistered(kocName)) {
                ko.components.register(kocName,{
                  viewModel:{require:viewModel},
                  template:{require:'text!'+template}
              }); 
            }
        }
        Builder.registerFunction(registerComponent, 'registerComponent');

        function getGuid() {
            function securedRandom(){
                var arr = new Uint32Array(1);
                var crypto = window.crypto || window.msCrypto;
                crypto.getRandomValues(arr);
                var result = arr[0] * Math.pow(2,-32);
                return result;
            }
            function S4() {
               return (((1+securedRandom())*0x10000)|0).toString(16).substring(1);
            }
            return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
        }
        Builder.registerFunction(getGuid, 'getGuid');
        
        function isSmallMediaQuery() {
            var smQuery = oj.ResponsiveUtils.getFrameworkQuery(
                                oj.ResponsiveUtils.FRAMEWORK_QUERY_KEY.SM_ONLY);
            var smObservable = oj.ResponsiveKnockoutUtils.createMediaQueryObservable(smQuery);
            window.DEV_MODE && console.debug("Checking sm media type result: " + (smObservable&smObservable()));
            return smObservable & smObservable();
        }
        Builder.registerFunction(isSmallMediaQuery, 'isSmallMediaQuery');
        
        function fetchDashboardOptions(dashboardId, succCallBack, errorCallBack){
            var url = dfu.buildFullUrl(getBaseUrl(),dashboardId+"/options" );
            dfu.ajaxWithRetry(url, {
                type: 'get',
                dataType: "json",
                headers: getDefaultHeaders(),
                success: function(data) {
                    if (succCallBack)
                        succCallBack(data);
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    if (errorCallBack)
                        errorCallBack(jqXHR, textStatus, errorThrown);
                },
                async: false
            });
        }
        
        Builder.registerFunction(fetchDashboardOptions, 'fetchDashboardOptions');
        
        function updateDashboardOptions(optionsJson, succCallBack, errorCallBack){
            var url = dfu.buildFullUrl(getBaseUrl(),optionsJson["dashboardId"]+"/options" );
            dfu.ajaxWithRetry(url, {
                type: 'put',
                dataType: "json",
                headers: getDefaultHeaders(),
                data:JSON.stringify(optionsJson),
                success: function(data) {
                    if (succCallBack)
                        succCallBack(data);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    if (errorCallBack)
                        errorCallBack(jqXHR, textStatus, errorThrown);
                }
            });
        }
        
        Builder.registerFunction(updateDashboardOptions, 'updateDashboardOptions');
        
        function saveDashboardOptions(optionsJson, succCallBack, errorCallBack){
            var url = dfu.buildFullUrl(getBaseUrl(),optionsJson["dashboardId"]+"/options" );
            dfu.ajaxWithRetry(url, {
                type: 'post',
                dataType: "json",
                headers: getDefaultHeaders(),
                data:JSON.stringify(optionsJson),
                success: function(data) {
                    if (succCallBack)
                        succCallBack(data);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    if (errorCallBack)
                        errorCallBack(jqXHR, textStatus, errorThrown);
                }
            });
        }
        function isTimeRangeAvailInUrl() {
            var dfu_model = new dfumodel(dfu.getUserName(), dfu.getTenantName());
            var start = dfu_model.getUrlParam("startTime") ? true : false;
            var end = dfu_model.getUrlParam("endTime") ? true : false;
            return start && end;
        }
        
        Builder.registerFunction(saveDashboardOptions, 'saveDashboardOptions');
        Builder.registerFunction(isTimeRangeAvailInUrl, 'isTimeRangeAvailInUrl');
        
        function removeScreenshotElementClone(clone) {
            if (!(clone instanceof $))
                throw new RangeError("Invalid clone element to remove: jquery object expected");
            var cloneId = clone.attr('id');
            var maskId = cloneId + "-mask";
            var mask = $('#' + maskId);
            document.body.removeChild(mask[0]);
            document.body.removeChild(clone[0]);
        }
        Builder.registerFunction(removeScreenshotElementClone, 'removeScreenshotElementClone');
        
        function createScreenshotElementClone(src) {
            function createMask(id, width, height) {
                var mask = $(document.createElement('div'));
                mask.css({
                    position: 'absolute',
                    left: 0,
                    top: 0,
                    width: width,
                    height: height,
                    'background-color': 'rgb(247, 247, 247)',
                    'z-index': -100
                });
                mask.attr('id', id);
                document.body.appendChild(mask[0]);
                return mask;
            }
            if (!(src instanceof $))
                throw new RangeError("Invalid source element to remove: jquery object expected");
            var cloneId = Builder.getGuid();
            var maskId = cloneId + "-mask";
            createMask(maskId, src.width(), src.height());
            var clone = src.clone(true);
            clone.css({
                position: 'absolute',
                left: 0,
                top: 0,
                'background-color': 'rgb(247, 247, 247)',
                'z-index': -200
            });
            clone.attr('id', cloneId);
            document.body.appendChild(clone[0]);
            return clone;
        }
        Builder.registerFunction(createScreenshotElementClone, 'createScreenshotElementClone');
        
        var timePeriods = [
            {value: "last15mins", string: "Last 15 minutes"},
            {value: "last30mins", string: "Last 30 minutes"},
            {value: "last60mins", string: "Last 60 minutes"},
            {value: "last4hours", string:  "Last 4 hours"},
            {value: "last6hours", string: "Last 6 hours"},
            {value: "last1day", string: "Last 1 day"},
            {value: "last7days", string: "Last 7 days"},
            {value: "last14days", string: "Last 14 days"},
            {value: "last30days", string: "Last 30 days"},
            {value: "last90days", string: "Last 90 days"},
            {value: "last1year", string: "Last 1 year"},
            {value: "latest", string: "Latest"},
            {value: "custom", string: "Custom"},
            {value: "custom1", string: "Custom"}
        ];
            
        function getTimePeriodString(value) {
            for(var i=0; i<timePeriods.length; i++) {
                if(timePeriods[i].value === value) {
                    return timePeriods[i].string;
                }
            }
        }
        Builder.registerFunction(getTimePeriodString, 'getTimePeriodString');
        
        function getTimePeriodValue(string) {
            for(var i=0; i<timePeriods.length; i++) {
                if(timePeriods[i].string === string) {
                    return timePeriods[i].value;
                }
            }
        }
        Builder.registerFunction(getTimePeriodValue, "getTimePeriodValue");

        var assetRoots = [];
        function getWidgetAssetRoot(provider_name, provider_version, provider_asset_root) {
            for(var i=0; i<assetRoots.length; i++) {
                var art = assetRoots[i];
                if((art.provider_name === provider_name) && (art.provider_version === provider_version) && (art.provider_asset_root === provider_asset_root)) {
                    return art.asset_root;
                }
            }
            return null;
        };
        Builder.registerFunction(getWidgetAssetRoot, "getWidgetAssetRoot");
            
        function getAllWidgetsAssetRoot(widgets) {
            var asset_root;                
            assetRoots = [];
            for(var i=0; i<widgets.length; i++) {
                var wgt = widgets[i];
                var provider_name = wgt.PROVIDER_NAME();
                var provider_version = wgt.PROVIDER_VERSION();
                var provider_asset_root = wgt.PROVIDER_ASSET_ROOT();
                if (getWidgetAssetRoot(provider_name, provider_version, provider_asset_root) === null) {
                    asset_root = dfu.df_util_widget_lookup_assetRootUrl(provider_name, provider_version, provider_asset_root, true);
                    assetRoots.push({provider_name: provider_name, provider_version: provider_version, provider_asset_root: provider_asset_root, asset_root: asset_root});
                }
            }                                
        }
        Builder.registerFunction(getAllWidgetsAssetRoot, "getAllWidgetsAssetRoot");

    }
);
