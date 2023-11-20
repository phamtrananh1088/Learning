using System;
using System.Web;
using System.Web.Mvc;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;
using WinMacOs.Utility;
using WinMacOs.Utils;
using WinMacOs.Utils.Resolver;

namespace WinMacOs.ActionFilter
{
    /// <summary>
    /// JSON戻る値フィルター.
    /// </summary>
    public class LargeJsonNetFilterAttribute : ActionFilterAttribute
    {
        /// <summary>
        /// 日付フォーマット.
        /// </summary>
        private string sDateFormat;

        /// <summary>
        /// Nullを空白に変換フラグ.
        /// </summary>
        private bool bWriteNullToEmpty;

        /// <summary>
        /// 第一文字のみを小文字に変換する.
        /// </summary>
        private bool bFirstWordLowerOnly;

        /// <summary>
        /// キーを小文字に変換する.
        /// </summary>
        private bool bKeyWordLower;

        /// <summary>
        /// コンストラクタ.
        /// </summary>
        /// <param name="sDateFormat">日付フォーマット.</param>
        /// <param name="bWriteNullToEmpty">Nullを空白に変換する.</param>
        /// <param name="bKeyWordLower">文字を小文字に変換する.</param>
        /// <param name="bFirstWordLowerOnly">第一文字のみを小文字に変換する.</param>
        public LargeJsonNetFilterAttribute(string sDateFormat = "", bool bWriteNullToEmpty = false, bool bKeyWordLower = false, bool bFirstWordLowerOnly = false)
        {
            this.sDateFormat = sDateFormat;
            this.bWriteNullToEmpty = bWriteNullToEmpty;
            this.bFirstWordLowerOnly = bFirstWordLowerOnly;
            this.bKeyWordLower = bKeyWordLower;
        }

        /// <summary>
        /// アクション メソッドの実行後.
        /// </summary>
        /// <param name="filterContext">コンテキスト.</param>
        public override void OnActionExecuted(ActionExecutedContext filterContext)
        {
            // 戻る値はJsonReult以外とき
            if (filterContext.Result is JsonResult == false)
                return;

            // 戻る値変換
            filterContext.Result = new CustomJsonResult((JsonResult)filterContext.Result, this.sDateFormat, this.bWriteNullToEmpty, this.bKeyWordLower, this.bFirstWordLowerOnly);
        }

        /// <summary>
        /// JSONデータ変換.
        /// </summary>
        private class CustomJsonResult : JsonResult
        {
            /// <summary>
            /// 日付フォーマット.
            /// </summary>
            private string sDateFormat;

            /// <summary>
            /// Nullを空白に変換フラグ.
            /// </summary>
            private bool bWriteNullToEmpty;

            /// <summary>
            /// 第一文字のみを小文字に変換する.
            /// </summary>
            private bool bFirstWordLowerOnly;

            /// <summary>
            /// キーを小文字に変換する.
            /// </summary>
            private bool bKeyWordLower;

            /// <summary>
            /// コンストラクタ.
            /// </summary>
            /// <param name="jsonResult">JsonResultデータ.</param>
            /// <param name="dateFormat">日付フォーマット.</param>
            /// <param name="bWriteNullToEmpty">Nullを空白に変換する.</param>
            /// <param name="bKeyWordLower">文字を小文字に変換する.</param>
            /// <param name="bFirstWordLowerOnly">第一文字のみを小文字に変換する.</param>
            public CustomJsonResult(JsonResult jsonResult, string sDateFormat, bool bWriteNullToEmpty, bool bKeyWordLower, bool bFirstWordLowerOnly)
            {
                this.ContentEncoding = jsonResult.ContentEncoding;
                this.ContentType = jsonResult.ContentType;
                this.Data = jsonResult.Data;
                this.JsonRequestBehavior = jsonResult.JsonRequestBehavior;
                this.MaxJsonLength = int.MaxValue;
                this.RecursionLimit = jsonResult.RecursionLimit;

                this.sDateFormat = sDateFormat;
                this.bWriteNullToEmpty = bWriteNullToEmpty;
                this.bKeyWordLower = bKeyWordLower;
                this.bFirstWordLowerOnly = bFirstWordLowerOnly;
            }

            /// <summary>
            /// JSON変換.
            /// </summary>
            /// <param name="context">コンテキスト.</param>
            public override void ExecuteResult(ControllerContext context)
            {
                // 内容なし
                if (context == null)
                    throw new ArgumentNullException("context");

                // GETできない設定するとき、
                if (this.JsonRequestBehavior == JsonRequestBehavior.DenyGet
                    && string.Equals(context.HttpContext.Request.HttpMethod, "GET", StringComparison.OrdinalIgnoreCase))
                    throw new InvalidOperationException("GET not allowed! Change JsonRequestBehavior to AllowGet.");

                // レスポンス情報設定
                HttpResponseBase response = context.HttpContext.Response;

                response.ContentType = string.IsNullOrEmpty(this.ContentType) ? "application/json" : this.ContentType;

                if (this.ContentEncoding != null)
                    response.ContentEncoding = this.ContentEncoding;

                // データを変換する
                // 小文字に変換
                if (this.Data != null)
                {
                    JsonSerializerSettings js = new JsonSerializerSettings();
                    if (this.bKeyWordLower)
                    {
                        if (this.bFirstWordLowerOnly)
                        {
                            js.ContractResolver = new FirstWordLowerCaseContractResolver(this.bWriteNullToEmpty);
                        }
                        else
                        {
                            js.ContractResolver = new LowerCaseContractResolver(this.bWriteNullToEmpty);
                        }
                    }
                    else
                    {
                        if (bWriteNullToEmpty)
                        {
                            js.ContractResolver = new NullToEmptyStringContractResolver(this.bWriteNullToEmpty);
                        }
                    }

                    if (string.IsNullOrEmpty(this.sDateFormat))
                    {
                        js.DateFormatString = StaticConst.LONG_DATE_TIME;
                    }
                    else
                    {
                        js.DateFormatString = this.sDateFormat;
                    }

                    string sJson = JsonConvert.SerializeObject(
                        this.Data,
                        js);

                    response.Write(sJson);
                }
            }
        }
    }
}