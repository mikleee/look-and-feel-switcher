/**
 * @param $scope
 * @constructor
 */
function MessageController($scope) {
    var status;
    $scope.message = null;


    $scope.messageStyle = function () {
        switch (status) {
            case 'error':
                return 'alert-danger';
            case 'warning':
                return 'alert-warning';
            case 'success':
                return 'alert-success';
            default :
                return '';
        }
    };

    var listener = {
        onShowMessage: function (event, message, messageStatus) {
            $scope.message = lfsConstants.getMessage(message);
            status = messageStatus;
        },
        onHideMessage: function () {
            status = null;
            $scope.message = null;
        }
    };
    $scope.$on(lfsConstants.event.SHOW_MESSAGE, listener.onShowMessage);
    $scope.$on(lfsConstants.event.HIDE_MESSAGE, listener.onHideMessage);
}