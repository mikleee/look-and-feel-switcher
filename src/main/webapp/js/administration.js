var lookAndFeelAdministrationController = function ($scope, $http, service, initConfig) {
    $scope.models = {
        /**
         * @type{[Theme]}
         */
        lookAndFeels: [],
        message: null,
        status: 'waiting'
    };

    var handlers = {
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

                $scope.models.status = 'success';
                handlers.hideMessage();

                angular.forEach($scope.models.lookAndFeels, function (theme, i) {
                    if (theme.hasColorSchemes()) {
                        $scope.$watch('models.lookAndFeels[' + i + '].selected', function (newVal, oldVal) {
                            angular.forEach(theme.colorSchemes, function (v, k) {
                                v.selected = newVal;
                            });
                        });

                        angular.forEach(theme.colorSchemes, function (cs, k) {
                            $scope.$watch('models.lookAndFeels[' + i + '].colorSchemes[' + k + '].selected', function (newVal, oldVal) {
                                if (newVal == true/* && service.areOtherSchemesSelected(theme, cs)*/) {
                                    theme.selected = true;
                                } else if (newVal == false && service.areOtherSchemesUnselected(theme, cs)) {
                                    theme.selected = false;
                                }
                            });
                        });
                    }
                });
            }
        }
    };

    $scope.expressions = {
        screenShotPath: function () {
            return service.getScreenshotPath($scope.models.currentTheme, $scope.models.currentColorScheme);
        },
        isColorSchemePresent: function () {
            return $scope.models.currentTheme && $scope.models.currentTheme['colorSchemes'] && $scope.models.currentTheme['colorSchemes'].length > 0;
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
        },
        nsValue: function (value) {
            return initConfig.ns + value;
        }
    };

    $scope.listeners = {
        applyMap: function () {
            var data = [];
            angular.forEach($scope.models.lookAndFeels, function (theme) {
                data.push(new LookAndFeel(theme.id, theme.selected));
                if(theme.hasColorSchemes()){
                    angular.forEach(theme.colorSchemes, function (cs) {
                        data.push(new LookAndFeel(cs.id, cs.selected));
                    });
                }
            });

            $http.post(initConfig.applyLookAndFeelMapUrl, data).then(callBacks.onBindingApplied, callBacks.onRequestFailed);
        }
    };

    {   //init
        $http.get(initConfig.initLookAndFeelUrl).then(callBacks.onInitLookAndFeels, callBacks.onRequestFailed);
    }

};