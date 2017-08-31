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
                self.widgetSelectorParamsDialog = params;
                self.widgetSelectorParamsPopup = $.extend({},params,true);
                self.widgetSelectorParamsPopup.dialogId = self.widgetSelectorParamsPopup.dialogId + "-Popup";
                if (!ko.components.isRegistered('df-widget-selector-dialog')) {
                    ko.components.register("df-widget-selector-dialog",{
                        viewModel:{require:'uifwk/js/widgets/widgetselector/js/widget-selector-dialog'},
                        template:{require:'text!uifwk/js/widgets/widgetselector/html/widget-selector-dialog.html'}
                    });
                }
                if (!ko.components.isRegistered('df-widget-selector-popup')) {
                    ko.components.register("df-widget-selector-popup",{
                        viewModel:{require:'uifwk/js/widgets/widgetselector/js/widget-selector-popup'},
                        template:{require:'text!uifwk/js/widgets/widgetselector/html/widget-selector-popup.html'}
                    });
                }
            }
            return WidgetSelectorViewModel;
        });

