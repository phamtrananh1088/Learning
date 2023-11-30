using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace WinMacOs.Models.Manager.Canvas
{
    /// <summary>
    /// MenuItem.
    /// </summary>
    public class MenuItemModel
    {
        [Required]
        [Key]
        public string MenuItemNo { get; set; }
        /// <summary>
        ///Name.
        /// </summary>
        [Required]
        public string MenuItemName { get; set; }

        public string[] ShortCut { get; set; }

        public string ParentMenuItemNo { get; set; }

        [UIHint("MenuItems")]
        public List<MenuItemModel> MenuItems { get; set; }
    }
}