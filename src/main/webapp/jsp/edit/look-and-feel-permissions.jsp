<%@ include file="../init.jspf" %>

<div ng-controller="lookAndFeelPermissionsController">

    <div class="row-fluid">

        <div class="span4" ng-controller="selectLookAndFeelPreferencesController">
            <div>
                <h3><liferay-ui:message key="lfs-select-theme"/></h3>
            </div>
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

        <div class="span8">
            <div class="lfs-locker" ng-if="expressions.disableCondition()"></div>
            <h3><liferay-ui:message key="lfs-define-permissions"/></h3>
            <table class="table table-bordered table-hover table-striped">
                <thead class="table-columns">
                    <tr>
                        <th><liferay-ui:message key="lfs-role"/></th>
                        <th class="lfs-action-label" ng-click="listeners.toggleAction(a.id)" ng-repeat="a in models.resourcePermissions.allowedActions track by a.id">
                            {{a.name}}
                        </th>
                    </tr>
                </thead>
                <tbody class="table-data">
                    <tr ng-repeat="p in models.resourcePermissions.permissions track by p.role.name" class="{{'lfr-role lfr-role-' + p.role.type}}">
                        <td class="first">{{p.role.name}}</td>
                        <td ng-repeat="a in p.actions track by a.id">
                            <input type="checkbox" ng-model="models.resourcePermissions.permissions[$parent.$index].actions[$index].permitted"/>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <div class="row-fliud">
        <button type="button" class="btn btn-primary" ng-click="listeners.submitPermissions()" ng-disabled="expressions.disableCondition()"><liferay-ui:message key="save"/></button>
        <button type="button" class="btn btn-default"><liferay-ui:message key="submit"/></button>
    </div>

</div>