define('uifwk/@version@/js/sdk/widget-selector-util-impl', [
    'ojs/ojcore',
    'knockout',
    'jquery'
],
    function (oj, ko, $)
    {
        function UIFWKWidgetSelectorUtil(widgetSelectorId, delayBinding) {
            var self = this;
            self.widgetSelectorId = widgetSelectorId + '-Popup';
            self.widgetSelectorInitializeded = ko.observable(false);
            if(!delayBinding){
                self.widgetSelectorInitializeded(true);
            }
            
            /**
             * Apply binding on widget selector component
             * 
             * @returns
             */
            self.renderWidgetSelector = function (callback){
                if(!self.widgetSelectorInitializeded()){
                    function onWidgetSelecrotInitialized(event) {
                        if (event.origin !== window.location.protocol + '//' + window.location.host) {
                            return;
                        }
                        var data = event.data;
                        if (data && data.tag && data.tag === 'EMAAS_WIDGETSELECTOR_INITIALIZED') {
                            self.widgetSelectorInitializeded = ko.observable(true);
                            callback && callback();
                        }
                    }
                    window.addEventListener("message", onWidgetSelecrotInitialized, false);
                    var message = {'tag': 'EMAAS_WIDGETSELECTOR_RENDER'};
                    window.postMessage(message, window.location.href);
                }else{
                    callback && callback();
                }
            };

            /**
             * Almost same with API ojXXX of Oracle Jet component
             * 
             * @param same with Oracle Jet API
             * 
             * @returns none on the first time called, same with Oracle Jet API then
             */
            self.widgetSelector = function() {
                var _args = arguments;
                if(!self.widgetSelectorInitializeded()){
                    self.renderWidgetSelector(function(){
                        $('#' + self.widgetSelectorId).ojPopup.apply($('#' + self.widgetSelectorId), [].slice.apply(_args));
                    });
                }else{
                    return $('#'+ self.widgetSelectorId).ojPopup.apply($('#'+ self.widgetSelectorId), [].slice.apply(_args));
                }
            };
            
        }
        return UIFWKWidgetSelectorUtil;
    }
);

