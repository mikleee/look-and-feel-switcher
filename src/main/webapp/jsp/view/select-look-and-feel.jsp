<%@ include file="../init.jspf" %>

<script src="<c:url value="/js/models.js"/>"></script>
<script src="<c:url value="/js/look-and-feel-service.js"/>"></script>
<script src="<c:url value="/js/message-controller.js"/>"></script>
<script src="<c:url value="/js/select-look-and-feel.js"/>"></script>

<%--@elvariable id="themes" type="java.util.List<com.liferay.portal.model.Theme>"--%>
<%--@elvariable id="themeDisplay" type="com.liferay.portal.theme.ThemeDisplay"--%>
<%--@elvariable id="screenShotPath" type="java.lang.String"--%>
<%--@elvariable id="colorSchemes" type="java.util.List<com.liferay.portal.model.ColorScheme>"--%>
<%--@elvariable id="lookAndFeelBinding" type="com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelBinding"--%>

<portlet:defineObjects/>

<portlet:actionURL var="resetBindingUrl">
    <portlet:param name="action" value="resetBinding"/>
    <portlet:param name="redirectURL" value="${startPage}"/>
    <portlet:param name="userId" value="${themeDisplay.userId}"/>
    <portlet:param name="groupId" value="${themeDisplay.scopeGroupId}"/>
    <portlet:param name="lookAndFeel.companyId" value="${themeDisplay.companyId}"/>
    <portlet:param name="companyId" value="${themeDisplay.companyId}"/>
    <portlet:param name="sessionId" value="${themeDisplay.sessionId}"/>
</portlet:actionURL>

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

<script>
    angular.module('${ns}selectLookAndFeel', ['lookAndFeelServices'])
            .controller('messageController', ['$scope', MessageController])
            .controller('selectLookAndFeelController', ['$scope', '$http', 'lookAndFeelService', 'initConfig', SelectLookAndFeelController])
            .service('initConfig', function () {
                return {
                    ns: '${ns}',
                    resetBindingUrl: '${resetBindingUrl}',
                    initLookAndFeelUrl: '${initLookAndFeelUrl}',
                    applyBindingUrl: '${applyBindingUrl}',
                    lookAndFeelBinding: new LookAndFeelBinding().fromJson('${lookAndFeelBinding}')
                }
            });
</script>


<div ng-app="${ns}selectLookAndFeel">
    <div class="ts-container" ng-controller="messageController" ng-cloak>
        <div ng-show="message" class="alert" ng-class="expressions.messageStyle() + 'ts-message'" ng-bind="message"></div>
        <div ng-controller="selectLookAndFeelController">
            <div class="row-fluid">
                <div class="span5">
                    <h3><liferay-ui:message key="ts-select-theme"/></h3>

                    <div>
                        <label for="${ns}themes"><liferay-ui:message key="ts-themes"/></label>
                        <select id="${ns}themes" class="lfb-select" ng-disabled="expressions.disableFormCondition()"
                                ng-options="theme as theme.name for theme in models.lookAndFeels track by theme.id" ng-model="models.currentTheme"></select>
                    </div>
                    <div ng-if="models.currentTheme && models.currentTheme.hasColorSchemes()">
                        <label for="${ns}color-schemes"><liferay-ui:message key="ts-color-schemes"/></label>
                        <select id="${ns}color-schemes" class="lfb-select" ng-disabled="expressions.disableFormCondition()"
                                ng-options="cs as cs.name for cs in models.currentTheme.colorSchemes track by cs.id" ng-model="models.currentColorScheme"></select>
                    </div>
                </div>
                <div class="span7">
                    <img ng-src="{{expressions.screenShotPath()}}" class="ts-screen-shot">
                </div>
            </div>
            <div class="row-fluid">
                <button class="btn btn-primary" ng-disabled="expressions.disableFormCondition()"
                        ng-click="listeners.applyBinding()"><liferay-ui:message key="ts-apply"/></button>
                <button class="btn btn-default" ng-disabled="expressions.disableFormCondition()" ng-click="listeners.resetBinding()"
                        ng-if="models.lookAndFeelBinding.id != null"><liferay-ui:message key="ts-reset-to-default"/></button>
            </div>
        </div>
    </div>
</div>