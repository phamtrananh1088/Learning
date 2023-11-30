using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace WinMacOs.Models.Manager.Canvas
{
    /// <summary>
    /// Menu.
    /// </summary>
    public class MenuModel
    {
        /// <summary>
        ///Name.
        /// </summary>
        public string Name { get; set; }

        [UIHint("MenuItems")]
        public List<MenuItemModel> MenuItems { get; set; }
    }
}