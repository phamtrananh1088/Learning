using System;
using System.ComponentModel;


namespace WinMacOs.Models
{
    /// <summary>
    /// 注文入力.
    /// </summary>
    public class NoteModel
    {
        /// <summary>
        /// Note.
        /// </summary>
        [DisplayName("Note")]
        public string Data { get; set; }

        /// <summary>
        /// IsCreateNew.
        /// </summary>
        public bool IsCreateNew { get; set; }

        /// <summary>
        /// FileName.
        /// </summary>
        public string FileName { get; set; }

        /// <summary>
        /// CreateDate.
        /// </summary>
        public DateTime CreateDate { get; set; }
        /// <summary>
        /// Level.
        /// </summary>
        [DisplayName("Level")]
        public string Level { get; set; }
    }
}