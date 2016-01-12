var Util = {
    getMessage: function getMessage(code) {
        return Liferay.Language.get(code)
    },
    events: {
        FETCH_PERMISSIONS_REQUESTED: 'FETCH_PERMISSIONS_REQUESTED'
    }
};