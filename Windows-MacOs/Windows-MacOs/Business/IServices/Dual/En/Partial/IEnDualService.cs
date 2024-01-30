using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.Models;
using WinMacOs.Models.Dual.En;
using WinMacOs.Utility.DomainModels;

namespace WinMacOs.Business.IServices.Dual
{
    public partial interface IEnDualService
    {
        Task<EnDualModel> GetData();
    }
}
