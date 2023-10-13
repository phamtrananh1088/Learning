using System;
using System.ComponentModel;


namespace WinMacOs.Models
{
    /// <summary>
    /// File.
    /// </summary>
    public class FileModel
    {
        /// <summary>
        /// File Name.
        /// </summary>
        [DisplayName("File Name")]
        public string FileName { get; set; }

        /// <summary>
        /// Size.
        /// </summary>
        [DisplayName("Size")]
        public long FileSize { get; set; }

        /// <summary>
        /// Created By.
        /// </summary
        [DisplayName("Created By")]
        public string CreatedBy { get; set; }

        /// <summary>
        /// CreateDate.
        /// </summary>
        public DateTime CreateDate { get; set; }

        /// <summary>
        /// Updated By.
        /// </summary
        [DisplayName("Updated By")]
        public string UpdatedBy { get; set; }


        /// <summary>
        /// Updated.
        /// </summary>
        [DisplayName("Updated")]
        public DateTime UpdatedDate { get; set; }
    }
}