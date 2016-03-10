<%@ include file="../init.jspf" %>

<div ng-controller="adminController">
    <div>
        <button class="btn btn-danger" ng-click="listener.removeAllBindings()" ng-disabled="expressions.disableFormCondition() || stat.isEmpty()">
            <liferay-ui:message key="ts-remove-all-bindings"/>
        </button>
        <span class="label">{{expressions.statMessage()}}</span>
    </div>
</div>