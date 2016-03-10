var tsConstants = {
    getMessage: function (key, args) {
        var template = Liferay.Language.get(key);
        if (args && args.length > 0) {
            var result = template;
            for (var i = 0; i < args.length; i++) {
                result = result.replace('%s', args[i]);
            }
            return result;
        } else {
            return template;
        }
    },
    event: {
        FETCH_PERMISSIONS_REQUESTED: 'LFS_FETCH_PERMISSIONS_REQUESTED',
        SHOW_MESSAGE: 'LFS_SHOW_MESSAGE',
        HIDE_MESSAGE: 'LFS_HIDE_MESSAGE'
    },
    state: {
        ERROR: 'error',
        SUCCESS: 'success',
        WAITING: 'waiting',
        WARNING: 'warning'
    }
};