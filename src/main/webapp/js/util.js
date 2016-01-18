var tsConstants = {
    getMessage: function (key) {
        return Liferay.Language.get(key);
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