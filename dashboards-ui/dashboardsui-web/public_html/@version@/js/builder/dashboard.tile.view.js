/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['knockout',
        'jquery',
        'dfutil',
        'uifwk/js/util/screenshot-util',
        'ojs/ojcore',
        'ojs/ojtree',
        'ojs/ojvalidation',
        'ojs/ojknockout-validation',
        'ojs/ojbutton',
        'ojs/ojselectcombobox',
        'ojs/ojpopup',
        'builder/builder.core'
    ],
    
    function(ko, $, dfu, ssu, oj)
    {            
        function DashboardTilesView($b) {
            var self = this;
            self.dashboard = $b.dashboard;
            $b.registerObject(this, 'DashboardTilesView');
            
            self.resizeEventHandler = function(width, height, leftWidth, topHeight) {
                $('#tiles-col-container').css("left", leftWidth);
                $('#tiles-col-container').width(width - leftWidth);
                $('#tiles-col-container').height(height - topHeight);               
//                window.DEV_MODE && console.debug('tiles-col-container left set to: ' + leftWidth + ', width set:' + (width - leftWidth) + ', height set to: ' + (height - topHeight));
            };
            
            self.getTileElement = function(tile) {
                if (!tile || !tile.clientGuid)
                    return null;
                return $("#tile" + tile.clientGuid + ".dbd-widget");
            };
            
            self.disableDraggable = function(tile) {
                if (self.dashboard.systemDashboard()) {
                    console.log("Draggable not supported for OOB dashboard");
                    return;
                }
                var tiles = tile ? [self.getTileElement(tile)] : $(".dbd-widget");                
                for (var i = 0; i < tiles.length; i++) {
                    var target = $(tiles[i]);
                    if (target.is(".ui-draggable"))
                        target.draggable("disable");
                }
            };
            
            self.enableDraggable = function(tile) {
                if (self.dashboard.systemDashboard()) {
                    console.log("Draggable not supported for OOB dashboard");
                    return;
                }
                var tiles = tile ? [self.getTileElement(tile)] : $(".dbd-widget");                
                for (var i = 0; i < tiles.length; i++) {
                    var target = $(tiles[i]);
                    if (!target.is(".ui-draggable")) {
                        target.draggable({
                            zIndex: 30,
                            handle: ".tile-drag-handle"
                        });
                    }
                    else
                        target.draggable("enable");
                }
            };
                        
            self.enableMovingTransition = function() {
                if (!$('#widget-area').hasClass('dbd-support-transition'))
                    $('#widget-area').addClass('dbd-support-transition');
            };
            
            self.disableMovingTransition = function() {
                if ($('#widget-area').hasClass('dbd-support-transition'))
                    $('#widget-area').removeClass('dbd-support-transition');
            };
            
            self.postDocumentShow = function() {
                $("body").on("DOMSubtreeModified", function(e) {
                    if (e.currentTarget && e.currentTarget.nodeName !== "BODY")
                        return;
                    if ($(e.currentTarget.lastChild).hasClass('cke_chrome')) {
                        var mo = new MutationObserver(self.onTargetAttributesChange);
                        mo.observe(e.currentTarget.lastChild, {'attributes': true, attributeOldValue: true});
                        $(e.currentTarget.lastChild).prependTo('#tiles-col-container');
                    }
                });
            };
            
            self.onTargetAttributesChange = function(records) {
                if (records[0].attributeName === "style") {
                    var elem = records[0].target, target = $(elem);
                    if (!elem || elem.cacheLeft && elem.cacheLeft === target.css("left"))
                        return;
                    var top = parseInt(target.css("top")), left = parseInt(target.css("left"));
                    if (!isNaN(top) && !isNaN(left) && target.position() && target.position().left !== 0 && target.position().top !== 0) {
//                        window.DEV_MODE && console.debug("old target position: top-" + target.css("top") + ", left-" + target.css("left"));
                        target.css("top", top - $('#headerWrapper').outerHeight() - $('#head-bar-container').outerHeight() + $("#tiles-col-container").scrollTop());
                        target.css("left", left - $("#dbd-left-panel").width());
                        elem.cacheLeft = target.css("left");
//                        window.DEV_MODE && console.debug("new target position: top-" + target.css("top") + ", left-" + target.css("left"));
                    }
                }
            };
            
            $b.addBuilderResizeListener(self.resizeEventHandler);
            $b.addEventListener($b.EVENT_POST_DOCUMENT_SHOW, self.postDocumentShow);
        }
        
        Builder.registerModule(DashboardTilesView, 'DashboardTilesView');

        return DashboardTilesView;
    }
);

