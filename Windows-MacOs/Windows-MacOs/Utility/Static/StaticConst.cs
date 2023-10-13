using System;
using System.Web;

namespace WinMacOs.Utility
{
    /// <summary>
    /// 定数定義.
    /// </summary>
    public class StaticConst : IDisposable
    {
        #region 日時フォーマット

        /// <summary>
        /// 日付フォーマット:yyyy年MM月dd日.
        /// </summary>
        public const string LOCAL_SHORT_DATE = "yyyy年MM月dd日";

        /// <summary>
        /// 日付フォーマット:yyyy/MM/dd.
        /// </summary>
        public const string SHORT_DATE = "yyyy/MM/dd";

        /// <summary>
        /// 日付フォーマット:yyyy/MM/dd HH:mm:ss.
        /// </summary>
        public const string LONG_DATE_TIME = "yyyy/MM/dd HH:mm:ss";

        /// <summary>
        /// 日付フォーマット:yyyy/MM/dd HH:mm.
        /// </summary>
        public const string LONG_DATE_TIME_MM = "yyyy/MM/dd HH:mm";

        /// <summary>
        /// 時間フォーマット:HH:mm:ss.
        /// </summary>
        public const string TIME_FMT = "HH:mm:ss";

        /// <summary>
        /// 時間フォーマット:HH:mm.
        /// </summary>
        public const string SHORT_TIME_FMT = "HH:mm";

        /// <summary>
        /// 日付フォーマット:MM/dd.
        /// </summary>
        public const string MMDD_DATE = "MM/dd";

        /// <summary>
        /// 日付フォーマット:yyyy/MM/dd (木).
        /// </summary>
        public const string SHORT_DATE_DDD = "yyyy/MM/dd (ddd)";

        /// <summary>
        /// 日付フォーマット:yyyy/MM.
        /// </summary>
        public const string SHORT_YM = "yyyy/MM";

        /// <summary>
        /// 日付フォーマット:yyyy/MM/dd HH:mm:ss.ffffff.
        /// </summary>
        public const string FULL_DATE = "yyyy/MM/dd HH:mm:ss.ffffff";

        /// <summary>
        /// 日付フォーマット:yyyyMMdd.
        /// </summary>
        public const string SHORT_DATE_STR = "yyyyMMdd";
        #endregion

        #region システム

        /// <summary>
        /// システムタイトル.
        /// </summary>
        public const string SYSTEM_TITLE = "ActiveConstrunct Windows MacOs";

        /// <summary>
        /// パスワード有効期限超えるの警告日数.
        /// </summary>
        public const long PASSWORD_EXPIRED_DAYS = 90;

        /// <summary>
        /// パスワード有効期限超えるの警告日数.
        /// </summary>
        public const long PASSWORD_EXPIRED_WARNING_DAYS = 10;

        /// <summary>
        /// 最大検索件数.
        /// </summary>
        public const int MAX_SEARCH_ROWS = 100;

        /// <summary>
        /// ホームページコメント通知表示期間(日).
        /// </summary>
        public const int COMMENT_SHOW_PERIOD = 30;

        /// <summary>
        /// ホームページお知らせ通知表示期間(日).
        /// </summary>
        public const int NOTICE_SHOW_PERIOD = 30;

        /// <summary>
        /// ファイルアップロードサイズ(100m).
        /// </summary>
        public const int FILE_UPLOAD_MAX_SIZE = 104857600;

        /// <summary>
        /// フラグ 1.
        /// </summary>
        public const string FLG_TRUE = "1";

        /// <summary>
        /// フラグ 0.
        /// </summary>
        public const string FLG_FALSE = "0";

        /// <summary>
        /// 表示モードキー.
        /// </summary>
        public const string SHOW_MODE_KEY = "DATA_SHOW_MODE";

        /// <summary>
        /// 命名空間開始.
        /// </summary>
        public const string NAMESPACE_START = "WinMacOs.";

        /// <summary>
        /// 命名空間開始.
        /// </summary>
        public const string SHOW_ERROR_VIEW = "ShowErrorMessage";

        /// <summary>
        /// 画面を遷移元に戻るキー.
        /// </summary>
        public const string BACK = "_Back";

        /// <summary>
        /// 画面をリロードキー.
        /// </summary>
        public const string RELOAD = "_Reload";

        /// <summary>
        /// テーブル列の区分キー開始.
        /// </summary>
        public const string TABLE_FIELD_COMMENT_KUBUN_KEY_START = "\\(区分№=";

        /// <summary>
        /// テーブル列の区分キー終了.
        /// </summary>
        public const string TABLE_FIELD_COMMENT_KUBUN_KEY_END = "\\)";

        /// <summary>
        /// ページセッションID.
        /// </summary>
        public const string PAGE_SESSION_KEY = "t";

        #endregion

        #region ビューデータのキー

        /// <summary>
        /// ビューデータの実行画面権限情報キー.
        /// </summary>
        public const string CUR_MENU_KENGEN = "__CurMenuKengen";

        #endregion

        #region ワークフロー連携

        /// <summary>
        /// ワークフロー連携処理結果(初期).
        /// </summary>
        public const string NOTES_WF_LINKAGE_RESULT_NONE = "0";

        /// <summary>
        /// ワークフロー連携処理結果(成功).
        /// </summary>
        public const string NOTES_WF_LINKAGE_RESULT_SUCCESS = "1";

        /// <summary>
        /// ワークフロー連携処理結果(失敗).
        /// </summary>
        public const string NOTES_WF_LINKAGE_RESULT_ERROR = "2";

        /// <summary>
        /// NOTESサーバーAPIキー.
        /// </summary>
        public const string WF_LINKAGE_API_KEY = "ApiKey";

        /// <summary>
        /// NOTESサーバーAPIキー.
        /// </summary>
        public const string NOTES_WF_LINKAGE_API_KEY = "ApiKey";

        #endregion

        #region 排他検証

        /// <summary>
        /// 排他検証キー.
        /// </summary>
        public static string[] ExclusiveCheckKey => new string[] { "UPD_CNT" };

        #endregion

        #region システム

        /// <summary>
        /// 開始URL.
        /// </summary>
        public static string SiteStartPath => "/".Equals(HttpContext.Current.Request.ApplicationPath) ? string.Empty : HttpContext.Current.Request.ApplicationPath;

        /// <summary>
        /// ログインURL.
        /// </summary>
        public static string LoginPath => SiteStartPath + WebContext.LOGIN_URL;
        #endregion

        /// <inheritdoc/>
        public void Dispose()
        {
        }
    }
}