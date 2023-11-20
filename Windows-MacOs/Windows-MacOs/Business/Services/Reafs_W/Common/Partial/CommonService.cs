using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Data;
using Newtonsoft.Json.Linq;
using System.Linq;
using Newtonsoft.Json;
using WinMacOs.Utility.Extensions;
using WinMacOs.Utility.PInvoke.Mpr;
using WinMacOs.ActionFilter;
using WinMacOs.DataRepository.Utilities;
using WinMacOs.Models.Enums;
using WinMacOs.Utility.TableModels;
using WinMacOs.Models;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Business.Services.Reafs_W
{

    public partial class CommonService
    {
        public struct 工事
        {
            public string 依頼No { get; set; }
            public string 契約No { get; set; }
            public string 契約履歴No { get; set; }
            public string 年月明細No { get; set; }
            public string 契約年月 { get; set; }
            public string 予定実施年月 { get; set; }
            public string 作業明細No { get; set; }
            public string 枝番 { get; set; }
            public string 取引先 { get; set; }
            public string 検収回数 { get; set; }
            public string 請求書No { get; set; }
            public string 契約書管理No { get; set; }
            public string 支払日 { get; set; }
            public string 完了年月 { get; set; }
            public string 請求書管理No { get; set; }
            public string 物件コード { get; set; }
        }

        public async Task<byte[]> GetImage(string path, string type)
        {
            try
            {
                string ルートパス = await getS018_ルートパス(type);
                byte[] imageArray = new byte[] { };
                if (!string.IsNullOrEmpty(ルートパス))
                {
                    Mpr.NetResourceConnect();
                    string sFilePath = ルートパス + path;
                    if (System.IO.File.Exists(sFilePath))
                    {
                        imageArray = await FileExtensions.ReadAllBytesAsync(sFilePath);
                    }
                }
                return imageArray;
            }
            catch (Exception ex)
            {
                repository.Dispose();
                LogActionAttribute.WriteError(ex.Message + ex.StackTrace, path + ";" + type);
                return null;
            }
        }

        // ハノイ側修正2022/10/21　課題管理表№186：設計書「修正履歴」シートの「2022/10/20仕様変更分」「工事区分のハテナボックス押下時の処理追記」START
        public async Task<byte[]> GetImage2(string path)
        {
            try
            {
                byte[] imageArray = new byte[] { };

                Mpr.NetResourceConnect();
                if (System.IO.File.Exists(path))
                {
                    imageArray = await FileExtensions.ReadAllBytesAsync(path);
                }
                return imageArray;
            }
            catch (Exception ex)
            {
                LogActionAttribute.WriteError(ex.Message + ex.StackTrace, path);
                return null;
            }
        }
        // ハノイ側修正2022/10/21　課題管理表№186：設計書「修正履歴」シートの「2022/10/20仕様変更分」「工事区分のハテナボックス押下時の処理追記」END

        private async Task<string> getS018_ルートパス(string 添付種類)
        {
            S018_ドキュメント定義 s018Db = await repository.S018_ドキュメント定義.FindFirstAsync(x => x.帳票種類 == 添付種類);
            return s018Db.ルートパス;
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

        /// <summary>
        /// Download File
        /// </summary>
        /// <param name="path"></param>
        /// <returns></returns>
        public async Task<byte[]> DownloadFile(string path)
        {
            try
            {
                byte[] imageArray = new byte[] { };
                if (!string.IsNullOrEmpty(path))
                {
                    Mpr.NetResourceConnect();
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
                LogActionAttribute.WriteError(ex.Message, path); // ハノイ側修正2023/07/10　STEP2_W　課題管理表№364：「最大 3 回の結果チェック、再実行」
                return null;
            }
        }

        #region File

        #region MyRegion
        public async Task<Stream> getImageTempF093(int 添付NO)
        {
            try
            {
                F093_一時添付ファイル f093Data = await repository.F093_一時添付ファイル.FindFirstAsync(x => x.添付NO == 添付NO && x.枝番 == 1);

                if (f093Data != null)
                {
                    return new MemoryStream(f093Data.ファイルデータ);
                }
                else return null;
            }
            catch (Exception ex)
            {
                LogActionAttribute.WriteError(ex.Message + ex.StackTrace, 添付NO.ToString());
                repository.Dispose();
                return null;
            }
        }
        #endregion

        #region Helper
        private string getExt(string 添付ファイル名)
        {
            string ext = "";
            if (添付ファイル名.LastIndexOf(".") >= 0 && 添付ファイル名.LastIndexOf(".") < 添付ファイル名.Length - 1)
            {
                ext = 添付ファイル名.Substring(添付ファイル名.LastIndexOf(".") + 1);
            }
            return ext;
        }

        private DataTable get保存時ファイル名()
        {
            string[,] arrCol =
            {
                {"101", "依頼書_{依頼№}_yyyyMMddHHmmss"},
                {"102", "見積書_{依頼№}{枝番}_{取引先}_yyyyMMddHHmmss"},
                {"103", "見積書_{依頼№}{枝番}_{取引先}_yyyyMMddHHmmss"},
                {"104", "見積書_{依頼№}{枝番}_yyyyMMddHHmmss"},
                {"105", "見積書_{依頼№}{枝番}_yyyyMMddHHmmss"},
                {"106", "概要書_{依頼№}_yyyyMMddHHmmss"},
                {"107", "概要書添付_{依頼№}_yyyyMMddHHmmss"},
                {"108", "稟議書_{依頼№}_yyyyMMddHHmmss"},
                {"109", "発注書_{依頼№}{枝番}_{取引先}_yyyyMMddHHmmss"},
                {"110", "請書_{依頼№}{枝番}_{取引先}_yyyyMMddHHmmss"},
                {"111", "発注書_{依頼№}{枝番}_yyyyMMddHHmmss"},
                {"112", "請書_{依頼№}{枝番}_yyyyMMddHHmmss"},
                {"113", "予算管理表_{依頼№}_yyyymmddHHmmss"},
                {"114", "押印書類実印_{依頼№}_yyyymmddHHmmss"},
                {"115", "押印書類銀行印_{依頼№}_yyyymmddHHmmss"},
                {"116", "押印書類認印_{依頼№}_yyyymmddHHmmss"},
                {"117", "押印書類角印_{依頼№}_yyyymmddHHmmss"},
                {"120", "完了報告書_{依頼№}{枝番}_{取引先}_{完了年月}{検収回数:00}_yyyyMMddHHmmss"},
                {"125", "完了報告書_{依頼№}{枝番}_{検収回数:00}_yyyyMMddHHmmss"},
                {"130", "請求書_{依頼№}{枝番}_{取引先}_{検収回数:00}_yyyyMMddHHmmss"},
                {"131", "請求書_{請求書№}_yyyyMMddHHmmss"},
                {"132", "支払申請書_{依頼№}{枝番}_{取引先}_{検収回数:00}_yyyyMMddHHmmss"},
                {"140", "支払通知書_{依頼№}{枝番}_{取引先}_{検収回数:00}_yyyyMMddHHmmss"},
                {"199", "その他_{依頼№}_yyyyMMddHHmmss"},
                {"201", "依頼書_{契約№}_yyyyMMddHHmmss"},
                {"202", "見積書_{契約№}{契約履歴№}{枝番}_{取引先}_{契約年月}_yyyyMMddHHmmss"},
                {"203", "見積書_{契約№}{契約履歴№}{枝番}_{取引先}_{契約年月}_yyyyMMddHHmmss"},
                {"204", "見積書_{契約№}{契約履歴№}{枝番}_{契約年月}_yyyyMMddHHmmss"},
                {"205", "見積書_{契約№}{契約履歴№}{枝番}_{契約年月}_yyyyMMddHHmmss"},
                {"213", "受託契約書Word_{契約書管理№}_yyyyMMddHHmmss"},
                {"214", "受託契約書仮_{契約書管理№}_yyyyMMddHHmmss"},
                {"215", "受託契約書_{契約書管理№}_yyyyMMddHHmmss"},
                {"216", "委託契約書Word_{契約書管理№}_yyyyMMddHHmmss"},
                {"217", "委託契約書仮_{契約書管理№}_yyyyMMddHHmmss"},
                {"218", "委託契約書_{契約書管理№}_yyyyMMddHHmmss"},
                {"220", "完了報告書_{契約№}{契約履歴№}_{取引先}_{予定実施年月}{作業明細№}99_yyyyMMddHHmmss"},
                {"221", "完了添付_{契約№}{契約履歴№}_{取引先}_{予定実施年月}{作業明細№}{年月明細№}_yyyyMMddHHmmss"},
                {"222", "法定添付_{契約№}{契約履歴№}_{取引先}_{予定実施年月}{作業明細№}{年月明細№}_yyyyMMddHHmmss"},
                {"223", "実費精算_{契約№}{契約履歴№}_{取引先}_{予定実施年月}_yyyyMMddHHmmss"},
                {"225", "完了報告書_{契約№}{契約履歴№}_{予定実施年月}_yyyyMMddHHmmss"},
                {"226", "法定添付_{契約№}{契約履歴№}_{取引先}_{予定実施年月}{作業明細№}99_yyyyMMddHHmmss"},
                {"230", "請求書_{請求書管理番号}_yyyyMMddHHmmss"},
                {"231", "請求書_{請求書№}_yyyyMMddHHmmss"},
                {"232", "支払申請書_{請求書管理№}_yyyyMMddHHmmss"},
                {"240", "支払通知書_{契約№}{契約履歴№}_{取引先}_{検収回数:00}_yyyyMMddHHmmss"},
                {"299", "その他_{契約№}_yyyyMMddHHmmss"},
                {"900", "{掲示ID}_{F093_一時添付ファイル.添付NO}"},
                {"901", "901_{F091_画像管理ファイル.ファイルNo:000}_{F093_一時添付ファイル.添付NO}"},
                {"902", "902_{F091_画像管理ファイル.ファイルNo:000}_{F093_一時添付ファイル.添付NO}"},
                {"903", "903_{F091_画像管理ファイル.ファイルNo:000}_{F093_一時添付ファイル.添付NO}"},
                {"910", "{物件コード}_{枝番}_yyyyMMddHHmmss"}
            };
            var dt = new DataTable();
            dt.Columns.Add("帳票コード");
            dt.Columns.Add("物理ファイル名");
            for (int i = 0; i < arrCol.GetLength(0) - 1; i++)
            {
                dt.Rows.Add(dt.NewRow());
                dt.Rows[i]["帳票コード"] = arrCol[i, 0];
                dt.Rows[i]["物理ファイル名"] = arrCol[i, 1];
            }

            return dt;
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

        private string RemoveInvalidCharacter(string str)
        {
            var invalidFileChars = Path.GetInvalidFileNameChars();
            if (str != null)
            {
                foreach (var invalidFileChar in invalidFileChars)
                {
                    str = str.Replace(invalidFileChar, "".ToChar());
                }
            }
            return str;
        }
        #endregion 

        #region SQL
        private string getSeq添付NOSQL()
        {
            StringBuilder sql = new StringBuilder();

            sql.AppendLine(" SELECT NEXT VALUE FOR SEQ_添付NO  ");

            return sql.ToString();
        }

        private string getSeqドキュメントNOSQL()
        {
            StringBuilder sql = new StringBuilder();

            sql.AppendLine(" SELECT NEXT VALUE FOR SEQ_ドキュメントNO  ");

            return sql.ToString();
        }

        private string insertF093SQL()
        {
            StringBuilder sql = new StringBuilder();

            sql.AppendLine("INSERT INTO F093_一時添付ファイル");
            sql.AppendLine("   (添付NO");
            sql.AppendLine("   ,枝番");
            sql.AppendLine("   ,添付元ファイル名");
            sql.AppendLine("   ,ファイルデータ");
            sql.AppendLine("   ,削除区分");
            sql.AppendLine("   ,INSERT_TIME");
            sql.AppendLine("   ,INSERT_PG");
            sql.AppendLine("   ,INSERT_HOST");
            sql.AppendLine("   ,INSERT_ID");
            sql.AppendLine("   )");
            sql.AppendLine(" VALUES ");
            sql.AppendLine("   (@添付NO");
            sql.AppendLine("   ,@枝番");
            sql.AppendLine("   ,@添付元ファイル名");
            sql.AppendLine("   ,@ファイルデータ");
            sql.AppendLine("   ,@削除区分");
            sql.AppendLine("   ,@INSERT_TIME");
            sql.AppendLine("   ,@INSERT_PG");
            sql.AppendLine("   ,@INSERT_HOST");
            sql.AppendLine("   ,@INSERT_ID");
            sql.AppendLine("   )");

            return sql.ToString();
        }

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
            sql.AppendLine("   ,0");
            sql.AppendLine("   ,@INSERT_TIME");
            sql.AppendLine("   ,@INSERT_PG");
            sql.AppendLine("   ,@INSERT_HOST");
            sql.AppendLine("   ,@INSERT_ID");
            sql.AppendLine("   ,@物件コード");
            sql.AppendLine("   )");

            return sql.ToString();
        }

        private string deleteOldF093SQL()
        {
            StringBuilder sql = new StringBuilder();

            sql.AppendLine("DELETE ");
            sql.AppendLine("  FROM F093_一時添付ファイル");
            sql.AppendLine(" WHERE INSERT_TIME < CONVERT(char(10), DATEADD(HOUR, -12, GETDATE()), 111) + ' ' + CONVERT(char(8), DATEADD(HOUR, -12, GETDATE()), 8)");

            return sql.ToString();
        }

        private string deleteF093SQL()
        {
            StringBuilder sql = new StringBuilder();

            sql.AppendLine(" DELETE FROM F093_一時添付ファイル ");
            sql.AppendLine("  WHERE 添付NO = @添付NO ");
            sql.AppendLine("    AND 枝番 = @枝番 ");

            return sql.ToString();
        }

        private string getF093_ファイルデータSQL()
        {
            StringBuilder sql = new StringBuilder();

            sql.AppendLine(" SELECT ファイルデータ");
            sql.AppendLine("   FROM F093_一時添付ファイル");
            sql.AppendLine("  WHERE 添付NO = @添付NO");
            sql.AppendLine("    AND 枝番 = 1");

            return sql.ToString();
        }

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

        #endregion

        #region CALL

        public byte[] getF093_ファイルデータ(int 添付NO)
        {
            string sql = getF093_ファイルデータSQL();
            var ファイルデータ = repository.F093_一時添付ファイル.FindFirst((v) => v.添付NO == 添付NO).ファイルデータ;
            return ファイルデータ;
        }

        private string deleteF093(int 添付NO)
        {
            string errMsg = "";
            int rsts = 0;
            string sql = deleteF093SQL();

            try
            {
                rsts = repository.DapperContext.ExcuteNonQuery(sql,
                    new F093_一時添付ファイル
                    {
                        添付NO = 添付NO,
                        枝番 = 1
                    });

                if (rsts < 0)
                {
                    errMsg = "更新に失敗しました";
                    return errMsg;
                }
            }
            catch (Exception ex)
            {
                LogActionAttribute.WriteError(ex.Message + ex.StackTrace, 添付NO.ToString());
                errMsg = "更新に失敗しました";
                return errMsg;
            }
            return errMsg;
        }

        private int getSeq添付NO()
        {
            string sql = getSeq添付NOSQL();
            int seqNo = (int)(Int64)repository.DapperContext.ExecuteScalar(sql, null);
            return seqNo;
        }

        private Int64 getSeqドキュメントNO()
        {
            string sql = getSeqドキュメントNOSQL();
            Int64 seqNo = (Int64)repository.DapperContext.ExecuteScalar(sql, null);
            return seqNo;
        }

        private async Task<string> fnc_insertF093(F093_一時添付ファイル f093)
        {
            string errMsg = "";
            int rsts = 0;
            string sql = insertF093SQL();

            try
            {
                var paramCommon = GetParamCommon();
                rsts = await repository.DapperContext.ExcuteNonQueryAsync(sql,
                    new F093_一時添付ファイル
                    {
                        添付NO = f093.添付NO,
                        枝番 = 1,
                        添付元ファイル名 = f093.添付元ファイル名,
                        ファイルデータ = f093.ファイルデータ,
                        削除区分 = 0,
                        INSERT_TIME = DateTime.Now.ToString("yyyy/MM/dd HH:mm:ss"),
                        INSERT_PG = f093.INSERT_PG,
                        INSERT_HOST = paramCommon.HOST,
                        INSERT_ID = UserContext.Current.社員ID
                    });

                if (rsts < 0)
                {
                    errMsg = "更新に失敗しました";
                    return errMsg;
                }
            }
            catch (Exception ex)
            {
                LogActionAttribute.WriteError(ex.Message + ex.StackTrace, System.Text.Json.JsonSerializer.Serialize(f093));
                errMsg = "更新に失敗しました";
                return errMsg;
            }
            return errMsg;
        }

        private async Task<string> fnc_insertF090(F090_ドキュメント管理ファイル f090)
        {
            string errMsg = "";
            int rsts = 0;
            string sql = insertF090SQL();

            if (String.IsNullOrEmpty(f090.添付元))
            {
                f090.添付元 = "W";
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
                        INSERT_TIME = DateTime.Now.ToString("yyyy/MM/dd HH:mm:ss"),
                        INSERT_PG = f090.INSERT_PG ?? "",
                        INSERT_HOST = paramCommon.HOST ?? "",
                        INSERT_ID = UserContext.Current.社員ID ?? "",
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
                LogActionAttribute.WriteError(ex.Message + ex.StackTrace, System.Text.Json.JsonSerializer.Serialize(f090));
                errMsg = "更新に失敗しました";
                return errMsg;
            }
            return errMsg;
        }

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
                LogActionAttribute.WriteError(ex.Message + ex.StackTrace, ドキュメントNO.ToString());
                errMsg = "更新に失敗しました";
                return errMsg;
            }
            return errMsg;
        }

        public async Task<WebResponseContent> insertF093(JObject data)
        {
            string errStr = String.Empty;
            int 添付NO = 0;
            WebResponseContent responseContent = new WebResponseContent();
            try
            {
                errStr = await fnc_deleteOldF093();

                if (errStr == string.Empty)
                {
                    F093_一時添付ファイル f093 = data["F093_一時添付ファイル"].ToObject<F093_一時添付ファイル>();
                    if (f093 == null)
                    {
                        return responseContent.Error(ResponseType.BadRequest);
                    }
                    添付NO = getSeq添付NO();
                    f093.添付NO = 添付NO;
                    //ハノイ側修正2022/10/18　課題管理表№26：「2022/10/04依頼分」「ファイルサイズが0KBのファイルをアップロードするとエラーが発生します。」
                    if (f093.ファイルデータ == null)
                        f093.ファイルデータ = new byte[] { };
                    errStr = await fnc_insertF093(f093);
                }

                if (errStr != string.Empty)
                {
                    return responseContent.Error(errStr);
                }

                responseContent.Data = 添付NO;
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

        public async Task<WebResponseContent> insertF090(JObject data)
        {
            string errStr = String.Empty;
            WebResponseContent responseContent = new WebResponseContent();
            F090_ドキュメント管理ファイル f090 = data["F090_ドキュメント管理ファイル"].ToObject<F090_ドキュメント管理ファイル>();
            if (f090 == null)
            {
                return responseContent.Error(ResponseType.BadRequest);
            }
            try
            {
                var ドキュメントNO = getSeqドキュメントNO();
                f090.ドキュメントNO = ドキュメントNO;
                errStr = await fnc_insertF090(f090);

                if (errStr != string.Empty)
                {
                    return responseContent.Error(errStr);
                }

                responseContent.Data = ドキュメントNO;
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

        private async Task<string> fnc_deleteOldF093()
        {
            string errMsg = "";
            int rsts = 0;
            string sql = deleteOldF093SQL();
            try
            {
                var paramCommon = GetParamCommon();
                rsts = await repository.DapperContext.ExcuteNonQueryAsync(sql, null);

                if (rsts < 0)
                {
                    errMsg = "更新に失敗しました";
                    return errMsg;
                }
            }
            catch (Exception ex)
            {
                LogActionAttribute.WriteError(ex.Message + ex.StackTrace, sql);
                errMsg = "更新に失敗しました";
                return errMsg;
            }
            return errMsg;
        }

        private bool fnc_saveFile(string SAVE_DIR, string SAVE_FILE_NAME, byte[] FILE_DATA)
        {
            try
            {
                Mpr.NetResourceConnect();
                if (Directory.Exists(SAVE_DIR) == false)
                {
                    Directory.CreateDirectory(SAVE_DIR);
                }
                File.WriteAllBytes(Path.Combine(SAVE_DIR, SAVE_FILE_NAME), FILE_DATA);
                return true;
            }
            catch (Exception ex)
            {
                LogActionAttribute.WriteError(ex.Message + ex.StackTrace, SAVE_DIR + Environment.NewLine + SAVE_FILE_NAME + Environment.NewLine + Convert.ToBase64String(FILE_DATA));
                return false;
            }
        }

        public WebResponseContent saveFile(string SAVE_DIR, string SAVE_FILE_NAME, byte[] FILE_DATA)
        {
            string errStr = String.Empty;
            WebResponseContent responseContent = new WebResponseContent();
            try
            {
                var check = fnc_saveFile(SAVE_DIR, SAVE_FILE_NAME, FILE_DATA);
                if (!check)
                {
                    return responseContent.Error(ResponseType.ServerError, errStr);
                }
                return responseContent.OK();
            }
            catch (Exception ex)
            {
                errStr = ex.Message;
                return responseContent.Error(ResponseType.ServerError, errStr);
            }
            finally
            {
            }
        }

        private bool fnc_saveFileByF093(string SAVE_DIR, string SAVE_FILE_NAME, int 添付NO)
        {
            byte[] FILE_DATA = getF093_ファイルデータ(添付NO);
            bool res = fnc_saveFile(SAVE_DIR, SAVE_FILE_NAME, FILE_DATA);
            String str = deleteF093(添付NO);
            return res && String.IsNullOrEmpty(str);
        }

        public WebResponseContent saveFileByF093(string SAVE_DIR, string SAVE_FILE_NAME, int 添付NO)
        {
            string errStr = String.Empty;
            WebResponseContent responseContent = new WebResponseContent();
            try
            {
                var check = fnc_saveFileByF093(SAVE_DIR, SAVE_FILE_NAME, 添付NO);
                if (!check)
                {
                    return responseContent.Error(ResponseType.ServerError, errStr);
                }
                return responseContent.OK();
            }
            catch (Exception ex)
            {
                errStr = ex.Message;
                return responseContent.Error(ResponseType.ServerError, errStr);
            }
            finally
            {
            }
        }

        public async Task<WebResponseContent> insertF090WithSaveFile(JObject data, string 添付NO)
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
                fnc_saveFileByF093(SAVE_DIR, SAVE_FILE_NAME, int.Parse(添付NO));

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

        public async Task<WebResponseContent> insertF090WithSaveFile(JObject data, byte[] FILE_DATA)
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
                fnc_saveFile(SAVE_DIR, SAVE_FILE_NAME, FILE_DATA);

                if (errStr != string.Empty)
                {
                    return responseContent.Error(errStr);
                }
                //await repository.SaveChangesAsync();

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

        // ハノイ側修正2023/07/10　STEP2_W　課題管理表№364：「最大 3 回の結果チェック、再実行」START
        public async Task<string> insertF090WithSaveFile1(JObject data, byte[] FILE_DATA, int cnt)
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
                fnc_saveFile(SAVE_DIR, SAVE_FILE_NAME, FILE_DATA);

                bool result = true;
                string message = string.Empty;
                byte[] bt = await DownloadFile(PATH);
                if (bt == null || bt.Length == 0)
                {
                    result = false;
                    int milliSeconds = (int)TimeSpan.FromSeconds(AppSetting.PrintAPIRecallDelayTime).TotalMilliseconds;

                    message = "insertF090WithSaveFile round: " + cnt.ToString();
                    string parameters = JsonConvert.SerializeObject(f090);
                    LogActionAttribute.WriteError(message, parameters);

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
        // ハノイ側修正2023/07/10　STEP2_W　課題管理表№364：「最大 3 回の結果チェック、再実行」END

        public WebResponseContent getFileImage(string PATH)
        {
            string errStr = String.Empty;
            WebResponseContent responseContent = new WebResponseContent();
            try
            {
                Mpr.NetResourceConnect();

                var 存在チェック = new FileInfo(PATH);
                if (存在チェック.Exists)
                {
                    byte[] by = System.IO.File.ReadAllBytes(PATH);
                    responseContent.Data = by;
                }
                else
                {
                    responseContent.Data = null;
                }

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

        public WebResponseContent deleteF090(JObject data)
        {
            string errStr = String.Empty;
            WebResponseContent responseContent = new WebResponseContent();
            try
            {
                var ドキュメントNO = int.Parse(data["ドキュメントNO"].ToString());
                var UPDATE_PG = data["UPDATE_PG"].ToString();
                errStr = fnc_deleteF090(ドキュメントNO, UPDATE_PG);

                if (errStr != string.Empty)
                {
                    return responseContent.Error(errStr);
                }
                repository.SaveChangesAsync();

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

        public async Task<WebResponseContent> GetControlActiveButton(string menuId, string submenuId)
        {
            string msg = string.Empty;
            WebResponseContent responseContent = new WebResponseContent();
            var userId = UserContext.Current.社員ID;
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

        #endregion 

        #endregion 
    }
}
