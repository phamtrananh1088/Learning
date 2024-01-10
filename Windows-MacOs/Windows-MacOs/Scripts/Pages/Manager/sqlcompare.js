var sqlcompare01 = {

    init: function () {
        let self = this;

        // 再検索判断
        //self.research = TorihikiUtils.initPageSession(sessionKey);

        // イベント設定
        this.setEvent();

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

        $("#checkbox1").click(function () {
            checkAll(this);
        });

        function checkAll(self) {
            const lstTR = $("#listmeisai #datalist > tr")
            for (var tr of lstTR) {
                var index = $(tr).index()
                if (!$(`#SQLTables_${index}__Selected`).prop('disabled')) {
                    $(`#SQLTables_${index}__Selected`).prop('checked', self.checked);
                    $(`#SQLTables_${index}__Selected`).trigger("change");
                }
            }
        }

        $("#listmeisai #datalist > tr").click(function () {
            $($(this).parents().children("tr")).removeClass("active");
            $(this).addClass("active");
            var data = {
                schemaName: $($(this).children('td:nth-child(2)')).text(),
                tableName: $($(this).children('td:nth-child(3)')).text()
            }
            TorihikiUtils.ajaxEx({
                url: GetSQLScript,
                data: data,
                success: function (data) {
                    $('#sqlview').html(data);
                },
                complete: function () {
                    TorihikiUtils.hideLoading();
                }
            });
        });
    }
}

$(document).ready(function () {
    sqlcompare01.init();

});
