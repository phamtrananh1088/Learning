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

        public string DBNameSource { get; set; }
        public string DBNameTarget { get; set; }
        public string SQLScriptSource { get; set; }
        public string SQLScriptTarget { get; set; }

    }
}