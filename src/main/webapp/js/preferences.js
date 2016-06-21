(function () {
    angular.module('ts-preferences', ['ts-message', 'ts-preferencesPermissions', 'ts-preferencesAdministration'])
        .controller('preferencesController', ['$scope', 'ts-messageService', PreferencesController]);

    function PreferencesController(scope, messageService) {
        scope.tab = 'permissions';
        scope.setTab = setTab;

        function setTab(t) {
            messageService.hideMessage();
            scope.tab = t;
        }

    }
})();


(function () {
    angular.module('ts-preferencesPermissions', ['ts-preferences', 'ts-lookAndFeelList', 'ts-message', 'ts-pagination'])
        .controller('preferencesPermissionsController', ['$scope', 'lookAndFeelListService', 'preferencesPermissionsService', 'ts-messageService', PreferencesPermissionsController])
        .service('preferencesPermissionsService', ['$http', 'paginatorServiceFactory', 'config', PreferencesPermissionsService]);

    function PreferencesPermissionsController(scope, lookAndFeelService, permissionService, messageService) {
        var state;
        scope.lookAndFeelService = lookAndFeelService;
        scope.permissionService = permissionService;


        scope.$watch('lookAndFeelService.getModels().currentTheme', onThemeChanged);
        scope.$watch('lookAndFeelService.getModels().currentColorScheme', onColorSchemeChanged);


        function fetchPermissions() {
            var activeLookAndFeel = lookAndFeelService.getActiveLookAndFeel();
            if (activeLookAndFeel) {
                permissionService.fetchPermissions(activeLookAndFeel);
            }
        }

        function onThemeChanged(theme) {
            if (theme && !theme.hasColorSchemes()) {
                permissionService.fetchPermissions(lookAndFeelService.getActiveLookAndFeel());
            }
        }

        function onColorSchemeChanged(cs) {
            if (cs) {
                permissionService.fetchPermissions(lookAndFeelService.getActiveLookAndFeel());
            }
        }


        // /**
        //  * @type {PaggedTable}
        //  */
        // scope.permissionsTable = new PaggedTable();
        //
        // var callBacks = {
        //     onRequestFailed: function (response) {
        //         state = messageService.showMessage('ts-internal-server-error', tsConstants.state.ERROR);
        //     },
        //     onPermissionsFetched: function (response) {
        //         lookAndFeelService.setResourcePermissions(response.data.body['permissions']);
        //         scope.permissionsTable.init({
        //             collection: lookAndFeelService.getModels().resourcePermissions.permissions,
        //             page: 1,
        //             pageSize: 5,
        //             pageSizes: [5, 10, 20]
        //         });
        //         state = messageService.hideMessage('ts-internal-server-error', tsConstants.state.SUCCESS);
        //     }
        // };
        //
        // var listeners = {
        //     fetchPermissions: function () {
        //         var activeLookAndFeel = lookAndFeelService.getActiveLookAndFeelOption();
        //         if (activeLookAndFeel) {
        //             state = messageService.showMessage('ts-loading', tsConstants.state.WAITING);
        //             $http.post(portletConfig.fetchPermissionsUrl, {id: activeLookAndFeel.id}).then(callBacks.onPermissionsFetched, callBacks.onRequestFailed);
        //         }
        //     }
        // };
        //
        // scope.expressions = {
        //     disableCondition: function () {
        //         return state == tsConstants.state.WAITING;
        //     }
        // };
        //
        // scope.$on(tsConstants.event.FETCH_PERMISSIONS_REQUESTED, listeners.fetchPermissions);
        //
        // scope.listeners = {
        //     submitPermissions: function () {
        //         state = tsConstants.state.WAITING;
        //         $http.post(portletConfig.applyPermissionsUrl, lookAndFeelService.getModels().resourcePermissions).then(callBacks.onPermissionsFetched, callBacks.onRequestFailed);
        //     },
        //     setDefaultPermissions: function () {
        //         state = tsConstants.state.WAITING;
        //         $http.post(portletConfig.setDefaultPermissionsUrl, lookAndFeelService.getModels().resourcePermissions).then(callBacks.onPermissionsFetched, callBacks.onRequestFailed);
        //     },
        //     toggleAction: function (actionId) {
        //         lookAndFeelService.toggleAction(actionId, scope.permissionsTable.getCurrentPage());
        //     }
        // };

    }

    function PreferencesPermissionsService(http, paginatorServiceFactory, config) {
        const me = this;

        /** @type {ResourcePermissions} */
        this.resourcePermissions = null;
        this.paginator = paginatorServiceFactory.dynamicPaginator();
        this.fetchPermissions = fetchPermissions;
        this.submitPermissions = submitPermissions;
        this.setDefaultPermissions = setDefaultPermissions;


        function fetchPermissions(activeLookAndFeel) {
            var rConfig = {
                ns: config.ns,
                success: function (data) {
                    me.resourcePermissions = onPermissionsFetched(data);
                    return {totalCount: data.body.totalCount, pageContent: me.resourcePermissions.permissions};
                }
            };

            return me.paginator.initPaginator(config.fetchPermissionsUrl + '&' + config.ns + 'id=' + activeLookAndFeel.id, rConfig);
        }

        // function fetchPermissions(activeLookAndFeel) {
        //     var promise = http.get(config.fetchPermissionsUrl + '&id=activeLookAndFeel.id');
        //     promise.then(onPermissionsFetched);
        //     return promise;
        // }

        function submitPermissions() {
            var promise = http.post(config.applyPermissionsUrl, resourcePermissions);
            promise.then(onPermissionsFetched);
            return promise;
        }

        function setDefaultPermissions() {
            var promise = http.post(config.setDefaultPermissionsUrl, resourcePermissions);
            promise.then(onPermissionsFetched);
            return promise;
        }


        function onPermissionsFetched(data) {
            var result = data.body['permissions'];
            angular.forEach(result.allowedActions, function (v) {
                v.name = ThemesSwitcher.getMessage(v.name);
            });
            return result;
        }

    }


    /** @constructor */
    function ResourcePermissions() {
        this.id = null;
        /** @type {[Action]} */
        this.allowedActions = [];
        /** @type {[RolePermissions]} */
        this.permissions = [];
    }

    /** @constructor */
    function RolePermissions() {
        /** @type {{name: String, type: String, description: String, id: Number}} */
        this.role = {};
        /** @type {[Action]} */
        this.actions = [];
    }

    /** @constructor */
    function Action() {
        this.id = null;
        this.name = null;
        this.permitted = false;
    }


})();


