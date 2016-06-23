(function () {
    angular.module('ts-directives', ['ui.bootstrap'])
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
            templateUrl: ThemesSwitcher.staticUrl.screenshotTemplate
        };

        function link(scope, element) {
            scope.scrState = 'loading';
            var img = element.find('img');
            img.on('error', function () {
                scope.scrState = 'failed';
            });
            img.on('load', function () {
                scope.scrState = 'success';
            })
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
                    $('<img>').attr('src', Liferay.ThemeDisplay.getPathThemeImages() + '/aui/loading_indicator.gif')
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