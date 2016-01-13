<%@ include file="/jsp/init.jspf" %>

<script src="<c:url value="/js/models.js"/>"></script>
<script src="<c:url value="/js/look-and-feel-service.js"/>"></script>
<script src="<c:url value="/js/administration.js"/>"></script>

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
    angular.module('${ns}lookAndFeelAdministration', ['lookAndFeelServices'])
            .controller('selectLookAndFeelAdministrationController', ['$scope', '$http', 'lookAndFeelService', 'portletConfig', SelectLookAndFeelAdministrationController])
            .controller('lookAndFeelPermissionsController', ['$scope', '$http', 'lookAndFeelService', 'portletConfig', LookAndFeelPermissionsController])
            .service('portletConfig', function () {
                return {
                    ns: '${ns}',
                    initLookAndFeelUrl: '${initLookAndFeelUrl}',
                    fetchPermissionsUrl: '<portlet:resourceURL id="permissionTable"/>',
                    applyPermissionsUrl: '<portlet:resourceURL id="applyPermissions"/>'
                }
            });
</script>

<div ng-app="${ns}lookAndFeelAdministration" class="aui" ng-cloak>

    <div class="lfs-container" ng-controller="selectLookAndFeelAdministrationController">
        <div>
            <div ng-if="models.message && models.status != 'waiting'" class="alert" ng-class="expressions.messageStyle()" ng-bind="models.message"></div>
            <div>
                <div class="row-fluid">
                    <div class="span4">
                        <div>
                            <label for="${ns}themes"><liferay-ui:message key="lfs-themes"/></label>
                            <select id="${ns}themes" class="lfb-select" ng-disabled="expressions.disableFormCondition()"
                                    ng-options="theme as theme.name for theme in models.lookAndFeels track by theme.id" ng-model="models.currentTheme"
                                    ng-change="listeners.onLookAndFeelChange()"></select>
                        </div>
                        <div ng-if="models.currentTheme && models.currentTheme.hasColorSchemes()">
                            <label for="${ns}color-schemes"><liferay-ui:message key="lfs-color-schemes"/></label>
                            <select id="${ns}color-schemes" class="lfb-select" ng-disabled="expressions.disableFormCondition()"
                                    ng-options="cs as cs.name for cs in models.currentTheme.colorSchemes track by cs.id" ng-model="models.currentColorScheme"
                                    ng-change="listeners.onLookAndFeelChange()"></select>
                        </div>
                        <div>
                            <img ng-src="{{expressions.screenShotPath()}}" class="lfs-screen-shot">
                        </div>
                    </div>
                    <div class="span8" ng-controller="lookAndFeelPermissionsController">
                        <div class="navbar navbar-inner container-fluid">
                            <div class="row-fluid">
                                <div>
                                    <p class="span6 navbar-text"><liferay-ui:message key="lfs-look-and-feel-permissions"/></p>
                                </div>
                                <div class="span2 offset4">
                                    <button type="button" class="btn btn-primary navbar-btn" ng-click="listeners.submitPermissions()" ng-disabled="expressions.disableCondition()"><liferay-ui:message key="submit"/></button>
                                </div>
                            </div>
                        </div>
                        <div>
                            <div class="lfs-locker" ng-if="expressions.disableCondition()"></div>
                            <table class="table table-bordered table-hover table-striped">
                                <thead class="table-columns">
                                    <tr>
                                        <th><liferay-ui:message key="lfs-role"/></th>
                                        <c:forEach items="${actions}" var="action">
                                            <th><label><liferay-ui:message key="${action.name}"/></label></th>
                                        </c:forEach>
                                    </tr>
                                </thead>
                                <tbody class="table-data">
                                    <tr ng-repeat="p in models.permissionMap track by p.role.name" class="{{'lfr-role lfr-role-' + p.role.type}}">
                                        <td class="first">{{p.role.name}}</td>
                                        <td ng-repeat="a in p track by a.id">
                                            <input type="checkbox" ng-model="p.actions[$index].permitted"/>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>