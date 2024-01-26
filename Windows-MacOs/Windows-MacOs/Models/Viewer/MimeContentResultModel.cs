using System;
using System.ComponentModel;
using System.Web.Mvc;

namespace WinMacOs.Models.Viewer
{
    /// <summary>
    /// File.
    /// </summary>
    public class MimeContentResultModel: ContentResult
    {
        /// <summary>
        /// File Name.
        /// </summary>
        public string FileName { get; set; }

        /// <summary>
        /// Size.
        /// </summary>
        public long FileSize { get; set; }

        public MimeContentResultModel()
        {

        }
    }
}