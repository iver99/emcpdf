define(["require", "knockout", "jquery", "ojs/ojcore", "ckeditor"],
        function (localrequire, ko, $) {
            function textWidgetViewModel(params) {
                var self = this;
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
                var configOptions = {
                    language: lang,
                    toolbar: [
                        {name: 'styles', items: ['Font', 'FontSize']},
                        {name: 'basicStyles', items: ['Bold', 'Italic']},
                        {name: 'colors', items: ['TextColor']},
                        {name: 'paragraph', items: ['JustifyLeft', 'JustifyCenter', 'JustifyRight']},
                        {name: 'links', items: ['Link', 'Image']}
                    ],
                    removePlugins: 'elementspath,magicline',
                    startupFocus: false,
                    uiColor: "#FFFFFF",
                    linkShowAdvancedTab: false,
                    linkShowTargetTab: false,
                    enterMode: CKEDITOR.ENTER_BR

                };

                var editor;
                self.loadTextEditor = function () {
                    $("#textEditor").attr("id", "textEditor_" + self.textWidgetId);
                    $("#textEditor_" + self.textWidgetId).attr("contenteditable", "true");
                    editor = CKEDITOR.inline("textEditor_" + self.textWidgetId, configOptions);
                        
                    editor.on("instanceReady", function () {
                        this.setData(self.content());
                        $('#textEditorWrapper_'+self.textWidgetId).hide();
                        $("#textEditor_" + self.textWidgetId).focus();
                    });

                    editor.on("blur", function () {
                        self.content(this.getData());
                        $('#textContentWrapper_'+self.textWidgetId).show();
                        $('#textEditorWrapper_'+self.textWidgetId).hide();
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
                };
                
                self.showTextEditor = function() {
                    $('#textContentWrapper_'+self.textWidgetId).hide();
                    $('#textEditorWrapper_'+self.textWidgetId).show();
                    $("#textEditor_" + self.textWidgetId).focus();
                };
                
                params.tile.onDashboardItemChangeEvent = function(dashboardItemChangeEvent) {
                    //When receive "Edit" event from dashboard, show text widget in edit mode
                    if(dashboardItemChangeEvent && dashboardItemChangeEvent.tileChange && dashboardItemChangeEvent.tileChange.status === "POST_EDIT") {
                        self.showTextEditor();
                    }
                };
                
                var TimeFn = null;
                self.textWidgetDblClickHandler = function() {
                    clearTimeout(TimeFn);
                    self.showTextEditor();
                };
                
                self.textWidgetClickHandler = function(data, event) {
                    clearTimeout(TimeFn);
                    TimeFn = setTimeout(function() {
                        //If user clicks on hyperlink or image, open them instead of switching to edit mode
                        if(event.target && event.target.tagName === "A" && event.target.href) {
                            window.open(event.target.href);
                            return false;
                        }else if(event.target && event.target.tagName === "IMG" && event.target.src) {
                            window.open(event.target.src);
                            return false;
                        }
                        return true;
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
                
                self.loadTextEditor();
            }
            return textWidgetViewModel;
        });