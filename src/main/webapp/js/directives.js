/**
 * @constructor
 */
function NgImage() {
    const NG_MODEL = 'ngModel';

    var apply = function (scope, model, state) {
        model.state = state;
        scope.$digest();
    };

    return {
        restrict: 'E',
        link: function (scope, element, attributes) {
            var modelKey = attributes[NG_MODEL];
            if (modelKey) {
                scope[modelKey] = {state: 'loading'};
                element.on('error', function () {
                    apply(scope, scope[modelKey], 'failed');
                });
                element.on('load', function () {
                    apply(scope, scope[modelKey], 'success');
                })
            }

        }
    }
}

angular.module('tsDirectives', []).directive('img', [NgImage]);

