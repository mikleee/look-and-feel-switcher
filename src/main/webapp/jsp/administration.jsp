<%@ include file="/jsp/init.jspf" %>

<script src="<c:url value="/js/models.js"/>"></script>
<script src="<c:url value="/js/look-and-feel-service.js"/>"></script>
<script src="<c:url value="/js/administration.js"/>"></script>

<%--@elvariable id="themes" type="java.util.List<com.liferay.portal.model.Theme>"--%>
<%--@elvariable id="themeDisplay" type="com.liferay.portal.theme.ThemeDisplay"--%>
<%--@elvariable id="screenShotPath" type="java.lang.String"--%>
<%--@elvariable id="colorSchemes" type="java.util.List<com.liferay.portal.model.ColorScheme>"--%>

<portlet:defineObjects/>

<portlet:resourceURL var="initLookAndFeelUrl" id="getLookAndFeelMap">
    <portlet:param name="companyId" value="${themeDisplay.companyId}"/>
</portlet:resourceURL>


<script>
    angular.module('${ns}lookAndFeelAdministration', ['lookAndFeelServices'])
            .controller('controller', ['$scope', '$http', 'lookAndFeelService', 'portletConfig', lookAndFeelAdministrationController])
            .service('portletConfig', function () {
                return {
                    ns: '${ns}',
                    initLookAndFeelUrl: '${initLookAndFeelUrl}',
                    applyLookAndFeelMapUrl: '<portlet:resourceURL id="applyLookAndFeelsToShow"/>'
                }
            });
</script>


<div ng-app="${ns}lookAndFeelAdministration" class="lfs-container">
    <div ng-controller="controller">
        <div id="${ns}error-message"></div>

        <div class="row-fluid lfs-container">
            <div class="navbar navbar-inner container-fluid">
                <div class="row-fluid">
                    <div>
                        <p class="span6 navbar-text">Look and feel to show</p>
                    </div>
                    <div class="span2 offset4">
                        <button type="button" class="btn btn-primary navbar-btn" ng-click="listeners.applyMap()">Apply</button>
                    </div>
                </div>
            </div>
            <div class="look-and-feel-map" ng-cloak>
                <div class="theme-row" ng-repeat="theme in models.lookAndFeels">
                    <div class="row-fluid">
                        <div class="span6">
                            <div class="row-fluid">
                                <div class="span1">
                                    <input id="{{expressions.nsValue('theme'+theme.id)}}" type="checkbox" ng-model="theme.selected">
                                </div>
                                <div class="span7">
                                    <label for="{{expressions.nsValue('theme'+theme.id)}}">{{theme.name}}</label>
                                </div>
                                <%--<div class="span4" ng-if="!theme['colorSchemes'] || theme['colorSchemes'].length == 0">--%>
                                <%--<img ng-src="{{theme['screenShotPath']}}"/>--%>
                                <%--</div>--%>
                            </div>
                        </div>
                        <div class="span6" ng-if="theme.hasColorSchemes()">
                            <div class="row-fluid" ng-repeat="cs in theme.colorSchemes">
                                <div class="span1">
                                    <input id="{{expressions.nsValue('cs'+cs.id)}}" type="checkbox" ng-model="cs.selected">
                                </div>
                                <div class="span7">
                                    <label for="{{expressions.nsValue('cs'+cs.id)}}">{{cs.name}}</label>
                                </div>
                                <%--<div class="span4">--%>
                                <%--<img ng-src="{{cs['screenShotPath']}}"/>--%>
                                <%--</div>--%>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>