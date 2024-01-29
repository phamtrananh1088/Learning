using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;

namespace WinMacOs.Models.Manager.SQLCompare
{
    /// <summary>
    /// SQLTableModel.
    /// </summary>
    public class SQLTableModel
    {
        public string Type
        {
            get
            {
                return "U ";
            }
        }
        [Required]
        [Key]
        [DisplayName("SchemaName")]
        public string SchemaName { get; set; }
        [Required]
        [Key]
        [DisplayName("Name")]
        public string TableName { get; set; }

        public bool Selected { get; set; }
        public List<string> Keys { get; set; }

        public List<SQLIndexModel> SQLIndexs { get; set; }

        public List<SQLDefaultConstraint> SQLDefaultConstraint { get; set; }

        [DisplayName("Status")]
        public Status Status { get; set; }
        /// <summary>
        ///SQLColumns.
        /// </summary>
        [UIHint("SQLColumns")]
        public List<SQLColumnModel> SQLColumns { get; set; }
    }
}