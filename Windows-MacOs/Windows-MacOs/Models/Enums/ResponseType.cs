using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WinMacOs.Models.Enums
{
    //レスポンスの形
    public enum ResponseType
    {
        ServerError = 500,
        BadRequest = 400,
        LoginExpiration = 302,
        ParametersLack = 303,
        TokenExpiration,
        NoRolePermissions,
        LoginSuccess,
        LoginError,
        SaveSuccess,
        EidtSuccess,
        DelSuccess,
        Other
    }

    public enum ResAuthType
    {
        //ServerError = -5,
        //LoginExpiration = -4,
        ServerError = -12,
        LoginExpiration = -11,
        PwLapse = -5, //PW失効
        IdExpired = -4, //ID期限切れ
        IdDeleted = -3, //ID削除済
        PasswordExpired = -2, //PW期限切れ	
        IdOrPassInvalid = -1, //ID、PW不一致	
        NotApplicable = 0, //該当なし		
        LoginSuccess = 1, //認証OK		
    }
}