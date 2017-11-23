define(['ojs/ojcore',
    'knockout',
    'jquery',
    'uifwk/@version@/js/sdk/context-util-impl'
],function(oj, ko, $, contextUtilViewModel){
    return {
        run: function(){
            QUnit.module("contextUtilTest");
            QUnit.test("testContextUtilTranslatedTimePeriodString", function(assert){
               var contextutil = new contextUtilViewModel();
               var result1 = "";
               var result2 = "";
               var result3 = "";
               var result4 = "";
               result1 = contextutil.getTranslatedTimePeriod("LAST_2_WEEK","LONG_TERM");
               result2 = contextutil.getTranslatedTimePeriod("CUSTOM","LONG_TERM");
               result3 = contextutil.getTranslatedTimePeriod("LATEST","SHORT_TERM");
               result4 = contextutil.getTranslatedTimePeriod("LAST_15_MINUTE","SHORT_TERM");
               assert.equal(result1,"Last 2 weeks");     
               assert.equal(result2,"Custom");
               assert.equal(result3,"Latest");     
               assert.equal(result4,"Last 15 mins");
            });
        }
    }
});

