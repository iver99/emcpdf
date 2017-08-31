define('uifwk/@version@/js/sdk/widget-selector-util-impl', [
    'ojs/ojcore',
    'knockout',
    'jquery'
],
    function (oj, ko, $)
    {
        function UIFWKWidgetSelectorUtil(widgetSelectorId) {
            var self = this;
            self.widgetSelectorId = widgetSelectorId + '-Popup';

            /**
             * Set current menu item
             * 
             * @param {String} menuItemId Id of the menu item which should be set in selected and highlighted status
             * 
             * @returns
             */
            self.widgetSelector = function() {
//                return $('#'+ self.widgetSelectorId).ojPopup(arguments[0]);
                return $('#'+ self.widgetSelectorId).ojPopup.apply($('#'+ self.widgetSelectorId), [].slice.apply(arguments));
            };

        }
        return UIFWKWidgetSelectorUtil;
    }
);

