using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.Models;
using WinMacOs.Utility.DomainModels;

namespace WinMacOs.Business.IServices.Manager
{
    public partial interface ICanvasService
    {
        Task<WebResponseContent> DrawLine();
    }
}
