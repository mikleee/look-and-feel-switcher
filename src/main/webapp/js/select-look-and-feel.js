var selectLookAndFeelController = function ($scope, $http, service, initConfig) {
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

//var selectLookAndFeel = angular.module('selectLookAndFeel', ['lookAndFeelServices'])
//    .controller('selectLookAndFeelController', ['$scope', 'lookAndFeelService', selectLookAndFeelController]);


//var ThemesSwitcherController = function (config) {
//    var
//        ns = function (value) {
//            return config['ns'] + value;
//        },
//        nsId = function (id) {
//            return '#' + ns(id);
//        },
//        themes, screenShot, colorSchemes, errorMessage, lookAndFeelService, lookAndFeelContainer = nsId('look-and-feel-container');
//
//    var locker = {
//        block: function () {
//            var waitingIndicator = new dom.WaitingIndicator({
//                contextPath: config['contextPath'],
//                message: 'ashduashduiashduiashdaushdauishdashdauisdhui'
//            });
//            $(lookAndFeelContainer).block(Util.getBlockUIConfig(waitingIndicator));
//        },
//        unblock: function () {
//            $(lookAndFeelContainer).unblock();
//        }
//    };
//
//    var listener = {
//        onInitResponseSucceed: function (response) {
//            if (response['status'] == 'success') {
//                var currentBinding = response['body']['currentBinding'];
//                var lookAndFeels = response['body']['lookAndFeels'];
//
//                $(nsId('reset-binding')).val(currentBinding).prop('disabled', currentBinding == null);
//
//                if (lookAndFeels.length == 0) {
//                    errorMessage.render('jopa'); //todo
//                } else {
//                    lookAndFeelService = SelectLookAndFeelService(lookAndFeels);
//
//                    var themeToShow = lookAndFeelService.getThemeToShow();
//
//                    themes.renderContent(lookAndFeelService.getThemes());
//                    if (lookAndFeelService.hasColorSchemes(themeToShow)) {
//                        colorSchemes.renderContent(lookAndFeelService.getColorSchemes(themeToShow));
//                    } else {
//                        colorSchemes.behaviour.disable();
//                    }
//                    screenShot.renderContent(lookAndFeelService.getThemeScreenShot(themeToShow));
//                }
//            }
//        },
//        onInitResponseFail: function (response) {
//            $(lookAndFeelContainer).html(response.responseText);
//        },
//        onThemeChange: function () {
//            var theme = lookAndFeelService.findTheme(themes.getValue());
//            var colorSchemas = lookAndFeelService.getColorSchemes(theme);
//
//            if (colorSchemas) {
//                colorSchemes.renderContent(colorSchemas);
//                colorSchemes.behaviour.enable();
//            } else {
//                colorSchemes.behaviour.disable();
//            }
//            screenShot.renderContent(lookAndFeelService.getThemeScreenShot(theme));
//        },
//        onColorSchemeChange: function () {
//            var colorScheme = lookAndFeelService.getColorScheme(themes.getValue(), colorSchemes.getValue());
//            screenShot.renderContent(colorScheme['screenShotPath']);
//        },
//        applyBinding: function () {
//            locker.block();
//            $(nsId('themes-switcher-form')).submit();
//        },
//        resetBinding: function () {
//            locker.block();
//            window.location = config['resetBindingUrl']
//        }
//    };
//
//
//    var init = function () {   //init
//        locker.block();
//
//        screenShot = new dom.Image({
//            elem: nsId('screen-shot'),
//            contextPath: config['contextPath']
//        }).init();
//
//        themes = new dom.Select({
//            elem: nsId('themes'),
//            container: nsId('themes-container')
//        }).init();
//
//        colorSchemes = new dom.Select({
//            elem: nsId('color-schemes'),
//            container: nsId('color-schemes-container')
//        }).init();
//
//        errorMessage = new dom.ErrorMessage({container: nsId('error-message')});
//
//        $.ajax({
//            dataType: "json",
//            url: config['initLookAndFeelUrl'],
//            success: listener.onInitResponseSucceed,
//            error: listener.onInitResponseFail,
//            complete: locker.unblock
//        });
//    };
//
//    return {
//        config: config,
//        listener: listener,
//        locker: locker,
//        init: function () {
//            $(document).ready(init);
//            return this;
//        }
//    }
//
//};
//
//var SelectLookAndFeelService = function (data) {
//    var themes = data;
//
//    var
//        findById = function (id, container) {
//            for (var i = 0; i < container.length; i++) {
//                if (container[i]['id'] == id) {
//                    return container[i];
//                }
//            }
//        },
//        getThemeToShow = function () {
//            for (var i = 0; i < themes.length; i++) {
//                if (themes[i]['bind']) {
//                    return themes[i];
//                }
//            }
//
//            return themes[0];
//        };
//
//    return {
//        findTheme: function (id) {
//            return findById(id, themes);
//        },
//        getThemeScreenShot: function (theme) {
//            return theme['colorSchemes'] ?
//                theme['colorSchemes'][0]['screenShotPath'] : theme['screenShotPath'];
//        },
//        getColorSchemes: function (theme) {
//            return theme['colorSchemes'];
//        },
//        hasColorSchemes: function (theme) {
//            return theme['colorSchemes'] != null && theme['colorSchemes'].length > 0;
//        },
//        getColorScheme: function (themeId, colorSchemeId) {
//            var theme = this.findTheme(themeId);
//            return findById(colorSchemeId, theme['colorSchemes']);
//        },
//        getThemeToShow: getThemeToShow,
//        getThemes: function () {
//            return themes;
//        }
//    }
//};