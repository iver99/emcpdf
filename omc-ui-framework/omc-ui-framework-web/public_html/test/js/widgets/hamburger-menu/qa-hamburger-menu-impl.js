define(['ojs/ojcore',
    'knockout',
    'jquery',
    'uifwk/@version@/js/widgets/hamburger-menu/hamburger-menu-impl'
],function(oj, ko, $, hamburgerMenuViewModel){
    return {
        run: function(){
            QUnit.module("hamburgerMenuTest");
            QUnit.test("testHamburgerMenuLoadedWhenNoSubscribe", function(assert){
               var params = {"userName":"userName", "tenantName":"tenantName"};
               var hamburger = new hamburgerMenuViewModel(params);
               hamburger.subscribedApps = ["OMC"]; 
               assert.ok(hamburger.serviceMenuData);   
            });
        }
    }
});

