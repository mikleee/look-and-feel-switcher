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
        <uib-carousel active="0" interval="10000" template-url="${crouselTemplateUrl}">
            <uib-slide ng-repeat="slide in models.currentTheme.colorSchemes" index="$index" actual="slide">
                <img ng-src="{{slide.screenShotPath}}" style="height: 130px; margin:auto;">
            </uib-slide>
        </uib-carousel>

        <ts-global-message></ts-global-message>

        <div ng-if="state == 'waiting' || models.lookAndFeels.length > 0">
            <h3><span><liferay-ui:message key="ts-select-theme"/></span>
                <ts-spinner ng-show="isLocked()"></ts-spinner>
            </h3>
            <div class="ts-row" ng-if="models.lookAndFeels.length > 0">
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
            <div class="ts-row button-footer" ng-if="models.lookAndFeels.length > 0">
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
        angular.module('${module}', ['ts-lookAndFeelList']);
        angular.bootstrap(document.getElementById('${module}'), ['${module}']);
    })();
</script>

<style>

    .aui .ts-carousel .carousel-control {
        background: none;
        color: inherit;
        cursor: pointer;
    }

    .aui .ts-carousel .carousel-control:hover {
        color: inherit;
    }

    .aui .ts-carousel .carousel-indicators {
        position: static;
        z-index: 5;
        list-style: none;

        margin: auto;
    }

    .aui .ts-carousel .carousel-indicators li {
        border: 1px solid;
        cursor: pointer;
        display: inline-block;
        float: none;
    }

    .aui .ts-carousel .carousel-indicators li.active {
        background-color: #50a2f5;
    }

    .aui .ts-carousel .carousel-indicators li:hover {
        background-color: rgba(80, 162, 245, 0.53);
    }

    /*.aui .ts-container .carousel {*/
    /*position: relative;*/
    /*}*/

    /*.aui .ts-container .carousel-inner {*/
    /*position: relative;*/
    /*width: 100%;*/
    /*overflow: hidden;*/
    /*}*/

    /*.aui .ts-container .carousel-control {*/
    /*position: absolute;*/
    /*top: 0;*/
    /*bottom: 0;*/
    /*left: 0;*/
    /*font-size: 20px;*/
    /*color: inherit;*/
    /*text-align: center;*/
    /*text-shadow: 0 1px 2px rgba(0, 0, 0, .6);*/
    /*filter: alpha(opacity=50);*/
    /*opacity: .5;*/
    /*height: 100%;*/
    /*margin: 0;*/
    /*width: auto;*/
    /*padding: 0 1em;*/
    /*border: none;*/
    /*border-radius: 0;*/
    /*background: none;*/
    /*}*/

    /*.aui .ts-container .carousel-control.left {*/

    /*}*/

    /*.aui .ts-container .carousel-control.right {*/
    /*right: 0;*/
    /*left: auto;*/
    /*}*/

    /*.aui .ts-container .glyphicon-chevron-left:before {*/
    /*content: "<";*/
    /*}*/

    /*.aui .ts-container .glyphicon-chevron-right:before {*/
    /*content: ">";*/
    /*}*/

    /*.aui .ts-container .sr-only {*/
    /*position: absolute;*/
    /*width: 1px;*/
    /*height: 1px;*/
    /*padding: 0;*/
    /*margin: -1px;*/
    /*overflow: hidden;*/
    /*clip: rect(0, 0, 0, 0);*/
    /*border: 0;*/
    /*}*/

    /*.aui .ts-container .carousel-indicators {*/
    /*position: absolute;*/
    /*bottom: 10px;*/
    /*left: 50%;*/
    /*z-index: 15;*/
    /*width: 60%;*/
    /*padding-left: 0;*/
    /*margin-left: -30%;*/
    /*text-align: center;*/
    /*list-style: none;*/
    /*}*/

    /*.aui .ts-container .carousel-indicators li {*/
    /*display: inline-block;*/
    /*width: 10px;*/
    /*height: 10px;*/
    /*margin: 1px;*/
    /*text-indent: -999px;*/
    /*cursor: pointer;*/
    /*!*background-color: #000 \9;*!*/
    /*!*background-color: rgba(0, 0, 0, 0);*!*/
    /*border: 1px solid;*/
    /*border-color: inherit;*/
    /*border-radius: 10px;*/
    /*float: none;*/
    /*}*/

    /*.aui .ts-container .carousel-indicators .active {*/
    /*width: 12px;*/
    /*height: 12px;*/
    /*margin: 0;*/
    /*background-color: #fff;*/
    /*}*/

    /*.ts-carousel-content {*/
    /*position: relative;*/
    /*}*/

    /*.ts-carousel-indicators {*/
    /*position: absolute;*/
    /*bottom: 10px;*/
    /*left: 50%;*/
    /*z-index: 15;*/
    /*width: 60%;*/
    /*padding-left: 0;*/
    /*margin-left: -30%;*/
    /*text-align: center;*/
    /*list-style: none;*/
    /*}*/

    /*.ts-carousel-indicators li {*/
    /*display: inline-block;*/
    /*width: 10px;*/
    /*height: 10px;*/
    /*margin: 1px;*/
    /*text-indent: -999px;*/
    /*cursor: pointer;*/
    /*!*background-color: #000 \9;*!*/
    /*!*background-color: rgba(0, 0, 0, 0);*!*/
    /*border: 1px solid;*/
    /*border-color: inherit;*/
    /*border-radius: 10px;*/
    /*float: none;*/
    /*}*/

    /*.ts-carousel-indicators .active {*/
    /*width: 12px;*/
    /*height: 12px;*/
    /*margin: 0;*/
    /*background-color: #fff;*/
    /*}*/

    .ts-carousel {
        display: table;
        width: 100%;
        text-align: center;
        position: relative;
    }

    .ts-carousel-elements {
        display: table-row;
    }

    .ts-carousel-elements > .ts-carousel-control,
    .ts-carousel-elements > .ts-carousel-content {
        display: table-cell;
    }

    .ts-carousel-control {
        padding: 0 5px;
        /*-webkit-transition: .4s ease-in-out;*/
        /*-moz-transition: .4s ease-in-out;*/
        /*-o-transition: .4s ease-in-out;*/
        transition: background .4s ease-in-out;
    }

    .ts-carousel-control.ts-left {
        text-align: left;

    }

    .ts-carousel-control.ts-left:hover {
        background-image: linear-gradient(to right, rgba(0, 0, 0, .5) 0, rgba(0, 0, 0, .0001) 100%);
        border-bottom-left-radius: 10px;
        border-top-left-radius: 10px;
    }

    .ts-carousel-control.ts-right {
        text-align: right;
    }

    .ts-carousel-control.ts-right:hover {
        background-image: linear-gradient(to left, rgba(0, 0, 0, .5) 0, rgba(0, 0, 0, .0001) 100%);
        border-bottom-right-radius: 10px;
        border-top-right-radius: 10px;
    }

    .ts-carousel-control > .ts-carousel-control-label {
        vertical-align: middle;
        cursor: pointer;
        color: inherit;
    }

    .ts-carousel-slide {
        position: relative;
        width: 100%;
        overflow: hidden;
    }

    .ts-carousel-slide > .item {
        position: relative;
        display: none;
        -webkit-transition: .6s ease-in-out left;
        -o-transition: .6s ease-in-out left;
        transition: .6s ease-in-out left;
    }

    .ts-carousel-slide > .item.active {
        display: block;
        left: 0;
        -webkit-transform: translate3d(0, 0, 0);
        transform: translate3d(0, 0, 0)
    }

    .ts-carousel-slide,
    .ts-carousel-indicators {
        display: block;
        margin: auto;
    }

    .ts-sr-only {
        display: none;
    }

    .ts-carousel-indicators > .ts-carousel-indicator {
        display: inline-block;
        width: 10px;
        height: 10px;
        margin: 1px;
        text-indent: -999px;
        cursor: pointer;
        border: 1px solid;
        border-color: inherit;
        border-radius: 50%;
        -webkit-transition: .2s ease-in-out;
        -moz-transition: .2s ease-in-out;
        -o-transition: .2s ease-in-out;
        transition: .2s ease-in-out;
    }

    .ts-carousel-indicators > .ts-carousel-indicator.active {
        background-color: #50a2f5;
    }

    .ts-carousel-indicators > .ts-carousel-indicator:hover {
        background-color: rgba(80, 162, 245, 0.53);
    }
</style>


</style>