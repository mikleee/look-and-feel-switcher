(function () {
    angular.module('ts-message', [])
        .controller('ts-messageController', ['$scope', 'ts-messageService', MessageController])
        .service('ts-messageService', [MessageService])
        .directive('tsGlobalMessage', [Message]);


    function MessageController(scope, service) {
        scope.service = service;
    }

    function MessageService() {
        var me = this, status = null;

        this.message = null;
        this.messageStyle = '';
        this.showMessage = showMessage;
        this.hideMessage = hideMessage;


        function showMessage(message, messageStatus) {
            status = messageStatus;
            me.messageStyle = defineStyle();
            me.message = ThemesSwitcher.getMessage(message);
            return status;
        }

        function hideMessage(messageStatus) {
            status = messageStatus;
            me.message = '';
            me.messageStyle = '';
            return status;
        }

        function defineStyle() {
            switch (status) {
                case ThemesSwitcher.state.ERROR:
                    return 'alert-danger';
                case ThemesSwitcher.state.WARNING:
                    return 'alert-warning';
                case ThemesSwitcher.state.SUCCESS:
                    return 'alert-success';
                case ThemesSwitcher.state.WAITING:
                    return 'alert-info';
                default :
                    return '';
            }
        }
    }

    function Message() {
        return {
            restrict: 'E',
            controller: 'ts-messageController',
            scope: {},
            template: template
        };

        function template() {
            var root = angular.element('<div ng-show="service.message" class="alert ts-message" ng-class="service.messageStyle" ng-bind="service.message"></div>');
            return root[0].outerHTML;
        }
    }

})();

