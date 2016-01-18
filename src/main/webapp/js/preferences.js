/**
 * @param $scope
 * @constructor
 */
function PreferencesController($scope) {
    $scope.tab = 'permissions';

    $scope.expressions = {};

    $scope.listeners = {
        onTabChange: function (tab) {
            $scope.tab = tab;
        }
    };

}

/**
 * @param $scope
 * @param $http
 * @param service
 * @param portletConfig {{ns: String, initLookAndFeelUrl: String, fetchPermissionsUrl: String, applyPermissionsUrl: String}}
 * @constructor
 */
function SelectLookAndFeelPreferencesController($scope, $http, service, portletConfig) {
    var state;
    $scope.models = service.getModels();


    var handlers = {
        onThemeChange: function (newVal) {
            service.getModels().currentColorScheme = service.getPreselectedColorScheme(newVal);
        },
        showMessage: function (status, message) {
            state = status;
            $scope.$emit(tsConstants.event.SHOW_MESSAGE, message, status);
        },
        hideMessage: function () {
            $scope.message = null;
        }
    };

    var callBacks = {
        onRequestFailed: function (response) {
            handlers.showMessage('ts-internal-server-error', tsConstants.state.ERROR);
        },
        onInitLookAndFeels: function (response) {
            service.setLookAndFeels(response.data.body['lookAndFeels']);
            if (service.isNoData()) {
                handlers.showMessage('ts-no-themes-found', tsConstants.state.WARNING);
            } else {
                state = tsConstants.state.SUCCESS;
                handlers.hideMessage();
                $scope.$emit(tsConstants.event.FETCH_PERMISSIONS_REQUESTED);
            }
        }
    };

    $scope.expressions = {
        screenShotPath: function () {
            return service.getScreenshotPath();
        },
        disableFormCondition: function () {
            return state == tsConstants.state.WAITING || service.isNoData();
        }
    };

    $scope.listeners = {
        onLookAndFeelChange: function () {
            return $scope.$emit(tsConstants.event.FETCH_PERMISSIONS_REQUESTED);
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
 * @param portletConfig
 * @constructor
 */
function LookAndFeelPermissionsController($scope, $http, service, portletConfig) {
    var state;
    $scope.models = service.getModels();


    var handlers = {
        showMessage: function (status, message) {
            state = status;
            $scope.$emit(tsConstants.event.SHOW_MESSAGE, message, status);
        },
        hideMessage: function () {
            $scope.message = null;
        }
    };

    var callBacks = {
        onRequestFailed: function (response) {
            handlers.showMessage('ts-internal-server-error', tsConstants.state.ERROR);
        },
        onPermissionsFetched: function (response) {
            service.setResourcePermissions(response.data.body['permissions']);
            state = tsConstants.state.SUCCESS;
        }
    };

    var listeners = {
        fetchPermissions: function () {
            var activeLookAndFeel = service.getActiveLookAndFeelOption();
            if (activeLookAndFeel) {
                state = tsConstants.state.WAITING;
                $http.post(portletConfig.fetchPermissionsUrl, {id: activeLookAndFeel.id}).then(callBacks.onPermissionsFetched, callBacks.onRequestFailed);
            }
        }
    };

    $scope.expressions = {
        disableCondition: function () {
            return state == tsConstants.state.WAITING;
        }
    };

    $scope.$on(tsConstants.event.FETCH_PERMISSIONS_REQUESTED, listeners.fetchPermissions);

    $scope.listeners = {
        submitPermissions: function () {
            state = tsConstants.state.WAITING;
            $http.post(portletConfig.applyPermissionsUrl, service.getModels().resourcePermissions).then(callBacks.onPermissionsFetched, callBacks.onRequestFailed);
        },
        setDefaultPermissions: function () {
            state = tsConstants.state.WAITING;
            $http.post(portletConfig.setDefaultPermissionsUrl, service.getModels().resourcePermissions).then(callBacks.onPermissionsFetched, callBacks.onRequestFailed);
        },
        toggleAction: function (actionId) {
            service.toggleAction(actionId);
        }
    };

}