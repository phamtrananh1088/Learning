using System;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations.Schema;
using WinMacOs.Utility.SystemModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Utility.TableModels
{
    [Table("S018_ドキュメント定義")]
    [EntityAttribute(TableName = "S018_ドキュメント定義")]
    public class S018_ドキュメント定義 : BaseEntity
    {
        public string 会社コード { get; set; } = String.Empty;
        public string 帳票区分 { get; set; } = String.Empty;
        public string 帳票種類 { get; set; } = String.Empty;
        public int? 出力順 { get; set; } = 0;
        public int? 発注稟議添付 { get; set; } = 0;
        public string ルートパス { get; set; } = String.Empty;
        public string 帳票名称 { get; set; } = String.Empty;
        public string 帳票名称W { get; set; } = String.Empty;
        public string 帳票名称T { get; set; } = String.Empty;
        public int? Ｗ参照フラグ { get; set; } = 0;
        public int? Ｔ参照フラグ { get; set; } = 0;
        public int? 参照パターン { get; set; } = 0;
        public string 更新可能ステータス本体 { get; set; } = String.Empty;
        public string 更新可能ステータス発注 { get; set; } = String.Empty;
        public string 更新可能ステータス受注 { get; set; } = String.Empty;
        public int? 手動発生フラグ { get; set; } = 0;
        public int? 枝番入力フラグ { get; set; } = 0;
        public int? 取引先入力フラグ { get; set; } = 0;
        public int? 日付入力フラグ { get; set; } = 0;
        public int? 枝番表示フラグ { get; set; } = 0;
        public int? 取引先名表示フラグ { get; set; } = 0;
        public int? 年月表示フラグ { get; set; } = 0;
        public int? 作業名表示フラグ { get; set; } = 0;
        public int? 備考表示フラグ { get; set; } = 0;
        public int? 削除区分 { get; set; } = 0;

        [DatabaseGenerated(DatabaseGeneratedOption.Computed)]
        public byte[] VERSION { get; set; }
        public string 稟議帳票名称 { get; set; } = String.Empty;
        public int? Ｗ手動発生フラグ { get; set; } = 0;
        public int? Ｔ手動発生フラグ { get; set; } = 0;
    }

}
