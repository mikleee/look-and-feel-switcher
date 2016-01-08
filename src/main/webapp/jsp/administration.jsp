<%@ include file="/jsp/init.jspf" %>

<script src="<c:url value="/js/models.js"/>"></script>
<script src="<c:url value="/js/look-and-feel-service.js"/>"></script>
<script src="<c:url value="/js/administration.js"/>"></script>

<%--@elvariable id="themes" type="java.util.List<com.liferay.portal.model.Theme>"--%>
<%--@elvariable id="themeDisplay" type="com.liferay.portal.theme.ThemeDisplay"--%>
<%--@elvariable id="screenShotPath" type="java.lang.String"--%>
<%--@elvariable id="colorSchemes" type="java.util.List<com.liferay.portal.model.ColorScheme>"--%>
<%--@elvariable id="actions" type="java.util.List<java.lang.String>"--%>

<portlet:defineObjects/>

<portlet:resourceURL var="initLookAndFeelUrl" id="getLookAndFeelMap">
    <portlet:param name="companyId" value="${themeDisplay.companyId}"/>
</portlet:resourceURL>


<script>
    angular.module('${ns}lookAndFeelAdministration', ['lookAndFeelServices'])
            .controller('selectLookAndFeelAdministrationController', ['$scope', '$http', 'lookAndFeelService', 'portletConfig', SelectLookAndFeelAdministrationController])
            .service('portletConfig', function () {
                return {
                    ns: '${ns}',
                    initLookAndFeelUrl: '${initLookAndFeelUrl}',
                    permissionTableUrl: '<portlet:resourceURL id="permissionTable"/>'
                }
            });
</script>


<%--<div ng-app="${ns}lookAndFeelAdministration" class="lfs-container">--%>
<%--<div ng-controller="controller">--%>
<%--<div id="${ns}error-message"></div>--%>

<%--<div class="row-fluid lfs-container">--%>
<%--<div class="navbar navbar-inner container-fluid">--%>
<%--<div class="row-fluid">--%>
<%--<div>--%>
<%--<p class="span6 navbar-text">Look and feel to show</p>--%>
<%--</div>--%>
<%--<div class="span2 offset4">--%>
<%--<button type="button" class="btn btn-primary navbar-btn" ng-click="listeners.applyMap()">Apply</button>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--<div class="look-and-feel-map" ng-cloak>--%>
<%--<div class="theme-row" ng-repeat="theme in models.lookAndFeels">--%>
<%--<div class="row-fluid">--%>
<%--<div class="span6">--%>
<%--<div class="row-fluid">--%>
<%--<div class="span1">--%>
<%--<input id="{{expressions.nsValue('theme'+theme.id)}}" type="checkbox" ng-model="theme.selected">--%>
<%--</div>--%>
<%--<div class="span7">--%>
<%--<label for="{{expressions.nsValue('theme'+theme.id)}}">{{theme.name}}</label>--%>
<%--</div>--%>
<%--&lt;%&ndash;<div class="span4" ng-if="!theme['colorSchemes'] || theme['colorSchemes'].length == 0">&ndash;%&gt;--%>
<%--&lt;%&ndash;<img ng-src="{{theme['screenShotPath']}}"/>&ndash;%&gt;--%>
<%--&lt;%&ndash;</div>&ndash;%&gt;--%>
<%--</div>--%>
<%--</div>--%>
<%--<div class="span6" ng-if="theme.hasColorSchemes()">--%>
<%--<div class="row-fluid" ng-repeat="cs in theme.colorSchemes">--%>
<%--<div class="span1">--%>
<%--<input id="{{expressions.nsValue('cs'+cs.id)}}" type="checkbox" ng-model="cs.selected">--%>
<%--</div>--%>
<%--<div class="span7">--%>
<%--<label for="{{expressions.nsValue('cs'+cs.id)}}">{{cs.name}}</label>--%>
<%--</div>--%>
<%--&lt;%&ndash;<div class="span4">&ndash;%&gt;--%>
<%--&lt;%&ndash;<img ng-src="{{cs['screenShotPath']}}"/>&ndash;%&gt;--%>
<%--&lt;%&ndash;</div>&ndash;%&gt;--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>

<div ng-app="${ns}lookAndFeelAdministration">

    <div class="lfs-container" ng-controller="selectLookAndFeelAdministrationController">
        <div>
            <div ng-if="models.message && models.status != 'waiting'" class="alert" ng-class="expressions.messageStyle()" ng-bind="models.message"></div>
            <div>
                <div class="row-fluid">
                    <div class="span5">
                        <div>
                            <label for="${ns}themes"><liferay-ui:message key="lfs-themes"/></label>
                            <select id="${ns}themes" class="lfb-select" ng-disabled="expressions.disableFormCondition()"
                                    ng-options="theme as theme.name for theme in models.lookAndFeels track by theme.id" ng-model="models.currentTheme"></select>
                        </div>
                        <div ng-if="models.currentTheme && models.currentTheme.hasColorSchemes()">
                            <label for="${ns}color-schemes"><liferay-ui:message key="lfs-color-schemes"/></label>
                            <select id="${ns}color-schemes" class="lfb-select" ng-disabled="expressions.disableFormCondition()"
                                    ng-options="cs as cs.name for cs in models.currentTheme.colorSchemes track by cs.id" ng-model="models.currentColorScheme"></select>
                        </div>
                        <div>
                            <img ng-src="{{expressions.screenShotPath()}}" class="lfs-screen-shot">
                        </div>
                    </div>
                    <div class="span7">
                        <div>
                            <table class="table table-bordered table-hover table-striped">
                                <thead class="table-columns">
                                    <tr>
                                        <th><liferay-ui:message key="lfs-role"/></th>
                                        <c:forEach items="${actions}" var="action">
                                            <th><liferay-ui:message key="${action}"/></th>
                                        </c:forEach>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr ng-repeat="p in models.permissionMap track by p.role">
                                        <td>{{p.role}}</td>
                                        <td ng-repeat="(key, value) in p.actions">
                                            <input type="checkbox" ng-model="value"/>
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