<%@ include file="../init.jspf" %>

<div ng-controller="lookAndFeelPermissionsController">

    <div class="row-fluid">

        <div class="span4" ng-controller="selectLookAndFeelPreferencesController">
            <div>
                <h3><liferay-ui:message key="ts-select-theme"/></h3>
            </div>
            <div>
                <label for="${ns}themes"><liferay-ui:message key="ts-themes"/></label>
                <select id="${ns}themes" class="ts-select" ng-disabled="expressions.disableFormCondition()"
                        ng-options="theme as theme.name for theme in models.lookAndFeels track by theme.id" ng-model="models.currentTheme"
                        ng-change="listeners.onLookAndFeelChange()"></select>
            </div>
            <div ng-if="models.currentTheme && models.currentTheme.hasColorSchemes()">
                <label for="${ns}color-schemes"><liferay-ui:message key="ts-color-schemes"/></label>
                <select id="${ns}color-schemes" class="ts-select" ng-disabled="expressions.disableFormCondition()"
                        ng-options="cs as cs.name for cs in models.currentTheme.colorSchemes track by cs.id" ng-model="models.currentColorScheme"
                        ng-change="listeners.onLookAndFeelChange()"></select>
            </div>
            <div>
                <img ng-src="{{expressions.screenShotPath()}}" class="ts-screen-shot">
            </div>
        </div>

        <div class="span8">
            <h3><liferay-ui:message key="ts-define-permissions"/></h3>
            <table class="table table-bordered table-hover table-striped">
                <thead class="table-columns">
                    <tr>
                        <th><liferay-ui:message key="ts-role"/></th>
                        <th class="ts-action-label" ng-click="listeners.toggleAction(a.id)" ng-repeat="a in models.resourcePermissions.allowedActions track by a.id">
                            {{a.name}}
                        </th>
                    </tr>
                </thead>
                <tbody class="table-data">
                    <tr ng-repeat="p in models.resourcePermissions.permissions track by p.role.name" class="{{'lfr-role lfr-role-' + p.role.type}}">
                        <td class="first"><span class="ts-tooltip" title="{{p.role.description}}">{{p.role.name}}</span></td>
                        <td ng-repeat="a in p.actions track by a.id">
                            <input type="checkbox" ng-model="models.resourcePermissions.permissions[$parent.$index].actions[$index].permitted" ng-disabled="expressions.disableCondition()"/>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <div class="row-fliud">
        <button type="button" class="btn btn-primary" ng-click="listeners.submitPermissions()" ng-disabled="expressions.disableCondition()"><liferay-ui:message key="save"/></button>
        <button type="button" class="btn btn-default" ng-click="listeners.setDefaultPermissions()"><liferay-ui:message key="ts-set-default-permissions"/></button>
    </div>

</div>