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
            $scope.$emit(lfsConstants.event.SHOW_MESSAGE, message, status);
        },
        hideMessage: function () {
            $scope.$emit(lfsConstants.event.HIDE_MESSAGE);
        }
    };

    var callBacks = {
        onRequestFailed: function (response) {
            handlers.showMessage('ts-internal-server-error', lfsConstants.state.ERROR);
        },
        onInitLookAndFeels: function (response) {
            service.setLookAndFeels(response.data.body['lookAndFeels']);
            if (service.isNoData()) {
                handlers.showMessage('ts-no-themes-found', lfsConstants.state.WARNING);
            } else {
                state = lfsConstants.state.SUCCESS;
                handlers.hideMessage();
            }
        },
        onBindingApplied: function (response) {
            if (response.data.status == lfsConstants.state.ERROR) {
                handlers.showMessage(response.data.body['error'], lfsConstants.state.ERROR);
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
            return state == lfsConstants.state.WAITING || service.isNoData();
        }
    };

    $scope.listeners = {
        resetBinding: function () {
            state = lfsConstants.state.WAITING;
            window.location = initConfig.resetBindingUrl;
        },
        applyBinding: function () {
            state = lfsConstants.state.WAITING;
            var data = angular.merge({}, service.getModels().lookAndFeelBinding);
            data.lookAndFeel = service.getActiveLookAndFeel();
            $http.post(initConfig.applyBindingUrl, data).then(callBacks.onBindingApplied, callBacks.onRequestFailed);
        }
    };

    {   //init
        service.setLookAndFeelBinding(initConfig.lookAndFeelBinding);
        $http.get(initConfig.initLookAndFeelUrl).then(callBacks.onInitLookAndFeels, callBacks.onRequestFailed);

        $scope.$watch('models.currentTheme', handlers.onThemeChange);
        $scope.$watch('models.currentColorScheme', handlers.onColorSchemeChange);
    }

}