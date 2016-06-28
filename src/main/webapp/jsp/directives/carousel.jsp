<%--<div ng-mouseenter="pause()" ng-mouseleave="play()" class="ts-carousel" ng-swipe-right="prev()" ng-swipe-left="next()">--%>
<%--<div class="ts-carousel-elements">--%>
<%--<div class="ts-carousel-control ts-left" ng-click="prev()" ng-class="{ disabled: isPrevDisabled() }" ng-show="slides.length > 1">--%>
<%--<h3 class="ts-carousel-control-label">&#171</h3>--%>
<%--</div>--%>
<%--<div class="ts-carousel-content">--%>
<%--<div class="ts-carousel-slide" ng-transclude></div>--%>
<%--<div class="ts-carousel-indicators" ng-show="slides.length > 1">--%>
<%--<div class="ts-carousel-indicator"--%>
<%--ng-repeat="slide in slides | orderBy:indexOfSlide track by $index" ng-class="{ active: isActive(slide) }" ng-click="select(slide)"--%>
<%--uib-popover="{{slide}}" popover-trigger="mouseenter" popover-placement="bottom">--%>
<%--<span class="ts-sr-only">slide {{ $index + 1 }} of {{ slides.length }}<span ng-if="isActive(slide)">, currently active</span></span>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--<div class="ts-carousel-control ts-right" ng-click="next()" ng-class="{ disabled: isNextDisabled() }" ng-show="slides.length > 1">--%>
<%--<h3 class="ts-carousel-control-label">&#187;</h3>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>


<%--<div ng-mouseenter="pause()" ng-mouseleave="play()" ng-swipe-right="prev()" ng-swipe-left="next()">--%>

<%--<div class="ts-carousel-control ts-left" ng-click="prev()" ng-class="{ disabled: isPrevDisabled() }" ng-show="slides.length > 1">--%>
<%--<h3 class="ts-carousel-control-label">&#171</h3>--%>
<%--</div>--%>
<%--<div class="ts-carousel-content">--%>
<%--<div class="ts-carousel-slide" ng-transclude></div>--%>
<%--<div class="ts-carousel-indicators" ng-show="slides.length > 1">--%>
<%--<div class="ts-carousel-indicator"--%>
<%--ng-repeat="slide in slides | orderBy:indexOfSlide track by $index" ng-class="{ active: isActive(slide) }" ng-click="select(slide)"--%>
<%--uib-popover="{{slide}}" popover-trigger="mouseenter" popover-placement="bottom">--%>
<%--<span class="ts-sr-only">slide {{ $index + 1 }} of {{ slides.length }}<span ng-if="isActive(slide)">, currently active</span></span>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--<div class="ts-carousel-control ts-right" ng-click="next()" ng-class="{ disabled: isNextDisabled() }" ng-show="slides.length > 1">--%>
<%--<h3 class="ts-carousel-control-label">&#187;</h3>--%>
<%--</div>--%>

<%--</div>--%>


<div ng-mouseenter="pause()" ng-mouseleave="play()" class="ts-carousel" ng-swipe-right="prev()" ng-swipe-left="next()">
    <div class="carousel-inner" ng-transclude></div>
    <div class="left carousel-control" ng-click="prev()" ng-class="{ disabled: isPrevDisabled() }" ng-show="slides.length > 1">&#171;</div>
    <div class="right carousel-control" ng-click="next()" ng-class="{ disabled: isNextDisabled() }" ng-show="slides.length > 1">&#187;</div>
    <ol class="carousel-indicators" ng-show="slides.length > 1">
        <li ng-repeat="slide in slides | orderBy:indexOfSlide track by $index" ng-class="{ active: isActive(slide) }" ng-click="select(slide)"
            uib-popover="{{slide.slide.actual.name}}" popover-trigger="mouseenter" popover-placement="bottom">
        </li>
    </ol>
</div>

