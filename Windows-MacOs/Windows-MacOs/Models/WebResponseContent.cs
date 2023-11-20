using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;
using System.Threading.Tasks;
using System.Web;
using WinMacOs.Models.Enums;

namespace WinMacOs.Models
{
    public class WebResponseContent
    {
        //レスポンス内容　クラス
        public WebResponseContent()
        {
            //
            // コンストラクタ ロジックをここに追加してください。
            //
        }
        public WebResponseContent(bool status)
        {
            this.Status = status;
        }

        //処理状態
        [JsonProperty("status")]
        public bool Status { get; set; } = true;
        //Httpコード
        [JsonProperty("code")]
        public string Code { get; set; }
        //エラーメッセージ
        [JsonProperty("message")]
        public string Message { get; set; }
        //レスポンスデータ
        [JsonProperty("data")]
        public object Data { get; set; }

        #region Response OK
        public WebResponseContent OK()
        {
            this.Status = true;
            return this;
        }
        public async Task<WebResponseContent> OKAsync(string message = null, object data = null)
        {
            return await Task.Factory.StartNew(() => OK(message, data));
        }
        public WebResponseContent OK(string message = null, object data = null)
        {
            this.Status = true;
            this.Message = message;
            this.Data = data;
            return this;
        }
        public WebResponseContent OK<TValue>(TValue responseType) where TValue : struct, IConvertible
        {
            return Set(responseType, "", true);
        }
        public WebResponseContent OK<TValue>(TValue responseType, msgCode msgCode) where TValue : struct, IConvertible
        {
            return Set(responseType, msgCode, true);
        }
        #endregion

        #region Response Error
        public WebResponseContent Error(string message = null)
        {
            this.Status = false;
            this.Message = message;
            return this;
        }
        public WebResponseContent Error<TValue>(TValue responseType, string msg = "") where TValue : struct, IConvertible
        {
            return Set(responseType, msg, false);
        }
        public WebResponseContent Error<TValue>(TValue responseType, msgCode msgCode) where TValue : struct, IConvertible
        {
            return Set(responseType, msgCode, false);
        }
        #endregion

        #region レスポンス内容を設定
        public WebResponseContent Set<TValue>(TValue responseType, msgCode msgCode) where TValue : struct, IConvertible
        {
            bool? b = null;
            return this.Set(responseType, msgCode, b);
        }
        public WebResponseContent Set<TValue>(TValue responseType, msgCode msgCode, bool? status) where TValue : struct, IConvertible
        {
            string msg = msgCode.GetMsg();
            return this.Set(responseType, msg, status);
        }
        public WebResponseContent Set<TValue>(TValue responseType, string msg) where TValue : struct, IConvertible
        {
            bool? b = null;
            return this.Set(responseType, msg, b);
        }
        //public WebResponseContent Set(ResponseType responseType, string msg, bool? status)
        //{
        //    if (status != null)
        //    {
        //        this.Status = (bool)status;
        //    }
        //    this.Code = ((int)responseType).ToString();
        //    if (!string.IsNullOrEmpty(msg))
        //    {
        //        Message = msg;
        //        return this;
        //    }

        //    return this;
        //}

        public WebResponseContent Set<TValue>(TValue responseType, string msg, bool? status) where TValue : struct, IConvertible
        {
            if (!typeof(TValue).IsEnum)
            {
                throw new ArgumentException(nameof(responseType));
            }

            if (status != null)
            {
                this.Status = (bool)status;
            }
            this.Code = ((int)(object)responseType).ToString();
            if (!string.IsNullOrEmpty(msg))
            {
                Message = msg;
            }
            else if (responseType.GetType() == typeof(ResponseType))
            {
                ResponseType resType = (ResponseType)Enum.Parse(typeof(TValue), responseType.ToString());
                Message = resType.GetMsg();
            }
            return this;
        }
        #endregion
    }
}