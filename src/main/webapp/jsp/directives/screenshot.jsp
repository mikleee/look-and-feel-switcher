<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<span>
    <span ng-show="scrState == 'loading' || scrState == 'success'">
        <img ng-src="{{src}}" class="ts-screen-shot"/>
    </span>
    <span ng-show="scrState == 'failed'" class="alert alert-warning">
        <liferay-ui:message key="ts-screenshot-is-not-available"/>
    </span>
</span>


