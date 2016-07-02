<div class="ts-dropdown-container">
    <label for="themes-{{$id}}" ng-bind="label"></label>
    <div class="btn-group ts-dropdown" uib-dropdown>
        <button id="themes-{{$id}}" type="button" class="btn btn-default" uib-dropdown-toggle>
            <span ng-bind="getTitle(model)"></span> <span class="caret"></span>
        </button>
        <ul class="dropdown-menu" uib-dropdown-menu role="menu" aria-labelledby="single-button">
            <li role="menuitem" ng-repeat="item in items" ng-click="select(item)">
                <a ng-bind="getTitle(item)"></a>
            </li>
        </ul>
    </div>
</div>