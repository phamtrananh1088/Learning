
using System;
using System.Linq;
using System.Security.Claims;
using System.Collections.Generic;
using System.Web.Mvc;
using WinMacOs.DataRepository.AutofacManager;
using WinMacOs.Utility.DomainModels;
using WinMacOs.DataRepository.DBManager;
using WinMacOs.Utility.TableModels;
using WinMacOs.Utility.Utils;
using WinMacOs.Utility.CacheManager;
using System.Web;

namespace WinMacOs.DataRepository.Utilities
{

    public class UserContext
    {
        /// <summary>
        /// メモリ読み込む効率ため、UserContextをDIに注入する
        /// UserContextの属性は一回のみ読み込むため、検索効率を上げる
        /// </summary>
        public static UserContext Current
        {
            get
            {
                return DependencyResolver.Current.GetService(typeof(UserContext)) as UserContext;
                //return Context.RequestServices?.GetService(typeof(UserContext)) as UserContext;
            }
        }

        public UserContext(HttpContextBase Context)
        {
            this.HttpContext = Context ?? throw new Exception("HttpContextBaseはインスタンス化されません。");
        }

        private HttpContextBase HttpContext { get; set; }

        private static ICacheService CacheService
        {
            get { return GetService<ICacheService>(); }
        }

        private static T GetService<T>() where T : class
        {
            return AutofacContainerModule.GetService<T>();
        }

        public UserInfo UserInfo
        {
            get
            {
                if (_userInfo != null)
                {
                    return _userInfo;
                }
                return GetUserInfo(ログインID);
            }
        }

        private UserInfo _userInfo { get; set; }

        public UserInfo GetUserInfo(string ログインID)
        {
            if (_userInfo != null) 
                return _userInfo;

            if (ログインID == null || ログインID.Length <= 0)
            {
                _userInfo = new UserInfo();
                return _userInfo;
            }

            List<MultiToken> _tokens = DBServerProvider.DbContext.Set<F140_ログイン認証ファイル>()
                .Where(x => x.ログインID == ログインID).Select(s => new MultiToken()
                {
                    連番 = s.連番,
                    Token = s.Token,
                }).ToList();

            _userInfo = CacheService.Get<UserInfo>(ログインID);

            if (_userInfo != null && _userInfo.ログインID != null)
            {
                foreach (var item in _tokens)
                {
                    _userInfo.Tokens.Add(item);
                }

                if (_userInfo != null && _userInfo.ログインID != null) return _userInfo;
                return _userInfo;
            }

            _userInfo = DBServerProvider.DbContext.Set<M003_社員マスタ>()
                .Where(x => x.ログインID == ログインID).Select(s => new UserInfo()
                {
                    ログインID = ログインID,
                    社員ID = s.社員ID.ToString(),
                    社員名 = s.社員名,
                })
                .FirstOrDefault();

            // Reafs-T用に処理追加
            if(_userInfo == null)
            {
                _userInfo = DBServerProvider.DbContext.Set<M015_業者ユーザマスタ>()
                .Where(x => x.ユーザーＩＤ == ログインID).Select(s => new UserInfo()
                {
                    ログインID = ログインID,
                    社員ID = s.ユーザーＩＤ.ToString(),
                    社員名 = s.ユーザー名,
                })
                .FirstOrDefault();
            }


            if (_userInfo.Tokens == null)
            {
                _userInfo.Tokens = new List<MultiToken>();
            }

            foreach (var item in _tokens)
            {
                _userInfo.Tokens.Add(item);
            }

            if (_userInfo != null && _userInfo.ログインID != null)
            {
                //2022/10/20 DHK.Yan UPD キャシュ有効時限を設定する、3600秒
                // CacheService.AddObject(ログインID, _userInfo, 3600, true);
                // ExpMinutesと合わせる
                CacheService.AddObject(ログインID, _userInfo, (AppSetting.ExpMinutes * 60), true);
            }
            return _userInfo ?? new UserInfo();
        }

        public LoginInfo LoginInfo
        {
            get
            {
                if (_ログイン情報 != null)
                {
                    return _ログイン情報;
                }
                return Get部署情報();
            }
        }

