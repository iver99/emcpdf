/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['knockout',
    'jquery',
    'ojs/ojcore',
    'dfutil',
    'uifwk/js/sdk/menu-util',
    'builder/builder.core'
], function(ko, $, oj, dfu, menuModel) {
    var menuUtil = new menuModel();

    function DashboardDataSource() {
        var self = this;
        
        if(typeof DashboardDataSource.instance === 'object') {
            return DashboardDataSource.instance;
        }
        
        self.dataSource={};
        
        self.loadDashboardUserOptionsData = function (dashboardId, successCallback, errorCallback) {
            if (!self.dataSource[dashboardId]) {
                self.dataSource[dashboardId] = {};
            }
            if (isEmptyObject(self.dataSource[dashboardId]) || !self.dataSource[dashboardId].userOptions) {
            	// emcpdf-2527
            	self.loadDashboardData(dashboardId, function() {
            	    if (!self.dataSource[dashboardId].userOptions) {
            	        self.dataSource[dashboardId].userOptions = {};
            	    }
            		successCallback && successCallback(self.dataSource[dashboardId].userOptions);
            	}, 
                function (jqXHR, textStatus, errorThrown) {
            		self.dataSource[dashboardId].hasUserOptionInDB = false;
            		errorCallback && errorCallback(jqXHR, textStatus, errorThrown);
            	});
            } else {
            	    if (!self.dataSource[dashboardId].userOptions) {
            	        self.dataSource[dashboardId].userOptions = {};
            	    }
                    successCallback && successCallback(self.dataSource[dashboardId].userOptions);
            }
        };
        
        self.saveDashboardUserOptions = function (optionsJson, successCallback, errorCallback) {
            if (!self.dataSource[optionsJson.dashboardId]) {
                self.dataSource[optionsJson.dashboardId] = {};
            }
            if (isEmptyObject(self.dataSource[optionsJson.dashboardId]) || !self.dataSource[optionsJson.dashboardId].hasUserOptionInDB) {
                Builder.saveDashboardOptions(optionsJson,
                        function (data) {
                            self.dataSource[optionsJson.dashboardId] = {
                                "userOptions": data
                            };
                            successCallback && successCallback(data);
                        },
                        function (jqXHR, textStatus, errorThrown) {
                            errorCallback && errorCallback(jqXHR, textStatus, errorThrown);
                        });
            } else {
                Builder.updateDashboardOptions(optionsJson,
                        function (data) {
                            self.dataSource[optionsJson.dashboardId] = {
                                "userOptions": data
                            };
                            successCallback && successCallback(data);
                        },
                        function (jqXHR, textStatus, errorThrown) {
                            errorCallback && errorCallback(jqXHR, textStatus, errorThrown);
                        });
            }
        };
        
    	function initializeDashboardAfterLoad(dashboardId, kodsb, dsb) {
    		if (!dashboardId || !dsb || !kodsb) {
    			return;
    		}
    		if (!self.dataSource[dashboardId]) {
                self.dataSource[dashboardId] = {};
            }
            self.dataSource[dashboardId].dashboard = kodsb;
            // ensure preference&isFavorite could always be available
            if (dsb && dsb.userOptions) {
            	self.dataSource[dashboardId].userOptions = dsb.userOptions;
            	self.dataSource[dashboardId].hasUserOptionInDB = true;
            } else {
            	self.dataSource[dashboardId].userOptions = undefined;
            	self.dataSource[dashboardId].hasUserOptionInDB = false;
            }
            
            if (dsb && dsb.isFavorite) {
            	self.dataSource[dashboardId].isFavorite = {"isFavorite": true};
            } else {
            	self.dataSource[dashboardId].isFavorite = {"isFavorite": false};
            }
            
            if (dsb && dsb.preference) {
            	self.dataSource[dashboardId].preference = dsb.preference;
            } else {
            	self.dataSource[dashboardId].preference = undefined;
            }
            
            if (dsb && dsb.selectedSsData) {
                var ssDataFormat = JSON.parse(dsb.selectedSsData);
                var _hasNoSsDataBefore = !self.dataSource.savedSearchData;
                if (_hasNoSsDataBefore) {
                    self.dataSource.savedSearchData = ssDataFormat;
                } else {
                    //for multiple tab in set
                    ssDataFormat.forEach(function (ssItem) {
                        self.dataSource.savedSearchData = updateNewestDataToObj(self.dataSource.savedSearchData, ssItem);
                    });
                }
            } else {
                //when first enter the fetch the set data will enter this
                self.dataSource.savedSearchData = undefined;
            }
            
            if (dsb.selected) {
                initializeDashboardAfterLoad(dsb.selected.id, kodsb.selected, dsb.selected);
            }
            // data copied to dataSource object, so remove original data form dashboard object
        	self.dataSource[dashboardId].dashboard.userOptions = undefined;
        	self.dataSource[dashboardId].dashboard.isFavorite = undefined;
        	self.dataSource[dashboardId].dashboard.preference = undefined;
        	self.dataSource[dashboardId].dashboard.selected = undefined;
    	}
        
        self.loadDashboardData = function (dashboardId, successCallback, errorCallback) {
            if (!self.dataSource[dashboardId]) {
                self.dataSource[dashboardId] = {};
            }
            if (isEmptyObject(self.dataSource[dashboardId]) || !self.dataSource[dashboardId].dashboard) {
                if (window._dashboardServerCache && window._dashboardServerCache.id == dashboardId) {
                    console.log('Load dashboard meta data from the injected html');
                    var kodsb = getKODashboardForUI(window._dashboardServerCache);
                    initializeDashboardAfterLoad(dashboardId, kodsb, window._dashboardServerCache);
                    window._dashboardServerCache = undefined;
                }
            }
            if (isEmptyObject(self.dataSource[dashboardId]) || !self.dataSource[dashboardId].dashboard) {
                console.log('Load dashboard meta data with an Ajax call');
                Builder.loadDashboard(dashboardId,
                        function (dsb) {
                            var kodsb = getKODashboardForUI(dsb);
                            initializeDashboardAfterLoad(dashboardId, kodsb, dsb);
                            successCallback && successCallback(kodsb);
                        },
                        errorCallback);
            } else {
                console.log('Dashboard meta data has been loaded previously');
                successCallback && successCallback(self.dataSource[dashboardId].dashboard);
            }
        };

        self.fetchSelDbdSsData = function (widgetId, successCallback, errorCallback) {
            var foundSsDataInCache = false;
            self.dataSource.savedSearchData && self.dataSource.savedSearchData.filter(function isMatched(cachedSsData) {
                if (cachedSsData.id == widgetId) {
                    foundSsDataInCache = true;
                    successCallback && successCallback(cachedSsData);
                }
            });
            if (!self.dataSource.savedSearchData || !foundSsDataInCache) {
                if(!self.dataSource.savedSearchData) {
                    self.dataSource.savedSearchData = [];
                }
                //post request to savedsearch
                var url = '/sso.static/savedsearch.search';
                if (dfu.isDevMode()) {
                    url = dfu.buildFullUrl(dfu.getDevData().ssfRestApiEndPoint, 'search');
                }
                url += '/' + widgetId;
                dfu.ajaxWithRetry(url, {
                    type: 'get',
                    dataType: "json",
                    contentType: 'application/json',
                    headers: dfu.getSavedSearchRequestHeader(),
                    success: function (data) {
                        if (successCallback) {
                            self.dataSource.savedSearchData.push(data);
                            successCallback && successCallback(data);
                        }
                    },
                    error: function (e) {
                        oj.Logger.error("Error to load savedsearch Data: " + e.responseText);
                        if (errorCallback) {
                            errorCallback(ko.mapping.fromJSON(e.responseText));
                        }
                    }
                });
            }
        };
        
        
        self.updateDashboardData = function(dashboardId,dashboard,successCallback,errorCallback){
             if(!self.dataSource[dashboardId]){
                 self.dataSource[dashboardId] = {};
             }
             Builder.updateDashboard(dashboardId,dashboard,
                function (data) {                    
                    if (data && data['name'] && data['name'] !== null)
                    {
                        data['name'] = $("<div/>").html(data['name']).text();
                    }
                    if (data && data['description'] && data['description'] !== null)
                    {
                        data['description'] = $("<div/>").html(data['description']).text();
                    }
                    var kodsb = getKODashboardForUI(data);
                    self.dataSource[dashboardId].dashboard=kodsb;
                    successCallback && successCallback(self.dataSource[dashboardId].dashboard);
               }, 
               errorCallback);          
        };
        
        self.duplicateDashboard = function(dashboard, successCallback, errorCallback) {            
            Builder.duplicateDashboard(dashboard,
                    function(data) {
                        if (!self.dataSource[ko.unwrap(data.id)]) {
                            self.dataSource[ko.unwrap(data.id)] = {};
                        }
                        var kodsb = getKODashboardForUI(data);
                        self.dataSource[ko.unwrap(data.id)].dashboard = kodsb;
                        successCallback && successCallback(self.dataSource[ko.unwrap(data.id)].dashboard);
                    },
                    function(e) {
                        errorCallback && errorCallback(e);
                    });
        };
        
        self.fetchScreenshotData = function(dashboardId, successCallback, errorCallback){
            if(!self.dataSource[dashboardId]){
                 self.dataSource[dashboardId] = {};
             }
             if (isEmptyObject(self.dataSource[dashboardId]) || !self.dataSource[dashboardId].screenshot) {
                Builder.fetchDashboardScreenshot(dashboardId,
                        function (data) {
                            self.dataSource[dashboardId].screenshot = data;
                            successCallback && successCallback(data);
                        },
                        errorCallback);
            } else {
                successCallback && successCallback(self.dataSource[dashboardId].screenshot);
            }           
        };
        
        self.checkDashboardFavorites =function(dashboardId, successCallback, errorCallback){
            if (!self.dataSource[dashboardId]) {
                self.dataSource[dashboardId] = {};
            }
            if (isEmptyObject(self.dataSource[dashboardId])) {
            	// emcpdf-2527
            	self.loadDashboardData(dashboardId, function() {
                    successCallback && successCallback(self.dataSource[dashboardId].isFavorite);
            	}, errorCallback);
            } else {
                successCallback && successCallback(self.dataSource[dashboardId].isFavorite);
            }
        };
        
        self.addDashboardToFavorites = function(dashboardId, successCallback, errorCallback) {
            if(!self.dataSource[dashboardId]) {
                self.dataSource[dashboardId] = {};
            }
            
            Builder.addDashboardToFavorites(dashboardId,
                    function(data) {
                        self.dataSource[dashboardId].isFavorite = {"isFavorite": true};
                        successCallback && successCallback(data);
                        menuUtil.fireFavoriteDsbChangedEvent(true);
                    },
                    function(jqXHR, textStatus, errorThrown) {
                        errorCallback && errorCallback(jqXHR, textStatus, errorThrown);
                    });
        };
        
        self.removeDashboardFromFavorites = function(dashboardId, successCallback, errorCallback) {
            if(!self.dataSource[dashboardId]) {
                self.dataSource[dashboardId] = {};
            }
            
            Builder.removeDashboardFromFavorites(dashboardId,
                    function(data) {
                        self.dataSource[dashboardId].isFavorite = {"isFavorite": false};
                        successCallback && successCallback(data);
                        menuUtil.fireFavoriteDsbChangedEvent(false);
                    },
                    function(jqXHR, textStatus, errorThrown) {
                        errorCallback && errorCallback(jqXHR, textStatus, errorThrown);
                    });
        };

        self.getHomeDashboardPreference = function(dashboardId, successCallback, errorCallback) {
            if (!self.dataSource[dashboardId]) {
                self.dataSource[dashboardId] = {};
            }
            if (isEmptyObject(self.dataSource[dashboardId])) {
            	// emcpdf-2527
            	self.loadDashboardData(dashboardId, function() {
                	successCallback && successCallback(self.dataSource[dashboardId].preference);
            	}, errorCallback);
            } else {
            	successCallback && successCallback(self.dataSource[dashboardId].preference);
            }
        };
                      
        function isEmptyObject(obj) {
            var index;
            for (index in obj)
                return !1;
            return !0;
        };
        
        function updateNewestDataToObj(obj,newestData) {
            var replaceData = false;
            obj&&obj.filter(function isMatched(oldData, index) {
                if (oldData.name === newestData.name) {
                    obj.splice(index, 1, newestData);
                    replaceData = true;
                }
            });
            if(!replaceData){
                obj && obj.push(newestData);
            }
            return obj;
        }
                
        //convert dashboard returned from datebase to knockout obaservable for UI use
        function getKODashboardForUI(data) {
            // If dashboad is single page app, success callback will be ignored
            if (data.type === "SINGLEPAGE") {
                try {
                    var tile = data.tiles[0];
                    var url = dfu.df_util_widget_lookup_assetRootUrl(tile["PROVIDER_NAME"], tile["PROVIDER_VERSION"], tile["PROVIDER_ASSET_ROOT"], false);

                    if (dfu.isDevMode()) {
                        url = dfu.getRelUrlFromFullUrl(url);
                    }
                    window.location = url;
                    return;
                } catch (e) {
                    oj.Logger.error(e);
                }
            }
            
            
            var mapping = {
                "tiles": {
                    "create": function (options) {
                        options.data.content && (options.data.content = Builder.decodeHtml(options.data.content));
                        return new Builder.TileItem(options.data);
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
            dsb.isDefaultTileExist = function () {
                for (var i in dsb.tiles()) {
                    if (dsb.tiles()[i].type() === "DEFAULT") {
                        return true;
                    }
                }
                return false;
            };
            return dsb;
        }
        
        DashboardDataSource.instance = self;
    }
    
    Builder.registerModule(DashboardDataSource, "DashboardDataSource");
});
