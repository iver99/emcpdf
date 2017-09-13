/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout', 'jquery', 'ojs/ojcore', 'uifwk/@version@/js/util/ajax-util-impl', 'uifwk/@version@/js/util/df-util-impl'],
    function (ko, $, oj, ajaxUtilModel, dfumodel)
    {
        function DashboardFrameworkUserTenantUtility() {
            var self = this;
            var dfu = new dfumodel();
            self.devMode=dfu.isDevMode();
            var ajaxUtil = new ajaxUtilModel();

            /**
             * Get logged in user and tenant name from web service
             *
             * @returns {Object} user tenant object
             *                    e.g. {"tenant": "emaastesttenant1",
             *                          "user": "emcsadmin",
             *                          "tenantUser": "emaastesttenant1.emcsadmin"}
             */
            self.getUserTenant = function() {
                if (self.devMode){
                    return dfu.getDevData().userTenant;
                }
                var tenantName = null; //in case tenant name is not got
                var userName = null;   //in case use name is not got
                var tenantUser = null; //in case tenantName.userName is not got
                if(window._uifwk && window._uifwk.cachedData && window._uifwk.cachedData.loggedInUser){
                    var tenantIdDotUsername = window._uifwk.cachedData.loggedInUser.currentUser;
                    var indexOfDot = tenantIdDotUsername.indexOf(".");
                    tenantName = tenantIdDotUsername.substring(0, indexOfDot);
                    userName = tenantIdDotUsername.substring(indexOfDot + 1, tenantIdDotUsername.length);
                    tenantUser = tenantIdDotUsername;
                }else{
                    function doneCallback(data) {
                        if(!window._uifwk){
                            window._uifwk = {};
                        }
                        if(!window._uifwk.cachedData){
                            window._uifwk.cachedData = {};
                        }
                        if(data && data["userRoles"]){
                            window._uifwk.cachedData.roles = data["userRoles"];
                        }
                        if(data && data["currentUser"]){
                            window._uifwk.cachedData.loggedInUser = {"currentUser":data["currentUser"]};
                            var tenantIdDotUsername = data.currentUser;
                            var indexOfDot = tenantIdDotUsername.indexOf(".");
                            tenantName = tenantIdDotUsername.substring(0, indexOfDot);
                            userName = tenantIdDotUsername.substring(indexOfDot + 1, tenantIdDotUsername.length);
                            tenantUser = tenantIdDotUsername;
                        }
                    }

                    if (window._uifwk && window._uifwk.cachedData && window._uifwk.cachedData.userInfo) {
                        doneCallback(window._uifwk.cachedData.userInfo);
                    }
                    else {
                        ajaxUtil.ajaxWithRetry({
                            type: "GET",
                            url: "/sso.static/dashboards.configurations/userInfo",
                            async: false,
                            dataType: "json",
                            contentType: "application/json; charset=utf-8"
                        })
                        .done(
                            function (data) {
                                doneCallback(data);
                            })
                        .fail(function(jqXHR, textStatus, errorThrown){
                            //To avoid circular dependency use require call
                            require(['uifwk/@version@/js/sdk/context-util-impl'], function (cxtModel) {
                                var cxtUtil = new cxtModel();
                                if (jqXHR.status === 401 && location.href && location.href.indexOf("error.html") === -1) {
                                    oj.Logger.error("Failed to detect OMC planned downtime due to 401 error. Redirect to error page", true);
                                    location.href = cxtUtil.appendOMCContext("/emsaasui/emcpdfui/error.html?msg=DBS_ERROR_PAGE_NOT_FOUND_MSG&invalidUrl=" + encodeURIComponent(location.href));
                                }
                                else if (location.href && location.href.indexOf("error.html") === -1) {
                                    oj.Logger.error("Failed to retrieve tenant or user. Redirect to error page", true);
                                    location.href = cxtUtil.appendOMCContext("/emsaasui/emcpdfui/error.html?msg=DBS_ERROR_ORA_EMSAAS_USERNAME_AND_TENANTNAME_INVALID&invalidUrl=" + encodeURIComponent(location.href));
                                }
                                return;
                            });
                        });
                    }
                }

                  if ((!tenantName || !userName) && location.href && location.href.indexOf("error.html") === -1) {
                        return null;
                  }
                  else{
                      return {"tenant": tenantName, "user": userName, "tenantUser": tenantUser};
                  }
            };

            /**
             * Get logged in user and tenant name
             *
             * Note: this API is deprecated, keep it compatible as it may have been used already
             *
             * @returns {Object} user tenant object
             *                    e.g. {"tenant": "emaastesttenant1",
             *                          "user": "emcsadmin",
             *                          "tenantUser": "emaastesttenant1.emcsadmin"}
             */
            self.getUserTenantFromCookie = function() {
                return self.getUserTenant();
            };

            var userTenant = null;
            if (self.devMode){
                userTenant=dfu.getDevData().userTenant;
            }else{
                userTenant = self.getUserTenant();
            }
            /**
             * Get logged in user name
             *
             * @returns {String} user name
             */
            self.getUserName = function() {
                return userTenant && userTenant.user ? userTenant.user : null;
            };

            /**
             * Get logged in tenant name
             *
             * @returns {String} tenant name
             */
            self.getTenantName = function() {
                return userTenant && userTenant.tenant ? userTenant.tenant : null;
            };
            
            self.getUserRoles = function(callback,sendAsync) {
                var serviceUrl = "/sso.static/dashboards.configurations/userInfo";
                if (dfu.isDevMode()){
                    serviceUrl = dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint, 'configurations/userInfo');
                }
                if(window._uifwk && window._uifwk.cachedData && window._uifwk.cachedData.roles){
                    self.userRoles = window._uifwk.cachedData.roles; 
                    callback(window._uifwk.cachedData.roles);
                }else{
                    function doneCallback(data) {
                        self.userRoles = data && data["userRoles"] ? data["userRoles"] : null;
                        if(!window._uifwk){
                            window._uifwk = {};
                        }
                        if(!window._uifwk.cachedData){
                            window._uifwk.cachedData = {};
                        }
                        if(data && data["currentUser"]){
                            window._uifwk.cachedData.loggedInUser = {"currentUser":data["currentUser"]};
                        }
                        if(data && data["userGrants"]){
                            window._uifwk.cachedData.userGrants = ko.observable(data["userGrants"]);
                        }
                        if(data && data["userRoles"]){
                            window._uifwk.cachedData.roles = data["userRoles"];
                            callback(data["userRoles"]);
                        }
                        else {
                            callback(null);
                        }
                    }
                    if (window._uifwk && window._uifwk.cachedData && window._uifwk.cachedData.userInfo) {
                        doneCallback(window._uifwk.cachedData.userInfo);
                    }
                    else {
                        ajaxUtil.ajaxWithRetry({
                            url: serviceUrl,
                            async: sendAsync === false? false:true,
                            headers: dfu.getDefaultHeader(),
                            contentType:'application/json'
                        })
                        .done(
                            function (data) {
                                doneCallback(data);
                            })
                        .fail(function() {
                                callback(null);
                            });
                    }
                }
            };
            
            self.ADMIN_ROLE_NAME_APM = "APM Administrator";
            self.USER_ROLE_NAME_APM = "APM User";
            self.ADMIN_ROLE_NAME_ITA = "IT Analytics Administrator";
            self.USER_ROLE_NAME_ITA = "IT Analytics User";
            self.ADMIN_ROLE_NAME_LA = "Log Analytics Administrator";
            self.USER_ROLE_NAME_LA = "Log Analytics User";
            self.ADMIN_ROLE_NAME_MONITORING = "Monitoring Service Administrator";
            self.ADMIN_ROLE_NAME_SECURITY = "Security Analytics Administrator";
            self.ADMIN_ROLE_NAME_COMPLIANCE = "Compliance Administrator";
            self.ADMIN_ROLE_NAME_ORCHESTRATION = "Orchestration Administrator";
            self.ADMIN_ROLE_NAME_OMC = "OMC Administrator";
            
            self.userHasRole = function(role){
                self.getUserRoles(function(data){
                    self.userRoles = data; 
                },false);
                if(!self.userRoles || self.userRoles.indexOf(role)<0){
                    return false;
                }else{
                    return true;
                }
            };
            
            self.isAdminUser = function() {
                if (self.userHasRole(self.ADMIN_ROLE_NAME_OMC) ||
                    self.userHasRole(self.ADMIN_ROLE_NAME_APM) ||
                    self.userHasRole(self.ADMIN_ROLE_NAME_ITA) ||
                    self.userHasRole(self.ADMIN_ROLE_NAME_LA) ||
                    self.userHasRole(self.ADMIN_ROLE_NAME_MONITORING) ||
                    self.userHasRole(self.ADMIN_ROLE_NAME_SECURITY) ||
                    self.userHasRole(self.ADMIN_ROLE_NAME_COMPLIANCE) ||
                    self.userHasRole(self.ADMIN_ROLE_NAME_ORCHESTRATION)) {
                    return true;
                }
                return false;
            };

            /**
             * Get user granted privileges
             *
             * @param {Function} callback Callback function to be invoked when result is fetched. 
             * The input for the callback function will be a String of privilege names separated by comma e.g. 
             * "ADMINISTER_LOG_TYPE,RUN_AWR_VIEWER_APP,USE_TARGET_ANALYTICS,ADMIN_ITA_WAREHOUSE"
             * 
             * @returns
             */
            self.getUserGrants = function(callback) {
                var serviceUrl = '/sso.static/dashboards.configurations/userInfo';
                if (self.devMode) {
                    serviceUrl = dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint, 'configurations/userInfo');
                }
                
                if (window._uifwk && window._uifwk.cachedData && window._uifwk.cachedData.userGrants &&
                        ($.isFunction(window._uifwk.cachedData.userGrants) ? window._uifwk.cachedData.userGrants() : true)) {
                    callback($.isFunction(window._uifwk.cachedData.userGrants) ? window._uifwk.cachedData.userGrants() :
                            window._uifwk.cachedData.userGrants);
                } else {
                    if (!window._uifwk) {
                        window._uifwk = {};
                    }
                    if (!window._uifwk.cachedData) {
                        window._uifwk.cachedData = {};
                    }
                    if (!window._uifwk.cachedData.isFetchingUserGrants) {
                        window._uifwk.cachedData.isFetchingUserGrants = true;
                        if (!window._uifwk.cachedData.userGrants) {
                            window._uifwk.cachedData.userGrants = ko.observable();
                        }
                        if (!window._uifwk.cachedData.errGetUserGrants) {
                            window._uifwk.cachedData.errGetUserGrants = ko.observable(false);
                        }
                        else {
                            window._uifwk.cachedData.errGetUserGrants(false);
                        }

                        function doneCallback(data) {
                            if(data && data["currentUser"]){
                                window._uifwk.cachedData.loggedInUser = {"currentUser":data["currentUser"]};
                            }
                            if(data && data["userRoles"]){
                                window._uifwk.cachedData.roles = data["userRoles"];
                            }
                            if(data && data["userGrants"]){
                                window._uifwk.cachedData.userGrants(data["userGrants"]);
                                callback(data["userGrants"]);
                            }
                            else {
                                callback(null);
                            }
                            window._uifwk.cachedData.isFetchingUserGrants = false;
                        }
                        if (window._uifwk && window._uifwk.cachedData && window._uifwk.cachedData.userInfo) {
                            doneCallback(window._uifwk.cachedData.userInfo);
                        }
                        else {
                            ajaxUtil.ajaxWithRetry({
                                url: serviceUrl,
                                async: true,
                                headers: dfu.getDefaultHeader()
                            })
                            .done(function(data) {
                                doneCallback(data);
                                
                            })
                            .fail(function() {
                                oj.Logger.error('Failed to get user granted privileges!');
                                window._uifwk.cachedData.isFetchingUserGrants = false;
                                window._uifwk.cachedData.errGetUserGrants(true);
                                callback(null);
                            });
                        }
                    } 
                    else {
                        window._uifwk.cachedData.userGrants.subscribe(function(data) {
                            callback(data);
                        });
                        window._uifwk.cachedData.errGetUserGrants.subscribe(function(hitErr) {
                            if (hitErr && callback) {
                                callback(null);
                            }
                        });
                    }
                }
            };
        }

        return DashboardFrameworkUserTenantUtility;
    }
);

