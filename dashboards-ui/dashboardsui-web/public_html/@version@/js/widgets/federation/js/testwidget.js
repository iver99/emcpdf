define(["knockout"],
        function (ko) {
            function TestWidgetViewModel(params) {
                var self = this;

                self.currentMode = ko.observable("Green Field Mode");
                self.updateMode = function() {
                    var fedMode = Builder.isRunningInFederationMode();
                    if (fedMode === true) {
                        self.currentMode("Federation Mode");
                    }
                };
            }
            return TestWidget;
        });