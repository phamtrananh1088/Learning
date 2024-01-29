/*!
 * All Init
 * Copyright 2019 DHK  
 * History:
 * 2019/11/26 Created songxiaobo
 */

var StaticConst = {
    FLG_FALSE: '0',
    FLG_TRUE: '1',
}

var StaticVars = {
}

// 端数処理方法
var Fraction = {
    /** 数値の近似値 */
    System: 0,
    /** 切り上げ */
    Ceil: 1,
    /** 切り捨て */
    Floor: 2,
    /** 四捨五入 */
    Round: 3,
};

// 決済状況区分
var EnuKessaiJokyoKbn = {
    MiSinsei: '0',    // 未申請
    Sinseichu: '1',    // 申請中
    Sashimodoshi: '3',    // 差戻し
    Hinin: '4',    // 否認
    JokenShonin: '8',    // 条件付承認
    Shonin: '9'     // 承認
}

// プリンター種別
var EnuPrinter = {
    Dempyo: '1',    // 伝票用
    Other: '9',     // その他帳票用
}

var TorihikiGlobal = {
    version: '0.1.0',
    requestVerificationToken: 'input[name="__RequestVerificationToken"]',
    errorClass: "input-validation-error",
    validClass: "input-validation-valid",
    dataBumon: "#__DataBumon",
    workId: "#__WorkId",
    rownum: "rownum",
    listErrorMsgSign: "行目の",
    pageSize: 10,
    pageIndex: 1,
    isDebug: false,
    PageBack: '_Back',
    PageReload: '_Reload',
    PageSessionKey : 't',
    PageSessionIdKey: 'SessionId',
    scrollbarWidth: 17,
    SystemKingakuHasuKbn: Fraction.Round,
    SystemShohizeiHasuKbn: Fraction.Round,
    PageSourceKey: 'from',
    WijmoToolbarSearchWaiting: 300,
    WijmoRowDefaultHeight: 30,
    PageShowCommentKey: '_ShowComment',
    NoticeRefreshIntervalTime: 120000,
    NoticeSearchWaiting: 300,
    DempyoPrinterLocalKey: 'torihiki-dempyo-printer',
    DempyoPrinter: undefined,
    DempyoPrinterTray: undefined,
    DempyoPrinterModel: undefined,
    OtherPrinterLocalKey: 'torihiki-other-printer',
    OtherPrinter: undefined,
    OtherPrinterTray: undefined,
    OtherPrinterModel: undefined,
};

// Jquery から　コピーする
var rsubmitterTypes = /^(?:submit|button|image|reset|file)$/i;
var rsubmittable = /^(?:input|select|textarea|keygen)/i;
var rcheckableType = (/^(?:checkbox|radio)$/i);

var TorihikiUtils = {
    version: '0.1.0',
    browser: {
        /* ブラウザ情報 */
        isIE: false,
        IEver: 0,
        isIE11: false, // IE11以上(IE10までとは別のブラウザとして処理)
        isFirefox: false,
        isOpera: false,
        isSafari: false,
        isChrome: false,
        isMobile: false,
        isEdge: false,
    },
    pageInitKey: undefined,
    /** 初期化 */
    init: function () {
        // 初回
        var ua = navigator.userAgent.toLowerCase();
        this.browser.isIE = (ua.indexOf("msie") >= 0);
        if (this.browser.isIE) {
            var n = ua.indexOf("msie");
            var m = ua.indexOf(";", n);
            this.browse.IEver = Number(ua.substring(n + 5, m));
        }
        if (!this.browser.isIE) this.browser.isIE11 = (ua.indexOf("trident") >= 0); // IE11 以上の場合 this.browser.isIE は false で処理
        this.browser.isFirefox = (ua.indexOf("firefox") >= 0);
        this.browser.isOpera = (ua.indexOf("opera") >= 0); // "OPR" になっているようだ
        //	this.browser.isOpera = (ua.indexOf(" opr/") >= 0);
        this.browser.isSafari = (ua.indexOf("safari") >= 0) && (ua.indexOf("chrome") < 0);
        this.browser.isChrome = (ua.indexOf("chrome") >= 0);
        this.browser.isMobile = (ua.indexOf("mobile") >= 0);
        this.browser.isEdge = (ua.indexOf("edge") >= 0);

        return this;
    },
    /** 画面セッションデータ初期化 */
    initPageSession: function (sessionKey) {
        var research = false;

        // メニューから時、セッションをクリアする。
        var from = TorihikiUtils.getTargetUrlParam(location.href, TorihikiGlobal.PageSourceKey);
        switch (from) {
            case 'menu':
                // 設定した後で、保存した検索条件をクリアする。
                sessionStorage.removeItem(sessionKey);
                return false;
        }

        // 保存した検索情報を画面に設定する。
        if (typeof sessionKey != "undefined") {
            if (TorihikiUtils.getSessionData(sessionKey)) research = true;
            TorihikiUtils.setDataBySessionData(sessionKey, false);

            // 検索コントロール設定
            TorihikiUtils.resetSearchCtl(sessionKey);

            // 設定した後で、保存した検索条件をクリアする。
            sessionStorage.removeItem(sessionKey);
        }

        return research;
    },
    /**
     * ポップアップで再ログイン.
     * @param {any} data
     * @param {any} exoptions
     */
    reLogin: function (data, exoptions) {

        if (window.opener) {
            window.close();
            // ログインページに遷移する。
            window.opener.location.replace(SiteLoginPath);
        }
        else if (window.name == 'ifrm-0000') {
            // ログインページに遷移する。
            top.location.replace(SiteLoginPath);
        } else {
            window.close();
        }
        return;

        //// ログイン画面
        //TorihikiUtils.showEditPopup({
        //    url: SessionTimeOutPopUrl,
        //    params: {
        //        sDatabumon: $(TorihikiGlobal.dataBumon).val(),
        //        sWorkId: $(TorihikiGlobal.workId).val()
        //    },
        //    type: 'post',
        //    width: '450',
        //    height: '260',
        //    success: function (data) {
        //        if (typeof exoptions !== 'undefined' && typeof exoptions.callback === 'function') {
        //            exoptions.callback(data);
        //        }
        //    }
        //});
    },
    /**
     * セッションステータスチェック.
     * @param {any} resolve
     * @param {any} reject
     */
    checkSessionState: function (resolve, reject) {

        var self = this;

        // 重複ログインチェック
        if (!self.checkMultiLoginStatus(true)) {
            return false;
        }

        resolve(true);

        //// ローディング表示
        //TorihikiUtils.showLoading();
        //// セッションチェック
        //TorihikiUtils.ajaxEx({
        //    url: TorihikiUtils.addPageSessionId(SessionTimeOutCheckUrl),
        //    keepLoading: true,
        //    success: function (data) {
        //        resolve(true);
        //    },
        //    failed: function (data) {
        //        self.reLogin(
        //            null,
        //            { callback: function () { resolve(true); } }
        //        );
        //    }
        //});
    },
    /**
     * 重複ログインチェック.
     * @param {any} isAjax
     */
    checkMultiLoginStatus: function (isAjax) {
        var self = this;

        var sessionUser = TorihikiUtils.getSessionDataOfJson(TorihikiUtils.gLocalKey);
        var localUser = TorihikiUtils.getLocalDataOfJson(TorihikiUtils.gLocalKey);
        // セッションユーザが設定されない場合、ローカルユーザでセッションユーザを設定する。
        if (sessionUser == null) {
            TorihikiUtils.saveSessionData(TorihikiUtils.gLocalKey, localUser);
            return true;
        }
        // セッションユーザとローカルユーザが同じではない場合、エラーを表示する。
        if (localUser.workid != sessionUser.workid) {

            // トップページ
            if (typeof homePageUrl === 'undefined') {
                return true;
            }
            TorihikiMsg.show({
                id: 'SameUserLogined',
                callback: function () {
                    self.reLogin(
                        null,
                        { callback: function () { resolve(true); } }
                    );
                }
            });
            return false;
        }

        return true;
    },
    /**
     * コンソールログ出力
     * @param {any} data
     */
    log: function (data) {
        if (TorihikiGlobal.isDebug) {
            console.log(data);
        }
    },
    // =================================================================
    // =====================ボタン処理===================================
    // =================================================================
    /**
     * ボタンアイコン設定
     * @param {any} コンテナ
     */
    setButtonIcon: function (container) {

        var content;
        if (container) {
            content = $(container);
        } else {
            content = $(document);
        }

        ////　お客様の要望で、画面フッター部のボタンに付けるアイコンを消します　2021/10/15　RENT-445
        //if (content.find('.tcc-footer').length > 0) {
        //    return;
        //}

        // データ追加
        var pgtitleicon = '<i class="fas fa-list-alt"></i>';
        var printicon = '<i class="fas fa-print"></i>';
        var searchicon = '<i class="fas fa-search"></i>';
        var clearicon = '<i class="fas fa-trash-alt"></i>';
        var createicon = '<i class="fas fa-file-medical"></i>';
        var showicon = '<i class="fas fa-align-justify"></i>';
        var editicon = '<i class="fas fa-pen-nib"></i>';
        var updateicon = '<i class="fas fa-edit"></i>';
        var pasteicon = '<i class="fas fa-clone"></i>';
        var returnicon = '<i class="fas fa-undo"></i>';
        var closeicon = '<i class="fas fa-times-circle"></i>';
        var deleteicon = '<i class="fas fa-trash-alt"></i>';
        var reseticon = '<i class="fas fa-broom"></i>';
        var deliveryicon = '<i class="fas fa-truck-moving"></i>';
        var shiharaiChouseiReg = '<i class="fas fa-calculator"></i>';
        var shimegoteiseiRef = '<i class="fas fa-clipboard"></i>';
        var meisaiList = '<i class="fas fa-list-ul"></i>';
        var furiWake = '<i class="fas fa-dolly-flatbed"></i>';
        var plus = '<i class="fas fa-plus"></i>';
        var minus = '<i class="fas fa-minus"></i>';
        var userCheck = '<i class="fas fa-user-check"></i>';

        var flowLastIcon = '<i class="fas fa-lastfm-square"></i>';
        var flow_approve_icon = '<i class="fas fa-check-circle"></i>';
        var flow_request_icon = '<i class="fas fa-file-alt"></i>';

        var exporticon = '<i class="fas fa-file-export"></i>';
        var fileicon = '<i class="fas fa-paperclip"></i>';
        var csvicon = '<i class="fas fa-file-csv"></i>';
        var excelicon = '<i class="fas fa-file-excel"></i>';
        var addicon = '<i class="fas fa-plus"></i>';
        var accepticon = '<i class="fas fa-check"></i>';
        var cancelicon = '<i class="fas fa-pause"></i>';
        var dismissicon = '<i class="fas fa-times"></i>';
        var addrzipicon = '<i class="fas tcc-zip"></i>';
        //var rowupicon = '<i class="fas fa-arrow-up"></i>';
        //var rowdownicon = '<i class="fas fa-arrow-down"></i>';
        var rowupicon = '<i class="fas fa-arrow-circle-up"></i>';
        var rowdownicon = '<i class="fas fa-arrow-circle-down"></i>';
        var previcon = '<i class="fas fa-caret-left"></i>';
        var nexticon = '<i class="fas fa-caret-right"></i>';
        var upload = '<i class="fas fa-upload"></i>';
        var processicon = '<i class="fas fa-arrow-right"></i>';
        var copyicon = '<i class="fas fa-clone"></i>';

        var popuptitleicon = '<i class="fa fa-th-list"></i>';

        // 
        var noIcons = function (button) {
            // 通常画面
            if ($(button).parent().parent().hasClass('tcc-footer')) {
                $(button).find("i").remove();
                return true;
            }
            if ($(button).parent().hasClass('tcc-search-btn-area-no-toolbar')) {
                $(button).find("i").remove();
                return true;
            }
            if ($(button).parent().hasClass('tcc-search-btn-area')) {
                $(button).find("i").remove();
                return true;
            }
            if ($(button).parent().hasClass('tcc-cell-btn')) {
                $(button).find("i").remove();
                return true;
            }
            // ポップアップ画面
            if ($(button).parent().parent().hasClass('tcc-popup-footer')) {
                $(button).find("i").remove();
                return true;
            }
            if ($(button).parent().hasClass('tcc-btn-search')) {
                $(button).find("i").remove();
                return true;
            }
            return false;
        }

        // ポップアップ画面のタイトル前にアイコンを追加
        content.find('.popup-title').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }

            $(this).prepend(popuptitleicon);
        });
        //// タイトル前にアイコンを追加
        //content.find('.page-title h1').each(function () {
        //    if ($(this).find("i").length > 0) {
        //        return;
        //    }

        //    $(this).prepend(pgtitleicon);
        //});
        //// タイトル前にアイコンを追加
        //content.find('.page-title h3').each(function () {
        //    if ($(this).find("i").length > 0) {
        //        return;
        //    }

        //    $(this).prepend(pgtitleicon);
        //});
        // 印刷ボタン前にアイコンを追加
        content.find('.tcc-icon-print').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }

            $(this).prepend(printicon);
        });
        // 検索ボタン前にアイコンを追加
        content.find('.tcc-icon-search').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }

            $(this).prepend(searchicon);
        });

        // クリアボタン前にアイコンを追加
        content.find('.tcc-icon-clear').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }

            $(this).prepend(clearicon);
        });

        // 新規ボタン前にアイコンを追加
        content.find('.tcc-icon-create').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }

            $(this).prepend(createicon);
        });

        // コピーボタン前にアイコンを追加
        content.find('.tcc-icon-paste').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }

            $(this).prepend(pasteicon);
        });

        // 編集ボタン前にアイコンを追加
        content.find('.tcc-icon-edit').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }

            $(this).prepend(editicon);
        });

        // 参照ボタン前にアイコンを追加
        content.find('.tcc-icon-show').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }

            $(this).prepend(showicon);
        });

        // 更新ボタン前にアイコンを追加
        content.find('.tcc-icon-update').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }

            $(this).prepend(updateicon);
        });

        // 削除ボタン前にアイコンを追加
        content.find('.tcc-icon-delete').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }

            $(this).prepend(deleteicon);
        });

        // 戻るボタン前にアイコンを追加
        content.find('.tcc-icon-return').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }

            $(this).prepend(returnicon);
        });

        // 閉じるボタン前にアイコンを追加
        content.find('.tcc-icon-close').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }

            $(this).prepend(closeicon);
        });

        // リセット前にアイコンを追加
        content.find('.tcc-icon-reset').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }

            $(this).prepend(reseticon);
        });

        $('.tcc-icon-meisaiList').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }

            $(this).prepend(meisaiList);
        });

        $('.tcc-icon-userCheck').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }

            $(this).prepend(userCheck);
        });

        $('.tcc-icon-plus').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }

            $(this).prepend(plus);
        });
        //
        $('.tcc-icon-minus').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }

            $(this).prepend(minus);
        });

        // 最終処分
        $('.tcc-icon-flow-last').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(flowLastIcon);
        });
        // 承認
        $('.tcc-icon-flow-approve').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(flow_approve_icon);
        });
        // 申請
        $('.tcc-icon-flow-request').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(flow_request_icon);
        });

        // エクスポート（出力）
        content.find('.tcc-icon-export').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(exporticon);
        });

        // ファイル
        content.find('.tcc-icon-file').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(fileicon);
        });

        // CSVファイル
        content.find('.tcc-icon-csv').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(csvicon);
        });
        // Excelファイル
        content.find('.tcc-icon-excel').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(excelicon);
        });
        // 追加
        content.find('.tcc-icon-add').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(addicon);
        });
        // 確定
        content.find('.tcc-icon-accept').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(accepticon);
        });
        // 取消
        content.find('.tcc-icon-cancel').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(cancelicon);
        });
        // 却下
        content.find('.tcc-icon-dismiss').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(dismissicon);
        });
        // 郵便番号〒
        content.find('.tcc-icon-addrzip').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(addrzipicon);
        });
        // 行移動↑
        content.find('.tcc-icon-row-up').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(rowupicon);
        });
        // 行移動↓
        content.find('.tcc-icon-row-down').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(rowdownicon);
        });
        // 前＜
        content.find('.tcc-icon-prev').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(previcon);
        });
        // 後＞
        content.find('.tcc-icon-next').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(nexticon);
        });

        content.find('.tcc-icon-upload').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(upload);
        });

        // →
        content.find('.tcc-icon-process').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(processicon);
        });
        // copy
        content.find('.tcc-icon-copy').each(function () {
            if (noIcons(this)) return;

            if ($(this).find("i").length > 0) {
                return;
            }
            $(this).prepend(copyicon);
        });
    },

    /**ボタン権限設定 */
    setButtonKengen: function () {
        // メニュー権限は未設定する時
        if (typeof curmenukengen == "undefined") {
            return;
        }

        if (curmenukengen.sansyouken == 0) {
            // 参照権必要ボタン
            $("[sansyouken]").remove();
        }
        if (curmenukengen.kousinken == 0) {
            // 更新権必要ボタン
            $("[kousinken]").remove();
        }
        //// 参照権必要ボタン
        //$("[sansyouken]").attr("disabled", curmenukengen.sansyouken == 1 ? "" : "disabled");
        //// 更新権必要ボタン
        //$("[kousinken]").attr("disabled", curmenukengen.kousinken == 1 ? "" : "disabled");
    },

    // =================================================================
    // =====================共通方法処理=================================
    // =================================================================
    validitem: undefined,
    /**
     * 項目リストの値をKeyValueに変換する。
     * @param {any} o - 項目リスト
     */
    convertArray: function (o) {
        var v = {};
        for (var i in o) {
            if (o[i].name != '__VIEWSTATE') {
                if (typeof (v[o[i].name]) == 'undefined')
                    v[o[i].name] = o[i].value;
                else
                    v[o[i].name] = o[i].value;
            }
        }
        return v;
    },
    /**
     * フォームデータチェック
     * @param {any} form
     * @param {any} options
     */
    validateForm: function (form, options) {
        var checkfunc = typeof options === "undefined" ? undefined : options.checkfunc;
        var args = typeof options === "undefined" ? undefined : options.args;

        // 先のエラーをクリアする
        $(".validation-summary-errors li").remove();

        TorihikiUtils.validitem = null;
        $(form).valid();
        var validater = $(form).validate();
        var result = validater.errorList.length == 0;
        if (checkfunc) {
            result = result && checkfunc(args);
        }
        if (!result) {
            TorihikiUtils.validitem = $(form).validate().errorList[0].element;
        }
        return result;
    },
    /**
    * データチェック
    * @param {any} form
    * @param {any} pageModelCheckFunc
    * @param {any} pageModelCheckFuncArgs
    * @param {any} pageCheckFunc
    * @param {any} pageCheckFuncArgs
    */
    validateMethod: function (form, pageModelCheckFunc, pageModelCheckFuncArgs, pageCheckFunc, pageCheckFuncArgs) {
        var validater = $(form).validate();
        var check_result = validater.errorList.length == 0;

        if (typeof pageCheckFuncArgs === 'function') pageCheckFuncArgs = pageCheckFuncArgs();

        return new Promise((resolve, reject) => {

            if (typeof pageModelCheckFunc === 'function') {
                if (typeof pageModelCheckFuncArgs === 'function') pageModelCheckFuncArgs = pageModelCheckFuncArgs();
                pageModelCheckFunc(resolve, pageModelCheckFuncArgs);
            } else {
                resolve(check_result);
            }
        }).then(data => {
            return new Promise((resolve, reject) => {

                if (!data) {
                    $('.tcc-message-area').fadeIn();
                    $('div.tcc-error-message').draggable();
                    reject(false);
                } else {
                    resolve(data);
                }
            });
        }).then(data => {
            return new Promise(resolve => {
                if (!check_result) {
                    resolve(check_result);
                    return check_result;
                }
                if (pageCheckFunc) {
                    return new Promise(resolve => {
                        pageCheckFunc(resolve, pageCheckFuncArgs);
                    }).then(result => {
                        if (!result) {
                            resolve(result);
                            return result;
                        } else {
                            resolve(check_result);
                            return check_result;
                        }
                    });
                } else {
                    resolve(check_result);
                    return check_result;
                }
            });
        })
    },
    /**
    * フォームデータチェック(非同期処理)
    * @param {any} form
    * @param {any} options
    */
    validateFormAsync: function (form, options) {
        var checkfunc = options.checkfunc;
        var args = options.args;
        if (typeof args === 'function') args = args();

        var form = $('form');
        var validater = $(form).validate();

        return new Promise(resolve => {
            // ローディング
            TorihikiUtils.showLoadingImage();
            resolve('begin');
        }).then(val => {
            $(form).valid();
            return 'form check excecute';
            //resolve('1');
        }).then(val => {
            return new Promise(resolve => {
                var interval = setInterval(function () {
                    if (validater.pendingRequest == 0) {
                        clearInterval(interval);
                    }
                    resolve('remote check finished');
                }, 100);
            });
        }).then(val => {
            return TorihikiUtils.validateMethod(form, checkfunc, args)
                .then(result => {
                    if (!result) {

                        // エラーコントロール
                        if ($(form).validate().errorList.length > 0 && typeof $(form).validate().errorList[0] !== 'undefined') {
                            TorihikiUtils.validitem = $(form).validate().errorList[0].element;
                        }

                        TorihikiUtils.showErrmsgArea();
                        TorihikiUtils.hideLoadingImage();
                        return false;
                    }
                    TorihikiUtils.hideLoadingImage();
                    // チェック成功、エラーメッセージ非表示
                    TorihikiUtils.hideErrmsgArea();

                    return new Promise(resolve => {
                        options.exec();
                    });
                })
        });
    },
    /**
     * エラーリストにエラーを追加する。
     * @param {any} element
     * @param {any} msg
     */
    addError: function (msg, element) {

        if (element != undefined) {
            var elementctl = $(element)[0];
            var validator = $.validator.defaults;

            if (validator.errorList == undefined) {
                validator.errorList = [];
                $("[data-valmsg-summary=true]").addClass("validation-summary-errors").removeClass("validation-summary-valid");
            }

            validator.errorList.push({
                message: msg,
                element: elementctl
            });

            var form = $(element).closest('form');
            $(form).validate().errorList.push({
                message: msg,
                element: elementctl
            });

            validator.highlight(elementctl);

        } else {
            var ul = $("[data-valmsg-summary=true]").addClass("validation-summary-errors").removeClass("validation-summary-valid").find('ul');
            //var ul = $('div.tcc-error-message .card-body ul');
            var li = document.createElement("li");
            li.style.cursor = 'pointer';
            li.innerText = m.message;
            ul.append(li);
        };
    },
    /**
     * エラーリストにエラーを追加する。
     * @param {any} element
     * @param {any} msg
     */
    removeError: function (msg, element) {

        if (element != undefined) {
            var _element = $(element)[0];
            var errlists = $(".validation-summary-errors li");
            errlists.filter(function (i, t) {
                if ($(this).data('target') == undefined) return;
                var targetelement = $(this).data('target');
                var newmessage = $(t).text();
                return msg == newmessage && targetelement == _element;
            }).each(function (i, t) {
                $(this).addClass('tcc-error-clean');
            })

            // 未クリアの項目を取得する。
            var unclears = errlists.filter(function (x, item) {
                if ($(this).data('target') == undefined) return;
                var target_element = $(item).data('target');
                return !$(item).hasClass('tcc-error-clean') && target_element == _element;
            })
            // 未クリアの項目がない時、エラーをクリアする。
            if (unclears.length == 0) {
                $(element).removeClass('input-validation-error').addClass('valid');
            }
        }
    },
    /**
     * エラーリストにエラーを追加する。
     * @param {any} element
     * @param {any} msg
     */
    addWijmoError: function (errors, element) {
        // エラーリストをクリアする
        this.removeWijmoError(element);

        var ul = $("[data-valmsg-summary=true]").addClass("validation-summary-errors").removeClass("validation-summary-valid").find('ul');
        var uncleanli = ul.find('li:not(.tcc-error-clean)').length;
        if ($('.tcc-message-area').is(':hidden') && uncleanli == 0) {
            ul.empty();
        }
        $(errors).each(function (i, error) {
            // エラーメッセージ追加
            TorihikiUtils.bindWijmoItemError(element, error);
        })
        if (ul.find('li').length == 0) {
            $("[data-valmsg-summary=true]").removeClass("validation-summary-errors").addClass("validation-summary-valid")
            $('.tcc-message-area').fadeOut();
        } else {
            $('.tcc-message-area').fadeIn();
            $('div.tcc-error-message').draggable();
        }
    },
    /**
     * エラーリストをクリアする。
     * @param {any} element
     */
    removeWijmoError: function (element) {
        var ul = $("[data-valmsg-summary=true]").find('ul');
        ul.find('li').filter(function () {
            return $(this).data('target-filter') == element;
        }).remove();

        if (ul.find('li').length == 0) {
            $("[data-valmsg-summary=true]").removeClass("validation-summary-errors").addClass("validation-summary-valid")
            //$('.tcc-message-area').fadeOut();
        }
    },
    /**
     * エラーリストにエラーを追加する。
     * @param {any} element
     * @param {any} msg
     */
    addWijmoItemError: function (element, item, binding, message) {

        var grid = wijmo.grid.FlexGrid.getControl(element);
        var ul = $("[data-valmsg-summary=true]").addClass("validation-summary-errors").removeClass("validation-summary-valid").find('ul');

        var sourceindex = grid.collectionView.sourceCollection.indexOf(item);
        var error = {};
        error.message = (sourceindex + 1) + "行目の" + message;
        error.row = sourceindex;
        error.binding = binding;

        var exitsed = ul.find('li').filter(function () {
            return $(this).data('target-filter') == element
                && $(this).data('wijmo-row') == error.row
                && $(this).data('wijmo-binding') == error.binding
                && $(this).text() == error.message;;
        });
        if (exitsed.length > 0) {
            exitsed.removeClass('tcc-error-clean');
            return message;
        }

        // エラーメッセージ追加
        TorihikiUtils.bindWijmoItemError(element, error);
        //var ul = $('div.tcc-error-message .card-body ul');

        return message;
    },
    /**
     * エラーメッセージ追加
     * @param {any} element
     */
    bindWijmoItemError: function (element, error) {
        var ul = $("[data-valmsg-summary=true]").find('ul');
        var li = document.createElement("li");
        li.style.cursor = 'pointer';
        li.innerText = error.message;
        ul.append(li);
        $(li).data('target', $(element)[0]);
        $(li).data('target-filter', element);
        //$(li).data('wijmo', grid);
        $(li).data('wijmo-row', error.row);
        $(li).data('wijmo-col', error.col);
        $(li).data('wijmo-binding', error.binding);

        $(li).click(function () {
            var grid = wijmo.grid.FlexGrid.getControl(element);
            var row = $(li).data('wijmo-row');
            var binding = $(li).data('wijmo-binding');

            // 全てセル情報を取得する
            var dataItem = s.collectionView.currentItem;
            if (typeof grid.cellList !== 'undefined') {
                console.log(grid.cellList);

                var cell = grid.cellList.filter(function (cells) {
                    return cells.dataIndex + '_' + cells.binding == row + '_' + binding;
                });

                if (cell.length != 0) {
                    var range = cell[0];
                    var cellrange = new wijmo.grid.CellRange(range.row, range.col, range.row2, range.col2);
                    grid.select(cellrange, true);
                }
            } else {
                grid.collectionView.moveCurrentTo(grid.collectionView.sourceCollection[row]);
            }
        })
    },
    /**
     * エラーリストをクリアする。
     * @param {any} element
     */
    removeWijmoItemError: function (element, item, binding) {
        var grid = wijmo.grid.FlexGrid.getControl(element);
        var ul = $("[data-valmsg-summary=true]").find('ul');
        var sourceindex = grid.collectionView.sourceCollection.indexOf(item);
        ul.find('li').filter(function () {
            return $(this).data('target-filter') == element
                && $(this).data('wijmo-row') == sourceindex
                && $(this).data('wijmo-binding') == binding;
        }).addClass('tcc-error-clean');

        return '';
    },
    /**データチェックをリセット */
    resetValid: function (container) {

        var content;
        if (container) {
            content = $(container);
        } else {
            content = $('form');
        }

        //content.data("unobtrusiveValidation", null);
        //content.data("validator", null);
        //$.validator.unobtrusive.parse(content);
        $.validator.unobtrusive.parseDynamicContent(content);

        //// jquery.validate.unobtrusive onReset
        //var $form = content,
        //    key = '__jquery_unobtrusive_validation_form_reset';
        //if ($form.data(key)) {
        //    return;
        //}
        //// Set a flag that indicates we're currently resetting the form.
        //$form.data(key, true);
        //try {
        //    $form.data("validator").resetForm();
        //} finally {
        //    $form.removeData(key);
        //}

        $(".validation-summary-errors ul")
            .empty();
        // データチェック連動リセット
        TorihikiUtils.resetValidLinkage();

    },
    /**データチェック連動リセット */
    resetValidLinkage: function (container) {
        var content;
        if (container) {
            content = $(container);
        } else {
            content = $(document);
        }

        // 条件付けて必須チェック処理
        var requiredif = [];
        content.find('[data-val-requiredif-dependentproperty]').each(function (i, t) {
            if ($(t).is('div')) return;
            var item = $(t).data('val-requiredif-dependentproperty');
            var dependvalue = $(t).data('val-requiredif-dependentvalue');
            var dependoperator = $(t).data('val-requiredif-operator');
            var item = foolproof.getName(t, item);
            var exiited = requiredif.filter(function (t, j) {
                return t.dependitem == item && t.dependvalue == dependvalue;
            });
            var deptend = {};
            if (exiited.length == 0) {
                deptend.dependitem = item;
                deptend.dependvalue = dependvalue;
                deptend.dependoperator = dependoperator;
                deptend.targetitem = [];
                deptend.targetitem.push(t);
                requiredif.push(deptend)
            } else {
                deptend = exiited[0];
                deptend.targetitem.push(t);
            }
        });

        $(requiredif).each(function (i, depend) {
            // 変更イベントハンドル
            var item = $('[name="' + depend.dependitem + '"]');
            item.data('valid-link-depend-requiredif', []);
        });

        $(requiredif).each(function (i, depend) {
            // 変更イベントハンドル
            var item = $('[name="' + depend.dependitem + '"]');
            var depends = item.data('valid-link-depend-requiredif');
            depends.push(depend);
            item.data('valid-link-depend-requiredif', depends);

            item.unbind("input propertychange change", TorihikiUtils.requiredIfChangeEvent).
                bind("input propertychange change", TorihikiUtils.requiredIfChangeEvent);

            // 変更処理
            TorihikiUtils.requiredIfChange(depend, false);
        });

        var requiredifNotempty = [];
        content.find('[data-val-requiredifnotempty-dependentproperty]').each(function (i, t) {
            if ($(t).is('div')) return;
            var item = $(t).data('val-requiredifnotempty-dependentproperty');
            var item = foolproof.getName(t, item);
            var exiited = requiredifNotempty.filter(function (t, j) {
                return t.dependitem == item;
            });
            var deptend = {};
            if (exiited.length == 0) {
                deptend.dependitem = item;
                deptend.targetitem = [];
                deptend.targetitem.push(t);
                requiredifNotempty.push(deptend)
            } else {
                deptend = exiited[0];
                deptend.targetitem.push(t);
            }
        });

        $(requiredifNotempty).each(function (i, depend) {
            // 変更イベントハンドル
            var item = $('[name="' + depend.dependitem + '"]');
            item.data('valid-link-depend-requiredifnotempty', []);
        });

        $(requiredifNotempty).each(function (i, depend) {
            // 変更イベントハンドル
            var item = $('[name="' + depend.dependitem + '"]');
            var depends = item.data('valid-link-depend-requiredifnotempty');
            depends.push(depend);
            item.data('valid-link-depend-requiredifnotempty', depends);

            item.unbind("input propertychange change", TorihikiUtils.requiredIfNotEmptyChangeEvent).
                bind("input propertychange change", TorihikiUtils.requiredIfNotEmptyChangeEvent);

            // 変更処理
            TorihikiUtils.requiredIfNotEmptyChange(depend, false);
        });

        var requiredofcondition = [];
        content.find('[data-val-requiredofcondition-validconds]').each(function (i, t) {
            if ($(t).is('div')) return;
            var item = $(t).data('val-requiredofcondition-dependentproperty');
            var validconds = $(t).data('val-requiredofcondition-validconds');

            // 条件があるとき
            if (validconds != null) {
                var conds = validconds.split(';');
                for (i = 0; i < conds.length; i++) {
                    var cond = conds[i];
                    if (cond.indexOf('=') >= 1) {
                        item = cond.split('=')[0];
                    } else if (cond.indexOf('<>') >= 1) {
                        item = cond.split('<>')[0];
                    }

                    var item = foolproof.getName(t, item);
                    var exiited = requiredofcondition.filter(function (t, j) {
                        return t.dependitem == item && t.validconds == validconds;
                    });
                    var deptend = {};
                    if (exiited.length == 0) {
                        deptend.dependitem = item;
                        deptend.validconds = validconds;
                        deptend.targetitem = [];
                        deptend.targetitem.push(t);
                        requiredofcondition.push(deptend)
                    } else {
                        deptend = exiited[0];
                        deptend.targetitem.push(t);
                    }
                }
            }

        });

        $(requiredofcondition).each(function (i, depend) {
            // 変更イベントハンドル
            var item = $('[name="' + depend.dependitem + '"]');
            item.data('valid-link-depend-requiredofcondition', []);
        });

        $(requiredofcondition).each(function (i, depend) {
            // 変更イベントハンドル
            var item = $('[name="' + depend.dependitem + '"]');
            var depends = item.data('valid-link-depend-requiredofcondition');
            depends.push(depend);
            item.data('valid-link-depend-requiredofcondition', depends);

            item.unbind("input propertychange change", TorihikiUtils.requiredOfConditionChangeEvent).
                bind("input propertychange change", TorihikiUtils.requiredOfConditionChangeEvent);

            // 初期処理
            TorihikiUtils.requiredOfConditionChange(depend, false);
        });

        // 判断処理
        var valis = [];
        content.find('[data-val-is-dependentproperty]').each(function (i, t) {
            var item = $(t).data('val-is-dependentproperty');
            var dependvalue = $(t).data('val-is-dependentvalue');
            var dependoperator = $(t).data('val-is-operator');
            var item = foolproof.getName(t, item);
            var exiited = valis.filter(function (t, j) {
                return t.dependitem == item && t.dependvalue == dependvalue;
            });
            var deptend = {};
            if (exiited.length == 0) {
                deptend.dependitem = item;
                deptend.dependvalue = dependvalue;
                deptend.dependoperator = dependoperator;
                deptend.targetitem = [];
                deptend.targetitem.push(t);
                valis.push(deptend)
            } else {
                deptend = exiited[0];
                deptend.targetitem.push(t);
            }
        });

        $(valis).each(function (i, depend) {
            // 変更イベントハンドル
            var item = $('[name="' + depend.dependitem + '"]');
            item.bind("input propertychange change", function (event) {
                $(depend.targetitem).valid();
            });
        });

        // Range
        var rangesourcename = [];
        content.find('[data-val-range-source]').each(function (i, t) {
            var source = $(t).data('val-range-source');
            switch (source) {
                case 'name':
                    var minname = $(t).data('val-range-min');
                    var maxname = $(t).data('val-range-max');
                    minname = foolproof.getName(t, minname);
                    maxname = foolproof.getName(t, maxname);

                    var exiited = rangesourcename.filter(function (t, j) {
                        return t.dependitem == minname;
                    });

                    var deptend = {};
                    if (exiited.length == 0) {
                        deptend.dependitem = minname;
                        deptend.targetitem = [];
                        deptend.targetitem.push(t);
                        rangesourcename.push(deptend)
                    } else {
                        deptend = exiited[0];
                        deptend.targetitem.push(t);
                    }

                    exiited = rangesourcename.filter(function (t, j) {
                        return t.dependitem == maxname;
                    });

                    deptend = {};
                    if (exiited.length == 0) {
                        deptend.dependitem = maxname;
                        deptend.targetitem = [];
                        deptend.targetitem.push(t);
                        rangesourcename.push(deptend)
                    } else {
                        deptend = exiited[0];
                        deptend.targetitem.push(t);
                    }
                    break;
            }
        });
        $(rangesourcename).each(function (i, depend) {
            // 変更イベントハンドル
            var item = $('[name="' + depend.dependitem + '"]');
            item.bind("input propertychange change", function (event) {
                $(depend.targetitem).valid();
            });
        });

        // NotAllow
        var notallowsourcename = [];
        content.find('[data-val-notallow-source]').each(function (i, t) {
            var source = $(t).data('val-notallow-source');
            switch (source) {
                case 'name':
                    var checkValue = $(t).data('val-notallow-value');
                    checkValue = foolproof.getName(t, checkValue);

                    var exiited = notallowsourcename.filter(function (t, j) {
                        return t.dependitem == checkValue;
                    });

                    var deptend = {};
                    if (exiited.length == 0) {
                        deptend.dependitem = checkValue;
                        deptend.targetitem = [];
                        deptend.targetitem.push(t);
                        notallowsourcename.push(deptend)
                    } else {
                        deptend = exiited[0];
                        deptend.targetitem.push(t);
                    }
                    break;
            }
        });
        $(notallowsourcename).each(function (i, depend) {
            // 変更イベントハンドル
            var item = $('[name="' + depend.dependitem + '"]');
            item.bind("input propertychange change", function (event) {
                $(depend.targetitem).valid();
            });
        });
    },
    /**
     * RequiredIf変更イベント
     * */
    requiredIfChangeEvent: function () {
        var depends = $(this).data('valid-link-depend-requiredif');
        var _this = this;
        depends.forEach(function (depend) {
            if (_this.name != depend.dependitem) return;
            TorihikiUtils.requiredIfChange(depend, true);
        })
    },
    /**
     * requiredIf変更処理
     * @param {any} depend
     * @param {any} docheck
     */
    requiredIfChange: function (depend, docheck) {
        var item = $('[name="' + depend.dependitem + '"]');
        if (item.length == 0) return;
        var value = item.val();
        switch (item[0].type) {
            case 'checkbox':
            case 'radio':
                var checked = $('[name="' + depend.dependitem + '"]:checked');
                value = checked.val();
                if (typeof value === 'undefined') {
                    value = "";
                }
                break;
        }
        var removeRequired = false;
        switch (depend.dependoperator) {
            case 'NotEqualTo':
                removeRequired = value == depend.dependvalue;
                break;
            default:
                removeRequired = value != depend.dependvalue;
                break;
        }
        // 値は依頼値と同じではない時、属性変更
        if (removeRequired) {
            $(depend.targetitem).each(function (j, t) {
                var attrRi = $(t).attr('data-val-requiredif');
                $(t).removeAttr('data-val-requiredif').attr('data-val-requiredif-1', attrRi);
                if ($(t).hasClass('tcc-input-combo')) {
                    $('#' + $(t).attr('id') + '_input').removeAttr('data-val-requiredif').attr('data-val-requiredif-1', attrRi);
                }
                if ($(t).hasClass('tcc-checkbox-list-result') || $(t).hasClass('tcc-radio-list')) {
                    $(t).parent().removeAttr('data-val-requiredif').attr('data-val-requiredif-1', attrRi);
                }
                if ($(t).hasClass('tcc-warekidate-source')) {
                    $(t).parent().find('.tcc-warekidate-combo input').removeClass('tcc-bg-required');
                    $(t).parent().find('.tcc-warekidate-parts input').removeClass('tcc-bg-required');
                }
                if (docheck) {
                    $(t).valid();
                }
            })
        } else {
            $(depend.targetitem).each(function (j, t) {
                var attrRi = $(t).attr('data-val-requiredif-1') || $(t).attr('data-val-requiredif');
                $(t).removeAttr('data-val-requiredif-1').attr('data-val-requiredif', attrRi);
                if ($(t).hasClass('tcc-input-combo')) {
                    $('#' + $(t).attr('id') + '_input').removeAttr('data-val-requiredif-1').attr('data-val-requiredif', attrRi);
                }
                if ($(t).hasClass('tcc-checkbox-list-result') || $(t).hasClass('tcc-radio-list')) {
                    $(t).parent().removeAttr('data-val-requiredif-1').attr('data-val-requiredif', attrRi);
                }
                if ($(t).hasClass('tcc-warekidate-source')) {
                    ;
                    $(t).parent().find('.tcc-warekidate-combo input').addClass('tcc-bg-required');
                    $(t).parent().find('.tcc-warekidate-parts input').addClass('tcc-bg-required');
                }
                if (!$(".tcc-message-area").is(":hidden")) {
                    if (docheck) {
                        $(t).valid();
                    }
                }
            })
        }
    },
    /**
     * RequiredIfNotEmpty変更イベント
     * */
    requiredIfNotEmptyChangeEvent: function () {
        var depends = $(this).data('valid-link-depend-requiredifnotempty');
        var _this = this;
        depends.forEach(function (depend) {
            if (_this.name != depend.dependitem) return;
            TorihikiUtils.requiredIfNotEmptyChange(depend, true);
        })
    },
    /**
     * requiredIfNotEmpty変更処理
     * @param {any} depend
     * @param {any} docheck
     */
    requiredIfNotEmptyChange: function (depend, docheck) {
        var item = $('[name="' + depend.dependitem + '"]');
        if (item.length == 0) return;
        var value = item.val();
        switch (item[0].type) {
            case 'checkbox':
            case 'radio':
                var checked = $('[name="' + depend.dependitem + '"]:checked');
                value = checked.val();
                break;
        }
        var removeRequired = (value == "");
        // 値は依頼値と同じではない時、属性変更
        if (removeRequired) {
            $(depend.targetitem).each(function (j, t) {
                var attrRi = $(t).attr('data-val-requiredifnotempty');
                $(t).removeAttr('data-val-requiredifnotempty').attr('data-val-requiredifnotempty-1', attrRi);
                if ($(t).hasClass('tcc-input-combo')) {
                    $('#' + $(t).attr('id') + '_input').removeAttr('data-val-requiredifnotempty').attr('data-val-requiredifnotempty-1', attrRi);
                }
                if ($(t).hasClass('tcc-checkbox-list-result') || $(t).hasClass('tcc-radio-list')) {
                    $(t).parent().removeAttr('data-val-requiredifnotempty').attr('data-val-requiredifnotempty-1', attrRi);
                }
                if ($(t).hasClass('tcc-warekidate-source')) {
                    $(t).parent().find('.tcc-warekidate-combo input').removeClass('tcc-bg-required');
                    $(t).parent().find('.tcc-warekidate-parts input').removeClass('tcc-bg-required');
                }
                if (docheck) {
                    $(t).valid();
                }
            })
        } else {
            $(depend.targetitem).each(function (j, t) {
                var attrRi = $(t).attr('data-val-requiredifnotempty-1') || $(t).attr('data-val-requiredifnotempty');
                $(t).removeAttr('data-val-requiredifnotempty-1').attr('data-val-requiredifnotempty', attrRi);
                if ($(t).hasClass('tcc-input-combo')) {
                    $('#' + $(t).attr('id') + '_input').removeAttr('data-val-requiredifnotempty-1').attr('data-val-requiredifnotempty', attrRi);
                }
                if ($(t).hasClass('tcc-checkbox-list-result') || $(t).hasClass('tcc-radio-list')) {
                    $(t).parent().removeAttr('data-val-requiredifnotempty-1').attr('data-val-requiredifnotempty', attrRi);
                }
                if ($(t).hasClass('tcc-warekidate-source')) {
                    $(t).parent().find('.tcc-warekidate-combo input').addClass('tcc-bg-required');
                    $(t).parent().find('.tcc-warekidate-parts input').addClass('tcc-bg-required');
                }
                if (!$(".tcc-message-area").is(":hidden")) {
                    if (docheck) {
                        $(t).valid();
                    }
                }
            })
        }
    },
    /**
     * RequiredOfCondition変更イベント
     * */
    requiredOfConditionChangeEvent: function () {
        var depends = $(this).data('valid-link-depend-requiredofcondition');
        var _this = this;
        depends.forEach(function (depend) {
            if (_this.name != depend.dependitem) return;
            TorihikiUtils.requiredOfConditionChange(depend, true);
        })
    },
    /**
     * RequiredOfCondition変更処理
     * @param {any} depend
     * @param {any} docheck
     */
    requiredOfConditionChange: function (depend, docheck) {
        $(depend.targetitem).each(function () {
            var element = this;
            var validconds = $(this).data('val-requiredofcondition-validconds');

            var removeRequired = false;

            if (validconds != null) {
                var conds = validconds.split(';');
                // 条件付き必須チェック（複数条件）チェック
                removeRequired = !TorihikiUtils.checkRequiredValidConds(conds, element);
            }

            // 値は依頼値と同じではない時、属性変更
            if (removeRequired) {
                $(depend.targetitem).each(function (j, t) {
                    var attrRi = $(t).attr('data-val-requiredofcondition');
                    $(t).removeAttr('data-val-requiredofcondition').attr('data-val-requiredofcondition-1', attrRi);
                    if ($(t).hasClass('tcc-input-combo')) {
                        $('#' + $(t).attr('id') + '_input').removeAttr('data-val-requiredofcondition').attr('data-val-requiredofcondition-1', attrRi);
                    }
                    if ($(t).hasClass('tcc-checkbox-list-result') || $(t).hasClass('tcc-radio-list')) {
                        $(t).parent().removeAttr('data-val-requiredofcondition').attr('data-val-requiredofcondition-1', attrRi);
                    }
                    if ($(t).hasClass('tcc-warekidate-source')) {
                        $(t).parent().find('.tcc-warekidate-combo input').removeClass('tcc-bg-required');
                        $(t).parent().find('.tcc-warekidate-parts input').removeClass('tcc-bg-required');
                    }
                    if (docheck) {
                        $(t).valid();
                    }
                })
            } else {
                $(depend.targetitem).each(function (j, t) {
                    var attrRi = $(t).attr('data-val-requiredofcondition-1') || $(t).attr('data-val-requiredofcondition');
                    $(t).removeAttr('data-val-requiredofcondition-1').attr('data-val-requiredofcondition', attrRi);
                    if ($(t).hasClass('tcc-input-combo')) {
                        $('#' + $(t).attr('id') + '_input').removeAttr('data-val-requiredofcondition-1').attr('data-val-requiredofcondition', attrRi);
                    }
                    if ($(t).hasClass('tcc-checkbox-list-result') || $(t).hasClass('tcc-radio-list')) {
                        $(t).parent().removeAttr('data-val-requiredofcondition-1').attr('data-val-requiredofcondition', attrRi);
                    }
                    if ($(t).hasClass('tcc-warekidate-source')) {
                        $(t).parent().find('.tcc-warekidate-combo input').addClass('tcc-bg-required');
                        $(t).parent().find('.tcc-warekidate-parts input').addClass('tcc-bg-required');
                    }
                    if (!$(".tcc-message-area").is(":hidden")) {
                        if (docheck) {
                            $(t).valid();
                        }
                    }
                })
            }
        });
    },
    /**
     * 条件付き必須チェック（複数条件）チェック
     * @param {any} conds
     * @param {any} element
     */
    checkRequiredValidConds: function (conds, element) {

        var isRequired = false;
        var checkRequired = [];
        for (i = 0; i < conds.length; i++) {
            var t = conds[i];
            checkRequired.push(false);
            if (t.indexOf('=') >= 1) {
                key = t.split('=')[0];
                checkValue = t.split('=')[1].split(',');
                var condionItems = foolproof.getElements(element, key);               
                if ($(condionItems).hasClass('tcc-checkbox-list-result')) {  // チェックボックスリスト時
                    // tcc-checkbox-list-result
                    var checkEleValue = foolproof.getItemValue(element, key);
                    checkEleValue = checkEleValue.split(',');
                    for (j = 0; j < checkValue.length; j++) {
                        // 値リストに同じ値が設定された、必須となる
                        if (checkEleValue.indexOf(checkValue[j]) > -1) {
                            isRequired = true;
                            break;
                        }
                    }
                } else if (condionItems.length > 0 && condionItems[0].type == "checkbox") { // チェックボックス時
                    var checkedValue = '';
                    for (var index = 0; index != condionItems.length; index++) {
                        if (condionItems[index]["checked"]) {
                            checkedValue = condionItems[index].value;
                            for (j = 0; j < checkValue.length; j++) {
                                if (condionItems[index].value == checkValue[j]) {
                                    isRequired = true;
                                    break;
                                }
                            }
                            if (isRequired) {
                                break;
                            }
                        }
                    }
                    if (checkedValue == '') {
                        for (j = 0; j < checkValue.length; j++) {
                            if (checkedValue == checkValue[j]) {
                                isRequired = true;
                                break;
                            }
                        }
                    }
                } else {
                    var checkEleValue = foolproof.getItemValue(element, key);
                    isRequired = false;
                    for (j = 0; j < checkValue.length; j++) {
                        // 値リストに同じ値が設定された、必須となる
                        if (checkEleValue == checkValue[j]) {
                            isRequired = true;
                        }
                    }
                }
                if (isRequired) {
                    checkRequired[i] = true;
                }
            } else if (t.indexOf('<>') >= 1) {
                key = t.split('<>')[0];
                checkValue = t.split('<>')[1].split(',');
                isRequired = true;
                var condionItems = foolproof.getElements(element, key);   
                if ($(condionItems).hasClass('tcc-checkbox-list-result')) {  // チェックボックスリスト時
                    // tcc-checkbox-list-result
                    var checkEleValue = foolproof.getItemValue(element, key);
                    checkEleValue = checkEleValue.split(',');
                    for (j = 0; j < checkValue.length; j++) {
                        // 値リストに同じ値が設定された、必須となる
                        if (checkEleValue.indexOf(checkValue[j]) > -1) {
                            isRequired = false;
                            break;
                        }
                    }
                } else if (condionItems.length > 0 && condionItems[0].type == "checkbox") { // チェックボックス時
                    var checkedValue = '';
                    for (var index = 0; index != condionItems.length; index++) {
                        if (condionItems[index]["checked"]) {
                            checkedValue = condionItems[index].value;
                            for (j = 0; j < checkValue.length; j++) {
                                if (condionItems[index].value == checkValue[j]) {
                                    isRequired = false;
                                    break;
                                }
                            }
                            if (!isRequired) {
                                break;
                            }
                        }
                    }
                    if (checkedValue == '') {
                        for (j = 0; j < checkValue.length; j++) {
                            if (checkedValue == checkValue[j]) {
                                isRequired = false;
                                break;
                            }
                        }
                    }
                } else {
                    var checkEleValue = foolproof.getItemValue(element, key);
                    for (j = 0; j < checkValue.length; j++) {
                        // 値リストに同じ値が設定された、必須ないとなる
                        if (checkEleValue == checkValue[j]) {
                            isRequired = false;
                        }
                    }
                }
                if (isRequired) {
                    checkRequired[i] = true;
                };
            }
        }

        // 条件不一致
        for (i = 0; i < checkRequired.length; i++) {
            if (!checkRequired[i]) {
                return false;
            }
        }

        return true;
    },
    ///**
    // * フォームの内容をシリアライズ
    // * @param {any} form
    // */
    //serializeObject: function (form) {
    //    var o = {};
    //    var a = form.serializeArray();
    //    $.each(a, function () {
    //        this.name = this.name.split("____")[0];
    //        if (o[this.name]) {
    //            if (!o[this.name].push) {
    //                o[this.name] = [o[this.name]];
    //            }
    //            o[this.name].push(this.value || ',');
    //        } else {
    //            o[this.name] = this.value || '';
    //        }
    //    });
    //    return o;
    //},
    /**
     * フォームの内容をシリアライズ（チェックボックス対応）
     * @param {any} form
     */
    serializeObject: function (form) {
        if (form == undefined) {
            form = $(this).closest('form')[0];
        }
        var o = {};
        var objList = $(form).serializeArray();
        var ele = {};
        /* チェックボックスするとき */
        $(form).find("input[type='checkbox']").each(function (i, t) {
            ele[t.name] = "checkbox";
            var exited = objList.filter(function (item) {
                return item.name == t.name;
            })
            if (exited.length == 0) {
                objList.push({ name: t.name, value: '' });
            }
        });
        //$.each(objList, function () {
        //  /* チェックボックスするとき */
        //  if ($("input[type='checkbox'][name='" + this.name + "']").length > 0) {
        //    ele[this.name] = "checkbox";
        //  }
        //});

        $.each(objList, function () {
            var name = this.name.split("____")[0];
            /* タイプリストにある */
            if (ele[name]) {
                if (this.value == "true" || this.value == "false") {
                    return;
                }
            }
            var value = this.value;
            if ($('[name="' + name + '"]').hasClass('tcc-input-number')) {
                value = value.replace(new RegExp(',', 'g'), '');
            }
            if (o[this.name]) {
                o[name] += ',' + value;
            } else {
                o[name] = value || '';
            }
        });
        return o;
    },
    /**
     * コンテナーの内容をシリアライズ
     * @param {any} container
     */
    serializeContainer: function (container) {
        var content = $(container);
        var o = {};
        var checkbox = {};

        var inputs = content.find('input,select,textarea')
            .filter(function () {
                var type = this.type;

                // Use .is( ":disabled" ) so that fieldset[disabled] works
                return this.name && !jQuery(this).is(":disabled") &&
                    rsubmittable.test(this.nodeName) && !rsubmitterTypes.test(type) &&
                    (this.checked || !rcheckableType.test(type));
            });

        $.each(inputs, function () {
            var name = this.name.split("____")[0];
            var value = $(this).val();
            switch (this.type) {
                case 'checkbox':
                case 'radio':
                    checkbox[name] = this.type;
                    if (!$(this).prop('checked')) {
                        return;
                    }
                    break;
            }
            if ($('[name="' + name + '"]').hasClass('tcc-input-number')) {
                value = value.replace(new RegExp(',', 'g'), '');
            }
            if (o[this.name]) {
                o[name] += ',' + value;
            } else {
                o[name] = value || '';
            }
        });

        $.each(checkbox, function (key, data) {
            if (o[key]) {
                return;
            }
            o[key] = '';
        })

        return o;
    },
    /**
     * コンテナーの内容をシリアライズ(明細プレフィックス無視)
     * @param {any} container
     */
    serializeFieldData: function (container) {
        var content = $(container);
        var o = {};
        var checkbox = {};

        var inputs = content.find('input,select,textarea')
            .filter(function () {
                var type = this.type;

                // Use .is( ":disabled" ) so that fieldset[disabled] works
                return this.name && !jQuery(this).is(":disabled") &&
                    rsubmittable.test(this.nodeName) && !rsubmitterTypes.test(type) &&
                    (this.checked || !rcheckableType.test(type));
            });

        $.each(inputs, function () {
            var name = this.name.split("____")[0];
            var lastNameIndex = name.lastIndexOf('.');
            var lastName = name.substring(lastNameIndex + 1, name.length);
            var value = $(this).val();
            switch (this.type) {
                case 'checkbox':
                case 'radio':
                    checkbox[lastName] = this.type;
                    if (!$(this).prop('checked')) {
                        return;
                    }
                    break;
            }
            if ($('[name="' + name + '"]').hasClass('tcc-input-number')) {
                value = value.replace(new RegExp(',', 'g'), '');
            }
            if (o[lastName]) {
                o[lastName] += ',' + value;
            } else {
                o[lastName] = value || '';
            }
        });

        $.each(checkbox, function (key, data) {
            if (o[key]) {
                return;
            }
            o[key] = '';
        })

        return o;
    },
    /**
     * 他の画面にデータをコミットのために、フォームに一時コントロールを追加する。
     * @param {any} form
     */
    addFormItem: function (form, data, prefix) {
        var self = this;
        var textbox;
        if (typeof prefix === 'undefined') {
            prefix = '';
        }
        var curPrefix = prefix;

        if (TorihikiUtils.isArray(data)) {
            $(data).each(function (i, t) {
                curPrefix = prefix + '[' + i + '].';
                self.addFormItem(form, t, curPrefix);
            })
        } else {
            self.addFormItemBody(form, data, prefix);
        }
        return form;
    },
    /**
     * 他の画面にデータをコミットのために、フォームに一時コントロールを追加する(本体）
     * @param {any} form
     */
    addFormItemBody: function (form, data, prefix) {
        var self = this;
        var textbox;

        // キーでコントロール作成
        if (TorihikiUtils.isObj(data)) {
            for (var key in data) {
                var curPrefix = prefix;
                key = key.trim();
                var idPrefix;
                if (TorihikiUtils.isObj(data[key])) {
                    curPrefix = prefix + key + '.';
                    self.addFormItem(form, data[key], curPrefix);
                    continue;
                }
                if (TorihikiUtils.isArray(data[key])) {
                    $(data[key]).each(function (i, t) {
                        curPrefix = prefix + key + '[' + i + '].';
                        self.addFormItem(form, t, curPrefix);
                    })
                    continue;
                }
                if (curPrefix) {
                    idPrefix = curPrefix.replace(new RegExp('\\[', 'g'), '_').replace(new RegExp('\\]', 'g'), '_').replace(new RegExp('\\.', 'g'), '_');
                }
                var value = data[key] == null || data[key] == undefined ? '' : data[key];
                if (value.toString().indexOf('\n') != -1) {
                    textbox = '<textarea name="' + curPrefix + key + '" id="' + idPrefix + key + '">' + value + '</textarea>';
                } else {
                    textbox = '<input type=textbox name="' + curPrefix + key + '" id="' + idPrefix + key + '" value="' + value + '" />';
                }
                $(form).append(textbox);
            }
        } else {
            var curPrefix = prefix;
            curPrefix = curPrefix.substring(0, curPrefix.length - 1);
            if (curPrefix) {
                idPrefix = curPrefix.replace(new RegExp('\\[', 'g'), '_').replace(new RegExp('\\]', 'g'), '_').replace(new RegExp('\\.', 'g'), '_');
            }
            var value = data == null || data == undefined ? '' : data;
            if (value.toString().indexOf('\n') != -1) {
                textbox = '<textarea name="' + curPrefix  + '" id="' + idPrefix + '">' + value + '</textarea>';
            } else {
                textbox = '<input type=textbox name="' + curPrefix  + '" id="' + idPrefix + '" value="' + value + '" />';
            }
            $(form).append(textbox);
        }
        return form;
    },
    /**
     * コンテナの項目をコントロール化にする。
     * @param {any} container
     */
    getFormData: function (form) {
        var data = TorihikiUtils.serializeObject(form);
        var o = new FormData();
        for (var key in data) {
            o.append(key, data[key]);
        }
        return o;
    },
    addPageSessionId: function (url) {
        if (url.indexOf('?') > -1) {
            var t = TorihikiUtils.getTargetUrlParam(url, TorihikiGlobal.PageSessionKey);
            if (t == undefined) {
                url += '&t=' + TorihikiUtils.getPageSessionId();
            }
        } else {
            url += '?t=' + TorihikiUtils.getPageSessionId();
        }
        return url;
    },
    /**
     * 画面遷移処理。
     * @param {any} data
     * @param {any} url
     */
    pageRedirect: function (data, url) {

        if (typeof url === 'undefined') {
            var pathName = location.pathname;
            if (pathName.substring(pathName.length - 6, pathName.length).toLowerCase() == '/index') {
                pathName = pathName.substring(0, pathName.length - 6);
            }
            url = location.origin + pathName + "/PageRedirect?t=" + TorihikiUtils.getPageSessionId();
        } else {
            url = this.addPageSessionId(url);
        }
        TorihikiUtils.showLoading();

        //// 存在する仮フォームを削除する
        //$('#_dummyPostForm').remove();
        //// 仮フォーム作成
        //var dummyPostForm = document.createElement('form');
        //// 非表示になる
        //$(dummyPostForm).hide();
        //$(dummyPostForm).attr('id', '_dummyPostForm');
        //$(dummyPostForm).attr('action', url);
        //$(dummyPostForm).attr('method', 'post');
        //// パラメータをアイテムで作成し、フォームに追加する
        //$(dummyPostForm).addFormItem(data);
        //// 偽造防止フォーム フィールド追加
        //$(dummyPostForm).appendToken();
        //// Chrome時、仮フォームのコミットできないので、画面にフォームを追加する。
        //$(document.body).append(dummyPostForm)
        //$(dummyPostForm).submit();

        //// フォームを削除する
        //$(dummyPostForm).remove();

        TorihikiUtils.ajaxEx({
            url: url,
            data: data,
            //success: function (data) {
            //    if (typeof data !== 'undefined' && typeof data.redirecturl !== 'undefined') {
            //        location.replace(data.redirecturl);
            //    }
            //}
        })
    },
    /**
     * 画面をポップアップで表示する。
     * @param {any} options
     * @param {any} url
     */
    showPageOnPopUp: function (options, url) {

        // パラメータ処理Url
        if (typeof url === 'undefined') {
            var pathName = location.pathname;
            if (pathName.substring(pathName.length - 6, pathName.length).toLowerCase() == '/index') {
                pathName = pathName.substring(0, pathName.length - 6);
            }
            url = location.origin + pathName + "/PageRedirect?t=" + TorihikiUtils.getPageSessionId();
        } else {
            url = this.addPageSessionId(url);
        }

        TorihikiUtils.showLoading();

        var op = $.extend({ type: 'get'}, options);

        // パラメータ処理
        TorihikiUtils.ajaxEx({
            url: url,
            data: op.data,
            success: function (data) {
                if (typeof data !== 'undefined' && typeof data.redirecturl !== 'undefined') {
                    op.url = data.redirecturl;
                    op.params = data.data;
                    TorihikiUtils.showEditPopup(op);
                }
            }
        })
    },
    //　戻るボタン制御
    isHistoryPush: false,
    // pushしたいタイミングで呼び出します
    pushHistory: function () {
        if (history && history.pushState && history.state !== undefined) {
            TorihikiUtils.isHistoryPush = true;
            history.pushState(null, null, null);
        }
    },
    /**
     * コンテナの項目をコントロール化にする。
     * @param {any} container
     */
    addFormData: function (formData, data) {
        for (var key in data) {
            formData.append(key, data[key]);
        }
        return formData;
    },
    /**
     * コンテナの項目をコントロール化にする。
     * @param {any} container
     */
    setSettings: function (container) {
        var content;
        if (container) {
            content = $(container);
        } else {
            content = $(document);
        }
        /* ポップアップ画面関連 */
        TorihikiUtils.setPopUp(content);

        /* コントロール設定 */
        TorihikiUtils.setControls(content);

        /* ツールチップ設定 */
        TorihikiUtils.setTooltip(content);

        // # 変換
        content.find('a[href="#"]').each(function (i, t) {
            $(t).attr('href', 'javascript:void(0)');
        })

        // メッセージ非表示処理
        content.find('.tcc-message-area').on('hide.bs.modal', function () {
            if (TorihikiUtils.validitem) {
                var focustarget = TorihikiUtils.validitem;
                // FixedMidashi
                if (focustarget.$FXH_FIXED_ELEMENT) {
                    focustarget = focustarget.$FXH_FIXED_ELEMENT;
                    focustarget = $('#' + focustarget.id)[0];
                }
                var itemid = focustarget.id;
                if ($(focustarget[0]).hasClass('tcc-phone-source') || $(focustarget[0]).hasClass('tcc-warekidate-source')) {
                    var part1 = $(focustarget[0]).data('part1');
                    setTimeout("TorihikiUtils.setFocus('" + part1[0].id + "')", 50);
                } else {
                    setTimeout("TorihikiUtils.setFocus('" + itemid + "')", 50);
                }
            }
        });

        // 閉じるボタン
        content.find('.pop-close,#popCloseIcon').click(function () {
            // 画面閉じる
            TorihikiUtils.hidePopModalSelf();
        });

        // 閉じるボタン
        content.find('#popCloseDetailIcon').click(function () {
            // 画面閉じる
            TorihikiUtils.hidePopDetailModalSelf();
        });

        // 閉じる
        content.find('.tcc-btn-errmsg-close').click(function (e) {
            TorihikiUtils.hideErrmsgArea();
        });

        for (var v = 1; v < 10; v++) {
            setTimeout(function () {
                // リスト自動高さ設定
                TorihikiUtils.setGridAutoHeight();
            }, v * 20);
        }
    },
    /**
     * フォーカス処理。
     * @param {any} itemid
     */
    setFocus: function (item) {
        /** パラメータがコントロール時 */
        if (typeof item == 'object') {
            var tabpage = item.closest('.tcc-tab-page');
            /** Tabコントロール以外 */
            if (tabpage != undefined && tabpage.length > 0) {
                /** Tabコンテナ取得 */
                var tabPages = $(tabpage.closest('.tcc-tabs')).find('.tcc-tab-pages');
                if (tabPages != undefined) {
                    var pageid = $(tabpage).attr('id');
                    /** タブページ表示 */
                    tabPages.find('a[href="#' + pageid + '"]').tab("show");
                    if ($(item).attr('id') != undefined) {
                        setTimeout(function () {
                            TorihikiUtils.setFocusOnLoop($(item).attr('id'));
                        }, 50);
                    }
                }
            }
        } else {
            if ($(item).length != 0) {
                item = $(item);
            } else {
                item = $('#' + item);
            }
        }
        
        // 電話番号、和暦カレンダー、郵便番号時
        item = $(item);
        if (item.hasClass('tcc-phone-source') ||
            item.hasClass('tcc-zip-source')) {
            item = $(item.data('part1'));
        }
        if (item.hasClass('tcc-warekidate-source')) {
            item = $(item.data('partcombo'));
        }

        if (item.is(':radio')) {
            var elename = item.attr('name');
            item = $('[name="' + elename + '"]:visible:not(:disabled)').first();
        }

        // コンボボックス時
        if (item.hasClass('tcc-input-combo')) {
            setTimeout(function () {
                item.comboSelect('_open');
            }, 100)
        }

        item.focus();
        item.select();
    },
    /**
   * フォーカス処理。
   * @param {any} itemid
   */
    setFocusOnLoop: function (item, count) {
        if (count == undefined) {
            count = 1
        } else {
            count += 1;
        }
        if (count > 10) {
            return;
        }

        if ($('#' + item).hasClass('tcc-phone-source') ||
            $('#' + item).hasClass('tcc-zip-source')) {
            item = $('#' + item).data('part1').attr('id');
        }

        if ($('#' + item).hasClass('tcc-warekidate-source')) {
            item = $('#' + item).data('partcombo').attr('id');
        }

        $('#' + item).focus().select();
        if (document.activeElement.id != item) {
            setTimeout('TorihikiUtils.setFocusOnLoop("' + item + '",' + count + ')', 50);
        }
    },
    /**
     * エラーメッセージエラー取得する。
     * @param {any} element
     */
    getErrMessageArea: function (element) {
        var containter;
        if (typeof element === "undefined") {
            containter = $(document);
        } else if (typeof element === "object") {
            var tagName = (element.tagName || '').toUpperCase();
            if (element.tagName == "FORM") {
                containter = $(element);
            } else {
                containter = $(element).parents('form');
            }
        } else if (typeof element === "string") {
            containter = $(element);
        }
        return containter;
    },
    /** JqueryValidエラー内容表示。*/
    showErrmsgArea: function (element) {

        var containter = TorihikiUtils.getErrMessageArea(element);

        $("#tempMsg").remove();
        var count = containter.find('div.tcc-error-message .card-body li').length;
        if (count > 20) {
            count = 20;
        }
        containter.find('.tcc-message-area').removeClass('modal');
        /* 自定義Styleをクリア */
        containter.find('div.tcc-error-message').removeAttr('style');
        /* 上位置を初期する。 */
        if (!containter.find('.tcc-header').is(':hidden')) {
            containter.find('div.tcc-error-message')[0].style.top = $('.tcc-header').outerHeight() + 'px';
        } else {
            containter.find('div.tcc-error-message')[0].style.top = '0px';
        }
        /* 右位置を初期する。 */
        containter.find('div.tcc-error-message')[0].style.right = '3px';
        /* 高さを設定する。 */
        containter.find('div.tcc-error-message').height(145 + count * 20);
        // ログインページ時
        if (typeof topPageUrl === 'undefined') {
            $('.tcc-message-area').modal('show');
        } else {
            /* メッセージエリアを表示する。 */
            containter.find('.tcc-message-area').fadeIn();
            /* メッセージエリアを移動できるで設定する。 */
            containter.find('div.tcc-error-message').draggable();
            /* $('.tcc-message-area').modal('show'); */

        }
    },
    /** JqueryValidエラー内容非表示。*/
    hideErrmsgArea: function (element, focus) {
        var doFocus = focus;
        // パラメータ指定ない場合
        if (typeof doFocus === 'undefined') {
            doFocus = true;
        }
        // 一つ目パラメータはBoolean場合
        if (typeof element === "boolean") {
            doFocus = element;
            element = undefined;
        }
        var containter = TorihikiUtils.getErrMessageArea(element);

        $("#tempMsg").remove();

        //$('.tcc-message-area').modal('hide');
        containter.find('.tcc-message-area').fadeOut();

        //エラー画面が閉じられる時、まだ直していないエラーに関するコントロールへフォーカス移動
        var errlists = containter.find(".validation-summary-errors li");
        $(errlists).each(function (i, t) {
            if ($(this).data('target') == undefined) return;
            if (!$(this).hasClass('tcc-error-clean')) {
                var focustarget = $(this).data('target');
                // FixedMidashi
                if (focustarget.$FXH_FIXED_ELEMENT) {
                    focustarget = focustarget.$FXH_FIXED_ELEMENT;
                    focustarget = $('#' + focustarget.id)[0];
                }

                // フォーカス処理実行時
                if (doFocus) {
                    if ($(focustarget).hasClass('tcc-input-combo')) {
                        setTimeout(function () {
                            $(focustarget).comboSelect('_open');
                        }, 100)
                    }

                    TorihikiUtils.setFocus(focustarget);
                    $(focustarget).select();
                }

                return false;
            }
        });
    },
    /**
     * エラー情報表示.
     * @param {any} response
     * @param {any} status
     * @param {any} xhr
     * @param {any} url
     */
    showErrors: function (response, status, xhr, url) {
        TorihikiUtils.hideLoading();
        // サーバに接続できない時
        if (response.status == 0) {
            TorihikiMsg.alert(messages.ResponseErrorStatus0);
            return;
        }
        var popModelid = TorihikiUtils.getRandomId('popModal');
        var popIframeid = popModelid.replace('popModal', 'ifrmpop');
        var popdiv = TorihikiUtils.window.$('#popModal').clone().attr('id', popModelid).removeAttr('data-backdrop').addClass('tcc-popsearch').addClass('tcc-error-dialog');
        popdiv.empty();
        TorihikiUtils.window.$('#popModal').after(popdiv);
        var contents = '<div class="modal-dialog"><div class="modal-content"><div class="modal-header">';
        contents += '<h3 class="modal-title" id="myModalLabel">エラー情報</h3>';
        contents += '<a style="cursor:pointer" class="close" data-dismiss="modal" aria-hidden="true"><i class="fas fa-window-close"></i></a>';
        contents += '</div><div class="modal-body"></div></div> '
        popdiv.html(contents);
        if (response.status == 404) {
            popdiv.find('.modal-body').html("<h2>" + url + "<br/>" + response.status + " " + response.statusText + "</h2>");
        } else {
            // DEBUG
            popdiv.find('.modal-body').html('<iframe frameborder="0" id="ifrmerror" name="ifrmerror" scrolling="auto" style="width:100%;height:92%;" ></iframe>');
            TorihikiUtils.window.frames['ifrmerror'].document.write(response.responseText);
            popdiv.find('.modal-dialog').attr('style', 'width:800px;height:600px;');
            popdiv.find('.modal-content').attr('style', 'height:100%;');
        }
        popdiv.modal("show");
        popdiv.on('hide.bs.modal', function () {
            popdiv.remove();
        })
    },

    /**
     * 検索条件作成
     * @param {any} container
     */
    makeSearchCondition: function (container, pageIndex, pageSize) {
        var content;
        if (container) {
            content = $(container);
        } else {
            content = $('form')[0];
        }
        query = TorihikiUtils.serializeObject($(content));
        query.PageNumber = pageIndex;
        query.PageSize = pageSize;
        query.t = TorihikiUtils.getPageSessionId();
        return query;
    },
    /**
     * 日時の日付部分取得
     * @param {any} v
     */
    getDateOnly: function (v) {
        if (v && v.length > 10) {
            return v.substring(0, 10);
        }
        return v;
    },
    /**
     * Ajaxの戻る結果確認
     * @param {any} data
     */
    checkResult: function (data) {
        if (typeof (data) == "undefined") {
            return true;
        }
        if (typeof (data.success) != "undefined" && !data.success) {
            TorihikiMsg.alert(data.message);
            if (data.redirecturl != null) {
                top.location.replace(data.redirecturl);
            }
            return false;
        }
        return true;
    },
    /**
     * 文字フォーマット処理(C# string.format)
     * @param {any} source
     * @param {any} params
     */
    format: function (source, params) {
        if (arguments.length == 1)
            return function () {
                var args = $.makeArray(arguments);
                args.unshift(source);
                return $.format.apply(this, args);
            };
        if (arguments.length > 1 && (!params || params.constructor != Array)) {
            params = $.makeArray(arguments).slice(1);
        }
        if (params.constructor != Array) {
            params = [params];
        }
        $.each(params, function (i, n) {
            source = source.replace(new RegExp("\\{" + i + "\\}", "g"), n);
        });
        return source;
    },
    doChangeTabTitle: true,
    /**
     * タブページのタイトル変更
     * @param {any} title
     */
    changeTabTitle: function (title) {
        if (typeof title === 'undefined')
            title = '';

        var framename = window.name;
        var menuid = framename.split("-")[1];
        if (parent) {
            if (!TorihikiUtils.doChangeTabTitle) return;

            if (title == '') {
                parent.$('#pg_name').text('');
            }
            else {
                parent.$('#pg_name').text(' - ' + title);
            }
        }
        if (!menuid) {
            return;
        }

        var closebtn = '<i class="fas fa-window-close" style="float:right;cursor:pointer;" onclick="closeTab(\'{0}\')"></i>';
    },
    /**
     * ガイダンスボタンのurl変更
     * @param {any} url
     */
    changeGuidanceUrl: function (url) {
        if (TorihikiUtils.isPopUpWindow()) return;

        if (TorihikiUtils.isNullOrEmpty(url)) {
            $('.tcc-header-guidance').css('display', 'none');
            if (parent) {
                parent.$('.tcc-header-guidance').css('display', 'none');
            }
        }
        else {
            $('.tcc-header-guidance').css('display', 'unset');
            $('.tcc-header-guidance').data("url", url);
            if (parent) {
                parent.$('.tcc-header-guidance').css('display', 'unset');
                parent.$('.tcc-header-guidance').data("url", url);
            }
        }
    },
    /**
     * ガイダンスURLを開く
     * @param {any} url
     */
    openGuidanceUrl: function (url) {
        TorihikiUtils.showGuidancePopup({ url: url, width : '1024'});
    },
    /**
     * データ参照画面表示
     * @param {any} master
     * @param {any} keys
     */
    openShowDataPage: function (master, keys) {
        return false;
    },
    /**
     * 日時を日付に変換する
     * @param {any} cellval
     */
    changeDateFormat: function (cellval) {
        var dateVal = cellval + "";
        if (cellval != null) {
            var date = new Date(parseInt(dateVal.replace("/Date(", "").replace(")/", ""), 10));
            var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
            var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();

            var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
            var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
            var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();

            return date.getFullYear() + "/" + month + "/" + currentDate;
        }
    },
    /**
     * Objectをurl変数に変換
     * @param {any} param
     * @param {any} key
     */
    parseParam: function (param, key) {
        var paramStr = "";
        if (typeof param === 'string' || typeof param === 'number' || typeof param === 'boolean') {
            paramStr += "&" + key + "=" + encodeURIComponent(param);
        } else {
            $.each(param, function (i, t) {
                var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
                paramStr += '&' + TorihikiUtils.parseParam(t, k);
            });
        }
        return paramStr.substr(1);
    },
    /**
     *　全角英数字を半角英数字に変換する。
     * @param {any} str
     */
    CtoH: function (str) {
        var result = "";
        if (str == undefined) return result;
        var strTmp = str.toString().replace(/[‐－―ー]/g, '-');
        for (var i = 0; i < strTmp.length; i++) {
            if (strTmp.charCodeAt(i) == 12288) {
                result += String.fromCharCode(strTmp.charCodeAt(i) - 12256);
                continue;
            }
            if (strTmp.charCodeAt(i) > 65280 && strTmp.charCodeAt(i) < 65375) {
                result += String.fromCharCode(strTmp.charCodeAt(i) - 65248);
            } else {
                result += String.fromCharCode(strTmp.charCodeAt(i));
            }
        }
        return result;
    },
    /**
      * 数値判断
      * @param {any} str
      */
    isNumber: function (instr) {

        var basestr = TorihikiUtils.CtoH(instr);
        var str = basestr;

        str = str.replace(/[^(\-|\+)\d.]/g, "");
        str = str.replace(/^\./g, "");
        str = str.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");

        // 一番目文字が[.]時
        if (basestr.indexOf('.') == 0) {
            str = '.' + str;
        }
        if (basestr.length != str.length) {
            return false;
        }
        return true;
    },
    /**
     * 数値(int)判断
     * @param {any} instr
     */
    isIntNumber: function (instr) {
        var basestr = instr;
        var str = basestr;
        str = str.replace(/\D/g, '');
        if (str.value.length != basestr.length) {
            return false;
        }
        return true;
    },
    /**
     * ゼロを埋める
     * @param {any} obj
     * @param {any} len
     */
    appendZero: function (obj, len) {
        if (len == undefined) { len = 2; }
        var data = obj;
        for (i = obj.toString().length; i < len; i++) {
            data = '0' + data;
        }
        return data;
    },
    /**
     * 日付加算
     * @param {any} interval
     * @param {any} number
     * @param {any} date
     */
    dateAdd: function (interval, number, date) {
        switch (interval) {
            case "y": {
                date.setFullYear(date.getFullYear() + number);
                return date;
                break;
            }
            case "q": {
                date.setMonth(date.getMonth() + number * 3);
                return date;
                break;
            }
            case "m": {
                date.setMonth(date.getMonth() + number);
                return date;
                break;
            }
            case "w": {
                date.setDate(date.getDate() + number * 7);
                return date;
                break;
            }
            case "d": {
                date.setDate(date.getDate() + number);
                return date;
                break;
            }
            case "h": {
                date.setHours(date.getHours() + number);
                return date;
                break;
            }
            case "mi": {
                date.setMinutes(date.getMinutes() + number);
                return date;
                break;
            }
            case "s": {
                date.setSeconds(date.getSeconds() + number);
                return date;
                break;
            }
            default: {
                date.setDate(date.getDate() + number);
                return date;
                break;
            }
        }
    },
    /**
     * 日付フォーマット
     * @param {any} date
     * @param {any} format
     */
    formatDate: function (date, format) {
        if (!date)
            return '';

        var val = {
            d: date.getDate(),
            m: date.getMonth() + 1,
            yy: date.getFullYear().toString().substring(2),
            yyyy: date.getFullYear(),
            h: date.getHours(),
            mi: date.getMinutes(),
            s: date.getSeconds()
        };
        val.dd = (val.d < 10 ? '0' : '') + val.d;
        val.MM = (val.m < 10 ? '0' : '') + val.m;
        val.hh = (val.h < 10 ? '0' : '') + val.h;
        val.mm = (val.mi < 10 ? '0' : '') + val.mi;
        val.ss = (val.s < 10 ? '0' : '') + val.s;

        switch (format) {
            case 'yyyyMMddhhmmss':
            case 'yyyymmddhhmmss':
                return val.yyyy + val.MM + val.dd + val.hh + val.mm + val.ss;
            case 'yyyymmdd':
            case 'yyyyMMdd':
                return val.yyyy + val.MM + val.dd;
            case 'yyyymm':
            case 'yyyyMM':
                return val.yyyy + val.MM;
            case 'yyyy/mm':
            case 'yyyy/MM':
                return val.yyyy + '/' + val.MM;
            default:
                return val.yyyy + '/' + val.MM + '/' + val.dd;
        }
    },
    /**
     * プラグインSelect2の自定義マッチ関数
     * @param {any} params
     * @param {any} data
     */
    matchCustom: function (params, data) {
        // If there are no search terms, return all of the data
        if ($.trim(params.term) === '') {
            return '';
        }

        // Do not display the item if there is no 'text' property
        if (typeof data.text === 'undefined') {
            return null;
        }

        var original = data.text.toLowerCase();
        var term = params.term.toLowerCase();
        var tokens = $(data.element).data().tokens;

        if (original.indexOf(term) > -1) {
            return data;
        }

        for (var item in tokens) {
            if (typeof (tokens[item]) == "function")
                continue;

            if (TorihikiUtils.toDBC(tokens[item].toString()).toLowerCase().indexOf(TorihikiUtils.toDBC(term.toLowerCase())) > -1) {
                return data;
            }
        }

        // Return `null` if the term should not be displayed
        return null;
    },
    /**
     * 半角カタカナを全角カタカナに変換する
     * @param {any} str
     */
    toDBC: function (str) {

        if (str == "") return str;
        replaceFm = new Array(
            'ｳﾞ', 'ｶﾞ', 'ｷﾞ', 'ｸﾞ', 'ｹﾞ', 'ｺﾞ'
            , 'ｻﾞ', 'ｼﾞ', 'ｽﾞ', 'ｾﾞ', 'ｿﾞ'
            , 'ﾀﾞ', 'ﾁﾞ', 'ﾂﾞ', 'ﾃﾞ', 'ﾄﾞ'
            , 'ﾊﾞ', 'ﾋﾞ', 'ﾌﾞ', 'ﾍﾞ', 'ﾎﾞ'
            , 'ﾊﾟ', 'ﾋﾟ', 'ﾌﾟ', 'ﾍﾟ', 'ﾎﾟ'
        );
        replaceTo = new Array(
            'ヴ', 'ガ', 'ギ', 'グ', 'ゲ', 'ゴ'
            , 'ザ', 'ジ', 'ズ', 'ゼ', 'ゾ'
            , 'ダ', 'ヂ', 'ヅ', 'デ', 'ド'
            , 'バ', 'ビ', 'ブ', 'ベ', 'ボ'
            , 'パ', 'ピ', 'プ', 'ペ', 'ポ'
        );
        for (var key in replaceFm) {
            str = str.replace(new RegExp(replaceFm[key], 'g'), replaceTo[key]);
        }

        replaceFm = new Array(
            'ｱ', 'ｲ', 'ｳ', 'ｴ', 'ｵ'
            , 'ｶ', 'ｷ', 'ｸ', 'ｹ', 'ｺ'
            , 'ｻ', 'ｼ', 'ｽ', 'ｾ', 'ｿ'
            , 'ﾀ', 'ﾁ', 'ﾂ', 'ﾃ', 'ﾄ'
            , 'ﾅ', 'ﾆ', 'ﾇ', 'ﾈ', 'ﾉ'
            , 'ﾊ', 'ﾋ', 'ﾌ', 'ﾍ', 'ﾎ'
            , 'ﾏ', 'ﾐ', 'ﾑ', 'ﾒ', 'ﾓ'
            , 'ﾔ', 'ﾕ', 'ﾖ'
            , 'ﾗ', 'ﾘ', 'ﾙ', 'ﾚ', 'ﾛ'
            , 'ﾜ', 'ｦ', 'ﾝ'
            , 'ｧ', 'ｨ', 'ｩ', 'ｪ', 'ｫ'
            , 'ｬ', 'ｭ', 'ｮ', 'ｯ'
            , '､', '｡', 'ｰ', '｢', '｣', 'ﾞ', 'ﾟ'
        );
        replaceTo = new Array(
            'ア', 'イ', 'ウ', 'エ', 'オ'
            , 'カ', 'キ', 'ク', 'ケ', 'コ'
            , 'サ', 'シ', 'ス', 'セ', 'ソ'
            , 'タ', 'チ', 'ツ', 'テ', 'ト'
            , 'ナ', 'ニ', 'ヌ', 'ネ', 'ノ'
            , 'ハ', 'ヒ', 'フ', 'ヘ', 'ホ'
            , 'マ', 'ミ', 'ム', 'メ', 'モ'
            , 'ヤ', 'ユ', 'ヨ'
            , 'ラ', 'リ', 'ル', 'レ', 'ロ'
            , 'ワ', 'ヲ', 'ン'
            , 'ァ', 'ィ', 'ゥ', 'ェ', 'ォ'
            , 'ャ', 'ュ', 'ョ', 'ッ'
            , '、', '。', 'ー', '「', '」', '”', ''
        );
        for (var key in replaceFm) {
            str = str.replace(new RegExp(replaceFm[key], 'g'), replaceTo[key]);
        }

        return str;
    },
    /**
     * 半角英数字を全角英数字に変換する。
     * @param {any} str
     */
    toDBCFull: function (str) {
        var tmp = "";
        for (var i = 0; i < str.length; i++) {
            if (str.charCodeAt(i) == 32) {
                tmp = tmp + String.fromCharCode(12288);
            }
            if (str.charCodeAt(i) < 127) {
                tmp = tmp + String.fromCharCode(str.charCodeAt(i) + 65248);
            }
        }
        return tmp;
    },
    /**
     * 全角かなを半角ｶﾅに変換する。
     * @param {any} str
     */
    toCDBFull: function (str) {
        var tmp = "";
        if (str == undefined) return tmp;
        for (var i = 0; i < str.length; i++) {
            if (str.charCodeAt(i) > 65248 && str.charCodeAt(i) < 65375) {
                tmp += String.fromCharCode(str.charCodeAt(i) - 65248);
            } else {
                tmp += String.fromCharCode(str.charCodeAt(i));
            }
        }
        return tmp
    },
    /**
     * 全角英数字を半角英数字に変換する。
     * @param {any} str
     */
    toHalfNum: function (str) {
        var tmp = "";
        if (str == undefined) return tmp;
        var strTmp = str.replace(/[‐－―ー]/g, '-');
        for (var i = 0; i < strTmp.length; i++) {
            if (strTmp.charCodeAt(i) > 65248 && strTmp.charCodeAt(i) < 65375) {
                tmp += String.fromCharCode(strTmp.charCodeAt(i) - 65248);
            } else {
                tmp += String.fromCharCode(strTmp.charCodeAt(i));
            }
        }
        return tmp
    },
    /**
     * コントロールの属性をコメントのパラメータに変換する。
     * @param {any} ctl
     */
    convertCommentParams: function (ctl) {
        var data = {};
        data.dempyo_no = $(ctl).data('dempyo-no');
        if (data.dempyo_no == undefined) data.dempyo_no = '';
        data.dempyo_shikibetsu_kbn = $(ctl).data('dempyo-shikibetsu-kbn');
        if (data.dempyo_shikibetsu_kbn == undefined) {
            data.dempyo_shikibetsu_kbn = data.dempyo_no.substring(0, 2);
        }
        data.comment_seq_no = $(ctl).data('comment-seq-no');
        data.comment_readonly = $(ctl).data('comment-readonly');
        data.shanai_memo_readonly = $(ctl).data('shanai-momo-readonly');

        if (typeof $(ctl).data('shanai-memo-element') !== 'undefined') {
            var shanaiMemoElement = $(ctl).data('shanai-memo-element');
            data.shanai_memo = $('input' + shanaiMemoElement + ',textarea' + shanaiMemoElement).val();
            data.shanai_memo_param_flg = "1";
        }
        return data;
    },
    /**
     * 伝票№のリック作成
     * @param {any} denpyo_no
     * @param {any} otherParams  {a :'1', b: '2'}
     */
    makeDenpyoNoLink: function (denpyo_no, otherParams) {
        var basehtml = denpyo_no;
        var basetpl = '<a href="javascript:void(0)" master="{0}" class="popdetail-link" poprefdata=\'{1}\'>{2}</a>';
        var otherparam = '';
        if (typeof otherParams !== 'undefined') {
            otherparam = JSON.stringify(otherParams);
            otherparam = ', ' + otherparam.substring(1, otherparam.length - 1);
        }
        switch (denpyo_no.substring(0, 2).toUpperCase()) {
            case "AZ": // 預かり商品№
                basehtml = TorihikiUtils.format(basetpl, 'azukarishohin', '{"azukari_shohin_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "CI": // 駐車違反№
                basehtml = TorihikiUtils.format(basetpl, 'chushaihan', '{"chushaihan_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "CT": // カット伝票№
                basehtml = TorihikiUtils.format(basetpl, 'cut', '{"cut_dempyo_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "DH": // 導入注文№
                basehtml = TorihikiUtils.format(basetpl, 'donyuhatchu', '{"donyu_hatchu_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "DI": // 代車注文№
                basehtml = TorihikiUtils.format(basetpl, 'daishahatchu', '{"daisha_hatchu_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "DK": // 導入検収№
                basehtml = TorihikiUtils.format(basetpl, 'donyukenshu', '{"donyu_kenshu_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "DN": // 導入入荷№
                basehtml = TorihikiUtils.format(basetpl, 'donyunyuka', '{"donyu_nyuka_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "DT": // 導入申請No.
                basehtml = TorihikiUtils.format(basetpl, 'dounyusinsei', '{"dounyu_sinsei_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "DS": // 出来高請求№
                basehtml = TorihikiUtils.format(basetpl, 'dekidakaseikyu', '{"dekidaka_seikyu_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "FK": // 振分消込№
                basehtml = TorihikiUtils.format(basetpl, 'furiwakekeshikomi', '{"furiwake_keshikomi_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "GH": // 現場変更№
                basehtml = TorihikiUtils.format(basetpl, 'genbahenko', '{"genba_henko_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "HA": // 注文№
                basehtml = TorihikiUtils.format(basetpl, 'hatchu', '{"hatchu_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "HB": // 販売伝票№
                basehtml = TorihikiUtils.format(basetpl, 'hanbai', '{"hanbai_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "HD": // 返却伝票№
                basehtml = TorihikiUtils.format(basetpl, 'henkyakudempyo', '{"mitsumori_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "HE": // 返却№
                basehtml = TorihikiUtils.format(basetpl, 'henkyaku', '{"henkyaku_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "HT": // 配送手配№
                basehtml = TorihikiUtils.format(basetpl, 'haisotehai', '{"haiso_tehai_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "HU": // 返却受付№
                basehtml = TorihikiUtils.format(basetpl, 'henkyakuuketsuke', '{"henkyaku_uketsuke_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "HZ": // 保険増額依頼№
                basehtml = TorihikiUtils.format(basetpl, 'hokenzogakuirai', '{"hoken_zogaku_irai_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "ID": // 移動依頼№
                basehtml = TorihikiUtils.format(basetpl, 'idoirai', '{"ido_irai_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "IR": // 入替№
                basehtml = TorihikiUtils.format(basetpl, 'irekae', '{"irekae_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "JK": // 除却No.
                basehtml = TorihikiUtils.format(basetpl, 'jokyaku', '{"jokyaku_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "JT": // 巡回点検№
                basehtml = TorihikiUtils.format(basetpl, 'junkaitenken', '{"junkai_tenken_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "JU": // 受注№
                basehtml = TorihikiUtils.format(basetpl, 'juchu', '{"juchu_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "KA": // 貸渡証№
                basehtml = TorihikiUtils.format(basetpl, 'kashiwatashisho', '{"kashiwatashisho_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "KO": // 小口№
                basehtml = TorihikiUtils.format(basetpl, 'koguchi', '{"koguchi_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "KS": // 検収№
                basehtml = TorihikiUtils.format(basetpl, 'kenshu', '{"kenshu_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "KU": // 故障受付№
                basehtml = TorihikiUtils.format(basetpl, 'koshouketsuke', '{"kosho_uketsuke_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "MI": // 見積№
                basehtml = TorihikiUtils.format(basetpl, 'mitsumori', '{"mitsumori_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "MT": // 戻点検№
                basehtml = TorihikiUtils.format(basetpl, 'modoritenken', '{"modori_tenken_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "ND": // 納品伝票№
                basehtml = TorihikiUtils.format(basetpl, 'nohindempyo', '{"nohin_dempyo_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "NE": // 値引・加算伝票№
                basehtml = TorihikiUtils.format(basetpl, 'nebikikasan', '{"nebiki_kasan_dempyo_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "NK": // 入庫№
                basehtml = TorihikiUtils.format(basetpl, 'nyuko', '{"nyuko_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "NO": // 納品№
                basehtml = TorihikiUtils.format(basetpl, 'nohin', '{"nohin_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "NY": // 入金伝票№
                basehtml = TorihikiUtils.format(basetpl, 'nyukindempyo', '{"nyukin_dempyo_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "RM": // リメイク№
                basehtml = TorihikiUtils.format(basetpl, 'remake', '{"remake_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "RS": // 領収証№
                basehtml = TorihikiUtils.format(basetpl, 'ryoshusho', '{"ryoshusho_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "SC": // 支払請求チェック№
                basehtml = TorihikiUtils.format(basetpl, 'shiharaiseikyu', '{"shiharai_seikyu_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "SE": // 請求書№
                basehtml = TorihikiUtils.format(basetpl, 'seikyusho', '{"seikyusho_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "SH": // 支払明細書№
                basehtml = TorihikiUtils.format(basetpl, 'shiharaimeisai', '{"shiharai_meisai_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "SK": // 出庫№
                basehtml = TorihikiUtils.format(basetpl, 'shukko', '{"shukko_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "SM": // 修理代見積書№
                basehtml = TorihikiUtils.format(basetpl, 'shuridaimitsumori', '{"shuridai_mitsumori_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "SO": // 送金№
                basehtml = TorihikiUtils.format(basetpl, 'sokin', '{"sokin_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "SS": // 最終処分№
                basehtml = TorihikiUtils.format(basetpl, 'saishushobun', '{"saishu_shobun_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "ST": // 商品手配№
                basehtml = TorihikiUtils.format(basetpl, 'shohintehai', '{"shohin_tehai_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "SY": // 請求予定明細№
                basehtml = TorihikiUtils.format(basetpl, 'seikyuyoteimeisai', '{"seikyu_yotei_meisai_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "TA": // 棚卸№
                basehtml = TorihikiUtils.format(basetpl, 'tanaoroshi', '{"tanaoroshi_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "TK": // 点検№
                basehtml = TorihikiUtils.format(basetpl, 'tenken', '{"tenken_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "TS": // 棚卸指示№
                basehtml = TorihikiUtils.format(basetpl, 'tanaoroshishiji', '{"tanaoroshi_shiji_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "TT": // 単価訂正伝票№
                basehtml = TorihikiUtils.format(basetpl, 'tankateiseidempyo', '{"tanka_teisei_dempyo_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
            case "WR": // Ｗレンタル終了№
                basehtml = TorihikiUtils.format(basetpl, 'wrentalshuryo', '{"wrental_shuryo_no":"' + denpyo_no + '"' + otherparam + '}', denpyo_no);
                break;
        }
        return basehtml;
    },
    /**
     * 伝票№区分取得.
     * @param {any} dempyo_no
     */
    getDenpyoNoKbn: function (dempyo_no) {
        var data = "";
        switch (dempyo_no.substring(0, 2).toUpperCase()) {
            //case 'MI': // 見積
            //  break;
            case 'JU': // 受注
                data = "1";
                break;
            //case 'HA': // 注文
            //case 'HB': // 注文
            //  break;
            //  break;
            case 'NH': // 納品
                data = "2";
                break;
            case 'HU': // 返却受付
            case 'HK': // 返却
                data = "3";
                break;
        }
        return data;
    },
    /**
   * ランダムＩＤ作成
   * */
    getRandomId: function (prefix) {
        //var timestamp1 = Date.parse(new Date());
        var timestamp1 = (new Date()).getTime();
        if (typeof prefix === 'undefined') {
            return timestamp1;
        }
        return prefix + "_" + timestamp1;
    },
    /**
     * 名前でデータを設定する。
     * @param {any} row
     * @param {any} container
     */
    setDataByName: function (row, container) {
        var $container = $(container);
        for (var key in row) {
            $container.find('[name$=".' + key + '"]').val(row[key]);
            $container.find('[name$=".' + key.toUpperCase() + '"]').val(row[key]);
        }
    },
    /**
     * データフィールドでテキストを設定する。
     * @param {any} row
     * @param {any} container
     */
    setTextByDataColumn: function (row, container) {
        var $container = $(container);
        for (var key in row) {
            $container.find('[data-column="' + key + '"]').text(row[key]);
            $container.find('[data-column="' + key.toUpperCase() + '"]').text(row[key]);
        }
    },
    /**
     * データフィールドでデータを設定する。
     * @param {any} row
     * @param {any} container
     */
    setDataByDataColumn: function (row, container) {
        var $container = $(container);
        for (var key in row) {
            $container.find('[data-column="' + key + '"]').val(row[key]);
            $container.find('[data-column="' + key.toUpperCase() + '"]').val(row[key]);
        }
    },
    /**
    * イメージ選択し、画面に表示する
    * @param {any} options
    */
    imageFileVisible: function (options) {
        // デフォルト設定
        var defaults = {
            //イメージのコンテナー
            wrapSelector: null,
            //<input type=file />
            fileSelector: null,
            width: '100%',
            height: 'auto',
            errorMessage: "イメージじゃない"
        };
        // Extend our default options with those provided.    
        var opts = $.extend(defaults, options);
        $(opts.fileSelector).on("change", function () {
            var file = this.files[0];
            var imageType = /image.*/;
            if (file.type.match(imageType)) {
                var reader = new FileReader();
                reader.onload = function () {
                    $(opts.wrapSelector).empty();
                    var img = new Image();
                    img.src = reader.result;
                    $(img).attr('realwidth', img.width);
                    $(img).attr('realheight', img.height);
                    $(img).attr('filename', $(opts.fileSelector).val());
                    $(img).width(opts.width);
                    $(img).height(opts.height);
                    $(img).click(function () { openImageView(this); });
                    $(opts.wrapSelector).append(img);
                };
                reader.readAsDataURL(file);
            } else {
                alert(opts.errorMessage);
            }
        });
    },
    /**
     * サブ画面リクエスト
     * @param {any} options
     */
    requestSubAction: function (options) {
        var defaults = { url: null, params: null, method: 'post', container: '#list' };
        defaults = $.extend(defaults, options);
        if (defaults.method == "post") {
            $.post(defaults.url,
                defaults.params,
                function (data, status) {
                    TorihikiUtils.hideLoading();
                    if (status) {
                        if (data.message != null) {
                            alert(data.message);
                            if (data.redirecturl != null) {
                                top.location.replace(data.redirecturl);
                            }
                            return;
                        }
                        var resultData = $.parseHTML($.trim(data), true);
                        $(defaults.container).html(resultData);
                        var form = $(defaults.container).parents('form');
                        ////$.validator.pro
                        //validator.resetForm();
                        var validator = $(form).validate();
                        validator.destroy();
                        $.validator.unobtrusive.parse(form[0]);

                        if (defaults.callback) {
                            defaults.callback();
                        }
                    }
                });
        } else {
            $.get(defaults.url,
                function (data, status) {
                    TorihikiUtils.hideLoading();
                    if (status) {
                        if (data.message != null) {
                            alert(data.message);
                            if (data.redirecturl != null) {
                                top.location.replace(data.redirecturl);
                            }
                            return;
                        }
                        var resultData = $.parseHTML($.trim(data), true);
                        $(defaults.container).html(resultData);
                        var form = $(defaults.container).parents('form');
                        ////$.validator.pro
                        //validator.resetForm();
                        var validator = $(form).validate();
                        validator.destroy();
                        $.validator.unobtrusive.parse(form[0]);

                        if (defaults.callback) {
                            defaults.callback();
                        }
                    }
                });
        }
    },
    /**
     * Ajaxカスタマイズ
     * @param {any} options
     */
    ajaxEx: function (options) {
        var noerror = false;
        if (typeof (options.noerror) != "undefined") {
            noerror = options.noerror;
        }

        var paramsData = options.data;
        if (typeof paramsData === 'function') {
            paramsData = options.data(options.dataArgs);
        }

        if (paramsData != undefined) {
            if (typeof paramsData.__RequestVerificationToken === 'undefined') {
                paramsData.__RequestVerificationToken = $(TorihikiGlobal.requestVerificationToken).last().val();
            }
        } else if (typeof options.type === 'undefined' || options.type == 'post') {
            paramsData = {};
            paramsData.__RequestVerificationToken = $(TorihikiGlobal.requestVerificationToken).last().val();
        }

        if (typeof options.keepLoading === 'undefined' || !options.keepLoading) {
            TorihikiUtils.showLoading();
        }

        $.ajax({
            url: TorihikiUtils.addPageSessionId(options.url),
            data: paramsData,
            cache: false,
            method: options.method ? options.method : 'POST',
            type: options.type ? options.type : 'post',
            async: typeof options.async === 'undefined' ? true : options.async,
            success: function (data) {
                if (typeof TorihikiUtils === 'undefined') {
                    return;
                }

                // メッセージがない時
                if ((data.message == "" || data.message == null) && data.redirecturl != undefined && data.redirecturl != "") {
                } else {
                    if (typeof options.keepLoading === 'undefined' || !options.keepLoading) {
                        TorihikiUtils.hideLoading();
                        TorihikiUtils.hideLoadingImage();
                    }
                }

                if (typeof (data.success) == "undefined") {
                    if (options.success) {
                        options.success(data);
                    }
                } else {
                    if (!data.success) {
                        //alert(data.message);
                        // Model.Valid結果
                        if (data.message == null) {
                            TorihikiMsg.alert(data);
                        }
                        else {
                            if (options.unShowMessage) {
                                console.log(data.message);
                            } else {
                                TorihikiMsg.alert({
                                    message: data.message,
                                    callback: options.failed ? options.failed : data.callback,
                                    callbackdata: data
                                });
                            }
                            if (data.redirecturl != null) {
                                top.location.replace(data.redirecturl);
                            }
                            return;
                        }
                    }
                    if (data.success) {
                        if (options.success) {
                            if (data.message != undefined && data.message != null && data.message != "") {
                                data.callback = options.success;
                                data.callbackdata = data;
                                // メッセージ表示。
                                TorihikiMsg.message(data);
                            } else {
                                options.success(data);
                            }
                        }
                        else {
                            // 遷移元へ遷移する時
                            if (data.callback == TorihikiGlobal.PageBack) {
                                data.callback = function () {
                                    TorihikiUtils.showLoading();
                                    var historyurl = TorihikiUtils.getHistoryPage();
                                    // 遷移元ＵＲＬが取得できない時
                                    if (historyurl == null || historyurl == undefined) {
                                        var url = data.url ? data.url : data.redirecturl;
                                        // 履歴から現在のＵＲＬを削除する。
                                        TorihikiUtils.removeUrlFromHistory(document.location.href);
                                        // 画面遷移.
                                        location.replace(url);
                                    } else {
                                        location.replace(TorihikiUtils.getHistoryPage());
                                    }
                                };
                            } else if (data.callback == TorihikiGlobal.PageReload) {
                                data.callback = function () {
                                    TorihikiUtils.showLoading();
                                    TorihikiUtils.reload();
                                };
                            } else {
                                data.callback = data.callback;
                                data.callbackdata = { success: data.success, data: data.data };
                                //data.callback = eval(data.callback);
                                //data.callbackdata = { success: data.success, data: data.data };
                            }

                            // メッセージがない時
                            if ((data.message == "" || data.message == null) && data.redirecturl != undefined && data.redirecturl != "") {
                                // ローディング表示
                                TorihikiUtils.showLoading();
                                // 画面遷移
                                location.replace(data.redirecturl);
                                return;
                            }

                            // メッセージ表示。
                            TorihikiMsg.message(data);

                            return;
                        }
                    }
                }
            },
            error: function (response, status, xhr) {
                TorihikiUtils.hideLoading();
                if (options.error) {
                    options.error(response);
                    return;
                }
                if (noerror) return;

                //if (TorihikiGlobal.isDebug) {
                //    // エラー情報表示
                //    TorihikiUtils.showErrors(response, status, xhr, options.url);
                //} else {
                //    TorihikiMsg.alert({ message: messages.ExceptionError });
                //}
                //var errmsg;
                //TorihikiUtils.hideLoading();
                //if (response.status == 404) {
                //  errmsg = response.status + " " + response.statusText;
                //} else if (response.status == 0) {
                //  errmsg = errorstatus0;
                //} else {
                //  errmsg = response.responseText;
                //}
                //TorihikiMsg.alert({ message: errmsg });
            },
            complete: function (data) {
                if (options.complete) {
                    options.complete(data);
                }
                data = null;
            }
        });
    },
    /**
     * Ajaxカスタマイズ with FileUpload
     * @param {any} options
     */
    ajaxFileEx: function (options) {
        var noerror = false;
        if (typeof (options.noerror) != "undefined") {
            noerror = options.noerror;
        }

        var paramsData = options.data;
        if (typeof paramsData === 'function') {
            paramsData = options.data(options.dataArgs);
        }

        if (paramsData != undefined) {
            if (typeof paramsData.__RequestVerificationToken === 'undefined') {
                paramsData.__RequestVerificationToken = $(TorihikiGlobal.requestVerificationToken).last().val();
            }
        } else if (typeof options.type === 'undefined' || options.type == 'post') {
            paramsData = {};
            paramsData.__RequestVerificationToken = $(TorihikiGlobal.requestVerificationToken).last().val();
        }

        if (typeof options.keepLoading === 'undefined' || !options.keepLoading) {
            TorihikiUtils.showLoading();
        }

        $.ajax({
            url: TorihikiUtils.addPageSessionId(options.url),
            data: paramsData,
            cache: false,
            method: options.method ? options.method : 'POST',
            type: options.type ? options.type : 'post',
            processData: false,
            contentType: false,
            async: typeof options.async === 'undefined' ? true : options.async,
            success: function (data) {
                if (typeof TorihikiUtils === 'undefined') {
                    return;
                }

                // メッセージがない時
                if ((data.message == "" || data.message == null) && data.redirecturl != undefined && data.redirecturl != "") {
                } else {
                    if (typeof options.keepLoading === 'undefined' || !options.keepLoading) {
                        TorihikiUtils.hideLoading();
                        TorihikiUtils.hideLoadingImage();
                    }
                }

                if (typeof (data.success) == "undefined") {
                    if (options.success) {
                        options.success(data);
                    }
                } else {
                    if (!data.success) {
                        //alert(data.message);
                        // Model.Valid結果
                        if (data.message == null) {
                            TorihikiMsg.alert(data);
                        }
                        else {
                            if (options.unShowMessage) {
                                console.log(data.message);
                            } else {
                                TorihikiMsg.alert({
                                    message: data.message,
                                    callback: options.failed ? options.failed : data.callback,
                                    callbackdata: data
                                });
                            }
                            if (data.redirecturl != null) {
                                top.location.replace(data.redirecturl);
                            }
                            return;
                        }
                    }
                    if (data.success) {
                        if (options.success) {
                            if (data.message != undefined && data.message != null && data.message != "") {
                                data.callback = options.success;
                                data.callbackdata = data;
                                // メッセージ表示。
                                TorihikiMsg.message(data);
                            } else {
                                options.success(data);
                            }
                        }
                        else {
                            // 遷移元へ遷移する時
                            if (data.callback == TorihikiGlobal.PageBack) {
                                data.callback = function () {
                                    TorihikiUtils.showLoading();
                                    var historyurl = TorihikiUtils.getHistoryPage();
                                    // 遷移元ＵＲＬが取得できない時
                                    if (historyurl == null || historyurl == undefined) {
                                        var url = data.url ? data.url : data.redirecturl;
                                        // 履歴から現在のＵＲＬを削除する。
                                        TorihikiUtils.removeUrlFromHistory(document.location.href);
                                        // 画面遷移.
                                        location.replace(url);
                                    } else {
                                        location.replace(TorihikiUtils.getHistoryPage());
                                    }
                                };
                            } else if (data.callback == TorihikiGlobal.PageReload) {
                                data.callback = function () {
                                    TorihikiUtils.showLoading();
                                    TorihikiUtils.reload();
                                };
                            } else {
                                data.callback = data.callback;
                                data.callbackdata = { success: data.success, data: data.data };
                                //data.callback = eval(data.callback);
                                //data.callbackdata = { success: data.success, data: data.data };
                            }

                            // メッセージがない時
                            if ((data.message == "" || data.message == null) && data.redirecturl != undefined && data.redirecturl != "") {
                                // ローディング表示
                                TorihikiUtils.showLoading();
                                // 画面遷移
                                location.replace(data.redirecturl);
                                return;
                            }

                            // メッセージ表示。
                            TorihikiMsg.message(data);

                            return;
                        }
                    }
                }
            },
            error: function (response, status, xhr) {
                TorihikiUtils.hideLoading();
                if (options.error) {
                    options.error(response);
                    return;
                }
                if (noerror) return;

                //if (TorihikiGlobal.isDebug) {
                //    // エラー情報表示
                //    TorihikiUtils.showErrors(response, status, xhr, options.url);
                //} else {
                //    TorihikiMsg.alert({ message: messages.ExceptionError });
                //}
                //var errmsg;
                //TorihikiUtils.hideLoading();
                //if (response.status == 404) {
                //  errmsg = response.status + " " + response.statusText;
                //} else if (response.status == 0) {
                //  errmsg = errorstatus0;
                //} else {
                //  errmsg = response.responseText;
                //}
                //TorihikiMsg.alert({ message: errmsg });
            },
            complete: function (data) {
                if (options.complete) {
                    options.complete(data);
                }
                data = null;
            }
        });
    },
    /**
     * object判断
     * @param {any} object
     */
    isObj: function (object) {
        return object && typeof (object) == 'object' && Object.prototype.toString.call(object).toLowerCase() == "[object object]";
    },
    /**
     * 配列判断
     * @param {any} object
     */
    isArray: function (object) {
        return object && typeof (object) == 'object' && object.constructor == Array;
    },
    /**
     * 数値チェック.
     * @param {any} input
     */
    isNumeric: function (input) {
        return (input - 0) == input && input.length > 0;
    },
    /**
     * 日付チェック.
     * @param {any} input
     */
    isDate: function (input) {
        return input.length == 10 && !/Invalid|NaN/.test(new Date(input).toString());
    },
    /**
    * 日付チェック.
    * @param {any} input
    */
    isDateSkipEmpty: function (input) {
        if (input.length == 0) return true;
        return input.length == 10 && !/Invalid|NaN/.test(new Date(input).toString());
    },
    /**
     * 時間チェック.
     * @param {any} input
     */
    isTime: function (input) {
        var dateTest = new RegExp(/^(?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]$/);

        return dateTest.test(input);
    },
    /**
     * ブーリアンチェック
     * @param {any} input
     */
    isBool: function (input) {
        return input === true || input === false || input === "true" || input === "false";
    },
    isNullOrEmpty: function (input) {
        if (input == null) return true;
        if (input == undefined) return true;
        if (input == '') return true;
    },
    isNullOrEmptyOrWhiteSpace: function (input) {
        if (input == null) return true;
        if (input == undefined) return true;
        if (input == '') return true;
        if (input.toString().trim() == '') return true;
    },
    toEmpty: function (input) {
        if (input == null) return '';
        if (input == undefined) return '';
        return input;
    },
    /**
     * オブジェクト要素数取得する
     * @param {any} object
     */
    getObjectLength: function (object) {
        var self = this;
        var count = 0;
        for (var i in object) {
            if (self.isUnCheckKey(i)) {
                continue;
            }
            count++;
        }
        return count;
    },
    /**
     * Jsonデータ比較
     * @param {any} objA
     * @param {any} objB
     */
    jsonCompare: function (objA, objB) {
        var self = this;
        if (!self.isObj(objA) || !self.isObj(objB)) return false;
        if (self.getObjectLength(objA) != self.getObjectLength(objB)) return false;
        return self.jsonCompareObj(objA, objB, true);
    },
    /**
     * Jsonデータのオブジェクト比較
     * @param {any} objA
     * @param {any} objB
     * @param {any} flag
     */
    jsonCompareObj: function (objA, objB, flag) {
        var self = this;
        for (var key in objA) {
            if (!flag)
                break;
            if (!objB.hasOwnProperty(key)) { flag = false; console.log('no-key:' + key); break; }
            if (!self.isArray(objA[key])) {
                if (objB[key] != objA[key]) { flag = false; console.log('not-equal:' + key + ', ' + objA[key] + ', ' + objB[key]); break; }
            } else {
                if (!self.isArray(objB[key])) { flag = false; console.log('not-array :' + key + ', ' + objA[key] + ', ' + objB[key]); break; }
                var oA = objA[key], oB = objB[key];
                if (oA.length != oB.length) { flag = false; console.log('length-not-equal :' + key + ', ' + objA[key] + ', ' + objB[key]); break; }
                for (var k in oA) {
                    if (!flag)
                        break;
                    flag = self.jsonCompareObj(oA[k], oB[k], flag);
                }
            }
        }
        return flag;
    },
    /**
     * 比較必要ではないキー
     * @param {any} key
     */
    isUnCheckKey: function (key) {
        if (key.indexOf('_fixed') > -1) {
            return true;
        }
        return false;
    },
    /**
     * 偽造防止フォーム フィールド追加
     * @param {any} data
     */
    appendTokenData: function (data) {
        if (data == undefined) {
            data = {};
        }
        data.__RequestVerificationToken = $(TorihikiGlobal.requestVerificationToken).last().val();
        return data;
    },
    /**
     * コールバック関数を実行する。
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
            return callbackfunc(callbackdata, exoptions);
        } else {
            if (TorihikiGlobal.isDebug) {
                TorihikiMsg.alert('設定された呼出方法が見ていません。')
            }
            TorihikiUtils.log(callback + ":undefined");
            TorihikiUtils.hideLoading();
        }
    },
    /**
     * 非同期コールバック関数を実行する。
     * @param {any} callback
     * @param {any} callbackdata
     */
    execCallbackAsync: function (callback, resolve, reject, callbackdata, exoptions) {
        if (typeof callback === 'undefined' || callback == null) return;
        var callbacksplit = callback.split('.');
        var callbackfunc = window;
        $(callbacksplit).each(function (i, cb) {
            callbackfunc = callbackfunc[cb];
        })

        if (typeof callbackfunc !== 'undefined') {
            return callbackfunc(resolve, reject, callbackdata, exoptions);
        } else {
            if (TorihikiGlobal.isDebug) {
                TorihikiMsg.alert('設定された呼出方法が見ていません。')
            }
            TorihikiUtils.log(callback + ":undefined");
            TorihikiUtils.hideLoading();
        }
    },
    /**
     * ポップアップコールバック関数を実行する。
     * @param {any} callback
     * @param {any} master
     * @param {any} data
     * @param {any} actionhandle
     * @param {any} targetContainer
     */
    execPopCallback: function (callback, master, data, actionhandle, targetContainer) {
        if (typeof callback === 'undefined' || callback == null) return;
        var callbacksplit = callback.split('.');
        var callbackfunc = window;
        $(callbacksplit).each(function (i, cb) {
            callbackfunc = callbackfunc[cb];
        })

        if (typeof callbackfunc !== 'undefined') {
            return callbackfunc(master, data, actionhandle, targetContainer);
        }
    },
    /**
     * 区分値名取得.
     * @param {any} kbnNm
     * @param {any} kbnchiCd
     */
    getKbnchiNm: function (kbnNm, kbnchiCd) {
        var kubun = $(TorihikiGlobal.Kubun).filter(function (i, kbn) {
            return kbnNm == kbn.kbn_nm && kbnchiCd == kbn.kbnchi_cd;
        })
        if (kubun.length > 0) {
            return kubun[0].kbnchi_nm;
        }
    },
    // =================================================================
    // =====================コントロール設定=============================
    // =================================================================
    dateIconFrex: '_date_icon',
    /**
     * フォーム設定
     * @param {any} form
     */
    setForm: function (form) {
        if (typeof form === 'undefined') form = $('form');
        if (form.length > 0) {
            if (form.attr('enterToTab') == 'False' || form.attr('enterToTab') == 'false') return;

            //form.enableEnterToTab({ captureTabKey: false });
            form.enableEnterToTab({ captureTabKey: true });
        }
    },
    /**
     * カレンダーアイコン
     * @param {any} container
    */
    dateIcon: function (element) {
        //var icon = '<a class="show-icon tcc-icn-btn"><i class="fas fa-calendar-check tcc-icn-btn"></i></a>';
        var icon = '<a href="#" class="show-icon tcc-icn-btn" tabindex="{0}" subtabindex="5"><i class="fas fa-calendar-check tcc-icn-btn"></i></a>';
        var id = $(element).attr('id') + this.dateIconFrex;
        //var item = $(TorihikiUtils.format(icon, id));
        var item = $(TorihikiUtils.format(icon, $(element).attr('tabindex')));
        $(item).data('source', element);
        $(element).data('date-icon', item);
        if (typeof $(element).attr('disabled') !== 'undefined') {
            $(item).hide();
        }
        return item;
    },
    /**
     * コントロール設定
     * @param {any} container
    */
    setControls: function (container) {

        var content;
        if (container) {
            content = $(container);
        } else {
            content = $(document);
        }

        // 日付の次、前ボタン
        var setNextPrevButton = function setNextPrevButton(t) {
            if ($(t).hasClass("show-arrow")) {
                if (!$(t).next().hasClass("tcc-date-next-link")) {
                    var nextarrow = '<a href="#" class="tcc-date-next-link" tabindex="{0}" subtabindex="3"><i class="fas fa-caret-right tcc-date-next tcc-icn-btn"></i></a>';
                    var nextarrowForTabindex = $(TorihikiUtils.format(nextarrow, $(t).attr('tabindex')));
                    nextarrowForTabindex.data('source', t);
                    $(t).data('next-icon', nextarrowForTabindex);
                    $(t).after(nextarrowForTabindex);
                    if (typeof $(t).attr('disabled') !== 'undefined') {
                        $(nextarrowForTabindex).hide();
                    }
                }
                if (!$(t).before().hasClass("tcc-date-prev-link")) {
                    var prevnarrow = '<a href="#" class="tcc-date-prev-link" tabindex="{0}" subtabindex="1"><i class="fas fa-caret-left tcc-date-prev tcc-icn-btn-l"></i></a>';
                    var prevnarrowForTabindex = $(TorihikiUtils.format(prevnarrow, $(t).attr('tabindex')));
                    prevnarrowForTabindex.data('source', t);
                    $(t).data('prev-icon', prevnarrowForTabindex);
                    $(t).before(prevnarrowForTabindex);
                    if (typeof $(t).attr('disabled') !== 'undefined') {
                        $(prevnarrowForTabindex).hide();
                    }
                }
            }
        }
        // 日付コントロール設定
        if (content.find('.tcc-input-date:not(.tcc-display-control)').length > 0) {
            content.find('.tcc-input-date:not(.tcc-display-control):not([readonly])').each(function (i, t) {
                $(t).attr('subtabindex', '2');
                if ($(t).hasClass("show-icon")) {
                    if ($(t).next().length == 0 || $(t).next()[0].tagName != 'A') {
                        $(t).after(TorihikiUtils.dateIcon(t));
                    }
                }
            });

            content.find('.tcc-input-date:not(.tcc-display-control):not([readonly])').each(function (i, t) {
                // 次、後ボタン
                setNextPrevButton(t);
            });

            content.find('.tcc-input-date:not(.tcc-display-control):not(.show-week,[readonly])').each(function () {
                $(this).bsdatepicker({
                    language: "ja-JP",
                    autoclose: true,
                    todayHighlight: true,
                    forceParse: true,
                    showOnFocus: !$(this).hasClass('show-icon'),
                });
            });

            //content.find('.tcc-input-full-date:not(.tcc-display-control):not(.show-week,[readonly])').bsdatepicker({
            //  language: "ja-JP",
            //  autoclose: true,
            //  todayHighlight: true,
            //  forceParse: true
            //});

            //content.find('.tcc-input-date:not(.tcc-display-control):not(.show-week,[readonly])').bsdatepicker({
            //  language: "ja-JP",
            //  autoclose: true,
            //  todayHighlight: true,
            //  forceParse: true
            //});

            content.find('.tcc-input-date-md:not(.tcc-display-control):not(.show-week,[readonly])').bsdatepicker({
                language: "ja-JP",
                autoclose: true,
                todayHighlight: true,
                forceParse: true,
                format: "mm/dd"
            });

            content.find('.tcc-input-date.show-week:not(.tcc-display-control):not([readonly])').each(function () {
                $(this).bsdatepicker({
                    language: "ja-JP",
                    autoclose: true,
                    todayHighlight: true,
                    forceParse: true,
                    showWeek: true,
                    showOnFocus: !$(this).hasClass('show-icon'),
                });
            });
        }

        // 年月コントロール設定
        if (content.find('.tcc-input-ym:not(.tcc-display-control):not([readonly])').length > 0) {
            content.find('.tcc-input-ym:not(.tcc-display-control):not([readonly])').each(function (i, t) {
                $(t).attr('subtabindex', '2');
                if ($(t).hasClass("show-icon")) {
                    if ($(t).next().length == 0 || $(t).next()[0].tagName != 'A') {
                        $(t).after(TorihikiUtils.dateIcon(t));
                    }
                }
            });

            content.find('.tcc-input-ym:not(.tcc-display-control):not([readonly])').each(function (i, t) {
                // 次、後ボタン
                setNextPrevButton(t);
            });

            content.find('.tcc-input-ym:not(.tcc-display-control):not([readonly])').each(function () {
                $(this).bsdatepicker({
                    language: "ja-JP",
                    autoclose: true,
                    todayHighlight: true,
                    forceParse: true,
                    minViewMode: 'months',
                    format: "yyyy/mm",
                    showOnFocus: !$(this).hasClass('show-icon'),
                });
            });
        }

        // 年コントロール設定
        if (content.find('.tcc-input-year:not(.tcc-display-control):not([readonly])').length > 0) {
            content.find('.tcc-input-year:not(.tcc-display-control):not([readonly])').each(function (i, t) {
                $(t).attr('subtabindex', '2');
                if ($(t).hasClass("show-icon")) {
                    if ($(t).next().length == 0 || $(t).next()[0].tagName != 'A') {
                        $(t).after(TorihikiUtils.dateIcon(t));
                    }
                }
            });

            content.find('.tcc-input-year:not(.tcc-display-control):not([readonly])').each(function (i, t) {
                // 次、後ボタン
                setNextPrevButton(t);
            });

            content.find('.tcc-input-year:not(.tcc-display-control):not([readonly])').each(function () {
                $(this).bsdatepicker({
                    language: "ja-JP",
                    autoclose: true,
                    todayHighlight: true,
                    forceParse: true,
                    minViewMode: 'years',
                    format: "yyyy",
                    showOnFocus: !$(this).hasClass('show-icon'),
                });
            });
        }

        // 月コントロール設定
        if (content.find('.tcc-input-month:not(.tcc-display-control):not([readonly])').length > 0) {
            content.find('.tcc-input-month:not(.tcc-display-control):not([readonly])').each(function (i, t) {
                $(t).attr('subtabindex', '2');
                if ($(t).hasClass("show-icon")) {
                    if ($(t).next().length == 0 || $(t).next()[0].tagName != 'A') {
                        $(t).after(TorihikiUtils.dateIcon(t));
                    }
                }
            });

            content.find('.tcc-input-month:not(.tcc-display-control):not([readonly])').each(function (i, t) {
                // 次、後ボタン
                setNextPrevButton(t);
            });

            content.find('.tcc-input-month:not(.tcc-display-control):not([readonly])').each(function () {
                $(this).bsdatepicker({
                    language: "ja-JP",
                    autoclose: true,
                    todayHighlight: true,
                    forceParse: true,
                    minViewMode: 'months',
                    format: "mm",
                    hideHeader: true,
                    showOnFocus: !$(this).hasClass('show-icon'),
                });
            });
        }

        // 日付ボタン設定
        content.find('a.show-icon').click(function () {
            var source = $(this).data('source');
            // コントロールが使用不可の時
            if ($(source).data('disabled')) return;

            if (!$(this).prev().hasClass("tcc-date-next-link")) {
                $(this).prev().bsdatepicker("show");
            } else {
                $(this).prev().prev().bsdatepicker("show");
            }
        });

        // 日付ボタン設定
        content.find('.show-arrow').each(function (i, t) {
            var prev = $(this).prev();
            var next = $(this).next();
            var datectl = $(this);
            var interval = 'd';

            var ChangeDate = function ChangeDate(interval, number, _inputdate) {
                var inputdate = new Date();
                if (_inputdate != "") {
                    switch (interval) {
                        case 'm':
                            _inputdate = _inputdate + "/01";
                            break;
                    }
                    inputdate = new Date(_inputdate);
                }

                var newdate = TorihikiUtils.dateAdd(interval, number, inputdate);
                return newdate;
                //return TorihikiUtils.formatDate(newdate, format);
            }

            if ($(this).hasClass('tcc-input-ym')) {
                interval = 'm';
                //format='yyyy/mm';
            }

            prev.click(function () {

                var source = $(this).data('source');
                // コントロールが使用不可の時
                if ($(source).data('disabled')) return;

                datectl.bsdatepicker('update');
                datectl.addClass('input-validation-valid').removeClass('input-validation-error');
                datectl.bsdatepicker('setDate', ChangeDate(interval, -1, datectl.val()));
            });
            next.click(function () {
                var source = $(this).data('source');
                // コントロールが使用不可の時
                if ($(source).data('disabled')) return;

                datectl.bsdatepicker('update');
                datectl.addClass('input-validation-valid').removeClass('input-validation-error');
                datectl.bsdatepicker('setDate', ChangeDate(interval, 1, datectl.val()));
            });
        });

        // 時間コントロール設定
        if (content.find('.tcc-input-time:not(.tcc-display-control)').length > 0) {
            content.find('.tcc-input-time:not(.tcc-display-control)').timepicker({
                minuteStep: 1,
                template: false,
                appendWidgetTo: 'body',
                showSeconds: false,
                showMeridian: false,
                defaultTime: false
            });
        }

        // コンボボックス
        if (content.find('select.tcc-input-combo:not(.tcc-display-control)').length > 0) {
            content.find('select.tcc-input-combo:not(.tcc-display-control)').each(function (i, ele) {
                if ($(this).parent().hasClass('tcc-input-combo')) {
                    return;
                }
                $(this).comboSelect();
            });
            //content.find('.tcc-input-combo').comboSelect();
            //content.find('.tcc-input-combo').select2({ matcher: TorihikiUtils.matchCustom });
        }
        // コンボボックス
        content.find('.tcc-input-combo-multi:not(.tcc-display-control)').each(function (i, t) {
            var maximumSelectionLength = $(this).attr("maximumSelectionLength");
            if (typeof maximumSelectionLength !== 'undefined') {
                $(this).select2({ language: "ja", matcher: TorihikiUtils.matchCustom, multiple: true, maximumSelectionLength: maximumSelectionLength });
            } else {
                $(this).select2({ language: "ja", matcher: TorihikiUtils.matchCustom, multiple: true, maximumSelectionLength: 100 });
            }
        });

        // 数値
        if (content.find('.tcc-input-number:not(.tcc-display-control)').length > 0) {

            // 数値コントロールフォーマット設定
            TorihikiUtils.setNumberFormatControls(content);

            content.find('.tcc-input-number:not(.tcc-display-control)').blur(function () {
                var oldValue = $(this).val();
                // ブランク時
                if ($(this).val() == "" && (!$(this).attr("blanktozero") || $(this).attr("blanktozero").toLowerCase() == false)) return;

                // 数値チェック
                if (!TorihikiUtils.isNumber($(this).val())) {
                    return;
                }
                var sourcedata = TorihikiUtils.CtoH($(this).val());
                var scale = $(this).attr("scale");
                if (!scale) {
                    scale = 0;
                    if (sourcedata.indexOf('.') > -1) {
                        scale = sourcedata.length - sourcedata.indexOf('.') - 1;
                    }
                }
                var data = $.number(sourcedata, scale);
                $(this).val(data);

                var form = $(this).closest('form');

                if (oldValue != sourcedata) {
                    $(this).trigger('change');
                    $(this).valid();
                } else {
                    if (form.length > 0) $(this).valid();
                }
            });

            content.find('.tcc-input-number:not(.tcc-display-control)').focus(function () {
                $(this).val($(this).val().replace(new RegExp(',', 'g'), ''));
            });
        }

        //if (content.find('.tcc-input-tel:not(.tcc-display-control)').length > 0) {
        //  content.find('.tcc-input-tel:not(.tcc-display-control)').tel();
        //}

        // ラベルクリック
        content.find('.tcc-click-to-checked').click(function () {
            var ctlname = $(this).attr("tcc-tcc-input-name");

            if ($(this).prev()[0].disabled) return;

            if (content.find("[name='" + ctlname + "']")[0].type == "radio") {
                content.find("[name='" + ctlname + "']").removeAttr("checked");
                $(this).prev()[0].checked = true;
            } else if (content.find("[name='" + ctlname + "']")[0].type == "checkbox") {
                var ctl = $(this).prev();
                if (ctl[0].checked) {
                    ctl[0].checked = false;
                } else {
                    ctl[0].checked = true;
                }
            }

        });

        // 自動フリガナ
        content.find('[autokana]').each(function () {
            var kana_ctl = $(this).attr('autokana');
            $.fn.autoKana2($(this), '#' + kana_ctl, { katakana: true });
            // ひらがなを自動変換するイベントの呼び出し
            //TorihikiUtils.kanaChange($(this));
        });

        // チェックボックスリストの「全て」選択処理
        content.find('.tcc-checkbox-list-all').off('change').on('change', function (event) {
            // コンテナ取得
            var container = $(this).closest('div');
            // 選択状態取得
            var curvalue = $(this).prop('checked');
            // 結果項目取得する
            var result = container.find('.tcc-checkbox-list-result');
            // アイテムを全て選択状態で設定する。
            container.find('.tcc-checkbox-list-item').prop('checked', curvalue);

            // 選択値設定
            var resultdata = [];
            container.find('.tcc-checkbox-list-item:checked').each(function (i, t) {
                resultdata.push($(t).attr('value'));
            });
            result.val(resultdata.join(','));
            result.valid();
            result.trigger('change');
        });

        // チェックボックスリストのアイテム選択処理
        content.find('.tcc-checkbox-list-item').off('change').on('change', function (event) {
            // コンテナ取得
            var container = $(this).closest('div');
            // 選択状態取得
            var curvalue = $(this).prop('checked');
            // 結果項目取得する
            var result = container.find('.tcc-checkbox-list-result');

            // 「全て」が存在する
            if (container.has('.tcc-checkbox-list-all')) {
                // 「全て」項目
                var allitem = container.find('.tcc-checkbox-list-all');
                // アイテムが選択されない時
                if (container.find('.tcc-checkbox-list-item:checked').length == 0) {
                    allitem.prop('checked', false);
                    result.val('');　// 全て選択を選択なしで処理
                    result.valid();
                    result.trigger('change');
                    return;
                }
                // 選択岐は全部データの場合が存在可能ので、コメントアウト
                //// アイテムが全部選択される時
                //if (container.find('.tcc-checkbox-list-item').length == container.find('.tcc-checkbox-list-item:checked').length) {
                //  allitem.prop('checked', true);
                //  result.val('');　// 全て選択を選択なしで処理
                //  result.valid();
                //  return;
                //}
                // 全部選択されない時
                allitem.prop('checked', false);
            }

            var resultdata = [];
            container.find('.tcc-checkbox-list-item:checked').each(function (i, t) {
                resultdata.push($(t).attr('value'));
            });
            result.val(resultdata.join(','));
            result.valid();
            result.trigger('change');

            //console.log($(this).prop('checked'));
        });

        // 全角数字を半角数字に変換
        content.find('input').each(function () {
            if ($(this).hasClass('tcc-input-number') ||
                $(this).hasClass('tcc-input-full-date') ||
                $(this).hasClass('tcc-input-date') ||
                $(this).hasClass('tcc-input-ym') ||
                $(this).hasClass('tcc-input-time') ||
                $(this).hasClass('tcc-input-tel') ||
                $(this).hasClass('tcc-phone') ||
                $(this).hasClass('tcc-warekidate') ||
                $(this).hasClass('tcc-zip')) {
                $(this).bind("blur", function (e) {
                    //$(this).val(TorihikiUtils.toCDBFull($(this).val()));
                    $(this).val(TorihikiUtils.toHalfNum($(this).val()));
                });
            }
        })

        // 電話番号
        content.find('.tcc-phone').bind("input propertychange change", function (event) {
            //$(this).val(TorihikiUtils.toCDBFull($(this).val()));
            var input = $(this).parent().children("input.tcc-phone");
            var source = $(this).parent().children(".tcc-phone-source");
            if ($(input).eq(0).val() != "" || $(input).eq(1).val() != "" || $(input).eq(2).val() != "") {
                source.val(TorihikiUtils.toCDBFull($(input).eq(0).val()) + "-" + TorihikiUtils.toCDBFull($(input).eq(1).val()) + "-" + TorihikiUtils.toCDBFull($(input).eq(2).val()));
            } else {
                source.val('');
            }
            source.valid();
        });

        // 電話番号連動
        content.find('.tcc-phone-source').each(function (i, t) {
            var phone1 = $(t).parent().find('.tcc-phone-part1');
            $(t).data('part1', phone1);
            var phone2 = $(t).parent().find('.tcc-phone-part2');
            $(t).data('part2', phone2);
            var phone3 = $(t).parent().find('.tcc-phone-part3');
            $(t).data('part3', phone3);

            // ポップアップ戻る値対応
            $(t).bind("input propertychange change", function (event) {

                var phonedata = $(this).val().split('-');

                // クリア
                $($(this).data('part1')).val('');
                $($(this).data('part2')).val('');
                $($(this).data('part3')).val('');

                // データ設定
                if (phonedata.length > 0) {
                    $($(this).data('part1')).val(phonedata[0]);
                }
                if (phonedata.length > 1) {
                    $($(this).data('part2')).val(phonedata[1]);
                }
                if (phonedata.length > 2) {
                    $($(this).data('part3')).val(phonedata[2]);
                }
            });
        });

        // 和暦カレンダー
        content.find('input.tcc-warekidate,select.tcc-warekidate').bind("input propertychange change", function (event) {
            //$(this).val(TorihikiUtils.toCDBFull($(this).val()));
            var basecontainer = $(this).parent().parent();
            var input = basecontainer.find("input.tcc-warekidate");
            var selected = basecontainer.find("select.tcc-warekidate option:selected").data('tokens');
            var date1 = new Date(selected);
            var year = date1.getFullYear() + Number(TorihikiUtils.toCDBFull($(input).eq(0).val())) - 1;
            if ($(input).eq(0).val() == "") {
                year = NaN;
            }

            var source = basecontainer.find(".tcc-warekidate-source");
            if (selected != "" || $(input).eq(1).val() != "" || $(input).eq(2).val() != "") {
                source.val(year + "/" + TorihikiUtils.appendZero(TorihikiUtils.toCDBFull($(input).eq(1).val())) + "/" + TorihikiUtils.appendZero(TorihikiUtils.toCDBFull($(input).eq(2).val())));
            } else {
                source.val('');
            }
            source.valid();
        });

        // 和暦カレンダー
        content.find('input.tcc-warekidate').bind("blur", function (event) {
            $(this).val($(this).val().trim());
            if ($(this).val().length == 2 || $(this).val() == 0 || !TorihikiUtils.isNumber($(this).val())) {
                return;
            }

            $(this).val(TorihikiUtils.appendZero($(this).val())).trigger('change');
        });

        content.find('.tcc-warekidate-source').each(function (i, t) {
            var partcombo = $(t).parent().find('select.tcc-warekidate-combo');
            $(t).data('partcombo', partcombo);
            var part0 = $(t).parent().find('.combo-input');
            $(t).data('part0', part0);
            var part1 = $(t).parent().find('.tcc-warekidate-part1');
            $(t).data('part1', part1);
            var part2 = $(t).parent().find('.tcc-warekidate-part2');
            $(t).data('part2', part2);
            var part3 = $(t).parent().find('.tcc-warekidate-part3');
            $(t).data('part3', part3);
        });

        // 郵便番号
        content.find('.tcc-zip').bind("input propertychange change", function (event) {
            //$(this).val(TorihikiUtils.toCDBFull($(this).val()));
            var input = $(this).parent().children("input.tcc-zip");
            var source = $(this).parent().children(".tcc-zip-source");
            if ($(input).eq(0).val() != "" || $(input).eq(1).val() != "") {
                source.val(TorihikiUtils.toCDBFull($(input).eq(0).val()) + "-" + TorihikiUtils.toCDBFull($(input).eq(1).val()));
            } else {
                source.val('');
            }
            source.valid();
        });

        // 郵便番号連動
        content.find('.tcc-zip-source').each(function (i, t) {
            var zip1 = $(t).parent().find('.tcc-zip-part1');
            $(t).data('part1', zip1);
            var zip2 = $(t).parent().find('.tcc-zip-part2');
            $(t).data('part2', zip2);

            // ポップアップ戻る値対応
            $(t).bind("input propertychange change", function (event) {

                var zipdata = $(this).val().split('-');

                // クリア
                $($(this).data('part1')).val('');
                $($(this).data('part2')).val('');

                // データ設定
                if (zipdata.length > 0) {
                    $($(this).data('part1')).val(zipdata[0]);
                }
                if (zipdata.length > 1) {
                    $($(this).data('part2')).val(zipdata[1]);
                }
            });
        });

        /* 大文字は小文字に変換して数字のみ */
        //content.find('.tcc-input-number,.tcc-warekidate,.tcc-input-date,.tcc-phone,.tcc-zip,[data-val-allnumeric]').off('input').on('input', function (e) {
        //  //e.target.value = e.target.value
        //  //  .replace(/[！-～]/g, function (s) {
        //  //    return String.fromCharCode(s.charCodeAt(0) - 0xFEE0);
        //  //  })
        //  //  .replace(/[^0-9]/g, '');

        //  //// if (TorihikiUtils.browser.isEdge)
        //  //$(e.target).trigger('change');
        //});

        ///* 大文字は小文字に変換して数字のみ、千位にカンマ付与 */
        //$('.tcc-input-number').bind('input', function(e){

        //	var val = e.target.value
        //		.replace(/[！-～]/g, function(s) {
        //			return String.fromCharCode(s.charCodeAt(0) - 0xFEE0 );
        //      })
        //      .replace(/[^0-9]/g, '');
        //	    e.target.value = (isNaN(val) || !val) ? '' : parseInt(val).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        //  });

        // 選択できない設定
        content.find('[unselected="true"]').bind("contextmenu", function () { return false; });

        // 拡大。縮小ボタン
        var collapse = function (ctl) {
            // ローディング表示
            TorihikiUtils.showLoading();
            var target = ctl.data('target');
            var expandtarget = ctl.data('expand-target') + ":not(.fixed_header_display_none_at_print)";
            var targetHeight = ctl.data('target-height');
            var marginTop = ctl.data('expand-target-parent-margin-top') || 0;
            $(target).each(function (j, m) {
                //console.log($(m).outerHeight());
                $(m).data('old-height', $(m).outerHeight());
                $(m).data('old-overflowY', m.style.overflowY);
                var diffheight = $(m).outerHeight() - targetHeight;
                $(expandtarget).data('old-height', $(expandtarget).outerHeight());
                $(expandtarget).attr('data-old-height', $(expandtarget).outerHeight());
                if (marginTop != 0) {
                    var targetMarginBottom = $(m).css('margin-bottom');
                    if (targetMarginBottom != undefined) {
                        targetMarginBottom = parseInt(targetMarginBottom.replace('px', ''));
                    } else {
                        targetMarginBottom = 0
                    }
                    diffheight -= marginTop - targetMarginBottom;
                }

                // FixedMidashi
                if ($(expandtarget).parent()[0].style.position != 'relative') {
                    $(expandtarget).find('[_fixedhead]').each(function (i, tbl) {
                        if (typeof tbl.$FXH_FIXED_ELEMENT !== 'undefined') {
                            $(tbl.$FXH_FIXED_ELEMENT.parentNode).hide();
                        }
                    });
                }
                $(expandtarget).animate({ height: $(expandtarget).outerHeight() + diffheight }, 400, function () {
                    if (marginTop != 0) {
                        $(expandtarget).parent().css('margin-top', marginTop + 'px');
                    }
                });
                //$(expandtarget).height($(expandtarget).height() + diffheight);

                m.style.overflowY = 'auto';

                if ($(expandtarget).parent()[0].style.position != 'relative') {
                    $(m).animate({ height: targetHeight }, 200, function () {
                        $(target).addClass('tcc-collapse-width');
                        FixedMidashi.create();
                    });
                } else {
                    $(m).animate({ height: targetHeight }, 200);
                }
                //$(m).height(targetHeight);
                $(m).addClass('tcc-collapse');

                setTimeout(function () {
                    // 自動高さ設定属性があれば
                    if ($(expandtarget).hasClass('tcc-grid-auto-height')) {
                        TorihikiUtils.setGridAutoHeight($(expandtarget).parent())
                    }
                    // ローディング非表示
                    TorihikiUtils.hideLoading();
                }, 400);
            });
        };
        var expand = function (ctl) {
            // ローディング表示
            TorihikiUtils.showLoading();

            var target = ctl.data('target');
            var expandtarget = ctl.data('expand-target') + ":not(.fixed_header_display_none_at_print)";
            var targetHeight = ctl.data('target-height');
            var marginTop = ctl.data('expand-target-parent-margin-top') || 0;
            $(target).removeClass('tcc-collapse-width');
            $(target).each(function (j, m) {

                if (marginTop != 0) {
                    $(expandtarget).parent().css('margin-top', '0px');
                }
                $(expandtarget).animate({ height: $(expandtarget).attr('data-old-height') }, 200);
                //$(expandtarget).height($(expandtarget).attr('data-old-height'));
                $(expandtarget).removeAttr('data-old-height');

                m.style.overflowY = $(m).data('old-overflowY');

                // FixedMidashi
                if ($(expandtarget).parent()[0].style.position != 'relative') {
                    $(expandtarget).find('[_fixedhead]').each(function (i, tbl) {
                        if (typeof tbl.$FXH_FIXED_ELEMENT !== 'undefined') {
                            $(tbl.$FXH_FIXED_ELEMENT.parentNode).hide();
                        }
                    });
                }

                if ($(expandtarget).parent()[0].style.position != 'relative') {
                    $(m).animate({ height: $(m).data('old-height') }, 300, function () {
                        $(target).removeClass('tcc-collapse-width');
                        FixedMidashi.create();
                    });
                } else {
                    $(m).animate({ height: $(m).data('old-height') }, 300);
                }
                //$(m).height($(m).data('old-height'));
                $(m).removeData('old-height');
                $(m).removeData('old-overflowY');
                $(m).removeClass('tcc-collapse');

                setTimeout(function () {
                    // 自動高さ設定属性があれば
                    if ($(expandtarget).hasClass('tcc-grid-auto-height')) {
                        TorihikiUtils.setGridAutoHeight($(expandtarget).parent())
                    }
                    // ローディング非表示
                    TorihikiUtils.hideLoading();
                }, 400);
                //FixedMidashi.create();
            });
        };
        content.find('[tcc-toggle="tcc-collapse"]').each(function (i, t) {
            var ctl = $(t);
            var target = ctl.data('target');
            if ($(target).hasClass('tcc-collapse')) {
                collapse(ctl);
                ctl.html('<i class="fas fa-plus"></i>');
            } else {
                ctl.html('<i class="fas fa-minus"></i>');
            }
        });
        content.find('[tcc-toggle="tcc-collapse"]').off('click').on('click', function (e) {
            var ctl = $(this);
            var target = ctl.data('target');
            if ($(target).hasClass('tcc-collapse')) {
                expand(ctl);
                ctl.html('<i class="fas fa-minus"></i>');
            } else {
                collapse(ctl);
                ctl.html('<i class="fas fa-plus"></i>');
            }
        });

        // ロケーション先コントロール
        var setStatusOnocationKbnChange = function (div) {
            var $div = $(div);
            var kbn = $div.find('select.tcc-location-kbn').val();
            var classes = "nocheck hidden-fade,nocheck hidden-fade,nocheck hidden-fade".split(",");
            switch (kbn) {
                case '3':
                    classes[2] = '';
                    break;
                case '2':
                    classes[1] = '';
                    break;
                default:
                    classes[0] = '';
                    break;
            }

            var parts = {};
            parts.bumon = $div.find('div.tcc-location-bumon');
            parts.bumonddl = $div.find('select.tcc-location-bumon');
            parts.kokyaku_mei = $div.find('input.tcc-location-kokyaku-nm');
            parts.siiresaki_mei = $div.find('input.tcc-location-siiresaki-nm');

            parts.bumonddl.removeClass('nocheck').removeClass('hidden-fade');
            parts.bumon.removeClass('nocheck').removeClass('hidden-fade');
            parts.kokyaku_mei.removeClass('nocheck').removeClass('hidden-fade');
            parts.siiresaki_mei.removeClass('nocheck').removeClass('hidden-fade');

            parts.bumon.addClass(classes[0]);
            parts.bumonddl.addClass(classes[0])
            parts.kokyaku_mei.addClass(classes[1]);
            parts.siiresaki_mei.addClass(classes[2]);

            switch (kbn) {
                case '3':
                    parts.kokyaku_mei.next().hide().next().hide();
                    parts.siiresaki_mei.next().show().next().show();
                    break;
                case '2':
                    parts.kokyaku_mei.next().show().next().show();
                    parts.siiresaki_mei.next().hide().next().hide();
                    break;
                default:
                    parts.kokyaku_mei.next().hide().next().hide();
                    parts.siiresaki_mei.next().hide().next().hide();
                    break;
            }
        }
        content.find('.tcc-location').each(function (i, div) {
            setStatusOnocationKbnChange(div);
            // 変更イベント      
            $(div).find('select.tcc-location-kbn').change(function () {
                var div = $(this).parents('.tcc-location');
                setStatusOnocationKbnChange(div);

                var form = $(this).closest('form');
                $(form).data("unobtrusiveValidation", null);
                $(form).data("validator", null);
                $.validator.unobtrusive.parse($(form));
            })

            $(div).find('select.tcc-location-bumon').change(function () {
                $(div).find('.tcc-location-code').val($(this).val()).valid();
            })
        });

        // ボタンアイコン設定
        TorihikiUtils.setButtonIcon(content);

        // ツールチップ設定(三点リーダー)
        TorihikiUtils.setNoWijmoToolTips(content);

    },
    /**
     * 数値コントロールフォーマット設定
     * @param {any} container
    */
    setNumberFormatControls: function (container) {
        var content;
        if (container) {
            content = $(container);
        } else {
            content = $(document);
        }
        content.find('.tcc-input-number:not(.tcc-display-control)').each(function () {
            if ($(this).val() == "") {
                return;
            }
            var sourcedata = TorihikiUtils.CtoH($(this).val());
            var scale = $(this).attr("scale");
            if (!scale) {
                scale = 0;
                if (sourcedata.indexOf('.') > -1) {
                    scale = sourcedata.length - sourcedata.indexOf('.') - 1;
                }
            }
            $(this).val($.number($(this).val(), scale));
        });
    },
    /**
     * コントロールの値をかなに変換する。
     * @param {any} ele
     */
    kanaChange: function (ele) {
        var val = ele.val();
        // 正規表現でひらがなを引数の値に置換
        var kana = val.replace(/[ぁ-ん]/g, function (s) {
            // ユニコード値でカナに変換
            return String.fromCharCode(s.charCodeAt(0) + 0x60);
        });

        if (val.match(/[ぁ-ん]/g)) {
            $(ele).val(kana);
        }
    },
    /**
     * 利用不可切り替え.
     * @param {any} disabled
     * @param {any} element
     */
    disabled: function (element, disabled) {
        if (disabled == undefined) disabled = true;
        
        var $element = $(element);
        // 項目存在しない時
        if ($element.length == 0) return;
        // 複数コントロール時
        var isCheckOrRadio = $element.filter(function () {
            return this.type == 'checkbox' || this.type == 'radio';
        }).length > 0;

        if ($element.length > 1 && !isCheckOrRadio) {
            $element.filter(function () {
                switch (this.tagName.toLowerCase()) {
                    case "input":
                    case "select":
                    case "textarea":
                        return true
                    default:
                        return false;
                }
            }).each(function () {
                TorihikiUtils.disabled(this, disabled);
            })
            return;
        }
        // 非表示コントロール時、処理しない
        if ($element[0].type != 'hidden') {
            $element.data('disabled', disabled);
            $element.prop('disabled', disabled);
        }

        // 日付
        if ($element.hasClass('tcc-input-date') || $element.hasClass('tcc-input-ym') || $element.hasClass('tcc-input-month') || $element.hasClass('tcc-input-year')) {
            // 前後ボタン
            if ($element.hasClass('show-arrow')) {
                var prev = $element.prev();
                var next = $element.next();

                if (disabled) {
                    prev.hide();
                    next.hide();
                } else {
                    prev.show();
                    next.show();
                }
            }

            if (disabled) {
                TorihikiUtils.addHiddenItem(element);
                if ($element.data('date-icon')) $element.data('date-icon').hide();
            } else {
                TorihikiUtils.removeHiddenItem(element);
                $element.prop('disabled', false);
                if ($element.data('date-icon')) $element.data('date-icon').show();
            }
            return;
        }

        // ポップアップ検索
        if ($element.attr('popsearch') && $element.attr('popsearch').toLowerCase() == 'true') {
            if (disabled) {
                if ($element[0].type != 'hidden') {
                    TorihikiUtils.addHiddenItem(element);
                }
                if ($element.data('search-icon')) $element.data('search-icon').hide();
                if ($element.data('clear-icon')) $element.data('clear-icon').hide();
            } else {
                TorihikiUtils.removeHiddenItem(element);
                if ($element.data('search-icon')) $element.data('search-icon').show();
                if ($element.data('clear-icon')) $element.data('clear-icon').show();
            }
            return;
        }

        // コンボボックス
        if ($element.hasClass('tcc-input-combo')) {
            $element.comboSelect('dispose');
            if (disabled) {
                TorihikiUtils.addHiddenItem(element);
            } else {
                TorihikiUtils.removeHiddenItem(element);
            }
            $element.comboSelect();
            return;
        }

        // チェックボックス、ラジオボタン
        if (isCheckOrRadio) {
            var checkOrRadioElements = $element.filter(function () {
                return this.type == 'checkbox' || this.type == 'radio';
            })
            checkOrRadioElements.each(function (i, t) {
                let id = $(this).attr('id');
                let name = $(this).attr('name');
                // 単一アイテム
                if ($(this).parent().find('input[name="' + name + '"]:not([type=hidden])').length == 1) {
                    if (disabled) {
                        $(this).parent().attr('disabled', 'true');
                    } else {
                        $(this).parent().removeAttr('disabled');
                    }
                } else {
                    if (typeof id !== 'undefined') {
                        let label = $(this).parent().find('label[for="' + id + '"]');
                        if (disabled) {
                            label.attr('disabled', 'true');
                        } else {
                            label.removeAttr('disabled');
                        }
                    }
                    // すべてが設定された場合
                    if ($(this).parent().find('input[name="' + name + '"]:disabled:not([type=hidden]').length == $(this).parent().find('input[name="' + name + '"]:not([type=hidden]').length) {
                        $(this).parent().attr('disabled', 'true');
                    } else {
                        $(this).parent().removeAttr('disabled');
                    }
                }
            })

            var value = '';
            var checkeVals = checkOrRadioElements.filter(function () {
                return $(this).prop('checked'); //  || $(this).attr('checked')
            }).map(function () {
                return $(this).val();
            });
            if (checkeVals.length > 0) {
                value = checkeVals.toArray().toString();
            }

            //if ($(element).prop('checked') || $(element).attr('checked')) {
            //}
            if (disabled) {
                TorihikiUtils.addHiddenItem(element, value);
            } else {
                TorihikiUtils.removeHiddenItem(element);
            }

        } else {
            if (disabled) {
                if ($element[0].type != 'hidden') {
                    TorihikiUtils.addHiddenItem(element);
                }
            } else {
                TorihikiUtils.removeHiddenItem(element);
            }
            return;
        }
    },
    /**
   * 利用不可切り替え.
   * @param {any} container
   * @param {any} disabled
   */
    disabledAll: function (container, disabled) {
        TorihikiUtils.showLoading();
        $(container).find('input,select,textarea').each(function (i, t) {
            // 複数コントロール時
            var isCheckOrRadio = this.type == 'checkbox' || this.type == 'radio';
            if (isCheckOrRadio && t.name) {
                TorihikiUtils.disabled('[name="' + t.name + '"]', disabled);
            } else {
                TorihikiUtils.disabled(t, disabled);
            }
        });
        TorihikiUtils.hideLoading();
    },
    /**
     * 利用可能切り替え.
     * @param {any} disabled
     * @param {any} element
     */
    enabled: function (element, enabled) {
        if (enabled == undefined) enabled = true;
        TorihikiUtils.disabled(element, !enabled);
    },
    /**
   * 利用可能切り替え.
   * @param {any} container
   * @param {any} disabled
   */
    enabledAll: function (container, enabled) {
        $(container).find('input,select,textarea').each(function (i, t) {
            TorihikiUtils.enabled(t, enabled);
        });
        TorihikiUtils.hideLoading();
    },
    /**
   * コントロールの後に非表示コントロールを追加する.
   * @param {any} element
   */
    addHiddenItem: function (element, value) {
        if ($(element).length == 0) return;
        if ($(element).hasClass('tcc-zip-source')) return;
        if ($(element).hasClass('tcc-phone-source')) return;
        if ($(element).hasClass('tcc-warekidate-source')) return;

        var act_ctl = $(element)[0];

        var hiddenctlid = $(element).attr('id') + '_disabled_id';
        $('#' + hiddenctlid).remove();
        var hiddenhtml = '<input type=hidden class="tcc-hidden-for-disabled" name={0} id={1} value="{2}">';
        var eleValue = $(element).val() == null ? '' : $(element).val();
        if (typeof value !== 'undefined') {
            eleValue = value;
        }
        switch (act_ctl.tagName.toLowerCase()) {
            case 'input':
                var hiddenctl = $(TorihikiUtils.format(hiddenhtml, $(element).attr('name'), $(element).attr('id'), eleValue));
                break;;
            case 'select':
                var hiddenctl = $(TorihikiUtils.format(hiddenhtml, $(element).attr('name'), $(element).attr('id'), eleValue));
                break;;
            case 'textarea':
                //var hiddenctl = $(act_ctl.outerHTML);
                var hiddenctl = $(TorihikiUtils.format(hiddenhtml, $(element).attr('name'), $(element).attr('id'), eleValue));
                break;;
            default:
                return;
        }

        hiddenctl.attr('id', hiddenctlid);
        hiddenctl.attr('type', 'hidden');

        // 数値コントロール
        if ($(element).hasClass('tcc-input-number')) {
            hiddenctl.addClass('tcc-input-number');
        }

        // コントロールリスト最後に非表示コントロール追加。
        $(element).last().after(hiddenctl);
    },
    /**
  * コントロールの後の非表示コントロールを削除する.
  * @param {any} element
  */
    removeHiddenItem: function (element) {
        if ($(element).length == 0) return;
        if ($(element).hasClass('tcc-zip-source')) return;
        var $actCtl = $(element);
        // ラジオボタン時
        if ($(element).is('div') && $(element).hasClass('tcc-radio-list')) {
            $actCtl = $(element).find('input').first();
        }
        var hiddenctlid = $actCtl.attr('id') + '_disabled_id';
        $('#' + hiddenctlid).remove();
    },
    /**
     * コンボボックスデータリフレッシュ.
     * @param {any} master
     * @param {any} params
     */
    refreshComboData: function (options) {
        if (typeof options.url !== 'undefined') {
            // パラメータ
            var query = options.params;
            // RequestVerificationToken
            if (typeof query.__RequestVerificationToken === 'undefined') {
                query.__RequestVerificationToken = $(TorihikiGlobal.requestVerificationToken).last().val();
            }
            // リフレッシュ
            $(options.element).comboSelect(
                {
                    url: TorihikiUtils.addPageSessionId(options.url),
                    queryparams: query,
                    callback: options.callback
                }
            );

        } else {
            // リフレッシュ
            $(options.element).comboSelect(
                {
                    url: TorihikiUtils.addPageSessionId(ComboUpdateDataUrl),
                    queryparams: {
                        master: options.master,
                        condtionkey: JSON.stringify(options.params),
                        __RequestVerificationToken: $(TorihikiGlobal.requestVerificationToken).last().val(),
                    },
                    callback: options.callback
                }
            );
        }
    },
    /** 画面のコントロールのマスタキー設定チェック */
    checkMasterKeys: function () {
        $('.popsearch').each(function (i, t) {
            var page_layout = popSearchPageSetting[$(t).attr('master')];
            if (typeof page_layout === 'undefined' && $(t).attr('master') != 'dummy') {
                $(t).addClass('tcc-error-master');
            }
        });
        $('.popdetail-link').each(function (i, t) {
            var page_layout = popDetailPageSetting[$(t).attr('master')];
            if (typeof page_layout === 'undefined' && $(t).attr('master') != 'dummy') {
                $(t).addClass('tcc-error-master');
            }
        });
    },
    /**一番目コントロール（タブインデックス最小）フォーカス. */
    focusFirstElement: function (form) {
        if (typeof form === 'undefined') form = $('form');
        if (form.length > 0) {
            var $pool = form.getFocusCandidates(true);
            //$pool.first().focus().select();
            form.setFirstElement($pool);
        }
    },
    /**
     * データ処理、画面のコントロールに値を設定する。
     * @param {any} key
     */
    setContainerByData: function (container, data) {
        var $container = $(container);
        
        $.each(data, function (n, o) {

            if (n == "__RequestVerificationToken") return;

            var obj = $container.find("[name='" + n + "']");

            if (obj.length > 0 && (obj[0].type == 'radio' || obj[0].type == 'checkbox')) {

                obj.removeAttr('checked');

                switch (obj[0].type) {
                    case 'radio':
                        $("input[type=radio][name='" + n + "']").each(function () {
                            if ($(this).val() == o) {
                                $(this).attr("checked", "true");
                                $(this).trigger("change");
                            }
                        });
                        //$("input[type=radio][name='" + n + "']").val(o);
                        break;
                    case 'checkbox':
                        var arr = o.split(',');
                        $.each(arr, function (i, v) {
                            $("input[type=checkbox][name='" + n + "']").each(function () {
                                if ($(this).val() == v) {
                                    $(this).attr("checked", true);
                                    $(this).prop("checked", true);
                                    if (!$(this).hasClass('.tcc-checkbox-list-all')) {
                                        $(this).trigger("change");
                                    }
                                }
                            });
                        });
                        break;
                }

            } else {
                if (obj.is(".tcc-input-combo")) {
                    obj.val(o).trigger("change");
                } else if (obj.is(".tcc-input-date")) {
                    obj.val(o);
                    obj.bsdatepicker('update');
                } else {
                    obj.val(o);
                }
            }
        });
    },
    // =================================================================
    // =====================Iframe設定==================================
    // =================================================================
    popUpIframeId: 'ifrmpop',
    /** PopUpエリアをサブ画面のサイズに変更する。 */
    // ポップアップ画面は全て親画面で実装するので、自動サイズ変更は必要ではない
    //setWindowSize: function () {
    //  if (typeof (notautoresize) != "undefined" && notautoresize) {
    //    return;
    //  }
    //  if ($(document).width() == 0 && $("#container").length == 0) {
    //    //parent.$("#ifrmpop").show();
    //    $("#popModal").removeClass("fade")
    //    $("#popModal").addClass("fadeIn")
    //    $("#popModal").show();
    //  } else {
    //    if (parent.$("#" + TorihikiUtils.popUpIframeId).width() < $(document).width() && $(document).width() != 0) {
    //      parent.$("#" + TorihikiUtils.popUpIframeId).width($(document).width() + 20);
    //      //parent.$("#" + TorihikiUtils.popUpIframeId).height($(document).height() + 40);
    //      //parent.$("#ifrmpop").show();
    //    }
    //    if (parent.$("#" + TorihikiUtils.popUpIframeId).height() < $(document).height() && $(document).height() != 0) {
    //      parent.$("#" + TorihikiUtils.popUpIframeId).height($(document).height());
    //      //parent.$("#ifrmpop").show();
    //    }
    //  }
    //  parent.$("#" + TorihikiUtils.popUpIframeId).show();
    //},
    /**
     * 検索ポップアップ画面を表示する
     * @param {any} ctl
     * @param {any} subaction
     */
    showCommentPopup: function (options, noBeforeExecCheck) {
        var self = this;
        // セッションステータスチェック
        return new Promise((resolve, reject) => {
            self.checkSessionState(resolve, reject);
        }).then(data => {
            if (typeof noBeforeExecCheck === 'undefined') {
                // 前チェック
                var beforeExec = $(options.stylecontrol).attr('beforeExec');
                if (typeof beforeExec !== 'undefined') {
                    var callbackData = {};
                    callbackData.actionElement = options.stylecontrol;
                    callbackData.data = options.data;
                    callbackData.action = options.action;
                    var beforeExecEvent = beforeExec;
                    let isAsync = false;
                    if (beforeExec.indexOf(':') > -1) {
                        beforeExecEvent = beforeExec.split(':')[0].trim();
                        isAsync = true;
                    }
                    if (!isAsync) {
                        if (!self.execCallback(beforeExecEvent, callbackData)) return false;
                    } else {
                        TorihikiUtils.hideLoading();
                        return new Promise((resolve, reject) => {
                            self.execCallbackAsync(beforeExecEvent, resolve, reject, callbackData);
                        }).then(data => {
                            if (data) {
                                self.showCommentPopup(options, true);
                            }
                        });
                        return;
                    }
                }
            }

            // コメント処理Bodyを呼出する。
            self.showCommentPopupBody(options);
        });
    },
    /**
     * コメント画面.
     * @param {any} options
     */
    showCommentPopupBody: function (options) {
        var defaults = {
            url: options.url,
            action: 'Search',
            params: options.data,
            width: '1200px',
            height: '100%',
            callback: options.callback,
            stylecontrol: options.stylecontrol,
            shanaiMemoControl: options.shanaiMemoControl
        };
        defaults = $.extend(defaults, options);
        defaults.url = TorihikiUtils.addPageSessionId(defaults.url);

        // 幅と高さ設定
        if (defaults.width.indexOf('%') == -1 && defaults.width.indexOf('px') == -1) {
            defaults.width = defaults.width + 'px';
        }
        if (defaults.height.indexOf('%') == -1 && defaults.height.indexOf('px') == -1) {
            defaults.height = defaults.height + 'px';
        }

        // 伝票識別区分処理画面サイズを変更する。
        switch (defaults.data.dempyo_shikibetsu_kbn) {
            case 'JU': // 受注
            case 'HT': // 配送手配
            case 'ST': // 商品手配
            case 'HE': // 返却
            case 'HU': // 返却受付
                break;
            case 'AZ': // 預り商品
            case 'CI': // 駐車違反
            case 'ID': // 移動依頼
                defaults.width = '700px';
                break;
            default:
                break;
        }

        //
        TorihikiUtils.showLoading();

        // コンテナー
        var comentcontainer = '<div class="modal tcc-popup-right fade tcc-comment-popdetail" id="tempPopuppDetail" tabindex="-1" role="dialog" aria-labelledby="msg" aria-hidden="true" data-backdrop="static" >';
        comentcontainer += '<div class="modal-dialog" style="width:' + defaults.width + '">';
        comentcontainer += '<div class="modal-content">';
        comentcontainer += '<div class="modal-header">';
        comentcontainer += '<h3 class="tcc-group-title modal-title" id="tcc-comment-title">コメントパネル</h3>';
        comentcontainer += '<a class="close" style="cursor:pointer;" data-dismiss="modal" aria-hidden="true"><i class="fa fa-times"></i></a>';
        comentcontainer += '</div>';
        comentcontainer += '<div class="modal-body modal-body-comment">';
        comentcontainer += '</div>';
        comentcontainer += '</div></div></div>';
        // パラメータ設定
        var param = TorihikiUtils.parseParam(defaults.params);
        var popModelid = TorihikiUtils.getRandomId('ifrmpop_comment');
        var iframeadd = '<iframe frameborder="0" id="' + popModelid + '" name="' + popModelid + '" scrolling="auto" style="width:100%;height:92%;"></iframe>';

        // 既存のクリア
        if ($("div#tempPopuppDetail").length > 0) {
            $("div#tempPopuppDetail").remove();
        }
        // コンテナー追加
        TorihikiUtils.window.$(document.body).append(comentcontainer);
        // コンテナー内容追加
        $('div#tempPopuppDetail .modal-body').empty();
        $('div#tempPopuppDetail .modal-body').html(iframeadd);
        $('div#tempPopuppDetail')[0].style.zIndex = TorihikiUtils.newPopUpZIndex();

        // パラメータをフォームデータ作成し、コミットする。
        var form = document.createElement("form");
        $(form).addClass('hidden');
        TorihikiUtils.addFormItem(form, defaults.params);
        // Tokenを追加
        $(form).append($(TorihikiGlobal.requestVerificationToken).last().clone());
        form.target = popModelid;
        form.action = TorihikiUtils.addPageSessionId(defaults.url);
        form.method = 'post';
        TorihikiUtils.window.$(document.body).append(form);
        $(form).submit();
        $('div#tempPopuppDetail').modal("show");

        // 非表示になる時
        $('div#tempPopuppDetail').on('hide.bs.modal', function () {
            if (frames[popModelid].popresult != undefined && frames[popModelid].popresult) {
                if (defaults.callback) {
                    defaults.callback(frames[popModelid].popresult_data);
                }
                // 配送コメントなどがあれば
                if (frames[popModelid].popresult_data.hasComment) {
                    var $buttonCtl = $(defaults.stylecontrol);
                    if (typeof $buttonCtl.data('wijmo-row') !== 'undefined') {
                        var row = $buttonCtl.data('wijmo-row');
                        var dempyoNo = $buttonCtl.data('dempyo-no');
                        var existedKey = $buttonCtl.data('wijmo-row-comment-existed-key');
                        var dempyoNoKey = $buttonCtl.data('wijmo-row-comment-dempyono-key');
                        var grid = $buttonCtl.data('wijmo-grid');
                        var cv = grid.collectionView;
                        cv.items.filter(function (t) {
                            if (t[dempyoNoKey] == dempyoNo) {
                                t[existedKey] = StaticConst.FLG_TRUE;
                            }
                        })
                        grid.refresh();
                    } else {
                        if (defaults.stylecontrol != undefined) {
                            // 同じ伝票NoのコメントボタンのCssを変更
                            var dempyoNo = $(defaults.stylecontrol).data('dempyo-no');
                            var commentSeqNo = $(defaults.stylecontrol).data('comment-seq-no');
                            if (dempyoNo == "" || dempyoNo == undefined) {
                                $('[master="comment"][data-comment-seq-no="' + commentSeqNo + '"]').removeClass('tcc-btn-outline-green').addClass('tcc-btn-green');
                            } else {
                                $('[master="comment"][data-dempyo-no="' + dempyoNo + '"]').removeClass('tcc-btn-outline-green').addClass('tcc-btn-green');
                            }
                        }
                    }
                }
                if (defaults.shanaiMemoControl != undefined && typeof frames[popModelid].popresult_data.shanaiMemo !== 'undefined') {
                    $(defaults.shanaiMemoControl).val(frames[popModelid].popresult_data.shanaiMemo);
                }
            }
            else {
                if (defaults.callback) {
                    defaults.callback();
                }
            }
            $("div#tempPopuppDetail").remove();
        });

        setTimeout(function () {
            TorihikiUtils.checkLoadingItlIframe = setInterval(function () { TorihikiUtils.checkLoadingIframe(popModelid, defaults.url); }, 500);
            $(form).remove();
        }, 500);

    },
    /**
     * 画面ロード時、コメント画面表示処理.
     * */
    showCommentOnLoad: function () {
        // コメント表示情報取得
        var commentData = TorihikiUtils.getSessionDataOfJson(TorihikiGlobal.PageShowCommentKey);
        // 取得できない場合、処理中止
        if (typeof commentData === 'undefined') return;
        if (commentData == null) return;

        // コメント表示しない場合、処理中止
        if (commentData.showComment != '1') return;

        var dempyoNo = commentData.dempyoNo;
        // 伝票No.が設定されない場合、処理中止
        if (typeof dempyoNo === 'undefined') return;

        // 伝票No.よりコメントコントロールを取得する。
        var $denpyoLink = $('[data-dempyo-no="' + dempyoNo + '"]');
        // コントロール取得できない場合、処理中止。
        if ($denpyoLink.length == 0) return;

        // クリックイベントを呼出。
        $denpyoLink[0].click();
    },
    /**
     * 通知リスト表示切替処理.
     * */
    noticeListShowToggle: function () {
        $(document).click(function () {
            if (typeof event === 'undefined') {
                return;
            }

            if ($(event.srcElement).parents().hasClass('tcc-wf-notice-list')) {
                if (!top.$('.tcc-wf-notice-modal').is(':hidden')) {
                    top.$('.tcc-wf-notice-modal').modal('hide');
                }
            } else {
                if ($(event.srcElement).hasClass('tcc-wf-notice-icon-bg')
                    || $(event.srcElement).parents().hasClass('tcc-wf-notice-icon-bg')
                    || $(event.srcElement).parents().hasClass('tcc-wf-notice-tab-page')
                    || $(event.srcElement).parents().hasClass('tcc-wf-notice-modal')
                    || $(event.srcElement).parents().hasClass('tcc-comment-popdetail')
                    || window.name.indexOf('ifrmpop') > -1) {
                    return;
                }

                if (!top.$('.tcc-wf-notice-modal').is(':hidden')) {
                    top.$('.tcc-wf-notice-modal').modal('hide');
                }
            }
        });
    },
    /**
     * ポップアップ（iframe）ページ設定
     * */
    setIframePage: function () {
        if (window.name == "ifrmpop") {
            // 
            $('#container').addClass('popup-container');
            setTimeout(TorihikiUtils.setWindowSize, 100);
        }
        if (window.name == "ifrmpop-pos-absolute") {
            // 
            $('#container').addClass('popup-container');
            setTimeout(TorihikiUtils.setWindowSize, 100);
            TorihikiUtils.popUpIframeId = 'ifrmpop-pos-absolute';
        }

        if ($('#btnRelogin') && window.name.indexOf('ifrmpop') > -1) {
            showNoticeIcon = false;
            $('#btnRelogin').text('閉じる');
            $('#btnRelogin').click(function () {
                window.close();
            })
        }

        if (window.name.indexOf('ifrmpop') > -1) {
            //parent.$(TorihikiUtils.getPopModalId()).find('.modal-dialog').css('box-shadow', "10px 10px 5px #f00");
            parent.$(TorihikiUtils.getPopModalId()).addClass('tcc-popsearch-shadow');
            // $('.tcc-popup-fixed-footer').prev().addClass('tcc-mb-40');
        }
    },
    /**
     * リスト自動高さ設定.
     * @param {any} container
     */
    setGridAutoHeight: function (container) {
        var content;
        var scrollbarHeight = TorihikiGlobal.scrollbarWidth;
        var borderWidth = 2;
        if (container) {
            content = $(container);
        } else {
            content = $(document);
        }
        // リスト高さ自動変更
        if (content.find('.tcc-grid-auto-height').length > 0 || content.hasClass('tcc-grid-auto-height')) {
            var list = content.find('.tcc-grid-auto-height:not(.fixed_header_display_none_at_print)');
            if (content.hasClass('tcc-grid-auto-height')) {
                list = content;
            }
            var baseFooterHeight = 48;
            var popupFooterHeight = 32;
            var footerHeight = $('.tcc-footer').outerHeight();
            if (footerHeight == 0 && $('.tcc-footer').length > 0) {
                footerHeight = baseFooterHeight;
            }
            if ($('.tcc-footer').length == 0) {
                if ($('.tcc-popup-fixed-footer').length != 0) {
                    footerHeight = $('.tcc-popup-fixed-footer').outerHeight();
                    if (footerHeight <= 0) {
                        footerHeight = popupFooterHeight;
                    }
                }
                if ($('.tcc-popup-footer').length != 0) {
                    footerHeight = $('.tcc-popup-footer').outerHeight();
                    if (footerHeight <= 0) {
                        footerHeight = popupFooterHeight;
                    }
                }
            }

            if (typeof footerHeight === 'undefined') {
                footerHeight = 0;
            }

            // 最終コントロール以外の場合、data-margin-bottom属性があれば
            if (list.data('margin-bottom')) {
                footerHeight += list.data('margin-bottom');
            }

            /** リストサイズリセット */
            var resetListAutoHeight = function (con) {
                var $con = $(con);
                var isHidden = false;
                if ($con.is(':hidden') && $con.hasClass('hidden-fade')) {
                    isHidden = true;
                    $con.show();
                }
                var meisaiTop = $con.offset().top;
                var minHeight = $con.data('auto-height-min-height');
                // 横スクロールバーが存在すれば
                if (!TorihikiUtils.hasHorizScrollbar()) {
                    scrollbarHeight = 0;
                }
                if (typeof minHeight === 'undefined') {
                    minHeight = 100;
                }
                var costedheight = meisaiTop + footerHeight + $('#wrapper').css("paddingBottom").replace('px', '') * 1 + borderWidth;
                var heightDiff = window.innerHeight - costedheight;
                if (heightDiff > 0 && heightDiff > minHeight + scrollbarHeight) {
                    list.height(heightDiff - scrollbarHeight);
                    //$con.css({ "max-height": (heightDiff - scrollbarHeight).toString() + "px" });
                }
                else {
                    list.height(minHeight);
                    //$con.css({ "max-height": minHeight.toString() + "px" });
                }
                if (isHidden) {
                    $con.hide();
                }
            }

            resetListAutoHeight(list);

            //list.css({ "border-right": "none" });
            //list.css({ "border-bottom": "none" });

            //// 縦
            //if (TorihikiUtils.hasConVertiScrollbar(list[0])) {
            //    list.css({ "border-right": "solid 1px #cccccc" });
            //}

            //// 横
            //if (TorihikiUtils.hasConHorizScrollbar(list[0])) {
            //    list.css({ "border-bottom": "solid 1px #cccccc" });
            //}
        }
    },
    /**
     * Windows上の横スクロールバー存在判断
     **/
    hasHorizScrollbar: function() {
        return document.body.scrollWidth > (window.innerWidth || document.documentElement.clientWidth);
    },
    /**
     * スクロールバー存在判断
     **/
    hasConVertiScrollbar: function (container) {
        if ((container.scrollHeight > container.clientHeight) || (container.offsetHeight > container.clientHeight)) {
            return true;
        }
        return false;
    },

    hasConHorizScrollbar: function (container) {
        if ((container.scrollWidth > container.clientWidth) || (container.offsetWidth > container.clientWidth)) {
            return true;
        }
        return false;
    },
    /**
     * 横スクロールバー幅を取得する。
     **/
    getScrollbarWidth: function () {
        var scrollDiv = document.createElement("div");
        scrollDiv.style.cssText = 'width: 99px; height: 99px; overflow: scroll; position: absolute; top: -9999px;';
        document.body.appendChild(scrollDiv);
        var scrollbarWidth = scrollDiv.offsetWidth - scrollDiv.clientWidth;
        document.body.removeChild(scrollDiv);
        return scrollbarWidth;
    },
    /**
     * サムネイル画像設定.
     * @param {any} sourceImage
     */
    loadThumbnailImage: function(sourceImage) {
        if (typeof sourceImage === 'undefined') {
            sourceImage = this;
        }
        $(sourceImage).width('50');
        if (sourceImage.width > sourceImage.height) {
            $(sourceImage).addClass('tcc-thumbnail-img-fullwidth');
        } else {
            $(sourceImage).addClass('tcc-thumbnail-img-fullheight');
        }
        $(sourceImage).width('');
    },
    /**
     * フッターボタン位置自動調整.
     */
    setFooterButtonAutoMove: function () {
        var autoMove = function () {
            var baseFooterHeight = 48;
            var baseOffsetTop = 40;
            if ($('.tcc-footer').outerHeight() > baseFooterHeight) {
                $('.tcc-footer .tcc-footer-left .tcc-btn').each(function (i, btn) {
                    if (btn.offsetTop < baseOffsetTop) {
                        $(btn).removeClass('tcc-footer-btn-second');
                    } else {
                        console.log(btn);
                        $(btn).addClass('tcc-footer-btn-second');
                    }
                })
            }
        }

        $(window).on('resize', function () {
            autoMove();
        })

        autoMove();
    },
    // =================================================================
    // =====================連動設定====================================
    // =================================================================
    /**
   * 連動コントロール設定
   * @param {any} container
   */
    setLinkage: function (container) {
        var content;
        if (container) {
            content = $(container);
        } else {
            content = $(document);
        }

        // コンボボックス変更イベント
        content.find('[linktarget]').change(function (e) {
            //// 変更ターゲット取得する。
            //if (!$(this).attr("linktarget")) {
            //  return;
            //}
            var changectl = this;
            var linktarget = $(this).attr("linktarget").split(',');
            // ループ
            $(linktarget).each(function (i, t) {
                var elementName = t;
                if (elementName.indexOf('$') != -1) {
                    elementName = elementName.replace('$', '');
                } else if (elementName.indexOf('#') == 0) {
                    elementName = elementName.replace('#', '');
                    elementName = foolproof.getName(changectl, elementName);
                }
                var form = $(changectl).closest('form');
                // フォームが存在すれば
                if (form.length > 0 && form.find('[name="' + elementName + '"]').length > 0) {
                    var $el = form.find('[name="' + elementName + '"]');
                } else {
                    var $el = $('[name="' + elementName + '"]');
                }
                if (($el).length == 0) return;

                var ctl = $el[0];
                // コンボボックス
                if ($el.hasClass("tcc-input-combo")) {
                    var masterkey = $el.attr("master") || $el.data("master");
                    var params = {};
                    if ($(ctl).attr("linkrefdata")) {
                        //var poprefdatakeys = $(actionelement).attr("poprefdata").split(',');
                        var linkrefdata = $(ctl).attr("linkrefdata");
                        params = TorihikiUtils.convertRefDataToParams(ctl, ctl, linkrefdata);
                    }

                    $el.comboSelect(
                        {
                            url: TorihikiUtils.addPageSessionId(ComboUpdateDataUrl),
                            queryparams: {
                                master: masterkey,
                                condtionkey: JSON.stringify(params),
                                __RequestVerificationToken: $(TorihikiGlobal.requestVerificationToken).last().val(),
                            }
                        }
                    );
                }

            });
        });

        // 処理設定
        content.find('[linkage=True],[linkage=true]').each(function () {
            switch (this.tagName.toLowerCase()) {
                case 'select':
                    $(this).on('change', function (t) {
                        TorihikiUtils.getLinkageInfo(this);
                    });
                    break;
                default:
                    $(this).on('change', function (t) {
                        TorihikiUtils.getLinkageInfo(this);
                    });
                    break;
            }
        });
    },
    /**
     * 連動情報取得
     * @param {any} ctl
     */
    getLinkageInfo: function (ctl) {
        if ($(ctl).hasClass('tcc-zip-part')) {
            ctl = $(ctl).nextAll('.tcc-zip-source').first();
        }

        if ($(ctl).data('un-linkage')) {
            $(ctl).data('un-linkage', false);
            return;
        };

        // 連動処理実行されない場合
        var unchange = $(ctl).attr("skip-linkage-change");
        if (typeof unchange !== 'undefined' || unchange == '1') {
            $(ctl).removeAttr('skip-linkage-change');
            return;
        }
        var url = $(ctl).attr("master");
        // マスタ属性がない時
        if (typeof (url) == "undefined" || url == "") {
            return;
        }
        var action = "";
        var type = "post";
        action = $(ctl).attr("master");

        var actionHandle = $(ctl).attr("action-handle");
        if (actionHandle == undefined) actionHandle = $(ctl).attr("actionHandle");

        var actionelement = $(ctl);
        var actionvalue = actionelement.val();
        if (typeof ($(ctl).attr("data-inputmask")) != "undefined") {
            actionvalue = $(ctl).inputmask('unmaskedvalue');
        }

        switch (action) {
            case 'yubinbangou':
                actionvalue = $(ctl).inputmask('unmaskedvalue');
                if (actionvalue.length != 8) return;
                break;
        }

        TorihikiUtils.showLoading();
        var params = {};
        var refdata = $(ctl).attr("linkrefdata");
        var sourceField = $(ctl).attr("datafield") || $(ctl).attr("dataField");
        var sourceVal = $(ctl).val();
        params = TorihikiUtils.convertRefDataToParams($(ctl)[0], $(ctl)[0], refdata)
        TorihikiUtils.ajaxEx({
            url: TorihikiUtils.addPageSessionId(LinkageUrl + "/" + url),
            data: {
                master: action,
                condtionkey: JSON.stringify(params)
            },
            cache: false,
            type: type,
            success: function (data) {
                TorihikiUtils.hideLoading();
                if (data.data != null && data.data != 'null') {
                    if (data.data == "showPopUp") {
                        switch (action) {
                            case 'yubinbangou': // 郵便番号
                                var sourceVals = sourceVal.split('-');
                                var yubinNo1 = sourceVals[0];
                                var yubinNo2 = '';
                                if (sourceVals.length > 1) {
                                    yubinNo2 = sourceVals[1];
                                }
                                $(ctl).next().data('clear-poprefdata', true).attr('poprefdata', '{ "YUBIN_NO":"' + sourceVal + '","YUBIN_NO1":"' + yubinNo1 + '","YUBIN_NO2":"' + yubinNo2 + '"}').click();
                                break;
                        }
                    } else {
                        TorihikiUtils.setLinkageReturnData(action, actionHandle, JSON.parse(data.data));
                    }
                } else {
                    var setdata = {};
                    if (sourceField) {
                        sourceField = sourceField.split('/');
                        $(sourceField).each(function (i, key) {
                            setdata[key] = sourceVal;
                        })
                    }
                    TorihikiUtils.setLinkageReturnData(action, actionHandle, setdata);
                }
            }
        });
    },
    /**
     * ポップアップ画面で選択されたデータを画面に設定する。
     * @param {any} master
     * @param {any} actionHandle
     * @param {any} row
     */
    setLinkageReturnData: function (master, actionHandle, row) {
        if (row == undefined) {
            console.log('No Selected Data');
            return;
        }
        var poptargetitems = $.find("[action-handle='" + actionHandle + "']");
        if (poptargetitems == undefined || poptargetitems.length == 0) poptargetitems = $.find("[actionHandle='" + actionHandle + "']");
        $(poptargetitems).each(function () {
            // 郵便番号のパーツ時
            if ($(this).hasClass('tcc-zip-part')) return;
            var form = $(this).closest('form');
            var datafield = $(this).attr("data-field");
            var data_field_key = "data-field";
            if (typeof datafield == "undefined") {
                datafield = $(this).attr("dataField");
                data_field_key = "dataField";
            }
            if (typeof datafield != "undefined") {
                var data = '';
                var orsplit = '/';
                if (datafield.indexOf(orsplit) > -1) {
                    $.each(datafield.split(orsplit), function (i, df) {
                        if (row[df] != undefined) {
                            data = row[df];
                            return false;
                        }
                    });
                } else {
                    data = row[datafield];
                }

                switch (this.tagName.toLowerCase()) {
                    case "select":
                        if (data != $(this).val()) {
                            $(this).val(data);
                            $(this).trigger('change.select');
                            $(this).trigger('change');
                        }
                        break;
                    case "input":
                        switch (this.type.toLowerCase()) {
                            case "checkbox":
                                $(this).removeAttr("checked");
                                this.checked = false;
                                if ($(this).val() == data) {
                                    $(this).attr("checked", true);
                                }
                                break;
                            case "radio":
                                var elename = $(this).attr("name");
                                $("input[type=radio][name='" + elename + "']").each(function () {
                                    $(this).removeAttr("checked");
                                    if ($(this).val() == data) {
                                        $(this).attr("checked", true);
                                    }
                                });
                                break;
                            default:
                                if (data != $(this).val()) {
                                    $(this).val(data);
                                    $(this).trigger('change');
                                }
                                if (form.length > 0) $(this).valid();
                                break;
                        }
                        break;
                    case "span":
                    case "div":
                    case "label":
                    case "a":
                        var datafield = $(this).attr(data_field_key).split('+');
                        var datatext = '';
                        $(datafield).each(function (i, field) {
                            if ($.trim(field) != "") {
                                if (row[field] != undefined) datatext += row[field];
                            } else {
                                datatext += field;
                            }
                        });
                        $(this).text(datatext);
                        break;
                }
            }
        });

        // 画面別に設定処理があれば
        if (typeof afterSetLinkageDataOnPage !== 'undefined' && typeof afterSetLinkageDataOnPage === 'function') {
            afterSetLinkageDataOnPage(master, actionHandle, row);
            return;
        }
    },

    /**
     * 画面戻る時、検索コントロール再設定
     * @param {any} sessionKey
     */
    resetSearchCtl: function (sessionKey) {
        var self = this;
        if (typeof sessionKey === 'undefined') {
            sessionKey = self.pageInitKey;
        }
        if (sessionStorage.getItem(sessionKey) == null || sessionStorage.getItem(sessionKey) == undefined || sessionStorage.getItem(sessionKey) == 'undefined') return;
        var savedData = JSON.parse(sessionStorage.getItem(sessionKey));

        // 検索CDコントロール
        $('.tcc-search-ctl-cd-edit').each(function (i, t) {
            var $container = $(this).parent();
            var $this = $(this);
            var $name = $container.find(".tcc-search-ctl-name-edit");
            var $kana = $container.find("div.tcc-search-ctl-kana");
            var $linkc = $container.find(".tcc-search-ctl-name-show");
            var $linka = $container.find(".tcc-search-ctl-name-show  .tcc-search-ctl-link");
            var $badge = $linka.data('tcc-badge-ctl');

            var namehidden = $name.hasClass('hidden');
            $linka.text($name.val());

            // マック設定する
            if ($badge != undefined) {
                var showbadge = savedData[$name.attr('name') + "-tcc-badge-show"];
                if (showbadge) {
                    $badge.show();
                } else {
                    $badge.hide();
                }
            }

            // 名称コントロールが非表示で設定する時
            if (namehidden) {
                return;
            }
            // CDの値で名称コントロールと名称リンクの表示モードを切替する。
            if ($this.val() == "") {
                $kana.show();
                $name.show();
                $linkc.hide();
            } else {
                $kana.hide();
                $name.hide();
                $linkc.show();
            }

        })
    },
    /**
     * 検索コントロール情報保存.
     * @param {any} jsonData
     */
    saveSeachCtlInfo: function ($form, jsonData) {

        // 検索コントロール対応
        $form.find('.tcc-search-ctl-name-show').each(function () {
            var $container = $(this).parent();
            var $name = $container.find(".tcc-search-ctl-name-edit");
            var $linka = $container.find(".tcc-search-ctl-name-show  .tcc-search-ctl-link");

            var $badge = $linka.data('tcc-badge-ctl');

            // マック設定する
            if ($badge != undefined) {
                jsonData[$name.attr('name') + "-tcc-badge-show"] = !$badge.is(':hidden');
            }
        })
    },
    // =================================================================
    // =====================画面ページ処理===============================
    // =================================================================  
    pageReloadKey: '__pageReloadKey',
    pageListKey: '__pageList',
    /** 画面遷移履歴保存 */
    savePageTransferHistory: function () {
        var wname = window.name;
        var unSavePageTransfer = false;
        if (wname == "ifrmpop") unSavePageTransfer = true;
        if (wname == "ifrmpop-pos-absolute") unSavePageTransfer = true;
        if (wname == "ifrmpop_comment") unSavePageTransfer = true;
        if (wname.indexOf("ifrmpop_comment") > -1) unSavePageTransfer = true;
        if (wname.indexOf("ifrmpop") > -1) unSavePageTransfer = true; // ポップアップ検索画面
        if (wname.indexOf("ifrmDetail") > -1) unSavePageTransfer = true; // ポップアップ参照画面
        if (typeof SiteLoginPath === 'undefined')
            unSavePageTransfer = true;
        else
            if (location.href.indexOf(SiteLoginPath) > -1) unSavePageTransfer = true; // ポップアップ画面
        //console.log(unSavePageTransfer)
        //console.log(wname)
        if (unSavePageTransfer) return;
        /* 遷移元 */
        var ref = document.referrer;
        if (ref == "") return;
        /* 現在ＵＲＬ */
        var cur = document.location.href;

        var curPath = document.location.href.replace(document.location.origin + SiteStartPath + '/', '')
        var area = curPath.split('/').length > 0 ? curPath.split('/')[0] : ''
        window.parent.postMessage({
            type: "loaded",
            area
        })

        var objpage = TorihikiUtils.getSessionData(TorihikiUtils.gPageSessionKey);
        /* 存在しない時 */
        if (typeof objpage == undefined || objpage == null) {
            objpage = [];
            pagetran = {}
            pagetran.cur = cur;
            objpage.push(pagetran);
            /* 情報保存 */
            TorihikiUtils.saveSessionData(TorihikiUtils.gPageSessionKey, objpage);
        } else {

            /* セッションデータ取得(Json) */
            var objpage = TorihikiUtils.getSessionDataOfJson(TorihikiUtils.gPageSessionKey);

            var pageindex = -1;
            /* 既存情報取得 */
            var exited = objpage.filter(function (page, idx) {
                if (page.cur == cur) pageindex = idx;
                return page.cur == cur
            });

            /* 既存なし、#があれば */
            if (exited.length == 0 && cur.indexOf('#') > -1) {
                /* [#] 処理 */
                var addr1 = cur.split('#');
                var curtemp = addr1[0];

                /* 既存情報取得 */
                var exitedtemp = objpage.filter(function (page, idx) {
                    return page.cur == curtemp
                });

                // データが存在すれば、戻る
                if (exitedtemp.length > 0) {
                    //console.log(JSON.stringify(objpage));
                    return;
                }
            }

            /* 既存なし、追加 */
            if (exited.length == 0) {
                pagetran = {}
                pagetran.cur = cur;

                objpage.push(pagetran);
            } else {
                //console.log(pageindex + "_" + objpage.length);
                /* アイテム削除 */
                objpage.splice(pageindex + 1, objpage.length - pageindex - 1);
            }
            /* データ保存 */
            TorihikiUtils.saveSessionData(TorihikiUtils.gPageSessionKey, objpage);

            //console.log(JSON.stringify(objpage));

        }
    },
    /** 画面遷移履歴取得 
    * @param { any } pagereload
    */
    getHistoryPage: function (pagereload) {
        var url = TorihikiUtils.getHistoryPageUrl(pagereload);
        // メニューから時、セッションをクリアする。
        var from = TorihikiUtils.getTargetUrlParam(url, TorihikiGlobal.PageSourceKey);
        switch (from) {
            case 'menu':
                // 設定した後で、保存した検索条件をクリアする。
                url = url.replace('&from=menu', '');
                return url;
        }

        return url;
    },
    /** 画面遷移履歴取得 
    * @param { any } pagereload
    */
    getHistoryPageUrl: function (pagereload) {
        if (pagereload == undefined) {
            pagereload = true;
        }
        /* セッションデータ取得(Json) */
        var objpage = TorihikiUtils.getSessionDataOfJson(TorihikiUtils.gPageSessionKey);

        if (typeof objpage === 'undefined' || objpage == null) return '';

        /* 遷移元 */
        var ref = document.referrer;
        /* 現在ＵＲＬ */
        var cur = document.location.href;

        var pageindex = -1;
        /* 既存情報取得 */
        var exited = objpage.filter(function (page, idx) {
            if (page.cur == cur) pageindex = idx;
            return page.cur == cur
        });
        /* ページが存在する時 */
        if (pageindex > 0) {
            // 遷移元画面を再検索する時
            if (pagereload) TorihikiUtils.saveSessionData(TorihikiUtils.pageReloadKey, pagereload);
            return objpage[pageindex - 1].cur;
        } else {

            // 存在しない時、＃があれば
            if (cur.indexOf('#') > -1) {
                /* [#] 処理 */
                var addr1 = cur.split('#');
                cur = addr1[0];

                /* 既存情報取得 */
                exited = objpage.filter(function (page, idx) {
                    if (page.cur == cur) pageindex = idx;
                    return page.cur == cur
                });

                if (pageindex > 0) {
                    // 遷移元画面を再検索する時
                    if (pagereload) TorihikiUtils.saveSessionData(TorihikiUtils.pageReloadKey, pagereload);
                    return objpage[pageindex - 1].cur;
                }
            }
            if (objpage.length < 2) return '';
            return objpage[objpage.length - 2].cur;
        }
    },
    /** 遷移元に遷移する。
    * @param { any } url
    */
    gotoHistoryPage: function (url) {
        var historyurl = TorihikiUtils.getHistoryPage();
        if (historyurl != null && historyurl != undefined) {
            TorihikiUtils.showLoading();
            location.replace(historyurl);
        } else {
            if (url != undefined) {
                TorihikiUtils.showLoading();
                location.replace(url);
            }
        }
    },
    removeUrlFromHistory: function (url) {
        var pageindex = -1;
        /* セッションデータ取得(Json) */
        var objpage = TorihikiUtils.getSessionDataOfJson(TorihikiUtils.gPageSessionKey);
        /* 既存情報取得 */
        var exited = objpage.filter(function (page, idx) {
            if (page.cur == url) pageindex = idx;
            return page.cur == url
        });
        if (pageindex > -1) {
            /* アイテム削除 */
            objpage.splice(pageindex, 1);
            /* データ保存 */
            TorihikiUtils.saveSessionData(TorihikiUtils.gPageSessionKey, objpage);
        }
    },
    /**
     * 画面リロード判断.
     */
    ifBackReload: function () {
        // セッションデータ取得
        var pagereload = TorihikiUtils.getSessionData(TorihikiUtils.pageReloadKey);
        // セッションデータ削除
        TorihikiUtils.removeSessionData(TorihikiUtils.pageReloadKey);
        if (pagereload != undefined && pagereload) {
            return true;
        }
        return false;
    },
    /** 画面遷移履歴イベント設定 */
    setPageTransferHistory: function () {
        var his = TorihikiUtils.getHistoryPage(false);
        if (his != null) {
            $('.tcc-return-btn').removeClass('hidden').click(function () {
                location.replace(his);
            });
        }
    },
    /** 画面別セッションIDを作成する。*/
    getPageSessionId: function () {
        var t = TorihikiUtils.getUrlParam(TorihikiGlobal.PageSessionKey);
        if (t == undefined || t == '') {
            t = TorihikiUtils.getRandomId();
        }
        var pageSessionIdKey = TorihikiGlobal.PageSessionIdKey;

        TorihikiUtils.saveSessionData(pageSessionIdKey, t);

        return TorihikiUtils.getSessionData(pageSessionIdKey);
    },
    /** FormにセッションIDを追加する。*/
    addSessionIDToFormAction: function () {
        $('form').each(function () {
            this.action = TorihikiUtils.addPageSessionId(this.action);
        })
    },
    /**
     * URLの変数を取得する。
     * @param {any} name
     */
    getUrlParam: function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return undefined;
    },
    /**
     * URLの変数を取得する。
     * @param {any} name
     */
    getTargetUrlParam: function (url, name) {
        var allParams = TorihikiUtils.getAllUrlParms(url);
        return allParams[name];
    },
    /**
     * URLの引数を取得
     * @param {any} url
     */
    getAllUrlParms: function (url) {
        var theRequest = new Object();
        if (!url)
            url = location.href;
        if (url.indexOf("?") !== -1) {
            var str = url.substr(url.indexOf("?") + 1) + "&";
            var strs = str.split("&");
            for (var i = 0; i < strs.length - 1; i++) {
                var key = strs[i].substring(0, strs[i].indexOf("="));
                var val = strs[i].substring(strs[i].indexOf("=") + 1);
                theRequest[key] = val;
            }
        }
        return theRequest;
    },
    /**
     * メニューのクリック処理設定
     * @param {any} options
     */
    loadIframeLoc: function (options) {
        var defaults = { link: '.nav a', updateNow: true };
        defaults = $.extend(defaults, options);
        razorContainer = $(defaults.container);

        $(defaults.link).each(alink);

        //var razorUrl = "";
        function alink(idx, link) {

            if ($(link).attr("onclick")) return;
            if (link.href.indexOf('###') >= 0) return;
            if (link.href.indexOf('javascript') >= 0) return;
            if (link.href == '') return;
            var addr1 = link.href.split('#');
            var addr2 = location.href.split('#');
            if (addr1[0] == addr2[0]) return;

            $(link).click(function (e) {
                e.preventDefault();
                if ($(link).attr("action")) {
                    ConfirmDelete(link);
                }
                else {
                    if (e.delegateTarget.href.indexOf('#') == -1) {
                        // メニュー非表示になる
                        //$('.tcc-menu-fill').fadeOut();
                        //$('#tcc-header-menu-btn').click();

                        var makeNewUrl = function (isNew) {
                            var sessionid = TorihikiUtils.getRandomId();
                            appendParam = TorihikiGlobal.PageSessionKey + '=' + sessionid;
                            var linkurl = link.href;

                            if (linkurl.indexOf('?') > -1) {
                                linkurl += '&' + appendParam;
                            } else {
                                linkurl += '?' + appendParam;
                            }

                            linkurl += '&' + TorihikiGlobal.PageSourceKey + '=menu';
                            return linkurl;
                        }

                        var linkurl = makeNewUrl(true);

                        if ($(link).hasClass("tcc-open-new")) {
                            setTimeout(function () {
                                var newwin = window.open(linkurl);
                                TorihikiUtils.addWindowsList(newwin);
                            }, 100);
                        }
                        else {
                            //TorihikiUtils.showLoading();
                            if (typeof frames["ifrm-0000"].TorihikiUtils !== 'undefined') {
                                frames["ifrm-0000"].TorihikiUtils.showLoading();
                            }
                            frames["ifrm-0000"].location.replace(linkurl);
                            if (typeof frames["ifrm-0000"].TorihikiUtils !== 'undefined') {
                                setTimeout(function () {
                                    frames["ifrm-0000"].TorihikiUtils.hideLoading();
                                }, 2000);
                            }
                            TorihikiUtils.addWindowsList(frames["ifrm-0000"]);
                        }
                    }
                }
            });
        }
    },
    /**
     * 窓口リストに追加する。
     * @param {any} win
     */
    addWindowsList: function (win) {
        //if (TorihikiUtils.windowsList == undefined || TorihikiUtils.windowsList == null) {
        //  TorihikiUtils.windowsList = [];
        //  TorihikiUtils.windowsList.push(win);
        //}
        //var curdata = localStorage.getItem(TorihikiUtils.pageListKey);
        //if (curdata == undefined || curdata == null || curdata == '') curdata = [];
        //curdata.push(win);
        //localStorage.setItem(TorihikiUtils.pageListKey, curdata);
    },
    /**
     * Windows閉じるチェック処理。
     * @param {any} win
     */
    addCheckWindowClose: function (win) {
        if (window.opener) {
            var winopener = window.opener;
            var checkcount = 0;
            var interval = setInterval(function () {
                // console.log('validator.pendingRequest' + validator.pendingRequest);
                if (winopener.closed) {
                    TorihikiUtils.autoCloseWin = true;
                    window.close();
                    clearInterval(interval);
                    return;
                }
                // 部門コード変更判断
                var curBumon = $(TorihikiGlobal.dataBumon).val();
                try {
                    var openerBumon = winopener.$(TorihikiGlobal.dataBumon).val();
                    if (curBumon != openerBumon) {
                        TorihikiUtils.autoCloseWin = true;
                        window.close();
                        clearInterval(interval);
                        return;
                    }
                }
                catch (ex) {
                    //if (typeof winopener !== 'object' && winopener.location.href != topPageUrl) {
                    //    TorihikiUtils.autoCloseWin = true;
                    //    window.close();
                    //}
                    TorihikiUtils.autoCloseWin = true;
                    window.close();
                    //clearInterval(interval);
                    return;
                }
                //if (checkcount > 10) {
                //  clearInterval(interval);
                //}
                checkcount += 1;
            }, 100);
        }
        else if (window.name == 'ifrm-0000') {
            var interval = setInterval(function () {
                // 部門コード変更判断
                var curBumon = $(TorihikiGlobal.dataBumon).val();
                try {
                    var openerBumon = parent.$(TorihikiGlobal.dataBumon).val();
                    if (curBumon != openerBumon) {
                        TorihikiUtils.autoCloseWin = true;

                        // URL設定
                        // 区分マスタの値によって、トップページを切り替える。
                        // 表示フラグ1＝1　の場合 →　プログラムID=POL001900 を表示する
                        if (parent.PotalHyojiKbn == '1') {
                            // ポータルページに遷移する。
                            location.replace(parent.portalUrl);
                        }
                        // 該当データがない、または、表示フラグ1＝0　の場合 →　今まで通り、トップページに何も表示しない 
                        else {
                            // Infoページに遷移する。
                            location.replace(homePageUrl);
                        }

                        clearInterval(interval);
                        return;
                    }
                } catch (ex) {
                    //if (typeof curBumon !== 'undefined') {
                    //    TorihikiUtils.autoCloseWin = true;
                    //    // Infoページに遷移する。
                    //    location.replace(homePageUrl);
                    //}
                    //clearInterval(interval);
                    return;
                }
                checkcount += 1;
            }, 100);
        }
    },
    /**
     * ポップアップwindow判断
     * @param {any} win
     */
    isPopUpWindow: function (win) {
        if (typeof win === 'undefined') win = window;
        if (win.name.indexOf('ifrmpop') == 0) return true;
        if (win.name.indexOf('ifrmDetail') == 0) return true;
    },
    /**
     * 新しいWindow判断
     * @param {any} win
     */
    isNewTabWindow: function (win) {
        if (typeof win === 'undefined') win = window;
        if (win.name.indexOf('ifrm-0000') == 0) return false;
        return true;
    },
    /**
     * BeforeUnloadイベント設定
     * */
    setBeforeUnloadEvent: function () {
        $(window).on('beforeunload', function (evt) {
            // 他の処理で画面を閉じるとき、データ確認が必要ではない
            if (TorihikiUtils.autoCloseWin) {
                // 初期化セッションデータをクリアする。
                sessionStorage.removeItem(TorihikiUtils.pageInitKey);
                return;
            }

            var formfilter = $('form[leave-confirm="True"]');
            if (formfilter.length == 0) {
                // 初期化セッションデータをクリアする。
                sessionStorage.removeItem(TorihikiUtils.pageInitKey);
                return;
            }
            if (typeof evt === 'undefined') {
                evt = window.event;
            }
            try {
                //TorihikiUtils.testDiffMs()
                var old_params = TorihikiUtils.getPreviousDataOfJson();
                //console.log(old_params);
                var params = TorihikiUtils.serializeObject(formfilter);
                //console.log(params);
                var result = TorihikiUtils.jsonCompare(old_params, params);
                //TorihikiUtils.testDiffMs(1)
                if (!result) {
                    TorihikiUtils.hideLoading();
                    TorihikiUtils.hideLoadingImage();
                    return 'leave';
                } else {
                    // 初期化セッションデータをクリアする。
                    sessionStorage.removeItem(TorihikiUtils.pageInitKey);
                }
            } catch (ex) {
                //console.log(ex);
            }
        });
    },
    // 画面リロード
    reload: function () {
        location.reload();
    },
    // =================================================================
    // =====================ローディング処理=========================
    // ================================================================= 
    // 画面ロードした後、ローディング非表示になる
    autoHideLoading: true,
    /**
     * ローディングイメージ表示
     * @param {any} spread
     */
    showLoadingImage: function (spread) {
        //$('.tcc-loading-masker').remove();
        //var height = $(window).height();
        //var width = $(window).width();
        //var htmlValue = '<div class="tcc-loading_masker" style="height:' + height + 'px;width:' + width + 'px;"><img src="' + SiteStartPath + '/Content/images/loading2.gif" /><div>';
        //$("html").append(htmlValue);
        if (!TorihikiUtils.isNewTabWindow() && !parent.$('.tcc-loading-masker').is(':hidden')) {
            parent.$('.tcc-loading-masker').hide();
            setTimeout(function () {
                $('.tcc-loading-masker').show(spread);
            }, 50)
        } else {
            $('.tcc-loading-masker').show(spread);
        }
    },
    /**
   * ローディングイメージ非表示
   * @param {any} spread
   */
    hideLoadingImage: function (spread) {
        $('.tcc-loading-masker').fadeOut(spread);
        parent.$('.tcc-loading-masker').fadeOut(spread);
        setTimeout(function () {
        }, 60)
        //$('.tcc-loading-masker').remove();
    },
    /** ローディング表示。 */
    showLoading: function (spread) {
        if (!TorihikiUtils.isNewTabWindow() && !parent.$('.tcc-loading-masker').is(':hidden')) {
            parent.$('.tcc-loading-masker').hide();
            setTimeout(function () {
                $('.tcc-loading-masker').show(spread);
            }, 50)
        } else if ($('.tcc-loading-masker').is(':hidden')) {
            $('.tcc-loading-masker').show(spread);
        }
        //$('.tcc-loading-container').show();
    },
    /** ローディング非表示。 */
    hideLoading: function (spread) {
        $('.tcc-loading-masker').fadeOut(spread);
        if (parent) {
            parent.$('.tcc-loading-masker').fadeOut(spread);
        }
        setTimeout(function () {
        }, 60)
        //$('.tcc-loading-container').hide();
        //parent.$('.tcc-loading-container').hide();

    },
    /**画面ロードチェック */
    checkLoading: function (targetUrl) {
        //alert(frames["ifrm-0000"].location.href);
        //var title = frames["ifrm-0000"].document.title;
        //if (title.indexOf(SystemTitle) == -1) {
        //  TorihikiUtils.hideLoading();
        //  return;
        //}
        var state = frames['ifrm-0000'].document.readyState;
        var curUrl = frames['ifrm-0000'].location.href;
        if (state == "complete") {
            // 定時処理中止
            clearInterval(TorihikiUtils.checkLoadingItl);
            TorihikiUtils.hideLoading();
            return;
        }
    },
    /**IFrame画面ロードチェック */
    checkLoadingIframe: function (iframeName, targetUrl) {
        //alert(frames["ifrm-0000"].location.href);
        //var title = frames["ifrm-0000"].document.title;
        //if (title.indexOf(SystemTitle) == -1) {
        //  TorihikiUtils.hideLoading();
        //  return;
        //}
        // 画面が閉じるの場合
        if (!frames[iframeName]) {
            return;
        }
        try {
            var state = frames[iframeName].document.readyState;
            var curUrl = frames[iframeName].location.href;
            if (curUrl.indexOf(targetUrl) != -1) {
                if (state == "complete") {
                    TorihikiUtils.hideLoading();
                    // 定時処理中止
                    clearInterval(TorihikiUtils.checkLoadingItlIframe);
                    return;
                }
            }
        } catch (ex) {
            // 定時処理中止
            clearInterval(TorihikiUtils.checkLoadingItlIframe);
            return;
        }
    },
    // =================================================================
    // =====================ポップアップ画面処理=========================
    // ================================================================= 
    /* 現在のPopModaID(TODO) */
    popModalId: undefined,
    /* 現在のwindow */
    window: undefined,
    /* 現在のwindow */
    body: undefined,
    /* Popup画面の実行結果 */
    popresult: undefined,
    /* Popup画面の戻るのデータ */
    popresult_data: undefined,
    /* Popup画面のコンテナー幅 */
    conwidth: undefined,
    /* Popup画面のコンテナー高さ */
    conheight: undefined,
    /* Popup画面のコンテナー */
    messagecontainer: '<div class="modal tcc-popup-right fade" id="tempPopuppDetail" tabindex="-1" role="dialog" aria-labelledby="msg" aria-hidden="true"></div>',
    /* Popup画面のコンテナー */
    popupcontainer: '<div class="modal fade popModal" id="popModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static"></div>',
    /* ポップアップのZindex開始値 */
    popupZIndex: 2000,
    /**ポップアップの新しいZindex取得 */
    newPopUpZIndex: function () {
        TorihikiUtils.window.TorihikiUtils.popupZIndex += 1;
        return TorihikiUtils.window.TorihikiUtils.popupZIndex;
    },
    /* ポップアップのZindex開始値 */
    popdetailZIndex: 3100,
    /**ポップアップの新しいZindex取得 */
    newPopDetailZIndex: function () {
        TorihikiUtils.window.TorihikiUtils.popdetailZIndex += 1;
        return TorihikiUtils.window.TorihikiUtils.popdetailZIndex;
    },
    /**
     * カバーＤＩＶ追加
     * */
    addCoverDiv: function (id) {
        //$(document.body).append('<div class="modal-backdrop fade show" id="' + id + '"></div>');
    },
    /**
     * カバーＤＩＶ削除
     * */
    removeCoverDiv: function (id) {
        $(document.body).find('div[id="' + id + '"]').remove();
    },
    /**
     * 画面のコントロールより検索ポップアップハンドルとイベントなど設定
     * @param {any} container
     */
    setPopUp: function (container) {
        var content;
        if (container) {
            content = $(container);
        } else {
            content = $(document);
        }

        // ポップアップ関連マック設定
        content.find('[data-tcc-badge]').each(function () {
            var badge = $(this).data('tcc-badge');
            var badgectl = $(this).parent().parent().find(badge);
            badgectl.addClass('tcc-search-ctl-badge');
            $(this).data('tcc-badge-ctl', badgectl);
            $(this).after(badgectl);

            //if (badgectl.length == 0) {
            //    badgectl = $(this).next();
            //    if (badgectl.hasClass('tcc-search-ctl-badge')) {
            //        $(this).data('tcc-badge-ctl', badgectl);
            //    }
            //} else {
            //    badgectl.addClass('tcc-search-ctl-badge');
            //}
        });

        // リンク後追加したいHtml
        content.find('[data-tcc-link-append-html]').each(function () {
            var badge = $(this).data('tcc-badge-ctl');
            var appendhtml = $(this).data('tcc-link-append-html');
            $(this).data('tcc-link-append-html', $(this).parent().parent().find(appendhtml));
            if (typeof badge !== 'undefined') {
                badge.after($(this).data('tcc-link-append-html'));
            } else {
                $(this).after($(this).data('tcc-link-append-html'));
            }
        });

        // ポップアップ画面⇔検索ボタン
        //var popsearchbtn = '<button type="button" class="popsearch" master="{0}" action-handle-source="{1}" ><i class="fas fa-search"></i></button>';
        var popsearchbtn = '<a href="javascript:void(0)" class="tcc-popup popsearch" master="{0}" action-handle-element="{1}" action-handle-source="{2}" poprefdata=\'{3}\' tabindex="{4}" subtabindex="1"  ><i class="fas fa-search tcc-icn-btn "></i></a>';
        // データ追加
        var popaddbtn = '<a href="javascript:void(0)" class="tcc-popup popadd" master="{0}" action-handle-element="{1}" action-handle-source="{2}" poprefdata=\'{3}\' tabindex="{4}" subtabindex="2"><i class="fas fa-file-medical tcc-icn-btn "></i></a>';
        // データ明細
        var popdetailbtn = '<a href="javascript:void(0)" class="tcc-popup popdetail" master="{0}" action-handle-element="{1}" action-handle-source="{2}" poprefdata=\'{3}\' tabindex="{4}" subtabindex="3"><i class="fas fa-list-alt tcc-icn-btn "></i></a>';
        // データクリア
        var popclearbtn = '<a href="javascript:void(0)" class="tcc-popup popclear" master="{0}" action-handle-element="{1}" action-handle-source="{2}" poprefdata=\'{3}\' tabindex="{4}" subtabindex="4"  ><i class="fas fa-trash-alt tcc-icn-btn "></i></a>';
        // ポップアップ検索ボタン２
        var popsearchbtn2 = '<button type="button" class="tcc-popup popsearch tcc-btn tcc-btn-sm tcc-btn-outline-green" style="{4}" master="{0}" action-handle-element="{1}" action-handle-source="{2}" poprefdata=\'{3}\' tabindex="{6}" subtabindex="5">{5}</button>';
        // マスタコントロール設定
        content.find('[master]').each(function () {

            var fr = '';
            // tcc-float-right対応
            if ($(this).hasClass('tcc-float-right')) {
                fr = 'tcc-float-right';
            }

            // 右→左の順

            // クリアボタン追加
            if (!$(this).nextAll().hasClass('popclear')) {
                if ($(this).attr("showClear") && $(this).attr("showClear").toLowerCase() == "true") {
                    var clearbtn = TorihikiUtils.format(popclearbtn, $(this).attr("master"), $(this).attr('name'), $(this).attr('actionHandle'),
                        typeof ($(this).attr("poprefdata")) == "undefined" ? "" : $(this).attr("poprefdata"), $(this).attr('base-tabindex') || $(this).attr('tabindex'));
                    // 表示モードではない時
                    if (!$(this).data('show-model')) {
                        clearbtn = $(clearbtn);
                        clearbtn.addClass(fr);
                        $(this).after(clearbtn);
                        clearbtn.data('source', this);
                        $(this).data('clear-icon', clearbtn);
                    }
                }
            }
            else if (!$(this).hasClass('tcc-popup')) {
                var clearbtn = $(this).parent().find('.popclear');
                clearbtn.data('source', this);
                $(this).data('clear-icon', clearbtn);
            }

            // 検索ボタン追加
            if (!$(this).nextAll().hasClass('popsearch')) {
                if ($(this).attr("popSearch") && $(this).attr("popSearch").toLowerCase() == "true") {
                    //var search = popsearchbtn.replace('{0}', $(this).attr("master")).replace('{1}', $(this).attr('name')).replace('{2}', $(this).attr('actionHandle')).replace('{3}', $(this).attr("poprefdata"));
                    var popSearchText = $(this).attr("popSearchText");

                    if (popSearchText == undefined) {
                        var search = TorihikiUtils.format(popsearchbtn, $(this).attr("master"), $(this).attr('name'), $(this).attr('actionHandle'),
                            typeof ($(this).attr("poprefdata")) == "undefined" ? "" : $(this).attr("poprefdata"), $(this).attr('base-tabindex') || $(this).attr('tabindex'));
                    } else {
                        var popSearchCss = $(this).attr("popSearchCss");
                        if (popSearchCss == undefined) popSearchCss = "";//background-color:white;color:green;
                        if (popSearchText == undefined) popSearchText = "";

                        var search = TorihikiUtils.format(popsearchbtn2, $(this).attr("master"), $(this).attr('name'), $(this).attr('actionHandle'),
                            typeof ($(this).attr("poprefdata")) == "undefined" ? "" : $(this).attr("poprefdata"), popSearchCss, popSearchText, $(this).attr('base-tabindex') || $(this).attr('tabindex'));
                    }

                    // データ元
                    var search = $(search);
                    search.data('source', this);
                    $(this).data('search-icon', search);

                    // データが設定された判断
                    if ($(this).attr("poprefdata")) {
                        //var poprefdatakeys = $(actionelement).attr("poprefdata").split(',');
                        var poprefdata = $(this).attr("poprefdata");
                        if (poprefdata.indexOf('selected') > -1) {
                            var refdata = TorihikiUtils.convertRefDataToParams(this, this, poprefdata);

                            // データが設定された
                            if (typeof refdata.selected !== 'undefined') {
                                if (refdata.selected != '') {
                                    $(search).removeClass('tcc-btn-outline-green').addClass('tcc-btn-green');
                                } else {
                                    $(search).removeClass('tcc-btn-green').addClass('tcc-btn-outline-green');
                                }
                            }
                        }
                    }

                    var popiconloc = $(this).attr("popiconloc");
                    if (popiconloc && popiconloc.toLowerCase() == 'left') {
                        var icon = $(this).before($(search)).prev();
                        //if ($(this).hasClass('tcc-input-combo')) {
                        //  $(icon).addClass('float-left');
                        //}
                    } else {
                        var icon = $(this).after(search).next();
                    }

                    if ($(this).hasClass('tcc-input-combo')) {
                        $(icon).addClass('searchicon');
                    }
                    if ($(this).hasClass('tcc-float-right')) {
                        $(icon).addClass('tcc-float-right');
                    }
                    // 表示モード時
                    if ($(this).data('show-model')) {
                        $(icon).remove();
                    }
                }
            }
            else if(!$(this).hasClass('tcc-popup')) {
                var search = $(this).parent().find('.popsearch');
                search.data('source', this);
                $(this).data('search-icon', search);
            }

            // 追加ボタン追加
            if (!$(this).nextAll().hasClass('popadd')) {
                if ($(this).attr("dataAdd") && $(this).attr("dataAdd").toLowerCase() == "true") {
                    //var add = popaddbtn.replace('{0}', $(this).attr("master")).replace('{1}', $(this).attr('name')).replace('{2}', $(this).attr('actionHandle')).replace('{3}', $(this).attr("poprefdata"));
                    var add = TorihikiUtils.format(popaddbtn, $(this).attr("master"), $(this).attr('name'), $(this).attr('actionHandle'),
                        typeof ($(this).attr("poprefdata")) == "undefined" ? "" : $(this).attr("poprefdata"), $(this).attr('base-tabindex') || $(this).attr('tabindex'));
                    // データ元
                    var add = $(add);
                    add.data('source', this);
                    $(this).data('add-icon', add);
                    $(this).after(add);
                    // 表示モード時
                    if ($(this).data('show-model')) {
                        $(add).remove();
                    }
                }
            }
            else if (!$(this).hasClass('tcc-popup')) {
                var addbtn = $(this).parent().find('.popadd');
                addbtn.data('popadd', this);
            }

            // 明細ボタン追加
            if (!$(this).nextAll().hasClass('popdetail')) {
                if ($(this).attr("showDetail") && $(this).attr("showDetail").toLowerCase() == "true") {
                    //var add = popaddbtn.replace('{0}', $(this).attr("master")).replace('{1}', $(this).attr('name')).replace('{2}', $(this).attr('actionHandle')).replace('{3}', $(this).attr("poprefdata"));
                    var detailbtn = TorihikiUtils.format(popdetailbtn, $(this).attr("master"), $(this).attr('name'), $(this).attr('actionHandle'),
                        typeof ($(this).attr("poprefdata")) == "undefined" ? "" : $(this).attr("poprefdata"), $(this).attr('base-tabindex') || $(this).attr('tabindex'));
                    $(this).after(detailbtn);
                }
            }
            else if (!$(this).hasClass('tcc-popup')) {
                var detailbtn = $(this).parent().find('.popdetail');
                detailbtn.data('popadd', this);
            }
        });

        // マスタ検索ボタン設定
        content.find('.popsearch').each(function () {
            $(this).off('click').on('click', function (t) {
                var source = $(this).data('source');
                // コントロールが使用不可の時
                if ($(source).data('disabled')) return;

                TorihikiUtils.readPopInfo(this, 'search');
            });
        });

        // 追加ボタン設定
        content.find('.popadd').each(function () {
            $(this).off('click').on('click', function (t) {
                var source = $(this).data('source');
                // コントロールが使用不可の時
                if ($(source).data('disabled')) return;

                TorihikiUtils.readPopInfo(this, 'add');
            });
        });
        // 詳細表示の方法を変更したので、コメントアウト
        //// 詳細表示リンク設定
        //content.find('.popdetail').each(function () {
        //    $(this).off('click').on('click', function (t) {
        //        var url = $(this).attr("master");
        //        var action = $(this).attr("master");
        //        var actionelement = $('[name="' + $(this).attr("action-handle-element") + '"]')[0];

        //        var params = {};
        //        if (this.hasAttribute("poprefdata") && $(this).attr("poprefdata") != "") {
        //            //var poprefdatakeys = $(actionelement).attr("poprefdata").split(',');
        //            var poprefdata = $(this).attr("poprefdata");
        //            params = TorihikiUtils.convertRefDataToParams(this, this, poprefdata);
        //        }
        //        params[$(actionelement).attr("dataField")] = $(actionelement).val();

        //        TorihikiUtils.showDetailPopup({
        //            url: popDetailUrl + "/" + url,
        //            action: action,
        //            params: params,
        //            callback: function (data) {
        //                return false;
        //            }
        //        });

        //        return false;
        //    });
        //});

        // クリアボタン設定
        content.find('.popclear').each(function () {
            $(this).off('click').on('click', function (t) {
                var actionHandle = $(this).attr("action-handle-source");
                var master = $(this).attr("master");

                var actionSource = $(this).data('source');

                // クリア前チェック
                var beforeClear = $(actionSource).attr('beforeClear');

                // クリア後処理
                var afterClear = $(actionSource).attr('afterClear');
                if (typeof beforeClear !== 'undefined') {
                    var beforeClearEvent = beforeClear;
                    var isAsync = false;
                    if (beforeClear.indexOf(':') > -1) {
                        beforeClearEvent = beforeClear.split(':')[0].trim();
                        isAsync = true;
                    }
                    if (!isAsync) {
                        if (!TorihikiUtils.execCallback(beforeClearEvent, { element: actionSource, actionhandle: actionHandle })) return false;
                    } else {
                        TorihikiUtils.hideLoading();
                        return new Promise((resolve, reject) => {
                            TorihikiUtils.execCallbackAsync(beforeClearEvent, resolve, reject, { element: actionSource, actionhandle: actionHandle });
                        }).then(data => {
                            if (data) {
                                // クリア
                                TorihikiUtils.clearPopData(this);

                                // クリア後処理
                                if (typeof afterClear !== 'undefined') {
                                    TorihikiUtils.execPopCallback(afterClear, master, null, actionHandle);
                                }

                                TorihikiUtils.afterClearPopData(this);
                            }
                        });
                        return;
                    }
                }

                // クリア
                TorihikiUtils.clearPopData(this);

                // 検索コントロール連動
                if ($(actionSource).hasClass('tcc-search-ctl')) {
                    var searcherea = $(actionSource).parent();
                    if (!searcherea.find('.tcc-search-ctl-name-edit').hasClass('hidden')) {
                        searcherea.find('.tcc-search-ctl-kana').show();
                        searcherea.find('.tcc-search-ctl-name-edit').show().trigger('change');
                        searcherea.find('.tcc-search-ctl-name-show').hide();
                    }

                    // マックを非表示になる
                    var tccBadgeCtl = searcherea.find('[data-tcc-badge]').data('tcc-badge-ctl');
                    if (typeof tccBadgeCtl !== 'undefined') {
                        tccBadgeCtl.hide();
                    }
                }

                // クリア後処理
                if (typeof afterClear !== 'undefined') {
                    TorihikiUtils.execPopCallback(afterClear, master, null, actionHandle);
                }

                TorihikiUtils.afterClearPopData(this);
            });
        });

        // 詳細表示リンク設定
        TorihikiUtils.setPopDetail(content);

        // リンクポップアップ画面呼出
        TorihikiUtils.setLinkPopUp(content);

        // データ連動
        TorihikiUtils.setLinkage(content);

        // ポップアップ画面の閉じるボタン設定
        content.find('#popCloseBtn').click(function () {
            parent.$('#popModal').modal('hide');
        });

        if (content.find('#isPopAdd').length > 0) {
            var ispopadd = document.createElement('input');
            $(ispopadd).attr("name", "IsPopAdd");
            $(ispopadd).attr("type", "hidden");
            $(ispopadd).val($('#isPopAdd').val());
            $('form').append(ispopadd);
        }

    },
    /**
     * 画面のコントロールより詳細ポップアップハンドルを設定する
     * @param {any} container
     */
    setPopDetail: function (container) {
        var content;
        if (container) {
            content = $(container);
        } else {
            content = $(document);
        }

        // 詳細表示リンク設定
        content.find('.popdetail-link').each(function () {
            $(this).off('click').on('click', function (t) {
                return new Promise(resolve => {
                    var url = $(this).attr("master");
                    var posturl = '';
                    var action = $(this).attr("master");
                    var poprefdata = $(this).attr("poprefdata");

                    var setting = popDetailPageSetting[action];
                    //if (typeof setting === 'undefined' || typeof setting.action === 'undefined' || setting.action == '') return;

                    switch (action) {
                        case 'comment':
                            var data = TorihikiUtils.convertCommentParams(this);
                            var shanaiMemoControl = $(this).data('shanai-memo-element');
                            TorihikiUtils.showCommentPopup({
                                url: CommentUrl,
                                data: data,
                                stylecontrol: this,
                                shanaiMemoControl: shanaiMemoControl,
                                callback: function (data) {
                                }
                            });
                            return;
                    }
                    var params = {};
                    if (this.hasAttribute("poprefdata") && $(this).attr("poprefdata") != "") {
                        //var poprefdatakeys = $(actionelement).attr("poprefdata").split(',');
                        var poprefdata = $(this).attr("poprefdata");
                        params = TorihikiUtils.convertRefDataToParams(this, this, poprefdata);
                    }

                    var paramsIsNull = true;
                    for (let [key, value] of Object.entries(params)) {
                        if (typeof value != 'undefined' && value != null && value.trim() != '') {
                            paramsIsNull = false;
                        }
                    }

                    if ($(this).text().trim() == '' && paramsIsNull) {
                        return;
                    }

                    // モック画面全部実装済み後、else分岐を参照してください。
                    if (setting && (setting.controller || TorihikiGlobal.isDebug)) {
                        posturl = SiteStartPath + '/' + (typeof setting.area === 'undefined' ? popDetailArea : setting.area) + '/' + setting.controller;
                        TorihikiUtils.window.TorihikiUtils.showDetailPopup({
                            url: posturl,
                            action: action,
                            params: params,
                            callback: function (data) {
                                return false;
                            }
                        });

                    } else {
                        // TorihikiMsg.showAlertLabel({ message: messages.ControllerSettingError, time: 2000 });
                        // ロジック修正
                        TorihikiUtils.hideLoading();
                        TorihikiUtils.hideLoadingImage();
                        return false; // 実装ロジック修正
                        //posturl = popDetailUrl + "/" + action;

                        //TorihikiUtils.window.TorihikiUtils.showDetailPopup({
                        //    url: posturl,
                        //    action: action,
                        //    params: params,
                        //    callback: function (data) {
                        //        return false;
                        //    }
                        //});
                    }

                    return false;
                }).catch(res => {
                    console.log('catch', res);
                    TorihikiUtils.hideLoading();
                    TorihikiUtils.hideLoadingImage();
                });
            });
        });

    },
    /**
     * リンクでポップアップ画面を呼出
     * @param {any} container
     */
    setLinkPopUp: function (container) {
        var content;
        if (container) {
            content = $(container);
        } else {
            content = $(document);
        }
        content.find(".tcc-link-popup").off('click').on('click', function () {
            return new Promise(resolve => {
                var ctl = this;
                var url = $(ctl).attr("master");
                // マスタ属性がない時
                if (typeof (url) == "undefined" || url == "") {
                    return;
                }
                var action = "";
                var type = "post";

                action = $(ctl).attr("master");
                var actionHandle = $(ctl).attr("action-handle");
                if (actionHandle == undefined) actionHandle = $(ctl).attr("actionHandle");

                TorihikiUtils.showLoading();
                var params = {};
                if (ctl.hasAttribute("poprefdata") && $(ctl).attr("poprefdata") != "") {
                    var poprefdata = $(ctl).attr("poprefdata");
                    params = TorihikiUtils.convertRefDataToParams(ctl, ctl, poprefdata);
                }

                var post_options = {
                    url: popSearchArea + "/View/" + action,
                    params: params,
                    action: action,
                    actionHandle: actionHandle,
                }

                // ポップアップパラメータ
                TorihikiUtils.makePopUpOptions(post_options);

                if (post_options && (post_options.controller || TorihikiGlobal.isDebug)) {
                    post_options.url = SiteStartPath + '/' + (typeof post_options.area === 'undefined' ? popSearchArea : post_options.area) + '/' + post_options.controller;
                } else {
                    // TorihikiMsg.showAlertLabel({ message: messages.ControllerSettingError, time: 2000 });
                    // ロジック修正
                    TorihikiUtils.hideLoading();
                    TorihikiUtils.hideLoadingImage();
                    return false;
                }

                // コミット
                TorihikiUtils.popSearchPostAction(post_options);

            }).catch(res => {
                console.log('catch', res);
                TorihikiUtils.hideLoading();
                TorihikiUtils.hideLoadingImage();
            });
        });
    },
    /**
     * 参照データをパラメータに変換する。
     * @param {any} ctl
     * @param {any} actionelement
     * @param {any} pop_refdata
     */
    convertRefDataToParams: function (ctl, actionelement, pop_refdata) {
        var poprefdata = pop_refdata;
        if (poprefdata == undefined) return {};
        var params = {};
        if (!TorihikiUtils.isObj(poprefdata)) {
            if (poprefdata.indexOf('{') == -1) {
                var poprefdatakeys = poprefdata.split(',');
                // ループ
                $(poprefdatakeys).each(function (j, s) {
                    var elename = foolproof.getName(ctl, s);
                    params[s] = $('[name="' + elename + '"]').val();
                });

                return params;
            } else {
                poprefdata = JSON.parse(poprefdata);
            }
        }

        for (var key in poprefdata) {
            var value = poprefdata[key];
            if (value.indexOf('#') != -1 || value.indexOf('$') != -1) {
                var poprefdatakeys = value.split(',');
                var paramval = "";
                // ループ
                $(poprefdatakeys).each(function (j, s) {
                    if (value.indexOf('$') != -1) {
                        var elename = value.replace('$', '');
                    }
                    else if (actionelement == undefined) {
                        if ($(ctl).attr('name') != undefined) {
                            var elename = foolproof.getName(ctl, value.replace('#', ''));
                        } else {
                            var elename = value.replace('#', '');
                        }
                    } else {
                        if ($(actionelement).attr('name') != undefined) {
                            var elename = foolproof.getName(actionelement, value.replace('#', ''));
                        } else {
                            var elename = value.replace('#', '');
                            // 明細テーブル時
                            var $tr = $(ctl).closest('tr');
                            if ($tr.length != 0) {
                                var nameCtl = undefined;
                                $tr.find('input,select').each(function (x, ele) {
                                    if (ele.name.indexOf(".") > -1) {
                                        nameCtl = ele;
                                        return true;
                                    }
                                })
                                if (nameCtl != undefined) {
                                    elename = foolproof.getName(nameCtl, value.replace('#', ''));
                                }
                            }
                        }
                    }
                    var form = $(ctl).closest('form');
                    // フォームが存在すれば
                    var valuectl;
                    if (form.length > 0 && form.find('[name="' + elename + '"]').length > 0) {
                        valuectl = form.find('[name="' + elename + '"]');
                    } else {
                        valuectl = $('[name="' + elename + '"]');
                    }
                    paramval += (paramval == '' ? '' : '_') + valuectl.length == 0 ? '' : valuectl.val();
                });
                params[key] = paramval;
            } else {
                params[key] = value;
            }
        }

        return params;
    },
    /**
     * 検索ポップアップ画面を表示する
     * @param {any} ctl
     * @param {any} subaction
     */
    readPopInfo: function (ctl, subaction) {
        var self = this;
        // セッションステータスチェック
        return new Promise((resolve, reject) => {
            self.checkSessionState(resolve, reject);
        }).then(data => {
            self.readPopInfoBody(ctl, subaction);
        }).catch(res => {
            console.log('catch', res);
            TorihikiUtils.hideLoading();
            TorihikiUtils.hideLoadingImage();
        });
    },
    /**
     * 検索ポップアップ画面を表示する
     * @param {any} ctl
     * @param {any} subaction
     */
    readPopInfoBody: function (ctl, subaction, noBeforeSearchCheck) {
        var self = this;
        var url = $(ctl).attr("master");
        // マスタ属性がない時
        if (typeof (url) == "undefined" || url == "") {
            return;
        }
        var action = "";
        var type = "post";
        action = $(ctl).attr("master");

        var curwindow = this;

        var actionHandle = $(ctl).attr("action-handle-source");
        $('#popModal').attr("poptarget", actionHandle);
        $('#popModal').attr("popaddtarget", "");
        var actionelement = $('[name="' + $(ctl).attr("action-handle-element") + '"]')[0];
        var actionSource = $(ctl).data('source');

        if (typeof noBeforeSearchCheck === 'undefined') {
            // 検索前チェック
            var beforeSearch = $(actionSource).attr('beforeSearch');
            if (typeof beforeSearch !== 'undefined') {
                var beforeSearchEvent = beforeSearch;
                var isAsync = false;
                if (beforeSearch.indexOf(':') > -1) {
                    beforeSearchEvent = beforeSearch.split(':')[0].trim();
                    isAsync = true;
                }
                if (!isAsync) {
                    if (!self.execCallback(beforeSearchEvent, { element: actionelement, actionhandle: actionHandle })) return false;
                } else {
                    TorihikiUtils.hideLoading();
                    return new Promise((resolve, reject) => {
                        self.execCallbackAsync(beforeSearchEvent, resolve, reject, { element: actionelement, actionhandle: actionHandle });
                    }).then(data => {
                        if (data) {
                            self.readPopInfoBody(ctl, subaction, true);
                        }
                    });
                    return;
                }
            }
        }

        TorihikiUtils.showLoading();
        //poptarget = $.find("[actionHandle='" + actionHandle + "']");
        var params = {};
        if (ctl.hasAttribute("poprefdata") && $(ctl).attr("poprefdata") != "") {
            //var poprefdatakeys = $(actionelement).attr("poprefdata").split(',');
            var poprefdata = $(ctl).attr("poprefdata");
            params = TorihikiUtils.convertRefDataToParams(ctl, actionelement, poprefdata);

            // ポップアップ参照データをクリア必要時
            if ($(ctl).data('clear-poprefdata')) {
                $(ctl).attr('poprefdata', '');
                $(ctl).data('clear-poprefdata', false);
            }
        }

        var post_options = {
            url: popSearchArea + "/View/" + action,
            params: params,
            action: action,
            actionHandle: actionHandle,
        }

        // ポップアップパラメータ
        TorihikiUtils.makePopUpOptions(post_options);

        if (post_options && (post_options.controller || TorihikiGlobal.isDebug)) {
            post_options.url = SiteStartPath + '/' + (typeof post_options.area === 'undefined' ? popSearchArea : post_options.area) + '/' + post_options.controller;
        } else {
            // TorihikiMsg.showAlertLabel({ message: messages.ControllerSettingError, time: 2000 });
            // ロジック修正
            TorihikiUtils.hideLoading();
            TorihikiUtils.hideLoadingImage();
            return false;
        }

        // コミット
        self.popSearchPostAction(post_options);
    },
    /**
     * 検索ポップアップ画面Post送信.
     * @param {any} options
     */
    popSearchPostAction: function (options) {
        var defaults = {
            width: 800,
            height: 700,
            opentype: 'iframe',
            actionHandle: undefined,
            controller: undefined,
        };
        $.extend(defaults, options);

        var popModelid = TorihikiUtils.getRandomId('popModal');
        var popIframeid = popModelid.replace('popModal', 'ifrmpop');
        var popdiv = TorihikiUtils.window.$('#popModal').clone().attr('id', popModelid).removeAttr('data-backdrop').addClass('tcc-popsearch');
        popdiv.empty();
        popdiv[0].style.zIndex = TorihikiUtils.newPopUpZIndex();
        TorihikiUtils.window.$('#popModal').after(popdiv);
        // 呼出元
        popdiv.data("popsource-document", document);
        // 子画面で呼出時
        if (TorihikiUtils.isPopUpWindow()) {
            TorihikiUtils.addCoverDiv(window.name);
        }
        // 呼出元
        popdiv.data("popsource-window", window);
        // アクションハンドル
        popdiv.attr("poptarget", defaults.actionHandle);
        popdiv.attr("popaddtarget", "");

        if (defaults.opentype == 'iframe') {
            if (defaults.width.toString().indexOf('%') == -1) {
                defaults.width += 'px';
            }
            if (defaults.height.toString().indexOf('%') == -1) {
                defaults.height += 'px';
            }
            var divdata = '<iframe frameborder="0" id="' + popIframeid + '" name="' + popIframeid + '" scrolling="auto" style="width:100%;height:92%;"></iframe>';
            divdata = '<div class="modal-dialog">' + divdata + '</div>';

            popdiv.html(divdata);
            popdiv.find('.modal-dialog').addClass('tcc-popsearch-' + defaults.action);

            // CSS で設定するので、コメントアウト
            //// 幅・高さ
            //popdiv.find('.modal-dialog')[0].style.maxWidth = defaults.width;
            //popdiv.find('.modal-dialog')[0].style.maxHeight = defaults.height;

            popdiv.modal("show");

            popdiv.on('hide.bs.modal', function () {
                // 子画面で呼出時
                if (popdiv.data("popsource-window").name.indexOf('ifrmpop') > -1) {
                    popdiv.data("popsource-window").TorihikiUtils.removeCoverDiv(popdiv.data("popsource-window").name);
                }
                popdiv.remove();
                TorihikiUtils.hideLoading();
            });

        } else {
            if (screen.availWidth < defaults.width) defaults.width = screen.availWidth;
            if (screen.availHeight - 60 < defaults.height) defaults.height = screen.availHeight - 60;
            var win = window.open(defaults.url, defaults.opentype, 'width=' + defaults.width + ',height=' + defaults.height);
        }
        // 仮フォーム作成
        var form = document.createElement("form");
        $(form).addClass('hidden');
        var textbox;
        //// キーでコントロール作成
        //for (var key in defaults.params) {
        //    key = key.trim();
        //    textbox = '<input type=textbox name="' + key + '" id="' + key + '" value=\'' + defaults.params[key] + '\' />';
        //    $(form).append(textbox);
        //}
        TorihikiUtils.addFormItem(form, defaults.params);
        textbox = '<input type=textbox name="master" id="master" value=\'' + defaults.action + '\' />';
        $(form).append(textbox);
        textbox = '<input type=textbox name="condtionkey" id="condtionkey" value=\'' + JSON.stringify(defaults.params) + '\' />';
        $(form).append(textbox);
        textbox = '<input type=textbox name="actionHandle" id="actionHandle" value=\'' + defaults.actionHandle + '\' />';
        $(form).append(textbox);

        // Tokenを追加
        $(form).append($(TorihikiGlobal.requestVerificationToken).last().clone());

        if (defaults.opentype == 'iframe') {
            form.target = popIframeid;
        } else {
            form.target = defaults.opentype;
            win.focus();
        }
        form.action = TorihikiUtils.addPageSessionId(defaults.url);
        form.method = 'post';
        TorihikiUtils.window.$(document.body).append(form);
        $(form).submit();

        if (defaults.opentype == 'iframe') {
            setTimeout(function () {
                TorihikiUtils.checkLoadingItlIframe = setInterval(function () { TorihikiUtils.checkLoadingIframe(popIframeid, defaults.url); }, 500);
            }, 500);
        }

        // ポップアップ表示されない場合が存在するので、表示失敗場合、再度コミットする。（edge devのみで存在する）
        if (TorihikiUtils.browser.isEdge) {
            var $form = $(form);
            var resubmit = function () {
                if (defaults.opentype == 'iframe') {
                    if (TorihikiUtils.isPopUpWindow(this)) {
                        var ifloc = parent.frames[popIframeid].location.href;
                    } else {
                        var ifloc = frames[popIframeid].location.href;
                    }
                    if ('about:blank' == ifloc) {
                        $form.submit();
                    }
                } else {
                    var ifloc = win.location.href;
                    if ('about:blank' == ifloc) {
                        $form.submit();
                    }
                }
                $form.remove();
            }
            setTimeout(resubmit, 200);
        } else {
            $(form).remove();
        }
    },
    /**
     * ポップアップ画面で選択されたデータを画面に設定する
     * @param {any} master
     * @param {any} row
     * @param {any} actionHandle
     * @param {any} targetContainer
     */
    setSelectedDataOnPopUp: function (master, row, actionHandle, targetContainer) {
        if (row == undefined) {
            console.log('No Selected Data');
            return;
        }

        // サブ画面の名前判断
        if (window.name == '' || window.name == 'ifrm-0000') {
            if (targetContainer == undefined) {
                var targetContainer = $(document);
                var popcontain = $('#popModal');
            }
        } else if (targetContainer == undefined) {
            var popcontainer = '#' + window.name.replace('ifrmpop', 'popModal');
            var popModal = TorihikiUtils.window.$(popcontainer);
            var targetContainer = $(popModal.data("popsource-document"));
            var targetWindow = popModal.data("popsource-window");
            //console.log(targetWindow)

            actionHandle = popModal.attr("poptarget");

            // 呼出元画面のデータ設定処理呼出
            targetWindow.TorihikiUtils.setSelectedDataOnPopUp(master, row, actionHandle, targetContainer);

            // ローディング非表示
            targetWindow.TorihikiUtils.hideLoading();

            TorihikiUtils.window.$(popcontainer).modal('hide');
            if (TorihikiUtils != undefined) TorihikiUtils.window.$(popcontainer).remove();
            return;
        }

        // 画面別に設定処理があれば
        if (typeof setSelectedDataOnPage !== 'undefined' && typeof setSelectedDataOnPage === 'function') {
            setSelectedDataOnPage(master, row, actionHandle, targetContainer);
            return;
        }

        // データ設定
        TorihikiUtils.setSelectedData(master, row, actionHandle, targetContainer);
    },
    /**
     * ポップアップ画面で選択されたデータを画面に設定する
     * @param {any} master
     * @param {any} row
     * @param {any} actionHandle
     * @param {any} targetContainer
     */
    setSelectedData: function (master, row, actionHandle, targetContainer) {
        if (row == undefined) {
            console.log('No Selected Data');
            return;
        }

        // 複数行時、一行目データ使用だけ
        if (row.length > 0) row = row[0];

        if (targetContainer == undefined) {
            var targetContainer = $(document);
            var popcontain = $('#popModal');
        }

        if (actionHandle == undefined) {
            var achd = "[actionHandle='" + popcontain.attr("poptarget") + "']";
            achd += ",[actionHandle2='" + popcontain.attr("poptarget") + "']";
            //achd += ",[actionHandle^='" + popcontain.attr("poptarget") + ",']"; // 前 actionHandle = "{poptarget},xxx"
            //achd += ",[actionHandle$='," + popcontain.attr("poptarget") + "']"; // 後 actionHandle = "xxx,{poptarget}"
            //achd += ",[actionHandle*='," + popcontain.attr("poptarget") + ",']"; // LIKE actionHandle = "xxx,{poptarget},xxx"
            var poptargetitems = targetContainer.find(achd);
            //var poptargetitems = targetContainer.find("[actionHandle='" + popcontain.attr("poptarget") + "']");
            var action_control = targetContainer.find('[action-handle-source="' + popcontain.attr("poptarget") + '"]');
        } else {
            var achd = "[actionHandle='" + actionHandle + "']";
            achd += ",[actionHandle2='" + actionHandle + "']";
            //achd += ",[actionHandle^='" + actionHandle + ",']"; // 前
            //achd += ",[actionHandle$='," + actionHandle + "']"; // 後
            //achd += ",[actionHandle*='," + actionHandle + ",']"; // LIKE
            var poptargetitems = targetContainer.find(achd);
            //var poptargetitems = targetContainer.find("[actionHandle='" + actionHandle + "']");
            var action_control = targetContainer.find('[action-handle-source="' + actionHandle + '"]');
        }
        // 色設定
        if (action_control.length > 0) {
            var act_ctl = action_control[0];
            switch (act_ctl.tagName.toLowerCase()) {
                case 'button':
                    if (row.selecteddata != undefined && (row.selecteddata.length > 0 || TorihikiUtils.isObj(row.selecteddata))) {
                        $(act_ctl).removeClass('tcc-btn-outline-green').addClass('tcc-btn-green');
                    } else {
                        $(act_ctl).removeClass('tcc-btn-green').addClass('tcc-btn-outline-green');
                    }
                    break;
                default:
                    break;
            }
        }
        $(poptargetitems).each(function () {
            var dataField = $(this).attr("dataField");
            var form = $(this).closest('form');
            var data = '';
            var orsplit = '/';
            if (dataField != undefined) {
                if (dataField.indexOf(orsplit) > -1) {
                    $.each(dataField.split(orsplit), function (i, df) {
                        if (row[df] != undefined) {
                            data = row[df];
                            return false;
                        }
                    });
                } else {
                    data = row[dataField];
                }
            }
            switch (this.tagName.toLowerCase()) {
                case "select":
                    if (data != $(this).val()) {
                        $(this).val(data);
                        $(this).trigger('change.select');
                        $(this).trigger('change');
                    }
                    if (form.length > 0) $(this).valid();
                    break;
                case "input":
                    switch (this.type.toLowerCase()) {
                        case "checkbox":
                            $(this).removeAttr("checked");
                            this.checked = false;
                            if ($(this).val() == data) {
                                $(this).attr("checked", true);
                            }
                            break;
                        case "radio":
                            var elename = $(this).attr("name");
                            $("input[type=radio][name='" + elename + "']").each(function () {
                                $(this).removeAttr("checked");
                                if ($(this).val() == data) {
                                    $(this).attr("checked", true);
                                }
                            });
                            break;
                        default:
                            if (data != $(this).val()) {
                                var before = $(this).val();
                                $(this).val(data);
                                // 数値
                                if ($(this).hasClass('tcc-input-number')) {
                                    if ($(this).val() == "") {
                                        return;
                                    }
                                    var scale = $(this).attr("scale");
                                    if (!scale) {
                                        scale = 0;
                                    }
                                    $(this).val($.number($(this).val(), scale));
                                    if (before != $(this).val()) {
                                        $(this).trigger('change');
                                    }
                                }
                                else {
                                    switch (master) {
                                        case 'yubinbangou': // 郵便番号
                                            $(this).data('un-linkage', true);
                                            break;
                                        default:
                                            break;
                                    }
                                    $(this).trigger('change');
                                }
                                if (form.length > 0) $(this).valid();
                                break;
                            }
                            break;
                    }
                    break;
                case "span":
                case "div":
                case "label":
                case "a":
                    if ($(this).attr("dataField") != undefined) {
                        var datafield = $(this).attr("dataField").split('+');
                        var datatext = '';
                        $(datafield).each(function (i, field) {
                            if ($.trim(field) != "") {
                                if (row[field] != undefined) datatext += row[field];
                            } else {
                                datatext += field;
                            }
                        });
                        $(this).text(datatext);
                    }
                    break;
                case "textarea":
                    $(this).val(data);
                    break;
            }
        });
        if (targetContainer != undefined) {
        } else {
            $('#popModal').modal('hide');
            $('#popModal').empty();
        }

        // 検索コントロール連動
        var actionelement = $('[actionHandle="' + actionHandle + '"]');
        if (actionelement.hasClass('tcc-search-ctl')) {
            actionelement.parent().find('.tcc-search-ctl-kana').hide();
            var nameedit = actionelement.parent().find('.tcc-search-ctl-name-edit')
            if (!nameedit.data('keep-nameedit')) {
                nameedit.hide();
                actionelement.parent().find('.tcc-search-ctl-name-show').show().css("display", "inline-flex");
                // リンクコントロールにフォーカスする
                $('a[actionHandle="' + actionHandle + '"]').first().focus();
            } else {
                // リンクコントロールにフォーカスする
                nameedit.focus().select();
            }
        } else {
            // リンクコントロールにフォーカスする
            $('a[actionHandle="' + actionHandle + '"]').first().focus();
        }

        // 画面別に設定処理があれば
        if (typeof afterSetSelectedDataOnPage !== 'undefined' && typeof afterSetSelectedDataOnPage === 'function') {
            afterSetSelectedDataOnPage(master, row, actionHandle, targetContainer);
        }

        // 画面別ワークフロー連動処理があれば
        if (typeof afterSetSelectedDataOnPageForWorkFlow !== 'undefined' && typeof afterSetSelectedDataOnPageForWorkFlow === 'function') {
            afterSetSelectedDataOnPageForWorkFlow(master, row, actionHandle, targetContainer);
        }
    },
    /**
     * 追加されたデータを画面のコンボボックスに追加する
     * @param {any} row
     */
    comboAddOption: function (row) {

        var popaddtargetitem = $('#popModal').attr("popaddtarget");
        // オプション追加
        if ($("select[name='" + popaddtargetitem + "']").length > 0) {
            var ele = $("select[name='" + popaddtargetitem + "']");
            var opt = $('<option>', {
                text: row.option['text'], value: row.option['value'], selected: true
            });
            $(opt).attr("data-tokens", row.option['tokens']);
            $(ele).append(opt).trigger('comboselect:update');
            //.trigger('change.select');
        }

        TorihikiUtils.setSelectedData(row.data);

        //var poptargetitems = $.find("[actionHandle='" + $('#popModal').attr("poptarget") + "']");
        //$(poptargetitems).each(function () {
        //    $(this).val(row.data[$(this).attr("dataField")]);
        //    if (this.type.indexOf('select') > -1) {
        //        $(this).trigger('change.select');
        //    }
        //});

        $('#popModal').modal('hide');
    },
    /**
     * ポップアップと関連するコントロールのデータをクリアする。
     * @param {any} ctl
     */
    clearPopData: function (ctl) {

        var actionHandle = $(ctl).attr("action-handle-source");
        var actionelement = $('[name="' + $(ctl).attr("action-handle-element") + '"]')[0];

        var poptargetitems = $.find("[actionHandle='" + actionHandle + "'][datafield],[actionHandle='" + actionHandle + "'][dataField]");
        $(poptargetitems).each(function () {
            switch (this.tagName.toLowerCase()) {
                case "select":
                    $(this).val('');
                    $(this).trigger('change.select');
                    $(this).trigger('change');
                    break;
                case "input":
                    switch (this.type.toLowerCase()) {
                        case "checkbox":
                            $(this).removeAttr("checked");
                            this.checked = false;
                            break;
                        case "radio":
                            var elename = $(this).attr("name");
                            $("input[type=radio][name='" + elename + "']").each(function () {
                                $(this).removeAttr("checked");
                                $(this).attr("checked", false);
                            });
                            break;
                        default:
                            $(this).val('');
                            break;
                    }
                    break;
                case "textarea":
                    $(this).val('');
                    break;
                case "span":
                case "div":
                case "label":
                case "a":
                    $(this).text('');
                    break;

            }
        });
    },
    /**
     * ポップアップと関連するコントロールのクリア後処理。
     * @param {any} ctl
     */
    afterClearPopData: function (ctl) {

        var actionHandle = $(ctl).attr("action-handle-source");
        var poptargetitems = $.find("[actionHandle='" + actionHandle + "']");

        if (!$(".tcc-message-area").is(":hidden")) {
            $(poptargetitems).each(function () {
                switch (this.tagName.toLowerCase()) {
                    case "select":
                    case "input":
                    case "textarea":
                        $(this).valid();
                        break;
                }
            });
        }
    },
    /**
     * 検索ポップアップ画面を表示する
     * @param {any} ctl
     * @param {any} subaction
     */
    showDetailPopup: function (options) {
        var self = this;
        // セッションステータスチェック
        return new Promise((resolve, reject) => {
            self.checkSessionState(resolve, reject);
        }).then(data => {
            self.showDetailPopupBody(options);
        });
    },

    /**
      * ガイダンスポップアップ画面を表示する
      * @param {any} options
      */
    showGuidancePopup: function (options) {

        var defaults = { url: null, width: undefined };
        defaults = $.extend(defaults, options);

        var popModelid = TorihikiUtils.getRandomId('popDetail');
        var popIframeid = popModelid.replace('popDetail', 'ifrmDetail');

        // コンテナー
        var messagecontainer = '<div class="modal tcc-popup-right fade" id="' + popModelid + '" tabindex="-1" role="dialog" aria-labelledby="msg" aria-hidden="true">';
        //messagecontainer += '<div class="modal-dialog" ' + (defaults.width ? 'style="width:' + defaults.width + '">' : '>');
        messagecontainer += '<div class="modal-dialog">';
        messagecontainer += '<div class="modal-content">';
        messagecontainer += '<div class="modal-body">';
        messagecontainer += '</div>';
        messagecontainer += '</div></div></div>';

        TorihikiUtils.window.$('body').append(messagecontainer);

        var popdiv = TorihikiUtils.window.$('#' + popModelid).addClass('tcc-popup-detail');
        var popdivBody = popdiv.find('.modal-body');
        popdivBody.empty();

        popdiv[0].style.zIndex = TorihikiUtils.newPopDetailZIndex();

        var divdata = '<iframe frameborder="0" id="' + popIframeid + '" name="' + popIframeid + '"  src="' + defaults.url + '" scrolling="auto" style="width:100%;height:92%;"></iframe>';
        popdivBody.html(divdata);
        popdiv.find('.modal-dialog').addClass('tcc-popup-detail-guidance');
        popdiv.modal("show");

    },

    /**
     * 詳細ポップアップ画面を表示する
     * @param {any} options
     */
    showDetailPopupBody: function (options) {

        var defaults = { url: null, action: null, params: null, type: 'post', multiple: '0', callback: null, width: undefined };
        defaults = $.extend(defaults, options);
        var rect = popDetailPageSetting[defaults.action];

        var popModelid = TorihikiUtils.getRandomId('popDetail');
        var popIframeid = popModelid.replace('popDetail', 'ifrmDetail');

        // コンテナー
        var messagecontainer = '<div class="modal tcc-popup-right fade" id="' + popModelid +'" tabindex="-1" role="dialog" aria-labelledby="msg" aria-hidden="true">';
        messagecontainer += '<div class="modal-dialog" ' + (defaults.width ? 'style="width:' + defaults.width + '">' : '>');
        messagecontainer += '<div class="modal-content">';
        //messagecontainer += '<div class="modal-header">';
        //messagecontainer += '<h3 class="tcc-group-title modal-title" id="myModalLabel">コメント</h3>';
        //messagecontainer += '<a class="close" style="cursor:pointer;" data-dismiss="modal" aria-hidden="true"><i class="fa fa-times"></i></a>';
        //messagecontainer += '</div>';
        messagecontainer += '<div class="modal-body">';
        messagecontainer += '</div>';
        messagecontainer += '</div></div></div>';

        TorihikiUtils.window.$('body').append(messagecontainer);

        var popdiv = TorihikiUtils.window.$('#' + popModelid).addClass('tcc-popup-detail');
        var popdivBody = popdiv.find('.modal-body');

        popdivBody.empty();
        popdiv[0].style.zIndex = TorihikiUtils.newPopDetailZIndex();
        // 呼出元
        popdiv.data("popsource-document", document);
        // 呼出元
        popdiv.data("popsource-window", window);
        // 子画面で呼出時
        if (TorihikiUtils.isPopUpWindow()) {
            TorihikiUtils.addCoverDiv(window.name);
        }

        if (defaults.width && defaults.width.toString().indexOf('%') == -1) {
            defaults.width += 'px';
        }

        var divdata = '<iframe frameborder="0" id="' + popIframeid + '" name="' + popIframeid + '" scrolling="auto" style="width:100%;height:92%;"></iframe>';
        popdivBody.html(divdata);
        popdiv.find('.modal-dialog').addClass('tcc-popup-detail-' + defaults.action);

        // CSS で設定するので、コメントアウト
        //// 幅・高さ
        //popdiv.find('.modal-dialog')[0].style.maxWidth = defaults.width;
        //popdiv.find('.modal-dialog')[0].style.maxHeight = defaults.height;

        popdiv.modal("show");

        popdiv.on('hide.bs.modal', function () {
            // 子画面で呼出時
            if (popdiv.data("popsource-window").name.indexOf('ifrmpop') > -1) {
                popdiv.data("popsource-window").TorihikiUtils.removeCoverDiv(popdiv.data("popsource-window").name);
            }
            popdiv.remove();
            TorihikiUtils.hideLoading();
            if (defaults.callback) {
                defaults.callback();
            }
        });

        // 仮フォーム作成
        var form = document.createElement("form");
        $(form).addClass('hidden');
        //var textbox;
        //// キーでコントロール作成
        //for (var key in defaults.params) {
        //    key = key.trim();
        //    textbox = '<input type=textbox name="' + key + '" id="' + key + '" value=\'' + defaults.params[key] + '\' />';
        //    $(form).append(textbox);
        //}
        TorihikiUtils.addFormItem(form, defaults.params);
        //textbox = '<input type=textbox name="master" id="master" value=\'' + defaults.action + '\' />';
        //$(form).append(textbox);
        //textbox = '<input type=textbox name="condtionkey" id="condtionkey" value=\'' + JSON.stringify(defaults.params) + '\' />';
        //$(form).append(textbox);

        // Tokenを追加
        $(form).append($(TorihikiGlobal.requestVerificationToken).last().clone());

        form.target = popIframeid;
        form.action = TorihikiUtils.addPageSessionId(defaults.url);
        form.method = 'post';
        TorihikiUtils.window.$(document.body).append(form);
        $(form).submit();

        setTimeout(function () {
            TorihikiUtils.checkLoadingItlIframe = setInterval(function () { TorihikiUtils.checkLoadingIframe(popIframeid, defaults.url); }, 500);
        }, 500);

        // ポップアップ表示されない場合が存在するので、表示失敗場合、再度コミットする。（edge devのみで存在する）
        if (TorihikiUtils.browser.isEdge) {
            var $form = $(form);
            var resubmit = function () {
                if (TorihikiUtils.isPopUpWindow(this)) {
                    var ifloc = parent.frames[popIframeid].location.href;
                } else {
                    var ifloc = frames[popIframeid].location.href;
                }
                if ('about:blank' == ifloc) {
                    $form.submit();
                }
                $form.remove();
            }
            setTimeout(resubmit, 200);
        } else {
            $(form).remove();
        }
    },
    /**
     * サブポップアップ画面を表示する
     * @param {any} options
     */
    showEditPopup: function (options) {
        var self = this;
        // ポップアップログイン画面時、ポップアップ処理本体呼出
        if (options.url == SessionTimeOutPopUrl) {
            self.showEditPopupBody(options);
        } else {
            // セッションステータスチェック
            return new Promise((resolve, reject) => {
                self.checkSessionState(resolve, reject);
            }).then(data => {
                self.showEditPopupBody(options);
            }).catch(res => {
                console.log('catch', res);
                TorihikiUtils.hideLoading();
                TorihikiUtils.hideLoadingImage();
            });
        }
    },
    /**
     * サブポップアップ画面を表示する
     * @param {any} options
     */
    showEditPopupBody: function (options) {
        var defaults = {
            url: null,
            action: null,
            screensizekbn: null,    // 1:big 2:medium 3:small
            params: null,
            type: 'post',
            multiple: '0',
            width: '80%',
            height: 'calc(100% - 80px)',
            callback: null,
            success: null,
            failed: null,
            copysource: '#popModal',
            opentype: 'iframe',
            modeless: true,
        };
        defaults = $.extend(defaults, options);

        TorihikiUtils.window.TorihikiUtils.showLoading();

        if (defaults.action != null && defaults.url == null) {
            $.extend(defaults, popSearchPageSetting[defaults.action]);

            if (defaults.controller || TorihikiGlobal.isDebug) {
                defaults.url = SiteStartPath + '/' + (typeof defaults.area === 'undefined' ? popSearchArea : defaults.area) + '/' + defaults.controller;
            }
        }

        if (defaults.width.toString().indexOf('%') == -1) {
            defaults.width += 'px';
        }
        if (defaults.height.toString().indexOf('%') == -1) {
            defaults.height += 'px';
        }
        var popModelid = TorihikiUtils.getRandomId('popModal');
        var popIframeid = popModelid.replace('popModal', 'ifrmpop');
        if (TorihikiUtils.window.$(defaults.copysource).length == 0) {
            defaults.copysource = '#popModalTop';
        }
        if (TorihikiUtils.window.$(defaults.copysource).length == 0) {
            defaults.copysource = '#popModal';
        }
        var popdiv = TorihikiUtils.window.$(defaults.copysource).clone().attr('id', popModelid).removeAttr('data-backdrop').addClass('tcc-popsearch').empty();
        // モデルモード時
        if (!defaults.modeless) {
            popdiv.attr('data-backdrop', 'static');
        }
        TorihikiUtils.window.$(defaults.copysource).after(popdiv);
        popdiv[0].style.zIndex = TorihikiUtils.newPopUpZIndex();
        //$(defaults.copysource).after(popdiv);
        // 呼出元
        popdiv.data("popsource-document", document);
        // 呼出元
        popdiv.data("popsource-window", this);
        // 子画面で呼出時
        if (TorihikiUtils.isPopUpWindow()) {
            TorihikiUtils.addCoverDiv(window.name);
        }

        if (defaults.opentype == 'iframe') {
            if (defaults.type == 'post') {
                var divdata = '<iframe frameborder="0" id="' + popIframeid + '" name="' + popIframeid + '" scrolling="auto" style="width:100%;height:92%;"></iframe>';
                divdata = '<div class="modal-dialog">' + divdata + '</div>';

                popdiv.html(divdata);
                if (defaults.action != null) {
                    popdiv.find('.modal-dialog').addClass('tcc-popsearch-' + defaults.action);
                }
                else if (defaults.screensizekbn != null) {
                    switch (defaults.screensizekbn) {
                        case '1':
                            popdiv.find('.modal-dialog').addClass('tcc-pop-screensize-big');
                            break;
                        case '2':
                            popdiv.find('.modal-dialog').addClass('tcc-pop-screensize-medium');
                            break;
                        case '3':
                            popdiv.find('.modal-dialog').addClass('tcc-pop-screensize-small');
                            break;
                        default:
                            null;
                    }
                }
                else {
                    popdiv.find('.modal-dialog')[0].style.maxWidth = defaults.width;
                    popdiv.find('.modal-dialog')[0].style.maxHeight = defaults.height;
                }

                popdiv.modal("show");
                // 仮フォーム作成
                var form = document.createElement("form");
                //// キーでコントロール作成
                //for (var key in defaults.params) {
                //    key = key.trim();
                //    textbox = '<input type=textbox name="' + key + '" id="' + key + '" value="' + defaults.params[key] + '" />';
                //    $(form).append(textbox);
                //}
                TorihikiUtils.addFormItem(form, defaults.params)

                // Tokenを追加
                $(form).append($(TorihikiGlobal.requestVerificationToken).last().clone());

                form.target = popIframeid;
                form.action = TorihikiUtils.addPageSessionId(defaults.url);
                form.method = 'post';
                TorihikiUtils.window.$(document.body).append(form);
                $(form).submit();
                $(form).remove();
            }
            else {
                defaults.url = TorihikiUtils.addPageSessionId(defaults.url);
                var param = TorihikiUtils.parseParam(defaults.params);
                var coparam = "";
                if (param != "") {
                    if (defaults.url.indexOf('?') > -1) {
                        coparam = "&";
                    } else {
                        coparam = "?";
                    }
                }

                var divdata = '<iframe frameborder="0" id="' + popIframeid + '" name="' + popIframeid + '" scrolling="auto" style="width:100%;height:92%;" src="'
                    + defaults.url + coparam + param + '"></iframe>';
                divdata = '<div class="modal-dialog">' + divdata + '</div>';

                popdiv.html(divdata);
                if (defaults.action != null) {
                    popdiv.find('.modal-dialog').addClass('tcc-popsearch-' + defaults.action);
                }
                else if (defaults.screensizekbn != null) {
                    switch (defaults.screensizekbn) {
                        case '1':
                            popdiv.find('.modal-dialog').addClass('tcc-pop-screensize-big');
                            break;
                        case '2':
                            popdiv.find('.modal-dialog').addClass('tcc-pop-screensize-medium');
                            break;
                        case '3':
                            popdiv.find('.modal-dialog').addClass('tcc-pop-screensize-small');
                            break;
                        default:
                            null;
                    }
                }
                else {
                    popdiv.find('.modal-dialog')[0].style.maxWidth = defaults.width;
                    popdiv.find('.modal-dialog')[0].style.maxHeight = defaults.height;
                }
                popdiv.modal("show");
            }

            var popIframe = TorihikiUtils.window.frames[popIframeid];

            popdiv.off('hide.bs.modal').on('hide.bs.modal', function () {

                TorihikiUtils.window.TorihikiUtils.hideLoading();

                if (popIframe.TorihikiUtils != undefined && popIframe.TorihikiUtils.popresult != undefined && popIframe.TorihikiUtils.popresult) {
                    if (defaults.callback) {
                        defaults.callback(popIframe.TorihikiUtils.popresult_data);
                    }
                    if (defaults.success) {
                        defaults.success(popIframe.TorihikiUtils.popresult_data);
                    }
                } else if (popIframe.TorihikiUtils != undefined && popIframe.TorihikiUtils.popresult != undefined && !popIframe.TorihikiUtils.popresult) {
                    if (defaults.callback) {
                        defaults.callback(popIframe.TorihikiUtils.popresult_data);
                    }
                    if (defaults.failed) {
                        defaults.failed(popIframe.TorihikiUtils.popresult_data);
                    }
                } else if (typeof defaults.close === 'function') {
                    defaults.close();
                }
                popdiv.remove();
            });
        }
        else {
            var loc_top = 0;
            var loc_left = 0;
            if (options.width == undefined) {
                defaults.width = screen.availWidth * 0.8;
            } else {
                if (screen.availWidth < defaults.width) defaults.width = screen.availWidth;
            }
            if (options.height == undefined) {
                defaults.height = screen.availHeight - 80;
            } else {
                if (screen.availHeight - 60 < defaults.height) defaults.height = screen.availHeight - 60;
            }

            loc_top = (screen.availHeight - defaults.height) / 2;
            loc_left = (screen.availWidth - defaults.width) / 2;
            var win = window.open(defaults.url, defaults.opentype, 'width=' + defaults.width + ',height=' + defaults.height + ',top=' + loc_top + ',left=' + loc_left);

            // 仮フォーム作成
            var form = document.createElement("form");
            TorihikiUtils.addFormItem(form, defaults.params)

            // Tokenを追加
            $(form).append($(TorihikiGlobal.requestVerificationToken).last().clone());

            form.target = defaults.opentype;
            form.action = TorihikiUtils.addPageSessionId(defaults.url);
            form.method = 'post';
            TorihikiUtils.window.$(document.body).append(form);
            $(form).submit();
            $(form).remove();
            win.focus();

            var timer = setInterval(function () {
                if (win.closed) {
                    clearInterval(timer);

                    if (win.TorihikiUtils != undefined && win.TorihikiUtils.popresult != undefined && win.TorihikiUtils.popresult) {
                        if (defaults.callback) {
                            defaults.callback(win.TorihikiUtils.popresult_data);
                        }
                        if (defaults.success) {
                            defaults.success(win.TorihikiUtils.popresult_data);
                        }
                    } else if (win.TorihikiUtils != undefined && win.TorihikiUtils.popresult != undefined && !win.TorihikiUtils.popresult) {
                        if (defaults.callback) {
                            defaults.callback(win.TorihikiUtils.popresult_data);
                        }
                        if (defaults.failed) {
                            defaults.failed(win.TorihikiUtils.popresult_data);
                        }
                    }
                }
            }, 100);
        }

        TorihikiUtils.window.TorihikiUtils.hideLoading();
    },

    /**
     * ポップアップ画面のＩＤ取得
     * */
    getPopModalId: function () {
        return '#' + window.name.replace('ifrmpop', 'popModal');;
    },
    /**
     * ポップアップ画面のＩＤ取得
     * */
    getPopDetailModalId: function () {
        return '#' + window.name.replace('ifrmDetail', 'popDetail');;
    },
    /**
     * ポップアップ画面の閉じる処理
     * @param {any} result
     */
    hidePopModalSelf: function (result) {
        if (result != undefined) {
            TorihikiUtils.popresult = result.success;
            if (typeof result.data == 'string') {
                TorihikiUtils.popresult_data = JSON.parse(result.data);
            } else {
                TorihikiUtils.popresult_data = result.data;
            }
        }
        parent.$(TorihikiUtils.getPopModalId()).modal('hide');
    },
    /**
     * ポップアップ参照画面の閉じる処理
     * @param {any} result
     */
    hidePopDetailModalSelf: function (result) {
        if (result != undefined) {
            TorihikiUtils.popresult = result.success;
            if (typeof result.data == 'string') {
                TorihikiUtils.popresult_data = JSON.parse(result.data);
            } else {
                TorihikiUtils.popresult_data = result.data;
            }
        }
        parent.$(TorihikiUtils.getPopDetailModalId()).modal('hide');
    },
    /**
     * ポップアップパラメータ作成
     * @param {any} post_options
     */
    makePopUpOptions: function (postOptions) {
        $.extend(postOptions, popSearchPageSetting[postOptions.action]);
    },
    /**
     * ポップアップ検索パラメータ.
     * @param {any} params
     */
    makePopSearchQueryParams: function (params, popform) {
        query = TorihikiUtils.makePopSearchSearchCondition(popform);
        query.limit = params.limit;
        query.offset = params.offset;
        query.keyword = params.search;
        query.sortOrder = params.order;
        query.sortName = params.sort;
        return query;
    },
    /**
   * ポップアップ検索情報取得.
   * @param {any} params
   */
    makePopSearchSearchCondition: function (popform) {
        if (typeof popform === "undefined") popform = "#searchform";
        query = {};
        var condtionkey = {};
        if (baseCondtionkey != undefined && baseCondtionkey != "") {
            $.extend(condtionkey, JSON.parse(baseCondtionkey));
        }
        if (condtionkey.selected != undefined) {
            condtionkey.selected = "";
        }
        var params = TorihikiUtils.serializeObject($(popform));
        $.extend(condtionkey, params);
        query.condtionkey = JSON.stringify(condtionkey);

        if (typeof query.__RequestVerificationToken === 'undefined') {
            query.__RequestVerificationToken = $(TorihikiGlobal.requestVerificationToken).last().val();
        }

        query.master = master;
        query.PageNumber = pageIndex;
        query.PageSize = pageSize;

        return query;
    },
    // =================================================================
    // =====================画面セッション処理===========================
    // =================================================================   
    gLocalKey: "TorihikiMvc",
    gPageSessionKey: "TorihikiMvcPage",
    CurrentDataKey: "TorihikiMvcData",
    /**
     * 一つ目フォームのデータを取得する。
     * @param {any} key
     */
    getPageData: function (key, form) {
        var $form;
        if (typeof form === 'undefined') {
            $form = $("form");
        } else {
            $form = $(form);
        }
        var jsonData = TorihikiUtils.serializeObject($form);

        // 検索コントロール情報保存
        TorihikiUtils.saveSeachCtlInfo($form, jsonData);

        return jsonData;
    },
    /**
     * 一つ目フォームのデータをキーでセッションに保存する。
     * @param {any} key
     */
    saveData: function (key, form) {
        var $form;
        if (typeof form === 'undefined') {
            $form = $("form");
        } else {
            $form = $(form);
        }
        var jsonData = TorihikiUtils.serializeObject($form);

        // 検索コントロール情報保存
        TorihikiUtils.saveSeachCtlInfo($form, jsonData);

        sessionStorage.setItem(key, JSON.stringify(jsonData));
    },
    /**
     * キーでセッションデータをクリアする。
     * @param {any} key
     */
    removeData: function (key) {

        sessionStorage.removeItem(key);

    },
    /**
     * キーでデータをセッションに保存する。
     * @param {any} key
     */
    saveSessionData: function (key, value) {
        if (typeof value === "string") {
            sessionStorage.setItem(key, value);
        } else {
            sessionStorage.setItem(key, JSON.stringify(value));
        }
    },
    /**
     * キーでセッションからデータをJSONで戻る。
     * @param {any} key
     */
    getSessionDataOfJson: function (key) {
        return JSON.parse(sessionStorage.getItem(key));
    },
    /**
     * キーでセッションからデータを戻る。
     * @param {any} key
     */
    getSessionData: function (key) {
        return sessionStorage.getItem(key);
    },
    /**
     * キーでセッションからデータを取得し、画面に表示する。
     * @param {any} key
     */
    setDataBySessionData: function (key, removedata) {

        if (sessionStorage.getItem(key) == null || sessionStorage.getItem(key) == undefined || sessionStorage.getItem(key) == 'undefined') return;
        var savedData = JSON.parse(sessionStorage.getItem(key));

        $.each(savedData, function (n, o) {

            if (n == "__RequestVerificationToken") return;

            var obj = $("[name='" + n + "']");

            if (obj.length > 0 && (obj[0].type == 'radio' || obj[0].type == 'checkbox')) {

                obj.removeAttr('checked');

                switch (obj[0].type) {
                    case 'radio':
                        $("input[type=radio][name='" + n + "']").each(function () {
                            if ($(this).val() == o) {
                                $(this).attr("checked", true);
                                $(this).prop("checked", true);
                                $(this).trigger("change");
                            }
                        });
                        //$("input[type=radio][name='" + n + "']").val(o);
                        break;
                    case 'checkbox':
                        var arr = o.split(',');
                        $.each(arr, function (i, v) {
                            $("input[type=checkbox][name='" + n + "']").each(function () {
                                if ($(this).val() == v) {
                                    $(this).attr("checked", true);
                                    $(this).prop("checked", true);
                                    if (!$(this).hasClass('.tcc-checkbox-list-all')) {
                                        $(this).trigger("change");
                                    }
                                }
                            });
                        });
                        break;
                }

            } else {
                if (obj.is(".tcc-input-combo")) {
                    obj.val(o).trigger("change");
                } else if (obj.is(".tcc-input-date")) {
                    obj.val(o);
                    obj.bsdatepicker('update');
                } else {
                    obj.val(o);
                }
            }
        });
        if (typeof removedata === 'undefined' || removedata) {
            sessionStorage.removeItem(key);
        }
    },
    /**
     * キーで画面の初期値を保持する。
     * @param {any} key
     */
    savePageInitData: function (key, form) {
        var self = this;
        if (typeof key === 'undefined') {
            self.pageInitKey = document.location.href;
        } else {
            self.pageInitKey = key;
        }
        // 初期データ保存
        TorihikiUtils.saveData(self.pageInitKey, form);
    },
    /**
     * 保持した画面の初期値を取得し、画面に表示する。
     * @param {any} key
     */
    setInitData: function (key, form) {
        var self = this;
        var $form;
        if (typeof form === 'undefined') {
            $form = $("form");
        } else {
            $form = $(form);
        }

        var initKey = key;
        if (typeof initKey === 'undefined') {
            initKey = self.pageInitKey;
        }
        if (sessionStorage.getItem(initKey) == null || sessionStorage.getItem(initKey) == undefined || sessionStorage.getItem(initKey) == 'undefined') return;
        var savedData = JSON.parse(sessionStorage.getItem(initKey));

        $.each(savedData, function (n, o) {

            if (n == "__RequestVerificationToken") return;

            var obj = $form.find("[name='" + n + "']");

            if (obj.length > 0 && (obj[0].type == 'radio' || obj[0].type == 'checkbox')) {

                obj.removeAttr('checked');
                obj.prop("checked", false);

                switch (obj[0].type) {
                    case 'radio':
                        $form.find("input[type=radio][name='" + n + "']").each(function () {
                            if ($(this).val() == o) {
                                $(this).attr("checked", "true");
                                $(this).prop("checked", true);
                                $(this).trigger("change");
                            }
                        });
                        break;
                    case 'checkbox':
                        var arr = o.split(',');
                        $.each(arr, function (i, v) {
                            $form.find("input[type=checkbox][name='" + n + "']").each(function () {
                                if ($(this).val() == v) {
                                    $(this).attr("checked", true);
                                    $(this).prop("checked", true);
                                    $(this).trigger("change");
                                }
                            });
                        });
                        break;
                }

            } else {
                if (obj.is(".tcc-input-combo")) {
                    obj.val(o).trigger("change");
                } else if (obj.is(".tcc-input-date")) {
                    obj.val(o);
                    obj.bsdatepicker('update');
                } else {
                    obj.val(o);
                }
            }
        });
    },
    /**
     * セッションデータを削除する。
     * @param {any} key
     */
    removeSessionData: function (key) {
        sessionStorage.removeItem(key);
    },
    /**
     * キーでデータをローカルに保存する。
     * @param {any} key
     */
    saveLocalData: function (key, value) {
        if (typeof value === "string") {
            localStorage.setItem(key, value);
        } else {
            localStorage.setItem(key, JSON.stringify(value));
        }
    },
    /**
     * キーでローカルからデータをJSONで戻る。
     * @param {any} key
     */
    getLocalDataOfJson: function (key) {
        return JSON.parse(localStorage.getItem(key));
    },
    /**
     * キーでローカルデータを戻る。
     * @param {any} key
     */
    getLocalData: function (key) {
        return localStorage.getItem(key);
    },
    /**
     * キーでローカルデータを取得し、画面に表示する。
     * @param {any} key
     */
    setDataByLocalData: function (key) {

        var savedData = JSON.parse(localStorage.getItem(key));

        $.each(savedData, function (n, o) {
            var obj = $("[name='" + n + "']");

            if (obj.length > 1) {

                obj.removeAttr('checked');
                var arr = o.split(',');
                $.each(arr, function (i, v) {
                    $("input[type=checkbox][name='" + n + "']").each(function () {
                        if ($(this).val() == v) {
                            $(this).attr("checked", true);

                        }
                    });
                });

            } else {
                if (obj.is(".tcc-input-combo")) {
                    obj.val(o).trigger("change");
                } else if (obj.is(".tcc-input-date")) {
                    obj.val(o);
                    obj.bsdatepicker('update');
                } else {
                    obj.val(o);
                }
            }
        });
        localStorage.removeItem(key);
    },
    /**
     * コンテナーのデータをセッションに保存する。
     * @param {any} container
     */
    saveCurrentData: function (container) {
        if (typeof container === "undefined") {
            container = $('form')[0];
        }

        var params = TorihikiUtils.serializeObject(container);
        this.saveSessionData(TorihikiUtils.CurrentDataKey, params);
    },
    /**
     * 先に保存したデータ戻る。
     */
    getPreviousDataOfJson: function () {
        return this.getSessionDataOfJson(TorihikiUtils.CurrentDataKey);
    },
    /**
     * 先に保存したデータ戻る。
     */
    getPreviousData: function () {
        return this.getSessionData(TorihikiUtils.CurrentDataKey, params);
    },

    // =================================================================
    // =====================ツールチップ処理=============================
    // =================================================================  
    /**
     * ツールチップ設定.
     * @param {any} container
     */
    setTooltip: function (container) {
        var content;
        if (container) {
            content = $(container);
        } else {
            content = $(document);
        }

        var defaultWhiteList = $.fn.tooltip.Constructor.Default.whiteList
        // To allow elements
        defaultWhiteList.table = [];
        defaultWhiteList.thead = [];
        defaultWhiteList.tbody = [];
        defaultWhiteList.tr = [];
        defaultWhiteList.th = [];
        defaultWhiteList.td = [];
        defaultWhiteList.label = [];
        defaultWhiteList.input = ['type'];

        setTimeout(function () {
            content.find('[tooltip]').each(function () {
                $(this).mouseenter(function () {
                    $(this).tooltip({
                        title: function () {
                            if ($(this).attr('_titleToolTip') != undefined) {
                                return $(this).attr('_titleToolTip');
                            }
                            return TorihikiUtils.getContentHtmlForTooltip($(this).attr("_functionTooltip"), $(this).attr("_paramTooltip"));
                        },
                        html: true,
                        placement: "bottom",
                    });
                    $(this).tooltip('show');
                });
            });
        }, 200);
    },
    /**
     * ツールチップの内容を取得する。
     * @param {any} fuct
     * @param {any} param
     */
    getContentHtmlForTooltip: function (fuct, param) {
        if (param == undefined) {
            return eval(fuct);
        } else {
            return eval(fuct + param);
        }
    },

    // =================================================================
    // =====================ファイルダウンロード処理======================
    // =================================================================
    /**
     * Base64をBlobに変換する。
     * @param {any} base64
     * @param {any} mimetype
     * @param {any} slicesize
     */
    base64ToBlob: function (base64, mimetype, slicesize) {
        if (!window.atob || !window.Uint8Array) {
            // The current browser doesn't have the atob function. Cannot continue
            return null;
        }
        mimetype = mimetype || '';
        slicesize = slicesize || 512;
        var bytechars = atob(base64);
        var bytearrays = [];
        for (var offset = 0; offset < bytechars.length; offset += slicesize) {
            var slice = bytechars.slice(offset, offset + slicesize);
            var bytenums = new Array(slice.length);
            for (var i = 0; i < slice.length; i++) {
                bytenums[i] = slice.charCodeAt(i);
            }
            var bytearray = new Uint8Array(bytenums);
            bytearrays[bytearrays.length] = bytearray;
        }
        return new Blob(bytearrays, { type: mimetype });
    },
    /**
     * ObjectURLを解放する.
     * @param {any} url
     * @param {any} timeout_flg
     */
    destoryObjectURL: function (url, timeout_flg) {
        // 二回目時
        if (timeout_flg) {
            window.URL.revokeObjectURL(url);
            return;
        }

        // URL destory
        setTimeout(function () {
            TorihikiUtils.destoryObjectURL(url, true);
        }, 2000);
    },
    /**
     * 印刷プレビュー（サーバ戻る結果はBase64）
     * @param {any} data
     */
    printFileByBase64String: function (options) {
        // プレビュー
        var tempframe = 'print-dummy-iframe';
        $('#' + tempframe).remove();
        var iframepdf = document.createElement('iframe');
        $(iframepdf).attr('name', tempframe);
        $(iframepdf).attr('id', tempframe);
        $(iframepdf).addClass('hidden');
        var url = URL.createObjectURL(TorihikiUtils.base64ToBlob(options.file.data, options.file.mimetype));
        $(iframepdf).attr('src', url);

        $(iframepdf).bind('load', function () {
            iframepdf.contentWindow.print();
        });
        // 作成したコントロール画面に追加
        $(document.body).append(iframepdf);

        if (options.message) {
            TorihikiMsg.message({
                message: options.message,
                callback: options.callback,
                callbackdata: options.callbackdata
            });
        } else if (options.callback) {
            options.callback(options.callbackdata);
        }

        // URL destory
        TorihikiUtils.destoryObjectURL(url);

        // ローディング非表示
        TorihikiUtils.hideLoading();
    },
    /**
     * ファイルダウンロード（サーバ戻る結果はBase64）
     * @param {any} file
     */
    downloadFileByBase64String: function (options) {
        var a = document.createElement('a');
        if (window.URL && window.Blob && ('download' in a) && window.atob) {
            // Do it the HTML5 compliant way
            var blob = TorihikiUtils.base64ToBlob(options.file.data, options.file.mimetype);
            var url = window.URL.createObjectURL(blob);
            a.href = url;
            a.download = options.file.filename;
            a.click();

            if (options.callback) {
                options.callback();
            }

            // URL destory
            TorihikiUtils.destoryObjectURL(url);

            // ローディング非表示
            TorihikiUtils.hideLoading();
        }
    },

    downloadNormal: function (blob, fileName) {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        return true;
    },

    saveFileDownload: async function (data, fileName, contentType = '') {
        let blob = data;
        if (contentType !== '') {
            if (contentType === 'text/plain') {
                blob = new Blob([data], { type: 'text/plain' });
            } else {
            blob = TorihikiUtils.base64ToBlob(data, contentType);
            }
        }
        if (window.location.protocol == 'https:') {
            const fileHandle = await window.showSaveFilePicker({
                suggestedName: fileName
            }).catch(ex => {
                if (ex.name === 'AbortError') {
                    return false;
                }
                return TorihikiUtils.downloadNormal(blob, fileName);
            })
            if (fileHandle) {
                const fileStream = await fileHandle.createWritable();
                await fileStream.write(blob);
                await fileStream.close();
                return true;
            }
            return false
        } else {
            return TorihikiUtils.downloadNormal(blob, fileName);
        }
    },
    /**
     * 印刷プレビュー（サーバ戻る結果はバイナリ）
     * @param {any} options
     */
    printFileByBinary: function (options) {
        var request = new XMLHttpRequest();
        request.responseType = "blob";
        request.open("POST", TorihikiUtils.addPageSessionId(options.url), true);
        request.send(options.param);
        request.onload = function () {

            //var newBlob = new Blob([data], { type: "application/pdf" });
            //if (window.navigator && window.navigator.msSaveOrOpenBlob) {
            //  window.navigator.msSaveOrOpenBlob(newBlob);
            //  return;
            //} 
            // プレビュー
            var resonse = this.response;

            // 結果はhtml時
            if (resonse.type.indexOf('application/json') > -1) {
                TorihikiUtils.showDownloadMessage(resonse);
                return;
            }

            var tempframe = 'print-dummy-iframe';
            $('#' + tempframe).remove();
            var iframepdf = document.createElement('iframe');
            $(iframepdf).attr('name', tempframe);
            $(iframepdf).attr('id', tempframe);
            $(iframepdf).addClass('hidden');
            var url = window.URL.createObjectURL(this.response);
            $(iframepdf).attr('src', url);

            $(iframepdf).bind('load', function () {
                // 結果はhtml時
                if (resonse.type == 'text/html') return;

                iframepdf.contentWindow.print();

            });

            // 作成したコントロール画面に追加
            $(document.body).append(iframepdf);

            if (options.callback) {
                options.callback();
            }

            // URL destory
            TorihikiUtils.destoryObjectURL(url);

            // ローディング非表示
            TorihikiUtils.hideLoading();
        }
    },
    /**
     * ファイルダウンロードサーバ戻る結果はバイナリ）
     * @param {any} options
     */
    downloadFileByBinary: function (options) {
        var request = new XMLHttpRequest();
        request.responseType = "blob";
        request.open("POST", TorihikiUtils.addPageSessionId(options.url), true);
        // データフォーム作成
        var form = document.createElement('form');
        if (options.setdata) {
            options.setdata(form);
        }
        if (options.data) {
            TorihikiUtils.addFormItem(form, options.data);
        }
        if ($(form).find(TorihikiGlobal.requestVerificationToken).length == 0) {
            $(form).appendToken();
        }
        var data2 = TorihikiUtils.getFormData($(form));
        request.send(data2);
        // フォーム削除
        $(form).remove();
        request.onload = function (data) {

            // プレビュー
            var response = this.response;

            if (response.type.indexOf('application/json') > -1) {
                TorihikiUtils.showDownloadMessage(response);
                return;
            }
            if (response.type.indexOf('text/html') > -1) {
                TorihikiUtils.showDownloadError(response);
                return;
            }

            var a = document.createElement('a');
            var url = window.URL.createObjectURL(this.response);
            a.href = url;
            var filename = TorihikiUtils.getDownloadFileName(request);
            a.download = filename;
            a.click();

            // 実行完了メッセージ表示する時
            if (options.showSuccessMessage != undefined && options.showSuccessMessage) {
                TorihikiMsg.message({
                    message: 'KYI000005',
                    args: '印刷処理',
                    callback: function () {

                        // 成功後処理があれば
                        if (typeof options.success !== 'undefined') {
                            if (typeof options.success === 'function') {
                                options.success(options.data);
                            } else {
                                TorihikiUtils.execCallback(options.success, options.data);
                            }
                        }

                        if (typeof options.complete !== 'undefined') {
                            if (typeof options.complete === 'function') {
                                options.complete();
                            } else {
                                TorihikiUtils.execCallback(options.complete);
                            }
                        }
                    }
                });
            } else {
                // 成功後処理があれば
                if (typeof options.success !== 'undefined') {
                    if (typeof options.success === 'function') {
                        options.success(options.data);
                    } else {
                        TorihikiUtils.execCallback(options.success, options.data);
                    }
                }

                if (typeof options.complete !== 'undefined') {
                    if (typeof options.complete === 'function') {
                        options.complete();
                    } else {
                        TorihikiUtils.execCallback(options.complete);
                    }
                }
            }

            // URL destory
            TorihikiUtils.destoryObjectURL(url);

            // ローディング非表示
            TorihikiUtils.hideLoading();
        }
    },
    /**
     * サーバで作成したファイルを開ける（サーバ戻る結果はサーバURL）
     * @param {any} options
     */
    openServerFile: function (options) {

        TorihikiUtils.ajaxEx({
            url: options.url,
            printSessionKey: options.printSessionKey,
            keepLoading: options.keepLoading,
            data: options.data,
            showSuccessMessage: options.showSuccessMessage,
            success: function (data) {

                if (!TorihikiUtils.isNullOrEmptyOrWhiteSpace(data.downloadfileurl)) {
                    // 作成したファイルを開ける
                    window.open(data.downloadfileurl);
                }

                if (typeof options.success !== 'undefined') {
                    if (typeof options.success === 'function') {
                        options.success(data);
                    } else {
                        TorihikiUtils.execCallback(options.success, data);
                    }
                }
            },
            failed: function (data) {
                if (typeof options.failed !== 'undefined') {
                    if (typeof options.failed === 'function') {
                        options.failed(data);
                    } else {
                        TorihikiUtils.execCallback(options.failed, data);
                    }
                }
            },
            complete: function (data) {
                if (typeof options.complete !== 'undefined') {
                    if (typeof options.complete === 'function') {
                        options.complete(data);
                    } else {
                        TorihikiUtils.execCallback(options.complete, data);
                    }
                }
            }
        })
    },
    /**
     * ダウンロードのエラーメッセージ表示.
     * @param {any} response
     */
    showDownloadMessage: function (response) {
        var fr = new FileReader();
        fr.onload = function (evt) {
            var res = evt.target.result;

            var result = JSON.parse(res);

            if (result.message == null && result.data != undefined && result.data != null) {
                TorihikiMsg.alert(result);
            } else {
                if (result.success) {
                    TorihikiMsg.show({
                        message: result.message,
                        callback: result.callback,
                        callbackdata: result.callbackdata
                    });
                } else {
                    TorihikiMsg.alert({
                        message: result.message,
                        callback: result.callback,
                        callbackdata: result.callbackdata
                    });
                }
                // ローディング非表示
                TorihikiUtils.hideLoading();
            }
            return;
        };
        fr.readAsText(response);
    },
    /**
     * ダウンロードのエラーメッセージ表示.
     * @param {any} response
     */
    showDownloadError: function (response) {
        var fr = new FileReader();
        fr.onload = function (evt) {
            var res = evt.target.result;
            var response = {
                status: 500,
                responseText: res,
            }
            TorihikiUtils.showErrors(response);
            // ローディング非表示
            TorihikiUtils.hideLoading();
            return;
        };
        fr.readAsText(response);
    },
    /**
     * ダウンロードのファイル名を取得する.
     * @param {any} request
     */
    getDownloadFileName: function (request) {
        var filename = request.getResponseHeader("Content-disposition");
        var utf8key = "filename*=UTF-8''";
        var utf8Index = filename.lastIndexOf(utf8key);
        if (utf8Index != -1) {
            filename = filename.substring(utf8Index + utf8key.length, filename.length);
        } else {
            var equalIndex = filename.lastIndexOf('=');
            if (equalIndex != -1) {
                filename = filename.substring(equalIndex + 1, filename.length);
            } else {
                filename = filename.substring(21, filename.length);
            }
        }
        filename = decodeURIComponent(filename);
        return filename;
    },
    /**
     * 伝票印刷
     * @param {any} options
     */
    dempyoPrint: function (options) {

        var printDataUrl = options.url;
        var printParams = options.params;
        var printSuccess = options.success;
        var printCanceled = options.canceled;
        var printComplete = options.complete;
        var printFailed = options.failed;
        var printException = options.exception;
        var showExMessage = options.showExMessage;
        var printSessionKey = options.sessionKey;
        var showSuccessMessage = options.showSuccessMessage;
        var printtype = options.printType || 'download';
        var printer = options.printer;
        // パラメーターは関数の場合
        if (typeof printSessionKey === 'function') printSessionKey = printSessionKey();

        if (typeof printParams === 'undefined') printParams = {};
        // パラメーターは関数の場合
        if (typeof printParams === 'function') printParams = printParams();
        // プリントタイプをパラメータに追加する。
        printParams.printType = printtype
        // パラメーターは直接印刷した時、システムエラーのメッセージを表示するか制御
        if (typeof showExMessage === 'undefined') showExMessage = true;
        // 出力
        switch (printtype) {
            case 'client':
                TorihikiUtils.clientPrint({
                    url: printDataUrl,
                    data: printParams,
                    printer: printer,
                    keepLoading: options.keepLoading,
                    printSessionKey: printSessionKey,
                    showSuccessMessage: showSuccessMessage,
                    success: printSuccess,
                    canceled: printCanceled,
                    complete: printComplete,
                    failed: printFailed,
                    exception: printException,
                    showExMessage: showExMessage,
                })
                break;
            case 'selectprinter':
                TorihikiUtils.clientPrint({
                    url: printDataUrl,
                    data: printParams,
                    printer: printer,
                    keepLoading: options.keepLoading,
                    printSessionKey: printSessionKey,
                    selectPrinter: true,
                    showSuccessMessage: showSuccessMessage,
                    success: printSuccess,
                    canceled: printCanceled,
                    complete: printComplete,
                    failed: printFailed,
                    exception: printException,
                    showExMessage: showExMessage,
                })
                break;
            case 'preview':
                TorihikiUtils.clientPrint({
                    url: printDataUrl,
                    data: printParams,
                    printer: printer,
                    keepLoading: options.keepLoading,
                    preview: true,
                    printSessionKey: printSessionKey,
                    selectPrinter: true,
                    showSuccessMessage: showSuccessMessage,
                    success: printSuccess,
                    canceled: printCanceled,
                    complete: printComplete,
                    failed: printFailed,
                    exception: printException,
                    showExMessage: showExMessage,
                })
                break;
            case 'server':
                TorihikiUtils.ajaxEx({
                    url: printDataUrl,
                    printer: printer,
                    printSessionKey: printSessionKey,
                    keepLoading: options.keepLoading,
                    data: printParams,
                    showSuccessMessage: showSuccessMessage,
                    success: printSuccess,
                    canceled: printCanceled,
                    complete: printComplete,
                    failed: printFailed,
                    exception: printException,
                    showExMessage: showExMessage,
                })
                break;
            case 'download':
                TorihikiUtils.openServerFile({
                    url: printDataUrl,
                    printer: printer,
                    printSessionKey: printSessionKey,
                    keepLoading: options.keepLoading,
                    data: printParams,
                    showSuccessMessage: showSuccessMessage,
                    success: printSuccess,
                    complete: printComplete,
                    failed: printFailed,
                    exception: printException,
                    showExMessage: showExMessage,
                })
                break;
            default:
                // ローディング非表示
                TorihikiUtils.hideLoading();
                break;
        }
    },
    /**
     * クライアント印刷
     * @param {any} options
     */
    clientPrint: function (options) {
        // プリンター種別
        var printerType = options.printer;

        // プリンターを取得する。
        var printer = TorihikiUtils.getPrinterByPrinterType(printerType);

        // プリンターのトレイ
        var printerTray = TorihikiUtils.getPrinterTrayByPrinterType(printerType);
        options.data['TrayNo'] = printerTray;

        // プリンター取得失敗場合、戻る
        if (TorihikiUtils.isNullOrEmptyOrWhiteSpace(printer)) {
            throw TorihikiMsg.getMessage('KYE000059','');
            return false;
        }

        // 印刷ファイル削除
        var deletePrintFile = function () {
            TorihikiUtils.ajaxEx({
                url: DeletePrintFileUrl,
                data: { printSessionKey : options.printSessionKey || 'printFile'}
            })
        }
        // エラー処理
        var errorProcess = function (err, windowObj, popInfo) {
            if (typeof err.response === 'undefined') {
                message = '印刷処理にエラーが発生しました。<br />' + err.toString() + '。';

                if (options.showExMessage) {
                    TorihikiMsg.show({
                        message: message,
                        callback: function () { execExCallback(options) },
                    });
                }
                else {
                    execExCallback(options);
                }
            }
            else {
                var status = err.response.data.status; //ステータス取得します。
                var message = '';
                if (status === -101) {
                    message = '印刷処理にエラーが発生しました。<br />エラーコード:' + status + '。';
                    if (typeof err.response.data.statusmessage !== 'undefined' && typeof err.response.data.statusmessage !== null) {
                        message += '<br />エラーメッセージ：' + err.response.data.statusmessage + '。';
                    }

                    if (options.showExMessage) {
                        TorihikiMsg.show({
                            message: message,
                            callback: function () { execExCallback(options) },
                        });
                    }
                    else {
                        execExCallback(options);
                    }
                }
                else if (status === -103) {

                    console.log('印刷処理は中断しました。');

                    // 失敗後処理があれば
                    if (typeof options.canceled !== 'undefined') {
                        if (typeof options.canceled === 'function') {
                            options.canceled(options.data);
                        } else {
                            TorihikiUtils.execCallback(options.canceled, options.data);
                        }
                    }
                    else {
                        execExCallback(options);
                    }
                }
            }
        }

        var execExCallback = function (options) {
            if (typeof options.exception !== 'undefined') {
                if (typeof options.exception === 'function') {
                    options.exception(options.data);
                } else {
                    TorihikiUtils.execCallback(options.exception, options.data);
                }
            }
        }

        // 成功処理
        var successProcess = function (res, windowObj, popInfo) {
            if (typeof popInfo !== 'undefined') {
                popInfo.div.modal("hide");
            } else {
                if (typeof windowObj !== 'undefined') {
                    windowObj.close();
                }
            }

            // 実行完了メッセージ表示する時
            if (options.showSuccessMessage == undefined || options.showSuccessMessage) {
                TorihikiMsg.message({
                    message: 'KYI000005',
                    args: '印刷処理',
                    callback: function () {
                        // 成功後処理があれば
                        if (typeof options.success !== 'undefined') {
                            if (typeof options.success === 'function') {
                                options.success(options.data);
                            } else {
                                TorihikiUtils.execCallback(options.success, options.data);
                            }
                        }
                    }
                });
            } else {
                // 成功後処理があれば
                if (typeof options.success !== 'undefined') {
                    if (typeof options.success === 'function') {
                        options.success(options.data);
                    } else {
                        TorihikiUtils.execCallback(options.success, options.data);
                    }
                }
            }

            // 印刷ファイル削除
            deletePrintFile();
        }

        var makePrintModal = function () {

            var popModelid = TorihikiUtils.getRandomId('popModal');
            var popIframeid = popModelid.replace('popModal', 'ifrmpop');
            var popdiv = TorihikiUtils.window.$('#popModal').clone().attr('id', popModelid).addClass('tcc-popsearch').addClass('tcc-popsearch');
            popdiv.empty();
            popdiv[0].style.zIndex = TorihikiUtils.newPopUpZIndex();
            TorihikiUtils.window.$('#popModal').after(popdiv);
            // 子画面で呼出時
            if (TorihikiUtils.isPopUpWindow()) {
                TorihikiUtils.addCoverDiv(window.name);
            }
            // 呼出元
            popdiv.data("popsource-window", window);

            var divdata = '<iframe frameborder="0" id="' + popIframeid + '" name="' + popIframeid + '" scrolling="auto" style="width:92%;"></iframe>';
            divdata = '<div class="modal-dialog">' + divdata + '</div>';

            popdiv.html(divdata);
            popdiv.find('.modal-dialog').addClass('tcc-popsearch-print-preview');
            popdiv.find('.modal-dialog').prepend('<div class="tcc-group-title">印刷プレビュー</div>');
            popdiv.find('.modal-dialog').prepend('<button type="button" class="close" data-dismiss="modal" id="popCloseIcon" aria-hidden="true"><i class="fas fa-window-close"></i></button>');

            popdiv.modal("show");

            popdiv.on('hide.bs.modal', function () {
                // 子画面で呼出時
                if (popdiv.data("popsource-window").name.indexOf('ifrmpop') > -1) {
                    popdiv.data("popsource-window").TorihikiUtils.removeCoverDiv(popdiv.data("popsource-window").name);
                }
                popdiv.remove();
                TorihikiUtils.hideLoading();

                // 印刷ファイル削除
                deletePrintFile();
            });

            return {
                modalId: popModelid,
                iframeid: popIframeid,
                div: popdiv,
            };
        }

        // データ請求
        TorihikiUtils.ajaxEx({
            url: options.url,
            data: options.data,
            keepLoading: options.keepLoading,
            success: function (result) {

                // 実行成功後、メッセージがあれば
                if (result.warning && result.message != undefined && result.message != null) {
                    if (typeof options.success !== 'undefined') {
                        if (typeof options.success === 'function') {
                            options.success(result.data);
                        } else {
                            TorihikiUtils.execCallback(result.success, result.data);
                        }
                    }
                    return;
                }

                var printUrl = TorihikiUtils.addPageSessionId(location.origin + ClientPrintUrl);

                if (options.printSessionKey != undefined) {
                    printUrl += "&printSessionKey=" + options.printSessionKey;
                }

                // クライアント印刷オブジェクト
                var sdp = new SDP();
                sdp.trayMode = 1;

                // ローカルのキャッシュをすべて削除する
                sdp.deleteCache();

                // プレビュー場合
                if (options.preview) { // プレビューとプリンター選択
                    var windowObj;
                    var popModalInfo = makePrintModal();
                    // プレビュー
                    //sdp.showPreviewWindow(printUrl,
                    sdp.embedPreviewWindow(popModalInfo.iframeid, printUrl,
                        options.selectPrinter ? 2 : 1, // プリンター選択
                        1,
                        function (res) {
                            successProcess(res, windowObj, popModalInfo);
                        },
                        function (err) {
                            errorProcess(err);
                        }
                    )
                        .then(function (res) {
                            windowObj = res.window;
                            if (typeof windowObj !== 'undefined') {
                                $(windowObj.document).find('#background').css('background-color', 'lightgray');
                                $(windowObj.document).find('#footer').css('display', 'none');
                            }
                        }).catch(function (err) {
                            errorProcess(err);
                        });
                }
                else if (options.selectPrinter) { // プリンター選択
                    sdp.showPrinterSelectDialog(printUrl).then(function (res) {
                        successProcess(res);
                    }).catch(function (err) {
                        errorProcess(err);
                    });
                }
                else {
                    sdp.print(printUrl, printer).then(function (res) {
                        successProcess(res);
                    }).catch(function (err) {
                        errorProcess(err);
                    });
                }
            },
            // 実行失敗処理
            failed: function (result) {
                if (typeof options.failed !== 'undefined') {
                    if (typeof options.failed === 'function') {
                        options.failed(result.data);
                    } else {
                        TorihikiUtils.execCallback(options.failed, result.data);
                    }
                }
            },
            // 実行終了処理
            complete: function (result) {
                if (typeof options.complete !== 'undefined') {
                    if (typeof options.complete === 'function') {
                        options.complete(result.data);
                    } else {
                        TorihikiUtils.execCallback(options.complete, result.data);
                    }
                }
            }
        })
    },
    // =================================================================
    // =====================ファイルアップロード処理======================
    // =================================================================
    /**
     * ファイル状態チェック
     * @param {any} fileSelector
     */
    checkFileStatus: function (fileSelector) {

        return new Promise((resolve, reject) => {
            var checkResult = {};
            checkResult.result = true;
            let defer = $.Deferred();
            defer.resolve();

            $(fileSelector).each(function () {
                let element = this;
                defer = defer.then(function () {
                    return new Promise((rsv, rej) => {
                        if (element.files.length == 0) {
                            rsv(true);
                        }
                        // ファイルを一番目文字呼出でファイルが変更または削除されたことを判断する。
                        var file = element.files[0];
                        file.slice(0, 1) // only the first byte
                            .arrayBuffer() // try to read
                            .then(() => {
                                rsv(true);
                            })
                            .catch((err) => {
                                checkResult.error = err;
                                checkResult.result = false;
                                checkResult.fileName = file.name;
                                checkResult.element = element;
                                element.value = null;
                                rej(checkResult);
                            });
                    })
                })
            });

            // チェック結果
            defer.done(function () {
                resolve(checkResult);
            }).fail(function () {
                reject(checkResult);
            })
        })
    },
    // =================================================================
    // =====================CSV処理=====================================
    // =================================================================
    /*
     * options
     *   data  データ
     *   fileName ファイル名
     * 　showTitle ヘッダ出力フラグ
     *   columns 列情報
     *      datakey キー文字
     *      titlekey 値文字
     *      data 列情報
         formatter: function()　フォーマット
     */
    jsonToCSV: function (options) {
        var browser = function () {
            var Sys = {};
            var ua = navigator.userAgent.toLowerCase();
            var s;
            (s = ua.indexOf('edge') !== - 1 ? Sys.edge = 'edge' : ua.match(/rv:([\d.]+)\) like gecko/)) ? Sys.ie = s[1] :
                (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
                    (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
                        (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
                            (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
                                (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
            return Sys;
        };
        var getDownloadUrl = function (csvData) {
            var _utf = "\uFEFF"; // utf-8ために
            if (window.Blob && window.URL && window.URL.createObjectURL) {
                var csvData = new Blob([_utf + csvData], {
                    type: 'text/csv'
                });
                return URL.createObjectURL(csvData);
            }
            // return 'data:attachment/csv;charset=utf-8,' + _utf + encodeURIComponent(csvData);
        };

        var saveAs = function (fileName, csvData) {
            var bw = browser();
            if (!bw['edge'] && !bw['ie']) {
                var alink = document.createElement("a");
                alink.id = "linkDwnldLink";
                alink.href = getDownloadUrl(csvData);
                document.body.appendChild(alink);
                var linkDom = document.getElementById('linkDwnldLink');
                linkDom.setAttribute('download', fileName);
                linkDom.click();
                document.body.removeChild(linkDom);
            }
            else if (bw['ie'] >= 10 || bw['edge'] == 'edge') {
                var _utf = "\uFEFF";
                var _csvData = new Blob([_utf + csvData], {
                    type: 'text/csv'
                });
                window.navigator.msSaveBlob(_csvData, fileName);
            }
            else {
                var oWin = window.top.open("about:blank", "_blank");
                oWin.document.write('sep=,\r\n' + csvData);
                oWin.document.close();
                oWin.document.execCommand('SaveAs', true, fileName);
                oWin.close();
            }
        };
        var bw = browser();
        if (bw['ie'] < 9) return; // IE9前
        var data = options['data'],
            showTitle = typeof options['showTitle'] === 'undefined' ? true : options['showTitle'],
            fileName = (options['fileName'] || 'UserExport.csv'),
            columns = options['columns'] || {
                key: [],
                value: [],
                formatter: undefined
            };
        var showTitle = typeof showTitle === 'undefined' ? true : showTitle;
        var row = "", CSV = '', key;
        // ヘッダを出力時
        if (showTitle) {
            // ヘッダあれば
            if (columns.columns.length) {
                columns.columns.map(function (n) {
                    row += n[columns.titlekey] + ',';
                });
            } else {
                // ヘッダが設定されない場合、データのキーを設定する
                for (key in data[0]) row += key + ',';
            }
            row = row.slice(0, -1); //　最後のコンマを削除する
            CSV += row + '\r\n'; // 改行符号追加
        }
        // データ処理
        data.map(function (n) {
            row = '';
            // columnsが設定された
            if (columns.columns.length) {
                columns.columns.map(function (m) {
                    var outval = '';
                    if (typeof columns.formatter === 'function') {
                        outval = columns.formatter(m, n[m[columns.datakey]]);
                    } else if (typeof columns.formatter !== 'undefined') {
                        outval = columns.formatter(m, n[m[columns.datakey]]);
                    } else {
                        outval = n[m[columns.datakey]];
                    }
                    if (typeof outval === 'undefined' || outval == null) {
                        outval = '';
                    }
                    row += '"' + outval + '",';
                });
            } else {
                for (key in n) {
                    row += '"' + (typeof columns.formatter === 'function' ? columns.formatter(key, n[key]) || n[key] : n[key]) + '",';
                }
            }
            row.slice(0, row.length - 1); // 最後のコンマを削除する
            CSV += row + '\r\n'; // 改行符号追加
        });
        if (!CSV) return;
        saveAs(fileName, CSV);
    },
    // =================================================================
    // =====================Excel処理=====================================
    // =================================================================
    jsonToExcel: function (options) {

        // 書き込み時のオプションは以下を参照
        // https://github.com/SheetJS/js-xlsx/blob/master/README.md#writing-options
        var writeOpts = {
            bookType: 'xlsx',
            bookSST: false,
            type: 'binary'
        };
        var headers = $.map(options.columns.columns, function (header) {
            return header[options.columns.datakey];
        });
        var titles = $.map(options.columns.columns, function (header) {
            return header[options.columns.titlekey];
        });
        var widths = [];
        $.each(options.columns.columns, function (i, header) {
            var width = { wpx: 100 };
            if (header.width) width.wpx = header.width;
            widths.push(width);
        });
        var datas = $.map(options.data, function (data) {
            var output = {};
            $.each(options.columns.columns, function (i, column) {
                output[column[options.columns.datakey]] = TorihikiUtils.toEmpty(data[column[options.columns.datakey]]);
            })
            return output;
        })

        var sheetName = options.sheetName || "Sheet1";

        var out = XLSX.utils.book_new();

        // ArrayをWorksheetに変換する
        var sheet = XLSX.utils.json_to_sheet(datas, { header: headers });

        XLSX.utils.book_append_sheet(out, sheet, sheetName);

        // ボーダーを設定する。
        TorihikiXlsxStyleUtils.setBorderDefaultAll(out, sheetName);

        // ボーダーを設定する。
        TorihikiXlsxStyleUtils.setColWidth(out, sheetName, widths);

        /* write file and trigger a download */
        XLSX.writeFile(out, options.fileName + '.xlsx', writeOpts);
        //var wb_out = XLSX.write(out, options.fileName, writeOpts);

        //var wbout = XLSX.write(out, writeOpts);
        ////保存，使用FileSaver.js
        //return saveAs(new Blob([TorihikiXlsxStyleUtils.s2ab(wbout)], { type: "" }), options.fileName + '.xlsx');

        //// WorkbookからBlobオブジェクトを生成
        //// 参照：https://developer.mozilla.org/ja/docs/Web/API/Blob
        //var blob = new Blob([s2ab(wb_out)], { type: 'application/octet-stream' });

        //// FileSaverのsaveAs関数で、xlsxファイルとしてダウンロード
        //// 参照：https://github.com/eligrey/FileSaver.js/
        //saveAs(blob, options.fileName + '.xlsx');

    },

    // =================================================================
    // =====================Wijmo処理======================
    // =================================================================
    /**
     * WijmoのMultiRowコントロールを作成する。
     * @param {any} element
     * @param {any} options
     */
    createWijmoMultiRow: function (element, options) {
        //// HTML要素に含まれるすべてのWijmoコントロールを破棄します。
        //wijmo.grid.multirow.MultiRow.disposeAll(element);
        //// ツールバーを破棄します。
        //if ($(element).prev().hasClass('tcc-grid-toolbar')) {
        //    $(element).prev().remove();
        //}
        // this.hideWijimoNoRecord();

        return this.getWijmoMultiRow(element, options);
    },

    /**
     * WijmoのMultiRowコントロールを破棄する。
     * @param {any} element
     * @param {any} options
     */
    disposeWijmoMultiRow: function (element) {
        $(element).wijmotoolbar('destroy');
        // HTML要素に含まれるすべてのWijmoコントロールを破棄します。
        wijmo.grid.multirow.MultiRow.disposeAll(element);
        // ツールバーを破棄します。
        if ($(element).prev().hasClass('tcc-grid-toolbar')) {
            $(element).prev().remove();
        }
        // ツールバーを破棄します。
        if ($(element).prev().hasClass('tcc-norecord-message')) {
            $(element).prev().prev().remove();
        }
        // 件数なしメッセージ非表示
        TorihikiUtils.hideWijimoNoRecord();
    },

    /**
     * WijmoのMultiRowコントロールを取得する。
     * @param {any} element
     * @param {any} options
     */
    getWijmoMultiRow: function (element, options) {

        var grid = wijmo.grid.multirow.MultiRow.getControl(element);
        if (grid == null) {
            grid = new wijmo.grid.multirow.MultiRow(element, {
                validateEdits: false,
                keyActionTab: wijmo.grid.KeyAction.None,
                keyActionEnter: wijmo.grid.KeyAction.None,
                autoGenerateColumns: false,
                allowDragging: wijmo.grid.AllowDragging.None,
                isReadOnly: false,
                allowResizing: wijmo.grid.AllowResizing.None,
                headersVisibility: wijmo.grid.HeadersVisibility.Column,
                selectionMode: wijmo.grid.SelectionMode.RowRange,
            });
        }

        if (typeof options !== 'undefined') {

            // 列変更できる設定
            if (typeof options.layoutDefinition !== 'undefined' && !options.editable) {
                var setLayoutReadonly = function (cells) {

                    if (TorihikiUtils.isArray(cells)) {
                        for (var icell = 0; icell < cells.length; icell++) {
                            setLayoutReadonly(cells[icell]);
                        }
                    } else if (typeof cells.cells !== 'undefined') {
                        setLayoutReadonly(cells.cells);
                    } else {
                        if (typeof cells.isReadOnly === 'undefined') {
                            cells.isReadOnly = true;
                        }
                    }
                }

                setLayoutReadonly(options.layoutDefinition);
            }

            for (var key in options) {
                // イベント場合
                if (typeof grid[key] === 'object') {
                    if (typeof grid[key].constructor !== 'undefined' && grid[key].constructor.name == 'Event') {
                        grid[key].addHandler(options[key])
                        continue;
                    }
                }

                // 値の場合
                grid[key] = options[key];

                // 
                if (typeof options.selectedBColorByRadioCol !== 'undefined') {
                    grid.formatItem.addHandler(function (s, e) {
                        if (typeof e.panel !== 'undefined') {
                            if (e.panel.cellType == wijmo.grid.CellType.Cell) {
                                rowdata = e.panel.rows[e.row].dataItem;

                                if (rowdata[options.selectedBColorByRadioCol] == true) {
                                    wijmo.addClass(e.cell, 'wj-state-selected-cus');
                                }
                                else {
                                    wijmo.removeClass(e.cell, 'wj-state-selected-cus');
                                }
                            }
                        }
                    });
                }
            }

            // リストのみ時
            //if (!grid.editable) {
            //    grid.selectionMode = wijmo.grid.SelectionMode.RowRange;
            //    grid.selectionChanged.addHandler(function (s, e) {
            //        if (s.selection.row == -1 || s.selection.col == -1) {
            //            return;
            //        }
            //        var rowsPerItem = s.rowsPerItem;
            //        var curRowPerItem = e.row % rowsPerItem;
            //        var startRow = e.row - curRowPerItem;
            //        var endRow = startRow + rowsPerItem - 1;
            //        s.select(new wijmo.grid.CellRange(startRow, 0, endRow, s.columns.length - 1), false);
            //    });
            //}
        }

        var gridFrozenColumns = options.frozenColumns;
        if (typeof gridFrozenColumns === 'undefined') {
            gridFrozenColumns = 0;
        }

        if (typeof options.toolbars !== 'undefined') {
            var toolsbarOption = $.extend({}, options.toolbars);
            toolsbarOption = $.extend(toolsbarOption, { grid: grid });
            $(element).wijmotoolbar(toolsbarOption);
        }

        // 固定列
        grid.frozenColumns = gridFrozenColumns;

        grid.addEventListener(grid.hostElement, 'keydown', function (e) {
            if (!e.ctrlKey && e.keyCode == 32) {
                var $element = $(e.srcElement);
                if ($element.hasClass('wj-cell')) {
                    e.preventDefault();
                }
                return;
            }
        }, true);

        // Wijmoデファクト高さ設定
        grid.rows.defaultSize = TorihikiGlobal.WijmoRowDefaultHeight;

        // 仮想化を有効にするために必要な最小行数、最小列数デフォルト値設定
        grid.virtualizationThreshold = [0, 10000];

        return grid;
    },
    /**
     * WijmoのMultiRowコントロールのデータを設定する。
     * @param {any} element
     * @param {any} options
     */
    setWijmoMultiRowData: function(grid, datas, options) {
        if (typeof grid === 'undefined' || grid == null) {
            TorihikiUtils.log('グリッドが作成していませんでした。');
            return;
        }
        // 一覧の場合、ブランク行を追加すｒ。
        if (!grid.editable) {
            if (datas.length == 0) {
                ////$(grid.hostElement).addClass('hidden');
                //var rowsPerItem = grid.rowsPerItem;
                //var mm = new wijmo.grid.MergeManager(grid);
                //// getMergedRangeメソッドをオーバーライドします。
                //mm.getMergedRange = function (panel, r, c, clip = true) {
                //    if (panel.cellType == wijmo.grid.CellType.Cell) {
                //        return new wijmo.grid.CellRange(r - (r % rowsPerItem), 0, r + (rowsPerItem - (r % rowsPerItem)) - 1, panel.grid.columns.length - 1);
                //        //return new wijmo.grid.CellRange(r, 0, r, panel.grid.columns.length - 1);
                //    }
                //};
                //grid.mergeManager = mm;

                //var initData = {};
                //initData[grid.columns[0].binding] = messages.NoSearchRecord;
                //initData[grid.columns[1].binding] = messages.NoSearchRecord;
                //initData[grid.columns[2].binding] = messages.NoSearchRecord;
                //datas.push(initData);
                //} else {
                //$(grid.hostElement).removeClass('hidden');
            }
        }

        var exOptions = $.extend({ sortNulls: wijmo.collections.SortNulls.Natural }, options, true);

        var dataView = new wijmo.collections.CollectionView(datas, exOptions);
        // データソースバインディング
        grid.itemsSource = dataView;

        // データなしメッセージ表示
        TorihikiUtils.setWijmoNoRecord(grid, datas);

        // 行が選択されないに変更
        if (!grid.editable) {
            grid.select(new wijmo.grid.CellRange(-1, -1, -1, -1), true);
        }

        // Tooltip
        TorihikiUtils.setWijmoToolTips(grid);
    },
    /**
     * WijmoのFlexGridコントロールを作成する。
     * @param {any} element
     * @param {any} options
     */
    createWijmoFlexGrid: function (element, options) {
        // データ件数なしの表示は他のDIVで表示するので、一時コミット
        // // HTML要素に含まれるすべてのWijmoコントロールを破棄します。
        // wijmo.grid.FlexGrid.disposeAll(element);
        // // ツールバーを破棄します。
        // if ($(element).prev().hasClass('tcc-grid-toolbar')) {
        //     $(element).prev().remove();
        // }
        // this.hideWijimoNoRecord();

        return this.getWijmoFlexGrid(element, options);
    },

    /**
    * WijmoのMultiRowコントロールを破棄する。
    * @param {any} element
    * @param {any} options
    */
    disposeWijmoFlexGrid: function (element) {
        $(element).wijmotoolbar('destroy');
        var grid = wijmo.grid.FlexGrid.getControl(element);

        // ページング機能有る場合、Wijmoページコントロールを破棄します。
        if (grid != null && grid.pager != undefined) {
            grid.pager.dispose();
        }

        // HTML要素に含まれるすべてのWijmoコントロールを破棄します。
        wijmo.grid.FlexGrid.disposeAll(element);
        // ツールバーを破棄します。
        if ($(element).prev().hasClass('tcc-grid-toolbar')) {
            $(element).prev().remove();
        }
        // ツールバーを破棄します。
        if ($(element).prev().hasClass('tcc-norecord-message')) {
            $(element).prev().prev().remove();
        }
        // 件数なしメッセージ非表示
        TorihikiUtils.hideWijimoNoRecord();
    },

    /**
     * WijmoのFlexGridコントロールを取得する。
     * @param {any} element
     * @param {any} options
     */
    getWijmoFlexGrid: function (element, options) {
        var autoFitColumnHeaderWidth = function (s, e) {
            setTimeout(function () {
                for (var i = 0; i < s.columnHeaders.rows.length; i++) {
                    var row = s.columnHeaders.rows[i];
                    row.wordWrap = true;
                }
                s.autoSizeRow(s.columnHeaders.rows.length - 1, true);
            });

        }
        var grid = wijmo.grid.FlexGrid.getControl(element);
        if (grid == null) {

            // 列情報再設定
            if (typeof options.columns !== 'undefined') {
                for (var icell = 0; icell < options.columns.length; icell++) {
                    if (typeof options.columns[icell].isReadOnly === 'undefined') {
                        options.columns[icell].isReadOnly = true;
                    }
                }
            }

            grid = new wijmo.grid.FlexGrid(element, {
                validateEdits: false,
                keyActionTab: wijmo.grid.KeyAction.None,
                keyActionEnter: wijmo.grid.KeyAction.None,
                autoGenerateColumns: false,
                allowMerging: 'All',
                allowDragging: wijmo.grid.AllowDragging.None,
                isReadOnly: false,
                headersVisibility: wijmo.grid.HeadersVisibility.Column,
                columns: options.columns,
                resizedColumn: autoFitColumnHeaderWidth,
                loadedRows: autoFitColumnHeaderWidth,
                selectionMode: wijmo.grid.SelectionMode.Row,
                allowResizing: wijmo.grid.AllowResizing.None,
            });
        }

        if (typeof options !== 'undefined') {

            for (var key in options) {
                // イベント場合
                if (typeof grid[key] === 'object') {
                    if (typeof grid[key].constructor !== 'undefined' && grid[key].constructor.name == 'Event') {
                        grid[key].addHandler(options[key])
                        continue;
                    }
                }

                // 値の場合
                grid[key] = options[key];
            }

            if (typeof options.selectedBColorByRadioCol !== 'undefined') {
                grid.formatItem.addHandler(function (s, e) {
                    if (typeof e.panel !== 'undefined') {
                        if (e.panel.cellType == wijmo.grid.CellType.Cell) {
                            rowdata = e.panel.rows[e.row].dataItem;

                            if (rowdata[options.selectedBColorByRadioCol] == true) {
                                wijmo.addClass(e.cell, 'wj-state-selected-cus');
                            }
                            else {
                                wijmo.removeClass(e.cell, 'wj-state-selected-cus');
                            }
                        }
                    }
                });
            }
        }

        var gridFrozenColumns = options.frozenColumns;
        if (typeof gridFrozenColumns === 'undefined') {
            gridFrozenColumns = 0;
        }

        if (typeof options.toolbars !== 'undefined') {
            var toolsbarOption = $.extend({}, options.toolbars);
            toolsbarOption = $.extend(toolsbarOption, { grid: grid });
            $(element).wijmotoolbar(toolsbarOption);
        }

        // 固定列
        grid.frozenColumns = gridFrozenColumns;

        grid.addEventListener(grid.hostElement, 'keydown', function (e) {
            if (!e.ctrlKey && e.keyCode == 32) {
                var $element = $(e.srcElement);
                if ($element.hasClass('wj-cell')) {
                    e.preventDefault();
                }
                return;
            }
        }, true);

        // Wijmoデファクト高さ設定
        grid.rows.defaultSize = TorihikiGlobal.WijmoRowDefaultHeight;

        // 仮想化を有効にするために必要な最小行数、最小列数デフォルト値設定
        grid.virtualizationThreshold = [0, 10000];

        return grid;
    },
    /**
     * WijmoのFlexGridコントロールのデータを設定する。
     * @param {any} element
     * @param {any} options
     */
    setWijmoFlexGridData: function (grid, datas, options, pager) {
        if (typeof grid === 'undefined' || grid == null) {
            TorihikiUtils.log('グリッドが作成していませんでした。');
            return;
        }
        // 一覧の場合、ブランク行を追加すｒ。
        if (!grid.editable) {
            if (datas.length == 0) {
                //var mm = new wijmo.grid.MergeManager(grid);
                //// getMergedRangeメソッドをオーバーライドします。
                //mm.getMergedRange = function (panel, r, c, clip = true) {
                //    if (panel.cellType == wijmo.grid.CellType.Cell) {
                //        return new wijmo.grid.CellRange(r, 0, r, panel.grid.columns.length - 1);
                //    }
                //};
                //grid.mergeManager = mm;

                //var initData = {};
                //initData[grid.columns[0].binding] = '該当するレコードが見つかりません。';
                //datas.push(initData);
            }
        }

        var exOptions = $.extend({ sortNulls: wijmo.collections.SortNulls.Natural }, options, true);

        var dataView = new wijmo.collections.CollectionView(datas, exOptions);

        if (options != undefined && options.pageSize != undefined) {
            if (pager == undefined) {
                pager = '.tcc-pager-container';
            }
            if (grid.pager == undefined) {
                grid.pager = new wijmo.input.CollectionViewNavigator(pager, {
                    byPage: true,
                    headerFormat: '{currentPage:n0} / {pageCount:n0} ページ',
                    cv: dataView
                });
            }
        }

        // データソースバインディング
        grid.itemsSource = dataView;

        // データなしメッセージ表示
        TorihikiUtils.setWijmoNoRecord(grid, datas);

        // 行が選択されないに変更
        if (!grid.editable) {
            grid.select(new wijmo.grid.CellRange(-1, 0, -1, 0), true);
        }

        // Tooltip
        TorihikiUtils.setWijmoToolTips(grid);
    },
    /**
     * Wijmo日付コントロール作成.
     */
    createWijmoDate: function (grid, r, c, binding) {
        return '<div class="tcc-wijmo-input-date-area"><span id="' + binding + '" class="tcc-input-date show-icon" >' + TorihikiUtils.toEmpty(grid.cells.getCellData(r, c)) + '</span></div>';
    },
    /**
     * Wijmoデータなしメッセージを削除する.
     * @param {any} grid
     * @param {any} datas
     */
    hideWijimoNoRecord: function (grid, datas) {
        $('.tcc-norecord-message').remove();
        setTimeout(function () {
            $('.tcc-norecord-message').remove();
        }, 50)
        setTimeout(function () {
            $('.tcc-norecord-message').remove();
        }, 100)
    },
    /**
     * Wijmoデータなしメッセージ表示.
     * @param {any} grid
     * @param {any} datas
     */
    setWijmoNoRecord: function (grid, datas) {
        // 一覧の場合、ブランク行を追加すｒ。
        if (!grid.editable) {
            if (datas.length == 0) {

                //grid.collectionView.sourceCollection.splice(0, 0, {});
                //grid.collectionView.refresh();
                //grid.rows[0].isReadOnly = true;

                // 既作成した
                var prevctl = $(grid.hostElement).prev();
                if (prevctl.hasClass('tcc-grid-toolbar')) {
                    prevctl = prevctl.prev();
                }
                if (prevctl.hasClass('tcc-norecord-message')) {
                    prevctl.remove();
                }
                var $norecorddiv = $('<div class="tcc-norecord-message">' + messages.NoSearchRecord + '</div>');
                var setBorderRight = true;

                setTimeout(function () {
                    $(grid.hostElement).before($norecorddiv);
                    var width = $(grid.hostElement).find('.wj-cells').width();
                    if (width > $(grid.hostElement).width()) {
                        width = $(grid.hostElement).width();
                        setBorderRight = false;
                    }
                    $norecorddiv.css('width', width);
                    //$norecorddiv.css("height", $(grid.hostElement).find('.wj-cells').height() - 1);
                    $norecorddiv.css('top', $(grid.hostElement).find('.wj-cells').offset().top);
                    $norecorddiv.css('left', $(grid.hostElement).offset().left + 1);
                    if (setBorderRight) {
                        $norecorddiv.addClass('tcc-norecord-message-border-right');
                    }
                }, 100)

                var gridResized = function () {
                    setBorderRight = true;
                    // 幅再設定
                    var width = $(grid.hostElement).find('.wj-cells').width();
                    if (width > $(grid.hostElement).width()) {
                        width = $(grid.hostElement).width();
                        setBorderRight = false;
                    }
                    $norecorddiv.css('width', width);
                    if (setBorderRight) {
                        $norecorddiv.addClass('tcc-norecord-message-border-right');
                    } else {
                        $norecorddiv.removeClass('tcc-norecord-message-border-right');
                    }
                }
                $(window).on('resize', function () {
                    gridResized();
                });

                grid['resizedColumn'].addHandler(function (s, e) {
                    gridResized();
                });
                grid['resizingColumn'].addHandler(function (s, e) {
                    gridResized();
                });
            }
            else {
                TorihikiUtils.hideWijimoNoRecord(grid, datas);
            }
        }
    },
    /**
      * Tooltip設定
      * @param {any} grid
      */
    setNoWijmoToolTips: function (obj) {

        if (typeof wijmo === 'undefined') {
            return;
        }

        var tip = new wijmo.Tooltip(),
            el = null;

        var checkTextLength = function (element) {
            var $el = $(element);
            var resultC = false;

            //if ($el[0].offsetWidth < $el[0].scrollWidth) {
            $el.children().each(function (i, e) {
                if (e.offsetWidth < e.scrollWidth) {
                    resultC = true;
                    return false;
                }
            })

            if ($el[0].offsetWidth < $el[0].scrollWidth) {
                resultC = true;
            }

            if ($el[0].offsetHeight < $el[0].scrollHeight) {
                resultC = true;
            }

            return resultC;
        }

        $(".tcc-3point-leader,.tcc-doc-list-comment").mousemove(function (e) {
            if ($(this).parent(".wj-cell").length == 0) {
                if (el != this) {
                    el = this;
                    if (checkTextLength(this)) {
                        tip.show(this, this.innerText);
                    } else {
                        tip.hide();
                    }
                }
            }
        });

        $(".tcc-3point-leader,.tcc-doc-list-comment").mouseout(function () {
            var _this = $(this);
            if (_this.parent(".wj-cell").length == 0) {
                tip.hide();
                el = null;
            }
        });
    },
    /**
     * Tooltip設定
     * @param {any} grid
     */
    setWijmoToolTips: function (grid) {
        var tip = new wijmo.Tooltip(),
            rng = null;

        var checkTextLength = function (element) {
            var $el = $(element);
            if ($el[0].offsetWidth < $el[0].scrollWidth) {
                return true;
            } else {
                return false;
            }
        }

        grid.hostElement.addEventListener('mousemove', function (e) {
            var ht = grid.hitTest(e.pageX, e.pageY);
            if (!ht.range.equals(rng)) {
                //  && (ht.col == 1 || ht.col == 3)
                if (ht.cellType == wijmo.grid.CellType.Cell) {
                    rng = ht.range;
                    var cellElement = document.elementFromPoint(e.clientX, e.clientY),
                        cellBounds = grid.getCellBoundingRect(ht.row, ht.col),
                        data = wijmo.escapeHtml(grid.getCellData(rng.row, rng.col, true));
                    if (cellElement.className.indexOf('wj-cell') > -1 && cellElement.className.indexOf('tcc-show-tooltips') > -1) {
                        // 
                        if (checkTextLength(cellElement)) {
                            tip.show(grid.hostElement, cellElement.innerText, cellBounds);
                        } else {
                            tip.hide();
                        }
                    } else if ($(cellElement).parents().hasClass('wj-cell') && $(cellElement).parents().hasClass('tcc-show-tooltips')) {
                        if (checkTextLength($(cellElement).parents()) || checkTextLength($(cellElement))) {
                            tip.show(grid.hostElement, $(cellElement).parent().text(), cellBounds);
                        } else {
                            tip.hide();
                        }
                    } else {
                        tip.hide();
                    }
                }
            }
        });

        grid.hostElement.addEventListener('mouseout', function (e) {
            tip.hide();
            rng = null;
        });
    },
    /**
     * Wijmoクリック後、チェックボックスのチェック状態変更
     * @param {any} grid
     * @param {any} e
     */
    wijmoFlexGridClickToSelect: function (grid, e, checkBinding) {
        // ボタン時、戻る
        if (e.srcElement.tagName.toLowerCase() == 'button') {
            return;
        }
        if (checkBinding == undefined) {
            checkBinding = 'sel';
        }

        var ht = grid.hitTest(e);

        // 明細行以外の場合、下記処理を行うない
        if (ht.cellType != wijmo.grid.CellType.Cell) {
            return true;
        }
        // グループヘッダー行の場合、下記処理を行うない
        if (grid.rows[ht.row] instanceof wijmo.grid.GroupRow) {
            return true;
        }
        var cellElement = document.elementFromPoint(e.clientX, e.clientY);
        var currentItem = grid.collectionView.currentItem;
        if (cellElement.tagName != 'INPUT' && cellElement.tagName != 'LABEL') {
            currentItem[checkBinding] = !currentItem[checkBinding];
            // 当前行の選択状態の設定する
            grid.select(new wijmo.grid.CellRange(ht.row, 1, ht.row, 1), false);
        }
    },
    /**
    * Wijmoクリック後、チェックボックスのチェック状態変更
    * @param {any} grid
    * @param {any} e
    */
    wijmoMultiRowClickToSelect: function (grid, e, checkBinding) {
        // ボタン時、戻る
        if (e.srcElement.tagName.toLowerCase() == 'button') {
            return;
        }
        if (checkBinding == undefined) {
            checkBinding = 'sel';
        }

        var ht = grid.hitTest(e);

        // 明細行以外の場合、下記処理を行うない
        if (ht.cellType != wijmo.grid.CellType.Cell) {
            return true;
        }
        // グループヘッダー行の場合、下記処理を行うない
        if (grid.rows[ht.row] instanceof wijmo.grid.GroupRow) {
            return true;
        }
        var cellElement = document.elementFromPoint(e.clientX, e.clientY);
        var currentItem = grid.collectionView.currentItem;
        if (cellElement.tagName != 'INPUT' && cellElement.tagName != 'LABEL') {
            currentItem[checkBinding] = !currentItem[checkBinding];
            // 当前行の選択状態の設定する
            grid.select(new wijmo.grid.CellRange(ht.row, 1, ht.row, 1), false);
        }
    },
    /**
     * Wijmo固定列設定処理
     * @param {any} element
     * @param {any} columns
     */
    setWijmoFrozenColumns: function (element, columns) {
        setTimeout(function () {
            $(element).wijmotoolbar('setFrozenColumns', columns );
        }, 100);
    },
    /**
     * グリッドのスクロールバーリセット.
     * */
    setWijmoScrollPosition: function (grid, scrollPosition) {
        var prevScrollPosition = { x: -1, y: -1 };
        var loop = 1;
        setTimeout(function () {
            var scrollIv = setInterval(function () {
                TorihikiUtils.log('setWijmoScrollPosition');
                TorihikiUtils.log(prevScrollPosition);
                // 位置変更済み時
                if (grid.scrollPosition.y <= scrollPosition.y) {
                    clearInterval(scrollIv);
                    TorihikiUtils.log('setWijmoScrollPosition:Stop1');
                    return;
                }
                // 明細スクロールバーの位置保持
                grid.scrollPosition = scrollPosition;

                // 先に移動する位置が最大位置時
                if (prevScrollPosition.y <= grid.scrollPosition.y) {
                    clearInterval(scrollIv);
                    TorihikiUtils.log('setWijmoScrollPosition:Stop2');
                    return;
                }

                prevScrollPosition = grid.scrollPosition;
                loop++;
                if (loop > 100) {
                    TorihikiUtils.log('setWijmoScrollPosition:MaxLoop');
                }
            }, 100)
        }, 100);
    },
    /**
     * コメントリンク作成.
     * @param {any} grid
     * @param {any} row
     * @param {any} cell
     * @param {any} dempyoNoKey
     * @param {any} existedKey
     * @param {any} dempyoShikibetsuKbn
     * @param {any} commentReadonly
     * @param {any} shanaiMemoReadonly
     */
    createWijmoCommentLink: function (
        grid,
        row,
        cell,
        dempyoNoKey,
        existedKey,
        dempyoShikibetsuKbn,
        commentReadonly,
        shanaiMemoReadonly) {

        // セルクリア
        $(cell).empty();

        var btnClass = 'tcc-btn-outline-green';
        var tpl = '<button class="{2} tcc-btn tcc-btn-sm popdetail-link" data-dempyo-no="{0}" data-dempyo-shikibetsu-kbn="{1}" data-comment-readonly="{3}" data-shanai-momo-readonly="{4}" master="comment" type="button">コメント</button>';
        var dempyoNo = row[dempyoNoKey];
        if (dempyoNo == undefined || dempyoNo == null || dempyoNo.length < 2) {
            return TorihikiUtils.format(tpl, '', '', btnClass);
        }
        var commentExisted = row[existedKey];
        if (commentExisted == StaticConst.FLG_TRUE) {
            btnClass = 'tcc-btn-green';
        }
        if ((typeof dempyoShikibetsuKbn === 'undefined' || dempyoShikibetsuKbn == '') && typeof dempyoNo !== 'undefined') {
            dempyoShikibetsuKbn = dempyoNo.substring(0, 2);
        }

        // コメント機能ない伝票時、戻る
        switch (dempyoShikibetsuKbn) {
            case 'JU': // 受注
            case 'ID': // 移動依頼
            case 'HU': // 返却受付
            case 'AZ': // 預り商品
            case 'CI': // 駐車違反
                break;
            default:
                return;
        }

        if (typeof commentReadonly === 'undefined') {
            commentReadonly = '0';
        } else {
            commentReadonly = commentReadonly ? '1' : '0';
        }
        if (typeof shanaiMemoReadonly === 'undefined') {
            shanaiMemoReadonly = '0';
        } else {
            shanaiMemoReadonly = shanaiMemoReadonly ? '1' : '0';
        }
        var $tpl = $(TorihikiUtils.format(tpl, dempyoNo, dempyoShikibetsuKbn, btnClass, commentReadonly, shanaiMemoReadonly));
        $tpl.data('wijmo-row', row);
        $tpl.data('wijmo-row-comment-existed-key', existedKey);
        $tpl.data('wijmo-row-comment-dempyono-key', dempyoNoKey);
        $tpl.data('wijmo-grid', grid);
        $(cell).append($tpl);
        TorihikiUtils.setPopUp(cell);
    },
    // =================================================================
    // =====================FixedMidashi処理======================
    // =================================================================
    /** */
    hasFixedMidashiFixedLeftArea: function (element) {
        var _table = $(element).closest('table');
        if (_table.length == 0) return false;
        _table = _table[0];
        var fixedareas = _table.$FXH_FIXED_ELEMENT;
        var hasLeft = false;
        $(fixedareas).each(function () {
            var tblContainer = $(this).closest('div');
            if (tblContainer.hasClass('tcc-grid-edit-fixed-left')) {
                hasLeft = true;
                return;
            }
        });
        return hasLeft;
    },
    isFixedMidashiFixedLeftElement: function (element) {
        var _table = $(element).closest('table');
        if (_table.length == 0) return false;
        var tblContainer = $(_table).closest('div');

        if (tblContainer.hasClass('tcc-grid-edit-fixed-left')) {
            if (element.$SOURCE_ELEMENT) return true;
        }
        return false;
    },

    testTime: undefined,
    testIndex: undefined,
    testDiffMs: function (startindex, flg) {
        if (startindex != undefined) {
            this.testIndex = startindex;
        } else {
            this.testTime = undefined;
        }
        endtime = new Date();

        if (this.testTime != undefined) {
            console.log("******ExecuteTime:" + this.testIndex + (flg == undefined ? "" : "***" + flg + "***") + "---" + (endtime - this.testTime))
        }
        this.testTime = endtime;
        this.testIndex += 1;
    },
    colourBlend: function (c1, c2, ratio) {
        ratio = Math.max(Math.min(Number(ratio), 1), 0)
        var r1 = parseInt(c1.substring(1, 3), 16)
        var g1 = parseInt(c1.substring(3, 5), 16)
        var b1 = parseInt(c1.substring(5, 7), 16)
        var r2 = parseInt(c2.substring(1, 3), 16)
        var g2 = parseInt(c2.substring(3, 5), 16)
        var b2 = parseInt(c2.substring(5, 7), 16)
        var r = Math.round(r1 * (1 - ratio) + r2 * ratio)
        var g = Math.round(g1 * (1 - ratio) + g2 * ratio)
        var b = Math.round(b1 * (1 - ratio) + b2 * ratio)
        r = ('0' + (r || 0).toString(16)).slice(-2)
        g = ('0' + (g || 0).toString(16)).slice(-2)
        b = ('0' + (b || 0).toString(16)).slice(-2)
        return '#' + r + g + b
    },

    /**
     * 日付に、月数を加算 月末を対応するためメソッド作成
     * @param {date} dateObj 日付
     * @param {integer} months 月数
     * @return {date} 日数
     */
    addMonths : function (dateObj, months) {
        var y = dateObj.getFullYear();
        var m = dateObj.getMonth();
        var nextY = y;
        var nextM = m;
        if ((m + months) > 11) {
            nextY = y + 1;
            nextM = parseInt(m + months) - 12;
        } else {
            nextM = dateObj.getMonth() + months
        }
        var daysInNextMonth = TorihikiUtils.daysInMonth(nextY, nextM);
        var day = dateObj.getDate();
        if (day > daysInNextMonth) {
            day = daysInNextMonth;
        }
        return new Date(nextY, nextM, day);
    },
    /**
     * 年月の日数を取得
     * @param {integer} year 年
     * @param {integer} month 月
     * @return {Number} 日数
     */
    daysInMonth : function (year, month) {
        if (month == 1) {
            if (year % 4 == 0 && year % 100 != 0)
                return 29;
            else
                return 28;
        } else if ((month <= 6 && month % 2 == 0) || (month > 6 && month % 2 == 1))
            return 31;
        else
            return 30;
    },

    // =======================================================================================
    // =====================グリッド明細中の列の幅は、JS定義からCSSへ反映=====================
    // =======================================================================================
    changeRCWCss : function (theClass, element, value) {
        var cssRules;
        var sheet;
        var changed;

        if (document.all) {
            cssRules = 'rules';
        }
        else if (document.getElementById) {
            cssRules = 'cssRules';
        }

        for (var S = 0; S < document.styleSheets.length; S++) {

            sheet = document.styleSheets[S];

            //if (sheet.href == null) continue;
            //if (sheet.href.indexOf('comm') == -1) continue; // リリース

            changed = false;

            for (var R = 0; R < sheet[cssRules].length; R++) {
                if (sheet[cssRules][R].selectorText == theClass) {
                    sheet[cssRules][R].style[element] = value;
                    changed = true;
                }
            }

            //if (changed == false) {
            //    TorihikiUtils.insertRule(sheet, theClass, element + ":" + value + ";", 0);
            //}
        }
    },

    insertRule :function (sheet, selectorText, cssText, position) {
        if (sheet.insertRule) {
            sheet.insertRule(selectorText + "{" + cssText + "}", position);
        }
        else
            if (sheet.addRule) {  // sheet.deleteRuleをサポートしないBrowser用
                sheet.addRule(selectorText, cssText, position);
            }
    },

    deleteRule: function (sheet, index) {
        if (sheet.deleteRule) {
            sheet.deleteRule(index);
        }
        else
            if (sheet.removeRule) {  // sheet.deleteRuleをサポートしないBrowser用
                sheet.removeRule(index);
            }
    },

    // =======================================================================================
    // =====================クライアントプリンター設定処理=====================
    // =======================================================================================
    /**
     * 既設定されたプリンターを取得する。
     * */
    getPrinterSetting: function () {
        // クライアント伝票用プリンター
        var printerInfo = undefined;
        try {
            printerInfo = TorihikiUtils.getLocalDataOfJson(TorihikiGlobal.DempyoPrinterLocalKey);
        } catch { }

        if (printerInfo == undefined) {
            TorihikiGlobal.DempyoPrinter = undefined;
            TorihikiGlobal.DempyoPrinterModel = undefined;
            TorihikiGlobal.DempyoPrinterTray = undefined;
        }
        else {
            TorihikiGlobal.DempyoPrinter = printerInfo.printerName;
            TorihikiGlobal.DempyoPrinterModel = printerInfo.printerModel;
            TorihikiGlobal.DempyoPrinterTray = printerInfo.printerTray;

            if (TorihikiUtils.isNullOrEmptyOrWhiteSpace(TorihikiGlobal.DempyoPrinter)) {
                TorihikiGlobal.DempyoPrinter = undefined;
                TorihikiGlobal.DempyoPrinterModel = undefined;
                TorihikiGlobal.DempyoPrinterTray = undefined;
            }
        }

        // クライアントその他帳票用プリンター
        printerInfo = undefined;
        try {
            printerInfo = TorihikiUtils.getLocalDataOfJson(TorihikiGlobal.OtherPrinterLocalKey);
        } catch { }

        if (printerInfo == undefined) {
            TorihikiGlobal.OtherPrinter = undefined;
            TorihikiGlobal.OtherPrinterModel = undefined;
            TorihikiGlobal.OtherPrinterTray = undefined;
        }
        else {
            TorihikiGlobal.OtherPrinter = printerInfo.printerName;
            TorihikiGlobal.OtherPrinterModel = printerInfo.printerModel;
            TorihikiGlobal.OtherPrinterTray = printerInfo.printerTray;

            if (TorihikiUtils.isNullOrEmptyOrWhiteSpace(TorihikiGlobal.OtherPrinter)) {
                TorihikiGlobal.OtherPrinter = undefined;
                TorihikiGlobal.OtherPrinterModel = undefined;
                TorihikiGlobal.OtherPrinterTray = undefined;
            }
        }
    },
    /**
     * 選択されたプリンターを保存する
     * @param {any} dempyoPrinter
     * @param {any} otherPrinter
     */
    savePrinterSetting: function (dempyoPrinter, otherPrinter) {

        // クライアント伝票用プリンター
        TorihikiUtils.saveLocalData(TorihikiGlobal.DempyoPrinterLocalKey, dempyoPrinter);

        // クライアントその他帳票用プリンター
        TorihikiUtils.saveLocalData(TorihikiGlobal.OtherPrinterLocalKey, otherPrinter);
    },
    /**
     * 種別よりプリンターを取得する。
     * @param {any} printerType
     */
    getPrinterByPrinterType: function (printerType) {
        var printer = undefined;

        // プリンター情報リフレッシュ
        TorihikiUtils.getPrinterSetting();

        // プリンターチェック
        if (!TorihikiUtils.checkClientPrinter(printerType)) {
            return printer;
        }

        // 伝票用場合
        switch (printerType) {
            case EnuPrinter.Dempyo:
                printer = TorihikiGlobal.DempyoPrinter;
                return printer;
            case EnuPrinter.Other:
            default:
                // その他帳票用
                printer = TorihikiGlobal.OtherPrinter;
                return printer;
        }
    },
    /**
     * 種別よりプリンターを取得する。
     * @param {any} printerType
     */
    getPrinterTrayByPrinterType: function (printerType) {

        var printerTray = undefined;

        // プリンター情報リフレッシュ
        TorihikiUtils.getPrinterSetting();

        // プリンターチェック
        if (!TorihikiUtils.checkClientPrinter(printerType)) {
            return printerTray;
        }

        // 伝票用場合
        switch (printerType) {
            case EnuPrinter.Dempyo:
                printerTray = TorihikiGlobal.DempyoPrinterTray;
                return printerTray;

            case EnuPrinter.Other:
            default:
                // その他帳票用
                printerTray = TorihikiGlobal.OtherPrinterTray;
                return printerTray;
        }
    },
    /**
     * プリンターチェック.
     * @param {any} printerType
     */
    checkPrinter: function (printerType) {

        switch (printerType) {
            // 伝票用場合
            case EnuPrinter.Dempyo:
                printer = TorihikiGlobal.DempyoPrinter;
                if (printer == undefined) {
                    TorihikiUtils.hideLoading();
                    TorihikiMsg.show('KYE000059', '伝票用');
                    return false;
                }
                else {
                    if (TorihikiGlobal.PrinterShiyoFlg == StaticConst.FLG_TRUE) {
                        if (TorihikiGlobal.Printers.filter(function (t) {
                            return t.printerKbn == '01' &&
                                TorihikiGlobal.DempyoPrinterModel.toUpperCase().indexOf(t.printerModelNm.toUpperCase()) > -1
                        }).length == 0) {
                            TorihikiUtils.hideLoading();
                            TorihikiMsg.show('KYE000060');
                            return false;
                        }
                    }
                }
                break;
            case EnuPrinter.Other:
                // その他帳票用
                printer = TorihikiGlobal.OtherPrinter;
                if (printer == undefined) {
                    TorihikiUtils.hideLoading();
                    TorihikiMsg.show('KYE000059', 'その他帳票用');
                    return false;
                }
                else {
                    if (TorihikiGlobal.PrinterShiyoFlg == StaticConst.FLG_TRUE) {
                        if (TorihikiGlobal.Printers.filter(function (t) {
                            return t.printerKbn == '02' &&
                                TorihikiGlobal.OtherPrinterModel.toUpperCase().indexOf(t.printerModelNm.toUpperCase()) > -1
                        }).length == 0) {
                            TorihikiUtils.hideLoading();
                            TorihikiMsg.show('KYE000060');
                            return false;
                        }
                    }
                }
                break;
            default:
                return true;
        }
        return true;
    },
    /**
     * クライアントプリンター設定チェック。
     */
    checkClientPrinter: function (printerType) {
        // プリンター情報リフレッシュ
        TorihikiUtils.getPrinterSetting();

        if (TorihikiUtils.isNullOrEmptyOrWhiteSpace(printerType)) {
            // 伝票用
            if (!TorihikiUtils.checkPrinter(EnuPrinter.Dempyo)) {
                return false;
            }
            // その他帳票用
            if (!TorihikiUtils.checkPrinter(EnuPrinter.Other)) {
                return false;
            }
        } else {
            if (!TorihikiUtils.checkPrinter(printerType)) {
                return false;
            }
        }
        return true;
    },
    /**
     * Html Decode
     * @param {any} value
     */
    htmlDecode: function (value) {
        return $('<div />').html(value).text();
    },
    /**
     * 編集ボタン非活性制御
     * @param {any} obj
     */
    henshuButtonDisalbe: function (obj) {
        let buttonForUpdateCounts = 0;
        if (typeof KosinKengenFlg === 'undefined') {
            return;
        }
        if (KosinKengenFlg == '0') {
            let footerButtons = $('.tcc-footer').find('button');
            footerButtons.each(function (a) {
                //if ($(this).hasClass("tcc-btn-blue")) {
                //    $(this).prop('disabled', true);
                //}
                if ($(this).text().trim() == '編集' ||
                    $(this).text().trim() == '確定' ||
                    $(this).text().trim() == '保存' ||
                    $(this).text().trim() == '申請') {

                    $(this).prop('disabled', true);
                    buttonForUpdateCounts++;
                }
            })

            if (buttonForUpdateCounts > 0) {
                TorihikiMsg.message("更新権限がありません");
            }
        }
    },
    dataUrlToBase64: function (file) {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = () => {
                const base64String = reader.result
                .replace('data:', '')
                .replace(/^.+,/, '');
                resolve(base64String)
            };
            reader.onerror = error => reject(error);
        });
    }
};