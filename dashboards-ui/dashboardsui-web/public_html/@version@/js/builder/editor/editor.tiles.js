/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['knockout',
        'jquery',
        'builder/builder.core'
    ],
    function(ko, $) {
        function TileItem(data) {
            var self = this;
            self.column = ko.observable();
            self.row = ko.observable();
            self.width = ko.observable();
            self.height = ko.observable();
            self.left = ko.observable(0);
            self.top = ko.observable(0);
            self.cssWidth = ko.observable(10);
            self.cssHeight = ko.observable(45);
            if(data.type && data.type === "TEXT_WIDGET") {
                self.hideTitle = ko.observable("true");
            }else {
                self.hideTitle = ko.observable("false");
            }
            self.outlineHightlight = ko.observable(false);
            self.cssStyle = ko.computed(function() {
                return "position: absolute; left: " + self.left() + "px; top: " + self.top() + "px; width: " + self.cssWidth() + "px; height: " + self.cssHeight() + "px;";
            });
            self.widgetCssStyle = ko.computed(function() {
                return "width: " + (self.cssWidth()-10) + "px; height: " + (self.cssHeight()-35-10) + "px;";
            });

            ko.mapping.fromJS(data, {include: ['column', 'row', 'width', 'height']}, this);
            data.title && (self.title = ko.observable(Builder.decodeHtml(data.title)));
            self.clientGuid = Builder.getGuid();
            self.sectionBreak = false;
            self.displayHeight = function() {
                return self.height * Builder.DEFAULT_HEIGHT;
            };

            if (Array.isArray(data.tileParameters)) {
                $.each(data.tileParameters, function (i, parameter) {
                    if (parameter.name === "DF_HIDE_TITLE") {
                        self.hideTitle(parameter.value);
                    }
                });
            }
        }

        Builder.registerModule(TileItem, 'TileItem');

        return {"TileItem": TileItem/*,
            "TextTileItem": TextTileItem*/};
    }
);
