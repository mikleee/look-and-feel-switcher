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
            case lfsConstants.state.ERROR:
                return 'alert-danger';
            case lfsConstants.state.WARNING:
                return 'alert-warning';
            case lfsConstants.state.SUCCESS:
                return 'alert-success';
            case lfsConstants.state.WAITING:
                return 'alert-info';
            default :
                return '';
        }
    };

    var listener = {
        onShowMessage: function (event, message, messageStatus) {
            status = messageStatus;
            $scope.messageStyle = defineStyle();
            $scope.message = lfsConstants.getMessage(message);
        },
        onHideMessage: function () {
            $scope.message = null;
            $scope.messageStyle = '';
        }
    };
    $scope.$on(lfsConstants.event.SHOW_MESSAGE, listener.onShowMessage);
    $scope.$on(lfsConstants.event.HIDE_MESSAGE, listener.onHideMessage);
}