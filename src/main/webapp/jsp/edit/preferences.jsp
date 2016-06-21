<%@ include file="../init.jspf" %>

<link rel="stylesheet" href="<c:url value="/css/paginator.css"/>">
<script src="<c:url value="/js/plugins/directives.js"/>"></script>
<script src="<c:url value="/js/plugins/message.js"/>"></script>
<script src="<c:url value="/js/look-and-feel-list.js"/>"></script>
<script src="<c:url value="/js/preferences.js"/>"></script>
<script src="<c:url value="/js/plugins/paginator.js"/>"></script>

<%--@elvariable id="themes" type="java.util.List<com.liferay.portal.model.Theme>"--%>
<%--@elvariable id="themeDisplay" type="com.liferay.portal.theme.ThemeDisplay"--%>
<%--@elvariable id="screenShotPath" type="java.lang.String"--%>
<%--@elvariable id="colorSchemes" type="java.util.List<com.liferay.portal.model.ColorScheme>"--%>
<%--@elvariable id="actions" type="java.util.List<com.aimprosoft.lfs.model.view.Action>"--%>


<portlet:resourceURL var="initLookAndFeelUrl" id="getLookAndFeelMap">
    <portlet:param name="companyId" value="${themeDisplay.companyId}"/>
</portlet:resourceURL>

<c:set var="module" value="lookAndFeelPreferences${ns}"/>


<div id="${module}" class="aui" ng-cloak>
    <div class="ts-container">
        <div ng-controller="preferencesController">
            <div>
                <ul class="nav nav-tabs">
                    <li class="tab" ng-class="{active : tab == 'permissions'}" ng-click="setTab('permissions')">
                        <a href="javascript:void(0)"><liferay-ui:message key="ts-permissions"/></a>
                    </li>
                    <li class="tab" ng-class="{active : tab == 'administration'}" ng-click="setTab('administration')">
                        <a href="javascript:void(0)"><liferay-ui:message key="ts-administration"/></a>
                    </li>
                </ul>
            </div>
            <div>
                <ts-global-message></ts-global-message>
                <div ng-switch="tab">
                    <div ng-switch-when="permissions">
                        <div class="alert alert-danger">not implemented yet</div>
                        <%@ include file="./look-and-feel-permissions.jsp" %>
                    </div>
                    <div ng-switch-when="administration">
                        <%@ include file="./administration.jsp" %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<script>
    (function () {
        var config = {
            ns: '${ns}',
            initLookAndFeelUrl: '${initLookAndFeelUrl}',
            fetchPermissionsUrl: '<portlet:resourceURL id="fetchPermissions"/>',
            applyPermissionsUrl: '<portlet:resourceURL id="applyPermissions"/>',
            setDefaultPermissionsUrl: '<portlet:resourceURL id="setDefaultPermissions"/>',
            removeAllBindingsUrl: '<portlet:resourceURL id="removeAllBindings"/>',
            bindingsStatUrl: '<portlet:resourceURL id="bindingsStatUrl"/>'
        };

        angular.module('ts-lookAndFeelList').constant('config', config);
        angular.module('ts-preferencesAdministration').constant('config', config);
        angular.module('ts-preferencesPermissions').constant('config', config);

        angular.module('${module}', ['ts-directives', 'ts-lookAndFeelList', 'ts-preferences', 'ui.bootstrap']);
        angular.bootstrap(document.getElementById('${module}'), ['${module}']);
    })();
</script>