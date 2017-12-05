
define(['knockout', 'jquery', 'uifwk/@version@/js/util/ajax-util-impl'],
function(ko, $, ajaxUtilModel)
{
    var ajaxUtil = new ajaxUtilModel();

    /**
     * @preserve Copyright (c) 2015, Oracle and/or its affiliates.
     * All rights reserved.
     */

    var PreferenceUtility = function(prefRestApiUrl, requestHeader) {
        this.prefRestApiUrl = prefRestApiUrl;
        this.requestHeader = requestHeader;
    };


    PreferenceUtility.prototype.getHMItemShowPreferenceSync = function(key) {
        var dfd = $.Deferred();
        this.getHMItemShowPreference(key, function(value) {
            dfd.resolve(value);
        }, function() {
            dfd.resolve();
        });
        return dfd;
    }
    
    /**
     * Get preference value to decide show or hide an Item in HM root menu. By default, it is hidden.
     * 
     * @param {type} key
     * @param {type} callback
     * @param {type} async
     * @returns {undefined}
     */
    PreferenceUtility.prototype.getHMItemShowPreference = function(key, successCallback, errorCallback) {
        var self = this;
        if(key) {
            // window._uifwk.cachedData.preferences is changed from ko object to normal js object, 
            // and won't be a func any more. Don't need to use unnecessary ko.unwrap although it won't bring trouble
            if(window._uifwk && window._uifwk.cachedData && window._uifwk.cachedData.preferences !== undefined) {
                var preferences = window._uifwk.cachedData.preferences;
                for(var i in preferences) {
                    if(preferences[i].key === key) {
                        console.info("Getting getHMItemShowPreference from window._uifwk.cachedData.preferences. Value is: " + window._uifwk.cachedData.preferences);
                        successCallback(preferences[i].value);
                        return;
                    }
                }
            }
            
            //Didn't get preference value for desired key in cache, send request to get
            if (!window._uifwk) {
                window._uifwk = {};
            }
            if (!window._uifwk.cachedData) {
                window._uifwk.cachedData = {};
            }
            console.info("Getting preference by sending request. window._uifwk.cachedData.isFetchingPrefernce is " + window._uifwk.cachedData["isFetching"+key+"Preference"]);
            if (!window._uifwk.cachedData["isFetching"+key+"Preference"]) {
                window._uifwk.cachedData["isFetching"+key+"Preference"] = true;
                if (!window.preferenceFromRequest) {
                    console.info("initialize window.preferenceFromRequest to ko observable");
                    window.preferenceFromRequest = ko.observable();
                }
                function doneCallback(data, textStatus, jqXHR) {
                    if(window.preferenceFromRequest && $.isFunction(window.preferenceFromRequest)) {
                        console.info("window.preferenceFromRequest is ko observable");
                        window.preferenceFromRequest(data);
                    }else {
                        console.info("window.preferenceFromRequest is not ko observable");
                        window.preferenceFromRequest = ko.observable(data);
                    }
                    if(window._uifwk.cachedData.preferences && $.isArray(window._uifwk.cachedData.preferences)) {
                        window._uifwk.cachedData.preferences.push(data);
                    }else {
                        window._uifwk.cachedData.preferences = [data];
                    }
                    
                    window._uifwk.cachedData["isFetching"+key+"Preference"] = false;
                    successCallback(data.value, textStatus, jqXHR);
                }
                var url = self.prefRestApiUrl + "/" + key;
                ajaxUtil.ajaxWithRetry({type: 'GET', contentType: 'application/json', url: url,
                    dataType: 'json',
                    headers: self.requestHeader,
                    async: true,
                    success: function (data, textStatus, jqXHR) {
                        doneCallback(data, textStatus, jqXHR);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log('Failed to get HM item show preference info!');
                        window._uifwk.cachedData["isFetching"+key+"Preference"] = false;
                        if (errorCallback) {
                            errorCallback(jqXHR, textStatus, errorThrown);
                        }
                    }
                });
            } else {
                window.preferenceFromRequest.subscribe(function (data) {
                    console.info("window.preferenceFromRequest is fetched from back end");
                    if (data) {
                        successCallback(data.value);
                    }
                });
            }
        }
    };

    PreferenceUtility.prototype.getPreferenceValue = function(prefArray, key) {
        if (prefArray && key) {
            var arr;
            arr = $.grep(prefArray, function(pref) {
                if (pref !== undefined && pref['key'] === key) {
                    return true;
                }
                return false;
            });
            if (arr !== undefined && arr.length > 0) {
                return arr[0]['value'];
            }
        }
        return undefined;
    };

    PreferenceUtility.prototype.getPreference = function(key, options) {
        var self = this, _options = options || {}, _async = (_options['async'] === false) ? false : true;
        var _ajax = ajaxUtil.ajaxWithRetry({
            url: self.prefRestApiUrl + "/" + key,
            type: 'GET',
            async: _async,
            headers: self.requestHeader,//{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},
            success: function(response) {
                self._processSuccess(response, _options);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                self._processError(jqXHR, textStatus, errorThrown, _options);
            }
        });
        return _ajax;
    };

    PreferenceUtility.prototype.getAllPreferences = function(options) {
        var self = this, _options = options || {}, _async = (_options['async'] === false) ? false : true;
        var _ajax = ajaxUtil.ajaxWithRetry({
            url: self.prefRestApiUrl,
            type: 'GET',
            async: _async,
            headers: self.requestHeader,//{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},
            success: function(response) {
                self._processSuccess(response, _options);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                self._processError(jqXHR, textStatus, errorThrown, _options);
            }
        });
        return _ajax;
    };

    PreferenceUtility.prototype.removeAllPreferences = function(options) {
        var self = this, _options = options || {}, _async = (_options['async'] === false) ? false : true;
        var _ajax = ajaxUtil.ajaxWithRetry({
            url: self.prefRestApiUrl,
            type: 'DELETE',
            async: _async,
            headers: self.requestHeader,//{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},
            success: function(response) {
                self._processSuccess(response, _options);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                self._processError(jqXHR, textStatus, errorThrown, _options);
            }
        });
        return _ajax;
    };

    PreferenceUtility.prototype.removePreference = function(key, options) {
        var self = this, _options = options || {}, _async = (_options['async'] === false) ? false : true;
        var _ajax = ajaxUtil.ajaxWithRetry({
            url: self.prefRestApiUrl + "/" + key,
            type: 'DELETE',
            async: _async,
            headers: self.requestHeader,//{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},
            success: function(response) {
                self._processSuccess(response, _options);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                self._processError(jqXHR, textStatus, errorThrown, _options);
            }
        });
        return _ajax;
    };

    PreferenceUtility.prototype.setPreference = function(key, value, options) {
        var self = this, _options = options || {}, _async = (_options['async'] === false) ? false : true;
        var _ajax = ajaxUtil.ajaxWithRetry({
            url: self.prefRestApiUrl + "/" + key,
            type: 'PUT',
            async: _async,
            data: JSON.stringify({ 'value' : value}),
            contentType: "application/json",
            headers: self.requestHeader,//{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},
            success: function(response) {
                self._processSuccess(response, _options);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                self._processError(jqXHR, textStatus, errorThrown, _options);
            }
        });
        return _ajax;
    };

    PreferenceUtility.prototype._processSuccess = function(response, options)
    {
        var _options = options || {};
        if (_options['success'] && $.isFunction(_options['success']))
        {
            _options['success'](response);
        }
    };

    PreferenceUtility.prototype._processError = function(jqXHR, textStatus, errorThrown, options)
    {
        var _options = options || {};
        if (_options['error'] && $.isFunction(_options['error']))
        {
            _options['error'](jqXHR, textStatus, errorThrown);
        }
    };

    return PreferenceUtility;
});


