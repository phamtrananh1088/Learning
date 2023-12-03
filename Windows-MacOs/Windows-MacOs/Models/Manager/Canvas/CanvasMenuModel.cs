using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace WinMacOs.Models.Manager.Canvas
{
    /// <summary>
    /// Menu.
    /// </summary>
    public class CanvasMenuModel
    {
        [Required]
        [Key]
        public string MenuNo { get; set; }
        /// <summary>
        ///Name.
        /// </summary>
        public string MenuName { get; set; }

        public string[] ShortCut { get; set; }

        public string ParentMenuNo { get; set; }

        [UIHint("SubMenus")]
        public List<CanvasMenuModel> SubMenus { get; set; }

        public string MenuType { get; set; } = "";
    }
}