define('uifwk/@version@/js/util/zdt-util-impl', ['knockout',
    'jquery',
    'ojs/ojcore',
    'uifwk/@version@/js/util/df-util-impl',
    'uifwk/@version@/js/util/message-util-impl', 
    'uifwk/@version@/js/util/ajax-util-impl'
],
    function(ko, $, oj, dfuModel, msgUtilModel, ajaxUtilModel)
    {
        function DashboardFrameworkZdtUtil() {
            var self = this;
            var downtimeDetectUrl = "/sso.static/dashboards.omcstatus";
            var dfu = new dfuModel();
            var ajaxUtil = new ajaxUtilModel();
            var messageUtil = new msgUtilModel();
            
            /**
             * Check if OMC is under planned downtime or not.
             * 
             * This is a sync call to check whether OMC is under planned downtime or not, 
             * if yes return true, otherwise return false. 
             * 
             * @returns {boolean}
             */ 
            self.isUnderPlannedDowntime = function() {
                var underPlannedDowntime = false;
                if (dfu.isDevMode()){
                    downtimeDetectUrl = dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint,"omcstatus");
                }
                ajaxUtil.ajaxWithRetry({
                    type: "POST",
                    data: {},
                    url: downtimeDetectUrl,
                    async: false,
                    dataType: "json",
                    contentType: "application/json; charset=utf-8",
                    retryLimit: 0
                })
                .fail(function(jqXHR, textStatus, errorThrown) {
                    var apigwHeaders = ajaxUtil.getAPIGWHeaderValues(jqXHR, 'X-ORCL-OMC-APIGW-RETRYAFTER');
                    if (jqXHR.status === 503 && apigwHeaders && apigwHeaders['msg'].toLowerCase() === 'planned downtime') {
                        underPlannedDowntime = true;
                    }
                });
                
                return underPlannedDowntime;
            };
            
            /**
             * Detect whether OMC is under planned downtime or not asynchronously.
             * 
             * This is an async call to check whether OMC is under planned downtime or not, 
             * where callback is a function with a input paramter of type boolean, true: downtime, false: not downtime.
             * 
             * @param {Function} callback
             * @returns 
             */ 
            self.detectPlannedDowntime = function(callback, doNotUseCache) {
                if (typeof(callback) !== 'function') {
                    oj.Logger.error("Error: Failed to detect OMC's planned downtime. callback should be defined as a function.");
                    return;
                }
                
                // window._uifwk.cachedData.isPlannedDowntime is changed from ko object to normal js object, and won't be a func any more
                if (!doNotUseCache && window._uifwk && window._uifwk.cachedData && window._uifwk.cachedData.isPlannedDowntime !== undefined) {
                    console.info("Getting isPlannedDowntime from window._uifwk.cachedData.isPlannedDowntime. Value is: " + window._uifwk.cachedData.isPlannedDowntime);
                    callback(window._uifwk.cachedData.isPlannedDowntime);
                }else{
                    if(!window._uifwk){
                        window._uifwk = {};
                    }
                    if(!window._uifwk.cachedData){
                        window._uifwk.cachedData = {};
                    }
                    
                    console.info("Getting isPlannedDowntime by sending request. window._uifwk.cachedData.isFetchingOMCStatus is " + window._uifwk.cachedData.isFetchingOMCStatus);
                    if (!window._uifwk.cachedData.isFetchingOMCStatus) {
                        window._uifwk.cachedData.isFetchingOMCStatus = true;
                        if (!window.isPlannedDowntimeFromRequest) {
                            console.info("initialize window.isPlannedDowntimeFromRequest to ko observable");
                            window.isPlannedDowntimeFromRequest = ko.observable();
                        }

                        if (dfu.isDevMode()){
                            downtimeDetectUrl = dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint,"omcstatus");
                        }
                        
                        function doneCallback(data) {
                            if(window.isPlannedDowntimeFromRequest && $.isFunction(window.isPlannedDowntimeFromRequest)) {
                                console.info("window.isPlannedDowntimeFromRequest is ko observable");
                                window.isPlannedDowntimeFromRequest(data);
                            }else {
                                console.info("window.isPlannedDowntimeFromRequest is not ko observable");
                                window.isPlannedDowntimeFromRequest = ko.observable(data);
                            }
                        }
                        
                        ajaxUtil.ajaxWithRetry({
                            type: "POST",
                            url: downtimeDetectUrl,
                            async: true,
                            headers: dfu.getDefaultHeader()
                        })
                        .done(function() {
                            doneCallback(false);
                            window._uifwk.cachedData.isPlannedDowntime = false;
                            window._uifwk.cachedData.isFetchingOMCStatus = false;
                            messageUtil.removeMessageByCategory("omc_planned_downtime");
                            callback(false);
                        })
                        .fail(function(jqXHR, textStatus, errorThrown) {
                            if (jqXHR.status === 401) {
                                doneCallback(false);
                                window._uifwk.cachedData.isPlannedDowntime = false;
                                window._uifwk.cachedData.isFetchingOMCStatus = false;
                                window._uifwk.hideHamburgerMenuOnPage = true;
                                oj.Logger.error("Failed to detect OMC planned downtime due to 401 error. Redirect to error page", true);
                                callback(false);
                                if (location.href && location.href.indexOf("error.html") === -1) {
                                    location.href = "/emsaasui/emcpdfui/error.html?msg=DBS_ERROR_PAGE_NOT_FOUND_MSG";
                                }
                                return;
                            }
                            var apigwHeaders = ajaxUtil.getAPIGWHeaderValues(jqXHR, 'X-ORCL-OMC-APIGW-RETRYAFTER');
                            if (jqXHR.status === 503 && apigwHeaders && apigwHeaders['msg'].toLowerCase() === 'planned downtime') {
                                doneCallback(true);
                                window._uifwk.cachedData.isPlannedDowntime = true;
                                window._uifwk.cachedData.isFetchingOMCStatus = false;
                                callback(true);
                            }
                            else {
                                doneCallback(false);
                                window._uifwk.cachedData.isPlannedDowntime = false;
                                window._uifwk.cachedData.isFetchingOMCStatus = false;
                                messageUtil.removeMessageByCategory("omc_planned_downtime");
                                callback(false);
                            }
                        });
                    } else {
                        window.isPlannedDowntimeFromRequest.subscribe(function (data) {
                            console.info("window.isPlannedDowntimeFromRequest is fetched from back end");
                            if (data) {
                                callback(data);
                            }
                            else {
                                callback(false);
                            }
                        });
                    }
                }
            };
        }
        
        return DashboardFrameworkZdtUtil;
    }
);

