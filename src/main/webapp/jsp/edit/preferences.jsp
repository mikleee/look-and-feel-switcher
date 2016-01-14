<%@ include file="../init.jspf" %>


<script src="<c:url value="/js/models.js"/>"></script>
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

<script>
    angular.module('${ns}lookAndFeelAdministration', ['lookAndFeelServices', 'ngRoute'])
            .controller('preferencesController', ['$scope', PreferencesController])
            .controller('messageController', ['$scope', MessageController])
            .controller('selectLookAndFeelPreferencesController', ['$scope', '$http', 'lookAndFeelService', 'portletConfig', SelectLookAndFeelPreferencesController])
            .controller('lookAndFeelPermissionsController', ['$scope', '$http', 'lookAndFeelService', 'portletConfig', LookAndFeelPermissionsController])
            .service('portletConfig', function () {
                return {
                    ns: '${ns}',
                    initLookAndFeelUrl: '${initLookAndFeelUrl}',
                    fetchPermissionsUrl: '<portlet:resourceURL id="permissionTable"/>',
                    applyPermissionsUrl: '<portlet:resourceURL id="applyPermissions"/>'
                }
            })
            .config(function ($routeProvider, $locationProvider) {
                $routeProvider
                        .when('/permissions', {
                            templateUrl: '${pageContext.request.contextPath}/jsp/edit/look-and-feel-permissions.jsp'
                        }).when('/administration', {
                            templateUrl: '${pageContext.request.contextPath}/jsp/edit/administration.jsp'
                        }).otherwise({
                            templateUrl: '${pageContext.request.contextPath}/jsp/edit/look-and-feel-permissions.jsp'
                        });
            });
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
                    <div ng-show="message" class="alert" ng-class="expressions.messageStyle() + 'ts-message'" ng-bind="message"></div>
                    <ng-view></ng-view>
                </div>
            </div>
        </div>
    </div>
</div>