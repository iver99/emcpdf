/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['dfutil', 'ojs/ojcore', 'jquery', 'knockout','jqueryui', 'ojs/ojknockout-model'],
       /*
        * @param {Object} oj
        * @param {jQuery} $
        */
function(dfu, oj, $, ko)
{

(function ()
{ // make sure register is running

ko.bindingHandlers.dbsDashboardPanel = {
    init: function(element, valueAccessor) {
        var _value = ko.unwrap(valueAccessor());
        var _data = _value['data'], _dashabord =  _value['data'], _resturl = _dashabord['href'], _dmProperty = 'oj._internalObj', _dProperty = 'attributes';

        if (_data[_dmProperty]) //oj.KnockoutUtils.map interanl object
        {
            _dashabord = _data[_dmProperty][_dProperty];
            _resturl = _dashabord['href'];
            _value['dashboard'] = _dashabord;
            _value['baseRestUrl'] = _resturl;
            _value['dashboardModel'] = _data[_dmProperty];
        }

        $(element).dbsDashboardPanel(_value);

    },
    update: function(element, valueAccessor) {

    }
};

var TITLE_MAX_LENGTH = 34;

$.widget('dbs.dbsDashboardPanel',
{
        widgetEventPrefix: 'dbs',

        options: {
            activated: null,
            deactivated: null,
            navigated: null,
            deleteClicked: null,
            showInfoClicked: null,
            dashboard: null,
            dashboardModel: null,
            data: null,
            baseRestUrl: null
        },

        classNames:
        {
            //'chover': 'dbs-summary-container-hover',
            'headerTitle': 'dbs-summary-header-title',
            'headerContainer': 'dbs-summary-header-container',
            'headerToolbar': 'dbs-summary-header-toolbar',
            'pages': 'dbs-summary-pages',
            'page': 'dbs-summary-page',
            'active': 'active',
            'pageImage': 'dbs-summary-page-image'
        },

        active: false,

        _activate: function(event) {
            var self = this;
            if (self.active === false)
            {
                var _element = self.element, _toolbarEle = self.toolbarElement;
                _element.addClass(self.classNames['active']);
                self.titleElement.addClass(self.classNames['active']);
                _toolbarEle.addClass(self.classNames['active']);

                self.active = true;
                self._trigger('activated', event, {element: _element, dashboard: self.options['dashboard'], dashboardModel: self.options['dashboardModel']});
            }
        },

        _deactivate: function(event) {
            var self = this;
            if (self.active === true)
            {
                var _element = self.element, _toolbarEle = self.toolbarElement;
                _element.removeClass(self.classNames['active']);
                self.titleElement.removeClass(self.classNames['active']);
                _toolbarEle.removeClass(self.classNames['active']);

                self.active = false;
                self._trigger('deactivated');
            }
        },

        _create: function () {
            var self = this, _element = self.element;
            this._createComponent();
            this.element.attr("aria-dashboard", this.options['dashboard'].id);
            this.element.attr("aria-label", self.options['dashboard'].name);
            this.element.addClass("dbs-dsbnameele");
            if (self.options['dashboard'].systemDashboard)
            {
                this.element.addClass( "dbs-dsbsystem" );
            }
            else
            {
                this.element.addClass( "dbs-dsbnormal" );
            }
            setTimeout(function() {
                if(!_element.is(":hover")) {
                    self._deactivate(null);
                }
            }, 0);
        },

        _createComponent: function () {
            this._createContent();
            this._createHeader();

            var self = this, _name = self.name, _element = self.element;
            self.active = false;
            _element
            .bind('mouseenter.' + _name, function (event) {
                self._activate(event);
            })
            .bind('mouseleave.' + _name, function (event) {
                self._deactivate(event);
            })
            .bind('click.' + _name, function (event) {
                self._fireNavigated(event);
            });
            this._on( this.element, {
			keydown: function( event ) {
                            var keyCode = $.ui.keyCode;
                            switch ( event.keyCode ) {
                                case keyCode.ENTER:
                                        if (_element[0] === event.target)
                                        {
                                            self._fireNavigated(event);
                                            event.preventDefault();
                                        }
					break;
                                default: break;
                            }
                        }
                    });
        },

        _createHeader: function() {
            var self = this, _element = self.element, _name = self.name;
            var _title = self.options['dashboard'].name;
            self.headerElement = $("<div></div>").addClass(self.classNames['headerContainer']);
            self.titleElement = $("<div/>")
                                  .addClass(self.classNames['headerTitle'])
                                  .text(_title);
            self.headerElement.append(self.titleElement);
            _name = self.options['dashboard'].name;
            if ( _name &&  _name.length > TITLE_MAX_LENGTH)
            {
                self.headerElement.attr("title", _name);
            }

            // add toolbar
            self.toolbarElement = $("<div></div>").addClass(self.classNames['headerToolbar']);

            self.infoElement = $("<button data-bind=\"ojComponent: { component:'ojButton', chroming: 'half', display: 'icons', label: getNlsString('DBS_HOME_DSB_PAGE_INFO_LABEL'), icons: {start: 'icon-locationinfo-16 oj-fwk-icon dbs-icon-size-16'}}\"></button>")
                    .addClass("oj-button-half-chrome oj-sm-float-end")
                    .on('click.'+_name, function(event) {
                        //prevent event bubble
                        event.stopPropagation();
                        event.preventDefault();
                        self._fireShowInfoClicked(event, self.infoElement);
                    });
            self.toolbarElement.append(self.infoElement);
            self.headerElement.append(self.toolbarElement);
            _element.append(self.headerElement);

        },

        _createContent: function() {
            this._createContentPages();
            var self = this, _element = self.element;
            _element.append(self.contentPagesEle);
        },

        _setBase64ScreenShot: function(screenShotUrl) {
            var self = this, _title = (self.options['dashboard']) ? self.options['dashboard'].name : '';
            if (!screenShotUrl || screenShotUrl === null)
            {
                if (self.contentPage1ImgEle)
                {
                    self.contentPage1ImgEle.remove();
                    self.contentPage1ImgEle= undefined;
                }
            }
            else
            {
                if (!self.contentPage1ImgEle)
                {
                    self.contentPage1ImgEle = $("<img>").addClass(self.classNames['pageImage']).attr('alt', _title);
                    self.contentPage1Ele.append(self.contentPage1ImgEle);
                }
                self.contentPage1ImgEle.attr("src", screenShotUrl);
            }
        },

        _createContentPages: function() {
            var self = this,  _dmodel = self.options.dashboardModel, _dashboard = self.options['dashboard'];
            self.contentPagesEle = $("<div></div>")
                    .addClass(self.classNames['pages']);
            //image page
            self.contentPage1ImgEle = undefined;
            self.contentPage1Ele = $("<div></div>").addClass(self.classNames['active'])
                    .addClass(self.classNames['page']);

            if (_dmodel['screenShot'])
            {
                var _ss = _dmodel['screenShot'];
                self._setBase64ScreenShot(_ss);
            }
            else {
                if (_dashboard['screenShotHref']) {

                    var url = _dashboard['screenShotHref'];
                    if (!dfu.isDevMode()) {
                        url = dfu.getRelUrlFromFullUrl(url);
                    }

                    // don't use base64 image data, but use the URL retrieved from dashboard directly as screenshot url
                    self._setBase64ScreenShot(url);
                    if(_dmodel) {
                        _dmodel['screenShot'] = url;
                    }
                }
                else
                {
                     self._setBase64ScreenShot(null);
                }
            }
            self.contentPagesEle.append(self.contentPage1Ele);
        },

        _fireDeleteClicked: function(event) {
            this._trigger('deleteClicked', event, {dashboard: this.options['dashboard'], dashboardModel: this.options['dashboardModel']});
        },

        _fireShowInfoClicked: function(event, ele) {
            this._trigger('showInfoClicked', event, {dashboard: this.options['dashboard'], dashboardModel: this.options['dashboardModel'], element: ele});
        },

        _fireNavigated: function(event) {
            var self = this;
            self._trigger('navigated', event, {dashboard: self.options['dashboard'], dashboardModel: this.options['dashboardModel']});
        },

        _destroyComponent: function() {
            var self = this;
            if (self.infoElement && self.infoElement.length > 0) {
                self.infoElement.unbind("click." + self.name);
            }
            self.element.find("*").removeAttr('style').removeClass().remove();
            self.element.unbind("." + self.name);
        },

        _destroy: function () {
            var self = this;
            self._destroyComponent();
        },

        refresh: function () {
            var self = this;
            if (self.options.dashboardModel)
            {
                self.options.dashboard = self.options.dashboardModel['attributes'];
            }
            this._deactivate(null);
            self._destroyComponent();
            self._createComponent();
            ko.applyBindings({}, self.infoElement[0]);
        }
});

}()); // end make sure register is running

});



