using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;

namespace WinMacOs.Models.Manager.SQLCompare
{
    /// <summary>
    /// SQLColumnModel.
    /// </summary>
    public class SQLColumnModel
    {
        [Required]
        [Key]
        [DisplayName("ColumnName")]
        public string ColumnName { get; set; }

        [DisplayName("Status")]
        public Status Status { get; set; }
        
        [DisplayName("TypeName")]
        public string TypeName { get; set; }

        [DisplayName("MaxLength")]
        public int MaxLength { get; set; }

        [DisplayName("Precision")]
        public int Precision { get; set; }

        [DisplayName("Scale")]
        public int Scale { get; set; }

        [DisplayName("Allow Null")]
        public bool IsNull { get; set; }
    }
}