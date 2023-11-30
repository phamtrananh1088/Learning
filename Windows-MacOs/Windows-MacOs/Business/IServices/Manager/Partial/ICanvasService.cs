using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.Models;
using WinMacOs.Models.Manager.Canvas;
using WinMacOs.Utility.DomainModels;

namespace WinMacOs.Business.IServices.Manager
{
    public partial interface ICanvasService
    {
        Task<CanvasModel> GetCanvasModel();
        Task<WebResponseContent> DrawLine();
    }
}
