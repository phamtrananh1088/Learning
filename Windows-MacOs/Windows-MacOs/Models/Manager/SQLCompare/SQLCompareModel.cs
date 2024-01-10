using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace WinMacOs.Models.Manager.SQLCompare
{
    /// <summary>
    /// SQLCompare.
    /// </summary>
    public class SQLCompareModel
    {
        public Step Step { get; set; }
        /// <summary>
        ///SQLTable.
        /// </summary>
        [UIHint("SQLTables")]
        public List<SQLTableModel> SQLTables { get; set; }

        public string Source { get; set; }
        public string Target { get; set; }
        public string SQLScript { get; set; }

    }
}