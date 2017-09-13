define('uifwk/@version@/js/widgets/widgetselector/widget-selector-impl',[
    'knockout',
    'jquery',
    'uifwk/@version@/js/util/df-util-impl', 
    'ojs/ojcore',
    'ojs/ojselectcombobox',
    'ojs/ojpopup',
    'ojs/ojinputtext',
    'ojs/ojbutton',
    'ojs/ojlistview', 
    'ojs/ojjsontreedatasource'
    ],
        function (ko, $, dfumodel, oj) {
            function WidgetSelectorViewModel(params) {
                var self = this;
                
                self.renderWidgetSelector = ko.observable(false);
                if(!params.delayBinding){
                    self.renderWidgetSelector(true);
                }
                
                if(!params.type){
                    params.type = 'ojDialog';
                }
                self.widgetSelectorDialog = ko.observable(false);
                self.widgetSelectorPopup = ko.observable(false);
                self.widgetSelectorType = ko.observable(params.type);
                switch(params.type){
                    case 'ojDialog':
                        self.widgetSelectorDialog(true);
                        break;
                    case 'ojPopup':
                        self.widgetSelectorPopup(true);
                        break;
                    case 'both':
                        self.widgetSelectorPopup(true);
                        self.widgetSelectorDialog(true);
                        break;
                    default:
                        break;
                }

                if(self.widgetSelectorDialog()){
                    self.widgetSelectorParamsDialog = params;
                    if (!ko.components.isRegistered('df-widget-selector-dialog')) {
                        ko.components.register("df-widget-selector-dialog", {
                            viewModel: {require: 'uifwk/js/widgets/widgetselector/js/widget-selector-dialog'},
                            template: {require: 'text!uifwk/js/widgets/widgetselector/html/widget-selector-dialog.html'}
                        });
                    }
                }
                
                if(self.widgetSelectorPopup()){
                    self.widgetSelectorParamsPopup = $.extend({}, params, true);
                    self.widgetSelectorParamsPopup.dialogId = self.widgetSelectorParamsPopup.dialogId + "-Popup";
                    if (!ko.components.isRegistered('df-widget-selector-popup')) {
                        ko.components.register("df-widget-selector-popup", {
                            viewModel: {require: 'uifwk/js/widgets/widgetselector/js/widget-selector-popup'},
                            template: {require: 'text!uifwk/js/widgets/widgetselector/html/widget-selector-popup.html'}
                        });
                    }
                }

                function onWidgetSelecrotInitialized(event) {
                    if (event.origin !== window.location.protocol + '//' + window.location.host) {
                        return;
                    }
                    var data = event.data;
                    if (data && data.tag && data.tag === 'EMAAS_WIDGETSELECTOR_RENDER') {

                        self.renderWidgetSelector(true);
                    }
                }
                window.addEventListener("message", onWidgetSelecrotInitialized, false);
            }
            return WidgetSelectorViewModel;
        });

