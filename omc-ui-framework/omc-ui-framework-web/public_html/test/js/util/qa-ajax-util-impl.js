define(['ojs/ojcore',
    'knockout',
    'jquery',
    'uifwk/@version@/js/util/ajax-util-impl'
],function(oj, ko, $, ajaxUtilModel){
    return {
        run: function(){
            QUnit.module("ajaxUtilTest");
            QUnit.test("getAjaxOptionsTest", function(assert){
               var ajaxUtil = new ajaxUtilModel();
               var argsWithLenOneString = ["/emsaasui/emcpdfui/builder.html"];
               var retryOptions = ajaxUtil.getAjaxOptions(argsWithLenOneString);
               assert.equal(argsWithLenOneString[0],retryOptions.url);
               
               var argsWithLenOneObject = [{"url":"/emsaasui/emcpdfui/builder.html"}];
               retryOptions = ajaxUtil.getAjaxOptions(argsWithLenOneObject);
               assert.equal(argsWithLenOneObject[0].url, retryOptions.url);
               retryOptions.assertFunction= function(){return true;};
               assert.equal(undefined ,argsWithLenOneObject[0].assertFunction, "success");
               
               var argsWithLenTwoObject = ["/emsaasui/emcpdfui/builder.html", {"name":"dashboard"}];
               retryOptions = ajaxUtil.getAjaxOptions(argsWithLenTwoObject);
               assert.equal("dashboard", retryOptions.name);
               assert.equal("/emsaasui/emcpdfui/builder.html", retryOptions.url);
               retryOptions.assertFunction= function(){return true;};
               assert.equal(undefined ,argsWithLenTwoObject[1].assertFunction, "success");

               var argsWithFunction = function(){return true;}; 
               var argsWithLenTwoFunction = ["/emsaasui/emcpdfui/builder.html", argsWithFunction];
               retryOptions = ajaxUtil.getAjaxOptions(argsWithLenTwoFunction);
               assert.equal("/emsaasui/emcpdfui/builder.html", retryOptions.url);
               assert.equal(argsWithFunction, retryOptions.success);
               
               var argsWithLenTwoNull = ["/emsaasui/emcpdfui/builder.html", null];
               retryOptions = ajaxUtil.getAjaxOptions(argsWithLenTwoNull);
               assert.equal("/emsaasui/emcpdfui/builder.html", retryOptions.url);
               
               var argsWithLenTwoNullString = [{"name":"dashboard"}, null];
               retryOptions = ajaxUtil.getAjaxOptions(argsWithLenTwoNullString);
               assert.equal("dashboard", retryOptions.name);
               retryOptions.assertFunction= function(){return true;};
               assert.equal(undefined ,argsWithLenTwoNullString[0].assertFunction, "success");
               
               var argsWithLenThree = ["/emsaasui/emcpdfui/builder.html", argsWithFunction,{"name":"dashboard"}];
               retryOptions = ajaxUtil.getAjaxOptions(argsWithLenThree);
               assert.equal("/emsaasui/emcpdfui/builder.html", retryOptions.url);
               assert.equal(argsWithFunction, retryOptions.success);
               assert.equal("dashboard", retryOptions.name);
               retryOptions.assertFunction= function(){return true;};
               assert.equal(undefined ,argsWithLenThree[2].assertFunction, "success");
               
               var argsWithLenThreeNullObject = ["/emsaasui/emcpdfui/builder.html", argsWithFunction, null];
               retryOptions = ajaxUtil.getAjaxOptions(argsWithLenThreeNullObject);
               assert.equal("/emsaasui/emcpdfui/builder.html", retryOptions.url);
               assert.equal(argsWithFunction, retryOptions.success);
               
               var argsWithLenThreeNullFunction = ["/emsaasui/emcpdfui/builder.html", null, {"name":"dashboard"}];
               retryOptions = ajaxUtil.getAjaxOptions(argsWithLenThreeNullFunction);
               assert.equal("/emsaasui/emcpdfui/builder.html", retryOptions.url);
               assert.equal("dashboard", retryOptions.name);
               retryOptions.assertFunction= function(){return true;};
               assert.equal(undefined ,argsWithLenThreeNullFunction[2].assertFunction, "success");
               
               var argsWithLenThreeNullBoth = ["/emsaasui/emcpdfui/builder.html", null, null];
               retryOptions = ajaxUtil.getAjaxOptions(argsWithLenThreeNullBoth);
               assert.equal("/emsaasui/emcpdfui/builder.html", retryOptions.url);
            });
        }
    }
});