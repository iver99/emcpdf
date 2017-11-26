define(['jquery'], function ($) {

    function getScrollbarWidth() {
        var $outer = $('<div>').css({visibility: 'hidden', width: 100, overflow: 'scroll'}).appendTo('body'),
            widthWithScroll = $('<div>').css({width: '100%'}).appendTo($outer).outerWidth();
        $outer.remove();
        return (100 - widthWithScroll);
    }
    
    function hasVerticalScrollBar(selector){    //judge whether a visible element has scroll bar
        var elm = $(selector+':visible');
        return (elm.css('overflow-y') === 'scroll' || elm.prop('scrollHeight') > elm.prop('clientHeight'));
    }

    return {
        getScrollbarWidth: getScrollbarWidth,
        hasVerticalScrollBar: hasVerticalScrollBar
    };
});