using System;
using System.Collections.Generic;
using System.Text;

namespace WinMacOs.Utility.Utils
{
    public class EntityAttribute : Attribute
    {
        /// <summary>
        /// テーブル名称
        /// </summary>
        public string TableName { get; set; }
        /// <summary>
        /// サブテーブル
        /// </summary>
        public Type[] DetailTable { get; set; }
        /// <summary>
        /// データベース
        /// </summary>
        public string DBServer { get; set; }

        public Type ApiInput { get; set; }
        public Type ApiOutput { get; set; }
    }
}
