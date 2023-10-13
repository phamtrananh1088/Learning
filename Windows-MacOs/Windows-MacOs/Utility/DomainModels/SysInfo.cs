using System.Collections.Generic;
using System.Linq;
using System.Web;
using WinMacOs.Models;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Utility.DomainModels
{
    /// <summary>
    /// システム情報.
    /// </summary>
    public class SysInfo
    {
        /// <summary>
        /// ユーザー　セッションキー. 
        /// </summary>
        public const string SessionUserKey = "TORIHIKI-User";

        /// <summary>
        /// メニュー情報　セッションキー.
        /// </summary>
        public const string SessionAllMenuKey = "TORIHIKI-MENU-ALL";

        /// <summary>
        /// ログイン回数.
        /// </summary>
        public const string SessionLoginTimes = "TORIHIKI-Login-Times";

        /// <summary>
        /// インスタンス作成.
        /// </summary>
        public static SysInfo Instance
        {
            get { return SingletonProvider<SysInfo>.Instance; }
        }

        /// <summary>
        /// ログインユーザ.
        /// </summary>
        public LoginUser LoginUser
        {
            get
            {
                return HttpContext.Current.Session == null || HttpContext.Current.Session[SessionUserKey] == null ? new LoginUser() : (LoginUser)HttpContext.Current.Session[SessionUserKey];
            }

            set
            {
                HttpContext.Current.Session[SessionUserKey] = value;

                lock (AppSetting.LoginUsers)
                {
                    LoginUser user = AppSetting.LoginUsers.FirstOrDefault(n => n.WORK_ID.Equals(value.WORK_ID));
                    if (user == null)
                    {
                        AppSetting.LoginUsers.Add(value);
                    }
                    else
                    {
                        user = value;
                    }
                }
            }
        }

        /// <summary>
        /// 全体メニュー.
        /// </summary>
        public List<MenuModel> AllMenu
        {
            get { return HttpContext.Current.Session == null ? null : (List<MenuModel>)HttpContext.Current.Session[SessionAllMenuKey]; }
            set { HttpContext.Current.Session[SessionAllMenuKey] = value; }
        }

        /// <summary>
        /// ログインユーザ.
        /// </summary>
        public int LoginTimes
        {
            get
            {
                return HttpContext.Current.Session == null || HttpContext.Current.Session[SessionLoginTimes] == null ? 0 : (int)HttpContext.Current.Session[SessionLoginTimes];
            }

            set
            {
                HttpContext.Current.Session[SessionLoginTimes] = value;
            }
        }
    }
}