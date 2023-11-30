using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Data;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using WinMacOs.ActionFilter;
using WinMacOs.Business.IServices.Manager;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.DataRepository.Utilities;
using WinMacOs.Models;
using WinMacOs.Models.Enums;
using WinMacOs.Models.Manager.Canvas;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.Extensions;
using WinMacOs.Utility.PInvoke.CommonModels;
using WinMacOs.Utility.PInvoke.Mpr;
using WinMacOs.Utility.SystemModels;
using WinMacOs.Utility.TableModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Controllers.Manager
 {
    [RoutePrefix("manager/canvas")]
    public partial class CanvasController
    {
        public CanvasController(
               ICanvasService loginService
              )
          : base(loginService)
        {
        }

        [Route("")]
        [Route("index")]
        public async Task<ActionResult> Index()
        {
            CanvasModel model = await Service.GetCanvasModel();
            return View(model);
        }

        #region ログイン
        [HttpPost, Route("DrawLine"), AllowAnonymous]
        public async Task<ActionResult> DrawLine()
        {
            return Json(await Service.DrawLine());
        }
        #endregion

    }
}
