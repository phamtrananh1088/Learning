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
        public SQLTypeModel SQLTypeModel { 
            get
            {
                return string.IsNullOrEmpty(SQLTypeModelRaw) ? null : System.Text.Json.JsonSerializer.Deserialize<SQLTypeModel>(SQLTypeModelRaw);
            }
                }

        public string SQLTypeModelRaw { get; set; }
    }
}