using System;
using System.ComponentModel;


namespace WinMacOs.Models
{
    /// <summary>
    /// Directory.
    /// </summary>
    public class DirModel
    {
        /// <summary>
        /// Note.
        /// </summary>
        [DisplayName("Note")]
        public FileModel[] FileNames { get; set; }

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