/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['jquery'], function($) {
    function Dispatcher() {
        var self = this;
        self.queue = [];

        self.registerEventHandler = function(event, handler) {
            if (!event || !handler){
                return;
            }
            if (!self.queue[event]){
                self.queue[event] = [];
            }
            if (self.queue[event].indexOf(handler) !== -1){
                return;
            }
            self.queue[event].push(handler);
            window.DEV_MODE && console.debug('Dashboard builder event registration. [Event]' + event + ' [Handler]' + handler);
        };

        self.delayResizeListenerQueue = [];
        self.firstResize = false;
        self.lastBuilderResizeExecTimestamp = null;
        
        self.initFirstResize = function() {
            self.firstResize = false;
        };
        
        self.triggerEvent = function(event, p1, p2, p3, p4) {
            if (event === "EVENT_BUILDER_RESIZE") {
                if (!self.firstResize && $("#globalBody").is(":visible")) {
                    console.debug("resize widget area to get initial position/size for widgets for the 1st time");
                    self.executeListenersImmediately(event, p1, p2, p3, p4);
                    self.lastBuilderResizeExecTimestamp = performance.now();
                    self.firstResize = true;
                }
                else {
                    var delayedEvent = {
                        event: event,
                        p1: p1,
                        p2: p2,
                        p3: p3,
                        p4: p4,
                        timestamp: performance.now()
                    };
                    self.delayResizeListenerQueue.push(delayedEvent);
                    setTimeout(function() {
                        self.executeDelayedEvent();
                    }, 500); // builder resize event will be handled after delay time, and might be ignored if there're repeated event later
                }
            }
            else
                self.executeListenersImmediately(event, p1, p2, p3, p4);
        };
        
        self.executeDelayedEvent = function() {
            var delayedEvent = self.delayResizeListenerQueue.shift();
            if (delayedEvent) {
                if (self.delayResizeListenerQueue.length > 0) {
                    console.debug("Delayed event is ignored as there're " + self.delayResizeListenerQueue.length + " new event(s) in delayed queue");
                } else {// execute the delay event now
                    if (self.lastBuilderResizeExecTimestamp === null || delayedEvent.timestamp > self.lastBuilderResizeExecTimestamp) {
                        var delay = performance.now() - delayedEvent.timestamp;
                        console.info("Execute delayed event for builder resize after delay of " + delay + "ms");
                        self.executeListenersImmediately(delayedEvent.event, delayedEvent.p1, delayedEvent.p2, delayedEvent.p3, delayedEvent.p4);
                        self.lastBuilderResizeExecTimestamp = delayedEvent.timestamp;
                    }
                    else {
                        console.debug("There's newer builder resize event (timestamp " + self.lastBuilderResizeExecTimestamp+ ") executed earlier than the delayed event (timestamp " + delayedEvent.timestamp + "), so ignore the delayed event");
                    }
                }
            } else {
                console.warn("Unexpected, after delay the delayevent is missing: " + delayedEvent + ", just ingore the event");
            }
        };
        
        self.executeListenersImmediately = function(event, p1, p2, p3, p4) {
            if (!event || !self.queue[event]){
                return;
            }
            for (var i = 0; i < self.queue[event].length; i++) {
                self.queue[event][i](p1, p2, p3, p4);
            }
        };
    }

    return {"Dispatcher": Dispatcher};
});
