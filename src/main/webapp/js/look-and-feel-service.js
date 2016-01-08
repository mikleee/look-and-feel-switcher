/**
 * @constructor
 */
var LookAndFeelService = function () {
    /**
     * @type {{lookAndFeelBinding: LookAndFeelBinding, lookAndFeels: [Theme], currentTheme: Theme, currentColorScheme: LookAndFeelOption, permissionMap: [RolePermissions]}}
     */
    var models = {
        lookAndFeelBinding: null,
        lookAndFeels: [],
        currentTheme: null,
        currentColorScheme: null,
        permissionMap: []
    };

    var util = {
        getBindLookAndFeel: function (models) {
            for (var i = 0; i < models.length; i++) {
                if (models[i]['bind']) {
                    return models[i];
                }
            }
            return null;
        },
        /**
         * @param lookAndFeels {Theme}
         * @returns {Theme}
         */
        getPreselectedTheme: function (lookAndFeels) {
            if (lookAndFeels.length > 0) {
                var bindTheme = util.getBindLookAndFeel(lookAndFeels);
                return new Theme().fromObject(bindTheme ? bindTheme : lookAndFeels[0]);
            }
        },
        /**
         * @param currentTheme{Theme}
         * @returns {LookAndFeelOption}
         */
        getPreselectedColorScheme: function (currentTheme) {
            if (currentTheme && currentTheme.colorSchemes && currentTheme.colorSchemes.length > 0) {
                var bindTheme = util.getBindLookAndFeel(currentTheme.colorSchemes);
                return new LookAndFeelOption().fromObject(bindTheme ? bindTheme : currentTheme.colorSchemes[0]);
            } else {
                return null;
            }
        }
    };


    this.setLookAndFeels = function (lookAndFeels) {
        models.lookAndFeels = [];
        angular.forEach(lookAndFeels, function (v, k) {
            models.lookAndFeels.push(new Theme().fromObject(v));
        });
        models.currentTheme = util.getPreselectedTheme(lookAndFeels);
        models.currentColorScheme = util.getPreselectedColorScheme(models.currentTheme);
    };
    this.setLookAndFeelBinding = function (lookAndFeelBinding) {
        models.lookAndFeelBinding = lookAndFeelBinding;
        if (!lookAndFeelBinding.lookAndFeel) {
            models.lookAndFeelBinding.lookAndFeel = new LookAndFeel(null, null, lookAndFeelBinding.companyId);
        }
    };
    this.getScreenshotPath = function () {
        if (models.currentColorScheme) {
            return models.currentColorScheme.screenShotPath;
        } else {
            return models.currentTheme ? models.currentTheme.screenShotPath : null;
        }
    };
    /**
     * @param currentTheme
     * @returns {*|LookAndFeelOption}
     */
    this.getPreselectedColorScheme = function (currentTheme) {
        return util.getPreselectedColorScheme(currentTheme);
    };
    /**
     * @returns {{lookAndFeelBinding: LookAndFeelBinding, lookAndFeels: [Theme], currentTheme: Theme, currentColorScheme: LookAndFeelOption}}
     */
    this.getModels = function () {
        return models;
    };
    /**
     * @returns {boolean}
     */
    this.isNoData = function () {
        return models.lookAndFeels.length == 0;
    };

};

var lookAndFeelServices = angular.module('lookAndFeelServices', [])
    .service('lookAndFeelService', [LookAndFeelService]);