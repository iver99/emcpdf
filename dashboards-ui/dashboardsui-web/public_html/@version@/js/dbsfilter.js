define(['ojs/ojcore', 'jquery', 'knockout','uifwk/js/util/zdt-util'],
       function(oj, $, ko, zdtUtilModel)
{

/**
 * @preserve Copyright (c) 2015, Oracle and/or its affiliates.
 * All rights reserved.
 */

var DASHBOARDS_FILTER_CHANGE_EVENT = "DASHBOARDS_FILTER_CHANGE_EVENT";

var DashboardsFilter = function(filter, sApplications ,options)
{
    var self = this;
    var zdtUtil = new zdtUtilModel();
    self.zdtStatus = ko.observable(false);
    zdtUtil.detectPlannedDowntime(function (isUnderPlannedDowntime) {
        self.zdtStatus(isUnderPlannedDowntime);
    });
    this.filter = filter;
    this.sApplications = sApplications;

    self.serviceFilterItems = [
        {label: getNlsString('DBS_HOME_FILTER_SERVICE_APM_ABBR'), value: 'apm', id:'apmopt', appType:'APM', visible: false},
        {label: getNlsString('DBS_HOME_FILTER_SERVICE_ITA'), value: 'ita', id:'itaopt', appType:'ITAnalytics', visible: false},
        {label: getNlsString('DBS_HOME_FILTER_SERVICE_LA'), value: 'la', id:'laopt', appType:'LogAnalytics', visible: false},
        {label: getNlsString('DBS_HOME_FILTER_SERVICE_OCS'), value: 'ocs', id:'ocsopt', appType:'Orchestration', visible: false},
        {label: getNlsString('DBS_HOME_FILTER_SERVICE_SEC'), value: 'sec', id:'secopt', appType:'SecurityAnalytics', visible: false}
    ];
    self.serviceFilter = ko.observableArray();
    self.showServiceFilter = ko.observable(false);
    self.creatorFilterItems = [
        {label: getNlsString('DBS_HOME_FILTER_CREATOR_ORACLE'), value: 'oracle', id:'oracleopt'},
        {label: getNlsString('DBS_HOME_FILTER_CREATOR_SHARE'), value: 'share', id:'shareopt'},
        {label: getNlsString('DBS_HOME_FILTER_CREATOR_ME'), value: 'me', id:'otheropt'}
    ];
    self.creatorFilter = ko.observableArray();
    self.favoritesFilterItems = [
        {label: getNlsString('DBS_HOME_FILTER_FAVORITES_MY'), value: 'favorites', id:'myfavorites'}
    ];
    self.favoritesFilter = ko.observableArray();
    this.Init();
    this.setFilterOptions(options);
    this.initFilterSelection();
//    this.initFilterListeners();
};

// Subclass from oj.PagingDataSource
oj.Object.createSubclass(DashboardsFilter, oj.EventSource, "DashboardsFilter");

DashboardsFilter.prototype.Init = function()
{
    DashboardsFilter.superclass.Init.call(this);
};

//DashboardsFilter.prototype.initFilterListeners = function()
//{
//    var self = this;
//    self.creatorFilter.subscribe(function(newValue) {
//        self.saveFilter();
//        self.handleFilterChange({filterType: 'serviceFilter', newValue: newValue});
//    });
//    self.favoritesFilter.subscribe(function(newValue) {
//        self.saveFilter();
//        self.handleFilterChange({filterType: 'serviceFilter', newValue: newValue});
//    });
//};

DashboardsFilter.prototype.initFilterSelection = function()
{
    var self = this;
    if (this.filter)
    {
        var _fs = this.filter.split(",");
        $.each(_fs, function( i, _item ) {
            self._addFilterSelection(_item);
        });
    }
};

DashboardsFilter.prototype._addFilterSelection = function(selection)
{
    var self = this;
    if (selection && selection.trim().length > 0)
    {
        $.each(self.serviceFilterItems, function( i, _item ) {
            if (_item['value'] && _item['value'] === selection)
            {
                self.serviceFilter.push(selection);
            }
        });
        $.each(self.creatorFilterItems, function( i, _item ) {
            if (_item['value'] && _item['value'] === selection)
            {
                self.creatorFilter.push(selection);
            }
        });
        $.each(self.favoritesFilterItems, function( i, _item ) {
            if (_item['value'] && _item['value'] === selection)
            {
                self.favoritesFilter.push(selection);
            }
        });
    }
};

DashboardsFilter.prototype.saveFilter = function()
{
    if (this.saveFilterPref === true && this.prefUtil && this.filterPrefKey)
    {
        var _fs =  this.toFilterString();
        if (_fs && _fs.trim().length > 0)
        {
            this.prefUtil.setPreference(this.filterPrefKey, _fs);
        }
        else
        {
            this.prefUtil.removePreference(this.filterPrefKey);
        }
    }
};

DashboardsFilter.prototype.setFilterOptions = function(options)
{
    this.options = options || {};
    if (this.options['saveFilterPref'])
    {
        this.saveFilterPref = this.options['saveFilterPref'];
    }
    if (this.options['prefUtil'])
    {
        this.prefUtil = this.options['prefUtil'];
    }
    if (this.options['filterPrefKey'])
    {
        this.filterPrefKey = this.options['filterPrefKey'];
    }
    if (this.options['filterChange'])
    {
        this.onFilterChange(this.options['filterChange']);
    }
    this.onFilterChange(function(event){ console.log("Filter change: "+event.filterType+" value: "+event.newValue);});
};

DashboardsFilter.prototype.toFilterString = function()
{
    var self = this, _fjoin = [];
    _fjoin = _fjoin.concat(self.serviceFilter(), self.creatorFilter(), self.favoritesFilter());

    return _fjoin.length > 0 ? _fjoin.join(',') : undefined;
};

DashboardsFilter.prototype.onFilterChange = function(eventHandler)
{
    this.on(DASHBOARDS_FILTER_CHANGE_EVENT, eventHandler);
};

DashboardsFilter.prototype.handleFilterChange = function(event)
{
    this.handleEvent(DASHBOARDS_FILTER_CHANGE_EVENT, event);
};

return {'DashboardsFilter': DashboardsFilter};
});
