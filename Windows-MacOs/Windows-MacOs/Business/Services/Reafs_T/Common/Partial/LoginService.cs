using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using WinMacOs.ActionFilter;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.DataRepository.Utilities;
using WinMacOs.Models;
using WinMacOs.Models.Enums;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.Enums;
using WinMacOs.Utility.Extensions;
using WinMacOs.Utility.PInvoke.CommonModels;
using WinMacOs.Utility.PInvoke.Mpr;
using WinMacOs.Utility.TableModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Business.Services.Reafs_T
{
    public partial class LoginService
    {
        /// <summary>
        /// 担当者ログイン処理
        /// </summary>
        /// <param name="loginInfo"></param>
        /// <returns></returns>
        public async Task<WebResponseContent> Login(LoginInfo loginInfo)
        {
            string msg = string.Empty;
            WebResponseContent responseContent = new WebResponseContent();

            try
            {
                msgCode msgCode = msgCode.Null;
                if (!CheckInput(loginInfo,ref msgCode))
                {
                    if (msgCode == msgCode.E000001)
                    {
                        //ログイン処理の前にメッセージマスタを呼び出せない為、直接ＰＧに記載
                        msg = "ログインIDを入力して下さい。";
                        return responseContent.Error(ResAuthType.NotApplicable, msg);
                    }
                    else if (msgCode == msgCode.E000002)
                    {
                        //ログイン処理の前にメッセージマスタを呼び出せない為、直接ＰＧに記載
                        msg = "パスワードを入力して下さい。";
                        return responseContent.Error(ResAuthType.NotApplicable, msg);
                    }
                }

                //[社員ID]より[M003_社員マスタ]から社員情報を取得
                M015_業者ユーザマスタ user = await repository.M015_業者ユーザマスタ
                    .FindFirstAsync(x => x.ユーザーＩＤ == loginInfo.userName);
                //M015_業者ユーザマスタ user = await repository.M015_業者ユーザマスタ
                //    .FindFirstAsync(x => x.ユーザーＩＤ == loginInfo.userName,
                //                    user1 => new M015_業者ユーザマスタ()
                //                    {
                //                        会社コード = user1.会社コード,
                //                        業者コード = user1.業者コード,
                //                        業者コード枝番 = user1.業者コード枝番,
                //                        ユーザーＩＤ = user1.ユーザーＩＤ,
                //                        ユーザー種別 = user1.ユーザー種別,
                //                        ユーザー名 = user1.ユーザー名,
                //                        部署等 = user1.部署等,
                //                        メールアドレス = user1.メールアドレス,
                //                        利用期間開始 = user1.利用期間開始,
                //                        利用期間終了 = user1.利用期間終了,
                //                        パスワード変換 = user1.パスワード変換,
                //                        パスワード期限開始 = user1.パスワード期限開始,
                //                        パスワード期限終了 = user1.パスワード期限終了,
                //                        親ＩＤ = user1.親ＩＤ,
                //                        新規区分 = user1.新規区分,
                //                        削除区分 = user1.削除区分,
                //                        ソルト値 = user1.ソルト値,
                //                    });

                //if (ResponseMsg.ListResponseMsg is null)
                //{
                ResponseMsg.ListResponseMsg = await repository.S016_メッセージマスタ
                                                                .FindAsIQueryable(x => x.削除区分 != 1)
                                                                .ToDictionaryAsync(x => x.メッセージコード, x => x.メッセージ);
                //}
                if (user == null)
                    return responseContent.Error(ResAuthType.NotApplicable, msgCode.E000003);

                //社員パスワード認証
                if (PassWordHelper.GenerateHashPass(user.ソルト値, loginInfo.passWord.Trim()) != user.パスワード変換)
                    return responseContent.Error(ResAuthType.IdOrPassInvalid, msgCode.E000003);

                var today = DateTime.Now.ToString("yyyy/MM/dd");
                if (!(today.CompareTo(user.パスワード期限開始) >= 0 && today.CompareTo(user.パスワード期限終了) <= 0))
                {
                    //20221208 rep パスワードが失効していた場合のメッセージを追加
                    if(user.パスワード期限終了 == "0000/00/00")
                    {
                        return responseContent.Error(ResAuthType.PwLapse, msgCode.E040743);
                    }
                    else
                    {
                        return responseContent.Error(ResAuthType.PasswordExpired, msgCode.E040742);
                    }
                    //20221208 rep
                }

                if (user.削除区分 != 0)
                    return responseContent.Error(ResAuthType.IdDeleted, msgCode.E000004);

                if (!(today.CompareTo(user.利用期間開始) >= 0 && today.CompareTo(user.利用期間終了) <= 0))
                    return responseContent.Error(ResAuthType.IdExpired, msgCode.E040744);

                // GetM006に変更
                string str取引先名 = fnc_GetM006(user.会社コード, user.業者コード);                

                //社員tokenを作成
                string token = JwtHelper.IssueJwt(new UserInfo()
                {
                    ログインID = user.ユーザーＩＤ,
                    社員名 = user.ユーザー名,
                    // Tは所属部署コード不要
                    所属部署コード = "" // loginInfo.shozokuBushoCd,
                });

                responseContent.Data = new
                {
                    token,
                    会社コード = user.会社コード,
                    業者コード = user.業者コード,
                    業者コード枝番 = user.業者コード枝番,
                    ユーザーＩＤ = user.ユーザーＩＤ,
                    ユーザー種別 = user.ユーザー種別,
                    ユーザー名 = user.ユーザー名,
                    部署等 = user.部署等,
                    メールアドレス = user.メールアドレス,
                    親ＩＤ = user.親ＩＤ,
                    新規区分 = user.新規区分,
                    listMessage = ResponseMsg.ListResponseMsg,
                    取引先名 = str取引先名,
                    reafsWRTFlg = loginInfo.reafsWRTFlg
                };
                //
                List<F140_ログイン認証ファイル> f140a = repository.F140_ログイン認証ファイル
                    .Find(x => x.ログインID == user.ユーザーＩＤ);
                List<F140_ログイン認証ファイル> f140 = repository.F140_ログイン認証ファイル
                    .Find(x => x.ログインID == user.ユーザーＩＤ,
                                    order => new Dictionary<object, QueryOrderBy>()
                                    {
                                        { order.UPDATE_TIME, QueryOrderBy.Desc }
                                    });

                List<F140_ログイン認証ファイル> f140s = await repository.F140_ログイン認証ファイル
                    .FindAsync(x => x.ログインID == user.ユーザーＩＤ,
                                    order => new Dictionary<object, QueryOrderBy>()
                                    {
                                        { order.UPDATE_TIME, QueryOrderBy.Desc }
                                    });

                var paramCommon = GetParamCommon();
                if (f140s == null || f140s.Count < AppSetting.TokenCount)
                {
                    f140s.Add(new F140_ログイン認証ファイル
                    {
                        連番 = (int)(long)repository.DapperContext.ExecuteScalar("SELECT NEXT VALUE FOR SEQ_ログイン認証連番", null),
                        ログインID = user.ユーザーＩＤ,
                        Token = token,
                        INSERT_TIME = DateTime.Now.ToString("yyyy/MM/dd HH:mm:ss"),
                        INSERT_HOST = paramCommon.HOST,
                        INSERT_ID = user.ユーザーＩＤ,
                        INSERT_PG = loginInfo.reafsWRTFlg,
                        UPDATE_TIME = DateTime.Now.ToString("yyyy/MM/dd HH:mm:ss"),
                        UPDATE_HOST = paramCommon.HOST,
                        UPDATE_ID = user.ユーザーＩＤ,
                        UPDATE_PG = loginInfo.reafsWRTFlg,
                    });

                    await repository.F140_ログイン認証ファイル.AddAsync(f140s[f140s.Count - 1]);
                }
                else
                {
                    f140s[0].Token = token;
                    f140s[0].UPDATE_TIME = DateTime.Now.ToString("yyyy/MM/dd HH:mm:ss");
                    f140s[0].UPDATE_HOST = paramCommon.HOST;
                    f140s[0].UPDATE_ID = user.ユーザーＩＤ;
                    f140s[0].UPDATE_PG = loginInfo.reafsWRTFlg;

                    repository.F140_ログイン認証ファイル.Update(f140s[0]);
                }
                await repository.SaveChangesAsync();

                loginInfo.passWord = string.Empty;

                return responseContent.OK(ResAuthType.LoginSuccess);
            }
            catch (Exception ex)
            {
                msg = ex.Message + ex.StackTrace;
                Logger.Error(msg);
                return responseContent.Error(ResponseType.ServerError, msg);
            }
        }

        private bool CheckInput(LoginInfo loginInfo, ref msgCode msgCode)
        {
            bool bolOut = true;
            if (String.IsNullOrEmpty(loginInfo.userName))
            {
                msgCode = msgCode.E000001;
                bolOut = false;
                return bolOut;
            }

            if (String.IsNullOrEmpty(loginInfo.passWord))
            {
                msgCode = msgCode.E000002;
                bolOut = false;
                return bolOut;
            }

            return bolOut;
        }

        /// <summary>
        /// token期限切れが近づいている時，新しいtokenを再作成
        /// </summary>
        /// <returns></returns>
        public async Task<WebResponseContent> ReplaceToken()
        {
            WebResponseContent responseContent = new WebResponseContent();
            string error = "";
            UserInfo userInfo = null;
            try
            {
                string requestToken = HttpContext.Current.Request.Headers[AppSetting.TokenHeaderName];
                requestToken = requestToken?.Replace("Bearer ", "");

                //if (UserContext.Current.Token != requestToken) 
                //    return responseContent.Error("Tokenは無効になりました。");

                if (JwtHelper.IsExp(requestToken)) 
                    return responseContent.Error("Tokenは期限切れました。");

                string userId = UserContext.Current.ログインID;
                userInfo = await repository.M015_業者ユーザマスタ.FindFirstAsync(x => x.ユーザーＩＤ == userId,
                                                           user => new UserInfo()
                                                           {
                                                               ログインID = userId,
                                                               社員名 = user.ユーザー名,
                                                               所属部署コード = ""//ハノイ側修正2022/12/01　課題管理表№35：「2022/12/01依頼分」「タイムアウト設定時間リセット機能を有効にしてください。」
                                                           });

                if (userInfo == null) return 
                        responseContent.Error("社員情報は見つかりませんでした。");


                string token = JwtHelper.IssueJwt(userInfo);
                //キャッシュから削除する
                base.CacheContext.Remove(userId);

                //更新するtoken項目
                List<F140_ログイン認証ファイル> f140s = await repository.F140_ログイン認証ファイル
                                                                        .FindAsync(x => x.ログインID == userId,
                                                                                    f140s1 => f140s1,
                                                                                    order => new Dictionary<object, QueryOrderBy>()
                                                                                    {
                                                                                                        { order.UPDATE_TIME, QueryOrderBy.Desc }
                                                                                    });

                var paramCommon = GetParamCommon();
                f140s[0].Token = token;
                f140s[0].UPDATE_TIME = DateTime.Now.ToString("yyyy/MM/dd HH:mm:ss");
                f140s[0].UPDATE_HOST = paramCommon.HOST;
                f140s[0].UPDATE_ID = userId;
                f140s[0].UPDATE_PG = "LoginT";

                responseContent.OK(null, token);
            }
            catch (Exception ex)
            {
                error = ex.Message + ex.StackTrace + ex.Source;
                Logger.Error(error);
                responseContent.Error("Token更新処理はエラーが発生しました。");
            }

            return responseContent;
        }

        ///// <summary>
        ///// 担当者の部署を取得処理
        ///// </summary>
        ///// <param name="loginInfo"></param>
        ///// <returns></returns>
        //public WebResponseContent GetDepartment(string userName)
        //{
        //    string msg = string.Empty;
        //    WebResponseContent responseContent = new WebResponseContent();

        //    //if (String.IsNullOrEmpty(userName))
        //    //{
        //    //    return responseContent.OK(ResponseType.LoginError);
        //    //}
        //    //入力されたログインＩＤより「M004_兼務マスタ」を参照し、所属部署をプルダウンに設定する。
        //    try
        //    {
        //        string sql =
        //                    $"SELECT                                                                            " +
        //                    //$"      M004.役職コード," +
        //                    //$"      M004.役職名," +
        //                    //$"      M004.役職兼務区分," +
        //                    //$"      M004.社員ID," +
        //                    //$"      M004.事務所コード," +
        //                    //$"      M004.営業所コード," +
        //                    //$"      M004.部コード," +
        //                    //$"      M004.課コード," +
        //                    //$"      M004.係コード," +
        //                    //$"      M004.組織コード," +
        //                    //$"      M002.組織名,                                                               " +
        //                    $"      M004.事務所コード + M004.営業所コード + M004.部コード + M004.課コード + M004.係コード AS コード,       " +
        //                    $"      CASE WHEN M004.事業会社コード = '8000' THEN IsNull(M002.部門名, '') + ' ' + IsNull(M004.役職名, '') " +
        //                    $"          ELSE IsNull(M002.組織名, '') + ' ' + IsNull(M004.役職名, '') END コードおよび名称,               " +
        //                    $"      M002.部門名 AS  部門名                                                             " +

        //                    $"FROM                                                                              " +
        //                    $"    M003_社員マスタ M003,                                                         " +
        //                    $"    M004_兼務マスタ M004                                                          " +
        //                    $"LEFT OUTER JOIN                                                                   " +
        //                    $"    M002_組織マスタ M002                                                          " +
        //                    $"ON                                                                                " +
        //                    $"    M002.事務所コード = M004.事務所コード                                         " +
        //                    $"AND                                                                               " +
        //                    $"    M002.営業所コード = M004.営業所コード                                         " +
        //                    $"AND                                                                               " +
        //                    $"    M002.部コード = M004.部コード                                                 " +
        //                    $"AND                                                                               " +
        //                    $"    M002.課コード = M004.課コード                                                 " +
        //                    $"AND                                                                               " +
        //                    $"    M002.係コード = M004.係コード                                                 " +
        //                    $"AND                                                                               " +
        //                    $"    M002.削除フラグ = 0                                                           " +
        //                    $"WHERE                                                                             " +
        //                    $"    M003.ログインID = @ログインID                                                   " +
        //                    $"AND                                                                               " +
        //                    $"    M004.社員ID = M003.社員ID                                                     " +
        //                    $"AND                                                                               " +
        //                    $"    M004.有効開始日 <= convert(nvarchar(MAX), GETDATE(), 111)                     " +
        //                    $"AND                                                                               " +
        //                    $"    M004.有効終了日 >= convert(nvarchar(MAX), GETDATE(), 111)                     " +
        //                    $"AND                                                                               " +
        //                    $"    M004.削除区分 = 0                                                             " +
        //                    $"AND                                                                               " +
        //                    $"    M003.削除区分 = 0                                                             " +
        //                    $"ORDER BY                                                                          " +
        //                    $"    M004.役職兼務区分,M004.組織表示順, M004.役職表示順                            ";


        //        responseContent.Data = repository.DapperContext.QueryList<object>(sql, new UserInfo(){ ログインID = userName });

        //        return responseContent.OK(ResponseType.LoginSuccess);
        //    }
        //    catch (Exception ex)
        //    {
        //        msg = ex.Message + ex.StackTrace;
        //        return responseContent.Error(ResponseType.ServerError);
        //    }
        //    finally
        //    {
        //    }
        //}

        #region fnc_GetM006
        /// <summary>　
        /// 業者略名を取得
        /// </summary>
        /// <returns></returns>
        private String fnc_GetM006(string str会社コード, string str業者コード)
        {
            StringBuilder sql = new StringBuilder();

            //sql.AppendLine(" ");
            //sql.AppendLine(" SELECT TOP 1");
            //sql.AppendLine("        M005.本社情報取引先略名");
            //sql.AppendLine("   FROM M005_取引先マスタ M005 ");
            //sql.AppendLine("  WHERE M005.会社コード = @会社コード ");
            //sql.AppendLine("    AND M005.取引先コード = @業者コード ");
            ////sql.AppendLine("    AND M005.取引開始日 <= @today ");
            ////sql.AppendLine("    AND M005.取引停止日 >= @today ");
            //sql.AppendLine("    AND M005.停止区分 = '0' ");
            //sql.AppendLine("    AND M005.削除区分 = 0 ");
            //sql.AppendLine("    AND M005.支社コード IN ('9900','0010','3010','6010','7010','9010', '9945') ");
            //sql.AppendLine("    ORDER BY M005.取引先コード枝番 + M005.支社コード + M005.支社コード枝番 + M005.申請番号 ");

            // 取得方法をM006_業者マスタに変更
            sql.AppendLine(" SELECT TOP 1");
            sql.AppendLine("        M006.業者略名");
            sql.AppendLine("   FROM M015_業者ユーザマスタ M015");
            sql.AppendLine("   LEFT JOIN M006_業者マスタ M006");
            sql.AppendLine("     ON M015.会社コード = M006.会社コード");
            sql.AppendLine("    AND M015.業者コード = M006.業者コード");
            sql.AppendLine("   INNER JOIN");
            sql.AppendLine("     (SELECT 業者コード,MIN(業者コード枝番) AS 業者コード枝番");
            sql.AppendLine("	   FROM M006_業者マスタ");
            sql.AppendLine("	 GROUP BY 業者コード) M006_MIN");
            sql.AppendLine("	 ON M006.業者コード = M006_MIN.業者コード");
            sql.AppendLine("	AND M006.業者コード枝番 = M006_MIN.業者コード枝番");
            sql.AppendLine("  WHERE M015.会社コード = @会社コード ");
            sql.AppendLine("    AND M015.業者コード = @業者コード ");
            sql.AppendLine("    AND M006.削除区分 = 0");
            sql.AppendLine("    ORDER BY M006_MIN.業者コード + M006_MIN.業者コード枝番");


            object Cntable = repository.DapperContext.ExecuteScalar(sql.ToString(), new
            {
                会社コード = str会社コード,
                業者コード = str業者コード
                //today = DateTime.Now.ToString("yyyy/MM/dd")
            });

            return Cntable.ToString();
        }
        #endregion

        /// <summary>
        /// 戻り値が「-1」を5回繰り返した場合はパスワードを失効扱いにする
        /// </summary>
        /// <param name="data"></param>
        /// <returns></returns>
        public async Task<WebResponseContent> UpdM015(LoginInfo loginInfo)
        {

            string msg = string.Empty;
            WebResponseContent responseContent = new WebResponseContent();
            try
            {
                //[社員ID]より[M003_社員マスタ]から社員情報を取得
                M015_業者ユーザマスタ user = await repository.M015_業者ユーザマスタ
                    .FindFirstAsync(x => x.ユーザーＩＤ == loginInfo.userName,
                    user1 => new M015_業者ユーザマスタ()
                    {
                        会社コード = user1.会社コード

                    });

                var paramCommon = GetParamCommon();
                StringBuilder sql = new StringBuilder();
                int rst = 0;

                sql.AppendLine("UPDATE M015_業者ユーザマスタ ");
                sql.AppendLine("   SET  ");
                sql.AppendLine("       パスワード期限終了 = '0000/00/00', ");
                sql.AppendLine("       UPDATE_TIME = @UPDATE_TIME, ");
                sql.AppendLine("       UPDATE_PG = @UPDATE_PG, ");
                sql.AppendLine("       UPDATE_HOST = @UPDATE_HOST, ");
                sql.AppendLine("       UPDATE_ID = @UPDATE_ID ");
                sql.AppendLine(" WHERE 会社コード = @会社コード ");
                sql.AppendLine("   AND ユーザーＩＤ = @ユーザーＩＤ ");

                rst = repository.DapperContext.ExcuteNonQuery(sql.ToString(), new
                {
                    会社コード = user.会社コード,
                    ユーザーＩＤ = loginInfo.userName,
                    UPDATE_TIME = DateTime.Now.ToString("yyyy/MM/dd HH:mm:ss"),
                    UPDATE_PG = loginInfo.reafsWRTFlg,
                    UPDATE_HOST = paramCommon.HOST,
                    UPDATE_ID = loginInfo.userName
                });

                return responseContent.OK(ResponseType.Other, msgCode.E040743);

            }
            catch (Exception ex)
            {
                msg = ex.Message;
                Logger.Error(msg);
                return responseContent.Error(ResponseType.ServerError, msg);
            }
        }
    }
}
