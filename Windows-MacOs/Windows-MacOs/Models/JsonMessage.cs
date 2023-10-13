using System;

namespace WinMacOs.Models
{
    /// <summary>
    /// Jsonメッセージ.
    /// </summary>
    [Serializable]
    public class JsonMessage
    {
        /// <summary>
        /// 実行結果.
        /// </summary>
        public bool success { get; set; }

        /// <summary>
        /// メッセージ.
        /// </summary>
        public string message { get; set; }

        /// <summary>
        /// 戻るデータ.
        /// </summary>
        public string data { get; set; }

        /// <summary>
        /// 画面遷移先.
        /// </summary>
        public string redirecturl { get; set; }

        /// <summary>
        /// 実行SQL.
        /// </summary>
        public string callback { get; set; }

        /// <summary>
        /// 請求ステータスモデル.
        /// </summary>
        public string requeststatus { get; set; }

        /// <summary>
        /// 警告メッセージ.
        /// </summary>
        public bool warning { get; set; }
    }
}
