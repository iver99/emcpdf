/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['builder/core/builder.event.dispatcher', 'knockout', 'jquery'], function(dsp, ko, $) {
    var Builder = {
        _modules: {},
        _funcs: {},
        registerModule: function(module, name) {
            if (!name) {
                console.error("Failed to register module: name for module '" + functionName(module) + "' is not specified");
                return;
            }
            if (this._modules[name]) {
                console.warn("There might be some issue: module with name '" + name + "' has been registered already");
            }
            if (this._funcs[name]) {
                console.warn("There might be some issue: module with name '" + name + "' has been registered (as a function) already");
            }
            window.DEV_MODE && console.debug("Registering module: " + name);
            this._modules[name] = this[name] = module;
        },
        registerFunction: function(func, name) {
            if (!name) {
                console.error("Failed to register module: name for function '" + functionName(func) + "' is not specified");
                return;
            }
            if (this._modules[name]) {
                console.warn("There might be some issue: function with name '" + name + "' has been registered (as a module) already");
            }
            if (this._funcs[name]) {
                console.warn("There might be some issue: function with name '" + name + "' has been registered already");
            }
            window.DEV_MODE && console.debug("Registering function: " + name);
            this._funcs[name] = this[name] = func;
        }
    };

    // Literal definitions
    // dashboard type to keep the same with return data from REST API
    Builder.SINGLEPAGE_TYPE = "SINGLEPAGE";
    Builder.WIDGET_SOURCE_DASHBOARD_FRAMEWORK = 0;
    Builder.LINK_NAME_MAX_LENGTH = 4000;
    Builder.LINK_URL_MAX_LENGTH = 4000;
    Builder.BUILDER_DEFAULT_TILE_WIDTH = 6;
    Builder.BUILDER_DEFAULT_TILE_HEIGHT = 2;
    Builder.DEFAULT_HEIGHT = 161;
    Builder.EDITOR_POS_BASED_ON_ROW_COLUMN = "BASED_ON_ROW_COLUMN";
    Builder.EDITOR_POS_FIND_SUITABLE_SPACE = "FIND_SUITABLE_SPACE";
    Builder.DEFAULT_AUTO_REFRESH_INTERVAL = 300000;

    window.Builder = Builder;

    function DashboardBuilder(dashboard, $dashboardEl) {
        var self = this;

        self.dashboard = dashboard;

        self.findEl = function (expr) {
            if (arguments.length === 0) {
                return $dashboardEl;
            }

            var $foundEl = $dashboardEl.find(expr);
            // TODO should be removed in production. Just for checking if missing
            // elements after migrating dashboard to dashboardsets.
            var whiteNameList = [".dbd-widget", ".dbd-left-panel-widget-text", ".tooltip-add-widget"];
            if ($foundEl.length === 0 && (whiteNameList.indexOf(expr) < 0)) {
                console.error(expr + " is not found in dashboard container element");
            }
            return $foundEl;
        };

        self.isDashboardUpdated = ko.observable(false);

        // module objects registration
        self._objects = {};
        self.registerObject = function(obj, name) {
            window.DEV_MODE && console.debug("Registering object: " + name);
            self._objects[name] = self['get'+name] = function() {return obj;};
        };

        self.EVENT_POST_DOCUMENT_SHOW = "EVENT_POST_DOCUMENT_SHOW";
        self.EVENT_BUILDER_RESIZE = "EVENT_BUILDER_RESIZE";
        self.EVENT_ENTER_NORMAL_MODE = "EVENT_ENTER_NORMAL_MODE";
        self.EVENT_ENTER_TABLET_MODE = "EVENT_ENTER_TABLET_MODE";

        self.EVENT_DSB_ENABLE_TIMERANGE_CHANGED = "EVENT_DSB_ENABLE_TIMERANGE_CHANGED";


        self.EVENT_NEW_WIDGET_START_DRAGGING = "EVENT_NEW_WIDGET_START_DRAGGING";
        self.EVENT_NEW_WIDGET_DRAGGING = "EVENT_NEW_WIDGET_DRAGGING";
        self.EVENT_NEW_WIDGET_STOP_DRAGGING = "EVENT_NEW_WIDGET_STOP_DRAGGING";

        self.EVENT_TILE_MAXIMIZED = "EVENT_TILE_MAXIMIZED";
        self.EVENT_TILE_RESTORED = "EVENT_TILE_RESTORED";

        self.EVENT_TILE_ADDED = "EVENT_TILE_ADDED";
        self.EVENT_TILE_DELETED = "EVENT_TILE_DELETED";

        /*self.EVENT_TEXT_START_EDITING = "EVENT_TEXT_START_EDITING";
        self.EVENT_TEXT_STOP_EDITING = "EVENT_TEXT_STOP_EDITING";*/

        // an addition bool parameter to indicate at least one tile exists in dashboard, false to indicate no tiles in dashboard
        self.EVENT_TILE_EXISTS_CHANGED = "EVENT_TILE_EXISTS_CHANGED";
       
        self.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA = "EVENT_DISPLAY_CONTENT_IN_EDIT_AREA";

        self.EVENT_TILE_MOVE_STOPED = "EVENT_TILE_MOVE_STOPED";
        self.EVENT_TILE_RESIZED = "EVENT_TILE_RESIZED";
        self.EVENT_TILE_LINK_CHANGED = "EVENT_TILE_LINK_CHANGED";

        self.EVENT_AUTO_REFRESH_CHANGED = "EVENT_AUTO_REFRESH_CHANGED";
        
        self.EVENT_AUTO_REFRESHING_PAGE = "EVENT_AUTO_REFRESHING_PAGE";
        
        self.EVENT_DASHBOARD_SHARE_CHANGED = "EVENT_DASHBOARD_SHARE_CHANGED";
        
        self.EVENT_DASHBOARD_CLEANUP_DELETED_WIDGETS = "EVENT_DASHBOARD_CLEANUP_DELETED_WIDGETS";
        
        self.EVENT_DASHBOARD_OPTION_MENU_RENDERED = "EVENT_DASHBOARD_OPTION_MENU_RENDERED";
        
        self.EVENT_DASHBOARD_INIT_FIRST_RESIZE = "EVENT_DASHBOARD_INIT_FIRST_RESIZE";
        
        self.dispatcher = new dsp.Dispatcher();
        self.addEventListener = function(event, listener) {
            self.dispatcher.registerEventHandler(event, listener);
        };
        
        // event dispatcher listens to init first resize
        self.addEventListener(self.EVENT_DASHBOARD_INIT_FIRST_RESIZE, self.dispatcher.initFirstResize);

        self.triggerEvent = function(event, message, p1, p2, p3, p4) {
            window.DEV_MODE && console.debug('Dashboard builder event [Event]' + event + (message?' [Message]'+message:'') + ((p1||p2||p3)?(' [Parameter(s)]'+(p1?'(p1:'+p1+')':'')+(p2?'(p2:'+p2+')':'')+(p3?'(p3:'+p3+')':'')+(p4?'(p4:'+p4+')':'')):""));
            self.dispatcher.triggerEvent(event, p1, p2, p3, p4);
        };

        self.addNewWidgetStartDraggingListener = function(listener) {
            self.addEventListener(self.EVENT_NEW_WIDGET_START_DRAGGING, listener);
        };

        self.addNewWidgetDraggingListener = function(listener) {
            self.addEventListener(self.EVENT_NEW_WIDGET_DRAGGING, listener);
        };

        self.addNewWidgetStopDraggingListener = function(listener) {
            self.addEventListener(self.EVENT_NEW_WIDGET_STOP_DRAGGING, listener);
        };

        var previousWidth, NORMAL_MIN_WIDTH = 768;
        self.triggerBuilderResizeEvent = function(message) {
            var height = $(window).height()/* - $('#headerWrapper').outerHeight()
                    - $('#head-bar-container').outerHeight()*/;
            var width = ($("#omcHamburgerMenu:visible").not(".oj-offcanvas-start").length > 0 && $("#omcHamburgerMenu:visible").not(".oj-offcanvas-start").height() > 0) ? $(window).width()-$("#omcHamburgerMenu").width():$(window).width();
            var $visibleDashboard = $("#dashboards-tabs-contents .dashboard-content:visible");
            var $rightPanelToggler = $('.right-panel-toggler');
            var $dbdLeftPanel = $('.dbd-left-panel');
            var panelWidth = $dbdLeftPanel.is(":visible") ? Math.min(320, $dbdLeftPanel.width()) : 0;
            var togglerWidth = $rightPanelToggler.is(":visible") ? Math.min(35, $rightPanelToggler.outerWidth()) : 0;
            var leftWidth = panelWidth + togglerWidth;
            var $dbdSetTabs = $('#dbd-set-tabs');
            var tabSetHeight = $dbdSetTabs.is(":visible") ? $dbdSetTabs.height() : 0;
            var topHeight = $('#headerWrapper').outerHeight() +
                    tabSetHeight +
                    $visibleDashboard.find('.head-bar-container:visible').outerHeight();
            console.log("******width:"+width+", panelWidth:" + panelWidth + ", togglerWidth:" + togglerWidth);
            self.triggerEvent(self.EVENT_BUILDER_RESIZE, message, width, height, leftWidth, topHeight);
            if (previousWidth && width >= NORMAL_MIN_WIDTH && previousWidth < NORMAL_MIN_WIDTH){
                self.triggerEvent(self.EVENT_ENTER_NORMAL_MODE, null, width, height, leftWidth, topHeight);
            }
            else if (previousWidth && width < NORMAL_MIN_WIDTH && previousWidth >= NORMAL_MIN_WIDTH){
                self.triggerEvent(self.EVENT_ENTER_TABLET_MODE, null, width, height, leftWidth, topHeight);
            }
            previousWidth = width;
        };

        self.addBuilderResizeListener = function(listener) {
            self.addEventListener(self.EVENT_BUILDER_RESIZE, listener);
        };

        function dashboardUpdatedListener(event) {
            self.isDashboardUpdated(true);
        };
        self.addEventListener(self.EVENT_TILE_ADDED, dashboardUpdatedListener);
        self.addEventListener(self.EVENT_TILE_DELETED, dashboardUpdatedListener);
        self.addEventListener(self.EVENT_DSB_ENABLE_TIMERANGE_CHANGED, dashboardUpdatedListener);
        self.addEventListener(self.EVENT_TILE_MOVE_STOPED, dashboardUpdatedListener);
        self.addEventListener(self.EVENT_TILE_RESIZED, dashboardUpdatedListener);
        self.addEventListener(self.EVENT_TILE_LINK_CHANGED, dashboardUpdatedListener);
    }

    Builder.registerModule(DashboardBuilder, 'DashboardBuilder');

    function functionName(fn) {
        if (Function.prototype.name === undefined) {
            var funcNameRegex = /function\s([^(]{1,})\(/;
            var results = (funcNameRegex).exec((fn).toString());
            return (results && results.length > 1) ? results[1].trim() : "";
        }
        else if (fn.prototype === undefined) {
            return fn.constructor.name;
        }
        else {
            return fn.prototype.constructor.name;
        }
    }

    return DashboardBuilder;
});
