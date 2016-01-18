/**
 * @param $scope
 * @constructor
 */
function MessageController($scope) {
    var status;
    $scope.message = null;
    $scope.messageStyle = '';


    var defineStyle = function () {
        switch (status) {
            case tsConstants.state.ERROR:
                return 'alert-danger';
            case tsConstants.state.WARNING:
                return 'alert-warning';
            case tsConstants.state.SUCCESS:
                return 'alert-success';
            case tsConstants.state.WAITING:
                return 'alert-info';
            default :
                return '';
        }
    };

    var listener = {
        onShowMessage: function (event, message, messageStatus) {
            status = messageStatus;
            $scope.messageStyle = defineStyle();
            $scope.message = tsConstants.getMessage(message);
        },
        onHideMessage: function () {
            $scope.message = null;
            $scope.messageStyle = '';
        }
    };
    $scope.$on(tsConstants.event.SHOW_MESSAGE, listener.onShowMessage);
    $scope.$on(tsConstants.event.HIDE_MESSAGE, listener.onHideMessage);
}