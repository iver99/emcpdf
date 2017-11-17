

define(['dfutil', 'ojs/ojcore', 'knockout', 'jquery', 'ojs/ojknockout', 'ojs/ojmodel'],
function(dfu, oj, ko, $)
{
/**
 * @preserve Copyright (c) 2015, Oracle and/or its affiliates.
 * All rights reserved.
 */


    DashboardCollection = function(models, options) {
        var self = this, _options = options || {};
        self.queryString = _options['query'] || null;
        self.orderBy = _options['orderBy'] || null;
        self.filter = _options['filter'] || null;
        self.federationFeatureShowInUi = _options['federationFeatureShowInUi'] || false;
        //self.url
        var _customPagingOptions = function(response){
          var _ret = {
              totalResults: response['totalResults'],
              limit: response['limit'],
              count: response['count'],
              offset:response['offset']
              //hasMore:response['hasMore'],
          };
          return _ret;
        };

        var _customURL = function(_operation, _col, _opt) {
            var __url = ($.isFunction(self.url) === true ? self.url() : self.url);
            if (_operation === "read")
            {
                __url = __url + "?" + (_opt['startIndex'] ? ("offset=" + _opt['startIndex']) : "offset=0");
                if (_opt['fetchSize'])
                {
                    __url = __url + "&limit=" + _opt['fetchSize'];
                }
                if (self.queryString !== null)
                {
                    __url = __url + "&queryString=" + encodeURIComponent(self.queryString);
                }
                if (self.orderBy !== null)
                {
                    __url = __url + "&orderBy=" + self.orderBy;
                }
                if (self.filter !== null && self.filter.trim().length > 0)
                {
                     __url = __url + "&filter=" + self.filter.trim();
                }
                if (self.federationFeatureShowInUi === true) {
                     __url = __url + "&federationFeatureShowInUi=true";
                }
            }
            return {
                    url: __url,
                    headers: dfu.getDashboardsRequestHeader()
               };
        };

        _options['customPagingOptions'] = _customPagingOptions;
        _options['customURL'] = _customURL;

        DashboardCollection.superclass.constructor.call(this, models, _options);
    };

    oj.Object.createSubclass(DashboardCollection, oj.Collection, 'DashboardCollection');

    /**
     * Initializes the data source.
     * @export
     */
    DashboardCollection.prototype.Init = function()
    {
        // super
        DashboardCollection.superclass.Init.call(this);
    };

    return {'DashboardCollection': DashboardCollection};
});

