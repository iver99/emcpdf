/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'jquery',
        'uifwk/js/util/df-util',
        'uifwk/js/util/usertenant-util',
        'uifwk/js/util/ajax-util',
        'uifwk/js/util/message-util'
    ],
    
    function(ko, $, dfumodel, userTenantUtilModel, ajaxUtilModel, msgUtilModel)
    {
        function InternalDashboardFrameworkUtility() {
            var self = this;
            var userTenantUtil = new userTenantUtilModel();
            var ajaxUtil = new ajaxUtilModel();
            var msgUtil = new msgUtilModel();
            
            self.getUserTenant = function() {
                return userTenantUtil.getUserTenant();
            };
            
            var userTenant = self.getUserTenant();
            var userName = getUserName(userTenant);
            var tenantName = userTenant && userTenant.tenant ? userTenant.tenant : null;
            var dfu = new dfumodel(userName, tenantName);
            var isDevMode=dfu.isDevMode(); 
            var devData = dfu.getDevData();  
            if (isDevMode){
               self.getDevData = function(){
                   return devData;
               };
            }
            self.isDevMode = function(){
                return isDevMode;
            };
            
            self.getUserName = function() {
                return userName;
            };
            
            self.getTenantName = function() {
                return tenantName;
            };
            
            /**
             * Discover available Saved Search service URL
             * @returns {String} url
             */
            self.discoverSavedSearchServiceUrl = function() {
                if (self.isDevMode()){
                   return self.getDevData().ssfRestApiEndPoint; 
                }else{
                    return '/sso.static/savedsearch.navigation';
                }
            };

            self.registrationInfo = null;
            self.getRegistrationInfo=function(){
                if (self.registrationInfo===null){
                    ajaxUtil.ajaxWithRetry({type: 'GET', contentType:'application/json',url: self.getRegistrationUrl(),
                        dataType: 'json',
                        headers: dfu.getDefaultHeader(), 
                        async: false,
                        success: function(data, textStatus){
                            self.registrationInfo = data;
                        },
                        error: function(data, textStatus){
                            console.log('Failed to get registion info!');
                        }
                    });                     
                }
                return self.registrationInfo;
            };
            
            self.getRegistrationUrl=function(){
                //change value to 'data/servicemanager.json' for local debugging, otherwise you need to deploy app as ear
                if (self.isDevMode()){
                    return self.buildFullUrl(self.getDevData().dfRestApiEndPoint,"configurations/registration"); 
                }else{
                    return '/sso.static/dashboards.configurations/registration';
                }
            };

            self.getLogUrl=function(){
                //change value to 'data/servicemanager.json' for local debugging, otherwise you need to deploy app as ear
                if (self.isDevMode()){
                    return self.buildFullUrl(self.getDevData().dfRestApiEndPoint,"logging/logs"); 
                }else{
                    return '/sso.static/dashboards.logging/logs';
                }
            };   
            
            self.getDashboardsUrl=function(){
                //change value to 'data/servicemanager.json' for local debugging, otherwise you need to deploy app as ear
                if (self.isDevMode()){
                    return self.buildFullUrl(self.getDevData().dfRestApiEndPoint,"dashboards"); 
                }else{
                    return '/sso.static/dashboards.service';
                }
            };   
            
            self.getWidgetsUrl = function(){
                if (self.isDevMode()){
                    return self.buildFullUrl(self.getDevData().ssfRestApiEndPoint,"/widgets");
                }else{
                    return '/sso.static/savedsearch.widgets';
                }
            };
            self.getPreferencesUrl=function(){
                //change value to 'data/servicemanager.json' for local debugging, otherwise you need to deploy app as ear
                if (self.isDevMode()){
                    return self.buildFullUrl(self.getDevData().dfRestApiEndPoint,"preferences"); 
                }else{
                    return '/sso.static/dashboards.preferences';
                }
            }; 
            
            self.getSubscribedappsUrl=function(){
                //change value to 'data/servicemanager.json' for local debugging, otherwise you need to deploy app as ear
                if (self.isDevMode()){
                    return self.buildFullUrl(self.getDevData().dfRestApiEndPoint,"subscribedapps"); 
                }else{
                    return '/sso.static/dashboards.subscribedapps';
                }
            };             
            /**
             * Discover available quick links
             * @returns {Array} quickLinks
             */
            self.discoverQuickLinks = function() {
                var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.quickLinks){
                    return regInfo.quickLinks;
                }
                else {
                    return [];
                }
            };
            
            /**
             * Discover available visual analyzer links
             * @returns {Array} visualAnalyzerLinks
             */
            self.discoverVisualAnalyzerLinks = function() {
                var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.visualAnalyzers){
                    return regInfo.visualAnalyzers;
                }
                else {
                    return [];
                }
            };
            
            /**
             * Get URL parameter value according to URL parameter name
             * @param {String} name
             * @returns {parameter value}
             */
            self.getUrlParam = function(name){
                var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(window.location.search);
                return results === null ? "" : results[1];                
            };
            
            self.guid = function(){
                function S4() {
                   return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
                }
                
                return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
            };
            
            /**
            * Returns a random integer between min (inclusive) and max (inclusive)
            * Using Math.round() will give you a non-uniform distribution!
            */
            self.getRandomInt = function(min,max){
                return Math.floor(Math.random() * (max - min + 1)) + min;
            };
            
            /**
             * catenate root and path to build full path
             * e.g.
             * root=http://host:port/root/
             * path=home.html
             * output=http://host:port/root/home.html
             * 
             * root=http://host:port/root
             * path=home.html
             * output=http://host:port/root/home.html
             * 
             * root=http://host:port/root/
             * path=/home.html
             * output=http://host:port/root/home.html
             * 
             * @param {type} root
             * @param {type} path
             * @returns {String}
             */
            self.buildFullUrl=function(root, path){
                return dfu.buildFullUrl(root, path);
            };
                        
            self.getDashboardsRequestHeader = function() {
                return dfu.getDashboardsRequestHeader();
            };

            self.getSavedSearchRequestHeader = function() {
                var header = self.getDashboardsRequestHeader();
                if (self.isDevMode()){
                    header["OAM_REMOTE_USER"]=header["X-REMOTE-USER"];
                    if (header["X-REMOTE-USER"]){
                        delete header["X-REMOTE-USER"];
                    }
                    if (header["X-USER-IDENTITY-DOMAIN-NAME"]){
                        delete header["X-USER-IDENTITY-DOMAIN-NAME"];
                    } 
                }
                return header;
            };
            
            self.getUserTenant = function() {
                return userTenant;
            };
            
            self.getRelUrlFromFullUrl = function(url) {
                if (!url)
                    return url;
                var protocolIndex = url.indexOf('://');
                if (protocolIndex === -1)
                    return url;
                var urlNoProtocol = url.substring(protocolIndex + 3);
                var relPathIndex = urlNoProtocol.indexOf('/');
                if (relPathIndex === -1)
                    return url;
                return urlNoProtocol.substring(relPathIndex);
            };
            
            /**
             * Discover service asset root path by provider information
             * @param {String} providerName
             * @param {String} providerVersion
             * @param {String} providerAssetRoot
             * @param {String} relUrlExpected indicates if a relative url is expected or not, false means full url is returned
             * @returns {String} assetRoot
             */
            self.df_util_widget_lookup_assetRootUrl = function(providerName, providerVersion, providerAssetRoot, relUrlExpected){
                var regInfo = self.getRegistrationInfo();
                if (regInfo){
                    var assetRoot = dfu.discoverUrl(providerName, providerVersion, providerAssetRoot);
                    if (assetRoot){
                        if (relUrlExpected)
                            assetRoot = self.getRelUrlFromFullUrl(assetRoot);
                        return assetRoot;
                    }else{
                        console.log("Warning: asset root not found by providerName="+providerName+", providerVersion="+providerVersion+", providerAssetRoot="+providerAssetRoot);
                        return assetRoot;
                    }
                } else {
                    return null;
                }

            };
            
            function getUserName(userTenant) {
                if (userTenant && userTenant.tenantUser) {
                    var idx = userTenant.tenantUser.indexOf('.');
                    if (idx !== -1) {
                        return userTenant.tenantUser.substring(idx + 1, userTenant.tenantUser.length);
                    }
                }
                return null;
            }
            
            /**
             * Discover quick link
             * @param {type} serviceName
             * @param {type} version
             * @param {type} rel
             * @returns {result@arr;items@arr;links.href}
             */
            self.discoverQuickLink = function(serviceName, version, rel){
                return dfu.discoverLinkWithRelPrefix(serviceName, version, rel);
            };
            
            /**
             * Ajax call with retry logic
             * @returns 
             */ 
            self.ajaxWithRetry = function() {
		var args = arguments;
		var options = ajaxUtil.getAjaxOptions(args);
                return ajaxUtil.ajaxWithRetry(options);
            };
            
            /**
             * Display message
             */
            self.showMessage = function(messageObj) {
            	return msgUtil.showMessage(messageObj);
            };
            
            /**
             * Discover logout url for current logged in user
             */
            self.discoverLogoutUrl = function() {
                return dfu.discoverLogoutUrl();
            };
            
        }
        return new InternalDashboardFrameworkUtility();
    }
);

