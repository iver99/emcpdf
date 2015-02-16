/**
 * @preserve Copyright (c) 2014, Oracle and/or its affiliates.
 * All rights reserved.
 */

/**
 * @preserve Copyright 2013 jQuery Foundation and other contributors
 * Released under the MIT license.
 * http://jquery.org/license
 */
requirejs.config({
    // Path mappings for the logical module names
    paths: {
        'knockout': '../emcsDependencies/oraclejet/js/libs/knockout/knockout-3.2.0',
        'jquery': '../emcsDependencies/oraclejet/js/libs/jquery/jquery-2.1.1.min',
        'jqueryui': '../emcsDependencies/oraclejet/js/libs/jquery/jquery-ui-1.11.1.custom.min',
        'jqueryui-amd':'../emcsDependencies/oraclejet/js/libs/jquery/jqueryui-amd-1.11.1',
        'ojs': '../emcsDependencies/oraclejet/js/libs/oj/v1.0.0/min',
        'ojL10n': '../emcsDependencies/oraclejet/js/libs/oj/v1.0.0/ojL10n',
        'ojtranslations': '../emcsDependencies/oraclejet/js/libs/oj/v1.0.0/resources',
        'signals': '../emcsDependencies/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../emcsDependencies/oraclejet/js/libs/crossroads/crossroads.min',
        'history': '../emcsDependencies/oraclejet/js/libs/history/history.iegte8.min',
        'text': '../emcsDependencies/oraclejet/js/libs/require/text',
        'promise': '../emcsDependencies/oraclejet/js/libs/es6-promise/promise-1.0.0.min',
        'dfutil':'../emcsDependencies/internaldfcommon/js/util/internal-df-util',
        'dbs': '../js',
        'require':'../emcsDependencies/oraclejet/js/libs/require/require'
    },
    // Shim configurations for modules that do not expose AMD
    shim: {
        'jquery': {
            exports: ['jQuery', '$']
        },
        'jqueryui': {
            deps: ['jquery']
        },
        'crossroads': {
            deps: ['signals'],
            exports: 'crossroads'
        }
    },
    // This section configures the i18n plugin. It is merging the Oracle JET built-in translation 
    // resources with a custom translation file.
    // Any resource file added, must be placed under a directory named "nls". You can use a path mapping or you can define
    // a path that is relative to the location of this main.js file.
    config: {
        ojL10n: {
            merge: {
                'ojtranslations/nls/ojtranslations': 'resources/nls/dashboardsMsgBundle'
            }
        }
    }
});

var dashboardsViewModle = undefined;

/**
 * A top-level require call executed by the Application.
 * Although 'ojcore' and 'knockout' would be loaded in any case (they are specified as dependencies
 * by the modules themselves), we are listing them explicitly to get the references to the 'oj' and 'ko'
 * objects in the callback
 */
