define([
'knockout',
'uifwk/@version@/js/sdk/menu-util-impl',
'uifwk/@version@/js/widgets/aboutbox/aboutbox-impl',
'text!uifwk/js/widgets/aboutbox/html/aboutbox.html',
'uifwk/@version@/js/widgets/navlinks/navigation-links-impl',
'text!uifwk/js/widgets/navlinks/html/navigation-links.html',
'uifwk/@version@/js/widgets/hamburger-menu/hamburger-menu-impl',
'text!uifwk/js/widgets/hamburger-menu/html/hamburger-menu.html',
'uifwk/@version@/js/widgets/brandingbar/brandingbar-impl',
'text!uifwk/js/widgets/brandingbar/html/brandingbar.html',
'uifwk/@version@/js/widgets/widgetselector/widget-selector-popup-impl',
'text!uifwk/js/widgets/widgetselector/html/widget-selector-popup.html',
'uifwk/@version@/js/widgets/widgetselector/widget-selector-dialog-impl',
'text!uifwk/js/widgets/widgetselector/html/widget-selector-dialog.html',
'uifwk/@version@/js/widgets/widgetselector/widget-selector-impl',
'text!uifwk/js/widgets/widgetselector/html/widget-selector.html',
'uifwk/@version@/js/widgets/time-filter/time-filter-impl',
'text!uifwk/js/widgets/timeFilter/html/timeFilter.html',
'uifwk/@version@/js/widgets/datetime-picker/datetime-picker-impl',
'text!uifwk/js/widgets/datetime-picker/html/datetime-picker.html',
'uifwk/@version@/js/util/ajax-util-impl',
'uifwk/@version@/js/util/df-util-impl',
'uifwk/@version@/js/util/logging-util-impl',
'uifwk/@version@/js/util/message-util-impl',
'uifwk/@version@/js/util/mobile-util-impl',
'uifwk/@version@/js/util/preference-util-impl',
'uifwk/@version@/js/util/screenshot-util-impl',
'uifwk/@version@/js/util/typeahead-search-impl',
'uifwk/@version@/js/util/usertenant-util-impl',
'uifwk/@version@/js/sdk/context-util-impl',
'uifwk/@version@/js/sdk/widget-selector-util-impl',
'uifwk/@version@/js/sdk/SessionCacheUtil',
'uifwk/@version@/js/resources/nls/uifwkCommonMsg',
'uifwk/@version@/js/resources/nls/root/uifwkCommonMsg',
'uifwk/@version@/js/util/zdt-util-impl',
'uifwk/@version@/js/sdk/entity-object'
],
    function (ko, menuModel, aboutVM, aboutTemplate, navVM, navTemplate, brandingVM, brandingTemplate, hbgmenuVM, hbgmenuTemplate, widgetsVM, widgetsDialogTemplate, widgetsPopupTemplate, widgetsTemplate, timefilterVM, timeFilterTemplate, timePickerVM, timePickerTemplate) {
//        function registerComponent(kocName, kocViewModel, kocTemplate)
//        {
//            if (!ko.components.isRegistered(kocName))
//            {
//                ko.components.register(kocName, {viewModel: kocViewModel, template: kocTemplate});
//            }
//        };
//        registerComponent("df-oracle-about-box", aboutVM, aboutTemplate);
//        registerComponent("df-oracle-nav-links", navVM, navTemplate);
//        registerComponent("df-oracle-branding-bar", brandingVM, brandingTemplate);
//        registerComponent("df-common-widget-selector", widgetsVM, widgetsTemplate);
//        registerComponent("time-filter", timefilterVM, timeFilterTemplate);
//        registerComponent("date-time-picker", timePickerVM, timePickerTemplate);
        $(document).ready(function() {
            var avoidPageResizeOptIn = $('#uifwkLayoutHbgmenuPlaceHolder').length > 0 ? true : false;
            if (avoidPageResizeOptIn) {
                var menuUtil = new menuModel();
                menuUtil.initializeHamburgerMenuLayout();
            }
        });
        
        var versionedContextSelectorUtils = window.getSDKVersionFile ?
            window.getSDKVersionFile('emsaasui/emcta/ta/js/sdk/contextSelector/api/ContextSelectorUtils') : null;
        var contextSelectorUtil = versionedContextSelectorUtils ? versionedContextSelectorUtils :
            '/emsaasui/emcta/ta/js/sdk/contextSelector/api/ContextSelectorUtils.js';

        require([contextSelectorUtil], function (EmctaContextSelectorUtil) {
            EmctaContextSelectorUtil.registerComponents();
        });
    }
);
