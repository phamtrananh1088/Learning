var viewer = {

    init: function () {
        let self = this;
        //self.research = TorihikiUtils.initPageSession(sessionKey);
        this.setEvent();
        this.start();
    },
    setEvent: function () {
        var self = this;


    },
    start: function () {
        const urlParams = new URLSearchParams(window.location.search);
        const getFile = urlParams.get('file');
        if (getFile) {
            TorihikiUtils.ajaxEx({
                method: 'get',
                url: getFile,
                data: null,
                success: function (data) {
                    $('#viewdata').html(data);
                },
                complete: function () {
                    TorihikiUtils.hideLoading();
                }
            });
        }
    },
}

$(document).ready(function () {
    viewer.init();

});