require(['dbs/dbsmodel',
    'knockout',
    'jquery',
    'ojs/ojcore',
    'dfutil',
    'ojs/ojmodel',
    'ojs/ojknockout',
    'ojs/ojknockout-model',
    'ojs/ojcomponents',
    'ojs/ojvalidation',
//    'ojs/ojdatagrid', 
//    'ojs/ojtable',
//    'ojs/ojtable-model',
    'ojs/ojbutton',
    'ojs/ojinputtext',
    'ojs/ojknockout-validation',
    'ojs/ojpopup',
    'dbs/dbstypeahead',
    'dbs/dbsdashboardpanel',
//    'ojs/ojvalidation'
    'ojs/ojselectcombobox',
    'ojs/ojmenu'
//    'ojs/ojmodel',
//    'ojs/ojknockout-model',
//    'ojs/ojselectcombobox',
//    'ojs/ojdatetimepicker',
//    'ojs/ojtable',
//    'ojs/ojdatagrid',
//    'ojs/ojchart', 
//    'ojs/ojgauge', 
//    'ojs/ojlegend', 
//    'ojs/ojsunburst', 
//    'ojs/ojthematicmap', 
//    'ojs/ojtreemap',
//    'ojs/ojvalidation'
],
        function(model, ko, $, oj, dfu) // this callback gets executed when all required modules are loaded
        {
//            if (!ko.components.isRegistered('df-nav-links')) {
//                ko.components.register("df-nav-links",{
//                    viewModel:{require:'../emcsDependencies/navlinks/js/navigation-links'},
//                    template:{require:'text!../emcsDependencies/navlinks/navigation-links.html'}
//                });
//            }
            if (!ko.components.isRegistered('df-oracle-branding-bar')) {
                ko.components.register("df-oracle-branding-bar",{
                    viewModel:{require:'../emcsDependencies/dfcommon/widgets/brandingbar/js/brandingbar'},
                    template:{require:'text!../emcsDependencies/dfcommon/widgets/brandingbar/brandingbar.html'}
                });
            }
            
            function FooterViewModel() {
                var self = this;

                var aboutOracle = 'http://www.oracle.com/us/corporate/index.html#menu-about';
                var contactUs = 'http://www.oracle.com/us/corporate/contact/index.html';
                var legalNotices = 'http://www.oracle.com/us/legal/index.html';
                var termsOfUse = 'http://www.oracle.com/us/legal/terms/index.html';
                var privacyRights = 'http://www.oracle.com/us/legal/privacy/index.html';

                self.ojVersion = ko.observable('v' + oj.version + ', rev: ' + oj.revision);

                self.footerLinks = ko.observableArray([
                    new FooterNavModel('About Oracle', 'aboutOracle', aboutOracle),
                    new FooterNavModel('Contact Us', 'contactUs', contactUs),
                    new FooterNavModel('Legal Notices', 'legalNotices', legalNotices),
                    new FooterNavModel('Terms Of Use', 'termsOfUse', termsOfUse),
                    new FooterNavModel('Your Privacy Rights', 'yourPrivacyRights', privacyRights)
                ]);

            }

            function FooterNavModel(name, id, linkTarget) {

                this.name = name;
                this.linkId = id;
                this.linkTarget = linkTarget;
            }
 
            function HeaderViewModel() {
                var self = this;
                self.registryUrl = dfu.getRegistryUrl();//"http://adc00pos.us.oracle.com:7001/registry/servicemanager/registry/v1/";
                self.authToken = dfu.getAuthToken();//"Basic d2VibG9naWM6d2VsY29tZTE=";
                self.userName = dfu.getUserName();
                self.tenantName = dfu.getTenantName();
                self.appName = "Dashboard Framework";
            }
           
            dashboardsViewModle = new model.ViewModel();
            headerViewModel = new HeaderViewModel();

            $(document).ready(function() {
                
                //Caution: need below line to enable KO binding, otherwise KOC inside headerWrapper doesn't work
                ko.applyBindings(headerViewModel, document.getElementById('headerWrapper'));
//                ko.applyBindings({navLinksNeedRefresh: headerViewModel.navLinksNeedRefresh}, document.getElementById('links_menu'));
                $("#loading").hide();
//                ko.applyBindings(new HeaderViewModel(), document.getElementById('headerWrapper'));
                $('#globalBody').show();
                // Setup bindings for the header and footer then display everything
                //ko.applyBindings(new FooterViewModel(), document.getElementById('footerWrapper'));
                
                ko.applyBindings(dashboardsViewModle, document.getElementById('mainContent'));
                $('#mainContent').show(); 
                
                
               window.addEventListener('message', childMessageListener, false);
               window.name = 'dashboardhome'; 
               
               if (window.parent && window.parent.updateOnePageHeight)
                   window.parent.updateOnePageHeight('2000px');
            });
        }
);
            
/**
 * listener on messages from child page
 * @param {type} builderData
 * @returns {undefined} */
function childMessageListener(builderData) {
    //console.log(builderData);
    var _o = JSON.parse(builderData);
    //var _did = _o.dashboardId;
    //_o.dashboardId = 0;
    if (_o.eventType && _o.eventType === 'SAVE') {
        dashboardsViewModle.updateDashboard(_o);
    }
//    else if (_o.eventType && _o.dashboardId && _o.eventType === 'ADD_TO_FAVORITES') {
//        dashboardsViewModle.addToFavorites(parseInt(_o.dashboardId));
//    }
//    else if (_o.eventType && _o.dashboardId && _o.eventType === 'REMOVE_FROM_FAVORITES') {
//        dashboardsViewModle.removeFromFavorites(parseInt(_o.dashboardId));
//    }
    
};

/**
*  Callback method to be invokced by child builder page to get dashboard data

 * @param {type} dashboardid
 * @returns {dashboarInfoCallBack.Anonym$0} */
function dashboarDataCallBack(dashboardid) {
    var dashboard = dashboardsViewModle.getDashboard(dashboardid);
    // TODO: put code to retrieve dashboard data, and update code to add 'real' dashboard/widgets data below
    if (dashboard) {
        return {
            dashboardId: dashboard.id,
            dashboardName: dashboard.name, 
            dashboardDescription: dashboard.description, 
            showTimeSlider: String(dashboard.includeTimeRangeFilter),   
//            showTimeSlider: "false",     // to keep consistent with existing code in builder page, put exactly the same STRING "true" for true boolean value, and "false" for false
            type: dashboard.type,//(dashboard.type === 1) ? "onePage" : "normal",  // IMPORTANT: "normal" for common builder page, and "onePage" for special new dashboard type
            widgets: dashboard.widgets
        };
    }
    else // provide the default dashboard
        return {
            dashboardId: dashboardid,
            dashboardName: "Weblogic", 
            dashboardDescription: "Dashboards for weblogic server management", 
            showTimeSlider: "false",     // to keep consistent with existing code in builder page, put exactly the same STRING "true" for true boolean value, and "false" for false
            type: "normal",  // IMPORTANT: "normal" for common builder page, and "onePage" for special new dashboard type
            widgets: [
                {title: "CPU Load"},
                {title: "Error Reports"}
            ]};
};

function truncateString(str, length) {
    if (str && length > 0 && str.length > length)
    {
        var _tlocation = str.indexOf(' ', length);
        if ( _tlocation <= 0 )
            _tlocation = length;
        return str.substring(0, _tlocation) + "...";
    }
    return str;
};


function getNlsString(key, args) {
    return oj.Translations.getTranslatedString(key, args);
};



