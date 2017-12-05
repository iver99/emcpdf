 ({
    appDir: "../build/staging/public_html/",
    baseUrl: "@version@/js",
    dir: "../build/public_html",
    optimize:"none",
    optimizeCss: "standard", 
    modules: [
        {
            name: "builder/builder.jet.partition"
        },
        {
            name: "builderMain",
            include: ["dashboards/dashboardhome-impl", 'dashboards/dbsdashboardpanel', 'text!../../@version@/html/dashboardhome.html', "DOMPurify"],
            exclude: ["builder/builder.jet.partition"]
        },
        {
            name: "homeMain"
        },
	{
            name: "welcomeMain"
        }
    ],
    paths: {
    	'knockout': '../../libs/@version@/js/oraclejet/js/libs/knockout/knockout-3.4.0',
        'knockout.mapping': '../../libs/@version@/js/oraclejet/js/libs/knockout/knockout.mapping-latest',
        'jquery': '../../libs/@version@/js/oraclejet/js/libs/jquery/jquery-2.1.3.min',
        'jqueryui': '../../libs/@version@/js/oraclejet/js/libs/jquery/jquery-ui-1.11.4.custom.min',
        'jqueryui-amd':'../../libs/@version@/js/oraclejet/js/libs/jquery/jqueryui-amd-1.11.4.min',
        'hammerjs': '../../libs/@version@/js/oraclejet/js/libs/hammer/hammer-2.0.4.min',
        'ojs': '../../libs/@version@/js/oraclejet/js/libs/oj/v2.0.2/min',
        'ojL10n': '../../libs/@version@/js/oraclejet/js/libs/oj/v2.0.2/ojL10n',
        'ojtranslations': '../../libs/@version@/js/oraclejet/js/libs/oj/v2.0.2/resources',
        'ojdnd': '../../libs/@version@/js/oraclejet/js/libs/dnd-polyfill/dnd-polyfill-1.0.0.min',
        'signals': '../../libs/@version@/js/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../../libs/@version@/js/oraclejet/js/libs/crossroads/crossroads.min',
        'history': '../../libs/@version@/js/oraclejet/js/libs/history/history.iegte8.min',
        'text': '../../libs/@version@/js/oraclejet/js/libs/require/text',
        'promise': '../../libs/@version@/js/oraclejet/js/libs/es6-promise/promise-1.0.0.min',
        'dashboards': '.',
        'dfutil':'internaldfcommon/js/util/internal-df-util',
        'mobileutil':'empty:',
        //'uiutil':'empty:',
        //'uifwk/js/util/df-util':'empty:',
	'uifwk':'empty:',
        'loggingutil':'empty:',
        //'idfbcutil':'empty:',
        //'timeselector':'empty:',
        //'html2canvas':'empty:',
        //'canvg-rgbcolor':'empty:',
        //'canvg-stackblur':'empty:',
        //'canvg':'empty:',
        'uiutil':'internaldfcommon/js/util/ui-util',
        'idfbcutil':'internaldfcommon/js/util/internal-df-browser-close-util',
        'd3':'../../libs/@version@/js/d3/d3.min',
        //'emcta':'../../emcta/ta/js',
	'emsaasui':'/emsaasui',
        'emcta':'/emsaasui/emcta/ta/js',
	'emsaasui/emcta/ta/js': 'empty:',
        'emcla':'/emsaasui/emlacore/js',
        'emcsutl': '/emsaasui/uifwk/emcsDependencies/uifwk/js/util',
        'dbs': '../js',
        'require':'../../libs/@version@/js/oraclejet/js/libs/require/require',
        'DOMPurify': '../../libs/@version@/js/DOMPurify/purify',
        'prefutil':'empty:'
//	'ckeditor': '../../libs/@version@/js/ckeditor/ckeditor',
    }
})
