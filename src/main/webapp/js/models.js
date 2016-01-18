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
    /**
     * @type {[{name: String, id: String, permitted: Boolean}]}
     */
    this.actions = [];
    /**
     * @param action
     * @returns {boolean}
     */
    this.isActionPermitted = function (action) {
        for (var i = 0; i < this.actions.length; i++) {
            if (this.actions[i].name == action) {
                return this.actions[i].permitted;
            }
        }
        return false;
    };
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
    };
    this.fromObject = function (obj) {
        angular.merge(this, obj);
        this.colorSchemes = [];
        for (var i = 0; i < obj.colorSchemes.length; i++) {
            this.colorSchemes.push(new LookAndFeelOption().fromObject(obj.colorSchemes[i]));
        }
        return this;
    };
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
 * @constructor
 */
function LookAndFeel(id, companyId) {
    this.id = id;
    this.themeId = null;
    this.colorSchemeId = null;
    this.companyId = companyId;
}

/**
 * @constructor
 */
function ResourcePermissions() {
    this.id = null;
    /**
     * @type {[Action]}
     */
    this.allowedActions = [];
    /**
     * @type {[RolePermissions]}
     */
    this.permissions = [];
}


/**
 * @constructor
 */
function RolePermissions() {
    /**
     * @type {{name: String, type: String, description: String}}
     */
    this.role = {};
    /**
     * @type {[Action]}
     */
    this.actions = [];
}

/**
 * @constructor
 */
function Action() {
    this.id = null;
    this.name = null;
    this.permitted = false;
}

