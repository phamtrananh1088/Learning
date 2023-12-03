using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace WinMacOs.Models.Manager.Canvas
{
    /// <summary>
    /// Canvas.
    /// </summary>
    public class CanvasModel
    {
        /// <summary>
        ///Menu.
        /// </summary>
        [UIHint("Menus")]
        public List<CanvasMenuModel> Menus { get; set; }

    }
}