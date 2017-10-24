define(['dfutil', 'ojs/ojcore', 'jquery', 'knockout', 'uifwk/js/sdk/context-util', 'uifwk/js/util/preference-util', 'ojs/ojpagingcontrol', 'ojs/ojknockout-model'],
       function(dfu, oj, $, ko, cxtModel, prefUtilModel)
{

/**
 * @preserve Copyright (c) 2015, Oracle and/or its affiliates.
 * All rights reserved.
 */


 var DashboardPaging = function(collection)
{
    this.collection = collection;
    this.current = 0;
    this.dataWindow = [];
    this.showPagingObservable = ko.observable(false);
    this.setPageSize(10); // Default the page size to 10
    this.Init();
};

// Subclass from oj.PagingDataSource
oj.Object.createSubclass(DashboardPaging, oj.DataSource, "DashboardPaging");

DashboardPaging.prototype.Init = function()
{
  DashboardPaging.superclass.Init.call(this);
};


DashboardPaging.prototype._getSize = function() {
    if (!this.hasMore()) {
        // Return a size representing only what's left if we'd go over the bounds
        return this.totalSize() - this.startIndex();
    }
    // Otherwise window size should match the page size
    return this.pageSize;
};

DashboardPaging.prototype._refreshDataWindow = function() {
    // Reinit the array
    var _isShow = this.showPagingObservable();
    if (_isShow !== true && this.pageSize < this.totalSize()) {
        this.showPagingObservable(true);
    }//show paging
    if (_isShow === true && this.pageSize >= this.totalSize()) {
        this.showPagingObservable(false);
    }//show paging

    this.dataWindow = [];
    this.dataWindow.length = this._getSize();

    var self = this;
    if (this._getSize() === 0 && this.collection.length === 0)
    {
        return self.__getPromise(function(resolve, reject) {
            self._refreshObservableDataWindow();
            resolve(self.dataWindow);
        });
    }
    return this.IterativeAt(this.startIndex(), this.startIndex() + this.dataWindow.length).then(function(array) {
        // Copy from the source data
        for (var i = 0; i < array.length; i++) {
            self.dataWindow[i] = array[i];
        }
        // Update the observable array
        self._refreshObservableDataWindow();
    });
}; 

 DashboardPaging.prototype.getCxtNavLink = function(model){
        var self = this;
        var cxtUtil = new cxtModel();
        var url = self.getNavLink(model);
        if (typeof url==="string"){ 
           return cxtUtil.appendOMCContext(url);
        }else{
           return null;
        }
};

DashboardPaging.prototype.getNavLink = function(model){
        var _id = model.get('id');
        if (!_id) {
            return null;
        }
        var _type = model.get('type');
        if ("SINGLEPAGE"===_type){
            var tiles = model.get('tiles');
            if (Array.isArray(tiles) && tiles.length===1){
                var providerName = tiles[0]["PROVIDER_NAME"];
                var version = tiles[0]["PROVIDER_VERSION"];
                var assetRoot = tiles[0]["PROVIDER_ASSET_ROOT"];
                var url = dfu.df_util_widget_lookup_assetRootUrl(providerName,version, assetRoot, false);
                if (dfu.isDevMode()){
                    url = dfu.getRelUrlFromFullUrl(url);
                }
                if (typeof url==="string"){
                    oj.Logger.info("Single page URL for dashboard ID=" + model.get("id") + " is: " + url);
                   return url;
                }else{
                   oj.Logger.error("Single Page Dashboard URL is not found by: serviceName="+providerName+", version="+version+", asset root="+assetRoot);
                   return null;
                }
            }else{
                oj.Logger.error("Invalid tiles: "+JSON.stringify(tiles));
            }
        }else{
            return document.location.protocol + '//' + document.location.host + '/emsaasui/emcpdfui/builder.html?dashboardId=' + _id;
        }
 };

DashboardPaging.prototype.IterativeAt = function (start, end) {
    var array = [], i;
    var self = this;
    var dashboardPagingObject= this;
    return self.__getPromise(function(allResolve, allReject) {
        var doTask = function(index) {
                        return self.__getPromise(function(resolve, reject) {
                            self.collection.at(index, null).then(function(model) {
                                if (model) {
                                    array.push(model);
                                }
                                if (model.isDsbAttrsHtmlDecoded !== true)
                                {
                                    var __dname = $("<div/>").html(model.get('name')).text();
                                    var _url = dashboardPagingObject.getCxtNavLink(model);
                                    model.set('name', __dname, {silent: true});
                                    model.set('enableDescription','',{silent:true});
                                    model.set('enableEntityFilter','',{silent:true});
                                    model.set('lastModifiedBy','',{silent:true});
                                    model.set('buildPageUrl',_url,{silent:true});
                                    model.set('isFavorite','',{silent:true});
                                    model.set('selectedSsData','',{silent:true});
                                    model.set('preference','',{silent:true});
                                    model.set('userOptions','',{silent:true});
                                    if (model.get('description') && model.get('description') !== null)
                                    {
                                        var __ddesc =  $("<div/>").html(model.get('description')).text();
                                        model.set('description', __ddesc, {silent: true});
                                    }
                                    model.isDsbAttrsHtmlDecoded = true;
                                }
                                resolve(index);
                            });
                        });
        };

        var currentStep = doTask(start);
        var nextTask = function(j) {
                            return doTask(j+1);
        };

        for (i = start+1; i < end; i++) {
            currentStep = currentStep.then(nextTask);
        }
        currentStep.then(function() {
                            allResolve(array);
                        });
    });
};

DashboardPaging.prototype.__getPromise = function(func) {
    if (!Promise.prototype['done']) {
        Promise.prototype['done'] = Promise.prototype['then'];
    }
    return new Promise(func);
};

DashboardPaging.prototype._refreshObservableDataWindow = function() {
    if (this.observableDataWindow !== undefined) {
        // Manage the observable window array
        this.observableDataWindow.removeAll();
        for (var i = 0; i < this.dataWindow.length; i++) {
            this.observableDataWindow.push(oj.KnockoutUtils.map(this.dataWindow[i]));
        }
    }
};




DashboardPaging.prototype.getWindow = function() {
    return this.dataWindow;
};


DashboardPaging.prototype.getWindowObservable = function() {
    if (this.observableDataWindow === undefined) {
        this.observableDataWindow = ko.observableArray();
        this._refreshObservableDataWindow();
    }
    return this.observableDataWindow;
};

DashboardPaging.prototype.getPage = function()
{
  return this._page;
};

var prefUtil = new prefUtilModel(dfu.getPreferencesUrl(), dfu.getDashboardsRequestHeader());
DashboardPaging.prototype.setPage = function(v, opts)
{
  var self = this, options = opts || {}, value = parseInt(v, 10),
          _successCallback = self.setPageCallback || options['success'], _errorCallbakc = options['error'];
  try
  {
    DashboardPaging.superclass.handleEvent.call(this, oj.PagingModel.EventType['BEFOREPAGE'], {'page' : value, 'previousPage' : this._page});
  }
  catch (err)
  {
    return Promise.reject(null);
  }
  var previousPage = this._page;
  this._page = value;
  if (this._page >= 0 && this._page === previousPage)
  {
      var _event = {data: self.dataWindow, startIndex: self.startIndex()};
      if (options['silent'] !== true)
      {
          this.handleEvent("sync", _event);
      }
      return Promise.resolve(null);
  }
  this._startIndex = value * this.pageSize;

  return new Promise(function(resolve, reject)
  {
    $("#loading").show();
    var showFederationInHM = prefUtil.getHMItemShowPreferenceSync("uifwk.hm.federation.show");
    showFederationInHM.done(function(showInUI) {
        var federationFeatureShowInUi = false;
        if (showInUI && showInUI.toUpperCase() === "TRUE") {
            federationFeatureShowInUi = true;
        }
        self.fetch({'startIndex': self._startIndex, 'silent': options['silent'], 'federationFeatureShowInUi': federationFeatureShowInUi,
            'success': function() {
                $("#loading").hide();
                DashboardPaging.superclass.handleEvent.call(self, oj.PagingModel.EventType['PAGE'], {'page' : self._page, 'previousPage' : previousPage});
                if ($.isFunction(_successCallback))
                {
                   _successCallback({'page' : self._page, 'previousPage' : previousPage});
                }
                resolve(null);
            },
            'error': function(jqXHR, textStatus, errorThrown) {
                $("#loading").hide();
                oj.Logger.error("Error when fetching data for paginge data source. " + (jqXHR ? jqXHR.responseText : ""));
                // restore old page
                self._page = previousPage;
                self._startIndex = self._page * self.pageSize;
                if ($.isFunction(_errorCallbakc))
                {
                   _successCallback(jqXHR, textStatus, errorThrown);
                }
                reject(null);
            }
        } );
    });
  });
};

DashboardPaging.prototype.getStartItemIndex = function()
{
  return this._startIndex;
};

DashboardPaging.prototype.getEndItemIndex = function()
{
  var self = this;
  return self._startIndex + self.dataWindow.length - 1;
};

DashboardPaging.prototype.getPageCount = function()
{
  var totalSize = this.totalSize();
  return totalSize === -1 ? -1 : Math.ceil(totalSize / this.pageSize);
};

DashboardPaging.prototype.refreshModel = function(modelId, options)
{
    var self = this;
    self.collection.get(modelId).then(
                function (model){
                    if (model)
                    {
                        model.fetch(options);
                    }
                });
};

DashboardPaging.prototype.create = function(attributes, options)
{
    var opts = options || {};
    var self = this, _m = null;
    try {
       if ($.isFunction(this.collection['model'])) {
            _m = new this.collection['model'](undefined, {collection: this.collection});
        }
        else {
            _m = new this.collection['model'].constructor(undefined, {collection: this.collection});
        }
        _m.save(attributes, {
                        'forceNew': true,
                        success: function (_model, _resp, _options) {
                            opts['success'](_model, _resp, _options);
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            self._processError(opts, jqXHR, textStatus, errorThrown);
                        }});
    }
    catch (e) {
        var _e = e;
        self._refreshDataWindow().then(function() {
            self._processError(opts, null, null, _e);});
    }
};

DashboardPaging.prototype.remove = function(model, options)
{
    var self = this;
    var url="/sso.static/dashboards.service/";
    if (dfu.isDevMode()){
        url=dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint,"dashboards/");
    }
    dfu.ajaxWithRetry(url + model.get('id'), {
       type: 'DELETE',
       headers: dfu.getDashboardsRequestHeader(),
       success: function(result) {
          // Do something with the result
          self.collection.remove(model);
          self._refreshDataWindow().then(function() { self._processSuccess(options); });
        },
        error: function(jqXHR, textStatus, errorThrown) {
            self._processError(options, jqXHR, textStatus, errorThrown);
        }
    });

};



DashboardPaging.prototype.fetch = function(options)
{
    if (!this.collection) {
        return;
    }
    var self = this, opts = options || {}, _forceFetch = options['forceFetch'];
    if (opts['startIndex'] !== undefined) {
         this.current = opts['startIndex'];
    }
    if (opts['pageSize'] !== undefined) {
        this.pageSize = opts['pageSize'];
    }
    var _lastFetchSize = self.collection.lastFetchSize, _offset = self.collection.offset;
    if (_forceFetch !== true && (this.current >= _offset && (this.pageSize + this.current) <= (_offset + _lastFetchSize)))
    {
        return this._refreshDataWindow().then(function() {
                              self._processSuccess(opts);
                          });
    }
    self._fetch(options);
};

DashboardPaging.prototype._fetch = function(options)
{

    var self = this, opts = options || {};
    // Call collection fetch, and refresh the window on success
    // Allow for the fact that this collection might not be backed by a rest service
    try {
        this.collection.fetch({
            success:function() {
                self._refreshDataWindow().then(function() {
                     self._processSuccess(opts);
                 });
            },
            error: function(jqXHR, textStatus, errorThrown) {
                self._processError(options, jqXHR, textStatus, errorThrown);
            }
        });
    }
    catch (e) {
        var _e = e;
        self._refreshDataWindow().then(function() {
            self._processError(opts, null, null, _e);
        });
    }

};

DashboardPaging.prototype._processError = function(opts, jqXHR, textStatus, errorThrown) {
    var self = this, options = opts || {}, _event = null;
    _event = {data: self.dataWindow, startIndex: self.startIndex()};
    if (options['silent'] !== true)
    {
        this.handleEvent("sync", _event);
    }
    if (options['error']) {
        options['error'](jqXHR, textStatus, errorThrown);
    }
};

DashboardPaging.prototype._processSuccess = function(opts, eventType, event) {
    var self = this, options = opts || {}, _event = null;
    if (!event || event === null)
    {
        _event = {data: self.dataWindow, startIndex: self.startIndex()};
    }
    if (options['silent'] !== true)
    {
        this.handleEvent(eventType || "sync", _event);
    }

    if (options['success']) {
        options['success']();
    }
};

DashboardPaging.prototype.handleEvent = function(eventType, event)
{
    DashboardPaging.superclass.handleEvent.call(this, eventType, event);
};

DashboardPaging.prototype.on = function(eventType, eventHandler)
{
    DashboardPaging.superclass.on.call(this, eventType, eventHandler);
};

DashboardPaging.prototype.hasMore = function()
{
  return this.startIndex() + this.pageSize < this.totalSize();
};


DashboardPaging.prototype.next = function()
{
    if (!this.hasMore()) {
        // At the limit
        this._processSuccess(null);
        return;
    }
    // Bump the position by page size
    this.current += this.pageSize;
    var self = this;
    return this._refreshDataWindow().then(function() {
                self._processSuccess(null);});
};


DashboardPaging.prototype.previous = function()
{
    if (this.startIndex() - this.pageSize < 0) {
        // Don't go below 0!
        this.current = 0;
    }
    else {
        this.current -= this.pageSize;
    }
    var self = this;
    return this._refreshDataWindow().then(function() {
                self._processSuccess(null);});
};


DashboardPaging.prototype.setPageSize = function(n)
{
  this.pageSize = n;
};

DashboardPaging.prototype.setFetchCallback = function(callback) {
    this.fetchCallback = callback;
};


DashboardPaging.prototype.startIndex = function()
{
  return this.current;
};


DashboardPaging.prototype.size = function()
{
  var w = this.getWindow();
  return w ? w.length : 0;
};


DashboardPaging.prototype.totalSize = function()
{
    return this.collection.length;
};

DashboardPaging.prototype.getShowPagingObservable = function()
{
    return this.showPagingObservable;
};

DashboardPaging.prototype.getModelFromWindow = function(id)
{
    var _i= 0, w = this.getWindow();
    if (w && w !== null)
    {
        for (_i= 0 ; _i < w.length; _i++)
        {
            if (w[_i].id === id) {
                return w[_i];
            }
        }
    }

    return null;
};

/**
 * Returns the confidence for the totalSize value.
 * @return {string} "actual" if the totalSize is the time of the fetch is an exact number
 *                  "estimate" if the totalSize is an estimate
 *                  "atLeast" if the totalSize is at least a certain number
 *                  "unknown" if the totalSize is unknown
 */
DashboardPaging.prototype.totalSizeConfidence = function()
{
  return "actual";
};

return {'DashboardPaging': DashboardPaging};

});



