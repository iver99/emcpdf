/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
    'jquery',
    'dfutil',
    'idfbcutil',
    'uifwk/js/util/screenshot-util',
    'ojs/ojcore',
    'builder/tool-bar/edit.dialog',
    'builder/tool-bar/duplicate.dialog',
    'uifwk/js/util/preference-util',
    'mobileutil',
    'builder/builder.core'
],
    function (ko, $, dfu, idfbcutil, ssu, oj, ed, dd, pfu,mbu) {
        // dashboard type to keep the same with return data from REST API
        var SINGLEPAGE_TYPE = "SINGLEPAGE";

        function DashboardsetToolBarModel(dsbId) {                     
            var self = this;
            var dashboardsetId = dsbId.split(/[,]/);
            var startDashboardId = Number(location.search.split(/start=/)[1]) || Number(dashboardsetId[0]);

            self.dashboardsetItems = [];

            self.selectedDashboardItem = ko.observable();
                        
            self.selectedDashboardItem.subscribe(function (selected) {
                console.log("current selectselectedDashboardItemed dashboard: %o", selected);
            });

            InitializeDashboardset(dashboardsetId);

            //later will receive the info from the backend
            self.dashboardsetInfo = {"name":ko.observable("Middleware Dashboards"),"description":ko.observable("first dashboard set"),"type":getNlsString("DBSSET_BUILDER_DASHBOARDSET")};
            self.dashboardsetConfig={"isCreator":true,"refresh":ko.observable(false),"refreshOffIcon":ko.observable("dbd-icon-check"),"refreshOnIcon":ko.observable("dbd-noselected"),"share":ko.observable(true),"shareLabel":ko.observable(getNlsString("COMMON_TEXT_SHARE")),"addFavorite":ko.observable(true),"favoriteIcon":ko.observable("fa-star"),"favoriteLabel":ko.observable(getNlsString("DBS_BUILDER_BTN_FAVORITES_ADD")),"setHome":ko.observable(true),"homeLabel":ko.observable(getNlsString("DBS_BUILDER_BTN_HOME_SET")),"homeIcon":ko.observable("dbd-toolbar-icon-home")};

            self.isDashboardSet = ko.observable(dashboardsetId.length > 1);            
            self.isMobileDevice = ((new mbu()).isMobile === true ? 'true' : 'false');
            
            self.dashboardsetConfigMenu =function(event,data){
                var configId = data.item.attr('id');
                switch (configId) {
                    case 'dbs-edit':
                        $('#changeDashboardsetInfo').ojDialog("open");
                        break;
                    case 'refresh-off':
                        self.dbConfigMenuClick.refreshDbs(self);       
                        break;
                    case 'refresh-time':
                        self.dbConfigMenuClick.refreshDbs(self);
                        break;
                    case 'dbs-share':
                         self.dbConfigMenuClick.shareDbs(self);
                        break;
                    case 'dbs-print':
                        //TO DO:print all the element on dashboard set
                        window.print();
                        break;
                    case 'dbs-favorite' :
                        self.dbConfigMenuClick.favoriteDbs(self);
                        break;
                    case 'dbs-home':
                        self.dbConfigMenuClick.homeDbs(self);
                        break;
                    case 'dbs-delete':
                        $('#deleteDashboardset').ojDialog("open");
                        break;
                    default:
                        break;
                }
            };
                      

            self.addNewDashboard = function (data, event) {
                var newDashboardItem = {type: "new", name: ko.observable("Dashboard"), dashboardId: Builder.getGuid()};
                self.dashboardsetItems.push(newDashboardItem);
                addNewTab(newDashboardItem.name(),newDashboardItem.dashboardId);
                $("#dbd-tabs-container").ojTabs({"selected": 'dashboardTab-' + newDashboardItem.dashboardId});
                $($('.other-nav').find(".oj-tabs-close-icon")).attr("title", getNlsString('DBSSET_BUILDER_REMOVE_DASHBOARD'));
                self.selectedDashboardItem(newDashboardItem);
            };
            
            /**
             * replace the dashboard list page to the selected dashboard
             * @param {type} dashboardGuid dashboard list page id
             * @param {type} selectedDashboard the dashboard user picked
             * @returns {undefined}
             */
            self.pickDashboard = function(dashboardPickerId, selectedDashboard) {
                var removeResult=findRemoveTab(self.dashboardsetItems,dashboardPickerId);
                
                if (removeResult.removeIndex > -1) {  
                    removeTargetTab(removeResult.removeItem);
                    addNewTab(selectedDashboard.name,selectedDashboard.dashboardId); 
                    self.dashboardsetItems.splice(removeResult.removeIndex, 1, selectedDashboard);
                    self.selectedDashboardItem(selectedDashboard);
                    $("#dbd-tabs-container").ojTabs({"selected": 'dashboardTab-' + selectedDashboard.dashboardId});
                    $($('.other-nav').find(".oj-tabs-close-icon")).attr("title", getNlsString('DBSSET_BUILDER_REMOVE_DASHBOARD'));
                }
            };
                    
            self.dbConfigMenuClick = new dbConfigMenuClick();  
            
            self.dashboardsetMenu = ko.observableArray([
                {
                    "label": getNlsString("COMMON_BTN_EDIT"),
                    "url": "#",
                    "id": "dbs-edit",
                    "icon": "fa-pencil",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": false,       
                    "showOnMobile": false,
                    "showOnViewer":false, 
                    "visibility":visibilityOnDifDevice(false,false),
                    "subMenu": []
                },
                {
                    "label":  getNlsString("DBS_BUILDER_TILE_REFRESH"),
                    "url": "#",
                    "id": "dbs-refresh",
                    "icon": "dbd-icon-refresh",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": true,    
                    "showOnMobile": true,
                    "showOnViewer":true,
                    "visibility":visibilityOnDifDevice(true,true),
                    "subMenu": [{
                            "label": getNlsString("DBS_BUILDER_AUTOREFRESH_OFF"),
                            "url": "#",
                            "id": "refresh-off",
                            "icon": self.dashboardsetConfig.refreshOnIcon,
                            "title": "",
                            "disabled": "",
                            "endOfGroup": false,    
                            "showOnMobile": true,
                            "showOnViewer":true,
                            "visibility":visibilityOnDifDevice(true,true),
                            "subMenu": []
                        }, {
                            "label": getNlsString("DBS_BUILDER_AUTOREFRESH_ON"),
                            "url": "#",
                            "id": "refresh-time",
                            "icon": self.dashboardsetConfig.refreshOffIcon,
                            "title": "",
                            "disabled": "",
                            "endOfGroup": false,
                            "showOnMobile": true,
                            "showOnViewer":true,
                            "visibility":visibilityOnDifDevice(true,true),
                            "subMenu": []
                        }]
                },
                {
                    "label":self.dashboardsetConfig.shareLabel,
                    "url": "#",
                    "id": "dbs-share",
                    "icon": "dbd-icon-users",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": false,    
                    "showOnMobile": true,
                    "showOnViewer":false,
                    "visibility":visibilityOnDifDevice(true,false),
                    "subMenu": []
                },
                {
                    "label": getNlsString("DBSSET_BUILDER_PRINT_ALL"),
                    "url": "#",
                    "id": "dbs-print",
                    "icon": "fa-print",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": false,         
                    "showOnMobile": true,          
                    "showOnViewer":true,
                    "visibility":visibilityOnDifDevice(true,true),
                    "subMenu": []
                },
                {
                    "label": self.dashboardsetConfig.favoriteLabel,
                    "url": "#",
                    "id": "dbs-favorite",
                    "icon": self.dashboardsetConfig.favoriteIcon,
                    "title": "",
                    "disabled": "",
                    "endOfGroup": false,
                    "showOnMobile": true,
                    "showOnViewer":true,
                    "visibility":visibilityOnDifDevice(true,true),
                    "subMenu": []
                },
                {
                    "label": self.dashboardsetConfig.homeLabel,
                    "url": "#",
                    "id": "dbs-home",
                    "icon": self.dashboardsetConfig.homeIcon,
                    "title": "",
                    "disabled": "",
                    "endOfGroup": true,
                    "showOnMobile": true,
                    "showOnViewer":true,
                    "visibility":visibilityOnDifDevice(true,true),
                    "subMenu": []
                },
                {
                    "label": getNlsString("COMMON_BTN_DELETE"),
                    "url": "#",
                    "id": "dbs-delete",
                    "icon": "dbd-toolbar-icon-delete",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": false,
                    "showOnMobile": false,
                    "showOnViewer":false,
                    "visibility":visibilityOnDifDevice(false,false),
                    "subMenu": []
                }
            ]);


            function InitializeDashboardset(dashboardsetId) {
                for (var index in dashboardsetId) {
                    var singleDashboardId = dashboardsetId[index];
                    Builder.loadDashboard(singleDashboardId, function (dashboard) {
                        var singleDashboardItem = new dashboardItem(dashboard);
                        if (singleDashboardItem.dashboardId === startDashboardId) {
                            self.selectedDashboardItem(singleDashboardItem);
                            self.dashboardsetItems.push(singleDashboardItem);
                            addNewTab(singleDashboardItem.name(),singleDashboardItem.dashboardId);
                            $("#dbd-tabs-container").ojTabs({"selected": 'dashboardTab-' +singleDashboardItem.dashboardId});
                        } else {
                            self.dashboardsetItems.push(singleDashboardItem);
                            addNewTab(singleDashboardItem.name(),singleDashboardItem.dashboardId);
                        }
                        $($('.other-nav').find(".oj-tabs-close-icon")).attr("title", getNlsString('DBSSET_BUILDER_REMOVE_DASHBOARD'));
                    });
                }
            }

            function dashboardItem(obj) {
                var self = this;
                self.dashboardId = obj.id();
                self.name = ko.observable(obj.name());
                self.discription = obj.description && obj.description();
                self.type = "included";
                self.raw = obj;  
            }
            
            function addNewTab(tabName,dashboardId) {                
                $( "#dbd-tabs-container" ).ojTabs( "addTab", 
                         {
                           "tab" : $("<li class='other-nav' id='dashboardTab-"+dashboardId+"'><span class='tabs-name'>"+tabName+"</span></li>"),
                           "content" : $("<div class='dbd-info other-nav-info' id='dashboardTabInfo-"+dashboardId+"'></div>"),
                           "index":-1
                         } );
            }
            
            function findRemoveTab(dashboardsetItems,dashboardPickerId){
                var dashboardToRemove = -1;
                var dashboardToRemoveItem;
                ko.utils.arrayForEach(dashboardsetItems, function (item, index) {
                    if (item.dashboardId === dashboardPickerId) {
                        dashboardToRemove = index;
                        dashboardToRemoveItem=item;
                    }
                });
                var returnResult ={"removeIndex":dashboardToRemove,"removeItem":dashboardToRemoveItem};
                return returnResult;
            }
            
            function removeTargetTab(removeTargetItem){
                $('#dashboardTab-'+removeTargetItem.dashboardId).remove();
                $('#dashboardTabInfo-'+removeTargetItem.dashboardId).remove();
            }

            function dbConfigMenuClick (){
                var self=this;
                self.saveDbsDescription=function(){
                    var self=this;
                    var nameEdit=$('.dbs-name input').val();
                    var descriptionEdit=$('.dbs-description textarea').val();
                    self.dashboardsetInfo.name(nameEdit);
                    self.dashboardsetInfo.description(descriptionEdit);
                    $('#changeDashboardsetInfo').ojDialog("close");         
                };   
                self.shareDbs = function(dbsToolBar){
                    dbsToolBar.dashboardsetConfig.share(!dbsToolBar.dashboardsetConfig.share());
                        if(dbsToolBar.dashboardsetConfig.share()){
                           dbsToolBar.dashboardsetConfig.shareLabel(getNlsString("COMMON_TEXT_SHARE")); 
                        }else{
                           dbsToolBar.dashboardsetConfig.shareLabel(getNlsString("COMMON_TEXT_UNSHARE"));  
                        }
                };
                self.favoriteDbs = function (dbsToolBar) {
                    dbsToolBar.dashboardsetConfig.addFavorite(!dbsToolBar.dashboardsetConfig.addFavorite());
                    if (dbsToolBar.dashboardsetConfig.addFavorite()) {
                        dbsToolBar.dashboardsetConfig.favoriteIcon("fa-star");
                        dbsToolBar.dashboardsetConfig.favoriteLabel(getNlsString("DBS_BUILDER_BTN_FAVORITES_ADD"));
                    } else {
                        dbsToolBar.dashboardsetConfig.favoriteIcon("fa-star-o");
                        dbsToolBar.dashboardsetConfig.favoriteLabel(getNlsString("DBS_BUILDER_BTN_FAVORITES_REMOVE"));
                    }
                };
                self.homeDbs = function(dbsToolBar){
                     dbsToolBar.dashboardsetConfig.setHome(!dbsToolBar.dashboardsetConfig.setHome());
                        if(dbsToolBar.dashboardsetConfig.setHome()){
                           dbsToolBar.dashboardsetConfig.homeIcon("dbd-toolbar-icon-home"); 
                           dbsToolBar.dashboardsetConfig.homeLabel(getNlsString("DBS_BUILDER_BTN_HOME_SET")); 
                        }else{
                           dbsToolBar.dashboardsetConfig.homeIcon("dbd-toolbar-icon-home");  
                           dbsToolBar.dashboardsetConfig.homeLabel(getNlsString("DBS_BUILDER_BTN_HOME_REMOVE")); 
                        }
                };
                self.refreshDbs= function(dbsToolBar){
                    dbsToolBar.dashboardsetConfig.refresh(!dbsToolBar.dashboardsetConfig.refresh());
                    if (dbsToolBar.dashboardsetConfig.refresh()) {
                        dbsToolBar.dashboardsetConfig.refreshOnIcon("dbd-icon-check");
                        dbsToolBar.dashboardsetConfig.refreshOffIcon("dbd-noselected");

                    } else {
                        dbsToolBar.dashboardsetConfig.refreshOnIcon("dbd-noselected");
                        dbsToolBar.dashboardsetConfig.refreshOffIcon("dbd-icon-check");
                    }
                };
                self.deleteDbs = function(){
                    //TODO:ajax to delete
                     window.location = document.location.protocol + '//' + document.location.host + '/emsaasui/emcpdfui/home.html';
                     $('#deleteDashboardset').ojDialog("close");  
                };
                self.cancelDeleteDbs= function(){
                     $('#deleteDashboardset').ojDialog("close");   
                };       
            };
            
            function visibilityOnDifDevice(showOnMobile,showOnViewer){
                if(self.dashboardsetConfig.isCreator){
                    if(self.isMobileDevice==='true'){
                        return showOnMobile;
                    }else{
                        return true;
                    }
                }else{
                        return showOnViewer;                 
                }
            };
        
            $( "#dbd-tabs-container" ).on( "ojbeforeremove", function( event, ui ) {
                var removeDashboardId = Number(ui.tab.attr('id').split(/dashboardTab-/)[1])||(ui.tab.attr('id').split(/dashboardTab-/)[1])
                
                var removeResult=findRemoveTab(self.dashboardsetItems,removeDashboardId);
                
                if (removeResult.removeIndex > -1) {
                     var currentShowIndex=$('.other-nav').index(ui.tab); 
                     self.dashboardsetItems.splice(removeResult.removeIndex, 1);
                     removeTargetTab(removeResult.removeItem);
                    if (ui.tab.hasClass('oj-selected')) {
                        if (self.dashboardsetItems.length === currentShowIndex && self.dashboardsetItems.length !== 0) {
                            $("#dbd-tabs-container").ojTabs({"selected": 'dashboardTab-' + self.dashboardsetItems[currentShowIndex - 1].dashboardId});
                            self.selectedDashboardItem(self.dashboardsetItems[currentShowIndex - 1]);
                        } else if (self.dashboardsetItems.length === 0) {                          
                            self.addNewDashboard();
                        }
                        else {   
                            $("#dbd-tabs-container").ojTabs({"selected": 'dashboardTab-' + self.dashboardsetItems[currentShowIndex].dashboardId});
                            self.selectedDashboardItem(self.dashboardsetItems[currentShowIndex]);
                        }
                    } 
                }              
            } );
            
            $("#dbd-tabs-container").on("ojdeselect", function (event, ui) {
                if (typeof (event.originalEvent) !== 'undefined') {
                    var selectedDashboardId=Number(event.originalEvent.currentTarget.id.split(/dashboardTab-/)[1])||event.originalEvent.currentTarget.id.split(/dashboardTab-/)[1];
                    ko.utils.arrayForEach(self.dashboardsetItems, function (item, index) {
                        if (item.dashboardId === selectedDashboardId) {
                            self.selectedDashboardItem(item);
                        }
                    });
                }
            });
            
        }

        Builder.registerModule(DashboardsetToolBarModel, 'DashboardsetToolBarModel');
        return DashboardsetToolBarModel;
    }
);

