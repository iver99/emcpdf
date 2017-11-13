define(["require", "knockout", "jquery", "ojs/ojcore", 'DOMPurify'],
        function (localrequire, ko, $, oj, DOMPurify) {
            function htmlWidgetViewModel(params) {
                var self = this;

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
                
                var config = {
                    ALLOWED_TAGS: ['a','abbr','acronym','address','area','article','aside','b','bdi','bdo','big','blockquote','br','br','caption','center','cite','code','col','colgroup','dd','del','del','dfn','dir','div','dl','dt','em','figcaption','figure','font','footer','h1','h2','h3','h4','h5','h6','header','hgroup','hr','i','img','ins','kbd','label','li','map','map','mark','menu','nav','ol','p','pre','q','rp','rt','ruby','s','samp','section','small','span','strike','strong','sub','sup','table','tbody','td','tfoot','th','thead','time','tr','tt','u','ul','var','wbr'],
                    ALLOWED_ATTR: ['abbr','align','alt','axis','bgcolor','border','cellpadding','cellspacing','class','clear','color','cols','colspan','compact','coords','dir','face','headers','height','hreflang','hspace','ismap','lang','language','nohref','nowrap','rel','rev','rows','rowspan','rules','scope','scrolling','shape','size','span','start','summary','tabindex','target','title','type','valign','value','vspace','width','background','cite','href','src','style'],
                    ALLOW_DATA_ATTR: false
                };
                $("#textEditor").attr("id", "textEditor_" + self.htmlWidgetId);
                $("#textEditor_" + self.htmlWidgetId).attr("contenteditable", "true");
                self.content(DOMPurify.sanitize(self.content(), config));
                self.previewHTMLData = function(){
                    var rawHtml = self.textAreaVal();
                    var filteredHtml = DOMPurify.sanitize(rawHtml, config);
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
                
                params.tile.onDashboardItemChangeEvent = function(dashboardItemChangeEvent) {
                    //When receive "Edit" event from dashboard, show html widget in edit mode
                    if(dashboardItemChangeEvent && dashboardItemChangeEvent.tileChange && dashboardItemChangeEvent.tileChange.status === "POST_EDIT") {
                        self.editHTMLData();
                    }
                };
            }
            return htmlWidgetViewModel;
        });