        private LoginInfo _ログイン情報 { get; set; }

        public LoginInfo Get部署情報()
        {
            if (_ログイン情報 != null) return _ログイン情報;

            // 役職コード'0'の場合。'9999'に置換する　 -- M004.役職コード,
            string sql =
                        @" SELECT                                 
                                 CASE WHEN M004.役職コード = '0' THEN '9999' ELSE M004.役職コード END AS 役職コード,
                                 M004.役職名,
                                 M004.役職兼務区分,
                                 M004.社員ID,
                                 M004.事務所コード,
                                 M004.営業所コード,
                                 M004.部コード,
                                 M004.課コード,
                                 M004.係コード,
                                 M004.組織コード,
                                 M002.組織名,
                                 M002.部門名,
                                 M003.社員名,
                                 M004.事務所コード 
                                    + M004.営業所コード 
                                    + M004.部コード 
                                    + M004.課コード 
                                    + M004.係コード 
                                 AS コード,
                                 M004.事業会社コード,
                                 ' ' AS コードおよび名称
                         FROM 
                             M003_社員マスタ AS M003, 
                             M004_兼務マスタ AS M004 
                         LEFT OUTER JOIN 
                             M002_組織マスタ AS M002 
                           ON 
                             M002.事務所コード = M004.事務所コード 
                           AND 
                             M002.営業所コード = M004.営業所コード 
                           AND 
                             M002.部コード = M004.部コード 
                           AND 
                             M002.課コード = M004.課コード 
                           AND 
                             M002.係コード = M004.係コード 
                           AND 
                             M002.削除フラグ = 0
                         WHERE 
                             M003.ログインID = @ログインID
                           AND 
                             M004.社員ID = M003.社員ID 
                           AND 
                             M004.有効開始日 <= @有効日
                           AND 
                             M004.有効終了日 >= @有効日
                           AND 
                             M004.削除区分 = 0
                           AND 
                             M003.削除区分 = 0";

            var list権限 = DBServerProvider.SqlDapper.QueryList<object>(sql, new
            {
                ログインID = ログインID,
                有効日 = DateTime.Now.ToString("yyyy/MM/dd")
            });

            _ログイン情報 = list権限.Where(x => x.GetValue("事務所コード")?.ToString()
                                    + x.GetValue("営業所コード")?.ToString()
                                    + x.GetValue("部コード")?.ToString()
                                    + x.GetValue("課コード")?.ToString()
                                    + x.GetValue("係コード")?.ToString()
                                    == 所属部署コード)
                                    .Select(o => new LoginInfo
                                    {
                                        事務所コード = o.GetValue("事務所コード")?.ToString(),
                                        営業所コード = o.GetValue("営業所コード")?.ToString(),
                                        部署コード = o.GetValue("部コード")?.ToString(),
                                        課コード = o.GetValue("課コード")?.ToString(),
                                        係コード = o.GetValue("係コード")?.ToString(),
                                        役職コード = o.GetValue("役職コード")?.ToString(),
                                        部門名 = o.GetValue("部門名")?.ToString(),
                                        社員名 = o.GetValue("社員名")?.ToString()
                                    }).FirstOrDefault();

            return _ログイン情報 ?? new LoginInfo();
        }

        public void LogOut(string key)
        {
            CacheService.Remove(key);
        }

        #region ユーザー情報プロパティ
        public string ログインID
        {
            get
            {
                return (HttpContext.User.FindFirstValue(/*JwtRegisteredClaimNames.Jti*/"Jti")
                    ?? HttpContext.User.FindFirstValue(ClaimTypes.NameIdentifier))?.ToString();
            }
        }
        public string 社員ID
        {
            get { return UserInfo.社員ID; }
        }
        public string 社員名
        {
            get { return UserInfo.社員名; }
        }
        public List<MultiToken> Token
        {
            get { return UserInfo.Tokens; }
        }
        public string 所属部署コード
        {
            get
            {
                return HttpContext.User.FindFirstValue(ClaimTypes.Role);
            }
        }
        #endregion

        public string ClientHost
        {
            get
            {
                return RouteUtils.GetClientIP();
            }
        }
    }
}
