using Microsoft.AspNet.Identity;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using WinMacOs.DataRepository.Utilities;
using WinMacOs.Models;
using WinMacOs.Utility.Extensions;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Business.Business.Filters
{
    public class JWTAuthorizeAttribute : AuthorizeAttribute
    {
        public JWTAuthorizeAttribute() : base()
        {
        }
        public override void OnAuthorization(AuthorizationContext filterContext)
        {
            if (filterContext.ActionDescriptor.GetCustomAttributes(typeof(AllowAnonymousAttribute), false).Length > 0)
            {
                ////固定Token（期限なし）を使用されたら、tokenの正しさと存在チェック
                //if (filterContext.Filters
                //    .Where(item => item is IFixedTokenFilter)
                //    .FirstOrDefault() is IFixedTokenFilter tokenFilter)
                //{
                //    tokenFilter.OnAuthorization(context);
                //    return;
                //}
                //匿名及びtokenが持ってくる場合は、ユーザーのIDをキャシュに保存して、UserHelper中にユーザー情報は取得できることを保証する
                if (!filterContext.HttpContext.User.Identity.IsAuthenticated
                    && !string.IsNullOrEmpty(filterContext.HttpContext.Request.Headers[AppSetting.TokenHeaderName]))
                {
                    filterContext.AddIdentity();
                }
                return;
            }

            if (!filterContext.HttpContext.User.Identity.IsAuthenticated)
            {
                Console.Write($"IsAuthenticated:{filterContext.HttpContext.User.Identity.IsAuthenticated}," +
                    $"userToken{UserContext.Current.Token}");

                filterContext.Unauthorized("タイムアウトしましたので、手動で再ログインしてください。");
                return;
            }

            DateTime expDate = ((ClaimsIdentity)filterContext.HttpContext.User.Identity).FindFirst(x => x.Type == JwtRegisteredClaimNames.Exp)
                .Value.GetTimeStampToDate();

            if (expDate < DateTime.Now)
            {
                filterContext.Unauthorized("タイムアウトしましたので、手動で再ログインしてください。");
                return;
            }
            //tokenをリフレッシュ
            //ハノイ側修正2022/12/01　課題管理表№35：「2022/12/01依頼分」「タイムアウト設定時間リセット機能を有効にしてください。」
            if ((expDate - DateTime.Now).TotalMinutes < AppSetting.ExpMinutes / 3 && filterContext.HttpContext.Request.Path != replaceTokenPath && filterContext.HttpContext.Request.Path != replaceTokenPath_T)
            {
                filterContext.HttpContext.Response.Headers.Add("reafs_exp", "1");
            }
        }
        private static readonly string replaceTokenPath = "/api/User/replaceToken";
        private static readonly string replaceTokenPath_T = "/api/Reafs_T/replaceToken";
    }
}
