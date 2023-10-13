using System;
using System.Collections.Generic;
using System.Text;

namespace WinMacOs.Utility.Utils
{
    /// <summary>
    /// 暗号化の鍵 (暗号)Key
    /// </summary>
    public class Secret
    {
        /// <summary>
        /// データベース暗号化key
        /// </summary>
        public string DB { get; set; }

        /// <summary>
        /// jwt暗号化key
        /// </summary>
        public string JWT { get; set; }

        /// <summary>
        /// クライアント側権限
        /// </summary>
        public string Audience { get; set; }

        /// <summary>
        /// 作成者
        /// </summary>
        public string Issuer { get; set; }

    }
}
