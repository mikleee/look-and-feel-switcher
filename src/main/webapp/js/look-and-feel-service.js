var lookAndFeelService = function () {
    var util = {
        getBindLookAndFeel: function (models) {
            for (var i = 0; i < models.length; i++) {
                if (models[i]['bind']) {
                    return models[i];
                }
            }
            return null;
        }
    };


    return {
        getThemeToShow: function (lookAndFeels) {
            if (lookAndFeels.length == 0) {
                return null;
            }

            var bindTheme = util.getBindLookAndFeel(lookAndFeels);
            return bindTheme ? bindTheme : lookAndFeels[0];
        },
        getColorSchemeToShow: function (currentTheme) {
            if (currentTheme && currentTheme['colorSchemes'] && currentTheme['colorSchemes'].length > 0) {
                var bindTheme = util.getBindLookAndFeel(currentTheme['colorSchemes']);
                return bindTheme ? bindTheme : currentTheme['colorSchemes'][0];
            } else {
                return null;
            }
        },
        getScreenshotPath: function (theme, colorScheme) {
            if (colorScheme) {
                return colorScheme.screenShotPath;
            } else {
                return theme ? theme.screenShotPath : null;
            }
        },
        areOtherSchemesSelected: function (theme, colorScheme) {
            for (var i = 0; i < theme['colorSchemes'].length; i++) {
                if (theme['colorSchemes'][i].id != colorScheme.id && theme['colorSchemes'][i].selected != true) {
                    return false;
                }
            }
            return true;
        },
        areOtherSchemesUnselected: function (theme, colorScheme) {
            for (var i = 0; i < theme['colorSchemes'].length; i++) {
                if (theme['colorSchemes'][i].id != colorScheme.id && theme['colorSchemes'][i].selected == true) {
                    return false;
                }
            }
            return true;
        }
    }
};

var lookAndFeelServices = angular.module('lookAndFeelServices', [])
    .service('lookAndFeelService', [lookAndFeelService]);