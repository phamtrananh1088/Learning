using System;
using System.Collections.Generic;
using System.Text;
using WinMacOs.Utility.SystemModels;

namespace WinMacOs.Utility.DomainModels
{
    public class MenuInfo: BaseEntity
    {
        public string MenuId { get; set; }
        //public string MenuName { get; set; }
        public string SubMenuId { get; set; }
        public string SubMenuName { get; set; }
    }
}
