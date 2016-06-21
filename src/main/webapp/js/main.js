(function () {
    if (!window.ThemesSwitcher) {
        window.ThemesSwitcher = {};

        window.ThemesSwitcher.getMessage = getMessage;
        window.ThemesSwitcher.sequence = new Sequence();
        window.ThemesSwitcher.state = {
            ERROR: 'error',
            SUCCESS: 'success',
            WAITING: 'waiting',
            WARNING: 'warning'
        };


        function getMessage(key, args) {
            var template = Liferay.Language.get(key);
            if (args && args.length > 0) {
                var result = template;
                for (var i = 0; i < args.length; i++) {
                    result = result.replace('%s', args[i]);
                }
                return result;
            } else {
                return template;
            }
        }

        function Sequence() {
            var cursor = Math.ceil(Math.random() * 1000 + 1000);
            this.getUnique = function () {
                return cursor++;
            }
        }
    }
})();

(function () {
    if (!window.ThemesSwitcher.models) {
        window.ThemesSwitcher.models = {};

        window.ThemesSwitcher.models.BaseModel = BaseModel;
        window.ThemesSwitcher.models.LookAndFeelOption = LookAndFeelOption;
        window.ThemesSwitcher.models.Theme = Theme;
        window.ThemesSwitcher.models.LookAndFeelBinding = LookAndFeelBinding;
        window.ThemesSwitcher.models.LookAndFeel = LookAndFeel;


        /** @constructor */
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

        /** @constructor */
        function LookAndFeelOption() {
            this.id = null;
            this.name = null;
            this.selected = false;
            this.bind = false;
            this.portalDefault = false;
            this.screenShotPath = null;
            this.lookAndFeelId = null;
            /** @type {[{name: String, id: String, permitted: Boolean}]} */
            this.actions = [];
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

        /** @constructor */
        function Theme() {
            /** @type {[LookAndFeelOption]} */
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

        /** @constructor */
        function LookAndFeelBinding() {
            this.id = null;
            /** @type {LookAndFeel} */
            this.lookAndFeel = null;
            this.userId = null;
            this.groupId = null;
            this.companyId = null;
        }

        LookAndFeelBinding.prototype = new BaseModel();

        /** @constructor */
        function LookAndFeel(id, companyId) {
            this.id = id;
            this.themeId = null;
            this.colorSchemeId = null;
            this.companyId = companyId;
        }

    }
})();

