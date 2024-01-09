using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;

namespace WinMacOs.Models.Manager.SQLCompare
{
    /// <summary>
    /// SQLTypeModel.
    /// </summary>
    public class SQLTypeModel
    {
        [DisplayName("Name")]
        public string Name { get; set; }

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