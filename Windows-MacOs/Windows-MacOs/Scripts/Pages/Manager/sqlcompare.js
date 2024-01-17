var sqlcompare01 = {

    init: function () {
        let self = this;

        // 再検索判断
        //self.research = TorihikiUtils.initPageSession(sessionKey);

        // イベント設定
        this.setEvent();
        this.start();
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
            var index = $(this).index()
            var data = {
                schemaName: $(`#SQLTables_${index}__SchemaName`).val(),
                tableName: $(`#SQLTables_${index}__TableName`).val()
            }
            TorihikiUtils.ajaxEx({
                url: getSQLScript,
                data: data,
                success: function (data) {
                    $('#sqlview').html(data);
                },
                complete: function () {
                    TorihikiUtils.hideLoading();
                }
            });
        });

        $("#btnResizer")
            .bind("mousedown", function (evt) {
                let res = mousedown(this, evt);
                return res;
            })
            .bind("mousemove", function (evt) {
                let res = mousemove(this, evt);
                return res;
            })
            .bind("mouseup", function (evt) {
                let res = mouseup(this, evt);
                return res;
            });

        function mousedown(t, evt) {
            console.log(evt.type, evt.clientX, evt.clientY, evt.pageX, evt.pageY, evt.offsetX, evt.offsetY);
            $(t).addClass("resizing")
            return true;
        }

        function mousemove(t, evt) {
            console.log(evt.type, evt.clientX, evt.clientY, evt.pageX, evt.pageY, evt.offsetX, evt.offsetY);
            return true;
        }

        function mouseup(t, evt) {
            console.log(evt.type, evt.clientX, evt.clientY, evt.pageX, evt.pageY, evt.offsetX, evt.offsetY);
            $(t).removeClass("resizing")
            return true;
        }

        $("#btnDeploy").click(function () {
            $('#DeployModal').modal({ backdrop: 'static', keyboard: false });
        });

        $("#popMaximizeDeploy").click(function () {
            $(".modal-deploy").toggleClass("fullscreen");
            $("#popMaximizeDeploy > i").toggleClass("fa-expand fa-compress");
            $(".modal-deploy").css("transform", 'translate(-50%, -50%)');
        });


        $(".modal-deploy > .title")
            .bind("drag", function (evt) {
                console.log(evt.type, evt.clientX, evt.clientY, evt.pageX, evt.pageY, evt.offsetX, evt.offsetY);
                if (evt.clientX !== 0 && evt.clientY !== 0) {
                    var x = evt.clientX - $(this).data("clientx");
                    var y = evt.clientY - $(this).data("clienty");
                    var ox = $(this).data("offsetx");
                    var oy = $(this).data("offsety");

                    $(".modal-deploy").css("transform", `translate(${ox + x}px, ${oy + y}px)`);
                }

                return true;
            })
            .bind("dragend", function (evt) {
                console.log(evt.type, evt.clientX, evt.clientY, evt.pageX, evt.pageY, evt.offsetX, evt.offsetY);
                var x = evt.clientX - $(this).data("clientx");
                var y = evt.clientY - $(this).data("clienty");
                var ox = $(this).data("offsetx");
                var oy = $(this).data("offsety");
                $(this).data("clientx", evt.clientX);
                $(this).data("clienty", evt.clientY);
                $(this).data("offsetx", ox + x);
                $(this).data("offsety", oy + y);

                $(".modal-deploy").css("transform", `translate(${ox + x}px, ${oy + y}px)`);
                return true;
            })
            .bind("dragstart", function (evt) {
                console.log(evt.type, evt.clientX, evt.clientY, evt.pageX, evt.pageY, evt.offsetX, evt.offsetY);
                $(this).data("clientx", evt.clientX);
                $(this).data("clienty", evt.clientY);
                var d = $(".modal-deploy").css("transform").replace('matrix(', '').replace(')', '').split(', ')
                $(this).data("offsetx", Number(d[4]));
                $(this).data("offsety", Number(d[5]));
                console.log($(this).data())
                var img = new Image();
                img.src = 'data:image/gif;base64,R0lGODlhAQABAIAAAAUEBAAAACwAAAAAAQABAAACAkQBADs=';
                evt.originalEvent.dataTransfer.setDragImage(img, 0, 0);
                return true;
            });

    },
    start: function () {
        $("#btnDeploy").click();
    }
}

$(document).ready(function () {
    sqlcompare01.init();

});
