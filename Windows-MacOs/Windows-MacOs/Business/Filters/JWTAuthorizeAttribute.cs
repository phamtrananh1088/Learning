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
        private static readonly string replaceTokenPath = "/api/User/replaceToken";
        private static readonly string replaceTokenPath_T = "/api/Reafs_T/replaceToken";
        public JWTAuthorizeAttribute() : base()
        {
        }
        public override void OnAuthorization(AuthorizationContext filterContext)
        {
            if (filterContext == null)
            {
                throw new ArgumentNullException("filterContext");
            }

            //if (OutputCacheAttribute.IsChildActionCacheActive(filterContext))
            //{
            //    throw new InvalidOperationException("AuthorizeAttribute_CannotUseWithinChildActionCache");
            //}
            //匿名及びtokenが持ってくる場合は、ユーザーのIDをキャシュに保存して、UserHelper中にユーザー情報は取得できることを保証する
            if (!filterContext.HttpContext.User.Identity.IsAuthenticated
                && !string.IsNullOrEmpty(filterContext.HttpContext.Request.Headers[AppSetting.TokenHeaderName]))
            {
                filterContext.AddIdentity();
            }
            if (!filterContext.ActionDescriptor.IsDefined(typeof(AllowAnonymousAttribute), inherit: true) && !filterContext.ActionDescriptor.ControllerDescriptor.IsDefined(typeof(AllowAnonymousAttribute), inherit: true))
            {
                if (AuthorizeCore(filterContext.HttpContext))
                {
                    return;
                }
                else
                {
                    HandleUnauthorizedRequest(filterContext);
                }
            }
        }

        protected override bool AuthorizeCore(HttpContextBase httpContext)
        {
            if(!base.AuthorizeCore(httpContext))
            {
                Console.Write($"IsAuthenticated:{httpContext.User.Identity.IsAuthenticated}," +
                    $"userToken{UserContext.Current.Token}");
                return false;
            }

            DateTime expDate = JwtHelper.GetExp(httpContext.Request.Headers[AppSetting.TokenHeaderName]);

            if (expDate < DateTime.Now)
            {
                return false;
            }
            //tokenをリフレッシュ
            //ハノイ側修正2022/12/01　課題管理表№35：「2022/12/01依頼分」「タイムアウト設定時間リセット機能を有効にしてください。」
            if ((expDate - DateTime.Now).TotalMinutes < AppSetting.ExpMinutes / 3 && httpContext.Request.Path != replaceTokenPath && httpContext.Request.Path != replaceTokenPath_T)
            {
                httpContext.Response.Headers.Add("reafs_exp", "1");
            }
            return true;
        }

        protected override void HandleUnauthorizedRequest(AuthorizationContext filterContext)
        {
            filterContext.Unauthorized("タイムアウトしましたので、手動で再ログインしてください。");
        }
    }
}
