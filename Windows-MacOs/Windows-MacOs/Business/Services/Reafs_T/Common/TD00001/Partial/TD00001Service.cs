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
using WinMacOs.ActionFilter;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.DataRepository.Utilities;
using WinMacOs.Models;
using WinMacOs.Models.Enums;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.Extensions;
using WinMacOs.Utility.PInvoke.CommonModels;
using WinMacOs.Utility.PInvoke.Mpr;
using WinMacOs.Utility.TableModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Business.Services.Reafs_T
{
    public partial class TD00001Service
    {
        private DateTime today ;
        #region お知らせ通知一覧の取得
        /// <summary>　
        /// お知らせ通知一覧の取得
        /// </summary>
        /// <returns></returns>
        public WebResponseContent GetNewsList()
        {
            string msg = string.Empty;

            WebResponseContent responseContent = new WebResponseContent();
            try
            {
                StringBuilder sql = new StringBuilder();

                sql.AppendLine(" ");
                sql.AppendLine(" SELECT ");
                sql.AppendLine("        S014.掲示開始日 AS KEIJI_S_YMD ");
                sql.AppendLine("      , 'Ｒｅａｆｓ－' +    ");
                sql.AppendLine("        CASE S014.発生元 WHEN 0 THEN 'R' ");
                sql.AppendLine("                         WHEN 1 THEN 'W' ");
                sql.AppendLine("                         WHEN 2 THEN 'T' ");
                sql.AppendLine("        END AS TOUROKU_KBN ");
                sql.AppendLine("      ,M002.部門名 AS TOUROKU_BUSHO ");
                sql.AppendLine("      ,S014.タイトル AS TITLE");
                sql.AppendLine("      ,S014.掲示ID AS KEIJI_ID");
                sql.AppendLine("   FROM S014_掲示板マスタ S014 ");
                sql.AppendLine("   LEFT JOIN M002_組織マスタ M002 ");
                sql.AppendLine("     ON M002.事務所コード = S014.登録事務所コード ");
                sql.AppendLine("    AND M002.営業所コード = S014.登録営業所コード ");
                sql.AppendLine("    AND M002.部コード = S014.登録部コード ");
                sql.AppendLine("    AND M002.課コード = S014.登録課コード ");
                sql.AppendLine("    AND M002.係コード = S014.登録係コード ");
                sql.AppendLine("    AND M002.削除フラグ = 0 ");
                sql.AppendLine("  WHERE S014.削除区分 = '0' ");
                //20230508 add お知らせ通知に掲載期間の条件漏れを追加。
                sql.AppendLine("    AND @systemDate BETWEEN S014.掲示開始日 AND S014.掲示終了日  ");
                sql.AppendLine("    AND S014.T参照区分 = '1'  ");

                responseContent.Data = repository.DapperContext.QueryList<object>(sql.ToString(), new
                {
                    systemDate = DateTime.Now.ToString("yyyy/MM/dd"),
                });

                return responseContent.OK(ResponseType.Other);
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

        #region リンク件数の取得
        /// <summary>　
        /// リンク件数の取得
        /// </summary>
        /// <returns></returns>
        public async Task<WebResponseContent> GetCnt(LoginInfo LoginInfo)
        {
            string msg = string.Empty;
            today = DateTime.Now;
            WebResponseContent responseContent = new WebResponseContent();

            M015_業者ユーザマスタ M015 = await repository.M015_業者ユーザマスタ
                    .FindFirstAsync(x => x.ユーザーＩＤ == LoginInfo.userName,
                    M015a => new M015_業者ユーザマスタ()
                    {
                        ユーザーＩＤ = M015a.ユーザーＩＤ,
                        親ＩＤ = M015a.親ＩＤ,
                        ユーザー種別 = M015a.ユーザー種別,
                        業者コード = M015a.業者コード
                    });
            try
            {

                string parユーザーID = string.Empty;
                if (M015.ユーザー種別 == "2")
                {
                    parユーザーID = M015.親ＩＤ;
                }
                else
                {
                    parユーザーID = M015.ユーザーＩＤ;
                }

                String[] Data = new String[11];
                
                Data[0] = fnc_GetCnt1(M015, parユーザーID, 1); //工事未受付（緊急）
                //Data[1] = fnc_GetCnt1(M015, parユーザーID, 2); //工事未応諾（高額）
                //Data[2] = fnc_GetCnt1(M015, parユーザーID, 3); //工事未応諾
                Data[1] = fnc_GetCnt3(M015, parユーザーID, 3); //工事未応諾（高額）
                Data[2] = fnc_GetCnt3(M015, parユーザーID, 4); //工事未応諾

                Data[3] = (int.Parse(fnc_GetCnt1(M015, parユーザーID, 4)) + int.Parse(fnc_GetCnt2(M015, parユーザーID))).ToString(); //見積未提出

                Data[4] = fnc_GetCnt3(M015, parユーザーID, 5); //工事未完了

                Data[5] = fnc_GetCnt3(M015, parユーザーID, 1); //作業予定未入力(修繕)
                Data[7] = fnc_GetCnt3(M015, parユーザーID, 2); //作業完了未入力(修繕)
                Data[6] = fnc_GetCnt5(M015, parユーザーID, 1); //作業予定未入力(定期)
                Data[8] = fnc_GetCnt5(M015, parユーザーID, 2); //作業完了未入力(定期)
                Data[9] = fnc_GetCnt7(M015, parユーザーID);　//本書類未提出

                Data[10] = (int.Parse(fnc_GetCnt4(M015, parユーザーID)) + int.Parse(fnc_GetCnt6(M015, parユーザーID))).ToString(); //請求未処理

                responseContent.Data = Data;

                return responseContent.OK(ResponseType.Other);
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

        #region fnc_GetCnt1
        /// <summary>　
        /// リンク件数の取得(1-1)
        /// </summary>
        /// <returns></returns>
        private String fnc_GetCnt1(M015_業者ユーザマスタ M015, string parユーザーID, int 種類)
        {
            StringBuilder sql = new StringBuilder();

            sql.AppendLine(" ");
            sql.AppendLine(" SELECT ");
            sql.AppendLine("        COUNT(*) AS CNT ");
            sql.AppendLine("   FROM F044_業者別見積マスタ F044 ");
            sql.AppendLine("   INNER JOIN F100_依頼マスタ F100 ");
            sql.AppendLine("     ON F044.会社コード = F100.会社コード ");
            sql.AppendLine("    AND F044.工事依頼NO = F100.依頼No ");
            //20230306 add 継続区分は0の件数を取得するように修正。
            sql.AppendLine("    AND F100.継続区分 = '0' ");
            //20230306 add
            //20220902 add 緊急の判定条件を工事区分から修繕区分小に変更。
            sql.AppendLine("   INNER JOIN F001_受注マスタ F001 ");
            sql.AppendLine("     ON F044.会社コード = F001.会社コード ");
            sql.AppendLine("    AND F044.工事依頼NO = F001.工事依頼NO ");
            //20220902 add
            sql.AppendLine("   INNER JOIN M015_業者ユーザマスタ M015 ");
            sql.AppendLine("     ON F044.業者コード = M015.業者コード ");
            sql.AppendLine("    AND M015.ユーザーＩＤ = @ユーザーＩＤ ");
            sql.AppendLine("   LEFT JOIN M016_業者親ユーザセキュリティ M016 ");
            sql.AppendLine("     ON F044.業者コード = M016.業者コード ");
            sql.AppendLine("    AND F044.業者コード枝番 = M016.業者コード枝番 ");
            sql.AppendLine("    AND M016.親ユーザーＩＤ = @ユーザーＩＤ ");
            sql.AppendLine("   LEFT JOIN FT040_業者見積ヘッダマスタ FT040 ");
            sql.AppendLine("     ON F044.会社コード = FT040.会社コード ");
            sql.AppendLine("    AND F044.工事依頼NO = FT040.工事依頼No ");
            sql.AppendLine("    AND F044.工事依頼NO枝番 = FT040.工事依頼No枝番 ");  
            sql.AppendLine("    AND F044.業者コード = FT040.業者コード ");
            sql.AppendLine("    AND F044.業者コード枝番 = FT040.業者コード枝番 ");
            //20220902 add 高額判定時の金額を固定値から名称マスタの値に変更。
            //sql.AppendLine("   LEFT JOIN M018_コード名称マスタ M018 ");
            //sql.AppendLine("     ON M018.区分コード = '071' ");
            //sql.AppendLine("    AND M018.名称コード = '1' ");
            //20220902 add
            sql.AppendLine("  WHERE F044.業者コード = @業者コード ");
            sql.AppendLine("    AND F044.依頼フラグ = '1' ");
            sql.AppendLine("    AND F044.削除区分 = '0' ");
            //20221125 rep 重複見積書枝番は1件しか出ないように修正。
            sql.AppendLine("    AND F044.取引種別 + F044.工事依頼NO枝番 NOT IN ('221','231') ");
            //20221125 rep
            if (M015.ユーザー種別 != "0")
            {
                sql.AppendLine("    AND (M015.全枝参照区分 = '1' OR M016.削除区分 = '0') ");
            }
            if (種類 == 1)    
            {
                sql.AppendLine("    AND F044.受付フラグ = '0' ");
                //20220902 rep 緊急の判定条件を工事区分から修繕区分小に変更。
                //sql.AppendLine("    AND F100.工事区分 IN ('2','12','13','22','23') ");
                sql.AppendLine("    AND F001.修繕区分小 IN ('02','03','06','07') ");
                //20220902 rep
            }
            if (種類 == 4)
            {
                sql.AppendLine("    AND F044.提出フラグ = '0' ");
                //20220902 rep 緊急の判定条件を工事区分から修繕区分小に変更。
                //sql.AppendLine("    AND NOT (F044.受付フラグ = 0 AND F100.工事区分 IN ('2','12','13','22','23')) ");
                sql.AppendLine("    AND NOT (F044.受付フラグ = 0 AND F001.修繕区分小 IN ('02','03','06','07')) ");
                //20220902 rep
            }
            //if (種類 == 2)
            //{
            //    sql.AppendLine("    AND F044.発注確定フラグ = '1' ");
            //    sql.AppendLine("    AND F044.応諾フラグ = '0' ");
            //    //20220909 rep 見積未定出、工事未応諾（高額）、工事未応諾の件数取得条件を修正。
            //    //20220902 rep 緊急の判定条件を工事区分から修繕区分小に変更、および高額判定時の金額を固定値から名称マスタの値に変更。
            //    //sql.AppendLine("    AND F100.工事区分 IN ('2','12','13','22','23') ");
            //    //sql.AppendLine("    AND FT040.見積金額税抜 >= '100000' ");
            //    //sql.AppendLine("    AND F001.修繕区分小 IN ('02','03','06','07') ");
            //    //sql.AppendLine("    AND FT040.見積金額税抜 >= CONVERT(decimal, M018.名称) ");
            //    //20220902 rep
            //    sql.AppendLine("    AND F044.高額承認フラグ = '1' ");
            //    //20220909 rep
            //}
            //if (種類 == 3)
            //{
            //    sql.AppendLine("    AND F044.発注確定フラグ = '1' ");
            //    //sql.AppendLine("    AND (F044.発注確定フラグ = 1 OR (F044.受付フラグ = 0 AND F001.修繕区分小 IN ('02','03','06','07'))) ");
            //    sql.AppendLine("    AND F044.応諾フラグ = '0' ");
            //    //20220909 rep 見積未定出、工事未応諾（高額）、工事未応諾の件数取得条件を修正。
            //    //20220902 rep 緊急の判定条件を工事区分から修繕区分小に変更、および高額判定時の金額を固定値から名称マスタの値に変更。
            //    //sql.AppendLine("    AND NOT (F100.工事区分 IN ('2','12','13','22','23') AND FT040.見積金額税抜 >= '100000') ");
            //    //sql.AppendLine("    AND NOT (F001.修繕区分小 IN ('02','03','06','07') AND FT040.見積金額税抜 >= CONVERT(decimal, M018.名称)) ");
            //    //20220902 rep
            //    sql.AppendLine("    AND F044.高額承認フラグ = '0' ");
            //    //20220909 rep
            //}
            //if (種類 == 5)
            //{
            //    sql.AppendLine("    AND F044.応諾フラグ = '1' ");
            //    sql.AppendLine("    AND F044.工事完了フラグ = '0' ");
            //}

            object Cntable = repository.DapperContext.ExecuteScalar(sql.ToString(), new M015_業者ユーザマスタ()
            { ユーザーＩＤ = parユーザーID,
              業者コード = M015.業者コード,
              ユーザー種別 = M015.ユーザー種別
            });

            return Cntable.ToString();
        }
        #endregion

        #region fnc_GetCnt2
        /// <summary>　
        /// リンク件数の取得(1-2)
        /// </summary>
        /// <returns></returns>
        private String fnc_GetCnt2(M015_業者ユーザマスタ M015, string parユーザーID)
        {
            StringBuilder sql = new StringBuilder();

            sql.AppendLine(" ");
            sql.AppendLine(" SELECT ");
            sql.AppendLine("        COUNT(*) AS CNT ");
            sql.AppendLine("   FROM F045_業者別定期見積マスタ F045 ");
            sql.AppendLine("   INNER JOIN M015_業者ユーザマスタ M015 ");
            sql.AppendLine("     ON F045.業者コード = M015.業者コード ");
            sql.AppendLine("    AND M015.ユーザーＩＤ = @ユーザーＩＤ ");
            //20230306 add 継続区分は0の件数を取得するため、F011を追加。
            sql.AppendLine("   INNER JOIN F011_定期見積マスタ F011 ");
            sql.AppendLine("     ON F045.会社コード = F011.会社コード ");
            sql.AppendLine("    AND F045.契約NO = F011.契約No ");
            sql.AppendLine("    AND F045.履歴No = F011.履歴No ");
            sql.AppendLine("    AND F011.継続区分 = '0' ");
            //20230306 add
            sql.AppendLine("   LEFT JOIN M016_業者親ユーザセキュリティ M016 ");
            sql.AppendLine("     ON F045.業者コード = M016.業者コード ");
            sql.AppendLine("    AND F045.業者コード枝番 = M016.業者コード枝番 ");
            sql.AppendLine("    AND M016.親ユーザーＩＤ = @ユーザーＩＤ ");
            sql.AppendLine("  WHERE F045.業者コード = @業者コード ");
            sql.AppendLine("    AND F045.依頼フラグ = '1' ");
            sql.AppendLine("    AND F045.提出フラグ = '0' ");
            sql.AppendLine("    AND F045.削除区分 = '0' ");
            if (M015.ユーザー種別 != "0")
            {
                sql.AppendLine("    AND (M015.全枝参照区分 = '1' OR M016.削除区分 = '0') ");
            }

            object Cntable = repository.DapperContext.ExecuteScalar(sql.ToString(), new M015_業者ユーザマスタ()
            {
                ユーザーＩＤ = parユーザーID,
                業者コード = M015.業者コード,
                ユーザー種別 = M015.ユーザー種別
            });

            return Cntable.ToString();
        }
        #endregion

        #region fnc_GetCnt3
        /// <summary>　
        /// リンク件数の取得(1-3)
        /// </summary>
        /// <returns></returns>
        private String fnc_GetCnt3(M015_業者ユーザマスタ M015, string parユーザーID, int 種類)
        {
            StringBuilder sql = new StringBuilder();
            #region 古いSQL
            //sql.AppendLine(" ");
            //sql.AppendLine(" SELECT ");
            ////sql.AppendLine("        COUNT(*) AS CNT ");
            //sql.AppendLine("        COUNT(DISTINCT F044.VERSION) ");
            //sql.AppendLine("   FROM F044_業者別見積マスタ F044 ");
            //sql.AppendLine("   INNER JOIN M015_業者ユーザマスタ M015 ");
            //sql.AppendLine("     ON F044.業者コード = M015.業者コード ");
            //sql.AppendLine("    AND M015.ユーザーＩＤ = @ユーザーＩＤ ");
            //sql.AppendLine("   LEFT JOIN M016_業者親ユーザセキュリティ M016 ");
            //sql.AppendLine("     ON F044.業者コード = M016.業者コード ");
            //sql.AppendLine("    AND F044.業者コード枝番 = M016.業者コード枝番 ");
            //sql.AppendLine("    AND M016.親ユーザーＩＤ = @ユーザーＩＤ ");

            //sql.AppendLine("   INNER JOIN F001_受注マスタ F001 ");
            //sql.AppendLine("     ON F044.会社コード = F001.会社コード ");
            //sql.AppendLine("    AND F044.工事依頼NO = F001.工事依頼No ");

            //sql.AppendLine("   LEFT JOIN F004_発注明細マスタ F004 ");
            //sql.AppendLine("     ON F044.会社コード = F004.会社コード ");
            //sql.AppendLine("    AND F044.工事依頼NO = F004.工事依頼No ");
            //sql.AppendLine("    AND F044.工事依頼NO枝番 = F004.工事依頼No枝番 ");
            //sql.AppendLine("    AND F044.業者コード = F004.取引先コード ");
            //sql.AppendLine("    AND F044.業者コード枝番 = F004.取引先コード枝番 ");
            //sql.AppendLine("   LEFT JOIN F052_作業報告明細マスタ F052 ");
            //sql.AppendLine("     ON ISNULL(F004.会社コード, F044.会社コード) = F052.会社コード ");
            //sql.AppendLine("    AND ISNULL(F004.工事依頼No, F044.工事依頼No) = F052.工事依頼No ");
            //sql.AppendLine("    AND ISNULL(F004.発注サブNo, 1) = F052.発注サブNo  ");
            //sql.AppendLine("  WHERE F044.業者コード = @業者コード ");
            ////sql.AppendLine("    AND F044.応諾フラグ = '1' ");
            //sql.AppendLine("    AND F044.削除区分 = '0' ");
            #endregion
            sql.AppendLine("    with  ");
            sql.AppendLine("    FILTER_F044 as (  ");
            sql.AppendLine("    select F044.会社コード  ");
            sql.AppendLine("          ,F044.工事依頼NO  ");
            sql.AppendLine("          ,F044.工事依頼NO枝番  ");
            sql.AppendLine("          ,F044.業者コード  ");
            sql.AppendLine("          ,F044.業者コード枝番  ");
            sql.AppendLine("          ,ISNULL(F044.受付フラグ, 0) as 受付フラグ ");
            sql.AppendLine("          ,ISNULL(F044.応諾フラグ, 0) as 応諾フラグ ");
            sql.AppendLine("          ,ISNULL(F044.発注確定フラグ, 0) as 発注確定フラグ ");
            sql.AppendLine("          ,ISNULL(F044.高額承認フラグ, 0) as 高額承認フラグ  ");
            sql.AppendLine("          ,ISNULL(F044.工事完了フラグ, 0) as 工事完了フラグ  ");
            sql.AppendLine("          ,M015.全枝参照区分  ");
            sql.AppendLine("          ,M016.削除区分 as セキュリティ区分  ");
            sql.AppendLine("          ,F044.VERSION as F044_VERSION  ");
            sql.AppendLine("      from F044_業者別見積マスタ F044  ");
            sql.AppendLine("      left join M015_業者ユーザマスタ M015   ");
            sql.AppendLine("        on F044.業者コード = M015.業者コード   ");
            sql.AppendLine("       and M015.ユーザーＩＤ = @ユーザーＩＤ   ");
            sql.AppendLine("       and M015.削除区分 = '0'   ");
            sql.AppendLine("      left join M016_業者親ユーザセキュリティ M016   ");
            sql.AppendLine("        on F044.業者コード = M016.業者コード   ");
            sql.AppendLine("       and F044.業者コード枝番 = M016.業者コード枝番   ");
            sql.AppendLine("       and M016.親ユーザーＩＤ = @ユーザーＩＤ  ");
            sql.AppendLine("       and M016.削除区分 = '0'   ");
            sql.AppendLine("     where F044.業者コード = @業者コード  ");
            sql.AppendLine("       and F044.依頼フラグ = '1'  ");
            sql.AppendLine("       and F044.削除区分 = '0'   ");
            sql.AppendLine("    ),  ");
            sql.AppendLine("    ADD_F004 as (  ");
            sql.AppendLine("    select a.*  ");
            //20230127 rep F004がデータない場合、発注サブNoは1に設定するように修正。
            //sql.AppendLine("          ,case when b.会社コード is not null then b.発注サブNo else 0 end as 発注サブNo  ");
            sql.AppendLine("          ,case when b.会社コード is not null then b.発注サブNo else 1 end as 発注サブNo  ");
            //20230127 rep
            sql.AppendLine("          ,row_number() over (partition by a.工事依頼No, a.工事依頼No枝番, a.業者コード, a.業者コード枝番 order by b.発注サブNo) as F004_no  ");
            sql.AppendLine("      from FILTER_F044 a  ");
            sql.AppendLine("      left join F004_発注明細マスタ b   ");
            sql.AppendLine("        on a.会社コード = b.会社コード  ");
            sql.AppendLine("       and a.工事依頼NO = b.工事依頼No  ");
            sql.AppendLine("       and a.工事依頼NO枝番 = b.工事依頼No枝番  ");
            sql.AppendLine("       and a.業者コード = b.取引先コード  ");
            sql.AppendLine("       and a.業者コード枝番 = b.取引先コード枝番  ");
            sql.AppendLine("    ),  ");
            sql.AppendLine("    ADD_F005 as (  ");
            sql.AppendLine("    select a.*  ");
            sql.AppendLine("          ,case when c.会社コード is not null then c.検収回数 else 0 end as 検収回数  ");
            sql.AppendLine("          ,case when c.会社コード is not null then c.検収金額 else 0 end as 検収金額  ");
            sql.AppendLine("          ,case when c.会社コード is not null then c.検収回数 else 999 end as 検収ソート  ");
            sql.AppendLine("          ,ISNULL(c.ペア発注サブNO, 0) as ペア発注サブNO  ");
            sql.AppendLine("          ,ISNULL(c.ペア検収回数, 0) as ペア検収回数  ");
            //2022/09/19 add ↓
            sql.AppendLine("          ,ISDATE(ISNULL(c.検収承認日, '')) as 検収承認フラグ ");
            sql.AppendLine("          ,ISDATE(ISNULL(c.支払承認日, '')) as 支払承認フラグ ");
            //2022/09/19 add ↑
            sql.AppendLine("      from ADD_F004 a  ");
            sql.AppendLine("      left join F005_発注支払マスタ c  ");
            sql.AppendLine("        on a.会社コード = c.会社コード  ");
            sql.AppendLine("       and a.工事依頼No = c.工事依頼No  ");
            sql.AppendLine("       and a.発注サブNo = c.発注サブNo  ");
            sql.AppendLine("       and c.ペア発注サブNO = 0  ");
            sql.AppendLine("     where a.F004_no = 1  ");
            //2022/09/20 add ↓
            sql.AppendLine("       and not exists (select 'x' ");
            sql.AppendLine("                         from  F005_発注支払マスタ d ");
            sql.AppendLine("                        where a.会社コード = d.会社コード ");
            sql.AppendLine("                          and a.工事依頼No = d.工事依頼No ");
            sql.AppendLine("                          and a.発注サブNo = d.ペア発注サブNo ");
            sql.AppendLine("                          and d.ペア発注サブNO > 0 ) ");
            //2022/09/20 add ↑
            sql.AppendLine("     union  ");
            sql.AppendLine("    select a.*  ");
            sql.AppendLine("          ,case when c.会社コード is not null then c.検収回数 else 0 end as 検収回数  ");
            sql.AppendLine("          ,sum(ISNULL(c.検収金額, 0)) over (partition by a.工事依頼No, a.工事依頼No枝番, a.業者コード, a.業者コード枝番, c.ペア発注サブNO, c.ペア検収回数) as 検収金額  ");
            sql.AppendLine("          ,case when c.会社コード is not null then c.検収回数 else 999 end as 検収ソート  ");
            sql.AppendLine("          ,ISNULL(c.ペア発注サブNO, 0) as ペア発注サブNO  ");
            sql.AppendLine("          ,ISNULL(c.ペア検収回数, 0) as ペア検収回数  ");
            //2022/09/19 add ↓
            sql.AppendLine("          ,ISDATE(ISNULL(c.検収承認日, '')) as 検収承認フラグ ");
            sql.AppendLine("          ,ISDATE(ISNULL(c.支払承認日, '')) as 支払承認フラグ ");
            //2022/09/19 add ↑
            sql.AppendLine("      from ADD_F004 a  ");
            sql.AppendLine("      inner join F005_発注支払マスタ c  ");
            sql.AppendLine("        on a.会社コード = c.会社コード  ");
            sql.AppendLine("       and a.工事依頼No = c.工事依頼No  ");
            //2022/09/20 mod ↓
            //sql.AppendLine("       and a.発注サブNo = c.発注サブNo ");
            sql.AppendLine("       and a.発注サブNo = c.ペア発注サブNo ");
            //2022/09/20 mod ↑
            sql.AppendLine("       and c.ペア発注サブNO > 0  ");
            sql.AppendLine("     where a.F004_no = 1  ");
            //20221114 add 全額で検収申請しない場合、工事一覧に申請した行と申請しない行が出るように修正。
            sql.AppendLine("     union ");
            sql.AppendLine("    select a.* ");
            sql.AppendLine("          ,max(c.検収回数 + 1) over (partition by a.工事依頼No, a.工事依頼No枝番, a.業者コード, a.業者コード枝番, c.発注サブNo) as 検収回数 ");
            sql.AppendLine("          ,0 as 検収金額 ");
            sql.AppendLine("          ,999 検収ソート  ");
            sql.AppendLine("          ,c.ペア発注サブNO as ペア発注サブNO ");
            sql.AppendLine("          ,case when c.ペア発注サブNO > 0 then c.ペア検収回数 + 1 else 0 end as ペア検収回数  ");
            sql.AppendLine("          ,'' as 検収承認フラグ ");
            sql.AppendLine("          ,'' as 支払承認フラグ ");
            sql.AppendLine("      from ADD_F004 a ");
            sql.AppendLine("      inner join F005_発注支払マスタ c ");
            sql.AppendLine("        on a.会社コード = c.会社コード ");
            sql.AppendLine("       and a.工事依頼No = c.工事依頼No ");
            sql.AppendLine("       and a.工事依頼No枝番 = c.工事依頼No枝番 ");
            sql.AppendLine("       and a.発注サブNo = c.発注サブNo ");
            sql.AppendLine("       and c.検収回数 > 0 ");
            sql.AppendLine("       and c.検収承認日 <> '' ");
            sql.AppendLine("       and not exists (select 'x' ");
            sql.AppendLine("                         from F005_発注支払マスタ d ");
            sql.AppendLine("                        where d.会社コード = c.会社コード ");
            sql.AppendLine("                          and d.工事依頼No = c.工事依頼No ");
            sql.AppendLine("                          and d.工事依頼No枝番 = c.工事依頼No枝番 ");
            sql.AppendLine("                          and d.発注サブNo = c.発注サブNo ");
            sql.AppendLine("                          and d.検収回数 = c.検収回数 + 1) ");
            sql.AppendLine("     where a.F004_no = 1 ");
            //20221114 add
            sql.AppendLine("    ),  ");
            sql.AppendLine("    ADD_F052 as (  ");
            sql.AppendLine("    select a.*  ");
            sql.AppendLine("          ,b.会社コード as F052_会社コード  ");
            sql.AppendLine("          ,case when b.会社コード is not null then b.予定日開始 else '' end as 予定日開始  ");
            sql.AppendLine("          ,case when b.会社コード is not null then b.予定日終了 else '' end as 予定日終了  ");
            sql.AppendLine("          ,case when b.会社コード is not null then b.実施日開始 else '' end as 実施日開始  ");
            sql.AppendLine("          ,case when b.会社コード is not null then b.実施日終了 else '' end as 実施日終了  ");
            sql.AppendLine("          ,case when b.会社コード is not null then b.T完了報告フラグ else '0' end as T完了報告フラグ  ");
            sql.AppendLine("          ,case when b.会社コード is not null then b.T作業報告日 else '' end as T作業報告日  ");
            sql.AppendLine("          ,case when b.会社コード is not null then b.W作業結果承認日 else '' end as W作業結果承認日  ");
            sql.AppendLine("          ,case when b.会社コード is not null then b.[進捗コード（発注）] else '' end as [進捗コード（発注）]  ");
            sql.AppendLine("          ,case when b.会社コード is not null then b.R作業予定確認フラグ else '0' end as R作業予定確認フラグ  ");
            sql.AppendLine("          ,case when b.会社コード is not null then b.W作業予定確認フラグ else '0' end as W作業予定確認フラグ  ");
            //20220920 add 差戻を表示する条件を追加。
            sql.AppendLine("          ,case when b.会社コード is not null then b.R作業結果確認フラグ else '0' end as R作業結果確認フラグ ");
            sql.AppendLine("          ,case when b.会社コード is not null then b.W作業結果確認フラグ else '0' end as W作業結果確認フラグ ");
            //20220920 add
            sql.AppendLine("          ,sum(a.検収金額) over (partition by a.工事依頼NO, a.発注サブNo) as 検収金額計  ");
            sql.AppendLine("          ,max(a.検収回数) over (partition by a.工事依頼NO, a.発注サブNo) as 最大検収回数  ");
            //2022/09/19 add ↓
            sql.AppendLine("          ,min(a.検収承認フラグ) over (partition by a.工事依頼NO, a.発注サブNo) as 全件検収フラグ  ");
            sql.AppendLine("          ,min(a.支払承認フラグ) over (partition by a.工事依頼NO, a.発注サブNo) as 全件支払フラグ ");
            sql.AppendLine("          ,min(ISNULL(b.T完了報告フラグ, 0)) over (partition by a.工事依頼NO, a.発注サブNo) as 全件完了フラグ ");
            //2022/09/19 add ↑
            sql.AppendLine("      from ADD_F005 a  ");
            sql.AppendLine("      left join F052_作業報告明細マスタ b  ");
            sql.AppendLine("        on a.会社コード = b.会社コード  ");
            sql.AppendLine("       and a.工事依頼NO = b.工事依頼NO  ");
            sql.AppendLine("       and a.発注サブNo = b.発注サブNo   ");
            sql.AppendLine("       and a.検収回数 = b.検収回数  ");
            sql.AppendLine("       and (b.ペア発注サブNO = 0 or b.ペア発注サブNO = b.発注サブNO)  ");
            sql.AppendLine("    ),  ");
            sql.AppendLine("    ADD_F001_FT040 as (  ");
            sql.AppendLine("    select a.*  ");
            sql.AppendLine("          ,b.修繕区分小  ");
            sql.AppendLine("          ,ISNULL(c.見積金額税込, 0) as 見積金額税込  ");
            //20230711 add 工事一覧の明細の取得条件修正した分の取得件数修正。
            sql.AppendLine("          ,d.[進捗コード（発注）] as 進捗コード_F102 ");
            sql.AppendLine("      from ADD_F052 a  ");
            sql.AppendLine("      join F001_受注マスタ b  ");
            sql.AppendLine("        on a.会社コード = b.会社コード   ");
            sql.AppendLine("       and a.工事依頼NO = b.工事依頼No   ");
            sql.AppendLine("       and b.削除区分 <> 1   ");
            //20230306 add 継続区分は0の件数を取得するように修正。
            sql.AppendLine("       AND b.継続区分 = '0' ");
            //20230306 add
            sql.AppendLine("      left join FT040_業者見積ヘッダマスタ c  ");
            sql.AppendLine("        on a.会社コード = c.会社コード   ");
            sql.AppendLine("       and a.工事依頼NO = c.工事依頼No   ");
            sql.AppendLine("       and a.工事依頼NO枝番 = c.工事依頼No枝番   ");
            sql.AppendLine("       and a.業者コード = c.業者コード   ");
            sql.AppendLine("       and a.業者コード枝番 = c.業者コード枝番   ");
            //20230711 add 工事一覧の明細の取得条件修正した分の取得件数修正。
            sql.AppendLine("      left join F102_修繕進捗管理ファイル d    ");
            sql.AppendLine("        on b.工事依頼NO = d.依頼NO    ");
            sql.AppendLine("    )  ");
            sql.AppendLine("    SELECT COUNT(DISTINCT main.F044_VERSION) ");
            sql.AppendLine("      FROM ADD_F001_FT040 main  ");
            //20230711 rep 工事一覧の明細の取得条件修正した分の取得件数修正。
            //sql.AppendLine("     WHERE (main.発注確定フラグ = 1 OR (main.受付フラグ = 1 and main.修繕区分小 IN ('02','06','03','07'))) ");
            sql.AppendLine("     WHERE (main.発注確定フラグ = 1 OR (main.修繕区分小 IN ('02','06','03','07') and main.受付フラグ = 1 and main.[進捗コード_F102] < '490')) ");
            //20221114 add 全額で検収申請しない場合、工事一覧に申請した行と申請しない行が出るように修正。
            //sql.AppendLine("       AND (main.検収回数 > 0 OR main.最大検収回数 = 0 OR main.見積金額税込 <> main.検収金額計) ");
            sql.AppendLine("       AND((main.検収ソート < 999 and main.検収回数 > 0) OR main.最大検収回数 = 0 OR main.見積金額税込<> main.検収金額計) ");
            //20221114 add
            if (M015.ユーザー種別 != "0")
            {
                sql.AppendLine("       AND (main.全枝参照区分 = '1' OR main.セキュリティ区分 = '0') ");
            }
            if (種類 == 1)
            {
                sql.AppendLine("       AND main.T完了報告フラグ = '0' ");
                sql.AppendLine("       AND main.予定日開始 = '' ");
            }
            if (種類 == 2)
            {
                //sql.AppendLine("       AND main.[進捗コード（発注）] > '520'  ");  //2022/09/18 mod
                sql.AppendLine("       AND main.[進捗コード（発注）] >= '520'  ");
                //sql.AppendLine("       AND main.T完了報告フラグ = '0' ");
                sql.AppendLine("       AND (main.T完了報告フラグ = '0' OR main.R作業結果確認フラグ = '3' OR main.W作業結果確認フラグ = '3') ");
                //sql.AppendLine("       AND (ISDATE(main.予定日開始) = 1 AND main.予定日開始 < '" + DateTime.Now.ToString("yyyy/MM/dd") + "') ");    //2022/09/18 mod
                sql.AppendLine("       AND (ISDATE(main.予定日開始) = 1 AND main.予定日開始 <= '" + today.ToString("yyyy/MM/dd") + "') ");
            }
            if (種類 == 3)
            {
                sql.AppendLine("       AND main.発注確定フラグ = '1' ");
                sql.AppendLine("       AND main.応諾フラグ = '0' ");
                sql.AppendLine("       AND main.高額承認フラグ = '1' ");
            }
            if (種類 == 4)
            {
                sql.AppendLine("       AND main.発注確定フラグ = '1' ");
                sql.AppendLine("       AND main.応諾フラグ = '0' ");
                sql.AppendLine("       AND main.高額承認フラグ = '0' ");
            }
            if (種類 == 5)
            {
                //2022/09/19 mod ↓
                //sql.AppendLine("       AND main.T完了報告フラグ = '0' ");
                sql.AppendLine("       AND (main.全件完了フラグ = 0 OR main.全件検収フラグ = 0) ");
                //2022/09/19 mod ↑
            }
            object Cntable = repository.DapperContext.ExecuteScalar(sql.ToString(), new M015_業者ユーザマスタ()
            {
                ユーザーＩＤ = parユーザーID,
                業者コード = M015.業者コード,
                ユーザー種別 = M015.ユーザー種別
            });

            return Cntable.ToString();
        }
        #endregion

        #region fnc_GetCnt4
        /// <summary>　
        /// リンク件数の取得(1-4)
        /// </summary>
        /// <returns></returns>
        private String fnc_GetCnt4(M015_業者ユーザマスタ M015, string parユーザーID)
        {
            StringBuilder sql = new StringBuilder();

            sql.AppendLine(" ");
            sql.AppendLine(" SELECT ");
            sql.AppendLine("        COUNT(*) AS CNT ");
            sql.AppendLine("   FROM F044_業者別見積マスタ F044 ");
            sql.AppendLine("   INNER JOIN M015_業者ユーザマスタ M015 ");
            sql.AppendLine("     ON F044.業者コード = M015.業者コード ");
            sql.AppendLine("    AND M015.ユーザーＩＤ = @ユーザーＩＤ ");
            //20230306 add 継続区分は0の件数を取得するため、F001を追加。
            sql.AppendLine("   INNER JOIN F001_受注マスタ F001 ");
            sql.AppendLine("     ON F044.会社コード = F001.会社コード ");
            sql.AppendLine("    AND F044.工事依頼NO = F001.工事依頼No ");
            sql.AppendLine("    AND F001.継続区分 = '0' ");
            //20230306 add
            sql.AppendLine("   LEFT JOIN M016_業者親ユーザセキュリティ M016 ");
            sql.AppendLine("     ON F044.業者コード = M016.業者コード ");
            sql.AppendLine("    AND F044.業者コード枝番 = M016.業者コード枝番 ");
            sql.AppendLine("    AND M016.親ユーザーＩＤ = @ユーザーＩＤ ");
            sql.AppendLine("   LEFT JOIN F004_発注明細マスタ F004 ");
            sql.AppendLine("     ON F044.会社コード = F004.会社コード ");
            sql.AppendLine("    AND F044.工事依頼NO = F004.工事依頼No ");
            sql.AppendLine("    AND F044.工事依頼NO枝番 = F004.工事依頼No枝番 ");
            sql.AppendLine("    AND F044.業者コード = F004.取引先コード ");
            sql.AppendLine("    AND F044.業者コード枝番 = F004.取引先コード枝番 ");
            sql.AppendLine("   LEFT JOIN F005_発注支払マスタ F005 ");
            sql.AppendLine("     ON F004.会社コード = F005.会社コード ");
            sql.AppendLine("    AND F004.工事依頼No  = F005.工事依頼No ");
            sql.AppendLine("    AND F004.発注サブNo  = F005.発注サブNo  ");
            sql.AppendLine("  WHERE F044.業者コード = @業者コード ");
            //20230216 rep 修繕の請求未処理の件数取得条件を修正
            //sql.AppendLine("    AND F044.応諾フラグ = '1' ");
            sql.AppendLine("    AND F005.検収承認日 <> '' ");
            //20230216 rep 
            sql.AppendLine("    AND F044.削除区分 = '0' ");
            sql.AppendLine("    AND F005.請求合意区分 = '0' ");
            if (M015.ユーザー種別 != "0")
            {
                sql.AppendLine("    AND (M015.全枝参照区分 = '1' OR M016.削除区分 = '0') ");
            }

            object Cntable = repository.DapperContext.ExecuteScalar(sql.ToString(), new M015_業者ユーザマスタ()
            {
                ユーザーＩＤ = parユーザーID,
                業者コード = M015.業者コード,
                ユーザー種別 = M015.ユーザー種別
            });

            return Cntable.ToString();
        }
        #endregion

        #region fnc_GetCnt5
        /// <summary>　
        /// リンク件数の取得(1-5)
        /// </summary>
        /// <returns></returns>
        private String fnc_GetCnt5(M015_業者ユーザマスタ M015, string parユーザーID, int 種類)
        {
            StringBuilder sql = new StringBuilder();

            sql.AppendLine(" ");
            sql.AppendLine(" SELECT ");
            sql.AppendLine("        COUNT(*) AS CNT ");
            //20221004 rep 定期の作業予定未入力、作業完了未入力のカウント方法を修正。
            //sql.AppendLine("   FROM F045_業者別定期見積マスタ F045 ");
            //sql.AppendLine("   INNER JOIN M015_業者ユーザマスタ M015 ");
            //sql.AppendLine("     ON F045.業者コード = M015.業者コード ");
            //sql.AppendLine("    AND M015.ユーザーＩＤ = @ユーザーＩＤ ");
            //sql.AppendLine("   LEFT JOIN M016_業者親ユーザセキュリティ M016 ");
            //sql.AppendLine("     ON F045.業者コード = M016.業者コード ");
            //sql.AppendLine("    AND F045.業者コード枝番 = M016.業者コード枝番 ");
            //sql.AppendLine("    AND M016.親ユーザーＩＤ = @ユーザーＩＤ ");
            //sql.AppendLine("   LEFT JOIN F025_契約原価マスタ F025 ");
            //sql.AppendLine("     ON F045.会社コード = F025.会社コード ");
            //sql.AppendLine("    AND F045.契約NO = F025.契約NO ");
            //sql.AppendLine("    AND F045.履歴No = F025.履歴No ");
            //sql.AppendLine("   LEFT JOIN F052_作業報告明細マスタ F052 ");
            //sql.AppendLine("     ON F025.会社コード = F052.会社コード ");
            //sql.AppendLine("    AND F025.契約NO  = F052.契約NO ");
            //sql.AppendLine("    AND F025.履歴NO  = F052.履歴NO  ");
            //sql.AppendLine("    AND F025.明細NO = F052.明細NO ");
            //sql.AppendLine("    AND F025.契約年月 = F052.契約年月 ");
            //sql.AppendLine("  WHERE F045.業者コード = @業者コード ");
            //sql.AppendLine("    AND F045.提出フラグ = '1' ");
            //sql.AppendLine("    AND F045.削除区分 = '0' ");
            sql.AppendLine("   FROM F052_作業報告明細マスタ F052 ");
            //20221227 add F050とF025を内部結合するように修正。
            sql.AppendLine("   INNER JOIN F050_作業報告マスタ F050 ");
            sql.AppendLine("     ON F052.会社コード = F050.会社コード ");
            sql.AppendLine("    AND F052.契約NO = F050.契約NO ");
            sql.AppendLine("    AND F052.履歴No = F050.履歴No ");
            sql.AppendLine("    AND F052.明細NO = F050.明細NO ");
            //20230425 rep 結合条件を詳細仕様に合わせて修正。
            //sql.AppendLine("    AND F052.契約年月 = F050.契約年月 ");
            sql.AppendLine("    AND F052.実施予定年月 = F050.契約年月 ");
            sql.AppendLine("   INNER JOIN F025_契約原価マスタ F025 ");
            sql.AppendLine("     ON F052.会社コード = F025.会社コード ");
            sql.AppendLine("    AND F052.契約NO = F025.契約NO ");
            sql.AppendLine("    AND F052.履歴No = F025.履歴No ");
            sql.AppendLine("    AND F052.明細NO = F025.明細NO ");
            //20230425 rep 結合条件を詳細仕様に合わせて修正。
            //sql.AppendLine("    AND F052.契約年月 = F025.契約年月 ");
            sql.AppendLine("    AND F052.実施予定年月 = F025.契約年月 ");
            //20221227 add
            sql.AppendLine("   LEFT JOIN F027_定期契約ヘッダ F027 ");
            sql.AppendLine("     ON F052.会社コード = F027.会社コード ");
            sql.AppendLine("    AND F052.契約NO = F027.契約NO ");
            sql.AppendLine("    AND F052.履歴No = F027.履歴No ");
            sql.AppendLine("   INNER JOIN M015_業者ユーザマスタ M015 ");
            sql.AppendLine("     ON F027.取引先コード = M015.業者コード ");
            sql.AppendLine("    AND M015.ユーザーＩＤ = @ユーザーＩＤ ");
            sql.AppendLine("   LEFT JOIN M016_業者親ユーザセキュリティ M016 ");
            sql.AppendLine("     ON F027.取引先コード = M016.業者コード ");
            sql.AppendLine("    AND F027.取引先コード枝番 = M016.業者コード枝番 ");
            sql.AppendLine("    AND M016.親ユーザーＩＤ = @ユーザーＩＤ ");
            //20230626 add 定期の作業予定未入力、作業完了未入力の件数取得条件を修正
            sql.AppendLine("   LEFT JOIN F092_経理年月管理ファイル F092 ");
            sql.AppendLine("     ON F052.会社コード = F092.会社コード ");
            sql.AppendLine("    AND F092.確定フラグ = 0 ");
            sql.AppendLine("  WHERE F027.取引先コード = @業者コード ");
            //20221004 rep
            //20221207 add 定期の作業予定未入力、作業完了未入力の件数取得条件を修正
            sql.AppendLine("    AND F052.削除区分 = '0' ");
            //20221207 add
            //20221227 add 定期の作業予定未入力、作業完了未入力の件数取得条件を修正。
            sql.AppendLine("    AND F050.削除区分 = '0' ");
            sql.AppendLine("    AND ((F050.実施予定作業回数 = '0' AND F025.作業回数 = '0' AND F025.[予定発注金額（税抜）] > '0') ");
            sql.AppendLine("           OR (F050.実施予定作業回数 >= '1' AND F052.回目 >= '1')) ");
            //20221227 add
            if (M015.ユーザー種別 != "0")
            {
                sql.AppendLine("    AND (M015.全枝参照区分 = '1' OR M016.削除区分 = '0') ");
            }
            if (種類 == 1)
            {
                //20230612 rep 作業予定未入力(定期)の件数取得SQLを修正
                //開始年度取得
                String 年度開始 = string.Empty;
                String 年度初月 = "04";
                if (today.Month <= 3)
                {
                    年度開始 = (today.AddYears(-1).Year).ToString().Substring(0, 4);
                }
                else
                {
                    年度開始 = (today.Year).ToString().Substring(0, 4);
                }
                年度開始 = 年度開始 + "/" + 年度初月;

                //sql.AppendLine("    AND F052.予定日開始 = '' ");
                //20230612 rep 作業予定未入力(定期)の件数取得SQLを修正
                //sql.AppendLine("    AND F052.実施予定年月 >= '" + today.ToString("yyyy/MM") + "' ");
                sql.AppendLine("    AND F052.実施予定年月 >= '" + 年度開始 + "' ");
                sql.AppendLine("    AND F052.実施予定年月 <= '" + today.AddMonths(1).ToString("yyyy/MM") + "' ");
                //20230612 rep 作業予定未入力(定期)の件数取得SQLを修正
                //sql.AppendLine("    AND F052.[進捗コード（発注）] = '510' ");
                sql.AppendLine("    AND F052.[進捗コード（発注）] in　('510', '516', '518') ");

            }
            if (種類 == 2)
            {
                //20230626 rep 定期の作業予定未入力、作業完了未入力の件数取得条件を修正
                //sql.AppendLine("    AND (ISDATE(F052.予定日開始) = 1 AND F052.予定日開始 <= '" + today.ToString("yyyy/MM/dd") + "') ");
                //sql.AppendLine("    AND F052.実施日開始 = '' ");
                //sql.AppendLine("    AND F052.T完了報告フラグ = '0' ");
                //sql.AppendLine("    AND F052.実施予定年月 >= '" + today.ToString("yyyy/MM") + "' ");
                //sql.AppendLine("    AND F052.実施予定年月 <= '" + today.AddYears(1).ToString("yyyy/MM") + "' ");
                //sql.AppendLine("    AND F052.[進捗コード（発注）] = '530' ");
                sql.AppendLine("    AND F052.実施予定年月 = F092.経理年月 ");
                sql.AppendLine("    AND F052.[進捗コード（発注）]　in　('530', '536', '538') ");

            }

            object Cntable = repository.DapperContext.ExecuteScalar(sql.ToString(), new M015_業者ユーザマスタ()
            {
                ユーザーＩＤ = parユーザーID,
                業者コード = M015.業者コード,
                ユーザー種別 = M015.ユーザー種別
            });

            return Cntable.ToString();
        }
        #endregion

        #region fnc_GetCnt6
        /// <summary>　
        /// リンク件数の取得(1-6)
        /// </summary>
        /// <returns></returns>
        private String fnc_GetCnt6(M015_業者ユーザマスタ M015, string parユーザーID)
        {
            StringBuilder sql = new StringBuilder();

            sql.AppendLine(" ");
            sql.AppendLine(" SELECT ");
            //sql.AppendLine("        COUNT(*) AS CNT ");
            //sql.AppendLine("   FROM F045_業者別定期見積マスタ F045 ");
            //sql.AppendLine("   INNER JOIN M015_業者ユーザマスタ M015 ");
            //sql.AppendLine("     ON F045.業者コード = M015.業者コード ");
            //sql.AppendLine("    AND M015.ユーザーＩＤ = @ユーザーＩＤ ");
            //sql.AppendLine("   LEFT JOIN M016_業者親ユーザセキュリティ M016 ");
            //sql.AppendLine("     ON F045.業者コード = M016.業者コード ");
            //sql.AppendLine("    AND F045.業者コード枝番 = M016.業者コード枝番 ");
            //sql.AppendLine("    AND M016.親ユーザーＩＤ = @ユーザーＩＤ ");
            //sql.AppendLine("   LEFT JOIN F025_契約原価マスタ F025 ");
            //sql.AppendLine("     ON F045.会社コード = F025.会社コード ");
            //sql.AppendLine("    AND F045.契約NO = F025.契約NO ");
            //sql.AppendLine("    AND F045.履歴No = F025.履歴No ");
            //sql.AppendLine("  WHERE F045.業者コード = @業者コード ");
            //sql.AppendLine("    AND F045.提出フラグ = '1' ");
            //sql.AppendLine("    AND F045.削除区分 = '0' ");
            //sql.AppendLine("    AND F025.請求合意区分 = '0' ");
            sql.AppendLine("        COUNT(F025.契約NO) AS CNT ");
            sql.AppendLine("   FROM F025_契約原価マスタ F025 ");
            sql.AppendLine("   LEFT JOIN F027_定期契約ヘッダ F027 ");
            sql.AppendLine("     ON F027.会社コード = F025.会社コード ");
            sql.AppendLine("    AND F027.契約NO = F025.契約NO ");
            sql.AppendLine("    AND F027.履歴No = F025.履歴No ");
            sql.AppendLine("   INNER JOIN M015_業者ユーザマスタ M015 ");
            sql.AppendLine("     ON F027.取引先コード = M015.業者コード ");
            sql.AppendLine("    AND M015.ユーザーＩＤ = @ユーザーＩＤ ");
            sql.AppendLine("   LEFT JOIN M016_業者親ユーザセキュリティ M016 ");
            sql.AppendLine("     ON F027.取引先コード = M016.業者コード ");
            sql.AppendLine("    AND F027.取引先コード枝番 = M016.業者コード枝番 ");
            sql.AppendLine("    AND M016.親ユーザーＩＤ = @ユーザーＩＤ ");
            sql.AppendLine("  WHERE F027.取引先コード = @業者コード ");
            sql.AppendLine("    AND F025.請求合意区分 = '0' ");
            sql.AppendLine("    AND F025.検収区分 = '1' ");
            if (M015.ユーザー種別 != "0")
            {
                sql.AppendLine("    AND (M015.全枝参照区分 = '1' OR M016.削除区分 = '0') ");
            }

            object Cntable = repository.DapperContext.ExecuteScalar(sql.ToString(), new M015_業者ユーザマスタ()
            {
                ユーザーＩＤ = parユーザーID,
                業者コード = M015.業者コード,
                ユーザー種別 = M015.ユーザー種別
            });

            return Cntable.ToString();
        }
        #endregion

        #region fnc_GetCnt7
        /// <summary>　
        /// リンク件数の取得(1-7)
        /// </summary>
        /// <returns></returns>
        private String fnc_GetCnt7(M015_業者ユーザマスタ M015, string parユーザーID)
        {
            StringBuilder sql = new StringBuilder();

            sql.AppendLine(" ");
            sql.AppendLine(" SELECT ");
            sql.AppendLine("        COUNT(*) AS CNT ");
            //20221207 rep 本書類未提出の件数取得条件を修正
            //sql.AppendLine("   FROM F045_業者別定期見積マスタ F045 ");
            //sql.AppendLine("   INNER JOIN M015_業者ユーザマスタ M015 ");
            //sql.AppendLine("     ON F045.業者コード = M015.業者コード ");
            //sql.AppendLine("    AND M015.ユーザーＩＤ = @ユーザーＩＤ ");
            //sql.AppendLine("   LEFT JOIN M016_業者親ユーザセキュリティ M016 ");
            //sql.AppendLine("     ON F045.業者コード = M016.業者コード ");
            //sql.AppendLine("    AND F045.業者コード枝番 = M016.業者コード枝番 ");
            //sql.AppendLine("    AND M016.親ユーザーＩＤ = @ユーザーＩＤ ");
            //sql.AppendLine("   LEFT JOIN F050_作業報告マスタ F050 ");
            //sql.AppendLine("     ON F045.会社コード = F050.会社コード ");
            //sql.AppendLine("    AND F045.契約NO = F050.契約NO ");
            //sql.AppendLine("    AND F045.履歴No = F050.履歴No ");
            //sql.AppendLine("  WHERE F045.業者コード = @業者コード ");
            //sql.AppendLine("    AND F045.確定フラグ = '1' ");
            //sql.AppendLine("    AND F045.削除区分 = '0' ");
            //sql.AppendLine("    AND F050.点検書類待ち = '1' ");
            //sql.AppendLine("    AND F050.点検書類待ち解消日 = '' ");
            //sql.AppendLine("    AND F050.完了報告書不備 = '1' ");
            //sql.AppendLine("    AND F050.完了報告書不備解消日 = '' ");
            //20230626 rep 定期の本書類未提出の件数取得条件を修正
            sql.AppendLine("   FROM F052_作業報告明細マスタ F052 ");
            //20221227 rep F052を内部結合するように修正。
            //sql.AppendLine("   INNER JOIN F025_契約原価マスタ F025 ");
            //sql.AppendLine("     ON F050.会社コード = F025.会社コード ");
            //sql.AppendLine("    AND F050.契約NO = F025.契約NO ");
            //sql.AppendLine("    AND F050.履歴No = F025.履歴No ");
            //sql.AppendLine("    AND F050.明細NO = F025.明細NO ");
            //sql.AppendLine("    AND F050.契約年月 = F025.契約年月 ");
            sql.AppendLine("   INNER JOIN F050_作業報告マスタ F050 ");
            sql.AppendLine("     ON F052.会社コード = F050.会社コード ");
            sql.AppendLine("    AND F052.契約NO = F050.契約NO ");
            sql.AppendLine("    AND F052.履歴No = F050.履歴No ");
            sql.AppendLine("    AND F052.明細NO = F050.明細NO ");
            //20230425 rep 結合条件を詳細仕様に合わせて修正。
            //sql.AppendLine("    AND F050.契約年月 = F052.契約年月 ");
            sql.AppendLine("    AND F052.実施予定年月 = F050.契約年月 ");
            sql.AppendLine("   INNER JOIN F025_契約原価マスタ F025 ");
            sql.AppendLine("     ON F052.会社コード = F025.会社コード ");
            sql.AppendLine("    AND F052.契約NO = F025.契約NO ");
            sql.AppendLine("    AND F052.履歴No = F025.履歴No ");
            sql.AppendLine("    AND F052.明細NO = F025.明細NO ");
            //20230425 rep 結合条件を詳細仕様に合わせて修正。
            //sql.AppendLine("    AND F052.契約年月 = F025.契約年月 ");
            sql.AppendLine("    AND F052.実施予定年月 = F025.契約年月 ");
            //20221227 rep
            sql.AppendLine("   LEFT JOIN F027_定期契約ヘッダ F027 ");
            sql.AppendLine("     ON F025.会社コード = F027.会社コード ");
            sql.AppendLine("    AND F025.契約NO = F027.契約NO ");
            sql.AppendLine("    AND F025.履歴No = F027.履歴No ");
            sql.AppendLine("   INNER JOIN M015_業者ユーザマスタ M015 ");
            sql.AppendLine("     ON F027.取引先コード = M015.業者コード ");
            sql.AppendLine("    AND M015.ユーザーＩＤ = @ユーザーＩＤ ");
            sql.AppendLine("   LEFT JOIN M016_業者親ユーザセキュリティ M016 ");
            sql.AppendLine("     ON F027.取引先コード = M016.業者コード ");
            sql.AppendLine("    AND F027.取引先コード枝番 = M016.業者コード枝番 ");
            sql.AppendLine("    AND M016.親ユーザーＩＤ = @ユーザーＩＤ ");
            sql.AppendLine("   LEFT JOIN F092_経理年月管理ファイル F092 ");
            sql.AppendLine("     ON F052.会社コード = F092.会社コード ");
            sql.AppendLine("    AND F092.確定フラグ = '0'");
            sql.AppendLine("  WHERE F027.取引先コード = @業者コード ");
            sql.AppendLine("    AND F050.削除区分 = '0' ");
            //20221227 add 本書類未提出の件数取得条件を追加。
            sql.AppendLine("    AND F052.削除区分 = '0' ");
            sql.AppendLine("    AND ((F050.実施予定作業回数 = '0' AND F025.作業回数 = '0' AND F025.[予定発注金額（税抜）] > '0') ");
            sql.AppendLine("           OR (F050.実施予定作業回数 >= '1' AND F052.回目 >= '1')) ");
            //20221227 add
            sql.AppendLine("    AND (((F050.完了報告書不備 = '1' AND F050.完了報告書不備解消日 = '')");
            sql.AppendLine("           OR (F050.請求書不備 = '1' AND F050.請求書不備解消日 = '')) ");
            sql.AppendLine("         AND F025.検収区分 = '1') ");
            //20221207 rep
            if (M015.ユーザー種別 != "0")
            {
                sql.AppendLine("    AND (M015.全枝参照区分 = '1' OR M016.削除区分 = '0') ");
            }
                sql.AppendLine("    AND F052.実施予定年月 = F092.経理年月 ");
            
            object Cntable = repository.DapperContext.ExecuteScalar(sql.ToString(), new M015_業者ユーザマスタ()
            {
                ユーザーＩＤ = parユーザーID,
                業者コード = M015.業者コード,
                ユーザー種別 = M015.ユーザー種別
            });

            return Cntable.ToString();
        }
        #endregion
    }
}
