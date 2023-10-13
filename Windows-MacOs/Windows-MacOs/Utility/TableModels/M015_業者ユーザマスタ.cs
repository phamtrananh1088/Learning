using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text;
using WinMacOs.Utility.SystemModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Utility.TableModels
{
    //DB定義図_220905
    [Table("M015_業者ユーザマスタ")]
    [EntityAttribute(TableName = "M015_業者ユーザマスタ")]
    public class M015_業者ユーザマスタ : BaseEntity
    {
        public string 会社コード { get; set; } = String.Empty;
        public string 業者コード { get; set; } = String.Empty;
        public string 業者コード枝番 { get; set; } = String.Empty;
        public string ユーザーＩＤ { get; set; } = String.Empty;
        public string ユーザー種別 { get; set; } = String.Empty;
        public string ユーザー名 { get; set; } = String.Empty;
        public string 部署等 { get; set; } = String.Empty;
        public string メールアドレス { get; set; } = String.Empty;
        public string 利用期間開始 { get; set; } = String.Empty;
        public string 利用期間終了 { get; set; } = String.Empty;
        public string パスワード { get; set; } = String.Empty;
        public string パスワード変換 { get; set; } = String.Empty;
        public string パスワード期限開始 { get; set; } = String.Empty;
        public string パスワード期限終了 { get; set; } = String.Empty;
        public string 親ＩＤ { get; set; } = String.Empty;
        public short? 新規区分 { get; set; } = 0;
        public short? 削除区分 { get; set; } = 0;
        [DatabaseGenerated(DatabaseGeneratedOption.Computed)]
        [Column("VERSION", TypeName = "ROWVERSION")]
        public byte[] VERSION { get; set; }
        public string ソルト値 { get; set; } = String.Empty;
        public string 旧パスワード変換1 { get; set; } = String.Empty;
        public string 旧パスワード設定日1 { get; set; } = String.Empty;
        public string 旧パスワード変換2 { get; set; } = String.Empty;
        public string 旧パスワード設定日2 { get; set; } = String.Empty;
        public string 旧パスワード変換3 { get; set; } = String.Empty;
        public string 旧パスワード設定日3 { get; set; } = String.Empty;
        public string 旧パスワード変換4 { get; set; } = String.Empty;
        public string 旧パスワード設定日4 { get; set; } = String.Empty;
        public string 旧パスワード変換5 { get; set; } = String.Empty;
        public string 旧パスワード設定日5 { get; set; } = String.Empty;
        public short? 全枝参照区分 { get; set; } = 0;
    }
}
