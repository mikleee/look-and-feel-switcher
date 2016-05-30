/**
 * @constructor
 */
function LookAndFeelService() {

    var models = {
        /**
         * @type {LookAndFeelBinding}
         */
        lookAndFeelBinding: null,
        /**
         * @type [Theme]
         */
        lookAndFeels: [],
        /**
         * @type {Theme}
         */
        currentTheme: null,
        /**
         * @type {LookAndFeelOption}
         */
        currentColorScheme: null,
        /**
         * @type {ResourcePermissions}
         */
        resourcePermissions: null
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
            v.name = tsConstants.getMessage(v.name);
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
    this.toggleAction = function (actionId, permissions) {
        var checkedCount = 0, perspectiveState;

        angular.forEach(permissions, function (v) {
            var a = util.getActionById(v, actionId);
            if (a && a.permitted) {
                checkedCount++;
            }
        });

        perspectiveState = checkedCount != permissions.length;

        angular.forEach(permissions, function (v) {
            var a = util.getActionById(v, actionId);
            if (a) {
                a.permitted = perspectiveState;
            }
        });
    };
}

/**
 * @constructor
 */
function PaggedTable() {
    var page = 1;
    var pageSize;
    var pageSizes;
    var collection;
    var pages;
    var isApplicable;

    var init = function () {
        pages = [];
        var cursor = 0;
        while (cursor < collection.length - 1) {
            var page = [];
            var startIndex = cursor;
            var endIndex = (startIndex + pageSize) > collection.length ? collection.length : startIndex + pageSize;
            for (var i = cursor; i < endIndex; i++) {
                page.push(collection[i]);
            }
            pages.push(page);
            cursor += pageSize;
        }
    };

    this.init = function (config) {
        collection = config.collection;
        pageSize = config.pageSize;
        pageSizes = config.pageSizes;
        {
            var minPageSize = pageSizes[0];
            for (var i = 0; i < pageSizes.length; i++) {
                minPageSize = Math.min(minPageSize, pageSizes[i]);
            }
            isApplicable = minPageSize < collection.length;
        }

        page = config.page;
        init();
    };

    this.setPageSize = function (size) {
        pageSize = size;
        page = 1;
        init();
    };

    this.getPageSize = function () {
        return pageSize;
    };

    this.getPageSizes = function () {
        return pageSizes;
    };

    this.setPage = function (p) {
        page = p;
    };
    this.getPage = function () {
        return page;
    };


    this.getCurrentPage = function () {
        return pages && pages.length > 0 ? pages[page - 1] : [];
    };

    this.getPages = function () {
        return pages;
    };

    this.isApplicable = function () {
        return isApplicable;
    };

}

var lookAndFeelServices = angular.module('lookAndFeelServices', [])
    .service('lookAndFeelService', [LookAndFeelService]);