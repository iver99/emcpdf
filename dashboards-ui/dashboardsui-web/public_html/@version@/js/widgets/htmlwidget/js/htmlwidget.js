define(["require", "knockout", "jquery", "ojs/ojcore", 'DOMPurify'],
        function (localrequire, ko, $, oj, DOMPurify) {
            function getGuid() {
                function securedRandom(){
                    var arr = new Uint32Array(1);
                    var crypto = window.crypto || window.msCrypto;
                    crypto.getRandomValues(arr);
                    var result = arr[0] * Math.pow(2,-32);
                    return result;
                }
                function S4() {
                    return parseInt(((1 + securedRandom()) * 0x10000)).toString(16).substring(1);
                }
                return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
            }

            function htmlWidgetViewModel(params) {
                var self = this;

                self.textAreaVal = ko.observable();
                self.editing = ko.observable(false);

                if(params.tile){
                    // branch for html editing on dashboard builder page with params.tile exists
                    if(!params.tile.content) {
                        params.tile.content = ko.observable();
                    }
                    self.content = params.tile.content;
                }else{
                    // branch for params.source without params.tile
                    self.content = ko.observable(params.source);
                }

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
                self.htmlWidgetId = params.tile?(params.tile.tileId ? ko.unwrap(params.tile.tileId) : params.tile.clientGuid):getGuid();
                
                var config = {
                    ALLOWED_TAGS: ['a','abbr','acronym','address','area','article','aside','b','bdi','bdo','big','blockquote','br','br','caption','center','cite','code','col','colgroup','dd','del','del','dfn','dir','div','dl','dt','em','figcaption','figure','font','footer','h1','h2','h3','h4','h5','h6','header','hgroup','hr','i','img','ins','kbd','label','li','map','map','mark','menu','nav','ol','p','pre','q','rp','rt','ruby','s','samp','section','small','span','strike','strong','sub','sup','table','tbody','td','tfoot','th','thead','time','tr','tt','u','ul','var','wbr'],
                    ALLOWED_ATTR: ['abbr','align','alt','axis','bgcolor','border','cellpadding','cellspacing','class','clear','color','cols','colspan','compact','coords','dir','face','headers','height','hreflang','hspace','ismap','lang','language','nohref','nowrap','rel','rev','rows','rowspan','rules','scope','scrolling','shape','size','span','start','summary','tabindex','target','title','type','valign','value','vspace','width','background','cite','href','src','style'],
                    ALLOW_DATA_ATTR: false
                };
                $("#htmlEditor").attr("id", "htmlEditor_" + self.htmlWidgetId);
                $("#htmlEditor_" + self.htmlWidgetId).attr("contenteditable", "true");
                self.content(DOMPurify.sanitize(self.content(), config));
                self.previewHTMLData = function(){
                    var rawHtml = self.textAreaVal();
                    var filteredHtml = DOMPurify.sanitize(rawHtml, config);
                    self.content(filteredHtml);
                    self.editing(false);
                };

                self.showTextEditor = function(data, event) {
                    self.editing(true);
                    $("#htmlEditor_" + self.htmlWidgetId).focus();
                };
                
                self.editHTMLData = function(data, event) {
                    self.textAreaVal(self.content());
                    self.editing(true);
                    $("#htmlEditor_" + self.htmlWidgetId).focus();
                };
                
                if(params.tile){
                    params.tile.onDashboardItemChangeEvent = function(dashboardItemChangeEvent) {
                        //When receive "Edit" event from dashboard, show html widget in edit mode
                        if(dashboardItemChangeEvent && dashboardItemChangeEvent.tileChange && dashboardItemChangeEvent.tileChange.status === "POST_EDIT") {
                            self.editHTMLData();
                        }
                    };
                }
            }
            return htmlWidgetViewModel;
        });
