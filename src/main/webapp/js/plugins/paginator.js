(function () {
    angular.module('ts-pagination', [])
        .controller('paginatorController', ['$scope', PaginatorController])
        .service('paginatorServiceFactory', ['$http', PaginatorServiceFactory])
        .directive('tsPaginator', ['$interval', Paginator])

        .controller('sorterController', ['$scope', SorterController])
        .directive('sorter', [Sorter]);


    function PaginatorDelegate() {
        var pageSizes = [5, 10, 20, 75];

        this.prepareData = prepareData;
        this.isActual = isActual;
        this.getApplicablePageSizes = getApplicablePageSizes;
        this.getDefaultPageSize = getDefaultPageSize;


        function prepareData(d) {
            angular.forEach(d, function (v) {
                if (v['_pid'] == null) {
                    v['_pid'] = ThemesSwitcher.sequence.getUnique();
                }
            });
            return d;
        }

        function isActual(totalCount) {
            return isPageSizeApplicable(totalCount, pageSizes[0]);
        }

        function getApplicablePageSizes(totalCount) {
            var result = [];
            for (var i = 0; i < pageSizes.length; i++) {
                var pSize = pageSizes[i];
                if (isPageSizeApplicable(totalCount, pSize)) {
                    result.push(pSize);
                } else {
                    break;
                }
            }
            if (totalCount > result[result.length - 1]) {
                result.push(totalCount);
            }
            return result;
        }

        function getDefaultPageSize() {
            return pageSizes[0];
        }

        function isPageSizeApplicable(totalCount, pSize) {
            return totalCount / pSize > 1;
        }
    }


    /**
     * @constructor
     */
    function DynamicPaginationService(http) {
        const me = this, delegate = new PaginatorDelegate();


        var callback = {success: null, error: null, beforeRequest: null};
        var state = {totalCount: 0, pageContent: []};
        var ns = '';
        var requestUrl = null;

        this.pageSizes = [5, 10, 20, 50];
        this.pageSize = me.pageSizes[0];
        this.pageNo = 1;
        this.sortOptions = new SortOptions();

        this.initPaginator = initPaginator;
        this.getTotalCount = getTotalCount;
        this.isActual = isActual;
        this.getApplicablePageSizes = getApplicablePageSizes;
        this.getPageContent = getPageContent;
        this.isPagesLineActual = isPagesLineActual;
        this.setPageSize = setPageSize;
        this.getPageSize = getPageSize;
        this.setPageNo = setPageNo;
        this.sort = sort;
        this.onPageChange = onPageChange;


        function initPaginator(url, config) {
            ns = config.ns || '';
            requestUrl = url;
            callback = config;
            return sendRequest();
        }

        function getTotalCount() {
            return state.totalCount;
        }

        function isActual() {
            return delegate.isActual(getTotalCount());
        }

        function getApplicablePageSizes() {
            return delegate.getApplicablePageSizes(getTotalCount());
        }

        function getPageContent() {
            return state.pageContent;
        }

        function isPagesLineActual() {
            return getTotalCount() / me.pageSize >= 2;
        }

        function setPageSize(pSize) {
            me.pageNo = 1;
            me.pageSize = pSize;
            sendRequest();
        }

        function getPageSize() {
            return me.pageSize;
        }

        function setPageNo(pNo) {
            me.pageNo = pNo;
            sendRequest();
        }

        function sort(sortOptions) {
            me.sortOptions = sortOptions;
            sendRequest();
        }

        function onPageChange() {
            sendRequest();
        }

        function sendRequest() {
            if (callback.beforeRequest) {
                callback.beforeRequest();
            }

            var url = requestUrl + '&' + ns + 'dir=' + me.sortOptions.dir + '&' + ns + 'field=' + me.sortOptions.key + '&' + ns + 'page=' + me.pageNo + '&' + ns + 'size=' + me.pageSize;
            var promise = http.get(url);
            promise.then(function (response) {
                state = callback.success(response.data);
                state.pageContent = delegate.prepareData(state.pageContent);
            }, function (response) {
                if (callback.error) {
                    callback.error(response)
                }
            });
            return promise;
        }

    }

    function PaginatorServiceFactory(http) {
        this.dynamicPaginator = function () {
            return new DynamicPaginationService(http);
        }
    }

    function PaginatorController(scope) {
        scope.setPageSize = setPageSize;
        scope.getPageSizes = getPageSizes;
        scope.setPageNo = setPageNo;
        scope.getPageLine = getPageLine;

        scope.getPageSizeTitle = getPageSizeTitle;
        scope.getPageNoTitle = getPageNoTitle;
        scope.getResultsTitle = getResultsTitle;

        scope.getLastPageNo = getLastPageNo;


        function setPageSize(ps) {
            scope.paginator.setPageSize(ps);
        }

        function getPageSizes() {
            return scope.paginator.pageSizes;
        }

        function setPageNo(pNo) {
            if (getLastPageNo() >= pNo && pNo >= 1) {
                scope.paginator.setPageNo(pNo);
            }
        }

        function getPageSizeTitle() {
            return ThemesSwitcher.getMessage('ts-pagination-items-per-page', [scope.paginator.pageSize]);
        }

        function getPageNoTitle() {
            return ThemesSwitcher.getMessage('ts-pagination-page', [scope.paginator.pageNo, getPageCount()]);
        }

        function getResultsTitle() {
            if (getPageCount() > 1) {
                return ThemesSwitcher.getMessage('ts-pagination-results-full',
                    [
                        (scope.paginator.pageNo - 1) * scope.paginator.pageSize + 1,
                        (scope.paginator.pageNo - 1) * scope.paginator.pageSize + scope.paginator.getPageContent().length,
                        scope.paginator.getTotalCount()
                    ]);
            } else {
                return ThemesSwitcher.getMessage('ts-pagination-results-short', [scope.paginator.getPageContent().length]);
            }
        }

        function getPageLine() {
            var result = [];
            for (var i = 1; i <= getPageCount(); i++) {
                result.push(i);
            }
            return result;
        }

        function getLastPageNo() {
            var pageCount = getPageLine();
            return pageCount[pageCount.length - 1];
        }

        function getPageCount() {
            return Math.ceil(scope.paginator.getTotalCount() / scope.paginator.pageSize);
        }


    }

    function Paginator() {
        return {
            controller: 'paginatorController',
            restrict: 'E',
            scope: {
                paginator: '=paginatorService'
            },
            templateUrl: ThemesSwitcher.staticUrl.paginatorTemplate,
            link: link
        };

        function link(scope, elem, attr) {
            var ns = scope.$id;
            var container = attr['container'];

            // init
            var initChecker = setInterval(function () {
                if (isPaginatorReady()) {
                    clearInterval(initChecker);
                    init();
                }
            }, 0);

            function init() {
                renderGrid();
                window.onresize = renderGrid;
                Liferay.on('portletMoved', onPortletMoved);
                scope.$on('$destroy', onScopeDestroy);
            }

            function onPortletMoved() {
                var id = setInterval(function () {
                    if (isPaginatorReady()) {
                        clearInterval(id);
                        renderGrid();
                    }
                }, 0)
            }

            function onScopeDestroy() {
                if (initChecker) {
                    clearInterval(initChecker);
                }
            }

            function renderGrid() {
                const el = angular.element, elements = getElements();

                if (isPaginatorReady()) {
                    // paginator.style.width = container.offsetWidth + 'px';
                    if (el(elements.paginator).hasClass('short')) {
                        el(elements.paginator).removeClass('short');
                        renderGrid();
                    } else {
                        var pw = elements.pageConfig.offsetWidth, tw = elements.totalCount.offsetWidth, bw = elements.buttons.offsetWidth, cw = elements.container.offsetWidth;
                        if (pw + tw + bw > cw) {
                            el(elements.paginator).addClass('short');
                        }
                    }
                }
            }

            function isPaginatorReady() {
                var elements = getElements();
                return elements.pageConfig && elements.totalCount && elements.buttons && elements.paginator && elements.container && Liferay;
            }

            function getElements() {
                return {
                    pageConfig: document.getElementById('ts-pagination-page-config-' + ns),
                    totalCount: document.getElementById('ts-pagination-results-count-' + ns),
                    buttons: document.getElementById('ts-pagination-buttons-' + ns),
                    paginator: document.getElementById('ts-pagination-' + ns),
                    container: document.getElementById(container)
                }

            }
        }

    }

    function Sorter() {
        return {
            controller: 'sorterController',
            scope: {
                sortKey: '@field',
                paginator: '=paginatorService'
            },
            restrict: 'E',
            template: '<span class="sorter glyphicon" ng-click="sort()" ng-class="styleClass()"></span>'
        };
    }


    function SorterController(scope) {
        scope.sortOptions = new SortOptions(scope.sortKey);
        scope.isActive = isActive;
        scope.sort = sort;
        scope.styleClass = styleClass;


        function sort() {
            if (isActive()) {
                scope.sortOptions.toggle();
            } else {
                scope.sortOptions.dir = 'asc'
            }

            scope.paginator.sort(scope.sortOptions);
        }

        function isActive() {
            return scope.paginator && scope.paginator.sortOptions && scope.paginator.sortOptions.key == scope.sortOptions.key;
        }

        function styleClass() {
            if (isActive() && scope.sortOptions.dir == 'asc') {
                return 'glyphicon-sort-by-attributes active-sorter';
            } else if (isActive() && scope.sortOptions.dir == 'desc') {
                return 'glyphicon-sort-by-attributes-alt active-sorter'
            } else if (!isActive() || !scope.sortOptions.dir) {
                return 'glyphicon-sort'
            } else {
                return ''
            }
        }
    }

    function SortOptions(key) {
        const me = this;
        this.dir = null;
        this.key = key;

        this.toggle = function () {
            if (me.dir == 'asc') {
                me.dir = 'desc';
            } else if (me.dir == 'desc') {
                me.dir = 'asc';
            }
        }
    }


})();