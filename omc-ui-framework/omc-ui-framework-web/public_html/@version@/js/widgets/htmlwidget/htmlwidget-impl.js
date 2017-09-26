define('uifwk/@version@/js/widgets/htmlwidget/htmlwidget-impl',
["require", "knockout", "jquery", "ojs/ojcore", 'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg', "uifwk/libs/@version@/js/DOMPurify/purify"],
        function (localrequire, ko, $, oj, nls, DOMPurify) {
            function htmlWidgetViewModel(params) {
                var self = this;

                self.HTML_WIDGET_PROMPT_STRING = nls.HTML_WIDGET_PROMPT_STRING;
                self.textAreaVal = ko.observable();
                self.editing = ko.observable(false);
                if(!params.tile.content) {
                    params.tile.content = ko.observable();
                }
                self.content = params.tile.content;
                self.emptyContent = ko.computed(function() {
                    if(!self.content()) {
                        return true;
                    }
                    var content = self.content().replace(/(?:^(?:&nbsp;)+)|(?:(?:&nbsp;)+$)/g, '');
                    if(content) {
                        return false;
                    }else {
                        self.content(content);
                        return true;
                    }
                });
                self.htmlWidgetId = 'hahaha';
                
                $("#textEditor").attr("id", "textEditor_" + self.htmlWidgetId);
                $("#textEditor_" + self.htmlWidgetId).attr("contenteditable", "true");
                self.content(DOMPurify.sanitize(self.content()));
                self.previewHTMLData = function(){
                    var rawHtml = self.textAreaVal();
                    var filteredHtml = DOMPurify.sanitize(rawHtml);
                    self.content(filteredHtml);
                    self.editing(false);
                };

                self.showTextEditor = function(data, event) {
                    self.editing(true);
                    $("#textEditor_" + self.htmlWidgetId).focus();
                };
                
                self.editHTMLData = function(data, event) {
                    self.textAreaVal(self.content());
                    self.editing(true);
                    $("#textEditor_" + self.htmlWidgetId).focus();
                };
            }
            return htmlWidgetViewModel;
        });
