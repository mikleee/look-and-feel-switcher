(function () {
    angular.module('ts-directives', ['ui.bootstrap'])
        .directive('tsScreenshot', [Screenshot])
        .controller('tsScreenshotController', ['$scope', ScreenshotController])
        .directive('tsSpinner', [Spinner])
        .directive('tsLockedOn', [LockedOn]);


    function Screenshot() {
        return {
            restrict: 'E',
            scope: {
                src: '='
            },
            controller: 'tsScreenshotController',
            link: link,
            template: template
        };

        function link(scope, element) {
            scope.state = 0;
            var img = element.find('img')[0];
            img.onerror = applyState;
            img.onload = applyState;

            function applyState(event) {
                scope.$apply(function () {
                    scope.state = event.type == 'load' ? 1 : -1;
                });
            }
        }

        function template() {
            var $ = angular.element;

            var screenshot = $('<img id="ts-screenshot-{{$id}}" ng-src="{{src}}" ng-show="state == 1" class="img-polaroid theme-screenshot">');
            var loadingMessage = $('<ts-spinner ng-show="state == 0">');
            var errorMessage = $('<img ng-show="state == -1" title="{{errorTitle()}}">').attr('src', ThemesSwitcher.getThemeImage('/application/forbidden_action.png'));

            var root = $('<span class="ts-screenshot">').append(screenshot).append(loadingMessage).append(errorMessage);
            return root[0].outerHTML;
        }
    }

    function ScreenshotController(scope) {
        scope.errorTitle = function () {
            return scope.alt || ThemesSwitcher.getMessage('ts-screenshot-is-not-available');
        }
    }


    function Spinner() {
        return {
            restrict: 'E',
            template: template
        };

        function template() {
            var $ = angular.element;
            var root = $('<div class="ts-spinner">')
                .append(
                    $('<img>').attr('src', ThemesSwitcher.getThemeImage('/aui/loading_indicator.gif'))
                );
            return root[0].outerHTML;
        }
    }


    function LockedOn() {
        return {
            restrict: 'A',
            link: link
        };

        function link(scope, element, attr) {
            var locker = angular.element('<div>').addClass('ts-locker');
            element.append(locker).css('position', 'relative');

            scope.$watch(attr['tsLockedOn'], function (n, o) {
                if (n === true) {
                    locker.addClass('ts-locked')
                } else {
                    locker.removeClass('ts-locked')
                }
            });
        }
    }

})();