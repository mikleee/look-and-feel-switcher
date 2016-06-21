(function () {
    angular.module('ts-directives', [])
        .directive('tsScreenshot', [Screenshot]);


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
})();