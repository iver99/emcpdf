/**
 * EMSaas JET based custom logger module.
 *
 * The only properties that are public are the methods in function emJETCustomLogger.
 *
 * Initialize this logger during your initialization, passing the necessary parameters.
 * Then, call the usual JET oj.logger methods.  This custom logger intercepts those logs
 * and sends them to your logger url (passed in initialization).
 */
define(['ojs/ojcore', 'uifwk/@version@/js/util/ajax-util-impl', 'uifwk/@version@/js/util/df-util-impl'],
    function(oj, ajaxUtilModel, dfumodel)
    {

        // Custom logger.
        var customFeatureUsageLogger = {};

        customFeatureUsageLogger.featureUsageLogType = {OOB_DASHBOARD: 'OOB-DBD', CUSTOM_DASHBOARD: 'CUSTOM-DBD', HAMBURGER_MENU: 'HBGMENU'};

        //
        // Methods defined on the custom logger, as required by JET's oj.logger.
        //
        
        /**
         * Collect metric: Usage of feature.
         */
        customFeatureUsageLogger.metricFeatureUsage = function (args,flush,isAsyncStatus)
        {
            var useAsyncCall = true;
            if(isAsyncStatus===false){
                useAsyncCall= false;
            }
            if (window && window.console) {
                window.console.log(args);
            }
            _cacheOrSend(args, flush, useAsyncCall);
        };

        //
        // Utility methods and variables for logging
        //

        // Logs are cached here before sending them in bulk to server.
        var logsCache = [];
        // If we have not sent logs to server for this long then send whatever is in cache.
        var logsCacheMaxInterval = 60000;
        // Frequency that we check to see if any logs are cached.
        var logsCacheFrequency = 20000;
        // Cache is limited to this size.  Once it reaches the limit, logs are sent to server.
        var logsCacheLimit = 20;
        // Last time we sent to server
        var logsCacheLastTimeWeSent = 1;
        
        var logAutoSendTimer = null;
        var logSaveOnPageLeaveIsBind = false;

        var logOwner = "UnknownTenant.UnknownUser";
        var tenantName = "UnknownTenant";
        var userName = "UnknownUser";

        var serverUrlToSendLogs = '/sso.static/dashboards.logging/feature/logs';

        var ajaxUtil = new ajaxUtilModel();
        var dfu = null;

        /**
         * Cache the log and send to server if cache limit is reached.
         */
        function _cacheOrSend(msgObj, flush ,useAsyncCall)
        {
            // TODO: Look into guarding against too many logs in a short period
            // of time.  Use case: Something bad may have happened and now we are getting
            // inundated with logs.

            // TODO: Send the cache when browser is closed, or user leaves the page.

            logsCache.push({"type": msgObj.type, "logMsg": msgObj.msg});

            // If cache is full, then send.
            if (flush || logsCache.length >= logsCacheLimit) {
                _sendToServer(useAsyncCall);
            }
        };

        /**
         * Ensure the logs get sent before too long.
         */
        function _sendBeforeTooLong()
        {
            if (logsCache.length > 0 && (new Date().getTime() - logsCacheLastTimeWeSent) > logsCacheMaxInterval) {
                _sendToServer(true);
            }
        };

        /**
         * Send the cached logs to server
         */
        function _sendToServer(useAsyncCall)
        {
            // Send the logs asynchronously and clear the cache.
            new _asyncSender(useAsyncCall)();
            logsCache = [];
            logsCacheLastTimeWeSent = new Date().getTime();
        };

        /**
         * An asynchronous sender that clones the cache and then sends the logs from the clone.
         * A new instance of this object must be created for each use.
         */
        function _asyncSender(useAsyncCall)
        {
            var logsCacheCloned = [];

            var _sendIt = function()
            {
                //TODO: Change to use callServiceManager.
                //TODO: Why not get tenantId from cookie?
                //TODO: Should global be false?
                var headers;
                
                if (dfu.isDevMode()){
                    headers = {"Authorization":"Basic " + btoa(dfu.getDevData().wlsAuth)};
                }
                ajaxUtil.ajaxWithRetry({
                    url: serverUrlToSendLogs,
                    type: "POST",
                    data: JSON.stringify({"tenantName": tenantName, "userName": userName, "logArray": logsCacheCloned}),
                    dataType: "json",
                    global: false,
                    contentType: "application/json; charset=utf-8",
                    success: function(data){
                        window.console.log("Logs sent to server");
                    },
                    error: function(jqXHR, textStatus, errorThrown){
                        window.console.log("Failed to send logs to server" +
                            "textStatus: " + textStatus +
                            "errorThrown: " + errorThrown);
                    },
                    description: "custom logger: Sending logs to server",
                    headers: headers,
                    async:useAsyncCall
                });
            };

            $.extend(true, logsCacheCloned, logsCache);

            return _sendIt;
        };

        /**
         * Format the log so time and client are identified.
         */
        function _format(args)
        {
            //TODO:  Add something to identify who is logging:  tenantID, host, IP address, what else?
            //       Some of the info may already be available from the request object on server side.
            var timestamp = new Date().toISOString();
            return timestamp + ": " + args;
        };

        customFeatureUsageLogger.initialize = function(url, maxInterval, frequency, limit, tenantUser){
            logOwner = tenantUser || null;
            if (logOwner === null){
                console.log("Error to initilize Logger with user: "+tenantUser);
            }
            serverUrlToSendLogs = url||serverUrlToSendLogs;
            userName = (logOwner === null ? null : logOwner.substring(logOwner.indexOf('.')+1));
            tenantName = (logOwner === null ? null : logOwner.substring(0, logOwner.indexOf('.')));
            dfu = new dfumodel(userName, tenantName);

            if (maxInterval !== undefined) {
                logsCacheMaxInterval = maxInterval;
            }
            if (frequency !== undefined) {
                logsCacheFrequency = frequency;
            }
            if (limit !== undefined) {
                logsCacheLimit = limit;
            }

            logsCacheLastTimeWeSent = new Date().getTime();

            // Ensure logs are sent at least in this frequency.
            if(logAutoSendTimer){
                clearInterval(logAutoSendTimer);
            }
            logAutoSendTimer = setInterval(_sendBeforeTooLong, logsCacheFrequency);

            if(!logSaveOnPageLeaveIsBind){
                // Send cached logs to server on leaving page.
                $(window).bind("beforeunload", function(){
                    if(logsCache.length > 0){
                        _sendToServer(true);
                    }
                });
                logSaveOnPageLeaveIsBind = true;
            }
        };

        return customFeatureUsageLogger;
    });
