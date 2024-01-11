using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;

namespace WinMacOs.Models.Manager.SQLCompare
{
    /// <summary>
    /// SQLDefaultConstraint.
    /// </summary>
    public class SQLDefaultConstraint
    {
        [Required]
        [Key]
        [DisplayName("ConstraintName")]
        public string ConstraintName { get; set; }

        [DisplayName("ColumnName")]
        public string ColumnName { get; set; }

        [DisplayName("Definition")]
        public string Definition { get; set; }
    }
}