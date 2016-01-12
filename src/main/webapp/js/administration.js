/**
 * @param $scope
 * @param $http
 * @param service
 * @param portletConfig {{ns: String, initLookAndFeelUrl: String, fetchPermissionsUrl: String, applyPermissionsUrl: String}}
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
                $scope.$broadcast(Util.events.FETCH_PERMISSIONS_REQUESTED);
            }
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

    $scope.listeners = {
        onLookAndFeelChange: function () {
            return $scope.$broadcast(Util.events.FETCH_PERMISSIONS_REQUESTED);
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
 * @param portletConfig {{ns: String, initLookAndFeelUrl: String, fetchPermissionsUrl: String, applyPermissionsUrl: String}}
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
            $scope.status = 'success';
        },
        onPermissionsFetched: function (response) {
            service.getModels().permissionMap = response.data.body['permissions']['permissions'];
            $scope.status = 'success';
        }
    };

    var listeners = {
        fetchPermissions: function () {
            var activeLookAndFeel = service.getActiveLookAndFeel();
            if (activeLookAndFeel) {
                $scope.status = 'waiting';
                $http.post(portletConfig.fetchPermissionsUrl, {id: activeLookAndFeel.id}).then(callBacks.onPermissionsFetched, callBacks.onRequestFailed);
            }
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
        },
        disableCondition: function () {
            return $scope.status == 'waiting';
        }
    };

    $scope.$on(Util.events.FETCH_PERMISSIONS_REQUESTED, listeners.fetchPermissions);

    $scope.listeners = {
        submitPermissions: function () {
            $scope.status = 'waiting';
            var activeLookAndFeel = service.getActiveLookAndFeel();
            var data = {id: activeLookAndFeel ? activeLookAndFeel.id : null, permissions: service.getModels().permissionMap};
            $http.post(portletConfig.applyPermissionsUrl, data).then(callBacks.onPermissionSubmitted, callBacks.onRequestFailed);
        }
    };

}