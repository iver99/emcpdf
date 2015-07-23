 ({
    appDir: "./build/staging/public_html/",
    baseUrl: "js",
    dir: "./build/public_html",
    optimize:"none",
    optimizeCss: "none",
    modules: [
        {
            name: "builderMain"
        },
        {
        	name: "homeMain"
        }
    ],
    paths: {
    	'knockout': 'empty:',
        'knockout.mapping': 'empty:',
        'jquery': 'empty:',
        'jqueryui': 'empty:',
        'jqueryui-amd':'empty:',
        'ojs': 'empty:',
        'ojL10n': 'empty:',
        'ojtranslations': 'empty:',
        'signals': 'empty:',
        'crossroads': 'empty:',
        'history': 'empty:',
        'text': 'empty:',
        'promise': 'empty:',
        'dashboards': '.',
        'dfutil':'empty:',
        'loggingutil':'empty:',
        'idfbcutil':'empty:',
        'timeselector':'empty:',
        'html2canvas':'empty:',
        'canvg-rgbcolor':'empty:',
        'canvg-stackblur':'empty:',
        'canvg':'empty:',
        'd3':'empty:',
        'emcta':'../../emcta/ta/js',
        'dbs': '../js',
        'require':'empty:',
        'prefutil':'empty:',
    }
})