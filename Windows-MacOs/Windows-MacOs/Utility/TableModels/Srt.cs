using System;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations.Schema;
using WinMacOs.Utility.SystemModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Utility.TableModels
{
    /// <summary>
    /// SubRip Text.
    /// </summary>
    [Table("Srt")]
    [EntityAttribute(TableName = "Srt")]
    public class Srt : BaseEntity
    {
        public string JI_NO { get; set; }
        /// <summary>
        /// IsCreateNew.
        /// </summary>
        public bool IsCreateNew { get; set; }

        /// <summary>
        /// FileName.
        /// </summary>
        public string FileName { get; set; }

        /// <summary>
        /// CreateDate.
        /// </summary>
        public DateTime CreateDate { get; set; }
        /// <summary>
        /// Level.
        /// </summary>
        [DisplayName("Level")]
        public string Level { get; set; }
    }
}