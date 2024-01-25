using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;

namespace WinMacOs.Models.Manager.SQLCompare
{
    /// <summary>
    /// SQLDependencyModel.
    /// </summary>
    public class SQLDependencyModel
    {
        public string Type { get; set; }
        public string SchemaName { get; set; }
        public string Name { get; set; }
        public string DepType { get; set; }
        public string DepSchemaName { get; set; }
        public string DepName { get; set; }
    }
}