function SelectLookAndFeelController($scope, $http, service, initConfig) {
    $scope.models = {
        /**
         * @type{LookAndFeelBinding}
         */
        lookAndFeelBinding: initConfig.lookAndFeelBinding,
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
            var cs = service.getColorSchemeToShow(newVal);
            $scope.models.currentColorScheme = cs;
            $scope.models.lookAndFeelBinding.lookAndFeel.themeId = newVal ? newVal.id : null;
            $scope.models.lookAndFeelBinding.lookAndFeel.colorSchemeId = cs ? cs.id : null;
        },
        onColorSchemeChange: function (newVal) {
            $scope.models.currentColorScheme = newVal;
            $scope.models.lookAndFeelBinding.lookAndFeel.colorSchemeId = newVal ? newVal.id : null;
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
                $scope.models.currentTheme = service.getThemeToShow($scope.models.lookAndFeels);
                $scope.models.currentColorScheme = service.getColorSchemeToShow($scope.models.currentTheme);
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
            return service.getScreenshotPath($scope.models.currentTheme, $scope.models.currentColorScheme);
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

    $scope.listeners = {
        resetBinding: function () {
            $scope.models.status = 'waiting';
            window.location = initConfig.resetBindingUrl;
        },
        applyBinding: function () {
            $scope.models.status = 'waiting';

            var data = {'lookAndFeel.themeId': $scope.models.currentTheme.id};
            if ($scope.models.currentColorScheme) {
                data['lookAndFeel.colorSchemeId'] = $scope.models.currentColorScheme.id;
            }

            $http.post(initConfig.applyBindingUrl, $scope.models.lookAndFeelBinding).then(callBacks.onBindingApplied, callBacks.onRequestFailed);
        }
    };

    {   //init
        if (!$scope.models.lookAndFeelBinding.lookAndFeel) {
            var lookAndFeel = new LookAndFeel();
            lookAndFeel.companyId = $scope.models.lookAndFeelBinding.companyId;
            $scope.models.lookAndFeelBinding.lookAndFeel = lookAndFeel;
        }

        $scope.$watch('models.currentTheme', handlers.onThemeChange);
        $scope.$watch('models.currentColorScheme', handlers.onColorSchemeChange);

        $http.get(initConfig.initLookAndFeelUrl).then(callBacks.onInitLookAndFeels, callBacks.onRequestFailed);
    }

};