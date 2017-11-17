

define(['dashboards/dashboardmodel', 'dashboards/dashboardcollection', 'dashboards/dashboardpaging', 'ojs/ojcore', 'knockout', 'jquery',
    'ojs/ojknockout', 'ojs/ojmodel','ojs/ojpagingcontrol',
    'ojs/ojpagingcontrol-model'],
function(dm, dc, dp, oj, ko, $)
{
/**
 * @preserve Copyright (c) 2015, Oracle and/or its affiliates.
 * All rights reserved.
 */


    var DatasourceFactory = function(dashbaordsUrl, sortBy, filter, federationFeatureShowInUi) {
        var self = this;
        self.dashbaordsUrl = dashbaordsUrl;
        self.sortBy = sortBy;
        self.filter = filter;
        self.federationFeatureShowInUi = federationFeatureShowInUi;
        self.build = function(query, pageSize) {
          return (function () {
            var _orderby = self.sortBy, _filter = self.filter,
                        _model = dm.DashboardModel, _pageSize = 20, _fetchSize = 60, _modelLimit = 1000;
            if (pageSize)
            {
                _pageSize = pageSize;
                _fetchSize = pageSize;
                if (_fetchSize > _modelLimit) {
                  _modelLimit = _fetchSize;
                }
            }
            var _collection = new dc.DashboardCollection([],
                        {'fetchSize': _fetchSize,
                         'modelLimit': _modelLimit,
                         'url': self.dashbaordsUrl,
                         'query': query,
                         'orderBy': _orderby,
                         'filter': _filter,
                         'model': _model,
                         'federationFeatureShowInUi': federationFeatureShowInUi});
            var _pagingds = new dp.DashboardPaging(_collection);
            _pagingds.setPageSize(_pageSize);
            return {'model' : _model, 'collection': _collection, "pagingDS": _pagingds};
          })();
        };
    };

    return {'DatasourceFactory': DatasourceFactory};
});

