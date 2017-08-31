define([
    'jquery',
    'knockout',
    'ojs/ojcore',
    'uifwk/@version@/js/util/message-util-impl', 
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg'
],
    function($, ko, oj, msgUtilModel, nls)
    {
        /**
         * Register ajax global start / stop event
         * so that application may know if there are active ajax requests.
         * Note that only ajax requests that are sent via jQuery can be monitored.
         * @type Function|ko.dependentObservable
         */
        var activeAjaxMonitor = (function () {
            var ajaxPending = ko.observable(false);

            $(document).ajaxStart(function () {
                ajaxPending(true);
            });

            $(document).ajaxStop(function () {
                ajaxPending(false);
            });

            var ajaxPendingDelayNotification = ko.pureComputed(function () {
                return ajaxPending();
            });
            ajaxPendingDelayNotification.extend({rateLimit: {timeout: 0, method: "notifyWhenChangesStop"}});
            return ajaxPendingDelayNotification;

        })();

        function DashboardFrameworkAjaxUtility() {
            var self = this;
            var messageUtil = new msgUtilModel();

            /**
             * register the ajax stop event
             * @param {type} handler
             * @param {type} wait    wait n ms to call the handler after ajax stops
             * @param {type} maxWait if wait time is over maxWait, the handler
             *                       will be executed anyway.
             * @returns {undefined}
             */
            self.actionAfterAjaxStop = function(handler, wait, maxWait) {
                var maxWaitTimeout = null;
                var waitTimeout = null;
                if (maxWait) {
                    maxWaitTimeout = setTimeout(function () {
                        clearTimeout(waitTimeout);
                        handler();
                    }, maxWait);
                }

                var subscription = activeAjaxMonitor.subscribe(function (ajaxRunning) {
                    if (!ajaxRunning) {
                        clearTimeout(waitTimeout);
                        waitTimeout = setTimeout(function () {
                            subscription.dispose();
                            clearTimeout(maxWaitTimeout);
                            handler();
                        }, wait);
                    }
                });
            };

            /**
             * Ajax call with retry logic
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
            self.ajaxWithRetry = function() {
                var args = arguments;
                var retryOptions = self.getAjaxOptions(args);
                var retryCount = 0;
                var retryLimit = retryOptions.retryLimit !== null &&
                        typeof(retryOptions.retryLimit) === 'number' ? retryOptions.retryLimit : 3;
                retryLimit = retryLimit > 0 ? retryLimit : 0;
                //Do not show error message on UI
//                var showMessages = 'none'; //isValidShowMessageOption(retryOptions.showMessages) ? retryOptions.showMessages : 'none';
                //Retry delay time in milliseconds, if not set, will be 2 seconds by default
                var retryDelayTime = retryOptions.retryDelayTime !== null &&
                        typeof(retryOptions.retryDelayTime) === 'number' ? retryOptions.retryDelayTime : 500;
                var messageId = null;
                var messageObj = null;
                var errorCallBack = retryOptions.error;
                retryOptions.error = null;
                var beforeSendCallback = retryOptions.beforeSend;
                retryOptions.beforeSend = function(jqXHR, settings) {
                    jqXHR.url = retryOptions.url;
                    //Call individual beforeSend callback if exists
                    if (beforeSendCallback && $.isFunction(beforeSendCallback)){
                        beforeSendCallback(jqXHR, settings);
                    }
                    //Otherwise call beforeSend callback if it has been set up by $.ajaxSetup()
                    else {
                        var beforeSendInAjaxSetup = $.ajaxSetup()['beforeSend'];
                        if (beforeSendInAjaxSetup && $.isFunction(beforeSendInAjaxSetup)){
                            beforeSendInAjaxSetup(jqXHR, settings);
                        }
                    }
                };

                var ajaxCallDfd = $.Deferred();
                var jqXhrObj = null;
                var firstCalled = false;
//                var underPlannedDowntime = false;
                //Add ability to abort ajaxWithRetry calls
                ajaxCallDfd.abort = function(){
                    if (jqXhrObj !== null && $.isFunction(jqXhrObj.abort)) {
                        return jqXhrObj.abort();
                    }
                };

                (function ajaxCall (retries) {
                    jqXhrObj = $.ajax(retryOptions);
                    jqXhrObj.done(function (data, textStatus, jqXHR) {
//                        removeMessage(messageId);
                        ajaxCallDfd.resolve(data, textStatus, jqXHR);
                    })
                    .fail(function (jqXHR, textStatus, errorThrown) {
                        //Do retry
                        if (jqXHR.status === 408 || jqXHR.status === 503 || (jqXHR.status === 0 && textStatus !== 'abort')) {
                            if (!firstCalled) {
                                //When API Gateway node can't process an incoming ordered write request because an ordered write request 
                                //from that tenant is already in progress. In this case the client (GUI code) should retry the request 
                                //after every "retry-after" number of seconds every "num-retry" number of times before giving up and 
                                //returning the error to the user. In the following example GUI shall retry 3 times after every 2 secs 
                                //before returning the error message to the user
                                //X-ORCL-OMC-APIGW-RETRYAFTER : retry-after=2, num-retry=3, msg="tenant locked"
                                var apigwHeaders = self.getAPIGWHeaderValues(jqXHR, 'X-ORCL-OMC-APIGW-RETRYAFTER');
                                if (jqXHR.status === 503 && apigwHeaders && apigwHeaders['msg'].toLowerCase() === 'tenant locked') {
                                    if (apigwHeaders['retry-after'] && apigwHeaders['num-retry'] && retries!== 0) {
                                        retries = apigwHeaders['num-retry'];
                                        retryDelayTime = apigwHeaders['retry-after']*1000; //Convert to milliseconds
                                        retryLimit = apigwHeaders['num-retry'];
                                    }
                                }
                                //Check to see if OMC is under planned downtime, if yes, show a warning message to user and no need to retry
                                else if (jqXHR.status === 503 && apigwHeaders && apigwHeaders['msg'].toLowerCase() === 'planned downtime') {
                                    retries = 0;
//                                    underPlannedDowntime = true;
                                    //show message to user when OMC is under planned downtime
                                    messageId = messageUtil.getGuid();
                                    var summaryMsg = nls.BRANDING_BAR_MESSAGE_PLANNED_DOWNTIME_SUMMARY;
                                    var detailMsg = null;
                                    //Show planned downtime message detail
                                    detailMsg = nls.BRANDING_BAR_MESSAGE_PLANNED_DOWNTIME_DETAIL;
                                    messageObj = {
                                        id: messageId, 
                                        action: 'show', 
                                        type: 'warn', 
                                        category: 'omc_planned_downtime',
                                        summary: summaryMsg, 
                                        detail: detailMsg};

                                    //Always show retry message summary and detail on UI
                                    if (window._uiwfk && window._uiwfk.brandingbar_initialized) {
                                        messageUtil.showMessage(messageObj);
                                    }
                                    else {
                                        function onBrandingbarInitialized(event) {
                                            if (event.origin !== window.location.protocol + '//' + window.location.host) {
                                                return;
                                            }
                                            var data = event.data;
                                            if (data && data.tag && data.tag === 'EMAAS_BRANDINGBAR_INSTANTIATED') {
                                                messageUtil.showMessage(messageObj);
                                            }
                                        }
                                        window.addEventListener("message", onBrandingbarInitialized, false);
                                    }
                                }
                                firstCalled = true;
                            }
                            
                            retryCount++;
                            if (retries > 0) {
//                                //remove old retry message
//                                removeMessage(messageId);
//
//                                //Set new retry message to be shown on UI
//                                messageId = messageUtil.getGuid();
//                                var summaryMsg = nls.BRANDING_BAR_MESSAGE_AJAX_RETRYING_SUMMARY;
//                                var detailMsg = null;
//                                //Show retry message detail
//                                detailMsg = nls.BRANDING_BAR_MESSAGE_AJAX_RETRYING_DETAIL;
//                                detailMsg = messageUtil.formatMessage(detailMsg, retryCount);
//                                messageObj = {
//                                    id: messageId,
//                                    action: 'show',
//                                    type: 'warn',
//                                    category: 'retry_in_progress',
//                                    summary: summaryMsg,
//                                    detail: detailMsg};
//
//                                //Always show retry message summary and detail on UI
//                                messageUtil.showMessage(messageObj);

                                //Do retry once again if failed
                                setTimeout(function(){ajaxCall(retries - 1);}, retryDelayTime);

                                return false;
                            }

//                            //remove old retrying message after retries
//                            removeMessage(messageId);

//                            if (showMessages !== 'none') {
//                                //show new retry error message
//                                //Set failure message to be shown on UI after retries
//                                var errorSummaryMsg = nls.BRANDING_BAR_MESSAGE_AJAX_RETRY_FAIL_SUMMARY;
//                                errorSummaryMsg = messageUtil.formatMessage(errorSummaryMsg, retryLimit);
//
//                                var errorDetailMsg = null;
//                                if (showMessages === 'all') {
//                                    var responseErrorMsg = getMessageFromXhrResponse(jqXHR);
//                                    errorDetailMsg = responseErrorMsg !== null ? responseErrorMsg :
//                                            nls.BRANDING_BAR_MESSAGE_AJAX_RETRY_FAIL_DETAIL;
//                                    errorDetailMsg = messageUtil.formatMessage(errorDetailMsg);
//                                }
//                                messageObj = {
//                                    id: messageUtil.getGuid(),
//                                    action: 'show',
//                                    type: "error",
//                                    category: 'retry_fail',
//                                    summary: errorSummaryMsg,
//                                    detail: errorDetailMsg};
//
//                                //Show error message on UI
//                                messageUtil.showMessage(messageObj);
//                            }
                            //Log message
                            var errorMsg = "Attempts to connect to your cloud service failed after {0} tries. Target URL: {1}.";
                            errorMsg = messageUtil.formatMessage(errorMsg, retryLimit, retryOptions.url);
                            //Output log to console if requested url is logging api to avoid endless loop, otherwise output to server side
                            logMessage(retryOptions.url, 'error', errorMsg);
                        }

                        //Call error callback if the ajax call finally failed
                        if (errorCallBack) {
                            errorCallBack(jqXHR, textStatus, errorThrown);
                        }

                        ajaxCallDfd.reject(jqXHR, textStatus, errorThrown);

                        return false;
                    });
                }(retryLimit));

                return ajaxCallDfd;
            };

            /**
             * Make an ajax get call with retry logic
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
            self.ajaxGetWithRetry = function() {
                var args = arguments;
                var retryOptions = self.getAjaxOptions(args);

                //Set ajax call type to GET
                retryOptions.type = 'GET';

                //call ajaxWithRetry
                return self.ajaxWithRetry(retryOptions);
            };

            self.getAjaxOptions = function(args) {
                var argsLength = args.length;
                var retryOptions = {};
                if (argsLength === 1) {
                    if (typeof(args[0]) === 'string'){
                        retryOptions.url = args[0];
                    }
                    if (typeof(args[0]) === 'object'){
                        retryOptions = args[0];
                    }
                }
                else if (argsLength === 2) {
                    if (typeof(args[0]) === 'string' && args[1] !== null && typeof(args[1]) === 'object') {
                        retryOptions = args[1];
                        retryOptions.url = args[0];
                    }
                    else if (typeof(args[0]) === 'string' && typeof(args[1]) === 'function') {
                        retryOptions.url = args[0];
                        retryOptions.success = args[1];
                    }
                    else if (typeof(args[0]) === 'string' &&
                            (typeof(args[1]) === 'undefined' || args[1] === null)) {
                        retryOptions.url = args[0];
                    }
                    else if (args[0] !== null && typeof(args[0]) === 'object' &&
                            (typeof(args[1]) === 'undefined' || args[1] === null)) {
                        retryOptions = args[0];
                    }
                }
                else if (argsLength === 3) {
                    if (typeof(args[0]) === 'string' &&
                            typeof(args[1]) === 'function' &&
                            args[2] !== null && typeof(args[2]) === 'object') {
                        retryOptions = args[2];
                        retryOptions.url = args[0];
                        retryOptions.success = args[1];
                    }
                    else if (typeof(args[0]) === 'string' &&
                            typeof(args[1]) === 'function' &&
                            (typeof(args[2]) === 'undefined' || args[2] === null)) {
                        retryOptions.url = args[0];
                        retryOptions.success = args[1];
                    }
                    else if (typeof(args[0]) === 'string' &&
                            (args[1] === null || typeof(args[1]) === 'undefined') &&
                            args[2] !== null && typeof(args[2]) === 'object') {
                        retryOptions = args[2];
                        retryOptions.url = args[0];
                    }
                    else if (typeof(args[0]) === 'string' &&
                            (args[1] === null || typeof(args[1]) === 'undefined') &&
                            (args[2] === null || typeof(args[2]) === 'undefined')) {
                        retryOptions.url = args[0];
                    }
                }

                return retryOptions;
            };
            
            /**
             * Get header values map from API Gateway's response.
             * 
             * @param {Object} jqXHR
             * @param {String} headerName
             * 
             * @returns {Object} headerValuesMap
             */ 
            self.getAPIGWHeaderValues = function(jqXHR, headerName) {
                var headerValuesMap = null;
                var apigwHeader = jqXHR.getResponseHeader(headerName); //'retry-after=3600, num-retry=0, msg="planned downtime"';
                if (apigwHeader) {
                    var headerValues = apigwHeader.split(',');
                    headerValuesMap = {};
                    for (var i = 0; i < headerValues.length; i++) {
                        var headerItem = headerValues[i];
                        var itemValuePair = headerItem.split('=');
                        if (itemValuePair.length === 2) {
                            headerValuesMap[$.trim(itemValuePair[0])] = $.trim(itemValuePair[1].replace(/["']/g, ''));
                        }
                    }
                }
                return headerValuesMap;
            };

//            function isValidShowMessageOption(messageOption) {
//                return messageOption === "none" || messageOption === "summary" ||
//                        messageOption === "all";
//            }

            function logMessage(url, messageType, messageText) {
                if (messageType){
                    messageType = messageType.toLowerCase();
                }
                if (url === '/sso.static/dashboards.logging/logs') {
                    switch(messageType) {
                        case 'error':
                            window.console.error(messageText);
                            break;
                        case 'warn':
                            window.console.warn(messageText);
                            break;
                        case 'info':
                            window.console.info(messageText);
                            break;
                        case 'log':
                            window.console.log(messageText);
                            break;
                        default:
                            window.console.log(messageText);
                    }
                }
                else {
                    switch(messageType) {
                        case 'error':
                            oj.Logger.error(messageText);
                            break;
                        case 'warn':
                            oj.Logger.warn(messageText);
                            break;
                        case 'info':
                            oj.Logger.info(messageText);
                            break;
                        case 'log':
                            oj.Logger.log(messageText);
                            break;
                        default:
                            oj.Logger.log(messageText);
                    }
                }
            }

//            function removeMessage(messageId) {
//                if (messageId) {
//                    var messageObj = {id: messageId, tag: 'EMAAS_SHOW_PAGE_LEVEL_MESSAGE', action: 'remove', category: 'retry_in_progress'};
//                    window.postMessage(messageObj, window.location.href);
//                }
//            }

//            function getMessageFromXhrResponse(xhr) {
//                var message = null;
//                var respJson = xhr.responseJSON;
//                if (typeof respJson !== "undefined" &&
//                        respJson.hasOwnProperty("errorMessage") &&
//                        typeof respJson.errorMessage !== "undefined" &&
//                        respJson.errorMessage !== "") {
//                    message = respJson.errorMessage;
//                }
//                //do not show response text for now, as it may contains information not friendly to the end user
//
//                return message;
//            }

        }

        return DashboardFrameworkAjaxUtility;
    }
);

