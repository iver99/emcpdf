define(["require", "knockout", "jquery", "ojs/ojcore", "DOMPurify", "ojs/ojtabs", "ckeditor"],
        function (localrequire, ko, $, oj, DOMPurify) {
            function textWidgetViewModel(params) {
                var self = this;
                self.isEditing = ko.observable(false);
                self.disabledTabs = ko.observableArray([1]);
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
                self.textWidgetId = params.tile.tileId ? ko.unwrap(params.tile.tileId) : params.tile.clientGuid;
                var lang;
                try {
                    lang = requirejs.s.contexts._.config.config.i18n.locale;
                }catch(err) {
                    lang = $("html").attr("lang") ? $("html").attr("lang") : window.navigator.language;
                }
                
                var config = {
                    ALLOWED_TAGS: ['a','abbr','acronym','address','area','article','aside','b','bdi','bdo','big','blockquote','br','br','caption','center','cite','code','col','colgroup','dd','del','del','dfn','dir','div','dl','dt','em','figcaption','figure','font','footer','h1','h2','h3','h4','h5','h6','header','hgroup','hr','i','img','ins','kbd','label','li','map','map','mark','menu','nav','ol','p','pre','q','rp','rt','ruby','s','samp','section','small','span','strike','strong','sub','sup','table','tbody','td','tfoot','th','thead','time','tr','tt','u','ul','var','wbr'],
                    ALLOWED_ATTR: ['abbr','align','alt','axis','bgcolor','border','cellpadding','cellspacing','class','clear','color','cols','colspan','compact','coords','dir','face','headers','height','hreflang','hspace','ismap','lang','language','nohref','nowrap','rel','rev','rows','rowspan','rules','scope','scrolling','shape','size','span','start','summary','tabindex','target','title','type','valign','value','vspace','width','background','cite','href','src','style'],
                    ALLOW_DATA_ATTR: false
                };
                
                var allowedContent = "";
                var allowedAttrs = config.ALLOWED_ATTR.join();
                var allowedTags = config.ALLOWED_TAGS.join(" ");
                //Below allowed content rules allows: all tags in ALLOWED_TAGS, with attirbues in ALLOWED_ATTR, with all styles and classes.
                allowedContent = allowedTags + "; *[" + allowedAttrs + "]{*}(*)";
                
                var configOptions = {
                    language: lang,
                    toolbar: [
                        {name: 'styles', items: ['Font', 'FontSize']},
                        {name: 'basicStyles', items: ['Bold', 'Italic']},
                        {name: 'colors', items: ['TextColor']},
                        {name: 'paragraph', items: ['JustifyLeft', 'JustifyCenter', 'JustifyRight']},
                        {name: 'links', items: ['Link', 'Image']}
                    ],
                    //Below 2 configs will remove bottom bar in ckeditor
                    removePlugins: 'elementspath,magicline',
                    resize_enabled: false,
                    startupFocus: true,
                    uiColor: "#FFFFFF",
                    linkShowAdvancedTab: false,
                    linkShowTargetTab: false,
                    enterMode: CKEDITOR.ENTER_BR,
                    allowedContent: allowedContent
                };

                var editor;
                self.loadTextEditor = function () {
                    if(self.isCKEditorInited) {
                        editor.focus();
                        return;
                    }
                    editor = CKEDITOR.replace("textEditor_" + self.textWidgetId, configOptions);
                        
                    editor.on("instanceReady", function () {
                        self.disabledTabs([]);
                        self.setTextEditorContentHeight();
                        this.setData(self.content());
                    });

                    editor.on("blur", function () {
                        self.content(this.getData());
                        if(self.switchToHtmlMode) {
                            self.switchToHtmlMode = false;
                        }else {
                            self.showRenderedMode();
                        }
                    });
                    
                    editor.on("focus", function() {
                       this.setData(self.content()); 
                    });
                    //"change" event needs to be added even if we have "key" event as "add link/image" won't fire "key" event
                    editor.on("change", function() {
                       self.content(this.getData());
                    });
                    //fix "can't input Chinese" issue in IE
                    editor.on("key", function() {
                        var _self = this;
                        setTimeout(function(){self.content(_self.getData());}, 100);
                    });
                    self.isCKEditorInited = true;
                };
                
                self.setTextEditorContentHeight = function() {
                    var textEditorHeight = $("#cke_textEditor_" + self.textWidgetId + " .cke_inner").height();
                    var textEditorToolbarHeight = $("#cke_textEditor_" + self.textWidgetId + " .cke_top").outerHeight();
                    var height = textEditorHeight - textEditorToolbarHeight;
                    $("#cke_textEditor_" + self.textWidgetId + " .cke_contents").css("height", height + "px");
                };
                
                self.showTextEditor = function() {
                    self.isEditing(true);
                    setTimeout(function() {
                        self.loadTextEditor();
                    }, 0);
                };
                
                self.showRenderedMode = function() {                   
                    self.isEditing(false);
                    //When setting isEditing to false, editor in html will be removed
                    //Next time when entering editing mode, need to initiaize ckeditor self.loadTextEditor
                    self.isCKEditorInited = false;
                    self.disabledTabs([1]);
                };
                
                self.showHtmlEditor = function() {
                    self.isEditing(true);
                    setTimeout(function() {
                        $("#htmlEditor_" + self.textWidgetId).focus();
                    }, 0);
                };

                $("#htmlEditor").attr("id", "htmlEditor_" + self.textWidgetId);
                $("#htmlEditor_" + self.textWidgetId).attr("contenteditable", "true");
                self.previewHTMLData = function(){
                    //Use "setTimeout" to make sure html widget "blur" event is later than "Back to text widget" "click" event
                    setTimeout(function() {
                        
                        var rawHtml = self.content();
                        var filteredHtml = DOMPurify.sanitize(rawHtml, config);
                        self.content(filteredHtml);
                        
                        if(self.switchToTextMode) {
                            //"blur" event triggered by clicking "Back to text widget button"
                            self.switchToTextMode = false;
                        }else {
                            //"blur" event triggered by clicking outside widget
                            self.showRenderedMode();
                        }
                    }, 300);
                    
                };
                
                self.editModeChangehandler = function(event, data) {
                    if(data.value === "textMode") {
                        self.switchToTextMode = true;
                        self.showTextEditor();
                    }else if(data.value === "htmlMode") {
                        self.switchToHtmlMode = true;
                        self.showHtmlEditor();
                    }
                };
                
                params.tile.onDashboardItemChangeEvent = function(dashboardItemChangeEvent) {
                    //When receive "Edit" event from dashboard, show text widget in edit mode
                    if(dashboardItemChangeEvent && dashboardItemChangeEvent.tileChange) {
                        if(dashboardItemChangeEvent.tileChange.status === "POST_EDIT") {
                            self.showTextEditor();
                        }else if($.inArray(dashboardItemChangeEvent.tileChange.status, ["POST_WIDER", "POST_NARROWER", "POST_TALLER", "POST_SHORTER"])) {
                            self.setTextEditorContentHeight();
                        }
                    }
                };
                
                var TimeFn = null;
                self.textWidgetDblClickHandler = function() {
                    if(!params.tile.editDisabled()) {
                        clearTimeout(TimeFn);
                        self.showTextEditor();
                    }
                };
                
                self.textWidgetClickHandler = function(data, event) {
                    clearTimeout(TimeFn);
                    TimeFn = setTimeout(function() {
                        //If user clicks on hyperlink or image, open them instead of switching to edit mode
                        if(event.target && event.target.tagName === "A" && event.target.href) {
                            //Open link in new tab. Open email in current tab
                            if(event.target.href.startsWith("mailto:")) {
                                window.open(event.target.href, "_self");
                                return false;
                            }else {
                                window.open(event.target.href);
                                return false;
                            }
                        }else if(event.target && event.target.tagName === "IMG" && event.target.src) {
                            var parentA = $(event.target).closest("a", $("#textContentWrapper_" + self.textWidgetId));
                            //If image is wrapped by <a>, open the link. Otherwise, open the image
                            if(parentA && parentA.attr("href")) {
                                window.open(parentA.attr("href"));
                                return false;
                            }else {
                                window.open(event.target.src);
                                return false;
                            }
                        }
                    }, 300);
                    
                }
                
                CKEDITOR.on("dialogDefinition", function(ev) {
                    var dialogName = ev.data.name;
                    var dialogDefinition = ev.data.definition;
                    
                    if(dialogName === "image") {
                        //hide "Link" and "Advanced" Tab
                        dialogDefinition.removeContents("Link");
                        dialogDefinition.removeContents("advanced");
                        
                        //set width and height for image dialog
                        dialogDefinition.width = 420;
                        dialogDefinition.height = 150;
                        
                        var imageInfoTab = dialogDefinition.getContents("info");
                        //hide other boxes inside "info" Tab
                        imageInfoTab.get("htmlPreview").style = "display: none";
                        imageInfoTab.get("txtWidth").style = "display: none";
                        imageInfoTab.get("txtHeight").style = "display: none";
                        imageInfoTab.get("ratioLock").style = "display: none";
                        imageInfoTab.get("txtBorder").style = "display: none";
                        imageInfoTab.get("txtHSpace").style = "display: none";
                        imageInfoTab.get("txtVSpace").style = "display: none";
                        imageInfoTab.get("cmbAlign").style = "display: none";
                        
                    }else if(dialogName === "link") {
                        var linkInfoTab = dialogDefinition.getContents("info");
                        var linkType = linkInfoTab.get("linkType");
                        //update Link Type items: only keep URL and E-mail.
                        linkType.items = [["URL", "url"], ["E-mail", "email"]];
                    }
                });
            }
            return textWidgetViewModel;
        });