using System.Linq;
using System.Web;
using System.Web.Routing;
using WinMacOs.Models;
using WinMacOs.Utility.DomainModels;

namespace WinMacOs.Utility.Utils
{
    /// <summary>
    /// ルートツール.
    /// </summary>
    public static class RouteUtils
    {
        /// <summary>
        /// URLよりルート情報を取得する.
        /// </summary>
        /// <param name="sUrl">URL.</param>
        /// <returns>ルートデータ.</returns>
        public static RouteData GetRouteDataByUrl(string sUrl)
        {
            if (sUrl != null && sUrl.ToLower().StartsWith("~/api/"))
            {
                RouteData routedataApi = new RouteData();
                routedataApi.DataTokens["area"] = string.Empty;
                // コントローラー
                routedataApi.Values["controller"] = "api";
                // アクション
                routedataApi.Values["action"] = sUrl.Substring(6);
                return routedataApi;
            }

            RequestContext rc = HttpContext.Current.Request.RequestContext;
            RouteData routedata = null;
            if (HttpContext.Current.Request.AppRelativeCurrentExecutionFilePath.Equals(sUrl) || HttpContext.Current.Request.CurrentExecutionFilePath.Equals(sUrl))
            {
                if (rc != null && rc.RouteData != null)
                {
                    routedata = rc.RouteData;
                }
            }

            if (routedata == null)
            {
                routedata = RouteTable.Routes.GetRouteData(new RewritedHttpContextBase(sUrl));
            }

            if (!routedata.DataTokens.ContainsKey("area"))
            {
                routedata.DataTokens.Add("area", string.Empty);
            }

            return routedata;
        }

        /// <summary>
        /// URLよりルートルールでアクションURLを取得する.
        /// </summary>
        /// <param name="sUrl">URL.</param>
        /// <returns>ルートデータ.</returns>
        public static string GetActionUrl(string sUrl)
        {
            RouteData routedata = RouteUtils.GetRouteDataByUrl(sUrl);
            // エリア
            string sArea = routedata.DataTokens["area"].ToString();
            // コントローラー
            string sController = routedata.Values["controller"].ToString();
            // アクション
            string sAction = string.Empty;

            if (routedata.Values["action"] != null)
            {
                // アクション
                sAction = routedata.Values["action"].ToString();
            }

            string sActionUrl = (string.IsNullOrEmpty(sArea) ? string.Empty : "/" + sArea) + "/" + sController + (string.IsNullOrEmpty(sAction) ? string.Empty : "/" + sAction);

            return sActionUrl;
        }

        /// <summary>
        /// クライアント側IPアドレスを取得する.
        /// </summary>
        /// <returns>クライアントのIPアドレス.</returns>
        public static string GetClientIP()
        {
            string sResult = "SYS_BASE";
            try
            {
                sResult = HttpContext.Current.Request.ServerVariables["HTTP_X_FORWARDED_FOR"];
                if (string.IsNullOrEmpty(sResult))
                {
                    sResult = HttpContext.Current.Request.ServerVariables["REMOTE_ADDR"];
                }

                if (string.IsNullOrEmpty(sResult))
                {
                    sResult = HttpContext.Current.Request.UserHostAddress;
                }
            }
            catch
            {
            }

            return sResult;
        }

        /// <summary>
        /// プログラムIDを取得する.
        /// </summary>
        /// <returns>プログラムID.</returns>
        public static string GetPGID()
        {
            string sUrl = HttpContext.Current.Request.AppRelativeCurrentExecutionFilePath;
            RouteData routedata = RouteUtils.GetRouteDataByUrl(sUrl);

            return StringUtils.NullToEmpty(routedata.Values["controller"]);
        }

        /// <summary>
        /// プログラムIDを取得する.
        /// </summary>
        /// <returns>プログラムID.</returns>
        public static string GetReferrerPGID()
        {
            string sUrl = "~" + GetRefUrl();
            RouteData routedata = RouteUtils.GetRouteDataByUrl(sUrl);

            return StringUtils.NullToEmpty(routedata.Values["controller"]);
        }

