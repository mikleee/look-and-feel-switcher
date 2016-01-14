/**
 * @constructor
 */
var LookAndFeelService = function () {
    /**
     * @type {{lookAndFeelBinding: LookAndFeelBinding, lookAndFeels: [Theme], currentTheme: Theme, currentColorScheme: LookAndFeelOption, resourcePermissions: ResourcePermissions}}
     */
    var models = {
        lookAndFeelBinding: null,
        lookAndFeels: [],
        currentTheme: null,
        currentColorScheme: null,
        resourcePermissions: []
    };

    var util = {
        /**
         * @param models: {[LookAndFeelOption]}
         * @returns {LookAndFeelOption}
         */
        getBindLookAndFeel: function (models) {
            for (var i = 0; i < models.length; i++) {
                if (models[i].bind) {
                    return models[i];
                }
            }
            return null;
        },
        /**
         * @param lookAndFeels {[Theme]}
         * @returns {Theme}
         */
        getPreselectedTheme: function (lookAndFeels) {
            if (lookAndFeels.length > 0) {
                var bindTheme = util.getBindLookAndFeel(lookAndFeels);
                return bindTheme ? bindTheme : lookAndFeels[0];
            }
        },
        /**
         * @param currentTheme {Theme}
         * @returns {LookAndFeelOption}
         */
        getPreselectedColorScheme: function (currentTheme) {
            if (currentTheme && currentTheme.colorSchemes && currentTheme.colorSchemes.length > 0) {
                var bindCs = util.getBindLookAndFeel(currentTheme.colorSchemes);
                return bindCs ? bindCs : currentTheme.colorSchemes[0];
            } else {
                return null;
            }
        },
        /**
         * @param rolePermissions {RolePermissions}
         * @param id
         * @returns {Action}
         */
        getActionById: function (rolePermissions, id) {
            for (var i = 0; i < rolePermissions.actions.length; i++) {
                if (rolePermissions.actions[i].id == id) {
                    return rolePermissions.actions[i];
                }
            }
            return null;
        }
    };


    this.setLookAndFeels = function (lookAndFeels) {
        models.lookAndFeels = [];
        angular.forEach(lookAndFeels, function (v) {
            models.lookAndFeels.push(new Theme().fromObject(v));
        });
        models.currentTheme = util.getPreselectedTheme(models.lookAndFeels);
        models.currentColorScheme = util.getPreselectedColorScheme(models.currentTheme);
    };
    this.setResourcePermissions = function (resourcePermissions) {
        models.resourcePermissions = resourcePermissions;
        angular.forEach(models.resourcePermissions.allowedActions, function (v) {
            v.name = lfsConstants.getMessage(v.name);
        });
    };
    this.setLookAndFeelBinding = function (lookAndFeelBinding) {
        models.lookAndFeelBinding = lookAndFeelBinding;
        if (!lookAndFeelBinding.lookAndFeel) {
            models.lookAndFeelBinding.lookAndFeel = new LookAndFeel(null, lookAndFeelBinding.companyId);
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
     * @returns {LookAndFeelOption}
     */
    this.getPreselectedColorScheme = function (currentTheme) {
        return util.getPreselectedColorScheme(currentTheme);
    };
    /**
     * @returns {{lookAndFeelBinding: LookAndFeelBinding, lookAndFeels: [Theme], currentTheme: Theme, currentColorScheme: LookAndFeelOption, resourcePermissions: ResourcePermissions}}
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
    /**
     * @returns {LookAndFeelOption}
     */
    this.getActiveLookAndFeelOption = function () {
        return models.currentColorScheme ? models.currentColorScheme : models.currentTheme;
    };
    /**
     * @returns {LookAndFeel}
     */
    this.getActiveLookAndFeel = function () {
        var lookAndFeelOption = this.getActiveLookAndFeelOption();
        return new LookAndFeel(lookAndFeelOption.id, models.lookAndFeelBinding.companyId);
    };
    this.toggleAction = function (actionId) {
        var checkedCount = 0, perspectiveState;

        angular.forEach(models.resourcePermissions.permissions, function (v) {
            var a = util.getActionById(v, actionId);
            if (a && a.permitted) {
                checkedCount++;
            }
        });

        perspectiveState = checkedCount != models.resourcePermissions.permissions.length;

        angular.forEach(models.resourcePermissions.permissions, function (v) {
            var a = util.getActionById(v, actionId);
            if (a) {
                a.permitted = perspectiveState;
            }
        });
    }
};

var lookAndFeelServices = angular.module('lookAndFeelServices', [])
    .service('lookAndFeelService', [LookAndFeelService]);