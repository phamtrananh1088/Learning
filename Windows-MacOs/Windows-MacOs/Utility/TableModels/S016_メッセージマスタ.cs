using System;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using WinMacOs.Utility.SystemModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Utility.TableModels
{
	[Table("S016_メッセージマスタ")]
	[EntityAttribute(TableName = "S016_メッセージマスタ")]
	public class S016_メッセージマスタ : BaseEntity
	{
		[Key]
		public string メッセージコード { get; set; }
		public string メッセージ { get; set; }
		public short? 削除区分 { get; set; }
		[DatabaseGenerated(DatabaseGeneratedOption.Computed)]
		public byte[] VERSION { get; set; }		
	}
}
