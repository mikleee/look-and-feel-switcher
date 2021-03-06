<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<div id="ts-pagination-{{$id}}" class="ts-pagination">
    <div id="ts-pagination-page-config-{{$id}}" class="ts-page-config">
        <div class="ts-page-size">
            <div class="btn-group lfr-icon-menu">
                <div class="btn-group lfr-icon-menu current-page-menu" uib-dropdown>
                    <button class="btn" uib-dropdown-toggle>
                        <span class="lfr-icon-menu-text"><span ng-bind="getPageSizeTitle()"></span></span><i class="caret"></i>
                    </button>
                    <ul class="dropdown-menu" uib-dropdown-menu role="menu" aria-labelledby="single-button">
                        <li role="menuitem" ng-repeat="ps in getPageSizes()">
                            <a href="javascript:void(0);" class="taglib-icon focus" ng-click="setPageSize(ps)">
                                <span class="taglib-text-icon" ng-bind="ps"></span>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="ts-page-number" ng-show="isPageNoControlActual()">
            <div class="btn-group lfr-icon-menu current-page-menu" uib-dropdown>
                <button class="btn" uib-dropdown-toggle>
                    <span class="lfr-icon-menu-text"><span ng-bind="getPageNoTitle()"></span><i class="caret"></i></span>
                </button>
                <ul class="dropdown-menu" uib-dropdown-menu role="menu" aria-labelledby="single-button">
                    <li role="menuitem" ng-repeat="pno in getPageLine()">
                        <a href="javascript:void(0);" class="taglib-icon focus" ng-click="setPageNo(pno)"><span class="taglib-text-icon" ng-bind="pno"></span></a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <small id="ts-pagination-results-count-{{$id}}" class="ts-results-count" ng-bind="getResultsTitle()"></small>
    <div id="ts-pagination-buttons-{{$id}}" class="ts-pagination-buttons" ng-show="isPageNoControlActual()">
        <ul class="lfr-pagination-buttons pager">
            <li class="first" ng-class="{'disabled' : paginator.pageNo == 1}"><a href="javascript:void(0);" ng-click="setPageNo(1)"> &#8592; <liferay-ui:message key="first"/> </a></li>
            <li ng-class="{'disabled' : paginator.pageNo == 1}"><a href="javascript:void(0);" ng-click="setPageNo(paginator.pageNo - 1)"> <liferay-ui:message key="previous"/> </a></li>
            <li ng-class="{'disabled' : paginator.pageNo == getLastPageNo()}"><a href="javascript:void(0);" ng-click="setPageNo(paginator.pageNo + 1)"> <liferay-ui:message key="next"/> </a></li>
            <li class="last" ng-class="{'disabled' : paginator.pageNo == getLastPageNo()}"><a href="javascript:void(0);" ng-click="setPageNo(getLastPageNo())"> <liferay-ui:message key="last"/> &#8594; </a></li>
        </ul>
    </div>
</div>