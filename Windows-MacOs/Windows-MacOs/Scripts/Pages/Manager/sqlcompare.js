var sqlcompare01 = {
    // Declaration
    Data: class {
        #targetProxy = undefined;
        #data = {};

        constructor(data) {
            this.#targetProxy = new Proxy(this, {
                set: function (target, key, value) {
                    var changeEvent = new CustomEvent("datachange", { detail: { property: key, oldValue: target.#data[`#${key}`], newValue: value } });
                    // Dispatch the event.
                    target.#data[`#${key}`] = value;
                    document.dispatchEvent(changeEvent);
                    return true;
                }
            });
            for (const property in data) {
                this.#data[`#${property}`] = data[property]
                this.#targetProxy[property] = data[property]
            }
        }

        get countRowSelected() {
            return this.#data["#countRowSelected"];
        };
        set countRowSelected(v) {
            this.#targetProxy.countRowSelected = v;
        };

        get objectsSelected() {
            return this.#data["#objectsSelected"];
        };
        set objectsSelected(v) {
            this.#targetProxy.objectsSelected = v;
        };
        get depAllDependencies() {
            return this.#data["#depAllDependencies"];
        };
        set depAllDependencies(v) {
            this.#targetProxy.depAllDependencies = v;
        };
    },
    _data: {
        countRowSelected: 0,
        objectsSelected: [],
        depAllDependencies: false
    }
    , data: undefined,

    init: function () {
        let self = this;
        //self.research = TorihikiUtils.initPageSession(sessionKey);
        this.setEvent();
        this.start();
    },
    setEvent: function () {
        var self = this;

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
        $("#listmeisai #datalist > tr input[data-name='check']").change(function () {
            var li = $("#listmeisai #datalist > tr input[data-name='check']:checked");
            var countRowSelected = li.length;
            self.data.countRowSelected = countRowSelected

            var objectsSelected = [];
            for (var i = 0; i < countRowSelected; i++) {
                var index = $(li[i]).data("index")
                var data = {
                    key: index,
                    Type: "U ",
                    SchemaName: $(`#SQLTables_${index}__SchemaName`).val(),
                    Name: $(`#SQLTables_${index}__TableName`).val()
                }
                objectsSelected.push(data);
            }
            self.data.objectsSelected = objectsSelected;
        });
        $("#listmeisai #datalist > tr").click(function () {
            $($(this).parents().children("tr")).removeClass("active");
            $(this).addClass("active");
            var index = $(this).data("index")
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
            $(".row.dep-row[data-index='1']").click();
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
        $(".row.dep-row").click(function () {
            var key = $(this).data("index");
            $($(this).parents().children("div.dep-row")).filter(function (i, k) {
                return $(k).data("index") > key;
            }).removeClass("active");
            $(this).addClass("active");
            $(".dep-right").removeClass("active");
            $(`.dep-right[data-index='${key}']`).addClass("active");
            switch (key) {
                case 1:
                    break;
                case 2:
                    TorihikiUtils.ajaxEx({
                        url: getSQLDependencies,
                        data: { SQLObjects: self.data.objectsSelected },
                        success: function (data) {
                            $('#SQLDependencies').html(data);
                        },
                        complete: function () {
                            TorihikiUtils.hideLoading();
                        }
                    });
                    break;
                case 3:
                    break;
                default:
                    break;
            }
        });
        $("#btnDepNext").click(function () {
            var key = $(".row.dep-row.active").last().data("index");
            $(`.row.dep-row[data-index='${key + 1}']`).click();
        });
        $("#btnDepBack").click(function () {
            var key = $(".row.dep-row.active").last().data("index");
            $(`.row.dep-row[data-index='${key - 1}']`).click();
        });
        $("#btnDepCancel").click(function () {
            $("#popCloseIcon").click();
        });

        $("#chkdepAllDependencies").click(function () {
            self.data.depAllDependencies = this.checked
        });
        $(document).bind("datachange", function (evt) {
            if (Array.isArray(evt.detail.newValue)) {
                var li = [];
                evt.detail.newValue.map((v) => {
                    li.push(`<div key="${v.key}">[${v.SchemaName}].[${v.Name}]</div>`)
                });
                $(`*[data-bind='data.${evt.detail.property}']`).html(li.join(""));
            } else if ($(`*[data-bind='data.${evt.detail.property}']`).prop('nodeName') === 'input') {
                if ($(`*[data-bind='data.${evt.detail.property}']`).prop('type') === 'checkbox') {
                    $(`*[data-bind='data.${evt.detail.property}']`).prop('checked', evt.detail.newValue);
                }
            } else {
                $(`*[data-bind='data.${evt.detail.property}']`).text(evt.detail.newValue);
            }

        });
    },
    start: function () {
        $("#btnDeploy").click();
        $(".row.dep-row[data-index='1']").click();
        this.data = new this.Data(this._data);
    }
}

$(document).ready(function () {
    sqlcompare01.init();

});
