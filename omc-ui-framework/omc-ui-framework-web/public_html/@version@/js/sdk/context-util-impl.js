define('uifwk/@version@/js/sdk/context-util-impl', [
    'ojs/ojcore',
    'knockout',
    'jquery',
    'uifwk/@version@/js/util/df-util-impl',
    'uifwk/@version@/js/sdk/entity-object',
    'uifwk/@version@/js/sdk/SessionCacheUtil',
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg'
],
    function (oj, ko, $, dfuModel, EntityObject, SessionCacheUtil, nls)
    {
        function UIFWKContextUtil() {
            var self = this;
            var dfu = new dfuModel();
            var supportedContext = [{'contextName': 'time', 'paramNames': ['startTime', 'endTime', 'timePeriod']},
                {'contextName': 'composite', 'paramNames': ['compositeType', 'compositeName', 'compositeMEID']},
                {'contextName': 'entity', 'paramNames': ['entitiesType', /*'entityName',*/ 'entityMEIDs']}
            ];
            var omcCtxParamName = 'omcCtx';

            //Initialize window _uifwk object
            if (!window._uifwk) {
                window._uifwk = {};
                //Respect all OMC global context by default
                window._uifwk.respectOMCApplicationContext = true;
                window._uifwk.respectOMCEntityContext = true;
                window._uifwk.respectOMCTimeContext = true;
            }

            self.OMCEventSourceConstants = {
                GLOBAL_TIME_SELECTOR: 'OMC_UIFWK_TIME_SELECTOR',
                GLOBAL_ENTITY_SELECTOR: 'OMC_UIFWK_ENTITY_SELECTOR',
                GLOBAL_APPLICATION_SELECTOR: 'OMC_UIFWK_APPLICATION_SELECTOR',
                GLOBAL_TOPOLOGY: 'OMC_UIFWK_TOPOLOGY'
            };

            self.OMCEventContextNameConstants = {
                ALL: "All",
                START_TIME: "startTime",
                END_TIME: "endTime",
                TIME_PERIOD: "timePeriod",
                START_END_TIME: "startEndTime",
                COMPOSITE_MEID: "compositeMEID",
                ENTITY_MEIDS: "entityMEIDs",
                ENTITY: "entity",
                COMPOSITE: "composite",
                TIME: "time"
            };

            self.OMCTimeConstants = {
                TIME_UNIT: {
                    SECOND: 'SECOND',
                    MINUTE: 'MINUTE',
                    HOUR: 'HOUR',
                    DAY: 'DAY',
                    WEEK: 'WEEK',
                    MONTH: 'MONTH',
                    YEAR: 'YEAR'
                },
                QUICK_PICK: {
                    LAST_15_MINUTE: 'LAST_15_MINUTE',
                    LAST_30_MINUTE: 'LAST_30_MINUTE',
                    LAST_60_MINUTE: 'LAST_60_MINUTE',
                    LAST_2_HOUR: 'LAST_2_HOUR',
                    LAST_4_HOUR: 'LAST_4_HOUR',
                    LAST_6_HOUR: 'LAST_6_HOUR',
                    LAST_8_HOUR: 'LAST_8_HOUR',
                    LAST_24_HOUR: 'LAST_24_HOUR',
                    LAST_1_DAY: 'LAST_1_DAY',
                    LAST_7_DAY: 'LAST_7_DAY',
                    LAST_14_DAY: 'LAST_14_DAY',
                    LAST_30_DAY: 'LAST_30_DAY',
                    LAST_90_DAY: 'LAST_90_DAY',
                    LAST_12_MONTH: 'LAST_12_MONTH',
                    LAST_1_YEAR: 'LAST_1_YEAR',
                    LATEST: 'LATEST',
                    CUSTOM: 'CUSTOM'
                },
                timePeriodsSet: {
                    SHORT_TERM: "SHORT_TERM",
                    LONG_TERM: "LONG_TERM"
                }
            };

            //freeze every constant object inside
            Object.freeze(self.OMCEventSourceConstants);
            Object.freeze(self.OMCEventContextNameConstants);
            Object.freeze(self.OMCTimeConstants.TIME_UNIT);
            Object.freeze(self.OMCTimeConstants.QUICK_PICK);
            Object.freeze(self.OMCTimeConstants.timePeriodsSet);

            //
            // sessionStorage cache
            //
            var sessionCaches = [];
            var sessionCacheNames = ['_uifwk_omccontextcache_composite', '_uifwk_omccontextcache_entity'];
            for (var i = 0; i < sessionCacheNames.length; i++) {
                sessionCaches.push(new SessionCacheUtil(sessionCacheNames[i], 1));
            }
            if (window.performance) {
                //When there're multiple instance of ContextUtil, we should only clear the cache once during a page refresh, otherwise
                //it may cause cached data lost though composite/entities already fetched
                if (window.performance.navigation.type === 1 && !window._uifwk.isOmcContextCacheCleared) {
                    for (var i = 0; i < sessionCaches.length; i++) {
                        sessionCaches[i].clearCache();
                    }
                    window._uifwk.isOmcContextCacheCleared = true;
                }
            }
            /**
             * Get URL parameter name for OMC global context.
             * 
             * @returns {String} URL parameter name for OMC global context
             */
            self.getOMCContextUrlParamName = function () {
                return omcCtxParamName;
            };

            /**
             * Specify whether to respect the OMC application context or not
             * 
             * @param {boolean} respectOmcAppCtx Flag for whether respect OMC application context or not
             * 
             * @returns
             */
            self.respectOMCApplicationContext = function (respectOmcAppCtx) {
                window._uifwk.respectOMCApplicationContext = respectOmcAppCtx;
            };

            /**
             * Specify whether to respect the OMC entity context or not
             * 
             * @param {boolean} respectOmcEntityCtx Flag for whether respect OMC entity context or not
             * 
             * @returns
             */
            self.respectOMCEntityContext = function (respectOmcEntityCtx) {
                window._uifwk.respectOMCEntityContext = respectOmcEntityCtx;
            };

            /**
             * Specify whether to respect the OMC time context or not
             * 
             * @param {boolean} respectOmcTimeCtx Flag for whether respect OMC time context or not
             * 
             * @returns
             */
            self.respectOMCTimeContext = function (respectOmcTimeCtx) {
                window._uifwk.respectOMCTimeContext = respectOmcTimeCtx;
            };

            function getGlobalContext() {
                var globalCtx = null;
                //If context already retrieved, fetch it from window object directly
                if (window._uifwk.omcContext) {
                    globalCtx = window._uifwk.omcContext;
                }
                //Otherwise, retrieve the context from URL parameters
                if (!globalCtx) {
                    globalCtx = getContextFromUrl();

                    //Check and fetch cached data from session storage
                    //composite type/name/display name/class
                    if (globalCtx && globalCtx['composite'] && globalCtx['composite']['compositeMEID']) {
                        var compositeCacheKey = globalCtx['composite']['compositeMEID'];
                        var cache = sessionCaches[0].retrieveDataFromCache(compositeCacheKey);
                        if (cache) {
                            if (cache['compositeType']) {
                                globalCtx['composite']['compositeType'] = cache['compositeType'];
                            }
                            if (cache['compositeName']) {
                                globalCtx['composite']['compositeName'] = cache['compositeName'];
                            }
                            if (cache['compositeDisplayName']) {
                                globalCtx['composite']['compositeDisplayName'] = cache['compositeDisplayName'];
                            }
                            if (cache['compositeClass']) {
                                globalCtx['composite']['compositeClass'] = cache['compositeClass'];
                            }
                        }
                    }
                    //Entities
                    if (globalCtx && globalCtx['entity'] && (globalCtx['entity']['entitiesType'] || globalCtx['entity']['entityMEIDs'])) {
                        var entityCacheKey = null;
                        var entityMeIds = globalCtx['entity']['entityMEIDs'] ? globalCtx['entity']['entityMEIDs'].split(',') : null;
                        var entitiesType = globalCtx['entity']['entitiesType'];
                        if (entityMeIds && entityMeIds.length > 0 && entitiesType) {
                            entityCacheKey = entityMeIds.sort().join() + entitiesType;
                        }
                        else if (entityMeIds && entityMeIds.length > 0) {
                            entityCacheKey = entityMeIds.sort().join();
                        }
                        else if (entitiesType) {
                            entityCacheKey = entitiesType;
                        }
                        var cache = sessionCaches[1].retrieveDataFromCache(entityCacheKey);
                        if (cache && cache['entities']) {
                            var entities = [];
                            cache['entities'].forEach(function (item) {
                                var entity = EntityObject.prototype.createFromObject(item);
                                entities.push(entity);
                            });
                            globalCtx['entity']['entities'] = entities;
                        }
                    }
                }

                return globalCtx;
            }

            function getNonGlobalContext() {
                var nonGlobalCtx = null;
                if (window._uifwk.nonGlobalContext) {
                    nonGlobalCtx = window._uifwk.nonGlobalContext;
                }
                return nonGlobalCtx;
            }

            function fetchRespectedOmcContext(context, ctxName, respectOmcCtx) {
                var globalCtx = getGlobalContext();
                var nonGlobalCtx = getNonGlobalContext();
                if (respectOmcCtx !== false) {
                    if (globalCtx && globalCtx[ctxName]) {
                        context[ctxName] = globalCtx[ctxName];
                    }
                }
                else {
                    if (nonGlobalCtx && nonGlobalCtx[ctxName]) {
                        context[ctxName] = nonGlobalCtx[ctxName];
                    }
                }
            }

            function isGlobalContextRespected() {
                return window._uifwk.respectOMCApplicationContext !== false ||
                    window._uifwk.respectOMCEntityContext !== false ||
                    window._uifwk.respectOMCTimeContext !== false;
            }

            /**
             * Get the OMC global context. This api will only return OMC conext, 
             * any page's private context from URL will be ignored. For any page 
             * to use oj_Router, if you want to get OMC context acccurately by 
             * this api during page loading, this api is expected to be called 
             * before any call to oj.Router.rootInstance.store(state) is called.
             * 
             * @param {boolean} respectOmcAppCtx Flag for whether respect OMC application context or not
             * @param {boolean} respectOmcEntityCtx Flag for whether respect OMC entity context or not
             * @param {boolean} respectOmcTimeCtx Flag for whether respect OMC time context or not
             * 
             * @returns {Object} OMC global context in json format
             */
            self.getOMCContext = function (respectOmcAppCtx, respectOmcEntityCtx, respectOmcTimeCtx) {
                var omcContext = null;
                if (respectOmcAppCtx === null || typeof respectOmcAppCtx === 'undefined') {
                    respectOmcAppCtx = window._uifwk.respectOMCApplicationContext;
                }
                if (respectOmcEntityCtx === null || typeof respectOmcEntityCtx === 'undefined') {
                    respectOmcEntityCtx = window._uifwk.respectOMCEntityContext;
                }
                if (respectOmcTimeCtx === null || typeof respectOmcTimeCtx === 'undefined') {
                    respectOmcTimeCtx = window._uifwk.respectOMCTimeContext;
                }
                if (respectOmcAppCtx !== false && respectOmcEntityCtx !== false && respectOmcTimeCtx !== false) {
                    omcContext = getGlobalContext();
                }
                else if (respectOmcAppCtx === false && respectOmcEntityCtx === false && respectOmcTimeCtx === false) {
                    omcContext = getNonGlobalContext();
                }
                else {
                    omcContext = {};
                    //Get application context
                    fetchRespectedOmcContext(omcContext, 'composite', respectOmcAppCtx);
                    fetchRespectedOmcContext(omcContext, 'previousCompositeMeId', respectOmcAppCtx);
                    fetchRespectedOmcContext(omcContext, 'topology', respectOmcAppCtx);
                    //Get entity context
                    fetchRespectedOmcContext(omcContext, 'entity', respectOmcEntityCtx);
                    //Get time context
                    fetchRespectedOmcContext(omcContext, 'time', respectOmcTimeCtx);
                }

                if (!omcContext) {
                    omcContext = {};
                    storeContext(omcContext, respectOmcAppCtx, respectOmcEntityCtx, respectOmcTimeCtx);
                }

//                oj.Logger.info("OMC global context is fetched as: " + JSON.stringify(omcContext));
                return omcContext;
            };

            function getContextFromUrl() {
                var omcContext = {};
                var omcCtxString = decodeURIComponent(dfu.getUrlParam(omcCtxParamName));
                //Loop through supported context list
                for (var i = 0; i < supportedContext.length; i++) {
                    var contextDef = supportedContext[i];
                    var contextName = contextDef.contextName;
                    var contextParams = contextDef.paramNames;
                    //Loop through parameters for each context
                    for (var j = 0; j < contextParams.length; j++) {
                        var paramName = contextParams[j];
                        //Get param value form URL by name
                        var paramValue = retrieveParamValueFromUrl(omcCtxString, paramName);
                        if (paramValue) {
                            //Initialize
                            if (!omcContext[contextName]) {
                                omcContext[contextName] = {};
                            }
                            //Set value into the OMC context JSON object
//                            if (paramName === 'entityMEIDs') {
//                                //Convert string value to array, separated by comma
//                                omcContext[contextName][paramName] = paramValue.split(',');
//                            }
//                            else {
                            omcContext[contextName][paramName] = paramValue;
//                            }
                        }
                    }
                }
                if (!$.isEmptyObject(omcContext)) {
                    storeContext(omcContext, true, true, true);
                    return omcContext;
                }
                return null;
            }

            /**
             * Update the OMC global context. This function is used any the 
             * context is changed from within the page. For example, user changes
             * the time range, or selects a new entity to investigate.
             * 
             * @param {Object} context Context object in json format
             * @param {String} source Source name to tell where the API is called
             * @returns 
             */
            self.setOMCContext = function (context, source) {
                //In case the input context object refers to the same object with window._uifwk.omcContext, 
                //and the context is updated directly by modifying context object rather than call our set methods, 
                //we will never get the previous value by getCompositeMeId. In order to solve this issue, we
                //always get the previous value from the backed up one
                var previousCompositeMeId = getIndividualContext('composite', 'backupCompositeMEID');
//                var omcCtx = self.getOMCContext();
//                omcCtx.previousCompositeMeId = previousCompositeMeId;
                if (!context) {
                    context = {};
                }
                context.previousCompositeMeId = previousCompositeMeId;
                var prevCtx = self.getOMCContext();
                storeContext(context);
                if (isGlobalContextRespected()) {
                    updateCurrentURL();
                    //Fire change event. Need to parse json to avoid data clone error when post message.
                    fireOMCContextChangeEvent('All', JSON.parse(JSON.stringify(prevCtx)), JSON.parse(JSON.stringify(context)), null, source);
                }
            };

            function updateCurrentURL(replaceState) {
                if (replaceState !== false) { //history.replaceState will always be called unless replaceState is set to false explicitly
                    //update current URL
                    var url = window.location.href.split('/').pop();
                    url = self.appendOMCContext(url, true, true, true);
                    var newurl = window.location.pathname.substring(0, window.location.pathname.lastIndexOf('/'));
                    newurl = newurl + '/' + url;
                    window.history.replaceState(window.history.state, document.title, newurl);
                }
            }

            function storeIndividualContext(context, ctxName, respectOmcCtx) {
                if (context && context[ctxName]) {
                    if (respectOmcCtx !== false) {
                        if (!window._uifwk.omcContext) {
                            window._uifwk.omcContext = {};
                        }
                        window._uifwk.omcContext[ctxName] = context[ctxName];
                    }
                    else {
                        if (!window._uifwk.nonGlobalContext) {
                            window._uifwk.nonGlobalContext = {};
                        }
                        window._uifwk.nonGlobalContext[ctxName] = context[ctxName];
                    }
                }
                else {
                    if (respectOmcCtx !== false && window._uifwk.omcContext && window._uifwk.omcContext[ctxName]) {
                        delete window._uifwk.omcContext[ctxName];
                    }
                    else if (window._uifwk.nonGlobalContext && window._uifwk.nonGlobalContext[ctxName]) {
                        delete window._uifwk.nonGlobalContext[ctxName];
                    }
                }
            }

            function storeContext(context, respectOmcAppCtx, respectOmcEntityCtx, respectOmcTimeCtx) {
                //Remember the composite id as previous value, so that we can compare the current/previous value
                //to determine whether topology needs refresh when setOMCContext is called
                if (context && context['composite'] && context['composite']['compositeMEID']) {
                    context['composite']['backupCompositeMEID'] = context['composite']['compositeMEID'];
                }
                //For now, we use window local variable to store the omc context once it's fetched from URL.
                //So even page owner rewrites the URL using oj_Router etc., the omc context will not be lost.
                //But need to make sure the omc context is initialized before page owner start to rewrites
                //the URL by oj_Router etc..
                if (respectOmcAppCtx === null || typeof respectOmcAppCtx === 'undefined') {
                    respectOmcAppCtx = window._uifwk.respectOMCApplicationContext;
                }
                if (respectOmcEntityCtx === null || typeof respectOmcEntityCtx === 'undefined') {
                    respectOmcEntityCtx = window._uifwk.respectOMCEntityContext;
                }
                if (respectOmcTimeCtx === null || typeof respectOmcTimeCtx === 'undefined') {
                    respectOmcTimeCtx = window._uifwk.respectOMCTimeContext;
                }
                if (respectOmcAppCtx !== false && respectOmcEntityCtx !== false && respectOmcTimeCtx !== false) {
                    window._uifwk.omcContext = context;
                }
                else if (respectOmcAppCtx === false && respectOmcEntityCtx === false && respectOmcTimeCtx === false) {
                    window._uifwk.nonGlobalContext = context;
                }
                else {
                    storeIndividualContext(context, 'composite', respectOmcAppCtx);
                    storeIndividualContext(context, 'previousCompositeMeId', respectOmcAppCtx);
                    storeIndividualContext(context, 'topology', respectOmcAppCtx);
                    storeIndividualContext(context, 'entity', respectOmcEntityCtx);
                    storeIndividualContext(context, 'time', respectOmcTimeCtx);
                }
            }

            /**
             * Generate URL with given global context. 
             * The given global context will be appended into the given URL as parameters. 
             * This function is used by custom deep linking code written by page, where the 
             * page owner generates the destination but want to pass on the specific global 
             * context rather than current page's global context.
             * 
             * @param {String} url Original URL
             * @param {Object} omcContext A json object for global context
             * @returns {String} New URL with appended global context
             */
            self.generateUrlWithContext = function (url, omcContext) {
                var newUrl = url;
                if (url) {
                    var omcCtxString = "";
                    if (omcContext) {
                        //Add or update URL parameters string for OMC context
                        for (var i = 0; i < supportedContext.length; i++) {
                            var contextDef = supportedContext[i];
                            var contextName = contextDef.contextName;
                            var contextParams = contextDef.paramNames;
                            //Loop through parameters for each context
                            for (var j = 0; j < contextParams.length; j++) {
                                var paramName = contextParams[j];
                                //Check for available context which should be appended into URL
                                if (omcContext[contextName] && omcContext[contextName][paramName]) {
                                    var paramValue = omcContext[contextName][paramName];
                                    var paramValueStr = '';
                                    //If it's an array, convert it to a comma seperated string
                                    if ($.isArray(paramValue)) {
                                        for (var k = 0; k < paramValue.length; k++) {
                                            if (k === paramValue.length - 1) {
                                                paramValueStr = paramValueStr + paramValue[k];
                                            }
                                            else {
                                                paramValueStr = paramValueStr + paramValue[k] + ',';
                                            }
                                        }
                                    }
                                    else {
                                        paramValueStr = paramValue;
                                    }

                                    omcCtxString = omcCtxString + encodeURIComponent(paramName) + "=" + encodeURIComponent(paramValueStr) + '&';
                                }
                            }
                        }
                        if (omcCtxString && omcCtxString.lastIndexOf('&') !== -1) {
                            omcCtxString = omcCtxString.substring(0, omcCtxString.lastIndexOf('&'));
                        }
                    }
                    //Retrieve omcCtx from original URL
                    var origOmcCtx = retrieveParamValueFromUrl(url, omcCtxParamName);
                    //If OMC context is not empty, append it to the URL, or if original URL has omcCtx already, then update it.
                    if (omcCtxString || origOmcCtx) {
                        //Add or update URL parameters
                        newUrl = addOrUpdateUrlParam(newUrl, omcCtxParamName, encodeURIComponent(omcCtxString));
                    }
                }
                else {
                    oj.Logger.error("Invalid empty URL input!");
                }

                return newUrl;
            };

            /**
             * Get the current OMC global context and append it into the given 
             * URL as parameters. This function is used by custom deep linking 
             * code written by page. Where the page owner generates the destination 
             * but want to pass on the global context.
             * 
             * @param {String} url Original URL
             * @param {boolean} respectOmcAppCtx Flag for whether respect OMC application context or not
             * @param {boolean} respectOmcEntityCtx Flag for whether respect OMC entity context or not
             * @param {boolean} respectOmcTimeCtx Flag for whether respect OMC time context or not
             * 
             * @returns {String} New URL with appended OMC global context
             */
            self.appendOMCContext = function (url, respectOmcAppCtx, respectOmcEntityCtx, respectOmcTimeCtx) {
                return self.generateUrlWithContext(url, self.getOMCContext(respectOmcAppCtx, respectOmcEntityCtx, respectOmcTimeCtx));
            };

            /**
             * Set OMC global context of start time.
             * 
             * @param {Number} startTime Start time
             * @param {String} source Source name to tell where the API is called
             * @returns 
             */
            self.setStartTime = function (startTime, source) {
                if (self.getStartTime() !== parseInt(startTime)) {
                    setIndividualContext('time', 'startTime', parseInt(startTime), true, true, false, source);
                }
            };

            /**
             * Get OMC global context of start time.
             * 
             * @param 
             * @returns {Number} OMC global context of start time
             */
            self.getStartTime = function () {
                var start = getIndividualContext('time', 'startTime');
                if (start && !isNaN(parseInt(start))) {
                    return parseInt(start);
                } else {
                    return null;
                }
            };

            /**
             * Set OMC global context of end time.
             * 
             * @param {Number} endTime End time
             * @param {String} source Source name to tell where the API is called
             * @returns 
             */
            self.setEndTime = function (endTime, source) {
                if (self.getEndTime() !== parseInt(endTime)) {
                    setIndividualContext('time', 'endTime', parseInt(endTime), true, true, false, source);
                }
            };

            /**
             * Get OMC global context of end time.
             * 
             * @param 
             * @returns {Number} OMC global context of end time
             */
            self.getEndTime = function () {
                var end = getIndividualContext('time', 'endTime');
                if (end && !isNaN(parseInt(end))) {
                    return parseInt(end);
                } else {
                    return null;
                }
            };

            /**
             * Set OMC global context of time period.
             * 
             * @param {String} timePeriod Time period like 'Last 1 Week' etc.
             * @param {String} source Source name to tell where the API is called
             * @returns 
             */
            self.setTimePeriod = function (timePeriod, source) {
                setIndividualContext('time', 'startTime', null, false, false);
                setIndividualContext('time', 'endTime', null, false, false);
                setIndividualContext('time', 'timePeriod', timePeriod, true, true, false, source);
            };

            /**
             * Set OMC global context of start and end time.
             * 
             * @param {Number} start Start time.
             * @param {Number} end End time.
             * @param {String} source Source name to tell where the API is called
             * @returns 
             */
            self.setStartAndEndTime = function (start, end, source) {
                var prevStartTime = self.getStartTime();
                var prevEndTime = self.getEndTime();
                if (prevStartTime !== start || prevEndTime !== end) {
                    setIndividualContext('time', 'timePeriod', 'CUSTOM', false, false);
                    setIndividualContext('time', 'startTime', parseInt(start), false, false);
                    setIndividualContext('time', 'endTime', parseInt(end), false, false);
                    if (isGlobalContextRespected()) {
                        updateCurrentURL();
                        fireOMCContextChangeEvent('startEndTime',
                            {'startTime': prevStartTime, 'endTime': prevEndTime},
                        {'startTime': start, 'endTime': end}, null, source);
                    }
                }
            };

            /**
             * Evaluate start and end time.
             * If both start and end are avail in global context, return them directly.
             * If one of start and end time is not avail in global context and non-custom time period is in global context, evaluate them from time period and return.
             * If no time context in global context, return null.
             * 
             * @returns {start: <start timestamp in Number>, end: <end timestamp in Number>} or null
             */
            self.evaluateStartEndTime = function () {
                var start = self.getStartTime();
                var end = self.getEndTime();
                var timePeriod = self.getTimePeriod();
                if (start && end) {
                    return {
                        start: start,
                        end: end
                    };
                } else if (timePeriod) {
                    var timeRange = self.getStartEndTimeFromTimePeriod(timePeriod);
                    if (timeRange) {
                        return {
                            start: timeRange.start.getTime(),
                            end: timeRange.end.getTime()
                        };
                    }
                    return timeRange;
                } else {
                    return null;
                }
            };

            /**
             * 
             * @param {type} timePeriod
             * @returns {unresolved} The result is "LAST_X_UNIT".
             */
            self.formalizeTimePeriod = function (timePeriod) {
                if (!timePeriod) {
                    return null;
                }
                var tp = timePeriod.toUpperCase();
                if (tp.slice(-1) === "S") {
                    tp = tp.slice(0, -1);
                }
                var arr = tp.split(" ");
                tp = arr.join("_");
                return tp;
            };

            self.isValidTimePeriod = function (timePeriod) {
                var tpPattern = new RegExp("^LAST_[1-9]{1}[0-9]*_(SECOND|MINUTE|HOUR|DAY|WEEK|MONTH|YEAR){1}$");
                return tpPattern.test(timePeriod);
            };

            /**
             * Generate "LAST_X_UNIT" from time unit and time duration
             * 
             * @param {type} unit - OMC context time unit CONSTANT
             * @param {type} duration - number larger than 0
             * @returns {String} - formalized time period like "LAST_X_UNIT"
             */
            self.generateTimePeriodFromUnitAndDuration = function (unit, duration) {
                if (!self.OMCTimeConstants.TIME_UNIT[unit]) {
                    console.log("Invalid unit: " + unit + " to generateTimePeriodFromUnitAndDuration");
                    return null;
                }
                if (duration <= 0 || isNaN(parseInt(duration))) {
                    console.log("Invalid duration: " + duration + " to generateTimePeriodFromUnitAndDuration");
                    return null;
                }
                var arr = [];
                arr[0] = "LAST";
                arr[1] = parseInt(duration);
                arr[2] = self.OMCTimeConstants.TIME_UNIT[unit];
                return arr.join("_");
            };

            /**
             * Parse user specified time period or OMC context time period to unit and duration
             * 
             * @param {type} timePeriod
             * @returns 
             */
            self.parseTimePeriodToUnitAndDuration = function (timePeriod) {
                var tp = null;
                if (!timePeriod) {
                    tp = self.getTimePeriod();
                } else {
                    tp = timePeriod;
                }

                if (!self.isValidTimePeriod(tp)) {
                    console.log("Invalid time period: " + tp + " in parseTimePeriodToUnitAndDuration");
                    return null;
                }

                var arr = tp.split("_");
                return {
                    unit: arr[2],
                    duration: parseInt(arr[1])
                }
            };

            /**
             * 
             * @param {type} timePeriod
             * @returns {start: <start time>, end: <end time>}
             */
            self.getStartEndTimeFromTimePeriod = function (timePeriod) {
                console.log("Calling getStartEndTimeFromTimePeriod to get start and end time. The timePeriod is " + timePeriod);
                timePeriod = self.formalizeTimePeriod(timePeriod);
                console.log("The fomalized time period is " + timePeriod);

                if (!timePeriod) {
                    return null;
                }

                var start = null;
//                var end = new Date(2016, 2, 13, 3, 0, 0, 0); //For DST testing
                var end = new Date();
                var arr = null;
                var num = null;
                var opt = null;
                if (timePeriod === "LATEST") {
                    return {
                        start: end,
                        end: end
                    };
                } else if (self.isValidTimePeriod(timePeriod)) {
                    arr = timePeriod.split("_");
                    num = arr[1];
                    opt = arr[2];
                    switch (opt) {
                        case self.OMCTimeConstants.TIME_UNIT.SECOND:
                            start = new Date(end - num * 1000);
                            break;
                        case self.OMCTimeConstants.TIME_UNIT.MINUTE:
                            start = new Date(end - num * 60 * 1000);
                            break;
                        case self.OMCTimeConstants.TIME_UNIT.HOUR:
                            start = new Date(end - num * 60 * 60 * 1000);
                            break;
                        case self.OMCTimeConstants.TIME_UNIT.DAY:
                            start = new Date(end.getFullYear(), end.getMonth(), end.getDate() - num, end.getHours(), end.getMinutes(), end.getSeconds(), end.getMilliseconds());
                            break;
                        case self.OMCTimeConstants.TIME_UNIT.WEEK:
                            start = new Date(end.getFullYear(), end.getMonth(), end.getDate() - 7 * num, end.getHours(), end.getMinutes(), end.getSeconds(), end.getMilliseconds());
                            break;
                        case self.OMCTimeConstants.TIME_UNIT.MONTH:
                            var startMonthLastDate = new Date(end.getFullYear(), end.getMonth() - num + 1, 0).getDate();
                            if (startMonthLastDate >= end.getDate()) {
                                start = new Date(end.getFullYear(), end.getMonth() - num, end.getDate(), end.getHours(), end.getMinutes(), end.getSeconds(), end.getMilliseconds());
                            } else {
                                start = new Date(end.getFullYear(), end.getMonth() - num, startMonthLastDate, end.getHours(), end.getMinutes(), end.getSeconds(), end.getMilliseconds());
                            }
                            break;
                        case self.OMCTimeConstants.TIME_UNIT.YEAR:
                            var startMonthLastDate = new Date(end.getFullYear() - num, end.getMonth() + 1, 0).getDate();
                            if (startMonthLastDate >= end.getDate()) {
                                start = new Date(end.getFullYear() - num, end.getMonth(), end.getDate(), end.getHours(), end.getMinutes(), end.getSeconds(), end.getMilliseconds());
                            } else {
                                start = new Date(end.getFullYear() - num, end.getMonth(), startMonthLastDate, end.getHours(), end.getMinutes(), end.getSeconds(), end.getMilliseconds());
                            }
                            break;
                        default:
                            throw new Error("Error in getStartEndTimeFromTimePeriod function: timePeriod - " + opt + " is invalid");
                    }
                    console.log("Start and end time for '" + timePeriod + "' are start: " + start + ", end: " + end);
                    return {
                        start: start,
                        end: end
                    };
                } else {
                    return null;
                }
            };

            /**
             * Get OMC global context of time period.
             * 
             * @param 
             * @returns {String} OMC global context of time period
             */
            self.getTimePeriod = function () {
                return getIndividualContext('time', 'timePeriod');
            };

            /**
             * Get OMC global context of time period duration
             * 
             * @returns time period duration number or null
             */
            self.getTimePeriodDuration = function () {
                var tp = self.parseTimePeriodToUnitAndDuration();
                if (tp) {
                    return tp.duration;
                } else {
                    return null;
                }
            };

            /**
             * Set OMC context of time period duration
             * 
             * @param {type} duration
             * @param {String} source Source name to tell where the API is called
             * @returns {undefined}
             */
            self.setTimePeriodDuration = function (duration, source) {
                var tp = self.parseTimePeriodToUnitAndDuration();
                tp && (tp = self.generateTimePeriodFromUnitAndDuration(tp.unit, parseInt(duration)));
                tp && self.setTimePeriod(tp, source);
            };

            /**
             * Get OMC global context of time period unit
             * 
             * @returns time period unit or null
             */
            self.getTimePeriodUnit = function () {
                var tp = self.parseTimePeriodToUnitAndDuration();
                if (tp) {
                    return tp.unit;
                } else {
                    return null;
                }
            };

            /**
             * Set OMC context of time period unit
             * 
             * @param {type} unit
             * @param {String} source Source name to tell where the API is called
             * @returns {undefined}
             */
            self.setTimePeriodUnit = function (unit, source) {
                var tp = self.parseTimePeriodToUnitAndDuration();
                tp && (tp = self.generateTimePeriodFromUnitAndDuration(unit, tp.duration));
                tp && self.setTimePeriod(tp, source);
            };

            /**
             * Set OMC context time period unit and duration at the same time
             * 
             * @param {type} unit
             * @param {type} duration
             * @param {String} source Source name to tell where the API is called
             * @returns {undefined}
             */
            self.setTimePeriodUnitAndDuration = function (unit, duration, source) {
                var tp = self.generateTimePeriodFromUnitAndDuration(unit, duration);
                tp && self.setTimePeriod(tp, source);
            };
            
            self.getTranslatedTimePeriod = function(timePeriodId, timePeriodsSet) {
                if(!timePeriodId) {
                    timePeriodId = self.getTimePeriod();
                }
                if(!timePeriodId) {
                    console.error("Error in getTranslatedTimePeriod. Invalid time peroid id");
                    return;
                }
                var quickPicks = self.OMCTimeConstants.QUICK_PICK;
                var timeUnits = self.OMCTimeConstants.TIME_UNIT;
                
                if(timePeriodId === quickPicks.CUSTOM) {
                    return nls.DATETIME_PICKER_TIME_PERIOD_OPTION_CUSTOM;
                }
                
                if(timePeriodId === quickPicks.LATEST) {
                    return nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LATEST;
                }
                
                if(!self.isValidTimePeriod(timePeriodId)) {
                    console.error("Error in getTranslatedTimePeriod. Invalid time peroid id: " + timePeriodId);
                    return;
                }
                
                if(timePeriodsSet === self.OMCTimeConstants.timePeriodsSet.SHORT_TERM) {
                    if(timePeriodId === quickPicks.LAST_15_MINUTE) {
                        return nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_15_MINS;
                    }
                    if(timePeriodId === quickPicks.LAST_30_MINUTE) {
                        return nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_30_MINS;
                    }
                    if(timePeriodId === quickPicks.LAST_60_MINUTE) {
                        return nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_60_MINS;
                    }
                }
                
                var parsedTp = self.parseTimePeriodToUnitAndDuration(timePeriodId);
                return self.getTranslatedFlexTimePeriod(parsedTp.duration, parsedTp.unit);
            }
            
            self.getTranslatedFlexTimePeriod = function(num, opt) {
                var optLabel;
                switch(opt) {
                    case self.OMCTimeConstants.TIME_UNIT.SECOND:
                        if(num === 1) {
                            optLabel = nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_SECOND;
                        }else {
                            optLabel = nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_SECONDS;
                        }                            
                        break;
                    case self.OMCTimeConstants.TIME_UNIT.MINUTE:
                        if(num === 1) {
                            optLabel = nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_MINUTE;
                        }else {
                            optLabel = nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_MINUTES;
                        }                            
                        break;
                    case self.OMCTimeConstants.TIME_UNIT.HOUR:
                        if(num === 1) {
                            optLabel = nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_HOUR;
                        }else {
                            optLabel = nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_HOURS;
                        }                            
                        break;
                    case self.OMCTimeConstants.TIME_UNIT.DAY:
                        if(num === 1) {
                            optLabel = nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_DAY;
                        }else {
                            optLabel = nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_DAYS;
                        }                            
                        break;
                    case self.OMCTimeConstants.TIME_UNIT.WEEK:
                        if(num === 1) {
                            optLabel = nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_WEEK;
                        }else {
                            optLabel = nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_WEEKS;
                        }                            
                        break;
                    case self.OMCTimeConstants.TIME_UNIT.MONTH:
                        if(num === 1) {
                            optLabel = nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_MONTH;
                        }else {
                            optLabel = nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_MONTHS;
                        }                            
                        break;
                    case self.OMCTimeConstants.TIME_UNIT.YEAR:
                        if(num === 1) {
                            optLabel = nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_YEAR;
                        }else {
                            optLabel = nls.DATETIME_PICKER_FLEX_REL_TIME_OPTION_YEARS;
                        }                            
                        break;
                    default:
                        throw new Error("error in getting flexible relative time period: flexible relative time option-" + opt + " is invalid.");
                }
                    
                return nls.DATETIME_PICKER_FLEX_REL_TIME_LAST + " " + num + " " + optLabel;
            }

            /**
             * Set OMC global context of composite guid.
             * 
             * @param {String} compositeMEID Composite GUID
             * @param {String} source Source name to tell where the API is called
             * @returns 
             */
            self.setCompositeMeId = function (compositeMEID, source) {
                if (compositeMEID !== self.getCompositeMeId()) {
                    var omcContext = self.getOMCContext();
                    if (compositeMEID) {
                        compositeMEID = decodeURIComponent(compositeMEID);
                    }
                    if (compositeMEID !== self.getCompositeMeId()) {
                        omcContext.previousCompositeMeId = self.getCompositeMeId();
                    }

                    storeContext(omcContext);

                    //Set composite meId will reset composite type/name, 
                    //next time you get the composite type/name will return the new type/name
                    setIndividualContext('composite', 'compositeType', null, false, false);
                    setIndividualContext('composite', 'compositeName', null, false, false);
                    setIndividualContext('composite', 'compositeDisplayName', null, false, false);
                    setIndividualContext('composite', 'compositeEntity', null, false, false);
                    setIndividualContext('composite', 'compositeNeedRefresh', true, false, false);
                    setIndividualContext('composite', 'compositeMEID', compositeMEID, true, false, false, source);
                }
            };

            /**
             * Get OMC global context of composite guid.
             * 
             * @param 
             * @returns {String} OMC global context of composite guid
             */
            self.getCompositeMeId = function () {
                return getIndividualContext('composite', 'compositeMEID');
            };
            
            function restoreCompositeCtxFromSessionCache(cache) {
                setIndividualContext('composite', 'compositeDisplayName', cache['compositeDisplayName'], false, false);
                setIndividualContext('composite', 'compositeName', cache['compositeName'], false, false);
                setIndividualContext('composite', 'compositeType', cache['compositeType'], false, false);
                setIndividualContext('composite', 'compositeClass', cache['compositeClass'], false, false);
                setIndividualContext('composite', 'compositeNeedRefresh', 'false', false, true);
            }

            self.getCompositeEntity = function () {
                var compositeEntity = getIndividualContext('composite', 'compositeEntity');
                if (compositeEntity) {
                    return compositeEntity;
                }
                var entity = new EntityObject();
                entity['meId'] = self.getCompositeMeId();
                entity['displayName'] = self.getCompositeDisplayName();
                entity['entityName'] = self.getCompositeName();
                entity['entityType'] = self.getCompositeType();
                entity['meClass'] = self.getCompositeClass();
                compositeEntity = entity;

                //Cache the entities data
                var omcCtx = self.getOMCContext();
                if (!omcCtx['composite']) {
                    omcCtx['composite'] = {};
                }
                omcCtx['composite']['compositeEntity'] = compositeEntity;
                storeContext(omcCtx);

                return compositeEntity;
            };

//            /**
//             * Set OMC global context of composite type.
//             * 
//             * @param {String} compositeType Composite type
//             * @returns 
//             */
//            self.setCompositeType = function(compositeType) {
//                setIndividualContext('composite', 'compositeType', compositeType);
//            };

            /**
             * Get OMC global context of composite type.
             * 
             * @param 
             * @returns {String} OMC global context of composite type
             */
            self.getCompositeType = function () {
                var compositeType = getIndividualContext('composite', 'compositeType');
                if (compositeType) {
                    return compositeType;
                }
                else if (self.getCompositeMeId() && getIndividualContext('composite', 'compositeNeedRefresh') !== 'false') {
                    //sessionStorage cache
                    var compositeCacheKey = self.getCompositeMeId();
                    var cache = sessionCaches[0].retrieveDataFromCache(compositeCacheKey);
                    if (cache && cache['compositeType']) {
                        restoreCompositeCtxFromSessionCache(cache);
                        return cache['compositeType'];
                    }
                    //Fetch composite name/type
                    queryODSEntitiesByMeIds([self.getCompositeMeId()], fetchCompositeCallback);
                }
                return getIndividualContext('composite', 'compositeType');
            };

//            /**
//             * Set OMC global context of composite name.
//             * 
//             * @param {String} compositeName Composite name
//             * @returns 
//             */
//            self.setCompositeName = function(compositeName) {
//                setIndividualContext('composite', 'compositeName', compositeName);
//            };

            /**
             * Get OMC global context of composite internal name.
             * 
             * @param 
             * @returns {String} OMC global context of composite internal name
             */
            self.getCompositeName = function () {
                var compositeName = getIndividualContext('composite', 'compositeName');
                if (compositeName) {
                    return compositeName;
                }
                else if (self.getCompositeMeId() && getIndividualContext('composite', 'compositeNeedRefresh') !== 'false') {
                    //sessionStorage cache
                    var compositeCacheKey = self.getCompositeMeId();
                    var cache = sessionCaches[0].retrieveDataFromCache(compositeCacheKey);
                    if (cache && cache['compositeName']) {
                        restoreCompositeCtxFromSessionCache(cache);
                        return cache['compositeName'];
                    }
                    //Fetch composite name/type
                    queryODSEntitiesByMeIds([self.getCompositeMeId()], fetchCompositeCallback);
                }
                return getIndividualContext('composite', 'compositeName');
            };

            /**
             * Get OMC global context of composite display name.
             * 
             * @param 
             * @returns {String} OMC global context of composite display name
             */
            self.getCompositeDisplayName = function () {
                var compositeDisplayName = getIndividualContext('composite', 'compositeDisplayName');
                if (compositeDisplayName) {
                    return compositeDisplayName;
                }
                else if (self.getCompositeMeId() && getIndividualContext('composite', 'compositeNeedRefresh') !== 'false') {
                    //sessionStorage cache
                    var compositeCacheKey = self.getCompositeMeId();
                    var cache = sessionCaches[0].retrieveDataFromCache(compositeCacheKey);
                    if (cache && cache['compositeDisplayName']) {
                        restoreCompositeCtxFromSessionCache(cache);
                        return cache['compositeDisplayName'];
                    }
                    //Fetch composite name/type
                    queryODSEntitiesByMeIds([self.getCompositeMeId()], fetchCompositeCallback);
                }
                //In case composite type+name are passed in from URL, return composite name for now
                //Need to enhance the logic to query composite by type+name in next steps
                else if (self.getCompositeType() && self.getCompositeName()) {
                    return self.getCompositeName();
                }
                return getIndividualContext('composite', 'compositeDisplayName');
            };

            /**
             * Get composite class.
             * 
             * @param 
             * @returns {String} Composite class
             */
            self.getCompositeClass = function () {
                var compositeClass = getIndividualContext('composite', 'compositeClass');
                if (compositeClass) {
                    return compositeClass;
                }
                else if (self.getCompositeMeId() && getIndividualContext('composite', 'compositeNeedRefresh') !== 'false') {
                    //sessionStorage cache
                    var compositeCacheKey = self.getCompositeMeId();
                    var cache = sessionCaches[0].retrieveDataFromCache(compositeCacheKey);
                    if (cache && cache['compositeClass']) {
                        restoreCompositeCtxFromSessionCache(cache);
                        return cache['compositeClass'];
                    }
                    //Fetch composite name/type
                    queryODSEntitiesByMeIds([self.getCompositeMeId()], fetchCompositeCallback);
                }
                return getIndividualContext('composite', 'compositeClass');
            };

//            /**
//             * Set OMC global context of entity guid.
//             * 
//             * @param {String} entityMEID Entity GUID
//             * @returns 
//             */
//            self.setEntityMeId = function(entityMEID) {
//                setIndividualContext('entity', 'entityMEID', entityMEID);
//            };
//            
//            /**
//             * Get OMC global context of entity guid.
//             * 
//             * @param 
//             * @returns {String} OMC global context of entity guid
//             */
//            self.getEntityMeId = function() {
//                return getIndividualContext('entity', 'entityMEID');
//            };

            /**
             * Set OMC global context of multiple entity GUIDs.
             * 
             * @param {Array} entityMEIDs A list of Entity GUIDs
             * @param {String} source Source name to tell where the API is called
             * @returns 
             */
            self.setEntityMeIds = function (entityMEIDs, source) {
//                var omcContext = self.getOMCContext();
//                var ids = self.getEntityMeIds();
//                omcContext.previousEntityMeIds = ids ? ids : [];

                var meIds = null;
                var _meIds = null;

                //If it's an array, convert to a comma separated string
                if ($.isArray(entityMEIDs)) {
                    //Filter out duplicated IDs
                    entityMEIDs = entityMEIDs.filter(function(elem, index, self) {
                        return index === self.indexOf(elem);
                    });
                }
                //If it's a string
                else if (entityMEIDs) {
                    //Filter out duplicated IDs
                    entityMEIDs = entityMEIDs.split(',').filter(function(elem, index, self) {
                        return index === self.indexOf(elem);
                    });
                }
                if (entityMEIDs) {
                    meIds = entityMEIDs.join();
                    _meIds = entityMEIDs.sort().join();
                }
                var currentEntityIds = self.getEntityMeIds() ? self.getEntityMeIds().sort().join() : null;
                if (_meIds !== currentEntityIds) {
                    var omcContext = self.getOMCContext();
                    //Fix EMCPDF-4727: if no composite in GC and entity meids are changed, update previousCompositeMeId
                    if(!self.getCompositeMeId()) {
                        omcContext.previousCompositeMeId = currentEntityIds;
                    }
                    console.log("****************** updating entity ids");
                    setIndividualContext('entity', 'entityMEIDs', meIds, true, true, false, source);
                    //Set entity meIds will reset the cached entity objects, 
                    //next time you get the entities will return the new ones
                    setIndividualContext('entity', 'entities', null, false, false);
                }
            };

            /**
             * Get OMC global context of entity MEIDs.
             * 
             * @param 
             * @returns {Array} OMC global context of entity MEIDs
             */
            self.getEntityMeIds = function () {
                var entityMEIDs = getIndividualContext('entity', 'entityMEIDs');
                if ($.isArray(entityMEIDs)) {
                    //Filter out duplicated IDs
                    return entityMEIDs.filter(function(elem, index, self) {
                        return index === self.indexOf(elem);
                    });
                }
                else if (entityMEIDs) {
                    //Convert to a array and filter out duplicated IDs
                    return entityMEIDs.split(',').filter(function(elem, index, self) {
                        return index === self.indexOf(elem);
                    });
                }
                return null;
            };

            /**
             * Set Non-Removable EntityMeId. GC Emctas components like Topology and ContextSelector
             * will prevent deletion of this entity.
             *
             * @param {String} entityMEID entity meid
             * @param {String} source Source name to tell where the API is called
             * @returns
             */
            self.setNonRemovableEntityMeId = function (entityMEID, source) {
                if (self.getNonRemovableEntity() !== entityMEID) {
                    setIndividualContext('entity', 'nonRemovableEntityMEID', entityMEID, false, false, false, source);
                }
            };

            /**
             * Get Non-Removable Entity. GC Emctas components like Topology and ContextSelector
             * will prevent deletion of this entity.
             *
             * @param
             * @returns {Object} an Entity object
             */
            self.getNonRemovableEntity = function () {
                var entityMEID = getIndividualContext('entity', 'nonRemovableEntityMEID');
                if (entityMEID) {
                    var entities = self.getEntities();
                    for (var i = 0; i < entities.length; i++) {
                        var entity = entities[i];
                        if (entity.meId === entityMEID) {
                            return entity;
                        }
                    }
                    ;
                }
                return null;
            };

            /**
             * Set OMC global context of entities type.
             * 
             * @param {String} entitiesType Entities type
             * @param {String} source Source name to tell where the API is called
             * @returns 
             */
            self.setEntitiesType = function (entitiesType, source) {
                if (self.getEntitiesType() !== entitiesType) {
                    setIndividualContext('entity', 'entitiesType', entitiesType, true, true, false, source);
                }
            };

            /**
             * Get OMC global context of entities type.
             * 
             * @param 
             * @returns {String} OMC global context of entities type
             */
            self.getEntitiesType = function () {
                return getIndividualContext('entity', 'entitiesType');
            };

//            /**
//             * Set OMC global context of entity name.
//             * 
//             * @param {String} entityName Entity name
//             * @returns 
//             */
//            self.setEntityName = function(entityName) {
//                setIndividualContext('entity', 'entityName', entityName);
//            };

//            /**
//             * Get OMC global context of entity name.
//             * 
//             * @param 
//             * @returns {String} OMC global context of entity name
//             */
//            self.getEntityName = function() {
//                return getIndividualContext('entity', 'entityName');
//            };

            /**
             * Clear OMC global composite context.
             * 
             * @param {String} source Source name to tell where the API is called
             * @returns 
             */
            self.clearCompositeContext = function (source) {
                var compositeCacheKey = self.getCompositeMeId();
                if (compositeCacheKey) {
                    sessionCaches[0].updateCacheData(compositeCacheKey, 'compositeDisplayName', null);
                    sessionCaches[0].updateCacheData(compositeCacheKey, 'compositeName', null);
                    sessionCaches[0].updateCacheData(compositeCacheKey, 'compositeType', null);
                    sessionCaches[0].updateCacheData(compositeCacheKey, 'compositeClass', null);
                    clearIndividualContext('composite', source);
                }
            };

            /**
             * Clear OMC global time context.
             * 
             * @param {String} source Source name to tell where the API is called
             * @returns 
             */
            self.clearTimeContext = function (source) {
                clearIndividualContext('time', source);
            };

            /**
             * Clear OMC global entity context.
             * 
             * @param {String} source Source name to tell where the API is called
             * @returns 
             */
            self.clearEntityContext = function (source) {
                var entityCacheKey = getEntityCacheKey();
                if (entityCacheKey) {
                    sessionCaches[1].updateCacheData(entityCacheKey, 'entities', null);
                }
                clearIndividualContext('entity', source);
            };

            /**
             * Get a list of entity objects by entity MEIDs.
             * 
             * @param 
             * @returns {Object} a list of entity objects
             */
            self.getEntities = function () {
                var entities = getIndividualContext('entity', 'entities');
                if (entities && $.isArray(entities) && entities.length > 0) {
                    return entities;
                }
                else {
                    var entityMeIds = self.getEntityMeIds();
                    var entitiesType = self.getEntitiesType();
                    //sessionStorage cache
                    var entityCacheKey = getEntityCacheKey();
                    var cache = sessionCaches[1].retrieveDataFromCache(entityCacheKey);
                    if (cache) {
                        entities = cache['entities'];
                    }

                    if (!entities) {
                        entities = [];
                        if (entityMeIds && entityMeIds.length > 0 && entitiesType) {
                            //Query entities by meIds and filter by entites type
                            queryODSEntitiesByMeIds(entityMeIds, loadEntities);
                            for (var i = 0; i < entitiesFetched.length; i++) {
                                var entity = entitiesFetched[i];
                                //Do not filter by entitiesType for now, will add support once we have clear requirement for it
//                                if (entity['entityType'] === entitiesType) {
                                    entities.push(entity);
//                                }
                            }
                        }
                        else if (entityMeIds && entityMeIds.length > 0) {
                            //Query entities by meIds
                            queryODSEntitiesByMeIds(entityMeIds, loadEntities);
                            for (var i = 0; i < entitiesFetched.length; i++) {
                                entities.push(entitiesFetched[i]);
                            }
                        }
                        //Do not query entities by entitiesType for now, will add support once we have clear requirement for it
//                        else if (entitiesType) {
//                            //Query by entities type
//                            queryODSEntitiesByEntityType(entitiesType, loadEntities);
//                            for (var i = 0; i < entitiesFetched.length; i++) {
//                                entities.push(entitiesFetched[i]);
//                            }
//                        }
                        // update sessionStorage cache
                        sessionCaches[1].updateCacheData(entityCacheKey, 'entities', entities);
                    }
                    //Cache the entities data
                    var omcCtx = self.getOMCContext();
                    if (!omcCtx['entity']) {
                        omcCtx['entity'] = {};
                    }
                    omcCtx['entity']['entities'] = entities;
                    storeContext(omcCtx);
                    return entities;
                }
            };

            /**
             * set topologyParams in omcContext
             * @param {type} topologyParams
             * @param {String} source Source name to tell where the API is called
             * @returns {undefined}
             */
            self.setTopologyParams = function (topologyParams, source) {
                setIndividualContext('topology', 'topologyParams', topologyParams, null, null, true, source);
                // 
                // it is possible that the brandingbar has not been instantiated yet, 
                // during brandingbar instantiation, topologyParams will be cleared from global context, 
                // so it is necessary to reset it after brandingbar is instantiated
                //
                afterBrandingBarInstantiated(function () {
                    setIndividualContext('topology', 'topologyParams', topologyParams, null, null, true, source);
                });
            };
            /**
             * get topologyParams
             * @returns {String}
             */
            self.getTopologyParams = function () {
                return getIndividualContext('topology', 'topologyParams');
            };
            /**
             * 
             * @returns {undefined}
             */
            self.clearTopologyParams = function () {
                var omcContext = self.getOMCContext();
                if (omcContext['topology']) {
                    delete omcContext['topology'];
                    storeContext(omcContext);
                }
            };

            /**
             * Add event change listener for global context change
             *
             * @param {Function} callback Callback function for the listener
             *
             * @returns
             */
            self.subscribeOMCContextChangeEvent = function (callback) {
                function onCtxChange(event) {
                    if (event.origin !== window.location.protocol + '//' + window.location.host) {
                        return;
                    }
                    var data = event.data;
                    //Only handle received message for global context change
                    if (data && data.tag && data.tag === 'EMAAS_OMC_GLOBAL_CONTEXT_UPDATED') {
                        if ($.isFunction(callback)) {
                            callback(data);
                        }
                    }
                }
                ;
                window.addEventListener("message", onCtxChange, false);
            };

            /**
             * Add event change listener for topology status change when it's opened or closed
             *
             * @param {Function} callback Callback function for the listener
             *
             * @returns
             */
            self.subscribeTopologyStatusChangeEvent = function (callback) {
                function onTopologyStatusChange(event) {
                    if (event.origin !== window.location.protocol + '//' + window.location.host) {
                        return;
                    }
                    var data = event.data;
                    //Only handle received message for topology status change when it's opened or closed
                    if (data && data.tag && data.tag === 'EMAAS_OMC_TOPOLOGY_STATUS_UPDATED') {
                        if ($.isFunction(callback)) {
                            callback(data);
                        }
                    }
                }
                ;
                window.addEventListener("message", onTopologyStatusChange, false);
                //return reference to event listener so integrator can dispose it
                //when not needed
                return onTopologyStatusChange;
            };

            function getEntityCacheKey() {
                var entityCacheKey = null;
                var entityMeIds = self.getEntityMeIds();
//                var entitiesType = self.getEntitiesType();
//                if (entityMeIds && entityMeIds.length > 0 && entitiesType) {
//                    entityCacheKey = entityMeIds.sort().join() + entitiesType;
//                }
//                else 
                if (entityMeIds && entityMeIds.length > 0) {
                    entityCacheKey = entityMeIds.sort().join();
                }
//                else if (entitiesType) {
//                    entityCacheKey = entitiesType;
//                }
                return entityCacheKey;
            }

            function afterBrandingBarInstantiated(callback) {
                function receiveMessage(event) {
                    if (event.origin !== window.location.protocol + '//' + window.location.host) {
                        return;
                    }
                    var data = event.data;
                    if (data && data.tag && data.tag === 'EMAAS_BRANDINGBAR_INSTANTIATED') {
                        if (callback) {
                            callback();
                        }
                    }
                }
                window.addEventListener("message", receiveMessage, false);
            }

            /**
             * Fire OMC change event when omc context is updated.
             * 
             * @param {String} contextName Name of the updated context
             * @param {Object or String} previousValue Value before change
             * @param {Object or String} currentValue Value after change
             * @param {boolean} ignoreContextInfo if true, don't include context info since context info may contain functions
             * @param {String} source Source name to tell where the event is fired
             * @returns 
             */
            function fireOMCContextChangeEvent(contextName, previousValue, currentValue, ignoreContextInfo, source) {
                var message = {'tag': 'EMAAS_OMC_GLOBAL_CONTEXT_UPDATED'};
                message.contextName = contextName;
                message.source = source;
                if (!ignoreContextInfo) {
                    message.previousValue = previousValue;
                    message.currentValue = currentValue;
                }
                window.postMessage(message, window.location.href);
            }

            /**
             * Clear individual OMC global context.
             * 
             * @param {String} contextName Context definition name
             * @param {String} source Source name to tell where the API is called
             * @returns 
             */
            function clearIndividualContext(contextName, source) {
                if (contextName) {
                    var omcContext = self.getOMCContext();
                    if (omcContext[contextName]) {
                        var previousValue = omcContext[contextName];
                        delete omcContext[contextName];
                        storeContext(omcContext);
                        if (isGlobalContextRespected()) {
                            updateCurrentURL();
                            fireOMCContextChangeEvent(contextName, JSON.parse(JSON.stringify(previousValue)), null, null, source);
                        }
                    }
                }
            }

            /**
             * Set individual OMC global context.
             * 
             * @param {String} contextName Context definition name
             * @param {String} paramName URL parameter name for the individual context
             * @param {String} value Context value
             * @param {Boolean} fireChangeEvent Flag to determine whether to fire change event
             * @param {Boolean} replaceState Flag to determine whether to replace history state
             * @param {Boolean} raw true if raw set the context parameter with the raw value
             * @param {String} source Source name to tell where the API is called
             * @returns 
             */
            function setIndividualContext(contextName, paramName, value, fireChangeEvent, replaceState, raw, source) {
                if (contextName && paramName) {
                    var omcContext = self.getOMCContext();
                    var previousValue = null;
                    var currentValue = value;
                    //If value is not null and not empty
                    if (value) {
                        if (!omcContext[contextName]) {
                            omcContext[contextName] = {};
                        }
                        if (omcContext[contextName][paramName]) {
                            previousValue = omcContext[contextName][paramName];
                        }
                        if (raw) {
                            omcContext[contextName][paramName] = value;
                        } else {
                            omcContext[contextName][paramName] = $.isArray(value) ? value : decodeURIComponent(value);
                        }
                    }
                    //Otherwise, if value is null or empty then clear the context
                    else if (omcContext[contextName] && omcContext[contextName][paramName]) {
                        previousValue = omcContext[contextName][paramName];
                        delete omcContext[contextName][paramName];
                    }

                    //Set new evaluated start, end time when time period is updated
                    if (paramName === 'timePeriod') {
                        //Get previous evaluated start, end time
                        var prevEvaluatedStartEndTime = getIndividualContext('time', 'evaluatedStartEndTime');
                        var curEvaluatedStartEndTime = null;
                        var timeRange = self.getStartEndTimeFromTimePeriod(value);
                        if (timeRange) {
                            curEvaluatedStartEndTime = {
                                start: timeRange.start.getTime(),
                                end: timeRange.end.getTime()
                            };
                        }
                        if (value) {
                            omcContext[contextName]['evaluatedStartEndTime'] = curEvaluatedStartEndTime;
                        }
                        previousValue = {'timePeriod': previousValue,
                            'startTime': prevEvaluatedStartEndTime ? prevEvaluatedStartEndTime.start : null,
                            'endTime': prevEvaluatedStartEndTime ? prevEvaluatedStartEndTime.end : null};
                        currentValue = {'timePeriod': value,
                            'startTime': curEvaluatedStartEndTime ? curEvaluatedStartEndTime.start : null,
                            'endTime': curEvaluatedStartEndTime ? curEvaluatedStartEndTime.end : null};
                    }
                    storeContext(omcContext);
                    if (isGlobalContextRespected()) {
                        updateCurrentURL(replaceState);
                        var ignoreContextInfo = false;
                        if (fireChangeEvent !== false) {
                            if (contextName === 'topology') {
                                ignoreContextInfo = true;
                            }
                            fireOMCContextChangeEvent(paramName, previousValue, currentValue, ignoreContextInfo, source);
                        }
                    }

                }
            }

            /**
             * Get individual OMC global context.
             * 
             * @param {String} contextName Context definition name
             * @param {String} paramName URL parameter name for the individual context
             * @returns {String} Individual OMC global context
             */
            function getIndividualContext(contextName, paramName) {
                if (contextName && paramName) {
                    var omcContext = self.getOMCContext();
                    if (omcContext[contextName] && omcContext[contextName][paramName]) {
                        return omcContext[contextName][paramName];
                    }
                }
                return null;
            }

            /**
             * Add new parameter into the URL if it doesn't exist in original URL.
             * Otherwise, update the parameter in the URL if it exists already.
             * 
             * @param {String} url Original URL
             * @param {String} paramName Parameter name
             * @param {String} paramValue Parameter value
             * @returns {String} New URL
             */
            function addOrUpdateUrlParam(url, paramName, paramValue) {
                if (paramValue === null) {
                    paramValue = '';
                }
                //Handle the case anchor section ('#') exists in the given URL 
                var anchorIdx = url.indexOf('#');
                var hash = '';
                //Retrieve hash string from the URL and append to the end of the URL after appending context string
                if (anchorIdx !== -1) {
                    hash = url.substring(anchorIdx);
                    url = url.substring(0, anchorIdx);
                }
                var pattern = new RegExp('([?&])' + paramName + '=.*?(&|$|#)(.*)', 'i');
                if (url.match(pattern)) {
                    //If parameter value is not empty, update URL parameter
                    if (paramValue) {
                        return url.replace(pattern, '$1' + paramName + "=" + paramValue + '$2$3') + hash;
                    }
                    //Otherwise, remove the parameter from URL
                    else {
                        return url.replace(pattern, '$1$3').replace(/(&|\?)$/, '') + hash;
                    }
                }

                //If value is not empty, append it to the URL
                if (paramValue) {
                    return url + (url.indexOf('?') > 0 ?
                        //Handle case that an URL ending with a question mark only
                            (url.lastIndexOf('?') === url.length - 1 ? '' : '&') : '?') + paramName + '=' + paramValue + hash;
                }
                //If value is empty, return original URL
                return url;
            }

            /**
             * Retrieve parameter value from given URL string.
             * 
             * @param {String} decodedUrl Decoded URL string
             * @param {String} paramName Parameter name
             * @returns {String} Parameter value
             */
            function retrieveParamValueFromUrl(decodedUrl, paramName) {
                if (decodedUrl && paramName) {
                    if (decodedUrl.indexOf('?') !== 0) {
                        decodedUrl = '?' + decodedUrl;
                    }
                    var regex = new RegExp("[\\?&]" + encodeURIComponent(paramName) + "=([^&#]*)"), results = regex.exec(decodedUrl);
                    try {
                        return results === null ? null : decodeURIComponent(results[1]);
                    }
                    catch (err) {
                        oj.Logger.info("Failed to retrieve value for parameter [" + paramName + "] from URL: " + decodedUrl, false);
                        return null;
                    }
                }
                return null;
            }

            var entitiesFetched = [];
            function loadEntities(data) {
                entitiesFetched = [];
                var entityMeIds = self.getEntityMeIds();
                var meIdIndex = null;
                if (!$.isArray(entityMeIds)) {
                    return;
                }
                if (data && data['rows']) {
                    var dataRows = data['rows'];
                    for (var i = 0; i < dataRows.length; i++) {
                        var entity = new EntityObject();
                        entity['meId'] = dataRows[i][0];
                        entity['displayName'] = dataRows[i][1];
                        entity['entityName'] = dataRows[i][2];
                        entity['entityType'] = dataRows[i][4];
                        entity['meClass'] = dataRows[i][5];

                        meIdIndex = entityMeIds.indexOf(entity['meId']);
                        (meIdIndex > -1) && (entitiesFetched[meIdIndex] = entity);
                    }
                }
            }

            function queryODSEntitiesByMeIds(meIds, callback) {
                if (meIds && meIds.length > 0) {
                    var jsonOdsQuery = {
                        "ast": {"query": "simple",
                            "select": [{"item": {"expr": "column", "table": "me", "column": "meId"}, "alias": "s1"},
                                {"item": {"expr": "column", "table": "me", "column": "displayName"}, "alias": "s2"},
                                {"item": {"expr": "column", "table": "me", "column": "entityName"}, "alias": "s3"},
                                {"item": {"expr": "function", "name": "NVL", "args": [{"expr": "column", "table": "tp1", "column": "typeDisplayName"},
                                            {"expr": "column", "table": "me", "column": "entityType"}]}, "alias": "s4"},
                                {"item": {"expr": "column", "table": "me", "column": "entityType"}, "alias": "s5"},
                                {"item": {"expr": "column", "table": "tp1", "column": "meClass"}, "alias": "s6"}],
                            "distinct": true,
                            "from": [{
                                    "table": "innerJoin",
                                    "lhs": {"table": "virtual", "name": "ManageableEntity", "alias": "me"},
                                    "rhs": {"table": "virtual", "name": "ManageableEntityType", "alias": "tp1"},
                                    "on": {
                                        "cond": "compare",
                                        "comparator": "EQ",
                                        "lhs": {"expr": "column", "table": "me", "column": "entityType"},
                                        "rhs": {"expr": "column", "table": "tp1", "column": "entityType"}
                                    }
                                }],
                            "where": {
                                "cond": "inExpr",
                                "not": false,
                                "lhs": {"expr": "column", "table": "me", "column": "meId"},
                                "rhs": []
                            },
                            "groupBy": null
                        }
                    };

                    for (var i = 0; i < meIds.length; i++) {
                        jsonOdsQuery['ast']['where']['rhs'][i] = {};
                        jsonOdsQuery['ast']['where']['rhs'][i]['expr'] = 'str';
                        jsonOdsQuery['ast']['where']['rhs'][i]['val'] = meIds[i];
                    }
                    oj.Logger.info("Start to get ODS entities by entity MEIDs.", false);
                    executeODSQuery(jsonOdsQuery, callback);
                }
            }

            function queryODSEntitiesByEntityType(entityType, callback) {
                if (entityType) {
                    var jsonOdsQuery = {
                        "ast": {"query": "simple",
                            "select": [{"item": {"expr": "column", "table": "me", "column": "meId"}, "alias": "s1"},
                                {"item": {"expr": "column", "table": "me", "column": "displayName"}, "alias": "s2"},
                                {"item": {"expr": "column", "table": "me", "column": "entityName"}, "alias": "s3"},
                                {"item": {"expr": "function", "name": "NVL", "args": [{"expr": "column", "table": "tp1", "column": "typeDisplayName"},
                                            {"expr": "column", "table": "me", "column": "entityType"}]}, "alias": "s4"},
                                {"item": {"expr": "column", "table": "me", "column": "entityType"}, "alias": "s5"},
                                {"item": {"expr": "column", "table": "tp1", "column": "meClass"}, "alias": "s6"}],
                            "distinct": true,
                            "from": [{
                                    "table": "innerJoin",
                                    "lhs": {"table": "virtual", "name": "Target", "alias": "me"},
                                    "rhs": {"table": "virtual", "name": "ManageableEntityType", "alias": "tp1"},
                                    "on": {
                                        "cond": "compare",
                                        "comparator": "EQ",
                                        "lhs": {"expr": "column", "table": "me", "column": "entityType"},
                                        "rhs": {"expr": "column", "table": "tp1", "column": "entityType"}
                                    }
                                }],
                            "where": {"cond": "compare", "comparator": "EQ",
                                "lhs": {"expr": "column", "table": "me", "column": "entityType"},
                                "rhs": {'expr': 'str', 'val': entityType}
                            },
                            "groupBy": null
                        }
                    };

                    oj.Logger.info("Start to get ODS entities by entity type.", false);
                    executeODSQuery(jsonOdsQuery, callback);
                }
            }

            function fetchCompositeCallback(data) {
                var compositeDisplayName = null, compositeName = null, compositeType = null, compositeClass = null;
                if (data && data['rows'] && data['rows'].length > 0) {
                    var entity = data['rows'][0];
                    compositeDisplayName = entity[1];
                    compositeName = entity[2];
                    compositeType = entity[4];
                    compositeClass = entity[5];
                }
                setIndividualContext('composite', 'compositeDisplayName', compositeDisplayName, false, false);
                setIndividualContext('composite', 'compositeName', compositeName, false, false);
                setIndividualContext('composite', 'compositeType', compositeType, false, false);
                setIndividualContext('composite', 'compositeClass', compositeClass, false, false);
                // cache
                var compositeCacheKey = self.getCompositeMeId();
                sessionCaches[0].updateCacheData(compositeCacheKey, 'compositeDisplayName', compositeDisplayName);
                sessionCaches[0].updateCacheData(compositeCacheKey, 'compositeName', compositeName);
                sessionCaches[0].updateCacheData(compositeCacheKey, 'compositeType', compositeType);
                sessionCaches[0].updateCacheData(compositeCacheKey, 'compositeClass', compositeClass);

                setIndividualContext('composite', 'compositeNeedRefresh', 'false', false, true);
            }

            function executeODSQuery(jsonOdsQuery, callback) {
                var odsQueryUrl = getODSEntityQueryUrl();
                oj.Logger.info("Start to execute ODS query by URL:" + odsQueryUrl, false);
                dfu.ajaxWithRetry(odsQueryUrl, {
                    type: 'POST',
                    async: false,
                    data: JSON.stringify(jsonOdsQuery),
                    contentType: 'application/json',
                    headers: dfu.getHeadersForService(),
                    success: function (data, textStatus, jqXHR) {
                        callback(data);
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        oj.Logger.error("ODS query failed due to error: " + textStatus);
                        callback(null);
                    }
                });
            }

            function getODSEntityQueryUrl() {
                var odsUrl = '/sso.static/datamodel-query';
                if (dfu.isDevMode()) {
                    odsUrl = dfu.getTargetModelServiceInDEVMode();
                    odsUrl = dfu.buildFullUrl(odsUrl, "query");
                }
                return odsUrl;
            }
        }
        return UIFWKContextUtil;
    }
    );

