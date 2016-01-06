function SelectLookAndFeelAdministrationController($scope, $http, service, portletConfig) {
    $scope.message = null;
    $scope.status = 'waiting';
    $scope.models = service.getModels();


    var handlers = {
        onThemeChange: function (newVal) {
            service.getModels().currentColorScheme = service.getPreselectedColorScheme(newVal);
        },
        onColorSchemeChange: function (newVal) {
            service.getModels().currentColorScheme = newVal;
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
            handlers.showMessage('error', 'internal server error');
        },
        onInitLookAndFeels: function (response) {
            service.setLookAndFeels(response.data.body['lookAndFeels']);
            if (service.isNoData()) {
                handlers.showMessage('warning', 'no available themes have been found');
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

    };

    {   //init
        $http.get(portletConfig.initLookAndFeelUrl).then(callBacks.onInitLookAndFeels, callBacks.onRequestFailed);

        $scope.$watch('models.currentTheme', handlers.onThemeChange);
        $scope.$watch('models.currentColorScheme', handlers.onColorSchemeChange);
    }


}