define(['knockout',
    'jquery',
    'ojs/ojcore',
    'uifwk/@version@/js/util/ajax-util-impl',
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg'
],
    function (ko, $, oj, ajaxUtilModel, nls)
    {
        function DashboardFrameworkUtility(userName, tenantName) {
            var self = this;
            self.SERVICE_VERSION = encodeURIComponent('1.0+');
            var ajaxUtil = new ajaxUtilModel();

            self.userName = userName;
            self.tenantName = tenantName;

            if (!userName || !tenantName) {
                if (window.DEV_MODE) {
                    self.userName = window.DEV_MODE.user;
                    self.tenantName = window.DEV_MODE.tenant;
                }
            }

            /**
             * Get URL parameter value according to URL parameter name
             * @param {String} name
             * @returns {parameter value}
             */
            self.getUrlParam = function (name) {
                /* globals location */
                var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
                return results === null ? "" : results[1];
            };


            /**
             * Catenate root and path to build full path
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
             * @param {String} root
             * @param {String} path
             * @returns {String}
             */
            self.buildFullUrl = function (root, path) {
                if (root === null || root === undefined) {
                    oj.Logger.warn("Warning: root is null, path=" + path);
                    return path;
                }

                if (path === null || path === undefined) {
                    oj.Logger.warn("Warning: path is null, root=" + root);
                    return root;
                }

                if (typeof root === "string" && typeof path === "string") {
                    var expRoot = root;
                    var expPath = path;
                    if (root.charAt(root.length - 1) === '/') {
                        expRoot = root.substring(0, root.length - 1);
                    }
                    if (path.charAt(0) === '/') {
                        expPath = path.substring(1);
                    }
                    return expRoot + "/" + expPath;
                } else {
                    return root + "/" + path;
                }
            };

            self.LOOKUP_REST_URL_BASE = "/sso.static/dashboards.registry/lookup/";
            self.SUBSCIBED_APPS_REST_URL = "/sso.static/dashboards.subscribedapps";
            self.SSF_REST_API_BASE = "/sso.static/savedsearch.navigation";
            var devMode = (window.DEV_MODE !== null && typeof window.DEV_MODE === "object");
            var devData = window.DEV_MODE;
            if (window.DEV_MODE !== null && typeof window.DEV_MODE === "object") {
                devMode = true;
                devData.userTenant = {"tenant": devData.tenant, "user": devData.user, "tenantUser": devData.tenant + "." + devData.user};
                self.LOOKUP_REST_URL_BASE = self.buildFullUrl(devData.dfRestApiEndPoint, "registry/lookup/");
                self.SUBSCIBED_APPS_REST_URL = self.buildFullUrl(devData.dfRestApiEndPoint, "subscribedapps");
                self.SSF_REST_API_BASE = devData.ssfRestApiEndPoint;
                console.log("====>DEV MODE:" + devMode);
            }


            self.isDevMode = function () {
                return devMode;
            };

            self.getDevData = function () {
                return devData;
            };

            var getHeadersForRegistry = function ()
            {
                var defHeader = {};
                if (self.isDevMode()) {
                    defHeader["X-USER-IDENTITY-DOMAIN-NAME"] = self.tenantName;
                    defHeader["X-SSO-CLIENT"] = "true";
                    defHeader.Authorization = "Basic " + btoa(self.getDevData().wlsAuth);
                }
                oj.Logger.info("Sent Header: " + JSON.stringify(defHeader));
                return defHeader;
            };

            self.getTargetModelServiceInDEVMode = function ()
            {
                if (window.DEV_MODE.odsRestApiEndPoint) {
                    return window.DEV_MODE.odsRestApiEndPoint;
                }
                var registryUrlInDevMode = self.getDevData().registryUrl;

                self.ajaxWithRetry(registryUrlInDevMode, {
                    headers: getHeadersForRegistry(),
                    type: 'GET',
                    data: {"serviceName": "ODSQuery"},
                    async: false,
                    dataType: "json",
                    contentType: "application/json; charset=utf-8",
                    description: "Obtaining target model service in DEV mode",
                    success: function (data, textStatus, jqXHR) {
                        if (data.total > 0) {
                            // Note: We need to get the canonical end points, otherwise we run into the options
                            // issues.  Hence, we cannot use static links, since they are built from virtual end points.
                            var endpoints = data.items[0].canonicalEndpoints;
                            if (endpoints.length === 0) {
                                endpoints = data.items[0].virtualEndpoints;
                                window.console.warn("DEV mode could not obtain canonical endpoints (http) for target service.\n" +
                                    "Falling back to virtual endpoints (https).\n" +
                                    "You must find the OPTIONS calls, copy the url to a different tab, accept the SSL warning,\n" +
                                    "go back to the original tab, and hit F5");
                            }
                            var targetModelUrl = endpoints[0];
                            if (targetModelUrl.charAt(targetModelUrl.length - 1) !== "/") {
                                targetModelUrl = targetModelUrl + "/";
                            }
                            window.DEV_MODE.odsRestApiEndPoint = targetModelUrl;
                            oj.Logger.log("DEV mode - Obtained " + targetModelUrl);
                        } else {
                            oj.Logger.log("DEV mode - For registry " + registryUrlInDevMode + " Obtaining Target Model service URL in DEV mode " + textStatus + " No data was returned.");
                        }
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        oj.Logger.log("DEV mode - For registry " + registryUrlInDevMode + " Obtaining Target Model service URL in DEV mode" + textStatus + errorThrown);
                    }
                });
                return window.DEV_MODE.odsRestApiEndPoint;
            };

            /**
             *
             * Discover URL from Service Manager Registry
             * @param {String} serviceName
             * @param {String} version
             * @param {String} rel
             * @param {Boolean} useApiGWLookup
             * @returns {String} result
             */
            self.discoverUrl = function (serviceName, version, rel, useApiGWLookup) {
                if (serviceName === null || serviceName === undefined) {
                    oj.Logger.error("Error: Failed to discover URL, serviceName=" + serviceName);
                    return null;
                }
                if (version === null || version === undefined) {
                    oj.Logger.error("Error: Failed to discover URL, version=" + version);
                    return null;
                }
                //By default useApiGWLookup is false
                useApiGWLookup = (useApiGWLookup === true) ? true : false;

                var result = null;
                var url = self.LOOKUP_REST_URL_BASE + "endpoint?serviceName=" + serviceName + "&version=" + version + "&useApiGWLookup=" + useApiGWLookup;
                if (typeof rel === "string") {
                    url = self.LOOKUP_REST_URL_BASE + "link?serviceName=" + serviceName + "&version=" + version + "&rel=" + rel + "&useApiGWLookup=" + useApiGWLookup;
                }

                self.ajaxWithRetry(url, {
                    type: 'get',
                    dataType: 'json',
                    headers: self.getDefaultHeader(),
                    success: function (data, textStatus, jqXHR) {
                        result = data;
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        oj.Logger.error("Error: URL not found due to error: " + textStatus);
                    },
                    async: false
                });

                if (result) {
                    if (typeof rel === "string") {
                        oj.Logger.info("Link found by serviceName=" + serviceName + ", version=" + version + ", rel=" + rel);
                        return result.href;
                    } else {
                        oj.Logger.info("EndPoint found by serviceName=" + serviceName + ", version=" + version + ", rel=" + rel);
                        return result.href;
                    }
                }

                oj.Logger.error("Error: URL not found by serviceName=" + serviceName + ", version=" + version + ", rel=" + rel);
                return null;
            };

            /**
             *
             * Discover URL from Service Manager Registry asynchronously
             * @param {String} serviceName
             * @param {String} version
             * @param {String} rel
             * @param {Function} callbackFunc
             * @param {Boolean} useApiGWLookup
             * @returns
             */
            self.discoverUrlAsync = function (serviceName, version, rel, callbackFunc, useApiGWLookup) {
                if (!$.isFunction(callbackFunc)) {
                    oj.Logger.error("Invalid callback function: " + callbackFunc);
                    return;
                }
                if (serviceName === null || serviceName === undefined) {
                    oj.Logger.error("Error: Failed to discover URL, serviceName=" + serviceName);
                    return;
                }
                if (version === null || version === undefined) {
                    oj.Logger.error("Error: Failed to discover URL, version=" + version);
                    return;
                }
                
                //By default useApiGWLookup is false
                useApiGWLookup = (useApiGWLookup === true) ? true : false;

                var url = self.LOOKUP_REST_URL_BASE + "endpoint?serviceName=" + serviceName + "&version=" + version + "&useApiGWLookup=" + useApiGWLookup;
                if (typeof rel === "string") {
                    url = self.LOOKUP_REST_URL_BASE + "link?serviceName=" + serviceName + "&version=" + version + "&rel=" + rel + "&useApiGWLookup=" + useApiGWLookup;
                }

                self.ajaxWithRetry(url, {
                    type: 'get',
                    dataType: 'json',
                    headers: self.getDefaultHeader(),
                    success: function (data, textStatus, jqXHR) {
                        if (data) {
                            if (typeof rel === "string") {
                                oj.Logger.info("Link found by serviceName=" + serviceName + ", version=" + version + ", rel=" + rel);
                            }
                            else {
                                oj.Logger.info("EndPoint found by serviceName=" + serviceName + ", version=" + version + ", rel=" + rel);
                            }
                            callbackFunc(data.href);
                        }
                        else {
                            callbackFunc(null);
                        }
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        oj.Logger.error("Error: URL not found due to error: " + textStatus);
                        callbackFunc(null);
                    },
                    async: true
                });
            };

            /**
             *
             * Discover link URL from Service Manager Registry with prefixed rel
             * @param {String} serviceName
             * @param {String} version
             * @param {String} rel
             * @param {Boolean} useApiGWLookup
             * @returns {String} result
             */
            self.discoverLinkWithRelPrefix = function (serviceName, version, rel, useApiGWLookup) {
                if (typeof serviceName !== "string") {
                    oj.Logger.error("Error: Failed to discover Link (with Rel Prefix), serviceName=" + serviceName);
                    return null;
                }
                if (typeof version !== "string") {
                    oj.Logger.error("Error: Failed to discover Link (with Rel Prefix), version=" + version);
                    return null;
                }

                if (typeof rel !== "string") {
                    oj.Logger.error("Error: Failed to discover Link (with Rel Prefix), rel=" + rel);
                    return null;
                }
                //By default useApiGWLookup is false
                useApiGWLookup = (useApiGWLookup === true) ? true : false;
                
                var result = null;
                var url = self.LOOKUP_REST_URL_BASE + "linkWithRelPrefix?serviceName=" + serviceName + "&version=" + version + "&rel=" + rel + "&useApiGWLookup=" + useApiGWLookup;

                self.ajaxWithRetry(url, {
                    type: 'get',
                    dataType: 'json',
                    headers: self.getDefaultHeader(),
                    success: function (data, textStatus, jqXHR) {
                        result = data;
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        oj.Logger.error("Error: Link (with Rel Prefix) not found due to error: " + textStatus);
                    },
                    async: false
                });

                if (result) {
                    oj.Logger.info("Link (with Rel Prefix) found by serviceName=" + serviceName + ", version=" + version + ", rel=" + rel);
                    return result.href;
                }
                oj.Logger.error("Error: Link (with Rel Prefix) not found by serviceName=" + serviceName + ", version=" + version + ", rel=" + rel);
                return null;
            };

            /**
             *
             * Discover link URL from Service Manager Registry with prefixed rel asynchronously
             * @param {String} serviceName
             * @param {String} version
             * @param {String} rel
             * @param {Function} callbackFunc
             * @param {Boolean} useApiGWLookup
             * @returns
             */
            self.discoverLinkWithRelPrefixAsync = function (serviceName, version, rel, callbackFunc, useApiGWLookup) {
                if (!$.isFunction(callbackFunc)) {
                    oj.Logger.error("Invalid callback function: " + callbackFunc);
                    return;
                }
                if (typeof serviceName !== "string") {
                    oj.Logger.error("Error: Failed to discover Link (with Rel Prefix), serviceName=" + serviceName);
                    return;
                }
                if (typeof version !== "string") {
                    oj.Logger.error("Error: Failed to discover Link (with Rel Prefix), version=" + version);
                    return;
                }

                if (typeof rel !== "string") {
                    oj.Logger.error("Error: Failed to discover Link (with Rel Prefix), rel=" + rel);
                    return;
                }
                //By default useApiGWLookup is false
                useApiGWLookup = (useApiGWLookup === true) ? true : false;

                var url = self.LOOKUP_REST_URL_BASE + "linkWithRelPrefix?serviceName=" + serviceName + "&version=" + version + "&rel=" + rel + "&useApiGWLookup=" + useApiGWLookup;
                self.ajaxWithRetry(url, {
                    type: 'get',
                    dataType: 'json',
                    headers: self.getDefaultHeader(),
                    success: function (data, textStatus, jqXHR) {
                        if (data) {
                            oj.Logger.info("Link (with Rel Prefix) found by serviceName=" + serviceName + ", version=" + version + ", rel=" + rel);
                            callbackFunc(data.href);
                        }
                        else {
                            callbackFunc(null);
                        }
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        oj.Logger.error("Error: Link (with Rel Prefix) not found due to error: " + textStatus);
                        callbackFunc(null);
                    },
                    async: true
                });
            };

            /**
             * Discover SSO logout URL
             * @returns {String}
             */
            self.discoverLogoutUrl = function () {
                return self.discoverUrl('SecurityService', self.SERVICE_VERSION, 'sso.logout');
            };

            /**
             * Discover SSO logout URL asynchronously
             * @param {Function} callbackFunc
             * @returns
             */
            self.discoverLogoutUrlAsync = function (callbackFunc) {
                return self.discoverUrlAsync('SecurityService', self.SERVICE_VERSION, 'sso.logout', callbackFunc);
            };

            /**
             * Discover dashboard home URL
             * @returns {String} url
             */
            self.discoverDFHomeUrl = function () {
                return "/emsaasui/emcpdfui/home.html";
            };

            /**
             * Discover welcome URL
             * @returns {String} url
             */
            self.discoverWelcomeUrl = function () {
                var welcomeUrl = "/emsaasui/emcpdfui/welcome.html";
                return welcomeUrl;
            };

            /**
             * Get default request header for ajax call
             * @returns {Object}
             */
            self.getDefaultHeader = function () {
                var defHeader = {};
                if (self.isDevMode()) {
                    defHeader["X-USER-IDENTITY-DOMAIN-NAME"] = self.tenantName;
                    defHeader["X-REMOTE-USER"] = self.tenantName + '.' + self.userName;
                    defHeader.Authorization = "Basic " + btoa(self.getDevData().wlsAuth);
                }
                oj.Logger.info("Sent Header: " + JSON.stringify(defHeader));
                return defHeader;
            };

            self.getHeadersForService = function ()
            {
                var defHeader = {};
                if (self.isDevMode()) {
                    defHeader["X-USER-IDENTITY-DOMAIN-NAME"] = self.tenantName;
                    defHeader.Authorization = "Basic " + btoa(self.getDevData().wlsAuth);
                }
                oj.Logger.info("Sent Header: " + JSON.stringify(defHeader));
                return defHeader;
            };

            /**
             * Get request header for Dashboard API call
             * @returns {Object}
             */
            self.getDashboardsRequestHeader = function () {
                return self.getDefaultHeader();
            };

            /**
             * Returns an array of application names subscribed by specified tenant
             * Note:
             * See constructor of this utility to know more about how to set tenant and user.
             *
             * @returns {string array or null if no application is subscribed}, e.g. ["APM","ITAnalytics,"LogAnalytics"], ["APM"], null, etc.
             */
            self.getSubscribedApplications = function () {
                if (!self.tenantName) {
                    oj.Logger.error("Specified tenant name is empty, and the query won't be executed.");
                    return null;
                }
                if (!self.userName) {
                    oj.Logger.error("Specified user name is empty, and the query won't be executed.");
                    return null;
                }

                var header = self.getDefaultHeader();
                var url = self.SUBSCIBED_APPS_REST_URL;
                var result = null;
                self.ajaxWithRetry(url, {
                    type: 'get',
                    dataType: 'json',
                    headers: header,
                    success: function (data) {
                        result = data.applications;
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        oj.Logger.error("Failed to get subscribed applicatoins due to error: " + textStatus);
                    },
                    async: false
                });
                return result;
            };

            /**
             * Returns an array of application with edition information subscribed by specified tenant
             * Note:
             * See constructor of this utility to know more about how to set tenant and user.
             *
             * @returns {string array or null if no application is subscribed},
             * e.g.
             * [
             *          {"application":"LogAnalytics","edition":"Log Analytics Enterprise Edition"},
             *          {"application":"ITAnalytics","edition":"IT Analytics Enterprise Edition"},
             *          {"application":"APM","edition":"Application Performance Monitoring Enterprise Edition"}
             * ]
             */
            self.getSubscribedApplicationsWithEdition = function () {
                if (!self.tenantName) {
                    oj.Logger.error("Specified tenant name is empty, and the query won't be executed.");
                    return null;
                }
                if (!self.userName) {
                    oj.Logger.error("Specified user name is empty, and the query won't be executed.");
                    return null;
                }

                var header = self.getDefaultHeader();
                var url = self.SUBSCIBED_APPS_REST_URL + "?withEdition=true";

                var result = null;
                $.ajax(url, {
                    type: 'get',
                    dataType: 'json',
                    headers: header,
                    success: function (data) {
                        result = data.applications;
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        oj.Logger.error("Failed to get subscribed applicatoins with edition due to error: " + textStatus);
                    },
                    async: false
                });
                return result;
            };


            /**
             * Retrieves an array of application with edition information subscribed by specified tenant, and call the specified callback function
             * to handle the applications
             *
             * Note:
             * See constructor of this utility to know more about how to set tenant and user.
             *
             * e.g.
             * [
             *          {"application":"LogAnalytics","edition":"Log Analytics Enterprise Edition"},
             *          {"application":"ITAnalytics","edition":"IT Analytics Enterprise Edition"},
             *          {"application":"APM","edition":"Application Performance Monitoring Enterprise Edition"}
             * ]
             */
            self.checkSubscribedApplicationsWithEdition = function (callbackFunc) {
                if (!self.tenantName) {
                    oj.Logger.error("Specified tenant name is empty, and the query won't be executed.");
                    return null;
                }
                if (!self.userName) {
                    oj.Logger.error("Specified user name is empty, and the query won't be executed.");
                    return null;
                }
                if (!callbackFunc || !(callbackFunc instanceof Function)) {
                    oj.Logger.error("Specified callback func isn't an Function, and the query won't be executed.");
                    return null;
                }

                var header = self.getDefaultHeader();
                var url = self.SUBSCIBED_APPS_REST_URL + "?withEdition=true";

                var result = null;
                $.ajax(url, {
                    type: 'get',
                    dataType: 'json',
                    headers: header,
                    success: function (data) {
                        result = data.applications;
                        callbackFunc(result);
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        oj.Logger.error("Failed to get subscribed applicatoins with edition due to error: " + textStatus);
                    },
                    async: true
                });
            };

            /**
             * Check subscribed applications and call callback function with subscribed application names in an string array or null for none
             * Note:
             * See constructor of this utility to know more about how to set tenant and user.
             *
             */
            self.checkSubscribedApplications = function (callbackFunc) {
                if (!$.isFunction(callbackFunc)) {
                    oj.Logger.error("Invalid callback function: " + callbackFunc);
                    return;
                }

                if (!self.tenantName) {
                    oj.Logger.error("Specified tenant name is empty, and the query won't be executed.");
                    return;
                }
                if (!self.userName) {
                    oj.Logger.error("Specified user name is empty, and the query won't be executed.");
                    return;
                }

                var header = self.getDefaultHeader();
                var url = self.SUBSCIBED_APPS_REST_URL;

                self.ajaxWithRetry(url, {
                    type: 'get',
                    dataType: 'json',
                    headers: header,
                    success: function (data) {
                        callbackFunc(data.applications);
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        oj.Logger.error("Failed to get subscribed applications due to error: " + textStatus);
                        callbackFunc(null);
                    },
                    async: true
                });
            };

            /**
             * Get request header for Saved Search Framework API call
             * @returns {Object}
             */
            self.getSavedSearchServiceRequestHeader = function () {
                var defHeader = {};
                if (self.isDevMode()) {
                    defHeader["OAM_REMOTE_USER"] = self.tenantName + '.' + self.userName;
                    defHeader.Authorization = "Basic " + btoa(self.getDevData().wlsAuth);
                }
                oj.Logger.info("Sent Header: " + JSON.stringify(defHeader));
                return defHeader;
            };

            /**
             * Discover available Saved Search service URL
             * @returns {String} url
             */
            self.discoverSavedSearchServiceUrl = function () {
                return self.SSF_REST_API_BASE;
            };

            /**
             * Ajax call with retry logic
             *
             * Note:
             * This API is deprecated. Please use the corresponding API in ajax-util instead.
             * Keep it here for now and will be removed in the future.
             *
             * Supported patterns:
             * 1. ajaxWithRetry(url)
             *    url: a target URL string (e.g. '/sso.static/dashboards.subscribedapps')
             * 2. ajaxWithRetry(options)
             *    options: a Object which holds all the settings (including url) required by an ajax call.
             *             Support standard jquery settings and additional options "retryLimit", "showMessages"
             * 3. ajaxWithRetry(url, options)
             *    url: a target URL string
             *    options: a Object which contains the settings required by an ajax call
             *             Support standard jquery settings and additional options "retryLimit", "showMessages"
             * 4. ajaxWithRetry(url, successCallback)
             *     url: a target URL string
             *     successCallback: success call back function
             * 5. ajaxWithRetry(url, successCallback, options)
             *     url: a target URL string
             *     successCallback: success call back function
             *     options: a Object which contains the settings required by an ajax call
             *             Support standard jquery settings and additional options "retryLimit", "showMessages"
             *
             * @returns {Deferred Object}
             */
            self.ajaxWithRetry = function () {
                var args = arguments;
                var ajaxOptions = ajaxUtil.getAjaxOptions(args);
                return ajaxUtil.ajaxWithRetry(ajaxOptions);
            };

            /**
             * Make an ajax get call with retry logic
             *
             * Note:
             * This API is deprecated. Please use the corresponding API in ajax-util instead.
             * Keep it here for now and will be removed in the future.
             *
             * Supported patterns:
             * 1. ajaxGetWithRetry(url)
             *    url: a target URL string (e.g. '/sso.static/dashboards.subscribedapps')
             * 2. ajaxGetWithRetry(options)
             *    options: a Object which holds all the settings (including url) required by an ajax call.
             *             Support standard jquery settings and additional options "retryLimit", "showMessages"
             * 3. ajaxGetWithRetry(url, options)
             *    url: a target URL string
             *    options: a Object which contains the settings required by an ajax call
             *             Support standard jquery settings and additional options "retryLimit", "showMessages"
             * 4. ajaxGetWithRetry(url, successCallback)
             *     url: a target URL string
             *     successCallback: success call back function
             * 5. ajaxGetWithRetry(url, successCallback, options)
             *     url: a target URL string
             *     successCallback: success call back function
             *     options: a Object which contains the settings required by an ajax call
             *             Support standard jquery settings and additional options "retryLimit", "showMessages"
             *
             * @returns {Deferred Object}
             */
            self.ajaxGetWithRetry = function () {
                var args = arguments;
                var ajaxOptions = ajaxUtil.getAjaxOptions(args);
                return ajaxUtil.ajaxGetWithRetry(ajaxOptions);
            };

            /**
             * Generate a GUID string
             *
             * @returns {String}
             */
            self.getGuid = function () {
                function securedRandom() {
                    var arr = new Uint32Array(1);
                    var crypto = window.crypto || window.msCrypto;
                    crypto.getRandomValues(arr);
                    var result = arr[0] * Math.pow(2, -32);
                    return result;
                }
                function S4() {
                    return parseInt(((1 + securedRandom()) * 0x10000)).toString(16).substring(1);
                }

                return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
            };

            /**
             * Show a page level message in branding bar
             *
             * Note:
             * This API is deprecated. Please use the corresponding API in message-util instead.
             * Keep it here for now and will be removed in the future.
             *
             * @param {Object} message message to be shown on UI, supported properties include:
             *          type:            String, Required. Message type, should be one of "error", "warn", "confirm", "info".
             *          summary:         String, Required. Message summary.
             *          detail:          String, Optional. Message details.
             *          removeDelayTime: Number, Optional. Delay time (in milliseconds) for the message to be closed automatically from common message UI.
             *                           If not specified, it will not be closed automatically by default.
             *
             * @returns {String} message id
             */
            self.showMessage = function (message) {
                var messageId = null;
                if (message && typeof (message) === "object") {
                    message.tag = "EMAAS_SHOW_PAGE_LEVEL_MESSAGE";
                    if (message.id) {
                        messageId = message.id;
                    }
                    else {
                        messageId = self.getGuid();
                        message.id = messageId;
                    }
                    window.postMessage(message, window.location.href);
                }
                return messageId;
            };

            /**
             * Format a message by replacing the placeholds inside the message string with parameters passed in
             *
             * Note:
             * This API is deprecated. Please use the corresponding API in message-util instead.
             * Keep it here for now and will be removed in the future.
             *
             * @param {String} message message to be formatted.
             * @param {Array} args parameters used to replace the placeholds
             *
             * @returns
             */
            self.formatMessage = function (message) {
                var i = 1;
                while (i < arguments.length) {
                    message = message.replace("{" + (i - 1) + "}", arguments[i++]);
                }
                return message;
            };

            self.getRelUrlFromFullUrl = function (url) {
                if (!url) {
                    return url;
                }
                var protocolIndex = url.indexOf('://');
                if (protocolIndex === -1) {
                    return url;
                }
                var urlNoProtocol = url.substring(protocolIndex + 3);
                var relPathIndex = urlNoProtocol.indexOf('/');
                if (relPathIndex === -1) {
                    return url;
                }
                return urlNoProtocol.substring(relPathIndex);
            };

            /**
             * Generate the window title according to given params.
             *
             * @param {type} pageName
             * @param {type} contextName
             * @param {type} targetType
             * @param {type} serviceName
             * @returns {String}
             */
            self.generateWindowTitle = function (pageName, contextName, targetType, serviceName) {
                var title = "";
                var title_suffix = nls.BRANDING_BAR_ABOUT_DIALOG_SUB_TITLE;
                if (pageName) {
                    title = title + pageName;
                }
                if (contextName) {
                    title = pageName ? (title + ": ") : title;
                    title = title + contextName;
                    if (targetType) {
                        title = title + " (" + targetType + ")";
                    }
                }
                if (pageName || contextName) {
                    title = title + " - ";
                }
                if (serviceName) {
                    title = title + serviceName + " - ";
                }
                title = title + title_suffix;
                return title;
            };

            self.setupSessionLifecycleTimeoutTimer = function (sessionExpiryTime, warningDialogId) {
                //Get session expiry time and do session timeout handling
                if (sessionExpiryTime && sessionExpiryTime.length >= 14) {
                    var now = new Date().getTime();
                    //Get UTC session expiry time
                    var utcSessionExpiry = Date.UTC(sessionExpiryTime.substring(0, 4),
                        sessionExpiryTime.substring(4, 6) - 1, sessionExpiryTime.substring(6, 8),
                        sessionExpiryTime.substring(8, 10), sessionExpiryTime.substring(10, 12),
                        sessionExpiryTime.substring(12, 14));
                    //Caculate wait time for client timer which will show warning dialog when session expired
                    //Note: the actual session expiry happens about 40 secs - 1 min before the time we get from
                    //SESSION_EXP header, so set the timer for session expiry to be 1 min before SESSION_EXP
                    var waitTimeBeforeWarning = utcSessionExpiry - now - 60 * 1000;
                    //Show warning dialog when session expired
                    setTimeout(function () {
                        window.postMessage({'tag': 'EMAAS_OMC_SESSION_TIME_OUT'}, window.location.href);
                    }, waitTimeBeforeWarning);
                }
            };

            self.getPreferencesUrl = function () {
                if (self.isDevMode()) {
                    return self.buildFullUrl(self.getDevData().dfRestApiEndPoint, "preferences");
                } else {
                    return '/sso.static/dashboards.preferences';
                }
            };

            /**
             *
             * @param {number} pWidth The width of the container to put screenshot
             * @param {number} pHeight The height of the container to put screenshot
             * @param {type} scrshotHref The url of screenshot
             * @param {function} callback The callback after image is loaded
             * @returns {undefined}
             */
            self.getScreenshotSizePerRatio = function (pWidth, pHeight, scrshotHref, callback) {
                var ratio = pWidth / pHeight;

                var tmpImage = new Image();
                tmpImage.src = scrshotHref;

                tmpImage.onload = function () {
                    var imgWidth = tmpImage.width;
                    var imgHeight = tmpImage.height;
                    var imgRatio = imgWidth / imgHeight;

                    if (imgRatio > ratio) {
                        imgHeight = imgHeight * pWidth / imgWidth;
                        imgWidth = pWidth;
                    } else {
                        imgWidth = imgWidth * pHeight / imgHeight;
                        imgHeight = pHeight;
                    }

                    if (callback) {
                        callback(imgWidth, imgHeight);
                    }
                };
            };

            /**
             * Add UIFWK stylesheet to the head of the html document.
             *
             * @returns 
             */
            self.loadUifwkCss = function () {
                //Check if uifwk css file has been loaded already or not, if not then load it
                if (!$('#uifwkAltaCss').length) {
                    //Append uifwk css file into document head
                    $('head').append('<link id="uifwkAltaCss" rel="stylesheet" href="/emsaasui/uifwk/@version@/css/uifwk-alta.css" type="text/css"/>');
                }
                
                document.getElementById('uifwkAltaCss').onload = function() {
                    if (window._uifwk && window._uifwk.brandingbar_css_load_callback && $.isFunction(window._uifwk.brandingbar_css_load_callback)) {
                        window._uifwk.brandingbar_css_load_callback();
                    }
                };
            };

            self.showSessionTimeoutWarningDialog = function(warningDialogId) {
                //Clear interval for extending user session
                /* globals clearInterval */
                if (window.intervalToExtendCurrentUserSession) {
                    clearInterval(window.intervalToExtendCurrentUserSession);
                }
                window.currentUserSessionExpired = true;
                self.clearSessionCache();
                //Open sessin timeout warning dialog
                $('#' + warningDialogId).ojDialog('open');
            }

            self.clearSessionCache = function () {
                window.sessionStorage.removeItem('_uifwk_brandingbar_cache');
                window.sessionStorage.removeItem('_uifwk_omccontextcache_composite');
                window.sessionStorage.removeItem('_uifwk_omccontextcache_entity');
                window.sessionStorage.removeItem('_uifwk_servicemenucache');
                window.sessionStorage.removeItem('_udeCommonSessionCache');
                for (var attr in window.sessionStorage) {
                    if (attr.indexOf('_udeTopologyCache') === 0) {
                        window.sessionStorage.removeItem(attr);
                    }
                }
            };

            self.getVisualAnalyzer = function (serviceName) {
                var visualAnalyzer = null;
                self.getRegistrations(function (registrations) {
                    if (registrations && registrations.visualAnalyzers) {
                        for (var i = 0; i < registrations.visualAnalyzers.length; i++) {
                            if (registrations.visualAnalyzers[i].serviceName === serviceName) {
                                visualAnalyzer = registrations.visualAnalyzers[i].href;
                                console.debug("Retrieved visualAnalyzer link for service " + serviceName + " from configuration/registration, link is " + visualAnalyzer);
                                break;
                            }
                        }
                    }
                }, false);
                return visualAnalyzer;
            }
            self.getAssetRoot = function (serviceName) {
                var assetRoot = null;
                self.getRegistrations(function (registrations) {
                    if (registrations && registrations.assetRoots) {
                        for (var i = 0; i < registrations.assetRoots.length; i++) {
                            if (registrations.assetRoots[i].serviceName === serviceName) {
                                assetRoot = registrations.assetRoots[i].href;
                                console.debug("Retrieved visualAnalyzer link for service " + serviceName + " from configuration/registration, link is " + assetRoot);
                                break;
                            }
                        }
                    }
                }, false);
                return assetRoot;
            }
            
            self.getSubscribedApps2WithEdition = function(successCallback, errorCallback) {
                if (window._uifwk && window._uifwk.cachedData && window._uifwk.cachedData.subscribedapps2 &&
                        ($.isFunction(window._uifwk.cachedData.subscribedapps2) ? window._uifwk.cachedData.subscribedapps2() : true)) {
                    successCallback($.isFunction(window._uifwk.cachedData.subscribedapps2) ? window._uifwk.cachedData.subscribedapps2() :
                            window._uifwk.cachedData.subscribedapps2);
                } else {
                    if (!window._uifwk) {
                        window._uifwk = {};
                    }
                    if (!window._uifwk.cachedData) {
                        window._uifwk.cachedData = {};
                    }
                    if (!window._uifwk.cachedData.isFetchingSubscribedApps2) {
                        window._uifwk.cachedData.isFetchingSubscribedApps2 = true;
                        if (!window._uifwk.cachedData.subscribedapps2) {
                            window._uifwk.cachedData.subscribedapps2 = ko.observable();
                        }

                        function doneCallback(data, textStatus, jqXHR) {
                            window._uifwk.cachedData.subscribedapps2(data);
                            window._uifwk.cachedData.isFetchingSubscribedApps2 = false;
                            successCallback(data, textStatus, jqXHR);
                        }
                        var url = null;
                        if (self.isDevMode()) {
                            url = self.buildFullUrl(self.getDevData().dfRestApiEndPoint, "subscribedapps2");
                        } else {
                            url = '/sso.static/dashboards.subscribedapps2';
                        }
                        ajaxUtil.ajaxWithRetry({type: 'GET', contentType: 'application/json', url: url,
                            dataType: 'json',
                            headers: this.getDefaultHeader(),
                            async: true,
                            success: function (data, textStatus, jqXHR) {
                                doneCallback(data, textStatus, jqXHR);
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                console.log('Failed to get subscribed app info!');
                                window._uifwk.cachedData.isFetchingSubscribedApps2 = false;
                                if (errorCallback) {
                                    errorCallback(jqXHR, textStatus, errorThrown);
                                }
                            }
                        });
                    } else {
                        window._uifwk.cachedData.subscribedapps2.subscribe(function (data) {
                            if (data) {
                                successCallback(data);
                            }
                        });
                    }
                }
            }

            self.getSubscribedAppsWithoutEdition = function(successCallback, errorCallback) {
                if (window._uifwk && window._uifwk.cachedData && window._uifwk.cachedData.subscribedapps &&
                        ($.isFunction(window._uifwk.cachedData.subscribedapps) ? window._uifwk.cachedData.subscribedapps() : true)) {
                    successCallback($.isFunction(window._uifwk.cachedData.subscribedapps) ? window._uifwk.cachedData.subscribedapps() :
                            window._uifwk.cachedData.subscribedapps);
                } else {
                    if (!window._uifwk) {
                        window._uifwk = {};
                    }
                    if (!window._uifwk.cachedData) {
                        window._uifwk.cachedData = {};
                    }
                    if (!window._uifwk.cachedData.isFetchingSubscribedApps) {
                        window._uifwk.cachedData.isFetchingSubscribedApps = true;
                        if (!window._uifwk.cachedData.subscribedapps) {
                            window._uifwk.cachedData.subscribedapps = ko.observable();
                        }

                        function doneCallback(data, textStatus, jqXHR) {
                            window._uifwk.cachedData.subscribedapps(data);
                            window._uifwk.cachedData.isFetchingSubscribedApps = false;
                            successCallback(data, textStatus, jqXHR);
                        }
                        var url = null;
                        if (self.isDevMode()) {
                            url = self.buildFullUrl(self.getDevData().dfRestApiEndPoint, "subscribedapps");
                        } else {
                            url = '/sso.static/dashboards.subscribedapps';
                        }
                        ajaxUtil.ajaxWithRetry({type: 'GET', contentType: 'application/json', url: url,
                            dataType: 'json',
                            headers: this.getDefaultHeader(),
                            async: true,
                            success: function (data, textStatus, jqXHR) {
                                doneCallback(data, textStatus, jqXHR);
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                console.log('Failed to get subscribed app info!');
                                window._uifwk.cachedData.isFetchingSubscribedApps = false;
                                if (errorCallback) {
                                    errorCallback(jqXHR, textStatus, errorThrown);
                                }
                            }
                        });
                    } else {
                        window._uifwk.cachedData.subscribedapps.subscribe(function (data) {
                            if (data) {
                                successCallback(data);
                            }
                        });
                    }
                }
            }

            self.getRegistrations = function (successCallback, toSendAsync, errorCallback) {
                if (window._uifwk && window._uifwk.cachedData && window._uifwk.cachedData.registrations && 
                        ($.isFunction(window._uifwk.cachedData.registrations) ? window._uifwk.cachedData.registrations() : true)) {
                    successCallback($.isFunction(window._uifwk.cachedData.registrations) ? window._uifwk.cachedData.registrations() : 
                            window._uifwk.cachedData.registrations);
                } else {
                    if (!window._uifwk) {
                        window._uifwk = {};
                    }
                    if (!window._uifwk.cachedData) {
                        window._uifwk.cachedData = {};
                    }
                    if (!window._uifwk.cachedData.isFetchingRegistrations) {
                        window._uifwk.cachedData.isFetchingRegistrations = true;
                        if (!window._uifwk.cachedData.registrations) {
                            window._uifwk.cachedData.registrations = ko.observable();
                        }else{
                            if (!$.isFunction(window._uifwk.cachedData.registrations));{
                                console.info("EMCPDF-5019 is hit: BEGIN");
                                console.info("registrations data: "+JSON.stringify(window._uifwk.cachedData.registrations));
                                console.info("toSendAsync: "+toSendAsync);
                                console.info("successCallback: "+successCallback);
                                console.info("errorCallback: "+errorCallback);
                                console.info("EMCPDF-5019 is hit: END");
                            }                            
                        }
                        if (!window._uifwk.cachedData.errGetRegistration) {
                            window._uifwk.cachedData.errGetRegistration = ko.observable(false);
                        }
                        else {
                            window._uifwk.cachedData.errGetRegistration(false);
                        }

                        function doneCallback(data, textStatus, jqXHR) {
                            if (!$.isFunction(window._uifwk.cachedData.registrations));{
                                console.info("EMCPDF-5019 is hit 2: BEGIN");
                                console.info("data: "+JSON.stringify(data));
                                console.info(jqXHR);
                                console.info("EMCPDF-5019 is hit 2: END");
                            }                            
                            window._uifwk.cachedData.registrations(data);
                            window._uifwk.cachedData.isFetchingRegistrations = false;
                            successCallback(data, textStatus, jqXHR);
                        }
                        if (window._registrationServerCache) {
                            doneCallback(window._registrationServerCache);
                        }
                        else {
                            ajaxUtil.ajaxWithRetry({type: 'GET', contentType: 'application/json', url: self.getRegistrationUrl(),
                                dataType: 'json',
                                headers: this.getDefaultHeader(),
                                async: toSendAsync === false ? false : true,
                                success: function (data, textStatus, jqXHR) {
                                    doneCallback(data, textStatus, jqXHR);
                                },
                                error: function (jqXHR, textStatus, errorThrown) {
                                    console.log('Failed to get registration info!');
                                    window._uifwk.cachedData.isFetchingRegistrations = false;
                                    window._uifwk.cachedData.errGetRegistration(true);
                                    if (errorCallback) {
                                        errorCallback(jqXHR, textStatus, errorThrown);
                                    }
                                }
                            });
                        }
                    } else {
                        window._uifwk.cachedData.registrations.subscribe(function (data) {
                            if (data) {
                                successCallback(data);
                            }
                        });
                        window._uifwk.cachedData.errGetRegistration.subscribe(function (data) {
                            if (data && errorCallback) {
                                errorCallback();
                            }
                        });
                    }
                }
            };

            self.getRegistrationUrl = function () {
                if (self.isDevMode()) {
                    return self.buildFullUrl(self.getDevData().dfRestApiEndPoint, "configurations/registration");
                } else {
                    return '/sso.static/dashboards.configurations/registration';
                }
            };
            
            /**
             * Check if service type is V1 or not.
             * 
             * @param {type} subscribedApps: applications returned from subscribedapps rest api.
             * @returns {Boolean}
             */
            self.isV1ServiceTypes = function(subscribedApps) {
                var len = subscribedApps.length;
                var nonV1ServiceTypes = ["OMC", "OSMACC", "OMCSE", "OMCEE", "OMCLOG", "SECSE", "SECSMA"];
                var isV1ServiceTypes = true;
                for(var i=0; i<len; i++) {
                    var app = subscribedApps[i];
                    if($.inArray(app["id"], nonV1ServiceTypes) >=0) {
                        isV1ServiceTypes = false;
                        return isV1ServiceTypes;
                    }
                }
                return isV1ServiceTypes;
            };
        }

        return DashboardFrameworkUtility;
    }
);

