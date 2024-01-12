using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;

namespace WinMacOs.Models.Manager.SQLCompare
{
    /// <summary>
    /// SQLIndexModel.
    /// </summary>
    public class SQLIndexModel
    {
        [Required]
        [Key]
        public string IndexName { get; set; }

        public string IndexColumn { get; set; }
        
        public string IncludedColumn { get; set; }

        public string IndexType { get; set; }

        public bool IsUnique { get; set; }

    }
}