using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using WinMacOs.Utility.SystemModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Utility.TableModels
{
    //DB定義図_220905
    [Table("M003_社員マスタ")]
    [EntityAttribute(TableName = "M003_社員マスタ")]
    public class M003_社員マスタ : BaseEntity
    {
        public string 会社コード { get; set; } = String.Empty;
        [Key]
        public int 社員ID { get; set; } = 0;
        public string 社員コード { get; set; } = String.Empty;
        public string ログインID { get; set; } = String.Empty;

        public short? 統一ID区分 { get; set; } = 0;
        public int? ユーザ識別ID { get; set; } = 0;
        public string 利用用途区分 { get; set; } = String.Empty;
        public string 所属兼務区分 { get; set; } = String.Empty;
        public string 役職兼務区分 { get; set; } = String.Empty;
        public string 部門コード { get; set; } = String.Empty;
        public string 社員名 { get; set; } = String.Empty;

        [Display(Name = "社員名(姓)")]
        [Column("社員名(姓)", TypeName = "NVARCHAR(40)")]
        [Editable(true)]
        public string 社員名_姓 { get; set; } = String.Empty;

        [Display(Name = "社員名(名)")]
        [Column("社員名(名)", TypeName = "NVARCHAR(40)")]
        [Editable(true)]
        public string 社員名_名 { get; set; } = String.Empty;
        public string 社員名カナ { get; set; } = String.Empty;

        [Display(Name = "社員名カナ(姓)")]
        [Column("社員名カナ(姓)", TypeName = "NVARCHAR(40)")]
        [Editable(true)]
        public string 社員名カナ_姓 { get; set; } = String.Empty;

        [Display(Name = "社員名カナ(名)")]
        [Column("社員名カナ(名)", TypeName = "NVARCHAR(40)")]
        [Editable(true)]
        public string 社員名カナ_名 { get; set; } = String.Empty;
        public string 社員名アルファベット { get; set; } = String.Empty;

        [Display(Name = "社員名アルファベット(姓)")]
        [Column("社員名アルファベット(姓)", TypeName = "NVARCHAR(50)")]
        [Editable(true)]
        public string 社員名アルファベット_姓 { get; set; } = String.Empty;

        [Display(Name = "社員名アルファベット(名)")]
        [Column("社員名アルファベット(名)", TypeName = "NVARCHAR(50)")]
        [Editable(true)]
        public string 社員名アルファベット_名 { get; set; } = String.Empty;

        public string 表示社員名 { get; set; } = String.Empty;

        [Display(Name = "表示社員名(姓)")]
        [Column("表示社員名(姓)", TypeName = "NVARCHAR(40)")]
        [Editable(true)]
        public string 表示社員名_姓 { get; set; } = String.Empty;

        [Display(Name = "表示社員名(名)")]
        [Column("表示社員名(名)", TypeName = "NVARCHAR(40)")]
        [Editable(true)]
        public string 表示社員名_名 { get; set; } = String.Empty;
        public string 表示社員名カナ { get; set; } = String.Empty;

        [Display(Name = "表示社員名カナ(姓)")]
        [Column("表示社員名カナ(姓)", TypeName = "NVARCHAR(40)")]
        [Editable(true)]
        public string 表示社員名カナ_姓 { get; set; } = String.Empty;

        [Display(Name = "表示社員名カナ(名)")]
        [Column("表示社員名カナ(名)", TypeName = "NVARCHAR(40)")]
        [Editable(true)]
        public string 表示社員名カナ_名 { get; set; } = String.Empty;
        public string 表示社員名アルファベット { get; set; } = String.Empty;

        [Display(Name = "表示社員名アルファベット(姓)")]
        [Column("表示社員名アルファベット(姓)", TypeName = "NVARCHAR(50)")]
        [Editable(true)]
        public string 表示社員名アルファベット_姓 { get; set; } = String.Empty;

        [Display(Name = "表示社員名アルファベット(名)")]
        [Column("表示社員名アルファベット(名)", TypeName = "NVARCHAR(50)")]
        [Editable(true)]
        public string 表示社員名アルファベット_名 { get; set; } = String.Empty;
        public string 生年月日 { get; set; } = String.Empty;
        public string メールアドレス { get; set; } = String.Empty;
        public string パスワード { get; set; } = String.Empty;
        public string パスワード最終変更日 { get; set; } = String.Empty;
        public string 旧パスワード1 { get; set; } = String.Empty;
        public string 旧パスワード設定日1 { get; set; } = String.Empty;
        public string 旧パスワード2 { get; set; } = String.Empty;
        public string 旧パスワード設定日2 { get; set; } = String.Empty;
        public string 旧パスワード3 { get; set; } = String.Empty;
        public string 旧パスワード設定日3 { get; set; } = String.Empty;
        public string 旧パスワード4 { get; set; } = String.Empty;
        public string 旧パスワード設定日4 { get; set; } = String.Empty;
        public string 旧パスワード5 { get; set; } = String.Empty;
        public string 旧パスワード設定日5 { get; set; } = String.Empty;
        public string 社員支払銀行コード { get; set; } = String.Empty;
        public string 社員支払支店コード { get; set; } = String.Empty;
        public string 社員支払口座番号 { get; set; } = String.Empty;
        public string 社員支払名義ｶﾅ { get; set; } = String.Empty;
        public string 最終出勤日 { get; set; } = String.Empty;
        public string グループ入社日 { get; set; } = String.Empty;
        public string グループ退社日 { get; set; } = String.Empty;
        public string 事業会社入社日 { get; set; } = String.Empty;
        public string 事業会社退社日 { get; set; } = String.Empty;
        public string 転籍日 { get; set; } = String.Empty;
        public string 休職開始日 { get; set; } = String.Empty;
        public string 休職期日 { get; set; } = String.Empty;
        public string 休職フラグ { get; set; } = String.Empty;
        public short? 使用区分 { get; set; } = 0;
        public short? 削除区分 { get; set; } = 0;

        [DatabaseGenerated(DatabaseGeneratedOption.Computed)]
        [Column("VERSION", TypeName = "ROWVERSION")]
        public string VERSION { get; set; }
        public string 承認メール受信フラグ { get; set; } = "0";
        public string 経理処理会社区分 { get; set; } = "0";
        public string Token { get; set; }
    }
}
