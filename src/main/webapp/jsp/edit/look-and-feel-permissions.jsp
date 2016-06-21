<div>

    <div class="ts-row">

        <div ng-controller="lookAndFeelListController">
            <div>
                <h3><liferay-ui:message key="ts-select-theme"/></h3>
            </div>
            <div>
                <label for="${ns}themes"><liferay-ui:message key="ts-themes"/></label>
                <select id="${ns}themes" class="ts-select" ng-disabled="isLocked()" ng-options="theme as theme.name for theme in models.lookAndFeels track by theme.id" ng-model="models.currentTheme"></select>
            </div>
            <div ng-if="models.currentTheme && models.currentTheme.hasColorSchemes()">
                <label for="${ns}color-schemes"><liferay-ui:message key="ts-color-schemes"/></label>
                <select id="${ns}color-schemes" class="ts-select" ng-disabled="isLocked()" ng-options="cs as cs.name for cs in models.currentTheme.colorSchemes track by cs.id" ng-model="models.currentColorScheme"></select>
            </div>
            <div>
                <ts-screenshot src="getScreenshotPath()" alt="<liferay-ui:message key="ts-screenshot-is-not-available"/>"></ts-screenshot>
            </div>
        </div>

        <div ng-controller="preferencesPermissionsController">
            <h3><liferay-ui:message key="ts-define-permissions"/></h3>
            <table id="${ns}permissions" class="table table-bordered table-hover table-striped role-permission-table">
                <thead class="table-columns">
                    <tr>
                        <th><liferay-ui:message key="ts-role"/></th>
                        <th ng-repeat="a in permissionService.resourcePermissions.allowedActions">
                            <input id="${ns}toggleActions" type="checkbox" ng-change="permissionService.toggleAction(a)" ng-model="a.allSelected"/>
                            <label for="${ns}toggleActions" ng-bind="a.name" class="ts-toggle-all-actions"></label>
                        </th>
                    </tr>
                </thead>
                <tbody class="table-data">
                    <tr ng-repeat="p in permissionService.resourcePermissions.permissions" class="{{'lfr-role lfr-role-' + p.role.type}}">
                        <td class="first"><span class="ts-tooltip" uib-popover="{{p.role.description}}" popover-trigger="mouseenter">{{p.role.name}}</span></td>
                        <td ng-repeat="a in permissionService.resourcePermissions.allowedActions">
                            <input type="checkbox" ng-model="permissionService.resourcePermissions.permissions[$parent.$index].actions[$index].permitted" ng-change="onActionPermissionChange(a)"<%--ng-disabled="expressions.disableCondition()"--%>/>
                        </td>
                    </tr>
                </tbody>
            </table>
            <ts-paginator container="${ns}permissions" paginator-service="permissionService.paginator"></ts-paginator>
        </div>

    </div>

    <div class="row-fluid button-footer">
        <button type="button" class="btn btn-primary" ng-click="listeners.submitPermissions()" ng-disabled="expressions.disableCondition()"><liferay-ui:message key="save"/></button>
        <button type="button" class="btn btn-default" ng-click="listeners.setDefaultPermissions()"><liferay-ui:message key="ts-set-default-permissions"/></button>
    </div>

</div>