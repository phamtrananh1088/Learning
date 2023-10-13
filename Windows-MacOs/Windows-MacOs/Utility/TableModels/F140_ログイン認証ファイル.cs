using System;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations.Schema;
using WinMacOs.Utility.SystemModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Utility.TableModels
{
    [Table("F140_ログイン認証ファイル")]
    [EntityAttribute(TableName = "F140_ログイン認証ファイル")]
    public class F140_ログイン認証ファイル : BaseEntity
    {
        public int 連番 { get; set; }
        public string ログインID { get; set; }
        public string Token { get; set; }
        public int 削除区分 { get; set; } = 0;
        [DatabaseGenerated(DatabaseGeneratedOption.Computed)]
        public byte[] VERSION { get; set; }
    }
}
