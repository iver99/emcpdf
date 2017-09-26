define('uifwk/@version@/js/widgets/htmlwidget/htmlwidget-impl',
["require", "knockout", "jquery", "ojs/ojcore", "uifwk/libs/@version@/js/ckeditor/ckeditor"],
        function (localrequire, ko, $) {
            function htmlWidgetViewModel(params) {
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
                self.htmlWidgetId = 'hahaha';
                var lang;
                try {
                    lang = requirejs.s.contexts._.config.config.i18n.locale;
                }catch(err) {
                    lang = $("html").attr("lang") ? $("html").attr("lang") : window.navigator.language;
                }
                var configOptions = {
                    language: lang,
                    startupFocus: false,
                    uiColor: "#FFFFFF",
                    linkShowAdvancedTab: false,
                    linkShowTargetTab: false,
                    
                    extraPlugins: 'sourcedialog',
                    removePlugins: 'elementspath', //remove default p element
    //                extraPlugins: 'stylesheetparser',
                    enterMode: CKEDITOR.ENTER_BR,
                    shiftEnterMode: CKEDITOR.ENTER_BR,
                    toolbar: [
                        { name: 'document', items: [ 'Sourcedialog' ] }
                    ],
                    allowedContent: 'a[!href]{*}; abbr[*]; acronym[*]; address[*]; area[*]; article[*]; aside[*]; audio[*]; b[*]; bdi[*]; bdo[*]; big[*]; blink[*]; blockquote[*]; body[*]; br[*]; button[*]; canvas[*]; caption[*]; center[*]; cite[*]; code[*]; col[*]; colgroup[*]; content[*]; data[*]; datalist[*]; dd[*]; decorator[*]; del[*]; details[*]; dfn[*]; dir[*]; div[*]{*}; dl[*]; dt[*]; element[*]; em[*]; fieldset[*]; figcaption[*]; figure[*]; font[*]; footer[*]; form[*]; h1[*]; h2[*]; h3[*]; h4[*]; h5[*]; h6[*]; head[*]; header[*]; hgroup[*]; hr[*]; html[*]; i[*]; img[*]{*}; input[*]; ins[*]; kbd[*]; label[*]; legend[*]; li[*]; main[*]; map[*]; mark[*]; marquee[*]; menu[*]; menuitem[*]; meter[*]; nav[*]; nobr[*]; ol[*]; optgroup[*]; option[*]; output[*]; p[*]; pre[*]; progress[*]; q[*]; rp[*]; rt[*]; ruby[*]; s[*]; samp[*]; section[*]; select[*]; shadow[*]; small[*]; source[*]; spacer[*]; span[*]; strike[*]; strong[*]; style[*]; sub[*]; summary[*]; sup[*]; table[*]; tbody[*]; td[*]; template[*]; textarea[*]; tfoot[*]; th[*]; thead[*]; time[*]; tr[*]; track[*]; tt[*]; u[*]; ul[*]; var[*]; video[*]; wbr'

                };
                
                $("#textEditor").attr("id", "textEditor_" + self.htmlWidgetId);
                $("#textEditor_" + self.htmlWidgetId).attr("contenteditable", "true");
                var editor = CKEDITOR.inline("textEditor_" + self.htmlWidgetId, configOptions);
                
                editor.on('instanceReady', function( evt ) {
                        var editor = evt.editor;

//                        alert(editor.filter.check( 'img' )); // -> false
//                        console.log( editor.filter.allowedContent );
//                        editor.setData( '<h1><i>Foo</i></h1><p class="left"><span>Bar</span> <a href="http://foo.bar">foo</a></p>' );
//                        editor.setData( '<a href="http://www.baidu.com" onclick="alert(\'hh\');">aaa</a>' );
//                                                editor.setReadOnly(true);

                        // Editor contents will be:
                        //'<p><i>Foo</i></p><p>Bar <a href="http://foo.bar">foo</a></p>'
                        if(!self.emptyContent()){
                            editor.setData(self.content());
                            self.content(editor.getData());
                        }
                        $('#textEditorWrapper_'+self.htmlWidgetId).hide();
                        $("#textEditor_" + self.htmlWidgetId).focus();
                    });
                
                editor.on("focus", function() {
                       this.setData(self.content()); 
                    });
                editor.on("blur", function () {
                        self.content(this.getData());
                        $('#textContentWrapper_'+self.htmlWidgetId).show();
                        $('#textEditorWrapper_'+self.htmlWidgetId).hide();
                    });

                self.showTextEditor = function(data, event) {
                    //If user clicks on hyperlink, do not enter edit mode.
                    if(event.target && event.target.tagName === "A") {
                        return true;
                    }
                    $('#textContentWrapper_'+self.htmlWidgetId).hide();
                    $('#textEditorWrapper_'+self.htmlWidgetId).show();
                    $("#textEditor_" + self.htmlWidgetId).focus();
                };
            }
            return htmlWidgetViewModel;
        });
