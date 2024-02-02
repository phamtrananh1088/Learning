using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace WinMacOs.Models.Manager.NoteBookElementary
{
    /// <summary>
    /// NoteBookElementaryModel.
    /// </summary>
    public class NoteBookElementaryModel
    {
        /// <summary>
        ///New.
        /// </summary>
        public List<string> New { get; set; }

        /// <summary>
        ///Recent.
        /// </summary>
        public List<string> Recent { get; set; }

        public int CurrentPage { get; set; }
        public int TotalPage { get; set; }

    }
}