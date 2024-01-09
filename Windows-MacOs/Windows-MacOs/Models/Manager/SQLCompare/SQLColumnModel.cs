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
        
        [DisplayName("Type")]
        public SQLTypeModel SQLTypeModel { get; set; }
    }
}