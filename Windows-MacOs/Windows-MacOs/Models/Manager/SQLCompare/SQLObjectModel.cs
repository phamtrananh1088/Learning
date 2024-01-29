using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;

namespace WinMacOs.Models.Manager.SQLCompare
{
    /// <summary>
    /// SQLObjectModel.
    /// </summary>
    public class SQLObjectModel
    {
        public string Type { get; set; }
        public string SchemaName { get; set; }
        public string Name { get; set; }
        
    }
}