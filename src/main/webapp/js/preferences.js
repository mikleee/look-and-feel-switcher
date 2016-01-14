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
            $scope.$emit(lfsConstants.event.SHOW_MESSAGE, message, status);
        },
        hideMessage: function () {
            $scope.message = null;
        }
    };

    var callBacks = {
        onRequestFailed: function (response) {
            handlers.showMessage('lfs-internal-server-error', lfsConstants.state.ERROR);
        },
        onInitLookAndFeels: function (response) {
            service.setLookAndFeels(response.data.body['lookAndFeels']);
            if (service.isNoData()) {
                handlers.showMessage('lfs-no-themes-found', lfsConstants.state.WARNING);
            } else {
                state = lfsConstants.state.SUCCESS;
                handlers.hideMessage();
                $scope.$emit(lfsConstants.event.FETCH_PERMISSIONS_REQUESTED);
            }
        }
    };

    $scope.expressions = {
        screenShotPath: function () {
            return service.getScreenshotPath();
        },
        disableFormCondition: function () {
            return state == lfsConstants.state.WAITING || service.isNoData();
        }
    };

    $scope.listeners = {
        onLookAndFeelChange: function () {
            return $scope.$emit(lfsConstants.event.FETCH_PERMISSIONS_REQUESTED);
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
    var state;
    $scope.models = service.getModels();


    var handlers = {
        showMessage: function (status, message) {
            state = status;
            $scope.$emit(lfsConstants.event.SHOW_MESSAGE, message, status);
        },
        hideMessage: function () {
            $scope.message = null;
        }
    };

    var callBacks = {
        onRequestFailed: function (response) {
            handlers.showMessage('lfs-internal-server-error', lfsConstants.state.ERROR);
        },
        onPermissionSubmitted: function (response) {
            state = lfsConstants.state.SUCCESS;
        },
        onPermissionsFetched: function (response) {
            service.setResourcePermissions(response.data.body['permissions']);
            state = lfsConstants.state.SUCCESS;
        }
    };

    var listeners = {
        fetchPermissions: function () {
            var activeLookAndFeel = service.getActiveLookAndFeelOption();
            if (activeLookAndFeel) {
                state = lfsConstants.state.WAITING;
                $http.post(portletConfig.fetchPermissionsUrl, {id: activeLookAndFeel.id}).then(callBacks.onPermissionsFetched, callBacks.onRequestFailed);
            }
        }
    };

    $scope.expressions = {
        disableCondition: function () {
            return state == lfsConstants.state.WAITING;
        }
    };

    $scope.$on(lfsConstants.event.FETCH_PERMISSIONS_REQUESTED, listeners.fetchPermissions);

    $scope.listeners = {
        submitPermissions: function () {
            state = lfsConstants.state.WAITING;
            $http.post(portletConfig.applyPermissionsUrl, service.getModels().resourcePermissions).then(callBacks.onPermissionSubmitted, callBacks.onRequestFailed);
        },
        toggleAction: function (actionId) {
            service.toggleAction(actionId);
        }
    };

}