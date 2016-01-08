/**
 * @param $scope
 * @param $http
 * @param service
 * @param portletConfig {{ns: String, initLookAndFeelUrl: String, permissionTableUrl: String, applyPermissionsUrl: String}}
 * @constructor
 */
function SelectLookAndFeelAdministrationController($scope, $http, service, portletConfig) {
    $scope.message = null;
    $scope.status = 'waiting';
    $scope.models = service.getModels();


    var handlers = {
        onThemeChange: function (newVal) {
            service.getModels().currentColorScheme = service.getPreselectedColorScheme(newVal);
        },
        onColorSchemeChange: function (newVal) {
            listeners.fetchPermissions(newVal);
        },
        showMessage: function (status, message) {
            $scope.status = status;
            $scope.message = message;
        },
        hideMessage: function () {
            $scope.message = null;
        }
    };

    var callBacks = {
        onRequestFailed: function (response) {
            handlers.showMessage('error', Util.getMessage('internal-server-errors'));
        },
        onInitLookAndFeels: function (response) {
            service.setLookAndFeels(response.data.body['lookAndFeels']);
            if (service.isNoData()) {
                handlers.showMessage('warning', Util.getMessage('no-themes-found'));
            } else {
                $scope.status = 'success';
                handlers.hideMessage();
            }
        },
        onPermissionsFetched: function (response) {
            service.getModels().permissionMap = response.data.body['permissions'];
        }
    };

    $scope.expressions = {
        screenShotPath: function () {
            return service.getScreenshotPath();
        },
        disableFormCondition: function () {
            return $scope.status == 'waiting' || service.isNoData();
        },
        messageStyle: function () {
            switch ($scope.status) {
                case 'error':
                    return 'alert-danger';
                case 'warning':
                    return 'alert-warning';
                case 'success':
                    return 'alert-success';
                default :
                    return '';
            }
        }
    };

    var listeners = {
        fetchPermissions: function (lookAndFeel) {
            if (lookAndFeel) {
                $http.post(portletConfig.permissionTableUrl, {id: lookAndFeel.id}).then(callBacks.onPermissionsFetched, callBacks.onRequestFailed);
            }
        }
    };

    {   //init
        $http.get(portletConfig.initLookAndFeelUrl).then(callBacks.onInitLookAndFeels, callBacks.onRequestFailed);

        $scope.$watch('models.currentTheme', handlers.onThemeChange);
        $scope.$watch('models.currentColorScheme', handlers.onColorSchemeChange);
    }

}


/**
 * @param $scope
 * @param $http
 * @param service
 * @param portletConfig {{ns: String, initLookAndFeelUrl: String, permissionTableUrl: String, applyPermissionsUrl: String}}
 * @constructor
 */
function LookAndFeelPermissionsController($scope, $http, service, portletConfig) {
    $scope.message = null;
    $scope.status = 'waiting';
    $scope.models = service.getModels();


    var handlers = {
        showMessage: function (status, message) {
            $scope.status = status;
            $scope.message = message;
        },
        hideMessage: function () {
            $scope.message = null;
        }
    };

    var callBacks = {
        onRequestFailed: function (response) {
            handlers.showMessage('error', Util.getMessage('internal-server-errors'));
        },
        onPermissionSubmitted: function (response) {

        }
    };

    $scope.expressions = {
        messageStyle: function () {
            switch ($scope.status) {
                case 'error':
                    return 'alert-danger';
                case 'warning':
                    return 'alert-warning';
                case 'success':
                    return 'alert-success';
                default :
                    return '';
            }
        }
    };

    $scope.listeners = {
        submitPermissions: function () {
            var data = {id: 'asdasd', permissions: service.getModels().permissionMap};
            $http.post(portletConfig.applyPermissionsUrl, data).then(callBacks.onPermissionSubmitted, callBacks.onRequestFailed);
        }
    };

}