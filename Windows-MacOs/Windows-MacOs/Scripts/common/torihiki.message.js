/*!
 * TorihikiUtils page
 * Copyright 2019 DHK  
 * History:
 * 2019/11/26 Created songxiaobo
 */
var TorihikiMsg = {
    version: '0.1.0',
    messageid: undefined,
    show: function (op, exoptions) {
        var self = this;
        var options = {
            id: undefined,
            message: undefined,
            args: undefined,
            callback: undefined,
            callbackdata: undefined,
            ok: undefined,
            cancel: undefined,
            exoptions: undefined,
            titleClass: undefined,
        };

        // メッセージIDのみ時
        if (typeof op === 'string') {
            options.id = op;
            options.args = $.makeArray(arguments).slice(1);
        } else {
            if (typeof op.id === 'undefined' && typeof op.message !== 'undefined') {
                op.id = op.message;
            }
            $.extend(options, op, true);
        }

        if (typeof options.callbackdata === 'function') {
            options.callbackdata = options.callbackdata();
        }

        // メッセージ区分取得
        var msgkbn = options.id.substring(2, 3).toUpperCase();
        var messageoptions = {};
        var exoptions = {};
        var formatmsg = self.getMessage(options);
        // メッセージ区分別処理
        switch (msgkbn) {
            case "C":
                messageoptions = {
                    message: formatmsg,
                    ok: options.ok,
                    cancel: options.cancel,
                    callbackdata: options.callbackdata,
                    titleClass: options.titleClass,
                };
                // メッセージ表示
                self.confirm(messageoptions);
                break;
            case "I":
                messageoptions = {
                    message: formatmsg,
                    callback: options.callback,
                    callbackdata: options.callbackdata,
                    titleClass: options.titleClass,
                };
                exoptions = options.exoptions;
                // メッセージ表示
                self.message(messageoptions, exoptions);
                break;
            case "W":
                messageoptions = {
                    message: formatmsg,
                    ok: options.ok,
                    cancel: options.cancel,
                    callbackdata: options.callbackdata,
                    titleClass: options.titleClass || 'tcc-bg-orange',
                    title: options.title || captions.title.Warning,
                    okButtonClass: options.okButtonClass || 'tcc-btn-orange',
                };
                exoptions = options.exoptions;
                // メッセージ表示
                self.confirm(messageoptions, exoptions);
                break;
            case "X":
            case "E":
            default:
                messageoptions = {
                    message: formatmsg,
                    callback: options.callback,
                    callbackdata: options.callbackdata,
                    titleClass: options.titleClass,
                };
                exoptions = options.exoptions;
                // メッセージ表示
                self.alert(messageoptions, exoptions);
                break;
        }
    },
    /**
     * エラーメッセージ
     * @param {any} options
     */
    alert: function (options, exoptions) {
        var self = this;

        //エラーメッセージエリアに表示する時
        if (typeof (options.data) != "undefined" && options.data != null && options.data != 'null' && options.data != '') {
            var data = TorihikiUtils.isObj(options.data) ? options.data : JSON.parse(options.data);
            if (data.length > 0) {
                $("[data-valmsg-summary=true]").addClass("validation-summary-errors").removeClass("validation-summary-valid");
                var ul = $('div.tcc-error-message .card-body ul');
                ////var ul = $('div.tcc-error-message-alert .card-body ul');
                //$(ul.parent()).removeClass('validation-summary-valid');
                //$(ul.parent()).addClass('validation-summary-errors');
                ul.empty();
                var form = $(TorihikiGlobal.requestVerificationToken).last().closest('form');
                var validater = form.validate();
                validater.errorList = [];
                $(data).each(function (i, t) {
                    var curmsg = '';
                    $(t).each(function (j, m) {
                        //if (curmsg != m.ErrorMessage) {
                        //var li = document.createElement("li");
                        //li.style.cursor = 'pointer';
                        //li.innerText = m.message;
                        //ul.append(li);
                        //$(li).data('target', $('[name="' + m.key + '"]').first());
                        var element = $('[name="' + m.key + '"]')[0];
                        if (element) {
                            if (typeof ($(element).attr(TorihikiGlobal.rownum)) != "undefined") {
                                m.message = ($(element).attr(TorihikiGlobal.rownum) * 1 + 1) + TorihikiGlobal.listErrorMsgSign + m.message
                            }
                        }
                        validater.errorList.push({
                            message: m.message,
                            element: $('[name="' + m.key + '"]')[0]
                        });
                        if (element) {
                            validater.settings.highlight($('[name="' + m.key + '"]')[0])
                        } else {
                            var li = document.createElement("li");
                            li.style.cursor = 'pointer';
                            li.innerText = m.message;
                            ul.append(li);
                        };
                        //$(li).off('click').on('click', function (e) {
                        //  TorihikiUtils.setFocus($(this).data('target'));
                        //})
                        //curmsg = m.ErrorMessage;
                        //}
                    });
                });
                TorihikiUtils.showErrmsgArea();
                TorihikiUtils.setFocus(ul.find('button'))
                return;
            }
        }
        // メッセージがない時
        if (TorihikiUtils.isNullOrEmpty(options.message)) {
            if (typeof options.callback !== 'undefined') {
                if (typeof options.callback === "function") {
                    options.callback(typeof options.callbackdata === 'function' ? options.callbackdata() : options.callbackdata, exoptions);
                } else {
                    self.execCallback(options.callback, options.callbackdata, exoptions);
                }
                return;
            }
        }

        if (typeof (options) == "string") {
            TorihikiMsg.alert({ message: options });
            return;
        }

        // メッセージIDよりメッセージを取得する。
        options.message = self.getMessage(options);

        var closefunc = function () {
            var url = options.url ? options.url : options.redirecturl;
            if (options.callback) {
                if (options.callback == TorihikiGlobal.PageBack) {
                    TorihikiUtils.gotoHistoryPage(options.url);
                } else {
                    if (typeof options.callback === "function") {
                        options.callback(typeof options.callbackdata === 'function' ? options.callbackdata() : options.callbackdata, exoptions);
                    } else {
                        self.execCallback(options.callback, options.callbackdata, exoptions);
                        //var func = new Function('data', options.callback);
                        //func(options.callbackdata);
                        //var func = eval(options.callback);
                        //func(options.callbackdata);
                    }
                    TorihikiUtils.hideLoading();
                }
                return;
            }

            if (options.callback == TorihikiGlobal.PageReload) {
                TorihikiUtils.showLoading();
                TorihikiUtils.reload();
                return;
            }

            if (typeof (url) != "undefined" && url != null) {
                TorihikiUtils.showLoading();
                location.replace(url);
                return;
            }
            TorihikiUtils.hideLoading();
        };

        // ローディング非表示
        TorihikiUtils.hideLoading();
        TorihikiUtils.hideLoadingImage();

        return $.alert({
            title: (options.title || 'エラー') + '<span style="float:right">' + ServerNo_Message + '</span>',
            content: options.message,
            containerClass: 'tcc-alert-area',
            titleClass: options.titleClass || 'tcc-bg-red',
            backgroundDismiss: true,
            scrollToPreviousElement: false,
            columnClass: options.columnClass || 'tcc-message-box-container',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: options.buttonClass || 'tcc-btn-red',
                    keys:['']
                    //action: closefunc
                }
            },
            onClose: closefunc || TorihikiUtils.hideLoading,
            onContentReady: function () {
                self.setDefaultFocus(this);
            }
        });
    },
    /**
     * 表示メッセージ
     * @param {any} options
     */
    message: function (options, exoptions) {
        var self = this;
        if (typeof (options) == "string") {
            TorihikiMsg.message({ message: options });
            return;
        }
        // 警告メッセージ場合
        if (options.warning) {
            TorihikiMsg.warning(options);
            return;
        }
        // メッセージがない時
        if (typeof options.message === 'undefined' || options.message == "" || options.message == null) {
            if (typeof options.callback !== 'undefined') {
                if (typeof options.callback === "function") {
                    options.callback(typeof options.callbackdata === 'function' ? options.callbackdata() : options.callbackdata, exoptions);
                } else {
                    self.execCallback(options.callback, options.callbackdata, exoptions);
                }
                return;
            }
        }

        // メッセージIDよりメッセージを取得する。
        options.message = self.getMessage(options);

        var closefunc = function () {
            var url = options.url ? options.url : options.redirecturl;
            if (options.callback) {
                if (typeof options.callback === "function") {
                    options.callback(typeof options.callbackdata === 'function' ? options.callbackdata() : options.callbackdata, exoptions);
                } else {
                    self.execCallback(options.callback, options.callbackdata, exoptions);
                    //var func = new Function('data', options.callback);
                    //func(options.callbackdata);
                    //var func = eval(options.callback);
                    //func(options.callbackdata);
                }
                return;
            }
            if (typeof (url) != "undefined" && url != null) {
                TorihikiUtils.showLoading();
                location.replace(url);
                return;
            }
            TorihikiUtils.hideLoading();
        };

        return $.confirm({
            title: options.title || 'インフォメーション',
            content: options.message,
            backgroundDismiss: true,
            titleClass: options.titleClass || 'tcc-bg-green',
            columnClass: options.columnClass || 'tcc-message-box-container',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: options.buttonClass || 'tcc-btn-green',
                    //action: closefunc
                }
            },
            onClose: closefunc,
            onContentReady: function () {
                self.setDefaultFocus(this);
            }
        });
    },
    /**
     * 警告メッセージ
     * @param {any} options
     */
    warning: function (options, exoptions) {
        var self = this;
        if (typeof (options) == "string") {
            TorihikiMsg.message({ message: options });
            return;
        }
        // メッセージがない時
        if (typeof options.message === 'undefined' || options.message == "" || options.message == null) {
            if (typeof options.callback !== 'undefined') {
                if (typeof options.callback === "function") {
                    options.callback(typeof options.callbackdata === 'function' ? options.callbackdata() : options.callbackdata, exoptions);
                } else {
                    self.execCallback(options.callback, options.callbackdata, exoptions);
                }
                return;
            }
        }
        // メッセージIDよりメッセージを取得する。
        options.message = self.getMessage(options);
        var closefunc = function () {
            var url = options.url ? options.url : options.redirecturl;
            if (options.callback) {
                if (typeof options.callback === "function") {
                    options.callback(typeof options.callbackdata === 'function' ? options.callbackdata() : options.callbackdata, exoptions);
                } else {
                    self.execCallback(options.callback, options.callbackdata, exoptions);
                    //var func = new Function('data', options.callback);
                    //func(options.callbackdata);
                    //var func = eval(options.callback);
                    //func(options.callbackdata);
                }
                return;
            }
            if (typeof (url) != "undefined" && url != null) {
                TorihikiUtils.showLoading();
                location.replace(url);
                return;
            }
            TorihikiUtils.hideLoading();
        };
        return $.confirm({
            title: options.title || captions.title.Warning,
            content: options.message,
            backgroundDismiss: true,
            titleClass: options.titleClass || 'tcc-bg-orange',
            columnClass: options.columnClass || 'tcc-message-box-container',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: options.buttonClass || 'tcc-btn-orange',
                    //action: closefunc
                }
            },
            onClose: closefunc,
            onContentReady: function () {
                self.setDefaultFocus(this);
            }
        });
    },
    /**
     * 確認メッセージ(OK/Cancel)
     * @param {any} options
     */
    confirm: function (options) {
        var self = this;
        var defaults = {
            title: options.title || captions.title.ConfirmTitle,
            content: '',
            titleClass: options.titleClass || 'tcc-bg-cyan',
            columnClass: options.columnClass || 'tcc-message-box-container',
            buttons: {
                confirm: {
                    text: options.buttonOkText || captions.button.OKButton,
                    btnClass: options.okButtonClass || 'tcc-btn-cyan',
                    action: function () {
                        if (options.ok) {
                            if (typeof options.ok === "function") {
                                var result = options.ok(typeof options.callbackdata === 'function' ? options.callbackdata() : options.callbackdata);
                            } else {
                                self.execCallback(options.ok, options.callbackdata);
                                //var func = new Function('data', options.ok);
                                //func(options.callbackdata);
                                //var func = eval(options.ok);
                                //func(options.callbackdata);
                            };
                            return result;
                        }
                    },
                    keys: ['']
                },
                cancel: {
                    text: options.buttonCancelText || captions.button.CancelButton,
                    btnClass: options.cancelButtonClass || 'tcc-btn-gray',
                    action: function () {
                        if (options.cancel) {
                            if (typeof options.cancel === "function") {
                                var result = options.cancel(typeof options.callbackdata === 'function' ? options.callbackdata() : options.callbackdata);
                            } else {
                                self.execCallback(options.cancel, options.callbackdata);
                                //var func = new Function('data', options.cancel);
                                //func(options.callbackdata);
                                //var func = eval(options.cancel);
                                //func(options.callbackdata);
                            };
                            return result;
                        }
                    }
                },
            },
            onContentReady: function () {
                self.setDefaultFocus(this);
            }
        }
        defaults = $.extend(defaults, options);
        // 幅
        if (options.columnClass) {
            defaults.columnClass = options.columnClass;
        }

        // メッセージIDよりメッセージを取得する。
        options.message = self.getMessage(options);

        defaults.content = options.message;
        return $.confirm(defaults);
    },
    /**
     * 確認メッセージ(Yes/No)
     * @param {any} options
     */
    yesNoCancel: function (options) {
        var self = this;
        var defaults = {
            title: options.title || captions.title.ConfirmTitle,
            content: '',
            titleClass: options.titleClass || 'tcc-bg-cyan',
            columnClass: options.columnClass || 'tcc-message-box-container',
            buttons: {
                yes: {
                    text: captions.button.Yes,
                    btnClass: 'tcc-btn-cyan',
                    action: function () {
                        if (options.yes) {
                            if (typeof options.yes === "function") {
                                options.yes(typeof options.callbackdata === 'function' ? options.callbackdata() : options.callbackdata);
                            } else {
                                self.execCallback(options.callback, options.yes);
                                //var func = new Function('data', options.yes);
                                //func(options.callbackdata);
                                //var func = eval(options.yes);
                                //func(options.callbackdata);
                            }
                        }
                    },
                    keys: ['']
                },
                no: {
                    text: captions.button.No,
                    btnClass: 'tcc-btn-orange',
                    action: function () {
                        if (options.no) {
                            if (typeof options.no === "function") {
                                options.no(typeof options.callbackdata === 'function' ? options.callbackdata() : options.callbackdata);
                            } else {
                                self.execCallback(options.callback, options.no);
                                //var func = new Function('data', options.no);
                                //func(options.callbackdata);
                                //var func = eval(options.no);
                                //func(options.callbackdata);
                            }
                        }
                    }
                },
                cancel: {
                    text: captions.button.CancelButton,
                    btnClass: 'tcc-btn-gray',
                    action: function () {
                        if (options.cancel) {
                            if (typeof options.cancel === "function") {
                                options.cancel(typeof options.callbackdata === 'function' ? options.callbackdata() : options.callbackdata);
                            } else {
                                self.execCallback(options.callback, options.cancel);
                                //var func = new Function('data', options.cancel);
                                //func(options.callbackdata);
                                //var func = eval(options.cancel);
                                //func(options.callbackdata);
                            }
                        }
                        TorihikiUtils.hideLoading();
                    }
                }
            },
            onContentReady: function () {
                self.setDefaultFocus(this);
            }
        }
        defaults = $.extend(defaults, options);

        // メッセージIDよりメッセージを取得する。
        options.message = self.getMessage(options);

        defaults.content = options.message;
        return $.confirm(defaults);
    },
    /**
     * コールバック関数
     * @param {any} callback
     * @param {any} callbackdata
     */
    execCallback: function (callback, callbackdata, exoptions) {
        if (typeof callback === 'undefined' || callback == null) return;
        var callbacksplit = callback.split('.');
        var callbackfunc = window;
        $(callbacksplit).each(function (i, cb) {
            callbackfunc = callbackfunc[cb];
        })

        if (typeof callbackfunc !== 'undefined') {
            callbackfunc(callbackdata, exoptions);
        }
    },
    /**
     * メッセージIDよりメッセージを取得する。
     * @param {any} options
     */
    getMessage: function (options) {
        if (typeof options === 'undefined') return;
        var self = this;
        var formatmsg;
        // メッセージIDの場合、IDよりメッセージを取得する。
        if (typeof options === 'string') {
            var op = options;
            options = {};
            options.id = op;
            options.args = $.makeArray(arguments).slice(1);
        }
        if (typeof options.id !== 'undefined') {
            formatmsg = messages[options.id];
            if (typeof formatmsg !== 'undefined') {
                self.messageid = options.id;
            } else {
                formatmsg = options.id;
            }
        } else {
            formatmsg = messages[options.message];
            if (typeof formatmsg !== 'undefined') {
                self.messageid = options.message;
            } else {
                formatmsg = options.message;
            }
        }
        if (typeof options.args !== 'undefined') {
            formatmsg = TorihikiUtils.format(formatmsg, options.args);
        }

        if (TorihikiGlobal.isDebug && typeof self.messageid !== 'undefined' && self.messageid != '') {
            formatmsg = formatmsg + '(' + self.messageid + ')';
        }
        self.messageid = '';

        return formatmsg;
    },
    /**
     * メッセージラベル表示
     * @param {any} message
     * @param {any} time
     */
    showMessageLabel: function (message, time) {
        var self = this;
        var options = {
            message: undefined,
            time: 1000,
            container: undefined,
        };
        if (typeof message === 'string') {
            options.message = options.message || message;
            options.time = options.time || time;
        } else {
            $.extend(options, message, true);
        }

        options.labelClass = 'tcc-bg-green tcc-message-label';
        self.showLabel(options);
    },
    /**
     * エラーラベル表示
     * @param {any} message
     * @param {any} time
     */
    showAlertLabel: function (message, time) {
        var self = this;
        var options = {
            message: undefined,
            time: 1000,
            container: undefined,
        };
        if (typeof message === 'string') {
            options.message = options.message || message;
            options.time = options.time || time;
        } else {
            $.extend(options, message, true);
        }

        options.labelClass = 'tcc-bg-red tcc-alert-label';
        self.showLabel(options);
    },
    /**
     * ラベル表示
     * @param {any} message
     * @param {any} time
     */
    showLabel: function (message, time) {
        var options = {
            message: undefined,
            time: 1000,
            container: undefined,
            labelClass: undefined || 'tcc-bg-green tcc-message-label',
        };
        if (typeof message === 'string') {
            options.message = options.message || message;
            options.time = options.time || time;
        } else {
            $.extend(options, message, true);
        }

        var divAlert = $('<div id="myAlert" class="alert">' + options.message + '</div>');
        divAlert.addClass(options.labelClass);

        if (options.container) {
            options.container.append(divAlert);
            var toploc = options.container.offset().top + (options.container.height() / 2) - 20 - window.scrollY;
            var leftloc = options.container.offset().left + (options.container.width() / 2) - 100;
            divAlert.css('top', toploc + 'px');
            divAlert.css('left', leftloc + 'px');
        } else {
            divAlert.addClass('tcc-message-label-basic');
            $('body').append(divAlert);
        }

        divAlert.bind('closed.bs.alert', function () {
            divAlert.remove();
        })

        divAlert.alert();

        setTimeout(function () {
            divAlert.alert('close');
        }, options.time);
    },
    /**
     * デファクトボタン設定.
     * @param {any} $jConfrim
     */
    setDefaultFocus: function ($jConfrim) {
        var $btnc = $jConfrim.$btnc;
        var $content = $jConfrim.$content;

        $btnc.find('button').each(function (i) {
            $(this).attr('tabindex', 10000 + i);
        })

        // コンテスト内容に入力コントロールがあれば
        if ($content.find('input[tabindex]:not([tabindex=-1]),select[tabindex]:not([tabindex=-1]),textarea[tabindex]:not([tabindex=-1])').length > 0) {
            TorihikiUtils.focusFirstElement($content);
            //form.enableEnterToTab({ captureTabKey: false });
            $('.jconfirm').enableEnterToTab({ captureTabKey: true });
            return;
        }
        // ボタンエリアの最後ボタンをフォーカスする
        $btnc.find('button').last().focus();

        //form.enableEnterToTab({ captureTabKey: false });
        $('.jconfirm').enableEnterToTab({ captureTabKey: true });

        $(document).on('keydown', '.jconfirm', function (e) {
        })
    }
};