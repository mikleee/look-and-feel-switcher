/**
 * @param $scope
 * @param $http
 * @param service {LookAndFeelService}
 * @param initConfig
 * @constructor
 */
function SelectLookAndFeelController($scope, $http, service, initConfig) {
    var state;
    $scope.models = service.getModels();

    var handlers = {
        onThemeChange: function (newVal) {
            var cs = service.getPreselectedColorScheme(newVal);
            service.getModels().currentColorScheme = cs;
            service.getModels().lookAndFeelBinding.lookAndFeel.themeId = newVal ? newVal.id : null;
            service.getModels().lookAndFeelBinding.lookAndFeel.colorSchemeId = cs ? cs.id : null;
        },
        onColorSchemeChange: function (newVal) {
            service.getModels().currentColorScheme = newVal;
            service.getModels().lookAndFeelBinding.lookAndFeel.colorSchemeId = newVal ? newVal.id : null;
        },
        showMessage: function (message, status) {
            state = status;
            $scope.$emit(tsConstants.event.SHOW_MESSAGE, message, status);
        },
        hideMessage: function () {
            $scope.$emit(tsConstants.event.HIDE_MESSAGE);
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
                for (var i = 0; i < service.getModels().lookAndFeels.length; i++) {
                    var theme = service.getModels().lookAndFeels[i];
                    if (theme.portalDefault === true) {
                        var namePrefix = ' (' + tsConstants.getMessage('ts-portal-default') + ')';
                        if (theme.colorSchemes.length == 0) {
                            theme.name += namePrefix;
                        } else {
                            for (i = 0; i < theme.colorSchemes.length; i++) {
                                var cs = theme.colorSchemes[i];
                                if (theme.portalDefault === true) {
                                    cs.name += namePrefix;
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }


                state = tsConstants.state.SUCCESS;
                handlers.hideMessage();
            }
        }
        ,
        onBindingApplied: function (response) {
            if (response.data.status == tsConstants.state.ERROR) {
                handlers.showMessage(response.data.body['error'], tsConstants.state.ERROR);
            } else {
                window.location.reload();
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
        resetBinding: function () {
            state = tsConstants.state.WAITING;
            window.location = initConfig.resetBindingUrl;
        },
        applyBinding: function () {
            state = tsConstants.state.WAITING;
            var data = angular.merge({}, service.getModels().lookAndFeelBinding);
            data.lookAndFeel = service.getActiveLookAndFeel();
            $http.post(initConfig.applyBindingUrl, data).then(callBacks.onBindingApplied, callBacks.onRequestFailed);
        }
    };

    {   //init
        handlers.showMessage('ts-loading', tsConstants.state.WAITING);
        service.setLookAndFeelBinding(initConfig.lookAndFeelBinding);
        $http.get(initConfig.initLookAndFeelUrl).then(callBacks.onInitLookAndFeels, callBacks.onRequestFailed);

        $scope.$watch('models.currentTheme', handlers.onThemeChange);
        $scope.$watch('models.currentColorScheme', handlers.onColorSchemeChange);
    }

}