(function () {
    angular.module('ts-preferencesAdministration', ['ts-message'])
        .controller('preferencesAdministrationController', ['$scope', 'preferencesAdministrationService', 'ts-messageService', PreferencesAdministrationController])
        .service('preferencesAdministrationService', ['$http', 'config', PreferencesAdministrationService]);

    function PreferencesAdministrationController(scope, service, messageService) {
        var state = messageService.showMessage('ts-loading', ThemesSwitcher.state.WAITING);

        scope.stat = service.getStats();
        scope.isLocked = isLocked;
        scope.getStatMessage = getStatMessage;
        scope.removeAllBindings = removeAllBindings;


        service.fetchStats().then(onStatFetched, onRequestFailed);


        function isLocked() {
            return state == ThemesSwitcher.state.WAITING || scope.stat.isEmpty();
        }

        function getStatMessage() {
            return ThemesSwitcher.getMessage('ts-bindings-stat-message', [scope.stat.user.count, scope.stat.guest.count]);
        }

        function removeAllBindings() {
            state = messageService.showMessage('ts-remove-bindings', ThemesSwitcher.state.WAITING);
            return service.removeAllBindings(onBindingsRemoved, onRequestFailed)
        }

        function onStatFetched() {
            state = messageService.hideMessage(ThemesSwitcher.state.SUCCESS);
        }

        function onBindingsRemoved() {
            state = messageService.showMessage('ts-all-bindings-has-been-removed', ThemesSwitcher.state.SUCCESS);
        }

        function onRequestFailed() {
            state = messageService.showMessage('ts-internal-server-error', ThemesSwitcher.state.ERROR);
        }

    }

    function PreferencesAdministrationService(http, config) {
        var stat = new BindingsStats();
        this.getStats = getStats;
        this.fetchStats = fetchStats;
        this.removeAllBindings = removeAllBindings;

        function getStats() {
            return stat;
        }

        function fetchStats() {
            var promise = http.get(config.bindingsStatUrl);
            promise.then(onStatFetched);
            return promise;
        }

        function removeAllBindings() {
            var promise = http.post(config.removeAllBindingsUrl);
            promise.then(onBindingsRemoved);
            return promise;
        }

        function onStatFetched(response) {
            stat.guest = response.data.body['guest'];
            stat.user = response.data.body['user'];
        }

        function onBindingsRemoved(response) {
            stat.guest = response.data.body['guest'];
            stat.user = response.data.body['user'];
        }

    }

    /** @constructor */
    function BindingsStats() {
        this.guest = new BindingStat();
        this.user = new BindingStat();
        this.isEmpty = function () {
            return this.guest.count + this.user.count == 0;
        }
    }

    /** @constructor */
    function BindingStat() {
        this.count = 0;
    }
})();