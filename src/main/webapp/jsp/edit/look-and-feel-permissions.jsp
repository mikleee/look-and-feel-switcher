<%@ include file="../init.jspf" %>

<div ng-controller="lookAndFeelPermissionsController">

    <div class="ts-row">

        <div ng-controller="selectLookAndFeelPreferencesController">
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
                <span ng-show="screenshot.state == 'loading' || screenshot.state == 'success'"><img ng-src="{{expressions.screenShotPath()}}" class="ts-screen-shot" ng-model="screenshot"></span>
                <span ng-show="screenshot.state == 'failed'" class="alert alert-warning"><liferay-ui:message key="ts-screenshot-is-not-available"/></span>
            </div>
        </div>
        <div>
            <h3><liferay-ui:message key="ts-define-permissions"/></h3>
            <table class="table table-bordered table-hover table-striped role-permission-table">
                <thead class="table-columns">
                    <tr>
                        <th><liferay-ui:message key="ts-role"/></th>
                        <th class="ts-action-label" ng-click="listeners.toggleAction(a.id)" ng-repeat="a in models.resourcePermissions.allowedActions track by a.id">
                            {{a.name}}
                        </th>
                    </tr>
                </thead>
                <tbody class="table-data">
                    <tr ng-repeat="p in permissionsTable.getCurrentPage() track by p.role.name" class="{{'lfr-role lfr-role-' + p.role.type}}">
                        <td class="first"><span class="ts-tooltip" title="{{p.role.description}}">{{p.role.name}}</span></td>
                        <td ng-repeat="a in p.actions track by a.id">
                            <input type="checkbox" ng-model="permissionsTable.getCurrentPage()[$parent.$index].actions[$index].permitted" ng-disabled="expressions.disableCondition()"/>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div ng-show="permissionsTable.isApplicable()">
                <div class="btn-group">
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        {{permissionsTable.getPageSize()}} <liferay-ui:message key="items-per-page"/> <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li ng-repeat="p in permissionsTable.getPageSizes()"><a href="" ng-click="permissionsTable.setPageSize(p)">{{p}}</a></li>
                    </ul>
                </div>
                <div class="btn-group">
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <liferay-ui:message key="page"/> {{permissionsTable.getPage()}} <liferay-ui:message key="of"/> {{permissionsTable.getPages().length}} <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li ng-repeat="p in permissionsTable.getPages() track by $index"><a href="" ng-click="permissionsTable.setPage($index+1)">{{$index + 1}}</a></li>
                    </ul>
                </div>
            </div>
        </div>

    </div>

    <div class="row-fluid button-footer">
        <button type="button" class="btn btn-primary" ng-click="listeners.submitPermissions()" ng-disabled="expressions.disableCondition()"><liferay-ui:message key="save"/></button>
        <button type="button" class="btn btn-default" ng-click="listeners.setDefaultPermissions()"><liferay-ui:message key="ts-set-default-permissions"/></button>
    </div>

</div>