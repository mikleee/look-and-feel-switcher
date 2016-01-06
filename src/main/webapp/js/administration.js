function SelectLookAndFeelAdministrationController($scope, $http, lookAndFeelService, portletConfig) {
    $scope.models = {
        /**
         * @type{[Theme]}
         */
        lookAndFeels: [],
        /**
         * @type{Theme}
         */
        currentTheme: null,
        /**
         * @type{LookAndFeelOption}
         */
        currentColorScheme: null,
        message: null,
        status: 'waiting'
    };

    var handlers = {
        onThemeChange: function (newVal) {
            $scope.models.currentColorScheme = lookAndFeelService.getColorSchemeToShow(newVal);
        },
        onColorSchemeChange: function (newVal) {
            $scope.models.currentColorScheme = newVal;
        },
        showMessage: function (status, message) {
            $scope.models.status = status;
            $scope.models.message = message;
        },
        hideMessage: function () {
            $scope.models.message = null;
        }
    };

    var callBacks = {
        onRequestFailed: function (response) {
            handlers.showMessage('error', 'internal server error');
        },
        onInitLookAndFeels: function (response) {
            if (response.data.body['lookAndFeels'].length == 0) {
                handlers.showMessage('warning', 'no available themes have been found');
            } else {
                angular.forEach(response.data.body['lookAndFeels'], function (v, k) {
                    $scope.models.lookAndFeels.push(new Theme().fromObject(v));
                });
                $scope.models.currentTheme = lookAndFeelService.getThemeToShow($scope.models.lookAndFeels);
                $scope.models.currentColorScheme = lookAndFeelService.getColorSchemeToShow($scope.models.currentTheme);
                $scope.models.status = 'success';
                handlers.hideMessage();
            }
        },
        onBindingApplied: function (response) {
            if (response.data.status == 'error') {
                handlers.showMessage('error', response.data.body);
            } else {
                window.location.reload();
            }
        }
    };

    $scope.expressions = {
        screenShotPath: function () {
            return lookAndFeelService.getScreenshotPath($scope.models.currentTheme, $scope.models.currentColorScheme);
        },
        disableFormCondition: function () {
            return $scope.models.status == 'waiting' || $scope.models.lookAndFeels.length == 0;
        },
        messageStyle: function () {
            switch ($scope.models.status) {
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

    $scope.listeners = {};

    {   //init

        $scope.$watch('models.currentTheme', handlers.onThemeChange);
        $scope.$watch('models.currentColorScheme', handlers.onColorSchemeChange);

        $http.get(portletConfig.initLookAndFeelUrl).then(callBacks.onInitLookAndFeels, callBacks.onRequestFailed);
    }

}