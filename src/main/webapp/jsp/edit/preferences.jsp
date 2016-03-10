<%@ include file="../init.jspf" %>

<script src="<c:url value="/js/models.js"/>"></script>
<script src="<c:url value="/js/directives.js"/>"></script>
<script src="<c:url value="/js/message-controller.js"/>"></script>
<script src="<c:url value="/js/look-and-feel-service.js"/>"></script>
<script src="<c:url value="/js/preferences.js"/>"></script>

<%--@elvariable id="themes" type="java.util.List<com.liferay.portal.model.Theme>"--%>
<%--@elvariable id="themeDisplay" type="com.liferay.portal.theme.ThemeDisplay"--%>
<%--@elvariable id="screenShotPath" type="java.lang.String"--%>
<%--@elvariable id="colorSchemes" type="java.util.List<com.liferay.portal.model.ColorScheme>"--%>
<%--@elvariable id="actions" type="java.util.List<com.aimprosoft.look_and_feel_switcher.model.view.Action>"--%>

<portlet:defineObjects/>

<portlet:resourceURL var="initLookAndFeelUrl" id="getLookAndFeelMap">
    <portlet:param name="companyId" value="${themeDisplay.companyId}"/>
</portlet:resourceURL>

<portlet:resourceURL var="permissionsTemplateUrl" id="getTemplate">
    <portlet:param name="template" value="edit/look-and-feel-permissions"/>
</portlet:resourceURL>
<portlet:resourceURL var="administrationTemplateUrl" id="getTemplate">
    <portlet:param name="template" value="edit/administration"/>
</portlet:resourceURL>

<script>

    /**
     * @constructor
     */
    var ${ns}PortletConfig = function PortletConfig() {
        this.ns = '${ns}';
        this.initLookAndFeelUrl = '${initLookAndFeelUrl}';
        this.fetchPermissionsUrl = '<portlet:resourceURL id="fetchPermissions"/>';
        this.applyPermissionsUrl = '<portlet:resourceURL id="applyPermissions"/>';
        this.setDefaultPermissionsUrl = '<portlet:resourceURL id="setDefaultPermissions"/>';
        this.removeAllBindingsUrl = '<portlet:resourceURL id="removeAllBindings"/>';
        this.bindingsStatUrl = '<portlet:resourceURL id="bindingsStatUrl"/>';
    };

    angular.module('${ns}lookAndFeelAdministration', ['lookAndFeelServices', 'ngRoute', 'tsDirectives'])
            .controller('preferencesController', ['$scope', '$location', PreferencesController])
            .controller('messageController', ['$scope', MessageController])
            .controller('selectLookAndFeelPreferencesController', ['$scope', '$http', 'lookAndFeelService', 'portletConfig', SelectLookAndFeelPreferencesController])
            .controller('lookAndFeelPermissionsController', ['$scope', '$http', 'lookAndFeelService', 'portletConfig', LookAndFeelPermissionsController])
            .controller('adminController', ['$scope', '$http', 'portletConfig', LookAndFeelAdministrationController])
            .service('portletConfig', ${ns}PortletConfig)
            .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
                $routeProvider
                        .when('/permissions', {
                            templateUrl: '${permissionsTemplateUrl}'
                        }).when('/administration', {
                            templateUrl: '${administrationTemplateUrl}'
                        }).otherwise({
                            templateUrl: '${permissionsTemplateUrl}'
                        });
            }]);
</script>

<div ng-app="${ns}lookAndFeelAdministration" class="aui" ng-cloak>
    <div class="ts-container">
        <div ng-controller="preferencesController">
            <div>
                <ul class="nav nav-tabs">
                    <li class="tab" ng-class="{active : tab == 'permissions'}" ng-click="listeners.onTabChange('permissions')">
                        <a href="#permissions"><liferay-ui:message key="ts-permissions"/></a>
                    </li>
                    <li class="tab" ng-class="{active : tab == 'administration'}" ng-click="listeners.onTabChange('administration')">
                        <a href="#administration"><liferay-ui:message key="ts-administration"/></a>
                    </li>
                </ul>
            </div>
            <div>
                <div class="ts-container" ng-controller="messageController">
                    <div ng-show="message" class="alert" ng-class="messageStyle + ' ts-message'" ng-bind="message"></div>
                    <ng-view></ng-view>
                </div>
            </div>
        </div>
    </div>
</div>