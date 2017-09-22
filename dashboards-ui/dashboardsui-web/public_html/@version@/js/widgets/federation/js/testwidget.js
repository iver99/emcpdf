define(["knockout"],
        function (ko) {
            function TestWidgetViewModel(params) {
                var self = this;

                self.federationMode = ko.observable(Builder.isRunningInFederationMode());
                self.currentModeText = ko.observable(self.federationMode() === true ? "Federation Mode" : "Green Field Mode");
            }
            return TestWidgetViewModel;
        });