/**
 * @param $scope
 * @constructor
 */
function PreferencesController($scope, $location) {
    $scope.tab = null;
    var hash = $location.path();
    debugger;
    if (hash == '/permissions') {
        $scope.tab = 'permissions';
    } else if (hash == '/administration') {
        $scope.tab = 'administration';
    } else {
        $scope.tab = 'permissions';
    }

    $scope.expressions = {};

    $scope.listeners = {
        onTabChange: function (tab) {
            $scope.tab = tab;
            $scope.$broadcast(tsConstants.event.HIDE_MESSAGE);
        }
    };

}

/**
 * @param $scope
 * @param $http
 * @param service
 * @param portletConfig {PortletConfig}
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
 * @param lookAndFeelService{LookAndFeelService}
 * @param portletConfig {PortletConfig}
 * @constructor
 */
function LookAndFeelPermissionsController($scope, $http, lookAndFeelService, portletConfig) {
    var state;
    $scope.models = lookAndFeelService.getModels();
    /**
     * @type {PaggedTable}
     */
    $scope.permissionsTable = new PaggedTable();

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
            lookAndFeelService.setResourcePermissions(response.data.body['permissions']);
            state = tsConstants.state.SUCCESS;
            $scope.permissionsTable.init({
                collection: lookAndFeelService.getModels().resourcePermissions.permissions,
                page: 1,
                pageSize: 5,
                pageSizes: [5, 10, 20]
            });
        }
    };

    var listeners = {
        fetchPermissions: function () {
            var activeLookAndFeel = lookAndFeelService.getActiveLookAndFeelOption();
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
            $http.post(portletConfig.applyPermissionsUrl, lookAndFeelService.getModels().resourcePermissions).then(callBacks.onPermissionsFetched, callBacks.onRequestFailed);
        },
        setDefaultPermissions: function () {
            state = tsConstants.state.WAITING;
            $http.post(portletConfig.setDefaultPermissionsUrl, lookAndFeelService.getModels().resourcePermissions).then(callBacks.onPermissionsFetched, callBacks.onRequestFailed);
        },
        toggleAction: function (actionId) {
            lookAndFeelService.toggleAction(actionId, $scope.permissionsTable.getCurrentPage());
        }
    };

}

/**
 * @param $scope
 * @param $http
 * @param portletConfig {PortletConfig}
 * @constructor
 */
function LookAndFeelAdministrationController($scope, $http, portletConfig) {
    var state = tsConstants.state.WAITING;
    $scope.stat = new BindingsStats();

    var handlers = {
        showMessage: function (message, status) {
            state = status;
            $scope.$emit(tsConstants.event.SHOW_MESSAGE, message, status);
        },
        hideMessage: function () {
            state = null;
            $scope.$emit(tsConstants.event.HIDE_MESSAGE);
        }
    };

    var callBacks = {
        onRequestFailed: function (response) {
            handlers.showMessage('ts-internal-server-error', tsConstants.state.ERROR);
        },
        onBindingsRemoved: function (response) {
            var count = response.data.body['count'];
            $scope.stat.guest = response.data.body['guest'];
            $scope.stat.user = response.data.body['user'];
            handlers.showMessage('ts-all-bindings-has-been-removed', tsConstants.state.SUCCESS);
        },
        onStatFetched: function (response) {
            $scope.stat.guest = response.data.body['guest'];
            $scope.stat.user = response.data.body['user'];
            handlers.hideMessage();
        }
    };

    $scope.expressions = {
        disableFormCondition: function () {
            debugger;
            return state == tsConstants.state.WAITING;
        },
        statMessage: function () {
            return tsConstants.getMessage('ts-bindings-stat-message', [$scope.stat.user.count, $scope.stat.guest.count]);
        }
    };

    $scope.listener = {
        fetchStats: function () {
            handlers.showMessage('loading', tsConstants.state.WAITING);
            $http.get(portletConfig.bindingsStatUrl).then(callBacks.onStatFetched, callBacks.onRequestFailed);
        },
        removeAllBindings: function () {
            handlers.showMessage('ts-remove-bindings', tsConstants.state.WAITING);
            $http.post(portletConfig.removeAllBindingsUrl).then(callBacks.onBindingsRemoved, callBacks.onRequestFailed);
        }
    };

    {
        $scope.listener.fetchStats();
    }

}