using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Data;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.DataRepository.Utilities;
using WinMacOs.Models;
using WinMacOs.Models.Enums;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.Extensions;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Controllers.Reafs_T
{
    //[Authorize]
    public partial class CommonController
    {
        async Task<WebResponseContent> GetAllMenuActionList()
        {
            WebResponseContent responseContent = new WebResponseContent();

            string sql =
                        $"   SELECT                                          " +
                        $"          S002.メニューID     AS MenuId            " +
                        $"         ,S002.メニュー名     AS MenuName          " +
                        $"         ,S003.サブメニューID AS SubMenuId         " +
                        $"         ,S003.サブメニュー名 AS SubMenuName       " +
                        $"         ,S003.フォームID     AS Path              " +
                        $"     FROM ST002_メニューマスタ     S002            " +
                        $"LEFT JOIN ST003_サブメニューマスタ S003            " +
                        $"       ON S002.メニューID = S003.メニューID        " +
                        $"    WHERE S003.削除区分 = 0                        " +
                        $"    ORDER BY S003.表示順                         ";

            responseContent.Data = await repository.DapperContext.QueryListAsync<object>(sql, null);

            return responseContent;
        }

        public class NETRESOURCE
        {
            public int dwScope { get; set; }
            public int dwType { get; set; }
            public int dwDisplayType { get; set; }
            public int dwUsage { get; set; }
            public string lpLocalName { get; set; }
            public string lpRemoteName { get; set; }
            public string lpComment { get; set; }
            public string lpProvider { get; set; }
        }

        [DllImport("mpr.dll")]
        private static extern int WNetAddConnection2(NETRESOURCE lpNetResource, string lpPassword, string lpUsername, int dwFlags);

        public static int NetResourceConnect()
        {
            int ret = 0;
            string shareName = AppSetting.ShareName;
            string userId = AppSetting.Credentials.userId;
            string password = AppSetting.Credentials.password;

            if (!string.IsNullOrEmpty(shareName))
            {
                var netResource = new NETRESOURCE();
                netResource.dwScope = 0;
                netResource.dwType = 1;
                netResource.dwDisplayType = 0;
                netResource.dwUsage = 0;
                netResource.lpLocalName = "";
                netResource.lpRemoteName = shareName;
                netResource.lpComment = "";
                netResource.lpProvider = "";
                ret = WNetAddConnection2(netResource, password, userId, 0);
            }

            return ret;
        }

        struct 工事
        {
            public string 依頼No;
            public string 契約No;
            public string 契約履歴No;
            public string 年月明細No;
            public string 契約年月;
            public string 予定実施年月;
            public string 作業明細No;
            public string 枝番;
            public string 取引先;
            public string 検収回数;
            public string 請求書No;
            public string 契約書管理No;
            public string 支払日;
            public string 完了年月;
            public string 請求書管理No;
            public string 物件コード;
        }

        /// <summary>
        /// </summary>
        /// <returns></returns>
        async Task<WebResponseContent> GetMsg(string messageCode)
        {
            WebResponseContent responseContent = new WebResponseContent();

            string sql =
                        $"   SELECT メッセージ                               " +
                        $"     FROM S016_メッセージマスタ                    " +
                        $"    WHERE メッセージコード = @メッセージコード      " +
                        $"      AND 削除区分 = 0                             ";

            //responseContent.Data = repository.DapperContext.QueryList<object>(sql, new CommonInfo() { メッセージコード = messageCode });
            string sメッセージ = (string)await repository.DapperContext.ExecuteScalarAsync(sql, new CommonInfo() { メッセージコード = messageCode });

            responseContent.Data = sメッセージ;

            return responseContent;
        }

        /// <summary>
        /// 権限情報を取得
        /// </summary>
        /// <param name="menuId"></param>
        /// <param name="submenuId"></param>
        /// <returns></returns>
        async Task<WebResponseContent> GetControlActiveButton(string menuId, string submenuId)
        {
            string msg = string.Empty;
            WebResponseContent responseContent = new WebResponseContent();
            var userId = UserContext.Current.ログインID;
            //入力されたログインＩＤより「M004_兼務マスタ」を参照し、所属部署をプルダウンに設定する。
            try
            {
                string sql =
                $" SELECT		" +
                $" 	S004.コントロールID,	" +
                $" 	S006.権限区分 AS 利用可否	" +
                $" FROM		" +
                $" 	S007_個人権限ロールマスタ S007," +
                $" 	S006_機能別権限マスタ S006,	" +
                $" 	S004_機能マスタ S004	" +
                $" WHERE		" +
                $" 	S007.権限グループID = S006.権限グループID	" +
                $" AND	S006.メニューID = S004.メニューID	" +
                $" AND	S006.サブメニューID = S004.サブメニューID	" +
                $" AND	S006.機能ID = S004.機能ID	" +
                $" AND S007.削除区分 = 0	" +
                $" AND S006.削除区分 = 0	" +
                $" AND S006.データ区分 = 2	" +
                $" AND S007.社員ID = @社員ID" +
                $" AND S007.権限終了日 >= @権限終了日" +
                $" AND S006.メニューID = @メニューID" +
                $" AND S006.サブメニューID = @サブメニューID";

                var dt権限 = await repository.DapperContext.QueryListAsync<object>(sql, new { 社員ID = userId, 権限終了日 = DateTime.Now.ToString("yyyy/MM/dd"), メニューID = menuId, サブメニューID = submenuId });
                if (dt権限.Count() == 0)
                {
                    //S007に登録されない場合、S006の汎用を参照
                    //S006にも登録されない場合、利用不可とする
                    sql = null;
                    sql =
                    @"SELECT	
                    	    S004.コントロールID,	
                    	    CASE  
                    	      WHEN S006.メニューID IS NULL THEN 0 
                    	      ELSE 1 
                           END AS 利用可否	
                    	FROM	
                    	    S004_機能マスタ S004 LEFT JOIN	
                    		S006_機能別権限マスタ S006 
                    	ON	
                    	    S006.データ区分 = 1	
                    	AND	S006.メニューID = S004.メニューID	
                    	AND	S006.サブメニューID = S004.サブメニューID	
                    	AND	S006.機能ID = S004.機能ID	
                    	AND S006.削除区分 = 0	
                    	AND (	
                    	     (	
                            S006.権限部署_役職_01 <= @役職コード
                               AND @営業所コード BETWEEN REPLACE(S006.権限部署_営業所_01, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_営業所_01, 'X', '9')	
                               AND @部コード BETWEEN REPLACE(S006.権限部署_部_01, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_部_01, 'X', '9')	
                               AND @課コード BETWEEN REPLACE(S006.権限部署_課_01, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_課_01, 'X', '9')	
                               AND @係コード BETWEEN REPLACE(S006.権限部署_係_01, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_係_01, 'X', '9')	
                    	        )	
                    	        OR  (	
                                S006.権限部署_役職_02 <= @役職コード
                               AND @営業所コード BETWEEN REPLACE(S006.権限部署_営業所_02, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_営業所_02, 'X', '9')	
                               AND @部コード BETWEEN REPLACE(S006.権限部署_部_02, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_部_02, 'X', '9')	
                               AND @課コード BETWEEN REPLACE(S006.権限部署_課_02, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_課_02, 'X', '9')	
                               AND @係コード BETWEEN REPLACE(S006.権限部署_係_02, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_係_02, 'X', '9')	
                    	        )	
                    	    OR  (	
                                S006.権限部署_役職_03 <= @役職コード
                               AND @営業所コード BETWEEN REPLACE(S006.権限部署_営業所_03, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_営業所_03, 'X', '9')	
                               AND @部コード BETWEEN REPLACE(S006.権限部署_部_03, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_部_03, 'X', '9')	
                               AND @課コード BETWEEN REPLACE(S006.権限部署_課_03, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_課_03, 'X', '9')	
                               AND @係コード BETWEEN REPLACE(S006.権限部署_係_03, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_係_03, 'X', '9')	
                    	        )	
                    	    OR  (	
                                S006.権限部署_役職_04 <= @役職コード
                               AND @営業所コード BETWEEN REPLACE(S006.権限部署_営業所_04, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_営業所_04, 'X', '9')	
                               AND @部コード BETWEEN REPLACE(S006.権限部署_部_04, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_部_04, 'X', '9')	
                               AND @課コード BETWEEN REPLACE(S006.権限部署_課_04, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_課_04, 'X', '9')	
                               AND @係コード BETWEEN REPLACE(S006.権限部署_係_04, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_係_04, 'X', '9')	
                    	        )	
                    	    OR  (	
                                S006.権限部署_役職_05 <= @役職コード
                               AND @営業所コード BETWEEN REPLACE(S006.権限部署_営業所_05, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_営業所_05, 'X', '9')	
                               AND @部コード BETWEEN REPLACE(S006.権限部署_部_05, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_部_05, 'X', '9')	
                               AND @課コード BETWEEN REPLACE(S006.権限部署_課_05, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_課_05, 'X', '9')	
                               AND @係コード BETWEEN REPLACE(S006.権限部署_係_05, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_係_05, 'X', '9')	
                    	        )	
                    	    OR  (	
                                S006.権限部署_役職_06 <= @役職コード
                               AND @営業所コード BETWEEN REPLACE(S006.権限部署_営業所_06, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_営業所_06, 'X', '9')	
                               AND @部コード BETWEEN REPLACE(S006.権限部署_部_06, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_部_06, 'X', '9')	
                               AND @課コード BETWEEN REPLACE(S006.権限部署_課_06, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_課_06, 'X', '9')	
                               AND @係コード BETWEEN REPLACE(S006.権限部署_係_06, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_係_06, 'X', '9')	
                    	        )	
                    	    OR  (	
                                S006.権限部署_役職_07 <= @役職コード
                               AND @営業所コード BETWEEN REPLACE(S006.権限部署_営業所_07, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_営業所_07, 'X', '9')	
                               AND @部コード BETWEEN REPLACE(S006.権限部署_部_07, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_部_07, 'X', '9')	
                               AND @課コード BETWEEN REPLACE(S006.権限部署_課_07, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_課_07, 'X', '9')	
                               AND @係コード BETWEEN REPLACE(S006.権限部署_係_07, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_係_07, 'X', '9')	
                    	        )	
                    	    OR  (	
                                S006.権限部署_役職_08 <= @役職コード
                               AND @営業所コード BETWEEN REPLACE(S006.権限部署_営業所_08, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_営業所_08, 'X', '9')	
                               AND @部コード BETWEEN REPLACE(S006.権限部署_部_08, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_部_08, 'X', '9')	
                               AND @課コード BETWEEN REPLACE(S006.権限部署_課_08, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_課_08, 'X', '9')	
                               AND @係コード BETWEEN REPLACE(S006.権限部署_係_08, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_係_08, 'X', '9')	
                    	        )	
                    	    OR  (	
                                S006.権限部署_役職_09 <= @役職コード
                               AND @営業所コード BETWEEN REPLACE(S006.権限部署_営業所_09, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_営業所_09, 'X', '9')	
                               AND @部コード BETWEEN REPLACE(S006.権限部署_部_09, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_部_09, 'X', '9')	
                               AND @課コード BETWEEN REPLACE(S006.権限部署_課_09, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_課_09, 'X', '9')	
                               AND @係コード BETWEEN REPLACE(S006.権限部署_係_09, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_係_09, 'X', '9')	
                    	        )	
                    	    OR  (	
                               S006.権限部署_役職_10 <= @役職コード
                               AND @営業所コード BETWEEN REPLACE(S006.権限部署_営業所_10, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_営業所_10, 'X', '9')	
                               AND @部コード BETWEEN REPLACE(S006.権限部署_部_10, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_部_10, 'X', '9')	
                               AND @課コード BETWEEN REPLACE(S006.権限部署_課_10, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_課_10, 'X', '9')	
                               AND @係コード BETWEEN REPLACE(S006.権限部署_係_10, 'X', '0')	
                    	        AND REPLACE(S006.権限部署_係_10, 'X', '9')	
                    	        )	
                    	    )	
                    	WHERE 
                       S004.メニューID = @メニューID
                       AND S004.サブメニューID = @サブメニューID";
                    dt権限 = await repository.DapperContext.QueryListAsync<object>(sql,
                        new
                        {
                            営業所コード = UserContext.Current.ログイン情報.営業所コード,
                            部コード = UserContext.Current.ログイン情報.部署コード,
                            課コード = UserContext.Current.ログイン情報.課コード,
                            係コード = UserContext.Current.ログイン情報.係コード,
                            役職コード = UserContext.Current.ログイン情報.役職コード,
                            メニューID = menuId,
                            サブメニューID = submenuId
                        });
                }
                responseContent.Data = dt権限;
                return responseContent.OK();
            }
            catch (Exception ex)
            {
                msg = ex.Message + ex.StackTrace;
                return responseContent.Error(ResponseType.ServerError);
            }
            finally
            {
            }
        }

        /// <summary>
        /// Download File
        /// </summary>
        /// <param name="path"></param>
        /// <returns></returns>
        async Task<byte[]> DownloadFile(string path)
        {
            try
            {
                byte[] imageArray = new byte[] { };
                if (!string.IsNullOrEmpty(path))
                {
                    Reafs_W.CommonService.NetResourceConnect();
                    if (System.IO.File.Exists(path))
                    {
                        imageArray = await FileExtensions.ReadAllBytesAsync(path);
                    }
                }

                return imageArray;
            }
            catch (Exception ex)
            {
                repository.Dispose();
                return null;
            }
        }

        async Task<WebResponseContent> GetCurrentMenuActionList(string ユーザー種別)
        {
            WebResponseContent responseContent = new WebResponseContent();

            //string sql =
            //            $"   SELECT                                          " +
            //            $"          S002.メニューID     AS MenuId            " +
            //            $"         ,S002.メニュー名     AS MenuName          " +
            //            $"         ,S003.サブメニューID AS SubMenuId         " +
            //            $"         ,S003.サブメニュー名 AS SubMenuName       " +
            //            $"         ,S003.フォームID     AS Path              " +
            //            $"     FROM ST002_メニューマスタ     S002            " +
            //            $"LEFT JOIN ST003_サブメニューマスタ S003            " +
            //            $"       ON S002.メニューID = S003.メニューID        " +
            //            $"    WHERE S003.削除区分 = 0                        " +
            //            $"      AND S003.表示フラグ = 1                      " +
            //            $"      ORDER BY S003.表示順                         " ;
            StringBuilder sql = new StringBuilder();
            sql.AppendLine("   SELECT ");
            sql.AppendLine("          S002.メニューID AS MenuId ");
            sql.AppendLine("         ,S002.メニュー名 AS MenuName ");
            sql.AppendLine("         ,S003.サブメニューID AS SubMenuId ");
            sql.AppendLine("         ,S003.サブメニュー名 AS SubMenuName ");
            sql.AppendLine("         ,S003.フォームID AS Path ");
            sql.AppendLine("     FROM ST002_メニューマスタ S002 ");
            sql.AppendLine("   LEFT JOIN ST003_サブメニューマスタ S003 ");
            sql.AppendLine("       ON S002.メニューID = S003.メニューID ");
            sql.AppendLine("    WHERE S003.削除区分 = 0 ");
            sql.AppendLine("      AND S003.表示フラグ = 1 ");
            if (ユーザー種別 == "1")
            {
                sql.AppendLine("      AND S003.親ユーザー表示制限フラグ = 0 ");
            }
            else if (ユーザー種別 == "2")
            {
                sql.AppendLine("      AND S003.子ユーザー表示制限フラグ = 0 ");
            }
            sql.AppendLine("      ORDER BY S003.表示順 ");


            responseContent.Data = await repository.DapperContext.QueryListAsync<object>(sql.ToString(), null);

            return responseContent;
        }

        async Task<WebResponseContent> GetMenuNameList()
        {
            WebResponseContent responseContent = new WebResponseContent();

            string sql =
                        $"   SELECT                                          " +
                        $"          S002.メニューID     AS MenuId            " +
                        $"         ,S002.メニュー名     AS MenuName          " +
                        $"         ,S003.サブメニューID AS SubMenuId         " +
                        $"         ,S003.サブメニュー名 AS SubMenuName       " +
                        $"         ,S003.フォームID     AS Path              " +
                        $"     FROM ST002_メニューマスタ     S002            " +
                        $"LEFT JOIN ST003_サブメニューマスタ S003            " +
                        $"       ON S002.メニューID = S003.メニューID        " +
                        $"    WHERE S003.削除区分 = 0                        ";

            responseContent.Data = await repository.DapperContext.QueryListAsync<object>(sql, null);

            return responseContent;
        }


        private F090_ドキュメント管理ファイル setDefaultCommonF090(F090_ドキュメント管理ファイル _f090)
        {
            F090_ドキュメント管理ファイル f090 = _f090;
            f090.工事依頼No = !string.IsNullOrEmpty(f090.工事依頼No) ? f090.工事依頼No : "";
            f090.添付元 = !string.IsNullOrEmpty(f090.添付元) ? f090.添付元 : "";
            f090.添付種類 = !string.IsNullOrEmpty(f090.添付種類) ? f090.添付種類 : "";
            f090.その他帳票名 = !string.IsNullOrEmpty(f090.その他帳票名) ? f090.その他帳票名 : "";
            f090.ファイルパス = !string.IsNullOrEmpty(f090.ファイルパス) ? f090.ファイルパス : "";
            f090.物理ファイル名 = !string.IsNullOrEmpty(f090.物理ファイル名) ? f090.物理ファイル名 : "";
            f090.添付元ファイル名 = !string.IsNullOrEmpty(f090.添付元ファイル名) ? f090.添付元ファイル名 : "";
            f090.契約NO = !string.IsNullOrEmpty(f090.契約NO) ? f090.契約NO : "";
            f090.履歴NO = f090.履歴NO != null ? f090.履歴NO : 0;
            f090.明細NO = f090.明細NO != null ? f090.明細NO : 0;
            f090.契約年月 = !string.IsNullOrEmpty(f090.契約年月) ? f090.契約年月 : "";
            f090.契約年月明細NO = f090.契約年月明細NO != null ? f090.契約年月明細NO : 0;
            f090.予定年月 = !string.IsNullOrEmpty(f090.予定年月) ? f090.予定年月 : "";
            f090.契約書管理NO = !string.IsNullOrEmpty(f090.契約書管理NO) ? f090.契約書管理NO : "";
            f090.請求書管理NO = !string.IsNullOrEmpty(f090.請求書管理NO) ? f090.請求書管理NO : "";
            f090.枝番 = !string.IsNullOrEmpty(f090.枝番) ? f090.枝番 : "";
            f090.取引先コード = !string.IsNullOrEmpty(f090.取引先コード) ? f090.取引先コード : "";
            f090.取引先コード枝番 = !string.IsNullOrEmpty(f090.取引先コード枝番) ? f090.取引先コード枝番 : "";
            f090.検収回数 = f090.検収回数 != null ? f090.検収回数 : 0;
            f090.請求書NO = !string.IsNullOrEmpty(f090.請求書NO) ? f090.請求書NO : "";
            f090.日付 = !string.IsNullOrEmpty(f090.日付) ? f090.日付 : "";
            f090.備考1 = !string.IsNullOrEmpty(f090.備考1) ? f090.備考1 : "";
            f090.備考2 = !string.IsNullOrEmpty(f090.備考2) ? f090.備考2 : "";
            f090.Ｗ参照区分 = f090.Ｗ参照区分 != null ? f090.Ｗ参照区分 : 0;
            f090.Ｔ参照区分 = f090.Ｔ参照区分 != null ? f090.Ｔ参照区分 : 0;
            f090.発注稟議添付 = f090.発注稟議添付 != null ? f090.発注稟議添付 : 0;
            f090.稟議申請ファイル名 = !string.IsNullOrEmpty(f090.稟議申請ファイル名) ? f090.稟議申請ファイル名 : "";
            f090.INSERT_PG = !string.IsNullOrEmpty(f090.INSERT_PG) ? f090.INSERT_PG : "";
            f090.INSERT_HOST = !string.IsNullOrEmpty(f090.INSERT_HOST) ? f090.INSERT_HOST : "";
            f090.INSERT_ID = !string.IsNullOrEmpty(f090.INSERT_ID) ? f090.INSERT_ID : "";
            f090.物件コード = !string.IsNullOrEmpty(f090.物件コード) ? f090.物件コード : "";

            return f090;
        }

        async Task<WebResponseContent> fnc_InsertF090(JArray data)
        {
            string errStr = String.Empty;
            WebResponseContent responseContent = new WebResponseContent();
            try
            {
                List<object> lstData = data.ToObject<List<object>>();
                foreach (JObject item in lstData)
                {
                    F090_ドキュメント管理ファイル _f090 = item["F090_ドキュメント管理ファイル"].ToObject<F090_ドキュメント管理ファイル>();
                    F090_ドキュメント管理ファイル f090 = setDefaultCommonF090(_f090);
                    string 添付NO = item["添付NO"].ToObject<string>();
                    string 帳票種類 = item["帳票種類"].ToObject<string>();
                    string 業者コード = item["業者コード"].ToObject<string>();
                    string 業者コード枝番 = item["業者コード枝番"].ToObject<string>();
                    string 工事依頼No = item["工事依頼No"]?.ToObject<string>();
                    string 枝番 = item["枝番"]?.ToObject<string>();
                    string 契約No = item["契約No"]?.ToObject<string>();
                    string 契約履歴No = item["契約履歴No"]?.ToObject<string>();
                    string 契約年月 = item["契約年月"]?.ToObject<string>();
                    string 見積明細No = item["見積明細No"]?.ToObject<string>();
                    string 予定年月 = item["予定年月"]?.ToObject<string>();
                    string 契約年月明細No = item["契約年月明細No"]?.ToObject<string>();
                    var paramCommon = GetParamCommon();

                    int i契約履歴No = !string.IsNullOrEmpty(契約履歴No) ? int.Parse(契約履歴No) : 0;
                    int i見積明細No = !string.IsNullOrEmpty(見積明細No) ? int.Parse(見積明細No) : 0;
                    int i契約年月明細No = !string.IsNullOrEmpty(契約年月明細No) ? int.Parse(契約年月明細No) : 0;

                    switch (帳票種類)
                    {
                        case "102":
                            f090.工事依頼No = 工事依頼No;
                            f090.枝番 = 枝番;
                            break;
                        case "109":
                            f090.工事依頼No = 工事依頼No;
                            f090.枝番 = 枝番;
                            break;
                        case "110":
                            f090.工事依頼No = 工事依頼No;
                            f090.枝番 = 枝番;
                            break;
                        case "120":
                            f090.工事依頼No = 工事依頼No;
                            f090.枝番 = 枝番;
                            f090.検収回数 = 0;
                            break;
                        case "199":
                            f090.工事依頼No = 工事依頼No;
                            break;
                        case "202":
                            f090.契約NO = 契約No;
                            f090.履歴NO = i契約履歴No;
                            f090.契約年月 = 契約年月;
                            f090.契約年月明細NO = 0;
                            f090.枝番 = 枝番;
                            break;
                        case "221":
                            f090.契約NO = 契約No;
                            f090.履歴NO = i契約履歴No;
                            f090.明細NO = i見積明細No;
                            f090.契約年月 = 予定年月;
                            f090.契約年月明細NO = i契約年月明細No;
                            f090.予定年月 = 契約年月;
                            break;
                        case "222":
                            f090.契約NO = 契約No;
                            f090.履歴NO = i契約履歴No;
                            f090.明細NO = i見積明細No;
                            f090.契約年月 = 予定年月;
                            f090.契約年月明細NO = i契約年月明細No;
                            f090.予定年月 = 契約年月;
                            break;
                        case "223":
                            f090.契約NO = 契約No;
                            f090.履歴NO = i契約履歴No;
                            f090.明細NO = i見積明細No;
                            f090.契約年月 = 予定年月;
                            f090.契約年月明細NO = i契約年月明細No;
                            f090.予定年月 = 契約年月;
                            break;
                        case "226":
                            f090.契約NO = 契約No;
                            f090.履歴NO = i契約履歴No;
                            f090.明細NO = i見積明細No;
                            f090.契約年月 = 契約年月;
                            f090.契約年月明細NO = 99;
                            f090.予定年月 = 予定年月;
                            break;
                        case "299":
                            f090.契約NO = 契約No;
                            f090.履歴NO = i契約履歴No;
                            break;
                    }

                    f090.添付元 = "T";
                    f090.取引先コード = 業者コード;
                    f090.取引先コード枝番 = 業者コード枝番;
                    f090.日付 = DateTime.Now.ToString("yyyy/MM/dd");
                    f090.備考1 = "登録日：" + DateTime.Now.ToString("yyyy/MM/dd");
                    f090.INSERT_HOST = paramCommon.HOST;
                    f090.INSERT_ID = UserContext.Current.ログインID;
                    //20221118 rep 図面添付登録処理はReafs-Rと同じように修正。
                    //    if (string.IsNullOrEmpty(f090.物理ファイル名))
                    //    {
                    //        string ext = getExt(f090.添付元ファイル名);
                    //        工事 koji = new 工事();
                    //        koji.依頼No = f090.工事依頼No;
                    //        koji.契約No = f090.契約NO;
                    //        koji.契約履歴No = f090.履歴NO.ToString();
                    //        koji.年月明細No = f090.契約年月明細NO.ToString();
                    //        koji.契約年月 = f090.契約年月;
                    //        koji.予定実施年月 = f090.予定年月;
                    //        koji.作業明細No = f090.明細NO.ToString();
                    //        koji.枝番 = f090.枝番;
                    //        koji.取引先 = f090.取引先コード + f090.取引先コード枝番;
                    //        koji.検収回数 = f090.検収回数.ToString();
                    //        koji.請求書No = f090.請求書NO;
                    //        koji.契約書管理No = f090.契約書管理NO;
                    //        koji.支払日 = "";
                    //        koji.完了年月 = "";
                    //        koji.請求書管理No = f090.請求書管理NO;

                    //        f090.物理ファイル名 = getPhysicalFileName(f090.添付種類, ext, koji);
                    //    }

                    //    if (!string.IsNullOrEmpty(添付NO))
                    //    {
                    //        string folder = "";
                    //        if(f090.添付種類 == "101" || f090.添付種類 == "102" || f090.添付種類 == "103" || f090.添付種類 == "109" || f090.添付種類 == "110" || f090.添付種類 == "120" || f090.添付種類 == "199")
                    //        {
                    //            folder = f090.工事依頼No;
                    //        }

                    //        if (f090.添付種類 == "201" || f090.添付種類 == "202" || f090.添付種類 == "203" || f090.添付種類 == "217" || f090.添付種類 == "218" || f090.添付種類 == "221" ||
                    //            f090.添付種類 == "222" || f090.添付種類 == "223" || f090.添付種類 == "226" || f090.添付種類 == "299")
                    //        {
                    //            folder = f090.契約NO;
                    //        }

                    //        f090.ファイルパス = @"\" + folder + @"\" + f090.物理ファイル名;

                    //        string ルートパス = await getS018_ルートパス(f090.添付種類);

                    //        await fnc_SaveFileServer(添付NO, f090, ルートパス, folder);
                    //    }

                    //    Int64 iSeq = getSeqドキュメントNO();
                    //    f090.ドキュメントNO = iSeq;
                    //    F090_ドキュメント管理ファイル f090Res = await Fnc_insF090Entity(f090);

                    //    if (!string.IsNullOrEmpty(添付NO))
                    //    {
                    //        msgCode msgCd = await Fnc_delF093Entity(添付NO);
                    //        if (msgCd != msgCode.Null)
                    //        {
                    //            return responseContent.Error(msgCd);
                    //        }
                    //    }
                    var ドキュメントNO = getSeqドキュメントNO();
                    f090.ドキュメントNO = ドキュメントNO;
                    errStr = await fnc_insertF090(f090);

                    string SAVE_FILE_NAME = f090.物理ファイル名;
                    string ルートパス = await getS018_ルートパス(f090.添付種類);
                    string SAVE_DIR = ルートパス + f090.ファイルパス;
                    SAVE_DIR = new FileInfo(SAVE_DIR).DirectoryName;
                    await fnc_SaveFileServer(SAVE_DIR, SAVE_FILE_NAME, int.Parse(添付NO));
                }
                //20221118 rep
                if (errStr != string.Empty)
                {
                    return responseContent.Error(errStr);
                }
                await repository.SaveChangesAsync();

                return responseContent.OK(ResponseType.SaveSuccess);
            }
            catch (Exception ex)
            {
                repository.Dispose();
                errStr = ex.Message + ex.StackTrace;
                return responseContent.Error(ResponseType.ServerError, errStr);
            }
            finally
            {
            }
        }

        private string getExt(string 添付元ファイル名)
        {
            string ext = "";
            if (添付元ファイル名.LastIndexOf(".") >= 0 && 添付元ファイル名.LastIndexOf(".") < 添付元ファイル名.Length - 1)
            {
                ext = 添付元ファイル名.Substring(添付元ファイル名.LastIndexOf(".") + 1);
            }
            return ext;
        }

        private string getPhysicalFileName(string 帳票コード, string 拡張子, 工事 Koji)
        {
            string 物理ファイル名 = string.Empty;
            DataTable dt保存時ファイル名 = get保存時ファイル名();
            物理ファイル名 = dt保存時ファイル名.Select("帳票コード='" + 帳票コード + "'")[0]["物理ファイル名"].ToString();
            if (!Koji.Equals(null))
            {
                物理ファイル名 = 物理ファイル名.Replace("{依頼№}", Koji.依頼No);
                物理ファイル名 = 物理ファイル名.Replace("{枝番}", Koji.枝番);
                物理ファイル名 = 物理ファイル名.Replace("{取引先}", (Koji.取引先));
                物理ファイル名 = 物理ファイル名.Replace("{完了年月}", Koji.完了年月);
                物理ファイル名 = 物理ファイル名.Replace("{検収回数:00}", Koji.検収回数.PadLeft(2, '0'));

                物理ファイル名 = 物理ファイル名.Replace("{請求書№}", Koji.請求書No);
                物理ファイル名 = 物理ファイル名.Replace("{契約№}", Koji.契約No);
                物理ファイル名 = 物理ファイル名.Replace("{契約履歴№}", Koji.契約履歴No);
                物理ファイル名 = 物理ファイル名.Replace("{契約年月}", Koji.契約年月);
                物理ファイル名 = 物理ファイル名.Replace("{契約書管理№}", Koji.契約書管理No);
                物理ファイル名 = 物理ファイル名.Replace("{予定実施年月}", Koji.予定実施年月);
                物理ファイル名 = 物理ファイル名.Replace("{作業明細№}", Koji.作業明細No);
                物理ファイル名 = 物理ファイル名.Replace("{年月明細№}", Koji.年月明細No);
            }

            物理ファイル名 = 物理ファイル名.Replace("yyyyMMddHHmmss", DateTime.Now.ToString("yyyyMMddHHmmss"));
            物理ファイル名 = 物理ファイル名.Replace("yyyymmddHHmmss", DateTime.Now.ToString("yyyyMMddHHmmss"));

            if (!string.IsNullOrEmpty(拡張子))
            {
                物理ファイル名 = 物理ファイル名 + (拡張子.Substring(0, 1) == "." ? "" : ".") + 拡張子;
            }

            if (!物理ファイル名.Equals(null))
            {
                物理ファイル名 = RemoveInvalidCharacter(物理ファイル名);
            }
            return 物理ファイル名;
        }

        private DataTable get保存時ファイル名()
        {
            string[,] arrCol = {
                { "101", "依頼書_{依頼№}_yyyyMMddHHmmss"},
                { "102", "見積書_{依頼№}{枝番}_{取引先}_yyyyMMddHHmmss"},
                { "103", "見積書_{依頼№}{枝番}_{取引先}_yyyyMMddHHmmss"},
                { "109", "発注書_{依頼№}{枝番}_{取引先}_yyyyMMddHHmmss"},
                { "110", "請書_{依頼№}{枝番}_{取引先}_yyyyMMddHHmmss"},
                { "120", "完了報告書_{依頼№}{枝番}_{取引先}_{完了年月}{検収回数:00}_yyyyMMddHHmmss"},
                { "130", "請求書_{依頼№}{枝番}_{取引先}_{検収回数:00}_yyyyMMddHHmmss"},
                { "199", "その他_{依頼№}_yyyyMMddHHmmss"},
                { "201", "依頼書_{契約№}_yyyyMMddHHmmss"},
                { "202", "見積書_{契約№}{契約履歴№}{枝番}_{取引先}_{契約年月}_yyyyMMddHHmmss"},
                { "203", "見積書_{契約№}{契約履歴№}{枝番}_{取引先}_{契約年月}_yyyyMMddHHmmss"},
                { "217", "委託契約書仮_{契約書管理№}_yyyyMMddHHmmss"},
                { "218", "委託契約書_{契約書管理№}_yyyyMMddHHmmss"},
                { "221", "完了添付_{契約№}{契約履歴№}_{取引先}_{予定実施年月}{作業明細№}{年月明細№}_yyyyMMddHHmmss"},
                { "222", "法定添付_{契約№}{契約履歴№}_{取引先}_{予定実施年月}{作業明細№}{年月明細№}_yyyyMMddHHmmss"},
                { "223", "実費精算_{契約№}{契約履歴№}_{取引先}_{予定実施年月}_yyyyMMddHHmmss"},
                { "226", "法定添付_{契約№}{契約履歴№}_{取引先}_{予定実施年月}{作業明細№}99_yyyyMMddHHmmss"},
                { "230", "請求書_{請求書管理番号}_yyyyMMddHHmmss"},
                { "299", "その他_{契約№}_yyyyMMddHHmmss"}
            };

            DataTable dt = new DataTable();
            dt.Columns.Add("帳票コード");
            dt.Columns.Add("物理ファイル名");

            for (int i = 0; i < arrCol.GetLength(0); i++)
            {
                string col帳票コード = arrCol[i, 0].ToString();
                string col物理ファイル名 = arrCol[i, 1].ToString();

                dt.Rows.Add(dt.NewRow());
                dt.Rows[i]["帳票コード"] = col帳票コード;
                dt.Rows[i]["物理ファイル名"] = col物理ファイル名;
            }
            return dt;
        }

        private string RemoveInvalidCharacter(string str)
        {
            char[] invalidFileChars = System.IO.Path.GetInvalidFileNameChars();
            if (!string.IsNullOrEmpty(str))
            {
                foreach (var invalidFileChar in invalidFileChars)
                {
                    str = str.Replace(invalidFileChar.ToString(), "");
                }
            }

            return str;
        }

        private Int64 getSeqドキュメントNO()
        {
            string sql = getSeqドキュメントNOSQL();
            Int64 seqNo = (Int64)repository.DapperContext.ExecuteScalar(sql, null);
            return seqNo;
        }

        private string getSeqドキュメントNOSQL()
        {
            StringBuilder sql = new StringBuilder();

            sql.AppendLine(" SELECT NEXT VALUE FOR SEQ_ドキュメントNO  ");

            return sql.ToString();
        }

        private async Task<F090_ドキュメント管理ファイル> Fnc_insF090Entity(F090_ドキュメント管理ファイル f090)
        {
            await repository.F090_ドキュメント管理ファイル.AddAsync(f090);
            return f090;
        }

        //20221118 rep 図面添付登録処理はReafs-Rと同じように修正。
        private async Task<string> Fnc_delF093Entity(int 添付NO)
        {
            string errMsg = "";
            //int i添付NO = int.Parse(添付NO);
            F093_一時添付ファイル f093Data = await repository.F093_一時添付ファイル.FindFirstAsync(x => x.添付NO == 添付NO && x.枝番 == 1);
            if (f093Data != null)
            {
                repository.F093_一時添付ファイル.Delete(f093Data);
            }
            else
            {
                errMsg = "更新に失敗しました";
            }
            return errMsg;
        }
        //20221118 rep

        private async Task<string> getS018_ルートパス(string 添付種類)
        {
            S018_ドキュメント定義 s018Db = await repository.S018_ドキュメント定義.FindFirstAsync(x => x.帳票種類 == 添付種類);
            return s018Db.ルートパス;
        }

        //20221118 rep 図面添付登録処理はReafs-Rと同じように修正。
        private async Task<bool> fnc_SaveFileServer(string SAVE_DIR, string SAVE_FILE_NAME, int 添付NO)
        {
            byte[] bファイルデータ = new byte[] { };
            //if (!string.IsNullOrEmpty(添付NO))
            //{
            //    int i添付NO = Int32.Parse(添付NO);
            //    bファイルデータ = await getFile(i添付NO);
            //    string path = string.Empty;
            //    path = Path.Combine(ルートパス, folder);

            //    saveFile(path, f090.物理ファイル名, bファイルデータ);
            //}
            //return true;
            bファイルデータ = await getFile(添付NO);
            bool res = saveFile(SAVE_DIR, SAVE_FILE_NAME, bファイルデータ);
            string msgCd = await Fnc_delF093Entity(添付NO);

            return res && String.IsNullOrEmpty(msgCd);
        }
        //20221118 rep

        private async Task<byte[]> getFile(int 添付NO)
        {
            F093_一時添付ファイル f093Data = await repository.F093_一時添付ファイル.FindFirstAsync(x => x.添付NO == 添付NO && x.枝番 == 1);
            byte[] ファイルデータ = new byte[] { };
            if (f093Data != null)
            {
                ファイルデータ = f093Data.ファイルデータ;
            }
            return ファイルデータ;
        }

        private bool saveFile(string SAVE_DIR, string SAVE_FILE_NAME, byte[] FILE_DATA)
        {
            NetResourceConnect();
            if (!Directory.Exists(SAVE_DIR))
            {
                Directory.CreateDirectory(SAVE_DIR);
            }
            File.WriteAllBytes(Path.Combine(SAVE_DIR, SAVE_FILE_NAME), FILE_DATA);
            return true;
        }

        private async Task<string> fnc_insertF090(F090_ドキュメント管理ファイル f090)
        {
            string errMsg = "";
            int rsts = 0;
            string sql = insertF090SQL();

            if (String.IsNullOrEmpty(f090.添付元))
            {
                f090.添付元 = "R";
            }
            if (String.IsNullOrEmpty(f090.物理ファイル名))
            {
                string ext = getExt(f090.添付元ファイル名);
                工事 koji = new 工事();
                koji.依頼No = f090.工事依頼No;
                koji.契約No = f090.契約NO;
                koji.契約履歴No = f090.履歴NO.ToString();
                koji.年月明細No = f090.契約年月明細NO.ToString();
                koji.契約年月 = f090.契約年月;
                koji.予定実施年月 = f090.予定年月;
                koji.作業明細No = f090.明細NO.ToString();
                koji.枝番 = f090.枝番;
                koji.取引先 = f090.取引先コード + f090.取引先コード枝番;
                koji.検収回数 = f090.検収回数.ToString();
                koji.請求書No = f090.請求書NO;
                koji.契約書管理No = f090.契約書管理NO;
                koji.支払日 = "";
                koji.完了年月 = "";
                koji.請求書管理No = f090.請求書管理NO;

                f090.物理ファイル名 = fnc_getPhysicalFileName(f090.添付種類, ext, koji, f090.掲示ID, f090.ファイルNo, f090.添付NO);

                //20210112 add デフォルトの稟議申請ファイル名を取得
                f090.稟議申請ファイル名 = getS018_帳票名称取得(f090.添付種類) + "." + ext;

            }
            string[] type1 = { "101", "102", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "120", "125", "130", "140", "199", "901", "902", "903" };
            string[] type2 = { "131", "231" };

            string[] type3 = { "201", "202", "203", "204", "205", "220", "221", "222", "223", "225", "226", "240", "299" };
            string[] type4 = { };
            string[] type5 = { "910" };
            string[] type6 = { "900" };
            string folder = "";

            foreach (var type in type1)
            {
                if (type == f090.添付種類)
                {
                    folder = f090.工事依頼No;
                    break;
                }
            }
            if (folder.Length == 0)
            {
                foreach (var type in type2)
                {
                    if (type == f090.添付種類)
                    {
                        folder = f090.請求書NO;
                        break;
                    }
                }
            }
            if (folder.Length == 0)
            {
                foreach (var type in type3)
                {
                    if (type == f090.添付種類)
                    {
                        folder = f090.契約NO;
                        break;
                    }
                }
            }
            if (folder.Length == 0)
            {
                foreach (var type in type5)
                {
                    if (type == f090.添付種類)
                    {
                        folder = f090.物件コード;
                        break;
                    }
                }
            }
            if (folder.Length == 0)
            {
                foreach (var type in type6)
                {
                    if (type == f090.添付種類)
                    {
                        folder = f090.掲示ID.ToString();
                        break;
                    }
                }
            }

            if (String.IsNullOrEmpty(f090.ファイルパス))
            {
                if (!String.IsNullOrEmpty(folder))
                {
                    f090.ファイルパス = @"\" + folder + @"\" + f090.物理ファイル名;
                }
                else
                {
                    f090.ファイルパス = folder + @"\" + f090.物理ファイル名;
                }
            }
            if (String.IsNullOrEmpty(f090.備考1) && String.IsNullOrEmpty(f090.日付) == false)
            {
                f090.備考1 = "登録日：" + f090.日付;
                if (f090.添付種類 == "109")
                {
                    f090.備考1 = "工事開始日：" + f090.日付;
                }
                else if (f090.添付種類 == "120" || f090.添付種類 == "125")
                {
                    f090.備考1 = "工事完了日／部分検収日：" + f090.日付;
                }
                else if (f090.添付種類 == "130" || f090.添付種類 == "132" || f090.添付種類 == "140" || f090.添付種類 == "230" || f090.添付種類 == "232" || f090.添付種類 == "240")
                {
                    f090.備考1 = "支払日：" + f090.日付;
                }
                else if (f090.添付種類 == "131" || f090.添付種類 == "231")
                {
                    f090.備考1 = "請求日：" + f090.日付;
                }
                else if (f090.添付種類 == "221" || f090.添付種類 == "222" || f090.添付種類 == "223" || f090.添付種類 == "225")
                {
                    f090.備考1 = "作業実施日(至)：" + f090.日付;
                }
            }
            if ((f090.発注稟議添付 ?? 0) == 0)
            {
                f090.発注稟議添付 = getS018_発注稟議添付(f090.添付種類);
            }
            try
            {
                var paramCommon = GetParamCommon();
                rsts = await repository.DapperContext.ExcuteNonQueryAsync(sql,
                    new F090_ドキュメント管理ファイル
                    {
                        会社コード = f090.会社コード,
                        ドキュメントNO = f090.ドキュメントNO,
                        工事依頼No = f090.工事依頼No ?? "",
                        添付元 = f090.添付元 ?? "",
                        添付種類 = f090.添付種類 ?? "",
                        その他帳票名 = f090.その他帳票名 ?? "",
                        ファイルパス = f090.ファイルパス ?? "",
                        物理ファイル名 = f090.物理ファイル名 ?? "",
                        添付元ファイル名 = f090.添付元ファイル名 ?? "",
                        契約NO = f090.契約NO ?? "",
                        履歴NO = f090.履歴NO ?? 0,
                        明細NO = f090.明細NO ?? 0,
                        契約年月 = f090.契約年月 ?? "",
                        契約年月明細NO = f090.契約年月明細NO ?? 0,
                        予定年月 = f090.予定年月 ?? "",
                        契約書管理NO = f090.契約書管理NO ?? "",
                        請求書管理NO = f090.請求書管理NO ?? "",
                        枝番 = f090.枝番 ?? "",
                        取引先コード = f090.取引先コード ?? "",
                        取引先コード枝番 = f090.取引先コード枝番 ?? "",
                        検収回数 = f090.検収回数 ?? 0,
                        請求書NO = f090.請求書NO ?? "",
                        日付 = f090.日付 ?? "",
                        備考1 = f090.備考1 ?? "",
                        備考2 = f090.備考2 ?? "",
                        Ｗ参照区分 = f090.Ｗ参照区分 ?? 0,
                        Ｔ参照区分 = f090.Ｔ参照区分 ?? 0,
                        発注稟議添付 = f090.発注稟議添付,
                        稟議申請ファイル名 = f090.稟議申請ファイル名 ?? "",
                        削除区分 = f090.削除区分 != 0 && f090.削除区分 != null ? f090.削除区分 : 0, //ハノイ側修正2022/10/12　課題管理表№28：設計書「修正履歴」シートの「2022/10/11仕様変更分」「ファイルアップロード用の共通関数呼び出し時、参照時の引数を追加。F090_ドキュメント管理ファイル参照処理の条件に添付種類を追加。」
                        INSERT_TIME = DateTime.Now.ToString("yyyy/MM/dd HH:mm:ss"),
                        INSERT_PG = f090.INSERT_PG ?? "",
                        INSERT_HOST = paramCommon.HOST ?? "",
                        INSERT_ID = !String.IsNullOrEmpty(f090.INSERT_ID) ? f090.INSERT_ID : (UserContext.Current.ログインID ?? ""),
                        物件コード = f090.物件コード ?? ""
                    });

                if (rsts < 0)
                {
                    errMsg = "更新に失敗しました";
                    return errMsg;
                }
            }
            catch (Exception ex)
            {
                errMsg = "更新に失敗しました";
                return errMsg;
            }
            return errMsg;
        }

        private string fnc_getPhysicalFileName(string 帳票コード, string 拡張子, 工事 Koji, long 掲示ID, int ファイルNo, int 添付NO)
        {
            string 物理ファイル名 = "";
            DataTable dt保存時ファイル名 = get保存時ファイル名();

            物理ファイル名 = dt保存時ファイル名.Select("帳票コード='" + 帳票コード + "'").Length > 0 ? dt保存時ファイル名.Select("帳票コード='" + 帳票コード + "'")[0]["物理ファイル名"].ToString() : "";
            物理ファイル名 = 物理ファイル名.Replace("{依頼№}", Koji.依頼No);
            物理ファイル名 = 物理ファイル名.Replace("{枝番}", Koji.枝番);
            物理ファイル名 = 物理ファイル名.Replace("{取引先}", (Koji.取引先));
            物理ファイル名 = 物理ファイル名.Replace("{完了年月}", Koji.完了年月);
            物理ファイル名 = 物理ファイル名.Replace("{検収回数:00}", Koji.検収回数.PadLeft(2, '0'));
            物理ファイル名 = 物理ファイル名.Replace("{請求書№}", Koji.請求書No);
            物理ファイル名 = 物理ファイル名.Replace("{契約№}", Koji.契約No);
            物理ファイル名 = 物理ファイル名.Replace("{契約履歴№}", Koji.契約履歴No);
            物理ファイル名 = 物理ファイル名.Replace("{契約年月}", Koji.契約年月);
            物理ファイル名 = 物理ファイル名.Replace("{契約書管理№}", Koji.契約書管理No);
            物理ファイル名 = 物理ファイル名.Replace("{予定実施年月}", Koji.予定実施年月);
            物理ファイル名 = 物理ファイル名.Replace("{作業明細№}", Koji.作業明細No);
            物理ファイル名 = 物理ファイル名.Replace("{年月明細№}", Koji.年月明細No);
            物理ファイル名 = 物理ファイル名.Replace("{請求書管理№}", Koji.請求書管理No);
            物理ファイル名 = 物理ファイル名.Replace("{請求書管理番号}", Koji.請求書管理No);
            物理ファイル名 = 物理ファイル名.Replace("{掲示ID}", 掲示ID.ToString());
            物理ファイル名 = 物理ファイル名.Replace("{F091_画像管理ファイル.ファイルNo:000}", ファイルNo.ToString().PadLeft(3, '0'));
            物理ファイル名 = 物理ファイル名.Replace("{F093_一時添付ファイル.添付NO}", 添付NO.ToString());
            物理ファイル名 = 物理ファイル名.Replace("yyyyMMddHHmmss", DateTime.Now.ToString("yyyyMMddHHmmss"));
            物理ファイル名 = 物理ファイル名.Replace("yyyymmddHHmmss", DateTime.Now.ToString("yyyyMMddHHmmss"));
            物理ファイル名 = 物理ファイル名.Replace("{物件コード}", Koji.物件コード);
            if (!String.IsNullOrEmpty(拡張子))
            {
                物理ファイル名 = 物理ファイル名 + (拡張子.Substring(0, 1) == "." ? "" : ".") + 拡張子;
            }
            if (物理ファイル名 != null)
            {
                物理ファイル名 = 物理ファイル名.Replace(@"/", "");
                物理ファイル名 = RemoveInvalidCharacter(物理ファイル名);
            }
            return 物理ファイル名;
        }

        private string getS018_帳票名称取得(string 添付種類)
        {
            S018_ドキュメント定義 s018Db = repository.S018_ドキュメント定義.FindFirst(x => x.帳票種類 == 添付種類);
            var 帳票名称 = s018Db.稟議帳票名称 == "" || s018Db.稟議帳票名称 == null ? s018Db.帳票名称 : s018Db.稟議帳票名称;
            return 帳票名称;
        }

        private int getS018_発注稟議添付(string 添付種類)
        {
            S018_ドキュメント定義 s018Db = repository.S018_ドキュメント定義.FindFirst(x => x.帳票種類 == 添付種類);
            return s018Db.発注稟議添付 ?? 0;
        }

        async Task<WebResponseContent> insertF090WithSaveFile(JObject data, byte[] FILE_DATA)
        {
            string errStr = String.Empty;
            WebResponseContent responseContent = new WebResponseContent();
            try
            {
                F090_ドキュメント管理ファイル f090 = data["F090_ドキュメント管理ファイル"].ToObject<F090_ドキュメント管理ファイル>();
                if (f090 == null)
                {
                    return responseContent.Error(ResponseType.BadRequest);
                }

                var ドキュメントNO = getSeqドキュメントNO();
                f090.ドキュメントNO = ドキュメントNO;
                errStr = await fnc_insertF090(f090);

                string SAVE_FILE_NAME = f090.物理ファイル名;
                string ルートパス = await getS018_ルートパス(f090.添付種類);
                string SAVE_DIR = ルートパス + f090.ファイルパス;
                SAVE_DIR = new FileInfo(SAVE_DIR).DirectoryName;
                saveFile(SAVE_DIR, SAVE_FILE_NAME, FILE_DATA);

                if (errStr != string.Empty)
                {
                    return responseContent.Error(errStr);
                }
                await repository.SaveChangesAsync();

                return responseContent.OK();
            }
            catch (Exception ex)
            {
                repository.Dispose();
                errStr = ex.Message;
                return responseContent.Error(ResponseType.ServerError, errStr);
            }
            finally
            {
            }
        }
        // 20230726 add 3回チェック処理追加
        async Task<string> insertF090WithSaveFile1(JObject data, byte[] FILE_DATA, int cnt)
        {
            string errStr = String.Empty;
            try
            {
                F090_ドキュメント管理ファイル f090 = data["F090_ドキュメント管理ファイル"].ToObject<F090_ドキュメント管理ファイル>();
                if (f090 == null)
                {
                    return "f090 is null";
                }

                var ドキュメントNO = getSeqドキュメントNO();
                f090.ドキュメントNO = ドキュメントNO;
                errStr = await fnc_insertF090(f090);
                if (errStr != string.Empty)
                {
                    return errStr;
                }

                string SAVE_FILE_NAME = f090.物理ファイル名;
                string ルートパス = await getS018_ルートパス(f090.添付種類);
                string PATH = ルートパス + f090.ファイルパス;
                string SAVE_DIR = new FileInfo(PATH).DirectoryName;
                saveFile(SAVE_DIR, SAVE_FILE_NAME, FILE_DATA);

                bool result = true;
                string message = string.Empty;
                byte[] bt = await DownloadFile(PATH);
                if (bt == null || bt.Length == 0)
                {
                    result = false;
                    int milliSeconds = (int)TimeSpan.FromSeconds(AppSetting.PrintAPIRecallDelayTime).TotalMilliseconds;

                    message = "insertF090WithSaveFile round: " + cnt.ToString();
                    string parameters = JsonConvert.SerializeObject(f090);
                    Filters.LogActionAttribute.WriteError(message, parameters);

                    errStr = fnc_deleteF090(ドキュメントNO, f090.INSERT_PG);
                    if (cnt < 3)
                    {
                        await Task.Delay(milliSeconds); // タイマー時間設定方法：30秒⇒30　、　1分⇒60
                        cnt++;
                        errStr = await insertF090WithSaveFile1(data, FILE_DATA, cnt);
                        if (errStr == string.Empty)
                        {
                            result = true;
                        }
                    }
                }

                if (result == true)
                {
                    return "";
                }
                else
                {
                    return "insertF090WithSaveFile";
                }
            }
            catch (Exception ex)
            {
                errStr = ex.Message;
                return errStr;
            }
            finally
            {
            }
        }
        // 20230726 add 
        // 20230726 add 3回チェック処理追加
        private string fnc_deleteF090(long ドキュメントNO, string UPDATE_PG)
        {
            string errMsg = "";
            int rsts = 0;
            string sql = deleteF090SQL();

            try
            {
                var paramCommon = GetParamCommon();
                rsts = repository.DapperContext.ExcuteNonQuery(sql,
                    new F090_ドキュメント管理ファイル
                    {
                        ドキュメントNO = ドキュメントNO,
                        UPDATE_TIME = DateTime.Now.ToString("yyyy/MM/dd HH:mm:ss"),
                        UPDATE_PG = UPDATE_PG,
                        UPDATE_HOST = paramCommon.HOST,
                        UPDATE_ID = UserContext.Current.社員ID
                    });

                if (rsts < 0)
                {
                    errMsg = "更新に失敗しました";
                    return errMsg;
                }
            }
            catch (Exception ex)
            {
                errMsg = "更新に失敗しました";
                return errMsg;
            }
            return errMsg;
        }
        // 20230726 add
        // 20230726 add 3回チェック処理追加
        private string deleteF090SQL()
        {
            StringBuilder sql = new StringBuilder();

            sql.AppendLine(" UPDATE F090_ドキュメント管理ファイル ");
            sql.AppendLine("    SET ");
            sql.AppendLine("        削除区分 =  1 ");
            sql.AppendLine("       ,UPDATE_TIME = @UPDATE_TIME ");
            sql.AppendLine("       ,UPDATE_PG = @UPDATE_PG ");
            sql.AppendLine("       ,UPDATE_HOST = @UPDATE_HOST ");
            sql.AppendLine("       ,UPDATE_ID = @UPDATE_ID ");
            sql.AppendLine("  WHERE ドキュメントNO = @ドキュメントNO ");

            return sql.ToString();
        }
        // 20230726 add 

        async Task<WebResponseContent> GetAllMenuActionList()
        {
            WebResponseContent responseContent = new WebResponseContent();

            string sql =
                        $"   SELECT                                          " +
                        $"          S002.メニューID     AS MenuId            " +
                        $"         ,S002.メニュー名     AS MenuName          " +
                        $"         ,S003.サブメニューID AS SubMenuId         " +
                        $"         ,S003.サブメニュー名 AS SubMenuName       " +
                        $"         ,S003.フォームID     AS Path              " +
                        $"     FROM ST002_メニューマスタ     S002            " +
                        $"LEFT JOIN ST003_サブメニューマスタ S003            " +
                        $"       ON S002.メニューID = S003.メニューID        " +
                        $"    WHERE S003.削除区分 = 0                        " +
                        $"    ORDER BY S003.表示順                         ";

            responseContent.Data = await repository.DapperContext.QueryListAsync<object>(sql, null);

            return responseContent;
        }

        #region SQL
        private string insertF090SQL()
        {
            StringBuilder sql = new StringBuilder();

            sql.AppendLine("INSERT INTO F090_ドキュメント管理ファイル");
            sql.AppendLine("   (ドキュメントNO");
            sql.AppendLine("   ,会社コード");
            sql.AppendLine("   ,工事依頼No");
            sql.AppendLine("   ,添付元");
            sql.AppendLine("   ,添付種類");
            sql.AppendLine("   ,その他帳票名	");
            sql.AppendLine("   ,ファイルパス");
            sql.AppendLine("   ,物理ファイル名");
            sql.AppendLine("   ,添付元ファイル名");
            sql.AppendLine("   ,契約NO");
            sql.AppendLine("   ,履歴NO");
            sql.AppendLine("   ,明細NO");
            sql.AppendLine("   ,契約年月");
            sql.AppendLine("   ,契約年月明細NO");
            sql.AppendLine("   ,予定年月");
            sql.AppendLine("   ,契約書管理NO");
            sql.AppendLine("   ,請求書管理NO");
            sql.AppendLine("   ,枝番");
            sql.AppendLine("   ,取引先コード	");
            sql.AppendLine("   ,取引先コード枝番");
            sql.AppendLine("   ,検収回数");
            sql.AppendLine("   ,請求書NO");
            sql.AppendLine("   ,日付");
            sql.AppendLine("   ,備考1");
            sql.AppendLine("   ,備考2");
            sql.AppendLine("   ,Ｗ参照区分");
            sql.AppendLine("   ,Ｔ参照区分");
            sql.AppendLine("   ,発注稟議添付");
            sql.AppendLine("   ,稟議申請ファイル名");
            sql.AppendLine("   ,削除区分");
            sql.AppendLine("   ,INSERT_TIME");
            sql.AppendLine("   ,INSERT_PG");
            sql.AppendLine("   ,INSERT_HOST");
            sql.AppendLine("   ,INSERT_ID");
            sql.AppendLine("   ,物件コード");
            sql.AppendLine("   )");
            sql.AppendLine(" VALUES ");
            sql.AppendLine("   (@ドキュメントNO");
            sql.AppendLine("   ,@会社コード");
            sql.AppendLine("   ,@工事依頼No");
            sql.AppendLine("   ,@添付元");
            sql.AppendLine("   ,@添付種類");
            sql.AppendLine("   ,@その他帳票名	");
            sql.AppendLine("   ,@ファイルパス");
            sql.AppendLine("   ,@物理ファイル名");
            sql.AppendLine("   ,@添付元ファイル名");
            sql.AppendLine("   ,@契約NO");
            sql.AppendLine("   ,@履歴NO");
            sql.AppendLine("   ,@明細NO");
            sql.AppendLine("   ,@契約年月");
            sql.AppendLine("   ,@契約年月明細NO");
            sql.AppendLine("   ,@予定年月");
            sql.AppendLine("   ,@契約書管理NO");
            sql.AppendLine("   ,@請求書管理NO");
            sql.AppendLine("   ,@枝番");
            sql.AppendLine("   ,@取引先コード	");
            sql.AppendLine("   ,@取引先コード枝番");
            sql.AppendLine("   ,@検収回数");
            sql.AppendLine("   ,@請求書NO");
            sql.AppendLine("   ,@日付");
            sql.AppendLine("   ,@備考1");
            sql.AppendLine("   ,@備考2");
            sql.AppendLine("   ,@Ｗ参照区分");
            sql.AppendLine("   ,@Ｔ参照区分");
            sql.AppendLine("   ,@発注稟議添付");
            sql.AppendLine("   ,@稟議申請ファイル名");
            sql.AppendLine("   ,@削除区分"); //ハノイ側修正2022/10/12　課題管理表№28：設計書「修正履歴」シートの「2022/10/11仕様変更分」「ファイルアップロード用の共通関数呼び出し時、参照時の引数を追加。F090_ドキュメント管理ファイル参照処理の条件に添付種類を追加。」
            sql.AppendLine("   ,@INSERT_TIME");
            sql.AppendLine("   ,@INSERT_PG");
            sql.AppendLine("   ,@INSERT_HOST");
            sql.AppendLine("   ,@INSERT_ID");
            sql.AppendLine("   ,@物件コード");
            sql.AppendLine("   )");

            return sql.ToString();
        }

        #endregion

        static Byte[] getFileImage(string PATH)
        {
            string errStr = String.Empty;
            try
            {
                NetResourceConnect();
                var 存在チェック = new FileInfo(PATH);
                if (存在チェック.Exists)
                {
                    byte[] by = System.IO.File.ReadAllBytes(PATH);
                    return by;
                }
                else
                {
                    return null;
                }
            }
            catch (Exception ex)
            {
                return null;
            }
        }
    }
}
