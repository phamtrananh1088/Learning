var CHUMON01 = {

    init: function () {
        let self = this;

        // 再検索判断
        //self.research = TorihikiUtils.initPageSession(sessionKey);

        // イベント設定
        this.setEvent();

        const canvas = document.getElementById("myCanvas");
        const ctx = canvas.getContext("2d");

        // Start a new Path
        ctx.beginPath();
        ctx.moveTo(0, 0);
        ctx.lineTo(300, 150);

        // Draw the Path
        ctx.stroke();
    },
    setEvent: function () {

        //let self = this;

        //// 戻るボタン
        //$('#btnReturn').process({
        //    showConfirm: false,
        //    submit: function () {
        //        location.replace(TorihikiUtils.getHistoryPage());
        //    }
        //});

        //$('#btnKakunin').process({
        //    showConfirm: false,
        //    submit: function () {
        //        var sChuNo = $('#CHU_NO').val();
        //        var iChuKai = $('#CHU_KAI').val();
        //        var iJotaiKbn = $('#JOTAI_KBN').val();
        //        var sChuYmd = $('#CHU_YMD').val();

        //        TorihikiUtils.saveCurrentData($('form')[0]);
        //        TorihikiUtils.pageRedirect({ action: 'chumon02', sChuNo: sChuNo, iChuKai: iChuKai, iJotaiKbn: iJotaiKbn, sChuYmd: sChuYmd });
        //    }
        //});
    }
}

$(document).ready(function () {
    CHUMON01.init();
});