        /// <summary>
        /// 遷移元URL取得する.
        /// </summary>
        /// <returns>遷移元URL.</returns>
        public static string GetRefUrl()
        {
            if (!string.IsNullOrEmpty(StaticConst.SiteStartPath) && !"/".Equals(StaticConst.SiteStartPath))
            {
                if (HttpContext.Current.Request.UrlReferrer == null)
                {
                    return string.Empty;
                }

                string sRrl = HttpContext.Current.Request.UrlReferrer.AbsolutePath.ToUpper();
                string sRealurl = HttpContext.Current.Request.UrlReferrer.AbsolutePath;
                int iPathStart = sRrl.IndexOf(StaticConst.SiteStartPath.ToUpper());
                if (iPathStart == 0)
                {
                    return sRealurl.Substring(StaticConst.SiteStartPath.Length);
                }
                else if (iPathStart > 0)
                {
                    string sLeft = sRealurl.Substring(0, iPathStart);
                    return sLeft + sRealurl.Substring(iPathStart + sLeft.Length);
                }
            }

            if (HttpContext.Current.Request.UrlReferrer == null)
            {
                return string.Empty;
            }

            return HttpContext.Current.Request.UrlReferrer.AbsolutePath;
        }

        /// <summary>
        /// 画面タイトル取得する.
        /// </summary>
        /// <param name="sUrl">Url.</param>
        /// <returns>画面タイトル.</returns>
        public static string GetPageTitle(string sUrl = null)
        {
            if (string.IsNullOrEmpty(sUrl))
            {
                sUrl = HttpContext.Current.Request.AppRelativeCurrentExecutionFilePath;
            }

            if (!"~".Equals(sUrl.Substring(0, 1)))
            {
                sUrl = "~" + sUrl;
            }

            RouteData routedata = RouteUtils.GetRouteDataByUrl(sUrl);
            // エリア
            string sArea = routedata.DataTokens["area"].ToString();
            // コントローラー
            string sController = routedata.Values["controller"].ToString();
            // アクション
            string sAction = string.Empty;

            if (routedata.Values["action"] != null)
            {
                // アクション
                sAction = routedata.Values["action"].ToString();
            }

            string sActionUrl = "/" + sArea + "/" + sController + "/" + sAction;
            string sControlUrl = "/" + sArea + "/" + sController;

            if (!sAction.ToUpper().Equals("LOGIN"))
            {
                if (SysInfo.Instance.AllMenu != null)
                {
                    MenuModel menu = SysInfo.Instance.AllMenu.FirstOrDefault(n => StringUtils.NullToEmpty(n.URL).Equals(sActionUrl));
                    if (menu == null)
                    {
                        // コントローラーで検索
                        menu = SysInfo.Instance.AllMenu.FirstOrDefault(n => StringUtils.NullToEmpty(n.URL).Contains(sControlUrl));
                    }

                    if (menu != null)
                    {
                        return menu.MENU_NM;
                    }
                }

                return string.Empty;
            }
            else
            {
                return "ログイン";
            }
        }

        /// <summary>
        /// HTTP 固有の情報を含むクラスの基底クラス.
        /// </summary>
        private class RewritedHttpContextBase : HttpContextBase
        {
            /// <summary>
            /// モックリクエスト.
            /// </summary>
            private readonly HttpRequestBase mockHttpRequestBase;

            /// <summary>
            /// コンストラクタ.
            /// </summary>
            /// <param name="sAppRelativeUrl">Url.</param>
            public RewritedHttpContextBase(string sAppRelativeUrl)
            {
                this.mockHttpRequestBase = new MockHttpRequestBase(sAppRelativeUrl);
            }

            /// <summary>
            /// モックリクエスト.
            /// </summary>
            public override HttpRequestBase Request
            {
                get
                {
                    return mockHttpRequestBase;
                }
            }

            /// <summary>
            /// モック 基底クラス.
            /// </summary>
            private class MockHttpRequestBase : HttpRequestBase
            {
                private readonly string appRelativeUrl;

                public MockHttpRequestBase(string sAppRelativeUrl)
                {
                    this.appRelativeUrl = sAppRelativeUrl;
                }

                public override string AppRelativeCurrentExecutionFilePath
                {
                    get { return appRelativeUrl; }
                }

                public override string PathInfo
                {
                    get { return string.Empty; }
                }
            }
        }
    }
}