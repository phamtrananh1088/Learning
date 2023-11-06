using System;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations.Schema;
using WinMacOs.Utility.SystemModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Utility.TableModels
{
    //DB定義図_220905
    [Table("F093_一時添付ファイル")]
    [EntityAttribute(TableName = "F093_一時添付ファイル")]
    public class F093_一時添付ファイル : BaseEntity
    {
        public int 添付NO { get; set; }

        public short 枝番 { get; set; }

        public string 添付元ファイル名 { get; set; } = String.Empty;

        public byte[] ファイルデータ { get; set; }

        public int? 削除区分 { get; set; } = 0;

        [DatabaseGenerated(DatabaseGeneratedOption.Computed)]
        public byte[] VERSION { get; set; }

    }
}
