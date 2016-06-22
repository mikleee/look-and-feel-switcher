(function () {
    angular.module('ts-directives', [])
        .directive('tsScreenshot', [Screenshot])
        .directive('tsSpinner', [Spinner])
        .directive('tsLockedOn', [LockedOn]);


    function Screenshot() {
        return {
            restrict: 'E',
            scope: {
                src: '=',
                alt: '@'
            },
            link: link,
            template: template
        };

        function link(scope, element) {
            scope.state = 'loading';
            var img = element.find('img');
            img.on('error', function () {
                scope.state = 'failed';
            });
            img.on('load', function () {
                scope.state = 'success';
            })
        }

        function template() {
            var $ = angular.element;
            var root = $('<span>')
                .append('<span ng-show="state == \'loading\' || state == \'success\'"><img ng-src="{{src}}" class="ts-screen-shot"></span>')
                .append('<span ng-show="screenshot.state == \'failed\'" class="alert alert-warning"><liferay-ui:message key="ts-screenshot-is-not-available"/></span>');
            return root[0].outerHTML;
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
                    $('<img>').attr('src', ThemesSwitcher.contextPath + '/img/spinner.gif')
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