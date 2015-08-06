define(['dfutil', 'ojs/ojcore', 'jquery', 'knockout', 'ojs/ojpagingcontrol', 'ojs/ojknockout-model'],
       function(dfu, oj, $, ko)
{

/**
 * @preserve Copyright (c) 2015, Oracle and/or its affiliates.
 * All rights reserved.
 */


 DashboardPaging = function(collection)
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
    if (_isShow !== true && this.pageSize < this.totalSize()) this.showPagingObservable(true); //show paging
    if (_isShow === true && this.pageSize >= this.totalSize()) this.showPagingObservable(false); //show paging
    //console.log("[DashboardPaging] show paging? " + this.showPagingObservable());
    
    this.dataWindow = new Array(this._getSize());
    
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

DashboardPaging.prototype.IterativeAt = function (start, end) {
    var array = [], i;
    var self = this;
    return self.__getPromise(function(allResolve, allReject) {
        var doTask = function(index) {
                        return self.__getPromise(function(resolve, reject) {
                            self.collection.at(index, null).then(function(model) {
                                if (model) array.push(model);
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
  //this.pageSize = options['pageSize'] != null ? options['pageSize'] : this.pageSize;
  //options['startIndex'] = value * this.pageSize;
  var previousPage = this._page;
  this._page = value;
  if (this._page >= 0 && this._page === previousPage) 
  {
      return Promise.resolve(null);
  }
  this._startIndex = value * this.pageSize;
  
  return new Promise(function(resolve, reject)
  {
    self.fetch({'startIndex': self._startIndex, 
            'success': function() {
                DashboardPaging.superclass.handleEvent.call(self, oj.PagingModel.EventType['PAGE'], {'page' : self._page, 'previousPage' : previousPage});
                if ($.isFunction(_successCallback))
                {
                   _successCallback({'page' : self._page, 'previousPage' : previousPage});
                }
                resolve(null);
            },
            'error': function(jqXHR, textStatus, errorThrown) {
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
};

DashboardPaging.prototype.getStartItemIndex = function()
{
  return this._startIndex;
};

DashboardPaging.prototype.getEndItemIndex = function()
{
  var self = this;
  return self._startIndex + self.dataWindow.length - 1;//this._endIndex;
};

DashboardPaging.prototype.getPageCount = function()
{
  var totalSize = this.totalSize();
  return totalSize == -1 ? -1 : Math.ceil(totalSize / this.pageSize);
};

DashboardPaging.prototype.refreshModel = function(modelId, options)
{
    var opts = options || {};
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
       // var _url =  this.collection['url'];
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
                            /* //no need for refresh, nav directly
                            self._fetch( { 
                                'contentType': 'application/json',
                                success: function() {
                                    if ($.isFunction(opts['success']))
                                    {
                                        opts['success'](_model, _resp, _options);
                                    }
                                }, 
                                error: opts['error']});*/
                            //model.openDashboardPage();
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            self._processError(opts, jqXHR, textStatus, errorThrown);
                            //console.log('Error in Create: ' + textStatus);
                        }});
    }
    catch (e) {
        var _e = e;
        self._refreshDataWindow().then(function() {
            self._processError(opts, null, null, _e)});
    }
};

DashboardPaging.prototype.remove = function(model, options)
{
    var self = this;
    dfu.ajaxWithRetry('/sso.static/dashboards.service/' + model.get('id'), {
       type: 'DELETE',
       headers: dfu.getDashboardsRequestHeader(),//{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},
       success: function(result) {
          // Do something with the result
          self.collection.remove(model);
          if (self.pageSize <= self.totalSize())
          {
              self._refreshDataWindow().then(self._processSuccess(options, "remove"));
          }
          else {
              self._refreshDataWindow().then(function() { self._processSuccess(options); });
          }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            self._processError(options, jqXHR, textStatus, errorThrown);
            //console.log('Error in Create: ' + textStatus);
        }
    });
    
};



DashboardPaging.prototype.fetch = function(options)
{
    if (!this.collection) return;
    var self = this, opts = options || {}, _forceFetch = options['forceFetch'];
    if (opts['startIndex'] !== undefined) {
         this.current = opts['startIndex'];
    }
    if (opts['pageSize'] !== undefined) {
        this.pageSize = opts['pageSize'];
    }
    var _lastFetchSize = self.collection.lastFetchSize, _offset = self.collection.offset;
    if (_forceFetch !== true)
    {
        if (this.current >= _offset && (this.pageSize + this.current) <= (_offset + _lastFetchSize))
        {
            //console.log("[DashboardPaging] resolve fetch");
            return this._refreshDataWindow().then(function() {
                              self._processSuccess(opts);
                          });
        }
    }
    //console.log("[DashboardPaging] called fetch");
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
            //console.log('Error in Create: ' + textStatus);
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
    this.handleEvent("sync", _event);
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
    this.handleEvent(eventType || "sync", _event);
    
    if (options['success']) {
        options['success']();
    }
/*    else if (this.fetchCallback) {
        this.fetchCallback();
    }*/
};

DashboardPaging.prototype.handleEvent = function(eventType, event)
{
    DashboardPaging.superclass.handleEvent.call(this, eventType, event);
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
                self._processSuccess(null)});
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
                self._processSuccess(null)});
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

return {'DashboardPaging': DashboardPaging};

});



