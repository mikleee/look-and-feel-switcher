<%@ include file="../init.jspf" %>

<script src="<c:url value="/js/plugins/directives.js"/>"></script>
<script src="<c:url value="/js/plugins/message.js"/>"></script>
<script src="<c:url value="/js/look-and-feel-list.js"/>"></script>

<%--@elvariable id="themes" type="java.util.List<com.liferay.portal.model.Theme>"--%>
<%--@elvariable id="themeDisplay" type="com.liferay.portal.theme.ThemeDisplay"--%>
<%--@elvariable id="screenShotPath" type="java.lang.String"--%>
<%--@elvariable id="colorSchemes" type="java.util.List<com.liferay.portal.model.ColorScheme>"--%>
<%--@elvariable id="lookAndFeelBinding" type="com.aimprosoft.lfs.model.persist.LookAndFeelBinding"--%>

<%--<portlet:actionURL var="resetBindingUrl">--%>
<%--<portlet:param name="action" value="resetBinding"/>--%>
<%--<portlet:param name="redirectURL" value="${startPage}"/>--%>
<%--<portlet:param name="userId" value="${themeDisplay.userId}"/>--%>
<%--<portlet:param name="groupId" value="${themeDisplay.scopeGroupId}"/>--%>
<%--<portlet:param name="lookAndFeel.companyId" value="${themeDisplay.companyId}"/>--%>
<%--<portlet:param name="companyId" value="${themeDisplay.companyId}"/>--%>
<%--<portlet:param name="sessionId" value="${themeDisplay.sessionId}"/>--%>
<%--</portlet:actionURL>--%>

<portlet:resourceURL var="resetBindingUrl" id="resetBinding">
    <portlet:param name="action" value="resetBinding"/>
    <portlet:param name="userId" value="${themeDisplay.userId}"/>
    <portlet:param name="groupId" value="${themeDisplay.scopeGroupId}"/>
    <portlet:param name="lookAndFeel.companyId" value="${themeDisplay.companyId}"/>
    <portlet:param name="companyId" value="${themeDisplay.companyId}"/>
    <portlet:param name="sessionId" value="${themeDisplay.sessionId}"/>
</portlet:resourceURL>
<portlet:resourceURL var="applyBindingUrl" id="applyBinding">
    <portlet:param name="userId" value="${themeDisplay.userId}"/>
    <portlet:param name="groupId" value="${themeDisplay.scopeGroupId}"/>
    <portlet:param name="lookAndFeel.companyId" value="${themeDisplay.companyId}"/>
    <portlet:param name="companyId" value="${themeDisplay.companyId}"/>
    <portlet:param name="sessionId" value="${themeDisplay.sessionId}"/>
</portlet:resourceURL>
<portlet:resourceURL var="initLookAndFeelUrl" id="initLookAndFeel">
    <portlet:param name="userId" value="${themeDisplay.userId}"/>
    <portlet:param name="groupId" value="${themeDisplay.scopeGroupId}"/>
    <portlet:param name="companyId" value="${themeDisplay.companyId}"/>
    <portlet:param name="lookAndFeel.companyId" value="${themeDisplay.companyId}"/>
    <portlet:param name="sessionId" value="${themeDisplay.sessionId}"/>
</portlet:resourceURL>

<c:set var="module" value="selectLookAndFeel${ns}"/>


<div id="${module}" ng-cloak>
    <div class="ts-container ts-look-and-feel-list" ng-controller="lookAndFeelListController">
        <ts-global-message></ts-global-message>

        <div>
            <h3><span><liferay-ui:message key="ts-select-theme"/></span><ts-spinner ng-show="isLocked()"></ts-spinner></h3>
            <div class="ts-row">
                <div class="ts-column look-and-feel-list-control">
                    <div>
                        <label for="${ns}themes"><liferay-ui:message key="ts-themes"/></label>
                        <select id="${ns}themes" class="ts-select" ng-disabled="isLocked()" ng-options="theme as theme.name for theme in models.lookAndFeels track by theme.id" ng-model="models.currentTheme"></select>
                    </div>
                    <div ng-if="models.currentTheme && models.currentTheme.hasColorSchemes()">
                        <label for="${ns}color-schemes"><liferay-ui:message key="ts-color-schemes"/></label>
                        <select id="${ns}color-schemes" class="ts-select" ng-disabled="isLocked()" ng-options="cs as cs.name for cs in models.currentTheme.colorSchemes track by cs.id" ng-model="models.currentColorScheme"></select>
                    </div>
                </div>
                <div class="ts-column">
                    <ts-screenshot src="getScreenshotPath()" alt="<liferay-ui:message key="ts-screenshot-is-not-available"/>"></ts-screenshot>
                </div>
            </div>
            <div class="ts-row button-footer">
                <button class="btn btn-primary" ng-disabled="isLocked()" ng-click="applyBinding()"><liferay-ui:message key="ts-apply"/></button>
                <button class="btn btn-default" ng-disabled="isLocked()" ng-click="resetBinding()" ng-if="models.lookAndFeelBinding.id != null"><liferay-ui:message key="ts-reset-to-default"/></button>
            </div>
        </div>
    </div>
</div>

<script>
    (function () {
        var config = {
            resetBindingUrl: '${resetBindingUrl}',
            initLookAndFeelUrl: '${initLookAndFeelUrl}',
            applyBindingUrl: '${applyBindingUrl}',
            lookAndFeelBinding: new ThemesSwitcher.models.LookAndFeelBinding().fromJson('${lookAndFeelBinding}')
        };

        angular.module('ts-lookAndFeelList').constant('config', config);
        angular.module('${module}', ['ts-directives', 'ts-lookAndFeelList', 'ui.bootstrap']);
        angular.bootstrap(document.getElementById('${module}'), ['${module}']);
    })();
</script>