/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['knockout',
        'jquery',
        'dfutil',
        'uiutil',
        'uifwk/js/util/screenshot-util',
        'ojs/ojcore',
//        'builder/builder.jet.partition',
        'builder/builder.core'
    ],

    function(ko, $, dfu, uiutil, ssu, oj)
    {

        function DashboardTilesView($b) {
            var self = this;
            self.dashboard = $b.dashboard;
            self.scrollbarWidth = uiutil.hasVerticalScrollBar('.tiles-col-container')?uiutil.getScrollbarWidth():0;

            $b.registerObject(this, 'DashboardTilesView');

            self.resizeEventHandler = function(width, height, leftWidth, topHeight) {
                $b.findEl('.tiles-col-container').width(width);
                $b.findEl('.tiles-col-container').height(height - topHeight);
                self.scrollbarWidth = uiutil.hasVerticalScrollBar('.tiles-col-container')?uiutil.getScrollbarWidth():0;
                $b.findEl('.df-computed-content-width').width(width - leftWidth - self.scrollbarWidth);
            };

            self.getTileElement = function(tile) {
                if (!tile || !tile.clientGuid){
                    return null;
                }
                return $("#tile" + tile.clientGuid + ".dbd-widget");
            };

            self.disableDraggable = function(tile) {
                if (self.dashboard.systemDashboard()) {
                    console.log("Draggable not supported for OOB dashboard");
                    return;
                }
                var tiles = tile ? [self.getTileElement(tile)] : $b.findEl(".dbd-widget");
                for (var i = 0; i < tiles.length; i++) {
                    var target = $(tiles[i]);
                    if (target.is(".ui-draggable")){
                        target.draggable("disable");
                    }
                }
            };

            self.enableDraggable = function(tile) {
                if (self.dashboard.systemDashboard()) {
                    console.log("Draggable not supported for OOB dashboard");
                    return;
                }
                var tiles = tile ? [self.getTileElement(tile)] : $b.findEl(".dbd-widget");
                for (var i = 0; i < tiles.length; i++) {
                    var target = $(tiles[i]);
                    if (!target.is(".ui-draggable")) {
                        target.draggable({
                            zIndex: 30,
                            handle: ".tile-drag-handle",
                            opacity: 0.6,
                            stack: ".dbd-tile-in-dragging"
                        });
                    }
                    else{
                        target.draggable("enable");
                    }
                }
            };

            self.enableMovingTransition = function() {
                if (!$b.findEl('.widget-area').hasClass('dbd-support-transition')){
                    $b.findEl('.widget-area').addClass('dbd-support-transition');
                }
            };

            self.disableMovingTransition = function() {
                if ($b.findEl('.widget-area').hasClass('dbd-support-transition')){
                    $b.findEl('.widget-area').removeClass('dbd-support-transition');
                }
            };

            self.postDocumentShow = function() {
                $("body").on("DOMSubtreeModified", function(e) {
                    if (e.currentTarget && e.currentTarget.nodeName !== "BODY"){
                        return;
                    }
                    if ($(e.currentTarget.lastChild).hasClass('cke_chrome')) {
                        var mo = new MutationObserver(self.onTargetAttributesChange);
                        mo.observe(e.currentTarget.lastChild, {'attributes': true, attributeOldValue: true});
                        $(e.currentTarget.lastChild).prependTo($b.findEl('.tiles-col-container'));
                    }
                });
            };

            self.onTargetAttributesChange = function(records) {
                if (records[0].attributeName === "style") {
                    var elem = records[0].target, target = $(elem);
                    if (!elem || elem.cacheLeft && elem.cacheLeft === target.css("left")){
                        return;
                    }
                    var top = parseInt(target.css("top")), left = parseInt(target.css("left"));
                    if (!isNaN(top) && !isNaN(left) && target.position() && target.position().left !== 0 && target.position().top !== 0) {
                        target.css("top", top - $('#headerWrapper').outerHeight() - $b.findEl('.head-bar-container').outerHeight() + $b.findEl(".tiles-col-container").scrollTop());
                        target.css("left", left - $b.findEl(".dbd-left-panel").width());
                        elem.cacheLeft = target.css("left");
                    }
                }
            };

            $b.addBuilderResizeListener(self.resizeEventHandler);
//            $b.addEventListener($b.EVENT_POST_DOCUMENT_SHOW, self.postDocumentShow);
        }

        Builder.registerModule(DashboardTilesView, 'DashboardTilesView');

        return DashboardTilesView;
    }
);

