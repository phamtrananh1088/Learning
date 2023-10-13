using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Web;
using WinMacOs.Models.Enums;
using WinMacOs.Utility.Extensions;

namespace WinMacOs.Models
{
    //レスポンス　共通エラーメッセージ内容
    public static class ResponseMsg
    {
        public static Dictionary<string, string> ListResponseMsg;
        public static string GetMsg(this ResponseType responseType)
        {
            string msg;
            switch (responseType)
            {
                case ResponseType.LoginExpiration:
                    msg = "ログイン有効期限が切れましたので、もう一度ログインしてください"; break;
                case ResponseType.ParametersLack:
                    msg = "パラメータが不足しています"; break;
                case ResponseType.NoRolePermissions:
                    msg = "処理権限がありません"; break;
                case ResponseType.ServerError:
                    msg = "サーバー処理でエラーが発生しました"; break;
                case ResponseType.BadRequest:
                    msg = "必要なパラメーターがありません。"; break;
                case ResponseType.SaveSuccess:
                    msg = "登録しました。"; break;
                case ResponseType.EidtSuccess:
                    msg = "更新しました。"; break;
                case ResponseType.DelSuccess:
                    msg = "削除しました。"; break;

                default: msg = responseType.ToString(); break;
            }
            return msg;
        }

        public static string GetMsg(this msgCode msgCode)
        {
            string msg = "";
            try
            {
                if (!(ListResponseMsg is null))
                {
                    msg = ListResponseMsg.GetValueOrDefault(Enum.GetName(typeof(msgCode), msgCode));
                }
            }
            catch (Exception ex)
            {
                Debug.WriteLine(ex);
            }
            return msg;
        }
    }
}