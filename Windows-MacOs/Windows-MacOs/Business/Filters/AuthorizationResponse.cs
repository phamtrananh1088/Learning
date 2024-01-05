using Microsoft.AspNet.Identity;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Net;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using WinMacOs.DataRepository.Utilities;
using WinMacOs.Models;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Business.Business.Filters
{
    public class ContentResult : ActionResult
    {
        public ContentResult()
        {

        }

        //
        // Summary:
        //     Gets or set the content representing the body of the response.
        public string Content { get; set; }
        //
        // Summary:
        //     Gets or sets the Content-Type header for the response.
        public string ContentType { get; set; }
        //
        // Summary:
        //     Gets or sets the HTTP status code.
        public int StatusCode { get; set; }

        public override void ExecuteResult(ControllerContext context)
        {
            if (context == null)
            {
                throw new ArgumentNullException("context");
            }

            context.HttpContext.Response.StatusCode = StatusCode;
            if (ContentType != null)
            {
                context.HttpContext.Response.ContentType = ContentType;
            }
            context.HttpContext.Response.Write(Content);
        }
    }
    public static class AuthorizationResponse
    {
        public static AuthorizationContext FilterResult(
          this AuthorizationContext context,
            HttpStatusCode statusCode,
            string message = null)
        {
            context.Result = new ContentResult()
            {
                Content = System.Text.Json.JsonSerializer.Serialize(new { message, status = false, code = (int)statusCode }),
                ContentType = "application/json",
                StatusCode = (int)statusCode
            };
            //Logger.Info(LoggerType.ApiAuthorize, message);
            return context;
        }

        public static AuthorizationContext Unauthorized(this AuthorizationContext context, string message = null)
        {
            return context.FilterResult(HttpStatusCode.Unauthorized, message);
        }

        public static void AddIdentity(this AuthorizationContext context, string userId = null)
        {
            string _userId = userId ?? JwtHelper.GetUserId(context.HttpContext.Request.Headers[AppSetting.TokenHeaderName]);
            if (_userId == null || _userId == string.Empty) return;

            //ログインIDをコンテキストに保存する。定義対象はAddScoped形でコンテキストにDIする。
            var claims = new Claim[] { new Claim(JwtRegisteredClaimNames.Jti, _userId) };
            context.HttpContext.User = new ClaimsPrincipal(new ClaimsIdentity(claims, Enum.GetName(typeof(AuthenticationTypes), AuthenticationTypes.Basic)));
        }
    }
    internal enum AuthenticationTypes
    {
        Basic,
        Federation
    }
}
