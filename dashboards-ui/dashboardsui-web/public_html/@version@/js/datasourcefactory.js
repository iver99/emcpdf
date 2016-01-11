

define(['dbs/dashboardmodel', 'dbs/dashboardcollection', 'dbs/dashboardpaging', 'ojs/ojcore', 'knockout', 'jquery', 
    'ojs/ojknockout', 'ojs/ojmodel','ojs/ojpagingcontrol',
    'ojs/ojpagingcontrol-model'], 
function(dm, dc, dp, oj, ko, $)
{
/**
 * @preserve Copyright (c) 2015, Oracle and/or its affiliates.
 * All rights reserved.
 */


    var DatasourceFactory = function(dashbaordsUrl, sortBy, types, appTypes, owners, favoritesOnly) {
        var self = this;
        self.dashbaordsUrl = dashbaordsUrl;
        self.sortBy = sortBy;
        self.types = types;
        self.appTypes = appTypes;
        self.owners = owners;
        self.favoritesOnly = favoritesOnly;
        self.build = function(query, pageSize) {
          return (function () {
            var _orderby = self.sortBy, _types = self.types, _appTypes = self.appTypes, _owners =  self.owners, _favoritesOnly = self.favoritesOnly,
                        _model = dm.DashboardModel, _pageSize = 20, _fetchSize = 60, _modelLimit = 1000;
            if (pageSize)
            {
                _pageSize = pageSize;
                _fetchSize = ((_pageSize * 2) > 300 ? _pageSize : (_pageSize * 2));
                if (_fetchSize > _modelLimit) _modelLimit = _fetchSize;
            }
            var _collection = new dc.DashboardCollection([], 
                        {'fetchSize': _fetchSize, 
                         'modelLimit': _modelLimit, 
                         'url': self.dashbaordsUrl, 
                         'query': query,
                         'orderBy': _orderby,
                         'types': _types,
                         'appTypes': _appTypes,
                         'owners': _owners,
                         "favoritesOnly": _favoritesOnly, 
                         'model': _model});
            var _pagingds = new dp.DashboardPaging(_collection);// new oj.CollectionPagingDataSource(_collection);
            _pagingds.setPageSize(_pageSize);
            return {'model' : _model, 'collection': _collection, "pagingDS": _pagingds};
          })();
        };
    };

    return {'DatasourceFactory': DatasourceFactory};
});

