/**
 * @param $scope
 * @param $http
 * @param service {LookAndFeelService}
 * @param initConfig
 * @constructor
 */
function SelectLookAndFeelController($scope, $http, service, initConfig) {
    $scope.message = null;
    $scope.status = 'waiting';
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
            return service.getScreenshotPath();
        },
        jopa: function () {
            return $scope.models.currentTheme && $scope.models.currentTheme.hasColorSchemes();
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
        resetBinding: function () {
            $scope.status = 'waiting';
            window.location = initConfig.resetBindingUrl;
        },
        applyBinding: function () {
            $scope.status = 'waiting';

            var data = {'lookAndFeel.themeId': service.getModels().currentTheme.id};
            if ($scope.models.currentColorScheme) {
                data['lookAndFeel.colorSchemeId'] = service.getModels().currentColorScheme.id;
            }

            $http.post(initConfig.applyBindingUrl, service.getModels().lookAndFeelBinding).then(callBacks.onBindingApplied, callBacks.onRequestFailed);
        }
    };

    {   //init
        service.setLookAndFeelBinding(initConfig.lookAndFeelBinding);
        $http.get(initConfig.initLookAndFeelUrl).then(callBacks.onPermissionSubmitted, callBacks.onRequestFailed);

        $scope.$watch('models.currentTheme', handlers.onThemeChange);
        $scope.$watch('models.currentColorScheme', handlers.onColorSchemeChange);
    }

}