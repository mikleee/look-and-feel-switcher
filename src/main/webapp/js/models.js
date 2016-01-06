/**
 * @constructor
 */
function BaseModel() {
    this.fromJson = function (json) {
        if (json) {
            var obj = angular.fromJson(json);
            angular.merge(this, obj);
        }
        return this;
    };
    this.fromObject = function (obj) {
        angular.merge(this, obj);
        return this;
    };
}

/**
 * @constructor
 */
function LookAndFeelOption() {
    this.id = null;
    this.name = null;
    this.selected = false;
    this.bind = false;
    this.portalDefault = false;
    this.screenShotPath = null;
    this.lookAndFeelId = null;
}
LookAndFeelOption.prototype = new BaseModel();

/**
 * @constructor
 */
function Theme() {
    /**
     * @type {[LookAndFeelOption]}
     */
    this.colorSchemes = [];
    this.hasColorSchemes = function () {
        return this.colorSchemes && this.colorSchemes.length > 0;
    }
}
Theme.prototype = new LookAndFeelOption();

/**
 * @constructor
 */
function LookAndFeelBinding() {
    this.id = null;
    /**
     * @type {LookAndFeel}
     */
    this.lookAndFeel = null;
    this.userId = null;
    this.groupId = null;
    this.companyId = null;
}
LookAndFeelBinding.prototype = new BaseModel();

/**
 *
 * @constructor
 */
function LookAndFeel(id, shown, companyId) {
    this.id = id;
    this.themeId = null;
    this.colorSchemeId = null;
    this.companyId = companyId;
    this.shown = shown
}

