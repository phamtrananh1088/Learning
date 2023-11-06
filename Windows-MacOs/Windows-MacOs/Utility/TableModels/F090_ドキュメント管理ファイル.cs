using System;
using System.ComponentModel.DataAnnotations.Schema;
using WinMacOs.Utility.SystemModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Utility.TableModels
{
    //DB定義図_220905
    [Table("F090_ドキュメント管理ファイル")]
    [EntityAttribute(TableName = "F090_ドキュメント管理ファイル")]
    public class F090_ドキュメント管理ファイル : BaseEntity
    {
        public long ドキュメントNO { get; set; } = 0;
        public string 会社コード { get; set; } = String.Empty;
        public string 工事依頼No { get; set; } = String.Empty;
        public string 工事依頼No枝番 { get; set; } = String.Empty;
        public string 添付元 { get; set; } = String.Empty;
        public string 添付種類 { get; set; } = String.Empty;
        public string その他帳票名 { get; set; } = String.Empty;
        public string ファイルパス { get; set; } = String.Empty;
        public string 物理ファイル名 { get; set; } = String.Empty;
        public string 添付元ファイル名 { get; set; } = String.Empty;
        public string 契約NO { get; set; } = String.Empty;
        public int? 履歴NO { get; set; } = 0;
        public int? 明細NO { get; set; } = 0;
        public string 契約年月 { get; set; } = String.Empty;
        public int? 契約年月明細NO { get; set; } = 0;
        public string 予定年月 { get; set; } = String.Empty;
        public string 契約書管理NO { get; set; } = String.Empty;
        public string 請求書管理NO { get; set; } = String.Empty;
        public string 枝番 { get; set; } = String.Empty;
        public string 取引先コード { get; set; } = String.Empty;
        public string 取引先コード枝番 { get; set; } = String.Empty;
        public int? 検収回数 { get; set; } = 0;
        public string 請求書NO { get; set; } = String.Empty;
        public string 日付 { get; set; } = String.Empty;
        public string 備考1 { get; set; } = String.Empty;
        public string 備考2 { get; set; } = String.Empty;
        public int? Ｗ参照区分 { get; set; } = 0;
        public int? Ｔ参照区分 { get; set; } = 0;
        public int? 発注稟議添付 { get; set; } = 0;
        public string 稟議申請ファイル名 { get; set; } = String.Empty;
        public int? 削除区分 { get; set; } = 0;
        [DatabaseGenerated(DatabaseGeneratedOption.Computed)]
        public byte[] VERSION { get; set; }
        public string 物件コード { get; set; } = String.Empty;
        [NotMapped()]
        public long 掲示ID { get; set; }
        [NotMapped()]
        public int ファイルNo { get; set; }
        [NotMapped()]
        public int 添付NO { get; set; }
    }

}
